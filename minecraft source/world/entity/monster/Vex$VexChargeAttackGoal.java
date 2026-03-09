/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class VexChargeAttackGoal
/*     */   extends Goal
/*     */ {
/* 265 */   public VexChargeAttackGoal() { setFlags(EnumSet.of(Goal.Flag.MOVE)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 270 */     LivingEntity target = Vex.this.getTarget();
/* 271 */     if (target != null && target.isAlive() && !Vex.this.getMoveControl().hasWanted() && Vex.access$000(Vex.this).nextInt(reducedTickDelay(7)) == 0) {
/* 272 */       return (Vex.this.distanceToSqr(target) > 4.0D);
/*     */     }
/* 274 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 279 */   public boolean canContinueToUse() { return (Vex.this.getMoveControl().hasWanted() && Vex.this.isCharging() && Vex.this.getTarget() != null && Vex.this.getTarget().isAlive()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 284 */     LivingEntity attackTarget = Vex.this.getTarget();
/* 285 */     if (attackTarget != null) {
/* 286 */       Vec3 eyePosition = attackTarget.getEyePosition();
/* 287 */       Vex.access$100(Vex.this).setWantedPosition(eyePosition.x, eyePosition.y, eyePosition.z, 1.0D);
/*     */     } 
/* 289 */     Vex.this.setIsCharging(true);
/* 290 */     Vex.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 295 */   public void stop() { Vex.this.setIsCharging(false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 300 */   public boolean requiresUpdateEveryTick() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 305 */     LivingEntity attackTarget = Vex.this.getTarget();
/* 306 */     if (attackTarget == null) {
/*     */       return;
/*     */     }
/* 309 */     if (Vex.this.getBoundingBox().intersects(attackTarget.getBoundingBox())) {
/* 310 */       Vex.this.doHurtTarget(getServerLevel(Vex.this.level()), attackTarget);
/* 311 */       Vex.this.setIsCharging(false);
/*     */     } else {
/* 313 */       double distance = Vex.this.distanceToSqr(attackTarget);
/* 314 */       if (distance < 9.0D) {
/* 315 */         Vec3 eyePosition = attackTarget.getEyePosition();
/* 316 */         Vex.access$200(Vex.this).setWantedPosition(eyePosition.x, eyePosition.y, eyePosition.z, 1.0D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Vex$VexChargeAttackGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */