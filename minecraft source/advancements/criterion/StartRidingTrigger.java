/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class StartRidingTrigger
/*    */   extends SimpleCriterionTrigger<StartRidingTrigger.TriggerInstance> {
/* 14 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public void trigger(ServerPlayer player) { trigger(player, t -> true); }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player;
/*    */     public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/StartRidingTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/StartRidingTrigger$TriggerInstance; }
/* 21 */     public TriggerInstance(Optional<ContextAwarePredicate> player) { this.player = player; } public Optional<ContextAwarePredicate> player() { return this.player; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/StartRidingTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/StartRidingTrigger$TriggerInstance; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/StartRidingTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/StartRidingTrigger$TriggerInstance;
/*    */       //   0	8	1	o	Ljava/lang/Object; }
/* 24 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 25 */           .optionalFieldOf("player").forGetter(TriggerInstance::player))
/* 26 */         .apply(i, TriggerInstance::new));
/*    */ 
/*    */     
/* 29 */     public static Criterion<TriggerInstance> playerStartsRiding(EntityPredicate.Builder player) { return CriteriaTriggers.START_RIDING_TRIGGER.createCriterion(new TriggerInstance(Optional.of(EntityPredicate.wrap(player)))); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\StartRidingTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */