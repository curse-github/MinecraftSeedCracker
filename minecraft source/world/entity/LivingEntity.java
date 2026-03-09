/*      */ package net.minecraft.world.entity;
/*      */ 
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.DataResult;
/*      */ import com.mojang.serialization.Dynamic;
/*      */ import com.mojang.serialization.JavaOps;
/*      */ import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
/*      */ import it.unimi.dsi.fastutil.objects.Object2LongMap;
/*      */ import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*      */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*      */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*      */ import java.util.Collection;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.EnumMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.Supplier;
/*      */ import net.minecraft.advancements.CriteriaTriggers;
/*      */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.particles.BlockParticleOption;
/*      */ import net.minecraft.core.particles.ItemParticleOption;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.nbt.NbtOps;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.level.ServerChunkCache;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.server.waypoints.ServerWaypointManager;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.DamageTypeTags;
/*      */ import net.minecraft.tags.EntityTypeTags;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.BlockUtil;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.damagesource.CombatRules;
/*      */ import net.minecraft.world.damagesource.CombatTracker;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.damagesource.DamageTypes;
/*      */ import net.minecraft.world.effect.MobEffect;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.effect.MobEffectUtil;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.ai.Brain;
/*      */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeMap;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
/*      */ import net.minecraft.world.entity.animal.wolf.Wolf;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.projectile.Projectile;
/*      */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.component.AttackRange;
/*      */ import net.minecraft.world.item.component.BlocksAttacks;
/*      */ import net.minecraft.world.item.component.DeathProtection;
/*      */ import net.minecraft.world.item.component.KineticWeapon;
/*      */ import net.minecraft.world.item.component.Weapon;
/*      */ import net.minecraft.world.item.enchantment.Enchantment;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
/*      */ import net.minecraft.world.item.equipment.Equippable;
/*      */ import net.minecraft.world.level.ClipContext;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.block.BedBlock;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.HoneyBlock;
/*      */ import net.minecraft.world.level.block.LadderBlock;
/*      */ import net.minecraft.world.level.block.PowderSnowBlock;
/*      */ import net.minecraft.world.level.block.SoundType;
/*      */ import net.minecraft.world.level.block.TrapDoorBlock;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.level.storage.loot.LootParams;
/*      */ import net.minecraft.world.level.storage.loot.LootTable;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.HitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ import net.minecraft.world.scores.PlayerTeam;
/*      */ import net.minecraft.world.scores.Scoreboard;
/*      */ import net.minecraft.world.waypoints.Waypoint;
/*      */ import net.minecraft.world.waypoints.WaypointTransmitter;
/*      */ import org.jetbrains.annotations.Contract;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public abstract class LivingEntity
/*      */   extends Entity
/*      */   implements Attackable, WaypointTransmitter
/*      */ {
/*  149 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   private static final String TAG_ACTIVE_EFFECTS = "active_effects";
/*      */   
/*      */   public static final String TAG_ATTRIBUTES = "attributes";
/*      */   public static final String TAG_SLEEPING_POS = "sleeping_pos";
/*      */   public static final String TAG_EQUIPMENT = "equipment";
/*      */   public static final String TAG_BRAIN = "Brain";
/*      */   public static final String TAG_FALL_FLYING = "FallFlying";
/*      */   public static final String TAG_HURT_TIME = "HurtTime";
/*      */   public static final String TAG_DEATH_TIME = "DeathTime";
/*      */   public static final String TAG_HURT_BY_TIMESTAMP = "HurtByTimestamp";
/*      */   public static final String TAG_HEALTH = "Health";
/*  162 */   private static final Identifier SPEED_MODIFIER_POWDER_SNOW_ID = Identifier.withDefaultNamespace("powder_snow");
/*  163 */   private static final Identifier SPRINTING_MODIFIER_ID = Identifier.withDefaultNamespace("sprinting");
/*  164 */   private static final AttributeModifier SPEED_MODIFIER_SPRINTING = new AttributeModifier(SPRINTING_MODIFIER_ID, 0.30000001192092896D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
/*      */   
/*      */   public static final int EQUIPMENT_SLOT_OFFSET = 98;
/*      */   
/*      */   public static final int ARMOR_SLOT_OFFSET = 100;
/*      */   
/*      */   public static final int BODY_ARMOR_OFFSET = 105;
/*      */   
/*      */   public static final int SADDLE_OFFSET = 106;
/*      */   
/*      */   public static final int PLAYER_HURT_EXPERIENCE_TIME = 100;
/*      */   private static final int DAMAGE_SOURCE_TIMEOUT = 40;
/*      */   public static final double MIN_MOVEMENT_DISTANCE = 0.003D;
/*      */   public static final double DEFAULT_BASE_GRAVITY = 0.08D;
/*      */   public static final int DEATH_DURATION = 20;
/*      */   protected static final float INPUT_FRICTION = 0.98F;
/*      */   private static final int TICKS_PER_ELYTRA_FREE_FALL_EVENT = 10;
/*      */   private static final int FREE_FALL_EVENTS_PER_ELYTRA_BREAK = 2;
/*      */   public static final float BASE_JUMP_POWER = 0.42F;
/*      */   protected static final float DEFAULT_KNOCKBACK = 0.4F;
/*      */   protected static final int INVULNERABLE_DURATION = 20;
/*      */   private static final double MAX_LINE_OF_SIGHT_TEST_RANGE = 128.0D;
/*      */   protected static final int LIVING_ENTITY_FLAG_IS_USING = 1;
/*      */   protected static final int LIVING_ENTITY_FLAG_OFF_HAND = 2;
/*      */   protected static final int LIVING_ENTITY_FLAG_SPIN_ATTACK = 4;
/*  189 */   protected static final EntityDataAccessor<Byte> DATA_LIVING_ENTITY_FLAGS = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BYTE);
/*  190 */   private static final EntityDataAccessor<Float> DATA_HEALTH_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.FLOAT);
/*  191 */   private static final EntityDataAccessor<List<ParticleOptions>> DATA_EFFECT_PARTICLES = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.PARTICLES);
/*  192 */   private static final EntityDataAccessor<Boolean> DATA_EFFECT_AMBIENCE_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
/*  193 */   private static final EntityDataAccessor<Integer> DATA_ARROW_COUNT_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
/*  194 */   private static final EntityDataAccessor<Integer> DATA_STINGER_COUNT_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
/*  195 */   private static final EntityDataAccessor<Optional<BlockPos>> SLEEPING_POS_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
/*      */   private static final int PARTICLE_FREQUENCY_WHEN_INVISIBLE = 15;
/*  197 */   protected static final EntityDimensions SLEEPING_DIMENSIONS = EntityDimensions.fixed(0.2F, 0.2F).withEyeHeight(0.2F);
/*      */   public static final float EXTRA_RENDER_CULLING_SIZE_WITH_BIG_HAT = 0.5F;
/*      */   public static final float DEFAULT_BABY_SCALE = 0.5F;
/*      */   private static final float WATER_FLOAT_IMPULSE = 0.04F;
/*      */   public static final Predicate<LivingEntity> PLAYER_NOT_WEARING_DISGUISE_ITEM = livingEntity -> {
/*      */       Player player;
/*  203 */       if (livingEntity instanceof Player) { player = (Player)livingEntity; }
/*  204 */       else { return true; }
/*      */       
/*  206 */       ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
/*  207 */       return !helmet.is(ItemTags.GAZE_DISGUISE_EQUIPMENT);
/*      */     };
/*      */   
/*  210 */   private static final Dynamic<?> EMPTY_BRAIN = new Dynamic(JavaOps.INSTANCE, Map.of("memories", Map.of()));
/*      */   
/*      */   private final AttributeMap attributes;
/*  213 */   private final CombatTracker combatTracker = new CombatTracker(this);
/*  214 */   private final Map<Holder<MobEffect>, MobEffectInstance> activeEffects = Maps.newHashMap();
/*  215 */   private final Map<EquipmentSlot, ItemStack> lastEquipmentItems = Util.makeEnumMap(EquipmentSlot.class, slot -> ItemStack.EMPTY);
/*      */   public boolean swinging;
/*      */   private boolean discardFriction = false;
/*      */   public InteractionHand swingingArm;
/*      */   public int swingTime;
/*      */   public int removeArrowTime;
/*      */   public int removeStingerTime;
/*      */   public int hurtTime;
/*      */   public int hurtDuration;
/*      */   public int deathTime;
/*      */   public float oAttackAnim;
/*      */   public float attackAnim;
/*      */   protected int attackStrengthTicker;
/*      */   protected int itemSwapTicker;
/*  229 */   public final WalkAnimationState walkAnimation = new WalkAnimationState();
/*      */   public float yBodyRot;
/*      */   public float yBodyRotO;
/*      */   public float yHeadRot;
/*      */   public float yHeadRotO;
/*  234 */   public final ElytraAnimationState elytraAnimationState = new ElytraAnimationState(this);
/*      */   
/*      */   protected EntityReference<Player> lastHurtByPlayer;
/*      */   
/*      */   protected int lastHurtByPlayerMemoryTime;
/*      */   
/*      */   protected boolean dead;
/*      */   protected int noActionTime;
/*      */   protected float lastHurt;
/*      */   protected boolean jumping;
/*      */   public float xxa;
/*      */   public float yya;
/*      */   public float zza;
/*  247 */   protected InterpolationHandler interpolation = new InterpolationHandler(this);
/*      */   protected double lerpYHeadRot;
/*      */   protected int lerpHeadSteps;
/*      */   private boolean effectsDirty = true;
/*      */   private EntityReference<LivingEntity> lastHurtByMob;
/*      */   private int lastHurtByMobTimestamp;
/*      */   private LivingEntity lastHurtMob;
/*      */   private int lastHurtMobTimestamp;
/*      */   private float speed;
/*      */   private int noJumpDelay;
/*      */   private float absorptionAmount;
/*  258 */   protected ItemStack useItem = ItemStack.EMPTY;
/*      */   protected int useItemRemaining;
/*      */   protected int fallFlyTicks;
/*  261 */   private long lastKineticHitFeedbackTime = -2147483648L;
/*      */   private BlockPos lastPos;
/*  263 */   private Optional<BlockPos> lastClimbablePos = Optional.empty();
/*      */   private DamageSource lastDamageSource;
/*      */   private long lastDamageStamp;
/*      */   protected int autoSpinAttackTicks;
/*      */   protected float autoSpinAttackDmg;
/*      */   protected ItemStack autoSpinAttackItemStack;
/*      */   protected Object2LongMap<Entity> recentKineticEnemies;
/*      */   private float swimAmount;
/*      */   private float swimAmountO;
/*      */   protected Brain<?> brain;
/*      */   private boolean skipDropExperience;
/*  274 */   private final EnumMap<EquipmentSlot, Reference2ObjectMap<Enchantment, Set<EnchantmentLocationBasedEffect>>> activeLocationDependentEnchantments = new EnumMap(EquipmentSlot.class);
/*      */   protected final EntityEquipment equipment;
/*  276 */   private Waypoint.Icon locatorBarIcon = new Waypoint.Icon();
/*      */   
/*      */   protected LivingEntity(EntityType<? extends LivingEntity> type, Level level) {
/*  279 */     super(type, level);
/*      */     
/*  281 */     this.attributes = new AttributeMap(DefaultAttributes.getSupplier(type));
/*  282 */     setHealth(getMaxHealth());
/*      */     
/*  284 */     this.equipment = createEquipment();
/*      */     
/*  286 */     this.blocksBuilding = true;
/*  287 */     reapplyPosition();
/*  288 */     setYRot(this.random.nextFloat() * 6.2831855F);
/*  289 */     this.yHeadRot = getYRot();
/*      */ 
/*      */     
/*  292 */     this.brain = makeBrain(EMPTY_BRAIN);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  297 */   public LivingEntity asLivingEntity() { return this; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract(pure = true)
/*  303 */   protected EntityEquipment createEquipment() { return new EntityEquipment(); }
/*      */ 
/*      */ 
/*      */   
/*  307 */   public Brain<?> getBrain() { return this.brain; }
/*      */ 
/*      */ 
/*      */   
/*  311 */   protected Brain.Provider<?> brainProvider() { return Brain.provider(ImmutableList.of(), ImmutableList.of()); }
/*      */ 
/*      */ 
/*      */   
/*  315 */   protected Brain<?> makeBrain(Dynamic<?> input) { return brainProvider().makeBrain(input); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  320 */   public void kill(ServerLevel level) { hurtServer(level, damageSources().genericKill(), Float.MAX_VALUE); }
/*      */ 
/*      */ 
/*      */   
/*  324 */   public boolean canAttackType(EntityType<?> targetType) { return true; }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  329 */     entityData.define(DATA_LIVING_ENTITY_FLAGS, Byte.valueOf((byte)0));
/*  330 */     entityData.define(DATA_EFFECT_PARTICLES, List.of());
/*  331 */     entityData.define(DATA_EFFECT_AMBIENCE_ID, Boolean.valueOf(false));
/*  332 */     entityData.define(DATA_ARROW_COUNT_ID, Integer.valueOf(0));
/*  333 */     entityData.define(DATA_STINGER_COUNT_ID, Integer.valueOf(0));
/*  334 */     entityData.define(DATA_HEALTH_ID, Float.valueOf(1.0F));
/*  335 */     entityData.define(SLEEPING_POS_ID, Optional.empty());
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createLivingAttributes() {
/*  339 */     return AttributeSupplier.builder()
/*  340 */       .add(Attributes.MAX_HEALTH)
/*  341 */       .add(Attributes.KNOCKBACK_RESISTANCE)
/*  342 */       .add(Attributes.MOVEMENT_SPEED)
/*  343 */       .add(Attributes.ARMOR)
/*  344 */       .add(Attributes.ARMOR_TOUGHNESS)
/*  345 */       .add(Attributes.MAX_ABSORPTION)
/*  346 */       .add(Attributes.STEP_HEIGHT)
/*  347 */       .add(Attributes.SCALE)
/*  348 */       .add(Attributes.GRAVITY)
/*  349 */       .add(Attributes.SAFE_FALL_DISTANCE)
/*  350 */       .add(Attributes.FALL_DAMAGE_MULTIPLIER)
/*  351 */       .add(Attributes.JUMP_STRENGTH)
/*  352 */       .add(Attributes.OXYGEN_BONUS)
/*  353 */       .add(Attributes.BURNING_TIME)
/*  354 */       .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE)
/*  355 */       .add(Attributes.WATER_MOVEMENT_EFFICIENCY)
/*  356 */       .add(Attributes.MOVEMENT_EFFICIENCY)
/*  357 */       .add(Attributes.ATTACK_KNOCKBACK)
/*  358 */       .add(Attributes.CAMERA_DISTANCE)
/*  359 */       .add(Attributes.WAYPOINT_TRANSMIT_RANGE);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
/*  365 */     if (!isInWater())
/*      */     {
/*  367 */       updateInWaterStateAndDoWaterCurrentPushing();
/*      */     }
/*      */     
/*  370 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1; if (onGround && this.fallDistance > 0.0D) {
/*      */         
/*  372 */         onChangedBlock(level, pos);
/*      */         
/*  374 */         double power = Math.max(0, Mth.floor(calculateFallPower(this.fallDistance)));
/*  375 */         if (power > 0.0D && !onState.isAir()) {
/*  376 */           double x = getX();
/*  377 */           double y = getY();
/*  378 */           double z = getZ();
/*      */           
/*  380 */           BlockPos entityPos = blockPosition();
/*  381 */           if (pos.getX() != entityPos.getX() || pos.getZ() != entityPos.getZ()) {
/*  382 */             double xDiff = x - pos.getX() - 0.5D;
/*  383 */             double zDiff = z - pos.getZ() - 0.5D;
/*  384 */             double maxDiff = Math.max(Math.abs(xDiff), Math.abs(zDiff));
/*      */             
/*  386 */             x = pos.getX() + 0.5D + xDiff / maxDiff * 0.5D;
/*  387 */             z = pos.getZ() + 0.5D + zDiff / maxDiff * 0.5D;
/*      */           } 
/*      */           
/*  390 */           double scale = Math.min(0.20000000298023224D + power / 15.0D, 2.5D);
/*  391 */           int particles = (int)(150.0D * scale);
/*  392 */           level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, onState), x, y, z, particles, 0.0D, 0.0D, 0.0D, 0.15000000596046448D);
/*      */         } 
/*      */       }  }
/*      */     
/*  396 */     super.checkFallDamage(ya, onGround, onState, pos);
/*      */     
/*  398 */     if (onGround) {
/*  399 */       this.lastClimbablePos = Optional.empty();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  404 */   public boolean canBreatheUnderwater() { return getType().is(EntityTypeTags.CAN_BREATHE_UNDER_WATER); }
/*      */ 
/*      */ 
/*      */   
/*  408 */   public float getSwimAmount(float a) { return Mth.lerp(a, this.swimAmountO, this.swimAmount); }
/*      */ 
/*      */ 
/*      */   
/*  412 */   public boolean hasLandedInLiquid() { return (getDeltaMovement().y() < 9.999999747378752E-6D && isInLiquid()); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void baseTick() {
/*  417 */     this.oAttackAnim = this.attackAnim;
/*      */     
/*  419 */     if (this.firstTick) {
/*  420 */       getSleepingPos().ifPresent(this::setPosToBed);
/*      */     }
/*      */     
/*  423 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1;
/*      */       
/*  425 */       EnchantmentHelper.tickEffects(serverLevel, this); }
/*      */ 
/*      */     
/*  428 */     super.baseTick();
/*      */     
/*  430 */     ProfilerFiller profiler = Profiler.get();
/*  431 */     profiler.push("livingEntityBaseTick");
/*      */     
/*  433 */     if (isAlive()) { Level level2 = level(); if (level2 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level2;
/*  434 */         boolean isPlayer = this instanceof Player;
/*  435 */         if (isInWall()) {
/*  436 */           hurtServer(level, damageSources().inWall(), 1.0F);
/*      */         }
/*  438 */         else if (isPlayer && !level.getWorldBorder().isWithinBounds(getBoundingBox())) {
/*  439 */           double dist = level.getWorldBorder().getDistanceToBorder(this) + level.getWorldBorder().getSafeZone();
/*  440 */           if (dist < 0.0D) {
/*  441 */             double damagePerBlock = level.getWorldBorder().getDamagePerBlock();
/*  442 */             if (damagePerBlock > 0.0D) {
/*  443 */               hurtServer(level, damageSources().outOfBorder(), Math.max(1, Mth.floor(-dist * damagePerBlock)));
/*      */             }
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  449 */         if (isEyeInFluid(FluidTags.WATER) && !level.getBlockState(BlockPos.containing(getX(), getEyeY(), getZ())).is(Blocks.BUBBLE_COLUMN)) {
/*  450 */           boolean canDrownInWater = (!canBreatheUnderwater() && !MobEffectUtil.hasWaterBreathing(this) && (!isPlayer || !(((Player)this).getAbilities()).invulnerable));
/*  451 */           if (canDrownInWater) {
/*  452 */             setAirSupply(decreaseAirSupply(getAirSupply()));
/*  453 */             if (shouldTakeDrowningDamage()) {
/*  454 */               setAirSupply(0);
/*  455 */               level.broadcastEntityEvent(this, (byte)67);
/*  456 */               hurtServer(level, damageSources().drown(), 2.0F);
/*      */             } 
/*  458 */           } else if (getAirSupply() < getMaxAirSupply() && MobEffectUtil.shouldEffectsRefillAirsupply(this)) {
/*  459 */             setAirSupply(increaseAirSupply(getAirSupply()));
/*      */           } 
/*      */           
/*  462 */           if (isPassenger() && getVehicle() != null && getVehicle().dismountsUnderwater()) {
/*  463 */             stopRiding();
/*      */           }
/*  465 */         } else if (getAirSupply() < getMaxAirSupply()) {
/*  466 */           setAirSupply(increaseAirSupply(getAirSupply()));
/*      */         } 
/*      */         
/*  469 */         BlockPos pos = blockPosition();
/*  470 */         if (!Objects.equal(this.lastPos, pos)) {
/*  471 */           this.lastPos = pos;
/*  472 */           onChangedBlock(level, pos);
/*      */         }  }
/*      */        }
/*      */     
/*  476 */     if (this.hurtTime > 0) {
/*  477 */       this.hurtTime--;
/*      */     }
/*  479 */     if (this.invulnerableTime > 0 && !(this instanceof ServerPlayer)) {
/*  480 */       this.invulnerableTime--;
/*      */     }
/*  482 */     if (isDeadOrDying() && level().shouldTickDeath(this)) {
/*  483 */       tickDeath();
/*      */     }
/*  485 */     if (this.lastHurtByPlayerMemoryTime > 0) {
/*  486 */       this.lastHurtByPlayerMemoryTime--;
/*      */     } else {
/*  488 */       this.lastHurtByPlayer = null;
/*      */     } 
/*  490 */     if (this.lastHurtMob != null && !this.lastHurtMob.isAlive()) {
/*  491 */       this.lastHurtMob = null;
/*      */     }
/*      */     
/*  494 */     LivingEntity hurtByMob = getLastHurtByMob();
/*  495 */     if (hurtByMob != null) {
/*  496 */       if (!hurtByMob.isAlive()) {
/*  497 */         setLastHurtByMob(null);
/*  498 */       } else if (this.tickCount - this.lastHurtByMobTimestamp > 100) {
/*  499 */         setLastHurtByMob(null);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  504 */     tickEffects();
/*      */     
/*  506 */     this.yHeadRotO = this.yHeadRot;
/*  507 */     this.yBodyRotO = this.yBodyRot;
/*  508 */     this.yRotO = getYRot();
/*  509 */     this.xRotO = getXRot();
/*      */     
/*  511 */     profiler.pop();
/*      */   }
/*      */ 
/*      */   
/*  515 */   protected boolean shouldTakeDrowningDamage() { return (getAirSupply() <= -20); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  520 */   protected float getBlockSpeedFactor() { return Mth.lerp((float)getAttributeValue(Attributes.MOVEMENT_EFFICIENCY), super.getBlockSpeedFactor(), 1.0F); }
/*      */ 
/*      */ 
/*      */   
/*  524 */   public float getLuck() { return 0.0F; }
/*      */ 
/*      */   
/*      */   protected void removeFrost() {
/*  528 */     AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
/*      */     
/*  530 */     if (speed == null) {
/*      */       return;
/*      */     }
/*      */     
/*  534 */     if (speed.getModifier(SPEED_MODIFIER_POWDER_SNOW_ID) != null) {
/*  535 */       speed.removeModifier(SPEED_MODIFIER_POWDER_SNOW_ID);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void tryAddFrost() {
/*  540 */     if (!getBlockStateOnLegacy().isAir()) {
/*  541 */       int ticksFrozen = getTicksFrozen();
/*  542 */       if (ticksFrozen > 0) {
/*  543 */         AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
/*      */         
/*  545 */         if (speed == null) {
/*      */           return;
/*      */         }
/*      */         
/*  549 */         float slowAmount = -0.05F * getPercentFrozen();
/*  550 */         speed.addTransientModifier(new AttributeModifier(SPEED_MODIFIER_POWDER_SNOW_ID, slowAmount, AttributeModifier.Operation.ADD_VALUE));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  556 */   protected void onChangedBlock(ServerLevel level, BlockPos pos) { EnchantmentHelper.runLocationChangedEffects(level, this); }
/*      */ 
/*      */ 
/*      */   
/*  560 */   public boolean isBaby() { return false; }
/*      */ 
/*      */ 
/*      */   
/*  564 */   public float getAgeScale() { return isBaby() ? 0.5F : 1.0F; }
/*      */ 
/*      */   
/*      */   public final float getScale() {
/*  568 */     AttributeMap attributes = getAttributes();
/*  569 */     if (attributes == null)
/*      */     {
/*  571 */       return 1.0F;
/*      */     }
/*  573 */     return sanitizeScale((float)attributes.getValue(Attributes.SCALE));
/*      */   }
/*      */ 
/*      */   
/*  577 */   protected float sanitizeScale(float scale) { return scale; }
/*      */ 
/*      */ 
/*      */   
/*  581 */   public boolean isAffectedByFluids() { return true; }
/*      */ 
/*      */   
/*      */   protected void tickDeath() {
/*  585 */     this.deathTime++;
/*  586 */     if (this.deathTime >= 20 && !level().isClientSide() && !isRemoved()) {
/*  587 */       level().broadcastEntityEvent(this, (byte)60);
/*  588 */       remove(Entity.RemovalReason.KILLED);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  593 */   public boolean shouldDropExperience() { return !isBaby(); }
/*      */ 
/*      */ 
/*      */   
/*  597 */   protected boolean shouldDropLoot(ServerLevel level) { return (!isBaby() && ((Boolean)level.getGameRules().get(GameRules.MOB_DROPS)).booleanValue()); }
/*      */   
/*      */   protected int decreaseAirSupply(int currentSupply) {
/*      */     double oxygenBonus;
/*  601 */     AttributeInstance respiration = getAttribute(Attributes.OXYGEN_BONUS);
/*      */     
/*  603 */     if (respiration != null) {
/*  604 */       oxygenBonus = respiration.getValue();
/*      */     } else {
/*  606 */       oxygenBonus = 0.0D;
/*      */     } 
/*  608 */     if (oxygenBonus > 0.0D && 
/*  609 */       this.random.nextDouble() >= 1.0D / (oxygenBonus + 1.0D))
/*      */     {
/*  611 */       return currentSupply;
/*      */     }
/*      */     
/*  614 */     return currentSupply - 1;
/*      */   }
/*      */ 
/*      */   
/*  618 */   protected int increaseAirSupply(int currentSupply) { return Math.min(currentSupply + 4, getMaxAirSupply()); }
/*      */ 
/*      */ 
/*      */   
/*  622 */   public final int getExperienceReward(ServerLevel level, Entity killer) { return EnchantmentHelper.processMobExperience(level, killer, this, getBaseExperienceReward(level)); }
/*      */ 
/*      */ 
/*      */   
/*  626 */   protected int getBaseExperienceReward(ServerLevel level) { return 0; }
/*      */ 
/*      */ 
/*      */   
/*  630 */   protected boolean isAlwaysExperienceDropper() { return false; }
/*      */ 
/*      */ 
/*      */   
/*  634 */   public LivingEntity getLastHurtByMob() { return EntityReference.getLivingEntity(this.lastHurtByMob, level()); }
/*      */ 
/*      */ 
/*      */   
/*  638 */   public Player getLastHurtByPlayer() { return EntityReference.getPlayer(this.lastHurtByPlayer, level()); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  643 */   public LivingEntity getLastAttacker() { return getLastHurtByMob(); }
/*      */ 
/*      */ 
/*      */   
/*  647 */   public int getLastHurtByMobTimestamp() { return this.lastHurtByMobTimestamp; }
/*      */ 
/*      */ 
/*      */   
/*  651 */   public void setLastHurtByPlayer(Player player, int timeToRemember) { setLastHurtByPlayer(EntityReference.of(player), timeToRemember); }
/*      */ 
/*      */ 
/*      */   
/*  655 */   public void setLastHurtByPlayer(UUID player, int timeToRemember) { setLastHurtByPlayer(EntityReference.of(player), timeToRemember); }
/*      */ 
/*      */   
/*      */   private void setLastHurtByPlayer(EntityReference<Player> player, int timeToRemember) {
/*  659 */     this.lastHurtByPlayer = player;
/*  660 */     this.lastHurtByPlayerMemoryTime = timeToRemember;
/*      */   }
/*      */   
/*      */   public void setLastHurtByMob(LivingEntity hurtBy) {
/*  664 */     this.lastHurtByMob = EntityReference.of(hurtBy);
/*  665 */     this.lastHurtByMobTimestamp = this.tickCount;
/*      */   }
/*      */ 
/*      */   
/*  669 */   public LivingEntity getLastHurtMob() { return this.lastHurtMob; }
/*      */ 
/*      */ 
/*      */   
/*  673 */   public int getLastHurtMobTimestamp() { return this.lastHurtMobTimestamp; }
/*      */ 
/*      */   
/*      */   public void setLastHurtMob(Entity target) {
/*  677 */     if (target instanceof LivingEntity) {
/*  678 */       this.lastHurtMob = (LivingEntity)target;
/*      */     } else {
/*  680 */       this.lastHurtMob = null;
/*      */     } 
/*  682 */     this.lastHurtMobTimestamp = this.tickCount;
/*      */   }
/*      */ 
/*      */   
/*  686 */   public int getNoActionTime() { return this.noActionTime; }
/*      */ 
/*      */ 
/*      */   
/*  690 */   public void setNoActionTime(int noActionTime) { this.noActionTime = noActionTime; }
/*      */ 
/*      */ 
/*      */   
/*  694 */   public boolean shouldDiscardFriction() { return this.discardFriction; }
/*      */ 
/*      */ 
/*      */   
/*  698 */   public void setDiscardFriction(boolean discardFriction) { this.discardFriction = discardFriction; }
/*      */ 
/*      */ 
/*      */   
/*  702 */   protected boolean doesEmitEquipEvent(EquipmentSlot slot) { return true; }
/*      */ 
/*      */   
/*      */   public void onEquipItem(EquipmentSlot slot, ItemStack oldStack, ItemStack stack) {
/*  706 */     if (level().isClientSide() || isSpectator()) {
/*      */       return;
/*      */     }
/*      */     
/*  710 */     if (ItemStack.isSameItemSameComponents(oldStack, stack) || this.firstTick) {
/*      */       return;
/*      */     }
/*      */     
/*  714 */     Equippable equippable = (Equippable)stack.get(DataComponents.EQUIPPABLE);
/*  715 */     if (!isSilent() && equippable != null && slot == equippable.slot()) {
/*  716 */       level().playSeededSound(null, getX(), getY(), getZ(), getEquipSound(slot, stack, equippable), getSoundSource(), 1.0F, 1.0F, this.random.nextLong());
/*      */     }
/*  718 */     if (doesEmitEquipEvent(slot)) {
/*  719 */       gameEvent((equippable != null) ? GameEvent.EQUIP : GameEvent.UNEQUIP);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  724 */   protected Holder<SoundEvent> getEquipSound(EquipmentSlot slot, ItemStack stack, Equippable equippable) { return equippable.equipSound(); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void remove(Entity.RemovalReason reason) {
/*  729 */     if (reason == Entity.RemovalReason.KILLED || reason == Entity.RemovalReason.DISCARDED) { Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/*      */         
/*  731 */         triggerOnDeathMobEffects(level, reason); }
/*      */        }
/*  733 */      super.remove(reason);
/*  734 */     this.brain.clearMemories();
/*      */   }
/*      */ 
/*      */   
/*      */   public void onRemoval(Entity.RemovalReason reason) {
/*  739 */     super.onRemoval(reason);
/*  740 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  741 */       serverLevel.getWaypointManager().untrackWaypoint(this); }
/*      */   
/*      */   }
/*      */   
/*      */   protected void triggerOnDeathMobEffects(ServerLevel level, Entity.RemovalReason reason) {
/*  746 */     for (MobEffectInstance effect : getActiveEffects()) {
/*  747 */       effect.onMobRemoved(level, this, reason);
/*      */     }
/*  749 */     this.activeEffects.clear();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  754 */     output.putFloat("Health", getHealth());
/*  755 */     output.putShort("HurtTime", (short)this.hurtTime);
/*  756 */     output.putInt("HurtByTimestamp", this.lastHurtByMobTimestamp);
/*  757 */     output.putShort("DeathTime", (short)this.deathTime);
/*  758 */     output.putFloat("AbsorptionAmount", getAbsorptionAmount());
/*      */     
/*  760 */     output.store("attributes", AttributeInstance.Packed.LIST_CODEC, getAttributes().pack());
/*      */     
/*  762 */     if (!this.activeEffects.isEmpty()) {
/*  763 */       output.store("active_effects", MobEffectInstance.CODEC.listOf(), List.copyOf(this.activeEffects.values()));
/*      */     }
/*      */     
/*  766 */     output.putBoolean("FallFlying", isFallFlying());
/*      */     
/*  768 */     getSleepingPos().ifPresent(sleepingPos -> 
/*  769 */         output.store("sleeping_pos", BlockPos.CODEC, sleepingPos));
/*      */ 
/*      */     
/*  772 */     DataResult<Dynamic<?>> writtenBrain = this.brain.serializeStart(NbtOps.INSTANCE).map(t -> new Dynamic(NbtOps.INSTANCE, t));
/*  773 */     Objects.requireNonNull(LOGGER); writtenBrain.resultOrPartial(LOGGER::error).ifPresent(b -> output.store("Brain", Codec.PASSTHROUGH, b));
/*      */     
/*  775 */     if (this.lastHurtByPlayer != null) {
/*  776 */       this.lastHurtByPlayer.store(output, "last_hurt_by_player");
/*  777 */       output.putInt("last_hurt_by_player_memory_time", this.lastHurtByPlayerMemoryTime);
/*      */     } 
/*  779 */     if (this.lastHurtByMob != null) {
/*  780 */       this.lastHurtByMob.store(output, "last_hurt_by_mob");
/*  781 */       output.putInt("ticks_since_last_hurt_by_mob", this.tickCount - this.lastHurtByMobTimestamp);
/*      */     } 
/*      */     
/*  784 */     if (!this.equipment.isEmpty()) {
/*  785 */       output.store("equipment", EntityEquipment.CODEC, this.equipment);
/*      */     }
/*      */     
/*  788 */     if (this.locatorBarIcon.hasData()) {
/*  789 */       output.store("locator_bar_icon", Waypoint.Icon.CODEC, this.locatorBarIcon);
/*      */     }
/*      */   }
/*      */   
/*      */   public ItemEntity drop(ItemStack itemStack, boolean randomly, boolean thrownFromHand) {
/*  794 */     if (itemStack.isEmpty()) {
/*  795 */       return null;
/*      */     }
/*      */     
/*  798 */     if (level().isClientSide()) {
/*  799 */       swing(InteractionHand.MAIN_HAND);
/*  800 */       return null;
/*      */     } 
/*      */     
/*  803 */     ItemEntity entity = createItemStackToDrop(itemStack, randomly, thrownFromHand);
/*  804 */     if (entity != null) {
/*  805 */       level().addFreshEntity(entity);
/*      */     }
/*  807 */     return entity;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  812 */     internalSetAbsorptionAmount(input.getFloatOr("AbsorptionAmount", 0.0F));
/*      */     
/*  814 */     if (level() != null && !level().isClientSide()) {
/*  815 */       Objects.requireNonNull(getAttributes()); input.read("attributes", AttributeInstance.Packed.LIST_CODEC).ifPresent(getAttributes()::apply);
/*      */     } 
/*      */     
/*  818 */     List<MobEffectInstance> effects = (List)input.read("active_effects", MobEffectInstance.CODEC.listOf()).orElse(List.of());
/*  819 */     this.activeEffects.clear();
/*  820 */     for (MobEffectInstance effect : effects) {
/*  821 */       this.activeEffects.put(effect.getEffect(), effect);
/*  822 */       this.effectsDirty = true;
/*      */     } 
/*      */     
/*  825 */     setHealth(input.getFloatOr("Health", getMaxHealth()));
/*      */     
/*  827 */     this.hurtTime = input.getShortOr("HurtTime", (short)0);
/*  828 */     this.deathTime = input.getShortOr("DeathTime", (short)0);
/*  829 */     this.lastHurtByMobTimestamp = input.getIntOr("HurtByTimestamp", 0);
/*      */ 
/*      */     
/*  832 */     input.getString("Team").ifPresent(teamName -> {
/*  833 */           Scoreboard scoreboard = level().getScoreboard();
/*  834 */           PlayerTeam team = scoreboard.getPlayerTeam(teamName);
/*  835 */           boolean success = (team != null && scoreboard.addPlayerToTeam(getStringUUID(), team));
/*  836 */           if (!success) {
/*  837 */             LOGGER.warn("Unable to add mob to team \"{}\" (that team probably doesn't exist)", teamName);
/*      */           }
/*      */         });
/*      */     
/*  841 */     setSharedFlag(7, input.getBooleanOr("FallFlying", false));
/*      */     
/*  843 */     input.read("sleeping_pos", BlockPos.CODEC).ifPresentOrElse(sleepingPos -> {
/*      */           
/*  845 */           setSleepingPos(sleepingPos);
/*  846 */           this.entityData.set(DATA_POSE, Pose.SLEEPING);
/*      */           
/*  848 */           if (!this.firstTick)
/*      */           {
/*  850 */             setPosToBed(sleepingPos);
/*      */           }
/*      */         }this::clearSleepingPos);
/*      */ 
/*      */ 
/*      */     
/*  856 */     input.read("Brain", Codec.PASSTHROUGH).ifPresent(brainTag -> this.brain = makeBrain(brainTag));
/*      */     
/*  858 */     this.lastHurtByPlayer = EntityReference.read(input, "last_hurt_by_player");
/*  859 */     this.lastHurtByPlayerMemoryTime = input.getIntOr("last_hurt_by_player_memory_time", 0);
/*      */     
/*  861 */     this.lastHurtByMob = EntityReference.read(input, "last_hurt_by_mob");
/*  862 */     this.lastHurtByMobTimestamp = input.getIntOr("ticks_since_last_hurt_by_mob", 0) + this.tickCount;
/*      */     
/*  864 */     this.equipment.setAll((EntityEquipment)input.read("equipment", EntityEquipment.CODEC).orElseGet(EntityEquipment::new));
/*      */     
/*  866 */     this.locatorBarIcon = (Waypoint.Icon)input.read("locator_bar_icon", Waypoint.Icon.CODEC).orElseGet(net.minecraft.world.waypoints.Waypoint.Icon::new);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateDataBeforeSync() {
/*  871 */     super.updateDataBeforeSync();
/*  872 */     updateDirtyEffects();
/*      */   }
/*      */   
/*      */   protected void tickEffects() {
/*  876 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  877 */       Iterator<Holder<MobEffect>> iterator = this.activeEffects.keySet().iterator();
/*      */       try {
/*  879 */         while (iterator.hasNext()) {
/*  880 */           Holder<MobEffect> mobEffect = (Holder)iterator.next();
/*  881 */           MobEffectInstance effect = (MobEffectInstance)this.activeEffects.get(mobEffect);
/*      */           
/*  883 */           if (!effect.tickServer(serverLevel, this, () -> onEffectUpdated(effect, true, null))) {
/*  884 */             iterator.remove();
/*  885 */             onEffectsRemoved(List.of(effect)); continue;
/*  886 */           }  if (effect.getDuration() % 600 == 0)
/*      */           {
/*  888 */             onEffectUpdated(effect, false, null);
/*      */           }
/*      */         } 
/*  891 */       } catch (ConcurrentModificationException concurrentModificationException) {}
/*      */        }
/*      */     
/*      */     else
/*      */     
/*      */     { 
/*  897 */       for (MobEffectInstance effect : this.activeEffects.values()) {
/*  898 */         effect.tickClient();
/*      */       }
/*      */       
/*  901 */       List<ParticleOptions> particles = (List)this.entityData.get(DATA_EFFECT_PARTICLES);
/*  902 */       if (!particles.isEmpty()) {
/*  903 */         boolean isAmbient = ((Boolean)this.entityData.get(DATA_EFFECT_AMBIENCE_ID)).booleanValue();
/*  904 */         int bound = isInvisible() ? 15 : 4;
/*  905 */         int ambientFactor = isAmbient ? 5 : 1;
/*      */         
/*  907 */         if (this.random.nextInt(bound * ambientFactor) == 0) {
/*  908 */           level().addParticle((ParticleOptions)Util.getRandom(particles, this.random), getRandomX(0.5D), getRandomY(), getRandomZ(0.5D), 1.0D, 1.0D, 1.0D);
/*      */         }
/*      */       }  }
/*      */   
/*      */   }
/*      */   
/*      */   private void updateDirtyEffects() {
/*  915 */     if (this.effectsDirty) {
/*  916 */       updateInvisibilityStatus();
/*  917 */       updateGlowingStatus();
/*  918 */       this.effectsDirty = false;
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void updateInvisibilityStatus() {
/*  923 */     if (this.activeEffects.isEmpty()) {
/*  924 */       removeEffectParticles();
/*  925 */       setInvisible(false);
/*      */       
/*      */       return;
/*      */     } 
/*  929 */     setInvisible(hasEffect(MobEffects.INVISIBILITY));
/*      */     
/*  931 */     updateSynchronizedMobEffectParticles();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateSynchronizedMobEffectParticles() {
/*  938 */     List<ParticleOptions> visibleEffectParticles = this.activeEffects.values().stream().filter(MobEffectInstance::isVisible).map(MobEffectInstance::getParticleOptions).toList();
/*      */     
/*  940 */     this.entityData.set(DATA_EFFECT_PARTICLES, visibleEffectParticles);
/*  941 */     this.entityData.set(DATA_EFFECT_AMBIENCE_ID, Boolean.valueOf(areAllEffectsAmbient(this.activeEffects.values())));
/*      */   }
/*      */   
/*      */   private void updateGlowingStatus() {
/*  945 */     boolean glowingState = isCurrentlyGlowing();
/*  946 */     if (getSharedFlag(6) != glowingState) {
/*  947 */       setSharedFlag(6, glowingState);
/*      */     }
/*      */   }
/*      */   
/*      */   public double getVisibilityPercent(Entity targetingEntity) {
/*  952 */     double visibilityPercent = 1.0D;
/*      */     
/*  954 */     if (isDiscrete()) {
/*  955 */       visibilityPercent *= 0.8D;
/*      */     }
/*  957 */     if (isInvisible()) {
/*  958 */       float coverPercentage = getArmorCoverPercentage();
/*  959 */       if (coverPercentage < 0.1F) {
/*  960 */         coverPercentage = 0.1F;
/*      */       }
/*  962 */       visibilityPercent *= 0.7D * coverPercentage;
/*      */     } 
/*  964 */     if (targetingEntity != null) {
/*  965 */       ItemStack itemStack = getItemBySlot(EquipmentSlot.HEAD);
/*  966 */       EntityType<?> type = targetingEntity.getType();
/*      */       
/*  968 */       if ((type == EntityType.SKELETON && itemStack.is(Items.SKELETON_SKULL)) || (type == EntityType.ZOMBIE && itemStack
/*  969 */         .is(Items.ZOMBIE_HEAD)) || (type == EntityType.PIGLIN && itemStack
/*  970 */         .is(Items.PIGLIN_HEAD)) || (type == EntityType.PIGLIN_BRUTE && itemStack
/*  971 */         .is(Items.PIGLIN_HEAD)) || (type == EntityType.CREEPER && itemStack
/*  972 */         .is(Items.CREEPER_HEAD)))
/*      */       {
/*  974 */         visibilityPercent *= 0.5D;
/*      */       }
/*      */     } 
/*      */     
/*  978 */     return visibilityPercent;
/*      */   }
/*      */   
/*      */   public boolean canAttack(LivingEntity target) {
/*  982 */     if (target instanceof Player && level().getDifficulty() == Difficulty.PEACEFUL) {
/*  983 */       return false;
/*      */     }
/*  985 */     return target.canBeSeenAsEnemy();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  990 */   public boolean canBeSeenAsEnemy() { return (!isInvulnerable() && canBeSeenByAnyone()); }
/*      */ 
/*      */ 
/*      */   
/*  994 */   public boolean canBeSeenByAnyone() { return (!isSpectator() && isAlive()); }
/*      */ 
/*      */   
/*      */   public static boolean areAllEffectsAmbient(Collection<MobEffectInstance> effects) {
/*  998 */     for (MobEffectInstance effect : effects) {
/*  999 */       if (effect.isVisible() && !effect.isAmbient()) {
/* 1000 */         return false;
/*      */       }
/*      */     } 
/*      */     
/* 1004 */     return true;
/*      */   }
/*      */ 
/*      */   
/* 1008 */   protected void removeEffectParticles() { this.entityData.set(DATA_EFFECT_PARTICLES, List.of()); }
/*      */ 
/*      */   
/*      */   public boolean removeAllEffects() {
/* 1012 */     if (level().isClientSide()) {
/* 1013 */       return false;
/*      */     }
/*      */     
/* 1016 */     if (this.activeEffects.isEmpty()) {
/* 1017 */       return false;
/*      */     }
/*      */     
/* 1020 */     Map<Holder<MobEffect>, MobEffectInstance> copy = Maps.newHashMap(this.activeEffects);
/* 1021 */     this.activeEffects.clear();
/* 1022 */     onEffectsRemoved(copy.values());
/*      */     
/* 1024 */     return true;
/*      */   }
/*      */ 
/*      */   
/* 1028 */   public Collection<MobEffectInstance> getActiveEffects() { return this.activeEffects.values(); }
/*      */ 
/*      */ 
/*      */   
/* 1032 */   public Map<Holder<MobEffect>, MobEffectInstance> getActiveEffectsMap() { return this.activeEffects; }
/*      */ 
/*      */ 
/*      */   
/* 1036 */   public boolean hasEffect(Holder<MobEffect> effect) { return this.activeEffects.containsKey(effect); }
/*      */ 
/*      */ 
/*      */   
/* 1040 */   public MobEffectInstance getEffect(Holder<MobEffect> effect) { return (MobEffectInstance)this.activeEffects.get(effect); }
/*      */ 
/*      */   
/*      */   public float getEffectBlendFactor(Holder<MobEffect> effect, float partialTicks) {
/* 1044 */     MobEffectInstance instance = getEffect(effect);
/* 1045 */     if (instance != null) {
/* 1046 */       return instance.getBlendFactor(this, partialTicks);
/*      */     }
/* 1048 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   
/* 1052 */   public final boolean addEffect(MobEffectInstance newEffect) { return addEffect(newEffect, null); }
/*      */ 
/*      */   
/*      */   public boolean addEffect(MobEffectInstance newEffect, Entity source) {
/* 1056 */     if (!canBeAffected(newEffect)) {
/* 1057 */       return false;
/*      */     }
/*      */     
/* 1060 */     MobEffectInstance effect = (MobEffectInstance)this.activeEffects.get(newEffect.getEffect());
/* 1061 */     boolean changed = false;
/* 1062 */     if (effect == null) {
/* 1063 */       this.activeEffects.put(newEffect.getEffect(), newEffect);
/* 1064 */       onEffectAdded(newEffect, source);
/* 1065 */       changed = true;
/* 1066 */       newEffect.onEffectAdded(this);
/*      */     
/*      */     }
/* 1069 */     else if (effect.update(newEffect)) {
/* 1070 */       onEffectUpdated(effect, true, source);
/* 1071 */       changed = true;
/*      */     } 
/*      */     
/* 1074 */     newEffect.onEffectStarted(this);
/*      */     
/* 1076 */     return changed;
/*      */   }
/*      */   
/*      */   public boolean canBeAffected(MobEffectInstance newEffect) {
/* 1080 */     if (getType().is(EntityTypeTags.IMMUNE_TO_INFESTED)) {
/* 1081 */       return !newEffect.is(MobEffects.INFESTED);
/*      */     }
/* 1083 */     if (getType().is(EntityTypeTags.IMMUNE_TO_OOZING)) {
/* 1084 */       return !newEffect.is(MobEffects.OOZING);
/*      */     }
/* 1086 */     if (getType().is(EntityTypeTags.IGNORES_POISON_AND_REGEN)) {
/* 1087 */       return (!newEffect.is(MobEffects.REGENERATION) && !newEffect.is(MobEffects.POISON));
/*      */     }
/* 1089 */     return true;
/*      */   }
/*      */   
/*      */   public void forceAddEffect(MobEffectInstance newEffect, Entity source) {
/* 1093 */     if (!canBeAffected(newEffect)) {
/*      */       return;
/*      */     }
/*      */     
/* 1097 */     MobEffectInstance previousEffect = (MobEffectInstance)this.activeEffects.put(newEffect.getEffect(), newEffect);
/* 1098 */     if (previousEffect == null) {
/* 1099 */       onEffectAdded(newEffect, source);
/*      */     } else {
/* 1101 */       newEffect.copyBlendState(previousEffect);
/* 1102 */       onEffectUpdated(newEffect, true, source);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 1107 */   public boolean isInvertedHealAndHarm() { return getType().is(EntityTypeTags.INVERTED_HEALING_AND_HARM); }
/*      */ 
/*      */ 
/*      */   
/* 1111 */   public final MobEffectInstance removeEffectNoUpdate(Holder<MobEffect> effect) { return (MobEffectInstance)this.activeEffects.remove(effect); }
/*      */ 
/*      */   
/*      */   public boolean removeEffect(Holder<MobEffect> effect) {
/* 1115 */     MobEffectInstance effectInstance = removeEffectNoUpdate(effect);
/* 1116 */     if (effectInstance != null) {
/* 1117 */       onEffectsRemoved(List.of(effectInstance));
/* 1118 */       return true;
/*      */     } 
/* 1120 */     return false;
/*      */   }
/*      */   
/*      */   protected void onEffectAdded(MobEffectInstance effect, Entity source) {
/* 1124 */     if (!level().isClientSide()) {
/* 1125 */       this.effectsDirty = true;
/* 1126 */       ((MobEffect)effect.getEffect().value()).addAttributeModifiers(getAttributes(), effect.getAmplifier());
/* 1127 */       sendEffectToPassengers(effect);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void sendEffectToPassengers(MobEffectInstance effect) {
/* 1132 */     for (Entity passenger : getPassengers()) {
/* 1133 */       if (passenger instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)passenger;
/* 1134 */         serverPlayer.connection.send(new ClientboundUpdateMobEffectPacket(getId(), effect, false)); }
/*      */     
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void onEffectUpdated(MobEffectInstance effect, boolean doRefreshAttributes, Entity source) {
/* 1140 */     if (level().isClientSide()) {
/*      */       return;
/*      */     }
/* 1143 */     this.effectsDirty = true;
/* 1144 */     if (doRefreshAttributes) {
/* 1145 */       MobEffect mobEffect = (MobEffect)effect.getEffect().value();
/* 1146 */       mobEffect.removeAttributeModifiers(getAttributes());
/* 1147 */       mobEffect.addAttributeModifiers(getAttributes(), effect.getAmplifier());
/* 1148 */       refreshDirtyAttributes();
/*      */     } 
/* 1150 */     sendEffectToPassengers(effect);
/*      */   }
/*      */   
/*      */   protected void onEffectsRemoved(Collection<MobEffectInstance> effects) {
/* 1154 */     if (level().isClientSide()) {
/*      */       return;
/*      */     }
/* 1157 */     this.effectsDirty = true;
/* 1158 */     for (MobEffectInstance effect : effects) {
/* 1159 */       ((MobEffect)effect.getEffect().value()).removeAttributeModifiers(getAttributes());
/* 1160 */       for (Entity passenger : getPassengers()) {
/* 1161 */         if (passenger instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)passenger;
/* 1162 */           serverPlayer.connection.send(new ClientboundRemoveMobEffectPacket(getId(), effect.getEffect())); }
/*      */       
/*      */       } 
/*      */     } 
/* 1166 */     refreshDirtyAttributes();
/*      */   }
/*      */   
/*      */   private void refreshDirtyAttributes() {
/* 1170 */     Set<AttributeInstance> attributesToUpdate = getAttributes().getAttributesToUpdate();
/* 1171 */     for (AttributeInstance changedAttributeInstance : attributesToUpdate) {
/* 1172 */       onAttributeUpdated(changedAttributeInstance.getAttribute());
/*      */     }
/* 1174 */     attributesToUpdate.clear();
/*      */   }
/*      */   
/*      */   protected void onAttributeUpdated(Holder<Attribute> attribute) {
/* 1178 */     if (attribute.is(Attributes.MAX_HEALTH)) {
/* 1179 */       float currentMaxHealth = getMaxHealth();
/* 1180 */       if (getHealth() > currentMaxHealth) {
/* 1181 */         setHealth(currentMaxHealth);
/*      */       }
/* 1183 */     } else if (attribute.is(Attributes.MAX_ABSORPTION)) {
/* 1184 */       float currentMaxAbsorption = getMaxAbsorption();
/* 1185 */       if (getAbsorptionAmount() > currentMaxAbsorption) {
/* 1186 */         setAbsorptionAmount(currentMaxAbsorption);
/*      */       }
/* 1188 */     } else if (attribute.is(Attributes.SCALE)) {
/* 1189 */       refreshDimensions();
/* 1190 */     } else if (attribute.is(Attributes.WAYPOINT_TRANSMIT_RANGE)) {
/* 1191 */       Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 1192 */         ServerWaypointManager waypointManager = serverLevel.getWaypointManager();
/* 1193 */         if (this.attributes.getValue(attribute) > 0.0D) {
/* 1194 */           waypointManager.trackWaypoint(this);
/*      */         } else {
/* 1196 */           waypointManager.untrackWaypoint(this);
/*      */         }  }
/*      */     
/*      */     } 
/*      */   }
/*      */   
/*      */   public void heal(float heal) {
/* 1203 */     float health = getHealth();
/* 1204 */     if (health > 0.0F) {
/* 1205 */       setHealth(health + heal);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 1210 */   public float getHealth() { return ((Float)this.entityData.get(DATA_HEALTH_ID)).floatValue(); }
/*      */ 
/*      */ 
/*      */   
/* 1214 */   public void setHealth(float health) { this.entityData.set(DATA_HEALTH_ID, Float.valueOf(Mth.clamp(health, 0.0F, getMaxHealth()))); }
/*      */ 
/*      */ 
/*      */   
/* 1218 */   public boolean isDeadOrDying() { return (getHealth() <= 0.0F); }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 1223 */     if (isInvulnerableTo(level, source)) {
/* 1224 */       return false;
/*      */     }
/*      */     
/* 1227 */     if (isDeadOrDying()) {
/* 1228 */       return false;
/*      */     }
/*      */     
/* 1231 */     if (source.is(DamageTypeTags.IS_FIRE) && hasEffect(MobEffects.FIRE_RESISTANCE)) {
/* 1232 */       return false;
/*      */     }
/*      */     
/* 1235 */     if (isSleeping()) {
/* 1236 */       stopSleeping();
/*      */     }
/*      */     
/* 1239 */     this.noActionTime = 0;
/* 1240 */     if (damage < 0.0F) {
/* 1241 */       damage = 0.0F;
/*      */     }
/* 1243 */     float originalDamage = damage;
/*      */ 
/*      */     
/* 1246 */     ItemStack itemInUse = getUseItem();
/* 1247 */     float damageBlocked = applyItemBlocking(level, source, damage);
/* 1248 */     damage -= damageBlocked;
/* 1249 */     boolean blocked = (damageBlocked > 0.0F);
/*      */     
/* 1251 */     if (source.is(DamageTypeTags.IS_FREEZING) && getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
/* 1252 */       damage *= 5.0F;
/*      */     }
/*      */     
/* 1255 */     if (source.is(DamageTypeTags.DAMAGES_HELMET) && !getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
/* 1256 */       hurtHelmet(source, damage);
/* 1257 */       damage *= 0.75F;
/*      */     } 
/*      */ 
/*      */     
/* 1261 */     if (Float.isNaN(damage) || Float.isInfinite(damage)) {
/* 1262 */       damage = Float.MAX_VALUE;
/*      */     }
/*      */     
/* 1265 */     boolean tookFullDamage = true;
/* 1266 */     if (this.invulnerableTime > 10.0F && !source.is(DamageTypeTags.BYPASSES_COOLDOWN)) {
/* 1267 */       if (damage <= this.lastHurt) {
/* 1268 */         return false;
/*      */       }
/* 1270 */       actuallyHurt(level, source, damage - this.lastHurt);
/* 1271 */       this.lastHurt = damage;
/* 1272 */       tookFullDamage = false;
/*      */     } else {
/* 1274 */       this.lastHurt = damage;
/* 1275 */       this.invulnerableTime = 20;
/* 1276 */       actuallyHurt(level, source, damage);
/* 1277 */       this.hurtDuration = 10;
/* 1278 */       this.hurtTime = this.hurtDuration;
/*      */     } 
/*      */     
/* 1281 */     resolveMobResponsibleForDamage(source);
/* 1282 */     resolvePlayerResponsibleForDamage(source);
/*      */     
/* 1284 */     if (tookFullDamage) {
/* 1285 */       BlocksAttacks blocksAttacks = (BlocksAttacks)itemInUse.get(DataComponents.BLOCKS_ATTACKS);
/* 1286 */       if (blocked && blocksAttacks != null) {
/* 1287 */         blocksAttacks.onBlocked(level, this);
/*      */       } else {
/* 1289 */         level.broadcastDamageEvent(this, source);
/*      */       } 
/* 1291 */       if (!source.is(DamageTypeTags.NO_IMPACT) && (!blocked || damage > 0.0F)) {
/* 1292 */         markHurt();
/*      */       }
/* 1294 */       if (!source.is(DamageTypeTags.NO_KNOCKBACK)) {
/* 1295 */         double xd = 0.0D;
/* 1296 */         double zd = 0.0D;
/* 1297 */         Entity entity1 = source.getDirectEntity(); if (entity1 instanceof Projectile) { Projectile projectile = (Projectile)entity1;
/* 1298 */           DoubleDoubleImmutablePair knockbackDirection = projectile.calculateHorizontalHurtKnockbackDirection(this, source);
/* 1299 */           xd = -knockbackDirection.leftDouble();
/* 1300 */           zd = -knockbackDirection.rightDouble(); }
/* 1301 */         else if (source.getSourcePosition() != null)
/* 1302 */         { xd = source.getSourcePosition().x() - getX();
/* 1303 */           zd = source.getSourcePosition().z() - getZ(); }
/*      */         
/* 1305 */         knockback(0.4000000059604645D, xd, zd);
/* 1306 */         if (!blocked) {
/* 1307 */           indicateDamage(xd, zd);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1312 */     if (isDeadOrDying()) {
/* 1313 */       if (!checkTotemDeathProtection(source)) {
/* 1314 */         if (tookFullDamage) {
/* 1315 */           makeSound(getDeathSound());
/* 1316 */           playSecondaryHurtSound(source);
/*      */         } 
/* 1318 */         die(source);
/*      */       } 
/* 1320 */     } else if (tookFullDamage) {
/* 1321 */       playHurtSound(source);
/* 1322 */       playSecondaryHurtSound(source);
/*      */     } 
/*      */     
/* 1325 */     boolean success = (!blocked || damage > 0.0F);
/* 1326 */     if (success) {
/* 1327 */       this.lastDamageSource = source;
/* 1328 */       this.lastDamageStamp = level().getGameTime();
/*      */       
/* 1330 */       for (MobEffectInstance effect : getActiveEffects()) {
/* 1331 */         effect.onMobHurt(level, this, source, damage);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1336 */     LivingEntity livingEntity = this; if (livingEntity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
/* 1337 */       CriteriaTriggers.ENTITY_HURT_PLAYER.trigger(serverPlayer, source, originalDamage, damage, blocked);
/*      */       
/* 1339 */       if (damageBlocked > 0.0F && damageBlocked < 3.4028235E37F) {
/* 1340 */         serverPlayer.awardStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(damageBlocked * 10.0F));
/*      */       } }
/*      */     
/* 1343 */     Entity entity = source.getEntity(); if (entity instanceof ServerPlayer) { ServerPlayer sourcePlayer = (ServerPlayer)entity;
/* 1344 */       CriteriaTriggers.PLAYER_HURT_ENTITY.trigger(sourcePlayer, this, source, originalDamage, damage, blocked); }
/*      */ 
/*      */     
/* 1347 */     return success;
/*      */   }
/*      */   public float applyItemBlocking(ServerLevel level, DamageSource source, float damage) {
/*      */     double angle;
/* 1351 */     if (damage <= 0.0F) {
/* 1352 */       return 0.0F;
/*      */     }
/* 1354 */     ItemStack blockingWith = getItemBlockingWith();
/* 1355 */     if (blockingWith == null) {
/* 1356 */       return 0.0F;
/*      */     }
/* 1358 */     BlocksAttacks blocksAttacks = (BlocksAttacks)blockingWith.get(DataComponents.BLOCKS_ATTACKS);
/* 1359 */     Objects.requireNonNull(source); if (blocksAttacks == null || ((Boolean)blocksAttacks.bypassedBy().map(source::is).orElse(Boolean.valueOf(false))).booleanValue()) {
/* 1360 */       return 0.0F;
/*      */     }
/* 1362 */     Entity entity = source.getDirectEntity(); if (entity instanceof AbstractArrow) { AbstractArrow abstractArrow = (AbstractArrow)entity; if (abstractArrow.getPierceLevel() > 0) {
/* 1363 */         return 0.0F;
/*      */       } }
/*      */ 
/*      */     
/* 1367 */     Vec3 sourcePosition = source.getSourcePosition();
/* 1368 */     if (sourcePosition != null) {
/* 1369 */       Vec3 viewVector = calculateViewVector(0.0F, getYHeadRot());
/* 1370 */       Vec3 vectorTo = sourcePosition.subtract(position());
/* 1371 */       vectorTo = (new Vec3(vectorTo.x, 0.0D, vectorTo.z)).normalize();
/*      */ 
/*      */ 
/*      */       
/* 1375 */       angle = Math.acos(vectorTo.dot(viewVector));
/*      */     } else {
/*      */       
/* 1378 */       angle = 3.1415927410125732D;
/*      */     } 
/* 1380 */     float damageBlocked = blocksAttacks.resolveBlockedDamage(source, damage, angle);
/* 1381 */     blocksAttacks.hurtBlockingItem(level(), blockingWith, this, getUsedItemHand(), damageBlocked);
/*      */ 
/*      */     
/* 1384 */     if (damageBlocked > 0.0F && !source.is(DamageTypeTags.IS_PROJECTILE)) {
/* 1385 */       Entity directEntity = source.getDirectEntity();
/* 1386 */       if (directEntity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)directEntity;
/* 1387 */         blockUsingItem(level, livingEntity); }
/*      */     
/*      */     } 
/* 1390 */     return damageBlocked;
/*      */   }
/*      */   
/*      */   private void playSecondaryHurtSound(DamageSource source) {
/* 1394 */     if (source.is(DamageTypes.THORNS)) {
/* 1395 */       SoundSource soundSource = (this instanceof Player) ? SoundSource.PLAYERS : SoundSource.HOSTILE;
/* 1396 */       level().playSound(null, (position()).x, (position()).y, (position()).z, SoundEvents.THORNS_HIT, soundSource);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void resolveMobResponsibleForDamage(DamageSource source) {
/* 1401 */     Entity entity = source.getEntity(); if (entity instanceof LivingEntity) { LivingEntity livingSource = (LivingEntity)entity; if (!source.is(DamageTypeTags.NO_ANGER) && (
/* 1402 */         !source.is(DamageTypes.WIND_CHARGE) || !getType().is(EntityTypeTags.NO_ANGER_FROM_WIND_CHARGE)))
/* 1403 */         setLastHurtByMob(livingSource);  }
/*      */   
/*      */   }
/*      */   
/*      */   protected Player resolvePlayerResponsibleForDamage(DamageSource source) {
/* 1408 */     Entity sourceEntity = source.getEntity();
/* 1409 */     if (sourceEntity instanceof Player) { Player playerSource = (Player)sourceEntity;
/* 1410 */       setLastHurtByPlayer(playerSource, 100); }
/* 1411 */     else if (sourceEntity instanceof Wolf) { Wolf wolf = (Wolf)sourceEntity; if (wolf.isTame())
/* 1412 */         if (wolf.getOwnerReference() != null) {
/* 1413 */           setLastHurtByPlayer(wolf.getOwnerReference().getUUID(), 100);
/*      */         } else {
/* 1415 */           this.lastHurtByPlayer = null;
/* 1416 */           this.lastHurtByPlayerMemoryTime = 0;
/*      */         }   }
/*      */     
/* 1419 */     return EntityReference.getPlayer(this.lastHurtByPlayer, level());
/*      */   }
/*      */ 
/*      */   
/* 1423 */   protected void blockUsingItem(ServerLevel level, LivingEntity attacker) { attacker.blockedByItem(this); }
/*      */ 
/*      */ 
/*      */   
/* 1427 */   protected void blockedByItem(LivingEntity defender) { defender.knockback(0.5D, defender.getX() - getX(), defender.getZ() - getZ()); }
/*      */ 
/*      */   
/*      */   private boolean checkTotemDeathProtection(DamageSource killingDamage) {
/* 1431 */     if (killingDamage.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
/* 1432 */       return false;
/*      */     }
/*      */     
/* 1435 */     ItemStack protectionItem = null;
/* 1436 */     DeathProtection protection = null;
/*      */     
/* 1438 */     for (InteractionHand hand : InteractionHand.values()) {
/* 1439 */       ItemStack itemStack = getItemInHand(hand);
/* 1440 */       protection = (DeathProtection)itemStack.get(DataComponents.DEATH_PROTECTION);
/* 1441 */       if (protection != null) {
/* 1442 */         protectionItem = itemStack.copy();
/* 1443 */         itemStack.shrink(1);
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/* 1448 */     if (protectionItem != null) {
/*      */       
/* 1450 */       LivingEntity livingEntity = this; if (livingEntity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)livingEntity;
/* 1451 */         player.awardStat(Stats.ITEM_USED.get(protectionItem.getItem()));
/* 1452 */         CriteriaTriggers.USED_TOTEM.trigger(player, protectionItem);
/* 1453 */         protectionItem.causeUseVibration(this, GameEvent.ITEM_INTERACT_FINISH); }
/*      */       
/* 1455 */       setHealth(1.0F);
/* 1456 */       protection.applyEffects(protectionItem, this);
/* 1457 */       level().broadcastEntityEvent(this, (byte)35);
/*      */     } 
/* 1459 */     return (protection != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DamageSource getLastDamageSource() {
/* 1466 */     if (level().getGameTime() - this.lastDamageStamp > 40L) {
/* 1467 */       this.lastDamageSource = null;
/*      */     }
/* 1469 */     return this.lastDamageSource;
/*      */   }
/*      */ 
/*      */   
/* 1473 */   protected void playHurtSound(DamageSource source) { makeSound(getHurtSound(source)); }
/*      */ 
/*      */   
/*      */   public void makeSound(SoundEvent sound) {
/* 1477 */     if (sound != null) {
/* 1478 */       playSound(sound, getSoundVolume(), getVoicePitch());
/*      */     }
/*      */   }
/*      */   
/*      */   private void breakItem(ItemStack itemStack) {
/* 1483 */     if (!itemStack.isEmpty()) {
/* 1484 */       Holder<SoundEvent> breakSound = (Holder)itemStack.get(DataComponents.BREAK_SOUND);
/* 1485 */       if (breakSound != null && !isSilent()) {
/* 1486 */         level().playLocalSound(getX(), getY(), getZ(), (SoundEvent)breakSound.value(), getSoundSource(), 0.8F, 0.8F + (level()).random.nextFloat() * 0.4F, false);
/*      */       }
/* 1488 */       spawnItemParticles(itemStack, 5);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void die(DamageSource source) {
/* 1493 */     if (isRemoved() || this.dead) {
/*      */       return;
/*      */     }
/* 1496 */     Entity sourceEntity = source.getEntity();
/* 1497 */     LivingEntity killer = getKillCredit();
/* 1498 */     if (killer != null) {
/* 1499 */       killer.awardKillScore(this, source);
/*      */     }
/*      */     
/* 1502 */     if (isSleeping()) {
/* 1503 */       stopSleeping();
/*      */     }
/* 1505 */     stopUsingItem();
/* 1506 */     if (!level().isClientSide() && hasCustomName()) {
/* 1507 */       LOGGER.info("Named entity {} died: {}", this, getCombatTracker().getDeathMessage().getString());
/*      */     }
/*      */     
/* 1510 */     this.dead = true;
/* 1511 */     getCombatTracker().recheckStatus();
/*      */     
/* 1513 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 1514 */       if (sourceEntity == null || sourceEntity.killedEntity(serverLevel, this, source)) {
/* 1515 */         gameEvent(GameEvent.ENTITY_DIE);
/* 1516 */         dropAllDeathLoot(serverLevel, source);
/* 1517 */         createWitherRose(killer);
/*      */       } 
/* 1519 */       level().broadcastEntityEvent(this, (byte)3); }
/*      */     
/* 1521 */     setPose(Pose.DYING);
/*      */   }
/*      */   protected void createWitherRose(LivingEntity killer) {
/*      */     ServerLevel serverLevel;
/* 1525 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*      */     else
/*      */     { return; }
/*      */     
/* 1529 */     boolean plantedWitherRose = false;
/* 1530 */     if (killer instanceof net.minecraft.world.entity.boss.wither.WitherBoss) {
/* 1531 */       if (((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 1532 */         BlockPos pos = blockPosition();
/* 1533 */         BlockState state = Blocks.WITHER_ROSE.defaultBlockState();
/* 1534 */         if (level().getBlockState(pos).isAir() && state.canSurvive(level(), pos)) {
/* 1535 */           level().setBlock(pos, state, 3);
/* 1536 */           plantedWitherRose = true;
/*      */         } 
/*      */       } 
/*      */       
/* 1540 */       if (!plantedWitherRose) {
/* 1541 */         ItemEntity itemEntity = new ItemEntity(level(), getX(), getY(), getZ(), new ItemStack(Items.WITHER_ROSE));
/* 1542 */         level().addFreshEntity(itemEntity);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void dropAllDeathLoot(ServerLevel level, DamageSource source) {
/* 1548 */     boolean playerKilled = (this.lastHurtByPlayerMemoryTime > 0);
/*      */     
/* 1550 */     if (shouldDropLoot(level)) {
/* 1551 */       dropFromLootTable(level, source, playerKilled);
/* 1552 */       dropCustomDeathLoot(level, source, playerKilled);
/*      */     } 
/* 1554 */     dropEquipment(level);
/* 1555 */     dropExperience(level, source.getEntity());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void dropEquipment(ServerLevel level) {}
/*      */   
/*      */   protected void dropExperience(ServerLevel level, Entity killer) {
/* 1562 */     if (!wasExperienceConsumed() && (isAlwaysExperienceDropper() || (this.lastHurtByPlayerMemoryTime > 0 && shouldDropExperience() && ((Boolean)level.getGameRules().get(GameRules.MOB_DROPS)).booleanValue()))) {
/* 1563 */       ExperienceOrb.award(level, position(), getExperienceReward(level, killer));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {}
/*      */ 
/*      */   
/* 1571 */   public long getLootTableSeed() { return 0L; }
/*      */ 
/*      */   
/*      */   protected float getKnockback(Entity target, DamageSource damageSource) {
/* 1575 */     float knockback = (float)getAttributeValue(Attributes.ATTACK_KNOCKBACK);
/* 1576 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/* 1577 */       return EnchantmentHelper.modifyKnockback(level, getWeaponItem(), target, damageSource, knockback) / 2.0F; }
/*      */     
/* 1579 */     return knockback / 2.0F;
/*      */   }
/*      */   
/*      */   protected void dropFromLootTable(ServerLevel level, DamageSource source, boolean playerKilled) {
/* 1583 */     Optional<ResourceKey<LootTable>> lootTable = getLootTable();
/* 1584 */     if (lootTable.isEmpty()) {
/*      */       return;
/*      */     }
/* 1587 */     dropFromLootTable(level, source, playerKilled, (ResourceKey)lootTable.get());
/*      */   }
/*      */ 
/*      */   
/* 1591 */   public void dropFromLootTable(ServerLevel level, DamageSource source, boolean playerKilled, ResourceKey<LootTable> lootTable) { dropFromLootTable(level, source, playerKilled, lootTable, itemStack -> spawnAtLocation(level, itemStack)); }
/*      */ 
/*      */   
/*      */   public void dropFromLootTable(ServerLevel level, DamageSource source, boolean playerKilled, ResourceKey<LootTable> lootTable, Consumer<ItemStack> itemStackConsumer) {
/* 1595 */     LootTable table = level.getServer().reloadableRegistries().getLootTable(lootTable);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1602 */     LootParams.Builder builder = (new LootParams.Builder(level)).withParameter(LootContextParams.THIS_ENTITY, this).withParameter(LootContextParams.ORIGIN, position()).withParameter(LootContextParams.DAMAGE_SOURCE, source).withOptionalParameter(LootContextParams.ATTACKING_ENTITY, source.getEntity()).withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, source.getDirectEntity());
/*      */     
/* 1604 */     Player killerPlayer = getLastHurtByPlayer();
/* 1605 */     if (playerKilled && killerPlayer != null) {
/* 1606 */       builder = builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, killerPlayer).withLuck(killerPlayer.getLuck());
/*      */     }
/* 1608 */     LootParams params = builder.create(LootContextParamSets.ENTITY);
/* 1609 */     table.getRandomItems(params, getLootTableSeed(), itemStackConsumer);
/*      */   }
/*      */ 
/*      */   
/* 1613 */   public boolean dropFromEntityInteractLootTable(ServerLevel level, ResourceKey<LootTable> key, Entity interactingEntity, ItemStack tool, BiConsumer<ServerLevel, ItemStack> consumer) { return dropFromLootTable(level, key, params -> 
/*      */ 
/*      */         
/* 1616 */         params
/* 1617 */         .withParameter(LootContextParams.TARGET_ENTITY, this)
/* 1618 */         .withOptionalParameter(LootContextParams.INTERACTING_ENTITY, interactingEntity)
/* 1619 */         .withParameter(LootContextParams.TOOL, tool)
/* 1620 */         .create(LootContextParamSets.ENTITY_INTERACT), consumer); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1626 */   public boolean dropFromGiftLootTable(ServerLevel level, ResourceKey<LootTable> key, BiConsumer<ServerLevel, ItemStack> consumer) { return dropFromLootTable(level, key, params -> 
/*      */ 
/*      */         
/* 1629 */         params
/* 1630 */         .withParameter(LootContextParams.ORIGIN, position())
/* 1631 */         .withParameter(LootContextParams.THIS_ENTITY, this)
/* 1632 */         .create(LootContextParamSets.GIFT), consumer); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1638 */   protected void dropFromShearingLootTable(ServerLevel level, ResourceKey<LootTable> key, ItemStack tool, BiConsumer<ServerLevel, ItemStack> consumer) { dropFromLootTable(level, key, params -> 
/*      */ 
/*      */         
/* 1641 */         params
/* 1642 */         .withParameter(LootContextParams.ORIGIN, position())
/* 1643 */         .withParameter(LootContextParams.THIS_ENTITY, this)
/* 1644 */         .withParameter(LootContextParams.TOOL, tool)
/* 1645 */         .create(LootContextParamSets.SHEARING), consumer); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean dropFromLootTable(ServerLevel level, ResourceKey<LootTable> key, Function<LootParams.Builder, LootParams> paramsBuilder, BiConsumer<ServerLevel, ItemStack> consumer) {
/* 1651 */     LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(key);
/* 1652 */     LootParams params = (LootParams)paramsBuilder.apply(new LootParams.Builder(level));
/* 1653 */     ObjectArrayList objectArrayList = lootTable.getRandomItems(params);
/* 1654 */     if (!objectArrayList.isEmpty()) {
/* 1655 */       objectArrayList.forEach(stack -> consumer.accept(level, stack));
/* 1656 */       return true;
/*      */     } 
/* 1658 */     return false;
/*      */   }
/*      */   
/*      */   public void knockback(double power, double xd, double zd) {
/* 1662 */     power *= (1.0D - getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
/* 1663 */     if (power <= 0.0D) {
/*      */       return;
/*      */     }
/*      */     
/* 1667 */     this.needsSync = true;
/*      */     
/* 1669 */     Vec3 deltaMovement = getDeltaMovement();
/* 1670 */     while (xd * xd + zd * zd < 9.999999747378752E-6D) {
/* 1671 */       xd = (this.random.nextDouble() - this.random.nextDouble()) * 0.01D;
/* 1672 */       zd = (this.random.nextDouble() - this.random.nextDouble()) * 0.01D;
/*      */     } 
/*      */     
/* 1675 */     Vec3 deltaVector = (new Vec3(xd, 0.0D, zd)).normalize().scale(power);
/*      */     
/* 1677 */     setDeltaMovement(deltaMovement.x / 2.0D - deltaVector.x, 
/*      */         
/* 1679 */         onGround() ? Math.min(0.4D, deltaMovement.y / 2.0D + power) : deltaMovement.y, deltaMovement.z / 2.0D - deltaVector.z);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void indicateDamage(double xd, double zd) {}
/*      */ 
/*      */ 
/*      */   
/* 1688 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.GENERIC_HURT; }
/*      */ 
/*      */ 
/*      */   
/* 1692 */   protected SoundEvent getDeathSound() { return SoundEvents.GENERIC_DEATH; }
/*      */ 
/*      */ 
/*      */   
/* 1696 */   private SoundEvent getFallDamageSound(int dmg) { return (dmg > 4) ? getFallSounds().big() : getFallSounds().small(); }
/*      */ 
/*      */ 
/*      */   
/* 1700 */   public void skipDropExperience() { this.skipDropExperience = true; }
/*      */ 
/*      */ 
/*      */   
/* 1704 */   public boolean wasExperienceConsumed() { return this.skipDropExperience; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1711 */   public float getHurtDir() { return 0.0F; }
/*      */ 
/*      */   
/*      */   protected AABB getHitbox() {
/* 1715 */     AABB aabb = getBoundingBox();
/* 1716 */     Entity vehicle = getVehicle();
/* 1717 */     if (vehicle != null) {
/* 1718 */       Vec3 pos = vehicle.getPassengerRidingPosition(this);
/* 1719 */       return aabb.setMinY(Math.max(pos.y, aabb.minY));
/*      */     } 
/* 1721 */     return aabb;
/*      */   }
/*      */ 
/*      */   
/* 1725 */   public Map<Enchantment, Set<EnchantmentLocationBasedEffect>> activeLocationDependentEnchantments(EquipmentSlot slot) { return (Map)this.activeLocationDependentEnchantments.computeIfAbsent(slot, s -> new Reference2ObjectArrayMap()); }
/*      */ 
/*      */   
/*      */   public void lungeForwardMaybe() {
/* 1729 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 1730 */       EnchantmentHelper.doLungeEffects(serverLevel, this); }
/*      */   
/*      */   }
/*      */   public static final class Fallsounds extends Record { private final SoundEvent small; private final SoundEvent big;
/* 1734 */     public Fallsounds(SoundEvent small, SoundEvent big) { this.small = small; this.big = big; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/LivingEntity$Fallsounds;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1734	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/entity/LivingEntity$Fallsounds; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/LivingEntity$Fallsounds;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1734	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/entity/LivingEntity$Fallsounds; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/LivingEntity$Fallsounds;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1734	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/entity/LivingEntity$Fallsounds;
/* 1734 */       //   0	8	1	o	Ljava/lang/Object; } public SoundEvent small() { return this.small; } public SoundEvent big() { return this.big; } }
/*      */ 
/*      */ 
/*      */   
/* 1738 */   public Fallsounds getFallSounds() { return new Fallsounds(SoundEvents.GENERIC_SMALL_FALL, SoundEvents.GENERIC_BIG_FALL); }
/*      */ 
/*      */ 
/*      */   
/* 1742 */   public Optional<BlockPos> getLastClimbablePos() { return this.lastClimbablePos; }
/*      */ 
/*      */   
/*      */   public boolean onClimbable() {
/* 1746 */     if (isSpectator()) {
/* 1747 */       return false;
/*      */     }
/*      */     
/* 1750 */     BlockPos ladderCheckPos = blockPosition();
/*      */     
/* 1752 */     BlockState state = getInBlockState();
/*      */     
/* 1754 */     if (isFallFlying() && state.is(BlockTags.CAN_GLIDE_THROUGH)) {
/* 1755 */       return false;
/*      */     }
/*      */     
/* 1758 */     if (state.is(BlockTags.CLIMBABLE)) {
/* 1759 */       this.lastClimbablePos = Optional.of(ladderCheckPos);
/* 1760 */       return true;
/*      */     } 
/*      */     
/* 1763 */     if (state.getBlock() instanceof TrapDoorBlock && trapdoorUsableAsLadder(ladderCheckPos, state)) {
/* 1764 */       this.lastClimbablePos = Optional.of(ladderCheckPos);
/* 1765 */       return true;
/*      */     } 
/* 1767 */     return false;
/*      */   }
/*      */   
/*      */   private boolean trapdoorUsableAsLadder(BlockPos pos, BlockState state) {
/* 1771 */     if (((Boolean)state.getValue(TrapDoorBlock.OPEN)).booleanValue()) {
/* 1772 */       BlockState belowState = level().getBlockState(pos.below());
/* 1773 */       return (belowState.is(Blocks.LADDER) && belowState.getValue(LadderBlock.FACING) == state.getValue(TrapDoorBlock.FACING));
/*      */     } 
/* 1775 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1783 */   public boolean isAlive() { return (!isRemoved() && getHealth() > 0.0F); }
/*      */ 
/*      */   
/*      */   public boolean isLookingAtMe(LivingEntity target, double coneSize, boolean adjustForDistance, boolean seeThroughTransparentBlocks, double... gazeHeights) {
/* 1787 */     Vec3 look = target.getViewVector(1.0F).normalize();
/* 1788 */     for (double gazeHeight : gazeHeights) {
/* 1789 */       Vec3 dir = new Vec3(getX() - target.getX(), gazeHeight - target.getEyeY(), getZ() - target.getZ());
/* 1790 */       double dist = dir.length();
/* 1791 */       dir = dir.normalize();
/* 1792 */       double dot = look.dot(dir);
/* 1793 */       if (dot > 1.0D - coneSize / (adjustForDistance ? dist : 1.0D) && 
/* 1794 */         target.hasLineOfSight(this, seeThroughTransparentBlocks ? ClipContext.Block.VISUAL : ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, gazeHeight)) {
/* 1795 */         return true;
/*      */       }
/*      */     } 
/*      */     
/* 1799 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1804 */   public int getMaxFallDistance() { return getComfortableFallDistance(0.0F); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1809 */   protected final int getComfortableFallDistance(float allowedDamage) { return Mth.floor(allowedDamage + 3.0F); }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
/* 1814 */     boolean damaged = super.causeFallDamage(fallDistance, damageModifier, damageSource);
/* 1815 */     int dmg = calculateFallDamage(fallDistance, damageModifier);
/*      */     
/* 1817 */     if (dmg > 0) {
/* 1818 */       playSound(getFallDamageSound(dmg), 1.0F, 1.0F);
/* 1819 */       playBlockFallSound();
/* 1820 */       hurt(damageSource, dmg);
/* 1821 */       return true;
/*      */     } 
/* 1823 */     return damaged;
/*      */   }
/*      */   
/*      */   protected int calculateFallDamage(double fallDistance, float damageModifier) {
/* 1827 */     if (getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE)) {
/* 1828 */       return 0;
/*      */     }
/* 1830 */     double baseDamage = calculateFallPower(fallDistance);
/* 1831 */     return Mth.floor(baseDamage * damageModifier * getAttributeValue(Attributes.FALL_DAMAGE_MULTIPLIER));
/*      */   }
/*      */ 
/*      */   
/* 1835 */   private double calculateFallPower(double fallDistance) { return fallDistance + 1.0E-6D - getAttributeValue(Attributes.SAFE_FALL_DISTANCE); }
/*      */ 
/*      */   
/*      */   protected void playBlockFallSound() {
/* 1839 */     if (isSilent()) {
/*      */       return;
/*      */     }
/* 1842 */     int xx = Mth.floor(getX());
/* 1843 */     int yy = Mth.floor(getY() - 0.20000000298023224D);
/* 1844 */     int zz = Mth.floor(getZ());
/*      */     
/* 1846 */     BlockState state = level().getBlockState(new BlockPos(xx, yy, zz));
/* 1847 */     if (!state.isAir()) {
/* 1848 */       SoundType soundType = state.getSoundType();
/* 1849 */       playSound(soundType.getFallSound(), soundType.getVolume() * 0.5F, soundType.getPitch() * 0.75F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void animateHurt(float yaw) {
/* 1855 */     this.hurtDuration = 10;
/* 1856 */     this.hurtTime = this.hurtDuration;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1865 */   public int getArmorValue() { return Mth.floor(getAttributeValue(Attributes.ARMOR)); }
/*      */ 
/*      */   
/*      */   protected void hurtArmor(DamageSource damageSource, float damage) {}
/*      */ 
/*      */   
/*      */   protected void hurtHelmet(DamageSource damageSource, float damage) {}
/*      */ 
/*      */   
/*      */   protected void doHurtEquipment(DamageSource damageSource, float damage, EquipmentSlot... slots) {
/* 1875 */     if (damage <= 0.0F) {
/*      */       return;
/*      */     }
/* 1878 */     int durabilityDamage = (int)Math.max(1.0F, damage / 4.0F);
/*      */     
/* 1880 */     for (EquipmentSlot slot : slots) {
/* 1881 */       ItemStack itemStack = getItemBySlot(slot);
/* 1882 */       Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 1883 */       if (equippable != null && equippable.damageOnHurt() && itemStack.isDamageableItem() && itemStack.canBeHurtBy(damageSource)) {
/* 1884 */         itemStack.hurtAndBreak(durabilityDamage, this, slot);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   protected float getDamageAfterArmorAbsorb(DamageSource damageSource, float damage) {
/* 1890 */     if (!damageSource.is(DamageTypeTags.BYPASSES_ARMOR)) {
/* 1891 */       hurtArmor(damageSource, damage);
/* 1892 */       damage = CombatRules.getDamageAfterAbsorb(this, damage, damageSource, getArmorValue(), (float)getAttributeValue(Attributes.ARMOR_TOUGHNESS));
/*      */     } 
/* 1894 */     return damage;
/*      */   }
/*      */   protected float getDamageAfterMagicAbsorb(DamageSource damageSource, float damage) {
/*      */     float enchantmentArmor;
/* 1898 */     if (damageSource.is(DamageTypeTags.BYPASSES_EFFECTS)) {
/* 1899 */       return damage;
/*      */     }
/*      */     
/* 1902 */     if (hasEffect(MobEffects.RESISTANCE) && !damageSource.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
/* 1903 */       enchantmentArmor = (getEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
/* 1904 */       int absorb = 25 - enchantmentArmor;
/* 1905 */       float v = damage * absorb;
/* 1906 */       float oldDamage = damage;
/* 1907 */       damage = Math.max(v / 25.0F, 0.0F);
/*      */       
/* 1909 */       float damageResisted = oldDamage - damage;
/* 1910 */       if (damageResisted > 0.0F && damageResisted < 3.4028235E37F) {
/* 1911 */         if (this instanceof ServerPlayer) {
/* 1912 */           ((ServerPlayer)this).awardStat(Stats.DAMAGE_RESISTED, Math.round(damageResisted * 10.0F));
/* 1913 */         } else if (damageSource.getEntity() instanceof ServerPlayer) {
/* 1914 */           ((ServerPlayer)damageSource.getEntity()).awardStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(damageResisted * 10.0F));
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 1919 */     if (damage <= 0.0F) {
/* 1920 */       return 0.0F;
/*      */     }
/*      */     
/* 1923 */     if (damageSource.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
/* 1924 */       return damage;
/*      */     }
/*      */ 
/*      */     
/* 1928 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 1929 */       enchantmentArmor = EnchantmentHelper.getDamageProtection(serverLevel, this, damageSource); }
/*      */     else
/* 1931 */     { enchantmentArmor = 0.0F; }
/*      */     
/* 1933 */     if (enchantmentArmor > 0.0F) {
/* 1934 */       damage = CombatRules.getDamageAfterMagicAbsorb(damage, enchantmentArmor);
/*      */     }
/*      */     
/* 1937 */     return damage;
/*      */   }
/*      */   
/*      */   protected void actuallyHurt(ServerLevel level, DamageSource source, float dmg) {
/* 1941 */     if (isInvulnerableTo(level, source)) {
/*      */       return;
/*      */     }
/* 1944 */     dmg = getDamageAfterArmorAbsorb(source, dmg);
/* 1945 */     dmg = getDamageAfterMagicAbsorb(source, dmg);
/*      */     
/* 1947 */     float originalDamage = dmg;
/* 1948 */     dmg = Math.max(dmg - getAbsorptionAmount(), 0.0F);
/* 1949 */     setAbsorptionAmount(getAbsorptionAmount() - originalDamage - dmg);
/*      */     
/* 1951 */     float absorbedDamage = originalDamage - dmg;
/* 1952 */     if (absorbedDamage > 0.0F && absorbedDamage < 3.4028235E37F) { Entity entity = source.getEntity(); if (entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 1953 */         serverPlayer.awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(absorbedDamage * 10.0F)); }
/*      */        }
/*      */     
/* 1956 */     if (dmg == 0.0F) {
/*      */       return;
/*      */     }
/*      */     
/* 1960 */     getCombatTracker().recordDamage(source, dmg);
/* 1961 */     setHealth(getHealth() - dmg);
/* 1962 */     setAbsorptionAmount(getAbsorptionAmount() - dmg);
/* 1963 */     gameEvent(GameEvent.ENTITY_DAMAGE);
/*      */   }
/*      */ 
/*      */   
/* 1967 */   public CombatTracker getCombatTracker() { return this.combatTracker; }
/*      */ 
/*      */   
/*      */   public LivingEntity getKillCredit() {
/* 1971 */     if (this.lastHurtByPlayer != null) {
/* 1972 */       return (LivingEntity)this.lastHurtByPlayer.getEntity(level(), Player.class);
/*      */     }
/* 1974 */     if (this.lastHurtByMob != null) {
/* 1975 */       return (LivingEntity)this.lastHurtByMob.getEntity(level(), LivingEntity.class);
/*      */     }
/* 1977 */     return null;
/*      */   }
/*      */ 
/*      */   
/* 1981 */   public final float getMaxHealth() { return (float)getAttributeValue(Attributes.MAX_HEALTH); }
/*      */ 
/*      */ 
/*      */   
/* 1985 */   public final float getMaxAbsorption() { return (float)getAttributeValue(Attributes.MAX_ABSORPTION); }
/*      */ 
/*      */ 
/*      */   
/* 1989 */   public final int getArrowCount() { return ((Integer)this.entityData.get(DATA_ARROW_COUNT_ID)).intValue(); }
/*      */ 
/*      */ 
/*      */   
/* 1993 */   public final void setArrowCount(int count) { this.entityData.set(DATA_ARROW_COUNT_ID, Integer.valueOf(count)); }
/*      */ 
/*      */ 
/*      */   
/* 1997 */   public final int getStingerCount() { return ((Integer)this.entityData.get(DATA_STINGER_COUNT_ID)).intValue(); }
/*      */ 
/*      */ 
/*      */   
/* 2001 */   public final void setStingerCount(int count) { this.entityData.set(DATA_STINGER_COUNT_ID, Integer.valueOf(count)); }
/*      */ 
/*      */   
/*      */   private int getCurrentSwingDuration() {
/* 2005 */     ItemStack handStack = getItemInHand(InteractionHand.MAIN_HAND);
/* 2006 */     int swingDuration = handStack.getSwingAnimation().duration();
/* 2007 */     if (MobEffectUtil.hasDigSpeed(this)) {
/* 2008 */       return swingDuration - 1 + MobEffectUtil.getDigSpeedAmplification(this);
/*      */     }
/* 2010 */     if (hasEffect(MobEffects.MINING_FATIGUE)) {
/* 2011 */       return swingDuration + (1 + getEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2;
/*      */     }
/* 2013 */     return swingDuration;
/*      */   }
/*      */ 
/*      */   
/* 2017 */   public void swing(InteractionHand hand) { swing(hand, false); }
/*      */ 
/*      */   
/*      */   public void swing(InteractionHand hand, boolean sendToSwingingEntity) {
/* 2021 */     if (!this.swinging || this.swingTime >= getCurrentSwingDuration() / 2 || this.swingTime < 0) {
/* 2022 */       this.swingTime = -1;
/* 2023 */       this.swinging = true;
/* 2024 */       this.swingingArm = hand;
/*      */       
/* 2026 */       if (level() instanceof ServerLevel) {
/* 2027 */         ClientboundAnimatePacket packet = new ClientboundAnimatePacket(this, (hand == InteractionHand.MAIN_HAND) ? 0 : 3);
/* 2028 */         ServerChunkCache chunkSource = ((ServerLevel)level()).getChunkSource();
/*      */         
/* 2030 */         if (sendToSwingingEntity) {
/* 2031 */           chunkSource.sendToTrackingPlayersAndSelf(this, packet);
/*      */         } else {
/* 2033 */           chunkSource.sendToTrackingPlayers(this, packet);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleDamageEvent(DamageSource source) {
/* 2041 */     this.walkAnimation.setSpeed(1.5F);
/*      */     
/* 2043 */     this.invulnerableTime = 20;
/* 2044 */     this.hurtDuration = 10;
/* 2045 */     this.hurtTime = this.hurtDuration;
/*      */     
/* 2047 */     SoundEvent hurtSound = getHurtSound(source);
/* 2048 */     if (hurtSound != null) {
/* 2049 */       playSound(hurtSound, getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/*      */     }
/* 2051 */     this.lastDamageSource = source;
/* 2052 */     this.lastDamageStamp = level().getGameTime();
/*      */   }
/*      */   public void handleEntityEvent(byte id) {
/*      */     int i, count;
/*      */     SoundEvent deathSound;
/* 2057 */     switch (id) {
/*      */       case 3:
/* 2059 */         deathSound = getDeathSound();
/* 2060 */         if (deathSound != null) {
/* 2061 */           playSound(deathSound, getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/*      */         }
/*      */         
/* 2064 */         if (!(this instanceof Player)) {
/* 2065 */           setHealth(0.0F);
/* 2066 */           die(damageSources().generic());
/*      */         } 
/*      */         return;
/*      */       
/*      */       case 46:
/* 2071 */         count = 128;
/*      */         
/* 2073 */         for (i = 0; i < 128; i++) {
/* 2074 */           double d = i / 127.0D;
/* 2075 */           float xa = (this.random.nextFloat() - 0.5F) * 0.2F;
/* 2076 */           float ya = (this.random.nextFloat() - 0.5F) * 0.2F;
/* 2077 */           float za = (this.random.nextFloat() - 0.5F) * 0.2F;
/*      */           
/* 2079 */           double x = Mth.lerp(d, this.xo, getX()) + (this.random.nextDouble() - 0.5D) * getBbWidth() * 2.0D;
/* 2080 */           double y = Mth.lerp(d, this.yo, getY()) + this.random.nextDouble() * getBbHeight();
/* 2081 */           double z = Mth.lerp(d, this.zo, getZ()) + (this.random.nextDouble() - 0.5D) * getBbWidth() * 2.0D;
/* 2082 */           level().addParticle(ParticleTypes.PORTAL, x, y, z, xa, ya, za);
/*      */         } 
/*      */         return;
/*      */       
/*      */       case 47:
/* 2087 */         breakItem(getItemBySlot(EquipmentSlot.MAINHAND));
/*      */         return;
/*      */       case 48:
/* 2090 */         breakItem(getItemBySlot(EquipmentSlot.OFFHAND));
/*      */         return;
/*      */       case 49:
/* 2093 */         breakItem(getItemBySlot(EquipmentSlot.HEAD));
/*      */         return;
/*      */       case 50:
/* 2096 */         breakItem(getItemBySlot(EquipmentSlot.CHEST));
/*      */         return;
/*      */       case 51:
/* 2099 */         breakItem(getItemBySlot(EquipmentSlot.LEGS));
/*      */         return;
/*      */       case 52:
/* 2102 */         breakItem(getItemBySlot(EquipmentSlot.FEET));
/*      */         return;
/*      */       case 65:
/* 2105 */         breakItem(getItemBySlot(EquipmentSlot.BODY));
/*      */         return;
/*      */       case 68:
/* 2108 */         breakItem(getItemBySlot(EquipmentSlot.SADDLE));
/*      */         return;
/*      */       case 54:
/* 2111 */         HoneyBlock.showJumpParticles(this);
/*      */         return;
/*      */       case 55:
/* 2114 */         swapHandItems();
/*      */         return;
/*      */       case 60:
/* 2117 */         makePoofParticles();
/*      */         return;
/*      */       case 67:
/* 2120 */         makeDrownParticles();
/*      */         return;
/*      */       case 2:
/* 2123 */         onKineticHit();
/*      */         return;
/*      */     } 
/* 2126 */     super.handleEntityEvent(id);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getTicksSinceLastKineticHitFeedback(float partial) {
/* 2131 */     if (this.lastKineticHitFeedbackTime < 0L) {
/* 2132 */       return 0.0F;
/*      */     }
/* 2134 */     return (float)(level().getGameTime() - this.lastKineticHitFeedbackTime) + partial;
/*      */   }
/*      */   
/*      */   public void makePoofParticles() {
/* 2138 */     for (int i = 0; i < 20; i++) {
/* 2139 */       double xa = this.random.nextGaussian() * 0.02D;
/* 2140 */       double ya = this.random.nextGaussian() * 0.02D;
/* 2141 */       double za = this.random.nextGaussian() * 0.02D;
/* 2142 */       double dd = 10.0D;
/* 2143 */       level().addParticle(ParticleTypes.POOF, getRandomX(1.0D) - xa * 10.0D, getRandomY() - ya * 10.0D, getRandomZ(1.0D) - za * 10.0D, xa, ya, za);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void makeDrownParticles() {
/* 2148 */     Vec3 movement = getDeltaMovement();
/* 2149 */     for (int i = 0; i < 8; i++) {
/* 2150 */       double offsetX = this.random.triangle(0.0D, 1.0D);
/* 2151 */       double offsetY = this.random.triangle(0.0D, 1.0D);
/* 2152 */       double offsetZ = this.random.triangle(0.0D, 1.0D);
/* 2153 */       level().addParticle(ParticleTypes.BUBBLE, getX() + offsetX, getY() + offsetY, getZ() + offsetZ, movement.x, movement.y, movement.z);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void onKineticHit() {
/* 2159 */     if (level().getGameTime() - this.lastKineticHitFeedbackTime <= 10L) {
/*      */       return;
/*      */     }
/*      */     
/* 2163 */     this.lastKineticHitFeedbackTime = level().getGameTime();
/*      */     
/* 2165 */     KineticWeapon kineticWeapon = (KineticWeapon)this.useItem.get(DataComponents.KINETIC_WEAPON);
/* 2166 */     if (kineticWeapon == null) {
/*      */       return;
/*      */     }
/*      */     
/* 2170 */     kineticWeapon.makeLocalHitSound(this);
/*      */   }
/*      */   
/*      */   private void swapHandItems() {
/* 2174 */     ItemStack tmp = getItemBySlot(EquipmentSlot.OFFHAND);
/* 2175 */     setItemSlot(EquipmentSlot.OFFHAND, getItemBySlot(EquipmentSlot.MAINHAND));
/* 2176 */     setItemSlot(EquipmentSlot.MAINHAND, tmp);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 2181 */   protected void onBelowWorld() { hurt(damageSources().fellOutOfWorld(), 4.0F); }
/*      */ 
/*      */   
/*      */   protected void updateSwingTime() {
/* 2185 */     int currentSwingDuration = getCurrentSwingDuration();
/* 2186 */     if (this.swinging) {
/* 2187 */       this.swingTime++;
/* 2188 */       if (this.swingTime >= currentSwingDuration) {
/* 2189 */         this.swingTime = 0;
/* 2190 */         this.swinging = false;
/*      */       } 
/*      */     } else {
/* 2193 */       this.swingTime = 0;
/*      */     } 
/*      */     
/* 2196 */     this.attackAnim = this.swingTime / currentSwingDuration;
/*      */   }
/*      */ 
/*      */   
/* 2200 */   public AttributeInstance getAttribute(Holder<Attribute> attribute) { return getAttributes().getInstance(attribute); }
/*      */ 
/*      */ 
/*      */   
/* 2204 */   public double getAttributeValue(Holder<Attribute> attribute) { return getAttributes().getValue(attribute); }
/*      */ 
/*      */ 
/*      */   
/* 2208 */   public double getAttributeBaseValue(Holder<Attribute> attribute) { return getAttributes().getBaseValue(attribute); }
/*      */ 
/*      */ 
/*      */   
/* 2212 */   public AttributeMap getAttributes() { return this.attributes; }
/*      */ 
/*      */ 
/*      */   
/* 2216 */   public ItemStack getMainHandItem() { return getItemBySlot(EquipmentSlot.MAINHAND); }
/*      */ 
/*      */ 
/*      */   
/* 2220 */   public ItemStack getOffhandItem() { return getItemBySlot(EquipmentSlot.OFFHAND); }
/*      */ 
/*      */ 
/*      */   
/* 2224 */   public ItemStack getItemHeldByArm(HumanoidArm arm) { return (getMainArm() == arm) ? getMainHandItem() : getOffhandItem(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2229 */   public ItemStack getWeaponItem() { return getMainHandItem(); }
/*      */ 
/*      */   
/*      */   public AttackRange entityAttackRange() {
/* 2233 */     AttackRange attackRange = (AttackRange)getActiveItem().get(DataComponents.ATTACK_RANGE);
/* 2234 */     return (attackRange != null) ? attackRange : AttackRange.defaultFor(this);
/*      */   }
/*      */   
/*      */   public ItemStack getActiveItem() {
/* 2238 */     if (isUsingItem()) {
/* 2239 */       return getUseItem();
/*      */     }
/* 2241 */     return getMainHandItem();
/*      */   }
/*      */ 
/*      */   
/* 2245 */   public boolean isHolding(Item item) { return isHolding(heldItem -> heldItem.is(item)); }
/*      */ 
/*      */ 
/*      */   
/* 2249 */   public boolean isHolding(Predicate<ItemStack> itemPredicate) { return (itemPredicate.test(getMainHandItem()) || itemPredicate.test(getOffhandItem())); }
/*      */ 
/*      */   
/*      */   public ItemStack getItemInHand(InteractionHand hand) {
/* 2253 */     if (hand == InteractionHand.MAIN_HAND)
/* 2254 */       return getItemBySlot(EquipmentSlot.MAINHAND); 
/* 2255 */     if (hand == InteractionHand.OFF_HAND) {
/* 2256 */       return getItemBySlot(EquipmentSlot.OFFHAND);
/*      */     }
/* 2258 */     throw new IllegalArgumentException("Invalid hand " + String.valueOf(hand));
/*      */   }
/*      */ 
/*      */   
/*      */   public void setItemInHand(InteractionHand hand, ItemStack itemStack) {
/* 2263 */     if (hand == InteractionHand.MAIN_HAND) {
/* 2264 */       setItemSlot(EquipmentSlot.MAINHAND, itemStack);
/* 2265 */     } else if (hand == InteractionHand.OFF_HAND) {
/* 2266 */       setItemSlot(EquipmentSlot.OFFHAND, itemStack);
/*      */     } else {
/* 2268 */       throw new IllegalArgumentException("Invalid hand " + String.valueOf(hand));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 2273 */   public boolean hasItemInSlot(EquipmentSlot slot) { return !getItemBySlot(slot).isEmpty(); }
/*      */ 
/*      */ 
/*      */   
/* 2277 */   public boolean canUseSlot(EquipmentSlot slot) { return true; }
/*      */ 
/*      */ 
/*      */   
/* 2281 */   public ItemStack getItemBySlot(EquipmentSlot slot) { return this.equipment.get(slot); }
/*      */ 
/*      */ 
/*      */   
/* 2285 */   public void setItemSlot(EquipmentSlot slot, ItemStack itemStack) { onEquipItem(slot, this.equipment.set(slot, itemStack), itemStack); }
/*      */ 
/*      */   
/*      */   public float getArmorCoverPercentage() {
/* 2289 */     int total = 0;
/* 2290 */     int count = 0;
/* 2291 */     for (EquipmentSlot slot : EquipmentSlotGroup.ARMOR) {
/*      */       
/* 2293 */       if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) {
/*      */         continue;
/*      */       }
/* 2296 */       ItemStack itemStack = getItemBySlot(slot);
/* 2297 */       if (!itemStack.isEmpty()) {
/* 2298 */         count++;
/*      */       }
/* 2300 */       total++;
/*      */     } 
/* 2302 */     return (total > 0) ? (count / total) : 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSprinting(boolean isSprinting) {
/* 2307 */     super.setSprinting(isSprinting);
/*      */     
/* 2309 */     AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
/* 2310 */     speed.removeModifier(SPEED_MODIFIER_SPRINTING.id());
/* 2311 */     if (isSprinting) {
/* 2312 */       speed.addTransientModifier(SPEED_MODIFIER_SPRINTING);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 2317 */   protected float getSoundVolume() { return 1.0F; }
/*      */ 
/*      */   
/*      */   public float getVoicePitch() {
/* 2321 */     if (isBaby()) {
/* 2322 */       return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F;
/*      */     }
/* 2324 */     return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
/*      */   }
/*      */ 
/*      */   
/* 2328 */   protected boolean isImmobile() { return isDeadOrDying(); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void push(Entity entity) {
/* 2333 */     if (!isSleeping()) {
/* 2334 */       super.push(entity);
/*      */     }
/*      */   }
/*      */   
/*      */   private void dismountVehicle(Entity vehicle) {
/*      */     Vec3 teleportTarget;
/* 2340 */     if (isRemoved()) {
/* 2341 */       teleportTarget = position();
/* 2342 */     } else if (vehicle.isRemoved() || level().getBlockState(vehicle.blockPosition()).is(BlockTags.PORTALS)) {
/*      */ 
/*      */       
/* 2345 */       double maxY = Math.max(getY(), vehicle.getY());
/* 2346 */       teleportTarget = new Vec3(getX(), maxY, getZ());
/*      */       
/* 2348 */       boolean isSmall = (getBbWidth() <= 4.0F && getBbHeight() <= 4.0F);
/* 2349 */       if (isSmall) {
/* 2350 */         double halfHeight = getBbHeight() / 2.0D;
/* 2351 */         Vec3 center = teleportTarget.add(0.0D, halfHeight, 0.0D);
/* 2352 */         VoxelShape allowedCenters = Shapes.create(AABB.ofSize(center, getBbWidth(), getBbHeight(), getBbWidth()));
/*      */ 
/*      */ 
/*      */         
/* 2356 */         teleportTarget = (Vec3)level().findFreePosition(this, allowedCenters, center, getBbWidth(), getBbHeight(), getBbWidth()).map(pos -> pos.add(0.0D, -halfHeight, 0.0D)).orElse(teleportTarget);
/*      */       } 
/*      */     } else {
/* 2359 */       teleportTarget = vehicle.getDismountLocationForPassenger(this);
/*      */     } 
/*      */     
/* 2362 */     dismountTo(teleportTarget.x, teleportTarget.y, teleportTarget.z);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 2367 */   public boolean shouldShowName() { return isCustomNameVisible(); }
/*      */ 
/*      */ 
/*      */   
/* 2371 */   protected float getJumpPower() { return getJumpPower(1.0F); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2376 */   protected float getJumpPower(float multiplier) { return (float)getAttributeValue(Attributes.JUMP_STRENGTH) * multiplier * getBlockJumpFactor() + getJumpBoostPower(); }
/*      */ 
/*      */ 
/*      */   
/* 2380 */   public float getJumpBoostPower() { return hasEffect(MobEffects.JUMP_BOOST) ? (0.1F * (getEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1.0F)) : 0.0F; }
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   public void jumpFromGround() {
/* 2385 */     float jumpPower = getJumpPower();
/* 2386 */     if (jumpPower <= 1.0E-5F) {
/*      */       return;
/*      */     }
/*      */     
/* 2390 */     Vec3 movement = getDeltaMovement();
/* 2391 */     setDeltaMovement(movement.x, Math.max(jumpPower, movement.y), movement.z);
/*      */     
/* 2393 */     if (isSprinting()) {
/* 2394 */       float angle = getYRot() * 0.017453292F;
/* 2395 */       addDeltaMovement(new Vec3(
/* 2396 */             -Mth.sin(angle) * 0.2D, 0.0D, 
/*      */             
/* 2398 */             Mth.cos(angle) * 0.2D));
/*      */     } 
/*      */     
/* 2401 */     this.needsSync = true;
/*      */   }
/*      */ 
/*      */   
/* 2405 */   protected void goDownInWater() { setDeltaMovement(getDeltaMovement().add(0.0D, -0.03999999910593033D, 0.0D)); }
/*      */ 
/*      */ 
/*      */   
/* 2409 */   protected void jumpInLiquid(TagKey<Fluid> type) { setDeltaMovement(getDeltaMovement().add(0.0D, 0.03999999910593033D, 0.0D)); }
/*      */ 
/*      */ 
/*      */   
/* 2413 */   protected float getWaterSlowDown() { return 0.8F; }
/*      */ 
/*      */ 
/*      */   
/* 2417 */   public boolean canStandOnFluid(FluidState fluid) { return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2422 */   protected double getDefaultGravity() { return getAttributeValue(Attributes.GRAVITY); }
/*      */ 
/*      */   
/*      */   protected double getEffectiveGravity() {
/* 2426 */     boolean isFalling = ((getDeltaMovement()).y <= 0.0D);
/* 2427 */     if (isFalling && hasEffect(MobEffects.SLOW_FALLING)) {
/* 2428 */       return Math.min(getGravity(), 0.01D);
/*      */     }
/* 2430 */     return getGravity();
/*      */   }
/*      */   
/*      */   public void travel(Vec3 input) {
/* 2434 */     if (shouldTravelInFluid(level().getFluidState(blockPosition()))) {
/* 2435 */       travelInFluid(input);
/* 2436 */     } else if (isFallFlying()) {
/* 2437 */       travelFallFlying(input);
/*      */     } else {
/* 2439 */       travelInAir(input);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 2444 */   protected boolean shouldTravelInFluid(FluidState fluidState) { return ((isInWater() || isInLava()) && isAffectedByFluids() && !canStandOnFluid(fluidState)); }
/*      */ 
/*      */ 
/*      */   
/* 2448 */   protected void travelFlying(Vec3 input, float speed) { travelFlying(input, 0.02F, 0.02F, speed); }
/*      */ 
/*      */   
/*      */   protected void travelFlying(Vec3 input, float waterSpeed, float lavaSpeed, float airSpeed) {
/* 2452 */     if (isInWater()) {
/* 2453 */       moveRelative(waterSpeed, input);
/* 2454 */       move(MoverType.SELF, getDeltaMovement());
/* 2455 */       setDeltaMovement(getDeltaMovement().scale(0.800000011920929D));
/* 2456 */     } else if (isInLava()) {
/* 2457 */       moveRelative(lavaSpeed, input);
/* 2458 */       move(MoverType.SELF, getDeltaMovement());
/* 2459 */       setDeltaMovement(getDeltaMovement().scale(0.5D));
/*      */     } else {
/* 2461 */       moveRelative(airSpeed, input);
/* 2462 */       move(MoverType.SELF, getDeltaMovement());
/* 2463 */       setDeltaMovement(getDeltaMovement().scale(0.9100000262260437D));
/*      */     } 
/*      */   }
/*      */   
/*      */   private void travelInAir(Vec3 input) {
/* 2468 */     BlockPos posBelow = getBlockPosBelowThatAffectsMyMovement();
/* 2469 */     float blockFriction = onGround() ? level().getBlockState(posBelow).getBlock().getFriction() : 1.0F;
/* 2470 */     float friction = blockFriction * 0.91F;
/* 2471 */     Vec3 movement = handleRelativeFrictionAndCalculateMovement(input, blockFriction);
/*      */     
/* 2473 */     double movementY = movement.y;
/* 2474 */     MobEffectInstance levitationEffect = getEffect(MobEffects.LEVITATION);
/* 2475 */     if (levitationEffect != null) {
/* 2476 */       movementY += (0.05D * (levitationEffect.getAmplifier() + 1) - movement.y) * 0.2D;
/* 2477 */     } else if (!level().isClientSide() || level().hasChunkAt(posBelow)) {
/* 2478 */       movementY -= getEffectiveGravity();
/* 2479 */     } else if (getY() > level().getMinY()) {
/* 2480 */       movementY = -0.1D;
/*      */     } else {
/* 2482 */       movementY = 0.0D;
/*      */     } 
/*      */     
/* 2485 */     if (shouldDiscardFriction()) {
/* 2486 */       setDeltaMovement(movement.x, movementY, movement.z);
/*      */     } else {
/* 2488 */       float verticalFriction = (this instanceof net.minecraft.world.entity.animal.FlyingAnimal) ? friction : 0.98F;
/* 2489 */       setDeltaMovement(movement.x * friction, movementY * verticalFriction, movement.z * friction);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void travelInFluid(Vec3 input) {
/* 2494 */     boolean isFalling = ((getDeltaMovement()).y <= 0.0D);
/* 2495 */     double oldY = getY();
/* 2496 */     double baseGravity = getEffectiveGravity();
/*      */     
/* 2498 */     if (isInWater()) {
/* 2499 */       travelInWater(input, baseGravity, isFalling, oldY);
/* 2500 */       floatInWaterWhileRidden();
/*      */     } else {
/* 2502 */       travelInLava(input, baseGravity, isFalling, oldY);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 2507 */     float slowDown = isSprinting() ? 0.9F : getWaterSlowDown();
/* 2508 */     float speed = 0.02F;
/*      */     
/* 2510 */     float waterWalker = (float)getAttributeValue(Attributes.WATER_MOVEMENT_EFFICIENCY);
/* 2511 */     if (!onGround()) {
/* 2512 */       waterWalker *= 0.5F;
/*      */     }
/* 2514 */     if (waterWalker > 0.0F) {
/*      */       
/* 2516 */       slowDown += (0.54600006F - slowDown) * waterWalker;
/*      */       
/* 2518 */       speed += (getSpeed() - speed) * waterWalker;
/*      */     } 
/*      */     
/* 2521 */     if (hasEffect(MobEffects.DOLPHINS_GRACE)) {
/* 2522 */       slowDown = 0.96F;
/*      */     }
/*      */     
/* 2525 */     moveRelative(speed, input);
/* 2526 */     move(MoverType.SELF, getDeltaMovement());
/*      */     
/* 2528 */     Vec3 ladderMovement = getDeltaMovement();
/* 2529 */     if (this.horizontalCollision && onClimbable()) {
/* 2530 */       ladderMovement = new Vec3(ladderMovement.x, 0.2D, ladderMovement.z);
/*      */     }
/*      */     
/* 2533 */     ladderMovement = ladderMovement.multiply(slowDown, 0.800000011920929D, slowDown);
/* 2534 */     setDeltaMovement(getFluidFallingAdjustedMovement(baseGravity, isFalling, ladderMovement));
/*      */     
/* 2536 */     jumpOutOfFluid(oldY);
/*      */   }
/*      */   
/*      */   private void travelInLava(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 2540 */     moveRelative(0.02F, input);
/* 2541 */     move(MoverType.SELF, getDeltaMovement());
/*      */ 
/*      */ 
/*      */     
/* 2545 */     if (getFluidHeight(FluidTags.LAVA) <= getFluidJumpThreshold()) {
/* 2546 */       setDeltaMovement(getDeltaMovement().multiply(0.5D, 0.800000011920929D, 0.5D));
/* 2547 */       Vec3 movement = getFluidFallingAdjustedMovement(baseGravity, isFalling, getDeltaMovement());
/* 2548 */       setDeltaMovement(movement);
/*      */     } else {
/* 2550 */       setDeltaMovement(getDeltaMovement().scale(0.5D));
/*      */     } 
/*      */     
/* 2553 */     if (baseGravity != 0.0D) {
/* 2554 */       setDeltaMovement(getDeltaMovement().add(0.0D, -baseGravity / 4.0D, 0.0D));
/*      */     }
/*      */     
/* 2557 */     jumpOutOfFluid(oldY);
/*      */   }
/*      */   
/*      */   private void jumpOutOfFluid(double oldY) {
/* 2561 */     Vec3 movement = getDeltaMovement();
/* 2562 */     if (this.horizontalCollision && isFree(movement.x, movement.y + 0.6000000238418579D - getY() + oldY, movement.z)) {
/* 2563 */       setDeltaMovement(movement.x, 0.30000001192092896D, movement.z);
/*      */     }
/*      */   }
/*      */   
/*      */   private void floatInWaterWhileRidden() {
/* 2568 */     boolean canEntityFloatInWater = getType().is(EntityTypeTags.CAN_FLOAT_WHILE_RIDDEN);
/* 2569 */     if (canEntityFloatInWater && isVehicle() && getFluidHeight(FluidTags.WATER) > getFluidJumpThreshold()) {
/* 2570 */       setDeltaMovement(getDeltaMovement().add(0.0D, 0.03999999910593033D, 0.0D));
/*      */     }
/*      */   }
/*      */   
/*      */   private void travelFallFlying(Vec3 input) {
/* 2575 */     if (onClimbable()) {
/*      */       
/* 2577 */       travelInAir(input);
/* 2578 */       stopFallFlying();
/*      */       return;
/*      */     } 
/* 2581 */     Vec3 lastMovement = getDeltaMovement();
/* 2582 */     double lastSpeed = lastMovement.horizontalDistance();
/*      */     
/* 2584 */     setDeltaMovement(updateFallFlyingMovement(lastMovement));
/* 2585 */     move(MoverType.SELF, getDeltaMovement());
/*      */     
/* 2587 */     if (!level().isClientSide()) {
/* 2588 */       double newSpeed = getDeltaMovement().horizontalDistance();
/* 2589 */       handleFallFlyingCollisions(lastSpeed, newSpeed);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void stopFallFlying() {
/* 2595 */     setSharedFlag(7, true);
/* 2596 */     setSharedFlag(7, false);
/*      */   }
/*      */   
/*      */   private Vec3 updateFallFlyingMovement(Vec3 movement) {
/* 2600 */     Vec3 lookAngle = getLookAngle();
/* 2601 */     float leanAngle = getXRot() * 0.017453292F;
/* 2602 */     double lookHorLength = Math.sqrt(lookAngle.x * lookAngle.x + lookAngle.z * lookAngle.z);
/* 2603 */     double moveHorLength = movement.horizontalDistance();
/*      */     
/* 2605 */     double gravity = getEffectiveGravity();
/*      */ 
/*      */     
/* 2608 */     double liftForce = Mth.square(Math.cos(leanAngle));
/* 2609 */     movement = movement.add(0.0D, gravity * (-1.0D + liftForce * 0.75D), 0.0D);
/*      */ 
/*      */     
/* 2612 */     if (movement.y < 0.0D && lookHorLength > 0.0D) {
/* 2613 */       double convert = movement.y * -0.1D * liftForce;
/* 2614 */       movement = movement.add(lookAngle.x * convert / lookHorLength, convert, lookAngle.z * convert / lookHorLength);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2621 */     if (leanAngle < 0.0F && lookHorLength > 0.0D) {
/* 2622 */       double convert = moveHorLength * -Mth.sin(leanAngle) * 0.04D;
/*      */       
/* 2624 */       movement = movement.add(-lookAngle.x * convert / lookHorLength, convert * 3.2D, -lookAngle.z * convert / lookHorLength);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2632 */     if (lookHorLength > 0.0D) {
/* 2633 */       movement = movement.add((lookAngle.x / lookHorLength * moveHorLength - movement.x) * 0.1D, 0.0D, (lookAngle.z / lookHorLength * moveHorLength - movement.z) * 0.1D);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2640 */     return movement.multiply(0.9900000095367432D, 0.9800000190734863D, 0.9900000095367432D);
/*      */   }
/*      */   
/*      */   private void handleFallFlyingCollisions(double moveHorLength, double newMoveHorLength) {
/* 2644 */     if (this.horizontalCollision) {
/* 2645 */       double diff = moveHorLength - newMoveHorLength;
/* 2646 */       float dmg = (float)(diff * 10.0D - 3.0D);
/*      */       
/* 2648 */       if (dmg > 0.0F) {
/* 2649 */         playSound(getFallDamageSound((int)dmg), 1.0F, 1.0F);
/* 2650 */         hurt(damageSources().flyIntoWall(), dmg);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void travelRidden(Player controller, Vec3 selfInput) {
/* 2656 */     Vec3 riddenInput = getRiddenInput(controller, selfInput);
/* 2657 */     tickRidden(controller, riddenInput);
/*      */     
/* 2659 */     if (canSimulateMovement()) {
/* 2660 */       setSpeed(getRiddenSpeed(controller));
/* 2661 */       travel(riddenInput);
/*      */     } else {
/* 2663 */       setDeltaMovement(Vec3.ZERO);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void tickRidden(Player controller, Vec3 riddenInput) {}
/*      */ 
/*      */   
/* 2671 */   protected Vec3 getRiddenInput(Player controller, Vec3 selfInput) { return selfInput; }
/*      */ 
/*      */ 
/*      */   
/* 2675 */   protected float getRiddenSpeed(Player controller) { return getSpeed(); }
/*      */ 
/*      */   
/*      */   public void calculateEntityAnimation(boolean useY) {
/* 2679 */     float distance = (float)Mth.length(
/* 2680 */         getX() - this.xo, 
/* 2681 */         useY ? (getY() - this.yo) : 0.0D, 
/* 2682 */         getZ() - this.zo);
/*      */ 
/*      */     
/* 2685 */     if (isPassenger() || !isAlive()) {
/* 2686 */       this.walkAnimation.stop();
/*      */     } else {
/* 2688 */       updateWalkAnimation(distance);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void updateWalkAnimation(float distance) {
/* 2693 */     float targetSpeed = Math.min(distance * 4.0F, 1.0F);
/* 2694 */     this.walkAnimation.update(targetSpeed, 0.4F, isBaby() ? 3.0F : 1.0F);
/*      */   }
/*      */   
/*      */   private Vec3 handleRelativeFrictionAndCalculateMovement(Vec3 input, float friction) {
/* 2698 */     moveRelative(getFrictionInfluencedSpeed(friction), input);
/*      */     
/* 2700 */     setDeltaMovement(handleOnClimbable(getDeltaMovement()));
/* 2701 */     move(MoverType.SELF, getDeltaMovement());
/*      */     
/* 2703 */     Vec3 movement = getDeltaMovement();
/* 2704 */     if ((this.horizontalCollision || this.jumping) && (onClimbable() || (this.wasInPowderSnow && PowderSnowBlock.canEntityWalkOnPowderSnow(this)))) {
/* 2705 */       movement = new Vec3(movement.x, 0.2D, movement.z);
/*      */     }
/* 2707 */     return movement;
/*      */   }
/*      */   
/*      */   public Vec3 getFluidFallingAdjustedMovement(double baseGravity, boolean isFalling, Vec3 movement) {
/* 2711 */     if (baseGravity != 0.0D && !isSprinting()) {
/*      */       double yd;
/* 2713 */       if (isFalling && Math.abs(movement.y - 0.005D) >= 0.003D && Math.abs(movement.y - baseGravity / 16.0D) < 0.003D) {
/*      */         
/* 2715 */         yd = -0.003D;
/*      */       } else {
/* 2717 */         yd = movement.y - baseGravity / 16.0D;
/*      */       } 
/* 2719 */       return new Vec3(movement.x, yd, movement.z);
/*      */     } 
/* 2721 */     return movement;
/*      */   }
/*      */   
/*      */   private Vec3 handleOnClimbable(Vec3 delta) {
/* 2725 */     if (onClimbable()) {
/* 2726 */       resetFallDistance();
/*      */       
/* 2728 */       float max = 0.15F;
/* 2729 */       double xd = Mth.clamp(delta.x, -0.15000000596046448D, 0.15000000596046448D);
/* 2730 */       double zd = Mth.clamp(delta.z, -0.15000000596046448D, 0.15000000596046448D);
/*      */       
/* 2732 */       double yd = Math.max(delta.y, -0.15000000596046448D);
/* 2733 */       if (yd < 0.0D && !getInBlockState().is(Blocks.SCAFFOLDING) && isSuppressingSlidingDownLadder() && this instanceof Player) {
/* 2734 */         yd = 0.0D;
/*      */       }
/*      */       
/* 2737 */       delta = new Vec3(xd, yd, zd);
/*      */     } 
/* 2739 */     return delta;
/*      */   }
/*      */   
/*      */   private float getFrictionInfluencedSpeed(float blockFriction) {
/* 2743 */     if (onGround()) {
/* 2744 */       return getSpeed() * 0.21600002F / blockFriction * blockFriction * blockFriction;
/*      */     }
/* 2746 */     return getFlyingSpeed();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 2751 */   protected float getFlyingSpeed() { return (getControllingPassenger() instanceof Player) ? (getSpeed() * 0.1F) : 0.02F; }
/*      */ 
/*      */ 
/*      */   
/* 2755 */   public float getSpeed() { return this.speed; }
/*      */ 
/*      */ 
/*      */   
/* 2759 */   public void setSpeed(float speed) { this.speed = speed; }
/*      */ 
/*      */   
/*      */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/* 2763 */     setLastHurtMob(target);
/* 2764 */     return false;
/*      */   }
/*      */   
/*      */   public void causeExtraKnockback(Entity target, float knockback, Vec3 oldMovement) {
/* 2768 */     if (knockback > 0.0F && target instanceof LivingEntity) { LivingEntity livingTarget = (LivingEntity)target;
/* 2769 */       livingTarget.knockback(knockback, Mth.sin((getYRot() * 0.017453292F)), -Mth.cos((getYRot() * 0.017453292F)));
/* 2770 */       setDeltaMovement(getDeltaMovement().multiply(0.6D, 1.0D, 0.6D)); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   protected void playAttackSound() {}
/*      */ 
/*      */   
/*      */   public void tick() {
/* 2779 */     super.tick();
/* 2780 */     updatingUsingItem();
/* 2781 */     updateSwimAmount();
/*      */     
/* 2783 */     if (!level().isClientSide()) {
/* 2784 */       int arrowCount = getArrowCount();
/* 2785 */       if (arrowCount > 0) {
/* 2786 */         if (this.removeArrowTime <= 0) {
/* 2787 */           this.removeArrowTime = 20 * (30 - arrowCount);
/*      */         }
/* 2789 */         this.removeArrowTime--;
/* 2790 */         if (this.removeArrowTime <= 0) {
/* 2791 */           setArrowCount(arrowCount - 1);
/*      */         }
/*      */       } 
/*      */       
/* 2795 */       int stingerCount = getStingerCount();
/* 2796 */       if (stingerCount > 0) {
/* 2797 */         if (this.removeStingerTime <= 0) {
/* 2798 */           this.removeStingerTime = 20 * (30 - stingerCount);
/*      */         }
/* 2800 */         this.removeStingerTime--;
/* 2801 */         if (this.removeStingerTime <= 0) {
/* 2802 */           setStingerCount(stingerCount - 1);
/*      */         }
/*      */       } 
/*      */       
/* 2806 */       detectEquipmentUpdates();
/*      */       
/* 2808 */       if (this.tickCount % 20 == 0) {
/* 2809 */         getCombatTracker().recheckStatus();
/*      */       }
/*      */       
/* 2812 */       if (isSleeping() && (!canInteractWithLevel() || !checkBedExists())) {
/* 2813 */         stopSleeping();
/*      */       }
/*      */     } 
/*      */     
/* 2817 */     if (!isRemoved()) {
/* 2818 */       aiStep();
/*      */     }
/*      */     
/* 2821 */     double xd = getX() - this.xo;
/* 2822 */     double zd = getZ() - this.zo;
/*      */     
/* 2824 */     float sideDist = (float)(xd * xd + zd * zd);
/*      */     
/* 2826 */     float yBodyRotT = this.yBodyRot;
/*      */     
/* 2828 */     if (sideDist > 0.0025000002F) {
/* 2829 */       float walkDirection = (float)Mth.atan2(zd, xd) * 57.295776F - 90.0F;
/* 2830 */       float diffBetweenDirectionAndFacing = Mth.abs(Mth.wrapDegrees(getYRot()) - walkDirection);
/* 2831 */       if (95.0F < diffBetweenDirectionAndFacing && diffBetweenDirectionAndFacing < 265.0F) {
/* 2832 */         yBodyRotT = walkDirection - 180.0F;
/*      */       } else {
/* 2834 */         yBodyRotT = walkDirection;
/*      */       } 
/*      */     } 
/* 2837 */     if (this.attackAnim > 0.0F) {
/* 2838 */       yBodyRotT = getYRot();
/*      */     }
/*      */     
/* 2841 */     ProfilerFiller profiler = Profiler.get();
/* 2842 */     profiler.push("headTurn");
/*      */     
/* 2844 */     tickHeadTurn(yBodyRotT);
/*      */     
/* 2846 */     profiler.pop();
/*      */     
/* 2848 */     profiler.push("rangeChecks");
/* 2849 */     while (getYRot() - this.yRotO < -180.0F) {
/* 2850 */       this.yRotO -= 360.0F;
/*      */     }
/* 2852 */     while (getYRot() - this.yRotO >= 180.0F) {
/* 2853 */       this.yRotO += 360.0F;
/*      */     }
/*      */     
/* 2856 */     while (this.yBodyRot - this.yBodyRotO < -180.0F) {
/* 2857 */       this.yBodyRotO -= 360.0F;
/*      */     }
/* 2859 */     while (this.yBodyRot - this.yBodyRotO >= 180.0F) {
/* 2860 */       this.yBodyRotO += 360.0F;
/*      */     }
/*      */     
/* 2863 */     while (getXRot() - this.xRotO < -180.0F) {
/* 2864 */       this.xRotO -= 360.0F;
/*      */     }
/* 2866 */     while (getXRot() - this.xRotO >= 180.0F) {
/* 2867 */       this.xRotO += 360.0F;
/*      */     }
/*      */     
/* 2870 */     while (this.yHeadRot - this.yHeadRotO < -180.0F) {
/* 2871 */       this.yHeadRotO -= 360.0F;
/*      */     }
/* 2873 */     while (this.yHeadRot - this.yHeadRotO >= 180.0F) {
/* 2874 */       this.yHeadRotO += 360.0F;
/*      */     }
/* 2876 */     profiler.pop();
/*      */     
/* 2878 */     if (isFallFlying()) {
/* 2879 */       this.fallFlyTicks++;
/*      */     } else {
/* 2881 */       this.fallFlyTicks = 0;
/*      */     } 
/*      */     
/* 2884 */     if (isSleeping()) {
/* 2885 */       setXRot(0.0F);
/*      */     }
/*      */     
/* 2888 */     refreshDirtyAttributes();
/* 2889 */     this.elytraAnimationState.tick();
/*      */   }
/*      */   
/*      */   public boolean wasRecentlyStabbed(Entity target, int allowedTime) {
/* 2893 */     if (this.recentKineticEnemies == null) {
/* 2894 */       return false;
/*      */     }
/* 2896 */     if (this.recentKineticEnemies.containsKey(target)) {
/* 2897 */       return (level().getGameTime() - this.recentKineticEnemies.getLong(target) < allowedTime);
/*      */     }
/* 2899 */     return false;
/*      */   }
/*      */   
/*      */   public void rememberStabbedEntity(Entity target) {
/* 2903 */     if (this.recentKineticEnemies != null) {
/* 2904 */       this.recentKineticEnemies.put(target, level().getGameTime());
/*      */     }
/*      */   }
/*      */   
/*      */   public int stabbedEntities(Predicate<Entity> filter) {
/* 2909 */     if (this.recentKineticEnemies == null) {
/* 2910 */       return 0;
/*      */     }
/* 2912 */     return (int)this.recentKineticEnemies.keySet().stream().filter(filter).count();
/*      */   }
/*      */   public boolean stabAttack(EquipmentSlot weaponSlot, Entity target, float baseDamage, boolean dealsDamage, boolean dealsKnockback, boolean dismounts) {
/*      */     ServerLevel serverLevel;
/* 2916 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/* 2917 */     else { return false; }
/*      */ 
/*      */     
/* 2920 */     ItemStack weaponItem = getItemBySlot(weaponSlot);
/*      */     
/* 2922 */     DamageSource damageSource = weaponItem.getDamageSource(this, () -> damageSources().mobAttack(this));
/*      */     
/* 2924 */     float postEnchantmentDamage = EnchantmentHelper.modifyDamage(serverLevel, weaponItem, target, damageSource, baseDamage);
/*      */     
/* 2926 */     Vec3 oldMovement = target.getDeltaMovement();
/*      */     
/* 2928 */     boolean affected = dealsKnockback;
/*      */     
/* 2930 */     boolean dealtDamage = (dealsDamage && target.hurtServer(serverLevel, damageSource, postEnchantmentDamage));
/*      */     
/* 2932 */     affected |= dealtDamage;
/*      */     
/* 2934 */     if (dealsKnockback)
/*      */     {
/* 2936 */       causeExtraKnockback(target, 0.4F + getKnockback(target, damageSource), oldMovement);
/*      */     }
/* 2938 */     if (dismounts && target.isPassenger()) {
/* 2939 */       affected = true;
/* 2940 */       target.stopRiding();
/*      */     } 
/*      */     
/* 2943 */     if (target instanceof LivingEntity) { LivingEntity livingTarget = (LivingEntity)target;
/* 2944 */       weaponItem.hurtEnemy(livingTarget, this); }
/*      */     
/* 2946 */     if (dealtDamage) {
/* 2947 */       EnchantmentHelper.doPostAttackEffects(serverLevel, target, damageSource);
/*      */     }
/*      */     
/* 2950 */     if (!affected) {
/* 2951 */       return false;
/*      */     }
/* 2953 */     setLastHurtMob(target);
/* 2954 */     playAttackSound();
/* 2955 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onAttack() {}
/*      */   
/*      */   private void detectEquipmentUpdates() {
/* 2962 */     Map<EquipmentSlot, ItemStack> changedItems = collectEquipmentChanges();
/*      */     
/* 2964 */     if (changedItems != null) {
/* 2965 */       handleHandSwap(changedItems);
/*      */       
/* 2967 */       if (!changedItems.isEmpty()) {
/* 2968 */         handleEquipmentChanges(changedItems);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private Map<EquipmentSlot, ItemStack> collectEquipmentChanges() {
/* 2974 */     Map<EquipmentSlot, ItemStack> changedItems = null;
/* 2975 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 2976 */       ItemStack previous = (ItemStack)this.lastEquipmentItems.get(slot);
/* 2977 */       ItemStack current = getItemBySlot(slot);
/*      */       
/* 2979 */       if (equipmentHasChanged(previous, current)) {
/* 2980 */         if (changedItems == null) {
/* 2981 */           changedItems = Maps.newEnumMap(EquipmentSlot.class);
/*      */         }
/* 2983 */         changedItems.put(slot, current);
/*      */         
/* 2985 */         AttributeMap attributes = getAttributes();
/* 2986 */         if (!previous.isEmpty()) {
/* 2987 */           stopLocationBasedEffects(previous, slot, attributes);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 2992 */     if (changedItems != null) {
/* 2993 */       for (Iterator iterator = changedItems.entrySet().iterator(); iterator.hasNext(); ) { Map.Entry<EquipmentSlot, ItemStack> entry = (Map.Entry)iterator.next();
/* 2994 */         EquipmentSlot slot = (EquipmentSlot)entry.getKey();
/* 2995 */         ItemStack current = (ItemStack)entry.getValue();
/* 2996 */         if (!current.isEmpty() && !current.isBroken()) {
/* 2997 */           current.forEachModifier(slot, (attribute, modifier) -> {
/* 2998 */                 AttributeInstance instance = this.attributes.getInstance(attribute);
/* 2999 */                 if (instance != null) {
/* 3000 */                   instance.removeModifier(modifier.id());
/* 3001 */                   instance.addTransientModifier(modifier);
/*      */                 } 
/*      */               });
/* 3004 */           Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 3005 */             EnchantmentHelper.runLocationChangedEffects(serverLevel, current, this, slot); }
/*      */         
/*      */         }  }
/*      */     
/*      */     }
/* 3010 */     return changedItems;
/*      */   }
/*      */ 
/*      */   
/* 3014 */   public boolean equipmentHasChanged(ItemStack previous, ItemStack current) { return !ItemStack.matches(current, previous); }
/*      */ 
/*      */ 
/*      */   
/*      */   private void handleHandSwap(Map<EquipmentSlot, ItemStack> changedItems) {
/* 3019 */     ItemStack currentMainHand = (ItemStack)changedItems.get(EquipmentSlot.MAINHAND);
/* 3020 */     ItemStack currentOffHand = (ItemStack)changedItems.get(EquipmentSlot.OFFHAND);
/*      */     
/* 3022 */     if (currentMainHand != null && currentOffHand != null && 
/* 3023 */       ItemStack.matches(currentMainHand, (ItemStack)this.lastEquipmentItems.get(EquipmentSlot.OFFHAND)) && 
/* 3024 */       ItemStack.matches(currentOffHand, (ItemStack)this.lastEquipmentItems.get(EquipmentSlot.MAINHAND))) {
/*      */       
/* 3026 */       ((ServerLevel)level()).getChunkSource().sendToTrackingPlayers(this, new ClientboundEntityEventPacket(this, (byte)55));
/* 3027 */       changedItems.remove(EquipmentSlot.MAINHAND);
/* 3028 */       changedItems.remove(EquipmentSlot.OFFHAND);
/* 3029 */       this.lastEquipmentItems.put(EquipmentSlot.MAINHAND, currentMainHand.copy());
/* 3030 */       this.lastEquipmentItems.put(EquipmentSlot.OFFHAND, currentOffHand.copy());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void handleEquipmentChanges(Map<EquipmentSlot, ItemStack> changedItems) {
/* 3035 */     List<Pair<EquipmentSlot, ItemStack>> itemsToSend = Lists.newArrayListWithCapacity(changedItems.size());
/* 3036 */     changedItems.forEach((slot, newItem) -> {
/*      */           
/* 3038 */           ItemStack newItemToStore = newItem.copy();
/* 3039 */           itemsToSend.add(Pair.of(slot, newItemToStore));
/* 3040 */           this.lastEquipmentItems.put(slot, newItemToStore);
/*      */         });
/* 3042 */     ((ServerLevel)level()).getChunkSource().sendToTrackingPlayers(this, new ClientboundSetEquipmentPacket(getId(), itemsToSend));
/*      */   }
/*      */   
/*      */   protected void tickHeadTurn(float yBodyRotT) {
/* 3046 */     float yBodyRotD = Mth.wrapDegrees(yBodyRotT - this.yBodyRot);
/* 3047 */     this.yBodyRot += yBodyRotD * 0.3F;
/*      */     
/* 3049 */     float headDiff = Mth.wrapDegrees(getYRot() - this.yBodyRot);
/*      */     
/* 3051 */     float maxHeadRotation = getMaxHeadRotationRelativeToBody();
/* 3052 */     if (Math.abs(headDiff) > maxHeadRotation) {
/* 3053 */       this.yBodyRot += headDiff - Mth.sign(headDiff) * maxHeadRotation;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 3058 */   protected float getMaxHeadRotationRelativeToBody() { return 50.0F; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void aiStep() { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield noJumpDelay : I
/*      */     //   4: ifle -> 17
/*      */     //   7: aload_0
/*      */     //   8: dup
/*      */     //   9: getfield noJumpDelay : I
/*      */     //   12: iconst_1
/*      */     //   13: isub
/*      */     //   14: putfield noJumpDelay : I
/*      */     //   17: aload_0
/*      */     //   18: invokevirtual isInterpolating : ()Z
/*      */     //   21: ifeq -> 34
/*      */     //   24: aload_0
/*      */     //   25: invokevirtual getInterpolation : ()Lnet/minecraft/world/entity/InterpolationHandler;
/*      */     //   28: invokevirtual interpolate : ()V
/*      */     //   31: goto -> 55
/*      */     //   34: aload_0
/*      */     //   35: invokevirtual canSimulateMovement : ()Z
/*      */     //   38: ifne -> 55
/*      */     //   41: aload_0
/*      */     //   42: aload_0
/*      */     //   43: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*      */     //   46: ldc2_w 0.98
/*      */     //   49: invokevirtual scale : (D)Lnet/minecraft/world/phys/Vec3;
/*      */     //   52: invokevirtual setDeltaMovement : (Lnet/minecraft/world/phys/Vec3;)V
/*      */     //   55: aload_0
/*      */     //   56: getfield lerpHeadSteps : I
/*      */     //   59: ifle -> 84
/*      */     //   62: aload_0
/*      */     //   63: aload_0
/*      */     //   64: getfield lerpHeadSteps : I
/*      */     //   67: aload_0
/*      */     //   68: getfield lerpYHeadRot : D
/*      */     //   71: invokevirtual lerpHeadRotationStep : (ID)V
/*      */     //   74: aload_0
/*      */     //   75: dup
/*      */     //   76: getfield lerpHeadSteps : I
/*      */     //   79: iconst_1
/*      */     //   80: isub
/*      */     //   81: putfield lerpHeadSteps : I
/*      */     //   84: aload_0
/*      */     //   85: getfield equipment : Lnet/minecraft/world/entity/EntityEquipment;
/*      */     //   88: aload_0
/*      */     //   89: invokevirtual tick : (Lnet/minecraft/world/entity/Entity;)V
/*      */     //   92: aload_0
/*      */     //   93: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*      */     //   96: astore_1
/*      */     //   97: aload_1
/*      */     //   98: getfield x : D
/*      */     //   101: dstore_2
/*      */     //   102: aload_1
/*      */     //   103: getfield y : D
/*      */     //   106: dstore #4
/*      */     //   108: aload_1
/*      */     //   109: getfield z : D
/*      */     //   112: dstore #6
/*      */     //   114: aload_0
/*      */     //   115: invokevirtual getType : ()Lnet/minecraft/world/entity/EntityType;
/*      */     //   118: getstatic net/minecraft/world/entity/EntityType.PLAYER : Lnet/minecraft/world/entity/EntityType;
/*      */     //   121: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */     //   124: ifeq -> 146
/*      */     //   127: aload_1
/*      */     //   128: invokevirtual horizontalDistanceSqr : ()D
/*      */     //   131: ldc2_w 9.0E-6
/*      */     //   134: dcmpg
/*      */     //   135: ifge -> 179
/*      */     //   138: dconst_0
/*      */     //   139: dstore_2
/*      */     //   140: dconst_0
/*      */     //   141: dstore #6
/*      */     //   143: goto -> 179
/*      */     //   146: aload_1
/*      */     //   147: getfield x : D
/*      */     //   150: invokestatic abs : (D)D
/*      */     //   153: ldc2_w 0.003
/*      */     //   156: dcmpg
/*      */     //   157: ifge -> 162
/*      */     //   160: dconst_0
/*      */     //   161: dstore_2
/*      */     //   162: aload_1
/*      */     //   163: getfield z : D
/*      */     //   166: invokestatic abs : (D)D
/*      */     //   169: ldc2_w 0.003
/*      */     //   172: dcmpg
/*      */     //   173: ifge -> 179
/*      */     //   176: dconst_0
/*      */     //   177: dstore #6
/*      */     //   179: aload_1
/*      */     //   180: getfield y : D
/*      */     //   183: invokestatic abs : (D)D
/*      */     //   186: ldc2_w 0.003
/*      */     //   189: dcmpg
/*      */     //   190: ifge -> 196
/*      */     //   193: dconst_0
/*      */     //   194: dstore #4
/*      */     //   196: aload_0
/*      */     //   197: dload_2
/*      */     //   198: dload #4
/*      */     //   200: dload #6
/*      */     //   202: invokevirtual setDeltaMovement : (DDD)V
/*      */     //   205: invokestatic get : ()Lnet/minecraft/util/profiling/ProfilerFiller;
/*      */     //   208: astore #8
/*      */     //   210: aload #8
/*      */     //   212: ldc_w 'ai'
/*      */     //   215: invokeinterface push : (Ljava/lang/String;)V
/*      */     //   220: aload_0
/*      */     //   221: invokevirtual applyInput : ()V
/*      */     //   224: aload_0
/*      */     //   225: invokevirtual isImmobile : ()Z
/*      */     //   228: ifeq -> 249
/*      */     //   231: aload_0
/*      */     //   232: iconst_0
/*      */     //   233: putfield jumping : Z
/*      */     //   236: aload_0
/*      */     //   237: fconst_0
/*      */     //   238: putfield xxa : F
/*      */     //   241: aload_0
/*      */     //   242: fconst_0
/*      */     //   243: putfield zza : F
/*      */     //   246: goto -> 287
/*      */     //   249: aload_0
/*      */     //   250: invokevirtual isEffectiveAi : ()Z
/*      */     //   253: ifeq -> 287
/*      */     //   256: aload_0
/*      */     //   257: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   260: invokevirtual isClientSide : ()Z
/*      */     //   263: ifne -> 287
/*      */     //   266: aload #8
/*      */     //   268: ldc_w 'newAi'
/*      */     //   271: invokeinterface push : (Ljava/lang/String;)V
/*      */     //   276: aload_0
/*      */     //   277: invokevirtual serverAiStep : ()V
/*      */     //   280: aload #8
/*      */     //   282: invokeinterface pop : ()V
/*      */     //   287: aload #8
/*      */     //   289: invokeinterface pop : ()V
/*      */     //   294: aload #8
/*      */     //   296: ldc_w 'jump'
/*      */     //   299: invokeinterface push : (Ljava/lang/String;)V
/*      */     //   304: aload_0
/*      */     //   305: getfield jumping : Z
/*      */     //   308: ifeq -> 475
/*      */     //   311: aload_0
/*      */     //   312: invokevirtual isAffectedByFluids : ()Z
/*      */     //   315: ifeq -> 475
/*      */     //   318: aload_0
/*      */     //   319: invokevirtual isInLava : ()Z
/*      */     //   322: ifeq -> 337
/*      */     //   325: aload_0
/*      */     //   326: getstatic net/minecraft/tags/FluidTags.LAVA : Lnet/minecraft/tags/TagKey;
/*      */     //   329: invokevirtual getFluidHeight : (Lnet/minecraft/tags/TagKey;)D
/*      */     //   332: dstore #9
/*      */     //   334: goto -> 346
/*      */     //   337: aload_0
/*      */     //   338: getstatic net/minecraft/tags/FluidTags.WATER : Lnet/minecraft/tags/TagKey;
/*      */     //   341: invokevirtual getFluidHeight : (Lnet/minecraft/tags/TagKey;)D
/*      */     //   344: dstore #9
/*      */     //   346: aload_0
/*      */     //   347: invokevirtual isInWater : ()Z
/*      */     //   350: ifeq -> 364
/*      */     //   353: dload #9
/*      */     //   355: dconst_0
/*      */     //   356: dcmpl
/*      */     //   357: ifle -> 364
/*      */     //   360: iconst_1
/*      */     //   361: goto -> 365
/*      */     //   364: iconst_0
/*      */     //   365: istore #11
/*      */     //   367: aload_0
/*      */     //   368: invokevirtual getFluidJumpThreshold : ()D
/*      */     //   371: dstore #12
/*      */     //   373: iload #11
/*      */     //   375: ifeq -> 403
/*      */     //   378: aload_0
/*      */     //   379: invokevirtual onGround : ()Z
/*      */     //   382: ifeq -> 393
/*      */     //   385: dload #9
/*      */     //   387: dload #12
/*      */     //   389: dcmpl
/*      */     //   390: ifle -> 403
/*      */     //   393: aload_0
/*      */     //   394: getstatic net/minecraft/tags/FluidTags.WATER : Lnet/minecraft/tags/TagKey;
/*      */     //   397: invokevirtual jumpInLiquid : (Lnet/minecraft/tags/TagKey;)V
/*      */     //   400: goto -> 472
/*      */     //   403: aload_0
/*      */     //   404: invokevirtual isInLava : ()Z
/*      */     //   407: ifeq -> 435
/*      */     //   410: aload_0
/*      */     //   411: invokevirtual onGround : ()Z
/*      */     //   414: ifeq -> 425
/*      */     //   417: dload #9
/*      */     //   419: dload #12
/*      */     //   421: dcmpl
/*      */     //   422: ifle -> 435
/*      */     //   425: aload_0
/*      */     //   426: getstatic net/minecraft/tags/FluidTags.LAVA : Lnet/minecraft/tags/TagKey;
/*      */     //   429: invokevirtual jumpInLiquid : (Lnet/minecraft/tags/TagKey;)V
/*      */     //   432: goto -> 472
/*      */     //   435: aload_0
/*      */     //   436: invokevirtual onGround : ()Z
/*      */     //   439: ifne -> 455
/*      */     //   442: iload #11
/*      */     //   444: ifeq -> 472
/*      */     //   447: dload #9
/*      */     //   449: dload #12
/*      */     //   451: dcmpg
/*      */     //   452: ifgt -> 472
/*      */     //   455: aload_0
/*      */     //   456: getfield noJumpDelay : I
/*      */     //   459: ifne -> 472
/*      */     //   462: aload_0
/*      */     //   463: invokevirtual jumpFromGround : ()V
/*      */     //   466: aload_0
/*      */     //   467: bipush #10
/*      */     //   469: putfield noJumpDelay : I
/*      */     //   472: goto -> 480
/*      */     //   475: aload_0
/*      */     //   476: iconst_0
/*      */     //   477: putfield noJumpDelay : I
/*      */     //   480: aload #8
/*      */     //   482: invokeinterface pop : ()V
/*      */     //   487: aload #8
/*      */     //   489: ldc_w 'travel'
/*      */     //   492: invokeinterface push : (Ljava/lang/String;)V
/*      */     //   497: aload_0
/*      */     //   498: invokevirtual isFallFlying : ()Z
/*      */     //   501: ifeq -> 508
/*      */     //   504: aload_0
/*      */     //   505: invokevirtual updateFallFlying : ()V
/*      */     //   508: aload_0
/*      */     //   509: invokevirtual getBoundingBox : ()Lnet/minecraft/world/phys/AABB;
/*      */     //   512: astore #9
/*      */     //   514: new net/minecraft/world/phys/Vec3
/*      */     //   517: dup
/*      */     //   518: aload_0
/*      */     //   519: getfield xxa : F
/*      */     //   522: f2d
/*      */     //   523: aload_0
/*      */     //   524: getfield yya : F
/*      */     //   527: f2d
/*      */     //   528: aload_0
/*      */     //   529: getfield zza : F
/*      */     //   532: f2d
/*      */     //   533: invokespecial <init> : (DDD)V
/*      */     //   536: astore #10
/*      */     //   538: aload_0
/*      */     //   539: getstatic net/minecraft/world/effect/MobEffects.SLOW_FALLING : Lnet/minecraft/core/Holder;
/*      */     //   542: invokevirtual hasEffect : (Lnet/minecraft/core/Holder;)Z
/*      */     //   545: ifne -> 558
/*      */     //   548: aload_0
/*      */     //   549: getstatic net/minecraft/world/effect/MobEffects.LEVITATION : Lnet/minecraft/core/Holder;
/*      */     //   552: invokevirtual hasEffect : (Lnet/minecraft/core/Holder;)Z
/*      */     //   555: ifeq -> 562
/*      */     //   558: aload_0
/*      */     //   559: invokevirtual resetFallDistance : ()V
/*      */     //   562: aload_0
/*      */     //   563: invokevirtual getControllingPassenger : ()Lnet/minecraft/world/entity/LivingEntity;
/*      */     //   566: astore #12
/*      */     //   568: aload #12
/*      */     //   570: instanceof net/minecraft/world/entity/player/Player
/*      */     //   573: ifeq -> 601
/*      */     //   576: aload #12
/*      */     //   578: checkcast net/minecraft/world/entity/player/Player
/*      */     //   581: astore #11
/*      */     //   583: aload_0
/*      */     //   584: invokevirtual isAlive : ()Z
/*      */     //   587: ifeq -> 601
/*      */     //   590: aload_0
/*      */     //   591: aload #11
/*      */     //   593: aload #10
/*      */     //   595: invokevirtual travelRidden : (Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/phys/Vec3;)V
/*      */     //   598: goto -> 621
/*      */     //   601: aload_0
/*      */     //   602: invokevirtual canSimulateMovement : ()Z
/*      */     //   605: ifeq -> 621
/*      */     //   608: aload_0
/*      */     //   609: invokevirtual isEffectiveAi : ()Z
/*      */     //   612: ifeq -> 621
/*      */     //   615: aload_0
/*      */     //   616: aload #10
/*      */     //   618: invokevirtual travel : (Lnet/minecraft/world/phys/Vec3;)V
/*      */     //   621: aload_0
/*      */     //   622: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   625: invokevirtual isClientSide : ()Z
/*      */     //   628: ifeq -> 638
/*      */     //   631: aload_0
/*      */     //   632: invokevirtual isLocalInstanceAuthoritative : ()Z
/*      */     //   635: ifeq -> 642
/*      */     //   638: aload_0
/*      */     //   639: invokevirtual applyEffectsFromBlocks : ()V
/*      */     //   642: aload_0
/*      */     //   643: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   646: invokevirtual isClientSide : ()Z
/*      */     //   649: ifeq -> 660
/*      */     //   652: aload_0
/*      */     //   653: aload_0
/*      */     //   654: instanceof net/minecraft/world/entity/animal/FlyingAnimal
/*      */     //   657: invokevirtual calculateEntityAnimation : (Z)V
/*      */     //   660: aload #8
/*      */     //   662: invokeinterface pop : ()V
/*      */     //   667: aload_0
/*      */     //   668: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   671: astore #12
/*      */     //   673: aload #12
/*      */     //   675: instanceof net/minecraft/server/level/ServerLevel
/*      */     //   678: ifeq -> 780
/*      */     //   681: aload #12
/*      */     //   683: checkcast net/minecraft/server/level/ServerLevel
/*      */     //   686: astore #11
/*      */     //   688: aload #8
/*      */     //   690: ldc_w 'freezing'
/*      */     //   693: invokeinterface push : (Ljava/lang/String;)V
/*      */     //   698: aload_0
/*      */     //   699: getfield isInPowderSnow : Z
/*      */     //   702: ifeq -> 712
/*      */     //   705: aload_0
/*      */     //   706: invokevirtual canFreeze : ()Z
/*      */     //   709: ifne -> 726
/*      */     //   712: aload_0
/*      */     //   713: iconst_0
/*      */     //   714: aload_0
/*      */     //   715: invokevirtual getTicksFrozen : ()I
/*      */     //   718: iconst_2
/*      */     //   719: isub
/*      */     //   720: invokestatic max : (II)I
/*      */     //   723: invokevirtual setTicksFrozen : (I)V
/*      */     //   726: aload_0
/*      */     //   727: invokevirtual removeFrost : ()V
/*      */     //   730: aload_0
/*      */     //   731: invokevirtual tryAddFrost : ()V
/*      */     //   734: aload_0
/*      */     //   735: getfield tickCount : I
/*      */     //   738: bipush #40
/*      */     //   740: irem
/*      */     //   741: ifne -> 773
/*      */     //   744: aload_0
/*      */     //   745: invokevirtual isFullyFrozen : ()Z
/*      */     //   748: ifeq -> 773
/*      */     //   751: aload_0
/*      */     //   752: invokevirtual canFreeze : ()Z
/*      */     //   755: ifeq -> 773
/*      */     //   758: aload_0
/*      */     //   759: aload #11
/*      */     //   761: aload_0
/*      */     //   762: invokevirtual damageSources : ()Lnet/minecraft/world/damagesource/DamageSources;
/*      */     //   765: invokevirtual freeze : ()Lnet/minecraft/world/damagesource/DamageSource;
/*      */     //   768: fconst_1
/*      */     //   769: invokevirtual hurtServer : (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z
/*      */     //   772: pop
/*      */     //   773: aload #8
/*      */     //   775: invokeinterface pop : ()V
/*      */     //   780: aload #8
/*      */     //   782: ldc_w 'push'
/*      */     //   785: invokeinterface push : (Ljava/lang/String;)V
/*      */     //   790: aload_0
/*      */     //   791: getfield autoSpinAttackTicks : I
/*      */     //   794: ifle -> 817
/*      */     //   797: aload_0
/*      */     //   798: dup
/*      */     //   799: getfield autoSpinAttackTicks : I
/*      */     //   802: iconst_1
/*      */     //   803: isub
/*      */     //   804: putfield autoSpinAttackTicks : I
/*      */     //   807: aload_0
/*      */     //   808: aload #9
/*      */     //   810: aload_0
/*      */     //   811: invokevirtual getBoundingBox : ()Lnet/minecraft/world/phys/AABB;
/*      */     //   814: invokevirtual checkAutoSpinAttack : (Lnet/minecraft/world/phys/AABB;Lnet/minecraft/world/phys/AABB;)V
/*      */     //   817: aload_0
/*      */     //   818: invokevirtual pushEntities : ()V
/*      */     //   821: aload #8
/*      */     //   823: invokeinterface pop : ()V
/*      */     //   828: aload_0
/*      */     //   829: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   832: astore #12
/*      */     //   834: aload #12
/*      */     //   836: instanceof net/minecraft/server/level/ServerLevel
/*      */     //   839: ifeq -> 878
/*      */     //   842: aload #12
/*      */     //   844: checkcast net/minecraft/server/level/ServerLevel
/*      */     //   847: astore #11
/*      */     //   849: aload_0
/*      */     //   850: invokevirtual isSensitiveToWater : ()Z
/*      */     //   853: ifeq -> 878
/*      */     //   856: aload_0
/*      */     //   857: invokevirtual isInWaterOrRain : ()Z
/*      */     //   860: ifeq -> 878
/*      */     //   863: aload_0
/*      */     //   864: aload #11
/*      */     //   866: aload_0
/*      */     //   867: invokevirtual damageSources : ()Lnet/minecraft/world/damagesource/DamageSources;
/*      */     //   870: invokevirtual drown : ()Lnet/minecraft/world/damagesource/DamageSource;
/*      */     //   873: fconst_1
/*      */     //   874: invokevirtual hurtServer : (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z
/*      */     //   877: pop
/*      */     //   878: return
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #3062	-> 0
/*      */     //   #3063	-> 7
/*      */     //   #3065	-> 17
/*      */     //   #3066	-> 24
/*      */     //   #3067	-> 34
/*      */     //   #3069	-> 41
/*      */     //   #3071	-> 55
/*      */     //   #3072	-> 62
/*      */     //   #3073	-> 74
/*      */     //   #3075	-> 84
/*      */     //   #3077	-> 92
/*      */     //   #3078	-> 97
/*      */     //   #3079	-> 102
/*      */     //   #3080	-> 108
/*      */     //   #3081	-> 114
/*      */     //   #3082	-> 127
/*      */     //   #3083	-> 138
/*      */     //   #3084	-> 140
/*      */     //   #3089	-> 146
/*      */     //   #3090	-> 160
/*      */     //   #3092	-> 162
/*      */     //   #3093	-> 176
/*      */     //   #3096	-> 179
/*      */     //   #3097	-> 193
/*      */     //   #3099	-> 196
/*      */     //   #3101	-> 205
/*      */     //   #3102	-> 210
/*      */     //   #3103	-> 220
/*      */     //   #3104	-> 224
/*      */     //   #3105	-> 231
/*      */     //   #3106	-> 236
/*      */     //   #3107	-> 241
/*      */     //   #3108	-> 249
/*      */     //   #3109	-> 266
/*      */     //   #3110	-> 276
/*      */     //   #3111	-> 280
/*      */     //   #3114	-> 287
/*      */     //   #3116	-> 294
/*      */     //   #3117	-> 304
/*      */     //   #3121	-> 318
/*      */     //   #3122	-> 325
/*      */     //   #3124	-> 337
/*      */     //   #3126	-> 346
/*      */     //   #3127	-> 367
/*      */     //   #3128	-> 373
/*      */     //   #3129	-> 393
/*      */     //   #3130	-> 403
/*      */     //   #3131	-> 425
/*      */     //   #3132	-> 435
/*      */     //   #3133	-> 455
/*      */     //   #3134	-> 462
/*      */     //   #3135	-> 466
/*      */     //   #3138	-> 472
/*      */     //   #3139	-> 475
/*      */     //   #3141	-> 480
/*      */     //   #3143	-> 487
/*      */     //   #3145	-> 497
/*      */     //   #3146	-> 504
/*      */     //   #3148	-> 508
/*      */     //   #3149	-> 514
/*      */     //   #3150	-> 538
/*      */     //   #3151	-> 558
/*      */     //   #3153	-> 562
/*      */     //   #3154	-> 590
/*      */     //   #3155	-> 601
/*      */     //   #3156	-> 615
/*      */     //   #3158	-> 621
/*      */     //   #3159	-> 638
/*      */     //   #3162	-> 642
/*      */     //   #3163	-> 652
/*      */     //   #3166	-> 660
/*      */     //   #3168	-> 667
/*      */     //   #3169	-> 688
/*      */     //   #3171	-> 698
/*      */     //   #3172	-> 712
/*      */     //   #3174	-> 726
/*      */     //   #3175	-> 730
/*      */     //   #3177	-> 734
/*      */     //   #3178	-> 758
/*      */     //   #3180	-> 773
/*      */     //   #3183	-> 780
/*      */     //   #3184	-> 790
/*      */     //   #3185	-> 797
/*      */     //   #3186	-> 807
/*      */     //   #3188	-> 817
/*      */     //   #3189	-> 821
/*      */     //   #3192	-> 828
/*      */     //   #3193	-> 863
/*      */     //   #3195	-> 878
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   334	3	9	fluidHeight	D
/*      */     //   346	126	9	fluidHeight	D
/*      */     //   367	105	11	inWaterAndHasFluidHeight	Z
/*      */     //   373	99	12	fluidJumpThreshold	D
/*      */     //   583	18	11	controller	Lnet/minecraft/world/entity/player/Player;
/*      */     //   688	92	11	serverLevel	Lnet/minecraft/server/level/ServerLevel;
/*      */     //   849	29	11	serverLevel	Lnet/minecraft/server/level/ServerLevel;
/*      */     //   0	879	0	this	Lnet/minecraft/world/entity/LivingEntity;
/*      */     //   97	782	1	movement	Lnet/minecraft/world/phys/Vec3;
/*      */     //   102	777	2	dx	D
/*      */     //   108	771	4	dy	D
/*      */     //   114	765	6	dz	D
/*      */     //   210	669	8	profiler	Lnet/minecraft/util/profiling/ProfilerFiller;
/*      */     //   514	365	9	beforeTravelBox	Lnet/minecraft/world/phys/AABB;
/*      */     //   538	341	10	input	Lnet/minecraft/world/phys/Vec3; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void applyInput() {
/* 3198 */     this.xxa *= 0.98F;
/* 3199 */     this.zza *= 0.98F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3206 */   public boolean isSensitiveToWater() { return false; }
/*      */ 
/*      */ 
/*      */   
/* 3210 */   public boolean isJumping() { return this.jumping; }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void updateFallFlying() {
/* 3215 */     checkFallDistanceAccumulation();
/*      */     
/* 3217 */     if (!level().isClientSide()) {
/* 3218 */       if (!canGlide()) {
/* 3219 */         setSharedFlag(7, false);
/*      */         
/*      */         return;
/*      */       } 
/* 3223 */       int checkFallFlyTicks = this.fallFlyTicks + 1;
/* 3224 */       if (checkFallFlyTicks % 10 == 0) {
/* 3225 */         int freeFallInterval = checkFallFlyTicks / 10;
/*      */         
/* 3227 */         if (freeFallInterval % 2 == 0) {
/*      */ 
/*      */           
/* 3230 */           List<EquipmentSlot> slotsWithGliders = EquipmentSlot.VALUES.stream().filter(slot -> canGlideUsing(getItemBySlot(slot), slot)).toList();
/* 3231 */           EquipmentSlot slotToDamage = (EquipmentSlot)Util.getRandom(slotsWithGliders, this.random);
/*      */           
/* 3233 */           getItemBySlot(slotToDamage).hurtAndBreak(1, this, slotToDamage);
/*      */         } 
/*      */         
/* 3236 */         gameEvent(GameEvent.ELYTRA_GLIDE);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected boolean canGlide() {
/* 3242 */     if (onGround() || isPassenger() || hasEffect(MobEffects.LEVITATION)) {
/* 3243 */       return false;
/*      */     }
/* 3245 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 3246 */       if (canGlideUsing(getItemBySlot(slot), slot)) {
/* 3247 */         return true;
/*      */       }
/*      */     } 
/* 3250 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void serverAiStep() {}
/*      */   
/*      */   protected void pushEntities() {
/* 3257 */     List<Entity> pushableEntities = level().getPushableEntities(this, getBoundingBox());
/* 3258 */     if (pushableEntities.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/* 3262 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 3263 */       int maxCramming = ((Integer)serverLevel.getGameRules().get(GameRules.MAX_ENTITY_CRAMMING)).intValue();
/* 3264 */       if (maxCramming > 0 && pushableEntities.size() > maxCramming - 1 && this.random.nextInt(4) == 0) {
/* 3265 */         int count = 0;
/* 3266 */         for (Entity entity : pushableEntities) {
/* 3267 */           if (!entity.isPassenger()) {
/* 3268 */             count++;
/*      */           }
/*      */         } 
/* 3271 */         if (count > maxCramming - 1) {
/* 3272 */           hurtServer(serverLevel, damageSources().cramming(), 6.0F);
/*      */         }
/*      */       }  }
/*      */ 
/*      */     
/* 3277 */     for (Entity entity : pushableEntities) {
/* 3278 */       doPush(entity);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void checkAutoSpinAttack(AABB old, AABB current) {
/* 3283 */     AABB minmax = old.minmax(current);
/* 3284 */     List<Entity> entities = level().getEntities(this, minmax);
/* 3285 */     if (!entities.isEmpty()) {
/* 3286 */       for (Entity entity : entities) {
/* 3287 */         if (entity instanceof LivingEntity) {
/* 3288 */           doAutoAttackOnTouch((LivingEntity)entity);
/* 3289 */           this.autoSpinAttackTicks = 0;
/* 3290 */           setDeltaMovement(getDeltaMovement().scale(-0.2D));
/*      */           break;
/*      */         } 
/*      */       } 
/* 3294 */     } else if (this.horizontalCollision) {
/* 3295 */       this.autoSpinAttackTicks = 0;
/*      */     } 
/* 3297 */     if (!level().isClientSide() && this.autoSpinAttackTicks <= 0) {
/* 3298 */       setLivingEntityFlag(4, false);
/* 3299 */       this.autoSpinAttackDmg = 0.0F;
/* 3300 */       this.autoSpinAttackItemStack = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 3305 */   protected void doPush(Entity entity) { entity.push(this); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void doAutoAttackOnTouch(LivingEntity entity) {}
/*      */ 
/*      */   
/* 3312 */   public boolean isAutoSpinAttack() { return ((((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS)).byteValue() & 0x4) != 0); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void stopRiding() {
/* 3317 */     Entity oldVehicle = getVehicle();
/* 3318 */     super.stopRiding();
/* 3319 */     if (oldVehicle != null && oldVehicle != getVehicle() && !level().isClientSide()) {
/* 3320 */       dismountVehicle(oldVehicle);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void rideTick() {
/* 3326 */     super.rideTick();
/* 3327 */     resetFallDistance();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 3332 */   public InterpolationHandler getInterpolation() { return this.interpolation; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void lerpHeadTo(float yRot, int steps) {
/* 3337 */     this.lerpYHeadRot = yRot;
/* 3338 */     this.lerpHeadSteps = steps;
/*      */   }
/*      */ 
/*      */   
/* 3342 */   public void setJumping(boolean jump) { this.jumping = jump; }
/*      */ 
/*      */   
/*      */   public void onItemPickup(ItemEntity entity) {
/* 3346 */     Entity thrower = entity.getOwner();
/* 3347 */     if (thrower instanceof ServerPlayer) {
/* 3348 */       CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_ENTITY.trigger((ServerPlayer)thrower, entity.getItem(), this);
/*      */     }
/*      */   }
/*      */   
/*      */   public void take(Entity entity, int orgCount) {
/* 3353 */     if (!entity.isRemoved() && !level().isClientSide() && (
/* 3354 */       entity instanceof ItemEntity || entity instanceof AbstractArrow || entity instanceof ExperienceOrb)) {
/* 3355 */       ((ServerLevel)level()).getChunkSource().sendToTrackingPlayers(entity, new ClientboundTakeItemEntityPacket(entity.getId(), getId(), orgCount));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 3361 */   public boolean hasLineOfSight(Entity target) { return hasLineOfSight(target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, target.getEyeY()); }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasLineOfSight(Entity target, ClipContext.Block blockCollidingContext, ClipContext.Fluid fluidCollidingContext, double eyeHeight) {
/* 3366 */     if (target.level() != level()) {
/* 3367 */       return false;
/*      */     }
/* 3369 */     Vec3 from = new Vec3(getX(), getEyeY(), getZ());
/* 3370 */     Vec3 to = new Vec3(target.getX(), eyeHeight, target.getZ());
/*      */     
/* 3372 */     if (to.distanceTo(from) > 128.0D) {
/* 3373 */       return false;
/*      */     }
/* 3375 */     return (level().clip(new ClipContext(from, to, blockCollidingContext, fluidCollidingContext, this)).getType() == HitResult.Type.MISS);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getViewYRot(float a) {
/* 3380 */     if (a == 1.0F) {
/* 3381 */       return this.yHeadRot;
/*      */     }
/* 3383 */     return Mth.rotLerp(a, this.yHeadRotO, this.yHeadRot);
/*      */   }
/*      */   
/*      */   public float getAttackAnim(float a) {
/* 3387 */     float diff = this.attackAnim - this.oAttackAnim;
/* 3388 */     if (diff < 0.0F) {
/* 3389 */       diff++;
/*      */     }
/* 3391 */     return this.oAttackAnim + diff * a;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 3396 */   public boolean isPickable() { return !isRemoved(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3401 */   public boolean isPushable() { return (isAlive() && !isSpectator() && !onClimbable()); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3406 */   public float getYHeadRot() { return this.yHeadRot; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3411 */   public void setYHeadRot(float yHeadRot) { this.yHeadRot = yHeadRot; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3416 */   public void setYBodyRot(float yBodyRot) { this.yBodyRot = yBodyRot; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3421 */   public Vec3 getRelativePortalPosition(Direction.Axis axis, BlockUtil.FoundRectangle portalArea) { return resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(axis, portalArea)); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3426 */   public static Vec3 resetForwardDirectionOfRelativePortalPosition(Vec3 offsets) { return new Vec3(offsets.x, offsets.y, 0.0D); }
/*      */ 
/*      */ 
/*      */   
/* 3430 */   public float getAbsorptionAmount() { return this.absorptionAmount; }
/*      */ 
/*      */ 
/*      */   
/* 3434 */   public final void setAbsorptionAmount(float absorptionAmount) { internalSetAbsorptionAmount(Mth.clamp(absorptionAmount, 0.0F, getMaxAbsorption())); }
/*      */ 
/*      */ 
/*      */   
/* 3438 */   protected void internalSetAbsorptionAmount(float absorptionAmount) { this.absorptionAmount = absorptionAmount; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onEnterCombat() {}
/*      */ 
/*      */   
/*      */   public void onLeaveCombat() {}
/*      */ 
/*      */   
/* 3448 */   protected void updateEffectVisibility() { this.effectsDirty = true; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3454 */   public boolean isUsingItem() { return ((((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS)).byteValue() & true) > 0); }
/*      */ 
/*      */ 
/*      */   
/* 3458 */   public InteractionHand getUsedItemHand() { return ((((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS)).byteValue() & 0x2) > 0) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND; }
/*      */ 
/*      */   
/*      */   private void updatingUsingItem() {
/* 3462 */     if (isUsingItem())
/*      */     {
/* 3464 */       if (ItemStack.isSameItem(getItemInHand(getUsedItemHand()), this.useItem)) {
/* 3465 */         this.useItem = getItemInHand(getUsedItemHand());
/* 3466 */         updateUsingItem(this.useItem);
/*      */       } else {
/* 3468 */         stopUsingItem();
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private ItemEntity createItemStackToDrop(ItemStack itemStack, boolean randomly, boolean thrownFromHand) {
/* 3474 */     if (itemStack.isEmpty()) {
/* 3475 */       return null;
/*      */     }
/* 3477 */     double yHandPos = getEyeY() - 0.30000001192092896D;
/* 3478 */     ItemEntity entity = new ItemEntity(level(), getX(), yHandPos, getZ(), itemStack);
/* 3479 */     entity.setPickUpDelay(40);
/*      */     
/* 3481 */     if (thrownFromHand) {
/* 3482 */       entity.setThrower(this);
/*      */     }
/*      */     
/* 3485 */     if (randomly) {
/* 3486 */       float pow = this.random.nextFloat() * 0.5F;
/* 3487 */       float dir = this.random.nextFloat() * 6.2831855F;
/* 3488 */       entity.setDeltaMovement((
/* 3489 */           -Mth.sin(dir) * pow), 0.20000000298023224D, (
/*      */           
/* 3491 */           Mth.cos(dir) * pow));
/*      */     } else {
/*      */       
/* 3494 */       float pow = 0.3F;
/* 3495 */       float sinX = Mth.sin((getXRot() * 0.017453292F));
/* 3496 */       float cosX = Mth.cos((getXRot() * 0.017453292F));
/* 3497 */       float sinY = Mth.sin((getYRot() * 0.017453292F));
/* 3498 */       float cosY = Mth.cos((getYRot() * 0.017453292F));
/*      */       
/* 3500 */       float dir = this.random.nextFloat() * 6.2831855F;
/* 3501 */       float pow2 = 0.02F * this.random.nextFloat();
/*      */       
/* 3503 */       entity.setDeltaMovement((-sinY * cosX * 0.3F) + 
/* 3504 */           Math.cos(dir) * pow2, (-sinX * 0.3F + 0.1F + (this.random
/* 3505 */           .nextFloat() - this.random.nextFloat()) * 0.1F), (cosY * cosX * 0.3F) + 
/* 3506 */           Math.sin(dir) * pow2);
/*      */     } 
/*      */     
/* 3509 */     return entity;
/*      */   }
/*      */   
/*      */   protected void updateUsingItem(ItemStack useItem) {
/* 3513 */     useItem.onUseTick(level(), this, getUseItemRemainingTicks());
/* 3514 */     if (--this.useItemRemaining == 0 && !level().isClientSide() && !useItem.useOnRelease()) {
/* 3515 */       completeUsingItem();
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateSwimAmount() {
/* 3520 */     this.swimAmountO = this.swimAmount;
/* 3521 */     if (isVisuallySwimming()) {
/* 3522 */       this.swimAmount = Math.min(1.0F, this.swimAmount + 0.09F);
/*      */     } else {
/* 3524 */       this.swimAmount = Math.max(0.0F, this.swimAmount - 0.09F);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void setLivingEntityFlag(int flag, boolean value) {
/* 3529 */     int currentFlags = ((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS)).byteValue();
/* 3530 */     if (value) {
/* 3531 */       currentFlags |= flag;
/*      */     } else {
/* 3533 */       currentFlags &= (flag ^ 0xFFFFFFFF);
/*      */     } 
/* 3535 */     this.entityData.set(DATA_LIVING_ENTITY_FLAGS, Byte.valueOf((byte)currentFlags));
/*      */   }
/*      */   
/*      */   public void startUsingItem(InteractionHand hand) {
/* 3539 */     ItemStack itemStack = getItemInHand(hand);
/* 3540 */     if (itemStack.isEmpty() || isUsingItem()) {
/*      */       return;
/*      */     }
/*      */     
/* 3544 */     this.useItem = itemStack;
/* 3545 */     this.useItemRemaining = itemStack.getUseDuration(this);
/*      */     
/* 3547 */     if (!level().isClientSide()) {
/* 3548 */       setLivingEntityFlag(1, true);
/* 3549 */       setLivingEntityFlag(2, (hand == InteractionHand.OFF_HAND));
/* 3550 */       this.useItem.causeUseVibration(this, GameEvent.ITEM_INTERACT_START);
/* 3551 */       if (this.useItem.has(DataComponents.KINETIC_WEAPON)) {
/* 3552 */         this.recentKineticEnemies = new Object2LongOpenHashMap();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 3559 */     super.onSyncedDataUpdated(accessor);
/*      */     
/* 3561 */     if (SLEEPING_POS_ID.equals(accessor)) {
/* 3562 */       if (level().isClientSide())
/*      */       {
/* 3564 */         getSleepingPos().ifPresent(this::setPosToBed);
/*      */       }
/* 3566 */     } else if (DATA_LIVING_ENTITY_FLAGS.equals(accessor) && level().isClientSide()) {
/* 3567 */       if (isUsingItem() && this.useItem.isEmpty()) {
/* 3568 */         this.useItem = getItemInHand(getUsedItemHand());
/* 3569 */         if (!this.useItem.isEmpty()) {
/* 3570 */           this.useItemRemaining = this.useItem.getUseDuration(this);
/*      */         }
/* 3572 */       } else if (!isUsingItem() && !this.useItem.isEmpty()) {
/* 3573 */         this.useItem = ItemStack.EMPTY;
/* 3574 */         this.useItemRemaining = 0;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void lookAt(EntityAnchorArgument.Anchor anchor, Vec3 pos) {
/* 3581 */     super.lookAt(anchor, pos);
/* 3582 */     this.yHeadRotO = this.yHeadRot;
/* 3583 */     this.yBodyRot = this.yHeadRot;
/* 3584 */     this.yBodyRotO = this.yBodyRot;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 3589 */   public float getPreciseBodyRotation(float partial) { return Mth.lerp(partial, this.yBodyRotO, this.yBodyRot); }
/*      */ 
/*      */   
/*      */   public void spawnItemParticles(ItemStack itemStack, int count) {
/* 3593 */     for (int i = 0; i < count; i++) {
/* 3594 */       Vec3 d = new Vec3((this.random.nextFloat() - 0.5D) * 0.1D, this.random.nextFloat() * 0.1D + 0.1D, 0.0D);
/* 3595 */       d = d.xRot(-getXRot() * 0.017453292F);
/* 3596 */       d = d.yRot(-getYRot() * 0.017453292F);
/*      */       
/* 3598 */       double y1 = -this.random.nextFloat() * 0.6D - 0.3D;
/* 3599 */       Vec3 p = new Vec3((this.random.nextFloat() - 0.5D) * 0.3D, y1, 0.6D);
/* 3600 */       p = p.xRot(-getXRot() * 0.017453292F);
/* 3601 */       p = p.yRot(-getYRot() * 0.017453292F);
/* 3602 */       p = p.add(getX(), getEyeY(), getZ());
/* 3603 */       level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemStack), p.x, p.y, p.z, d.x, d.y + 0.05D, d.z);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void completeUsingItem() {
/* 3608 */     if (level().isClientSide() && !isUsingItem()) {
/*      */       return;
/*      */     }
/*      */     
/* 3612 */     InteractionHand hand = getUsedItemHand();
/* 3613 */     if (!this.useItem.equals(getItemInHand(hand))) {
/* 3614 */       releaseUsingItem();
/*      */       
/*      */       return;
/*      */     } 
/* 3618 */     if (!this.useItem.isEmpty() && isUsingItem()) {
/* 3619 */       ItemStack result = this.useItem.finishUsingItem(level(), this);
/* 3620 */       if (result != this.useItem) {
/* 3621 */         setItemInHand(hand, result);
/*      */       }
/* 3623 */       stopUsingItem();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleExtraItemsCreatedOnUse(ItemStack extraCreatedRemainder) {}
/*      */ 
/*      */   
/* 3631 */   public ItemStack getUseItem() { return this.useItem; }
/*      */ 
/*      */ 
/*      */   
/* 3635 */   public int getUseItemRemainingTicks() { return this.useItemRemaining; }
/*      */ 
/*      */   
/*      */   public int getTicksUsingItem() {
/* 3639 */     if (isUsingItem()) {
/* 3640 */       return this.useItem.getUseDuration(this) - getUseItemRemainingTicks();
/*      */     }
/* 3642 */     return 0;
/*      */   }
/*      */   
/*      */   public float getTicksUsingItem(float partialTicks) {
/* 3646 */     if (!isUsingItem()) {
/* 3647 */       return 0.0F;
/*      */     }
/* 3649 */     return getTicksUsingItem() + partialTicks;
/*      */   }
/*      */ 
/*      */   
/*      */   public void releaseUsingItem() {
/* 3654 */     ItemStack itemInUsedHand = getItemInHand(getUsedItemHand());
/* 3655 */     if (!this.useItem.isEmpty() && ItemStack.isSameItem(itemInUsedHand, this.useItem)) {
/* 3656 */       this.useItem = itemInUsedHand;
/* 3657 */       this.useItem.releaseUsing(level(), this, getUseItemRemainingTicks());
/* 3658 */       if (this.useItem.useOnRelease()) {
/* 3659 */         updatingUsingItem();
/*      */       }
/*      */     } 
/* 3662 */     stopUsingItem();
/*      */   }
/*      */   
/*      */   public void stopUsingItem() {
/* 3666 */     if (!level().isClientSide()) {
/* 3667 */       boolean wasUsingItem = isUsingItem();
/* 3668 */       this.recentKineticEnemies = null;
/* 3669 */       setLivingEntityFlag(1, false);
/* 3670 */       if (wasUsingItem) {
/* 3671 */         this.useItem.causeUseVibration(this, GameEvent.ITEM_INTERACT_FINISH);
/*      */       }
/*      */     } 
/* 3674 */     this.useItem = ItemStack.EMPTY;
/* 3675 */     this.useItemRemaining = 0;
/*      */   }
/*      */ 
/*      */   
/* 3679 */   public boolean isBlocking() { return (getItemBlockingWith() != null); }
/*      */ 
/*      */   
/*      */   public ItemStack getItemBlockingWith() {
/* 3683 */     if (!isUsingItem()) {
/* 3684 */       return null;
/*      */     }
/* 3686 */     BlocksAttacks blocksAttacks = (BlocksAttacks)this.useItem.get(DataComponents.BLOCKS_ATTACKS);
/* 3687 */     if (blocksAttacks != null) {
/* 3688 */       int elapsedTicks = this.useItem.getItem().getUseDuration(this.useItem, this) - this.useItemRemaining;
/* 3689 */       if (elapsedTicks >= blocksAttacks.blockDelayTicks()) {
/* 3690 */         return this.useItem;
/*      */       }
/*      */     } 
/* 3693 */     return null;
/*      */   }
/*      */ 
/*      */   
/* 3697 */   public boolean isSuppressingSlidingDownLadder() { return isShiftKeyDown(); }
/*      */ 
/*      */ 
/*      */   
/* 3701 */   public boolean isFallFlying() { return getSharedFlag(7); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3708 */   public boolean isVisuallySwimming() { return (super.isVisuallySwimming() || (!isFallFlying() && hasPose(Pose.FALL_FLYING))); }
/*      */ 
/*      */ 
/*      */   
/* 3712 */   public int getFallFlyingTicks() { return this.fallFlyTicks; }
/*      */ 
/*      */   
/*      */   public boolean randomTeleport(double xx, double yy, double zz, boolean showParticles) {
/* 3716 */     double xo = getX();
/* 3717 */     double yo = getY();
/* 3718 */     double zo = getZ();
/*      */     
/* 3720 */     double y = yy;
/* 3721 */     boolean ok = false;
/* 3722 */     BlockPos pos = BlockPos.containing(xx, y, zz);
/* 3723 */     Level level = level();
/*      */     
/* 3725 */     if (level.hasChunkAt(pos)) {
/*      */       
/* 3727 */       boolean landed = false;
/* 3728 */       while (!landed && pos.getY() > level.getMinY()) {
/* 3729 */         BlockPos below = pos.below();
/* 3730 */         BlockState state = level.getBlockState(below);
/* 3731 */         if (state.blocksMotion()) {
/* 3732 */           landed = true; continue;
/*      */         } 
/* 3734 */         y--;
/* 3735 */         pos = below;
/*      */       } 
/*      */       
/* 3738 */       if (landed) {
/* 3739 */         teleportTo(xx, y, zz);
/* 3740 */         if (level.noCollision(this) && !level.containsAnyLiquid(getBoundingBox())) {
/* 3741 */           ok = true;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 3746 */     if (!ok) {
/* 3747 */       teleportTo(xo, yo, zo);
/* 3748 */       return false;
/*      */     } 
/*      */     
/* 3751 */     if (showParticles) {
/* 3752 */       level.broadcastEntityEvent(this, (byte)46);
/*      */     }
/*      */     
/* 3755 */     LivingEntity livingEntity = this; if (livingEntity instanceof PathfinderMob) { PathfinderMob pathfinderMob = (PathfinderMob)livingEntity;
/* 3756 */       pathfinderMob.getNavigation().stop(); }
/*      */ 
/*      */     
/* 3759 */     return true;
/*      */   }
/*      */ 
/*      */   
/* 3763 */   public boolean isAffectedByPotions() { return !isDeadOrDying(); }
/*      */ 
/*      */ 
/*      */   
/* 3767 */   public boolean attackable() { return true; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRecordPlayingNearby(BlockPos jukebox, boolean isPlaying) {}
/*      */ 
/*      */   
/* 3774 */   public boolean canPickUpLoot() { return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3779 */   public final EntityDimensions getDimensions(Pose pose) { return (pose == Pose.SLEEPING) ? SLEEPING_DIMENSIONS : getDefaultDimensions(pose).scale(getScale()); }
/*      */ 
/*      */ 
/*      */   
/* 3783 */   protected EntityDimensions getDefaultDimensions(Pose pose) { return getType().getDimensions().scale(getAgeScale()); }
/*      */ 
/*      */ 
/*      */   
/* 3787 */   public ImmutableList<Pose> getDismountPoses() { return ImmutableList.of(Pose.STANDING); }
/*      */ 
/*      */   
/*      */   public AABB getLocalBoundsForPose(Pose pose) {
/* 3791 */     EntityDimensions dimensions = getDimensions(pose);
/* 3792 */     return new AABB((
/* 3793 */         -dimensions.width() / 2.0F), 0.0D, (-dimensions.width() / 2.0F), (dimensions
/* 3794 */         .width() / 2.0F), dimensions.height(), (dimensions.width() / 2.0F));
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean wouldNotSuffocateAtTargetPose(Pose pose) {
/* 3799 */     AABB targetBB = getDimensions(pose).makeBoundingBox(position());
/* 3800 */     return level().noBlockCollision(this, targetBB);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 3805 */   public boolean canUsePortal(boolean ignorePassenger) { return (super.canUsePortal(ignorePassenger) && !isSleeping()); }
/*      */ 
/*      */ 
/*      */   
/* 3809 */   public Optional<BlockPos> getSleepingPos() { return (Optional)this.entityData.get(SLEEPING_POS_ID); }
/*      */ 
/*      */ 
/*      */   
/* 3813 */   public void setSleepingPos(BlockPos bedPosition) { this.entityData.set(SLEEPING_POS_ID, Optional.of(bedPosition)); }
/*      */ 
/*      */ 
/*      */   
/* 3817 */   public void clearSleepingPos() { this.entityData.set(SLEEPING_POS_ID, Optional.empty()); }
/*      */ 
/*      */ 
/*      */   
/* 3821 */   public boolean isSleeping() { return getSleepingPos().isPresent(); }
/*      */ 
/*      */   
/*      */   public void startSleeping(BlockPos bedPosition) {
/* 3825 */     if (isPassenger()) {
/* 3826 */       stopRiding();
/*      */     }
/*      */     
/* 3829 */     BlockState blockState = level().getBlockState(bedPosition);
/* 3830 */     if (blockState.getBlock() instanceof BedBlock) {
/* 3831 */       level().setBlock(bedPosition, (BlockState)blockState.setValue(BedBlock.OCCUPIED, Boolean.valueOf(true)), 3);
/*      */     }
/*      */     
/* 3834 */     setPose(Pose.SLEEPING);
/* 3835 */     setPosToBed(bedPosition);
/* 3836 */     setSleepingPos(bedPosition);
/* 3837 */     setDeltaMovement(Vec3.ZERO);
/* 3838 */     this.needsSync = true;
/*      */   }
/*      */ 
/*      */   
/* 3842 */   private void setPosToBed(BlockPos bedPosition) { setPos(bedPosition.getX() + 0.5D, bedPosition.getY() + 0.6875D, bedPosition.getZ() + 0.5D); }
/*      */ 
/*      */ 
/*      */   
/* 3846 */   private boolean checkBedExists() { return ((Boolean)getSleepingPos().map(bedPosition -> Boolean.valueOf(level().getBlockState(bedPosition).getBlock() instanceof BedBlock)).orElse(Boolean.valueOf(false))).booleanValue(); }
/*      */ 
/*      */   
/*      */   public void stopSleeping() {
/* 3850 */     Objects.requireNonNull(level()); getSleepingPos().filter(level()::hasChunkAt).ifPresent(bedPosition -> {
/* 3851 */           BlockState state = level().getBlockState(bedPosition);
/* 3852 */           if (state.getBlock() instanceof BedBlock) {
/* 3853 */             Direction facing = (Direction)state.getValue(BedBlock.FACING);
/* 3854 */             level().setBlock(bedPosition, (BlockState)state.setValue(BedBlock.OCCUPIED, Boolean.valueOf(false)), 3);
/*      */             
/* 3856 */             Vec3 standUp = (Vec3)BedBlock.findStandUpPosition(getType(), level(), bedPosition, facing, getYRot()).orElseGet(());
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 3861 */             Vec3 lookDirection = Vec3.atBottomCenterOf(bedPosition).subtract(standUp).normalize();
/* 3862 */             float yaw = (float)Mth.wrapDegrees(Mth.atan2(lookDirection.z, lookDirection.x) * 57.2957763671875D - 90.0D);
/*      */             
/* 3864 */             setPos(standUp.x, standUp.y, standUp.z);
/* 3865 */             setYRot(yaw);
/* 3866 */             setXRot(0.0F);
/*      */           } 
/*      */         });
/*      */     
/* 3870 */     Vec3 pos = position();
/* 3871 */     setPose(Pose.STANDING);
/* 3872 */     setPos(pos.x, pos.y, pos.z);
/* 3873 */     clearSleepingPos();
/*      */   }
/*      */   
/*      */   public Direction getBedOrientation() {
/* 3877 */     BlockPos bedPos = (BlockPos)getSleepingPos().orElse(null);
/* 3878 */     return (bedPos != null) ? BedBlock.getBedOrientation(level(), bedPos) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 3883 */   public boolean isInWall() { return (!isSleeping() && super.isInWall()); }
/*      */ 
/*      */ 
/*      */   
/* 3887 */   public ItemStack getProjectile(ItemStack heldWeapon) { return ItemStack.EMPTY; }
/*      */ 
/*      */   
/*      */   private static byte entityEventForEquipmentBreak(EquipmentSlot equipmentSlot) {
/* 3891 */     switch (equipmentSlot) { default: throw new MatchException(null, null);case MAINHAND: case OFFHAND: case HEAD: case CHEST: case FEET: case LEGS: case BODY: case SADDLE: break; }  return 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3899 */       68;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onEquippedItemBroken(Item brokenItem, EquipmentSlot inSlot) {
/* 3904 */     level().broadcastEntityEvent(this, entityEventForEquipmentBreak(inSlot));
/* 3905 */     stopLocationBasedEffects(getItemBySlot(inSlot), inSlot, this.attributes);
/*      */   }
/*      */   
/*      */   private void stopLocationBasedEffects(ItemStack previous, EquipmentSlot inSlot, AttributeMap attributes) {
/* 3909 */     previous.forEachModifier(inSlot, (attribute, modifier) -> {
/* 3910 */           AttributeInstance instance = attributes.getInstance(attribute);
/* 3911 */           if (instance != null) {
/* 3912 */             instance.removeModifier(modifier);
/*      */           }
/*      */         });
/* 3915 */     EnchantmentHelper.stopLocationBasedEffects(previous, this, inSlot);
/*      */   }
/*      */   
/*      */   public final boolean canEquipWithDispenser(ItemStack itemStack) {
/* 3919 */     if (!isAlive() || isSpectator()) {
/* 3920 */       return false;
/*      */     }
/* 3922 */     Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 3923 */     if (equippable == null || !equippable.dispensable()) {
/* 3924 */       return false;
/*      */     }
/* 3926 */     EquipmentSlot slot = equippable.slot();
/* 3927 */     if (!canUseSlot(slot) || !equippable.canBeEquippedBy(getType())) {
/* 3928 */       return false;
/*      */     }
/* 3930 */     return (getItemBySlot(slot).isEmpty() && canDispenserEquipIntoSlot(slot));
/*      */   }
/*      */ 
/*      */   
/* 3934 */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) { return true; }
/*      */ 
/*      */   
/*      */   public final EquipmentSlot getEquipmentSlotForItem(ItemStack itemStack) {
/* 3938 */     Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 3939 */     if (equippable != null && canUseSlot(equippable.slot())) {
/* 3940 */       return equippable.slot();
/*      */     }
/* 3942 */     return EquipmentSlot.MAINHAND;
/*      */   }
/*      */   
/*      */   public final boolean isEquippableInSlot(ItemStack itemStack, EquipmentSlot slot) {
/* 3946 */     Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 3947 */     if (equippable == null) {
/* 3948 */       return (slot == EquipmentSlot.MAINHAND && canUseSlot(EquipmentSlot.MAINHAND));
/*      */     }
/* 3950 */     return (slot == equippable.slot() && canUseSlot(equippable.slot()) && equippable.canBeEquippedBy(getType()));
/*      */   }
/*      */   
/*      */   private static SlotAccess createEquipmentSlotAccess(LivingEntity entity, EquipmentSlot equipmentSlot) {
/* 3954 */     if (equipmentSlot == EquipmentSlot.HEAD || equipmentSlot == EquipmentSlot.MAINHAND || equipmentSlot == EquipmentSlot.OFFHAND) {
/* 3955 */       return SlotAccess.forEquipmentSlot(entity, equipmentSlot);
/*      */     }
/* 3957 */     return SlotAccess.forEquipmentSlot(entity, equipmentSlot, stack -> (stack.isEmpty() || entity.getEquipmentSlotForItem(stack) == equipmentSlot));
/*      */   }
/*      */ 
/*      */   
/*      */   private static EquipmentSlot getEquipmentSlot(int slot) {
/* 3962 */     if (slot == 100 + EquipmentSlot.HEAD.getIndex()) {
/* 3963 */       return EquipmentSlot.HEAD;
/*      */     }
/* 3965 */     if (slot == 100 + EquipmentSlot.CHEST.getIndex()) {
/* 3966 */       return EquipmentSlot.CHEST;
/*      */     }
/* 3968 */     if (slot == 100 + EquipmentSlot.LEGS.getIndex()) {
/* 3969 */       return EquipmentSlot.LEGS;
/*      */     }
/* 3971 */     if (slot == 100 + EquipmentSlot.FEET.getIndex()) {
/* 3972 */       return EquipmentSlot.FEET;
/*      */     }
/* 3974 */     if (slot == 98) {
/* 3975 */       return EquipmentSlot.MAINHAND;
/*      */     }
/* 3977 */     if (slot == 99) {
/* 3978 */       return EquipmentSlot.OFFHAND;
/*      */     }
/* 3980 */     if (slot == 105) {
/* 3981 */       return EquipmentSlot.BODY;
/*      */     }
/* 3983 */     if (slot == 106) {
/* 3984 */       return EquipmentSlot.SADDLE;
/*      */     }
/* 3986 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public SlotAccess getSlot(int slot) {
/* 3991 */     EquipmentSlot equipmentSlot = getEquipmentSlot(slot);
/*      */     
/* 3993 */     if (equipmentSlot != null) {
/* 3994 */       return createEquipmentSlotAccess(this, equipmentSlot);
/*      */     }
/*      */     
/* 3997 */     return super.getSlot(slot);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canFreeze() {
/* 4002 */     if (isSpectator()) {
/* 4003 */       return false;
/*      */     }
/* 4005 */     for (EquipmentSlot slot : EquipmentSlotGroup.ARMOR) {
/* 4006 */       if (getItemBySlot(slot).is(ItemTags.FREEZE_IMMUNE_WEARABLES)) {
/* 4007 */         return false;
/*      */       }
/*      */     } 
/* 4010 */     return super.canFreeze();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 4015 */   public boolean isCurrentlyGlowing() { return ((!level().isClientSide() && hasEffect(MobEffects.GLOWING)) || super.isCurrentlyGlowing()); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 4020 */   public float getVisualRotationYInDegrees() { return this.yBodyRot; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 4025 */     double x = packet.getX();
/* 4026 */     double y = packet.getY();
/* 4027 */     double z = packet.getZ();
/* 4028 */     float yRot = packet.getYRot();
/* 4029 */     float xRot = packet.getXRot();
/* 4030 */     syncPacketPositionCodec(x, y, z);
/* 4031 */     this.yBodyRot = packet.getYHeadRot();
/* 4032 */     this.yHeadRot = packet.getYHeadRot();
/* 4033 */     this.yBodyRotO = this.yBodyRot;
/* 4034 */     this.yHeadRotO = this.yHeadRot;
/*      */     
/* 4036 */     setId(packet.getId());
/* 4037 */     setUUID(packet.getUUID());
/* 4038 */     absSnapTo(x, y, z, yRot, xRot);
/* 4039 */     setDeltaMovement(packet.getMovement());
/*      */   }
/*      */   
/*      */   public float getSecondsToDisableBlocking() {
/* 4043 */     ItemStack weaponItem = getWeaponItem();
/* 4044 */     Weapon weapon = (Weapon)weaponItem.get(DataComponents.WEAPON);
/* 4045 */     return (weapon != null && weaponItem == getActiveItem()) ? weapon.disableBlockingForSeconds() : 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public float maxUpStep() {
/* 4050 */     float maxUpStep = (float)getAttributeValue(Attributes.STEP_HEIGHT);
/* 4051 */     return (getControllingPassenger() instanceof Player) ? Math.max(maxUpStep, 1.0F) : maxUpStep;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 4056 */   public Vec3 getPassengerRidingPosition(Entity passenger) { return position().add(getPassengerAttachmentPoint(passenger, getDimensions(getPose()), getScale() * getAgeScale())); }
/*      */ 
/*      */ 
/*      */   
/* 4060 */   protected void lerpHeadRotationStep(int lerpHeadSteps, double targetYHeadRot) { this.yHeadRot = (float)Mth.rotLerp(1.0D / lerpHeadSteps, this.yHeadRot, targetYHeadRot); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 4065 */   public void igniteForTicks(int numberOfTicks) { super.igniteForTicks(Mth.ceil(numberOfTicks * getAttributeValue(Attributes.BURNING_TIME))); }
/*      */ 
/*      */ 
/*      */   
/* 4069 */   public boolean hasInfiniteMaterials() { return false; }
/*      */ 
/*      */   
/*      */   public boolean isInvulnerableTo(ServerLevel level, DamageSource source) {
/* 4073 */     return (isInvulnerableToBase(source) || 
/* 4074 */       EnchantmentHelper.isImmuneToDamage(level, this, source));
/*      */   }
/*      */   
/*      */   public static boolean canGlideUsing(ItemStack itemStack, EquipmentSlot slot) {
/* 4078 */     if (!itemStack.has(DataComponents.GLIDER)) {
/* 4079 */       return false;
/*      */     }
/* 4081 */     Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 4082 */     return (equippable != null && slot == equippable.slot() && !itemStack.nextDamageWillBreak());
/*      */   }
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/* 4087 */   public int getLastHurtByPlayerMemoryTime() { return this.lastHurtByPlayerMemoryTime; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 4092 */   public boolean isTransmittingWaypoint() { return (getAttributeValue(Attributes.WAYPOINT_TRANSMIT_RANGE) > 0.0D); }
/*      */ 
/*      */ 
/*      */   
/*      */   public Optional<WaypointTransmitter.Connection> makeWaypointConnectionWith(ServerPlayer player) {
/* 4097 */     if (this.firstTick || player == this) {
/* 4098 */       return Optional.empty();
/*      */     }
/*      */     
/* 4101 */     if (WaypointTransmitter.doesSourceIgnoreReceiver(this, player)) {
/* 4102 */       return Optional.empty();
/*      */     }
/*      */     
/* 4105 */     Waypoint.Icon icon = this.locatorBarIcon.cloneAndAssignStyle(this);
/*      */     
/* 4107 */     if (WaypointTransmitter.isReallyFar(this, player)) {
/* 4108 */       return Optional.of(new WaypointTransmitter.EntityAzimuthConnection(this, icon, player));
/*      */     }
/*      */     
/* 4111 */     if (!WaypointTransmitter.isChunkVisible(chunkPosition(), player)) {
/* 4112 */       return Optional.of(new WaypointTransmitter.EntityChunkConnection(this, icon, player));
/*      */     }
/*      */     
/* 4115 */     return Optional.of(new WaypointTransmitter.EntityBlockConnection(this, icon, player));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 4120 */   public Waypoint.Icon waypointIcon() { return this.locatorBarIcon; }
/*      */   
/*      */   public abstract HumanoidArm getMainArm();
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\LivingEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */