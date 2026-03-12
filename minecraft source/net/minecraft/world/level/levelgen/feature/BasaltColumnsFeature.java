/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;
/*     */ 
/*     */ public class BasaltColumnsFeature
/*     */   extends Feature<ColumnFeatureConfiguration> {
/*  17 */   private static final ImmutableList<Block> CANNOT_PLACE_ON = ImmutableList.of(Blocks.LAVA, Blocks.BEDROCK, Blocks.MAGMA_BLOCK, Blocks.SOUL_SAND, Blocks.NETHER_BRICKS, Blocks.NETHER_BRICK_FENCE, Blocks.NETHER_BRICK_STAIRS, Blocks.NETHER_WART, Blocks.CHEST, Blocks.SPAWNER);
/*     */ 
/*     */   
/*     */   private static final int CLUSTERED_REACH = 5;
/*     */ 
/*     */   
/*     */   private static final int CLUSTERED_SIZE = 50;
/*     */ 
/*     */   
/*     */   private static final int UNCLUSTERED_REACH = 8;
/*     */   
/*     */   private static final int UNCLUSTERED_SIZE = 15;
/*     */ 
/*     */   
/*  31 */   public BasaltColumnsFeature(Codec<ColumnFeatureConfiguration> codec) { super(codec); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<ColumnFeatureConfiguration> context) {
/*  36 */     int lavaSeaLevel = context.chunkGenerator().getSeaLevel();
/*  37 */     BlockPos origin = context.origin();
/*  38 */     WorldGenLevel level = context.level();
/*  39 */     RandomSource random = context.random();
/*  40 */     ColumnFeatureConfiguration config = (ColumnFeatureConfiguration)context.config();
/*  41 */     if (!canPlaceAt(level, lavaSeaLevel, origin.mutable())) {
/*  42 */       return false;
/*     */     }
/*     */     
/*  45 */     int columnHeight = config.height().sample(random);
/*     */     
/*  47 */     boolean genereteClustered = (random.nextFloat() < 0.9F);
/*  48 */     int reach = Math.min(columnHeight, genereteClustered ? 5 : 8);
/*  49 */     int count = genereteClustered ? 50 : 15;
/*     */ 
/*     */     
/*  52 */     boolean placed = false;
/*  53 */     for (BlockPos pos : BlockPos.randomBetweenClosed(random, count, origin.getX() - reach, origin.getY(), origin.getZ() - reach, origin.getX() + reach, origin.getY(), origin.getZ() + reach)) {
/*  54 */       int blocksToPlaceY = columnHeight - pos.distManhattan(origin);
/*  55 */       if (blocksToPlaceY >= 0) {
/*  56 */         placed |= placeColumn(level, lavaSeaLevel, pos, blocksToPlaceY, config.reach().sample(random));
/*     */       }
/*     */     } 
/*     */     
/*  60 */     return placed;
/*     */   }
/*     */   
/*     */   private boolean placeColumn(LevelAccessor level, int lavaSeaLevel, BlockPos origin, int columnHeight, int reach) {
/*  64 */     boolean placedAny = false;
/*     */     
/*  66 */     for (BlockPos pos : BlockPos.betweenClosed(origin.getX() - reach, origin.getY(), origin.getZ() - reach, origin.getX() + reach, origin.getY(), origin.getZ() + reach)) {
/*  67 */       int stepLimit = pos.distManhattan(origin);
/*     */ 
/*     */ 
/*     */       
/*  71 */       BlockPos columnPos = isAirOrLavaOcean(level, lavaSeaLevel, pos) ? findSurface(level, lavaSeaLevel, pos.mutable(), stepLimit) : findAir(level, pos.mutable(), stepLimit);
/*  72 */       if (columnPos == null) {
/*     */         continue;
/*     */       }
/*     */       
/*  76 */       int blocksY = columnHeight - stepLimit / 2;
/*  77 */       BlockPos.MutableBlockPos cursor = columnPos.mutable();
/*  78 */       while (blocksY >= 0) {
/*  79 */         if (isAirOrLavaOcean(level, lavaSeaLevel, cursor)) {
/*  80 */           setBlock(level, cursor, Blocks.BASALT.defaultBlockState());
/*  81 */           cursor.move(Direction.UP);
/*  82 */           placedAny = true;
/*  83 */         } else if (level.getBlockState(cursor).is(Blocks.BASALT)) {
/*  84 */           cursor.move(Direction.UP);
/*     */         } else {
/*     */           break;
/*     */         } 
/*     */         
/*  89 */         blocksY--;
/*     */       } 
/*     */     } 
/*     */     
/*  93 */     return placedAny;
/*     */   }
/*     */   
/*     */   private static BlockPos findSurface(LevelAccessor level, int lavaSeaLevel, BlockPos.MutableBlockPos cursor, int limit) {
/*  97 */     while (cursor.getY() > level.getMinY() + 1 && limit > 0) {
/*  98 */       limit--;
/*  99 */       if (canPlaceAt(level, lavaSeaLevel, cursor)) {
/* 100 */         return cursor;
/*     */       }
/* 102 */       cursor.move(Direction.DOWN);
/*     */     } 
/* 104 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean canPlaceAt(LevelAccessor level, int lavaSeaLevel, BlockPos.MutableBlockPos cursor) {
/* 108 */     if (isAirOrLavaOcean(level, lavaSeaLevel, cursor)) {
/* 109 */       BlockState blockState = level.getBlockState(cursor.move(Direction.DOWN));
/* 110 */       cursor.move(Direction.UP);
/* 111 */       return (!blockState.isAir() && !CANNOT_PLACE_ON.contains(blockState.getBlock()));
/*     */     } 
/* 113 */     return false;
/*     */   }
/*     */   
/*     */   private static BlockPos findAir(LevelAccessor level, BlockPos.MutableBlockPos cursor, int limit) {
/* 117 */     while (cursor.getY() <= level.getMaxY() && limit > 0) {
/* 118 */       limit--;
/*     */       
/* 120 */       BlockState blockState = level.getBlockState(cursor);
/* 121 */       if (CANNOT_PLACE_ON.contains(blockState.getBlock())) {
/* 122 */         return null;
/*     */       }
/*     */       
/* 125 */       if (blockState.isAir()) {
/* 126 */         return cursor;
/*     */       }
/*     */       
/* 129 */       cursor.move(Direction.UP);
/*     */     } 
/* 131 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean isAirOrLavaOcean(LevelAccessor level, int lavaSeaLevel, BlockPos blockPos) {
/* 135 */     BlockState blockState = level.getBlockState(blockPos);
/* 136 */     return (blockState.isAir() || (blockState.is(Blocks.LAVA) && blockPos.getY() <= lavaSeaLevel));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\BasaltColumnsFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */