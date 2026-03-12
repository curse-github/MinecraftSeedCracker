/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.ConversionType;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ 
/*     */ 
/*     */ public class Slime
/*     */   extends Mob
/*     */   implements Enemy
/*     */ {
/*  55 */   private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);
/*     */   
/*     */   public static final int MIN_SIZE = 1;
/*     */   public static final int MAX_SIZE = 127;
/*     */   public static final int MAX_NATURAL_SIZE = 4;
/*     */   private static final boolean DEFAULT_WAS_ON_GROUND = false;
/*     */   public float targetSquish;
/*     */   public float squish;
/*     */   public float oSquish;
/*     */   private boolean wasOnGround = false;
/*     */   
/*     */   public Slime(EntityType<? extends Slime> type, Level level) {
/*  67 */     super(type, level);
/*     */ 
/*     */     
/*  70 */     fixupDimensions();
/*  71 */     this.moveControl = new SlimeMoveControl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  76 */     this.goalSelector.addGoal(1, new SlimeFloatGoal(this));
/*     */     
/*  78 */     this.goalSelector.addGoal(2, new SlimeAttackGoal(this));
/*  79 */     this.goalSelector.addGoal(3, new SlimeRandomDirectionGoal(this));
/*     */     
/*  81 */     this.goalSelector.addGoal(5, new SlimeKeepOnJumpingGoal(this));
/*     */ 
/*     */     
/*  84 */     this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, (target, level) -> (Math.abs(target.getY() - getY()) <= 4.0D)));
/*  85 */     this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.animal.golem.IronGolem.class, true));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  90 */   public SoundSource getSoundSource() { return SoundSource.HOSTILE; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  95 */     super.defineSynchedData(entityData);
/*     */     
/*  97 */     entityData.define(ID_SIZE, Integer.valueOf(1));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setSize(int size, boolean updateHealth) {
/* 102 */     int actualSize = Mth.clamp(size, 1, 127);
/* 103 */     this.entityData.set(ID_SIZE, Integer.valueOf(actualSize));
/* 104 */     reapplyPosition();
/*     */     
/* 106 */     refreshDimensions();
/*     */     
/* 108 */     getAttribute(Attributes.MAX_HEALTH).setBaseValue((actualSize * actualSize));
/* 109 */     getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((0.2F + 0.1F * actualSize));
/* 110 */     getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(actualSize);
/* 111 */     if (updateHealth) {
/* 112 */       setHealth(getMaxHealth());
/*     */     }
/* 114 */     this.xpReward = actualSize;
/*     */   }
/*     */ 
/*     */   
/* 118 */   public int getSize() { return ((Integer)this.entityData.get(ID_SIZE)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 123 */     super.addAdditionalSaveData(output);
/* 124 */     output.putInt("Size", getSize() - 1);
/* 125 */     output.putBoolean("wasOnGround", this.wasOnGround);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 130 */     setSize(input.getIntOr("Size", 0) + 1, false);
/* 131 */     super.readAdditionalSaveData(input);
/* 132 */     this.wasOnGround = input.getBooleanOr("wasOnGround", false);
/*     */   }
/*     */ 
/*     */   
/* 136 */   public boolean isTiny() { return (getSize() <= 1); }
/*     */ 
/*     */ 
/*     */   
/* 140 */   protected ParticleOptions getParticleType() { return ParticleTypes.ITEM_SLIME; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 145 */     this.oSquish = this.squish;
/* 146 */     this.squish += (this.targetSquish - this.squish) * 0.5F;
/* 147 */     super.tick();
/*     */     
/* 149 */     if (onGround() && !this.wasOnGround) {
/* 150 */       float size = getDimensions(getPose()).width() * 2.0F;
/* 151 */       float radius = size / 2.0F;
/* 152 */       for (int i = 0; i < size * 16.0F; i++) {
/* 153 */         float dir = this.random.nextFloat() * 6.2831855F;
/* 154 */         float d = this.random.nextFloat() * 0.5F + 0.5F;
/* 155 */         float xd = Mth.sin(dir) * radius * d;
/* 156 */         float zd = Mth.cos(dir) * radius * d;
/* 157 */         level().addParticle(getParticleType(), getX() + xd, getY(), getZ() + zd, 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */       
/* 160 */       playSound(getSquishSound(), getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
/* 161 */       this.targetSquish = -0.5F;
/* 162 */     } else if (!onGround() && this.wasOnGround) {
/* 163 */       this.targetSquish = 1.0F;
/*     */     } 
/* 165 */     this.wasOnGround = onGround();
/* 166 */     decreaseSquish();
/*     */   }
/*     */ 
/*     */   
/* 170 */   protected void decreaseSquish() { this.targetSquish *= 0.6F; }
/*     */ 
/*     */ 
/*     */   
/* 174 */   protected int getJumpDelay() { return this.random.nextInt(20) + 10; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void refreshDimensions() {
/* 179 */     double oldX = getX();
/* 180 */     double oldY = getY();
/* 181 */     double oldZ = getZ();
/* 182 */     super.refreshDimensions();
/* 183 */     setPos(oldX, oldY, oldZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 188 */     if (ID_SIZE.equals(accessor)) {
/* 189 */       refreshDimensions();
/* 190 */       setYRot(this.yHeadRot);
/* 191 */       this.yBodyRot = this.yHeadRot;
/*     */       
/* 193 */       if (isInWater() && 
/* 194 */         this.random.nextInt(20) == 0) {
/* 195 */         doWaterSplashEffect();
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 200 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 206 */   public EntityType<? extends Slime> getType() { return super.getType(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(Entity.RemovalReason reason) {
/* 211 */     int size = getSize();
/* 212 */     if (!level().isClientSide() && size > 1 && isDeadOrDying()) {
/* 213 */       float width = getDimensions(getPose()).width();
/* 214 */       float xzSlimeSpawnOffset = width / 2.0F;
/* 215 */       int halfSize = size / 2;
/*     */       
/* 217 */       int count = 2 + this.random.nextInt(3);
/* 218 */       PlayerTeam team = getTeam();
/* 219 */       for (int i = 0; i < count; i++) {
/* 220 */         float xd = ((i % 2) - 0.5F) * xzSlimeSpawnOffset;
/* 221 */         float zd = ((i / 2) - 0.5F) * xzSlimeSpawnOffset;
/* 222 */         convertTo(getType(), new ConversionParams(ConversionType.SPLIT_ON_DEATH, false, false, team), EntitySpawnReason.TRIGGERED, slime -> {
/* 223 */               slime.setSize(halfSize, true);
/* 224 */               slime.snapTo(getX() + xd, getY() + 0.5D, getZ() + zd, this.random.nextFloat() * 360.0F, 0.0F);
/*     */             });
/*     */       } 
/*     */     } 
/* 228 */     super.remove(reason);
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(Entity entity) {
/* 233 */     super.push(entity);
/* 234 */     if (entity instanceof net.minecraft.world.entity.animal.golem.IronGolem && isDealsDamage()) {
/* 235 */       dealDamage((LivingEntity)entity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerTouch(Player player) {
/* 241 */     if (isDealsDamage()) {
/* 242 */       dealDamage(player);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void dealDamage(LivingEntity target) {
/* 247 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/* 248 */       if (isAlive() && 
/* 249 */         isWithinMeleeAttackRange(target) && hasLineOfSight(target)) {
/* 250 */         DamageSource damageSource = damageSources().mobAttack(this);
/* 251 */         if (target.hurtServer(level, damageSource, getAttackDamage())) {
/* 252 */           playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/* 253 */           EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
/*     */         } 
/*     */       }  }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 262 */   protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) { return new Vec3(0.0D, dimensions.height() - 0.015625D * getSize() * scale, 0.0D); }
/*     */ 
/*     */ 
/*     */   
/* 266 */   protected boolean isDealsDamage() { return (!isTiny() && isEffectiveAi()); }
/*     */ 
/*     */ 
/*     */   
/* 270 */   protected float getAttackDamage() { return (float)getAttributeValue(Attributes.ATTACK_DAMAGE); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 275 */     if (isTiny()) {
/* 276 */       return SoundEvents.SLIME_HURT_SMALL;
/*     */     }
/* 278 */     return SoundEvents.SLIME_HURT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 284 */     if (isTiny()) {
/* 285 */       return SoundEvents.SLIME_DEATH_SMALL;
/*     */     }
/* 287 */     return SoundEvents.SLIME_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSquishSound() {
/* 292 */     if (isTiny()) {
/* 293 */       return SoundEvents.SLIME_SQUISH_SMALL;
/*     */     }
/* 295 */     return SoundEvents.SLIME_SQUISH;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean checkSlimeSpawnRules(EntityType<Slime> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 300 */     if (level.getDifficulty() != Difficulty.PEACEFUL) {
/* 301 */       if (EntitySpawnReason.isSpawner(spawnReason)) {
/* 302 */         return checkMobSpawnRules(type, level, spawnReason, pos, random);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 307 */       if (level.getBiome(pos).is(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS) && pos.getY() > 50 && pos.getY() < 70) {
/* 308 */         float surfaceSlimeSpawnChance = ((Float)level.environmentAttributes().getValue(EnvironmentAttributes.SURFACE_SLIME_SPAWN_CHANCE, pos)).floatValue();
/* 309 */         if (random.nextFloat() < surfaceSlimeSpawnChance && level.getMaxLocalRawBrightness(pos) <= random.nextInt(8)) {
/* 310 */           return checkMobSpawnRules(type, level, spawnReason, pos, random);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 315 */       if (!(level instanceof WorldGenLevel)) {
/* 316 */         return false;
/*     */       }
/* 318 */       ChunkPos chunkPos = new ChunkPos(pos);
/* 319 */       boolean slimeChunk = (WorldgenRandom.seedSlimeChunk(chunkPos.x, chunkPos.z, ((WorldGenLevel)level).getSeed(), 987234911L).nextInt(10) == 0);
/* 320 */       if (random.nextInt(10) == 0 && slimeChunk && pos.getY() < 40) {
/* 321 */         return checkMobSpawnRules(type, level, spawnReason, pos, random);
/*     */       }
/*     */     } 
/* 324 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 329 */   protected float getSoundVolume() { return 0.4F * getSize(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 334 */   public int getMaxHeadXRot() { return 0; }
/*     */ 
/*     */ 
/*     */   
/* 338 */   protected boolean doPlayJumpSound() { return (getSize() > 0); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void jumpFromGround() {
/* 343 */     Vec3 movement = getDeltaMovement();
/* 344 */     setDeltaMovement(movement.x, getJumpPower(), movement.z);
/* 345 */     this.needsSync = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 350 */     RandomSource random = level.getRandom();
/* 351 */     int sizeScale = random.nextInt(3);
/* 352 */     if (sizeScale < 2 && random.nextFloat() < 0.5F * difficulty.getSpecialMultiplier()) {
/* 353 */       sizeScale++;
/*     */     }
/* 355 */     int size = 1 << sizeScale;
/* 356 */     setSize(size, true);
/*     */     
/* 358 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   private static class SlimeMoveControl extends MoveControl {
/*     */     private float yRot;
/*     */     private int jumpDelay;
/*     */     private final Slime slime;
/*     */     private boolean isAggressive;
/*     */     
/*     */     public SlimeMoveControl(Slime slime) {
/* 368 */       super(slime);
/* 369 */       this.slime = slime;
/* 370 */       this.yRot = 180.0F * slime.getYRot() / 3.1415927F;
/*     */     }
/*     */     
/*     */     public void setDirection(float yRot, boolean isAggressive) {
/* 374 */       this.yRot = yRot;
/* 375 */       this.isAggressive = isAggressive;
/*     */     }
/*     */     
/*     */     public void setWantedMovement(double speedModifier) {
/* 379 */       this.speedModifier = speedModifier;
/* 380 */       this.operation = MoveControl.Operation.MOVE_TO;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 385 */       this.mob.setYRot(rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
/* 386 */       this.mob.yHeadRot = this.mob.getYRot();
/* 387 */       this.mob.yBodyRot = this.mob.getYRot();
/*     */       
/* 389 */       if (this.operation != MoveControl.Operation.MOVE_TO) {
/* 390 */         this.mob.setZza(0.0F);
/*     */         return;
/*     */       } 
/* 393 */       this.operation = MoveControl.Operation.WAIT;
/*     */       
/* 395 */       if (this.mob.onGround()) {
/* 396 */         this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
/* 397 */         if (this.jumpDelay-- <= 0) {
/* 398 */           this.jumpDelay = this.slime.getJumpDelay();
/* 399 */           if (this.isAggressive) {
/* 400 */             this.jumpDelay /= 3;
/*     */           }
/* 402 */           this.slime.getJumpControl().jump();
/* 403 */           if (this.slime.doPlayJumpSound()) {
/* 404 */             this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), this.slime.getSoundPitch());
/*     */           }
/*     */         } else {
/* 407 */           this.slime.xxa = 0.0F;
/* 408 */           this.slime.zza = 0.0F;
/* 409 */           this.mob.setSpeed(0.0F);
/*     */         } 
/*     */       } else {
/* 412 */         this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private float getSoundPitch() {
/* 418 */     float pitchAdjuster = isTiny() ? 1.4F : 0.8F;
/* 419 */     return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * pitchAdjuster;
/*     */   }
/*     */ 
/*     */   
/* 423 */   protected SoundEvent getJumpSound() { return isTiny() ? SoundEvents.SLIME_JUMP_SMALL : SoundEvents.SLIME_JUMP; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 428 */   public EntityDimensions getDefaultDimensions(Pose pose) { return super.getDefaultDimensions(pose).scale(getSize()); }
/*     */   
/*     */   private static class SlimeAttackGoal
/*     */     extends Goal {
/*     */     private final Slime slime;
/*     */     private int growTiredTimer;
/*     */     
/*     */     public SlimeAttackGoal(Slime slime) {
/* 436 */       this.slime = slime;
/* 437 */       setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 442 */       LivingEntity target = this.slime.getTarget();
/*     */       
/* 444 */       if (target == null) {
/* 445 */         return false;
/*     */       }
/*     */       
/* 448 */       if (!this.slime.canAttack(target)) {
/* 449 */         return false;
/*     */       }
/*     */       
/* 452 */       return this.slime.getMoveControl() instanceof Slime.SlimeMoveControl;
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 457 */       this.growTiredTimer = reducedTickDelay(300);
/* 458 */       super.start();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 463 */       LivingEntity target = this.slime.getTarget();
/*     */       
/* 465 */       if (target == null) {
/* 466 */         return false;
/*     */       }
/*     */       
/* 469 */       if (!this.slime.canAttack(target)) {
/* 470 */         return false;
/*     */       }
/*     */       
/* 473 */       if (--this.growTiredTimer <= 0) {
/* 474 */         return false;
/*     */       }
/*     */       
/* 477 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 482 */     public boolean requiresUpdateEveryTick() { return true; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 487 */       LivingEntity target = this.slime.getTarget();
/* 488 */       if (target != null) {
/* 489 */         this.slime.lookAt(target, 10.0F, 10.0F);
/*     */       }
/* 491 */       MoveControl moveControl = this.slime.getMoveControl(); if (moveControl instanceof Slime.SlimeMoveControl) { Slime.SlimeMoveControl slimeMoveControl = (Slime.SlimeMoveControl)moveControl;
/* 492 */         slimeMoveControl.setDirection(this.slime.getYRot(), this.slime.isDealsDamage()); }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SlimeRandomDirectionGoal
/*     */     extends Goal {
/*     */     private final Slime slime;
/*     */     private float chosenDegrees;
/*     */     private int nextRandomizeTime;
/*     */     
/*     */     public SlimeRandomDirectionGoal(Slime slime) {
/* 504 */       this.slime = slime;
/* 505 */       setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 510 */     public boolean canUse() { return (this.slime.getTarget() == null && (this.slime.onGround() || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(MobEffects.LEVITATION)) && this.slime.getMoveControl() instanceof Slime.SlimeMoveControl); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 515 */       if (--this.nextRandomizeTime <= 0) {
/* 516 */         this.nextRandomizeTime = adjustedTickDelay(40 + this.slime.getRandom().nextInt(60));
/* 517 */         this.chosenDegrees = this.slime.getRandom().nextInt(360);
/*     */       } 
/* 519 */       MoveControl moveControl = this.slime.getMoveControl(); if (moveControl instanceof Slime.SlimeMoveControl) { Slime.SlimeMoveControl slimeMoveControl = (Slime.SlimeMoveControl)moveControl;
/* 520 */         slimeMoveControl.setDirection(this.chosenDegrees, false); }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SlimeFloatGoal extends Goal {
/*     */     private final Slime slime;
/*     */     
/*     */     public SlimeFloatGoal(Slime mob) {
/* 529 */       this.slime = mob;
/* 530 */       setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/* 531 */       mob.getNavigation().setCanFloat(true);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 536 */     public boolean canUse() { return ((this.slime.isInWater() || this.slime.isInLava()) && this.slime.getMoveControl() instanceof Slime.SlimeMoveControl); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 541 */     public boolean requiresUpdateEveryTick() { return true; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 546 */       if (this.slime.getRandom().nextFloat() < 0.8F) {
/* 547 */         this.slime.getJumpControl().jump();
/*     */       }
/* 549 */       MoveControl moveControl = this.slime.getMoveControl(); if (moveControl instanceof Slime.SlimeMoveControl) { Slime.SlimeMoveControl slimeMoveControl = (Slime.SlimeMoveControl)moveControl;
/* 550 */         slimeMoveControl.setWantedMovement(1.2D); }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SlimeKeepOnJumpingGoal extends Goal {
/*     */     private final Slime slime;
/*     */     
/*     */     public SlimeKeepOnJumpingGoal(Slime mob) {
/* 559 */       this.slime = mob;
/* 560 */       setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 565 */     public boolean canUse() { return !this.slime.isPassenger(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 570 */       MoveControl moveControl = this.slime.getMoveControl(); if (moveControl instanceof Slime.SlimeMoveControl) { Slime.SlimeMoveControl slimeMoveControl = (Slime.SlimeMoveControl)moveControl;
/* 571 */         slimeMoveControl.setWantedMovement(1.0D); }
/*     */     
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Slime.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */