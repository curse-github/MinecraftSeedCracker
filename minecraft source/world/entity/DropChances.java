/*    */ package net.minecraft.world.entity;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public final class DropChances extends Record {
/*    */   private final Map<EquipmentSlot, Float> byEquipment;
/*    */   public static final float DEFAULT_EQUIPMENT_DROP_CHANCE = 0.085F;
/*    */   
/* 10 */   public DropChances(Map<EquipmentSlot, Float> byEquipment) { this.byEquipment = byEquipment; } public static final float PRESERVE_ITEM_DROP_CHANCE_THRESHOLD = 1.0F; public static final int PRESERVE_ITEM_DROP_CHANCE = 2; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/DropChances;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/entity/DropChances; } public Map<EquipmentSlot, Float> byEquipment() { return this.byEquipment; }
/*    */ 
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/DropChances;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/DropChances; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/DropChances;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/DropChances;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 17 */   public static final DropChances DEFAULT = new DropChances(Util.makeEnumMap(EquipmentSlot.class, slot -> Float.valueOf(0.085F)));
/*    */   
/* 19 */   public static final Codec<DropChances> CODEC = Codec.unboundedMap(EquipmentSlot.CODEC, ExtraCodecs.NON_NEGATIVE_FLOAT)
/* 20 */     .xmap(DropChances::toEnumMap, DropChances::filterDefaultValues)
/* 21 */     .xmap(DropChances::new, DropChances::byEquipment);
/*    */   
/*    */   private static Map<EquipmentSlot, Float> filterDefaultValues(Map<EquipmentSlot, Float> map) {
/* 24 */     Map<EquipmentSlot, Float> filteredMap = new HashMap<EquipmentSlot, Float>(map);
/* 25 */     filteredMap.values().removeIf(chance -> (chance.floatValue() == 0.085F));
/* 26 */     return filteredMap;
/*    */   }
/*    */ 
/*    */   
/* 30 */   private static Map<EquipmentSlot, Float> toEnumMap(Map<EquipmentSlot, Float> map) { return Util.makeEnumMap(EquipmentSlot.class, slot -> (Float)map.getOrDefault(slot, Float.valueOf(0.085F))); }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public DropChances withGuaranteedDrop(EquipmentSlot slot) { return withEquipmentChance(slot, 2.0F); }
/*    */ 
/*    */   
/*    */   public DropChances withEquipmentChance(EquipmentSlot slot, float chance) {
/* 38 */     if (chance < 0.0F) {
/* 39 */       throw new IllegalArgumentException("Tried to set invalid equipment chance " + chance + " for " + String.valueOf(slot));
/*    */     }
/* 41 */     if (byEquipment(slot) == chance) {
/* 42 */       return this;
/*    */     }
/* 44 */     return new DropChances(
/* 45 */         Util.makeEnumMap(EquipmentSlot.class, newSlot -> Float.valueOf((newSlot == slot) ? chance : byEquipment(newSlot))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public float byEquipment(EquipmentSlot slot) { return ((Float)this.byEquipment.getOrDefault(slot, Float.valueOf(0.085F))).floatValue(); }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public boolean isPreserved(EquipmentSlot slot) { return (byEquipment(slot) > 1.0F); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\DropChances.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */