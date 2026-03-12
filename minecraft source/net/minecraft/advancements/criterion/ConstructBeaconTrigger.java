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
/*    */ 
/*    */ public class ConstructBeaconTrigger
/*    */   extends SimpleCriterionTrigger<ConstructBeaconTrigger.TriggerInstance> {
/* 14 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public void trigger(ServerPlayer player, int levels) { trigger(player, t -> t.matches(levels)); }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final MinMaxBounds.Ints level;
/*    */     
/* 21 */     public TriggerInstance(Optional<ContextAwarePredicate> player, MinMaxBounds.Ints level) { this.player = player; this.level = level; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/ConstructBeaconTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 21 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/ConstructBeaconTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/ConstructBeaconTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/ConstructBeaconTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/ConstructBeaconTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/ConstructBeaconTrigger$TriggerInstance;
/* 21 */       //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Ints level() { return this.level; }
/*    */ 
/*    */ 
/*    */     
/* 25 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 26 */           .optionalFieldOf("player").forGetter(TriggerInstance::player), MinMaxBounds.Ints.CODEC
/* 27 */           .optionalFieldOf("level", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::level))
/* 28 */         .apply(i, TriggerInstance::new));
/*    */ 
/*    */     
/* 31 */     public static Criterion<TriggerInstance> constructedBeacon() { return CriteriaTriggers.CONSTRUCT_BEACON.createCriterion(new TriggerInstance(Optional.empty(), MinMaxBounds.Ints.ANY)); }
/*    */ 
/*    */ 
/*    */     
/* 35 */     public static Criterion<TriggerInstance> constructedBeacon(MinMaxBounds.Ints level) { return CriteriaTriggers.CONSTRUCT_BEACON.createCriterion(new TriggerInstance(Optional.empty(), level)); }
/*    */ 
/*    */ 
/*    */     
/* 39 */     public boolean matches(int levels) { return this.level.matches(levels); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\ConstructBeaconTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */