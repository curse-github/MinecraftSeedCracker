/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ public class GrassColor {
/*  4 */   private static int[] pixels = new int[65536];
/*    */ 
/*    */   
/*  7 */   public static void init(int[] pixels) { GrassColor.pixels = pixels; }
/*    */ 
/*    */ 
/*    */   
/* 11 */   public static int get(double temp, double rain) { return ColorMapColorUtil.get(temp, rain, pixels, -65281); }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public static int getDefaultColor() { return get(0.5D, 1.0D); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\GrassColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */