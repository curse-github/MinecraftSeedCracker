/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.SpawnPlacements;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.NetherFortressStructure;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NaturalSpawner
/*     */ {
/*  59 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MIN_SPAWN_DISTANCE = 24;
/*     */   public static final int SPAWN_DISTANCE_CHUNK = 8;
/*     */   public static final int SPAWN_DISTANCE_BLOCK = 128;
/*  64 */   public static final int INSCRIBED_SQUARE_SPAWN_DISTANCE_CHUNK = Mth.floor(8.0F / Mth.SQRT_OF_TWO);
/*  65 */   private static final int MAGIC_NUMBER = (int)Math.pow(17.0D, 2.0D);
/*  66 */   private static final MobCategory[] SPAWNING_CATEGORIES = (MobCategory[])Stream.of(MobCategory.values()).filter(c -> (c != MobCategory.MISC)).toArray(x$0 -> new MobCategory[x$0]);
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ChunkGetter {
/*     */     void query(long param1Long, Consumer<LevelChunk> param1Consumer);
/*     */   }
/*     */   
/*     */   public static class SpawnState {
/*     */     private final int spawnableChunkCount;
/*     */     private final Object2IntOpenHashMap<MobCategory> mobCategoryCounts;
/*     */     private final PotentialCalculator spawnPotential;
/*     */     private final Object2IntMap<MobCategory> unmodifiableMobCategoryCounts;
/*     */     
/*     */     private SpawnState(int spawnableChunkCount, Object2IntOpenHashMap<MobCategory> mobCategoryCounts, PotentialCalculator spawnPotential, LocalMobCapCalculator localMobCapCalculator) {
/*  80 */       this.spawnableChunkCount = spawnableChunkCount;
/*  81 */       this.mobCategoryCounts = mobCategoryCounts;
/*  82 */       this.spawnPotential = spawnPotential;
/*  83 */       this.localMobCapCalculator = localMobCapCalculator;
/*  84 */       this.unmodifiableMobCategoryCounts = Object2IntMaps.unmodifiable(mobCategoryCounts);
/*     */     }
/*     */     private final LocalMobCapCalculator localMobCapCalculator; private BlockPos lastCheckedPos; private EntityType<?> lastCheckedType; private double lastCharge;
/*     */     private boolean canSpawn(EntityType<?> type, BlockPos testPos, ChunkAccess chunk) {
/*  88 */       this.lastCheckedPos = testPos;
/*  89 */       this.lastCheckedType = type;
/*     */       
/*  91 */       MobSpawnSettings.MobSpawnCost mobSpawnCost = NaturalSpawner.getRoughBiome(testPos, chunk).getMobSettings().getMobSpawnCost(type);
/*  92 */       if (mobSpawnCost == null) {
/*  93 */         this.lastCharge = 0.0D;
/*  94 */         return true;
/*     */       } 
/*  96 */       double charge = mobSpawnCost.charge();
/*  97 */       this.lastCharge = charge;
/*  98 */       double energyChange = this.spawnPotential.getPotentialEnergyChange(testPos, charge);
/*  99 */       return (energyChange <= mobSpawnCost.energyBudget());
/*     */     }
/*     */     private void afterSpawn(Mob mob, ChunkAccess chunk) {
/*     */       double charge;
/* 103 */       EntityType<?> type = mob.getType();
/*     */       
/* 105 */       BlockPos pos = mob.blockPosition();
/* 106 */       if (pos.equals(this.lastCheckedPos) && type == this.lastCheckedType) {
/* 107 */         charge = this.lastCharge;
/*     */       } else {
/*     */         
/* 110 */         MobSpawnSettings.MobSpawnCost mobSpawnCost = NaturalSpawner.getRoughBiome(pos, chunk).getMobSettings().getMobSpawnCost(type);
/* 111 */         if (mobSpawnCost != null) {
/* 112 */           charge = mobSpawnCost.charge();
/*     */         } else {
/* 114 */           charge = 0.0D;
/*     */         } 
/*     */       } 
/* 117 */       this.spawnPotential.addCharge(pos, charge);
/* 118 */       MobCategory category = type.getCategory();
/* 119 */       this.mobCategoryCounts.addTo(category, 1);
/* 120 */       this.localMobCapCalculator.addMob(new ChunkPos(pos), category);
/*     */     }
/*     */ 
/*     */     
/* 124 */     public int getSpawnableChunkCount() { return this.spawnableChunkCount; }
/*     */ 
/*     */ 
/*     */     
/* 128 */     public Object2IntMap<MobCategory> getMobCategoryCounts() { return this.unmodifiableMobCategoryCounts; }
/*     */ 
/*     */     
/*     */     private boolean canSpawnForCategoryGlobal(MobCategory mobCategory) {
/* 132 */       int maxMobCount = mobCategory.getMaxInstancesPerChunk() * this.spawnableChunkCount / NaturalSpawner.MAGIC_NUMBER;
/* 133 */       return (this.mobCategoryCounts.getInt(mobCategory) < maxMobCount);
/*     */     }
/*     */ 
/*     */     
/* 137 */     private boolean canSpawnForCategoryLocal(MobCategory mobCategory, ChunkPos chunkPos) { return (this.localMobCapCalculator.canSpawn(mobCategory, chunkPos) || SharedConstants.DEBUG_IGNORE_LOCAL_MOB_CAP); }
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
/*     */   public static SpawnState createState(int spawnableChunkCount, Iterable<Entity> entities, ChunkGetter chunkGetter, LocalMobCapCalculator localMobCapCalculator) {
/* 160 */     PotentialCalculator spawnPotential = new PotentialCalculator();
/* 161 */     Object2IntOpenHashMap<MobCategory> mobCounts = new Object2IntOpenHashMap<MobCategory>();
/*     */     
/* 163 */     for (Iterator iterator = entities.iterator(); iterator.hasNext(); ) { Entity entity = (Entity)iterator.next();
/* 164 */       if (entity instanceof Mob) { Mob mob = (Mob)entity; if (mob.isPersistenceRequired() || mob.requiresCustomPersistence())
/*     */           continue;  }
/*     */       
/* 167 */       MobCategory category = entity.getType().getCategory();
/* 168 */       if (category == MobCategory.MISC) {
/*     */         continue;
/*     */       }
/*     */       
/* 172 */       BlockPos pos = entity.blockPosition();
/*     */       
/* 174 */       chunkGetter.query(ChunkPos.asLong(pos), chunk -> {
/* 175 */             MobSpawnSettings.MobSpawnCost mobSpawnCost = getRoughBiome(pos, chunk).getMobSettings().getMobSpawnCost(entity.getType());
/* 176 */             if (mobSpawnCost != null) {
/* 177 */               spawnPotential.addCharge(entity.blockPosition(), mobSpawnCost.charge());
/*     */             }
/* 179 */             if (entity instanceof Mob) {
/* 180 */               localMobCapCalculator.addMob(chunk.getPos(), category);
/*     */             }
/* 182 */             mobCounts.addTo(category, 1);
/*     */           }); }
/*     */ 
/*     */     
/* 186 */     return new SpawnState(spawnableChunkCount, mobCounts, spawnPotential, localMobCapCalculator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 191 */   private static Biome getRoughBiome(BlockPos pos, ChunkAccess chunk) { return (Biome)chunk.getNoiseBiome(QuartPos.fromBlock(pos.getX()), QuartPos.fromBlock(pos.getY()), QuartPos.fromBlock(pos.getZ())).value(); }
/*     */ 
/*     */   
/*     */   public static List<MobCategory> getFilteredSpawningCategories(SpawnState state, boolean spawnFriendlies, boolean spawnEnemies, boolean spawnPersistent) {
/* 195 */     List<MobCategory> spawningCategories = new ArrayList<MobCategory>(SPAWNING_CATEGORIES.length);
/* 196 */     for (MobCategory mobCategory : SPAWNING_CATEGORIES) {
/* 197 */       if ((spawnFriendlies || !mobCategory.isFriendly()) && (spawnEnemies || mobCategory
/* 198 */         .isFriendly()) && (spawnPersistent || 
/* 199 */         !mobCategory.isPersistent()) && state
/* 200 */         .canSpawnForCategoryGlobal(mobCategory))
/*     */       {
/* 202 */         spawningCategories.add(mobCategory);
/*     */       }
/*     */     } 
/* 205 */     return spawningCategories;
/*     */   }
/*     */   
/*     */   public static void spawnForChunk(ServerLevel level, LevelChunk chunk, SpawnState state, List<MobCategory> spawningCategories) {
/* 209 */     ProfilerFiller profiler = Profiler.get();
/* 210 */     profiler.push("spawner");
/* 211 */     for (MobCategory mobCategory : spawningCategories) {
/* 212 */       if (state.canSpawnForCategoryLocal(mobCategory, chunk.getPos())) {
/* 213 */         Objects.requireNonNull(state); Objects.requireNonNull(state); spawnCategoryForChunk(mobCategory, level, chunk, state::canSpawn, state::afterSpawn);
/*     */       } 
/*     */     } 
/* 216 */     profiler.pop();
/*     */   }
/*     */   
/*     */   public static void spawnCategoryForChunk(MobCategory mobCategory, ServerLevel level, LevelChunk chunk, SpawnPredicate extraTest, AfterSpawnCallback spawnCallback) {
/* 220 */     BlockPos start = getRandomPosWithin(level, chunk);
/*     */     
/* 222 */     if (start.getY() < level.getMinY() + 1) {
/*     */       return;
/*     */     }
/* 225 */     spawnCategoryForPosition(mobCategory, level, chunk, start, extraTest, spawnCallback);
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public static void spawnCategoryForPosition(MobCategory mobCategory, ServerLevel level, BlockPos start) {
/* 230 */     spawnCategoryForPosition(mobCategory, level, level.getChunk(start), start, (type, chunk, pos) -> true, (mob, chunk) -> {
/*     */         
/*     */         });
/*     */   }
/* 234 */   public static void spawnCategoryForPosition(MobCategory mobCategory, ServerLevel level, ChunkAccess chunk, BlockPos start, SpawnPredicate extraTest, AfterSpawnCallback spawnCallback) { StructureManager structureManager = level.structureManager();
/* 235 */     ChunkGenerator generator = level.getChunkSource().getGenerator();
/* 236 */     int yStart = start.getY();
/*     */     
/* 238 */     BlockState state = chunk.getBlockState(start);
/* 239 */     if (state.isRedstoneConductor(chunk, start)) {
/*     */       return;
/*     */     }
/*     */     
/* 243 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 244 */     int clusterSize = 0;
/*     */     
/* 246 */     for (int groupCount = 0; groupCount < 3; groupCount++) {
/* 247 */       int x = start.getX();
/* 248 */       int z = start.getZ();
/* 249 */       int ss = 6;
/*     */       
/* 251 */       MobSpawnSettings.SpawnerData currentSpawnData = null;
/* 252 */       SpawnGroupData groupData = null;
/*     */       
/* 254 */       int max = Mth.ceil(level.random.nextFloat() * 4.0F);
/* 255 */       int groupSize = 0;
/*     */ 
/*     */       
/* 258 */       for (int ll = 0; ll < max; ll++) {
/* 259 */         x += level.random.nextInt(6) - level.random.nextInt(6);
/* 260 */         z += level.random.nextInt(6) - level.random.nextInt(6);
/*     */         
/* 262 */         pos.set(x, yStart, z);
/*     */         
/* 264 */         double xx = x + 0.5D;
/* 265 */         double zz = z + 0.5D;
/*     */         
/* 267 */         Player nearestPlayer = level.getNearestPlayer(xx, yStart, zz, -1.0D, false);
/* 268 */         if (nearestPlayer != null) {
/*     */ 
/*     */ 
/*     */           
/* 272 */           double nearestPlayerDistanceSqr = nearestPlayer.distanceToSqr(xx, yStart, zz);
/* 273 */           if (isRightDistanceToPlayerAndSpawnPoint(level, chunk, pos, nearestPlayerDistanceSqr)) {
/*     */ 
/*     */ 
/*     */             
/* 277 */             if (currentSpawnData == null) {
/* 278 */               Optional<MobSpawnSettings.SpawnerData> nextSpawnData = getRandomSpawnMobAt(level, structureManager, generator, mobCategory, level.random, pos);
/* 279 */               if (nextSpawnData.isEmpty()) {
/*     */                 break;
/*     */               }
/* 282 */               currentSpawnData = (MobSpawnSettings.SpawnerData)nextSpawnData.get();
/*     */ 
/*     */               
/* 285 */               max = currentSpawnData.minCount() + level.random.nextInt(1 + currentSpawnData.maxCount() - currentSpawnData.minCount());
/*     */             } 
/*     */             
/* 288 */             if (isValidSpawnPostitionForType(level, mobCategory, structureManager, generator, currentSpawnData, pos, nearestPlayerDistanceSqr))
/*     */             {
/*     */ 
/*     */               
/* 292 */               if (extraTest.test(currentSpawnData.type(), pos, chunk)) {
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 297 */                 Mob mob = getMobForSpawn(level, currentSpawnData.type());
/* 298 */                 if (mob == null) {
/*     */                   return;
/*     */                 }
/*     */                 
/* 302 */                 mob.snapTo(xx, yStart, zz, level.random.nextFloat() * 360.0F, 0.0F);
/*     */                 
/* 304 */                 if (isValidPositionForMob(level, mob, nearestPlayerDistanceSqr)) {
/*     */ 
/*     */ 
/*     */                   
/* 308 */                   groupData = mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.NATURAL, groupData);
/*     */                   
/* 310 */                   clusterSize++;
/* 311 */                   groupSize++;
/* 312 */                   level.addFreshEntityWithPassengers(mob);
/* 313 */                   spawnCallback.run(mob, chunk);
/*     */                   
/* 315 */                   if (clusterSize >= mob.getMaxSpawnClusterSize()) {
/*     */                     return;
/*     */                   }
/* 318 */                   if (mob.isMaxGroupSizeReached(groupSize))
/*     */                     break; 
/*     */                 } 
/*     */               }  } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }  } private static boolean isRightDistanceToPlayerAndSpawnPoint(ServerLevel level, ChunkAccess chunk, BlockPos.MutableBlockPos pos, double nearestPlayerDistanceSqr) {
/* 326 */     if (nearestPlayerDistanceSqr <= 576.0D) {
/* 327 */       return false;
/*     */     }
/* 329 */     LevelData.RespawnData respawnData = level.getRespawnData();
/* 330 */     if (respawnData.dimension() == level.dimension() && respawnData.pos().closerToCenterThan(new Vec3(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D), 24.0D)) {
/* 331 */       return false;
/*     */     }
/*     */     
/* 334 */     ChunkPos chunkPos = new ChunkPos(pos);
/* 335 */     return (Objects.equals(chunkPos, chunk.getPos()) || level.canSpawnEntitiesInChunk(chunkPos));
/*     */   }
/*     */   
/*     */   private static boolean isValidSpawnPostitionForType(ServerLevel level, MobCategory mobCategory, StructureManager structureManager, ChunkGenerator generator, MobSpawnSettings.SpawnerData currentSpawnData, BlockPos.MutableBlockPos pos, double nearestPlayerDistanceSqr) {
/* 339 */     EntityType<?> type = currentSpawnData.type();
/*     */     
/* 341 */     if (type.getCategory() == MobCategory.MISC) {
/* 342 */       return false;
/*     */     }
/*     */     
/* 345 */     if (!type.canSpawnFarFromPlayer() && nearestPlayerDistanceSqr > (type.getCategory().getDespawnDistance() * type.getCategory().getDespawnDistance())) {
/* 346 */       return false;
/*     */     }
/*     */     
/* 349 */     if (!type.canSummon() || !canSpawnMobAt(level, structureManager, generator, mobCategory, currentSpawnData, pos)) {
/* 350 */       return false;
/*     */     }
/*     */     
/* 353 */     if (!SpawnPlacements.isSpawnPositionOk(type, level, pos)) {
/* 354 */       return false;
/*     */     }
/* 356 */     if (!SpawnPlacements.checkSpawnRules(type, level, EntitySpawnReason.NATURAL, pos, level.random)) {
/* 357 */       return false;
/*     */     }
/* 359 */     if (!level.noCollision(type.getSpawnAABB(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D))) {
/* 360 */       return false;
/*     */     }
/* 362 */     return true;
/*     */   }
/*     */   
/*     */   private static Mob getMobForSpawn(ServerLevel level, EntityType<?> type) {
/*     */     try {
/* 367 */       Entity entity = type.create(level, EntitySpawnReason.NATURAL); if (entity instanceof Mob) return (Mob)entity;
/*     */ 
/*     */       
/* 370 */       LOGGER.warn("Can't spawn entity of type: {}", BuiltInRegistries.ENTITY_TYPE.getKey(type));
/* 371 */     } catch (Exception e) {
/* 372 */       LOGGER.warn("Failed to create mob", e);
/*     */     } 
/* 374 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean isValidPositionForMob(ServerLevel level, Mob mob, double nearestPlayerDistanceSqr) {
/* 378 */     if (nearestPlayerDistanceSqr > (mob.getType().getCategory().getDespawnDistance() * mob.getType().getCategory().getDespawnDistance()) && mob.removeWhenFarAway(nearestPlayerDistanceSqr)) {
/* 379 */       return false;
/*     */     }
/* 381 */     return (mob.checkSpawnRules(level, EntitySpawnReason.NATURAL) && mob.checkSpawnObstruction(level));
/*     */   }
/*     */   
/*     */   private static Optional<MobSpawnSettings.SpawnerData> getRandomSpawnMobAt(ServerLevel level, StructureManager structureManager, ChunkGenerator generator, MobCategory mobCategory, RandomSource random, BlockPos pos) {
/* 385 */     Holder<Biome> biome = level.getBiome(pos);
/*     */     
/* 387 */     if (mobCategory == MobCategory.WATER_AMBIENT && biome.is(BiomeTags.REDUCED_WATER_AMBIENT_SPAWNS) && random.nextFloat() < 0.98F) {
/* 388 */       return Optional.empty();
/*     */     }
/* 390 */     return mobsAt(level, structureManager, generator, mobCategory, pos, biome).getRandom(random);
/*     */   }
/*     */ 
/*     */   
/* 394 */   private static boolean canSpawnMobAt(ServerLevel level, StructureManager structureManager, ChunkGenerator generator, MobCategory mobCategory, MobSpawnSettings.SpawnerData spawnerData, BlockPos pos) { return mobsAt(level, structureManager, generator, mobCategory, pos, null).contains(spawnerData); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static WeightedList<MobSpawnSettings.SpawnerData> mobsAt(ServerLevel level, StructureManager structureManager, ChunkGenerator generator, MobCategory mobCategory, BlockPos pos, Holder<Biome> biome) {
/* 399 */     if (isInNetherFortressBounds(pos, level, mobCategory, structureManager)) {
/* 400 */       return NetherFortressStructure.FORTRESS_ENEMIES;
/*     */     }
/* 402 */     return generator.getMobsAt((biome != null) ? biome : level.getBiome(pos), structureManager, mobCategory, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isInNetherFortressBounds(BlockPos pos, ServerLevel level, MobCategory category, StructureManager structureManager) {
/* 407 */     if (category != MobCategory.MONSTER || !level.getBlockState(pos.below()).is(Blocks.NETHER_BRICKS)) {
/* 408 */       return false;
/*     */     }
/* 410 */     Structure fortress = (Structure)structureManager.registryAccess().lookupOrThrow(Registries.STRUCTURE).getValue(BuiltinStructures.FORTRESS);
/* 411 */     if (fortress == null) {
/* 412 */       return false;
/*     */     }
/* 414 */     return structureManager.getStructureAt(pos, fortress).isValid();
/*     */   }
/*     */   
/*     */   private static BlockPos getRandomPosWithin(Level level, LevelChunk chunk) {
/* 418 */     ChunkPos pos = chunk.getPos();
/* 419 */     int x = pos.getMinBlockX() + level.random.nextInt(16);
/* 420 */     int z = pos.getMinBlockZ() + level.random.nextInt(16);
/*     */     
/* 422 */     int topEmptyY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x, z) + 1;
/* 423 */     int y = Mth.randomBetweenInclusive(level.random, level.getMinY(), topEmptyY);
/*     */     
/* 425 */     return new BlockPos(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isValidEmptySpawnBlock(BlockGetter level, BlockPos pos, BlockState blockState, FluidState fluidState, EntityType<?> type) {
/* 430 */     if (blockState.isCollisionShapeFullBlock(level, pos)) {
/* 431 */       return false;
/*     */     }
/*     */     
/* 434 */     if (blockState.isSignalSource()) {
/* 435 */       return false;
/*     */     }
/*     */     
/* 438 */     if (!fluidState.isEmpty()) {
/* 439 */       return false;
/*     */     }
/*     */     
/* 442 */     if (blockState.is(BlockTags.PREVENT_MOB_SPAWNING_INSIDE)) {
/* 443 */       return false;
/*     */     }
/*     */     
/* 446 */     if (type.isBlockDangerous(blockState)) {
/* 447 */       return false;
/*     */     }
/* 449 */     return true;
/*     */   }
/*     */   
/*     */   public static void spawnMobsForChunkGeneration(ServerLevelAccessor level, Holder<Biome> biome, ChunkPos chunkPos, RandomSource random) {
/* 453 */     MobSpawnSettings mobSettings = ((Biome)biome.value()).getMobSettings();
/* 454 */     WeightedList<MobSpawnSettings.SpawnerData> mobs = mobSettings.getMobs(MobCategory.CREATURE);
/* 455 */     if (mobs.isEmpty() || !((Boolean)level.getLevel().getGameRules().get(GameRules.SPAWN_MOBS)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 459 */     int xo = chunkPos.getMinBlockX();
/* 460 */     int zo = chunkPos.getMinBlockZ();
/*     */ 
/*     */     
/* 463 */     while (random.nextFloat() < mobSettings.getCreatureProbability()) {
/* 464 */       Optional<MobSpawnSettings.SpawnerData> nextSpawnerData = mobs.getRandom(random);
/* 465 */       if (nextSpawnerData.isEmpty()) {
/*     */         continue;
/*     */       }
/* 468 */       MobSpawnSettings.SpawnerData spawnerData = (MobSpawnSettings.SpawnerData)nextSpawnerData.get();
/*     */       
/* 470 */       int count = spawnerData.minCount() + random.nextInt(1 + spawnerData.maxCount() - spawnerData.minCount());
/* 471 */       SpawnGroupData groupSpawnData = null;
/*     */       
/* 473 */       int x = xo + random.nextInt(16);
/* 474 */       int z = zo + random.nextInt(16);
/* 475 */       int startX = x;
/* 476 */       int startZ = z;
/*     */       
/* 478 */       for (int i = 0; i < count; i++) {
/* 479 */         boolean success = false;
/* 480 */         for (int attempts = 0; !success && attempts < 4; attempts++) {
/*     */ 
/*     */           
/* 483 */           BlockPos pos = getTopNonCollidingPos(level, spawnerData.type(), x, z);
/* 484 */           if (spawnerData.type().canSummon() && SpawnPlacements.isSpawnPositionOk(spawnerData.type(), level, pos)) {
/* 485 */             Entity entity; float width = spawnerData.type().getWidth();
/* 486 */             double fx = Mth.clamp(x, xo + width, xo + 16.0D - width);
/* 487 */             double fz = Mth.clamp(z, zo + width, zo + 16.0D - width);
/*     */             
/* 489 */             if (!level.noCollision(spawnerData.type().getSpawnAABB(fx, pos.getY(), fz))) {
/*     */               continue;
/*     */             }
/*     */             
/* 493 */             if (!SpawnPlacements.checkSpawnRules(spawnerData.type(), level, EntitySpawnReason.CHUNK_GENERATION, BlockPos.containing(fx, pos.getY(), fz), level.getRandom())) {
/*     */               continue;
/*     */             }
/*     */ 
/*     */             
/*     */             try {
/* 499 */               entity = spawnerData.type().create(level.getLevel(), EntitySpawnReason.NATURAL);
/* 500 */             } catch (Exception e) {
/* 501 */               LOGGER.warn("Failed to create mob", e);
/*     */               
/*     */               continue;
/*     */             } 
/* 505 */             if (entity == null) {
/*     */               continue;
/*     */             }
/*     */             
/* 509 */             entity.snapTo(fx, pos.getY(), fz, random.nextFloat() * 360.0F, 0.0F);
/*     */             
/* 511 */             if (entity instanceof Mob) { Mob mob = (Mob)entity;
/* 512 */               if (mob.checkSpawnRules(level, EntitySpawnReason.CHUNK_GENERATION) && mob.checkSpawnObstruction(level)) {
/* 513 */                 groupSpawnData = mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.CHUNK_GENERATION, groupSpawnData);
/* 514 */                 level.addFreshEntityWithPassengers(mob);
/* 515 */                 success = true;
/*     */               }  }
/*     */           
/*     */           } 
/*     */           
/* 520 */           x += random.nextInt(5) - random.nextInt(5);
/* 521 */           z += random.nextInt(5) - random.nextInt(5);
/* 522 */           while (x < xo || x >= xo + 16 || z < zo || z >= zo + 16) {
/* 523 */             x = startX + random.nextInt(5) - random.nextInt(5);
/* 524 */             z = startZ + random.nextInt(5) - random.nextInt(5);
/*     */           } 
/*     */           continue;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private static BlockPos getTopNonCollidingPos(LevelReader level, EntityType<?> type, int x, int z) {
/* 532 */     int levelHeight = level.getHeight(SpawnPlacements.getHeightmapType(type), x, z);
/* 533 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, levelHeight, z);
/*     */     
/* 535 */     if (level.dimensionType().hasCeiling()) {
/*     */       
/*     */       do {
/* 538 */         pos.move(Direction.DOWN);
/* 539 */       } while (!level.getBlockState(pos).isAir());
/*     */       do {
/* 541 */         pos.move(Direction.DOWN);
/* 542 */       } while (level.getBlockState(pos).isAir() && pos.getY() > level.getMinY());
/*     */     } 
/*     */     
/* 545 */     return SpawnPlacements.getPlacementType(type).adjustSpawnPosition(level, pos.immutable());
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface SpawnPredicate {
/*     */     boolean test(EntityType<?> param1EntityType, BlockPos param1BlockPos, ChunkAccess param1ChunkAccess);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface AfterSpawnCallback {
/*     */     void run(Mob param1Mob, ChunkAccess param1ChunkAccess);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\NaturalSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */