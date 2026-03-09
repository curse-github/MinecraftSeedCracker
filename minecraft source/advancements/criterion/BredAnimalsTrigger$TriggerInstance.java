/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TriggerInstance
/*    */   extends Record
/*    */   implements SimpleCriterionTrigger.SimpleInstance
/*    */ {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   private final Optional<ContextAwarePredicate> parent;
/*    */   private final Optional<ContextAwarePredicate> partner;
/*    */   private final Optional<ContextAwarePredicate> child;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/BredAnimalsTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/BredAnimalsTrigger$TriggerInstance; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/BredAnimalsTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/BredAnimalsTrigger$TriggerInstance; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/BredAnimalsTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/BredAnimalsTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 29 */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> parent, Optional<ContextAwarePredicate> partner, Optional<ContextAwarePredicate> child) { this.player = player; this.parent = parent; this.partner = partner; this.child = child; } public Optional<ContextAwarePredicate> player() { return this.player; } public Optional<ContextAwarePredicate> parent() { return this.parent; } public Optional<ContextAwarePredicate> partner() { return this.partner; } public Optional<ContextAwarePredicate> child() { return this.child; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 36 */         .optionalFieldOf("player").forGetter(TriggerInstance::player), EntityPredicate.ADVANCEMENT_CODEC
/* 37 */         .optionalFieldOf("parent").forGetter(TriggerInstance::parent), EntityPredicate.ADVANCEMENT_CODEC
/* 38 */         .optionalFieldOf("partner").forGetter(TriggerInstance::partner), EntityPredicate.ADVANCEMENT_CODEC
/* 39 */         .optionalFieldOf("child").forGetter(TriggerInstance::child))
/* 40 */       .apply(i, TriggerInstance::new));
/*    */ 
/*    */   
/* 43 */   public static Criterion<TriggerInstance> bredAnimals() { return CriteriaTriggers.BRED_ANIMALS.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())); }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public static Criterion<TriggerInstance> bredAnimals(EntityPredicate.Builder child) { return CriteriaTriggers.BRED_ANIMALS.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(EntityPredicate.wrap(child)))); }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public static Criterion<TriggerInstance> bredAnimals(Optional<EntityPredicate> parent1, Optional<EntityPredicate> parent2, Optional<EntityPredicate> child) { return CriteriaTriggers.BRED_ANIMALS.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(parent1), EntityPredicate.wrap(parent2), EntityPredicate.wrap(child))); }
/*    */ 
/*    */   
/*    */   public boolean matches(LootContext parent, LootContext partner, LootContext child) {
/* 55 */     if (this.child.isPresent() && (child == null || !((ContextAwarePredicate)this.child.get()).matches(child))) {
/* 56 */       return false;
/*    */     }
/*    */     
/* 59 */     return ((matches(this.parent, parent) && matches(this.partner, partner)) || (matches(this.parent, partner) && matches(this.partner, parent)));
/*    */   }
/*    */ 
/*    */   
/* 63 */   private static boolean matches(Optional<ContextAwarePredicate> predicate, LootContext context) { return (predicate.isEmpty() || ((ContextAwarePredicate)predicate.get()).matches(context)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(CriterionValidator validator) {
/* 68 */     super.validate(validator);
/* 69 */     validator.validateEntity(this.parent, "parent");
/* 70 */     validator.validateEntity(this.partner, "partner");
/* 71 */     validator.validateEntity(this.child, "child");
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\BredAnimalsTrigger$TriggerInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */