/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EnderMan
/*     */   extends Monster
/*     */   implements NeutralMob
/*     */ {
/*  73 */   private static final Identifier SPEED_MODIFIER_ATTACKING_ID = Identifier.withDefaultNamespace("attacking");
/*  74 */   private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_ID, 0.15000000596046448D, AttributeModifier.Operation.ADD_VALUE);
/*     */   
/*     */   private static final int DELAY_BETWEEN_CREEPY_STARE_SOUND = 400;
/*     */   private static final int MIN_DEAGGRESSION_TIME = 600;
/*  78 */   private static final EntityDataAccessor<Optional<BlockState>> DATA_CARRY_STATE = SynchedEntityData.defineId(EnderMan.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE);
/*  79 */   private static final EntityDataAccessor<Boolean> DATA_CREEPY = SynchedEntityData.defineId(EnderMan.class, EntityDataSerializers.BOOLEAN);
/*  80 */   private static final EntityDataAccessor<Boolean> DATA_STARED_AT = SynchedEntityData.defineId(EnderMan.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  82 */   private int lastStareSound = Integer.MIN_VALUE;
/*     */   
/*     */   private int targetChangeTime;
/*  85 */   private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   private long persistentAngerEndTime;
/*     */   private EntityReference<LivingEntity> persistentAngerTarget;
/*     */   
/*     */   public EnderMan(EntityType<? extends EnderMan> type, Level level) {
/*  90 */     super(type, level);
/*     */     
/*  92 */     setPathfindingMalus(PathType.WATER, -1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  97 */     this.goalSelector.addGoal(0, new FloatGoal(this));
/*  98 */     this.goalSelector.addGoal(1, new EndermanFreezeWhenLookedAt(this));
/*  99 */     this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
/* 100 */     this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
/* 101 */     this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
/* 102 */     this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
/*     */     
/* 104 */     this.goalSelector.addGoal(10, new EndermanLeaveBlockGoal(this));
/* 105 */     this.goalSelector.addGoal(11, new EndermanTakeBlockGoal(this));
/*     */     
/* 107 */     this.targetSelector.addGoal(1, new EndermanLookForPlayerGoal(this, this::isAngryAt));
/* 108 */     this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
/* 109 */     this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Endermite.class, true, false));
/* 110 */     this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal(this, false));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public float getWalkTargetValue(BlockPos pos, LevelReader level) { return 0.0F; }
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 119 */     return Monster.createMonsterAttributes()
/* 120 */       .add(Attributes.MAX_HEALTH, 40.0D)
/* 121 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 122 */       .add(Attributes.ATTACK_DAMAGE, 7.0D)
/* 123 */       .add(Attributes.FOLLOW_RANGE, 64.0D)
/* 124 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTarget(LivingEntity target) {
/* 129 */     super.setTarget(target);
/*     */     
/* 131 */     AttributeInstance movementSpeed = getAttribute(Attributes.MOVEMENT_SPEED);
/*     */     
/* 133 */     if (target == null) {
/* 134 */       this.targetChangeTime = 0;
/* 135 */       this.entityData.set(DATA_CREEPY, Boolean.valueOf(false));
/* 136 */       this.entityData.set(DATA_STARED_AT, Boolean.valueOf(false));
/*     */       
/* 138 */       movementSpeed.removeModifier(SPEED_MODIFIER_ATTACKING_ID);
/*     */     } else {
/* 140 */       this.targetChangeTime = this.tickCount;
/* 141 */       this.entityData.set(DATA_CREEPY, Boolean.valueOf(true));
/*     */       
/* 143 */       if (!movementSpeed.hasModifier(SPEED_MODIFIER_ATTACKING_ID)) {
/* 144 */         movementSpeed.addTransientModifier(SPEED_MODIFIER_ATTACKING);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 151 */     super.defineSynchedData(entityData);
/*     */     
/* 153 */     entityData.define(DATA_CARRY_STATE, Optional.empty());
/* 154 */     entityData.define(DATA_CREEPY, Boolean.valueOf(false));
/* 155 */     entityData.define(DATA_STARED_AT, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 160 */   public void startPersistentAngerTimer() { setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 165 */   public void setPersistentAngerEndTime(long endTime) { this.persistentAngerEndTime = endTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 170 */   public long getPersistentAngerEndTime() { return this.persistentAngerEndTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 175 */   public void setPersistentAngerTarget(EntityReference<LivingEntity> persistentAngerTarget) { this.persistentAngerTarget = persistentAngerTarget; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 180 */   public EntityReference<LivingEntity> getPersistentAngerTarget() { return this.persistentAngerTarget; }
/*     */ 
/*     */   
/*     */   public void playStareSound() {
/* 184 */     if (this.tickCount >= this.lastStareSound + 400) {
/* 185 */       this.lastStareSound = this.tickCount;
/* 186 */       if (!isSilent()) {
/* 187 */         level().playLocalSound(getX(), getEyeY(), getZ(), SoundEvents.ENDERMAN_STARE, getSoundSource(), 2.5F, 1.0F, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 194 */     if (DATA_CREEPY.equals(accessor) && 
/* 195 */       hasBeenStaredAt() && level().isClientSide()) {
/* 196 */       playStareSound();
/*     */     }
/*     */     
/* 199 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 204 */     super.addAdditionalSaveData(output);
/* 205 */     BlockState blockState = getCarriedBlock();
/* 206 */     if (blockState != null) {
/* 207 */       output.store("carriedBlockState", BlockState.CODEC, blockState);
/*     */     }
/* 209 */     addPersistentAngerSaveData(output);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 214 */     super.readAdditionalSaveData(input);
/*     */     
/* 216 */     setCarriedBlock((BlockState)input.read("carriedBlockState", BlockState.CODEC)
/* 217 */         .filter(blockState -> !blockState.isAir())
/* 218 */         .orElse(null));
/*     */     
/* 220 */     readPersistentAngerSaveData(level(), input);
/*     */   }
/*     */   
/*     */   private boolean isBeingStaredBy(Player player) {
/* 224 */     if (!LivingEntity.PLAYER_NOT_WEARING_DISGUISE_ITEM.test(player)) {
/* 225 */       return false;
/*     */     }
/* 227 */     return isLookingAtMe(player, 0.025D, true, false, new double[] { getEyeY() });
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 232 */     if (level().isClientSide()) {
/* 233 */       for (int i = 0; i < 2; i++) {
/* 234 */         level().addParticle(ParticleTypes.PORTAL, getRandomX(0.5D), getRandomY() - 0.25D, getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
/*     */       }
/*     */     }
/*     */     
/* 238 */     this.jumping = false;
/*     */     
/* 240 */     if (!level().isClientSide()) {
/* 241 */       updatePersistentAnger((ServerLevel)level(), true);
/*     */     }
/* 243 */     super.aiStep();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 248 */   public boolean isSensitiveToWater() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 253 */     if (level.isBrightOutside() && this.tickCount >= this.targetChangeTime + 600) {
/* 254 */       float br = getLightLevelDependentMagicValue();
/* 255 */       if (br > 0.5F && 
/* 256 */         level.canSeeSky(blockPosition()) && this.random.nextFloat() * 30.0F < (br - 0.4F) * 2.0F) {
/* 257 */         setTarget(null);
/* 258 */         teleport();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 263 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   protected boolean teleport() {
/* 267 */     if (level().isClientSide() || !isAlive()) {
/* 268 */       return false;
/*     */     }
/*     */     
/* 271 */     double xx = getX() + (this.random.nextDouble() - 0.5D) * 64.0D;
/* 272 */     double yy = getY() + (this.random.nextInt(64) - 32);
/* 273 */     double zz = getZ() + (this.random.nextDouble() - 0.5D) * 64.0D;
/* 274 */     return teleport(xx, yy, zz);
/*     */   }
/*     */   
/*     */   private boolean teleportTowards(Entity entity) {
/* 278 */     Vec3 dir = new Vec3(getX() - entity.getX(), getY(0.5D) - entity.getEyeY(), getZ() - entity.getZ());
/* 279 */     dir = dir.normalize();
/* 280 */     double d = 16.0D;
/* 281 */     double xx = getX() + (this.random.nextDouble() - 0.5D) * 8.0D - dir.x * 16.0D;
/* 282 */     double yy = getY() + (this.random.nextInt(16) - 8) - dir.y * 16.0D;
/* 283 */     double zz = getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - dir.z * 16.0D;
/* 284 */     return teleport(xx, yy, zz);
/*     */   }
/*     */   
/*     */   private boolean teleport(double x, double y, double z) {
/* 288 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, y, z);
/* 289 */     while (pos.getY() > level().getMinY() && !level().getBlockState(pos).blocksMotion()) {
/* 290 */       pos.move(Direction.DOWN);
/*     */     }
/* 292 */     BlockState blockState = level().getBlockState(pos);
/* 293 */     boolean couldStandOn = blockState.blocksMotion();
/* 294 */     boolean isWet = blockState.getFluidState().is(FluidTags.WATER);
/* 295 */     if (!couldStandOn || isWet) {
/* 296 */       return false;
/*     */     }
/*     */     
/* 299 */     Vec3 oldPos = position();
/* 300 */     boolean result = randomTeleport(x, y, z, true);
/* 301 */     if (result) {
/* 302 */       level().gameEvent(GameEvent.TELEPORT, oldPos, GameEvent.Context.of(this));
/*     */       
/* 304 */       if (!isSilent()) {
/* 305 */         level().playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, getSoundSource(), 1.0F, 1.0F);
/* 306 */         playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
/*     */       } 
/*     */     } 
/*     */     
/* 310 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 315 */   protected SoundEvent getAmbientSound() { return isCreepy() ? SoundEvents.ENDERMAN_SCREAM : SoundEvents.ENDERMAN_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 320 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ENDERMAN_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 325 */   protected SoundEvent getDeathSound() { return SoundEvents.ENDERMAN_DEATH; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
/* 330 */     super.dropCustomDeathLoot(level, source, killedByPlayer);
/* 331 */     BlockState carryingBlock = getCarriedBlock();
/* 332 */     if (carryingBlock != null) {
/*     */       
/* 334 */       ItemStack fakeTool = new ItemStack(Items.DIAMOND_AXE);
/* 335 */       EnchantmentHelper.enchantItemFromProvider(fakeTool, level.registryAccess(), VanillaEnchantmentProviders.ENDERMAN_LOOT_DROP, level.getCurrentDifficultyAt(blockPosition()), getRandom());
/*     */ 
/*     */ 
/*     */       
/* 339 */       LootParams.Builder params = (new LootParams.Builder((ServerLevel)level())).withParameter(LootContextParams.ORIGIN, position()).withParameter(LootContextParams.TOOL, fakeTool).withOptionalParameter(LootContextParams.THIS_ENTITY, this);
/* 340 */       List<ItemStack> blockDrops = carryingBlock.getDrops(params);
/* 341 */       for (ItemStack itemStack : blockDrops) {
/* 342 */         spawnAtLocation(level, itemStack);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 348 */   public void setCarriedBlock(BlockState carryingBlock) { this.entityData.set(DATA_CARRY_STATE, Optional.ofNullable(carryingBlock)); }
/*     */ 
/*     */ 
/*     */   
/* 352 */   public BlockState getCarriedBlock() { return (BlockState)((Optional)this.entityData.get(DATA_CARRY_STATE)).orElse(null); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 357 */     if (isInvulnerableTo(level, source)) {
/* 358 */       return false;
/*     */     }
/*     */     
/* 361 */     Entity entity = source.getDirectEntity(); AbstractThrownPotion potion = (AbstractThrownPotion)entity, thrownPotion = (entity instanceof AbstractThrownPotion) ? potion : null;
/* 362 */     if (source.is(DamageTypeTags.IS_PROJECTILE) || thrownPotion != null) {
/* 363 */       boolean hurtWithCleanWater = (thrownPotion != null && hurtWithCleanWater(level, source, thrownPotion, damage));
/* 364 */       for (int i = 0; i < 64; i++) {
/* 365 */         if (teleport()) {
/* 366 */           return true;
/*     */         }
/*     */       } 
/* 369 */       return hurtWithCleanWater;
/*     */     } 
/*     */     
/* 372 */     boolean result = super.hurtServer(level, source, damage);
/* 373 */     if (!(source.getEntity() instanceof LivingEntity) && this.random.nextInt(10) != 0) {
/* 374 */       teleport();
/*     */     }
/*     */     
/* 377 */     return result;
/*     */   }
/*     */   
/*     */   private boolean hurtWithCleanWater(ServerLevel level, DamageSource source, AbstractThrownPotion thrownPotion, float damage) {
/* 381 */     ItemStack potionItemStack = thrownPotion.getItem();
/*     */     
/* 383 */     PotionContents potionContents = (PotionContents)potionItemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
/* 384 */     if (potionContents.is(Potions.WATER)) {
/* 385 */       return super.hurtServer(level, source, damage);
/*     */     }
/*     */     
/* 388 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 392 */   public boolean isCreepy() { return ((Boolean)this.entityData.get(DATA_CREEPY)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 396 */   public boolean hasBeenStaredAt() { return ((Boolean)this.entityData.get(DATA_STARED_AT)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 400 */   public void setBeingStaredAt() { this.entityData.set(DATA_STARED_AT, Boolean.valueOf(true)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 405 */   public boolean requiresCustomPersistence() { return (super.requiresCustomPersistence() || getCarriedBlock() != null); }
/*     */   
/*     */   private static class EndermanLookForPlayerGoal
/*     */     extends NearestAttackableTargetGoal<Player>
/*     */   {
/*     */     private final EnderMan enderman;
/*     */     private Player pendingTarget;
/*     */     private int aggroTime;
/*     */     private int teleportTime;
/*     */     private final TargetingConditions startAggroTargetConditions;
/* 415 */     private final TargetingConditions continueAggroTargetConditions = TargetingConditions.forCombat().ignoreLineOfSight();
/*     */     private final TargetingConditions.Selector isAngerInducing;
/*     */     
/*     */     public EndermanLookForPlayerGoal(EnderMan enderman, TargetingConditions.Selector isAngryAt) {
/* 419 */       super(enderman, Player.class, 10, false, false, isAngryAt);
/* 420 */       this.enderman = enderman;
/* 421 */       this.isAngerInducing = ((target, level) -> ((enderman.isBeingStaredBy((Player)target) || enderman.isAngryAt(target, level)) && !enderman.hasIndirectPassenger(target)));
/*     */       
/* 423 */       this.startAggroTargetConditions = TargetingConditions.forCombat().range(getFollowDistance()).selector(this.isAngerInducing);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 428 */       this.pendingTarget = getServerLevel(this.enderman).getNearestPlayer(this.startAggroTargetConditions.range(getFollowDistance()), this.enderman);
/* 429 */       return (this.pendingTarget != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 434 */       this.aggroTime = adjustedTickDelay(5);
/* 435 */       this.teleportTime = 0;
/* 436 */       this.enderman.setBeingStaredAt();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void stop() {
/* 442 */       this.pendingTarget = null;
/*     */       
/* 444 */       super.stop();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 449 */       if (this.pendingTarget != null) {
/* 450 */         if (!this.isAngerInducing.test(this.pendingTarget, getServerLevel(this.enderman))) {
/* 451 */           return false;
/*     */         }
/* 453 */         this.enderman.lookAt(this.pendingTarget, 10.0F, 10.0F);
/* 454 */         return true;
/* 455 */       }  if (this.target != null) {
/* 456 */         if (this.enderman.hasIndirectPassenger(this.target))
/* 457 */           return false; 
/* 458 */         if (this.continueAggroTargetConditions.test(getServerLevel(this.enderman), this.enderman, this.target)) {
/* 459 */           return true;
/*     */         }
/*     */       } 
/* 462 */       return super.canContinueToUse();
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 467 */       if (this.enderman.getTarget() == null) {
/* 468 */         setTarget(null);
/*     */       }
/*     */       
/* 471 */       if (this.pendingTarget != null) {
/* 472 */         if (--this.aggroTime <= 0) {
/* 473 */           this.target = this.pendingTarget;
/* 474 */           this.pendingTarget = null;
/* 475 */           super.start();
/*     */         } 
/*     */       } else {
/* 478 */         if (this.target != null && !this.enderman.isPassenger()) {
/* 479 */           if (this.enderman.isBeingStaredBy((Player)this.target)) {
/* 480 */             if (this.target.distanceToSqr(this.enderman) < 16.0D) {
/* 481 */               this.enderman.teleport();
/*     */             }
/* 483 */             this.teleportTime = 0;
/* 484 */           } else if (this.target.distanceToSqr(this.enderman) > 256.0D && 
/* 485 */             this.teleportTime++ >= adjustedTickDelay(30) && 
/* 486 */             this.enderman.teleportTowards(this.target)) {
/* 487 */             this.teleportTime = 0;
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 493 */         super.tick();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EndermanFreezeWhenLookedAt extends Goal {
/*     */     private final EnderMan enderman;
/*     */     private LivingEntity target;
/*     */     
/*     */     public EndermanFreezeWhenLookedAt(EnderMan enderman) {
/* 503 */       this.enderman = enderman;
/* 504 */       setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/*     */       Player playerTarget;
/* 509 */       this.target = this.enderman.getTarget();
/* 510 */       LivingEntity livingEntity = this.target; if (livingEntity instanceof Player) { playerTarget = (Player)livingEntity; }
/* 511 */       else { return false; }
/*     */       
/* 513 */       double dist = this.target.distanceToSqr(this.enderman);
/* 514 */       if (dist > 256.0D) {
/* 515 */         return false;
/*     */       }
/* 517 */       return this.enderman.isBeingStaredBy(playerTarget);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 522 */     public void start() { this.enderman.getNavigation().stop(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 527 */     public void tick() { this.enderman.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ()); }
/*     */   }
/*     */   
/*     */   private static class EndermanLeaveBlockGoal
/*     */     extends Goal
/*     */   {
/*     */     private final EnderMan enderman;
/*     */     
/* 535 */     public EndermanLeaveBlockGoal(EnderMan enderman) { this.enderman = enderman; }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 540 */       if (this.enderman.getCarriedBlock() == null) {
/* 541 */         return false;
/*     */       }
/* 543 */       if (!((Boolean)getServerLevel(this.enderman).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 544 */         return false;
/*     */       }
/* 546 */       return (this.enderman.getRandom().nextInt(reducedTickDelay(2000)) == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 551 */       RandomSource random = this.enderman.getRandom();
/* 552 */       Level level = this.enderman.level();
/*     */       
/* 554 */       int xt = Mth.floor(this.enderman.getX() - 1.0D + random.nextDouble() * 2.0D);
/* 555 */       int yt = Mth.floor(this.enderman.getY() + random.nextDouble() * 2.0D);
/* 556 */       int zt = Mth.floor(this.enderman.getZ() - 1.0D + random.nextDouble() * 2.0D);
/* 557 */       BlockPos pos = new BlockPos(xt, yt, zt);
/* 558 */       BlockState targetState = level.getBlockState(pos);
/* 559 */       BlockPos below = pos.below();
/* 560 */       BlockState belowState = level.getBlockState(below);
/*     */       
/* 562 */       BlockState carried = this.enderman.getCarriedBlock();
/* 563 */       if (carried == null) {
/*     */         return;
/*     */       }
/*     */       
/* 567 */       carried = Block.updateFromNeighbourShapes(carried, this.enderman.level(), pos);
/* 568 */       if (canPlaceBlock(level, pos, carried, targetState, belowState, below)) {
/* 569 */         level.setBlock(pos, carried, 3);
/* 570 */         level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(this.enderman, carried));
/* 571 */         this.enderman.setCarriedBlock(null);
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean canPlaceBlock(Level level, BlockPos pos, BlockState carried, BlockState targetState, BlockState belowState, BlockPos below) {
/* 576 */       return (targetState.isAir() && !belowState.isAir() && !belowState.is(Blocks.BEDROCK) && belowState.isCollisionShapeFullBlock(level, below) && carried.canSurvive(level, pos) && level
/* 577 */         .getEntities(this.enderman, AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf(pos))).isEmpty());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EndermanTakeBlockGoal
/*     */     extends Goal {
/*     */     private final EnderMan enderman;
/*     */     
/* 585 */     public EndermanTakeBlockGoal(EnderMan enderman) { this.enderman = enderman; }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 590 */       if (this.enderman.getCarriedBlock() != null) {
/* 591 */         return false;
/*     */       }
/* 593 */       if (!((Boolean)getServerLevel(this.enderman).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 594 */         return false;
/*     */       }
/* 596 */       return (this.enderman.getRandom().nextInt(reducedTickDelay(20)) == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 601 */       RandomSource random = this.enderman.getRandom();
/* 602 */       Level level = this.enderman.level();
/*     */       
/* 604 */       int xt = Mth.floor(this.enderman.getX() - 2.0D + random.nextDouble() * 4.0D);
/* 605 */       int yt = Mth.floor(this.enderman.getY() + random.nextDouble() * 3.0D);
/* 606 */       int zt = Mth.floor(this.enderman.getZ() - 2.0D + random.nextDouble() * 4.0D);
/* 607 */       BlockPos pos = new BlockPos(xt, yt, zt);
/* 608 */       BlockState blockState = level.getBlockState(pos);
/*     */       
/* 610 */       Vec3 from = new Vec3(this.enderman.getBlockX() + 0.5D, yt + 0.5D, this.enderman.getBlockZ() + 0.5D);
/* 611 */       Vec3 to = new Vec3(xt + 0.5D, yt + 0.5D, zt + 0.5D);
/* 612 */       BlockHitResult result = level.clip(new ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this.enderman));
/* 613 */       boolean reachable = result.getBlockPos().equals(pos);
/*     */       
/* 615 */       if (blockState.is(BlockTags.ENDERMAN_HOLDABLE) && reachable) {
/* 616 */         level.removeBlock(pos, false);
/* 617 */         level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(this.enderman, blockState));
/* 618 */         this.enderman.setCarriedBlock(blockState.getBlock().defaultBlockState());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\EnderMan.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */