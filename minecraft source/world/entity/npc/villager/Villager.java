/*      */ package net.minecraft.world.entity.npc.villager;
/*      */ 
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.Dynamic;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.function.BiPredicate;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.GlobalPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.component.DataComponentGetter;
/*      */ import net.minecraft.core.component.DataComponentType;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.SpawnUtil;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.SimpleContainer;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.AgeableMob;
/*      */ import net.minecraft.world.entity.ConversionParams;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.ExperienceOrb;
/*      */ import net.minecraft.world.entity.LightningBolt;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.ReputationEventHandler;
/*      */ import net.minecraft.world.entity.SpawnGroupData;
/*      */ import net.minecraft.world.entity.ai.Brain;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.behavior.VillagerGoalPackages;
/*      */ import net.minecraft.world.entity.ai.gossip.GossipContainer;
/*      */ import net.minecraft.world.entity.ai.gossip.GossipType;
/*      */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*      */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*      */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*      */ import net.minecraft.world.entity.ai.sensing.GolemSensor;
/*      */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*      */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*      */ import net.minecraft.world.entity.ai.village.ReputationEventType;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.monster.Witch;
/*      */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.raid.Raid;
/*      */ import net.minecraft.world.entity.schedule.Activity;
/*      */ import net.minecraft.world.flag.FeatureFlags;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.trading.MerchantOffer;
/*      */ import net.minecraft.world.item.trading.MerchantOffers;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class Villager
/*      */   extends AbstractVillager
/*      */   implements VillagerDataHolder, ReputationEventHandler {
/*   98 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*  100 */   private static final EntityDataAccessor<VillagerData> DATA_VILLAGER_DATA = SynchedEntityData.defineId(Villager.class, EntityDataSerializers.VILLAGER_DATA);
/*      */   
/*      */   public static final int BREEDING_FOOD_THRESHOLD = 12;
/*  103 */   public static final Map<Item, Integer> FOOD_POINTS = ImmutableMap.of(Items.BREAD, 
/*  104 */       Integer.valueOf(4), Items.POTATO, 
/*  105 */       Integer.valueOf(1), Items.CARROT, 
/*  106 */       Integer.valueOf(1), Items.BEETROOT, 
/*  107 */       Integer.valueOf(1));
/*      */   
/*      */   private static final int TRADES_PER_LEVEL = 2;
/*      */   
/*      */   private static final int MAX_GOSSIP_TOPICS = 10;
/*      */   
/*      */   private static final int GOSSIP_COOLDOWN = 1200;
/*      */   
/*      */   private static final int GOSSIP_DECAY_INTERVAL = 24000;
/*      */   
/*      */   private static final int HOW_FAR_AWAY_TO_TALK_TO_OTHER_VILLAGERS_ABOUT_GOLEMS = 10;
/*      */   
/*      */   private static final int HOW_MANY_VILLAGERS_NEED_TO_AGREE_TO_SPAWN_A_GOLEM = 5;
/*      */   
/*      */   private static final long TIME_SINCE_SLEEPING_FOR_GOLEM_SPAWNING = 24000L;
/*      */   
/*      */   @VisibleForTesting
/*      */   public static final float SPEED_MODIFIER = 0.5F;
/*      */   
/*      */   private static final int DEFAULT_XP = 0;
/*      */   private static final byte DEFAULT_FOOD_LEVEL = 0;
/*      */   private static final int DEFAULT_LAST_RESTOCK = 0;
/*      */   private static final int DEFAULT_LAST_GOSSIP_DECAY = 0;
/*      */   private static final int DEFAULT_RESTOCKS_TODAY = 0;
/*      */   private static final boolean DEFAULT_ASSIGN_PROFESSION_WHEN_SPAWNED = false;
/*      */   private int updateMerchantTimer;
/*      */   private boolean increaseProfessionLevelOnUpdate;
/*      */   private Player lastTradedPlayer;
/*      */   private boolean chasing;
/*  136 */   private int foodLevel = 0;
/*      */   
/*  138 */   private final GossipContainer gossips = new GossipContainer();
/*      */   
/*      */   private long lastGossipTime;
/*  141 */   private long lastGossipDecayTime = 0L;
/*      */   
/*  143 */   private int villagerXp = 0;
/*  144 */   private long lastRestockGameTime = 0L;
/*  145 */   private int numberOfRestocksToday = 0;
/*      */   
/*      */   private long lastRestockCheckDay;
/*      */   private boolean assignProfessionWhenSpawned = false;
/*  149 */   private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.HOME, MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleType.MEETING_POINT, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, new MemoryModuleType[] { MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.BREED_TARGET, MemoryModuleType.PATH, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleType.HIDING_PLACE, MemoryModuleType.HEARD_BELL_TIME, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LAST_SLEPT, MemoryModuleType.LAST_WOKEN, MemoryModuleType.LAST_WORKED_AT_POI, MemoryModuleType.GOLEM_DETECTED_RECENTLY });
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  182 */   private static final ImmutableList<SensorType<? extends Sensor<? super Villager>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_BED, SensorType.HURT_BY, SensorType.VILLAGER_HOSTILES, SensorType.VILLAGER_BABIES, SensorType.SECONDARY_POIS, SensorType.GOLEM_DETECTED);
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
/*  194 */   public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<Villager, Holder<PoiType>>> POI_MEMORIES = ImmutableMap.of(MemoryModuleType.HOME, (villager, poiType) -> 
/*  195 */       poiType.is(PoiTypes.HOME), MemoryModuleType.JOB_SITE, (villager, poiType) -> (
/*  196 */       (VillagerProfession)villager.getVillagerData().profession().value()).heldJobSite().test(poiType), MemoryModuleType.POTENTIAL_JOB_SITE, (villager, poiType) -> 
/*  197 */       VillagerProfession.ALL_ACQUIRABLE_JOBS.test(poiType), MemoryModuleType.MEETING_POINT, (villager, poiType) -> 
/*  198 */       poiType.is(PoiTypes.MEETING));
/*      */ 
/*      */ 
/*      */   
/*  202 */   public Villager(EntityType<? extends Villager> type, Level level) { this(type, level, VillagerType.PLAINS); }
/*      */ 
/*      */ 
/*      */   
/*  206 */   public Villager(EntityType<? extends Villager> entityType, Level level, ResourceKey<VillagerType> type) { this(entityType, level, level.registryAccess().getOrThrow(type)); }
/*      */ 
/*      */   
/*      */   public Villager(EntityType<? extends Villager> entityType, Level level, Holder<VillagerType> type) {
/*  210 */     super(entityType, level);
/*  211 */     getNavigation().setCanOpenDoors(true);
/*  212 */     getNavigation().setCanFloat(true);
/*  213 */     getNavigation().setRequiredPathLength(48.0F);
/*  214 */     setCanPickUpLoot(true);
/*  215 */     setVillagerData(getVillagerData().withType(type).withProfession(level.registryAccess(), VillagerProfession.NONE));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  221 */   public Brain<Villager> getBrain() { return super.getBrain(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  226 */   protected Brain.Provider<Villager> brainProvider() { return Brain.provider(MEMORY_TYPES, SENSOR_TYPES); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected Brain<?> makeBrain(Dynamic<?> input) {
/*  231 */     Brain<Villager> brain = brainProvider().makeBrain(input);
/*  232 */     registerBrainGoals(brain);
/*  233 */     return brain;
/*      */   }
/*      */   
/*      */   public void refreshBrain(ServerLevel level) {
/*  237 */     Brain<Villager> oldBrain = getBrain();
/*  238 */     oldBrain.stopAll(level, this);
/*  239 */     this.brain = oldBrain.copyWithoutBehaviors();
/*  240 */     registerBrainGoals(getBrain());
/*      */   }
/*      */   
/*      */   private void registerBrainGoals(Brain<Villager> brain) {
/*  244 */     Holder<VillagerProfession> profession = getVillagerData().profession();
/*      */     
/*  246 */     if (isBaby()) {
/*  247 */       brain.setSchedule(EnvironmentAttributes.BABY_VILLAGER_ACTIVITY);
/*  248 */       brain.addActivity(Activity.PLAY, VillagerGoalPackages.getPlayPackage(0.5F));
/*      */     } else {
/*  250 */       brain.setSchedule(EnvironmentAttributes.VILLAGER_ACTIVITY);
/*  251 */       brain.addActivityWithConditions(Activity.WORK, VillagerGoalPackages.getWorkPackage(profession, 0.5F), ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT)));
/*      */     } 
/*      */     
/*  254 */     brain.addActivity(Activity.CORE, VillagerGoalPackages.getCorePackage(profession, 0.5F));
/*  255 */     brain.addActivityWithConditions(Activity.MEET, VillagerGoalPackages.getMeetPackage(profession, 0.5F), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT)));
/*  256 */     brain.addActivity(Activity.REST, VillagerGoalPackages.getRestPackage(profession, 0.5F));
/*  257 */     brain.addActivity(Activity.IDLE, VillagerGoalPackages.getIdlePackage(profession, 0.5F));
/*  258 */     brain.addActivity(Activity.PANIC, VillagerGoalPackages.getPanicPackage(profession, 0.5F));
/*  259 */     brain.addActivity(Activity.PRE_RAID, VillagerGoalPackages.getPreRaidPackage(profession, 0.5F));
/*  260 */     brain.addActivity(Activity.RAID, VillagerGoalPackages.getRaidPackage(profession, 0.5F));
/*  261 */     brain.addActivity(Activity.HIDE, VillagerGoalPackages.getHidePackage(profession, 0.5F));
/*  262 */     brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
/*  263 */     brain.setDefaultActivity(Activity.IDLE);
/*  264 */     brain.setActiveActivityIfPossible(Activity.IDLE);
/*  265 */     brain.updateActivityFromSchedule(level().environmentAttributes(), level().getGameTime(), position());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void ageBoundaryReached() {
/*  270 */     super.ageBoundaryReached();
/*  271 */     if (level() instanceof ServerLevel) {
/*  272 */       refreshBrain((ServerLevel)level());
/*      */     }
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createAttributes() {
/*  277 */     return Mob.createMobAttributes()
/*  278 */       .add(Attributes.MOVEMENT_SPEED, 0.5D);
/*      */   }
/*      */ 
/*      */   
/*  282 */   public boolean assignProfessionWhenSpawned() { return this.assignProfessionWhenSpawned; }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void customServerAiStep(ServerLevel level) {
/*  287 */     ProfilerFiller profiler = Profiler.get();
/*  288 */     profiler.push("villagerBrain");
/*  289 */     getBrain().tick(level, this);
/*  290 */     profiler.pop();
/*      */     
/*  292 */     if (this.assignProfessionWhenSpawned) {
/*  293 */       this.assignProfessionWhenSpawned = false;
/*      */     }
/*      */     
/*  296 */     if (!isTrading() && this.updateMerchantTimer > 0) {
/*  297 */       this.updateMerchantTimer--;
/*  298 */       if (this.updateMerchantTimer <= 0) {
/*  299 */         if (this.increaseProfessionLevelOnUpdate) {
/*  300 */           increaseMerchantCareer(level);
/*  301 */           this.increaseProfessionLevelOnUpdate = false;
/*      */         } 
/*  303 */         addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
/*      */       } 
/*      */     } 
/*      */     
/*  307 */     if (this.lastTradedPlayer != null) {
/*  308 */       level.onReputationEvent(ReputationEventType.TRADE, this.lastTradedPlayer, this);
/*  309 */       level.broadcastEntityEvent(this, (byte)14);
/*  310 */       this.lastTradedPlayer = null;
/*      */     } 
/*      */ 
/*      */     
/*  314 */     if (!isNoAi() && this.random.nextInt(100) == 0) {
/*  315 */       Raid raid = level.getRaidAt(blockPosition());
/*  316 */       if (raid != null && raid.isActive() && !raid.isOver()) {
/*  317 */         level.broadcastEntityEvent(this, (byte)42);
/*      */       }
/*      */     } 
/*      */     
/*  321 */     if (getVillagerData().profession().is(VillagerProfession.NONE) && isTrading()) {
/*  322 */       stopTrading();
/*      */     }
/*      */     
/*  325 */     super.customServerAiStep(level);
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  330 */     super.tick();
/*      */     
/*  332 */     if (getUnhappyCounter() > 0) {
/*  333 */       setUnhappyCounter(getUnhappyCounter() - 1);
/*      */     }
/*      */     
/*  336 */     maybeDecayGossip();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/*  342 */     ItemStack itemStack = player.getItemInHand(hand);
/*  343 */     if (!itemStack.is(Items.VILLAGER_SPAWN_EGG) && isAlive() && !isTrading() && !isSleeping()) {
/*  344 */       if (isBaby()) {
/*  345 */         setUnhappy();
/*      */         
/*  347 */         return InteractionResult.SUCCESS;
/*      */       } 
/*      */       
/*  350 */       if (!level().isClientSide()) {
/*  351 */         boolean noOffers = getOffers().isEmpty();
/*      */ 
/*      */         
/*  354 */         if (hand == InteractionHand.MAIN_HAND) {
/*  355 */           if (noOffers) {
/*  356 */             setUnhappy();
/*      */           }
/*  358 */           player.awardStat(Stats.TALKED_TO_VILLAGER);
/*      */         } 
/*      */         
/*  361 */         if (noOffers) {
/*  362 */           return InteractionResult.CONSUME;
/*      */         }
/*      */ 
/*      */         
/*  366 */         startTrading(player);
/*      */       } 
/*      */       
/*  369 */       return InteractionResult.SUCCESS;
/*      */     } 
/*  371 */     return super.mobInteract(player, hand);
/*      */   }
/*      */   
/*      */   private void setUnhappy() {
/*  375 */     setUnhappyCounter(40);
/*  376 */     if (!level().isClientSide()) {
/*  377 */       makeSound(SoundEvents.VILLAGER_NO);
/*      */     }
/*      */   }
/*      */   
/*      */   private void startTrading(Player player) {
/*  382 */     updateSpecialPrices(player);
/*  383 */     setTradingPlayer(player);
/*  384 */     openTradingScreen(player, getDisplayName(), getVillagerData().level());
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTradingPlayer(Player player) {
/*  389 */     boolean shouldStop = (getTradingPlayer() != null && player == null);
/*  390 */     super.setTradingPlayer(player);
/*  391 */     if (shouldStop) {
/*  392 */       stopTrading();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void stopTrading() {
/*  398 */     super.stopTrading();
/*  399 */     resetSpecialPrices();
/*      */   }
/*      */   
/*      */   private void resetSpecialPrices() {
/*  403 */     if (level().isClientSide()) {
/*      */       return;
/*      */     }
/*  406 */     for (MerchantOffer offer : getOffers()) {
/*  407 */       offer.resetSpecialPriceDiff();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  413 */   public boolean canRestock() { return true; }
/*      */ 
/*      */   
/*      */   public void restock() {
/*  417 */     updateDemand();
/*  418 */     for (MerchantOffer offer : getOffers()) {
/*  419 */       offer.resetUses();
/*      */     }
/*  421 */     resendOffersToTradingPlayer();
/*      */     
/*  423 */     this.lastRestockGameTime = level().getGameTime();
/*  424 */     this.numberOfRestocksToday++;
/*      */   }
/*      */   
/*      */   private void resendOffersToTradingPlayer() {
/*  428 */     MerchantOffers offers = getOffers();
/*  429 */     Player tradingPlayer = getTradingPlayer();
/*  430 */     if (tradingPlayer != null && !offers.isEmpty()) {
/*  431 */       tradingPlayer.sendMerchantOffers(tradingPlayer.containerMenu.containerId, offers, getVillagerData().level(), getVillagerXp(), showProgressBar(), canRestock());
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean needsToRestock() {
/*  436 */     for (MerchantOffer offer : getOffers()) {
/*  437 */       if (offer.needsRestock()) {
/*  438 */         return true;
/*      */       }
/*      */     } 
/*  441 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  446 */   private boolean allowedToRestock() { return (this.numberOfRestocksToday == 0 || (this.numberOfRestocksToday < 2 && level().getGameTime() > this.lastRestockGameTime + 2400L)); }
/*      */ 
/*      */   
/*      */   public boolean shouldRestock(ServerLevel level) {
/*  450 */     long halfDayPassedTime = this.lastRestockGameTime + 12000L;
/*  451 */     long gameTime = level().getGameTime();
/*  452 */     boolean isNewDay = (gameTime > halfDayPassedTime);
/*      */ 
/*      */ 
/*      */     
/*  456 */     long currentDay = level.getDayCount();
/*  457 */     isNewDay |= ((this.lastRestockCheckDay > 0L && currentDay > this.lastRestockCheckDay));
/*  458 */     this.lastRestockCheckDay = currentDay;
/*      */     
/*  460 */     if (isNewDay) {
/*  461 */       this.lastRestockGameTime = gameTime;
/*  462 */       resetNumberOfRestocks();
/*      */     } 
/*      */     
/*  465 */     return (allowedToRestock() && needsToRestock());
/*      */   }
/*      */ 
/*      */   
/*      */   private void catchUpDemand() {
/*  470 */     int missedUpdates = 2 - this.numberOfRestocksToday;
/*  471 */     if (missedUpdates > 0) {
/*  472 */       for (MerchantOffer offer : getOffers()) {
/*  473 */         offer.resetUses();
/*      */       }
/*      */     }
/*  476 */     for (int i = 0; i < missedUpdates; i++) {
/*  477 */       updateDemand();
/*      */     }
/*  479 */     resendOffersToTradingPlayer();
/*      */   }
/*      */   
/*      */   private void updateDemand() {
/*  483 */     for (MerchantOffer offer : getOffers()) {
/*  484 */       offer.updateDemand();
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateSpecialPrices(Player player) {
/*  489 */     int reputation = getPlayerReputation(player);
/*  490 */     if (reputation != 0) {
/*  491 */       for (MerchantOffer offer : getOffers()) {
/*  492 */         offer.addToSpecialPriceDiff(-Mth.floor(reputation * offer.getPriceMultiplier()));
/*      */       }
/*      */     }
/*      */     
/*  496 */     if (player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
/*  497 */       MobEffectInstance effect = player.getEffect(MobEffects.HERO_OF_THE_VILLAGE);
/*  498 */       int amplifier = effect.getAmplifier();
/*  499 */       for (MerchantOffer offer : getOffers()) {
/*  500 */         double modifier = 0.3D + 0.0625D * amplifier;
/*  501 */         int costReduction = (int)Math.floor(modifier * offer.getBaseCostA().getCount());
/*  502 */         offer.addToSpecialPriceDiff(-Math.max(costReduction, 1));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  509 */     super.defineSynchedData(entityData);
/*  510 */     entityData.define(DATA_VILLAGER_DATA, createDefaultVillagerData());
/*      */   }
/*      */ 
/*      */   
/*  514 */   public static VillagerData createDefaultVillagerData() { return new VillagerData(BuiltInRegistries.VILLAGER_TYPE.getOrThrow(VillagerType.PLAINS), BuiltInRegistries.VILLAGER_PROFESSION.getOrThrow(VillagerProfession.NONE), 1); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  519 */     super.addAdditionalSaveData(output);
/*  520 */     output.store("VillagerData", VillagerData.CODEC, getVillagerData());
/*      */     
/*  522 */     output.putByte("FoodLevel", (byte)this.foodLevel);
/*  523 */     output.store("Gossips", GossipContainer.CODEC, this.gossips);
/*  524 */     output.putInt("Xp", this.villagerXp);
/*  525 */     output.putLong("LastRestock", this.lastRestockGameTime);
/*  526 */     output.putLong("LastGossipDecay", this.lastGossipDecayTime);
/*  527 */     output.putInt("RestocksToday", this.numberOfRestocksToday);
/*  528 */     if (this.assignProfessionWhenSpawned) {
/*  529 */       output.putBoolean("AssignProfessionWhenSpawned", true);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  535 */     super.readAdditionalSaveData(input);
/*      */     
/*  537 */     this.entityData.set(DATA_VILLAGER_DATA, (VillagerData)input.read("VillagerData", VillagerData.CODEC).orElseGet(Villager::createDefaultVillagerData));
/*      */     
/*  539 */     this.foodLevel = input.getByteOr("FoodLevel", (byte)0);
/*      */     
/*  541 */     this.gossips.clear();
/*  542 */     Objects.requireNonNull(this.gossips); input.read("Gossips", GossipContainer.CODEC).ifPresent(this.gossips::putAll);
/*      */     
/*  544 */     this.villagerXp = input.getIntOr("Xp", 0);
/*      */     
/*  546 */     this.lastRestockGameTime = input.getLongOr("LastRestock", 0L);
/*      */     
/*  548 */     this.lastGossipDecayTime = input.getLongOr("LastGossipDecay", 0L);
/*      */ 
/*      */     
/*  551 */     if (level() instanceof ServerLevel) {
/*  552 */       refreshBrain((ServerLevel)level());
/*      */     }
/*      */     
/*  555 */     this.numberOfRestocksToday = input.getIntOr("RestocksToday", 0);
/*      */     
/*  557 */     this.assignProfessionWhenSpawned = input.getBooleanOr("AssignProfessionWhenSpawned", false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  562 */   public boolean removeWhenFarAway(double distSqr) { return false; }
/*      */ 
/*      */ 
/*      */   
/*      */   protected SoundEvent getAmbientSound() {
/*  567 */     if (isSleeping()) {
/*  568 */       return null;
/*      */     }
/*      */     
/*  571 */     if (isTrading()) {
/*  572 */       return SoundEvents.VILLAGER_TRADE;
/*      */     }
/*  574 */     return SoundEvents.VILLAGER_AMBIENT;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  579 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.VILLAGER_HURT; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  584 */   protected SoundEvent getDeathSound() { return SoundEvents.VILLAGER_DEATH; }
/*      */ 
/*      */ 
/*      */   
/*  588 */   public void playWorkSound() { makeSound(((VillagerProfession)getVillagerData().profession().value()).workSound()); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVillagerData(VillagerData data) {
/*  593 */     VillagerData currentData = getVillagerData();
/*  594 */     if (!currentData.profession().equals(data.profession())) {
/*  595 */       this.offers = null;
/*      */     }
/*      */     
/*  598 */     this.entityData.set(DATA_VILLAGER_DATA, data);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  603 */   public VillagerData getVillagerData() { return (VillagerData)this.entityData.get(DATA_VILLAGER_DATA); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void rewardTradeXp(MerchantOffer offer) {
/*  608 */     int popXp = 3 + this.random.nextInt(4);
/*      */     
/*  610 */     this.villagerXp += offer.getXp();
/*  611 */     this.lastTradedPlayer = getTradingPlayer();
/*      */     
/*  613 */     if (shouldIncreaseLevel()) {
/*  614 */       this.updateMerchantTimer = 40;
/*  615 */       this.increaseProfessionLevelOnUpdate = true;
/*  616 */       popXp += 5;
/*      */     } 
/*      */     
/*  619 */     if (offer.shouldRewardExp()) {
/*  620 */       level().addFreshEntity(new ExperienceOrb(level(), getX(), getY() + 0.5D, getZ(), popXp));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastHurtByMob(LivingEntity hurtBy) {
/*  627 */     if (hurtBy != null && level() instanceof ServerLevel) {
/*  628 */       ((ServerLevel)level()).onReputationEvent(ReputationEventType.VILLAGER_HURT, hurtBy, this);
/*  629 */       if (isAlive() && hurtBy instanceof Player) {
/*  630 */         level().broadcastEntityEvent(this, (byte)13);
/*      */       }
/*      */     } 
/*  633 */     super.setLastHurtByMob(hurtBy);
/*      */   }
/*      */ 
/*      */   
/*      */   public void die(DamageSource source) {
/*  638 */     LOGGER.info("Villager {} died, message: '{}'", this, source.getLocalizedDeathMessage(this).getString());
/*  639 */     Entity murderer = source.getEntity();
/*  640 */     if (murderer != null) {
/*  641 */       tellWitnessesThatIWasMurdered(murderer);
/*      */     }
/*      */     
/*  644 */     releaseAllPois();
/*  645 */     super.die(source);
/*      */   }
/*      */   
/*      */   private void releaseAllPois() {
/*  649 */     releasePoi(MemoryModuleType.HOME);
/*  650 */     releasePoi(MemoryModuleType.JOB_SITE);
/*  651 */     releasePoi(MemoryModuleType.POTENTIAL_JOB_SITE);
/*  652 */     releasePoi(MemoryModuleType.MEETING_POINT);
/*      */   }
/*      */   private void tellWitnessesThatIWasMurdered(Entity murderer) {
/*      */     ServerLevel serverLevel;
/*  656 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*      */     else
/*      */     { return; }
/*      */     
/*  660 */     Optional<NearestVisibleLivingEntities> witnesses = this.brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
/*  661 */     if (witnesses.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/*  665 */     Objects.requireNonNull(ReputationEventHandler.class); ((NearestVisibleLivingEntities)witnesses.get()).findAll(ReputationEventHandler.class::isInstance)
/*  666 */       .forEach(witness -> serverLevel.onReputationEvent(ReputationEventType.VILLAGER_KILLED, murderer, (ReputationEventHandler)witness));
/*      */   }
/*      */   
/*      */   public void releasePoi(MemoryModuleType<GlobalPos> memoryType) {
/*  670 */     if (!(level() instanceof ServerLevel)) {
/*      */       return;
/*      */     }
/*  673 */     MinecraftServer server = ((ServerLevel)level()).getServer();
/*  674 */     this.brain.getMemory(memoryType).ifPresent(memory -> {
/*  675 */           ServerLevel poiLevel = server.getLevel(memory.dimension());
/*  676 */           if (poiLevel == null) {
/*      */             return;
/*      */           }
/*  679 */           PoiManager poiManager = poiLevel.getPoiManager();
/*  680 */           Optional<Holder<PoiType>> type = poiManager.getType(memory.pos());
/*  681 */           BiPredicate<Villager, Holder<PoiType>> poiTypePredicate = (BiPredicate)POI_MEMORIES.get(memoryType);
/*  682 */           if (type.isPresent() && poiTypePredicate.test(this, (Holder)type.get())) {
/*  683 */             poiManager.release(memory.pos());
/*  684 */             poiLevel.debugSynchronizers().updatePoi(memory.pos());
/*      */           } 
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  691 */   public boolean canBreed() { return (this.foodLevel + countFoodPointsInInventory() >= 12 && !isSleeping() && getAge() == 0); }
/*      */ 
/*      */ 
/*      */   
/*  695 */   private boolean hungry() { return (this.foodLevel < 12); }
/*      */ 
/*      */   
/*      */   private void eatUntilFull() {
/*  699 */     if (!hungry() || countFoodPointsInInventory() == 0) {
/*      */       return;
/*      */     }
/*      */     
/*  703 */     for (int slot = 0; slot < getInventory().getContainerSize(); slot++) {
/*  704 */       ItemStack itemStack = getInventory().getItem(slot);
/*      */       
/*  706 */       if (!itemStack.isEmpty()) {
/*  707 */         Integer value = (Integer)FOOD_POINTS.get(itemStack.getItem());
/*  708 */         if (value != null) {
/*  709 */           int itemCount = itemStack.getCount();
/*  710 */           for (int count = itemCount; count > 0; count--) {
/*  711 */             this.foodLevel += value.intValue();
/*  712 */             getInventory().removeItem(slot, 1);
/*      */             
/*  714 */             if (!hungry()) {
/*      */               return;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  724 */   public int getPlayerReputation(Player player) { return this.gossips.getReputation(player.getUUID(), t -> true); }
/*      */ 
/*      */ 
/*      */   
/*  728 */   private void digestFood(int amount) { this.foodLevel -= amount; }
/*      */ 
/*      */   
/*      */   public void eatAndDigestFood() {
/*  732 */     eatUntilFull();
/*  733 */     digestFood(12);
/*      */   }
/*      */ 
/*      */   
/*  737 */   public void setOffers(MerchantOffers offers) { this.offers = offers; }
/*      */ 
/*      */   
/*      */   private boolean shouldIncreaseLevel() {
/*  741 */     int currentLevel = getVillagerData().level();
/*  742 */     return (VillagerData.canLevelUp(currentLevel) && this.villagerXp >= VillagerData.getMaxXpPerLevel(currentLevel));
/*      */   }
/*      */   
/*      */   private void increaseMerchantCareer(ServerLevel level) {
/*  746 */     setVillagerData(getVillagerData().withLevel(getVillagerData().level() + 1));
/*      */     
/*  748 */     updateTrades(level);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  753 */   protected Component getTypeName() { return ((VillagerProfession)getVillagerData().profession().value()).name(); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleEntityEvent(byte id) {
/*  758 */     if (id == 12) {
/*  759 */       addParticlesAroundSelf(ParticleTypes.HEART);
/*  760 */     } else if (id == 13) {
/*  761 */       addParticlesAroundSelf(ParticleTypes.ANGRY_VILLAGER);
/*  762 */     } else if (id == 14) {
/*  763 */       addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
/*  764 */     } else if (id == 42) {
/*  765 */       addParticlesAroundSelf(ParticleTypes.SPLASH);
/*      */     } else {
/*  767 */       super.handleEntityEvent(id);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*  773 */     if (spawnReason == EntitySpawnReason.BREEDING) {
/*  774 */       setVillagerData(getVillagerData().withProfession(level.registryAccess(), VillagerProfession.NONE));
/*      */     }
/*  776 */     if (spawnReason == EntitySpawnReason.COMMAND || spawnReason == EntitySpawnReason.SPAWN_ITEM_USE || EntitySpawnReason.isSpawner(spawnReason) || spawnReason == EntitySpawnReason.DISPENSER) {
/*  777 */       setVillagerData(getVillagerData().withType(level.registryAccess(), VillagerType.byBiome(level.getBiome(blockPosition()))));
/*      */     }
/*      */     
/*  780 */     if (spawnReason == EntitySpawnReason.STRUCTURE) {
/*  781 */       this.assignProfessionWhenSpawned = true;
/*      */     }
/*      */     
/*  784 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*      */   }
/*      */ 
/*      */   
/*      */   public Villager getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*      */     Holder<VillagerType> type;
/*  790 */     double random = this.random.nextDouble();
/*  791 */     if (random < 0.5D) {
/*  792 */       type = level.registryAccess().getOrThrow(VillagerType.byBiome(level.getBiome(blockPosition())));
/*  793 */     } else if (random < 0.75D) {
/*  794 */       type = getVillagerData().type();
/*      */     } else {
/*  796 */       type = ((Villager)partner).getVillagerData().type();
/*      */     } 
/*      */     
/*  799 */     Villager villager = new Villager(EntityType.VILLAGER, level, type);
/*  800 */     villager.finalizeSpawn(level, level.getCurrentDifficultyAt(villager.blockPosition()), EntitySpawnReason.BREEDING, null);
/*  801 */     return villager;
/*      */   }
/*      */ 
/*      */   
/*      */   public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
/*  806 */     if (level.getDifficulty() != Difficulty.PEACEFUL) {
/*  807 */       LOGGER.info("Villager {} was struck by lightning {}.", this, lightningBolt);
/*  808 */       Witch witch = (Witch)convertTo(EntityType.WITCH, ConversionParams.single(this, false, false), w -> {
/*  809 */             w.finalizeSpawn(level, level.getCurrentDifficultyAt(w.blockPosition()), EntitySpawnReason.CONVERSION, null);
/*  810 */             w.setPersistenceRequired();
/*  811 */             releaseAllPois();
/*      */           });
/*      */       
/*  814 */       if (witch == null) {
/*  815 */         super.thunderHit(level, lightningBolt);
/*      */       }
/*      */     } else {
/*  818 */       super.thunderHit(level, lightningBolt);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  824 */   protected void pickUpItem(ServerLevel level, ItemEntity entity) { InventoryCarrier.pickUpItem(level, this, this, entity); }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
/*  829 */     Item item = itemStack.getItem();
/*  830 */     return ((itemStack.is(ItemTags.VILLAGER_PICKS_UP) || ((VillagerProfession)getVillagerData().profession().value()).requestedItems().contains(item)) && getInventory().canAddItem(itemStack));
/*      */   }
/*      */ 
/*      */   
/*  834 */   public boolean hasExcessFood() { return (countFoodPointsInInventory() >= 24); }
/*      */ 
/*      */ 
/*      */   
/*  838 */   public boolean wantsMoreFood() { return (countFoodPointsInInventory() < 12); }
/*      */ 
/*      */   
/*      */   private int countFoodPointsInInventory() {
/*  842 */     SimpleContainer inventory = getInventory();
/*  843 */     return FOOD_POINTS.entrySet().stream().mapToInt(entry -> inventory.countItem((Item)entry.getKey()) * ((Integer)entry.getValue()).intValue()).sum();
/*      */   }
/*      */ 
/*      */   
/*  847 */   public boolean hasFarmSeeds() { return getInventory().hasAnyMatching(item -> item.is(ItemTags.VILLAGER_PLANTABLE_SEEDS)); }
/*      */ 
/*      */   
/*      */   protected void updateTrades(ServerLevel level) {
/*      */     Int2ObjectMap<VillagerTrades.ItemListing[]> tradesByLevel;
/*  852 */     VillagerData data = getVillagerData();
/*  853 */     ResourceKey<VillagerProfession> profession = (ResourceKey)data.profession().unwrapKey().orElse(null);
/*  854 */     if (profession == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  859 */     if (level().enabledFeatures().contains(FeatureFlags.TRADE_REBALANCE)) {
/*  860 */       Int2ObjectMap<VillagerTrades.ItemListing[]> experimentalTrades = (Int2ObjectMap)VillagerTrades.EXPERIMENTAL_TRADES.get(profession);
/*  861 */       tradesByLevel = (experimentalTrades != null) ? experimentalTrades : (Int2ObjectMap)VillagerTrades.TRADES.get(profession);
/*      */     } else {
/*  863 */       tradesByLevel = (Int2ObjectMap)VillagerTrades.TRADES.get(profession);
/*      */     } 
/*      */     
/*  866 */     if (tradesByLevel == null || tradesByLevel.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/*  870 */     ItemListing[] itemListings = (ItemListing[])tradesByLevel.get(data.level());
/*      */     
/*  872 */     if (itemListings == null) {
/*      */       return;
/*      */     }
/*      */     
/*  876 */     MerchantOffers offers = getOffers();
/*  877 */     addOffersFromItemListings(level, offers, itemListings, 2);
/*  878 */     if (SharedConstants.DEBUG_UNLOCK_ALL_TRADES && data.level() < tradesByLevel.size()) {
/*  879 */       increaseMerchantCareer(level);
/*      */     }
/*      */   }
/*      */   
/*      */   public void gossip(ServerLevel level, Villager target, long timestamp) {
/*  884 */     if ((timestamp >= this.lastGossipTime && timestamp < this.lastGossipTime + 1200L) || (timestamp >= target.lastGossipTime && timestamp < target.lastGossipTime + 1200L)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  890 */     this.gossips.transferFrom(target.gossips, this.random, 10);
/*      */     
/*  892 */     this.lastGossipTime = timestamp;
/*  893 */     target.lastGossipTime = timestamp;
/*      */     
/*  895 */     spawnGolemIfNeeded(level, timestamp, 5);
/*      */   }
/*      */   
/*      */   private void maybeDecayGossip() {
/*  899 */     long timestamp = level().getGameTime();
/*      */     
/*  901 */     if (this.lastGossipDecayTime == 0L) {
/*  902 */       this.lastGossipDecayTime = timestamp;
/*      */       
/*      */       return;
/*      */     } 
/*  906 */     if (timestamp < this.lastGossipDecayTime + 24000L) {
/*      */       return;
/*      */     }
/*      */     
/*  910 */     this.gossips.decay();
/*  911 */     this.lastGossipDecayTime = timestamp;
/*      */   }
/*      */   
/*      */   public void spawnGolemIfNeeded(ServerLevel level, long timestamp, int villagersNeededToAgree) {
/*  915 */     if (!wantsToSpawnGolem(timestamp)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  920 */     AABB villagerSearchBox = getBoundingBox().inflate(10.0D, 10.0D, 10.0D);
/*      */     
/*  922 */     List<Villager> nearbyVillagers = level.getEntitiesOfClass(Villager.class, villagerSearchBox);
/*      */ 
/*      */ 
/*      */     
/*  926 */     List<Villager> nearbyVillagersThatWantAGolem = nearbyVillagers.stream().filter(villager -> villager.wantsToSpawnGolem(timestamp)).limit(5L).toList();
/*      */     
/*  928 */     if (nearbyVillagersThatWantAGolem.size() < villagersNeededToAgree) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  934 */     if (SpawnUtil.trySpawnMob(EntityType.IRON_GOLEM, EntitySpawnReason.MOB_SUMMONED, level, blockPosition(), 10, 8, 6, SpawnUtil.Strategy.LEGACY_IRON_GOLEM, false).isEmpty()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  939 */     nearbyVillagers.forEach(GolemSensor::golemDetected);
/*      */   }
/*      */   
/*      */   public boolean wantsToSpawnGolem(long timestamp) {
/*  943 */     if (!golemSpawnConditionsMet(level().getGameTime())) {
/*  944 */       return false;
/*      */     }
/*  946 */     return !this.brain.hasMemoryValue(MemoryModuleType.GOLEM_DETECTED_RECENTLY);
/*      */   }
/*      */ 
/*      */   
/*      */   public void onReputationEventFrom(ReputationEventType type, Entity source) {
/*  951 */     if (type == ReputationEventType.ZOMBIE_VILLAGER_CURED) {
/*  952 */       this.gossips.add(source.getUUID(), GossipType.MAJOR_POSITIVE, 20);
/*  953 */       this.gossips.add(source.getUUID(), GossipType.MINOR_POSITIVE, 25);
/*  954 */     } else if (type == ReputationEventType.TRADE) {
/*  955 */       this.gossips.add(source.getUUID(), GossipType.TRADING, 2);
/*  956 */     } else if (type == ReputationEventType.VILLAGER_HURT) {
/*  957 */       this.gossips.add(source.getUUID(), GossipType.MINOR_NEGATIVE, 25);
/*  958 */     } else if (type == ReputationEventType.VILLAGER_KILLED) {
/*  959 */       this.gossips.add(source.getUUID(), GossipType.MAJOR_NEGATIVE, 25);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  965 */   public int getVillagerXp() { return this.villagerXp; }
/*      */ 
/*      */ 
/*      */   
/*  969 */   public void setVillagerXp(int value) { this.villagerXp = value; }
/*      */ 
/*      */   
/*      */   private void resetNumberOfRestocks() {
/*  973 */     catchUpDemand();
/*  974 */     this.numberOfRestocksToday = 0;
/*      */   }
/*      */ 
/*      */   
/*  978 */   public GossipContainer getGossips() { return this.gossips; }
/*      */ 
/*      */ 
/*      */   
/*  982 */   public void setGossips(GossipContainer gossips) { this.gossips.putAll(gossips); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void startSleeping(BlockPos bedPosition) {
/*  987 */     super.startSleeping(bedPosition);
/*  988 */     this.brain.setMemory(MemoryModuleType.LAST_SLEPT, Long.valueOf(level().getGameTime()));
/*  989 */     this.brain.eraseMemory(MemoryModuleType.WALK_TARGET);
/*  990 */     this.brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/*      */   }
/*      */ 
/*      */   
/*      */   public void stopSleeping() {
/*  995 */     super.stopSleeping();
/*  996 */     this.brain.setMemory(MemoryModuleType.LAST_WOKEN, Long.valueOf(level().getGameTime()));
/*      */   }
/*      */   
/*      */   private boolean golemSpawnConditionsMet(long gameTime) {
/* 1000 */     Optional<Long> sleepMemory = this.brain.getMemory(MemoryModuleType.LAST_SLEPT);
/* 1001 */     return sleepMemory.filter(aLong -> (gameTime - aLong.longValue() < 24000L)).isPresent();
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T get(DataComponentType<? extends T> type) {
/* 1006 */     if (type == DataComponents.VILLAGER_VARIANT) {
/* 1007 */       return (T)castComponentValue(type, getVillagerData().type());
/*      */     }
/*      */     
/* 1010 */     return (T)super.get(type);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 1015 */     applyImplicitComponentIfPresent(components, DataComponents.VILLAGER_VARIANT);
/* 1016 */     super.applyImplicitComponents(components);
/*      */   }
/*      */ 
/*      */   
/*      */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 1021 */     if (type == DataComponents.VILLAGER_VARIANT) {
/* 1022 */       Holder<VillagerType> variant = (Holder)castComponentValue(DataComponents.VILLAGER_VARIANT, value);
/* 1023 */       setVillagerData(getVillagerData().withType(variant));
/* 1024 */       return true;
/*      */     } 
/*      */     
/* 1027 */     return super.applyImplicitComponent(type, value);
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\npc\villager\Villager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */