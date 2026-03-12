/*     */ package net.minecraft.world.entity.animal.allay;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.GameEventTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.control.FlyingMoveControl;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.DynamicGameEventListener;
/*     */ import net.minecraft.world.level.gameevent.EntityPositionSource;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Allay
/*     */   extends PathfinderMob
/*     */   implements InventoryCarrier, VibrationSystem
/*     */ {
/*  75 */   private static final Vec3i ITEM_PICKUP_REACH = new Vec3i(1, 1, 1);
/*     */   
/*     */   private static final int LIFTING_ITEM_ANIMATION_DURATION = 5;
/*     */   
/*     */   private static final float DANCING_LOOP_DURATION = 55.0F;
/*     */   private static final float SPINNING_ANIMATION_DURATION = 15.0F;
/*     */   private static final int DEFAULT_DUPLICATION_COOLDOWN = 0;
/*     */   private static final int DUPLICATION_COOLDOWN_TICKS = 6000;
/*     */   private static final int NUM_OF_DUPLICATION_HEARTS = 3;
/*     */   public static final int MAX_NOTEBLOCK_DISTANCE = 1024;
/*  85 */   private static final EntityDataAccessor<Boolean> DATA_DANCING = SynchedEntityData.defineId(Allay.class, EntityDataSerializers.BOOLEAN);
/*  86 */   private static final EntityDataAccessor<Boolean> DATA_CAN_DUPLICATE = SynchedEntityData.defineId(Allay.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  88 */   protected static final ImmutableList<SensorType<? extends Sensor<? super Allay>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.NEAREST_ITEMS);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.PATH, MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.HURT_BY, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.LIKED_PLAYER, MemoryModuleType.LIKED_NOTEBLOCK_POSITION, MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryModuleType.IS_PANICKING, new MemoryModuleType[0]);
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
/* 110 */   public static final ImmutableList<Float> THROW_SOUND_PITCHES = ImmutableList.of(
/* 111 */       Float.valueOf(0.5625F), 
/* 112 */       Float.valueOf(0.625F), 
/* 113 */       Float.valueOf(0.75F), 
/* 114 */       Float.valueOf(0.9375F), 
/* 115 */       Float.valueOf(1.0F), 
/* 116 */       Float.valueOf(1.0F), 
/* 117 */       Float.valueOf(1.125F), 
/* 118 */       Float.valueOf(1.25F), 
/* 119 */       Float.valueOf(1.5F), 
/* 120 */       Float.valueOf(1.875F), 
/* 121 */       Float.valueOf(2.0F), 
/* 122 */       Float.valueOf(2.25F), new Float[] {
/* 123 */         Float.valueOf(2.5F), 
/* 124 */         Float.valueOf(3.0F), 
/* 125 */         Float.valueOf(3.75F), 
/* 126 */         Float.valueOf(4.0F)
/*     */       });
/*     */   
/*     */   private final DynamicGameEventListener<VibrationSystem.Listener> dynamicVibrationListener;
/*     */   
/*     */   private VibrationSystem.Data vibrationData;
/*     */   
/*     */   private final VibrationSystem.User vibrationUser;
/*     */   private final DynamicGameEventListener<JukeboxListener> dynamicJukeboxListener;
/* 135 */   private final SimpleContainer inventory = new SimpleContainer(1);
/*     */   private BlockPos jukeboxPos;
/* 137 */   private long duplicationCooldown = 0L;
/*     */   
/*     */   private float holdingItemAnimationTicks;
/*     */   private float holdingItemAnimationTicks0;
/*     */   private float dancingAnimationTicks;
/*     */   private float spinningAnimationTicks;
/*     */   private float spinningAnimationTicks0;
/*     */   
/*     */   public Allay(EntityType<? extends Allay> type, Level level) {
/* 146 */     super(type, level);
/* 147 */     this.moveControl = new FlyingMoveControl(this, 20, true);
/* 148 */     setCanPickUpLoot(canPickUpLoot());
/*     */     
/* 150 */     this.vibrationUser = new VibrationUser();
/* 151 */     this.vibrationData = new VibrationSystem.Data();
/* 152 */     this.dynamicVibrationListener = new DynamicGameEventListener(new VibrationSystem.Listener(this));
/* 153 */     this.dynamicJukeboxListener = new DynamicGameEventListener(new JukeboxListener(this, this.vibrationUser.getPositionSource(), ((GameEvent)GameEvent.JUKEBOX_PLAY.value()).notificationRadius()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 158 */   protected Brain.Provider<Allay> brainProvider() { return Brain.provider(MEMORY_TYPES, SENSOR_TYPES); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 163 */   protected Brain<?> makeBrain(Dynamic<?> input) { return AllayAi.makeBrain(brainProvider().makeBrain(input)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   public Brain<Allay> getBrain() { return super.getBrain(); }
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 173 */     return Mob.createMobAttributes()
/* 174 */       .add(Attributes.MAX_HEALTH, 20.0D)
/* 175 */       .add(Attributes.FLYING_SPEED, 0.10000000149011612D)
/* 176 */       .add(Attributes.MOVEMENT_SPEED, 0.10000000149011612D)
/* 177 */       .add(Attributes.ATTACK_DAMAGE, 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/* 182 */     FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level);
/* 183 */     flyingPathNavigation.setCanOpenDoors(false);
/* 184 */     flyingPathNavigation.setCanFloat(true);
/* 185 */     flyingPathNavigation.setRequiredPathLength(48.0F);
/* 186 */     return flyingPathNavigation;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 191 */     super.defineSynchedData(entityData);
/* 192 */     entityData.define(DATA_DANCING, Boolean.valueOf(false));
/* 193 */     entityData.define(DATA_CAN_DUPLICATE, Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 198 */   public void travel(Vec3 input) { travelFlying(input, getSpeed()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 203 */     if (isLikedPlayer(source.getEntity())) {
/* 204 */       return false;
/*     */     }
/* 206 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 211 */   protected boolean considersEntityAsAlly(Entity other) { return (isLikedPlayer(other) || super.considersEntityAsAlly(other)); }
/*     */ 
/*     */   
/*     */   private boolean isLikedPlayer(Entity other) {
/* 215 */     if (other instanceof Player) { Player player = (Player)other;
/* 216 */       Optional<UUID> likedPlayer = getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
/* 217 */       return (likedPlayer.isPresent() && player.getUUID().equals(likedPlayer.get())); }
/*     */     
/* 219 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {}
/*     */ 
/*     */ 
/*     */   
/* 234 */   protected SoundEvent getAmbientSound() { return hasItemInSlot(EquipmentSlot.MAINHAND) ? SoundEvents.ALLAY_AMBIENT_WITH_ITEM : SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 239 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ALLAY_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 244 */   protected SoundEvent getDeathSound() { return SoundEvents.ALLAY_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 249 */   protected float getSoundVolume() { return 0.4F; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 254 */     ProfilerFiller profiler = Profiler.get();
/* 255 */     profiler.push("allayBrain");
/* 256 */     getBrain().tick(level, this);
/* 257 */     profiler.pop();
/*     */     
/* 259 */     profiler.push("allayActivityUpdate");
/* 260 */     AllayAi.updateActivity(this);
/* 261 */     profiler.pop();
/*     */     
/* 263 */     super.customServerAiStep(level);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 268 */     super.aiStep();
/*     */     
/* 270 */     if (!level().isClientSide() && isAlive() && this.tickCount % 10 == 0) {
/* 271 */       heal(1.0F);
/*     */     }
/*     */     
/* 274 */     if (isDancing() && shouldStopDancing() && this.tickCount % 20 == 0) {
/* 275 */       setDancing(false);
/* 276 */       this.jukeboxPos = null;
/*     */     } 
/* 278 */     updateDuplicationCooldown();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 283 */     super.tick();
/*     */     
/* 285 */     if (level().isClientSide()) {
/* 286 */       this.holdingItemAnimationTicks0 = this.holdingItemAnimationTicks;
/* 287 */       if (hasItemInHand()) {
/* 288 */         this.holdingItemAnimationTicks = Mth.clamp(this.holdingItemAnimationTicks + 1.0F, 0.0F, 5.0F);
/*     */       } else {
/* 290 */         this.holdingItemAnimationTicks = Mth.clamp(this.holdingItemAnimationTicks - 1.0F, 0.0F, 5.0F);
/*     */       } 
/*     */       
/* 293 */       if (isDancing()) {
/* 294 */         this.dancingAnimationTicks++;
/* 295 */         this.spinningAnimationTicks0 = this.spinningAnimationTicks;
/* 296 */         if (isSpinning()) {
/* 297 */           this.spinningAnimationTicks++;
/*     */         } else {
/* 299 */           this.spinningAnimationTicks--;
/*     */         } 
/* 301 */         this.spinningAnimationTicks = Mth.clamp(this.spinningAnimationTicks, 0.0F, 15.0F);
/*     */       } else {
/* 303 */         this.dancingAnimationTicks = 0.0F;
/* 304 */         this.spinningAnimationTicks = 0.0F;
/* 305 */         this.spinningAnimationTicks0 = 0.0F;
/*     */       } 
/*     */     } else {
/* 308 */       VibrationSystem.Ticker.tick(level(), this.vibrationData, this.vibrationUser);
/* 309 */       if (isPanicking()) {
/* 310 */         setDancing(false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 317 */   public boolean canPickUpLoot() { return (!isOnPickupCooldown() && hasItemInHand()); }
/*     */ 
/*     */ 
/*     */   
/* 321 */   public boolean hasItemInHand() { return !getItemInHand(InteractionHand.MAIN_HAND).isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 326 */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) { return false; }
/*     */ 
/*     */ 
/*     */   
/* 330 */   private boolean isOnPickupCooldown() { return getBrain().checkMemory(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryStatus.VALUE_PRESENT); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 335 */     ItemStack interactionItem = player.getItemInHand(hand);
/* 336 */     ItemStack itemInHand = getItemInHand(InteractionHand.MAIN_HAND);
/*     */     
/* 338 */     if (isDancing() && interactionItem.is(ItemTags.DUPLICATES_ALLAYS) && canDuplicate()) {
/* 339 */       duplicateAllay();
/* 340 */       level().broadcastEntityEvent(this, (byte)18);
/* 341 */       level().playSound(player, this, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.NEUTRAL, 2.0F, 1.0F);
/* 342 */       removeInteractionItem(player, interactionItem);
/* 343 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 346 */     if (itemInHand.isEmpty() && !interactionItem.isEmpty()) {
/* 347 */       ItemStack itemToGive = interactionItem.copyWithCount(1);
/* 348 */       setItemInHand(InteractionHand.MAIN_HAND, itemToGive);
/* 349 */       removeInteractionItem(player, interactionItem);
/* 350 */       level().playSound(player, this, SoundEvents.ALLAY_ITEM_GIVEN, SoundSource.NEUTRAL, 2.0F, 1.0F);
/* 351 */       getBrain().setMemory(MemoryModuleType.LIKED_PLAYER, player.getUUID());
/*     */       
/* 353 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 356 */     if (!itemInHand.isEmpty() && hand == InteractionHand.MAIN_HAND && interactionItem.isEmpty()) {
/* 357 */       setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 358 */       level().playSound(player, this, SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.NEUTRAL, 2.0F, 1.0F);
/* 359 */       swing(InteractionHand.MAIN_HAND);
/* 360 */       for (ItemStack itemStack : getInventory().removeAllItems()) {
/* 361 */         BehaviorUtils.throwItem(this, itemStack, position());
/*     */       }
/* 363 */       getBrain().eraseMemory(MemoryModuleType.LIKED_PLAYER);
/* 364 */       player.addItem(itemInHand);
/*     */       
/* 366 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 369 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   public void setJukeboxPlaying(BlockPos jukebox, boolean isPlaying) {
/* 373 */     if (isPlaying) {
/* 374 */       if (!isDancing()) {
/* 375 */         this.jukeboxPos = jukebox;
/* 376 */         setDancing(true);
/*     */       } 
/* 378 */     } else if (jukebox.equals(this.jukeboxPos) || this.jukeboxPos == null) {
/* 379 */       this.jukeboxPos = null;
/* 380 */       setDancing(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 386 */   public SimpleContainer getInventory() { return this.inventory; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 391 */   protected Vec3i getPickupReach() { return ITEM_PICKUP_REACH; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
/* 396 */     ItemStack itemInHand = getItemInHand(InteractionHand.MAIN_HAND);
/* 397 */     return (!itemInHand.isEmpty() && ((Boolean)level
/* 398 */       .getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() && this.inventory
/* 399 */       .canAddItem(itemStack) && 
/* 400 */       allayConsidersItemEqual(itemInHand, itemStack));
/*     */   }
/*     */ 
/*     */   
/* 404 */   private boolean allayConsidersItemEqual(ItemStack item1, ItemStack item2) { return (ItemStack.isSameItem(item1, item2) && !hasNonMatchingPotion(item1, item2)); }
/*     */ 
/*     */   
/*     */   private boolean hasNonMatchingPotion(ItemStack itemInHand, ItemStack pickupItem) {
/* 408 */     PotionContents potionInHand = (PotionContents)itemInHand.get(DataComponents.POTION_CONTENTS);
/* 409 */     PotionContents potionInPickupItem = (PotionContents)pickupItem.get(DataComponents.POTION_CONTENTS);
/* 410 */     return !Objects.equals(potionInHand, potionInPickupItem);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 415 */   protected void pickUpItem(ServerLevel level, ItemEntity entity) { InventoryCarrier.pickUpItem(level, this, this, entity); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 420 */   public boolean isFlapping() { return !onGround(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> action) {
/* 425 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 426 */       action.accept(this.dynamicVibrationListener, serverLevel);
/* 427 */       action.accept(this.dynamicJukeboxListener, serverLevel); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/* 432 */   public boolean isDancing() { return ((Boolean)this.entityData.get(DATA_DANCING)).booleanValue(); }
/*     */ 
/*     */   
/*     */   public void setDancing(boolean isDancing) {
/* 436 */     if (level().isClientSide() || !isEffectiveAi() || (isDancing && isPanicking())) {
/*     */       return;
/*     */     }
/* 439 */     this.entityData.set(DATA_DANCING, Boolean.valueOf(isDancing));
/*     */   }
/*     */   
/*     */   private boolean shouldStopDancing() {
/* 443 */     return (this.jukeboxPos == null || 
/* 444 */       !this.jukeboxPos.closerToCenterThan(position(), ((GameEvent)GameEvent.JUKEBOX_PLAY.value()).notificationRadius()) || 
/* 445 */       !level().getBlockState(this.jukeboxPos).is(Blocks.JUKEBOX));
/*     */   }
/*     */ 
/*     */   
/* 449 */   public float getHoldingItemAnimationProgress(float a) { return Mth.lerp(a, this.holdingItemAnimationTicks0, this.holdingItemAnimationTicks) / 5.0F; }
/*     */ 
/*     */   
/*     */   public boolean isSpinning() {
/* 453 */     float spinningProgress = this.dancingAnimationTicks % 55.0F;
/* 454 */     return (spinningProgress < 15.0F);
/*     */   }
/*     */ 
/*     */   
/* 458 */   public float getSpinningProgress(float a) { return Mth.lerp(a, this.spinningAnimationTicks0, this.spinningAnimationTicks) / 15.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 463 */   public boolean equipmentHasChanged(ItemStack previous, ItemStack current) { return !allayConsidersItemEqual(previous, current); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dropEquipment(ServerLevel level) {
/* 468 */     super.dropEquipment(level);
/* 469 */     this.inventory.removeAllItems().forEach(stack -> spawnAtLocation(level, stack));
/*     */ 
/*     */ 
/*     */     
/* 473 */     ItemStack itemStack = getItemBySlot(EquipmentSlot.MAINHAND);
/* 474 */     if (!itemStack.isEmpty() && !EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
/* 475 */       spawnAtLocation(level, itemStack);
/* 476 */       setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 482 */   public boolean removeWhenFarAway(double distSqr) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 487 */     super.addAdditionalSaveData(output);
/*     */     
/* 489 */     writeInventoryToTag(output);
/*     */     
/* 491 */     output.store("listener", VibrationSystem.Data.CODEC, this.vibrationData);
/*     */     
/* 493 */     output.putLong("DuplicationCooldown", this.duplicationCooldown);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 498 */     super.readAdditionalSaveData(input);
/*     */     
/* 500 */     readInventoryFromTag(input);
/*     */     
/* 502 */     this.vibrationData = (VibrationSystem.Data)input.read("listener", VibrationSystem.Data.CODEC).orElseGet(net.minecraft.world.level.gameevent.vibrations.VibrationSystem.Data::new);
/*     */     
/* 504 */     setDuplicationCooldown(input.getIntOr("DuplicationCooldown", 0));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 509 */   protected boolean shouldStayCloseToLeashHolder() { return false; }
/*     */ 
/*     */   
/*     */   private void updateDuplicationCooldown() {
/* 513 */     if (!level().isClientSide() && this.duplicationCooldown > 0L) {
/* 514 */       setDuplicationCooldown(this.duplicationCooldown - 1L);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setDuplicationCooldown(long duplicationCooldown) {
/* 519 */     this.duplicationCooldown = duplicationCooldown;
/* 520 */     this.entityData.set(DATA_CAN_DUPLICATE, Boolean.valueOf((duplicationCooldown == 0L)));
/*     */   }
/*     */   
/*     */   private void duplicateAllay() {
/* 524 */     Allay allay = (Allay)EntityType.ALLAY.create(level(), EntitySpawnReason.BREEDING);
/* 525 */     if (allay != null) {
/* 526 */       allay.snapTo(position());
/* 527 */       allay.setPersistenceRequired();
/* 528 */       allay.resetDuplicationCooldown();
/* 529 */       resetDuplicationCooldown();
/* 530 */       level().addFreshEntity(allay);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 535 */   private void resetDuplicationCooldown() { setDuplicationCooldown(6000L); }
/*     */ 
/*     */ 
/*     */   
/* 539 */   private boolean canDuplicate() { return ((Boolean)this.entityData.get(DATA_CAN_DUPLICATE)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 543 */   private void removeInteractionItem(Player player, ItemStack interactionItem) { interactionItem.consume(1, player); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 548 */   public Vec3 getLeashOffset() { return new Vec3(0.0D, getEyeHeight() * 0.6D, getBbWidth() * 0.1D); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 553 */     if (id == 18) {
/* 554 */       for (int i = 0; i < 3; i++) {
/* 555 */         spawnHeartParticle();
/*     */       }
/*     */     } else {
/* 558 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnHeartParticle() {
/* 563 */     double xd = this.random.nextGaussian() * 0.02D;
/* 564 */     double yd = this.random.nextGaussian() * 0.02D;
/* 565 */     double zd = this.random.nextGaussian() * 0.02D;
/* 566 */     level().addParticle(ParticleTypes.HEART, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), xd, yd, zd);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 571 */   public VibrationSystem.Data getVibrationData() { return this.vibrationData; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 576 */   public VibrationSystem.User getVibrationUser() { return this.vibrationUser; }
/*     */   
/*     */   private class JukeboxListener
/*     */     implements GameEventListener {
/*     */     private final PositionSource listenerSource;
/*     */     private final int listenerRadius;
/*     */     
/*     */     public JukeboxListener(PositionSource listenerSource, int listenerRadius) {
/* 584 */       this.listenerSource = listenerSource;
/* 585 */       this.listenerRadius = listenerRadius;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 590 */     public PositionSource getListenerSource() { return this.listenerSource; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 595 */     public int getListenerRadius() { return this.listenerRadius; }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 sourcePosition) {
/* 600 */       if (event.is(GameEvent.JUKEBOX_PLAY)) {
/* 601 */         Allay.this.setJukeboxPlaying(BlockPos.containing(sourcePosition), true);
/* 602 */         return true;
/*     */       } 
/*     */       
/* 605 */       if (event.is(GameEvent.JUKEBOX_STOP_PLAY)) {
/* 606 */         Allay.this.setJukeboxPlaying(BlockPos.containing(sourcePosition), false);
/* 607 */         return true;
/*     */       } 
/*     */       
/* 610 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private class VibrationUser
/*     */     implements VibrationSystem.User {
/*     */     private static final int VIBRATION_EVENT_LISTENER_RANGE = 16;
/* 617 */     private final PositionSource positionSource = new EntityPositionSource(Allay.this, Allay.this.getEyeHeight());
/*     */ 
/*     */ 
/*     */     
/* 621 */     public int getListenerRadius() { return 16; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 626 */     public PositionSource getPositionSource() { return this.positionSource; }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, GameEvent.Context context) {
/* 631 */       if (Allay.this.isNoAi()) {
/* 632 */         return false;
/*     */       }
/*     */       
/* 635 */       Optional<GlobalPos> maybeGlobalPos = Allay.this.getBrain().getMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
/* 636 */       if (maybeGlobalPos.isEmpty()) {
/* 637 */         return true;
/*     */       }
/* 639 */       GlobalPos globalPos = (GlobalPos)maybeGlobalPos.get();
/* 640 */       return (globalPos.isCloseEnough(level.dimension(), Allay.this.blockPosition(), 1024) && globalPos.pos().equals(pos));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, Entity sourceEntity, Entity projectileOwner, float receivingDistance) {
/* 645 */       if (event.is(GameEvent.NOTE_BLOCK_PLAY)) {
/* 646 */         AllayAi.hearNoteblock(Allay.this, new BlockPos(pos));
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 652 */     public TagKey<GameEvent> getListenableEvents() { return GameEventTags.ALLAY_CAN_LISTEN; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\allay\Allay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */