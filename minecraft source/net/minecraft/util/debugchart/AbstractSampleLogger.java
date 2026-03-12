/*    */ package net.minecraft.util.debugchart;
/*    */ 
/*    */ public abstract class AbstractSampleLogger implements SampleLogger {
/*    */   protected final long[] defaults;
/*    */   protected final long[] sample;
/*    */   
/*    */   protected AbstractSampleLogger(int dimensions, long[] defaults) {
/*  8 */     if (defaults.length != dimensions) {
/*  9 */       throw new IllegalArgumentException("defaults have incorrect length of " + defaults.length);
/*    */     }
/* 11 */     this.sample = new long[dimensions];
/* 12 */     this.defaults = defaults;
/*    */   }
/*    */ 
/*    */   
/*    */   public void logFullSample(long[] sample) {
/* 17 */     System.arraycopy(sample, 0, this.sample, 0, sample.length);
/* 18 */     useSample();
/* 19 */     resetSample();
/*    */   }
/*    */ 
/*    */   
/*    */   public void logSample(long sample) {
/* 24 */     this.sample[0] = sample;
/* 25 */     useSample();
/* 26 */     resetSample();
/*    */   }
/*    */ 
/*    */   
/*    */   public void logPartialSample(long sample, int dimension) {
/* 31 */     if (dimension < 1 || dimension >= this.sample.length) {
/* 32 */       throw new IndexOutOfBoundsException("" + dimension + " out of bounds for dimensions " + dimension);
/*    */     }
/* 34 */     this.sample[dimension] = sample;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract void useSample();
/*    */   
/* 40 */   protected void resetSample() { System.arraycopy(this.defaults, 0, this.sample, 0, this.defaults.length); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debugchart\AbstractSampleLogger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */