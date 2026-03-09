/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.monster.hoglin.HoglinBase;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Zoglin
/*     */   extends Monster
/*     */   implements HoglinBase
/*     */ {
/*  62 */   private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(Zoglin.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int MAX_HEALTH = 40;
/*     */   
/*     */   private static final int ATTACK_KNOCKBACK = 1;
/*     */   
/*     */   private static final float KNOCKBACK_RESISTANCE = 0.6F;
/*     */   
/*     */   private static final int ATTACK_DAMAGE = 6;
/*     */   
/*     */   private static final float BABY_ATTACK_DAMAGE = 0.5F;
/*     */   private static final int ATTACK_INTERVAL = 40;
/*     */   private static final int BABY_ATTACK_INTERVAL = 15;
/*     */   private static final int ATTACK_DURATION = 200;
/*     */   private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.3F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.4F;
/*     */   private static final boolean DEFAULT_BABY = false;
/*     */   private int attackAnimationRemainingTicks;
/*  80 */   protected static final ImmutableList<? extends SensorType<? extends Sensor<? super Zoglin>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS);
/*     */ 
/*     */ 
/*     */   
/*  84 */   protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN);
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
/*     */   public Zoglin(EntityType<? extends Zoglin> type, Level level) {
/*  98 */     super(type, level);
/*  99 */     this.xpReward = 5;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 104 */   protected Brain.Provider<Zoglin> brainProvider() { return Brain.provider(MEMORY_TYPES, SENSOR_TYPES); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> input) {
/* 109 */     Brain<Zoglin> brain = brainProvider().makeBrain(input);
/* 110 */     initCoreActivity(brain);
/* 111 */     initIdleActivity(brain);
/* 112 */     initFightActivity(brain);
/*     */     
/* 114 */     brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
/* 115 */     brain.setDefaultActivity(Activity.IDLE);
/* 116 */     brain.useDefaultActivity();
/* 117 */     return brain;
/*     */   }
/*     */ 
/*     */   
/* 121 */   private static void initCoreActivity(Brain<Zoglin> brain) { brain.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink())); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Zoglin> brain) {
/* 128 */     brain.addActivity(Activity.IDLE, 10, ImmutableList.of(
/* 129 */           StartAttacking.create((level, zoglin) -> zoglin.findNearestValidAttackTarget(level)), 
/* 130 */           SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)), new RunOne(
/* 131 */             ImmutableList.of(
/* 132 */               Pair.of(RandomStroll.stroll(0.4F), Integer.valueOf(2)), 
/* 133 */               Pair.of(SetWalkTargetFromLookTarget.create(0.4F, 3), Integer.valueOf(2)), 
/* 134 */               Pair.of(new DoNothing(30, 60), Integer.valueOf(1))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   private static void initFightActivity(Brain<Zoglin> brain) { brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(
/* 141 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F), 
/* 142 */           BehaviorBuilder.triggerIf(Zoglin::isAdult, MeleeAttack.create(40)), 
/* 143 */           BehaviorBuilder.triggerIf(Zoglin::isBaby, MeleeAttack.create(15)), 
/* 144 */           StopAttackingIfTargetInvalid.create()), MemoryModuleType.ATTACK_TARGET); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 149 */   private Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel level) { return ((NearestVisibleLivingEntities)getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty())).findClosest(target -> isTargetable(level, target)); }
/*     */ 
/*     */   
/*     */   private boolean isTargetable(ServerLevel level, LivingEntity livingEntity) {
/* 153 */     EntityType<?> type = livingEntity.getType();
/* 154 */     return (type != EntityType.ZOGLIN && type != EntityType.CREEPER && Sensor.isEntityAttackable(level, this, livingEntity));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 159 */     super.defineSynchedData(entityData);
/* 160 */     entityData.define(DATA_BABY_ID, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 165 */     super.onSyncedDataUpdated(accessor);
/* 166 */     if (DATA_BABY_ID.equals(accessor)) {
/* 167 */       refreshDimensions();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 173 */     if (level.getRandom().nextFloat() < 0.2F) {
/* 174 */       setBaby(true);
/*     */     }
/*     */     
/* 177 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 181 */     return Monster.createMonsterAttributes()
/* 182 */       .add(Attributes.MAX_HEALTH, 40.0D)
/* 183 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 184 */       .add(Attributes.KNOCKBACK_RESISTANCE, 0.6000000238418579D)
/* 185 */       .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
/* 186 */       .add(Attributes.ATTACK_DAMAGE, 6.0D);
/*     */   }
/*     */ 
/*     */   
/* 190 */   public boolean isAdult() { return !isBaby(); }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/*     */     LivingEntity entity;
/* 195 */     if (target instanceof LivingEntity) { entity = (LivingEntity)target; }
/* 196 */     else { return false; }
/*     */     
/* 198 */     this.attackAnimationRemainingTicks = 10;
/* 199 */     level.broadcastEntityEvent(this, (byte)4);
/*     */     
/* 201 */     makeSound(SoundEvents.ZOGLIN_ATTACK);
/* 202 */     return HoglinBase.hurtAndThrowTarget(level, this, entity);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 207 */   public boolean canBeLeashed() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void blockedByItem(LivingEntity defender) {
/* 212 */     if (!isBaby()) {
/* 213 */       HoglinBase.throwTarget(this, defender);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*     */     LivingEntity attacker;
/* 219 */     boolean wasHurt = super.hurtServer(level, source, damage);
/* 220 */     if (wasHurt) { Entity entity = source.getEntity(); if (entity instanceof LivingEntity) { attacker = (LivingEntity)entity; }
/* 221 */       else { return wasHurt; }  } else { return wasHurt; }
/*     */     
/* 223 */     if (canAttack(attacker) && !BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(this, attacker, 4.0D)) {
/* 224 */       setAttackTarget(attacker);
/*     */     }
/* 226 */     return true;
/*     */   }
/*     */   
/*     */   private void setAttackTarget(LivingEntity target) {
/* 230 */     this.brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 231 */     this.brain.setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, target, 200L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 237 */   public Brain<Zoglin> getBrain() { return super.getBrain(); }
/*     */ 
/*     */   
/*     */   protected void updateActivity() {
/* 241 */     Activity oldActivity = (Activity)this.brain.getActiveNonCoreActivity().orElse(null);
/*     */ 
/*     */     
/* 244 */     this.brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
/*     */     
/* 246 */     Activity newActivity = (Activity)this.brain.getActiveNonCoreActivity().orElse(null);
/* 247 */     if (newActivity == Activity.FIGHT && oldActivity != Activity.FIGHT)
/*     */     {
/* 249 */       playAngrySound();
/*     */     }
/*     */ 
/*     */     
/* 253 */     setAggressive(this.brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 258 */     ProfilerFiller profiler = Profiler.get();
/* 259 */     profiler.push("zoglinBrain");
/* 260 */     getBrain().tick(level, this);
/* 261 */     profiler.pop();
/*     */     
/* 263 */     updateActivity();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBaby(boolean baby) {
/* 268 */     getEntityData().set(DATA_BABY_ID, Boolean.valueOf(baby));
/* 269 */     if (!level().isClientSide() && baby) {
/* 270 */       getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.5D);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 276 */   public boolean isBaby() { return ((Boolean)getEntityData().get(DATA_BABY_ID)).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 282 */     if (this.attackAnimationRemainingTicks > 0) {
/* 283 */       this.attackAnimationRemainingTicks--;
/*     */     }
/* 285 */     super.aiStep();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 291 */     if (id == 4) {
/*     */       
/* 293 */       this.attackAnimationRemainingTicks = 10;
/* 294 */       makeSound(SoundEvents.ZOGLIN_ATTACK);
/*     */     } else {
/* 296 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 302 */   public int getAttackAnimationRemainingTicks() { return this.attackAnimationRemainingTicks; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 307 */     if (level().isClientSide()) {
/* 308 */       return null;
/*     */     }
/* 310 */     if (this.brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
/* 311 */       return SoundEvents.ZOGLIN_ANGRY;
/*     */     }
/* 313 */     return SoundEvents.ZOGLIN_AMBIENT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 318 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ZOGLIN_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 323 */   protected SoundEvent getDeathSound() { return SoundEvents.ZOGLIN_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 328 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.ZOGLIN_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/* 332 */   protected void playAngrySound() { makeSound(SoundEvents.ZOGLIN_ANGRY); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 337 */   public LivingEntity getTarget() { return getTargetFromBrain(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 342 */     super.addAdditionalSaveData(output);
/*     */     
/* 344 */     output.putBoolean("IsBaby", isBaby());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 349 */     super.readAdditionalSaveData(input);
/*     */     
/* 351 */     setBaby(input.getBooleanOr("IsBaby", false));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Zoglin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */