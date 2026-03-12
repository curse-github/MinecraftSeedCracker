/*    */ package net.minecraft.data.loot.packs;
/*    */ 
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.advancements.criterion.StatePropertiesPredicate;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.data.loot.LootTableSubProvider;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.SweetBerryBushBlock;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ import net.minecraft.world.level.storage.loot.LootPool;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*    */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*    */ 
/*    */ public final class VanillaBlockInteractLoot extends Record implements LootTableSubProvider {
/*    */   private final HolderLookup.Provider registries;
/*    */   
/* 23 */   public VanillaBlockInteractLoot(HolderLookup.Provider registries) { this.registries = registries; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaBlockInteractLoot;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 23 */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaBlockInteractLoot; } public HolderLookup.Provider registries() { return this.registries; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaBlockInteractLoot;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaBlockInteractLoot; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaBlockInteractLoot;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaBlockInteractLoot;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/* 26 */     output.accept(BuiltInLootTables.HARVEST_BEEHIVE, LootTable.lootTable()
/* 27 */         .withPool(LootPool.lootPool()
/* 28 */           .setRolls(ConstantValue.exactly(1.0F))
/* 29 */           .add(LootItem.lootTableItem(Items.HONEYCOMB).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F))))));
/*    */ 
/*    */     
/* 32 */     output.accept(BuiltInLootTables.HARVEST_CAVE_VINE, 
/* 33 */         LootTable.lootTable()
/* 34 */         .withPool(LootPool.lootPool()
/* 35 */           .setRolls(ConstantValue.exactly(1.0F))
/* 36 */           .add(LootItem.lootTableItem(Items.GLOW_BERRIES))));
/*    */ 
/*    */     
/* 39 */     output.accept(BuiltInLootTables.HARVEST_SWEET_BERRY_BUSH, LootTable.lootTable()
/* 40 */         .withPool(LootPool.lootPool()
/* 41 */           .add(LootItem.lootTableItem(Items.SWEET_BERRIES)
/* 42 */             .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
/* 43 */             .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SWEET_BERRY_BUSH).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SweetBerryBushBlock.AGE, 3)))))
/*    */ 
/*    */         
/* 46 */         .withPool(LootPool.lootPool()
/* 47 */           .add(LootItem.lootTableItem(Items.SWEET_BERRIES)
/* 48 */             .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))));
/*    */ 
/*    */ 
/*    */     
/* 52 */     output.accept(BuiltInLootTables.CARVE_PUMPKIN, LootTable.lootTable()
/* 53 */         .withPool(LootPool.lootPool()
/* 54 */           .setRolls(ConstantValue.exactly(1.0F))
/* 55 */           .add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\loot\packs\VanillaBlockInteractLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */