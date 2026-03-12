/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.ChorusFlowerBlock;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class ChorusPlantFeature
/*    */   extends Feature<NoneFeatureConfiguration> {
/* 13 */   public ChorusPlantFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 18 */     WorldGenLevel level = context.level();
/* 19 */     BlockPos origin = context.origin();
/* 20 */     RandomSource random = context.random();
/* 21 */     if (level.isEmptyBlock(origin) && level.getBlockState(origin.below()).is(Blocks.END_STONE)) {
/* 22 */       ChorusFlowerBlock.generatePlant(level, origin, random, 8);
/* 23 */       return true;
/*    */     } 
/* 25 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\ChorusPlantFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */