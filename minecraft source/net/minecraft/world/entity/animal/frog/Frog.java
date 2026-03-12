/*     */ package net.minecraft.world.entity.animal.frog;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.monster.Slime;
/*     */ import net.minecraft.world.entity.variant.SpawnContext;
/*     */ import net.minecraft.world.entity.variant.VariantUtils;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.PathfindingContext;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class Frog
/*     */   extends Animal
/*     */ {
/*  75 */   protected static final ImmutableList<SensorType<? extends Sensor<? super Frog>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.FROG_ATTACKABLES, SensorType.FROG_TEMPTATIONS, SensorType.IS_IN_WATER);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.BREED_TARGET, MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, new MemoryModuleType[] { MemoryModuleType.IS_TEMPTED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.IS_IN_WATER, MemoryModuleType.IS_PREGNANT, MemoryModuleType.IS_PANICKING, MemoryModuleType.UNREACHABLE_TONGUE_TARGETS });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   private static final EntityDataAccessor<Holder<FrogVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(Frog.class, EntityDataSerializers.FROG_VARIANT);
/* 107 */   private static final EntityDataAccessor<OptionalInt> DATA_TONGUE_TARGET_ID = SynchedEntityData.defineId(Frog.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
/*     */   
/*     */   private static final int FROG_FALL_DAMAGE_REDUCTION = 5;
/* 110 */   private static final ResourceKey<FrogVariant> DEFAULT_VARIANT = FrogVariants.TEMPERATE;
/*     */   
/* 112 */   public final AnimationState jumpAnimationState = new AnimationState();
/* 113 */   public final AnimationState croakAnimationState = new AnimationState();
/* 114 */   public final AnimationState tongueAnimationState = new AnimationState();
/* 115 */   public final AnimationState swimIdleAnimationState = new AnimationState();
/*     */   
/*     */   public Frog(EntityType<? extends Animal> type, Level level) {
/* 118 */     super(type, level);
/* 119 */     this.lookControl = new FrogLookControl(this);
/*     */     
/* 121 */     setPathfindingMalus(PathType.WATER, 4.0F);
/* 122 */     setPathfindingMalus(PathType.TRAPDOOR, -1.0F);
/* 123 */     this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 128 */   protected Brain.Provider<Frog> brainProvider() { return Brain.provider(MEMORY_TYPES, SENSOR_TYPES); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   protected Brain<?> makeBrain(Dynamic<?> input) { return FrogAi.makeBrain(brainProvider().makeBrain(input)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   public Brain<Frog> getBrain() { return super.getBrain(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 144 */     super.defineSynchedData(entityData);
/* 145 */     Registry<FrogVariant> variants = registryAccess().lookupOrThrow(Registries.FROG_VARIANT);
/* 146 */     entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(registryAccess(), DEFAULT_VARIANT));
/* 147 */     entityData.define(DATA_TONGUE_TARGET_ID, OptionalInt.empty());
/*     */   }
/*     */ 
/*     */   
/* 151 */   public void eraseTongueTarget() { this.entityData.set(DATA_TONGUE_TARGET_ID, OptionalInt.empty()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<Entity> getTongueTarget() {
/* 156 */     Objects.requireNonNull(level()); return ((OptionalInt)this.entityData.get(DATA_TONGUE_TARGET_ID)).stream().mapToObj(level()::getEntity)
/* 157 */       .filter(Objects::nonNull)
/* 158 */       .findFirst();
/*     */   }
/*     */ 
/*     */   
/* 162 */   public void setTongueTarget(Entity target) { this.entityData.set(DATA_TONGUE_TARGET_ID, OptionalInt.of(target.getId())); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   public int getHeadRotSpeed() { return 35; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 172 */   public int getMaxHeadYRot() { return 5; }
/*     */ 
/*     */ 
/*     */   
/* 176 */   public Holder<FrogVariant> getVariant() { return (Holder)this.entityData.get(DATA_VARIANT_ID); }
/*     */ 
/*     */ 
/*     */   
/* 180 */   private void setVariant(Holder<FrogVariant> variant) { this.entityData.set(DATA_VARIANT_ID, variant); }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 185 */     if (type == DataComponents.FROG_VARIANT) {
/* 186 */       return (T)castComponentValue(type, getVariant());
/*     */     }
/*     */     
/* 189 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 194 */     applyImplicitComponentIfPresent(components, DataComponents.FROG_VARIANT);
/* 195 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 200 */     if (type == DataComponents.FROG_VARIANT) {
/* 201 */       setVariant((Holder)castComponentValue(DataComponents.FROG_VARIANT, value));
/* 202 */       return true;
/*     */     } 
/*     */     
/* 205 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 210 */     super.addAdditionalSaveData(output);
/* 211 */     VariantUtils.writeVariant(output, getVariant());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 216 */     super.readAdditionalSaveData(input);
/* 217 */     VariantUtils.readVariant(input, Registries.FROG_VARIANT).ifPresent(this::setVariant);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 222 */     ProfilerFiller profiler = Profiler.get();
/* 223 */     profiler.push("frogBrain");
/* 224 */     getBrain().tick(level, this);
/* 225 */     profiler.pop();
/*     */     
/* 227 */     profiler.push("frogActivityUpdate");
/* 228 */     FrogAi.updateActivity(this);
/* 229 */     profiler.pop();
/*     */     
/* 231 */     super.customServerAiStep(level);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 236 */     if (level().isClientSide()) {
/* 237 */       this.swimIdleAnimationState.animateWhen((isInWater() && !this.walkAnimation.isMoving()), this.tickCount);
/*     */     }
/*     */     
/* 240 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 245 */     if (DATA_POSE.equals(accessor)) {
/* 246 */       Pose pose = getPose();
/*     */       
/* 248 */       if (pose == Pose.LONG_JUMPING) {
/* 249 */         this.jumpAnimationState.start(this.tickCount);
/*     */       } else {
/* 251 */         this.jumpAnimationState.stop();
/*     */       } 
/*     */       
/* 254 */       if (pose == Pose.CROAKING) {
/* 255 */         this.croakAnimationState.start(this.tickCount);
/*     */       } else {
/* 257 */         this.croakAnimationState.stop();
/*     */       } 
/*     */       
/* 260 */       if (pose == Pose.USING_TONGUE) {
/* 261 */         this.tongueAnimationState.start(this.tickCount);
/*     */       } else {
/* 263 */         this.tongueAnimationState.stop();
/*     */       } 
/*     */     } 
/* 266 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateWalkAnimation(float distance) {
/*     */     float targetSpeed;
/* 272 */     if (this.jumpAnimationState.isStarted()) {
/* 273 */       targetSpeed = 0.0F;
/*     */     } else {
/* 275 */       targetSpeed = Math.min(distance * 25.0F, 1.0F);
/*     */     } 
/* 277 */     this.walkAnimation.update(targetSpeed, 0.4F, isBaby() ? 3.0F : 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 282 */   public void playEatingSound() { level().playSound(null, this, SoundEvents.FROG_EAT, SoundSource.NEUTRAL, 2.0F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 287 */     Frog frog = (Frog)EntityType.FROG.create(level, EntitySpawnReason.BREEDING);
/* 288 */     if (frog != null) {
/* 289 */       FrogAi.initMemories(frog, level.getRandom());
/*     */     }
/*     */     
/* 292 */     return frog;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 298 */   public boolean isBaby() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBaby(boolean baby) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void spawnChildFromBreeding(ServerLevel level, Animal partner) {
/* 309 */     finalizeSpawnChildFromBreeding(level, partner, null);
/*     */     
/* 311 */     getBrain().setMemory(MemoryModuleType.IS_PREGNANT, Unit.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 316 */     VariantUtils.selectVariantToSpawn(SpawnContext.create(level, blockPosition()), Registries.FROG_VARIANT).ifPresent(this::setVariant);
/*     */     
/* 318 */     FrogAi.initMemories(this, level.getRandom());
/*     */     
/* 320 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   private class FrogLookControl
/*     */     extends LookControl {
/* 325 */     FrogLookControl(Mob mob) { super(mob); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 330 */     protected boolean resetXRotOnTick() { return Frog.this.getTongueTarget().isEmpty(); }
/*     */   }
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 335 */     return Animal.createAnimalAttributes()
/* 336 */       .add(Attributes.MOVEMENT_SPEED, 1.0D)
/* 337 */       .add(Attributes.MAX_HEALTH, 10.0D)
/* 338 */       .add(Attributes.ATTACK_DAMAGE, 10.0D)
/* 339 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 344 */   protected SoundEvent getAmbientSound() { return SoundEvents.FROG_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 349 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.FROG_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 354 */   protected SoundEvent getDeathSound() { return SoundEvents.FROG_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 359 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.FROG_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 364 */   public boolean isPushedByFluid() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 369 */   protected int calculateFallDamage(double fallDistance, float damageModifier) { return super.calculateFallDamage(fallDistance, damageModifier) - 5; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 374 */     moveRelative(getSpeed(), input);
/* 375 */     move(MoverType.SELF, getDeltaMovement());
/* 376 */     setDeltaMovement(getDeltaMovement().scale(0.9D));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean canEat(LivingEntity entity) {
/* 381 */     if (entity instanceof Slime) { Slime slime = (Slime)entity; if (slime.getSize() != 1)
/* 382 */         return false;  }
/*     */     
/* 384 */     return entity.getType().is(EntityTypeTags.FROG_FOOD);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 389 */   protected PathNavigation createNavigation(Level level) { return new FrogPathNavigation(this, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 394 */   public LivingEntity getTarget() { return getTargetFromBrain(); }
/*     */   
/*     */   private static class FrogPathNavigation
/*     */     extends AmphibiousPathNavigation
/*     */   {
/* 399 */     FrogPathNavigation(Frog mob, Level level) { super(mob, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 404 */     public boolean canCutCorner(PathType pathType) { return (pathType != PathType.WATER_BORDER && super.canCutCorner(pathType)); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected PathFinder createPathFinder(int maxVisitedNodes) {
/* 409 */       this.nodeEvaluator = new Frog.FrogNodeEvaluator(true);
/* 410 */       return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FrogNodeEvaluator extends AmphibiousNodeEvaluator {
/* 415 */     private final BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();
/*     */ 
/*     */     
/* 418 */     public FrogNodeEvaluator(boolean prefersShallowSwimming) { super(prefersShallowSwimming); }
/*     */ 
/*     */ 
/*     */     
/*     */     public Node getStart() {
/* 423 */       if (!this.mob.isInWater()) {
/* 424 */         return super.getStart();
/*     */       }
/* 426 */       return getStartNode(new BlockPos(Mth.floor((this.mob.getBoundingBox()).minX), Mth.floor((this.mob.getBoundingBox()).minY), Mth.floor((this.mob.getBoundingBox()).minZ)));
/*     */     }
/*     */ 
/*     */     
/*     */     public PathType getPathType(PathfindingContext context, int x, int y, int z) {
/* 431 */       this.belowPos.set(x, y - 1, z);
/*     */       
/* 433 */       BlockState belowState = context.getBlockState(this.belowPos);
/* 434 */       if (belowState.is(BlockTags.FROG_PREFER_JUMP_TO)) {
/* 435 */         return PathType.OPEN;
/*     */       }
/*     */       
/* 438 */       return super.getPathType(context, x, y, z);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 444 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.FROG_FOOD); }
/*     */ 
/*     */   
/*     */   public static boolean checkFrogSpawnRules(EntityType<? extends Animal> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 448 */     return (level.getBlockState(pos.below()).is(BlockTags.FROGS_SPAWNABLE_ON) && 
/* 449 */       isBrightEnoughToSpawn(level, pos));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\frog\Frog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */