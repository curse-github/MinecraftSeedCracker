/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.BuddingAmethystBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.levelgen.GeodeBlockSettings;
/*     */ import net.minecraft.world.level.levelgen.GeodeCrackSettings;
/*     */ import net.minecraft.world.level.levelgen.GeodeLayerSettings;
/*     */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ public class GeodeFeature extends Feature<GeodeConfiguration> {
/*  29 */   private static final Direction[] DIRECTIONS = Direction.values();
/*     */ 
/*     */   
/*  32 */   public GeodeFeature(Codec<GeodeConfiguration> codec) { super(codec); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<GeodeConfiguration> context) {
/*  37 */     GeodeConfiguration config = (GeodeConfiguration)context.config();
/*  38 */     RandomSource random = context.random();
/*  39 */     BlockPos origin = context.origin();
/*  40 */     WorldGenLevel level = context.level();
/*  41 */     int minGenOffset = config.minGenOffset;
/*  42 */     int maxGenOffset = config.maxGenOffset;
/*     */     
/*  44 */     List<Pair<BlockPos, Integer>> points = Lists.newLinkedList();
/*  45 */     int numPoints = config.distributionPoints.sample(random);
/*  46 */     WorldgenRandom random1 = new WorldgenRandom(new LegacyRandomSource(level.getSeed()));
/*  47 */     NormalNoise noise = NormalNoise.create(random1, -4, new double[] { 1.0D });
/*  48 */     List<BlockPos> crackPoints = Lists.newLinkedList();
/*  49 */     double crackSizeAdjustment = numPoints / config.outerWallDistance.getMaxValue();
/*  50 */     GeodeLayerSettings layerSettings = config.geodeLayerSettings;
/*  51 */     GeodeBlockSettings blockSettings = config.geodeBlockSettings;
/*  52 */     GeodeCrackSettings crackSettings = config.geodeCrackSettings;
/*  53 */     double innerAir = 1.0D / Math.sqrt(layerSettings.filling);
/*  54 */     double innermostBlockLayer = 1.0D / Math.sqrt(layerSettings.innerLayer + crackSizeAdjustment);
/*  55 */     double innerCrust = 1.0D / Math.sqrt(layerSettings.middleLayer + crackSizeAdjustment);
/*  56 */     double outerCrust = 1.0D / Math.sqrt(layerSettings.outerLayer + crackSizeAdjustment);
/*  57 */     double crackSize = 1.0D / Math.sqrt(crackSettings.baseCrackSize + random.nextDouble() / 2.0D + ((numPoints > 3) ? crackSizeAdjustment : 0.0D));
/*  58 */     boolean shouldGenerateCrack = (random.nextFloat() < crackSettings.generateCrackChance);
/*     */     
/*  60 */     int numInvalidPoints = 0;
/*  61 */     for (int i = 0; i < numPoints; i++) {
/*  62 */       int x = config.outerWallDistance.sample(random);
/*  63 */       int y = config.outerWallDistance.sample(random);
/*  64 */       int z = config.outerWallDistance.sample(random);
/*  65 */       BlockPos pos = origin.offset(x, y, z);
/*  66 */       BlockState state = level.getBlockState(pos);
/*  67 */       if ((state.isAir() || state.is(blockSettings.invalidBlocks)) && 
/*  68 */         ++numInvalidPoints > config.invalidBlocksThreshold) {
/*  69 */         return false;
/*     */       }
/*     */       
/*  72 */       points.add(Pair.of(pos, Integer.valueOf(config.pointOffset.sample(random))));
/*     */     } 
/*     */     
/*  75 */     if (shouldGenerateCrack) {
/*  76 */       int offsetIndex = random.nextInt(4);
/*     */       
/*  78 */       int crackOffset = numPoints * 2 + 1;
/*  79 */       if (offsetIndex == 0) {
/*  80 */         crackPoints.add(origin.offset(crackOffset, 7, 0));
/*  81 */         crackPoints.add(origin.offset(crackOffset, 5, 0));
/*  82 */         crackPoints.add(origin.offset(crackOffset, 1, 0));
/*  83 */       } else if (offsetIndex == 1) {
/*  84 */         crackPoints.add(origin.offset(0, 7, crackOffset));
/*  85 */         crackPoints.add(origin.offset(0, 5, crackOffset));
/*  86 */         crackPoints.add(origin.offset(0, 1, crackOffset));
/*  87 */       } else if (offsetIndex == 2) {
/*  88 */         crackPoints.add(origin.offset(crackOffset, 7, crackOffset));
/*  89 */         crackPoints.add(origin.offset(crackOffset, 5, crackOffset));
/*  90 */         crackPoints.add(origin.offset(crackOffset, 1, crackOffset));
/*     */       } else {
/*  92 */         crackPoints.add(origin.offset(0, 7, 0));
/*  93 */         crackPoints.add(origin.offset(0, 5, 0));
/*  94 */         crackPoints.add(origin.offset(0, 1, 0));
/*     */       } 
/*     */     } 
/*     */     
/*  98 */     List<BlockPos> potentialCrystalPlacements = Lists.newArrayList();
/*  99 */     Predicate<BlockState> canReplace = isReplaceable(config.geodeBlockSettings.cannotReplace);
/*     */     
/* 101 */     for (BlockPos pointInside : BlockPos.betweenClosed(origin.offset(minGenOffset, minGenOffset, minGenOffset), origin.offset(maxGenOffset, maxGenOffset, maxGenOffset))) {
/* 102 */       double noiseOffset = noise.getValue(pointInside.getX(), pointInside.getY(), pointInside.getZ()) * config.noiseMultiplier;
/*     */       
/* 104 */       double distSumShell = 0.0D;
/* 105 */       double distSumCrack = 0.0D;
/*     */       
/* 107 */       for (Pair<BlockPos, Integer> point : points) {
/* 108 */         distSumShell += Mth.invSqrt(pointInside.distSqr((Vec3i)point.getFirst()) + ((Integer)point.getSecond()).intValue()) + noiseOffset;
/*     */       }
/*     */       
/* 111 */       for (BlockPos point : crackPoints) {
/* 112 */         distSumCrack += Mth.invSqrt(pointInside.distSqr(point) + crackSettings.crackPointOffset) + noiseOffset;
/*     */       }
/*     */ 
/*     */       
/* 116 */       if (distSumShell < outerCrust) {
/*     */         continue;
/*     */       }
/*     */       
/* 120 */       if (shouldGenerateCrack && distSumCrack >= crackSize && distSumShell < innerAir) {
/*     */         
/* 122 */         safeSetBlock(level, pointInside, Blocks.AIR.defaultBlockState(), canReplace);
/*     */ 
/*     */         
/* 125 */         for (Direction direction : DIRECTIONS) {
/* 126 */           BlockPos adjacentPos = pointInside.relative(direction);
/* 127 */           FluidState adjacentFluidState = level.getFluidState(adjacentPos);
/* 128 */           if (!adjacentFluidState.isEmpty())
/* 129 */             level.scheduleTick(adjacentPos, adjacentFluidState.getType(), 0); 
/*     */         }  continue;
/*     */       } 
/* 132 */       if (distSumShell >= innerAir) {
/* 133 */         safeSetBlock(level, pointInside, blockSettings.fillingProvider.getState(random, pointInside), canReplace); continue;
/* 134 */       }  if (distSumShell >= innermostBlockLayer) {
/* 135 */         boolean useAlternateLayer = (random.nextFloat() < config.useAlternateLayer0Chance);
/* 136 */         if (useAlternateLayer) {
/* 137 */           safeSetBlock(level, pointInside, blockSettings.alternateInnerLayerProvider.getState(random, pointInside), canReplace);
/*     */         } else {
/* 139 */           safeSetBlock(level, pointInside, blockSettings.innerLayerProvider.getState(random, pointInside), canReplace);
/*     */         } 
/*     */         
/* 142 */         if ((!config.placementsRequireLayer0Alternate || useAlternateLayer) && random.nextFloat() < config.usePotentialPlacementsChance)
/* 143 */           potentialCrystalPlacements.add(pointInside.immutable());  continue;
/*     */       } 
/* 145 */       if (distSumShell >= innerCrust) {
/* 146 */         safeSetBlock(level, pointInside, blockSettings.middleLayerProvider.getState(random, pointInside), canReplace); continue;
/* 147 */       }  if (distSumShell >= outerCrust) {
/* 148 */         safeSetBlock(level, pointInside, blockSettings.outerLayerProvider.getState(random, pointInside), canReplace);
/*     */       }
/*     */     } 
/*     */     
/* 152 */     List<BlockState> innerPlacements = blockSettings.innerPlacements;
/* 153 */     for (BlockPos crystalPos : potentialCrystalPlacements) {
/* 154 */       BlockState blockState = (BlockState)Util.getRandom(innerPlacements, random);
/* 155 */       for (Direction direction : DIRECTIONS) {
/* 156 */         if (blockState.hasProperty(BlockStateProperties.FACING)) {
/* 157 */           blockState = (BlockState)blockState.setValue(BlockStateProperties.FACING, direction);
/*     */         }
/*     */         
/* 160 */         BlockPos placePos = crystalPos.relative(direction);
/* 161 */         BlockState placeState = level.getBlockState(placePos);
/* 162 */         if (blockState.hasProperty(BlockStateProperties.WATERLOGGED)) {
/* 163 */           blockState = (BlockState)blockState.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(placeState.getFluidState().isSource()));
/*     */         }
/*     */         
/* 166 */         if (BuddingAmethystBlock.canClusterGrowAtState(placeState)) {
/* 167 */           safeSetBlock(level, placePos, blockState, canReplace);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 173 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\GeodeFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */