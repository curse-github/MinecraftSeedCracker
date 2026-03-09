/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import java.util.Optional;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/* 45 */   private MinMaxBounds.Doubles dealtDamage = MinMaxBounds.Doubles.ANY;
/* 46 */   private MinMaxBounds.Doubles takenDamage = MinMaxBounds.Doubles.ANY;
/* 47 */   private Optional<EntityPredicate> sourceEntity = Optional.empty();
/* 48 */   private Optional<Boolean> blocked = Optional.empty();
/* 49 */   private Optional<DamageSourcePredicate> type = Optional.empty();
/*    */ 
/*    */   
/* 52 */   public static Builder damageInstance() { return new Builder(); }
/*    */ 
/*    */   
/*    */   public Builder dealtDamage(MinMaxBounds.Doubles dealtDamage) {
/* 56 */     this.dealtDamage = dealtDamage;
/* 57 */     return this;
/*    */   }
/*    */   
/*    */   public Builder takenDamage(MinMaxBounds.Doubles takenDamage) {
/* 61 */     this.takenDamage = takenDamage;
/* 62 */     return this;
/*    */   }
/*    */   
/*    */   public Builder sourceEntity(EntityPredicate sourceEntity) {
/* 66 */     this.sourceEntity = Optional.of(sourceEntity);
/* 67 */     return this;
/*    */   }
/*    */   
/*    */   public Builder blocked(Boolean blocked) {
/* 71 */     this.blocked = Optional.of(blocked);
/* 72 */     return this;
/*    */   }
/*    */   
/*    */   public Builder type(DamageSourcePredicate type) {
/* 76 */     this.type = Optional.of(type);
/* 77 */     return this;
/*    */   }
/*    */   
/*    */   public Builder type(DamageSourcePredicate.Builder type) {
/* 81 */     this.type = Optional.of(type.build());
/* 82 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 86 */   public DamagePredicate build() { return new DamagePredicate(this.dealtDamage, this.takenDamage, this.sourceEntity, this.blocked, this.type); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\DamagePredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */