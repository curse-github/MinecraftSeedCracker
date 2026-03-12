/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ 
/*    */ public final class KeyDispatchDataCodec<A>
/*    */   extends Record
/*    */ {
/*    */   private final MapCodec<A> codec;
/*    */   
/* 10 */   public KeyDispatchDataCodec(MapCodec<A> codec) { this.codec = codec; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/KeyDispatchDataCodec;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/KeyDispatchDataCodec;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 10 */     //   0	7	0	this	Lnet/minecraft/util/KeyDispatchDataCodec<TA;>; } public MapCodec<A> codec() { return this.codec; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/KeyDispatchDataCodec;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/KeyDispatchDataCodec;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/KeyDispatchDataCodec<TA;>; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/KeyDispatchDataCodec;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/KeyDispatchDataCodec;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/util/KeyDispatchDataCodec<TA;>; }
/* 12 */   public static <A> KeyDispatchDataCodec<A> of(MapCodec<A> codec) { return new KeyDispatchDataCodec(codec); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\KeyDispatchDataCodec.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */