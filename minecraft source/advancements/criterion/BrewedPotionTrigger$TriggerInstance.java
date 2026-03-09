/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.alchemy.Potion;
/*    */ 
/*    */ public final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   private final Optional<Holder<Potion>> potion;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/BrewedPotionTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/BrewedPotionTrigger$TriggerInstance; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/BrewedPotionTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/BrewedPotionTrigger$TriggerInstance; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/BrewedPotionTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/BrewedPotionTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 23 */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<Holder<Potion>> potion) { this.player = player; this.potion = potion; } public Optional<ContextAwarePredicate> player() { return this.player; } public Optional<Holder<Potion>> potion() { return this.potion; }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 28 */         .optionalFieldOf("player").forGetter(TriggerInstance::player), Potion.CODEC
/* 29 */         .optionalFieldOf("potion").forGetter(TriggerInstance::potion))
/* 30 */       .apply(i, TriggerInstance::new));
/*    */ 
/*    */   
/* 33 */   public static Criterion<TriggerInstance> brewedPotion() { return CriteriaTriggers.BREWED_POTION.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty())); }
/*    */ 
/*    */   
/*    */   public boolean matches(Holder<Potion> potion) {
/* 37 */     if (this.potion.isPresent() && !((Holder)this.potion.get()).equals(potion)) {
/* 38 */       return false;
/*    */     }
/* 40 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\BrewedPotionTrigger$TriggerInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */