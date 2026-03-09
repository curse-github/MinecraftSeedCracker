/*    */ package net.minecraft.advancements.criterion;
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
/* 24 */   private MinMaxBounds.Ints composite = MinMaxBounds.Ints.ANY;
/*    */ 
/*    */   
/* 27 */   public static Builder light() { return new Builder(); }
/*    */ 
/*    */   
/*    */   public Builder setComposite(MinMaxBounds.Ints composite) {
/* 31 */     this.composite = composite;
/* 32 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 36 */   public LightPredicate build() { return new LightPredicate(this.composite); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\LightPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */