/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ public final class FloatWithAlpha extends Record {
/*    */   private final float value;
/*    */   private final float alpha;
/*    */   
/*  7 */   public FloatWithAlpha(float value, float alpha) { this.value = value; this.alpha = alpha; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/modifier/FloatWithAlpha;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/FloatWithAlpha; } public float value() { return this.value; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/modifier/FloatWithAlpha;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/FloatWithAlpha; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/modifier/FloatWithAlpha;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/modifier/FloatWithAlpha;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public float alpha() { return this.alpha; }
/*  8 */   private static final Codec<FloatWithAlpha> FULL_CODEC = RecordCodecBuilder.create(i -> i.group(Codec.FLOAT
/*  9 */         .fieldOf("value").forGetter(FloatWithAlpha::value), 
/* 10 */         Codec.floatRange(0.0F, 1.0F).optionalFieldOf("alpha", Float.valueOf(1.0F)).forGetter(FloatWithAlpha::alpha))
/* 11 */       .apply(i, FloatWithAlpha::new));
/*    */   
/* 13 */   public static final Codec<FloatWithAlpha> CODEC = Codec.either(Codec.FLOAT, FULL_CODEC).xmap(either -> 
/* 14 */       (FloatWithAlpha)either.map(FloatWithAlpha::new, ()), parameter -> 
/* 15 */       (parameter.alpha() == 1.0F) ? Either.left(Float.valueOf(parameter.value())) : Either.right(parameter));
/*    */ 
/*    */ 
/*    */   
/* 19 */   public FloatWithAlpha(float value) { this(value, 1.0F); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\modifier\FloatWithAlpha.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */