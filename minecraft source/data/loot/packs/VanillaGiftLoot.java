/*     */ package net.minecraft.data.loot.packs;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.advancements.criterion.DataComponentMatchers;
/*     */ import net.minecraft.advancements.criterion.EntityPredicate;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentExactPredicate;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.loot.LootTableSubProvider;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.entity.animal.chicken.ChickenVariants;
/*     */ import net.minecraft.world.item.EitherHolder;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
/*     */ import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*     */ 
/*     */ public final class VanillaGiftLoot
/*     */   extends Record implements LootTableSubProvider {
/*     */   private final HolderLookup.Provider registries;
/*     */   
/*  34 */   public VanillaGiftLoot(HolderLookup.Provider registries) { this.registries = registries; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaGiftLoot;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  34 */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaGiftLoot; } public HolderLookup.Provider registries() { return this.registries; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaGiftLoot;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaGiftLoot; }
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaGiftLoot;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaGiftLoot;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/*  37 */     HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.CHICKEN_VARIANT);
/*     */     
/*  39 */     output.accept(BuiltInLootTables.CAT_MORNING_GIFT, 
/*  40 */         LootTable.lootTable()
/*  41 */         .withPool(LootPool.lootPool()
/*  42 */           .setRolls(ConstantValue.exactly(1.0F))
/*  43 */           .add(LootItem.lootTableItem(Items.RABBIT_HIDE).setWeight(10))
/*  44 */           .add(LootItem.lootTableItem(Items.RABBIT_FOOT).setWeight(10))
/*  45 */           .add(LootItem.lootTableItem(Items.CHICKEN).setWeight(10))
/*  46 */           .add(LootItem.lootTableItem(Items.FEATHER).setWeight(10))
/*  47 */           .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10))
/*  48 */           .add(LootItem.lootTableItem(Items.STRING).setWeight(10))
/*  49 */           .add(LootItem.lootTableItem(Items.PHANTOM_MEMBRANE).setWeight(2))));
/*     */ 
/*     */     
/*  52 */     output.accept(BuiltInLootTables.ARMORER_GIFT, 
/*  53 */         LootTable.lootTable()
/*  54 */         .withPool(LootPool.lootPool()
/*  55 */           .setRolls(ConstantValue.exactly(1.0F))
/*  56 */           .add(LootItem.lootTableItem(Items.CHAINMAIL_HELMET))
/*  57 */           .add(LootItem.lootTableItem(Items.CHAINMAIL_CHESTPLATE))
/*  58 */           .add(LootItem.lootTableItem(Items.CHAINMAIL_LEGGINGS))
/*  59 */           .add(LootItem.lootTableItem(Items.CHAINMAIL_BOOTS))));
/*     */ 
/*     */     
/*  62 */     output.accept(BuiltInLootTables.BUTCHER_GIFT, 
/*  63 */         LootTable.lootTable()
/*  64 */         .withPool(LootPool.lootPool()
/*  65 */           .setRolls(ConstantValue.exactly(1.0F))
/*  66 */           .add(LootItem.lootTableItem(Items.COOKED_RABBIT))
/*  67 */           .add(LootItem.lootTableItem(Items.COOKED_CHICKEN))
/*  68 */           .add(LootItem.lootTableItem(Items.COOKED_PORKCHOP))
/*  69 */           .add(LootItem.lootTableItem(Items.COOKED_BEEF))
/*  70 */           .add(LootItem.lootTableItem(Items.COOKED_MUTTON))));
/*     */ 
/*     */     
/*  73 */     output.accept(BuiltInLootTables.CARTOGRAPHER_GIFT, 
/*  74 */         LootTable.lootTable()
/*  75 */         .withPool(LootPool.lootPool()
/*  76 */           .setRolls(ConstantValue.exactly(1.0F))
/*  77 */           .add(LootItem.lootTableItem(Items.MAP))
/*  78 */           .add(LootItem.lootTableItem(Items.PAPER))));
/*     */ 
/*     */     
/*  81 */     output.accept(BuiltInLootTables.CLERIC_GIFT, 
/*  82 */         LootTable.lootTable()
/*  83 */         .withPool(LootPool.lootPool()
/*  84 */           .setRolls(ConstantValue.exactly(1.0F))
/*  85 */           .add(LootItem.lootTableItem(Items.REDSTONE))
/*  86 */           .add(LootItem.lootTableItem(Items.LAPIS_LAZULI))));
/*     */ 
/*     */     
/*  89 */     output.accept(BuiltInLootTables.FARMER_GIFT, 
/*  90 */         LootTable.lootTable()
/*  91 */         .withPool(LootPool.lootPool()
/*  92 */           .setRolls(ConstantValue.exactly(1.0F))
/*  93 */           .add(LootItem.lootTableItem(Items.BREAD))
/*  94 */           .add(LootItem.lootTableItem(Items.PUMPKIN_PIE))
/*  95 */           .add(LootItem.lootTableItem(Items.COOKIE))));
/*     */ 
/*     */ 
/*     */     
/*  99 */     output.accept(BuiltInLootTables.FISHERMAN_GIFT, 
/* 100 */         LootTable.lootTable()
/* 101 */         .withPool(LootPool.lootPool()
/* 102 */           .setRolls(ConstantValue.exactly(1.0F))
/* 103 */           .add(LootItem.lootTableItem(Items.COD))
/* 104 */           .add(LootItem.lootTableItem(Items.SALMON))));
/*     */ 
/*     */     
/* 107 */     output.accept(BuiltInLootTables.FLETCHER_GIFT, 
/* 108 */         LootTable.lootTable()
/* 109 */         .withPool(LootPool.lootPool()
/* 110 */           .setRolls(ConstantValue.exactly(1.0F))
/* 111 */           .add(LootItem.lootTableItem(Items.ARROW).setWeight(26))
/* 112 */           .add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(SetPotionFunction.setPotion(Potions.SWIFTNESS)))
/* 113 */           .add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(SetPotionFunction.setPotion(Potions.SLOWNESS)))
/* 114 */           .add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(SetPotionFunction.setPotion(Potions.STRENGTH)))
/* 115 */           .add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(SetPotionFunction.setPotion(Potions.HEALING)))
/* 116 */           .add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(SetPotionFunction.setPotion(Potions.HARMING)))
/* 117 */           .add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(SetPotionFunction.setPotion(Potions.LEAPING)))
/* 118 */           .add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(SetPotionFunction.setPotion(Potions.REGENERATION)))
/* 119 */           .add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(SetPotionFunction.setPotion(Potions.FIRE_RESISTANCE)))
/* 120 */           .add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(SetPotionFunction.setPotion(Potions.WATER_BREATHING)))
/* 121 */           .add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(SetPotionFunction.setPotion(Potions.INVISIBILITY)))
/* 122 */           .add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(SetPotionFunction.setPotion(Potions.NIGHT_VISION)))
/* 123 */           .add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(SetPotionFunction.setPotion(Potions.WEAKNESS)))
/* 124 */           .add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(SetPotionFunction.setPotion(Potions.POISON)))));
/*     */ 
/*     */     
/* 127 */     output.accept(BuiltInLootTables.LEATHERWORKER_GIFT, 
/* 128 */         LootTable.lootTable()
/* 129 */         .withPool(LootPool.lootPool()
/* 130 */           .setRolls(ConstantValue.exactly(1.0F))
/* 131 */           .add(LootItem.lootTableItem(Items.LEATHER))));
/*     */ 
/*     */     
/* 134 */     output.accept(BuiltInLootTables.LIBRARIAN_GIFT, 
/* 135 */         LootTable.lootTable()
/* 136 */         .withPool(LootPool.lootPool()
/* 137 */           .setRolls(ConstantValue.exactly(1.0F))
/* 138 */           .add(LootItem.lootTableItem(Items.BOOK))));
/*     */ 
/*     */     
/* 141 */     output.accept(BuiltInLootTables.MASON_GIFT, 
/* 142 */         LootTable.lootTable()
/* 143 */         .withPool(LootPool.lootPool()
/* 144 */           .setRolls(ConstantValue.exactly(1.0F))
/* 145 */           .add(LootItem.lootTableItem(Items.CLAY))));
/*     */ 
/*     */     
/* 148 */     output.accept(BuiltInLootTables.SHEPHERD_GIFT, 
/* 149 */         LootTable.lootTable()
/* 150 */         .withPool(LootPool.lootPool()
/* 151 */           .setRolls(ConstantValue.exactly(1.0F))
/* 152 */           .add(LootItem.lootTableItem(Items.WHITE_WOOL))
/* 153 */           .add(LootItem.lootTableItem(Items.ORANGE_WOOL))
/* 154 */           .add(LootItem.lootTableItem(Items.MAGENTA_WOOL))
/* 155 */           .add(LootItem.lootTableItem(Items.LIGHT_BLUE_WOOL))
/* 156 */           .add(LootItem.lootTableItem(Items.YELLOW_WOOL))
/* 157 */           .add(LootItem.lootTableItem(Items.LIME_WOOL))
/* 158 */           .add(LootItem.lootTableItem(Items.PINK_WOOL))
/* 159 */           .add(LootItem.lootTableItem(Items.GRAY_WOOL))
/* 160 */           .add(LootItem.lootTableItem(Items.LIGHT_GRAY_WOOL))
/* 161 */           .add(LootItem.lootTableItem(Items.CYAN_WOOL))
/* 162 */           .add(LootItem.lootTableItem(Items.PURPLE_WOOL))
/* 163 */           .add(LootItem.lootTableItem(Items.BLUE_WOOL))
/* 164 */           .add(LootItem.lootTableItem(Items.BROWN_WOOL))
/* 165 */           .add(LootItem.lootTableItem(Items.GREEN_WOOL))
/* 166 */           .add(LootItem.lootTableItem(Items.RED_WOOL))
/* 167 */           .add(LootItem.lootTableItem(Items.BLACK_WOOL))));
/*     */ 
/*     */     
/* 170 */     output.accept(BuiltInLootTables.TOOLSMITH_GIFT, 
/* 171 */         LootTable.lootTable()
/* 172 */         .withPool(LootPool.lootPool()
/* 173 */           .setRolls(ConstantValue.exactly(1.0F))
/* 174 */           .add(LootItem.lootTableItem(Items.STONE_PICKAXE))
/* 175 */           .add(LootItem.lootTableItem(Items.STONE_AXE))
/* 176 */           .add(LootItem.lootTableItem(Items.STONE_HOE))
/* 177 */           .add(LootItem.lootTableItem(Items.STONE_SHOVEL))));
/*     */ 
/*     */     
/* 180 */     output.accept(BuiltInLootTables.WEAPONSMITH_GIFT, 
/* 181 */         LootTable.lootTable()
/* 182 */         .withPool(LootPool.lootPool()
/* 183 */           .setRolls(ConstantValue.exactly(1.0F))
/* 184 */           .add(LootItem.lootTableItem(Items.STONE_AXE))
/* 185 */           .add(LootItem.lootTableItem(Items.GOLDEN_AXE))
/* 186 */           .add(LootItem.lootTableItem(Items.IRON_AXE))));
/*     */ 
/*     */     
/* 189 */     output.accept(BuiltInLootTables.UNEMPLOYED_GIFT, 
/* 190 */         LootTable.lootTable()
/* 191 */         .withPool(LootPool.lootPool()
/* 192 */           .setRolls(ConstantValue.exactly(1.0F))
/* 193 */           .add(LootItem.lootTableItem(Items.WHEAT_SEEDS))));
/*     */ 
/*     */     
/* 196 */     output.accept(BuiltInLootTables.BABY_VILLAGER_GIFT, 
/* 197 */         LootTable.lootTable()
/* 198 */         .withPool(LootPool.lootPool()
/* 199 */           .setRolls(ConstantValue.exactly(1.0F))
/* 200 */           .add(LootItem.lootTableItem(Items.POPPY))));
/*     */ 
/*     */     
/* 203 */     output.accept(BuiltInLootTables.SNIFFER_DIGGING, 
/* 204 */         LootTable.lootTable()
/* 205 */         .withPool(LootPool.lootPool()
/* 206 */           .setRolls(ConstantValue.exactly(1.0F))
/* 207 */           .add(LootItem.lootTableItem(Items.TORCHFLOWER_SEEDS))
/* 208 */           .add(LootItem.lootTableItem(Items.PITCHER_POD))));
/*     */ 
/*     */     
/* 211 */     output.accept(BuiltInLootTables.PANDA_SNEEZE, 
/* 212 */         LootTable.lootTable()
/* 213 */         .withPool(LootPool.lootPool()
/* 214 */           .setRolls(ConstantValue.exactly(1.0F))
/* 215 */           .add(LootItem.lootTableItem(Items.SLIME_BALL).setWeight(1))
/* 216 */           .add(EmptyLootItem.emptyItem().setWeight(699))));
/*     */ 
/*     */     
/* 219 */     output.accept(BuiltInLootTables.CHICKEN_LAY, 
/* 220 */         LootTable.lootTable()
/* 221 */         .withPool(LootPool.lootPool()
/* 222 */           .setRolls(ConstantValue.exactly(1.0F))
/* 223 */           .add(AlternativesEntry.alternatives(new LootPoolEntryContainer.Builder[] {
/* 224 */                 LootItem.lootTableItem(Items.EGG)
/* 225 */                 .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.CHICKEN_VARIANT, new EitherHolder(registryLookup.getOrThrow(ChickenVariants.TEMPERATE)))).build()))), 
/* 226 */                 LootItem.lootTableItem(Items.BROWN_EGG)
/* 227 */                 .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.CHICKEN_VARIANT, new EitherHolder(registryLookup.getOrThrow(ChickenVariants.WARM)))).build()))), 
/* 228 */                 LootItem.lootTableItem(Items.BLUE_EGG)
/* 229 */                 .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.CHICKEN_VARIANT, new EitherHolder(registryLookup.getOrThrow(ChickenVariants.COLD)))).build())))
/*     */               }))));
/* 231 */     output.accept(BuiltInLootTables.ARMADILLO_SHED, 
/* 232 */         LootTable.lootTable()
/* 233 */         .withPool(LootPool.lootPool()
/* 234 */           .setRolls(ConstantValue.exactly(1.0F))
/* 235 */           .add(LootItem.lootTableItem(Items.ARMADILLO_SCUTE))));
/*     */ 
/*     */     
/* 238 */     output.accept(BuiltInLootTables.TURTLE_GROW, 
/* 239 */         LootTable.lootTable()
/* 240 */         .withPool(LootPool.lootPool()
/* 241 */           .setRolls(ConstantValue.exactly(1.0F))
/* 242 */           .add(LootItem.lootTableItem(Items.TURTLE_SCUTE))));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\loot\packs\VanillaGiftLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */