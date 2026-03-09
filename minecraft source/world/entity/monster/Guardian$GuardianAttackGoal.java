/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class GuardianAttackGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Guardian guardian;
/*     */   private int attackTime;
/*     */   private final boolean elder;
/*     */   
/*     */   public GuardianAttackGoal(Guardian guardian) {
/* 371 */     this.guardian = guardian;
/*     */ 
/*     */     
/* 374 */     this.elder = guardian instanceof ElderGuardian;
/*     */     
/* 376 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 381 */     LivingEntity target = this.guardian.getTarget();
/* 382 */     return (target != null && target.isAlive());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 387 */   public boolean canContinueToUse() { return (super.canContinueToUse() && (this.elder || (this.guardian.getTarget() != null && this.guardian.distanceToSqr(this.guardian.getTarget()) > 9.0D))); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 392 */     this.attackTime = -10;
/* 393 */     this.guardian.getNavigation().stop();
/* 394 */     LivingEntity target = this.guardian.getTarget();
/* 395 */     if (target != null) {
/* 396 */       this.guardian.getLookControl().setLookAt(target, 90.0F, 90.0F);
/*     */     }
/*     */ 
/*     */     
/* 400 */     this.guardian.needsSync = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 405 */     this.guardian.setActiveAttackTarget(0);
/* 406 */     this.guardian.setTarget(null);
/*     */     
/* 408 */     this.guardian.randomStrollGoal.trigger();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 413 */   public boolean requiresUpdateEveryTick() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 418 */     LivingEntity target = this.guardian.getTarget();
/* 419 */     if (target == null) {
/*     */       return;
/*     */     }
/*     */     
/* 423 */     this.guardian.getNavigation().stop();
/* 424 */     this.guardian.getLookControl().setLookAt(target, 90.0F, 90.0F);
/*     */     
/* 426 */     if (!this.guardian.hasLineOfSight(target)) {
/* 427 */       this.guardian.setTarget(null);
/*     */       
/*     */       return;
/*     */     } 
/* 431 */     this.attackTime++;
/* 432 */     if (this.attackTime == 0) {
/*     */       
/* 434 */       this.guardian.setActiveAttackTarget(target.getId());
/* 435 */       if (!this.guardian.isSilent()) {
/* 436 */         this.guardian.level().broadcastEntityEvent(this.guardian, (byte)21);
/*     */       }
/* 438 */     } else if (this.attackTime >= this.guardian.getAttackDuration()) {
/* 439 */       float magicDamage = 1.0F;
/* 440 */       if (this.guardian.level().getDifficulty() == Difficulty.HARD) {
/* 441 */         magicDamage += 2.0F;
/*     */       }
/* 443 */       if (this.elder) {
/* 444 */         magicDamage += 2.0F;
/*     */       }
/* 446 */       ServerLevel serverLevel = getServerLevel(this.guardian);
/* 447 */       target.hurtServer(serverLevel, this.guardian.damageSources().indirectMagic(this.guardian, this.guardian), magicDamage);
/* 448 */       this.guardian.doHurtTarget(serverLevel, target);
/* 449 */       this.guardian.setTarget(null);
/*     */     } 
/*     */     
/* 452 */     super.tick();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Guardian$GuardianAttackGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */