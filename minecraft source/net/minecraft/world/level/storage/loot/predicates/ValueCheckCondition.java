/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public final class ValueCheckCondition extends Record implements LootItemCondition {
/*    */   private final NumberProvider provider;
/*    */   private final IntRange range;
/*    */   
/* 14 */   public ValueCheckCondition(NumberProvider provider, IntRange range) { this.provider = provider; this.range = range; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition; } public NumberProvider provider() { return this.provider; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public IntRange range() { return this.range; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static final MapCodec<ValueCheckCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(NumberProviders.CODEC
/* 19 */         .fieldOf("value").forGetter(ValueCheckCondition::provider), IntRange.CODEC
/* 20 */         .fieldOf("range").forGetter(ValueCheckCondition::range))
/* 21 */       .apply(i, ValueCheckCondition::new));
/*    */ 
/*    */ 
/*    */   
/* 25 */   public LootItemConditionType getType() { return LootItemConditions.VALUE_CHECK; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public Set<ContextKey<?>> getReferencedContextParams() { return Sets.union(this.provider.getReferencedContextParams(), this.range.getReferencedContextParams()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public boolean test(LootContext context) { return this.range.test(context, this.provider.getInt(context)); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public static LootItemCondition.Builder hasValue(NumberProvider target, IntRange range) { return () -> new ValueCheckCondition(target, range); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\ValueCheckCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */