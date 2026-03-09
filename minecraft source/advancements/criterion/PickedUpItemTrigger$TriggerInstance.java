/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.server.level.ServerPlayer;
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
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/PickedUpItemTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/PickedUpItemTrigger$TriggerInstance; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/PickedUpItemTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/PickedUpItemTrigger$TriggerInstance; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/PickedUpItemTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/PickedUpItemTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 26 */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, Optional<ContextAwarePredicate> entity) { this.player = player; this.item = item; this.entity = entity; } public Optional<ContextAwarePredicate> player() { return this.player; } public Optional<ItemPredicate> item() { return this.item; } public Optional<ContextAwarePredicate> entity() { return this.entity; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 32 */         .optionalFieldOf("player").forGetter(TriggerInstance::player), ItemPredicate.CODEC
/* 33 */         .optionalFieldOf("item").forGetter(TriggerInstance::item), EntityPredicate.ADVANCEMENT_CODEC
/* 34 */         .optionalFieldOf("entity").forGetter(TriggerInstance::entity))
/* 35 */       .apply(i, TriggerInstance::new));
/*    */ 
/*    */   
/* 38 */   public static Criterion<TriggerInstance> thrownItemPickedUpByEntity(ContextAwarePredicate player, Optional<ItemPredicate> item, Optional<ContextAwarePredicate> entity) { return CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_ENTITY.createCriterion(new TriggerInstance(Optional.of(player), item, entity)); }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public static Criterion<TriggerInstance> thrownItemPickedUpByPlayer(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, Optional<ContextAwarePredicate> entity) { return CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_PLAYER.createCriterion(new TriggerInstance(player, item, entity)); }
/*    */ 
/*    */   
/*    */   public boolean matches(ServerPlayer player, ItemStack itemStack, LootContext pickedUpBy) {
/* 46 */     if (this.item.isPresent() && !((ItemPredicate)this.item.get()).test(itemStack)) {
/* 47 */       return false;
/*    */     }
/*    */     
/* 50 */     if (this.entity.isPresent() && !((ContextAwarePredicate)this.entity.get()).matches(pickedUpBy)) {
/* 51 */       return false;
/*    */     }
/*    */     
/* 54 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(CriterionValidator validator) {
/* 59 */     super.validate(validator);
/* 60 */     validator.validateEntity(this.entity, "entity");
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\PickedUpItemTrigger$TriggerInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */