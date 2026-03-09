/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class Ghast
/*     */   extends Mob
/*     */   implements Enemy
/*     */ {
/*  47 */   private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING = SynchedEntityData.defineId(Ghast.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final byte DEFAULT_EXPLOSION_POWER = 1;
/*     */   
/*  51 */   private int explosionPower = 1;
/*     */   
/*     */   public Ghast(EntityType<? extends Ghast> type, Level level) {
/*  54 */     super(type, level);
/*     */     
/*  56 */     this.xpReward = 5;
/*     */     
/*  58 */     this.moveControl = new GhastMoveControl(this, false, () -> false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  63 */     this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this));
/*     */     
/*  65 */     this.goalSelector.addGoal(7, new GhastLookGoal(this));
/*  66 */     this.goalSelector.addGoal(7, new GhastShootFireballGoal(this));
/*     */ 
/*     */     
/*  69 */     this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.player.Player.class, 10, true, false, (target, level) -> (Math.abs(target.getY() - getY()) <= 4.0D)));
/*     */   }
/*     */ 
/*     */   
/*  73 */   public boolean isCharging() { return ((Boolean)this.entityData.get(DATA_IS_CHARGING)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public void setCharging(boolean onOff) { this.entityData.set(DATA_IS_CHARGING, Boolean.valueOf(onOff)); }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public int getExplosionPower() { return this.explosionPower; }
/*     */ 
/*     */ 
/*     */   
/*  85 */   private static boolean isReflectedFireball(DamageSource source) { return (source.getDirectEntity() instanceof LargeFireball && source.getEntity() instanceof net.minecraft.world.entity.player.Player); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInvulnerableTo(ServerLevel level, DamageSource source) {
/*  90 */     return ((isInvulnerable() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) || (
/*  91 */       !isReflectedFireball(source) && super.isInvulnerableTo(level, source)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   public boolean onClimbable() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public void travel(Vec3 input) { travelFlying(input, 0.02F); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 112 */     if (isReflectedFireball(source)) {
/*     */       
/* 114 */       super.hurtServer(level, source, 1000.0F);
/* 115 */       return true;
/*     */     } 
/*     */     
/* 118 */     if (isInvulnerableTo(level, source)) {
/* 119 */       return false;
/*     */     }
/*     */     
/* 122 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 127 */     super.defineSynchedData(entityData);
/*     */     
/* 129 */     entityData.define(DATA_IS_CHARGING, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 133 */     return Mob.createMobAttributes()
/* 134 */       .add(Attributes.MAX_HEALTH, 10.0D)
/* 135 */       .add(Attributes.FOLLOW_RANGE, 100.0D)
/* 136 */       .add(Attributes.CAMERA_DISTANCE, 8.0D)
/* 137 */       .add(Attributes.FLYING_SPEED, 0.06D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 142 */   public SoundSource getSoundSource() { return SoundSource.HOSTILE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   protected SoundEvent getAmbientSound() { return SoundEvents.GHAST_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.GHAST_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   protected SoundEvent getDeathSound() { return SoundEvents.GHAST_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   protected float getSoundVolume() { return 5.0F; }
/*     */ 
/*     */   
/*     */   public static boolean checkGhastSpawnRules(EntityType<Ghast> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 166 */     return (level.getDifficulty() != Difficulty.PEACEFUL && random
/* 167 */       .nextInt(20) == 0 && 
/* 168 */       checkMobSpawnRules(type, level, spawnReason, pos, random));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 173 */   public int getMaxSpawnClusterSize() { return 1; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 178 */     super.addAdditionalSaveData(output);
/* 179 */     output.putByte("ExplosionPower", (byte)this.explosionPower);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 184 */     super.readAdditionalSaveData(input);
/* 185 */     this.explosionPower = input.getByteOr("ExplosionPower", (byte)1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 190 */   public boolean supportQuadLeashAsHolder() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 195 */   public double leashElasticDistance() { return 10.0D; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 200 */   public double leashSnapDistance() { return 16.0D; }
/*     */   
/*     */   public static class GhastMoveControl
/*     */     extends MoveControl {
/*     */     private final Mob ghast;
/*     */     private int floatDuration;
/*     */     private final boolean careful;
/*     */     private final BooleanSupplier shouldBeStopped;
/*     */     
/*     */     public GhastMoveControl(Mob ghast, boolean careful, BooleanSupplier shouldBeStopped) {
/* 210 */       super(ghast);
/* 211 */       this.ghast = ghast;
/* 212 */       this.careful = careful;
/* 213 */       this.shouldBeStopped = shouldBeStopped;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 218 */       if (this.shouldBeStopped.getAsBoolean()) {
/* 219 */         this.operation = MoveControl.Operation.WAIT;
/* 220 */         this.ghast.stopInPlace();
/*     */       } 
/*     */       
/* 223 */       if (this.operation != MoveControl.Operation.MOVE_TO) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 228 */       if (this.floatDuration-- <= 0) {
/* 229 */         this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 234 */         Vec3 travel = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
/*     */ 
/*     */         
/* 237 */         if (canReach(travel)) {
/* 238 */           this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(travel.normalize().scale(this.ghast.getAttributeValue(Attributes.FLYING_SPEED) * 5.0D / 3.0D)));
/*     */         } else {
/* 240 */           this.operation = MoveControl.Operation.WAIT;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean canReach(Vec3 travel) {
/* 246 */       AABB aabb = this.ghast.getBoundingBox();
/* 247 */       AABB aabbAtDestination = aabb.move(travel);
/* 248 */       if (this.careful) {
/* 249 */         for (BlockPos pos : BlockPos.betweenClosed(aabbAtDestination.inflate(1.0D))) {
/* 250 */           if (!blockTraversalPossible(this.ghast.level(), null, null, pos, false, false)) {
/* 251 */             return false;
/*     */           }
/*     */         } 
/*     */       }
/* 255 */       boolean isInWater = this.ghast.isInWater();
/* 256 */       boolean isInLava = this.ghast.isInLava();
/* 257 */       Vec3 start = this.ghast.position();
/* 258 */       Vec3 end = start.add(travel);
/*     */       
/* 260 */       return BlockGetter.forEachBlockIntersectedBetween(start, end, aabbAtDestination, (blockPos, i) -> {
/* 261 */             if (aabb.intersects(blockPos)) {
/* 262 */               return true;
/*     */             }
/* 264 */             return blockTraversalPossible(this.ghast.level(), start, end, blockPos, isInWater, isInLava);
/*     */           });
/*     */     }
/*     */     
/*     */     private boolean blockTraversalPossible(BlockGetter level, Vec3 start, Vec3 end, BlockPos pos, boolean canPathThroughWater, boolean canPathThroughLava) {
/* 269 */       BlockState state = level.getBlockState(pos);
/* 270 */       if (state.isAir()) {
/* 271 */         return true;
/*     */       }
/* 273 */       boolean preciseBlockCollisions = (start != null && end != null);
/* 274 */       boolean pathNoCollisions = preciseBlockCollisions ? (!this.ghast.collidedWithShapeMovingFrom(start, end, state.getCollisionShape(level, pos).move(new Vec3(pos)).toAabbs())) : state.getCollisionShape(level, pos).isEmpty();
/* 275 */       if (!this.careful) {
/* 276 */         return pathNoCollisions;
/*     */       }
/* 278 */       if (state.is(BlockTags.HAPPY_GHAST_AVOIDS)) {
/* 279 */         return false;
/*     */       }
/* 281 */       FluidState fluidState = level.getFluidState(pos);
/* 282 */       if (!fluidState.isEmpty() && (!preciseBlockCollisions || this.ghast.collidedWithFluid(fluidState, pos, start, end))) {
/* 283 */         if (fluidState.is(FluidTags.WATER)) {
/* 284 */           return canPathThroughWater;
/*     */         }
/* 286 */         if (fluidState.is(FluidTags.LAVA)) {
/* 287 */           return canPathThroughLava;
/*     */         }
/*     */       } 
/* 290 */       return pathNoCollisions;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RandomFloatAroundGoal
/*     */     extends Goal {
/*     */     private static final int MAX_ATTEMPTS = 64;
/*     */     private final Mob ghast;
/*     */     private final int distanceToBlocks;
/*     */     
/* 300 */     public RandomFloatAroundGoal(Mob ghast) { this(ghast, 0); }
/*     */ 
/*     */     
/*     */     public RandomFloatAroundGoal(Mob ghast, int distanceToBlocks) {
/* 304 */       this.ghast = ghast;
/* 305 */       this.distanceToBlocks = distanceToBlocks;
/* 306 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 311 */       MoveControl moveControl = this.ghast.getMoveControl();
/* 312 */       if (!moveControl.hasWanted()) {
/* 313 */         return true;
/*     */       }
/*     */       
/* 316 */       double xd = moveControl.getWantedX() - this.ghast.getX();
/* 317 */       double yd = moveControl.getWantedY() - this.ghast.getY();
/* 318 */       double zd = moveControl.getWantedZ() - this.ghast.getZ();
/*     */       
/* 320 */       double dd = xd * xd + yd * yd + zd * zd;
/*     */       
/* 322 */       return (dd < 1.0D || dd > 3600.0D);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 327 */     public boolean canContinueToUse() { return false; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() {
/* 332 */       Vec3 result = getSuitableFlyToPosition(this.ghast, this.distanceToBlocks);
/* 333 */       this.ghast.getMoveControl().setWantedPosition(result.x(), result.y(), result.z(), 1.0D);
/*     */     }
/*     */     
/*     */     public static Vec3 getSuitableFlyToPosition(Mob mob, int distanceToBlocks) {
/* 337 */       Level level = mob.level();
/* 338 */       RandomSource random = mob.getRandom();
/* 339 */       Vec3 center = mob.position();
/* 340 */       Vec3 result = null;
/* 341 */       for (int i = 0; i < 64; i++) {
/* 342 */         result = chooseRandomPositionWithRestriction(mob, center, random);
/* 343 */         if (result != null && isGoodTarget(level, result, distanceToBlocks)) {
/* 344 */           return result;
/*     */         }
/*     */       } 
/* 347 */       if (result == null) {
/* 348 */         result = chooseRandomPosition(center, random);
/*     */       }
/*     */ 
/*     */       
/* 352 */       BlockPos pos = BlockPos.containing(result);
/* 353 */       int heightY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos.getX(), pos.getZ());
/* 354 */       if (heightY < pos.getY() && heightY > level.getMinY()) {
/* 355 */         result = new Vec3(result.x(), mob.getY() - Math.abs(mob.getY() - result.y()), result.z());
/*     */       }
/* 357 */       return result;
/*     */     }
/*     */     
/*     */     private static boolean isGoodTarget(Level level, Vec3 target, int distanceToBlocks) {
/* 361 */       if (distanceToBlocks <= 0) {
/* 362 */         return true;
/*     */       }
/* 364 */       BlockPos pos = BlockPos.containing(target);
/* 365 */       if (!level.getBlockState(pos).isAir()) {
/* 366 */         return false;
/*     */       }
/*     */       
/* 369 */       for (Direction dir : Direction.values()) {
/* 370 */         for (int i = 1; i < distanceToBlocks; i++) {
/* 371 */           BlockPos offset = pos.relative(dir, i);
/* 372 */           if (!level.getBlockState(offset).isAir()) {
/* 373 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 377 */       return false;
/*     */     }
/*     */     
/*     */     private static Vec3 chooseRandomPosition(Vec3 center, RandomSource random) {
/* 381 */       double xTarget = center.x() + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 382 */       double yTarget = center.y() + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 383 */       double zTarget = center.z() + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 384 */       return new Vec3(xTarget, yTarget, zTarget);
/*     */     }
/*     */     
/*     */     private static Vec3 chooseRandomPositionWithRestriction(Mob mob, Vec3 center, RandomSource random) {
/* 388 */       Vec3 target = chooseRandomPosition(center, random);
/* 389 */       if (mob.hasHome() && !mob.isWithinHome(target)) {
/* 390 */         return null;
/*     */       }
/* 392 */       return target;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GhastLookGoal extends Goal {
/*     */     private final Mob ghast;
/*     */     
/*     */     public GhastLookGoal(Mob ghast) {
/* 400 */       this.ghast = ghast;
/*     */       
/* 402 */       setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 407 */     public boolean canUse() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 412 */     public boolean requiresUpdateEveryTick() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 417 */     public void tick() { Ghast.faceMovementDirection(this.ghast); }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void faceMovementDirection(Mob ghast) {
/* 422 */     if (ghast.getTarget() == null) {
/* 423 */       Vec3 movement = ghast.getDeltaMovement();
/* 424 */       ghast.setYRot(-((float)Mth.atan2(movement.x, movement.z)) * 57.295776F);
/* 425 */       ghast.yBodyRot = ghast.getYRot();
/*     */     } else {
/* 427 */       LivingEntity target = ghast.getTarget();
/*     */       
/* 429 */       double maxDist = 64.0D;
/* 430 */       if (target.distanceToSqr(ghast) < 4096.0D) {
/* 431 */         double xdd = target.getX() - ghast.getX();
/* 432 */         double zdd = target.getZ() - ghast.getZ();
/* 433 */         ghast.setYRot(-((float)Mth.atan2(xdd, zdd)) * 57.295776F);
/* 434 */         ghast.yBodyRot = ghast.getYRot();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class GhastShootFireballGoal
/*     */     extends Goal {
/*     */     private final Ghast ghast;
/*     */     public int chargeTime;
/*     */     
/* 444 */     public GhastShootFireballGoal(Ghast ghast) { this.ghast = ghast; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 449 */     public boolean canUse() { return (this.ghast.getTarget() != null); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 454 */     public void start() { this.chargeTime = 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 459 */     public void stop() { this.ghast.setCharging(false); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 464 */     public boolean requiresUpdateEveryTick() { return true; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 469 */       LivingEntity target = this.ghast.getTarget();
/* 470 */       if (target == null) {
/*     */         return;
/*     */       }
/*     */       
/* 474 */       double maxDist = 64.0D;
/* 475 */       if (target.distanceToSqr(this.ghast) < 4096.0D && this.ghast.hasLineOfSight(target)) {
/* 476 */         Level level = this.ghast.level();
/*     */         
/* 478 */         this.chargeTime++;
/* 479 */         if (this.chargeTime == 10 && !this.ghast.isSilent()) {
/* 480 */           level.levelEvent(null, 1015, this.ghast.blockPosition(), 0);
/*     */         }
/* 482 */         if (this.chargeTime == 20) {
/* 483 */           double d = 4.0D;
/* 484 */           Vec3 viewVector = this.ghast.getViewVector(1.0F);
/*     */           
/* 486 */           double xdd = target.getX() - this.ghast.getX() + viewVector.x * 4.0D;
/* 487 */           double ydd = target.getY(0.5D) - 0.5D + this.ghast.getY(0.5D);
/* 488 */           double zdd = target.getZ() - this.ghast.getZ() + viewVector.z * 4.0D;
/* 489 */           Vec3 direction = new Vec3(xdd, ydd, zdd);
/*     */           
/* 491 */           if (!this.ghast.isSilent()) {
/* 492 */             level.levelEvent(null, 1016, this.ghast.blockPosition(), 0);
/*     */           }
/* 494 */           LargeFireball entity = new LargeFireball(level, this.ghast, direction.normalize(), this.ghast.getExplosionPower());
/* 495 */           entity.setPos(this.ghast.getX() + viewVector.x * 4.0D, this.ghast.getY(0.5D) + 0.5D, entity.getZ() + viewVector.z * 4.0D);
/* 496 */           level.addFreshEntity(entity);
/* 497 */           this.chargeTime = -40;
/*     */         } 
/* 499 */       } else if (this.chargeTime > 0) {
/* 500 */         this.chargeTime--;
/*     */       } 
/* 502 */       this.ghast.setCharging((this.chargeTime > 10));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Ghast.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */