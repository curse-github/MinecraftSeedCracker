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
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class TriggerInstance
/*    */   extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   private final Optional<MobEffectsPredicate> effects;
/*    */   private final Optional<ContextAwarePredicate> source;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/EffectsChangedTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EffectsChangedTrigger$TriggerInstance; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/EffectsChangedTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EffectsChangedTrigger$TriggerInstance; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/EffectsChangedTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/EffectsChangedTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 25 */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<MobEffectsPredicate> effects, Optional<ContextAwarePredicate> source) { this.player = player; this.effects = effects; this.source = source; } public Optional<ContextAwarePredicate> player() { return this.player; } public Optional<MobEffectsPredicate> effects() { return this.effects; } public Optional<ContextAwarePredicate> source() { return this.source; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 31 */         .optionalFieldOf("player").forGetter(TriggerInstance::player), MobEffectsPredicate.CODEC
/* 32 */         .optionalFieldOf("effects").forGetter(TriggerInstance::effects), EntityPredicate.ADVANCEMENT_CODEC
/* 33 */         .optionalFieldOf("source").forGetter(TriggerInstance::source))
/* 34 */       .apply(i, TriggerInstance::new));
/*    */ 
/*    */   
/* 37 */   public static Criterion<TriggerInstance> hasEffects(MobEffectsPredicate.Builder effects) { return CriteriaTriggers.EFFECTS_CHANGED.createCriterion(new TriggerInstance(Optional.empty(), effects.build(), Optional.empty())); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public static Criterion<TriggerInstance> gotEffectsFrom(EntityPredicate.Builder source) { return CriteriaTriggers.EFFECTS_CHANGED.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(EntityPredicate.wrap(source.build())))); }
/*    */ 
/*    */   
/*    */   public boolean matches(ServerPlayer player, LootContext source) {
/* 45 */     if (this.effects.isPresent() && !((MobEffectsPredicate)this.effects.get()).matches(player)) {
/* 46 */       return false;
/*    */     }
/*    */     
/* 49 */     if (this.source.isPresent() && (
/* 50 */       source == null || !((ContextAwarePredicate)this.source.get()).matches(source))) {
/* 51 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 55 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(CriterionValidator validator) {
/* 60 */     super.validate(validator);
/* 61 */     validator.validateEntity(this.source, "source");
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\EffectsChangedTrigger$TriggerInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */