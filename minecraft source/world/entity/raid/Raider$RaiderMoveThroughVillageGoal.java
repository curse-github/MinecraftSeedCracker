/*     */ package net.minecraft.world.entity.raid;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
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
/*     */ class RaiderMoveThroughVillageGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Raider raider;
/*     */   private final double speedModifier;
/*     */   private BlockPos poiPos;
/*     */   private final List<BlockPos> visited;
/*     */   private final int distanceToPoi;
/*     */   private boolean stuck;
/*     */   
/*     */   public RaiderMoveThroughVillageGoal(Raider mob, double speedModifier, int distanceToPoi) {
/* 500 */     this.visited = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 505 */     this.raider = mob;
/* 506 */     this.speedModifier = speedModifier;
/* 507 */     this.distanceToPoi = distanceToPoi;
/* 508 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 513 */     updateVisited();
/* 514 */     return (isValidRaid() && hasSuitablePoi() && this.raider.getTarget() == null);
/*     */   }
/*     */ 
/*     */   
/* 518 */   private boolean isValidRaid() { return (this.raider.hasActiveRaid() && !this.raider.getCurrentRaid().isOver()); }
/*     */ 
/*     */   
/*     */   private boolean hasSuitablePoi() {
/* 522 */     ServerLevel level = (ServerLevel)this.raider.level();
/* 523 */     BlockPos pos = this.raider.blockPosition();
/* 524 */     Optional<BlockPos> homePos = level.getPoiManager().getRandom(p -> p.is(PoiTypes.HOME), this::hasNotVisited, PoiManager.Occupancy.ANY, pos, 48, Raider.access$400(this.raider));
/* 525 */     if (homePos.isEmpty()) {
/* 526 */       return false;
/*     */     }
/*     */     
/* 529 */     this.poiPos = ((BlockPos)homePos.get()).immutable();
/*     */     
/* 531 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/* 536 */     if (this.raider.getNavigation().isDone()) {
/* 537 */       return false;
/*     */     }
/* 539 */     return (this.raider.getTarget() == null && !this.poiPos.closerToCenterThan(this.raider.position(), (this.raider.getBbWidth() + this.distanceToPoi)) && !this.stuck);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 544 */     if (this.poiPos.closerToCenterThan(this.raider.position(), this.distanceToPoi)) {
/* 545 */       this.visited.add(this.poiPos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 551 */     super.start();
/* 552 */     this.raider.setNoActionTime(0);
/* 553 */     this.raider.getNavigation().moveTo(this.poiPos.getX(), this.poiPos.getY(), this.poiPos.getZ(), this.speedModifier);
/* 554 */     this.stuck = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 559 */     if (this.raider.getNavigation().isDone()) {
/* 560 */       Vec3 poiVec = Vec3.atBottomCenterOf(this.poiPos);
/* 561 */       Vec3 nextPos = DefaultRandomPos.getPosTowards(this.raider, 16, 7, poiVec, 0.3141592741012573D);
/* 562 */       if (nextPos == null) {
/* 563 */         nextPos = DefaultRandomPos.getPosTowards(this.raider, 8, 7, poiVec, 1.5707963705062866D);
/*     */       }
/*     */       
/* 566 */       if (nextPos == null) {
/* 567 */         this.stuck = true;
/*     */         
/*     */         return;
/*     */       } 
/* 571 */       this.raider.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean hasNotVisited(BlockPos poi) {
/* 576 */     for (BlockPos visitedPoi : this.visited) {
/* 577 */       if (Objects.equals(poi, visitedPoi)) {
/* 578 */         return false;
/*     */       }
/*     */     } 
/* 581 */     return true;
/*     */   }
/*     */   
/*     */   private void updateVisited() {
/* 585 */     if (this.visited.size() > 2)
/* 586 */       this.visited.remove(0); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\raid\Raider$RaiderMoveThroughVillageGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */