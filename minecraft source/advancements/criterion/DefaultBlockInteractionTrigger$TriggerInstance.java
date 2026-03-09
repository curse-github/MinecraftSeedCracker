/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TriggerInstance
/*    */   extends Record
/*    */   implements SimpleCriterionTrigger.SimpleInstance
/*    */ {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   private final Optional<ContextAwarePredicate> location;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/DefaultBlockInteractionTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #35	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/DefaultBlockInteractionTrigger$TriggerInstance; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/DefaultBlockInteractionTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #35	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/DefaultBlockInteractionTrigger$TriggerInstance; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/DefaultBlockInteractionTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #35	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/DefaultBlockInteractionTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 35 */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> location) { this.player = player; this.location = location; } public Optional<ContextAwarePredicate> player() { return this.player; } public Optional<ContextAwarePredicate> location() { return this.location; }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 40 */         .optionalFieldOf("player").forGetter(TriggerInstance::player), ContextAwarePredicate.CODEC
/* 41 */         .optionalFieldOf("location").forGetter(TriggerInstance::location))
/* 42 */       .apply(i, TriggerInstance::new));
/*    */ 
/*    */   
/* 45 */   public boolean matches(LootContext locationContext) { return (this.location.isEmpty() || ((ContextAwarePredicate)this.location.get()).matches(locationContext)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(CriterionValidator validator) {
/* 50 */     super.validate(validator);
/* 51 */     this.location.ifPresent(predicate -> validator.validate(predicate, LootContextParamSets.BLOCK_USE, "location"));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\DefaultBlockInteractionTrigger$TriggerInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */