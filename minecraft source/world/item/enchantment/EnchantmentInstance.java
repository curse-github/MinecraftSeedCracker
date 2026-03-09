/*   */ package net.minecraft.world.item.enchantment;public final class EnchantmentInstance extends Record {
/*   */   private final Holder<Enchantment> enchantment;
/*   */   private final int level;
/*   */   
/* 5 */   public EnchantmentInstance(Holder<Enchantment> enchantment, int level) { this.enchantment = enchantment; this.level = level; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/EnchantmentInstance;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 5 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/EnchantmentInstance; } public Holder<Enchantment> enchantment() { return this.enchantment; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/EnchantmentInstance;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/EnchantmentInstance; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/EnchantmentInstance;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/EnchantmentInstance;
/* 5 */     //   0	8	1	o	Ljava/lang/Object; } public int level() { return this.level; }
/*   */   
/* 7 */   public int weight() { return ((Enchantment)enchantment().value()).getWeight(); }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\EnchantmentInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */