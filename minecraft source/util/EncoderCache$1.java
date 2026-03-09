/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.mojang.serialization.DataResult;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends CacheLoader<EncoderCache.Key<?, ?>, DataResult<?>>
/*    */ {
/*    */   null(EncoderCache this$0) {}
/*    */   
/* 25 */   public DataResult<?> load(EncoderCache.Key<?, ?> key) { return key.resolve(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\EncoderCache$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */