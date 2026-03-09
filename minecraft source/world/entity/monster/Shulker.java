/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.InterpolationHandler;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.golem.AbstractGolem;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.ShulkerBullet;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ 
/*     */ public class Shulker
/*     */   extends AbstractGolem
/*     */   implements Enemy
/*     */ {
/*  67 */   private static final Identifier COVERED_ARMOR_MODIFIER_ID = Identifier.withDefaultNamespace("covered");
/*  68 */   private static final AttributeModifier COVERED_ARMOR_MODIFIER = new AttributeModifier(COVERED_ARMOR_MODIFIER_ID, 20.0D, AttributeModifier.Operation.ADD_VALUE);
/*     */   
/*  70 */   protected static final EntityDataAccessor<Direction> DATA_ATTACH_FACE_ID = SynchedEntityData.defineId(Shulker.class, EntityDataSerializers.DIRECTION);
/*  71 */   protected static final EntityDataAccessor<Byte> DATA_PEEK_ID = SynchedEntityData.defineId(Shulker.class, EntityDataSerializers.BYTE);
/*  72 */   protected static final EntityDataAccessor<Byte> DATA_COLOR_ID = SynchedEntityData.defineId(Shulker.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   private static final int TELEPORT_STEPS = 6;
/*     */   
/*     */   private static final byte NO_COLOR = 16;
/*     */   private static final byte DEFAULT_COLOR = 16;
/*     */   private static final int MAX_TELEPORT_DISTANCE = 8;
/*     */   private static final int OTHER_SHULKER_SCAN_RADIUS = 8;
/*     */   private static final int OTHER_SHULKER_LIMIT = 5;
/*     */   private static final float PEEK_PER_TICK = 0.05F;
/*     */   private static final byte DEFAULT_PEEK = 0;
/*  83 */   private static final Direction DEFAULT_ATTACH_FACE = Direction.DOWN;
/*     */   
/*  85 */   private static final Vector3f FORWARD = (Vector3f)Util.make(() -> {
/*     */         
/*  87 */         forwardNormal = Direction.SOUTH.getUnitVec3i();
/*  88 */         return new Vector3f(forwardNormal.getX(), forwardNormal.getY(), forwardNormal.getZ());
/*     */       });
/*     */   
/*     */   private static final float MAX_SCALE = 3.0F;
/*     */   
/*     */   private float currentPeekAmountO;
/*     */   
/*     */   private float currentPeekAmount;
/*     */   
/*     */   private BlockPos clientOldAttachPosition;
/*     */   
/*     */   private int clientSideTeleportInterpolation;
/*     */   private static final float MAX_LID_OPEN = 1.0F;
/*     */   
/*     */   public Shulker(EntityType<? extends Shulker> type, Level level) {
/* 103 */     super(type, level);
/*     */     
/* 105 */     this.xpReward = 5;
/*     */     
/* 107 */     this.lookControl = new ShulkerLookControl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 112 */     this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F, 0.02F, true));
/* 113 */     this.goalSelector.addGoal(4, new ShulkerAttackGoal());
/* 114 */     this.goalSelector.addGoal(7, new ShulkerPeekGoal());
/* 115 */     this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
/*     */     
/* 117 */     this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[] { getClass() })).setAlertOthers(new Class[0]));
/* 118 */     this.targetSelector.addGoal(2, new ShulkerNearestAttackGoal(this));
/* 119 */     this.targetSelector.addGoal(3, new ShulkerDefenseAttackGoal(this));
/*     */   }
/*     */   
/*     */   private class ShulkerLookControl
/*     */     extends LookControl {
/* 124 */     public ShulkerLookControl(Mob mob) { super(mob); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void clampHeadRotationToBody() {}
/*     */ 
/*     */ 
/*     */     
/*     */     protected Optional<Float> getYRotD() {
/* 134 */       Direction attachFace = Shulker.this.getAttachFace().getOpposite();
/*     */ 
/*     */       
/* 137 */       Vector3f forward = attachFace.getRotation().transform(new Vector3f(Shulker.FORWARD));
/*     */       
/* 139 */       Vec3i upNormal = attachFace.getUnitVec3i();
/* 140 */       Vector3f right = new Vector3f(upNormal.getX(), upNormal.getY(), upNormal.getZ());
/* 141 */       right.cross(forward);
/*     */       
/* 143 */       double xd = this.wantedX - this.mob.getX();
/* 144 */       double yd = this.wantedY - this.mob.getEyeY();
/* 145 */       double zd = this.wantedZ - this.mob.getZ();
/*     */ 
/*     */       
/* 148 */       Vector3f out = new Vector3f((float)xd, (float)yd, (float)zd);
/* 149 */       float deltaRight = right.dot(out);
/* 150 */       float deltaForward = forward.dot(out);
/*     */       
/* 152 */       return (Math.abs(deltaRight) > 1.0E-5F || Math.abs(deltaForward) > 1.0E-5F) ? Optional.of(Float.valueOf((float)(Mth.atan2(-deltaRight, deltaForward) * 57.2957763671875D))) : Optional.empty();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 157 */     protected Optional<Float> getXRotD() { return Optional.of(Float.valueOf(0.0F)); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 163 */   protected Entity.MovementEmission getMovementEmission() { return Entity.MovementEmission.NONE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 168 */   public SoundSource getSoundSource() { return SoundSource.HOSTILE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 173 */   protected SoundEvent getAmbientSound() { return SoundEvents.SHULKER_AMBIENT; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void playAmbientSound() {
/* 178 */     if (!isClosed()) {
/* 179 */       super.playAmbientSound();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 185 */   protected SoundEvent getDeathSound() { return SoundEvents.SHULKER_DEATH; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 190 */     if (isClosed()) {
/* 191 */       return SoundEvents.SHULKER_HURT_CLOSED;
/*     */     }
/* 193 */     return SoundEvents.SHULKER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 198 */     super.defineSynchedData(entityData);
/*     */     
/* 200 */     entityData.define(DATA_ATTACH_FACE_ID, DEFAULT_ATTACH_FACE);
/* 201 */     entityData.define(DATA_PEEK_ID, Byte.valueOf((byte)0));
/* 202 */     entityData.define(DATA_COLOR_ID, Byte.valueOf((byte)16));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 206 */     return Mob.createMobAttributes()
/* 207 */       .add(Attributes.MAX_HEALTH, 30.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 212 */   protected BodyRotationControl createBodyControl() { return new ShulkerBodyRotationControl(this); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 217 */     super.readAdditionalSaveData(input);
/*     */     
/* 219 */     setAttachFace((Direction)input.read("AttachFace", Direction.LEGACY_ID_CODEC).orElse(DEFAULT_ATTACH_FACE));
/* 220 */     this.entityData.set(DATA_PEEK_ID, Byte.valueOf(input.getByteOr("Peek", (byte)0)));
/* 221 */     this.entityData.set(DATA_COLOR_ID, Byte.valueOf(input.getByteOr("Color", (byte)16)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 226 */     super.addAdditionalSaveData(output);
/*     */     
/* 228 */     output.store("AttachFace", Direction.LEGACY_ID_CODEC, getAttachFace());
/* 229 */     output.putByte("Peek", ((Byte)this.entityData.get(DATA_PEEK_ID)).byteValue());
/* 230 */     output.putByte("Color", ((Byte)this.entityData.get(DATA_COLOR_ID)).byteValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 235 */     super.tick();
/*     */     
/* 237 */     if (!level().isClientSide() && !isPassenger() && !canStayAt(blockPosition(), getAttachFace())) {
/* 238 */       findNewAttachment();
/*     */     }
/*     */     
/* 241 */     if (updatePeekAmount()) {
/* 242 */       onPeekAmountChange();
/*     */     }
/*     */     
/* 245 */     if (level().isClientSide()) {
/* 246 */       if (this.clientSideTeleportInterpolation > 0) {
/* 247 */         this.clientSideTeleportInterpolation--;
/*     */       } else {
/* 249 */         this.clientOldAttachPosition = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void findNewAttachment() {
/* 256 */     Direction attachmentDirection = findAttachableSurface(blockPosition());
/* 257 */     if (attachmentDirection != null) {
/* 258 */       setAttachFace(attachmentDirection);
/*     */     } else {
/*     */       
/* 261 */       teleportSomewhere();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected AABB makeBoundingBox(Vec3 position) {
/* 267 */     float physPeek = getPhysicalPeek(this.currentPeekAmount);
/* 268 */     Direction direction = getAttachFace().getOpposite();
/*     */     
/* 270 */     return getProgressAabb(getScale(), direction, physPeek, position);
/*     */   }
/*     */ 
/*     */   
/* 274 */   private static float getPhysicalPeek(float amount) { return 0.5F - Mth.sin(((0.5F + amount) * 3.1415927F)) * 0.5F; }
/*     */ 
/*     */   
/*     */   private boolean updatePeekAmount() {
/* 278 */     this.currentPeekAmountO = this.currentPeekAmount;
/* 279 */     float targetPeekAmount = getRawPeekAmount() * 0.01F;
/* 280 */     if (this.currentPeekAmount == targetPeekAmount) {
/* 281 */       return false;
/*     */     }
/*     */     
/* 284 */     if (this.currentPeekAmount > targetPeekAmount) {
/* 285 */       this.currentPeekAmount = Mth.clamp(this.currentPeekAmount - 0.05F, targetPeekAmount, 1.0F);
/*     */     } else {
/* 287 */       this.currentPeekAmount = Mth.clamp(this.currentPeekAmount + 0.05F, 0.0F, targetPeekAmount);
/*     */     } 
/* 289 */     return true;
/*     */   }
/*     */   
/*     */   private void onPeekAmountChange() {
/* 293 */     reapplyPosition();
/*     */     
/* 295 */     float physicalPeek = getPhysicalPeek(this.currentPeekAmount);
/* 296 */     float physicalPeekOld = getPhysicalPeek(this.currentPeekAmountO);
/* 297 */     Direction direction = getAttachFace().getOpposite();
/*     */     
/* 299 */     float push = (physicalPeek - physicalPeekOld) * getScale();
/* 300 */     if (push <= 0.0F) {
/*     */       return;
/*     */     }
/* 303 */     List<Entity> entities = level().getEntities(this, getProgressDeltaAabb(getScale(), direction, physicalPeekOld, physicalPeek, position()), EntitySelector.NO_SPECTATORS.and(e -> !e.isPassengerOfSameVehicle(this)));
/* 304 */     for (Entity entity : entities) {
/* 305 */       if (!(entity instanceof Shulker) && !entity.noPhysics) {
/* 306 */         entity.move(MoverType.SHULKER, new Vec3((push * direction
/* 307 */               .getStepX()), (push * direction
/* 308 */               .getStepY()), (push * direction
/* 309 */               .getStepZ())));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 316 */   public static AABB getProgressAabb(float size, Direction direction, float progressTo, Vec3 position) { return getProgressDeltaAabb(size, direction, -1.0F, progressTo, position); }
/*     */ 
/*     */   
/*     */   public static AABB getProgressDeltaAabb(float size, Direction direction, float progressFrom, float progressTo, Vec3 position) {
/* 320 */     AABB boundsAtBottomCenter = new AABB(-size * 0.5D, 0.0D, -size * 0.5D, size * 0.5D, size, size * 0.5D);
/* 321 */     double maxMovement = Math.max(progressFrom, progressTo);
/* 322 */     double minMovement = Math.min(progressFrom, progressTo);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 327 */     AABB aabb = boundsAtBottomCenter.expandTowards(direction.getStepX() * maxMovement * size, direction.getStepY() * maxMovement * size, direction.getStepZ() * maxMovement * size).contract(
/* 328 */         -direction.getStepX() * (1.0D + minMovement) * size, 
/* 329 */         -direction.getStepY() * (1.0D + minMovement) * size, 
/* 330 */         -direction.getStepZ() * (1.0D + minMovement) * size);
/*     */     
/* 332 */     return aabb.move(position.x, position.y, position.z);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean startRiding(Entity entity, boolean force, boolean sendEventAndTriggers) {
/* 337 */     if (level().isClientSide()) {
/* 338 */       this.clientOldAttachPosition = null;
/* 339 */       this.clientSideTeleportInterpolation = 0;
/*     */     } 
/* 341 */     setAttachFace(Direction.DOWN);
/* 342 */     return super.startRiding(entity, force, sendEventAndTriggers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopRiding() {
/* 347 */     super.stopRiding();
/* 348 */     if (level().isClientSide()) {
/* 349 */       this.clientOldAttachPosition = blockPosition();
/*     */     }
/* 351 */     this.yBodyRotO = 0.0F;
/* 352 */     this.yBodyRot = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 357 */     setYRot(0.0F);
/* 358 */     this.yHeadRot = getYRot();
/* 359 */     setOldPosAndRot();
/*     */     
/* 361 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(MoverType moverType, Vec3 delta) {
/* 366 */     if (moverType == MoverType.SHULKER_BOX) {
/* 367 */       teleportSomewhere();
/*     */     } else {
/* 369 */       super.move(moverType, delta);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 375 */   public Vec3 getDeltaMovement() { return Vec3.ZERO; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeltaMovement(Vec3 deltaMovement) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPos(double x, double y, double z) {
/* 385 */     BlockPos oldPos = blockPosition();
/* 386 */     if (isPassenger()) {
/* 387 */       super.setPos(x, y, z);
/*     */     } else {
/* 389 */       super.setPos(Mth.floor(x) + 0.5D, Mth.floor(y + 0.5D), Mth.floor(z) + 0.5D);
/*     */     } 
/* 391 */     if (this.tickCount == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 395 */     BlockPos pos = blockPosition();
/* 396 */     if (!pos.equals(oldPos)) {
/* 397 */       this.entityData.set(DATA_PEEK_ID, Byte.valueOf((byte)0));
/* 398 */       this.needsSync = true;
/* 399 */       if (level().isClientSide() && !isPassenger() && !pos.equals(this.clientOldAttachPosition)) {
/* 400 */         this.clientOldAttachPosition = oldPos;
/* 401 */         this.clientSideTeleportInterpolation = 6;
/*     */ 
/*     */         
/* 404 */         this.xOld = getX();
/* 405 */         this.yOld = getY();
/* 406 */         this.zOld = getZ();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Direction findAttachableSurface(BlockPos target) {
/* 412 */     for (Direction direction : Direction.values()) {
/* 413 */       if (canStayAt(target, direction)) {
/* 414 */         return direction;
/*     */       }
/*     */     } 
/* 417 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canStayAt(BlockPos target, Direction face) {
/* 422 */     if (isPositionBlocked(target)) {
/* 423 */       return false;
/*     */     }
/*     */     
/* 426 */     Direction oppositeFace = face.getOpposite();
/* 427 */     if (!level().loadedAndEntityCanStandOnFace(target.relative(face), this, oppositeFace)) {
/* 428 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 432 */     AABB fullyOpened = getProgressAabb(getScale(), oppositeFace, 1.0F, target.getBottomCenter()).deflate(1.0E-6D);
/* 433 */     return level().noCollision(this, fullyOpened);
/*     */   }
/*     */   
/*     */   private boolean isPositionBlocked(BlockPos target) {
/* 437 */     BlockState state = level().getBlockState(target);
/* 438 */     if (state.isAir()) {
/* 439 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 443 */     boolean movingPistonInOurCurrentPosition = (state.is(Blocks.MOVING_PISTON) && target.equals(blockPosition()));
/* 444 */     return !movingPistonInOurCurrentPosition;
/*     */   }
/*     */   
/*     */   protected boolean teleportSomewhere() {
/* 448 */     if (isNoAi() || !isAlive()) {
/* 449 */       return false;
/*     */     }
/* 451 */     BlockPos current = blockPosition();
/* 452 */     for (int attempt = 0; attempt < 5; attempt++) {
/* 453 */       BlockPos target = current.offset(
/* 454 */           Mth.randomBetweenInclusive(this.random, -8, 8), 
/* 455 */           Mth.randomBetweenInclusive(this.random, -8, 8), 
/* 456 */           Mth.randomBetweenInclusive(this.random, -8, 8));
/*     */       
/* 458 */       if (target.getY() > level().getMinY() && level().isEmptyBlock(target) && level().getWorldBorder().isWithinBounds(target) && level().noCollision(this, (new AABB(target)).deflate(1.0E-6D))) {
/* 459 */         Direction attachmentDirection = findAttachableSurface(target);
/* 460 */         if (attachmentDirection != null) {
/* 461 */           unRide();
/*     */ 
/*     */           
/* 464 */           setAttachFace(attachmentDirection);
/*     */           
/* 466 */           playSound(SoundEvents.SHULKER_TELEPORT, 1.0F, 1.0F);
/* 467 */           setPos(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D);
/* 468 */           level().gameEvent(GameEvent.TELEPORT, current, GameEvent.Context.of(this));
/* 469 */           this.entityData.set(DATA_PEEK_ID, Byte.valueOf((byte)0));
/* 470 */           setTarget(null);
/* 471 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 475 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 480 */   public InterpolationHandler getInterpolation() { return null; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 485 */     if (isClosed()) {
/* 486 */       Entity directEntity = source.getDirectEntity();
/* 487 */       if (directEntity instanceof net.minecraft.world.entity.projectile.arrow.AbstractArrow) {
/* 488 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 492 */     if (super.hurtServer(level, source, damage)) {
/* 493 */       if (getHealth() < getMaxHealth() * 0.5D && this.random.nextInt(4) == 0) {
/* 494 */         teleportSomewhere();
/* 495 */       } else if (source.is(DamageTypeTags.IS_PROJECTILE)) {
/* 496 */         Entity directEntity = source.getDirectEntity();
/* 497 */         if (directEntity != null && directEntity.getType() == EntityType.SHULKER_BULLET) {
/* 498 */           hitByShulkerBullet();
/*     */         }
/*     */       } 
/*     */       
/* 502 */       return true;
/*     */     } 
/* 504 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 508 */   private boolean isClosed() { return (getRawPeekAmount() == 0); }
/*     */ 
/*     */   
/*     */   private void hitByShulkerBullet() {
/* 512 */     Vec3 oldPosition = position();
/* 513 */     AABB oldAabb = getBoundingBox();
/*     */     
/* 515 */     if (isClosed() || !teleportSomewhere()) {
/*     */       return;
/*     */     }
/*     */     
/* 519 */     int shulkerCount = level().getEntities(EntityType.SHULKER, oldAabb.inflate(8.0D), Entity::isAlive).size();
/*     */     
/* 521 */     float failureChance = (shulkerCount - 1) / 5.0F;
/* 522 */     if ((level()).random.nextFloat() < failureChance) {
/*     */       return;
/*     */     }
/*     */     
/* 526 */     Shulker baby = (Shulker)EntityType.SHULKER.create(level(), EntitySpawnReason.BREEDING);
/*     */     
/* 528 */     if (baby != null) {
/* 529 */       baby.setVariant(getVariant());
/* 530 */       baby.snapTo(oldPosition);
/* 531 */       level().addFreshEntity(baby);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 537 */   public boolean canBeCollidedWith(Entity other) { return isAlive(); }
/*     */ 
/*     */ 
/*     */   
/* 541 */   public Direction getAttachFace() { return (Direction)this.entityData.get(DATA_ATTACH_FACE_ID); }
/*     */ 
/*     */ 
/*     */   
/* 545 */   private void setAttachFace(Direction attachmentDirection) { this.entityData.set(DATA_ATTACH_FACE_ID, attachmentDirection); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 550 */     if (DATA_ATTACH_FACE_ID.equals(accessor)) {
/* 551 */       setBoundingBox(makeBoundingBox());
/*     */     }
/* 553 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */   
/* 557 */   private int getRawPeekAmount() { return ((Byte)this.entityData.get(DATA_PEEK_ID)).byteValue(); }
/*     */ 
/*     */   
/*     */   private void setRawPeekAmount(int amount) {
/* 561 */     if (!level().isClientSide()) {
/* 562 */       getAttribute(Attributes.ARMOR).removeModifier(COVERED_ARMOR_MODIFIER_ID);
/* 563 */       if (amount == 0) {
/* 564 */         getAttribute(Attributes.ARMOR).addPermanentModifier(COVERED_ARMOR_MODIFIER);
/* 565 */         playSound(SoundEvents.SHULKER_CLOSE, 1.0F, 1.0F);
/* 566 */         gameEvent(GameEvent.CONTAINER_CLOSE);
/*     */       } else {
/* 568 */         playSound(SoundEvents.SHULKER_OPEN, 1.0F, 1.0F);
/* 569 */         gameEvent(GameEvent.CONTAINER_OPEN);
/*     */       } 
/*     */     } 
/*     */     
/* 573 */     this.entityData.set(DATA_PEEK_ID, Byte.valueOf((byte)amount));
/*     */   }
/*     */ 
/*     */   
/* 577 */   public float getClientPeekAmount(float a) { return Mth.lerp(a, this.currentPeekAmountO, this.currentPeekAmount); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 582 */     super.recreateFromPacket(packet);
/* 583 */     this.yBodyRot = 0.0F;
/* 584 */     this.yBodyRotO = 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 589 */   public int getMaxHeadXRot() { return 180; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 594 */   public int getMaxHeadYRot() { return 180; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void push(Entity entity) {}
/*     */ 
/*     */   
/*     */   public Vec3 getRenderPosition(float a) {
/* 602 */     if (this.clientOldAttachPosition == null || this.clientSideTeleportInterpolation <= 0) {
/* 603 */       return null;
/*     */     }
/*     */     
/* 606 */     double scale = (this.clientSideTeleportInterpolation - a) / 6.0D;
/* 607 */     scale *= scale;
/* 608 */     scale *= getScale();
/*     */     
/* 610 */     BlockPos currentPos = blockPosition();
/* 611 */     double ox = (currentPos.getX() - this.clientOldAttachPosition.getX()) * scale;
/* 612 */     double oy = (currentPos.getY() - this.clientOldAttachPosition.getY()) * scale;
/* 613 */     double oz = (currentPos.getZ() - this.clientOldAttachPosition.getZ()) * scale;
/*     */     
/* 615 */     return new Vec3(-ox, -oy, -oz);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 620 */   protected float sanitizeScale(float scale) { return Math.min(scale, 3.0F); }
/*     */   
/*     */   private static class ShulkerBodyRotationControl
/*     */     extends BodyRotationControl
/*     */   {
/* 625 */     public ShulkerBodyRotationControl(Mob mob) { super(mob); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void clientTick() {}
/*     */   }
/*     */ 
/*     */   
/*     */   private class ShulkerPeekGoal
/*     */     extends Goal
/*     */   {
/*     */     private int peekTime;
/*     */ 
/*     */     
/* 639 */     public boolean canUse() { return (Shulker.this.getTarget() == null && Shulker.this.random.nextInt(reducedTickDelay(40)) == 0 && Shulker.this.canStayAt(Shulker.this.blockPosition(), Shulker.this.getAttachFace())); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 644 */     public boolean canContinueToUse() { return (Shulker.this.getTarget() == null && this.peekTime > 0); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() {
/* 649 */       this.peekTime = adjustedTickDelay(20 * (1 + Shulker.this.random.nextInt(3)));
/* 650 */       Shulker.this.setRawPeekAmount(30);
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 655 */       if (Shulker.this.getTarget() == null) {
/* 656 */         Shulker.this.setRawPeekAmount(0);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 662 */     public void tick() { this.peekTime--; }
/*     */   }
/*     */   
/*     */   private class ShulkerAttackGoal
/*     */     extends Goal
/*     */   {
/*     */     private int attackTime;
/*     */     
/* 670 */     public ShulkerAttackGoal() { setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK)); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 675 */       LivingEntity target = Shulker.this.getTarget();
/* 676 */       if (target == null || !target.isAlive()) {
/* 677 */         return false;
/*     */       }
/* 679 */       if (Shulker.this.level().getDifficulty() == Difficulty.PEACEFUL) {
/* 680 */         return false;
/*     */       }
/*     */       
/* 683 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 688 */       this.attackTime = 20;
/* 689 */       Shulker.this.setRawPeekAmount(100);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 694 */     public void stop() { Shulker.this.setRawPeekAmount(0); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 699 */     public boolean requiresUpdateEveryTick() { return true; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 704 */       if (Shulker.this.level().getDifficulty() == Difficulty.PEACEFUL) {
/*     */         return;
/*     */       }
/* 707 */       this.attackTime--;
/*     */       
/* 709 */       LivingEntity target = Shulker.this.getTarget();
/* 710 */       if (target == null) {
/*     */         return;
/*     */       }
/* 713 */       Shulker.this.getLookControl().setLookAt(target, 180.0F, 180.0F);
/*     */       
/* 715 */       double distance = Shulker.this.distanceToSqr(target);
/*     */       
/* 717 */       if (distance < 400.0D) {
/* 718 */         if (this.attackTime <= 0) {
/* 719 */           this.attackTime = 20 + Shulker.this.random.nextInt(10) * 20 / 2;
/*     */           
/* 721 */           Shulker.this.level().addFreshEntity(new ShulkerBullet(Shulker.this.level(), Shulker.this, target, Shulker.this.getAttachFace().getAxis()));
/* 722 */           Shulker.this.playSound(SoundEvents.SHULKER_SHOOT, 2.0F, (Shulker.this.random.nextFloat() - Shulker.this.random.nextFloat()) * 0.2F + 1.0F);
/*     */         } 
/*     */       } else {
/* 725 */         Shulker.this.setTarget(null);
/*     */       } 
/*     */       
/* 728 */       super.tick();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ShulkerNearestAttackGoal
/*     */     extends NearestAttackableTargetGoal<Player> {
/* 734 */     public ShulkerNearestAttackGoal(Shulker mob) { super(mob, Player.class, true); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 739 */       if (Shulker.this.level().getDifficulty() == Difficulty.PEACEFUL) {
/* 740 */         return false;
/*     */       }
/* 742 */       return super.canUse();
/*     */     }
/*     */ 
/*     */     
/*     */     protected AABB getTargetSearchArea(double followDistance) {
/* 747 */       Direction attachFace = ((Shulker)this.mob).getAttachFace();
/* 748 */       if (attachFace.getAxis() == Direction.Axis.X) {
/* 749 */         return this.mob.getBoundingBox().inflate(4.0D, followDistance, followDistance);
/*     */       }
/* 751 */       if (attachFace.getAxis() == Direction.Axis.Z) {
/* 752 */         return this.mob.getBoundingBox().inflate(followDistance, followDistance, 4.0D);
/*     */       }
/* 754 */       return this.mob.getBoundingBox().inflate(followDistance, 4.0D, followDistance);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ShulkerDefenseAttackGoal
/*     */     extends NearestAttackableTargetGoal<LivingEntity> {
/* 760 */     public ShulkerDefenseAttackGoal(Shulker mob) { super(mob, LivingEntity.class, 10, true, false, (input, level) -> input instanceof Enemy); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 765 */       if (this.mob.getTeam() == null) {
/* 766 */         return false;
/*     */       }
/* 768 */       return super.canUse();
/*     */     }
/*     */ 
/*     */     
/*     */     protected AABB getTargetSearchArea(double followDistance) {
/* 773 */       Direction attachFace = ((Shulker)this.mob).getAttachFace();
/* 774 */       if (attachFace.getAxis() == Direction.Axis.X) {
/* 775 */         return this.mob.getBoundingBox().inflate(4.0D, followDistance, followDistance);
/*     */       }
/* 777 */       if (attachFace.getAxis() == Direction.Axis.Z) {
/* 778 */         return this.mob.getBoundingBox().inflate(followDistance, followDistance, 4.0D);
/*     */       }
/* 780 */       return this.mob.getBoundingBox().inflate(followDistance, 4.0D, followDistance);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 785 */   private void setVariant(Optional<DyeColor> color) { this.entityData.set(DATA_COLOR_ID, (Byte)color.map(dyeColor -> Byte.valueOf((byte)dyeColor.getId())).orElse(Byte.valueOf((byte)16))); }
/*     */ 
/*     */ 
/*     */   
/* 789 */   public Optional<DyeColor> getVariant() { return Optional.ofNullable(getColor()); }
/*     */ 
/*     */   
/*     */   public DyeColor getColor() {
/* 793 */     byte color = ((Byte)this.entityData.get(DATA_COLOR_ID)).byteValue();
/* 794 */     if (color == 16 || color > 15) {
/* 795 */       return null;
/*     */     }
/* 797 */     return DyeColor.byId(color);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 802 */     if (type == DataComponents.SHULKER_COLOR) {
/* 803 */       return (T)castComponentValue(type, getColor());
/*     */     }
/*     */     
/* 806 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 811 */     applyImplicitComponentIfPresent(components, DataComponents.SHULKER_COLOR);
/* 812 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 817 */     if (type == DataComponents.SHULKER_COLOR) {
/* 818 */       setVariant(Optional.of((DyeColor)castComponentValue(DataComponents.SHULKER_COLOR, value)));
/* 819 */       return true;
/*     */     } 
/*     */     
/* 822 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Shulker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */