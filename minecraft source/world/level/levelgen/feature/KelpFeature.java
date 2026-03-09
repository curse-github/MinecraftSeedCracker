/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.KelpBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class KelpFeature
/*    */   extends Feature<NoneFeatureConfiguration>
/*    */ {
/* 16 */   public KelpFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 21 */     int placed = 0;
/* 22 */     WorldGenLevel level = context.level();
/* 23 */     BlockPos origin = context.origin();
/* 24 */     RandomSource random = context.random();
/* 25 */     int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, origin.getX(), origin.getZ());
/* 26 */     BlockPos kelpPos = new BlockPos(origin.getX(), y, origin.getZ());
/*    */     
/* 28 */     if (level.getBlockState(kelpPos).is(Blocks.WATER)) {
/* 29 */       BlockState stateTop = Blocks.KELP.defaultBlockState();
/* 30 */       BlockState state = Blocks.KELP_PLANT.defaultBlockState();
/* 31 */       int height = 1 + random.nextInt(10);
/* 32 */       for (int h = 0; h <= height; h++) {
/* 33 */         if (level.getBlockState(kelpPos).is(Blocks.WATER) && level.getBlockState(kelpPos.above()).is(Blocks.WATER) && state.canSurvive(level, kelpPos)) {
/* 34 */           if (h == height) {
/* 35 */             level.setBlock(kelpPos, (BlockState)stateTop.setValue(KelpBlock.AGE, Integer.valueOf(random.nextInt(4) + 20)), 2);
/* 36 */             placed++;
/*    */           } else {
/* 38 */             level.setBlock(kelpPos, state, 2);
/*    */           } 
/* 40 */         } else if (h > 0) {
/* 41 */           BlockPos below = kelpPos.below();
/* 42 */           if (stateTop.canSurvive(level, below) && !level.getBlockState(below.below()).is(Blocks.KELP)) {
/* 43 */             level.setBlock(below, (BlockState)stateTop.setValue(KelpBlock.AGE, Integer.valueOf(random.nextInt(4) + 20)), 2);
/* 44 */             placed++;
/*    */           } 
/*    */           
/*    */           break;
/*    */         } 
/* 49 */         kelpPos = kelpPos.above();
/*    */       } 
/*    */     } 
/*    */     
/* 53 */     return (placed > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\KelpFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */