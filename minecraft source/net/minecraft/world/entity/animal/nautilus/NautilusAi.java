/*     */ package net.minecraft.world.entity.animal.nautilus;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.ChargeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*     */ import net.minecraft.world.entity.ai.behavior.GateBehavior;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NautilusAi
/*     */ {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 1.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.3F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 0.4F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 1.6F;
/*  49 */   private static final UniformInt TIME_BETWEEN_NON_PLAYER_ATTACKS = UniformInt.of(2400, 3600);
/*     */   
/*     */   private static final float SPEED_WHEN_ATTACKING = 0.6F;
/*     */   private static final float ATTACK_KNOCKBACK_FORCE = 2.0F;
/*     */   private static final int ANGER_DURATION = 400;
/*     */   private static final int TIME_BETWEEN_ATTACKS = 80;
/*     */   private static final double MAX_CHARGE_DISTANCE = 12.0D;
/*     */   private static final double MAX_TARGET_DETECTION_DISTANCE = 11.0D;
/*  57 */   protected static final TargetingConditions ATTACK_TARGET_CONDITIONS = TargetingConditions.forCombat().selector((target, level) -> 
/*  58 */       ((((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() || !target.getType().equals(EntityType.ARMOR_STAND)) && level
/*  59 */       .getWorldBorder().isWithinBounds(target.getBoundingBox())));
/*     */   
/*  61 */   protected static final ImmutableList<SensorType<? extends Sensor<? super Nautilus>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_ADULT, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.NAUTILUS_TEMPTATIONS);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.BREED_TARGET, MemoryModuleType.IS_PANICKING, MemoryModuleType.ATTACK_TARGET, new MemoryModuleType[] { MemoryModuleType.CHARGE_COOLDOWN_TICKS, MemoryModuleType.HURT_BY, MemoryModuleType.ANGRY_AT, MemoryModuleType.ATTACK_TARGET_COOLDOWN });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   protected static void initMemories(AbstractNautilus body, RandomSource random) { body.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET_COOLDOWN, Integer.valueOf(TIME_BETWEEN_NON_PLAYER_ATTACKS.sample(random))); }
/*     */ 
/*     */ 
/*     */   
/*  93 */   protected static Brain.Provider<Nautilus> brainProvider() { return Brain.provider(MEMORY_TYPES, SENSOR_TYPES); }
/*     */ 
/*     */   
/*     */   protected static Brain<?> makeBrain(Brain<Nautilus> brain) {
/*  97 */     initCoreActivity(brain);
/*  98 */     initIdleActivity(brain);
/*  99 */     initFightActivity(brain);
/*     */     
/* 101 */     brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
/* 102 */     brain.setDefaultActivity(Activity.IDLE);
/* 103 */     brain.useDefaultActivity();
/* 104 */     return brain;
/*     */   }
/*     */ 
/*     */   
/* 108 */   private static void initCoreActivity(Brain<Nautilus> brain) { brain.addActivity(Activity.CORE, 0, ImmutableList.of(new AnimalPanic(1.6F), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.CHARGE_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.ATTACK_TARGET_COOLDOWN))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Nautilus> brain) {
/* 119 */     brain.addActivity(Activity.IDLE, ImmutableList.of(
/* 120 */           Pair.of(Integer.valueOf(1), new AnimalMakeLove(EntityType.NAUTILUS, 0.4F, 2)), 
/* 121 */           Pair.of(Integer.valueOf(2), new FollowTemptation(mob -> Float.valueOf(1.3F), mob -> Double.valueOf(mob.isBaby() ? 2.5D : 3.5D))), 
/* 122 */           Pair.of(Integer.valueOf(3), StartAttacking.create(NautilusAi::findNearestValidAttackTarget)), 
/* 123 */           Pair.of(Integer.valueOf(4), new GateBehavior(
/* 124 */               ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*     */ 
/*     */               
/* 127 */               ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.TRY_ALL, 
/*     */ 
/*     */               
/* 130 */               ImmutableList.of(
/* 131 */                 Pair.of(RandomStroll.swim(1.0F), Integer.valueOf(2)), 
/* 132 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(3)))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initFightActivity(Brain<Nautilus> brain) {
/* 140 */     brain.addActivityWithConditions(Activity.FIGHT, ImmutableList.of(
/* 141 */           Pair.of(Integer.valueOf(0), new ChargeAttack(80, ATTACK_TARGET_CONDITIONS, 0.6F, 2.0F, 12.0D, 11.0D, SoundEvents.NAUTILUS_DASH))), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 149 */         ImmutableSet.of(
/* 150 */           Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 
/* 151 */           Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT), 
/* 152 */           Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 153 */           Pair.of(MemoryModuleType.CHARGE_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel level, AbstractNautilus body) {
/* 158 */     if (BehaviorUtils.isBreeding(body) || !body.isInWater() || body.isBaby() || body.isTame()) {
/* 159 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 163 */     Optional<LivingEntity> angryAt = BehaviorUtils.getLivingEntityFromUUIDMemory(body, MemoryModuleType.ANGRY_AT).filter(entity -> (entity.isInWater() && Sensor.isEntityAttackableIgnoringLineOfSight(level, body, entity)));
/* 164 */     if (angryAt.isPresent()) {
/* 165 */       return angryAt;
/*     */     }
/*     */     
/* 168 */     if (body.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET_COOLDOWN)) {
/* 169 */       return Optional.empty();
/*     */     }
/*     */     
/* 172 */     body.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET_COOLDOWN, Integer.valueOf(TIME_BETWEEN_NON_PLAYER_ATTACKS.sample(level.random)));
/*     */     
/* 174 */     if (level.random.nextFloat() < 0.5F) {
/* 175 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 180 */     return ((NearestVisibleLivingEntities)body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty())).findClosest(NautilusAi::isHostileTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void setAngerTarget(ServerLevel level, AbstractNautilus body, LivingEntity target) {
/* 186 */     if (Sensor.isEntityAttackableIgnoringLineOfSight(level, body, target)) {
/* 187 */       body.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 188 */       body.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, target.getUUID(), 400L);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 193 */   private static boolean isHostileTarget(LivingEntity mob) { return (mob.isInWater() && mob.getType().is(EntityTypeTags.NAUTILUS_HOSTILES)); }
/*     */ 
/*     */ 
/*     */   
/* 197 */   public static void updateActivity(Nautilus body) { body.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE)); }
/*     */ 
/*     */ 
/*     */   
/* 201 */   public static Predicate<ItemStack> getTemptations() { return i -> i.is(ItemTags.NAUTILUS_FOOD); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\nautilus\NautilusAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */