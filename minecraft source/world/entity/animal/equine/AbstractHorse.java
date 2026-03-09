/*      */ package net.minecraft.world.entity.animal.equine;
/*      */ 
/*      */ import com.google.common.collect.UnmodifiableIterator;
/*      */ import java.util.function.DoubleSupplier;
/*      */ import java.util.function.IntUnaryOperator;
/*      */ import net.minecraft.advancements.CriteriaTriggers;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.core.particles.SimpleParticleType;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.SimpleContainer;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.AgeableMob;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityDimensions;
/*      */ import net.minecraft.world.entity.EntityReference;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.HasCustomInventoryScreen;
/*      */ import net.minecraft.world.entity.HumanoidArm;
/*      */ import net.minecraft.world.entity.Leashable;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.OwnableEntity;
/*      */ import net.minecraft.world.entity.PlayerRideableJumping;
/*      */ import net.minecraft.world.entity.Pose;
/*      */ import net.minecraft.world.entity.SlotAccess;
/*      */ import net.minecraft.world.entity.SpawnGroupData;
/*      */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*      */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*      */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*      */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*      */ import net.minecraft.world.entity.ai.goal.RandomStandGoal;
/*      */ import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
/*      */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*      */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*      */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*      */ import net.minecraft.world.entity.animal.Animal;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.vehicle.DismountHelper;
/*      */ import net.minecraft.world.inventory.AbstractMountInventoryMenu;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.equipment.Equippable;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.SoundType;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.Vec2;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractHorse
/*      */   extends Animal
/*      */   implements PlayerRideableJumping, HasCustomInventoryScreen, OwnableEntity
/*      */ {
/*      */   public static final int CHEST_SLOT_OFFSET = 499;
/*      */   public static final int INVENTORY_SLOT_OFFSET = 500;
/*      */   public static final double BREEDING_CROSS_FACTOR = 0.15D;
/*   90 */   private static final float MIN_MOVEMENT_SPEED = (float)generateSpeed(() -> 0.0D);
/*   91 */   private static final float MAX_MOVEMENT_SPEED = (float)generateSpeed(() -> 1.0D);
/*   92 */   private static final float MIN_JUMP_STRENGTH = (float)generateJumpStrength(() -> 0.0D);
/*   93 */   private static final float MAX_JUMP_STRENGTH = (float)generateJumpStrength(() -> 1.0D);
/*   94 */   private static final float MIN_HEALTH = generateMaxHealth(i -> 0);
/*   95 */   private static final float MAX_HEALTH = generateMaxHealth(i -> i - 1);
/*      */   
/*      */   private static final float BACKWARDS_MOVE_SPEED_FACTOR = 0.25F;
/*      */   private static final float SIDEWAYS_MOVE_SPEED_FACTOR = 0.5F;
/*      */   private static final TargetingConditions.Selector PARENT_HORSE_SELECTOR = (target, level) -> {
/*  100 */       if (target instanceof AbstractHorse) { AbstractHorse horse = (AbstractHorse)target; if (horse.isBred()); }  return false;
/*  101 */     }; private static final TargetingConditions MOMMY_TARGETING = TargetingConditions.forNonCombat().range(16.0D).ignoreLineOfSight().selector(PARENT_HORSE_SELECTOR);
/*      */   
/*  103 */   private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(AbstractHorse.class, EntityDataSerializers.BYTE);
/*      */   
/*      */   private static final int FLAG_TAME = 2;
/*      */   
/*      */   private static final int FLAG_BRED = 8;
/*      */   
/*      */   private static final int FLAG_EATING = 16;
/*      */   
/*      */   private static final int FLAG_STANDING = 32;
/*      */   
/*      */   private static final int FLAG_OPEN_MOUTH = 64;
/*      */   public static final int INVENTORY_ROWS = 3;
/*      */   private static final int DEFAULT_TEMPER = 0;
/*      */   private static final boolean DEFAULT_EATING_HAYSTACK = false;
/*      */   private static final boolean DEFAULT_BRED = false;
/*      */   private static final boolean DEFAULT_TAME = false;
/*      */   private int eatingCounter;
/*      */   private int mouthCounter;
/*      */   private int standCounter;
/*      */   public int tailCounter;
/*      */   public int sprintCounter;
/*      */   protected SimpleContainer inventory;
/*  125 */   protected int temper = 0;
/*      */   
/*      */   protected float playerJumpPendingScale;
/*      */   
/*      */   protected boolean allowStandSliding;
/*      */   private float eatAnim;
/*      */   private float eatAnimO;
/*      */   private float standAnim;
/*      */   private float standAnimO;
/*      */   private float mouthAnim;
/*      */   private float mouthAnimO;
/*      */   protected boolean canGallop = true;
/*      */   protected int gallopSoundCounter;
/*      */   private EntityReference<LivingEntity> owner;
/*      */   
/*      */   protected AbstractHorse(EntityType<? extends AbstractHorse> type, Level level) {
/*  141 */     super(type, level);
/*      */     
/*  143 */     createInventory();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void registerGoals() {
/*  148 */     this.goalSelector.addGoal(1, new MountPanicGoal(1.2D));
/*  149 */     this.goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.2D));
/*  150 */     this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D, AbstractHorse.class));
/*  151 */     this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.0D));
/*  152 */     this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7D));
/*  153 */     this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
/*  154 */     this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
/*  155 */     if (canPerformRearing()) {
/*  156 */       this.goalSelector.addGoal(9, new RandomStandGoal(this));
/*      */     }
/*      */     
/*  159 */     addBehaviourGoals();
/*      */   }
/*      */   
/*      */   protected void addBehaviourGoals() {
/*  163 */     this.goalSelector.addGoal(0, new FloatGoal(this));
/*  164 */     this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, i -> i.is(ItemTags.HORSE_TEMPT_ITEMS), false));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  169 */     super.defineSynchedData(entityData);
/*  170 */     entityData.define(DATA_ID_FLAGS, Byte.valueOf((byte)0));
/*      */   }
/*      */ 
/*      */   
/*  174 */   protected boolean getFlag(int flag) { return ((((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue() & flag) != 0); }
/*      */ 
/*      */   
/*      */   protected void setFlag(int flag, boolean value) {
/*  178 */     byte current = ((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue();
/*  179 */     if (value) {
/*  180 */       this.entityData.set(DATA_ID_FLAGS, Byte.valueOf((byte)(current | flag)));
/*      */     } else {
/*  182 */       this.entityData.set(DATA_ID_FLAGS, Byte.valueOf((byte)(current & (flag ^ 0xFFFFFFFF))));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  187 */   public boolean isTamed() { return getFlag(2); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  192 */   public EntityReference<LivingEntity> getOwnerReference() { return this.owner; }
/*      */ 
/*      */ 
/*      */   
/*  196 */   public void setOwner(LivingEntity owner) { this.owner = EntityReference.of(owner); }
/*      */ 
/*      */ 
/*      */   
/*  200 */   public void setTamed(boolean flag) { setFlag(2, flag); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onElasticLeashPull() {
/*  205 */     super.onElasticLeashPull();
/*  206 */     if (isEating()) {
/*  207 */       setEating(false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  213 */   public boolean supportQuadLeash() { return true; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  218 */   public Vec3[] getQuadLeashOffsets() { return Leashable.createQuadLeashOffsets(this, 0.04D, 0.52D, 0.23D, 0.87D); }
/*      */ 
/*      */ 
/*      */   
/*  222 */   public boolean isEating() { return getFlag(16); }
/*      */ 
/*      */ 
/*      */   
/*  226 */   public boolean isStanding() { return getFlag(32); }
/*      */ 
/*      */ 
/*      */   
/*  230 */   public boolean isBred() { return getFlag(8); }
/*      */ 
/*      */ 
/*      */   
/*  234 */   public void setBred(boolean flag) { setFlag(8, flag); }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canUseSlot(EquipmentSlot slot) {
/*  239 */     if (slot == EquipmentSlot.SADDLE) {
/*  240 */       return (isAlive() && !isBaby() && isTamed());
/*      */     }
/*  242 */     return super.canUseSlot(slot);
/*      */   }
/*      */   
/*      */   public void equipBodyArmor(Player player, ItemStack itemStack) {
/*  246 */     if (isEquippableInSlot(itemStack, EquipmentSlot.BODY)) {
/*  247 */       setBodyArmorItem(itemStack.consumeAndReturn(1, player));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  253 */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) { return (((slot == EquipmentSlot.BODY || slot == EquipmentSlot.SADDLE) && isTamed()) || super.canDispenserEquipIntoSlot(slot)); }
/*      */ 
/*      */ 
/*      */   
/*  257 */   public int getTemper() { return this.temper; }
/*      */ 
/*      */ 
/*      */   
/*  261 */   public void setTemper(int temper) { this.temper = temper; }
/*      */ 
/*      */   
/*      */   public int modifyTemper(int amount) {
/*  265 */     int temper = Mth.clamp(getTemper() + amount, 0, getMaxTemper());
/*      */     
/*  267 */     setTemper(temper);
/*  268 */     return temper;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  273 */   public boolean isPushable() { return !isVehicle(); }
/*      */ 
/*      */   
/*      */   private void eating() {
/*  277 */     openMouth();
/*  278 */     if (!isSilent()) {
/*  279 */       SoundEvent eatingSound = getEatingSound();
/*  280 */       if (eatingSound != null) {
/*  281 */         level().playSound(null, getX(), getY(), getZ(), eatingSound, getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
/*  288 */     if (fallDistance > 1.0D) {
/*  289 */       playSound(SoundEvents.HORSE_LAND, 0.4F, 1.0F);
/*      */     }
/*      */     
/*  292 */     int dmg = calculateFallDamage(fallDistance, damageModifier);
/*  293 */     if (dmg <= 0) {
/*  294 */       return false;
/*      */     }
/*      */     
/*  297 */     hurt(damageSource, dmg);
/*  298 */     propagateFallToPassengers(fallDistance, damageModifier, damageSource);
/*      */     
/*  300 */     playBlockFallSound();
/*  301 */     return true;
/*      */   }
/*      */ 
/*      */   
/*  305 */   public final int getInventorySize() { return AbstractMountInventoryMenu.getInventorySize(getInventoryColumns()); }
/*      */ 
/*      */   
/*      */   protected void createInventory() {
/*  309 */     SimpleContainer old = this.inventory;
/*  310 */     this.inventory = new SimpleContainer(getInventorySize());
/*  311 */     if (old != null) {
/*  312 */       int max = Math.min(old.getContainerSize(), this.inventory.getContainerSize());
/*  313 */       for (int slot = 0; slot < max; slot++) {
/*  314 */         ItemStack itemStack = old.getItem(slot);
/*  315 */         if (!itemStack.isEmpty()) {
/*  316 */           this.inventory.setItem(slot, itemStack.copy());
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected Holder<SoundEvent> getEquipSound(EquipmentSlot slot, ItemStack stack, Equippable equippable) {
/*  324 */     if (slot == EquipmentSlot.SADDLE) {
/*  325 */       return SoundEvents.HORSE_SADDLE;
/*      */     }
/*  327 */     return super.getEquipSound(slot, stack, equippable);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*  332 */     boolean wasHurt = super.hurtServer(level, source, damage);
/*  333 */     if (wasHurt && this.random.nextInt(3) == 0) {
/*  334 */       standIfPossible();
/*      */     }
/*  336 */     return wasHurt;
/*      */   }
/*      */ 
/*      */   
/*  340 */   protected boolean canPerformRearing() { return true; }
/*      */ 
/*      */ 
/*      */   
/*  344 */   protected SoundEvent getEatingSound() { return null; }
/*      */ 
/*      */ 
/*      */   
/*  348 */   protected SoundEvent getAngrySound() { return null; }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/*  353 */     if (blockState.liquid()) {
/*      */       return;
/*      */     }
/*      */     
/*  357 */     BlockState aboveState = level().getBlockState(pos.above());
/*  358 */     SoundType soundType = blockState.getSoundType();
/*  359 */     if (aboveState.is(Blocks.SNOW)) {
/*  360 */       soundType = aboveState.getSoundType();
/*      */     }
/*      */     
/*  363 */     if (isVehicle() && this.canGallop) {
/*  364 */       this.gallopSoundCounter++;
/*  365 */       if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
/*  366 */         playGallopSound(soundType);
/*  367 */       } else if (this.gallopSoundCounter <= 5) {
/*  368 */         playSound(SoundEvents.HORSE_STEP_WOOD, soundType.getVolume() * 0.15F, soundType.getPitch());
/*      */       } 
/*  370 */     } else if (isWoodSoundType(soundType)) {
/*  371 */       playSound(SoundEvents.HORSE_STEP_WOOD, soundType.getVolume() * 0.15F, soundType.getPitch());
/*      */     } else {
/*  373 */       playSound(SoundEvents.HORSE_STEP, soundType.getVolume() * 0.15F, soundType.getPitch());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  378 */   private boolean isWoodSoundType(SoundType soundType) { return (soundType == SoundType.WOOD || soundType == SoundType.NETHER_WOOD || soundType == SoundType.STEM || soundType == SoundType.CHERRY_WOOD || soundType == SoundType.BAMBOO_WOOD); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  383 */   protected void playGallopSound(SoundType soundType) { playSound(SoundEvents.HORSE_GALLOP, soundType.getVolume() * 0.15F, soundType.getPitch()); }
/*      */ 
/*      */   
/*      */   public static AttributeSupplier.Builder createBaseHorseAttributes() {
/*  387 */     return Animal.createAnimalAttributes()
/*  388 */       .add(Attributes.JUMP_STRENGTH, 0.7D)
/*  389 */       .add(Attributes.MAX_HEALTH, 53.0D)
/*  390 */       .add(Attributes.MOVEMENT_SPEED, 0.22499999403953552D)
/*  391 */       .add(Attributes.STEP_HEIGHT, 1.0D)
/*  392 */       .add(Attributes.SAFE_FALL_DISTANCE, 6.0D)
/*  393 */       .add(Attributes.FALL_DAMAGE_MULTIPLIER, 0.5D);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  398 */   public int getMaxSpawnClusterSize() { return 6; }
/*      */ 
/*      */ 
/*      */   
/*  402 */   public int getMaxTemper() { return 100; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  407 */   protected float getSoundVolume() { return 0.8F; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  412 */   public int getAmbientSoundInterval() { return 400; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void openCustomInventoryScreen(Player player) {
/*  420 */     if (!level().isClientSide() && (!isVehicle() || hasPassenger(player)) && isTamed()) {
/*  421 */       player.openHorseInventory(this, this.inventory);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public InteractionResult fedFood(Player player, ItemStack itemStack) {
/*  427 */     boolean ateFood = handleEating(player, itemStack);
/*  428 */     if (ateFood) {
/*  429 */       itemStack.consume(1, player);
/*      */     }
/*  431 */     return (ateFood || level().isClientSide()) ? InteractionResult.SUCCESS_SERVER : InteractionResult.PASS;
/*      */   }
/*      */   
/*      */   protected boolean handleEating(Player player, ItemStack itemStack) {
/*  435 */     boolean itemUsed = false;
/*  436 */     float heal = 0.0F;
/*  437 */     int ageUp = 0;
/*  438 */     int temper = 0;
/*      */     
/*  440 */     if (itemStack.is(Items.WHEAT)) {
/*  441 */       heal = 2.0F;
/*  442 */       ageUp = 20;
/*  443 */       temper = 3;
/*  444 */     } else if (itemStack.is(Items.SUGAR)) {
/*  445 */       heal = 1.0F;
/*  446 */       ageUp = 30;
/*  447 */       temper = 3;
/*  448 */     } else if (itemStack.is(Blocks.HAY_BLOCK.asItem())) {
/*  449 */       heal = 20.0F;
/*  450 */       ageUp = 180;
/*  451 */     } else if (itemStack.is(Items.APPLE)) {
/*  452 */       heal = 3.0F;
/*  453 */       ageUp = 60;
/*  454 */       temper = 3;
/*  455 */     } else if (itemStack.is(Items.RED_MUSHROOM)) {
/*  456 */       heal = 3.0F;
/*  457 */       ageUp = 0;
/*  458 */       temper = 3;
/*  459 */     } else if (itemStack.is(Items.CARROT)) {
/*  460 */       heal = 3.0F;
/*  461 */       ageUp = 60;
/*  462 */       temper = 3;
/*  463 */     } else if (itemStack.is(Items.GOLDEN_CARROT)) {
/*  464 */       heal = 4.0F;
/*  465 */       ageUp = 60;
/*  466 */       temper = 5;
/*  467 */       if (!level().isClientSide() && isTamed() && getAge() == 0 && !isInLove()) {
/*  468 */         itemUsed = true;
/*  469 */         setInLove(player);
/*      */       } 
/*  471 */     } else if (itemStack.is(Items.GOLDEN_APPLE) || itemStack.is(Items.ENCHANTED_GOLDEN_APPLE)) {
/*  472 */       heal = 10.0F;
/*  473 */       ageUp = 240;
/*  474 */       temper = 10;
/*  475 */       if (!level().isClientSide() && isTamed() && getAge() == 0 && !isInLove()) {
/*  476 */         itemUsed = true;
/*  477 */         setInLove(player);
/*      */       } 
/*      */     } 
/*  480 */     if (getHealth() < getMaxHealth() && heal > 0.0F) {
/*  481 */       heal(heal);
/*  482 */       itemUsed = true;
/*      */     } 
/*  484 */     if (isBaby() && ageUp > 0) {
/*  485 */       level().addParticle(ParticleTypes.HAPPY_VILLAGER, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
/*  486 */       if (!level().isClientSide()) {
/*  487 */         ageUp(ageUp);
/*  488 */         itemUsed = true;
/*      */       } 
/*      */     } 
/*  491 */     if (temper > 0 && (itemUsed || !isTamed()) && getTemper() < getMaxTemper() && 
/*  492 */       !level().isClientSide()) {
/*  493 */       modifyTemper(temper);
/*  494 */       itemUsed = true;
/*      */     } 
/*      */     
/*  497 */     if (itemUsed) {
/*  498 */       eating();
/*  499 */       gameEvent(GameEvent.EAT);
/*      */     } 
/*  501 */     return itemUsed;
/*      */   }
/*      */   
/*      */   protected void doPlayerRide(Player player) {
/*  505 */     setEating(false);
/*  506 */     clearStanding();
/*  507 */     if (!level().isClientSide()) {
/*  508 */       player.setYRot(getYRot());
/*  509 */       player.setXRot(getXRot());
/*  510 */       player.startRiding(this);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  516 */   public boolean isImmobile() { return ((super.isImmobile() && isVehicle() && isSaddled()) || isEating() || isStanding()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  523 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.HORSE_FOOD); }
/*      */ 
/*      */ 
/*      */   
/*  527 */   private void moveTail() { this.tailCounter = 1; }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void dropEquipment(ServerLevel level) {
/*  532 */     super.dropEquipment(level);
/*  533 */     if (this.inventory == null) {
/*      */       return;
/*      */     }
/*  536 */     for (int i = 0; i < this.inventory.getContainerSize(); i++) {
/*  537 */       ItemStack itemStack = this.inventory.getItem(i);
/*  538 */       if (!itemStack.isEmpty() && !EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP))
/*      */       {
/*      */         
/*  541 */         spawnAtLocation(level, itemStack);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void aiStep() {
/*  547 */     if (this.random.nextInt(200) == 0) {
/*  548 */       moveTail();
/*      */     }
/*      */     
/*  551 */     super.aiStep();
/*      */     
/*  553 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1; if (isAlive()) {
/*      */ 
/*      */ 
/*      */         
/*  557 */         if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
/*  558 */           heal(1.0F);
/*      */         }
/*      */         
/*  561 */         if (canEatGrass()) {
/*  562 */           if (!isEating() && !isVehicle() && this.random.nextInt(300) == 0 && 
/*  563 */             level.getBlockState(blockPosition().below()).is(Blocks.GRASS_BLOCK)) {
/*  564 */             setEating(true);
/*      */           }
/*      */ 
/*      */           
/*  568 */           if (isEating() && ++this.eatingCounter > 50) {
/*  569 */             this.eatingCounter = 0;
/*  570 */             setEating(false);
/*      */           } 
/*      */         } 
/*      */         
/*  574 */         followMommy(level);
/*      */         return;
/*      */       }  }
/*      */      } protected void followMommy(ServerLevel level) {
/*  578 */     if (isBred() && isBaby() && !isEating()) {
/*  579 */       LivingEntity mommy = level.getNearestEntity(AbstractHorse.class, MOMMY_TARGETING, this, getX(), getY(), getZ(), getBoundingBox().inflate(16.0D));
/*  580 */       if (mommy != null && distanceToSqr(mommy) > 4.0D) {
/*  581 */         this.navigation.createPath(mommy, 0);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  587 */   public boolean canEatGrass() { return true; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void tick() {
/*  592 */     super.tick();
/*      */     
/*  594 */     if (this.mouthCounter > 0 && ++this.mouthCounter > 30) {
/*  595 */       this.mouthCounter = 0;
/*  596 */       setFlag(64, false);
/*      */     } 
/*      */     
/*  599 */     if (this.standCounter > 0 && 
/*  600 */       --this.standCounter <= 0) {
/*  601 */       clearStanding();
/*      */     }
/*      */ 
/*      */     
/*  605 */     if (this.tailCounter > 0 && ++this.tailCounter > 8) {
/*  606 */       this.tailCounter = 0;
/*      */     }
/*      */     
/*  609 */     if (this.sprintCounter > 0) {
/*  610 */       this.sprintCounter++;
/*      */       
/*  612 */       if (this.sprintCounter > 300) {
/*  613 */         this.sprintCounter = 0;
/*      */       }
/*      */     } 
/*      */     
/*  617 */     this.eatAnimO = this.eatAnim;
/*  618 */     if (isEating()) {
/*  619 */       this.eatAnim += (1.0F - this.eatAnim) * 0.4F + 0.05F;
/*  620 */       if (this.eatAnim > 1.0F) {
/*  621 */         this.eatAnim = 1.0F;
/*      */       }
/*      */     } else {
/*  624 */       this.eatAnim += (0.0F - this.eatAnim) * 0.4F - 0.05F;
/*  625 */       if (this.eatAnim < 0.0F) {
/*  626 */         this.eatAnim = 0.0F;
/*      */       }
/*      */     } 
/*  629 */     this.standAnimO = this.standAnim;
/*  630 */     if (isStanding()) {
/*      */       
/*  632 */       this.eatAnim = 0.0F;
/*  633 */       this.eatAnimO = this.eatAnim;
/*  634 */       this.standAnim += (1.0F - this.standAnim) * 0.4F + 0.05F;
/*  635 */       if (this.standAnim > 1.0F) {
/*  636 */         this.standAnim = 1.0F;
/*      */       }
/*      */     } else {
/*  639 */       this.allowStandSliding = false;
/*      */       
/*  641 */       this.standAnim += (0.8F * this.standAnim * this.standAnim * this.standAnim - this.standAnim) * 0.6F - 0.05F;
/*  642 */       if (this.standAnim < 0.0F) {
/*  643 */         this.standAnim = 0.0F;
/*      */       }
/*      */     } 
/*  646 */     this.mouthAnimO = this.mouthAnim;
/*  647 */     if (getFlag(64)) {
/*  648 */       this.mouthAnim += (1.0F - this.mouthAnim) * 0.7F + 0.05F;
/*  649 */       if (this.mouthAnim > 1.0F) {
/*  650 */         this.mouthAnim = 1.0F;
/*      */       }
/*      */     } else {
/*  653 */       this.mouthAnim += (0.0F - this.mouthAnim) * 0.7F - 0.05F;
/*  654 */       if (this.mouthAnim < 0.0F) {
/*  655 */         this.mouthAnim = 0.0F;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/*  662 */     if (isVehicle() || isBaby()) {
/*  663 */       return super.mobInteract(player, hand);
/*      */     }
/*  665 */     if (isTamed() && player.isSecondaryUseActive()) {
/*  666 */       openCustomInventoryScreen(player);
/*  667 */       return InteractionResult.SUCCESS;
/*      */     } 
/*      */     
/*  670 */     ItemStack itemStack = player.getItemInHand(hand);
/*  671 */     if (!itemStack.isEmpty()) {
/*  672 */       InteractionResult interactionResult = itemStack.interactLivingEntity(player, this, hand);
/*  673 */       if (interactionResult.consumesAction()) {
/*  674 */         return interactionResult;
/*      */       }
/*      */       
/*  677 */       if (isEquippableInSlot(itemStack, EquipmentSlot.BODY) && !isWearingBodyArmor()) {
/*  678 */         equipBodyArmor(player, itemStack);
/*  679 */         return InteractionResult.SUCCESS;
/*      */       } 
/*      */     } 
/*      */     
/*  683 */     doPlayerRide(player);
/*  684 */     return InteractionResult.SUCCESS;
/*      */   }
/*      */   
/*      */   private void openMouth() {
/*  688 */     if (!level().isClientSide()) {
/*  689 */       this.mouthCounter = 1;
/*  690 */       setFlag(64, true);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  695 */   public void setEating(boolean flag) { setFlag(16, flag); }
/*      */ 
/*      */   
/*      */   public void setStanding(int ticks) {
/*  699 */     setEating(false);
/*  700 */     setFlag(32, true);
/*  701 */     this.standCounter = ticks;
/*      */   }
/*      */   
/*      */   public void clearStanding() {
/*  705 */     setFlag(32, false);
/*  706 */     this.standCounter = 0;
/*      */   }
/*      */ 
/*      */   
/*  710 */   public SoundEvent getAmbientStandSound() { return getAmbientSound(); }
/*      */ 
/*      */   
/*      */   public void standIfPossible() {
/*  714 */     if (canPerformRearing() && (isEffectiveAi() || !level().isClientSide())) {
/*  715 */       setStanding(20);
/*      */     }
/*      */   }
/*      */   
/*      */   public void makeMad() {
/*  720 */     if (!isStanding() && !level().isClientSide()) {
/*  721 */       standIfPossible();
/*  722 */       makeSound(getAngrySound());
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean tameWithName(Player player) {
/*  727 */     setOwner(player);
/*  728 */     setTamed(true);
/*  729 */     if (player instanceof ServerPlayer) {
/*  730 */       CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayer)player, this);
/*      */     }
/*  732 */     level().broadcastEntityEvent(this, (byte)7);
/*  733 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void tickRidden(Player controller, Vec3 riddenInput) {
/*  738 */     super.tickRidden(controller, riddenInput);
/*  739 */     Vec2 rotation = getRiddenRotation(controller);
/*  740 */     setRot(rotation.y, rotation.x);
/*  741 */     this.yRotO = this.yBodyRot = this.yHeadRot = getYRot();
/*      */     
/*  743 */     if (isLocalInstanceAuthoritative()) {
/*  744 */       if (riddenInput.z <= 0.0D) {
/*  745 */         this.gallopSoundCounter = 0;
/*      */       }
/*      */       
/*  748 */       if (onGround()) {
/*  749 */         if (this.playerJumpPendingScale > 0.0F && !isJumping()) {
/*  750 */           executeRidersJump(this.playerJumpPendingScale, riddenInput);
/*      */         }
/*  752 */         this.playerJumpPendingScale = 0.0F;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  758 */   protected Vec2 getRiddenRotation(LivingEntity controller) { return new Vec2(controller.getXRot() * 0.5F, controller.getYRot()); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addPassenger(Entity passenger) {
/*  763 */     super.addPassenger(passenger);
/*  764 */     passenger.absSnapRotationTo(getViewYRot(0.0F), getViewXRot(0.0F));
/*      */   }
/*      */ 
/*      */   
/*      */   protected Vec3 getRiddenInput(Player controller, Vec3 selfInput) {
/*  769 */     if (onGround() && this.playerJumpPendingScale == 0.0F && isStanding() && !this.allowStandSliding) {
/*  770 */       return Vec3.ZERO;
/*      */     }
/*      */     
/*  773 */     float sideways = controller.xxa * 0.5F;
/*      */     
/*  775 */     float forward = controller.zza;
/*  776 */     if (forward <= 0.0F) {
/*  777 */       forward *= 0.25F;
/*      */     }
/*      */     
/*  780 */     return new Vec3(sideways, 0.0D, forward);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  785 */   protected float getRiddenSpeed(Player controller) { return (float)getAttributeValue(Attributes.MOVEMENT_SPEED); }
/*      */ 
/*      */   
/*      */   protected void executeRidersJump(float amount, Vec3 input) {
/*  789 */     double impulse = getJumpPower(amount);
/*  790 */     Vec3 movement = getDeltaMovement();
/*  791 */     setDeltaMovement(movement.x, impulse, movement.z);
/*      */     
/*  793 */     this.needsSync = true;
/*      */     
/*  795 */     if (input.z > 0.0D) {
/*  796 */       float sin = Mth.sin((getYRot() * 0.017453292F));
/*  797 */       float cos = Mth.cos((getYRot() * 0.017453292F));
/*      */       
/*  799 */       setDeltaMovement(getDeltaMovement().add((-0.4F * sin * amount), 0.0D, (0.4F * cos * amount)));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  808 */   protected void playJumpSound() { playSound(SoundEvents.HORSE_JUMP, 0.4F, 1.0F); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  813 */     super.addAdditionalSaveData(output);
/*      */     
/*  815 */     output.putBoolean("EatingHaystack", isEating());
/*  816 */     output.putBoolean("Bred", isBred());
/*  817 */     output.putInt("Temper", getTemper());
/*  818 */     output.putBoolean("Tame", isTamed());
/*      */     
/*  820 */     EntityReference.store(this.owner, output, "Owner");
/*      */   }
/*      */ 
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  825 */     super.readAdditionalSaveData(input);
/*  826 */     setEating(input.getBooleanOr("EatingHaystack", false));
/*  827 */     setBred(input.getBooleanOr("Bred", false));
/*  828 */     setTemper(input.getIntOr("Temper", 0));
/*  829 */     setTamed(input.getBooleanOr("Tame", false));
/*      */     
/*  831 */     this.owner = EntityReference.readWithOldOwnerConversion(input, "Owner", level());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  836 */   public boolean canMate(Animal partner) { return false; }
/*      */ 
/*      */ 
/*      */   
/*  840 */   protected boolean canParent() { return (!isVehicle() && !isPassenger() && isTamed() && !isBaby() && getHealth() >= getMaxHealth() && isInLove()); }
/*      */ 
/*      */ 
/*      */   
/*  844 */   public boolean isMobControlled() { return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  849 */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) { return null; }
/*      */ 
/*      */   
/*      */   protected void setOffspringAttributes(AgeableMob partner, AbstractHorse baby) {
/*  853 */     setOffspringAttribute(partner, baby, Attributes.MAX_HEALTH, MIN_HEALTH, MAX_HEALTH);
/*  854 */     setOffspringAttribute(partner, baby, Attributes.JUMP_STRENGTH, MIN_JUMP_STRENGTH, MAX_JUMP_STRENGTH);
/*  855 */     setOffspringAttribute(partner, baby, Attributes.MOVEMENT_SPEED, MIN_MOVEMENT_SPEED, MAX_MOVEMENT_SPEED);
/*      */   }
/*      */   
/*      */   private void setOffspringAttribute(AgeableMob partner, AbstractHorse baby, Holder<Attribute> attribute, double attributeRangeMin, double attributeRangeMax) {
/*  859 */     double newValue = createOffspringAttribute(getAttributeBaseValue(attribute), partner.getAttributeBaseValue(attribute), attributeRangeMin, attributeRangeMax, this.random);
/*  860 */     baby.getAttribute(attribute).setBaseValue(newValue);
/*      */   }
/*      */   
/*      */   static double createOffspringAttribute(double parentAValue, double parentBValue, double attributeRangeMin, double attributeRangeMax, RandomSource random) {
/*  864 */     if (attributeRangeMax <= attributeRangeMin) {
/*  865 */       throw new IllegalArgumentException("Incorrect range for an attribute");
/*      */     }
/*  867 */     parentAValue = Mth.clamp(parentAValue, attributeRangeMin, attributeRangeMax);
/*  868 */     parentBValue = Mth.clamp(parentBValue, attributeRangeMin, attributeRangeMax);
/*      */     
/*  870 */     double margin = 0.15D * (attributeRangeMax - attributeRangeMin);
/*  871 */     double range = Math.abs(parentAValue - parentBValue) + margin * 2.0D;
/*      */     
/*  873 */     double average = (parentAValue + parentBValue) / 2.0D;
/*  874 */     double babyQuality = (random.nextDouble() + random.nextDouble() + random.nextDouble()) / 3.0D - 0.5D;
/*  875 */     double newValue = average + range * babyQuality;
/*      */ 
/*      */     
/*  878 */     if (newValue > attributeRangeMax) {
/*  879 */       double difference = newValue - attributeRangeMax;
/*  880 */       return attributeRangeMax - difference;
/*      */     } 
/*  882 */     if (newValue < attributeRangeMin) {
/*  883 */       double difference = attributeRangeMin - newValue;
/*  884 */       return attributeRangeMin + difference;
/*      */     } 
/*  886 */     return newValue;
/*      */   }
/*      */ 
/*      */   
/*  890 */   public float getEatAnim(float a) { return Mth.lerp(a, this.eatAnimO, this.eatAnim); }
/*      */ 
/*      */ 
/*      */   
/*  894 */   public float getStandAnim(float a) { return Mth.lerp(a, this.standAnimO, this.standAnim); }
/*      */ 
/*      */ 
/*      */   
/*  898 */   public float getMouthAnim(float a) { return Mth.lerp(a, this.mouthAnimO, this.mouthAnim); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onPlayerJump(int jumpAmount) {
/*  903 */     if (!isSaddled()) {
/*      */       return;
/*      */     }
/*      */     
/*  907 */     if (jumpAmount < 0) {
/*  908 */       jumpAmount = 0;
/*      */     } else {
/*  910 */       this.allowStandSliding = true;
/*  911 */       standIfPossible();
/*      */     } 
/*  913 */     this.playerJumpPendingScale = getPlayerJumpPendingScale(jumpAmount);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  918 */   public boolean canJump() { return isSaddled(); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleStartJump(int jumpScale) {
/*  923 */     this.allowStandSliding = true;
/*  924 */     standIfPossible();
/*  925 */     playJumpSound();
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleStopJump() {}
/*      */ 
/*      */   
/*      */   protected void spawnTamingParticles(boolean success) {
/*  933 */     SimpleParticleType simpleParticleType = success ? ParticleTypes.HEART : ParticleTypes.SMOKE;
/*      */     
/*  935 */     for (int i = 0; i < 7; i++) {
/*  936 */       double xa = this.random.nextGaussian() * 0.02D;
/*  937 */       double ya = this.random.nextGaussian() * 0.02D;
/*  938 */       double za = this.random.nextGaussian() * 0.02D;
/*  939 */       level().addParticle(simpleParticleType, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), xa, ya, za);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEntityEvent(byte id) {
/*  945 */     if (id == 7) {
/*  946 */       spawnTamingParticles(true);
/*  947 */     } else if (id == 6) {
/*  948 */       spawnTamingParticles(false);
/*      */     } else {
/*  950 */       super.handleEntityEvent(id);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
/*  956 */     super.positionRider(passenger, moveFunction);
/*  957 */     if (passenger instanceof LivingEntity) {
/*  958 */       ((LivingEntity)passenger).yBodyRot = this.yBodyRot;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  964 */   protected static float generateMaxHealth(IntUnaryOperator integerByBoundProvider) { return 15.0F + integerByBoundProvider.applyAsInt(8) + integerByBoundProvider.applyAsInt(9); }
/*      */ 
/*      */ 
/*      */   
/*  968 */   protected static double generateJumpStrength(DoubleSupplier probabilityProvider) { return 0.4000000059604645D + probabilityProvider.getAsDouble() * 0.2D + probabilityProvider.getAsDouble() * 0.2D + probabilityProvider.getAsDouble() * 0.2D; }
/*      */ 
/*      */ 
/*      */   
/*  972 */   protected static double generateSpeed(DoubleSupplier probabilityProvider) { return (0.44999998807907104D + probabilityProvider.getAsDouble() * 0.3D + probabilityProvider.getAsDouble() * 0.3D + probabilityProvider.getAsDouble() * 0.3D) * 0.25D; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  977 */   public boolean onClimbable() { return false; }
/*      */ 
/*      */ 
/*      */   
/*      */   public SlotAccess getSlot(int slot) {
/*  982 */     int inventorySlot = slot - 500;
/*  983 */     if (inventorySlot >= 0 && inventorySlot < this.inventory.getContainerSize()) {
/*  984 */       return this.inventory.getSlot(inventorySlot);
/*      */     }
/*  986 */     return super.getSlot(slot);
/*      */   }
/*      */ 
/*      */   
/*      */   public LivingEntity getControllingPassenger() {
/*  991 */     if (isSaddled()) { Entity entity = getFirstPassenger(); if (entity instanceof Player) return (Player)entity;
/*      */        }
/*      */     
/*  994 */     return super.getControllingPassenger();
/*      */   }
/*      */   
/*      */   private Vec3 getDismountLocationInDirection(Vec3 direction, LivingEntity passenger) {
/*  998 */     double targetX = getX() + direction.x;
/*  999 */     double targetY = (getBoundingBox()).minY;
/* 1000 */     double targetZ = getZ() + direction.z;
/*      */     
/* 1002 */     BlockPos.MutableBlockPos targetBlockPos = new BlockPos.MutableBlockPos(); UnmodifiableIterator unmodifiableIterator;
/* 1003 */     label18: for (unmodifiableIterator = passenger.getDismountPoses().iterator(); unmodifiableIterator.hasNext(); ) { Pose dismountPose = (Pose)unmodifiableIterator.next();
/* 1004 */       targetBlockPos.set(targetX, targetY, targetZ);
/* 1005 */       double dismountJumpLimit = (getBoundingBox()).maxY + 0.75D;
/*      */       
/*      */       while (true) {
/* 1008 */         double blockFloorHeight = level().getBlockFloorHeight(targetBlockPos);
/*      */         
/* 1010 */         if (targetBlockPos.getY() + blockFloorHeight > dismountJumpLimit) {
/*      */           continue label18;
/*      */         }
/*      */         
/* 1014 */         if (DismountHelper.isBlockFloorValid(blockFloorHeight)) {
/* 1015 */           AABB poseCollisionBox = passenger.getLocalBoundsForPose(dismountPose);
/* 1016 */           Vec3 location = new Vec3(targetX, targetBlockPos.getY() + blockFloorHeight, targetZ);
/*      */           
/* 1018 */           if (DismountHelper.canDismountTo(level(), passenger, poseCollisionBox.move(location))) {
/* 1019 */             passenger.setPose(dismountPose);
/* 1020 */             return location;
/*      */           } 
/*      */         } 
/*      */         
/* 1024 */         targetBlockPos.move(Direction.UP);
/* 1025 */         if (targetBlockPos.getY() >= dismountJumpLimit)
/*      */           continue label18; 
/*      */       }  }
/* 1028 */      return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
/* 1033 */     Vec3 mainHandDirection = getCollisionHorizontalEscapeVector(getBbWidth(), passenger.getBbWidth(), getYRot() + ((passenger.getMainArm() == HumanoidArm.RIGHT) ? 90.0F : -90.0F));
/* 1034 */     Vec3 mainHandLocation = getDismountLocationInDirection(mainHandDirection, passenger);
/*      */     
/* 1036 */     if (mainHandLocation != null) {
/* 1037 */       return mainHandLocation;
/*      */     }
/*      */     
/* 1040 */     Vec3 offHandDirection = getCollisionHorizontalEscapeVector(getBbWidth(), passenger.getBbWidth(), getYRot() + ((passenger.getMainArm() == HumanoidArm.LEFT) ? 90.0F : -90.0F));
/* 1041 */     Vec3 offHandLocation = getDismountLocationInDirection(offHandDirection, passenger);
/*      */     
/* 1043 */     if (offHandLocation != null) {
/* 1044 */       return offHandLocation;
/*      */     }
/*      */     
/* 1047 */     return position();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void randomizeAttributes(RandomSource random) {}
/*      */   
/*      */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*      */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/* 1055 */     if (groupData == null) {
/* 1056 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(0.2F);
/*      */     }
/*      */     
/* 1059 */     randomizeAttributes(level.getRandom());
/*      */     
/* 1061 */     return super.finalizeSpawn(level, difficulty, spawnReason, ageableMobGroupData);
/*      */   }
/*      */ 
/*      */   
/* 1065 */   public boolean hasInventoryChanged(Container oldInventory) { return (this.inventory != oldInventory); }
/*      */ 
/*      */ 
/*      */   
/* 1069 */   public int getAmbientStandInterval() { return getAmbientSoundInterval(); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
/* 1074 */     return super.getPassengerAttachmentPoint(passenger, dimensions, scale).add((new Vec3(0.0D, 0.15D * this.standAnimO * scale, -0.7D * this.standAnimO * scale))
/*      */ 
/*      */ 
/*      */         
/* 1078 */         .yRot(-getYRot() * 0.017453292F));
/*      */   }
/*      */ 
/*      */   
/* 1082 */   public int getInventoryColumns() { return 0; }
/*      */   
/*      */   private class MountPanicGoal
/*      */     extends PanicGoal
/*      */   {
/* 1087 */     public MountPanicGoal(double speedModifier) { super(AbstractHorse.this, speedModifier); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1092 */     public boolean shouldPanic() { return (!AbstractHorse.this.isMobControlled() && super.shouldPanic()); }
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\equine\AbstractHorse.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */