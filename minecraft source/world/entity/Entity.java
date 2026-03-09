/*      */ package net.minecraft.world.entity;
/*      */ 
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.google.common.collect.UnmodifiableIterator;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.Codec;
/*      */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*      */ import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
/*      */ import it.unimi.dsi.fastutil.floats.FloatArraySet;
/*      */ import it.unimi.dsi.fastutil.floats.FloatArrays;
/*      */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*      */ import it.unimi.dsi.fastutil.longs.LongSet;
/*      */ import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
/*      */ import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.advancements.CriteriaTriggers;
/*      */ import net.minecraft.commands.CommandSource;
/*      */ import net.minecraft.commands.CommandSourceStack;
/*      */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.core.UUIDUtil;
/*      */ import net.minecraft.core.component.DataComponentGetter;
/*      */ import net.minecraft.core.component.DataComponentType;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.particles.BlockParticleOption;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.ComponentSerialization;
/*      */ import net.minecraft.network.chat.HoverEvent;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.Style;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*      */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
/*      */ import net.minecraft.network.protocol.game.VecDeltaCodec;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SyncedDataHolder;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.level.ServerEntity;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.server.level.TicketType;
/*      */ import net.minecraft.server.permissions.PermissionSet;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.DamageTypeTags;
/*      */ import net.minecraft.tags.EntityTypeTags;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.ARGB;
/*      */ import net.minecraft.util.BlockUtil;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.ProblemReporter;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.debug.DebugEntityBlockIntersection;
/*      */ import net.minecraft.util.debug.DebugSubscriptions;
/*      */ import net.minecraft.util.debug.DebugValueSource;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.Nameable;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.damagesource.DamageSources;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.projectile.Projectile;
/*      */ import net.minecraft.world.entity.projectile.ProjectileDeflection;
/*      */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.component.CustomData;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.equipment.Equippable;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.ClipContext;
/*      */ import net.minecraft.world.level.Explosion;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.HoneyBlock;
/*      */ import net.minecraft.world.level.block.Mirror;
/*      */ import net.minecraft.world.level.block.Portal;
/*      */ import net.minecraft.world.level.block.RenderShape;
/*      */ import net.minecraft.world.level.block.Rotation;
/*      */ import net.minecraft.world.level.block.SoundType;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.border.WorldBorder;
/*      */ import net.minecraft.world.level.entity.EntityAccess;
/*      */ import net.minecraft.world.level.entity.EntityInLevelCallback;
/*      */ import net.minecraft.world.level.gameevent.DynamicGameEventListener;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.levelgen.Heightmap;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.material.PushReaction;
/*      */ import net.minecraft.world.level.portal.PortalShape;
/*      */ import net.minecraft.world.level.portal.TeleportTransition;
/*      */ import net.minecraft.world.level.storage.TagValueInput;
/*      */ import net.minecraft.world.level.storage.TagValueOutput;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.level.storage.loot.LootTable;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.HitResult;
/*      */ import net.minecraft.world.phys.Vec2;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.BooleanOp;
/*      */ import net.minecraft.world.phys.shapes.CollisionContext;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ import net.minecraft.world.scores.PlayerTeam;
/*      */ import net.minecraft.world.scores.ScoreHolder;
/*      */ import net.minecraft.world.scores.Team;
/*      */ import net.minecraft.world.waypoints.WaypointTransmitter;
/*      */ import org.jetbrains.annotations.Contract;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Entity
/*      */   implements Nameable, EntityAccess, ScoreHolder, SyncedDataHolder, DataComponentGetter, ItemOwner, SlotProvider, DebugValueSource
/*      */ {
/*  162 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   public static final String TAG_ID = "id";
/*      */   
/*      */   public static final String TAG_UUID = "UUID";
/*      */   public static final String TAG_PASSENGERS = "Passengers";
/*      */   public static final String TAG_DATA = "data";
/*      */   public static final String TAG_POS = "Pos";
/*      */   public static final String TAG_MOTION = "Motion";
/*      */   public static final String TAG_ROTATION = "Rotation";
/*      */   public static final String TAG_PORTAL_COOLDOWN = "PortalCooldown";
/*      */   public static final String TAG_NO_GRAVITY = "NoGravity";
/*      */   public static final String TAG_AIR = "Air";
/*      */   public static final String TAG_ON_GROUND = "OnGround";
/*      */   public static final String TAG_FALL_DISTANCE = "fall_distance";
/*      */   public static final String TAG_FIRE = "Fire";
/*      */   public static final String TAG_SILENT = "Silent";
/*      */   public static final String TAG_GLOWING = "Glowing";
/*      */   public static final String TAG_INVULNERABLE = "Invulnerable";
/*      */   public static final String TAG_CUSTOM_NAME = "CustomName";
/*  182 */   private static final AtomicInteger ENTITY_COUNTER = new AtomicInteger();
/*      */   
/*      */   public static final int CONTENTS_SLOT_INDEX = 0;
/*      */   
/*      */   public static final int BOARDING_COOLDOWN = 60;
/*      */   public static final int TOTAL_AIR_SUPPLY = 300;
/*      */   public static final int MAX_ENTITY_TAG_COUNT = 1024;
/*  189 */   private static final Codec<List<String>> TAG_LIST_CODEC = Codec.STRING.sizeLimitedListOf(1024);
/*      */   
/*      */   public static final float DELTA_AFFECTED_BY_BLOCKS_BELOW_0_2 = 0.2F;
/*      */   
/*      */   public static final double DELTA_AFFECTED_BY_BLOCKS_BELOW_0_5 = 0.500001D;
/*      */   public static final double DELTA_AFFECTED_BY_BLOCKS_BELOW_1_0 = 0.999999D;
/*      */   public static final int BASE_TICKS_REQUIRED_TO_FREEZE = 140;
/*      */   public static final int FREEZE_HURT_FREQUENCY = 40;
/*      */   public static final int BASE_SAFE_FALL_DISTANCE = 3;
/*  198 */   private static final AABB INITIAL_AABB = new AABB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
/*      */   
/*      */   private static final double WATER_FLOW_SCALE = 0.014D;
/*      */   
/*      */   private static final double LAVA_FAST_FLOW_SCALE = 0.007D;
/*      */   
/*      */   private static final double LAVA_SLOW_FLOW_SCALE = 0.0023333333333333335D;
/*      */   private static final int MAX_BLOCK_ITERATIONS_ALONG_TRAVEL_PER_TICK = 16;
/*      */   private static final double MAX_MOVEMENT_RESETTING_TRACE_DISTANCE = 8.0D;
/*  207 */   private static double viewScale = 1.0D;
/*      */   
/*      */   private final EntityType<?> type;
/*      */   
/*      */   private boolean requiresPrecisePosition;
/*      */   
/*      */   private int id;
/*      */   
/*      */   public boolean blocksBuilding;
/*      */   
/*      */   private ImmutableList<Entity> passengers;
/*      */   
/*      */   protected int boardingCooldown;
/*      */   
/*      */   private Entity vehicle;
/*      */   
/*      */   private Level level;
/*      */   
/*      */   public double xo;
/*      */   
/*      */   public double yo;
/*      */   
/*      */   public double zo;
/*      */   
/*      */   private Vec3 position;
/*      */   
/*      */   private BlockPos blockPosition;
/*      */   
/*      */   private ChunkPos chunkPosition;
/*      */   
/*      */   private Vec3 deltaMovement;
/*      */   private float yRot;
/*      */   private float xRot;
/*      */   public float yRotO;
/*      */   public float xRotO;
/*      */   private AABB bb;
/*      */   private boolean onGround;
/*      */   public boolean horizontalCollision;
/*      */   public boolean verticalCollision;
/*      */   public boolean verticalCollisionBelow;
/*      */   public boolean minorHorizontalCollision;
/*      */   public boolean hurtMarked;
/*      */   protected Vec3 stuckSpeedMultiplier;
/*      */   private RemovalReason removalReason;
/*      */   public static final float DEFAULT_BB_WIDTH = 0.6F;
/*      */   public static final float DEFAULT_BB_HEIGHT = 1.8F;
/*      */   public float moveDist;
/*      */   public float flyDist;
/*      */   public double fallDistance;
/*      */   private float nextStep;
/*      */   public double xOld;
/*      */   public double yOld;
/*      */   public double zOld;
/*      */   public boolean noPhysics;
/*      */   protected final RandomSource random;
/*      */   public int tickCount;
/*      */   private int remainingFireTicks;
/*      */   protected boolean wasTouchingWater;
/*      */   protected Object2DoubleMap<TagKey<Fluid>> fluidHeight;
/*      */   protected boolean wasEyeInWater;
/*      */   private final Set<TagKey<Fluid>> fluidOnEyes;
/*      */   public int invulnerableTime;
/*      */   protected boolean firstTick;
/*      */   protected final SynchedEntityData entityData;
/*  271 */   protected static final EntityDataAccessor<Byte> DATA_SHARED_FLAGS_ID = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.BYTE);
/*      */   
/*      */   protected static final int FLAG_ONFIRE = 0;
/*      */   
/*      */   private static final int FLAG_SHIFT_KEY_DOWN = 1;
/*      */   private static final int FLAG_SPRINTING = 3;
/*      */   private static final int FLAG_SWIMMING = 4;
/*      */   private static final int FLAG_INVISIBLE = 5;
/*      */   protected static final int FLAG_GLOWING = 6;
/*      */   protected static final int FLAG_FALL_FLYING = 7;
/*  281 */   private static final EntityDataAccessor<Integer> DATA_AIR_SUPPLY_ID = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.INT);
/*  282 */   private static final EntityDataAccessor<Optional<Component>> DATA_CUSTOM_NAME = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.OPTIONAL_COMPONENT);
/*  283 */   private static final EntityDataAccessor<Boolean> DATA_CUSTOM_NAME_VISIBLE = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.BOOLEAN);
/*  284 */   private static final EntityDataAccessor<Boolean> DATA_SILENT = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.BOOLEAN);
/*  285 */   private static final EntityDataAccessor<Boolean> DATA_NO_GRAVITY = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.BOOLEAN);
/*  286 */   protected static final EntityDataAccessor<Pose> DATA_POSE = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.POSE);
/*  287 */   private static final EntityDataAccessor<Integer> DATA_TICKS_FROZEN = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.INT);
/*      */   
/*      */   private EntityInLevelCallback levelCallback;
/*      */   
/*      */   private final VecDeltaCodec packetPositionCodec;
/*      */   
/*      */   public boolean needsSync;
/*      */   
/*      */   public PortalProcessor portalProcess;
/*      */   
/*      */   private int portalCooldown;
/*      */   
/*      */   private boolean invulnerable;
/*      */   
/*      */   protected UUID uuid;
/*      */   
/*      */   protected String stringUUID;
/*      */   private boolean hasGlowingTag;
/*      */   private final Set<String> tags;
/*      */   private final double[] pistonDeltas;
/*      */   private long pistonDeltasGameTime;
/*      */   private EntityDimensions dimensions;
/*      */   private float eyeHeight;
/*      */   public boolean isInPowderSnow;
/*      */   public boolean wasInPowderSnow;
/*      */   public Optional<BlockPos> mainSupportingBlockPos;
/*      */   private boolean onGroundNoBlocks;
/*      */   private float crystalSoundIntensity;
/*      */   private int lastCrystalSoundPlayTick;
/*      */   private boolean hasVisualFire;
/*      */   private Vec3 lastKnownSpeed;
/*      */   private Vec3 lastKnownPosition;
/*      */   private BlockState inBlockState;
/*      */   public static final int MAX_MOVEMENTS_HANDELED_PER_TICK = 100;
/*      */   private final ArrayDeque<Movement> movementThisTick;
/*      */   private final List<Movement> finalMovementsThisTick;
/*      */   private final LongSet visitedBlocks;
/*      */   private final InsideBlockEffectApplier.StepBasedCollector insideEffectCollector;
/*      */   private CustomData customData;
/*      */   
/*      */   private static final class Movement
/*      */     extends Record
/*      */   {
/*      */     private final Vec3 from;
/*      */     private final Vec3 to;
/*      */     private final Optional<Vec3> axisDependentOriginalMovement;
/*      */     
/*  334 */     private Movement(Vec3 from, Vec3 to, Optional<Vec3> axisDependentOriginalMovement) { this.from = from; this.to = to; this.axisDependentOriginalMovement = axisDependentOriginalMovement; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Entity$Movement;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #334	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/entity/Entity$Movement; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Entity$Movement;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #334	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/entity/Entity$Movement; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Entity$Movement;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #334	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/entity/Entity$Movement;
/*  334 */       //   0	8	1	o	Ljava/lang/Object; } public Vec3 from() { return this.from; } public Vec3 to() { return this.to; } public Optional<Vec3> axisDependentOriginalMovement() { return this.axisDependentOriginalMovement; }
/*      */     
/*  336 */     public Movement(Vec3 from, Vec3 to, Vec3 axisDependentOriginalMovement) { this(from, to, Optional.of(axisDependentOriginalMovement)); }
/*      */ 
/*      */ 
/*      */     
/*  340 */     public Movement(Vec3 from, Vec3 to) { this(from, to, Optional.empty()); } } public Entity(EntityType<?> type, Level level) { this.id = ENTITY_COUNTER.incrementAndGet(); this.passengers = ImmutableList.of(); this.deltaMovement = Vec3.ZERO; this.bb = INITIAL_AABB; this.stuckSpeedMultiplier = Vec3.ZERO; this.nextStep = 1.0F; this.random = RandomSource.create(); this.fluidHeight = new Object2DoubleArrayMap(2); this.fluidOnEyes = new HashSet(); this.firstTick = true; this.levelCallback = EntityInLevelCallback.NULL; this.packetPositionCodec = new VecDeltaCodec(); this.uuid = Mth.createInsecureUUID(this.random); this.stringUUID = this.uuid.toString(); this.tags = Sets.newHashSet(); this.pistonDeltas = new double[] { 0.0D, 0.0D, 0.0D }; this.mainSupportingBlockPos = Optional.empty(); this.onGroundNoBlocks = false; this.lastKnownSpeed = Vec3.ZERO; this.inBlockState = null; this.movementThisTick = new ArrayDeque(100);
/*      */     this.finalMovementsThisTick = new ObjectArrayList();
/*      */     this.visitedBlocks = new LongOpenHashSet();
/*      */     this.insideEffectCollector = new InsideBlockEffectApplier.StepBasedCollector();
/*      */     this.customData = CustomData.EMPTY;
/*  345 */     this.type = type;
/*  346 */     this.level = level;
/*      */     
/*  348 */     this.dimensions = type.getDimensions();
/*  349 */     this.position = Vec3.ZERO;
/*  350 */     this.blockPosition = BlockPos.ZERO;
/*  351 */     this.chunkPosition = ChunkPos.ZERO;
/*      */     
/*  353 */     SynchedEntityData.Builder entityDataBuilder = new SynchedEntityData.Builder(this);
/*  354 */     entityDataBuilder.define(DATA_SHARED_FLAGS_ID, Byte.valueOf((byte)0));
/*  355 */     entityDataBuilder.define(DATA_AIR_SUPPLY_ID, Integer.valueOf(getMaxAirSupply()));
/*  356 */     entityDataBuilder.define(DATA_CUSTOM_NAME_VISIBLE, Boolean.valueOf(false));
/*  357 */     entityDataBuilder.define(DATA_CUSTOM_NAME, Optional.empty());
/*  358 */     entityDataBuilder.define(DATA_SILENT, Boolean.valueOf(false));
/*  359 */     entityDataBuilder.define(DATA_NO_GRAVITY, Boolean.valueOf(false));
/*  360 */     entityDataBuilder.define(DATA_POSE, Pose.STANDING);
/*  361 */     entityDataBuilder.define(DATA_TICKS_FROZEN, Integer.valueOf(0));
/*  362 */     defineSynchedData(entityDataBuilder);
/*  363 */     this.entityData = entityDataBuilder.build();
/*      */     
/*  365 */     setPos(0.0D, 0.0D, 0.0D);
/*      */     
/*  367 */     this.eyeHeight = this.dimensions.eyeHeight(); }
/*      */ 
/*      */   
/*      */   public boolean isColliding(BlockPos pos, BlockState state) {
/*  371 */     VoxelShape movedBlockShape = state.getCollisionShape(level(), pos, CollisionContext.of(this)).move(pos);
/*  372 */     return Shapes.joinIsNotEmpty(movedBlockShape, Shapes.create(getBoundingBox()), BooleanOp.AND);
/*      */   }
/*      */   
/*      */   public int getTeamColor() {
/*  376 */     PlayerTeam playerTeam = getTeam();
/*  377 */     if (playerTeam != null && playerTeam.getColor().getColor() != null) {
/*  378 */       return playerTeam.getColor().getColor().intValue();
/*      */     }
/*  380 */     return 16777215;
/*      */   }
/*      */ 
/*      */   
/*  384 */   public boolean isSpectator() { return false; }
/*      */ 
/*      */ 
/*      */   
/*  388 */   public boolean canInteractWithLevel() { return (isAlive() && !isRemoved() && !isSpectator()); }
/*      */ 
/*      */   
/*      */   public final void unRide() {
/*  392 */     if (isVehicle()) {
/*  393 */       ejectPassengers();
/*      */     }
/*  395 */     if (isPassenger()) {
/*  396 */       stopRiding();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  406 */   public void syncPacketPositionCodec(double x, double y, double z) { this.packetPositionCodec.setBase(new Vec3(x, y, z)); }
/*      */ 
/*      */ 
/*      */   
/*  410 */   public VecDeltaCodec getPositionCodec() { return this.packetPositionCodec; }
/*      */ 
/*      */ 
/*      */   
/*  414 */   public EntityType<?> getType() { return this.type; }
/*      */ 
/*      */ 
/*      */   
/*  418 */   public boolean getRequiresPrecisePosition() { return this.requiresPrecisePosition; }
/*      */ 
/*      */ 
/*      */   
/*  422 */   public void setRequiresPrecisePosition(boolean requiresPrecisePosition) { this.requiresPrecisePosition = requiresPrecisePosition; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  427 */   public int getId() { return this.id; }
/*      */ 
/*      */ 
/*      */   
/*  431 */   public void setId(int id) { this.id = id; }
/*      */ 
/*      */ 
/*      */   
/*  435 */   public Set<String> getTags() { return this.tags; }
/*      */ 
/*      */   
/*      */   public boolean addTag(String tag) {
/*  439 */     if (this.tags.size() >= 1024) {
/*  440 */       return false;
/*      */     }
/*  442 */     return this.tags.add(tag);
/*      */   }
/*      */ 
/*      */   
/*  446 */   public boolean removeTag(String tag) { return this.tags.remove(tag); }
/*      */ 
/*      */   
/*      */   public void kill(ServerLevel level) {
/*  450 */     remove(RemovalReason.KILLED);
/*  451 */     gameEvent(GameEvent.ENTITY_DIE);
/*      */   }
/*      */ 
/*      */   
/*  455 */   public final void discard() { remove(RemovalReason.DISCARDED); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  461 */   public SynchedEntityData getEntityData() { return this.entityData; }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object obj) {
/*  466 */     if (obj instanceof Entity) {
/*  467 */       return (((Entity)obj).id == this.id);
/*      */     }
/*  469 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  474 */   public int hashCode() { return this.id; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  479 */   public void remove(RemovalReason reason) { setRemoved(reason); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onClientRemoval() {}
/*      */ 
/*      */   
/*      */   public void onRemoval(RemovalReason reason) {}
/*      */ 
/*      */   
/*  489 */   public void setPose(Pose pose) { this.entityData.set(DATA_POSE, pose); }
/*      */ 
/*      */ 
/*      */   
/*  493 */   public Pose getPose() { return (Pose)this.entityData.get(DATA_POSE); }
/*      */ 
/*      */ 
/*      */   
/*  497 */   public boolean hasPose(Pose pose) { return (getPose() == pose); }
/*      */ 
/*      */ 
/*      */   
/*  501 */   public boolean closerThan(Entity other, double distance) { return position().closerThan(other.position(), distance); }
/*      */ 
/*      */   
/*      */   public boolean closerThan(Entity other, double distanceXZ, double distanceY) {
/*  505 */     double dx = other.getX() - getX();
/*  506 */     double dy = other.getY() - getY();
/*  507 */     double dz = other.getZ() - getZ();
/*  508 */     return (Mth.lengthSquared(dx, dz) < Mth.square(distanceXZ) && 
/*  509 */       Mth.square(dy) < Mth.square(distanceY));
/*      */   }
/*      */   
/*      */   protected void setRot(float yRot, float xRot) {
/*  513 */     setYRot(yRot % 360.0F);
/*  514 */     setXRot(xRot % 360.0F);
/*      */   }
/*      */ 
/*      */   
/*  518 */   public final void setPos(Vec3 pos) { setPos(pos.x(), pos.y(), pos.z()); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPos(double x, double y, double z) {
/*  523 */     setPosRaw(x, y, z);
/*  524 */     setBoundingBox(makeBoundingBox());
/*      */   }
/*      */ 
/*      */   
/*  528 */   protected final AABB makeBoundingBox() { return makeBoundingBox(this.position); }
/*      */ 
/*      */ 
/*      */   
/*  532 */   protected AABB makeBoundingBox(Vec3 position) { return this.dimensions.makeBoundingBox(position); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void reapplyPosition() {
/*  537 */     this.lastKnownPosition = null;
/*  538 */     setPos(this.position.x, this.position.y, this.position.z);
/*      */   }
/*      */   
/*      */   public void turn(double xo, double yo) {
/*  542 */     float xDelta = (float)yo * 0.15F;
/*  543 */     float yDelta = (float)xo * 0.15F;
/*      */     
/*  545 */     setXRot(getXRot() + xDelta);
/*  546 */     setYRot(getYRot() + yDelta);
/*  547 */     setXRot(Mth.clamp(getXRot(), -90.0F, 90.0F));
/*      */     
/*  549 */     this.xRotO += xDelta;
/*  550 */     this.yRotO += yDelta;
/*  551 */     this.xRotO = Mth.clamp(this.xRotO, -90.0F, 90.0F);
/*      */     
/*  553 */     if (this.vehicle != null) {
/*  554 */       this.vehicle.onPassengerTurned(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateDataBeforeSync() {}
/*      */ 
/*      */   
/*  562 */   public void tick() { baseTick(); }
/*      */ 
/*      */   
/*      */   public void baseTick() {
/*  566 */     ProfilerFiller profiler = Profiler.get();
/*  567 */     profiler.push("entityBaseTick");
/*      */     
/*  569 */     computeSpeed();
/*      */     
/*  571 */     this.inBlockState = null;
/*      */     
/*  573 */     if (isPassenger() && getVehicle().isRemoved()) {
/*  574 */       stopRiding();
/*      */     }
/*      */     
/*  577 */     if (this.boardingCooldown > 0) {
/*  578 */       this.boardingCooldown--;
/*      */     }
/*      */     
/*  581 */     handlePortal();
/*      */ 
/*      */     
/*  584 */     if (canSpawnSprintParticle()) {
/*  585 */       spawnSprintParticle();
/*      */     }
/*      */     
/*  588 */     this.wasInPowderSnow = this.isInPowderSnow;
/*  589 */     this.isInPowderSnow = false;
/*  590 */     updateInWaterStateAndDoFluidPushing();
/*  591 */     updateFluidOnEyes();
/*  592 */     updateSwimming();
/*      */     
/*  594 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1;
/*  595 */       if (this.remainingFireTicks > 0) {
/*  596 */         if (fireImmune()) {
/*  597 */           clearFire();
/*      */         } else {
/*  599 */           if (this.remainingFireTicks % 20 == 0 && !isInLava()) {
/*  600 */             hurtServer(serverLevel, damageSources().onFire(), 1.0F);
/*      */           }
/*  602 */           setRemainingFireTicks(this.remainingFireTicks - 1);
/*      */         } 
/*      */       } }
/*      */     else
/*  606 */     { clearFire(); }
/*      */ 
/*      */     
/*  609 */     if (isInLava()) {
/*  610 */       this.fallDistance *= 0.5D;
/*      */     }
/*      */     
/*  613 */     checkBelowWorld();
/*      */     
/*  615 */     if (!level().isClientSide()) {
/*  616 */       setSharedFlagOnFire((this.remainingFireTicks > 0));
/*      */     }
/*      */     
/*  619 */     this.firstTick = false;
/*      */     
/*  621 */     level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1;
/*  622 */       if (this instanceof Leashable) {
/*  623 */         Leashable.tickLeash(serverLevel, (Entity)this);
/*      */       } }
/*      */ 
/*      */     
/*  627 */     profiler.pop();
/*      */   }
/*      */   
/*      */   protected void computeSpeed() {
/*  631 */     if (this.lastKnownPosition == null) {
/*  632 */       this.lastKnownPosition = position();
/*      */     }
/*  634 */     this.lastKnownSpeed = position().subtract(this.lastKnownPosition);
/*  635 */     this.lastKnownPosition = position();
/*      */   }
/*      */ 
/*      */   
/*  639 */   public void setSharedFlagOnFire(boolean value) { setSharedFlag(0, (value || this.hasVisualFire)); }
/*      */ 
/*      */   
/*      */   public void checkBelowWorld() {
/*  643 */     if (getY() < (level().getMinY() - 64)) {
/*  644 */       onBelowWorld();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  649 */   public void setPortalCooldown() { this.portalCooldown = getDimensionChangingDelay(); }
/*      */ 
/*      */ 
/*      */   
/*  653 */   public void setPortalCooldown(int portalCooldown) { this.portalCooldown = portalCooldown; }
/*      */ 
/*      */ 
/*      */   
/*  657 */   public int getPortalCooldown() { return this.portalCooldown; }
/*      */ 
/*      */ 
/*      */   
/*  661 */   public boolean isOnPortalCooldown() { return (this.portalCooldown > 0); }
/*      */ 
/*      */   
/*      */   protected void processPortalCooldown() {
/*  665 */     if (isOnPortalCooldown()) {
/*  666 */       this.portalCooldown--;
/*      */     }
/*      */   }
/*      */   
/*      */   public void lavaIgnite() {
/*  671 */     if (fireImmune()) {
/*      */       return;
/*      */     }
/*      */     
/*  675 */     igniteForSeconds(15.0F);
/*      */   }
/*      */   
/*      */   public void lavaHurt() {
/*  679 */     if (fireImmune()) {
/*      */       return;
/*      */     }
/*  682 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1;
/*  683 */       if (hurtServer(serverLevel, damageSources().lava(), 4.0F) && 
/*  684 */         shouldPlayLavaHurtSound() && !isSilent()) {
/*  685 */         serverLevel.playSound(null, getX(), getY(), getZ(), SoundEvents.GENERIC_BURN, getSoundSource(), 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
/*      */       } }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  692 */   protected boolean shouldPlayLavaHurtSound() { return true; }
/*      */ 
/*      */ 
/*      */   
/*  696 */   public final void igniteForSeconds(float numberOfSeconds) { igniteForTicks(Mth.floor(numberOfSeconds * 20.0F)); }
/*      */ 
/*      */   
/*      */   public void igniteForTicks(int numberOfTicks) {
/*  700 */     if (this.remainingFireTicks < numberOfTicks) {
/*  701 */       setRemainingFireTicks(numberOfTicks);
/*      */     }
/*  703 */     clearFreeze();
/*      */   }
/*      */ 
/*      */   
/*  707 */   public void setRemainingFireTicks(int remainingTicks) { this.remainingFireTicks = remainingTicks; }
/*      */ 
/*      */ 
/*      */   
/*  711 */   public int getRemainingFireTicks() { return this.remainingFireTicks; }
/*      */ 
/*      */ 
/*      */   
/*  715 */   public void clearFire() { setRemainingFireTicks(Math.min(0, getRemainingFireTicks())); }
/*      */ 
/*      */ 
/*      */   
/*  719 */   protected void onBelowWorld() { discard(); }
/*      */ 
/*      */ 
/*      */   
/*  723 */   public boolean isFree(double xa, double ya, double za) { return isFree(getBoundingBox().move(xa, ya, za)); }
/*      */ 
/*      */ 
/*      */   
/*  727 */   private boolean isFree(AABB box) { return (level().noCollision(this, box) && !level().containsAnyLiquid(box)); }
/*      */ 
/*      */   
/*      */   public void setOnGround(boolean onGround) {
/*  731 */     this.onGround = onGround;
/*  732 */     checkSupportingBlock(onGround, null);
/*      */   }
/*      */ 
/*      */   
/*  736 */   public void setOnGroundWithMovement(boolean onGround, Vec3 movement) { setOnGroundWithMovement(onGround, this.horizontalCollision, movement); }
/*      */ 
/*      */   
/*      */   public void setOnGroundWithMovement(boolean onGround, boolean horizontalCollision, Vec3 movement) {
/*  740 */     this.onGround = onGround;
/*  741 */     this.horizontalCollision = horizontalCollision;
/*  742 */     checkSupportingBlock(onGround, movement);
/*      */   }
/*      */ 
/*      */   
/*  746 */   public boolean isSupportedBy(BlockPos pos) { return (this.mainSupportingBlockPos.isPresent() && ((BlockPos)this.mainSupportingBlockPos.get()).equals(pos)); }
/*      */ 
/*      */   
/*      */   protected void checkSupportingBlock(boolean onGround, Vec3 movement) {
/*  750 */     if (onGround) {
/*  751 */       AABB boundingBox = getBoundingBox();
/*  752 */       AABB testArea = new AABB(boundingBox.minX, boundingBox.minY - 1.0E-6D, boundingBox.minZ, boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
/*  753 */       Optional<BlockPos> supportingBlock = this.level.findSupportingBlock(this, testArea);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  762 */       if (supportingBlock.isPresent() || this.onGroundNoBlocks) {
/*  763 */         this.mainSupportingBlockPos = supportingBlock;
/*  764 */       } else if (movement != null) {
/*      */ 
/*      */         
/*  767 */         AABB onGroundCollisionTestArea = testArea.move(-movement.x, 0.0D, -movement.z);
/*  768 */         supportingBlock = this.level.findSupportingBlock(this, onGroundCollisionTestArea);
/*  769 */         this.mainSupportingBlockPos = supportingBlock;
/*      */       } 
/*  771 */       this.onGroundNoBlocks = supportingBlock.isEmpty();
/*      */     } else {
/*  773 */       this.onGroundNoBlocks = false;
/*  774 */       if (this.mainSupportingBlockPos.isPresent()) {
/*  775 */         this.mainSupportingBlockPos = Optional.empty();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  781 */   public boolean onGround() { return this.onGround; }
/*      */ 
/*      */   
/*      */   public void move(MoverType moverType, Vec3 delta) {
/*  785 */     if (this.noPhysics) {
/*  786 */       setPos(getX() + delta.x, getY() + delta.y, getZ() + delta.z);
/*  787 */       this.horizontalCollision = false;
/*  788 */       this.verticalCollision = false;
/*  789 */       this.verticalCollisionBelow = false;
/*  790 */       this.minorHorizontalCollision = false;
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  795 */     if (moverType == MoverType.PISTON) {
/*  796 */       delta = limitPistonMovement(delta);
/*  797 */       if (delta.equals(Vec3.ZERO)) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */     
/*  802 */     ProfilerFiller profiler = Profiler.get();
/*  803 */     profiler.push("move");
/*      */     
/*  805 */     if (this.stuckSpeedMultiplier.lengthSqr() > 1.0E-7D) {
/*  806 */       if (moverType != MoverType.PISTON) {
/*  807 */         delta = delta.multiply(this.stuckSpeedMultiplier);
/*      */       }
/*  809 */       this.stuckSpeedMultiplier = Vec3.ZERO;
/*  810 */       setDeltaMovement(Vec3.ZERO);
/*      */     } 
/*      */     
/*  813 */     delta = maybeBackOffFromEdge(delta, moverType);
/*      */     
/*  815 */     Vec3 movement = collide(delta);
/*  816 */     double movementLength = movement.lengthSqr();
/*  817 */     if (movementLength > 1.0E-7D || delta.lengthSqr() - movementLength < 1.0E-7D) {
/*  818 */       if (this.fallDistance != 0.0D && movementLength >= 1.0D) {
/*      */         
/*  820 */         double checkDistance = Math.min(movement.length(), 8.0D);
/*  821 */         Vec3 checkTo = position().add(movement.normalize().scale(checkDistance));
/*  822 */         BlockHitResult hitResult = level().clip(new ClipContext(position(), checkTo, ClipContext.Block.FALLDAMAGE_RESETTING, ClipContext.Fluid.WATER, this));
/*  823 */         if (hitResult.getType() != HitResult.Type.MISS) {
/*  824 */           resetFallDistance();
/*      */         }
/*      */       } 
/*      */       
/*  828 */       Vec3 pos = position();
/*  829 */       Vec3 newPosition = pos.add(movement);
/*  830 */       addMovementThisTick(new Movement(pos, newPosition, delta));
/*  831 */       setPos(newPosition);
/*      */     } 
/*      */     
/*  834 */     profiler.pop();
/*  835 */     profiler.push("rest");
/*      */ 
/*      */     
/*  838 */     boolean xCollision = !Mth.equal(delta.x, movement.x);
/*  839 */     boolean zCollision = !Mth.equal(delta.z, movement.z);
/*  840 */     this.horizontalCollision = (xCollision || zCollision);
/*      */     
/*  842 */     if (Math.abs(delta.y) > 0.0D || isLocalInstanceAuthoritative()) {
/*      */       
/*  844 */       this.verticalCollision = (delta.y != movement.y);
/*  845 */       this.verticalCollisionBelow = (this.verticalCollision && delta.y < 0.0D);
/*  846 */       setOnGroundWithMovement(this.verticalCollisionBelow, this.horizontalCollision, movement);
/*      */     } 
/*      */     
/*  849 */     if (this.horizontalCollision) {
/*  850 */       this.minorHorizontalCollision = isHorizontalCollisionMinor(movement);
/*      */     } else {
/*  852 */       this.minorHorizontalCollision = false;
/*      */     } 
/*      */     
/*  855 */     BlockPos effectPos = getOnPosLegacy();
/*  856 */     BlockState effectState = level().getBlockState(effectPos);
/*      */     
/*  858 */     if (isLocalInstanceAuthoritative())
/*      */     {
/*      */ 
/*      */       
/*  862 */       checkFallDamage(movement.y, onGround(), effectState, effectPos);
/*      */     }
/*      */     
/*  865 */     if (isRemoved()) {
/*  866 */       profiler.pop();
/*      */       
/*      */       return;
/*      */     } 
/*  870 */     if (this.horizontalCollision) {
/*  871 */       Vec3 vec3 = getDeltaMovement();
/*  872 */       setDeltaMovement(xCollision ? 0.0D : vec3.x, vec3.y, zCollision ? 0.0D : vec3.z);
/*      */     } 
/*      */     
/*  875 */     if (canSimulateMovement()) {
/*  876 */       Block onBlock = effectState.getBlock();
/*  877 */       if (delta.y != movement.y) {
/*  878 */         onBlock.updateEntityMovementAfterFallOn(level(), this);
/*      */       }
/*      */     } 
/*      */     
/*  882 */     if (!level().isClientSide() || isLocalInstanceAuthoritative()) {
/*  883 */       MovementEmission emission = getMovementEmission();
/*  884 */       if (emission.emitsAnything() && !isPassenger()) {
/*  885 */         applyMovementEmissionAndPlaySound(emission, movement, effectPos, effectState);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  892 */     float blockSpeedFactor = getBlockSpeedFactor();
/*  893 */     setDeltaMovement(getDeltaMovement().multiply(blockSpeedFactor, 1.0D, blockSpeedFactor));
/*      */     
/*  895 */     profiler.pop();
/*      */   }
/*      */   
/*      */   private void applyMovementEmissionAndPlaySound(MovementEmission emission, Vec3 clippedMovement, BlockPos effectPos, BlockState effectState) {
/*  899 */     float moveDistScale = 0.6F;
/*  900 */     float movedDistance = (float)(clippedMovement.length() * 0.6000000238418579D);
/*  901 */     float horizontalMovedDistance = (float)(clippedMovement.horizontalDistance() * 0.6000000238418579D);
/*      */     
/*  903 */     BlockPos supportingPos = getOnPos();
/*  904 */     BlockState supportingState = level().getBlockState(supportingPos);
/*      */     
/*  906 */     boolean climbing = isStateClimbable(supportingState);
/*  907 */     this.moveDist += (climbing ? movedDistance : horizontalMovedDistance);
/*      */ 
/*      */     
/*  910 */     this.flyDist += movedDistance;
/*      */     
/*  912 */     if (this.moveDist > this.nextStep && !supportingState.isAir()) {
/*  913 */       boolean onlyEffectStateEmittions = supportingPos.equals(effectPos);
/*  914 */       boolean producedSideEffects = vibrationAndSoundEffectsFromBlock(effectPos, effectState, emission.emitsSounds(), onlyEffectStateEmittions, clippedMovement);
/*  915 */       if (!onlyEffectStateEmittions) {
/*  916 */         producedSideEffects |= vibrationAndSoundEffectsFromBlock(supportingPos, supportingState, false, emission.emitsEvents(), clippedMovement);
/*      */       }
/*  918 */       if (producedSideEffects) {
/*  919 */         this.nextStep = nextStep();
/*  920 */       } else if (isInWater()) {
/*  921 */         this.nextStep = nextStep();
/*  922 */         if (emission.emitsSounds()) {
/*  923 */           waterSwimSound();
/*      */         }
/*  925 */         if (emission.emitsEvents()) {
/*  926 */           gameEvent(GameEvent.SWIM);
/*      */         }
/*      */       } 
/*  929 */     } else if (supportingState.isAir()) {
/*  930 */       processFlappingMovement();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void applyEffectsFromBlocks() {
/*  935 */     this.finalMovementsThisTick.clear();
/*  936 */     this.finalMovementsThisTick.addAll(this.movementThisTick);
/*  937 */     this.movementThisTick.clear();
/*      */     
/*  939 */     if (this.finalMovementsThisTick.isEmpty()) {
/*      */       
/*  941 */       this.finalMovementsThisTick.add(new Movement(oldPosition(), position()));
/*  942 */     } else if (((Movement)this.finalMovementsThisTick.getLast()).to.distanceToSqr(position()) > 9.999999439624929E-11D) {
/*      */       
/*  944 */       this.finalMovementsThisTick.add(new Movement(((Movement)this.finalMovementsThisTick.getLast()).to, position()));
/*      */     } 
/*      */     
/*  947 */     applyEffectsFromBlocks(this.finalMovementsThisTick);
/*      */   }
/*      */   
/*      */   private void addMovementThisTick(Movement movement) {
/*  951 */     if (this.movementThisTick.size() >= 100) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  959 */       Movement first = (Movement)this.movementThisTick.removeFirst();
/*  960 */       Movement second = (Movement)this.movementThisTick.removeFirst();
/*  961 */       Movement combined = new Movement(first.from(), second.to());
/*  962 */       this.movementThisTick.addFirst(combined);
/*      */     } 
/*  964 */     this.movementThisTick.add(movement);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeLatestMovementRecording() {
/*  971 */     if (!this.movementThisTick.isEmpty()) {
/*  972 */       this.movementThisTick.removeLast();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  977 */   protected void clearMovementThisTick() { this.movementThisTick.clear(); }
/*      */ 
/*      */ 
/*      */   
/*  981 */   public boolean hasMovedHorizontallyRecently() { return (Math.abs(this.lastKnownSpeed.horizontalDistance()) > 9.999999747378752E-6D); }
/*      */ 
/*      */ 
/*      */   
/*  985 */   public void applyEffectsFromBlocks(Vec3 from, Vec3 to) { applyEffectsFromBlocks(List.of(new Movement(from, to))); }
/*      */ 
/*      */   
/*      */   private void applyEffectsFromBlocks(List<Movement> movements) {
/*  989 */     if (!isAffectedByBlocks()) {
/*      */       return;
/*      */     }
/*      */     
/*  993 */     if (onGround()) {
/*  994 */       BlockPos effectPos = getOnPosLegacy();
/*  995 */       BlockState effectState = level().getBlockState(effectPos);
/*  996 */       effectState.getBlock().stepOn(level(), effectPos, effectState, this);
/*      */     } 
/*      */     
/*  999 */     boolean wasOnFire = isOnFire();
/* 1000 */     boolean wasFreezing = isFreezing();
/* 1001 */     int previousRemainingFireTicks = getRemainingFireTicks();
/* 1002 */     checkInsideBlocks(movements, this.insideEffectCollector);
/* 1003 */     this.insideEffectCollector.applyAndClear(this);
/*      */     
/* 1005 */     if (isInRain()) {
/* 1006 */       clearFire();
/*      */     }
/*      */     
/* 1009 */     if ((wasOnFire && !isOnFire()) || (wasFreezing && 
/* 1010 */       !isFreezing())) {
/* 1011 */       playEntityOnFireExtinguishedSound();
/*      */     }
/*      */     
/* 1014 */     boolean wasIgnitedThisTick = (getRemainingFireTicks() > previousRemainingFireTicks);
/* 1015 */     if (!level().isClientSide() && !isOnFire() && !wasIgnitedThisTick)
/*      */     {
/* 1017 */       setRemainingFireTicks(-getFireImmuneTicks());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 1022 */   protected boolean isAffectedByBlocks() { return (!isRemoved() && !this.noPhysics); }
/*      */ 
/*      */ 
/*      */   
/* 1026 */   private boolean isStateClimbable(BlockState state) { return (state.is(BlockTags.CLIMBABLE) || state.is(Blocks.POWDER_SNOW)); }
/*      */ 
/*      */   
/*      */   private boolean vibrationAndSoundEffectsFromBlock(BlockPos pos, BlockState blockState, boolean shouldSound, boolean shouldVibrate, Vec3 clippedMovement) {
/* 1030 */     if (blockState.isAir()) {
/* 1031 */       return false;
/*      */     }
/* 1033 */     boolean isClimbable = isStateClimbable(blockState);
/* 1034 */     if ((onGround() || isClimbable || (isCrouching() && clippedMovement.y == 0.0D) || isOnRails()) && !isSwimming()) {
/* 1035 */       if (shouldSound) {
/* 1036 */         walkingStepSound(pos, blockState);
/*      */       }
/* 1038 */       if (shouldVibrate) {
/* 1039 */         level().gameEvent(GameEvent.STEP, position(), GameEvent.Context.of(this, blockState));
/*      */       }
/* 1041 */       return true;
/*      */     } 
/* 1043 */     return false;
/*      */   }
/*      */ 
/*      */   
/* 1047 */   protected boolean isHorizontalCollisionMinor(Vec3 movement) { return false; }
/*      */ 
/*      */   
/*      */   protected void playEntityOnFireExtinguishedSound() {
/* 1051 */     if (!this.level.isClientSide()) {
/* 1052 */       level().playSound(null, getX(), getY(), getZ(), SoundEvents.GENERIC_EXTINGUISH_FIRE, getSoundSource(), 0.7F, 1.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
/*      */     }
/*      */   }
/*      */   
/*      */   public void extinguishFire() {
/* 1057 */     if (isOnFire()) {
/* 1058 */       playEntityOnFireExtinguishedSound();
/*      */     }
/* 1060 */     clearFire();
/*      */   }
/*      */   
/*      */   protected void processFlappingMovement() {
/* 1064 */     if (isFlapping()) {
/* 1065 */       onFlap();
/* 1066 */       if (getMovementEmission().emitsEvents()) {
/* 1067 */         gameEvent(GameEvent.FLAP);
/*      */       }
/*      */     } 
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
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/* 1084 */   public BlockPos getOnPosLegacy() { return getOnPos(0.2F); }
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
/* 1095 */   public BlockPos getBlockPosBelowThatAffectsMyMovement() { return getOnPos(0.500001F); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1105 */   public BlockPos getOnPos() { return getOnPos(1.0E-5F); }
/*      */ 
/*      */   
/*      */   protected BlockPos getOnPos(float offset) {
/* 1109 */     if (this.mainSupportingBlockPos.isPresent()) {
/* 1110 */       BlockPos getOnPos = (BlockPos)this.mainSupportingBlockPos.get();
/* 1111 */       if (offset > 1.0E-5F) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1118 */         BlockState belowState = level().getBlockState(getOnPos);
/*      */         
/* 1120 */         if ((offset <= 0.5D && belowState.is(BlockTags.FENCES)) || belowState.is(BlockTags.WALLS) || belowState.getBlock() instanceof net.minecraft.world.level.block.FenceGateBlock) {
/* 1121 */           return getOnPos;
/*      */         }
/* 1123 */         return getOnPos.atY(Mth.floor(this.position.y - offset));
/*      */       } 
/* 1125 */       return getOnPos;
/*      */     } 
/* 1127 */     int xTruncated = Mth.floor(this.position.x);
/* 1128 */     int yTruncatedBelow = Mth.floor(this.position.y - offset);
/* 1129 */     int zTruncated = Mth.floor(this.position.z);
/*      */     
/* 1131 */     return new BlockPos(xTruncated, yTruncatedBelow, zTruncated);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected float getBlockJumpFactor() {
/* 1140 */     float jumpFactorHere = level().getBlockState(blockPosition()).getBlock().getJumpFactor();
/* 1141 */     float jumpFactorBelow = level().getBlockState(getBlockPosBelowThatAffectsMyMovement()).getBlock().getJumpFactor();
/* 1142 */     return (jumpFactorHere == 1.0D) ? jumpFactorBelow : jumpFactorHere;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected float getBlockSpeedFactor() {
/* 1151 */     BlockState state = level().getBlockState(blockPosition());
/* 1152 */     float speedFactorHere = state.getBlock().getSpeedFactor();
/* 1153 */     if (state.is(Blocks.WATER) || state.is(Blocks.BUBBLE_COLUMN)) {
/* 1154 */       return speedFactorHere;
/*      */     }
/* 1156 */     return (speedFactorHere == 1.0D) ? level().getBlockState(getBlockPosBelowThatAffectsMyMovement()).getBlock().getSpeedFactor() : speedFactorHere;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1163 */   protected Vec3 maybeBackOffFromEdge(Vec3 delta, MoverType moverType) { return delta; }
/*      */ 
/*      */ 
/*      */   
/*      */   protected Vec3 limitPistonMovement(Vec3 vec) {
/* 1168 */     if (vec.lengthSqr() <= 1.0E-7D) {
/* 1169 */       return vec;
/*      */     }
/*      */     
/* 1172 */     long currentGameTime = level().getGameTime();
/* 1173 */     if (currentGameTime != this.pistonDeltasGameTime) {
/* 1174 */       Arrays.fill(this.pistonDeltas, 0.0D);
/* 1175 */       this.pistonDeltasGameTime = currentGameTime;
/*      */     } 
/*      */     
/* 1178 */     if (vec.x != 0.0D) {
/* 1179 */       double xa = applyPistonMovementRestriction(Direction.Axis.X, vec.x);
/* 1180 */       return (Math.abs(xa) <= 9.999999747378752E-6D) ? Vec3.ZERO : new Vec3(xa, 0.0D, 0.0D);
/*      */     } 
/* 1182 */     if (vec.y != 0.0D) {
/* 1183 */       double ya = applyPistonMovementRestriction(Direction.Axis.Y, vec.y);
/* 1184 */       return (Math.abs(ya) <= 9.999999747378752E-6D) ? Vec3.ZERO : new Vec3(0.0D, ya, 0.0D);
/*      */     } 
/* 1186 */     if (vec.z != 0.0D) {
/* 1187 */       double za = applyPistonMovementRestriction(Direction.Axis.Z, vec.z);
/* 1188 */       return (Math.abs(za) <= 9.999999747378752E-6D) ? Vec3.ZERO : new Vec3(0.0D, 0.0D, za);
/*      */     } 
/*      */     
/* 1191 */     return Vec3.ZERO;
/*      */   }
/*      */   
/*      */   private double applyPistonMovementRestriction(Direction.Axis axis, double amount) {
/* 1195 */     int ordinal = axis.ordinal();
/* 1196 */     double min = Mth.clamp(amount + this.pistonDeltas[ordinal], -0.51D, 0.51D);
/* 1197 */     amount = min - this.pistonDeltas[ordinal];
/* 1198 */     this.pistonDeltas[ordinal] = min;
/* 1199 */     return amount;
/*      */   }
/*      */   
/*      */   public double getAvailableSpaceBelow(double maxDistance) {
/* 1203 */     AABB aabb = getBoundingBox();
/* 1204 */     AABB below = aabb.setMinY(aabb.minY - maxDistance).setMaxY(aabb.minY);
/* 1205 */     List<VoxelShape> colliders = collectAllColliders(this, this.level, below);
/*      */     
/* 1207 */     if (colliders.isEmpty()) {
/* 1208 */       return maxDistance;
/*      */     }
/* 1210 */     return -Shapes.collide(Direction.Axis.Y, aabb, colliders, -maxDistance);
/*      */   }
/*      */   
/*      */   private Vec3 collide(Vec3 movement) {
/* 1214 */     AABB aabb = getBoundingBox();
/*      */ 
/*      */     
/* 1217 */     List<VoxelShape> entityColliders = level().getEntityCollisions(this, aabb.expandTowards(movement));
/* 1218 */     Vec3 movementStep = (movement.lengthSqr() == 0.0D) ? movement : collideBoundingBox(this, movement, aabb, level(), entityColliders);
/*      */ 
/*      */     
/* 1221 */     boolean xCollision = (movement.x != movementStep.x);
/* 1222 */     boolean yCollision = (movement.y != movementStep.y);
/* 1223 */     boolean zCollision = (movement.z != movementStep.z);
/*      */     
/* 1225 */     boolean onGroundAfterCollision = (yCollision && movement.y < 0.0D);
/*      */     
/* 1227 */     if (maxUpStep() > 0.0F && (onGroundAfterCollision || onGround()) && (xCollision || zCollision)) {
/*      */       
/* 1229 */       AABB groundedAABB = onGroundAfterCollision ? aabb.move(0.0D, movementStep.y, 0.0D) : aabb;
/*      */       
/* 1231 */       AABB stepUpAABB = groundedAABB.expandTowards(movement.x, maxUpStep(), movement.z);
/*      */ 
/*      */       
/* 1234 */       if (!onGroundAfterCollision) {
/* 1235 */         stepUpAABB = stepUpAABB.expandTowards(0.0D, -9.999999747378752E-6D, 0.0D);
/*      */       }
/*      */       
/* 1238 */       List<VoxelShape> colliders = collectColliders(this, this.level, entityColliders, stepUpAABB);
/*      */ 
/*      */       
/* 1241 */       float stepHeightToSkip = (float)movementStep.y;
/* 1242 */       float[] candidateStepUpHeights = collectCandidateStepUpHeights(groundedAABB, colliders, maxUpStep(), stepHeightToSkip);
/*      */       
/* 1244 */       for (float candidateStepUpHeight : candidateStepUpHeights) {
/* 1245 */         Vec3 stepFromGround = collideWithShapes(new Vec3(movement.x, candidateStepUpHeight, movement.z), groundedAABB, colliders);
/*      */         
/* 1247 */         if (stepFromGround.horizontalDistanceSqr() > movementStep.horizontalDistanceSqr()) {
/*      */           
/* 1249 */           double distanceToGround = aabb.minY - groundedAABB.minY;
/* 1250 */           return stepFromGround.subtract(0.0D, distanceToGround, 0.0D);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1254 */     return movementStep;
/*      */   }
/*      */ 
/*      */   
/*      */   private static float[] collectCandidateStepUpHeights(AABB boundingBox, List<VoxelShape> colliders, float maxStepHeight, float stepHeightToSkip) {
/* 1259 */     FloatArraySet floatArraySet = new FloatArraySet(4);
/* 1260 */     for (VoxelShape collider : colliders) {
/* 1261 */       DoubleList coords = collider.getCoords(Direction.Axis.Y);
/* 1262 */       for (DoubleListIterator doubleListIterator = coords.iterator(); doubleListIterator.hasNext(); ) { double coord = ((Double)doubleListIterator.next()).doubleValue();
/* 1263 */         float relativeCoord = (float)(coord - boundingBox.minY);
/*      */         
/* 1265 */         if (relativeCoord < 0.0F)
/*      */           continue; 
/* 1267 */         if (relativeCoord == stepHeightToSkip)
/*      */           continue; 
/* 1269 */         if (relativeCoord > maxStepHeight) {
/*      */           break;
/*      */         }
/* 1272 */         floatArraySet.add(relativeCoord); }
/*      */     
/*      */     } 
/*      */     
/* 1276 */     float[] sortedCandidates = floatArraySet.toFloatArray();
/* 1277 */     FloatArrays.unstableSort(sortedCandidates);
/* 1278 */     return sortedCandidates;
/*      */   }
/*      */   
/*      */   public static Vec3 collideBoundingBox(Entity source, Vec3 movement, AABB boundingBox, Level level, List<VoxelShape> entityColliders) {
/* 1282 */     List<VoxelShape> colliders = collectColliders(source, level, entityColliders, boundingBox.expandTowards(movement));
/* 1283 */     return collideWithShapes(movement, boundingBox, colliders);
/*      */   }
/*      */   
/*      */   public static List<VoxelShape> collectAllColliders(Entity source, Level level, AABB boundingBox) {
/* 1287 */     List<VoxelShape> entityColliders = level.getEntityCollisions(source, boundingBox);
/* 1288 */     return collectColliders(source, level, entityColliders, boundingBox);
/*      */   }
/*      */   
/*      */   private static List<VoxelShape> collectColliders(Entity source, Level level, List<VoxelShape> entityColliders, AABB boundingBox) {
/* 1292 */     ImmutableList.Builder<VoxelShape> colliders = ImmutableList.builderWithExpectedSize(entityColliders.size() + 1);
/*      */     
/* 1294 */     if (!entityColliders.isEmpty()) {
/* 1295 */       colliders.addAll(entityColliders);
/*      */     }
/*      */     
/* 1298 */     WorldBorder worldBorder = level.getWorldBorder();
/* 1299 */     boolean isEntityInsideCloseToBorder = (source != null && worldBorder.isInsideCloseToBorder(source, boundingBox));
/*      */     
/* 1301 */     if (isEntityInsideCloseToBorder) {
/* 1302 */       colliders.add(worldBorder.getCollisionShape());
/*      */     }
/*      */     
/* 1305 */     colliders.addAll(level.getBlockCollisions(source, boundingBox));
/* 1306 */     return colliders.build();
/*      */   }
/*      */   
/*      */   private static Vec3 collideWithShapes(Vec3 movement, AABB boundingBox, List<VoxelShape> shapes) {
/* 1310 */     if (shapes.isEmpty()) {
/* 1311 */       return movement;
/*      */     }
/*      */     
/* 1314 */     Vec3 resolvedMovement = Vec3.ZERO;
/* 1315 */     for (UnmodifiableIterator unmodifiableIterator = Direction.axisStepOrder(movement).iterator(); unmodifiableIterator.hasNext(); ) { Direction.Axis axis = (Direction.Axis)unmodifiableIterator.next();
/* 1316 */       double axisMovement = movement.get(axis);
/* 1317 */       if (axisMovement == 0.0D) {
/*      */         continue;
/*      */       }
/* 1320 */       double collision = Shapes.collide(axis, boundingBox.move(resolvedMovement), shapes, axisMovement);
/* 1321 */       resolvedMovement = resolvedMovement.with(axis, collision); }
/*      */     
/* 1323 */     return resolvedMovement;
/*      */   }
/*      */ 
/*      */   
/* 1327 */   protected float nextStep() { return ((int)this.moveDist + 1); }
/*      */ 
/*      */ 
/*      */   
/* 1331 */   protected SoundEvent getSwimSound() { return SoundEvents.GENERIC_SWIM; }
/*      */ 
/*      */ 
/*      */   
/* 1335 */   protected SoundEvent getSwimSplashSound() { return SoundEvents.GENERIC_SPLASH; }
/*      */ 
/*      */ 
/*      */   
/* 1339 */   protected SoundEvent getSwimHighSpeedSplashSound() { return SoundEvents.GENERIC_SPLASH; }
/*      */ 
/*      */   
/*      */   private void checkInsideBlocks(List<Movement> movements, InsideBlockEffectApplier.StepBasedCollector effectCollector) {
/* 1343 */     if (!isAffectedByBlocks()) {
/*      */       return;
/*      */     }
/*      */     
/* 1347 */     LongSet visitedBlocks = this.visitedBlocks;
/* 1348 */     for (Movement movement : movements) {
/* 1349 */       Vec3 pos = movement.from;
/* 1350 */       Vec3 delta = movement.to().subtract(movement.from());
/* 1351 */       int maxMovementIterations = 16;
/* 1352 */       if (movement.axisDependentOriginalMovement().isPresent() && delta.lengthSqr() > 0.0D) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1359 */         for (UnmodifiableIterator unmodifiableIterator = Direction.axisStepOrder((Vec3)movement.axisDependentOriginalMovement().get()).iterator(); unmodifiableIterator.hasNext(); ) { Direction.Axis axis = (Direction.Axis)unmodifiableIterator.next();
/* 1360 */           double axisMove = delta.get(axis);
/* 1361 */           if (axisMove != 0.0D) {
/* 1362 */             Vec3 to = pos.relative(axis.getPositive(), axisMove);
/* 1363 */             maxMovementIterations -= checkInsideBlocks(pos, to, effectCollector, visitedBlocks, maxMovementIterations);
/* 1364 */             pos = to;
/*      */           }  }
/*      */       
/*      */       } else {
/* 1368 */         maxMovementIterations -= checkInsideBlocks(movement.from(), movement.to(), effectCollector, visitedBlocks, 16);
/*      */       } 
/*      */       
/* 1371 */       if (maxMovementIterations <= 0)
/*      */       {
/* 1373 */         checkInsideBlocks(movement.to(), movement.to(), effectCollector, visitedBlocks, 1);
/*      */       }
/*      */     } 
/* 1376 */     visitedBlocks.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int checkInsideBlocks(Vec3 from, Vec3 to, InsideBlockEffectApplier.StepBasedCollector effectCollector, LongSet visitedBlocks, int maxMovementIterations) {
/* 1382 */     AABB deflatedBoundingBoxAtTarget = makeBoundingBox(to).deflate(9.999999747378752E-6D);
/*      */     
/* 1384 */     boolean movedFar = (from.distanceToSqr(to) > Mth.square(0.9999900000002526D));
/* 1385 */     Level level1 = this.level; if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1; if (serverLevel.getServer().debugSubscribers().hasAnySubscriberFor(DebugSubscriptions.ENTITY_BLOCK_INTERSECTIONS)); }  boolean debugEntityBlockIntersections = false;
/*      */     
/* 1387 */     AtomicInteger iterations = new AtomicInteger();
/* 1388 */     BlockGetter.forEachBlockIntersectedBetween(from, to, deflatedBoundingBoxAtTarget, (blockIntersection, iteration) -> {
/* 1389 */           if (!isAlive()) {
/* 1390 */             return false;
/*      */           }
/*      */           
/* 1393 */           if (iteration >= maxMovementIterations)
/*      */           {
/* 1395 */             return false;
/*      */           }
/* 1397 */           iterations.set(iteration);
/*      */ 
/*      */           
/* 1400 */           BlockState state = level().getBlockState(blockIntersection);
/* 1401 */           if (state.isAir()) {
/* 1402 */             if (debugEntityBlockIntersections) {
/* 1403 */               debugBlockIntersection((ServerLevel)level(), blockIntersection.immutable(), false, false);
/*      */             }
/* 1405 */             return true;
/*      */           } 
/*      */           
/* 1408 */           VoxelShape intersectShape = state.getEntityInsideCollisionShape(level(), blockIntersection, this);
/*      */           
/* 1410 */           boolean insideBlock = (intersectShape == Shapes.block() || collidedWithShapeMovingFrom(from, to, intersectShape.move(new Vec3(blockIntersection)).toAabbs()));
/* 1411 */           boolean insideFluid = collidedWithFluid(state.getFluidState(), blockIntersection, from, to);
/*      */           
/* 1413 */           if ((!insideBlock && !insideFluid) || !visitedBlocks.add(blockIntersection.asLong())) {
/* 1414 */             return true;
/*      */           }
/*      */           
/* 1417 */           if (insideBlock) {
/*      */             
/*      */             try {
/*      */               
/* 1421 */               boolean isPrecise = (movedFar || deflatedBoundingBoxAtTarget.intersects(blockIntersection));
/* 1422 */               effectCollector.advanceStep(iteration);
/*      */               
/* 1424 */               state.entityInside(level(), blockIntersection, this, effectCollector, isPrecise);
/* 1425 */               onInsideBlock(state);
/* 1426 */             } catch (Throwable t) {
/* 1427 */               CrashReport report = CrashReport.forThrowable(t, "Colliding entity with block");
/*      */               
/* 1429 */               CrashReportCategory category = report.addCategory("Block being collided with");
/* 1430 */               CrashReportCategory.populateBlockDetails(category, level(), blockIntersection, state);
/*      */               
/* 1432 */               CrashReportCategory entityCategory = report.addCategory("Entity being checked for collision");
/* 1433 */               fillCrashReportCategory(entityCategory);
/*      */               
/* 1435 */               throw new ReportedException(report);
/*      */             } 
/*      */           }
/*      */           
/* 1439 */           if (insideFluid) {
/* 1440 */             effectCollector.advanceStep(iteration);
/* 1441 */             state.getFluidState().entityInside(level(), blockIntersection, this, effectCollector);
/*      */           } 
/*      */           
/* 1444 */           if (debugEntityBlockIntersections) {
/* 1445 */             debugBlockIntersection((ServerLevel)level(), blockIntersection.immutable(), insideBlock, insideFluid);
/*      */           }
/*      */           
/* 1448 */           return true;
/*      */         });
/*      */     
/* 1451 */     return iterations.get() + 1;
/*      */   }
/*      */   
/*      */   private void debugBlockIntersection(ServerLevel level, BlockPos pos, boolean insideBlock, boolean insideFluid) {
/*      */     DebugEntityBlockIntersection type;
/* 1456 */     if (insideFluid) {
/* 1457 */       type = DebugEntityBlockIntersection.IN_FLUID;
/* 1458 */     } else if (insideBlock) {
/* 1459 */       type = DebugEntityBlockIntersection.IN_BLOCK;
/*      */     } else {
/* 1461 */       type = DebugEntityBlockIntersection.IN_AIR;
/*      */     } 
/* 1463 */     level.debugSynchronizers().sendBlockValue(pos, DebugSubscriptions.ENTITY_BLOCK_INTERSECTIONS, type);
/*      */   }
/*      */   
/*      */   public boolean collidedWithFluid(FluidState fluidState, BlockPos blockPos, Vec3 from, Vec3 to) {
/* 1467 */     AABB fluidAABB = fluidState.getAABB(level(), blockPos);
/* 1468 */     return (fluidAABB != null && collidedWithShapeMovingFrom(from, to, List.of(fluidAABB)));
/*      */   }
/*      */   
/*      */   public boolean collidedWithShapeMovingFrom(Vec3 from, Vec3 to, List<AABB> aabbs) {
/* 1472 */     AABB boundingBoxAtFrom = makeBoundingBox(from);
/* 1473 */     Vec3 travelVector = to.subtract(from);
/* 1474 */     return boundingBoxAtFrom.collidedAlongVector(travelVector, aabbs);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onInsideBlock(BlockState state) {}
/*      */   
/*      */   public BlockPos adjustSpawnLocation(ServerLevel level, BlockPos spawnSuggestion) {
/* 1481 */     BlockPos spawnBlockPos = level.getRespawnData().pos();
/* 1482 */     Vec3 spawnPos = spawnBlockPos.getCenter();
/* 1483 */     int spawnHeight = level.getChunkAt(spawnBlockPos).getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, spawnBlockPos.getX(), spawnBlockPos.getZ()) + 1;
/* 1484 */     return BlockPos.containing(spawnPos.x, spawnHeight, spawnPos.z);
/*      */   }
/*      */ 
/*      */   
/* 1488 */   public void gameEvent(Holder<GameEvent> event, Entity sourceEntity) { level().gameEvent(sourceEntity, event, this.position); }
/*      */ 
/*      */ 
/*      */   
/* 1492 */   public void gameEvent(Holder<GameEvent> event) { gameEvent(event, this); }
/*      */ 
/*      */   
/*      */   private void walkingStepSound(BlockPos onPos, BlockState onState) {
/* 1496 */     playStepSound(onPos, onState);
/*      */     
/* 1498 */     if (shouldPlayAmethystStepSound(onState)) {
/* 1499 */       playAmethystStepSound();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void waterSwimSound() {
/* 1504 */     Entity entity = (Entity)Objects.requireNonNullElse(getControllingPassenger(), this);
/* 1505 */     float volumeModifier = (entity == this) ? 0.35F : 0.4F;
/* 1506 */     Vec3 deltaMovement = entity.getDeltaMovement();
/* 1507 */     float speed = Math.min(1.0F, (float)Math.sqrt(deltaMovement.x * deltaMovement.x * 0.20000000298023224D + deltaMovement.y * deltaMovement.y + deltaMovement.z * deltaMovement.z * 0.20000000298023224D) * volumeModifier);
/* 1508 */     playSwimSound(speed);
/*      */   }
/*      */   
/*      */   protected BlockPos getPrimaryStepSoundBlockPos(BlockPos affectingPos) {
/* 1512 */     BlockPos abovePos = affectingPos.above();
/* 1513 */     BlockState aboveState = level().getBlockState(abovePos);
/* 1514 */     if (aboveState.is(BlockTags.INSIDE_STEP_SOUND_BLOCKS) || aboveState.is(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)) {
/* 1515 */       return abovePos;
/*      */     }
/* 1517 */     return affectingPos;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void playCombinationStepSounds(BlockState primaryStepSound, BlockState secondaryStepSound) {
/* 1522 */     SoundType primaryStepSoundType = primaryStepSound.getSoundType();
/* 1523 */     playSound(primaryStepSoundType.getStepSound(), primaryStepSoundType.getVolume() * 0.15F, primaryStepSoundType.getPitch());
/* 1524 */     playMuffledStepSound(secondaryStepSound);
/*      */   }
/*      */   
/*      */   protected void playMuffledStepSound(BlockState blockState) {
/* 1528 */     SoundType secondaryStepSoundType = blockState.getSoundType();
/* 1529 */     playSound(secondaryStepSoundType.getStepSound(), secondaryStepSoundType.getVolume() * 0.05F, secondaryStepSoundType.getPitch() * 0.8F);
/*      */   }
/*      */   
/*      */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 1533 */     SoundType soundType = blockState.getSoundType();
/* 1534 */     playSound(soundType.getStepSound(), soundType.getVolume() * 0.15F, soundType.getPitch());
/*      */   }
/*      */ 
/*      */   
/* 1538 */   private boolean shouldPlayAmethystStepSound(BlockState affectingState) { return (affectingState.is(BlockTags.CRYSTAL_SOUND_BLOCKS) && this.tickCount >= this.lastCrystalSoundPlayTick + 20); }
/*      */ 
/*      */ 
/*      */   
/*      */   private void playAmethystStepSound() {
/* 1543 */     this.crystalSoundIntensity *= (float)Math.pow(0.997D, (this.tickCount - this.lastCrystalSoundPlayTick));
/* 1544 */     this.crystalSoundIntensity = Math.min(1.0F, this.crystalSoundIntensity + 0.07F);
/*      */     
/* 1546 */     float pitch = 0.5F + this.crystalSoundIntensity * this.random.nextFloat() * 1.2F;
/* 1547 */     float volume = 0.1F + this.crystalSoundIntensity * 1.2F;
/* 1548 */     playSound(SoundEvents.AMETHYST_BLOCK_CHIME, volume, pitch);
/* 1549 */     this.lastCrystalSoundPlayTick = this.tickCount;
/*      */   }
/*      */ 
/*      */   
/* 1553 */   protected void playSwimSound(float volume) { playSound(getSwimSound(), volume, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onFlap() {}
/*      */ 
/*      */   
/* 1560 */   protected boolean isFlapping() { return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void playSound(SoundEvent sound, float volume, float pitch) {
/* 1568 */     if (!isSilent()) {
/* 1569 */       level().playSound(null, getX(), getY(), getZ(), sound, getSoundSource(), volume, pitch);
/*      */     }
/*      */   }
/*      */   
/*      */   public void playSound(SoundEvent sound) {
/* 1574 */     if (!isSilent()) {
/* 1575 */       playSound(sound, 1.0F, 1.0F);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 1580 */   public boolean isSilent() { return ((Boolean)this.entityData.get(DATA_SILENT)).booleanValue(); }
/*      */ 
/*      */ 
/*      */   
/* 1584 */   public void setSilent(boolean silent) { this.entityData.set(DATA_SILENT, Boolean.valueOf(silent)); }
/*      */ 
/*      */ 
/*      */   
/* 1588 */   public boolean isNoGravity() { return ((Boolean)this.entityData.get(DATA_NO_GRAVITY)).booleanValue(); }
/*      */ 
/*      */ 
/*      */   
/* 1592 */   public void setNoGravity(boolean noGravity) { this.entityData.set(DATA_NO_GRAVITY, Boolean.valueOf(noGravity)); }
/*      */ 
/*      */ 
/*      */   
/* 1596 */   protected double getDefaultGravity() { return 0.0D; }
/*      */ 
/*      */ 
/*      */   
/* 1600 */   public final double getGravity() { return isNoGravity() ? 0.0D : getDefaultGravity(); }
/*      */ 
/*      */   
/*      */   protected void applyGravity() {
/* 1604 */     double gravity = getGravity();
/* 1605 */     if (gravity != 0.0D) {
/* 1606 */       setDeltaMovement(getDeltaMovement().add(0.0D, -gravity, 0.0D));
/*      */     }
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
/*      */ 
/*      */ 
/*      */   
/* 1621 */   protected MovementEmission getMovementEmission() { return MovementEmission.ALL; }
/*      */ 
/*      */ 
/*      */   
/* 1625 */   public boolean dampensVibrations() { return false; }
/*      */ 
/*      */   
/*      */   public final void doCheckFallDamage(double xa, double ya, double za, boolean onGround) {
/* 1629 */     if (touchingUnloadedChunk()) {
/*      */       return;
/*      */     }
/* 1632 */     checkSupportingBlock(onGround, new Vec3(xa, ya, za));
/* 1633 */     BlockPos pos = getOnPosLegacy();
/* 1634 */     BlockState state = level().getBlockState(pos);
/*      */     
/* 1636 */     checkFallDamage(ya, onGround, state, pos);
/*      */   }
/*      */   
/*      */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
/* 1640 */     if (!isInWater() && ya < 0.0D) {
/* 1641 */       this.fallDistance -= (float)ya;
/*      */     }
/* 1643 */     if (onGround) {
/* 1644 */       if (this.fallDistance > 0.0D) {
/* 1645 */         onState.getBlock().fallOn(level(), onState, pos, this, this.fallDistance);
/*      */ 
/*      */         
/* 1648 */         level().gameEvent(GameEvent.HIT_GROUND, this.position, GameEvent.Context.of(this, (BlockState)this.mainSupportingBlockPos.map(blockPos -> level().getBlockState(blockPos)).orElse(onState)));
/*      */       } 
/* 1650 */       resetFallDistance();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 1655 */   public boolean fireImmune() { return getType().fireImmune(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
/* 1662 */     if (this.type.is(EntityTypeTags.FALL_DAMAGE_IMMUNE)) {
/* 1663 */       return false;
/*      */     }
/* 1665 */     propagateFallToPassengers(fallDistance, damageModifier, damageSource);
/* 1666 */     return false;
/*      */   }
/*      */   
/*      */   protected void propagateFallToPassengers(double fallDistance, float damageModifier, DamageSource damageSource) {
/* 1670 */     if (isVehicle()) {
/* 1671 */       for (Entity passenger : getPassengers()) {
/* 1672 */         passenger.causeFallDamage(fallDistance, damageModifier, damageSource);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 1678 */   public boolean isInWater() { return this.wasTouchingWater; }
/*      */ 
/*      */   
/*      */   boolean isInRain() {
/* 1682 */     BlockPos pos = blockPosition();
/* 1683 */     return (level().isRainingAt(pos) || level().isRainingAt(BlockPos.containing(pos.getX(), (getBoundingBox()).maxY, pos.getZ())));
/*      */   }
/*      */ 
/*      */   
/* 1687 */   public boolean isInWaterOrRain() { return (isInWater() || isInRain()); }
/*      */ 
/*      */ 
/*      */   
/* 1691 */   public boolean isInLiquid() { return (isInWater() || isInLava()); }
/*      */ 
/*      */ 
/*      */   
/* 1695 */   public boolean isUnderWater() { return (this.wasEyeInWater && isInWater()); }
/*      */ 
/*      */ 
/*      */   
/* 1699 */   public boolean isInShallowWater() { return (isInWater() && !isUnderWater()); }
/*      */ 
/*      */   
/*      */   public boolean isInClouds() {
/* 1703 */     if (ARGB.alpha(((Integer)this.level.environmentAttributes().getValue(EnvironmentAttributes.CLOUD_COLOR, position())).intValue()) == 0) {
/* 1704 */       return false;
/*      */     }
/* 1706 */     float cloudBottom = ((Float)this.level.environmentAttributes().getValue(EnvironmentAttributes.CLOUD_HEIGHT, position())).floatValue();
/* 1707 */     if (getY() + getBbHeight() < cloudBottom) {
/* 1708 */       return false;
/*      */     }
/* 1710 */     float cloudTop = cloudBottom + 4.0F;
/* 1711 */     return (getY() <= cloudTop);
/*      */   }
/*      */   
/*      */   public void updateSwimming() {
/* 1715 */     if (isSwimming()) {
/* 1716 */       setSwimming((isSprinting() && isInWater() && !isPassenger()));
/*      */     } else {
/* 1718 */       setSwimming((isSprinting() && isUnderWater() && !isPassenger() && level().getFluidState(this.blockPosition).is(FluidTags.WATER)));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean updateInWaterStateAndDoFluidPushing() {
/* 1726 */     this.fluidHeight.clear();
/* 1727 */     updateInWaterStateAndDoWaterCurrentPushing();
/* 1728 */     double lavaFlowScale = ((Boolean)this.level.environmentAttributes().getDimensionValue(EnvironmentAttributes.FAST_LAVA)).booleanValue() ? 0.007D : 0.0023333333333333335D;
/* 1729 */     boolean isInLava = updateFluidHeightAndDoFluidPushing(FluidTags.LAVA, lavaFlowScale);
/* 1730 */     return (isInWater() || isInLava);
/*      */   }
/*      */   
/*      */   void updateInWaterStateAndDoWaterCurrentPushing() {
/* 1734 */     Entity entity = getVehicle(); if (entity instanceof AbstractBoat) { AbstractBoat boat = (AbstractBoat)entity; if (!boat.isUnderWater())
/* 1735 */       { this.wasTouchingWater = false; return; }  }
/* 1736 */      if (updateFluidHeightAndDoFluidPushing(FluidTags.WATER, 0.014D)) {
/* 1737 */       if (!this.wasTouchingWater && !this.firstTick) {
/* 1738 */         doWaterSplashEffect();
/*      */       }
/* 1740 */       resetFallDistance();
/* 1741 */       this.wasTouchingWater = true;
/*      */     } else {
/* 1743 */       this.wasTouchingWater = false;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateFluidOnEyes() {
/* 1748 */     this.wasEyeInWater = isEyeInFluid(FluidTags.WATER);
/*      */     
/* 1750 */     this.fluidOnEyes.clear();
/* 1751 */     double eyeY = getEyeY();
/* 1752 */     Entity vehicle = getVehicle();
/* 1753 */     if (vehicle instanceof AbstractBoat) { AbstractBoat boat = (AbstractBoat)vehicle;
/* 1754 */       if (!boat.isUnderWater() && (boat.getBoundingBox()).maxY >= eyeY && (boat.getBoundingBox()).minY <= eyeY) {
/*      */         return;
/*      */       } }
/*      */     
/* 1758 */     BlockPos pos = BlockPos.containing(getX(), eyeY, getZ());
/* 1759 */     FluidState fluidState = level().getFluidState(pos);
/*      */     
/* 1761 */     double blockFluidHeight = (pos.getY() + fluidState.getHeight(level(), pos));
/* 1762 */     if (blockFluidHeight > eyeY) {
/* 1763 */       Objects.requireNonNull(this.fluidOnEyes); fluidState.getTags().forEach(this.fluidOnEyes::add);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void doWaterSplashEffect() {
/* 1768 */     Entity entity = (Entity)Objects.requireNonNullElse(getControllingPassenger(), this);
/* 1769 */     float volumeModifier = (entity == this) ? 0.2F : 0.9F;
/* 1770 */     Vec3 movement = entity.getDeltaMovement();
/*      */ 
/*      */     
/* 1773 */     float speed = Math.min(1.0F, (float)Math.sqrt(movement.x * movement.x * 0.20000000298023224D + movement.y * movement.y + movement.z * movement.z * 0.20000000298023224D) * volumeModifier);
/*      */     
/* 1775 */     if (speed < 0.25F) {
/* 1776 */       playSound(getSwimSplashSound(), speed, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
/*      */     } else {
/* 1778 */       playSound(getSwimHighSpeedSplashSound(), speed, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
/*      */     } 
/*      */     
/* 1781 */     float yt = Mth.floor(getY());
/* 1782 */     for (int i = 0; i < 1.0F + this.dimensions.width() * 20.0F; i++) {
/* 1783 */       double xo = (this.random.nextDouble() * 2.0D - 1.0D) * this.dimensions.width();
/* 1784 */       double zo = (this.random.nextDouble() * 2.0D - 1.0D) * this.dimensions.width();
/* 1785 */       level().addParticle(ParticleTypes.BUBBLE, getX() + xo, (yt + 1.0F), getZ() + zo, movement.x, movement.y - this.random.nextDouble() * 0.20000000298023224D, movement.z);
/*      */     } 
/* 1787 */     for (int i = 0; i < 1.0F + this.dimensions.width() * 20.0F; i++) {
/* 1788 */       double xo = (this.random.nextDouble() * 2.0D - 1.0D) * this.dimensions.width();
/* 1789 */       double zo = (this.random.nextDouble() * 2.0D - 1.0D) * this.dimensions.width();
/* 1790 */       level().addParticle(ParticleTypes.SPLASH, getX() + xo, (yt + 1.0F), getZ() + zo, movement.x, movement.y, movement.z);
/*      */     } 
/*      */     
/* 1793 */     gameEvent(GameEvent.SPLASH);
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
/*      */ 
/*      */   
/*      */   @Deprecated
/* 1807 */   protected BlockState getBlockStateOnLegacy() { return level().getBlockState(getOnPosLegacy()); }
/*      */ 
/*      */ 
/*      */   
/* 1811 */   public BlockState getBlockStateOn() { return level().getBlockState(getOnPos()); }
/*      */ 
/*      */ 
/*      */   
/* 1815 */   public boolean canSpawnSprintParticle() { return (isSprinting() && !isInWater() && !isSpectator() && !isCrouching() && !isInLava() && isAlive()); }
/*      */ 
/*      */   
/*      */   protected void spawnSprintParticle() {
/* 1819 */     BlockPos pos = getOnPosLegacy();
/* 1820 */     BlockState blockState = level().getBlockState(pos);
/* 1821 */     if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
/* 1822 */       Vec3 movement = getDeltaMovement();
/* 1823 */       BlockPos entityPosition = blockPosition();
/* 1824 */       double x = getX() + (this.random.nextDouble() - 0.5D) * this.dimensions.width();
/* 1825 */       double z = getZ() + (this.random.nextDouble() - 0.5D) * this.dimensions.width();
/* 1826 */       if (entityPosition.getX() != pos.getX())
/*      */       {
/* 1828 */         x = Mth.clamp(x, pos.getX(), pos.getX() + 1.0D);
/*      */       }
/* 1830 */       if (entityPosition.getZ() != pos.getZ())
/*      */       {
/* 1832 */         z = Mth.clamp(z, pos.getZ(), pos.getZ() + 1.0D);
/*      */       }
/* 1834 */       level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), x, getY() + 0.1D, z, movement.x * -4.0D, 1.5D, movement.z * -4.0D);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 1839 */   public boolean isEyeInFluid(TagKey<Fluid> type) { return this.fluidOnEyes.contains(type); }
/*      */ 
/*      */ 
/*      */   
/* 1843 */   public boolean isInLava() { return (!this.firstTick && this.fluidHeight.getDouble(FluidTags.LAVA) > 0.0D); }
/*      */ 
/*      */   
/*      */   public void moveRelative(float speed, Vec3 input) {
/* 1847 */     Vec3 delta = getInputVector(input, speed, getYRot());
/*      */     
/* 1849 */     setDeltaMovement(getDeltaMovement().add(delta));
/*      */   }
/*      */   
/*      */   protected static Vec3 getInputVector(Vec3 input, float speed, float yRot) {
/* 1853 */     double length = input.lengthSqr();
/* 1854 */     if (length < 1.0E-7D) {
/* 1855 */       return Vec3.ZERO;
/*      */     }
/*      */ 
/*      */     
/* 1859 */     Vec3 movement = ((length > 1.0D) ? input.normalize() : input).scale(speed);
/*      */     
/* 1861 */     float sin = Mth.sin((yRot * 0.017453292F));
/* 1862 */     float cos = Mth.cos((yRot * 0.017453292F));
/* 1863 */     return new Vec3(movement.x * cos - movement.z * sin, movement.y, movement.z * cos + movement.x * sin);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public float getLightLevelDependentMagicValue() {
/* 1869 */     if (level().hasChunkAt(getBlockX(), getBlockZ())) {
/* 1870 */       return level().getLightLevelDependentMagicValue(BlockPos.containing(getX(), getEyeY(), getZ()));
/*      */     }
/* 1872 */     return 0.0F;
/*      */   }
/*      */   
/*      */   public void absSnapTo(double x, double y, double z, float yRot, float xRot) {
/* 1876 */     absSnapTo(x, y, z);
/*      */     
/* 1878 */     absSnapRotationTo(yRot, xRot);
/*      */   }
/*      */   
/*      */   public void absSnapRotationTo(float yRot, float xRot) {
/* 1882 */     setYRot(yRot % 360.0F);
/* 1883 */     setXRot(Mth.clamp(xRot, -90.0F, 90.0F) % 360.0F);
/*      */     
/* 1885 */     this.yRotO = getYRot();
/* 1886 */     this.xRotO = getXRot();
/*      */   }
/*      */   
/*      */   public void absSnapTo(double x, double y, double z) {
/* 1890 */     double cx = Mth.clamp(x, -3.0E7D, 3.0E7D);
/* 1891 */     double cz = Mth.clamp(z, -3.0E7D, 3.0E7D);
/*      */     
/* 1893 */     this.xo = cx;
/* 1894 */     this.yo = y;
/* 1895 */     this.zo = cz;
/*      */     
/* 1897 */     setPos(cx, y, cz);
/*      */   }
/*      */ 
/*      */   
/* 1901 */   public void snapTo(Vec3 pos) { snapTo(pos.x, pos.y, pos.z); }
/*      */ 
/*      */ 
/*      */   
/* 1905 */   public void snapTo(double x, double y, double z) { snapTo(x, y, z, getYRot(), getXRot()); }
/*      */ 
/*      */ 
/*      */   
/* 1909 */   public void snapTo(BlockPos spawnPos, float yRot, float xRot) { snapTo(spawnPos.getBottomCenter(), yRot, xRot); }
/*      */ 
/*      */ 
/*      */   
/* 1913 */   public void snapTo(Vec3 spawnPos, float yRot, float xRot) { snapTo(spawnPos.x, spawnPos.y, spawnPos.z, yRot, xRot); }
/*      */ 
/*      */   
/*      */   public void snapTo(double x, double y, double z, float yRot, float xRot) {
/* 1917 */     setPosRaw(x, y, z);
/* 1918 */     setYRot(yRot);
/* 1919 */     setXRot(xRot);
/*      */     
/* 1921 */     setOldPosAndRot();
/*      */     
/* 1923 */     reapplyPosition();
/*      */   }
/*      */   
/*      */   public final void setOldPosAndRot() {
/* 1927 */     setOldPos();
/* 1928 */     setOldRot();
/*      */   }
/*      */   
/*      */   public final void setOldPosAndRot(Vec3 position, float yRot, float xRot) {
/* 1932 */     setOldPos(position);
/* 1933 */     setOldRot(yRot, xRot);
/*      */   }
/*      */ 
/*      */   
/* 1937 */   protected void setOldPos() { setOldPos(this.position); }
/*      */ 
/*      */ 
/*      */   
/* 1941 */   public void setOldRot() { setOldRot(getYRot(), getXRot()); }
/*      */ 
/*      */   
/*      */   private void setOldPos(Vec3 position) {
/* 1945 */     this.xo = this.xOld = position.x;
/* 1946 */     this.yo = this.yOld = position.y;
/* 1947 */     this.zo = this.zOld = position.z;
/*      */   }
/*      */   
/*      */   private void setOldRot(float yRot, float xRot) {
/* 1951 */     this.yRotO = yRot;
/* 1952 */     this.xRotO = xRot;
/*      */   }
/*      */ 
/*      */   
/* 1956 */   public final Vec3 oldPosition() { return new Vec3(this.xOld, this.yOld, this.zOld); }
/*      */ 
/*      */   
/*      */   public float distanceTo(Entity entity) {
/* 1960 */     float xd = (float)(getX() - entity.getX());
/* 1961 */     float yd = (float)(getY() - entity.getY());
/* 1962 */     float zd = (float)(getZ() - entity.getZ());
/* 1963 */     return Mth.sqrt(xd * xd + yd * yd + zd * zd);
/*      */   }
/*      */   
/*      */   public double distanceToSqr(double x2, double y2, double z2) {
/* 1967 */     double xd = getX() - x2;
/* 1968 */     double yd = getY() - y2;
/* 1969 */     double zd = getZ() - z2;
/* 1970 */     return xd * xd + yd * yd + zd * zd;
/*      */   }
/*      */ 
/*      */   
/* 1974 */   public double distanceToSqr(Entity entity) { return distanceToSqr(entity.position()); }
/*      */ 
/*      */   
/*      */   public double distanceToSqr(Vec3 pos) {
/* 1978 */     double xd = getX() - pos.x;
/* 1979 */     double yd = getY() - pos.y;
/* 1980 */     double zd = getZ() - pos.z;
/* 1981 */     return xd * xd + yd * yd + zd * zd;
/*      */   }
/*      */ 
/*      */   
/*      */   public void playerTouch(Player player) {}
/*      */   
/*      */   public void push(Entity entity) {
/* 1988 */     if (isPassengerOfSameVehicle(entity)) {
/*      */       return;
/*      */     }
/* 1991 */     if (entity.noPhysics || this.noPhysics) {
/*      */       return;
/*      */     }
/*      */     
/* 1995 */     double xa = entity.getX() - getX();
/* 1996 */     double za = entity.getZ() - getZ();
/*      */     
/* 1998 */     double dd = Mth.absMax(xa, za);
/*      */     
/* 2000 */     if (dd >= 0.009999999776482582D) {
/* 2001 */       dd = Math.sqrt(dd);
/* 2002 */       xa /= dd;
/* 2003 */       za /= dd;
/*      */       
/* 2005 */       double pow = 1.0D / dd;
/* 2006 */       if (pow > 1.0D) {
/* 2007 */         pow = 1.0D;
/*      */       }
/* 2009 */       xa *= pow;
/* 2010 */       za *= pow;
/*      */       
/* 2012 */       xa *= 0.05000000074505806D;
/* 2013 */       za *= 0.05000000074505806D;
/*      */       
/* 2015 */       if (!isVehicle() && isPushable()) {
/* 2016 */         push(-xa, 0.0D, -za);
/*      */       }
/* 2018 */       if (!entity.isVehicle() && entity.isPushable()) {
/* 2019 */         entity.push(xa, 0.0D, za);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void push(Vec3 impulse) {
/* 2025 */     if (impulse.isFinite()) {
/* 2026 */       push(impulse.x, impulse.y, impulse.z);
/*      */     }
/*      */   }
/*      */   
/*      */   public void push(double xa, double ya, double za) {
/* 2031 */     if (Double.isFinite(xa) && Double.isFinite(ya) && Double.isFinite(za)) {
/* 2032 */       setDeltaMovement(getDeltaMovement().add(xa, ya, za));
/* 2033 */       this.needsSync = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 2038 */   protected void markHurt() { this.hurtMarked = true; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final void hurt(DamageSource source, float damage) {
/* 2046 */     Level level1 = this.level; if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1;
/* 2047 */       hurtServer(serverLevel, source, damage); }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final boolean hurtOrSimulate(DamageSource source, float damage) {
/* 2058 */     Level level1 = this.level; if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1;
/* 2059 */       return hurtServer(serverLevel, source, damage); }
/*      */     
/* 2061 */     return hurtClient(source);
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
/* 2072 */   public boolean hurtClient(DamageSource source) { return false; }
/*      */ 
/*      */ 
/*      */   
/* 2076 */   public final Vec3 getViewVector(float a) { return calculateViewVector(getViewXRot(a), getViewYRot(a)); }
/*      */ 
/*      */ 
/*      */   
/* 2080 */   public Direction getNearestViewDirection() { return Direction.getApproximateNearest(getViewVector(1.0F)); }
/*      */ 
/*      */ 
/*      */   
/* 2084 */   public float getViewXRot(float a) { return getXRot(a); }
/*      */ 
/*      */ 
/*      */   
/* 2088 */   public float getViewYRot(float a) { return getYRot(a); }
/*      */ 
/*      */   
/*      */   public float getXRot(float partialTicks) {
/* 2092 */     if (partialTicks == 1.0F) {
/* 2093 */       return getXRot();
/*      */     }
/* 2095 */     return Mth.lerp(partialTicks, this.xRotO, getXRot());
/*      */   }
/*      */   
/*      */   public float getYRot(float partialTicks) {
/* 2099 */     if (partialTicks == 1.0F) {
/* 2100 */       return getYRot();
/*      */     }
/* 2102 */     return Mth.rotLerp(partialTicks, this.yRotO, getYRot());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Vec3 calculateViewVector(float xRot, float yRot) {
/* 2108 */     float realXRot = xRot * 0.017453292F;
/* 2109 */     float realYRot = -yRot * 0.017453292F;
/*      */     
/* 2111 */     float yCos = Mth.cos(realYRot);
/* 2112 */     float ySin = Mth.sin(realYRot);
/* 2113 */     float xCos = Mth.cos(realXRot);
/* 2114 */     float xSin = Mth.sin(realXRot);
/*      */     
/* 2116 */     return new Vec3((ySin * xCos), -xSin, (yCos * xCos));
/*      */   }
/*      */ 
/*      */   
/* 2120 */   public final Vec3 getUpVector(float a) { return calculateUpVector(getViewXRot(a), getViewYRot(a)); }
/*      */ 
/*      */ 
/*      */   
/* 2124 */   protected final Vec3 calculateUpVector(float xRot, float yRot) { return calculateViewVector(xRot - 90.0F, yRot); }
/*      */ 
/*      */ 
/*      */   
/* 2128 */   public final Vec3 getEyePosition() { return new Vec3(getX(), getEyeY(), getZ()); }
/*      */ 
/*      */   
/*      */   public final Vec3 getEyePosition(float partialTickTime) {
/* 2132 */     double x = Mth.lerp(partialTickTime, this.xo, getX());
/* 2133 */     double y = Mth.lerp(partialTickTime, this.yo, getY()) + getEyeHeight();
/* 2134 */     double z = Mth.lerp(partialTickTime, this.zo, getZ());
/*      */     
/* 2136 */     return new Vec3(x, y, z);
/*      */   }
/*      */ 
/*      */   
/* 2140 */   public Vec3 getLightProbePosition(float partialTickTime) { return getEyePosition(partialTickTime); }
/*      */ 
/*      */   
/*      */   public final Vec3 getPosition(float partialTickTime) {
/* 2144 */     double endX = Mth.lerp(partialTickTime, this.xo, getX());
/* 2145 */     double endY = Mth.lerp(partialTickTime, this.yo, getY());
/* 2146 */     double endZ = Mth.lerp(partialTickTime, this.zo, getZ());
/* 2147 */     return new Vec3(endX, endY, endZ);
/*      */   }
/*      */   
/*      */   public HitResult pick(double range, float a, boolean withLiquids) {
/* 2151 */     Vec3 from = getEyePosition(a);
/* 2152 */     Vec3 viewVector = getViewVector(a);
/* 2153 */     Vec3 to = from.add(viewVector.x * range, viewVector.y * range, viewVector.z * range);
/* 2154 */     return level().clip(new ClipContext(from, to, ClipContext.Block.OUTLINE, withLiquids ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, this));
/*      */   }
/*      */ 
/*      */   
/* 2158 */   public boolean canBeHitByProjectile() { return (isAlive() && isPickable()); }
/*      */ 
/*      */ 
/*      */   
/* 2162 */   public boolean isPickable() { return false; }
/*      */ 
/*      */ 
/*      */   
/* 2166 */   public boolean isPushable() { return false; }
/*      */ 
/*      */   
/*      */   public void awardKillScore(Entity victim, DamageSource killingBlow) {
/* 2170 */     if (victim instanceof ServerPlayer) {
/* 2171 */       CriteriaTriggers.ENTITY_KILLED_PLAYER.trigger((ServerPlayer)victim, this, killingBlow);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean shouldRender(double camX, double camY, double camZ) {
/* 2176 */     double xd = getX() - camX;
/* 2177 */     double yd = getY() - camY;
/* 2178 */     double zd = getZ() - camZ;
/* 2179 */     double distance = xd * xd + yd * yd + zd * zd;
/* 2180 */     return shouldRenderAtSqrDistance(distance);
/*      */   }
/*      */   
/*      */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 2184 */     double size = getBoundingBox().getSize();
/* 2185 */     if (Double.isNaN(size)) {
/* 2186 */       size = 1.0D;
/*      */     }
/* 2188 */     size *= 64.0D * viewScale;
/* 2189 */     return (distance < size * size);
/*      */   }
/*      */   
/*      */   public boolean saveAsPassenger(ValueOutput output) {
/* 2193 */     if (this.removalReason != null && !this.removalReason.shouldSave()) {
/* 2194 */       return false;
/*      */     }
/* 2196 */     String id = getEncodeId();
/* 2197 */     if (id == null) {
/* 2198 */       return false;
/*      */     }
/* 2200 */     output.putString("id", id);
/* 2201 */     saveWithoutId(output);
/* 2202 */     return true;
/*      */   }
/*      */   
/*      */   public boolean save(ValueOutput output) {
/* 2206 */     if (isPassenger()) {
/* 2207 */       return false;
/*      */     }
/* 2209 */     return saveAsPassenger(output);
/*      */   }
/*      */   
/*      */   public void saveWithoutId(ValueOutput output) {
/*      */     try {
/* 2214 */       if (this.vehicle != null) {
/*      */         
/* 2216 */         output.store("Pos", Vec3.CODEC, new Vec3(this.vehicle.getX(), getY(), this.vehicle.getZ()));
/*      */       } else {
/* 2218 */         output.store("Pos", Vec3.CODEC, position());
/*      */       } 
/*      */       
/* 2221 */       output.store("Motion", Vec3.CODEC, getDeltaMovement());
/* 2222 */       output.store("Rotation", Vec2.CODEC, new Vec2(getYRot(), getXRot()));
/*      */       
/* 2224 */       output.putDouble("fall_distance", this.fallDistance);
/* 2225 */       output.putShort("Fire", (short)this.remainingFireTicks);
/* 2226 */       output.putShort("Air", (short)getAirSupply());
/* 2227 */       output.putBoolean("OnGround", onGround());
/* 2228 */       output.putBoolean("Invulnerable", this.invulnerable);
/* 2229 */       output.putInt("PortalCooldown", this.portalCooldown);
/*      */       
/* 2231 */       output.store("UUID", UUIDUtil.CODEC, getUUID());
/*      */       
/* 2233 */       output.storeNullable("CustomName", ComponentSerialization.CODEC, getCustomName());
/*      */       
/* 2235 */       if (isCustomNameVisible()) {
/* 2236 */         output.putBoolean("CustomNameVisible", isCustomNameVisible());
/*      */       }
/* 2238 */       if (isSilent()) {
/* 2239 */         output.putBoolean("Silent", isSilent());
/*      */       }
/* 2241 */       if (isNoGravity()) {
/* 2242 */         output.putBoolean("NoGravity", isNoGravity());
/*      */       }
/* 2244 */       if (this.hasGlowingTag) {
/* 2245 */         output.putBoolean("Glowing", true);
/*      */       }
/* 2247 */       int ticksFrozen = getTicksFrozen();
/* 2248 */       if (ticksFrozen > 0) {
/* 2249 */         output.putInt("TicksFrozen", getTicksFrozen());
/*      */       }
/* 2251 */       if (this.hasVisualFire) {
/* 2252 */         output.putBoolean("HasVisualFire", this.hasVisualFire);
/*      */       }
/* 2254 */       if (!this.tags.isEmpty()) {
/* 2255 */         output.store("Tags", TAG_LIST_CODEC, List.copyOf(this.tags));
/*      */       }
/*      */       
/* 2258 */       if (!this.customData.isEmpty()) {
/* 2259 */         output.store("data", CustomData.CODEC, this.customData);
/*      */       }
/*      */       
/* 2262 */       addAdditionalSaveData(output);
/*      */       
/* 2264 */       if (isVehicle()) {
/* 2265 */         ValueOutput.ValueOutputList passengersList = output.childrenList("Passengers");
/* 2266 */         for (Entity passenger : getPassengers()) {
/* 2267 */           ValueOutput passengerOutput = passengersList.addChild();
/* 2268 */           if (!passenger.saveAsPassenger(passengerOutput)) {
/* 2269 */             passengersList.discardLast();
/*      */           }
/*      */         } 
/* 2272 */         if (passengersList.isEmpty()) {
/* 2273 */           output.discard("Passengers");
/*      */         }
/*      */       } 
/* 2276 */     } catch (Throwable t) {
/* 2277 */       CrashReport report = CrashReport.forThrowable(t, "Saving entity NBT");
/* 2278 */       CrashReportCategory category = report.addCategory("Entity being saved");
/* 2279 */       fillCrashReportCategory(category);
/* 2280 */       throw new ReportedException(report);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void load(ValueInput input) {
/*      */     try {
/* 2286 */       Vec3 pos = (Vec3)input.read("Pos", Vec3.CODEC).orElse(Vec3.ZERO);
/* 2287 */       Vec3 motion = (Vec3)input.read("Motion", Vec3.CODEC).orElse(Vec3.ZERO);
/* 2288 */       Vec2 rotation = (Vec2)input.read("Rotation", Vec2.CODEC).orElse(Vec2.ZERO);
/*      */ 
/*      */       
/* 2291 */       setDeltaMovement(
/* 2292 */           (Math.abs(motion.x) > 10.0D) ? 0.0D : motion.x, 
/* 2293 */           (Math.abs(motion.y) > 10.0D) ? 0.0D : motion.y, 
/* 2294 */           (Math.abs(motion.z) > 10.0D) ? 0.0D : motion.z);
/*      */       
/* 2296 */       this.needsSync = true;
/*      */       
/* 2298 */       double maxHorizontalPosition = 3.0000512E7D;
/* 2299 */       setPosRaw(
/* 2300 */           Mth.clamp(pos.x, -3.0000512E7D, 3.0000512E7D), 
/* 2301 */           Mth.clamp(pos.y, -2.0E7D, 2.0E7D), 
/* 2302 */           Mth.clamp(pos.z, -3.0000512E7D, 3.0000512E7D));
/*      */       
/* 2304 */       setYRot(rotation.x);
/* 2305 */       setXRot(rotation.y);
/*      */       
/* 2307 */       setOldPosAndRot();
/*      */       
/* 2309 */       setYHeadRot(getYRot());
/* 2310 */       setYBodyRot(getYRot());
/*      */       
/* 2312 */       this.fallDistance = input.getDoubleOr("fall_distance", 0.0D);
/* 2313 */       this.remainingFireTicks = input.getShortOr("Fire", (short)0);
/* 2314 */       setAirSupply(input.getIntOr("Air", getMaxAirSupply()));
/*      */       
/* 2316 */       this.onGround = input.getBooleanOr("OnGround", false);
/* 2317 */       this.invulnerable = input.getBooleanOr("Invulnerable", false);
/* 2318 */       this.portalCooldown = input.getIntOr("PortalCooldown", 0);
/*      */       
/* 2320 */       input.read("UUID", UUIDUtil.CODEC).ifPresent(id -> {
/* 2321 */             this.uuid = id;
/* 2322 */             this.stringUUID = this.uuid.toString();
/*      */           });
/*      */       
/* 2325 */       if (!Double.isFinite(getX()) || !Double.isFinite(getY()) || !Double.isFinite(getZ())) {
/* 2326 */         throw new IllegalStateException("Entity has invalid position");
/*      */       }
/* 2328 */       if (!Double.isFinite(getYRot()) || !Double.isFinite(getXRot())) {
/* 2329 */         throw new IllegalStateException("Entity has invalid rotation");
/*      */       }
/*      */       
/* 2332 */       reapplyPosition();
/* 2333 */       setRot(getYRot(), getXRot());
/*      */       
/* 2335 */       setCustomName((Component)input.read("CustomName", ComponentSerialization.CODEC).orElse(null));
/* 2336 */       setCustomNameVisible(input.getBooleanOr("CustomNameVisible", false));
/* 2337 */       setSilent(input.getBooleanOr("Silent", false));
/* 2338 */       setNoGravity(input.getBooleanOr("NoGravity", false));
/* 2339 */       setGlowingTag(input.getBooleanOr("Glowing", false));
/* 2340 */       setTicksFrozen(input.getIntOr("TicksFrozen", 0));
/* 2341 */       this.hasVisualFire = input.getBooleanOr("HasVisualFire", false);
/*      */       
/* 2343 */       this.customData = (CustomData)input.read("data", CustomData.CODEC).orElse(CustomData.EMPTY);
/*      */       
/* 2345 */       this.tags.clear();
/* 2346 */       Objects.requireNonNull(this.tags); input.read("Tags", TAG_LIST_CODEC).ifPresent(this.tags::addAll);
/*      */       
/* 2348 */       readAdditionalSaveData(input);
/*      */       
/* 2350 */       if (repositionEntityAfterLoad()) {
/* 2351 */         reapplyPosition();
/*      */       }
/* 2353 */     } catch (Throwable t) {
/* 2354 */       CrashReport report = CrashReport.forThrowable(t, "Loading entity NBT");
/* 2355 */       CrashReportCategory category = report.addCategory("Entity being loaded");
/* 2356 */       fillCrashReportCategory(category);
/* 2357 */       throw new ReportedException(report);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 2362 */   protected boolean repositionEntityAfterLoad() { return true; }
/*      */ 
/*      */   
/*      */   protected final String getEncodeId() {
/* 2366 */     EntityType<?> type = getType();
/* 2367 */     Identifier key = EntityType.getKey(type);
/* 2368 */     return !type.canSerialize() ? null : key.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2376 */   public ItemEntity spawnAtLocation(ServerLevel level, ItemLike resource) { return spawnAtLocation(level, new ItemStack(resource), 0.0F); }
/*      */ 
/*      */ 
/*      */   
/* 2380 */   public ItemEntity spawnAtLocation(ServerLevel level, ItemStack itemStack) { return spawnAtLocation(level, itemStack, 0.0F); }
/*      */ 
/*      */   
/*      */   public ItemEntity spawnAtLocation(ServerLevel level, ItemStack itemStack, Vec3 offset) {
/* 2384 */     if (itemStack.isEmpty()) {
/* 2385 */       return null;
/*      */     }
/* 2387 */     ItemEntity entity = new ItemEntity(level, getX() + offset.x, getY() + offset.y, getZ() + offset.z, itemStack);
/* 2388 */     entity.setDefaultPickUpDelay();
/* 2389 */     level.addFreshEntity(entity);
/* 2390 */     return entity;
/*      */   }
/*      */ 
/*      */   
/* 2394 */   public ItemEntity spawnAtLocation(ServerLevel level, ItemStack itemStack, float offset) { return spawnAtLocation(level, itemStack, new Vec3(0.0D, offset, 0.0D)); }
/*      */ 
/*      */ 
/*      */   
/* 2398 */   public boolean isAlive() { return !isRemoved(); }
/*      */ 
/*      */   
/*      */   public boolean isInWall() {
/* 2402 */     if (this.noPhysics) {
/* 2403 */       return false;
/*      */     }
/*      */     
/* 2406 */     float checkWidth = this.dimensions.width() * 0.8F;
/* 2407 */     AABB eyeBb = AABB.ofSize(getEyePosition(), checkWidth, 1.0E-6D, checkWidth);
/* 2408 */     return BlockPos.betweenClosedStream(eyeBb)
/* 2409 */       .anyMatch(pos -> {
/* 2410 */           BlockState state = level().getBlockState(pos);
/* 2411 */           return (!state.isAir() && state
/* 2412 */             .isSuffocating(level(), pos) && 
/* 2413 */             Shapes.joinIsNotEmpty(state.getCollisionShape(level(), pos).move(pos), Shapes.create(eyeBb), BooleanOp.AND));
/*      */         });
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
/*      */   public InteractionResult interact(Player player, InteractionHand hand) { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   4: invokevirtual isClientSide : ()Z
/*      */     //   7: ifne -> 195
/*      */     //   10: aload_1
/*      */     //   11: invokevirtual isSecondaryUseActive : ()Z
/*      */     //   14: ifeq -> 195
/*      */     //   17: aload_0
/*      */     //   18: astore #4
/*      */     //   20: aload #4
/*      */     //   22: instanceof net/minecraft/world/entity/Leashable
/*      */     //   25: ifeq -> 195
/*      */     //   28: aload #4
/*      */     //   30: checkcast net/minecraft/world/entity/Leashable
/*      */     //   33: astore_3
/*      */     //   34: aload_3
/*      */     //   35: invokeinterface canBeLeashed : ()Z
/*      */     //   40: ifeq -> 195
/*      */     //   43: aload_0
/*      */     //   44: invokevirtual isAlive : ()Z
/*      */     //   47: ifeq -> 195
/*      */     //   50: aload_0
/*      */     //   51: astore #5
/*      */     //   53: aload #5
/*      */     //   55: instanceof net/minecraft/world/entity/LivingEntity
/*      */     //   58: ifeq -> 76
/*      */     //   61: aload #5
/*      */     //   63: checkcast net/minecraft/world/entity/LivingEntity
/*      */     //   66: astore #4
/*      */     //   68: aload #4
/*      */     //   70: invokevirtual isBaby : ()Z
/*      */     //   73: ifne -> 195
/*      */     //   76: aload_0
/*      */     //   77: aload_1
/*      */     //   78: <illegal opcode> test : (Lnet/minecraft/world/entity/player/Player;)Ljava/util/function/Predicate;
/*      */     //   83: invokestatic leashableInArea : (Lnet/minecraft/world/entity/Entity;Ljava/util/function/Predicate;)Ljava/util/List;
/*      */     //   86: astore #5
/*      */     //   88: aload #5
/*      */     //   90: invokeinterface isEmpty : ()Z
/*      */     //   95: ifne -> 195
/*      */     //   98: iconst_0
/*      */     //   99: istore #6
/*      */     //   101: aload #5
/*      */     //   103: invokeinterface iterator : ()Ljava/util/Iterator;
/*      */     //   108: astore #7
/*      */     //   110: aload #7
/*      */     //   112: invokeinterface hasNext : ()Z
/*      */     //   117: ifeq -> 158
/*      */     //   120: aload #7
/*      */     //   122: invokeinterface next : ()Ljava/lang/Object;
/*      */     //   127: checkcast net/minecraft/world/entity/Leashable
/*      */     //   130: astore #8
/*      */     //   132: aload #8
/*      */     //   134: aload_0
/*      */     //   135: invokeinterface canHaveALeashAttachedTo : (Lnet/minecraft/world/entity/Entity;)Z
/*      */     //   140: ifeq -> 155
/*      */     //   143: aload #8
/*      */     //   145: aload_0
/*      */     //   146: iconst_1
/*      */     //   147: invokeinterface setLeashedTo : (Lnet/minecraft/world/entity/Entity;Z)V
/*      */     //   152: iconst_1
/*      */     //   153: istore #6
/*      */     //   155: goto -> 110
/*      */     //   158: iload #6
/*      */     //   160: ifeq -> 195
/*      */     //   163: aload_0
/*      */     //   164: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   167: getstatic net/minecraft/world/level/gameevent/GameEvent.ENTITY_ACTION : Lnet/minecraft/core/Holder$Reference;
/*      */     //   170: aload_0
/*      */     //   171: invokevirtual blockPosition : ()Lnet/minecraft/core/BlockPos;
/*      */     //   174: aload_1
/*      */     //   175: invokestatic of : (Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/level/gameevent/GameEvent$Context;
/*      */     //   178: invokevirtual gameEvent : (Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)V
/*      */     //   181: aload_0
/*      */     //   182: getstatic net/minecraft/sounds/SoundEvents.LEAD_TIED : Lnet/minecraft/sounds/SoundEvent;
/*      */     //   185: invokevirtual playSound : (Lnet/minecraft/sounds/SoundEvent;)V
/*      */     //   188: getstatic net/minecraft/world/InteractionResult.SUCCESS_SERVER : Lnet/minecraft/world/InteractionResult$Success;
/*      */     //   191: invokevirtual withoutItem : ()Lnet/minecraft/world/InteractionResult$Success;
/*      */     //   194: areturn
/*      */     //   195: aload_1
/*      */     //   196: aload_2
/*      */     //   197: invokevirtual getItemInHand : (Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;
/*      */     //   200: astore_3
/*      */     //   201: aload_3
/*      */     //   202: getstatic net/minecraft/world/item/Items.SHEARS : Lnet/minecraft/world/item/Item;
/*      */     //   205: invokevirtual is : (Lnet/minecraft/world/item/Item;)Z
/*      */     //   208: ifeq -> 230
/*      */     //   211: aload_0
/*      */     //   212: aload_1
/*      */     //   213: invokevirtual shearOffAllLeashConnections : (Lnet/minecraft/world/entity/player/Player;)Z
/*      */     //   216: ifeq -> 230
/*      */     //   219: aload_3
/*      */     //   220: iconst_1
/*      */     //   221: aload_1
/*      */     //   222: aload_2
/*      */     //   223: invokevirtual hurtAndBreak : (ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;)V
/*      */     //   226: getstatic net/minecraft/world/InteractionResult.SUCCESS : Lnet/minecraft/world/InteractionResult$Success;
/*      */     //   229: areturn
/*      */     //   230: aload_0
/*      */     //   231: astore #5
/*      */     //   233: aload #5
/*      */     //   235: instanceof net/minecraft/world/entity/Mob
/*      */     //   238: ifeq -> 290
/*      */     //   241: aload #5
/*      */     //   243: checkcast net/minecraft/world/entity/Mob
/*      */     //   246: astore #4
/*      */     //   248: aload_3
/*      */     //   249: getstatic net/minecraft/world/item/Items.SHEARS : Lnet/minecraft/world/item/Item;
/*      */     //   252: invokevirtual is : (Lnet/minecraft/world/item/Item;)Z
/*      */     //   255: ifeq -> 290
/*      */     //   258: aload #4
/*      */     //   260: aload_1
/*      */     //   261: invokevirtual canShearEquipment : (Lnet/minecraft/world/entity/player/Player;)Z
/*      */     //   264: ifeq -> 290
/*      */     //   267: aload_1
/*      */     //   268: invokevirtual isSecondaryUseActive : ()Z
/*      */     //   271: ifne -> 290
/*      */     //   274: aload_0
/*      */     //   275: aload_1
/*      */     //   276: aload_2
/*      */     //   277: aload_3
/*      */     //   278: aload #4
/*      */     //   280: invokevirtual attemptToShearEquipment : (Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Mob;)Z
/*      */     //   283: ifeq -> 290
/*      */     //   286: getstatic net/minecraft/world/InteractionResult.SUCCESS : Lnet/minecraft/world/InteractionResult$Success;
/*      */     //   289: areturn
/*      */     //   290: aload_0
/*      */     //   291: invokevirtual isAlive : ()Z
/*      */     //   294: ifeq -> 481
/*      */     //   297: aload_0
/*      */     //   298: astore #5
/*      */     //   300: aload #5
/*      */     //   302: instanceof net/minecraft/world/entity/Leashable
/*      */     //   305: ifeq -> 481
/*      */     //   308: aload #5
/*      */     //   310: checkcast net/minecraft/world/entity/Leashable
/*      */     //   313: astore #4
/*      */     //   315: aload #4
/*      */     //   317: invokeinterface getLeashHolder : ()Lnet/minecraft/world/entity/Entity;
/*      */     //   322: aload_1
/*      */     //   323: if_acmpne -> 382
/*      */     //   326: aload_0
/*      */     //   327: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   330: invokevirtual isClientSide : ()Z
/*      */     //   333: ifne -> 375
/*      */     //   336: aload_1
/*      */     //   337: invokevirtual hasInfiniteMaterials : ()Z
/*      */     //   340: ifeq -> 353
/*      */     //   343: aload #4
/*      */     //   345: invokeinterface removeLeash : ()V
/*      */     //   350: goto -> 360
/*      */     //   353: aload #4
/*      */     //   355: invokeinterface dropLeash : ()V
/*      */     //   360: aload_0
/*      */     //   361: getstatic net/minecraft/world/level/gameevent/GameEvent.ENTITY_INTERACT : Lnet/minecraft/core/Holder$Reference;
/*      */     //   364: aload_1
/*      */     //   365: invokevirtual gameEvent : (Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/Entity;)V
/*      */     //   368: aload_0
/*      */     //   369: getstatic net/minecraft/sounds/SoundEvents.LEAD_UNTIED : Lnet/minecraft/sounds/SoundEvent;
/*      */     //   372: invokevirtual playSound : (Lnet/minecraft/sounds/SoundEvent;)V
/*      */     //   375: getstatic net/minecraft/world/InteractionResult.SUCCESS : Lnet/minecraft/world/InteractionResult$Success;
/*      */     //   378: invokevirtual withoutItem : ()Lnet/minecraft/world/InteractionResult$Success;
/*      */     //   381: areturn
/*      */     //   382: aload_1
/*      */     //   383: aload_2
/*      */     //   384: invokevirtual getItemInHand : (Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;
/*      */     //   387: astore #5
/*      */     //   389: aload #5
/*      */     //   391: getstatic net/minecraft/world/item/Items.LEAD : Lnet/minecraft/world/item/Item;
/*      */     //   394: invokevirtual is : (Lnet/minecraft/world/item/Item;)Z
/*      */     //   397: ifeq -> 481
/*      */     //   400: aload #4
/*      */     //   402: invokeinterface getLeashHolder : ()Lnet/minecraft/world/entity/Entity;
/*      */     //   407: instanceof net/minecraft/world/entity/player/Player
/*      */     //   410: ifne -> 481
/*      */     //   413: aload_0
/*      */     //   414: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   417: invokevirtual isClientSide : ()Z
/*      */     //   420: ifeq -> 427
/*      */     //   423: getstatic net/minecraft/world/InteractionResult.CONSUME : Lnet/minecraft/world/InteractionResult$Success;
/*      */     //   426: areturn
/*      */     //   427: aload #4
/*      */     //   429: aload_1
/*      */     //   430: invokeinterface canHaveALeashAttachedTo : (Lnet/minecraft/world/entity/Entity;)Z
/*      */     //   435: ifeq -> 481
/*      */     //   438: aload #4
/*      */     //   440: invokeinterface isLeashed : ()Z
/*      */     //   445: ifeq -> 455
/*      */     //   448: aload #4
/*      */     //   450: invokeinterface dropLeash : ()V
/*      */     //   455: aload #4
/*      */     //   457: aload_1
/*      */     //   458: iconst_1
/*      */     //   459: invokeinterface setLeashedTo : (Lnet/minecraft/world/entity/Entity;Z)V
/*      */     //   464: aload_0
/*      */     //   465: getstatic net/minecraft/sounds/SoundEvents.LEAD_TIED : Lnet/minecraft/sounds/SoundEvent;
/*      */     //   468: invokevirtual playSound : (Lnet/minecraft/sounds/SoundEvent;)V
/*      */     //   471: aload #5
/*      */     //   473: iconst_1
/*      */     //   474: invokevirtual shrink : (I)V
/*      */     //   477: getstatic net/minecraft/world/InteractionResult.SUCCESS_SERVER : Lnet/minecraft/world/InteractionResult$Success;
/*      */     //   480: areturn
/*      */     //   481: getstatic net/minecraft/world/InteractionResult.PASS : Lnet/minecraft/world/InteractionResult$Pass;
/*      */     //   484: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2420	-> 0
/*      */     //   #2422	-> 50
/*      */     //   #2423	-> 76
/*      */     //   #2424	-> 88
/*      */     //   #2425	-> 98
/*      */     //   #2426	-> 101
/*      */     //   #2427	-> 132
/*      */     //   #2428	-> 143
/*      */     //   #2429	-> 152
/*      */     //   #2431	-> 155
/*      */     //   #2432	-> 158
/*      */     //   #2433	-> 163
/*      */     //   #2434	-> 181
/*      */     //   #2435	-> 188
/*      */     //   #2441	-> 195
/*      */     //   #2442	-> 201
/*      */     //   #2443	-> 219
/*      */     //   #2444	-> 226
/*      */     //   #2447	-> 230
/*      */     //   #2448	-> 274
/*      */     //   #2449	-> 286
/*      */     //   #2453	-> 290
/*      */     //   #2454	-> 315
/*      */     //   #2455	-> 326
/*      */     //   #2456	-> 336
/*      */     //   #2457	-> 343
/*      */     //   #2459	-> 353
/*      */     //   #2461	-> 360
/*      */     //   #2462	-> 368
/*      */     //   #2464	-> 375
/*      */     //   #2468	-> 382
/*      */     //   #2470	-> 389
/*      */     //   #2471	-> 413
/*      */     //   #2472	-> 423
/*      */     //   #2474	-> 427
/*      */     //   #2475	-> 438
/*      */     //   #2476	-> 448
/*      */     //   #2478	-> 455
/*      */     //   #2479	-> 464
/*      */     //   #2480	-> 471
/*      */     //   #2481	-> 477
/*      */     //   #2485	-> 481
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   68	8	4	le	Lnet/minecraft/world/entity/LivingEntity;
/*      */     //   132	23	8	mob	Lnet/minecraft/world/entity/Leashable;
/*      */     //   101	94	6	anyLeashed	Z
/*      */     //   88	107	5	mobsToLeash	Ljava/util/List;
/*      */     //   34	161	3	leashable	Lnet/minecraft/world/entity/Leashable;
/*      */     //   248	42	4	target	Lnet/minecraft/world/entity/Mob;
/*      */     //   389	92	5	itemStack	Lnet/minecraft/world/item/ItemStack;
/*      */     //   315	166	4	leashable	Lnet/minecraft/world/entity/Leashable;
/*      */     //   0	485	0	this	Lnet/minecraft/world/entity/Entity;
/*      */     //   0	485	1	player	Lnet/minecraft/world/entity/player/Player;
/*      */     //   0	485	2	hand	Lnet/minecraft/world/InteractionHand;
/*      */     //   201	284	3	heldItem	Lnet/minecraft/world/item/ItemStack;
/*      */     // Local variable type table:
/*      */     //   start	length	slot	name	signature
/*      */     //   88	107	5	mobsToLeash	Ljava/util/List<Lnet/minecraft/world/entity/Leashable;>; }
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
/*      */   public boolean shearOffAllLeashConnections(Player player) {
/* 2489 */     boolean dropped = dropAllLeashConnections(player);
/* 2490 */     if (dropped) { Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1;
/* 2491 */         serverLevel.playSound(null, blockPosition(), SoundEvents.SHEARS_SNIP, (player != null) ? player.getSoundSource() : getSoundSource()); }
/*      */        }
/* 2493 */      return dropped;
/*      */   }
/*      */   
/*      */   public boolean dropAllLeashConnections(Player player) {
/* 2497 */     List<Leashable> leashables = Leashable.leashableLeashedTo(this);
/* 2498 */     boolean dropped = !leashables.isEmpty();
/* 2499 */     Entity entity = this; if (entity instanceof Leashable) { Leashable leashableThis = (Leashable)entity; if (leashableThis.isLeashed()) {
/* 2500 */         leashableThis.dropLeash();
/* 2501 */         dropped = true;
/*      */       }  }
/* 2503 */      for (Leashable leashable : leashables) {
/* 2504 */       leashable.dropLeash();
/*      */     }
/* 2506 */     if (dropped) {
/* 2507 */       gameEvent(GameEvent.SHEAR, player);
/* 2508 */       return true;
/*      */     } 
/* 2510 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean attemptToShearEquipment(Player player, InteractionHand hand, ItemStack heldItem, Mob target) {
/* 2518 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 2519 */       ItemStack itemStack = target.getItemBySlot(slot);
/* 2520 */       Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 2521 */       if (equippable != null && equippable.canBeSheared() && (!EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE) || player.isCreative())) {
/* 2522 */         heldItem.hurtAndBreak(1, player, hand.asEquipmentSlot());
/* 2523 */         Vec3 equipmentSpawnOffset = this.dimensions.attachments().getAverage(EntityAttachment.PASSENGER);
/* 2524 */         target.setItemSlotAndDropWhenKilled(slot, ItemStack.EMPTY);
/* 2525 */         gameEvent(GameEvent.SHEAR, player);
/* 2526 */         playSound((SoundEvent)equippable.shearingSound().value());
/* 2527 */         Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1;
/* 2528 */           spawnAtLocation(serverLevel, itemStack, equipmentSpawnOffset);
/* 2529 */           CriteriaTriggers.PLAYER_SHEARED_EQUIPMENT.trigger((ServerPlayer)player, itemStack, target); }
/*      */         
/* 2531 */         return true;
/*      */       } 
/*      */     } 
/* 2534 */     return false;
/*      */   }
/*      */ 
/*      */   
/* 2538 */   public boolean canCollideWith(Entity entity) { return (entity.canBeCollidedWith(this) && !isPassengerOfSameVehicle(entity)); }
/*      */ 
/*      */ 
/*      */   
/* 2542 */   public boolean canBeCollidedWith(Entity other) { return false; }
/*      */ 
/*      */   
/*      */   public void rideTick() {
/* 2546 */     setDeltaMovement(Vec3.ZERO);
/* 2547 */     tick();
/* 2548 */     if (!isPassenger()) {
/*      */       return;
/*      */     }
/*      */     
/* 2552 */     getVehicle().positionRider(this);
/*      */   }
/*      */   
/*      */   public final void positionRider(Entity passenger) {
/* 2556 */     if (!hasPassenger(passenger)) {
/*      */       return;
/*      */     }
/* 2559 */     positionRider(passenger, Entity::setPos);
/*      */   }
/*      */   
/*      */   protected void positionRider(Entity passenger, MoveFunction moveFunction) {
/* 2563 */     Vec3 position = getPassengerRidingPosition(passenger);
/* 2564 */     Vec3 offset = passenger.getVehicleAttachmentPoint(this);
/* 2565 */     moveFunction.accept(passenger, position.x - offset.x, position.y - offset.y, position.z - offset.z);
/*      */   }
/*      */ 
/*      */   
/*      */   public void onPassengerTurned(Entity passenger) {}
/*      */ 
/*      */   
/* 2572 */   public Vec3 getVehicleAttachmentPoint(Entity vehicle) { return getAttachments().get(EntityAttachment.VEHICLE, 0, this.yRot); }
/*      */ 
/*      */ 
/*      */   
/* 2576 */   public Vec3 getPassengerRidingPosition(Entity passenger) { return position().add(getPassengerAttachmentPoint(passenger, this.dimensions, 1.0F)); }
/*      */ 
/*      */ 
/*      */   
/* 2580 */   protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) { return getDefaultPassengerAttachmentPoint(this, passenger, dimensions.attachments()); }
/*      */ 
/*      */   
/*      */   protected static Vec3 getDefaultPassengerAttachmentPoint(Entity vehicle, Entity passenger, EntityAttachments attachments) {
/* 2584 */     int passengerIndex = vehicle.getPassengers().indexOf(passenger);
/* 2585 */     return attachments.getClamped(EntityAttachment.PASSENGER, passengerIndex, vehicle.yRot);
/*      */   }
/*      */ 
/*      */   
/* 2589 */   public final boolean startRiding(Entity entity) { return startRiding(entity, false, true); }
/*      */ 
/*      */ 
/*      */   
/* 2593 */   public boolean showVehicleHealth() { return this instanceof LivingEntity; }
/*      */ 
/*      */   
/*      */   public boolean startRiding(Entity entityToRide, boolean force, boolean sendEventAndTriggers) {
/* 2597 */     if (entityToRide == this.vehicle) {
/* 2598 */       return false;
/*      */     }
/* 2600 */     if (!entityToRide.couldAcceptPassenger()) {
/* 2601 */       return false;
/*      */     }
/*      */     
/* 2604 */     if (!level().isClientSide() && !entityToRide.type.canSerialize()) {
/* 2605 */       return false;
/*      */     }
/* 2607 */     Entity vehicleEntity = entityToRide;
/* 2608 */     while (vehicleEntity.vehicle != null) {
/* 2609 */       if (vehicleEntity.vehicle == this) {
/* 2610 */         return false;
/*      */       }
/*      */       
/* 2613 */       vehicleEntity = vehicleEntity.vehicle;
/*      */     } 
/*      */     
/* 2616 */     if (!force && (!canRide(entityToRide) || !entityToRide.canAddPassenger(this))) {
/* 2617 */       return false;
/*      */     }
/*      */     
/* 2620 */     if (isPassenger()) {
/* 2621 */       stopRiding();
/*      */     }
/*      */     
/* 2624 */     setPose(Pose.STANDING);
/* 2625 */     this.vehicle = entityToRide;
/* 2626 */     this.vehicle.addPassenger(this);
/*      */     
/* 2628 */     if (sendEventAndTriggers) {
/* 2629 */       level().gameEvent(this, GameEvent.ENTITY_MOUNT, this.vehicle.position);
/* 2630 */       entityToRide.getIndirectPassengersStream()
/* 2631 */         .filter(e -> e instanceof ServerPlayer)
/* 2632 */         .forEach(player -> 
/* 2633 */           CriteriaTriggers.START_RIDING_TRIGGER.trigger((ServerPlayer)player));
/*      */     } 
/*      */ 
/*      */     
/* 2637 */     return true;
/*      */   }
/*      */ 
/*      */   
/* 2641 */   protected boolean canRide(Entity vehicle) { return (!isShiftKeyDown() && this.boardingCooldown <= 0); }
/*      */ 
/*      */   
/*      */   public void ejectPassengers() {
/* 2645 */     for (int i = this.passengers.size() - 1; i >= 0; i--) {
/* 2646 */       ((Entity)this.passengers.get(i)).stopRiding();
/*      */     }
/*      */   }
/*      */   
/*      */   public void removeVehicle() {
/* 2651 */     if (this.vehicle != null) {
/* 2652 */       Entity oldVehicle = this.vehicle;
/* 2653 */       this.vehicle = null;
/* 2654 */       oldVehicle.removePassenger(this);
/* 2655 */       RemovalReason removalReason = getRemovalReason();
/* 2656 */       if (removalReason == null || removalReason.shouldDestroy())
/*      */       {
/* 2658 */         level().gameEvent(this, GameEvent.ENTITY_DISMOUNT, oldVehicle.position);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 2664 */   public void stopRiding() { removeVehicle(); }
/*      */ 
/*      */   
/*      */   protected void addPassenger(Entity passenger) {
/* 2668 */     if (passenger.getVehicle() != this) {
/* 2669 */       throw new IllegalStateException("Use x.startRiding(y), not y.addPassenger(x)");
/*      */     }
/*      */     
/* 2672 */     if (this.passengers.isEmpty()) {
/* 2673 */       this.passengers = ImmutableList.of(passenger);
/*      */     } else {
/* 2675 */       List<Entity> newPassengers = Lists.newArrayList(this.passengers);
/* 2676 */       if (!level().isClientSide() && passenger instanceof Player && !(getFirstPassenger() instanceof Player)) {
/* 2677 */         newPassengers.add(0, passenger);
/*      */       } else {
/* 2679 */         newPassengers.add(passenger);
/*      */       } 
/* 2681 */       this.passengers = ImmutableList.copyOf(newPassengers);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void removePassenger(Entity passenger) {
/* 2686 */     if (passenger.getVehicle() == this) {
/* 2687 */       throw new IllegalStateException("Use x.stopRiding(y), not y.removePassenger(x)");
/*      */     }
/*      */     
/* 2690 */     if (this.passengers.size() == 1 && this.passengers.get(false) == passenger) {
/* 2691 */       this.passengers = ImmutableList.of();
/*      */     } else {
/* 2693 */       this.passengers = (ImmutableList)this.passengers.stream().filter(p -> (p != passenger)).collect(ImmutableList.toImmutableList());
/*      */     } 
/* 2695 */     passenger.boardingCooldown = 60;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2702 */   protected boolean canAddPassenger(Entity passenger) { return this.passengers.isEmpty(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2709 */   protected boolean couldAcceptPassenger() { return true; }
/*      */ 
/*      */ 
/*      */   
/* 2713 */   public final boolean isInterpolating() { return (getInterpolation() != null && getInterpolation().hasActiveInterpolation()); }
/*      */ 
/*      */ 
/*      */   
/* 2717 */   public final void moveOrInterpolateTo(Vec3 position, float yRot, float xRot) { moveOrInterpolateTo(Optional.of(position), Optional.of(Float.valueOf(yRot)), Optional.of(Float.valueOf(xRot))); }
/*      */ 
/*      */ 
/*      */   
/* 2721 */   public final void moveOrInterpolateTo(float yRot, float xRot) { moveOrInterpolateTo(Optional.empty(), Optional.of(Float.valueOf(yRot)), Optional.of(Float.valueOf(xRot))); }
/*      */ 
/*      */ 
/*      */   
/* 2725 */   public final void moveOrInterpolateTo(Vec3 position) { moveOrInterpolateTo(Optional.of(position), Optional.empty(), Optional.empty()); }
/*      */ 
/*      */   
/*      */   public final void moveOrInterpolateTo(Optional<Vec3> position, Optional<Float> yRot, Optional<Float> xRot) {
/* 2729 */     InterpolationHandler interpolationHandler = getInterpolation();
/* 2730 */     if (interpolationHandler != null) {
/* 2731 */       interpolationHandler.interpolateTo((Vec3)position
/* 2732 */           .orElse(interpolationHandler.position()), ((Float)yRot
/* 2733 */           .orElse(Float.valueOf(interpolationHandler.yRot()))).floatValue(), ((Float)xRot
/* 2734 */           .orElse(Float.valueOf(interpolationHandler.xRot()))).floatValue());
/*      */     } else {
/* 2736 */       position.ifPresent(this::setPos);
/* 2737 */       yRot.ifPresent(y -> setYRot(y.floatValue() % 360.0F));
/* 2738 */       xRot.ifPresent(x -> setXRot(x.floatValue() % 360.0F));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 2743 */   public InterpolationHandler getInterpolation() { return null; }
/*      */ 
/*      */ 
/*      */   
/* 2747 */   public void lerpHeadTo(float yRot, int steps) { setYHeadRot(yRot); }
/*      */ 
/*      */ 
/*      */   
/* 2751 */   public float getPickRadius() { return 0.0F; }
/*      */ 
/*      */ 
/*      */   
/* 2755 */   public Vec3 getLookAngle() { return calculateViewVector(getXRot(), getYRot()); }
/*      */ 
/*      */ 
/*      */   
/* 2759 */   public Vec3 getHeadLookAngle() { return calculateViewVector(getXRot(), getYHeadRot()); }
/*      */ 
/*      */   
/*      */   public Vec3 getHandHoldingItemAngle(Item item) {
/* 2763 */     Entity entity = this; if (entity instanceof Player) { Player player = (Player)entity;
/* 2764 */       boolean itemOnlyInOffhand = (player.getOffhandItem().is(item) && !player.getMainHandItem().is(item));
/* 2765 */       HumanoidArm itemArm = itemOnlyInOffhand ? player.getMainArm().getOpposite() : player.getMainArm();
/* 2766 */       return calculateViewVector(0.0F, getYRot() + ((itemArm == HumanoidArm.RIGHT) ? 80 : -80)).scale(0.5D); }
/*      */     
/* 2768 */     return Vec3.ZERO;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 2773 */   public Vec2 getRotationVector() { return new Vec2(getXRot(), getYRot()); }
/*      */ 
/*      */ 
/*      */   
/* 2777 */   public Vec3 getForward() { return Vec3.directionFromRotation(getRotationVector()); }
/*      */ 
/*      */   
/*      */   public void setAsInsidePortal(Portal portal, BlockPos pos) {
/* 2781 */     if (isOnPortalCooldown()) {
/* 2782 */       setPortalCooldown();
/*      */       
/*      */       return;
/*      */     } 
/* 2786 */     if (this.portalProcess == null || !this.portalProcess.isSamePortal(portal)) {
/* 2787 */       this.portalProcess = new PortalProcessor(portal, pos.immutable());
/* 2788 */     } else if (!this.portalProcess.isInsidePortalThisTick()) {
/*      */       
/* 2790 */       this.portalProcess.updateEntryPosition(pos.immutable());
/* 2791 */       this.portalProcess.setAsInsidePortalThisTick(true);
/*      */     } 
/*      */   }
/*      */   protected void handlePortal() {
/*      */     ServerLevel level;
/* 2796 */     Level level1 = level(); if (level1 instanceof ServerLevel) { level = (ServerLevel)level1; }
/*      */     else
/*      */     { return; }
/*      */     
/* 2800 */     processPortalCooldown();
/*      */     
/* 2802 */     if (this.portalProcess == null) {
/*      */       return;
/*      */     }
/*      */     
/* 2806 */     if (this.portalProcess.processPortalTeleportation(level, this, canUsePortal(false))) {
/* 2807 */       ProfilerFiller profiler = Profiler.get();
/* 2808 */       profiler.push("portal");
/*      */       
/* 2810 */       setPortalCooldown();
/* 2811 */       TeleportTransition teleportTransition = this.portalProcess.getPortalDestination(level, this);
/* 2812 */       if (teleportTransition != null) {
/* 2813 */         ServerLevel newLevel = teleportTransition.newLevel();
/* 2814 */         if (level.isAllowedToEnterPortal(newLevel) && (
/* 2815 */           newLevel.dimension() == level.dimension() || canTeleport(level, newLevel))) {
/* 2816 */           teleport(teleportTransition);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 2821 */       profiler.pop();
/* 2822 */     } else if (this.portalProcess.hasExpired()) {
/* 2823 */       this.portalProcess = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getDimensionChangingDelay() {
/* 2828 */     Entity firstPassenger = getFirstPassenger();
/* 2829 */     return (firstPassenger instanceof ServerPlayer) ? firstPassenger.getDimensionChangingDelay() : 300;
/*      */   }
/*      */ 
/*      */   
/* 2833 */   public void lerpMotion(Vec3 movement) { setDeltaMovement(movement); }
/*      */ 
/*      */   
/*      */   public void handleDamageEvent(DamageSource source) {}
/*      */ 
/*      */   
/*      */   public void handleEntityEvent(byte id) {
/* 2840 */     switch (id) {
/*      */       case 53:
/* 2842 */         HoneyBlock.showSlideParticles(this);
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void animateHurt(float direction) {}
/*      */ 
/*      */   
/*      */   public boolean isOnFire() {
/* 2852 */     boolean isClientSide = (level() != null && level().isClientSide());
/*      */     
/* 2854 */     return (!fireImmune() && (this.remainingFireTicks > 0 || (isClientSide && getSharedFlag(0))));
/*      */   }
/*      */ 
/*      */   
/* 2858 */   public boolean isPassenger() { return (getVehicle() != null); }
/*      */ 
/*      */ 
/*      */   
/* 2862 */   public boolean isVehicle() { return !this.passengers.isEmpty(); }
/*      */ 
/*      */ 
/*      */   
/* 2866 */   public boolean dismountsUnderwater() { return getType().is(EntityTypeTags.DISMOUNTS_UNDERWATER); }
/*      */ 
/*      */ 
/*      */   
/* 2870 */   public boolean canControlVehicle() { return !getType().is(EntityTypeTags.NON_CONTROLLING_RIDER); }
/*      */ 
/*      */ 
/*      */   
/* 2874 */   public void setShiftKeyDown(boolean shiftKeyDown) { setSharedFlag(1, shiftKeyDown); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2879 */   public boolean isShiftKeyDown() { return getSharedFlag(1); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2886 */   public boolean isSteppingCarefully() { return isShiftKeyDown(); }
/*      */ 
/*      */ 
/*      */   
/* 2890 */   public boolean isSuppressingBounce() { return isShiftKeyDown(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2897 */   public boolean isDiscrete() { return isShiftKeyDown(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2904 */   public boolean isDescending() { return isShiftKeyDown(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2911 */   public boolean isCrouching() { return hasPose(Pose.CROUCHING); }
/*      */ 
/*      */ 
/*      */   
/* 2915 */   public boolean isSprinting() { return getSharedFlag(3); }
/*      */ 
/*      */ 
/*      */   
/* 2919 */   public void setSprinting(boolean isSprinting) { setSharedFlag(3, isSprinting); }
/*      */ 
/*      */ 
/*      */   
/* 2923 */   public boolean isSwimming() { return getSharedFlag(4); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2933 */   public boolean isVisuallySwimming() { return hasPose(Pose.SWIMMING); }
/*      */ 
/*      */ 
/*      */   
/* 2937 */   public boolean isVisuallyCrawling() { return (isVisuallySwimming() && !isInWater()); }
/*      */ 
/*      */ 
/*      */   
/* 2941 */   public void setSwimming(boolean swimming) { setSharedFlag(4, swimming); }
/*      */ 
/*      */ 
/*      */   
/* 2945 */   public final boolean hasGlowingTag() { return this.hasGlowingTag; }
/*      */ 
/*      */   
/*      */   public final void setGlowingTag(boolean value) {
/* 2949 */     this.hasGlowingTag = value;
/* 2950 */     setSharedFlag(6, isCurrentlyGlowing());
/*      */   }
/*      */   
/*      */   public boolean isCurrentlyGlowing() {
/* 2954 */     if (level().isClientSide()) {
/* 2955 */       return getSharedFlag(6);
/*      */     }
/* 2957 */     return this.hasGlowingTag;
/*      */   }
/*      */ 
/*      */   
/* 2961 */   public boolean isInvisible() { return getSharedFlag(5); }
/*      */ 
/*      */   
/*      */   public boolean isInvisibleTo(Player player) {
/* 2965 */     if (player.isSpectator()) {
/* 2966 */       return false;
/*      */     }
/* 2968 */     PlayerTeam playerTeam = getTeam();
/* 2969 */     if (playerTeam != null && player != null && player.getTeam() == playerTeam && playerTeam.canSeeFriendlyInvisibles()) {
/* 2970 */       return false;
/*      */     }
/* 2972 */     return isInvisible();
/*      */   }
/*      */ 
/*      */   
/* 2976 */   public boolean isOnRails() { return false; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> action) {}
/*      */ 
/*      */ 
/*      */   
/* 2984 */   public PlayerTeam getTeam() { return level().getScoreboard().getPlayersTeam(getScoreboardName()); }
/*      */ 
/*      */   
/*      */   public final boolean isAlliedTo(Entity other) {
/* 2988 */     if (other == null) {
/* 2989 */       return false;
/*      */     }
/* 2991 */     return (this == other || 
/* 2992 */       considersEntityAsAlly(other) || other
/* 2993 */       .considersEntityAsAlly(this));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3001 */   protected boolean considersEntityAsAlly(Entity other) { return isAlliedTo(other.getTeam()); }
/*      */ 
/*      */   
/*      */   public boolean isAlliedTo(Team other) {
/* 3005 */     if (getTeam() != null) {
/* 3006 */       return getTeam().isAlliedTo(other);
/*      */     }
/* 3008 */     return false;
/*      */   }
/*      */ 
/*      */   
/* 3012 */   public void setInvisible(boolean invisible) { setSharedFlag(5, invisible); }
/*      */ 
/*      */ 
/*      */   
/* 3016 */   protected boolean getSharedFlag(int flag) { return ((((Byte)this.entityData.get(DATA_SHARED_FLAGS_ID)).byteValue() & 1 << flag) != 0); }
/*      */ 
/*      */   
/*      */   protected void setSharedFlag(int flag, boolean value) {
/* 3020 */     byte currentValue = ((Byte)this.entityData.get(DATA_SHARED_FLAGS_ID)).byteValue();
/* 3021 */     if (value) {
/* 3022 */       this.entityData.set(DATA_SHARED_FLAGS_ID, Byte.valueOf((byte)(currentValue | 1 << flag)));
/*      */     } else {
/* 3024 */       this.entityData.set(DATA_SHARED_FLAGS_ID, Byte.valueOf((byte)(currentValue & (1 << flag ^ 0xFFFFFFFF))));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 3029 */   public int getMaxAirSupply() { return 300; }
/*      */ 
/*      */ 
/*      */   
/* 3033 */   public int getAirSupply() { return ((Integer)this.entityData.get(DATA_AIR_SUPPLY_ID)).intValue(); }
/*      */ 
/*      */ 
/*      */   
/* 3037 */   public void setAirSupply(int supply) { this.entityData.set(DATA_AIR_SUPPLY_ID, Integer.valueOf(supply)); }
/*      */ 
/*      */ 
/*      */   
/* 3041 */   public void clearFreeze() { setTicksFrozen(0); }
/*      */ 
/*      */ 
/*      */   
/* 3045 */   public int getTicksFrozen() { return ((Integer)this.entityData.get(DATA_TICKS_FROZEN)).intValue(); }
/*      */ 
/*      */ 
/*      */   
/* 3049 */   public void setTicksFrozen(int ticks) { this.entityData.set(DATA_TICKS_FROZEN, Integer.valueOf(ticks)); }
/*      */ 
/*      */   
/*      */   public float getPercentFrozen() {
/* 3053 */     int ticksToFreeze = getTicksRequiredToFreeze();
/* 3054 */     return Math.min(getTicksFrozen(), ticksToFreeze) / ticksToFreeze;
/*      */   }
/*      */ 
/*      */   
/* 3058 */   public boolean isFullyFrozen() { return (getTicksFrozen() >= getTicksRequiredToFreeze()); }
/*      */ 
/*      */ 
/*      */   
/* 3062 */   public int getTicksRequiredToFreeze() { return 140; }
/*      */ 
/*      */   
/*      */   public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
/* 3066 */     setRemainingFireTicks(this.remainingFireTicks + 1);
/* 3067 */     if (this.remainingFireTicks == 0) {
/* 3068 */       igniteForSeconds(8.0F);
/*      */     }
/* 3070 */     hurtServer(level, damageSources().lightningBolt(), 5.0F);
/*      */   }
/*      */ 
/*      */   
/* 3074 */   public void onAboveBubbleColumn(boolean dragDown, BlockPos pos) { handleOnAboveBubbleColumn(this, dragDown, pos); }
/*      */   
/*      */   protected static void handleOnAboveBubbleColumn(Entity entity, boolean dragDown, BlockPos pos) {
/*      */     double yd;
/* 3078 */     Vec3 movement = entity.getDeltaMovement();
/*      */     
/* 3080 */     if (dragDown) {
/* 3081 */       yd = Math.max(-0.9D, movement.y - 0.03D);
/*      */     } else {
/* 3083 */       yd = Math.min(1.8D, movement.y + 0.1D);
/*      */     } 
/* 3085 */     entity.setDeltaMovement(movement.x, yd, movement.z);
/* 3086 */     sendBubbleColumnParticles(entity.level, pos);
/*      */   }
/*      */   
/*      */   protected static void sendBubbleColumnParticles(Level level, BlockPos pos) {
/* 3090 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 3091 */       for (int i = 0; i < 2; i++) {
/* 3092 */         serverLevel.sendParticles(ParticleTypes.SPLASH, pos.getX() + level.random.nextDouble(), (pos.getY() + 1), pos.getZ() + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 1.0D);
/* 3093 */         serverLevel.sendParticles(ParticleTypes.BUBBLE, pos.getX() + level.random.nextDouble(), (pos.getY() + 1), pos.getZ() + level.random.nextDouble(), 1, 0.0D, 0.01D, 0.0D, 0.2D);
/*      */       }  }
/*      */   
/*      */   }
/*      */ 
/*      */   
/* 3099 */   public void onInsideBubbleColumn(boolean dragDown) { handleOnInsideBubbleColumn(this, dragDown); }
/*      */   
/*      */   protected static void handleOnInsideBubbleColumn(Entity entity, boolean dragDown) {
/*      */     double yd;
/* 3103 */     Vec3 movement = entity.getDeltaMovement();
/*      */     
/* 3105 */     if (dragDown) {
/* 3106 */       yd = Math.max(-0.3D, movement.y - 0.03D);
/*      */     } else {
/* 3108 */       yd = Math.min(0.7D, movement.y + 0.06D);
/*      */     } 
/* 3110 */     entity.setDeltaMovement(movement.x, yd, movement.z);
/* 3111 */     entity.resetFallDistance();
/*      */   }
/*      */ 
/*      */   
/* 3115 */   public boolean killedEntity(ServerLevel level, LivingEntity entity, DamageSource source) { return true; }
/*      */ 
/*      */   
/*      */   public void checkFallDistanceAccumulation() {
/* 3119 */     if (getDeltaMovement().y() > -0.5D && this.fallDistance > 1.0D) {
/* 3120 */       this.fallDistance = 1.0D;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 3125 */   public void resetFallDistance() { this.fallDistance = 0.0D; }
/*      */ 
/*      */   
/*      */   protected void moveTowardsClosestSpace(double x, double y, double z) {
/* 3129 */     BlockPos pos = BlockPos.containing(x, y, z);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3134 */     Vec3 delta = new Vec3(x - pos.getX(), y - pos.getY(), z - pos.getZ());
/*      */ 
/*      */     
/* 3137 */     BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
/* 3138 */     Direction closestDirection = Direction.UP;
/* 3139 */     double closest = Double.MAX_VALUE;
/* 3140 */     for (Direction direction : new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP }) {
/* 3141 */       neighborPos.setWithOffset(pos, direction);
/* 3142 */       if (!level().getBlockState(neighborPos).isCollisionShapeFullBlock(level(), neighborPos)) {
/* 3143 */         double d = delta.get(direction.getAxis());
/* 3144 */         double orientedDelta = (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) ? (1.0D - d) : d;
/* 3145 */         if (orientedDelta < closest) {
/* 3146 */           closest = orientedDelta;
/* 3147 */           closestDirection = direction;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 3152 */     float speed = this.random.nextFloat() * 0.2F + 0.1F;
/* 3153 */     float step = closestDirection.getAxisDirection().getStep();
/*      */     
/* 3155 */     Vec3 scaledMovement = getDeltaMovement().scale(0.75D);
/* 3156 */     if (closestDirection.getAxis() == Direction.Axis.X) {
/* 3157 */       setDeltaMovement((step * speed), scaledMovement.y, scaledMovement.z);
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 3162 */     else if (closestDirection.getAxis() == Direction.Axis.Y) {
/* 3163 */       setDeltaMovement(scaledMovement.x, (step * speed), scaledMovement.z);
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 3168 */     else if (closestDirection.getAxis() == Direction.Axis.Z) {
/* 3169 */       setDeltaMovement(scaledMovement.x, scaledMovement.y, (step * speed));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void makeStuckInBlock(BlockState blockState, Vec3 speedMultiplier) {
/* 3178 */     resetFallDistance();
/* 3179 */     this.stuckSpeedMultiplier = speedMultiplier;
/*      */   }
/*      */   
/*      */   private static Component removeAction(Component component) {
/* 3183 */     MutableComponent result = component.plainCopy().setStyle(component.getStyle().withClickEvent(null));
/* 3184 */     for (Component s : component.getSiblings()) {
/* 3185 */       result.append(removeAction(s));
/*      */     }
/* 3187 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public Component getName() {
/* 3192 */     Component customName = getCustomName();
/* 3193 */     if (customName != null) {
/* 3194 */       return removeAction(customName);
/*      */     }
/* 3196 */     return getTypeName();
/*      */   }
/*      */ 
/*      */   
/* 3200 */   protected Component getTypeName() { return this.type.getDescription(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3205 */   public boolean is(Entity other) { return (this == other); }
/*      */ 
/*      */ 
/*      */   
/* 3209 */   public float getYHeadRot() { return 0.0F; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setYHeadRot(float yHeadRot) {}
/*      */ 
/*      */   
/*      */   public void setYBodyRot(float yBodyRot) {}
/*      */ 
/*      */   
/* 3219 */   public boolean isAttackable() { return true; }
/*      */ 
/*      */ 
/*      */   
/* 3223 */   public boolean skipAttackInteraction(Entity source) { return false; }
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 3228 */     String levelId = (level() == null) ? "~NULL~" : level().toString();
/* 3229 */     if (this.removalReason != null) {
/* 3230 */       return String.format(Locale.ROOT, "%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f, removed=%s]", new Object[] { getClass().getSimpleName(), getPlainTextName(), Integer.valueOf(this.id), levelId, Double.valueOf(getX()), Double.valueOf(getY()), Double.valueOf(getZ()), this.removalReason });
/*      */     }
/* 3232 */     return String.format(Locale.ROOT, "%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", new Object[] { getClass().getSimpleName(), getPlainTextName(), Integer.valueOf(this.id), levelId, Double.valueOf(getX()), Double.valueOf(getY()), Double.valueOf(getZ()) });
/*      */   }
/*      */ 
/*      */   
/*      */   protected final boolean isInvulnerableToBase(DamageSource source) {
/* 3237 */     return (isRemoved() || (this.invulnerable && 
/* 3238 */       !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !source.isCreativePlayer()) || (source
/* 3239 */       .is(DamageTypeTags.IS_FIRE) && fireImmune()) || (source
/* 3240 */       .is(DamageTypeTags.IS_FALL) && getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE)));
/*      */   }
/*      */ 
/*      */   
/* 3244 */   public boolean isInvulnerable() { return this.invulnerable; }
/*      */ 
/*      */ 
/*      */   
/* 3248 */   public void setInvulnerable(boolean invulnerable) { this.invulnerable = invulnerable; }
/*      */ 
/*      */ 
/*      */   
/* 3252 */   public void copyPosition(Entity target) { snapTo(target.getX(), target.getY(), target.getZ(), target.getYRot(), target.getXRot()); }
/*      */ 
/*      */   
/*      */   public void restoreFrom(Entity oldEntity) {
/* 3256 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(problemPath(), LOGGER); 
/* 3257 */     try { TagValueOutput entityData = TagValueOutput.createWithContext(reporter, oldEntity.registryAccess());
/* 3258 */       oldEntity.saveWithoutId(entityData);
/* 3259 */       load(TagValueInput.create(reporter, registryAccess(), entityData.buildResult()));
/* 3260 */       reporter.close(); } catch (Throwable throwable) { try { reporter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 3261 */      this.portalCooldown = oldEntity.portalCooldown;
/* 3262 */     this.portalProcess = oldEntity.portalProcess;
/*      */   }
/*      */   
/*      */   public Entity teleport(TeleportTransition transition) {
/* 3266 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1; if (!isRemoved()) {
/*      */ 
/*      */ 
/*      */         
/* 3270 */         ServerLevel newLevel = transition.newLevel();
/* 3271 */         boolean otherDimension = (newLevel.dimension() != serverLevel.dimension());
/*      */         
/* 3273 */         if (!transition.asPassenger()) {
/* 3274 */           stopRiding();
/*      */         }
/*      */         
/* 3277 */         if (otherDimension) {
/* 3278 */           return teleportCrossDimension(serverLevel, newLevel, transition);
/*      */         }
/* 3280 */         return teleportSameDimension(serverLevel, transition);
/*      */       }  }
/*      */     
/*      */     return null;
/*      */   } private Entity teleportSameDimension(ServerLevel level, TeleportTransition transition) {
/* 3285 */     for (Entity passenger : getPassengers())
/*      */     {
/* 3287 */       passenger.teleport(calculatePassengerTransition(transition, passenger));
/*      */     }
/*      */     
/* 3290 */     ProfilerFiller profiler = Profiler.get();
/* 3291 */     profiler.push("teleportSameDimension");
/*      */     
/* 3293 */     teleportSetPosition(PositionMoveRotation.of(transition), transition.relatives());
/* 3294 */     if (!transition.asPassenger()) {
/* 3295 */       sendTeleportTransitionToRidingPlayers(transition);
/*      */     }
/*      */     
/* 3298 */     transition.postTeleportTransition().onTransition(this);
/*      */     
/* 3300 */     profiler.pop();
/* 3301 */     return this;
/*      */   }
/*      */   
/*      */   private Entity teleportCrossDimension(ServerLevel oldLevel, ServerLevel newLevel, TeleportTransition transition) {
/* 3305 */     List<Entity> oldPassengers = getPassengers();
/* 3306 */     List<Entity> newPassengers = new ArrayList<Entity>(oldPassengers.size());
/*      */ 
/*      */     
/* 3309 */     ejectPassengers();
/*      */     
/* 3311 */     for (Entity passenger : oldPassengers) {
/* 3312 */       Entity newPassenger = passenger.teleport(calculatePassengerTransition(transition, passenger));
/* 3313 */       if (newPassenger != null) {
/* 3314 */         newPassengers.add(newPassenger);
/*      */       }
/*      */     } 
/* 3317 */     ProfilerFiller profiler = Profiler.get();
/* 3318 */     profiler.push("teleportCrossDimension");
/* 3319 */     Entity newEntity = getType().create(newLevel, EntitySpawnReason.DIMENSION_TRAVEL);
/*      */     
/* 3321 */     if (newEntity == null) {
/* 3322 */       profiler.pop();
/* 3323 */       return null;
/*      */     } 
/*      */     
/* 3326 */     newEntity.restoreFrom(this);
/* 3327 */     removeAfterChangingDimensions();
/*      */ 
/*      */     
/* 3330 */     newEntity.teleportSetPosition(PositionMoveRotation.of(this), PositionMoveRotation.of(transition), transition.relatives());
/* 3331 */     newLevel.addDuringTeleport(newEntity);
/*      */     
/* 3333 */     for (Entity newPassenger : newPassengers) {
/* 3334 */       newPassenger.startRiding(newEntity, true, false);
/*      */     }
/*      */     
/* 3337 */     newLevel.resetEmptyTime();
/* 3338 */     transition.postTeleportTransition().onTransition(newEntity);
/*      */     
/* 3340 */     teleportSpectators(transition, oldLevel);
/*      */     
/* 3342 */     profiler.pop();
/* 3343 */     return newEntity;
/*      */   }
/*      */   
/*      */   protected void teleportSpectators(TeleportTransition transition, ServerLevel oldLevel) {
/* 3347 */     List<ServerPlayer> players = List.copyOf(oldLevel.players());
/* 3348 */     for (ServerPlayer serverPlayer : players) {
/* 3349 */       if (serverPlayer.getCamera() == this) {
/* 3350 */         serverPlayer.teleport(transition);
/* 3351 */         serverPlayer.setCamera(null);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private TeleportTransition calculatePassengerTransition(TeleportTransition transition, Entity passenger) {
/* 3357 */     float passengerYRot = transition.yRot() + (transition.relatives().contains(Relative.Y_ROT) ? 0.0F : (passenger.getYRot() - getYRot()));
/* 3358 */     float passengerXRot = transition.xRot() + (transition.relatives().contains(Relative.X_ROT) ? 0.0F : (passenger.getXRot() - getXRot()));
/*      */     
/* 3360 */     Vec3 passengerOffset = passenger.position().subtract(position());
/* 3361 */     Vec3 passengerPos = transition.position().add(
/* 3362 */         transition.relatives().contains(Relative.X) ? 0.0D : passengerOffset.x(), 
/* 3363 */         transition.relatives().contains(Relative.Y) ? 0.0D : passengerOffset.y(), 
/* 3364 */         transition.relatives().contains(Relative.Z) ? 0.0D : passengerOffset.z());
/*      */ 
/*      */     
/* 3367 */     return transition
/* 3368 */       .withPosition(passengerPos)
/* 3369 */       .withRotation(passengerYRot, passengerXRot)
/* 3370 */       .transitionAsPassenger();
/*      */   }
/*      */   
/*      */   private void sendTeleportTransitionToRidingPlayers(TeleportTransition transition) {
/* 3374 */     Entity controller = getControllingPassenger();
/* 3375 */     for (Entity passenger : getIndirectPassengers()) {
/* 3376 */       if (passenger instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)passenger;
/* 3377 */         if (controller != null && player.getId() == controller.getId()) {
/* 3378 */           player.connection.send(ClientboundTeleportEntityPacket.teleport(getId(), PositionMoveRotation.of(transition), transition.relatives(), this.onGround)); continue;
/*      */         } 
/* 3380 */         player.connection.send(ClientboundTeleportEntityPacket.teleport(getId(), PositionMoveRotation.of(this), Set.of(), this.onGround)); }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 3387 */   public void teleportSetPosition(PositionMoveRotation destination, Set<Relative> relatives) { teleportSetPosition(PositionMoveRotation.of(this), destination, relatives); }
/*      */ 
/*      */   
/*      */   public void teleportSetPosition(PositionMoveRotation currentValues, PositionMoveRotation destination, Set<Relative> relatives) {
/* 3391 */     PositionMoveRotation absoluteDestination = PositionMoveRotation.calculateAbsolute(currentValues, destination, relatives);
/* 3392 */     setPosRaw((absoluteDestination.position()).x, (absoluteDestination.position()).y, (absoluteDestination.position()).z);
/* 3393 */     setYRot(absoluteDestination.yRot());
/* 3394 */     setYHeadRot(absoluteDestination.yRot());
/* 3395 */     setXRot(absoluteDestination.xRot());
/* 3396 */     reapplyPosition();
/* 3397 */     setOldPosAndRot();
/* 3398 */     setDeltaMovement(absoluteDestination.deltaMovement());
/* 3399 */     clearMovementThisTick();
/*      */   }
/*      */   
/*      */   public void forceSetRotation(float yRot, boolean relativeY, float xRot, boolean relativeX) {
/* 3403 */     Set<Relative> relatives = Relative.rotation(relativeY, relativeX);
/* 3404 */     PositionMoveRotation currentValues = PositionMoveRotation.of(this);
/* 3405 */     PositionMoveRotation destination = currentValues.withRotation(yRot, xRot);
/* 3406 */     PositionMoveRotation absoluteDestination = PositionMoveRotation.calculateAbsolute(currentValues, destination, relatives);
/*      */     
/* 3408 */     setYRot(absoluteDestination.yRot());
/* 3409 */     setYHeadRot(absoluteDestination.yRot());
/* 3410 */     setXRot(absoluteDestination.xRot());
/* 3411 */     setOldRot();
/*      */   }
/*      */   
/*      */   public void placePortalTicket(BlockPos ticketPosition) {
/* 3415 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1;
/* 3416 */       serverLevel.getChunkSource().addTicketWithRadius(TicketType.PORTAL, new ChunkPos(ticketPosition), 3); }
/*      */   
/*      */   }
/*      */   
/*      */   protected void removeAfterChangingDimensions() {
/* 3421 */     setRemoved(RemovalReason.CHANGED_DIMENSION);
/* 3422 */     Entity entity1 = this; if (entity1 instanceof Leashable) { Leashable leashable = (Leashable)entity1;
/*      */       
/* 3424 */       leashable.removeLeash(); }
/*      */     
/* 3426 */     Entity entity2 = this; if (entity2 instanceof WaypointTransmitter) { WaypointTransmitter waypoint = (WaypointTransmitter)entity2; Level level1 = this.level; if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1;
/* 3427 */         serverLevel.getWaypointManager().untrackWaypoint(waypoint); }
/*      */        }
/*      */   
/*      */   }
/*      */   
/* 3432 */   public Vec3 getRelativePortalPosition(Direction.Axis axis, BlockUtil.FoundRectangle portalArea) { return PortalShape.getRelativePosition(portalArea, axis, position(), getDimensions(getPose())); }
/*      */ 
/*      */ 
/*      */   
/* 3436 */   public boolean canUsePortal(boolean ignorePassenger) { return ((ignorePassenger || !isPassenger()) && isAlive()); }
/*      */ 
/*      */   
/*      */   public boolean canTeleport(Level from, Level to) {
/* 3440 */     if (from.dimension() == Level.END && to.dimension() == Level.OVERWORLD)
/* 3441 */       for (Entity passenger : getPassengers()) {
/* 3442 */         if (passenger instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)passenger; if (!player.seenCredits) {
/* 3443 */             return false;
/*      */           } }
/*      */       
/*      */       }  
/* 3447 */     return true;
/*      */   }
/*      */ 
/*      */   
/* 3451 */   public float getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState block, FluidState fluid, float resistance) { return resistance; }
/*      */ 
/*      */ 
/*      */   
/* 3455 */   public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) { return true; }
/*      */ 
/*      */ 
/*      */   
/* 3459 */   public int getMaxFallDistance() { return 3; }
/*      */ 
/*      */ 
/*      */   
/* 3463 */   public boolean isIgnoringBlockTriggers() { return false; }
/*      */ 
/*      */   
/*      */   public void fillCrashReportCategory(CrashReportCategory category) {
/* 3467 */     category.setDetail("Entity Type", () -> String.valueOf(EntityType.getKey(getType())) + " (" + String.valueOf(EntityType.getKey(getType())) + ")");
/* 3468 */     category.setDetail("Entity ID", Integer.valueOf(this.id));
/* 3469 */     category.setDetail("Entity Name", () -> getPlainTextName());
/* 3470 */     category.setDetail("Entity's Exact location", String.format(Locale.ROOT, "%.2f, %.2f, %.2f", new Object[] { Double.valueOf(getX()), Double.valueOf(getY()), Double.valueOf(getZ()) }));
/* 3471 */     category.setDetail("Entity's Block location", CrashReportCategory.formatLocation(level(), Mth.floor(getX()), Mth.floor(getY()), Mth.floor(getZ())));
/* 3472 */     Vec3 movement = getDeltaMovement();
/* 3473 */     category.setDetail("Entity's Momentum", String.format(Locale.ROOT, "%.2f, %.2f, %.2f", new Object[] { Double.valueOf(movement.x), Double.valueOf(movement.y), Double.valueOf(movement.z) }));
/* 3474 */     category.setDetail("Entity's Passengers", () -> getPassengers().toString());
/* 3475 */     category.setDetail("Entity's Vehicle", () -> String.valueOf(getVehicle()));
/*      */   }
/*      */ 
/*      */   
/* 3479 */   public boolean displayFireAnimation() { return (isOnFire() && !isSpectator()); }
/*      */ 
/*      */   
/*      */   public void setUUID(UUID uuid) {
/* 3483 */     this.uuid = uuid;
/* 3484 */     this.stringUUID = this.uuid.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 3489 */   public UUID getUUID() { return this.uuid; }
/*      */ 
/*      */ 
/*      */   
/* 3493 */   public String getStringUUID() { return this.stringUUID; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3498 */   public String getScoreboardName() { return this.stringUUID; }
/*      */ 
/*      */ 
/*      */   
/* 3502 */   public boolean isPushedByFluid() { return true; }
/*      */ 
/*      */ 
/*      */   
/* 3506 */   public static double getViewScale() { return viewScale; }
/*      */ 
/*      */ 
/*      */   
/* 3510 */   public static void setViewScale(double viewScale) { Entity.viewScale = viewScale; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3515 */   public Component getDisplayName() { return PlayerTeam.formatNameForTeam(getTeam(), getName()).withStyle(s -> s.withHoverEvent(createHoverEvent()).withInsertion(getStringUUID())); }
/*      */ 
/*      */ 
/*      */   
/* 3519 */   public void setCustomName(Component name) { this.entityData.set(DATA_CUSTOM_NAME, Optional.ofNullable(name)); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3524 */   public Component getCustomName() { return (Component)((Optional)this.entityData.get(DATA_CUSTOM_NAME)).orElse(null); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3529 */   public boolean hasCustomName() { return ((Optional)this.entityData.get(DATA_CUSTOM_NAME)).isPresent(); }
/*      */ 
/*      */ 
/*      */   
/* 3533 */   public void setCustomNameVisible(boolean visible) { this.entityData.set(DATA_CUSTOM_NAME_VISIBLE, Boolean.valueOf(visible)); }
/*      */ 
/*      */ 
/*      */   
/* 3537 */   public boolean isCustomNameVisible() { return ((Boolean)this.entityData.get(DATA_CUSTOM_NAME_VISIBLE)).booleanValue(); }
/*      */ 
/*      */   
/*      */   public boolean teleportTo(ServerLevel level, double x, double y, double z, Set<Relative> relatives, float newYRot, float newXRot, boolean resetCamera) {
/* 3541 */     Entity newEntity = teleport(new TeleportTransition(level, new Vec3(x, y, z), Vec3.ZERO, newYRot, newXRot, relatives, TeleportTransition.DO_NOTHING));
/* 3542 */     return (newEntity != null);
/*      */   }
/*      */ 
/*      */   
/* 3546 */   public void dismountTo(double x, double y, double z) { teleportTo(x, y, z); }
/*      */ 
/*      */   
/*      */   public void teleportTo(double x, double y, double z) {
/* 3550 */     if (!(level() instanceof ServerLevel)) {
/*      */       return;
/*      */     }
/* 3553 */     snapTo(x, y, z, getYRot(), getXRot());
/* 3554 */     teleportPassengers();
/*      */   }
/*      */   
/*      */   private void teleportPassengers() {
/* 3558 */     getSelfAndPassengers().forEach(entity -> {
/* 3559 */           for (UnmodifiableIterator unmodifiableIterator = entity.passengers.iterator(); unmodifiableIterator.hasNext(); ) { Entity passenger = (Entity)unmodifiableIterator.next();
/* 3560 */             entity.positionRider(passenger, Entity::snapTo); }
/*      */         
/*      */         });
/*      */   }
/*      */ 
/*      */   
/* 3566 */   public void teleportRelative(double dx, double dy, double dz) { teleportTo(getX() + dx, getY() + dy, getZ() + dz); }
/*      */ 
/*      */ 
/*      */   
/* 3570 */   public boolean shouldShowName() { return isCustomNameVisible(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onSyncedDataUpdated(List<SynchedEntityData.DataValue<?>> updatedItems) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 3582 */     if (DATA_POSE.equals(accessor)) {
/* 3583 */       refreshDimensions();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected void fixupDimensions() {
/* 3594 */     Pose pose = getPose();
/* 3595 */     EntityDimensions newDim = getDimensions(pose);
/*      */     
/* 3597 */     this.dimensions = newDim;
/* 3598 */     this.eyeHeight = newDim.eyeHeight();
/*      */   }
/*      */   
/*      */   public void refreshDimensions() {
/* 3602 */     EntityDimensions oldDim = this.dimensions;
/* 3603 */     Pose pose = getPose();
/* 3604 */     EntityDimensions newDim = getDimensions(pose);
/*      */     
/* 3606 */     this.dimensions = newDim;
/* 3607 */     this.eyeHeight = newDim.eyeHeight();
/*      */     
/* 3609 */     reapplyPosition();
/*      */     
/* 3611 */     boolean isSmall = (newDim.width() <= 4.0F && newDim.height() <= 4.0F);
/* 3612 */     if (!this.level.isClientSide() && !this.firstTick && !this.noPhysics && isSmall && (newDim.width() > oldDim.width() || newDim.height() > oldDim.height()) && !(this instanceof Player)) {
/* 3613 */       fudgePositionAfterSizeChange(oldDim);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean fudgePositionAfterSizeChange(EntityDimensions previousDimensions) {
/* 3618 */     EntityDimensions newDimensions = getDimensions(getPose());
/* 3619 */     Vec3 oldCenter = position().add(0.0D, previousDimensions.height() / 2.0D, 0.0D);
/* 3620 */     double widthDelta = Math.max(0.0F, newDimensions.width() - previousDimensions.width()) + 1.0E-6D;
/* 3621 */     double heightDelta = Math.max(0.0F, newDimensions.height() - previousDimensions.height()) + 1.0E-6D;
/* 3622 */     VoxelShape allowedCenters = Shapes.create(AABB.ofSize(oldCenter, widthDelta, heightDelta, widthDelta));
/*      */     
/* 3624 */     Optional<Vec3> freePosition = this.level.findFreePosition(this, allowedCenters, oldCenter, newDimensions.width(), newDimensions.height(), newDimensions.width());
/* 3625 */     if (freePosition.isPresent()) {
/* 3626 */       setPos(((Vec3)freePosition.get()).add(0.0D, -newDimensions.height() / 2.0D, 0.0D));
/* 3627 */       return true;
/*      */     } 
/*      */     
/* 3630 */     if (newDimensions.width() > previousDimensions.width() && newDimensions.height() > previousDimensions.height()) {
/*      */       
/* 3632 */       VoxelShape allowedCentersIgnoringY = Shapes.create(AABB.ofSize(oldCenter, widthDelta, 1.0E-6D, widthDelta));
/* 3633 */       Optional<Vec3> freePositionIgnoreVertical = this.level.findFreePosition(this, allowedCentersIgnoringY, oldCenter, newDimensions.width(), previousDimensions.height(), newDimensions.width());
/* 3634 */       if (freePositionIgnoreVertical.isPresent()) {
/* 3635 */         setPos(((Vec3)freePositionIgnoreVertical.get()).add(0.0D, -previousDimensions.height() / 2.0D + 1.0E-6D, 0.0D));
/* 3636 */         return true;
/*      */       } 
/*      */     } 
/* 3639 */     return false;
/*      */   }
/*      */ 
/*      */   
/* 3643 */   public Direction getDirection() { return Direction.fromYRot(getYRot()); }
/*      */ 
/*      */ 
/*      */   
/* 3647 */   public Direction getMotionDirection() { return getDirection(); }
/*      */ 
/*      */ 
/*      */   
/* 3651 */   protected HoverEvent createHoverEvent() { return new HoverEvent.ShowEntity(new HoverEvent.EntityTooltipInfo(getType(), getUUID(), getName())); }
/*      */ 
/*      */ 
/*      */   
/* 3655 */   public boolean broadcastToPlayer(ServerPlayer player) { return true; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3660 */   public final AABB getBoundingBox() { return this.bb; }
/*      */ 
/*      */ 
/*      */   
/* 3664 */   public final void setBoundingBox(AABB bb) { this.bb = bb; }
/*      */ 
/*      */ 
/*      */   
/* 3668 */   public final float getEyeHeight(Pose pose) { return getDimensions(pose).eyeHeight(); }
/*      */ 
/*      */ 
/*      */   
/* 3672 */   public final float getEyeHeight() { return this.eyeHeight; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3677 */   public SlotAccess getSlot(int slot) { return null; }
/*      */ 
/*      */ 
/*      */   
/* 3681 */   public InteractionResult interactAt(Player player, Vec3 location, InteractionHand hand) { return InteractionResult.PASS; }
/*      */ 
/*      */ 
/*      */   
/* 3685 */   public boolean ignoreExplosion(Explosion explosion) { return false; }
/*      */ 
/*      */   
/*      */   public void startSeenByPlayer(ServerPlayer player) {}
/*      */ 
/*      */   
/*      */   public void stopSeenByPlayer(ServerPlayer player) {}
/*      */ 
/*      */   
/*      */   public float rotate(Rotation rotation) {
/* 3695 */     float angle = Mth.wrapDegrees(getYRot());
/* 3696 */     switch (rotation) { case FRONT_BACK: case LEFT_RIGHT: case null:  }  return 
/*      */ 
/*      */ 
/*      */       
/* 3700 */       angle;
/*      */   }
/*      */ 
/*      */   
/*      */   public float mirror(Mirror mirror) {
/* 3705 */     float angle = Mth.wrapDegrees(getYRot());
/* 3706 */     switch (mirror) { case FRONT_BACK: case LEFT_RIGHT:  }  return 
/*      */ 
/*      */       
/* 3709 */       angle;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 3714 */   public ProjectileDeflection deflection(Projectile projectile) { return getType().is(EntityTypeTags.DEFLECTS_PROJECTILES) ? ProjectileDeflection.REVERSE : ProjectileDeflection.NONE; }
/*      */ 
/*      */ 
/*      */   
/* 3718 */   public LivingEntity getControllingPassenger() { return null; }
/*      */ 
/*      */ 
/*      */   
/* 3722 */   public final boolean hasControllingPassenger() { return (getControllingPassenger() != null); }
/*      */ 
/*      */ 
/*      */   
/* 3726 */   public final List<Entity> getPassengers() { return this.passengers; }
/*      */ 
/*      */ 
/*      */   
/* 3730 */   public Entity getFirstPassenger() { return this.passengers.isEmpty() ? null : (Entity)this.passengers.get(0); }
/*      */ 
/*      */ 
/*      */   
/* 3734 */   public boolean hasPassenger(Entity entity) { return this.passengers.contains(entity); }
/*      */ 
/*      */   
/*      */   public boolean hasPassenger(Predicate<Entity> test) {
/* 3738 */     for (UnmodifiableIterator unmodifiableIterator = this.passengers.iterator(); unmodifiableIterator.hasNext(); ) { Entity passenger = (Entity)unmodifiableIterator.next();
/* 3739 */       if (test.test(passenger)) {
/* 3740 */         return true;
/*      */       } }
/*      */     
/* 3743 */     return false;
/*      */   }
/*      */ 
/*      */   
/* 3747 */   private Stream<Entity> getIndirectPassengersStream() { return this.passengers.stream().flatMap(Entity::getSelfAndPassengers); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3752 */   public Stream<Entity> getSelfAndPassengers() { return Stream.concat(Stream.of(this), getIndirectPassengersStream()); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3757 */   public Stream<Entity> getPassengersAndSelf() { return Stream.concat(this.passengers.stream().flatMap(Entity::getPassengersAndSelf), Stream.of(this)); }
/*      */ 
/*      */ 
/*      */   
/* 3761 */   public Iterable<Entity> getIndirectPassengers() { return () -> getIndirectPassengersStream().iterator(); }
/*      */ 
/*      */ 
/*      */   
/* 3765 */   public int countPlayerPassengers() { return (int)getIndirectPassengersStream().filter(e -> e instanceof Player).count(); }
/*      */ 
/*      */ 
/*      */   
/* 3769 */   public boolean hasExactlyOnePlayerPassenger() { return (countPlayerPassengers() == 1); }
/*      */ 
/*      */   
/*      */   public Entity getRootVehicle() {
/* 3773 */     Entity result = this;
/* 3774 */     while (result.isPassenger()) {
/* 3775 */       result = result.getVehicle();
/*      */     }
/* 3777 */     return result;
/*      */   }
/*      */ 
/*      */   
/* 3781 */   public boolean isPassengerOfSameVehicle(Entity other) { return (getRootVehicle() == other.getRootVehicle()); }
/*      */ 
/*      */   
/*      */   public boolean hasIndirectPassenger(Entity entity) {
/* 3785 */     if (!entity.isPassenger()) {
/* 3786 */       return false;
/*      */     }
/* 3788 */     Entity ridden = entity.getVehicle();
/* 3789 */     if (ridden == this) {
/* 3790 */       return true;
/*      */     }
/* 3792 */     return hasIndirectPassenger(ridden);
/*      */   }
/*      */   
/*      */   public final boolean isLocalInstanceAuthoritative() {
/* 3796 */     if (this.level.isClientSide()) {
/* 3797 */       return isLocalClientAuthoritative();
/*      */     }
/* 3799 */     return !isClientAuthoritative();
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean isLocalClientAuthoritative() {
/* 3804 */     LivingEntity passenger = getControllingPassenger();
/* 3805 */     return (passenger != null && passenger.isLocalClientAuthoritative());
/*      */   }
/*      */   
/*      */   public boolean isClientAuthoritative() {
/* 3809 */     LivingEntity passenger = getControllingPassenger();
/* 3810 */     return (passenger != null && passenger.isClientAuthoritative());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3819 */   public boolean canSimulateMovement() { return isLocalInstanceAuthoritative(); }
/*      */ 
/*      */ 
/*      */   
/* 3823 */   public boolean isEffectiveAi() { return isLocalInstanceAuthoritative(); }
/*      */ 
/*      */   
/*      */   protected static Vec3 getCollisionHorizontalEscapeVector(double colliderWidth, double collidingWidth, float directionDegrees) {
/* 3827 */     double distance = (colliderWidth + collidingWidth + 9.999999747378752E-6D) / 2.0D;
/*      */     
/* 3829 */     float directionX = -Mth.sin((directionDegrees * 0.017453292F));
/* 3830 */     float directionZ = Mth.cos((directionDegrees * 0.017453292F));
/*      */     
/* 3832 */     float scale = Math.max(Math.abs(directionX), Math.abs(directionZ));
/*      */     
/* 3834 */     return new Vec3(directionX * distance / scale, 0.0D, directionZ * distance / scale);
/*      */   }
/*      */ 
/*      */   
/* 3838 */   public Vec3 getDismountLocationForPassenger(LivingEntity passenger) { return new Vec3(getX(), (getBoundingBox()).maxY, getZ()); }
/*      */ 
/*      */ 
/*      */   
/* 3842 */   public Entity getVehicle() { return this.vehicle; }
/*      */ 
/*      */ 
/*      */   
/* 3846 */   public Entity getControlledVehicle() { return (this.vehicle != null && this.vehicle.getControllingPassenger() == this) ? this.vehicle : null; }
/*      */ 
/*      */ 
/*      */   
/* 3850 */   public PushReaction getPistonPushReaction() { return PushReaction.NORMAL; }
/*      */ 
/*      */ 
/*      */   
/* 3854 */   public SoundSource getSoundSource() { return SoundSource.NEUTRAL; }
/*      */ 
/*      */ 
/*      */   
/* 3858 */   protected int getFireImmuneTicks() { return 0; }
/*      */ 
/*      */ 
/*      */   
/* 3862 */   public CommandSourceStack createCommandSourceStackForNameResolution(ServerLevel level) { return new CommandSourceStack(CommandSource.NULL, position(), getRotationVector(), level, PermissionSet.NO_PERMISSIONS, getPlainTextName(), getDisplayName(), level.getServer(), this); }
/*      */ 
/*      */   
/*      */   public void lookAt(EntityAnchorArgument.Anchor anchor, Vec3 pos) {
/* 3866 */     Vec3 from = anchor.apply(this);
/* 3867 */     double xd = pos.x - from.x;
/* 3868 */     double yd = pos.y - from.y;
/* 3869 */     double zd = pos.z - from.z;
/* 3870 */     double sd = Math.sqrt(xd * xd + zd * zd);
/*      */     
/* 3872 */     setXRot(Mth.wrapDegrees((float)-(Mth.atan2(yd, sd) * 57.2957763671875D)));
/* 3873 */     setYRot(Mth.wrapDegrees((float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F));
/* 3874 */     setYHeadRot(getYRot());
/* 3875 */     this.xRotO = getXRot();
/* 3876 */     this.yRotO = getYRot();
/*      */   }
/*      */ 
/*      */   
/* 3880 */   public float getPreciseBodyRotation(float partial) { return Mth.lerp(partial, this.yRotO, this.yRot); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean updateFluidHeightAndDoFluidPushing(TagKey<Fluid> type, double flowScale) {
/* 3888 */     if (touchingUnloadedChunk()) {
/* 3889 */       return false;
/*      */     }
/*      */     
/* 3892 */     AABB box = getBoundingBox().deflate(0.001D);
/* 3893 */     int x0 = Mth.floor(box.minX);
/* 3894 */     int x1 = Mth.ceil(box.maxX);
/* 3895 */     int y0 = Mth.floor(box.minY);
/* 3896 */     int y1 = Mth.ceil(box.maxY);
/* 3897 */     int z0 = Mth.floor(box.minZ);
/* 3898 */     int z1 = Mth.ceil(box.maxZ);
/*      */     
/* 3900 */     double fluidHeight = 0.0D;
/* 3901 */     boolean pushedByFluid = isPushedByFluid();
/*      */     
/* 3903 */     boolean inFluid = false;
/* 3904 */     Vec3 current = Vec3.ZERO;
/* 3905 */     int numberOfCurrents = 0;
/* 3906 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 3907 */     for (int x = x0; x < x1; x++) {
/* 3908 */       for (int y = y0; y < y1; y++) {
/* 3909 */         for (int z = z0; z < z1; z++) {
/* 3910 */           pos.set(x, y, z);
/* 3911 */           FluidState fluidState = level().getFluidState(pos);
/* 3912 */           if (fluidState.is(type)) {
/* 3913 */             double blockFluidHeight = (y + fluidState.getHeight(level(), pos));
/* 3914 */             if (blockFluidHeight >= box.minY) {
/* 3915 */               inFluid = true;
/* 3916 */               fluidHeight = Math.max(blockFluidHeight - box.minY, fluidHeight);
/* 3917 */               if (pushedByFluid) {
/* 3918 */                 Vec3 flow = fluidState.getFlow(level(), pos);
/* 3919 */                 if (fluidHeight < 0.4D) {
/* 3920 */                   flow = flow.scale(fluidHeight);
/*      */                 }
/*      */                 
/* 3923 */                 current = current.add(flow);
/* 3924 */                 numberOfCurrents++;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 3932 */     if (current.length() > 0.0D) {
/* 3933 */       if (numberOfCurrents > 0) {
/* 3934 */         current = current.scale(1.0D / numberOfCurrents);
/*      */       }
/*      */       
/* 3937 */       if (!(this instanceof Player)) {
/* 3938 */         current = current.normalize();
/*      */       }
/*      */       
/* 3941 */       Vec3 oldMovement = getDeltaMovement();
/* 3942 */       current = current.scale(flowScale);
/*      */ 
/*      */       
/* 3945 */       double min = 0.003D;
/* 3946 */       if (Math.abs(oldMovement.x) < 0.003D && Math.abs(oldMovement.z) < 0.003D && current.length() < 0.0045000000000000005D) {
/* 3947 */         current = current.normalize().scale(0.0045000000000000005D);
/*      */       }
/*      */       
/* 3950 */       setDeltaMovement(getDeltaMovement().add(current));
/*      */     } 
/* 3952 */     this.fluidHeight.put(type, fluidHeight);
/* 3953 */     return inFluid;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean touchingUnloadedChunk() {
/* 3958 */     AABB box = getBoundingBox().inflate(1.0D);
/* 3959 */     int x0 = Mth.floor(box.minX);
/* 3960 */     int x1 = Mth.ceil(box.maxX);
/* 3961 */     int z0 = Mth.floor(box.minZ);
/* 3962 */     int z1 = Mth.ceil(box.maxZ);
/* 3963 */     return !level().hasChunksAt(x0, z0, x1, z1);
/*      */   }
/*      */ 
/*      */   
/* 3967 */   public double getFluidHeight(TagKey<Fluid> type) { return this.fluidHeight.getDouble(type); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3974 */   public double getFluidJumpThreshold() { return (getEyeHeight() < 0.4D) ? 0.0D : 0.4D; }
/*      */ 
/*      */ 
/*      */   
/* 3978 */   public final float getBbWidth() { return this.dimensions.width(); }
/*      */ 
/*      */ 
/*      */   
/* 3982 */   public final float getBbHeight() { return this.dimensions.height(); }
/*      */ 
/*      */ 
/*      */   
/* 3986 */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) { return new ClientboundAddEntityPacket(this, serverEntity); }
/*      */ 
/*      */ 
/*      */   
/* 3990 */   public EntityDimensions getDimensions(Pose pose) { return this.type.getDimensions(); }
/*      */ 
/*      */ 
/*      */   
/* 3994 */   public final EntityAttachments getAttachments() { return this.dimensions.attachments(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3999 */   public Vec3 position() { return this.position; }
/*      */ 
/*      */ 
/*      */   
/* 4003 */   public Vec3 trackingPosition() { return position(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 4008 */   public BlockPos blockPosition() { return this.blockPosition; }
/*      */ 
/*      */   
/*      */   public BlockState getInBlockState() {
/* 4012 */     if (this.inBlockState == null) {
/* 4013 */       this.inBlockState = level().getBlockState(blockPosition());
/*      */     }
/* 4015 */     return this.inBlockState;
/*      */   }
/*      */ 
/*      */   
/* 4019 */   public ChunkPos chunkPosition() { return this.chunkPosition; }
/*      */ 
/*      */ 
/*      */   
/* 4023 */   public Vec3 getDeltaMovement() { return this.deltaMovement; }
/*      */ 
/*      */   
/*      */   public void setDeltaMovement(Vec3 deltaMovement) {
/* 4027 */     if (deltaMovement.isFinite()) {
/* 4028 */       this.deltaMovement = deltaMovement;
/*      */     }
/*      */   }
/*      */   
/*      */   public void addDeltaMovement(Vec3 momentum) {
/* 4033 */     if (momentum.isFinite()) {
/* 4034 */       setDeltaMovement(getDeltaMovement().add(momentum));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 4039 */   public void setDeltaMovement(double xd, double yd, double zd) { setDeltaMovement(new Vec3(xd, yd, zd)); }
/*      */ 
/*      */ 
/*      */   
/* 4043 */   public final int getBlockX() { return this.blockPosition.getX(); }
/*      */ 
/*      */ 
/*      */   
/* 4047 */   public final double getX() { return this.position.x; }
/*      */ 
/*      */ 
/*      */   
/* 4051 */   public double getX(double progress) { return this.position.x + getBbWidth() * progress; }
/*      */ 
/*      */ 
/*      */   
/* 4055 */   public double getRandomX(double spread) { return getX((2.0D * this.random.nextDouble() - 1.0D) * spread); }
/*      */ 
/*      */ 
/*      */   
/* 4059 */   public final int getBlockY() { return this.blockPosition.getY(); }
/*      */ 
/*      */ 
/*      */   
/* 4063 */   public final double getY() { return this.position.y; }
/*      */ 
/*      */ 
/*      */   
/* 4067 */   public double getY(double progress) { return this.position.y + getBbHeight() * progress; }
/*      */ 
/*      */ 
/*      */   
/* 4071 */   public double getRandomY() { return getY(this.random.nextDouble()); }
/*      */ 
/*      */ 
/*      */   
/* 4075 */   public double getEyeY() { return this.position.y + this.eyeHeight; }
/*      */ 
/*      */ 
/*      */   
/* 4079 */   public final int getBlockZ() { return this.blockPosition.getZ(); }
/*      */ 
/*      */ 
/*      */   
/* 4083 */   public final double getZ() { return this.position.z; }
/*      */ 
/*      */ 
/*      */   
/* 4087 */   public double getZ(double progress) { return this.position.z + getBbWidth() * progress; }
/*      */ 
/*      */ 
/*      */   
/* 4091 */   public double getRandomZ(double spread) { return getZ((2.0D * this.random.nextDouble() - 1.0D) * spread); }
/*      */ 
/*      */   
/*      */   public final void setPosRaw(double x, double y, double z) {
/* 4095 */     if (this.position.x != x || this.position.y != y || this.position.z != z) {
/* 4096 */       this.position = new Vec3(x, y, z);
/*      */       
/* 4098 */       int fx = Mth.floor(x);
/* 4099 */       int fy = Mth.floor(y);
/* 4100 */       int fz = Mth.floor(z);
/* 4101 */       if (fx != this.blockPosition.getX() || fy != this.blockPosition.getY() || fz != this.blockPosition.getZ()) {
/* 4102 */         this.blockPosition = new BlockPos(fx, fy, fz);
/* 4103 */         this.inBlockState = null;
/* 4104 */         if (SectionPos.blockToSectionCoord(fx) != this.chunkPosition.x || SectionPos.blockToSectionCoord(fz) != this.chunkPosition.z) {
/* 4105 */           this.chunkPosition = new ChunkPos(this.blockPosition);
/*      */         }
/*      */       } 
/* 4108 */       this.levelCallback.onMove();
/* 4109 */       if (!this.firstTick) { Level level1 = this.level; if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1; if (!isRemoved()) {
/* 4110 */             Entity entity = this; if (entity instanceof WaypointTransmitter) { WaypointTransmitter waypoint = (WaypointTransmitter)entity; if (waypoint.isTransmittingWaypoint())
/* 4111 */                 serverLevel.getWaypointManager().updateWaypoint(waypoint);  }
/*      */             
/* 4113 */             entity = this; if (entity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)entity; if (player.isReceivingWaypoints() && player.connection != null)
/* 4114 */                 serverLevel.getWaypointManager().updatePlayer(player);  }
/*      */           
/*      */           }  }
/*      */          }
/*      */     
/*      */     } 
/*      */   }
/*      */   
/*      */   public void checkDespawn() {}
/*      */   
/* 4124 */   public Vec3[] getQuadLeashHolderOffsets() { return Leashable.createQuadLeashOffsets(this, 0.0D, 0.5D, 0.5D, 0.0D); }
/*      */ 
/*      */ 
/*      */   
/* 4128 */   public boolean supportQuadLeashAsHolder() { return false; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void notifyLeashHolder(Leashable entity) {}
/*      */ 
/*      */   
/*      */   public void notifyLeasheeRemoved(Leashable entity) {}
/*      */ 
/*      */   
/* 4138 */   public Vec3 getRopeHoldPosition(float partialTickTime) { return getPosition(partialTickTime).add(0.0D, this.eyeHeight * 0.7D, 0.0D); }
/*      */ 
/*      */   
/*      */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 4142 */     int entityId = packet.getId();
/* 4143 */     double x = packet.getX();
/* 4144 */     double y = packet.getY();
/* 4145 */     double z = packet.getZ();
/* 4146 */     syncPacketPositionCodec(x, y, z);
/* 4147 */     snapTo(x, y, z, packet.getYRot(), packet.getXRot());
/* 4148 */     setId(entityId);
/* 4149 */     setUUID(packet.getUUID());
/* 4150 */     setDeltaMovement(packet.getMovement());
/*      */   }
/*      */ 
/*      */   
/* 4154 */   public ItemStack getPickResult() { return null; }
/*      */ 
/*      */ 
/*      */   
/* 4158 */   public void setIsInPowderSnow(boolean isInPowderSnow) { this.isInPowderSnow = isInPowderSnow; }
/*      */ 
/*      */ 
/*      */   
/* 4162 */   public boolean canFreeze() { return !getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES); }
/*      */ 
/*      */ 
/*      */   
/* 4166 */   public boolean isFreezing() { return (getTicksFrozen() > 0); }
/*      */ 
/*      */ 
/*      */   
/* 4170 */   public float getYRot() { return this.yRot; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 4180 */   public float getVisualRotationYInDegrees() { return getYRot(); }
/*      */ 
/*      */   
/*      */   public void setYRot(float yRot) {
/* 4184 */     if (!Float.isFinite(yRot)) {
/* 4185 */       Util.logAndPauseIfInIde("Invalid entity rotation: " + yRot + ", discarding.");
/*      */       return;
/*      */     } 
/* 4188 */     this.yRot = yRot;
/*      */   }
/*      */ 
/*      */   
/* 4192 */   public float getXRot() { return this.xRot; }
/*      */ 
/*      */   
/*      */   public void setXRot(float xRot) {
/* 4196 */     if (!Float.isFinite(xRot)) {
/* 4197 */       Util.logAndPauseIfInIde("Invalid entity rotation: " + xRot + ", discarding.");
/*      */       return;
/*      */     } 
/* 4200 */     this.xRot = Math.clamp(xRot % 360.0F, -90.0F, 90.0F);
/*      */   }
/*      */ 
/*      */   
/* 4204 */   public boolean canSprint() { return false; }
/*      */ 
/*      */ 
/*      */   
/* 4208 */   public float maxUpStep() { return 0.0F; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onExplosionHit(Entity explosionCausedBy) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 4222 */   public final boolean isRemoved() { return (this.removalReason != null); }
/*      */ 
/*      */ 
/*      */   
/* 4226 */   public RemovalReason getRemovalReason() { return this.removalReason; }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setRemoved(RemovalReason reason) {
/* 4231 */     if (this.removalReason == null) {
/* 4232 */       this.removalReason = reason;
/*      */     }
/* 4234 */     if (this.removalReason.shouldDestroy()) {
/* 4235 */       stopRiding();
/*      */     }
/* 4237 */     getPassengers().forEach(Entity::stopRiding);
/* 4238 */     this.levelCallback.onRemove(reason);
/* 4239 */     onRemoval(reason);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 4244 */   protected void unsetRemoved() { this.removalReason = null; }
/*      */   
/*      */   public enum MovementEmission
/*      */   {
/* 4248 */     NONE(false, false),
/* 4249 */     SOUNDS(true, false),
/* 4250 */     EVENTS(false, true),
/* 4251 */     ALL(true, true);
/*      */     
/*      */     final boolean sounds;
/*      */     final boolean events;
/*      */     
/*      */     MovementEmission(boolean sounds, boolean events) {
/* 4257 */       this.sounds = sounds;
/* 4258 */       this.events = events;
/*      */     }
/*      */ 
/*      */     
/* 4262 */     public boolean emitsAnything() { return (this.events || this.sounds); }
/*      */ 
/*      */ 
/*      */     
/* 4266 */     public boolean emitsEvents() { return this.events; }
/*      */ 
/*      */ 
/*      */     
/* 4270 */     public boolean emitsSounds() { return this.sounds; }
/*      */   }
/*      */ 
/*      */   
/*      */   public enum RemovalReason
/*      */   {
/* 4276 */     KILLED(true, false),
/*      */     
/* 4278 */     DISCARDED(true, false),
/*      */     
/* 4280 */     UNLOADED_TO_CHUNK(false, true),
/*      */     
/* 4282 */     UNLOADED_WITH_PLAYER(false, false),
/*      */     
/* 4284 */     CHANGED_DIMENSION(false, false);
/*      */     
/*      */     private final boolean destroy;
/*      */     
/*      */     private final boolean save;
/*      */ 
/*      */     
/*      */     RemovalReason(boolean destroy, boolean save) {
/* 4292 */       this.destroy = destroy;
/* 4293 */       this.save = save;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4300 */     public boolean shouldDestroy() { return this.destroy; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4307 */     public boolean shouldSave() { return this.save; }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 4313 */   public void setLevelCallback(EntityInLevelCallback levelCallback) { this.levelCallback = levelCallback; }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean shouldBeSaved() {
/* 4318 */     if (this.removalReason != null && !this.removalReason.shouldSave()) {
/* 4319 */       return false;
/*      */     }
/* 4321 */     if (isPassenger()) {
/* 4322 */       return false;
/*      */     }
/* 4324 */     return (!isVehicle() || !hasExactlyOnePlayerPassenger());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 4329 */   public boolean isAlwaysTicking() { return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 4336 */   public boolean mayInteract(ServerLevel level, BlockPos pos) { return true; }
/*      */ 
/*      */ 
/*      */   
/* 4340 */   public boolean isFlyingVehicle() { return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 4345 */   public Level level() { return this.level; }
/*      */ 
/*      */ 
/*      */   
/* 4349 */   protected void setLevel(Level level) { this.level = level; }
/*      */ 
/*      */ 
/*      */   
/* 4353 */   public DamageSources damageSources() { return level().damageSources(); }
/*      */ 
/*      */ 
/*      */   
/* 4357 */   public RegistryAccess registryAccess() { return level().registryAccess(); }
/*      */ 
/*      */   
/*      */   protected void lerpPositionAndRotationStep(int stepsToTarget, double targetX, double targetY, double targetZ, double targetYRot, double targetXRot) {
/* 4361 */     double alpha = 1.0D / stepsToTarget;
/*      */     
/* 4363 */     double x = Mth.lerp(alpha, getX(), targetX);
/* 4364 */     double y = Mth.lerp(alpha, getY(), targetY);
/* 4365 */     double z = Mth.lerp(alpha, getZ(), targetZ);
/*      */     
/* 4367 */     float yRot = (float)Mth.rotLerp(alpha, getYRot(), targetYRot);
/*      */     
/* 4369 */     float xRot = (float)Mth.lerp(alpha, getXRot(), targetXRot);
/*      */     
/* 4371 */     setPos(x, y, z);
/* 4372 */     setRot(yRot, xRot);
/*      */   }
/*      */ 
/*      */   
/* 4376 */   public RandomSource getRandom() { return this.random; }
/*      */ 
/*      */   
/*      */   public Vec3 getKnownMovement() {
/* 4380 */     LivingEntity livingEntity = getControllingPassenger(); if (livingEntity instanceof Player) { Player controller = (Player)livingEntity; if (isAlive())
/* 4381 */         return controller.getKnownMovement();  }
/*      */     
/* 4383 */     return getDeltaMovement();
/*      */   }
/*      */   
/*      */   public Vec3 getKnownSpeed() {
/* 4387 */     LivingEntity livingEntity = getControllingPassenger(); if (livingEntity instanceof Player) { Player controller = (Player)livingEntity; if (isAlive())
/* 4388 */         return controller.getKnownSpeed();  }
/*      */     
/* 4390 */     return this.lastKnownSpeed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 4397 */   public ItemStack getWeaponItem() { return null; }
/*      */ 
/*      */ 
/*      */   
/* 4401 */   public Optional<ResourceKey<LootTable>> getLootTable() { return this.type.getDefaultLootTable(); }
/*      */ 
/*      */   
/*      */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 4405 */     applyImplicitComponentIfPresent(components, DataComponents.CUSTOM_NAME);
/* 4406 */     applyImplicitComponentIfPresent(components, DataComponents.CUSTOM_DATA);
/*      */   }
/*      */ 
/*      */   
/* 4410 */   public final void applyComponentsFromItemStack(ItemStack stack) { applyImplicitComponents(stack.getComponents()); }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T get(DataComponentType<? extends T> type) {
/* 4415 */     if (type == DataComponents.CUSTOM_NAME) {
/* 4416 */       return (T)castComponentValue(type, getCustomName());
/*      */     }
/*      */     
/* 4419 */     if (type == DataComponents.CUSTOM_DATA) {
/* 4420 */       return (T)castComponentValue(type, this.customData);
/*      */     }
/*      */     
/* 4423 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Contract("_,!null->!null;_,_->_")
/* 4429 */   protected static <T> T castComponentValue(DataComponentType<T> type, Object value) { return (T)value; }
/*      */ 
/*      */ 
/*      */   
/* 4433 */   public <T> void setComponent(DataComponentType<T> type, T value) { applyImplicitComponent(type, value); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 4440 */     if (type == DataComponents.CUSTOM_NAME) {
/* 4441 */       setCustomName((Component)castComponentValue(DataComponents.CUSTOM_NAME, value));
/* 4442 */       return true;
/*      */     } 
/*      */     
/* 4445 */     if (type == DataComponents.CUSTOM_DATA) {
/* 4446 */       this.customData = (CustomData)castComponentValue(DataComponents.CUSTOM_DATA, value);
/* 4447 */       return true;
/*      */     } 
/*      */     
/* 4450 */     return false;
/*      */   }
/*      */   
/*      */   protected <T> boolean applyImplicitComponentIfPresent(DataComponentGetter components, DataComponentType<T> type) {
/* 4454 */     T value = (T)components.get(type);
/* 4455 */     if (value != null) {
/* 4456 */       return applyImplicitComponent(type, value);
/*      */     }
/* 4458 */     return false;
/*      */   }
/*      */   private static final class EntityPathElement extends Record implements ProblemReporter.PathElement { private final Entity entity;
/* 4461 */     private EntityPathElement(Entity entity) { this.entity = entity; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Entity$EntityPathElement;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #4461	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/entity/Entity$EntityPathElement; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Entity$EntityPathElement;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #4461	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/entity/Entity$EntityPathElement; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Entity$EntityPathElement;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #4461	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/entity/Entity$EntityPathElement;
/* 4461 */       //   0	8	1	o	Ljava/lang/Object; } public Entity entity() { return this.entity; }
/*      */ 
/*      */     
/* 4464 */     public String get() { return this.entity.toString(); } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 4469 */   public ProblemReporter.PathElement problemPath() { return new EntityPathElement(this); }
/*      */   
/*      */   public void registerDebugValues(ServerLevel level, DebugValueSource.Registration registration) {}
/*      */   
/*      */   protected abstract void defineSynchedData(SynchedEntityData.Builder paramBuilder);
/*      */   
/*      */   public abstract boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat);
/*      */   
/*      */   protected abstract void readAdditionalSaveData(ValueInput paramValueInput);
/*      */   
/*      */   protected abstract void addAdditionalSaveData(ValueOutput paramValueOutput);
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface MoveFunction {
/*      */     void accept(Entity param1Entity, double param1Double1, double param1Double2, double param1Double3);
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\Entity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */