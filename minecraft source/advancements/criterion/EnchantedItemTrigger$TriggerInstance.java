/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   private final Optional<ItemPredicate> item;
/*    */   private final MinMaxBounds.Ints levels;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/EnchantedItemTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EnchantedItemTrigger$TriggerInstance; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/EnchantedItemTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EnchantedItemTrigger$TriggerInstance; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/EnchantedItemTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/EnchantedItemTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 22 */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, MinMaxBounds.Ints levels) { this.player = player; this.item = item; this.levels = levels; } public Optional<ContextAwarePredicate> player() { return this.player; } public Optional<ItemPredicate> item() { return this.item; } public MinMaxBounds.Ints levels() { return this.levels; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 28 */         .optionalFieldOf("player").forGetter(TriggerInstance::player), ItemPredicate.CODEC
/* 29 */         .optionalFieldOf("item").forGetter(TriggerInstance::item), MinMaxBounds.Ints.CODEC
/* 30 */         .optionalFieldOf("levels", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::levels))
/* 31 */       .apply(i, TriggerInstance::new));
/*    */ 
/*    */   
/* 34 */   public static Criterion<TriggerInstance> enchantedItem() { return CriteriaTriggers.ENCHANTED_ITEM.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), MinMaxBounds.Ints.ANY)); }
/*    */ 
/*    */   
/*    */   public boolean matches(ItemStack itemStack, int levels) {
/* 38 */     if (this.item.isPresent() && !((ItemPredicate)this.item.get()).test(itemStack)) {
/* 39 */       return false;
/*    */     }
/* 41 */     if (!this.levels.matches(levels)) {
/* 42 */       return false;
/*    */     }
/* 44 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\EnchantedItemTrigger$TriggerInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */