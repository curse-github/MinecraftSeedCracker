/*    */ package net.minecraft.util;
/*    */ public final class Brightness extends Record {
/*    */   private final int block;
/*    */   private final int sky;
/*    */   
/*  6 */   public Brightness(int block, int sky) { this.block = block; this.sky = sky; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/Brightness;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  6 */     //   0	7	0	this	Lnet/minecraft/util/Brightness; } public int block() { return this.block; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/Brightness;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/Brightness; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/Brightness;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/Brightness;
/*  6 */     //   0	8	1	o	Ljava/lang/Object; } public int sky() { return this.sky; }
/*  7 */   public static final Codec<Integer> LIGHT_VALUE_CODEC = ExtraCodecs.intRange(0, 15);
/*    */   
/*  9 */   public static final Codec<Brightness> CODEC = RecordCodecBuilder.create(i -> i.group(LIGHT_VALUE_CODEC
/* 10 */         .fieldOf("block").forGetter(Brightness::block), LIGHT_VALUE_CODEC
/* 11 */         .fieldOf("sky").forGetter(Brightness::sky))
/* 12 */       .apply(i, Brightness::new));
/*    */   
/* 14 */   public static final Brightness FULL_BRIGHT = new Brightness(15, 15);
/*    */ 
/*    */   
/* 17 */   public static int pack(int block, int sky) { return block << 4 | sky << 20; }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public int pack() { return pack(this.block, this.sky); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static int block(int packed) { return packed >> 4 & 0xFFFF; }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static int sky(int packed) { return packed >> 20 & 0xFFFF; }
/*    */ 
/*    */   
/*    */   public static Brightness unpack(int packed) {
/* 33 */     return new Brightness(
/* 34 */         block(packed), 
/* 35 */         sky(packed));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\Brightness.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */