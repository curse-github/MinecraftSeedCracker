/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class TriggerInstance
/*    */   extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   private final Optional<ItemPredicate> item;
/*    */   private final Optional<ContextAwarePredicate> entity;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/PlayerInteractTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerInteractTrigger$TriggerInstance; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/PlayerInteractTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerInteractTrigger$TriggerInstance; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/PlayerInteractTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/PlayerInteractTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 25 */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, Optional<ContextAwarePredicate> entity) { this.player = player; this.item = item; this.entity = entity; } public Optional<ContextAwarePredicate> player() { return this.player; } public Optional<ItemPredicate> item() { return this.item; } public Optional<ContextAwarePredicate> entity() { return this.entity; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 31 */         .optionalFieldOf("player").forGetter(TriggerInstance::player), ItemPredicate.CODEC
/* 32 */         .optionalFieldOf("item").forGetter(TriggerInstance::item), EntityPredicate.ADVANCEMENT_CODEC
/* 33 */         .optionalFieldOf("entity").forGetter(TriggerInstance::entity))
/* 34 */       .apply(i, TriggerInstance::new));
/*    */ 
/*    */   
/* 37 */   public static Criterion<TriggerInstance> itemUsedOnEntity(Optional<ContextAwarePredicate> player, ItemPredicate.Builder item, Optional<ContextAwarePredicate> entity) { return CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.createCriterion(new TriggerInstance(player, Optional.of(item.build()), entity)); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public static Criterion<TriggerInstance> equipmentSheared(Optional<ContextAwarePredicate> player, ItemPredicate.Builder item, Optional<ContextAwarePredicate> entity) { return CriteriaTriggers.PLAYER_SHEARED_EQUIPMENT.createCriterion(new TriggerInstance(player, Optional.of(item.build()), entity)); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public static Criterion<TriggerInstance> equipmentSheared(ItemPredicate.Builder item, Optional<ContextAwarePredicate> entity) { return CriteriaTriggers.PLAYER_SHEARED_EQUIPMENT.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(item.build()), entity)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public static Criterion<TriggerInstance> itemUsedOnEntity(ItemPredicate.Builder item, Optional<ContextAwarePredicate> entity) { return itemUsedOnEntity(Optional.empty(), item, entity); }
/*    */ 
/*    */   
/*    */   public boolean matches(ItemStack itemStack, LootContext interactedWith) {
/* 54 */     if (this.item.isPresent() && !((ItemPredicate)this.item.get()).test(itemStack)) {
/* 55 */       return false;
/*    */     }
/*    */     
/* 58 */     return (this.entity.isEmpty() || ((ContextAwarePredicate)this.entity.get()).matches(interactedWith));
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(CriterionValidator validator) {
/* 63 */     super.validate(validator);
/* 64 */     validator.validateEntity(this.entity, "entity");
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\PlayerInteractTrigger$TriggerInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */