/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class MoveTowardsRestrictionGoal
/*    */   extends Goal
/*    */ {
/*    */   private final PathfinderMob mob;
/*    */   private double wantedX;
/*    */   private double wantedY;
/*    */   private double wantedZ;
/*    */   private final double speedModifier;
/*    */   
/*    */   public MoveTowardsRestrictionGoal(PathfinderMob mob, double moveSpeedModifier) {
/* 18 */     this.mob = mob;
/* 19 */     this.speedModifier = moveSpeedModifier;
/* 20 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 25 */     if (this.mob.isWithinHome()) {
/* 26 */       return false;
/*    */     }
/* 28 */     Vec3 pos = DefaultRandomPos.getPosTowards(this.mob, 16, 7, Vec3.atBottomCenterOf(this.mob.getHomePosition()), 1.5707963705062866D);
/* 29 */     if (pos == null) {
/* 30 */       return false;
/*    */     }
/* 32 */     this.wantedX = pos.x;
/* 33 */     this.wantedY = pos.y;
/* 34 */     this.wantedZ = pos.z;
/* 35 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public boolean canContinueToUse() { return !this.mob.getNavigation().isDone(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public void start() { this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\MoveTowardsRestrictionGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */