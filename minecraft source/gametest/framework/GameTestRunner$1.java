/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.LongArraySet;
/*     */ import net.minecraft.world.level.ChunkPos;
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
/*     */ class null
/*     */   implements GameTestListener
/*     */ {
/*     */   private void testCompleted(GameTestInfo testInfo) {
/* 179 */     testInfo.getTestInstanceBlockEntity().removeBarriers();
/* 180 */     if (currentBatchTracker.isDone()) {
/* 181 */       GameTestRunner.this.batchListeners.forEach(listener -> listener.testBatchFinished(currentBatch));
/* 182 */       LongArraySet longArraySet = new LongArraySet(GameTestRunner.this.level.getForceLoadedChunks());
/* 183 */       longArraySet.forEach(pos -> GameTestRunner.this.level.setChunkForced(ChunkPos.getX(pos), ChunkPos.getZ(pos), false));
/* 184 */       GameTestRunner.this.runBatch(batchIndex + 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void testStructureLoaded(GameTestInfo testInfo) {}
/*     */ 
/*     */ 
/*     */   
/* 194 */   public void testPassed(GameTestInfo testInfo, GameTestRunner runner) { testCompleted(testInfo); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void testFailed(GameTestInfo testInfo, GameTestRunner runner) {
/* 199 */     if (GameTestRunner.this.haltOnError) {
/* 200 */       GameTestRunner.this.endCurrentEnvironment();
/* 201 */       LongArraySet longArraySet = new LongArraySet(GameTestRunner.this.level.getForceLoadedChunks());
/* 202 */       longArraySet.forEach(pos -> GameTestRunner.this.level.setChunkForced(ChunkPos.getX(pos), ChunkPos.getZ(pos), false));
/* 203 */       GameTestTicker.SINGLETON.clear();
/* 204 */       testInfo.getTestInstanceBlockEntity().removeBarriers();
/*     */     } else {
/* 206 */       testCompleted(testInfo);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void testAddedForRerun(GameTestInfo original, GameTestInfo copy, GameTestRunner runner) {}
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestRunner$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */