/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.ints.IntArraySet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.NoiseColumn;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.FeatureSorter;
/*     */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.RandomSupport;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ import net.minecraft.world.level.levelgen.feature.FeatureCountTracker;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ 
/*     */ public abstract class ChunkGenerator {
/*  82 */   public static final Codec<ChunkGenerator> CODEC = BuiltInRegistries.CHUNK_GENERATOR.byNameCodec().dispatchStable(ChunkGenerator::codec, Function.identity());
/*     */ 
/*     */   
/*     */   protected final BiomeSource biomeSource;
/*     */ 
/*     */   
/*     */   private final Supplier<List<FeatureSorter.StepFeatureData>> featuresPerStep;
/*     */ 
/*     */   
/*     */   private final Function<Holder<Biome>, BiomeGenerationSettings> generationSettingsGetter;
/*     */ 
/*     */ 
/*     */   
/*  95 */   public ChunkGenerator(BiomeSource biomeSource) { this(biomeSource, biome -> ((Biome)biome.value()).getGenerationSettings()); }
/*     */ 
/*     */   
/*     */   public ChunkGenerator(BiomeSource biomeSource, Function<Holder<Biome>, BiomeGenerationSettings> generationSettingsGetter) {
/*  99 */     this.biomeSource = biomeSource;
/* 100 */     this.generationSettingsGetter = generationSettingsGetter;
/*     */     
/* 102 */     this.featuresPerStep = Suppliers.memoize(() -> FeatureSorter.buildFeaturesPerStep(List.copyOf(biomeSource.possibleBiomes()), (), true));
/*     */   }
/*     */ 
/*     */   
/* 106 */   public void validate() { this.featuresPerStep.get(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   public ChunkGeneratorStructureState createState(HolderLookup<StructureSet> structureSets, RandomState randomState, long legacyLevelSeed) { return ChunkGeneratorStructureState.createForNormal(randomState, legacyLevelSeed, this.biomeSource, structureSets); }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public Optional<ResourceKey<MapCodec<? extends ChunkGenerator>>> getTypeNameForDataFixer() { return BuiltInRegistries.CHUNK_GENERATOR.getResourceKey(codec()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkAccess> createBiomes(RandomState randomState, Blender blender, StructureManager structureManager, ChunkAccess protoChunk) {
/* 123 */     return CompletableFuture.supplyAsync(() -> {
/* 124 */           protoChunk.fillBiomesFromNoise(this.biomeSource, randomState.sampler());
/* 125 */           return protoChunk;
/* 126 */         }Util.backgroundExecutor().forName("init_biomes"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Pair<BlockPos, Holder<Structure>> findNearestMapStructure(ServerLevel level, HolderSet<Structure> wantedStructures, BlockPos pos, int maxSearchRadius, boolean createReference) {
/* 132 */     if (SharedConstants.DEBUG_DISABLE_FEATURES) {
/* 133 */       return null;
/*     */     }
/*     */     
/* 136 */     ChunkGeneratorStructureState generatorState = level.getChunkSource().getGeneratorState();
/* 137 */     Object2ObjectArrayMap object2ObjectArrayMap = new Object2ObjectArrayMap();
/* 138 */     for (Holder<Structure> structure : wantedStructures) {
/* 139 */       for (StructurePlacement placement : generatorState.getPlacementsForStructure(structure)) {
/* 140 */         ((Set)object2ObjectArrayMap.computeIfAbsent(placement, p -> new ObjectArraySet())).add(structure);
/*     */       }
/*     */     } 
/*     */     
/* 144 */     if (object2ObjectArrayMap.isEmpty()) {
/* 145 */       return null;
/*     */     }
/*     */     
/* 148 */     Pair<BlockPos, Holder<Structure>> nearest = null;
/* 149 */     double distanceSqr = Double.MAX_VALUE;
/* 150 */     StructureManager structureManager = level.structureManager();
/* 151 */     List<Map.Entry<StructurePlacement, Set<Holder<Structure>>>> randomSpreadEntries = new ArrayList<Map.Entry<StructurePlacement, Set<Holder<Structure>>>>(object2ObjectArrayMap.size());
/* 152 */     for (Map.Entry<StructurePlacement, Set<Holder<Structure>>> entry : object2ObjectArrayMap.entrySet()) {
/* 153 */       StructurePlacement placement = (StructurePlacement)entry.getKey();
/* 154 */       if (placement instanceof ConcentricRingsStructurePlacement) { ConcentricRingsStructurePlacement rings = (ConcentricRingsStructurePlacement)placement;
/* 155 */         Pair<BlockPos, Holder<Structure>> generating = getNearestGeneratedStructure((Set)entry.getValue(), level, structureManager, pos, createReference, rings);
/* 156 */         if (generating != null) {
/* 157 */           BlockPos structurePos = (BlockPos)generating.getFirst();
/* 158 */           double newDistanceSqr = pos.distSqr(structurePos);
/* 159 */           if (newDistanceSqr < distanceSqr) {
/* 160 */             distanceSqr = newDistanceSqr;
/* 161 */             nearest = generating;
/*     */           } 
/*     */         }  continue; }
/* 164 */        if (placement instanceof RandomSpreadStructurePlacement) {
/* 165 */         randomSpreadEntries.add(entry);
/*     */       }
/*     */     } 
/*     */     
/* 169 */     if (!randomSpreadEntries.isEmpty()) {
/* 170 */       int chunkOriginX = SectionPos.blockToSectionCoord(pos.getX());
/* 171 */       int chunkOriginZ = SectionPos.blockToSectionCoord(pos.getZ());
/*     */ 
/*     */       
/* 174 */       for (int radius = 0; radius <= maxSearchRadius; radius++) {
/* 175 */         boolean foundSomething = false;
/* 176 */         for (Map.Entry<StructurePlacement, Set<Holder<Structure>>> entry : randomSpreadEntries) {
/* 177 */           RandomSpreadStructurePlacement randomPlacement = (RandomSpreadStructurePlacement)entry.getKey();
/* 178 */           Pair<BlockPos, Holder<Structure>> structurePos = getNearestGeneratedStructure((Set)entry.getValue(), level, structureManager, chunkOriginX, chunkOriginZ, radius, createReference, generatorState.getLevelSeed(), randomPlacement);
/* 179 */           if (structurePos != null) {
/* 180 */             foundSomething = true;
/* 181 */             double newDistanceSqr = pos.distSqr((Vec3i)structurePos.getFirst());
/* 182 */             if (newDistanceSqr < distanceSqr) {
/* 183 */               distanceSqr = newDistanceSqr;
/* 184 */               nearest = structurePos;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 189 */         if (foundSomething) {
/* 190 */           return nearest;
/*     */         }
/*     */       } 
/*     */     } 
/* 194 */     return nearest;
/*     */   }
/*     */   
/*     */   private Pair<BlockPos, Holder<Structure>> getNearestGeneratedStructure(Set<Holder<Structure>> structures, ServerLevel level, StructureManager structureManager, BlockPos pos, boolean createReference, ConcentricRingsStructurePlacement rings) {
/* 198 */     List<ChunkPos> positions = level.getChunkSource().getGeneratorState().getRingPositionsFor(rings);
/* 199 */     if (positions == null) {
/* 200 */       throw new IllegalStateException("Somehow tried to find structures for a placement that doesn't exist");
/*     */     }
/* 202 */     Pair<BlockPos, Holder<Structure>> closestPos = null;
/* 203 */     double closest = Double.MAX_VALUE;
/* 204 */     BlockPos.MutableBlockPos structurePos = new BlockPos.MutableBlockPos();
/* 205 */     for (ChunkPos chunkPos : positions) {
/* 206 */       structurePos.set(SectionPos.sectionToBlockCoord(chunkPos.x, 8), 32, SectionPos.sectionToBlockCoord(chunkPos.z, 8));
/* 207 */       double distSqr = structurePos.distSqr(pos);
/* 208 */       boolean isClosest = (closestPos == null || distSqr < closest);
/* 209 */       if (isClosest) {
/* 210 */         Pair<BlockPos, Holder<Structure>> generating = getStructureGeneratingAt(structures, level, structureManager, createReference, rings, chunkPos);
/* 211 */         if (generating != null) {
/* 212 */           closestPos = generating;
/* 213 */           closest = distSqr;
/*     */         } 
/*     */       } 
/*     */     } 
/* 217 */     return closestPos;
/*     */   }
/*     */   
/*     */   private static Pair<BlockPos, Holder<Structure>> getNearestGeneratedStructure(Set<Holder<Structure>> structures, LevelReader level, StructureManager structureManager, int chunkOriginX, int chunkOriginZ, int radius, boolean createReference, long seed, RandomSpreadStructurePlacement config) {
/* 221 */     int spacing = config.spacing();
/*     */     
/* 223 */     for (int x = -radius; x <= radius; x++) {
/* 224 */       boolean xEdge = (x == -radius || x == radius);
/* 225 */       for (int z = -radius; z <= radius; z++) {
/* 226 */         boolean zEdge = (z == -radius || z == radius);
/* 227 */         if (xEdge || zEdge) {
/*     */ 
/*     */ 
/*     */           
/* 231 */           int sectorX = chunkOriginX + spacing * x;
/* 232 */           int sectorZ = chunkOriginZ + spacing * z;
/*     */           
/* 234 */           ChunkPos chunkTarget = config.getPotentialStructureChunk(seed, sectorX, sectorZ);
/*     */           
/* 236 */           Pair<BlockPos, Holder<Structure>> generating = getStructureGeneratingAt(structures, level, structureManager, createReference, config, chunkTarget);
/* 237 */           if (generating != null) {
/* 238 */             return generating;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 243 */     return null;
/*     */   }
/*     */   
/*     */   private static Pair<BlockPos, Holder<Structure>> getStructureGeneratingAt(Set<Holder<Structure>> structures, LevelReader level, StructureManager structureManager, boolean createReference, StructurePlacement config, ChunkPos chunkTarget) {
/* 247 */     for (Holder<Structure> structure : structures) {
/* 248 */       StructureCheckResult fastCheckResult = structureManager.checkStructurePresence(chunkTarget, (Structure)structure.value(), config, createReference);
/*     */       
/* 250 */       if (fastCheckResult == StructureCheckResult.START_NOT_PRESENT)
/*     */         continue; 
/* 252 */       if (!createReference && fastCheckResult == StructureCheckResult.START_PRESENT) {
/* 253 */         return Pair.of(config.getLocatePos(chunkTarget), structure);
/*     */       }
/*     */       
/* 256 */       ChunkAccess chunk = level.getChunk(chunkTarget.x, chunkTarget.z, ChunkStatus.STRUCTURE_STARTS);
/* 257 */       StructureStart start = structureManager.getStartForStructure(SectionPos.bottomOf(chunk), (Structure)structure.value(), chunk);
/* 258 */       if (start != null && start.isValid() && (
/* 259 */         !createReference || tryAddReference(structureManager, start))) {
/* 260 */         return Pair.of(config.getLocatePos(start.getChunkPos()), structure);
/*     */       }
/*     */     } 
/*     */     
/* 264 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean tryAddReference(StructureManager manager, StructureStart start) {
/* 268 */     if (start.canBeReferenced()) {
/* 269 */       manager.addReference(start);
/* 270 */       return true;
/*     */     } 
/* 272 */     return false;
/*     */   }
/*     */   
/*     */   public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
/* 276 */     ChunkPos centerPos = chunk.getPos();
/*     */     
/* 278 */     if (SharedConstants.debugVoidTerrain(centerPos)) {
/*     */       return;
/*     */     }
/*     */     
/* 282 */     SectionPos sectionPos = SectionPos.of(centerPos, level.getMinSectionY());
/* 283 */     BlockPos origin = sectionPos.origin();
/*     */     
/* 285 */     Registry<Structure> structuresRegistry = level.registryAccess().lookupOrThrow(Registries.STRUCTURE);
/* 286 */     Map<Integer, List<Structure>> structuresByStep = (Map)structuresRegistry.stream().collect(Collectors.groupingBy(structure -> Integer.valueOf(structure.step().ordinal())));
/*     */     
/* 288 */     List<FeatureSorter.StepFeatureData> featureList = (List)this.featuresPerStep.get();
/*     */     
/* 290 */     WorldgenRandom random = new WorldgenRandom(new XoroshiroRandomSource(RandomSupport.generateUniqueSeed()));
/* 291 */     long decorationSeed = random.setDecorationSeed(level.getSeed(), origin.getX(), origin.getZ());
/*     */     
/* 293 */     ObjectArraySet objectArraySet = new ObjectArraySet();
/* 294 */     ChunkPos.rangeClosed(sectionPos.chunk(), 1).forEach(chunkPos -> {
/* 295 */           ChunkAccess chunkInRange = level.getChunk(chunkPos.x, chunkPos.z);
/* 296 */           for (LevelChunkSection section : chunkInRange.getSections()) {
/* 297 */             Objects.requireNonNull(possibleBiomes); section.getBiomes().getAll(possibleBiomes::add);
/*     */           } 
/*     */         });
/*     */     
/* 301 */     objectArraySet.retainAll(this.biomeSource.possibleBiomes());
/* 302 */     int featureStepCount = featureList.size();
/*     */     
/*     */     try {
/* 305 */       Registry<PlacedFeature> featureRegistry = level.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE);
/*     */       
/* 307 */       int generationSteps = Math.max(GenerationStep.Decoration.values().length, featureStepCount);
/* 308 */       for (int stepIndex = 0; stepIndex < generationSteps; stepIndex++) {
/* 309 */         int index = 0;
/* 310 */         if (structureManager.shouldGenerateStructures()) {
/* 311 */           List<Structure> structures = (List)structuresByStep.getOrDefault(Integer.valueOf(stepIndex), Collections.emptyList());
/* 312 */           for (Structure structure : structures) {
/* 313 */             random.setFeatureSeed(decorationSeed, index, stepIndex);
/*     */             
/* 315 */             Supplier<String> currentlyGenerating = () -> { Objects.requireNonNull(structure); return (String)structuresRegistry.getResourceKey(structure).map(Object::toString).orElseGet(structure::toString);
/*     */               }; try {
/* 317 */               level.setCurrentlyGenerating(currentlyGenerating);
/*     */               
/* 319 */               structureManager.startsForStructure(sectionPos, structure).forEach(start -> 
/* 320 */                   start.placeInChunk(level, structureManager, this, random, getWritableArea(chunk), centerPos));
/*     */             }
/* 322 */             catch (Exception e) {
/* 323 */               CrashReport report = CrashReport.forThrowable(e, "Feature placement");
/*     */               
/* 325 */               Objects.requireNonNull(currentlyGenerating); report.addCategory("Feature").setDetail("Description", currentlyGenerating::get);
/* 326 */               throw new ReportedException(report);
/*     */             } 
/* 328 */             index++;
/*     */           } 
/*     */         } 
/* 331 */         if (stepIndex < featureStepCount) {
/* 332 */           IntArraySet intArraySet = new IntArraySet();
/* 333 */           for (Holder<Biome> biome : objectArraySet) {
/*     */             
/* 335 */             List<HolderSet<PlacedFeature>> featuresInBiome = ((BiomeGenerationSettings)this.generationSettingsGetter.apply(biome)).features();
/* 336 */             if (stepIndex >= featuresInBiome.size()) {
/*     */               continue;
/*     */             }
/* 339 */             HolderSet<PlacedFeature> featuresInBiomeThisStep = (HolderSet)featuresInBiome.get(stepIndex);
/* 340 */             FeatureSorter.StepFeatureData stepFeatureData = (FeatureSorter.StepFeatureData)featureList.get(stepIndex);
/* 341 */             featuresInBiomeThisStep.stream().map(Holder::value).forEach(feature -> possibleFeaturesThisStep.add(stepFeatureData.indexMapping().applyAsInt(feature)));
/*     */           } 
/*     */           
/* 344 */           int numberOfFeaturesInStep = intArraySet.size();
/* 345 */           int[] indexArray = intArraySet.toIntArray();
/* 346 */           Arrays.sort(indexArray);
/*     */           
/* 348 */           FeatureSorter.StepFeatureData stepFeatureData = (FeatureSorter.StepFeatureData)featureList.get(stepIndex);
/* 349 */           for (int featureIndex = 0; featureIndex < numberOfFeaturesInStep; featureIndex++) {
/* 350 */             int globalIndexOfFeature = indexArray[featureIndex];
/* 351 */             PlacedFeature feature = (PlacedFeature)stepFeatureData.features().get(globalIndexOfFeature);
/*     */             
/* 353 */             Supplier<String> currentlyGenerating = () -> { Objects.requireNonNull(feature); return (String)featureRegistry.getResourceKey(feature).map(Object::toString).orElseGet(feature::toString);
/* 354 */               }; random.setFeatureSeed(decorationSeed, globalIndexOfFeature, stepIndex);
/*     */             try {
/* 356 */               level.setCurrentlyGenerating(currentlyGenerating);
/* 357 */               feature.placeWithBiomeCheck(level, this, random, origin);
/* 358 */             } catch (Exception e) {
/* 359 */               CrashReport report = CrashReport.forThrowable(e, "Feature placement");
/*     */               
/* 361 */               Objects.requireNonNull(currentlyGenerating); report.addCategory("Feature").setDetail("Description", currentlyGenerating::get);
/* 362 */               throw new ReportedException(report);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 367 */       level.setCurrentlyGenerating(null);
/* 368 */       if (SharedConstants.DEBUG_FEATURE_COUNT) {
/* 369 */         FeatureCountTracker.chunkDecorated(level.getLevel());
/*     */       }
/* 371 */     } catch (Exception e) {
/* 372 */       CrashReport report = CrashReport.forThrowable(e, "Biome decoration");
/* 373 */       report.addCategory("Generation")
/* 374 */         .setDetail("CenterX", Integer.valueOf(centerPos.x))
/* 375 */         .setDetail("CenterZ", Integer.valueOf(centerPos.z))
/* 376 */         .setDetail("Decoration Seed", Long.valueOf(decorationSeed));
/* 377 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static BoundingBox getWritableArea(ChunkAccess chunk) {
/* 382 */     ChunkPos chunkPos = chunk.getPos();
/* 383 */     int targetBlockX = chunkPos.getMinBlockX();
/* 384 */     int targetBlockZ = chunkPos.getMinBlockZ();
/*     */     
/* 386 */     LevelHeightAccessor heightAccessor = chunk.getHeightAccessorForGeneration();
/* 387 */     int minY = heightAccessor.getMinY() + 1;
/* 388 */     int maxY = heightAccessor.getMaxY();
/*     */     
/* 390 */     return new BoundingBox(targetBlockX, minY, targetBlockZ, targetBlockX + 15, maxY, targetBlockZ + 15);
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
/* 401 */   public int getSpawnHeight(LevelHeightAccessor heightAccessor) { return 64; }
/*     */ 
/*     */ 
/*     */   
/* 405 */   public BiomeSource getBiomeSource() { return this.biomeSource; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WeightedList<MobSpawnSettings.SpawnerData> getMobsAt(Holder<Biome> biome, StructureManager structureManager, MobCategory mobCategory, BlockPos pos) {
/* 411 */     Map<Structure, LongSet> structures = structureManager.getAllStructuresAt(pos);
/*     */     
/* 413 */     for (Map.Entry<Structure, LongSet> entry : structures.entrySet()) {
/* 414 */       Structure structure = (Structure)entry.getKey();
/* 415 */       StructureSpawnOverride override = (StructureSpawnOverride)structure.spawnOverrides().get(mobCategory);
/* 416 */       if (override == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 420 */       MutableBoolean inOverrideBox = new MutableBoolean(false);
/*     */ 
/*     */       
/* 423 */       Predicate<StructureStart> check = (override.boundingBox() == StructureSpawnOverride.BoundingBoxType.PIECE) ? (start -> structureManager.structureHasPieceAt(pos, start)) : (start -> start.getBoundingBox().isInside(pos));
/*     */       
/* 425 */       structureManager.fillStartsForStructure(structure, (LongSet)entry.getValue(), start -> {
/* 426 */             if (inOverrideBox.isFalse() && check.test(start)) {
/* 427 */               inOverrideBox.setTrue();
/*     */             }
/*     */           });
/* 430 */       if (inOverrideBox.isTrue()) {
/* 431 */         return override.spawns();
/*     */       }
/*     */     } 
/*     */     
/* 435 */     return ((Biome)biome.value()).getMobSettings().getMobs(mobCategory);
/*     */   }
/*     */   
/*     */   public void createStructures(RegistryAccess registryAccess, ChunkGeneratorStructureState state, StructureManager structureManager, ChunkAccess centerChunk, StructureTemplateManager structureTemplateManager, ResourceKey<Level> level) {
/* 439 */     if (SharedConstants.DEBUG_DISABLE_STRUCTURES) {
/*     */       return;
/*     */     }
/*     */     
/* 443 */     ChunkPos sourceChunkPos = centerChunk.getPos();
/* 444 */     SectionPos sectionPos = SectionPos.bottomOf(centerChunk);
/*     */     
/* 446 */     RandomState randomState = state.randomState();
/*     */     
/* 448 */     state.possibleStructureSets().forEach(set -> {
/*     */           
/* 450 */           StructurePlacement featurePlacement = ((StructureSet)set.value()).placement();
/*     */           
/* 452 */           List<StructureSet.StructureSelectionEntry> structures = ((StructureSet)set.value()).structures();
/* 453 */           for (StructureSet.StructureSelectionEntry structure : structures) {
/* 454 */             StructureStart existingStart = structureManager.getStartForStructure(sectionPos, (Structure)structure.structure().value(), centerChunk);
/* 455 */             if (existingStart != null && existingStart.isValid()) {
/*     */               return;
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/* 461 */           if (!featurePlacement.isStructureChunk(state, sourceChunkPos.x, sourceChunkPos.z)) {
/*     */             return;
/*     */           }
/*     */           
/* 465 */           if (structures.size() == 1) {
/* 466 */             tryGenerateStructure((StructureSet.StructureSelectionEntry)structures.get(0), structureManager, registryAccess, randomState, structureTemplateManager, state.getLevelSeed(), centerChunk, sourceChunkPos, sectionPos, level);
/*     */ 
/*     */ 
/*     */             
/*     */             return;
/*     */           } 
/*     */ 
/*     */           
/* 474 */           ArrayList<StructureSet.StructureSelectionEntry> options = new ArrayList<StructureSet.StructureSelectionEntry>(structures.size());
/* 475 */           options.addAll(structures);
/* 476 */           WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
/* 477 */           random.setLargeFeatureSeed(state.getLevelSeed(), sourceChunkPos.x, sourceChunkPos.z);
/* 478 */           int total = 0;
/* 479 */           for (StructureSet.StructureSelectionEntry option : options) {
/* 480 */             total += option.weight();
/*     */           }
/*     */           
/* 483 */           while (!options.isEmpty()) {
/* 484 */             int choice = random.nextInt(total);
/* 485 */             int index = 0;
/* 486 */             for (StructureSet.StructureSelectionEntry option : options) {
/* 487 */               choice -= option.weight();
/* 488 */               if (choice < 0) {
/*     */                 break;
/*     */               }
/* 491 */               index++;
/*     */             } 
/* 493 */             StructureSet.StructureSelectionEntry selected = (StructureSet.StructureSelectionEntry)options.get(index);
/*     */             
/* 495 */             if (tryGenerateStructure(selected, structureManager, registryAccess, randomState, structureTemplateManager, state.getLevelSeed(), centerChunk, sourceChunkPos, sectionPos, level)) {
/*     */               return;
/*     */             }
/*     */             
/* 499 */             options.remove(index);
/* 500 */             total -= selected.weight();
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean tryGenerateStructure(StructureSet.StructureSelectionEntry selected, StructureManager structureManager, RegistryAccess registryAccess, RandomState randomState, StructureTemplateManager structureTemplateManager, long seed, ChunkAccess centerChunk, ChunkPos sourceChunkPos, SectionPos sectionPos, ResourceKey<Level> level) {
/* 507 */     Structure structure = (Structure)selected.structure().value();
/* 508 */     int references = fetchReferences(structureManager, centerChunk, sectionPos, structure);
/*     */ 
/*     */     
/* 511 */     HolderSet<Biome> biomeAllowedForStructure = structure.biomes();
/* 512 */     Objects.requireNonNull(biomeAllowedForStructure); Predicate<Holder<Biome>> biomePredicate = biomeAllowedForStructure::contains;
/* 513 */     StructureStart start = structure.generate(selected.structure(), level, registryAccess, this, this.biomeSource, randomState, structureTemplateManager, seed, sourceChunkPos, references, centerChunk, biomePredicate);
/* 514 */     if (start.isValid()) {
/* 515 */       structureManager.setStartForStructure(sectionPos, structure, start, centerChunk);
/* 516 */       return true;
/*     */     } 
/* 518 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int fetchReferences(StructureManager structureManager, ChunkAccess centerChunk, SectionPos sectionPos, Structure structure) {
/* 523 */     StructureStart prevEntry = structureManager.getStartForStructure(sectionPos, structure, centerChunk);
/* 524 */     return (prevEntry != null) ? prevEntry.getReferences() : 0;
/*     */   }
/*     */   
/*     */   public void createReferences(WorldGenLevel level, StructureManager structureManager, ChunkAccess centerChunk) {
/* 528 */     int range = 8;
/* 529 */     ChunkPos chunkPos = centerChunk.getPos();
/* 530 */     int targetX = chunkPos.x;
/* 531 */     int targetZ = chunkPos.z;
/* 532 */     int targetBlockX = chunkPos.getMinBlockX();
/* 533 */     int targetBlockZ = chunkPos.getMinBlockZ();
/*     */     
/* 535 */     SectionPos pos = SectionPos.bottomOf(centerChunk);
/*     */     
/* 537 */     for (int sourceX = targetX - 8; sourceX <= targetX + 8; sourceX++) {
/* 538 */       for (int sourceZ = targetZ - 8; sourceZ <= targetZ + 8; sourceZ++) {
/* 539 */         long sourceChunkKey = ChunkPos.asLong(sourceX, sourceZ);
/*     */         
/* 541 */         for (StructureStart start : level.getChunk(sourceX, sourceZ).getAllStarts().values()) {
/*     */           try {
/* 543 */             if (start.isValid() && start.getBoundingBox().intersects(targetBlockX, targetBlockZ, targetBlockX + 15, targetBlockZ + 15)) {
/* 544 */               structureManager.addReferenceForStructure(pos, start.getStructure(), sourceChunkKey, centerChunk);
/*     */             }
/* 546 */           } catch (Exception e) {
/* 547 */             CrashReport report = CrashReport.forThrowable(e, "Generating structure reference");
/* 548 */             CrashReportCategory structure = report.addCategory("Structure");
/* 549 */             Optional<? extends Registry<Structure>> configuredStructuresRegistry = level.registryAccess().lookup(Registries.STRUCTURE);
/* 550 */             structure.setDetail("Id", () -> (String)configuredStructuresRegistry.map(()).orElse("UNKNOWN"));
/* 551 */             structure.setDetail("Name", () -> BuiltInRegistries.STRUCTURE_TYPE.getKey(start.getStructure().type()).toString());
/* 552 */             structure.setDetail("Class", () -> start.getStructure().getClass().getCanonicalName());
/* 553 */             throw new ReportedException(report);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
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
/* 574 */   public int getFirstFreeHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor heightAccessor, RandomState randomState) { return getBaseHeight(x, z, type, heightAccessor, randomState); }
/*     */ 
/*     */ 
/*     */   
/* 578 */   public int getFirstOccupiedHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor heightAccessor, RandomState randomState) { return getBaseHeight(x, z, type, heightAccessor, randomState) - 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 589 */   public BiomeGenerationSettings getBiomeGenerationSettings(Holder<Biome> biome) { return (BiomeGenerationSettings)this.generationSettingsGetter.apply(biome); }
/*     */   
/*     */   protected abstract MapCodec<? extends ChunkGenerator> codec();
/*     */   
/*     */   public abstract void applyCarvers(WorldGenRegion paramWorldGenRegion, long paramLong, RandomState paramRandomState, BiomeManager paramBiomeManager, StructureManager paramStructureManager, ChunkAccess paramChunkAccess);
/*     */   
/*     */   public abstract void buildSurface(WorldGenRegion paramWorldGenRegion, StructureManager paramStructureManager, RandomState paramRandomState, ChunkAccess paramChunkAccess);
/*     */   
/*     */   public abstract void spawnOriginalMobs(WorldGenRegion paramWorldGenRegion);
/*     */   
/*     */   public abstract int getGenDepth();
/*     */   
/*     */   public abstract CompletableFuture<ChunkAccess> fillFromNoise(Blender paramBlender, RandomState paramRandomState, StructureManager paramStructureManager, ChunkAccess paramChunkAccess);
/*     */   
/*     */   public abstract int getSeaLevel();
/*     */   
/*     */   public abstract int getMinY();
/*     */   
/*     */   public abstract int getBaseHeight(int paramInt1, int paramInt2, Heightmap.Types paramTypes, LevelHeightAccessor paramLevelHeightAccessor, RandomState paramRandomState);
/*     */   
/*     */   public abstract NoiseColumn getBaseColumn(int paramInt1, int paramInt2, LevelHeightAccessor paramLevelHeightAccessor, RandomState paramRandomState);
/*     */   
/*     */   public abstract void addDebugScreenInfo(List<String> paramList, RandomState paramRandomState, BlockPos paramBlockPos);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\ChunkGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */