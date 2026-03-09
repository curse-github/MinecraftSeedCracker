/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class CoralMushroomFeature
/*    */   extends CoralFeature {
/* 13 */   public CoralMushroomFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean placeFeature(LevelAccessor level, RandomSource random, BlockPos origin, BlockState state) {
/* 18 */     int height = random.nextInt(3) + 3;
/* 19 */     int width = random.nextInt(3) + 3;
/* 20 */     int length = random.nextInt(3) + 3;
/*    */     
/* 22 */     int sinkValue = random.nextInt(3) + 1;
/*    */     
/* 24 */     BlockPos.MutableBlockPos mutPos = origin.mutable();
/*    */ 
/*    */ 
/*    */     
/* 28 */     for (int x = 0; x <= width; x++) {
/* 29 */       for (int y = 0; y <= height; y++) {
/* 30 */         for (int z = 0; z <= length; z++) {
/* 31 */           mutPos.set(x + origin.getX(), y + origin.getY(), z + origin.getZ());
/* 32 */           mutPos.move(Direction.DOWN, sinkValue);
/*    */ 
/*    */           
/* 35 */           if ((x != 0 && x != width) || (y != 0 && y != height))
/*    */           {
/*    */ 
/*    */             
/* 39 */             if ((z != 0 && z != length) || (y != 0 && y != height))
/*    */             {
/*    */ 
/*    */               
/* 43 */               if ((x != 0 && x != width) || (z != 0 && z != length))
/*    */               {
/*    */ 
/*    */ 
/*    */                 
/* 48 */                 if (x == 0 || x == width || y == 0 || y == height || z == 0 || z == length)
/*    */                 {
/*    */ 
/*    */ 
/*    */                   
/* 53 */                   if (random.nextFloat() >= 0.1F)
/*    */                   {
/*    */ 
/*    */                     
/* 57 */                     if (!placeCoralBlock(level, random, mutPos, state)); }  }  } 
/*    */             }
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/* 63 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\CoralMushroomFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */