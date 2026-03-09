/*     */ package net.minecraft.world.entity.animal.dolphin;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.StructureTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BreathAirGoal;
/*     */ import net.minecraft.world.entity.ai.goal.DolphinJumpGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowBoatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.entity.animal.AgeableWaterCreature;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Dolphin
/*     */   extends AgeableWaterCreature
/*     */ {
/*  71 */   private static final EntityDataAccessor<Boolean> GOT_FISH = SynchedEntityData.defineId(Dolphin.class, EntityDataSerializers.BOOLEAN);
/*  72 */   private static final EntityDataAccessor<Integer> MOISTNESS_LEVEL = SynchedEntityData.defineId(Dolphin.class, EntityDataSerializers.INT);
/*     */   
/*  74 */   private static final TargetingConditions SWIM_WITH_PLAYER_TARGETING = TargetingConditions.forNonCombat().range(10.0D).ignoreLineOfSight();
/*     */   
/*     */   public static final int TOTAL_AIR_SUPPLY = 4800;
/*     */   private static final int TOTAL_MOISTNESS_LEVEL = 2400;
/*  78 */   public static final Predicate<ItemEntity> ALLOWED_ITEMS = e -> (!e.hasPickUpDelay() && e.isAlive() && e.isInWater());
/*     */   
/*     */   public static final float BABY_SCALE = 0.65F;
/*     */   
/*     */   private static final boolean DEFAULT_GOT_FISH = false;
/*     */   private BlockPos treasurePos;
/*     */   
/*     */   public Dolphin(EntityType<? extends Dolphin> type, Level level) {
/*  86 */     super(type, level);
/*     */     
/*  88 */     this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
/*  89 */     this.lookControl = new SmoothSwimmingLookControl(this, 10);
/*     */     
/*  91 */     setCanPickUpLoot(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*  96 */     setAirSupply(getMaxAirSupply());
/*  97 */     setXRot(0.0F);
/*     */     
/*  99 */     SpawnGroupData spawnGroupData = (SpawnGroupData)Objects.requireNonNullElseGet(groupData, () -> new AgeableMob.AgeableMobGroupData(0.1F));
/*     */     
/* 101 */     return super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 106 */   public Dolphin getBreedOffspring(ServerLevel level, AgeableMob partner) { return (Dolphin)EntityType.DOLPHIN.create(level, EntitySpawnReason.BREEDING); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   public float getAgeScale() { return isBaby() ? 0.65F : 1.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleAirSupply(int preTickAirSupply) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public boolean gotFish() { return ((Boolean)this.entityData.get(GOT_FISH)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 125 */   public void setGotFish(boolean gotFish) { this.entityData.set(GOT_FISH, Boolean.valueOf(gotFish)); }
/*     */ 
/*     */ 
/*     */   
/* 129 */   public int getMoistnessLevel() { return ((Integer)this.entityData.get(MOISTNESS_LEVEL)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 133 */   public void setMoisntessLevel(int level) { this.entityData.set(MOISTNESS_LEVEL, Integer.valueOf(level)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 138 */     super.defineSynchedData(entityData);
/* 139 */     entityData.define(GOT_FISH, Boolean.valueOf(false));
/* 140 */     entityData.define(MOISTNESS_LEVEL, Integer.valueOf(2400));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 145 */     super.addAdditionalSaveData(output);
/*     */     
/* 147 */     output.putBoolean("GotFish", gotFish());
/* 148 */     output.putInt("Moistness", getMoistnessLevel());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 153 */     super.readAdditionalSaveData(input);
/* 154 */     setGotFish(input.getBooleanOr("GotFish", false));
/* 155 */     setMoisntessLevel(input.getIntOr("Moistness", 2400));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 160 */     this.goalSelector.addGoal(0, new BreathAirGoal(this));
/* 161 */     this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
/* 162 */     this.goalSelector.addGoal(1, new DolphinSwimToTreasureGoal(this));
/* 163 */     this.goalSelector.addGoal(2, new DolphinSwimWithPlayerGoal(this, 4.0D));
/* 164 */     this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
/* 165 */     this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
/* 166 */     this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
/* 167 */     this.goalSelector.addGoal(5, new DolphinJumpGoal(this, 10));
/* 168 */     this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.2000000476837158D, true));
/* 169 */     this.goalSelector.addGoal(8, new PlayWithItemsGoal());
/* 170 */     this.goalSelector.addGoal(8, new FollowBoatGoal(this));
/* 171 */     this.goalSelector.addGoal(9, new AvoidEntityGoal(this, net.minecraft.world.entity.monster.Guardian.class, 8.0F, 1.0D, 1.0D));
/*     */     
/* 173 */     this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[] { net.minecraft.world.entity.monster.Guardian.class })).setAlertOthers(new Class[0]));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 177 */     return Mob.createMobAttributes()
/* 178 */       .add(Attributes.MAX_HEALTH, 10.0D)
/* 179 */       .add(Attributes.MOVEMENT_SPEED, 1.2000000476837158D)
/* 180 */       .add(Attributes.ATTACK_DAMAGE, 3.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 185 */   protected PathNavigation createNavigation(Level level) { return new WaterBoundPathNavigation(this, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 190 */   public void playAttackSound() { playSound(SoundEvents.DOLPHIN_ATTACK, 1.0F, 1.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 195 */   public boolean canAttack(LivingEntity target) { return (!isBaby() && super.canAttack(target)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 200 */   public int getMaxAirSupply() { return 4800; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 205 */   protected int increaseAirSupply(int currentSupply) { return getMaxAirSupply(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 210 */   public int getMaxHeadXRot() { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 215 */   public int getMaxHeadYRot() { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 220 */   protected boolean canRide(Entity vehicle) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 225 */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) { return (slot == EquipmentSlot.MAINHAND && canPickUpLoot()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/* 230 */     if (getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
/* 231 */       ItemStack itemStack = entity.getItem();
/* 232 */       if (canHoldItem(itemStack)) {
/* 233 */         onItemPickup(entity);
/* 234 */         setItemSlot(EquipmentSlot.MAINHAND, itemStack);
/* 235 */         setGuaranteedDrop(EquipmentSlot.MAINHAND);
/* 236 */         take(entity, itemStack.getCount());
/* 237 */         entity.discard();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 244 */     super.tick();
/*     */     
/* 246 */     if (isNoAi()) {
/*     */       
/* 248 */       setAirSupply(getMaxAirSupply());
/*     */       
/*     */       return;
/*     */     } 
/* 252 */     if (isInWaterOrRain()) {
/* 253 */       setMoisntessLevel(2400);
/*     */     } else {
/* 255 */       setMoisntessLevel(getMoistnessLevel() - 1);
/*     */       
/* 257 */       if (getMoistnessLevel() <= 0) {
/* 258 */         hurt(damageSources().dryOut(), 1.0F);
/*     */       }
/*     */       
/* 261 */       if (onGround()) {
/* 262 */         setDeltaMovement(getDeltaMovement().add(((this.random
/* 263 */               .nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5D, ((this.random
/*     */               
/* 265 */               .nextFloat() * 2.0F - 1.0F) * 0.2F)));
/*     */         
/* 267 */         setYRot(this.random.nextFloat() * 360.0F);
/* 268 */         setOnGround(false);
/* 269 */         this.needsSync = true;
/*     */       } 
/*     */     } 
/*     */     
/* 273 */     if (level().isClientSide() && isInWater() && getDeltaMovement().lengthSqr() > 0.03D) {
/* 274 */       Vec3 viewVector = getViewVector(0.0F);
/* 275 */       float c = Mth.cos((getYRot() * 0.017453292F)) * 0.3F;
/* 276 */       float s = Mth.sin((getYRot() * 0.017453292F)) * 0.3F;
/* 277 */       float multiplier = 1.2F - this.random.nextFloat() * 0.7F;
/* 278 */       for (int i = 0; i < 2; i++) {
/* 279 */         level().addParticle(ParticleTypes.DOLPHIN, getX() - viewVector.x * multiplier + c, getY() - viewVector.y, getZ() - viewVector.z * multiplier + s, 0.0D, 0.0D, 0.0D);
/* 280 */         level().addParticle(ParticleTypes.DOLPHIN, getX() - viewVector.x * multiplier - c, getY() - viewVector.y, getZ() - viewVector.z * multiplier - s, 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 287 */     if (id == 38) {
/* 288 */       addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
/*     */     } else {
/* 290 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addParticlesAroundSelf(ParticleOptions particle) {
/* 295 */     for (int i = 0; i < 7; i++) {
/* 296 */       double xa = this.random.nextGaussian() * 0.01D;
/* 297 */       double ya = this.random.nextGaussian() * 0.01D;
/* 298 */       double za = this.random.nextGaussian() * 0.01D;
/* 299 */       level().addParticle(particle, getRandomX(1.0D), getRandomY() + 0.2D, getRandomZ(1.0D), xa, ya, za);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 305 */     ItemStack itemStack = player.getItemInHand(hand);
/*     */     
/* 307 */     if (!itemStack.isEmpty() && itemStack.is(ItemTags.FISHES)) {
/* 308 */       if (!level().isClientSide()) {
/* 309 */         playSound(SoundEvents.DOLPHIN_EAT, 1.0F, 1.0F);
/*     */       }
/*     */       
/* 312 */       if (isBaby()) {
/* 313 */         itemStack.consume(1, player);
/*     */         
/* 315 */         ageUp(getSpeedUpSecondsWhenFeeding(-this.age), true);
/*     */       } else {
/* 317 */         setGotFish(true);
/*     */         
/* 319 */         itemStack.consume(1, player);
/*     */       } 
/*     */       
/* 322 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 325 */     return super.mobInteract(player, hand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 330 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.DOLPHIN_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 335 */   protected SoundEvent getDeathSound() { return SoundEvents.DOLPHIN_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 340 */   protected SoundEvent getAmbientSound() { return isInWater() ? SoundEvents.DOLPHIN_AMBIENT_WATER : SoundEvents.DOLPHIN_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 345 */   protected SoundEvent getSwimSplashSound() { return SoundEvents.DOLPHIN_SPLASH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 350 */   protected SoundEvent getSwimSound() { return SoundEvents.DOLPHIN_SWIM; }
/*     */ 
/*     */   
/*     */   protected boolean closeToNextPos() {
/* 354 */     BlockPos target = getNavigation().getTargetPos();
/* 355 */     if (target != null) {
/* 356 */       return target.closerToCenterThan(position(), 12.0D);
/*     */     }
/* 358 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 363 */     moveRelative(getSpeed(), input);
/* 364 */     move(MoverType.SELF, getDeltaMovement());
/*     */     
/* 366 */     setDeltaMovement(getDeltaMovement().scale(0.9D));
/*     */     
/* 368 */     if (getTarget() == null) {
/* 369 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.005D, 0.0D));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 375 */   public boolean canBeLeashed() { return true; }
/*     */   
/*     */   private class PlayWithItemsGoal
/*     */     extends Goal
/*     */   {
/*     */     private int cooldown;
/*     */     
/*     */     public boolean canUse() {
/* 383 */       if (this.cooldown > Dolphin.this.tickCount) {
/* 384 */         return false;
/*     */       }
/* 386 */       List<ItemEntity> items = Dolphin.this.level().getEntitiesOfClass(ItemEntity.class, Dolphin.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Dolphin.ALLOWED_ITEMS);
/* 387 */       return (!items.isEmpty() || !Dolphin.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty());
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 392 */       List<ItemEntity> items = Dolphin.this.level().getEntitiesOfClass(ItemEntity.class, Dolphin.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Dolphin.ALLOWED_ITEMS);
/* 393 */       if (!items.isEmpty()) {
/* 394 */         Dolphin.this.getNavigation().moveTo((Entity)items.get(0), 1.2000000476837158D);
/* 395 */         Dolphin.this.playSound(SoundEvents.DOLPHIN_PLAY, 1.0F, 1.0F);
/*     */       } 
/* 397 */       this.cooldown = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 402 */       ItemStack itemStack = Dolphin.this.getItemBySlot(EquipmentSlot.MAINHAND);
/* 403 */       if (!itemStack.isEmpty()) {
/* 404 */         drop(itemStack);
/* 405 */         Dolphin.this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 406 */         this.cooldown = Dolphin.this.tickCount + Dolphin.this.random.nextInt(100);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 412 */       List<ItemEntity> items = Dolphin.this.level().getEntitiesOfClass(ItemEntity.class, Dolphin.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Dolphin.ALLOWED_ITEMS);
/*     */       
/* 414 */       ItemStack itemStack = Dolphin.this.getItemBySlot(EquipmentSlot.MAINHAND);
/* 415 */       if (!itemStack.isEmpty()) {
/* 416 */         drop(itemStack);
/* 417 */         Dolphin.this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 418 */       } else if (!items.isEmpty()) {
/* 419 */         Dolphin.this.getNavigation().moveTo((Entity)items.get(0), 1.2000000476837158D);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void drop(ItemStack itemStack) {
/* 424 */       if (itemStack.isEmpty()) {
/*     */         return;
/*     */       }
/*     */       
/* 428 */       double yHandPos = Dolphin.this.getEyeY() - 0.30000001192092896D;
/* 429 */       ItemEntity thrownItem = new ItemEntity(Dolphin.this.level(), Dolphin.this.getX(), yHandPos, Dolphin.this.getZ(), itemStack);
/* 430 */       thrownItem.setPickUpDelay(40);
/*     */       
/* 432 */       thrownItem.setThrower(Dolphin.this);
/*     */       
/* 434 */       float pow = 0.3F;
/* 435 */       float dir = Dolphin.this.random.nextFloat() * 6.2831855F;
/* 436 */       float pow2 = 0.02F * Dolphin.this.random.nextFloat();
/* 437 */       thrownItem.setDeltaMovement((0.3F * 
/* 438 */           -Mth.sin((Dolphin.this.getYRot() * 0.017453292F)) * Mth.cos((Dolphin.this.getXRot() * 0.017453292F)) + Mth.cos(dir) * pow2), (0.3F * 
/* 439 */           Mth.sin((Dolphin.this.getXRot() * 0.017453292F)) * 1.5F), (0.3F * 
/* 440 */           Mth.cos((Dolphin.this.getYRot() * 0.017453292F)) * Mth.cos((Dolphin.this.getXRot() * 0.017453292F)) + Mth.sin(dir) * pow2));
/*     */ 
/*     */       
/* 443 */       Dolphin.this.level().addFreshEntity(thrownItem);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DolphinSwimWithPlayerGoal extends Goal {
/*     */     private final Dolphin dolphin;
/*     */     private final double speedModifier;
/*     */     private Player player;
/*     */     
/*     */     DolphinSwimWithPlayerGoal(Dolphin dolphin, double speedModifier) {
/* 453 */       this.dolphin = dolphin;
/* 454 */       this.speedModifier = speedModifier;
/* 455 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 460 */       this.player = getServerLevel(this.dolphin).getNearestPlayer(Dolphin.SWIM_WITH_PLAYER_TARGETING, this.dolphin);
/* 461 */       if (this.player == null) {
/* 462 */         return false;
/*     */       }
/* 464 */       return (this.player.isSwimming() && this.dolphin.getTarget() != this.player);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 469 */     public boolean canContinueToUse() { return (this.player != null && this.player.isSwimming() && this.dolphin.distanceToSqr(this.player) < 256.0D); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 474 */     public void start() { this.player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100), this.dolphin); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void stop() {
/* 479 */       this.player = null;
/* 480 */       this.dolphin.getNavigation().stop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 485 */       this.dolphin.getLookControl().setLookAt(this.player, (this.dolphin.getMaxHeadYRot() + 20), this.dolphin.getMaxHeadXRot());
/* 486 */       if (this.dolphin.distanceToSqr(this.player) < 6.25D) {
/* 487 */         this.dolphin.getNavigation().stop();
/*     */       } else {
/* 489 */         this.dolphin.getNavigation().moveTo(this.player, this.speedModifier);
/*     */       } 
/*     */       
/* 492 */       if (this.player.isSwimming() && (this.player.level()).random.nextInt(6) == 0)
/* 493 */         this.player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100), this.dolphin); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DolphinSwimToTreasureGoal
/*     */     extends Goal {
/*     */     private final Dolphin dolphin;
/*     */     private boolean stuck;
/*     */     
/*     */     DolphinSwimToTreasureGoal(Dolphin dolphin) {
/* 503 */       this.dolphin = dolphin;
/* 504 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 509 */     public boolean isInterruptable() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 514 */     public boolean canUse() { return (this.dolphin.gotFish() && this.dolphin.getAirSupply() >= 100); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 519 */       BlockPos treasurePos = this.dolphin.treasurePos;
/* 520 */       if (treasurePos == null) {
/* 521 */         return false;
/*     */       }
/* 523 */       return (!BlockPos.containing(treasurePos.getX(), this.dolphin.getY(), treasurePos.getZ()).closerToCenterThan(this.dolphin.position(), 4.0D) && !this.stuck && this.dolphin.getAirSupply() >= 100);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 528 */       if (!(this.dolphin.level() instanceof ServerLevel)) {
/*     */         return;
/*     */       }
/* 531 */       ServerLevel level = (ServerLevel)this.dolphin.level();
/* 532 */       this.stuck = false;
/* 533 */       this.dolphin.getNavigation().stop();
/*     */       
/* 535 */       BlockPos dolphinPos = this.dolphin.blockPosition();
/*     */       
/* 537 */       BlockPos treasurePos = level.findNearestMapStructure(StructureTags.DOLPHIN_LOCATED, dolphinPos, 50, false);
/* 538 */       if (treasurePos != null) {
/* 539 */         this.dolphin.treasurePos = treasurePos;
/*     */       } else {
/*     */         
/* 542 */         this.stuck = true;
/*     */         
/*     */         return;
/*     */       } 
/* 546 */       level.broadcastEntityEvent(this.dolphin, (byte)38);
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 551 */       BlockPos treasurePos = this.dolphin.treasurePos;
/* 552 */       if (treasurePos == null || BlockPos.containing(treasurePos.getX(), this.dolphin.getY(), treasurePos.getZ()).closerToCenterThan(this.dolphin.position(), 4.0D) || this.stuck) {
/* 553 */         this.dolphin.setGotFish(false);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 559 */       if (this.dolphin.treasurePos == null) {
/*     */         return;
/*     */       }
/*     */       
/* 563 */       Level level = this.dolphin.level();
/*     */       
/* 565 */       if (this.dolphin.closeToNextPos() || this.dolphin.getNavigation().isDone()) {
/* 566 */         Vec3 treasurePos = Vec3.atCenterOf(this.dolphin.treasurePos);
/* 567 */         Vec3 nextPos = DefaultRandomPos.getPosTowards(this.dolphin, 16, 1, treasurePos, 0.39269909262657166D);
/* 568 */         if (nextPos == null) {
/* 569 */           nextPos = DefaultRandomPos.getPosTowards(this.dolphin, 8, 4, treasurePos, 1.5707963705062866D);
/*     */         }
/*     */         
/* 572 */         if (nextPos != null) {
/* 573 */           BlockPos next = BlockPos.containing(nextPos);
/* 574 */           if (!level.getFluidState(next).is(FluidTags.WATER) || !level.getBlockState(next).isPathfindable(PathComputationType.WATER)) {
/* 575 */             nextPos = DefaultRandomPos.getPosTowards(this.dolphin, 8, 5, treasurePos, 1.5707963705062866D);
/*     */           }
/*     */         } 
/*     */         
/* 579 */         if (nextPos == null) {
/* 580 */           this.stuck = true;
/*     */           
/*     */           return;
/*     */         } 
/* 584 */         this.dolphin.getLookControl().setLookAt(nextPos.x, nextPos.y, nextPos.z, (this.dolphin.getMaxHeadYRot() + 20), this.dolphin.getMaxHeadXRot());
/* 585 */         this.dolphin.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, 1.3D);
/*     */         
/* 587 */         if (level.random.nextInt(adjustedTickDelay(80)) == 0)
/* 588 */           level.broadcastEntityEvent(this.dolphin, (byte)38); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\dolphin\Dolphin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */