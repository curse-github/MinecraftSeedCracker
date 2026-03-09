/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public final class InvertedLootItemCondition extends Record implements LootItemCondition {
/*    */   private final LootItemCondition term;
/*    */   
/* 11 */   public InvertedLootItemCondition(LootItemCondition term) { this.term = term; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition; } public LootItemCondition term() { return this.term; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 12 */   public static final MapCodec<InvertedLootItemCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LootItemCondition.DIRECT_CODEC
/* 13 */         .fieldOf("term").forGetter(InvertedLootItemCondition::term))
/* 14 */       .apply(i, InvertedLootItemCondition::new));
/*    */ 
/*    */ 
/*    */   
/* 18 */   public LootItemConditionType getType() { return LootItemConditions.INVERTED; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public boolean test(LootContext context) { return !this.term.test(context); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public Set<ContextKey<?>> getReferencedContextParams() { return this.term.getReferencedContextParams(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext output) {
/* 33 */     super.validate(output);
/* 34 */     this.term.validate(output);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder invert(LootItemCondition.Builder term) {
/* 38 */     InvertedLootItemCondition result = new InvertedLootItemCondition(term.build());
/* 39 */     return () -> result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\InvertedLootItemCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */