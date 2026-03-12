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
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class TriggerInstance
/*    */   extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   private final MinMaxBounds.Ints signalStrength;
/*    */   private final Optional<ContextAwarePredicate> projectile;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/TargetBlockTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/TargetBlockTrigger$TriggerInstance; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/TargetBlockTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/TargetBlockTrigger$TriggerInstance; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/TargetBlockTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/TargetBlockTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 25 */   public TriggerInstance(Optional<ContextAwarePredicate> player, MinMaxBounds.Ints signalStrength, Optional<ContextAwarePredicate> projectile) { this.player = player; this.signalStrength = signalStrength; this.projectile = projectile; } public Optional<ContextAwarePredicate> player() { return this.player; } public MinMaxBounds.Ints signalStrength() { return this.signalStrength; } public Optional<ContextAwarePredicate> projectile() { return this.projectile; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/* 31 */         .optionalFieldOf("player").forGetter(TriggerInstance::player), MinMaxBounds.Ints.CODEC
/* 32 */         .optionalFieldOf("signal_strength", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::signalStrength), EntityPredicate.ADVANCEMENT_CODEC
/* 33 */         .optionalFieldOf("projectile").forGetter(TriggerInstance::projectile))
/* 34 */       .apply(i, TriggerInstance::new));
/*    */ 
/*    */   
/* 37 */   public static Criterion<TriggerInstance> targetHit(MinMaxBounds.Ints redstoneSignalStrength, Optional<ContextAwarePredicate> projectile) { return CriteriaTriggers.TARGET_BLOCK_HIT.createCriterion(new TriggerInstance(Optional.empty(), redstoneSignalStrength, projectile)); }
/*    */ 
/*    */   
/*    */   public boolean matches(LootContext projectile, Vec3 hitPosition, int signalStrength) {
/* 41 */     if (!this.signalStrength.matches(signalStrength)) {
/* 42 */       return false;
/*    */     }
/* 44 */     if (this.projectile.isPresent() && !((ContextAwarePredicate)this.projectile.get()).matches(projectile)) {
/* 45 */       return false;
/*    */     }
/* 47 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(CriterionValidator validator) {
/* 52 */     super.validate(validator);
/* 53 */     validator.validateEntity(this.projectile, "projectile");
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\TargetBlockTrigger$TriggerInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */