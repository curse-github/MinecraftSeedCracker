/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.item.BowItem;
/*     */ import net.minecraft.world.item.Items;
/*     */ 
/*     */ public class RangedBowAttackGoal<T extends Monster & RangedAttackMob> extends Goal {
/*     */   private final T mob;
/*     */   private final double speedModifier;
/*     */   private int attackIntervalMin;
/*     */   private final float attackRadiusSqr;
/*     */   
/*     */   public RangedBowAttackGoal(T mob, double speedModifier, int attackIntervalMin, float attackRadius) {
/*  19 */     this.attackTime = -1;
/*     */ 
/*     */ 
/*     */     
/*  23 */     this.strafingTime = -1;
/*     */ 
/*     */     
/*  26 */     this.mob = mob;
/*  27 */     this.speedModifier = speedModifier;
/*  28 */     this.attackIntervalMin = attackIntervalMin;
/*  29 */     this.attackRadiusSqr = attackRadius * attackRadius;
/*  30 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */   private int attackTime; private int seeTime; private boolean strafingClockwise; private boolean strafingBackwards; private int strafingTime;
/*     */   
/*  34 */   public void setMinAttackInterval(int ticks) { this.attackIntervalMin = ticks; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  39 */     if (this.mob.getTarget() == null) {
/*  40 */       return false;
/*     */     }
/*  42 */     return isHoldingBow();
/*     */   }
/*     */ 
/*     */   
/*  46 */   protected boolean isHoldingBow() { return this.mob.isHolding(Items.BOW); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   public boolean canContinueToUse() { return ((canUse() || !this.mob.getNavigation().isDone()) && isHoldingBow()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  56 */     super.start();
/*     */     
/*  58 */     this.mob.setAggressive(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  63 */     super.stop();
/*     */     
/*  65 */     this.mob.setAggressive(false);
/*  66 */     this.seeTime = 0;
/*  67 */     this.attackTime = -1;
/*  68 */     this.mob.stopUsingItem();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  73 */   public boolean requiresUpdateEveryTick() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  78 */     LivingEntity target = this.mob.getTarget();
/*  79 */     if (target == null) {
/*     */       return;
/*     */     }
/*  82 */     double targetDistSqr = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
/*  83 */     boolean hasLineOfSight = this.mob.getSensing().hasLineOfSight(target);
/*  84 */     boolean hadLineOfSight = (this.seeTime > 0);
/*     */     
/*  86 */     if (hasLineOfSight != hadLineOfSight) {
/*  87 */       this.seeTime = 0;
/*     */     }
/*     */     
/*  90 */     if (hasLineOfSight) {
/*  91 */       this.seeTime++;
/*     */     } else {
/*  93 */       this.seeTime--;
/*     */     } 
/*     */     
/*  96 */     if (targetDistSqr > this.attackRadiusSqr || this.seeTime < 20) {
/*  97 */       this.mob.getNavigation().moveTo(target, this.speedModifier);
/*  98 */       this.strafingTime = -1;
/*     */     } else {
/* 100 */       this.mob.getNavigation().stop();
/* 101 */       this.strafingTime++;
/*     */     } 
/*     */     
/* 104 */     if (this.strafingTime >= 20) {
/* 105 */       if (this.mob.getRandom().nextFloat() < 0.3D) {
/* 106 */         this.strafingClockwise = !this.strafingClockwise;
/*     */       }
/* 108 */       if (this.mob.getRandom().nextFloat() < 0.3D) {
/* 109 */         this.strafingBackwards = !this.strafingBackwards;
/*     */       }
/* 111 */       this.strafingTime = 0;
/*     */     } 
/*     */     
/* 114 */     if (this.strafingTime > -1) {
/* 115 */       if (targetDistSqr > (this.attackRadiusSqr * 0.75F)) {
/* 116 */         this.strafingBackwards = false;
/* 117 */       } else if (targetDistSqr < (this.attackRadiusSqr * 0.25F)) {
/* 118 */         this.strafingBackwards = true;
/*     */       } 
/* 120 */       this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
/* 121 */       Entity entity = this.mob.getControlledVehicle(); if (entity instanceof Mob) { Mob vehicle = (Mob)entity;
/* 122 */         vehicle.lookAt(target, 30.0F, 30.0F); }
/*     */       
/* 124 */       this.mob.lookAt(target, 30.0F, 30.0F);
/*     */     } else {
/* 126 */       this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
/*     */     } 
/*     */     
/* 129 */     if (this.mob.isUsingItem()) {
/* 130 */       if (!hasLineOfSight && this.seeTime < -60) {
/* 131 */         this.mob.stopUsingItem();
/* 132 */       } else if (hasLineOfSight) {
/* 133 */         int pullTime = this.mob.getTicksUsingItem();
/*     */         
/* 135 */         if (pullTime >= 20) {
/* 136 */           this.mob.stopUsingItem();
/* 137 */           ((RangedAttackMob)this.mob).performRangedAttack(target, BowItem.getPowerForTime(pullTime));
/* 138 */           this.attackTime = this.attackIntervalMin;
/*     */         } 
/*     */       } 
/* 141 */     } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
/* 142 */       this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.mob, Items.BOW));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\RangedBowAttackGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */