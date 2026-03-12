/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.item.Item;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class EnchantmentDefinition
/*     */   extends Record
/*     */ {
/*     */   private final HolderSet<Item> supportedItems;
/*     */   private final Optional<HolderSet<Item>> primaryItems;
/*     */   private final int weight;
/*     */   private final int maxLevel;
/*     */   private final Enchantment.Cost minCost;
/*     */   private final Enchantment.Cost maxCost;
/*     */   private final int anvilCost;
/*     */   private final List<EquipmentSlotGroup> slots;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #88	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #88	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #88	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*  88 */   public EnchantmentDefinition(HolderSet<Item> supportedItems, Optional<HolderSet<Item>> primaryItems, int weight, int maxLevel, Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, List<EquipmentSlotGroup> slots) { this.supportedItems = supportedItems; this.primaryItems = primaryItems; this.weight = weight; this.maxLevel = maxLevel; this.minCost = minCost; this.maxCost = maxCost; this.anvilCost = anvilCost; this.slots = slots; } public HolderSet<Item> supportedItems() { return this.supportedItems; } public Optional<HolderSet<Item>> primaryItems() { return this.primaryItems; } public int weight() { return this.weight; } public int maxLevel() { return this.maxLevel; } public Enchantment.Cost minCost() { return this.minCost; } public Enchantment.Cost maxCost() { return this.maxCost; } public int anvilCost() { return this.anvilCost; } public List<EquipmentSlotGroup> slots() { return this.slots; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public static final MapCodec<EnchantmentDefinition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/*  99 */         RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("supported_items").forGetter(EnchantmentDefinition::supportedItems), 
/* 100 */         RegistryCodecs.homogeneousList(Registries.ITEM).optionalFieldOf("primary_items").forGetter(EnchantmentDefinition::primaryItems), 
/* 101 */         ExtraCodecs.intRange(1, 1024).fieldOf("weight").forGetter(EnchantmentDefinition::weight), 
/* 102 */         ExtraCodecs.intRange(1, 255).fieldOf("max_level").forGetter(EnchantmentDefinition::maxLevel), Enchantment.Cost.CODEC
/* 103 */         .fieldOf("min_cost").forGetter(EnchantmentDefinition::minCost), Enchantment.Cost.CODEC
/* 104 */         .fieldOf("max_cost").forGetter(EnchantmentDefinition::maxCost), ExtraCodecs.NON_NEGATIVE_INT
/* 105 */         .fieldOf("anvil_cost").forGetter(EnchantmentDefinition::anvilCost), EquipmentSlotGroup.CODEC
/* 106 */         .listOf().fieldOf("slots").forGetter(EnchantmentDefinition::slots))
/* 107 */       .apply(i, EnchantmentDefinition::new));
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\Enchantment$EnchantmentDefinition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */