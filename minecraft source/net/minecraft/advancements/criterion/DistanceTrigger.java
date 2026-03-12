/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DistanceTrigger
/*    */   extends SimpleCriterionTrigger<DistanceTrigger.TriggerInstance> {
/* 16 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*    */ 
/*    */   
/*    */   public void trigger(ServerPlayer player, Vec3 startPosition) {
/* 20 */     Vec3 playerPosition = player.position();
/* 21 */     trigger(player, t -> t.matches(player.level(), startPosition, playerPosition));
/*    */   }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final Optional<LocationPredicate> startPosition; private final Optional<DistancePredicate> distance;
/* 24 */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<LocationPredicate> startPosition, Optional<DistancePredicate> distance) { this.player = player; this.startPosition = startPosition; this.distance = distance; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/DistanceTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 24 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/DistanceTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/DistanceTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/DistanceTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/DistanceTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/DistanceTrigger$TriggerInstance;
/* 24 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<LocationPredicate> startPosition() { return this.startPosition; } public Optional<DistancePredicate> distance() { return this.distance; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 29 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 30 */           .optionalFieldOf("player").forGetter(TriggerInstance::player), LocationPredicate.CODEC
/* 31 */           .optionalFieldOf("start_position").forGetter(TriggerInstance::startPosition), DistancePredicate.CODEC
/* 32 */           .optionalFieldOf("distance").forGetter(TriggerInstance::distance))
/* 33 */         .apply(i, TriggerInstance::new));
/*    */ 
/*    */     
/* 36 */     public static Criterion<TriggerInstance> fallFromHeight(EntityPredicate.Builder player, DistancePredicate distance, LocationPredicate.Builder startPosition) { return CriteriaTriggers.FALL_FROM_HEIGHT.createCriterion(new TriggerInstance(Optional.of(EntityPredicate.wrap(player)), Optional.of(startPosition.build()), Optional.of(distance))); }
/*    */ 
/*    */ 
/*    */     
/* 40 */     public static Criterion<TriggerInstance> rideEntityInLava(EntityPredicate.Builder player, DistancePredicate distance) { return CriteriaTriggers.RIDE_ENTITY_IN_LAVA_TRIGGER.createCriterion(new TriggerInstance(Optional.of(EntityPredicate.wrap(player)), Optional.empty(), Optional.of(distance))); }
/*    */ 
/*    */ 
/*    */     
/* 44 */     public static Criterion<TriggerInstance> travelledThroughNether(DistancePredicate distance) { return CriteriaTriggers.NETHER_TRAVEL.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(distance))); }
/*    */ 
/*    */     
/*    */     public boolean matches(ServerLevel level, Vec3 enteredPosition, Vec3 playerPosition) {
/* 48 */       if (this.startPosition.isPresent() && !((LocationPredicate)this.startPosition.get()).matches(level, enteredPosition.x, enteredPosition.y, enteredPosition.z)) {
/* 49 */         return false;
/*    */       }
/* 51 */       if (this.distance.isPresent() && !((DistancePredicate)this.distance.get()).matches(enteredPosition.x, enteredPosition.y, enteredPosition.z, playerPosition.x, playerPosition.y, playerPosition.z)) {
/* 52 */         return false;
/*    */       }
/* 54 */       return true;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\DistanceTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */