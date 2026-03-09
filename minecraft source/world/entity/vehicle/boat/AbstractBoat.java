/*     */ package net.minecraft.world.entity.vehicle.boat;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.BlockUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.InterpolationHandler;
/*     */ import net.minecraft.world.entity.Leashable;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.DismountHelper;
/*     */ import net.minecraft.world.entity.vehicle.VehicleEntity;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ public abstract class AbstractBoat
/*     */   extends VehicleEntity
/*     */   implements Leashable
/*     */ {
/*  54 */   private static final EntityDataAccessor<Boolean> DATA_ID_PADDLE_LEFT = SynchedEntityData.defineId(AbstractBoat.class, EntityDataSerializers.BOOLEAN);
/*  55 */   private static final EntityDataAccessor<Boolean> DATA_ID_PADDLE_RIGHT = SynchedEntityData.defineId(AbstractBoat.class, EntityDataSerializers.BOOLEAN);
/*  56 */   private static final EntityDataAccessor<Integer> DATA_ID_BUBBLE_TIME = SynchedEntityData.defineId(AbstractBoat.class, EntityDataSerializers.INT);
/*     */   
/*     */   public static final int PADDLE_LEFT = 0;
/*     */   public static final int PADDLE_RIGHT = 1;
/*     */   private static final int TIME_TO_EJECT = 60;
/*     */   private static final float PADDLE_SPEED = 0.3926991F;
/*     */   public static final double PADDLE_SOUND_TIME = 0.7853981852531433D;
/*     */   public static final int BUBBLE_TIME = 60;
/*  64 */   private final float[] paddlePositions = new float[2];
/*     */   
/*     */   private float outOfControlTicks;
/*     */   private float deltaRotation;
/*  68 */   private final InterpolationHandler interpolation = new InterpolationHandler(this, 3);
/*     */   
/*     */   private boolean inputLeft;
/*     */   
/*     */   private boolean inputRight;
/*     */   
/*     */   private boolean inputUp;
/*     */   private boolean inputDown;
/*     */   private double waterLevel;
/*     */   private float landFriction;
/*     */   private Status status;
/*     */   private Status oldStatus;
/*     */   private double lastYd;
/*     */   private boolean isAboveBubbleColumn;
/*     */   private boolean bubbleColumnDirectionIsDown;
/*     */   private float bubbleMultiplier;
/*     */   private float bubbleAngle;
/*     */   private float bubbleAngleO;
/*     */   private Leashable.LeashData leashData;
/*     */   private final Supplier<Item> dropItem;
/*     */   
/*     */   public AbstractBoat(EntityType<? extends AbstractBoat> type, Level level, Supplier<Item> dropItem) {
/*  90 */     super(type, level);
/*  91 */     this.dropItem = dropItem;
/*  92 */     this.blocksBuilding = true;
/*     */   }
/*     */   
/*     */   public void setInitialPos(double x, double y, double z) {
/*  96 */     setPos(x, y, z);
/*     */     
/*  98 */     this.xo = x;
/*  99 */     this.yo = y;
/* 100 */     this.zo = z;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 105 */   protected Entity.MovementEmission getMovementEmission() { return Entity.MovementEmission.EVENTS; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 110 */     super.defineSynchedData(entityData);
/* 111 */     entityData.define(DATA_ID_PADDLE_LEFT, Boolean.valueOf(false));
/* 112 */     entityData.define(DATA_ID_PADDLE_RIGHT, Boolean.valueOf(false));
/* 113 */     entityData.define(DATA_ID_BUBBLE_TIME, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 118 */   public boolean canCollideWith(Entity entity) { return canVehicleCollide(this, entity); }
/*     */ 
/*     */ 
/*     */   
/* 122 */   public static boolean canVehicleCollide(Entity vehicle, Entity entity) { return ((entity.canBeCollidedWith(vehicle) || entity.isPushable()) && !vehicle.isPassengerOfSameVehicle(entity)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public boolean canBeCollidedWith(Entity other) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   public boolean isPushable() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   public Vec3 getRelativePortalPosition(Direction.Axis axis, BlockUtil.FoundRectangle portalArea) { return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(axis, portalArea)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
/* 144 */     float offset = getSinglePassengerXOffset();
/* 145 */     if (getPassengers().size() > 1) {
/* 146 */       int index = getPassengers().indexOf(passenger);
/* 147 */       if (index == 0) {
/* 148 */         offset = 0.2F;
/*     */       } else {
/* 150 */         offset = -0.6F;
/*     */       } 
/*     */       
/* 153 */       if (passenger instanceof Animal) {
/* 154 */         offset += 0.2F;
/*     */       }
/*     */     } 
/*     */     
/* 158 */     return (new Vec3(0.0D, rideHeight(dimensions), offset)).yRot(-getYRot() * 0.017453292F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAboveBubbleColumn(boolean dragDown, BlockPos pos) {
/* 163 */     if (level() instanceof net.minecraft.server.level.ServerLevel) {
/* 164 */       this.isAboveBubbleColumn = true;
/* 165 */       this.bubbleColumnDirectionIsDown = dragDown;
/* 166 */       if (getBubbleTime() == 0) {
/* 167 */         setBubbleTime(60);
/*     */       }
/*     */     } 
/*     */     
/* 171 */     if (!isUnderWater() && this.random.nextInt(100) == 0) {
/* 172 */       level().playLocalSound(getX(), getY(), getZ(), getSwimSplashSound(), getSoundSource(), 1.0F, 0.8F + 0.4F * this.random.nextFloat(), false);
/* 173 */       level().addParticle(ParticleTypes.SPLASH, getX() + this.random.nextFloat(), getY() + 0.7D, getZ() + this.random.nextFloat(), 0.0D, 0.0D, 0.0D);
/* 174 */       gameEvent(GameEvent.SPLASH, getControllingPassenger());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(Entity entity) {
/* 180 */     if (entity instanceof AbstractBoat) {
/* 181 */       if ((entity.getBoundingBox()).minY < (getBoundingBox()).maxY) {
/* 182 */         super.push(entity);
/*     */       }
/* 184 */     } else if ((entity.getBoundingBox()).minY <= (getBoundingBox()).minY) {
/* 185 */       super.push(entity);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateHurt(float yaw) {
/* 191 */     setHurtDir(-getHurtDir());
/* 192 */     setHurtTime(10);
/* 193 */     setDamage(getDamage() * 11.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 198 */   public boolean isPickable() { return !isRemoved(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   public InterpolationHandler getInterpolation() { return this.interpolation; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 208 */   public Direction getMotionDirection() { return getDirection().getClockWise(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 213 */     this.oldStatus = this.status;
/* 214 */     this.status = getStatus();
/*     */     
/* 216 */     if (this.status == Status.UNDER_WATER || this.status == Status.UNDER_FLOWING_WATER) {
/* 217 */       this.outOfControlTicks++;
/*     */     } else {
/* 219 */       this.outOfControlTicks = 0.0F;
/*     */     } 
/*     */     
/* 222 */     if (!level().isClientSide() && this.outOfControlTicks >= 60.0F) {
/* 223 */       ejectPassengers();
/*     */     }
/*     */     
/* 226 */     if (getHurtTime() > 0) {
/* 227 */       setHurtTime(getHurtTime() - 1);
/*     */     }
/* 229 */     if (getDamage() > 0.0F) {
/* 230 */       setDamage(getDamage() - 1.0F);
/*     */     }
/*     */     
/* 233 */     super.tick();
/* 234 */     this.interpolation.interpolate();
/*     */     
/* 236 */     if (isLocalInstanceAuthoritative()) {
/* 237 */       if (!(getFirstPassenger() instanceof Player)) {
/* 238 */         setPaddleState(false, false);
/*     */       }
/*     */       
/* 241 */       floatBoat();
/* 242 */       if (level().isClientSide()) {
/* 243 */         controlBoat();
/* 244 */         level().sendPacketToServer(new ServerboundPaddleBoatPacket(getPaddleState(0), getPaddleState(1)));
/*     */       } 
/* 246 */       move(MoverType.SELF, getDeltaMovement());
/*     */     } else {
/* 248 */       setDeltaMovement(Vec3.ZERO);
/*     */     } 
/*     */     
/* 251 */     applyEffectsFromBlocks();
/*     */ 
/*     */     
/* 254 */     applyEffectsFromBlocks();
/*     */     
/* 256 */     tickBubbleColumn();
/*     */     
/* 258 */     for (int i = 0; i <= 1; i++) {
/* 259 */       if (getPaddleState(i)) {
/* 260 */         if (!isSilent() && (this.paddlePositions[i] % 6.2831855F) <= 0.7853981852531433D && ((this.paddlePositions[i] + 0.3926991F) % 6.2831855F) >= 0.7853981852531433D) {
/* 261 */           SoundEvent sound = getPaddleSound();
/* 262 */           if (sound != null) {
/* 263 */             Vec3 viewVector = getViewVector(1.0F);
/* 264 */             double dx = (i == 1) ? -viewVector.z : viewVector.z;
/* 265 */             double dz = (i == 1) ? viewVector.x : -viewVector.x;
/*     */             
/* 267 */             level().playSound(null, getX() + dx, getY(), getZ() + dz, sound, getSoundSource(), 1.0F, 0.8F + 0.4F * this.random.nextFloat());
/*     */           } 
/*     */         } 
/* 270 */         this.paddlePositions[i] = this.paddlePositions[i] + 0.3926991F;
/*     */       } else {
/* 272 */         this.paddlePositions[i] = 0.0F;
/*     */       } 
/*     */     } 
/*     */     
/* 276 */     List<Entity> entities = level().getEntities(this, getBoundingBox().inflate(0.20000000298023224D, -0.009999999776482582D, 0.20000000298023224D), EntitySelector.pushableBy(this));
/*     */     
/* 278 */     if (!entities.isEmpty()) {
/* 279 */       boolean addNewPassengers = (!level().isClientSide() && !(getControllingPassenger() instanceof Player));
/* 280 */       for (Entity entity : entities) {
/* 281 */         if (entity.hasPassenger(this)) {
/*     */           continue;
/*     */         }
/*     */         
/* 285 */         if (addNewPassengers && 
/* 286 */           getPassengers().size() < getMaxPassengers() && 
/* 287 */           !entity.isPassenger() && 
/* 288 */           hasEnoughSpaceFor(entity) && entity instanceof LivingEntity && 
/*     */           
/* 290 */           !entity.getType().is(EntityTypeTags.CANNOT_BE_PUSHED_ONTO_BOATS)) {
/*     */           
/* 292 */           entity.startRiding(this); continue;
/*     */         } 
/* 294 */         push(entity);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void tickBubbleColumn() {
/* 301 */     if (level().isClientSide()) {
/* 302 */       int clientBubbleTime = getBubbleTime();
/* 303 */       if (clientBubbleTime > 0) {
/* 304 */         this.bubbleMultiplier += 0.05F;
/*     */       } else {
/* 306 */         this.bubbleMultiplier -= 0.1F;
/*     */       } 
/* 308 */       this.bubbleMultiplier = Mth.clamp(this.bubbleMultiplier, 0.0F, 1.0F);
/*     */       
/* 310 */       this.bubbleAngleO = this.bubbleAngle;
/* 311 */       this.bubbleAngle = 10.0F * (float)Math.sin(0.5D * this.tickCount) * this.bubbleMultiplier;
/*     */     } else {
/* 313 */       if (!this.isAboveBubbleColumn) {
/* 314 */         setBubbleTime(0);
/*     */       }
/*     */       
/* 317 */       int bubbleTime = getBubbleTime();
/* 318 */       if (bubbleTime > 0) {
/* 319 */         bubbleTime--;
/* 320 */         setBubbleTime(bubbleTime);
/*     */         
/* 322 */         int diff = 60 - bubbleTime - 1;
/* 323 */         if (diff > 0 && 
/* 324 */           bubbleTime == 0) {
/* 325 */           setBubbleTime(0);
/* 326 */           Vec3 movement = getDeltaMovement();
/* 327 */           if (this.bubbleColumnDirectionIsDown) {
/* 328 */             setDeltaMovement(movement.add(0.0D, -0.7D, 0.0D));
/* 329 */             ejectPassengers();
/*     */           } else {
/* 331 */             setDeltaMovement(movement.x, hasPassenger(e -> e instanceof Player) ? 2.7D : 0.6D, movement.z);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 336 */         this.isAboveBubbleColumn = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected SoundEvent getPaddleSound() {
/* 342 */     switch (getStatus().ordinal()) { case 0: case 1: case 2: case 3:  }  return 
/*     */ 
/*     */       
/* 345 */       null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPaddleState(boolean left, boolean right) {
/* 350 */     this.entityData.set(DATA_ID_PADDLE_LEFT, Boolean.valueOf(left));
/* 351 */     this.entityData.set(DATA_ID_PADDLE_RIGHT, Boolean.valueOf(right));
/*     */   }
/*     */   
/*     */   public float getRowingTime(int side, float a) {
/* 355 */     if (getPaddleState(side)) {
/* 356 */       return Mth.clampedLerp(a, this.paddlePositions[side] - 0.3926991F, this.paddlePositions[side]);
/*     */     }
/* 358 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 365 */   public Leashable.LeashData getLeashData() { return this.leashData; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 370 */   public void setLeashData(Leashable.LeashData leashData) { this.leashData = leashData; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 375 */   public Vec3 getLeashOffset() { return new Vec3(0.0D, (0.88F * getBbHeight()), (0.64F * getBbWidth())); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 380 */   public boolean supportQuadLeash() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 385 */   public Vec3[] getQuadLeashOffsets() { return Leashable.createQuadLeashOffsets(this, 0.0D, 0.64D, 0.382D, 0.88D); }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Status
/*     */   {
/* 391 */     IN_WATER,
/* 392 */     UNDER_WATER,
/* 393 */     UNDER_FLOWING_WATER,
/* 394 */     ON_LAND,
/* 395 */     IN_AIR;
/*     */   }
/*     */   
/*     */   private Status getStatus() {
/* 399 */     Status waterStatus = isUnderwater();
/* 400 */     if (waterStatus != null) {
/* 401 */       this.waterLevel = (getBoundingBox()).maxY;
/* 402 */       return waterStatus;
/*     */     } 
/*     */     
/* 405 */     if (checkInWater()) {
/* 406 */       return Status.IN_WATER;
/*     */     }
/*     */     
/* 409 */     float friction = getGroundFriction();
/* 410 */     if (friction > 0.0F) {
/* 411 */       this.landFriction = friction;
/* 412 */       return Status.ON_LAND;
/*     */     } 
/*     */     
/* 415 */     return Status.IN_AIR;
/*     */   }
/*     */   
/*     */   public float getWaterLevelAbove() {
/* 419 */     AABB aabb = getBoundingBox();
/* 420 */     int minX = Mth.floor(aabb.minX);
/* 421 */     int maxX = Mth.ceil(aabb.maxX);
/* 422 */     int minY = Mth.floor(aabb.maxY);
/* 423 */     int maxY = Mth.ceil(aabb.maxY - this.lastYd);
/* 424 */     int minZ = Mth.floor(aabb.minZ);
/* 425 */     int maxZ = Mth.ceil(aabb.maxZ);
/*     */     
/* 427 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/*     */     int y;
/* 429 */     label24: for (y = minY; y < maxY; y++) {
/* 430 */       float blockHeight = 0.0F;
/* 431 */       for (int x = minX; x < maxX; x++) {
/* 432 */         for (int z = minZ; z < maxZ; z++) {
/* 433 */           pos.set(x, y, z);
/* 434 */           FluidState fluidState = level().getFluidState(pos);
/* 435 */           if (fluidState.is(FluidTags.WATER)) {
/* 436 */             blockHeight = Math.max(blockHeight, fluidState.getHeight(level(), pos));
/*     */           }
/* 438 */           if (blockHeight >= 1.0F) {
/*     */             continue label24;
/*     */           }
/*     */         } 
/*     */       } 
/* 443 */       if (blockHeight < 1.0F) {
/* 444 */         return pos.getY() + blockHeight;
/*     */       }
/*     */     } 
/* 447 */     return (maxY + 1);
/*     */   }
/*     */   
/*     */   public float getGroundFriction() {
/* 451 */     AABB bb = getBoundingBox();
/* 452 */     AABB box = new AABB(bb.minX, bb.minY - 0.001D, bb.minZ, bb.maxX, bb.minY, bb.maxZ);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 462 */     int x0 = Mth.floor(box.minX) - 1;
/* 463 */     int x1 = Mth.ceil(box.maxX) + 1;
/* 464 */     int y0 = Mth.floor(box.minY) - 1;
/* 465 */     int y1 = Mth.ceil(box.maxY) + 1;
/* 466 */     int z0 = Mth.floor(box.minZ) - 1;
/* 467 */     int z1 = Mth.ceil(box.maxZ) + 1;
/*     */     
/* 469 */     VoxelShape boatShape = Shapes.create(box);
/* 470 */     float friction = 0.0F;
/* 471 */     int count = 0;
/*     */     
/* 473 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 474 */     for (int x = x0; x < x1; x++) {
/* 475 */       for (int z = z0; z < z1; z++) {
/*     */         
/* 477 */         int edges = ((x == x0 || x == x1 - 1) ? 1 : 0) + ((z == z0 || z == z1 - 1) ? 1 : 0);
/* 478 */         if (edges != 2)
/*     */         {
/*     */ 
/*     */           
/* 482 */           for (int y = y0; y < y1; y++) {
/*     */             
/* 484 */             if (edges <= 0 || (y != y0 && y != y1 - 1)) {
/*     */ 
/*     */ 
/*     */               
/* 488 */               blockPos.set(x, y, z);
/*     */               
/* 490 */               BlockState blockState = level().getBlockState(blockPos);
/* 491 */               if (!(blockState.getBlock() instanceof net.minecraft.world.level.block.WaterlilyBlock))
/*     */               {
/*     */                 
/* 494 */                 if (Shapes.joinIsNotEmpty(blockState.getCollisionShape(level(), blockPos).move(blockPos), boatShape, BooleanOp.AND)) {
/* 495 */                   friction += blockState.getBlock().getFriction();
/* 496 */                   count++;
/*     */                 }  } 
/*     */             } 
/*     */           }  } 
/*     */       } 
/*     */     } 
/* 502 */     return friction / count;
/*     */   }
/*     */   
/*     */   private boolean checkInWater() {
/* 506 */     AABB bb = getBoundingBox();
/* 507 */     int minX = Mth.floor(bb.minX);
/* 508 */     int maxX = Mth.ceil(bb.maxX);
/* 509 */     int minY = Mth.floor(bb.minY);
/* 510 */     int maxY = Mth.ceil(bb.minY + 0.001D);
/* 511 */     int minZ = Mth.floor(bb.minZ);
/* 512 */     int maxZ = Mth.ceil(bb.maxZ);
/*     */     
/* 514 */     boolean inWater = false;
/* 515 */     this.waterLevel = -1.7976931348623157E308D;
/*     */     
/* 517 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 518 */     for (int x = minX; x < maxX; x++) {
/* 519 */       for (int y = minY; y < maxY; y++) {
/* 520 */         for (int z = minZ; z < maxZ; z++) {
/* 521 */           pos.set(x, y, z);
/* 522 */           FluidState fluidState = level().getFluidState(pos);
/*     */           
/* 524 */           if (fluidState.is(FluidTags.WATER)) {
/*     */ 
/*     */ 
/*     */             
/* 528 */             float height = y + fluidState.getHeight(level(), pos);
/* 529 */             this.waterLevel = Math.max(height, this.waterLevel);
/* 530 */             inWater |= ((bb.minY < height));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 535 */     return inWater;
/*     */   }
/*     */   
/*     */   private Status isUnderwater() {
/* 539 */     AABB aabb = getBoundingBox();
/* 540 */     double maxY = aabb.maxY + 0.001D;
/*     */     
/* 542 */     int x0 = Mth.floor(aabb.minX);
/* 543 */     int x1 = Mth.ceil(aabb.maxX);
/* 544 */     int y0 = Mth.floor(aabb.maxY);
/* 545 */     int y1 = Mth.ceil(maxY);
/* 546 */     int z0 = Mth.floor(aabb.minZ);
/* 547 */     int z1 = Mth.ceil(aabb.maxZ);
/*     */     
/* 549 */     boolean underWater = false;
/* 550 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 551 */     for (int x = x0; x < x1; x++) {
/* 552 */       for (int y = y0; y < y1; y++) {
/* 553 */         for (int z = z0; z < z1; z++) {
/* 554 */           pos.set(x, y, z);
/* 555 */           FluidState fluidState = level().getFluidState(pos);
/* 556 */           if (fluidState.is(FluidTags.WATER) && 
/* 557 */             maxY < (pos.getY() + fluidState.getHeight(level(), pos))) {
/* 558 */             if (fluidState.isSource()) {
/* 559 */               underWater = true;
/*     */             } else {
/* 561 */               return Status.UNDER_FLOWING_WATER;
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 569 */     return underWater ? Status.UNDER_WATER : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 574 */   protected double getDefaultGravity() { return 0.04D; }
/*     */ 
/*     */   
/*     */   private void floatBoat() {
/* 578 */     double vspeed = -getGravity();
/* 579 */     double buoyancy = 0.0D;
/* 580 */     float invFriction = 0.05F;
/*     */     
/* 582 */     if (this.oldStatus == Status.IN_AIR && this.status != Status.IN_AIR && this.status != Status.ON_LAND) {
/* 583 */       this.waterLevel = getY(1.0D);
/* 584 */       double targetY = (getWaterLevelAbove() - getBbHeight()) + 0.101D;
/* 585 */       if (level().noCollision(this, getBoundingBox().move(0.0D, targetY - getY(), 0.0D))) {
/* 586 */         setPos(getX(), targetY, getZ());
/* 587 */         setDeltaMovement(getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
/* 588 */         this.lastYd = 0.0D;
/*     */       } 
/*     */       
/* 591 */       this.status = Status.IN_WATER;
/*     */     } else {
/* 593 */       if (this.status == Status.IN_WATER) {
/* 594 */         buoyancy = (this.waterLevel - getY()) / getBbHeight();
/* 595 */         invFriction = 0.9F;
/* 596 */       } else if (this.status == Status.UNDER_FLOWING_WATER) {
/* 597 */         vspeed = -7.0E-4D;
/* 598 */         invFriction = 0.9F;
/* 599 */       } else if (this.status == Status.UNDER_WATER) {
/* 600 */         buoyancy = 0.009999999776482582D;
/* 601 */         invFriction = 0.45F;
/* 602 */       } else if (this.status == Status.IN_AIR) {
/* 603 */         invFriction = 0.9F;
/* 604 */       } else if (this.status == Status.ON_LAND) {
/* 605 */         invFriction = this.landFriction;
/* 606 */         if (getControllingPassenger() instanceof Player) {
/* 607 */           this.landFriction /= 2.0F;
/*     */         }
/*     */       } 
/*     */       
/* 611 */       Vec3 movement = getDeltaMovement();
/* 612 */       setDeltaMovement(movement.x * invFriction, movement.y + vspeed, movement.z * invFriction);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 617 */       this.deltaRotation *= invFriction;
/*     */       
/* 619 */       if (buoyancy > 0.0D) {
/* 620 */         Vec3 deltaMovement = getDeltaMovement();
/* 621 */         setDeltaMovement(deltaMovement.x, (deltaMovement.y + buoyancy * 
/*     */             
/* 623 */             getDefaultGravity() / 0.65D) * 0.75D, deltaMovement.z);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void controlBoat() {
/* 631 */     if (!isVehicle()) {
/*     */       return;
/*     */     }
/*     */     
/* 635 */     float acceleration = 0.0F;
/* 636 */     if (this.inputLeft) {
/* 637 */       this.deltaRotation--;
/*     */     }
/* 639 */     if (this.inputRight) {
/* 640 */       this.deltaRotation++;
/*     */     }
/* 642 */     if (this.inputRight != this.inputLeft && !this.inputUp && !this.inputDown) {
/* 643 */       acceleration += 0.005F;
/*     */     }
/* 645 */     setYRot(getYRot() + this.deltaRotation);
/*     */     
/* 647 */     if (this.inputUp) {
/* 648 */       acceleration += 0.04F;
/*     */     }
/* 650 */     if (this.inputDown) {
/* 651 */       acceleration -= 0.005F;
/*     */     }
/*     */     
/* 654 */     setDeltaMovement(getDeltaMovement().add((
/* 655 */           Mth.sin((-getYRot() * 0.017453292F)) * acceleration), 0.0D, (
/*     */           
/* 657 */           Mth.cos((getYRot() * 0.017453292F)) * acceleration)));
/*     */ 
/*     */     
/* 660 */     setPaddleState(((this.inputRight && !this.inputLeft) || this.inputUp), ((this.inputLeft && !this.inputRight) || this.inputUp));
/*     */   }
/*     */ 
/*     */   
/* 664 */   protected float getSinglePassengerXOffset() { return 0.0F; }
/*     */ 
/*     */ 
/*     */   
/* 668 */   public boolean hasEnoughSpaceFor(Entity entity) { return (entity.getBbWidth() < getBbWidth()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
/* 673 */     super.positionRider(passenger, moveFunction);
/*     */     
/* 675 */     if (passenger.getType().is(EntityTypeTags.CAN_TURN_IN_BOATS)) {
/*     */       return;
/*     */     }
/*     */     
/* 679 */     passenger.setYRot(passenger.getYRot() + this.deltaRotation);
/* 680 */     passenger.setYHeadRot(passenger.getYHeadRot() + this.deltaRotation);
/*     */     
/* 682 */     clampRotation(passenger);
/*     */     
/* 684 */     if (passenger instanceof Animal && getPassengers().size() == getMaxPassengers()) {
/* 685 */       int rotationOffset = (passenger.getId() % 2 == 0) ? 90 : 270;
/* 686 */       passenger.setYBodyRot(((Animal)passenger).yBodyRot + rotationOffset);
/* 687 */       passenger.setYHeadRot(passenger.getYHeadRot() + rotationOffset);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
/* 693 */     Vec3 direction = getCollisionHorizontalEscapeVector((getBbWidth() * Mth.SQRT_OF_TWO), passenger.getBbWidth(), passenger.getYRot());
/*     */     
/* 695 */     double targetX = getX() + direction.x;
/* 696 */     double targetZ = getZ() + direction.z;
/*     */     
/* 698 */     BlockPos targetBlockPos = BlockPos.containing(targetX, (getBoundingBox()).maxY, targetZ);
/* 699 */     BlockPos belowBlockPos = targetBlockPos.below();
/*     */     
/* 701 */     if (!level().isWaterAt(belowBlockPos)) {
/* 702 */       List<Vec3> targets = Lists.newArrayList();
/*     */       
/* 704 */       double targetFloor = level().getBlockFloorHeight(targetBlockPos);
/* 705 */       if (DismountHelper.isBlockFloorValid(targetFloor)) {
/* 706 */         targets.add(new Vec3(targetX, targetBlockPos.getY() + targetFloor, targetZ));
/*     */       }
/*     */       
/* 709 */       double belowFloor = level().getBlockFloorHeight(belowBlockPos);
/* 710 */       if (DismountHelper.isBlockFloorValid(belowFloor)) {
/* 711 */         targets.add(new Vec3(targetX, belowBlockPos.getY() + belowFloor, targetZ));
/*     */       }
/*     */       
/* 714 */       for (UnmodifiableIterator unmodifiableIterator = passenger.getDismountPoses().iterator(); unmodifiableIterator.hasNext(); ) { Pose dismountPose = (Pose)unmodifiableIterator.next();
/* 715 */         for (Vec3 target : targets) {
/* 716 */           if (DismountHelper.canDismountTo(level(), target, passenger, dismountPose)) {
/* 717 */             passenger.setPose(dismountPose);
/* 718 */             return target;
/*     */           } 
/*     */         }  }
/*     */     
/*     */     } 
/*     */     
/* 724 */     return super.getDismountLocationForPassenger(passenger);
/*     */   }
/*     */   
/*     */   protected void clampRotation(Entity passenger) {
/* 728 */     passenger.setYBodyRot(getYRot());
/*     */     
/* 730 */     float delta = Mth.wrapDegrees(passenger.getYRot() - getYRot());
/* 731 */     float targetDelta = Mth.clamp(delta, -105.0F, 105.0F);
/* 732 */     passenger.yRotO += targetDelta - delta;
/* 733 */     passenger.setYRot(passenger.getYRot() + targetDelta - delta);
/* 734 */     passenger.setYHeadRot(passenger.getYRot());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 739 */   public void onPassengerTurned(Entity passenger) { clampRotation(passenger); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 744 */   protected void addAdditionalSaveData(ValueOutput output) { writeLeashData(output, this.leashData); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 749 */   protected void readAdditionalSaveData(ValueInput input) { readLeashData(input); }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand) {
/* 754 */     InteractionResult superInteraction = super.interact(player, hand);
/* 755 */     if (superInteraction != InteractionResult.PASS) {
/* 756 */       return superInteraction;
/*     */     }
/* 758 */     if (!player.isSecondaryUseActive() && this.outOfControlTicks < 60.0F && (level().isClientSide() || player.startRiding(this))) {
/* 759 */       return InteractionResult.SUCCESS;
/*     */     }
/* 761 */     return InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Entity.RemovalReason reason) {
/* 766 */     if (!level().isClientSide() && reason.shouldDestroy() && isLeashed())
/*     */     {
/* 768 */       dropLeash();
/*     */     }
/* 770 */     super.remove(reason);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
/* 775 */     this.lastYd = (getDeltaMovement()).y;
/* 776 */     if (isPassenger()) {
/*     */       return;
/*     */     }
/*     */     
/* 780 */     if (onGround) {
/* 781 */       resetFallDistance();
/* 782 */     } else if (!level().getFluidState(blockPosition().below()).is(FluidTags.WATER) && 
/* 783 */       ya < 0.0D) {
/* 784 */       this.fallDistance -= (float)ya;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 790 */   public boolean getPaddleState(int side) { return (((Boolean)this.entityData.get((side == 0) ? DATA_ID_PADDLE_LEFT : DATA_ID_PADDLE_RIGHT)).booleanValue() && getControllingPassenger() != null); }
/*     */ 
/*     */ 
/*     */   
/* 794 */   private void setBubbleTime(int val) { this.entityData.set(DATA_ID_BUBBLE_TIME, Integer.valueOf(val)); }
/*     */ 
/*     */ 
/*     */   
/* 798 */   private int getBubbleTime() { return ((Integer)this.entityData.get(DATA_ID_BUBBLE_TIME)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 802 */   public float getBubbleAngle(float a) { return Mth.lerp(a, this.bubbleAngleO, this.bubbleAngle); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 807 */   protected boolean canAddPassenger(Entity passenger) { return (getPassengers().size() < getMaxPassengers() && !isEyeInFluid(FluidTags.WATER)); }
/*     */ 
/*     */ 
/*     */   
/* 811 */   protected int getMaxPassengers() { return 2; }
/*     */ 
/*     */ 
/*     */   
/*     */   public LivingEntity getControllingPassenger() {
/* 816 */     Entity entity = getFirstPassenger(); LivingEntity passenger = (LivingEntity)entity; return (entity instanceof LivingEntity) ? passenger : super.getControllingPassenger();
/*     */   }
/*     */   
/*     */   public void setInput(boolean left, boolean right, boolean up, boolean down) {
/* 820 */     this.inputLeft = left;
/* 821 */     this.inputRight = right;
/* 822 */     this.inputUp = up;
/* 823 */     this.inputDown = down;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 828 */   public boolean isUnderWater() { return (this.status == Status.UNDER_WATER || this.status == Status.UNDER_FLOWING_WATER); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 833 */   protected final Item getDropItem() { return (Item)this.dropItem.get(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 838 */   public final ItemStack getPickResult() { return new ItemStack((ItemLike)this.dropItem.get()); }
/*     */   
/*     */   protected abstract double rideHeight(EntityDimensions paramEntityDimensions);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\boat\AbstractBoat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */