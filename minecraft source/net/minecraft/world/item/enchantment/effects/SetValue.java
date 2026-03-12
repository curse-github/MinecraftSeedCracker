/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ 
/*    */ public final class SetValue extends Record implements EnchantmentValueEffect {
/*    */   private final LevelBasedValue value;
/*    */   
/*  8 */   public SetValue(LevelBasedValue value) { this.value = value; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/SetValue;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SetValue; } public LevelBasedValue value() { return this.value; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/SetValue;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SetValue; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/SetValue;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/SetValue;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*  9 */   public static final MapCodec<SetValue> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LevelBasedValue.CODEC
/* 10 */         .fieldOf("value").forGetter(SetValue::value))
/* 11 */       .apply(i, SetValue::new));
/*    */ 
/*    */ 
/*    */   
/* 15 */   public float process(int enchantmentLevel, RandomSource random, float inputValue) { return this.value.calculate(enchantmentLevel); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public MapCodec<SetValue> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\SetValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */