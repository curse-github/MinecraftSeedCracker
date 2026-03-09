/*     */ package net.minecraft.world.level.levelgen;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.NoiseColumn;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.BiomeResolver;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.CarvingMask;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.ProtoChunk;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ import net.minecraft.world.level.levelgen.carver.CarvingContext;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public final class NoiseBasedChunkGenerator extends ChunkGenerator {
/*  53 */   public static final MapCodec<NoiseBasedChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BiomeSource.CODEC
/*  54 */         .fieldOf("biome_source").forGetter(()), NoiseGeneratorSettings.CODEC
/*  55 */         .fieldOf("settings").forGetter(()))
/*  56 */       .apply(i, i.stable(NoiseBasedChunkGenerator::new)));
/*     */   
/*  58 */   private static final BlockState AIR = Blocks.AIR.defaultBlockState();
/*     */   
/*     */   private final Holder<NoiseGeneratorSettings> settings;
/*     */   
/*     */   private final Supplier<Aquifer.FluidPicker> globalFluidPicker;
/*     */   
/*     */   public NoiseBasedChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
/*  65 */     super(biomeSource);
/*     */     
/*  67 */     this.settings = settings;
/*  68 */     this.globalFluidPicker = Suppliers.memoize(() -> createFluidPicker((NoiseGeneratorSettings)settings.value()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static Aquifer.FluidPicker createFluidPicker(NoiseGeneratorSettings settings) {
/*  73 */     Aquifer.FluidStatus lavaStatus = new Aquifer.FluidStatus(-54, Blocks.LAVA.defaultBlockState());
/*  74 */     int seaLevel = settings.seaLevel();
/*  75 */     Aquifer.FluidStatus seaStatus = new Aquifer.FluidStatus(seaLevel, settings.defaultFluid());
/*     */     
/*  77 */     Aquifer.FluidStatus emptyStatus = new Aquifer.FluidStatus(DimensionType.MIN_Y * 2, Blocks.AIR.defaultBlockState());
/*     */ 
/*     */     
/*  80 */     return (x, y, z) -> {
/*  81 */         if (SharedConstants.DEBUG_DISABLE_FLUID_GENERATION) {
/*  82 */           return emptyStatus;
/*     */         }
/*  84 */         if (y < Math.min(-54, seaLevel)) {
/*  85 */           return lavaStatus;
/*     */         }
/*  87 */         return seaStatus;
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkAccess> createBiomes(RandomState randomState, Blender blender, StructureManager structureManager, ChunkAccess protoChunk) {
/*  93 */     return CompletableFuture.supplyAsync(() -> {
/*  94 */           doCreateBiomes(blender, randomState, structureManager, protoChunk);
/*  95 */           return protoChunk;
/*  96 */         }Util.backgroundExecutor().forName("init_biomes"));
/*     */   }
/*     */   
/*     */   private void doCreateBiomes(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess protoChunk) {
/* 100 */     NoiseChunk noiseChunk = protoChunk.getOrCreateNoiseChunk(chunk -> createNoiseChunk(chunk, structureManager, blender, randomState));
/*     */     
/* 102 */     BiomeResolver biomeResolver = BelowZeroRetrogen.getBiomeResolver(blender.getBiomeResolver(this.biomeSource), protoChunk);
/*     */     
/* 104 */     protoChunk.fillBiomesFromNoise(biomeResolver, noiseChunk.cachedClimateSampler(randomState.router(), ((NoiseGeneratorSettings)this.settings.value()).spawnTarget()));
/*     */   }
/*     */ 
/*     */   
/* 108 */   private NoiseChunk createNoiseChunk(ChunkAccess chunk, StructureManager structureManager, Blender blender, RandomState randomState) { return NoiseChunk.forChunk(chunk, randomState, Beardifier.forStructuresInChunk(structureManager, chunk.getPos()), (NoiseGeneratorSettings)this.settings.value(), (Aquifer.FluidPicker)this.globalFluidPicker.get(), blender); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   protected MapCodec<? extends ChunkGenerator> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */   
/* 117 */   public Holder<NoiseGeneratorSettings> generatorSettings() { return this.settings; }
/*     */ 
/*     */ 
/*     */   
/* 121 */   public boolean stable(ResourceKey<NoiseGeneratorSettings> expectedPreset) { return this.settings.is(expectedPreset); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor heightAccessor, RandomState randomState) { return iterateNoiseColumn(heightAccessor, randomState, x, z, null, type.isOpaque()).orElse(heightAccessor.getMinY()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor heightAccessor, RandomState randomState) {
/* 131 */     MutableObject<NoiseColumn> result = new MutableObject<NoiseColumn>();
/* 132 */     iterateNoiseColumn(heightAccessor, randomState, x, z, result, null);
/* 133 */     return (NoiseColumn)result.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addDebugScreenInfo(List<String> result, RandomState randomState, BlockPos feetPos) {
/* 138 */     DecimalFormat format = new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(Locale.ROOT));
/*     */     
/* 140 */     NoiseRouter router = randomState.router();
/* 141 */     DensityFunction.SinglePointContext context = new DensityFunction.SinglePointContext(feetPos.getX(), feetPos.getY(), feetPos.getZ());
/*     */     
/* 143 */     double weirdness = router.ridges().compute(context);
/* 144 */     result.add("NoiseRouter T: " + format
/* 145 */         .format(router.temperature().compute(context)) + " V: " + format
/* 146 */         .format(router.vegetation().compute(context)) + " C: " + format
/* 147 */         .format(router.continents().compute(context)) + " E: " + format
/* 148 */         .format(router.erosion().compute(context)) + " D: " + format
/* 149 */         .format(router.depth().compute(context)) + " W: " + format
/* 150 */         .format(weirdness) + " PV: " + format
/* 151 */         .format(NoiseRouterData.peaksAndValleys((float)weirdness)) + " PS: " + format
/* 152 */         .format(router.preliminarySurfaceLevel().compute(context)) + " N: " + format
/* 153 */         .format(router.finalDensity().compute(context)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private OptionalInt iterateNoiseColumn(LevelHeightAccessor heightAccessor, RandomState randomState, int blockX, int blockZ, MutableObject<NoiseColumn> columnReference, Predicate<BlockState> tester) {
/*     */     BlockState[] writeTo;
/* 164 */     NoiseSettings noiseSettings = ((NoiseGeneratorSettings)this.settings.value()).noiseSettings().clampToHeightAccessor(heightAccessor);
/* 165 */     int cellHeight = noiseSettings.getCellHeight();
/*     */     
/* 167 */     int minY = noiseSettings.minY();
/* 168 */     int cellMinY = Mth.floorDiv(minY, cellHeight);
/* 169 */     int cellCountY = Mth.floorDiv(noiseSettings.height(), cellHeight);
/*     */     
/* 171 */     if (cellCountY <= 0) {
/* 172 */       return OptionalInt.empty();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 177 */     if (columnReference == null) {
/* 178 */       writeTo = null;
/*     */     } else {
/* 180 */       writeTo = new BlockState[noiseSettings.height()];
/* 181 */       columnReference.setValue(new NoiseColumn(minY, writeTo));
/*     */     } 
/*     */     
/* 184 */     int cellWidth = noiseSettings.getCellWidth();
/*     */     
/* 186 */     int noiseChunkX = Math.floorDiv(blockX, cellWidth);
/* 187 */     int noiseChunkZ = Math.floorDiv(blockZ, cellWidth);
/* 188 */     int xInCell = Math.floorMod(blockX, cellWidth);
/* 189 */     int zInCell = Math.floorMod(blockZ, cellWidth);
/* 190 */     int firstBlockX = noiseChunkX * cellWidth;
/* 191 */     int firstBlockZ = noiseChunkZ * cellWidth;
/*     */     
/* 193 */     double factorX = xInCell / cellWidth;
/* 194 */     double factorZ = zInCell / cellWidth;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     NoiseChunk noiseChunk = new NoiseChunk(1, randomState, firstBlockX, firstBlockZ, noiseSettings, DensityFunctions.BeardifierMarker.INSTANCE, (NoiseGeneratorSettings)this.settings.value(), (Aquifer.FluidPicker)this.globalFluidPicker.get(), Blender.empty());
/*     */ 
/*     */     
/* 203 */     noiseChunk.initializeForFirstCellX();
/* 204 */     noiseChunk.advanceCellX(0);
/*     */     
/* 206 */     for (int cellYIndex = cellCountY - 1; cellYIndex >= 0; cellYIndex--) {
/* 207 */       noiseChunk.selectCellYZ(cellYIndex, 0);
/*     */       
/* 209 */       for (int yInCell = cellHeight - 1; yInCell >= 0; yInCell--) {
/* 210 */         int posY = (cellMinY + cellYIndex) * cellHeight + yInCell;
/*     */         
/* 212 */         double factorY = yInCell / cellHeight;
/* 213 */         noiseChunk.updateForY(posY, factorY);
/* 214 */         noiseChunk.updateForX(blockX, factorX);
/* 215 */         noiseChunk.updateForZ(blockZ, factorZ);
/*     */         
/* 217 */         BlockState baseState = noiseChunk.getInterpolatedState();
/* 218 */         BlockState state = (baseState == null) ? ((NoiseGeneratorSettings)this.settings.value()).defaultBlock() : baseState;
/*     */         
/* 220 */         if (writeTo != null) {
/* 221 */           int yIndex = cellYIndex * cellHeight + yInCell;
/* 222 */           writeTo[yIndex] = state;
/*     */         } 
/* 224 */         if (tester != null && tester.test(state)) {
/* 225 */           noiseChunk.stopInterpolation();
/* 226 */           return OptionalInt.of(posY + 1);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 231 */     noiseChunk.stopInterpolation();
/*     */     
/* 233 */     return OptionalInt.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void buildSurface(WorldGenRegion region, StructureManager structureManager, RandomState randomState, ChunkAccess protoChunk) {
/* 238 */     if (SharedConstants.debugVoidTerrain(protoChunk.getPos()) || SharedConstants.DEBUG_DISABLE_SURFACE) {
/*     */       return;
/*     */     }
/*     */     
/* 242 */     WorldGenerationContext context = new WorldGenerationContext(this, region);
/*     */     
/* 244 */     buildSurface(protoChunk, context, randomState, structureManager, region.getBiomeManager(), region.registryAccess().lookupOrThrow(Registries.BIOME), Blender.of(region));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void buildSurface(ChunkAccess protoChunk, WorldGenerationContext context, RandomState randomState, StructureManager structureManager, BiomeManager biomeManager, Registry<Biome> biomeRegistry, Blender blender) {
/* 249 */     NoiseChunk noiseChunk = protoChunk.getOrCreateNoiseChunk(chunk -> createNoiseChunk(chunk, structureManager, blender, randomState));
/* 250 */     NoiseGeneratorSettings settings = (NoiseGeneratorSettings)this.settings.value();
/* 251 */     randomState.surfaceSystem().buildSurface(randomState, biomeManager, biomeRegistry, settings.useLegacyRandomSource(), context, protoChunk, noiseChunk, settings.surfaceRule());
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk) {
/* 256 */     if (SharedConstants.DEBUG_DISABLE_CARVERS) {
/*     */       return;
/*     */     }
/* 259 */     BiomeManager correctBiomeManager = biomeManager.withDifferentSource((quartX, quartY, quartZ) -> this.biomeSource.getNoiseBiome(quartX, quartY, quartZ, randomState.sampler()));
/*     */     
/* 261 */     WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
/* 262 */     int range = 8;
/*     */     
/* 264 */     ChunkPos pos = chunk.getPos();
/*     */     
/* 266 */     NoiseChunk noiseChunk = chunk.getOrCreateNoiseChunk(c -> createNoiseChunk(c, structureManager, Blender.of(region), randomState));
/* 267 */     Aquifer aquifer = noiseChunk.aquifer();
/* 268 */     CarvingContext context = new CarvingContext(this, region.registryAccess(), chunk.getHeightAccessorForGeneration(), noiseChunk, randomState, ((NoiseGeneratorSettings)this.settings.value()).surfaceRule());
/*     */     
/* 270 */     CarvingMask mask = ((ProtoChunk)chunk).getOrCreateCarvingMask();
/* 271 */     for (int dx = -8; dx <= 8; dx++) {
/* 272 */       for (int dz = -8; dz <= 8; dz++) {
/* 273 */         ChunkPos sourcePos = new ChunkPos(pos.x + dx, pos.z + dz);
/* 274 */         ChunkAccess carverCenterChunk = region.getChunk(sourcePos.x, sourcePos.z);
/* 275 */         BiomeGenerationSettings sourceBiomeGenerationSettings = carverCenterChunk.carverBiome(() -> getBiomeGenerationSettings(this.biomeSource.getNoiseBiome(QuartPos.fromBlock(sourcePos.getMinBlockX()), 0, QuartPos.fromBlock(sourcePos.getMinBlockZ()), randomState.sampler())));
/* 276 */         Iterable<Holder<ConfiguredWorldCarver<?>>> carvers = sourceBiomeGenerationSettings.getCarvers();
/*     */         
/* 278 */         int index = 0;
/* 279 */         for (Holder<ConfiguredWorldCarver<?>> carverHolder : carvers) {
/* 280 */           ConfiguredWorldCarver<?> carver = (ConfiguredWorldCarver)carverHolder.value();
/* 281 */           random.setLargeFeatureSeed(seed + index, sourcePos.x, sourcePos.z);
/* 282 */           if (carver.isStartChunk(random)) {
/* 283 */             Objects.requireNonNull(correctBiomeManager); carver.carve(context, chunk, correctBiomeManager::getBiome, random, aquifer, sourcePos, mask);
/*     */           } 
/* 285 */           index++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess centerChunk) {
/* 293 */     NoiseSettings noiseSettings = ((NoiseGeneratorSettings)this.settings.value()).noiseSettings().clampToHeightAccessor(centerChunk.getHeightAccessorForGeneration());
/*     */     
/* 295 */     int minY = noiseSettings.minY();
/* 296 */     int cellYMin = Mth.floorDiv(minY, noiseSettings.getCellHeight());
/* 297 */     int cellCountY = Mth.floorDiv(noiseSettings.height(), noiseSettings.getCellHeight());
/*     */     
/* 299 */     if (cellCountY <= 0) {
/* 300 */       return CompletableFuture.completedFuture(centerChunk);
/*     */     }
/*     */     
/* 303 */     return CompletableFuture.supplyAsync(() -> {
/*     */           
/* 305 */           int topSectionIndex = centerChunk.getSectionIndex(cellCountY * noiseSettings.getCellHeight() - 1 + minY);
/* 306 */           int bottomSectionIndex = centerChunk.getSectionIndex(minY);
/* 307 */           sections = Sets.newHashSet();
/* 308 */           for (int sectionIndex = topSectionIndex; sectionIndex >= bottomSectionIndex; sectionIndex--) {
/* 309 */             LevelChunkSection section = centerChunk.getSection(sectionIndex);
/* 310 */             section.acquire();
/* 311 */             sections.add(section);
/*     */           } 
/*     */           try {
/* 314 */             return doFill(blender, structureManager, randomState, centerChunk, cellYMin, cellCountY);
/*     */           } finally {
/* 316 */             for (LevelChunkSection section : sections) {
/* 317 */               section.release();
/*     */             }
/*     */           }
/*     */         
/* 321 */         }Util.backgroundExecutor().forName("wgen_fill_noise"));
/*     */   }
/*     */ 
/*     */   
/*     */   private ChunkAccess doFill(Blender blender, StructureManager structureManager, RandomState randomState, ChunkAccess centerChunk, int cellMinY, int cellCountY) {
/* 326 */     NoiseChunk noiseChunk = centerChunk.getOrCreateNoiseChunk(chunk -> createNoiseChunk(chunk, structureManager, blender, randomState));
/*     */     
/* 328 */     Heightmap oceanFloor = centerChunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
/* 329 */     Heightmap worldSurface = centerChunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
/*     */     
/* 331 */     ChunkPos chunkPos = centerChunk.getPos();
/*     */     
/* 333 */     int chunkStartBlockX = chunkPos.getMinBlockX();
/* 334 */     int chunkStartBlockZ = chunkPos.getMinBlockZ();
/*     */     
/* 336 */     Aquifer aquifer = noiseChunk.aquifer();
/*     */     
/* 338 */     noiseChunk.initializeForFirstCellX();
/*     */     
/* 340 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/*     */     
/* 342 */     int cellWidth = noiseChunk.cellWidth();
/* 343 */     int cellHeight = noiseChunk.cellHeight();
/*     */     
/* 345 */     int cellCountX = 16 / cellWidth;
/* 346 */     int cellCountZ = 16 / cellWidth;
/*     */ 
/*     */     
/* 349 */     for (int cellXIndex = 0; cellXIndex < cellCountX; cellXIndex++) {
/* 350 */       noiseChunk.advanceCellX(cellXIndex);
/*     */       
/* 352 */       for (int cellZIndex = 0; cellZIndex < cellCountZ; cellZIndex++) {
/* 353 */         int lastSectionIndex = centerChunk.getSectionsCount() - 1;
/* 354 */         LevelChunkSection section = centerChunk.getSection(lastSectionIndex);
/*     */         
/* 356 */         for (int cellYIndex = cellCountY - 1; cellYIndex >= 0; cellYIndex--) {
/* 357 */           noiseChunk.selectCellYZ(cellYIndex, cellZIndex);
/*     */           
/* 359 */           for (int yInCell = cellHeight - 1; yInCell >= 0; yInCell--) {
/* 360 */             int posY = (cellMinY + cellYIndex) * cellHeight + yInCell;
/* 361 */             int yInSection = posY & 0xF;
/*     */             
/* 363 */             int sectionIndex = centerChunk.getSectionIndex(posY);
/* 364 */             if (lastSectionIndex != sectionIndex) {
/* 365 */               lastSectionIndex = sectionIndex;
/* 366 */               section = centerChunk.getSection(sectionIndex);
/*     */             } 
/*     */             
/* 369 */             double factorY = yInCell / cellHeight;
/* 370 */             noiseChunk.updateForY(posY, factorY);
/*     */             
/* 372 */             for (int xInCell = 0; xInCell < cellWidth; xInCell++) {
/* 373 */               int posX = chunkStartBlockX + cellXIndex * cellWidth + xInCell;
/* 374 */               int xInSection = posX & 0xF;
/*     */               
/* 376 */               double factorX = xInCell / cellWidth;
/* 377 */               noiseChunk.updateForX(posX, factorX);
/*     */               
/* 379 */               for (int zInCell = 0; zInCell < cellWidth; zInCell++) {
/* 380 */                 int posZ = chunkStartBlockZ + cellZIndex * cellWidth + zInCell;
/* 381 */                 int zInSection = posZ & 0xF;
/*     */                 
/* 383 */                 double factorZ = zInCell / cellWidth;
/*     */                 
/* 385 */                 noiseChunk.updateForZ(posZ, factorZ);
/*     */                 
/* 387 */                 BlockState state = noiseChunk.getInterpolatedState();
/*     */                 
/* 389 */                 if (state == null) {
/* 390 */                   state = ((NoiseGeneratorSettings)this.settings.value()).defaultBlock();
/*     */                 }
/*     */                 
/* 393 */                 state = debugPreliminarySurfaceLevel(noiseChunk, posX, posY, posZ, state);
/*     */                 
/* 395 */                 if (state != AIR && !SharedConstants.debugVoidTerrain(centerChunk.getPos())) {
/*     */ 
/*     */                   
/* 398 */                   section.setBlockState(xInSection, yInSection, zInSection, state, false);
/* 399 */                   oceanFloor.update(xInSection, posY, zInSection, state);
/* 400 */                   worldSurface.update(xInSection, posY, zInSection, state);
/*     */                   
/* 402 */                   if (aquifer.shouldScheduleFluidUpdate() && !state.getFluidState().isEmpty()) {
/* 403 */                     blockPos.set(posX, posY, posZ);
/*     */                     
/* 405 */                     centerChunk.markPosForPostprocessing(blockPos);
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/* 412 */       }  noiseChunk.swapSlices();
/*     */     } 
/* 414 */     noiseChunk.stopInterpolation();
/* 415 */     return centerChunk;
/*     */   }
/*     */   
/*     */   private BlockState debugPreliminarySurfaceLevel(NoiseChunk noiseChunk, int posX, int posY, int posZ, BlockState state) {
/* 419 */     if (SharedConstants.DEBUG_AQUIFERS && posZ >= 0 && posZ % 4 == 0) {
/* 420 */       int preliminarySurfaceLevel = noiseChunk.preliminarySurfaceLevel(posX, posZ);
/* 421 */       int adjustedSurfaceLevel = preliminarySurfaceLevel + 8;
/* 422 */       if (posY == adjustedSurfaceLevel) {
/* 423 */         state = (adjustedSurfaceLevel < getSeaLevel()) ? Blocks.SLIME_BLOCK.defaultBlockState() : Blocks.HONEY_BLOCK.defaultBlockState();
/*     */       }
/*     */     } 
/* 426 */     return state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 431 */   public int getGenDepth() { return ((NoiseGeneratorSettings)this.settings.value()).noiseSettings().height(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 436 */   public int getSeaLevel() { return ((NoiseGeneratorSettings)this.settings.value()).seaLevel(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 441 */   public int getMinY() { return ((NoiseGeneratorSettings)this.settings.value()).noiseSettings().minY(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void spawnOriginalMobs(WorldGenRegion worldGenRegion) {
/* 447 */     if (((NoiseGeneratorSettings)this.settings.value()).disableMobGeneration()) {
/*     */       return;
/*     */     }
/* 450 */     ChunkPos center = worldGenRegion.getCenter();
/*     */     
/* 452 */     Holder<Biome> biome = worldGenRegion.getBiome(center.getWorldPosition().atY(worldGenRegion.getMaxY()));
/*     */     
/* 454 */     WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
/* 455 */     random.setDecorationSeed(worldGenRegion.getSeed(), center.getMinBlockX(), center.getMinBlockZ());
/* 456 */     NaturalSpawner.spawnMobsForChunkGeneration(worldGenRegion, biome, center, random);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\NoiseBasedChunkGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */