/*     */ package net.minecraft.world.entity.animal.wolf;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ItemParticleOption;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Crackiness;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.TamableAnimal;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BegGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*     */ import net.minecraft.world.entity.animal.equine.Llama;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.variant.SpawnContext;
/*     */ import net.minecraft.world.entity.variant.VariantUtils;
/*     */ import net.minecraft.world.food.FoodProperties;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.DyeItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Wolf
/*     */   extends TamableAnimal
/*     */   implements NeutralMob
/*     */ {
/*  93 */   private static final EntityDataAccessor<Boolean> DATA_INTERESTED_ID = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.BOOLEAN);
/*  94 */   private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.INT);
/*  95 */   private static final EntityDataAccessor<Long> DATA_ANGER_END_TIME = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.LONG);
/*     */   
/*  97 */   private static final EntityDataAccessor<Holder<WolfVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.WOLF_VARIANT);
/*  98 */   private static final EntityDataAccessor<Holder<WolfSoundVariant>> DATA_SOUND_VARIANT_ID = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.WOLF_SOUND_VARIANT);
/*     */   public static final TargetingConditions.Selector PREY_SELECTOR = (target, level) -> {
/* 100 */       EntityType<?> type = target.getType();
/* 101 */       return (type == EntityType.SHEEP || type == EntityType.RABBIT || type == EntityType.FOX);
/*     */     };
/*     */   
/*     */   private static final float START_HEALTH = 8.0F;
/*     */   
/*     */   private static final float TAME_HEALTH = 40.0F;
/*     */   private static final float ARMOR_REPAIR_UNIT = 0.125F;
/*     */   public static final float DEFAULT_TAIL_ANGLE = 0.62831855F;
/* 109 */   private static final DyeColor DEFAULT_COLLAR_COLOR = DyeColor.RED;
/*     */   
/*     */   private float interestedAngle;
/*     */   
/*     */   private float interestedAngleO;
/*     */   private boolean isWet;
/*     */   private boolean isShaking;
/*     */   private float shakeAnim;
/*     */   private float shakeAnimO;
/* 118 */   private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   private EntityReference<LivingEntity> persistentAngerTarget;
/*     */   
/*     */   public Wolf(EntityType<? extends Wolf> type, Level level) {
/* 122 */     super(type, level);
/*     */     
/* 124 */     setTame(false, false);
/*     */     
/* 126 */     setPathfindingMalus(PathType.POWDER_SNOW, -1.0F);
/* 127 */     setPathfindingMalus(PathType.DANGER_POWDER_SNOW, -1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 132 */     this.goalSelector.addGoal(1, new FloatGoal(this));
/* 133 */     this.goalSelector.addGoal(1, new TamableAnimal.TamableAnimalPanicGoal(this, 1.5D, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
/* 134 */     this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
/* 135 */     this.goalSelector.addGoal(3, new WolfAvoidEntityGoal(this, Llama.class, 24.0F, 1.5D, 1.5D));
/* 136 */     this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
/* 137 */     this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
/* 138 */     this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
/* 139 */     this.goalSelector.addGoal(7, new BreedGoal(this, 1.0D));
/* 140 */     this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
/* 141 */     this.goalSelector.addGoal(9, new BegGoal(this, 8.0F));
/* 142 */     this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
/* 143 */     this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
/*     */     
/* 145 */     this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
/* 146 */     this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
/* 147 */     this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
/* 148 */     this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::isAngryAt));
/* 149 */     this.targetSelector.addGoal(5, new NonTameRandomTargetGoal(this, Animal.class, false, PREY_SELECTOR));
/* 150 */     this.targetSelector.addGoal(6, new NonTameRandomTargetGoal(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
/* 151 */     this.targetSelector.addGoal(7, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.monster.skeleton.AbstractSkeleton.class, false));
/* 152 */     this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal(this, true));
/*     */   }
/*     */   
/*     */   public Identifier getTexture() {
/* 156 */     WolfVariant variant = (WolfVariant)getVariant().value();
/* 157 */     if (isTame()) {
/* 158 */       return variant.assetInfo().tame().texturePath();
/*     */     }
/* 160 */     if (isAngry()) {
/* 161 */       return variant.assetInfo().angry().texturePath();
/*     */     }
/* 163 */     return variant.assetInfo().wild().texturePath();
/*     */   }
/*     */ 
/*     */   
/* 167 */   private Holder<WolfVariant> getVariant() { return (Holder)this.entityData.get(DATA_VARIANT_ID); }
/*     */ 
/*     */ 
/*     */   
/* 171 */   private void setVariant(Holder<WolfVariant> variant) { this.entityData.set(DATA_VARIANT_ID, variant); }
/*     */ 
/*     */ 
/*     */   
/* 175 */   private Holder<WolfSoundVariant> getSoundVariant() { return (Holder)this.entityData.get(DATA_SOUND_VARIANT_ID); }
/*     */ 
/*     */ 
/*     */   
/* 179 */   private void setSoundVariant(Holder<WolfSoundVariant> soundVariant) { this.entityData.set(DATA_SOUND_VARIANT_ID, soundVariant); }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 184 */     if (type == DataComponents.WOLF_VARIANT) {
/* 185 */       return (T)castComponentValue(type, getVariant());
/*     */     }
/*     */     
/* 188 */     if (type == DataComponents.WOLF_SOUND_VARIANT) {
/* 189 */       return (T)castComponentValue(type, getSoundVariant());
/*     */     }
/*     */     
/* 192 */     if (type == DataComponents.WOLF_COLLAR) {
/* 193 */       return (T)castComponentValue(type, getCollarColor());
/*     */     }
/*     */     
/* 196 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 201 */     applyImplicitComponentIfPresent(components, DataComponents.WOLF_VARIANT);
/* 202 */     applyImplicitComponentIfPresent(components, DataComponents.WOLF_SOUND_VARIANT);
/* 203 */     applyImplicitComponentIfPresent(components, DataComponents.WOLF_COLLAR);
/* 204 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 209 */     if (type == DataComponents.WOLF_VARIANT) {
/* 210 */       setVariant((Holder)castComponentValue(DataComponents.WOLF_VARIANT, value));
/* 211 */       return true;
/*     */     } 
/*     */     
/* 214 */     if (type == DataComponents.WOLF_SOUND_VARIANT) {
/* 215 */       setSoundVariant((Holder)castComponentValue(DataComponents.WOLF_SOUND_VARIANT, value));
/* 216 */       return true;
/*     */     } 
/*     */     
/* 219 */     if (type == DataComponents.WOLF_COLLAR) {
/* 220 */       setCollarColor((DyeColor)castComponentValue(DataComponents.WOLF_COLLAR, value));
/* 221 */       return true;
/*     */     } 
/*     */     
/* 224 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 228 */     return Animal.createAnimalAttributes()
/* 229 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 230 */       .add(Attributes.MAX_HEALTH, 8.0D)
/* 231 */       .add(Attributes.ATTACK_DAMAGE, 4.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 236 */     super.defineSynchedData(entityData);
/* 237 */     Registry<WolfSoundVariant> wolfSoundVariants = registryAccess().lookupOrThrow(Registries.WOLF_SOUND_VARIANT);
/* 238 */     entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(registryAccess(), WolfVariants.DEFAULT));
/* 239 */     Objects.requireNonNull(wolfSoundVariants); entityData.define(DATA_SOUND_VARIANT_ID, (Holder)wolfSoundVariants.get(WolfSoundVariants.CLASSIC).or(wolfSoundVariants::getAny).orElseThrow());
/* 240 */     entityData.define(DATA_INTERESTED_ID, Boolean.valueOf(false));
/* 241 */     entityData.define(DATA_COLLAR_COLOR, Integer.valueOf(DEFAULT_COLLAR_COLOR.getId()));
/* 242 */     entityData.define(DATA_ANGER_END_TIME, Long.valueOf(-1L));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 247 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 252 */     super.addAdditionalSaveData(output);
/*     */     
/* 254 */     output.store("CollarColor", DyeColor.LEGACY_ID_CODEC, getCollarColor());
/* 255 */     VariantUtils.writeVariant(output, getVariant());
/* 256 */     addPersistentAngerSaveData(output);
/* 257 */     getSoundVariant().unwrapKey().ifPresent(soundVariant -> output.store("sound_variant", ResourceKey.codec(Registries.WOLF_SOUND_VARIANT), soundVariant));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 262 */     super.readAdditionalSaveData(input);
/*     */     
/* 264 */     VariantUtils.readVariant(input, Registries.WOLF_VARIANT).ifPresent(this::setVariant);
/*     */     
/* 266 */     setCollarColor((DyeColor)input.read("CollarColor", DyeColor.LEGACY_ID_CODEC).orElse(DEFAULT_COLLAR_COLOR));
/*     */     
/* 268 */     readPersistentAngerSaveData(level(), input);
/*     */     
/* 270 */     input.read("sound_variant", ResourceKey.codec(Registries.WOLF_SOUND_VARIANT))
/* 271 */       .flatMap(soundVariant -> registryAccess().lookupOrThrow(Registries.WOLF_SOUND_VARIANT).get(soundVariant)).ifPresent(this::setSoundVariant);
/*     */   }
/*     */   
/*     */   public static class WolfPackData extends AgeableMob.AgeableMobGroupData {
/*     */     public final Holder<WolfVariant> type;
/*     */     
/*     */     public WolfPackData(Holder<WolfVariant> type) {
/* 278 */       super(false);
/* 279 */       this.type = type;
/*     */     }
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     WolfPackData wolfPackData;
/* 285 */     if (groupData instanceof WolfPackData) { WolfPackData wolfGroupData = (WolfPackData)groupData;
/* 286 */       setVariant(wolfGroupData.type); }
/*     */     else
/* 288 */     { Optional<? extends Holder<WolfVariant>> selectedVariant = VariantUtils.selectVariantToSpawn(SpawnContext.create(level, blockPosition()), Registries.WOLF_VARIANT);
/* 289 */       if (selectedVariant.isPresent()) {
/* 290 */         setVariant((Holder)selectedVariant.get());
/* 291 */         wolfPackData = new WolfPackData((Holder)selectedVariant.get());
/*     */       }  }
/*     */     
/* 294 */     setSoundVariant(WolfSoundVariants.pickRandomSoundVariant(registryAccess(), level.getRandom()));
/* 295 */     return super.finalizeSpawn(level, difficulty, spawnReason, wolfPackData);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 300 */     if (isAngry()) {
/* 301 */       return (SoundEvent)((WolfSoundVariant)getSoundVariant().value()).growlSound().value();
/*     */     }
/* 303 */     if (this.random.nextInt(3) == 0) {
/* 304 */       if (isTame() && getHealth() < 20.0F) {
/* 305 */         return (SoundEvent)((WolfSoundVariant)getSoundVariant().value()).whineSound().value();
/*     */       }
/* 307 */       return (SoundEvent)((WolfSoundVariant)getSoundVariant().value()).pantSound().value();
/*     */     } 
/* 309 */     return (SoundEvent)((WolfSoundVariant)getSoundVariant().value()).ambientSound().value();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 314 */     if (canArmorAbsorb(source)) {
/* 315 */       return SoundEvents.WOLF_ARMOR_DAMAGE;
/*     */     }
/* 317 */     return (SoundEvent)((WolfSoundVariant)getSoundVariant().value()).hurtSound().value();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 322 */   protected SoundEvent getDeathSound() { return (SoundEvent)((WolfSoundVariant)getSoundVariant().value()).deathSound().value(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 327 */   protected float getSoundVolume() { return 0.4F; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 332 */     super.aiStep();
/*     */     
/* 334 */     if (!level().isClientSide() && this.isWet && !this.isShaking && !isPathFinding() && onGround()) {
/* 335 */       this.isShaking = true;
/* 336 */       this.shakeAnim = 0.0F;
/* 337 */       this.shakeAnimO = 0.0F;
/* 338 */       level().broadcastEntityEvent(this, (byte)8);
/*     */     } 
/* 340 */     if (!level().isClientSide()) {
/* 341 */       updatePersistentAnger((ServerLevel)level(), true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 347 */     super.tick();
/*     */     
/* 349 */     if (!isAlive()) {
/*     */       return;
/*     */     }
/*     */     
/* 353 */     this.interestedAngleO = this.interestedAngle;
/* 354 */     if (isInterested()) {
/* 355 */       this.interestedAngle += (1.0F - this.interestedAngle) * 0.4F;
/*     */     } else {
/* 357 */       this.interestedAngle += (0.0F - this.interestedAngle) * 0.4F;
/*     */     } 
/*     */     
/* 360 */     if (isInWaterOrRain()) {
/* 361 */       this.isWet = true;
/* 362 */       if (this.isShaking && !level().isClientSide()) {
/* 363 */         level().broadcastEntityEvent(this, (byte)56);
/* 364 */         cancelShake();
/*     */       } 
/* 366 */     } else if ((this.isWet || this.isShaking) && 
/* 367 */       this.isShaking) {
/* 368 */       if (this.shakeAnim == 0.0F) {
/* 369 */         playSound(SoundEvents.WOLF_SHAKE, getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/* 370 */         gameEvent(GameEvent.ENTITY_ACTION);
/*     */       } 
/*     */       
/* 373 */       this.shakeAnimO = this.shakeAnim;
/* 374 */       this.shakeAnim += 0.05F;
/*     */       
/* 376 */       if (this.shakeAnimO >= 2.0F) {
/* 377 */         this.isWet = false;
/* 378 */         this.isShaking = false;
/* 379 */         this.shakeAnimO = 0.0F;
/* 380 */         this.shakeAnim = 0.0F;
/*     */       } 
/*     */       
/* 383 */       if (this.shakeAnim > 0.4F) {
/* 384 */         float yt = (float)getY();
/* 385 */         int shakeCount = (int)(Mth.sin(((this.shakeAnim - 0.4F) * 3.1415927F)) * 7.0F);
/* 386 */         Vec3 movement = getDeltaMovement();
/* 387 */         for (int i = 0; i < shakeCount; i++) {
/* 388 */           float xo = (this.random.nextFloat() * 2.0F - 1.0F) * getBbWidth() * 0.5F;
/* 389 */           float zo = (this.random.nextFloat() * 2.0F - 1.0F) * getBbWidth() * 0.5F;
/* 390 */           level().addParticle(ParticleTypes.SPLASH, getX() + xo, (yt + 0.8F), getZ() + zo, movement.x, movement.y, movement.z);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void cancelShake() {
/* 398 */     this.isShaking = false;
/* 399 */     this.shakeAnim = 0.0F;
/* 400 */     this.shakeAnimO = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void die(DamageSource source) {
/* 405 */     this.isWet = false;
/* 406 */     this.isShaking = false;
/* 407 */     this.shakeAnimO = 0.0F;
/* 408 */     this.shakeAnim = 0.0F;
/*     */     
/* 410 */     super.die(source);
/*     */   }
/*     */   
/*     */   public float getWetShade(float a) {
/* 414 */     if (!this.isWet) {
/* 415 */       return 1.0F;
/*     */     }
/* 417 */     return Math.min(0.75F + Mth.lerp(a, this.shakeAnimO, this.shakeAnim) / 2.0F * 0.25F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/* 421 */   public float getShakeAnim(float a) { return Mth.lerp(a, this.shakeAnimO, this.shakeAnim); }
/*     */ 
/*     */ 
/*     */   
/* 425 */   public float getHeadRollAngle(float a) { return Mth.lerp(a, this.interestedAngleO, this.interestedAngle) * 0.15F * 3.1415927F; }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxHeadXRot() {
/* 430 */     if (isInSittingPose()) {
/* 431 */       return 20;
/*     */     }
/* 433 */     return super.getMaxHeadXRot();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 438 */     if (isInvulnerableTo(level, source)) {
/* 439 */       return false;
/*     */     }
/*     */     
/* 442 */     setOrderedToSit(false);
/*     */     
/* 444 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actuallyHurt(ServerLevel level, DamageSource source, float damage) {
/* 449 */     if (!canArmorAbsorb(source)) {
/* 450 */       super.actuallyHurt(level, source, damage);
/*     */       return;
/*     */     } 
/* 453 */     ItemStack armorBefore = getBodyArmorItem();
/* 454 */     int damageBefore = armorBefore.getDamageValue();
/* 455 */     int maxDamage = armorBefore.getMaxDamage();
/* 456 */     armorBefore.hurtAndBreak(Mth.ceil(damage), this, EquipmentSlot.BODY);
/* 457 */     if (Crackiness.WOLF_ARMOR.byDamage(damageBefore, maxDamage) != Crackiness.WOLF_ARMOR.byDamage(getBodyArmorItem())) {
/*     */       
/* 459 */       playSound(SoundEvents.WOLF_ARMOR_CRACK);
/* 460 */       level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, Items.ARMADILLO_SCUTE.getDefaultInstance()), getX(), getY() + 1.0D, getZ(), 20, 0.2D, 0.1D, 0.2D, 0.1D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 465 */   private boolean canArmorAbsorb(DamageSource source) { return (getBodyArmorItem().is(Items.WOLF_ARMOR) && !source.is(DamageTypeTags.BYPASSES_WOLF_ARMOR)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyTamingSideEffects() {
/* 470 */     if (isTame()) {
/* 471 */       getAttribute(Attributes.MAX_HEALTH).setBaseValue(40.0D);
/* 472 */       setHealth(40.0F);
/*     */     } else {
/* 474 */       getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 480 */   protected void hurtArmor(DamageSource damageSource, float damage) { doHurtEquipment(damageSource, damage, new EquipmentSlot[] { EquipmentSlot.BODY }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 485 */   protected boolean canShearEquipment(Player player) { return isOwnedBy(player); }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 490 */     ItemStack itemStack = player.getItemInHand(hand);
/* 491 */     Item item = itemStack.getItem();
/*     */     
/* 493 */     if (isTame())
/*     */     
/* 495 */     { if (isFood(itemStack) && getHealth() < getMaxHealth()) {
/* 496 */         usePlayerItem(player, hand, itemStack);
/* 497 */         FoodProperties foodProperties = (FoodProperties)itemStack.get(DataComponents.FOOD);
/* 498 */         float nutrition = (foodProperties != null) ? foodProperties.nutrition() : 1.0F;
/* 499 */         heal(2.0F * nutrition);
/* 500 */         return InteractionResult.SUCCESS;
/* 501 */       }  if (item instanceof DyeItem) { DyeItem dyeItem = (DyeItem)item; if (isOwnedBy(player))
/* 502 */         { DyeColor color = dyeItem.getDyeColor();
/* 503 */           if (color != getCollarColor()) {
/* 504 */             setCollarColor(color);
/* 505 */             itemStack.consume(1, player);
/* 506 */             return InteractionResult.SUCCESS;
/*     */           } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 538 */           return super.mobInteract(player, hand); }  }  if (isEquippableInSlot(itemStack, EquipmentSlot.BODY) && !isWearingBodyArmor() && isOwnedBy(player) && !isBaby()) { setBodyArmorItem(itemStack.copyWithCount(1)); itemStack.consume(1, player); return InteractionResult.SUCCESS; }  if (isInSittingPose() && isWearingBodyArmor() && isOwnedBy(player) && getBodyArmorItem().isDamaged() && getBodyArmorItem().isValidRepairItem(itemStack)) { itemStack.shrink(1); playSound(SoundEvents.WOLF_ARMOR_REPAIR); ItemStack armor = getBodyArmorItem(); int repairUnit = (int)(armor.getMaxDamage() * 0.125F); armor.setDamageValue(Math.max(0, armor.getDamageValue() - repairUnit)); return InteractionResult.SUCCESS; }  InteractionResult interactionResult = super.mobInteract(player, hand); if (!interactionResult.consumesAction() && isOwnedBy(player)) { setOrderedToSit(!isOrderedToSit()); this.jumping = false; this.navigation.stop(); setTarget(null); return InteractionResult.SUCCESS.withoutItem(); }  return interactionResult; }  if (!level().isClientSide() && itemStack.is(Items.BONE) && !isAngry()) { itemStack.consume(1, player); tryToTame(player); return InteractionResult.SUCCESS_SERVER; }  return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   private void tryToTame(Player player) {
/* 542 */     if (this.random.nextInt(3) == 0) {
/* 543 */       tame(player);
/* 544 */       this.navigation.stop();
/* 545 */       setTarget(null);
/* 546 */       setOrderedToSit(true);
/* 547 */       level().broadcastEntityEvent(this, (byte)7);
/*     */     } else {
/* 549 */       level().broadcastEntityEvent(this, (byte)6);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 555 */     if (id == 8) {
/* 556 */       this.isShaking = true;
/* 557 */       this.shakeAnim = 0.0F;
/* 558 */       this.shakeAnimO = 0.0F;
/* 559 */     } else if (id == 56) {
/* 560 */       cancelShake();
/*     */     } else {
/* 562 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getTailAngle() {
/* 567 */     if (isAngry())
/* 568 */       return 1.5393804F; 
/* 569 */     if (isTame()) {
/* 570 */       float maxHealth = getMaxHealth();
/* 571 */       float damageRatio = (maxHealth - getHealth()) / maxHealth;
/* 572 */       return (0.55F - damageRatio * 0.4F) * 3.1415927F;
/*     */     } 
/* 574 */     return 0.62831855F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 579 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.WOLF_FOOD); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 584 */   public int getMaxSpawnClusterSize() { return 8; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 589 */   public long getPersistentAngerEndTime() { return ((Long)this.entityData.get(DATA_ANGER_END_TIME)).longValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 594 */   public void setPersistentAngerEndTime(long endTime) { this.entityData.set(DATA_ANGER_END_TIME, Long.valueOf(endTime)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 599 */   public void startPersistentAngerTimer() { setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 604 */   public EntityReference<LivingEntity> getPersistentAngerTarget() { return this.persistentAngerTarget; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 609 */   public void setPersistentAngerTarget(EntityReference<LivingEntity> persistentAngerTarget) { this.persistentAngerTarget = persistentAngerTarget; }
/*     */ 
/*     */ 
/*     */   
/* 613 */   public DyeColor getCollarColor() { return DyeColor.byId(((Integer)this.entityData.get(DATA_COLLAR_COLOR)).intValue()); }
/*     */ 
/*     */ 
/*     */   
/* 617 */   private void setCollarColor(DyeColor color) { this.entityData.set(DATA_COLLAR_COLOR, Integer.valueOf(color.getId())); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Wolf getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 622 */     Wolf baby = (Wolf)EntityType.WOLF.create(level, EntitySpawnReason.BREEDING);
/* 623 */     if (baby != null && partner instanceof Wolf) { Wolf partnerWolf = (Wolf)partner;
/* 624 */       if (this.random.nextBoolean()) {
/* 625 */         baby.setVariant(getVariant());
/*     */       } else {
/* 627 */         baby.setVariant(partnerWolf.getVariant());
/*     */       } 
/*     */       
/* 630 */       if (isTame()) {
/* 631 */         baby.setOwnerReference(getOwnerReference());
/* 632 */         baby.setTame(true, true);
/* 633 */         DyeColor parent1CollarColor = getCollarColor();
/* 634 */         DyeColor parent2CollarColor = partnerWolf.getCollarColor();
/* 635 */         baby.setCollarColor(DyeColor.getMixedColor(level, parent1CollarColor, parent2CollarColor));
/*     */       } 
/* 637 */       baby.setSoundVariant(WolfSoundVariants.pickRandomSoundVariant(registryAccess(), this.random)); }
/*     */     
/* 639 */     return baby;
/*     */   }
/*     */ 
/*     */   
/* 643 */   public void setIsInterested(boolean value) { this.entityData.set(DATA_INTERESTED_ID, Boolean.valueOf(value)); }
/*     */ 
/*     */   
/*     */   public boolean canMate(Animal partner) {
/*     */     Wolf wolf;
/* 648 */     if (partner == this) {
/* 649 */       return false;
/*     */     }
/* 651 */     if (!isTame()) {
/* 652 */       return false;
/*     */     }
/* 654 */     if (partner instanceof Wolf) { wolf = (Wolf)partner; }
/* 655 */     else { return false; }
/*     */     
/* 657 */     if (!wolf.isTame()) {
/* 658 */       return false;
/*     */     }
/* 660 */     if (wolf.isInSittingPose()) {
/* 661 */       return false;
/*     */     }
/* 663 */     return (isInLove() && wolf.isInLove());
/*     */   }
/*     */ 
/*     */   
/* 667 */   public boolean isInterested() { return ((Boolean)this.entityData.get(DATA_INTERESTED_ID)).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
/* 673 */     if (target instanceof net.minecraft.world.entity.monster.Creeper || target instanceof net.minecraft.world.entity.monster.Ghast || target instanceof net.minecraft.world.entity.decoration.ArmorStand) {
/* 674 */       return false;
/*     */     }
/*     */     
/* 677 */     if (target instanceof Wolf) { Wolf wolfTarget = (Wolf)target;
/* 678 */       return (!wolfTarget.isTame() || wolfTarget.getOwner() != owner); }
/*     */     
/* 680 */     if (target instanceof Player) { Player playerTarget = (Player)target; if (owner instanceof Player) { Player playerOwner = (Player)owner; if (!playerOwner.canHarmPlayer(playerTarget))
/*     */         {
/* 682 */           return false; }  }
/*     */        }
/*     */     
/* 685 */     if (target instanceof AbstractHorse) { AbstractHorse horse = (AbstractHorse)target; if (horse.isTamed()) {
/* 686 */         return false;
/*     */       } }
/*     */     
/* 689 */     if (target instanceof TamableAnimal) { TamableAnimal animal = (TamableAnimal)target; if (!animal.isTame()); return false; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/* 694 */   public boolean canBeLeashed() { return !isAngry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 699 */   public Vec3 getLeashOffset() { return new Vec3(0.0D, (0.6F * getEyeHeight()), (getBbWidth() * 0.4F)); }
/*     */ 
/*     */   
/*     */   public static boolean checkWolfSpawnRules(EntityType<Wolf> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 703 */     return (level.getBlockState(pos.below()).is(BlockTags.WOLVES_SPAWNABLE_ON) && 
/* 704 */       isBrightEnoughToSpawn(level, pos));
/*     */   }
/*     */ 
/*     */   
/*     */   private class WolfAvoidEntityGoal<T extends LivingEntity>
/*     */     extends AvoidEntityGoal<T>
/*     */   {
/*     */     private final Wolf wolf;
/*     */     
/*     */     public WolfAvoidEntityGoal(Wolf wolf, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
/* 714 */       super(wolf, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier);
/* 715 */       this.wolf = wolf;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 720 */       if (super.canUse() && 
/* 721 */         this.toAvoid instanceof Llama) {
/* 722 */         return (!this.wolf.isTame() && avoidLlama((Llama)this.toAvoid));
/*     */       }
/*     */ 
/*     */       
/* 726 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 730 */     private boolean avoidLlama(Llama llama) { return (llama.getStrength() >= Wolf.this.random.nextInt(5)); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() {
/* 735 */       Wolf.this.setTarget(null);
/* 736 */       super.start();
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 741 */       Wolf.this.setTarget(null);
/* 742 */       super.tick();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\wolf\Wolf.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */