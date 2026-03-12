/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.damagesource.DamageTypes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Guardian
/*     */   extends Monster
/*     */ {
/*     */   protected static final int ATTACK_TIME = 80;
/*  53 */   private static final EntityDataAccessor<Boolean> DATA_ID_MOVING = SynchedEntityData.defineId(Guardian.class, EntityDataSerializers.BOOLEAN);
/*  54 */   private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(Guardian.class, EntityDataSerializers.INT);
/*     */   
/*     */   private float clientSideTailAnimation;
/*     */   private float clientSideTailAnimationO;
/*     */   private float clientSideTailAnimationSpeed;
/*     */   private float clientSideSpikesAnimation;
/*     */   private float clientSideSpikesAnimationO;
/*     */   private LivingEntity clientSideCachedAttackTarget;
/*     */   private int clientSideAttackTime;
/*     */   private boolean clientSideTouchedGround;
/*     */   protected RandomStrollGoal randomStrollGoal;
/*     */   
/*     */   public Guardian(EntityType<? extends Guardian> type, Level level) {
/*  67 */     super(type, level);
/*     */     
/*  69 */     this.xpReward = 10;
/*     */     
/*  71 */     setPathfindingMalus(PathType.WATER, 0.0F);
/*  72 */     this.moveControl = new GuardianMoveControl(this);
/*     */     
/*  74 */     this.clientSideTailAnimation = this.random.nextFloat();
/*  75 */     this.clientSideTailAnimationO = this.clientSideTailAnimation;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  80 */     MoveTowardsRestrictionGoal goal = new MoveTowardsRestrictionGoal(this, 1.0D);
/*  81 */     this.randomStrollGoal = new RandomStrollGoal(this, 1.0D, 80);
/*     */     
/*  83 */     this.goalSelector.addGoal(4, new GuardianAttackGoal(this));
/*  84 */     this.goalSelector.addGoal(5, goal);
/*  85 */     this.goalSelector.addGoal(7, this.randomStrollGoal);
/*  86 */     this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 8.0F));
/*     */     
/*  88 */     this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Guardian.class, 12.0F, 0.01F));
/*  89 */     this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
/*     */ 
/*     */     
/*  92 */     this.randomStrollGoal.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*  93 */     goal.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     
/*  95 */     this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, true, false, new GuardianAttackSelector(this)));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  99 */     return Monster.createMonsterAttributes()
/* 100 */       .add(Attributes.ATTACK_DAMAGE, 6.0D)
/* 101 */       .add(Attributes.MOVEMENT_SPEED, 0.5D)
/* 102 */       .add(Attributes.MAX_HEALTH, 30.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 107 */   protected PathNavigation createNavigation(Level level) { return new WaterBoundPathNavigation(this, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 112 */     super.defineSynchedData(entityData);
/*     */     
/* 114 */     entityData.define(DATA_ID_MOVING, Boolean.valueOf(false));
/* 115 */     entityData.define(DATA_ID_ATTACK_TARGET, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/* 119 */   public boolean isMoving() { return ((Boolean)this.entityData.get(DATA_ID_MOVING)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 123 */   private void setMoving(boolean value) { this.entityData.set(DATA_ID_MOVING, Boolean.valueOf(value)); }
/*     */ 
/*     */ 
/*     */   
/* 127 */   public int getAttackDuration() { return 80; }
/*     */ 
/*     */ 
/*     */   
/* 131 */   private void setActiveAttackTarget(int entityId) { this.entityData.set(DATA_ID_ATTACK_TARGET, Integer.valueOf(entityId)); }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public boolean hasActiveAttackTarget() { return (((Integer)this.entityData.get(DATA_ID_ATTACK_TARGET)).intValue() != 0); }
/*     */ 
/*     */   
/*     */   public LivingEntity getActiveAttackTarget() {
/* 139 */     if (!hasActiveAttackTarget()) {
/* 140 */       return null;
/*     */     }
/* 142 */     if (level().isClientSide()) {
/* 143 */       if (this.clientSideCachedAttackTarget != null) {
/* 144 */         return this.clientSideCachedAttackTarget;
/*     */       }
/* 146 */       Entity entity = level().getEntity(((Integer)this.entityData.get(DATA_ID_ATTACK_TARGET)).intValue());
/* 147 */       if (entity instanceof LivingEntity) {
/* 148 */         this.clientSideCachedAttackTarget = (LivingEntity)entity;
/* 149 */         return this.clientSideCachedAttackTarget;
/*     */       } 
/* 151 */       return null;
/*     */     } 
/* 153 */     return getTarget();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 158 */     super.onSyncedDataUpdated(accessor);
/*     */     
/* 160 */     if (DATA_ID_ATTACK_TARGET.equals(accessor)) {
/* 161 */       this.clientSideAttackTime = 0;
/* 162 */       this.clientSideCachedAttackTarget = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public int getAmbientSoundInterval() { return 160; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 173 */   protected SoundEvent getAmbientSound() { return isInWater() ? SoundEvents.GUARDIAN_AMBIENT : SoundEvents.GUARDIAN_AMBIENT_LAND; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 178 */   protected SoundEvent getHurtSound(DamageSource source) { return isInWater() ? SoundEvents.GUARDIAN_HURT : SoundEvents.GUARDIAN_HURT_LAND; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 183 */   protected SoundEvent getDeathSound() { return isInWater() ? SoundEvents.GUARDIAN_DEATH : SoundEvents.GUARDIAN_DEATH_LAND; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 188 */   protected Entity.MovementEmission getMovementEmission() { return Entity.MovementEmission.EVENTS; }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/* 193 */     if (level.getFluidState(pos).is(FluidTags.WATER)) {
/* 194 */       return 10.0F + level.getPathfindingCostFromLightLevels(pos);
/*     */     }
/* 196 */     return super.getWalkTargetValue(pos, level);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 201 */     if (isAlive()) {
/* 202 */       if (level().isClientSide()) {
/*     */         
/* 204 */         this.clientSideTailAnimationO = this.clientSideTailAnimation;
/* 205 */         if (!isInWater()) {
/* 206 */           this.clientSideTailAnimationSpeed = 2.0F;
/* 207 */           Vec3 movement = getDeltaMovement();
/* 208 */           if (movement.y > 0.0D && this.clientSideTouchedGround && !isSilent()) {
/* 209 */             level().playLocalSound(getX(), getY(), getZ(), getFlopSound(), getSoundSource(), 1.0F, 1.0F, false);
/*     */           }
/* 211 */           this.clientSideTouchedGround = (movement.y < 0.0D && level().loadedAndEntityCanStandOn(blockPosition().below(), this));
/* 212 */         } else if (isMoving()) {
/* 213 */           if (this.clientSideTailAnimationSpeed < 0.5F) {
/* 214 */             this.clientSideTailAnimationSpeed = 4.0F;
/*     */           } else {
/* 216 */             this.clientSideTailAnimationSpeed += (0.5F - this.clientSideTailAnimationSpeed) * 0.1F;
/*     */           } 
/*     */         } else {
/* 219 */           this.clientSideTailAnimationSpeed += (0.125F - this.clientSideTailAnimationSpeed) * 0.2F;
/*     */         } 
/* 221 */         this.clientSideTailAnimation += this.clientSideTailAnimationSpeed;
/*     */ 
/*     */         
/* 224 */         this.clientSideSpikesAnimationO = this.clientSideSpikesAnimation;
/* 225 */         if (!isInWater()) {
/* 226 */           this.clientSideSpikesAnimation = this.random.nextFloat();
/* 227 */         } else if (isMoving()) {
/* 228 */           this.clientSideSpikesAnimation += (0.0F - this.clientSideSpikesAnimation) * 0.25F;
/*     */         } else {
/* 230 */           this.clientSideSpikesAnimation += (1.0F - this.clientSideSpikesAnimation) * 0.06F;
/*     */         } 
/*     */         
/* 233 */         if (isMoving() && isInWater()) {
/* 234 */           Vec3 viewVector = getViewVector(0.0F);
/* 235 */           for (int i = 0; i < 2; i++) {
/* 236 */             level().addParticle(ParticleTypes.BUBBLE, getRandomX(0.5D) - viewVector.x * 1.5D, getRandomY() - viewVector.y * 1.5D, getRandomZ(0.5D) - viewVector.z * 1.5D, 0.0D, 0.0D, 0.0D);
/*     */           }
/*     */         } 
/*     */         
/* 240 */         if (hasActiveAttackTarget()) {
/* 241 */           if (this.clientSideAttackTime < getAttackDuration()) {
/* 242 */             this.clientSideAttackTime++;
/*     */           }
/* 244 */           LivingEntity attackTarget = getActiveAttackTarget();
/* 245 */           if (attackTarget != null) {
/* 246 */             getLookControl().setLookAt(attackTarget, 90.0F, 90.0F);
/* 247 */             getLookControl().tick();
/*     */             
/* 249 */             double at = getAttackAnimationScale(0.0F);
/* 250 */             double dx = attackTarget.getX() - getX();
/* 251 */             double dy = attackTarget.getY(0.5D) - getEyeY();
/* 252 */             double dz = attackTarget.getZ() - getZ();
/* 253 */             double dd = Math.sqrt(dx * dx + dy * dy + dz * dz);
/* 254 */             dx /= dd;
/* 255 */             dy /= dd;
/* 256 */             dz /= dd;
/* 257 */             double dist = this.random.nextDouble();
/* 258 */             while (dist < dd) {
/* 259 */               dist += 1.8D - at + this.random.nextDouble() * (1.7D - at);
/* 260 */               level().addParticle(ParticleTypes.BUBBLE, getX() + dx * dist, getEyeY() + dy * dist, getZ() + dz * dist, 0.0D, 0.0D, 0.0D);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 266 */       if (isInWater()) {
/* 267 */         setAirSupply(300);
/*     */       }
/* 269 */       else if (onGround()) {
/* 270 */         setDeltaMovement(getDeltaMovement().add(((this.random
/* 271 */               .nextFloat() * 2.0F - 1.0F) * 0.4F), 0.5D, ((this.random
/*     */               
/* 273 */               .nextFloat() * 2.0F - 1.0F) * 0.4F)));
/*     */         
/* 275 */         setYRot(this.random.nextFloat() * 360.0F);
/* 276 */         setOnGround(false);
/* 277 */         this.needsSync = true;
/*     */       } 
/*     */ 
/*     */       
/* 281 */       if (hasActiveAttackTarget()) {
/* 282 */         setYRot(this.yHeadRot);
/*     */       }
/*     */     } 
/*     */     
/* 286 */     super.aiStep();
/*     */   }
/*     */ 
/*     */   
/* 290 */   protected SoundEvent getFlopSound() { return SoundEvents.GUARDIAN_FLOP; }
/*     */ 
/*     */ 
/*     */   
/* 294 */   public float getTailAnimation(float a) { return Mth.lerp(a, this.clientSideTailAnimationO, this.clientSideTailAnimation); }
/*     */ 
/*     */ 
/*     */   
/* 298 */   public float getSpikesAnimation(float a) { return Mth.lerp(a, this.clientSideSpikesAnimationO, this.clientSideSpikesAnimation); }
/*     */ 
/*     */ 
/*     */   
/* 302 */   public float getAttackAnimationScale(float a) { return (this.clientSideAttackTime + a) / getAttackDuration(); }
/*     */ 
/*     */ 
/*     */   
/* 306 */   public float getClientSideAttackTime() { return this.clientSideAttackTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 311 */   public boolean checkSpawnObstruction(LevelReader level) { return level.isUnobstructed(this); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean checkGuardianSpawnRules(EntityType<? extends Guardian> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 316 */     return ((random.nextInt(20) == 0 || !level.canSeeSkyFromBelowWater(pos)) && level
/* 317 */       .getDifficulty() != Difficulty.PEACEFUL && (
/* 318 */       EntitySpawnReason.isSpawner(spawnReason) || level.getFluidState(pos).is(FluidTags.WATER)) && level
/* 319 */       .getFluidState(pos.below()).is(FluidTags.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 324 */     if (!isMoving() && !source.is(DamageTypeTags.AVOIDS_GUARDIAN_THORNS) && !source.is(DamageTypes.THORNS)) { Entity entity = source.getDirectEntity(); if (entity instanceof LivingEntity) { LivingEntity cause = (LivingEntity)entity;
/* 325 */         cause.hurtServer(level, damageSources().thorns(this), 2.0F); }
/*     */        }
/*     */     
/* 328 */     if (this.randomStrollGoal != null) {
/* 329 */       this.randomStrollGoal.trigger();
/*     */     }
/*     */     
/* 332 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 337 */   public int getMaxHeadXRot() { return 180; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 342 */     moveRelative(0.1F, input);
/* 343 */     move(MoverType.SELF, getDeltaMovement());
/*     */     
/* 345 */     setDeltaMovement(getDeltaMovement().scale(0.9D));
/*     */     
/* 347 */     if (!isMoving() && getTarget() == null)
/* 348 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.005D, 0.0D)); 
/*     */   }
/*     */   
/*     */   private static class GuardianAttackSelector
/*     */     implements TargetingConditions.Selector
/*     */   {
/*     */     private final Guardian guardian;
/*     */     
/* 356 */     public GuardianAttackSelector(Guardian guardian) { this.guardian = guardian; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 361 */     public boolean test(LivingEntity target, ServerLevel level) { return ((target instanceof net.minecraft.world.entity.player.Player || target instanceof net.minecraft.world.entity.animal.squid.Squid || target instanceof net.minecraft.world.entity.animal.axolotl.Axolotl) && target.distanceToSqr(this.guardian) > 9.0D); }
/*     */   }
/*     */   
/*     */   private static class GuardianAttackGoal
/*     */     extends Goal {
/*     */     private final Guardian guardian;
/*     */     private int attackTime;
/*     */     private final boolean elder;
/*     */     
/*     */     public GuardianAttackGoal(Guardian guardian) {
/* 371 */       this.guardian = guardian;
/*     */ 
/*     */       
/* 374 */       this.elder = guardian instanceof ElderGuardian;
/*     */       
/* 376 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 381 */       LivingEntity target = this.guardian.getTarget();
/* 382 */       return (target != null && target.isAlive());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 387 */     public boolean canContinueToUse() { return (super.canContinueToUse() && (this.elder || (this.guardian.getTarget() != null && this.guardian.distanceToSqr(this.guardian.getTarget()) > 9.0D))); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() {
/* 392 */       this.attackTime = -10;
/* 393 */       this.guardian.getNavigation().stop();
/* 394 */       LivingEntity target = this.guardian.getTarget();
/* 395 */       if (target != null) {
/* 396 */         this.guardian.getLookControl().setLookAt(target, 90.0F, 90.0F);
/*     */       }
/*     */ 
/*     */       
/* 400 */       this.guardian.needsSync = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 405 */       this.guardian.setActiveAttackTarget(0);
/* 406 */       this.guardian.setTarget(null);
/*     */       
/* 408 */       this.guardian.randomStrollGoal.trigger();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 413 */     public boolean requiresUpdateEveryTick() { return true; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 418 */       LivingEntity target = this.guardian.getTarget();
/* 419 */       if (target == null) {
/*     */         return;
/*     */       }
/*     */       
/* 423 */       this.guardian.getNavigation().stop();
/* 424 */       this.guardian.getLookControl().setLookAt(target, 90.0F, 90.0F);
/*     */       
/* 426 */       if (!this.guardian.hasLineOfSight(target)) {
/* 427 */         this.guardian.setTarget(null);
/*     */         
/*     */         return;
/*     */       } 
/* 431 */       this.attackTime++;
/* 432 */       if (this.attackTime == 0) {
/*     */         
/* 434 */         this.guardian.setActiveAttackTarget(target.getId());
/* 435 */         if (!this.guardian.isSilent()) {
/* 436 */           this.guardian.level().broadcastEntityEvent(this.guardian, (byte)21);
/*     */         }
/* 438 */       } else if (this.attackTime >= this.guardian.getAttackDuration()) {
/* 439 */         float magicDamage = 1.0F;
/* 440 */         if (this.guardian.level().getDifficulty() == Difficulty.HARD) {
/* 441 */           magicDamage += 2.0F;
/*     */         }
/* 443 */         if (this.elder) {
/* 444 */           magicDamage += 2.0F;
/*     */         }
/* 446 */         ServerLevel serverLevel = getServerLevel(this.guardian);
/* 447 */         target.hurtServer(serverLevel, this.guardian.damageSources().indirectMagic(this.guardian, this.guardian), magicDamage);
/* 448 */         this.guardian.doHurtTarget(serverLevel, target);
/* 449 */         this.guardian.setTarget(null);
/*     */       } 
/*     */       
/* 452 */       super.tick();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class GuardianMoveControl extends MoveControl {
/*     */     private final Guardian guardian;
/*     */     
/*     */     public GuardianMoveControl(Guardian guardian) {
/* 460 */       super(guardian);
/* 461 */       this.guardian = guardian;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 466 */       if (this.operation != MoveControl.Operation.MOVE_TO || this.guardian.getNavigation().isDone()) {
/*     */         
/* 468 */         this.guardian.setSpeed(0.0F);
/* 469 */         this.guardian.setMoving(false);
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 477 */       Vec3 delta = new Vec3(this.wantedX - this.guardian.getX(), this.wantedY - this.guardian.getY(), this.wantedZ - this.guardian.getZ());
/*     */       
/* 479 */       double length = delta.length();
/*     */       
/* 481 */       double xd = delta.x / length;
/* 482 */       double yd = delta.y / length;
/* 483 */       double zd = delta.z / length;
/*     */       
/* 485 */       float yRotD = (float)(Mth.atan2(delta.z, delta.x) * 57.2957763671875D) - 90.0F;
/*     */       
/* 487 */       this.guardian.setYRot(rotlerp(this.guardian.getYRot(), yRotD, 90.0F));
/* 488 */       this.guardian.yBodyRot = this.guardian.getYRot();
/*     */       
/* 490 */       float targetSpeed = (float)(this.speedModifier * this.guardian.getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 491 */       float newSpeed = Mth.lerp(0.125F, this.guardian.getSpeed(), targetSpeed);
/* 492 */       this.guardian.setSpeed(newSpeed);
/* 493 */       double push = Math.sin((this.guardian.tickCount + this.guardian.getId()) * 0.5D) * 0.05D;
/* 494 */       double cos = Math.cos((this.guardian.getYRot() * 0.017453292F));
/* 495 */       double sin = Math.sin((this.guardian.getYRot() * 0.017453292F));
/* 496 */       double yPush = Math.sin((this.guardian.tickCount + this.guardian.getId()) * 0.75D) * 0.05D;
/*     */       
/* 498 */       this.guardian.setDeltaMovement(this.guardian.getDeltaMovement().add(push * cos, yPush * (sin + cos) * 0.25D + newSpeed * yd * 0.1D, push * sin));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 504 */       LookControl control = this.guardian.getLookControl();
/* 505 */       double newLookX = this.guardian.getX() + xd * 2.0D;
/* 506 */       double newLookY = this.guardian.getEyeY() + yd / length;
/* 507 */       double newLookZ = this.guardian.getZ() + zd * 2.0D;
/* 508 */       double oldLookX = control.getWantedX();
/* 509 */       double oldLookY = control.getWantedY();
/* 510 */       double oldLookZ = control.getWantedZ();
/* 511 */       if (!control.isLookingAtTarget()) {
/* 512 */         oldLookX = newLookX;
/* 513 */         oldLookY = newLookY;
/* 514 */         oldLookZ = newLookZ;
/*     */       } 
/* 516 */       this.guardian.getLookControl().setLookAt(Mth.lerp(0.125D, oldLookX, newLookX), Mth.lerp(0.125D, oldLookY, newLookY), Mth.lerp(0.125D, oldLookZ, newLookZ), 10.0F, 40.0F);
/* 517 */       this.guardian.setMoving(true);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Guardian.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */