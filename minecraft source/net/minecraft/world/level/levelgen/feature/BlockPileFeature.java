/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
/*    */ 
/*    */ public class BlockPileFeature
/*    */   extends Feature<BlockPileConfiguration>
/*    */ {
/* 16 */   public BlockPileFeature(Codec<BlockPileConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<BlockPileConfiguration> context) {
/* 21 */     BlockPos origin = context.origin();
/* 22 */     WorldGenLevel level = context.level();
/* 23 */     RandomSource random = context.random();
/* 24 */     BlockPileConfiguration config = (BlockPileConfiguration)context.config();
/* 25 */     if (origin.getY() < level.getMinY() + 5) {
/* 26 */       return false;
/*    */     }
/*    */     
/* 29 */     int xr = 2 + random.nextInt(2);
/* 30 */     int zr = 2 + random.nextInt(2);
/*    */     
/* 32 */     for (BlockPos blockPos : BlockPos.betweenClosed(origin.offset(-xr, 0, -zr), origin.offset(xr, 1, zr))) {
/* 33 */       int xd = origin.getX() - blockPos.getX();
/* 34 */       int zd = origin.getZ() - blockPos.getZ();
/* 35 */       if ((xd * xd + zd * zd) <= random.nextFloat() * 10.0F - random.nextFloat() * 6.0F) {
/* 36 */         tryPlaceBlock(level, blockPos, random, config); continue;
/* 37 */       }  if (random.nextFloat() < 0.031D) {
/* 38 */         tryPlaceBlock(level, blockPos, random, config);
/*    */       }
/*    */     } 
/*    */     
/* 42 */     return true;
/*    */   }
/*    */   
/*    */   private boolean mayPlaceOn(LevelAccessor level, BlockPos blockPos, RandomSource random) {
/* 46 */     BlockPos below = blockPos.below();
/* 47 */     BlockState belowState = level.getBlockState(below);
/* 48 */     if (belowState.is(Blocks.DIRT_PATH)) {
/* 49 */       return random.nextBoolean();
/*    */     }
/*    */     
/* 52 */     return belowState.isFaceSturdy(level, below, Direction.UP);
/*    */   }
/*    */   
/*    */   private void tryPlaceBlock(LevelAccessor level, BlockPos blockPos, RandomSource random, BlockPileConfiguration config) {
/* 56 */     if (level.isEmptyBlock(blockPos) && mayPlaceOn(level, blockPos, random))
/* 57 */       level.setBlock(blockPos, config.stateProvider.getState(random, blockPos), 260); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\BlockPileFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */