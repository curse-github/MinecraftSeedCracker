/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class EndermanLookForPlayerGoal
/*     */   extends NearestAttackableTargetGoal<Player>
/*     */ {
/*     */   private final EnderMan enderman;
/*     */   private Player pendingTarget;
/*     */   private int aggroTime;
/*     */   private int teleportTime;
/*     */   private final TargetingConditions startAggroTargetConditions;
/* 415 */   private final TargetingConditions continueAggroTargetConditions = TargetingConditions.forCombat().ignoreLineOfSight();
/*     */   private final TargetingConditions.Selector isAngerInducing;
/*     */   
/*     */   public EndermanLookForPlayerGoal(EnderMan enderman, TargetingConditions.Selector isAngryAt) {
/* 419 */     super(enderman, Player.class, 10, false, false, isAngryAt);
/* 420 */     this.enderman = enderman;
/* 421 */     this.isAngerInducing = ((target, level) -> ((enderman.isBeingStaredBy((Player)target) || enderman.isAngryAt(target, level)) && !enderman.hasIndirectPassenger(target)));
/*     */     
/* 423 */     this.startAggroTargetConditions = TargetingConditions.forCombat().range(getFollowDistance()).selector(this.isAngerInducing);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 428 */     this.pendingTarget = getServerLevel(this.enderman).getNearestPlayer(this.startAggroTargetConditions.range(getFollowDistance()), this.enderman);
/* 429 */     return (this.pendingTarget != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 434 */     this.aggroTime = adjustedTickDelay(5);
/* 435 */     this.teleportTime = 0;
/* 436 */     this.enderman.setBeingStaredAt();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 442 */     this.pendingTarget = null;
/*     */     
/* 444 */     super.stop();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/* 449 */     if (this.pendingTarget != null) {
/* 450 */       if (!this.isAngerInducing.test(this.pendingTarget, getServerLevel(this.enderman))) {
/* 451 */         return false;
/*     */       }
/* 453 */       this.enderman.lookAt(this.pendingTarget, 10.0F, 10.0F);
/* 454 */       return true;
/* 455 */     }  if (this.target != null) {
/* 456 */       if (this.enderman.hasIndirectPassenger(this.target))
/* 457 */         return false; 
/* 458 */       if (this.continueAggroTargetConditions.test(getServerLevel(this.enderman), this.enderman, this.target)) {
/* 459 */         return true;
/*     */       }
/*     */     } 
/* 462 */     return super.canContinueToUse();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 467 */     if (this.enderman.getTarget() == null) {
/* 468 */       setTarget(null);
/*     */     }
/*     */     
/* 471 */     if (this.pendingTarget != null) {
/* 472 */       if (--this.aggroTime <= 0) {
/* 473 */         this.target = this.pendingTarget;
/* 474 */         this.pendingTarget = null;
/* 475 */         super.start();
/*     */       } 
/*     */     } else {
/* 478 */       if (this.target != null && !this.enderman.isPassenger()) {
/* 479 */         if (this.enderman.isBeingStaredBy((Player)this.target)) {
/* 480 */           if (this.target.distanceToSqr(this.enderman) < 16.0D) {
/* 481 */             this.enderman.teleport();
/*     */           }
/* 483 */           this.teleportTime = 0;
/* 484 */         } else if (this.target.distanceToSqr(this.enderman) > 256.0D && 
/* 485 */           this.teleportTime++ >= adjustedTickDelay(30) && 
/* 486 */           this.enderman.teleportTowards(this.target)) {
/* 487 */           this.teleportTime = 0;
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 493 */       super.tick();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\EnderMan$EndermanLookForPlayerGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */