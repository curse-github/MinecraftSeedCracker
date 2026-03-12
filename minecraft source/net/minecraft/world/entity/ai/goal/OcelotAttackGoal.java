/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class OcelotAttackGoal
/*    */   extends Goal
/*    */ {
/*    */   private final Mob mob;
/*    */   private LivingEntity target;
/*    */   private int attackTime;
/*    */   
/*    */   public OcelotAttackGoal(Mob mob) {
/* 15 */     this.mob = mob;
/* 16 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 21 */     LivingEntity bestTarget = this.mob.getTarget();
/* 22 */     if (bestTarget == null) {
/* 23 */       return false;
/*    */     }
/* 25 */     this.target = bestTarget;
/* 26 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 31 */     if (!this.target.isAlive()) {
/* 32 */       return false;
/*    */     }
/* 34 */     if (this.mob.distanceToSqr(this.target) > 225.0D) {
/* 35 */       return false;
/*    */     }
/* 37 */     return (!this.mob.getNavigation().isDone() || canUse());
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 42 */     this.target = null;
/* 43 */     this.mob.getNavigation().stop();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public boolean requiresUpdateEveryTick() { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 53 */     this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
/*    */     
/* 55 */     double meleeRadiusSqr = (this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F);
/* 56 */     double distSqr = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
/*    */     
/* 58 */     double speedModifier = 0.8D;
/* 59 */     if (distSqr > meleeRadiusSqr && distSqr < 16.0D) {
/* 60 */       speedModifier = 1.33D;
/* 61 */     } else if (distSqr < 225.0D) {
/* 62 */       speedModifier = 0.6D;
/*    */     } 
/*    */     
/* 65 */     this.mob.getNavigation().moveTo(this.target, speedModifier);
/*    */     
/* 67 */     this.attackTime = Math.max(this.attackTime - 1, 0);
/*    */     
/* 69 */     if (distSqr > meleeRadiusSqr) {
/*    */       return;
/*    */     }
/* 72 */     if (this.attackTime > 0) {
/*    */       return;
/*    */     }
/* 75 */     this.attackTime = 20;
/* 76 */     this.mob.doHurtTarget(getServerLevel(this.mob), this.target);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\OcelotAttackGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */