/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class RandomSelectorFeature extends Feature<RandomFeatureConfiguration> {
/* 12 */   public RandomSelectorFeature(Codec<RandomFeatureConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<RandomFeatureConfiguration> context) {
/* 17 */     RandomFeatureConfiguration config = (RandomFeatureConfiguration)context.config();
/* 18 */     RandomSource random = context.random();
/* 19 */     WorldGenLevel level = context.level();
/* 20 */     ChunkGenerator chunkGenerator = context.chunkGenerator();
/* 21 */     BlockPos origin = context.origin();
/* 22 */     for (WeightedPlacedFeature feature : config.features) {
/* 23 */       if (random.nextFloat() < feature.chance) {
/* 24 */         return feature.place(level, chunkGenerator, random, origin);
/*    */       }
/*    */     } 
/* 27 */     return ((PlacedFeature)config.defaultFeature.value()).place(level, chunkGenerator, random, origin);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\RandomSelectorFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */