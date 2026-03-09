/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.item.component.KineticWeapon;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpearUseGoal<T extends Monster>
/*     */   extends Goal
/*     */ {
/*     */   static final int MIN_REPOSITION_DISTANCE = 6;
/*     */   static final int MAX_REPOSITION_DISTANCE = 7;
/*     */   static final int MIN_COOLDOWN_DISTANCE = 9;
/*     */   static final int MAX_COOLDOWN_DISTANCE = 11;
/*  24 */   private static final double MAX_FLEEING_TIME = reducedTickDelay(100);
/*     */   
/*     */   private final T mob;
/*     */   
/*     */   private SpearUseState state;
/*     */   double speedModifierWhenCharging;
/*     */   double speedModifierWhenRepositioning;
/*     */   float approachDistanceSq;
/*     */   float targetInRangeRadiusSq;
/*     */   
/*     */   public SpearUseGoal(T mob, double speedModifierWhenCharging, double speedModifierWhenRepositioning, float approachDistance, float targetInRangeRadius) {
/*  35 */     this.mob = mob;
/*  36 */     this.speedModifierWhenCharging = speedModifierWhenCharging;
/*  37 */     this.speedModifierWhenRepositioning = speedModifierWhenRepositioning;
/*  38 */     this.approachDistanceSq = approachDistance * approachDistance;
/*  39 */     this.targetInRangeRadiusSq = targetInRangeRadius * targetInRangeRadius;
/*  40 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  45 */   public boolean canUse() { return (ableToAttack() && !this.mob.isUsingItem()); }
/*     */ 
/*     */ 
/*     */   
/*  49 */   private boolean ableToAttack() { return (this.mob.getTarget() != null && this.mob.getMainHandItem().has(DataComponents.KINETIC_WEAPON)); }
/*     */ 
/*     */   
/*     */   private int getKineticWeaponUseDuration() {
/*  53 */     int durationTicks = ((Integer)Optional.ofNullable((KineticWeapon)this.mob.getMainHandItem().get(DataComponents.KINETIC_WEAPON)).map(KineticWeapon::computeDamageUseDuration).orElse(Integer.valueOf(0))).intValue();
/*  54 */     return reducedTickDelay(durationTicks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  59 */   public boolean canContinueToUse() { return (this.state != null && !this.state.done && ableToAttack()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  64 */     super.start();
/*  65 */     this.mob.setAggressive(true);
/*  66 */     this.state = new SpearUseState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  71 */     super.stop();
/*  72 */     this.mob.getNavigation().stop();
/*  73 */     this.mob.setAggressive(false);
/*  74 */     this.state = null;
/*  75 */     this.mob.stopUsingItem();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  80 */     if (this.state == null) {
/*     */       return;
/*     */     }
/*     */     
/*  84 */     LivingEntity target = this.mob.getTarget();
/*  85 */     double targetDistSqr = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
/*  86 */     Entity mount = this.mob.getRootVehicle();
/*  87 */     float speedModifier = 1.0F;
/*  88 */     if (mount instanceof Mob) { Mob vehicleMob = (Mob)mount;
/*  89 */       speedModifier = vehicleMob.chargeSpeedModifier(); }
/*     */     
/*  91 */     int mountDistance = this.mob.isPassenger() ? 2 : 0;
/*     */     
/*  93 */     this.mob.lookAt(target, 30.0F, 30.0F);
/*  94 */     this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
/*     */     
/*  96 */     if (this.state.notEngagedYet()) {
/*  97 */       if (targetDistSqr > this.approachDistanceSq) {
/*  98 */         this.mob.getNavigation().moveTo(target, speedModifier * this.speedModifierWhenRepositioning);
/*     */         return;
/*     */       } 
/* 101 */       this.state.startEngagement(getKineticWeaponUseDuration());
/* 102 */       this.mob.startUsingItem(InteractionHand.MAIN_HAND);
/*     */     } 
/*     */     
/* 105 */     if (this.state.tickAndCheckEngagement()) {
/* 106 */       this.mob.stopUsingItem();
/* 107 */       double distance = Math.sqrt(targetDistSqr);
/* 108 */       this.state.awayPos = LandRandomPos.getPosAway(this.mob, Math.max(0.0D, (9 + mountDistance) - distance), Math.max(1.0D, (11 + mountDistance) - distance), 7, target.position());
/* 109 */       this.state.fleeingTime = 1;
/*     */     } 
/*     */     
/* 112 */     if (this.state.tickAndCheckFleeing()) {
/*     */       return;
/*     */     }
/*     */     
/* 116 */     if (this.state.awayPos != null) {
/* 117 */       this.mob.getNavigation().moveTo(this.state.awayPos.x, this.state.awayPos.y, this.state.awayPos.z, speedModifier * this.speedModifierWhenRepositioning);
/* 118 */       if (this.mob.getNavigation().isDone()) {
/* 119 */         if (this.state.fleeingTime > 0) {
/* 120 */           this.state.done = true;
/*     */           return;
/*     */         } 
/* 123 */         this.state.awayPos = null;
/*     */       } 
/*     */     } else {
/* 126 */       this.mob.getNavigation().moveTo(target, speedModifier * this.speedModifierWhenCharging);
/*     */       
/* 128 */       if (targetDistSqr < this.targetInRangeRadiusSq || this.mob.getNavigation().isDone()) {
/* 129 */         double distance = Math.sqrt(targetDistSqr);
/* 130 */         this.state.awayPos = LandRandomPos.getPosAway(this.mob, (6 + mountDistance) - distance, (7 + mountDistance) - distance, 7, target.position());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SpearUseState
/*     */   {
/* 142 */     private int engageTime = -1;
/* 143 */     private int fleeingTime = -1;
/*     */     
/*     */     private Vec3 awayPos;
/*     */     private boolean done = false;
/*     */     
/* 148 */     public boolean notEngagedYet() { return (this.engageTime < 0); }
/*     */ 
/*     */ 
/*     */     
/* 152 */     public void startEngagement(int spearDownTime) { this.engageTime = spearDownTime; }
/*     */ 
/*     */     
/*     */     public boolean tickAndCheckEngagement() {
/* 156 */       if (this.engageTime > 0) {
/* 157 */         this.engageTime--;
/* 158 */         if (this.engageTime == 0) {
/* 159 */           return true;
/*     */         }
/*     */       } 
/* 162 */       return false;
/*     */     }
/*     */     
/*     */     public boolean tickAndCheckFleeing() {
/* 166 */       if (this.fleeingTime > 0) {
/* 167 */         this.fleeingTime++;
/* 168 */         if (this.fleeingTime > SpearUseGoal.MAX_FLEEING_TIME) {
/* 169 */           this.done = true;
/* 170 */           return true;
/*     */         } 
/*     */       } 
/* 173 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\SpearUseGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */