/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
/*    */ 
/*    */ public abstract class AbstractHugeMushroomFeature
/*    */   extends Feature<HugeMushroomFeatureConfiguration> {
/* 15 */   public AbstractHugeMushroomFeature(Codec<HugeMushroomFeatureConfiguration> codec) { super(codec); }
/*    */ 
/*    */   
/*    */   protected void placeTrunk(LevelAccessor level, RandomSource random, BlockPos origin, HugeMushroomFeatureConfiguration config, int treeHeight, BlockPos.MutableBlockPos blockPos) {
/* 19 */     for (int dy = 0; dy < treeHeight; dy++) {
/* 20 */       blockPos.set(origin).move(Direction.UP, dy);
/* 21 */       placeMushroomBlock(level, blockPos, config.stemProvider.getState(random, origin));
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void placeMushroomBlock(LevelAccessor level, BlockPos.MutableBlockPos blockPos, BlockState newState) {
/* 26 */     BlockState currentState = level.getBlockState(blockPos);
/* 27 */     if (currentState.isAir() || currentState.is(BlockTags.REPLACEABLE_BY_MUSHROOMS)) {
/* 28 */       setBlock(level, blockPos, newState);
/*    */     }
/*    */   }
/*    */   
/*    */   protected int getTreeHeight(RandomSource random) {
/* 33 */     int treeHeight = random.nextInt(3) + 4;
/* 34 */     if (random.nextInt(12) == 0) {
/* 35 */       treeHeight *= 2;
/*    */     }
/* 37 */     return treeHeight;
/*    */   }
/*    */   
/*    */   protected boolean isValidPosition(LevelAccessor level, BlockPos origin, int treeHeight, BlockPos.MutableBlockPos blockPos, HugeMushroomFeatureConfiguration config) {
/* 41 */     int y = origin.getY();
/* 42 */     if (y < level.getMinY() + 1 || y + treeHeight + 1 > level.getMaxY()) {
/* 43 */       return false;
/*    */     }
/*    */     
/* 46 */     BlockState belowState = level.getBlockState(origin.below());
/* 47 */     if (!isDirt(belowState) && !belowState.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
/* 48 */       return false;
/*    */     }
/*    */     
/* 51 */     for (int dy = 0; dy <= treeHeight; dy++) {
/* 52 */       int radius = getTreeRadiusForHeight(-1, -1, config.foliageRadius, dy);
/* 53 */       for (int dx = -radius; dx <= radius; dx++) {
/* 54 */         for (int dz = -radius; dz <= radius; dz++) {
/* 55 */           BlockState state = level.getBlockState(blockPos.setWithOffset(origin, dx, dy, dz));
/* 56 */           if (!state.isAir() && !state.is(BlockTags.LEAVES)) {
/* 57 */             return false;
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/* 62 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<HugeMushroomFeatureConfiguration> context) {
/* 67 */     WorldGenLevel level = context.level();
/* 68 */     BlockPos origin = context.origin();
/* 69 */     RandomSource random = context.random();
/* 70 */     HugeMushroomFeatureConfiguration config = (HugeMushroomFeatureConfiguration)context.config();
/* 71 */     int treeHeight = getTreeHeight(random);
/*    */     
/* 73 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 74 */     if (!isValidPosition(level, origin, treeHeight, blockPos, config)) {
/* 75 */       return false;
/*    */     }
/*    */     
/* 78 */     makeCap(level, random, origin, treeHeight, blockPos, config);
/* 79 */     placeTrunk(level, random, origin, config, treeHeight, blockPos);
/* 80 */     return true;
/*    */   }
/*    */   
/*    */   protected abstract int getTreeRadiusForHeight(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*    */   
/*    */   protected abstract void makeCap(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos, int paramInt, BlockPos.MutableBlockPos paramMutableBlockPos, HugeMushroomFeatureConfiguration paramHugeMushroomFeatureConfiguration);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\AbstractHugeMushroomFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */