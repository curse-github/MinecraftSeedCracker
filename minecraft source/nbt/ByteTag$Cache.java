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
/* 16 */   private static final ByteTag[] cache = new ByteTag[256];
/*    */   
/*    */   static  {
/* 19 */     for (i = 0; i < cache.length; i++)
/* 20 */       cache[i] = new ByteTag((byte)(i - 128)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\ByteTag$Cache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */