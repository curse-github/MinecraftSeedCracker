/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   private final Optional<ItemPredicate> item;
/*    */   private final MinMaxBounds.Ints durability;
/*    */   private final MinMaxBounds.Ints delta;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/ItemDurabilityTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/ItemDurabilityTrigger$TriggerInstance; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/ItemDurabilityTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/ItemDurabilityTrigger$TriggerInstance; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/ItemDurabilityTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/ItemDurabilityTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 22 */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, MinMaxBounds.Ints durability, MinMaxBounds.Ints delta) { this.player = player; this.item = item; this.durability = durability; this.delta = delta; } public Optional<ContextAwarePredicate> player() { return this.player; } public Optional<ItemPredicate> item() { return this.item; } public MinMaxBounds.Ints durability() { return this.durability; } public MinMaxBounds.Ints delta() { return this.delta; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 29 */         .optionalFieldOf("player").forGetter(TriggerInstance::player), ItemPredicate.CODEC
/* 30 */         .optionalFieldOf("item").forGetter(TriggerInstance::item), MinMaxBounds.Ints.CODEC
/* 31 */         .optionalFieldOf("durability", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::durability), MinMaxBounds.Ints.CODEC
/* 32 */         .optionalFieldOf("delta", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::delta))
/* 33 */       .apply(i, TriggerInstance::new));
/*    */ 
/*    */   
/* 36 */   public static Criterion<TriggerInstance> changedDurability(Optional<ItemPredicate> item, MinMaxBounds.Ints durability) { return changedDurability(Optional.empty(), item, durability); }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public static Criterion<TriggerInstance> changedDurability(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, MinMaxBounds.Ints durability) { return CriteriaTriggers.ITEM_DURABILITY_CHANGED.createCriterion(new TriggerInstance(player, item, durability, MinMaxBounds.Ints.ANY)); }
/*    */ 
/*    */   
/*    */   public boolean matches(ItemStack itemStack, int newDurability) {
/* 44 */     if (this.item.isPresent() && !((ItemPredicate)this.item.get()).test(itemStack)) {
/* 45 */       return false;
/*    */     }
/* 47 */     if (!this.durability.matches(itemStack.getMaxDamage() - newDurability)) {
/* 48 */       return false;
/*    */     }
/* 50 */     if (!this.delta.matches(itemStack.getDamageValue() - newDurability)) {
/* 51 */       return false;
/*    */     }
/* 53 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\ItemDurabilityTrigger$TriggerInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */