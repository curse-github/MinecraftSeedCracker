/*     */ package net.minecraft.world.entity.animal.dolphin;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.StructureTags;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DolphinSwimToTreasureGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Dolphin dolphin;
/*     */   private boolean stuck;
/*     */   
/*     */   DolphinSwimToTreasureGoal(Dolphin dolphin) {
/* 503 */     this.dolphin = dolphin;
/* 504 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 509 */   public boolean isInterruptable() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 514 */   public boolean canUse() { return (this.dolphin.gotFish() && this.dolphin.getAirSupply() >= 100); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/* 519 */     BlockPos treasurePos = this.dolphin.treasurePos;
/* 520 */     if (treasurePos == null) {
/* 521 */       return false;
/*     */     }
/* 523 */     return (!BlockPos.containing(treasurePos.getX(), this.dolphin.getY(), treasurePos.getZ()).closerToCenterThan(this.dolphin.position(), 4.0D) && !this.stuck && this.dolphin.getAirSupply() >= 100);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 528 */     if (!(this.dolphin.level() instanceof ServerLevel)) {
/*     */       return;
/*     */     }
/* 531 */     ServerLevel level = (ServerLevel)this.dolphin.level();
/* 532 */     this.stuck = false;
/* 533 */     this.dolphin.getNavigation().stop();
/*     */     
/* 535 */     BlockPos dolphinPos = this.dolphin.blockPosition();
/*     */     
/* 537 */     BlockPos treasurePos = level.findNearestMapStructure(StructureTags.DOLPHIN_LOCATED, dolphinPos, 50, false);
/* 538 */     if (treasurePos != null) {
/* 539 */       this.dolphin.treasurePos = treasurePos;
/*     */     } else {
/*     */       
/* 542 */       this.stuck = true;
/*     */       
/*     */       return;
/*     */     } 
/* 546 */     level.broadcastEntityEvent(this.dolphin, (byte)38);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 551 */     BlockPos treasurePos = this.dolphin.treasurePos;
/* 552 */     if (treasurePos == null || BlockPos.containing(treasurePos.getX(), this.dolphin.getY(), treasurePos.getZ()).closerToCenterThan(this.dolphin.position(), 4.0D) || this.stuck) {
/* 553 */       this.dolphin.setGotFish(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 559 */     if (this.dolphin.treasurePos == null) {
/*     */       return;
/*     */     }
/*     */     
/* 563 */     Level level = this.dolphin.level();
/*     */     
/* 565 */     if (this.dolphin.closeToNextPos() || this.dolphin.getNavigation().isDone()) {
/* 566 */       Vec3 treasurePos = Vec3.atCenterOf(this.dolphin.treasurePos);
/* 567 */       Vec3 nextPos = DefaultRandomPos.getPosTowards(this.dolphin, 16, 1, treasurePos, 0.39269909262657166D);
/* 568 */       if (nextPos == null) {
/* 569 */         nextPos = DefaultRandomPos.getPosTowards(this.dolphin, 8, 4, treasurePos, 1.5707963705062866D);
/*     */       }
/*     */       
/* 572 */       if (nextPos != null) {
/* 573 */         BlockPos next = BlockPos.containing(nextPos);
/* 574 */         if (!level.getFluidState(next).is(FluidTags.WATER) || !level.getBlockState(next).isPathfindable(PathComputationType.WATER)) {
/* 575 */           nextPos = DefaultRandomPos.getPosTowards(this.dolphin, 8, 5, treasurePos, 1.5707963705062866D);
/*     */         }
/*     */       } 
/*     */       
/* 579 */       if (nextPos == null) {
/* 580 */         this.stuck = true;
/*     */         
/*     */         return;
/*     */       } 
/* 584 */       this.dolphin.getLookControl().setLookAt(nextPos.x, nextPos.y, nextPos.z, (this.dolphin.getMaxHeadYRot() + 20), this.dolphin.getMaxHeadXRot());
/* 585 */       this.dolphin.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, 1.3D);
/*     */       
/* 587 */       if (level.random.nextInt(adjustedTickDelay(80)) == 0)
/* 588 */         level.broadcastEntityEvent(this.dolphin, (byte)38); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\dolphin\Dolphin$DolphinSwimToTreasureGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */