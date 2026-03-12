/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.function.LongSupplier;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface TimeSource
/*    */ {
/*    */   long get(TimeUnit paramTimeUnit);
/*    */   
/*    */   public static interface NanoTimeSource
/*    */     extends LongSupplier, TimeSource {
/* 13 */     default long get(TimeUnit timeUnit) { return timeUnit.convert(getAsLong(), TimeUnit.NANOSECONDS); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\TimeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */