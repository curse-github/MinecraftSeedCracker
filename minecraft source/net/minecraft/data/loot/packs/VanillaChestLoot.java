/*      */ package net.minecraft.data.loot.packs;
/*      */ 
/*      */ import java.util.function.BiConsumer;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.data.loot.LootTableSubProvider;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.tags.InstrumentTags;
/*      */ import net.minecraft.tags.StructureTags;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.alchemy.Potions;
/*      */ import net.minecraft.world.item.enchantment.Enchantment;
/*      */ import net.minecraft.world.item.enchantment.Enchantments;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ import net.minecraft.world.level.storage.loot.LootPool;
/*      */ import net.minecraft.world.level.storage.loot.LootTable;
/*      */ import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
/*      */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*      */ import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
/*      */ import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetInstrumentFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetNameFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetOminousBottleAmplifierFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetStewEffectFunction;
/*      */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
/*      */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*      */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*      */ 
/*      */ public final class VanillaChestLoot
/*      */   extends Record implements LootTableSubProvider {
/*      */   private final HolderLookup.Provider registries;
/*      */   
/*   45 */   public VanillaChestLoot(HolderLookup.Provider registries) { this.registries = registries; } public final String toString() { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaChestLoot;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #45	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*   45 */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChestLoot; } public HolderLookup.Provider registries() { return this.registries; } public final int hashCode() { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaChestLoot;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #45	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChestLoot; }
/*      */   public final boolean equals(Object o) { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaChestLoot;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #45	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaChestLoot;
/*      */     //   0	8	1	o	Ljava/lang/Object; }
/*      */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/*   48 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/*      */     
/*   50 */     output.accept(BuiltInLootTables.ABANDONED_MINESHAFT, 
/*   51 */         LootTable.lootTable()
/*   52 */         .withPool(LootPool.lootPool()
/*   53 */           .setRolls(ConstantValue.exactly(1.0F))
/*   54 */           .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(20))
/*   55 */           .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE))
/*   56 */           .add(LootItem.lootTableItem(Items.NAME_TAG).setWeight(30))
/*   57 */           .add(LootItem.lootTableItem(Items.BOOK).setWeight(10).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*   58 */           .add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(5))
/*   59 */           .add(EmptyLootItem.emptyItem().setWeight(5)))
/*      */         
/*   61 */         .withPool(LootPool.lootPool()
/*   62 */           .setRolls(UniformGenerator.between(2.0F, 4.0F))
/*   63 */           .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/*   64 */           .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*   65 */           .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
/*   66 */           .add(LootItem.lootTableItem(Items.LAPIS_LAZULI).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
/*   67 */           .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
/*   68 */           .add(LootItem.lootTableItem(Items.COAL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F))))
/*   69 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*   70 */           .add(LootItem.lootTableItem(Items.GLOW_BERRIES).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F))))
/*   71 */           .add(LootItem.lootTableItem(Items.MELON_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
/*   72 */           .add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
/*   73 */           .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))))
/*      */         
/*   75 */         .withPool(LootPool.lootPool()
/*   76 */           .setRolls(ConstantValue.exactly(3.0F))
/*   77 */           .add(LootItem.lootTableItem(Blocks.RAIL).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))))
/*   78 */           .add(LootItem.lootTableItem(Blocks.POWERED_RAIL).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*   79 */           .add(LootItem.lootTableItem(Blocks.DETECTOR_RAIL).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*   80 */           .add(LootItem.lootTableItem(Blocks.ACTIVATOR_RAIL).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*   81 */           .add(LootItem.lootTableItem(Blocks.TORCH).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))));
/*      */ 
/*      */ 
/*      */     
/*   85 */     output.accept(BuiltInLootTables.BASTION_BRIDGE, bastionBridgeLootTable());
/*   86 */     output.accept(BuiltInLootTables.BASTION_HOGLIN_STABLE, bastionHoglinStableLootTable());
/*   87 */     output.accept(BuiltInLootTables.BASTION_OTHER, bastionOtherLootTable());
/*   88 */     output.accept(BuiltInLootTables.BASTION_TREASURE, bastionTreasureLootTable());
/*      */     
/*   90 */     output.accept(BuiltInLootTables.BURIED_TREASURE, 
/*   91 */         LootTable.lootTable()
/*   92 */         .withPool(LootPool.lootPool()
/*   93 */           .setRolls(ConstantValue.exactly(1.0F))
/*   94 */           .add(LootItem.lootTableItem(Items.HEART_OF_THE_SEA)))
/*      */         
/*   96 */         .withPool(LootPool.lootPool()
/*   97 */           .setRolls(UniformGenerator.between(5.0F, 8.0F))
/*   98 */           .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*   99 */           .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  100 */           .add(LootItem.lootTableItem(Blocks.TNT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
/*      */         
/*  102 */         .withPool(LootPool.lootPool()
/*  103 */           .setRolls(UniformGenerator.between(1.0F, 3.0F))
/*  104 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))))
/*  105 */           .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
/*  106 */           .add(LootItem.lootTableItem(Items.PRISMARINE_CRYSTALS).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F)))))
/*      */         
/*  108 */         .withPool(LootPool.lootPool()
/*  109 */           .setRolls(UniformGenerator.between(0.0F, 1.0F))
/*  110 */           .add(LootItem.lootTableItem(Items.LEATHER_CHESTPLATE))
/*  111 */           .add(LootItem.lootTableItem(Items.IRON_SWORD))
/*  112 */           .add(LootItem.lootTableItem(Items.IRON_SPEAR)))
/*      */         
/*  114 */         .withPool(LootPool.lootPool()
/*  115 */           .setRolls(ConstantValue.exactly(2.0F))
/*  116 */           .add(LootItem.lootTableItem(Items.COOKED_COD).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
/*  117 */           .add(LootItem.lootTableItem(Items.COOKED_SALMON).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))))
/*      */         
/*  119 */         .withPool(LootPool.lootPool()
/*  120 */           .setRolls(UniformGenerator.between(0.0F, 2.0F))
/*  121 */           .add(LootItem.lootTableItem(Items.POTION)).apply(SetPotionFunction.setPotion(Potions.WATER_BREATHING)))
/*      */         
/*  123 */         .withPool(LootPool.lootPool()
/*  124 */           .setRolls(ConstantValue.exactly(1.0F))
/*  125 */           .add(EmptyLootItem.emptyItem().setWeight(148))
/*  126 */           .add(LootItem.lootTableItem(Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  127 */           .add(LootItem.lootTableItem(Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  128 */           .add(LootItem.lootTableItem(Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  129 */           .add(LootItem.lootTableItem(Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  133 */     output.accept(BuiltInLootTables.ANCIENT_CITY, ancientCityLootTable());
/*      */     
/*  135 */     output.accept(BuiltInLootTables.ANCIENT_CITY_ICE_BOX, 
/*  136 */         LootTable.lootTable()
/*  137 */         .withPool(LootPool.lootPool()
/*  138 */           .setRolls(UniformGenerator.between(4.0F, 10.0F))
/*      */ 
/*      */           
/*  141 */           .add(LootItem.lootTableItem(Items.SUSPICIOUS_STEW).setWeight(1).apply(SetStewEffectFunction.stewEffect()
/*  142 */               .withEffect(MobEffects.NIGHT_VISION, UniformGenerator.between(7.0F, 10.0F))
/*  143 */               .withEffect(MobEffects.BLINDNESS, UniformGenerator.between(5.0F, 7.0F)))
/*  144 */             .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
/*  145 */           .add(LootItem.lootTableItem(Items.GOLDEN_CARROT).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F))))
/*  146 */           .add(LootItem.lootTableItem(Items.BAKED_POTATO).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F))))
/*      */ 
/*      */           
/*  149 */           .add(LootItem.lootTableItem(Items.PACKED_ICE).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
/*      */ 
/*      */           
/*  152 */           .add(LootItem.lootTableItem(Items.SNOWBALL).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  156 */     output.accept(BuiltInLootTables.DESERT_PYRAMID, desertPyramidLootTable());
/*  157 */     output.accept(BuiltInLootTables.END_CITY_TREASURE, endCityTreasureLootTable());
/*      */     
/*  159 */     output.accept(BuiltInLootTables.IGLOO_CHEST, 
/*  160 */         LootTable.lootTable()
/*  161 */         .withPool(LootPool.lootPool()
/*  162 */           .setRolls(UniformGenerator.between(2.0F, 8.0F))
/*  163 */           .add(LootItem.lootTableItem(Items.APPLE).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  164 */           .add(LootItem.lootTableItem(Items.COAL).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  165 */           .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  166 */           .add(LootItem.lootTableItem(Items.STONE_AXE).setWeight(2))
/*  167 */           .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10))
/*  168 */           .add(LootItem.lootTableItem(Items.EMERALD))
/*  169 */           .add(LootItem.lootTableItem(Items.WHEAT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))))
/*      */         
/*  171 */         .withPool(LootPool.lootPool()
/*  172 */           .setRolls(ConstantValue.exactly(1.0F))
/*  173 */           .add(LootItem.lootTableItem(Items.GOLDEN_APPLE))));
/*      */ 
/*      */ 
/*      */     
/*  177 */     output.accept(BuiltInLootTables.JUNGLE_TEMPLE, jungleTempleLootTable());
/*      */     
/*  179 */     output.accept(BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER, 
/*  180 */         LootTable.lootTable()
/*  181 */         .withPool(LootPool.lootPool()
/*  182 */           .setRolls(UniformGenerator.between(1.0F, 2.0F))
/*  183 */           .add(LootItem.lootTableItem(Items.ARROW).setWeight(30).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  187 */     output.accept(BuiltInLootTables.NETHER_BRIDGE, netherBridgeLootTable());
/*      */     
/*  189 */     output.accept(BuiltInLootTables.PILLAGER_OUTPOST, pillagerOutpostLootTable());
/*      */     
/*  191 */     output.accept(BuiltInLootTables.SHIPWRECK_MAP, shipwreckMapLootTable());
/*      */     
/*  193 */     output.accept(BuiltInLootTables.SHIPWRECK_SUPPLY, shipwreckSupplyLootTable());
/*      */     
/*  195 */     output.accept(BuiltInLootTables.SHIPWRECK_TREASURE, shipwreckTreasureLootTable());
/*      */     
/*  197 */     output.accept(BuiltInLootTables.SIMPLE_DUNGEON, 
/*  198 */         LootTable.lootTable()
/*  199 */         .withPool(LootPool.lootPool()
/*  200 */           .setRolls(UniformGenerator.between(1.0F, 3.0F))
/*  201 */           .add(LootItem.lootTableItem(Items.LEATHER).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/*  202 */           .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(15))
/*  203 */           .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
/*  204 */           .add(LootItem.lootTableItem(Items.MUSIC_DISC_OTHERSIDE).setWeight(2))
/*  205 */           .add(LootItem.lootTableItem(Items.MUSIC_DISC_13).setWeight(15))
/*  206 */           .add(LootItem.lootTableItem(Items.MUSIC_DISC_CAT).setWeight(15))
/*  207 */           .add(LootItem.lootTableItem(Items.NAME_TAG).setWeight(20))
/*  208 */           .add(LootItem.lootTableItem(Items.GOLDEN_HORSE_ARMOR).setWeight(10))
/*  209 */           .add(LootItem.lootTableItem(Items.COPPER_HORSE_ARMOR).setWeight(15))
/*  210 */           .add(LootItem.lootTableItem(Items.IRON_HORSE_ARMOR).setWeight(15))
/*  211 */           .add(LootItem.lootTableItem(Items.DIAMOND_HORSE_ARMOR).setWeight(5))
/*  212 */           .add(LootItem.lootTableItem(Items.BOOK).setWeight(10).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))))
/*      */         
/*  214 */         .withPool(LootPool.lootPool()
/*  215 */           .setRolls(UniformGenerator.between(1.0F, 4.0F))
/*  216 */           .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  217 */           .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  218 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(20))
/*  219 */           .add(LootItem.lootTableItem(Items.WHEAT).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  220 */           .add(LootItem.lootTableItem(Items.BUCKET).setWeight(10))
/*  221 */           .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  222 */           .add(LootItem.lootTableItem(Items.COAL).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  223 */           .add(LootItem.lootTableItem(Items.MELON_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
/*  224 */           .add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
/*  225 */           .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))))
/*      */         
/*  227 */         .withPool(LootPool.lootPool()
/*  228 */           .setRolls(ConstantValue.exactly(3.0F))
/*  229 */           .add(LootItem.lootTableItem(Items.BONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
/*  230 */           .add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
/*  231 */           .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
/*  232 */           .add(LootItem.lootTableItem(Items.STRING).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  236 */     output.accept(BuiltInLootTables.SPAWN_BONUS_CHEST, 
/*  237 */         LootTable.lootTable()
/*  238 */         .withPool(LootPool.lootPool()
/*  239 */           .setRolls(ConstantValue.exactly(1.0F))
/*  240 */           .add(LootItem.lootTableItem(Items.STONE_AXE))
/*  241 */           .add(LootItem.lootTableItem(Items.WOODEN_AXE).setWeight(3)))
/*      */         
/*  243 */         .withPool(LootPool.lootPool()
/*  244 */           .setRolls(ConstantValue.exactly(1.0F))
/*  245 */           .add(LootItem.lootTableItem(Items.STONE_PICKAXE))
/*  246 */           .add(LootItem.lootTableItem(Items.WOODEN_PICKAXE).setWeight(3)))
/*      */         
/*  248 */         .withPool(LootPool.lootPool()
/*  249 */           .setRolls(ConstantValue.exactly(3.0F))
/*  250 */           .add(LootItem.lootTableItem(Items.APPLE).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
/*  251 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
/*  252 */           .add(LootItem.lootTableItem(Items.SALMON).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
/*      */         
/*  254 */         .withPool(LootPool.lootPool()
/*  255 */           .setRolls(ConstantValue.exactly(4.0F))
/*  256 */           .add(LootItem.lootTableItem(Items.STICK).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 12.0F))))
/*  257 */           .add(LootItem.lootTableItem(Blocks.OAK_PLANKS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 12.0F))))
/*  258 */           .add(LootItem.lootTableItem(Blocks.OAK_LOG).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  259 */           .add(LootItem.lootTableItem(Blocks.SPRUCE_LOG).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  260 */           .add(LootItem.lootTableItem(Blocks.BIRCH_LOG).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  261 */           .add(LootItem.lootTableItem(Blocks.JUNGLE_LOG).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  262 */           .add(LootItem.lootTableItem(Blocks.ACACIA_LOG).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  263 */           .add(LootItem.lootTableItem(Blocks.DARK_OAK_LOG).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  264 */           .add(LootItem.lootTableItem(Blocks.MANGROVE_LOG).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  268 */     output.accept(BuiltInLootTables.STRONGHOLD_CORRIDOR, strongholdCorridorLootTable());
/*      */     
/*  270 */     output.accept(BuiltInLootTables.STRONGHOLD_CROSSING, 
/*  271 */         LootTable.lootTable()
/*  272 */         .withPool(LootPool.lootPool()
/*  273 */           .setRolls(UniformGenerator.between(1.0F, 4.0F))
/*  274 */           .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/*  275 */           .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  276 */           .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
/*  277 */           .add(LootItem.lootTableItem(Items.COAL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F))))
/*  278 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  279 */           .add(LootItem.lootTableItem(Items.APPLE).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  280 */           .add(LootItem.lootTableItem(Items.IRON_PICKAXE))
/*  281 */           .add(LootItem.lootTableItem(Items.BOOK).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(30.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  285 */     output.accept(BuiltInLootTables.STRONGHOLD_LIBRARY, strongholdLibraryLootTable());
/*      */     
/*  287 */     output.accept(BuiltInLootTables.UNDERWATER_RUIN_BIG, 
/*  288 */         LootTable.lootTable()
/*  289 */         .withPool(LootPool.lootPool()
/*  290 */           .setRolls(UniformGenerator.between(2.0F, 8.0F))
/*  291 */           .add(LootItem.lootTableItem(Items.COAL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  292 */           .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  293 */           .add(LootItem.lootTableItem(Items.EMERALD))
/*  294 */           .add(LootItem.lootTableItem(Items.STONE_SPEAR).setWeight(2))
/*  295 */           .add(LootItem.lootTableItem(Items.WHEAT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))))
/*      */         
/*  297 */         .withPool(LootPool.lootPool()
/*  298 */           .setRolls(ConstantValue.exactly(1.0F))
/*  299 */           .add(LootItem.lootTableItem(Items.GOLDEN_APPLE))
/*  300 */           .add(LootItem.lootTableItem(Items.BOOK).setWeight(5).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  301 */           .add(LootItem.lootTableItem(Items.LEATHER_CHESTPLATE))
/*  302 */           .add(LootItem.lootTableItem(Items.GOLDEN_HELMET))
/*  303 */           .add(LootItem.lootTableItem(Items.FISHING_ROD).setWeight(5).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  304 */           .add(LootItem.lootTableItem(Items.MAP).setWeight(10).apply(ExplorationMapFunction.makeExplorationMap().setDestination(StructureTags.ON_TREASURE_MAPS).setMapDecoration(MapDecorationTypes.RED_X).setZoom((byte)1).setSkipKnownStructures(false)).apply(SetNameFunction.setName(Component.translatable("filled_map.buried_treasure"), SetNameFunction.Target.ITEM_NAME))))
/*      */         
/*  306 */         .withPool(LootPool.lootPool()
/*  307 */           .setRolls(ConstantValue.exactly(1.0F))
/*  308 */           .add(EmptyLootItem.emptyItem().setWeight(148))
/*  309 */           .add(LootItem.lootTableItem(Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  310 */           .add(LootItem.lootTableItem(Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  311 */           .add(LootItem.lootTableItem(Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  312 */           .add(LootItem.lootTableItem(Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  316 */     output.accept(BuiltInLootTables.UNDERWATER_RUIN_SMALL, 
/*  317 */         LootTable.lootTable()
/*  318 */         .withPool(LootPool.lootPool()
/*  319 */           .setRolls(UniformGenerator.between(2.0F, 8.0F))
/*  320 */           .add(LootItem.lootTableItem(Items.COAL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  321 */           .add(LootItem.lootTableItem(Items.STONE_AXE).setWeight(2))
/*  322 */           .add(LootItem.lootTableItem(Items.STONE_SPEAR).setWeight(2))
/*  323 */           .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(5))
/*  324 */           .add(LootItem.lootTableItem(Items.EMERALD))
/*  325 */           .add(LootItem.lootTableItem(Items.WHEAT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))))
/*      */         
/*  327 */         .withPool(LootPool.lootPool()
/*  328 */           .setRolls(ConstantValue.exactly(1.0F))
/*  329 */           .add(LootItem.lootTableItem(Items.LEATHER_CHESTPLATE))
/*  330 */           .add(LootItem.lootTableItem(Items.GOLDEN_HELMET))
/*  331 */           .add(LootItem.lootTableItem(Items.FISHING_ROD).setWeight(5).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  332 */           .add(LootItem.lootTableItem(Items.MAP).setWeight(5).apply(ExplorationMapFunction.makeExplorationMap().setDestination(StructureTags.ON_TREASURE_MAPS).setMapDecoration(MapDecorationTypes.RED_X).setZoom((byte)1).setSkipKnownStructures(false)).apply(SetNameFunction.setName(Component.translatable("filled_map.buried_treasure"), SetNameFunction.Target.ITEM_NAME))))
/*      */         
/*  334 */         .withPool(LootPool.lootPool()
/*  335 */           .setRolls(ConstantValue.exactly(1.0F))
/*  336 */           .add(EmptyLootItem.emptyItem().setWeight(148))
/*  337 */           .add(LootItem.lootTableItem(Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  338 */           .add(LootItem.lootTableItem(Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  339 */           .add(LootItem.lootTableItem(Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  340 */           .add(LootItem.lootTableItem(Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  344 */     output.accept(BuiltInLootTables.VILLAGE_WEAPONSMITH, 
/*  345 */         LootTable.lootTable()
/*  346 */         .withPool(LootPool.lootPool()
/*  347 */           .setRolls(UniformGenerator.between(3.0F, 8.0F))
/*  348 */           .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  349 */           .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/*  350 */           .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  351 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  352 */           .add(LootItem.lootTableItem(Items.APPLE).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  353 */           .add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(5))
/*  354 */           .add(LootItem.lootTableItem(Items.IRON_SWORD).setWeight(5))
/*  355 */           .add(LootItem.lootTableItem(Items.IRON_SPEAR).setWeight(5))
/*  356 */           .add(LootItem.lootTableItem(Items.COPPER_SPEAR).setWeight(7))
/*  357 */           .add(LootItem.lootTableItem(Items.IRON_CHESTPLATE).setWeight(5))
/*  358 */           .add(LootItem.lootTableItem(Items.IRON_HELMET).setWeight(5))
/*  359 */           .add(LootItem.lootTableItem(Items.IRON_LEGGINGS).setWeight(5))
/*  360 */           .add(LootItem.lootTableItem(Items.IRON_BOOTS).setWeight(5))
/*  361 */           .add(LootItem.lootTableItem(Blocks.OBSIDIAN).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
/*  362 */           .add(LootItem.lootTableItem(Blocks.OAK_SAPLING).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
/*  363 */           .add(LootItem.lootTableItem(Items.SADDLE).setWeight(3))
/*  364 */           .add(LootItem.lootTableItem(Items.COPPER_HORSE_ARMOR))
/*  365 */           .add(LootItem.lootTableItem(Items.IRON_HORSE_ARMOR))
/*  366 */           .add(LootItem.lootTableItem(Items.GOLDEN_HORSE_ARMOR))
/*  367 */           .add(LootItem.lootTableItem(Items.DIAMOND_HORSE_ARMOR)))
/*      */         
/*  369 */         .withPool(LootPool.lootPool()
/*  370 */           .setRolls(ConstantValue.exactly(1.0F))
/*  371 */           .add(LootItem.lootTableItem(Items.BUNDLE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  372 */           .add(EmptyLootItem.emptyItem().setWeight(2))));
/*      */ 
/*      */     
/*  375 */     output.accept(BuiltInLootTables.VILLAGE_TOOLSMITH, 
/*  376 */         LootTable.lootTable()
/*  377 */         .withPool(LootPool.lootPool()
/*  378 */           .setRolls(UniformGenerator.between(3.0F, 8.0F))
/*  379 */           .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  380 */           .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/*  381 */           .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  382 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  383 */           .add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(5))
/*  384 */           .add(LootItem.lootTableItem(Items.COAL).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  385 */           .add(LootItem.lootTableItem(Items.STICK).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  386 */           .add(LootItem.lootTableItem(Items.IRON_SHOVEL).setWeight(5))));
/*      */ 
/*      */ 
/*      */     
/*  390 */     output.accept(BuiltInLootTables.VILLAGE_CARTOGRAPHER, 
/*  391 */         LootTable.lootTable()
/*  392 */         .withPool(LootPool.lootPool()
/*  393 */           .setRolls(UniformGenerator.between(1.0F, 5.0F))
/*  394 */           .add(LootItem.lootTableItem(Items.MAP).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  395 */           .add(LootItem.lootTableItem(Items.PAPER).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/*  396 */           .add(LootItem.lootTableItem(Items.COMPASS).setWeight(5))
/*  397 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  398 */           .add(LootItem.lootTableItem(Items.STICK).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
/*  399 */         .withPool(LootPool.lootPool()
/*  400 */           .setRolls(ConstantValue.exactly(1.0F))
/*  401 */           .add(LootItem.lootTableItem(Items.BUNDLE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  402 */           .add(EmptyLootItem.emptyItem().setWeight(2))));
/*      */ 
/*      */     
/*  405 */     output.accept(BuiltInLootTables.VILLAGE_MASON, 
/*  406 */         LootTable.lootTable()
/*  407 */         .withPool(LootPool.lootPool()
/*  408 */           .setRolls(UniformGenerator.between(1.0F, 5.0F))
/*  409 */           .add(LootItem.lootTableItem(Items.CLAY_BALL).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  410 */           .add(LootItem.lootTableItem(Items.FLOWER_POT).setWeight(1))
/*  411 */           .add(LootItem.lootTableItem(Blocks.STONE).setWeight(2))
/*  412 */           .add(LootItem.lootTableItem(Blocks.STONE_BRICKS).setWeight(2))
/*  413 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  414 */           .add(LootItem.lootTableItem(Items.YELLOW_DYE).setWeight(1))
/*  415 */           .add(LootItem.lootTableItem(Blocks.SMOOTH_STONE).setWeight(1))
/*  416 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1))));
/*      */ 
/*      */ 
/*      */     
/*  420 */     output.accept(BuiltInLootTables.VILLAGE_ARMORER, 
/*  421 */         LootTable.lootTable()
/*  422 */         .withPool(LootPool.lootPool()
/*  423 */           .setRolls(UniformGenerator.between(1.0F, 5.0F))
/*  424 */           .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  425 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  426 */           .add(LootItem.lootTableItem(Items.IRON_HELMET).setWeight(1))
/*  427 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1))));
/*      */ 
/*      */ 
/*      */     
/*  431 */     output.accept(BuiltInLootTables.VILLAGE_SHEPHERD, 
/*  432 */         LootTable.lootTable()
/*  433 */         .withPool(LootPool.lootPool()
/*  434 */           .setRolls(UniformGenerator.between(1.0F, 5.0F))
/*  435 */           .add(LootItem.lootTableItem(Blocks.WHITE_WOOL).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
/*  436 */           .add(LootItem.lootTableItem(Blocks.BLACK_WOOL).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  437 */           .add(LootItem.lootTableItem(Blocks.GRAY_WOOL).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  438 */           .add(LootItem.lootTableItem(Blocks.BROWN_WOOL).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  439 */           .add(LootItem.lootTableItem(Blocks.LIGHT_GRAY_WOOL).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  440 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1))
/*  441 */           .add(LootItem.lootTableItem(Items.SHEARS).setWeight(1))
/*  442 */           .add(LootItem.lootTableItem(Items.WHEAT).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 6.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  446 */     output.accept(BuiltInLootTables.VILLAGE_BUTCHER, 
/*  447 */         LootTable.lootTable()
/*  448 */         .withPool(LootPool.lootPool()
/*  449 */           .setRolls(UniformGenerator.between(1.0F, 5.0F))
/*  450 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1))
/*  451 */           .add(LootItem.lootTableItem(Items.PORKCHOP).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  452 */           .add(LootItem.lootTableItem(Items.WHEAT).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  453 */           .add(LootItem.lootTableItem(Items.BEEF).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  454 */           .add(LootItem.lootTableItem(Items.MUTTON).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  455 */           .add(LootItem.lootTableItem(Items.COAL).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  459 */     output.accept(BuiltInLootTables.VILLAGE_FLETCHER, 
/*  460 */         LootTable.lootTable()
/*  461 */         .withPool(LootPool.lootPool()
/*  462 */           .setRolls(UniformGenerator.between(1.0F, 5.0F))
/*  463 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1))
/*  464 */           .add(LootItem.lootTableItem(Items.ARROW).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  465 */           .add(LootItem.lootTableItem(Items.FEATHER).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  466 */           .add(LootItem.lootTableItem(Items.EGG).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  467 */           .add(LootItem.lootTableItem(Items.FLINT).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  468 */           .add(LootItem.lootTableItem(Items.STICK).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  472 */     output.accept(BuiltInLootTables.VILLAGE_FISHER, 
/*  473 */         LootTable.lootTable()
/*  474 */         .withPool(LootPool.lootPool()
/*  475 */           .setRolls(UniformGenerator.between(1.0F, 5.0F))
/*  476 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1))
/*  477 */           .add(LootItem.lootTableItem(Items.COD).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  478 */           .add(LootItem.lootTableItem(Items.SALMON).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  479 */           .add(LootItem.lootTableItem(Items.WATER_BUCKET).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  480 */           .add(LootItem.lootTableItem(Items.BARREL).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  481 */           .add(LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  482 */           .add(LootItem.lootTableItem(Items.COAL).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  486 */     output.accept(BuiltInLootTables.VILLAGE_TANNERY, 
/*  487 */         LootTable.lootTable()
/*  488 */         .withPool(LootPool.lootPool()
/*  489 */           .setRolls(UniformGenerator.between(1.0F, 5.0F))
/*  490 */           .add(LootItem.lootTableItem(Items.LEATHER).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  491 */           .add(LootItem.lootTableItem(Items.LEATHER_CHESTPLATE).setWeight(2))
/*  492 */           .add(LootItem.lootTableItem(Items.LEATHER_BOOTS).setWeight(2))
/*  493 */           .add(LootItem.lootTableItem(Items.LEATHER_HELMET).setWeight(2))
/*  494 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  495 */           .add(LootItem.lootTableItem(Items.LEATHER_LEGGINGS).setWeight(2))
/*  496 */           .add(LootItem.lootTableItem(Items.SADDLE).setWeight(1))
/*  497 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))))
/*      */         
/*  499 */         .withPool(LootPool.lootPool()
/*  500 */           .setRolls(ConstantValue.exactly(1.0F))
/*  501 */           .add(LootItem.lootTableItem(Items.BUNDLE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  502 */           .add(EmptyLootItem.emptyItem().setWeight(2))));
/*      */ 
/*      */     
/*  505 */     output.accept(BuiltInLootTables.VILLAGE_TEMPLE, 
/*  506 */         LootTable.lootTable()
/*  507 */         .withPool(LootPool.lootPool()
/*  508 */           .setRolls(UniformGenerator.between(3.0F, 8.0F))
/*  509 */           .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  510 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(7).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  511 */           .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(7).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  512 */           .add(LootItem.lootTableItem(Items.LAPIS_LAZULI).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  513 */           .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  514 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  518 */     output.accept(BuiltInLootTables.VILLAGE_PLAINS_HOUSE, 
/*  519 */         LootTable.lootTable()
/*  520 */         .withPool(LootPool.lootPool()
/*  521 */           .setRolls(UniformGenerator.between(3.0F, 8.0F))
/*  522 */           .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  523 */           .add(LootItem.lootTableItem(Items.DANDELION).setWeight(2))
/*  524 */           .add(LootItem.lootTableItem(Items.POPPY).setWeight(1))
/*  525 */           .add(LootItem.lootTableItem(Items.POTATO).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 7.0F))))
/*  526 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  527 */           .add(LootItem.lootTableItem(Items.APPLE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/*  528 */           .add(LootItem.lootTableItem(Items.BOOK).setWeight(1))
/*  529 */           .add(LootItem.lootTableItem(Items.FEATHER).setWeight(1))
/*  530 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  531 */           .add(LootItem.lootTableItem(Blocks.OAK_SAPLING).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
/*      */         
/*  533 */         .withPool(LootPool.lootPool()
/*  534 */           .setRolls(ConstantValue.exactly(1.0F))
/*  535 */           .add(LootItem.lootTableItem(Items.BUNDLE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  536 */           .add(EmptyLootItem.emptyItem().setWeight(2))));
/*      */ 
/*      */     
/*  539 */     output.accept(BuiltInLootTables.VILLAGE_TAIGA_HOUSE, 
/*  540 */         LootTable.lootTable()
/*  541 */         .withPool(LootPool.lootPool()
/*  542 */           .setRolls(UniformGenerator.between(3.0F, 8.0F))
/*  543 */           .add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/*  544 */           .add(LootItem.lootTableItem(Items.FERN).setWeight(2))
/*  545 */           .add(LootItem.lootTableItem(Items.LARGE_FERN).setWeight(2))
/*  546 */           .add(LootItem.lootTableItem(Items.POTATO).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 7.0F))))
/*  547 */           .add(LootItem.lootTableItem(Items.SWEET_BERRIES).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 7.0F))))
/*  548 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  549 */           .add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/*  550 */           .add(LootItem.lootTableItem(Items.PUMPKIN_PIE).setWeight(1))
/*  551 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  552 */           .add(LootItem.lootTableItem(Blocks.SPRUCE_SAPLING).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/*  553 */           .add(LootItem.lootTableItem(Items.SPRUCE_SIGN).setWeight(1))
/*  554 */           .add(LootItem.lootTableItem(Items.SPRUCE_LOG).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F)))))
/*      */         
/*  556 */         .withPool(LootPool.lootPool()
/*  557 */           .setRolls(ConstantValue.exactly(1.0F))
/*  558 */           .add(LootItem.lootTableItem(Items.BUNDLE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  559 */           .add(EmptyLootItem.emptyItem().setWeight(2))));
/*      */ 
/*      */     
/*  562 */     output.accept(BuiltInLootTables.VILLAGE_SAVANNA_HOUSE, 
/*  563 */         LootTable.lootTable()
/*  564 */         .withPool(LootPool.lootPool()
/*  565 */           .setRolls(UniformGenerator.between(3.0F, 8.0F))
/*  566 */           .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  567 */           .add(LootItem.lootTableItem(Items.SHORT_GRASS).setWeight(5))
/*  568 */           .add(LootItem.lootTableItem(Items.TALL_GRASS).setWeight(5))
/*  569 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  570 */           .add(LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/*  571 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  572 */           .add(LootItem.lootTableItem(Blocks.ACACIA_SAPLING).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
/*  573 */           .add(LootItem.lootTableItem(Items.SADDLE).setWeight(1))
/*  574 */           .add(LootItem.lootTableItem(Blocks.TORCH).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
/*  575 */           .add(LootItem.lootTableItem(Items.BUCKET).setWeight(1)))
/*      */         
/*  577 */         .withPool(LootPool.lootPool()
/*  578 */           .setRolls(ConstantValue.exactly(1.0F))
/*  579 */           .add(LootItem.lootTableItem(Items.BUNDLE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  580 */           .add(EmptyLootItem.emptyItem().setWeight(2))));
/*      */ 
/*      */     
/*  583 */     output.accept(BuiltInLootTables.VILLAGE_SNOWY_HOUSE, 
/*  584 */         LootTable.lootTable()
/*  585 */         .withPool(LootPool.lootPool()
/*  586 */           .setRolls(UniformGenerator.between(3.0F, 8.0F))
/*  587 */           .add(LootItem.lootTableItem(Blocks.BLUE_ICE).setWeight(1))
/*  588 */           .add(LootItem.lootTableItem(Blocks.SNOW_BLOCK).setWeight(4))
/*  589 */           .add(LootItem.lootTableItem(Items.POTATO).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 7.0F))))
/*  590 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  591 */           .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/*  592 */           .add(LootItem.lootTableItem(Items.BEETROOT_SOUP).setWeight(1))
/*  593 */           .add(LootItem.lootTableItem(Items.FURNACE).setWeight(1))
/*  594 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  595 */           .add(LootItem.lootTableItem(Items.SNOWBALL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 7.0F))))
/*  596 */           .add(LootItem.lootTableItem(Items.COAL).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))))
/*      */         
/*  598 */         .withPool(LootPool.lootPool()
/*  599 */           .setRolls(ConstantValue.exactly(1.0F))
/*  600 */           .add(LootItem.lootTableItem(Items.BUNDLE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  601 */           .add(EmptyLootItem.emptyItem().setWeight(2))));
/*      */ 
/*      */     
/*  604 */     output.accept(BuiltInLootTables.VILLAGE_DESERT_HOUSE, 
/*  605 */         LootTable.lootTable()
/*  606 */         .withPool(LootPool.lootPool()
/*  607 */           .setRolls(UniformGenerator.between(3.0F, 8.0F))
/*  608 */           .add(LootItem.lootTableItem(Items.CLAY_BALL).setWeight(1))
/*  609 */           .add(LootItem.lootTableItem(Items.GREEN_DYE).setWeight(1))
/*  610 */           .add(LootItem.lootTableItem(Blocks.CACTUS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  611 */           .add(LootItem.lootTableItem(Items.WHEAT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 7.0F))))
/*  612 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  613 */           .add(LootItem.lootTableItem(Items.BOOK).setWeight(1))
/*  614 */           .add(LootItem.lootTableItem(Blocks.DEAD_BUSH).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  615 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))))
/*      */         
/*  617 */         .withPool(LootPool.lootPool()
/*  618 */           .setRolls(ConstantValue.exactly(1.0F))
/*  619 */           .add(LootItem.lootTableItem(Items.BUNDLE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  620 */           .add(EmptyLootItem.emptyItem().setWeight(2))));
/*      */ 
/*      */     
/*  623 */     output.accept(BuiltInLootTables.WOODLAND_MANSION, woodlandMansionLootTable());
/*      */     
/*  625 */     output.accept(BuiltInLootTables.RUINED_PORTAL, 
/*  626 */         LootTable.lootTable()
/*  627 */         .withPool(LootPool.lootPool()
/*  628 */           .setRolls(UniformGenerator.between(4.0F, 8.0F))
/*  629 */           .add(LootItem.lootTableItem(Items.OBSIDIAN).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
/*  630 */           .add(LootItem.lootTableItem(Items.FLINT).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  631 */           .add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(9.0F, 18.0F))))
/*  632 */           .add(LootItem.lootTableItem(Items.FLINT_AND_STEEL).setWeight(40))
/*  633 */           .add(LootItem.lootTableItem(Items.FIRE_CHARGE).setWeight(40))
/*      */           
/*  635 */           .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(15))
/*  636 */           .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 24.0F))))
/*  637 */           .add(LootItem.lootTableItem(Items.GOLDEN_SWORD).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  638 */           .add(LootItem.lootTableItem(Items.GOLDEN_AXE).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  639 */           .add(LootItem.lootTableItem(Items.GOLDEN_HOE).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  640 */           .add(LootItem.lootTableItem(Items.GOLDEN_SHOVEL).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  641 */           .add(LootItem.lootTableItem(Items.GOLDEN_PICKAXE).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  642 */           .add(LootItem.lootTableItem(Items.GOLDEN_BOOTS).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  643 */           .add(LootItem.lootTableItem(Items.GOLDEN_CHESTPLATE).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  644 */           .add(LootItem.lootTableItem(Items.GOLDEN_HELMET).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  645 */           .add(LootItem.lootTableItem(Items.GOLDEN_LEGGINGS).setWeight(15).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*      */           
/*  647 */           .add(LootItem.lootTableItem(Items.GLISTERING_MELON_SLICE).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 12.0F))))
/*  648 */           .add(LootItem.lootTableItem(Items.GOLDEN_HORSE_ARMOR).setWeight(5))
/*  649 */           .add(LootItem.lootTableItem(Items.LIGHT_WEIGHTED_PRESSURE_PLATE).setWeight(5))
/*  650 */           .add(LootItem.lootTableItem(Items.GOLDEN_CARROT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 12.0F))))
/*  651 */           .add(LootItem.lootTableItem(Items.CLOCK).setWeight(5))
/*  652 */           .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
/*      */           
/*  654 */           .add(LootItem.lootTableItem(Items.BELL).setWeight(1))
/*  655 */           .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(1))
/*  656 */           .add(LootItem.lootTableItem(Items.GOLD_BLOCK).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
/*      */         
/*  658 */         .withPool(LootPool.lootPool()
/*  659 */           .setRolls(ConstantValue.exactly(1.0F))
/*  660 */           .add(EmptyLootItem.emptyItem().setWeight(1))
/*  661 */           .add(LootItem.lootTableItem(Items.LODESTONE).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  666 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_CORRIDOR_DISPENSER, 
/*  667 */         LootTable.lootTable()
/*  668 */         .withPool(LootPool.lootPool()
/*  669 */           .setRolls(ConstantValue.exactly(1.0F))
/*  670 */           .add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  675 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_WATER_DISPENSER, 
/*  676 */         LootTable.lootTable()
/*  677 */         .withPool(LootPool.lootPool()
/*  678 */           .setRolls(ConstantValue.exactly(1.0F))
/*  679 */           .add(LootItem.lootTableItem(Items.WATER_BUCKET).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  684 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_CHAMBER_DISPENSER, 
/*  685 */         LootTable.lootTable()
/*  686 */         .withPool(LootPool.lootPool()
/*  687 */           .setRolls(ConstantValue.exactly(1.0F))
/*  688 */           .add(LootItem.lootTableItem(Items.WATER_BUCKET).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).setWeight(4))
/*  689 */           .add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))).setWeight(4))
/*  690 */           .add(LootItem.lootTableItem(Items.SNOWBALL).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))).setWeight(6))
/*  691 */           .add(LootItem.lootTableItem(Items.EGG).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))).setWeight(2))
/*  692 */           .add(LootItem.lootTableItem(Items.FIRE_CHARGE).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))).setWeight(6))
/*  693 */           .add(LootItem.lootTableItem(Items.SPLASH_POTION).apply(SetPotionFunction.setPotion(Potions.SLOWNESS)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  694 */           .add(LootItem.lootTableItem(Items.SPLASH_POTION).apply(SetPotionFunction.setPotion(Potions.POISON)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  695 */           .add(LootItem.lootTableItem(Items.SPLASH_POTION).apply(SetPotionFunction.setPotion(Potions.WEAKNESS)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  696 */           .add(LootItem.lootTableItem(Items.LINGERING_POTION).apply(SetPotionFunction.setPotion(Potions.SLOWNESS)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  697 */           .add(LootItem.lootTableItem(Items.LINGERING_POTION).apply(SetPotionFunction.setPotion(Potions.POISON)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  698 */           .add(LootItem.lootTableItem(Items.LINGERING_POTION).apply(SetPotionFunction.setPotion(Potions.WEAKNESS)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  699 */           .add(LootItem.lootTableItem(Items.LINGERING_POTION).apply(SetPotionFunction.setPotion(Potions.HEALING)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).setWeight(1))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  705 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_CORRIDOR_POT, 
/*  706 */         LootTable.lootTable()
/*  707 */         .withPool(LootPool.lootPool()
/*  708 */           .setRolls(ConstantValue.exactly(1.0F))
/*  709 */           .add(LootItem.lootTableItem(Items.EMERALD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))).setWeight(125))
/*  710 */           .add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))).setWeight(100))
/*  711 */           .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))).setWeight(100))
/*  712 */           .add(LootItem.lootTableItem(Items.TRIAL_KEY).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).setWeight(10))
/*  713 */           .add(LootItem.lootTableItem(Items.MUSIC_DISC_CREATOR_MUSIC_BOX).setWeight(5))
/*  714 */           .add(LootItem.lootTableItem(Items.DIAMOND).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))).setWeight(5))
/*  715 */           .add(LootItem.lootTableItem(Items.EMERALD_BLOCK).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).setWeight(5))
/*  716 */           .add(LootItem.lootTableItem(Items.DIAMOND_BLOCK).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).setWeight(1))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  721 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_SUPPLY, 
/*  722 */         LootTable.lootTable()
/*  723 */         .withPool(LootPool.lootPool()
/*  724 */           .setRolls(UniformGenerator.between(3.0F, 5.0F))
/*  725 */           .add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 14.0F))).setWeight(2))
/*  726 */           .add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))).apply(SetPotionFunction.setPotion(Potions.POISON)).setWeight(1))
/*  727 */           .add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))).apply(SetPotionFunction.setPotion(Potions.SLOWNESS)).setWeight(1))
/*  728 */           .add(LootItem.lootTableItem(Items.BAKED_POTATO).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).setWeight(2))
/*  729 */           .add(LootItem.lootTableItem(Items.GLOW_BERRIES).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 10.0F))).setWeight(2))
/*  730 */           .add(LootItem.lootTableItem(Items.ACACIA_PLANKS).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F))).setWeight(1))
/*  731 */           .add(LootItem.lootTableItem(Items.MOSS_BLOCK).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  732 */           .add(LootItem.lootTableItem(Items.BONE_MEAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  733 */           .add(LootItem.lootTableItem(Items.TUFF).apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 10.0F))).setWeight(1))
/*  734 */           .add(LootItem.lootTableItem(Items.TORCH).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F))).setWeight(1))
/*  735 */           .add(LootItem.lootTableItem(Items.POTION).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))).apply(SetPotionFunction.setPotion(Potions.REGENERATION)))
/*  736 */           .add(LootItem.lootTableItem(Items.POTION).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))).apply(SetPotionFunction.setPotion(Potions.STRENGTH)))
/*  737 */           .add(LootItem.lootTableItem(Items.STONE_PICKAXE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.15F, 0.8F))).setWeight(2))
/*  738 */           .add(LootItem.lootTableItem(Items.MILK_BUCKET).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  744 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_ENTRANCE, 
/*  745 */         LootTable.lootTable()
/*  746 */         .withPool(LootPool.lootPool()
/*  747 */           .setRolls(UniformGenerator.between(2.0F, 3.0F))
/*  748 */           .add(LootItem.lootTableItem(Items.TRIAL_KEY).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).setWeight(1))
/*  749 */           .add(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).setWeight(5))
/*  750 */           .add(LootItem.lootTableItem(Items.WOODEN_AXE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).setWeight(10))
/*  751 */           .add(LootItem.lootTableItem(Items.HONEYCOMB).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))).setWeight(10))
/*  752 */           .add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 10.0F))).setWeight(10))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  758 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_INTERSECTION, 
/*  759 */         LootTable.lootTable()
/*  760 */         .withPool(LootPool.lootPool()
/*  761 */           .setRolls(UniformGenerator.between(1.0F, 3.0F))
/*  762 */           .add(LootItem.lootTableItem(Items.DIAMOND_BLOCK).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).setWeight(1))
/*  763 */           .add(LootItem.lootTableItem(Items.EMERALD_BLOCK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))).setWeight(5))
/*  764 */           .add(LootItem.lootTableItem(Items.DIAMOND_AXE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.1F, 0.5F))).setWeight(5))
/*  765 */           .add(LootItem.lootTableItem(Items.DIAMOND_PICKAXE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.1F, 0.5F))).setWeight(5))
/*  766 */           .add(LootItem.lootTableItem(Items.DIAMOND).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))).setWeight(10))
/*  767 */           .add(LootItem.lootTableItem(Items.CAKE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))).setWeight(20))
/*  768 */           .add(LootItem.lootTableItem(Items.AMETHYST_SHARD).apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 20.0F))).setWeight(20))
/*  769 */           .add(LootItem.lootTableItem(Items.IRON_BLOCK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))).setWeight(20))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  775 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_INTERSECTION_BARREL, 
/*  776 */         LootTable.lootTable()
/*  777 */         .withPool(LootPool.lootPool()
/*  778 */           .setRolls(UniformGenerator.between(1.0F, 3.0F))
/*  779 */           .add(LootItem.lootTableItem(Items.DIAMOND_AXE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.4F, 0.9F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)).setWeight(1))
/*  780 */           .add(LootItem.lootTableItem(Items.DIAMOND_PICKAXE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.15F, 0.8F))).setWeight(1))
/*  781 */           .add(LootItem.lootTableItem(Items.DIAMOND).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))).setWeight(1))
/*  782 */           .add(LootItem.lootTableItem(Items.COMPASS).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.15F, 0.8F))).setWeight(1))
/*  783 */           .add(LootItem.lootTableItem(Items.BUCKET).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))).setWeight(1))
/*  784 */           .add(LootItem.lootTableItem(Items.GOLDEN_AXE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.15F, 0.8F))).setWeight(4))
/*  785 */           .add(LootItem.lootTableItem(Items.GOLDEN_PICKAXE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.15F, 0.8F))).setWeight(4))
/*  786 */           .add(LootItem.lootTableItem(Items.BAMBOO_PLANKS).apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 15.0F))).setWeight(10))
/*  787 */           .add(LootItem.lootTableItem(Items.BAKED_POTATO).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 10.0F))).setWeight(10))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  794 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_CORRIDOR, 
/*  795 */         LootTable.lootTable()
/*  796 */         .withPool(LootPool.lootPool()
/*  797 */           .setRolls(UniformGenerator.between(1.0F, 3.0F))
/*  798 */           .add(LootItem.lootTableItem(Items.IRON_AXE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.4F, 0.9F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)).setWeight(1))
/*  799 */           .add(LootItem.lootTableItem(Items.HONEYCOMB).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))).setWeight(1))
/*  800 */           .add(LootItem.lootTableItem(Items.STONE_AXE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.15F, 0.8F))).setWeight(2))
/*  801 */           .add(LootItem.lootTableItem(Items.STONE_PICKAXE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.15F, 0.8F))).setWeight(2))
/*  802 */           .add(LootItem.lootTableItem(Items.ENDER_PEARL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))).setWeight(2))
/*  803 */           .add(LootItem.lootTableItem(Items.BAMBOO_HANGING_SIGN).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))).setWeight(2))
/*  804 */           .add(LootItem.lootTableItem(Items.BAMBOO_PLANKS).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F))).setWeight(2))
/*  805 */           .add(LootItem.lootTableItem(Items.SCAFFOLDING).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 10.0F))).setWeight(2))
/*  806 */           .add(LootItem.lootTableItem(Items.TORCH).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F))).setWeight(2))
/*  807 */           .add(LootItem.lootTableItem(Items.TUFF).apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 20.0F))).setWeight(3))));
/*      */ 
/*      */ 
/*      */     
/*  811 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_RARE, 
/*  812 */         LootTable.lootTable()
/*  813 */         .withPool(LootPool.lootPool()
/*  814 */           .setRolls(ConstantValue.exactly(1.0F))
/*  815 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
/*  816 */           .add(LootItem.lootTableItem(Items.SHIELD).setWeight(3).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.5F, 1.0F))))
/*  817 */           .add(LootItem.lootTableItem(Items.BOW).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(5.0F, 15.0F))))
/*  818 */           .add(LootItem.lootTableItem(Items.CROSSBOW).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(5.0F, 20.0F))))
/*  819 */           .add(LootItem.lootTableItem(Items.IRON_AXE).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(0.0F, 10.0F))))
/*  820 */           .add(LootItem.lootTableItem(Items.IRON_CHESTPLATE).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(0.0F, 10.0F))))
/*  821 */           .add(LootItem.lootTableItem(Items.GOLDEN_CARROT).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
/*  822 */           .add(LootItem.lootTableItem(Items.BOOK).setWeight(2).apply((new EnchantRandomlyFunction.Builder())
/*  823 */               .withOneOf(HolderSet.direct(new Holder[] {
/*  824 */                     enchantments.getOrThrow(Enchantments.SHARPNESS), enchantments
/*  825 */                     .getOrThrow(Enchantments.BANE_OF_ARTHROPODS), enchantments
/*  826 */                     .getOrThrow(Enchantments.EFFICIENCY), enchantments
/*  827 */                     .getOrThrow(Enchantments.FORTUNE), enchantments
/*  828 */                     .getOrThrow(Enchantments.SILK_TOUCH), enchantments
/*  829 */                     .getOrThrow(Enchantments.FEATHER_FALLING)
/*      */ 
/*      */                   
/*  832 */                   })))).add(LootItem.lootTableItem(Items.BOOK).setWeight(2).apply((new EnchantRandomlyFunction.Builder())
/*  833 */               .withOneOf(HolderSet.direct(new Holder[] {
/*  834 */                     enchantments.getOrThrow(Enchantments.RIPTIDE), enchantments
/*  835 */                     .getOrThrow(Enchantments.LOYALTY), enchantments
/*  836 */                     .getOrThrow(Enchantments.CHANNELING), enchantments
/*  837 */                     .getOrThrow(Enchantments.IMPALING), enchantments
/*  838 */                     .getOrThrow(Enchantments.MENDING)
/*      */                   
/*  840 */                   })))).add(LootItem.lootTableItem(Items.DIAMOND_CHESTPLATE).setWeight(1).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(5.0F, 15.0F))))
/*  841 */           .add(LootItem.lootTableItem(Items.DIAMOND_AXE).setWeight(1).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(5.0F, 15.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  845 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_COMMON, 
/*  846 */         LootTable.lootTable()
/*  847 */         .withPool(LootPool.lootPool()
/*  848 */           .setRolls(ConstantValue.exactly(1.0F))
/*  849 */           .add(LootItem.lootTableItem(Items.ARROW).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
/*  850 */           .add(LootItem.lootTableItem(Items.TIPPED_ARROW).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))).apply(SetPotionFunction.setPotion(Potions.POISON)))
/*  851 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
/*  852 */           .add(LootItem.lootTableItem(Items.WIND_CHARGE).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  853 */           .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/*  854 */           .add(LootItem.lootTableItem(Items.HONEY_BOTTLE).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
/*  855 */           .add(LootItem.lootTableItem(Items.OMINOUS_BOTTLE).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetOminousBottleAmplifierFunction.setAmplifier(UniformGenerator.between(0.0F, 1.0F))))
/*  856 */           .add(LootItem.lootTableItem(Items.WIND_CHARGE).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 12.0F))))
/*  857 */           .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  861 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_UNIQUE, 
/*  862 */         LootTable.lootTable()
/*  863 */         .withPool(LootPool.lootPool()
/*  864 */           .setRolls(ConstantValue.exactly(1.0F))
/*  865 */           .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(4))
/*  866 */           .add(LootItem.lootTableItem(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(3))
/*  867 */           .add(LootItem.lootTableItem(Items.GUSTER_BANNER_PATTERN).setWeight(2))
/*  868 */           .add(LootItem.lootTableItem(Items.MUSIC_DISC_PRECIPICE).setWeight(2))
/*  869 */           .add(LootItem.lootTableItem(Items.TRIDENT).setWeight(1))));
/*      */ 
/*      */ 
/*      */     
/*  873 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD, 
/*  874 */         LootTable.lootTable()
/*  875 */         .withPool(LootPool.lootPool()
/*  876 */           .setRolls(ConstantValue.exactly(1.0F))
/*  877 */           .add(NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_RARE).setWeight(8))
/*  878 */           .add(NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_COMMON).setWeight(2)))
/*      */         
/*  880 */         .withPool(LootPool.lootPool()
/*  881 */           .setRolls(UniformGenerator.between(1.0F, 3.0F))
/*  882 */           .add(NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_COMMON)))
/*      */         
/*  884 */         .withPool(LootPool.lootPool()
/*  885 */           .setRolls(ConstantValue.exactly(1.0F))
/*  886 */           .when(LootItemRandomChanceCondition.randomChance(0.25F))
/*  887 */           .add(NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_UNIQUE))));
/*      */ 
/*      */ 
/*      */     
/*  891 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_RARE, 
/*  892 */         LootTable.lootTable()
/*  893 */         .withPool(LootPool.lootPool()
/*  894 */           .setRolls(ConstantValue.exactly(1.0F))
/*  895 */           .add(LootItem.lootTableItem(Items.EMERALD_BLOCK).setWeight(5))
/*  896 */           .add(LootItem.lootTableItem(Items.IRON_BLOCK).setWeight(4))
/*  897 */           .add(LootItem.lootTableItem(Items.CROSSBOW).setWeight(4).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(5.0F, 20.0F))))
/*  898 */           .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(3))
/*  899 */           .add(LootItem.lootTableItem(Items.DIAMOND_AXE).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(10.0F, 20.0F))))
/*  900 */           .add(LootItem.lootTableItem(Items.DIAMOND_CHESTPLATE).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(10.0F, 20.0F))))
/*  901 */           .add(LootItem.lootTableItem(Items.BOOK).setWeight(2).apply((new EnchantRandomlyFunction.Builder())
/*  902 */               .withOneOf(HolderSet.direct(new Holder[] {
/*  903 */                     enchantments.getOrThrow(Enchantments.KNOCKBACK), enchantments
/*  904 */                     .getOrThrow(Enchantments.PUNCH), enchantments
/*  905 */                     .getOrThrow(Enchantments.SMITE), enchantments
/*  906 */                     .getOrThrow(Enchantments.LOOTING), enchantments
/*  907 */                     .getOrThrow(Enchantments.MULTISHOT)
/*      */                   
/*  909 */                   })))).add(LootItem.lootTableItem(Items.BOOK).setWeight(2).apply((new EnchantRandomlyFunction.Builder())
/*  910 */               .withOneOf(HolderSet.direct(new Holder[] {
/*  911 */                     enchantments.getOrThrow(Enchantments.BREACH), enchantments
/*  912 */                     .getOrThrow(Enchantments.DENSITY)
/*      */                   
/*  914 */                   })))).add(LootItem.lootTableItem(Items.BOOK).setWeight(2).apply((new SetEnchantmentsFunction.Builder())
/*  915 */               .withEnchantment(enchantments.getOrThrow(Enchantments.WIND_BURST), ConstantValue.exactly(1.0F))))
/*  916 */           .add(LootItem.lootTableItem(Items.DIAMOND_BLOCK).setWeight(1))));
/*      */ 
/*      */ 
/*      */     
/*  920 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_COMMON, 
/*  921 */         LootTable.lootTable()
/*  922 */         .withPool(LootPool.lootPool()
/*  923 */           .setRolls(ConstantValue.exactly(1.0F))
/*  924 */           .add(LootItem.lootTableItem(Items.EMERALD).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 10.0F))))
/*  925 */           .add(LootItem.lootTableItem(Items.WIND_CHARGE).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 12.0F))))
/*  926 */           .add(LootItem.lootTableItem(Items.TIPPED_ARROW).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 12.0F))).apply(SetPotionFunction.setPotion(Potions.STRONG_SLOWNESS)))
/*  927 */           .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F))))
/*  928 */           .add(LootItem.lootTableItem(Items.OMINOUS_BOTTLE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetOminousBottleAmplifierFunction.setAmplifier(UniformGenerator.between(2.0F, 4.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  932 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_UNIQUE, 
/*  933 */         LootTable.lootTable()
/*  934 */         .withPool(LootPool.lootPool()
/*  935 */           .setRolls(ConstantValue.exactly(1.0F))
/*  936 */           .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(3))
/*  937 */           .add(LootItem.lootTableItem(Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(3))
/*  938 */           .add(LootItem.lootTableItem(Items.FLOW_BANNER_PATTERN).setWeight(2))
/*  939 */           .add(LootItem.lootTableItem(Items.MUSIC_DISC_CREATOR).setWeight(1))
/*  940 */           .add(LootItem.lootTableItem(Items.HEAVY_CORE).setWeight(1))));
/*      */ 
/*      */ 
/*      */     
/*  944 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS, 
/*  945 */         LootTable.lootTable()
/*  946 */         .withPool(LootPool.lootPool()
/*  947 */           .setRolls(ConstantValue.exactly(1.0F))
/*  948 */           .add(NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_RARE).setWeight(8))
/*  949 */           .add(NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_COMMON).setWeight(2)))
/*      */         
/*  951 */         .withPool(LootPool.lootPool()
/*  952 */           .setRolls(UniformGenerator.between(1.0F, 3.0F))
/*  953 */           .add(NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_COMMON)))
/*      */         
/*  955 */         .withPool(LootPool.lootPool()
/*  956 */           .setRolls(ConstantValue.exactly(1.0F))
/*  957 */           .when(LootItemRandomChanceCondition.randomChance(0.75F))
/*  958 */           .add(NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_UNIQUE))));
/*      */ 
/*      */ 
/*      */     
/*  962 */     spawnerLootTables(output);
/*      */   }
/*      */   
/*      */   public void spawnerLootTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/*  966 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/*      */ 
/*      */ 
/*      */     
/*  970 */     output.accept(BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_KEY, 
/*  971 */         LootTable.lootTable()
/*  972 */         .withPool(LootPool.lootPool()
/*  973 */           .setRolls(ConstantValue.exactly(1.0F))
/*  974 */           .add(LootItem.lootTableItem(Items.TRIAL_KEY))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  981 */     output.accept(BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_CONSUMABLES, 
/*  982 */         LootTable.lootTable()
/*  983 */         .withPool(LootPool.lootPool()
/*  984 */           .setRolls(ConstantValue.exactly(1.0F))
/*  985 */           .add(LootItem.lootTableItem(Items.COOKED_CHICKEN).setWeight(3).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*  986 */           .add(LootItem.lootTableItem(Items.BREAD).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  987 */           .add(LootItem.lootTableItem(Items.BAKED_POTATO).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*  988 */           .add(LootItem.lootTableItem(Items.POTION).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetPotionFunction.setPotion(Potions.REGENERATION)))
/*  989 */           .add(LootItem.lootTableItem(Items.POTION).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetPotionFunction.setPotion(Potions.SWIFTNESS)))));
/*      */ 
/*      */ 
/*      */     
/*  993 */     output.accept(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 
/*  994 */         LootTable.lootTable()
/*  995 */         .withPool(LootPool.lootPool()
/*  996 */           .setRolls(ConstantValue.exactly(1.0F))
/*  997 */           .add(LootItem.lootTableItem(Items.OMINOUS_TRIAL_KEY))));
/*      */ 
/*      */ 
/*      */     
/* 1001 */     output.accept(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 
/* 1002 */         LootTable.lootTable()
/* 1003 */         .withPool(LootPool.lootPool()
/* 1004 */           .setRolls(ConstantValue.exactly(1.0F))
/* 1005 */           .add(LootItem.lootTableItem(Items.COOKED_BEEF).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
/* 1006 */           .add(LootItem.lootTableItem(Items.BAKED_POTATO).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
/* 1007 */           .add(LootItem.lootTableItem(Items.GOLDEN_CARROT).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
/* 1008 */           .add(LootItem.lootTableItem(Items.POTION).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetPotionFunction.setPotion(Potions.REGENERATION)))
/* 1009 */           .add(LootItem.lootTableItem(Items.POTION).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetPotionFunction.setPotion(Potions.STRENGTH)))));
/*      */ 
/*      */ 
/*      */     
/* 1013 */     output.accept(BuiltInLootTables.SPAWNER_TRIAL_ITEMS_TO_DROP_WHEN_OMINOUS, 
/* 1014 */         LootTable.lootTable()
/* 1015 */         .withPool(LootPool.lootPool()
/* 1016 */           .setRolls(ConstantValue.exactly(1.0F))
/* 1017 */           .add(LootItem.lootTableItem(Items.LINGERING_POTION).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetPotionFunction.setPotion(Potions.WIND_CHARGED)))
/* 1018 */           .add(LootItem.lootTableItem(Items.LINGERING_POTION).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetPotionFunction.setPotion(Potions.OOZING)))
/* 1019 */           .add(LootItem.lootTableItem(Items.LINGERING_POTION).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetPotionFunction.setPotion(Potions.WEAVING)))
/* 1020 */           .add(LootItem.lootTableItem(Items.LINGERING_POTION).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetPotionFunction.setPotion(Potions.INFESTED)))
/* 1021 */           .add(LootItem.lootTableItem(Items.LINGERING_POTION).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetPotionFunction.setPotion(Potions.STRENGTH)))
/* 1022 */           .add(LootItem.lootTableItem(Items.LINGERING_POTION).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetPotionFunction.setPotion(Potions.SWIFTNESS)))
/* 1023 */           .add(LootItem.lootTableItem(Items.LINGERING_POTION).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetPotionFunction.setPotion(Potions.SLOW_FALLING))))
/*      */         
/* 1025 */         .withPool(LootPool.lootPool()
/* 1026 */           .setRolls(ConstantValue.exactly(1.0F))
/* 1027 */           .add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1028 */           .add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetPotionFunction.setPotion(Potions.POISON)))
/* 1029 */           .add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetPotionFunction.setPotion(Potions.STRONG_SLOWNESS)))
/* 1030 */           .add(LootItem.lootTableItem(Items.FIRE_CHARGE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1031 */           .add(LootItem.lootTableItem(Items.WIND_CHARGE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public LootTable.Builder shipwreckSupplyLootTable() {
/* 1037 */     return LootTable.lootTable()
/* 1038 */       .withPool(LootPool.lootPool()
/* 1039 */         .setRolls(UniformGenerator.between(3.0F, 10.0F))
/* 1040 */         .add(LootItem.lootTableItem(Items.PAPER).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 12.0F))))
/* 1041 */         .add(LootItem.lootTableItem(Items.POTATO).setWeight(7).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
/* 1042 */         .add(LootItem.lootTableItem(Items.MOSS_BLOCK).setWeight(7).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/* 1043 */         .add(LootItem.lootTableItem(Items.POISONOUS_POTATO).setWeight(7).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
/* 1044 */         .add(LootItem.lootTableItem(Items.CARROT).setWeight(7).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))))
/* 1045 */         .add(LootItem.lootTableItem(Items.WHEAT).setWeight(7).apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 21.0F))))
/* 1046 */         .add(LootItem.lootTableItem(Items.SUSPICIOUS_STEW).setWeight(10).apply(SetStewEffectFunction.stewEffect()
/* 1047 */             .withEffect(MobEffects.NIGHT_VISION, UniformGenerator.between(7.0F, 10.0F))
/* 1048 */             .withEffect(MobEffects.JUMP_BOOST, UniformGenerator.between(7.0F, 10.0F))
/* 1049 */             .withEffect(MobEffects.WEAKNESS, UniformGenerator.between(6.0F, 8.0F))
/* 1050 */             .withEffect(MobEffects.BLINDNESS, UniformGenerator.between(5.0F, 7.0F))
/* 1051 */             .withEffect(MobEffects.POISON, UniformGenerator.between(10.0F, 20.0F))
/* 1052 */             .withEffect(MobEffects.SATURATION, UniformGenerator.between(7.0F, 10.0F))))
/*      */         
/* 1054 */         .add(LootItem.lootTableItem(Items.COAL).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
/* 1055 */         .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 24.0F))))
/* 1056 */         .add(LootItem.lootTableItem(Blocks.PUMPKIN).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1057 */         .add(LootItem.lootTableItem(Blocks.BAMBOO).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1058 */         .add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/* 1059 */         .add(LootItem.lootTableItem(Blocks.TNT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
/* 1060 */         .add(LootItem.lootTableItem(Items.LEATHER_HELMET).setWeight(3).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1061 */         .add(LootItem.lootTableItem(Items.LEATHER_CHESTPLATE).setWeight(3).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1062 */         .add(LootItem.lootTableItem(Items.LEATHER_LEGGINGS).setWeight(3).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1063 */         .add(LootItem.lootTableItem(Items.LEATHER_BOOTS).setWeight(3).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))))
/*      */       
/* 1065 */       .withPool(LootPool.lootPool()
/* 1066 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1067 */         .add(EmptyLootItem.emptyItem().setWeight(5))
/* 1068 */         .add(LootItem.lootTableItem(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))
/*      */       
/* 1070 */       .withPool(LootPool.lootPool()
/* 1071 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1072 */         .add(EmptyLootItem.emptyItem().setWeight(148))
/* 1073 */         .add(LootItem.lootTableItem(Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1074 */         .add(LootItem.lootTableItem(Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1075 */         .add(LootItem.lootTableItem(Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1076 */         .add(LootItem.lootTableItem(Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder shipwreckMapLootTable() {
/* 1081 */     return LootTable.lootTable()
/* 1082 */       .withPool(LootPool.lootPool()
/* 1083 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1084 */         .add(LootItem.lootTableItem(Items.MAP).apply(ExplorationMapFunction.makeExplorationMap().setDestination(StructureTags.ON_TREASURE_MAPS).setMapDecoration(MapDecorationTypes.RED_X).setZoom((byte)1).setSkipKnownStructures(false)).apply(SetNameFunction.setName(Component.translatable("filled_map.buried_treasure"), SetNameFunction.Target.ITEM_NAME))))
/*      */       
/* 1086 */       .withPool(LootPool.lootPool()
/* 1087 */         .setRolls(ConstantValue.exactly(3.0F))
/* 1088 */         .add(LootItem.lootTableItem(Items.COMPASS))
/* 1089 */         .add(LootItem.lootTableItem(Items.MAP))
/* 1090 */         .add(LootItem.lootTableItem(Items.CLOCK))
/* 1091 */         .add(LootItem.lootTableItem(Items.PAPER).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F))))
/* 1092 */         .add(LootItem.lootTableItem(Items.FEATHER).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/* 1093 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F)))))
/*      */       
/* 1095 */       .withPool(LootPool.lootPool()
/* 1096 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1097 */         .add(EmptyLootItem.emptyItem().setWeight(5))
/* 1098 */         .add(LootItem.lootTableItem(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))
/*      */       
/* 1100 */       .withPool(LootPool.lootPool()
/* 1101 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1102 */         .add(EmptyLootItem.emptyItem().setWeight(148))
/* 1103 */         .add(LootItem.lootTableItem(Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1104 */         .add(LootItem.lootTableItem(Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1105 */         .add(LootItem.lootTableItem(Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1106 */         .add(LootItem.lootTableItem(Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder bastionHoglinStableLootTable() {
/* 1111 */     return LootTable.lootTable()
/* 1112 */       .withPool(LootPool.lootPool()
/* 1113 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1114 */         .add(LootItem.lootTableItem(Items.DIAMOND_SHOVEL).setWeight(15).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.15F, 0.8F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1115 */         .add(LootItem.lootTableItem(Items.DIAMOND_PICKAXE).setWeight(12).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.15F, 0.95F))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1116 */         .add(LootItem.lootTableItem(Items.NETHERITE_SCRAP).setWeight(8).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1117 */         .add(LootItem.lootTableItem(Items.ANCIENT_DEBRIS).setWeight(12).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1118 */         .add(LootItem.lootTableItem(Items.ANCIENT_DEBRIS).setWeight(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))))
/* 1119 */         .add(LootItem.lootTableItem(Items.SADDLE).setWeight(12).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1120 */         .add(LootItem.lootTableItem(Blocks.GOLD_BLOCK).setWeight(16).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
/* 1121 */         .add(LootItem.lootTableItem(Items.GOLDEN_CARROT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 17.0F))))
/* 1122 */         .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(10).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))))
/*      */       
/* 1124 */       .withPool(LootPool.lootPool()
/* 1125 */         .setRolls(UniformGenerator.between(3.0F, 4.0F))
/* 1126 */         .add(LootItem.lootTableItem(Items.GOLDEN_AXE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1127 */         .add(LootItem.lootTableItem(Blocks.CRYING_OBSIDIAN).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/* 1128 */         .add(LootItem.lootTableItem(Blocks.GLOWSTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F))))
/* 1129 */         .add(LootItem.lootTableItem(Blocks.GILDED_BLACKSTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))))
/* 1130 */         .add(LootItem.lootTableItem(Blocks.SOUL_SAND).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F))))
/* 1131 */         .add(LootItem.lootTableItem(Blocks.CRIMSON_NYLIUM).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F))))
/* 1132 */         .add(LootItem.lootTableItem(Items.GOLD_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
/* 1133 */         .add(LootItem.lootTableItem(Items.LEATHER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1134 */         .add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 17.0F))))
/* 1135 */         .add(LootItem.lootTableItem(Items.STRING).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F))))
/* 1136 */         .add(LootItem.lootTableItem(Items.PORKCHOP).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))))
/* 1137 */         .add(LootItem.lootTableItem(Items.COOKED_PORKCHOP).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))))
/* 1138 */         .add(LootItem.lootTableItem(Blocks.CRIMSON_FUNGUS).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F))))
/* 1139 */         .add(LootItem.lootTableItem(Blocks.CRIMSON_ROOTS).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F)))))
/*      */       
/* 1141 */       .withPool(LootPool.lootPool()
/* 1142 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1143 */         .add(EmptyLootItem.emptyItem().setWeight(11))
/* 1144 */         .add(LootItem.lootTableItem(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)))
/*      */       
/* 1146 */       .withPool(LootPool.lootPool()
/* 1147 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1148 */         .add(EmptyLootItem.emptyItem().setWeight(9))
/* 1149 */         .add(LootItem.lootTableItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder bastionBridgeLootTable() {
/* 1154 */     return LootTable.lootTable()
/* 1155 */       .withPool(LootPool.lootPool()
/* 1156 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1157 */         .add(LootItem.lootTableItem(Blocks.LODESTONE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))))
/*      */       
/* 1159 */       .withPool(LootPool.lootPool()
/* 1160 */         .setRolls(UniformGenerator.between(1.0F, 2.0F))
/* 1161 */         .add(LootItem.lootTableItem(Items.CROSSBOW).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.1F, 0.5F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1162 */         .add(LootItem.lootTableItem(Items.SPECTRAL_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(10.0F, 28.0F))))
/* 1163 */         .add(LootItem.lootTableItem(Blocks.GILDED_BLACKSTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 12.0F))))
/* 1164 */         .add(LootItem.lootTableItem(Blocks.CRYING_OBSIDIAN).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F))))
/* 1165 */         .add(LootItem.lootTableItem(Blocks.GOLD_BLOCK).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1166 */         .add(LootItem.lootTableItem(Items.GOLD_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
/* 1167 */         .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
/* 1168 */         .add(LootItem.lootTableItem(Items.GOLDEN_SWORD).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1169 */         .add(LootItem.lootTableItem(Items.GOLDEN_CHESTPLATE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1170 */         .add(LootItem.lootTableItem(Items.GOLDEN_HELMET).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1171 */         .add(LootItem.lootTableItem(Items.GOLDEN_LEGGINGS).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1172 */         .add(LootItem.lootTableItem(Items.GOLDEN_BOOTS).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1173 */         .add(LootItem.lootTableItem(Items.GOLDEN_AXE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))))
/*      */       
/* 1175 */       .withPool(LootPool.lootPool()
/* 1176 */         .setRolls(UniformGenerator.between(2.0F, 4.0F))
/* 1177 */         .add(LootItem.lootTableItem(Items.STRING).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 6.0F))))
/* 1178 */         .add(LootItem.lootTableItem(Items.LEATHER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1179 */         .add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 17.0F))))
/* 1180 */         .add(LootItem.lootTableItem(Items.IRON_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
/* 1181 */         .add(LootItem.lootTableItem(Items.GOLD_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F)))))
/*      */       
/* 1183 */       .withPool(LootPool.lootPool()
/* 1184 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1185 */         .add(EmptyLootItem.emptyItem().setWeight(11))
/* 1186 */         .add(LootItem.lootTableItem(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)))
/*      */       
/* 1188 */       .withPool(LootPool.lootPool()
/* 1189 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1190 */         .add(EmptyLootItem.emptyItem().setWeight(9))
/* 1191 */         .add(LootItem.lootTableItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder endCityTreasureLootTable() {
/* 1196 */     return LootTable.lootTable()
/* 1197 */       .withPool(LootPool.lootPool()
/* 1198 */         .setRolls(UniformGenerator.between(2.0F, 6.0F))
/* 1199 */         .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F))))
/* 1200 */         .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))))
/* 1201 */         .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F))))
/* 1202 */         .add(LootItem.lootTableItem(Items.EMERALD).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
/* 1203 */         .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F))))
/* 1204 */         .add(LootItem.lootTableItem(Items.SADDLE).setWeight(3))
/* 1205 */         .add(LootItem.lootTableItem(Items.COPPER_HORSE_ARMOR))
/* 1206 */         .add(LootItem.lootTableItem(Items.IRON_HORSE_ARMOR))
/* 1207 */         .add(LootItem.lootTableItem(Items.GOLDEN_HORSE_ARMOR))
/* 1208 */         .add(LootItem.lootTableItem(Items.DIAMOND_HORSE_ARMOR))
/* 1209 */         .add(LootItem.lootTableItem(Items.DIAMOND_SWORD).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(20.0F, 39.0F))))
/* 1210 */         .add(LootItem.lootTableItem(Items.DIAMOND_SPEAR).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(20.0F, 39.0F))))
/* 1211 */         .add(LootItem.lootTableItem(Items.DIAMOND_BOOTS).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(20.0F, 39.0F))))
/* 1212 */         .add(LootItem.lootTableItem(Items.DIAMOND_CHESTPLATE).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(20.0F, 39.0F))))
/* 1213 */         .add(LootItem.lootTableItem(Items.DIAMOND_LEGGINGS).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(20.0F, 39.0F))))
/* 1214 */         .add(LootItem.lootTableItem(Items.DIAMOND_HELMET).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(20.0F, 39.0F))))
/* 1215 */         .add(LootItem.lootTableItem(Items.DIAMOND_PICKAXE).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(20.0F, 39.0F))))
/* 1216 */         .add(LootItem.lootTableItem(Items.DIAMOND_SHOVEL).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(20.0F, 39.0F))))
/* 1217 */         .add(LootItem.lootTableItem(Items.IRON_SWORD).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(20.0F, 39.0F))))
/* 1218 */         .add(LootItem.lootTableItem(Items.IRON_BOOTS).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(20.0F, 39.0F))))
/* 1219 */         .add(LootItem.lootTableItem(Items.IRON_CHESTPLATE).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(20.0F, 39.0F))))
/* 1220 */         .add(LootItem.lootTableItem(Items.IRON_LEGGINGS).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(20.0F, 39.0F))))
/* 1221 */         .add(LootItem.lootTableItem(Items.IRON_HELMET).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(20.0F, 39.0F))))
/* 1222 */         .add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(20.0F, 39.0F))))
/* 1223 */         .add(LootItem.lootTableItem(Items.IRON_SHOVEL).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(20.0F, 39.0F)))))
/*      */       
/* 1225 */       .withPool(LootPool.lootPool()
/* 1226 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1227 */         .add(EmptyLootItem.emptyItem().setWeight(14))
/* 1228 */         .add(LootItem.lootTableItem(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder netherBridgeLootTable() {
/* 1233 */     return LootTable.lootTable()
/* 1234 */       .withPool(LootPool.lootPool()
/* 1235 */         .setRolls(UniformGenerator.between(2.0F, 4.0F))
/* 1236 */         .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1237 */         .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/* 1238 */         .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1239 */         .add(LootItem.lootTableItem(Items.GOLDEN_SWORD).setWeight(5))
/* 1240 */         .add(LootItem.lootTableItem(Items.GOLDEN_CHESTPLATE).setWeight(5))
/* 1241 */         .add(LootItem.lootTableItem(Items.FLINT_AND_STEEL).setWeight(5))
/* 1242 */         .add(LootItem.lootTableItem(Items.NETHER_WART).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
/* 1243 */         .add(LootItem.lootTableItem(Items.SADDLE).setWeight(10))
/* 1244 */         .add(LootItem.lootTableItem(Items.GOLDEN_HORSE_ARMOR).setWeight(8))
/* 1245 */         .add(LootItem.lootTableItem(Items.COPPER_HORSE_ARMOR).setWeight(5))
/* 1246 */         .add(LootItem.lootTableItem(Items.IRON_HORSE_ARMOR).setWeight(5))
/* 1247 */         .add(LootItem.lootTableItem(Items.DIAMOND_HORSE_ARMOR).setWeight(3))
/* 1248 */         .add(LootItem.lootTableItem(Blocks.OBSIDIAN).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))))
/*      */       
/* 1250 */       .withPool(LootPool.lootPool()
/* 1251 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1252 */         .add(EmptyLootItem.emptyItem().setWeight(14))
/* 1253 */         .add(LootItem.lootTableItem(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder bastionTreasureLootTable() {
/* 1258 */     return LootTable.lootTable()
/* 1259 */       .withPool(LootPool.lootPool()
/* 1260 */         .setRolls(ConstantValue.exactly(3.0F))
/* 1261 */         .add(LootItem.lootTableItem(Items.NETHERITE_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1262 */         .add(LootItem.lootTableItem(Blocks.ANCIENT_DEBRIS).setWeight(10).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1263 */         .add(LootItem.lootTableItem(Items.NETHERITE_SCRAP).setWeight(8).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1264 */         .add(LootItem.lootTableItem(Blocks.ANCIENT_DEBRIS).setWeight(4).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))))
/* 1265 */         .add(LootItem.lootTableItem(Items.DIAMOND_SWORD).setWeight(6).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.8F, 1.0F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1266 */         .add(LootItem.lootTableItem(Items.DIAMOND_SPEAR).setWeight(6).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.8F, 1.0F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1267 */         .add(LootItem.lootTableItem(Items.DIAMOND_CHESTPLATE).setWeight(6).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.8F, 1.0F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1268 */         .add(LootItem.lootTableItem(Items.DIAMOND_HELMET).setWeight(6).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.8F, 1.0F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1269 */         .add(LootItem.lootTableItem(Items.DIAMOND_LEGGINGS).setWeight(6).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.8F, 1.0F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1270 */         .add(LootItem.lootTableItem(Items.DIAMOND_BOOTS).setWeight(6).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.8F, 1.0F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1271 */         .add(LootItem.lootTableItem(Items.DIAMOND_SWORD).setWeight(6))
/* 1272 */         .add(LootItem.lootTableItem(Items.DIAMOND_SPEAR).setWeight(6))
/* 1273 */         .add(LootItem.lootTableItem(Items.DIAMOND_CHESTPLATE).setWeight(5))
/* 1274 */         .add(LootItem.lootTableItem(Items.DIAMOND_HELMET).setWeight(5))
/* 1275 */         .add(LootItem.lootTableItem(Items.DIAMOND_BOOTS).setWeight(5))
/* 1276 */         .add(LootItem.lootTableItem(Items.DIAMOND_LEGGINGS).setWeight(5))
/* 1277 */         .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
/* 1278 */         .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))))
/*      */       
/* 1280 */       .withPool(LootPool.lootPool()
/* 1281 */         .setRolls(UniformGenerator.between(3.0F, 4.0F))
/* 1282 */         .add(LootItem.lootTableItem(Items.SPECTRAL_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(12.0F, 25.0F))))
/* 1283 */         .add(LootItem.lootTableItem(Blocks.GOLD_BLOCK).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))))
/* 1284 */         .add(LootItem.lootTableItem(Blocks.IRON_BLOCK).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))))
/* 1285 */         .add(LootItem.lootTableItem(Items.GOLD_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
/* 1286 */         .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
/* 1287 */         .add(LootItem.lootTableItem(Blocks.CRYING_OBSIDIAN).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 5.0F))))
/* 1288 */         .add(LootItem.lootTableItem(Items.QUARTZ).apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 23.0F))))
/* 1289 */         .add(LootItem.lootTableItem(Blocks.GILDED_BLACKSTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 15.0F))))
/* 1290 */         .add(LootItem.lootTableItem(Items.MAGMA_CREAM).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F)))))
/*      */       
/* 1292 */       .withPool(LootPool.lootPool()
/* 1293 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1294 */         .add(EmptyLootItem.emptyItem().setWeight(11))
/* 1295 */         .add(LootItem.lootTableItem(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)))
/*      */       
/* 1297 */       .withPool(LootPool.lootPool()
/* 1298 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1299 */         .add(LootItem.lootTableItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder bastionOtherLootTable() {
/* 1304 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 1305 */     return LootTable.lootTable()
/* 1306 */       .withPool(LootPool.lootPool()
/* 1307 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1308 */         .add(LootItem.lootTableItem(Items.DIAMOND_PICKAXE).setWeight(6).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1309 */         .add(LootItem.lootTableItem(Items.DIAMOND_SHOVEL).setWeight(6).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1310 */         .add(LootItem.lootTableItem(Items.CROSSBOW).setWeight(6).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.1F, 0.9F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1311 */         .add(LootItem.lootTableItem(Items.ANCIENT_DEBRIS).setWeight(12).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1312 */         .add(LootItem.lootTableItem(Items.NETHERITE_SCRAP).setWeight(4).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1313 */         .add(LootItem.lootTableItem(Items.SPECTRAL_ARROW).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(10.0F, 22.0F))))
/* 1314 */         .add(LootItem.lootTableItem(Items.PIGLIN_BANNER_PATTERN).setWeight(9).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1315 */         .add(LootItem.lootTableItem(Items.MUSIC_DISC_PIGSTEP).setWeight(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1316 */         .add(LootItem.lootTableItem(Items.GOLDEN_CARROT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 17.0F))))
/* 1317 */         .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(9).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1318 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(10).apply((new EnchantRandomlyFunction.Builder()).withEnchantment(enchantments.getOrThrow(Enchantments.SOUL_SPEED)))))
/*      */       
/* 1320 */       .withPool(LootPool.lootPool()
/* 1321 */         .setRolls(ConstantValue.exactly(2.0F))
/* 1322 */         .add(LootItem.lootTableItem(Items.IRON_SWORD).setWeight(2).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.1F, 0.9F))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1323 */         .add(LootItem.lootTableItem(Blocks.IRON_BLOCK).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1324 */         .add(LootItem.lootTableItem(Items.GOLDEN_BOOTS).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply((new EnchantRandomlyFunction.Builder()).withEnchantment(enchantments.getOrThrow(Enchantments.SOUL_SPEED))))
/* 1325 */         .add(LootItem.lootTableItem(Items.GOLDEN_AXE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1326 */         .add(LootItem.lootTableItem(Blocks.GOLD_BLOCK).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1327 */         .add(LootItem.lootTableItem(Items.CROSSBOW).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1328 */         .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 6.0F))))
/* 1329 */         .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 6.0F))))
/* 1330 */         .add(LootItem.lootTableItem(Items.GOLDEN_SWORD).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1331 */         .add(LootItem.lootTableItem(Items.GOLDEN_CHESTPLATE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1332 */         .add(LootItem.lootTableItem(Items.GOLDEN_HELMET).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1333 */         .add(LootItem.lootTableItem(Items.GOLDEN_LEGGINGS).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1334 */         .add(LootItem.lootTableItem(Items.GOLDEN_BOOTS).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1335 */         .add(LootItem.lootTableItem(Blocks.CRYING_OBSIDIAN).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F)))))
/*      */       
/* 1337 */       .withPool(LootPool.lootPool()
/* 1338 */         .setRolls(UniformGenerator.between(3.0F, 4.0F))
/* 1339 */         .add(LootItem.lootTableItem(Blocks.GILDED_BLACKSTONE).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/* 1340 */         .add(LootItem.lootTableItem(Blocks.IRON_CHAIN).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 10.0F))))
/* 1341 */         .add(LootItem.lootTableItem(Items.MAGMA_CREAM).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
/* 1342 */         .add(LootItem.lootTableItem(Blocks.BONE_BLOCK).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F))))
/* 1343 */         .add(LootItem.lootTableItem(Items.IRON_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
/* 1344 */         .add(LootItem.lootTableItem(Blocks.OBSIDIAN).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 6.0F))))
/* 1345 */         .add(LootItem.lootTableItem(Items.GOLD_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
/* 1346 */         .add(LootItem.lootTableItem(Items.STRING).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 6.0F))))
/* 1347 */         .add(LootItem.lootTableItem(Items.ARROW).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 17.0F))))
/* 1348 */         .add(LootItem.lootTableItem(Items.COOKED_PORKCHOP).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))))
/*      */       
/* 1350 */       .withPool(LootPool.lootPool()
/* 1351 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1352 */         .add(EmptyLootItem.emptyItem().setWeight(11))
/* 1353 */         .add(LootItem.lootTableItem(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)))
/*      */       
/* 1355 */       .withPool(LootPool.lootPool()
/* 1356 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1357 */         .add(EmptyLootItem.emptyItem().setWeight(9))
/* 1358 */         .add(LootItem.lootTableItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder woodlandMansionLootTable() {
/* 1363 */     return LootTable.lootTable()
/* 1364 */       .withPool(LootPool.lootPool()
/* 1365 */         .setRolls(UniformGenerator.between(1.0F, 3.0F))
/* 1366 */         .add(LootItem.lootTableItem(Items.LEAD).setWeight(20))
/* 1367 */         .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(15))
/* 1368 */         .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
/* 1369 */         .add(LootItem.lootTableItem(Items.MUSIC_DISC_13).setWeight(15))
/* 1370 */         .add(LootItem.lootTableItem(Items.MUSIC_DISC_CAT).setWeight(15))
/* 1371 */         .add(LootItem.lootTableItem(Items.NAME_TAG).setWeight(20))
/* 1372 */         .add(LootItem.lootTableItem(Items.CHAINMAIL_CHESTPLATE).setWeight(10))
/* 1373 */         .add(LootItem.lootTableItem(Items.DIAMOND_HOE).setWeight(15))
/* 1374 */         .add(LootItem.lootTableItem(Items.DIAMOND_CHESTPLATE).setWeight(5))
/* 1375 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(10).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))))
/*      */       
/* 1377 */       .withPool(LootPool.lootPool()
/* 1378 */         .setRolls(UniformGenerator.between(1.0F, 4.0F))
/* 1379 */         .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/* 1380 */         .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/* 1381 */         .add(LootItem.lootTableItem(Items.BREAD).setWeight(20))
/* 1382 */         .add(LootItem.lootTableItem(Items.WHEAT).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/* 1383 */         .add(LootItem.lootTableItem(Items.BUCKET).setWeight(10))
/* 1384 */         .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/* 1385 */         .add(LootItem.lootTableItem(Items.COAL).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/* 1386 */         .add(LootItem.lootTableItem(Items.MELON_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
/* 1387 */         .add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
/* 1388 */         .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
/* 1389 */         .add(LootItem.lootTableItem(Items.RESIN_CLUMP).setWeight(50).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))))
/*      */       
/* 1391 */       .withPool(LootPool.lootPool()
/* 1392 */         .setRolls(ConstantValue.exactly(3.0F))
/* 1393 */         .add(LootItem.lootTableItem(Items.BONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
/* 1394 */         .add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
/* 1395 */         .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
/* 1396 */         .add(LootItem.lootTableItem(Items.STRING).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F)))))
/*      */       
/* 1398 */       .withPool(LootPool.lootPool()
/* 1399 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1400 */         .add(EmptyLootItem.emptyItem().setWeight(1))
/* 1401 */         .add(LootItem.lootTableItem(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder strongholdLibraryLootTable() {
/* 1406 */     return LootTable.lootTable()
/* 1407 */       .withPool(LootPool.lootPool()
/* 1408 */         .setRolls(UniformGenerator.between(2.0F, 10.0F))
/* 1409 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1410 */         .add(LootItem.lootTableItem(Items.PAPER).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F))))
/* 1411 */         .add(LootItem.lootTableItem(Items.MAP))
/* 1412 */         .add(LootItem.lootTableItem(Items.COMPASS))
/* 1413 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(10).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(30.0F)))))
/*      */       
/* 1415 */       .withPool(LootPool.lootPool()
/* 1416 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1417 */         .add(LootItem.lootTableItem(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder strongholdCorridorLootTable() {
/* 1422 */     return LootTable.lootTable()
/* 1423 */       .withPool(LootPool.lootPool()
/* 1424 */         .setRolls(UniformGenerator.between(2.0F, 3.0F))
/* 1425 */         .add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(10))
/* 1426 */         .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1427 */         .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/* 1428 */         .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1429 */         .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
/* 1430 */         .add(LootItem.lootTableItem(Items.BREAD).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1431 */         .add(LootItem.lootTableItem(Items.APPLE).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1432 */         .add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(5))
/* 1433 */         .add(LootItem.lootTableItem(Items.IRON_SWORD).setWeight(5))
/* 1434 */         .add(LootItem.lootTableItem(Items.IRON_CHESTPLATE).setWeight(5))
/* 1435 */         .add(LootItem.lootTableItem(Items.IRON_HELMET).setWeight(5))
/* 1436 */         .add(LootItem.lootTableItem(Items.IRON_LEGGINGS).setWeight(5))
/* 1437 */         .add(LootItem.lootTableItem(Items.IRON_BOOTS).setWeight(5))
/* 1438 */         .add(LootItem.lootTableItem(Items.GOLDEN_APPLE))
/* 1439 */         .add(LootItem.lootTableItem(Items.LEATHER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/* 1440 */         .add(LootItem.lootTableItem(Items.COPPER_HORSE_ARMOR))
/* 1441 */         .add(LootItem.lootTableItem(Items.IRON_HORSE_ARMOR))
/* 1442 */         .add(LootItem.lootTableItem(Items.GOLDEN_HORSE_ARMOR))
/* 1443 */         .add(LootItem.lootTableItem(Items.DIAMOND_HORSE_ARMOR))
/* 1444 */         .add(LootItem.lootTableItem(Items.MUSIC_DISC_OTHERSIDE))
/* 1445 */         .add(LootItem.lootTableItem(Items.BOOK).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(30.0F)))))
/*      */       
/* 1447 */       .withPool(LootPool.lootPool()
/* 1448 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1449 */         .add(EmptyLootItem.emptyItem().setWeight(9))
/* 1450 */         .add(LootItem.lootTableItem(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder ancientCityLootTable() {
/* 1455 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 1456 */     return LootTable.lootTable()
/* 1457 */       .withPool(LootPool.lootPool()
/* 1458 */         .setRolls(UniformGenerator.between(5.0F, 10.0F))
/*      */ 
/*      */         
/* 1461 */         .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
/* 1462 */         .add(LootItem.lootTableItem(Items.MUSIC_DISC_OTHERSIDE).setWeight(1))
/*      */ 
/*      */         
/* 1465 */         .add(LootItem.lootTableItem(Items.COMPASS).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1466 */         .add(LootItem.lootTableItem(Items.SCULK_CATALYST).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
/* 1467 */         .add(LootItem.lootTableItem(Items.NAME_TAG).setWeight(2))
/* 1468 */         .add(LootItem.lootTableItem(Items.DIAMOND_HOE).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.8F, 1.0F))).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(30.0F, 50.0F))))
/* 1469 */         .add(LootItem.lootTableItem(Items.LEAD).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1470 */         .add(LootItem.lootTableItem(Items.DIAMOND_HORSE_ARMOR).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1471 */         .add(LootItem.lootTableItem(Items.LEATHER).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/* 1472 */         .add(LootItem.lootTableItem(Items.MUSIC_DISC_13).setWeight(2))
/* 1473 */         .add(LootItem.lootTableItem(Items.MUSIC_DISC_CAT).setWeight(2))
/* 1474 */         .add(LootItem.lootTableItem(Items.DIAMOND_LEGGINGS).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(30.0F, 50.0F))))
/*      */ 
/*      */         
/* 1477 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(3).apply((new EnchantRandomlyFunction.Builder()).withEnchantment(enchantments.getOrThrow(Enchantments.SWIFT_SNEAK))))
/* 1478 */         .add(LootItem.lootTableItem(Items.SCULK).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 10.0F))))
/* 1479 */         .add(LootItem.lootTableItem(Items.SCULK_SENSOR).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1480 */         .add(LootItem.lootTableItem(Items.CANDLE).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
/* 1481 */         .add(LootItem.lootTableItem(Items.AMETHYST_SHARD).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 15.0F))))
/* 1482 */         .add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1483 */         .add(LootItem.lootTableItem(Items.GLOW_BERRIES).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 15.0F))))
/* 1484 */         .add(LootItem.lootTableItem(Items.IRON_LEGGINGS).setWeight(3).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(20.0F, 39.0F))))
/*      */         
/* 1486 */         .add(LootItem.lootTableItem(Items.ECHO_SHARD).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1487 */         .add(LootItem.lootTableItem(Items.DISC_FRAGMENT_5).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/*      */ 
/*      */         
/* 1490 */         .add(LootItem.lootTableItem(Items.POTION).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))).apply(SetPotionFunction.setPotion(Potions.STRONG_REGENERATION)))
/* 1491 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(5).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1492 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 10.0F))))
/* 1493 */         .add(LootItem.lootTableItem(Items.BONE).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 15.0F))))
/* 1494 */         .add(LootItem.lootTableItem(Items.SOUL_TORCH).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 15.0F))))
/*      */ 
/*      */         
/* 1497 */         .add(LootItem.lootTableItem(Items.COAL).setWeight(7).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 15.0F)))))
/*      */       
/* 1499 */       .withPool(LootPool.lootPool()
/* 1500 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1501 */         .add(EmptyLootItem.emptyItem().setWeight(75))
/* 1502 */         .add(LootItem.lootTableItem(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(4))
/* 1503 */         .add(LootItem.lootTableItem(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder jungleTempleLootTable() {
/* 1508 */     return LootTable.lootTable()
/* 1509 */       .withPool(LootPool.lootPool()
/* 1510 */         .setRolls(UniformGenerator.between(2.0F, 6.0F))
/* 1511 */         .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1512 */         .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/* 1513 */         .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F))))
/* 1514 */         .add(LootItem.lootTableItem(Blocks.BAMBOO).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1515 */         .add(LootItem.lootTableItem(Items.EMERALD).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1516 */         .add(LootItem.lootTableItem(Items.BONE).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 6.0F))))
/* 1517 */         .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(16).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
/* 1518 */         .add(LootItem.lootTableItem(Items.LEATHER).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/* 1519 */         .add(LootItem.lootTableItem(Items.COPPER_HORSE_ARMOR))
/* 1520 */         .add(LootItem.lootTableItem(Items.IRON_HORSE_ARMOR))
/* 1521 */         .add(LootItem.lootTableItem(Items.GOLDEN_HORSE_ARMOR))
/* 1522 */         .add(LootItem.lootTableItem(Items.DIAMOND_HORSE_ARMOR))
/* 1523 */         .add(LootItem.lootTableItem(Items.BOOK).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(30.0F)))))
/*      */       
/* 1525 */       .withPool(LootPool.lootPool()
/* 1526 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1527 */         .add(EmptyLootItem.emptyItem().setWeight(2))
/* 1528 */         .add(LootItem.lootTableItem(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder shipwreckTreasureLootTable() {
/* 1533 */     return LootTable.lootTable()
/* 1534 */       .withPool(LootPool.lootPool()
/* 1535 */         .setRolls(UniformGenerator.between(3.0F, 6.0F))
/* 1536 */         .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(90).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/* 1537 */         .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/* 1538 */         .add(LootItem.lootTableItem(Items.EMERALD).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/* 1539 */         .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5))
/* 1540 */         .add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).setWeight(5)))
/*      */       
/* 1542 */       .withPool(LootPool.lootPool()
/* 1543 */         .setRolls(UniformGenerator.between(2.0F, 5.0F))
/* 1544 */         .add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(50).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F))))
/* 1545 */         .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F))))
/* 1546 */         .add(LootItem.lootTableItem(Items.LAPIS_LAZULI).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F)))))
/*      */       
/* 1548 */       .withPool(LootPool.lootPool()
/* 1549 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1550 */         .add(EmptyLootItem.emptyItem().setWeight(5))
/* 1551 */         .add(LootItem.lootTableItem(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))
/*      */       
/* 1553 */       .withPool(LootPool.lootPool()
/* 1554 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1555 */         .add(EmptyLootItem.emptyItem().setWeight(148))
/* 1556 */         .add(LootItem.lootTableItem(Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1557 */         .add(LootItem.lootTableItem(Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1558 */         .add(LootItem.lootTableItem(Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/* 1559 */         .add(LootItem.lootTableItem(Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder pillagerOutpostLootTable() {
/* 1564 */     return LootTable.lootTable()
/* 1565 */       .withPool(LootPool.lootPool()
/* 1566 */         .setRolls(UniformGenerator.between(0.0F, 1.0F))
/* 1567 */         .add(LootItem.lootTableItem(Items.CROSSBOW)))
/*      */       
/* 1569 */       .withPool(LootPool.lootPool()
/* 1570 */         .setRolls(UniformGenerator.between(2.0F, 3.0F))
/* 1571 */         .add(LootItem.lootTableItem(Items.WHEAT).setWeight(7).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 5.0F))))
/* 1572 */         .add(LootItem.lootTableItem(Items.POTATO).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))))
/* 1573 */         .add(LootItem.lootTableItem(Items.CARROT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 5.0F)))))
/*      */       
/* 1575 */       .withPool(LootPool.lootPool()
/* 1576 */         .setRolls(UniformGenerator.between(1.0F, 3.0F))
/* 1577 */         .add(LootItem.lootTableItem(Blocks.DARK_OAK_LOG).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))))
/*      */       
/* 1579 */       .withPool(LootPool.lootPool()
/* 1580 */         .setRolls(UniformGenerator.between(2.0F, 3.0F))
/* 1581 */         .add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).setWeight(7))
/* 1582 */         .add(LootItem.lootTableItem(Items.STRING).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 6.0F))))
/* 1583 */         .add(LootItem.lootTableItem(Items.ARROW).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F))))
/* 1584 */         .add(LootItem.lootTableItem(Items.TRIPWIRE_HOOK).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1585 */         .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1586 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))))
/*      */       
/* 1588 */       .withPool(LootPool.lootPool()
/* 1589 */         .setRolls(UniformGenerator.between(0.0F, 1.0F))
/* 1590 */         .add(LootItem.lootTableItem(Items.GOAT_HORN)).apply(SetInstrumentFunction.setInstrumentOptions(InstrumentTags.REGULAR_GOAT_HORNS)))
/*      */       
/* 1592 */       .withPool(LootPool.lootPool()
/* 1593 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1594 */         .add(EmptyLootItem.emptyItem().setWeight(3))
/* 1595 */         .add(LootItem.lootTableItem(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder desertPyramidLootTable() {
/* 1600 */     return LootTable.lootTable()
/* 1601 */       .withPool(LootPool.lootPool()
/* 1602 */         .setRolls(UniformGenerator.between(2.0F, 4.0F))
/* 1603 */         .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1604 */         .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/* 1605 */         .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F))))
/* 1606 */         .add(LootItem.lootTableItem(Items.EMERALD).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1607 */         .add(LootItem.lootTableItem(Items.BONE).setWeight(25).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 6.0F))))
/* 1608 */         .add(LootItem.lootTableItem(Items.SPIDER_EYE).setWeight(25).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 1609 */         .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(25).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
/* 1610 */         .add(LootItem.lootTableItem(Items.LEATHER).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
/* 1611 */         .add(LootItem.lootTableItem(Items.COPPER_HORSE_ARMOR).setWeight(15))
/* 1612 */         .add(LootItem.lootTableItem(Items.IRON_HORSE_ARMOR).setWeight(15))
/* 1613 */         .add(LootItem.lootTableItem(Items.GOLDEN_HORSE_ARMOR).setWeight(10))
/* 1614 */         .add(LootItem.lootTableItem(Items.DIAMOND_HORSE_ARMOR).setWeight(5))
/* 1615 */         .add(LootItem.lootTableItem(Items.BOOK).setWeight(20).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1616 */         .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(20))
/* 1617 */         .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
/* 1618 */         .add(EmptyLootItem.emptyItem().setWeight(15)))
/*      */       
/* 1620 */       .withPool(LootPool.lootPool()
/* 1621 */         .setRolls(ConstantValue.exactly(4.0F))
/* 1622 */         .add(LootItem.lootTableItem(Items.BONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
/* 1623 */         .add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
/* 1624 */         .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
/* 1625 */         .add(LootItem.lootTableItem(Items.STRING).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
/* 1626 */         .add(LootItem.lootTableItem(Blocks.SAND).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F)))))
/*      */       
/* 1628 */       .withPool(LootPool.lootPool()
/* 1629 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1630 */         .add(EmptyLootItem.emptyItem().setWeight(6))
/* 1631 */         .add(LootItem.lootTableItem(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))));
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\loot\packs\VanillaChestLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */