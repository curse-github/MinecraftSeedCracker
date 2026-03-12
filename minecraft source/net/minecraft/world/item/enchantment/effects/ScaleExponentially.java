/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ 
/*    */ public final class ScaleExponentially extends Record implements EnchantmentValueEffect {
/*    */   private final LevelBasedValue base;
/*    */   private final LevelBasedValue exponent;
/*    */   
/*  8 */   public ScaleExponentially(LevelBasedValue base, LevelBasedValue exponent) { this.base = base; this.exponent = exponent; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially; } public LevelBasedValue base() { return this.base; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ScaleExponentially;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public LevelBasedValue exponent() { return this.exponent; }
/*  9 */   public static final MapCodec<ScaleExponentially> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LevelBasedValue.CODEC
/* 10 */         .fieldOf("base").forGetter(ScaleExponentially::base), LevelBasedValue.CODEC
/* 11 */         .fieldOf("exponent").forGetter(ScaleExponentially::exponent))
/* 12 */       .apply(i, ScaleExponentially::new));
/*    */ 
/*    */ 
/*    */   
/* 16 */   public float process(int level, RandomSource random, float inputValue) { return (float)(inputValue * Math.pow(this.base.calculate(level), this.exponent.calculate(level))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public MapCodec<ScaleExponentially> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\ScaleExponentially.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */