/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class MoveTowardsTargetGoal
/*    */   extends Goal
/*    */ {
/*    */   private final PathfinderMob mob;
/*    */   private LivingEntity target;
/*    */   private double wantedX;
/*    */   private double wantedY;
/*    */   private double wantedZ;
/*    */   private final double speedModifier;
/*    */   private final float within;
/*    */   
/*    */   public MoveTowardsTargetGoal(PathfinderMob mob, double speedModifier, float within) {
/* 22 */     this.mob = mob;
/* 23 */     this.speedModifier = speedModifier;
/* 24 */     this.within = within;
/* 25 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 30 */     this.target = this.mob.getTarget();
/* 31 */     if (this.target == null) {
/* 32 */       return false;
/*    */     }
/* 34 */     if (this.target.distanceToSqr(this.mob) > (this.within * this.within)) {
/* 35 */       return false;
/*    */     }
/* 37 */     Vec3 pos = DefaultRandomPos.getPosTowards(this.mob, 16, 7, this.target.position(), 1.5707963705062866D);
/* 38 */     if (pos == null) {
/* 39 */       return false;
/*    */     }
/* 41 */     this.wantedX = pos.x;
/* 42 */     this.wantedY = pos.y;
/* 43 */     this.wantedZ = pos.z;
/* 44 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public boolean canContinueToUse() { return (!this.mob.getNavigation().isDone() && this.target.isAlive() && this.target.distanceToSqr(this.mob) < (this.within * this.within)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public void stop() { this.target = null; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public void start() { this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\MoveTowardsTargetGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */