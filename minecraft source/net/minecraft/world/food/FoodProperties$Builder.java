/*    */ package net.minecraft.world.food;
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
/*    */   private int nutrition;
/*    */   private float saturationModifier;
/*    */   private boolean canAlwaysEat;
/*    */   
/*    */   public Builder nutrition(int nutrition) {
/* 56 */     this.nutrition = nutrition;
/* 57 */     return this;
/*    */   }
/*    */   
/*    */   public Builder saturationModifier(float saturationModifier) {
/* 61 */     this.saturationModifier = saturationModifier;
/* 62 */     return this;
/*    */   }
/*    */   
/*    */   public Builder alwaysEdible() {
/* 66 */     this.canAlwaysEat = true;
/* 67 */     return this;
/*    */   }
/*    */   
/*    */   public FoodProperties build() {
/* 71 */     float saturation = FoodConstants.saturationByModifier(this.nutrition, this.saturationModifier);
/* 72 */     return new FoodProperties(this.nutrition, saturation, this.canAlwaysEat);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\food\FoodProperties$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */