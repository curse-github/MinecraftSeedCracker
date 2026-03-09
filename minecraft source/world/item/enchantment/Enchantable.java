/*    */ package net.minecraft.world.item.enchantment;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class Enchantable extends Record {
/*    */   private final int value;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/Enchantable;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantable; }
/*    */   
/* 10 */   public int value() { return this.value; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/Enchantable;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantable; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/Enchantable;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/Enchantable;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 11 */   public static final Codec<Enchantable> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.POSITIVE_INT
/* 12 */         .fieldOf("value").forGetter(Enchantable::value))
/* 13 */       .apply(i, Enchantable::new));
/*    */   
/* 15 */   public static final StreamCodec<ByteBuf, Enchantable> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, Enchantable::value, Enchantable::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Enchantable(int value) {
/* 21 */     if (value <= 0)
/* 22 */       throw new IllegalArgumentException("Enchantment value must be positive, but was " + value); 
/*    */     this.value = value;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\Enchantable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */