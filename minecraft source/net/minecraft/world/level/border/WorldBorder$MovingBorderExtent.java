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
/*     */ class MovingBorderExtent
/*     */   implements WorldBorder.BorderExtent
/*     */ {
/*     */   private final double from;
/*     */   private final double to;
/*     */   private final long lerpEnd;
/*     */   private final long lerpBegin;
/*     */   private final double lerpDuration;
/*     */   private long lerpProgress;
/*     */   private double size;
/*     */   private double previousSize;
/*     */   
/*     */   private MovingBorderExtent(double from, double to, long duration, long gameTime) {
/*  70 */     this.from = from;
/*  71 */     this.to = to;
/*     */     
/*  73 */     this.lerpDuration = duration;
/*  74 */     this.lerpProgress = duration;
/*  75 */     this.lerpBegin = gameTime;
/*  76 */     this.lerpEnd = this.lerpBegin + duration;
/*  77 */     double size = calculateSize();
/*  78 */     this.size = size;
/*  79 */     this.previousSize = size;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public double getMinX(float deltaPartialTick) { return Mth.clamp(WorldBorder.this.getCenterX() - Mth.lerp(deltaPartialTick, getPreviousSize(), getSize()) / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   public double getMinZ(float deltaPartialTick) { return Mth.clamp(WorldBorder.this.getCenterZ() - Mth.lerp(deltaPartialTick, getPreviousSize(), getSize()) / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   public double getMaxX(float deltaPartialTick) { return Mth.clamp(WorldBorder.this.getCenterX() + Mth.lerp(deltaPartialTick, getPreviousSize(), getSize()) / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public double getMaxZ(float deltaPartialTick) { return Mth.clamp(WorldBorder.this.getCenterZ() + Mth.lerp(deltaPartialTick, getPreviousSize(), getSize()) / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public double getSize() { return this.size; }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public double getPreviousSize() { return this.previousSize; }
/*     */ 
/*     */   
/*     */   private double calculateSize() {
/* 112 */     double progress = (this.lerpDuration - this.lerpProgress) / this.lerpDuration;
/* 113 */     return (progress < 1.0D) ? Mth.lerp(progress, this.from, this.to) : this.to;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 118 */   public double getLerpSpeed() { return Math.abs(this.from - this.to) / (this.lerpEnd - this.lerpBegin); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public long getLerpTime() { return this.lerpProgress; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   public double getLerpTarget() { return this.to; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   public BorderStatus getStatus() { return (this.to < this.from) ? BorderStatus.SHRINKING : BorderStatus.GROWING; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onCenterChange() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onAbsoluteMaxSizeChange() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public WorldBorder.BorderExtent update() {
/* 146 */     this.lerpProgress--;
/* 147 */     this.previousSize = this.size;
/* 148 */     this.size = calculateSize();
/* 149 */     if (this.lerpProgress <= 0L) {
/* 150 */       WorldBorder.this.setDirty();
/* 151 */       return new WorldBorder.StaticBorderExtent(WorldBorder.this, this.to);
/*     */     } 
/*     */     
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 159 */   public VoxelShape getCollisionShape() { return Shapes.join(Shapes.INFINITY, Shapes.box(
/* 160 */           Math.floor(getMinX(0.0F)), Double.NEGATIVE_INFINITY, Math.floor(getMinZ(0.0F)), 
/* 161 */           Math.ceil(getMaxX(0.0F)), Double.POSITIVE_INFINITY, Math.ceil(getMaxZ(0.0F))), BooleanOp.ONLY_FIRST); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\border\WorldBorder$MovingBorderExtent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */