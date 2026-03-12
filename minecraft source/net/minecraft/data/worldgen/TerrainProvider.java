/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import net.minecraft.util.BoundedFloatFunction;
/*     */ import net.minecraft.util.CubicSpline;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.levelgen.NoiseRouterData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TerrainProvider
/*     */ {
/*     */   private static final float DEEP_OCEAN_CONTINENTALNESS = -0.51F;
/*     */   private static final float OCEAN_CONTINENTALNESS = -0.4F;
/*     */   private static final float PLAINS_CONTINENTALNESS = 0.1F;
/*     */   private static final float BEACH_CONTINENTALNESS = -0.15F;
/*  21 */   private static final BoundedFloatFunction<Float> NO_TRANSFORM = BoundedFloatFunction.IDENTITY;
/*     */   
/*  23 */   private static final BoundedFloatFunction<Float> AMPLIFIED_OFFSET = BoundedFloatFunction.createUnlimited(offset -> (offset < 0.0F) ? offset : (offset * 2.0F));
/*  24 */   private static final BoundedFloatFunction<Float> AMPLIFIED_FACTOR = BoundedFloatFunction.createUnlimited(factor -> 1.25F - 6.25F / (factor + 5.0F));
/*  25 */   private static final BoundedFloatFunction<Float> AMPLIFIED_JAGGEDNESS = BoundedFloatFunction.createUnlimited(jaggedness -> jaggedness * 2.0F);
/*     */   
/*     */   public static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> overworldOffset(I continents, I erosion, I ridges, boolean amplified) {
/*  28 */     BoundedFloatFunction<Float> offsetTransformer = amplified ? AMPLIFIED_OFFSET : NO_TRANSFORM;
/*     */ 
/*     */     
/*  31 */     CubicSpline<C, I> beachSpline = buildErosionOffsetSpline(erosion, ridges, -0.15F, 0.0F, 0.0F, 0.1F, 0.0F, -0.03F, false, false, offsetTransformer);
/*  32 */     CubicSpline<C, I> lowSpline = buildErosionOffsetSpline(erosion, ridges, -0.1F, 0.03F, 0.1F, 0.1F, 0.01F, -0.03F, false, false, offsetTransformer);
/*  33 */     CubicSpline<C, I> midSpline = buildErosionOffsetSpline(erosion, ridges, -0.1F, 0.03F, 0.1F, 0.7F, 0.01F, -0.03F, true, true, offsetTransformer);
/*  34 */     CubicSpline<C, I> highSpline = buildErosionOffsetSpline(erosion, ridges, -0.05F, 0.03F, 0.1F, 1.0F, 0.01F, 0.01F, true, true, offsetTransformer);
/*     */     
/*  36 */     return CubicSpline.builder(continents, offsetTransformer)
/*  37 */       .addPoint(-1.1F, 0.044F)
/*  38 */       .addPoint(-1.02F, -0.2222F)
/*  39 */       .addPoint(-0.51F, -0.2222F)
/*  40 */       .addPoint(-0.44F, -0.12F)
/*  41 */       .addPoint(-0.18F, -0.12F)
/*  42 */       .addPoint(-0.16F, beachSpline)
/*  43 */       .addPoint(-0.15F, beachSpline)
/*  44 */       .addPoint(-0.1F, lowSpline)
/*  45 */       .addPoint(0.25F, midSpline)
/*  46 */       .addPoint(1.0F, highSpline)
/*  47 */       .build();
/*     */   }
/*     */   
/*     */   public static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> overworldFactor(I continents, I erosion, I weirdness, I ridges, boolean amplified) {
/*  51 */     BoundedFloatFunction<Float> factorTransformer = amplified ? AMPLIFIED_FACTOR : NO_TRANSFORM;
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
/*  73 */     return CubicSpline.builder(continents, NO_TRANSFORM)
/*     */ 
/*     */       
/*  76 */       .addPoint(-0.19F, 3.95F)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  82 */       .addPoint(-0.15F, getErosionFactor(erosion, weirdness, ridges, 6.25F, true, NO_TRANSFORM))
/*     */ 
/*     */       
/*  85 */       .addPoint(-0.1F, getErosionFactor(erosion, weirdness, ridges, 5.47F, true, factorTransformer))
/*  86 */       .addPoint(0.03F, getErosionFactor(erosion, weirdness, ridges, 5.08F, true, factorTransformer))
/*  87 */       .addPoint(0.06F, getErosionFactor(erosion, weirdness, ridges, 4.69F, false, factorTransformer))
/*  88 */       .build();
/*     */   }
/*     */   
/*     */   public static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> overworldJaggedness(I continents, I erosion, I weirdness, I ridges, boolean amplified) {
/*  92 */     BoundedFloatFunction<Float> jaggednessTransformer = amplified ? AMPLIFIED_JAGGEDNESS : NO_TRANSFORM;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     float farInlandMiddle = 0.65F;
/* 101 */     return CubicSpline.builder(continents, jaggednessTransformer)
/* 102 */       .addPoint(-0.11F, 0.0F)
/* 103 */       .addPoint(0.03F, buildErosionJaggednessSpline(erosion, weirdness, ridges, 1.0F, 0.5F, 0.0F, 0.0F, jaggednessTransformer))
/* 104 */       .addPoint(0.65F, buildErosionJaggednessSpline(erosion, weirdness, ridges, 1.0F, 1.0F, 1.0F, 0.0F, jaggednessTransformer))
/* 105 */       .build();
/*     */   }
/*     */   
/*     */   private static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> buildErosionJaggednessSpline(I erosion, I weirdness, I ridges, float jaggednessFactorAtPeakRidgeAndErosionIndex0, float jaggednessFactorAtPeakRidgeAndErosionIndex1, float jaggednessFactorAtHighRidgeAndErosionIndex0, float jaggednessFactorAtHighRidgeAndErosionIndex1, BoundedFloatFunction<Float> jaggednessTransformer) {
/* 109 */     float erosionIndex1Middle = -0.5775F;
/*     */     
/* 111 */     CubicSpline<C, I> ridgeJaggednessSplineAtErosion0 = buildRidgeJaggednessSpline(weirdness, ridges, jaggednessFactorAtPeakRidgeAndErosionIndex0, jaggednessFactorAtHighRidgeAndErosionIndex0, jaggednessTransformer);
/* 112 */     CubicSpline<C, I> ridgeJaggednessSplineAtErosion1 = buildRidgeJaggednessSpline(weirdness, ridges, jaggednessFactorAtPeakRidgeAndErosionIndex1, jaggednessFactorAtHighRidgeAndErosionIndex1, jaggednessTransformer);
/*     */     
/* 114 */     return CubicSpline.builder(erosion, jaggednessTransformer)
/* 115 */       .addPoint(-1.0F, ridgeJaggednessSplineAtErosion0)
/* 116 */       .addPoint(-0.78F, ridgeJaggednessSplineAtErosion1)
/* 117 */       .addPoint(-0.5775F, ridgeJaggednessSplineAtErosion1)
/* 118 */       .addPoint(-0.375F, 0.0F)
/* 119 */       .build();
/*     */   }
/*     */   
/*     */   private static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> buildRidgeJaggednessSpline(I weirdness, I ridges, float jaggednessFactorAtPeakRidge, float jaggednessFactorAtHighRidge, BoundedFloatFunction<Float> jaggednessTransformer) {
/* 123 */     float highSliceStart = NoiseRouterData.peaksAndValleys(0.4F);
/* 124 */     float highSliceEnd = NoiseRouterData.peaksAndValleys(0.56666666F);
/*     */     
/* 126 */     float highSliceMiddle = (highSliceStart + highSliceEnd) / 2.0F;
/*     */     
/* 128 */     CubicSpline.Builder<C, I> ridgeSpline = CubicSpline.builder(ridges, jaggednessTransformer);
/*     */     
/* 130 */     ridgeSpline.addPoint(highSliceStart, 0.0F);
/*     */     
/* 132 */     if (jaggednessFactorAtHighRidge > 0.0F) {
/* 133 */       ridgeSpline.addPoint(highSliceMiddle, buildWeirdnessJaggednessSpline(weirdness, jaggednessFactorAtHighRidge, jaggednessTransformer));
/*     */     } else {
/* 135 */       ridgeSpline.addPoint(highSliceMiddle, 0.0F);
/*     */     } 
/*     */     
/* 138 */     if (jaggednessFactorAtPeakRidge > 0.0F) {
/* 139 */       ridgeSpline.addPoint(1.0F, buildWeirdnessJaggednessSpline(weirdness, jaggednessFactorAtPeakRidge, jaggednessTransformer));
/*     */     } else {
/* 141 */       ridgeSpline.addPoint(1.0F, 0.0F);
/*     */     } 
/* 143 */     return ridgeSpline.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> buildWeirdnessJaggednessSpline(I weirdness, float jaggednessFactor, BoundedFloatFunction<Float> jaggednessTransformer) {
/* 151 */     float maxJaggednessAtNegativeWeirdness = 0.63F * jaggednessFactor;
/* 152 */     float maxJaggednessAtPositiveWeirdness = 0.3F * jaggednessFactor;
/*     */     
/* 154 */     return CubicSpline.builder(weirdness, jaggednessTransformer)
/* 155 */       .addPoint(-0.01F, maxJaggednessAtNegativeWeirdness)
/* 156 */       .addPoint(0.01F, maxJaggednessAtPositiveWeirdness)
/* 157 */       .build();
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
/*     */   private static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> getErosionFactor(I erosion, I weirdness, I ridges, float baseValue, boolean shatteredTerrain, BoundedFloatFunction<Float> factorTransformer) {
/* 169 */     CubicSpline<C, I> baseSpline = CubicSpline.builder(weirdness, factorTransformer).addPoint(-0.2F, 6.3F).addPoint(0.2F, baseValue).build();
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
/* 186 */     CubicSpline.Builder<C, I> erosionPoints = CubicSpline.builder(erosion, factorTransformer).addPoint(-0.6F, baseSpline).addPoint(-0.5F, CubicSpline.builder(weirdness, factorTransformer).addPoint(-0.05F, 6.3F).addPoint(0.05F, 2.67F).build()).addPoint(-0.35F, baseSpline).addPoint(-0.25F, baseSpline).addPoint(-0.1F, CubicSpline.builder(weirdness, factorTransformer).addPoint(-0.05F, 2.67F).addPoint(0.05F, 6.3F).build()).addPoint(0.03F, baseSpline);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     if (shatteredTerrain) {
/*     */ 
/*     */ 
/*     */       
/* 198 */       CubicSpline<C, I> weirdnessShattered = CubicSpline.builder(weirdness, factorTransformer).addPoint(0.0F, baseValue).addPoint(0.1F, 0.625F).build();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 203 */       CubicSpline<C, I> ridgesShattered = CubicSpline.builder(ridges, factorTransformer).addPoint(-0.9F, baseValue).addPoint(-0.69F, weirdnessShattered).build();
/*     */       
/* 205 */       erosionPoints
/* 206 */         .addPoint(0.35F, baseValue)
/* 207 */         .addPoint(0.45F, ridgesShattered)
/* 208 */         .addPoint(0.55F, ridgesShattered)
/* 209 */         .addPoint(0.62F, baseValue);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 214 */       CubicSpline<C, I> extremeHillsTerrainFromMidSliceAndUp = CubicSpline.builder(ridges, factorTransformer).addPoint(-0.7F, baseSpline).addPoint(-0.15F, 1.37F).build();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 219 */       CubicSpline<C, I> extra3dNoiseOnPeaksOnly = CubicSpline.builder(ridges, factorTransformer).addPoint(0.45F, baseSpline).addPoint(0.7F, 1.56F).build();
/*     */       
/* 221 */       erosionPoints
/* 222 */         .addPoint(0.05F, extra3dNoiseOnPeaksOnly)
/* 223 */         .addPoint(0.4F, extra3dNoiseOnPeaksOnly)
/* 224 */         .addPoint(0.45F, extremeHillsTerrainFromMidSliceAndUp)
/* 225 */         .addPoint(0.55F, extremeHillsTerrainFromMidSliceAndUp)
/* 226 */         .addPoint(0.58F, baseValue);
/*     */     } 
/* 228 */     return erosionPoints.build();
/*     */   }
/*     */ 
/*     */   
/* 232 */   private static float calculateSlope(float y1, float y2, float x1, float x2) { return (y2 - y1) / (x2 - x1); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> buildMountainRidgeSplineWithPoints(I ridges, float modulation, boolean saddle, BoundedFloatFunction<Float> offsetTransformer) {
/* 237 */     CubicSpline.Builder<C, I> build = CubicSpline.builder(ridges, offsetTransformer);
/*     */     
/* 239 */     float allowRiversBelow = -0.7F;
/* 240 */     float minPoint = -1.0F;
/* 241 */     float minPointContinentalness = mountainContinentalness(-1.0F, modulation, -0.7F);
/* 242 */     float maxPoint = 1.0F;
/* 243 */     float maxPointContinentalness = mountainContinentalness(1.0F, modulation, -0.7F);
/*     */     
/* 245 */     float ridgeZeroPoint = calculateMountainRidgeZeroContinentalnessPoint(modulation);
/*     */     
/* 247 */     float afterRiverPoint = -0.65F;
/*     */     
/* 249 */     if (-0.65F < ridgeZeroPoint && ridgeZeroPoint < 1.0F) {
/*     */ 
/*     */ 
/*     */       
/* 253 */       float afterRiverThresholdContinentalness = mountainContinentalness(-0.65F, modulation, -0.7F);
/* 254 */       float beforeRiverPoint = -0.75F;
/* 255 */       float beforeRiverThresholdContinentalness = mountainContinentalness(-0.75F, modulation, -0.7F);
/*     */ 
/*     */       
/* 258 */       float minPointDerivative = calculateSlope(minPointContinentalness, beforeRiverThresholdContinentalness, -1.0F, -0.75F);
/* 259 */       build.addPoint(-1.0F, minPointContinentalness, minPointDerivative);
/*     */ 
/*     */       
/* 262 */       build.addPoint(-0.75F, beforeRiverThresholdContinentalness);
/* 263 */       build.addPoint(-0.65F, afterRiverThresholdContinentalness);
/*     */ 
/*     */       
/* 266 */       float ridgeZeroPointContinentalness = mountainContinentalness(ridgeZeroPoint, modulation, -0.7F);
/* 267 */       float maxPointDerivative = calculateSlope(ridgeZeroPointContinentalness, maxPointContinentalness, ridgeZeroPoint, 1.0F);
/* 268 */       float smallOffset = 0.01F;
/* 269 */       build.addPoint(ridgeZeroPoint - 0.01F, ridgeZeroPointContinentalness);
/* 270 */       build.addPoint(ridgeZeroPoint, ridgeZeroPointContinentalness, maxPointDerivative);
/* 271 */       build.addPoint(1.0F, maxPointContinentalness, maxPointDerivative);
/*     */     } else {
/* 273 */       float simpleDerivative = calculateSlope(minPointContinentalness, maxPointContinentalness, -1.0F, 1.0F);
/*     */       
/* 275 */       if (saddle) {
/*     */         
/* 277 */         build.addPoint(-1.0F, Math.max(0.2F, minPointContinentalness));
/* 278 */         build.addPoint(0.0F, Mth.lerp(0.5F, minPointContinentalness, maxPointContinentalness), simpleDerivative);
/*     */       }
/*     */       else {
/*     */         
/* 282 */         build.addPoint(-1.0F, minPointContinentalness, simpleDerivative);
/*     */       } 
/* 284 */       build.addPoint(1.0F, maxPointContinentalness, simpleDerivative);
/*     */     } 
/*     */     
/* 287 */     return build.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float mountainContinentalness(float ridge, float modulation, float allowRiversBelow) {
/* 295 */     float ridgeOffset = 1.17F;
/* 296 */     float ridgeAmplitude = 0.46082947F;
/* 297 */     float ridgeSlope = 1.0F - (1.0F - modulation) * 0.5F;
/* 298 */     float ridgeIntersect = 0.5F * (1.0F - modulation);
/*     */     
/* 300 */     float adjustedRidgeHeight = (ridge + 1.17F) * 0.46082947F;
/* 301 */     float continentalness = adjustedRidgeHeight * ridgeSlope - ridgeIntersect;
/*     */     
/* 303 */     if (ridge < allowRiversBelow)
/*     */     {
/*     */       
/* 306 */       return Math.max(continentalness, -0.2222F);
/*     */     }
/*     */     
/* 309 */     return Math.max(continentalness, 0.0F);
/*     */   }
/*     */   
/*     */   private static float calculateMountainRidgeZeroContinentalnessPoint(float modulation) {
/* 313 */     float ridgeOffset = 1.17F;
/* 314 */     float ridgeAmplitude = 0.46082947F;
/* 315 */     float ridgeSlope = 1.0F - (1.0F - modulation) * 0.5F;
/* 316 */     float ridgeIntersect = 0.5F * (1.0F - modulation);
/*     */     
/* 318 */     return ridgeIntersect / 0.46082947F * ridgeSlope - 1.17F;
/*     */   }
/*     */   
/*     */   public static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> buildErosionOffsetSpline(I erosion, I ridges, float lowValley, float hill, float tallHill, float mountainFactor, float plain, float swamp, boolean includeExtremeHills, boolean saddle, BoundedFloatFunction<Float> offsetTransformer) {
/* 322 */     float lowPeaks = 0.6F;
/*     */     
/* 324 */     float valleyPlateau = 0.5F;
/* 325 */     float plateau = 0.5F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 335 */     CubicSpline<C, I> veryLowErosionMountains = buildMountainRidgeSplineWithPoints(ridges, Mth.lerp(mountainFactor, 0.6F, 1.5F), saddle, offsetTransformer);
/*     */     
/* 337 */     CubicSpline<C, I> lowErosionMountains = buildMountainRidgeSplineWithPoints(ridges, Mth.lerp(mountainFactor, 0.6F, 1.0F), saddle, offsetTransformer);
/* 338 */     CubicSpline<C, I> mountains = buildMountainRidgeSplineWithPoints(ridges, mountainFactor, saddle, offsetTransformer);
/*     */     
/* 340 */     CubicSpline<C, I> widePlateau = ridgeSpline(ridges, lowValley - 0.15F, 0.5F * mountainFactor, 
/*     */ 
/*     */         
/* 343 */         Mth.lerp(0.5F, 0.5F, 0.5F) * mountainFactor, 0.5F * mountainFactor, 0.6F * mountainFactor, 0.5F, offsetTransformer);
/*     */ 
/*     */ 
/*     */     
/* 347 */     CubicSpline<C, I> narrowPlateau = ridgeSpline(ridges, lowValley, plain * mountainFactor, hill * mountainFactor, 0.5F * mountainFactor, 0.6F * mountainFactor, 0.5F, offsetTransformer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 354 */     CubicSpline<C, I> plains = ridgeSpline(ridges, lowValley, plain, plain, hill, tallHill, 0.5F, offsetTransformer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 361 */     CubicSpline<C, I> plainsFarInland = ridgeSpline(ridges, lowValley, plain, plain, hill, tallHill, 0.5F, offsetTransformer);
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
/* 373 */     CubicSpline<C, I> extremeHills = CubicSpline.builder(ridges, offsetTransformer).addPoint(-1.0F, lowValley).addPoint(-0.4F, plains).addPoint(0.0F, tallHill + 0.07F).build();
/*     */     
/* 375 */     CubicSpline<C, I> swamps = ridgeSpline(ridges, -0.02F, swamp, swamp, hill, tallHill, 0.0F, offsetTransformer);
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
/* 390 */     CubicSpline.Builder<C, I> builder = CubicSpline.builder(erosion, offsetTransformer).addPoint(-0.85F, veryLowErosionMountains).addPoint(-0.7F, lowErosionMountains).addPoint(-0.4F, mountains).addPoint(-0.35F, widePlateau).addPoint(-0.1F, narrowPlateau).addPoint(0.2F, plains);
/*     */     
/* 392 */     if (includeExtremeHills)
/*     */     {
/*     */       
/* 395 */       builder
/* 396 */         .addPoint(0.4F, plainsFarInland)
/* 397 */         .addPoint(0.45F, extremeHills)
/* 398 */         .addPoint(0.55F, extremeHills)
/* 399 */         .addPoint(0.58F, plainsFarInland);
/*     */     }
/* 401 */     builder
/* 402 */       .addPoint(0.7F, swamps);
/*     */     
/* 404 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   private static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> ridgeSpline(I ridges, float valley, float low, float mid, float high, float peaks, float minValleySteepness, BoundedFloatFunction<Float> offsetTransformer) {
/* 409 */     float d1 = Math.max(0.5F * (low - valley), minValleySteepness);
/* 410 */     float d2 = 5.0F * (mid - low);
/* 411 */     return CubicSpline.builder(ridges, offsetTransformer)
/* 412 */       .addPoint(-1.0F, valley, d1)
/* 413 */       .addPoint(-0.4F, low, Math.min(d1, d2))
/* 414 */       .addPoint(0.0F, mid, d2)
/* 415 */       .addPoint(0.4F, high, 2.0F * (high - mid))
/* 416 */       .addPoint(1.0F, peaks, 0.7F * (peaks - high))
/* 417 */       .build();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\TerrainProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */