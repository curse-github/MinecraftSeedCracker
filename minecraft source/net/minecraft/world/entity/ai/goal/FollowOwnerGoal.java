/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.TamableAnimal;
/*    */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*    */ import net.minecraft.world.level.pathfinder.PathType;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FollowOwnerGoal
/*    */   extends Goal
/*    */ {
/*    */   private final TamableAnimal tamable;
/*    */   private LivingEntity owner;
/*    */   private final double speedModifier;
/*    */   private final PathNavigation navigation;
/*    */   private int timeToRecalcPath;
/*    */   private final float stopDistance;
/*    */   private final float startDistance;
/*    */   private float oldWaterCost;
/*    */   
/*    */   public FollowOwnerGoal(TamableAnimal tamable, double speedModifier, float startDistance, float stopDistance) {
/* 24 */     this.tamable = tamable;
/* 25 */     this.speedModifier = speedModifier;
/* 26 */     this.navigation = tamable.getNavigation();
/* 27 */     this.startDistance = startDistance;
/* 28 */     this.stopDistance = stopDistance;
/* 29 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*    */     
/* 31 */     if (!(tamable.getNavigation() instanceof net.minecraft.world.entity.ai.navigation.GroundPathNavigation) && !(tamable.getNavigation() instanceof net.minecraft.world.entity.ai.navigation.FlyingPathNavigation)) {
/* 32 */       throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 38 */     LivingEntity owner = this.tamable.getOwner();
/* 39 */     if (owner == null) {
/* 40 */       return false;
/*    */     }
/* 42 */     if (this.tamable.unableToMoveToOwner()) {
/* 43 */       return false;
/*    */     }
/* 45 */     if (this.tamable.distanceToSqr(owner) < (this.startDistance * this.startDistance)) {
/* 46 */       return false;
/*    */     }
/* 48 */     this.owner = owner;
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 54 */     if (this.navigation.isDone()) {
/* 55 */       return false;
/*    */     }
/* 57 */     if (this.tamable.unableToMoveToOwner()) {
/* 58 */       return false;
/*    */     }
/* 60 */     if (this.tamable.distanceToSqr(this.owner) <= (this.stopDistance * this.stopDistance)) {
/* 61 */       return false;
/*    */     }
/* 63 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 68 */     this.timeToRecalcPath = 0;
/* 69 */     this.oldWaterCost = this.tamable.getPathfindingMalus(PathType.WATER);
/* 70 */     this.tamable.setPathfindingMalus(PathType.WATER, 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 75 */     this.owner = null;
/* 76 */     this.navigation.stop();
/* 77 */     this.tamable.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 82 */     boolean isOwnerFarAway = this.tamable.shouldTryTeleportToOwner();
/* 83 */     if (!isOwnerFarAway) {
/* 84 */       this.tamable.getLookControl().setLookAt(this.owner, 10.0F, this.tamable.getMaxHeadXRot());
/*    */     }
/*    */     
/* 87 */     if (--this.timeToRecalcPath > 0) {
/*    */       return;
/*    */     }
/* 90 */     this.timeToRecalcPath = adjustedTickDelay(10);
/*    */ 
/*    */     
/* 93 */     if (isOwnerFarAway) {
/* 94 */       this.tamable.tryToTeleportToOwner();
/*    */     } else {
/* 96 */       this.navigation.moveTo(this.owner, this.speedModifier);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\FollowOwnerGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */