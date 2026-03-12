/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class GlowstoneFeature
/*    */   extends Feature<NoneFeatureConfiguration>
/*    */ {
/* 15 */   public GlowstoneFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 20 */     WorldGenLevel level = context.level();
/* 21 */     BlockPos origin = context.origin();
/* 22 */     RandomSource random = context.random();
/* 23 */     if (!level.isEmptyBlock(origin)) {
/* 24 */       return false;
/*    */     }
/*    */     
/* 27 */     BlockState aboveState = level.getBlockState(origin.above());
/* 28 */     if (!aboveState.is(Blocks.NETHERRACK) && !aboveState.is(Blocks.BASALT) && !aboveState.is(Blocks.BLACKSTONE)) {
/* 29 */       return false;
/*    */     }
/*    */     
/* 32 */     level.setBlock(origin, Blocks.GLOWSTONE.defaultBlockState(), 2);
/*    */     
/* 34 */     for (int i = 0; i < 1500; i++) {
/* 35 */       BlockPos placePos = origin.offset(random.nextInt(8) - random.nextInt(8), -random.nextInt(12), random.nextInt(8) - random.nextInt(8));
/* 36 */       if (level.getBlockState(placePos).isAir()) {
/*    */ 
/*    */ 
/*    */         
/* 40 */         int neighbours = 0;
/* 41 */         for (Direction direction : Direction.values()) {
/* 42 */           if (level.getBlockState(placePos.relative(direction)).is(Blocks.GLOWSTONE)) {
/* 43 */             neighbours++;
/*    */           }
/*    */           
/* 46 */           if (neighbours > 1) {
/*    */             break;
/*    */           }
/*    */         } 
/*    */         
/* 51 */         if (neighbours == 1) {
/* 52 */           level.setBlock(placePos, Blocks.GLOWSTONE.defaultBlockState(), 2);
/*    */         }
/*    */       } 
/*    */     } 
/* 56 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\GlowstoneFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */