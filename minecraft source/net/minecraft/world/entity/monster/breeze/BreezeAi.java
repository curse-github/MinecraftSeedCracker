/*     */ package net.minecraft.world.entity.monster.breeze;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ 
/*     */ public class BreezeAi
/*     */ {
/*     */   public static final float SPEED_MULTIPLIER_WHEN_SLIDING = 0.6F;
/*     */   public static final float JUMP_CIRCLE_INNER_RADIUS = 4.0F;
/*     */   public static final float JUMP_CIRCLE_MIDDLE_RADIUS = 8.0F;
/*     */   public static final float JUMP_CIRCLE_OUTER_RADIUS = 24.0F;
/*  39 */   static final List<SensorType<? extends Sensor<? super Breeze>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.NEAREST_PLAYERS, SensorType.BREEZE_ATTACK_ENTITY_SENSOR);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   static final List<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.BREEZE_JUMP_COOLDOWN, MemoryModuleType.BREEZE_JUMP_INHALING, MemoryModuleType.BREEZE_SHOOT, MemoryModuleType.BREEZE_SHOOT_CHARGING, MemoryModuleType.BREEZE_SHOOT_RECOVERING, MemoryModuleType.BREEZE_SHOOT_COOLDOWN, new MemoryModuleType[] { MemoryModuleType.BREEZE_JUMP_TARGET, MemoryModuleType.BREEZE_LEAVING_WATER, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.PATH });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int TICKS_TO_REMEMBER_SEEN_TARGET = 100;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Brain<?> makeBrain(Breeze breeze, Brain<Breeze> brain) {
/*  68 */     initCoreActivity(brain);
/*  69 */     initIdleActivity(brain);
/*  70 */     initFightActivity(breeze, brain);
/*     */     
/*  72 */     brain.setCoreActivities(Set.of(Activity.CORE));
/*  73 */     brain.setDefaultActivity(Activity.FIGHT);
/*  74 */     brain.useDefaultActivity();
/*  75 */     return brain;
/*     */   }
/*     */ 
/*     */   
/*  79 */   private static void initCoreActivity(Brain<Breeze> brain) { brain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new LookAtTargetSink(45, 90))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Breeze> brain) {
/*  86 */     brain.addActivity(Activity.IDLE, ImmutableList.of(
/*  87 */           Pair.of(Integer.valueOf(0), StartAttacking.create((level, breeze) -> breeze.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE))), 
/*  88 */           Pair.of(Integer.valueOf(1), StartAttacking.create((level, breeze) -> breeze.getHurtBy())), 
/*  89 */           Pair.of(Integer.valueOf(2), new SlideToTargetSink(20, 40)), 
/*  90 */           Pair.of(Integer.valueOf(3), new RunOne(ImmutableList.of(
/*  91 */                 Pair.of(new DoNothing(20, 100), Integer.valueOf(1)), 
/*  92 */                 Pair.of(RandomStroll.stroll(0.6F), Integer.valueOf(2)))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initFightActivity(Breeze body, Brain<Breeze> brain) {
/* 100 */     Objects.requireNonNull(Sensor.wasEntityAttackableLastNTicks(body, 100).negate()); brain.addActivityWithConditions(Activity.FIGHT, ImmutableList.of(Pair.of(Integer.valueOf(0), StopAttackingIfTargetInvalid.create(Sensor.wasEntityAttackableLastNTicks(body, 100).negate()::test)), 
/* 101 */           Pair.of(Integer.valueOf(1), new Shoot()), 
/* 102 */           Pair.of(Integer.valueOf(2), new LongJump()), 
/* 103 */           Pair.of(Integer.valueOf(3), new ShootWhenStuck()), 
/* 104 */           Pair.of(Integer.valueOf(4), new Slide())), 
/* 105 */         ImmutableSet.of(
/* 106 */           Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 
/* 107 */           Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   static void updateActivity(Breeze body) { body.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SlideToTargetSink
/*     */     extends MoveToTargetSink
/*     */   {
/*     */     @VisibleForTesting
/* 123 */     public SlideToTargetSink(int minTimeout, int maxTimeout) { super(minTimeout, maxTimeout); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void start(ServerLevel level, Mob body, long timestamp) {
/* 128 */       super.start(level, body, timestamp);
/* 129 */       body.playSound(SoundEvents.BREEZE_SLIDE);
/* 130 */       body.setPose(Pose.SLIDING);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void stop(ServerLevel level, Mob body, long timestamp) {
/* 135 */       super.stop(level, body, timestamp);
/* 136 */       body.setPose(Pose.STANDING);
/*     */ 
/*     */       
/* 139 */       if (body.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET))
/* 140 */         body.getBrain().setMemoryWithExpiry(MemoryModuleType.BREEZE_SHOOT, Unit.INSTANCE, 60L); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\breeze\BreezeAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */