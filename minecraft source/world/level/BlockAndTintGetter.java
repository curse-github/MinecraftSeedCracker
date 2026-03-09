/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ 
/*    */ public interface BlockAndTintGetter
/*    */   extends BlockGetter
/*    */ {
/*    */   float getShade(Direction paramDirection, boolean paramBoolean);
/*    */   
/*    */   LevelLightEngine getLightEngine();
/*    */   
/*    */   int getBlockTint(BlockPos paramBlockPos, ColorResolver paramColorResolver);
/*    */   
/* 16 */   default int getBrightness(LightLayer layer, BlockPos pos) { return getLightEngine().getLayerListener(layer).getLightValue(pos); }
/*    */ 
/*    */ 
/*    */   
/* 20 */   default int getRawBrightness(BlockPos pos, int darkening) { return getLightEngine().getRawBrightness(pos, darkening); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   default boolean canSeeSky(BlockPos pos) { return (getBrightness(LightLayer.SKY, pos) >= 15); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\BlockAndTintGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */