/*     */ package net.minecraft.world.entity.animal.turtle;
/*     */ 
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.TurtleEggBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Turtle
/*     */   extends Animal
/*     */ {
/*  68 */   private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN);
/*  69 */   private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final float BABY_SCALE = 0.3F;
/*  72 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.TURTLE.getDimensions()
/*  73 */     .withAttachments(EntityAttachments.builder()
/*  74 */       .attach(EntityAttachment.PASSENGER, 0.0F, EntityType.TURTLE.getHeight(), -0.25F))
/*     */     
/*  76 */     .scale(0.3F);
/*     */   
/*     */   private static final boolean DEFAULT_HAS_EGG = false;
/*     */   
/*     */   private int layEggCounter;
/*     */   
/*  82 */   public static final TargetingConditions.Selector BABY_ON_LAND_SELECTOR = (target, level) -> (target.isBaby() && !target.isInWater());
/*     */   
/*  84 */   private BlockPos homePos = BlockPos.ZERO;
/*     */   private BlockPos travelPos;
/*     */   private boolean goingHome;
/*     */   
/*     */   public Turtle(EntityType<? extends Turtle> type, Level level) {
/*  89 */     super(type, level);
/*     */     
/*  91 */     setPathfindingMalus(PathType.WATER, 0.0F);
/*  92 */     setPathfindingMalus(PathType.DOOR_IRON_CLOSED, -1.0F);
/*  93 */     setPathfindingMalus(PathType.DOOR_WOOD_CLOSED, -1.0F);
/*  94 */     setPathfindingMalus(PathType.DOOR_OPEN, -1.0F);
/*  95 */     this.moveControl = new TurtleMoveControl(this);
/*     */   }
/*     */ 
/*     */   
/*  99 */   public void setHomePos(BlockPos pos) { this.homePos = pos; }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public boolean hasEgg() { return ((Boolean)this.entityData.get(HAS_EGG)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   private void setHasEgg(boolean onOff) { this.entityData.set(HAS_EGG, Boolean.valueOf(onOff)); }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public boolean isLayingEgg() { return ((Boolean)this.entityData.get(LAYING_EGG)).booleanValue(); }
/*     */ 
/*     */   
/*     */   private void setLayingEgg(boolean on) {
/* 115 */     this.layEggCounter = on ? 1 : 0;
/* 116 */     this.entityData.set(LAYING_EGG, Boolean.valueOf(on));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 121 */     super.defineSynchedData(entityData);
/* 122 */     entityData.define(HAS_EGG, Boolean.valueOf(false));
/* 123 */     entityData.define(LAYING_EGG, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 128 */     super.addAdditionalSaveData(output);
/*     */     
/* 130 */     output.store("home_pos", BlockPos.CODEC, this.homePos);
/*     */     
/* 132 */     output.putBoolean("has_egg", hasEgg());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 137 */     setHomePos((BlockPos)input.read("home_pos", BlockPos.CODEC).orElse(blockPosition()));
/*     */     
/* 139 */     super.readAdditionalSaveData(input);
/* 140 */     setHasEgg(input.getBooleanOr("has_egg", false));
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 145 */     setHomePos(blockPosition());
/* 146 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public static boolean checkTurtleSpawnRules(EntityType<Turtle> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 150 */     return (pos.getY() < level.getSeaLevel() + 4 && 
/* 151 */       TurtleEggBlock.onSand(level, pos) && 
/* 152 */       isBrightEnoughToSpawn(level, pos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 157 */     this.goalSelector.addGoal(0, new TurtlePanicGoal(this, 1.2D));
/* 158 */     this.goalSelector.addGoal(1, new TurtleBreedGoal(this, 1.0D));
/* 159 */     this.goalSelector.addGoal(1, new TurtleLayEggGoal(this, 1.0D));
/* 160 */     this.goalSelector.addGoal(2, new TemptGoal(this, 1.1D, i -> i.is(ItemTags.TURTLE_FOOD), false));
/* 161 */     this.goalSelector.addGoal(3, new TurtleGoToWaterGoal(this, 1.0D));
/* 162 */     this.goalSelector.addGoal(4, new TurtleGoHomeGoal(this, 1.0D));
/* 163 */     this.goalSelector.addGoal(7, new TurtleTravelGoal(this, 1.0D));
/* 164 */     this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 8.0F));
/* 165 */     this.goalSelector.addGoal(9, new TurtleRandomStrollGoal(this, 1.0D, 100));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 169 */     return Animal.createAnimalAttributes()
/* 170 */       .add(Attributes.MAX_HEALTH, 30.0D)
/* 171 */       .add(Attributes.MOVEMENT_SPEED, 0.25D)
/* 172 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 177 */   public boolean isPushedByFluid() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   public int getAmbientSoundInterval() { return 200; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 187 */     if (!isInWater() && onGround() && !isBaby()) {
/* 188 */       return SoundEvents.TURTLE_AMBIENT_LAND;
/*     */     }
/*     */     
/* 191 */     return super.getAmbientSound();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 196 */   protected void playSwimSound(float volume) { super.playSwimSound(volume * 1.5F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 201 */   protected SoundEvent getSwimSound() { return SoundEvents.TURTLE_SWIM; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 206 */     if (isBaby()) {
/* 207 */       return SoundEvents.TURTLE_HURT_BABY;
/*     */     }
/* 209 */     return SoundEvents.TURTLE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 214 */     if (isBaby()) {
/* 215 */       return SoundEvents.TURTLE_DEATH_BABY;
/*     */     }
/* 217 */     return SoundEvents.TURTLE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 222 */     SoundEvent sound = isBaby() ? SoundEvents.TURTLE_SHAMBLE_BABY : SoundEvents.TURTLE_SHAMBLE;
/*     */     
/* 224 */     playSound(sound, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 229 */   public boolean canFallInLove() { return (super.canFallInLove() && !hasEgg()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 234 */   protected float nextStep() { return this.moveDist + 0.15F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 239 */   public float getAgeScale() { return isBaby() ? 0.3F : 1.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 244 */   protected PathNavigation createNavigation(Level level) { return new TurtlePathNavigation(this, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 249 */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) { return (AgeableMob)EntityType.TURTLE.create(level, EntitySpawnReason.BREEDING); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 254 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.TURTLE_FOOD); }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/* 259 */     if (!this.goingHome && level.getFluidState(pos).is(FluidTags.WATER)) {
/* 260 */       return 10.0F;
/*     */     }
/*     */     
/* 263 */     if (TurtleEggBlock.onSand(level, pos)) {
/* 264 */       return 10.0F;
/*     */     }
/*     */     
/* 267 */     return level.getPathfindingCostFromLightLevels(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 272 */     super.aiStep();
/*     */     
/* 274 */     if (isAlive() && isLayingEgg() && this.layEggCounter >= 1 && this.layEggCounter % 5 == 0) {
/* 275 */       BlockPos pos = blockPosition();
/* 276 */       if (TurtleEggBlock.onSand(level(), pos)) {
/* 277 */         level().levelEvent(2001, pos, Block.getId(level().getBlockState(pos.below())));
/* 278 */         gameEvent(GameEvent.ENTITY_ACTION);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void ageBoundaryReached() {
/* 285 */     super.ageBoundaryReached();
/*     */     
/* 287 */     if (!isBaby()) { Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1; if (((Boolean)level.getGameRules().get(GameRules.MOB_DROPS)).booleanValue())
/* 288 */           dropFromGiftLootTable(level, BuiltInLootTables.TURTLE_GROW, this::spawnAtLocation);  }
/*     */        }
/*     */   
/*     */   }
/*     */   
/*     */   protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 294 */     moveRelative(0.1F, input);
/* 295 */     move(MoverType.SELF, getDeltaMovement());
/*     */     
/* 297 */     setDeltaMovement(getDeltaMovement().scale(0.9D));
/* 298 */     if (getTarget() == null && (!this.goingHome || !this.homePos.closerToCenterThan(position(), 20.0D))) {
/* 299 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.005D, 0.0D));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 305 */   public boolean canBeLeashed() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 310 */   public void thunderHit(ServerLevel level, LightningBolt lightningBolt) { hurtServer(level, damageSources().lightningBolt(), Float.MAX_VALUE); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 315 */   public EntityDimensions getDefaultDimensions(Pose pose) { return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose); }
/*     */   
/*     */   private static class TurtlePanicGoal
/*     */     extends PanicGoal
/*     */   {
/* 320 */     TurtlePanicGoal(Turtle turtle, double speedModifier) { super(turtle, speedModifier); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 325 */       if (!shouldPanic()) {
/* 326 */         return false;
/*     */       }
/*     */       
/* 329 */       BlockPos blockPos = lookForWater(this.mob.level(), this.mob, 7);
/* 330 */       if (blockPos != null) {
/* 331 */         this.posX = blockPos.getX();
/* 332 */         this.posY = blockPos.getY();
/* 333 */         this.posZ = blockPos.getZ();
/*     */         
/* 335 */         return true;
/*     */       } 
/*     */       
/* 338 */       return findRandomPosition();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleTravelGoal extends Goal {
/*     */     private final Turtle turtle;
/*     */     private final double speedModifier;
/*     */     private boolean stuck;
/*     */     
/*     */     TurtleTravelGoal(Turtle turtle, double speedModifier) {
/* 348 */       this.turtle = turtle;
/* 349 */       this.speedModifier = speedModifier;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 354 */     public boolean canUse() { return (!this.turtle.goingHome && !this.turtle.hasEgg() && this.turtle.isInWater()); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() {
/* 359 */       int xzDist = 512;
/* 360 */       int yDist = 4;
/* 361 */       RandomSource random = this.turtle.random;
/* 362 */       int xt = random.nextInt(1025) - 512;
/* 363 */       int yt = random.nextInt(9) - 4;
/* 364 */       int zt = random.nextInt(1025) - 512;
/*     */       
/* 366 */       if (yt + this.turtle.getY() > (this.turtle.level().getSeaLevel() - 1)) {
/* 367 */         yt = 0;
/*     */       }
/* 369 */       this.turtle.travelPos = BlockPos.containing(xt + this.turtle.getX(), yt + this.turtle.getY(), zt + this.turtle.getZ());
/* 370 */       this.stuck = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 375 */       if (this.turtle.travelPos == null) {
/* 376 */         this.stuck = true;
/*     */         
/*     */         return;
/*     */       } 
/* 380 */       if (this.turtle.getNavigation().isDone()) {
/* 381 */         Vec3 targetPos = Vec3.atBottomCenterOf(this.turtle.travelPos);
/* 382 */         Vec3 nextPos = DefaultRandomPos.getPosTowards(this.turtle, 16, 3, targetPos, 0.3141592741012573D);
/* 383 */         if (nextPos == null) {
/* 384 */           nextPos = DefaultRandomPos.getPosTowards(this.turtle, 8, 7, targetPos, 1.5707963705062866D);
/*     */         }
/*     */ 
/*     */         
/* 388 */         if (nextPos != null) {
/* 389 */           int xc = Mth.floor(nextPos.x);
/* 390 */           int zc = Mth.floor(nextPos.z);
/* 391 */           int r = 34;
/* 392 */           if (!this.turtle.level().hasChunksAt(xc - 34, zc - 34, xc + 34, zc + 34)) {
/* 393 */             nextPos = null;
/*     */           }
/*     */         } 
/*     */         
/* 397 */         if (nextPos == null) {
/* 398 */           this.stuck = true;
/*     */           
/*     */           return;
/*     */         } 
/* 402 */         this.turtle.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 408 */     public boolean canContinueToUse() { return (!this.turtle.getNavigation().isDone() && !this.stuck && !this.turtle.goingHome && !this.turtle.isInLove() && !this.turtle.hasEgg()); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void stop() {
/* 413 */       this.turtle.travelPos = null;
/* 414 */       super.stop();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleGoHomeGoal extends Goal {
/*     */     private final Turtle turtle;
/*     */     private final double speedModifier;
/*     */     private boolean stuck;
/*     */     private int closeToHomeTryTicks;
/*     */     private static final int GIVE_UP_TICKS = 600;
/*     */     
/*     */     TurtleGoHomeGoal(Turtle turtle, double speedModifier) {
/* 426 */       this.turtle = turtle;
/* 427 */       this.speedModifier = speedModifier;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 432 */       if (this.turtle.isBaby()) {
/* 433 */         return false;
/*     */       }
/*     */       
/* 436 */       if (this.turtle.hasEgg()) {
/* 437 */         return true;
/*     */       }
/*     */       
/* 440 */       if (this.turtle.getRandom().nextInt(reducedTickDelay(700)) != 0) {
/* 441 */         return false;
/*     */       }
/*     */       
/* 444 */       return !this.turtle.homePos.closerToCenterThan(this.turtle.position(), 64.0D);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 449 */       this.turtle.goingHome = true;
/* 450 */       this.stuck = false;
/* 451 */       this.closeToHomeTryTicks = 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 456 */     public void stop() { this.turtle.goingHome = false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 461 */     public boolean canContinueToUse() { return (!this.turtle.homePos.closerToCenterThan(this.turtle.position(), 7.0D) && !this.stuck && this.closeToHomeTryTicks <= adjustedTickDelay(600)); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 466 */       BlockPos homePos = this.turtle.homePos;
/* 467 */       boolean closeToHome = homePos.closerToCenterThan(this.turtle.position(), 16.0D);
/* 468 */       if (closeToHome) {
/* 469 */         this.closeToHomeTryTicks++;
/*     */       }
/*     */       
/* 472 */       if (this.turtle.getNavigation().isDone()) {
/* 473 */         Vec3 homePosVec = Vec3.atBottomCenterOf(homePos);
/* 474 */         Vec3 nextPos = DefaultRandomPos.getPosTowards(this.turtle, 16, 3, homePosVec, 0.3141592741012573D);
/* 475 */         if (nextPos == null) {
/* 476 */           nextPos = DefaultRandomPos.getPosTowards(this.turtle, 8, 7, homePosVec, 1.5707963705062866D);
/*     */         }
/*     */         
/* 479 */         if (nextPos != null && !closeToHome && !this.turtle.level().getBlockState(BlockPos.containing(nextPos)).is(Blocks.WATER))
/*     */         {
/* 481 */           nextPos = DefaultRandomPos.getPosTowards(this.turtle, 16, 5, homePosVec, 1.5707963705062866D);
/*     */         }
/*     */         
/* 484 */         if (nextPos == null) {
/* 485 */           this.stuck = true;
/*     */           
/*     */           return;
/*     */         } 
/* 489 */         this.turtle.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleBreedGoal extends BreedGoal {
/*     */     private final Turtle turtle;
/*     */     
/*     */     TurtleBreedGoal(Turtle turtle, double speedModifier) {
/* 498 */       super(turtle, speedModifier);
/* 499 */       this.turtle = turtle;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 504 */     public boolean canUse() { return (super.canUse() && !this.turtle.hasEgg()); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void breed() {
/* 509 */       ServerPlayer loveCause = this.animal.getLoveCause();
/* 510 */       if (loveCause == null && this.partner.getLoveCause() != null) {
/* 511 */         loveCause = this.partner.getLoveCause();
/*     */       }
/*     */       
/* 514 */       if (loveCause != null) {
/* 515 */         loveCause.awardStat(Stats.ANIMALS_BRED);
/* 516 */         CriteriaTriggers.BRED_ANIMALS.trigger(loveCause, this.animal, this.partner, null);
/*     */       } 
/*     */       
/* 519 */       this.turtle.setHasEgg(true);
/* 520 */       this.animal.setAge(6000);
/* 521 */       this.partner.setAge(6000);
/* 522 */       this.animal.resetLove();
/* 523 */       this.partner.resetLove();
/*     */       
/* 525 */       RandomSource random = this.animal.getRandom();
/* 526 */       if (((Boolean)getServerLevel(this.level).getGameRules().get(GameRules.MOB_DROPS)).booleanValue())
/* 527 */         this.level.addFreshEntity(new ExperienceOrb(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random.nextInt(7) + 1)); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleLayEggGoal
/*     */     extends MoveToBlockGoal {
/*     */     private final Turtle turtle;
/*     */     
/*     */     TurtleLayEggGoal(Turtle turtle, double speedModifier) {
/* 536 */       super(turtle, speedModifier, 16);
/* 537 */       this.turtle = turtle;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 542 */       if (this.turtle.hasEgg() && this.turtle.homePos.closerToCenterThan(this.turtle.position(), 9.0D)) {
/* 543 */         return super.canUse();
/*     */       }
/* 545 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 550 */     public boolean canContinueToUse() { return (super.canContinueToUse() && this.turtle.hasEgg() && this.turtle.homePos.closerToCenterThan(this.turtle.position(), 9.0D)); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 555 */       super.tick();
/*     */       
/* 557 */       BlockPos turtlePos = this.turtle.blockPosition();
/* 558 */       if (!this.turtle.isInWater() && isReachedTarget()) {
/* 559 */         if (this.turtle.layEggCounter < 1) {
/* 560 */           this.turtle.setLayingEgg(true);
/* 561 */         } else if (this.turtle.layEggCounter > adjustedTickDelay(200)) {
/* 562 */           Level level = this.turtle.level();
/* 563 */           level.playSound(null, turtlePos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);
/* 564 */           BlockPos eggPos = this.blockPos.above();
/* 565 */           BlockState eggState = (BlockState)Blocks.TURTLE_EGG.defaultBlockState().setValue(TurtleEggBlock.EGGS, Integer.valueOf(this.turtle.random.nextInt(4) + 1));
/* 566 */           level.setBlock(eggPos, eggState, 3);
/* 567 */           level.gameEvent(GameEvent.BLOCK_PLACE, eggPos, GameEvent.Context.of(this.turtle, eggState));
/* 568 */           this.turtle.setHasEgg(false);
/* 569 */           this.turtle.setLayingEgg(false);
/* 570 */           this.turtle.setInLoveTime(600);
/*     */         } 
/* 572 */         if (this.turtle.isLayingEgg()) {
/* 573 */           this.turtle.layEggCounter++;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 580 */       if (!level.isEmptyBlock(pos.above())) {
/* 581 */         return false;
/*     */       }
/*     */       
/* 584 */       return TurtleEggBlock.isSand(level, pos);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleRandomStrollGoal extends RandomStrollGoal {
/*     */     private final Turtle turtle;
/*     */     
/*     */     private TurtleRandomStrollGoal(Turtle turtle, double speedModifier, int interval) {
/* 592 */       super(turtle, speedModifier, interval);
/* 593 */       this.turtle = turtle;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 598 */       if (!this.mob.isInWater() && !this.turtle.goingHome && !this.turtle.hasEgg()) {
/* 599 */         return super.canUse();
/*     */       }
/*     */       
/* 602 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleGoToWaterGoal
/*     */     extends MoveToBlockGoal {
/*     */     private static final int GIVE_UP_TICKS = 1200;
/*     */     private final Turtle turtle;
/*     */     
/*     */     private TurtleGoToWaterGoal(Turtle turtle, double speedModifier) {
/* 612 */       super(turtle, turtle.isBaby() ? 2.0D : speedModifier, 24);
/* 613 */       this.turtle = turtle;
/* 614 */       this.verticalSearchStart = -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 619 */     public boolean canContinueToUse() { return (!this.turtle.isInWater() && this.tryTicks <= 1200 && isValidTarget(this.turtle.level(), this.blockPos)); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 624 */       if (this.turtle.isBaby() && !this.turtle.isInWater()) {
/* 625 */         return super.canUse();
/*     */       }
/*     */       
/* 628 */       if (!this.turtle.goingHome && !this.turtle.isInWater() && !this.turtle.hasEgg()) {
/* 629 */         return super.canUse();
/*     */       }
/*     */       
/* 632 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 637 */     public boolean shouldRecalculatePath() { return (this.tryTicks % 160 == 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 642 */     protected boolean isValidTarget(LevelReader level, BlockPos pos) { return level.getBlockState(pos).is(Blocks.WATER); }
/*     */   }
/*     */   
/*     */   private static class TurtleMoveControl
/*     */     extends MoveControl {
/*     */     private final Turtle turtle;
/*     */     
/*     */     TurtleMoveControl(Turtle turtle) {
/* 650 */       super(turtle);
/* 651 */       this.turtle = turtle;
/*     */     }
/*     */     
/*     */     private void updateSpeed() {
/* 655 */       if (this.turtle.isInWater()) {
/*     */         
/* 657 */         this.turtle.setDeltaMovement(this.turtle.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
/*     */         
/* 659 */         if (!this.turtle.homePos.closerToCenterThan(this.turtle.position(), 16.0D)) {
/* 660 */           this.turtle.setSpeed(Math.max(this.turtle.getSpeed() / 2.0F, 0.08F));
/*     */         }
/*     */         
/* 663 */         if (this.turtle.isBaby()) {
/* 664 */           this.turtle.setSpeed(Math.max(this.turtle.getSpeed() / 3.0F, 0.06F));
/*     */         }
/* 666 */       } else if (this.turtle.onGround()) {
/* 667 */         this.turtle.setSpeed(Math.max(this.turtle.getSpeed() / 2.0F, 0.06F));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 673 */       updateSpeed();
/*     */       
/* 675 */       if (this.operation != MoveControl.Operation.MOVE_TO || this.turtle.getNavigation().isDone()) {
/* 676 */         this.turtle.setSpeed(0.0F);
/*     */         
/*     */         return;
/*     */       } 
/* 680 */       double xd = this.wantedX - this.turtle.getX();
/* 681 */       double yd = this.wantedY - this.turtle.getY();
/* 682 */       double zd = this.wantedZ - this.turtle.getZ();
/* 683 */       double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
/* 684 */       if (dd < 9.999999747378752E-6D) {
/* 685 */         this.mob.setSpeed(0.0F);
/*     */         
/*     */         return;
/*     */       } 
/* 689 */       yd /= dd;
/*     */       
/* 691 */       float yRotD = (float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F;
/* 692 */       this.turtle.setYRot(rotlerp(this.turtle.getYRot(), yRotD, 90.0F));
/* 693 */       this.turtle.yBodyRot = this.turtle.getYRot();
/*     */       
/* 695 */       float targetSpeed = (float)(this.speedModifier * this.turtle.getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 696 */       this.turtle.setSpeed(Mth.lerp(0.125F, this.turtle.getSpeed(), targetSpeed));
/*     */       
/* 698 */       this.turtle.setDeltaMovement(this.turtle.getDeltaMovement().add(0.0D, this.turtle.getSpeed() * yd * 0.1D, 0.0D));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtlePathNavigation
/*     */     extends AmphibiousPathNavigation {
/* 704 */     TurtlePathNavigation(Turtle mob, Level level) { super(mob, level); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isStableDestination(BlockPos pos) {
/* 709 */       Mob mob = this.mob; if (mob instanceof Turtle) { Turtle turtle = (Turtle)mob;
/* 710 */         if (turtle.travelPos != null) {
/* 711 */           return this.level.getBlockState(pos).is(Blocks.WATER);
/*     */         } }
/*     */ 
/*     */       
/* 715 */       return !this.level.getBlockState(pos.below()).isAir();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\turtle\Turtle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */