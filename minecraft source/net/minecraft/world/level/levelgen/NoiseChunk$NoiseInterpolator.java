/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NoiseInterpolator
/*     */   implements NoiseChunk.NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked
/*     */ {
/*     */   private double[][] slice0;
/*     */   private double[][] slice1;
/*     */   private final DensityFunction noiseFiller;
/*     */   private double noise000;
/*     */   private double noise001;
/*     */   private double noise100;
/*     */   private double noise101;
/*     */   private double noise010;
/*     */   private double noise011;
/*     */   private double noise110;
/*     */   private double noise111;
/*     */   private double valueXZ00;
/*     */   private double valueXZ10;
/*     */   private double valueXZ01;
/*     */   private double valueXZ11;
/*     */   private double valueZ0;
/*     */   private double valueZ1;
/*     */   private double value;
/*     */   
/*     */   private NoiseInterpolator(DensityFunction noiseFiller) {
/* 571 */     this.noiseFiller = noiseFiller;
/* 572 */     this.slice0 = allocateSlice(NoiseChunk.this.cellCountY, NoiseChunk.this.cellCountXZ);
/* 573 */     this.slice1 = allocateSlice(NoiseChunk.this.cellCountY, NoiseChunk.this.cellCountXZ);
/*     */     
/* 575 */     NoiseChunk.this.interpolators.add(this);
/*     */   }
/*     */   
/*     */   private double[][] allocateSlice(int cellCountY, int cellCountZ) {
/* 579 */     int sizeZ = cellCountZ + 1;
/* 580 */     int sizeY = cellCountY + 1;
/* 581 */     double[][] result = new double[sizeZ][sizeY];
/* 582 */     for (int cellZIndex = 0; cellZIndex < sizeZ; cellZIndex++) {
/* 583 */       result[cellZIndex] = new double[sizeY];
/*     */     }
/* 585 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void selectCellYZ(int cellYIndex, int cellZIndex) {
/* 595 */     this.noise000 = this.slice0[cellZIndex][cellYIndex];
/* 596 */     this.noise001 = this.slice0[cellZIndex + 1][cellYIndex];
/* 597 */     this.noise100 = this.slice1[cellZIndex][cellYIndex];
/* 598 */     this.noise101 = this.slice1[cellZIndex + 1][cellYIndex];
/*     */     
/* 600 */     this.noise010 = this.slice0[cellZIndex][cellYIndex + 1];
/* 601 */     this.noise011 = this.slice0[cellZIndex + 1][cellYIndex + 1];
/* 602 */     this.noise110 = this.slice1[cellZIndex][cellYIndex + 1];
/* 603 */     this.noise111 = this.slice1[cellZIndex + 1][cellYIndex + 1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateForY(double factorY) {
/* 612 */     this.valueXZ00 = Mth.lerp(factorY, this.noise000, this.noise010);
/* 613 */     this.valueXZ10 = Mth.lerp(factorY, this.noise100, this.noise110);
/* 614 */     this.valueXZ01 = Mth.lerp(factorY, this.noise001, this.noise011);
/* 615 */     this.valueXZ11 = Mth.lerp(factorY, this.noise101, this.noise111);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateForX(double factorX) {
/* 624 */     this.valueZ0 = Mth.lerp(factorX, this.valueXZ00, this.valueXZ10);
/* 625 */     this.valueZ1 = Mth.lerp(factorX, this.valueXZ01, this.valueXZ11);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 634 */   private void updateForZ(double factorZ) { this.value = Mth.lerp(factorZ, this.valueZ0, this.valueZ1); }
/*     */ 
/*     */ 
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 639 */     if (context != NoiseChunk.this) {
/* 640 */       return this.noiseFiller.compute(context);
/*     */     }
/* 642 */     if (!NoiseChunk.this.interpolating) {
/* 643 */       throw new IllegalStateException("Trying to sample interpolator outside the interpolation loop");
/*     */     }
/* 645 */     if (NoiseChunk.this.fillingCell) {
/* 646 */       return Mth.lerp3(NoiseChunk.this.inCellX / NoiseChunk.this.cellWidth, NoiseChunk.this.inCellY / NoiseChunk.this.cellHeight, NoiseChunk.this.inCellZ / NoiseChunk.this.cellWidth, this.noise000, this.noise100, this.noise010, this.noise110, this.noise001, this.noise101, this.noise011, this.noise111);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 654 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 659 */     if (NoiseChunk.this.fillingCell) {
/*     */       
/* 661 */       contextProvider.fillAllDirectly(output, this);
/*     */       return;
/*     */     } 
/* 664 */     wrapped().fillArray(output, contextProvider);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 669 */   public DensityFunction wrapped() { return this.noiseFiller; }
/*     */ 
/*     */ 
/*     */   
/*     */   private void swapSlices() {
/* 674 */     double[][] tmp = this.slice0;
/* 675 */     this.slice0 = this.slice1;
/* 676 */     this.slice1 = tmp;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 681 */   public DensityFunctions.Marker.Type type() { return DensityFunctions.Marker.Type.Interpolated; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\NoiseChunk$NoiseInterpolator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */