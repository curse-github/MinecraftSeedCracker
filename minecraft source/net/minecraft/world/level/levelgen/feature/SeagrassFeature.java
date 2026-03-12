/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.TallSeagrassBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*    */ 
/*    */ public class SeagrassFeature
/*    */   extends Feature<ProbabilityFeatureConfiguration>
/*    */ {
/* 17 */   public SeagrassFeature(Codec<ProbabilityFeatureConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> context) {
/* 22 */     boolean placedAny = false;
/* 23 */     RandomSource random = context.random();
/* 24 */     WorldGenLevel level = context.level();
/* 25 */     BlockPos origin = context.origin();
/* 26 */     ProbabilityFeatureConfiguration config = (ProbabilityFeatureConfiguration)context.config();
/* 27 */     int x = random.nextInt(8) - random.nextInt(8);
/* 28 */     int z = random.nextInt(8) - random.nextInt(8);
/* 29 */     int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, origin.getX() + x, origin.getZ() + z);
/* 30 */     BlockPos grassPos = new BlockPos(origin.getX() + x, y, origin.getZ() + z);
/*    */     
/* 32 */     if (level.getBlockState(grassPos).is(Blocks.WATER)) {
/* 33 */       boolean isTall = (random.nextDouble() < config.probability);
/* 34 */       BlockState state = isTall ? Blocks.TALL_SEAGRASS.defaultBlockState() : Blocks.SEAGRASS.defaultBlockState();
/* 35 */       if (state.canSurvive(level, grassPos)) {
/* 36 */         if (isTall) {
/* 37 */           BlockState upperState = (BlockState)state.setValue(TallSeagrassBlock.HALF, DoubleBlockHalf.UPPER);
/* 38 */           BlockPos above = grassPos.above();
/* 39 */           if (level.getBlockState(above).is(Blocks.WATER)) {
/* 40 */             level.setBlock(grassPos, state, 2);
/* 41 */             level.setBlock(above, upperState, 2);
/*    */           } 
/*    */         } else {
/* 44 */           level.setBlock(grassPos, state, 2);
/*    */         } 
/* 46 */         placedAny = true;
/*    */       } 
/*    */     } 
/* 49 */     return placedAny;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\SeagrassFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */