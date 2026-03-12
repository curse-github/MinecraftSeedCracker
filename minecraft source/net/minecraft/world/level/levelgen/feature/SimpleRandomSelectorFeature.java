/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class SimpleRandomSelectorFeature
/*    */   extends Feature<SimpleRandomFeatureConfiguration> {
/* 13 */   public SimpleRandomSelectorFeature(Codec<SimpleRandomFeatureConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<SimpleRandomFeatureConfiguration> context) {
/* 18 */     RandomSource random = context.random();
/* 19 */     SimpleRandomFeatureConfiguration config = (SimpleRandomFeatureConfiguration)context.config();
/* 20 */     WorldGenLevel level = context.level();
/* 21 */     BlockPos origin = context.origin();
/* 22 */     ChunkGenerator chunkGenerator = context.chunkGenerator();
/* 23 */     int index = random.nextInt(config.features.size());
/* 24 */     PlacedFeature feature = (PlacedFeature)config.features.get(index).value();
/* 25 */     return feature.place(level, chunkGenerator, random, origin);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\SimpleRandomSelectorFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */