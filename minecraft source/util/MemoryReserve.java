/*    */ package net.minecraft.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MemoryReserve
/*    */ {
/*    */   private static byte[] reserve;
/*    */   
/* 10 */   public static void allocate() { reserve = new byte[10485760]; }
/*    */ 
/*    */   
/*    */   public static void release() {
/* 14 */     if (reserve != null) {
/* 15 */       reserve = null;
/*    */       try {
/* 17 */         System.gc();
/* 18 */         System.gc();
/* 19 */         System.gc();
/* 20 */       } catch (Throwable throwable) {}
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\MemoryReserve.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */