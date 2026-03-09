/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ 
/*    */ public final class TriggerInstance
/*    */   extends Record
/*    */   implements SimpleCriterionTrigger.SimpleInstance
/*    */ {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   private final Optional<ContextAwarePredicate> zombie;
/*    */   private final Optional<ContextAwarePredicate> villager;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/CuredZombieVillagerTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/CuredZombieVillagerTrigger$TriggerInstance; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/CuredZombieVillagerTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/CuredZombieVillagerTrigger$TriggerInstance; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/CuredZombieVillagerTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/CuredZombieVillagerTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 27 */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> zombie, Optional<ContextAwarePredicate> villager) { this.player = player; this.zombie = zombie; this.villager = villager; } public Optional<ContextAwarePredicate> player() { return this.player; } public Optional<ContextAwarePredicate> zombie() { return this.zombie; } public Optional<ContextAwarePredicate> villager() { return this.villager; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 33 */         .optionalFieldOf("player").forGetter(TriggerInstance::player), EntityPredicate.ADVANCEMENT_CODEC
/* 34 */         .optionalFieldOf("zombie").forGetter(TriggerInstance::zombie), EntityPredicate.ADVANCEMENT_CODEC
/* 35 */         .optionalFieldOf("villager").forGetter(TriggerInstance::villager))
/* 36 */       .apply(i, TriggerInstance::new));
/*    */ 
/*    */   
/* 39 */   public static Criterion<TriggerInstance> curedZombieVillager() { return CriteriaTriggers.CURED_ZOMBIE_VILLAGER.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty())); }
/*    */ 
/*    */   
/*    */   public boolean matches(LootContext zombie, LootContext villager) {
/* 43 */     if (this.zombie.isPresent() && !((ContextAwarePredicate)this.zombie.get()).matches(zombie)) {
/* 44 */       return false;
/*    */     }
/* 46 */     if (this.villager.isPresent() && !((ContextAwarePredicate)this.villager.get()).matches(villager)) {
/* 47 */       return false;
/*    */     }
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(CriterionValidator validator) {
/* 54 */     super.validate(validator);
/* 55 */     validator.validateEntity(this.zombie, "zombie");
/* 56 */     validator.validateEntity(this.villager, "villager");
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\CuredZombieVillagerTrigger$TriggerInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */