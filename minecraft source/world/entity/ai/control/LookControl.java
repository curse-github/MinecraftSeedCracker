/*     */ package net.minecraft.world.entity.ai.control;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LookControl
/*     */   implements Control
/*     */ {
/*     */   protected final Mob mob;
/*     */   protected float yMaxRotSpeed;
/*     */   protected float xMaxRotAngle;
/*     */   protected int lookAtCooldown;
/*     */   protected double wantedX;
/*     */   protected double wantedY;
/*     */   protected double wantedZ;
/*     */   
/*  20 */   public LookControl(Mob mob) { this.mob = mob; }
/*     */ 
/*     */ 
/*     */   
/*  24 */   public void setLookAt(Vec3 vec) { setLookAt(vec.x, vec.y, vec.z); }
/*     */ 
/*     */ 
/*     */   
/*  28 */   public void setLookAt(Entity target) { setLookAt(target.getX(), target.getEyeY(), target.getZ()); }
/*     */ 
/*     */ 
/*     */   
/*  32 */   public void setLookAt(Entity target, float yMaxRotSpeed, float xMaxRotAngle) { setLookAt(target.getX(), target.getEyeY(), target.getZ(), yMaxRotSpeed, xMaxRotAngle); }
/*     */ 
/*     */ 
/*     */   
/*  36 */   public void setLookAt(double x, double y, double z) { setLookAt(x, y, z, this.mob.getHeadRotSpeed(), this.mob.getMaxHeadXRot()); }
/*     */ 
/*     */   
/*     */   public void setLookAt(double x, double y, double z, float yMaxRotSpeed, float xMaxRotAngle) {
/*  40 */     this.wantedX = x;
/*  41 */     this.wantedY = y;
/*  42 */     this.wantedZ = z;
/*  43 */     this.yMaxRotSpeed = yMaxRotSpeed;
/*  44 */     this.xMaxRotAngle = xMaxRotAngle;
/*  45 */     this.lookAtCooldown = 2;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  49 */     if (resetXRotOnTick()) {
/*  50 */       this.mob.setXRot(0.0F);
/*     */     }
/*     */     
/*  53 */     if (this.lookAtCooldown > 0) {
/*  54 */       this.lookAtCooldown--;
/*  55 */       getYRotD().ifPresent(yRotD -> this.mob.yHeadRot = rotateTowards(this.mob.yHeadRot, yRotD.floatValue(), this.yMaxRotSpeed));
/*  56 */       getXRotD().ifPresent(xRotD -> this.mob.setXRot(rotateTowards(this.mob.getXRot(), xRotD.floatValue(), this.xMaxRotAngle)));
/*     */     } else {
/*  58 */       this.mob.yHeadRot = rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, 10.0F);
/*     */     } 
/*     */     
/*  61 */     clampHeadRotationToBody();
/*     */   }
/*     */   
/*     */   protected void clampHeadRotationToBody() {
/*  65 */     if (!this.mob.getNavigation().isDone())
/*     */     {
/*  67 */       this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, this.mob.getMaxHeadYRot());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  73 */   protected boolean resetXRotOnTick() { return true; }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public boolean isLookingAtTarget() { return (this.lookAtCooldown > 0); }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public double getWantedX() { return this.wantedX; }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public double getWantedY() { return this.wantedY; }
/*     */ 
/*     */ 
/*     */   
/*  89 */   public double getWantedZ() { return this.wantedZ; }
/*     */ 
/*     */   
/*     */   protected Optional<Float> getXRotD() {
/*  93 */     double xd = this.wantedX - this.mob.getX();
/*  94 */     double yd = this.wantedY - this.mob.getEyeY();
/*  95 */     double zd = this.wantedZ - this.mob.getZ();
/*  96 */     double sd = Math.sqrt(xd * xd + zd * zd);
/*  97 */     return (Math.abs(yd) > 9.999999747378752E-6D || Math.abs(sd) > 9.999999747378752E-6D) ? Optional.of(Float.valueOf((float)-(Mth.atan2(yd, sd) * 57.2957763671875D))) : Optional.empty();
/*     */   }
/*     */   
/*     */   protected Optional<Float> getYRotD() {
/* 101 */     double xd = this.wantedX - this.mob.getX();
/* 102 */     double zd = this.wantedZ - this.mob.getZ();
/* 103 */     return (Math.abs(zd) > 9.999999747378752E-6D || Math.abs(xd) > 9.999999747378752E-6D) ? Optional.of(Float.valueOf((float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F)) : Optional.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\control\LookControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */