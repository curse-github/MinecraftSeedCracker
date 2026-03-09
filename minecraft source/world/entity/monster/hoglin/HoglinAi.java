/*     */ package net.minecraft.world.entity.monster.hoglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
/*     */ import net.minecraft.world.entity.ai.behavior.BecomePassiveIfMemoryPresent;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.EraseMemoryIf;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetAwayFrom;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HoglinAi
/*     */ {
/*     */   public static final int REPELLENT_DETECTION_RANGE_HORIZONTAL = 8;
/*     */   public static final int REPELLENT_DETECTION_RANGE_VERTICAL = 4;
/*  50 */   private static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
/*     */   private static final int ATTACK_DURATION = 200;
/*     */   private static final int DESIRED_DISTANCE_FROM_PIGLIN_WHEN_IDLING = 8;
/*     */   private static final int DESIRED_DISTANCE_FROM_PIGLIN_WHEN_RETREATING = 15;
/*     */   private static final int ATTACK_INTERVAL = 40;
/*     */   private static final int BABY_ATTACK_INTERVAL = 15;
/*     */   private static final int REPELLENT_PACIFY_TIME = 200;
/*  57 */   private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_AVOIDING_REPELLENT = 1.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_RETREATING = 1.3F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 0.6F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.4F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT = 0.6F;
/*     */   
/*     */   protected static Brain<?> makeBrain(Brain<Hoglin> brain) {
/*  67 */     initCoreActivity(brain);
/*     */     
/*  69 */     initIdleActivity(brain);
/*  70 */     initFightActivity(brain);
/*  71 */     initRetreatActivity(brain);
/*     */     
/*  73 */     brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
/*  74 */     brain.setDefaultActivity(Activity.IDLE);
/*  75 */     brain.useDefaultActivity();
/*  76 */     return brain;
/*     */   }
/*     */ 
/*     */   
/*  80 */   private static void initCoreActivity(Brain<Hoglin> brain) { brain.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink())); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Hoglin> brain) {
/*  87 */     brain.addActivity(Activity.IDLE, 10, ImmutableList.of(
/*  88 */           BecomePassiveIfMemoryPresent.create(MemoryModuleType.NEAREST_REPELLENT, 200), new AnimalMakeLove(EntityType.HOGLIN, 0.6F, 2), 
/*     */           
/*  90 */           SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0F, 8, true), 
/*  91 */           StartAttacking.create(HoglinAi::findNearestValidAttackTarget), 
/*  92 */           BehaviorBuilder.triggerIf(Hoglin::isAdult, SetWalkTargetAwayFrom.entity(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, 0.4F, 8, false)), 
/*  93 */           SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)), 
/*  94 */           BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 0.6F), 
/*  95 */           createIdleMovementBehaviors()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 100 */   private static void initFightActivity(Brain<Hoglin> brain) { brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(
/* 101 */           BecomePassiveIfMemoryPresent.create(MemoryModuleType.NEAREST_REPELLENT, 200), new AnimalMakeLove(EntityType.HOGLIN, 0.6F, 2), 
/*     */           
/* 103 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F), 
/* 104 */           BehaviorBuilder.triggerIf(Hoglin::isAdult, MeleeAttack.create(40)), 
/* 105 */           BehaviorBuilder.triggerIf(AgeableMob::isBaby, MeleeAttack.create(15)), 
/* 106 */           StopAttackingIfTargetInvalid.create(), 
/* 107 */           EraseMemoryIf.create(HoglinAi::isBreeding, MemoryModuleType.ATTACK_TARGET)), MemoryModuleType.ATTACK_TARGET); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   private static void initRetreatActivity(Brain<Hoglin> brain) { brain.addActivityAndRemoveMemoryWhenStopped(Activity.AVOID, 10, ImmutableList.of(
/* 113 */           SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.3F, 15, false), 
/* 114 */           createIdleMovementBehaviors(), 
/* 115 */           SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)), 
/* 116 */           EraseMemoryIf.create(HoglinAi::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)), MemoryModuleType.AVOID_TARGET); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static RunOne<Hoglin> createIdleMovementBehaviors() {
/* 121 */     return new RunOne(ImmutableList.of(
/* 122 */           Pair.of(RandomStroll.stroll(0.4F), Integer.valueOf(2)), 
/* 123 */           Pair.of(SetWalkTargetFromLookTarget.create(0.4F, 3), Integer.valueOf(2)), 
/* 124 */           Pair.of(new DoNothing(30, 60), Integer.valueOf(1))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void updateActivity(Hoglin body) {
/* 129 */     Brain<Hoglin> brain = body.getBrain();
/*     */     
/* 131 */     Activity oldActivity = (Activity)brain.getActiveNonCoreActivity().orElse(null);
/*     */ 
/*     */     
/* 134 */     brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.AVOID, Activity.IDLE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     Activity newActivity = (Activity)brain.getActiveNonCoreActivity().orElse(null);
/* 141 */     if (oldActivity != newActivity) {
/*     */       
/* 143 */       Objects.requireNonNull(body); getSoundForCurrentActivity(body).ifPresent(body::makeSound);
/*     */     } 
/*     */ 
/*     */     
/* 147 */     body.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/*     */   }
/*     */   
/*     */   protected static void onHitTarget(Hoglin attackerBody, LivingEntity target) {
/* 151 */     if (attackerBody.isBaby()) {
/*     */       return;
/*     */     }
/*     */     
/* 155 */     if (target.getType() == EntityType.PIGLIN && piglinsOutnumberHoglins(attackerBody)) {
/*     */       
/* 157 */       setAvoidTarget(attackerBody, target);
/* 158 */       broadcastRetreat(attackerBody, target);
/*     */       return;
/*     */     } 
/* 161 */     broadcastAttackTarget(attackerBody, target);
/*     */   }
/*     */ 
/*     */   
/* 165 */   private static void broadcastRetreat(Hoglin body, LivingEntity target) { getVisibleAdultHoglins(body).forEach(hoglin -> retreatFromNearestTarget(hoglin, target)); }
/*     */ 
/*     */   
/*     */   private static void retreatFromNearestTarget(Hoglin body, LivingEntity newAvoidTarget) {
/* 169 */     LivingEntity nearest = newAvoidTarget;
/*     */     
/* 171 */     Brain<Hoglin> brain = body.getBrain();
/* 172 */     nearest = BehaviorUtils.getNearestTarget(body, brain.getMemory(MemoryModuleType.AVOID_TARGET), nearest);
/* 173 */     nearest = BehaviorUtils.getNearestTarget(body, brain.getMemory(MemoryModuleType.ATTACK_TARGET), nearest);
/*     */     
/* 175 */     setAvoidTarget(body, nearest);
/*     */   }
/*     */   
/*     */   private static void setAvoidTarget(Hoglin body, LivingEntity avoidTarget) {
/* 179 */     body.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/* 180 */     body.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 181 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, avoidTarget, RETREAT_DURATION.sample((body.level()).random));
/*     */   }
/*     */   
/*     */   private static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel level, Hoglin body) {
/* 185 */     if (isPacified(body) || isBreeding(body))
/*     */     {
/* 187 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 191 */     return body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
/*     */   }
/*     */   
/*     */   static boolean isPosNearNearestRepellent(Hoglin body, BlockPos pos) {
/* 195 */     Optional<BlockPos> repellentPos = body.getBrain().getMemory(MemoryModuleType.NEAREST_REPELLENT);
/* 196 */     return (repellentPos.isPresent() && ((BlockPos)repellentPos.get()).closerThan(pos, 8.0D));
/*     */   }
/*     */ 
/*     */   
/* 200 */   private static boolean wantsToStopFleeing(Hoglin body) { return (body.isAdult() && !piglinsOutnumberHoglins(body)); }
/*     */ 
/*     */   
/*     */   private static boolean piglinsOutnumberHoglins(Hoglin body) {
/* 204 */     if (body.isBaby()) {
/* 205 */       return false;
/*     */     }
/*     */     
/* 208 */     int piglinCount = ((Integer)body.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT).orElse(Integer.valueOf(0))).intValue();
/* 209 */     int hoglinCount = ((Integer)body.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT).orElse(Integer.valueOf(0))).intValue() + 1;
/* 210 */     return (piglinCount > hoglinCount);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void wasHurtBy(ServerLevel level, Hoglin body, LivingEntity attacker) {
/* 215 */     Brain<Hoglin> brain = body.getBrain();
/* 216 */     brain.eraseMemory(MemoryModuleType.PACIFIED);
/* 217 */     brain.eraseMemory(MemoryModuleType.BREED_TARGET);
/*     */     
/* 219 */     if (body.isBaby()) {
/*     */       
/* 221 */       retreatFromNearestTarget(body, attacker);
/*     */       
/*     */       return;
/*     */     } 
/* 225 */     maybeRetaliate(level, body, attacker);
/*     */   }
/*     */   
/*     */   private static void maybeRetaliate(ServerLevel level, Hoglin body, LivingEntity attacker) {
/* 229 */     if (body.getBrain().isActive(Activity.AVOID) && attacker.getType() == EntityType.PIGLIN) {
/*     */       return;
/*     */     }
/* 232 */     if (attacker.getType() == EntityType.HOGLIN) {
/*     */       return;
/*     */     }
/* 235 */     if (BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(body, attacker, 4.0D)) {
/*     */       return;
/*     */     }
/*     */     
/* 239 */     if (!Sensor.isEntityAttackable(level, body, attacker)) {
/*     */       return;
/*     */     }
/*     */     
/* 243 */     setAttackTarget(body, attacker);
/* 244 */     broadcastAttackTarget(body, attacker);
/*     */   }
/*     */   
/*     */   private static void setAttackTarget(Hoglin body, LivingEntity target) {
/* 248 */     Brain<Hoglin> brain = body.getBrain();
/* 249 */     brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 250 */     brain.eraseMemory(MemoryModuleType.BREED_TARGET);
/* 251 */     brain.setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, target, 200L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 256 */   private static void broadcastAttackTarget(Hoglin body, LivingEntity target) { getVisibleAdultHoglins(body).forEach(hoglin -> setAttackTargetIfCloserThanCurrent(hoglin, target)); }
/*     */ 
/*     */   
/*     */   private static void setAttackTargetIfCloserThanCurrent(Hoglin body, LivingEntity newTarget) {
/* 260 */     if (isPacified(body)) {
/*     */       return;
/*     */     }
/*     */     
/* 264 */     Optional<LivingEntity> currentTarget = body.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
/* 265 */     LivingEntity nearest = BehaviorUtils.getNearestTarget(body, currentTarget, newTarget);
/* 266 */     setAttackTarget(body, nearest);
/*     */   }
/*     */ 
/*     */   
/* 270 */   public static Optional<SoundEvent> getSoundForCurrentActivity(Hoglin body) { return body.getBrain().getActiveNonCoreActivity().map(activity -> getSoundForActivity(body, activity)); }
/*     */ 
/*     */   
/*     */   private static SoundEvent getSoundForActivity(Hoglin body, Activity activity) {
/* 274 */     if (activity == Activity.AVOID || body.isConverting())
/* 275 */       return SoundEvents.HOGLIN_RETREAT; 
/* 276 */     if (activity == Activity.FIGHT)
/* 277 */       return SoundEvents.HOGLIN_ANGRY; 
/* 278 */     if (isNearRepellent(body)) {
/* 279 */       return SoundEvents.HOGLIN_RETREAT;
/*     */     }
/* 281 */     return SoundEvents.HOGLIN_AMBIENT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 286 */   private static List<Hoglin> getVisibleAdultHoglins(Hoglin body) { return (List)body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS).orElse(ImmutableList.of()); }
/*     */ 
/*     */ 
/*     */   
/* 290 */   private static boolean isNearRepellent(Hoglin body) { return body.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT); }
/*     */ 
/*     */ 
/*     */   
/* 294 */   private static boolean isBreeding(Hoglin body) { return body.getBrain().hasMemoryValue(MemoryModuleType.BREED_TARGET); }
/*     */ 
/*     */ 
/*     */   
/* 298 */   protected static boolean isPacified(Hoglin body) { return body.getBrain().hasMemoryValue(MemoryModuleType.PACIFIED); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\hoglin\HoglinAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */