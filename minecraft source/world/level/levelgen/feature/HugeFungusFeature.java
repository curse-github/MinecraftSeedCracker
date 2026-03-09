/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ 
/*     */ public class HugeFungusFeature
/*     */   extends Feature<HugeFungusConfiguration> {
/*     */   private static final float HUGE_PROBABILITY = 0.06F;
/*     */   
/*  20 */   public HugeFungusFeature(Codec<HugeFungusConfiguration> codec) { super(codec); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<HugeFungusConfiguration> context) {
/*  25 */     WorldGenLevel level = context.level();
/*  26 */     BlockPos origin = context.origin();
/*  27 */     RandomSource random = context.random();
/*  28 */     ChunkGenerator chunkGenerator = context.chunkGenerator();
/*  29 */     HugeFungusConfiguration config = (HugeFungusConfiguration)context.config();
/*  30 */     Block allowedBaseBlock = config.validBaseState.getBlock();
/*  31 */     BlockPos newOrigin = null;
/*     */     
/*  33 */     BlockState belowState = level.getBlockState(origin.below());
/*  34 */     if (belowState.is(allowedBaseBlock)) {
/*  35 */       newOrigin = origin;
/*     */     }
/*     */     
/*  38 */     if (newOrigin == null) {
/*  39 */       return false;
/*     */     }
/*     */     
/*  42 */     int totalHeight = Mth.nextInt(random, 4, 13);
/*  43 */     if (random.nextInt(12) == 0) {
/*  44 */       totalHeight *= 2;
/*     */     }
/*     */     
/*  47 */     if (!config.planted) {
/*  48 */       int maxHeight = chunkGenerator.getGenDepth();
/*  49 */       if (newOrigin.getY() + totalHeight + 1 >= maxHeight) {
/*  50 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  54 */     boolean isHuge = (!config.planted && random.nextFloat() < 0.06F);
/*     */     
/*  56 */     level.setBlock(origin, Blocks.AIR.defaultBlockState(), 260);
/*     */     
/*  58 */     placeStem(level, random, config, newOrigin, totalHeight, isHuge);
/*  59 */     placeHat(level, random, config, newOrigin, totalHeight, isHuge);
/*     */     
/*  61 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isReplaceable(WorldGenLevel level, BlockPos pos, HugeFungusConfiguration config, boolean checkNonReplaceablePlants) {
/*  65 */     if (level.isStateAtPosition(pos, BlockBehaviour.BlockStateBase::canBeReplaced)) {
/*  66 */       return true;
/*     */     }
/*  68 */     if (checkNonReplaceablePlants)
/*     */     {
/*     */       
/*  71 */       return config.replaceableBlocks.test(level, pos);
/*     */     }
/*  73 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void placeStem(WorldGenLevel level, RandomSource random, HugeFungusConfiguration config, BlockPos surfaceOrigin, int totalHeight, boolean isHuge) {
/*  78 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/*  79 */     BlockState stem = config.stemState;
/*  80 */     int stemRadius = isHuge ? 1 : 0;
/*     */     
/*  82 */     for (int dx = -stemRadius; dx <= stemRadius; dx++) {
/*  83 */       for (int dz = -stemRadius; dz <= stemRadius; dz++) {
/*  84 */         boolean cornerOfHugeStem = (isHuge && Mth.abs(dx) == stemRadius && Mth.abs(dz) == stemRadius);
/*     */         
/*  86 */         for (int dy = 0; dy < totalHeight; dy++) {
/*  87 */           blockPos.setWithOffset(surfaceOrigin, dx, dy, dz);
/*  88 */           if (isReplaceable(level, blockPos, config, true)) {
/*  89 */             if (config.planted) {
/*  90 */               if (!level.getBlockState(blockPos.below()).isAir()) {
/*  91 */                 level.destroyBlock(blockPos, true);
/*     */               }
/*     */               
/*  94 */               level.setBlock(blockPos, stem, 3);
/*     */             }
/*  96 */             else if (cornerOfHugeStem) {
/*  97 */               if (random.nextFloat() < 0.1F) {
/*  98 */                 setBlock(level, blockPos, stem);
/*     */               }
/*     */             } else {
/* 101 */               setBlock(level, blockPos, stem);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void placeHat(WorldGenLevel level, RandomSource random, HugeFungusConfiguration config, BlockPos surfaceOrigin, int totalHeight, boolean isHuge) {
/* 111 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 112 */     boolean placeVines = config.hatState.is(Blocks.NETHER_WART_BLOCK);
/* 113 */     int hatHeight = Math.min(random.nextInt(1 + totalHeight / 3) + 5, totalHeight);
/* 114 */     int hatStartY = totalHeight - hatHeight;
/* 115 */     for (int dy = hatStartY; dy <= totalHeight; dy++) {
/* 116 */       int radius = (dy < totalHeight - random.nextInt(3)) ? 2 : 1;
/* 117 */       if (hatHeight > 8 && dy < hatStartY + 4) {
/* 118 */         radius = 3;
/*     */       }
/*     */       
/* 121 */       if (isHuge) {
/* 122 */         radius++;
/*     */       }
/*     */       
/* 125 */       for (int dx = -radius; dx <= radius; dx++) {
/* 126 */         for (int dz = -radius; dz <= radius; dz++) {
/* 127 */           boolean isEdgeX = (dx == -radius || dx == radius);
/* 128 */           boolean isEdgeZ = (dz == -radius || dz == radius);
/* 129 */           boolean inside = (!isEdgeX && !isEdgeZ && dy != totalHeight);
/* 130 */           boolean corner = (isEdgeX && isEdgeZ);
/* 131 */           boolean isHatBottom = (dy < hatStartY + 3);
/*     */           
/* 133 */           blockPos.setWithOffset(surfaceOrigin, dx, dy, dz);
/* 134 */           if (isReplaceable(level, blockPos, config, false)) {
/* 135 */             if (config.planted && !level.getBlockState(blockPos.below()).isAir()) {
/* 136 */               level.destroyBlock(blockPos, true);
/*     */             }
/*     */             
/* 139 */             if (isHatBottom) {
/* 140 */               if (!inside) {
/* 141 */                 placeHatDropBlock(level, random, blockPos, config.hatState, placeVines);
/*     */               }
/* 143 */             } else if (inside) {
/* 144 */               placeHatBlock(level, random, config, blockPos, 0.1F, 0.2F, placeVines ? 0.1F : 0.0F);
/* 145 */             } else if (corner) {
/* 146 */               placeHatBlock(level, random, config, blockPos, 0.01F, 0.7F, placeVines ? 0.083F : 0.0F);
/*     */             } else {
/* 148 */               placeHatBlock(level, random, config, blockPos, 5.0E-4F, 0.98F, placeVines ? 0.07F : 0.0F);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeHatBlock(LevelAccessor level, RandomSource random, HugeFungusConfiguration config, BlockPos.MutableBlockPos blockPos, float decorBlockProbability, float hatBlockProbability, float vinesProbability) {
/* 157 */     if (random.nextFloat() < decorBlockProbability) {
/* 158 */       setBlock(level, blockPos, config.decorState);
/* 159 */     } else if (random.nextFloat() < hatBlockProbability) {
/* 160 */       setBlock(level, blockPos, config.hatState);
/* 161 */       if (random.nextFloat() < vinesProbability) {
/* 162 */         tryPlaceWeepingVines(blockPos, level, random);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeHatDropBlock(LevelAccessor level, RandomSource random, BlockPos blockPos, BlockState hatState, boolean placeVines) {
/* 168 */     if (level.getBlockState(blockPos.below()).is(hatState.getBlock())) {
/* 169 */       setBlock(level, blockPos, hatState);
/* 170 */     } else if (random.nextFloat() < 0.15D) {
/* 171 */       setBlock(level, blockPos, hatState);
/* 172 */       if (placeVines && random.nextInt(11) == 0) {
/* 173 */         tryPlaceWeepingVines(blockPos, level, random);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void tryPlaceWeepingVines(BlockPos hatBlockPos, LevelAccessor level, RandomSource random) {
/* 179 */     BlockPos.MutableBlockPos placePos = hatBlockPos.mutable().move(Direction.DOWN);
/*     */     
/* 181 */     if (!level.isEmptyBlock(placePos)) {
/*     */       return;
/*     */     }
/*     */     
/* 185 */     int goalVineHeight = Mth.nextInt(random, 1, 5);
/* 186 */     if (random.nextInt(7) == 0) {
/* 187 */       goalVineHeight *= 2;
/*     */     }
/*     */     
/* 190 */     int minVineAge = 23;
/* 191 */     int maxVineAge = 25;
/* 192 */     WeepingVinesFeature.placeWeepingVinesColumn(level, random, placePos, goalVineHeight, 23, 25);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\HugeFungusFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */