/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.OverworldBiomeBuilder;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import org.apache.commons.lang3.mutable.MutableDouble;
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
/*     */ public class NoiseBasedAquifer
/*     */   implements Aquifer
/*     */ {
/*     */   private static final int X_RANGE = 10;
/*     */   private static final int Y_RANGE = 9;
/*     */   private static final int Z_RANGE = 10;
/*     */   private static final int X_SEPARATION = 6;
/*     */   private static final int Y_SEPARATION = 3;
/*     */   private static final int Z_SEPARATION = 6;
/*     */   private static final int X_SPACING = 16;
/*     */   private static final int Y_SPACING = 12;
/*     */   private static final int Z_SPACING = 16;
/*     */   private static final int X_SPACING_SHIFT = 4;
/*     */   private static final int Z_SPACING_SHIFT = 4;
/*     */   private static final int MAX_REASONABLE_DISTANCE_TO_AQUIFER_CENTER = 11;
/* 104 */   private static final double FLOWING_UPDATE_SIMULARITY = similarity(
/* 105 */       Mth.square(10), 
/* 106 */       Mth.square(12));
/*     */ 
/*     */   
/*     */   private static final int SAMPLE_OFFSET_X = -5;
/*     */ 
/*     */   
/*     */   private static final int SAMPLE_OFFSET_Y = 1;
/*     */ 
/*     */   
/*     */   private static final int SAMPLE_OFFSET_Z = -5;
/*     */ 
/*     */   
/*     */   private static final int MIN_CELL_SAMPLE_X = 0;
/*     */   
/*     */   private static final int MIN_CELL_SAMPLE_Y = -1;
/*     */   
/*     */   private static final int MIN_CELL_SAMPLE_Z = 0;
/*     */   
/*     */   private static final int MAX_CELL_SAMPLE_X = 1;
/*     */   
/*     */   private static final int MAX_CELL_SAMPLE_Y = 1;
/*     */   
/*     */   private static final int MAX_CELL_SAMPLE_Z = 1;
/*     */   
/*     */   private final NoiseChunk noiseChunk;
/*     */   
/*     */   private final DensityFunction barrierNoise;
/*     */   
/*     */   private final DensityFunction fluidLevelFloodednessNoise;
/*     */   
/*     */   private final DensityFunction fluidLevelSpreadNoise;
/*     */   
/*     */   private final DensityFunction lavaNoise;
/*     */   
/*     */   private final PositionalRandomFactory positionalRandomFactory;
/*     */   
/*     */   private final Aquifer.FluidStatus[] aquiferCache;
/*     */   
/*     */   private final long[] aquiferLocationCache;
/*     */   
/*     */   private final Aquifer.FluidPicker globalFluidPicker;
/*     */   
/*     */   private final DensityFunction erosion;
/*     */   
/*     */   private final DensityFunction depth;
/*     */   
/*     */   private boolean shouldScheduleFluidUpdate;
/*     */   
/*     */   private final int skipSamplingAboveY;
/*     */   
/*     */   private final int minGridX;
/*     */   
/*     */   private final int minGridY;
/*     */   
/*     */   private final int minGridZ;
/*     */   
/*     */   private final int gridSizeX;
/*     */   
/*     */   private final int gridSizeZ;
/*     */   
/*     */   private static final int[][] SURFACE_SAMPLING_OFFSETS_IN_CHUNKS = { 
/* 167 */       { 0, 0 }, { -2, -1 }, { -1, -1 }, { 0, -1 }, { 1, -1 }, { -3, 0 }, { -2, 0 }, { -1, 0 }, { 1, 0 }, { -2, 1 }, { -1, 1 }, { 0, 1 }, { 1, 1 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NoiseBasedAquifer(NoiseChunk noiseChunk, ChunkPos pos, NoiseRouter router, PositionalRandomFactory positionalRandomFactory, int minBlockY, int yBlockSize, Aquifer.FluidPicker globalFluidPicker) {
/* 175 */     this.noiseChunk = noiseChunk;
/* 176 */     this.barrierNoise = router.barrierNoise();
/* 177 */     this.fluidLevelFloodednessNoise = router.fluidLevelFloodednessNoise();
/* 178 */     this.fluidLevelSpreadNoise = router.fluidLevelSpreadNoise();
/* 179 */     this.lavaNoise = router.lavaNoise();
/* 180 */     this.erosion = router.erosion();
/* 181 */     this.depth = router.depth();
/*     */     
/* 183 */     this.positionalRandomFactory = positionalRandomFactory;
/*     */     
/* 185 */     this.minGridX = gridX(pos.getMinBlockX() + -5) + 0;
/* 186 */     this.globalFluidPicker = globalFluidPicker;
/* 187 */     int maxGridX = gridX(pos.getMaxBlockX() + -5) + 1;
/* 188 */     this.gridSizeX = maxGridX - this.minGridX + 1;
/*     */     
/* 190 */     this.minGridY = gridY(minBlockY + 1) + -1;
/* 191 */     int maxGridY = gridY(minBlockY + yBlockSize + 1) + 1;
/* 192 */     int gridSizeY = maxGridY - this.minGridY + 1;
/*     */     
/* 194 */     this.minGridZ = gridZ(pos.getMinBlockZ() + -5) + 0;
/* 195 */     int maxGridZ = gridZ(pos.getMaxBlockZ() + -5) + 1;
/* 196 */     this.gridSizeZ = maxGridZ - this.minGridZ + 1;
/* 197 */     int totalGridSize = this.gridSizeX * gridSizeY * this.gridSizeZ;
/*     */     
/* 199 */     this.aquiferCache = new Aquifer.FluidStatus[totalGridSize];
/*     */     
/* 201 */     this.aquiferLocationCache = new long[totalGridSize];
/* 202 */     Arrays.fill(this.aquiferLocationCache, Float.MAX_VALUE);
/*     */     
/* 204 */     int maxAdjustedSurfaceLevel = adjustSurfaceLevel(noiseChunk.maxPreliminarySurfaceLevel(
/* 205 */           fromGridX(this.minGridX, 0), 
/* 206 */           fromGridZ(this.minGridZ, 0), 
/* 207 */           fromGridX(maxGridX, 9), 
/* 208 */           fromGridZ(maxGridZ, 9)));
/*     */     
/* 210 */     int skipSamplingAboveGridY = gridY(maxAdjustedSurfaceLevel + 12) - -1;
/* 211 */     this.skipSamplingAboveY = fromGridY(skipSamplingAboveGridY, 11) - 1;
/*     */   }
/*     */   
/*     */   private int getIndex(int gridX, int gridY, int gridZ) {
/* 215 */     int x = gridX - this.minGridX;
/* 216 */     int y = gridY - this.minGridY;
/* 217 */     int z = gridZ - this.minGridZ;
/*     */     
/* 219 */     return (y * this.gridSizeZ + z) * this.gridSizeX + x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState computeSubstance(DensityFunction.FunctionContext context, double density) {
/* 227 */     if (density > 0.0D) {
/* 228 */       this.shouldScheduleFluidUpdate = false;
/* 229 */       return null;
/*     */     } 
/*     */     
/* 232 */     int posX = context.blockX();
/* 233 */     int posY = context.blockY();
/* 234 */     int posZ = context.blockZ();
/*     */     
/* 236 */     Aquifer.FluidStatus globalFluid = this.globalFluidPicker.computeFluid(posX, posY, posZ);
/*     */ 
/*     */     
/* 239 */     if (posY > this.skipSamplingAboveY) {
/* 240 */       this.shouldScheduleFluidUpdate = false;
/* 241 */       return globalFluid.at(posY);
/*     */     } 
/*     */     
/* 244 */     if (globalFluid.at(posY).is(Blocks.LAVA)) {
/* 245 */       this.shouldScheduleFluidUpdate = false;
/* 246 */       return SharedConstants.DEBUG_DISABLE_FLUID_GENERATION ? Blocks.AIR.defaultBlockState() : Blocks.LAVA.defaultBlockState();
/*     */     } 
/*     */     
/* 249 */     int xAnchor = gridX(posX + -5);
/* 250 */     int yAnchor = gridY(posY + 1);
/* 251 */     int zAnchor = gridZ(posZ + -5);
/*     */ 
/*     */     
/* 254 */     int distanceSqr1 = Integer.MAX_VALUE;
/* 255 */     int distanceSqr2 = Integer.MAX_VALUE;
/* 256 */     int distanceSqr3 = Integer.MAX_VALUE;
/* 257 */     int distanceSqr4 = Integer.MAX_VALUE;
/*     */     
/* 259 */     int closestIndex1 = 0;
/* 260 */     int closestIndex2 = 0;
/* 261 */     int closestIndex3 = 0;
/* 262 */     int closestIndex4 = 0;
/*     */     
/* 264 */     for (int x1 = 0; x1 <= 1; x1++) {
/* 265 */       for (int y1 = -1; y1 <= 1; y1++) {
/* 266 */         for (int z1 = 0; z1 <= 1; z1++) {
/* 267 */           long location; int spacedGridX = xAnchor + x1;
/* 268 */           int spacedGridY = yAnchor + y1;
/* 269 */           int spacedGridZ = zAnchor + z1;
/*     */           
/* 271 */           int index = getIndex(spacedGridX, spacedGridY, spacedGridZ);
/*     */ 
/*     */           
/* 274 */           long existingLocation = this.aquiferLocationCache[index];
/* 275 */           if (existingLocation != Float.MAX_VALUE) {
/* 276 */             location = existingLocation;
/*     */           } else {
/* 278 */             RandomSource random = this.positionalRandomFactory.at(spacedGridX, spacedGridY, spacedGridZ);
/*     */             
/* 280 */             location = BlockPos.asLong(
/* 281 */                 fromGridX(spacedGridX, random.nextInt(10)), 
/* 282 */                 fromGridY(spacedGridY, random.nextInt(9)), 
/* 283 */                 fromGridZ(spacedGridZ, random.nextInt(10)));
/*     */             
/* 285 */             this.aquiferLocationCache[index] = location;
/*     */           } 
/*     */           
/* 288 */           int dx = BlockPos.getX(location) - posX;
/* 289 */           int dy = BlockPos.getY(location) - posY;
/* 290 */           int dz = BlockPos.getZ(location) - posZ;
/* 291 */           int newDistance = dx * dx + dy * dy + dz * dz;
/*     */ 
/*     */           
/* 294 */           if (distanceSqr1 >= newDistance) {
/* 295 */             closestIndex4 = closestIndex3;
/* 296 */             closestIndex3 = closestIndex2;
/* 297 */             closestIndex2 = closestIndex1;
/* 298 */             closestIndex1 = index;
/*     */             
/* 300 */             distanceSqr4 = distanceSqr3;
/* 301 */             distanceSqr3 = distanceSqr2;
/* 302 */             distanceSqr2 = distanceSqr1;
/* 303 */             distanceSqr1 = newDistance;
/* 304 */           } else if (distanceSqr2 >= newDistance) {
/* 305 */             closestIndex4 = closestIndex3;
/* 306 */             closestIndex3 = closestIndex2;
/* 307 */             closestIndex2 = index;
/*     */             
/* 309 */             distanceSqr4 = distanceSqr3;
/* 310 */             distanceSqr3 = distanceSqr2;
/* 311 */             distanceSqr2 = newDistance;
/* 312 */           } else if (distanceSqr3 >= newDistance) {
/* 313 */             closestIndex4 = closestIndex3;
/* 314 */             closestIndex3 = index;
/*     */             
/* 316 */             distanceSqr4 = distanceSqr3;
/* 317 */             distanceSqr3 = newDistance;
/* 318 */           } else if (distanceSqr4 >= newDistance) {
/* 319 */             closestIndex4 = index;
/*     */             
/* 321 */             distanceSqr4 = newDistance;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 332 */     Aquifer.FluidStatus closestStatus1 = getAquiferStatus(closestIndex1);
/*     */     
/* 334 */     double similarity12 = similarity(distanceSqr1, distanceSqr2);
/*     */     
/* 336 */     BlockState fluidState = closestStatus1.at(posY);
/* 337 */     BlockState actualFluidState = SharedConstants.DEBUG_DISABLE_FLUID_GENERATION ? Blocks.AIR.defaultBlockState() : fluidState;
/*     */ 
/*     */ 
/*     */     
/* 341 */     if (similarity12 <= 0.0D) {
/* 342 */       if (similarity12 >= FLOWING_UPDATE_SIMULARITY) {
/* 343 */         Aquifer.FluidStatus closestStatus2 = getAquiferStatus(closestIndex2);
/* 344 */         this.shouldScheduleFluidUpdate = !closestStatus1.equals(closestStatus2);
/*     */       } else {
/* 346 */         this.shouldScheduleFluidUpdate = false;
/*     */       } 
/* 348 */       return actualFluidState;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 353 */     if (fluidState.is(Blocks.WATER) && this.globalFluidPicker.computeFluid(posX, posY - 1, posZ).at(posY - 1).is(Blocks.LAVA)) {
/* 354 */       this.shouldScheduleFluidUpdate = true;
/* 355 */       return actualFluidState;
/*     */     } 
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
/* 372 */     MutableDouble barrierNoiseValue = new MutableDouble(NaND);
/* 373 */     Aquifer.FluidStatus closestStatus2 = getAquiferStatus(closestIndex2);
/*     */ 
/*     */     
/* 376 */     double barrier12 = similarity12 * calculatePressure(context, barrierNoiseValue, closestStatus1, closestStatus2);
/* 377 */     if (density + barrier12 > 0.0D) {
/* 378 */       this.shouldScheduleFluidUpdate = false;
/* 379 */       return null;
/*     */     } 
/*     */     
/* 382 */     Aquifer.FluidStatus closestStatus3 = getAquiferStatus(closestIndex3);
/*     */     
/* 384 */     double similarity13 = similarity(distanceSqr1, distanceSqr3);
/* 385 */     if (similarity13 > 0.0D) {
/*     */       
/* 387 */       double barrier13 = similarity12 * similarity13 * calculatePressure(context, barrierNoiseValue, closestStatus1, closestStatus3);
/* 388 */       if (density + barrier13 > 0.0D) {
/* 389 */         this.shouldScheduleFluidUpdate = false;
/* 390 */         return null;
/*     */       } 
/*     */     } 
/*     */     
/* 394 */     double similarity23 = similarity(distanceSqr2, distanceSqr3);
/* 395 */     if (similarity23 > 0.0D) {
/*     */       
/* 397 */       double barrier23 = similarity12 * similarity23 * calculatePressure(context, barrierNoiseValue, closestStatus2, closestStatus3);
/* 398 */       if (density + barrier23 > 0.0D) {
/* 399 */         this.shouldScheduleFluidUpdate = false;
/* 400 */         return null;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 405 */     boolean mayFlow12 = !closestStatus1.equals(closestStatus2);
/* 406 */     boolean mayFlow23 = (similarity23 >= FLOWING_UPDATE_SIMULARITY && !closestStatus2.equals(closestStatus3));
/* 407 */     boolean mayFlow13 = (similarity13 >= FLOWING_UPDATE_SIMULARITY && !closestStatus1.equals(closestStatus3));
/* 408 */     if (mayFlow12 || mayFlow23 || mayFlow13) {
/* 409 */       this.shouldScheduleFluidUpdate = true;
/*     */     } else {
/*     */       
/* 412 */       this
/*     */         
/* 414 */         .shouldScheduleFluidUpdate = (similarity13 >= FLOWING_UPDATE_SIMULARITY && similarity(distanceSqr1, distanceSqr4) >= FLOWING_UPDATE_SIMULARITY && !closestStatus1.equals(getAquiferStatus(closestIndex4)));
/*     */     } 
/*     */     
/* 417 */     return actualFluidState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 422 */   public boolean shouldScheduleFluidUpdate() { return this.shouldScheduleFluidUpdate; }
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
/*     */   private static double similarity(int distanceSqr1, int distanceSqr2) {
/* 435 */     double threshold = 25.0D;
/*     */ 
/*     */     
/* 438 */     return 1.0D - (distanceSqr2 - distanceSqr1) / 25.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double calculatePressure(DensityFunction.FunctionContext context, MutableDouble barrierNoiseValue, Aquifer.FluidStatus statusClosest1, Aquifer.FluidStatus statusClosest2) {
/*     */     double noiseValue, gradient;
/* 446 */     int posY = context.blockY();
/* 447 */     BlockState type1 = statusClosest1.at(posY);
/* 448 */     BlockState type2 = statusClosest2.at(posY);
/*     */     
/* 450 */     if ((type1.is(Blocks.LAVA) && type2.is(Blocks.WATER)) || (type1.is(Blocks.WATER) && type2.is(Blocks.LAVA)))
/*     */     {
/* 452 */       return 2.0D;
/*     */     }
/*     */ 
/*     */     
/* 456 */     int fluidYDiff = Math.abs(statusClosest1.fluidLevel - statusClosest2.fluidLevel);
/*     */     
/* 458 */     if (fluidYDiff == 0) {
/* 459 */       return 0.0D;
/*     */     }
/*     */     
/* 462 */     double averageFluidY = 0.5D * (statusClosest1.fluidLevel + statusClosest2.fluidLevel);
/*     */ 
/*     */     
/* 465 */     double howFarAboveAverageFluidPoint = posY + 0.5D - averageFluidY;
/*     */     
/* 467 */     double baseValue = fluidYDiff / 2.0D;
/*     */ 
/*     */ 
/*     */     
/* 471 */     double topBias = 0.0D;
/*     */     
/* 473 */     double furthestRocksFromTopBias = 2.5D;
/*     */     
/* 475 */     double furthestHolesFromTopBias = 1.5D;
/*     */ 
/*     */     
/* 478 */     double bottomBias = 3.0D;
/* 479 */     double furthestRocksFromBottomBias = 10.0D;
/* 480 */     double furthestHolesFromBottomBias = 3.0D;
/*     */ 
/*     */ 
/*     */     
/* 484 */     double distanceFromBarrierEdgeTowardsMiddle = baseValue - Math.abs(howFarAboveAverageFluidPoint);
/*     */ 
/*     */     
/* 487 */     if (howFarAboveAverageFluidPoint > 0.0D) {
/*     */       
/* 489 */       double centerPoint = 0.0D + distanceFromBarrierEdgeTowardsMiddle;
/* 490 */       if (centerPoint > 0.0D) {
/*     */         
/* 492 */         gradient = centerPoint / 1.5D;
/*     */       } else {
/*     */         
/* 495 */         gradient = centerPoint / 2.5D;
/*     */       } 
/*     */     } else {
/*     */       
/* 499 */       double centerPoint = 3.0D + distanceFromBarrierEdgeTowardsMiddle;
/* 500 */       if (centerPoint > 0.0D) {
/*     */         
/* 502 */         gradient = centerPoint / 3.0D;
/*     */       } else {
/*     */         
/* 505 */         gradient = centerPoint / 10.0D;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 510 */     double amplitude = 2.0D;
/*     */ 
/*     */ 
/*     */     
/* 514 */     if (gradient < -2.0D || gradient > 2.0D) {
/* 515 */       noiseValue = 0.0D;
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 520 */       double currentNoiseValue = barrierNoiseValue.doubleValue();
/* 521 */       if (Double.isNaN(currentNoiseValue)) {
/* 522 */         double barrierNoise = this.barrierNoise.compute(context);
/* 523 */         barrierNoiseValue.setValue(barrierNoise);
/* 524 */         noiseValue = barrierNoise;
/*     */       } else {
/* 526 */         noiseValue = currentNoiseValue;
/*     */       } 
/*     */     } 
/*     */     
/* 530 */     return 2.0D * (noiseValue + gradient);
/*     */   }
/*     */ 
/*     */   
/* 534 */   private static int gridX(int blockCoord) { return blockCoord >> 4; }
/*     */ 
/*     */ 
/*     */   
/* 538 */   private static int fromGridX(int gridCoord, int blockOffset) { return (gridCoord << 4) + blockOffset; }
/*     */ 
/*     */ 
/*     */   
/* 542 */   private static int gridY(int blockCoord) { return Math.floorDiv(blockCoord, 12); }
/*     */ 
/*     */ 
/*     */   
/* 546 */   private static int fromGridY(int gridCoord, int blockOffset) { return gridCoord * 12 + blockOffset; }
/*     */ 
/*     */ 
/*     */   
/* 550 */   private static int gridZ(int blockCoord) { return blockCoord >> 4; }
/*     */ 
/*     */ 
/*     */   
/* 554 */   private static int fromGridZ(int gridCoord, int blockOffset) { return (gridCoord << 4) + blockOffset; }
/*     */ 
/*     */   
/*     */   private Aquifer.FluidStatus getAquiferStatus(int index) {
/* 558 */     Aquifer.FluidStatus oldStatus = this.aquiferCache[index];
/* 559 */     if (oldStatus != null) {
/* 560 */       return oldStatus;
/*     */     }
/* 562 */     long location = this.aquiferLocationCache[index];
/* 563 */     Aquifer.FluidStatus status = computeFluid(BlockPos.getX(location), BlockPos.getY(location), BlockPos.getZ(location));
/* 564 */     this.aquiferCache[index] = status;
/* 565 */     return status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Aquifer.FluidStatus computeFluid(int x, int y, int z) {
/* 573 */     Aquifer.FluidStatus globalFluid = this.globalFluidPicker.computeFluid(x, y, z);
/*     */     
/* 575 */     int lowestPreliminarySurface = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 582 */     int topOfAquiferCell = y + 12;
/* 583 */     int bottomOfAquiferCell = y - 12;
/* 584 */     boolean surfaceAtCenterIsUnderGlobalFluidLevel = false;
/* 585 */     for (int[] offset : SURFACE_SAMPLING_OFFSETS_IN_CHUNKS) {
/* 586 */       int sampleX = x + SectionPos.sectionToBlockCoord(offset[0]);
/* 587 */       int sampleZ = z + SectionPos.sectionToBlockCoord(offset[1]);
/* 588 */       int preliminarySurfaceLevel = this.noiseChunk.preliminarySurfaceLevel(sampleX, sampleZ);
/*     */ 
/*     */       
/* 591 */       int adjustedSurfaceLevel = adjustSurfaceLevel(preliminarySurfaceLevel);
/*     */       
/* 593 */       boolean start = (offset[0] == 0 && offset[1] == 0);
/*     */       
/* 595 */       if (start && bottomOfAquiferCell > adjustedSurfaceLevel)
/*     */       {
/*     */         
/* 598 */         return globalFluid;
/*     */       }
/*     */       
/* 601 */       boolean topOfAquiferCellPokesAboveSurface = (topOfAquiferCell > adjustedSurfaceLevel);
/* 602 */       if (topOfAquiferCellPokesAboveSurface || start) {
/* 603 */         Aquifer.FluidStatus globalFluidAtSurface = this.globalFluidPicker.computeFluid(sampleX, adjustedSurfaceLevel, sampleZ);
/* 604 */         if (!globalFluidAtSurface.at(adjustedSurfaceLevel).isAir()) {
/* 605 */           if (start) {
/* 606 */             surfaceAtCenterIsUnderGlobalFluidLevel = true;
/*     */           }
/* 608 */           if (topOfAquiferCellPokesAboveSurface)
/*     */           {
/* 610 */             return globalFluidAtSurface;
/*     */           }
/*     */         } 
/*     */       } 
/* 614 */       lowestPreliminarySurface = Math.min(lowestPreliminarySurface, preliminarySurfaceLevel);
/*     */     } 
/*     */     
/* 617 */     int fluidSurfaceLevel = computeSurfaceLevel(x, y, z, globalFluid, lowestPreliminarySurface, surfaceAtCenterIsUnderGlobalFluidLevel);
/*     */     
/* 619 */     return new Aquifer.FluidStatus(fluidSurfaceLevel, computeFluidType(x, y, z, globalFluid, fluidSurfaceLevel));
/*     */   }
/*     */ 
/*     */   
/* 623 */   private int adjustSurfaceLevel(int preliminarySurfaceLevel) { return preliminarySurfaceLevel + 8; }
/*     */   private int computeSurfaceLevel(int x, int y, int z, Aquifer.FluidStatus globalFluid, int lowestPreliminarySurface, boolean surfaceAtCenterIsUnderGlobalFluidLevel) {
/*     */     int fluidSurfaceLevel;
/*     */     double fullyFloodidness, partiallyFloodedness;
/* 627 */     DensityFunction.SinglePointContext context = new DensityFunction.SinglePointContext(x, y, z);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 632 */     if (OverworldBiomeBuilder.isDeepDarkRegion(this.erosion, this.depth, context)) {
/* 633 */       partiallyFloodedness = -1.0D;
/* 634 */       fullyFloodidness = -1.0D;
/*     */     } else {
/* 636 */       fluidSurfaceLevel = lowestPreliminarySurface + 8 - y;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 641 */       int floodednessMaxDepth = 64;
/* 642 */       double floodednessFactor = surfaceAtCenterIsUnderGlobalFluidLevel ? Mth.clampedMap(fluidSurfaceLevel, 0.0D, 64.0D, 1.0D, 0.0D) : 0.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 647 */       double floodednessNoiseValue = Mth.clamp(this.fluidLevelFloodednessNoise.compute(context), -1.0D, 1.0D);
/*     */ 
/*     */ 
/*     */       
/* 651 */       double fullyFloodedThreshold = Mth.map(floodednessFactor, 1.0D, 0.0D, -0.3D, 0.8D);
/*     */ 
/*     */ 
/*     */       
/* 655 */       double partiallyFloodedThreshold = Mth.map(floodednessFactor, 1.0D, 0.0D, -0.8D, 0.4D);
/*     */       
/* 657 */       partiallyFloodedness = floodednessNoiseValue - partiallyFloodedThreshold;
/* 658 */       fullyFloodidness = floodednessNoiseValue - fullyFloodedThreshold;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 664 */     if (fullyFloodidness > 0.0D) {
/*     */       
/* 666 */       fluidSurfaceLevel = globalFluid.fluidLevel;
/* 667 */     } else if (partiallyFloodedness > 0.0D) {
/* 668 */       fluidSurfaceLevel = computeRandomizedFluidSurfaceLevel(x, y, z, lowestPreliminarySurface);
/*     */     } else {
/*     */       
/* 671 */       fluidSurfaceLevel = DimensionType.WAY_BELOW_MIN_Y;
/*     */     } 
/* 673 */     return fluidSurfaceLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int computeRandomizedFluidSurfaceLevel(int x, int y, int z, int lowestPreliminarySurface) {
/* 682 */     int fluidCellWidth = 16;
/* 683 */     int fluidCellHeight = 40;
/* 684 */     int fluidLevelCellX = Math.floorDiv(x, 16);
/* 685 */     int fluidLevelCellY = Math.floorDiv(y, 40);
/* 686 */     int fluidLevelCellZ = Math.floorDiv(z, 16);
/* 687 */     int fluidCellMiddleY = fluidLevelCellY * 40 + 20;
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
/* 698 */     int maxSpread = 10;
/* 699 */     double fluidLevelSpread = this.fluidLevelSpreadNoise.compute(new DensityFunction.SinglePointContext(fluidLevelCellX, fluidLevelCellY, fluidLevelCellZ)) * 10.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 704 */     int fluidLevelSpreadQuantized = Mth.quantize(fluidLevelSpread, 3);
/*     */     
/* 706 */     int targetFluidSurfaceLevel = fluidCellMiddleY + fluidLevelSpreadQuantized;
/*     */ 
/*     */     
/* 709 */     return Math.min(lowestPreliminarySurface, targetFluidSurfaceLevel);
/*     */   }
/*     */   
/*     */   private BlockState computeFluidType(int x, int y, int z, Aquifer.FluidStatus globalFluid, int fluidSurfaceLevel) {
/* 713 */     BlockState fluidType = globalFluid.fluidType;
/*     */ 
/*     */     
/* 716 */     if (fluidSurfaceLevel <= -10 && fluidSurfaceLevel != DimensionType.WAY_BELOW_MIN_Y && globalFluid.fluidType != Blocks.LAVA.defaultBlockState()) {
/* 717 */       int fluidTypeCellWidth = 64;
/* 718 */       int fluidTypeCellHeight = 40;
/*     */       
/* 720 */       int fluidTypeCellX = Math.floorDiv(x, 64);
/* 721 */       int fluidTypeCellY = Math.floorDiv(y, 40);
/* 722 */       int fluidTypeCellZ = Math.floorDiv(z, 64);
/*     */       
/* 724 */       double lavaNoiseValue = this.lavaNoise.compute(new DensityFunction.SinglePointContext(fluidTypeCellX, fluidTypeCellY, fluidTypeCellZ));
/* 725 */       if (Math.abs(lavaNoiseValue) > 0.3D) {
/* 726 */         fluidType = Blocks.LAVA.defaultBlockState();
/*     */       }
/*     */     } 
/* 729 */     return fluidType;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\Aquifer$NoiseBasedAquifer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */