/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public interface BitRandomSource
/*    */   extends RandomSource
/*    */ {
/*    */   public static final float FLOAT_MULTIPLIER = 5.9604645E-8F;
/*    */   public static final double DOUBLE_MULTIPLIER = 1.1102230246251565E-16D;
/*    */   
/*    */   int next(int paramInt);
/*    */   
/* 13 */   default int nextInt() { return next(32); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default int nextInt(int bound) {
/*    */     int modulo;
/*    */     int sample;
/* 21 */     if (bound <= 0) {
/* 22 */       throw new IllegalArgumentException("Bound must be positive");
/*    */     }
/*    */     
/* 25 */     if ((bound & bound - 1) == 0)
/*    */     {
/* 27 */       return (int)(bound * next(31) >> 31);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     do {
/* 33 */       sample = next(31);
/* 34 */       modulo = sample % bound;
/* 35 */     } while (sample - modulo + bound - 1 < 0);
/* 36 */     return modulo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default long nextLong() {
/* 43 */     int upper = next(32);
/* 44 */     int lower = next(32);
/* 45 */     long shifted = upper << 32;
/* 46 */     return shifted + lower;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 51 */   default boolean nextBoolean() { return (next(1) != 0); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   default float nextFloat() { return next(24) * 5.9604645E-8F; }
/*    */ 
/*    */ 
/*    */   
/*    */   default double nextDouble() {
/* 61 */     int upper = next(26);
/* 62 */     int lower = next(27);
/* 63 */     long combined = (upper << 27) + lower;
/* 64 */     return combined * 1.1102230246251565E-16D;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\BitRandomSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */