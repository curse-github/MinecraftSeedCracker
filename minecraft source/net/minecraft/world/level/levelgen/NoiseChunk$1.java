/*     */ package net.minecraft.world.level.levelgen;
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
/*     */   implements DensityFunction.ContextProvider
/*     */ {
/*     */   public DensityFunction.FunctionContext forIndex(int cellYIndex) {
/*  83 */     NoiseChunk.this.cellStartBlockY = (cellYIndex + NoiseChunk.this.cellNoiseMinY) * NoiseChunk.this.cellHeight;
/*  84 */     NoiseChunk.this.interpolationCounter++;
/*     */     
/*  86 */     NoiseChunk.this.inCellY = 0;
/*  87 */     NoiseChunk.this.arrayIndex = cellYIndex;
/*  88 */     return NoiseChunk.this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillAllDirectly(double[] output, DensityFunction function) {
/*  94 */     for (int cellYIndex = 0; cellYIndex < NoiseChunk.this.cellCountY + 1; cellYIndex++) {
/*  95 */       NoiseChunk.this.cellStartBlockY = (cellYIndex + NoiseChunk.this.cellNoiseMinY) * NoiseChunk.this.cellHeight;
/*  96 */       NoiseChunk.this.interpolationCounter++;
/*     */       
/*  98 */       NoiseChunk.this.inCellY = 0;
/*  99 */       NoiseChunk.this.arrayIndex = cellYIndex;
/*     */       
/* 101 */       output[cellYIndex] = function.compute(NoiseChunk.this);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\NoiseChunk$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */