/*     */ package net.minecraft.data.loot.packs;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.loot.LootTableSubProvider;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.item.equipment.trim.ArmorTrim;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterials;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPatterns;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ 
/*     */ public final class VanillaEquipmentLoot
/*     */   extends Record implements LootTableSubProvider {
/*     */   private final HolderLookup.Provider registries;
/*     */   
/*  32 */   public VanillaEquipmentLoot(HolderLookup.Provider registries) { this.registries = registries; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaEquipmentLoot;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #32	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  32 */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaEquipmentLoot; } public HolderLookup.Provider registries() { return this.registries; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaEquipmentLoot;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #32	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaEquipmentLoot; }
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaEquipmentLoot;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #32	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaEquipmentLoot;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/*  35 */     HolderLookup.RegistryLookup<TrimPattern> trimPatterns = this.registries.lookupOrThrow(Registries.TRIM_PATTERN);
/*  36 */     HolderLookup.RegistryLookup<TrimMaterial> trimMaterials = this.registries.lookupOrThrow(Registries.TRIM_MATERIAL);
/*  37 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/*  38 */     ArmorTrim flowTrim = new ArmorTrim(trimMaterials.getOrThrow(TrimMaterials.COPPER), trimPatterns.getOrThrow(TrimPatterns.FLOW));
/*  39 */     ArmorTrim boltTrim = new ArmorTrim(trimMaterials.getOrThrow(TrimMaterials.COPPER), trimPatterns.getOrThrow(TrimPatterns.BOLT));
/*     */     
/*  41 */     output.accept(BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER, 
/*  42 */         LootTable.lootTable()
/*  43 */         .withPool(LootPool.lootPool()
/*  44 */           .setRolls(ConstantValue.exactly(1.0F))
/*  45 */           .add(NestedLootTable.inlineLootTable(trialChamberEquipment(Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, boltTrim, enchantments).build()).setWeight(4))
/*  46 */           .add(NestedLootTable.inlineLootTable(trialChamberEquipment(Items.IRON_HELMET, Items.IRON_CHESTPLATE, flowTrim, enchantments).build()).setWeight(2))
/*  47 */           .add(NestedLootTable.inlineLootTable(trialChamberEquipment(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, flowTrim, enchantments).build()).setWeight(1))));
/*     */ 
/*     */ 
/*     */     
/*  51 */     output.accept(BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_MELEE, 
/*  52 */         LootTable.lootTable()
/*  53 */         .withPool(LootPool.lootPool()
/*  54 */           .setRolls(ConstantValue.exactly(1.0F))
/*  55 */           .add(NestedLootTable.lootTableReference(BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER)))
/*     */         
/*  57 */         .withPool(LootPool.lootPool()
/*  58 */           .setRolls(ConstantValue.exactly(1.0F))
/*  59 */           .add(LootItem.lootTableItem(Items.IRON_SWORD).setWeight(4))
/*  60 */           .add(LootItem.lootTableItem(Items.IRON_SWORD).apply((new SetEnchantmentsFunction.Builder())
/*  61 */               .withEnchantment(enchantments.getOrThrow(Enchantments.SHARPNESS), ConstantValue.exactly(1.0F))))
/*     */           
/*  63 */           .add(LootItem.lootTableItem(Items.IRON_SWORD).apply((new SetEnchantmentsFunction.Builder())
/*  64 */               .withEnchantment(enchantments.getOrThrow(Enchantments.KNOCKBACK), ConstantValue.exactly(1.0F))))
/*     */           
/*  66 */           .add(LootItem.lootTableItem(Items.DIAMOND_SWORD))));
/*     */ 
/*     */ 
/*     */     
/*  70 */     output.accept(BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_RANGED, 
/*  71 */         LootTable.lootTable()
/*  72 */         .withPool(LootPool.lootPool()
/*  73 */           .setRolls(ConstantValue.exactly(1.0F))
/*  74 */           .add(NestedLootTable.lootTableReference(BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER)))
/*     */         
/*  76 */         .withPool(LootPool.lootPool()
/*  77 */           .setRolls(ConstantValue.exactly(1.0F))
/*  78 */           .add(LootItem.lootTableItem(Items.BOW).setWeight(2))
/*  79 */           .add(LootItem.lootTableItem(Items.BOW).apply((new SetEnchantmentsFunction.Builder())
/*  80 */               .withEnchantment(enchantments.getOrThrow(Enchantments.POWER), ConstantValue.exactly(1.0F))))
/*     */           
/*  82 */           .add(LootItem.lootTableItem(Items.BOW).apply((new SetEnchantmentsFunction.Builder())
/*  83 */               .withEnchantment(enchantments.getOrThrow(Enchantments.PUNCH), ConstantValue.exactly(1.0F))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LootTable.Builder trialChamberEquipment(Item helmet, Item chestplate, ArmorTrim trim, HolderLookup.RegistryLookup<Enchantment> enchantments) {
/*  90 */     return LootTable.lootTable()
/*  91 */       .withPool(LootPool.lootPool()
/*  92 */         .setRolls(ConstantValue.exactly(1.0F))
/*  93 */         .when(LootItemRandomChanceCondition.randomChance(0.5F))
/*  94 */         .add(LootItem.lootTableItem(helmet).apply(SetComponentsFunction.setComponent(DataComponents.TRIM, trim)).apply((new SetEnchantmentsFunction.Builder())
/*  95 */             .withEnchantment(enchantments.getOrThrow(Enchantments.PROTECTION), ConstantValue.exactly(4.0F))
/*  96 */             .withEnchantment(enchantments.getOrThrow(Enchantments.PROJECTILE_PROTECTION), ConstantValue.exactly(4.0F))
/*  97 */             .withEnchantment(enchantments.getOrThrow(Enchantments.FIRE_PROTECTION), ConstantValue.exactly(4.0F)))))
/*     */ 
/*     */       
/* 100 */       .withPool(LootPool.lootPool()
/* 101 */         .setRolls(ConstantValue.exactly(1.0F))
/* 102 */         .when(LootItemRandomChanceCondition.randomChance(0.5F))
/* 103 */         .add(LootItem.lootTableItem(chestplate).apply(SetComponentsFunction.setComponent(DataComponents.TRIM, trim)).apply((new SetEnchantmentsFunction.Builder())
/* 104 */             .withEnchantment(enchantments.getOrThrow(Enchantments.PROTECTION), ConstantValue.exactly(4.0F))
/* 105 */             .withEnchantment(enchantments.getOrThrow(Enchantments.PROJECTILE_PROTECTION), ConstantValue.exactly(4.0F))
/* 106 */             .withEnchantment(enchantments.getOrThrow(Enchantments.FIRE_PROTECTION), ConstantValue.exactly(4.0F)))));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\loot\packs\VanillaEquipmentLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */