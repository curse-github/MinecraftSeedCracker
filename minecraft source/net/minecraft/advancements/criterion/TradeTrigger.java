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
/*    */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class TradeTrigger
/*    */   extends SimpleCriterionTrigger<TradeTrigger.TriggerInstance> {
/* 17 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*    */ 
/*    */   
/*    */   public void trigger(ServerPlayer player, AbstractVillager villager, ItemStack itemStack) {
/* 21 */     LootContext villagerContext = EntityPredicate.createContext(player, villager);
/* 22 */     trigger(player, t -> t.matches(villagerContext, itemStack));
/*    */   }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final Optional<ContextAwarePredicate> villager; private final Optional<ItemPredicate> item;
/* 25 */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> villager, Optional<ItemPredicate> item) { this.player = player; this.villager = villager; this.item = item; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/TradeTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 25 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/TradeTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/TradeTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/TradeTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/TradeTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/TradeTrigger$TriggerInstance;
/* 25 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<ContextAwarePredicate> villager() { return this.villager; } public Optional<ItemPredicate> item() { return this.item; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 30 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 31 */           .optionalFieldOf("player").forGetter(TriggerInstance::player), EntityPredicate.ADVANCEMENT_CODEC
/* 32 */           .optionalFieldOf("villager").forGetter(TriggerInstance::villager), ItemPredicate.CODEC
/* 33 */           .optionalFieldOf("item").forGetter(TriggerInstance::item))
/* 34 */         .apply(i, TriggerInstance::new));
/*    */ 
/*    */     
/* 37 */     public static Criterion<TriggerInstance> tradedWithVillager() { return CriteriaTriggers.TRADE.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty())); }
/*    */ 
/*    */ 
/*    */     
/* 41 */     public static Criterion<TriggerInstance> tradedWithVillager(EntityPredicate.Builder player) { return CriteriaTriggers.TRADE.createCriterion(new TriggerInstance(Optional.of(EntityPredicate.wrap(player)), Optional.empty(), Optional.empty())); }
/*    */ 
/*    */     
/*    */     public boolean matches(LootContext villager, ItemStack itemStack) {
/* 45 */       if (this.villager.isPresent() && !((ContextAwarePredicate)this.villager.get()).matches(villager)) {
/* 46 */         return false;
/*    */       }
/* 48 */       if (this.item.isPresent() && !((ItemPredicate)this.item.get()).test(itemStack)) {
/* 49 */         return false;
/*    */       }
/* 51 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public void validate(CriterionValidator validator) {
/* 56 */       super.validate(validator);
/* 57 */       validator.validateEntity(this.villager, "villager");
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\TradeTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */