/*     */ package net.minecraft.world.entity.animal.polarbear;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class PolarBear
/*     */   extends Animal
/*     */   implements NeutralMob {
/*  59 */   private static final EntityDataAccessor<Boolean> DATA_STANDING_ID = SynchedEntityData.defineId(PolarBear.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final float STAND_ANIMATION_TICKS = 6.0F;
/*     */   
/*     */   private float clientSideStandAnimationO;
/*     */   private float clientSideStandAnimation;
/*     */   private int warningSoundTicks;
/*  66 */   private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   
/*     */   private long persistentAngerEndTime;
/*     */   private EntityReference<LivingEntity> persistentAngerTarget;
/*     */   
/*  71 */   public PolarBear(EntityType<? extends PolarBear> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) { return (AgeableMob)EntityType.POLAR_BEAR.create(level, EntitySpawnReason.BREEDING); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public boolean isFood(ItemStack itemStack) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  86 */     super.registerGoals();
/*     */     
/*  88 */     this.goalSelector.addGoal(0, new FloatGoal(this));
/*  89 */     this.goalSelector.addGoal(1, new PolarBearMeleeAttackGoal());
/*  90 */     this.goalSelector.addGoal(1, new PanicGoal(this, 2.0D, bear -> bear.isBaby() ? DamageTypeTags.PANIC_CAUSES : DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
/*  91 */     this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
/*  92 */     this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
/*  93 */     this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
/*  94 */     this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
/*     */     
/*  96 */     this.targetSelector.addGoal(1, new PolarBearHurtByTargetGoal());
/*  97 */     this.targetSelector.addGoal(2, new PolarBearAttackPlayersGoal());
/*  98 */     this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::isAngryAt));
/*  99 */     this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.animal.fox.Fox.class, 10, true, true, null));
/* 100 */     this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal(this, false));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 104 */     return Animal.createAnimalAttributes()
/* 105 */       .add(Attributes.MAX_HEALTH, 30.0D)
/* 106 */       .add(Attributes.FOLLOW_RANGE, 20.0D)
/* 107 */       .add(Attributes.MOVEMENT_SPEED, 0.25D)
/* 108 */       .add(Attributes.ATTACK_DAMAGE, 6.0D);
/*     */   }
/*     */   
/*     */   public static boolean checkPolarBearSpawnRules(EntityType<PolarBear> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 112 */     Holder<Biome> biome = level.getBiome(pos);
/*     */     
/* 114 */     if (biome.is(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS)) {
/* 115 */       return (isBrightEnoughToSpawn(level, pos) && level.getBlockState(pos.below()).is(BlockTags.POLAR_BEARS_SPAWNABLE_ON_ALTERNATE));
/*     */     }
/*     */     
/* 118 */     return checkAnimalSpawnRules(type, level, spawnReason, pos, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 123 */     super.readAdditionalSaveData(input);
/* 124 */     readPersistentAngerSaveData(level(), input);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 129 */     super.addAdditionalSaveData(output);
/* 130 */     addPersistentAngerSaveData(output);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public void startPersistentAngerTimer() { setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   public void setPersistentAngerEndTime(long endTime) { this.persistentAngerEndTime = endTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   public long getPersistentAngerEndTime() { return this.persistentAngerEndTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 150 */   public void setPersistentAngerTarget(EntityReference<LivingEntity> persistentAngerTarget) { this.persistentAngerTarget = persistentAngerTarget; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   public EntityReference<LivingEntity> getPersistentAngerTarget() { return this.persistentAngerTarget; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 160 */     if (isBaby()) {
/* 161 */       return SoundEvents.POLAR_BEAR_AMBIENT_BABY;
/*     */     }
/* 163 */     return SoundEvents.POLAR_BEAR_AMBIENT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 168 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.POLAR_BEAR_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 173 */   protected SoundEvent getDeathSound() { return SoundEvents.POLAR_BEAR_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 178 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.POLAR_BEAR_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */   
/*     */   protected void playWarningSound() {
/* 182 */     if (this.warningSoundTicks <= 0) {
/* 183 */       makeSound(SoundEvents.POLAR_BEAR_WARNING);
/*     */       
/* 185 */       this.warningSoundTicks = 40;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 191 */     super.defineSynchedData(entityData);
/*     */     
/* 193 */     entityData.define(DATA_STANDING_ID, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 198 */     super.tick();
/*     */     
/* 200 */     if (level().isClientSide()) {
/* 201 */       if (this.clientSideStandAnimation != this.clientSideStandAnimationO) {
/* 202 */         refreshDimensions();
/*     */       }
/* 204 */       this.clientSideStandAnimationO = this.clientSideStandAnimation;
/* 205 */       if (isStanding()) {
/* 206 */         this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation + 1.0F, 0.0F, 6.0F);
/*     */       } else {
/* 208 */         this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation - 1.0F, 0.0F, 6.0F);
/*     */       } 
/*     */     } 
/*     */     
/* 212 */     if (this.warningSoundTicks > 0) {
/* 213 */       this.warningSoundTicks--;
/*     */     }
/*     */     
/* 216 */     if (!level().isClientSide()) {
/* 217 */       updatePersistentAnger((ServerLevel)level(), true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 223 */     if (this.clientSideStandAnimation > 0.0F) {
/*     */       
/* 225 */       float standFactor = this.clientSideStandAnimation / 6.0F;
/* 226 */       float heightScaleFactor = 1.0F + standFactor;
/* 227 */       return super.getDefaultDimensions(pose).scale(1.0F, heightScaleFactor);
/*     */     } 
/* 229 */     return super.getDefaultDimensions(pose);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 234 */   public boolean isStanding() { return ((Boolean)this.entityData.get(DATA_STANDING_ID)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 238 */   public void setStanding(boolean value) { this.entityData.set(DATA_STANDING_ID, Boolean.valueOf(value)); }
/*     */ 
/*     */ 
/*     */   
/* 242 */   public float getStandingAnimationScale(float a) { return Mth.lerp(a, this.clientSideStandAnimationO, this.clientSideStandAnimation) / 6.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 247 */   protected float getWaterSlowDown() { return 0.98F; }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/* 252 */     if (groupData == null) {
/* 253 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(1.0F);
/*     */     }
/*     */     
/* 256 */     return super.finalizeSpawn(level, difficulty, spawnReason, ageableMobGroupData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class PolarBearHurtByTargetGoal
/*     */     extends HurtByTargetGoal
/*     */   {
/* 265 */     public PolarBearHurtByTargetGoal() { super(PolarBear.this, new Class[0]); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() {
/* 270 */       super.start();
/* 271 */       if (PolarBear.this.isBaby()) {
/* 272 */         alertOthers();
/* 273 */         stop();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void alertOther(Mob other, LivingEntity hurtByMob) {
/* 279 */       if (other instanceof PolarBear && 
/* 280 */         !other.isBaby()) {
/* 281 */         super.alertOther(other, hurtByMob);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class PolarBearAttackPlayersGoal
/*     */     extends NearestAttackableTargetGoal<Player>
/*     */   {
/* 293 */     public PolarBearAttackPlayersGoal() { super(PolarBear.this, Player.class, 20, true, true, null); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 298 */       if (PolarBear.this.isBaby()) {
/* 299 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 303 */       if (super.canUse()) {
/* 304 */         List<PolarBear> bears = PolarBear.this.level().getEntitiesOfClass(PolarBear.class, PolarBear.this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
/* 305 */         for (PolarBear bear : bears) {
/* 306 */           if (bear.isBaby()) {
/* 307 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 312 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 317 */     protected double getFollowDistance() { return super.getFollowDistance() * 0.5D; }
/*     */   }
/*     */   
/*     */   private class PolarBearMeleeAttackGoal
/*     */     extends MeleeAttackGoal
/*     */   {
/* 323 */     public PolarBearMeleeAttackGoal() { super(PolarBear.this, 1.25D, true); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void checkAndPerformAttack(LivingEntity target) {
/* 328 */       if (canPerformAttack(target)) {
/* 329 */         resetAttackCooldown();
/* 330 */         this.mob.doHurtTarget(getServerLevel(this.mob), target);
/* 331 */         PolarBear.this.setStanding(false);
/* 332 */       } else if (this.mob.distanceToSqr(target) < ((target.getBbWidth() + 3.0F) * (target.getBbWidth() + 3.0F))) {
/* 333 */         if (isTimeToAttack()) {
/* 334 */           PolarBear.this.setStanding(false);
/* 335 */           resetAttackCooldown();
/*     */         } 
/* 337 */         if (getTicksUntilNextAttack() <= 10) {
/* 338 */           PolarBear.this.setStanding(true);
/* 339 */           PolarBear.this.playWarningSound();
/*     */         } 
/*     */       } else {
/*     */         
/* 343 */         resetAttackCooldown();
/* 344 */         PolarBear.this.setStanding(false);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 350 */       PolarBear.this.setStanding(false);
/* 351 */       super.stop();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\polarbear\PolarBear.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */