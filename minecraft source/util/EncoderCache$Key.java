/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
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
/*    */ final class Key<A, T>
/*    */   extends Record
/*    */ {
/*    */   private final Codec<A> codec;
/*    */   private final A value;
/*    */   private final DynamicOps<T> ops;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/EncoderCache$Key;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/EncoderCache$Key;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/EncoderCache$Key<TA;TT;>; }
/*    */   
/* 51 */   private Key(Codec<A> codec, A value, DynamicOps<T> ops) { this.codec = codec; this.value = value; this.ops = ops; } public Codec<A> codec() { return this.codec; } public A value() { return (A)this.value; } public DynamicOps<T> ops() { return this.ops; }
/*    */   
/* 53 */   public DataResult<T> resolve() { return this.codec.encodeStart(this.ops, this.value); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 58 */     if (this == obj) {
/* 59 */       return true;
/*    */     }
/* 61 */     if (obj instanceof Key) { Key<?, ?> key = (Key)obj;
/* 62 */       return (this.codec == key.codec && this.value.equals(key.value) && this.ops.equals(key.ops)); }
/*    */     
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 69 */     result = System.identityHashCode(this.codec);
/* 70 */     result = 31 * result + this.value.hashCode();
/* 71 */     return 31 * result + this.ops.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\EncoderCache$Key.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */