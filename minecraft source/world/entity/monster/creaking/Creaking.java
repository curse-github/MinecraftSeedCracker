/*     */ package net.minecraft.world.entity.monster.creaking;
/*     */ 
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ import net.minecraft.world.entity.ai.control.JumpControl;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CreakingHeartBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.CreakingHeartBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.CreakingHeartState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.PathfindingContext;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Creaking
/*     */   extends Monster
/*     */ {
/*  60 */   private static final EntityDataAccessor<Boolean> CAN_MOVE = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.BOOLEAN);
/*  61 */   private static final EntityDataAccessor<Boolean> IS_ACTIVE = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  63 */   private static final EntityDataAccessor<Boolean> IS_TEARING_DOWN = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  65 */   private static final EntityDataAccessor<Optional<BlockPos>> HOME_POS = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
/*     */   
/*     */   private static final int ATTACK_ANIMATION_DURATION = 15;
/*     */   
/*     */   private static final int MAX_HEALTH = 1;
/*     */   
/*     */   private static final float ATTACK_DAMAGE = 3.0F;
/*     */   
/*     */   private static final float FOLLOW_RANGE = 32.0F;
/*     */   
/*     */   private static final float ACTIVATION_RANGE_SQ = 144.0F;
/*     */   
/*     */   public static final int ATTACK_INTERVAL = 40;
/*     */   
/*     */   private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.4F;
/*     */   
/*     */   public static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.3F;
/*     */   
/*     */   public static final int CREAKING_ORANGE = 16545810;
/*     */   public static final int CREAKING_GRAY = 6250335;
/*     */   public static final int INVULNERABILITY_ANIMATION_DURATION = 8;
/*     */   public static final int TWITCH_DEATH_DURATION = 45;
/*     */   private static final int MAX_PLAYER_STUCK_COUNTER = 4;
/*     */   private int attackAnimationRemainingTicks;
/*  89 */   public final AnimationState attackAnimationState = new AnimationState();
/*  90 */   public final AnimationState invulnerabilityAnimationState = new AnimationState();
/*     */ 
/*     */   
/*  93 */   public final AnimationState deathAnimationState = new AnimationState();
/*     */   
/*     */   private int invulnerabilityAnimationRemainingTicks;
/*     */   
/*     */   private boolean eyesGlowing;
/*     */   private int nextFlickerTime;
/*     */   private int playerStuckCounter;
/*     */   
/*     */   public Creaking(EntityType<? extends Creaking> type, Level level) {
/* 102 */     super(type, level);
/*     */     
/* 104 */     this.lookControl = new CreakingLookControl(this);
/* 105 */     this.moveControl = new CreakingMoveControl(this);
/* 106 */     this.jumpControl = new CreakingJumpControl(this);
/*     */     
/* 108 */     GroundPathNavigation navigation = (GroundPathNavigation)getNavigation();
/* 109 */     navigation.setCanFloat(true);
/*     */     
/* 111 */     this.xpReward = 0;
/*     */   }
/*     */   
/*     */   public void setTransient(BlockPos pos) {
/* 115 */     setHomePos(pos);
/* 116 */     setPathfindingMalus(PathType.DAMAGE_OTHER, 8.0F);
/* 117 */     setPathfindingMalus(PathType.POWDER_SNOW, 8.0F);
/* 118 */     setPathfindingMalus(PathType.LAVA, 8.0F);
/* 119 */     setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
/* 120 */     setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
/*     */   }
/*     */ 
/*     */   
/* 124 */   public boolean isHeartBound() { return (getHomePos() != null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   protected BodyRotationControl createBodyControl() { return new CreakingBodyRotationControl(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   protected Brain.Provider<Creaking> brainProvider() { return CreakingAi.brainProvider(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   protected Brain<?> makeBrain(Dynamic<?> input) { return CreakingAi.makeBrain(this, brainProvider().makeBrain(input)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 144 */     super.defineSynchedData(entityData);
/* 145 */     entityData.define(CAN_MOVE, Boolean.valueOf(true));
/* 146 */     entityData.define(IS_ACTIVE, Boolean.valueOf(false));
/* 147 */     entityData.define(IS_TEARING_DOWN, Boolean.valueOf(false));
/* 148 */     entityData.define(HOME_POS, Optional.empty());
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 152 */     return Monster.createMonsterAttributes()
/* 153 */       .add(Attributes.MAX_HEALTH, 1.0D)
/* 154 */       .add(Attributes.MOVEMENT_SPEED, 0.4000000059604645D)
/* 155 */       .add(Attributes.ATTACK_DAMAGE, 3.0D)
/* 156 */       .add(Attributes.FOLLOW_RANGE, 32.0D)
/* 157 */       .add(Attributes.STEP_HEIGHT, 1.0625D);
/*     */   }
/*     */ 
/*     */   
/* 161 */   public boolean canMove() { return ((Boolean)this.entityData.get(CAN_MOVE)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/* 166 */     if (!(target instanceof LivingEntity)) {
/* 167 */       return false;
/*     */     }
/* 169 */     this.attackAnimationRemainingTicks = 15;
/* 170 */     level().broadcastEntityEvent(this, (byte)4);
/*     */     
/* 172 */     return super.doHurtTarget(level, target);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 177 */     BlockPos homePos = getHomePos();
/* 178 */     if (homePos == null || source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
/* 179 */       return super.hurtServer(level, source, damage);
/*     */     }
/*     */     
/* 182 */     if (isInvulnerableTo(level, source) || this.invulnerabilityAnimationRemainingTicks > 0 || isDeadOrDying()) {
/* 183 */       return false;
/*     */     }
/*     */     
/* 186 */     Player responsiblePlayer = blameSourceForDamage(source);
/*     */     
/* 188 */     Entity directEntity = source.getDirectEntity();
/* 189 */     if (!(directEntity instanceof LivingEntity) && !(directEntity instanceof net.minecraft.world.entity.projectile.Projectile) && responsiblePlayer == null) {
/* 190 */       return false;
/*     */     }
/*     */     
/* 193 */     this.invulnerabilityAnimationRemainingTicks = 8;
/* 194 */     level().broadcastEntityEvent(this, (byte)66);
/* 195 */     gameEvent(GameEvent.ENTITY_ACTION);
/*     */     
/* 197 */     BlockEntity blockEntity = level().getBlockEntity(homePos); if (blockEntity instanceof CreakingHeartBlockEntity) { CreakingHeartBlockEntity creakingHeartBlockEntity = (CreakingHeartBlockEntity)blockEntity;
/* 198 */       if (creakingHeartBlockEntity.isProtector(this)) {
/* 199 */         if (responsiblePlayer != null) {
/* 200 */           creakingHeartBlockEntity.creakingHurt();
/*     */         }
/* 202 */         playHurtSound(source);
/*     */       }  }
/*     */     
/* 205 */     return true;
/*     */   }
/*     */   
/*     */   public Player blameSourceForDamage(DamageSource source) {
/* 209 */     resolveMobResponsibleForDamage(source);
/* 210 */     return resolvePlayerResponsibleForDamage(source);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 215 */   public boolean isPushable() { return (super.isPushable() && canMove()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void push(double xa, double ya, double za) {
/* 220 */     if (!canMove()) {
/*     */       return;
/*     */     }
/* 223 */     super.push(xa, ya, za);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 229 */   public Brain<Creaking> getBrain() { return super.getBrain(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 234 */     ProfilerFiller profiler = Profiler.get();
/* 235 */     profiler.push("creakingBrain");
/* 236 */     getBrain().tick((ServerLevel)level(), this);
/* 237 */     profiler.pop();
/* 238 */     CreakingAi.updateActivity(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 244 */     if (this.invulnerabilityAnimationRemainingTicks > 0) {
/* 245 */       this.invulnerabilityAnimationRemainingTicks--;
/*     */     }
/* 247 */     if (this.attackAnimationRemainingTicks > 0) {
/* 248 */       this.attackAnimationRemainingTicks--;
/*     */     }
/*     */     
/* 251 */     if (!level().isClientSide()) {
/* 252 */       boolean canMove = ((Boolean)this.entityData.get(CAN_MOVE)).booleanValue();
/* 253 */       boolean nowCanMove = checkCanMove();
/* 254 */       if (nowCanMove != canMove) {
/*     */         
/* 256 */         gameEvent(GameEvent.ENTITY_ACTION);
/* 257 */         if (nowCanMove) {
/* 258 */           makeSound(SoundEvents.CREAKING_UNFREEZE);
/*     */         } else {
/* 260 */           stopInPlace();
/* 261 */           makeSound(SoundEvents.CREAKING_FREEZE);
/*     */         } 
/*     */       } 
/* 264 */       this.entityData.set(CAN_MOVE, Boolean.valueOf(nowCanMove));
/*     */     } 
/* 266 */     super.aiStep();
/*     */   }
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
/*     */   public void tick() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   4: invokevirtual isClientSide : ()Z
/*     */     //   7: ifne -> 66
/*     */     //   10: aload_0
/*     */     //   11: invokevirtual getHomePos : ()Lnet/minecraft/core/BlockPos;
/*     */     //   14: astore_1
/*     */     //   15: aload_1
/*     */     //   16: ifnull -> 66
/*     */     //   19: aload_0
/*     */     //   20: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   23: aload_1
/*     */     //   24: invokevirtual getBlockEntity : (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;
/*     */     //   27: astore #4
/*     */     //   29: aload #4
/*     */     //   31: instanceof net/minecraft/world/level/block/entity/CreakingHeartBlockEntity
/*     */     //   34: ifeq -> 55
/*     */     //   37: aload #4
/*     */     //   39: checkcast net/minecraft/world/level/block/entity/CreakingHeartBlockEntity
/*     */     //   42: astore_3
/*     */     //   43: aload_3
/*     */     //   44: aload_0
/*     */     //   45: invokevirtual isProtector : (Lnet/minecraft/world/entity/monster/creaking/Creaking;)Z
/*     */     //   48: ifeq -> 55
/*     */     //   51: iconst_1
/*     */     //   52: goto -> 56
/*     */     //   55: iconst_0
/*     */     //   56: istore_2
/*     */     //   57: iload_2
/*     */     //   58: ifne -> 66
/*     */     //   61: aload_0
/*     */     //   62: fconst_0
/*     */     //   63: invokevirtual setHealth : (F)V
/*     */     //   66: aload_0
/*     */     //   67: invokespecial tick : ()V
/*     */     //   70: aload_0
/*     */     //   71: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   74: invokevirtual isClientSide : ()Z
/*     */     //   77: ifeq -> 88
/*     */     //   80: aload_0
/*     */     //   81: invokevirtual setupAnimationStates : ()V
/*     */     //   84: aload_0
/*     */     //   85: invokevirtual checkEyeBlink : ()V
/*     */     //   88: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #271	-> 0
/*     */     //   #272	-> 10
/*     */     //   #273	-> 15
/*     */     //   #274	-> 19
/*     */     //   #275	-> 57
/*     */     //   #276	-> 61
/*     */     //   #281	-> 66
/*     */     //   #282	-> 70
/*     */     //   #283	-> 80
/*     */     //   #284	-> 84
/*     */     //   #286	-> 88
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   43	12	3	creakingHeartBlockEntity	Lnet/minecraft/world/level/block/entity/CreakingHeartBlockEntity;
/*     */     //   57	9	2	hasProtectionFromCreakingHeart	Z
/*     */     //   15	51	1	homePos	Lnet/minecraft/core/BlockPos;
/*     */     //   0	89	0	this	Lnet/minecraft/world/entity/monster/creaking/Creaking; }
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
/*     */   protected void tickDeath() {
/* 290 */     if (isHeartBound() && isTearingDown()) {
/* 291 */       this.deathTime++;
/* 292 */       if (!level().isClientSide() && this.deathTime > 45 && !isRemoved()) {
/* 293 */         tearDown();
/*     */       }
/*     */     } else {
/* 296 */       super.tickDeath();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateWalkAnimation(float distance) {
/* 303 */     float targetSpeed = Math.min(distance * 25.0F, 3.0F);
/* 304 */     this.walkAnimation.update(targetSpeed, 0.4F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupAnimationStates() {
/* 309 */     this.attackAnimationState.animateWhen((this.attackAnimationRemainingTicks > 0), this.tickCount);
/* 310 */     this.invulnerabilityAnimationState.animateWhen((this.invulnerabilityAnimationRemainingTicks > 0), this.tickCount);
/* 311 */     this.deathAnimationState.animateWhen(isTearingDown(), this.tickCount);
/*     */   }
/*     */   
/*     */   public void tearDown() {
/* 315 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 316 */       AABB box = getBoundingBox();
/*     */       
/* 318 */       Vec3 center = box.getCenter();
/* 319 */       double xSpread = box.getXsize() * 0.3D;
/* 320 */       double ySpread = box.getYsize() * 0.3D;
/* 321 */       double zSpread = box.getZsize() * 0.3D;
/* 322 */       serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK_CRUMBLE, Blocks.PALE_OAK_WOOD.defaultBlockState()), center.x, center.y, center.z, 100, xSpread, ySpread, zSpread, 0.0D);
/* 323 */       serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK_CRUMBLE, (BlockState)Blocks.CREAKING_HEART.defaultBlockState().setValue(CreakingHeartBlock.STATE, CreakingHeartState.AWAKE)), center.x, center.y, center.z, 10, xSpread, ySpread, zSpread, 0.0D); }
/*     */     
/* 325 */     makeSound(getDeathSound());
/* 326 */     remove(Entity.RemovalReason.DISCARDED);
/*     */   }
/*     */   
/*     */   public void creakingDeathEffects(DamageSource source) {
/* 330 */     blameSourceForDamage(source);
/* 331 */     die(source);
/* 332 */     makeSound(SoundEvents.CREAKING_TWITCH);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 338 */     if (id == 66) {
/* 339 */       this.invulnerabilityAnimationRemainingTicks = 8;
/* 340 */       playHurtSound(damageSources().generic());
/* 341 */     } else if (id == 4) {
/* 342 */       this.attackAnimationRemainingTicks = 15;
/* 343 */       playAttackSound();
/*     */     } else {
/* 345 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 351 */   public boolean fireImmune() { return (isHeartBound() || super.fireImmune()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 356 */   public boolean canUsePortal(boolean ignorePassenger) { return (!isHeartBound() && super.canUsePortal(ignorePassenger)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 361 */   protected PathNavigation createNavigation(Level level) { return new CreakingPathNavigation(this, level); }
/*     */ 
/*     */   
/*     */   public boolean playerIsStuckInYou() {
/* 365 */     List<Player> players = (List)this.brain.getMemory(MemoryModuleType.NEAREST_PLAYERS).orElse(List.of());
/* 366 */     if (players.isEmpty()) {
/* 367 */       this.playerStuckCounter = 0;
/* 368 */       return false;
/*     */     } 
/* 370 */     AABB ownBox = getBoundingBox();
/* 371 */     for (Player player : players) {
/* 372 */       if (ownBox.contains(player.getEyePosition())) {
/* 373 */         this.playerStuckCounter++;
/* 374 */         return (this.playerStuckCounter > 4);
/*     */       } 
/*     */     } 
/* 377 */     this.playerStuckCounter = 0;
/* 378 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 383 */     super.readAdditionalSaveData(input);
/* 384 */     input.read("home_pos", BlockPos.CODEC).ifPresent(this::setTransient);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 389 */     super.addAdditionalSaveData(output);
/* 390 */     output.storeNullable("home_pos", BlockPos.CODEC, getHomePos());
/*     */   }
/*     */ 
/*     */   
/* 394 */   public void setHomePos(BlockPos pos) { this.entityData.set(HOME_POS, Optional.of(pos)); }
/*     */ 
/*     */ 
/*     */   
/* 398 */   public BlockPos getHomePos() { return (BlockPos)((Optional)this.entityData.get(HOME_POS)).orElse(null); }
/*     */ 
/*     */ 
/*     */   
/* 402 */   public void setTearingDown() { this.entityData.set(IS_TEARING_DOWN, Boolean.valueOf(true)); }
/*     */ 
/*     */ 
/*     */   
/* 406 */   public boolean isTearingDown() { return ((Boolean)this.entityData.get(IS_TEARING_DOWN)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 410 */   public boolean hasGlowingEyes() { return this.eyesGlowing; }
/*     */ 
/*     */   
/*     */   public void checkEyeBlink() {
/* 414 */     if (this.deathTime > this.nextFlickerTime) {
/* 415 */       this.nextFlickerTime = this.deathTime + getRandom().nextIntBetweenInclusive(this.eyesGlowing ? 2 : (this.deathTime / 4), this.eyesGlowing ? 8 : (this.deathTime / 2));
/* 416 */       this.eyesGlowing = !this.eyesGlowing;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 422 */   public void playAttackSound() { makeSound(SoundEvents.CREAKING_ATTACK); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 427 */     if (isActive()) {
/* 428 */       return null;
/*     */     }
/* 430 */     return SoundEvents.CREAKING_AMBIENT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 435 */   protected SoundEvent getHurtSound(DamageSource source) { return isHeartBound() ? SoundEvents.CREAKING_SWAY : super.getHurtSound(source); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 440 */   protected SoundEvent getDeathSound() { return SoundEvents.CREAKING_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 445 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.CREAKING_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 450 */   public LivingEntity getTarget() { return getTargetFromBrain(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void knockback(double power, double xd, double zd) {
/* 455 */     if (!canMove()) {
/*     */       return;
/*     */     }
/* 458 */     super.knockback(power, xd, zd);
/*     */   }
/*     */   
/*     */   public boolean checkCanMove() {
/* 462 */     List<Player> players = (List)this.brain.getMemory(MemoryModuleType.NEAREST_PLAYERS).orElse(List.of());
/*     */     
/* 464 */     boolean active = isActive();
/* 465 */     if (players.isEmpty()) {
/* 466 */       if (active) {
/* 467 */         deactivate();
/*     */       }
/* 469 */       return true;
/*     */     } 
/* 471 */     boolean hasPotentialTarget = false;
/* 472 */     for (Player player : players) {
/* 473 */       if (!canAttack(player) || isAlliedTo(player)) {
/*     */         continue;
/*     */       }
/* 476 */       hasPotentialTarget = true;
/* 477 */       if (active && !LivingEntity.PLAYER_NOT_WEARING_DISGUISE_ITEM.test(player)) {
/*     */         continue;
/*     */       }
/*     */       
/* 481 */       if (isLookingAtMe(player, 0.5D, false, true, new double[] { getEyeY(), getY() + 0.5D * getScale(), (getEyeY() + getY()) / 2.0D })) {
/* 482 */         if (active) {
/* 483 */           return false;
/*     */         }
/* 485 */         if (player.distanceToSqr(this) < 144.0D) {
/* 486 */           activate(player);
/* 487 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/* 491 */     if (!hasPotentialTarget && active) {
/* 492 */       deactivate();
/*     */     }
/* 494 */     return true;
/*     */   }
/*     */   
/*     */   public void activate(Player player) {
/* 498 */     getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, player);
/* 499 */     gameEvent(GameEvent.ENTITY_ACTION);
/* 500 */     makeSound(SoundEvents.CREAKING_ACTIVATE);
/* 501 */     setIsActive(true);
/*     */   }
/*     */   
/*     */   public void deactivate() {
/* 505 */     getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/* 506 */     gameEvent(GameEvent.ENTITY_ACTION);
/* 507 */     makeSound(SoundEvents.CREAKING_DEACTIVATE);
/* 508 */     setIsActive(false);
/*     */   }
/*     */ 
/*     */   
/* 512 */   public void setIsActive(boolean active) { this.entityData.set(IS_ACTIVE, Boolean.valueOf(active)); }
/*     */ 
/*     */ 
/*     */   
/* 516 */   public boolean isActive() { return ((Boolean)this.entityData.get(IS_ACTIVE)).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 521 */   public float getWalkTargetValue(BlockPos pos, LevelReader level) { return 0.0F; }
/*     */   
/*     */   private class CreakingLookControl
/*     */     extends LookControl
/*     */   {
/* 526 */     public CreakingLookControl(Creaking creaking) { super(creaking); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 531 */       if (Creaking.this.canMove())
/* 532 */         super.tick(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private class CreakingMoveControl
/*     */     extends MoveControl
/*     */   {
/* 539 */     public CreakingMoveControl(Creaking creaking) { super(creaking); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 544 */       if (Creaking.this.canMove())
/* 545 */         super.tick(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private class CreakingJumpControl
/*     */     extends JumpControl
/*     */   {
/* 552 */     public CreakingJumpControl(Creaking creaking) { super(creaking); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 557 */       if (Creaking.this.canMove()) {
/* 558 */         super.tick();
/*     */       } else {
/* 560 */         Creaking.this.setJumping(false);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class CreakingBodyRotationControl
/*     */     extends BodyRotationControl {
/* 567 */     public CreakingBodyRotationControl(Creaking creaking) { super(creaking); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void clientTick() {
/* 572 */       if (Creaking.this.canMove()) {
/* 573 */         super.clientTick();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class HomeNodeEvaluator
/*     */     extends WalkNodeEvaluator
/*     */   {
/*     */     private static final int MAX_DISTANCE_TO_HOME_SQ = 1024;
/*     */     
/*     */     public PathType getPathType(PathfindingContext context, int x, int y, int z) {
/* 584 */       BlockPos homePos = Creaking.this.getHomePos();
/*     */       
/* 586 */       if (homePos == null) {
/* 587 */         return super.getPathType(context, x, y, z);
/*     */       }
/*     */       
/* 590 */       double homeDistance = homePos.distSqr(new Vec3i(x, y, z));
/* 591 */       if (homeDistance > 1024.0D && homeDistance >= homePos.distSqr(context.mobPosition())) {
/* 592 */         return PathType.BLOCKED;
/*     */       }
/* 594 */       return super.getPathType(context, x, y, z);
/*     */     }
/*     */   }
/*     */   
/*     */   private class CreakingPathNavigation
/*     */     extends GroundPathNavigation {
/* 600 */     CreakingPathNavigation(Creaking mob, Level level) { super(mob, level); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 605 */       if (Creaking.this.canMove()) {
/* 606 */         super.tick();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected PathFinder createPathFinder(int maxVisitedNodes) {
/* 612 */       this.nodeEvaluator = new Creaking.HomeNodeEvaluator(Creaking.this);
/* 613 */       this.nodeEvaluator.setCanPassDoors(true);
/* 614 */       return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\creaking\Creaking.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */