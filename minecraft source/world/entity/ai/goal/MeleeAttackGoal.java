/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ 
/*     */ public class MeleeAttackGoal extends Goal {
/*     */   protected final PathfinderMob mob;
/*     */   private final double speedModifier;
/*     */   private final boolean followingTargetEvenIfNotSeen;
/*     */   private Path path;
/*     */   private double pathedTargetX;
/*     */   private double pathedTargetY;
/*     */   private double pathedTargetZ;
/*     */   private int ticksUntilNextPathRecalculation;
/*     */   private int ticksUntilNextAttack;
/*     */   private final int attackInterval = 20;
/*     */   private long lastCanUseCheck;
/*     */   private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;
/*     */   
/*     */   public MeleeAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
/*  26 */     this.attackInterval = 20;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  32 */     this.mob = mob;
/*  33 */     this.speedModifier = speedModifier;
/*  34 */     this.followingTargetEvenIfNotSeen = followingTargetEvenIfNotSeen;
/*  35 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  40 */     long time = this.mob.level().getGameTime();
/*  41 */     if (time - this.lastCanUseCheck < 20L) {
/*  42 */       return false;
/*     */     }
/*     */     
/*  45 */     this.lastCanUseCheck = time;
/*     */     
/*  47 */     LivingEntity target = this.mob.getTarget();
/*  48 */     if (target == null) {
/*  49 */       return false;
/*     */     }
/*  51 */     if (!target.isAlive()) {
/*  52 */       return false;
/*     */     }
/*  54 */     this.path = this.mob.getNavigation().createPath(target, 0);
/*  55 */     if (this.path != null) {
/*  56 */       return true;
/*     */     }
/*  58 */     if (this.mob.isWithinMeleeAttackRange(target)) {
/*  59 */       return true;
/*     */     }
/*  61 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  66 */     LivingEntity target = this.mob.getTarget();
/*  67 */     if (target == null) {
/*  68 */       return false;
/*     */     }
/*  70 */     if (!target.isAlive()) {
/*  71 */       return false;
/*     */     }
/*  73 */     if (!this.followingTargetEvenIfNotSeen) {
/*  74 */       return !this.mob.getNavigation().isDone();
/*     */     }
/*  76 */     if (!this.mob.isWithinHome(target.blockPosition())) {
/*  77 */       return false;
/*     */     }
/*     */     
/*  80 */     if (target instanceof Player) { Player player = (Player)target; if (player.isSpectator() || player.isCreative()) {
/*  81 */         return false;
/*     */       } }
/*     */     
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  89 */     this.mob.getNavigation().moveTo(this.path, this.speedModifier);
/*  90 */     this.mob.setAggressive(true);
/*  91 */     this.ticksUntilNextPathRecalculation = 0;
/*  92 */     this.ticksUntilNextAttack = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  97 */     LivingEntity target = this.mob.getTarget();
/*  98 */     if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
/*  99 */       this.mob.setTarget(null);
/*     */     }
/* 101 */     this.mob.setAggressive(false);
/* 102 */     this.mob.getNavigation().stop();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public boolean requiresUpdateEveryTick() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 112 */     LivingEntity target = this.mob.getTarget();
/* 113 */     if (target == null) {
/*     */       return;
/*     */     }
/* 116 */     this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
/* 117 */     this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
/*     */     
/* 119 */     if ((this.followingTargetEvenIfNotSeen || this.mob.getSensing().hasLineOfSight(target)) && 
/* 120 */       this.ticksUntilNextPathRecalculation <= 0 && ((
/* 121 */       this.pathedTargetX == 0.0D && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D) || target.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0D || this.mob.getRandom().nextFloat() < 0.05F)) {
/* 122 */       this.pathedTargetX = target.getX();
/* 123 */       this.pathedTargetY = target.getY();
/* 124 */       this.pathedTargetZ = target.getZ();
/* 125 */       this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
/*     */       
/* 127 */       double targetDistanceSqr = this.mob.distanceToSqr(target);
/* 128 */       if (targetDistanceSqr > 1024.0D) {
/* 129 */         this.ticksUntilNextPathRecalculation += 10;
/* 130 */       } else if (targetDistanceSqr > 256.0D) {
/* 131 */         this.ticksUntilNextPathRecalculation += 5;
/*     */       } 
/*     */       
/* 134 */       if (!this.mob.getNavigation().moveTo(target, this.speedModifier)) {
/* 135 */         this.ticksUntilNextPathRecalculation += 15;
/*     */       }
/*     */       
/* 138 */       this.ticksUntilNextPathRecalculation = adjustedTickDelay(this.ticksUntilNextPathRecalculation);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 143 */     this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
/* 144 */     checkAndPerformAttack(target);
/*     */   }
/*     */   
/*     */   protected void checkAndPerformAttack(LivingEntity target) {
/* 148 */     if (canPerformAttack(target)) {
/* 149 */       resetAttackCooldown();
/* 150 */       this.mob.swing(InteractionHand.MAIN_HAND);
/* 151 */       this.mob.doHurtTarget(getServerLevel(this.mob), target);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 156 */   protected void resetAttackCooldown() { this.ticksUntilNextAttack = adjustedTickDelay(20); }
/*     */ 
/*     */ 
/*     */   
/* 160 */   protected boolean isTimeToAttack() { return (this.ticksUntilNextAttack <= 0); }
/*     */ 
/*     */ 
/*     */   
/* 164 */   protected boolean canPerformAttack(LivingEntity target) { return (isTimeToAttack() && this.mob.isWithinMeleeAttackRange(target) && this.mob.getSensing().hasLineOfSight(target)); }
/*     */ 
/*     */ 
/*     */   
/* 168 */   protected int getTicksUntilNextAttack() { return this.ticksUntilNextAttack; }
/*     */ 
/*     */ 
/*     */   
/* 172 */   protected int getAttackInterval() { return adjustedTickDelay(20); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\MeleeAttackGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */