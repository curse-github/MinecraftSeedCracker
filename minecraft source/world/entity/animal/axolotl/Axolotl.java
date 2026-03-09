/*     */ package net.minecraft.world.entity.animal.axolotl;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.BinaryAnimator;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.EasingType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.Bucketable;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUtils;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.CustomData;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class Axolotl
/*     */   extends Animal
/*     */   implements Bucketable
/*     */ {
/*     */   public static final int TOTAL_PLAYDEAD_TIME = 200;
/*     */   private static final int POSE_ANIMATION_TICKS = 10;
/*  80 */   protected static final ImmutableList<? extends SensorType<? extends Sensor<? super Axolotl>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_ADULT, SensorType.HURT_BY, SensorType.AXOLOTL_ATTACKABLES, SensorType.FOOD_TEMPTATIONS);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.BREED_TARGET, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_VISIBLE_ADULT, new MemoryModuleType[] { MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.PLAY_DEAD_TICKS, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.HAS_HUNTING_COOLDOWN, MemoryModuleType.IS_PANICKING });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(Axolotl.class, EntityDataSerializers.INT);
/* 112 */   private static final EntityDataAccessor<Boolean> DATA_PLAYING_DEAD = SynchedEntityData.defineId(Axolotl.class, EntityDataSerializers.BOOLEAN);
/* 113 */   private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Axolotl.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   public static final double PLAYER_REGEN_DETECTION_RANGE = 20.0D;
/*     */   
/*     */   public static final int RARE_VARIANT_CHANCE = 1200;
/*     */   
/*     */   private static final int AXOLOTL_TOTAL_AIR_SUPPLY = 6000;
/*     */   
/*     */   public static final String VARIANT_TAG = "Variant";
/*     */   private static final int REHYDRATE_AIR_SUPPLY = 1800;
/*     */   private static final int REGEN_BUFF_MAX_DURATION = 2400;
/*     */   private static final boolean DEFAULT_FROM_BUCKET = false;
/* 125 */   public final BinaryAnimator playingDeadAnimator = new BinaryAnimator(10, EasingType.IN_OUT_SINE);
/* 126 */   public final BinaryAnimator inWaterAnimator = new BinaryAnimator(10, EasingType.IN_OUT_SINE);
/* 127 */   public final BinaryAnimator onGroundAnimator = new BinaryAnimator(10, EasingType.IN_OUT_SINE);
/* 128 */   public final BinaryAnimator movingAnimator = new BinaryAnimator(10, EasingType.IN_OUT_SINE); private static final int REGEN_BUFF_BASE_DURATION = 100;
/*     */   public enum Variant implements StringRepresentable { public static final Variant DEFAULT;
/*     */     private static final IntFunction<Variant> BY_ID;
/* 131 */     LUCY(0, "lucy", true),
/* 132 */     WILD(1, "wild", true),
/* 133 */     GOLD(2, "gold", true),
/* 134 */     CYAN(3, "cyan", true),
/* 135 */     BLUE(4, "blue", false); public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC; public static final Codec<Variant> CODEC;
/*     */     static  {
/* 137 */       DEFAULT = LUCY;
/*     */       
/* 139 */       BY_ID = ByIdMap.continuous(Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */       
/* 141 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::getId);
/*     */       
/* 143 */       CODEC = StringRepresentable.fromEnum(Variant::values);
/*     */       
/* 145 */       Objects.requireNonNull(BY_ID); LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Variant::getId);
/*     */     }
/*     */     @Deprecated
/*     */     public static final Codec<Variant> LEGACY_CODEC; private final int id; private final String name;
/*     */     private final boolean common;
/*     */     
/*     */     Variant(int id, String name, boolean common) {
/* 152 */       this.id = id;
/* 153 */       this.name = name;
/* 154 */       this.common = common;
/*     */     }
/*     */ 
/*     */     
/* 158 */     public int getId() { return this.id; }
/*     */ 
/*     */ 
/*     */     
/* 162 */     public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 167 */     public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */     
/* 171 */     public static Variant byId(int id) { return (Variant)BY_ID.apply(id); }
/*     */ 
/*     */ 
/*     */     
/* 175 */     public static Variant getCommonSpawnVariant(RandomSource random) { return getSpawnVariant(random, true); }
/*     */ 
/*     */ 
/*     */     
/* 179 */     public static Variant getRareSpawnVariant(RandomSource random) { return getSpawnVariant(random, false); }
/*     */ 
/*     */     
/*     */     private static Variant getSpawnVariant(RandomSource random, boolean common) {
/* 183 */       Variant[] validVariants = (Variant[])Arrays.stream(values()).filter(v -> (v.common == common)).toArray(x$0 -> new Variant[x$0]);
/* 184 */       return (Variant)Util.getRandom(validVariants, random);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Axolotl(EntityType<? extends Axolotl> type, Level level) {
/* 191 */     super(type, level);
/*     */     
/* 193 */     setPathfindingMalus(PathType.WATER, 0.0F);
/* 194 */     this.moveControl = new AxolotlMoveControl(this);
/* 195 */     this.lookControl = new AxolotlLookControl(this, 20);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 200 */   public float getWalkTargetValue(BlockPos pos, LevelReader level) { return 0.0F; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 205 */     super.defineSynchedData(entityData);
/* 206 */     entityData.define(DATA_VARIANT, Integer.valueOf(0));
/* 207 */     entityData.define(DATA_PLAYING_DEAD, Boolean.valueOf(false));
/* 208 */     entityData.define(FROM_BUCKET, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 213 */     super.addAdditionalSaveData(output);
/* 214 */     output.store("Variant", Variant.LEGACY_CODEC, getVariant());
/* 215 */     output.putBoolean("FromBucket", fromBucket());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 220 */     super.readAdditionalSaveData(input);
/* 221 */     setVariant((Variant)input.read("Variant", Variant.LEGACY_CODEC).orElse(Variant.DEFAULT));
/* 222 */     setFromBucket(input.getBooleanOr("FromBucket", false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playAmbientSound() {
/* 227 */     if (isPlayingDead()) {
/*     */       return;
/*     */     }
/* 230 */     super.playAmbientSound();
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     AxolotlGroupData axolotlGroupData;
/* 235 */     boolean isBaby = false;
/*     */     
/* 237 */     if (spawnReason == EntitySpawnReason.BUCKET) {
/* 238 */       return groupData;
/*     */     }
/*     */     
/* 241 */     RandomSource random = level.getRandom();
/* 242 */     if (groupData instanceof AxolotlGroupData) {
/* 243 */       if (((AxolotlGroupData)groupData).getGroupSize() >= 2) {
/* 244 */         isBaby = true;
/*     */       }
/*     */     } else {
/*     */       
/* 248 */       axolotlGroupData = new AxolotlGroupData(new Variant[] { null, (new Variant[2][0] = Variant.getCommonSpawnVariant(random)).getCommonSpawnVariant(random) });
/*     */     } 
/*     */     
/* 251 */     setVariant(((AxolotlGroupData)axolotlGroupData).getVariant(random));
/* 252 */     if (isBaby) {
/* 253 */       setAge(-24000);
/*     */     }
/*     */     
/* 256 */     return super.finalizeSpawn(level, difficulty, spawnReason, axolotlGroupData);
/*     */   }
/*     */ 
/*     */   
/*     */   public void baseTick() {
/* 261 */     int airSupply = getAirSupply();
/* 262 */     super.baseTick();
/* 263 */     if (!isNoAi()) { Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 264 */         handleAirSupply(serverLevel, airSupply); }
/*     */        }
/*     */     
/* 267 */     if (level().isClientSide()) {
/* 268 */       tickAnimations();
/*     */     }
/*     */   }
/*     */   
/*     */   private void tickAnimations() {
/*     */     AnimationState animationState;
/* 274 */     if (isPlayingDead()) {
/* 275 */       animationState = AnimationState.PLAYING_DEAD;
/* 276 */     } else if (isInWater()) {
/* 277 */       animationState = AnimationState.IN_WATER;
/* 278 */     } else if (onGround()) {
/* 279 */       animationState = AnimationState.ON_GROUND;
/*     */     } else {
/* 281 */       animationState = AnimationState.IN_AIR;
/*     */     } 
/*     */     
/* 284 */     this.playingDeadAnimator.tick((animationState == AnimationState.PLAYING_DEAD));
/* 285 */     this.inWaterAnimator.tick((animationState == AnimationState.IN_WATER));
/* 286 */     this.onGroundAnimator.tick((animationState == AnimationState.ON_GROUND));
/*     */ 
/*     */ 
/*     */     
/* 290 */     boolean isMoving = (this.walkAnimation.isMoving() || getXRot() != this.xRotO || getYRot() != this.yRotO);
/* 291 */     this.movingAnimator.tick(isMoving);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleAirSupply(ServerLevel level, int preTickAirSupply) {
/* 296 */     if (isAlive() && !isInWaterOrRain()) {
/* 297 */       setAirSupply(preTickAirSupply - 1);
/* 298 */       if (shouldTakeDrowningDamage()) {
/* 299 */         setAirSupply(0);
/* 300 */         hurtServer(level, damageSources().dryOut(), 2.0F);
/*     */       } 
/*     */     } else {
/* 303 */       setAirSupply(getMaxAirSupply());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rehydrate() {
/* 308 */     int newAirSupply = getAirSupply() + 1800;
/* 309 */     setAirSupply(Math.min(newAirSupply, getMaxAirSupply()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 314 */   public int getMaxAirSupply() { return 6000; }
/*     */ 
/*     */ 
/*     */   
/* 318 */   public Variant getVariant() { return Variant.byId(((Integer)this.entityData.get(DATA_VARIANT)).intValue()); }
/*     */ 
/*     */ 
/*     */   
/* 322 */   private void setVariant(Variant variant) { this.entityData.set(DATA_VARIANT, Integer.valueOf(variant.getId())); }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 327 */     if (type == DataComponents.AXOLOTL_VARIANT) {
/* 328 */       return (T)castComponentValue(type, getVariant());
/*     */     }
/*     */     
/* 331 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 336 */     applyImplicitComponentIfPresent(components, DataComponents.AXOLOTL_VARIANT);
/* 337 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 342 */     if (type == DataComponents.AXOLOTL_VARIANT) {
/* 343 */       setVariant((Variant)castComponentValue(DataComponents.AXOLOTL_VARIANT, value));
/* 344 */       return true;
/*     */     } 
/*     */     
/* 347 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ 
/*     */   
/* 351 */   private static boolean useRareVariant(RandomSource random) { return (random.nextInt(1200) == 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 356 */   public boolean checkSpawnObstruction(LevelReader level) { return level.isUnobstructed(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 362 */   public boolean isPushedByFluid() { return false; }
/*     */ 
/*     */ 
/*     */   
/* 366 */   public void setPlayingDead(boolean playingDead) { this.entityData.set(DATA_PLAYING_DEAD, Boolean.valueOf(playingDead)); }
/*     */ 
/*     */ 
/*     */   
/* 370 */   public boolean isPlayingDead() { return ((Boolean)this.entityData.get(DATA_PLAYING_DEAD)).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 375 */   public boolean fromBucket() { return ((Boolean)this.entityData.get(FROM_BUCKET)).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 380 */   public void setFromBucket(boolean fromBucket) { this.entityData.set(FROM_BUCKET, Boolean.valueOf(fromBucket)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 385 */     Axolotl baby = (Axolotl)EntityType.AXOLOTL.create(level, EntitySpawnReason.BREEDING);
/* 386 */     if (baby != null) {
/*     */       Variant variant;
/* 388 */       if (useRareVariant(this.random)) {
/* 389 */         variant = Variant.getRareSpawnVariant(this.random);
/*     */       } else {
/* 391 */         variant = this.random.nextBoolean() ? getVariant() : ((Axolotl)partner).getVariant();
/*     */       } 
/* 393 */       baby.setVariant(variant);
/* 394 */       baby.setPersistenceRequired();
/*     */     } 
/* 396 */     return baby;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 401 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.AXOLOTL_FOOD); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 406 */   public boolean canBeLeashed() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 411 */     ProfilerFiller profiler = Profiler.get();
/* 412 */     profiler.push("axolotlBrain");
/* 413 */     getBrain().tick(level, this);
/* 414 */     profiler.pop();
/*     */     
/* 416 */     profiler.push("axolotlActivityUpdate");
/* 417 */     AxolotlAi.updateActivity(this);
/* 418 */     profiler.pop();
/*     */     
/* 420 */     if (!isNoAi()) {
/* 421 */       Optional<Integer> playDeadTicks = getBrain().getMemory(MemoryModuleType.PLAY_DEAD_TICKS);
/* 422 */       setPlayingDead((playDeadTicks.isPresent() && ((Integer)playDeadTicks.get()).intValue() > 0));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 427 */     return Animal.createAnimalAttributes()
/* 428 */       .add(Attributes.MAX_HEALTH, 14.0D)
/* 429 */       .add(Attributes.MOVEMENT_SPEED, 1.0D)
/* 430 */       .add(Attributes.ATTACK_DAMAGE, 2.0D)
/* 431 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 436 */   protected PathNavigation createNavigation(Level level) { return new AmphibiousPathNavigation(this, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 441 */   public void playAttackSound() { playSound(SoundEvents.AXOLOTL_ATTACK, 1.0F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 446 */     float currentHealth = getHealth();
/* 447 */     if (!isNoAi() && 
/* 448 */       (level()).random.nextInt(3) == 0 && (
/* 449 */       (level()).random.nextInt(3) < damage || currentHealth / getMaxHealth() < 0.5F) && damage < currentHealth && 
/*     */       
/* 451 */       isInWater() && (source
/* 452 */       .getEntity() != null || source.getDirectEntity() != null) && 
/* 453 */       !isPlayingDead()) {
/* 454 */       this.brain.setMemory(MemoryModuleType.PLAY_DEAD_TICKS, Integer.valueOf(200));
/*     */     }
/*     */     
/* 457 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 462 */   public int getMaxHeadXRot() { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 467 */   public int getMaxHeadYRot() { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 472 */   public InteractionResult mobInteract(Player player, InteractionHand hand) { return (InteractionResult)Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveToBucketTag(ItemStack bucket) {
/* 477 */     Bucketable.saveDefaultDataToBucketTag(this, bucket);
/* 478 */     bucket.copyFrom(DataComponents.AXOLOTL_VARIANT, this);
/*     */     
/* 480 */     CustomData.update(DataComponents.BUCKET_ENTITY_DATA, bucket, tag -> {
/* 481 */           tag.putInt("Age", getAge());
/*     */           
/* 483 */           Brain<?> brain = getBrain();
/* 484 */           if (brain.hasMemoryValue(MemoryModuleType.HAS_HUNTING_COOLDOWN)) {
/* 485 */             tag.putLong("HuntingCooldown", brain.getTimeUntilExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN));
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadFromBucketTag(CompoundTag tag) {
/* 492 */     Bucketable.loadDefaultDataFromBucketTag(this, tag);
/*     */     
/* 494 */     setAge(tag.getIntOr("Age", 0));
/* 495 */     tag.getLong("HuntingCooldown").ifPresentOrElse(huntingCooldown -> 
/* 496 */         getBrain().setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, Boolean.valueOf(true), tag.getLongOr("HuntingCooldown", 0L)), () -> 
/* 497 */         getBrain().setMemory(MemoryModuleType.HAS_HUNTING_COOLDOWN, Optional.empty()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 503 */   public ItemStack getBucketItemStack() { return new ItemStack(Items.AXOLOTL_BUCKET); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 508 */   public SoundEvent getPickupSound() { return SoundEvents.BUCKET_FILL_AXOLOTL; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 513 */   public boolean canBeSeenAsEnemy() { return (!isPlayingDead() && super.canBeSeenAsEnemy()); }
/*     */ 
/*     */   
/*     */   public static void onStopAttacking(ServerLevel level, Axolotl body, LivingEntity target) {
/* 517 */     if (target.isDeadOrDying()) {
/* 518 */       DamageSource lastDamageSource = target.getLastDamageSource();
/* 519 */       if (lastDamageSource != null) {
/* 520 */         Entity entity = lastDamageSource.getEntity();
/* 521 */         if (entity != null && entity.getType() == EntityType.PLAYER) {
/* 522 */           Player player = (Player)entity;
/* 523 */           List<Player> playersInRange = level.getEntitiesOfClass(Player.class, body.getBoundingBox().inflate(20.0D));
/*     */ 
/*     */ 
/*     */           
/* 527 */           if (playersInRange.contains(player)) {
/* 528 */             body.applySupportingEffects(player);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void applySupportingEffects(Player player) {
/* 537 */     MobEffectInstance regenEffect = player.getEffect(MobEffects.REGENERATION);
/*     */     
/* 539 */     if (regenEffect == null || regenEffect.endsWithin(2399)) {
/* 540 */       int previousDuration = (regenEffect != null) ? regenEffect.getDuration() : 0;
/* 541 */       int regenDuration = Math.min(2400, 100 + previousDuration);
/* 542 */       player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, regenDuration, 0), this);
/*     */     } 
/*     */ 
/*     */     
/* 546 */     player.removeEffect(MobEffects.MINING_FATIGUE);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 551 */   public boolean requiresCustomPersistence() { return (super.requiresCustomPersistence() || fromBucket()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 556 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.AXOLOTL_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 561 */   protected SoundEvent getDeathSound() { return SoundEvents.AXOLOTL_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 566 */   protected SoundEvent getAmbientSound() { return isInWater() ? SoundEvents.AXOLOTL_IDLE_WATER : SoundEvents.AXOLOTL_IDLE_AIR; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 571 */   protected SoundEvent getSwimSplashSound() { return SoundEvents.AXOLOTL_SPLASH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 576 */   protected SoundEvent getSwimSound() { return SoundEvents.AXOLOTL_SWIM; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 581 */   protected Brain.Provider<Axolotl> brainProvider() { return Brain.provider(MEMORY_TYPES, SENSOR_TYPES); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 586 */   protected Brain<?> makeBrain(Dynamic<?> input) { return AxolotlAi.makeBrain(brainProvider().makeBrain(input)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 592 */   public Brain<Axolotl> getBrain() { return super.getBrain(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 597 */     moveRelative(getSpeed(), input);
/* 598 */     move(MoverType.SELF, getDeltaMovement());
/* 599 */     setDeltaMovement(getDeltaMovement().scale(0.9D));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void usePlayerItem(Player player, InteractionHand hand, ItemStack itemStack) {
/* 605 */     if (itemStack.is(Items.TROPICAL_FISH_BUCKET)) {
/* 606 */       player.setItemInHand(hand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.WATER_BUCKET)));
/*     */     } else {
/* 608 */       super.usePlayerItem(player, hand, itemStack);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 614 */   public boolean removeWhenFarAway(double distSqr) { return (!fromBucket() && !hasCustomName()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 619 */   public LivingEntity getTarget() { return getTargetFromBrain(); }
/*     */ 
/*     */ 
/*     */   
/* 623 */   public static boolean checkAxolotlSpawnRules(EntityType<? extends LivingEntity> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) { return level.getBlockState(pos.below()).is(BlockTags.AXOLOTLS_SPAWNABLE_ON); }
/*     */   
/*     */   private static class AxolotlMoveControl
/*     */     extends SmoothSwimmingMoveControl {
/*     */     private final Axolotl axolotl;
/*     */     
/*     */     public AxolotlMoveControl(Axolotl axolotl) {
/* 630 */       super(axolotl, 85, 10, 0.1F, 0.5F, false);
/* 631 */       this.axolotl = axolotl;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 636 */       if (!this.axolotl.isPlayingDead())
/* 637 */         super.tick(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private class AxolotlLookControl
/*     */     extends SmoothSwimmingLookControl
/*     */   {
/* 644 */     public AxolotlLookControl(Axolotl axolotl, int maxYRotFromCenter) { super(axolotl, maxYRotFromCenter); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 649 */       if (!Axolotl.this.isPlayingDead())
/* 650 */         super.tick(); 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AxolotlGroupData
/*     */     extends AgeableMob.AgeableMobGroupData {
/*     */     public final Axolotl.Variant[] types;
/*     */     
/*     */     public AxolotlGroupData(Variant... types) {
/* 659 */       super(false);
/* 660 */       this.types = types;
/*     */     }
/*     */ 
/*     */     
/* 664 */     public Axolotl.Variant getVariant(RandomSource random) { return this.types[random.nextInt(this.types.length)]; }
/*     */   }
/*     */   
/*     */   public enum AnimationState
/*     */   {
/* 669 */     PLAYING_DEAD,
/* 670 */     IN_WATER,
/* 671 */     ON_GROUND,
/* 672 */     IN_AIR;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\axolotl\Axolotl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */