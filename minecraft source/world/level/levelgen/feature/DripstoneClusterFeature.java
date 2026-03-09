/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.ClampedNormalFloat;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Column;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DripstoneClusterConfiguration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DripstoneClusterFeature
/*     */   extends Feature<DripstoneClusterConfiguration>
/*     */ {
/*  30 */   public DripstoneClusterFeature(Codec<DripstoneClusterConfiguration> codec) { super(codec); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<DripstoneClusterConfiguration> context) {
/*  35 */     WorldGenLevel level = context.level();
/*  36 */     BlockPos origin = context.origin();
/*  37 */     DripstoneClusterConfiguration config = (DripstoneClusterConfiguration)context.config();
/*  38 */     RandomSource random = context.random();
/*     */     
/*  40 */     if (!DripstoneUtils.isEmptyOrWater(level, origin)) {
/*  41 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  45 */     int height = config.height.sample(random);
/*     */     
/*  47 */     float wetness = config.wetness.sample(random);
/*  48 */     float density = config.density.sample(random);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  53 */     int xRadius = config.radius.sample(random);
/*  54 */     int zRadius = config.radius.sample(random);
/*  55 */     for (int dx = -xRadius; dx <= xRadius; dx++) {
/*  56 */       for (int dz = -zRadius; dz <= zRadius; dz++) {
/*  57 */         double chanceOfStalagmiteOrStalactite = getChanceOfStalagmiteOrStalactite(xRadius, zRadius, dx, dz, config);
/*  58 */         BlockPos pos = origin.offset(dx, 0, dz);
/*  59 */         placeColumn(level, random, pos, dx, dz, wetness, chanceOfStalagmiteOrStalactite, height, density, config);
/*     */       } 
/*     */     } 
/*  62 */     return true;
/*     */   }
/*     */   private void placeColumn(WorldGenLevel level, RandomSource random, BlockPos pos, int dx, int dz, float chanceOfWater, double chanceOfStalagmiteOrStalactite, int clusterHeight, float density, DripstoneClusterConfiguration config) {
/*     */     int actualStalagmiteHeight, actualStalactiteHeight, stalagmiteHeight, stalactiteHeight;
/*     */     Column column;
/*  67 */     Optional<Column> baseColumn = Column.scan(level, pos, config.floorToCeilingSearchRange, DripstoneUtils::isEmptyOrWater, DripstoneUtils::isNeitherEmptyNorWater);
/*  68 */     if (baseColumn.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  72 */     OptionalInt ceiling = ((Column)baseColumn.get()).getCeiling();
/*  73 */     OptionalInt baseFloor = ((Column)baseColumn.get()).getFloor();
/*     */     
/*  75 */     if (ceiling.isEmpty() && baseFloor.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  81 */     boolean wantPool = (random.nextFloat() < chanceOfWater);
/*     */     
/*  83 */     if (wantPool && baseFloor.isPresent() && canPlacePool(level, pos.atY(baseFloor.getAsInt()))) {
/*     */       
/*  85 */       int baseFloorY = baseFloor.getAsInt();
/*  86 */       column = ((Column)baseColumn.get()).withFloor(OptionalInt.of(baseFloorY - 1));
/*  87 */       level.setBlock(pos.atY(baseFloorY), Blocks.WATER.defaultBlockState(), 2);
/*     */     } else {
/*  89 */       column = (Column)baseColumn.get();
/*     */     } 
/*     */     
/*  92 */     OptionalInt floor = column.getFloor();
/*     */ 
/*     */ 
/*     */     
/*  96 */     boolean wantStalactite = (random.nextDouble() < chanceOfStalagmiteOrStalactite);
/*  97 */     if (ceiling.isPresent() && wantStalactite && !isLava(level, pos.atY(ceiling.getAsInt()))) {
/*  98 */       int maxHeightForThisColumn; stalagmiteHeight = config.dripstoneBlockLayerThickness.sample(random);
/*  99 */       replaceBlocksWithDripstoneBlocks(level, pos.atY(ceiling.getAsInt()), stalagmiteHeight, Direction.UP);
/*     */       
/* 101 */       if (floor.isPresent()) {
/* 102 */         maxHeightForThisColumn = Math.min(clusterHeight, ceiling.getAsInt() - floor.getAsInt());
/*     */       } else {
/* 104 */         maxHeightForThisColumn = clusterHeight;
/*     */       } 
/* 106 */       stalactiteHeight = getDripstoneHeight(random, dx, dz, density, maxHeightForThisColumn, config);
/*     */     } else {
/* 108 */       stalactiteHeight = 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 113 */     boolean wantStalagmite = (random.nextDouble() < chanceOfStalagmiteOrStalactite);
/* 114 */     if (floor.isPresent() && wantStalagmite && !isLava(level, pos.atY(floor.getAsInt()))) {
/* 115 */       actualStalactiteHeight = config.dripstoneBlockLayerThickness.sample(random);
/* 116 */       replaceBlocksWithDripstoneBlocks(level, pos.atY(floor.getAsInt()), actualStalactiteHeight, Direction.DOWN);
/*     */       
/* 118 */       if (ceiling.isPresent()) {
/* 119 */         stalagmiteHeight = Math.max(0, stalactiteHeight + Mth.randomBetweenInclusive(random, -config.maxStalagmiteStalactiteHeightDiff, config.maxStalagmiteStalactiteHeightDiff));
/*     */       } else {
/*     */         
/* 122 */         stalagmiteHeight = getDripstoneHeight(random, dx, dz, density, clusterHeight, config);
/*     */       } 
/*     */     } else {
/* 125 */       stalagmiteHeight = 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     if (ceiling.isPresent() && floor.isPresent() && ceiling.getAsInt() - stalactiteHeight <= floor.getAsInt() + stalagmiteHeight) {
/*     */ 
/*     */       
/* 134 */       int floorY = floor.getAsInt();
/* 135 */       int ceilingY = ceiling.getAsInt();
/* 136 */       int lowestStalactiteBottom = Math.max(ceilingY - stalactiteHeight, floorY + 1);
/* 137 */       int highestStalagmiteTop = Math.min(floorY + stalagmiteHeight, ceilingY - 1);
/* 138 */       int actualStalactiteBottom = Mth.randomBetweenInclusive(random, lowestStalactiteBottom, highestStalagmiteTop + 1);
/* 139 */       int actualStalagmiteTop = actualStalactiteBottom - 1;
/* 140 */       actualStalactiteHeight = ceilingY - actualStalactiteBottom;
/* 141 */       actualStalagmiteHeight = actualStalagmiteTop - floorY;
/*     */     } else {
/* 143 */       actualStalactiteHeight = stalactiteHeight;
/* 144 */       actualStalagmiteHeight = stalagmiteHeight;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     boolean mergeTips = (random.nextBoolean() && actualStalactiteHeight > 0 && actualStalagmiteHeight > 0 && column.getHeight().isPresent() && actualStalactiteHeight + actualStalagmiteHeight == column.getHeight().getAsInt());
/*     */     
/* 153 */     if (ceiling.isPresent()) {
/* 154 */       DripstoneUtils.growPointedDripstone(level, pos.atY(ceiling.getAsInt() - 1), Direction.DOWN, actualStalactiteHeight, mergeTips);
/*     */     }
/* 156 */     if (floor.isPresent()) {
/* 157 */       DripstoneUtils.growPointedDripstone(level, pos.atY(floor.getAsInt() + 1), Direction.UP, actualStalagmiteHeight, mergeTips);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 162 */   private boolean isLava(LevelReader level, BlockPos pos) { return level.getBlockState(pos).is(Blocks.LAVA); }
/*     */ 
/*     */   
/*     */   private int getDripstoneHeight(RandomSource random, int dx, int dz, float density, int maxHeight, DripstoneClusterConfiguration config) {
/* 166 */     if (random.nextFloat() > density) {
/* 167 */       return 0;
/*     */     }
/*     */     
/* 170 */     int distanceFromCenter = Math.abs(dx) + Math.abs(dz);
/*     */ 
/*     */     
/* 173 */     float heightMean = (float)Mth.clampedMap(distanceFromCenter, 0.0D, config.maxDistanceFromCenterAffectingHeightBias, maxHeight / 2.0D, 0.0D);
/* 174 */     return (int)randomBetweenBiased(random, 0.0F, maxHeight, heightMean, config.heightDeviation);
/*     */   }
/*     */   
/*     */   private boolean canPlacePool(WorldGenLevel level, BlockPos pos) {
/* 178 */     BlockState state = level.getBlockState(pos);
/* 179 */     if (state.is(Blocks.WATER) || state.is(Blocks.DRIPSTONE_BLOCK) || state.is(Blocks.POINTED_DRIPSTONE)) {
/* 180 */       return false;
/*     */     }
/* 182 */     if (level.getBlockState(pos.above()).getFluidState().is(FluidTags.WATER)) {
/* 183 */       return false;
/*     */     }
/*     */     
/* 186 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 187 */       if (!canBeAdjacentToWater(level, pos.relative(direction))) {
/* 188 */         return false;
/*     */       }
/*     */     } 
/* 191 */     return canBeAdjacentToWater(level, pos.below());
/*     */   }
/*     */   
/*     */   private boolean canBeAdjacentToWater(LevelAccessor level, BlockPos pos) {
/* 195 */     BlockState state = level.getBlockState(pos);
/* 196 */     return (state.is(BlockTags.BASE_STONE_OVERWORLD) || state.getFluidState().is(FluidTags.WATER));
/*     */   }
/*     */   
/*     */   private void replaceBlocksWithDripstoneBlocks(WorldGenLevel level, BlockPos firstPos, int maxCount, Direction direction) {
/* 200 */     BlockPos.MutableBlockPos pos = firstPos.mutable();
/* 201 */     for (int i = 0; i < maxCount; i++) {
/* 202 */       if (!DripstoneUtils.placeDripstoneBlockIfPossible(level, pos)) {
/*     */         return;
/*     */       }
/* 205 */       pos.move(direction);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double getChanceOfStalagmiteOrStalactite(int xRadius, int zRadius, int dx, int dz, DripstoneClusterConfiguration config) {
/* 213 */     int xDistanceFromEdge = xRadius - Math.abs(dx);
/* 214 */     int zDistanceFromEdge = zRadius - Math.abs(dz);
/* 215 */     int distanceFromEdge = Math.min(xDistanceFromEdge, zDistanceFromEdge);
/*     */     
/* 217 */     return Mth.clampedMap(distanceFromEdge, 0.0F, config.maxDistanceFromEdgeAffectingChanceOfDripstoneColumn, config.chanceOfDripstoneColumnAtMaxDistanceFromCenter, 1.0F);
/*     */   }
/*     */ 
/*     */   
/* 221 */   private static float randomBetweenBiased(RandomSource random, float min, float maxExclusive, float mean, float deviation) { return ClampedNormalFloat.sample(random, mean, deviation, min, maxExclusive); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\DripstoneClusterFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */