/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ 
/*    */ public final class MultiplyValue extends Record implements EnchantmentValueEffect {
/*  8 */   public MultiplyValue(LevelBasedValue factor) { this.factor = factor; } private final LevelBasedValue factor; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/MultiplyValue;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/MultiplyValue; } public LevelBasedValue factor() { return this.factor; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/MultiplyValue;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/MultiplyValue; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/MultiplyValue;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/MultiplyValue;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 11 */   public static final MapCodec<MultiplyValue> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LevelBasedValue.CODEC
/* 12 */         .fieldOf("factor").forGetter(MultiplyValue::factor))
/* 13 */       .apply(i, MultiplyValue::new));
/*    */ 
/*    */ 
/*    */   
/* 17 */   public float process(int enchantmentLevel, RandomSource random, float inputValue) { return inputValue * this.factor.calculate(enchantmentLevel); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public MapCodec<MultiplyValue> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\MultiplyValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */