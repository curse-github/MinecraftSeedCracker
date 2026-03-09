/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class BinaryAnimator
/*    */ {
/*    */   private final int animationLength;
/*    */   private final EasingType easing;
/*    */   private int ticks;
/*    */   private int ticksOld;
/*    */   
/*    */   public BinaryAnimator(int animationLength, EasingType easing) {
/* 11 */     this.animationLength = animationLength;
/* 12 */     this.easing = easing;
/*    */   }
/*    */ 
/*    */   
/* 16 */   public BinaryAnimator(int animationLength) { this(animationLength, EasingType.LINEAR); }
/*    */ 
/*    */   
/*    */   public void tick(boolean active) {
/* 20 */     this.ticksOld = this.ticks;
/* 21 */     if (active) {
/* 22 */       if (this.ticks < this.animationLength) {
/* 23 */         this.ticks++;
/*    */       }
/*    */     }
/* 26 */     else if (this.ticks > 0) {
/* 27 */       this.ticks--;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFactor(float partialTicks) {
/* 33 */     float factor = Mth.lerp(partialTicks, this.ticksOld, this.ticks) / this.animationLength;
/* 34 */     return this.easing.apply(factor);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\BinaryAnimator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */