/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FollowMobGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Mob mob;
/*     */   private final Predicate<Mob> followPredicate;
/*     */   private Mob followingMob;
/*     */   private final double speedModifier;
/*     */   private final PathNavigation navigation;
/*     */   private int timeToRecalcPath;
/*     */   private final float stopDistance;
/*     */   private float oldWaterCost;
/*     */   private final float areaSize;
/*     */   
/*     */   public FollowMobGoal(Mob mob, double speedModifier, float stopDistance, float areaSize) {
/*  27 */     this.mob = mob;
/*  28 */     this.followPredicate = (input -> (mob.getClass() != input.getClass()));
/*  29 */     this.speedModifier = speedModifier;
/*  30 */     this.navigation = mob.getNavigation();
/*  31 */     this.stopDistance = stopDistance;
/*  32 */     this.areaSize = areaSize;
/*     */     
/*  34 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     
/*  36 */     if (!(mob.getNavigation() instanceof net.minecraft.world.entity.ai.navigation.GroundPathNavigation) && !(mob.getNavigation() instanceof net.minecraft.world.entity.ai.navigation.FlyingPathNavigation)) {
/*  37 */       throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  43 */     List<Mob> mobs = this.mob.level().getEntitiesOfClass(Mob.class, this.mob.getBoundingBox().inflate(this.areaSize), this.followPredicate);
/*  44 */     if (!mobs.isEmpty()) {
/*  45 */       for (Mob mobInList : mobs) {
/*  46 */         if (mobInList.isInvisible()) {
/*     */           continue;
/*     */         }
/*     */         
/*  50 */         this.followingMob = mobInList;
/*  51 */         return true;
/*     */       } 
/*     */     }
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  59 */   public boolean canContinueToUse() { return (this.followingMob != null && !this.navigation.isDone() && this.mob.distanceToSqr(this.followingMob) > (this.stopDistance * this.stopDistance)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  64 */     this.timeToRecalcPath = 0;
/*  65 */     this.oldWaterCost = this.mob.getPathfindingMalus(PathType.WATER);
/*  66 */     this.mob.setPathfindingMalus(PathType.WATER, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  71 */     this.followingMob = null;
/*  72 */     this.navigation.stop();
/*  73 */     this.mob.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  78 */     if (this.followingMob == null || this.mob.isLeashed()) {
/*     */       return;
/*     */     }
/*     */     
/*  82 */     this.mob.getLookControl().setLookAt(this.followingMob, 10.0F, this.mob.getMaxHeadXRot());
/*     */     
/*  84 */     if (--this.timeToRecalcPath > 0) {
/*     */       return;
/*     */     }
/*  87 */     this.timeToRecalcPath = adjustedTickDelay(10);
/*     */     
/*  89 */     double xxd = this.mob.getX() - this.followingMob.getX();
/*  90 */     double yyd = this.mob.getY() - this.followingMob.getY();
/*  91 */     double zzd = this.mob.getZ() - this.followingMob.getZ();
/*     */     
/*  93 */     double distSqr = xxd * xxd + yyd * yyd + zzd * zzd;
/*  94 */     if (distSqr <= (this.stopDistance * this.stopDistance)) {
/*  95 */       this.navigation.stop();
/*     */       
/*  97 */       LookControl lookControl = this.followingMob.getLookControl();
/*  98 */       if (distSqr <= this.stopDistance || (lookControl.getWantedX() == this.mob.getX() && lookControl.getWantedY() == this.mob.getY() && lookControl.getWantedZ() == this.mob.getZ())) {
/*  99 */         double deltaX = this.followingMob.getX() - this.mob.getX();
/* 100 */         double deltaZ = this.followingMob.getZ() - this.mob.getZ();
/* 101 */         this.navigation.moveTo(this.mob.getX() - deltaX, this.mob.getY(), this.mob.getZ() - deltaZ, this.speedModifier);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 106 */     this.navigation.moveTo(this.followingMob, this.speedModifier);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\FollowMobGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */