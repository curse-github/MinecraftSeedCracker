/*    */ package net.minecraft.util;
/*    */ public final class Keyframe<T> extends Record {
/*    */   private final int ticks;
/*    */   private final T value;
/*    */   
/*  6 */   public Keyframe(int ticks, T value) { this.ticks = ticks; this.value = value; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/Keyframe;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/Keyframe;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  6 */     //   0	7	0	this	Lnet/minecraft/util/Keyframe<TT;>; } public int ticks() { return this.ticks; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/Keyframe;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/Keyframe;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/Keyframe<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/Keyframe;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/Keyframe;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  6 */     //   0	8	0	this	Lnet/minecraft/util/Keyframe<TT;>; } public T value() { return (T)this.value; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 11 */   public static <T> Codec<Keyframe<T>> codec(Codec<T> valueCodec) { return RecordCodecBuilder.create(i -> i.group(ExtraCodecs.NON_NEGATIVE_INT
/* 12 */           .fieldOf("ticks").forGetter(Keyframe::ticks), valueCodec
/* 13 */           .fieldOf("value").forGetter(Keyframe::value))
/* 14 */         .apply(i, Keyframe::new)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\Keyframe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */