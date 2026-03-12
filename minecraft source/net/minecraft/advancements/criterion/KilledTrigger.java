/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.advancements.Criterion;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ 
/*     */ public class KilledTrigger
/*     */   extends SimpleCriterionTrigger<KilledTrigger.TriggerInstance> {
/*  17 */   public Codec<TriggerInstance> codec() { return TriggerInstance.CODEC; }
/*     */ 
/*     */   
/*     */   public void trigger(ServerPlayer player, Entity entity, DamageSource killingBlow) {
/*  21 */     LootContext entityContext = EntityPredicate.createContext(player, entity);
/*  22 */     trigger(player, t -> t.matches(player, entityContext, killingBlow));
/*     */   }
/*     */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final Optional<ContextAwarePredicate> entityPredicate; private final Optional<DamageSourcePredicate> killingBlow;
/*  25 */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entityPredicate, Optional<DamageSourcePredicate> killingBlow) { this.player = player; this.entityPredicate = entityPredicate; this.killingBlow = killingBlow; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/KilledTrigger$TriggerInstance;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  25 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/KilledTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/KilledTrigger$TriggerInstance;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/KilledTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/KilledTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/KilledTrigger$TriggerInstance;
/*  25 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<ContextAwarePredicate> entityPredicate() { return this.entityPredicate; } public Optional<DamageSourcePredicate> killingBlow() { return this.killingBlow; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  30 */     public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/*  31 */           .optionalFieldOf("player").forGetter(TriggerInstance::player), EntityPredicate.ADVANCEMENT_CODEC
/*  32 */           .optionalFieldOf("entity").forGetter(TriggerInstance::entityPredicate), DamageSourcePredicate.CODEC
/*  33 */           .optionalFieldOf("killing_blow").forGetter(TriggerInstance::killingBlow))
/*  34 */         .apply(i, TriggerInstance::new));
/*     */ 
/*     */     
/*  37 */     public static Criterion<TriggerInstance> playerKilledEntity(Optional<EntityPredicate> entity) { return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), Optional.empty())); }
/*     */ 
/*     */ 
/*     */     
/*  41 */     public static Criterion<TriggerInstance> playerKilledEntity(EntityPredicate.Builder entity) { return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), Optional.empty())); }
/*     */ 
/*     */ 
/*     */     
/*  45 */     public static Criterion<TriggerInstance> playerKilledEntity() { return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty())); }
/*     */ 
/*     */ 
/*     */     
/*  49 */     public static Criterion<TriggerInstance> playerKilledEntity(Optional<EntityPredicate> entity, Optional<DamageSourcePredicate> killingBlow) { return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), killingBlow)); }
/*     */ 
/*     */ 
/*     */     
/*  53 */     public static Criterion<TriggerInstance> playerKilledEntity(EntityPredicate.Builder entity, Optional<DamageSourcePredicate> killingBlow) { return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), killingBlow)); }
/*     */ 
/*     */ 
/*     */     
/*  57 */     public static Criterion<TriggerInstance> playerKilledEntity(Optional<EntityPredicate> entity, DamageSourcePredicate.Builder killingBlow) { return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), Optional.of(killingBlow.build()))); }
/*     */ 
/*     */ 
/*     */     
/*  61 */     public static Criterion<TriggerInstance> playerKilledEntity(EntityPredicate.Builder entity, DamageSourcePredicate.Builder killingBlow) { return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), Optional.of(killingBlow.build()))); }
/*     */ 
/*     */ 
/*     */     
/*  65 */     public static Criterion<TriggerInstance> playerKilledEntityNearSculkCatalyst() { return CriteriaTriggers.KILL_MOB_NEAR_SCULK_CATALYST.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty())); }
/*     */ 
/*     */ 
/*     */     
/*  69 */     public static Criterion<TriggerInstance> entityKilledPlayer(Optional<EntityPredicate> entity) { return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), Optional.empty())); }
/*     */ 
/*     */ 
/*     */     
/*  73 */     public static Criterion<TriggerInstance> entityKilledPlayer(EntityPredicate.Builder entity) { return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), Optional.empty())); }
/*     */ 
/*     */ 
/*     */     
/*  77 */     public static Criterion<TriggerInstance> entityKilledPlayer() { return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty())); }
/*     */ 
/*     */ 
/*     */     
/*  81 */     public static Criterion<TriggerInstance> entityKilledPlayer(Optional<EntityPredicate> entity, Optional<DamageSourcePredicate> killingBlow) { return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), killingBlow)); }
/*     */ 
/*     */ 
/*     */     
/*  85 */     public static Criterion<TriggerInstance> entityKilledPlayer(EntityPredicate.Builder entity, Optional<DamageSourcePredicate> killingBlow) { return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), killingBlow)); }
/*     */ 
/*     */ 
/*     */     
/*  89 */     public static Criterion<TriggerInstance> entityKilledPlayer(Optional<EntityPredicate> entity, DamageSourcePredicate.Builder killingBlow) { return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), Optional.of(killingBlow.build()))); }
/*     */ 
/*     */ 
/*     */     
/*  93 */     public static Criterion<TriggerInstance> entityKilledPlayer(EntityPredicate.Builder entity, DamageSourcePredicate.Builder killingBlow) { return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), Optional.of(killingBlow.build()))); }
/*     */ 
/*     */     
/*     */     public boolean matches(ServerPlayer player, LootContext entity, DamageSource killingBlow) {
/*  97 */       if (this.killingBlow.isPresent() && !((DamageSourcePredicate)this.killingBlow.get()).matches(player, killingBlow)) {
/*  98 */         return false;
/*     */       }
/* 100 */       return (this.entityPredicate.isEmpty() || ((ContextAwarePredicate)this.entityPredicate.get()).matches(entity));
/*     */     }
/*     */ 
/*     */     
/*     */     public void validate(CriterionValidator validator) {
/* 105 */       super.validate(validator);
/* 106 */       validator.validateEntity(this.entityPredicate, "entity");
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\KilledTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */