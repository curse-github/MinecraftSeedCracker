/*     */ package net.minecraft.world.level.border;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class StaticBorderExtent
/*     */   implements WorldBorder.BorderExtent
/*     */ {
/*     */   private final double size;
/*     */   private double minX;
/*     */   private double minZ;
/*     */   private double maxX;
/*     */   private double maxZ;
/*     */   private VoxelShape shape;
/*     */   
/*     */   public StaticBorderExtent(double size) {
/* 177 */     this.size = size;
/* 178 */     updateBox();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 183 */   public double getMinX(float deltaPartialTick) { return this.minX; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 188 */   public double getMaxX(float deltaPartialTick) { return this.maxX; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 193 */   public double getMinZ(float deltaPartialTick) { return this.minZ; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 198 */   public double getMaxZ(float deltaPartialTick) { return this.maxZ; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   public double getSize() { return this.size; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 208 */   public BorderStatus getStatus() { return BorderStatus.STATIONARY; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 213 */   public double getLerpSpeed() { return 0.0D; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 218 */   public long getLerpTime() { return 0L; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 223 */   public double getLerpTarget() { return this.size; }
/*     */ 
/*     */   
/*     */   private void updateBox() {
/* 227 */     this.minX = Mth.clamp(WorldBorder.this.getCenterX() - this.size / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/* 228 */     this.minZ = Mth.clamp(WorldBorder.this.getCenterZ() - this.size / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/* 229 */     this.maxX = Mth.clamp(WorldBorder.this.getCenterX() + this.size / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/* 230 */     this.maxZ = Mth.clamp(WorldBorder.this.getCenterZ() + this.size / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/*     */     
/* 232 */     this.shape = Shapes.join(Shapes.INFINITY, Shapes.box(
/* 233 */           Math.floor(getMinX(0.0F)), Double.NEGATIVE_INFINITY, Math.floor(getMinZ(0.0F)), 
/* 234 */           Math.ceil(getMaxX(0.0F)), Double.POSITIVE_INFINITY, Math.ceil(getMaxZ(0.0F))), BooleanOp.ONLY_FIRST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 240 */   public void onAbsoluteMaxSizeChange() { updateBox(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 245 */   public void onCenterChange() { updateBox(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 250 */   public WorldBorder.BorderExtent update() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 255 */   public VoxelShape getCollisionShape() { return this.shape; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\border\WorldBorder$StaticBorderExtent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */