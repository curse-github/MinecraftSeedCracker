/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Cache
/*    */ {
/*    */   private static final int HIGH = 1024;
/*    */   private static final int LOW = -128;
/* 18 */   static final LongTag[] cache = new LongTag[1153];
/*    */   
/*    */   static  {
/* 21 */     for (i = 0; i < cache.length; i++)
/* 22 */       cache[i] = new LongTag((-128 + i)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\LongTag$Cache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */