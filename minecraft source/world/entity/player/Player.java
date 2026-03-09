/*      */ package net.minecraft.world.entity.player;
/*      */ 
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.datafixers.util.Either;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.OptionalInt;
/*      */ import java.util.function.Predicate;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.GlobalPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.NbtUtils;
/*      */ import net.minecraft.network.chat.ClickEvent;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.Style;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.dialog.Dialog;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.server.permissions.PermissionSet;
/*      */ import net.minecraft.server.permissions.Permissions;
/*      */ import net.minecraft.server.players.NameAndId;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.stats.Stat;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.DamageTypeTags;
/*      */ import net.minecraft.tags.EntityTypeTags;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.Unit;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.ItemStackWithSlot;
/*      */ import net.minecraft.world.MenuProvider;
/*      */ import net.minecraft.world.attribute.BedRule;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.effect.MobEffectUtil;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.Avatar;
/*      */ import net.minecraft.world.entity.ContainerUser;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityEquipment;
/*      */ import net.minecraft.world.entity.EntityReference;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.HumanoidArm;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.MoverType;
/*      */ import net.minecraft.world.entity.Pose;
/*      */ import net.minecraft.world.entity.SlotAccess;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*      */ import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
/*      */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*      */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*      */ import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
/*      */ import net.minecraft.world.entity.decoration.ArmorStand;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.monster.warden.WardenSpawnTracker;
/*      */ import net.minecraft.world.entity.projectile.FishingHook;
/*      */ import net.minecraft.world.entity.projectile.Projectile;
/*      */ import net.minecraft.world.entity.projectile.ProjectileDeflection;
/*      */ import net.minecraft.world.entity.vehicle.minecart.MinecartCommandBlock;
/*      */ import net.minecraft.world.food.FoodData;
/*      */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*      */ import net.minecraft.world.inventory.ClickAction;
/*      */ import net.minecraft.world.inventory.InventoryMenu;
/*      */ import net.minecraft.world.inventory.PlayerEnderChestContainer;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemCooldowns;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.ProjectileWeaponItem;
/*      */ import net.minecraft.world.item.component.BlocksAttacks;
/*      */ import net.minecraft.world.item.crafting.Recipe;
/*      */ import net.minecraft.world.item.crafting.RecipeHolder;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.equipment.Equippable;
/*      */ import net.minecraft.world.item.trading.MerchantOffers;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
/*      */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.TestBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.scores.PlayerTeam;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Player
/*      */   extends Avatar
/*      */   implements ContainerUser
/*      */ {
/*      */   public static final int MAX_HEALTH = 20;
/*      */   public static final int SLEEP_DURATION = 100;
/*      */   public static final int WAKE_UP_DURATION = 10;
/*      */   public static final int ENDER_SLOT_OFFSET = 200;
/*      */   public static final int HELD_ITEM_SLOT = 499;
/*      */   public static final int CRAFTING_SLOT_OFFSET = 500;
/*      */   public static final float DEFAULT_BLOCK_INTERACTION_RANGE = 4.5F;
/*      */   public static final float DEFAULT_ENTITY_INTERACTION_RANGE = 3.0F;
/*      */   private static final int CURRENT_IMPULSE_CONTEXT_RESET_GRACE_TIME_TICKS = 40;
/*  144 */   private static final EntityDataAccessor<Float> DATA_PLAYER_ABSORPTION_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.FLOAT);
/*  145 */   private static final EntityDataAccessor<Integer> DATA_SCORE_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);
/*      */   
/*  147 */   private static final EntityDataAccessor<OptionalInt> DATA_SHOULDER_PARROT_LEFT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
/*  148 */   private static final EntityDataAccessor<OptionalInt> DATA_SHOULDER_PARROT_RIGHT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
/*      */   
/*      */   private static final short DEFAULT_SLEEP_TIMER = 0;
/*      */   
/*      */   private static final float DEFAULT_EXPERIENCE_PROGRESS = 0.0F;
/*      */   private static final int DEFAULT_EXPERIENCE_LEVEL = 0;
/*      */   private static final int DEFAULT_TOTAL_EXPERIENCE = 0;
/*      */   private static final int NO_ENCHANTMENT_SEED = 0;
/*      */   private static final int DEFAULT_SELECTED_SLOT = 0;
/*      */   private static final int DEFAULT_SCORE = 0;
/*      */   private static final boolean DEFAULT_IGNORE_FALL_DAMAGE_FROM_CURRENT_IMPULSE = false;
/*      */   private static final int DEFAULT_CURRENT_IMPULSE_CONTEXT_RESET_GRACE_TIME = 0;
/*      */   public static final float CREATIVE_ENTITY_INTERACTION_RANGE_MODIFIER_VALUE = 2.0F;
/*      */   private final Inventory inventory;
/*  162 */   protected PlayerEnderChestContainer enderChestInventory = new PlayerEnderChestContainer();
/*      */   
/*      */   public final InventoryMenu inventoryMenu;
/*      */   public AbstractContainerMenu containerMenu;
/*  166 */   protected FoodData foodData = new FoodData();
/*      */   
/*      */   protected int jumpTriggerTime;
/*      */   
/*      */   public int takeXpDelay;
/*      */   
/*  172 */   private int sleepCounter = 0;
/*      */   
/*      */   protected boolean wasUnderwater;
/*      */   
/*  176 */   private final Abilities abilities = new Abilities();
/*      */   
/*  178 */   public int experienceLevel = 0;
/*  179 */   public int totalExperience = 0;
/*  180 */   public float experienceProgress = 0.0F;
/*  181 */   protected int enchantmentSeed = 0;
/*      */   
/*  183 */   protected final float defaultFlySpeed = 0.02F;
/*      */   private int lastLevelUpTime;
/*      */   private final GameProfile gameProfile;
/*      */   private boolean reducedDebugInfo;
/*  187 */   private ItemStack lastItemInMainHand = ItemStack.EMPTY;
/*  188 */   private final ItemCooldowns cooldowns = createItemCooldowns();
/*  189 */   private Optional<GlobalPos> lastDeathLocation = Optional.empty();
/*      */   
/*      */   public FishingHook fishing;
/*      */   
/*      */   protected float hurtDir;
/*      */   
/*      */   public Vec3 currentImpulseImpactPos;
/*      */   
/*      */   public Entity currentExplosionCause;
/*      */   
/*      */   private boolean ignoreFallDamageFromCurrentImpulse = false;
/*  200 */   private int currentImpulseContextResetGraceTime = 0;
/*      */   
/*      */   public Player(Level level, GameProfile gameProfile) {
/*  203 */     super(EntityType.PLAYER, level);
/*  204 */     setUUID(gameProfile.id());
/*      */     
/*  206 */     this.gameProfile = gameProfile;
/*      */     
/*  208 */     this.inventory = new Inventory(this, this.equipment);
/*  209 */     this.inventoryMenu = new InventoryMenu(this.inventory, !level.isClientSide(), this);
/*  210 */     this.containerMenu = this.inventoryMenu;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  215 */   protected EntityEquipment createEquipment() { return new PlayerEquipment(this); }
/*      */ 
/*      */   
/*      */   public boolean blockActionRestricted(Level level, BlockPos pos, GameType gameType) {
/*  219 */     if (!gameType.isBlockPlacingRestricted()) {
/*  220 */       return false;
/*      */     }
/*  222 */     if (gameType == GameType.SPECTATOR) {
/*  223 */       return true;
/*      */     }
/*  225 */     if (mayBuild()) {
/*  226 */       return false;
/*      */     }
/*  228 */     ItemStack itemStack = getMainHandItem();
/*  229 */     return (itemStack.isEmpty() || !itemStack.canBreakBlockInAdventureMode(new BlockInWorld(level, pos, false)));
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createAttributes() {
/*  233 */     return LivingEntity.createLivingAttributes()
/*  234 */       .add(Attributes.ATTACK_DAMAGE, 1.0D)
/*  235 */       .add(Attributes.MOVEMENT_SPEED, 0.10000000149011612D)
/*  236 */       .add(Attributes.ATTACK_SPEED)
/*  237 */       .add(Attributes.LUCK)
/*  238 */       .add(Attributes.BLOCK_INTERACTION_RANGE, 4.5D)
/*  239 */       .add(Attributes.ENTITY_INTERACTION_RANGE, 3.0D)
/*  240 */       .add(Attributes.BLOCK_BREAK_SPEED)
/*  241 */       .add(Attributes.SUBMERGED_MINING_SPEED)
/*  242 */       .add(Attributes.SNEAKING_SPEED)
/*  243 */       .add(Attributes.MINING_EFFICIENCY)
/*  244 */       .add(Attributes.SWEEPING_DAMAGE_RATIO)
/*  245 */       .add(Attributes.WAYPOINT_TRANSMIT_RANGE, 6.0E7D)
/*  246 */       .add(Attributes.WAYPOINT_RECEIVE_RANGE, 6.0E7D);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  252 */     super.defineSynchedData(entityData);
/*      */     
/*  254 */     entityData.define(DATA_PLAYER_ABSORPTION_ID, Float.valueOf(0.0F));
/*  255 */     entityData.define(DATA_SCORE_ID, Integer.valueOf(0));
/*  256 */     entityData.define(DATA_SHOULDER_PARROT_LEFT, OptionalInt.empty());
/*  257 */     entityData.define(DATA_SHOULDER_PARROT_RIGHT, OptionalInt.empty());
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  262 */     this.noPhysics = isSpectator();
/*  263 */     if (isSpectator() || isPassenger()) {
/*  264 */       setOnGround(false);
/*      */     }
/*      */     
/*  267 */     if (this.takeXpDelay > 0) {
/*  268 */       this.takeXpDelay--;
/*      */     }
/*  270 */     if (isSleeping()) {
/*  271 */       this.sleepCounter++;
/*  272 */       if (this.sleepCounter > 100) {
/*  273 */         this.sleepCounter = 100;
/*      */       }
/*      */       
/*  276 */       if (!level().isClientSide() && !((BedRule)level().environmentAttributes().getValue(EnvironmentAttributes.BED_RULE, position())).canSleep(level())) {
/*  277 */         stopSleepInBed(false, true);
/*      */       }
/*  279 */     } else if (this.sleepCounter > 0) {
/*  280 */       this.sleepCounter++;
/*  281 */       if (this.sleepCounter >= 110) {
/*  282 */         this.sleepCounter = 0;
/*      */       }
/*      */     } 
/*      */     
/*  286 */     updateIsUnderwater();
/*      */     
/*  288 */     super.tick();
/*      */     
/*  290 */     int maxPositionOffset = 29999999;
/*  291 */     double nx = Mth.clamp(getX(), -2.9999999E7D, 2.9999999E7D);
/*  292 */     double nz = Mth.clamp(getZ(), -2.9999999E7D, 2.9999999E7D);
/*  293 */     if (nx != getX() || nz != getZ()) {
/*  294 */       setPos(nx, getY(), nz);
/*      */     }
/*      */     
/*  297 */     this.attackStrengthTicker++;
/*  298 */     this.itemSwapTicker++;
/*      */     
/*  300 */     ItemStack mainHandItemStack = getMainHandItem();
/*  301 */     if (!ItemStack.matches(this.lastItemInMainHand, mainHandItemStack)) {
/*      */ 
/*      */ 
/*      */       
/*  305 */       if (!ItemStack.isSameItem(this.lastItemInMainHand, mainHandItemStack)) {
/*  306 */         resetAttackStrengthTicker();
/*      */       }
/*  308 */       this.lastItemInMainHand = mainHandItemStack.copy();
/*      */     } 
/*      */     
/*  311 */     if (!isEyeInFluid(FluidTags.WATER) && isEquipped(Items.TURTLE_HELMET)) {
/*  312 */       turtleHelmetTick();
/*      */     }
/*  314 */     this.cooldowns.tick();
/*  315 */     updatePlayerPose();
/*      */     
/*  317 */     if (this.currentImpulseContextResetGraceTime > 0) {
/*  318 */       this.currentImpulseContextResetGraceTime--;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected float getMaxHeadRotationRelativeToBody() {
/*  324 */     if (isBlocking()) {
/*  325 */       return 15.0F;
/*      */     }
/*  327 */     return super.getMaxHeadRotationRelativeToBody();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  337 */   public boolean isSecondaryUseActive() { return isShiftKeyDown(); }
/*      */ 
/*      */ 
/*      */   
/*  341 */   protected boolean wantsToStopRiding() { return isShiftKeyDown(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  349 */   protected boolean isStayingOnGroundSurface() { return isShiftKeyDown(); }
/*      */ 
/*      */   
/*      */   protected boolean updateIsUnderwater() {
/*  353 */     this.wasUnderwater = isEyeInFluid(FluidTags.WATER);
/*  354 */     return this.wasUnderwater;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onAboveBubbleColumn(boolean dragDown, BlockPos pos) {
/*  359 */     if (!(getAbilities()).flying) {
/*  360 */       super.onAboveBubbleColumn(dragDown, pos);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void onInsideBubbleColumn(boolean dragDown) {
/*  366 */     if (!(getAbilities()).flying) {
/*  367 */       super.onInsideBubbleColumn(dragDown);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  372 */   private void turtleHelmetTick() { addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 0, false, false, true)); }
/*      */ 
/*      */   
/*      */   private boolean isEquipped(Item item) {
/*  376 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/*  377 */       ItemStack itemStack = getItemBySlot(slot);
/*  378 */       Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/*  379 */       if (itemStack.is(item) && equippable != null && equippable.slot() == slot) {
/*  380 */         return true;
/*      */       }
/*      */     } 
/*  383 */     return false;
/*      */   }
/*      */ 
/*      */   
/*  387 */   protected ItemCooldowns createItemCooldowns() { return new ItemCooldowns(); }
/*      */   
/*      */   protected void updatePlayerPose() {
/*      */     Pose actualPose;
/*  391 */     if (!canPlayerFitWithinBlocksAndEntitiesWhen(Pose.SWIMMING)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  399 */     Pose desiredPose = getDesiredPose();
/*      */     
/*  401 */     if (isSpectator() || isPassenger() || canPlayerFitWithinBlocksAndEntitiesWhen(desiredPose)) {
/*  402 */       actualPose = desiredPose;
/*  403 */     } else if (canPlayerFitWithinBlocksAndEntitiesWhen(Pose.CROUCHING)) {
/*      */       
/*  405 */       actualPose = Pose.CROUCHING;
/*      */     } else {
/*      */       
/*  408 */       actualPose = Pose.SWIMMING;
/*      */     } 
/*  410 */     setPose(actualPose);
/*      */   }
/*      */   
/*      */   private Pose getDesiredPose() {
/*  414 */     if (isSleeping())
/*  415 */       return Pose.SLEEPING; 
/*  416 */     if (isSwimming())
/*  417 */       return Pose.SWIMMING; 
/*  418 */     if (isFallFlying())
/*  419 */       return Pose.FALL_FLYING; 
/*  420 */     if (isAutoSpinAttack())
/*  421 */       return Pose.SPIN_ATTACK; 
/*  422 */     if (isShiftKeyDown() && !this.abilities.flying) {
/*  423 */       return Pose.CROUCHING;
/*      */     }
/*  425 */     return Pose.STANDING;
/*      */   }
/*      */ 
/*      */   
/*  429 */   protected boolean canPlayerFitWithinBlocksAndEntitiesWhen(Pose newPose) { return level().noCollision(this, getDimensions(newPose).makeBoundingBox(position()).deflate(1.0E-7D)); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  434 */   protected SoundEvent getSwimSound() { return SoundEvents.PLAYER_SWIM; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  439 */   protected SoundEvent getSwimSplashSound() { return SoundEvents.PLAYER_SPLASH; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  444 */   protected SoundEvent getSwimHighSpeedSplashSound() { return SoundEvents.PLAYER_SPLASH_HIGH_SPEED; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  449 */   public int getDimensionChangingDelay() { return 10; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  455 */   public void playSound(SoundEvent sound, float volume, float pitch) { level().playSound(this, getX(), getY(), getZ(), sound, getSoundSource(), volume, pitch); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  460 */   public SoundSource getSoundSource() { return SoundSource.PLAYERS; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  465 */   protected int getFireImmuneTicks() { return 20; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleEntityEvent(byte id) {
/*  470 */     if (id == 9) {
/*  471 */       completeUsingItem();
/*  472 */     } else if (id == 23) {
/*  473 */       setReducedDebugInfo(false);
/*  474 */     } else if (id == 22) {
/*  475 */       setReducedDebugInfo(true);
/*      */     } else {
/*  477 */       super.handleEntityEvent(id);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  482 */   protected void closeContainer() { this.containerMenu = this.inventoryMenu; }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void doCloseContainer() {}
/*      */ 
/*      */   
/*      */   public void rideTick() {
/*  490 */     if (!level().isClientSide() && wantsToStopRiding() && isPassenger()) {
/*  491 */       stopRiding();
/*  492 */       setShiftKeyDown(false);
/*      */       return;
/*      */     } 
/*  495 */     super.rideTick();
/*      */   }
/*      */ 
/*      */   
/*      */   public void aiStep() {
/*  500 */     if (this.jumpTriggerTime > 0) {
/*  501 */       this.jumpTriggerTime--;
/*      */     }
/*      */     
/*  504 */     tickRegeneration();
/*  505 */     this.inventory.tick();
/*      */     
/*  507 */     if (this.abilities.flying && !isPassenger()) {
/*  508 */       resetFallDistance();
/*      */     }
/*      */     
/*  511 */     super.aiStep();
/*  512 */     updateSwingTime();
/*      */     
/*  514 */     this.yHeadRot = getYRot();
/*      */     
/*  516 */     setSpeed((float)getAttributeValue(Attributes.MOVEMENT_SPEED));
/*      */     
/*  518 */     if (getHealth() > 0.0F && !isSpectator()) {
/*      */       AABB pickupArea;
/*  520 */       if (isPassenger() && !getVehicle().isRemoved()) {
/*      */         
/*  522 */         pickupArea = getBoundingBox().minmax(getVehicle().getBoundingBox()).inflate(1.0D, 0.0D, 1.0D);
/*      */       } else {
/*  524 */         pickupArea = getBoundingBox().inflate(1.0D, 0.5D, 1.0D);
/*      */       } 
/*      */       
/*  527 */       List<Entity> entities = level().getEntities(this, pickupArea);
/*  528 */       List<Entity> orbs = Lists.newArrayList();
/*  529 */       for (Entity entity : entities) {
/*  530 */         if (entity.getType() == EntityType.EXPERIENCE_ORB) {
/*  531 */           orbs.add(entity); continue;
/*  532 */         }  if (!entity.isRemoved()) {
/*  533 */           touch(entity);
/*      */         }
/*      */       } 
/*  536 */       if (!orbs.isEmpty()) {
/*  537 */         touch((Entity)Util.getRandom(orbs, this.random));
/*      */       }
/*      */     } 
/*      */     
/*  541 */     handleShoulderEntities();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void tickRegeneration() {}
/*      */ 
/*      */   
/*      */   public void handleShoulderEntities() {}
/*      */ 
/*      */   
/*      */   protected void removeEntitiesOnShoulder() {}
/*      */ 
/*      */   
/*  554 */   private void touch(Entity entity) { entity.playerTouch(this); }
/*      */ 
/*      */ 
/*      */   
/*  558 */   public int getScore() { return ((Integer)this.entityData.get(DATA_SCORE_ID)).intValue(); }
/*      */ 
/*      */ 
/*      */   
/*  562 */   public void setScore(int value) { this.entityData.set(DATA_SCORE_ID, Integer.valueOf(value)); }
/*      */ 
/*      */   
/*      */   public void increaseScore(int amount) {
/*  566 */     int score = getScore();
/*  567 */     this.entityData.set(DATA_SCORE_ID, Integer.valueOf(score + amount));
/*      */   }
/*      */   
/*      */   public void startAutoSpinAttack(int activationTicks, float dmg, ItemStack itemStackUsed) {
/*  571 */     this.autoSpinAttackTicks = activationTicks;
/*  572 */     this.autoSpinAttackDmg = dmg;
/*  573 */     this.autoSpinAttackItemStack = itemStackUsed;
/*  574 */     if (!level().isClientSide()) {
/*  575 */       removeEntitiesOnShoulder();
/*  576 */       setLivingEntityFlag(4, true);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemStack getWeaponItem() {
/*  582 */     if (isAutoSpinAttack() && this.autoSpinAttackItemStack != null) {
/*  583 */       return this.autoSpinAttackItemStack;
/*      */     }
/*  585 */     return super.getWeaponItem();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void die(DamageSource source) {
/*  591 */     super.die(source);
/*  592 */     reapplyPosition();
/*      */     
/*  594 */     if (!isSpectator()) { Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/*  595 */         dropAllDeathLoot(level, source); }
/*      */        }
/*      */     
/*  598 */     if (source != null) {
/*  599 */       setDeltaMovement((
/*  600 */           -Mth.cos(((getHurtDir() + getYRot()) * 0.017453292F)) * 0.1F), 0.10000000149011612D, (
/*      */           
/*  602 */           -Mth.sin(((getHurtDir() + getYRot()) * 0.017453292F)) * 0.1F));
/*      */     } else {
/*      */       
/*  605 */       setDeltaMovement(0.0D, 0.1D, 0.0D);
/*      */     } 
/*      */     
/*  608 */     awardStat(Stats.DEATHS);
/*  609 */     resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
/*  610 */     resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
/*  611 */     clearFire();
/*  612 */     setSharedFlagOnFire(false);
/*  613 */     setLastDeathLocation(Optional.of(GlobalPos.of(level().dimension(), blockPosition())));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void dropEquipment(ServerLevel level) {
/*  618 */     super.dropEquipment(level);
/*  619 */     if (!((Boolean)level.getGameRules().get(GameRules.KEEP_INVENTORY)).booleanValue()) {
/*  620 */       destroyVanishingCursedItems();
/*  621 */       this.inventory.dropAll();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void destroyVanishingCursedItems() {
/*  626 */     for (int i = 0; i < this.inventory.getContainerSize(); i++) {
/*  627 */       ItemStack itemStack = this.inventory.getItem(i);
/*  628 */       if (!itemStack.isEmpty() && EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
/*  629 */         this.inventory.removeItemNoUpdate(i);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  636 */   protected SoundEvent getHurtSound(DamageSource source) { return source.type().effects().sound(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  641 */   protected SoundEvent getDeathSound() { return SoundEvents.PLAYER_DEATH; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleCreativeModeItemDrop(ItemStack stack) {}
/*      */ 
/*      */   
/*  648 */   public ItemEntity drop(ItemStack itemStack, boolean thrownFromHand) { return drop(itemStack, false, thrownFromHand); }
/*      */ 
/*      */   
/*      */   public float getDestroySpeed(BlockState state) {
/*  652 */     float speed = this.inventory.getSelectedItem().getDestroySpeed(state);
/*  653 */     if (speed > 1.0F) {
/*  654 */       speed += (float)getAttributeValue(Attributes.MINING_EFFICIENCY);
/*      */     }
/*      */     
/*  657 */     if (MobEffectUtil.hasDigSpeed(this)) {
/*  658 */       speed *= (1.0F + (MobEffectUtil.getDigSpeedAmplification(this) + 1) * 0.2F);
/*      */     }
/*  660 */     if (hasEffect(MobEffects.MINING_FATIGUE)) {
/*      */ 
/*      */       
/*  663 */       switch (getEffect(MobEffects.MINING_FATIGUE).getAmplifier()) { case 0: 
/*      */         case 1: 
/*      */         case 2: 
/*      */         default:
/*  667 */           break; }  float scale = 8.1E-4F;
/*      */       
/*  669 */       speed *= scale;
/*      */     } 
/*      */     
/*  672 */     speed *= (float)getAttributeValue(Attributes.BLOCK_BREAK_SPEED);
/*      */     
/*  674 */     if (isEyeInFluid(FluidTags.WATER)) {
/*  675 */       speed *= (float)getAttribute(Attributes.SUBMERGED_MINING_SPEED).getValue();
/*      */     }
/*  677 */     if (!onGround()) {
/*  678 */       speed /= 5.0F;
/*      */     }
/*      */     
/*  681 */     return speed;
/*      */   }
/*      */ 
/*      */   
/*  685 */   public boolean hasCorrectToolForDrops(BlockState state) { return (!state.requiresCorrectToolForDrops() || this.inventory.getSelectedItem().isCorrectToolForDrops(state)); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  690 */     super.readAdditionalSaveData(input);
/*      */     
/*  692 */     setUUID(this.gameProfile.id());
/*  693 */     this.inventory.load(input.listOrEmpty("Inventory", ItemStackWithSlot.CODEC));
/*  694 */     this.inventory.setSelectedSlot(input.getIntOr("SelectedItemSlot", 0));
/*  695 */     this.sleepCounter = input.getShortOr("SleepTimer", (short)0);
/*      */     
/*  697 */     this.experienceProgress = input.getFloatOr("XpP", 0.0F);
/*  698 */     this.experienceLevel = input.getIntOr("XpLevel", 0);
/*  699 */     this.totalExperience = input.getIntOr("XpTotal", 0);
/*  700 */     this.enchantmentSeed = input.getIntOr("XpSeed", 0);
/*  701 */     if (this.enchantmentSeed == 0) {
/*  702 */       this.enchantmentSeed = this.random.nextInt();
/*      */     }
/*  704 */     setScore(input.getIntOr("Score", 0));
/*      */     
/*  706 */     this.foodData.readAdditionalSaveData(input);
/*      */     
/*  708 */     Objects.requireNonNull(this.abilities); input.read("abilities", Abilities.Packed.CODEC).ifPresent(this.abilities::apply);
/*      */     
/*  710 */     getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.abilities.getWalkingSpeed());
/*      */     
/*  712 */     this.enderChestInventory.fromSlots(input.listOrEmpty("EnderItems", ItemStackWithSlot.CODEC));
/*      */     
/*  714 */     setLastDeathLocation(input.read("LastDeathLocation", GlobalPos.CODEC));
/*  715 */     this.currentImpulseImpactPos = (Vec3)input.read("current_explosion_impact_pos", Vec3.CODEC).orElse(null);
/*      */     
/*  717 */     this.ignoreFallDamageFromCurrentImpulse = input.getBooleanOr("ignore_fall_damage_from_current_explosion", false);
/*  718 */     this.currentImpulseContextResetGraceTime = input.getIntOr("current_impulse_context_reset_grace_time", 0);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  723 */     super.addAdditionalSaveData(output);
/*  724 */     NbtUtils.addCurrentDataVersion(output);
/*  725 */     this.inventory.save(output.list("Inventory", ItemStackWithSlot.CODEC));
/*  726 */     output.putInt("SelectedItemSlot", this.inventory.getSelectedSlot());
/*  727 */     output.putShort("SleepTimer", (short)this.sleepCounter);
/*  728 */     output.putFloat("XpP", this.experienceProgress);
/*  729 */     output.putInt("XpLevel", this.experienceLevel);
/*  730 */     output.putInt("XpTotal", this.totalExperience);
/*  731 */     output.putInt("XpSeed", this.enchantmentSeed);
/*  732 */     output.putInt("Score", getScore());
/*      */     
/*  734 */     this.foodData.addAdditionalSaveData(output);
/*      */     
/*  736 */     output.store("abilities", Abilities.Packed.CODEC, this.abilities.pack());
/*  737 */     this.enderChestInventory.storeAsSlots(output.list("EnderItems", ItemStackWithSlot.CODEC));
/*      */     
/*  739 */     this.lastDeathLocation.ifPresent(pos -> output.store("LastDeathLocation", GlobalPos.CODEC, pos));
/*      */     
/*  741 */     output.storeNullable("current_explosion_impact_pos", Vec3.CODEC, this.currentImpulseImpactPos);
/*  742 */     output.putBoolean("ignore_fall_damage_from_current_explosion", this.ignoreFallDamageFromCurrentImpulse);
/*  743 */     output.putInt("current_impulse_context_reset_grace_time", this.currentImpulseContextResetGraceTime);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInvulnerableTo(ServerLevel level, DamageSource source) {
/*  748 */     if (super.isInvulnerableTo(level, source)) {
/*  749 */       return true;
/*      */     }
/*      */     
/*  752 */     if (source.is(DamageTypeTags.IS_DROWNING))
/*  753 */       return !((Boolean)level.getGameRules().get(GameRules.DROWNING_DAMAGE)).booleanValue(); 
/*  754 */     if (source.is(DamageTypeTags.IS_FALL))
/*  755 */       return !((Boolean)level.getGameRules().get(GameRules.FALL_DAMAGE)).booleanValue(); 
/*  756 */     if (source.is(DamageTypeTags.IS_FIRE))
/*  757 */       return !((Boolean)level.getGameRules().get(GameRules.FIRE_DAMAGE)).booleanValue(); 
/*  758 */     if (source.is(DamageTypeTags.IS_FREEZING)) {
/*  759 */       return !((Boolean)level.getGameRules().get(GameRules.FREEZE_DAMAGE)).booleanValue();
/*      */     }
/*  761 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*  766 */     if (isInvulnerableTo(level, source)) {
/*  767 */       return false;
/*      */     }
/*  769 */     if (this.abilities.invulnerable && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
/*  770 */       return false;
/*      */     }
/*      */     
/*  773 */     this.noActionTime = 0;
/*  774 */     if (isDeadOrDying()) {
/*  775 */       return false;
/*      */     }
/*      */     
/*  778 */     removeEntitiesOnShoulder();
/*      */     
/*  780 */     if (source.scalesWithDifficulty()) {
/*  781 */       if (level.getDifficulty() == Difficulty.PEACEFUL) {
/*  782 */         damage = 0.0F;
/*      */       }
/*  784 */       if (level.getDifficulty() == Difficulty.EASY) {
/*  785 */         damage = Math.min(damage / 2.0F + 1.0F, damage);
/*      */       }
/*  787 */       if (level.getDifficulty() == Difficulty.HARD) {
/*  788 */         damage = damage * 3.0F / 2.0F;
/*      */       }
/*      */     } 
/*      */     
/*  792 */     if (damage == 0.0F) {
/*  793 */       return false;
/*      */     }
/*      */     
/*  796 */     return super.hurtServer(level, source, damage);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void blockUsingItem(ServerLevel level, LivingEntity attacker) {
/*  801 */     super.blockUsingItem(level, attacker);
/*      */     
/*  803 */     ItemStack itemBlockingWith = getItemBlockingWith();
/*  804 */     BlocksAttacks blocksAttacks = (itemBlockingWith != null) ? (BlocksAttacks)itemBlockingWith.get(DataComponents.BLOCKS_ATTACKS) : null;
/*  805 */     float secondsToDisableBlocking = attacker.getSecondsToDisableBlocking();
/*  806 */     if (secondsToDisableBlocking > 0.0F && blocksAttacks != null) {
/*  807 */       blocksAttacks.disable(level, this, secondsToDisableBlocking, itemBlockingWith);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  813 */   public boolean canBeSeenAsEnemy() { return (!(getAbilities()).invulnerable && super.canBeSeenAsEnemy()); }
/*      */ 
/*      */   
/*      */   public boolean canHarmPlayer(Player target) {
/*  817 */     PlayerTeam playerTeam1 = getTeam();
/*  818 */     PlayerTeam playerTeam2 = target.getTeam();
/*      */     
/*  820 */     if (playerTeam1 == null) {
/*  821 */       return true;
/*      */     }
/*  823 */     if (!playerTeam1.isAlliedTo(playerTeam2)) {
/*  824 */       return true;
/*      */     }
/*  826 */     return playerTeam1.isAllowFriendlyFire();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  831 */   protected void hurtArmor(DamageSource damageSource, float damage) { doHurtEquipment(damageSource, damage, new EquipmentSlot[] { EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD }); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  836 */   protected void hurtHelmet(DamageSource damageSource, float damage) { doHurtEquipment(damageSource, damage, new EquipmentSlot[] { EquipmentSlot.HEAD }); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void actuallyHurt(ServerLevel level, DamageSource source, float dmg) {
/*  841 */     if (isInvulnerableTo(level, source)) {
/*      */       return;
/*      */     }
/*  844 */     dmg = getDamageAfterArmorAbsorb(source, dmg);
/*  845 */     dmg = getDamageAfterMagicAbsorb(source, dmg);
/*      */     
/*  847 */     float originalDamage = dmg;
/*  848 */     dmg = Math.max(dmg - getAbsorptionAmount(), 0.0F);
/*  849 */     setAbsorptionAmount(getAbsorptionAmount() - originalDamage - dmg);
/*      */     
/*  851 */     float absorbedDamage = originalDamage - dmg;
/*  852 */     if (absorbedDamage > 0.0F && absorbedDamage < 3.4028235E37F) {
/*  853 */       awardStat(Stats.DAMAGE_ABSORBED, Math.round(absorbedDamage * 10.0F));
/*      */     }
/*      */     
/*  856 */     if (dmg == 0.0F) {
/*      */       return;
/*      */     }
/*      */     
/*  860 */     causeFoodExhaustion(source.getFoodExhaustion());
/*  861 */     getCombatTracker().recordDamage(source, dmg);
/*  862 */     setHealth(getHealth() - dmg);
/*  863 */     if (dmg < 3.4028235E37F) {
/*  864 */       awardStat(Stats.DAMAGE_TAKEN, Math.round(dmg * 10.0F));
/*      */     }
/*  866 */     gameEvent(GameEvent.ENTITY_DAMAGE);
/*      */   }
/*      */ 
/*      */   
/*  870 */   public boolean isTextFilteringEnabled() { return false; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void openTextEdit(SignBlockEntity sign, boolean isFrontText) {}
/*      */ 
/*      */   
/*      */   public void openMinecartCommandBlock(MinecartCommandBlock commandBlock) {}
/*      */ 
/*      */   
/*      */   public void openCommandBlock(CommandBlockEntity commandBlock) {}
/*      */ 
/*      */   
/*      */   public void openStructureBlock(StructureBlockEntity structureBlock) {}
/*      */ 
/*      */   
/*      */   public void openTestBlock(TestBlockEntity testBlock) {}
/*      */ 
/*      */   
/*      */   public void openTestInstanceBlock(TestInstanceBlockEntity testInstanceBlock) {}
/*      */ 
/*      */   
/*      */   public void openJigsawBlock(JigsawBlockEntity jigsawBlock) {}
/*      */ 
/*      */   
/*      */   public void openHorseInventory(AbstractHorse horse, Container container) {}
/*      */ 
/*      */   
/*      */   public void openNautilusInventory(AbstractNautilus nautilus, Container container) {}
/*      */ 
/*      */   
/*  901 */   public OptionalInt openMenu(MenuProvider provider) { return OptionalInt.empty(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void openDialog(Holder<Dialog> dialog) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendMerchantOffers(int containerId, MerchantOffers offers, int merchantLevel, int merchantXp, boolean showProgressBar, boolean canRestock) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void openItemGui(ItemStack itemStack, InteractionHand hand) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public InteractionResult interactOn(Entity entity, InteractionHand hand) {
/*  920 */     if (isSpectator()) {
/*  921 */       if (entity instanceof MenuProvider) {
/*  922 */         openMenu((MenuProvider)entity);
/*      */       }
/*  924 */       return InteractionResult.PASS;
/*      */     } 
/*      */     
/*  927 */     ItemStack itemStack = getItemInHand(hand);
/*      */     
/*  929 */     ItemStack itemStackClone = itemStack.copy();
/*  930 */     InteractionResult interact = entity.interact(this, hand);
/*  931 */     if (interact.consumesAction()) {
/*  932 */       if (hasInfiniteMaterials() && itemStack == getItemInHand(hand) && itemStack.getCount() < itemStackClone.getCount()) {
/*  933 */         itemStack.setCount(itemStackClone.getCount());
/*      */       }
/*  935 */       return interact;
/*      */     } 
/*      */     
/*  938 */     if (!itemStack.isEmpty() && entity instanceof LivingEntity) {
/*      */       
/*  940 */       if (hasInfiniteMaterials()) {
/*  941 */         itemStack = itemStackClone;
/*      */       }
/*  943 */       InteractionResult interactionResult = itemStack.interactLivingEntity(this, (LivingEntity)entity, hand);
/*  944 */       if (interactionResult.consumesAction()) {
/*  945 */         level().gameEvent(GameEvent.ENTITY_INTERACT, entity.position(), GameEvent.Context.of(this));
/*      */         
/*  947 */         if (itemStack.isEmpty() && !hasInfiniteMaterials()) {
/*  948 */           setItemInHand(hand, ItemStack.EMPTY);
/*      */         }
/*  950 */         return interactionResult;
/*      */       } 
/*      */     } 
/*  953 */     return InteractionResult.PASS;
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeVehicle() {
/*  958 */     super.removeVehicle();
/*      */     
/*  960 */     this.boardingCooldown = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  965 */   protected boolean isImmobile() { return (super.isImmobile() || isSleeping()); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  970 */   public boolean isAffectedByFluids() { return !this.abilities.flying; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Vec3 maybeBackOffFromEdge(Vec3 delta, MoverType moverType) {
/*  980 */     float maxDownStep = maxUpStep();
/*      */     
/*  982 */     if (this.abilities.flying || delta.y > 0.0D || (moverType != MoverType.SELF && moverType != MoverType.PLAYER) || !isStayingOnGroundSurface() || !isAboveGround(maxDownStep)) {
/*  983 */       return delta;
/*      */     }
/*      */     
/*  986 */     double deltaX = delta.x;
/*  987 */     double deltaZ = delta.z;
/*  988 */     double step = 0.05D;
/*  989 */     double stepX = Math.signum(deltaX) * 0.05D;
/*  990 */     double stepZ = Math.signum(deltaZ) * 0.05D;
/*      */ 
/*      */     
/*  993 */     while (deltaX != 0.0D && canFallAtLeast(deltaX, 0.0D, maxDownStep)) {
/*  994 */       if (Math.abs(deltaX) <= 0.05D) {
/*  995 */         deltaX = 0.0D;
/*      */         break;
/*      */       } 
/*  998 */       deltaX -= stepX;
/*      */     } 
/*      */ 
/*      */     
/* 1002 */     while (deltaZ != 0.0D && canFallAtLeast(0.0D, deltaZ, maxDownStep)) {
/* 1003 */       if (Math.abs(deltaZ) <= 0.05D) {
/* 1004 */         deltaZ = 0.0D;
/*      */         break;
/*      */       } 
/* 1007 */       deltaZ -= stepZ;
/*      */     } 
/*      */ 
/*      */     
/* 1011 */     while (deltaX != 0.0D && deltaZ != 0.0D && canFallAtLeast(deltaX, deltaZ, maxDownStep)) {
/* 1012 */       if (Math.abs(deltaX) <= 0.05D) {
/* 1013 */         deltaX = 0.0D;
/*      */       } else {
/* 1015 */         deltaX -= stepX;
/*      */       } 
/*      */       
/* 1018 */       if (Math.abs(deltaZ) <= 0.05D) {
/* 1019 */         deltaZ = 0.0D; continue;
/*      */       } 
/* 1021 */       deltaZ -= stepZ;
/*      */     } 
/*      */ 
/*      */     
/* 1025 */     return new Vec3(deltaX, delta.y, deltaZ);
/*      */   }
/*      */ 
/*      */   
/* 1029 */   private boolean isAboveGround(float maxDownStep) { return (onGround() || (this.fallDistance < maxDownStep && !canFallAtLeast(0.0D, 0.0D, maxDownStep - this.fallDistance))); }
/*      */ 
/*      */   
/*      */   private boolean canFallAtLeast(double deltaX, double deltaZ, double minHeight) {
/* 1033 */     AABB boundingBox = getBoundingBox();
/* 1034 */     return level().noCollision(this, new AABB(boundingBox.minX + 1.0E-7D + deltaX, boundingBox.minY - minHeight - 1.0E-7D, boundingBox.minZ + 1.0E-7D + deltaZ, boundingBox.maxX - 1.0E-7D + deltaX, boundingBox.minY, boundingBox.maxZ - 1.0E-7D + deltaZ));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void attack(Entity entity) {
/* 1041 */     if (cannotAttack(entity)) {
/*      */       return;
/*      */     }
/*      */     
/* 1045 */     float baseDamage = isAutoSpinAttack() ? this.autoSpinAttackDmg : (float)getAttributeValue(Attributes.ATTACK_DAMAGE);
/* 1046 */     ItemStack attackingItemStack = getWeaponItem();
/*      */     
/* 1048 */     DamageSource damageSource = createAttackSource(attackingItemStack);
/*      */     
/* 1050 */     float attackStrengthScale = getAttackStrengthScale(0.5F);
/*      */ 
/*      */     
/* 1053 */     float magicBoost = attackStrengthScale * (getEnchantedDamage(entity, baseDamage, damageSource) - baseDamage);
/* 1054 */     baseDamage *= baseDamageScaleFactor();
/*      */     
/* 1056 */     onAttack();
/*      */     
/* 1058 */     if (deflectProjectile(entity)) {
/*      */       return;
/*      */     }
/*      */     
/* 1062 */     if (baseDamage > 0.0F || magicBoost > 0.0F) {
/* 1063 */       boolean knockbackAttack, fullStrengthAttack = (attackStrengthScale > 0.9F);
/*      */ 
/*      */ 
/*      */       
/* 1067 */       if (isSprinting() && fullStrengthAttack) {
/* 1068 */         playServerSideSound(SoundEvents.PLAYER_ATTACK_KNOCKBACK);
/* 1069 */         knockbackAttack = true;
/*      */       } else {
/* 1071 */         knockbackAttack = false;
/*      */       } 
/*      */ 
/*      */       
/* 1075 */       baseDamage += attackingItemStack.getItem().getAttackDamageBonus(entity, baseDamage, damageSource);
/*      */ 
/*      */ 
/*      */       
/* 1079 */       boolean criticalAttack = (fullStrengthAttack && canCriticalAttack(entity));
/* 1080 */       if (criticalAttack) {
/* 1081 */         baseDamage *= 1.5F;
/*      */       }
/* 1083 */       float totalDamage = baseDamage + magicBoost;
/*      */       
/* 1085 */       boolean sweepAttack = isSweepAttack(fullStrengthAttack, criticalAttack, knockbackAttack);
/*      */       
/* 1087 */       float oldLivingEntityHealth = 0.0F;
/* 1088 */       if (entity instanceof LivingEntity) { LivingEntity livingTarget = (LivingEntity)entity;
/* 1089 */         oldLivingEntityHealth = livingTarget.getHealth(); }
/*      */ 
/*      */       
/* 1092 */       Vec3 oldMovement = entity.getDeltaMovement();
/*      */       
/* 1094 */       boolean wasHurt = entity.hurtOrSimulate(damageSource, totalDamage);
/* 1095 */       if (wasHurt) {
/* 1096 */         causeExtraKnockback(entity, getKnockback(entity, damageSource) + (knockbackAttack ? 0.5F : 0.0F), oldMovement);
/* 1097 */         if (sweepAttack) {
/* 1098 */           doSweepAttack(entity, baseDamage, damageSource, attackStrengthScale);
/*      */         }
/*      */         
/* 1101 */         attackVisualEffects(entity, criticalAttack, sweepAttack, fullStrengthAttack, false, magicBoost);
/*      */         
/* 1103 */         setLastHurtMob(entity);
/*      */         
/* 1105 */         itemAttackInteraction(entity, attackingItemStack, damageSource, true);
/*      */         
/* 1107 */         damageStatsAndHearts(entity, oldLivingEntityHealth);
/*      */         
/* 1109 */         causeFoodExhaustion(0.1F);
/*      */       } else {
/* 1111 */         playServerSideSound(SoundEvents.PLAYER_ATTACK_NODAMAGE);
/*      */       } 
/*      */     } 
/* 1114 */     lungeForwardMaybe();
/*      */   }
/*      */ 
/*      */   
/* 1118 */   private void playServerSideSound(SoundEvent sound) { level().playSound(null, getX(), getY(), getZ(), sound, getSoundSource(), 1.0F, 1.0F); }
/*      */ 
/*      */ 
/*      */   
/* 1122 */   private DamageSource createAttackSource(ItemStack attackingItemStack) { return attackingItemStack.getDamageSource(this, () -> damageSources().playerAttack(this)); }
/*      */ 
/*      */   
/*      */   private boolean cannotAttack(Entity entity) {
/* 1126 */     if (!entity.isAttackable()) {
/* 1127 */       return true;
/*      */     }
/* 1129 */     return entity.skipAttackInteraction(this);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean deflectProjectile(Entity entity) {
/* 1134 */     if (entity.getType().is(EntityTypeTags.REDIRECTABLE_PROJECTILE) && entity instanceof Projectile) {
/* 1135 */       Projectile projectile = (Projectile)entity; if (projectile
/* 1136 */         .deflect(ProjectileDeflection.AIM_DEFLECT, this, EntityReference.of(this), true)) {
/* 1137 */         level().playSound(null, getX(), getY(), getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, getSoundSource());
/* 1138 */         return true;
/*      */       } 
/* 1140 */     }  return false;
/*      */   }
/*      */   
/*      */   private boolean canCriticalAttack(Entity entity) {
/* 1144 */     return (this.fallDistance > 0.0D && 
/* 1145 */       !onGround() && 
/* 1146 */       !onClimbable() && 
/* 1147 */       !isInWater() && 
/* 1148 */       !isMobilityRestricted() && 
/* 1149 */       !isPassenger() && entity instanceof LivingEntity && 
/*      */       
/* 1151 */       !isSprinting());
/*      */   }
/*      */   
/*      */   private boolean isSweepAttack(boolean fullStrengthAttack, boolean criticalAttack, boolean knockbackAttack) {
/* 1155 */     if (fullStrengthAttack && !criticalAttack && !knockbackAttack && onGround()) {
/*      */       
/* 1157 */       double approximateSpeedSq = getKnownMovement().horizontalDistanceSqr();
/* 1158 */       double maxSpeedForSweepAttack = getSpeed() * 2.5D;
/* 1159 */       if (approximateSpeedSq < Mth.square(maxSpeedForSweepAttack))
/*      */       {
/* 1161 */         return getItemInHand(InteractionHand.MAIN_HAND).is(ItemTags.SWORDS);
/*      */       }
/*      */     } 
/* 1164 */     return false;
/*      */   }
/*      */   
/*      */   private void attackVisualEffects(Entity entity, boolean criticalAttack, boolean sweepAttack, boolean fullStrengthAttack, boolean stabAttack, float magicBoost) {
/* 1168 */     if (criticalAttack) {
/* 1169 */       playServerSideSound(SoundEvents.PLAYER_ATTACK_CRIT);
/* 1170 */       crit(entity);
/*      */     } 
/*      */     
/* 1173 */     if (!criticalAttack && !sweepAttack && !stabAttack) {
/* 1174 */       playServerSideSound(fullStrengthAttack ? SoundEvents.PLAYER_ATTACK_STRONG : SoundEvents.PLAYER_ATTACK_WEAK);
/*      */     }
/*      */     
/* 1177 */     if (magicBoost > 0.0F) {
/* 1178 */       magicCrit(entity);
/*      */     }
/*      */   }
/*      */   
/*      */   private void damageStatsAndHearts(Entity entity, float oldLivingEntityHealth) {
/* 1183 */     if (entity instanceof LivingEntity) {
/* 1184 */       float actualDamage = oldLivingEntityHealth - ((LivingEntity)entity).getHealth();
/*      */       
/* 1186 */       awardStat(Stats.DAMAGE_DEALT, Math.round(actualDamage * 10.0F));
/* 1187 */       if (level() instanceof ServerLevel && actualDamage > 2.0F) {
/* 1188 */         int count = (int)(actualDamage * 0.5D);
/* 1189 */         ((ServerLevel)level()).sendParticles(ParticleTypes.DAMAGE_INDICATOR, entity.getX(), entity.getY(0.5D), entity.getZ(), count, 0.1D, 0.0D, 0.1D, 0.2D);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void itemAttackInteraction(Entity entity, ItemStack attackingItemStack, DamageSource damageSource, boolean applyToTarget) {
/* 1195 */     EnderDragon enderDragon = entity;
/* 1196 */     if (entity instanceof EnderDragonPart) {
/* 1197 */       enderDragon = ((EnderDragonPart)entity).parentMob;
/*      */     }
/* 1199 */     boolean itemHurtEnemy = false;
/* 1200 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 1201 */       if (enderDragon instanceof LivingEntity) { LivingEntity livingTarget = (LivingEntity)enderDragon;
/* 1202 */         itemHurtEnemy = attackingItemStack.hurtEnemy(livingTarget, this); }
/*      */       
/* 1204 */       if (applyToTarget) {
/* 1205 */         EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, entity, damageSource, attackingItemStack);
/*      */       } }
/*      */ 
/*      */     
/* 1209 */     if (!level().isClientSide() && !attackingItemStack.isEmpty() && enderDragon instanceof LivingEntity) {
/* 1210 */       if (itemHurtEnemy) {
/* 1211 */         attackingItemStack.postHurtEnemy((LivingEntity)enderDragon, this);
/*      */       }
/* 1213 */       if (attackingItemStack.isEmpty()) {
/* 1214 */         if (attackingItemStack == getMainHandItem()) {
/* 1215 */           setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
/*      */         } else {
/* 1217 */           setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void causeExtraKnockback(Entity entity, float knockbackAmount, Vec3 oldMovement) {
/* 1225 */     if (knockbackAmount > 0.0F) {
/* 1226 */       if (entity instanceof LivingEntity) { LivingEntity livingTarget = (LivingEntity)entity;
/* 1227 */         livingTarget.knockback(knockbackAmount, Mth.sin((getYRot() * 0.017453292F)), -Mth.cos((getYRot() * 0.017453292F))); }
/*      */       else
/* 1229 */       { entity.push((-Mth.sin((getYRot() * 0.017453292F)) * knockbackAmount), 0.1D, (Mth.cos((getYRot() * 0.017453292F)) * knockbackAmount)); }
/*      */       
/* 1231 */       setDeltaMovement(getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
/* 1232 */       setSprinting(false);
/*      */     } 
/* 1234 */     if (entity instanceof ServerPlayer && entity.hurtMarked) {
/* 1235 */       ((ServerPlayer)entity).connection.send(new ClientboundSetEntityMotionPacket(entity));
/* 1236 */       entity.hurtMarked = false;
/* 1237 */       entity.setDeltaMovement(oldMovement);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1243 */   public float getVoicePitch() { return 1.0F; }
/*      */   
/*      */   private void doSweepAttack(Entity entity, float baseDamage, DamageSource damageSource, float attackStrengthScale) {
/*      */     ServerLevel serverLevel;
/* 1247 */     playServerSideSound(SoundEvents.PLAYER_ATTACK_SWEEP);
/* 1248 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*      */     else
/*      */     { return; }
/* 1251 */      float sweepDamage = 1.0F + (float)getAttributeValue(Attributes.SWEEPING_DAMAGE_RATIO) * baseDamage;
/* 1252 */     List<LivingEntity> nearbyEntities = level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(1.0D, 0.25D, 1.0D));
/* 1253 */     for (LivingEntity nearby : nearbyEntities) {
/* 1254 */       if (nearby == this || nearby == entity || isAlliedTo(nearby)) {
/*      */         continue;
/*      */       }
/*      */       
/* 1258 */       if (nearby instanceof ArmorStand) { ArmorStand armorStand = (ArmorStand)nearby; if (armorStand.isMarker()) {
/*      */           continue;
/*      */         } }
/*      */       
/* 1262 */       if (distanceToSqr(nearby) < 9.0D) {
/* 1263 */         float enchantedDamage = getEnchantedDamage(nearby, sweepDamage, damageSource) * attackStrengthScale;
/* 1264 */         if (nearby.hurtServer(serverLevel, damageSource, enchantedDamage)) {
/* 1265 */           nearby.knockback(0.4000000059604645D, Mth.sin((getYRot() * 0.017453292F)), -Mth.cos((getYRot() * 0.017453292F)));
/* 1266 */           EnchantmentHelper.doPostAttackEffects(serverLevel, nearby, damageSource);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1271 */     double dx = -Mth.sin((getYRot() * 0.017453292F));
/* 1272 */     double dz = Mth.cos((getYRot() * 0.017453292F));
/* 1273 */     serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, getX() + dx, getY(0.5D), getZ() + dz, 0, dx, 0.0D, dz, 0.0D);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1278 */   protected float getEnchantedDamage(Entity entity, float dmg, DamageSource damageSource) { return dmg; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1283 */   protected void doAutoAttackOnTouch(LivingEntity entity) { attack(entity); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void crit(Entity entity) {}
/*      */ 
/*      */   
/*      */   private float baseDamageScaleFactor() {
/* 1291 */     float attackStrengthScale = getAttackStrengthScale(0.5F);
/* 1292 */     return 0.2F + attackStrengthScale * attackStrengthScale * 0.8F;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean stabAttack(EquipmentSlot slot, Entity target, float baseDamage, boolean dealsDamage, boolean dealsKnockback, boolean dismounts) {
/* 1297 */     if (cannotAttack(target)) {
/* 1298 */       return false;
/*      */     }
/*      */     
/* 1301 */     ItemStack weaponItem = getItemBySlot(slot);
/*      */     
/* 1303 */     DamageSource damageSource = createAttackSource(weaponItem);
/*      */     
/* 1305 */     float magicBoost = getEnchantedDamage(target, baseDamage, damageSource) - baseDamage;
/*      */     
/* 1307 */     if (!isUsingItem() || getUsedItemHand().asEquipmentSlot() != slot) {
/*      */       
/* 1309 */       magicBoost *= getAttackStrengthScale(0.5F);
/* 1310 */       baseDamage *= baseDamageScaleFactor();
/*      */     } 
/*      */     
/* 1313 */     if (dealsKnockback && deflectProjectile(target)) {
/* 1314 */       return true;
/*      */     }
/*      */     
/* 1317 */     float totalDamage = dealsDamage ? (baseDamage + magicBoost) : 0.0F;
/*      */     
/* 1319 */     float oldLivingEntityHealth = 0.0F;
/* 1320 */     if (target instanceof LivingEntity) { LivingEntity livingTarget = (LivingEntity)target;
/* 1321 */       oldLivingEntityHealth = livingTarget.getHealth(); }
/*      */ 
/*      */     
/* 1324 */     Vec3 oldMovement = target.getDeltaMovement();
/*      */     
/* 1326 */     boolean wasHurt = (dealsDamage && target.hurtOrSimulate(damageSource, totalDamage));
/* 1327 */     if (dealsKnockback) {
/* 1328 */       causeExtraKnockback(target, 0.4F + getKnockback(target, damageSource), oldMovement);
/*      */     }
/* 1330 */     boolean dismounted = false;
/* 1331 */     if (dismounts && target.isPassenger()) {
/* 1332 */       dismounted = true;
/* 1333 */       target.stopRiding();
/*      */     } 
/* 1335 */     if (!wasHurt && !dealsKnockback && !dismounted) {
/* 1336 */       return false;
/*      */     }
/* 1338 */     attackVisualEffects(target, false, false, dealsDamage, true, magicBoost);
/*      */     
/* 1340 */     setLastHurtMob(target);
/*      */     
/* 1342 */     itemAttackInteraction(target, weaponItem, damageSource, wasHurt);
/*      */     
/* 1344 */     damageStatsAndHearts(target, oldLivingEntityHealth);
/*      */     
/* 1346 */     causeFoodExhaustion(0.1F);
/* 1347 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void magicCrit(Entity entity) {}
/*      */ 
/*      */   
/*      */   public void remove(Entity.RemovalReason reason) {
/* 1355 */     super.remove(reason);
/*      */     
/* 1357 */     this.inventoryMenu.removed(this);
/* 1358 */     if (hasContainerOpen()) {
/* 1359 */       doCloseContainer();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1365 */   public boolean isClientAuthoritative() { return true; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1370 */   protected boolean isLocalClientAuthoritative() { return isLocalPlayer(); }
/*      */ 
/*      */ 
/*      */   
/* 1374 */   public boolean isLocalPlayer() { return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1379 */   public boolean canSimulateMovement() { return (!level().isClientSide() || isLocalPlayer()); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1384 */   public boolean isEffectiveAi() { return (!level().isClientSide() || isLocalPlayer()); }
/*      */ 
/*      */ 
/*      */   
/* 1388 */   public GameProfile getGameProfile() { return this.gameProfile; }
/*      */ 
/*      */ 
/*      */   
/* 1392 */   public NameAndId nameAndId() { return new NameAndId(this.gameProfile); }
/*      */ 
/*      */ 
/*      */   
/* 1396 */   public Inventory getInventory() { return this.inventory; }
/*      */ 
/*      */ 
/*      */   
/* 1400 */   public Abilities getAbilities() { return this.abilities; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1406 */   public boolean hasInfiniteMaterials() { return this.abilities.instabuild; }
/*      */ 
/*      */ 
/*      */   
/* 1410 */   public boolean preventsBlockDrops() { return this.abilities.instabuild; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateTutorialInventoryAction(ItemStack itemCarried, ItemStack itemInSlot, ClickAction clickAction) {}
/*      */ 
/*      */ 
/*      */   
/* 1418 */   public boolean hasContainerOpen() { return (this.containerMenu != this.inventoryMenu); }
/*      */ 
/*      */ 
/*      */   
/* 1422 */   public boolean canDropItems() { return true; }
/*      */   public static final class BedSleepingProblem extends Record { private final Component message;
/*      */     
/* 1425 */     public BedSleepingProblem(Component message) { this.message = message; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1425	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/* 1425 */       //   0	7	0	this	Lnet/minecraft/world/entity/player/Player$BedSleepingProblem; } public Component message() { return this.message; }
/*      */     public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1425	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/entity/player/Player$BedSleepingProblem; }
/*      */     public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1425	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;
/*      */       //   0	8	1	o	Ljava/lang/Object; }
/* 1428 */     public static final BedSleepingProblem TOO_FAR_AWAY = new BedSleepingProblem(Component.translatable("block.minecraft.bed.too_far_away"));
/* 1429 */     public static final BedSleepingProblem OBSTRUCTED = new BedSleepingProblem(Component.translatable("block.minecraft.bed.obstructed"));
/* 1430 */     public static final BedSleepingProblem OTHER_PROBLEM = new BedSleepingProblem(null);
/* 1431 */     public static final BedSleepingProblem NOT_SAFE = new BedSleepingProblem(Component.translatable("block.minecraft.bed.not_safe")); }
/*      */ 
/*      */   
/*      */   public Either<BedSleepingProblem, Unit> startSleepInBed(BlockPos pos) {
/* 1435 */     startSleeping(pos);
/*      */     
/* 1437 */     this.sleepCounter = 0;
/*      */     
/* 1439 */     return Either.right(Unit.INSTANCE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void stopSleepInBed(boolean forcefulWakeUp, boolean updateLevelList) {
/* 1450 */     super.stopSleeping();
/*      */     
/* 1452 */     if (level() instanceof ServerLevel && updateLevelList) {
/* 1453 */       ((ServerLevel)level()).updateSleepingPlayerList();
/*      */     }
/*      */     
/* 1456 */     this.sleepCounter = forcefulWakeUp ? 0 : 100;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1461 */   public void stopSleeping() { stopSleepInBed(true, true); }
/*      */ 
/*      */ 
/*      */   
/* 1465 */   public boolean isSleepingLongEnough() { return (isSleeping() && this.sleepCounter >= 100); }
/*      */ 
/*      */ 
/*      */   
/* 1469 */   public int getSleepTimer() { return this.sleepCounter; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void displayClientMessage(Component component, boolean overlayMessage) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1484 */   public void awardStat(Identifier location) { awardStat(Stats.CUSTOM.get(location)); }
/*      */ 
/*      */ 
/*      */   
/* 1488 */   public void awardStat(Identifier location, int count) { awardStat(Stats.CUSTOM.get(location), count); }
/*      */ 
/*      */ 
/*      */   
/* 1492 */   public void awardStat(Stat<?> stat) { awardStat(stat, 1); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void awardStat(Stat<?> stat, int count) {}
/*      */ 
/*      */   
/*      */   public void resetStat(Stat<?> stat) {}
/*      */ 
/*      */   
/* 1502 */   public int awardRecipes(Collection<RecipeHolder<?>> recipes) { return 0; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void triggerRecipeCrafted(RecipeHolder<?> recipe, List<ItemStack> itemStacks) {}
/*      */ 
/*      */   
/*      */   public void awardRecipesByKey(List<ResourceKey<Recipe<?>>> recipeIds) {}
/*      */ 
/*      */   
/* 1512 */   public int resetRecipes(Collection<RecipeHolder<?>> recipe) { return 0; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void travel(Vec3 input) {
/* 1517 */     if (isPassenger()) {
/* 1518 */       super.travel(input);
/*      */       
/*      */       return;
/*      */     } 
/* 1522 */     if (isSwimming()) {
/* 1523 */       double lookAngleY = (getLookAngle()).y;
/* 1524 */       double multiplier = (lookAngleY < -0.2D) ? 0.085D : 0.06D;
/*      */       
/* 1526 */       if (lookAngleY <= 0.0D || this.jumping || !level().getFluidState(BlockPos.containing(getX(), getY() + 1.0D - 0.1D, getZ())).isEmpty()) {
/* 1527 */         Vec3 movement = getDeltaMovement();
/* 1528 */         setDeltaMovement(movement.add(0.0D, (lookAngleY - movement.y) * multiplier, 0.0D));
/*      */       } 
/*      */     } 
/*      */     
/* 1532 */     if ((getAbilities()).flying) {
/* 1533 */       double originalMovementY = (getDeltaMovement()).y;
/* 1534 */       super.travel(input);
/* 1535 */       setDeltaMovement(getDeltaMovement().with(Direction.Axis.Y, originalMovementY * 0.6D));
/*      */     } else {
/* 1537 */       super.travel(input);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1543 */   protected boolean canGlide() { return (!this.abilities.flying && super.canGlide()); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateSwimming() {
/* 1548 */     if (this.abilities.flying) {
/* 1549 */       setSwimming(false);
/*      */     } else {
/* 1551 */       super.updateSwimming();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 1556 */   protected boolean freeAt(BlockPos pos) { return !level().getBlockState(pos).isSuffocating(level(), pos); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1561 */   public float getSpeed() { return (float)getAttributeValue(Attributes.MOVEMENT_SPEED); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
/*      */     double effectiveFallDistance;
/* 1569 */     if (this.abilities.mayfly) {
/* 1570 */       return false;
/*      */     }
/*      */     
/* 1573 */     if (fallDistance >= 2.0D) {
/* 1574 */       awardStat(Stats.FALL_ONE_CM, (int)Math.round(fallDistance * 100.0D));
/*      */     }
/*      */ 
/*      */     
/* 1578 */     boolean hasRelativeFallDamageResistance = (this.currentImpulseImpactPos != null && this.ignoreFallDamageFromCurrentImpulse);
/* 1579 */     if (hasRelativeFallDamageResistance) {
/* 1580 */       effectiveFallDistance = Math.min(fallDistance, this.currentImpulseImpactPos.y - getY());
/* 1581 */       boolean hasLandedAboveCurrentImpulseImpactPosY = (effectiveFallDistance <= 0.0D);
/* 1582 */       if (hasLandedAboveCurrentImpulseImpactPosY) {
/* 1583 */         resetCurrentImpulseContext();
/*      */       } else {
/* 1585 */         tryResetCurrentImpulseContext();
/*      */       } 
/*      */     } else {
/* 1588 */       effectiveFallDistance = fallDistance;
/*      */     } 
/*      */     
/* 1591 */     if (effectiveFallDistance > 0.0D && super.causeFallDamage(effectiveFallDistance, damageModifier, damageSource)) {
/* 1592 */       resetCurrentImpulseContext();
/* 1593 */       return true;
/*      */     } 
/* 1595 */     propagateFallToPassengers(fallDistance, damageModifier, damageSource);
/* 1596 */     return false;
/*      */   }
/*      */   
/*      */   public boolean tryToStartFallFlying() {
/* 1600 */     if (!isFallFlying() && canGlide() && !isInWater()) {
/* 1601 */       startFallFlying();
/* 1602 */       return true;
/*      */     } 
/* 1604 */     return false;
/*      */   }
/*      */ 
/*      */   
/* 1608 */   public void startFallFlying() { setSharedFlag(7, true); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void doWaterSplashEffect() {
/* 1613 */     if (!isSpectator()) {
/* 1614 */       super.doWaterSplashEffect();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void playStepSound(BlockPos onPos, BlockState onState) {
/* 1620 */     if (isInWater()) {
/* 1621 */       waterSwimSound();
/* 1622 */       playMuffledStepSound(onState);
/*      */     } else {
/* 1624 */       BlockPos primaryStepSoundPos = getPrimaryStepSoundBlockPos(onPos);
/* 1625 */       if (!onPos.equals(primaryStepSoundPos)) {
/* 1626 */         BlockState primaryStepState = level().getBlockState(primaryStepSoundPos);
/* 1627 */         if (primaryStepState.is(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)) {
/* 1628 */           playCombinationStepSounds(primaryStepState, onState);
/*      */         } else {
/* 1630 */           super.playStepSound(primaryStepSoundPos, primaryStepState);
/*      */         } 
/*      */       } else {
/* 1633 */         super.playStepSound(onPos, onState);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1640 */   public LivingEntity.Fallsounds getFallSounds() { return new LivingEntity.Fallsounds(SoundEvents.PLAYER_SMALL_FALL, SoundEvents.PLAYER_BIG_FALL); }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean killedEntity(ServerLevel level, LivingEntity entity, DamageSource source) {
/* 1645 */     awardStat(Stats.ENTITY_KILLED.get(entity.getType()));
/* 1646 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void makeStuckInBlock(BlockState blockState, Vec3 speedMultiplier) {
/* 1651 */     if (!this.abilities.flying) {
/* 1652 */       super.makeStuckInBlock(blockState, speedMultiplier);
/*      */     }
/* 1654 */     tryResetCurrentImpulseContext();
/*      */   }
/*      */   
/*      */   public void giveExperiencePoints(int i) {
/* 1658 */     increaseScore(i);
/* 1659 */     this.experienceProgress += i / getXpNeededForNextLevel();
/* 1660 */     this.totalExperience = Mth.clamp(this.totalExperience + i, 0, 2147483647);
/* 1661 */     while (this.experienceProgress < 0.0F) {
/* 1662 */       float remaining = this.experienceProgress * getXpNeededForNextLevel();
/* 1663 */       if (this.experienceLevel > 0) {
/* 1664 */         giveExperienceLevels(-1);
/* 1665 */         this.experienceProgress = 1.0F + remaining / getXpNeededForNextLevel(); continue;
/*      */       } 
/* 1667 */       giveExperienceLevels(-1);
/* 1668 */       this.experienceProgress = 0.0F;
/*      */     } 
/*      */     
/* 1671 */     while (this.experienceProgress >= 1.0F) {
/* 1672 */       this.experienceProgress = (this.experienceProgress - 1.0F) * getXpNeededForNextLevel();
/* 1673 */       giveExperienceLevels(1);
/* 1674 */       this.experienceProgress /= getXpNeededForNextLevel();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 1679 */   public int getEnchantmentSeed() { return this.enchantmentSeed; }
/*      */ 
/*      */   
/*      */   public void onEnchantmentPerformed(ItemStack itemStack, int enchantmentCost) {
/* 1683 */     this.experienceLevel -= enchantmentCost;
/* 1684 */     if (this.experienceLevel < 0) {
/* 1685 */       this.experienceLevel = 0;
/* 1686 */       this.experienceProgress = 0.0F;
/* 1687 */       this.totalExperience = 0;
/*      */     } 
/* 1689 */     this.enchantmentSeed = this.random.nextInt();
/*      */   }
/*      */   
/*      */   public void giveExperienceLevels(int amount) {
/* 1693 */     this.experienceLevel = IntMath.saturatedAdd(this.experienceLevel, amount);
/* 1694 */     if (this.experienceLevel < 0) {
/* 1695 */       this.experienceLevel = 0;
/* 1696 */       this.experienceProgress = 0.0F;
/* 1697 */       this.totalExperience = 0;
/*      */     } 
/*      */     
/* 1700 */     if (amount > 0 && this.experienceLevel % 5 == 0 && this.lastLevelUpTime < this.tickCount - 100.0F) {
/* 1701 */       float vol = (this.experienceLevel > 30) ? 1.0F : (this.experienceLevel / 30.0F);
/* 1702 */       level().playSound(null, getX(), getY(), getZ(), SoundEvents.PLAYER_LEVELUP, getSoundSource(), vol * 0.75F, 1.0F);
/* 1703 */       this.lastLevelUpTime = this.tickCount;
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getXpNeededForNextLevel() {
/* 1708 */     if (this.experienceLevel >= 30) {
/* 1709 */       return 112 + (this.experienceLevel - 30) * 9;
/*      */     }
/* 1711 */     if (this.experienceLevel >= 15) {
/* 1712 */       return 37 + (this.experienceLevel - 15) * 5;
/*      */     }
/* 1714 */     return 7 + this.experienceLevel * 2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void causeFoodExhaustion(float amount) {
/* 1726 */     if (this.abilities.invulnerable) {
/*      */       return;
/*      */     }
/*      */     
/* 1730 */     if (!level().isClientSide()) {
/* 1731 */       this.foodData.addExhaustion(amount);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void lungeForwardMaybe() {
/* 1737 */     if (hasEnoughFoodToDoExhaustiveManoeuvres()) {
/* 1738 */       super.lungeForwardMaybe();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 1743 */   protected boolean hasEnoughFoodToDoExhaustiveManoeuvres() { return (getFoodData().hasEnoughFood() || (getAbilities()).mayfly); }
/*      */ 
/*      */ 
/*      */   
/* 1747 */   public Optional<WardenSpawnTracker> getWardenSpawnTracker() { return Optional.empty(); }
/*      */ 
/*      */ 
/*      */   
/* 1751 */   public FoodData getFoodData() { return this.foodData; }
/*      */ 
/*      */ 
/*      */   
/* 1755 */   public boolean canEat(boolean canAlwaysEat) { return (this.abilities.invulnerable || canAlwaysEat || this.foodData.needsFood()); }
/*      */ 
/*      */ 
/*      */   
/* 1759 */   public boolean isHurt() { return (getHealth() > 0.0F && getHealth() < getMaxHealth()); }
/*      */ 
/*      */ 
/*      */   
/* 1763 */   public boolean mayBuild() { return this.abilities.mayBuild; }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayUseItemAt(BlockPos pos, Direction direction, ItemStack itemStack) {
/* 1768 */     if (this.abilities.mayBuild) {
/* 1769 */       return true;
/*      */     }
/*      */     
/* 1772 */     BlockPos target = pos.relative(direction.getOpposite());
/* 1773 */     BlockInWorld block = new BlockInWorld(level(), target, false);
/* 1774 */     return itemStack.canPlaceOnBlockInAdventureMode(block);
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getBaseExperienceReward(ServerLevel level) {
/* 1779 */     if (((Boolean)level.getGameRules().get(GameRules.KEEP_INVENTORY)).booleanValue() || isSpectator()) {
/* 1780 */       return 0;
/*      */     }
/*      */     
/* 1783 */     return Math.min(this.experienceLevel * 7, 100);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1789 */   protected boolean isAlwaysExperienceDropper() { return true; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1794 */   public boolean shouldShowName() { return true; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1801 */   protected Entity.MovementEmission getMovementEmission() { return (!this.abilities.flying && (!onGround() || !isDiscrete())) ? Entity.MovementEmission.ALL : Entity.MovementEmission.NONE; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onUpdateAbilities() {}
/*      */ 
/*      */ 
/*      */   
/* 1809 */   public Component getName() { return Component.literal(this.gameProfile.name()); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1814 */   public String getPlainTextName() { return this.gameProfile.name(); }
/*      */ 
/*      */ 
/*      */   
/* 1818 */   public PlayerEnderChestContainer getEnderChestInventory() { return this.enderChestInventory; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1825 */   protected boolean doesEmitEquipEvent(EquipmentSlot slot) { return (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR); }
/*      */ 
/*      */ 
/*      */   
/* 1829 */   public boolean addItem(ItemStack itemStack) { return this.inventory.add(itemStack); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1836 */   public boolean isSpectator() { return (gameMode() == GameType.SPECTATOR); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1841 */   public boolean canBeHitByProjectile() { return (!isSpectator() && super.canBeHitByProjectile()); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1846 */   public boolean isSwimming() { return (!this.abilities.flying && !isSpectator() && super.isSwimming()); }
/*      */ 
/*      */ 
/*      */   
/* 1850 */   public boolean isCreative() { return (gameMode() == GameType.CREATIVE); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1855 */   public boolean isPushedByFluid() { return !this.abilities.flying; }
/*      */ 
/*      */ 
/*      */   
/*      */   public Component getDisplayName() {
/* 1860 */     MutableComponent result = PlayerTeam.formatNameForTeam(getTeam(), getName());
/* 1861 */     return decorateDisplayNameComponent(result);
/*      */   }
/*      */   
/*      */   private MutableComponent decorateDisplayNameComponent(MutableComponent nameComponent) {
/* 1865 */     String name = getGameProfile().name();
/*      */     
/* 1867 */     return nameComponent.withStyle(s -> s
/* 1868 */         .withClickEvent(new ClickEvent.SuggestCommand("/tell " + name + " "))
/* 1869 */         .withHoverEvent(createHoverEvent())
/* 1870 */         .withInsertion(name));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1876 */   public String getScoreboardName() { return getGameProfile().name(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1881 */   protected void internalSetAbsorptionAmount(float absorptionAmount) { getEntityData().set(DATA_PLAYER_ABSORPTION_ID, Float.valueOf(absorptionAmount)); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1886 */   public float getAbsorptionAmount() { return ((Float)getEntityData().get(DATA_PLAYER_ABSORPTION_ID)).floatValue(); }
/*      */ 
/*      */ 
/*      */   
/*      */   public SlotAccess getSlot(int slot) {
/* 1891 */     if (slot == 499) {
/* 1892 */       return new SlotAccess()
/*      */         {
/*      */           public ItemStack get() {
/* 1895 */             return Player.this.containerMenu.getCarried();
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean set(ItemStack itemStack) {
/* 1900 */             Player.this.containerMenu.setCarried(itemStack);
/* 1901 */             return true;
/*      */           }
/*      */         };
/*      */     }
/* 1905 */     final int craftSlot = slot - 500;
/* 1906 */     if (craftSlot >= 0 && craftSlot < 4) {
/* 1907 */       return new SlotAccess()
/*      */         {
/*      */           public ItemStack get() {
/* 1910 */             return Player.this.inventoryMenu.getCraftSlots().getItem(craftSlot);
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean set(ItemStack itemStack) {
/* 1915 */             Player.this.inventoryMenu.getCraftSlots().setItem(craftSlot, itemStack);
/* 1916 */             Player.this.inventoryMenu.slotsChanged(Player.this.inventory);
/* 1917 */             return true;
/*      */           }
/*      */         };
/*      */     }
/* 1921 */     if (slot >= 0 && slot < this.inventory.getNonEquipmentItems().size()) {
/* 1922 */       return this.inventory.getSlot(slot);
/*      */     }
/* 1924 */     int enderSlot = slot - 200;
/* 1925 */     if (enderSlot >= 0 && enderSlot < this.enderChestInventory.getContainerSize()) {
/* 1926 */       return this.enderChestInventory.getSlot(enderSlot);
/*      */     }
/* 1928 */     return super.getSlot(slot);
/*      */   }
/*      */ 
/*      */   
/* 1932 */   public boolean isReducedDebugInfo() { return this.reducedDebugInfo; }
/*      */ 
/*      */ 
/*      */   
/* 1936 */   public void setReducedDebugInfo(boolean reducedDebugInfo) { this.reducedDebugInfo = reducedDebugInfo; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1941 */   public void setRemainingFireTicks(int remainingTicks) { super.setRemainingFireTicks(this.abilities.invulnerable ? Math.min(remainingTicks, 1) : remainingTicks); }
/*      */ 
/*      */   
/*      */   protected static Optional<Parrot.Variant> extractParrotVariant(CompoundTag tag) {
/* 1945 */     if (!tag.isEmpty()) {
/* 1946 */       EntityType<?> entityType = (EntityType)tag.read("id", EntityType.CODEC).orElse(null);
/* 1947 */       if (entityType == EntityType.PARROT) {
/* 1948 */         return tag.read("Variant", Parrot.Variant.LEGACY_CODEC);
/*      */       }
/*      */     } 
/* 1951 */     return Optional.empty();
/*      */   }
/*      */ 
/*      */   
/* 1955 */   protected static OptionalInt convertParrotVariant(Optional<Parrot.Variant> variant) { return (OptionalInt)variant.map(v -> OptionalInt.of(v.getId())).orElse(OptionalInt.empty()); }
/*      */ 
/*      */   
/*      */   private static Optional<Parrot.Variant> convertParrotVariant(OptionalInt variant) {
/* 1959 */     if (variant.isPresent()) {
/* 1960 */       return Optional.of(Parrot.Variant.byId(variant.getAsInt()));
/*      */     }
/* 1962 */     return Optional.empty();
/*      */   }
/*      */ 
/*      */   
/* 1966 */   public void setShoulderParrotLeft(Optional<Parrot.Variant> variant) { this.entityData.set(DATA_SHOULDER_PARROT_LEFT, convertParrotVariant(variant)); }
/*      */ 
/*      */ 
/*      */   
/* 1970 */   public Optional<Parrot.Variant> getShoulderParrotLeft() { return convertParrotVariant((OptionalInt)this.entityData.get(DATA_SHOULDER_PARROT_LEFT)); }
/*      */ 
/*      */ 
/*      */   
/* 1974 */   public void setShoulderParrotRight(Optional<Parrot.Variant> variant) { this.entityData.set(DATA_SHOULDER_PARROT_RIGHT, convertParrotVariant(variant)); }
/*      */ 
/*      */ 
/*      */   
/* 1978 */   public Optional<Parrot.Variant> getShoulderParrotRight() { return convertParrotVariant((OptionalInt)this.entityData.get(DATA_SHOULDER_PARROT_RIGHT)); }
/*      */ 
/*      */ 
/*      */   
/* 1982 */   public float getCurrentItemAttackStrengthDelay() { return (float)(1.0D / getAttributeValue(Attributes.ATTACK_SPEED) * 20.0D); }
/*      */ 
/*      */   
/*      */   public boolean cannotAttackWithItem(ItemStack itemStack, int tolerance) {
/* 1986 */     float requiredStrength = ((Float)itemStack.getOrDefault(DataComponents.MINIMUM_ATTACK_CHARGE, Float.valueOf(0.0F))).floatValue();
/* 1987 */     float optimisticStrength = (this.attackStrengthTicker + tolerance) / getCurrentItemAttackStrengthDelay();
/* 1988 */     return (requiredStrength > 0.0F && optimisticStrength < requiredStrength);
/*      */   }
/*      */ 
/*      */   
/* 1992 */   public float getAttackStrengthScale(float a) { return Mth.clamp((this.attackStrengthTicker + a) / getCurrentItemAttackStrengthDelay(), 0.0F, 1.0F); }
/*      */ 
/*      */ 
/*      */   
/* 1996 */   public float getItemSwapScale(float a) { return Mth.clamp((this.itemSwapTicker + a) / getCurrentItemAttackStrengthDelay(), 0.0F, 1.0F); }
/*      */ 
/*      */   
/*      */   public void resetAttackStrengthTicker() {
/* 2000 */     this.attackStrengthTicker = 0;
/* 2001 */     this.itemSwapTicker = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onAttack() {
/* 2006 */     resetOnlyAttackStrengthTicker();
/* 2007 */     super.onAttack();
/*      */   }
/*      */ 
/*      */   
/* 2011 */   public void resetOnlyAttackStrengthTicker() { this.attackStrengthTicker = 0; }
/*      */ 
/*      */ 
/*      */   
/* 2015 */   public ItemCooldowns getCooldowns() { return this.cooldowns; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2020 */   protected float getBlockSpeedFactor() { return (this.abilities.flying || isFallFlying()) ? 1.0F : super.getBlockSpeedFactor(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2025 */   public float getLuck() { return (float)getAttributeValue(Attributes.LUCK); }
/*      */ 
/*      */ 
/*      */   
/* 2029 */   public boolean canUseGameMasterBlocks() { return (this.abilities.instabuild && permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)); }
/*      */ 
/*      */ 
/*      */   
/* 2033 */   public PermissionSet permissions() { return PermissionSet.NO_PERMISSIONS; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2038 */   public ImmutableList<Pose> getDismountPoses() { return ImmutableList.of(Pose.STANDING, Pose.CROUCHING, Pose.SWIMMING); }
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemStack getProjectile(ItemStack heldWeapon) {
/* 2043 */     if (!(heldWeapon.getItem() instanceof ProjectileWeaponItem)) {
/* 2044 */       return ItemStack.EMPTY;
/*      */     }
/*      */     
/* 2047 */     Predicate<ItemStack> supportedProjectiles = ((ProjectileWeaponItem)heldWeapon.getItem()).getSupportedHeldProjectiles();
/* 2048 */     ItemStack heldProjectile = ProjectileWeaponItem.getHeldProjectile(this, supportedProjectiles);
/* 2049 */     if (!heldProjectile.isEmpty()) {
/* 2050 */       return heldProjectile;
/*      */     }
/*      */     
/* 2053 */     supportedProjectiles = ((ProjectileWeaponItem)heldWeapon.getItem()).getAllSupportedProjectiles();
/* 2054 */     for (int i = 0; i < this.inventory.getContainerSize(); i++) {
/* 2055 */       ItemStack itemStack = this.inventory.getItem(i);
/* 2056 */       if (supportedProjectiles.test(itemStack)) {
/* 2057 */         return itemStack;
/*      */       }
/*      */     } 
/* 2060 */     return hasInfiniteMaterials() ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
/*      */   }
/*      */ 
/*      */   
/*      */   public Vec3 getRopeHoldPosition(float partialTickTime) {
/* 2065 */     double xOff = 0.22D * ((getMainArm() == HumanoidArm.RIGHT) ? -1.0D : 1.0D);
/* 2066 */     float xRot = Mth.lerp(partialTickTime * 0.5F, getXRot(), this.xRotO) * 0.017453292F;
/* 2067 */     float yRot = Mth.lerp(partialTickTime, this.yBodyRotO, this.yBodyRot) * 0.017453292F;
/* 2068 */     if (isFallFlying() || isAutoSpinAttack()) {
/*      */       float zRot;
/* 2070 */       Vec3 lookAngle = getViewVector(partialTickTime);
/* 2071 */       Vec3 movement = getDeltaMovement();
/* 2072 */       double speedLen = movement.horizontalDistanceSqr();
/* 2073 */       double lookLen = lookAngle.horizontalDistanceSqr();
/*      */       
/* 2075 */       if (speedLen > 0.0D && lookLen > 0.0D) {
/* 2076 */         double dot = (movement.x * lookAngle.x + movement.z * lookAngle.z) / Math.sqrt(speedLen * lookLen);
/* 2077 */         double sign = movement.x * lookAngle.z - movement.z * lookAngle.x;
/* 2078 */         zRot = (float)(Math.signum(sign) * Math.acos(dot));
/*      */       } else {
/* 2080 */         zRot = 0.0F;
/*      */       } 
/* 2082 */       return getPosition(partialTickTime).add((new Vec3(xOff, -0.11D, 0.85D)).zRot(-zRot).xRot(-xRot).yRot(-yRot));
/* 2083 */     }  if (isVisuallySwimming()) {
/* 2084 */       return getPosition(partialTickTime).add((new Vec3(xOff, 0.2D, -0.15D)).xRot(-xRot).yRot(-yRot));
/*      */     }
/* 2086 */     double yOff = getBoundingBox().getYsize() - 1.0D;
/* 2087 */     double zOff = isCrouching() ? -0.2D : 0.07D;
/* 2088 */     return getPosition(partialTickTime).add((new Vec3(xOff, yOff, zOff)).yRot(-yRot));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2094 */   public boolean isAlwaysTicking() { return true; }
/*      */ 
/*      */ 
/*      */   
/* 2098 */   public boolean isScoping() { return (isUsingItem() && getUseItem().is(Items.SPYGLASS)); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2103 */   public boolean shouldBeSaved() { return false; }
/*      */ 
/*      */ 
/*      */   
/* 2107 */   public Optional<GlobalPos> getLastDeathLocation() { return this.lastDeathLocation; }
/*      */ 
/*      */ 
/*      */   
/* 2111 */   public void setLastDeathLocation(Optional<GlobalPos> pos) { this.lastDeathLocation = pos; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2116 */   public float getHurtDir() { return this.hurtDir; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void animateHurt(float yaw) {
/* 2121 */     super.animateHurt(yaw);
/* 2122 */     this.hurtDir = yaw;
/*      */   }
/*      */ 
/*      */   
/* 2126 */   public boolean isMobilityRestricted() { return hasEffect(MobEffects.BLINDNESS); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2131 */   public boolean canSprint() { return true; }
/*      */ 
/*      */ 
/*      */   
/*      */   protected float getFlyingSpeed() {
/* 2136 */     if (this.abilities.flying && !isPassenger()) {
/* 2137 */       return isSprinting() ? (this.abilities.getFlyingSpeed() * 2.0F) : this.abilities.getFlyingSpeed();
/*      */     }
/* 2139 */     return isSprinting() ? 0.025999999F : 0.02F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2145 */   public boolean hasContainerOpen(ContainerOpenersCounter container, BlockPos blockPos) { return container.isOwnContainer(this); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2150 */   public double getContainerInteractionRange() { return blockInteractionRange(); }
/*      */ 
/*      */ 
/*      */   
/* 2154 */   public double blockInteractionRange() { return getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE); }
/*      */ 
/*      */ 
/*      */   
/* 2158 */   public double entityInteractionRange() { return getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE); }
/*      */ 
/*      */   
/*      */   public boolean isWithinEntityInteractionRange(Entity entity, double buffer) {
/* 2162 */     if (entity.isRemoved()) {
/* 2163 */       return false;
/*      */     }
/* 2165 */     return isWithinEntityInteractionRange(entity.getBoundingBox(), buffer);
/*      */   }
/*      */   
/*      */   public boolean isWithinEntityInteractionRange(AABB aabb, double buffer) {
/* 2169 */     double maxRange = entityInteractionRange() + buffer;
/* 2170 */     double distanceToSq = aabb.distanceToSqr(getEyePosition());
/* 2171 */     return (distanceToSq < maxRange * maxRange);
/*      */   }
/*      */ 
/*      */   
/* 2175 */   public boolean isWithinAttackRange(AABB aabb, double buffer) { return entityAttackRange().isInRange(this, aabb, buffer); }
/*      */ 
/*      */   
/*      */   public boolean isWithinBlockInteractionRange(BlockPos pos, double buffer) {
/* 2179 */     double maxRange = blockInteractionRange() + buffer; return 
/* 2180 */       ((new AABB(pos)).distanceToSqr(getEyePosition()) < maxRange * maxRange);
/*      */   }
/*      */   
/*      */   public void setIgnoreFallDamageFromCurrentImpulse(boolean ignoreFallDamage) {
/* 2184 */     this.ignoreFallDamageFromCurrentImpulse = ignoreFallDamage;
/* 2185 */     if (ignoreFallDamage) {
/* 2186 */       applyPostImpulseGraceTime(40);
/*      */     } else {
/* 2188 */       this.currentImpulseContextResetGraceTime = 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 2193 */   public void applyPostImpulseGraceTime(int ticks) { this.currentImpulseContextResetGraceTime = Math.max(this.currentImpulseContextResetGraceTime, ticks); }
/*      */ 
/*      */ 
/*      */   
/* 2197 */   public boolean isIgnoringFallDamageFromCurrentImpulse() { return this.ignoreFallDamageFromCurrentImpulse; }
/*      */ 
/*      */   
/*      */   public void tryResetCurrentImpulseContext() {
/* 2201 */     if (this.currentImpulseContextResetGraceTime == 0) {
/* 2202 */       resetCurrentImpulseContext();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 2207 */   public boolean isInPostImpulseGraceTime() { return (this.currentImpulseContextResetGraceTime > 0); }
/*      */ 
/*      */   
/*      */   public void resetCurrentImpulseContext() {
/* 2211 */     this.currentImpulseContextResetGraceTime = 0;
/* 2212 */     this.currentExplosionCause = null;
/* 2213 */     this.currentImpulseImpactPos = null;
/* 2214 */     this.ignoreFallDamageFromCurrentImpulse = false;
/*      */   }
/*      */ 
/*      */   
/* 2218 */   public boolean shouldRotateWithMinecart() { return false; }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean onClimbable() {
/* 2223 */     if (this.abilities.flying) {
/* 2224 */       return false;
/*      */     }
/* 2226 */     return super.onClimbable();
/*      */   }
/*      */ 
/*      */   
/* 2230 */   public String debugInfo() { return MoreObjects.toStringHelper(this)
/* 2231 */       .add("name", getPlainTextName())
/* 2232 */       .add("id", getId())
/* 2233 */       .add("pos", position())
/* 2234 */       .add("mode", gameMode())
/* 2235 */       .add("permission", permissions())
/* 2236 */       .toString(); }
/*      */   
/*      */   public abstract GameType gameMode();
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\player\Player.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */