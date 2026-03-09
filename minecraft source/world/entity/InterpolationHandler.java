/*     */ package net.minecraft.world.entity;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class InterpolationHandler {
/*     */   public static final int DEFAULT_INTERPOLATION_STEPS = 3;
/*     */   private final Entity entity;
/*     */   private int interpolationSteps;
/*     */   private final InterpolationData interpolationData;
/*     */   private Vec3 previousTickPosition;
/*     */   private Vec2 previousTickRot;
/*     */   private final Consumer<InterpolationHandler> onInterpolationStart;
/*     */   
/*     */   private static class InterpolationData {
/*     */     protected int steps;
/*     */     Vec3 position;
/*     */     
/*     */     private InterpolationData(int steps, Vec3 position, float yRot, float xRot) {
/*  22 */       this.steps = steps;
/*  23 */       this.position = position;
/*  24 */       this.yRot = yRot;
/*  25 */       this.xRot = xRot;
/*     */     }
/*     */     float yRot; float xRot;
/*     */     
/*  29 */     public void decrease() { this.steps--; }
/*     */ 
/*     */ 
/*     */     
/*  33 */     public void addDelta(Vec3 delta) { this.position = this.position.add(delta); }
/*     */ 
/*     */     
/*     */     public void addRotation(float yRot, float xRot) {
/*  37 */       this.yRot += yRot;
/*  38 */       this.xRot += xRot;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   public InterpolationHandler(Entity entity) { this(entity, 3, null); }
/*     */ 
/*     */ 
/*     */   
/*  54 */   public InterpolationHandler(Entity entity, int interpolationSteps) { this(entity, interpolationSteps, null); }
/*     */ 
/*     */ 
/*     */   
/*  58 */   public InterpolationHandler(Entity entity, Consumer<InterpolationHandler> onInterpolationStart) { this(entity, 3, onInterpolationStart); }
/*     */   
/*     */   public InterpolationHandler(Entity entity, int interpolationSteps, Consumer<InterpolationHandler> onInterpolationStart) {
/*     */     this.interpolationData = new InterpolationData(0, Vec3.ZERO, 0.0F, 0.0F);
/*  62 */     this.interpolationSteps = interpolationSteps;
/*  63 */     this.entity = entity;
/*  64 */     this.onInterpolationStart = onInterpolationStart;
/*     */   }
/*     */ 
/*     */   
/*  68 */   public Vec3 position() { return (this.interpolationData.steps > 0) ? this.interpolationData.position : this.entity.position(); }
/*     */ 
/*     */ 
/*     */   
/*  72 */   public float yRot() { return (this.interpolationData.steps > 0) ? this.interpolationData.yRot : this.entity.getYRot(); }
/*     */ 
/*     */ 
/*     */   
/*  76 */   public float xRot() { return (this.interpolationData.steps > 0) ? this.interpolationData.xRot : this.entity.getXRot(); }
/*     */ 
/*     */   
/*     */   public void interpolateTo(Vec3 position, float yRot, float xRot) {
/*  80 */     if (this.interpolationSteps == 0) {
/*  81 */       this.entity.snapTo(position, yRot, xRot);
/*  82 */       cancel();
/*     */       
/*     */       return;
/*     */     } 
/*  86 */     if (hasActiveInterpolation() && 
/*  87 */       Objects.equals(Float.valueOf(yRot()), Float.valueOf(yRot)) && 
/*  88 */       Objects.equals(Float.valueOf(xRot()), Float.valueOf(xRot)) && 
/*  89 */       Objects.equals(position(), position)) {
/*     */       return;
/*     */     }
/*     */     
/*  93 */     this.interpolationData.steps = this.interpolationSteps;
/*  94 */     this.interpolationData.position = position;
/*  95 */     this.interpolationData.yRot = yRot;
/*  96 */     this.interpolationData.xRot = xRot;
/*     */     
/*  98 */     this.previousTickPosition = this.entity.position();
/*  99 */     this.previousTickRot = new Vec2(this.entity.getXRot(), this.entity.getYRot());
/*     */     
/* 101 */     if (this.onInterpolationStart != null) {
/* 102 */       this.onInterpolationStart.accept(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 107 */   public boolean hasActiveInterpolation() { return (this.interpolationData.steps > 0); }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public void setInterpolationLength(int steps) { this.interpolationSteps = steps; }
/*     */ 
/*     */   
/*     */   public void interpolate() {
/* 115 */     if (!hasActiveInterpolation()) {
/* 116 */       cancel();
/*     */       
/*     */       return;
/*     */     } 
/* 120 */     double alpha = 1.0D / this.interpolationData.steps;
/*     */     
/* 122 */     if (this.previousTickPosition != null) {
/*     */       
/* 124 */       Vec3 deltaSinceLastInterpolation = this.entity.position().subtract(this.previousTickPosition);
/* 125 */       if (this.entity.level().noCollision(this.entity, this.entity.makeBoundingBox(this.interpolationData.position.add(deltaSinceLastInterpolation))))
/*     */       {
/* 127 */         this.interpolationData.addDelta(deltaSinceLastInterpolation);
/*     */       }
/*     */     } 
/*     */     
/* 131 */     if (this.previousTickRot != null) {
/*     */       
/* 133 */       float deltaYRotSinceLastInterpolation = this.entity.getYRot() - this.previousTickRot.y;
/* 134 */       float deltaXRotSinceLastInterpolation = this.entity.getXRot() - this.previousTickRot.x;
/* 135 */       this.interpolationData.addRotation(deltaYRotSinceLastInterpolation, deltaXRotSinceLastInterpolation);
/*     */     } 
/*     */     
/* 138 */     double x = Mth.lerp(alpha, this.entity.getX(), this.interpolationData.position.x);
/* 139 */     double y = Mth.lerp(alpha, this.entity.getY(), this.interpolationData.position.y);
/* 140 */     double z = Mth.lerp(alpha, this.entity.getZ(), this.interpolationData.position.z);
/* 141 */     Vec3 newPosition = new Vec3(x, y, z);
/*     */     
/* 143 */     float newYRot = (float)Mth.rotLerp(alpha, this.entity.getYRot(), this.interpolationData.yRot);
/* 144 */     float newXRot = (float)Mth.lerp(alpha, this.entity.getXRot(), this.interpolationData.xRot);
/*     */     
/* 146 */     this.entity.setPos(newPosition);
/* 147 */     this.entity.setRot(newYRot, newXRot);
/* 148 */     this.interpolationData.decrease();
/* 149 */     this.previousTickPosition = newPosition;
/* 150 */     this.previousTickRot = new Vec2(this.entity.getXRot(), this.entity.getYRot());
/*     */   }
/*     */   
/*     */   public void cancel() {
/* 154 */     this.interpolationData.steps = 0;
/* 155 */     this.previousTickPosition = null;
/* 156 */     this.previousTickRot = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\InterpolationHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */