/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.BlockUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.InterpolationHandler;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.DismountHelper;
/*     */ import net.minecraft.world.entity.vehicle.VehicleEntity;
/*     */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BaseRailBlock;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.PoweredRailBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class AbstractMinecart
/*     */   extends VehicleEntity
/*     */ {
/*  51 */   private static final Vec3 LOWERED_PASSENGER_ATTACHMENT = new Vec3(0.0D, 0.0D, 0.0D);
/*     */   
/*  53 */   private static final EntityDataAccessor<Optional<BlockState>> DATA_ID_CUSTOM_DISPLAY_BLOCK = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE);
/*  54 */   private static final EntityDataAccessor<Integer> DATA_ID_DISPLAY_OFFSET = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.INT);
/*  55 */   private static final ImmutableMap<Pose, ImmutableList<Integer>> POSE_DISMOUNT_HEIGHTS = ImmutableMap.of(Pose.STANDING, 
/*  56 */       ImmutableList.of(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1)), Pose.CROUCHING, 
/*  57 */       ImmutableList.of(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1)), Pose.SWIMMING, 
/*  58 */       ImmutableList.of(Integer.valueOf(0), Integer.valueOf(1)));
/*     */   
/*     */   protected static final float WATER_SLOWDOWN_FACTOR = 0.95F;
/*     */   private static final boolean DEFAULT_FLIPPED_ROTATION = false;
/*     */   private boolean onRails;
/*     */   private boolean flipped = false;
/*     */   private final MinecartBehavior behavior;
/*     */   
/*     */   protected AbstractMinecart(EntityType<?> type, Level level) {
/*  67 */     super(type, level);
/*  68 */     this.blocksBuilding = true;
/*  69 */     if (useExperimentalMovement(level)) {
/*  70 */       this.behavior = new NewMinecartBehavior(this);
/*     */     } else {
/*  72 */       this.behavior = new OldMinecartBehavior(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected AbstractMinecart(EntityType<?> type, Level level, double x, double y, double z) {
/*  77 */     this(type, level);
/*  78 */     setInitialPos(x, y, z);
/*     */   }
/*     */   
/*     */   public void setInitialPos(double x, double y, double z) {
/*  82 */     setPos(x, y, z);
/*     */ 
/*     */     
/*  85 */     this.xo = x;
/*  86 */     this.yo = y;
/*  87 */     this.zo = z;
/*     */   }
/*     */   
/*     */   public static <T extends AbstractMinecart> T createMinecart(Level level, double x, double y, double z, EntityType<T> type, EntitySpawnReason reason, ItemStack itemStack, Player player) {
/*  91 */     T entity = (T)(AbstractMinecart)type.create(level, reason);
/*  92 */     if (entity != null) {
/*  93 */       entity.setInitialPos(x, y, z);
/*  94 */       EntityType.createDefaultStackConfig(level, itemStack, player).accept(entity);
/*     */       
/*  96 */       MinecartBehavior minecartBehavior = entity.getBehavior(); if (minecartBehavior instanceof NewMinecartBehavior) { NewMinecartBehavior newMinecartBehavior = (NewMinecartBehavior)minecartBehavior;
/*  97 */         BlockPos currentPos = entity.getCurrentBlockPosOrRailBelow();
/*  98 */         BlockState currentState = level.getBlockState(currentPos);
/*  99 */         newMinecartBehavior.adjustToRails(currentPos, currentState, true); }
/*     */     
/*     */     } 
/* 102 */     return entity;
/*     */   }
/*     */ 
/*     */   
/* 106 */   public MinecartBehavior getBehavior() { return this.behavior; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   protected Entity.MovementEmission getMovementEmission() { return Entity.MovementEmission.EVENTS; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 116 */     super.defineSynchedData(entityData);
/* 117 */     entityData.define(DATA_ID_CUSTOM_DISPLAY_BLOCK, Optional.empty());
/* 118 */     entityData.define(DATA_ID_DISPLAY_OFFSET, Integer.valueOf(getDefaultDisplayOffset()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 123 */   public boolean canCollideWith(Entity entity) { return AbstractBoat.canVehicleCollide(this, entity); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   public boolean isPushable() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   public Vec3 getRelativePortalPosition(Direction.Axis axis, BlockUtil.FoundRectangle portalArea) { return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(axis, portalArea)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
/* 140 */     boolean shouldLowerAttachmentPoint = (passenger instanceof net.minecraft.world.entity.npc.villager.Villager || passenger instanceof net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader);
/* 141 */     if (shouldLowerAttachmentPoint) {
/* 142 */       return LOWERED_PASSENGER_ATTACHMENT;
/*     */     }
/* 144 */     return super.getPassengerAttachmentPoint(passenger, dimensions, scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
/* 149 */     Direction forward = getMotionDirection();
/* 150 */     if (forward.getAxis() == Direction.Axis.Y) {
/* 151 */       return super.getDismountLocationForPassenger(passenger);
/*     */     }
/*     */     
/* 154 */     int[][] offsets = DismountHelper.offsetsForDirection(forward);
/* 155 */     BlockPos vehicleBlockPos = blockPosition();
/* 156 */     BlockPos.MutableBlockPos targetBlockPos = new BlockPos.MutableBlockPos();
/*     */     
/* 158 */     ImmutableList<Pose> dismountPoses = passenger.getDismountPoses();
/*     */     
/* 160 */     for (UnmodifiableIterator unmodifiableIterator1 = dismountPoses.iterator(); unmodifiableIterator1.hasNext(); ) { Pose pose = (Pose)unmodifiableIterator1.next();
/* 161 */       EntityDimensions passengerDimensions = passenger.getDimensions(pose);
/*     */ 
/*     */       
/* 164 */       float dismountAreaReach = Math.min(passengerDimensions.width(), 1.0F) / 2.0F;
/*     */       
/* 166 */       for (UnmodifiableIterator unmodifiableIterator = ((ImmutableList)POSE_DISMOUNT_HEIGHTS.get(pose)).iterator(); unmodifiableIterator.hasNext(); ) { int offsetY = ((Integer)unmodifiableIterator.next()).intValue();
/* 167 */         for (int[] offsetXZ : offsets) {
/* 168 */           targetBlockPos.set(vehicleBlockPos.getX() + offsetXZ[0], vehicleBlockPos.getY() + offsetY, vehicleBlockPos.getZ() + offsetXZ[1]);
/*     */           
/* 170 */           double blockFloorHeight = level().getBlockFloorHeight(DismountHelper.nonClimbableShape(level(), targetBlockPos), () -> DismountHelper.nonClimbableShape(level(), targetBlockPos.below()));
/* 171 */           if (DismountHelper.isBlockFloorValid(blockFloorHeight)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 177 */             AABB dismountCollisionBox = new AABB(-dismountAreaReach, 0.0D, -dismountAreaReach, dismountAreaReach, passengerDimensions.height(), dismountAreaReach);
/*     */ 
/*     */             
/* 180 */             Vec3 location = Vec3.upFromBottomCenterOf(targetBlockPos, blockFloorHeight);
/* 181 */             if (DismountHelper.canDismountTo(level(), passenger, dismountCollisionBox.move(location))) {
/* 182 */               passenger.setPose(pose);
/* 183 */               return location;
/*     */             } 
/*     */           } 
/*     */         }  }
/*     */        }
/*     */     
/* 189 */     double vehicleTop = (getBoundingBox()).maxY;
/* 190 */     targetBlockPos.set(vehicleBlockPos.getX(), vehicleTop, vehicleBlockPos.getZ());
/*     */     
/* 192 */     for (UnmodifiableIterator unmodifiableIterator2 = dismountPoses.iterator(); unmodifiableIterator2.hasNext(); ) { Pose pose = (Pose)unmodifiableIterator2.next();
/* 193 */       double poseHeight = passenger.getDimensions(pose).height();
/* 194 */       int blockCoverageY = Mth.ceil(vehicleTop - targetBlockPos.getY() + poseHeight);
/* 195 */       double ceilingAboveVehicle = DismountHelper.findCeilingFrom(targetBlockPos, blockCoverageY, pos -> level().getBlockState(pos).getCollisionShape(level(), pos));
/*     */       
/* 197 */       if (vehicleTop + poseHeight <= ceilingAboveVehicle) {
/* 198 */         passenger.setPose(pose);
/*     */         
/*     */         break;
/*     */       }  }
/*     */     
/* 203 */     return super.getDismountLocationForPassenger(passenger);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getBlockSpeedFactor() {
/* 208 */     BlockState blockState = level().getBlockState(blockPosition());
/* 209 */     if (blockState.is(BlockTags.RAILS)) {
/* 210 */       return 1.0F;
/*     */     }
/* 212 */     return super.getBlockSpeedFactor();
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateHurt(float yaw) {
/* 217 */     setHurtDir(-getHurtDir());
/* 218 */     setHurtTime(10);
/* 219 */     setDamage(getDamage() + getDamage() * 10.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 224 */   public boolean isPickable() { return !isRemoved(); }
/*     */ 
/*     */   
/* 227 */   private static final Map<RailShape, Pair<Vec3i, Vec3i>> EXITS = Maps.newEnumMap((Map)Util.make(() -> {
/* 228 */           xNeg = Direction.WEST.getUnitVec3i();
/* 229 */           Vec3i xPos = Direction.EAST.getUnitVec3i();
/* 230 */           Vec3i zNeg = Direction.NORTH.getUnitVec3i();
/* 231 */           Vec3i zPos = Direction.SOUTH.getUnitVec3i();
/*     */           
/* 233 */           Vec3i xNegBelow = xNeg.below();
/* 234 */           Vec3i xPosBelow = xPos.below();
/* 235 */           Vec3i zNegBelow = zNeg.below();
/* 236 */           Vec3i zPosBelow = zPos.below();
/*     */           
/* 238 */           return ImmutableMap.of(RailShape.NORTH_SOUTH, 
/* 239 */               Pair.of(zNeg, zPos), RailShape.EAST_WEST, 
/* 240 */               Pair.of(xNeg, xPos), RailShape.ASCENDING_EAST, 
/* 241 */               Pair.of(xNegBelow, xPos), RailShape.ASCENDING_WEST, 
/* 242 */               Pair.of(xNeg, xPosBelow), RailShape.ASCENDING_NORTH, 
/* 243 */               Pair.of(zNeg, zPosBelow), RailShape.ASCENDING_SOUTH, 
/* 244 */               Pair.of(zNegBelow, zPos), RailShape.SOUTH_EAST, 
/* 245 */               Pair.of(zPos, xPos), RailShape.SOUTH_WEST, 
/* 246 */               Pair.of(zPos, xNeg), RailShape.NORTH_WEST, 
/* 247 */               Pair.of(zNeg, xNeg), RailShape.NORTH_EAST, 
/* 248 */               Pair.of(zNeg, xPos));
/*     */         }));
/*     */ 
/*     */ 
/*     */   
/* 253 */   public static Pair<Vec3i, Vec3i> exits(RailShape shape) { return (Pair)EXITS.get(shape); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 258 */   public Direction getMotionDirection() { return this.behavior.getMotionDirection(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 263 */   protected double getDefaultGravity() { return isInWater() ? 0.005D : 0.04D; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 268 */     if (getHurtTime() > 0) {
/* 269 */       setHurtTime(getHurtTime() - 1);
/*     */     }
/* 271 */     if (getDamage() > 0.0F) {
/* 272 */       setDamage(getDamage() - 1.0F);
/*     */     }
/* 274 */     checkBelowWorld();
/* 275 */     computeSpeed();
/*     */     
/* 277 */     handlePortal();
/*     */     
/* 279 */     this.behavior.tick();
/*     */     
/* 281 */     updateInWaterStateAndDoFluidPushing();
/*     */     
/* 283 */     if (isInLava()) {
/* 284 */       lavaIgnite();
/* 285 */       lavaHurt();
/* 286 */       this.fallDistance *= 0.5D;
/*     */     } 
/*     */     
/* 289 */     this.firstTick = false;
/*     */   }
/*     */ 
/*     */   
/* 293 */   public boolean isFirstTick() { return this.firstTick; }
/*     */ 
/*     */   
/*     */   public BlockPos getCurrentBlockPosOrRailBelow() {
/* 297 */     int xt = Mth.floor(getX());
/* 298 */     int yt = Mth.floor(getY());
/* 299 */     int zt = Mth.floor(getZ());
/*     */     
/* 301 */     if (useExperimentalMovement(level())) {
/* 302 */       double y = getY() - 0.1D - 9.999999747378752E-6D;
/* 303 */       if (level().getBlockState(BlockPos.containing(xt, y, zt)).is(BlockTags.RAILS)) {
/* 304 */         yt = Mth.floor(y);
/*     */       }
/* 306 */     } else if (level().getBlockState(new BlockPos(xt, yt - 1, zt)).is(BlockTags.RAILS)) {
/* 307 */       yt--;
/*     */     } 
/*     */     
/* 310 */     return new BlockPos(xt, yt, zt);
/*     */   }
/*     */ 
/*     */   
/* 314 */   protected double getMaxSpeed(ServerLevel level) { return this.behavior.getMaxSpeed(level); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateMinecart(ServerLevel level, int xt, int yt, int zt, boolean state) {}
/*     */ 
/*     */ 
/*     */   
/* 322 */   public void lerpPositionAndRotationStep(int stepsToTarget, double targetX, double targetY, double targetZ, double targetYRot, double targetXRot) { super.lerpPositionAndRotationStep(stepsToTarget, targetX, targetY, targetZ, targetYRot, targetXRot); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 327 */   public void applyGravity() { super.applyGravity(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 332 */   public void reapplyPosition() { super.reapplyPosition(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 337 */   public boolean updateInWaterStateAndDoFluidPushing() { return super.updateInWaterStateAndDoFluidPushing(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 342 */   public Vec3 getKnownMovement() { return this.behavior.getKnownMovement(super.getKnownMovement()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 347 */   public InterpolationHandler getInterpolation() { return this.behavior.getInterpolation(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 352 */     super.recreateFromPacket(packet);
/* 353 */     this.behavior.lerpMotion(getDeltaMovement());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 358 */   public void lerpMotion(Vec3 movement) { this.behavior.lerpMotion(movement); }
/*     */ 
/*     */ 
/*     */   
/* 362 */   protected void moveAlongTrack(ServerLevel level) { this.behavior.moveAlongTrack(level); }
/*     */ 
/*     */   
/*     */   protected void comeOffTrack(ServerLevel level) {
/* 366 */     double maxSpeed = getMaxSpeed(level);
/* 367 */     Vec3 movement = getDeltaMovement();
/* 368 */     setDeltaMovement(
/* 369 */         Mth.clamp(movement.x, -maxSpeed, maxSpeed), movement.y, 
/*     */         
/* 371 */         Mth.clamp(movement.z, -maxSpeed, maxSpeed));
/*     */     
/* 373 */     if (onGround()) {
/* 374 */       setDeltaMovement(getDeltaMovement().scale(0.5D));
/*     */     }
/* 376 */     move(MoverType.SELF, getDeltaMovement());
/*     */     
/* 378 */     if (!onGround()) {
/* 379 */       setDeltaMovement(getDeltaMovement().scale(0.95D));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 387 */   protected double makeStepAlongTrack(BlockPos pos, RailShape shape, double movementLeft) { return this.behavior.stepAlongTrack(pos, shape, movementLeft); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void move(MoverType moverType, Vec3 delta) {
/* 392 */     if (useExperimentalMovement(level())) {
/* 393 */       Vec3 toPosition = position().add(delta);
/* 394 */       super.move(moverType, delta);
/* 395 */       boolean shouldContinue = this.behavior.pushAndPickupEntities();
/* 396 */       if (shouldContinue)
/*     */       {
/* 398 */         super.move(moverType, toPosition.subtract(position()));
/*     */       }
/* 400 */       if (moverType.equals(MoverType.PISTON)) {
/* 401 */         this.onRails = false;
/*     */       }
/*     */     } else {
/* 404 */       super.move(moverType, delta);
/*     */       
/* 406 */       applyEffectsFromBlocks();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyEffectsFromBlocks() {
/* 412 */     if (useExperimentalMovement(level())) {
/* 413 */       super.applyEffectsFromBlocks();
/*     */     } else {
/*     */       
/* 416 */       applyEffectsFromBlocks(position(), position());
/* 417 */       clearMovementThisTick();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 423 */   public boolean isOnRails() { return this.onRails; }
/*     */ 
/*     */ 
/*     */   
/* 427 */   public void setOnRails(boolean onRails) { this.onRails = onRails; }
/*     */ 
/*     */ 
/*     */   
/* 431 */   public boolean isFlipped() { return this.flipped; }
/*     */ 
/*     */ 
/*     */   
/* 435 */   public void setFlipped(boolean flipped) { this.flipped = flipped; }
/*     */ 
/*     */   
/*     */   public Vec3 getRedstoneDirection(BlockPos pos) {
/* 439 */     BlockState state = level().getBlockState(pos);
/* 440 */     if (!state.is(Blocks.POWERED_RAIL) || !((Boolean)state.getValue(PoweredRailBlock.POWERED)).booleanValue()) {
/* 441 */       return Vec3.ZERO;
/*     */     }
/*     */     
/* 444 */     RailShape shape = (RailShape)state.getValue(((BaseRailBlock)state.getBlock()).getShapeProperty());
/*     */     
/* 446 */     if (shape == RailShape.EAST_WEST) {
/* 447 */       if (isRedstoneConductor(pos.west()))
/* 448 */         return new Vec3(1.0D, 0.0D, 0.0D); 
/* 449 */       if (isRedstoneConductor(pos.east())) {
/* 450 */         return new Vec3(-1.0D, 0.0D, 0.0D);
/*     */       }
/* 452 */     } else if (shape == RailShape.NORTH_SOUTH) {
/* 453 */       if (isRedstoneConductor(pos.north()))
/* 454 */         return new Vec3(0.0D, 0.0D, 1.0D); 
/* 455 */       if (isRedstoneConductor(pos.south())) {
/* 456 */         return new Vec3(0.0D, 0.0D, -1.0D);
/*     */       }
/*     */     } 
/*     */     
/* 460 */     return Vec3.ZERO;
/*     */   }
/*     */ 
/*     */   
/* 464 */   public boolean isRedstoneConductor(BlockPos pos) { return level().getBlockState(pos).isRedstoneConductor(level(), pos); }
/*     */ 
/*     */   
/*     */   protected Vec3 applyNaturalSlowdown(Vec3 movement) {
/* 468 */     double slowdownFactor = this.behavior.getSlowdownFactor();
/* 469 */     Vec3 newMovement = movement.multiply(slowdownFactor, 0.0D, slowdownFactor);
/*     */     
/* 471 */     if (isInWater()) {
/* 472 */       newMovement = newMovement.scale(0.949999988079071D);
/*     */     }
/* 474 */     return newMovement;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 479 */     setCustomDisplayBlockState(input.read("DisplayState", BlockState.CODEC));
/* 480 */     setDisplayOffset(input.getIntOr("DisplayOffset", getDefaultDisplayOffset()));
/*     */     
/* 482 */     this.flipped = input.getBooleanOr("FlippedRotation", false);
/* 483 */     this.firstTick = input.getBooleanOr("HasTicked", false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 488 */     getCustomDisplayBlockState().ifPresent(blockState -> output.store("DisplayState", BlockState.CODEC, blockState));
/* 489 */     int displayOffset = getDisplayOffset();
/* 490 */     if (displayOffset != getDefaultDisplayOffset()) {
/* 491 */       output.putInt("DisplayOffset", displayOffset);
/*     */     }
/* 493 */     output.putBoolean("FlippedRotation", this.flipped);
/* 494 */     output.putBoolean("HasTicked", this.firstTick);
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(Entity entity) {
/* 499 */     if (level().isClientSide()) {
/*     */       return;
/*     */     }
/* 502 */     if (entity.noPhysics || this.noPhysics) {
/*     */       return;
/*     */     }
/*     */     
/* 506 */     if (hasPassenger(entity)) {
/*     */       return;
/*     */     }
/*     */     
/* 510 */     double xa = entity.getX() - getX();
/* 511 */     double za = entity.getZ() - getZ();
/*     */     
/* 513 */     double dd = xa * xa + za * za;
/* 514 */     if (dd >= 9.999999747378752E-5D) {
/* 515 */       dd = Math.sqrt(dd);
/* 516 */       xa /= dd;
/* 517 */       za /= dd;
/* 518 */       double pow = 1.0D / dd;
/* 519 */       if (pow > 1.0D) {
/* 520 */         pow = 1.0D;
/*     */       }
/* 522 */       xa *= pow;
/* 523 */       za *= pow;
/* 524 */       xa *= 0.10000000149011612D;
/* 525 */       za *= 0.10000000149011612D;
/*     */       
/* 527 */       xa *= 0.5D;
/* 528 */       za *= 0.5D;
/*     */       
/* 530 */       if (entity instanceof AbstractMinecart) { AbstractMinecart otherMinecart = (AbstractMinecart)entity;
/* 531 */         pushOtherMinecart(otherMinecart, xa, za); }
/*     */       else
/* 533 */       { push(-xa, 0.0D, -za);
/* 534 */         entity.push(xa / 4.0D, 0.0D, za / 4.0D); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void pushOtherMinecart(AbstractMinecart otherMinecart, double xa, double za) {
/*     */     double zo, xo;
/* 542 */     if (useExperimentalMovement(level())) {
/* 543 */       xo = (getDeltaMovement()).x;
/* 544 */       zo = (getDeltaMovement()).z;
/*     */     } else {
/* 546 */       xo = otherMinecart.getX() - getX();
/* 547 */       zo = otherMinecart.getZ() - getZ();
/*     */     } 
/*     */     
/* 550 */     Vec3 dir = (new Vec3(xo, 0.0D, zo)).normalize();
/* 551 */     Vec3 facing = (new Vec3(Mth.cos((getYRot() * 0.017453292F)), 0.0D, Mth.sin((getYRot() * 0.017453292F)))).normalize();
/*     */     
/* 553 */     double dot = Math.abs(dir.dot(facing));
/*     */     
/* 555 */     if (dot < 0.800000011920929D && !useExperimentalMovement(level())) {
/*     */       return;
/*     */     }
/*     */     
/* 559 */     Vec3 movement = getDeltaMovement();
/* 560 */     Vec3 entityMovement = otherMinecart.getDeltaMovement();
/*     */     
/* 562 */     if (otherMinecart.isFurnace() && !isFurnace()) {
/* 563 */       setDeltaMovement(movement.multiply(0.2D, 1.0D, 0.2D));
/* 564 */       push(entityMovement.x - xa, 0.0D, entityMovement.z - za);
/* 565 */       otherMinecart.setDeltaMovement(entityMovement.multiply(0.95D, 1.0D, 0.95D));
/* 566 */     } else if (!otherMinecart.isFurnace() && isFurnace()) {
/* 567 */       otherMinecart.setDeltaMovement(entityMovement.multiply(0.2D, 1.0D, 0.2D));
/* 568 */       otherMinecart.push(movement.x + xa, 0.0D, movement.z + za);
/* 569 */       setDeltaMovement(movement.multiply(0.95D, 1.0D, 0.95D));
/*     */     } else {
/* 571 */       double xdd = (entityMovement.x + movement.x) / 2.0D;
/* 572 */       double zdd = (entityMovement.z + movement.z) / 2.0D;
/*     */       
/* 574 */       setDeltaMovement(movement.multiply(0.2D, 1.0D, 0.2D));
/* 575 */       push(xdd - xa, 0.0D, zdd - za);
/* 576 */       otherMinecart.setDeltaMovement(entityMovement.multiply(0.2D, 1.0D, 0.2D));
/* 577 */       otherMinecart.push(xdd + xa, 0.0D, zdd + za);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 582 */   public BlockState getDisplayBlockState() { return (BlockState)getCustomDisplayBlockState().orElseGet(this::getDefaultDisplayBlockState); }
/*     */ 
/*     */ 
/*     */   
/* 586 */   private Optional<BlockState> getCustomDisplayBlockState() { return (Optional)getEntityData().get(DATA_ID_CUSTOM_DISPLAY_BLOCK); }
/*     */ 
/*     */ 
/*     */   
/* 590 */   public BlockState getDefaultDisplayBlockState() { return Blocks.AIR.defaultBlockState(); }
/*     */ 
/*     */ 
/*     */   
/* 594 */   public int getDisplayOffset() { return ((Integer)getEntityData().get(DATA_ID_DISPLAY_OFFSET)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 598 */   public int getDefaultDisplayOffset() { return 6; }
/*     */ 
/*     */ 
/*     */   
/* 602 */   public void setCustomDisplayBlockState(Optional<BlockState> state) { getEntityData().set(DATA_ID_CUSTOM_DISPLAY_BLOCK, state); }
/*     */ 
/*     */ 
/*     */   
/* 606 */   public void setDisplayOffset(int offset) { getEntityData().set(DATA_ID_DISPLAY_OFFSET, Integer.valueOf(offset)); }
/*     */ 
/*     */ 
/*     */   
/* 610 */   public static boolean useExperimentalMovement(Level level) { return level.enabledFeatures().contains(FeatureFlags.MINECART_IMPROVEMENTS); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 617 */   public boolean isRideable() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 624 */   public boolean isFurnace() { return false; }
/*     */   
/*     */   public abstract ItemStack getPickResult();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\minecart\AbstractMinecart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */