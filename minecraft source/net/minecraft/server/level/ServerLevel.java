/*      */ package net.minecraft.server.level;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.datafixers.DataFixer;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.longs.LongSet;
/*      */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*      */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.function.BooleanSupplier;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.ReportType;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.core.particles.ExplosionParticleInfo;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundExplodePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSoundPacket;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.ServerScoreboard;
/*      */ import net.minecraft.server.players.SleepStatus;
/*      */ import net.minecraft.server.waypoints.ServerWaypointManager;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.AbortableIterationConsumer;
/*      */ import net.minecraft.util.CsvOutput;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.ProgressListener;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.datafix.DataFixTypes;
/*      */ import net.minecraft.util.debug.DebugSubscriptions;
/*      */ import net.minecraft.util.debug.LevelDebugSynchronizers;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.util.random.WeightedList;
/*      */ import net.minecraft.util.valueproviders.IntProvider;
/*      */ import net.minecraft.util.valueproviders.UniformInt;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.RandomSequences;
/*      */ import net.minecraft.world.TickRateManager;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributeReader;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributeSystem;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.LightningBolt;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.MobCategory;
/*      */ import net.minecraft.world.entity.ReputationEventHandler;
/*      */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*      */ import net.minecraft.world.entity.ai.village.ReputationEventType;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*      */ import net.minecraft.world.entity.animal.equine.SkeletonHorse;
/*      */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*      */ import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.raid.Raid;
/*      */ import net.minecraft.world.entity.raid.Raids;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*      */ import net.minecraft.world.item.crafting.RecipeAccess;
/*      */ import net.minecraft.world.item.crafting.RecipeManager;
/*      */ import net.minecraft.world.level.BlockEventData;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.CustomSpawner;
/*      */ import net.minecraft.world.level.Explosion;
/*      */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.MoonPhase;
/*      */ import net.minecraft.world.level.NaturalSpawner;
/*      */ import net.minecraft.world.level.ServerExplosion;
/*      */ import net.minecraft.world.level.StructureManager;
/*      */ import net.minecraft.world.level.biome.Biome;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.SnowLayerBlock;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.FuelValues;
/*      */ import net.minecraft.world.level.block.entity.TickingBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.border.WorldBorder;
/*      */ import net.minecraft.world.level.chunk.ChunkAccess;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.chunk.ChunkSource;
/*      */ import net.minecraft.world.level.chunk.LevelChunk;
/*      */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*      */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*      */ import net.minecraft.world.level.chunk.storage.EntityStorage;
/*      */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*      */ import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
/*      */ import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
/*      */ import net.minecraft.world.level.dimension.DimensionType;
/*      */ import net.minecraft.world.level.dimension.LevelStem;
/*      */ import net.minecraft.world.level.dimension.end.EndDragonFight;
/*      */ import net.minecraft.world.level.entity.EntityTickList;
/*      */ import net.minecraft.world.level.entity.EntityTypeTest;
/*      */ import net.minecraft.world.level.entity.LevelCallback;
/*      */ import net.minecraft.world.level.entity.LevelEntityGetter;
/*      */ import net.minecraft.world.level.entity.PersistentEntitySectionManager;
/*      */ import net.minecraft.world.level.gameevent.DynamicGameEventListener;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.gameevent.GameEventDispatcher;
/*      */ import net.minecraft.world.level.gamerules.GameRule;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.levelgen.Heightmap;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.Structure;
/*      */ import net.minecraft.world.level.levelgen.structure.StructureCheck;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.pathfinder.PathTypeCache;
/*      */ import net.minecraft.world.level.portal.PortalForcer;
/*      */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*      */ import net.minecraft.world.level.redstone.Orientation;
/*      */ import net.minecraft.world.level.saveddata.maps.MapId;
/*      */ import net.minecraft.world.level.saveddata.maps.MapIndex;
/*      */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*      */ import net.minecraft.world.level.storage.DimensionDataStorage;
/*      */ import net.minecraft.world.level.storage.LevelData;
/*      */ import net.minecraft.world.level.storage.LevelStorageSource;
/*      */ import net.minecraft.world.level.storage.ServerLevelData;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.BooleanOp;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ import net.minecraft.world.scores.Scoreboard;
/*      */ import net.minecraft.world.ticks.LevelTickAccess;
/*      */ import net.minecraft.world.ticks.LevelTicks;
/*      */ import net.minecraft.world.waypoints.WaypointTransmitter;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class ServerLevel extends Level implements WorldGenLevel, ServerEntityGetter {
/*  188 */   public static final BlockPos END_SPAWN_POINT = new BlockPos(100, 50, 0);
/*      */   
/*  190 */   public static final IntProvider RAIN_DELAY = UniformInt.of(12000, 180000);
/*  191 */   public static final IntProvider RAIN_DURATION = UniformInt.of(12000, 24000);
/*      */ 
/*      */   
/*  194 */   private static final IntProvider THUNDER_DELAY = UniformInt.of(12000, 180000);
/*  195 */   public static final IntProvider THUNDER_DURATION = UniformInt.of(3600, 15600);
/*      */   
/*  197 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   private static final int EMPTY_TIME_NO_TICK = 300;
/*      */   
/*      */   private static final int MAX_SCHEDULED_TICKS_PER_TICK = 65536;
/*  202 */   private final List<ServerPlayer> players = Lists.newArrayList();
/*      */   
/*      */   private final ServerChunkCache chunkSource;
/*      */   private final MinecraftServer server;
/*      */   private final ServerLevelData serverLevelData;
/*  207 */   private final EntityTickList entityTickList = new EntityTickList();
/*      */   
/*      */   private final ServerWaypointManager waypointManager;
/*      */   
/*      */   private final EnvironmentAttributeSystem environmentAttributes;
/*      */   private final PersistentEntitySectionManager<Entity> entityManager;
/*      */   private final GameEventDispatcher gameEventDispatcher;
/*      */   public boolean noSave;
/*      */   private final SleepStatus sleepStatus;
/*      */   private int emptyTime;
/*      */   private final PortalForcer portalForcer;
/*  218 */   private final LevelTicks<Block> blockTicks = new LevelTicks(this::isPositionTickingWithEntitiesLoaded);
/*  219 */   private final LevelTicks<Fluid> fluidTicks = new LevelTicks(this::isPositionTickingWithEntitiesLoaded);
/*      */   
/*  221 */   private final PathTypeCache pathTypesByPosCache = new PathTypeCache();
/*  222 */   private final Set<Mob> navigatingMobs = new ObjectOpenHashSet();
/*      */ 
/*      */   
/*      */   protected final Raids raids;
/*      */   
/*  227 */   private final ObjectLinkedOpenHashSet<BlockEventData> blockEvents = new ObjectLinkedOpenHashSet();
/*  228 */   private final List<BlockEventData> blockEventsToReschedule = new ArrayList(64);
/*      */   
/*      */   private boolean handlingTick;
/*      */   
/*      */   private final List<CustomSpawner> customSpawners;
/*      */   
/*      */   private EndDragonFight dragonFight;
/*  235 */   private final Int2ObjectMap<EnderDragonPart> dragonParts = new Int2ObjectOpenHashMap();
/*      */   
/*      */   private final StructureManager structureManager;
/*      */   
/*      */   private final StructureCheck structureCheck;
/*      */   
/*      */   private final boolean tickTime;
/*      */   private final RandomSequences randomSequences;
/*  243 */   private final LevelDebugSynchronizers debugSynchronizers = new LevelDebugSynchronizers(this);
/*      */   
/*      */   public ServerLevel(MinecraftServer server, Executor executor, LevelStorageSource.LevelStorageAccess levelStorage, ServerLevelData levelData, ResourceKey<Level> dimension, LevelStem levelStem, boolean isDebug, long biomeZoomSeed, List<CustomSpawner> customSpawners, boolean tickTime, RandomSequences randomSequences) {
/*  246 */     super(levelData, dimension, server.registryAccess(), levelStem.type(), false, isDebug, biomeZoomSeed, server.getMaxChainedNeighborUpdates());
/*  247 */     this.tickTime = tickTime;
/*  248 */     this.server = server;
/*  249 */     this.customSpawners = customSpawners;
/*  250 */     this.serverLevelData = levelData;
/*      */     
/*  252 */     ChunkGenerator generator = levelStem.generator();
/*      */     
/*  254 */     boolean syncWrites = server.forceSynchronousWrites();
/*  255 */     DataFixer fixerUpper = server.getFixerUpper();
/*  256 */     EntityStorage entityStorage1 = new EntityStorage(new SimpleRegionStorage(new RegionStorageInfo(levelStorage.getLevelId(), dimension, "entities"), levelStorage.getDimensionPath(dimension).resolve("entities"), fixerUpper, syncWrites, DataFixTypes.ENTITY_CHUNK), this, server);
/*  257 */     this.entityManager = new PersistentEntitySectionManager(Entity.class, new EntityCallbacks(this), entityStorage1);
/*      */     
/*  259 */     Objects.requireNonNull(this.entityManager); this.chunkSource = new ServerChunkCache(this, levelStorage, fixerUpper, server.getStructureManager(), executor, generator, server.getPlayerList().getViewDistance(), server.getPlayerList().getSimulationDistance(), syncWrites, this.entityManager::updateChunkStatus, () -> server.overworld().getDataStorage());
/*  260 */     this.chunkSource.getGeneratorState().ensureStructuresGenerated();
/*      */     
/*  262 */     this.portalForcer = new PortalForcer(this);
/*      */     
/*  264 */     if (canHaveWeather()) {
/*  265 */       prepareWeather();
/*      */     }
/*      */     
/*  268 */     this.raids = (Raids)getDataStorage().computeIfAbsent(Raids.getType(dimensionTypeRegistration()));
/*      */     
/*  270 */     if (!server.isSingleplayer()) {
/*  271 */       levelData.setGameType(server.getDefaultGameType());
/*      */     }
/*      */     
/*  274 */     long seed = server.getWorldData().worldGenOptions().seed();
/*      */     
/*  276 */     this.structureCheck = new StructureCheck(this.chunkSource.chunkScanner(), registryAccess(), server.getStructureManager(), dimension, generator, this.chunkSource.randomState(), this, generator.getBiomeSource(), seed, fixerUpper);
/*  277 */     this.structureManager = new StructureManager(this, server.getWorldData().worldGenOptions(), this.structureCheck);
/*      */ 
/*      */ 
/*      */     
/*  281 */     if (dimension() == Level.END && dimensionTypeRegistration().is(BuiltinDimensionTypes.END)) {
/*  282 */       this.dragonFight = new EndDragonFight(this, seed, server.getWorldData().endDragonFightData());
/*      */     } else {
/*  284 */       this.dragonFight = null;
/*      */     } 
/*      */     
/*  287 */     this.sleepStatus = new SleepStatus();
/*  288 */     this.gameEventDispatcher = new GameEventDispatcher(this);
/*  289 */     this.randomSequences = (RandomSequences)Objects.requireNonNullElseGet(randomSequences, () -> (RandomSequences)getDataStorage().computeIfAbsent(RandomSequences.TYPE));
/*      */     
/*  291 */     this.waypointManager = new ServerWaypointManager();
/*  292 */     this
/*      */       
/*  294 */       .environmentAttributes = EnvironmentAttributeSystem.builder().addDefaultLayers(this).build();
/*      */     
/*  296 */     updateSkyBrightness();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @VisibleForTesting
/*  305 */   public void setDragonFight(EndDragonFight fight) { this.dragonFight = fight; }
/*      */ 
/*      */   
/*      */   public void setWeatherParameters(int clearTime, int rainTime, boolean raining, boolean thundering) {
/*  309 */     this.serverLevelData.setClearWeatherTime(clearTime);
/*  310 */     this.serverLevelData.setRainTime(rainTime);
/*  311 */     this.serverLevelData.setThunderTime(rainTime);
/*  312 */     this.serverLevelData.setRaining(raining);
/*  313 */     this.serverLevelData.setThundering(thundering);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  318 */   public Holder<Biome> getUncachedNoiseBiome(int quartX, int quartY, int quartZ) { return getChunkSource().getGenerator().getBiomeSource().getNoiseBiome(quartX, quartY, quartZ, getChunkSource().randomState().sampler()); }
/*      */ 
/*      */ 
/*      */   
/*  322 */   public StructureManager structureManager() { return this.structureManager; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  327 */   public EnvironmentAttributeSystem environmentAttributes() { return this.environmentAttributes; }
/*      */ 
/*      */   
/*      */   public void tick(BooleanSupplier haveTime) {
/*  331 */     ProfilerFiller profiler = Profiler.get();
/*      */     
/*  333 */     this.handlingTick = true;
/*  334 */     TickRateManager tickRateManager = tickRateManager();
/*  335 */     boolean runs = tickRateManager.runsNormally();
/*  336 */     if (runs) {
/*  337 */       profiler.push("world border");
/*  338 */       getWorldBorder().tick();
/*  339 */       profiler.popPush("weather");
/*  340 */       advanceWeatherCycle();
/*  341 */       profiler.pop();
/*      */     } 
/*  343 */     int percentage = ((Integer)getGameRules().get(GameRules.PLAYERS_SLEEPING_PERCENTAGE)).intValue();
/*  344 */     if (this.sleepStatus.areEnoughSleeping(percentage) && this.sleepStatus.areEnoughDeepSleeping(percentage, this.players)) {
/*  345 */       if (((Boolean)getGameRules().get(GameRules.ADVANCE_TIME)).booleanValue()) {
/*      */         
/*  347 */         long newTime = this.levelData.getDayTime() + 24000L;
/*  348 */         setDayTime(newTime - newTime % 24000L);
/*      */       } 
/*      */       
/*  351 */       wakeUpAllPlayers();
/*      */       
/*  353 */       if (((Boolean)getGameRules().get(GameRules.ADVANCE_WEATHER)).booleanValue() && isRaining()) {
/*  354 */         resetWeatherCycle();
/*      */       }
/*      */     } 
/*      */     
/*  358 */     updateSkyBrightness();
/*      */     
/*  360 */     if (runs) {
/*  361 */       tickTime();
/*      */     }
/*      */     
/*  364 */     profiler.push("tickPending");
/*  365 */     if (!isDebug() && runs) {
/*  366 */       long tick = getGameTime();
/*  367 */       profiler.push("blockTicks");
/*  368 */       this.blockTicks.tick(tick, 65536, this::tickBlock);
/*  369 */       profiler.popPush("fluidTicks");
/*  370 */       this.fluidTicks.tick(tick, 65536, this::tickFluid);
/*  371 */       profiler.pop();
/*      */     } 
/*      */     
/*  374 */     profiler.popPush("raid");
/*  375 */     if (runs) {
/*  376 */       this.raids.tick(this);
/*      */     }
/*  378 */     profiler.popPush("chunkSource");
/*  379 */     getChunkSource().tick(haveTime, true);
/*      */     
/*  381 */     profiler.popPush("blockEvents");
/*  382 */     if (runs) {
/*  383 */       runBlockEvents();
/*      */     }
/*  385 */     this.handlingTick = false;
/*  386 */     profiler.pop();
/*      */     
/*  388 */     boolean isActive = this.chunkSource.hasActiveTickets();
/*      */     
/*  390 */     if (isActive) {
/*  391 */       resetEmptyTime();
/*      */     }
/*  393 */     if (runs) {
/*  394 */       this.emptyTime++;
/*      */     }
/*  396 */     if (this.emptyTime < 300) {
/*  397 */       profiler.push("entities");
/*  398 */       if (this.dragonFight != null && runs) {
/*  399 */         profiler.push("dragonFight");
/*  400 */         this.dragonFight.tick();
/*  401 */         profiler.pop();
/*      */       } 
/*  403 */       this.entityTickList.forEach(entity -> {
/*      */             
/*  405 */             if (entity.isRemoved()) {
/*      */               return;
/*      */             }
/*      */             
/*  409 */             if (tickRateManager.isEntityFrozen(entity)) {
/*      */               return;
/*      */             }
/*      */             
/*  413 */             profiler.push("checkDespawn");
/*  414 */             entity.checkDespawn();
/*  415 */             profiler.pop();
/*      */             
/*  417 */             if (!(entity instanceof ServerPlayer) && 
/*      */               
/*  419 */               !this.chunkSource.chunkMap.getDistanceManager().inEntityTickingRange(entity.chunkPosition().toLong())) {
/*      */               return;
/*      */             }
/*      */             
/*  423 */             Entity vehicle = entity.getVehicle();
/*  424 */             if (vehicle != null) {
/*  425 */               if (vehicle.isRemoved() || !vehicle.hasPassenger(entity)) {
/*  426 */                 entity.stopRiding();
/*      */               } else {
/*      */                 return;
/*      */               } 
/*      */             }
/*      */             
/*  432 */             profiler.push("tick");
/*  433 */             guardEntityTick(this::tickNonPassenger, entity);
/*  434 */             profiler.pop();
/*      */           });
/*  436 */       profiler.popPush("blockEntities");
/*  437 */       tickBlockEntities();
/*  438 */       profiler.pop();
/*      */     } 
/*      */     
/*  441 */     profiler.push("entityManagement");
/*  442 */     this.entityManager.tick();
/*  443 */     profiler.pop();
/*      */     
/*  445 */     profiler.push("debugSynchronizers");
/*  446 */     if (this.debugSynchronizers.hasAnySubscriberFor(DebugSubscriptions.NEIGHBOR_UPDATES)) {
/*  447 */       this.neighborUpdater.setDebugListener(blockPos -> 
/*  448 */           this.debugSynchronizers.broadcastEventToTracking(blockPos, DebugSubscriptions.NEIGHBOR_UPDATES, blockPos));
/*      */     } else {
/*      */       
/*  451 */       this.neighborUpdater.setDebugListener(null);
/*      */     } 
/*  453 */     this.debugSynchronizers.tick(this.server.debugSubscribers());
/*  454 */     profiler.pop();
/*      */     
/*  456 */     environmentAttributes().invalidateTickCache();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  461 */   public boolean shouldTickBlocksAt(long chunkPos) { return this.chunkSource.chunkMap.getDistanceManager().inBlockTickingRange(chunkPos); }
/*      */ 
/*      */   
/*      */   protected void tickTime() {
/*  465 */     if (!this.tickTime) {
/*      */       return;
/*      */     }
/*  468 */     long time = this.levelData.getGameTime() + 1L;
/*  469 */     this.serverLevelData.setGameTime(time);
/*  470 */     Profiler.get().push("scheduledFunctions");
/*  471 */     this.serverLevelData.getScheduledEvents().tick(this.server, time);
/*  472 */     Profiler.get().pop();
/*  473 */     if (((Boolean)getGameRules().get(GameRules.ADVANCE_TIME)).booleanValue()) {
/*  474 */       setDayTime(this.levelData.getDayTime() + 1L);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  479 */   public void setDayTime(long newTime) { this.serverLevelData.setDayTime(newTime); }
/*      */ 
/*      */ 
/*      */   
/*  483 */   public long getDayCount() { return getDayTime() / 24000L; }
/*      */ 
/*      */   
/*      */   public void tickCustomSpawners(boolean spawnEnemies) {
/*  487 */     for (CustomSpawner spawner : this.customSpawners) {
/*  488 */       spawner.tick(this, spawnEnemies);
/*      */     }
/*      */   }
/*      */   
/*      */   private void wakeUpAllPlayers() {
/*  493 */     this.sleepStatus.removeAllSleepers();
/*      */     
/*  495 */     ((List)this.players.stream().filter(LivingEntity::isSleeping).collect(Collectors.toList())).forEach(player -> 
/*  496 */         player.stopSleepInBed(false, false));
/*      */   }
/*      */ 
/*      */   
/*      */   public void tickChunk(LevelChunk chunk, int tickSpeed) {
/*  501 */     ChunkPos chunkPos = chunk.getPos();
/*  502 */     int minX = chunkPos.getMinBlockX();
/*  503 */     int minZ = chunkPos.getMinBlockZ();
/*      */     
/*  505 */     ProfilerFiller profiler = Profiler.get();
/*      */     
/*  507 */     profiler.push("iceandsnow");
/*  508 */     for (int i = 0; i < tickSpeed; i++) {
/*  509 */       if (this.random.nextInt(48) == 0) {
/*  510 */         tickPrecipitation(getBlockRandomPos(minX, 0, minZ, 15));
/*      */       }
/*      */     } 
/*      */     
/*  514 */     profiler.popPush("tickBlocks");
/*  515 */     if (tickSpeed > 0) {
/*  516 */       LevelChunkSection[] sections = chunk.getSections();
/*  517 */       for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
/*  518 */         LevelChunkSection section = sections[sectionIndex];
/*  519 */         if (section.isRandomlyTicking()) {
/*  520 */           int sectionY = chunk.getSectionYFromSectionIndex(sectionIndex);
/*  521 */           int minYInSection = SectionPos.sectionToBlockCoord(sectionY);
/*  522 */           for (int i = 0; i < tickSpeed; i++) {
/*  523 */             BlockPos pos = getBlockRandomPos(minX, minYInSection, minZ, 15);
/*      */             
/*  525 */             profiler.push("randomTick");
/*  526 */             BlockState blockState = section.getBlockState(pos.getX() - minX, pos.getY() - minYInSection, pos.getZ() - minZ);
/*  527 */             if (blockState.isRandomlyTicking()) {
/*  528 */               blockState.randomTick(this, pos, this.random);
/*      */             }
/*  530 */             FluidState fluidState = blockState.getFluidState();
/*  531 */             if (fluidState.isRandomlyTicking()) {
/*  532 */               fluidState.randomTick(this, pos, this.random);
/*      */             }
/*  534 */             profiler.pop();
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  539 */     profiler.pop();
/*      */   }
/*      */   
/*      */   public void tickThunder(LevelChunk chunk) {
/*  543 */     ChunkPos chunkPos = chunk.getPos();
/*  544 */     boolean raining = isRaining();
/*  545 */     int minX = chunkPos.getMinBlockX();
/*  546 */     int minZ = chunkPos.getMinBlockZ();
/*      */     
/*  548 */     ProfilerFiller profiler = Profiler.get();
/*  549 */     profiler.push("thunder");
/*  550 */     if (raining && isThundering() && this.random.nextInt(100000) == 0) {
/*  551 */       BlockPos pos = findLightningTargetAround(getBlockRandomPos(minX, 0, minZ, 15));
/*  552 */       if (isRainingAt(pos)) {
/*  553 */         DifficultyInstance difficulty = getCurrentDifficultyAt(pos);
/*      */         
/*  555 */         boolean isTrap = (((Boolean)getGameRules().get(GameRules.SPAWN_MOBS)).booleanValue() && this.random.nextDouble() < difficulty.getEffectiveDifficulty() * 0.01D && !getBlockState(pos.below()).is(BlockTags.LIGHTNING_RODS));
/*  556 */         if (isTrap) {
/*  557 */           SkeletonHorse horse = (SkeletonHorse)EntityType.SKELETON_HORSE.create(this, EntitySpawnReason.EVENT);
/*  558 */           if (horse != null) {
/*  559 */             horse.setTrap(true);
/*  560 */             horse.setAge(0);
/*  561 */             horse.setPos(pos.getX(), pos.getY(), pos.getZ());
/*  562 */             addFreshEntity(horse);
/*      */           } 
/*      */         } 
/*  565 */         LightningBolt bolt = (LightningBolt)EntityType.LIGHTNING_BOLT.create(this, EntitySpawnReason.EVENT);
/*  566 */         if (bolt != null) {
/*  567 */           bolt.snapTo(Vec3.atBottomCenterOf(pos));
/*  568 */           bolt.setVisualOnly(isTrap);
/*  569 */           addFreshEntity(bolt);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  574 */     profiler.pop();
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   public void tickPrecipitation(BlockPos pos) {
/*  579 */     BlockPos topPos = getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos);
/*  580 */     BlockPos belowPos = topPos.below();
/*  581 */     Biome biome = (Biome)getBiome(topPos).value();
/*      */     
/*  583 */     if (biome.shouldFreeze(this, belowPos)) {
/*  584 */       setBlockAndUpdate(belowPos, Blocks.ICE.defaultBlockState());
/*      */     }
/*      */     
/*  587 */     if (isRaining()) {
/*  588 */       int maxHeight = ((Integer)getGameRules().get(GameRules.MAX_SNOW_ACCUMULATION_HEIGHT)).intValue();
/*  589 */       if (maxHeight > 0 && biome.shouldSnow(this, topPos)) {
/*  590 */         BlockState state = getBlockState(topPos);
/*  591 */         if (state.is(Blocks.SNOW)) {
/*  592 */           int currentLayers = ((Integer)state.getValue(SnowLayerBlock.LAYERS)).intValue();
/*  593 */           if (currentLayers < Math.min(maxHeight, 8)) {
/*  594 */             BlockState newState = (BlockState)state.setValue(SnowLayerBlock.LAYERS, Integer.valueOf(currentLayers + 1));
/*  595 */             Block.pushEntitiesUp(state, newState, this, topPos);
/*  596 */             setBlockAndUpdate(topPos, newState);
/*      */           } 
/*      */         } else {
/*  599 */           setBlockAndUpdate(topPos, Blocks.SNOW.defaultBlockState());
/*      */         } 
/*      */       } 
/*      */       
/*  603 */       Biome.Precipitation precipitation = biome.getPrecipitationAt(belowPos, getSeaLevel());
/*  604 */       if (precipitation != Biome.Precipitation.NONE) {
/*  605 */         BlockState belowState = getBlockState(belowPos);
/*  606 */         belowState.getBlock().handlePrecipitation(belowState, this, belowPos, precipitation);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private Optional<BlockPos> findLightningRod(BlockPos center) {
/*  612 */     Optional<BlockPos> nearbyLightningRod = getPoiManager().findClosest(p -> 
/*  613 */         p.is(PoiTypes.LIGHTNING_ROD), lightningRodPos -> 
/*  614 */         (lightningRodPos.getY() == getHeight(Heightmap.Types.WORLD_SURFACE, lightningRodPos.getX(), lightningRodPos.getZ()) - 1), center, 128, PoiManager.Occupancy.ANY);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  619 */     return nearbyLightningRod.map(blockPos -> blockPos.above(1));
/*      */   }
/*      */   
/*      */   protected BlockPos findLightningTargetAround(BlockPos pos) {
/*  623 */     BlockPos center = getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos);
/*      */     
/*  625 */     Optional<BlockPos> lightningRodTarget = findLightningRod(center);
/*  626 */     if (lightningRodTarget.isPresent()) {
/*  627 */       return (BlockPos)lightningRodTarget.get();
/*      */     }
/*      */     
/*  630 */     AABB search = AABB.encapsulatingFullBlocks(center, center.atY(getMaxY() + 1)).inflate(3.0D);
/*      */     
/*  632 */     List<LivingEntity> entities = getEntitiesOfClass(LivingEntity.class, search, input -> (input.isAlive() && canSeeSky(input.blockPosition())));
/*      */     
/*  634 */     if (!entities.isEmpty()) {
/*  635 */       return ((LivingEntity)entities.get(this.random.nextInt(entities.size()))).blockPosition();
/*      */     }
/*      */     
/*  638 */     if (center.getY() == getMinY() - 1) {
/*  639 */       center = center.above(2);
/*      */     }
/*      */     
/*  642 */     return center;
/*      */   }
/*      */ 
/*      */   
/*  646 */   public boolean isHandlingTick() { return this.handlingTick; }
/*      */ 
/*      */ 
/*      */   
/*  650 */   public boolean canSleepThroughNights() { return (((Integer)getGameRules().get(GameRules.PLAYERS_SLEEPING_PERCENTAGE)).intValue() <= 100); }
/*      */   
/*      */   private void announceSleepStatus() {
/*      */     MutableComponent mutableComponent;
/*  654 */     if (!canSleepThroughNights()) {
/*      */       return;
/*      */     }
/*      */     
/*  658 */     if (getServer().isSingleplayer() && !getServer().isPublished()) {
/*      */       return;
/*      */     }
/*  661 */     int percentage = ((Integer)getGameRules().get(GameRules.PLAYERS_SLEEPING_PERCENTAGE)).intValue();
/*      */     
/*  663 */     if (this.sleepStatus.areEnoughSleeping(percentage)) {
/*  664 */       mutableComponent = Component.translatable("sleep.skipping_night");
/*      */     } else {
/*  666 */       mutableComponent = Component.translatable("sleep.players_sleeping", new Object[] { Integer.valueOf(this.sleepStatus.amountSleeping()), Integer.valueOf(this.sleepStatus.sleepersNeeded(percentage)) });
/*      */     } 
/*      */     
/*  669 */     for (ServerPlayer player : this.players) {
/*  670 */       player.displayClientMessage(mutableComponent, true);
/*      */     }
/*      */   }
/*      */   
/*      */   public void updateSleepingPlayerList() {
/*  675 */     if (!this.players.isEmpty() && this.sleepStatus.update(this.players)) {
/*  676 */       announceSleepStatus();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  682 */   public ServerScoreboard getScoreboard() { return this.server.getScoreboard(); }
/*      */ 
/*      */ 
/*      */   
/*  686 */   public ServerWaypointManager getWaypointManager() { return this.waypointManager; }
/*      */ 
/*      */ 
/*      */   
/*      */   public DifficultyInstance getCurrentDifficultyAt(BlockPos pos) {
/*  691 */     long localTime = 0L;
/*  692 */     float moonBrightness = 0.0F;
/*      */     
/*  694 */     ChunkAccess chunk = getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
/*  695 */     if (chunk != null) {
/*  696 */       localTime = chunk.getInhabitedTime();
/*  697 */       moonBrightness = getMoonBrightness(pos);
/*      */     } 
/*      */     
/*  700 */     return new DifficultyInstance(getDifficulty(), getDayTime(), localTime, moonBrightness);
/*      */   }
/*      */   
/*      */   public float getMoonBrightness(BlockPos pos) {
/*  704 */     MoonPhase moonPhase = (MoonPhase)this.environmentAttributes.getValue(EnvironmentAttributes.MOON_PHASE, pos);
/*  705 */     return DimensionType.MOON_BRIGHTNESS_PER_PHASE[moonPhase.index()];
/*      */   }
/*      */   
/*      */   private void advanceWeatherCycle() {
/*  709 */     boolean wasRaining = isRaining();
/*  710 */     if (canHaveWeather()) {
/*  711 */       if (((Boolean)getGameRules().get(GameRules.ADVANCE_WEATHER)).booleanValue()) {
/*  712 */         int clearWeatherTime = this.serverLevelData.getClearWeatherTime();
/*  713 */         int thunderTime = this.serverLevelData.getThunderTime();
/*  714 */         int rainTime = this.serverLevelData.getRainTime();
/*  715 */         boolean thundering = this.levelData.isThundering();
/*  716 */         boolean raining = this.levelData.isRaining();
/*      */ 
/*      */         
/*  719 */         if (clearWeatherTime > 0) {
/*  720 */           clearWeatherTime--;
/*  721 */           thunderTime = thundering ? 0 : 1;
/*  722 */           rainTime = raining ? 0 : 1;
/*  723 */           thundering = false;
/*  724 */           raining = false;
/*      */         } else {
/*      */           
/*  727 */           if (thunderTime > 0) {
/*  728 */             thunderTime--;
/*  729 */             if (thunderTime == 0)
/*      */             {
/*  731 */               thundering = !thundering;
/*      */             
/*      */             }
/*      */           }
/*  735 */           else if (thundering) {
/*      */             
/*  737 */             thunderTime = THUNDER_DURATION.sample(this.random);
/*      */           } else {
/*      */             
/*  740 */             thunderTime = THUNDER_DELAY.sample(this.random);
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  745 */           if (rainTime > 0) {
/*  746 */             rainTime--;
/*  747 */             if (rainTime == 0) {
/*  748 */               raining = !raining;
/*      */             }
/*      */           }
/*  751 */           else if (raining) {
/*      */             
/*  753 */             rainTime = RAIN_DURATION.sample(this.random);
/*      */           }
/*      */           else {
/*      */             
/*  757 */             rainTime = RAIN_DELAY.sample(this.random);
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  762 */         this.serverLevelData.setThunderTime(thunderTime);
/*  763 */         this.serverLevelData.setRainTime(rainTime);
/*  764 */         this.serverLevelData.setClearWeatherTime(clearWeatherTime);
/*  765 */         this.serverLevelData.setThundering(thundering);
/*  766 */         this.serverLevelData.setRaining(raining);
/*      */       } 
/*      */       
/*  769 */       this.oThunderLevel = this.thunderLevel;
/*  770 */       if (this.levelData.isThundering()) {
/*  771 */         this.thunderLevel += 0.01F;
/*      */       } else {
/*  773 */         this.thunderLevel -= 0.01F;
/*      */       } 
/*  775 */       this.thunderLevel = Mth.clamp(this.thunderLevel, 0.0F, 1.0F);
/*      */       
/*  777 */       this.oRainLevel = this.rainLevel;
/*  778 */       if (this.levelData.isRaining()) {
/*  779 */         this.rainLevel += 0.01F;
/*      */       } else {
/*  781 */         this.rainLevel -= 0.01F;
/*      */       } 
/*  783 */       this.rainLevel = Mth.clamp(this.rainLevel, 0.0F, 1.0F);
/*      */     } 
/*      */     
/*  786 */     if (this.oRainLevel != this.rainLevel) {
/*  787 */       this.server.getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, this.rainLevel), dimension());
/*      */     }
/*  789 */     if (this.oThunderLevel != this.thunderLevel) {
/*  790 */       this.server.getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, this.thunderLevel), dimension());
/*      */     }
/*      */     
/*  793 */     if (wasRaining != isRaining()) {
/*  794 */       if (wasRaining) {
/*  795 */         this.server.getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.STOP_RAINING, 0.0F));
/*      */       } else {
/*  797 */         this.server.getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.START_RAINING, 0.0F));
/*      */       } 
/*  799 */       this.server.getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, this.rainLevel));
/*  800 */       this.server.getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, this.thunderLevel));
/*      */     } 
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   public void resetWeatherCycle() {
/*  806 */     this.serverLevelData.setRainTime(0);
/*  807 */     this.serverLevelData.setRaining(false);
/*  808 */     this.serverLevelData.setThunderTime(0);
/*  809 */     this.serverLevelData.setThundering(false);
/*      */   }
/*      */ 
/*      */   
/*  813 */   public void resetEmptyTime() { this.emptyTime = 0; }
/*      */ 
/*      */   
/*      */   private void tickFluid(BlockPos pos, Fluid type) {
/*  817 */     BlockState blockState = getBlockState(pos);
/*  818 */     FluidState fluidState = blockState.getFluidState();
/*  819 */     if (fluidState.is(type)) {
/*  820 */       fluidState.tick(this, pos, blockState);
/*      */     }
/*      */   }
/*      */   
/*      */   private void tickBlock(BlockPos pos, Block type) {
/*  825 */     BlockState state = getBlockState(pos);
/*  826 */     if (state.is(type)) {
/*  827 */       state.tick(this, pos, this.random);
/*      */     }
/*      */   }
/*      */   
/*      */   public void tickNonPassenger(Entity entity) {
/*  832 */     entity.setOldPosAndRot();
/*      */     
/*  834 */     ProfilerFiller profiler = Profiler.get();
/*  835 */     entity.tickCount++;
/*  836 */     profiler.push(() -> BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
/*  837 */     profiler.incrementCounter("tickNonPassenger");
/*  838 */     entity.tick();
/*  839 */     profiler.pop();
/*      */     
/*  841 */     for (Entity passenger : entity.getPassengers()) {
/*  842 */       tickPassenger(entity, passenger);
/*      */     }
/*      */   }
/*      */   
/*      */   private void tickPassenger(Entity vehicle, Entity entity) {
/*  847 */     if (entity.isRemoved() || entity.getVehicle() != vehicle) {
/*  848 */       entity.stopRiding();
/*      */       return;
/*      */     } 
/*  851 */     if (!(entity instanceof Player) && !this.entityTickList.contains(entity)) {
/*      */       return;
/*      */     }
/*      */     
/*  855 */     entity.setOldPosAndRot();
/*      */     
/*  857 */     entity.tickCount++;
/*  858 */     ProfilerFiller profiler = Profiler.get();
/*  859 */     profiler.push(() -> BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
/*  860 */     profiler.incrementCounter("tickPassenger");
/*  861 */     entity.rideTick();
/*  862 */     profiler.pop();
/*      */     
/*  864 */     for (Entity passenger : entity.getPassengers()) {
/*  865 */       tickPassenger(entity, passenger);
/*      */     }
/*      */   }
/*      */   
/*      */   public void updateNeighboursOnBlockSet(BlockPos pos, BlockState oldState) {
/*  870 */     BlockState blockState = getBlockState(pos);
/*  871 */     Block newBlock = blockState.getBlock();
/*  872 */     boolean blockChanged = !oldState.is(newBlock);
/*      */     
/*  874 */     if (blockChanged) {
/*  875 */       oldState.affectNeighborsAfterRemoval(this, pos, false);
/*      */     }
/*      */     
/*  878 */     updateNeighborsAt(pos, blockState.getBlock());
/*  879 */     if (blockState.hasAnalogOutputSignal()) {
/*  880 */       updateNeighbourForOutputSignal(pos, newBlock);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  886 */   public boolean mayInteract(Entity entity, BlockPos pos) { if (entity instanceof Player) { Player player = (Player)entity; if (!this.server.isUnderSpawnProtection(this, pos, player) && getWorldBorder().isWithinBounds(pos)); return false; }
/*      */      }
/*      */   
/*      */   public void save(ProgressListener progressListener, boolean flush, boolean noSave) {
/*  890 */     ServerChunkCache chunkSource = getChunkSource();
/*  891 */     if (noSave) {
/*      */       return;
/*      */     }
/*      */     
/*  895 */     if (progressListener != null) {
/*  896 */       progressListener.progressStartNoAbort(Component.translatable("menu.savingLevel"));
/*      */     }
/*  898 */     saveLevelData(flush);
/*      */     
/*  900 */     if (progressListener != null) {
/*  901 */       progressListener.progressStage(Component.translatable("menu.savingChunks"));
/*      */     }
/*  903 */     chunkSource.save(flush);
/*      */     
/*  905 */     if (flush) {
/*  906 */       this.entityManager.saveAll();
/*      */     } else {
/*  908 */       this.entityManager.autoSave();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void saveLevelData(boolean sync) {
/*  913 */     if (this.dragonFight != null) {
/*  914 */       this.server.getWorldData().setEndDragonFightData(this.dragonFight.saveData());
/*      */     }
/*  916 */     DimensionDataStorage dataStorage = getChunkSource().getDataStorage();
/*      */     
/*  918 */     if (sync) {
/*  919 */       dataStorage.saveAndJoin();
/*      */     } else {
/*  921 */       dataStorage.scheduleSave();
/*      */     } 
/*      */   }
/*      */   
/*      */   public <T extends Entity> List<? extends T> getEntities(EntityTypeTest<Entity, T> type, Predicate<? super T> selector) {
/*  926 */     List<T> result = Lists.newArrayList();
/*  927 */     getEntities(type, selector, result);
/*  928 */     return result;
/*      */   }
/*      */ 
/*      */   
/*  932 */   public <T extends Entity> void getEntities(EntityTypeTest<Entity, T> type, Predicate<? super T> selector, List<? super T> result) { getEntities(type, selector, result, 2147483647); }
/*      */ 
/*      */   
/*      */   public <T extends Entity> void getEntities(EntityTypeTest<Entity, T> type, Predicate<? super T> selector, List<? super T> result, int maxResults) {
/*  936 */     getEntities().get(type, entity -> {
/*  937 */           if (selector.test(entity)) {
/*  938 */             result.add(entity);
/*  939 */             if (result.size() >= maxResults) {
/*  940 */               return AbortableIterationConsumer.Continuation.ABORT;
/*      */             }
/*      */           } 
/*  943 */           return AbortableIterationConsumer.Continuation.CONTINUE;
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*  948 */   public List<? extends EnderDragon> getDragons() { return getEntities(EntityType.ENDER_DRAGON, LivingEntity::isAlive); }
/*      */ 
/*      */ 
/*      */   
/*  952 */   public List<ServerPlayer> getPlayers(Predicate<? super ServerPlayer> selector) { return getPlayers(selector, 2147483647); }
/*      */ 
/*      */   
/*      */   public List<ServerPlayer> getPlayers(Predicate<? super ServerPlayer> selector, int maxResults) {
/*  956 */     List<ServerPlayer> result = Lists.newArrayList();
/*  957 */     for (ServerPlayer player : this.players) {
/*  958 */       if (selector.test(player)) {
/*  959 */         result.add(player);
/*  960 */         if (result.size() >= maxResults) {
/*  961 */           return result;
/*      */         }
/*      */       } 
/*      */     } 
/*  965 */     return result;
/*      */   }
/*      */   
/*      */   public ServerPlayer getRandomPlayer() {
/*  969 */     List<ServerPlayer> players = getPlayers(LivingEntity::isAlive);
/*  970 */     if (players.isEmpty()) {
/*  971 */       return null;
/*      */     }
/*  973 */     return (ServerPlayer)players.get(this.random.nextInt(players.size()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  981 */   public boolean addFreshEntity(Entity entity) { return addEntity(entity); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  988 */   public boolean addWithUUID(Entity entity) { return addEntity(entity); }
/*      */ 
/*      */   
/*      */   public void addDuringTeleport(Entity entity) {
/*  992 */     if (entity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)entity;
/*  993 */       addPlayer(player); }
/*      */     else
/*  995 */     { addEntity(entity); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/* 1000 */   public void addNewPlayer(ServerPlayer player) { addPlayer(player); }
/*      */ 
/*      */ 
/*      */   
/* 1004 */   public void addRespawnedPlayer(ServerPlayer player) { addPlayer(player); }
/*      */ 
/*      */   
/*      */   private void addPlayer(ServerPlayer player) {
/* 1008 */     Entity existing = getEntity(player.getUUID());
/* 1009 */     if (existing != null) {
/* 1010 */       LOGGER.warn("Force-added player with duplicate UUID {}", player.getUUID());
/* 1011 */       existing.unRide();
/* 1012 */       removePlayerImmediately((ServerPlayer)existing, Entity.RemovalReason.DISCARDED);
/*      */     } 
/* 1014 */     this.entityManager.addNewEntity(player);
/*      */   }
/*      */   
/*      */   private boolean addEntity(Entity entity) {
/* 1018 */     if (entity.isRemoved()) {
/* 1019 */       LOGGER.warn("Tried to add entity {} but it was marked as removed already", EntityType.getKey(entity.getType()));
/* 1020 */       return false;
/*      */     } 
/*      */     
/* 1023 */     return this.entityManager.addNewEntity(entity);
/*      */   }
/*      */   
/*      */   public boolean tryAddFreshEntityWithPassengers(Entity entity) {
/* 1027 */     Objects.requireNonNull(this.entityManager); if (entity.getSelfAndPassengers().map(Entity::getUUID).anyMatch(this.entityManager::isLoaded)) {
/* 1028 */       return false;
/*      */     }
/*      */     
/* 1031 */     addFreshEntityWithPassengers(entity);
/* 1032 */     return true;
/*      */   }
/*      */   
/*      */   public void unload(LevelChunk levelChunk) {
/* 1036 */     levelChunk.clearAllBlockEntities();
/* 1037 */     levelChunk.unregisterTickContainerFromLevel(this);
/* 1038 */     this.debugSynchronizers.dropChunk(levelChunk.getPos());
/*      */   }
/*      */ 
/*      */   
/* 1042 */   public void removePlayerImmediately(ServerPlayer player, Entity.RemovalReason reason) { player.remove(reason); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void destroyBlockProgress(int id, BlockPos blockPos, int progress) {
/* 1047 */     for (ServerPlayer player : this.server.getPlayerList().getPlayers()) {
/* 1048 */       if (player.level() != this || player.getId() == id) {
/*      */         continue;
/*      */       }
/* 1051 */       double xd = blockPos.getX() - player.getX();
/* 1052 */       double yd = blockPos.getY() - player.getY();
/* 1053 */       double zd = blockPos.getZ() - player.getZ();
/*      */       
/* 1055 */       if (xd * xd + yd * yd + zd * zd < 1024.0D) {
/* 1056 */         player.connection.send(new ClientboundBlockDestructionPacket(id, blockPos, progress));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void playSeededSound(Entity except, double x, double y, double z, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch, long seed) {
/* 1063 */     Player player = (Player)except; this.server.getPlayerList().broadcast((except instanceof Player) ? player : null, x, y, z, ((SoundEvent)sound.value()).getRange(volume), dimension(), new ClientboundSoundPacket(sound, source, x, y, z, volume, pitch, seed));
/*      */   }
/*      */ 
/*      */   
/*      */   public void playSeededSound(Entity except, Entity sourceEntity, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch, long seed) {
/* 1068 */     Player player = (Player)except; this.server.getPlayerList().broadcast((except instanceof Player) ? player : null, sourceEntity.getX(), sourceEntity.getY(), sourceEntity.getZ(), ((SoundEvent)sound.value()).getRange(volume), dimension(), new ClientboundSoundEntityPacket(sound, source, sourceEntity, volume, pitch, seed));
/*      */   }
/*      */ 
/*      */   
/*      */   public void globalLevelEvent(int type, BlockPos pos, int data) {
/* 1073 */     if (((Boolean)getGameRules().get(GameRules.GLOBAL_SOUND_EVENTS)).booleanValue()) {
/* 1074 */       this.server.getPlayerList().getPlayers().forEach(player -> {
/*      */             Vec3 soundPos;
/*      */             
/* 1077 */             if (player.level() == this) {
/*      */               
/* 1079 */               Vec3 centerOfBlock = Vec3.atCenterOf(pos);
/* 1080 */               if (player.distanceToSqr(centerOfBlock) < Mth.square(32)) {
/* 1081 */                 soundPos = centerOfBlock;
/*      */               } else {
/* 1083 */                 Vec3 directionToEvent = centerOfBlock.subtract(player.position()).normalize();
/* 1084 */                 soundPos = player.position().add(directionToEvent.scale(32.0D));
/*      */               } 
/*      */             } else {
/* 1087 */               soundPos = player.position();
/*      */             } 
/* 1089 */             player.connection.send(new ClientboundLevelEventPacket(type, BlockPos.containing(soundPos), data, true));
/*      */           });
/*      */     } else {
/*      */       
/* 1093 */       levelEvent(null, type, pos, data);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void levelEvent(Entity source, int type, BlockPos pos, int data) {
/* 1099 */     Player player = (Player)source; this.server.getPlayerList().broadcast((source instanceof Player) ? player : null, pos.getX(), pos.getY(), pos.getZ(), 64.0D, dimension(), new ClientboundLevelEventPacket(type, pos, data, false));
/*      */   }
/*      */ 
/*      */   
/* 1103 */   public int getLogicalHeight() { return dimensionType().logicalHeight(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1108 */   public void gameEvent(Holder<GameEvent> gameEvent, Vec3 position, GameEvent.Context context) { this.gameEventDispatcher.post(gameEvent, position, context); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendBlockUpdated(BlockPos pos, BlockState old, BlockState current, int updateFlags) {
/* 1113 */     if (this.isUpdatingNavigations) {
/* 1114 */       String message = "recursive call to sendBlockUpdated";
/* 1115 */       Util.logAndPauseIfInIde("recursive call to sendBlockUpdated", new IllegalStateException("recursive call to sendBlockUpdated"));
/*      */     } 
/* 1117 */     getChunkSource().blockChanged(pos);
/* 1118 */     this.pathTypesByPosCache.invalidate(pos);
/*      */     
/* 1120 */     VoxelShape oldShape = old.getCollisionShape(this, pos);
/* 1121 */     VoxelShape newShape = current.getCollisionShape(this, pos);
/* 1122 */     if (!Shapes.joinIsNotEmpty(oldShape, newShape, BooleanOp.NOT_SAME)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1129 */     ObjectArrayList objectArrayList = new ObjectArrayList();
/*      */     
/* 1131 */     for (Mob navigatingMob : this.navigatingMobs) {
/* 1132 */       PathNavigation pathNavigation = navigatingMob.getNavigation();
/* 1133 */       if (pathNavigation.shouldRecomputePath(pos)) {
/* 1134 */         objectArrayList.add(pathNavigation);
/*      */       }
/*      */     } 
/*      */     
/*      */     try {
/* 1139 */       this.isUpdatingNavigations = true;
/* 1140 */       for (PathNavigation navigation : objectArrayList) {
/* 1141 */         navigation.recomputePath();
/*      */       }
/*      */     } finally {
/* 1144 */       this.isUpdatingNavigations = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1150 */   public void updateNeighborsAt(BlockPos pos, Block sourceBlock) { updateNeighborsAt(pos, sourceBlock, ExperimentalRedstoneUtils.initialOrientation(this, null, null)); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1155 */   public void updateNeighborsAt(BlockPos pos, Block sourceBlock, Orientation orientation) { this.neighborUpdater.updateNeighborsAtExceptFromFacing(pos, sourceBlock, null, orientation); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1160 */   public void updateNeighborsAtExceptFromFacing(BlockPos pos, Block blockObject, Direction skipDirection, Orientation orientation) { this.neighborUpdater.updateNeighborsAtExceptFromFacing(pos, blockObject, skipDirection, orientation); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1165 */   public void neighborChanged(BlockPos pos, Block changedBlock, Orientation orientation) { this.neighborUpdater.neighborChanged(pos, changedBlock, orientation); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1170 */   public void neighborChanged(BlockState state, BlockPos pos, Block changedBlock, Orientation orientation, boolean movedByPiston) { this.neighborUpdater.neighborChanged(state, pos, changedBlock, orientation, movedByPiston); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1175 */   public void broadcastEntityEvent(Entity entity, byte event) { getChunkSource().sendToTrackingPlayersAndSelf(entity, new ClientboundEntityEventPacket(entity, event)); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1180 */   public void broadcastDamageEvent(Entity entity, DamageSource source) { getChunkSource().sendToTrackingPlayersAndSelf(entity, new ClientboundDamageEventPacket(entity, source)); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1185 */   public ServerChunkCache getChunkSource() { return this.chunkSource; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void explode(Entity source, DamageSource damageSource, ExplosionDamageCalculator damageCalculator, double x, double y, double z, float r, boolean fire, Level.ExplosionInteraction interactionType, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, WeightedList<ExplosionParticleInfo> blockParticles, Holder<SoundEvent> explosionSound) {
/* 1190 */     switch (interactionType) { default: throw new MatchException(null, null);
/*      */       case NONE: 
/*      */       case BLOCK: 
/* 1193 */       case MOB: if (((Boolean)getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue());
/*      */       case TNT:
/*      */       
/*      */       case TRIGGER:
/* 1197 */         break; }  Explosion.BlockInteraction blockInteraction = Explosion.BlockInteraction.TRIGGER_BLOCK;
/*      */ 
/*      */     
/* 1200 */     Vec3 center = new Vec3(x, y, z);
/* 1201 */     ServerExplosion explosion = new ServerExplosion(this, source, damageSource, damageCalculator, center, r, fire, blockInteraction);
/* 1202 */     int blockCount = explosion.explode();
/*      */     
/* 1204 */     ParticleOptions explosionParticle = explosion.isSmall() ? smallExplosionParticles : largeExplosionParticles;
/*      */     
/* 1206 */     for (ServerPlayer player : this.players) {
/* 1207 */       if (player.distanceToSqr(center) < 4096.0D) {
/* 1208 */         Optional<Vec3> playerKnockback = Optional.ofNullable((Vec3)explosion.getHitPlayers().get(player));
/* 1209 */         player.connection.send(new ClientboundExplodePacket(center, r, blockCount, playerKnockback, explosionParticle, explosionSound, blockParticles));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 1215 */   private Explosion.BlockInteraction getDestroyType(GameRule<Boolean> gameRule) { return ((Boolean)getGameRules().get(gameRule)).booleanValue() ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.DESTROY; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1220 */   public void blockEvent(BlockPos pos, Block block, int b0, int b1) { this.blockEvents.add(new BlockEventData(pos, block, b0, b1)); }
/*      */ 
/*      */   
/*      */   private void runBlockEvents() {
/* 1224 */     this.blockEventsToReschedule.clear();
/* 1225 */     while (!this.blockEvents.isEmpty()) {
/* 1226 */       BlockEventData eventData = (BlockEventData)this.blockEvents.removeFirst();
/* 1227 */       if (shouldTickBlocksAt(eventData.pos())) {
/* 1228 */         if (doBlockEvent(eventData))
/* 1229 */           this.server.getPlayerList().broadcast(null, eventData.pos().getX(), eventData.pos().getY(), eventData.pos().getZ(), 64.0D, dimension(), new ClientboundBlockEventPacket(eventData.pos(), eventData.block(), eventData.paramA(), eventData.paramB())); 
/*      */         continue;
/*      */       } 
/* 1232 */       this.blockEventsToReschedule.add(eventData);
/*      */     } 
/*      */     
/* 1235 */     this.blockEvents.addAll(this.blockEventsToReschedule);
/*      */   }
/*      */   
/*      */   private boolean doBlockEvent(BlockEventData eventData) {
/* 1239 */     BlockState state = getBlockState(eventData.pos());
/* 1240 */     if (state.is(eventData.block())) {
/* 1241 */       return state.triggerEvent(this, eventData.pos(), eventData.paramA(), eventData.paramB());
/*      */     }
/* 1243 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1248 */   public LevelTicks<Block> getBlockTicks() { return this.blockTicks; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1253 */   public LevelTicks<Fluid> getFluidTicks() { return this.fluidTicks; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1258 */   public MinecraftServer getServer() { return this.server; }
/*      */ 
/*      */ 
/*      */   
/* 1262 */   public PortalForcer getPortalForcer() { return this.portalForcer; }
/*      */ 
/*      */ 
/*      */   
/* 1266 */   public StructureTemplateManager getStructureManager() { return this.server.getStructureManager(); }
/*      */ 
/*      */ 
/*      */   
/* 1270 */   public <T extends ParticleOptions> int sendParticles(T particle, double x, double y, double z, int count, double xDist, double yDist, double zDist, double speed) { return sendParticles(particle, false, false, x, y, z, count, xDist, yDist, zDist, speed); }
/*      */ 
/*      */   
/*      */   public <T extends ParticleOptions> int sendParticles(T particle, boolean overrideLimiter, boolean alwaysShow, double x, double y, double z, int count, double xDist, double yDist, double zDist, double speed) {
/* 1274 */     ClientboundLevelParticlesPacket packet = new ClientboundLevelParticlesPacket(particle, overrideLimiter, alwaysShow, x, y, z, (float)xDist, (float)yDist, (float)zDist, (float)speed, count);
/* 1275 */     int result = 0;
/*      */     
/* 1277 */     for (int i = 0; i < this.players.size(); i++) {
/* 1278 */       ServerPlayer player = (ServerPlayer)this.players.get(i);
/*      */       
/* 1280 */       if (sendParticles(player, overrideLimiter, x, y, z, packet)) {
/* 1281 */         result++;
/*      */       }
/*      */     } 
/*      */     
/* 1285 */     return result;
/*      */   }
/*      */   
/*      */   public <T extends ParticleOptions> boolean sendParticles(ServerPlayer player, T particle, boolean overrideLimiter, boolean alwaysShow, double x, double y, double z, int count, double xDist, double yDist, double zDist, double speed) {
/* 1289 */     ClientboundLevelParticlesPacket clientboundLevelParticlesPacket = new ClientboundLevelParticlesPacket(particle, overrideLimiter, alwaysShow, x, y, z, (float)xDist, (float)yDist, (float)zDist, (float)speed, count);
/*      */     
/* 1291 */     return sendParticles(player, overrideLimiter, x, y, z, clientboundLevelParticlesPacket);
/*      */   }
/*      */   
/*      */   private boolean sendParticles(ServerPlayer player, boolean overrideLimiter, double x, double y, double z, Packet<?> packet) {
/* 1295 */     if (player.level() != this) {
/* 1296 */       return false;
/*      */     }
/*      */     
/* 1299 */     BlockPos pos = player.blockPosition();
/*      */     
/* 1301 */     if (pos.closerToCenterThan(new Vec3(x, y, z), overrideLimiter ? 512.0D : 32.0D)) {
/* 1302 */       player.connection.send(packet);
/* 1303 */       return true;
/*      */     } 
/*      */     
/* 1306 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1311 */   public Entity getEntity(int id) { return (Entity)getEntities().get(id); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Entity getEntityInAnyDimension(UUID uuid) {
/* 1317 */     Entity entity = getEntity(uuid);
/* 1318 */     if (entity != null) {
/* 1319 */       return entity;
/*      */     }
/*      */     
/* 1322 */     for (ServerLevel otherLevel : getServer().getAllLevels()) {
/* 1323 */       if (otherLevel == this) {
/*      */         continue;
/*      */       }
/*      */       
/* 1327 */       Entity otherEntity = otherLevel.getEntity(uuid);
/* 1328 */       if (otherEntity != null) {
/* 1329 */         return otherEntity;
/*      */       }
/*      */     } 
/*      */     
/* 1333 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1338 */   public Player getPlayerInAnyDimension(UUID uuid) { return getServer().getPlayerList().getPlayer(uuid); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Entity getEntityOrPart(int id) {
/* 1348 */     Entity entity = (Entity)getEntities().get(id);
/* 1349 */     if (entity != null) {
/* 1350 */       return entity;
/*      */     }
/* 1352 */     return (Entity)this.dragonParts.get(id);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1357 */   public Collection<EnderDragonPart> dragonParts() { return this.dragonParts.values(); }
/*      */ 
/*      */ 
/*      */   
/*      */   public BlockPos findNearestMapStructure(TagKey<Structure> structureTag, BlockPos origin, int maxSearchRadius, boolean createReference) {
/* 1362 */     if (!this.server.getWorldData().worldGenOptions().generateStructures()) {
/* 1363 */       return null;
/*      */     }
/* 1365 */     Optional<HolderSet.Named<Structure>> tag = registryAccess().lookupOrThrow(Registries.STRUCTURE).get(structureTag);
/* 1366 */     if (tag.isEmpty()) {
/* 1367 */       return null;
/*      */     }
/* 1369 */     Pair<BlockPos, Holder<Structure>> result = getChunkSource().getGenerator().findNearestMapStructure(this, (HolderSet)tag.get(), origin, maxSearchRadius, createReference);
/* 1370 */     return (result != null) ? (BlockPos)result.getFirst() : null;
/*      */   }
/*      */ 
/*      */   
/* 1374 */   public Pair<BlockPos, Holder<Biome>> findClosestBiome3d(Predicate<Holder<Biome>> biomeTest, BlockPos origin, int maxSearchRadius, int sampleResolutionHorizontal, int sampleResolutionVertical) { return getChunkSource().getGenerator().getBiomeSource().findClosestBiome3d(origin, maxSearchRadius, sampleResolutionHorizontal, sampleResolutionVertical, biomeTest, getChunkSource().randomState().sampler(), this); }
/*      */ 
/*      */ 
/*      */   
/*      */   public WorldBorder getWorldBorder() {
/* 1379 */     WorldBorder worldBorder = (WorldBorder)getDataStorage().computeIfAbsent(WorldBorder.TYPE);
/* 1380 */     worldBorder.applyInitialSettings(this.levelData.getGameTime());
/* 1381 */     return worldBorder;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1386 */   public RecipeManager recipeAccess() { return this.server.getRecipeManager(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1391 */   public TickRateManager tickRateManager() { return this.server.tickRateManager(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1396 */   public boolean noSave() { return this.noSave; }
/*      */ 
/*      */ 
/*      */   
/* 1400 */   public DimensionDataStorage getDataStorage() { return getChunkSource().getDataStorage(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1405 */   public MapItemSavedData getMapData(MapId id) { return (MapItemSavedData)getServer().overworld().getDataStorage().get(MapItemSavedData.type(id)); }
/*      */ 
/*      */ 
/*      */   
/* 1409 */   public void setMapData(MapId id, MapItemSavedData data) { getServer().overworld().getDataStorage().set(MapItemSavedData.type(id), data); }
/*      */ 
/*      */ 
/*      */   
/* 1413 */   public MapId getFreeMapId() { return ((MapIndex)getServer().overworld().getDataStorage().computeIfAbsent(MapIndex.TYPE)).getNextMapId(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1418 */   public void setRespawnData(LevelData.RespawnData respawnData) { getServer().setRespawnData(respawnData); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1423 */   public LevelData.RespawnData getRespawnData() { return getServer().getRespawnData(); }
/*      */ 
/*      */ 
/*      */   
/* 1427 */   public LongSet getForceLoadedChunks() { return this.chunkSource.getForceLoadedChunks(); }
/*      */ 
/*      */   
/*      */   public boolean setChunkForced(int chunkX, int chunkZ, boolean forced) {
/* 1431 */     boolean updated = this.chunkSource.updateChunkForced(new ChunkPos(chunkX, chunkZ), forced);
/* 1432 */     if (forced && updated) {
/* 1433 */       getChunk(chunkX, chunkZ);
/*      */     }
/* 1435 */     return updated;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1440 */   public List<ServerPlayer> players() { return this.players; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updatePOIOnBlockStateChange(BlockPos pos, BlockState oldState, BlockState newState) {
/* 1445 */     Optional<Holder<PoiType>> oldType = PoiTypes.forState(oldState);
/* 1446 */     Optional<Holder<PoiType>> newType = PoiTypes.forState(newState);
/*      */     
/* 1448 */     if (Objects.equals(oldType, newType)) {
/*      */       return;
/*      */     }
/*      */     
/* 1452 */     BlockPos immutable = pos.immutable();
/* 1453 */     oldType.ifPresent(poiType -> getServer().execute(()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1458 */     newType.ifPresent(poiType -> getServer().execute(()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1467 */   public PoiManager getPoiManager() { return getChunkSource().getPoiManager(); }
/*      */ 
/*      */ 
/*      */   
/* 1471 */   public boolean isVillage(BlockPos pos) { return isCloseToVillage(pos, 1); }
/*      */ 
/*      */ 
/*      */   
/* 1475 */   public boolean isVillage(SectionPos sectionPos) { return isVillage(sectionPos.center()); }
/*      */ 
/*      */   
/*      */   public boolean isCloseToVillage(BlockPos pos, int sectionDistance) {
/* 1479 */     if (sectionDistance > 6) {
/* 1480 */       return false;
/*      */     }
/* 1482 */     return (sectionsToVillage(SectionPos.of(pos)) <= sectionDistance);
/*      */   }
/*      */ 
/*      */   
/* 1486 */   public int sectionsToVillage(SectionPos pos) { return getPoiManager().sectionsToVillage(pos); }
/*      */ 
/*      */ 
/*      */   
/* 1490 */   public Raids getRaids() { return this.raids; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1497 */   public Raid getRaidAt(BlockPos pos) { return this.raids.getNearbyRaid(pos, 9216); }
/*      */ 
/*      */ 
/*      */   
/* 1501 */   public boolean isRaided(BlockPos pos) { return (getRaidAt(pos) != null); }
/*      */ 
/*      */ 
/*      */   
/* 1505 */   public void onReputationEvent(ReputationEventType type, Entity source, ReputationEventHandler target) { target.onReputationEventFrom(type, source); }
/*      */ 
/*      */   
/*      */   public void saveDebugReport(Path rootDir) throws IOException {
/* 1509 */     ChunkMap chunkMap = (getChunkSource()).chunkMap;
/*      */     
/* 1511 */     Writer output = Files.newBufferedWriter(rootDir.resolve("stats.txt"), new java.nio.file.OpenOption[0]); 
/* 1512 */     try { output.write(String.format(Locale.ROOT, "spawning_chunks: %d\n", new Object[] { Integer.valueOf(chunkMap.getDistanceManager().getNaturalSpawnChunkCount()) }));
/* 1513 */       NaturalSpawner.SpawnState lastSpawnState = getChunkSource().getLastSpawnState();
/* 1514 */       if (lastSpawnState != null) {
/* 1515 */         for (ObjectIterator objectIterator = lastSpawnState.getMobCategoryCounts().object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry<MobCategory> entry = (Object2IntMap.Entry)objectIterator.next();
/* 1516 */           output.write(String.format(Locale.ROOT, "spawn_count.%s: %d\n", new Object[] { ((MobCategory)entry.getKey()).getName(), Integer.valueOf(entry.getIntValue()) })); }
/*      */       
/*      */       }
/* 1519 */       output.write(String.format(Locale.ROOT, "entities: %s\n", new Object[] { this.entityManager.gatherStats() }));
/* 1520 */       output.write(String.format(Locale.ROOT, "block_entity_tickers: %d\n", new Object[] { Integer.valueOf(this.blockEntityTickers.size()) }));
/* 1521 */       output.write(String.format(Locale.ROOT, "block_ticks: %d\n", new Object[] { Integer.valueOf(getBlockTicks().count()) }));
/* 1522 */       output.write(String.format(Locale.ROOT, "fluid_ticks: %d\n", new Object[] { Integer.valueOf(getFluidTicks().count()) }));
/* 1523 */       output.write("distance_manager: " + chunkMap.getDistanceManager().getDebugStatus() + "\n");
/* 1524 */       output.write(String.format(Locale.ROOT, "pending_tasks: %d\n", new Object[] { Integer.valueOf(getChunkSource().getPendingTasksCount()) }));
/* 1525 */       if (output != null) output.close();  } catch (Throwable throwable) { if (output != null)
/*      */         try { output.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 1527 */      CrashReport test = new CrashReport("Level dump", new Exception("dummy"));
/* 1528 */     fillReportDetails(test);
/* 1529 */     Writer output = Files.newBufferedWriter(rootDir.resolve("example_crash.txt"), new java.nio.file.OpenOption[0]); 
/* 1530 */     try { output.write(test.getFriendlyReport(ReportType.TEST));
/* 1531 */       if (output != null) output.close();  } catch (Throwable throwable) { if (output != null)
/*      */         try { output.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 1533 */      Path chunks = rootDir.resolve("chunks.csv");
/* 1534 */     Writer output = Files.newBufferedWriter(chunks, new java.nio.file.OpenOption[0]); 
/* 1535 */     try { chunkMap.dumpChunks(output);
/* 1536 */       if (output != null) output.close();  } catch (Throwable throwable) { if (output != null)
/*      */         try { output.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 1538 */      Path entityChunks = rootDir.resolve("entity_chunks.csv");
/* 1539 */     Writer output = Files.newBufferedWriter(entityChunks, new java.nio.file.OpenOption[0]); 
/* 1540 */     try { this.entityManager.dumpSections(output);
/* 1541 */       if (output != null) output.close();  } catch (Throwable throwable) { if (output != null)
/*      */         try { output.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 1543 */      Path entities = rootDir.resolve("entities.csv");
/* 1544 */     Writer output = Files.newBufferedWriter(entities, new java.nio.file.OpenOption[0]); 
/* 1545 */     try { dumpEntities(output, getEntities().getAll());
/* 1546 */       if (output != null) output.close();  } catch (Throwable throwable) { if (output != null)
/*      */         try { output.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 1548 */      Path blockEntities = rootDir.resolve("block_entities.csv");
/* 1549 */     Writer output = Files.newBufferedWriter(blockEntities, new java.nio.file.OpenOption[0]); try {
/* 1550 */       dumpBlockEntityTickers(output);
/* 1551 */       if (output != null) output.close(); 
/*      */     } catch (Throwable throwable) {
/*      */       if (output != null)
/*      */         try {
/*      */           output.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void dumpEntities(Writer output, Iterable<Entity> entities) throws IOException {
/* 1564 */     CsvOutput csvOutput = CsvOutput.builder().addColumn("x").addColumn("y").addColumn("z").addColumn("uuid").addColumn("type").addColumn("alive").addColumn("display_name").addColumn("custom_name").build(output);
/*      */     
/* 1566 */     for (Entity entity : entities) {
/* 1567 */       Component customName = entity.getCustomName();
/* 1568 */       Component displayName = entity.getDisplayName();
/* 1569 */       csvOutput.writeRow(new Object[] {
/* 1570 */             Double.valueOf(entity.getX()), 
/* 1571 */             Double.valueOf(entity.getY()), 
/* 1572 */             Double.valueOf(entity.getZ()), entity
/* 1573 */             .getUUID(), BuiltInRegistries.ENTITY_TYPE
/* 1574 */             .getKey(entity.getType()), 
/* 1575 */             Boolean.valueOf(entity.isAlive()), displayName
/* 1576 */             .getString(), 
/* 1577 */             (customName != null) ? customName.getString() : null
/*      */           });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void dumpBlockEntityTickers(Writer output) throws IOException {
/* 1588 */     CsvOutput csvOutput = CsvOutput.builder().addColumn("x").addColumn("y").addColumn("z").addColumn("type").build(output);
/*      */     
/* 1590 */     for (TickingBlockEntity ticker : this.blockEntityTickers) {
/* 1591 */       BlockPos blockPos = ticker.getPos();
/* 1592 */       csvOutput.writeRow(new Object[] {
/* 1593 */             Integer.valueOf(blockPos.getX()), 
/* 1594 */             Integer.valueOf(blockPos.getY()), 
/* 1595 */             Integer.valueOf(blockPos.getZ()), ticker
/* 1596 */             .getType()
/*      */           });
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/* 1603 */   public void clearBlockEvents(BoundingBox bb) { this.blockEvents.removeIf(e -> bb.isInside(e.pos())); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1608 */   public float getShade(Direction direction, boolean shade) { return 1.0F; }
/*      */ 
/*      */ 
/*      */   
/* 1612 */   public Iterable<Entity> getAllEntities() { return getEntities().getAll(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1617 */   public String toString() { return "ServerLevel[" + this.serverLevelData.getLevelName() + "]"; }
/*      */ 
/*      */ 
/*      */   
/* 1621 */   public boolean isFlat() { return this.server.getWorldData().isFlatWorld(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1626 */   public long getSeed() { return this.server.getWorldData().worldGenOptions().seed(); }
/*      */ 
/*      */ 
/*      */   
/* 1630 */   public EndDragonFight getDragonFight() { return this.dragonFight; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1635 */   public ServerLevel getLevel() { return this; }
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   public String getWatchdogStats() {
/* 1640 */     return String.format(Locale.ROOT, "players: %s, entities: %s [%s], block_entities: %d [%s], block_ticks: %d, fluid_ticks: %d, chunk_source: %s", new Object[] {
/* 1641 */           Integer.valueOf(this.players.size()), this.entityManager
/* 1642 */           .gatherStats(), 
/* 1643 */           getTypeCount(this.entityManager.getEntityGetter().getAll(), e -> BuiltInRegistries.ENTITY_TYPE.getKey(e.getType()).toString()), 
/* 1644 */           Integer.valueOf(this.blockEntityTickers.size()), 
/* 1645 */           getTypeCount(this.blockEntityTickers, TickingBlockEntity::getType), 
/* 1646 */           Integer.valueOf(getBlockTicks().count()), 
/* 1647 */           Integer.valueOf(getFluidTicks().count()), 
/* 1648 */           gatherChunkSourceStats()
/*      */         });
/*      */   }
/*      */   
/*      */   private static <T> String getTypeCount(Iterable<T> values, Function<T, String> typeGetter) {
/*      */     try {
/* 1654 */       Object2IntOpenHashMap<String> countByType = new Object2IntOpenHashMap<String>();
/* 1655 */       for (T e : values) {
/* 1656 */         String type = (String)typeGetter.apply(e);
/* 1657 */         countByType.addTo(type, 1);
/*      */       } 
/* 1659 */       return (String)countByType.object2IntEntrySet().stream()
/* 1660 */         .sorted(Comparator.comparing(Object2IntMap.Entry::getIntValue).reversed())
/* 1661 */         .limit(5L)
/* 1662 */         .map(e -> (String)e.getKey() + ":" + (String)e.getKey())
/* 1663 */         .collect(Collectors.joining(","));
/* 1664 */     } catch (Exception e) {
/* 1665 */       return "";
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1671 */   protected LevelEntityGetter<Entity> getEntities() { return this.entityManager.getEntityGetter(); }
/*      */ 
/*      */ 
/*      */   
/* 1675 */   public void addLegacyChunkEntities(Stream<Entity> loaded) { this.entityManager.addLegacyChunkEntities(loaded); }
/*      */ 
/*      */ 
/*      */   
/* 1679 */   public void addWorldGenChunkEntities(Stream<Entity> loaded) { this.entityManager.addWorldGenChunkEntities(loaded); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1686 */   public void startTickingChunk(LevelChunk levelChunk) { levelChunk.unpackTicks(getGameTime()); }
/*      */ 
/*      */ 
/*      */   
/* 1690 */   public void onStructureStartsAvailable(ChunkAccess chunk) { this.server.execute(() -> this.structureCheck.onStructureLoad(chunk.getPos(), chunk.getAllStarts())); }
/*      */ 
/*      */ 
/*      */   
/* 1694 */   public PathTypeCache getPathTypeCache() { return this.pathTypesByPosCache; }
/*      */ 
/*      */   
/*      */   public void waitForEntities(ChunkPos centerChunk, int radius) {
/* 1698 */     List<ChunkPos> chunks = ChunkPos.rangeClosed(centerChunk, radius).toList();
/*      */     
/* 1700 */     this.server.managedBlock(() -> {
/* 1701 */           this.entityManager.processPendingLoads();
/* 1702 */           for (ChunkPos chunk : chunks) {
/* 1703 */             if (!areEntitiesLoaded(chunk.toLong())) {
/* 1704 */               return false;
/*      */             }
/*      */           } 
/* 1707 */           return true;
/*      */         });
/*      */   }
/*      */ 
/*      */   
/* 1712 */   public boolean isSpawningMonsters() { return (getLevelData().getDifficulty() != Difficulty.PEACEFUL && ((Boolean)getGameRules().get(GameRules.SPAWN_MOBS)).booleanValue() && ((Boolean)getGameRules().get(GameRules.SPAWN_MONSTERS)).booleanValue()); }
/*      */   
/*      */   private final class EntityCallbacks
/*      */     extends Object
/*      */     implements LevelCallback<Entity> {
/*      */     public void onCreated(Entity entity) {
/* 1718 */       if (entity instanceof WaypointTransmitter) { WaypointTransmitter waypoint = (WaypointTransmitter)entity; if (waypoint.isTransmittingWaypoint()) {
/* 1719 */           ServerLevel.this.getWaypointManager().trackWaypoint(waypoint);
/*      */         } }
/*      */     
/*      */     }
/*      */     
/*      */     public void onDestroyed(Entity entity) {
/* 1725 */       if (entity instanceof WaypointTransmitter) { WaypointTransmitter waypoint = (WaypointTransmitter)entity;
/* 1726 */         ServerLevel.this.getWaypointManager().untrackWaypoint(waypoint); }
/*      */       
/* 1728 */       ServerLevel.this.getScoreboard().entityRemoved(entity);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1733 */     public void onTickingStart(Entity entity) { ServerLevel.this.entityTickList.add(entity); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1738 */     public void onTickingEnd(Entity entity) { ServerLevel.this.entityTickList.remove(entity); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void onTrackingStart(Entity entity) {
/* 1743 */       ServerLevel.this.getChunkSource().addEntity(entity);
/* 1744 */       if (entity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)entity;
/* 1745 */         ServerLevel.this.players.add(player);
/* 1746 */         if (player.isReceivingWaypoints()) {
/* 1747 */           ServerLevel.this.getWaypointManager().addPlayer(player);
/*      */         }
/* 1749 */         ServerLevel.this.updateSleepingPlayerList(); }
/*      */       
/* 1751 */       if (entity instanceof WaypointTransmitter) { WaypointTransmitter waypoint = (WaypointTransmitter)entity; if (waypoint.isTransmittingWaypoint())
/* 1752 */           ServerLevel.this.getWaypointManager().trackWaypoint(waypoint);  }
/*      */       
/* 1754 */       if (entity instanceof Mob) { Mob mob = (Mob)entity;
/* 1755 */         if (ServerLevel.this.isUpdatingNavigations) {
/* 1756 */           String message = "onTrackingStart called during navigation iteration";
/* 1757 */           Util.logAndPauseIfInIde("onTrackingStart called during navigation iteration", new IllegalStateException("onTrackingStart called during navigation iteration"));
/*      */         } 
/* 1759 */         ServerLevel.this.navigatingMobs.add(mob); }
/*      */       
/* 1761 */       if (entity instanceof EnderDragon) { EnderDragon dragon = (EnderDragon)entity;
/* 1762 */         for (EnderDragonPart subEntity : dragon.getSubEntities()) {
/* 1763 */           ServerLevel.this.dragonParts.put(subEntity.getId(), subEntity);
/*      */         } }
/*      */ 
/*      */       
/* 1767 */       entity.updateDynamicGameEventListener(DynamicGameEventListener::add);
/*      */     }
/*      */ 
/*      */     
/*      */     public void onTrackingEnd(Entity entity) {
/* 1772 */       ServerLevel.this.getChunkSource().removeEntity(entity);
/* 1773 */       if (entity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)entity;
/* 1774 */         ServerLevel.this.players.remove(player);
/* 1775 */         ServerLevel.this.getWaypointManager().removePlayer(player);
/* 1776 */         ServerLevel.this.updateSleepingPlayerList(); }
/*      */       
/* 1778 */       if (entity instanceof Mob) { Mob mob = (Mob)entity;
/* 1779 */         if (ServerLevel.this.isUpdatingNavigations) {
/* 1780 */           String message = "onTrackingStart called during navigation iteration";
/* 1781 */           Util.logAndPauseIfInIde("onTrackingStart called during navigation iteration", new IllegalStateException("onTrackingStart called during navigation iteration"));
/*      */         } 
/* 1783 */         ServerLevel.this.navigatingMobs.remove(mob); }
/*      */       
/* 1785 */       if (entity instanceof EnderDragon) { EnderDragon dragon = (EnderDragon)entity;
/* 1786 */         for (EnderDragonPart subEntity : dragon.getSubEntities()) {
/* 1787 */           ServerLevel.this.dragonParts.remove(subEntity.getId());
/*      */         } }
/*      */ 
/*      */       
/* 1791 */       entity.updateDynamicGameEventListener(DynamicGameEventListener::remove);
/*      */       
/* 1793 */       ServerLevel.this.debugSynchronizers.dropEntity(entity);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1798 */     public void onSectionChange(Entity entity) { entity.updateDynamicGameEventListener(DynamicGameEventListener::move); }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() {
/* 1804 */     super.close();
/* 1805 */     this.entityManager.close();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1810 */   public String gatherChunkSourceStats() { return "Chunks[S] W: " + this.chunkSource.gatherStats() + " E: " + this.entityManager.gatherStats(); }
/*      */ 
/*      */ 
/*      */   
/* 1814 */   public boolean areEntitiesLoaded(long chunkKey) { return this.entityManager.areEntitiesLoaded(chunkKey); }
/*      */ 
/*      */ 
/*      */   
/* 1818 */   public boolean isPositionTickingWithEntitiesLoaded(long key) { return (areEntitiesLoaded(key) && this.chunkSource.isPositionTicking(key)); }
/*      */ 
/*      */ 
/*      */   
/* 1822 */   public boolean isPositionEntityTicking(BlockPos pos) { return (this.entityManager.canPositionTick(pos) && this.chunkSource.chunkMap.getDistanceManager().inEntityTickingRange(ChunkPos.asLong(pos))); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1828 */   public boolean areEntitiesActuallyLoadedAndTicking(ChunkPos pos) { return (this.entityManager.isTicking(pos) && this.entityManager.areEntitiesLoaded(pos.toLong())); }
/*      */ 
/*      */ 
/*      */   
/* 1832 */   public boolean anyPlayerCloseEnoughForSpawning(BlockPos pos) { return anyPlayerCloseEnoughForSpawning(new ChunkPos(pos)); }
/*      */ 
/*      */ 
/*      */   
/* 1836 */   public boolean anyPlayerCloseEnoughForSpawning(ChunkPos pos) { return this.chunkSource.chunkMap.anyPlayerCloseEnoughForSpawning(pos); }
/*      */ 
/*      */   
/*      */   public boolean canSpreadFireAround(BlockPos pos) {
/* 1840 */     int spreadRadius = ((Integer)getGameRules().get(GameRules.FIRE_SPREAD_RADIUS_AROUND_PLAYER)).intValue();
/* 1841 */     return (spreadRadius == -1 || this.chunkSource.chunkMap.anyPlayerCloseEnoughTo(pos, spreadRadius));
/*      */   }
/*      */   
/*      */   public boolean canSpawnEntitiesInChunk(ChunkPos pos) {
/* 1845 */     return (this.entityManager.canPositionTick(pos) && 
/* 1846 */       getWorldBorder().isWithinBounds(pos));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1851 */   public FeatureFlagSet enabledFeatures() { return this.server.getWorldData().enabledFeatures(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1856 */   public PotionBrewing potionBrewing() { return this.server.potionBrewing(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1861 */   public FuelValues fuelValues() { return this.server.fuelValues(); }
/*      */ 
/*      */ 
/*      */   
/* 1865 */   public RandomSource getRandomSequence(Identifier key) { return this.randomSequences.get(key, getSeed()); }
/*      */ 
/*      */ 
/*      */   
/* 1869 */   public RandomSequences getRandomSequences() { return this.randomSequences; }
/*      */ 
/*      */ 
/*      */   
/* 1873 */   public GameRules getGameRules() { return this.serverLevelData.getGameRules(); }
/*      */ 
/*      */ 
/*      */   
/*      */   public CrashReportCategory fillReportDetails(CrashReport report) {
/* 1878 */     CrashReportCategory category = super.fillReportDetails(report);
/* 1879 */     category.setDetail("Loaded entity count", () -> String.valueOf(this.entityManager.count()));
/* 1880 */     return category;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1885 */   public int getSeaLevel() { return this.chunkSource.getGenerator().getSeaLevel(); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onBlockEntityAdded(BlockEntity blockEntity) {
/* 1890 */     super.onBlockEntityAdded(blockEntity);
/* 1891 */     this.debugSynchronizers.registerBlockEntity(blockEntity);
/*      */   }
/*      */ 
/*      */   
/* 1895 */   public LevelDebugSynchronizers debugSynchronizers() { return this.debugSynchronizers; }
/*      */ 
/*      */   
/*      */   public boolean isAllowedToEnterPortal(Level toLevel) {
/* 1899 */     if (toLevel.dimension() == Level.NETHER) {
/* 1900 */       return ((Boolean)getGameRules().get(GameRules.ALLOW_ENTERING_NETHER_USING_PORTALS)).booleanValue();
/*      */     }
/* 1902 */     return true;
/*      */   }
/*      */ 
/*      */   
/* 1906 */   public boolean isPvpAllowed() { return ((Boolean)getGameRules().get(GameRules.PVP)).booleanValue(); }
/*      */ 
/*      */ 
/*      */   
/* 1910 */   public boolean isCommandBlockEnabled() { return ((Boolean)getGameRules().get(GameRules.COMMAND_BLOCKS_WORK)).booleanValue(); }
/*      */ 
/*      */ 
/*      */   
/* 1914 */   public boolean isSpawnerBlockEnabled() { return ((Boolean)getGameRules().get(GameRules.SPAWNER_BLOCKS_WORK)).booleanValue(); }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ServerLevel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */