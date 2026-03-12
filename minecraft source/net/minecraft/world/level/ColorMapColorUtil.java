/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ public interface ColorMapColorUtil
/*    */ {
/*    */   static int get(double temp, double rain, int[] pixels, int defaultMapColor) {
/*  6 */     rain *= temp;
/*  7 */     int x = (int)((1.0D - temp) * 255.0D);
/*  8 */     int y = (int)((1.0D - rain) * 255.0D);
/*  9 */     int index = y << 8 | x;
/* 10 */     if (index >= pixels.length) {
/* 11 */       return defaultMapColor;
/*    */     }
/* 13 */     return pixels[index];
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\ColorMapColorUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */