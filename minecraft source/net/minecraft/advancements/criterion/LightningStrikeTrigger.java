/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LightningBolt;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class LightningStrikeTrigger extends SimpleCriterionTrigger<LightningStrikeTrigger.TriggerInstance> {
/* 19 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*    */ 
/*    */   
/*    */   public void trigger(ServerPlayer player, LightningBolt lightning, List<Entity> entitiesAround) {
/* 23 */     List<LootContext> entitiesAroundContexts = (List)entitiesAround.stream().map(v -> EntityPredicate.createContext(player, v)).collect(Collectors.toList());
/* 24 */     LootContext lightningContext = EntityPredicate.createContext(player, lightning);
/* 25 */     trigger(player, t -> t.matches(lightningContext, entitiesAroundContexts));
/*    */   }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final Optional<ContextAwarePredicate> lightning; private final Optional<ContextAwarePredicate> bystander;
/* 28 */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> lightning, Optional<ContextAwarePredicate> bystander) { this.player = player; this.lightning = lightning; this.bystander = bystander; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/LightningStrikeTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 28 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/LightningStrikeTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/LightningStrikeTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/LightningStrikeTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/LightningStrikeTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/LightningStrikeTrigger$TriggerInstance;
/* 28 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<ContextAwarePredicate> lightning() { return this.lightning; } public Optional<ContextAwarePredicate> bystander() { return this.bystander; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 33 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 34 */           .optionalFieldOf("player").forGetter(TriggerInstance::player), EntityPredicate.ADVANCEMENT_CODEC
/* 35 */           .optionalFieldOf("lightning").forGetter(TriggerInstance::lightning), EntityPredicate.ADVANCEMENT_CODEC
/* 36 */           .optionalFieldOf("bystander").forGetter(TriggerInstance::bystander))
/* 37 */         .apply(i, TriggerInstance::new));
/*    */ 
/*    */     
/* 40 */     public static Criterion<TriggerInstance> lightningStrike(Optional<EntityPredicate> lightning, Optional<EntityPredicate> bystander) { return CriteriaTriggers.LIGHTNING_STRIKE.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(lightning), EntityPredicate.wrap(bystander))); }
/*    */ 
/*    */     
/*    */     public boolean matches(LootContext bolt, List<LootContext> entitiesAround) {
/* 44 */       if (this.lightning.isPresent() && !((ContextAwarePredicate)this.lightning.get()).matches(bolt)) {
/* 45 */         return false;
/*    */       }
/*    */       
/* 48 */       Objects.requireNonNull((ContextAwarePredicate)this.bystander.get()); if (this.bystander.isPresent() && entitiesAround.stream().noneMatch((ContextAwarePredicate)this.bystander.get()::matches)) {
/* 49 */         return false;
/*    */       }
/*    */       
/* 52 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public void validate(CriterionValidator validator) {
/* 57 */       super.validate(validator);
/* 58 */       validator.validateEntity(this.lightning, "lightning");
/* 59 */       validator.validateEntity(this.bystander, "bystander");
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\LightningStrikeTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */