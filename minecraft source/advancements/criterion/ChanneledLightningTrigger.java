/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class ChanneledLightningTrigger
/*    */   extends SimpleCriterionTrigger<ChanneledLightningTrigger.TriggerInstance> {
/* 19 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*    */ 
/*    */   
/*    */   public void trigger(ServerPlayer player, Collection<? extends Entity> victims) {
/* 23 */     List<LootContext> victimsContexts = (List)victims.stream().map(v -> EntityPredicate.createContext(player, v)).collect(Collectors.toList());
/* 24 */     trigger(player, t -> t.matches(victimsContexts));
/*    */   }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final List<ContextAwarePredicate> victims;
/* 27 */     public TriggerInstance(Optional<ContextAwarePredicate> player, List<ContextAwarePredicate> victims) { this.player = player; this.victims = victims; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/ChanneledLightningTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 27 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/ChanneledLightningTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/ChanneledLightningTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/ChanneledLightningTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/ChanneledLightningTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/ChanneledLightningTrigger$TriggerInstance;
/* 27 */       //   0	8	1	o	Ljava/lang/Object; } public List<ContextAwarePredicate> victims() { return this.victims; }
/*    */ 
/*    */ 
/*    */     
/* 31 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 32 */           .optionalFieldOf("player").forGetter(TriggerInstance::player), EntityPredicate.ADVANCEMENT_CODEC
/* 33 */           .listOf().optionalFieldOf("victims", List.of()).forGetter(TriggerInstance::victims))
/* 34 */         .apply(i, TriggerInstance::new));
/*    */ 
/*    */     
/* 37 */     public static Criterion<TriggerInstance> channeledLightning(Builder... victims) { return CriteriaTriggers.CHANNELED_LIGHTNING.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(victims))); }
/*    */ 
/*    */     
/*    */     public boolean matches(Collection<? extends LootContext> victims) {
/* 41 */       for (ContextAwarePredicate predicate : this.victims) {
/* 42 */         boolean found = false;
/* 43 */         for (LootContext victim : victims) {
/* 44 */           if (predicate.matches(victim)) {
/* 45 */             found = true;
/*    */             break;
/*    */           } 
/*    */         } 
/* 49 */         if (!found) {
/* 50 */           return false;
/*    */         }
/*    */       } 
/* 53 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public void validate(CriterionValidator validator) {
/* 58 */       super.validate(validator);
/* 59 */       validator.validateEntities(this.victims, "victims");
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\ChanneledLightningTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */