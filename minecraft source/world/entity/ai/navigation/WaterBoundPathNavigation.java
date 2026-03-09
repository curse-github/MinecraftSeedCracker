/*    */ package net.minecraft.world.entity.ai.navigation;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.pathfinder.PathFinder;
/*    */ import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WaterBoundPathNavigation
/*    */   extends PathNavigation {
/*    */   private boolean allowBreaching;
/*    */   
/* 15 */   public WaterBoundPathNavigation(Mob mob, Level level) { super(mob, level); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected PathFinder createPathFinder(int maxVisitedNodes) {
/* 20 */     this.allowBreaching = (this.mob.getType() == EntityType.DOLPHIN);
/* 21 */     this.nodeEvaluator = new SwimNodeEvaluator(this.allowBreaching);
/*    */     
/* 23 */     this.nodeEvaluator.setCanPassDoors(false);
/* 24 */     return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 29 */   protected boolean canUpdatePath() { return (this.allowBreaching || this.mob.isInLiquid()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   protected Vec3 getTempMobPos() { return new Vec3(this.mob.getX(), this.mob.getY(0.5D), this.mob.getZ()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   protected double getGroundY(Vec3 target) { return target.y; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   protected boolean canMoveDirectly(Vec3 startPos, Vec3 stopPos) { return isClearForMovementBetween(this.mob, startPos, stopPos, false); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public boolean isStableDestination(BlockPos pos) { return !this.level.getBlockState(pos).isSolidRender(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCanFloat(boolean canFloat) {}
/*    */ 
/*    */ 
/*    */   
/* 58 */   public boolean canNavigateGround() { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\navigation\WaterBoundPathNavigation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */