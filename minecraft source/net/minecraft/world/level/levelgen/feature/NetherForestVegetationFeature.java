/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NetherForestVegetationConfig;
/*    */ 
/*    */ public class NetherForestVegetationFeature
/*    */   extends Feature<NetherForestVegetationConfig>
/*    */ {
/* 14 */   public NetherForestVegetationFeature(Codec<NetherForestVegetationConfig> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NetherForestVegetationConfig> context) {
/* 19 */     WorldGenLevel level = context.level();
/* 20 */     BlockPos origin = context.origin();
/* 21 */     BlockState belowState = level.getBlockState(origin.below());
/* 22 */     NetherForestVegetationConfig config = (NetherForestVegetationConfig)context.config();
/* 23 */     RandomSource random = context.random();
/*    */     
/* 25 */     if (!belowState.is(BlockTags.NYLIUM)) {
/* 26 */       return false;
/*    */     }
/*    */     
/* 29 */     int y = origin.getY();
/*    */     
/* 31 */     if (y < level.getMinY() + 1 || y + 1 > level.getMaxY()) {
/* 32 */       return false;
/*    */     }
/*    */     
/* 35 */     int placed = 0;
/*    */ 
/*    */     
/* 38 */     for (int i = 0; i < config.spreadWidth * config.spreadWidth; i++) {
/* 39 */       BlockPos finalPos = origin.offset(random.nextInt(config.spreadWidth) - random.nextInt(config.spreadWidth), random.nextInt(config.spreadHeight) - random.nextInt(config.spreadHeight), random.nextInt(config.spreadWidth) - random.nextInt(config.spreadWidth));
/* 40 */       BlockState state = config.stateProvider.getState(random, finalPos);
/* 41 */       if (level.isEmptyBlock(finalPos) && finalPos.getY() > level.getMinY() && 
/* 42 */         state.canSurvive(level, finalPos)) {
/* 43 */         level.setBlock(finalPos, state, 2);
/* 44 */         placed++;
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 49 */     return (placed > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\NetherForestVegetationFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */