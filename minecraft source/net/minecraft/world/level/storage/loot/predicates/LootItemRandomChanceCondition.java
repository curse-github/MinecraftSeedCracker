/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public final class LootItemRandomChanceCondition extends Record implements LootItemCondition {
/* 10 */   public LootItemRandomChanceCondition(NumberProvider chance) { this.chance = chance; } private final NumberProvider chance; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition; } public NumberProvider chance() { return this.chance; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 13 */   public static final MapCodec<LootItemRandomChanceCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(NumberProviders.CODEC
/* 14 */         .fieldOf("chance").forGetter(LootItemRandomChanceCondition::chance))
/* 15 */       .apply(i, LootItemRandomChanceCondition::new));
/*    */ 
/*    */ 
/*    */   
/* 19 */   public LootItemConditionType getType() { return LootItemConditions.RANDOM_CHANCE; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 24 */     float probability = this.chance.getFloat(context);
/* 25 */     return (context.getRandom().nextFloat() < probability);
/*    */   }
/*    */ 
/*    */   
/* 29 */   public static LootItemCondition.Builder randomChance(float probability) { return () -> new LootItemRandomChanceCondition(ConstantValue.exactly(probability)); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public static LootItemCondition.Builder randomChance(NumberProvider probability) { return () -> new LootItemRandomChanceCondition(probability); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemRandomChanceCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */