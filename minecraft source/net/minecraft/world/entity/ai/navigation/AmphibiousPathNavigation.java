/*    */ package net.minecraft.world.entity.ai.navigation;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
/*    */ import net.minecraft.world.level.pathfinder.PathFinder;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class AmphibiousPathNavigation
/*    */   extends PathNavigation {
/* 12 */   public AmphibiousPathNavigation(Mob mob, Level level) { super(mob, level); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected PathFinder createPathFinder(int maxVisitedNodes) {
/* 17 */     this.nodeEvaluator = new AmphibiousNodeEvaluator(false);
/* 18 */     return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 23 */   protected boolean canUpdatePath() { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   protected Vec3 getTempMobPos() { return new Vec3(this.mob.getX(), this.mob.getY(0.5D), this.mob.getZ()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   protected double getGroundY(Vec3 target) { return target.y; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean canMoveDirectly(Vec3 startPos, Vec3 stopPos) {
/* 38 */     if (this.mob.isInLiquid()) {
/* 39 */       return isClearForMovementBetween(this.mob, startPos, stopPos, false);
/*    */     }
/* 41 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public boolean isStableDestination(BlockPos pos) { return !this.level.getBlockState(pos.below()).isAir(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCanFloat(boolean canFloat) {}
/*    */ 
/*    */ 
/*    */   
/* 55 */   public boolean canNavigateGround() { return true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\navigation\AmphibiousPathNavigation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */