/*    */ package net.minecraft.world.entity.monster.skeleton;
/*    */ 
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends MeleeAttackGoal
/*    */ {
/* 62 */   null(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) { super(mob, speedModifier, followingTargetEvenIfNotSeen); }
/*    */   
/*    */   public void stop() {
/* 65 */     super.stop();
/* 66 */     AbstractSkeleton.this.setAggressive(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 71 */     super.start();
/* 72 */     AbstractSkeleton.this.setAggressive(true);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\skeleton\AbstractSkeleton$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */