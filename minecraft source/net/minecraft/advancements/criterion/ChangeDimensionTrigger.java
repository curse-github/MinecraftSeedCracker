/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ChangeDimensionTrigger
/*    */   extends SimpleCriterionTrigger<ChangeDimensionTrigger.TriggerInstance> {
/* 17 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public void trigger(ServerPlayer player, ResourceKey<Level> from, ResourceKey<Level> to) { trigger(player, t -> t.matches(from, to)); }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final Optional<ResourceKey<Level>> from; private final Optional<ResourceKey<Level>> to;
/*    */     
/* 24 */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ResourceKey<Level>> from, Optional<ResourceKey<Level>> to) { this.player = player; this.from = from; this.to = to; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/ChangeDimensionTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 24 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/ChangeDimensionTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/ChangeDimensionTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/ChangeDimensionTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/ChangeDimensionTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/ChangeDimensionTrigger$TriggerInstance;
/* 24 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<ResourceKey<Level>> from() { return this.from; } public Optional<ResourceKey<Level>> to() { return this.to; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 29 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 30 */           .optionalFieldOf("player").forGetter(TriggerInstance::player), 
/* 31 */           ResourceKey.codec(Registries.DIMENSION).optionalFieldOf("from").forGetter(TriggerInstance::from), 
/* 32 */           ResourceKey.codec(Registries.DIMENSION).optionalFieldOf("to").forGetter(TriggerInstance::to))
/* 33 */         .apply(i, TriggerInstance::new));
/*    */ 
/*    */     
/* 36 */     public static Criterion<TriggerInstance> changedDimension() { return CriteriaTriggers.CHANGED_DIMENSION.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty())); }
/*    */ 
/*    */ 
/*    */     
/* 40 */     public static Criterion<TriggerInstance> changedDimension(ResourceKey<Level> from, ResourceKey<Level> to) { return CriteriaTriggers.CHANGED_DIMENSION.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(from), Optional.of(to))); }
/*    */ 
/*    */ 
/*    */     
/* 44 */     public static Criterion<TriggerInstance> changedDimensionTo(ResourceKey<Level> to) { return CriteriaTriggers.CHANGED_DIMENSION.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(to))); }
/*    */ 
/*    */ 
/*    */     
/* 48 */     public static Criterion<TriggerInstance> changedDimensionFrom(ResourceKey<Level> from) { return CriteriaTriggers.CHANGED_DIMENSION.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(from), Optional.empty())); }
/*    */ 
/*    */     
/*    */     public boolean matches(ResourceKey<Level> from, ResourceKey<Level> to) {
/* 52 */       if (this.from.isPresent() && this.from.get() != from) {
/* 53 */         return false;
/*    */       }
/* 55 */       if (this.to.isPresent() && this.to.get() != to) {
/* 56 */         return false;
/*    */       }
/* 58 */       return true;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\ChangeDimensionTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */