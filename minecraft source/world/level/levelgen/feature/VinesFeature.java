/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.VineBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class VinesFeature
/*    */   extends Feature<NoneFeatureConfiguration> {
/* 14 */   public VinesFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 31 */     WorldGenLevel level = context.level();
/* 32 */     BlockPos origin = context.origin();
/* 33 */     context.config();
/* 34 */     if (!level.isEmptyBlock(origin)) {
/* 35 */       return false;
/*    */     }
/*    */     
/* 38 */     for (Direction direction : Direction.values()) {
/* 39 */       if (direction != Direction.DOWN)
/*    */       {
/*    */ 
/*    */         
/* 43 */         if (VineBlock.isAcceptableNeighbour(level, origin.relative(direction), direction)) {
/* 44 */           level.setBlock(origin, (BlockState)Blocks.VINE.defaultBlockState().setValue(VineBlock.getPropertyForFace(direction), Boolean.valueOf(true)), 2);
/* 45 */           return true;
/*    */         }  } 
/*    */     } 
/* 48 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\VinesFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */