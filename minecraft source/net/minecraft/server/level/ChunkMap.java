 package net.minecraft.server.level;
 import com.google.common.collect.ImmutableList;
 import com.google.common.collect.Lists;
 import com.google.common.collect.Queues;
 import com.google.common.collect.Sets;
 import com.mojang.datafixers.DataFixer;
 import com.mojang.datafixers.util.Pair;
 import com.mojang.logging.LogUtils;
 import com.mojang.serialization.MapCodec;
 import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
 import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
 import it.unimi.dsi.fastutil.longs.Long2ByteMap;
 import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
 import it.unimi.dsi.fastutil.longs.Long2LongMap;
 import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
 import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
 import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
 import it.unimi.dsi.fastutil.longs.LongIterator;
 import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
 import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
 import it.unimi.dsi.fastutil.longs.LongSet;
 import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
 import it.unimi.dsi.fastutil.objects.ObjectIterator;
 import java.io.IOException;
 import java.io.Writer;
 import java.nio.file.Path;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Locale;
 import java.util.Map;
 import java.util.Objects;
 import java.util.Optional;
 import java.util.Queue;
 import java.util.Set;
 import java.util.concurrent.CancellationException;
 import java.util.concurrent.CompletableFuture;
 import java.util.concurrent.CompletionException;
 import java.util.concurrent.Executor;
 import java.util.concurrent.atomic.AtomicInteger;
 import java.util.function.BooleanSupplier;
 import java.util.function.Consumer;
 import java.util.function.IntConsumer;
 import java.util.function.IntFunction;
 import java.util.function.IntSupplier;
 import java.util.function.Predicate;
 import java.util.function.Supplier;
 import java.util.stream.Stream;
 import net.minecraft.CrashReport;
 import net.minecraft.CrashReportCategory;
 import net.minecraft.ReportedException;
 import net.minecraft.core.BlockPos;
 import net.minecraft.core.RegistryAccess;
 import net.minecraft.core.SectionPos;
 import net.minecraft.core.registries.Registries;
 import net.minecraft.nbt.CompoundTag;
 import net.minecraft.network.protocol.Packet;
 import net.minecraft.network.protocol.game.ClientGamePacketListener;
 import net.minecraft.network.protocol.game.ClientboundChunksBiomesPacket;
 import net.minecraft.resources.ResourceKey;
 import net.minecraft.server.network.ServerPlayerConnection;
 import net.minecraft.util.CsvOutput;
 import net.minecraft.util.Mth;
 import net.minecraft.util.StaticCache2D;
 import net.minecraft.util.TriState;
 import net.minecraft.util.Util;
 import net.minecraft.util.datafix.DataFixTypes;
 import net.minecraft.util.profiling.Profiler;
 import net.minecraft.util.profiling.ProfilerFiller;
 import net.minecraft.util.thread.BlockableEventLoop;
 import net.minecraft.util.thread.ConsecutiveExecutor;
 import net.minecraft.world.entity.Entity;
 import net.minecraft.world.entity.EntityType;
 import net.minecraft.world.entity.ai.village.poi.PoiManager;
 import net.minecraft.world.level.ChunkPos;
 import net.minecraft.world.level.Level;
 import net.minecraft.world.level.TicketStorage;
 import net.minecraft.world.level.chunk.ChunkAccess;
 import net.minecraft.world.level.chunk.ChunkGenerator;
 import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
 import net.minecraft.world.level.chunk.LevelChunk;
 import net.minecraft.world.level.chunk.LightChunkGetter;
 import net.minecraft.world.level.chunk.ProtoChunk;
 import net.minecraft.world.level.chunk.UpgradeData;
 import net.minecraft.world.level.chunk.status.ChunkStatus;
 import net.minecraft.world.level.chunk.status.ChunkStep;
 import net.minecraft.world.level.chunk.status.ChunkType;
 import net.minecraft.world.level.chunk.status.WorldGenContext;
 import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
 import net.minecraft.world.level.chunk.storage.SerializableChunkData;
 import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
 import net.minecraft.world.level.entity.ChunkStatusUpdateListener;
 import net.minecraft.world.level.gamerules.GameRules;
 import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
 import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
 import net.minecraft.world.level.levelgen.RandomState;
 import net.minecraft.world.level.levelgen.structure.LegacyStructureDataHandler;
 import net.minecraft.world.level.levelgen.structure.StructureStart;
 import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
 import net.minecraft.world.level.storage.DimensionDataStorage;
 import net.minecraft.world.level.storage.LevelStorageSource;
 import net.minecraft.world.phys.Vec3;
 import org.apache.commons.lang3.mutable.MutableBoolean;
 import org.slf4j.Logger;
 public class ChunkMap
   extends SimpleRegionStorage
   implements ChunkHolder.PlayerProvider, GeneratingChunkMap
 {
   private static final ChunkResult<List<ChunkAccess>> UNLOADED_CHUNK_LIST_RESULT = ChunkResult.error("Unloaded chunks found in range");
   private static final CompletableFuture<ChunkResult<List<ChunkAccess>>> UNLOADED_CHUNK_LIST_FUTURE = CompletableFuture.completedFuture(UNLOADED_CHUNK_LIST_RESULT);
   private static final byte CHUNK_TYPE_REPLACEABLE = -1;
   private static final byte CHUNK_TYPE_UNKNOWN = 0;
   private static final byte CHUNK_TYPE_FULL = 1;
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final int CHUNK_SAVED_PER_TICK = 200;
   private static final int CHUNK_SAVED_EAGERLY_PER_TICK = 20;
   private static final int EAGER_CHUNK_SAVE_COOLDOWN_IN_MILLIS = 10000;
   private static final int MAX_ACTIVE_CHUNK_WRITES = 128;
   public static final int MIN_VIEW_DISTANCE = 2;
   public static final int MAX_VIEW_DISTANCE = 32;
   public static final int FORCED_TICKET_LEVEL = ChunkLevel.byStatus(FullChunkStatus.ENTITY_TICKING);
   private final Long2ObjectLinkedOpenHashMap<ChunkHolder> updatingChunkMap = new Long2ObjectLinkedOpenHashMap();
   private final Long2ObjectLinkedOpenHashMap<ChunkHolder> pendingUnloads = new Long2ObjectLinkedOpenHashMap();
   private final List<ChunkGenerationTask> pendingGenerationTasks = new ArrayList();
   private final ServerLevel level;
   private final ThreadedLevelLightEngine lightEngine;
   private final BlockableEventLoop<Runnable> mainThreadExecutor;
   private final RandomState randomState;
   private final ChunkGeneratorStructureState chunkGeneratorState;
   private final TicketStorage ticketStorage;
   private final PoiManager poiManager;
   private final LongSet toDrop = new LongOpenHashSet();
   private boolean modified;
   private final ChunkTaskDispatcher worldgenTaskDispatcher;
   private final ChunkTaskDispatcher lightTaskDispatcher;
   private final ChunkStatusUpdateListener chunkStatusListener;
   private final DistanceManager distanceManager;
   private final String storageName;
   private final PlayerMap playerMap = new PlayerMap();
   private final Int2ObjectMap<TrackedEntity> entityMap = new Int2ObjectOpenHashMap();
   private final Long2ByteMap chunkTypeCache = new Long2ByteOpenHashMap();
   private final Long2LongMap nextChunkSaveTime = new Long2LongOpenHashMap();
   private final LongSet chunksToEagerlySave = new LongLinkedOpenHashSet();
   private final Queue<Runnable> unloadQueue = Queues.newConcurrentLinkedQueue();
   private final AtomicInteger activeChunkWrites = new AtomicInteger();
   private int serverViewDistance;
   private final WorldGenContext worldGenContext;
   public ChunkMap(ServerLevel level, LevelStorageSource.LevelStorageAccess levelStorage, DataFixer dataFixer, StructureTemplateManager structureManager, Executor executor, BlockableEventLoop<Runnable> mainThreadExecutor, LightChunkGetter chunkGetter, ChunkGenerator generator, ChunkStatusUpdateListener chunkStatusListener, Supplier<DimensionDataStorage> overworldDataStorage, TicketStorage ticketStorage, int serverViewDistance, boolean syncWrites) {
     super(new RegionStorageInfo(levelStorage
           .getLevelId(), level.dimension(), "chunk"), levelStorage
         .getDimensionPath(level.dimension()).resolve("region"), dataFixer, syncWrites, DataFixTypes.CHUNK, 
         LegacyStructureDataHandler.getLegacyTagFixer(level.dimension(), overworldDataStorage, dataFixer));
     Path storageFolder = levelStorage.getDimensionPath(level.dimension());
     this.storageName = storageFolder.getFileName().toString();
     this.level = level;
     RegistryAccess registryAccess = level.registryAccess();
     long levelSeed = level.getSeed();
     if (generator instanceof NoiseBasedChunkGenerator) { NoiseBasedChunkGenerator noiseGenerator = (NoiseBasedChunkGenerator)generator;
       this.randomState = RandomState.create((NoiseGeneratorSettings)noiseGenerator.generatorSettings().value(), registryAccess.lookupOrThrow(Registries.NOISE), levelSeed); }
     else
     { this.randomState = RandomState.create(NoiseGeneratorSettings.dummy(), registryAccess.lookupOrThrow(Registries.NOISE), levelSeed); }
     this.chunkGeneratorState = generator.createState(registryAccess.lookupOrThrow(Registries.STRUCTURE_SET), this.randomState, levelSeed);
     this.mainThreadExecutor = mainThreadExecutor;
     ConsecutiveExecutor worldgen = new ConsecutiveExecutor(executor, "worldgen");
     this.chunkStatusListener = chunkStatusListener;
     ConsecutiveExecutor light = new ConsecutiveExecutor(executor, "light");
     this.worldgenTaskDispatcher = new ChunkTaskDispatcher(worldgen, executor);
     this.lightTaskDispatcher = new ChunkTaskDispatcher(light, executor);
     this.lightEngine = new ThreadedLevelLightEngine(chunkGetter, this, this.level.dimensionType().hasSkyLight(), light, this.lightTaskDispatcher);
     this.distanceManager = new DistanceManager(ticketStorage, executor, mainThreadExecutor);
     this.ticketStorage = ticketStorage;
     this.poiManager = new PoiManager(new RegionStorageInfo(levelStorage.getLevelId(), level.dimension(), "poi"), storageFolder.resolve("poi"), dataFixer, syncWrites, registryAccess, level.getServer(), level);
     setServerViewDistance(serverViewDistance);
     this.worldGenContext = new WorldGenContext(level, generator, structureManager, this.lightEngine, mainThreadExecutor, this::setChunkUnsaved);
   }
   private void setChunkUnsaved(ChunkPos chunkPos) { this.chunksToEagerlySave.add(chunkPos.toLong()); }
   protected ChunkGenerator generator() { return this.worldGenContext.generator(); }
   protected ChunkGeneratorStructureState generatorState() { return this.chunkGeneratorState; }
   protected RandomState randomState() { return this.randomState; }
   public boolean isChunkTracked(ServerPlayer player, int chunkX, int chunkZ) { return (player.getChunkTrackingView().contains(chunkX, chunkZ) && !player.connection.chunkSender.isPending(ChunkPos.asLong(chunkX, chunkZ))); }
   private boolean isChunkOnTrackedBorder(ServerPlayer player, int chunkX, int chunkZ) {
     if (!isChunkTracked(player, chunkX, chunkZ)) {
       return false;
     }
     for (int dx = -1; dx <= 1; dx++) {
       for (int dz = -1; dz <= 1; dz++) {
         if (dx != 0 || dz != 0)
         {
           if (!isChunkTracked(player, chunkX + dx, chunkZ + dz))
             return true; 
         }
       } 
     } 
     return false;
   }
   protected ThreadedLevelLightEngine getLightEngine() { return this.lightEngine; }
   public ChunkHolder getUpdatingChunkIfPresent(long key) { return (ChunkHolder)this.updatingChunkMap.get(key); }
   protected ChunkHolder getVisibleChunkIfPresent(long key) { return (ChunkHolder)this.visibleChunkMap.get(key); }
   public ChunkStatus getLatestStatus(long key) {
     ChunkHolder chunkHolder = getVisibleChunkIfPresent(key);
     return (chunkHolder != null) ? chunkHolder.getLatestStatus() : null;
   }
   protected IntSupplier getChunkQueueLevel(long pos) {
     return () -> {
         ChunkHolder chunk = getVisibleChunkIfPresent(pos);
         if (chunk == null) {
           return ChunkTaskPriorityQueue.PRIORITY_LEVEL_COUNT - 1;
         }
         return Math.min(chunk.getQueueLevel(), ChunkTaskPriorityQueue.PRIORITY_LEVEL_COUNT - 1);
       };
   }
   public String getChunkDebugData(ChunkPos pos) {
     ChunkHolder chunkHolder = getVisibleChunkIfPresent(pos.toLong());
     if (chunkHolder == null) {
       return "null";
     }
     String result = "" + chunkHolder.getTicketLevel() + "\n";
     ChunkStatus status = chunkHolder.getLatestStatus();
     ChunkAccess chunk = chunkHolder.getLatestChunk();
     if (status != null) {
       result = result + "St: §" + result + status.getIndex() + "§r\n";
     }
     if (chunk != null) {
       result = result + "Ch: §" + result + chunk.getPersistedStatus().getIndex() + "§r\n";
     }
     FullChunkStatus fullStatus = chunkHolder.getFullStatus();
     result = result + result + String.valueOf('§') + fullStatus.ordinal();
     return result + "§r";
   }
   CompletableFuture<ChunkResult<List<ChunkAccess>>> getChunkRangeFuture(ChunkHolder centerChunk, int range, IntFunction<ChunkStatus> distanceToStatus) {
     if (range == 0) {
       ChunkStatus status = (ChunkStatus)distanceToStatus.apply(0);
       return centerChunk.scheduleChunkGenerationTask(status, this).thenApply(r -> r.map(List::of));
     } 
     int chunkCount = Mth.square(range * 2 + 1);
     List<CompletableFuture<ChunkResult<ChunkAccess>>> deps = new ArrayList<CompletableFuture<ChunkResult<ChunkAccess>>>(chunkCount);
     ChunkPos centerPos = centerChunk.getPos();
     for (int z = -range; z <= range; z++) {
       for (int x = -range; x <= range; x++) {
         int distance = Math.max(Math.abs(x), Math.abs(z));
         long chunkNode = ChunkPos.asLong(centerPos.x + x, centerPos.z + z);
         ChunkHolder chunk = getUpdatingChunkIfPresent(chunkNode);
         if (chunk == null) {
           return UNLOADED_CHUNK_LIST_FUTURE;
         }
         ChunkStatus depStatus = (ChunkStatus)distanceToStatus.apply(distance);
         deps.add(chunk.scheduleChunkGenerationTask(depStatus, this));
       } 
     } 
     return Util.sequence(deps).thenApply(chunkResults -> {
           List<ChunkAccess> chunks = new ArrayList<ChunkAccess>(chunkResults.size());
           for (ChunkResult<ChunkAccess> chunkResult : chunkResults) {
             if (chunkResult == null) {
               throw debugFuturesAndCreateReportedException(new IllegalStateException("At least one of the chunk futures were null"), "n/a");
             }
             ChunkAccess chunk = (ChunkAccess)chunkResult.orElse(null);
             if (chunk == null) {
               return UNLOADED_CHUNK_LIST_RESULT;
             }
             chunks.add(chunk);
           } 
           return ChunkResult.of(chunks);
         });
   }
   public ReportedException debugFuturesAndCreateReportedException(IllegalStateException exception, String details) {
     StringBuilder sb = new StringBuilder();
     Consumer<ChunkHolder> addToDebug = holder -> 
       holder.getAllFutures().forEach(());
     sb.append("Updating:").append(System.lineSeparator());
     this.updatingChunkMap.values().forEach(addToDebug);
     sb.append("Visible:").append(System.lineSeparator());
     this.visibleChunkMap.values().forEach(addToDebug);
     CrashReport report = CrashReport.forThrowable(exception, "Chunk loading");
     CrashReportCategory category = report.addCategory("Chunk loading");
     category.setDetail("Details", details);
     category.setDetail("Futures", sb);
     return new ReportedException(report);
   }
   public CompletableFuture<ChunkResult<LevelChunk>> prepareEntityTickingChunk(ChunkHolder chunk) {
     return getChunkRangeFuture(chunk, 2, distance -> ChunkStatus.FULL)
       .thenApply(chunkResult -> chunkResult.map(()));
   }
   private ChunkHolder updateChunkScheduling(long node, int level, ChunkHolder chunk, int oldLevel) {
     if (!ChunkLevel.isLoaded(oldLevel) && !ChunkLevel.isLoaded(level)) {
       return chunk;
     }
     if (chunk != null) {
       chunk.setTicketLevel(level);
     }
     if (chunk != null) {
       if (!ChunkLevel.isLoaded(level)) {
         this.toDrop.add(node);
       } else {
         this.toDrop.remove(node);
       } 
     }
     if (ChunkLevel.isLoaded(level) && 
       chunk == null) {
       chunk = (ChunkHolder)this.pendingUnloads.remove(node);
       if (chunk != null) {
         chunk.setTicketLevel(level);
       } else {
         chunk = new ChunkHolder(new ChunkPos(node), level, this.level, this.lightEngine, this::onLevelChange, this);
       } 
       this.updatingChunkMap.put(node, chunk);
       this.modified = true;
     } 
     return chunk;
   }
   private void onLevelChange(ChunkPos pos, IntSupplier oldLevel, int newLevel, IntConsumer setQueueLevel) {
     this.worldgenTaskDispatcher.onLevelChange(pos, oldLevel, newLevel, setQueueLevel);
     this.lightTaskDispatcher.onLevelChange(pos, oldLevel, newLevel, setQueueLevel);
   }
   public void close() throws IOException {
     try {
       this.worldgenTaskDispatcher.close();
       this.lightTaskDispatcher.close();
       this.poiManager.close();
     } finally {
       super.close();
     } 
   }
   protected void saveAllChunks(boolean flushStorage) {
     if (flushStorage) {
       List<ChunkHolder> chunksToSave = this.visibleChunkMap.values().stream().filter(ChunkHolder::wasAccessibleSinceLastSave).peek(ChunkHolder::refreshAccessibility).toList();
       MutableBoolean didWork = new MutableBoolean();
       do {
         didWork.setFalse();
         chunksToSave.stream()
           .map(chunk -> {
               Objects.requireNonNull(chunk); this.mainThreadExecutor.managedBlock(chunk::isReadyForSaving);
               return chunk.getLatestChunk();
             }).filter(chunkAccess -> (chunkAccess instanceof net.minecraft.world.level.chunk.ImposterProtoChunk || chunkAccess instanceof LevelChunk))
           .filter(this::save)
           .forEach(c -> didWork.setTrue());
       } while (didWork.isTrue());
       this.poiManager.flushAll();
       processUnloads(() -> true);
       synchronize(true).join();
     } else {
       this.nextChunkSaveTime.clear();
       long now = Util.getMillis();
       for (ObjectIterator objectIterator = this.visibleChunkMap.values().iterator(); objectIterator.hasNext(); ) { ChunkHolder chunk = (ChunkHolder)objectIterator.next();
         saveChunkIfNeeded(chunk, now); }
     } 
   }
   protected void tick(BooleanSupplier haveTime) {
     ProfilerFiller profiler = Profiler.get();
     profiler.push("poi");
     this.poiManager.tick(haveTime);
     profiler.popPush("chunk_unload");
     if (!this.level.noSave()) {
       processUnloads(haveTime);
     }
     profiler.pop();
   }
   public boolean hasWork() {
     return (this.lightEngine.hasLightWork() || 
       !this.pendingUnloads.isEmpty() || 
       !this.updatingChunkMap.isEmpty() || this.poiManager
       .hasWork() || 
       !this.toDrop.isEmpty() || 
       !this.unloadQueue.isEmpty() || this.worldgenTaskDispatcher
       .hasWork() || this.lightTaskDispatcher
       .hasWork() || this.distanceManager
       .hasTickets());
   }
   private void processUnloads(BooleanSupplier haveTime) {
     LongIterator iterator = this.toDrop.iterator();
     while (iterator.hasNext()) {
       long pos = iterator.nextLong();
       ChunkHolder chunkHolder = (ChunkHolder)this.updatingChunkMap.get(pos);
       if (chunkHolder != null) {
         this.updatingChunkMap.remove(pos);
         this.pendingUnloads.put(pos, chunkHolder);
         this.modified = true;
         scheduleUnload(pos, chunkHolder);
       } 
       iterator.remove();
     } 
     int minimalNumberOfChunksToProcess = Math.max(0, this.unloadQueue.size() - 2000); Runnable unloadTask;
     while ((minimalNumberOfChunksToProcess > 0 || haveTime.getAsBoolean()) && (unloadTask = (Runnable)this.unloadQueue.poll()) != null) {
       minimalNumberOfChunksToProcess--;
       unloadTask.run();
     } 
     saveChunksEagerly(haveTime);
   }
   private void saveChunksEagerly(BooleanSupplier haveTime) {
     long now = Util.getMillis();
     int eagerlySavedCount = 0;
     LongIterator iterator = this.chunksToEagerlySave.iterator();
     while (eagerlySavedCount < 20 && this.activeChunkWrites
       .get() < 128 && haveTime
       .getAsBoolean() && iterator
       .hasNext()) {
       long chunkPos = iterator.nextLong();
       ChunkHolder chunkHolder = (ChunkHolder)this.visibleChunkMap.get(chunkPos);
       ChunkAccess latestChunk = (chunkHolder != null) ? chunkHolder.getLatestChunk() : null;
       if (latestChunk == null || !latestChunk.isUnsaved()) {
         iterator.remove();
         continue;
       } 
       if (saveChunkIfNeeded(chunkHolder, now)) {
         eagerlySavedCount++;
         iterator.remove();
       } 
     } 
   }
   private void scheduleUnload(long pos, ChunkHolder chunkHolder) {
     CompletableFuture<?> saveSyncFuture = chunkHolder.getSaveSyncFuture();
     Objects.requireNonNull(this.unloadQueue); saveSyncFuture.thenRunAsync(() -> { CompletableFuture<?> currentFuture = chunkHolder.getSaveSyncFuture(); if (currentFuture != saveSyncFuture) { scheduleUnload(pos, chunkHolder); return; }  ChunkAccess chunk = chunkHolder.getLatestChunk(); if (this.pendingUnloads.remove(pos, chunkHolder) && chunk != null) { if (chunk instanceof LevelChunk) { LevelChunk levelChunk = (LevelChunk)chunk; levelChunk.setLoaded(false); }  save(chunk); if (chunk instanceof LevelChunk) { LevelChunk levelChunk = (LevelChunk)chunk; this.level.unload(levelChunk); }  this.lightEngine.updateChunkStatus(chunk.getPos()); this.lightEngine.tryScheduleUpdate(); this.nextChunkSaveTime.remove(chunk.getPos().toLong()); }  }this.unloadQueue::add).whenComplete((ignored, throwable) -> {
           if (throwable != null) {
             LOGGER.error("Failed to save chunk {}", chunkHolder.getPos(), throwable);
           }
         });
   }
   protected boolean promoteChunkMap() {
     if (!this.modified) {
       return false;
     }
     this.visibleChunkMap = this.updatingChunkMap.clone();
     this.modified = false;
     return true;
   }
   private CompletableFuture<ChunkAccess> scheduleChunkLoad(ChunkPos pos) {
     CompletableFuture<Optional<SerializableChunkData>> chunkDataFuture = readChunk(pos).thenApplyAsync(chunkData -> chunkData.map(()), 
         Util.backgroundExecutor().forName("parseChunk"));
     CompletableFuture<?> poiFuture = this.poiManager.prefetch(pos);
     return chunkDataFuture.thenCombine(poiFuture, (chunkData, ignored) -> chunkData)
       .thenApplyAsync(chunkData -> {
           Profiler.get().incrementCounter("chunkLoad");
           if (chunkData.isPresent()) {
             ProtoChunk protoChunk = ((SerializableChunkData)chunkData.get()).read(this.level, this.poiManager, storageInfo(), pos);
             markPosition(pos, protoChunk.getPersistedStatus().getChunkType());
             return protoChunk;
           } 
           return createEmptyChunk(pos);
         }this.mainThreadExecutor).exceptionallyAsync(throwable -> handleChunkLoadFailure(throwable, pos), this.mainThreadExecutor);
   }
   private ChunkAccess handleChunkLoadFailure(Throwable throwable, ChunkPos pos) {
     CompletionException e = (CompletionException)throwable; Throwable unwrapped = (throwable instanceof CompletionException) ? e.getCause() : throwable;
     ReportedException e = (ReportedException)unwrapped; Throwable cause = (unwrapped instanceof ReportedException) ? e.getCause() : unwrapped;
     boolean alwaysThrow = cause instanceof Error;
     boolean ioException = (cause instanceof IOException || cause instanceof net.minecraft.nbt.NbtException);
     if (!alwaysThrow) { if (!ioException); }
     else { CrashReport report = CrashReport.forThrowable(throwable, "Exception loading chunk");
       CrashReportCategory chunkBeingLoaded = report.addCategory("Chunk being loaded");
       chunkBeingLoaded.setDetail("pos", pos);
       markPositionReplaceable(pos);
       throw new ReportedException(report); }
     this.level.getServer().reportChunkLoadFailure(cause, storageInfo(), pos);
     return createEmptyChunk(pos);
   }
   private ChunkAccess createEmptyChunk(ChunkPos pos) {
     markPositionReplaceable(pos);
     return new ProtoChunk(pos, UpgradeData.EMPTY, this.level, this.level.palettedContainerFactory(), null);
   }
   private void markPositionReplaceable(ChunkPos pos) { this.chunkTypeCache.put(pos.toLong(), (byte)-1); }
   private byte markPosition(ChunkPos pos, ChunkType type) { return this.chunkTypeCache.put(pos.toLong(), (type == ChunkType.PROTOCHUNK) ? -1 : 1); }
   public GenerationChunkHolder acquireGeneration(long chunkNode) {
     ChunkHolder chunkHolder = (ChunkHolder)this.updatingChunkMap.get(chunkNode);
     chunkHolder.increaseGenerationRefCount();
     return chunkHolder;
   }
   public void releaseGeneration(GenerationChunkHolder chunkHolder) { chunkHolder.decreaseGenerationRefCount(); }
   public CompletableFuture<ChunkAccess> applyStep(GenerationChunkHolder chunkHolder, ChunkStep step, StaticCache2D<GenerationChunkHolder> cache) {
     ChunkPos pos = chunkHolder.getPos();
     if (step.targetStatus() == ChunkStatus.EMPTY) {
       return scheduleChunkLoad(pos);
     }
     try {
       GenerationChunkHolder holder = (GenerationChunkHolder)cache.get(pos.x, pos.z);
       ChunkAccess centerChunk = holder.getChunkIfPresentUnchecked(step.targetStatus().getParent());
       if (centerChunk == null) {
         throw new IllegalStateException("Parent chunk missing");
       }
       return step.apply(this.worldGenContext, cache, centerChunk);
     } catch (Exception e) {
       e.getStackTrace();
       CrashReport report = CrashReport.forThrowable(e, "Exception generating new chunk");
       CrashReportCategory category = report.addCategory("Chunk to be generated");
       category.setDetail("Status being generated", () -> step.targetStatus().getName());
       category.setDetail("Location", String.format(Locale.ROOT, "%d,%d", new Object[] { Integer.valueOf(pos.x), Integer.valueOf(pos.z) }));
       category.setDetail("Position hash", Long.valueOf(ChunkPos.asLong(pos.x, pos.z)));
       category.setDetail("Generator", generator());
       this.mainThreadExecutor.execute(() -> {
             throw new ReportedException(report);
           });
       throw new ReportedException(report);
     } 
   }
   public ChunkGenerationTask scheduleGenerationTask(ChunkStatus targetStatus, ChunkPos pos) {
     ChunkGenerationTask task = ChunkGenerationTask.create(this, targetStatus, pos);
     this.pendingGenerationTasks.add(task);
     return task;
   }
   private void runGenerationTask(ChunkGenerationTask task) {
     GenerationChunkHolder chunk = task.getCenter();
     Objects.requireNonNull(chunk); this.worldgenTaskDispatcher.submit(() -> { CompletableFuture<?> future = task.runUntilWait(); if (future == null) return;  future.thenRun(()); }chunk.getPos().toLong(), chunk::getQueueLevel);
   }
   public void runGenerationTasks() throws IOException {
     this.pendingGenerationTasks.forEach(this::runGenerationTask);
     this.pendingGenerationTasks.clear();
   }
   public CompletableFuture<ChunkResult<LevelChunk>> prepareTickingChunk(ChunkHolder chunk) {
     CompletableFuture<ChunkResult<List<ChunkAccess>>> future = getChunkRangeFuture(chunk, 1, distance -> ChunkStatus.FULL);
     return future.thenApplyAsync(listResult -> listResult.map(()), this.mainThreadExecutor);
   }
   private void onChunkReadyToSend(ChunkHolder chunkHolder, LevelChunk chunk) {
     ChunkPos chunkPos = chunk.getPos();
     for (ServerPlayer player : this.playerMap.getAllPlayers()) {
       if (player.getChunkTrackingView().contains(chunkPos)) {
         markChunkPendingToSend(player, chunk);
       }
     } 
     this.level.getChunkSource().onChunkReadyToSend(chunkHolder);
     this.level.debugSynchronizers().registerChunk(chunk);
   }
   public CompletableFuture<ChunkResult<LevelChunk>> prepareAccessibleChunk(ChunkHolder chunk) {
     return getChunkRangeFuture(chunk, 1, ChunkLevel::getStatusAroundFullChunk)
       .thenApply(chunkResult -> chunkResult.map(()));
   }
   Stream<ChunkHolder> allChunksWithAtLeastStatus(ChunkStatus status) {
     int level = ChunkLevel.byStatus(status);
     return this.visibleChunkMap.values().stream().filter(chunk -> (chunk.getTicketLevel() <= level));
   }
   private boolean saveChunkIfNeeded(ChunkHolder chunk, long now) {
     if (!chunk.wasAccessibleSinceLastSave() || !chunk.isReadyForSaving()) {
       return false;
     }
     ChunkAccess chunkAccess = chunk.getLatestChunk();
     if (chunkAccess instanceof net.minecraft.world.level.chunk.ImposterProtoChunk || chunkAccess instanceof LevelChunk) {
       if (!chunkAccess.isUnsaved()) {
         return false;
       }
       long chunkPos = chunkAccess.getPos().toLong();
       long nextSaveTime = this.nextChunkSaveTime.getOrDefault(chunkPos, -1L);
       if (now < nextSaveTime) {
         return false;
       }
       boolean saved = save(chunkAccess);
       chunk.refreshAccessibility();
       if (saved) {
         this.nextChunkSaveTime.put(chunkPos, now + 10000L);
       }
       return saved;
     } 
     return false;
   }
   private boolean save(ChunkAccess chunk) {
     this.poiManager.flush(chunk.getPos());
     if (!chunk.tryMarkSaved()) {
       return false;
     }
     ChunkPos pos = chunk.getPos();
     try {
       ChunkStatus status = chunk.getPersistedStatus();
       if (status.getChunkType() != ChunkType.LEVELCHUNK) {
         if (isExistingChunkFull(pos))
         {
           return false;
         }
         if (status == ChunkStatus.EMPTY && chunk.getAllStarts().values().stream().noneMatch(StructureStart::isValid)) {
           return false;
         }
       } 
       Profiler.get().incrementCounter("chunkSave");
       this.activeChunkWrites.incrementAndGet();
       SerializableChunkData data = SerializableChunkData.copyOf(this.level, chunk);
       Objects.requireNonNull(data); CompletableFuture<CompoundTag> encodedData = CompletableFuture.supplyAsync(data::write, Util.backgroundExecutor());
       Objects.requireNonNull(encodedData); write(pos, encodedData::join).handle((ignored, throwable) -> {
             if (throwable != null) {
               this.level.getServer().reportChunkSaveFailure(throwable, storageInfo(), pos);
             }
             this.activeChunkWrites.decrementAndGet();
             return null;
           });
       markPosition(pos, status.getChunkType());
       return true;
     } catch (Exception e) {
       this.level.getServer().reportChunkSaveFailure(e, storageInfo(), pos);
       return false;
     } 
   } private boolean isExistingChunkFull(ChunkPos pos) {
     CompoundTag currentTag;
     byte cachedChunkType = this.chunkTypeCache.get(pos.toLong());
     if (cachedChunkType != 0) {
       return (cachedChunkType == 1);
     }
     try {
       currentTag = (CompoundTag)((Optional)readChunk(pos).join()).orElse(null);
       if (currentTag == null) {
         markPositionReplaceable(pos);
         return false;
       } 
     } catch (Exception e) {
       LOGGER.error("Failed to read chunk {}", pos, e);
       markPositionReplaceable(pos);
       return false;
     } 
     ChunkType chunkType = SerializableChunkData.getChunkStatusFromTag(currentTag).getChunkType();
     return (markPosition(pos, chunkType) == 1);
   }
   protected void setServerViewDistance(int newViewDistance) {
     int actualNewDistance = Mth.clamp(newViewDistance, 2, 32);
     if (actualNewDistance != this.serverViewDistance) {
       this.serverViewDistance = actualNewDistance;
       this.distanceManager.updatePlayerTickets(this.serverViewDistance);
       for (ServerPlayer player : this.playerMap.getAllPlayers()) {
         updateChunkTracking(player);
       }
     } 
   }
   private int getPlayerViewDistance(ServerPlayer player) { return Mth.clamp(player.requestedViewDistance(), 2, this.serverViewDistance); }
   private void markChunkPendingToSend(ServerPlayer player, ChunkPos pos) {
     LevelChunk chunk = getChunkToSend(pos.toLong());
     if (chunk != null) {
       markChunkPendingToSend(player, chunk);
     }
   }
   private static void markChunkPendingToSend(ServerPlayer player, LevelChunk chunk) { player.connection.chunkSender.markChunkPendingToSend(chunk); }
   private static void dropChunk(ServerPlayer player, ChunkPos pos) { player.connection.chunkSender.dropChunk(player, pos); }
   public LevelChunk getChunkToSend(long key) {
     ChunkHolder chunkHolder = getVisibleChunkIfPresent(key);
     if (chunkHolder == null) {
       return null;
     }
     return chunkHolder.getChunkToSend();
   }
   public int size() { return this.visibleChunkMap.size(); }
   public DistanceManager getDistanceManager() { return this.distanceManager; }
   void dumpChunks(Writer output) throws IOException {
     CsvOutput csvOutput = CsvOutput.builder().addColumn("x").addColumn("z").addColumn("level").addColumn("in_memory").addColumn("status").addColumn("full_status").addColumn("accessible_ready").addColumn("ticking_ready").addColumn("entity_ticking_ready").addColumn("ticket").addColumn("spawning").addColumn("block_entity_count").addColumn("ticking_ticket").addColumn("ticking_level").addColumn("block_ticks").addColumn("fluid_ticks").build(output);
     for (ObjectBidirectionalIterator objectBidirectionalIterator = this.visibleChunkMap.long2ObjectEntrySet().iterator(); objectBidirectionalIterator.hasNext(); ) { Long2ObjectMap.Entry<ChunkHolder> entry = (Long2ObjectMap.Entry)objectBidirectionalIterator.next();
       long posKey = entry.getLongKey();
       ChunkPos pos = new ChunkPos(posKey);
       ChunkHolder holder = (ChunkHolder)entry.getValue();
       Optional<ChunkAccess> chunk = Optional.ofNullable(holder.getLatestChunk());
       Optional<LevelChunk> fullChunk = chunk.flatMap(chunkAccess -> (chunkAccess instanceof LevelChunk) ? Optional.of((LevelChunk)chunkAccess) : Optional.empty());
       csvOutput.writeRow(new Object[] { 
             Integer.valueOf(pos.x), 
             Integer.valueOf(pos.z), 
             Integer.valueOf(holder.getTicketLevel()), 
             Boolean.valueOf(chunk.isPresent()), chunk
             .map(ChunkAccess::getPersistedStatus).orElse(null), fullChunk
             .map(LevelChunk::getFullStatus).orElse(null), 
             printFuture(holder.getFullChunkFuture()), 
             printFuture(holder.getTickingChunkFuture()), 
             printFuture(holder.getEntityTickingChunkFuture()), this.ticketStorage
             .getTicketDebugString(posKey, false), 
             Boolean.valueOf(anyPlayerCloseEnoughForSpawning(pos)), fullChunk
             .map(c -> Integer.valueOf(c.getBlockEntities().size())).orElse(Integer.valueOf(0)), this.ticketStorage
             .getTicketDebugString(posKey, true), 
             Integer.valueOf(this.distanceManager.getChunkLevel(posKey, true)), fullChunk
             .map(levelChunk -> Integer.valueOf(levelChunk.getBlockTicks().count())).orElse(Integer.valueOf(0)), fullChunk
             .map(levelChunk -> Integer.valueOf(levelChunk.getFluidTicks().count())).orElse(Integer.valueOf(0)) }); }
   }
   private static String printFuture(CompletableFuture<ChunkResult<LevelChunk>> future) {
     try {
       ChunkResult<LevelChunk> result = (ChunkResult)future.getNow(null);
       if (result != null) {
         return result.isSuccess() ? "done" : "unloaded";
       }
       return "not completed";
     }
     catch (CompletionException e) {
       return "failed " + e.getCause().getMessage();
     } catch (CancellationException e) {
       return "cancelled";
     } 
   }
   private CompletableFuture<Optional<CompoundTag>> readChunk(ChunkPos pos) { return read(pos).thenApplyAsync(chunkTag -> chunkTag.map(this::upgradeChunkTag), Util.backgroundExecutor().forName("upgradeChunk")); }
   private CompoundTag upgradeChunkTag(CompoundTag tag) { return upgradeChunkTag(tag, -1, getChunkDataFixContextTag(this.level.dimension(), generator().getTypeNameForDataFixer())); }
   public static CompoundTag getChunkDataFixContextTag(ResourceKey<Level> dimension, Optional<ResourceKey<MapCodec<? extends ChunkGenerator>>> generator) {
     CompoundTag contextTag = new CompoundTag();
     contextTag.putString("dimension", dimension.identifier().toString());
     generator.ifPresent(k -> contextTag.putString("generator", k.identifier().toString()));
     return contextTag;
   }
   void collectSpawningChunks(List<LevelChunk> output) {
     LongIterator spawnCandidateChunks = this.distanceManager.getSpawnCandidateChunks();
     while (spawnCandidateChunks.hasNext()) {
       ChunkHolder holder = (ChunkHolder)this.visibleChunkMap.get(spawnCandidateChunks.nextLong());
       if (holder == null) {
         continue;
       }
       LevelChunk chunk = holder.getTickingChunk();
       if (chunk == null) {
         continue;
       }
       if (anyPlayerCloseEnoughForSpawningInternal(holder.getPos())) {
         output.add(chunk);
       }
     } 
   }
   void forEachBlockTickingChunk(Consumer<LevelChunk> tickingChunkConsumer) {
     this.distanceManager.forEachEntityTickingChunk(chunkPos -> {
           ChunkHolder holder = (ChunkHolder)this.visibleChunkMap.get(chunkPos);
           if (holder == null) {
             return;
           }
           LevelChunk chunk = holder.getTickingChunk();
           if (chunk == null) {
             return;
           }
           tickingChunkConsumer.accept(chunk);
         });
   }
   boolean anyPlayerCloseEnoughForSpawning(ChunkPos pos) {
     TriState triState = this.distanceManager.hasPlayersNearby(pos.toLong());
     if (triState == TriState.DEFAULT) {
       return anyPlayerCloseEnoughForSpawningInternal(pos);
     }
     return triState.toBoolean(true);
   }
   boolean anyPlayerCloseEnoughTo(BlockPos pos, int maxDistance) {
     Vec3 target = new Vec3(pos);
     for (ServerPlayer player : this.playerMap.getAllPlayers()) {
       if (playerIsCloseEnoughTo(player, target, maxDistance)) {
         return true;
       }
     } 
     return false;
   }
   private boolean anyPlayerCloseEnoughForSpawningInternal(ChunkPos pos) {
     for (ServerPlayer player : this.playerMap.getAllPlayers()) {
       if (playerIsCloseEnoughForSpawning(player, pos)) {
         return true;
       }
     } 
     return false;
   }
   public List<ServerPlayer> getPlayersCloseForSpawning(ChunkPos pos) {
     long key = pos.toLong();
     if (!this.distanceManager.hasPlayersNearby(key).toBoolean(true)) {
       return List.of();
     }
     ImmutableList.Builder<ServerPlayer> builder = ImmutableList.builder();
     for (ServerPlayer player : this.playerMap.getAllPlayers()) {
       if (playerIsCloseEnoughForSpawning(player, pos)) {
         builder.add(player);
       }
     } 
     return builder.build();
   }
   private boolean playerIsCloseEnoughForSpawning(ServerPlayer player, ChunkPos pos) {
     if (player.isSpectator()) {
       return false;
     }
     double distanceToChunk = euclideanDistanceSquared(pos, player.position());
     return (distanceToChunk < 16384.0D);
   }
   private boolean playerIsCloseEnoughTo(ServerPlayer player, Vec3 pos, int maxDistance) {
     if (player.isSpectator()) {
       return false;
     }
     double distanceToPos = player.position().distanceTo(pos);
     return (distanceToPos < maxDistance);
   }
   private static double euclideanDistanceSquared(ChunkPos chunkPos, Vec3 pos) {
     double xPos = SectionPos.sectionToBlockCoord(chunkPos.x, 8);
     double zPos = SectionPos.sectionToBlockCoord(chunkPos.z, 8);
     double xd = xPos - pos.x;
     double zd = zPos - pos.z;
     return xd * xd + zd * zd;
   }
   private boolean skipPlayer(ServerPlayer player) { return (player.isSpectator() && !((Boolean)this.level.getGameRules().get(GameRules.SPECTATORS_GENERATE_CHUNKS)).booleanValue()); }
   void updatePlayerStatus(ServerPlayer player, boolean added) {
     boolean ignored = skipPlayer(player);
     boolean wasIgnored = this.playerMap.ignoredOrUnknown(player);
     if (added) {
       this.playerMap.addPlayer(player, ignored);
       updatePlayerPos(player);
       if (!ignored) {
         this.distanceManager.addPlayer(SectionPos.of(player), player);
       }
       player.setChunkTrackingView(ChunkTrackingView.EMPTY);
       updateChunkTracking(player);
     } else {
       SectionPos lastPos = player.getLastSectionPos();
       this.playerMap.removePlayer(player);
       if (!wasIgnored) {
         this.distanceManager.removePlayer(lastPos, player);
       }
       applyChunkTrackingView(player, ChunkTrackingView.EMPTY);
     } 
   }
   private void updatePlayerPos(ServerPlayer player) {
     SectionPos pos = SectionPos.of(player);
     player.setLastSectionPos(pos);
   }
   public void move(ServerPlayer player) {
     for (ObjectIterator objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity trackedEntity = (TrackedEntity)objectIterator.next();
       if (trackedEntity.entity == player) {
         trackedEntity.updatePlayers(this.level.players()); continue;
       } 
       trackedEntity.updatePlayer(player); }
     SectionPos oldSection = player.getLastSectionPos();
     SectionPos newSection = SectionPos.of(player);
     boolean wasIgnored = this.playerMap.ignored(player);
     boolean ignored = skipPlayer(player);
     boolean positionChanged = (oldSection.asLong() != newSection.asLong());
     if (positionChanged || wasIgnored != ignored) {
       updatePlayerPos(player);
       if (!wasIgnored) {
         this.distanceManager.removePlayer(oldSection, player);
       }
       if (!ignored) {
         this.distanceManager.addPlayer(newSection, player);
       }
       if (!wasIgnored && ignored) {
         this.playerMap.ignorePlayer(player);
       }
       if (wasIgnored && !ignored) {
         this.playerMap.unIgnorePlayer(player);
       }
       updateChunkTracking(player);
     } 
   }
   private void updateChunkTracking(ServerPlayer player) {
     ChunkPos chunkPos = player.chunkPosition();
     int playerViewDistance = getPlayerViewDistance(player);
     ChunkTrackingView chunkTrackingView = player.getChunkTrackingView(); if (chunkTrackingView instanceof ChunkTrackingView.Positioned) { ChunkTrackingView.Positioned view = (ChunkTrackingView.Positioned)chunkTrackingView; if (view.center().equals(chunkPos) && view.viewDistance() == playerViewDistance)
         return;  }
     applyChunkTrackingView(player, ChunkTrackingView.of(chunkPos, playerViewDistance));
   }
   private void applyChunkTrackingView(ServerPlayer player, ChunkTrackingView next) { 
   public List<ServerPlayer> getPlayers(ChunkPos pos, boolean borderOnly) {
     Set<ServerPlayer> allPlayers = this.playerMap.getAllPlayers();
     ImmutableList.Builder<ServerPlayer> result = ImmutableList.builder();
     for (ServerPlayer player : allPlayers) {
       if ((borderOnly && isChunkOnTrackedBorder(player, pos.x, pos.z)) || (!borderOnly && 
         isChunkTracked(player, pos.x, pos.z))) {
         result.add(player);
       }
     } 
     return result.build();
   }
   protected void addEntity(Entity entity) {
     if (entity instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragonPart) {
       return;
     }
     EntityType<?> type = entity.getType();
     int range = type.clientTrackingRange() * 16;
     if (range == 0) {
       return;
     }
     int updateInterval = type.updateInterval();
     if (this.entityMap.containsKey(entity.getId())) {
       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Entity is already tracked!"));
     }
     TrackedEntity trackedEntity = new TrackedEntity(entity, range, updateInterval, type.trackDeltas());
     this.entityMap.put(entity.getId(), trackedEntity);
     trackedEntity.updatePlayers(this.level.players());
     if (entity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)entity;
       updatePlayerStatus(player, true);
       for (ObjectIterator objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity e = (TrackedEntity)objectIterator.next();
         if (e.entity != player) {
           e.updatePlayer(player);
         } }
        }
   }
   protected void removeEntity(Entity entity) {
     if (entity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)entity;
       updatePlayerStatus(player, false);
       for (ObjectIterator objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity trackedEntity = (TrackedEntity)objectIterator.next();
         trackedEntity.removePlayer(player); }
        }
     TrackedEntity trackedEntity = (TrackedEntity)this.entityMap.remove(entity.getId());
     if (trackedEntity != null) {
       trackedEntity.broadcastRemoved();
     }
   }
   protected void tick() throws IOException {
     for (ServerPlayer player : this.playerMap.getAllPlayers()) {
       updateChunkTracking(player);
     }
     List<ServerPlayer> movedPlayers = Lists.newArrayList();
     List<ServerPlayer> players = this.level.players();
     ObjectIterator objectIterator;
     for (objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity trackedEntity = (TrackedEntity)objectIterator.next();
       SectionPos oldPos = trackedEntity.lastSectionPos;
       SectionPos newPos = SectionPos.of(trackedEntity.entity);
       boolean sectionPosChanged = !Objects.equals(oldPos, newPos);
       if (sectionPosChanged) {
         trackedEntity.updatePlayers(players);
         Entity entity = trackedEntity.entity;
         if (entity instanceof ServerPlayer) {
           movedPlayers.add((ServerPlayer)entity);
         }
         trackedEntity.lastSectionPos = newPos;
       } 
       if (sectionPosChanged || trackedEntity.entity.needsSync || this.distanceManager.inEntityTickingRange(newPos.chunk().toLong())) {
         trackedEntity.serverEntity.sendChanges();
       } }
     if (!movedPlayers.isEmpty()) {
       for (objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity trackedEntity = (TrackedEntity)objectIterator.next();
         trackedEntity.updatePlayers(movedPlayers); }
     }
   }
   public void sendToTrackingPlayers(Entity entity, Packet<? super ClientGamePacketListener> packet) {
     TrackedEntity trackedEntity = (TrackedEntity)this.entityMap.get(entity.getId());
     if (trackedEntity != null) {
       trackedEntity.sendToTrackingPlayers(packet);
     }
   }
   public void sendToTrackingPlayersFiltered(Entity entity, Packet<? super ClientGamePacketListener> packet, Predicate<ServerPlayer> targetPredicate) {
     TrackedEntity trackedEntity = (TrackedEntity)this.entityMap.get(entity.getId());
     if (trackedEntity != null) {
       trackedEntity.sendToTrackingPlayersFiltered(packet, targetPredicate);
     }
   }
   protected void sendToTrackingPlayersAndSelf(Entity entity, Packet<? super ClientGamePacketListener> packet) {
     TrackedEntity trackedEntity = (TrackedEntity)this.entityMap.get(entity.getId());
     if (trackedEntity != null) {
       trackedEntity.sendToTrackingPlayersAndSelf(packet);
     }
   }
   public boolean isTrackedByAnyPlayer(Entity entity) {
     TrackedEntity trackedEntity = (TrackedEntity)this.entityMap.get(entity.getId());
     if (trackedEntity != null) {
       return !trackedEntity.seenBy.isEmpty();
     }
     return false;
   }
   public void forEachEntityTrackedBy(ServerPlayer player, Consumer<Entity> consumer) {
     for (ObjectIterator objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity entity = (TrackedEntity)objectIterator.next();
       if (entity.seenBy.contains(player.connection)) {
         consumer.accept(entity.entity);
       } }
   }
   public void resendBiomesForChunks(List<ChunkAccess> chunks) {
     Map<ServerPlayer, List<LevelChunk>> chunksForPlayers = new HashMap<ServerPlayer, List<LevelChunk>>();
     for (ChunkAccess chunkAccess : chunks) {
       LevelChunk chunk; ChunkPos pos = chunkAccess.getPos();
       if (chunkAccess instanceof LevelChunk) { LevelChunk levelChunk = (LevelChunk)chunkAccess;
         chunk = levelChunk; }
       else
       { chunk = this.level.getChunk(pos.x, pos.z); }
       for (ServerPlayer player : getPlayers(pos, false)) {
         ((List)chunksForPlayers.computeIfAbsent(player, p -> new ArrayList())).add(chunk);
       }
     } 
     chunksForPlayers.forEach((player, chunkList) -> player.connection.send(ClientboundChunksBiomesPacket.forChunks(chunkList)));
   }
   protected PoiManager getPoiManager() { return this.poiManager; }
   public String getStorageName() { return this.storageName; }
   void onFullChunkStatusChange(ChunkPos pos, FullChunkStatus status) { this.chunkStatusListener.onChunkStatusChange(pos, status); }
   public void waitForLightBeforeSending(ChunkPos centerChunk, int chunkRadius) {
     int affectedLightChunkRadius = chunkRadius + 1;
     ChunkPos.rangeClosed(centerChunk, affectedLightChunkRadius).forEach(chunkPos -> {
           ChunkHolder chunkHolder = getVisibleChunkIfPresent(chunkPos.toLong());
           if (chunkHolder != null) {
             chunkHolder.addSendDependency(this.lightEngine.waitForPendingTasks(chunkPos.x, chunkPos.z));
           }
         });
   }
   public void forEachReadyToSendChunk(Consumer<LevelChunk> consumer) {
     for (ObjectIterator objectIterator = this.visibleChunkMap.values().iterator(); objectIterator.hasNext(); ) { ChunkHolder chunkHolder = (ChunkHolder)objectIterator.next();
       LevelChunk chunk = chunkHolder.getChunkToSend();
       if (chunk != null)
         consumer.accept(chunk);  }
   }
   private class DistanceManager
     extends DistanceManager
   {
     protected DistanceManager(TicketStorage ticketStorage, Executor executor, Executor mainThreadExecutor) { super(ticketStorage, executor, mainThreadExecutor); }
     protected boolean isChunkToRemove(long node) { return ChunkMap.this.toDrop.contains(node); }
     protected ChunkHolder getChunk(long node) { return ChunkMap.this.getUpdatingChunkIfPresent(node); }
     protected ChunkHolder updateChunkScheduling(long node, int level, ChunkHolder chunk, int oldLevel) { return ChunkMap.this.updateChunkScheduling(node, level, chunk, oldLevel); }
   }
   private class TrackedEntity
     implements ServerEntity.Synchronizer {
     private final ServerEntity serverEntity;
     private final Entity entity;
     private final int range;
     private SectionPos lastSectionPos;
     private final Set<ServerPlayerConnection> seenBy;
     public TrackedEntity(Entity entity, int range, int updateInterval, boolean trackDelta) {
       this.seenBy = Sets.newIdentityHashSet();
       this.serverEntity = new ServerEntity(ChunkMap.this.level, entity, updateInterval, trackDelta, this);
       this.entity = entity;
       this.range = range;
       this.lastSectionPos = SectionPos.of(entity);
     }
     public boolean equals(Object obj) {
       if (obj instanceof TrackedEntity) {
         return (((TrackedEntity)obj).entity.getId() == this.entity.getId());
       }
       return false;
     }
     public int hashCode() { return this.entity.getId(); }
     public void sendToTrackingPlayers(Packet<? super ClientGamePacketListener> packet) {
       for (ServerPlayerConnection connection : this.seenBy) {
         connection.send(packet);
       }
     }
     public void sendToTrackingPlayersAndSelf(Packet<? super ClientGamePacketListener> packet) {
       sendToTrackingPlayers(packet);
       Entity entity1 = this.entity; if (entity1 instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)entity1;
         player.connection.send(packet); }
     }
     public void sendToTrackingPlayersFiltered(Packet<? super ClientGamePacketListener> packet, Predicate<ServerPlayer> targetPredicate) {
       for (ServerPlayerConnection connection : this.seenBy) {
         if (targetPredicate.test(connection.getPlayer())) {
           connection.send(packet);
         }
       } 
     }
     public void broadcastRemoved() throws IOException {
       for (ServerPlayerConnection connection : this.seenBy) {
         this.serverEntity.removePairing(connection.getPlayer());
       }
     }
     public void removePlayer(ServerPlayer player) {
       if (this.seenBy.remove(player.connection)) {
         this.serverEntity.removePairing(player);
         if (this.seenBy.isEmpty()) {
           ChunkMap.this.level.debugSynchronizers().dropEntity(this.entity);
         }
       } 
     }
     public void updatePlayer(ServerPlayer player) {
       if (player == this.entity) {
         return;
       }
       Vec3 deltaToPlayer = player.position().subtract(this.entity.position());
       int playerViewDistance = ChunkMap.this.getPlayerViewDistance(player);
       double visibleRange = Math.min(getEffectiveRange(), playerViewDistance * 16);
       double distanceSquared = deltaToPlayer.x * deltaToPlayer.x + deltaToPlayer.z * deltaToPlayer.z;
       double rangeSquared = visibleRange * visibleRange;
       boolean visibleToPlayer = (distanceSquared <= rangeSquared && this.entity.broadcastToPlayer(player) && ChunkMap.this.isChunkTracked(player, (this.entity.chunkPosition()).x, (this.entity.chunkPosition()).z));
       if (visibleToPlayer) {
         if (this.seenBy.add(player.connection)) {
           this.serverEntity.addPairing(player);
           if (this.seenBy.size() == 1) {
             ChunkMap.this.level.debugSynchronizers().registerEntity(this.entity);
           }
           ChunkMap.this.level.debugSynchronizers().startTrackingEntity(player, this.entity);
         } 
       } else {
         removePlayer(player);
       } 
     }
     private int scaledRange(int range) { return ChunkMap.this.level.getServer().getScaledTrackingDistance(range); }
     private int getEffectiveRange() {
       int effectiveRange = this.range;
       for (Entity passenger : this.entity.getIndirectPassengers()) {
         int passengerRange = passenger.getType().clientTrackingRange() * 16;
         if (passengerRange > effectiveRange) {
           effectiveRange = passengerRange;
         }
       } 
       return scaledRange(effectiveRange);
     }
     public void updatePlayers(List<ServerPlayer> players) {
       for (ServerPlayer player : players)
         updatePlayer(player); 
     }
   }
 }
