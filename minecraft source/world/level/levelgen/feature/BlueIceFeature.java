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
/*    */ public class BlueIceFeature
/*    */   extends Feature<NoneFeatureConfiguration>
/*    */ {
/* 15 */   public BlueIceFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 20 */     BlockPos origin = context.origin();
/* 21 */     WorldGenLevel level = context.level();
/* 22 */     RandomSource random = context.random();
/* 23 */     if (origin.getY() > level.getSeaLevel() - 1) {
/* 24 */       return false;
/*    */     }
/* 26 */     if (!level.getBlockState(origin).is(Blocks.WATER) && !level.getBlockState(origin.below()).is(Blocks.WATER)) {
/* 27 */       return false;
/*    */     }
/*    */     
/* 30 */     boolean foundPackedIce = false;
/* 31 */     for (Direction direction : Direction.values()) {
/* 32 */       if (direction != Direction.DOWN)
/*    */       {
/*    */         
/* 35 */         if (level.getBlockState(origin.relative(direction)).is(Blocks.PACKED_ICE)) {
/* 36 */           foundPackedIce = true;
/*    */           break;
/*    */         }  } 
/*    */     } 
/* 40 */     if (!foundPackedIce) {
/* 41 */       return false;
/*    */     }
/*    */     
/* 44 */     level.setBlock(origin, Blocks.BLUE_ICE.defaultBlockState(), 2);
/*    */     
/* 46 */     for (int i = 0; i < 200; i++) {
/* 47 */       int yOff = random.nextInt(5) - random.nextInt(6);
/* 48 */       int xzDiff = 3;
/* 49 */       if (yOff < 2) {
/* 50 */         xzDiff += yOff / 2;
/*    */       }
/* 52 */       if (xzDiff >= 1) {
/*    */ 
/*    */ 
/*    */         
/* 56 */         BlockPos placePos = origin.offset(random.nextInt(xzDiff) - random.nextInt(xzDiff), yOff, random.nextInt(xzDiff) - random.nextInt(xzDiff));
/* 57 */         BlockState placeState = level.getBlockState(placePos);
/* 58 */         if (placeState.isAir() || placeState.is(Blocks.WATER) || placeState.is(Blocks.PACKED_ICE) || placeState.is(Blocks.ICE))
/*    */         {
/*    */ 
/*    */           
/* 62 */           for (Direction direction : Direction.values()) {
/* 63 */             BlockState relativeBlockState = level.getBlockState(placePos.relative(direction));
/* 64 */             if (relativeBlockState.is(Blocks.BLUE_ICE)) {
/* 65 */               level.setBlock(placePos, Blocks.BLUE_ICE.defaultBlockState(), 2);
/*    */               break;
/*    */             } 
/*    */           }  } 
/*    */       } 
/*    */     } 
/* 71 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\BlueIceFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */