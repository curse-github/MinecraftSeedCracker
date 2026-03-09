/*      */ package net.minecraft.server.level;
/*      */ 
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Queues;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.mojang.datafixers.DataFixer;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2LongMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*      */ import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
/*      */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*      */ import it.unimi.dsi.fastutil.longs.LongSet;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.nio.file.Path;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.CompletionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.function.BooleanSupplier;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.IntConsumer;
/*      */ import java.util.function.IntFunction;
/*      */ import java.util.function.IntSupplier;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*      */ import net.minecraft.network.protocol.game.ClientboundChunksBiomesPacket;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.network.ServerPlayerConnection;
/*      */ import net.minecraft.util.CsvOutput;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.StaticCache2D;
/*      */ import net.minecraft.util.TriState;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.datafix.DataFixTypes;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.util.thread.BlockableEventLoop;
/*      */ import net.minecraft.util.thread.ConsecutiveExecutor;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.TicketStorage;
/*      */ import net.minecraft.world.level.chunk.ChunkAccess;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
/*      */ import net.minecraft.world.level.chunk.LevelChunk;
/*      */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*      */ import net.minecraft.world.level.chunk.ProtoChunk;
/*      */ import net.minecraft.world.level.chunk.UpgradeData;
/*      */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*      */ import net.minecraft.world.level.chunk.status.ChunkStep;
/*      */ import net.minecraft.world.level.chunk.status.ChunkType;
/*      */ import net.minecraft.world.level.chunk.status.WorldGenContext;
/*      */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*      */ import net.minecraft.world.level.chunk.storage.SerializableChunkData;
/*      */ import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
/*      */ import net.minecraft.world.level.entity.ChunkStatusUpdateListener;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
/*      */ import net.minecraft.world.level.levelgen.RandomState;
/*      */ import net.minecraft.world.level.levelgen.structure.LegacyStructureDataHandler;
/*      */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*      */ import net.minecraft.world.level.storage.DimensionDataStorage;
/*      */ import net.minecraft.world.level.storage.LevelStorageSource;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ChunkMap
/*      */   extends SimpleRegionStorage
/*      */   implements ChunkHolder.PlayerProvider, GeneratingChunkMap
/*      */ {
/*  115 */   private static final ChunkResult<List<ChunkAccess>> UNLOADED_CHUNK_LIST_RESULT = ChunkResult.error("Unloaded chunks found in range");
/*  116 */   private static final CompletableFuture<ChunkResult<List<ChunkAccess>>> UNLOADED_CHUNK_LIST_FUTURE = CompletableFuture.completedFuture(UNLOADED_CHUNK_LIST_RESULT);
/*      */   
/*      */   private static final byte CHUNK_TYPE_REPLACEABLE = -1;
/*      */   private static final byte CHUNK_TYPE_UNKNOWN = 0;
/*      */   private static final byte CHUNK_TYPE_FULL = 1;
/*  121 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   private static final int CHUNK_SAVED_PER_TICK = 200;
/*      */   
/*      */   private static final int CHUNK_SAVED_EAGERLY_PER_TICK = 20;
/*      */   
/*      */   private static final int EAGER_CHUNK_SAVE_COOLDOWN_IN_MILLIS = 10000;
/*      */   
/*      */   private static final int MAX_ACTIVE_CHUNK_WRITES = 128;
/*      */   public static final int MIN_VIEW_DISTANCE = 2;
/*      */   public static final int MAX_VIEW_DISTANCE = 32;
/*  132 */   public static final int FORCED_TICKET_LEVEL = ChunkLevel.byStatus(FullChunkStatus.ENTITY_TICKING);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  137 */   private final Long2ObjectLinkedOpenHashMap<ChunkHolder> updatingChunkMap = new Long2ObjectLinkedOpenHashMap();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  142 */   private final Long2ObjectLinkedOpenHashMap<ChunkHolder> pendingUnloads = new Long2ObjectLinkedOpenHashMap();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  147 */   private final List<ChunkGenerationTask> pendingGenerationTasks = new ArrayList();
/*      */   
/*      */   private final ServerLevel level;
/*      */   
/*      */   private final ThreadedLevelLightEngine lightEngine;
/*      */   private final BlockableEventLoop<Runnable> mainThreadExecutor;
/*      */   private final RandomState randomState;
/*      */   private final ChunkGeneratorStructureState chunkGeneratorState;
/*      */   private final TicketStorage ticketStorage;
/*      */   private final PoiManager poiManager;
/*  157 */   private final LongSet toDrop = new LongOpenHashSet();
/*      */   
/*      */   private boolean modified;
/*      */   
/*      */   private final ChunkTaskDispatcher worldgenTaskDispatcher;
/*      */   
/*      */   private final ChunkTaskDispatcher lightTaskDispatcher;
/*      */   
/*      */   private final ChunkStatusUpdateListener chunkStatusListener;
/*      */   private final DistanceManager distanceManager;
/*      */   private final String storageName;
/*  168 */   private final PlayerMap playerMap = new PlayerMap();
/*  169 */   private final Int2ObjectMap<TrackedEntity> entityMap = new Int2ObjectOpenHashMap();
/*      */   
/*  171 */   private final Long2ByteMap chunkTypeCache = new Long2ByteOpenHashMap();
/*  172 */   private final Long2LongMap nextChunkSaveTime = new Long2LongOpenHashMap();
/*      */   
/*  174 */   private final LongSet chunksToEagerlySave = new LongLinkedOpenHashSet();
/*  175 */   private final Queue<Runnable> unloadQueue = Queues.newConcurrentLinkedQueue();
/*  176 */   private final AtomicInteger activeChunkWrites = new AtomicInteger();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int serverViewDistance;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final WorldGenContext worldGenContext;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ChunkMap(ServerLevel level, LevelStorageSource.LevelStorageAccess levelStorage, DataFixer dataFixer, StructureTemplateManager structureManager, Executor executor, BlockableEventLoop<Runnable> mainThreadExecutor, LightChunkGetter chunkGetter, ChunkGenerator generator, ChunkStatusUpdateListener chunkStatusListener, Supplier<DimensionDataStorage> overworldDataStorage, TicketStorage ticketStorage, int serverViewDistance, boolean syncWrites) {
/*  196 */     super(new RegionStorageInfo(levelStorage
/*  197 */           .getLevelId(), level.dimension(), "chunk"), levelStorage
/*  198 */         .getDimensionPath(level.dimension()).resolve("region"), dataFixer, syncWrites, DataFixTypes.CHUNK, 
/*      */ 
/*      */ 
/*      */         
/*  202 */         LegacyStructureDataHandler.getLegacyTagFixer(level.dimension(), overworldDataStorage, dataFixer));
/*      */     
/*  204 */     Path storageFolder = levelStorage.getDimensionPath(level.dimension());
/*  205 */     this.storageName = storageFolder.getFileName().toString();
/*  206 */     this.level = level;
/*  207 */     RegistryAccess registryAccess = level.registryAccess();
/*  208 */     long levelSeed = level.getSeed();
/*  209 */     if (generator instanceof NoiseBasedChunkGenerator) { NoiseBasedChunkGenerator noiseGenerator = (NoiseBasedChunkGenerator)generator;
/*  210 */       this.randomState = RandomState.create((NoiseGeneratorSettings)noiseGenerator.generatorSettings().value(), registryAccess.lookupOrThrow(Registries.NOISE), levelSeed); }
/*      */     
/*      */     else
/*      */     
/*  214 */     { this.randomState = RandomState.create(NoiseGeneratorSettings.dummy(), registryAccess.lookupOrThrow(Registries.NOISE), levelSeed); }
/*      */     
/*  216 */     this.chunkGeneratorState = generator.createState(registryAccess.lookupOrThrow(Registries.STRUCTURE_SET), this.randomState, levelSeed);
/*  217 */     this.mainThreadExecutor = mainThreadExecutor;
/*      */     
/*  219 */     ConsecutiveExecutor worldgen = new ConsecutiveExecutor(executor, "worldgen");
/*  220 */     this.chunkStatusListener = chunkStatusListener;
/*  221 */     ConsecutiveExecutor light = new ConsecutiveExecutor(executor, "light");
/*      */     
/*  223 */     this.worldgenTaskDispatcher = new ChunkTaskDispatcher(worldgen, executor);
/*  224 */     this.lightTaskDispatcher = new ChunkTaskDispatcher(light, executor);
/*      */     
/*  226 */     this.lightEngine = new ThreadedLevelLightEngine(chunkGetter, this, this.level.dimensionType().hasSkyLight(), light, this.lightTaskDispatcher);
/*      */     
/*  228 */     this.distanceManager = new DistanceManager(ticketStorage, executor, mainThreadExecutor);
/*  229 */     this.ticketStorage = ticketStorage;
/*  230 */     this.poiManager = new PoiManager(new RegionStorageInfo(levelStorage.getLevelId(), level.dimension(), "poi"), storageFolder.resolve("poi"), dataFixer, syncWrites, registryAccess, level.getServer(), level);
/*      */     
/*  232 */     setServerViewDistance(serverViewDistance);
/*      */     
/*  234 */     this.worldGenContext = new WorldGenContext(level, generator, structureManager, this.lightEngine, mainThreadExecutor, this::setChunkUnsaved);
/*      */   }
/*      */ 
/*      */   
/*  238 */   private void setChunkUnsaved(ChunkPos chunkPos) { this.chunksToEagerlySave.add(chunkPos.toLong()); }
/*      */ 
/*      */ 
/*      */   
/*  242 */   protected ChunkGenerator generator() { return this.worldGenContext.generator(); }
/*      */ 
/*      */ 
/*      */   
/*  246 */   protected ChunkGeneratorStructureState generatorState() { return this.chunkGeneratorState; }
/*      */ 
/*      */ 
/*      */   
/*  250 */   protected RandomState randomState() { return this.randomState; }
/*      */ 
/*      */ 
/*      */   
/*  254 */   public boolean isChunkTracked(ServerPlayer player, int chunkX, int chunkZ) { return (player.getChunkTrackingView().contains(chunkX, chunkZ) && !player.connection.chunkSender.isPending(ChunkPos.asLong(chunkX, chunkZ))); }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isChunkOnTrackedBorder(ServerPlayer player, int chunkX, int chunkZ) {
/*  259 */     if (!isChunkTracked(player, chunkX, chunkZ)) {
/*  260 */       return false;
/*      */     }
/*  262 */     for (int dx = -1; dx <= 1; dx++) {
/*  263 */       for (int dz = -1; dz <= 1; dz++) {
/*  264 */         if (dx != 0 || dz != 0)
/*      */         {
/*      */           
/*  267 */           if (!isChunkTracked(player, chunkX + dx, chunkZ + dz))
/*  268 */             return true; 
/*      */         }
/*      */       } 
/*      */     } 
/*  272 */     return false;
/*      */   }
/*      */ 
/*      */   
/*  276 */   protected ThreadedLevelLightEngine getLightEngine() { return this.lightEngine; }
/*      */ 
/*      */ 
/*      */   
/*  280 */   public ChunkHolder getUpdatingChunkIfPresent(long key) { return (ChunkHolder)this.updatingChunkMap.get(key); }
/*      */ 
/*      */ 
/*      */   
/*  284 */   protected ChunkHolder getVisibleChunkIfPresent(long key) { return (ChunkHolder)this.visibleChunkMap.get(key); }
/*      */ 
/*      */   
/*      */   public ChunkStatus getLatestStatus(long key) {
/*  288 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(key);
/*  289 */     return (chunkHolder != null) ? chunkHolder.getLatestStatus() : null;
/*      */   }
/*      */   
/*      */   protected IntSupplier getChunkQueueLevel(long pos) {
/*  293 */     return () -> {
/*  294 */         ChunkHolder chunk = getVisibleChunkIfPresent(pos);
/*  295 */         if (chunk == null) {
/*  296 */           return ChunkTaskPriorityQueue.PRIORITY_LEVEL_COUNT - 1;
/*      */         }
/*  298 */         return Math.min(chunk.getQueueLevel(), ChunkTaskPriorityQueue.PRIORITY_LEVEL_COUNT - 1);
/*      */       };
/*      */   }
/*      */   
/*      */   public String getChunkDebugData(ChunkPos pos) {
/*  303 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(pos.toLong());
/*  304 */     if (chunkHolder == null) {
/*  305 */       return "null";
/*      */     }
/*  307 */     String result = "" + chunkHolder.getTicketLevel() + "\n";
/*  308 */     ChunkStatus status = chunkHolder.getLatestStatus();
/*  309 */     ChunkAccess chunk = chunkHolder.getLatestChunk();
/*  310 */     if (status != null) {
/*  311 */       result = result + "St: §" + result + status.getIndex() + "§r\n";
/*      */     }
/*  313 */     if (chunk != null) {
/*  314 */       result = result + "Ch: §" + result + chunk.getPersistedStatus().getIndex() + "§r\n";
/*      */     }
/*  316 */     FullChunkStatus fullStatus = chunkHolder.getFullStatus();
/*  317 */     result = result + result + String.valueOf('§') + fullStatus.ordinal();
/*  318 */     return result + "§r";
/*      */   }
/*      */   
/*      */   CompletableFuture<ChunkResult<List<ChunkAccess>>> getChunkRangeFuture(ChunkHolder centerChunk, int range, IntFunction<ChunkStatus> distanceToStatus) {
/*  322 */     if (range == 0) {
/*  323 */       ChunkStatus status = (ChunkStatus)distanceToStatus.apply(0);
/*  324 */       return centerChunk.scheduleChunkGenerationTask(status, this).thenApply(r -> r.map(List::of));
/*      */     } 
/*      */     
/*  327 */     int chunkCount = Mth.square(range * 2 + 1);
/*  328 */     List<CompletableFuture<ChunkResult<ChunkAccess>>> deps = new ArrayList<CompletableFuture<ChunkResult<ChunkAccess>>>(chunkCount);
/*  329 */     ChunkPos centerPos = centerChunk.getPos();
/*  330 */     for (int z = -range; z <= range; z++) {
/*  331 */       for (int x = -range; x <= range; x++) {
/*  332 */         int distance = Math.max(Math.abs(x), Math.abs(z));
/*  333 */         long chunkNode = ChunkPos.asLong(centerPos.x + x, centerPos.z + z);
/*  334 */         ChunkHolder chunk = getUpdatingChunkIfPresent(chunkNode);
/*  335 */         if (chunk == null) {
/*  336 */           return UNLOADED_CHUNK_LIST_FUTURE;
/*      */         }
/*  338 */         ChunkStatus depStatus = (ChunkStatus)distanceToStatus.apply(distance);
/*  339 */         deps.add(chunk.scheduleChunkGenerationTask(depStatus, this));
/*      */       } 
/*      */     } 
/*  342 */     return Util.sequence(deps).thenApply(chunkResults -> {
/*  343 */           List<ChunkAccess> chunks = new ArrayList<ChunkAccess>(chunkResults.size());
/*  344 */           for (ChunkResult<ChunkAccess> chunkResult : chunkResults) {
/*  345 */             if (chunkResult == null) {
/*  346 */               throw debugFuturesAndCreateReportedException(new IllegalStateException("At least one of the chunk futures were null"), "n/a");
/*      */             }
/*  348 */             ChunkAccess chunk = (ChunkAccess)chunkResult.orElse(null);
/*  349 */             if (chunk == null) {
/*  350 */               return UNLOADED_CHUNK_LIST_RESULT;
/*      */             }
/*  352 */             chunks.add(chunk);
/*      */           } 
/*  354 */           return ChunkResult.of(chunks);
/*      */         });
/*      */   }
/*      */   
/*      */   public ReportedException debugFuturesAndCreateReportedException(IllegalStateException exception, String details) {
/*  359 */     StringBuilder sb = new StringBuilder();
/*  360 */     Consumer<ChunkHolder> addToDebug = holder -> 
/*  361 */       holder.getAllFutures().forEach(());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  369 */     sb.append("Updating:").append(System.lineSeparator());
/*  370 */     this.updatingChunkMap.values().forEach(addToDebug);
/*      */     
/*  372 */     sb.append("Visible:").append(System.lineSeparator());
/*  373 */     this.visibleChunkMap.values().forEach(addToDebug);
/*      */     
/*  375 */     CrashReport report = CrashReport.forThrowable(exception, "Chunk loading");
/*  376 */     CrashReportCategory category = report.addCategory("Chunk loading");
/*  377 */     category.setDetail("Details", details);
/*  378 */     category.setDetail("Futures", sb);
/*  379 */     return new ReportedException(report);
/*      */   }
/*      */   
/*      */   public CompletableFuture<ChunkResult<LevelChunk>> prepareEntityTickingChunk(ChunkHolder chunk) {
/*  383 */     return getChunkRangeFuture(chunk, 2, distance -> ChunkStatus.FULL)
/*  384 */       .thenApply(chunkResult -> chunkResult.map(()));
/*      */   }
/*      */   
/*      */   private ChunkHolder updateChunkScheduling(long node, int level, ChunkHolder chunk, int oldLevel) {
/*  388 */     if (!ChunkLevel.isLoaded(oldLevel) && !ChunkLevel.isLoaded(level)) {
/*  389 */       return chunk;
/*      */     }
/*      */     
/*  392 */     if (chunk != null) {
/*  393 */       chunk.setTicketLevel(level);
/*      */     }
/*      */     
/*  396 */     if (chunk != null) {
/*  397 */       if (!ChunkLevel.isLoaded(level)) {
/*  398 */         this.toDrop.add(node);
/*      */       } else {
/*  400 */         this.toDrop.remove(node);
/*      */       } 
/*      */     }
/*      */     
/*  404 */     if (ChunkLevel.isLoaded(level) && 
/*  405 */       chunk == null) {
/*  406 */       chunk = (ChunkHolder)this.pendingUnloads.remove(node);
/*      */       
/*  408 */       if (chunk != null) {
/*  409 */         chunk.setTicketLevel(level);
/*      */       } else {
/*  411 */         chunk = new ChunkHolder(new ChunkPos(node), level, this.level, this.lightEngine, this::onLevelChange, this);
/*      */       } 
/*  413 */       this.updatingChunkMap.put(node, chunk);
/*  414 */       this.modified = true;
/*      */     } 
/*      */     
/*  417 */     return chunk;
/*      */   }
/*      */   
/*      */   private void onLevelChange(ChunkPos pos, IntSupplier oldLevel, int newLevel, IntConsumer setQueueLevel) {
/*  421 */     this.worldgenTaskDispatcher.onLevelChange(pos, oldLevel, newLevel, setQueueLevel);
/*  422 */     this.lightTaskDispatcher.onLevelChange(pos, oldLevel, newLevel, setQueueLevel);
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() throws IOException {
/*      */     try {
/*  428 */       this.worldgenTaskDispatcher.close();
/*  429 */       this.lightTaskDispatcher.close();
/*  430 */       this.poiManager.close();
/*      */     } finally {
/*  432 */       super.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void saveAllChunks(boolean flushStorage) {
/*  438 */     if (flushStorage) {
/*      */ 
/*      */ 
/*      */       
/*  442 */       List<ChunkHolder> chunksToSave = this.visibleChunkMap.values().stream().filter(ChunkHolder::wasAccessibleSinceLastSave).peek(ChunkHolder::refreshAccessibility).toList();
/*      */ 
/*      */       
/*  445 */       MutableBoolean didWork = new MutableBoolean();
/*      */       do {
/*  447 */         didWork.setFalse();
/*  448 */         chunksToSave.stream()
/*  449 */           .map(chunk -> {
/*  450 */               Objects.requireNonNull(chunk); this.mainThreadExecutor.managedBlock(chunk::isReadyForSaving);
/*  451 */               return chunk.getLatestChunk();
/*      */             
/*  453 */             }).filter(chunkAccess -> (chunkAccess instanceof net.minecraft.world.level.chunk.ImposterProtoChunk || chunkAccess instanceof LevelChunk))
/*  454 */           .filter(this::save)
/*  455 */           .forEach(c -> didWork.setTrue());
/*  456 */       } while (didWork.isTrue());
/*      */       
/*  458 */       this.poiManager.flushAll();
/*  459 */       processUnloads(() -> true);
/*  460 */       synchronize(true).join();
/*      */     } else {
/*      */       
/*  463 */       this.nextChunkSaveTime.clear();
/*  464 */       long now = Util.getMillis();
/*  465 */       for (ObjectIterator objectIterator = this.visibleChunkMap.values().iterator(); objectIterator.hasNext(); ) { ChunkHolder chunk = (ChunkHolder)objectIterator.next();
/*  466 */         saveChunkIfNeeded(chunk, now); }
/*      */     
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void tick(BooleanSupplier haveTime) {
/*  472 */     ProfilerFiller profiler = Profiler.get();
/*  473 */     profiler.push("poi");
/*  474 */     this.poiManager.tick(haveTime);
/*  475 */     profiler.popPush("chunk_unload");
/*  476 */     if (!this.level.noSave()) {
/*  477 */       processUnloads(haveTime);
/*      */     }
/*  479 */     profiler.pop();
/*      */   }
/*      */   
/*      */   public boolean hasWork() {
/*  483 */     return (this.lightEngine.hasLightWork() || 
/*  484 */       !this.pendingUnloads.isEmpty() || 
/*  485 */       !this.updatingChunkMap.isEmpty() || this.poiManager
/*  486 */       .hasWork() || 
/*  487 */       !this.toDrop.isEmpty() || 
/*  488 */       !this.unloadQueue.isEmpty() || this.worldgenTaskDispatcher
/*  489 */       .hasWork() || this.lightTaskDispatcher
/*  490 */       .hasWork() || this.distanceManager
/*  491 */       .hasTickets());
/*      */   }
/*      */   
/*      */   private void processUnloads(BooleanSupplier haveTime) {
/*  495 */     LongIterator iterator = this.toDrop.iterator();
/*  496 */     while (iterator.hasNext()) {
/*  497 */       long pos = iterator.nextLong();
/*  498 */       ChunkHolder chunkHolder = (ChunkHolder)this.updatingChunkMap.get(pos);
/*  499 */       if (chunkHolder != null) {
/*  500 */         this.updatingChunkMap.remove(pos);
/*  501 */         this.pendingUnloads.put(pos, chunkHolder);
/*  502 */         this.modified = true;
/*  503 */         scheduleUnload(pos, chunkHolder);
/*      */       } 
/*  505 */       iterator.remove();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  510 */     int minimalNumberOfChunksToProcess = Math.max(0, this.unloadQueue.size() - 2000); Runnable unloadTask;
/*  511 */     while ((minimalNumberOfChunksToProcess > 0 || haveTime.getAsBoolean()) && (unloadTask = (Runnable)this.unloadQueue.poll()) != null) {
/*  512 */       minimalNumberOfChunksToProcess--;
/*      */       
/*  514 */       unloadTask.run();
/*      */     } 
/*      */     
/*  517 */     saveChunksEagerly(haveTime);
/*      */   }
/*      */ 
/*      */   
/*      */   private void saveChunksEagerly(BooleanSupplier haveTime) {
/*  522 */     long now = Util.getMillis();
/*  523 */     int eagerlySavedCount = 0;
/*  524 */     LongIterator iterator = this.chunksToEagerlySave.iterator();
/*  525 */     while (eagerlySavedCount < 20 && this.activeChunkWrites
/*      */       
/*  527 */       .get() < 128 && haveTime
/*  528 */       .getAsBoolean() && iterator
/*  529 */       .hasNext()) {
/*      */       
/*  531 */       long chunkPos = iterator.nextLong();
/*  532 */       ChunkHolder chunkHolder = (ChunkHolder)this.visibleChunkMap.get(chunkPos);
/*      */ 
/*      */       
/*  535 */       ChunkAccess latestChunk = (chunkHolder != null) ? chunkHolder.getLatestChunk() : null;
/*  536 */       if (latestChunk == null || !latestChunk.isUnsaved()) {
/*  537 */         iterator.remove();
/*      */         
/*      */         continue;
/*      */       } 
/*  541 */       if (saveChunkIfNeeded(chunkHolder, now)) {
/*  542 */         eagerlySavedCount++;
/*  543 */         iterator.remove();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void scheduleUnload(long pos, ChunkHolder chunkHolder) {
/*  549 */     CompletableFuture<?> saveSyncFuture = chunkHolder.getSaveSyncFuture();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  574 */     Objects.requireNonNull(this.unloadQueue); saveSyncFuture.thenRunAsync(() -> { CompletableFuture<?> currentFuture = chunkHolder.getSaveSyncFuture(); if (currentFuture != saveSyncFuture) { scheduleUnload(pos, chunkHolder); return; }  ChunkAccess chunk = chunkHolder.getLatestChunk(); if (this.pendingUnloads.remove(pos, chunkHolder) && chunk != null) { if (chunk instanceof LevelChunk) { LevelChunk levelChunk = (LevelChunk)chunk; levelChunk.setLoaded(false); }  save(chunk); if (chunk instanceof LevelChunk) { LevelChunk levelChunk = (LevelChunk)chunk; this.level.unload(levelChunk); }  this.lightEngine.updateChunkStatus(chunk.getPos()); this.lightEngine.tryScheduleUpdate(); this.nextChunkSaveTime.remove(chunk.getPos().toLong()); }  }this.unloadQueue::add).whenComplete((ignored, throwable) -> {
/*  575 */           if (throwable != null) {
/*  576 */             LOGGER.error("Failed to save chunk {}", chunkHolder.getPos(), throwable);
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   protected boolean promoteChunkMap() {
/*  582 */     if (!this.modified) {
/*  583 */       return false;
/*      */     }
/*      */     
/*  586 */     this.visibleChunkMap = this.updatingChunkMap.clone();
/*  587 */     this.modified = false;
/*  588 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CompletableFuture<ChunkAccess> scheduleChunkLoad(ChunkPos pos) {
/*  596 */     CompletableFuture<Optional<SerializableChunkData>> chunkDataFuture = readChunk(pos).thenApplyAsync(chunkData -> chunkData.map(()), 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  602 */         Util.backgroundExecutor().forName("parseChunk"));
/*  603 */     CompletableFuture<?> poiFuture = this.poiManager.prefetch(pos);
/*      */     
/*  605 */     return chunkDataFuture.thenCombine(poiFuture, (chunkData, ignored) -> chunkData)
/*  606 */       .thenApplyAsync(chunkData -> {
/*  607 */           Profiler.get().incrementCounter("chunkLoad");
/*  608 */           if (chunkData.isPresent()) {
/*  609 */             ProtoChunk protoChunk = ((SerializableChunkData)chunkData.get()).read(this.level, this.poiManager, storageInfo(), pos);
/*  610 */             markPosition(pos, protoChunk.getPersistedStatus().getChunkType());
/*  611 */             return protoChunk;
/*      */           } 
/*  613 */           return createEmptyChunk(pos);
/*      */         
/*  615 */         }this.mainThreadExecutor).exceptionallyAsync(throwable -> handleChunkLoadFailure(throwable, pos), this.mainThreadExecutor);
/*      */   }
/*      */   
/*      */   private ChunkAccess handleChunkLoadFailure(Throwable throwable, ChunkPos pos) {
/*  619 */     CompletionException e = (CompletionException)throwable; Throwable unwrapped = (throwable instanceof CompletionException) ? e.getCause() : throwable;
/*  620 */     ReportedException e = (ReportedException)unwrapped; Throwable cause = (unwrapped instanceof ReportedException) ? e.getCause() : unwrapped;
/*  621 */     boolean alwaysThrow = cause instanceof Error;
/*  622 */     boolean ioException = (cause instanceof IOException || cause instanceof net.minecraft.nbt.NbtException);
/*      */     
/*  624 */     if (!alwaysThrow) { if (!ioException); }
/*  625 */     else { CrashReport report = CrashReport.forThrowable(throwable, "Exception loading chunk");
/*  626 */       CrashReportCategory chunkBeingLoaded = report.addCategory("Chunk being loaded");
/*  627 */       chunkBeingLoaded.setDetail("pos", pos);
/*  628 */       markPositionReplaceable(pos);
/*  629 */       throw new ReportedException(report); }
/*      */     
/*  631 */     this.level.getServer().reportChunkLoadFailure(cause, storageInfo(), pos);
/*  632 */     return createEmptyChunk(pos);
/*      */   }
/*      */   
/*      */   private ChunkAccess createEmptyChunk(ChunkPos pos) {
/*  636 */     markPositionReplaceable(pos);
/*  637 */     return new ProtoChunk(pos, UpgradeData.EMPTY, this.level, this.level.palettedContainerFactory(), null);
/*      */   }
/*      */ 
/*      */   
/*  641 */   private void markPositionReplaceable(ChunkPos pos) { this.chunkTypeCache.put(pos.toLong(), (byte)-1); }
/*      */ 
/*      */ 
/*      */   
/*  645 */   private byte markPosition(ChunkPos pos, ChunkType type) { return this.chunkTypeCache.put(pos.toLong(), (type == ChunkType.PROTOCHUNK) ? -1 : 1); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GenerationChunkHolder acquireGeneration(long chunkNode) {
/*  655 */     ChunkHolder chunkHolder = (ChunkHolder)this.updatingChunkMap.get(chunkNode);
/*  656 */     chunkHolder.increaseGenerationRefCount();
/*  657 */     return chunkHolder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  665 */   public void releaseGeneration(GenerationChunkHolder chunkHolder) { chunkHolder.decreaseGenerationRefCount(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompletableFuture<ChunkAccess> applyStep(GenerationChunkHolder chunkHolder, ChunkStep step, StaticCache2D<GenerationChunkHolder> cache) {
/*  673 */     ChunkPos pos = chunkHolder.getPos();
/*  674 */     if (step.targetStatus() == ChunkStatus.EMPTY) {
/*  675 */       return scheduleChunkLoad(pos);
/*      */     }
/*      */     
/*      */     try {
/*  679 */       GenerationChunkHolder holder = (GenerationChunkHolder)cache.get(pos.x, pos.z);
/*  680 */       ChunkAccess centerChunk = holder.getChunkIfPresentUnchecked(step.targetStatus().getParent());
/*  681 */       if (centerChunk == null) {
/*  682 */         throw new IllegalStateException("Parent chunk missing");
/*      */       }
/*  684 */       return step.apply(this.worldGenContext, cache, centerChunk);
/*  685 */     } catch (Exception e) {
/*  686 */       e.getStackTrace();
/*  687 */       CrashReport report = CrashReport.forThrowable(e, "Exception generating new chunk");
/*  688 */       CrashReportCategory category = report.addCategory("Chunk to be generated");
/*      */       
/*  690 */       category.setDetail("Status being generated", () -> step.targetStatus().getName());
/*  691 */       category.setDetail("Location", String.format(Locale.ROOT, "%d,%d", new Object[] { Integer.valueOf(pos.x), Integer.valueOf(pos.z) }));
/*  692 */       category.setDetail("Position hash", Long.valueOf(ChunkPos.asLong(pos.x, pos.z)));
/*  693 */       category.setDetail("Generator", generator());
/*      */ 
/*      */       
/*  696 */       this.mainThreadExecutor.execute(() -> {
/*  697 */             throw new ReportedException(report);
/*      */           });
/*  699 */       throw new ReportedException(report);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ChunkGenerationTask scheduleGenerationTask(ChunkStatus targetStatus, ChunkPos pos) {
/*  708 */     ChunkGenerationTask task = ChunkGenerationTask.create(this, targetStatus, pos);
/*  709 */     this.pendingGenerationTasks.add(task);
/*  710 */     return task;
/*      */   }
/*      */   
/*      */   private void runGenerationTask(ChunkGenerationTask task) {
/*  714 */     GenerationChunkHolder chunk = task.getCenter();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  721 */     Objects.requireNonNull(chunk); this.worldgenTaskDispatcher.submit(() -> { CompletableFuture<?> future = task.runUntilWait(); if (future == null) return;  future.thenRun(()); }chunk.getPos().toLong(), chunk::getQueueLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void runGenerationTasks() throws IOException {
/*  729 */     this.pendingGenerationTasks.forEach(this::runGenerationTask);
/*  730 */     this.pendingGenerationTasks.clear();
/*      */   }
/*      */   
/*      */   public CompletableFuture<ChunkResult<LevelChunk>> prepareTickingChunk(ChunkHolder chunk) {
/*  734 */     CompletableFuture<ChunkResult<List<ChunkAccess>>> future = getChunkRangeFuture(chunk, 1, distance -> ChunkStatus.FULL);
/*      */     
/*  736 */     return future.thenApplyAsync(listResult -> listResult.map(()), this.mainThreadExecutor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void onChunkReadyToSend(ChunkHolder chunkHolder, LevelChunk chunk) {
/*  751 */     ChunkPos chunkPos = chunk.getPos();
/*  752 */     for (ServerPlayer player : this.playerMap.getAllPlayers()) {
/*  753 */       if (player.getChunkTrackingView().contains(chunkPos)) {
/*  754 */         markChunkPendingToSend(player, chunk);
/*      */       }
/*      */     } 
/*  757 */     this.level.getChunkSource().onChunkReadyToSend(chunkHolder);
/*  758 */     this.level.debugSynchronizers().registerChunk(chunk);
/*      */   }
/*      */   
/*      */   public CompletableFuture<ChunkResult<LevelChunk>> prepareAccessibleChunk(ChunkHolder chunk) {
/*  762 */     return getChunkRangeFuture(chunk, 1, ChunkLevel::getStatusAroundFullChunk)
/*  763 */       .thenApply(chunkResult -> chunkResult.map(()));
/*      */   }
/*      */   
/*      */   Stream<ChunkHolder> allChunksWithAtLeastStatus(ChunkStatus status) {
/*  767 */     int level = ChunkLevel.byStatus(status);
/*  768 */     return this.visibleChunkMap.values().stream().filter(chunk -> (chunk.getTicketLevel() <= level));
/*      */   }
/*      */   
/*      */   private boolean saveChunkIfNeeded(ChunkHolder chunk, long now) {
/*  772 */     if (!chunk.wasAccessibleSinceLastSave() || !chunk.isReadyForSaving()) {
/*  773 */       return false;
/*      */     }
/*      */     
/*  776 */     ChunkAccess chunkAccess = chunk.getLatestChunk();
/*  777 */     if (chunkAccess instanceof net.minecraft.world.level.chunk.ImposterProtoChunk || chunkAccess instanceof LevelChunk) {
/*  778 */       if (!chunkAccess.isUnsaved()) {
/*  779 */         return false;
/*      */       }
/*  781 */       long chunkPos = chunkAccess.getPos().toLong();
/*  782 */       long nextSaveTime = this.nextChunkSaveTime.getOrDefault(chunkPos, -1L);
/*  783 */       if (now < nextSaveTime) {
/*  784 */         return false;
/*      */       }
/*  786 */       boolean saved = save(chunkAccess);
/*  787 */       chunk.refreshAccessibility();
/*  788 */       if (saved) {
/*  789 */         this.nextChunkSaveTime.put(chunkPos, now + 10000L);
/*      */       }
/*  791 */       return saved;
/*      */     } 
/*  793 */     return false;
/*      */   }
/*      */   
/*      */   private boolean save(ChunkAccess chunk) {
/*  797 */     this.poiManager.flush(chunk.getPos());
/*      */     
/*  799 */     if (!chunk.tryMarkSaved()) {
/*  800 */       return false;
/*      */     }
/*      */     
/*  803 */     ChunkPos pos = chunk.getPos();
/*      */     try {
/*  805 */       ChunkStatus status = chunk.getPersistedStatus();
/*      */       
/*  807 */       if (status.getChunkType() != ChunkType.LEVELCHUNK) {
/*  808 */         if (isExistingChunkFull(pos))
/*      */         {
/*  810 */           return false;
/*      */         }
/*      */ 
/*      */         
/*  814 */         if (status == ChunkStatus.EMPTY && chunk.getAllStarts().values().stream().noneMatch(StructureStart::isValid)) {
/*  815 */           return false;
/*      */         }
/*      */       } 
/*      */       
/*  819 */       Profiler.get().incrementCounter("chunkSave");
/*  820 */       this.activeChunkWrites.incrementAndGet();
/*  821 */       SerializableChunkData data = SerializableChunkData.copyOf(this.level, chunk);
/*  822 */       Objects.requireNonNull(data); CompletableFuture<CompoundTag> encodedData = CompletableFuture.supplyAsync(data::write, Util.backgroundExecutor());
/*  823 */       Objects.requireNonNull(encodedData); write(pos, encodedData::join).handle((ignored, throwable) -> {
/*  824 */             if (throwable != null) {
/*  825 */               this.level.getServer().reportChunkSaveFailure(throwable, storageInfo(), pos);
/*      */             }
/*  827 */             this.activeChunkWrites.decrementAndGet();
/*  828 */             return null;
/*      */           });
/*  830 */       markPosition(pos, status.getChunkType());
/*  831 */       return true;
/*  832 */     } catch (Exception e) {
/*  833 */       this.level.getServer().reportChunkSaveFailure(e, storageInfo(), pos);
/*      */       
/*  835 */       return false;
/*      */     } 
/*      */   } private boolean isExistingChunkFull(ChunkPos pos) {
/*      */     CompoundTag currentTag;
/*  839 */     byte cachedChunkType = this.chunkTypeCache.get(pos.toLong());
/*  840 */     if (cachedChunkType != 0) {
/*  841 */       return (cachedChunkType == 1);
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*  846 */       currentTag = (CompoundTag)((Optional)readChunk(pos).join()).orElse(null);
/*  847 */       if (currentTag == null) {
/*  848 */         markPositionReplaceable(pos);
/*  849 */         return false;
/*      */       } 
/*  851 */     } catch (Exception e) {
/*  852 */       LOGGER.error("Failed to read chunk {}", pos, e);
/*  853 */       markPositionReplaceable(pos);
/*  854 */       return false;
/*      */     } 
/*      */     
/*  857 */     ChunkType chunkType = SerializableChunkData.getChunkStatusFromTag(currentTag).getChunkType();
/*  858 */     return (markPosition(pos, chunkType) == 1);
/*      */   }
/*      */   
/*      */   protected void setServerViewDistance(int newViewDistance) {
/*  862 */     int actualNewDistance = Mth.clamp(newViewDistance, 2, 32);
/*  863 */     if (actualNewDistance != this.serverViewDistance) {
/*  864 */       this.serverViewDistance = actualNewDistance;
/*  865 */       this.distanceManager.updatePlayerTickets(this.serverViewDistance);
/*  866 */       for (ServerPlayer player : this.playerMap.getAllPlayers()) {
/*  867 */         updateChunkTracking(player);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  873 */   private int getPlayerViewDistance(ServerPlayer player) { return Mth.clamp(player.requestedViewDistance(), 2, this.serverViewDistance); }
/*      */ 
/*      */   
/*      */   private void markChunkPendingToSend(ServerPlayer player, ChunkPos pos) {
/*  877 */     LevelChunk chunk = getChunkToSend(pos.toLong());
/*  878 */     if (chunk != null) {
/*  879 */       markChunkPendingToSend(player, chunk);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  884 */   private static void markChunkPendingToSend(ServerPlayer player, LevelChunk chunk) { player.connection.chunkSender.markChunkPendingToSend(chunk); }
/*      */ 
/*      */ 
/*      */   
/*  888 */   private static void dropChunk(ServerPlayer player, ChunkPos pos) { player.connection.chunkSender.dropChunk(player, pos); }
/*      */ 
/*      */   
/*      */   public LevelChunk getChunkToSend(long key) {
/*  892 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(key);
/*  893 */     if (chunkHolder == null) {
/*  894 */       return null;
/*      */     }
/*  896 */     return chunkHolder.getChunkToSend();
/*      */   }
/*      */ 
/*      */   
/*  900 */   public int size() { return this.visibleChunkMap.size(); }
/*      */ 
/*      */ 
/*      */   
/*  904 */   public DistanceManager getDistanceManager() { return this.distanceManager; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void dumpChunks(Writer output) throws IOException {
/*  925 */     CsvOutput csvOutput = CsvOutput.builder().addColumn("x").addColumn("z").addColumn("level").addColumn("in_memory").addColumn("status").addColumn("full_status").addColumn("accessible_ready").addColumn("ticking_ready").addColumn("entity_ticking_ready").addColumn("ticket").addColumn("spawning").addColumn("block_entity_count").addColumn("ticking_ticket").addColumn("ticking_level").addColumn("block_ticks").addColumn("fluid_ticks").build(output);
/*      */     
/*  927 */     for (ObjectBidirectionalIterator objectBidirectionalIterator = this.visibleChunkMap.long2ObjectEntrySet().iterator(); objectBidirectionalIterator.hasNext(); ) { Long2ObjectMap.Entry<ChunkHolder> entry = (Long2ObjectMap.Entry)objectBidirectionalIterator.next();
/*  928 */       long posKey = entry.getLongKey();
/*  929 */       ChunkPos pos = new ChunkPos(posKey);
/*  930 */       ChunkHolder holder = (ChunkHolder)entry.getValue();
/*  931 */       Optional<ChunkAccess> chunk = Optional.ofNullable(holder.getLatestChunk());
/*  932 */       Optional<LevelChunk> fullChunk = chunk.flatMap(chunkAccess -> (chunkAccess instanceof LevelChunk) ? Optional.of((LevelChunk)chunkAccess) : Optional.empty());
/*  933 */       csvOutput.writeRow(new Object[] { 
/*  934 */             Integer.valueOf(pos.x), 
/*  935 */             Integer.valueOf(pos.z), 
/*  936 */             Integer.valueOf(holder.getTicketLevel()), 
/*  937 */             Boolean.valueOf(chunk.isPresent()), chunk
/*  938 */             .map(ChunkAccess::getPersistedStatus).orElse(null), fullChunk
/*  939 */             .map(LevelChunk::getFullStatus).orElse(null), 
/*  940 */             printFuture(holder.getFullChunkFuture()), 
/*  941 */             printFuture(holder.getTickingChunkFuture()), 
/*  942 */             printFuture(holder.getEntityTickingChunkFuture()), this.ticketStorage
/*  943 */             .getTicketDebugString(posKey, false), 
/*  944 */             Boolean.valueOf(anyPlayerCloseEnoughForSpawning(pos)), fullChunk
/*  945 */             .map(c -> Integer.valueOf(c.getBlockEntities().size())).orElse(Integer.valueOf(0)), this.ticketStorage
/*  946 */             .getTicketDebugString(posKey, true), 
/*  947 */             Integer.valueOf(this.distanceManager.getChunkLevel(posKey, true)), fullChunk
/*  948 */             .map(levelChunk -> Integer.valueOf(levelChunk.getBlockTicks().count())).orElse(Integer.valueOf(0)), fullChunk
/*  949 */             .map(levelChunk -> Integer.valueOf(levelChunk.getFluidTicks().count())).orElse(Integer.valueOf(0)) }); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   private static String printFuture(CompletableFuture<ChunkResult<LevelChunk>> future) {
/*      */     try {
/*  956 */       ChunkResult<LevelChunk> result = (ChunkResult)future.getNow(null);
/*  957 */       if (result != null) {
/*  958 */         return result.isSuccess() ? "done" : "unloaded";
/*      */       }
/*  960 */       return "not completed";
/*      */     }
/*  962 */     catch (CompletionException e) {
/*  963 */       return "failed " + e.getCause().getMessage();
/*  964 */     } catch (CancellationException e) {
/*  965 */       return "cancelled";
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  970 */   private CompletableFuture<Optional<CompoundTag>> readChunk(ChunkPos pos) { return read(pos).thenApplyAsync(chunkTag -> chunkTag.map(this::upgradeChunkTag), Util.backgroundExecutor().forName("upgradeChunk")); }
/*      */ 
/*      */ 
/*      */   
/*  974 */   private CompoundTag upgradeChunkTag(CompoundTag tag) { return upgradeChunkTag(tag, -1, getChunkDataFixContextTag(this.level.dimension(), generator().getTypeNameForDataFixer())); }
/*      */ 
/*      */   
/*      */   public static CompoundTag getChunkDataFixContextTag(ResourceKey<Level> dimension, Optional<ResourceKey<MapCodec<? extends ChunkGenerator>>> generator) {
/*  978 */     CompoundTag contextTag = new CompoundTag();
/*  979 */     contextTag.putString("dimension", dimension.identifier().toString());
/*  980 */     generator.ifPresent(k -> contextTag.putString("generator", k.identifier().toString()));
/*  981 */     return contextTag;
/*      */   }
/*      */   
/*      */   void collectSpawningChunks(List<LevelChunk> output) {
/*  985 */     LongIterator spawnCandidateChunks = this.distanceManager.getSpawnCandidateChunks();
/*  986 */     while (spawnCandidateChunks.hasNext()) {
/*  987 */       ChunkHolder holder = (ChunkHolder)this.visibleChunkMap.get(spawnCandidateChunks.nextLong());
/*  988 */       if (holder == null) {
/*      */         continue;
/*      */       }
/*  991 */       LevelChunk chunk = holder.getTickingChunk();
/*  992 */       if (chunk == null) {
/*      */         continue;
/*      */       }
/*  995 */       if (anyPlayerCloseEnoughForSpawningInternal(holder.getPos())) {
/*  996 */         output.add(chunk);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   void forEachBlockTickingChunk(Consumer<LevelChunk> tickingChunkConsumer) {
/* 1002 */     this.distanceManager.forEachEntityTickingChunk(chunkPos -> {
/* 1003 */           ChunkHolder holder = (ChunkHolder)this.visibleChunkMap.get(chunkPos);
/* 1004 */           if (holder == null) {
/*      */             return;
/*      */           }
/* 1007 */           LevelChunk chunk = holder.getTickingChunk();
/* 1008 */           if (chunk == null) {
/*      */             return;
/*      */           }
/* 1011 */           tickingChunkConsumer.accept(chunk);
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean anyPlayerCloseEnoughForSpawning(ChunkPos pos) {
/* 1019 */     TriState triState = this.distanceManager.hasPlayersNearby(pos.toLong());
/* 1020 */     if (triState == TriState.DEFAULT) {
/* 1021 */       return anyPlayerCloseEnoughForSpawningInternal(pos);
/*      */     }
/* 1023 */     return triState.toBoolean(true);
/*      */   }
/*      */   
/*      */   boolean anyPlayerCloseEnoughTo(BlockPos pos, int maxDistance) {
/* 1027 */     Vec3 target = new Vec3(pos);
/* 1028 */     for (ServerPlayer player : this.playerMap.getAllPlayers()) {
/* 1029 */       if (playerIsCloseEnoughTo(player, target, maxDistance)) {
/* 1030 */         return true;
/*      */       }
/*      */     } 
/* 1033 */     return false;
/*      */   }
/*      */   
/*      */   private boolean anyPlayerCloseEnoughForSpawningInternal(ChunkPos pos) {
/* 1037 */     for (ServerPlayer player : this.playerMap.getAllPlayers()) {
/* 1038 */       if (playerIsCloseEnoughForSpawning(player, pos)) {
/* 1039 */         return true;
/*      */       }
/*      */     } 
/* 1042 */     return false;
/*      */   }
/*      */   
/*      */   public List<ServerPlayer> getPlayersCloseForSpawning(ChunkPos pos) {
/* 1046 */     long key = pos.toLong();
/*      */ 
/*      */     
/* 1049 */     if (!this.distanceManager.hasPlayersNearby(key).toBoolean(true)) {
/* 1050 */       return List.of();
/*      */     }
/*      */     
/* 1053 */     ImmutableList.Builder<ServerPlayer> builder = ImmutableList.builder();
/* 1054 */     for (ServerPlayer player : this.playerMap.getAllPlayers()) {
/* 1055 */       if (playerIsCloseEnoughForSpawning(player, pos)) {
/* 1056 */         builder.add(player);
/*      */       }
/*      */     } 
/* 1059 */     return builder.build();
/*      */   }
/*      */   
/*      */   private boolean playerIsCloseEnoughForSpawning(ServerPlayer player, ChunkPos pos) {
/* 1063 */     if (player.isSpectator()) {
/* 1064 */       return false;
/*      */     }
/* 1066 */     double distanceToChunk = euclideanDistanceSquared(pos, player.position());
/* 1067 */     return (distanceToChunk < 16384.0D);
/*      */   }
/*      */   
/*      */   private boolean playerIsCloseEnoughTo(ServerPlayer player, Vec3 pos, int maxDistance) {
/* 1071 */     if (player.isSpectator()) {
/* 1072 */       return false;
/*      */     }
/* 1074 */     double distanceToPos = player.position().distanceTo(pos);
/* 1075 */     return (distanceToPos < maxDistance);
/*      */   }
/*      */   
/*      */   private static double euclideanDistanceSquared(ChunkPos chunkPos, Vec3 pos) {
/* 1079 */     double xPos = SectionPos.sectionToBlockCoord(chunkPos.x, 8);
/* 1080 */     double zPos = SectionPos.sectionToBlockCoord(chunkPos.z, 8);
/*      */     
/* 1082 */     double xd = xPos - pos.x;
/* 1083 */     double zd = zPos - pos.z;
/*      */     
/* 1085 */     return xd * xd + zd * zd;
/*      */   }
/*      */ 
/*      */   
/* 1089 */   private boolean skipPlayer(ServerPlayer player) { return (player.isSpectator() && !((Boolean)this.level.getGameRules().get(GameRules.SPECTATORS_GENERATE_CHUNKS)).booleanValue()); }
/*      */ 
/*      */   
/*      */   void updatePlayerStatus(ServerPlayer player, boolean added) {
/* 1093 */     boolean ignored = skipPlayer(player);
/* 1094 */     boolean wasIgnored = this.playerMap.ignoredOrUnknown(player);
/* 1095 */     if (added) {
/* 1096 */       this.playerMap.addPlayer(player, ignored);
/* 1097 */       updatePlayerPos(player);
/*      */       
/* 1099 */       if (!ignored) {
/* 1100 */         this.distanceManager.addPlayer(SectionPos.of(player), player);
/*      */       }
/* 1102 */       player.setChunkTrackingView(ChunkTrackingView.EMPTY);
/* 1103 */       updateChunkTracking(player);
/*      */     } else {
/* 1105 */       SectionPos lastPos = player.getLastSectionPos();
/* 1106 */       this.playerMap.removePlayer(player);
/* 1107 */       if (!wasIgnored) {
/* 1108 */         this.distanceManager.removePlayer(lastPos, player);
/*      */       }
/* 1110 */       applyChunkTrackingView(player, ChunkTrackingView.EMPTY);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updatePlayerPos(ServerPlayer player) {
/* 1115 */     SectionPos pos = SectionPos.of(player);
/* 1116 */     player.setLastSectionPos(pos);
/*      */   }
/*      */   
/*      */   public void move(ServerPlayer player) {
/* 1120 */     for (ObjectIterator objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity trackedEntity = (TrackedEntity)objectIterator.next();
/* 1121 */       if (trackedEntity.entity == player) {
/* 1122 */         trackedEntity.updatePlayers(this.level.players()); continue;
/*      */       } 
/* 1124 */       trackedEntity.updatePlayer(player); }
/*      */ 
/*      */ 
/*      */     
/* 1128 */     SectionPos oldSection = player.getLastSectionPos();
/* 1129 */     SectionPos newSection = SectionPos.of(player);
/*      */     
/* 1131 */     boolean wasIgnored = this.playerMap.ignored(player);
/* 1132 */     boolean ignored = skipPlayer(player);
/* 1133 */     boolean positionChanged = (oldSection.asLong() != newSection.asLong());
/* 1134 */     if (positionChanged || wasIgnored != ignored) {
/* 1135 */       updatePlayerPos(player);
/*      */       
/* 1137 */       if (!wasIgnored) {
/* 1138 */         this.distanceManager.removePlayer(oldSection, player);
/*      */       }
/*      */       
/* 1141 */       if (!ignored) {
/* 1142 */         this.distanceManager.addPlayer(newSection, player);
/*      */       }
/*      */       
/* 1145 */       if (!wasIgnored && ignored) {
/* 1146 */         this.playerMap.ignorePlayer(player);
/*      */       }
/*      */       
/* 1149 */       if (wasIgnored && !ignored) {
/* 1150 */         this.playerMap.unIgnorePlayer(player);
/*      */       }
/*      */       
/* 1153 */       updateChunkTracking(player);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateChunkTracking(ServerPlayer player) {
/* 1158 */     ChunkPos chunkPos = player.chunkPosition();
/* 1159 */     int playerViewDistance = getPlayerViewDistance(player);
/* 1160 */     ChunkTrackingView chunkTrackingView = player.getChunkTrackingView(); if (chunkTrackingView instanceof ChunkTrackingView.Positioned) { ChunkTrackingView.Positioned view = (ChunkTrackingView.Positioned)chunkTrackingView; if (view.center().equals(chunkPos) && view.viewDistance() == playerViewDistance)
/*      */         return;  }
/*      */     
/* 1163 */     applyChunkTrackingView(player, ChunkTrackingView.of(chunkPos, playerViewDistance));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void applyChunkTrackingView(ServerPlayer player, ChunkTrackingView next) { // Byte code:
/*      */     //   0: aload_1
/*      */     //   1: invokevirtual level : ()Lnet/minecraft/server/level/ServerLevel;
/*      */     //   4: aload_0
/*      */     //   5: getfield level : Lnet/minecraft/server/level/ServerLevel;
/*      */     //   8: if_acmpeq -> 12
/*      */     //   11: return
/*      */     //   12: aload_1
/*      */     //   13: invokevirtual getChunkTrackingView : ()Lnet/minecraft/server/level/ChunkTrackingView;
/*      */     //   16: astore_3
/*      */     //   17: aload_2
/*      */     //   18: instanceof net/minecraft/server/level/ChunkTrackingView$Positioned
/*      */     //   21: ifeq -> 89
/*      */     //   24: aload_2
/*      */     //   25: checkcast net/minecraft/server/level/ChunkTrackingView$Positioned
/*      */     //   28: astore #4
/*      */     //   30: aload_3
/*      */     //   31: instanceof net/minecraft/server/level/ChunkTrackingView$Positioned
/*      */     //   34: ifeq -> 59
/*      */     //   37: aload_3
/*      */     //   38: checkcast net/minecraft/server/level/ChunkTrackingView$Positioned
/*      */     //   41: astore #5
/*      */     //   43: aload #5
/*      */     //   45: invokevirtual center : ()Lnet/minecraft/world/level/ChunkPos;
/*      */     //   48: aload #4
/*      */     //   50: invokevirtual center : ()Lnet/minecraft/world/level/ChunkPos;
/*      */     //   53: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */     //   56: ifne -> 89
/*      */     //   59: aload_1
/*      */     //   60: getfield connection : Lnet/minecraft/server/network/ServerGamePacketListenerImpl;
/*      */     //   63: new net/minecraft/network/protocol/game/ClientboundSetChunkCacheCenterPacket
/*      */     //   66: dup
/*      */     //   67: aload #4
/*      */     //   69: invokevirtual center : ()Lnet/minecraft/world/level/ChunkPos;
/*      */     //   72: getfield x : I
/*      */     //   75: aload #4
/*      */     //   77: invokevirtual center : ()Lnet/minecraft/world/level/ChunkPos;
/*      */     //   80: getfield z : I
/*      */     //   83: invokespecial <init> : (II)V
/*      */     //   86: invokevirtual send : (Lnet/minecraft/network/protocol/Packet;)V
/*      */     //   89: aload_3
/*      */     //   90: aload_2
/*      */     //   91: aload_0
/*      */     //   92: aload_1
/*      */     //   93: <illegal opcode> accept : (Lnet/minecraft/server/level/ChunkMap;Lnet/minecraft/server/level/ServerPlayer;)Ljava/util/function/Consumer;
/*      */     //   98: aload_1
/*      */     //   99: <illegal opcode> accept : (Lnet/minecraft/server/level/ServerPlayer;)Ljava/util/function/Consumer;
/*      */     //   104: invokestatic difference : (Lnet/minecraft/server/level/ChunkTrackingView;Lnet/minecraft/server/level/ChunkTrackingView;Ljava/util/function/Consumer;Ljava/util/function/Consumer;)V
/*      */     //   107: aload_1
/*      */     //   108: aload_2
/*      */     //   109: invokevirtual setChunkTrackingView : (Lnet/minecraft/server/level/ChunkTrackingView;)V
/*      */     //   112: return
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1170	-> 0
/*      */     //   #1171	-> 11
/*      */     //   #1173	-> 12
/*      */     //   #1174	-> 17
/*      */     //   #1175	-> 59
/*      */     //   #1177	-> 89
/*      */     //   #1182	-> 107
/*      */     //   #1183	-> 112
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   43	16	5	from	Lnet/minecraft/server/level/ChunkTrackingView$Positioned;
/*      */     //   30	59	4	to	Lnet/minecraft/server/level/ChunkTrackingView$Positioned;
/*      */     //   0	113	0	this	Lnet/minecraft/server/level/ChunkMap;
/*      */     //   0	113	1	player	Lnet/minecraft/server/level/ServerPlayer;
/*      */     //   0	113	2	next	Lnet/minecraft/server/level/ChunkTrackingView;
/*      */     //   17	96	3	previous	Lnet/minecraft/server/level/ChunkTrackingView; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ServerPlayer> getPlayers(ChunkPos pos, boolean borderOnly) {
/* 1187 */     Set<ServerPlayer> allPlayers = this.playerMap.getAllPlayers();
/*      */     
/* 1189 */     ImmutableList.Builder<ServerPlayer> result = ImmutableList.builder();
/* 1190 */     for (ServerPlayer player : allPlayers) {
/* 1191 */       if ((borderOnly && isChunkOnTrackedBorder(player, pos.x, pos.z)) || (!borderOnly && 
/* 1192 */         isChunkTracked(player, pos.x, pos.z))) {
/* 1193 */         result.add(player);
/*      */       }
/*      */     } 
/* 1196 */     return result.build();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void addEntity(Entity entity) {
/* 1201 */     if (entity instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragonPart) {
/*      */       return;
/*      */     }
/* 1204 */     EntityType<?> type = entity.getType();
/* 1205 */     int range = type.clientTrackingRange() * 16;
/* 1206 */     if (range == 0) {
/*      */       return;
/*      */     }
/* 1209 */     int updateInterval = type.updateInterval();
/* 1210 */     if (this.entityMap.containsKey(entity.getId())) {
/* 1211 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Entity is already tracked!"));
/*      */     }
/* 1213 */     TrackedEntity trackedEntity = new TrackedEntity(entity, range, updateInterval, type.trackDeltas());
/* 1214 */     this.entityMap.put(entity.getId(), trackedEntity);
/* 1215 */     trackedEntity.updatePlayers(this.level.players());
/*      */     
/* 1217 */     if (entity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)entity;
/* 1218 */       updatePlayerStatus(player, true);
/* 1219 */       for (ObjectIterator objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity e = (TrackedEntity)objectIterator.next();
/* 1220 */         if (e.entity != player) {
/* 1221 */           e.updatePlayer(player);
/*      */         } }
/*      */        }
/*      */   
/*      */   }
/*      */   
/*      */   protected void removeEntity(Entity entity) {
/* 1228 */     if (entity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)entity;
/* 1229 */       updatePlayerStatus(player, false);
/* 1230 */       for (ObjectIterator objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity trackedEntity = (TrackedEntity)objectIterator.next();
/* 1231 */         trackedEntity.removePlayer(player); }
/*      */        }
/*      */     
/* 1234 */     TrackedEntity trackedEntity = (TrackedEntity)this.entityMap.remove(entity.getId());
/* 1235 */     if (trackedEntity != null) {
/* 1236 */       trackedEntity.broadcastRemoved();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void tick() throws IOException {
/* 1241 */     for (ServerPlayer player : this.playerMap.getAllPlayers()) {
/* 1242 */       updateChunkTracking(player);
/*      */     }
/*      */ 
/*      */     
/* 1246 */     List<ServerPlayer> movedPlayers = Lists.newArrayList();
/* 1247 */     List<ServerPlayer> players = this.level.players();
/*      */     ObjectIterator objectIterator;
/* 1249 */     for (objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity trackedEntity = (TrackedEntity)objectIterator.next();
/* 1250 */       SectionPos oldPos = trackedEntity.lastSectionPos;
/* 1251 */       SectionPos newPos = SectionPos.of(trackedEntity.entity);
/* 1252 */       boolean sectionPosChanged = !Objects.equals(oldPos, newPos);
/* 1253 */       if (sectionPosChanged) {
/* 1254 */         trackedEntity.updatePlayers(players);
/* 1255 */         Entity entity = trackedEntity.entity;
/* 1256 */         if (entity instanceof ServerPlayer) {
/* 1257 */           movedPlayers.add((ServerPlayer)entity);
/*      */         }
/* 1259 */         trackedEntity.lastSectionPos = newPos;
/*      */       } 
/* 1261 */       if (sectionPosChanged || trackedEntity.entity.needsSync || this.distanceManager.inEntityTickingRange(newPos.chunk().toLong())) {
/* 1262 */         trackedEntity.serverEntity.sendChanges();
/*      */       } }
/*      */ 
/*      */     
/* 1266 */     if (!movedPlayers.isEmpty()) {
/* 1267 */       for (objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity trackedEntity = (TrackedEntity)objectIterator.next();
/* 1268 */         trackedEntity.updatePlayers(movedPlayers); }
/*      */     
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendToTrackingPlayers(Entity entity, Packet<? super ClientGamePacketListener> packet) {
/* 1274 */     TrackedEntity trackedEntity = (TrackedEntity)this.entityMap.get(entity.getId());
/* 1275 */     if (trackedEntity != null) {
/* 1276 */       trackedEntity.sendToTrackingPlayers(packet);
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendToTrackingPlayersFiltered(Entity entity, Packet<? super ClientGamePacketListener> packet, Predicate<ServerPlayer> targetPredicate) {
/* 1281 */     TrackedEntity trackedEntity = (TrackedEntity)this.entityMap.get(entity.getId());
/* 1282 */     if (trackedEntity != null) {
/* 1283 */       trackedEntity.sendToTrackingPlayersFiltered(packet, targetPredicate);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void sendToTrackingPlayersAndSelf(Entity entity, Packet<? super ClientGamePacketListener> packet) {
/* 1288 */     TrackedEntity trackedEntity = (TrackedEntity)this.entityMap.get(entity.getId());
/* 1289 */     if (trackedEntity != null) {
/* 1290 */       trackedEntity.sendToTrackingPlayersAndSelf(packet);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isTrackedByAnyPlayer(Entity entity) {
/* 1295 */     TrackedEntity trackedEntity = (TrackedEntity)this.entityMap.get(entity.getId());
/* 1296 */     if (trackedEntity != null) {
/* 1297 */       return !trackedEntity.seenBy.isEmpty();
/*      */     }
/* 1299 */     return false;
/*      */   }
/*      */   
/*      */   public void forEachEntityTrackedBy(ServerPlayer player, Consumer<Entity> consumer) {
/* 1303 */     for (ObjectIterator objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity entity = (TrackedEntity)objectIterator.next();
/* 1304 */       if (entity.seenBy.contains(player.connection)) {
/* 1305 */         consumer.accept(entity.entity);
/*      */       } }
/*      */   
/*      */   }
/*      */   
/*      */   public void resendBiomesForChunks(List<ChunkAccess> chunks) {
/* 1311 */     Map<ServerPlayer, List<LevelChunk>> chunksForPlayers = new HashMap<ServerPlayer, List<LevelChunk>>();
/* 1312 */     for (ChunkAccess chunkAccess : chunks) {
/* 1313 */       LevelChunk chunk; ChunkPos pos = chunkAccess.getPos();
/*      */       
/* 1315 */       if (chunkAccess instanceof LevelChunk) { LevelChunk levelChunk = (LevelChunk)chunkAccess;
/* 1316 */         chunk = levelChunk; }
/*      */       else
/* 1318 */       { chunk = this.level.getChunk(pos.x, pos.z); }
/*      */       
/* 1320 */       for (ServerPlayer player : getPlayers(pos, false)) {
/* 1321 */         ((List)chunksForPlayers.computeIfAbsent(player, p -> new ArrayList())).add(chunk);
/*      */       }
/*      */     } 
/*      */     
/* 1325 */     chunksForPlayers.forEach((player, chunkList) -> player.connection.send(ClientboundChunksBiomesPacket.forChunks(chunkList)));
/*      */   }
/*      */ 
/*      */   
/* 1329 */   protected PoiManager getPoiManager() { return this.poiManager; }
/*      */ 
/*      */ 
/*      */   
/* 1333 */   public String getStorageName() { return this.storageName; }
/*      */ 
/*      */ 
/*      */   
/* 1337 */   void onFullChunkStatusChange(ChunkPos pos, FullChunkStatus status) { this.chunkStatusListener.onChunkStatusChange(pos, status); }
/*      */ 
/*      */   
/*      */   public void waitForLightBeforeSending(ChunkPos centerChunk, int chunkRadius) {
/* 1341 */     int affectedLightChunkRadius = chunkRadius + 1;
/* 1342 */     ChunkPos.rangeClosed(centerChunk, affectedLightChunkRadius).forEach(chunkPos -> {
/* 1343 */           ChunkHolder chunkHolder = getVisibleChunkIfPresent(chunkPos.toLong());
/* 1344 */           if (chunkHolder != null) {
/* 1345 */             chunkHolder.addSendDependency(this.lightEngine.waitForPendingTasks(chunkPos.x, chunkPos.z));
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   public void forEachReadyToSendChunk(Consumer<LevelChunk> consumer) {
/* 1351 */     for (ObjectIterator objectIterator = this.visibleChunkMap.values().iterator(); objectIterator.hasNext(); ) { ChunkHolder chunkHolder = (ChunkHolder)objectIterator.next();
/* 1352 */       LevelChunk chunk = chunkHolder.getChunkToSend();
/* 1353 */       if (chunk != null)
/* 1354 */         consumer.accept(chunk);  }
/*      */   
/*      */   }
/*      */   
/*      */   private class DistanceManager
/*      */     extends DistanceManager
/*      */   {
/* 1361 */     protected DistanceManager(TicketStorage ticketStorage, Executor executor, Executor mainThreadExecutor) { super(ticketStorage, executor, mainThreadExecutor); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1366 */     protected boolean isChunkToRemove(long node) { return ChunkMap.this.toDrop.contains(node); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1371 */     protected ChunkHolder getChunk(long node) { return ChunkMap.this.getUpdatingChunkIfPresent(node); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1376 */     protected ChunkHolder updateChunkScheduling(long node, int level, ChunkHolder chunk, int oldLevel) { return ChunkMap.this.updateChunkScheduling(node, level, chunk, oldLevel); }
/*      */   }
/*      */   
/*      */   private class TrackedEntity
/*      */     implements ServerEntity.Synchronizer {
/*      */     private final ServerEntity serverEntity;
/*      */     private final Entity entity;
/*      */     private final int range;
/*      */     private SectionPos lastSectionPos;
/*      */     private final Set<ServerPlayerConnection> seenBy;
/*      */     
/*      */     public TrackedEntity(Entity entity, int range, int updateInterval, boolean trackDelta) {
/* 1388 */       this.seenBy = Sets.newIdentityHashSet();
/*      */ 
/*      */       
/* 1391 */       this.serverEntity = new ServerEntity(ChunkMap.this.level, entity, updateInterval, trackDelta, this);
/* 1392 */       this.entity = entity;
/* 1393 */       this.range = range;
/* 1394 */       this.lastSectionPos = SectionPos.of(entity);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1399 */       if (obj instanceof TrackedEntity) {
/* 1400 */         return (((TrackedEntity)obj).entity.getId() == this.entity.getId());
/*      */       }
/*      */       
/* 1403 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1408 */     public int hashCode() { return this.entity.getId(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void sendToTrackingPlayers(Packet<? super ClientGamePacketListener> packet) {
/* 1413 */       for (ServerPlayerConnection connection : this.seenBy) {
/* 1414 */         connection.send(packet);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void sendToTrackingPlayersAndSelf(Packet<? super ClientGamePacketListener> packet) {
/* 1420 */       sendToTrackingPlayers(packet);
/* 1421 */       Entity entity1 = this.entity; if (entity1 instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)entity1;
/* 1422 */         player.connection.send(packet); }
/*      */     
/*      */     }
/*      */ 
/*      */     
/*      */     public void sendToTrackingPlayersFiltered(Packet<? super ClientGamePacketListener> packet, Predicate<ServerPlayer> targetPredicate) {
/* 1428 */       for (ServerPlayerConnection connection : this.seenBy) {
/* 1429 */         if (targetPredicate.test(connection.getPlayer())) {
/* 1430 */           connection.send(packet);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public void broadcastRemoved() throws IOException {
/* 1436 */       for (ServerPlayerConnection connection : this.seenBy) {
/* 1437 */         this.serverEntity.removePairing(connection.getPlayer());
/*      */       }
/*      */     }
/*      */     
/*      */     public void removePlayer(ServerPlayer player) {
/* 1442 */       if (this.seenBy.remove(player.connection)) {
/* 1443 */         this.serverEntity.removePairing(player);
/* 1444 */         if (this.seenBy.isEmpty()) {
/* 1445 */           ChunkMap.this.level.debugSynchronizers().dropEntity(this.entity);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public void updatePlayer(ServerPlayer player) {
/* 1451 */       if (player == this.entity) {
/*      */         return;
/*      */       }
/*      */       
/* 1455 */       Vec3 deltaToPlayer = player.position().subtract(this.entity.position());
/* 1456 */       int playerViewDistance = ChunkMap.this.getPlayerViewDistance(player);
/* 1457 */       double visibleRange = Math.min(getEffectiveRange(), playerViewDistance * 16);
/* 1458 */       double distanceSquared = deltaToPlayer.x * deltaToPlayer.x + deltaToPlayer.z * deltaToPlayer.z;
/* 1459 */       double rangeSquared = visibleRange * visibleRange;
/*      */       
/* 1461 */       boolean visibleToPlayer = (distanceSquared <= rangeSquared && this.entity.broadcastToPlayer(player) && ChunkMap.this.isChunkTracked(player, (this.entity.chunkPosition()).x, (this.entity.chunkPosition()).z));
/*      */       
/* 1463 */       if (visibleToPlayer) {
/* 1464 */         if (this.seenBy.add(player.connection)) {
/* 1465 */           this.serverEntity.addPairing(player);
/* 1466 */           if (this.seenBy.size() == 1) {
/* 1467 */             ChunkMap.this.level.debugSynchronizers().registerEntity(this.entity);
/*      */           }
/* 1469 */           ChunkMap.this.level.debugSynchronizers().startTrackingEntity(player, this.entity);
/*      */         } 
/*      */       } else {
/* 1472 */         removePlayer(player);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1477 */     private int scaledRange(int range) { return ChunkMap.this.level.getServer().getScaledTrackingDistance(range); }
/*      */ 
/*      */     
/*      */     private int getEffectiveRange() {
/* 1481 */       int effectiveRange = this.range;
/* 1482 */       for (Entity passenger : this.entity.getIndirectPassengers()) {
/* 1483 */         int passengerRange = passenger.getType().clientTrackingRange() * 16;
/* 1484 */         if (passengerRange > effectiveRange) {
/* 1485 */           effectiveRange = passengerRange;
/*      */         }
/*      */       } 
/* 1488 */       return scaledRange(effectiveRange);
/*      */     }
/*      */     
/*      */     public void updatePlayers(List<ServerPlayer> players) {
/* 1492 */       for (ServerPlayer player : players)
/* 1493 */         updatePlayer(player); 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ChunkMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */