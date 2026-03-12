/*     */ package net.minecraft.world.entity.animal.frog;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.Croak;
/*     */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*     */ import net.minecraft.world.entity.ai.behavior.GateBehavior;
/*     */ import net.minecraft.world.entity.ai.behavior.LongJumpMidJump;
/*     */ import net.minecraft.world.entity.ai.behavior.LongJumpToPreferredBlock;
/*     */ import net.minecraft.world.entity.ai.behavior.LongJumpToRandomPos;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.TryFindLand;
/*     */ import net.minecraft.world.entity.ai.behavior.TryFindLandNearWater;
/*     */ import net.minecraft.world.entity.ai.behavior.TryLaySpawnOnWaterNearLand;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.PathfindingContext;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ 
/*     */ public class FrogAi
/*     */ {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
/*  58 */   private static final UniformInt TIME_BETWEEN_LONG_JUMPS = UniformInt.of(100, 140); private static final float SPEED_MULTIPLIER_ON_LAND = 1.0F;
/*     */   private static final float SPEED_MULTIPLIER_IN_WATER = 0.75F;
/*     */   private static final int MAX_LONG_JUMP_HEIGHT = 2;
/*     */   private static final int MAX_LONG_JUMP_WIDTH = 4;
/*     */   private static final float MAX_JUMP_VELOCITY_MULTIPLIER = 3.5714288F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;
/*     */   
/*  65 */   protected static void initMemories(Frog body, RandomSource random) { body.getBrain().setMemory(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, Integer.valueOf(TIME_BETWEEN_LONG_JUMPS.sample(random))); }
/*     */ 
/*     */   
/*     */   protected static Brain<?> makeBrain(Brain<Frog> brain) {
/*  69 */     initCoreActivity(brain);
/*  70 */     initIdleActivity(brain);
/*  71 */     initSwimActivity(brain);
/*  72 */     initLaySpawnActivity(brain);
/*  73 */     initTongueActivity(brain);
/*  74 */     initJumpActivity(brain);
/*     */     
/*  76 */     brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
/*  77 */     brain.setDefaultActivity(Activity.IDLE);
/*  78 */     brain.useDefaultActivity();
/*  79 */     return brain;
/*     */   }
/*     */ 
/*     */   
/*  83 */   private static void initCoreActivity(Brain<Frog> brain) { brain.addActivity(Activity.CORE, 0, ImmutableList.of(new AnimalPanic(2.0F), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Frog> brain) {
/*  93 */     brain.addActivityWithConditions(Activity.IDLE, ImmutableList.of(
/*  94 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/*  95 */           Pair.of(Integer.valueOf(0), new AnimalMakeLove(EntityType.FROG)), 
/*  96 */           Pair.of(Integer.valueOf(1), new FollowTemptation(s -> Float.valueOf(1.25F))), 
/*  97 */           Pair.of(Integer.valueOf(2), StartAttacking.create((level, body) -> canAttack(body), (level, body) -> body.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE))), 
/*  98 */           Pair.of(Integer.valueOf(3), TryFindLand.create(6, 1.0F)), 
/*  99 */           Pair.of(Integer.valueOf(4), new RunOne(
/* 100 */               ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*     */ 
/*     */               
/* 103 */               ImmutableList.of(
/* 104 */                 Pair.of(RandomStroll.stroll(1.0F), Integer.valueOf(1)), 
/* 105 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(1)), 
/* 106 */                 Pair.of(new Croak(), Integer.valueOf(3)), 
/* 107 */                 Pair.of(BehaviorBuilder.triggerIf(Entity::onGround), Integer.valueOf(2)))))), 
/*     */ 
/*     */         
/* 110 */         ImmutableSet.of(
/* 111 */           Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_ABSENT), 
/* 112 */           Pair.of(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initSwimActivity(Brain<Frog> brain) {
/* 117 */     brain.addActivityWithConditions(Activity.SWIM, ImmutableList.of(
/* 118 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/* 119 */           Pair.of(Integer.valueOf(1), new FollowTemptation(s -> Float.valueOf(1.25F))), 
/* 120 */           Pair.of(Integer.valueOf(2), StartAttacking.create((level, body) -> canAttack(body), (level, body) -> body.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE))), 
/* 121 */           Pair.of(Integer.valueOf(3), TryFindLand.create(8, 1.5F)), 
/* 122 */           Pair.of(Integer.valueOf(5), new GateBehavior(
/* 123 */               ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*     */ 
/*     */               
/* 126 */               ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.TRY_ALL, 
/*     */ 
/*     */               
/* 129 */               ImmutableList.of(
/* 130 */                 Pair.of(RandomStroll.swim(0.75F), Integer.valueOf(1)), 
/* 131 */                 Pair.of(RandomStroll.stroll(1.0F, true), Integer.valueOf(1)), 
/* 132 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(1)), 
/* 133 */                 Pair.of(BehaviorBuilder.triggerIf(Entity::isInWater), Integer.valueOf(5)))))), 
/*     */ 
/*     */         
/* 136 */         ImmutableSet.of(
/* 137 */           Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_ABSENT), 
/* 138 */           Pair.of(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_PRESENT)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initLaySpawnActivity(Brain<Frog> brain) {
/* 143 */     brain.addActivityWithConditions(Activity.LAY_SPAWN, ImmutableList.of(
/* 144 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/* 145 */           Pair.of(Integer.valueOf(1), StartAttacking.create((level, body) -> canAttack(body), (level, body) -> body.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE))), 
/* 146 */           Pair.of(Integer.valueOf(2), TryFindLandNearWater.create(8, 1.0F)), 
/* 147 */           Pair.of(Integer.valueOf(3), TryLaySpawnOnWaterNearLand.create(Blocks.FROGSPAWN)), 
/* 148 */           Pair.of(Integer.valueOf(4), new RunOne(
/* 149 */               ImmutableList.of(
/* 150 */                 Pair.of(RandomStroll.stroll(1.0F), Integer.valueOf(2)), 
/* 151 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(1)), 
/* 152 */                 Pair.of(new Croak(), Integer.valueOf(2)), 
/* 153 */                 Pair.of(BehaviorBuilder.triggerIf(Entity::onGround), Integer.valueOf(1)))))), 
/*     */ 
/*     */         
/* 156 */         ImmutableSet.of(
/* 157 */           Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_ABSENT), 
/* 158 */           Pair.of(MemoryModuleType.IS_PREGNANT, MemoryStatus.VALUE_PRESENT)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initJumpActivity(Brain<Frog> brain) {
/* 163 */     brain.addActivityWithConditions(Activity.LONG_JUMP, ImmutableList.of(
/* 164 */           Pair.of(Integer.valueOf(0), new LongJumpMidJump(TIME_BETWEEN_LONG_JUMPS, SoundEvents.FROG_STEP)), 
/* 165 */           Pair.of(Integer.valueOf(1), new LongJumpToPreferredBlock(TIME_BETWEEN_LONG_JUMPS, 2, 4, 3.5714288F, frog -> 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 170 */               SoundEvents.FROG_LONG_JUMP, BlockTags.FROG_PREFER_JUMP_TO, 0.5F, FrogAi::isAcceptableLandingSpot))), 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 175 */         ImmutableSet.of(
/* 176 */           Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT), 
/* 177 */           Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 178 */           Pair.of(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT), 
/* 179 */           Pair.of(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 184 */   private static void initTongueActivity(Brain<Frog> brain) { brain.addActivityAndRemoveMemoryWhenStopped(Activity.TONGUE, 0, ImmutableList.of(
/* 185 */           StopAttackingIfTargetInvalid.create(), new ShootTongue(SoundEvents.FROG_TONGUE, SoundEvents.FROG_EAT)), MemoryModuleType.ATTACK_TARGET); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E extends net.minecraft.world.entity.Mob> boolean isAcceptableLandingSpot(E body, BlockPos targetPos) {
/* 191 */     Level level = body.level();
/* 192 */     BlockPos below = targetPos.below();
/* 193 */     if (!level.getFluidState(targetPos).isEmpty() || 
/* 194 */       !level.getFluidState(below).isEmpty() || 
/* 195 */       !level.getFluidState(targetPos.above()).isEmpty()) {
/* 196 */       return false;
/*     */     }
/* 198 */     BlockState bs = level.getBlockState(targetPos);
/* 199 */     BlockState bsBelow = level.getBlockState(below);
/* 200 */     if (bs.is(BlockTags.FROG_PREFER_JUMP_TO) || bsBelow.is(BlockTags.FROG_PREFER_JUMP_TO)) {
/* 201 */       return true;
/*     */     }
/* 203 */     PathfindingContext context = new PathfindingContext(body.level(), body);
/* 204 */     PathType pathType = WalkNodeEvaluator.getPathTypeStatic(context, targetPos.mutable());
/* 205 */     PathType pathTypeBelow = WalkNodeEvaluator.getPathTypeStatic(context, below.mutable());
/* 206 */     if (pathType == PathType.TRAPDOOR || (bs.isAir() && pathTypeBelow == PathType.TRAPDOOR)) {
/* 207 */       return true;
/*     */     }
/* 209 */     return LongJumpToRandomPos.defaultAcceptableLandingSpot(body, targetPos);
/*     */   }
/*     */ 
/*     */   
/* 213 */   private static boolean canAttack(Frog e) { return !BehaviorUtils.isBreeding(e); }
/*     */ 
/*     */ 
/*     */   
/* 217 */   public static void updateActivity(Frog body) { body.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.TONGUE, Activity.LAY_SPAWN, Activity.LONG_JUMP, Activity.SWIM, Activity.IDLE)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 227 */   public static Predicate<ItemStack> getTemptations() { return i -> i.is(ItemTags.FROG_FOOD); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\frog\FrogAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */