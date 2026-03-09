/*     */ package net.minecraft.data.loot.packs;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.loot.LootTableSubProvider;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.InstrumentTags;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetInstrumentFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*     */ 
/*     */ public final class TradeRebalanceChestLoot
/*     */   extends Record implements LootTableSubProvider {
/*     */   private final HolderLookup.Provider registries;
/*     */   
/*  32 */   public TradeRebalanceChestLoot(HolderLookup.Provider registries) { this.registries = registries; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/TradeRebalanceChestLoot;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #32	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  32 */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/TradeRebalanceChestLoot; } public HolderLookup.Provider registries() { return this.registries; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/TradeRebalanceChestLoot;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #32	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/TradeRebalanceChestLoot; }
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/TradeRebalanceChestLoot;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #32	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/TradeRebalanceChestLoot;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/*  35 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/*  36 */     output.accept(BuiltInLootTables.ABANDONED_MINESHAFT, 
/*  37 */         LootTable.lootTable()
/*  38 */         .withPool(LootPool.lootPool()
/*  39 */           .setRolls(ConstantValue.exactly(1.0F))
/*  40 */           .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(20))
/*  41 */           .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE))
/*  42 */           .add(LootItem.lootTableItem(Items.NAME_TAG).setWeight(30))
/*  43 */           .add(LootItem.lootTableItem(Items.BOOK).setWeight(10).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  44 */           .add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(5))
/*  45 */           .add(EmptyLootItem.emptyItem().setWeight(5)))
/*     */         
/*  47 */         .withPool(LootPool.lootPool()
/*  48 */           .setRolls(UniformGenerator.between(2.0F, 4.0F))
/*  49 */           .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/*  50 */           .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  51 */           .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
/*  52 */           .add(LootItem.lootTableItem(Items.LAPIS_LAZULI).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
/*  53 */           .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
/*  54 */           .add(LootItem.lootTableItem(Items.COAL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F))))
/*  55 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  56 */           .add(LootItem.lootTableItem(Items.GLOW_BERRIES).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F))))
/*  57 */           .add(LootItem.lootTableItem(Items.MELON_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
/*  58 */           .add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
/*  59 */           .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))))
/*     */         
/*  61 */         .withPool(LootPool.lootPool()
/*  62 */           .setRolls(ConstantValue.exactly(3.0F))
/*  63 */           .add(LootItem.lootTableItem(Blocks.RAIL).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))))
/*  64 */           .add(LootItem.lootTableItem(Blocks.POWERED_RAIL).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  65 */           .add(LootItem.lootTableItem(Blocks.DETECTOR_RAIL).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  66 */           .add(LootItem.lootTableItem(Blocks.ACTIVATOR_RAIL).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  67 */           .add(LootItem.lootTableItem(Blocks.TORCH).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F)))))
/*     */         
/*  69 */         .withPool(LootPool.lootPool()
/*  70 */           .setRolls(ConstantValue.exactly(1.0F))
/*  71 */           .add(EmptyLootItem.emptyItem().setWeight(4))
/*  72 */           .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply((new EnchantRandomlyFunction.Builder()).withEnchantment(enchantments.getOrThrow(Enchantments.EFFICIENCY))))));
/*     */ 
/*     */ 
/*     */     
/*  76 */     output.accept(BuiltInLootTables.ANCIENT_CITY, ancientCityLootTable());
/*  77 */     output.accept(BuiltInLootTables.DESERT_PYRAMID, desertPyramidLootTable());
/*  78 */     output.accept(BuiltInLootTables.JUNGLE_TEMPLE, jungleTempleLootTable());
/*  79 */     output.accept(BuiltInLootTables.PILLAGER_OUTPOST, pillagerOutpostLootTable());
/*     */   }
/*     */   
/*     */   public LootTable.Builder pillagerOutpostLootTable() {
/*  83 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/*  84 */     return LootTable.lootTable()
/*  85 */       .withPool(LootPool.lootPool()
/*  86 */         .setRolls(UniformGenerator.between(0.0F, 1.0F))
/*  87 */         .add(LootItem.lootTableItem(Items.CROSSBOW)))
/*     */       
/*  89 */       .withPool(LootPool.lootPool()
/*  90 */         .setRolls(UniformGenerator.between(2.0F, 3.0F))
/*  91 */         .add(LootItem.lootTableItem(Items.WHEAT).setWeight(7).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 5.0F))))
/*  92 */         .add(LootItem.lootTableItem(Items.POTATO).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))))
/*  93 */         .add(LootItem.lootTableItem(Items.CARROT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 5.0F)))))
/*     */       
/*  95 */       .withPool(LootPool.lootPool()
/*  96 */         .setRolls(UniformGenerator.between(1.0F, 3.0F))
/*  97 */         .add(LootItem.lootTableItem(Blocks.DARK_OAK_LOG).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))))
/*     */       
/*  99 */       .withPool(LootPool.lootPool()
/* 100 */         .setRolls(UniformGenerator.between(2.0F, 3.0F))
/* 101 */         .add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).setWeight(7))
/* 102 */         .add(LootItem.lootTableItem(Items.STRING).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 6.0F))))
/* 103 */         .add(LootItem.lootTableItem(Items.ARROW).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F))))
/* 104 */         .add(LootItem.lootTableItem(Items.TRIPWIRE_HOOK).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 105 */         .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 106 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))))
/*     */       
/* 108 */       .withPool(LootPool.lootPool()
/* 109 */         .setRolls(UniformGenerator.between(0.0F, 1.0F))
/* 110 */         .add(LootItem.lootTableItem(Items.GOAT_HORN)).apply(SetInstrumentFunction.setInstrumentOptions(InstrumentTags.REGULAR_GOAT_HORNS)))
/*     */       
/* 112 */       .withPool(LootPool.lootPool()
/* 113 */         .setRolls(ConstantValue.exactly(1.0F))
/* 114 */         .add(EmptyLootItem.emptyItem().setWeight(3))
/* 115 */         .add(LootItem.lootTableItem(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))
/*     */       
/* 117 */       .withPool(LootPool.lootPool()
/* 118 */         .setRolls(ConstantValue.exactly(1.0F))
/* 119 */         .add(EmptyLootItem.emptyItem().setWeight(1))
/* 120 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(2).apply((new EnchantRandomlyFunction.Builder()).withEnchantment(enchantments.getOrThrow(Enchantments.QUICK_CHARGE)))));
/*     */   }
/*     */ 
/*     */   
/*     */   public LootTable.Builder desertPyramidLootTable() {
/* 125 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 126 */     return LootTable.lootTable()
/* 127 */       .withPool(LootPool.lootPool()
/* 128 */         .setRolls(UniformGenerator.between(2.0F, 4.0F))
/* 129 */         .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 130 */         .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/* 131 */         .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F))))
/* 132 */         .add(LootItem.lootTableItem(Items.EMERALD).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 133 */         .add(LootItem.lootTableItem(Items.BONE).setWeight(25).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 6.0F))))
/* 134 */         .add(LootItem.lootTableItem(Items.SPIDER_EYE).setWeight(25).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 135 */         .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(25).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
/* 136 */         .add(LootItem.lootTableItem(Items.SADDLE).setWeight(20))
/* 137 */         .add(LootItem.lootTableItem(Items.COPPER_HORSE_ARMOR).setWeight(15))
/* 138 */         .add(LootItem.lootTableItem(Items.IRON_HORSE_ARMOR).setWeight(15))
/* 139 */         .add(LootItem.lootTableItem(Items.GOLDEN_HORSE_ARMOR).setWeight(10))
/* 140 */         .add(LootItem.lootTableItem(Items.DIAMOND_HORSE_ARMOR).setWeight(5))
/* 141 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(10).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 142 */         .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(20))
/* 143 */         .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
/* 144 */         .add(EmptyLootItem.emptyItem().setWeight(15)))
/*     */       
/* 146 */       .withPool(LootPool.lootPool()
/* 147 */         .setRolls(ConstantValue.exactly(4.0F))
/* 148 */         .add(LootItem.lootTableItem(Items.BONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
/* 149 */         .add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
/* 150 */         .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
/* 151 */         .add(LootItem.lootTableItem(Items.STRING).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
/* 152 */         .add(LootItem.lootTableItem(Blocks.SAND).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F)))))
/*     */       
/* 154 */       .withPool(LootPool.lootPool()
/* 155 */         .setRolls(ConstantValue.exactly(1.0F))
/* 156 */         .add(EmptyLootItem.emptyItem().setWeight(4))
/* 157 */         .add(LootItem.lootTableItem(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))))
/* 158 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(2).apply((new EnchantRandomlyFunction.Builder()).withEnchantment(enchantments.getOrThrow(Enchantments.UNBREAKING)))));
/*     */   }
/*     */ 
/*     */   
/*     */   public LootTable.Builder ancientCityLootTable() {
/* 163 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 164 */     return LootTable.lootTable()
/* 165 */       .withPool(LootPool.lootPool()
/* 166 */         .setRolls(UniformGenerator.between(5.0F, 10.0F))
/*     */ 
/*     */         
/* 169 */         .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
/* 170 */         .add(LootItem.lootTableItem(Items.MUSIC_DISC_OTHERSIDE).setWeight(1))
/*     */ 
/*     */         
/* 173 */         .add(LootItem.lootTableItem(Items.COMPASS).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 174 */         .add(LootItem.lootTableItem(Items.SCULK_CATALYST).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
/* 175 */         .add(LootItem.lootTableItem(Items.NAME_TAG).setWeight(2))
/* 176 */         .add(LootItem.lootTableItem(Items.DIAMOND_HOE).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.8F, 1.0F))).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(30.0F, 50.0F))))
/* 177 */         .add(LootItem.lootTableItem(Items.LEAD).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 178 */         .add(LootItem.lootTableItem(Items.DIAMOND_HORSE_ARMOR).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 179 */         .add(LootItem.lootTableItem(Items.SADDLE).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 180 */         .add(LootItem.lootTableItem(Items.MUSIC_DISC_13).setWeight(2))
/* 181 */         .add(LootItem.lootTableItem(Items.MUSIC_DISC_CAT).setWeight(2))
/* 182 */         .add(LootItem.lootTableItem(Items.DIAMOND_LEGGINGS).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(30.0F, 50.0F))))
/*     */ 
/*     */         
/* 185 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(3).apply((new EnchantRandomlyFunction.Builder()).withEnchantment(enchantments.getOrThrow(Enchantments.SWIFT_SNEAK))))
/* 186 */         .add(LootItem.lootTableItem(Items.SCULK).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 10.0F))))
/* 187 */         .add(LootItem.lootTableItem(Items.SCULK_SENSOR).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 188 */         .add(LootItem.lootTableItem(Items.CANDLE).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/* 189 */         .add(LootItem.lootTableItem(Items.AMETHYST_SHARD).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 15.0F))))
/* 190 */         .add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 191 */         .add(LootItem.lootTableItem(Items.GLOW_BERRIES).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 15.0F))))
/* 192 */         .add(LootItem.lootTableItem(Items.IRON_LEGGINGS).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(20.0F, 39.0F))))
/*     */         
/* 194 */         .add(LootItem.lootTableItem(Items.ECHO_SHARD).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 195 */         .add(LootItem.lootTableItem(Items.DISC_FRAGMENT_5).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*     */ 
/*     */         
/* 198 */         .add(LootItem.lootTableItem(Items.POTION).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))).apply(SetPotionFunction.setPotion(Potions.STRONG_REGENERATION)))
/* 199 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(5).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 200 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 10.0F))))
/* 201 */         .add(LootItem.lootTableItem(Items.BONE).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 15.0F))))
/* 202 */         .add(LootItem.lootTableItem(Items.SOUL_TORCH).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 15.0F))))
/*     */ 
/*     */         
/* 205 */         .add(LootItem.lootTableItem(Items.COAL).setWeight(7).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 15.0F)))))
/*     */       
/* 207 */       .withPool(LootPool.lootPool()
/* 208 */         .setRolls(ConstantValue.exactly(1.0F))
/* 209 */         .add(EmptyLootItem.emptyItem().setWeight(71))
/* 210 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(4).apply((new EnchantRandomlyFunction.Builder()).withEnchantment(enchantments.getOrThrow(Enchantments.MENDING))))
/* 211 */         .add(LootItem.lootTableItem(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(4))
/* 212 */         .add(LootItem.lootTableItem(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*     */   }
/*     */ 
/*     */   
/*     */   public LootTable.Builder jungleTempleLootTable() {
/* 217 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 218 */     return LootTable.lootTable()
/* 219 */       .withPool(LootPool.lootPool()
/* 220 */         .setRolls(UniformGenerator.between(2.0F, 6.0F))
/* 221 */         .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 222 */         .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/* 223 */         .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F))))
/* 224 */         .add(LootItem.lootTableItem(Blocks.BAMBOO).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 225 */         .add(LootItem.lootTableItem(Items.EMERALD).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 226 */         .add(LootItem.lootTableItem(Items.BONE).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 6.0F))))
/* 227 */         .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(16).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
/* 228 */         .add(LootItem.lootTableItem(Items.SADDLE).setWeight(3))
/* 229 */         .add(LootItem.lootTableItem(Items.COPPER_HORSE_ARMOR))
/* 230 */         .add(LootItem.lootTableItem(Items.IRON_HORSE_ARMOR))
/* 231 */         .add(LootItem.lootTableItem(Items.GOLDEN_HORSE_ARMOR))
/* 232 */         .add(LootItem.lootTableItem(Items.DIAMOND_HORSE_ARMOR))
/* 233 */         .add(LootItem.lootTableItem(Items.BOOK).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(30.0F)))))
/*     */       
/* 235 */       .withPool(LootPool.lootPool()
/* 236 */         .setRolls(ConstantValue.exactly(1.0F))
/* 237 */         .add(EmptyLootItem.emptyItem().setWeight(2))
/* 238 */         .add(LootItem.lootTableItem(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))
/*     */       
/* 240 */       .withPool(LootPool.lootPool()
/* 241 */         .setRolls(ConstantValue.exactly(1.0F))
/* 242 */         .add(EmptyLootItem.emptyItem().setWeight(1))
/* 243 */         .add(LootItem.lootTableItem(Items.BOOK).apply((new EnchantRandomlyFunction.Builder()).withEnchantment(enchantments.getOrThrow(Enchantments.UNBREAKING)))));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\loot\packs\TradeRebalanceChestLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */