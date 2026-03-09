/*     */ package net.minecraft.world.entity.animal.happyghast;
/*     */ 
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.Leashable;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ import net.minecraft.world.entity.ai.control.FlyingMoveControl;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.monster.Ghast;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class HappyGhast
/*     */   extends Animal
/*     */ {
/*     */   public static final float BABY_SCALE = 0.2375F;
/*     */   public static final int WANDER_GROUND_DISTANCE = 16;
/*     */   public static final int SMALL_RESTRICTION_RADIUS = 32;
/*     */   public static final int LARGE_RESTRICTION_RADIUS = 64;
/*     */   public static final int RESTRICTION_RADIUS_BUFFER = 16;
/*     */   public static final int FAST_HEALING_TICKS = 20;
/*     */   public static final int SLOW_HEALING_TICKS = 600;
/*     */   public static final int MAX_PASSANGERS = 4;
/*     */   private static final int STILL_TIMEOUT_ON_LOAD_GRACE_PERIOD = 60;
/*     */   private static final int MAX_STILL_TIMEOUT = 10;
/*     */   public static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
/*  67 */   private int leashHolderTime = 0;
/*     */   
/*     */   private int serverStillTimeout;
/*  70 */   private static final EntityDataAccessor<Boolean> IS_LEASH_HOLDER = SynchedEntityData.defineId(HappyGhast.class, EntityDataSerializers.BOOLEAN);
/*  71 */   private static final EntityDataAccessor<Boolean> STAYS_STILL = SynchedEntityData.defineId(HappyGhast.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final float MAX_SCALE = 1.0F;
/*     */ 
/*     */   
/*     */   public HappyGhast(EntityType<? extends HappyGhast> type, Level level) {
/*  77 */     super(type, level);
/*  78 */     this.moveControl = new Ghast.GhastMoveControl(this, true, this::isOnStillTimeout);
/*  79 */     this.lookControl = new HappyGhastLookControl();
/*     */   }
/*     */   
/*     */   private void setServerStillTimeout(int serverStillTimeout) {
/*  83 */     if (this.serverStillTimeout <= 0 && serverStillTimeout > 0) { Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*     */         
/*  85 */         syncPacketPositionCodec(getX(), getY(), getZ());
/*  86 */         (serverLevel.getChunkSource()).chunkMap.sendToTrackingPlayers(this, ClientboundEntityPositionSyncPacket.of(this)); }
/*     */        }
/*  88 */      this.serverStillTimeout = serverStillTimeout;
/*  89 */     syncStayStillFlag();
/*     */   }
/*     */ 
/*     */   
/*  93 */   private PathNavigation createBabyNavigation(Level level) { return new BabyFlyingPathNavigation(this, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  98 */     this.goalSelector.addGoal(3, new HappyGhastFloatGoal());
/*  99 */     this.goalSelector.addGoal(4, new TemptGoal.ForNonPathfinders(this, 1.0D, itemStack -> (isWearingBodyArmor() || isBaby()) ? itemStack.is(ItemTags.HAPPY_GHAST_FOOD) : itemStack.is(ItemTags.HAPPY_GHAST_TEMPT_ITEMS), false, 7.0D));
/* 100 */     this.goalSelector.addGoal(5, new Ghast.RandomFloatAroundGoal(this, 16));
/*     */   }
/*     */   
/*     */   private void adultGhastSetup() {
/* 104 */     this.moveControl = new Ghast.GhastMoveControl(this, true, this::isOnStillTimeout);
/* 105 */     this.lookControl = new HappyGhastLookControl();
/* 106 */     this.navigation = createNavigation(level());
/* 107 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 108 */       removeAllGoals(goal -> true);
/* 109 */       registerGoals();
/* 110 */       this.brain.stopAll(serverLevel, this);
/* 111 */       this.brain.clearMemories(); }
/*     */   
/*     */   }
/*     */   
/*     */   private void babyGhastSetup() {
/* 116 */     this.moveControl = new FlyingMoveControl(this, 180, true);
/* 117 */     this.lookControl = new LookControl(this);
/* 118 */     this.navigation = createBabyNavigation(level());
/* 119 */     setServerStillTimeout(0);
/* 120 */     removeAllGoals(goal -> true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void ageBoundaryReached() {
/* 125 */     if (isBaby()) {
/* 126 */       babyGhastSetup();
/*     */     } else {
/* 128 */       adultGhastSetup();
/*     */     } 
/* 130 */     super.ageBoundaryReached();
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 134 */     return Animal.createAnimalAttributes()
/* 135 */       .add(Attributes.MAX_HEALTH, 20.0D)
/* 136 */       .add(Attributes.TEMPT_RANGE, 16.0D)
/* 137 */       .add(Attributes.FLYING_SPEED, 0.05D)
/* 138 */       .add(Attributes.MOVEMENT_SPEED, 0.05D)
/* 139 */       .add(Attributes.FOLLOW_RANGE, 16.0D)
/* 140 */       .add(Attributes.CAMERA_DISTANCE, 8.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 145 */   protected float sanitizeScale(float scale) { return Math.min(scale, 1.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   public boolean onClimbable() { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void travel(Vec3 input) {
/* 163 */     float speed = (float)getAttributeValue(Attributes.FLYING_SPEED) * 5.0F / 3.0F;
/* 164 */     travelFlying(input, speed, speed, speed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/* 170 */     if (!level.isEmptyBlock(pos)) {
/* 171 */       return 0.0F;
/*     */     }
/* 173 */     if (level.isEmptyBlock(pos.below()) && !level.isEmptyBlock(pos.below(2))) {
/* 174 */       return 10.0F;
/*     */     }
/* 176 */     return 5.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBreatheUnderwater() {
/* 181 */     if (isBaby()) {
/* 182 */       return true;
/*     */     }
/* 184 */     return super.canBreatheUnderwater();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 189 */   protected boolean shouldStayCloseToLeashHolder() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   public float getVoicePitch() { return 1.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 207 */   public SoundSource getSoundSource() { return SoundSource.NEUTRAL; }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAmbientSoundInterval() {
/* 212 */     int interval = super.getAmbientSoundInterval();
/* 213 */     if (isVehicle())
/*     */     {
/* 215 */       return interval * 6;
/*     */     }
/* 217 */     return interval;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 222 */   protected SoundEvent getAmbientSound() { return isBaby() ? SoundEvents.GHASTLING_AMBIENT : SoundEvents.HAPPY_GHAST_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 227 */   protected SoundEvent getHurtSound(DamageSource source) { return isBaby() ? SoundEvents.GHASTLING_HURT : SoundEvents.HAPPY_GHAST_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 232 */   protected SoundEvent getDeathSound() { return isBaby() ? SoundEvents.GHASTLING_DEATH : SoundEvents.HAPPY_GHAST_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 237 */   protected float getSoundVolume() { return isBaby() ? 1.0F : 4.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 242 */   public int getMaxSpawnClusterSize() { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 247 */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) { return (AgeableMob)EntityType.HAPPY_GHAST.create(level, EntitySpawnReason.BREEDING); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 252 */   public boolean canFallInLove() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 257 */   public float getAgeScale() { return isBaby() ? 0.2375F : 1.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 262 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.HAPPY_GHAST_FOOD); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canUseSlot(EquipmentSlot slot) {
/* 267 */     if (slot == EquipmentSlot.BODY) {
/* 268 */       return (isAlive() && !isBaby());
/*     */     }
/* 270 */     return super.canUseSlot(slot);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 275 */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) { return (slot == EquipmentSlot.BODY); }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 280 */     if (isBaby()) {
/* 281 */       return super.mobInteract(player, hand);
/*     */     }
/*     */ 
/*     */     
/* 285 */     ItemStack itemStack = player.getItemInHand(hand);
/* 286 */     if (!itemStack.isEmpty()) {
/* 287 */       InteractionResult interactionResult = itemStack.interactLivingEntity(player, this, hand);
/* 288 */       if (interactionResult.consumesAction()) {
/* 289 */         return interactionResult;
/*     */       }
/*     */     } 
/*     */     
/* 293 */     if (isWearingBodyArmor() && !player.isSecondaryUseActive()) {
/* 294 */       doPlayerRide(player);
/* 295 */       return InteractionResult.SUCCESS;
/*     */     } 
/* 297 */     return super.mobInteract(player, hand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void doPlayerRide(Player player) {
/* 303 */     if (!level().isClientSide()) {
/* 304 */       player.startRiding(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addPassenger(Entity passenger) {
/* 310 */     if (!isVehicle()) {
/* 311 */       level().playSound(null, getX(), getY(), getZ(), SoundEvents.HARNESS_GOGGLES_DOWN, getSoundSource(), 1.0F, 1.0F);
/*     */     }
/* 313 */     super.addPassenger(passenger);
/* 314 */     if (!level().isClientSide()) {
/* 315 */       if (!scanPlayerAboveGhast()) {
/* 316 */         setServerStillTimeout(0);
/* 317 */       } else if (this.serverStillTimeout > 10) {
/* 318 */         setServerStillTimeout(10);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void removePassenger(Entity passenger) {
/* 325 */     super.removePassenger(passenger);
/* 326 */     if (!level().isClientSide()) {
/* 327 */       setServerStillTimeout(10);
/*     */     }
/* 329 */     if (!isVehicle()) {
/* 330 */       clearHome();
/* 331 */       level().playSound(null, getX(), getY(), getZ(), SoundEvents.HARNESS_GOGGLES_UP, getSoundSource(), 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 340 */   protected boolean canAddPassenger(Entity passenger) { return (getPassengers().size() < 4); }
/*     */ 
/*     */ 
/*     */   
/*     */   public LivingEntity getControllingPassenger() {
/* 345 */     Entity firstPassenger = getFirstPassenger();
/* 346 */     if (isWearingBodyArmor() && !isOnStillTimeout() && firstPassenger instanceof Player) return (Player)firstPassenger;
/*     */ 
/*     */     
/* 349 */     return super.getControllingPassenger();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3 getRiddenInput(Player controller, Vec3 selfInput) {
/* 354 */     float strafe = controller.xxa;
/* 355 */     float forward = 0.0F;
/* 356 */     float up = 0.0F;
/*     */     
/* 358 */     if (controller.zza != 0.0F) {
/*     */       
/* 360 */       float forwardLook = Mth.cos((controller.getXRot() * 0.017453292F));
/* 361 */       float upLook = -Mth.sin((controller.getXRot() * 0.017453292F));
/* 362 */       if (controller.zza < 0.0F) {
/*     */         
/* 364 */         forwardLook *= -0.5F;
/* 365 */         upLook *= -0.5F;
/*     */       } 
/* 367 */       up = upLook;
/* 368 */       forward = forwardLook;
/*     */     } 
/* 370 */     if (controller.isJumping()) {
/* 371 */       up += 0.5F;
/*     */     }
/* 373 */     return (new Vec3(strafe, up, forward)).scale(3.9000000953674316D * getAttributeValue(Attributes.FLYING_SPEED));
/*     */   }
/*     */ 
/*     */   
/* 377 */   protected Vec2 getRiddenRotation(LivingEntity controller) { return new Vec2(controller.getXRot() * 0.5F, controller.getYRot()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tickRidden(Player controller, Vec3 riddenInput) {
/* 382 */     super.tickRidden(controller, riddenInput);
/* 383 */     Vec2 rotation = getRiddenRotation(controller);
/* 384 */     float yRot = getYRot();
/* 385 */     float diff = Mth.wrapDegrees(rotation.y - yRot);
/*     */     
/* 387 */     float turnSpeed = 0.08F;
/* 388 */     yRot += diff * 0.08F;
/* 389 */     setRot(yRot, rotation.x);
/* 390 */     this.yRotO = this.yBodyRot = this.yHeadRot = yRot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 398 */   protected Brain.Provider<HappyGhast> brainProvider() { return HappyGhastAi.brainProvider(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 403 */   protected Brain<?> makeBrain(Dynamic<?> input) { return HappyGhastAi.makeBrain(brainProvider().makeBrain(input)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 408 */     if (isBaby()) {
/* 409 */       ProfilerFiller profiler = Profiler.get();
/* 410 */       profiler.push("happyGhastBrain");
/* 411 */       this.brain.tick(level, this);
/* 412 */       profiler.pop();
/*     */       
/* 414 */       profiler.push("happyGhastActivityUpdate");
/* 415 */       HappyGhastAi.updateActivity(this);
/* 416 */       profiler.pop();
/*     */     } 
/*     */     
/* 419 */     checkRestriction();
/*     */     
/* 421 */     super.customServerAiStep(level);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 426 */     super.tick();
/* 427 */     if (level().isClientSide()) {
/*     */       return;
/*     */     }
/* 430 */     if (this.leashHolderTime > 0) {
/* 431 */       this.leashHolderTime--;
/*     */     }
/* 433 */     setLeashHolder((this.leashHolderTime > 0));
/*     */     
/* 435 */     if (this.serverStillTimeout > 0) {
/* 436 */       if (this.tickCount > 60) {
/* 437 */         this.serverStillTimeout--;
/*     */       }
/* 439 */       setServerStillTimeout(this.serverStillTimeout);
/*     */     } 
/* 441 */     if (scanPlayerAboveGhast()) {
/* 442 */       setServerStillTimeout(10);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 448 */     if (!level().isClientSide()) {
/* 449 */       setRequiresPrecisePosition(isOnStillTimeout());
/*     */     }
/* 451 */     super.aiStep();
/*     */     
/* 453 */     continuousHeal();
/*     */   }
/*     */   
/*     */   private int getHappyGhastRestrictionRadius() {
/* 457 */     if (!isBaby() && getItemBySlot(EquipmentSlot.BODY).isEmpty()) {
/* 458 */       return 64;
/*     */     }
/* 460 */     return 32;
/*     */   }
/*     */   
/*     */   private void checkRestriction() {
/* 464 */     if (isLeashed() || isVehicle()) {
/*     */       return;
/*     */     }
/* 467 */     int radius = getHappyGhastRestrictionRadius();
/* 468 */     if (hasHome() && getHomePosition().closerThan(blockPosition(), (radius + 16)) && radius == getHomeRadius()) {
/*     */       return;
/*     */     }
/* 471 */     setHomeTo(blockPosition(), radius);
/*     */   }
/*     */   
/*     */   private void continuousHeal() {
/* 475 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1; if (isAlive() && this.deathTime == 0 && getMaxHealth() != getHealth()) {
/*     */ 
/*     */ 
/*     */         
/* 479 */         boolean isFastHealing = (isInClouds() || level.precipitationAt(blockPosition()) != Biome.Precipitation.NONE);
/* 480 */         if (this.tickCount % (isFastHealing ? 20 : 600) == 0)
/* 481 */           heal(1.0F); 
/*     */         return;
/*     */       }  }
/*     */   
/*     */   }
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 487 */     super.defineSynchedData(entityData);
/* 488 */     entityData.define(IS_LEASH_HOLDER, Boolean.valueOf(false));
/* 489 */     entityData.define(STAYS_STILL, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/* 493 */   private void setLeashHolder(boolean isLeashHolder) { this.entityData.set(IS_LEASH_HOLDER, Boolean.valueOf(isLeashHolder)); }
/*     */ 
/*     */ 
/*     */   
/* 497 */   public boolean isLeashHolder() { return ((Boolean)this.entityData.get(IS_LEASH_HOLDER)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 501 */   private void syncStayStillFlag() { this.entityData.set(STAYS_STILL, Boolean.valueOf((this.serverStillTimeout > 0))); }
/*     */ 
/*     */ 
/*     */   
/* 505 */   public boolean staysStill() { return ((Boolean)this.entityData.get(STAYS_STILL)).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 510 */   public boolean supportQuadLeashAsHolder() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 515 */   public Vec3[] getQuadLeashHolderOffsets() { return Leashable.createQuadLeashOffsets(this, -0.03125D, 0.4375D, 0.46875D, 0.03125D); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 520 */   public Vec3 getLeashOffset() { return Vec3.ZERO; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 525 */   public double leashElasticDistance() { return 10.0D; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 530 */   public double leashSnapDistance() { return 16.0D; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onElasticLeashPull() {
/* 535 */     super.onElasticLeashPull();
/* 536 */     getMoveControl().setWait();
/*     */   }
/*     */ 
/*     */   
/*     */   public void notifyLeashHolder(Leashable entity) {
/* 541 */     if (entity.supportQuadLeash()) {
/* 542 */       this.leashHolderTime = 5;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(ValueOutput tag) {
/* 550 */     super.addAdditionalSaveData(tag);
/* 551 */     tag.putInt("still_timeout", this.serverStillTimeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(ValueInput tag) {
/* 556 */     super.readAdditionalSaveData(tag);
/* 557 */     setServerStillTimeout(tag.getIntOr("still_timeout", 0));
/*     */   }
/*     */   
/*     */   private static class BabyFlyingPathNavigation extends FlyingPathNavigation {
/*     */     public BabyFlyingPathNavigation(HappyGhast mob, Level level) {
/* 562 */       super(mob, level);
/* 563 */       setCanOpenDoors(false);
/* 564 */       setCanFloat(true);
/* 565 */       setRequiredPathLength(48.0F);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 570 */     protected boolean canMoveDirectly(Vec3 startPos, Vec3 stopPos) { return isClearForMovementBetween(this.mob, startPos, stopPos, false); }
/*     */   }
/*     */   
/*     */   private class HappyGhastFloatGoal
/*     */     extends FloatGoal {
/*     */     public HappyGhastFloatGoal() {
/* 576 */       super(HappyGhast.this);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 582 */     public boolean canUse() { return (!HappyGhast.this.isOnStillTimeout() && super.canUse()); }
/*     */   }
/*     */   
/*     */   private class HappyGhastLookControl
/*     */     extends LookControl {
/*     */     private HappyGhastLookControl() {
/* 588 */       super(HappyGhast.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 593 */       if (HappyGhast.this.isOnStillTimeout()) {
/* 594 */         float closeAngle = wrapDegrees90(HappyGhast.this.getYRot());
/* 595 */         HappyGhast.this.setYRot(HappyGhast.this.getYRot() - closeAngle);
/* 596 */         HappyGhast.this.setYHeadRot(HappyGhast.this.getYRot());
/*     */         return;
/*     */       } 
/* 599 */       if (this.lookAtCooldown > 0) {
/* 600 */         this.lookAtCooldown--;
/* 601 */         double xdd = this.wantedX - HappyGhast.this.getX();
/* 602 */         double zdd = this.wantedZ - HappyGhast.this.getZ();
/* 603 */         HappyGhast.this.setYRot(-((float)Mth.atan2(xdd, zdd)) * 57.295776F);
/* 604 */         HappyGhast.this.yBodyRot = HappyGhast.this.getYRot();
/* 605 */         HappyGhast.this.yHeadRot = HappyGhast.this.yBodyRot;
/*     */         return;
/*     */       } 
/* 608 */       Ghast.faceMovementDirection(this.mob);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static float wrapDegrees90(float angle) {
/* 615 */       float normalizedAngle = angle % 90.0F;
/* 616 */       if (normalizedAngle >= 45.0F) {
/* 617 */         normalizedAngle -= 90.0F;
/*     */       }
/* 619 */       if (normalizedAngle < -45.0F) {
/* 620 */         normalizedAngle += 90.0F;
/*     */       }
/* 622 */       return normalizedAngle;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 627 */   public boolean isOnStillTimeout() { return (staysStill() || this.serverStillTimeout > 0); }
/*     */ 
/*     */   
/*     */   private boolean scanPlayerAboveGhast() {
/* 631 */     AABB happyGhastBb = getBoundingBox();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 637 */     AABB ghastDetectionBox = new AABB(happyGhastBb.minX - 1.0D, happyGhastBb.maxY - 9.999999747378752E-6D, happyGhastBb.minZ - 1.0D, happyGhastBb.maxX + 1.0D, happyGhastBb.maxY + happyGhastBb.getYsize() / 2.0D, happyGhastBb.maxZ + 1.0D);
/*     */ 
/*     */     
/* 640 */     for (Player player : level().players()) {
/* 641 */       if (player.isSpectator()) {
/*     */         continue;
/*     */       }
/* 644 */       Entity rootVehicle = player.getRootVehicle();
/* 645 */       if (!(rootVehicle instanceof HappyGhast) && ghastDetectionBox.contains(rootVehicle.position())) {
/* 646 */         return true;
/*     */       }
/*     */     } 
/* 649 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 654 */   protected BodyRotationControl createBodyControl() { return new HappyGhastBodyRotationControl(); }
/*     */   
/*     */   private class HappyGhastBodyRotationControl
/*     */     extends BodyRotationControl {
/*     */     public HappyGhastBodyRotationControl() {
/* 659 */       super(HappyGhast.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clientTick() {
/* 664 */       if (HappyGhast.this.isVehicle()) {
/* 665 */         HappyGhast.this.yHeadRot = HappyGhast.this.getYRot();
/* 666 */         HappyGhast.this.yBodyRot = HappyGhast.this.yHeadRot;
/*     */       } 
/* 668 */       super.clientTick();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeCollidedWith(Entity other) {
/* 674 */     if (isBaby() || !isAlive())
/*     */     {
/* 676 */       return false;
/*     */     }
/* 678 */     if (level().isClientSide() && other instanceof Player)
/*     */     {
/*     */       
/* 681 */       if ((other.position()).y >= (getBoundingBox()).maxY) {
/* 682 */         return true;
/*     */       }
/*     */     }
/* 685 */     if (isVehicle() && other instanceof HappyGhast)
/*     */     {
/* 687 */       return true;
/*     */     }
/* 689 */     return isOnStillTimeout();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 694 */   public boolean isFlyingVehicle() { return !isBaby(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 699 */   public Vec3 getDismountLocationForPassenger(LivingEntity passenger) { return new Vec3(getX(), (getBoundingBox()).maxY, getZ()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\happyghast\HappyGhast.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */