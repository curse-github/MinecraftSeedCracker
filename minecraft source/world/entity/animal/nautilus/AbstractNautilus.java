/*     */ package net.minecraft.world.entity.animal.nautilus;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.HasCustomInventoryScreen;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.PlayerRideableJumping;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.TamableAnimal;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.food.FoodProperties;
/*     */ import net.minecraft.world.inventory.AbstractMountInventoryMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUtils;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractNautilus
/*     */   extends TamableAnimal
/*     */   implements PlayerRideableJumping, HasCustomInventoryScreen
/*     */ {
/*     */   public static final int INVENTORY_SLOT_OFFSET = 500;
/*     */   public static final int INVENTORY_ROWS = 3;
/*     */   public static final int SMALL_RESTRICTION_RADIUS = 16;
/*     */   public static final int LARGE_RESTRICTION_RADIUS = 32;
/*     */   public static final int RESTRICTION_RADIUS_BUFFER = 8;
/*     */   private static final int EFFECT_DURATION = 60;
/*     */   private static final int EFFECT_REFRESH_RATE = 40;
/*     */   private static final double NAUTILUS_WATER_RESISTANCE = 0.9D;
/*     */   private static final float IN_WATER_SPEED_MODIFIER = 0.011F;
/*     */   private static final float RIDDEN_SPEED_MODIFIER_IN_WATER = 0.0325F;
/*     */   private static final float RIDDEN_SPEED_MODIFIER_ON_LAND = 0.02F;
/*  81 */   private static final EntityDataAccessor<Boolean> DASH = SynchedEntityData.defineId(AbstractNautilus.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int DASH_COOLDOWN_TICKS = 40;
/*     */   private static final int DASH_MINIMUM_DURATION_TICKS = 5;
/*     */   private static final float DASH_MOMENTUM_IN_WATER = 1.2F;
/*     */   private static final float DASH_MOMENTUM_ON_LAND = 0.5F;
/*  87 */   private int dashCooldown = 0;
/*     */   
/*     */   protected float playerJumpPendingScale;
/*     */   protected SimpleContainer inventory;
/*     */   private static final double BUBBLE_SPREAD_FACTOR = 0.8D;
/*     */   private static final double BUBBLE_DIRECTION_SCALE = 1.1D;
/*     */   private static final double BUBBLE_Y_OFFSET = 0.25D;
/*     */   private static final double BUBBLE_PROBABILITY_MULTIPLIER = 2.0D;
/*     */   private static final float BUBBLE_PROBABILITY_MIN = 0.15F;
/*     */   private static final float BUBBLE_PROBABILITY_MAX = 1.0F;
/*     */   
/*     */   protected AbstractNautilus(EntityType<? extends AbstractNautilus> type, Level level) {
/*  99 */     super(type, level);
/*     */     
/* 101 */     this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.011F, 0.0F, true);
/* 102 */     this.lookControl = new SmoothSwimmingLookControl(this, 10);
/*     */     
/* 104 */     setPathfindingMalus(PathType.WATER, 0.0F);
/* 105 */     createInventory();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 110 */   public boolean isFood(ItemStack itemStack) { return (isTame() || isBaby()) ? itemStack.is(ItemTags.NAUTILUS_FOOD) : itemStack.is(ItemTags.NAUTILUS_TAMING_ITEMS); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void usePlayerItem(Player player, InteractionHand hand, ItemStack itemStack) {
/* 116 */     if (itemStack.is(ItemTags.NAUTILUS_BUCKET_FOOD)) {
/* 117 */       player.setItemInHand(hand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.WATER_BUCKET)));
/*     */     } else {
/* 119 */       super.usePlayerItem(player, hand, itemStack);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 124 */     return Animal.createAnimalAttributes()
/* 125 */       .add(Attributes.MAX_HEALTH, 15.0D)
/* 126 */       .add(Attributes.MOVEMENT_SPEED, 1.0D)
/* 127 */       .add(Attributes.ATTACK_DAMAGE, 3.0D)
/* 128 */       .add(Attributes.KNOCKBACK_RESISTANCE, 0.30000001192092896D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   public boolean isPushedByFluid() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   protected PathNavigation createNavigation(Level level) { return new WaterBoundPathNavigation(this, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 144 */   public float getWalkTargetValue(BlockPos pos, LevelReader level) { return 0.0F; }
/*     */ 
/*     */   
/*     */   public static boolean checkNautilusSpawnRules(EntityType<? extends AbstractNautilus> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 148 */     int seaLevel = level.getSeaLevel();
/* 149 */     int minSpawnLevel = seaLevel - 25;
/* 150 */     return (pos.getY() >= minSpawnLevel && pos
/* 151 */       .getY() <= seaLevel - 5 && level
/* 152 */       .getFluidState(pos.below()).is(FluidTags.WATER) && level
/* 153 */       .getBlockState(pos.above()).is(Blocks.WATER));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 158 */   public boolean checkSpawnObstruction(LevelReader level) { return level.isUnobstructed(this); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canUseSlot(EquipmentSlot slot) {
/* 163 */     if (slot == EquipmentSlot.SADDLE || slot == EquipmentSlot.BODY) {
/* 164 */       return (isAlive() && !isBaby() && isTame());
/*     */     }
/* 166 */     return super.canUseSlot(slot);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 171 */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) { return (slot == EquipmentSlot.BODY || slot == EquipmentSlot.SADDLE || super.canDispenserEquipIntoSlot(slot)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 178 */   protected boolean canAddPassenger(Entity passenger) { return !isVehicle(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public LivingEntity getControllingPassenger() {
/* 183 */     Entity firstPassenger = getFirstPassenger();
/* 184 */     if (isSaddled() && firstPassenger instanceof Player) return (Player)firstPassenger;
/*     */ 
/*     */     
/* 187 */     return super.getControllingPassenger();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Vec3 getRiddenInput(Player controller, Vec3 selfInput) {
/* 193 */     float strafe = controller.xxa;
/* 194 */     float forward = 0.0F;
/* 195 */     float up = 0.0F;
/*     */     
/* 197 */     if (controller.zza != 0.0F) {
/*     */       
/* 199 */       float forwardLook = Mth.cos((controller.getXRot() * 0.017453292F));
/* 200 */       float upLook = -Mth.sin((controller.getXRot() * 0.017453292F));
/* 201 */       if (controller.zza < 0.0F) {
/*     */         
/* 203 */         forwardLook *= -0.5F;
/* 204 */         upLook *= -0.5F;
/*     */       } 
/* 206 */       up = upLook;
/* 207 */       forward = forwardLook;
/*     */     } 
/* 209 */     return new Vec3(strafe, up, forward);
/*     */   }
/*     */ 
/*     */   
/* 213 */   protected Vec2 getRiddenRotation(LivingEntity controller) { return new Vec2(controller.getXRot() * 0.5F, controller.getYRot()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tickRidden(Player controller, Vec3 riddenInput) {
/* 218 */     super.tickRidden(controller, riddenInput);
/* 219 */     Vec2 rotation = getRiddenRotation(controller);
/* 220 */     float yRot = getYRot();
/* 221 */     float diff = Mth.wrapDegrees(rotation.y - yRot);
/* 222 */     float turnSpeed = 0.5F;
/* 223 */     yRot += diff * 0.5F;
/* 224 */     setRot(yRot, rotation.x);
/* 225 */     this.yRotO = this.yBodyRot = this.yHeadRot = yRot;
/* 226 */     if (isLocalInstanceAuthoritative()) {
/*     */       
/* 228 */       if (this.playerJumpPendingScale > 0.0F && !isJumping()) {
/* 229 */         executeRidersJump(this.playerJumpPendingScale, controller);
/*     */       }
/* 231 */       this.playerJumpPendingScale = 0.0F;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 237 */     float speed = getSpeed();
/* 238 */     moveRelative(speed, input);
/* 239 */     move(MoverType.SELF, getDeltaMovement());
/* 240 */     setDeltaMovement(getDeltaMovement().scale(0.9D));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 245 */   protected float getRiddenSpeed(Player controller) { return isInWater() ? (0.0325F * (float)getAttributeValue(Attributes.MOVEMENT_SPEED)) : (0.02F * (float)getAttributeValue(Attributes.MOVEMENT_SPEED)); }
/*     */ 
/*     */   
/*     */   protected void doPlayerRide(Player player) {
/* 249 */     if (!level().isClientSide()) {
/* 250 */       player.startRiding(this);
/* 251 */       if (!isVehicle()) {
/* 252 */         clearHome();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int getNautilusRestrictionRadius() {
/* 260 */     if (!isBaby() && getItemBySlot(EquipmentSlot.SADDLE).isEmpty()) {
/* 261 */       return 32;
/*     */     }
/* 263 */     return 16;
/*     */   }
/*     */   
/*     */   protected void checkRestriction() {
/* 267 */     if (isLeashed() || isVehicle() || !isTame()) {
/*     */       return;
/*     */     }
/* 270 */     int radius = getNautilusRestrictionRadius();
/* 271 */     if (hasHome() && getHomePosition().closerThan(blockPosition(), (radius + 8)) && radius == getHomeRadius()) {
/*     */       return;
/*     */     }
/* 274 */     setHomeTo(blockPosition(), radius);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 279 */     checkRestriction();
/* 280 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   private void applyEffects(Level level) {
/* 284 */     Entity passenger = getFirstPassenger();
/*     */     
/* 286 */     if (passenger instanceof Player) { Player player = (Player)passenger;
/* 287 */       boolean hasEffect = player.hasEffect(MobEffects.BREATH_OF_THE_NAUTILUS);
/* 288 */       boolean shouldRefresh = (level.getGameTime() % 40L == 0L);
/* 289 */       if (!hasEffect || shouldRefresh) {
/* 290 */         player.addEffect(new MobEffectInstance(MobEffects.BREATH_OF_THE_NAUTILUS, 60, 0, true, true, true));
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private void spawnBubbles() {
/* 297 */     double speed = getDeltaMovement().length();
/* 298 */     double bubbleProbability = Mth.clamp(speed * 2.0D, 0.15000000596046448D, 1.0D);
/* 299 */     if (this.random.nextFloat() < bubbleProbability) {
/*     */       
/* 301 */       float yRot = getYRot();
/* 302 */       float xRot = Mth.clamp(getXRot(), -10.0F, 10.0F);
/* 303 */       Vec3 mouthDirectionVector = calculateViewVector(xRot, yRot);
/* 304 */       double spread = this.random.nextDouble() * 0.8D * (1.0D + speed);
/* 305 */       double dx = (this.random.nextFloat() - 0.5D) * spread;
/* 306 */       double dy = (this.random.nextFloat() - 0.5D) * spread;
/* 307 */       double dz = (this.random.nextFloat() - 0.5D) * spread;
/* 308 */       level().addParticle(ParticleTypes.BUBBLE, getX() - mouthDirectionVector.x * 1.1D, getY() - mouthDirectionVector.y + 0.25D, getZ() - mouthDirectionVector.z * 1.1D, dx, dy, dz);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 314 */     super.tick();
/* 315 */     if (!level().isClientSide()) {
/* 316 */       applyEffects(level());
/*     */     }
/*     */     
/* 319 */     if (isDashing() && this.dashCooldown < 35) {
/* 320 */       setDashing(false);
/*     */     }
/* 322 */     if (this.dashCooldown > 0) {
/* 323 */       this.dashCooldown--;
/* 324 */       if (this.dashCooldown == 0) {
/* 325 */         makeSound(getDashReadySound());
/*     */       }
/*     */     } 
/*     */     
/* 329 */     if (isInWater()) {
/* 330 */       spawnBubbles();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 338 */   public boolean canJump() { return isSaddled(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPlayerJump(int jumpAmount) {
/* 343 */     if (!isSaddled() || this.dashCooldown > 0) {
/*     */       return;
/*     */     }
/* 346 */     this.playerJumpPendingScale = getPlayerJumpPendingScale(jumpAmount);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 351 */     super.defineSynchedData(entityData);
/* 352 */     entityData.define(DASH, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/* 356 */   public boolean isDashing() { return ((Boolean)this.entityData.get(DASH)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 360 */   public void setDashing(boolean isDashing) { this.entityData.set(DASH, Boolean.valueOf(isDashing)); }
/*     */ 
/*     */   
/*     */   protected void executeRidersJump(float amount, Player controller) {
/* 364 */     addDeltaMovement(controller.getLookAngle()
/* 365 */         .scale(((isInWater() ? 1.2F : 0.5F) * amount) * getAttributeValue(Attributes.MOVEMENT_SPEED) * getBlockSpeedFactor()));
/*     */ 
/*     */     
/* 368 */     this.dashCooldown = 40;
/* 369 */     setDashing(true);
/* 370 */     this.needsSync = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleStartJump(int jumpScale) {
/* 375 */     makeSound(getDashSound());
/* 376 */     gameEvent(GameEvent.ENTITY_ACTION);
/* 377 */     setDashing(true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 382 */   public int getJumpCooldown() { return this.dashCooldown; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 387 */     if (!this.firstTick && 
/* 388 */       DASH.equals(accessor)) {
/* 389 */       this.dashCooldown = (this.dashCooldown == 0) ? 40 : this.dashCooldown;
/*     */     }
/*     */     
/* 392 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleStopJump() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {}
/*     */ 
/*     */ 
/*     */   
/* 405 */   protected SoundEvent getDashSound() { return null; }
/*     */ 
/*     */ 
/*     */   
/* 409 */   protected SoundEvent getDashReadySound() { return null; }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand) {
/* 414 */     setPersistenceRequired();
/* 415 */     return super.interact(player, hand);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 420 */     ItemStack itemStack = player.getItemInHand(hand);
/*     */     
/* 422 */     if (isBaby()) {
/* 423 */       return super.mobInteract(player, hand);
/*     */     }
/*     */     
/* 426 */     if (isTame() && player.isSecondaryUseActive()) {
/* 427 */       openCustomInventoryScreen(player);
/* 428 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 431 */     if (!itemStack.isEmpty()) {
/*     */       
/* 433 */       if (!level().isClientSide() && !isTame() && isFood(itemStack)) {
/* 434 */         usePlayerItem(player, hand, itemStack);
/* 435 */         tryToTame(player);
/* 436 */         return InteractionResult.SUCCESS_SERVER;
/* 437 */       }  if (isFood(itemStack) && getHealth() < getMaxHealth()) {
/* 438 */         FoodProperties foodProperties = (FoodProperties)itemStack.get(DataComponents.FOOD);
/* 439 */         heal((foodProperties != null) ? (2 * foodProperties.nutrition()) : 1.0F);
/* 440 */         usePlayerItem(player, hand, itemStack);
/* 441 */         playEatingSound();
/* 442 */         return InteractionResult.SUCCESS;
/*     */       } 
/*     */       
/* 445 */       InteractionResult interactionResult = itemStack.interactLivingEntity(player, this, hand);
/* 446 */       if (interactionResult.consumesAction()) {
/* 447 */         return interactionResult;
/*     */       }
/*     */     } 
/*     */     
/* 451 */     if (isTame() && !player.isSecondaryUseActive() && !isFood(itemStack)) {
/* 452 */       doPlayerRide(player);
/* 453 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 456 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   private void tryToTame(Player player) {
/* 460 */     if (this.random.nextInt(3) == 0) {
/* 461 */       tame(player);
/* 462 */       this.navigation.stop();
/* 463 */       level().broadcastEntityEvent(this, (byte)7);
/*     */     } else {
/* 465 */       level().broadcastEntityEvent(this, (byte)6);
/*     */     } 
/* 467 */     playEatingSound();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 472 */   public boolean removeWhenFarAway(double distSqr) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 477 */     boolean wasHurt = super.hurtServer(level, source, damage);
/* 478 */     if (wasHurt) { Entity entity = source.getEntity(); if (entity instanceof LivingEntity) { LivingEntity sourceEntity = (LivingEntity)entity;
/* 479 */         NautilusAi.setAngerTarget(level, this, sourceEntity); }
/*     */        }
/* 481 */      return wasHurt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canBeAffected(MobEffectInstance newEffect) {
/* 487 */     if (newEffect.getEffect() == MobEffects.POISON) {
/* 488 */       return false;
/*     */     }
/* 490 */     return super.canBeAffected(newEffect);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 495 */     RandomSource random = level.getRandom();
/* 496 */     NautilusAi.initMemories(this, random);
/* 497 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   protected Holder<SoundEvent> getEquipSound(EquipmentSlot slot, ItemStack stack, Equippable equippable) {
/* 501 */     if (slot == EquipmentSlot.SADDLE && isUnderWater()) {
/* 502 */       return SoundEvents.NAUTILUS_SADDLE_UNDERWATER_EQUIP;
/*     */     }
/* 504 */     if (slot == EquipmentSlot.SADDLE) {
/* 505 */       return SoundEvents.NAUTILUS_SADDLE_EQUIP;
/*     */     }
/* 507 */     return super.getEquipSound(slot, stack, equippable);
/*     */   }
/*     */ 
/*     */   
/* 511 */   public final int getInventorySize() { return AbstractMountInventoryMenu.getInventorySize(getInventoryColumns()); }
/*     */ 
/*     */   
/*     */   protected void createInventory() {
/* 515 */     SimpleContainer old = this.inventory;
/* 516 */     this.inventory = new SimpleContainer(getInventorySize());
/* 517 */     if (old != null) {
/* 518 */       int max = Math.min(old.getContainerSize(), this.inventory.getContainerSize());
/* 519 */       for (int slot = 0; slot < max; slot++) {
/* 520 */         ItemStack itemStack = old.getItem(slot);
/* 521 */         if (!itemStack.isEmpty()) {
/* 522 */           this.inventory.setItem(slot, itemStack.copy());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void openCustomInventoryScreen(Player player) {
/* 530 */     if (!level().isClientSide() && (!isVehicle() || hasPassenger(player)) && isTame()) {
/* 531 */       player.openNautilusInventory(this, this.inventory);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/* 537 */     int inventorySlot = slot - 500;
/* 538 */     if (inventorySlot >= 0 && inventorySlot < this.inventory.getContainerSize()) {
/* 539 */       return this.inventory.getSlot(inventorySlot);
/*     */     }
/* 541 */     return super.getSlot(slot);
/*     */   }
/*     */ 
/*     */   
/* 545 */   public boolean hasInventoryChanged(Container oldInventory) { return (this.inventory != oldInventory); }
/*     */ 
/*     */ 
/*     */   
/* 549 */   public int getInventoryColumns() { return 0; }
/*     */ 
/*     */ 
/*     */   
/* 553 */   protected boolean isMobControlled() { return getFirstPassenger() instanceof net.minecraft.world.entity.Mob; }
/*     */ 
/*     */ 
/*     */   
/* 557 */   protected boolean isAggravated() { return (getBrain().hasMemoryValue(MemoryModuleType.ANGRY_AT) || getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\nautilus\AbstractNautilus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */