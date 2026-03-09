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
/*     */ public class Builder
/*     */ {
/*  79 */   private Optional<Boolean> isOnGround = Optional.empty();
/*  80 */   private Optional<Boolean> isOnFire = Optional.empty();
/*  81 */   private Optional<Boolean> isCrouching = Optional.empty();
/*  82 */   private Optional<Boolean> isSprinting = Optional.empty();
/*  83 */   private Optional<Boolean> isSwimming = Optional.empty();
/*  84 */   private Optional<Boolean> isFlying = Optional.empty();
/*  85 */   private Optional<Boolean> isBaby = Optional.empty();
/*  86 */   private Optional<Boolean> isInWater = Optional.empty();
/*  87 */   private Optional<Boolean> isFallFlying = Optional.empty();
/*     */ 
/*     */   
/*  90 */   public static Builder flags() { return new Builder(); }
/*     */ 
/*     */   
/*     */   public Builder setOnGround(Boolean onGround) {
/*  94 */     this.isOnGround = Optional.of(onGround);
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setOnFire(Boolean onFire) {
/*  99 */     this.isOnFire = Optional.of(onFire);
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setCrouching(Boolean crouching) {
/* 104 */     this.isCrouching = Optional.of(crouching);
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setSprinting(Boolean sprinting) {
/* 109 */     this.isSprinting = Optional.of(sprinting);
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setSwimming(Boolean swimming) {
/* 114 */     this.isSwimming = Optional.of(swimming);
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setIsFlying(Boolean flying) {
/* 119 */     this.isFlying = Optional.of(flying);
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setIsBaby(Boolean baby) {
/* 124 */     this.isBaby = Optional.of(baby);
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setIsInWater(Boolean inWater) {
/* 129 */     this.isInWater = Optional.of(inWater);
/* 130 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setIsFallFlying(Boolean fallFlying) {
/* 134 */     this.isFallFlying = Optional.of(fallFlying);
/* 135 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 139 */   public EntityFlagsPredicate build() { return new EntityFlagsPredicate(this.isOnGround, this.isOnFire, this.isCrouching, this.isSprinting, this.isSwimming, this.isFlying, this.isBaby, this.isInWater, this.isFallFlying); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\EntityFlagsPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */