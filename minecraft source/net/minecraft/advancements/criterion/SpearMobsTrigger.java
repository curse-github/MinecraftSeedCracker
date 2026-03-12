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
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class SpearMobsTrigger
/*    */   extends SimpleCriterionTrigger<SpearMobsTrigger.TriggerInstance> {
/* 15 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public void trigger(ServerPlayer player, int number) { trigger(player, t -> t.matches(number)); }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final Optional<Integer> count;
/*    */     
/* 22 */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<Integer> count) { this.player = player; this.count = count; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/SpearMobsTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 22 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/SpearMobsTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/SpearMobsTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/SpearMobsTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/SpearMobsTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/SpearMobsTrigger$TriggerInstance;
/* 22 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<Integer> count() { return this.count; }
/*    */ 
/*    */ 
/*    */     
/* 26 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 27 */           .optionalFieldOf("player").forGetter(TriggerInstance::player), ExtraCodecs.POSITIVE_INT
/* 28 */           .optionalFieldOf("count").forGetter(TriggerInstance::count))
/* 29 */         .apply(i, TriggerInstance::new));
/*    */ 
/*    */     
/* 32 */     public static Criterion<TriggerInstance> spearMobs(int requiredCount) { return CriteriaTriggers.SPEAR_MOBS_TRIGGER.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(Integer.valueOf(requiredCount)))); }
/*    */ 
/*    */ 
/*    */     
/* 36 */     public boolean matches(int requiredCount) { return (this.count.isEmpty() || requiredCount >= ((Integer)this.count.get()).intValue()); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\SpearMobsTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */