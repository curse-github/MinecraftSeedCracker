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
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class LevitationTrigger
/*    */   extends SimpleCriterionTrigger<LevitationTrigger.TriggerInstance> {
/* 15 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public void trigger(ServerPlayer player, Vec3 start, int duration) { trigger(player, t -> t.matches(player, start, duration)); }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final Optional<DistancePredicate> distance; private final MinMaxBounds.Ints duration;
/*    */     
/* 22 */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<DistancePredicate> distance, MinMaxBounds.Ints duration) { this.player = player; this.distance = distance; this.duration = duration; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/LevitationTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 22 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/LevitationTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/LevitationTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/LevitationTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/LevitationTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/LevitationTrigger$TriggerInstance;
/* 22 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<DistancePredicate> distance() { return this.distance; } public MinMaxBounds.Ints duration() { return this.duration; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 27 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 28 */           .optionalFieldOf("player").forGetter(TriggerInstance::player), DistancePredicate.CODEC
/* 29 */           .optionalFieldOf("distance").forGetter(TriggerInstance::distance), MinMaxBounds.Ints.CODEC
/* 30 */           .optionalFieldOf("duration", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::duration))
/* 31 */         .apply(i, TriggerInstance::new));
/*    */ 
/*    */     
/* 34 */     public static Criterion<TriggerInstance> levitated(DistancePredicate distance) { return CriteriaTriggers.LEVITATION.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(distance), MinMaxBounds.Ints.ANY)); }
/*    */ 
/*    */     
/*    */     public boolean matches(ServerPlayer player, Vec3 start, int duration) {
/* 38 */       if (this.distance.isPresent() && !((DistancePredicate)this.distance.get()).matches(start.x, start.y, start.z, player.getX(), player.getY(), player.getZ())) {
/* 39 */         return false;
/*    */       }
/* 41 */       if (!this.duration.matches(duration)) {
/* 42 */         return false;
/*    */       }
/* 44 */       return true;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\LevitationTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */