/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.GrowingPlantHeadBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ 
/*     */ public class WeepingVinesFeature
/*     */   extends Feature<NoneFeatureConfiguration> {
/*  17 */   private static final Direction[] DIRECTIONS = Direction.values();
/*     */ 
/*     */   
/*  20 */   public WeepingVinesFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/*  25 */     WorldGenLevel level = context.level();
/*  26 */     BlockPos origin = context.origin();
/*  27 */     RandomSource random = context.random();
/*  28 */     if (!level.isEmptyBlock(origin)) {
/*  29 */       return false;
/*     */     }
/*     */     
/*  32 */     BlockState stateAbove = level.getBlockState(origin.above());
/*  33 */     if (!stateAbove.is(Blocks.NETHERRACK) && !stateAbove.is(Blocks.NETHER_WART_BLOCK)) {
/*  34 */       return false;
/*     */     }
/*     */     
/*  37 */     placeRoofNetherWart(level, random, origin);
/*  38 */     placeRoofWeepingVines(level, random, origin);
/*     */     
/*  40 */     return true;
/*     */   }
/*     */   
/*     */   private void placeRoofNetherWart(LevelAccessor level, RandomSource random, BlockPos origin) {
/*  44 */     level.setBlock(origin, Blocks.NETHER_WART_BLOCK.defaultBlockState(), 2);
/*     */     
/*  46 */     BlockPos.MutableBlockPos placePos = new BlockPos.MutableBlockPos();
/*  47 */     BlockPos.MutableBlockPos neighbourPos = new BlockPos.MutableBlockPos();
/*     */     
/*  49 */     for (int i = 0; i < 200; i++) {
/*  50 */       placePos.setWithOffset(origin, random.nextInt(6) - random.nextInt(6), random.nextInt(2) - random.nextInt(5), random.nextInt(6) - random.nextInt(6));
/*  51 */       if (level.isEmptyBlock(placePos)) {
/*     */ 
/*     */ 
/*     */         
/*  55 */         int neighbours = 0;
/*  56 */         for (Direction direction : DIRECTIONS) {
/*  57 */           BlockState neighbourBlockState = level.getBlockState(neighbourPos.setWithOffset(placePos, direction));
/*  58 */           if (neighbourBlockState.is(Blocks.NETHERRACK) || neighbourBlockState.is(Blocks.NETHER_WART_BLOCK)) {
/*  59 */             neighbours++;
/*     */           }
/*     */           
/*  62 */           if (neighbours > 1) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */         
/*  67 */         if (neighbours == 1)
/*  68 */           level.setBlock(placePos, Blocks.NETHER_WART_BLOCK.defaultBlockState(), 2); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeRoofWeepingVines(LevelAccessor level, RandomSource random, BlockPos origin) {
/*  74 */     BlockPos.MutableBlockPos placePos = new BlockPos.MutableBlockPos();
/*     */     
/*  76 */     for (int i = 0; i < 100; i++) {
/*  77 */       placePos.setWithOffset(origin, random.nextInt(8) - random.nextInt(8), random.nextInt(2) - random.nextInt(7), random.nextInt(8) - random.nextInt(8));
/*  78 */       if (level.isEmptyBlock(placePos)) {
/*     */ 
/*     */ 
/*     */         
/*  82 */         BlockState stateAbove = level.getBlockState(placePos.above());
/*  83 */         if (stateAbove.is(Blocks.NETHERRACK) || stateAbove.is(Blocks.NETHER_WART_BLOCK)) {
/*     */ 
/*     */ 
/*     */           
/*  87 */           int vineHeight = Mth.nextInt(random, 1, 8);
/*  88 */           if (random.nextInt(6) == 0) {
/*  89 */             vineHeight *= 2;
/*     */           }
/*  91 */           if (random.nextInt(5) == 0) {
/*  92 */             vineHeight = 1;
/*     */           }
/*     */           
/*  95 */           int minVineAge = 17;
/*  96 */           int maxVineAge = 25;
/*  97 */           placeWeepingVinesColumn(level, random, placePos, vineHeight, 17, 25);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } public static void placeWeepingVinesColumn(LevelAccessor level, RandomSource random, BlockPos.MutableBlockPos placePos, int totalHeight, int minAge, int naxAge) {
/* 102 */     for (int height = 0; height <= totalHeight; height++) {
/* 103 */       if (level.isEmptyBlock(placePos)) {
/* 104 */         if (height == totalHeight || !level.isEmptyBlock(placePos.below())) {
/* 105 */           level.setBlock(placePos, (BlockState)Blocks.WEEPING_VINES.defaultBlockState().setValue(GrowingPlantHeadBlock.AGE, Integer.valueOf(Mth.nextInt(random, minAge, naxAge))), 2);
/*     */           break;
/*     */         } 
/* 108 */         level.setBlock(placePos, Blocks.WEEPING_VINES_PLANT.defaultBlockState(), 2);
/*     */       } 
/*     */ 
/*     */       
/* 112 */       placePos.move(Direction.DOWN);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\WeepingVinesFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */