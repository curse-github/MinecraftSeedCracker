/*    */ package net.minecraft.world.item.enchantment.providers;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentInstance;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*    */ 
/*    */ public final class EnchantmentsByCostWithDifficulty extends Record implements EnchantmentProvider {
/*    */   private final HolderSet<Enchantment> enchantments;
/*    */   private final int minCost;
/*    */   private final int maxCostSpan;
/*    */   public static final int MAX_ALLOWED_VALUE_PART = 10000;
/*    */   
/* 20 */   public EnchantmentsByCostWithDifficulty(HolderSet<Enchantment> enchantments, int minCost, int maxCostSpan) { this.enchantments = enchantments; this.minCost = minCost; this.maxCostSpan = maxCostSpan; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCostWithDifficulty;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 20 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCostWithDifficulty; } public HolderSet<Enchantment> enchantments() { return this.enchantments; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCostWithDifficulty;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCostWithDifficulty; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCostWithDifficulty;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCostWithDifficulty;
/* 20 */     //   0	8	1	o	Ljava/lang/Object; } public int minCost() { return this.minCost; } public int maxCostSpan() { return this.maxCostSpan; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public static final MapCodec<EnchantmentsByCostWithDifficulty> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 29 */         RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).fieldOf("enchantments").forGetter(EnchantmentsByCostWithDifficulty::enchantments), 
/* 30 */         ExtraCodecs.intRange(1, 10000).fieldOf("min_cost").forGetter(EnchantmentsByCostWithDifficulty::minCost), 
/* 31 */         ExtraCodecs.intRange(0, 10000).fieldOf("max_cost_span").forGetter(EnchantmentsByCostWithDifficulty::maxCostSpan))
/* 32 */       .apply(i, EnchantmentsByCostWithDifficulty::new));
/*    */ 
/*    */   
/*    */   public void enchant(ItemStack item, ItemEnchantments.Mutable itemEnchantments, RandomSource random, DifficultyInstance difficulty) {
/* 36 */     float difficultyModifier = difficulty.getSpecialMultiplier();
/* 37 */     int cost = Mth.randomBetweenInclusive(random, this.minCost, this.minCost + (int)(difficultyModifier * this.maxCostSpan));
/* 38 */     List<EnchantmentInstance> instances = EnchantmentHelper.selectEnchantment(random, item, cost, this.enchantments.stream());
/* 39 */     for (EnchantmentInstance instance : instances) {
/* 40 */       itemEnchantments.upgrade(instance.enchantment(), instance.level());
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public MapCodec<EnchantmentsByCostWithDifficulty> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\providers\EnchantmentsByCostWithDifficulty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */