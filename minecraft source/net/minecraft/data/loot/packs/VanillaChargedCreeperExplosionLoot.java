/*    */ package net.minecraft.data.loot.packs;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.advancements.criterion.EntityPredicate;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.loot.LootTableSubProvider;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ import net.minecraft.world.level.storage.loot.LootPool;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer.Builder;
/*    */ import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*    */ 
/*    */ public final class VanillaChargedCreeperExplosionLoot extends Record implements LootTableSubProvider {
/*    */   private final HolderLookup.Provider registries;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot; }
/*    */   
/* 31 */   public VanillaChargedCreeperExplosionLoot(HolderLookup.Provider registries) { this.registries = registries; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot;
/* 31 */     //   0	8	1	o	Ljava/lang/Object; } public HolderLookup.Provider registries() { return this.registries; }
/* 32 */   private static final List<Entry> ENTRIES = List.of(new Entry(BuiltInLootTables.CHARGED_CREEPER_PIGLIN, EntityType.PIGLIN, Items.PIGLIN_HEAD), new Entry(BuiltInLootTables.CHARGED_CREEPER_CREEPER, EntityType.CREEPER, Items.CREEPER_HEAD), new Entry(BuiltInLootTables.CHARGED_CREEPER_SKELETON, EntityType.SKELETON, Items.SKELETON_SKULL), new Entry(BuiltInLootTables.CHARGED_CREEPER_WITHER_SKELETON, EntityType.WITHER_SKELETON, Items.WITHER_SKELETON_SKULL), new Entry(BuiltInLootTables.CHARGED_CREEPER_ZOMBIE, EntityType.ZOMBIE, Items.ZOMBIE_HEAD));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/* 42 */     HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.ENTITY_TYPE);
/*    */     
/* 44 */     List<LootPoolEntryContainer.Builder<?>> alternatives = new ArrayList<LootPoolEntryContainer.Builder<?>>(ENTRIES.size());
/* 45 */     for (Entry entry : ENTRIES) {
/* 46 */       output.accept(entry.lootTable, LootTable.lootTable()
/* 47 */           .withPool(LootPool.lootPool()
/* 48 */             .setRolls(ConstantValue.exactly(1.0F))
/* 49 */             .add(LootItem.lootTableItem(entry.item))));
/*    */ 
/*    */       
/* 52 */       LootItemCondition.Builder predicate = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(registryLookup, entry.entityType)));
/* 53 */       alternatives.add(NestedLootTable.lootTableReference(entry.lootTable).when(predicate));
/*    */     } 
/*    */     
/* 56 */     output.accept(BuiltInLootTables.CHARGED_CREEPER, LootTable.lootTable()
/* 57 */         .withPool(LootPool.lootPool()
/* 58 */           .setRolls(ConstantValue.exactly(1.0F))
/* 59 */           .add(AlternativesEntry.alternatives((Builder[])alternatives.toArray(x$0 -> new LootPoolEntryContainer.Builder[x$0])))));
/*    */   }
/*    */   private static final class Entry extends Record { private final ResourceKey<LootTable> lootTable; private final EntityType<?> entityType;
/*    */     private final Item item;
/*    */     
/* 64 */     private Entry(ResourceKey<LootTable> lootTable, EntityType<?> entityType, Item item) { this.lootTable = lootTable; this.entityType = entityType; this.item = item; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #64	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #64	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #64	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;
/* 64 */       //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<LootTable> lootTable() { return this.lootTable; } public EntityType<?> entityType() { return this.entityType; } public Item item() { return this.item; } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\loot\packs\VanillaChargedCreeperExplosionLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */