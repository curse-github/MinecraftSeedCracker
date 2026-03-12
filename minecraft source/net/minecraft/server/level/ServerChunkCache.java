/*     */ package net.minecraft.server.level;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.thread.BlockableEventLoop;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.LocalMobCapCalculator;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.TicketStorage;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
/*     */ import net.minecraft.world.level.chunk.ChunkSource;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.LightChunk;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.storage.ChunkScanAccess;
/*     */ import net.minecraft.world.level.entity.ChunkStatusUpdateListener;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.storage.DimensionDataStorage;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerChunkCache extends ChunkSource {
/*  59 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final DistanceManager distanceManager;
/*     */   
/*     */   private final ServerLevel level;
/*     */   
/*     */   private final Thread mainThread;
/*     */   
/*     */   private final ThreadedLevelLightEngine lightEngine;
/*     */   private final MainThreadExecutor mainThreadProcessor;
/*     */   public final ChunkMap chunkMap;
/*     */   private final DimensionDataStorage dataStorage;
/*     */   private final TicketStorage ticketStorage;
/*     */   
/*     */   public ServerChunkCache(ServerLevel level, LevelStorageSource.LevelStorageAccess levelStorage, DataFixer fixerUpper, StructureTemplateManager structureTemplateManager, Executor executor, ChunkGenerator generator, int viewDistance, int simulationDistance, boolean syncWrites, ChunkStatusUpdateListener chunkStatusListener, Supplier<DimensionDataStorage> overworldDataStorage) {
/*  74 */     this.spawnEnemies = true;
/*     */ 
/*     */     
/*  77 */     this.lastChunkPos = new long[4];
/*  78 */     this.lastChunkStatus = new ChunkStatus[4];
/*  79 */     this.lastChunk = new ChunkAccess[4];
/*  80 */     this.spawningChunks = new ObjectArrayList();
/*  81 */     this.chunkHoldersToBroadcast = new ReferenceOpenHashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     this.level = level;
/*  88 */     this.mainThreadProcessor = new MainThreadExecutor(level);
/*  89 */     this.mainThread = Thread.currentThread();
/*     */     
/*  91 */     Path dataFolder = levelStorage.getDimensionPath(level.dimension()).resolve("data");
/*     */     try {
/*  93 */       FileUtil.createDirectoriesSafe(dataFolder);
/*  94 */     } catch (IOException e) {
/*  95 */       LOGGER.error("Failed to create dimension data storage directory", e);
/*     */     } 
/*     */     
/*  98 */     this.dataStorage = new DimensionDataStorage(dataFolder, fixerUpper, level.registryAccess());
/*  99 */     this.ticketStorage = (TicketStorage)this.dataStorage.computeIfAbsent(TicketStorage.TYPE);
/*     */     
/* 101 */     this.chunkMap = new ChunkMap(level, levelStorage, fixerUpper, structureTemplateManager, executor, this.mainThreadProcessor, this, generator, chunkStatusListener, overworldDataStorage, this.ticketStorage, viewDistance, syncWrites);
/* 102 */     this.lightEngine = this.chunkMap.getLightEngine();
/* 103 */     this.distanceManager = this.chunkMap.getDistanceManager();
/* 104 */     this.distanceManager.updateSimulationDistance(simulationDistance);
/* 105 */     clearCache();
/*     */   }
/*     */   private long lastInhabitedUpdate; private boolean spawnEnemies; private static final int CACHE_SIZE = 4; private final long[] lastChunkPos; private final ChunkStatus[] lastChunkStatus; private final ChunkAccess[] lastChunk; private final List<LevelChunk> spawningChunks; private final Set<ChunkHolder> chunkHoldersToBroadcast; @VisibleForDebug
/*     */   private NaturalSpawner.SpawnState lastSpawnState;
/*     */   
/* 110 */   public ThreadedLevelLightEngine getLightEngine() { return this.lightEngine; }
/*     */ 
/*     */ 
/*     */   
/* 114 */   private ChunkHolder getVisibleChunkIfPresent(long key) { return this.chunkMap.getVisibleChunkIfPresent(key); }
/*     */ 
/*     */   
/*     */   private void storeInCache(long pos, ChunkAccess chunk, ChunkStatus status) {
/* 118 */     for (int i = 3; i > 0; i--) {
/* 119 */       this.lastChunkPos[i] = this.lastChunkPos[i - 1];
/* 120 */       this.lastChunkStatus[i] = this.lastChunkStatus[i - 1];
/* 121 */       this.lastChunk[i] = this.lastChunk[i - 1];
/*     */     } 
/* 123 */     this.lastChunkPos[0] = pos;
/* 124 */     this.lastChunkStatus[0] = status;
/* 125 */     this.lastChunk[0] = chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkAccess getChunk(int x, int z, ChunkStatus targetStatus, boolean loadOrGenerate) {
/* 130 */     if (Thread.currentThread() != this.mainThread) {
/* 131 */       return (ChunkAccess)CompletableFuture.supplyAsync(() -> getChunk(x, z, targetStatus, loadOrGenerate), this.mainThreadProcessor).join();
/*     */     }
/* 133 */     ProfilerFiller profiler = Profiler.get();
/* 134 */     profiler.incrementCounter("getChunk");
/*     */     
/* 136 */     long pos = ChunkPos.asLong(x, z);
/* 137 */     for (int i = 0; i < 4; i++) {
/* 138 */       if (pos == this.lastChunkPos[i] && targetStatus == this.lastChunkStatus[i]) {
/* 139 */         ChunkAccess chunkAccess = this.lastChunk[i];
/* 140 */         if (chunkAccess != null || !loadOrGenerate) {
/* 141 */           return chunkAccess;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 146 */     profiler.incrementCounter("getChunkCacheMiss");
/* 147 */     CompletableFuture<ChunkResult<ChunkAccess>> serverFuture = getChunkFutureMainThread(x, z, targetStatus, loadOrGenerate);
/* 148 */     Objects.requireNonNull(serverFuture); this.mainThreadProcessor.managedBlock(serverFuture::isDone);
/*     */     
/* 150 */     ChunkResult<ChunkAccess> chunkResult = (ChunkResult)serverFuture.join();
/* 151 */     ChunkAccess chunk = (ChunkAccess)chunkResult.orElse(null);
/* 152 */     if (chunk == null && 
/* 153 */       loadOrGenerate) {
/* 154 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Chunk not there when requested: " + chunkResult.getError()));
/*     */     }
/*     */ 
/*     */     
/* 158 */     storeInCache(pos, chunk, targetStatus);
/* 159 */     return chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelChunk getChunkNow(int x, int z) {
/* 164 */     if (Thread.currentThread() != this.mainThread)
/*     */     {
/* 166 */       return null;
/*     */     }
/* 168 */     Profiler.get().incrementCounter("getChunkNow");
/*     */     
/* 170 */     long pos = ChunkPos.asLong(x, z);
/* 171 */     for (int i = 0; i < 4; i++) {
/* 172 */       if (pos == this.lastChunkPos[i] && this.lastChunkStatus[i] == ChunkStatus.FULL) {
/* 173 */         ChunkAccess chunkAccess = this.lastChunk[i];
/* 174 */         return (chunkAccess instanceof LevelChunk) ? (LevelChunk)chunkAccess : null;
/*     */       } 
/*     */     } 
/*     */     
/* 178 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(pos);
/* 179 */     if (chunkHolder == null) {
/* 180 */       return null;
/*     */     }
/* 182 */     ChunkAccess chunk = chunkHolder.getChunkIfPresent(ChunkStatus.FULL);
/* 183 */     if (chunk != null) {
/* 184 */       storeInCache(pos, chunk, ChunkStatus.FULL);
/* 185 */       if (chunk instanceof LevelChunk) {
/* 186 */         return (LevelChunk)chunk;
/*     */       }
/*     */     } 
/* 189 */     return null;
/*     */   }
/*     */   
/*     */   private void clearCache() {
/* 193 */     Arrays.fill(this.lastChunkPos, ChunkPos.INVALID_CHUNK_POS);
/* 194 */     Arrays.fill(this.lastChunkStatus, null);
/* 195 */     Arrays.fill(this.lastChunk, null);
/*     */   }
/*     */   public CompletableFuture<ChunkResult<ChunkAccess>> getChunkFuture(int x, int z, ChunkStatus targetStatus, boolean loadOrGenerate) {
/*     */     CompletableFuture<ChunkResult<ChunkAccess>> serverFuture;
/* 199 */     boolean isMainThread = (Thread.currentThread() == this.mainThread);
/*     */     
/* 201 */     if (isMainThread) {
/* 202 */       serverFuture = getChunkFutureMainThread(x, z, targetStatus, loadOrGenerate);
/* 203 */       Objects.requireNonNull(serverFuture); this.mainThreadProcessor.managedBlock(serverFuture::isDone);
/*     */     } else {
/* 205 */       serverFuture = CompletableFuture.supplyAsync(() -> getChunkFutureMainThread(x, z, targetStatus, loadOrGenerate), this.mainThreadProcessor).thenCompose(chunk -> chunk);
/*     */     } 
/* 207 */     return serverFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CompletableFuture<ChunkResult<ChunkAccess>> getChunkFutureMainThread(int x, int z, ChunkStatus targetStatus, boolean loadOrGenerate) {
/* 214 */     ChunkPos pos = new ChunkPos(x, z);
/* 215 */     long key = pos.toLong();
/* 216 */     int targetTicketLevel = ChunkLevel.byStatus(targetStatus);
/*     */     
/* 218 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(key);
/* 219 */     if (loadOrGenerate) {
/*     */       
/* 221 */       addTicket(new Ticket(TicketType.UNKNOWN, targetTicketLevel), pos);
/*     */       
/* 223 */       if (chunkAbsent(chunkHolder, targetTicketLevel)) {
/* 224 */         ProfilerFiller profiler = Profiler.get();
/* 225 */         profiler.push("chunkLoad");
/* 226 */         runDistanceManagerUpdates();
/* 227 */         chunkHolder = getVisibleChunkIfPresent(key);
/* 228 */         profiler.pop();
/* 229 */         if (chunkAbsent(chunkHolder, targetTicketLevel)) {
/* 230 */           throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("No chunk holder after ticket has been added"));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 235 */     if (chunkAbsent(chunkHolder, targetTicketLevel)) {
/* 236 */       return GenerationChunkHolder.UNLOADED_CHUNK_FUTURE;
/*     */     }
/*     */     
/* 239 */     return chunkHolder.scheduleChunkGenerationTask(targetStatus, this.chunkMap);
/*     */   }
/*     */ 
/*     */   
/* 243 */   private boolean chunkAbsent(ChunkHolder chunkHolder, int targetTicketLevel) { return (chunkHolder == null || chunkHolder.getTicketLevel() > targetTicketLevel); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasChunk(int x, int z) {
/* 248 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent((new ChunkPos(x, z)).toLong());
/* 249 */     int targetTicketLevel = ChunkLevel.byStatus(ChunkStatus.FULL);
/*     */     
/* 251 */     return !chunkAbsent(chunkHolder, targetTicketLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LightChunk getChunkForLighting(int x, int z) {
/* 257 */     long key = ChunkPos.asLong(x, z);
/* 258 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(key);
/* 259 */     if (chunkHolder == null) {
/* 260 */       return null;
/*     */     }
/*     */     
/* 263 */     return chunkHolder.getChunkIfPresentUnchecked(ChunkStatus.INITIALIZE_LIGHT.getParent());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 268 */   public Level getLevel() { return this.level; }
/*     */ 
/*     */ 
/*     */   
/* 272 */   public boolean pollTask() { return this.mainThreadProcessor.pollTask(); }
/*     */ 
/*     */   
/*     */   boolean runDistanceManagerUpdates() {
/* 276 */     boolean updated = this.distanceManager.runAllUpdates(this.chunkMap);
/* 277 */     boolean promoted = this.chunkMap.promoteChunkMap();
/* 278 */     this.chunkMap.runGenerationTasks();
/* 279 */     if (updated || promoted) {
/* 280 */       clearCache();
/* 281 */       return true;
/*     */     } 
/* 283 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isPositionTicking(long chunkKey) {
/* 287 */     if (!this.level.shouldTickBlocksAt(chunkKey)) {
/* 288 */       return false;
/*     */     }
/* 290 */     ChunkHolder holder = getVisibleChunkIfPresent(chunkKey);
/* 291 */     if (holder == null) {
/* 292 */       return false;
/*     */     }
/* 294 */     return ((ChunkResult)holder.getTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK)).isSuccess();
/*     */   }
/*     */   
/*     */   public void save(boolean flushStorage) {
/* 298 */     runDistanceManagerUpdates();
/* 299 */     this.chunkMap.saveAllChunks(flushStorage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 304 */     save(true);
/* 305 */     this.dataStorage.close();
/* 306 */     this.lightEngine.close();
/* 307 */     this.chunkMap.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BooleanSupplier haveTime, boolean tickChunks) {
/* 312 */     ProfilerFiller profiler = Profiler.get();
/* 313 */     profiler.push("purge");
/* 314 */     if (this.level.tickRateManager().runsNormally() || !tickChunks)
/*     */     {
/* 316 */       this.ticketStorage.purgeStaleTickets(this.chunkMap);
/*     */     }
/* 318 */     runDistanceManagerUpdates();
/* 319 */     profiler.popPush("chunks");
/* 320 */     if (tickChunks) {
/* 321 */       tickChunks();
/* 322 */       this.chunkMap.tick();
/*     */     } 
/* 324 */     profiler.popPush("unload");
/* 325 */     this.chunkMap.tick(haveTime);
/* 326 */     profiler.pop();
/* 327 */     clearCache();
/*     */   }
/*     */   
/*     */   private void tickChunks() {
/* 331 */     long time = this.level.getGameTime();
/* 332 */     long timeDiff = time - this.lastInhabitedUpdate;
/* 333 */     this.lastInhabitedUpdate = time;
/*     */     
/* 335 */     if (this.level.isDebug()) {
/*     */       return;
/*     */     }
/*     */     
/* 339 */     ProfilerFiller profiler = Profiler.get();
/* 340 */     profiler.push("pollingChunks");
/*     */     
/* 342 */     if (this.level.tickRateManager().runsNormally()) {
/* 343 */       profiler.push("tickingChunks");
/* 344 */       tickChunks(profiler, timeDiff);
/* 345 */       profiler.pop();
/*     */     } 
/*     */     
/* 348 */     broadcastChangedChunks(profiler);
/*     */     
/* 350 */     profiler.pop();
/*     */   }
/*     */   
/*     */   private void broadcastChangedChunks(ProfilerFiller profiler) {
/* 354 */     profiler.push("broadcast");
/*     */     
/* 356 */     for (ChunkHolder chunkHolder : this.chunkHoldersToBroadcast) {
/* 357 */       LevelChunk chunk = chunkHolder.getTickingChunk();
/* 358 */       if (chunk != null) {
/* 359 */         chunkHolder.broadcastChanges(chunk);
/*     */       }
/*     */     } 
/* 362 */     this.chunkHoldersToBroadcast.clear();
/*     */     
/* 364 */     profiler.pop();
/*     */   }
/*     */   private void tickChunks(ProfilerFiller profiler, long timeDiff) {
/*     */     List<MobCategory> spawningCategories;
/* 368 */     profiler.push("naturalSpawnCount");
/* 369 */     int chunkCount = this.distanceManager.getNaturalSpawnChunkCount();
/* 370 */     NaturalSpawner.SpawnState spawnCookie = NaturalSpawner.createState(chunkCount, this.level.getAllEntities(), this::getFullChunk, new LocalMobCapCalculator(this.chunkMap));
/* 371 */     this.lastSpawnState = spawnCookie;
/*     */     
/* 373 */     boolean doMobSpawning = ((Boolean)this.level.getGameRules().get(GameRules.SPAWN_MOBS)).booleanValue();
/* 374 */     int tickSpeed = ((Integer)this.level.getGameRules().get(GameRules.RANDOM_TICK_SPEED)).intValue();
/*     */ 
/*     */     
/* 377 */     if (doMobSpawning) {
/* 378 */       boolean spawnPersistent = (this.level.getGameTime() % 400L == 0L);
/* 379 */       spawningCategories = NaturalSpawner.getFilteredSpawningCategories(spawnCookie, true, this.spawnEnemies, spawnPersistent);
/*     */     } else {
/* 381 */       spawningCategories = List.of();
/*     */     } 
/*     */     
/* 384 */     spawningChunks = this.spawningChunks;
/*     */     try {
/* 386 */       profiler.popPush("filteringSpawningChunks");
/* 387 */       this.chunkMap.collectSpawningChunks(spawningChunks);
/* 388 */       profiler.popPush("shuffleSpawningChunks");
/*     */       
/* 390 */       Util.shuffle(spawningChunks, this.level.random);
/*     */       
/* 392 */       profiler.popPush("tickSpawningChunks");
/* 393 */       for (LevelChunk chunk : spawningChunks) {
/* 394 */         tickSpawningChunk(chunk, timeDiff, spawningCategories, spawnCookie);
/*     */       }
/*     */     } finally {
/* 397 */       spawningChunks.clear();
/*     */     } 
/*     */     
/* 400 */     profiler.popPush("tickTickingChunks");
/* 401 */     this.chunkMap.forEachBlockTickingChunk(chunk -> this.level.tickChunk(chunk, tickSpeed));
/*     */     
/* 403 */     if (doMobSpawning) {
/* 404 */       profiler.popPush("customSpawners");
/* 405 */       this.level.tickCustomSpawners(this.spawnEnemies);
/*     */     } 
/* 407 */     profiler.pop();
/*     */   }
/*     */   
/*     */   private void tickSpawningChunk(LevelChunk chunk, long timeDiff, List<MobCategory> spawningCategories, NaturalSpawner.SpawnState spawnCookie) {
/* 411 */     ChunkPos chunkPos = chunk.getPos();
/* 412 */     chunk.incrementInhabitedTime(timeDiff);
/* 413 */     if (this.distanceManager.inEntityTickingRange(chunkPos.toLong())) {
/* 414 */       this.level.tickThunder(chunk);
/*     */     }
/* 416 */     if (spawningCategories.isEmpty()) {
/*     */       return;
/*     */     }
/* 419 */     if (this.level.canSpawnEntitiesInChunk(chunkPos)) {
/* 420 */       NaturalSpawner.spawnForChunk(this.level, chunk, spawnCookie, spawningCategories);
/*     */     }
/*     */   }
/*     */   
/*     */   private void getFullChunk(long chunkKey, Consumer<LevelChunk> output) {
/* 425 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(chunkKey);
/*     */     
/* 427 */     if (chunkHolder != null) {
/* 428 */       ((ChunkResult)chunkHolder.getFullChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK)).ifSuccess(output);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 434 */   public String gatherStats() { return Integer.toString(getLoadedChunksCount()); }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 439 */   public int getPendingTasksCount() { return this.mainThreadProcessor.getPendingTasksCount(); }
/*     */ 
/*     */ 
/*     */   
/* 443 */   public ChunkGenerator getGenerator() { return this.chunkMap.generator(); }
/*     */ 
/*     */ 
/*     */   
/* 447 */   public ChunkGeneratorStructureState getGeneratorState() { return this.chunkMap.generatorState(); }
/*     */ 
/*     */ 
/*     */   
/* 451 */   public RandomState randomState() { return this.chunkMap.randomState(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 456 */   public int getLoadedChunksCount() { return this.chunkMap.size(); }
/*     */ 
/*     */   
/*     */   public void blockChanged(BlockPos pos) {
/* 460 */     int xc = SectionPos.blockToSectionCoord(pos.getX());
/* 461 */     int zc = SectionPos.blockToSectionCoord(pos.getZ());
/* 462 */     ChunkHolder chunk = getVisibleChunkIfPresent(ChunkPos.asLong(xc, zc));
/* 463 */     if (chunk != null && chunk.blockChanged(pos)) {
/* 464 */       this.chunkHoldersToBroadcast.add(chunk);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLightUpdate(LightLayer layer, SectionPos pos) {
/* 470 */     this.mainThreadProcessor.execute(() -> {
/* 471 */           ChunkHolder chunk = getVisibleChunkIfPresent(pos.chunk().toLong());
/* 472 */           if (chunk != null && chunk.sectionLightChanged(layer, pos.y())) {
/* 473 */             this.chunkHoldersToBroadcast.add(chunk);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/* 479 */   public boolean hasActiveTickets() { return this.ticketStorage.shouldKeepDimensionActive(); }
/*     */ 
/*     */ 
/*     */   
/* 483 */   public void addTicket(Ticket ticket, ChunkPos pos) { this.ticketStorage.addTicket(ticket, pos); }
/*     */ 
/*     */   
/*     */   public CompletableFuture<?> addTicketAndLoadWithRadius(TicketType type, ChunkPos pos, int radius) {
/* 487 */     if (!type.doesLoad())
/* 488 */       throw new IllegalStateException("Ticket type " + String.valueOf(type) + " does not trigger chunk loading"); 
/* 489 */     if (type.canExpireIfUnloaded()) {
/* 490 */       throw new IllegalStateException("Ticket type " + String.valueOf(type) + " can expire before it loads, cannot fetch asynchronously");
/*     */     }
/* 492 */     addTicketWithRadius(type, pos, radius);
/* 493 */     runDistanceManagerUpdates();
/* 494 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(pos.toLong());
/* 495 */     Objects.requireNonNull(chunkHolder, "No chunk was scheduled for loading");
/* 496 */     return this.chunkMap.getChunkRangeFuture(chunkHolder, radius, distance -> ChunkStatus.FULL);
/*     */   }
/*     */ 
/*     */   
/* 500 */   public void addTicketWithRadius(TicketType type, ChunkPos pos, int radius) { this.ticketStorage.addTicketWithRadius(type, pos, radius); }
/*     */ 
/*     */ 
/*     */   
/* 504 */   public void removeTicketWithRadius(TicketType type, ChunkPos pos, int radius) { this.ticketStorage.removeTicketWithRadius(type, pos, radius); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 509 */   public boolean updateChunkForced(ChunkPos pos, boolean forced) { return this.ticketStorage.updateChunkForced(pos, forced); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 514 */   public LongSet getForceLoadedChunks() { return this.ticketStorage.getForceLoadedChunks(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void move(ServerPlayer player) {
/* 521 */     if (!player.isRemoved()) {
/* 522 */       this.chunkMap.move(player);
/* 523 */       if (player.isReceivingWaypoints()) {
/* 524 */         this.level.getWaypointManager().updatePlayer(player);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 532 */   public void removeEntity(Entity entity) { this.chunkMap.removeEntity(entity); }
/*     */ 
/*     */ 
/*     */   
/* 536 */   public void addEntity(Entity entity) { this.chunkMap.addEntity(entity); }
/*     */ 
/*     */ 
/*     */   
/* 540 */   public void sendToTrackingPlayersAndSelf(Entity entity, Packet<? super ClientGamePacketListener> packet) { this.chunkMap.sendToTrackingPlayersAndSelf(entity, packet); }
/*     */ 
/*     */ 
/*     */   
/* 544 */   public void sendToTrackingPlayers(Entity entity, Packet<? super ClientGamePacketListener> packet) { this.chunkMap.sendToTrackingPlayers(entity, packet); }
/*     */ 
/*     */ 
/*     */   
/* 548 */   public void setViewDistance(int newDistance) { this.chunkMap.setServerViewDistance(newDistance); }
/*     */ 
/*     */ 
/*     */   
/* 552 */   public void setSimulationDistance(int simulationDistance) { this.distanceManager.updateSimulationDistance(simulationDistance); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 557 */   public void setSpawnSettings(boolean spawnEnemies) { this.spawnEnemies = spawnEnemies; }
/*     */ 
/*     */ 
/*     */   
/* 561 */   public String getChunkDebugData(ChunkPos pos) { return this.chunkMap.getChunkDebugData(pos); }
/*     */ 
/*     */ 
/*     */   
/* 565 */   public DimensionDataStorage getDataStorage() { return this.dataStorage; }
/*     */ 
/*     */ 
/*     */   
/* 569 */   public PoiManager getPoiManager() { return this.chunkMap.getPoiManager(); }
/*     */ 
/*     */ 
/*     */   
/* 573 */   public ChunkScanAccess chunkScanner() { return this.chunkMap.chunkScanner(); }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/* 578 */   public NaturalSpawner.SpawnState getLastSpawnState() { return this.lastSpawnState; }
/*     */ 
/*     */ 
/*     */   
/* 582 */   public void deactivateTicketsOnClosing() { this.ticketStorage.deactivateTicketsOnClosing(); }
/*     */ 
/*     */   
/*     */   public void onChunkReadyToSend(ChunkHolder chunk) {
/* 586 */     if (chunk.hasChangesToBroadcast())
/* 587 */       this.chunkHoldersToBroadcast.add(chunk); 
/*     */   }
/*     */   
/*     */   private final class MainThreadExecutor
/*     */     extends BlockableEventLoop<Runnable>
/*     */   {
/* 593 */     private MainThreadExecutor(Level level) { super("Chunk source main thread executor for " + String.valueOf(level.dimension().identifier())); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 598 */     public void managedBlock(BooleanSupplier condition) { super.managedBlock(() -> (MinecraftServer.throwIfFatalException() && condition.getAsBoolean())); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 603 */     public Runnable wrapRunnable(Runnable runnable) { return runnable; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 608 */     protected boolean shouldRun(Runnable task) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 614 */     protected boolean scheduleExecutables() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 619 */     protected Thread getRunningThread() { return ServerChunkCache.this.mainThread; }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void doRunTask(Runnable task) {
/* 624 */       Profiler.get().incrementCounter("runTask");
/* 625 */       super.doRunTask(task);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean pollTask() {
/* 630 */       if (ServerChunkCache.this.runDistanceManagerUpdates()) {
/* 631 */         return true;
/*     */       }
/* 633 */       ServerChunkCache.this.lightEngine.tryScheduleUpdate();
/* 634 */       return super.pollTask();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ServerChunkCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */