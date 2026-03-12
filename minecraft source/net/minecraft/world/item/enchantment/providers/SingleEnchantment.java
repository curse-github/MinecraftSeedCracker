/*    */ package net.minecraft.world.item.enchantment.providers;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*    */ 
/*    */ public final class SingleEnchantment extends Record implements EnchantmentProvider {
/*    */   private final Holder<Enchantment> enchantment;
/*    */   private final IntProvider level;
/*    */   
/* 14 */   public SingleEnchantment(Holder<Enchantment> enchantment, IntProvider level) { this.enchantment = enchantment; this.level = level; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment; } public Holder<Enchantment> enchantment() { return this.enchantment; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public IntProvider level() { return this.level; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static final MapCodec<SingleEnchantment> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Enchantment.CODEC
/* 19 */         .fieldOf("enchantment").forGetter(SingleEnchantment::enchantment), IntProvider.CODEC
/* 20 */         .fieldOf("level").forGetter(SingleEnchantment::level))
/* 21 */       .apply(i, SingleEnchantment::new));
/*    */ 
/*    */ 
/*    */   
/* 25 */   public void enchant(ItemStack item, ItemEnchantments.Mutable itemEnchantments, RandomSource random, DifficultyInstance difficulty) { itemEnchantments.upgrade(this.enchantment, Mth.clamp(this.level.sample(random), ((Enchantment)this.enchantment.value()).getMinLevel(), ((Enchantment)this.enchantment.value()).getMaxLevel())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public MapCodec<SingleEnchantment> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\providers\SingleEnchantment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */