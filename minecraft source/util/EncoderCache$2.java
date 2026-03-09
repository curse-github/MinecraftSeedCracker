/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import net.minecraft.nbt.Tag;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Object
/*    */   implements Codec<A>
/*    */ {
/* 34 */   public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) { return codec.decode(ops, input); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
/* 40 */     return ((DataResult)EncoderCache.this.cache.getUnchecked(new EncoderCache.Key(codec, input, ops))).map(value -> {
/*    */           
/* 42 */           if (value instanceof Tag) { Tag tag = (Tag)value;
/* 43 */             return tag.copy(); }
/*    */           
/* 45 */           return value;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\EncoderCache$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */