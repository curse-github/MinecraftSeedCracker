/*    */ package net.minecraft.data.loot.packs;
/*    */ 
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class Entry
/*    */   extends Record
/*    */ {
/*    */   private final ResourceKey<LootTable> lootTable;
/*    */   private final EntityType<?> entityType;
/*    */   private final Item item;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 64 */   private Entry(ResourceKey<LootTable> lootTable, EntityType<?> entityType, Item item) { this.lootTable = lootTable; this.entityType = entityType; this.item = item; } public ResourceKey<LootTable> lootTable() { return this.lootTable; } public EntityType<?> entityType() { return this.entityType; } public Item item() { return this.item; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\loot\packs\VanillaChargedCreeperExplosionLoot$Entry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */