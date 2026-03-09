/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.CrossbowAttackMob;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.CrossbowItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Piglin
/*     */   extends AbstractPiglin
/*     */   implements CrossbowAttackMob, InventoryCarrier
/*     */ {
/*  69 */   private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);
/*  70 */   private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);
/*  71 */   private static final EntityDataAccessor<Boolean> DATA_IS_DANCING = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  73 */   private static final Identifier SPEED_MODIFIER_BABY_ID = Identifier.withDefaultNamespace("baby");
/*  74 */   private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_ID, 0.20000000298023224D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
/*     */   
/*     */   private static final int MAX_HEALTH = 16;
/*     */   private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.35F;
/*     */   private static final int ATTACK_DAMAGE = 5;
/*     */   private static final float CHANCE_OF_WEARING_EACH_ARMOUR_ITEM = 0.1F;
/*     */   private static final int MAX_PASSENGERS_ON_ONE_HOGLIN = 3;
/*     */   private static final float PROBABILITY_OF_SPAWNING_AS_BABY = 0.2F;
/*  82 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.PIGLIN.getDimensions().scale(0.5F).withEyeHeight(0.97F);
/*     */   
/*     */   private static final double PROBABILITY_OF_SPAWNING_WITH_CROSSBOW_INSTEAD_OF_SWORD = 0.5D;
/*     */   private static final boolean DEFAULT_IS_BABY = false;
/*     */   private static final boolean DEFAULT_CANNOT_HUNT = false;
/*  87 */   private final SimpleContainer inventory = new SimpleContainer(8);
/*     */   
/*     */   private boolean cannotHunt = false;
/*  90 */   protected static final ImmutableList<SensorType<? extends Sensor<? super Piglin>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_SPECIFIC_SENSOR);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, new MemoryModuleType[] { MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.AVOID_TARGET, MemoryModuleType.ADMIRING_ITEM, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleType.CELEBRATE_LOCATION, MemoryModuleType.DANCING, MemoryModuleType.HUNTED_RECENTLY, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.RIDE_TARGET, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.ATE_RECENTLY, MemoryModuleType.NEAREST_REPELLENT, MemoryModuleType.SPEAR_FLEEING_TIME, MemoryModuleType.SPEAR_FLEEING_POSITION, MemoryModuleType.SPEAR_CHARGE_POSITION, MemoryModuleType.SPEAR_ENGAGE_TIME, MemoryModuleType.SPEAR_STATUS });
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
/*     */   public Piglin(EntityType<? extends AbstractPiglin> type, Level level) {
/* 146 */     super(type, level);
/* 147 */     this.xpReward = 5;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 152 */     super.addAdditionalSaveData(output);
/*     */     
/* 154 */     output.putBoolean("IsBaby", isBaby());
/* 155 */     output.putBoolean("CannotHunt", this.cannotHunt);
/* 156 */     writeInventoryToTag(output);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 161 */     super.readAdditionalSaveData(input);
/*     */     
/* 163 */     setBaby(input.getBooleanOr("IsBaby", false));
/* 164 */     setCannotHunt(input.getBooleanOr("CannotHunt", false));
/* 165 */     readInventoryFromTag(input);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/* 171 */   public SimpleContainer getInventory() { return this.inventory; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
/* 176 */     super.dropCustomDeathLoot(level, source, killedByPlayer);
/* 177 */     this.inventory.removeAllItems().forEach(itemStack -> spawnAtLocation(level, itemStack));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 184 */   protected ItemStack addToInventory(ItemStack itemStack) { return this.inventory.addItem(itemStack); }
/*     */ 
/*     */ 
/*     */   
/* 188 */   protected boolean canAddToInventory(ItemStack itemStack) { return this.inventory.canAddItem(itemStack); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 193 */     super.defineSynchedData(entityData);
/* 194 */     entityData.define(DATA_BABY_ID, Boolean.valueOf(false));
/* 195 */     entityData.define(DATA_IS_CHARGING_CROSSBOW, Boolean.valueOf(false));
/* 196 */     entityData.define(DATA_IS_DANCING, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 201 */     super.onSyncedDataUpdated(accessor);
/* 202 */     if (DATA_BABY_ID.equals(accessor)) {
/* 203 */       refreshDimensions();
/*     */     }
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 208 */     return Monster.createMonsterAttributes()
/* 209 */       .add(Attributes.MAX_HEALTH, 16.0D)
/* 210 */       .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355D)
/* 211 */       .add(Attributes.ATTACK_DAMAGE, 5.0D);
/*     */   }
/*     */ 
/*     */   
/* 215 */   public static boolean checkPiglinSpawnRules(EntityType<Piglin> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) { return !level.getBlockState(pos.below()).is(Blocks.NETHER_WART_BLOCK); }
/*     */ 
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 220 */     RandomSource random = level.getRandom();
/* 221 */     if (spawnReason != EntitySpawnReason.STRUCTURE) {
/* 222 */       if (random.nextFloat() < 0.2F) {
/* 223 */         setBaby(true);
/* 224 */       } else if (isAdult()) {
/* 225 */         setItemSlot(EquipmentSlot.MAINHAND, createSpawnWeapon());
/*     */       } 
/*     */     }
/* 228 */     PiglinAi.initMemories(this, level.getRandom());
/* 229 */     populateDefaultEquipmentSlots(random, difficulty);
/* 230 */     populateDefaultEquipmentEnchantments(level, random, difficulty);
/* 231 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 236 */   public boolean removeWhenFarAway(double distSqr) { return !isPersistenceRequired(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/* 241 */     if (isAdult()) {
/* 242 */       maybeWearArmor(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET), random);
/* 243 */       maybeWearArmor(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE), random);
/* 244 */       maybeWearArmor(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS), random);
/* 245 */       maybeWearArmor(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS), random);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void maybeWearArmor(EquipmentSlot slot, ItemStack itemStack, RandomSource random) {
/* 250 */     if (random.nextFloat() < 0.1F) {
/* 251 */       setItemSlot(slot, itemStack);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 257 */   protected Brain.Provider<Piglin> brainProvider() { return Brain.provider(MEMORY_TYPES, SENSOR_TYPES); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 262 */   protected Brain<?> makeBrain(Dynamic<?> input) { return PiglinAi.makeBrain(this, brainProvider().makeBrain(input)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 268 */   public Brain<Piglin> getBrain() { return super.getBrain(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 273 */     InteractionResult interactionResult = super.mobInteract(player, hand);
/* 274 */     if (interactionResult.consumesAction()) {
/* 275 */       return interactionResult;
/*     */     }
/* 277 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/* 278 */       return PiglinAi.mobInteract(level, this, player, hand); }
/*     */     
/* 280 */     boolean canAdmire = (PiglinAi.canAdmire(this, player.getItemInHand(hand)) && getArmPose() != PiglinArmPose.ADMIRING_ITEM);
/* 281 */     return canAdmire ? InteractionResult.SUCCESS : InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 286 */   public EntityDimensions getDefaultDimensions(Pose pose) { return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBaby(boolean baby) {
/* 291 */     getEntityData().set(DATA_BABY_ID, Boolean.valueOf(baby));
/*     */     
/* 293 */     if (!level().isClientSide()) {
/* 294 */       AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
/* 295 */       speed.removeModifier(SPEED_MODIFIER_BABY.id());
/* 296 */       if (baby) {
/* 297 */         speed.addTransientModifier(SPEED_MODIFIER_BABY);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 304 */   public boolean isBaby() { return ((Boolean)getEntityData().get(DATA_BABY_ID)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 308 */   private void setCannotHunt(boolean cannotHunt) { this.cannotHunt = cannotHunt; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 313 */   protected boolean canHunt() { return !this.cannotHunt; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 318 */     ProfilerFiller profiler = Profiler.get();
/* 319 */     profiler.push("piglinBrain");
/* 320 */     getBrain().tick(level, this);
/* 321 */     profiler.pop();
/*     */     
/* 323 */     PiglinAi.updateActivity(this);
/*     */     
/* 325 */     super.customServerAiStep(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 330 */   protected int getBaseExperienceReward(ServerLevel level) { return this.xpReward; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finishConversion(ServerLevel level) {
/* 335 */     PiglinAi.cancelAdmiring(level, this);
/* 336 */     this.inventory.removeAllItems().forEach(itemStack -> spawnAtLocation(level, itemStack));
/* 337 */     super.finishConversion(level);
/*     */   }
/*     */   
/*     */   private ItemStack createSpawnWeapon() {
/* 341 */     if (this.random.nextFloat() < 0.5D) {
/* 342 */       return new ItemStack(Items.CROSSBOW);
/*     */     }
/* 344 */     return new ItemStack((this.random.nextInt(10) == 0) ? Items.GOLDEN_SPEAR : Items.GOLDEN_SWORD);
/*     */   }
/*     */ 
/*     */   
/*     */   public TagKey<Item> getPreferredWeaponType() {
/* 349 */     if (isBaby()) {
/* 350 */       return null;
/*     */     }
/* 352 */     return ItemTags.PIGLIN_PREFERRED_WEAPONS;
/*     */   }
/*     */ 
/*     */   
/* 356 */   private boolean isChargingCrossbow() { return ((Boolean)this.entityData.get(DATA_IS_CHARGING_CROSSBOW)).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 361 */   public void setChargingCrossbow(boolean isCharging) { this.entityData.set(DATA_IS_CHARGING_CROSSBOW, Boolean.valueOf(isCharging)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 366 */   public void onCrossbowAttackPerformed() { this.noActionTime = 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PiglinArmPose getArmPose() {
/* 372 */     if (isDancing())
/* 373 */       return PiglinArmPose.DANCING; 
/* 374 */     if (PiglinAi.isLovedItem(getOffhandItem()))
/* 375 */       return PiglinArmPose.ADMIRING_ITEM; 
/* 376 */     if (isAggressive() && isHoldingMeleeWeapon())
/* 377 */       return PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON; 
/* 378 */     if (isChargingCrossbow())
/* 379 */       return PiglinArmPose.CROSSBOW_CHARGE; 
/* 380 */     if (isHolding(Items.CROSSBOW) && CrossbowItem.isCharged(getWeaponItem())) {
/* 381 */       return PiglinArmPose.CROSSBOW_HOLD;
/*     */     }
/* 383 */     return PiglinArmPose.DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 388 */   public boolean isDancing() { return ((Boolean)this.entityData.get(DATA_IS_DANCING)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 392 */   public void setDancing(boolean dancing) { this.entityData.set(DATA_IS_DANCING, Boolean.valueOf(dancing)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 397 */     boolean wasHurt = super.hurtServer(level, source, damage);
/* 398 */     if (wasHurt) { Entity entity = source.getEntity(); if (entity instanceof LivingEntity) { LivingEntity sourceEntity = (LivingEntity)entity;
/* 399 */         PiglinAi.wasHurtBy(level, this, sourceEntity); }
/*     */        }
/* 401 */      return wasHurt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 406 */   public void performRangedAttack(LivingEntity target, float power) { performCrossbowAttack(this, 1.6F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 411 */   public boolean canUseNonMeleeWeapon(ItemStack item) { return (item.getItem() == Items.CROSSBOW || item.has(DataComponents.KINETIC_WEAPON)); }
/*     */ 
/*     */ 
/*     */   
/* 415 */   protected void holdInMainHand(ItemStack itemStack) { setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, itemStack); }
/*     */ 
/*     */   
/*     */   protected void holdInOffHand(ItemStack itemStack) {
/* 419 */     if (itemStack.is(PiglinAi.BARTERING_ITEM)) {
/*     */       
/* 421 */       setItemSlot(EquipmentSlot.OFFHAND, itemStack);
/* 422 */       setGuaranteedDrop(EquipmentSlot.OFFHAND);
/*     */     } else {
/* 424 */       setItemSlotAndDropWhenKilled(EquipmentSlot.OFFHAND, itemStack);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 430 */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) { return (((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() && canPickUpLoot() && PiglinAi.wantsToPickup(this, itemStack)); }
/*     */ 
/*     */   
/*     */   protected boolean canReplaceCurrentItem(ItemStack newItemStack) {
/* 434 */     EquipmentSlot slot = getEquipmentSlotForItem(newItemStack);
/* 435 */     ItemStack currentItemStackInCorrespondingSlot = getItemBySlot(slot);
/* 436 */     return canReplaceCurrentItem(newItemStack, currentItemStackInCorrespondingSlot, slot);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canReplaceCurrentItem(ItemStack newItemStack, ItemStack currentItemStack, EquipmentSlot slot) {
/* 441 */     if (EnchantmentHelper.has(currentItemStack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) {
/* 442 */       return false;
/*     */     }
/*     */     
/* 445 */     TagKey<Item> preferredWeaponType = getPreferredWeaponType();
/*     */ 
/*     */ 
/*     */     
/* 449 */     boolean newItemWanted = (PiglinAi.isLovedItem(newItemStack) || (preferredWeaponType != null && newItemStack.is(preferredWeaponType)));
/* 450 */     boolean currentItemWanted = (PiglinAi.isLovedItem(currentItemStack) || (preferredWeaponType != null && currentItemStack.is(preferredWeaponType)));
/*     */ 
/*     */ 
/*     */     
/* 454 */     if (newItemWanted && !currentItemWanted) {
/* 455 */       return true;
/*     */     }
/* 457 */     if (!newItemWanted && currentItemWanted) {
/* 458 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 462 */     return super.canReplaceCurrentItem(newItemStack, currentItemStack, slot);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/* 467 */     onItemPickup(entity);
/* 468 */     PiglinAi.pickUpItem(level, this, entity);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean startRiding(Entity entityToRide, boolean force, boolean sendEventAndTriggers) {
/* 473 */     if (isBaby() && entityToRide.getType() == EntityType.HOGLIN) {
/* 474 */       entityToRide = getTopPassenger(entityToRide, 3);
/*     */     }
/* 476 */     return super.startRiding(entityToRide, force, sendEventAndTriggers);
/*     */   }
/*     */   
/*     */   private Entity getTopPassenger(Entity vehicle, int counter) {
/* 480 */     List<Entity> passengers = vehicle.getPassengers();
/* 481 */     if (counter == 1 || passengers.isEmpty()) {
/* 482 */       return vehicle;
/*     */     }
/* 484 */     return getTopPassenger((Entity)passengers.getFirst(), counter - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 490 */     if (level().isClientSide()) {
/* 491 */       return null;
/*     */     }
/* 493 */     return (SoundEvent)PiglinAi.getSoundForCurrentActivity(this).orElse(null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 498 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.PIGLIN_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 503 */   protected SoundEvent getDeathSound() { return SoundEvents.PIGLIN_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 508 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.PIGLIN_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 513 */   protected void playConvertedSound() { makeSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\piglin\Piglin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */