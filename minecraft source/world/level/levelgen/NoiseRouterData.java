/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.TerrainProvider;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.synth.BlendedNoise;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
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
/*     */ public class NoiseRouterData
/*     */ {
/*     */   public static final float GLOBAL_OFFSET = -0.50375F;
/*     */   private static final float ORE_THICKNESS = 0.08F;
/*     */   private static final double VEININESS_FREQUENCY = 1.5D;
/*     */   private static final double NOODLE_SPACING_AND_STRAIGHTNESS = 1.5D;
/*     */   private static final double SURFACE_DENSITY_THRESHOLD = 1.5625D;
/*     */   private static final double CHEESE_NOISE_TARGET = -0.703125D;
/*     */   public static final double NOISE_ZERO = 0.390625D;
/*     */   public static final int ISLAND_CHUNK_DISTANCE = 64;
/*     */   public static final long ISLAND_CHUNK_DISTANCE_SQR = 4096L;
/*     */   private static final int DENSITY_Y_ANCHOR_BOTTOM = -64;
/*     */   private static final int DENSITY_Y_ANCHOR_TOP = 320;
/*     */   private static final double DENSITY_Y_BOTTOM = 1.5D;
/*     */   private static final double DENSITY_Y_TOP = -1.5D;
/*     */   private static final int OVERWORLD_BOTTOM_SLIDE_HEIGHT = 24;
/*     */   private static final double BASE_DENSITY_MULTIPLIER = 4.0D;
/*  93 */   private static final DensityFunction BLENDING_FACTOR = DensityFunctions.constant(10.0D);
/*  94 */   private static final DensityFunction BLENDING_JAGGEDNESS = DensityFunctions.zero();
/*     */   
/*  96 */   private static final ResourceKey<DensityFunction> ZERO = createKey("zero");
/*  97 */   private static final ResourceKey<DensityFunction> Y = createKey("y");
/*  98 */   private static final ResourceKey<DensityFunction> SHIFT_X = createKey("shift_x");
/*  99 */   private static final ResourceKey<DensityFunction> SHIFT_Z = createKey("shift_z");
/*     */   
/* 101 */   private static final ResourceKey<DensityFunction> BASE_3D_NOISE_OVERWORLD = createKey("overworld/base_3d_noise");
/* 102 */   private static final ResourceKey<DensityFunction> BASE_3D_NOISE_NETHER = createKey("nether/base_3d_noise");
/* 103 */   private static final ResourceKey<DensityFunction> BASE_3D_NOISE_END = createKey("end/base_3d_noise");
/*     */   
/* 105 */   public static final ResourceKey<DensityFunction> CONTINENTS = createKey("overworld/continents");
/* 106 */   public static final ResourceKey<DensityFunction> EROSION = createKey("overworld/erosion");
/* 107 */   public static final ResourceKey<DensityFunction> RIDGES = createKey("overworld/ridges");
/* 108 */   public static final ResourceKey<DensityFunction> RIDGES_FOLDED = createKey("overworld/ridges_folded");
/*     */   
/* 110 */   public static final ResourceKey<DensityFunction> OFFSET = createKey("overworld/offset");
/* 111 */   public static final ResourceKey<DensityFunction> FACTOR = createKey("overworld/factor");
/* 112 */   public static final ResourceKey<DensityFunction> JAGGEDNESS = createKey("overworld/jaggedness");
/* 113 */   public static final ResourceKey<DensityFunction> DEPTH = createKey("overworld/depth");
/* 114 */   private static final ResourceKey<DensityFunction> SLOPED_CHEESE = createKey("overworld/sloped_cheese");
/*     */   
/* 116 */   public static final ResourceKey<DensityFunction> CONTINENTS_LARGE = createKey("overworld_large_biomes/continents");
/* 117 */   public static final ResourceKey<DensityFunction> EROSION_LARGE = createKey("overworld_large_biomes/erosion");
/*     */   
/* 119 */   private static final ResourceKey<DensityFunction> OFFSET_LARGE = createKey("overworld_large_biomes/offset");
/* 120 */   private static final ResourceKey<DensityFunction> FACTOR_LARGE = createKey("overworld_large_biomes/factor");
/* 121 */   private static final ResourceKey<DensityFunction> JAGGEDNESS_LARGE = createKey("overworld_large_biomes/jaggedness");
/* 122 */   private static final ResourceKey<DensityFunction> DEPTH_LARGE = createKey("overworld_large_biomes/depth");
/* 123 */   private static final ResourceKey<DensityFunction> SLOPED_CHEESE_LARGE = createKey("overworld_large_biomes/sloped_cheese");
/*     */   
/* 125 */   private static final ResourceKey<DensityFunction> OFFSET_AMPLIFIED = createKey("overworld_amplified/offset");
/* 126 */   private static final ResourceKey<DensityFunction> FACTOR_AMPLIFIED = createKey("overworld_amplified/factor");
/* 127 */   private static final ResourceKey<DensityFunction> JAGGEDNESS_AMPLIFIED = createKey("overworld_amplified/jaggedness");
/* 128 */   private static final ResourceKey<DensityFunction> DEPTH_AMPLIFIED = createKey("overworld_amplified/depth");
/* 129 */   private static final ResourceKey<DensityFunction> SLOPED_CHEESE_AMPLIFIED = createKey("overworld_amplified/sloped_cheese");
/*     */   
/* 131 */   private static final ResourceKey<DensityFunction> SLOPED_CHEESE_END = createKey("end/sloped_cheese");
/*     */   
/* 133 */   private static final ResourceKey<DensityFunction> SPAGHETTI_ROUGHNESS_FUNCTION = createKey("overworld/caves/spaghetti_roughness_function");
/* 134 */   private static final ResourceKey<DensityFunction> ENTRANCES = createKey("overworld/caves/entrances");
/* 135 */   private static final ResourceKey<DensityFunction> NOODLE = createKey("overworld/caves/noodle");
/* 136 */   private static final ResourceKey<DensityFunction> PILLARS = createKey("overworld/caves/pillars");
/* 137 */   private static final ResourceKey<DensityFunction> SPAGHETTI_2D_THICKNESS_MODULATOR = createKey("overworld/caves/spaghetti_2d_thickness_modulator");
/* 138 */   private static final ResourceKey<DensityFunction> SPAGHETTI_2D = createKey("overworld/caves/spaghetti_2d");
/*     */ 
/*     */   
/* 141 */   private static ResourceKey<DensityFunction> createKey(String name) { return ResourceKey.create(Registries.DENSITY_FUNCTION, Identifier.withDefaultNamespace(name)); }
/*     */ 
/*     */   
/*     */   public static Holder<? extends DensityFunction> bootstrap(BootstrapContext<DensityFunction> context) {
/* 145 */     HolderGetter<NormalNoise.NoiseParameters> noises = context.lookup(Registries.NOISE);
/* 146 */     HolderGetter<DensityFunction> functions = context.lookup(Registries.DENSITY_FUNCTION);
/*     */     
/* 148 */     context.register(ZERO, DensityFunctions.zero());
/*     */     
/* 150 */     int belowBottom = DimensionType.MIN_Y * 2;
/* 151 */     int aboveTop = DimensionType.MAX_Y * 2;
/* 152 */     context.register(Y, DensityFunctions.yClampedGradient(belowBottom, aboveTop, belowBottom, aboveTop));
/*     */     
/* 154 */     DensityFunction shiftX = registerAndWrap(context, SHIFT_X, DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftA(noises.getOrThrow(Noises.SHIFT)))));
/* 155 */     DensityFunction shiftZ = registerAndWrap(context, SHIFT_Z, DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftB(noises.getOrThrow(Noises.SHIFT)))));
/*     */     
/* 157 */     context.register(BASE_3D_NOISE_OVERWORLD, BlendedNoise.createUnseeded(0.25D, 0.125D, 80.0D, 160.0D, 8.0D));
/* 158 */     context.register(BASE_3D_NOISE_NETHER, BlendedNoise.createUnseeded(0.25D, 0.375D, 80.0D, 60.0D, 8.0D));
/* 159 */     context.register(BASE_3D_NOISE_END, BlendedNoise.createUnseeded(0.25D, 0.25D, 80.0D, 160.0D, 4.0D));
/*     */     
/* 161 */     Holder.Reference reference1 = context.register(CONTINENTS, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, noises.getOrThrow(Noises.CONTINENTALNESS))));
/* 162 */     Holder.Reference reference2 = context.register(EROSION, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, noises.getOrThrow(Noises.EROSION))));
/* 163 */     DensityFunction ridge = registerAndWrap(context, RIDGES, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, noises.getOrThrow(Noises.RIDGE))));
/* 164 */     context.register(RIDGES_FOLDED, peaksAndValleys(ridge));
/*     */     
/* 166 */     DensityFunction jaggedNoise = DensityFunctions.noise(noises.getOrThrow(Noises.JAGGED), 1500.0D, 0.0D);
/*     */     
/* 168 */     registerTerrainNoises(context, functions, jaggedNoise, reference1, reference2, OFFSET, FACTOR, JAGGEDNESS, DEPTH, SLOPED_CHEESE, false);
/*     */     
/* 170 */     Holder.Reference reference3 = context.register(CONTINENTS_LARGE, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, noises.getOrThrow(Noises.CONTINENTALNESS_LARGE))));
/* 171 */     Holder.Reference reference4 = context.register(EROSION_LARGE, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, noises.getOrThrow(Noises.EROSION_LARGE))));
/*     */     
/* 173 */     registerTerrainNoises(context, functions, jaggedNoise, reference3, reference4, OFFSET_LARGE, FACTOR_LARGE, JAGGEDNESS_LARGE, DEPTH_LARGE, SLOPED_CHEESE_LARGE, false);
/* 174 */     registerTerrainNoises(context, functions, jaggedNoise, reference1, reference2, OFFSET_AMPLIFIED, FACTOR_AMPLIFIED, JAGGEDNESS_AMPLIFIED, DEPTH_AMPLIFIED, SLOPED_CHEESE_AMPLIFIED, true);
/*     */     
/* 176 */     context.register(SLOPED_CHEESE_END, DensityFunctions.add(DensityFunctions.endIslands(0L), getFunction(functions, BASE_3D_NOISE_END)));
/*     */     
/* 178 */     context.register(SPAGHETTI_ROUGHNESS_FUNCTION, spaghettiRoughnessFunction(noises));
/* 179 */     context.register(SPAGHETTI_2D_THICKNESS_MODULATOR, DensityFunctions.cacheOnce(DensityFunctions.mappedNoise(noises.getOrThrow(Noises.SPAGHETTI_2D_THICKNESS), 2.0D, 1.0D, -0.6D, -1.3D)));
/* 180 */     context.register(SPAGHETTI_2D, spaghetti2D(functions, noises));
/* 181 */     context.register(ENTRANCES, entrances(functions, noises));
/* 182 */     context.register(NOODLE, noodle(functions, noises));
/* 183 */     return context.register(PILLARS, pillars(noises));
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
/*     */   private static void registerTerrainNoises(BootstrapContext<DensityFunction> context, HolderGetter<DensityFunction> functions, DensityFunction jaggedNoise, Holder<DensityFunction> continentsFunction, Holder<DensityFunction> erosionFunction, ResourceKey<DensityFunction> offsetName, ResourceKey<DensityFunction> factorName, ResourceKey<DensityFunction> jaggednessName, ResourceKey<DensityFunction> depthName, ResourceKey<DensityFunction> slopedCheeseName, boolean amplified) {
/* 199 */     DensityFunctions.Spline.Coordinate continents = new DensityFunctions.Spline.Coordinate(continentsFunction);
/* 200 */     DensityFunctions.Spline.Coordinate erosion = new DensityFunctions.Spline.Coordinate(erosionFunction);
/* 201 */     DensityFunctions.Spline.Coordinate weirdness = new DensityFunctions.Spline.Coordinate(functions.getOrThrow(RIDGES));
/* 202 */     DensityFunctions.Spline.Coordinate ridges = new DensityFunctions.Spline.Coordinate(functions.getOrThrow(RIDGES_FOLDED));
/*     */ 
/*     */     
/* 205 */     DensityFunction offset = registerAndWrap(context, offsetName, splineWithBlending(
/* 206 */           DensityFunctions.add(DensityFunctions.constant(-0.5037500262260437D), DensityFunctions.spline(TerrainProvider.overworldOffset(continents, erosion, ridges, amplified))), 
/* 207 */           DensityFunctions.blendOffset()));
/*     */     
/* 209 */     DensityFunction factor = registerAndWrap(context, factorName, splineWithBlending(
/* 210 */           DensityFunctions.spline(TerrainProvider.overworldFactor(continents, erosion, weirdness, ridges, amplified)), BLENDING_FACTOR));
/*     */ 
/*     */     
/* 213 */     DensityFunction depth = registerAndWrap(context, depthName, offsetToDepth(offset));
/* 214 */     DensityFunction unscaledJaggedness = registerAndWrap(context, jaggednessName, splineWithBlending(
/* 215 */           DensityFunctions.spline(TerrainProvider.overworldJaggedness(continents, erosion, weirdness, ridges, amplified)), BLENDING_JAGGEDNESS));
/*     */ 
/*     */ 
/*     */     
/* 219 */     DensityFunction jaggedness = DensityFunctions.mul(unscaledJaggedness, jaggedNoise.halfNegative());
/* 220 */     DensityFunction initialDensity = noiseGradientDensity(factor, DensityFunctions.add(depth, jaggedness));
/* 221 */     context.register(slopedCheeseName, DensityFunctions.add(initialDensity, getFunction(functions, BASE_3D_NOISE_OVERWORLD)));
/*     */   }
/*     */ 
/*     */   
/* 225 */   private static DensityFunction offsetToDepth(DensityFunction offset) { return DensityFunctions.add(DensityFunctions.yClampedGradient(-64, 320, 1.5D, -1.5D), offset); }
/*     */ 
/*     */ 
/*     */   
/* 229 */   private static DensityFunction registerAndWrap(BootstrapContext<DensityFunction> context, ResourceKey<DensityFunction> name, DensityFunction value) { return new DensityFunctions.HolderHolder(context.register(name, value)); }
/*     */ 
/*     */ 
/*     */   
/* 233 */   private static DensityFunction getFunction(HolderGetter<DensityFunction> functions, ResourceKey<DensityFunction> name) { return new DensityFunctions.HolderHolder(functions.getOrThrow(name)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 243 */   private static DensityFunction peaksAndValleys(DensityFunction weirdness) { return DensityFunctions.mul(DensityFunctions.add(DensityFunctions.add(weirdness.abs(), DensityFunctions.constant(-0.6666666666666666D)).abs(), DensityFunctions.constant(-0.3333333333333333D)), DensityFunctions.constant(-3.0D)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 250 */   public static float peaksAndValleys(float weirdness) { return -(Math.abs(Math.abs(weirdness) - 0.6666667F) - 0.33333334F) * 3.0F; }
/*     */ 
/*     */   
/*     */   private static DensityFunction spaghettiRoughnessFunction(HolderGetter<NormalNoise.NoiseParameters> noises) {
/* 254 */     DensityFunction spaghettiRoughnessNoise = DensityFunctions.noise(noises.getOrThrow(Noises.SPAGHETTI_ROUGHNESS));
/*     */     
/* 256 */     DensityFunction spaghettiRoughnessModulator = DensityFunctions.mappedNoise(noises.getOrThrow(Noises.SPAGHETTI_ROUGHNESS_MODULATOR), 0.0D, -0.1D);
/*     */     
/* 258 */     return DensityFunctions.cacheOnce(DensityFunctions.mul(spaghettiRoughnessModulator, 
/*     */           
/* 260 */           DensityFunctions.add(spaghettiRoughnessNoise.abs(), DensityFunctions.constant(-0.4D))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static DensityFunction entrances(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises) {
/* 265 */     DensityFunction spaghetti3DRarityModulator = DensityFunctions.cacheOnce(DensityFunctions.noise(noises.getOrThrow(Noises.SPAGHETTI_3D_RARITY), 2.0D, 1.0D));
/*     */ 
/*     */     
/* 268 */     DensityFunction spaghetti3DThicknessModulator = DensityFunctions.mappedNoise(noises.getOrThrow(Noises.SPAGHETTI_3D_THICKNESS), -0.065D, -0.088D);
/*     */     
/* 270 */     DensityFunction spaghetti3DCave1 = DensityFunctions.weirdScaledSampler(spaghetti3DRarityModulator, noises.getOrThrow(Noises.SPAGHETTI_3D_1), DensityFunctions.WeirdScaledSampler.RarityValueMapper.TYPE1);
/* 271 */     DensityFunction spaghetti3DCave2 = DensityFunctions.weirdScaledSampler(spaghetti3DRarityModulator, noises.getOrThrow(Noises.SPAGHETTI_3D_2), DensityFunctions.WeirdScaledSampler.RarityValueMapper.TYPE1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 277 */     DensityFunction spaghetti3DFunction = DensityFunctions.add(DensityFunctions.max(spaghetti3DCave1, spaghetti3DCave2), spaghetti3DThicknessModulator).clamp(-1.0D, 1.0D);
/*     */     
/* 279 */     DensityFunction spaghettiRoughnessFunction = getFunction(functions, SPAGHETTI_ROUGHNESS_FUNCTION);
/*     */     
/* 281 */     DensityFunction bigEntranceNoiseSource = DensityFunctions.noise(noises.getOrThrow(Noises.CAVE_ENTRANCE), 0.75D, 0.5D);
/*     */     
/* 283 */     DensityFunction bigEntrancesFunction = DensityFunctions.add(
/* 284 */         DensityFunctions.add(bigEntranceNoiseSource, DensityFunctions.constant(0.37D)), 
/*     */ 
/*     */         
/* 287 */         DensityFunctions.yClampedGradient(-10, 30, 0.3D, 0.0D));
/*     */ 
/*     */ 
/*     */     
/* 291 */     return DensityFunctions.cacheOnce(DensityFunctions.min(bigEntrancesFunction, DensityFunctions.add(spaghettiRoughnessFunction, spaghetti3DFunction)));
/*     */   }
/*     */   
/*     */   private static DensityFunction noodle(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises) {
/* 295 */     DensityFunction y = getFunction(functions, Y);
/*     */     
/* 297 */     int minBlockY = -64;
/*     */ 
/*     */     
/* 300 */     int noodleMinY = -60;
/* 301 */     int noodleMaxY = 320;
/*     */     
/* 303 */     DensityFunction noodleToggle = yLimitedInterpolatable(y, DensityFunctions.noise(noises.getOrThrow(Noises.NOODLE), 1.0D, 1.0D), -60, 320, -1);
/*     */ 
/*     */ 
/*     */     
/* 307 */     DensityFunction noodleThickness = yLimitedInterpolatable(y, DensityFunctions.mappedNoise(noises.getOrThrow(Noises.NOODLE_THICKNESS), 1.0D, 1.0D, -0.05D, -0.1D), -60, 320, 0);
/*     */     
/* 309 */     double noodleRidgeFrequency = 2.6666666666666665D;
/* 310 */     DensityFunction noodleRidgeA = yLimitedInterpolatable(y, DensityFunctions.noise(noises.getOrThrow(Noises.NOODLE_RIDGE_A), 2.6666666666666665D, 2.6666666666666665D), -60, 320, 0);
/* 311 */     DensityFunction noodleRidgeB = yLimitedInterpolatable(y, DensityFunctions.noise(noises.getOrThrow(Noises.NOODLE_RIDGE_B), 2.6666666666666665D, 2.6666666666666665D), -60, 320, 0);
/*     */     
/* 313 */     DensityFunction noodleRidged = DensityFunctions.mul(
/* 314 */         DensityFunctions.constant(1.5D), 
/* 315 */         DensityFunctions.max(noodleRidgeA.abs(), noodleRidgeB.abs()));
/*     */ 
/*     */     
/* 318 */     return DensityFunctions.rangeChoice(noodleToggle, -1000000.0D, 0.0D, 
/*     */ 
/*     */ 
/*     */         
/* 322 */         DensityFunctions.constant(64.0D), 
/* 323 */         DensityFunctions.add(noodleThickness, noodleRidged));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static DensityFunction pillars(HolderGetter<NormalNoise.NoiseParameters> noises) {
/* 331 */     double xzFrequency = 25.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 336 */     double yFrequency = 0.3D;
/*     */     
/* 338 */     DensityFunction pillarNoiseSource = DensityFunctions.noise(noises.getOrThrow(Noises.PILLAR), 25.0D, 0.3D);
/*     */ 
/*     */ 
/*     */     
/* 342 */     DensityFunction pillarRarenessModulator = DensityFunctions.mappedNoise(noises.getOrThrow(Noises.PILLAR_RARENESS), 0.0D, -2.0D);
/*     */ 
/*     */ 
/*     */     
/* 346 */     DensityFunction pillarThicknessModulator = DensityFunctions.mappedNoise(noises.getOrThrow(Noises.PILLAR_THICKNESS), 0.0D, 1.1D);
/*     */     
/* 348 */     DensityFunction pillarsWithRareness = DensityFunctions.add(
/* 349 */         DensityFunctions.mul(pillarNoiseSource, DensityFunctions.constant(2.0D)), pillarRarenessModulator);
/*     */ 
/*     */ 
/*     */     
/* 353 */     return DensityFunctions.cacheOnce(DensityFunctions.mul(pillarsWithRareness, pillarThicknessModulator
/*     */ 
/*     */           
/* 356 */           .cube()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static DensityFunction spaghetti2D(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises) {
/* 361 */     DensityFunction spaghetti2DRarityModulator = DensityFunctions.noise(noises.getOrThrow(Noises.SPAGHETTI_2D_MODULATOR), 2.0D, 1.0D);
/* 362 */     DensityFunction spaghetti2DCave = DensityFunctions.weirdScaledSampler(spaghetti2DRarityModulator, noises.getOrThrow(Noises.SPAGHETTI_2D), DensityFunctions.WeirdScaledSampler.RarityValueMapper.TYPE2);
/*     */     
/* 364 */     DensityFunction spaghetti2DElevationModulator = DensityFunctions.mappedNoise(noises.getOrThrow(Noises.SPAGHETTI_2D_ELEVATION), 0.0D, Math.floorDiv(-64, 8), 8.0D);
/*     */     
/* 366 */     DensityFunction spaghetti2DThicknessModulator = getFunction(functions, SPAGHETTI_2D_THICKNESS_MODULATOR);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 371 */     DensityFunction slopedSpaghetti = DensityFunctions.add(spaghetti2DElevationModulator, DensityFunctions.yClampedGradient(-64, 320, 8.0D, -40.0D)).abs();
/*     */     
/* 373 */     DensityFunction layerRidged = DensityFunctions.add(slopedSpaghetti, spaghetti2DThicknessModulator).cube();
/*     */     
/* 375 */     double ridgeOffset = 0.083D;
/* 376 */     DensityFunction caveNoise = DensityFunctions.add(spaghetti2DCave, DensityFunctions.mul(DensityFunctions.constant(0.083D), spaghetti2DThicknessModulator));
/*     */ 
/*     */     
/* 379 */     return DensityFunctions.max(caveNoise, layerRidged).clamp(-1.0D, 1.0D);
/*     */   }
/*     */   
/*     */   private static DensityFunction underground(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises, DensityFunction slopedCheese) {
/* 383 */     DensityFunction spaghetti2DFunction = getFunction(functions, SPAGHETTI_2D);
/*     */     
/* 385 */     DensityFunction spaghettiRoughnessFunction = getFunction(functions, SPAGHETTI_ROUGHNESS_FUNCTION);
/*     */     
/* 387 */     DensityFunction layerNoiseSource = DensityFunctions.noise(noises.getOrThrow(Noises.CAVE_LAYER), 8.0D);
/*     */     
/* 389 */     DensityFunction layerizedCavernsFunction = DensityFunctions.mul(DensityFunctions.constant(4.0D), layerNoiseSource.square());
/*     */     
/* 391 */     DensityFunction cheese = DensityFunctions.noise(noises.getOrThrow(Noises.CAVE_CHEESE), 0.6666666666666666D);
/*     */     
/* 393 */     DensityFunction solidifedCheeseWithTopSlide = DensityFunctions.add(
/*     */         
/* 395 */         DensityFunctions.add(DensityFunctions.constant(0.27D), cheese).clamp(-1.0D, 1.0D), 
/*     */         
/* 397 */         DensityFunctions.add(DensityFunctions.constant(1.5D), DensityFunctions.mul(DensityFunctions.constant(-0.64D), slopedCheese)).clamp(0.0D, 0.5D));
/*     */ 
/*     */     
/* 400 */     DensityFunction baseCaveDensity = DensityFunctions.add(layerizedCavernsFunction, solidifedCheeseWithTopSlide);
/*     */     
/* 402 */     DensityFunction undergroundSubtractions = DensityFunctions.min(DensityFunctions.min(baseCaveDensity, getFunction(functions, ENTRANCES)), DensityFunctions.add(spaghetti2DFunction, spaghettiRoughnessFunction));
/*     */     
/* 404 */     DensityFunction pillarsWithoutCutoff = getFunction(functions, PILLARS);
/*     */     
/* 406 */     DensityFunction pillars = DensityFunctions.rangeChoice(pillarsWithoutCutoff, -1000000.0D, 0.03D, DensityFunctions.constant(-1000000.0D), pillarsWithoutCutoff);
/*     */     
/* 408 */     return DensityFunctions.max(undergroundSubtractions, pillars);
/*     */   }
/*     */ 
/*     */   
/*     */   private static DensityFunction postProcess(DensityFunction slide) {
/* 413 */     DensityFunction blended = DensityFunctions.blendDensity(slide);
/* 414 */     return DensityFunctions.mul(DensityFunctions.interpolated(blended), DensityFunctions.constant(0.64D)).squeeze();
/*     */   }
/*     */   
/*     */   private static DensityFunction remap(DensityFunction input, double fromMin, double fromMax, double toMin, double toMax) {
/* 418 */     double factor = (toMax - toMin) / (fromMax - fromMin);
/* 419 */     double offset = toMin - fromMin * factor;
/* 420 */     return DensityFunctions.add(DensityFunctions.mul(input, DensityFunctions.constant(factor)), DensityFunctions.constant(offset));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static NoiseRouter overworld(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises, boolean largeBiomes, boolean amplified) {
/* 425 */     DensityFunction barrierNoise = DensityFunctions.noise(noises.getOrThrow(Noises.AQUIFER_BARRIER), 0.5D);
/* 426 */     DensityFunction fluidLevelFloodednessNoise = DensityFunctions.noise(noises.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67D);
/* 427 */     DensityFunction fluidLevelSpreadNoise = DensityFunctions.noise(noises.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143D);
/* 428 */     DensityFunction lavaNoise = DensityFunctions.noise(noises.getOrThrow(Noises.AQUIFER_LAVA));
/*     */     
/* 430 */     DensityFunction shiftX = getFunction(functions, SHIFT_X);
/* 431 */     DensityFunction shiftZ = getFunction(functions, SHIFT_Z);
/*     */ 
/*     */     
/* 434 */     DensityFunction temperature = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, noises.getOrThrow(largeBiomes ? Noises.TEMPERATURE_LARGE : Noises.TEMPERATURE));
/* 435 */     DensityFunction vegetation = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, noises.getOrThrow(largeBiomes ? Noises.VEGETATION_LARGE : Noises.VEGETATION));
/*     */     
/* 437 */     DensityFunction offset = getFunction(functions, largeBiomes ? OFFSET_LARGE : (amplified ? OFFSET_AMPLIFIED : OFFSET));
/* 438 */     DensityFunction factor = getFunction(functions, largeBiomes ? FACTOR_LARGE : (amplified ? FACTOR_AMPLIFIED : FACTOR));
/* 439 */     DensityFunction depth = getFunction(functions, largeBiomes ? DEPTH_LARGE : (amplified ? DEPTH_AMPLIFIED : DEPTH));
/*     */     
/* 441 */     DensityFunction preliminarySurfaceLevel = preliminarySurfaceLevel(offset, factor, amplified);
/*     */     
/* 443 */     DensityFunction slopedCheese = getFunction(functions, largeBiomes ? SLOPED_CHEESE_LARGE : (amplified ? SLOPED_CHEESE_AMPLIFIED : SLOPED_CHEESE));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 448 */     DensityFunction surfaceWithEntrances = DensityFunctions.min(slopedCheese, DensityFunctions.mul(DensityFunctions.constant(5.0D), getFunction(functions, ENTRANCES)));
/* 449 */     DensityFunction caves = DensityFunctions.rangeChoice(slopedCheese, -1000000.0D, 1.5625D, surfaceWithEntrances, underground(functions, noises, slopedCheese));
/*     */     
/* 451 */     DensityFunction fullNoise = DensityFunctions.min(postProcess(slideOverworld(amplified, caves)), getFunction(functions, NOODLE));
/*     */     
/* 453 */     DensityFunction y = getFunction(functions, Y);
/*     */     
/* 455 */     int veinMinY = Stream.of(OreVeinifier.VeinType.values()).mapToInt(t -> t.minY).min().orElse(-DimensionType.MIN_Y * 2);
/* 456 */     int veinMaxY = Stream.of(OreVeinifier.VeinType.values()).mapToInt(t -> t.maxY).max().orElse(-DimensionType.MIN_Y * 2);
/*     */ 
/*     */     
/* 459 */     DensityFunction veinToggle = yLimitedInterpolatable(y, DensityFunctions.noise(noises.getOrThrow(Noises.ORE_VEININESS), 1.5D, 1.5D), veinMinY, veinMaxY, 0);
/*     */     
/* 461 */     float oreRidgeFrequency = 4.0F;
/*     */ 
/*     */     
/* 464 */     DensityFunction veinA = yLimitedInterpolatable(y, DensityFunctions.noise(noises.getOrThrow(Noises.ORE_VEIN_A), 4.0D, 4.0D), veinMinY, veinMaxY, 0).abs();
/* 465 */     DensityFunction veinB = yLimitedInterpolatable(y, DensityFunctions.noise(noises.getOrThrow(Noises.ORE_VEIN_B), 4.0D, 4.0D), veinMinY, veinMaxY, 0).abs();
/*     */     
/* 467 */     DensityFunction veinRidged = DensityFunctions.add(DensityFunctions.constant(-0.07999999821186066D), DensityFunctions.max(veinA, veinB));
/*     */     
/* 469 */     DensityFunction veinGap = DensityFunctions.noise(noises.getOrThrow(Noises.ORE_GAP));
/*     */ 
/*     */ 
/*     */     
/* 473 */     return new NoiseRouter(barrierNoise, fluidLevelFloodednessNoise, fluidLevelSpreadNoise, lavaNoise, temperature, vegetation, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 480 */         getFunction(functions, largeBiomes ? CONTINENTS_LARGE : CONTINENTS), 
/* 481 */         getFunction(functions, largeBiomes ? EROSION_LARGE : EROSION), depth, 
/*     */         
/* 483 */         getFunction(functions, RIDGES), preliminarySurfaceLevel, fullNoise, veinToggle, veinRidged, veinGap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static NoiseRouter noNewCaves(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises, DensityFunction slide) {
/* 493 */     DensityFunction shiftX = getFunction(functions, SHIFT_X);
/* 494 */     DensityFunction shiftZ = getFunction(functions, SHIFT_Z);
/*     */     
/* 496 */     DensityFunction temperature = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, noises.getOrThrow(Noises.TEMPERATURE));
/* 497 */     DensityFunction vegetation = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, noises.getOrThrow(Noises.VEGETATION));
/*     */     
/* 499 */     DensityFunction fullNoise = postProcess(slide);
/*     */ 
/*     */ 
/*     */     
/* 503 */     return new NoiseRouter(
/* 504 */         DensityFunctions.zero(), 
/* 505 */         DensityFunctions.zero(), 
/* 506 */         DensityFunctions.zero(), 
/* 507 */         DensityFunctions.zero(), temperature, vegetation, 
/*     */ 
/*     */         
/* 510 */         DensityFunctions.zero(), 
/* 511 */         DensityFunctions.zero(), 
/* 512 */         DensityFunctions.zero(), 
/* 513 */         DensityFunctions.zero(), 
/* 514 */         DensityFunctions.zero(), fullNoise, 
/*     */         
/* 516 */         DensityFunctions.zero(), 
/* 517 */         DensityFunctions.zero(), 
/* 518 */         DensityFunctions.zero());
/*     */   }
/*     */ 
/*     */   
/*     */   private static DensityFunction slideOverworld(boolean isAmplified, DensityFunction caves) {
/* 523 */     return slide(caves, -64, 384, 
/*     */ 
/*     */ 
/*     */         
/* 527 */         isAmplified ? 16 : 80, 
/* 528 */         isAmplified ? 0 : 64, -0.078125D, 0, 24, 
/*     */ 
/*     */ 
/*     */         
/* 532 */         isAmplified ? 0.4D : 0.1171875D);
/*     */   }
/*     */ 
/*     */   
/*     */   private static DensityFunction slideNetherLike(HolderGetter<DensityFunction> functions, int minY, int height) {
/* 537 */     return slide(
/* 538 */         getFunction(functions, BASE_3D_NOISE_NETHER), minY, height, 24, 0, 0.9375D, -8, 24, 2.5D);
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
/* 551 */   private static DensityFunction slideEndLike(DensityFunction caves, int minY, int height) { return slide(caves, minY, height, 72, -184, -23.4375D, 4, 32, -0.234375D); }
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
/* 565 */   protected static NoiseRouter nether(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises) { return noNewCaves(functions, noises, slideNetherLike(functions, 0, 128)); }
/*     */ 
/*     */ 
/*     */   
/* 569 */   protected static NoiseRouter caves(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises) { return noNewCaves(functions, noises, slideNetherLike(functions, -64, 192)); }
/*     */ 
/*     */ 
/*     */   
/* 573 */   protected static NoiseRouter floatingIslands(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises) { return noNewCaves(functions, noises, slideEndLike(getFunction(functions, BASE_3D_NOISE_END), 0, 256)); }
/*     */ 
/*     */ 
/*     */   
/* 577 */   private static DensityFunction slideEnd(DensityFunction caves) { return slideEndLike(caves, 0, 128); }
/*     */ 
/*     */   
/*     */   protected static NoiseRouter end(HolderGetter<DensityFunction> functions) {
/* 581 */     DensityFunction islands = DensityFunctions.cache2d(DensityFunctions.endIslands(0L));
/* 582 */     DensityFunction fullNoise = postProcess(slideEnd(getFunction(functions, SLOPED_CHEESE_END)));
/*     */ 
/*     */     
/* 585 */     return new NoiseRouter(
/* 586 */         DensityFunctions.zero(), 
/* 587 */         DensityFunctions.zero(), 
/* 588 */         DensityFunctions.zero(), 
/* 589 */         DensityFunctions.zero(), 
/* 590 */         DensityFunctions.zero(), 
/* 591 */         DensityFunctions.zero(), 
/* 592 */         DensityFunctions.zero(), islands, 
/*     */         
/* 594 */         DensityFunctions.zero(), 
/* 595 */         DensityFunctions.zero(), 
/* 596 */         DensityFunctions.zero(), fullNoise, 
/*     */         
/* 598 */         DensityFunctions.zero(), 
/* 599 */         DensityFunctions.zero(), 
/* 600 */         DensityFunctions.zero());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static NoiseRouter none() {
/* 606 */     return new NoiseRouter(
/* 607 */         DensityFunctions.zero(), 
/* 608 */         DensityFunctions.zero(), 
/* 609 */         DensityFunctions.zero(), 
/* 610 */         DensityFunctions.zero(), 
/* 611 */         DensityFunctions.zero(), 
/* 612 */         DensityFunctions.zero(), 
/* 613 */         DensityFunctions.zero(), 
/* 614 */         DensityFunctions.zero(), 
/* 615 */         DensityFunctions.zero(), 
/* 616 */         DensityFunctions.zero(), 
/* 617 */         DensityFunctions.zero(), 
/* 618 */         DensityFunctions.zero(), 
/* 619 */         DensityFunctions.zero(), 
/* 620 */         DensityFunctions.zero(), 
/* 621 */         DensityFunctions.zero());
/*     */   }
/*     */ 
/*     */   
/*     */   private static DensityFunction splineWithBlending(DensityFunction spline, DensityFunction blendingTarget) {
/* 626 */     DensityFunction blendedSpline = DensityFunctions.lerp(DensityFunctions.blendAlpha(), blendingTarget, spline);
/* 627 */     return DensityFunctions.flatCache(DensityFunctions.cache2d(blendedSpline));
/*     */   }
/*     */   
/*     */   private static DensityFunction noiseGradientDensity(DensityFunction factor, DensityFunction depthWithJaggedness) {
/* 631 */     DensityFunction gradientUnscaled = DensityFunctions.mul(depthWithJaggedness, factor);
/*     */ 
/*     */     
/* 634 */     return DensityFunctions.mul(DensityFunctions.constant(4.0D), gradientUnscaled.quarterNegative());
/*     */   }
/*     */   
/*     */   private static DensityFunction preliminarySurfaceLevel(DensityFunction offset, DensityFunction factor, boolean amplified) {
/* 638 */     DensityFunction cachedFactor = DensityFunctions.cache2d(factor);
/* 639 */     DensityFunction cachedOffset = DensityFunctions.cache2d(offset);
/* 640 */     DensityFunction upperBound = remap(
/* 641 */         DensityFunctions.add(
/* 642 */           DensityFunctions.mul(
/* 643 */             DensityFunctions.constant(0.2734375D), cachedFactor
/* 644 */             .invert()), 
/*     */           
/* 646 */           DensityFunctions.mul(DensityFunctions.constant(-1.0D), cachedOffset)), 1.5D, -1.5D, -64.0D, 320.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 651 */     upperBound = upperBound.clamp(-40.0D, 320.0D);
/* 652 */     DensityFunction density = DensityFunctions.add(
/* 653 */         slideOverworld(amplified, 
/*     */           
/* 655 */           DensityFunctions.add(
/* 656 */             noiseGradientDensity(cachedFactor, offsetToDepth(cachedOffset)), 
/* 657 */             DensityFunctions.constant(-0.703125D))
/* 658 */           .clamp(-64.0D, 64.0D)), 
/* 659 */         DensityFunctions.constant(-0.390625D));
/*     */     
/* 661 */     return DensityFunctions.findTopSurface(density, upperBound, -64, NoiseSettings.OVERWORLD_NOISE_SETTINGS.getCellHeight());
/*     */   }
/*     */ 
/*     */   
/* 665 */   private static DensityFunction yLimitedInterpolatable(DensityFunction y, DensityFunction whenInRange, int minYInclusive, int maxYInclusive, int whenOutOfRange) { return DensityFunctions.interpolated(DensityFunctions.rangeChoice(y, minYInclusive, (maxYInclusive + 1), whenInRange, DensityFunctions.constant(whenOutOfRange))); }
/*     */ 
/*     */   
/*     */   private static DensityFunction slide(DensityFunction caves, int minY, int height, int topStartY, int topEndY, double topTarget, int bottomStartY, int bottomEndY, double bottomTarget) {
/* 669 */     noiseValue = caves;
/*     */     
/* 671 */     DensityFunction topFactor = DensityFunctions.yClampedGradient(minY + height - topStartY, minY + height - topEndY, 1.0D, 0.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 677 */     noiseValue = DensityFunctions.lerp(topFactor, topTarget, noiseValue);
/*     */     
/* 679 */     DensityFunction bottomFactor = DensityFunctions.yClampedGradient(minY + bottomStartY, minY + bottomEndY, 0.0D, 1.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 685 */     return DensityFunctions.lerp(bottomFactor, bottomTarget, noiseValue);
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
/*     */   protected static final class QuantizedSpaghettiRarity
/*     */   {
/*     */     protected static double getSphaghettiRarity2D(double rarityFactor) {
/* 699 */       if (rarityFactor < -0.75D)
/* 700 */         return 0.5D; 
/* 701 */       if (rarityFactor < -0.5D)
/* 702 */         return 0.75D; 
/* 703 */       if (rarityFactor < 0.5D)
/* 704 */         return 1.0D; 
/* 705 */       if (rarityFactor < 0.75D) {
/* 706 */         return 2.0D;
/*     */       }
/* 708 */       return 3.0D;
/*     */     }
/*     */ 
/*     */     
/*     */     protected static double getSpaghettiRarity3D(double rarityFactor) {
/* 713 */       if (rarityFactor < -0.5D)
/* 714 */         return 0.75D; 
/* 715 */       if (rarityFactor < 0.0D)
/* 716 */         return 1.0D; 
/* 717 */       if (rarityFactor < 0.5D) {
/* 718 */         return 1.5D;
/*     */       }
/* 720 */       return 2.0D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\NoiseRouterData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */