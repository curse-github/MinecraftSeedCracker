/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class RandomPatchFeature extends Feature<RandomPatchConfiguration> {
/* 11 */   public RandomPatchFeature(Codec<RandomPatchConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<RandomPatchConfiguration> context) {
/* 16 */     RandomPatchConfiguration config = (RandomPatchConfiguration)context.config();
/* 17 */     RandomSource random = context.random();
/* 18 */     BlockPos origin = context.origin();
/* 19 */     WorldGenLevel level = context.level();
/*    */     
/* 21 */     int placed = 0;
/*    */     
/* 23 */     BlockPos.MutableBlockPos grassPos = new BlockPos.MutableBlockPos();
/* 24 */     int xzBound = config.xzSpread() + 1;
/* 25 */     int yBound = config.ySpread() + 1;
/* 26 */     for (int i = 0; i < config.tries(); i++) {
/* 27 */       grassPos.setWithOffset(origin, random.nextInt(xzBound) - random.nextInt(xzBound), random.nextInt(yBound) - random.nextInt(yBound), random.nextInt(xzBound) - random.nextInt(xzBound));
/* 28 */       if (((PlacedFeature)config.feature().value()).place(level, context.chunkGenerator(), random, grassPos)) {
/* 29 */         placed++;
/*    */       }
/*    */     } 
/*    */     
/* 33 */     return (placed > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\RandomPatchFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */