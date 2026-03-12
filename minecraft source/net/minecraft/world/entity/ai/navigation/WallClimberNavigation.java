/*    */ package net.minecraft.world.entity.ai.navigation;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WallClimberNavigation
/*    */   extends GroundPathNavigation
/*    */ {
/*    */   private BlockPos pathToPosition;
/*    */   
/* 22 */   public WallClimberNavigation(Mob mob, Level level) { super(mob, level); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Path createPath(BlockPos pos, int reachRange) {
/* 27 */     this.pathToPosition = pos;
/* 28 */     return super.createPath(pos, reachRange);
/*    */   }
/*    */ 
/*    */   
/*    */   public Path createPath(Entity target, int reachRange) {
/* 33 */     this.pathToPosition = target.blockPosition();
/* 34 */     return super.createPath(target, reachRange);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean moveTo(Entity target, double speedModifier) {
/* 39 */     Path newPath = createPath(target, 0);
/* 40 */     if (newPath != null) {
/* 41 */       return moveTo(newPath, speedModifier);
/*    */     }
/* 43 */     this.pathToPosition = target.blockPosition();
/* 44 */     this.speedModifier = speedModifier;
/* 45 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 51 */     if (isDone()) {
/* 52 */       if (this.pathToPosition != null)
/*    */       {
/* 54 */         if (this.pathToPosition.closerToCenterThan(this.mob.position(), this.mob.getBbWidth()) || (this.mob.getY() > this.pathToPosition.getY() && BlockPos.containing(this.pathToPosition.getX(), this.mob.getY(), this.pathToPosition.getZ()).closerToCenterThan(this.mob.position(), this.mob.getBbWidth()))) {
/* 55 */           this.pathToPosition = null;
/*    */         } else {
/* 57 */           this.mob.getMoveControl().setWantedPosition(this.pathToPosition.getX(), this.pathToPosition.getY(), this.pathToPosition.getZ(), this.speedModifier);
/*    */         } 
/*    */       }
/*    */       return;
/*    */     } 
/* 62 */     super.tick();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\navigation\WallClimberNavigation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */