/*     */ package net.minecraft.world.entity.monster.hoglin;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.monster.Enemy;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.Zoglin;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Hoglin
/*     */   extends Animal
/*     */   implements Enemy, HoglinBase
/*     */ {
/*  60 */   private static final EntityDataAccessor<Boolean> DATA_IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.defineId(Hoglin.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int MAX_HEALTH = 40;
/*     */   
/*     */   private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.3F;
/*     */   private static final int ATTACK_KNOCKBACK = 1;
/*     */   private static final float KNOCKBACK_RESISTANCE = 0.6F;
/*     */   private static final int ATTACK_DAMAGE = 6;
/*     */   private static final float BABY_ATTACK_DAMAGE = 0.5F;
/*     */   private static final boolean DEFAULT_IMMUNE_TO_ZOMBIFICATION = false;
/*     */   private static final int DEFAULT_TIME_IN_OVERWORLD = 0;
/*     */   private static final boolean DEFAULT_CANNOT_BE_HUNTED = false;
/*     */   public static final int CONVERSION_TIME = 300;
/*     */   private int attackAnimationRemainingTicks;
/*  74 */   private int timeInOverworld = 0;
/*     */   
/*     */   private boolean cannotBeHunted = false;
/*  77 */   protected static final ImmutableList<? extends SensorType<? extends Sensor<? super Hoglin>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ADULT, SensorType.HOGLIN_SPECIFIC_SENSOR);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.BREED_TARGET, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, new MemoryModuleType[] { MemoryModuleType.AVOID_TARGET, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.NEAREST_REPELLENT, MemoryModuleType.PACIFIED, MemoryModuleType.IS_PANICKING });
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
/*     */   public Hoglin(EntityType<? extends Hoglin> type, Level level) {
/* 107 */     super(type, level);
/* 108 */     this.xpReward = 5;
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 113 */   public void setTimeInOverworld(int timeInOverworld) { this.timeInOverworld = timeInOverworld; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public boolean canBeLeashed() { return true; }
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 122 */     return Monster.createMonsterAttributes()
/* 123 */       .add(Attributes.MAX_HEALTH, 40.0D)
/* 124 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 125 */       .add(Attributes.KNOCKBACK_RESISTANCE, 0.6000000238418579D)
/* 126 */       .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
/* 127 */       .add(Attributes.ATTACK_DAMAGE, 6.0D);
/*     */   }
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/*     */     LivingEntity livingEntity;
/* 132 */     if (target instanceof LivingEntity) { livingEntity = (LivingEntity)target; }
/* 133 */     else { return false; }
/*     */     
/* 135 */     this.attackAnimationRemainingTicks = 10;
/* 136 */     level().broadcastEntityEvent(this, (byte)4);
/*     */     
/* 138 */     makeSound(SoundEvents.HOGLIN_ATTACK);
/* 139 */     HoglinAi.onHitTarget(this, livingEntity);
/* 140 */     return HoglinBase.hurtAndThrowTarget(level, this, livingEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void blockedByItem(LivingEntity defender) {
/* 145 */     if (isAdult()) {
/* 146 */       HoglinBase.throwTarget(this, defender);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 152 */     boolean wasHurt = super.hurtServer(level, source, damage);
/* 153 */     if (wasHurt) { Entity entity = source.getEntity(); if (entity instanceof LivingEntity) { LivingEntity sourceEntity = (LivingEntity)entity;
/* 154 */         HoglinAi.wasHurtBy(level, this, sourceEntity); }
/*     */        }
/* 156 */      return wasHurt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 161 */   protected Brain.Provider<Hoglin> brainProvider() { return Brain.provider(MEMORY_TYPES, SENSOR_TYPES); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 166 */   protected Brain<?> makeBrain(Dynamic<?> input) { return HoglinAi.makeBrain(brainProvider().makeBrain(input)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 172 */   public Brain<Hoglin> getBrain() { return super.getBrain(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 177 */     ProfilerFiller profiler = Profiler.get();
/* 178 */     profiler.push("hoglinBrain");
/* 179 */     getBrain().tick(level, this);
/* 180 */     profiler.pop();
/*     */     
/* 182 */     HoglinAi.updateActivity(this);
/*     */     
/* 184 */     if (isConverting()) {
/* 185 */       this.timeInOverworld++;
/* 186 */       if (this.timeInOverworld > 300) {
/* 187 */         makeSound(SoundEvents.HOGLIN_CONVERTED_TO_ZOMBIFIED);
/* 188 */         finishConversion();
/*     */       } 
/*     */     } else {
/* 191 */       this.timeInOverworld = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 198 */     if (this.attackAnimationRemainingTicks > 0) {
/* 199 */       this.attackAnimationRemainingTicks--;
/*     */     }
/* 201 */     super.aiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void ageBoundaryReached() {
/* 206 */     if (isBaby()) {
/* 207 */       this.xpReward = 3;
/* 208 */       getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.5D);
/*     */     } else {
/* 210 */       this.xpReward = 5;
/* 211 */       getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 216 */   public static boolean checkHoglinSpawnRules(EntityType<Hoglin> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) { return !level.getBlockState(pos.below()).is(Blocks.NETHER_WART_BLOCK); }
/*     */ 
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 221 */     if (level.getRandom().nextFloat() < 0.2F) {
/* 222 */       setBaby(true);
/*     */     }
/*     */     
/* 225 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 230 */   public boolean removeWhenFarAway(double distSqr) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/* 235 */     if (HoglinAi.isPosNearNearestRepellent(this, pos)) {
/* 236 */       return -1.0F;
/*     */     }
/* 238 */     if (level.getBlockState(pos.below()).is(Blocks.CRIMSON_NYLIUM))
/*     */     {
/* 240 */       return 10.0F;
/*     */     }
/* 242 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 247 */     InteractionResult interactionSucceeded = super.mobInteract(player, hand);
/* 248 */     if (interactionSucceeded.consumesAction()) {
/* 249 */       setPersistenceRequired();
/*     */     }
/* 251 */     return interactionSucceeded;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 257 */     if (id == 4) {
/*     */       
/* 259 */       this.attackAnimationRemainingTicks = 10;
/* 260 */       makeSound(SoundEvents.HOGLIN_ATTACK);
/*     */     } else {
/* 262 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 268 */   public int getAttackAnimationRemainingTicks() { return this.attackAnimationRemainingTicks; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 273 */   public boolean shouldDropExperience() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 278 */   protected int getBaseExperienceReward(ServerLevel level) { return this.xpReward; }
/*     */ 
/*     */   
/*     */   private void finishConversion() {
/* 282 */     convertTo(EntityType.ZOGLIN, ConversionParams.single(this, true, false), zoglin -> 
/* 283 */         zoglin.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 0)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 289 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.HOGLIN_FOOD); }
/*     */ 
/*     */ 
/*     */   
/* 293 */   public boolean isAdult() { return !isBaby(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 298 */     super.defineSynchedData(entityData);
/* 299 */     entityData.define(DATA_IMMUNE_TO_ZOMBIFICATION, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 304 */     super.addAdditionalSaveData(output);
/* 305 */     output.putBoolean("IsImmuneToZombification", isImmuneToZombification());
/* 306 */     output.putInt("TimeInOverworld", this.timeInOverworld);
/* 307 */     output.putBoolean("CannotBeHunted", this.cannotBeHunted);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 312 */     super.readAdditionalSaveData(input);
/* 313 */     setImmuneToZombification(input.getBooleanOr("IsImmuneToZombification", false));
/* 314 */     this.timeInOverworld = input.getIntOr("TimeInOverworld", 0);
/* 315 */     setCannotBeHunted(input.getBooleanOr("CannotBeHunted", false));
/*     */   }
/*     */ 
/*     */   
/* 319 */   public void setImmuneToZombification(boolean isImmuneToZombification) { getEntityData().set(DATA_IMMUNE_TO_ZOMBIFICATION, Boolean.valueOf(isImmuneToZombification)); }
/*     */ 
/*     */ 
/*     */   
/* 323 */   private boolean isImmuneToZombification() { return ((Boolean)getEntityData().get(DATA_IMMUNE_TO_ZOMBIFICATION)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 327 */   public boolean isConverting() { return (!isImmuneToZombification() && !isNoAi() && ((Boolean)level().environmentAttributes().getValue(EnvironmentAttributes.PIGLINS_ZOMBIFY, position())).booleanValue()); }
/*     */ 
/*     */ 
/*     */   
/* 331 */   private void setCannotBeHunted(boolean cannotBeHunted) { this.cannotBeHunted = cannotBeHunted; }
/*     */ 
/*     */ 
/*     */   
/* 335 */   public boolean canBeHunted() { return (isAdult() && !this.cannotBeHunted); }
/*     */ 
/*     */ 
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 340 */     Hoglin offspring = (Hoglin)EntityType.HOGLIN.create(level, EntitySpawnReason.BREEDING);
/* 341 */     if (offspring != null) {
/* 342 */       offspring.setPersistenceRequired();
/*     */     }
/* 344 */     return offspring;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 349 */   public boolean canFallInLove() { return (!HoglinAi.isPacified(this) && super.canFallInLove()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 354 */   public SoundSource getSoundSource() { return SoundSource.HOSTILE; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 359 */     if (level().isClientSide()) {
/* 360 */       return null;
/*     */     }
/* 362 */     return (SoundEvent)HoglinAi.getSoundForCurrentActivity(this).orElse(null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 367 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.HOGLIN_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 372 */   protected SoundEvent getDeathSound() { return SoundEvents.HOGLIN_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 377 */   protected SoundEvent getSwimSound() { return SoundEvents.HOSTILE_SWIM; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 382 */   protected SoundEvent getSwimSplashSound() { return SoundEvents.HOSTILE_SPLASH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 387 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.HOGLIN_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 392 */   public LivingEntity getTarget() { return getTargetFromBrain(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\hoglin\Hoglin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */