/*    */ package net.minecraft.world.level.levelgen.synth;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoiseUtils
/*    */ {
/* 14 */   public static double biasTowardsExtreme(double noise, double factor) { return noise + Math.sin(Math.PI * noise) * factor / Math.PI; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static void parityNoiseOctaveConfigString(StringBuilder sb, double xo, double yo, double zo, byte[] p) { sb.append(String.format(Locale.ROOT, "xo=%.3f, yo=%.3f, zo=%.3f, p0=%d, p255=%d", new Object[] { Float.valueOf((float)xo), Float.valueOf((float)yo), Float.valueOf((float)zo), Byte.valueOf(p[0]), Byte.valueOf(p[255]) })); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static void parityNoiseOctaveConfigString(StringBuilder sb, double xo, double yo, double zo, int[] p) { sb.append(String.format(Locale.ROOT, "xo=%.3f, yo=%.3f, zo=%.3f, p0=%d, p255=%d", new Object[] { Float.valueOf((float)xo), Float.valueOf((float)yo), Float.valueOf((float)zo), Integer.valueOf(p[0]), Integer.valueOf(p[255]) })); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\synth\NoiseUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */