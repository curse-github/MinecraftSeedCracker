/*     */ package net.minecraft.world.entity.raid;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.monster.illager.AbstractIllager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HoldGroundAttackGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Raider mob;
/*     */   private final float hostileRadiusSqr;
/*     */   public final TargetingConditions shoutTargeting;
/*     */   
/*     */   public HoldGroundAttackGoal(AbstractIllager mob, float hostileRadius) {
/* 429 */     this.shoutTargeting = TargetingConditions.forNonCombat().range(8.0D).ignoreLineOfSight().ignoreInvisibilityTesting();
/*     */ 
/*     */     
/* 432 */     this.mob = mob;
/* 433 */     this.hostileRadiusSqr = hostileRadius * hostileRadius;
/* 434 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 440 */     LivingEntity lastHurtByMob = this.mob.getLastHurtByMob();
/* 441 */     return (this.mob.getCurrentRaid() == null && Raider.access$200(this.mob) && this.mob.getTarget() != null && !this.mob.isAggressive() && (lastHurtByMob == null || lastHurtByMob.getType() != EntityType.PLAYER));
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 446 */     super.start();
/* 447 */     this.mob.getNavigation().stop();
/*     */     
/* 449 */     List<Raider> nearbyEntities = getServerLevel(this.mob).getNearbyEntities(Raider.class, this.shoutTargeting, this.mob, this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
/* 450 */     for (Raider entity : nearbyEntities) {
/* 451 */       entity.setTarget(this.mob.getTarget());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 457 */     super.stop();
/*     */     
/* 459 */     LivingEntity target = this.mob.getTarget();
/* 460 */     if (target != null) {
/* 461 */       List<Raider> nearbyEntities = getServerLevel(this.mob).getNearbyEntities(Raider.class, this.shoutTargeting, this.mob, this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
/* 462 */       for (Raider entity : nearbyEntities) {
/* 463 */         entity.setTarget(target);
/* 464 */         entity.setAggressive(true);
/*     */       } 
/* 466 */       this.mob.setAggressive(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 472 */   public boolean requiresUpdateEveryTick() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 477 */     LivingEntity target = this.mob.getTarget();
/* 478 */     if (target == null) {
/*     */       return;
/*     */     }
/*     */     
/* 482 */     if (this.mob.distanceToSqr(target) > this.hostileRadiusSqr) {
/* 483 */       this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
/*     */       
/* 485 */       if (Raider.access$300(this.mob).nextInt(50) == 0) {
/* 486 */         this.mob.playAmbientSound();
/*     */       }
/*     */     } else {
/* 489 */       this.mob.setAggressive(true);
/*     */     } 
/*     */     
/* 492 */     super.tick();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\raid\Raider$HoldGroundAttackGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */