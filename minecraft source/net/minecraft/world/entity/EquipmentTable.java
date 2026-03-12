/*    */ package net.minecraft.world.entity;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ 
/*    */ public final class EquipmentTable extends Record {
/*    */   private final ResourceKey<LootTable> lootTable;
/*    */   private final Map<EquipmentSlot, Float> slotDropChances;
/*    */   
/* 14 */   public EquipmentTable(ResourceKey<LootTable> lootTable, Map<EquipmentSlot, Float> slotDropChances) { this.lootTable = lootTable; this.slotDropChances = slotDropChances; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/EquipmentTable;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/entity/EquipmentTable; } public ResourceKey<LootTable> lootTable() { return this.lootTable; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/EquipmentTable;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/EquipmentTable; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/EquipmentTable;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/EquipmentTable;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public Map<EquipmentSlot, Float> slotDropChances() { return this.slotDropChances; }
/* 15 */   public static final Codec<Map<EquipmentSlot, Float>> DROP_CHANCES_CODEC = Codec.either(Codec.FLOAT, Codec.unboundedMap(EquipmentSlot.CODEC, Codec.FLOAT)).xmap(either -> 
/* 16 */       (Map)either.map(EquipmentTable::createForAllSlots, Function.identity()), provider -> {
/*    */         
/* 18 */         boolean dropChancesTheSame = (provider.values().stream().distinct().count() == 1L);
/* 19 */         boolean allSlotsArePresent = provider.keySet().containsAll(EquipmentSlot.VALUES);
/*    */         
/* 21 */         if (dropChancesTheSame && allSlotsArePresent) {
/* 22 */           return Either.left((Float)provider.values().stream().findFirst().orElse(Float.valueOf(0.0F)));
/*    */         }
/*    */         
/* 25 */         return Either.right(provider);
/*    */       });
/*    */ 
/*    */ 
/*    */   
/* 30 */   public EquipmentTable(ResourceKey<LootTable> lootTable, float dropChance) { this(lootTable, createForAllSlots(dropChance)); }
/*    */ 
/*    */   
/* 33 */   public static final Codec<EquipmentTable> CODEC = RecordCodecBuilder.create(i -> i.group(LootTable.KEY_CODEC
/* 34 */         .fieldOf("loot_table").forGetter(EquipmentTable::lootTable), DROP_CHANCES_CODEC
/* 35 */         .optionalFieldOf("slot_drop_chances", Map.of()).forGetter(EquipmentTable::slotDropChances))
/* 36 */       .apply(i, EquipmentTable::new));
/*    */ 
/*    */   
/* 39 */   private static Map<EquipmentSlot, Float> createForAllSlots(float dropChance) { return createForAllSlots(List.of(EquipmentSlot.values()), dropChance); }
/*    */ 
/*    */   
/*    */   private static Map<EquipmentSlot, Float> createForAllSlots(List<EquipmentSlot> slots, float dropChance) {
/* 43 */     Map<EquipmentSlot, Float> values = Maps.newHashMap();
/* 44 */     for (EquipmentSlot slot : slots) {
/* 45 */       values.put(slot, Float.valueOf(dropChance));
/*    */     }
/* 47 */     return values;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\EquipmentTable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */