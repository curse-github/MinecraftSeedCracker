/*    */ package net.minecraft.util.debugchart;
/*    */ 
/*    */ public class LocalSampleLogger
/*    */   extends AbstractSampleLogger implements SampleStorage {
/*    */   public static final int CAPACITY = 240;
/*    */   private final long[][] samples;
/*    */   private int start;
/*    */   private int size;
/*    */   
/* 10 */   public LocalSampleLogger(int dimensions) { this(dimensions, new long[dimensions]); }
/*    */ 
/*    */   
/*    */   public LocalSampleLogger(int dimensions, long[] defaults) {
/* 14 */     super(dimensions, defaults);
/* 15 */     this.samples = new long[240][dimensions];
/*    */   }
/*    */ 
/*    */   
/*    */   protected void useSample() {
/* 20 */     int nextIndex = wrapIndex(this.start + this.size);
/* 21 */     System.arraycopy(this.sample, 0, this.samples[nextIndex], 0, this.sample.length);
/* 22 */     if (this.size < 240) {
/* 23 */       this.size++;
/*    */     } else {
/* 25 */       this.start = wrapIndex(this.start + 1);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public int capacity() { return this.samples.length; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public int size() { return this.size; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public long get(int index) { return get(index, 0); }
/*    */ 
/*    */ 
/*    */   
/*    */   public long get(int index, int dimension) {
/* 46 */     if (index < 0 || index >= this.size) {
/* 47 */       throw new IndexOutOfBoundsException("" + index + " out of bounds for length " + index);
/*    */     }
/* 49 */     long[] sampleArray = this.samples[wrapIndex(this.start + index)];
/* 50 */     if (dimension < 0 || dimension >= sampleArray.length) {
/* 51 */       throw new IndexOutOfBoundsException("" + dimension + " out of bounds for dimensions " + dimension);
/*    */     }
/* 53 */     return sampleArray[dimension];
/*    */   }
/*    */ 
/*    */   
/* 57 */   private int wrapIndex(int index) { return index % 240; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void reset() {
/* 62 */     this.start = 0;
/* 63 */     this.size = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debugchart\LocalSampleLogger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */