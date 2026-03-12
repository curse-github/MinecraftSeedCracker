/*    */ package net.minecraft.data.loot.packs;
/*    */ 
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.loot.LootTableSubProvider;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.alchemy.Potions;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.Enchantments;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ import net.minecraft.world.level.storage.loot.LootPool;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*    */ import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
/*    */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*    */ import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*    */ 
/*    */ public final class VanillaPiglinBarterLoot extends Record implements LootTableSubProvider {
/*    */   private final HolderLookup.Provider registries;
/*    */   
/* 25 */   public VanillaPiglinBarterLoot(HolderLookup.Provider registries) { this.registries = registries; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaPiglinBarterLoot;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 25 */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaPiglinBarterLoot; } public HolderLookup.Provider registries() { return this.registries; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaPiglinBarterLoot;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaPiglinBarterLoot; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaPiglinBarterLoot;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaPiglinBarterLoot;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/* 28 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 29 */     output.accept(BuiltInLootTables.PIGLIN_BARTERING, 
/* 30 */         LootTable.lootTable()
/* 31 */         .withPool(LootPool.lootPool()
/* 32 */           .setRolls(ConstantValue.exactly(1.0F))
/*    */ 
/*    */           
/* 35 */           .add(LootItem.lootTableItem(Items.BOOK).setWeight(5).apply((new EnchantRandomlyFunction.Builder()).withEnchantment(enchantments.getOrThrow(Enchantments.SOUL_SPEED))))
/*    */ 
/*    */           
/* 38 */           .add(LootItem.lootTableItem(Items.IRON_BOOTS).setWeight(8).apply((new EnchantRandomlyFunction.Builder()).withEnchantment(enchantments.getOrThrow(Enchantments.SOUL_SPEED))))
/* 39 */           .add(LootItem.lootTableItem(Items.POTION).setWeight(8).apply(SetPotionFunction.setPotion(Potions.FIRE_RESISTANCE)))
/* 40 */           .add(LootItem.lootTableItem(Items.SPLASH_POTION).setWeight(8).apply(SetPotionFunction.setPotion(Potions.FIRE_RESISTANCE)))
/*    */ 
/*    */           
/* 43 */           .add(LootItem.lootTableItem(Items.POTION).setWeight(10).apply(SetPotionFunction.setPotion(Potions.WATER)))
/* 44 */           .add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(10.0F, 36.0F))))
/* 45 */           .add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
/* 46 */           .add(LootItem.lootTableItem(Items.DRIED_GHAST).setWeight(10).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
/*    */ 
/*    */           
/* 49 */           .add(LootItem.lootTableItem(Items.STRING).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
/* 50 */           .add(LootItem.lootTableItem(Items.QUARTZ).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 12.0F))))
/*    */ 
/*    */           
/* 53 */           .add(LootItem.lootTableItem(Items.OBSIDIAN).setWeight(40))
/* 54 */           .add(LootItem.lootTableItem(Items.CRYING_OBSIDIAN).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
/* 55 */           .add(LootItem.lootTableItem(Items.FIRE_CHARGE).setWeight(40))
/* 56 */           .add(LootItem.lootTableItem(Items.LEATHER).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
/* 57 */           .add(LootItem.lootTableItem(Items.SOUL_SAND).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
/* 58 */           .add(LootItem.lootTableItem(Items.NETHER_BRICK).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
/* 59 */           .add(LootItem.lootTableItem(Items.SPECTRAL_ARROW).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 12.0F))))
/* 60 */           .add(LootItem.lootTableItem(Items.GRAVEL).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 16.0F))))
/* 61 */           .add(LootItem.lootTableItem(Items.BLACKSTONE).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 16.0F))))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\loot\packs\VanillaPiglinBarterLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */