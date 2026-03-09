/*    */ package net.minecraft.world.item.enchantment;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.util.context.ContextKeySet;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public final class ConditionalEffect<T> extends Record {
/*    */   private final T effect;
/*    */   private final Optional<LootItemCondition> requirements;
/*    */   
/* 14 */   public ConditionalEffect(T effect, Optional<LootItemCondition> requirements) { this.effect = effect; this.requirements = requirements; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/ConditionalEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect<TT;>; } public T effect() { return (T)this.effect; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/ConditionalEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/ConditionalEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 14 */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect<TT;>; } public Optional<LootItemCondition> requirements() { return this.requirements; }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Codec<LootItemCondition> conditionCodec(ContextKeySet paramsSet) {
/* 19 */     return LootItemCondition.DIRECT_CODEC.validate(condition -> {
/* 20 */           ProblemReporter.Collector problemCollector = new ProblemReporter.Collector();
/* 21 */           ValidationContext validationContext = new ValidationContext(problemCollector, paramsSet);
/* 22 */           condition.validate(validationContext);
/* 23 */           if (!problemCollector.isEmpty()) {
/* 24 */             return DataResult.error(());
/*    */           }
/* 26 */           return DataResult.success(condition);
/*    */         });
/*    */   }
/*    */ 
/*    */   
/* 31 */   public static <T> Codec<ConditionalEffect<T>> codec(Codec<T> effectCodec, ContextKeySet paramsSet) { return RecordCodecBuilder.create(i -> i.group(effectCodec
/* 32 */           .fieldOf("effect").forGetter(ConditionalEffect::effect), 
/* 33 */           conditionCodec(paramsSet).optionalFieldOf("requirements").forGetter(ConditionalEffect::requirements))
/* 34 */         .apply(i, ConditionalEffect::new)); }
/*    */ 
/*    */   
/*    */   public boolean matches(LootContext context) {
/* 38 */     if (this.requirements.isEmpty()) {
/* 39 */       return true;
/*    */     }
/* 41 */     return ((LootItemCondition)this.requirements.get()).test(context);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\ConditionalEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */