/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ public class DryFoliageColor
/*    */ {
/*    */   public static final int FOLIAGE_DRY_DEFAULT = -10732494;
/*  6 */   private static int[] pixels = new int[65536];
/*    */ 
/*    */   
/*  9 */   public static void init(int[] pixels) { DryFoliageColor.pixels = pixels; }
/*    */ 
/*    */ 
/*    */   
/* 13 */   public static int get(double temp, double rain) { return ColorMapColorUtil.get(temp, rain, pixels, -10732494); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\DryFoliageColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */