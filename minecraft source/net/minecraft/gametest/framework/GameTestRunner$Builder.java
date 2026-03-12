/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*     */   private final ServerLevel level;
/*     */   private final GameTestTicker testTicker;
/*     */   private GameTestRunner.GameTestBatcher batcher;
/*     */   private GameTestRunner.StructureSpawner existingStructureSpawner;
/*     */   private GameTestRunner.StructureSpawner newStructureSpawner;
/*     */   private final Collection<GameTestBatch> batches;
/*     */   private boolean haltOnError;
/*     */   private boolean clearBetweenBatches;
/*     */   
/*     */   private Builder(Collection<GameTestBatch> batches, ServerLevel level) {
/*  54 */     this.testTicker = GameTestTicker.SINGLETON;
/*  55 */     this.batcher = GameTestBatchFactory.fromGameTestInfo();
/*  56 */     this.existingStructureSpawner = GameTestRunner.StructureSpawner.IN_PLACE;
/*  57 */     this.newStructureSpawner = GameTestRunner.StructureSpawner.NOT_SET;
/*     */     
/*  59 */     this.haltOnError = false;
/*  60 */     this.clearBetweenBatches = false;
/*     */ 
/*     */     
/*  63 */     this.batches = batches;
/*  64 */     this.level = level;
/*     */   }
/*     */ 
/*     */   
/*  68 */   public static Builder fromBatches(Collection<GameTestBatch> batches, ServerLevel level) { return new Builder(batches, level); }
/*     */ 
/*     */ 
/*     */   
/*  72 */   public static Builder fromInfo(Collection<GameTestInfo> tests, ServerLevel level) { return fromBatches(GameTestBatchFactory.fromGameTestInfo().batch(tests), level); }
/*     */ 
/*     */   
/*     */   public Builder haltOnError() {
/*  76 */     this.haltOnError = true;
/*  77 */     return this;
/*     */   }
/*     */   
/*     */   public Builder clearBetweenBatches() {
/*  81 */     this.clearBetweenBatches = true;
/*  82 */     return this;
/*     */   }
/*     */   
/*     */   public Builder newStructureSpawner(GameTestRunner.StructureSpawner structureSpawner) {
/*  86 */     this.newStructureSpawner = structureSpawner;
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   public Builder existingStructureSpawner(StructureGridSpawner spawner) {
/*  91 */     this.existingStructureSpawner = spawner;
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public Builder batcher(GameTestRunner.GameTestBatcher batcher) {
/*  96 */     this.batcher = batcher;
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 101 */   public GameTestRunner build() { return new GameTestRunner(this.batcher, this.batches, this.level, this.testTicker, this.existingStructureSpawner, this.newStructureSpawner, this.haltOnError, this.clearBetweenBatches); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestRunner$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */