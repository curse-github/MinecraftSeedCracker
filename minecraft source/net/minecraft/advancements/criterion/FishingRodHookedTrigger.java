/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.entity.projectile.FishingHook;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public class FishingRodHookedTrigger
/*    */   extends SimpleCriterionTrigger<FishingRodHookedTrigger.TriggerInstance> {
/* 21 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*    */ 
/*    */   
/*    */   public void trigger(ServerPlayer player, ItemStack rod, FishingHook hook, Collection<ItemStack> items) {
/* 25 */     LootContext hookedInContext = EntityPredicate.createContext(player, (hook.getHookedIn() != null) ? hook.getHookedIn() : hook);
/* 26 */     trigger(player, t -> t.matches(rod, hookedInContext, items));
/*    */   }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final Optional<ItemPredicate> rod; private final Optional<ContextAwarePredicate> entity; private final Optional<ItemPredicate> item;
/* 29 */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> rod, Optional<ContextAwarePredicate> entity, Optional<ItemPredicate> item) { this.player = player; this.rod = rod; this.entity = entity; this.item = item; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/FishingRodHookedTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 29 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/FishingRodHookedTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/FishingRodHookedTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/FishingRodHookedTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/FishingRodHookedTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/FishingRodHookedTrigger$TriggerInstance;
/* 29 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<ItemPredicate> rod() { return this.rod; } public Optional<ContextAwarePredicate> entity() { return this.entity; } public Optional<ItemPredicate> item() { return this.item; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 35 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 36 */           .optionalFieldOf("player").forGetter(TriggerInstance::player), ItemPredicate.CODEC
/* 37 */           .optionalFieldOf("rod").forGetter(TriggerInstance::rod), EntityPredicate.ADVANCEMENT_CODEC
/* 38 */           .optionalFieldOf("entity").forGetter(TriggerInstance::entity), ItemPredicate.CODEC
/* 39 */           .optionalFieldOf("item").forGetter(TriggerInstance::item))
/* 40 */         .apply(i, TriggerInstance::new));
/*    */ 
/*    */     
/* 43 */     public static Criterion<TriggerInstance> fishedItem(Optional<ItemPredicate> rod, Optional<EntityPredicate> entity, Optional<ItemPredicate> item) { return CriteriaTriggers.FISHING_ROD_HOOKED.createCriterion(new TriggerInstance(Optional.empty(), rod, EntityPredicate.wrap(entity), item)); }
/*    */ 
/*    */     
/*    */     public boolean matches(ItemStack rod, LootContext hookedIn, Collection<ItemStack> items) {
/* 47 */       if (this.rod.isPresent() && !((ItemPredicate)this.rod.get()).test(rod)) {
/* 48 */         return false;
/*    */       }
/* 50 */       if (this.entity.isPresent() && !((ContextAwarePredicate)this.entity.get()).matches(hookedIn)) {
/* 51 */         return false;
/*    */       }
/* 53 */       if (this.item.isPresent()) {
/* 54 */         boolean matched = false;
/*    */         
/* 56 */         Entity hookedInEntity = (Entity)hookedIn.getOptionalParameter(LootContextParams.THIS_ENTITY);
/* 57 */         if (hookedInEntity instanceof ItemEntity) { ItemEntity item = (ItemEntity)hookedInEntity;
/* 58 */           if (((ItemPredicate)this.item.get()).test(item.getItem())) {
/* 59 */             matched = true;
/*    */           } }
/*    */         
/* 62 */         for (ItemStack item : items) {
/* 63 */           if (((ItemPredicate)this.item.get()).test(item)) {
/* 64 */             matched = true;
/*    */             break;
/*    */           } 
/*    */         } 
/* 68 */         if (!matched) {
/* 69 */           return false;
/*    */         }
/*    */       } 
/* 72 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public void validate(CriterionValidator validator) {
/* 77 */       super.validate(validator);
/* 78 */       validator.validateEntity(this.entity, "entity");
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\FishingRodHookedTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */