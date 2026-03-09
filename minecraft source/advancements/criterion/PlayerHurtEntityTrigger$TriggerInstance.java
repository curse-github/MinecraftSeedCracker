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
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   private final Optional<DamagePredicate> damage;
/*    */   private final Optional<ContextAwarePredicate> entity;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/PlayerHurtEntityTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerHurtEntityTrigger$TriggerInstance; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/PlayerHurtEntityTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/PlayerHurtEntityTrigger$TriggerInstance; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/PlayerHurtEntityTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/PlayerHurtEntityTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 25 */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<DamagePredicate> damage, Optional<ContextAwarePredicate> entity) { this.player = player; this.damage = damage; this.entity = entity; } public Optional<ContextAwarePredicate> player() { return this.player; } public Optional<DamagePredicate> damage() { return this.damage; } public Optional<ContextAwarePredicate> entity() { return this.entity; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 31 */         .optionalFieldOf("player").forGetter(TriggerInstance::player), DamagePredicate.CODEC
/* 32 */         .optionalFieldOf("damage").forGetter(TriggerInstance::damage), EntityPredicate.ADVANCEMENT_CODEC
/* 33 */         .optionalFieldOf("entity").forGetter(TriggerInstance::entity))
/* 34 */       .apply(i, TriggerInstance::new));
/*    */ 
/*    */   
/* 37 */   public static Criterion<TriggerInstance> playerHurtEntity() { return CriteriaTriggers.PLAYER_HURT_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty())); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public static Criterion<TriggerInstance> playerHurtEntityWithDamage(Optional<DamagePredicate> damage) { return CriteriaTriggers.PLAYER_HURT_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), damage, Optional.empty())); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public static Criterion<TriggerInstance> playerHurtEntityWithDamage(DamagePredicate.Builder damage) { return CriteriaTriggers.PLAYER_HURT_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(damage.build()), Optional.empty())); }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public static Criterion<TriggerInstance> playerHurtEntity(Optional<EntityPredicate> entity) { return CriteriaTriggers.PLAYER_HURT_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), EntityPredicate.wrap(entity))); }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public static Criterion<TriggerInstance> playerHurtEntity(Optional<DamagePredicate> damage, Optional<EntityPredicate> entity) { return CriteriaTriggers.PLAYER_HURT_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), damage, EntityPredicate.wrap(entity))); }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public static Criterion<TriggerInstance> playerHurtEntity(DamagePredicate.Builder damage, Optional<EntityPredicate> entity) { return CriteriaTriggers.PLAYER_HURT_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(damage.build()), EntityPredicate.wrap(entity))); }
/*    */ 
/*    */   
/*    */   public boolean matches(ServerPlayer player, LootContext victim, DamageSource source, float originalDamage, float actualDamage, boolean blocked) {
/* 61 */     if (this.damage.isPresent() && !((DamagePredicate)this.damage.get()).matches(player, source, originalDamage, actualDamage, blocked)) {
/* 62 */       return false;
/*    */     }
/* 64 */     if (this.entity.isPresent() && !((ContextAwarePredicate)this.entity.get()).matches(victim)) {
/* 65 */       return false;
/*    */     }
/* 67 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(CriterionValidator validator) {
/* 72 */     super.validate(validator);
/* 73 */     validator.validateEntity(this.entity, "entity");
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\PlayerHurtEntityTrigger$TriggerInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */