/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ScatteredOreFeature
/*    */   extends Feature<OreConfiguration>
/*    */ {
/*    */   private static final int MAX_DIST_FROM_ORIGIN = 7;
/*    */   
/* 18 */   ScatteredOreFeature(Codec<OreConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<OreConfiguration> context) {
/* 26 */     WorldGenLevel level = context.level();
/* 27 */     RandomSource random = context.random();
/* 28 */     OreConfiguration config = (OreConfiguration)context.config();
/* 29 */     BlockPos origin = context.origin();
/* 30 */     int numberOfTries = random.nextInt(config.size + 1);
/* 31 */     BlockPos.MutableBlockPos targetPos = new BlockPos.MutableBlockPos();
/*    */     
/* 33 */     for (int i = 0; i < numberOfTries; i++) {
/*    */       
/* 35 */       offsetTargetPos(targetPos, random, origin, Math.min(i, 7));
/*    */       
/* 37 */       BlockState blockState = level.getBlockState(targetPos);
/* 38 */       for (OreConfiguration.TargetBlockState targetState : config.targetStates) {
/* 39 */         Objects.requireNonNull(level); if (OreFeature.canPlaceOre(blockState, level::getBlockState, random, config, targetState, targetPos)) {
/* 40 */           level.setBlock(targetPos, targetState.state, 2);
/*    */           break;
/*    */         } 
/*    */       } 
/*    */     } 
/* 45 */     return true;
/*    */   }
/*    */   
/*    */   private void offsetTargetPos(BlockPos.MutableBlockPos targetPos, RandomSource random, BlockPos origin, int maxDistFromOriginForThisTry) {
/* 49 */     int xd = getRandomPlacementInOneAxisRelativeToOrigin(random, maxDistFromOriginForThisTry);
/* 50 */     int yd = getRandomPlacementInOneAxisRelativeToOrigin(random, maxDistFromOriginForThisTry);
/* 51 */     int zd = getRandomPlacementInOneAxisRelativeToOrigin(random, maxDistFromOriginForThisTry);
/* 52 */     targetPos.setWithOffset(origin, xd, yd, zd);
/*    */   }
/*    */ 
/*    */   
/* 56 */   private int getRandomPlacementInOneAxisRelativeToOrigin(RandomSource random, int maxDistanceFromOrigin) { return Math.round((random.nextFloat() - random.nextFloat()) * maxDistanceFromOrigin); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\ScatteredOreFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */