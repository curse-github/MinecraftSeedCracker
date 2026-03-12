/*      */ package net.minecraft.world.entity.animal.bee;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Optional;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.tags.PoiTypeTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.TimeUtil;
/*      */ import net.minecraft.util.VisibleForDebug;
/*      */ import net.minecraft.util.debug.DebugBeeInfo;
/*      */ import net.minecraft.util.debug.DebugSubscriptions;
/*      */ import net.minecraft.util.debug.DebugValueSource;
/*      */ import net.minecraft.util.valueproviders.UniformInt;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.AgeableMob;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityReference;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.NeutralMob;
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.control.FlyingMoveControl;
/*      */ import net.minecraft.world.entity.ai.control.LookControl;
/*      */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.ai.goal.GoalSelector;
/*      */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*      */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*      */ import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
/*      */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*      */ import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
/*      */ import net.minecraft.world.entity.ai.util.AirRandomPos;
/*      */ import net.minecraft.world.entity.ai.util.HoverRandomPos;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*      */ import net.minecraft.world.entity.animal.Animal;
/*      */ import net.minecraft.world.entity.animal.FlyingAnimal;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.BlockItem;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.BonemealableBlock;
/*      */ import net.minecraft.world.level.block.CropBlock;
/*      */ import net.minecraft.world.level.block.DoublePlantBlock;
/*      */ import net.minecraft.world.level.block.FlowerBlock;
/*      */ import net.minecraft.world.level.block.StemBlock;
/*      */ import net.minecraft.world.level.block.SweetBerryBushBlock;
/*      */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*      */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.pathfinder.Path;
/*      */ import net.minecraft.world.level.pathfinder.PathType;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ public class Bee extends Animal implements FlyingAnimal, NeutralMob {
/*      */   public static final float FLAP_DEGREES_PER_TICK = 120.32113F;
/*  102 */   public static final int TICKS_PER_FLAP = Mth.ceil(1.4959966F);
/*      */   
/*  104 */   private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Bee.class, EntityDataSerializers.BYTE);
/*  105 */   private static final EntityDataAccessor<Long> DATA_ANGER_END_TIME = SynchedEntityData.defineId(Bee.class, EntityDataSerializers.LONG);
/*      */   
/*      */   private static final int FLAG_ROLL = 2;
/*      */   
/*      */   private static final int FLAG_HAS_STUNG = 4;
/*      */   
/*      */   private static final int FLAG_HAS_NECTAR = 8;
/*      */   
/*      */   private static final int STING_DEATH_COUNTDOWN = 1200;
/*      */   
/*      */   private static final int TICKS_BEFORE_GOING_TO_KNOWN_FLOWER = 600;
/*      */   
/*      */   private static final int TICKS_WITHOUT_NECTAR_BEFORE_GOING_HOME = 3600;
/*      */   
/*      */   private static final int MIN_ATTACK_DIST = 4;
/*      */   
/*      */   private static final int MAX_CROPS_GROWABLE = 10;
/*      */   
/*      */   private static final int POISON_SECONDS_NORMAL = 10;
/*      */   
/*      */   private static final int POISON_SECONDS_HARD = 18;
/*      */   
/*      */   private static final int TOO_FAR_DISTANCE = 48;
/*      */   
/*      */   private static final int HIVE_CLOSE_ENOUGH_DISTANCE = 2;
/*      */   private static final int RESTRICTED_WANDER_DISTANCE_REDUCTION = 24;
/*      */   private static final int DEFAULT_WANDER_DISTANCE_REDUCTION = 16;
/*      */   private static final int PATHFIND_TO_HIVE_WHEN_CLOSER_THAN = 16;
/*      */   private static final int HIVE_SEARCH_DISTANCE = 20;
/*      */   public static final String TAG_CROPS_GROWN_SINCE_POLLINATION = "CropsGrownSincePollination";
/*      */   public static final String TAG_CANNOT_ENTER_HIVE_TICKS = "CannotEnterHiveTicks";
/*      */   public static final String TAG_TICKS_SINCE_POLLINATION = "TicksSincePollination";
/*      */   public static final String TAG_HAS_STUNG = "HasStung";
/*      */   public static final String TAG_HAS_NECTAR = "HasNectar";
/*      */   public static final String TAG_FLOWER_POS = "flower_pos";
/*      */   public static final String TAG_HIVE_POS = "hive_pos";
/*      */   public static final boolean DEFAULT_HAS_NECTAR = false;
/*      */   private static final boolean DEFAULT_HAS_STUNG = false;
/*      */   private static final int DEFAULT_TICKS_SINCE_POLLINATION = 0;
/*      */   private static final int DEFAULT_CANNOT_ENTER_HIVE_TICKS = 0;
/*      */   private static final int DEFAULT_CROPS_GROWN_SINCE_POLLINATION = 0;
/*  146 */   private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*      */   
/*      */   private EntityReference<LivingEntity> persistentAngerTarget;
/*      */   
/*      */   private float rollAmount;
/*      */   
/*      */   private float rollAmountO;
/*      */   private int timeSinceSting;
/*  154 */   private int ticksWithoutNectarSinceExitingHive = 0;
/*      */ 
/*      */   
/*  157 */   private int stayOutOfHiveCountdown = 0;
/*      */ 
/*      */   
/*  160 */   private int numCropsGrownSincePollination = 0;
/*      */ 
/*      */   
/*      */   private static final int COOLDOWN_BEFORE_LOCATING_NEW_HIVE = 200;
/*      */ 
/*      */   
/*      */   private int remainingCooldownBeforeLocatingNewHive;
/*      */   
/*      */   private static final int COOLDOWN_BEFORE_LOCATING_NEW_FLOWER = 200;
/*      */   
/*      */   private static final int MIN_FIND_FLOWER_RETRY_COOLDOWN = 20;
/*      */   
/*      */   private static final int MAX_FIND_FLOWER_RETRY_COOLDOWN = 60;
/*      */   
/*  174 */   private int remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(this.random, 20, 60);
/*      */   
/*      */   private BlockPos savedFlowerPos;
/*      */   
/*      */   private BlockPos hivePos;
/*      */   
/*      */   private BeePollinateGoal beePollinateGoal;
/*      */   
/*      */   private BeeGoToHiveGoal goToHiveGoal;
/*      */   private BeeGoToKnownFlowerGoal goToKnownFlowerGoal;
/*      */   private int underWaterTicks;
/*      */   
/*      */   public Bee(EntityType<? extends Bee> type, Level level) {
/*  187 */     super(type, level);
/*  188 */     this.moveControl = new FlyingMoveControl(this, 20, true);
/*  189 */     this.lookControl = new BeeLookControl(this);
/*      */     
/*  191 */     setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
/*  192 */     setPathfindingMalus(PathType.WATER, -1.0F);
/*  193 */     setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
/*  194 */     setPathfindingMalus(PathType.COCOA, -1.0F);
/*  195 */     setPathfindingMalus(PathType.FENCE, -1.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  200 */     super.defineSynchedData(entityData);
/*  201 */     entityData.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*  202 */     entityData.define(DATA_ANGER_END_TIME, Long.valueOf(-1L));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/*  208 */     if (level.getBlockState(pos).isAir()) {
/*  209 */       return 10.0F;
/*      */     }
/*  211 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void registerGoals() {
/*  216 */     this.goalSelector.addGoal(0, new BeeAttackGoal(this, 1.399999976158142D, true));
/*  217 */     this.goalSelector.addGoal(1, new BeeEnterHiveGoal());
/*  218 */     this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
/*  219 */     this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, i -> i.is(ItemTags.BEE_FOOD), false));
/*      */     
/*  221 */     this.goalSelector.addGoal(3, new ValidateHiveGoal());
/*  222 */     this.goalSelector.addGoal(3, new ValidateFlowerGoal());
/*      */     
/*  224 */     this.beePollinateGoal = new BeePollinateGoal();
/*  225 */     this.goalSelector.addGoal(4, this.beePollinateGoal);
/*      */     
/*  227 */     this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
/*      */     
/*  229 */     this.goalSelector.addGoal(5, new BeeLocateHiveGoal());
/*      */     
/*  231 */     this.goToHiveGoal = new BeeGoToHiveGoal();
/*  232 */     this.goalSelector.addGoal(5, this.goToHiveGoal);
/*      */     
/*  234 */     this.goToKnownFlowerGoal = new BeeGoToKnownFlowerGoal();
/*  235 */     this.goalSelector.addGoal(6, this.goToKnownFlowerGoal);
/*      */     
/*  237 */     this.goalSelector.addGoal(7, new BeeGrowCropGoal());
/*  238 */     this.goalSelector.addGoal(8, new BeeWanderGoal());
/*  239 */     this.goalSelector.addGoal(9, new FloatGoal(this));
/*      */     
/*  241 */     this.targetSelector.addGoal(1, (new BeeHurtByOtherGoal(this)).setAlertOthers(new Class[0]));
/*  242 */     this.targetSelector.addGoal(2, new BeeBecomeAngryTargetGoal(this));
/*  243 */     this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal(this, true));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  248 */     super.addAdditionalSaveData(output);
/*      */     
/*  250 */     output.storeNullable("hive_pos", BlockPos.CODEC, this.hivePos);
/*  251 */     output.storeNullable("flower_pos", BlockPos.CODEC, this.savedFlowerPos);
/*  252 */     output.putBoolean("HasNectar", hasNectar());
/*  253 */     output.putBoolean("HasStung", hasStung());
/*  254 */     output.putInt("TicksSincePollination", this.ticksWithoutNectarSinceExitingHive);
/*  255 */     output.putInt("CannotEnterHiveTicks", this.stayOutOfHiveCountdown);
/*  256 */     output.putInt("CropsGrownSincePollination", this.numCropsGrownSincePollination);
/*      */     
/*  258 */     addPersistentAngerSaveData(output);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  263 */     super.readAdditionalSaveData(input);
/*  264 */     setHasNectar(input.getBooleanOr("HasNectar", false));
/*  265 */     setHasStung(input.getBooleanOr("HasStung", false));
/*  266 */     this.ticksWithoutNectarSinceExitingHive = input.getIntOr("TicksSincePollination", 0);
/*  267 */     this.stayOutOfHiveCountdown = input.getIntOr("CannotEnterHiveTicks", 0);
/*  268 */     this.numCropsGrownSincePollination = input.getIntOr("CropsGrownSincePollination", 0);
/*      */     
/*  270 */     this.hivePos = (BlockPos)input.read("hive_pos", BlockPos.CODEC).orElse(null);
/*  271 */     this.savedFlowerPos = (BlockPos)input.read("flower_pos", BlockPos.CODEC).orElse(null);
/*      */     
/*  273 */     readPersistentAngerSaveData(level(), input);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/*  278 */     DamageSource damageSource = damageSources().sting(this);
/*  279 */     boolean wasHurt = target.hurtServer(level, damageSource, (int)getAttributeValue(Attributes.ATTACK_DAMAGE));
/*  280 */     if (wasHurt) {
/*  281 */       EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
/*  282 */       if (target instanceof LivingEntity) { LivingEntity livingTarget = (LivingEntity)target;
/*  283 */         livingTarget.setStingerCount(livingTarget.getStingerCount() + 1);
/*  284 */         int poisonTime = 0;
/*  285 */         if (level().getDifficulty() == Difficulty.NORMAL) {
/*  286 */           poisonTime = 10;
/*  287 */         } else if (level().getDifficulty() == Difficulty.HARD) {
/*  288 */           poisonTime = 18;
/*      */         } 
/*      */         
/*  291 */         if (poisonTime > 0) {
/*  292 */           livingTarget.addEffect(new MobEffectInstance(MobEffects.POISON, poisonTime * 20, 0), this);
/*      */         } }
/*      */       
/*  295 */       setHasStung(true);
/*  296 */       stopBeingAngry();
/*      */       
/*  298 */       playSound(SoundEvents.BEE_STING, 1.0F, 1.0F);
/*      */     } 
/*  300 */     return wasHurt;
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  305 */     super.tick();
/*      */ 
/*      */     
/*  308 */     if (hasNectar() && getCropsGrownSincePollination() < 10 && this.random.nextFloat() < 0.05F) {
/*  309 */       for (int i = 0; i < this.random.nextInt(2) + 1; i++) {
/*  310 */         spawnFluidParticle(level(), getX() - 0.30000001192092896D, getX() + 0.30000001192092896D, getZ() - 0.30000001192092896D, getZ() + 0.30000001192092896D, getY(0.5D), ParticleTypes.FALLING_NECTAR);
/*      */       }
/*      */     }
/*      */     
/*  314 */     updateRollAmount();
/*      */   }
/*      */ 
/*      */   
/*  318 */   private void spawnFluidParticle(Level level, double x1, double x2, double z1, double z2, double y, ParticleOptions dripParticle) { level.addParticle(dripParticle, Mth.lerp(level.random.nextDouble(), x1, x2), y, Mth.lerp(level.random.nextDouble(), z1, z2), 0.0D, 0.0D, 0.0D); }
/*      */ 
/*      */   
/*      */   private void pathfindRandomlyTowards(BlockPos targetPos) {
/*  322 */     Vec3 targetVec = Vec3.atBottomCenterOf(targetPos);
/*  323 */     int yAdjust = 0;
/*  324 */     BlockPos beePos = blockPosition();
/*  325 */     int yDelta = (int)targetVec.y - beePos.getY();
/*  326 */     if (yDelta > 2) {
/*  327 */       yAdjust = 4;
/*  328 */     } else if (yDelta < -2) {
/*  329 */       yAdjust = -4;
/*      */     } 
/*      */     
/*  332 */     int xzDist = 6;
/*  333 */     int yDist = 8;
/*  334 */     int dist = beePos.distManhattan(targetPos);
/*  335 */     if (dist < 15) {
/*  336 */       xzDist = dist / 2;
/*  337 */       yDist = dist / 2;
/*      */     } 
/*      */     
/*  340 */     Vec3 nextPosTowards = AirRandomPos.getPosTowards(this, xzDist, yDist, yAdjust, targetVec, 0.3141592741012573D);
/*  341 */     if (nextPosTowards == null) {
/*      */       return;
/*      */     }
/*      */     
/*  345 */     this.navigation.setMaxVisitedNodesMultiplier(0.5F);
/*  346 */     this.navigation.moveTo(nextPosTowards.x, nextPosTowards.y, nextPosTowards.z, 1.0D);
/*      */   }
/*      */ 
/*      */   
/*  350 */   public BlockPos getSavedFlowerPos() { return this.savedFlowerPos; }
/*      */ 
/*      */ 
/*      */   
/*  354 */   public boolean hasSavedFlowerPos() { return (this.savedFlowerPos != null); }
/*      */ 
/*      */ 
/*      */   
/*  358 */   public void setSavedFlowerPos(BlockPos savedFlowerPos) { this.savedFlowerPos = savedFlowerPos; }
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForDebug
/*  363 */   public int getTravellingTicks() { return Math.max(this.goToHiveGoal.travellingTicks, this.goToKnownFlowerGoal.travellingTicks); }
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForDebug
/*  368 */   public List<BlockPos> getBlacklistedHives() { return this.goToHiveGoal.blacklistedTargets; }
/*      */ 
/*      */ 
/*      */   
/*  372 */   private boolean isTiredOfLookingForNectar() { return (this.ticksWithoutNectarSinceExitingHive > 3600); }
/*      */ 
/*      */   
/*      */   private void dropHive() {
/*  376 */     this.hivePos = null;
/*  377 */     this.remainingCooldownBeforeLocatingNewHive = 200;
/*      */   }
/*      */   
/*      */   private void dropFlower() {
/*  381 */     this.savedFlowerPos = null;
/*  382 */     this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(this.random, 20, 60);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean wantsToEnterHive() {
/*  387 */     if (this.stayOutOfHiveCountdown > 0 || this.beePollinateGoal.isPollinating() || hasStung() || getTarget() != null) {
/*  388 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  394 */     boolean wantsToEnterHive = (hasNectar() || isTiredOfLookingForNectar() || ((Boolean)level().environmentAttributes().getValue(EnvironmentAttributes.BEES_STAY_IN_HIVE, position())).booleanValue());
/*      */ 
/*      */     
/*  397 */     return (wantsToEnterHive && !isHiveNearFire());
/*      */   }
/*      */ 
/*      */   
/*  401 */   public void setStayOutOfHiveCountdown(int ticks) { this.stayOutOfHiveCountdown = ticks; }
/*      */ 
/*      */ 
/*      */   
/*  405 */   public float getRollAmount(float a) { return Mth.lerp(a, this.rollAmountO, this.rollAmount); }
/*      */ 
/*      */   
/*      */   private void updateRollAmount() {
/*  409 */     this.rollAmountO = this.rollAmount;
/*  410 */     if (isRolling()) {
/*  411 */       this.rollAmount = Math.min(1.0F, this.rollAmount + 0.2F);
/*      */     } else {
/*  413 */       this.rollAmount = Math.max(0.0F, this.rollAmount - 0.24F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void customServerAiStep(ServerLevel level) {
/*  419 */     boolean hasStung = hasStung();
/*      */     
/*  421 */     if (isInWater()) {
/*  422 */       this.underWaterTicks++;
/*      */     } else {
/*  424 */       this.underWaterTicks = 0;
/*      */     } 
/*      */     
/*  427 */     if (this.underWaterTicks > 20) {
/*  428 */       hurtServer(level, damageSources().drown(), 1.0F);
/*      */     }
/*      */     
/*  431 */     if (hasStung) {
/*  432 */       this.timeSinceSting++;
/*      */ 
/*      */ 
/*      */       
/*  436 */       if (this.timeSinceSting % 5 == 0 && this.random.nextInt(Mth.clamp(1200 - this.timeSinceSting, 1, 1200)) == 0) {
/*  437 */         hurtServer(level, damageSources().generic(), getHealth());
/*      */       }
/*      */     } 
/*      */     
/*  441 */     if (!hasNectar()) {
/*  442 */       this.ticksWithoutNectarSinceExitingHive++;
/*      */     }
/*      */     
/*  445 */     updatePersistentAnger(level, false);
/*      */   }
/*      */ 
/*      */   
/*  449 */   public void resetTicksWithoutNectarSinceExitingHive() { this.ticksWithoutNectarSinceExitingHive = 0; }
/*      */ 
/*      */   
/*      */   private boolean isHiveNearFire() {
/*  453 */     BeehiveBlockEntity beehiveBlockEntity = getBeehiveBlockEntity();
/*  454 */     return (beehiveBlockEntity != null && beehiveBlockEntity.isFireNearby());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  459 */   public long getPersistentAngerEndTime() { return ((Long)this.entityData.get(DATA_ANGER_END_TIME)).longValue(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  464 */   public void setPersistentAngerEndTime(long endTime) { this.entityData.set(DATA_ANGER_END_TIME, Long.valueOf(endTime)); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  469 */   public EntityReference<LivingEntity> getPersistentAngerTarget() { return this.persistentAngerTarget; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  474 */   public void setPersistentAngerTarget(EntityReference<LivingEntity> persistentAngerTarget) { this.persistentAngerTarget = persistentAngerTarget; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  479 */   public void startPersistentAngerTimer() { setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random)); }
/*      */ 
/*      */   
/*      */   private boolean doesHiveHaveSpace(BlockPos hivePos) {
/*  483 */     BlockEntity blockEntity = level().getBlockEntity(hivePos);
/*  484 */     if (blockEntity instanceof BeehiveBlockEntity) {
/*  485 */       return !((BeehiveBlockEntity)blockEntity).isFull();
/*      */     }
/*  487 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   @VisibleForDebug
/*  492 */   public boolean hasHive() { return (this.hivePos != null); }
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForDebug
/*  497 */   public BlockPos getHivePos() { return this.hivePos; }
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForDebug
/*  502 */   public GoalSelector getGoalSelector() { return this.goalSelector; }
/*      */ 
/*      */ 
/*      */   
/*  506 */   private int getCropsGrownSincePollination() { return this.numCropsGrownSincePollination; }
/*      */ 
/*      */ 
/*      */   
/*  510 */   private void resetNumCropsGrownSincePollination() { this.numCropsGrownSincePollination = 0; }
/*      */ 
/*      */ 
/*      */   
/*  514 */   private void incrementNumCropsGrownSincePollination() { this.numCropsGrownSincePollination++; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void aiStep() {
/*  519 */     super.aiStep();
/*      */     
/*  521 */     if (!level().isClientSide()) {
/*  522 */       if (this.stayOutOfHiveCountdown > 0) {
/*  523 */         this.stayOutOfHiveCountdown--;
/*      */       }
/*  525 */       if (this.remainingCooldownBeforeLocatingNewHive > 0) {
/*  526 */         this.remainingCooldownBeforeLocatingNewHive--;
/*      */       }
/*  528 */       if (this.remainingCooldownBeforeLocatingNewFlower > 0) {
/*  529 */         this.remainingCooldownBeforeLocatingNewFlower--;
/*      */       }
/*      */ 
/*      */       
/*  533 */       boolean shouldRoll = (isAngry() && !hasStung() && getTarget() != null && getTarget().distanceToSqr(this) < 4.0D);
/*  534 */       setRolling(shouldRoll);
/*      */       
/*  536 */       if (this.tickCount % 20 == 0)
/*      */       {
/*  538 */         if (!isHiveValid()) {
/*  539 */           this.hivePos = null;
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private BeehiveBlockEntity getBeehiveBlockEntity() {
/*  546 */     if (this.hivePos == null) {
/*  547 */       return null;
/*      */     }
/*  549 */     if (isTooFarAway(this.hivePos)) {
/*  550 */       return null;
/*      */     }
/*  552 */     return (BeehiveBlockEntity)level().getBlockEntity(this.hivePos, BlockEntityType.BEEHIVE).orElse(null);
/*      */   }
/*      */ 
/*      */   
/*  556 */   private boolean isHiveValid() { return (getBeehiveBlockEntity() != null); }
/*      */ 
/*      */ 
/*      */   
/*  560 */   public boolean hasNectar() { return getFlag(8); }
/*      */ 
/*      */   
/*      */   private void setHasNectar(boolean hasNectar) {
/*  564 */     if (hasNectar) {
/*  565 */       resetTicksWithoutNectarSinceExitingHive();
/*      */     }
/*  567 */     setFlag(8, hasNectar);
/*      */   }
/*      */ 
/*      */   
/*  571 */   public boolean hasStung() { return getFlag(4); }
/*      */ 
/*      */ 
/*      */   
/*  575 */   private void setHasStung(boolean hasStung) { setFlag(4, hasStung); }
/*      */ 
/*      */ 
/*      */   
/*  579 */   private boolean isRolling() { return getFlag(2); }
/*      */ 
/*      */ 
/*      */   
/*  583 */   private void setRolling(boolean rolling) { setFlag(2, rolling); }
/*      */ 
/*      */ 
/*      */   
/*  587 */   private boolean isTooFarAway(BlockPos targetPos) { return !closerThan(targetPos, 48); }
/*      */ 
/*      */   
/*      */   private void setFlag(int flag, boolean value) {
/*  591 */     if (value) {
/*  592 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() | flag)));
/*      */     } else {
/*  594 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & (flag ^ 0xFFFFFFFF))));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  599 */   private boolean getFlag(int flag) { return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & flag) != 0); }
/*      */ 
/*      */   
/*      */   public static AttributeSupplier.Builder createAttributes() {
/*  603 */     return Animal.createAnimalAttributes()
/*  604 */       .add(Attributes.MAX_HEALTH, 10.0D)
/*  605 */       .add(Attributes.FLYING_SPEED, 0.6000000238418579D)
/*  606 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/*  607 */       .add(Attributes.ATTACK_DAMAGE, 2.0D);
/*      */   }
/*      */ 
/*      */   
/*      */   protected PathNavigation createNavigation(Level level) {
/*  612 */     FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level)
/*      */       {
/*      */         public boolean isStableDestination(BlockPos pos) {
/*  615 */           return !this.level.getBlockState(pos.below()).isAir();
/*      */         }
/*      */ 
/*      */         
/*      */         public void tick() {
/*  620 */           if (Bee.this.beePollinateGoal.isPollinating()) {
/*      */             return;
/*      */           }
/*      */           
/*  624 */           super.tick();
/*      */         }
/*      */       };
/*  627 */     flyingPathNavigation.setCanOpenDoors(false);
/*  628 */     flyingPathNavigation.setCanFloat(false);
/*  629 */     flyingPathNavigation.setRequiredPathLength(48.0F);
/*  630 */     return flyingPathNavigation;
/*      */   }
/*      */ 
/*      */   
/*      */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/*  635 */     ItemStack heldItem = player.getItemInHand(hand);
/*  636 */     if (isFood(heldItem)) { Item item = heldItem.getItem(); if (item instanceof BlockItem) { BlockItem blockItem = (BlockItem)item; Block block = blockItem.getBlock(); if (block instanceof FlowerBlock) { FlowerBlock flower = (FlowerBlock)block;
/*  637 */           MobEffectInstance effect = flower.getBeeInteractionEffect();
/*  638 */           if (effect != null)
/*  639 */           { usePlayerItem(player, hand, heldItem);
/*  640 */             if (!level().isClientSide()) {
/*  641 */               addEffect(effect);
/*      */             }
/*  643 */             return InteractionResult.SUCCESS; }  }
/*      */          }
/*      */        }
/*  646 */      return super.mobInteract(player, hand);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  651 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.BEE_FOOD); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void playStepSound(BlockPos pos, BlockState blockState) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  661 */   protected SoundEvent getAmbientSound() { return null; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  666 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.BEE_HURT; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  671 */   protected SoundEvent getDeathSound() { return SoundEvents.BEE_DEATH; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  676 */   protected float getSoundVolume() { return 0.4F; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  681 */   public Bee getBreedOffspring(ServerLevel level, AgeableMob partner) { return (Bee)EntityType.BEE.create(level, EntitySpawnReason.BREEDING); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  693 */   public boolean isFlapping() { return (isFlying() && this.tickCount % TICKS_PER_FLAP == 0); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  698 */   public boolean isFlying() { return !onGround(); }
/*      */ 
/*      */   
/*      */   public void dropOffNectar() {
/*  702 */     setHasNectar(false);
/*  703 */     resetNumCropsGrownSincePollination();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*  708 */     if (isInvulnerableTo(level, source)) {
/*  709 */       return false;
/*      */     }
/*      */     
/*  712 */     this.beePollinateGoal.stopPollinating();
/*  713 */     return super.hurtServer(level, source, damage);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  718 */   protected void jumpInLiquid(TagKey<Fluid> type) { setDeltaMovement(getDeltaMovement().add(0.0D, 0.01D, 0.0D)); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  723 */   public Vec3 getLeashOffset() { return new Vec3(0.0D, (0.5F * getEyeHeight()), (getBbWidth() * 0.2F)); }
/*      */ 
/*      */ 
/*      */   
/*  727 */   private boolean closerThan(BlockPos targetPos, int distance) { return targetPos.closerThan(blockPosition(), distance); }
/*      */ 
/*      */ 
/*      */   
/*  731 */   public void setHivePos(BlockPos hivePos) { this.hivePos = hivePos; }
/*      */ 
/*      */   
/*      */   public static boolean attractsBees(BlockState state) {
/*  735 */     if (state.is(BlockTags.BEE_ATTRACTIVE)) {
/*  736 */       if (((Boolean)state.getValueOrElse(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false))).booleanValue()) {
/*  737 */         return false;
/*      */       }
/*  739 */       if (state.is(Blocks.SUNFLOWER)) {
/*  740 */         return (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER);
/*      */       }
/*  742 */       return true;
/*      */     } 
/*  744 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerDebugValues(ServerLevel level, DebugValueSource.Registration registration) {
/*  749 */     super.registerDebugValues(level, registration);
/*  750 */     registration.register(DebugSubscriptions.BEES, () -> new DebugBeeInfo(
/*  751 */           Optional.ofNullable(getHivePos()), 
/*  752 */           Optional.ofNullable(getSavedFlowerPos()), 
/*  753 */           getTravellingTicks(), 
/*  754 */           getBlacklistedHives()));
/*      */   }
/*      */   
/*      */   private class BeeHurtByOtherGoal
/*      */     extends HurtByTargetGoal
/*      */   {
/*  760 */     BeeHurtByOtherGoal(Bee bee) { super(bee, new Class[0]); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  766 */     public boolean canContinueToUse() { return (Bee.this.isAngry() && super.canContinueToUse()); }
/*      */ 
/*      */ 
/*      */     
/*      */     protected void alertOther(Mob other, LivingEntity hurtByMob) {
/*  771 */       if (other instanceof Bee && this.mob.hasLineOfSight(hurtByMob))
/*  772 */         other.setTarget(hurtByMob); 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class BeeBecomeAngryTargetGoal
/*      */     extends NearestAttackableTargetGoal<Player>
/*      */   {
/*  779 */     BeeBecomeAngryTargetGoal(Bee bee) { super(bee, Player.class, 10, true, false, bee::isAngryAt); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  784 */     public boolean canUse() { return (beeCanTarget() && super.canUse()); }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/*  789 */       boolean beeCanTarget = beeCanTarget();
/*  790 */       if (!beeCanTarget || this.mob.getTarget() == null) {
/*  791 */         this.targetMob = null;
/*  792 */         return false;
/*      */       } 
/*  794 */       return super.canContinueToUse();
/*      */     }
/*      */     
/*      */     private boolean beeCanTarget() {
/*  798 */       Bee bee = (Bee)this.mob;
/*  799 */       return (bee.isAngry() && !bee.hasStung());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private abstract class BaseBeeGoal
/*      */     extends Goal
/*      */   {
/*      */     public abstract boolean canBeeUse();
/*      */     
/*      */     public abstract boolean canBeeContinueToUse();
/*      */     
/*  811 */     public boolean canUse() { return (canBeeUse() && !Bee.this.isAngry()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  816 */     public boolean canContinueToUse() { return (canBeeContinueToUse() && !Bee.this.isAngry()); }
/*      */   }
/*      */ 
/*      */   
/*      */   private class BeeWanderGoal
/*      */     extends Goal
/*      */   {
/*  823 */     BeeWanderGoal() { setFlags(EnumSet.of(Goal.Flag.MOVE)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  828 */     public boolean canUse() { return (Bee.this.navigation.isDone() && Bee.this.random.nextInt(10) == 0); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  833 */     public boolean canContinueToUse() { return Bee.this.navigation.isInProgress(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void start() {
/*  838 */       Vec3 targetPos = findPos();
/*  839 */       if (targetPos != null) {
/*  840 */         Bee.this.navigation.moveTo(Bee.this.navigation.createPath(BlockPos.containing(targetPos), 1), 1.0D);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     private Vec3 findPos() {
/*      */       Vec3 wanderDirection;
/*  847 */       if (Bee.this.isHiveValid() && !Bee.this.closerThan(Bee.this.hivePos, getWanderThreshold())) {
/*      */         
/*  849 */         Vec3 hivePosVec = Vec3.atCenterOf(Bee.this.hivePos);
/*  850 */         wanderDirection = hivePosVec.subtract(Bee.this.position()).normalize();
/*      */       } else {
/*  852 */         wanderDirection = Bee.this.getViewVector(0.0F);
/*      */       } 
/*      */       
/*  855 */       int xzDist = 8;
/*  856 */       Vec3 groundBasedPosition = HoverRandomPos.getPos(Bee.this, 8, 7, wanderDirection.x, wanderDirection.z, 1.5707964F, 3, 1);
/*  857 */       if (groundBasedPosition != null) {
/*  858 */         return groundBasedPosition;
/*      */       }
/*      */ 
/*      */       
/*  862 */       return AirAndWaterRandomPos.getPos(Bee.this, 8, 4, -2, wanderDirection.x, wanderDirection.z, 1.5707963705062866D);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int getWanderThreshold() {
/*  869 */       int distanceReduction = (Bee.this.hasHive() || Bee.this.hasSavedFlowerPos()) ? 24 : 16;
/*  870 */       return 48 - distanceReduction;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForDebug
/*      */   public class BeeGoToHiveGoal
/*      */     extends BaseBeeGoal
/*      */   {
/*      */     public static final int MAX_TRAVELLING_TICKS = 2400;
/*      */     
/*      */     private int travellingTicks;
/*      */     
/*      */     private static final int MAX_BLACKLISTED_TARGETS = 3;
/*      */     
/*  886 */     private final List<BlockPos> blacklistedTargets = Lists.newArrayList();
/*      */ 
/*      */     
/*      */     private Path lastPath;
/*      */ 
/*      */     
/*      */     private static final int TICKS_BEFORE_HIVE_DROP = 60;
/*      */ 
/*      */     
/*      */     private int ticksStuck;
/*      */ 
/*      */     
/*      */     public boolean canBeeUse() {
/*  899 */       return (Bee.this.hivePos != null && 
/*  900 */         !Bee.this.isTooFarAway(Bee.this.hivePos) && 
/*  901 */         !Bee.this.hasHome() && Bee.this
/*  902 */         .wantsToEnterHive() && 
/*  903 */         !hasReachedTarget(Bee.this.hivePos) && Bee.this
/*  904 */         .level().getBlockState(Bee.this.hivePos).is(BlockTags.BEEHIVES));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  909 */     public boolean canBeeContinueToUse() { return canBeeUse(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void start() {
/*  914 */       this.travellingTicks = 0;
/*  915 */       this.ticksStuck = 0;
/*  916 */       super.start();
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/*  921 */       this.travellingTicks = 0;
/*  922 */       this.ticksStuck = 0;
/*  923 */       Bee.this.navigation.stop();
/*  924 */       Bee.this.navigation.resetMaxVisitedNodesMultiplier();
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/*  929 */       if (Bee.this.hivePos == null) {
/*      */         return;
/*      */       }
/*      */       
/*  933 */       this.travellingTicks++;
/*      */       
/*  935 */       if (this.travellingTicks > adjustedTickDelay(2400)) {
/*      */         
/*  937 */         dropAndBlacklistHive();
/*      */         
/*      */         return;
/*      */       } 
/*  941 */       if (Bee.this.navigation.isInProgress()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  947 */       if (Bee.this.closerThan(Bee.this.hivePos, 16)) {
/*      */         
/*  949 */         boolean canReachAllTheWayToTarget = pathfindDirectlyTowards(Bee.this.hivePos);
/*  950 */         if (!canReachAllTheWayToTarget) {
/*      */           
/*  952 */           dropAndBlacklistHive();
/*      */         }
/*  954 */         else if (this.lastPath != null && Bee.this.navigation.getPath().sameAs(this.lastPath)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  960 */           this.ticksStuck++;
/*      */           
/*  962 */           if (this.ticksStuck > 60) {
/*  963 */             Bee.this.dropHive();
/*  964 */             this.ticksStuck = 0;
/*      */           } 
/*      */         } else {
/*      */           
/*  968 */           this.lastPath = Bee.this.navigation.getPath();
/*      */         } 
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  974 */       if (Bee.this.isTooFarAway(Bee.this.hivePos)) {
/*      */         
/*  976 */         Bee.this.dropHive();
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */ 
/*      */       
/*  983 */       Bee.this.pathfindRandomlyTowards(Bee.this.hivePos);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean pathfindDirectlyTowards(BlockPos targetPos) {
/*  990 */       int closeEnough = Bee.this.closerThan(targetPos, 3) ? 1 : 2;
/*  991 */       Bee.this.navigation.setMaxVisitedNodesMultiplier(10.0F);
/*  992 */       Bee.this.navigation.moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), closeEnough, 1.0D);
/*  993 */       return (Bee.this.navigation.getPath() != null && Bee.this.navigation.getPath().canReach());
/*      */     }
/*      */ 
/*      */     
/*  997 */     private boolean isTargetBlacklisted(BlockPos targetPos) { return this.blacklistedTargets.contains(targetPos); }
/*      */ 
/*      */     
/*      */     private void blacklistTarget(BlockPos targetPos) {
/* 1001 */       this.blacklistedTargets.add(targetPos);
/* 1002 */       while (this.blacklistedTargets.size() > 3) {
/* 1003 */         this.blacklistedTargets.remove(0);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/* 1008 */     private void clearBlacklist() { this.blacklistedTargets.clear(); }
/*      */ 
/*      */     
/*      */     private void dropAndBlacklistHive() {
/* 1012 */       if (Bee.this.hivePos != null) {
/* 1013 */         blacklistTarget(Bee.this.hivePos);
/*      */       }
/* 1015 */       Bee.this.dropHive();
/*      */     }
/*      */     
/*      */     private boolean hasReachedTarget(BlockPos targetPos) {
/* 1019 */       if (Bee.this.closerThan(targetPos, 2)) {
/* 1020 */         return true;
/*      */       }
/* 1022 */       Path path = Bee.this.navigation.getPath();
/* 1023 */       return (path != null && path.getTarget().equals(targetPos) && path.canReach() && path.isDone());
/*      */     }
/*      */     
/*      */     BeeGoToHiveGoal() {
/*      */       super(Bee.this);
/*      */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */   }
/*      */   
/*      */   public class BeeGoToKnownFlowerGoal
/*      */     extends BaseBeeGoal {
/*      */     private static final int MAX_TRAVELLING_TICKS = 2400;
/*      */     private int travellingTicks;
/*      */     
/*      */     BeeGoToKnownFlowerGoal() {
/* 1038 */       super(Bee.this);
/* 1039 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeUse() {
/* 1044 */       return (Bee.this.savedFlowerPos != null && 
/* 1045 */         !Bee.this.hasHome() && 
/* 1046 */         wantsToGoToKnownFlower() && 
/* 1047 */         !Bee.this.closerThan(Bee.this.savedFlowerPos, 2));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1052 */     public boolean canBeeContinueToUse() { return canBeeUse(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void start() {
/* 1057 */       this.travellingTicks = 0;
/* 1058 */       super.start();
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/* 1063 */       this.travellingTicks = 0;
/* 1064 */       Bee.this.navigation.stop();
/* 1065 */       Bee.this.navigation.resetMaxVisitedNodesMultiplier();
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1070 */       if (Bee.this.savedFlowerPos == null) {
/*      */         return;
/*      */       }
/* 1073 */       this.travellingTicks++;
/*      */       
/* 1075 */       if (this.travellingTicks > adjustedTickDelay(2400)) {
/*      */         
/* 1077 */         Bee.this.dropFlower();
/*      */         
/*      */         return;
/*      */       } 
/* 1081 */       if (Bee.this.navigation.isInProgress()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/* 1086 */       if (Bee.this.isTooFarAway(Bee.this.savedFlowerPos)) {
/*      */         
/* 1088 */         Bee.this.dropFlower();
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */ 
/*      */       
/* 1095 */       Bee.this.pathfindRandomlyTowards(Bee.this.savedFlowerPos);
/*      */     }
/*      */ 
/*      */     
/* 1099 */     private boolean wantsToGoToKnownFlower() { return (Bee.this.ticksWithoutNectarSinceExitingHive > 600); }
/*      */   }
/*      */   
/*      */   private class BeeLookControl
/*      */     extends LookControl
/*      */   {
/* 1105 */     BeeLookControl(Mob mob) { super(mob); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1110 */       if (Bee.this.isAngry()) {
/*      */         return;
/*      */       }
/* 1113 */       super.tick();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1118 */     protected boolean resetXRotOnTick() { return !Bee.this.beePollinateGoal.isPollinating(); }
/*      */   }
/*      */ 
/*      */   
/*      */   private class BeePollinateGoal
/*      */     extends BaseBeeGoal
/*      */   {
/*      */     private static final int MIN_POLLINATION_TICKS = 400;
/*      */     
/*      */     private static final double ARRIVAL_THRESHOLD = 0.1D;
/*      */     
/*      */     private static final int POSITION_CHANGE_CHANCE = 25;
/*      */     
/*      */     private static final float SPEED_MODIFIER = 0.35F;
/*      */     
/*      */     private static final float HOVER_HEIGHT_WITHIN_FLOWER = 0.6F;
/*      */     
/*      */     private static final float HOVER_POS_OFFSET = 0.33333334F;
/*      */     
/*      */     private static final int FLOWER_SEARCH_RADIUS = 5;
/*      */     private int successfulPollinatingTicks;
/*      */     private int lastSoundPlayedTick;
/*      */     private boolean pollinating;
/*      */     private Vec3 hoverPos;
/*      */     private int pollinatingTicks;
/*      */     private static final int MAX_POLLINATING_TICKS = 600;
/* 1144 */     private Long2LongOpenHashMap unreachableFlowerCache = new Long2LongOpenHashMap();
/*      */     BeePollinateGoal() {
/* 1146 */       super(Bee.this);
/* 1147 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeUse() {
/* 1152 */       if (Bee.this.remainingCooldownBeforeLocatingNewFlower > 0) {
/* 1153 */         return false;
/*      */       }
/*      */       
/* 1156 */       if (Bee.this.hasNectar()) {
/* 1157 */         return false;
/*      */       }
/* 1159 */       if (Bee.this.level().isRaining()) {
/* 1160 */         return false;
/*      */       }
/*      */ 
/*      */       
/* 1164 */       Optional<BlockPos> nearbyPos = findNearbyFlower();
/* 1165 */       if (nearbyPos.isPresent()) {
/* 1166 */         Bee.this.savedFlowerPos = (BlockPos)nearbyPos.get();
/*      */         
/* 1168 */         Bee.this.navigation.moveTo(Bee.this.savedFlowerPos.getX() + 0.5D, Bee.this.savedFlowerPos.getY() + 0.5D, Bee.this.savedFlowerPos.getZ() + 0.5D, 1.2000000476837158D);
/* 1169 */         return true;
/*      */       } 
/*      */ 
/*      */       
/* 1173 */       Bee.this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(Bee.this.random, 20, 60);
/* 1174 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1179 */       if (!this.pollinating) {
/* 1180 */         return false;
/*      */       }
/* 1182 */       if (!Bee.this.hasSavedFlowerPos()) {
/* 1183 */         return false;
/*      */       }
/* 1185 */       if (Bee.this.level().isRaining()) {
/* 1186 */         return false;
/*      */       }
/* 1188 */       if (hasPollinatedLongEnough()) {
/* 1189 */         return (Bee.this.random.nextFloat() < 0.2F);
/*      */       }
/* 1191 */       return true;
/*      */     }
/*      */ 
/*      */     
/* 1195 */     private boolean hasPollinatedLongEnough() { return (this.successfulPollinatingTicks > 400); }
/*      */ 
/*      */ 
/*      */     
/* 1199 */     private boolean isPollinating() { return this.pollinating; }
/*      */ 
/*      */ 
/*      */     
/* 1203 */     private void stopPollinating() { this.pollinating = false; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void start() {
/* 1208 */       this.successfulPollinatingTicks = 0;
/* 1209 */       this.pollinatingTicks = 0;
/* 1210 */       this.lastSoundPlayedTick = 0;
/* 1211 */       this.pollinating = true;
/* 1212 */       Bee.this.resetTicksWithoutNectarSinceExitingHive();
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/* 1217 */       if (hasPollinatedLongEnough()) {
/* 1218 */         Bee.this.setHasNectar(true);
/*      */       }
/* 1220 */       this.pollinating = false;
/* 1221 */       Bee.this.navigation.stop();
/*      */       
/* 1223 */       Bee.this.remainingCooldownBeforeLocatingNewFlower = 200;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1228 */     public boolean requiresUpdateEveryTick() { return true; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1234 */       if (!Bee.this.hasSavedFlowerPos()) {
/*      */         return;
/*      */       }
/*      */       
/* 1238 */       this.pollinatingTicks++;
/* 1239 */       if (this.pollinatingTicks > 600) {
/*      */         
/* 1241 */         Bee.this.dropFlower();
/* 1242 */         this.pollinating = false;
/* 1243 */         Bee.this.remainingCooldownBeforeLocatingNewFlower = 200;
/*      */         
/*      */         return;
/*      */       } 
/* 1247 */       Vec3 flowerPos = Vec3.atBottomCenterOf(Bee.this.savedFlowerPos).add(0.0D, 0.6000000238418579D, 0.0D);
/*      */       
/* 1249 */       if (flowerPos.distanceTo(Bee.this.position()) > 1.0D) {
/* 1250 */         this.hoverPos = flowerPos;
/* 1251 */         setWantedPos();
/*      */         
/*      */         return;
/*      */       } 
/* 1255 */       if (this.hoverPos == null) {
/* 1256 */         this.hoverPos = flowerPos;
/*      */       }
/*      */       
/* 1259 */       boolean arrivedAtHoverPos = (Bee.this.position().distanceTo(this.hoverPos) <= 0.1D);
/* 1260 */       boolean shouldSetWantedPos = true;
/*      */       
/* 1262 */       if (!arrivedAtHoverPos && this.pollinatingTicks > 600) {
/*      */         
/* 1264 */         Bee.this.dropFlower();
/*      */         
/*      */         return;
/*      */       } 
/* 1268 */       if (arrivedAtHoverPos) {
/* 1269 */         boolean shouldChangeHoverPositions = (Bee.this.random.nextInt(25) == 0);
/* 1270 */         if (shouldChangeHoverPositions) {
/* 1271 */           this.hoverPos = new Vec3(flowerPos.x() + getOffset(), flowerPos.y(), flowerPos.z() + getOffset());
/*      */           
/* 1273 */           Bee.this.navigation.stop();
/*      */         } else {
/* 1275 */           shouldSetWantedPos = false;
/*      */         } 
/*      */         
/* 1278 */         Bee.this.getLookControl().setLookAt(flowerPos.x(), flowerPos.y(), flowerPos.z());
/*      */       } 
/*      */       
/* 1281 */       if (shouldSetWantedPos) {
/* 1282 */         setWantedPos();
/*      */       }
/*      */       
/* 1285 */       this.successfulPollinatingTicks++;
/*      */       
/* 1287 */       if (Bee.this.random.nextFloat() < 0.05F && this.successfulPollinatingTicks > this.lastSoundPlayedTick + 60) {
/* 1288 */         this.lastSoundPlayedTick = this.successfulPollinatingTicks;
/* 1289 */         Bee.this.playSound(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1294 */     private void setWantedPos() { Bee.this.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), 0.3499999940395355D); }
/*      */ 
/*      */ 
/*      */     
/* 1298 */     private float getOffset() { return (Bee.this.random.nextFloat() * 2.0F - 1.0F) * 0.33333334F; }
/*      */ 
/*      */     
/*      */     private Optional<BlockPos> findNearbyFlower() {
/* 1302 */       Iterable<BlockPos> closestNearbyFlowers = BlockPos.withinManhattan(Bee.this.blockPosition(), 5, 5, 5);
/* 1303 */       Long2LongOpenHashMap tempCache = new Long2LongOpenHashMap();
/*      */       
/* 1305 */       for (BlockPos pos : closestNearbyFlowers) {
/*      */         
/* 1307 */         long unreachableUntilTime = this.unreachableFlowerCache.getOrDefault(pos.asLong(), Float.MIN_VALUE);
/* 1308 */         if (Bee.this.level().getGameTime() < unreachableUntilTime) {
/* 1309 */           tempCache.put(pos.asLong(), unreachableUntilTime);
/*      */           continue;
/*      */         } 
/* 1312 */         if (Bee.attractsBees(Bee.this.level().getBlockState(pos))) {
/* 1313 */           Path path = Bee.this.navigation.createPath(pos, 1);
/* 1314 */           if (path != null && path.canReach()) {
/* 1315 */             return Optional.of(pos);
/*      */           }
/* 1317 */           tempCache.put(pos.asLong(), Bee.this.level().getGameTime() + 600L);
/*      */         } 
/*      */       } 
/*      */       
/* 1321 */       this.unreachableFlowerCache = tempCache;
/* 1322 */       return Optional.empty();
/*      */     }
/*      */   }
/*      */   
/*      */   private class BeeLocateHiveGoal
/*      */     extends BaseBeeGoal {
/*      */     private BeeLocateHiveGoal() {
/* 1329 */       super(Bee.this);
/*      */     }
/*      */     public boolean canBeeUse() {
/* 1332 */       return (Bee.this.remainingCooldownBeforeLocatingNewHive == 0 && 
/* 1333 */         !Bee.this.hasHive() && Bee.this
/* 1334 */         .wantsToEnterHive());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1339 */     public boolean canBeeContinueToUse() { return false; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void start() {
/* 1344 */       Bee.this.remainingCooldownBeforeLocatingNewHive = 200;
/*      */ 
/*      */       
/* 1347 */       List<BlockPos> hivesWithSpace = findNearbyHivesWithSpace();
/*      */       
/* 1349 */       if (hivesWithSpace.isEmpty()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1355 */       for (BlockPos posToCheck : hivesWithSpace) {
/* 1356 */         if (!Bee.this.goToHiveGoal.isTargetBlacklisted(posToCheck)) {
/*      */           
/* 1358 */           Bee.this.hivePos = posToCheck;
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1366 */       Bee.this.goToHiveGoal.clearBlacklist();
/* 1367 */       Bee.this.hivePos = (BlockPos)hivesWithSpace.get(0);
/*      */     }
/*      */     
/*      */     private List<BlockPos> findNearbyHivesWithSpace() {
/* 1371 */       BlockPos beePos = Bee.this.blockPosition();
/* 1372 */       PoiManager poiManager = ((ServerLevel)Bee.this.level()).getPoiManager();
/* 1373 */       Stream<PoiRecord> nearbyHives = poiManager.getInRange(p -> p.is(PoiTypeTags.BEE_HOME), beePos, 20, PoiManager.Occupancy.ANY);
/* 1374 */       return (List)nearbyHives.map(PoiRecord::getPos)
/* 1375 */         .filter(Bee.this::doesHiveHaveSpace)
/* 1376 */         .sorted(Comparator.comparingDouble(pos -> pos.distSqr(beePos))).collect(Collectors.toList());
/*      */     } }
/*      */   
/*      */   private class BeeGrowCropGoal extends BaseBeeGoal { private BeeGrowCropGoal() {
/* 1380 */       super(Bee.this);
/*      */     }
/*      */     static final int GROW_CHANCE = 30;
/*      */     
/*      */     public boolean canBeeUse() {
/* 1385 */       if (Bee.this.getCropsGrownSincePollination() >= 10) {
/* 1386 */         return false;
/*      */       }
/*      */       
/* 1389 */       if (Bee.this.random.nextFloat() < 0.3F) {
/* 1390 */         return false;
/*      */       }
/*      */ 
/*      */       
/* 1394 */       return (Bee.this.hasNectar() && Bee.this.isHiveValid());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1399 */     public boolean canBeeContinueToUse() { return canBeeUse(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1404 */       if (Bee.this.random.nextInt(adjustedTickDelay(30)) != 0) {
/*      */         return;
/*      */       }
/*      */       
/* 1408 */       for (int i = 1; i <= 2; i++) {
/* 1409 */         BlockPos belowPos = Bee.this.blockPosition().below(i);
/* 1410 */         BlockState belowState = Bee.this.level().getBlockState(belowPos);
/* 1411 */         Block belowBlock = belowState.getBlock();
/* 1412 */         BlockState growState = null;
/* 1413 */         if (belowState.is(BlockTags.BEE_GROWABLES)) {
/* 1414 */           if (belowBlock instanceof CropBlock) { CropBlock cropBlockBelow = (CropBlock)belowBlock;
/* 1415 */             if (!cropBlockBelow.isMaxAge(belowState)) {
/* 1416 */               growState = cropBlockBelow.getStateForAge(cropBlockBelow.getAge(belowState) + 1);
/*      */             } }
/* 1418 */           else if (belowBlock instanceof StemBlock)
/* 1419 */           { int age = ((Integer)belowState.getValue(StemBlock.AGE)).intValue();
/* 1420 */             if (age < 7) {
/* 1421 */               growState = (BlockState)belowState.setValue(StemBlock.AGE, Integer.valueOf(age + 1));
/*      */             } }
/* 1423 */           else if (belowState.is(Blocks.SWEET_BERRY_BUSH))
/* 1424 */           { int age = ((Integer)belowState.getValue(SweetBerryBushBlock.AGE)).intValue();
/* 1425 */             if (age < 3) {
/* 1426 */               growState = (BlockState)belowState.setValue(SweetBerryBushBlock.AGE, Integer.valueOf(age + 1));
/*      */             } }
/* 1428 */           else if (belowState.is(Blocks.CAVE_VINES) || belowState.is(Blocks.CAVE_VINES_PLANT))
/* 1429 */           { BonemealableBlock bonemealableBlock = (BonemealableBlock)belowState.getBlock();
/* 1430 */             if (bonemealableBlock.isValidBonemealTarget(Bee.this.level(), belowPos, belowState)) {
/* 1431 */               bonemealableBlock.performBonemeal((ServerLevel)Bee.this.level(), Bee.this.random, belowPos, belowState);
/* 1432 */               growState = Bee.this.level().getBlockState(belowPos);
/*      */             }  }
/*      */ 
/*      */           
/* 1436 */           if (growState != null) {
/* 1437 */             Bee.this.level().levelEvent(2011, belowPos, 15);
/* 1438 */             Bee.this.level().setBlockAndUpdate(belowPos, growState);
/* 1439 */             Bee.this.incrementNumCropsGrownSincePollination();
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } }
/*      */ 
/*      */   
/*      */   private class BeeAttackGoal
/*      */     extends MeleeAttackGoal {
/* 1448 */     BeeAttackGoal(PathfinderMob mob, double speedModifier, boolean trackTarget) { super(mob, speedModifier, trackTarget); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1453 */     public boolean canUse() { return (super.canUse() && Bee.this.isAngry() && !Bee.this.hasStung()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1458 */     public boolean canContinueToUse() { return (super.canContinueToUse() && Bee.this.isAngry() && !Bee.this.hasStung()); }
/*      */   }
/*      */   
/*      */   private class BeeEnterHiveGoal
/*      */     extends BaseBeeGoal
/*      */   {
/*      */     private BeeEnterHiveGoal() {
/* 1465 */       super(Bee.this);
/*      */     }
/*      */     public boolean canBeeUse() {
/* 1468 */       if (Bee.this.hivePos != null && Bee.this.wantsToEnterHive() && Bee.this.hivePos.closerToCenterThan(Bee.this.position(), 2.0D)) {
/* 1469 */         BeehiveBlockEntity beehiveBlockEntity = Bee.this.getBeehiveBlockEntity();
/* 1470 */         if (beehiveBlockEntity != null) {
/* 1471 */           if (beehiveBlockEntity.isFull()) {
/* 1472 */             Bee.this.hivePos = null;
/*      */           } else {
/* 1474 */             return true;
/*      */           } 
/*      */         }
/*      */       } 
/* 1478 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1483 */     public boolean canBeeContinueToUse() { return false; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void start() {
/* 1488 */       BeehiveBlockEntity beehiveBlockEntity = Bee.this.getBeehiveBlockEntity();
/* 1489 */       if (beehiveBlockEntity != null)
/* 1490 */         beehiveBlockEntity.addOccupant(Bee.this); 
/*      */     } }
/*      */   private class ValidateFlowerGoal extends BaseBeeGoal { private final int validateFlowerCooldown;
/*      */     
/*      */     private ValidateFlowerGoal() {
/* 1495 */       super(Bee.this);
/* 1496 */       this.validateFlowerCooldown = Mth.nextInt(Bee.this.random, 20, 40);
/*      */       
/* 1498 */       this.lastValidateTick = -1L;
/*      */     }
/*      */     private long lastValidateTick;
/*      */     
/*      */     public void start() {
/* 1503 */       if (Bee.this.savedFlowerPos != null && Bee.this.level().isLoaded(Bee.this.savedFlowerPos) && !isFlower(Bee.this.savedFlowerPos)) {
/* 1504 */         Bee.this.dropFlower();
/*      */       }
/* 1506 */       this.lastValidateTick = Bee.this.level().getGameTime();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1511 */     public boolean canBeeUse() { return (Bee.this.level().getGameTime() > this.lastValidateTick + this.validateFlowerCooldown); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1516 */     public boolean canBeeContinueToUse() { return false; }
/*      */ 
/*      */ 
/*      */     
/* 1520 */     private boolean isFlower(BlockPos flowerPos) { return Bee.attractsBees(Bee.this.level().getBlockState(flowerPos)); } }
/*      */   private class ValidateHiveGoal extends BaseBeeGoal { private final int VALIDATE_HIVE_COOLDOWN; private long lastValidateTick;
/*      */     
/*      */     private ValidateHiveGoal() {
/* 1524 */       super(Bee.this);
/* 1525 */       this.VALIDATE_HIVE_COOLDOWN = Mth.nextInt(Bee.this.random, 20, 40);
/*      */       
/* 1527 */       this.lastValidateTick = -1L;
/*      */     }
/*      */     
/*      */     public void start() {
/* 1531 */       if (Bee.this.hivePos != null && Bee.this.level().isLoaded(Bee.this.hivePos) && !Bee.this.isHiveValid()) {
/* 1532 */         Bee.this.dropHive();
/*      */       }
/* 1534 */       this.lastValidateTick = Bee.this.level().getGameTime();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1539 */     public boolean canBeeUse() { return (Bee.this.level().getGameTime() > this.lastValidateTick + this.VALIDATE_HIVE_COOLDOWN); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1544 */     public boolean canBeeContinueToUse() { return false; } }
/*      */ 
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\bee\Bee.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */