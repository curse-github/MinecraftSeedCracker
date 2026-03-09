/*    */ package net.minecraft.world.entity.ai.navigation;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ import net.minecraft.world.level.pathfinder.PathFinder;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class FlyingPathNavigation
/*    */   extends PathNavigation {
/* 15 */   public FlyingPathNavigation(Mob mob, Level level) { super(mob, level); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected PathFinder createPathFinder(int maxVisitedNodes) {
/* 20 */     this.nodeEvaluator = new FlyNodeEvaluator();
/* 21 */     return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected boolean canMoveDirectly(Vec3 startPos, Vec3 stopPos) { return isClearForMovementBetween(this.mob, startPos, stopPos, true); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   protected boolean canUpdatePath() { return ((canFloat() && this.mob.isInLiquid()) || !this.mob.isPassenger()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   protected Vec3 getTempMobPos() { return this.mob.position(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public Path createPath(Entity target, int reachRange) { return createPath(target.blockPosition(), reachRange); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 46 */     this.tick++;
/*    */     
/* 48 */     if (this.hasDelayedRecomputation) {
/* 49 */       recomputePath();
/*    */     }
/*    */     
/* 52 */     if (isDone()) {
/*    */       return;
/*    */     }
/*    */     
/* 56 */     if (canUpdatePath()) {
/* 57 */       followThePath();
/* 58 */     } else if (this.path != null && !this.path.isDone()) {
/* 59 */       Vec3 pos = this.path.getNextEntityPos(this.mob);
/* 60 */       if (this.mob.getBlockX() == Mth.floor(pos.x) && this.mob.getBlockY() == Mth.floor(pos.y) && this.mob.getBlockZ() == Mth.floor(pos.z)) {
/* 61 */         this.path.advance();
/*    */       }
/*    */     } 
/*    */     
/* 65 */     if (isDone()) {
/*    */       return;
/*    */     }
/* 68 */     Vec3 target = this.path.getNextEntityPos(this.mob);
/*    */     
/* 70 */     this.mob.getMoveControl().setWantedPosition(target.x, target.y, target.z, this.speedModifier);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 75 */   public boolean isStableDestination(BlockPos pos) { return this.level.getBlockState(pos).entityCanStandOn(this.level, pos, this.mob); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 80 */   public boolean canNavigateGround() { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\navigation\FlyingPathNavigation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */