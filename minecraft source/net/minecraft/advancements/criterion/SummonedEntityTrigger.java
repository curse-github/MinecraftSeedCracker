/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class SummonedEntityTrigger
/*    */   extends SimpleCriterionTrigger<SummonedEntityTrigger.TriggerInstance> {
/* 16 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*    */ 
/*    */   
/*    */   public void trigger(ServerPlayer player, Entity entity) {
/* 20 */     LootContext context = EntityPredicate.createContext(player, entity);
/* 21 */     trigger(player, t -> t.matches(context));
/*    */   }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final Optional<ContextAwarePredicate> entity;
/* 24 */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entity) { this.player = player; this.entity = entity; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/SummonedEntityTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 24 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/SummonedEntityTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/SummonedEntityTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/SummonedEntityTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/SummonedEntityTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/SummonedEntityTrigger$TriggerInstance;
/* 24 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<ContextAwarePredicate> entity() { return this.entity; }
/*    */ 
/*    */ 
/*    */     
/* 28 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 29 */           .optionalFieldOf("player").forGetter(TriggerInstance::player), EntityPredicate.ADVANCEMENT_CODEC
/* 30 */           .optionalFieldOf("entity").forGetter(TriggerInstance::entity))
/* 31 */         .apply(i, TriggerInstance::new));
/*    */ 
/*    */     
/* 34 */     public static Criterion<TriggerInstance> summonedEntity(EntityPredicate.Builder predicate) { return CriteriaTriggers.SUMMONED_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(predicate)))); }
/*    */ 
/*    */ 
/*    */     
/* 38 */     public boolean matches(LootContext entity) { return (this.entity.isEmpty() || ((ContextAwarePredicate)this.entity.get()).matches(entity)); }
/*    */ 
/*    */ 
/*    */     
/*    */     public void validate(CriterionValidator validator) {
/* 43 */       super.validate(validator);
/* 44 */       validator.validateEntity(this.entity, "entity");
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\SummonedEntityTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */