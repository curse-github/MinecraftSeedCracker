/*     */ package net.minecraft.world.entity.animal.camel;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomLookAround;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ public class CamelAi {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 4.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 2.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 2.5F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT = 2.5F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 1.0F;
/*  43 */   private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
/*     */   
/*  45 */   private static final ImmutableList<SensorType<? extends Sensor<? super Camel>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.FOOD_TEMPTATIONS, SensorType.NEAREST_ADULT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.IS_PANICKING, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.GAZE_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, new MemoryModuleType[] { MemoryModuleType.BREED_TARGET, MemoryModuleType.NEAREST_VISIBLE_ADULT });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void initMemories(Camel body, RandomSource random) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static Brain.Provider<Camel> brainProvider() { return Brain.provider(MEMORY_TYPES, SENSOR_TYPES); }
/*     */ 
/*     */   
/*     */   protected static Brain<?> makeBrain(Brain<Camel> brain) {
/*  77 */     initCoreActivity(brain);
/*  78 */     initIdleActivity(brain);
/*     */     
/*  80 */     brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
/*  81 */     brain.setDefaultActivity(Activity.IDLE);
/*  82 */     brain.useDefaultActivity();
/*  83 */     return brain;
/*     */   }
/*     */ 
/*     */   
/*  87 */   private static void initCoreActivity(Brain<Camel> brain) { brain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new CamelPanic(4.0F), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.GAZE_COOLDOWN_TICKS))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Camel> brain) {
/*  98 */     brain.addActivity(Activity.IDLE, ImmutableList.of(
/*  99 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/* 100 */           Pair.of(Integer.valueOf(1), new AnimalMakeLove(EntityType.CAMEL)), 
/* 101 */           Pair.of(Integer.valueOf(2), new RunOne(ImmutableList.of(
/* 102 */                 Pair.of(new FollowTemptation(camel -> Float.valueOf(2.5F), camel -> Double.valueOf(camel.isBaby() ? 2.5D : 3.5D)), Integer.valueOf(1)), 
/* 103 */                 Pair.of(BehaviorBuilder.triggerIf(Predicate.not(Camel::refuseToMove), BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 2.5F)), Integer.valueOf(1))))), 
/*     */           
/* 105 */           Pair.of(Integer.valueOf(3), new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F)), 
/* 106 */           Pair.of(Integer.valueOf(4), new RunOne(
/* 107 */               ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*     */ 
/*     */               
/* 110 */               ImmutableList.of(
/* 111 */                 Pair.of(BehaviorBuilder.triggerIf(Predicate.not(Camel::refuseToMove), RandomStroll.stroll(2.0F)), Integer.valueOf(1)), 
/* 112 */                 Pair.of(BehaviorBuilder.triggerIf(Predicate.not(Camel::refuseToMove), SetWalkTargetFromLookTarget.create(2.0F, 3)), Integer.valueOf(1)), 
/* 113 */                 Pair.of(new RandomSitting(20), Integer.valueOf(1)), 
/* 114 */                 Pair.of(new DoNothing(30, 60), Integer.valueOf(1)))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public static void updateActivity(Camel body) { body.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class CamelPanic
/*     */     extends AnimalPanic<Camel>
/*     */   {
/* 128 */     public CamelPanic(float speedMultiplier) { super(speedMultiplier); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 133 */     protected boolean checkExtraStartConditions(ServerLevel level, Camel body) { return (super.checkExtraStartConditions(level, body) && !body.isMobControlled()); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void start(ServerLevel level, Camel camel, long timestamp) {
/* 138 */       camel.standUpInstantly();
/* 139 */       super.start(level, camel, timestamp);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RandomSitting extends Behavior<Camel> {
/*     */     private final int minimalPoseTicks;
/*     */     
/*     */     public RandomSitting(int minimalPoseTimeSec) {
/* 147 */       super(ImmutableMap.of());
/* 148 */       this.minimalPoseTicks = minimalPoseTimeSec * 20;
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean checkExtraStartConditions(ServerLevel level, Camel body) {
/* 153 */       return (!body.isInWater() && body
/* 154 */         .getPoseTime() >= this.minimalPoseTicks && 
/* 155 */         !body.isLeashed() && body
/* 156 */         .onGround() && 
/* 157 */         !body.hasControllingPassenger() && body
/* 158 */         .canCamelChangePose());
/*     */     }
/*     */ 
/*     */     
/*     */     protected void start(ServerLevel level, Camel body, long timestamp) {
/* 163 */       if (body.isCamelSitting()) {
/* 164 */         body.standUp();
/* 165 */       } else if (!body.isPanicking()) {
/* 166 */         body.sitDown();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\camel\CamelAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */