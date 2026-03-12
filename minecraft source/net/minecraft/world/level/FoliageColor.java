/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ public class FoliageColor
/*    */ {
/*    */   public static final int FOLIAGE_EVERGREEN = -10380959;
/*    */   public static final int FOLIAGE_BIRCH = -8345771;
/*    */   public static final int FOLIAGE_DEFAULT = -12012264;
/*    */   public static final int FOLIAGE_MANGROVE = -7158200;
/*  9 */   private static int[] pixels = new int[65536];
/*    */ 
/*    */   
/* 12 */   public static void init(int[] pixels) { FoliageColor.pixels = pixels; }
/*    */ 
/*    */ 
/*    */   
/* 16 */   public static int get(double temp, double rain) { return ColorMapColorUtil.get(temp, rain, pixels, -12012264); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\FoliageColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */