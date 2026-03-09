/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.levelgen.Noises;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NoiseData
/*     */ {
/*     */   @Deprecated
/*  12 */   public static final NormalNoise.NoiseParameters DEFAULT_SHIFT = new NormalNoise.NoiseParameters(-3, 1.0D, new double[] { 1.0D, 1.0D, 0.0D });
/*     */   
/*     */   public static void bootstrap(BootstrapContext<NormalNoise.NoiseParameters> context) {
/*  15 */     registerBiomeNoises(context, 0, Noises.TEMPERATURE, Noises.VEGETATION, Noises.CONTINENTALNESS, Noises.EROSION);
/*  16 */     registerBiomeNoises(context, -2, Noises.TEMPERATURE_LARGE, Noises.VEGETATION_LARGE, Noises.CONTINENTALNESS_LARGE, Noises.EROSION_LARGE);
/*     */     
/*  18 */     register(context, Noises.RIDGE, -7, 1.0D, new double[] { 2.0D, 1.0D, 0.0D, 0.0D, 0.0D });
/*  19 */     context.register(Noises.SHIFT, DEFAULT_SHIFT);
/*     */     
/*  21 */     register(context, Noises.AQUIFER_BARRIER, -3, 1.0D, new double[0]);
/*  22 */     register(context, Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS, -7, 1.0D, new double[0]);
/*  23 */     register(context, Noises.AQUIFER_LAVA, -1, 1.0D, new double[0]);
/*  24 */     register(context, Noises.AQUIFER_FLUID_LEVEL_SPREAD, -5, 1.0D, new double[0]);
/*     */     
/*  26 */     register(context, Noises.PILLAR, -7, 1.0D, new double[] { 1.0D });
/*  27 */     register(context, Noises.PILLAR_RARENESS, -8, 1.0D, new double[0]);
/*  28 */     register(context, Noises.PILLAR_THICKNESS, -8, 1.0D, new double[0]);
/*     */     
/*  30 */     register(context, Noises.SPAGHETTI_2D, -7, 1.0D, new double[0]);
/*  31 */     register(context, Noises.SPAGHETTI_2D_ELEVATION, -8, 1.0D, new double[0]);
/*  32 */     register(context, Noises.SPAGHETTI_2D_MODULATOR, -11, 1.0D, new double[0]);
/*  33 */     register(context, Noises.SPAGHETTI_2D_THICKNESS, -11, 1.0D, new double[0]);
/*     */     
/*  35 */     register(context, Noises.SPAGHETTI_3D_1, -7, 1.0D, new double[0]);
/*  36 */     register(context, Noises.SPAGHETTI_3D_2, -7, 1.0D, new double[0]);
/*  37 */     register(context, Noises.SPAGHETTI_3D_RARITY, -11, 1.0D, new double[0]);
/*  38 */     register(context, Noises.SPAGHETTI_3D_THICKNESS, -8, 1.0D, new double[0]);
/*     */     
/*  40 */     register(context, Noises.SPAGHETTI_ROUGHNESS, -5, 1.0D, new double[0]);
/*  41 */     register(context, Noises.SPAGHETTI_ROUGHNESS_MODULATOR, -8, 1.0D, new double[0]);
/*     */     
/*  43 */     register(context, Noises.CAVE_ENTRANCE, -7, 0.4D, new double[] { 0.5D, 1.0D });
/*  44 */     register(context, Noises.CAVE_LAYER, -8, 1.0D, new double[0]);
/*     */     
/*  46 */     register(context, Noises.CAVE_CHEESE, -8, 0.5D, new double[] { 1.0D, 2.0D, 1.0D, 2.0D, 1.0D, 0.0D, 2.0D, 0.0D });
/*     */     
/*  48 */     register(context, Noises.ORE_VEININESS, -8, 1.0D, new double[0]);
/*  49 */     register(context, Noises.ORE_VEIN_A, -7, 1.0D, new double[0]);
/*  50 */     register(context, Noises.ORE_VEIN_B, -7, 1.0D, new double[0]);
/*  51 */     register(context, Noises.ORE_GAP, -5, 1.0D, new double[0]);
/*     */     
/*  53 */     register(context, Noises.NOODLE, -8, 1.0D, new double[0]);
/*  54 */     register(context, Noises.NOODLE_THICKNESS, -8, 1.0D, new double[0]);
/*  55 */     register(context, Noises.NOODLE_RIDGE_A, -7, 1.0D, new double[0]);
/*  56 */     register(context, Noises.NOODLE_RIDGE_B, -7, 1.0D, new double[0]);
/*     */     
/*  58 */     register(context, Noises.JAGGED, -16, 1.0D, new double[] { 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     register(context, Noises.SURFACE, -6, 1.0D, new double[] { 1.0D, 1.0D });
/*  66 */     register(context, Noises.SURFACE_SECONDARY, -6, 1.0D, new double[] { 1.0D, 0.0D, 1.0D });
/*     */     
/*  68 */     register(context, Noises.CLAY_BANDS_OFFSET, -8, 1.0D, new double[0]);
/*  69 */     register(context, Noises.BADLANDS_PILLAR, -2, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  70 */     register(context, Noises.BADLANDS_PILLAR_ROOF, -8, 1.0D, new double[0]);
/*  71 */     register(context, Noises.BADLANDS_SURFACE, -6, 1.0D, new double[] { 1.0D, 1.0D });
/*     */     
/*  73 */     register(context, Noises.ICEBERG_PILLAR, -6, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  74 */     register(context, Noises.ICEBERG_PILLAR_ROOF, -3, 1.0D, new double[0]);
/*  75 */     register(context, Noises.ICEBERG_SURFACE, -6, 1.0D, new double[] { 1.0D, 1.0D });
/*     */     
/*  77 */     register(context, Noises.SWAMP, -2, 1.0D, new double[0]);
/*     */     
/*  79 */     register(context, Noises.CALCITE, -9, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  80 */     register(context, Noises.GRAVEL, -8, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  81 */     register(context, Noises.POWDER_SNOW, -6, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  82 */     register(context, Noises.PACKED_ICE, -7, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  83 */     register(context, Noises.ICE, -4, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*     */     
/*  85 */     register(context, Noises.SOUL_SAND_LAYER, -8, 1.0D, new double[] { 1.0D, 1.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.013333333333333334D });
/*  86 */     register(context, Noises.GRAVEL_LAYER, -8, 1.0D, new double[] { 1.0D, 1.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.013333333333333334D });
/*  87 */     register(context, Noises.PATCH, -5, 1.0D, new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 0.013333333333333334D });
/*  88 */     register(context, Noises.NETHERRACK, -3, 1.0D, new double[] { 0.0D, 0.0D, 0.35D });
/*  89 */     register(context, Noises.NETHER_WART, -3, 1.0D, new double[] { 0.0D, 0.0D, 0.9D });
/*  90 */     register(context, Noises.NETHER_STATE_SELECTOR, -4, 1.0D, new double[0]);
/*     */   }
/*     */   
/*     */   private static void registerBiomeNoises(BootstrapContext<NormalNoise.NoiseParameters> context, int octaveOffset, ResourceKey<NormalNoise.NoiseParameters> temperature, ResourceKey<NormalNoise.NoiseParameters> vegetation, ResourceKey<NormalNoise.NoiseParameters> continentalness, ResourceKey<NormalNoise.NoiseParameters> erosion) {
/*  94 */     register(context, temperature, -10 + octaveOffset, 1.5D, new double[] { 0.0D, 1.0D, 0.0D, 0.0D, 0.0D });
/*  95 */     register(context, vegetation, -8 + octaveOffset, 1.0D, new double[] { 1.0D, 0.0D, 0.0D, 0.0D, 0.0D });
/*  96 */     register(context, continentalness, -9 + octaveOffset, 1.0D, new double[] { 1.0D, 2.0D, 2.0D, 2.0D, 1.0D, 1.0D, 1.0D, 1.0D });
/*  97 */     register(context, erosion, -9 + octaveOffset, 1.0D, new double[] { 1.0D, 0.0D, 1.0D, 1.0D });
/*     */   }
/*     */ 
/*     */   
/* 101 */   private static void register(BootstrapContext<NormalNoise.NoiseParameters> context, ResourceKey<NormalNoise.NoiseParameters> key, int firstOctave, double firstAmplitude, double... amplitudes) { context.register(key, new NormalNoise.NoiseParameters(firstOctave, firstAmplitude, amplitudes)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\NoiseData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */