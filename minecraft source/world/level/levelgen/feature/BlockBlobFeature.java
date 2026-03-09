/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
/*    */ 
/*    */ public class BlockBlobFeature
/*    */   extends Feature<BlockStateConfiguration>
/*    */ {
/* 13 */   public BlockBlobFeature(Codec<BlockStateConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
/* 18 */     BlockPos origin = context.origin();
/* 19 */     WorldGenLevel level = context.level();
/* 20 */     RandomSource random = context.random();
/* 21 */     BlockStateConfiguration config = (BlockStateConfiguration)context.config();
/* 22 */     while (origin.getY() > level.getMinY() + 3) {
/* 23 */       if (!level.isEmptyBlock(origin.below())) {
/* 24 */         BlockState subState = level.getBlockState(origin.below());
/* 25 */         if (isDirt(subState) || isStone(subState)) {
/*    */           break;
/*    */         }
/*    */       } 
/* 29 */       origin = origin.below();
/*    */     } 
/* 31 */     if (origin.getY() <= level.getMinY() + 3) {
/* 32 */       return false;
/*    */     }
/*    */     
/* 35 */     int c = 0;
/* 36 */     while (c < 3) {
/* 37 */       int xr = random.nextInt(2);
/* 38 */       int yr = random.nextInt(2);
/* 39 */       int zr = random.nextInt(2);
/* 40 */       float tr = (xr + yr + zr) * 0.333F + 0.5F;
/*    */       
/* 42 */       for (BlockPos blockPos : BlockPos.betweenClosed(origin.offset(-xr, -yr, -zr), origin.offset(xr, yr, zr))) {
/* 43 */         if (blockPos.distSqr(origin) <= (tr * tr)) {
/* 44 */           level.setBlock(blockPos, config.state, 3);
/*    */         }
/*    */       } 
/*    */       
/* 48 */       origin = origin.offset(-1 + random.nextInt(2), -random.nextInt(2), -1 + random.nextInt(2));
/* 49 */       c++;
/*    */     } 
/*    */     
/* 52 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\BlockBlobFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */