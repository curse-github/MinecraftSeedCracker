/*      */ package net.minecraft.world.entity;
/*      */ 
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.collect.Maps;
/*      */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.function.Predicate;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.tags.EntityTypeTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.debug.DebugBrainDump;
/*      */ import net.minecraft.util.debug.DebugGoalInfo;
/*      */ import net.minecraft.util.debug.DebugPathInfo;
/*      */ import net.minecraft.util.debug.DebugSubscriptions;
/*      */ import net.minecraft.util.debug.DebugValueSource;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*      */ import net.minecraft.world.entity.ai.control.JumpControl;
/*      */ import net.minecraft.world.entity.ai.control.LookControl;
/*      */ import net.minecraft.world.entity.ai.control.MoveControl;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.ai.goal.GoalSelector;
/*      */ import net.minecraft.world.entity.ai.goal.WrappedGoal;
/*      */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*      */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*      */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*      */ import net.minecraft.world.entity.ai.sensing.Sensing;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.SpawnEggItem;
/*      */ import net.minecraft.world.item.component.AttackRange;
/*      */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*      */ import net.minecraft.world.item.component.UseRemainder;
/*      */ import net.minecraft.world.item.enchantment.Enchantment;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*      */ import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelAccessor;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.pathfinder.Path;
/*      */ import net.minecraft.world.level.pathfinder.PathType;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.level.storage.loot.LootParams;
/*      */ import net.minecraft.world.level.storage.loot.LootTable;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.ticks.ContainerSingleItem;
/*      */ 
/*      */ 
/*      */ public abstract class Mob
/*      */   extends LivingEntity
/*      */   implements Targeting, EquipmentUser, Leashable
/*      */ {
/*   97 */   private static final EntityDataAccessor<Byte> DATA_MOB_FLAGS_ID = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.BYTE);
/*      */   private static final int MOB_FLAG_NO_AI = 1;
/*      */   private static final int MOB_FLAG_LEFTHANDED = 2;
/*      */   private static final int MOB_FLAG_AGGRESSIVE = 4;
/*      */   protected static final int PICKUP_REACH = 1;
/*  102 */   private static final Vec3i ITEM_PICKUP_REACH = new Vec3i(1, 0, 1);
/*      */   
/*  104 */   private static final List<EquipmentSlot> EQUIPMENT_POPULATION_ORDER = List.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
/*      */   
/*      */   public static final float MAX_WEARING_ARMOR_CHANCE = 0.15F;
/*      */   
/*      */   public static final float WEARING_ARMOR_UPGRADE_MATERIAL_CHANCE = 0.1087F;
/*      */   
/*      */   public static final float WEARING_ARMOR_UPGRADE_MATERIAL_ATTEMPTS = 3.0F;
/*      */   
/*      */   public static final float MAX_PICKUP_LOOT_CHANCE = 0.55F;
/*      */   
/*      */   public static final float MAX_ENCHANTED_ARMOR_CHANCE = 0.5F;
/*      */   public static final float MAX_ENCHANTED_WEAPON_CHANCE = 0.25F;
/*      */   public static final int UPDATE_GOAL_SELECTOR_EVERY_N_TICKS = 2;
/*  117 */   private static final double DEFAULT_ATTACK_REACH = Math.sqrt(2.0399999618530273D) - 0.6000000238418579D;
/*      */   
/*      */   private static final boolean DEFAULT_CAN_PICK_UP_LOOT = false;
/*      */   private static final boolean DEFAULT_PERSISTENCE_REQUIRED = false;
/*      */   private static final boolean DEFAULT_LEFT_HANDED = false;
/*      */   private static final boolean DEFAULT_NO_AI = false;
/*  123 */   protected static final Identifier RANDOM_SPAWN_BONUS_ID = Identifier.withDefaultNamespace("random_spawn_bonus");
/*      */   
/*      */   public static final String TAG_DROP_CHANCES = "drop_chances";
/*      */   
/*      */   public static final String TAG_LEFT_HANDED = "LeftHanded";
/*      */   public static final String TAG_CAN_PICK_UP_LOOT = "CanPickUpLoot";
/*      */   public static final String TAG_NO_AI = "NoAI";
/*      */   public int ambientSoundTime;
/*      */   protected int xpReward;
/*      */   protected LookControl lookControl;
/*      */   protected MoveControl moveControl;
/*      */   protected JumpControl jumpControl;
/*      */   private final BodyRotationControl bodyRotationControl;
/*      */   protected PathNavigation navigation;
/*      */   protected final GoalSelector goalSelector;
/*      */   protected final GoalSelector targetSelector;
/*      */   private LivingEntity target;
/*      */   private final Sensing sensing;
/*  141 */   private DropChances dropChances = DropChances.DEFAULT;
/*      */   private boolean canPickUpLoot = false;
/*      */   private boolean persistenceRequired = false;
/*  144 */   private final Map<PathType, Float> pathfindingMalus = Maps.newEnumMap(PathType.class);
/*  145 */   private Optional<ResourceKey<LootTable>> lootTable = Optional.empty();
/*      */   
/*      */   private long lootTableSeed;
/*      */   
/*      */   private Leashable.LeashData leashData;
/*  150 */   private BlockPos homePosition = BlockPos.ZERO;
/*  151 */   private int homeRadius = -1;
/*      */   
/*      */   protected Mob(EntityType<? extends Mob> type, Level level) {
/*  154 */     super(type, level);
/*      */     
/*  156 */     this.goalSelector = new GoalSelector();
/*  157 */     this.targetSelector = new GoalSelector();
/*  158 */     this.lookControl = new LookControl(this);
/*  159 */     this.moveControl = new MoveControl(this);
/*  160 */     this.jumpControl = new JumpControl(this);
/*  161 */     this.bodyRotationControl = createBodyControl();
/*  162 */     this.navigation = createNavigation(level);
/*  163 */     this.sensing = new Sensing(this);
/*      */     
/*  165 */     if (level instanceof ServerLevel) {
/*  166 */       registerGoals();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void registerGoals() {}
/*      */   
/*      */   public static AttributeSupplier.Builder createMobAttributes() {
/*  174 */     return LivingEntity.createLivingAttributes()
/*  175 */       .add(Attributes.FOLLOW_RANGE, 16.0D);
/*      */   }
/*      */ 
/*      */   
/*  179 */   protected PathNavigation createNavigation(Level level) { return new GroundPathNavigation(this, level); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  192 */   protected boolean shouldPassengersInheritMalus() { return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getPathfindingMalus(PathType pathType) { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual getControlledVehicle : ()Lnet/minecraft/world/entity/Entity;
/*      */     //   4: astore #4
/*      */     //   6: aload #4
/*      */     //   8: instanceof net/minecraft/world/entity/Mob
/*      */     //   11: ifeq -> 32
/*      */     //   14: aload #4
/*      */     //   16: checkcast net/minecraft/world/entity/Mob
/*      */     //   19: astore_3
/*      */     //   20: aload_3
/*      */     //   21: invokevirtual shouldPassengersInheritMalus : ()Z
/*      */     //   24: ifeq -> 32
/*      */     //   27: aload_3
/*      */     //   28: astore_2
/*      */     //   29: goto -> 34
/*      */     //   32: aload_0
/*      */     //   33: astore_2
/*      */     //   34: aload_2
/*      */     //   35: getfield pathfindingMalus : Ljava/util/Map;
/*      */     //   38: aload_1
/*      */     //   39: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   44: checkcast java/lang/Float
/*      */     //   47: astore_3
/*      */     //   48: aload_3
/*      */     //   49: ifnonnull -> 59
/*      */     //   52: aload_1
/*      */     //   53: invokevirtual getMalus : ()F
/*      */     //   56: goto -> 63
/*      */     //   59: aload_3
/*      */     //   60: invokevirtual floatValue : ()F
/*      */     //   63: freturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #200	-> 0
/*      */     //   #201	-> 27
/*      */     //   #203	-> 32
/*      */     //   #206	-> 34
/*      */     //   #207	-> 48
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   29	3	2	inheritFrom	Lnet/minecraft/world/entity/Mob;
/*      */     //   20	12	3	riding	Lnet/minecraft/world/entity/Mob;
/*      */     //   0	64	0	this	Lnet/minecraft/world/entity/Mob;
/*      */     //   0	64	1	pathType	Lnet/minecraft/world/level/pathfinder/PathType;
/*      */     //   34	30	2	inheritFrom	Lnet/minecraft/world/entity/Mob;
/*      */     //   48	16	3	malus	Ljava/lang/Float; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  211 */   public void setPathfindingMalus(PathType pathType, float cost) { this.pathfindingMalus.put(pathType, Float.valueOf(cost)); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onPathfindingStart() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void onPathfindingDone() {}
/*      */ 
/*      */ 
/*      */   
/*  223 */   protected BodyRotationControl createBodyControl() { return new BodyRotationControl(this); }
/*      */ 
/*      */ 
/*      */   
/*  227 */   public LookControl getLookControl() { return this.lookControl; }
/*      */ 
/*      */   
/*      */   public MoveControl getMoveControl() {
/*  231 */     Entity entity = getControlledVehicle(); if (entity instanceof Mob) { Mob riding = (Mob)entity;
/*  232 */       return riding.getMoveControl(); }
/*      */     
/*  234 */     return this.moveControl;
/*      */   }
/*      */ 
/*      */   
/*  238 */   public JumpControl getJumpControl() { return this.jumpControl; }
/*      */ 
/*      */   
/*      */   public PathNavigation getNavigation() {
/*  242 */     Entity entity = getControlledVehicle(); if (entity instanceof Mob) { Mob riding = (Mob)entity;
/*  243 */       return riding.getNavigation(); }
/*      */     
/*  245 */     return this.navigation;
/*      */   }
/*      */ 
/*      */   
/*      */   public LivingEntity getControllingPassenger() {
/*  250 */     Entity firstPassenger = getFirstPassenger();
/*  251 */     if (!isNoAi() && firstPassenger instanceof Mob) { Mob passenger = (Mob)firstPassenger; if (firstPassenger.canControlVehicle()); }  return null;
/*      */   }
/*      */ 
/*      */   
/*  255 */   public Sensing getSensing() { return this.sensing; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  260 */   public LivingEntity getTarget() { return this.target; }
/*      */ 
/*      */ 
/*      */   
/*  264 */   protected final LivingEntity getTargetFromBrain() { return (LivingEntity)getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null); }
/*      */ 
/*      */ 
/*      */   
/*  268 */   public void setTarget(LivingEntity target) { this.target = target; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  273 */   public boolean canAttackType(EntityType<?> targetType) { return (targetType != EntityType.GHAST); }
/*      */ 
/*      */ 
/*      */   
/*  277 */   public boolean canUseNonMeleeWeapon(ItemStack item) { return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  282 */   public void ate() { gameEvent(GameEvent.EAT); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  287 */     super.defineSynchedData(entityData);
/*  288 */     entityData.define(DATA_MOB_FLAGS_ID, Byte.valueOf((byte)0));
/*      */   }
/*      */ 
/*      */   
/*  292 */   public int getAmbientSoundInterval() { return 80; }
/*      */ 
/*      */ 
/*      */   
/*  296 */   public void playAmbientSound() { makeSound(getAmbientSound()); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void baseTick() {
/*  301 */     super.baseTick();
/*      */     
/*  303 */     ProfilerFiller profiler = Profiler.get();
/*  304 */     profiler.push("mobBaseTick");
/*  305 */     if (isAlive() && this.random.nextInt(1000) < this.ambientSoundTime++) {
/*  306 */       resetAmbientSoundTime();
/*  307 */       playAmbientSound();
/*      */     } 
/*  309 */     profiler.pop();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void playHurtSound(DamageSource source) {
/*  314 */     resetAmbientSoundTime();
/*  315 */     super.playHurtSound(source);
/*      */   }
/*      */ 
/*      */   
/*  319 */   private void resetAmbientSoundTime() { this.ambientSoundTime = -getAmbientSoundInterval(); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getBaseExperienceReward(ServerLevel level) {
/*  324 */     if (this.xpReward > 0) {
/*  325 */       int result = this.xpReward;
/*  326 */       for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/*  327 */         if (!slot.canIncreaseExperience()) {
/*      */           continue;
/*      */         }
/*  330 */         ItemStack item = getItemBySlot(slot);
/*  331 */         if (!item.isEmpty() && this.dropChances.byEquipment(slot) <= 1.0F) {
/*  332 */           result += 1 + this.random.nextInt(3);
/*      */         }
/*      */       } 
/*  335 */       return result;
/*      */     } 
/*  337 */     return this.xpReward;
/*      */   }
/*      */ 
/*      */   
/*      */   public void spawnAnim() {
/*  342 */     if (level().isClientSide()) {
/*  343 */       makePoofParticles();
/*      */     } else {
/*  345 */       level().broadcastEntityEvent(this, (byte)20);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEntityEvent(byte id) {
/*  351 */     if (id == 20) {
/*  352 */       spawnAnim();
/*      */     } else {
/*  354 */       super.handleEntityEvent(id);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  360 */     super.tick();
/*      */     
/*  362 */     if (!level().isClientSide() && 
/*  363 */       this.tickCount % 5 == 0) {
/*  364 */       updateControlFlags();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void updateControlFlags() {
/*  373 */     boolean noController = !(getControllingPassenger() instanceof Mob);
/*  374 */     boolean notInBoat = !(getVehicle() instanceof net.minecraft.world.entity.vehicle.boat.AbstractBoat);
/*  375 */     this.goalSelector.setControlFlag(Goal.Flag.MOVE, noController);
/*  376 */     this.goalSelector.setControlFlag(Goal.Flag.JUMP, (noController && notInBoat));
/*  377 */     this.goalSelector.setControlFlag(Goal.Flag.LOOK, noController);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  382 */   protected void tickHeadTurn(float yBodyRotT) { this.bodyRotationControl.clientTick(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  391 */   protected SoundEvent getAmbientSound() { return null; }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  396 */     super.addAdditionalSaveData(output);
/*  397 */     output.putBoolean("CanPickUpLoot", canPickUpLoot());
/*  398 */     output.putBoolean("PersistenceRequired", this.persistenceRequired);
/*      */     
/*  400 */     if (!this.dropChances.equals(DropChances.DEFAULT)) {
/*  401 */       output.store("drop_chances", DropChances.CODEC, this.dropChances);
/*      */     }
/*      */     
/*  404 */     writeLeashData(output, this.leashData);
/*      */     
/*  406 */     if (hasHome()) {
/*  407 */       output.putInt("home_radius", this.homeRadius);
/*  408 */       output.store("home_pos", BlockPos.CODEC, this.homePosition);
/*      */     } 
/*      */     
/*  411 */     output.putBoolean("LeftHanded", isLeftHanded());
/*      */     
/*  413 */     this.lootTable.ifPresent(lootTable -> output.store("DeathLootTable", LootTable.KEY_CODEC, lootTable));
/*  414 */     if (this.lootTableSeed != 0L) {
/*  415 */       output.putLong("DeathLootTableSeed", this.lootTableSeed);
/*      */     }
/*      */     
/*  418 */     if (isNoAi()) {
/*  419 */       output.putBoolean("NoAI", isNoAi());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  425 */     super.readAdditionalSaveData(input);
/*      */     
/*  427 */     setCanPickUpLoot(input.getBooleanOr("CanPickUpLoot", false));
/*  428 */     this.persistenceRequired = input.getBooleanOr("PersistenceRequired", false);
/*      */     
/*  430 */     this.dropChances = (DropChances)input.read("drop_chances", DropChances.CODEC).orElse(DropChances.DEFAULT);
/*      */     
/*  432 */     readLeashData(input);
/*      */     
/*  434 */     this.homeRadius = input.getIntOr("home_radius", -1);
/*  435 */     if (this.homeRadius >= 0) {
/*  436 */       this.homePosition = (BlockPos)input.read("home_pos", BlockPos.CODEC).orElse(BlockPos.ZERO);
/*      */     }
/*      */     
/*  439 */     setLeftHanded(input.getBooleanOr("LeftHanded", false));
/*      */     
/*  441 */     this.lootTable = input.read("DeathLootTable", LootTable.KEY_CODEC);
/*  442 */     this.lootTableSeed = input.getLongOr("DeathLootTableSeed", 0L);
/*      */     
/*  444 */     setNoAi(input.getBooleanOr("NoAI", false));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void dropFromLootTable(ServerLevel level, DamageSource source, boolean playerKilled) {
/*  449 */     super.dropFromLootTable(level, source, playerKilled);
/*  450 */     this.lootTable = Optional.empty();
/*      */   }
/*      */ 
/*      */   
/*      */   public final Optional<ResourceKey<LootTable>> getLootTable() {
/*  455 */     if (this.lootTable.isPresent()) {
/*  456 */       return this.lootTable;
/*      */     }
/*      */     
/*  459 */     return super.getLootTable();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  464 */   public long getLootTableSeed() { return this.lootTableSeed; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  469 */   public void setZza(float zza) { this.zza = zza; }
/*      */ 
/*      */ 
/*      */   
/*  473 */   public void setYya(float yya) { this.yya = yya; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  478 */   public void setXxa(float xxa) { this.xxa = xxa; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSpeed(float speed) {
/*  483 */     super.setSpeed(speed);
/*  484 */     setZza(speed);
/*      */   }
/*      */   
/*      */   public void stopInPlace() {
/*  488 */     getNavigation().stop();
/*  489 */     setXxa(0.0F);
/*  490 */     setYya(0.0F);
/*  491 */     setSpeed(0.0F);
/*  492 */     setDeltaMovement(0.0D, 0.0D, 0.0D);
/*  493 */     resetAngularLeashMomentum();
/*      */   }
/*      */ 
/*      */   
/*      */   public void aiStep() {
/*  498 */     super.aiStep();
/*      */     
/*  500 */     if (getType().is(EntityTypeTags.BURN_IN_DAYLIGHT)) {
/*  501 */       burnUndead();
/*      */     }
/*      */     
/*  504 */     ProfilerFiller profiler = Profiler.get();
/*  505 */     profiler.push("looting");
/*      */     
/*  507 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (canPickUpLoot() && isAlive() && !this.dead && ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/*  508 */         Vec3i pickupReach = getPickupReach();
/*  509 */         List<ItemEntity> entities = level().getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(pickupReach.getX(), pickupReach.getY(), pickupReach.getZ()));
/*  510 */         for (ItemEntity entity : entities) {
/*  511 */           if (entity.isRemoved() || entity.getItem().isEmpty() || entity.hasPickUpDelay()) {
/*      */             continue;
/*      */           }
/*  514 */           if (wantsToPickUp(serverLevel, entity.getItem()))
/*  515 */             pickUpItem(serverLevel, entity); 
/*      */         } 
/*      */       }  }
/*      */     
/*  519 */     profiler.pop();
/*      */   }
/*      */ 
/*      */   
/*  523 */   protected EquipmentSlot sunProtectionSlot() { return EquipmentSlot.HEAD; }
/*      */ 
/*      */   
/*      */   private void burnUndead() {
/*  527 */     if (!isAlive() || !isSunBurnTick()) {
/*      */       return;
/*      */     }
/*      */     
/*  531 */     EquipmentSlot slot = sunProtectionSlot();
/*  532 */     ItemStack sunBlocker = getItemBySlot(slot);
/*  533 */     if (!sunBlocker.isEmpty()) {
/*  534 */       if (sunBlocker.isDamageableItem()) {
/*  535 */         Item sunBlockerItem = sunBlocker.getItem();
/*  536 */         sunBlocker.setDamageValue(sunBlocker.getDamageValue() + this.random.nextInt(2));
/*  537 */         if (sunBlocker.getDamageValue() >= sunBlocker.getMaxDamage()) {
/*  538 */           onEquippedItemBroken(sunBlockerItem, slot);
/*  539 */           setItemSlot(slot, ItemStack.EMPTY);
/*      */         } 
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*  545 */     igniteForSeconds(8.0F);
/*      */   }
/*      */   
/*      */   private boolean isSunBurnTick() {
/*  549 */     if (!level().isClientSide() && ((Boolean)level().environmentAttributes().getValue(EnvironmentAttributes.MONSTERS_BURN, position())).booleanValue()) {
/*  550 */       float br = getLightLevelDependentMagicValue();
/*  551 */       BlockPos roundedPos = BlockPos.containing(getX(), getEyeY(), getZ());
/*      */       
/*  553 */       boolean isInNonBurnableBlock = (isInWaterOrRain() || this.isInPowderSnow || this.wasInPowderSnow);
/*  554 */       if (br > 0.5F && this.random.nextFloat() * 30.0F < (br - 0.4F) * 2.0F && !isInNonBurnableBlock && level().canSeeSky(roundedPos)) {
/*  555 */         return true;
/*      */       }
/*      */     } 
/*  558 */     return false;
/*      */   }
/*      */ 
/*      */   
/*  562 */   protected Vec3i getPickupReach() { return ITEM_PICKUP_REACH; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/*  569 */     ItemStack itemStack = entity.getItem();
/*  570 */     ItemStack equippedWithStack = equipItemIfPossible(level, itemStack.copy());
/*      */     
/*  572 */     if (!equippedWithStack.isEmpty()) {
/*  573 */       onItemPickup(entity);
/*  574 */       take(entity, equippedWithStack.getCount());
/*      */ 
/*      */       
/*  577 */       itemStack.shrink(equippedWithStack.getCount());
/*      */       
/*  579 */       if (itemStack.isEmpty()) {
/*  580 */         entity.discard();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public ItemStack equipItemIfPossible(ServerLevel level, ItemStack itemStack) {
/*  586 */     EquipmentSlot slot = getEquipmentSlotForItem(itemStack);
/*  587 */     if (!isEquippableInSlot(itemStack, slot)) {
/*  588 */       return ItemStack.EMPTY;
/*      */     }
/*      */     
/*  591 */     ItemStack current = getItemBySlot(slot);
/*  592 */     boolean canReplace = canReplaceCurrentItem(itemStack, current, slot);
/*      */     
/*  594 */     if (slot.isArmor() && !canReplace) {
/*  595 */       slot = EquipmentSlot.MAINHAND;
/*  596 */       current = getItemBySlot(slot);
/*  597 */       canReplace = current.isEmpty();
/*      */     } 
/*      */     
/*  600 */     if (canReplace && canHoldItem(itemStack)) {
/*  601 */       double dropChance = this.dropChances.byEquipment(slot);
/*  602 */       if (!current.isEmpty() && Math.max(this.random.nextFloat() - 0.1F, 0.0F) < dropChance) {
/*  603 */         spawnAtLocation(level, current);
/*      */       }
/*      */       
/*  606 */       ItemStack toEquip = slot.limit(itemStack);
/*  607 */       setItemSlotAndDropWhenKilled(slot, toEquip);
/*  608 */       return toEquip;
/*      */     } 
/*  610 */     return ItemStack.EMPTY;
/*      */   }
/*      */   
/*      */   protected void setItemSlotAndDropWhenKilled(EquipmentSlot slot, ItemStack itemStack) {
/*  614 */     setItemSlot(slot, itemStack);
/*  615 */     setGuaranteedDrop(slot);
/*  616 */     this.persistenceRequired = true;
/*      */   }
/*      */ 
/*      */   
/*  620 */   protected boolean canShearEquipment(Player player) { return !isVehicle(); }
/*      */ 
/*      */ 
/*      */   
/*  624 */   public void setGuaranteedDrop(EquipmentSlot slot) { this.dropChances = this.dropChances.withGuaranteedDrop(slot); }
/*      */ 
/*      */   
/*      */   protected boolean canReplaceCurrentItem(ItemStack newItemStack, ItemStack currentItemStack, EquipmentSlot slot) {
/*  628 */     if (currentItemStack.isEmpty())
/*  629 */       return true; 
/*  630 */     if (slot.isArmor())
/*  631 */       return compareArmor(newItemStack, currentItemStack, slot); 
/*  632 */     if (slot == EquipmentSlot.MAINHAND) {
/*  633 */       return compareWeapons(newItemStack, currentItemStack, slot);
/*      */     }
/*  635 */     return false;
/*      */   }
/*      */   
/*      */   private boolean compareArmor(ItemStack newItemStack, ItemStack currentItemStack, EquipmentSlot slot) {
/*  639 */     if (EnchantmentHelper.has(currentItemStack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) {
/*  640 */       return false;
/*      */     }
/*  642 */     double newDefense = getApproximateAttributeWith(newItemStack, Attributes.ARMOR, slot);
/*  643 */     double oldDefense = getApproximateAttributeWith(currentItemStack, Attributes.ARMOR, slot);
/*  644 */     double newToughness = getApproximateAttributeWith(newItemStack, Attributes.ARMOR_TOUGHNESS, slot);
/*  645 */     double oldToughness = getApproximateAttributeWith(currentItemStack, Attributes.ARMOR_TOUGHNESS, slot);
/*  646 */     if (newDefense != oldDefense)
/*  647 */       return (newDefense > oldDefense); 
/*  648 */     if (newToughness != oldToughness) {
/*  649 */       return (newToughness > oldToughness);
/*      */     }
/*  651 */     return canReplaceEqualItem(newItemStack, currentItemStack);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean compareWeapons(ItemStack newItemStack, ItemStack currentItemStack, EquipmentSlot slot) {
/*  656 */     TagKey<Item> preferredWeaponType = getPreferredWeaponType();
/*  657 */     if (preferredWeaponType != null) {
/*  658 */       if (currentItemStack.is(preferredWeaponType) && !newItemStack.is(preferredWeaponType))
/*  659 */         return false; 
/*  660 */       if (!currentItemStack.is(preferredWeaponType) && newItemStack.is(preferredWeaponType)) {
/*  661 */         return true;
/*      */       }
/*      */     } 
/*      */     
/*  665 */     double newAttackDamage = getApproximateAttributeWith(newItemStack, Attributes.ATTACK_DAMAGE, slot);
/*  666 */     double oldAttackDamage = getApproximateAttributeWith(currentItemStack, Attributes.ATTACK_DAMAGE, slot);
/*  667 */     if (newAttackDamage != oldAttackDamage) {
/*  668 */       return (newAttackDamage > oldAttackDamage);
/*      */     }
/*  670 */     return canReplaceEqualItem(newItemStack, currentItemStack);
/*      */   }
/*      */ 
/*      */   
/*      */   private double getApproximateAttributeWith(ItemStack itemStack, Holder<Attribute> attribute, EquipmentSlot slot) {
/*  675 */     double baseValue = getAttributes().hasAttribute(attribute) ? getAttributeBaseValue(attribute) : 0.0D;
/*  676 */     ItemAttributeModifiers attributeModifiers = (ItemAttributeModifiers)itemStack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
/*  677 */     return attributeModifiers.compute(attribute, baseValue, slot);
/*      */   }
/*      */   
/*      */   public boolean canReplaceEqualItem(ItemStack newItemStack, ItemStack currentItemStack) {
/*  681 */     Set<Object2IntMap.Entry<Holder<Enchantment>>> currentEnchantments = ((ItemEnchantments)currentItemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)).entrySet();
/*  682 */     Set<Object2IntMap.Entry<Holder<Enchantment>>> newEnchantments = ((ItemEnchantments)newItemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)).entrySet();
/*      */     
/*  684 */     if (newEnchantments.size() != currentEnchantments.size()) {
/*  685 */       return (newEnchantments.size() > currentEnchantments.size());
/*      */     }
/*      */     
/*  688 */     int newDamageValue = newItemStack.getDamageValue();
/*  689 */     int currentDamageValue = currentItemStack.getDamageValue();
/*  690 */     if (newDamageValue != currentDamageValue) {
/*  691 */       return (newDamageValue < currentDamageValue);
/*      */     }
/*      */     
/*  694 */     return (newItemStack.has(DataComponents.CUSTOM_NAME) && !currentItemStack.has(DataComponents.CUSTOM_NAME));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  699 */   public boolean canHoldItem(ItemStack itemStack) { return true; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  704 */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) { return canHoldItem(itemStack); }
/*      */ 
/*      */ 
/*      */   
/*  708 */   public TagKey<Item> getPreferredWeaponType() { return null; }
/*      */ 
/*      */ 
/*      */   
/*  712 */   public boolean removeWhenFarAway(double distSqr) { return true; }
/*      */ 
/*      */ 
/*      */   
/*  716 */   public boolean requiresCustomPersistence() { return isPassenger(); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void checkDespawn() {
/*  721 */     if (level().getDifficulty() == Difficulty.PEACEFUL && !getType().isAllowedInPeaceful()) {
/*  722 */       discard();
/*      */       
/*      */       return;
/*      */     } 
/*  726 */     if (isPersistenceRequired() || requiresCustomPersistence()) {
/*  727 */       this.noActionTime = 0;
/*      */       
/*      */       return;
/*      */     } 
/*  731 */     Player player1 = level().getNearestPlayer(this, -1.0D);
/*  732 */     if (player1 != null) {
/*  733 */       double distSqr = player1.distanceToSqr(this);
/*  734 */       int instantDespawnDistance = getType().getCategory().getDespawnDistance();
/*  735 */       int despawnDistanceSqr = instantDespawnDistance * instantDespawnDistance;
/*      */       
/*  737 */       if (distSqr > despawnDistanceSqr && removeWhenFarAway(distSqr)) {
/*  738 */         discard();
/*      */       }
/*      */       
/*  741 */       int noDespawnDistance = getType().getCategory().getNoDespawnDistance();
/*  742 */       int noDespawnDistanceSqr = noDespawnDistance * noDespawnDistance;
/*  743 */       if (this.noActionTime > 600 && this.random.nextInt(800) == 0 && distSqr > noDespawnDistanceSqr && removeWhenFarAway(distSqr)) {
/*  744 */         discard();
/*  745 */       } else if (distSqr < noDespawnDistanceSqr) {
/*  746 */         this.noActionTime = 0;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void serverAiStep() {
/*  754 */     this.noActionTime++;
/*      */     
/*  756 */     ProfilerFiller profiler = Profiler.get();
/*  757 */     profiler.push("sensing");
/*  758 */     this.sensing.tick();
/*  759 */     profiler.pop();
/*      */     
/*  761 */     int idBasedTickCount = this.tickCount + getId();
/*  762 */     if (idBasedTickCount % 2 == 0 || this.tickCount <= 1) {
/*  763 */       profiler.push("targetSelector");
/*  764 */       this.targetSelector.tick();
/*  765 */       profiler.pop();
/*      */       
/*  767 */       profiler.push("goalSelector");
/*  768 */       this.goalSelector.tick();
/*  769 */       profiler.pop();
/*      */     } else {
/*  771 */       profiler.push("targetSelector");
/*  772 */       this.targetSelector.tickRunningGoals(false);
/*  773 */       profiler.pop();
/*      */       
/*  775 */       profiler.push("goalSelector");
/*  776 */       this.goalSelector.tickRunningGoals(false);
/*  777 */       profiler.pop();
/*      */     } 
/*      */     
/*  780 */     profiler.push("navigation");
/*  781 */     this.navigation.tick();
/*  782 */     profiler.pop();
/*      */     
/*  784 */     profiler.push("mob tick");
/*      */     
/*  786 */     customServerAiStep((ServerLevel)level());
/*  787 */     profiler.pop();
/*      */     
/*  789 */     profiler.push("controls");
/*  790 */     profiler.push("move");
/*  791 */     this.moveControl.tick();
/*  792 */     profiler.popPush("look");
/*  793 */     this.lookControl.tick();
/*  794 */     profiler.popPush("jump");
/*  795 */     this.jumpControl.tick();
/*  796 */     profiler.pop();
/*  797 */     profiler.pop();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void customServerAiStep(ServerLevel level) {}
/*      */ 
/*      */   
/*  804 */   public int getMaxHeadXRot() { return 40; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  811 */   public int getMaxHeadYRot() { return 75; }
/*      */ 
/*      */   
/*      */   protected void clampHeadRotationToBody() {
/*  815 */     float limit = getMaxHeadYRot();
/*  816 */     float headYRot = getYHeadRot();
/*  817 */     float delta = Mth.wrapDegrees(this.yBodyRot - headYRot);
/*  818 */     float targetDelta = Mth.clamp(Mth.wrapDegrees(this.yBodyRot - headYRot), -limit, limit);
/*  819 */     float newHeadYRot = headYRot + delta - targetDelta;
/*  820 */     setYHeadRot(newHeadYRot);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  827 */   public int getHeadRotSpeed() { return 10; }
/*      */ 
/*      */   
/*      */   public void lookAt(Entity entity, float yMax, float xMax) {
/*  831 */     double yd, xd = entity.getX() - getX();
/*      */     
/*  833 */     double zd = entity.getZ() - getZ();
/*      */     
/*  835 */     if (entity instanceof LivingEntity) { LivingEntity mob = (LivingEntity)entity;
/*  836 */       yd = mob.getEyeY() - getEyeY(); }
/*      */     else
/*  838 */     { yd = ((entity.getBoundingBox()).minY + (entity.getBoundingBox()).maxY) / 2.0D - getEyeY(); }
/*      */ 
/*      */     
/*  841 */     double sd = Math.sqrt(xd * xd + zd * zd);
/*      */     
/*  843 */     float yRotD = (float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F;
/*  844 */     float xRotD = (float)-(Mth.atan2(yd, sd) * 57.2957763671875D);
/*  845 */     setXRot(rotlerp(getXRot(), xRotD, xMax));
/*  846 */     setYRot(rotlerp(getYRot(), yRotD, yMax));
/*      */   }
/*      */   
/*      */   private float rotlerp(float a, float b, float max) {
/*  850 */     float diff = Mth.wrapDegrees(b - a);
/*  851 */     if (diff > max) {
/*  852 */       diff = max;
/*      */     }
/*  854 */     if (diff < -max) {
/*  855 */       diff = -max;
/*      */     }
/*  857 */     return a + diff;
/*      */   }
/*      */   
/*      */   public static boolean checkMobSpawnRules(EntityType<? extends Mob> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/*  861 */     BlockPos below = pos.below();
/*  862 */     return (EntitySpawnReason.isSpawner(spawnReason) || level.getBlockState(below).isValidSpawn(level, below, type));
/*      */   }
/*      */ 
/*      */   
/*  866 */   public boolean checkSpawnRules(LevelAccessor level, EntitySpawnReason spawnReason) { return true; }
/*      */ 
/*      */ 
/*      */   
/*  870 */   public boolean checkSpawnObstruction(LevelReader level) { return (!level.containsAnyLiquid(getBoundingBox()) && level.isUnobstructed(this)); }
/*      */ 
/*      */ 
/*      */   
/*  874 */   public int getMaxSpawnClusterSize() { return 4; }
/*      */ 
/*      */ 
/*      */   
/*  878 */   public boolean isMaxGroupSizeReached(int groupSize) { return false; }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxFallDistance() {
/*  883 */     if (getTarget() == null) {
/*  884 */       return getComfortableFallDistance(0.0F);
/*      */     }
/*  886 */     int sacrifice = (int)(getHealth() - getMaxHealth() * 0.33F);
/*  887 */     sacrifice -= (3 - level().getDifficulty().getId()) * 4;
/*  888 */     if (sacrifice < 0) {
/*  889 */       sacrifice = 0;
/*      */     }
/*  891 */     return getComfortableFallDistance(sacrifice);
/*      */   }
/*      */ 
/*      */   
/*  895 */   public ItemStack getBodyArmorItem() { return getItemBySlot(EquipmentSlot.BODY); }
/*      */ 
/*      */ 
/*      */   
/*  899 */   public boolean isSaddled() { return hasValidEquippableItemForSlot(EquipmentSlot.SADDLE); }
/*      */ 
/*      */ 
/*      */   
/*  903 */   public boolean isWearingBodyArmor() { return hasValidEquippableItemForSlot(EquipmentSlot.BODY); }
/*      */ 
/*      */ 
/*      */   
/*  907 */   private boolean hasValidEquippableItemForSlot(EquipmentSlot slot) { return (hasItemInSlot(slot) && isEquippableInSlot(getItemBySlot(slot), slot)); }
/*      */ 
/*      */ 
/*      */   
/*  911 */   public void setBodyArmorItem(ItemStack item) { setItemSlotAndDropWhenKilled(EquipmentSlot.BODY, item); }
/*      */ 
/*      */   
/*      */   public Container createEquipmentSlotContainer(final EquipmentSlot slot) {
/*  915 */     return new ContainerSingleItem()
/*      */       {
/*      */         public ItemStack getTheItem() {
/*  918 */           return Mob.this.getItemBySlot(slot);
/*      */         }
/*      */ 
/*      */         
/*      */         public void setTheItem(ItemStack itemStack) {
/*  923 */           Mob.this.setItemSlot(slot, itemStack);
/*  924 */           if (!itemStack.isEmpty()) {
/*  925 */             Mob.this.setGuaranteedDrop(slot);
/*  926 */             Mob.this.setPersistenceRequired();
/*      */           } 
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public void setChanged() {}
/*      */ 
/*      */ 
/*      */         
/*  936 */         public boolean stillValid(Player player) { return (player.getVehicle() == Mob.this || player.isWithinEntityInteractionRange(Mob.this, 4.0D)); }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
/*  943 */     super.dropCustomDeathLoot(level, source, killedByPlayer);
/*  944 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/*  945 */       ItemStack itemStack = getItemBySlot(slot);
/*  946 */       float dropChance = this.dropChances.byEquipment(slot);
/*  947 */       if (dropChance == 0.0F) {
/*      */         continue;
/*      */       }
/*      */       
/*  951 */       boolean preserve = this.dropChances.isPreserved(slot);
/*  952 */       Entity entity = source.getEntity(); if (entity instanceof LivingEntity) { LivingEntity livingSource = (LivingEntity)entity; Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1;
/*  953 */           dropChance = EnchantmentHelper.processEquipmentDropChance(serverLevel, livingSource, source, dropChance); }
/*      */          }
/*      */       
/*  956 */       if (!itemStack.isEmpty() && 
/*  957 */         !EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP) && (killedByPlayer || preserve) && this.random
/*      */         
/*  959 */         .nextFloat() < dropChance) {
/*      */         
/*  961 */         if (!preserve && itemStack.isDamageableItem()) {
/*  962 */           itemStack.setDamageValue(itemStack.getMaxDamage() - this.random.nextInt(1 + this.random.nextInt(Math.max(itemStack.getMaxDamage() - 3, 1))));
/*      */         }
/*  964 */         spawnAtLocation(level, itemStack);
/*  965 */         setItemSlot(slot, ItemStack.EMPTY);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  971 */   public DropChances getDropChances() { return this.dropChances; }
/*      */ 
/*      */ 
/*      */   
/*  975 */   public void dropPreservedEquipment(ServerLevel level) { dropPreservedEquipment(level, stack -> true); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<EquipmentSlot> dropPreservedEquipment(ServerLevel level, Predicate<ItemStack> shouldDrop) {
/*  986 */     Set<EquipmentSlot> slotsPreventedFromDropping = new HashSet<EquipmentSlot>();
/*  987 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/*  988 */       ItemStack itemStack = getItemBySlot(slot);
/*  989 */       if (itemStack.isEmpty()) {
/*      */         continue;
/*      */       }
/*  992 */       if (!shouldDrop.test(itemStack)) {
/*  993 */         slotsPreventedFromDropping.add(slot);
/*      */         
/*      */         continue;
/*      */       } 
/*  997 */       if (this.dropChances.isPreserved(slot)) {
/*  998 */         setItemSlot(slot, ItemStack.EMPTY);
/*  999 */         spawnAtLocation(level, itemStack);
/*      */       } 
/*      */     } 
/* 1002 */     return slotsPreventedFromDropping;
/*      */   }
/*      */   
/*      */   private LootParams createEquipmentParams(ServerLevel serverLevel) {
/* 1006 */     return (new LootParams.Builder(serverLevel))
/* 1007 */       .withParameter(LootContextParams.ORIGIN, position())
/* 1008 */       .withParameter(LootContextParams.THIS_ENTITY, this)
/* 1009 */       .create(LootContextParamSets.EQUIPMENT);
/*      */   }
/*      */ 
/*      */   
/* 1013 */   public void equip(EquipmentTable equipment) { equip(equipment.lootTable(), equipment.slotDropChances()); }
/*      */ 
/*      */   
/*      */   public void equip(ResourceKey<LootTable> lootTable, Map<EquipmentSlot, Float> dropChances) {
/* 1017 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 1018 */       equip(lootTable, createEquipmentParams(serverLevel), dropChances); }
/*      */   
/*      */   }
/*      */   
/*      */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/* 1023 */     if (random.nextFloat() < 0.15F * difficulty.getSpecialMultiplier()) {
/* 1024 */       int armorType = random.nextInt(3);
/* 1025 */       for (int i = 1; i <= 3.0F; i++) {
/* 1026 */         if (random.nextFloat() < 0.1087F) {
/* 1027 */           armorType++;
/*      */         }
/*      */       } 
/*      */       
/* 1031 */       float partialChance = (level().getDifficulty() == Difficulty.HARD) ? 0.1F : 0.25F;
/* 1032 */       boolean first = true;
/* 1033 */       for (EquipmentSlot slot : EQUIPMENT_POPULATION_ORDER) {
/* 1034 */         ItemStack itemStack = getItemBySlot(slot);
/* 1035 */         if (!first && random.nextFloat() < partialChance) {
/*      */           break;
/*      */         }
/* 1038 */         first = false;
/* 1039 */         if (itemStack.isEmpty()) {
/* 1040 */           Item equip = getEquipmentForSlot(slot, armorType);
/* 1041 */           if (equip != null) {
/* 1042 */             setItemSlot(slot, new ItemStack(equip));
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static Item getEquipmentForSlot(EquipmentSlot slot, int type) {
/* 1050 */     switch (slot) {
/*      */       case HEAD:
/* 1052 */         if (type == 0) {
/* 1053 */           return Items.LEATHER_HELMET;
/*      */         }
/* 1055 */         if (type == 1) {
/* 1056 */           return Items.COPPER_HELMET;
/*      */         }
/* 1058 */         if (type == 2) {
/* 1059 */           return Items.GOLDEN_HELMET;
/*      */         }
/* 1061 */         if (type == 3) {
/* 1062 */           return Items.CHAINMAIL_HELMET;
/*      */         }
/* 1064 */         if (type == 4) {
/* 1065 */           return Items.IRON_HELMET;
/*      */         }
/* 1067 */         if (type == 5) {
/* 1068 */           return Items.DIAMOND_HELMET;
/*      */         }
/*      */       case CHEST:
/* 1071 */         if (type == 0) {
/* 1072 */           return Items.LEATHER_CHESTPLATE;
/*      */         }
/* 1074 */         if (type == 1) {
/* 1075 */           return Items.COPPER_CHESTPLATE;
/*      */         }
/* 1077 */         if (type == 2) {
/* 1078 */           return Items.GOLDEN_CHESTPLATE;
/*      */         }
/* 1080 */         if (type == 3) {
/* 1081 */           return Items.CHAINMAIL_CHESTPLATE;
/*      */         }
/* 1083 */         if (type == 4) {
/* 1084 */           return Items.IRON_CHESTPLATE;
/*      */         }
/* 1086 */         if (type == 5) {
/* 1087 */           return Items.DIAMOND_CHESTPLATE;
/*      */         }
/*      */       case LEGS:
/* 1090 */         if (type == 0) {
/* 1091 */           return Items.LEATHER_LEGGINGS;
/*      */         }
/* 1093 */         if (type == 1) {
/* 1094 */           return Items.COPPER_LEGGINGS;
/*      */         }
/* 1096 */         if (type == 2) {
/* 1097 */           return Items.GOLDEN_LEGGINGS;
/*      */         }
/* 1099 */         if (type == 3) {
/* 1100 */           return Items.CHAINMAIL_LEGGINGS;
/*      */         }
/* 1102 */         if (type == 4) {
/* 1103 */           return Items.IRON_LEGGINGS;
/*      */         }
/* 1105 */         if (type == 5) {
/* 1106 */           return Items.DIAMOND_LEGGINGS;
/*      */         }
/*      */       case FEET:
/* 1109 */         if (type == 0) {
/* 1110 */           return Items.LEATHER_BOOTS;
/*      */         }
/* 1112 */         if (type == 1) {
/* 1113 */           return Items.COPPER_BOOTS;
/*      */         }
/* 1115 */         if (type == 2) {
/* 1116 */           return Items.GOLDEN_BOOTS;
/*      */         }
/* 1118 */         if (type == 3) {
/* 1119 */           return Items.CHAINMAIL_BOOTS;
/*      */         }
/* 1121 */         if (type == 4) {
/* 1122 */           return Items.IRON_BOOTS;
/*      */         }
/* 1124 */         if (type == 5) {
/* 1125 */           return Items.DIAMOND_BOOTS;
/*      */         }
/*      */         break;
/*      */     } 
/* 1129 */     return null;
/*      */   }
/*      */   
/*      */   protected void populateDefaultEquipmentEnchantments(ServerLevelAccessor level, RandomSource random, DifficultyInstance localDifficulty) {
/* 1133 */     enchantSpawnedWeapon(level, random, localDifficulty);
/*      */     
/* 1135 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 1136 */       if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) {
/*      */         continue;
/*      */       }
/* 1139 */       enchantSpawnedArmor(level, random, slot, localDifficulty);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 1144 */   protected void enchantSpawnedWeapon(ServerLevelAccessor level, RandomSource random, DifficultyInstance difficulty) { enchantSpawnedEquipment(level, EquipmentSlot.MAINHAND, random, 0.25F, difficulty); }
/*      */ 
/*      */ 
/*      */   
/* 1148 */   protected void enchantSpawnedArmor(ServerLevelAccessor level, RandomSource random, EquipmentSlot slot, DifficultyInstance difficulty) { enchantSpawnedEquipment(level, slot, random, 0.5F, difficulty); }
/*      */ 
/*      */   
/*      */   private void enchantSpawnedEquipment(ServerLevelAccessor level, EquipmentSlot slot, RandomSource random, float chance, DifficultyInstance difficulty) {
/* 1152 */     ItemStack itemStack = getItemBySlot(slot);
/* 1153 */     if (!itemStack.isEmpty() && random.nextFloat() < chance * difficulty.getSpecialMultiplier()) {
/* 1154 */       EnchantmentHelper.enchantItemFromProvider(itemStack, level.registryAccess(), VanillaEnchantmentProviders.MOB_SPAWN_EQUIPMENT, difficulty, random);
/* 1155 */       setItemSlot(slot, itemStack);
/*      */     } 
/*      */   }
/*      */   
/*      */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 1160 */     RandomSource random = level.getRandom();
/* 1161 */     AttributeInstance followRange = (AttributeInstance)Objects.requireNonNull(getAttribute(Attributes.FOLLOW_RANGE));
/* 1162 */     if (!followRange.hasModifier(RANDOM_SPAWN_BONUS_ID)) {
/* 1163 */       followRange.addPermanentModifier(new AttributeModifier(RANDOM_SPAWN_BONUS_ID, random.triangle(0.0D, 0.11485000000000001D), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
/*      */     }
/*      */     
/* 1166 */     setLeftHanded((random.nextFloat() < 0.05F));
/*      */     
/* 1168 */     return groupData;
/*      */   }
/*      */ 
/*      */   
/* 1172 */   public void setPersistenceRequired() { this.persistenceRequired = true; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1177 */   public void setDropChance(EquipmentSlot slot, float percent) { this.dropChances = this.dropChances.withEquipmentChance(slot, percent); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1182 */   public boolean canPickUpLoot() { return this.canPickUpLoot; }
/*      */ 
/*      */ 
/*      */   
/* 1186 */   public void setCanPickUpLoot(boolean canPickUpLoot) { this.canPickUpLoot = canPickUpLoot; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1191 */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) { return canPickUpLoot(); }
/*      */ 
/*      */ 
/*      */   
/* 1195 */   public boolean isPersistenceRequired() { return this.persistenceRequired; }
/*      */ 
/*      */ 
/*      */   
/*      */   public InteractionResult interact(Player player, InteractionHand hand) {
/* 1200 */     if (!isAlive()) {
/* 1201 */       return InteractionResult.PASS;
/*      */     }
/*      */     
/* 1204 */     InteractionResult interactionResult = checkAndHandleImportantInteractions(player, hand);
/* 1205 */     if (interactionResult.consumesAction()) {
/* 1206 */       gameEvent(GameEvent.ENTITY_INTERACT, player);
/* 1207 */       return interactionResult;
/*      */     } 
/* 1209 */     InteractionResult superReaction = super.interact(player, hand);
/* 1210 */     if (superReaction != InteractionResult.PASS) {
/* 1211 */       return superReaction;
/*      */     }
/*      */     
/* 1214 */     interactionResult = mobInteract(player, hand);
/* 1215 */     if (interactionResult.consumesAction()) {
/* 1216 */       gameEvent(GameEvent.ENTITY_INTERACT, player);
/* 1217 */       return interactionResult;
/*      */     } 
/*      */     
/* 1220 */     return InteractionResult.PASS;
/*      */   }
/*      */   
/*      */   private InteractionResult checkAndHandleImportantInteractions(Player player, InteractionHand hand) {
/* 1224 */     ItemStack itemStack = player.getItemInHand(hand);
/*      */     
/* 1226 */     if (itemStack.is(Items.NAME_TAG)) {
/* 1227 */       InteractionResult nameTagInteractionResult = itemStack.interactLivingEntity(player, this, hand);
/* 1228 */       if (nameTagInteractionResult.consumesAction()) {
/* 1229 */         return nameTagInteractionResult;
/*      */       }
/*      */     } 
/*      */     
/* 1233 */     Item item = itemStack.getItem(); if (item instanceof SpawnEggItem) { SpawnEggItem egg = (SpawnEggItem)item;
/* 1234 */       if (level() instanceof ServerLevel) {
/* 1235 */         Optional<Mob> offspring = egg.spawnOffspringFromSpawnEgg(player, this, getType(), (ServerLevel)level(), position(), itemStack);
/* 1236 */         offspring.ifPresent(mob -> onOffspringSpawnedFromEgg(player, mob));
/* 1237 */         if (offspring.isEmpty()) {
/* 1238 */           return InteractionResult.PASS;
/*      */         }
/*      */       } 
/*      */       
/* 1242 */       return InteractionResult.SUCCESS_SERVER; }
/*      */     
/* 1244 */     return InteractionResult.PASS;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onOffspringSpawnedFromEgg(Player spawner, Mob offspring) {}
/*      */ 
/*      */   
/* 1251 */   protected InteractionResult mobInteract(Player player, InteractionHand hand) { return InteractionResult.PASS; }
/*      */ 
/*      */   
/*      */   protected void usePlayerItem(Player player, InteractionHand hand, ItemStack itemStack) {
/* 1255 */     int beforeUseCount = itemStack.getCount();
/* 1256 */     UseRemainder useRemainder = (UseRemainder)itemStack.get(DataComponents.USE_REMAINDER);
/*      */     
/* 1258 */     itemStack.consume(1, player);
/* 1259 */     if (useRemainder != null) {
/* 1260 */       Objects.requireNonNull(player); ItemStack newHandStack = useRemainder.convertIntoRemainder(itemStack, beforeUseCount, player.hasInfiniteMaterials(), player::handleExtraItemsCreatedOnUse);
/* 1261 */       player.setItemInHand(hand, newHandStack);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1267 */   public boolean isWithinHome() { return isWithinHome(blockPosition()); }
/*      */ 
/*      */   
/*      */   public boolean isWithinHome(BlockPos pos) {
/* 1271 */     if (this.homeRadius == -1) {
/* 1272 */       return true;
/*      */     }
/* 1274 */     return (this.homePosition.distSqr(pos) < (this.homeRadius * this.homeRadius));
/*      */   }
/*      */   
/*      */   public boolean isWithinHome(Vec3 pos) {
/* 1278 */     if (this.homeRadius == -1) {
/* 1279 */       return true;
/*      */     }
/* 1281 */     return (this.homePosition.distToCenterSqr(pos) < (this.homeRadius * this.homeRadius));
/*      */   }
/*      */   
/*      */   public void setHomeTo(BlockPos newCenter, int radius) {
/* 1285 */     this.homePosition = newCenter;
/* 1286 */     this.homeRadius = radius;
/*      */   }
/*      */ 
/*      */   
/* 1290 */   public BlockPos getHomePosition() { return this.homePosition; }
/*      */ 
/*      */ 
/*      */   
/* 1294 */   public int getHomeRadius() { return this.homeRadius; }
/*      */ 
/*      */ 
/*      */   
/* 1298 */   public void clearHome() { this.homeRadius = -1; }
/*      */ 
/*      */ 
/*      */   
/* 1302 */   public boolean hasHome() { return (this.homeRadius != -1); }
/*      */ 
/*      */   
/*      */   public <T extends Mob> T convertTo(EntityType<T> entityType, ConversionParams params, EntitySpawnReason spawnReason, ConversionParams.AfterConversion<T> afterConversion) {
/* 1306 */     if (isRemoved()) {
/* 1307 */       return null;
/*      */     }
/*      */     
/* 1310 */     T newMob = (T)(Mob)entityType.create(level(), spawnReason);
/* 1311 */     if (newMob == null) {
/* 1312 */       return null;
/*      */     }
/*      */     
/* 1315 */     params.type().convert(this, newMob, params);
/* 1316 */     afterConversion.finalizeConversion(newMob);
/*      */     
/* 1318 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 1319 */       serverLevel.addFreshEntity(newMob); }
/*      */ 
/*      */     
/* 1322 */     if (params.type().shouldDiscardAfterConversion()) {
/* 1323 */       discard();
/*      */     }
/* 1325 */     return newMob;
/*      */   }
/*      */ 
/*      */   
/* 1329 */   public <T extends Mob> T convertTo(EntityType<T> entityType, ConversionParams params, ConversionParams.AfterConversion<T> afterConversion) { return (T)convertTo(entityType, params, EntitySpawnReason.CONVERSION, afterConversion); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1334 */   public Leashable.LeashData getLeashData() { return this.leashData; }
/*      */ 
/*      */   
/*      */   private void resetAngularLeashMomentum() {
/* 1338 */     if (this.leashData != null) {
/* 1339 */       this.leashData.angularMomentum = 0.0D;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1345 */   public void setLeashData(Leashable.LeashData leashData) { this.leashData = leashData; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onLeashRemoved() {
/* 1350 */     if (getLeashData() == null) {
/* 1351 */       clearHome();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void leashTooFarBehaviour() {
/* 1357 */     super.leashTooFarBehaviour();
/* 1358 */     this.goalSelector.disableControlFlag(Goal.Flag.MOVE);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1363 */   public boolean canBeLeashed() { return !(this instanceof net.minecraft.world.entity.monster.Enemy); }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean startRiding(Entity entity, boolean force, boolean sendEventAndTriggers) {
/* 1368 */     boolean result = super.startRiding(entity, force, sendEventAndTriggers);
/* 1369 */     if (result && isLeashed())
/*      */     {
/* 1371 */       dropLeash();
/*      */     }
/*      */     
/* 1374 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1379 */   public boolean isEffectiveAi() { return (super.isEffectiveAi() && !isNoAi()); }
/*      */ 
/*      */   
/*      */   public void setNoAi(boolean flag) {
/* 1383 */     byte val = ((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue();
/* 1384 */     this.entityData.set(DATA_MOB_FLAGS_ID, Byte.valueOf(flag ? (byte)(val | true) : (byte)(val & 0xFFFFFFFE)));
/*      */   }
/*      */   
/*      */   public void setLeftHanded(boolean flag) {
/* 1388 */     byte val = ((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue();
/* 1389 */     this.entityData.set(DATA_MOB_FLAGS_ID, Byte.valueOf(flag ? (byte)(val | 0x2) : (byte)(val & 0xFFFFFFFD)));
/*      */   }
/*      */   
/*      */   public void setAggressive(boolean flag) {
/* 1393 */     byte val = ((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue();
/* 1394 */     this.entityData.set(DATA_MOB_FLAGS_ID, Byte.valueOf(flag ? (byte)(val | 0x4) : (byte)(val & 0xFFFFFFFB)));
/*      */   }
/*      */ 
/*      */   
/* 1398 */   public boolean isNoAi() { return ((((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue() & true) != 0); }
/*      */ 
/*      */ 
/*      */   
/* 1402 */   public boolean isLeftHanded() { return ((((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue() & 0x2) != 0); }
/*      */ 
/*      */ 
/*      */   
/* 1406 */   public boolean isAggressive() { return ((((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue() & 0x4) != 0); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBaby(boolean baby) {}
/*      */ 
/*      */ 
/*      */   
/* 1415 */   public HumanoidArm getMainArm() { return isLeftHanded() ? HumanoidArm.LEFT : HumanoidArm.RIGHT; }
/*      */   
/*      */   public boolean isWithinMeleeAttackRange(LivingEntity target) {
/*      */     double minRange, maxRange;
/* 1419 */     AttackRange attackRange = (AttackRange)getActiveItem().get(DataComponents.ATTACK_RANGE);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1424 */     if (attackRange == null) {
/* 1425 */       maxRange = DEFAULT_ATTACK_REACH;
/* 1426 */       minRange = 0.0D;
/*      */     } else {
/* 1428 */       maxRange = attackRange.effectiveMaxRange(this);
/* 1429 */       minRange = attackRange.effectiveMinRange(this);
/*      */     } 
/*      */     
/* 1432 */     AABB hitbox = target.getHitbox();
/*      */     
/* 1434 */     return (getAttackBoundingBox(maxRange).intersects(hitbox) && (minRange <= 0.0D || !getAttackBoundingBox(minRange).intersects(hitbox)));
/*      */   }
/*      */   
/*      */   protected AABB getAttackBoundingBox(double horizontalExpansion) {
/*      */     AABB aabb;
/* 1439 */     Entity vehicle = getVehicle();
/* 1440 */     if (vehicle != null) {
/* 1441 */       AABB mountAabb = vehicle.getBoundingBox();
/* 1442 */       AABB ownAabb = getBoundingBox();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1449 */       aabb = new AABB(Math.min(ownAabb.minX, mountAabb.minX), ownAabb.minY, Math.min(ownAabb.minZ, mountAabb.minZ), Math.max(ownAabb.maxX, mountAabb.maxX), ownAabb.maxY, Math.max(ownAabb.maxZ, mountAabb.maxZ));
/*      */     } else {
/*      */       
/* 1452 */       aabb = getBoundingBox();
/*      */     } 
/* 1454 */     return aabb.inflate(horizontalExpansion, 0.0D, horizontalExpansion);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/* 1462 */     float dmg = (float)getAttributeValue(Attributes.ATTACK_DAMAGE);
/*      */     
/* 1464 */     ItemStack weaponItem = getWeaponItem();
/*      */     
/* 1466 */     DamageSource damageSource = weaponItem.getDamageSource(this, () -> damageSources().mobAttack(this));
/*      */     
/* 1468 */     dmg = EnchantmentHelper.modifyDamage(level, weaponItem, target, damageSource, dmg);
/*      */     
/* 1470 */     dmg += weaponItem.getItem().getAttackDamageBonus(target, dmg, damageSource);
/*      */     
/* 1472 */     Vec3 oldMovement = target.getDeltaMovement();
/*      */     
/* 1474 */     boolean wasHurt = target.hurtServer(level, damageSource, dmg);
/*      */     
/* 1476 */     if (wasHurt) {
/* 1477 */       causeExtraKnockback(target, getKnockback(target, damageSource), oldMovement);
/*      */       
/* 1479 */       if (target instanceof LivingEntity) { LivingEntity livingTarget = (LivingEntity)target;
/* 1480 */         weaponItem.hurtEnemy(livingTarget, this); }
/*      */       
/* 1482 */       EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
/* 1483 */       setLastHurtMob(target);
/* 1484 */       playAttackSound();
/*      */     } 
/*      */     
/* 1487 */     lungeForwardMaybe();
/*      */     
/* 1489 */     return wasHurt;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void jumpInLiquid(TagKey<Fluid> type) {
/* 1494 */     if (getNavigation().canFloat()) {
/* 1495 */       super.jumpInLiquid(type);
/*      */     } else {
/* 1497 */       setDeltaMovement(getDeltaMovement().add(0.0D, 0.3D, 0.0D));
/*      */     } 
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   public void removeFreeWill() {
/* 1503 */     removeAllGoals(goal -> true);
/* 1504 */     getBrain().removeAllBehaviors();
/*      */   }
/*      */ 
/*      */   
/* 1508 */   public void removeAllGoals(Predicate<Goal> predicate) { this.goalSelector.removeAllGoals(predicate); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeAfterChangingDimensions() {
/* 1513 */     super.removeAfterChangingDimensions();
/* 1514 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 1515 */       ItemStack itemStack = getItemBySlot(slot);
/* 1516 */       if (!itemStack.isEmpty()) {
/* 1517 */         itemStack.setCount(0);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemStack getPickResult() {
/* 1524 */     SpawnEggItem egg = SpawnEggItem.byId(getType());
/* 1525 */     if (egg == null) {
/* 1526 */       return null;
/*      */     }
/* 1528 */     return new ItemStack(egg);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onAttributeUpdated(Holder<Attribute> attribute) {
/* 1533 */     super.onAttributeUpdated(attribute);
/*      */     
/* 1535 */     if (attribute.is(Attributes.FOLLOW_RANGE) || attribute.is(Attributes.TEMPT_RANGE)) {
/* 1536 */       getNavigation().updatePathfinderMaxVisitedNodes();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerDebugValues(ServerLevel level, DebugValueSource.Registration registration) {
/* 1542 */     registration.register(DebugSubscriptions.ENTITY_PATHS, () -> {
/* 1543 */           Path path = getNavigation().getPath();
/* 1544 */           if (path != null && path.debugData() != null) {
/* 1545 */             return new DebugPathInfo(path.copy(), getNavigation().getMaxDistanceToWaypoint());
/*      */           }
/* 1547 */           return null;
/*      */         });
/* 1549 */     registration.register(DebugSubscriptions.GOAL_SELECTORS, () -> {
/* 1550 */           Set<WrappedGoal> availableGoals = this.goalSelector.getAvailableGoals();
/* 1551 */           List<DebugGoalInfo.DebugGoal> goalInfo = new ArrayList<DebugGoalInfo.DebugGoal>(availableGoals.size());
/* 1552 */           availableGoals.forEach(());
/*      */ 
/*      */           
/* 1555 */           return new DebugGoalInfo(goalInfo);
/*      */         });
/* 1557 */     if (!this.brain.isBrainDead()) {
/* 1558 */       registration.register(DebugSubscriptions.BRAINS, () -> DebugBrainDump.takeBrainDump(level, this));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 1563 */   public float chargeSpeedModifier() { return 1.0F; }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\Mob.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */