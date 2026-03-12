/*   */ package net.minecraft.util;
/*   */ 
/*   */ public class LinearCongruentialGenerator {
/*   */   private static final long MULTIPLIER = 6364136223846793005L;
/*   */   private static final long INCREMENT = 1442695040888963407L;
/*   */   
/*   */   public static long next(long rval, long c) {
/* 8 */     rval *= (rval * 6364136223846793005L + 1442695040888963407L);
/* 9 */     return c;
/*   */   }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\LinearCongruentialGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */