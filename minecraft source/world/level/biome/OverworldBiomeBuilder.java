/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.RegistrySetBuilder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.NoiseData;
/*     */ import net.minecraft.data.worldgen.TerrainProvider;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.BoundedFloatFunction;
/*     */ import net.minecraft.util.CubicSpline;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.level.levelgen.DensityFunction;
/*     */ import net.minecraft.world.level.levelgen.DensityFunctions;
/*     */ import net.minecraft.world.level.levelgen.NoiseRouterData;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class OverworldBiomeBuilder
/*     */ {
/*     */   private static final float VALLEY_SIZE = 0.05F;
/*     */   private static final float LOW_START = 0.26666668F;
/*     */   public static final float HIGH_START = 0.4F;
/*     */   private static final float HIGH_END = 0.93333334F;
/*     */   private static final float PEAK_SIZE = 0.1F;
/*     */   public static final float PEAK_START = 0.56666666F;
/*     */   private static final float PEAK_END = 0.7666667F;
/*     */   public static final float NEAR_INLAND_START = -0.11F;
/*     */   public static final float MID_INLAND_START = 0.03F;
/*     */   public static final float FAR_INLAND_START = 0.3F;
/*     */   public static final float EROSION_INDEX_1_START = -0.78F;
/*     */   public static final float EROSION_INDEX_2_START = -0.375F;
/*     */   private static final float EROSION_DEEP_DARK_DRYNESS_THRESHOLD = -0.225F;
/*     */   private static final float DEPTH_DEEP_DARK_DRYNESS_THRESHOLD = 0.9F;
/*  63 */   private final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);
/*     */ 
/*     */   
/*  66 */   private final Climate.Parameter[] temperatures = { Climate.Parameter.span(-1.0F, -0.45F), 
/*  67 */       Climate.Parameter.span(-0.45F, -0.15F), 
/*  68 */       Climate.Parameter.span(-0.15F, 0.2F), 
/*  69 */       Climate.Parameter.span(0.2F, 0.55F), 
/*  70 */       Climate.Parameter.span(0.55F, 1.0F) };
/*     */ 
/*     */   
/*  73 */   private final Climate.Parameter[] humidities = { Climate.Parameter.span(-1.0F, -0.35F), 
/*  74 */       Climate.Parameter.span(-0.35F, -0.1F), 
/*  75 */       Climate.Parameter.span(-0.1F, 0.1F), 
/*  76 */       Climate.Parameter.span(0.1F, 0.3F), 
/*  77 */       Climate.Parameter.span(0.3F, 1.0F) };
/*     */ 
/*     */ 
/*     */   
/*  81 */   private final Climate.Parameter[] erosions = { Climate.Parameter.span(-1.0F, -0.78F), 
/*  82 */       Climate.Parameter.span(-0.78F, -0.375F), 
/*  83 */       Climate.Parameter.span(-0.375F, -0.2225F), 
/*  84 */       Climate.Parameter.span(-0.2225F, 0.05F), 
/*  85 */       Climate.Parameter.span(0.05F, 0.45F), 
/*  86 */       Climate.Parameter.span(0.45F, 0.55F), 
/*  87 */       Climate.Parameter.span(0.55F, 1.0F) };
/*     */ 
/*     */   
/*  90 */   private final Climate.Parameter FROZEN_RANGE = this.temperatures[0];
/*  91 */   private final Climate.Parameter UNFROZEN_RANGE = Climate.Parameter.span(this.temperatures[1], this.temperatures[4]);
/*     */   
/*  93 */   private final Climate.Parameter mushroomFieldsContinentalness = Climate.Parameter.span(-1.2F, -1.05F);
/*  94 */   private final Climate.Parameter deepOceanContinentalness = Climate.Parameter.span(-1.05F, -0.455F);
/*  95 */   private final Climate.Parameter oceanContinentalness = Climate.Parameter.span(-0.455F, -0.19F);
/*  96 */   private final Climate.Parameter coastContinentalness = Climate.Parameter.span(-0.19F, -0.11F);
/*  97 */   private final Climate.Parameter inlandContinentalness = Climate.Parameter.span(-0.11F, 0.55F);
/*     */   
/*  99 */   private final Climate.Parameter nearInlandContinentalness = Climate.Parameter.span(-0.11F, 0.03F);
/* 100 */   private final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(0.03F, 0.3F);
/* 101 */   private final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(0.3F, 1.0F);
/*     */ 
/*     */   
/* 104 */   private final ResourceKey<Biome>[][] OCEANS = { { Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.WARM_OCEAN }, { Biomes.FROZEN_OCEAN, Biomes.COLD_OCEAN, Biomes.OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   private final ResourceKey<Biome>[][] MIDDLE_BIOMES = { { Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.TAIGA }, { Biomes.PLAINS, Biomes.PLAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA }, { Biomes.FLOWER_FOREST, Biomes.PLAINS, Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST }, { Biomes.SAVANNA, Biomes.SAVANNA, Biomes.FOREST, Biomes.JUNGLE, Biomes.JUNGLE }, { Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT } };
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
/* 122 */   private final ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT = { { Biomes.ICE_SPIKES, null, Biomes.SNOWY_TAIGA, null, null }, { null, null, null, null, Biomes.OLD_GROWTH_PINE_TAIGA }, { Biomes.SUNFLOWER_PLAINS, null, null, Biomes.OLD_GROWTH_BIRCH_FOREST, null }, { null, null, Biomes.PLAINS, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE }, { null, null, null, null, null } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   private final ResourceKey<Biome>[][] PLATEAU_BIOMES = { { Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA }, { Biomes.MEADOW, Biomes.MEADOW, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA }, { Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.PALE_GARDEN }, { Biomes.SAVANNA_PLATEAU, Biomes.SAVANNA_PLATEAU, Biomes.FOREST, Biomes.FOREST, Biomes.JUNGLE }, { Biomes.BADLANDS, Biomes.BADLANDS, Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.WOODED_BADLANDS } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   private final ResourceKey<Biome>[][] PLATEAU_BIOMES_VARIANT = { { Biomes.ICE_SPIKES, null, null, null, null }, { Biomes.CHERRY_GROVE, null, Biomes.MEADOW, Biomes.MEADOW, Biomes.OLD_GROWTH_PINE_TAIGA }, { Biomes.CHERRY_GROVE, Biomes.CHERRY_GROVE, Biomes.FOREST, Biomes.BIRCH_FOREST, null }, { null, null, null, null, null }, { Biomes.ERODED_BADLANDS, Biomes.ERODED_BADLANDS, null, null, null } };
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
/* 153 */   private final ResourceKey<Biome>[][] SHATTERED_BIOMES = { { Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST }, { Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST }, { Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST }, { null, null, null, null, null }, { null, null, null, null, null } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Climate.ParameterPoint> spawnTarget() {
/* 162 */     Climate.Parameter surfaceDepth = Climate.Parameter.point(0.0F);
/* 163 */     float riverClearance = 0.16F;
/* 164 */     return List.of(new Climate.ParameterPoint(this.FULL_RANGE, this.FULL_RANGE, 
/*     */ 
/*     */ 
/*     */           
/* 168 */           Climate.Parameter.span(this.inlandContinentalness, this.FULL_RANGE), this.FULL_RANGE, surfaceDepth, 
/*     */ 
/*     */           
/* 171 */           Climate.Parameter.span(-1.0F, -0.16F), 0L), new Climate.ParameterPoint(this.FULL_RANGE, this.FULL_RANGE, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 177 */           Climate.Parameter.span(this.inlandContinentalness, this.FULL_RANGE), this.FULL_RANGE, surfaceDepth, 
/*     */ 
/*     */           
/* 180 */           Climate.Parameter.span(0.16F, 1.0F), 0L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes) {
/* 187 */     if (SharedConstants.debugGenerateSquareTerrainWithoutNoise) {
/* 188 */       addDebugBiomes(biomes);
/*     */       
/*     */       return;
/*     */     } 
/* 192 */     addOffCoastBiomes(biomes);
/* 193 */     addInlandBiomes(biomes);
/* 194 */     addUndergroundBiomes(biomes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addDebugBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes) {
/* 201 */     HolderLookup.Provider builtIns = (new RegistrySetBuilder()).add(Registries.DENSITY_FUNCTION, NoiseRouterData::bootstrap).add(Registries.NOISE, NoiseData::bootstrap).build(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY));
/*     */     
/* 203 */     HolderLookup.RegistryLookup registryLookup = builtIns.lookupOrThrow(Registries.DENSITY_FUNCTION);
/* 204 */     DensityFunctions.Spline.Coordinate continents = new DensityFunctions.Spline.Coordinate(registryLookup.getOrThrow(NoiseRouterData.CONTINENTS));
/* 205 */     DensityFunctions.Spline.Coordinate erosion = new DensityFunctions.Spline.Coordinate(registryLookup.getOrThrow(NoiseRouterData.EROSION));
/* 206 */     DensityFunctions.Spline.Coordinate ridges = new DensityFunctions.Spline.Coordinate(registryLookup.getOrThrow(NoiseRouterData.RIDGES_FOLDED));
/*     */     
/* 208 */     biomes.accept(Pair.of(Climate.parameters(this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.point(0.0F), this.FULL_RANGE, 0.01F), Biomes.PLAINS));
/*     */     
/* 210 */     CubicSpline<?, ?> erosionOffsetSpline = TerrainProvider.buildErosionOffsetSpline(erosion, ridges, -0.15F, 0.0F, 0.0F, 0.1F, 0.0F, -0.03F, false, false, BoundedFloatFunction.IDENTITY);
/* 211 */     if (erosionOffsetSpline instanceof CubicSpline.Multipoint) { CubicSpline.Multipoint<?, ?> multipoint = (CubicSpline.Multipoint)erosionOffsetSpline;
/* 212 */       ResourceKey<Biome> biome = Biomes.DESERT;
/* 213 */       for (float location : multipoint.locations()) {
/* 214 */         biomes.accept(Pair.of(Climate.parameters(this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.point(location), Climate.Parameter.point(0.0F), this.FULL_RANGE, 0.0F), biome));
/* 215 */         biome = (biome == Biomes.DESERT) ? Biomes.BADLANDS : Biomes.DESERT;
/*     */       }  }
/*     */ 
/*     */     
/* 219 */     CubicSpline<?, ?> overworldOffset = TerrainProvider.overworldOffset(continents, erosion, ridges, false);
/* 220 */     if (overworldOffset instanceof CubicSpline.Multipoint) { CubicSpline.Multipoint<?, ?> multipoint = (CubicSpline.Multipoint)overworldOffset;
/* 221 */       for (float location : multipoint.locations()) {
/* 222 */         biomes.accept(Pair.of(Climate.parameters(this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.point(location), this.FULL_RANGE, Climate.Parameter.point(0.0F), this.FULL_RANGE, 0.0F), Biomes.SNOWY_TAIGA));
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addOffCoastBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes) {
/* 231 */     addSurfaceBiome(biomes, this.FULL_RANGE, this.FULL_RANGE, this.mushroomFieldsContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.MUSHROOM_FIELDS);
/* 232 */     for (int temperatureIndex = 0; temperatureIndex < this.temperatures.length; temperatureIndex++) {
/* 233 */       Climate.Parameter temperature = this.temperatures[temperatureIndex];
/*     */       
/* 235 */       addSurfaceBiome(biomes, temperature, this.FULL_RANGE, this.deepOceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, this.OCEANS[0][temperatureIndex]);
/* 236 */       addSurfaceBiome(biomes, temperature, this.FULL_RANGE, this.oceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, this.OCEANS[1][temperatureIndex]);
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
/*     */   
/*     */   private void addInlandBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes) {
/* 256 */     addMidSlice(biomes, Climate.Parameter.span(-1.0F, -0.93333334F));
/*     */ 
/*     */     
/* 259 */     addHighSlice(biomes, Climate.Parameter.span(-0.93333334F, -0.7666667F));
/* 260 */     addPeaks(biomes, Climate.Parameter.span(-0.7666667F, -0.56666666F));
/* 261 */     addHighSlice(biomes, Climate.Parameter.span(-0.56666666F, -0.4F));
/*     */ 
/*     */     
/* 264 */     addMidSlice(biomes, Climate.Parameter.span(-0.4F, -0.26666668F));
/*     */ 
/*     */     
/* 267 */     addLowSlice(biomes, Climate.Parameter.span(-0.26666668F, -0.05F));
/* 268 */     addValleys(biomes, Climate.Parameter.span(-0.05F, 0.05F));
/* 269 */     addLowSlice(biomes, Climate.Parameter.span(0.05F, 0.26666668F));
/*     */ 
/*     */     
/* 272 */     addMidSlice(biomes, Climate.Parameter.span(0.26666668F, 0.4F));
/*     */ 
/*     */     
/* 275 */     addHighSlice(biomes, Climate.Parameter.span(0.4F, 0.56666666F));
/* 276 */     addPeaks(biomes, Climate.Parameter.span(0.56666666F, 0.7666667F));
/* 277 */     addHighSlice(biomes, Climate.Parameter.span(0.7666667F, 0.93333334F));
/*     */ 
/*     */     
/* 280 */     addMidSlice(biomes, Climate.Parameter.span(0.93333334F, 1.0F));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addPeaks(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, Climate.Parameter weirdness) {
/* 288 */     for (int temperatureIndex = 0; temperatureIndex < this.temperatures.length; temperatureIndex++) {
/* 289 */       Climate.Parameter temperature = this.temperatures[temperatureIndex];
/* 290 */       for (int humidityIndex = 0; humidityIndex < this.humidities.length; humidityIndex++) {
/* 291 */         Climate.Parameter humidity = this.humidities[humidityIndex];
/*     */         
/* 293 */         ResourceKey<Biome> middleBiome = pickMiddleBiome(temperatureIndex, humidityIndex, weirdness);
/* 294 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
/* 295 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(temperatureIndex, humidityIndex, weirdness);
/* 296 */         ResourceKey<Biome> plateauBiome = pickPlateauBiome(temperatureIndex, humidityIndex, weirdness);
/* 297 */         ResourceKey<Biome> shatteredBiome = pickShatteredBiome(temperatureIndex, humidityIndex, weirdness);
/* 298 */         ResourceKey<Biome> shatteredBiomeOrWindsweptSavanna = maybePickWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, shatteredBiome);
/* 299 */         ResourceKey<Biome> peakBiome = pickPeakBiome(temperatureIndex, humidityIndex, weirdness);
/*     */         
/* 301 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[0], weirdness, 0.0F, peakBiome);
/*     */         
/* 303 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[1], weirdness, 0.0F, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
/*     */         
/* 305 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[1], weirdness, 0.0F, peakBiome);
/*     */         
/* 307 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, middleBiome);
/* 308 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], weirdness, 0.0F, plateauBiome);
/*     */         
/* 310 */         addSurfaceBiome(biomes, temperature, humidity, this.midInlandContinentalness, this.erosions[3], weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
/* 311 */         addSurfaceBiome(biomes, temperature, humidity, this.farInlandContinentalness, this.erosions[3], weirdness, 0.0F, plateauBiome);
/*     */         
/* 313 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
/*     */         
/* 315 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[5], weirdness, 0.0F, shatteredBiomeOrWindsweptSavanna);
/* 316 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, shatteredBiome);
/*     */         
/* 318 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, middleBiome);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addHighSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, Climate.Parameter weirdness) {
/* 329 */     for (int temperatureIndex = 0; temperatureIndex < this.temperatures.length; temperatureIndex++) {
/* 330 */       Climate.Parameter temperature = this.temperatures[temperatureIndex];
/* 331 */       for (int humidityIndex = 0; humidityIndex < this.humidities.length; humidityIndex++) {
/* 332 */         Climate.Parameter humidity = this.humidities[humidityIndex];
/*     */         
/* 334 */         ResourceKey<Biome> middleBiome = pickMiddleBiome(temperatureIndex, humidityIndex, weirdness);
/* 335 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
/* 336 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(temperatureIndex, humidityIndex, weirdness);
/* 337 */         ResourceKey<Biome> plateauBiome = pickPlateauBiome(temperatureIndex, humidityIndex, weirdness);
/* 338 */         ResourceKey<Biome> shatteredBiome = pickShatteredBiome(temperatureIndex, humidityIndex, weirdness);
/* 339 */         ResourceKey<Biome> middleBiomeOrWindsweptSavanna = maybePickWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, middleBiome);
/* 340 */         ResourceKey<Biome> slopeBiome = pickSlopeBiome(temperatureIndex, humidityIndex, weirdness);
/* 341 */         ResourceKey<Biome> peakBiome = pickPeakBiome(temperatureIndex, humidityIndex, weirdness);
/*     */         
/* 343 */         addSurfaceBiome(biomes, temperature, humidity, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, middleBiome);
/* 344 */         addSurfaceBiome(biomes, temperature, humidity, this.nearInlandContinentalness, this.erosions[0], weirdness, 0.0F, slopeBiome);
/* 345 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[0], weirdness, 0.0F, peakBiome);
/*     */         
/* 347 */         addSurfaceBiome(biomes, temperature, humidity, this.nearInlandContinentalness, this.erosions[1], weirdness, 0.0F, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
/* 348 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[1], weirdness, 0.0F, slopeBiome);
/*     */         
/* 350 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, middleBiome);
/* 351 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], weirdness, 0.0F, plateauBiome);
/*     */         
/* 353 */         addSurfaceBiome(biomes, temperature, humidity, this.midInlandContinentalness, this.erosions[3], weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
/* 354 */         addSurfaceBiome(biomes, temperature, humidity, this.farInlandContinentalness, this.erosions[3], weirdness, 0.0F, plateauBiome);
/*     */         
/* 356 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
/*     */         
/* 358 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[5], weirdness, 0.0F, middleBiomeOrWindsweptSavanna);
/* 359 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, shatteredBiome);
/*     */         
/* 361 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, middleBiome);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addMidSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, Climate.Parameter weirdness) {
/* 371 */     addSurfaceBiome(biomes, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[2]), weirdness, 0.0F, Biomes.STONY_SHORE);
/* 372 */     addSurfaceBiome(biomes, Climate.Parameter.span(this.temperatures[1], this.temperatures[2]), this.FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, Biomes.SWAMP);
/* 373 */     addSurfaceBiome(biomes, Climate.Parameter.span(this.temperatures[3], this.temperatures[4]), this.FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, Biomes.MANGROVE_SWAMP);
/*     */     
/* 375 */     for (int temperatureIndex = 0; temperatureIndex < this.temperatures.length; temperatureIndex++) {
/* 376 */       Climate.Parameter temperature = this.temperatures[temperatureIndex];
/* 377 */       for (int humidityIndex = 0; humidityIndex < this.humidities.length; humidityIndex++) {
/* 378 */         Climate.Parameter humidity = this.humidities[humidityIndex];
/*     */         
/* 380 */         ResourceKey<Biome> middleBiome = pickMiddleBiome(temperatureIndex, humidityIndex, weirdness);
/* 381 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
/* 382 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(temperatureIndex, humidityIndex, weirdness);
/* 383 */         ResourceKey<Biome> shatteredBiome = pickShatteredBiome(temperatureIndex, humidityIndex, weirdness);
/* 384 */         ResourceKey<Biome> plateauBiome = pickPlateauBiome(temperatureIndex, humidityIndex, weirdness);
/* 385 */         ResourceKey<Biome> beachBiome = pickBeachBiome(temperatureIndex, humidityIndex);
/* 386 */         ResourceKey<Biome> middleBiomeOrWindsweptSavanna = maybePickWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, middleBiome);
/* 387 */         ResourceKey<Biome> shatteredCoastBiome = pickShatteredCoastBiome(temperatureIndex, humidityIndex, weirdness);
/* 388 */         ResourceKey<Biome> slopeBiome = pickSlopeBiome(temperatureIndex, humidityIndex, weirdness);
/*     */         
/* 390 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[0], weirdness, 0.0F, slopeBiome);
/*     */         
/* 392 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.midInlandContinentalness), this.erosions[1], weirdness, 0.0F, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
/* 393 */         addSurfaceBiome(biomes, temperature, humidity, this.farInlandContinentalness, this.erosions[1], weirdness, 0.0F, (temperatureIndex == 0) ? slopeBiome : plateauBiome);
/*     */         
/* 395 */         addSurfaceBiome(biomes, temperature, humidity, this.nearInlandContinentalness, this.erosions[2], weirdness, 0.0F, middleBiome);
/* 396 */         addSurfaceBiome(biomes, temperature, humidity, this.midInlandContinentalness, this.erosions[2], weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
/* 397 */         addSurfaceBiome(biomes, temperature, humidity, this.farInlandContinentalness, this.erosions[2], weirdness, 0.0F, plateauBiome);
/*     */         
/* 399 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[3], weirdness, 0.0F, middleBiome);
/* 400 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[3], weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
/*     */         
/* 402 */         if (weirdness.max() < 0L) {
/* 403 */           addSurfaceBiome(biomes, temperature, humidity, this.coastContinentalness, this.erosions[4], weirdness, 0.0F, beachBiome);
/* 404 */           addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
/*     */         } else {
/* 406 */           addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
/*     */         } 
/*     */         
/* 409 */         addSurfaceBiome(biomes, temperature, humidity, this.coastContinentalness, this.erosions[5], weirdness, 0.0F, shatteredCoastBiome);
/* 410 */         addSurfaceBiome(biomes, temperature, humidity, this.nearInlandContinentalness, this.erosions[5], weirdness, 0.0F, middleBiomeOrWindsweptSavanna);
/* 411 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, shatteredBiome);
/*     */         
/* 413 */         if (weirdness.max() < 0L) {
/* 414 */           addSurfaceBiome(biomes, temperature, humidity, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, beachBiome);
/*     */         } else {
/* 416 */           addSurfaceBiome(biomes, temperature, humidity, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, middleBiome);
/*     */         } 
/*     */ 
/*     */         
/* 420 */         if (temperatureIndex == 0) {
/* 421 */           addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, middleBiome);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addLowSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, Climate.Parameter weirdness) {
/* 432 */     addSurfaceBiome(biomes, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[2]), weirdness, 0.0F, Biomes.STONY_SHORE);
/* 433 */     addSurfaceBiome(biomes, Climate.Parameter.span(this.temperatures[1], this.temperatures[2]), this.FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, Biomes.SWAMP);
/* 434 */     addSurfaceBiome(biomes, Climate.Parameter.span(this.temperatures[3], this.temperatures[4]), this.FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, Biomes.MANGROVE_SWAMP);
/*     */     
/* 436 */     for (int temperatureIndex = 0; temperatureIndex < this.temperatures.length; temperatureIndex++) {
/* 437 */       Climate.Parameter temperature = this.temperatures[temperatureIndex];
/* 438 */       for (int humidityIndex = 0; humidityIndex < this.humidities.length; humidityIndex++) {
/* 439 */         Climate.Parameter humidity = this.humidities[humidityIndex];
/*     */         
/* 441 */         ResourceKey<Biome> middleBiome = pickMiddleBiome(temperatureIndex, humidityIndex, weirdness);
/* 442 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
/* 443 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(temperatureIndex, humidityIndex, weirdness);
/* 444 */         ResourceKey<Biome> beachBiome = pickBeachBiome(temperatureIndex, humidityIndex);
/* 445 */         ResourceKey<Biome> middleBiomeOrWindsweptSavanna = maybePickWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, middleBiome);
/* 446 */         ResourceKey<Biome> shatteredCoastBiome = pickShatteredCoastBiome(temperatureIndex, humidityIndex, weirdness);
/*     */         
/* 448 */         addSurfaceBiome(biomes, temperature, humidity, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
/* 449 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
/*     */         
/* 451 */         addSurfaceBiome(biomes, temperature, humidity, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, middleBiome);
/* 452 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
/*     */         
/* 454 */         addSurfaceBiome(biomes, temperature, humidity, this.coastContinentalness, Climate.Parameter.span(this.erosions[3], this.erosions[4]), weirdness, 0.0F, beachBiome);
/*     */         
/* 456 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
/*     */         
/* 458 */         addSurfaceBiome(biomes, temperature, humidity, this.coastContinentalness, this.erosions[5], weirdness, 0.0F, shatteredCoastBiome);
/* 459 */         addSurfaceBiome(biomes, temperature, humidity, this.nearInlandContinentalness, this.erosions[5], weirdness, 0.0F, middleBiomeOrWindsweptSavanna);
/* 460 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, middleBiome);
/*     */         
/* 462 */         addSurfaceBiome(biomes, temperature, humidity, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, beachBiome);
/*     */ 
/*     */         
/* 465 */         if (temperatureIndex == 0) {
/* 466 */           addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, middleBiome);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addValleys(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, Climate.Parameter weirdness) {
/* 477 */     addSurfaceBiome(biomes, this.FROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, (weirdness.max() < 0L) ? Biomes.STONY_SHORE : Biomes.FROZEN_RIVER);
/* 478 */     addSurfaceBiome(biomes, this.UNFROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, (weirdness.max() < 0L) ? Biomes.STONY_SHORE : Biomes.RIVER);
/*     */     
/* 480 */     addSurfaceBiome(biomes, this.FROZEN_RANGE, this.FULL_RANGE, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, Biomes.FROZEN_RIVER);
/* 481 */     addSurfaceBiome(biomes, this.UNFROZEN_RANGE, this.FULL_RANGE, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, Biomes.RIVER);
/*     */     
/* 483 */     addSurfaceBiome(biomes, this.FROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[5]), weirdness, 0.0F, Biomes.FROZEN_RIVER);
/* 484 */     addSurfaceBiome(biomes, this.UNFROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[5]), weirdness, 0.0F, Biomes.RIVER);
/*     */     
/* 486 */     addSurfaceBiome(biomes, this.FROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, Biomes.FROZEN_RIVER);
/* 487 */     addSurfaceBiome(biomes, this.UNFROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, Biomes.RIVER);
/*     */     
/* 489 */     addSurfaceBiome(biomes, Climate.Parameter.span(this.temperatures[1], this.temperatures[2]), this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, Biomes.SWAMP);
/* 490 */     addSurfaceBiome(biomes, Climate.Parameter.span(this.temperatures[3], this.temperatures[4]), this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, Biomes.MANGROVE_SWAMP);
/* 491 */     addSurfaceBiome(biomes, this.FROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, Biomes.FROZEN_RIVER);
/*     */     
/* 493 */     for (int temperatureIndex = 0; temperatureIndex < this.temperatures.length; temperatureIndex++) {
/* 494 */       Climate.Parameter temperature = this.temperatures[temperatureIndex];
/* 495 */       for (int humidityIndex = 0; humidityIndex < this.humidities.length; humidityIndex++) {
/* 496 */         Climate.Parameter humidity = this.humidities[humidityIndex];
/* 497 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
/*     */         
/* 499 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void addUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes) {
/* 506 */     addUndergroundBiome(biomes, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(0.8F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.DRIPSTONE_CAVES);
/*     */     
/* 508 */     addUndergroundBiome(biomes, this.FULL_RANGE, Climate.Parameter.span(0.7F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.LUSH_CAVES);
/*     */     
/* 510 */     addBottomBiome(biomes, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.erosions[0], this.erosions[1]), this.FULL_RANGE, 0.0F, Biomes.DEEP_DARK);
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickMiddleBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
/* 514 */     if (weirdness.max() < 0L) {
/* 515 */       return this.MIDDLE_BIOMES[temperatureIndex][humidityIndex];
/*     */     }
/* 517 */     ResourceKey<Biome> variant = this.MIDDLE_BIOMES_VARIANT[temperatureIndex][humidityIndex];
/* 518 */     return (variant == null) ? this.MIDDLE_BIOMES[temperatureIndex][humidityIndex] : variant;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 523 */   private ResourceKey<Biome> pickMiddleBiomeOrBadlandsIfHot(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) { return (temperatureIndex == 4) ? pickBadlandsBiome(humidityIndex, weirdness) : pickMiddleBiome(temperatureIndex, humidityIndex, weirdness); }
/*     */ 
/*     */ 
/*     */   
/* 527 */   private ResourceKey<Biome> pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) { return (temperatureIndex == 0) ? pickSlopeBiome(temperatureIndex, humidityIndex, weirdness) : pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness); }
/*     */ 
/*     */   
/*     */   private ResourceKey<Biome> maybePickWindsweptSavannaBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness, ResourceKey<Biome> underlyingBiome) {
/* 531 */     if (temperatureIndex > 1 && humidityIndex < 4 && weirdness.max() >= 0L) {
/* 532 */       return Biomes.WINDSWEPT_SAVANNA;
/*     */     }
/* 534 */     return underlyingBiome;
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickShatteredCoastBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
/* 538 */     ResourceKey<Biome> beachOrMiddleBiome = (weirdness.max() >= 0L) ? pickMiddleBiome(temperatureIndex, humidityIndex, weirdness) : pickBeachBiome(temperatureIndex, humidityIndex);
/* 539 */     return maybePickWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, beachOrMiddleBiome);
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickBeachBiome(int temperatureIndex, int humidityIndex) {
/* 543 */     if (temperatureIndex == 0) {
/* 544 */       return Biomes.SNOWY_BEACH;
/*     */     }
/* 546 */     if (temperatureIndex == 4)
/*     */     {
/* 548 */       return Biomes.DESERT;
/*     */     }
/* 550 */     return Biomes.BEACH;
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickBadlandsBiome(int humidityIndex, Climate.Parameter weirdness) {
/* 554 */     if (humidityIndex < 2)
/* 555 */       return (weirdness.max() < 0L) ? Biomes.BADLANDS : Biomes.ERODED_BADLANDS; 
/* 556 */     if (humidityIndex < 3) {
/* 557 */       return Biomes.BADLANDS;
/*     */     }
/* 559 */     return Biomes.WOODED_BADLANDS;
/*     */   }
/*     */ 
/*     */   
/*     */   private ResourceKey<Biome> pickPlateauBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
/* 564 */     if (weirdness.max() >= 0L) {
/* 565 */       ResourceKey<Biome> variant = this.PLATEAU_BIOMES_VARIANT[temperatureIndex][humidityIndex];
/* 566 */       if (variant != null) {
/* 567 */         return variant;
/*     */       }
/*     */     } 
/* 570 */     return this.PLATEAU_BIOMES[temperatureIndex][humidityIndex];
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickPeakBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
/* 574 */     if (temperatureIndex <= 2)
/*     */     {
/* 576 */       return (weirdness.max() < 0L) ? Biomes.JAGGED_PEAKS : Biomes.FROZEN_PEAKS;
/*     */     }
/* 578 */     if (temperatureIndex == 3) {
/* 579 */       return Biomes.STONY_PEAKS;
/*     */     }
/* 581 */     return pickBadlandsBiome(humidityIndex, weirdness);
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickSlopeBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
/* 585 */     if (temperatureIndex >= 3) {
/* 586 */       return pickPlateauBiome(temperatureIndex, humidityIndex, weirdness);
/*     */     }
/* 588 */     if (humidityIndex <= 1) {
/* 589 */       return Biomes.SNOWY_SLOPES;
/*     */     }
/* 591 */     return Biomes.GROVE;
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickShatteredBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
/* 595 */     ResourceKey<Biome> biome = this.SHATTERED_BIOMES[temperatureIndex][humidityIndex];
/* 596 */     return (biome == null) ? pickMiddleBiome(temperatureIndex, humidityIndex, weirdness) : biome;
/*     */   }
/*     */ 
/*     */   
/*     */   private void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> second) {
/* 601 */     biomes.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(0.0F), weirdness, offset), second));
/*     */     
/* 603 */     biomes.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(1.0F), weirdness, offset), second));
/*     */   }
/*     */ 
/*     */   
/* 607 */   private void addUndergroundBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) { biomes.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.span(0.2F, 0.9F), weirdness, offset), biome)); }
/*     */ 
/*     */ 
/*     */   
/* 611 */   private void addBottomBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) { biomes.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(1.1F), weirdness, offset), biome)); }
/*     */ 
/*     */ 
/*     */   
/* 615 */   public static boolean isDeepDarkRegion(DensityFunction erosion, DensityFunction depth, DensityFunction.FunctionContext context) { return (erosion.compute(context) < -0.22499999403953552D && depth.compute(context) > 0.8999999761581421D); }
/*     */ 
/*     */   
/*     */   public static String getDebugStringForPeaksAndValleys(double peaksAndValleys) {
/* 619 */     if (peaksAndValleys < NoiseRouterData.peaksAndValleys(0.05F))
/* 620 */       return "Valley"; 
/* 621 */     if (peaksAndValleys < NoiseRouterData.peaksAndValleys(0.26666668F))
/* 622 */       return "Low"; 
/* 623 */     if (peaksAndValleys < NoiseRouterData.peaksAndValleys(0.4F))
/* 624 */       return "Mid"; 
/* 625 */     if (peaksAndValleys < NoiseRouterData.peaksAndValleys(0.56666666F)) {
/* 626 */       return "High";
/*     */     }
/* 628 */     return "Peak";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDebugStringForContinentalness(double continentalness) {
/* 633 */     double continentalnessQuantized = Climate.quantizeCoord((float)continentalness);
/* 634 */     if (continentalnessQuantized < this.mushroomFieldsContinentalness.max())
/* 635 */       return "Mushroom fields"; 
/* 636 */     if (continentalnessQuantized < this.deepOceanContinentalness.max())
/* 637 */       return "Deep ocean"; 
/* 638 */     if (continentalnessQuantized < this.oceanContinentalness.max())
/* 639 */       return "Ocean"; 
/* 640 */     if (continentalnessQuantized < this.coastContinentalness.max())
/* 641 */       return "Coast"; 
/* 642 */     if (continentalnessQuantized < this.nearInlandContinentalness.max())
/* 643 */       return "Near inland"; 
/* 644 */     if (continentalnessQuantized < this.midInlandContinentalness.max()) {
/* 645 */       return "Mid inland";
/*     */     }
/* 647 */     return "Far inland";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 652 */   public String getDebugStringForErosion(double erosion) { return getDebugStringForNoiseValue(erosion, this.erosions); }
/*     */ 
/*     */ 
/*     */   
/* 656 */   public String getDebugStringForTemperature(double temperature) { return getDebugStringForNoiseValue(temperature, this.temperatures); }
/*     */ 
/*     */ 
/*     */   
/* 660 */   public String getDebugStringForHumidity(double humidity) { return getDebugStringForNoiseValue(humidity, this.humidities); }
/*     */ 
/*     */   
/*     */   private static String getDebugStringForNoiseValue(double noiseValue, Parameter[] array) {
/* 664 */     double noiseValueQuantized = Climate.quantizeCoord((float)noiseValue);
/* 665 */     for (int i = 0; i < array.length; i++) {
/* 666 */       if (noiseValueQuantized < array[i].max()) {
/* 667 */         return "" + i;
/*     */       }
/*     */     } 
/* 670 */     return "?";
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/* 675 */   public Climate.Parameter[] getTemperatureThresholds() { return this.temperatures; }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/* 680 */   public Climate.Parameter[] getHumidityThresholds() { return this.humidities; }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/* 685 */   public Climate.Parameter[] getErosionThresholds() { return this.erosions; }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/* 690 */   public Climate.Parameter[] getContinentalnessThresholds() { return new Climate.Parameter[] { this.mushroomFieldsContinentalness, this.deepOceanContinentalness, this.oceanContinentalness, this.coastContinentalness, this.nearInlandContinentalness, this.midInlandContinentalness, this.farInlandContinentalness }; }
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
/*     */   @VisibleForDebug
/*     */   public Climate.Parameter[] getPeaksAndValleysThresholds() {
/* 703 */     return new Climate.Parameter[] {
/* 704 */         Climate.Parameter.span(-2.0F, NoiseRouterData.peaksAndValleys(0.05F)), 
/* 705 */         Climate.Parameter.span(NoiseRouterData.peaksAndValleys(0.05F), NoiseRouterData.peaksAndValleys(0.26666668F)), 
/* 706 */         Climate.Parameter.span(NoiseRouterData.peaksAndValleys(0.26666668F), NoiseRouterData.peaksAndValleys(0.4F)), 
/* 707 */         Climate.Parameter.span(NoiseRouterData.peaksAndValleys(0.4F), NoiseRouterData.peaksAndValleys(0.56666666F)), 
/* 708 */         Climate.Parameter.span(NoiseRouterData.peaksAndValleys(0.56666666F), 2.0F)
/*     */       };
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public Climate.Parameter[] getWeirdnessThresholds() {
/* 714 */     return new Climate.Parameter[] {
/* 715 */         Climate.Parameter.span(-2.0F, 0.0F), 
/* 716 */         Climate.Parameter.span(0.0F, 2.0F)
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\OverworldBiomeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */