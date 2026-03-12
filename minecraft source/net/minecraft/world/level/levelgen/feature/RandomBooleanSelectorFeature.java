/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class RandomBooleanSelectorFeature extends Feature<RandomBooleanFeatureConfiguration> {
/* 12 */   public RandomBooleanSelectorFeature(Codec<RandomBooleanFeatureConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<RandomBooleanFeatureConfiguration> context) {
/* 17 */     RandomSource random = context.random();
/* 18 */     RandomBooleanFeatureConfiguration config = (RandomBooleanFeatureConfiguration)context.config();
/* 19 */     WorldGenLevel level = context.level();
/* 20 */     ChunkGenerator chunkGenerator = context.chunkGenerator();
/* 21 */     BlockPos origin = context.origin();
/* 22 */     boolean result = random.nextBoolean();
/* 23 */     return ((PlacedFeature)(result ? config.featureTrue : config.featureFalse).value()).place(level, chunkGenerator, random, origin);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\RandomBooleanSelectorFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */