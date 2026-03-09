/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ 
/*    */ public final class DamagePredicate extends Record {
/*    */   private final MinMaxBounds.Doubles dealtDamage;
/*    */   private final MinMaxBounds.Doubles takenDamage;
/*    */   
/* 10 */   public DamagePredicate(MinMaxBounds.Doubles dealtDamage, MinMaxBounds.Doubles takenDamage, Optional<EntityPredicate> sourceEntity, Optional<Boolean> blocked, Optional<DamageSourcePredicate> type) { this.dealtDamage = dealtDamage; this.takenDamage = takenDamage; this.sourceEntity = sourceEntity; this.blocked = blocked; this.type = type; } private final Optional<EntityPredicate> sourceEntity; private final Optional<Boolean> blocked; private final Optional<DamageSourcePredicate> type; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/DamagePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/DamagePredicate; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/DamagePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/DamagePredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/DamagePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/DamagePredicate;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Doubles dealtDamage() { return this.dealtDamage; } public MinMaxBounds.Doubles takenDamage() { return this.takenDamage; } public Optional<EntityPredicate> sourceEntity() { return this.sourceEntity; } public Optional<Boolean> blocked() { return this.blocked; } public Optional<DamageSourcePredicate> type() { return this.type; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static final Codec<DamagePredicate> CODEC = RecordCodecBuilder.create(i -> i.group(MinMaxBounds.Doubles.CODEC
/* 18 */         .optionalFieldOf("dealt", MinMaxBounds.Doubles.ANY).forGetter(DamagePredicate::dealtDamage), MinMaxBounds.Doubles.CODEC
/* 19 */         .optionalFieldOf("taken", MinMaxBounds.Doubles.ANY).forGetter(DamagePredicate::takenDamage), EntityPredicate.CODEC
/* 20 */         .optionalFieldOf("source_entity").forGetter(DamagePredicate::sourceEntity), Codec.BOOL
/* 21 */         .optionalFieldOf("blocked").forGetter(DamagePredicate::blocked), DamageSourcePredicate.CODEC
/* 22 */         .optionalFieldOf("type").forGetter(DamagePredicate::type))
/* 23 */       .apply(i, DamagePredicate::new));
/*    */   
/*    */   public boolean matches(ServerPlayer player, DamageSource source, float originalDamage, float actualDamage, boolean blocked) {
/* 26 */     if (!this.dealtDamage.matches(originalDamage)) {
/* 27 */       return false;
/*    */     }
/* 29 */     if (!this.takenDamage.matches(actualDamage)) {
/* 30 */       return false;
/*    */     }
/* 32 */     if (this.sourceEntity.isPresent() && !((EntityPredicate)this.sourceEntity.get()).matches(player, source.getEntity())) {
/* 33 */       return false;
/*    */     }
/* 35 */     if (this.blocked.isPresent() && ((Boolean)this.blocked.get()).booleanValue() != blocked) {
/* 36 */       return false;
/*    */     }
/* 38 */     if (this.type.isPresent() && !((DamageSourcePredicate)this.type.get()).matches(player, source)) {
/* 39 */       return false;
/*    */     }
/* 41 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 45 */     private MinMaxBounds.Doubles dealtDamage = MinMaxBounds.Doubles.ANY;
/* 46 */     private MinMaxBounds.Doubles takenDamage = MinMaxBounds.Doubles.ANY;
/* 47 */     private Optional<EntityPredicate> sourceEntity = Optional.empty();
/* 48 */     private Optional<Boolean> blocked = Optional.empty();
/* 49 */     private Optional<DamageSourcePredicate> type = Optional.empty();
/*    */ 
/*    */     
/* 52 */     public static Builder damageInstance() { return new Builder(); }
/*    */ 
/*    */     
/*    */     public Builder dealtDamage(MinMaxBounds.Doubles dealtDamage) {
/* 56 */       this.dealtDamage = dealtDamage;
/* 57 */       return this;
/*    */     }
/*    */     
/*    */     public Builder takenDamage(MinMaxBounds.Doubles takenDamage) {
/* 61 */       this.takenDamage = takenDamage;
/* 62 */       return this;
/*    */     }
/*    */     
/*    */     public Builder sourceEntity(EntityPredicate sourceEntity) {
/* 66 */       this.sourceEntity = Optional.of(sourceEntity);
/* 67 */       return this;
/*    */     }
/*    */     
/*    */     public Builder blocked(Boolean blocked) {
/* 71 */       this.blocked = Optional.of(blocked);
/* 72 */       return this;
/*    */     }
/*    */     
/*    */     public Builder type(DamageSourcePredicate type) {
/* 76 */       this.type = Optional.of(type);
/* 77 */       return this;
/*    */     }
/*    */     
/*    */     public Builder type(DamageSourcePredicate.Builder type) {
/* 81 */       this.type = Optional.of(type.build());
/* 82 */       return this;
/*    */     }
/*    */ 
/*    */     
/* 86 */     public DamagePredicate build() { return new DamagePredicate(this.dealtDamage, this.takenDamage, this.sourceEntity, this.blocked, this.type); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\DamagePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */