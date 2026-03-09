/*    */ package net.minecraft.data.loot.packs;
/*    */ 
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.data.loot.LootTableSubProvider;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ import net.minecraft.world.level.storage.loot.LootPool;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*    */ 
/*    */ public final class VanillaEntityInteractLoot extends Record implements LootTableSubProvider {
/*    */   private final HolderLookup.Provider registries;
/*    */   
/* 17 */   public VanillaEntityInteractLoot(HolderLookup.Provider registries) { this.registries = registries; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaEntityInteractLoot;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 17 */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaEntityInteractLoot; } public HolderLookup.Provider registries() { return this.registries; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaEntityInteractLoot;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaEntityInteractLoot; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaEntityInteractLoot;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaEntityInteractLoot;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/* 20 */     output.accept(BuiltInLootTables.ARMADILLO_BRUSH, 
/* 21 */         LootTable.lootTable()
/* 22 */         .withPool(LootPool.lootPool()
/* 23 */           .setRolls(ConstantValue.exactly(1.0F))
/* 24 */           .add(LootItem.lootTableItem(Items.ARMADILLO_SCUTE))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\loot\packs\VanillaEntityInteractLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */