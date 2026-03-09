/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class TickThrottler {
/*    */   private final int incrementStep;
/*    */   private final int threshold;
/*    */   private int count;
/*    */   
/*    */   public TickThrottler(int incrementStep, int threshold) {
/*  9 */     this.incrementStep = incrementStep;
/* 10 */     this.threshold = threshold;
/*    */   }
/*    */ 
/*    */   
/* 14 */   public void increment() { this.count += this.incrementStep; }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 18 */     if (this.count > 0) {
/* 19 */       this.count--;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 24 */   public boolean isUnderThreshold() { return (this.count < this.threshold); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\TickThrottler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */