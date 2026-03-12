/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.codecs.PrimitiveCodec;
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
/*    */   implements PrimitiveCodec<String>
/*    */ {
/*    */   public <T> DataResult<String> read(DynamicOps<T> ops, T input) {
/* 28 */     return ops
/* 29 */       .getStringValue(input)
/* 30 */       .map(NamespacedSchema::ensureNamespaced);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public <T> T write(DynamicOps<T> ops, String value) { return (T)ops.createString(value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public String toString() { return "NamespacedString"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\NamespacedSchema$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */