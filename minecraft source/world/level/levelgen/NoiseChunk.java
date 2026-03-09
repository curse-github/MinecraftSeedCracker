/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.server.level.ColumnPos;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.Climate;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ import net.minecraft.world.level.levelgen.material.MaterialRuleList;
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
/*     */ public class NoiseChunk
/*     */   implements DensityFunction.FunctionContext, DensityFunction.ContextProvider
/*     */ {
/*     */   private final int cellCountXZ;
/*     */   private final int cellCountY;
/*     */   private final int cellNoiseMinY;
/*     */   private final int firstCellX;
/*     */   private final int firstCellZ;
/*     */   private final int firstNoiseX;
/*     */   private final int firstNoiseZ;
/*     */   private final List<NoiseInterpolator> interpolators;
/*     */   private final List<CacheAllInCell> cellCaches;
/*     */   private final Map<DensityFunction, DensityFunction> wrapped;
/*     */   private final Long2IntMap preliminarySurfaceLevelCache;
/*     */   private final Aquifer aquifer;
/*     */   private final DensityFunction preliminarySurfaceLevel;
/*     */   private final BlockStateFiller blockStateRule;
/*     */   private final Blender blender;
/*     */   private final FlatCache blendAlpha;
/*     */   private final FlatCache blendOffset;
/*     */   private final DensityFunctions.BeardifierOrMarker beardifier;
/*     */   private long lastBlendingDataPos;
/*     */   private Blender.BlendingOutput lastBlendingOutput;
/*     */   private final int noiseSizeXZ;
/*     */   private final int cellWidth;
/*     */   private final int cellHeight;
/*     */   private boolean interpolating;
/*     */   private boolean fillingCell;
/*     */   private int cellStartBlockX;
/*     */   private int cellStartBlockY;
/*     */   private int cellStartBlockZ;
/*     */   private int inCellX;
/*     */   private int inCellY;
/*     */   private int inCellZ;
/*     */   private long interpolationCounter;
/*     */   private long arrayInterpolationCounter;
/*     */   private int arrayIndex;
/*     */   private final DensityFunction.ContextProvider sliceFillingContextProvider;
/*     */   
/*     */   public static NoiseChunk forChunk(ChunkAccess chunk, RandomState randomState, DensityFunctions.BeardifierOrMarker beardifier, NoiseGeneratorSettings settings, Aquifer.FluidPicker globalFluidPicker, Blender blender) {
/* 107 */     NoiseSettings noiseSettings = settings.noiseSettings().clampToHeightAccessor(chunk);
/* 108 */     ChunkPos pos = chunk.getPos();
/* 109 */     int cellCountXZ = 16 / noiseSettings.getCellWidth();
/* 110 */     return new NoiseChunk(cellCountXZ, randomState, pos.getMinBlockX(), pos.getMinBlockZ(), noiseSettings, beardifier, settings, globalFluidPicker, blender);
/*     */   } public NoiseChunk(int cellCountXZ, RandomState randomState, int chunkMinBlockX, int chunkMinBlockZ, NoiseSettings noiseSettings, DensityFunctions.BeardifierOrMarker beardifier, NoiseGeneratorSettings settings, Aquifer.FluidPicker globalFluidPicker, Blender blender) { this.wrapped = new HashMap(); this.preliminarySurfaceLevelCache = new Long2IntOpenHashMap(); this.lastBlendingDataPos = ChunkPos.INVALID_CHUNK_POS; this.lastBlendingOutput = new Blender.BlendingOutput(1.0D, 0.0D); this.sliceFillingContextProvider = new DensityFunction.ContextProvider() { public DensityFunction.FunctionContext forIndex(int cellYIndex) { NoiseChunk.this.cellStartBlockY = (cellYIndex + NoiseChunk.this.cellNoiseMinY) * NoiseChunk.this.cellHeight; NoiseChunk.this.interpolationCounter++; NoiseChunk.this.inCellY = 0; NoiseChunk.this.arrayIndex = cellYIndex; return NoiseChunk.this; } public void fillAllDirectly(double[] output, DensityFunction function) { for (int cellYIndex = 0; cellYIndex < NoiseChunk.this.cellCountY + 1; cellYIndex++) {
/*     */             NoiseChunk.this.cellStartBlockY = (cellYIndex + NoiseChunk.this.cellNoiseMinY) * NoiseChunk.this.cellHeight;
/*     */             NoiseChunk.this.interpolationCounter++;
/*     */             NoiseChunk.this.inCellY = 0;
/*     */             NoiseChunk.this.arrayIndex = cellYIndex;
/*     */             output[cellYIndex] = function.compute(NoiseChunk.this);
/*     */           }  } }
/*     */       ;
/* 119 */     this.cellWidth = noiseSettings.getCellWidth();
/* 120 */     this.cellHeight = noiseSettings.getCellHeight();
/*     */     
/* 122 */     this.cellCountXZ = cellCountXZ;
/* 123 */     this.cellCountY = Mth.floorDiv(noiseSettings.height(), this.cellHeight);
/* 124 */     this.cellNoiseMinY = Mth.floorDiv(noiseSettings.minY(), this.cellHeight);
/*     */     
/* 126 */     this.firstCellX = Math.floorDiv(chunkMinBlockX, this.cellWidth);
/* 127 */     this.firstCellZ = Math.floorDiv(chunkMinBlockZ, this.cellWidth);
/*     */     
/* 129 */     this.interpolators = Lists.newArrayList();
/* 130 */     this.cellCaches = Lists.newArrayList();
/*     */     
/* 132 */     this.firstNoiseX = QuartPos.fromBlock(chunkMinBlockX);
/* 133 */     this.firstNoiseZ = QuartPos.fromBlock(chunkMinBlockZ);
/*     */     
/* 135 */     this.noiseSizeXZ = QuartPos.fromBlock(cellCountXZ * this.cellWidth);
/*     */     
/* 137 */     this.blender = blender;
/* 138 */     this.beardifier = beardifier;
/*     */     
/* 140 */     this.blendAlpha = new FlatCache(new BlendAlpha(this), false);
/* 141 */     this.blendOffset = new FlatCache(new BlendOffset(this), false);
/*     */     
/* 143 */     if (!blender.isEmpty()) {
/*     */       
/* 145 */       for (int x = 0; x <= this.noiseSizeXZ; x++) {
/* 146 */         int quartX = this.firstNoiseX + x;
/* 147 */         int blockX = QuartPos.toBlock(quartX);
/*     */         
/* 149 */         for (int z = 0; z <= this.noiseSizeXZ; z++) {
/* 150 */           int quartZ = this.firstNoiseZ + z;
/* 151 */           int blockZ = QuartPos.toBlock(quartZ);
/*     */           
/* 153 */           Blender.BlendingOutput blendingOutput = blender.blendOffsetAndFactor(blockX, blockZ);
/*     */           
/* 155 */           this.blendAlpha.values[x + z * this.blendAlpha.sizeXZ] = blendingOutput.alpha();
/* 156 */           this.blendOffset.values[x + z * this.blendOffset.sizeXZ] = blendingOutput.blendingOffset();
/*     */         } 
/*     */       } 
/*     */     } else {
/* 160 */       Arrays.fill(this.blendAlpha.values, 1.0D);
/* 161 */       Arrays.fill(this.blendOffset.values, 0.0D);
/*     */     } 
/*     */     
/* 164 */     NoiseRouter router = randomState.router();
/*     */ 
/*     */     
/* 167 */     NoiseRouter wrappedRouter = router.mapAll(this::wrap);
/* 168 */     this.preliminarySurfaceLevel = wrappedRouter.preliminarySurfaceLevel();
/*     */ 
/*     */ 
/*     */     
/* 172 */     if (!settings.isAquifersEnabled()) {
/* 173 */       this.aquifer = Aquifer.createDisabled(globalFluidPicker);
/*     */     } else {
/* 175 */       int chunkX = SectionPos.blockToSectionCoord(chunkMinBlockX);
/* 176 */       int chunkZ = SectionPos.blockToSectionCoord(chunkMinBlockZ);
/* 177 */       this.aquifer = Aquifer.create(this, new ChunkPos(chunkX, chunkZ), wrappedRouter, randomState
/*     */ 
/*     */ 
/*     */           
/* 181 */           .aquiferRandom(), noiseSettings
/* 182 */           .minY(), noiseSettings
/* 183 */           .height(), globalFluidPicker);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 188 */     List<BlockStateFiller> builder = new ArrayList<BlockStateFiller>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 193 */     DensityFunction fullNoiseValue = DensityFunctions.cacheAllInCell(DensityFunctions.add(wrappedRouter.finalDensity(), DensityFunctions.BeardifierMarker.INSTANCE)).mapAll(this::wrap);
/*     */ 
/*     */     
/* 196 */     builder.add(context -> this.aquifer.computeSubstance(context, fullNoiseValue.compute(context)));
/*     */ 
/*     */     
/* 199 */     if (settings.oreVeinsEnabled()) {
/* 200 */       builder.add(OreVeinifier.create(wrappedRouter
/* 201 */             .veinToggle(), wrappedRouter
/* 202 */             .veinRidged(), wrappedRouter
/* 203 */             .veinGap(), randomState
/* 204 */             .oreRandom()));
/*     */     }
/*     */ 
/*     */     
/* 208 */     this.blockStateRule = new MaterialRuleList((BlockStateFiller[])builder.toArray(new BlockStateFiller[0])); }
/*     */ 
/*     */   
/*     */   protected Climate.Sampler cachedClimateSampler(NoiseRouter noises, List<Climate.ParameterPoint> spawnTarget) {
/* 212 */     return new Climate.Sampler(noises
/* 213 */         .temperature().mapAll(this::wrap), noises
/* 214 */         .vegetation().mapAll(this::wrap), noises
/* 215 */         .continents().mapAll(this::wrap), noises
/* 216 */         .erosion().mapAll(this::wrap), noises
/* 217 */         .depth().mapAll(this::wrap), noises
/* 218 */         .ridges().mapAll(this::wrap), spawnTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 224 */   protected BlockState getInterpolatedState() { return this.blockStateRule.calculate(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 229 */   public int blockX() { return this.cellStartBlockX + this.inCellX; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 234 */   public int blockY() { return this.cellStartBlockY + this.inCellY; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 239 */   public int blockZ() { return this.cellStartBlockZ + this.inCellZ; }
/*     */ 
/*     */   
/*     */   public int maxPreliminarySurfaceLevel(int minBlockX, int minBlockZ, int maxBlockX, int maxBlockZ) {
/* 243 */     int maxY = Integer.MIN_VALUE;
/* 244 */     for (int blockZ = minBlockZ; blockZ <= maxBlockZ; blockZ += 4) {
/* 245 */       for (int blockX = minBlockX; blockX <= maxBlockX; blockX += 4) {
/* 246 */         int surfaceLevel = preliminarySurfaceLevel(blockX, blockZ);
/* 247 */         if (surfaceLevel > maxY) {
/* 248 */           maxY = surfaceLevel;
/*     */         }
/*     */       } 
/*     */     } 
/* 252 */     return maxY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int preliminarySurfaceLevel(int sampleX, int sampleZ) {
/* 260 */     int quantizedX = QuartPos.toBlock(QuartPos.fromBlock(sampleX));
/* 261 */     int quantizedZ = QuartPos.toBlock(QuartPos.fromBlock(sampleZ));
/* 262 */     return this.preliminarySurfaceLevelCache.computeIfAbsent(ColumnPos.asLong(quantizedX, quantizedZ), this::computePreliminarySurfaceLevel);
/*     */   }
/*     */   
/*     */   private int computePreliminarySurfaceLevel(long key) {
/* 266 */     int blockX = ColumnPos.getX(key);
/* 267 */     int blockZ = ColumnPos.getZ(key);
/*     */     
/* 269 */     return Mth.floor(this.preliminarySurfaceLevel.compute(new DensityFunction.SinglePointContext(blockX, 0, blockZ)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 274 */   public Blender getBlender() { return this.blender; }
/*     */ 
/*     */   
/*     */   private void fillSlice(boolean slice0, int cellX) {
/* 278 */     this.cellStartBlockX = cellX * this.cellWidth;
/*     */     
/* 280 */     this.inCellX = 0;
/*     */     
/* 282 */     for (int cellZIndex = 0; cellZIndex < this.cellCountXZ + 1; cellZIndex++) {
/* 283 */       int cellZ = this.firstCellZ + cellZIndex;
/* 284 */       this.cellStartBlockZ = cellZ * this.cellWidth;
/* 285 */       this.inCellZ = 0;
/*     */       
/* 287 */       this.arrayInterpolationCounter++;
/*     */       
/* 289 */       for (NoiseInterpolator noiseInterpolator : this.interpolators) {
/* 290 */         double[] slice = slice0 ? noiseInterpolator.slice0 : noiseInterpolator.slice1[cellZIndex];
/*     */         
/* 292 */         noiseInterpolator.fillArray(slice, this.sliceFillingContextProvider);
/*     */       } 
/*     */     } 
/* 295 */     this.arrayInterpolationCounter++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initializeForFirstCellX() {
/* 300 */     if (this.interpolating) {
/* 301 */       throw new IllegalStateException("Staring interpolation twice");
/*     */     }
/* 303 */     this.interpolating = true;
/* 304 */     this.interpolationCounter = 0L;
/* 305 */     fillSlice(true, this.firstCellX);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void advanceCellX(int cellXIndex) {
/* 316 */     fillSlice(false, this.firstCellX + cellXIndex + 1);
/* 317 */     this.cellStartBlockX = (this.firstCellX + cellXIndex) * this.cellWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NoiseChunk forIndex(int cellIndex) {
/* 323 */     int zInCell = Math.floorMod(cellIndex, this.cellWidth);
/* 324 */     int xyIndex = Math.floorDiv(cellIndex, this.cellWidth);
/*     */     
/* 326 */     int xInCell = Math.floorMod(xyIndex, this.cellWidth);
/* 327 */     int yInCell = this.cellHeight - 1 - Math.floorDiv(xyIndex, this.cellWidth);
/*     */     
/* 329 */     this.inCellX = xInCell;
/* 330 */     this.inCellY = yInCell;
/* 331 */     this.inCellZ = zInCell;
/*     */     
/* 333 */     this.arrayIndex = cellIndex;
/* 334 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillAllDirectly(double[] output, DensityFunction function) {
/* 340 */     this.arrayIndex = 0;
/* 341 */     for (int yInCell = this.cellHeight - 1; yInCell >= 0; yInCell--) {
/* 342 */       this.inCellY = yInCell;
/* 343 */       for (int xInCell = 0; xInCell < this.cellWidth; xInCell++) {
/* 344 */         this.inCellX = xInCell;
/* 345 */         for (int zInCell = 0; zInCell < this.cellWidth; zInCell++) {
/* 346 */           this.inCellZ = zInCell;
/* 347 */           output[this.arrayIndex++] = function.compute(this);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void selectCellYZ(int cellYIndex, int cellZIndex) {
/* 354 */     for (NoiseInterpolator i : this.interpolators) {
/* 355 */       i.selectCellYZ(cellYIndex, cellZIndex);
/*     */     }
/*     */     
/* 358 */     this.fillingCell = true;
/* 359 */     this.cellStartBlockY = (cellYIndex + this.cellNoiseMinY) * this.cellHeight;
/* 360 */     this.cellStartBlockZ = (this.firstCellZ + cellZIndex) * this.cellWidth;
/*     */     
/* 362 */     this.arrayInterpolationCounter++;
/*     */     
/* 364 */     for (CacheAllInCell cellCache : this.cellCaches) {
/* 365 */       cellCache.noiseFiller.fillArray(cellCache.values, this);
/*     */     }
/*     */     
/* 368 */     this.arrayInterpolationCounter++;
/* 369 */     this.fillingCell = false;
/*     */   }
/*     */   
/*     */   public void updateForY(int posY, double factorY) {
/* 373 */     this.inCellY = posY - this.cellStartBlockY;
/* 374 */     for (NoiseInterpolator i : this.interpolators) {
/* 375 */       i.updateForY(factorY);
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateForX(int posX, double factorX) {
/* 380 */     this.inCellX = posX - this.cellStartBlockX;
/* 381 */     for (NoiseInterpolator i : this.interpolators) {
/* 382 */       i.updateForX(factorX);
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateForZ(int posZ, double factorZ) {
/* 387 */     this.inCellZ = posZ - this.cellStartBlockZ;
/* 388 */     this.interpolationCounter++;
/* 389 */     for (NoiseInterpolator i : this.interpolators) {
/* 390 */       i.updateForZ(factorZ);
/*     */     }
/*     */   }
/*     */   
/*     */   public void stopInterpolation() {
/* 395 */     if (!this.interpolating) {
/* 396 */       throw new IllegalStateException("Staring interpolation twice");
/*     */     }
/* 398 */     this.interpolating = false;
/*     */   }
/*     */ 
/*     */   
/* 402 */   public void swapSlices() { this.interpolators.forEach(NoiseInterpolator::swapSlices); }
/*     */ 
/*     */ 
/*     */   
/* 406 */   public Aquifer aquifer() { return this.aquifer; }
/*     */ 
/*     */ 
/*     */   
/* 410 */   protected int cellWidth() { return this.cellWidth; }
/*     */ 
/*     */ 
/*     */   
/* 414 */   protected int cellHeight() { return this.cellHeight; }
/*     */ 
/*     */   
/*     */   private static interface NoiseChunkDensityFunction
/*     */     extends DensityFunction
/*     */   {
/*     */     DensityFunction wrapped();
/*     */     
/* 422 */     default double minValue() { return wrapped().minValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 427 */     default double maxValue() { return wrapped().maxValue(); }
/*     */   }
/*     */   
/*     */   private class FlatCache
/*     */     implements NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked {
/*     */     private final DensityFunction noiseFiller;
/*     */     private final double[] values;
/*     */     private final int sizeXZ;
/*     */     
/*     */     private FlatCache(DensityFunction noiseFiller, boolean fill) {
/* 437 */       this.noiseFiller = noiseFiller;
/* 438 */       this.sizeXZ = NoiseChunk.this.noiseSizeXZ + 1;
/* 439 */       this.values = new double[this.sizeXZ * this.sizeXZ];
/* 440 */       if (fill) {
/* 441 */         for (int x = 0; x <= NoiseChunk.this.noiseSizeXZ; x++) {
/* 442 */           int quartX = NoiseChunk.this.firstNoiseX + x;
/* 443 */           int blockX = QuartPos.toBlock(quartX);
/*     */           
/* 445 */           for (int z = 0; z <= NoiseChunk.this.noiseSizeXZ; z++) {
/* 446 */             int quartZ = NoiseChunk.this.firstNoiseZ + z;
/* 447 */             int blockZ = QuartPos.toBlock(quartZ);
/*     */             
/* 449 */             this.values[x + z * this.sizeXZ] = noiseFiller.compute(new DensityFunction.SinglePointContext(blockX, 0, blockZ));
/*     */           } 
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public double compute(DensityFunction.FunctionContext context) {
/* 457 */       int quartX = QuartPos.fromBlock(context.blockX());
/* 458 */       int quartZ = QuartPos.fromBlock(context.blockZ());
/*     */       
/* 460 */       int x = quartX - NoiseChunk.this.firstNoiseX;
/* 461 */       int z = quartZ - NoiseChunk.this.firstNoiseZ;
/*     */       
/* 463 */       if (x >= 0 && z >= 0 && x < this.sizeXZ && z < this.sizeXZ) {
/* 464 */         return this.values[x + z * this.sizeXZ];
/*     */       }
/* 466 */       return this.noiseFiller.compute(context);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 472 */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { contextProvider.fillAllDirectly(output, this); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 477 */     public DensityFunction wrapped() { return this.noiseFiller; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 482 */     public DensityFunctions.Marker.Type type() { return DensityFunctions.Marker.Type.FlatCache; }
/*     */   }
/*     */   
/*     */   private class CacheAllInCell
/*     */     implements NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked {
/*     */     private final DensityFunction noiseFiller;
/*     */     private final double[] values;
/*     */     
/*     */     private CacheAllInCell(DensityFunction noiseFiller) {
/* 491 */       this.noiseFiller = noiseFiller;
/* 492 */       this.values = new double[NoiseChunk.this.cellWidth * NoiseChunk.this.cellWidth * NoiseChunk.this.cellHeight];
/* 493 */       NoiseChunk.this.cellCaches.add(this);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(DensityFunction.FunctionContext context) {
/* 499 */       if (context != NoiseChunk.this) {
/* 500 */         return this.noiseFiller.compute(context);
/*     */       }
/* 502 */       if (!NoiseChunk.this.interpolating) {
/* 503 */         throw new IllegalStateException("Trying to sample interpolator outside the interpolation loop");
/*     */       }
/* 505 */       int x = NoiseChunk.this.inCellX;
/* 506 */       int y = NoiseChunk.this.inCellY;
/* 507 */       int z = NoiseChunk.this.inCellZ;
/*     */       
/* 509 */       if (x >= 0 && y >= 0 && z >= 0 && x < NoiseChunk.this.cellWidth && y < NoiseChunk.this.cellHeight && z < NoiseChunk.this.cellWidth) {
/* 510 */         return this.values[((NoiseChunk.this.cellHeight - 1 - y) * NoiseChunk.this.cellWidth + x) * NoiseChunk.this.cellWidth + z];
/*     */       }
/* 512 */       return this.noiseFiller.compute(context);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 518 */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { contextProvider.fillAllDirectly(output, this); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 523 */     public DensityFunction wrapped() { return this.noiseFiller; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 528 */     public DensityFunctions.Marker.Type type() { return DensityFunctions.Marker.Type.CacheAllInCell; }
/*     */   }
/*     */ 
/*     */   
/*     */   public class NoiseInterpolator
/*     */     implements NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked
/*     */   {
/*     */     private double[][] slice0;
/*     */     
/*     */     private double[][] slice1;
/*     */     
/*     */     private final DensityFunction noiseFiller;
/*     */     
/*     */     private double noise000;
/*     */     
/*     */     private double noise001;
/*     */     
/*     */     private double noise100;
/*     */     
/*     */     private double noise101;
/*     */     
/*     */     private double noise010;
/*     */     
/*     */     private double noise011;
/*     */     
/*     */     private double noise110;
/*     */     
/*     */     private double noise111;
/*     */     
/*     */     private double valueXZ00;
/*     */     
/*     */     private double valueXZ10;
/*     */     
/*     */     private double valueXZ01;
/*     */     
/*     */     private double valueXZ11;
/*     */     
/*     */     private double valueZ0;
/*     */     
/*     */     private double valueZ1;
/*     */     private double value;
/*     */     
/*     */     private NoiseInterpolator(DensityFunction noiseFiller) {
/* 571 */       this.noiseFiller = noiseFiller;
/* 572 */       this.slice0 = allocateSlice(NoiseChunk.this.cellCountY, NoiseChunk.this.cellCountXZ);
/* 573 */       this.slice1 = allocateSlice(NoiseChunk.this.cellCountY, NoiseChunk.this.cellCountXZ);
/*     */       
/* 575 */       NoiseChunk.this.interpolators.add(this);
/*     */     }
/*     */     
/*     */     private double[][] allocateSlice(int cellCountY, int cellCountZ) {
/* 579 */       int sizeZ = cellCountZ + 1;
/* 580 */       int sizeY = cellCountY + 1;
/* 581 */       double[][] result = new double[sizeZ][sizeY];
/* 582 */       for (int cellZIndex = 0; cellZIndex < sizeZ; cellZIndex++) {
/* 583 */         result[cellZIndex] = new double[sizeY];
/*     */       }
/* 585 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void selectCellYZ(int cellYIndex, int cellZIndex) {
/* 595 */       this.noise000 = this.slice0[cellZIndex][cellYIndex];
/* 596 */       this.noise001 = this.slice0[cellZIndex + 1][cellYIndex];
/* 597 */       this.noise100 = this.slice1[cellZIndex][cellYIndex];
/* 598 */       this.noise101 = this.slice1[cellZIndex + 1][cellYIndex];
/*     */       
/* 600 */       this.noise010 = this.slice0[cellZIndex][cellYIndex + 1];
/* 601 */       this.noise011 = this.slice0[cellZIndex + 1][cellYIndex + 1];
/* 602 */       this.noise110 = this.slice1[cellZIndex][cellYIndex + 1];
/* 603 */       this.noise111 = this.slice1[cellZIndex + 1][cellYIndex + 1];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void updateForY(double factorY) {
/* 612 */       this.valueXZ00 = Mth.lerp(factorY, this.noise000, this.noise010);
/* 613 */       this.valueXZ10 = Mth.lerp(factorY, this.noise100, this.noise110);
/* 614 */       this.valueXZ01 = Mth.lerp(factorY, this.noise001, this.noise011);
/* 615 */       this.valueXZ11 = Mth.lerp(factorY, this.noise101, this.noise111);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void updateForX(double factorX) {
/* 624 */       this.valueZ0 = Mth.lerp(factorX, this.valueXZ00, this.valueXZ10);
/* 625 */       this.valueZ1 = Mth.lerp(factorX, this.valueXZ01, this.valueXZ11);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 634 */     private void updateForZ(double factorZ) { this.value = Mth.lerp(factorZ, this.valueZ0, this.valueZ1); }
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(DensityFunction.FunctionContext context) {
/* 639 */       if (context != NoiseChunk.this) {
/* 640 */         return this.noiseFiller.compute(context);
/*     */       }
/* 642 */       if (!NoiseChunk.this.interpolating) {
/* 643 */         throw new IllegalStateException("Trying to sample interpolator outside the interpolation loop");
/*     */       }
/* 645 */       if (NoiseChunk.this.fillingCell) {
/* 646 */         return Mth.lerp3(NoiseChunk.this.inCellX / NoiseChunk.this.cellWidth, NoiseChunk.this.inCellY / NoiseChunk.this.cellHeight, NoiseChunk.this.inCellZ / NoiseChunk.this.cellWidth, this.noise000, this.noise100, this.noise010, this.noise110, this.noise001, this.noise101, this.noise011, this.noise111);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 654 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 659 */       if (NoiseChunk.this.fillingCell) {
/*     */         
/* 661 */         contextProvider.fillAllDirectly(output, this);
/*     */         return;
/*     */       } 
/* 664 */       wrapped().fillArray(output, contextProvider);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 669 */     public DensityFunction wrapped() { return this.noiseFiller; }
/*     */ 
/*     */ 
/*     */     
/*     */     private void swapSlices() {
/* 674 */       double[][] tmp = this.slice0;
/* 675 */       this.slice0 = this.slice1;
/* 676 */       this.slice1 = tmp;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 681 */     public DensityFunctions.Marker.Type type() { return DensityFunctions.Marker.Type.Interpolated; }
/*     */   }
/*     */ 
/*     */   
/*     */   private class CacheOnce
/*     */     implements NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked
/*     */   {
/*     */     private final DensityFunction function;
/*     */     
/*     */     private long lastCounter;
/*     */     
/*     */     private long lastArrayCounter;
/*     */     private double lastValue;
/*     */     private double[] lastArray;
/*     */     
/* 696 */     private CacheOnce(DensityFunction function) { this.function = function; }
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(DensityFunction.FunctionContext context) {
/* 701 */       if (context != NoiseChunk.this) {
/* 702 */         return this.function.compute(context);
/*     */       }
/* 704 */       if (this.lastArray != null && this.lastArrayCounter == NoiseChunk.this.arrayInterpolationCounter) {
/* 705 */         return this.lastArray[NoiseChunk.this.arrayIndex];
/*     */       }
/* 707 */       if (this.lastCounter == NoiseChunk.this.interpolationCounter) {
/* 708 */         return this.lastValue;
/*     */       }
/* 710 */       this.lastCounter = NoiseChunk.this.interpolationCounter;
/* 711 */       double value = this.function.compute(context);
/* 712 */       this.lastValue = value;
/* 713 */       return value;
/*     */     }
/*     */ 
/*     */     
/*     */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 718 */       if (this.lastArray != null && this.lastArrayCounter == NoiseChunk.this.arrayInterpolationCounter) {
/* 719 */         System.arraycopy(this.lastArray, 0, output, 0, output.length);
/*     */         return;
/*     */       } 
/* 722 */       wrapped().fillArray(output, contextProvider);
/* 723 */       if (this.lastArray != null && this.lastArray.length == output.length) {
/* 724 */         System.arraycopy(output, 0, this.lastArray, 0, output.length);
/*     */       } else {
/* 726 */         this.lastArray = (double[])output.clone();
/*     */       } 
/* 728 */       this.lastArrayCounter = NoiseChunk.this.arrayInterpolationCounter;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 733 */     public DensityFunction wrapped() { return this.function; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 738 */     public DensityFunctions.Marker.Type type() { return DensityFunctions.Marker.Type.CacheOnce; }
/*     */   }
/*     */   
/*     */   private static class Cache2D implements NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked {
/*     */     private final DensityFunction function;
/*     */     private long lastPos2D;
/*     */     private double lastValue;
/*     */     
/*     */     private Cache2D(DensityFunction function) {
/* 747 */       this.lastPos2D = ChunkPos.INVALID_CHUNK_POS;
/*     */ 
/*     */ 
/*     */       
/* 751 */       this.function = function;
/*     */     }
/*     */ 
/*     */     
/*     */     public double compute(DensityFunction.FunctionContext context) {
/* 756 */       int blockX = context.blockX();
/* 757 */       int blockZ = context.blockZ();
/* 758 */       long pos2D = ChunkPos.asLong(blockX, blockZ);
/* 759 */       if (this.lastPos2D == pos2D) {
/* 760 */         return this.lastValue;
/*     */       }
/* 762 */       this.lastPos2D = pos2D;
/* 763 */       double value = this.function.compute(context);
/* 764 */       this.lastValue = value;
/* 765 */       return value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 770 */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { this.function.fillArray(output, contextProvider); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 775 */     public DensityFunction wrapped() { return this.function; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 780 */     public DensityFunctions.Marker.Type type() { return DensityFunctions.Marker.Type.Cache2D; }
/*     */   }
/*     */ 
/*     */   
/*     */   private Blender.BlendingOutput getOrComputeBlendingOutput(int blockX, int blockZ) {
/* 785 */     long pos2D = ChunkPos.asLong(blockX, blockZ);
/* 786 */     if (this.lastBlendingDataPos == pos2D) {
/* 787 */       return this.lastBlendingOutput;
/*     */     }
/* 789 */     this.lastBlendingDataPos = pos2D;
/* 790 */     Blender.BlendingOutput output = this.blender.blendOffsetAndFactor(blockX, blockZ);
/* 791 */     this.lastBlendingOutput = output;
/* 792 */     return output;
/*     */   }
/*     */   
/*     */   private class BlendAlpha
/*     */     implements NoiseChunkDensityFunction
/*     */   {
/* 798 */     public DensityFunction wrapped() { return DensityFunctions.BlendAlpha.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 803 */     public DensityFunction mapAll(DensityFunction.Visitor visitor) { return wrapped().mapAll(visitor); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 808 */     public double compute(DensityFunction.FunctionContext context) { return NoiseChunk.this.getOrComputeBlendingOutput(context.blockX(), context.blockZ()).alpha(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 814 */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { contextProvider.fillAllDirectly(output, this); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 819 */     public double minValue() { return 0.0D; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 824 */     public double maxValue() { return 1.0D; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 829 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return DensityFunctions.BlendAlpha.CODEC; }
/*     */   }
/*     */ 
/*     */   
/*     */   private class BlendOffset
/*     */     implements NoiseChunkDensityFunction
/*     */   {
/* 836 */     public DensityFunction wrapped() { return DensityFunctions.BlendOffset.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 841 */     public DensityFunction mapAll(DensityFunction.Visitor visitor) { return wrapped().mapAll(visitor); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 846 */     public double compute(DensityFunction.FunctionContext context) { return NoiseChunk.this.getOrComputeBlendingOutput(context.blockX(), context.blockZ()).blendingOffset(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 852 */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) { contextProvider.fillAllDirectly(output, this); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 858 */     public double minValue() { return Double.NEGATIVE_INFINITY; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 864 */     public double maxValue() { return Double.POSITIVE_INFINITY; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 869 */     public KeyDispatchDataCodec<? extends DensityFunction> codec() { return DensityFunctions.BlendOffset.CODEC; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 879 */   protected DensityFunction wrap(DensityFunction function) { return (DensityFunction)this.wrapped.computeIfAbsent(function, this::wrapNew); }
/*     */ 
/*     */   
/*     */   private DensityFunction wrapNew(DensityFunction function) {
/* 883 */     if (function instanceof DensityFunctions.Marker) { DensityFunctions.Marker marker = (DensityFunctions.Marker)function;
/* 884 */       switch (marker.type()) { default: throw new MatchException(null, null);case Interpolated: case FlatCache: case Cache2D: case CacheOnce: case CacheAllInCell: break; }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 889 */         new CacheAllInCell(marker.wrapped()); }
/*     */ 
/*     */     
/* 892 */     if (this.blender != Blender.empty()) {
/* 893 */       if (function == DensityFunctions.BlendAlpha.INSTANCE) {
/* 894 */         return this.blendAlpha;
/*     */       }
/* 896 */       if (function == DensityFunctions.BlendOffset.INSTANCE) {
/* 897 */         return this.blendOffset;
/*     */       }
/*     */     } 
/* 900 */     if (function == DensityFunctions.BeardifierMarker.INSTANCE) {
/* 901 */       return this.beardifier;
/*     */     }
/* 903 */     if (function instanceof DensityFunctions.HolderHolder) { DensityFunctions.HolderHolder holder = (DensityFunctions.HolderHolder)function;
/* 904 */       return (DensityFunction)holder.function().value(); }
/*     */     
/* 906 */     return function;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface BlockStateFiller {
/*     */     BlockState calculate(DensityFunction.FunctionContext param1FunctionContext);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\NoiseChunk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */