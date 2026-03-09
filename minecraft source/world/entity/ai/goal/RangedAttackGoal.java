/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*    */ 
/*    */ public class RangedAttackGoal
/*    */   extends Goal
/*    */ {
/*    */   private final Mob mob;
/*    */   private final RangedAttackMob rangedAttackMob;
/*    */   private LivingEntity target;
/* 15 */   private int attackTime = -1;
/*    */   
/*    */   private final double speedModifier;
/*    */   private int seeTime;
/*    */   private final int attackIntervalMin;
/*    */   private final int attackIntervalMax;
/*    */   private final float attackRadius;
/*    */   private final float attackRadiusSqr;
/*    */   
/* 24 */   public RangedAttackGoal(RangedAttackMob mob, double speedModifier, int attackInterval, float attackRadius) { this(mob, speedModifier, attackInterval, attackInterval, attackRadius); }
/*    */ 
/*    */   
/*    */   public RangedAttackGoal(RangedAttackMob mob, double speedModifier, int attackIntervalMin, int attackIntervalMax, float attackRadius) {
/* 28 */     if (!(mob instanceof LivingEntity)) {
/* 29 */       throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
/*    */     }
/* 31 */     this.rangedAttackMob = mob;
/* 32 */     this.mob = (Mob)mob;
/* 33 */     this.speedModifier = speedModifier;
/* 34 */     this.attackIntervalMin = attackIntervalMin;
/* 35 */     this.attackIntervalMax = attackIntervalMax;
/* 36 */     this.attackRadius = attackRadius;
/* 37 */     this.attackRadiusSqr = attackRadius * attackRadius;
/* 38 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 43 */     LivingEntity bestTarget = this.mob.getTarget();
/* 44 */     if (bestTarget == null || !bestTarget.isAlive()) {
/* 45 */       return false;
/*    */     }
/* 47 */     this.target = bestTarget;
/* 48 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public boolean canContinueToUse() { return (canUse() || (this.target.isAlive() && !this.mob.getNavigation().isDone())); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void stop() {
/* 58 */     this.target = null;
/* 59 */     this.seeTime = 0;
/* 60 */     this.attackTime = -1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public boolean requiresUpdateEveryTick() { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 70 */     double targetDistSqr = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
/* 71 */     boolean hasLineOfSight = this.mob.getSensing().hasLineOfSight(this.target);
/*    */     
/* 73 */     if (hasLineOfSight) {
/* 74 */       this.seeTime++;
/*    */     } else {
/* 76 */       this.seeTime = 0;
/*    */     } 
/*    */     
/* 79 */     if (targetDistSqr > this.attackRadiusSqr || this.seeTime < 5) {
/* 80 */       this.mob.getNavigation().moveTo(this.target, this.speedModifier);
/*    */     } else {
/* 82 */       this.mob.getNavigation().stop();
/*    */     } 
/*    */     
/* 85 */     this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
/*    */     
/* 87 */     if (--this.attackTime == 0) {
/* 88 */       if (!hasLineOfSight) {
/*    */         return;
/*    */       }
/*    */       
/* 92 */       float dist = (float)Math.sqrt(targetDistSqr) / this.attackRadius;
/* 93 */       float power = Mth.clamp(dist, 0.1F, 1.0F);
/*    */       
/* 95 */       this.rangedAttackMob.performRangedAttack(this.target, power);
/* 96 */       this.attackTime = Mth.floor(dist * (this.attackIntervalMax - this.attackIntervalMin) + this.attackIntervalMin);
/* 97 */     } else if (this.attackTime < 0) {
/* 98 */       this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(targetDistSqr) / this.attackRadius, this.attackIntervalMin, this.attackIntervalMax));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\RangedAttackGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */