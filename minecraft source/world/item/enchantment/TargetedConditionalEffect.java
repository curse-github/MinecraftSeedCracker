/*    */ package net.minecraft.world.item.enchantment;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.context.ContextKeySet;
/*    */ 
/*    */ public final class TargetedConditionalEffect<T> extends Record {
/*    */   private final EnchantmentTarget enchanted;
/*    */   private final EnchantmentTarget affected;
/*    */   private final T effect;
/*    */   private final Optional<LootItemCondition> requirements;
/*    */   
/* 12 */   public TargetedConditionalEffect(EnchantmentTarget enchanted, EnchantmentTarget affected, T effect, Optional<LootItemCondition> requirements) { this.enchanted = enchanted; this.affected = affected; this.effect = effect; this.requirements = requirements; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect<TT;>; } public EnchantmentTarget enchanted() { return this.enchanted; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect<TT;>; } public EnchantmentTarget affected() { return this.affected; } public T effect() { return (T)this.effect; } public Optional<LootItemCondition> requirements() { return this.requirements; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static <S> Codec<TargetedConditionalEffect<S>> codec(Codec<S> effectCodec, ContextKeySet paramsSet) { return RecordCodecBuilder.create(i -> i.group(EnchantmentTarget.CODEC
/* 20 */           .fieldOf("enchanted").forGetter(TargetedConditionalEffect::enchanted), EnchantmentTarget.CODEC
/* 21 */           .fieldOf("affected").forGetter(TargetedConditionalEffect::affected), effectCodec
/* 22 */           .fieldOf("effect").forGetter(TargetedConditionalEffect::effect), 
/* 23 */           ConditionalEffect.conditionCodec(paramsSet).optionalFieldOf("requirements").forGetter(TargetedConditionalEffect::requirements))
/* 24 */         .apply(i, TargetedConditionalEffect::new)); }
/*    */ 
/*    */   
/*    */   public static <S> Codec<TargetedConditionalEffect<S>> equipmentDropsCodec(Codec<S> effectCodec, ContextKeySet paramsSet) {
/* 28 */     return RecordCodecBuilder.create(i -> i.group(EnchantmentTarget.CODEC
/* 29 */           .validate(()).fieldOf("enchanted").forGetter(TargetedConditionalEffect::enchanted), effectCodec
/* 30 */           .fieldOf("effect").forGetter(TargetedConditionalEffect::effect), 
/* 31 */           ConditionalEffect.conditionCodec(paramsSet).optionalFieldOf("requirements").forGetter(TargetedConditionalEffect::requirements))
/* 32 */         .apply(i, ()));
/*    */   }
/*    */   
/*    */   public boolean matches(LootContext context) {
/* 36 */     if (this.requirements.isEmpty()) {
/* 37 */       return true;
/*    */     }
/* 39 */     return ((LootItemCondition)this.requirements.get()).test(context);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\TargetedConditionalEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */