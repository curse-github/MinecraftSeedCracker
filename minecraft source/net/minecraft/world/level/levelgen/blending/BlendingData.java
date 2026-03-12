/*     */ package net.minecraft.world.level.levelgen.blending;
/*     */ 
/*     */ import com.google.common.primitives.Doubles;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleArrays;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Direction8;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BlendingData
/*     */ {
/*     */   private static final double BLENDING_DENSITY_FACTOR = 0.1D;
/*     */   protected static final int CELL_WIDTH = 4;
/*     */   protected static final int CELL_HEIGHT = 8;
/*     */   protected static final int CELL_RATIO = 2;
/*     */   private static final double SOLID_DENSITY = 1.0D;
/*     */   private static final double AIR_DENSITY = -1.0D;
/*     */   private static final int CELLS_PER_SECTION_Y = 2;
/*  47 */   private static final int QUARTS_PER_SECTION = QuartPos.fromBlock(16);
/*  48 */   private static final int CELL_HORIZONTAL_MAX_INDEX_INSIDE = QUARTS_PER_SECTION - 1;
/*  49 */   private static final int CELL_HORIZONTAL_MAX_INDEX_OUTSIDE = QUARTS_PER_SECTION;
/*  50 */   private static final int CELL_COLUMN_INSIDE_COUNT = 2 * CELL_HORIZONTAL_MAX_INDEX_INSIDE + 1;
/*  51 */   private static final int CELL_COLUMN_OUTSIDE_COUNT = 2 * CELL_HORIZONTAL_MAX_INDEX_OUTSIDE + 1;
/*  52 */   private static final int CELL_COLUMN_COUNT = CELL_COLUMN_INSIDE_COUNT + CELL_COLUMN_OUTSIDE_COUNT;
/*     */ 
/*     */   
/*     */   private final LevelHeightAccessor areaWithOldGeneration;
/*     */   
/*  57 */   private static final List<Block> SURFACE_BLOCKS = List.of(new Block[] { Blocks.PODZOL, Blocks.GRAVEL, Blocks.GRASS_BLOCK, Blocks.STONE, Blocks.COARSE_DIRT, Blocks.SAND, Blocks.RED_SAND, Blocks.MYCELIUM, Blocks.SNOW_BLOCK, Blocks.TERRACOTTA, Blocks.DIRT });
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final double NO_VALUE = 1.7976931348623157E308D;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasCalculatedData;
/*     */ 
/*     */ 
/*     */   
/*     */   private final double[] heights;
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<List<Holder<Biome>>> biomes;
/*     */ 
/*     */   
/*     */   private final double[][] densities;
/*     */ 
/*     */ 
/*     */   
/*     */   private BlendingData(int minSection, int maxSection, Optional<double[]> heights) {
/*  81 */     this.heights = (double[])heights.orElseGet(() -> (double[])Util.make(new double[CELL_COLUMN_COUNT], ()));
/*     */     
/*  83 */     this.densities = new double[CELL_COLUMN_COUNT][];
/*     */     
/*  85 */     ObjectArrayList<List<Holder<Biome>>> biomes = new ObjectArrayList<List<Holder<Biome>>>(CELL_COLUMN_COUNT);
/*  86 */     biomes.size(CELL_COLUMN_COUNT);
/*  87 */     this.biomes = biomes;
/*     */     
/*  89 */     int minY = SectionPos.sectionToBlockCoord(minSection);
/*  90 */     int height = SectionPos.sectionToBlockCoord(maxSection) - minY;
/*     */     
/*  92 */     this.areaWithOldGeneration = LevelHeightAccessor.create(minY, height);
/*     */   }
/*     */   
/*     */   public static BlendingData unpack(Packed packed) {
/*  96 */     if (packed == null) {
/*  97 */       return null;
/*     */     }
/*  99 */     return new BlendingData(packed.minSection(), packed.maxSection(), packed.heights());
/*     */   }
/*     */   
/*     */   public Packed pack() {
/* 103 */     boolean hasHeight = false;
/* 104 */     for (double height : this.heights) {
/* 105 */       if (height != Double.MAX_VALUE) {
/* 106 */         hasHeight = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 110 */     return new Packed(this.areaWithOldGeneration
/* 111 */         .getMinSectionY(), this.areaWithOldGeneration
/* 112 */         .getMaxSectionY() + 1, 
/* 113 */         hasHeight ? Optional.of(DoubleArrays.copy(this.heights)) : Optional.empty());
/*     */   }
/*     */ 
/*     */   
/*     */   public static BlendingData getOrUpdateBlendingData(WorldGenRegion region, int chunkX, int chunkZ) {
/* 118 */     ChunkAccess chunk = region.getChunk(chunkX, chunkZ);
/* 119 */     BlendingData blendingData = chunk.getBlendingData();
/* 120 */     if (blendingData == null || chunk.getHighestGeneratedStatus().isBefore(ChunkStatus.BIOMES)) {
/* 121 */       return null;
/*     */     }
/*     */     
/* 124 */     blendingData.calculateData(chunk, sideByGenerationAge(region, chunkX, chunkZ, false));
/*     */     
/* 126 */     return blendingData;
/*     */   }
/*     */   
/*     */   public static Set<Direction8> sideByGenerationAge(WorldGenLevel region, int chunkX, int chunkZ, boolean wantedOldGen) {
/* 130 */     Set<Direction8> sides = EnumSet.noneOf(Direction8.class);
/* 131 */     for (Direction8 direction8 : Direction8.values()) {
/* 132 */       int testChunkX = chunkX + direction8.getStepX();
/* 133 */       int testChunkZ = chunkZ + direction8.getStepZ();
/*     */       
/* 135 */       if (region.getChunk(testChunkX, testChunkZ).isOldNoiseGeneration() == wantedOldGen) {
/* 136 */         sides.add(direction8);
/*     */       }
/*     */     } 
/* 139 */     return sides;
/*     */   }
/*     */   
/*     */   private void calculateData(ChunkAccess chunk, Set<Direction8> newSides) {
/* 143 */     if (this.hasCalculatedData) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 148 */     if (newSides.contains(Direction8.NORTH) || newSides.contains(Direction8.WEST) || newSides.contains(Direction8.NORTH_WEST)) {
/* 149 */       addValuesForColumn(getInsideIndex(0, 0), chunk, 0, 0);
/*     */     }
/* 151 */     if (newSides.contains(Direction8.NORTH)) {
/* 152 */       for (int i = 1; i < QUARTS_PER_SECTION; i++) {
/* 153 */         addValuesForColumn(getInsideIndex(i, 0), chunk, 4 * i, 0);
/*     */       }
/*     */     }
/* 156 */     if (newSides.contains(Direction8.WEST)) {
/* 157 */       for (int i = 1; i < QUARTS_PER_SECTION; i++) {
/* 158 */         addValuesForColumn(getInsideIndex(0, i), chunk, 0, 4 * i);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 163 */     if (newSides.contains(Direction8.EAST)) {
/* 164 */       for (int i = 1; i < QUARTS_PER_SECTION; i++) {
/* 165 */         addValuesForColumn(getOutsideIndex(CELL_HORIZONTAL_MAX_INDEX_OUTSIDE, i), chunk, 15, 4 * i);
/*     */       }
/*     */     }
/* 168 */     if (newSides.contains(Direction8.SOUTH)) {
/* 169 */       for (int i = 0; i < QUARTS_PER_SECTION; i++) {
/* 170 */         addValuesForColumn(getOutsideIndex(i, CELL_HORIZONTAL_MAX_INDEX_OUTSIDE), chunk, 4 * i, 15);
/*     */       }
/*     */     }
/* 173 */     if (newSides.contains(Direction8.EAST) && newSides.contains(Direction8.NORTH_EAST)) {
/* 174 */       addValuesForColumn(getOutsideIndex(CELL_HORIZONTAL_MAX_INDEX_OUTSIDE, 0), chunk, 15, 0);
/*     */     }
/*     */     
/* 177 */     if (newSides.contains(Direction8.EAST) && newSides.contains(Direction8.SOUTH) && newSides.contains(Direction8.SOUTH_EAST)) {
/* 178 */       addValuesForColumn(getOutsideIndex(CELL_HORIZONTAL_MAX_INDEX_OUTSIDE, CELL_HORIZONTAL_MAX_INDEX_OUTSIDE), chunk, 15, 15);
/*     */     }
/* 180 */     this.hasCalculatedData = true;
/*     */   }
/*     */   
/*     */   private void addValuesForColumn(int index, ChunkAccess chunk, int blockX, int blockZ) {
/* 184 */     if (this.heights[index] == Double.MAX_VALUE) {
/* 185 */       this.heights[index] = getHeightAtXZ(chunk, blockX, blockZ);
/*     */     }
/* 187 */     this.densities[index] = getDensityColumn(chunk, blockX, blockZ, Mth.floor(this.heights[index]));
/*     */     
/* 189 */     this.biomes.set(index, getBiomeColumn(chunk, blockX, blockZ));
/*     */   }
/*     */   
/*     */   private int getHeightAtXZ(ChunkAccess chunk, int blockX, int blockZ) {
/*     */     int height;
/* 194 */     if (chunk.hasPrimedHeightmap(Heightmap.Types.WORLD_SURFACE_WG)) {
/* 195 */       height = Math.min(chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, blockX, blockZ), this.areaWithOldGeneration.getMaxY());
/*     */     } else {
/* 197 */       height = this.areaWithOldGeneration.getMaxY();
/*     */     } 
/*     */     
/* 200 */     int minY = this.areaWithOldGeneration.getMinY();
/* 201 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(blockX, height, blockZ);
/* 202 */     while (pos.getY() > minY) {
/* 203 */       if (SURFACE_BLOCKS.contains(chunk.getBlockState(pos).getBlock())) {
/* 204 */         return pos.getY();
/*     */       }
/* 206 */       pos.move(Direction.DOWN);
/*     */     } 
/* 208 */     return minY;
/*     */   }
/*     */ 
/*     */   
/* 212 */   private static double read1(ChunkAccess chunk, BlockPos.MutableBlockPos pos) { return isGround(chunk, pos.move(Direction.DOWN)) ? 1.0D : -1.0D; }
/*     */ 
/*     */   
/*     */   private static double read7(ChunkAccess chunk, BlockPos.MutableBlockPos pos) {
/* 216 */     double sum = 0.0D;
/* 217 */     for (int i = 0; i < 7; i++) {
/* 218 */       sum += read1(chunk, pos);
/*     */     }
/* 220 */     return sum;
/*     */   }
/*     */   
/*     */   private double[] getDensityColumn(ChunkAccess chunk, int x, int z, int height) {
/* 224 */     double[] densities = new double[cellCountPerColumn()];
/* 225 */     Arrays.fill(densities, -1.0D);
/*     */     
/* 227 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, this.areaWithOldGeneration.getMaxY() + 1, z);
/*     */     
/* 229 */     double last7 = read7(chunk, pos);
/*     */     
/* 231 */     for (int cellIndex = densities.length - 2; cellIndex >= 0; cellIndex--) {
/* 232 */       double one = read1(chunk, pos);
/* 233 */       double current7 = read7(chunk, pos);
/*     */       
/* 235 */       densities[cellIndex] = (last7 + one + current7) / 15.0D;
/*     */       
/* 237 */       last7 = current7;
/*     */     } 
/*     */     
/* 240 */     int highestCellWithSurfaceIndex = getCellYIndex(Mth.floorDiv(height, 8));
/* 241 */     if (highestCellWithSurfaceIndex >= 0 && highestCellWithSurfaceIndex < densities.length - 1) {
/* 242 */       double inCellIndex = (height + 0.5D) % 8.0D / 8.0D;
/* 243 */       double amplitudeAboveToMakeSurfaceBeAtHeight = (1.0D - inCellIndex) / inCellIndex;
/* 244 */       double max = Math.max(amplitudeAboveToMakeSurfaceBeAtHeight, 1.0D) * 0.25D;
/*     */       
/* 246 */       densities[highestCellWithSurfaceIndex + 1] = -amplitudeAboveToMakeSurfaceBeAtHeight / max;
/* 247 */       densities[highestCellWithSurfaceIndex] = 1.0D / max;
/*     */     } 
/* 249 */     return densities;
/*     */   }
/*     */   
/*     */   private List<Holder<Biome>> getBiomeColumn(ChunkAccess chunk, int blockX, int blockZ) {
/* 253 */     ObjectArrayList<Holder<Biome>> biomes = new ObjectArrayList<Holder<Biome>>(quartCountPerColumn());
/* 254 */     biomes.size(quartCountPerColumn());
/* 255 */     for (int quartIndex = 0; quartIndex < biomes.size(); quartIndex++) {
/* 256 */       int quartY = quartIndex + QuartPos.fromBlock(this.areaWithOldGeneration.getMinY());
/* 257 */       biomes.set(quartIndex, chunk.getNoiseBiome(QuartPos.fromBlock(blockX), quartY, QuartPos.fromBlock(blockZ)));
/*     */     } 
/* 259 */     return biomes;
/*     */   }
/*     */   
/*     */   private static boolean isGround(ChunkAccess chunk, BlockPos pos) {
/* 263 */     BlockState state = chunk.getBlockState(pos);
/* 264 */     if (state.isAir()) {
/* 265 */       return false;
/*     */     }
/* 267 */     if (state.is(BlockTags.LEAVES)) {
/* 268 */       return false;
/*     */     }
/* 270 */     if (state.is(BlockTags.LOGS)) {
/* 271 */       return false;
/*     */     }
/* 273 */     if (state.is(Blocks.BROWN_MUSHROOM_BLOCK) || state.is(Blocks.RED_MUSHROOM_BLOCK)) {
/* 274 */       return false;
/*     */     }
/* 276 */     if (state.getCollisionShape(chunk, pos).isEmpty()) {
/* 277 */       return false;
/*     */     }
/*     */     
/* 280 */     return true;
/*     */   }
/*     */   
/*     */   protected double getHeight(int cellX, int cellY, int cellZ) {
/* 284 */     if (cellX == CELL_HORIZONTAL_MAX_INDEX_OUTSIDE || cellZ == CELL_HORIZONTAL_MAX_INDEX_OUTSIDE) {
/* 285 */       return this.heights[getOutsideIndex(cellX, cellZ)];
/*     */     }
/* 287 */     if (cellX == 0 || cellZ == 0) {
/* 288 */       return this.heights[getInsideIndex(cellX, cellZ)];
/*     */     }
/* 290 */     return Double.MAX_VALUE;
/*     */   }
/*     */   
/*     */   private double getDensity(double[] densityColumn, int cellY) {
/* 294 */     if (densityColumn == null) {
/* 295 */       return Double.MAX_VALUE;
/*     */     }
/* 297 */     int yIndex = getCellYIndex(cellY);
/* 298 */     if (yIndex < 0 || yIndex >= densityColumn.length) {
/* 299 */       return Double.MAX_VALUE;
/*     */     }
/* 301 */     return densityColumn[yIndex] * 0.1D;
/*     */   }
/*     */   
/*     */   protected double getDensity(int cellX, int cellY, int cellZ) {
/* 305 */     if (cellY == getMinY()) {
/* 306 */       return 0.1D;
/*     */     }
/* 308 */     if (cellX == CELL_HORIZONTAL_MAX_INDEX_OUTSIDE || cellZ == CELL_HORIZONTAL_MAX_INDEX_OUTSIDE) {
/* 309 */       return getDensity(this.densities[getOutsideIndex(cellX, cellZ)], cellY);
/*     */     }
/* 311 */     if (cellX == 0 || cellZ == 0) {
/* 312 */       return getDensity(this.densities[getInsideIndex(cellX, cellZ)], cellY);
/*     */     }
/* 314 */     return Double.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void iterateBiomes(int minCellX, int quartY, int minCellZ, BiomeConsumer biomeConsumer) {
/* 322 */     if (quartY < QuartPos.fromBlock(this.areaWithOldGeneration.getMinY()) || quartY > QuartPos.fromBlock(this.areaWithOldGeneration.getMaxY())) {
/*     */       return;
/*     */     }
/* 325 */     int quartIndex = quartY - QuartPos.fromBlock(this.areaWithOldGeneration.getMinY());
/* 326 */     for (int i = 0; i < this.biomes.size(); i++) {
/* 327 */       List<Holder<Biome>> biomeCell = (List)this.biomes.get(i);
/* 328 */       if (biomeCell != null) {
/*     */ 
/*     */         
/* 331 */         Holder<Biome> value = (Holder)biomeCell.get(quartIndex);
/* 332 */         if (value != null) {
/* 333 */           biomeConsumer.consume(minCellX + getX(i), minCellZ + getZ(i), value);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void iterateHeights(int minCellX, int minCellZ, HeightConsumer heightConsumer) {
/* 343 */     for (int i = 0; i < this.heights.length; i++) {
/* 344 */       double value = this.heights[i];
/* 345 */       if (value != Double.MAX_VALUE) {
/* 346 */         heightConsumer.consume(minCellX + getX(i), minCellZ + getZ(i), value);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void iterateDensities(int minCellX, int minCellZ, int fromCellY, int toCellY, DensityConsumer densityConsumer) {
/* 356 */     int minCellY = getColumnMinY();
/* 357 */     int minYIndex = Math.max(0, fromCellY - minCellY);
/* 358 */     int maxYIndex = Math.min(cellCountPerColumn(), toCellY - minCellY);
/*     */     
/* 360 */     for (int i = 0; i < this.densities.length; i++) {
/* 361 */       double[] densityColumn = this.densities[i];
/* 362 */       if (densityColumn != null) {
/* 363 */         int testCellX = minCellX + getX(i);
/* 364 */         int testCellZ = minCellZ + getZ(i);
/* 365 */         for (int yIndex = minYIndex; yIndex < maxYIndex; yIndex++) {
/* 366 */           densityConsumer.consume(testCellX, yIndex + minCellY, testCellZ, densityColumn[yIndex] * 0.1D);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 373 */   private int cellCountPerColumn() { return this.areaWithOldGeneration.getSectionsCount() * 2; }
/*     */ 
/*     */ 
/*     */   
/* 377 */   private int quartCountPerColumn() { return QuartPos.fromSection(this.areaWithOldGeneration.getSectionsCount()); }
/*     */ 
/*     */ 
/*     */   
/* 381 */   private int getColumnMinY() { return getMinY() + 1; }
/*     */ 
/*     */ 
/*     */   
/* 385 */   private int getMinY() { return this.areaWithOldGeneration.getMinSectionY() * 2; }
/*     */ 
/*     */ 
/*     */   
/* 389 */   private int getCellYIndex(int cellY) { return cellY - getColumnMinY(); }
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
/* 410 */   private static int getInsideIndex(int x, int z) { return CELL_HORIZONTAL_MAX_INDEX_INSIDE - x + z; }
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
/* 433 */   private static int getOutsideIndex(int x, int z) { return CELL_COLUMN_INSIDE_COUNT + x + CELL_HORIZONTAL_MAX_INDEX_OUTSIDE - z; }
/*     */ 
/*     */   
/*     */   private static int getX(int index) {
/* 437 */     if (index < CELL_COLUMN_INSIDE_COUNT) {
/* 438 */       return zeroIfNegative(CELL_HORIZONTAL_MAX_INDEX_INSIDE - index);
/*     */     }
/* 440 */     int offsetIndex = index - CELL_COLUMN_INSIDE_COUNT;
/* 441 */     return CELL_HORIZONTAL_MAX_INDEX_OUTSIDE - zeroIfNegative(CELL_HORIZONTAL_MAX_INDEX_OUTSIDE - offsetIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getZ(int index) {
/* 446 */     if (index < CELL_COLUMN_INSIDE_COUNT) {
/* 447 */       return zeroIfNegative(index - CELL_HORIZONTAL_MAX_INDEX_INSIDE);
/*     */     }
/* 449 */     int offsetIndex = index - CELL_COLUMN_INSIDE_COUNT;
/* 450 */     return CELL_HORIZONTAL_MAX_INDEX_OUTSIDE - zeroIfNegative(offsetIndex - CELL_HORIZONTAL_MAX_INDEX_OUTSIDE);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 455 */   private static int zeroIfNegative(int value) { return value & (value >> 31 ^ 0xFFFFFFFF); }
/*     */ 
/*     */ 
/*     */   
/* 459 */   public LevelHeightAccessor getAreaWithOldGeneration() { return this.areaWithOldGeneration; }
/*     */   public static final class Packed extends Record { private final int minSection; private final int maxSection; private final Optional<double[]> heights;
/*     */     
/* 462 */     public Packed(int minSection, int maxSection, Optional<double[]> heights) { this.minSection = minSection; this.maxSection = maxSection; this.heights = heights; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #462	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 462 */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed; } public int minSection() { return this.minSection; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #462	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #462	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed;
/* 462 */       //   0	8	1	o	Ljava/lang/Object; } public int maxSection() { return this.maxSection; } public Optional<double[]> heights() { return this.heights; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 467 */     private static final Codec<double[]> DOUBLE_ARRAY_CODEC = Codec.DOUBLE.listOf().xmap(Doubles::toArray, Doubles::asList);
/* 468 */     public static final Codec<Packed> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.INT
/* 469 */           .fieldOf("min_section").forGetter(Packed::minSection), Codec.INT
/* 470 */           .fieldOf("max_section").forGetter(Packed::maxSection), DOUBLE_ARRAY_CODEC
/* 471 */           .lenientOptionalFieldOf("heights").forGetter(Packed::heights))
/* 472 */         .apply(i, Packed::new)).validate(Packed::validateArraySize);
/*     */     
/*     */     private static DataResult<Packed> validateArraySize(Packed blendingData) {
/* 475 */       if (blendingData.heights.isPresent() && (double[])blendingData.heights.get().length != BlendingData.CELL_COLUMN_COUNT) {
/* 476 */         return DataResult.error(() -> "heights has to be of length " + BlendingData.CELL_COLUMN_COUNT);
/*     */       }
/*     */       
/* 479 */       return DataResult.success(blendingData);
/*     */     } }
/*     */ 
/*     */   
/*     */   protected static interface BiomeConsumer {
/*     */     void consume(int param1Int1, int param1Int2, Holder<Biome> param1Holder);
/*     */   }
/*     */   
/*     */   protected static interface HeightConsumer {
/*     */     void consume(int param1Int1, int param1Int2, double param1Double);
/*     */   }
/*     */   
/*     */   protected static interface DensityConsumer {
/*     */     void consume(int param1Int1, int param1Int2, int param1Int3, double param1Double);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blending\BlendingData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */