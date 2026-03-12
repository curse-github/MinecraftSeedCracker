/*    */ package net.minecraft.world.item.enchantment.providers;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentInstance;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*    */ 
/*    */ public final class EnchantmentsByCost extends Record implements EnchantmentProvider {
/*    */   private final HolderSet<Enchantment> enchantments;
/*    */   private final IntProvider cost;
/*    */   
/* 19 */   public EnchantmentsByCost(HolderSet<Enchantment> enchantments, IntProvider cost) { this.enchantments = enchantments; this.cost = cost; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCost;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 19 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCost; } public HolderSet<Enchantment> enchantments() { return this.enchantments; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCost;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCost; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCost;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCost;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public IntProvider cost() { return this.cost; }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static final MapCodec<EnchantmentsByCost> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 24 */         RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).fieldOf("enchantments").forGetter(EnchantmentsByCost::enchantments), IntProvider.CODEC
/* 25 */         .fieldOf("cost").forGetter(EnchantmentsByCost::cost))
/* 26 */       .apply(i, EnchantmentsByCost::new));
/*    */ 
/*    */   
/*    */   public void enchant(ItemStack item, ItemEnchantments.Mutable itemEnchantments, RandomSource random, DifficultyInstance difficulty) {
/* 30 */     List<EnchantmentInstance> instances = EnchantmentHelper.selectEnchantment(random, item, this.cost.sample(random), this.enchantments.stream());
/* 31 */     for (EnchantmentInstance instance : instances) {
/* 32 */       itemEnchantments.upgrade(instance.enchantment(), instance.level());
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public MapCodec<EnchantmentsByCost> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\providers\EnchantmentsByCost.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */