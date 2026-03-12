/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
/*    */ 
/*    */ public class BlockColumnFeature
/*    */   extends Feature<BlockColumnConfiguration>
/*    */ {
/* 12 */   public BlockColumnFeature(Codec<BlockColumnConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<BlockColumnConfiguration> context) {
/* 17 */     WorldGenLevel level = context.level();
/* 18 */     BlockColumnConfiguration config = (BlockColumnConfiguration)context.config();
/* 19 */     RandomSource random = context.random();
/*    */     
/* 21 */     int layerCount = config.layers().size();
/* 22 */     int[] layerHeights = new int[layerCount];
/* 23 */     int totalHeight = 0;
/* 24 */     for (int i = 0; i < layerCount; i++) {
/* 25 */       layerHeights[i] = ((BlockColumnConfiguration.Layer)config.layers().get(i)).height().sample(random);
/* 26 */       totalHeight += layerHeights[i];
/*    */     } 
/* 28 */     if (totalHeight == 0) {
/* 29 */       return false;
/*    */     }
/*    */     
/* 32 */     BlockPos.MutableBlockPos placePos = context.origin().mutable();
/* 33 */     BlockPos.MutableBlockPos nextPos = placePos.mutable().move(config.direction());
/* 34 */     for (int y = 0; y < totalHeight; y++) {
/* 35 */       if (!config.allowedPlacement().test(level, nextPos)) {
/* 36 */         truncate(layerHeights, totalHeight, y, config.prioritizeTip());
/*    */         break;
/*    */       } 
/* 39 */       nextPos.move(config.direction());
/*    */     } 
/*    */     
/* 42 */     for (int i = 0; i < layerCount; i++) {
/* 43 */       int count = layerHeights[i];
/* 44 */       if (count != 0) {
/*    */ 
/*    */ 
/*    */         
/* 48 */         BlockColumnConfiguration.Layer layer = (BlockColumnConfiguration.Layer)config.layers().get(i);
/* 49 */         for (int y = 0; y < count; y++) {
/* 50 */           level.setBlock(placePos, layer.state().getState(random, placePos), 2);
/* 51 */           placePos.move(config.direction());
/*    */         } 
/*    */       } 
/* 54 */     }  return true;
/*    */   }
/*    */ 
/*    */   
/*    */   private static void truncate(int[] layerHeights, int totalHeight, int newHeight, boolean prioritizeTip) {
/* 59 */     int amountToRemove = totalHeight - newHeight;
/* 60 */     int direction = prioritizeTip ? 1 : -1;
/* 61 */     int start = prioritizeTip ? 0 : (layerHeights.length - 1);
/* 62 */     int end = prioritizeTip ? layerHeights.length : -1;
/*    */     int i;
/* 64 */     for (i = start; i != end && amountToRemove > 0; i += direction) {
/* 65 */       int thisLayer = layerHeights[i];
/* 66 */       int toRemoveFromLayer = Math.min(thisLayer, amountToRemove);
/* 67 */       amountToRemove -= toRemoveFromLayer;
/* 68 */       layerHeights[i] = layerHeights[i] - toRemoveFromLayer;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\BlockColumnFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */