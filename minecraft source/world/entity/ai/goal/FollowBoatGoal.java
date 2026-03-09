/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FollowBoatGoal
/*     */   extends Goal
/*     */ {
/*     */   private int timeToRecalcPath;
/*     */   private final PathfinderMob mob;
/*     */   private Player following;
/*     */   private BoatGoals currentGoal;
/*     */   
/*  27 */   public FollowBoatGoal(PathfinderMob mob) { this.mob = mob; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  32 */     if (this.following != null && this.following.hasMovedHorizontallyRecently()) {
/*  33 */       return true;
/*     */     }
/*  35 */     List<AbstractBoat> boats = this.mob.level().getEntitiesOfClass(AbstractBoat.class, this.mob.getBoundingBox().inflate(5.0D));
/*  36 */     for (AbstractBoat boat : boats) {
/*  37 */       LivingEntity livingEntity = boat.getControllingPassenger(); if (livingEntity instanceof Player) { Player controllingPlayer = (Player)livingEntity; if (controllingPlayer.hasMovedHorizontallyRecently())
/*  38 */           return true;  }
/*     */     
/*     */     } 
/*  41 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  46 */   public boolean isInterruptable() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   public boolean canContinueToUse() { return (this.following != null && this.following.isPassenger() && this.following.hasMovedHorizontallyRecently()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  56 */     List<AbstractBoat> boats = this.mob.level().getEntitiesOfClass(AbstractBoat.class, this.mob.getBoundingBox().inflate(5.0D));
/*  57 */     for (AbstractBoat boat : boats) {
/*  58 */       LivingEntity livingEntity = boat.getControllingPassenger(); if (livingEntity instanceof Player) { Player player = (Player)livingEntity;
/*  59 */         this.following = player;
/*     */         
/*     */         break; }
/*     */     
/*     */     } 
/*  64 */     this.timeToRecalcPath = 0;
/*  65 */     this.currentGoal = BoatGoals.GO_TO_BOAT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  70 */   public void stop() { this.following = null; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  75 */     float speed = (this.currentGoal == BoatGoals.GO_IN_BOAT_DIRECTION) ? 0.01F : 0.015F;
/*  76 */     this.mob.moveRelative(speed, new Vec3(this.mob.xxa, this.mob.yya, this.mob.zza));
/*  77 */     this.mob.move(MoverType.SELF, this.mob.getDeltaMovement());
/*     */     
/*  79 */     if (--this.timeToRecalcPath > 0) {
/*     */       return;
/*     */     }
/*  82 */     this.timeToRecalcPath = adjustedTickDelay(10);
/*     */     
/*  84 */     if (this.currentGoal == BoatGoals.GO_TO_BOAT) {
/*  85 */       BlockPos behindBoatPos = this.following.blockPosition().relative(this.following.getDirection().getOpposite());
/*  86 */       behindBoatPos = behindBoatPos.offset(0, -1, 0);
/*  87 */       this.mob.getNavigation().moveTo(behindBoatPos.getX(), behindBoatPos.getY(), behindBoatPos.getZ(), 1.0D);
/*     */       
/*  89 */       if (this.mob.distanceTo(this.following) < 4.0F) {
/*  90 */         this.timeToRecalcPath = 0;
/*  91 */         this.currentGoal = BoatGoals.GO_IN_BOAT_DIRECTION;
/*     */       } 
/*  93 */     } else if (this.currentGoal == BoatGoals.GO_IN_BOAT_DIRECTION) {
/*     */       
/*  95 */       Direction direction = this.following.getMotionDirection();
/*  96 */       BlockPos goTo = this.following.blockPosition().relative(direction, 10);
/*     */ 
/*     */       
/*  99 */       this.mob.getNavigation().moveTo(goTo.getX(), (goTo.getY() - 1), goTo.getZ(), 1.0D);
/*     */       
/* 101 */       if (this.mob.distanceTo(this.following) > 12.0F) {
/* 102 */         this.timeToRecalcPath = 0;
/* 103 */         this.currentGoal = BoatGoals.GO_TO_BOAT;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\FollowBoatGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */