/*   */ package net.minecraft.world.level.storage.loot.predicates;
/*   */ import com.mojang.serialization.MapCodec;
/*   */ 
/*   */ public final class LootItemConditionType extends Record {
/* 5 */   public LootItemConditionType(MapCodec<? extends LootItemCondition> codec) { this.codec = codec; } private final MapCodec<? extends LootItemCondition> codec; public MapCodec<? extends LootItemCondition> codec() { return this.codec; }
/*   */   
/*   */   public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemConditionType;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemConditionType; }
/*   */   
/*   */   public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemConditionType;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemConditionType; }
/*   */   
/*   */   public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemConditionType;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemConditionType;
/*   */     //   0	8	1	o	Ljava/lang/Object; }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemConditionType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */