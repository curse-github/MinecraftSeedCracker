/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class FallAfterExplosionTrigger
/*    */   extends SimpleCriterionTrigger<FallAfterExplosionTrigger.TriggerInstance>
/*    */ {
/* 19 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*    */ 
/*    */   
/*    */   public void trigger(ServerPlayer player, Vec3 startPosition, Entity cause) {
/* 23 */     Vec3 playerPosition = player.position();
/* 24 */     LootContext wrappedCause = (cause != null) ? EntityPredicate.createContext(player, cause) : null;
/* 25 */     trigger(player, t -> t.matches(player.level(), startPosition, playerPosition, wrappedCause));
/*    */   }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final Optional<LocationPredicate> startPosition; private final Optional<DistancePredicate> distance; private final Optional<ContextAwarePredicate> cause;
/* 28 */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<LocationPredicate> startPosition, Optional<DistancePredicate> distance, Optional<ContextAwarePredicate> cause) { this.player = player; this.startPosition = startPosition; this.distance = distance; this.cause = cause; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/FallAfterExplosionTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 28 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/FallAfterExplosionTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/FallAfterExplosionTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/FallAfterExplosionTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/FallAfterExplosionTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/FallAfterExplosionTrigger$TriggerInstance;
/* 28 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<LocationPredicate> startPosition() { return this.startPosition; } public Optional<DistancePredicate> distance() { return this.distance; } public Optional<ContextAwarePredicate> cause() { return this.cause; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 34 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 35 */           .optionalFieldOf("player").forGetter(TriggerInstance::player), LocationPredicate.CODEC
/* 36 */           .optionalFieldOf("start_position").forGetter(TriggerInstance::startPosition), DistancePredicate.CODEC
/* 37 */           .optionalFieldOf("distance").forGetter(TriggerInstance::distance), EntityPredicate.ADVANCEMENT_CODEC
/* 38 */           .optionalFieldOf("cause").forGetter(TriggerInstance::cause))
/* 39 */         .apply(i, TriggerInstance::new));
/*    */ 
/*    */     
/* 42 */     public static Criterion<TriggerInstance> fallAfterExplosion(DistancePredicate distance, EntityPredicate.Builder cause) { return CriteriaTriggers.FALL_AFTER_EXPLOSION.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(distance), Optional.of(EntityPredicate.wrap(cause)))); }
/*    */ 
/*    */ 
/*    */     
/*    */     public void validate(CriterionValidator validator) {
/* 47 */       super.validate(validator);
/* 48 */       validator.validateEntity(cause(), "cause");
/*    */     }
/*    */     
/*    */     public boolean matches(ServerLevel level, Vec3 enteredPosition, Vec3 playerPosition, LootContext cause) {
/* 52 */       if (this.startPosition.isPresent() && !((LocationPredicate)this.startPosition.get()).matches(level, enteredPosition.x, enteredPosition.y, enteredPosition.z)) {
/* 53 */         return false;
/*    */       }
/* 55 */       if (this.distance.isPresent() && !((DistancePredicate)this.distance.get()).matches(enteredPosition.x, enteredPosition.y, enteredPosition.z, playerPosition.x, playerPosition.y, playerPosition.z)) {
/* 56 */         return false;
/*    */       }
/* 58 */       if (this.cause.isPresent() && (cause == null || !((ContextAwarePredicate)this.cause.get()).matches(cause))) {
/* 59 */         return false;
/*    */       }
/* 61 */       return true;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\FallAfterExplosionTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */