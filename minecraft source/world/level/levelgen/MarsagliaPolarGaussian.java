/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ public class MarsagliaPolarGaussian
/*    */ {
/*    */   public final RandomSource randomSource;
/*    */   private double nextNextGaussian;
/*    */   private boolean haveNextNextGaussian;
/*    */   
/* 13 */   public MarsagliaPolarGaussian(RandomSource randomSource) { this.randomSource = randomSource; }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public void reset() { this.haveNextNextGaussian = false; }
/*    */ 
/*    */   
/*    */   public double nextGaussian() {
/*    */     double radiusSquared, y, x;
/* 22 */     if (this.haveNextNextGaussian) {
/* 23 */       this.haveNextNextGaussian = false;
/* 24 */       return this.nextNextGaussian;
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     do {
/* 33 */       x = 2.0D * this.randomSource.nextDouble() - 1.0D;
/* 34 */       y = 2.0D * this.randomSource.nextDouble() - 1.0D;
/* 35 */       radiusSquared = Mth.square(x) + Mth.square(y);
/* 36 */     } while (radiusSquared >= 1.0D || radiusSquared == 0.0D);
/*    */     
/* 38 */     double multiplier = Math.sqrt(-2.0D * Math.log(radiusSquared) / radiusSquared);
/*    */     
/* 40 */     this.nextNextGaussian = y * multiplier;
/* 41 */     this.haveNextNextGaussian = true;
/* 42 */     return x * multiplier;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\MarsagliaPolarGaussian.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */