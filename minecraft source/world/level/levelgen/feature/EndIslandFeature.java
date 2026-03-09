/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class EndIslandFeature
/*    */   extends Feature<NoneFeatureConfiguration> {
/* 13 */   public EndIslandFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 18 */     WorldGenLevel level = context.level();
/* 19 */     RandomSource random = context.random();
/* 20 */     BlockPos origin = context.origin();
/* 21 */     float size = random.nextInt(3) + 4.0F;
/* 22 */     int y = 0;
/* 23 */     while (size > 0.5F) {
/* 24 */       for (int x = Mth.floor(-size); x <= Mth.ceil(size); x++) {
/* 25 */         for (int z = Mth.floor(-size); z <= Mth.ceil(size); z++) {
/* 26 */           if ((x * x + z * z) <= (size + 1.0F) * (size + 1.0F)) {
/* 27 */             setBlock(level, origin.offset(x, y, z), Blocks.END_STONE.defaultBlockState());
/*    */           }
/*    */         } 
/*    */       } 
/* 31 */       size -= random.nextInt(2) + 0.5F;
/* 32 */       y--;
/*    */     } 
/*    */     
/* 35 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\EndIslandFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */