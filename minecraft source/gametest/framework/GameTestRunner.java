/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.LongArraySet;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GameTestRunner
/*     */ {
/*     */   public static final int DEFAULT_TESTS_PER_ROW = 8;
/*  23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final ServerLevel level;
/*     */   
/*     */   private final GameTestTicker testTicker;
/*     */   
/*     */   private final List<GameTestInfo> allTestInfos;
/*     */   
/*     */   private ImmutableList<GameTestBatch> batches;
/*     */   
/*     */   private final List<GameTestBatchListener> batchListeners;
/*     */   private final List<GameTestInfo> scheduledForRerun;
/*     */   private final GameTestBatcher testBatcher;
/*     */   private boolean stopped;
/*     */   private Holder<TestEnvironmentDefinition> currentEnvironment;
/*     */   private final StructureSpawner existingStructureSpawner;
/*     */   private final StructureSpawner newStructureSpawner;
/*     */   private final boolean haltOnError;
/*     */   private final boolean clearBetweenBatches;
/*     */   
/*     */   public static interface StructureSpawner
/*     */   {
/*     */     default void onBatchStart(ServerLevel level) {}
/*     */     
/*  47 */     public static final StructureSpawner IN_PLACE = testInfo -> Optional.ofNullable(testInfo.prepareTestStructure())
/*  48 */       .map(());
/*  49 */     public static final StructureSpawner NOT_SET = testInfo -> Optional.empty();
/*     */     Optional<GameTestInfo> spawnStructure(GameTestInfo param1GameTestInfo); }
/*     */   public static class Builder { private final ServerLevel level; private final GameTestTicker testTicker; private GameTestRunner.GameTestBatcher batcher; private GameTestRunner.StructureSpawner existingStructureSpawner;
/*     */     
/*     */     private Builder(Collection<GameTestBatch> batches, ServerLevel level) {
/*  54 */       this.testTicker = GameTestTicker.SINGLETON;
/*  55 */       this.batcher = GameTestBatchFactory.fromGameTestInfo();
/*  56 */       this.existingStructureSpawner = GameTestRunner.StructureSpawner.IN_PLACE;
/*  57 */       this.newStructureSpawner = GameTestRunner.StructureSpawner.NOT_SET;
/*     */       
/*  59 */       this.haltOnError = false;
/*  60 */       this.clearBetweenBatches = false;
/*     */ 
/*     */       
/*  63 */       this.batches = batches;
/*  64 */       this.level = level;
/*     */     }
/*     */     private GameTestRunner.StructureSpawner newStructureSpawner; private final Collection<GameTestBatch> batches; private boolean haltOnError; private boolean clearBetweenBatches;
/*     */     
/*  68 */     public static Builder fromBatches(Collection<GameTestBatch> batches, ServerLevel level) { return new Builder(batches, level); }
/*     */ 
/*     */ 
/*     */     
/*  72 */     public static Builder fromInfo(Collection<GameTestInfo> tests, ServerLevel level) { return fromBatches(GameTestBatchFactory.fromGameTestInfo().batch(tests), level); }
/*     */ 
/*     */     
/*     */     public Builder haltOnError() {
/*  76 */       this.haltOnError = true;
/*  77 */       return this;
/*     */     }
/*     */     
/*     */     public Builder clearBetweenBatches() {
/*  81 */       this.clearBetweenBatches = true;
/*  82 */       return this;
/*     */     }
/*     */     
/*     */     public Builder newStructureSpawner(GameTestRunner.StructureSpawner structureSpawner) {
/*  86 */       this.newStructureSpawner = structureSpawner;
/*  87 */       return this;
/*     */     }
/*     */     
/*     */     public Builder existingStructureSpawner(StructureGridSpawner spawner) {
/*  91 */       this.existingStructureSpawner = spawner;
/*  92 */       return this;
/*     */     }
/*     */     
/*     */     public Builder batcher(GameTestRunner.GameTestBatcher batcher) {
/*  96 */       this.batcher = batcher;
/*  97 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 101 */     public GameTestRunner build() { return new GameTestRunner(this.batcher, this.batches, this.level, this.testTicker, this.existingStructureSpawner, this.newStructureSpawner, this.haltOnError, this.clearBetweenBatches); } }
/*     */   protected GameTestRunner(GameTestBatcher batcher, Collection<GameTestBatch> batches, ServerLevel level, GameTestTicker testTicker, StructureSpawner existingStructureSpawner, StructureSpawner newStructureSpawner, boolean haltOnError, boolean clearBetweenBatches) {
/*     */     this.batchListeners = Lists.newArrayList();
/*     */     this.scheduledForRerun = Lists.newArrayList();
/*     */     this.stopped = true;
/* 106 */     this.level = level;
/* 107 */     this.testTicker = testTicker;
/* 108 */     this.testBatcher = batcher;
/* 109 */     this.existingStructureSpawner = existingStructureSpawner;
/* 110 */     this.newStructureSpawner = newStructureSpawner;
/* 111 */     this.batches = ImmutableList.copyOf(batches);
/* 112 */     this.haltOnError = haltOnError;
/* 113 */     this.clearBetweenBatches = clearBetweenBatches;
/*     */     
/* 115 */     this.allTestInfos = (List)this.batches.stream().flatMap(batch -> batch.gameTestInfos().stream()).collect(Util.toMutableList());
/* 116 */     testTicker.setRunner(this);
/* 117 */     this.allTestInfos.forEach(info -> info.addListener(new ReportGameListener()));
/*     */   }
/*     */ 
/*     */   
/* 121 */   public List<GameTestInfo> getTestInfos() { return this.allTestInfos; }
/*     */ 
/*     */   
/*     */   public void start() {
/* 125 */     this.stopped = false;
/* 126 */     runBatch(0);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 130 */     this.stopped = true;
/* 131 */     if (this.currentEnvironment != null) {
/* 132 */       endCurrentEnvironment();
/*     */     }
/*     */   }
/*     */   
/*     */   public void rerunTest(GameTestInfo info) {
/* 137 */     GameTestInfo copy = info.copyReset();
/* 138 */     info.getListeners().forEach(listener -> listener.testAddedForRerun(info, copy, this));
/*     */     
/* 140 */     this.allTestInfos.add(copy);
/* 141 */     this.scheduledForRerun.add(copy);
/*     */     
/* 143 */     if (this.stopped) {
/* 144 */       runScheduledRerunTests();
/*     */     }
/*     */   }
/*     */   
/*     */   private void runBatch(final int batchIndex) {
/* 149 */     if (batchIndex >= this.batches.size()) {
/*     */       
/* 151 */       endCurrentEnvironment();
/* 152 */       runScheduledRerunTests(); return;
/*     */     } 
/* 154 */     if (batchIndex > 0 && this.clearBetweenBatches) {
/* 155 */       GameTestBatch lastBatch = (GameTestBatch)this.batches.get(batchIndex - 1);
/* 156 */       lastBatch.gameTestInfos().forEach(gameTestInfo -> {
/* 157 */             TestInstanceBlockEntity testInstanceBlockEntity = gameTestInfo.getTestInstanceBlockEntity();
/* 158 */             StructureUtils.clearSpaceForStructure(testInstanceBlockEntity.getStructureBoundingBox(), this.level);
/* 159 */             this.level.destroyBlock(testInstanceBlockEntity.getBlockPos(), false);
/*     */           });
/*     */     } 
/*     */     
/* 163 */     final GameTestBatch currentBatch = (GameTestBatch)this.batches.get(batchIndex);
/* 164 */     this.existingStructureSpawner.onBatchStart(this.level);
/* 165 */     this.newStructureSpawner.onBatchStart(this.level);
/* 166 */     Collection<GameTestInfo> testInfosForThisBatch = createStructuresForBatch(currentBatch.gameTestInfos());
/*     */     
/* 168 */     LOGGER.info("Running test environment '{}' batch {} ({} tests)...", new Object[] { currentBatch.environment().getRegisteredName(), Integer.valueOf(currentBatch.index()), Integer.valueOf(testInfosForThisBatch.size()) });
/* 169 */     endCurrentEnvironment();
/* 170 */     this.currentEnvironment = currentBatch.environment();
/* 171 */     ((TestEnvironmentDefinition)this.currentEnvironment.value()).setup(this.level);
/*     */     
/* 173 */     this.batchListeners.forEach(listener -> listener.testBatchStarting(currentBatch));
/*     */     
/* 175 */     final MultipleTestTracker currentBatchTracker = new MultipleTestTracker();
/* 176 */     Objects.requireNonNull(currentBatchTracker); testInfosForThisBatch.forEach(currentBatchTracker::addTestToTrack);
/* 177 */     currentBatchTracker.addListener(new GameTestListener() {
/*     */           private void testCompleted(GameTestInfo testInfo) {
/* 179 */             testInfo.getTestInstanceBlockEntity().removeBarriers();
/* 180 */             if (currentBatchTracker.isDone()) {
/* 181 */               GameTestRunner.this.batchListeners.forEach(listener -> listener.testBatchFinished(currentBatch));
/* 182 */               LongArraySet longArraySet = new LongArraySet(GameTestRunner.this.level.getForceLoadedChunks());
/* 183 */               longArraySet.forEach(pos -> GameTestRunner.this.level.setChunkForced(ChunkPos.getX(pos), ChunkPos.getZ(pos), false));
/* 184 */               GameTestRunner.this.runBatch(batchIndex + 1);
/*     */             } 
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void testStructureLoaded(GameTestInfo testInfo) {}
/*     */ 
/*     */ 
/*     */           
/* 194 */           public void testPassed(GameTestInfo testInfo, GameTestRunner runner) { testCompleted(testInfo); }
/*     */ 
/*     */ 
/*     */           
/*     */           public void testFailed(GameTestInfo testInfo, GameTestRunner runner) {
/* 199 */             if (GameTestRunner.this.haltOnError) {
/* 200 */               GameTestRunner.this.endCurrentEnvironment();
/* 201 */               LongArraySet longArraySet = new LongArraySet(GameTestRunner.this.level.getForceLoadedChunks());
/* 202 */               longArraySet.forEach(pos -> GameTestRunner.this.level.setChunkForced(ChunkPos.getX(pos), ChunkPos.getZ(pos), false));
/* 203 */               GameTestTicker.SINGLETON.clear();
/* 204 */               testInfo.getTestInstanceBlockEntity().removeBarriers();
/*     */             } else {
/* 206 */               testCompleted(testInfo);
/*     */             } 
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void testAddedForRerun(GameTestInfo original, GameTestInfo copy, GameTestRunner runner) {}
/*     */         });
/* 215 */     Objects.requireNonNull(this.testTicker); testInfosForThisBatch.forEach(this.testTicker::add);
/*     */   }
/*     */   
/*     */   private void endCurrentEnvironment() {
/* 219 */     if (this.currentEnvironment != null) {
/* 220 */       ((TestEnvironmentDefinition)this.currentEnvironment.value()).teardown(this.level);
/* 221 */       this.currentEnvironment = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void runScheduledRerunTests() {
/* 226 */     if (!this.scheduledForRerun.isEmpty()) {
/* 227 */       LOGGER.info("Starting re-run of tests: {}", this.scheduledForRerun.stream().map(info -> info.id().toString()).collect(Collectors.joining(", ")));
/* 228 */       this.batches = ImmutableList.copyOf(this.testBatcher.batch(this.scheduledForRerun));
/* 229 */       this.scheduledForRerun.clear();
/* 230 */       this.stopped = false;
/* 231 */       runBatch(0);
/*     */     } else {
/* 233 */       this.batches = ImmutableList.of();
/* 234 */       this.stopped = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 239 */   public void addListener(GameTestBatchListener listener) { this.batchListeners.add(listener); }
/*     */ 
/*     */ 
/*     */   
/* 243 */   private Collection<GameTestInfo> createStructuresForBatch(Collection<GameTestInfo> batch) { return batch.stream().map(this::spawn).flatMap(Optional::stream).toList(); }
/*     */ 
/*     */   
/*     */   private Optional<GameTestInfo> spawn(GameTestInfo testInfo) {
/* 247 */     if (testInfo.getTestBlockPos() == null) {
/* 248 */       return this.newStructureSpawner.spawnStructure(testInfo);
/*     */     }
/* 250 */     return this.existingStructureSpawner.spawnStructure(testInfo);
/*     */   }
/*     */   
/*     */   public static interface GameTestBatcher {
/*     */     Collection<GameTestBatch> batch(Collection<GameTestInfo> param1Collection);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestRunner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */