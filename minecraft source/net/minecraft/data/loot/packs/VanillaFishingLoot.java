/*     */ package net.minecraft.data.loot.packs;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.advancements.criterion.EntityPredicate;
/*     */ import net.minecraft.advancements.criterion.FishingHookPredicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.loot.LootTableSubProvider;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
/*     */ import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
/*     */ import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*     */ 
/*     */ public final class VanillaFishingLoot extends Record implements LootTableSubProvider {
/*     */   private final HolderLookup.Provider registries;
/*     */   
/*  35 */   public VanillaFishingLoot(HolderLookup.Provider registries) { this.registries = registries; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaFishingLoot;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #35	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  35 */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaFishingLoot; } public HolderLookup.Provider registries() { return this.registries; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaFishingLoot;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #35	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaFishingLoot; }
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaFishingLoot;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #35	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaFishingLoot;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/*  38 */     HolderLookup.RegistryLookup<Biome> biomes = this.registries.lookupOrThrow(Registries.BIOME);
/*     */     
/*  40 */     output.accept(BuiltInLootTables.FISHING, 
/*  41 */         LootTable.lootTable()
/*  42 */         .withPool(LootPool.lootPool()
/*  43 */           .setRolls(ConstantValue.exactly(1.0F))
/*  44 */           .add(NestedLootTable.lootTableReference(BuiltInLootTables.FISHING_JUNK).setWeight(10).setQuality(-2))
/*  45 */           .add(NestedLootTable.lootTableReference(BuiltInLootTables.FISHING_TREASURE).setWeight(5).setQuality(2)
/*  46 */             .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(FishingHookPredicate.inOpenWater(true)))))
/*  47 */           .add(NestedLootTable.lootTableReference(BuiltInLootTables.FISHING_FISH).setWeight(85).setQuality(-1))));
/*     */ 
/*     */ 
/*     */     
/*  51 */     output.accept(BuiltInLootTables.FISHING_FISH, fishingFishLootTable());
/*     */     
/*  53 */     output.accept(BuiltInLootTables.FISHING_JUNK, 
/*  54 */         LootTable.lootTable()
/*  55 */         .withPool(LootPool.lootPool()
/*  56 */           .add(LootItem.lootTableItem(Blocks.LILY_PAD).setWeight(17))
/*  57 */           .add(LootItem.lootTableItem(Items.LEATHER_BOOTS).setWeight(10).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.9F))))
/*  58 */           .add(LootItem.lootTableItem(Items.LEATHER).setWeight(10))
/*  59 */           .add(LootItem.lootTableItem(Items.BONE).setWeight(10))
/*  60 */           .add(LootItem.lootTableItem(Items.POTION).setWeight(10).apply(SetPotionFunction.setPotion(Potions.WATER)))
/*  61 */           .add(LootItem.lootTableItem(Items.STRING).setWeight(5))
/*  62 */           .add(LootItem.lootTableItem(Items.FISHING_ROD).setWeight(2).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.9F))))
/*  63 */           .add(LootItem.lootTableItem(Items.BOWL).setWeight(10))
/*  64 */           .add(LootItem.lootTableItem(Items.STICK).setWeight(5))
/*  65 */           .add(LootItem.lootTableItem(Items.INK_SAC).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(10.0F))))
/*  66 */           .add(LootItem.lootTableItem(Blocks.TRIPWIRE_HOOK).setWeight(10))
/*  67 */           .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10))
/*  68 */           .add(((LootPoolSingletonContainer.Builder)LootItem.lootTableItem(Blocks.BAMBOO)
/*  69 */             .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(new Holder[] { biomes
/*  70 */                       .getOrThrow(Biomes.JUNGLE), biomes
/*  71 */                       .getOrThrow(Biomes.SPARSE_JUNGLE), biomes
/*  72 */                       .getOrThrow(Biomes.BAMBOO_JUNGLE)
/*     */                     
/*  74 */                     }))))).setWeight(10))));
/*     */ 
/*     */ 
/*     */     
/*  78 */     output.accept(BuiltInLootTables.FISHING_TREASURE, 
/*  79 */         LootTable.lootTable()
/*  80 */         .withPool(LootPool.lootPool()
/*  81 */           .add(LootItem.lootTableItem(Items.NAME_TAG))
/*  82 */           .add(LootItem.lootTableItem(Items.SADDLE))
/*  83 */           .add(LootItem.lootTableItem(Items.BOW)
/*  84 */             .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.25F)))
/*  85 */             .apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(30.0F))))
/*     */           
/*  87 */           .add(LootItem.lootTableItem(Items.FISHING_ROD)
/*  88 */             .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.25F)))
/*  89 */             .apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(30.0F))))
/*     */           
/*  91 */           .add(LootItem.lootTableItem(Items.BOOK)
/*  92 */             .apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(30.0F))))
/*     */           
/*  94 */           .add(LootItem.lootTableItem(Items.NAUTILUS_SHELL))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static LootTable.Builder fishingFishLootTable() {
/* 100 */     return LootTable.lootTable()
/* 101 */       .withPool(LootPool.lootPool()
/* 102 */         .add(LootItem.lootTableItem(Items.COD).setWeight(60))
/* 103 */         .add(LootItem.lootTableItem(Items.SALMON).setWeight(25))
/* 104 */         .add(LootItem.lootTableItem(Items.TROPICAL_FISH).setWeight(2))
/* 105 */         .add(LootItem.lootTableItem(Items.PUFFERFISH).setWeight(13)));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\loot\packs\VanillaFishingLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */