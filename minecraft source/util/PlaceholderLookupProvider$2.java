/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.JavaOps;
/*    */ import net.minecraft.core.HolderLookup;
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
/*    */   implements RegistryContextSwapper
/*    */ {
/* 53 */   public <T> DataResult<T> swapTo(Codec<T> codec, T value, HolderLookup.Provider newContext) { return codec
/* 54 */       .encodeStart(PlaceholderLookupProvider.this.createSerializationContext(JavaOps.INSTANCE), value)
/* 55 */       .flatMap(v -> codec.parse(newContext.createSerializationContext(JavaOps.INSTANCE), v)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\PlaceholderLookupProvider$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */