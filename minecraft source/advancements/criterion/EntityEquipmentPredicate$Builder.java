/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import java.util.Optional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*  86 */   private Optional<ItemPredicate> head = Optional.empty();
/*  87 */   private Optional<ItemPredicate> chest = Optional.empty();
/*  88 */   private Optional<ItemPredicate> legs = Optional.empty();
/*  89 */   private Optional<ItemPredicate> feet = Optional.empty();
/*  90 */   private Optional<ItemPredicate> body = Optional.empty();
/*  91 */   private Optional<ItemPredicate> mainhand = Optional.empty();
/*  92 */   private Optional<ItemPredicate> offhand = Optional.empty();
/*     */ 
/*     */   
/*  95 */   public static Builder equipment() { return new Builder(); }
/*     */ 
/*     */   
/*     */   public Builder head(ItemPredicate.Builder head) {
/*  99 */     this.head = Optional.of(head.build());
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public Builder chest(ItemPredicate.Builder chest) {
/* 104 */     this.chest = Optional.of(chest.build());
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public Builder legs(ItemPredicate.Builder legs) {
/* 109 */     this.legs = Optional.of(legs.build());
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public Builder feet(ItemPredicate.Builder feet) {
/* 114 */     this.feet = Optional.of(feet.build());
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public Builder body(ItemPredicate.Builder body) {
/* 119 */     this.body = Optional.of(body.build());
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   public Builder mainhand(ItemPredicate.Builder mainhand) {
/* 124 */     this.mainhand = Optional.of(mainhand.build());
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public Builder offhand(ItemPredicate.Builder offhand) {
/* 129 */     this.offhand = Optional.of(offhand.build());
/* 130 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 134 */   public EntityEquipmentPredicate build() { return new EntityEquipmentPredicate(this.head, this.chest, this.legs, this.feet, this.body, this.mainhand, this.offhand); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\EntityEquipmentPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */