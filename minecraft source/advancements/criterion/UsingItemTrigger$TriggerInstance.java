/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   private final Optional<ItemPredicate> item;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/UsingItemTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/UsingItemTrigger$TriggerInstance; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/UsingItemTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/UsingItemTrigger$TriggerInstance; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/UsingItemTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/UsingItemTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 22 */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item) { this.player = player; this.item = item; } public Optional<ContextAwarePredicate> player() { return this.player; } public Optional<ItemPredicate> item() { return this.item; }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 27 */         .optionalFieldOf("player").forGetter(TriggerInstance::player), ItemPredicate.CODEC
/* 28 */         .optionalFieldOf("item").forGetter(TriggerInstance::item))
/* 29 */       .apply(i, TriggerInstance::new));
/*    */ 
/*    */   
/* 32 */   public static Criterion<TriggerInstance> lookingAt(EntityPredicate.Builder player, ItemPredicate.Builder with) { return CriteriaTriggers.USING_ITEM.createCriterion(new TriggerInstance(Optional.of(EntityPredicate.wrap(player)), Optional.of(with.build()))); }
/*    */ 
/*    */   
/*    */   public boolean matches(ItemStack item) {
/* 36 */     if (this.item.isPresent() && !((ItemPredicate)this.item.get()).test(item)) {
/* 37 */       return false;
/*    */     }
/* 39 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\UsingItemTrigger$TriggerInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */