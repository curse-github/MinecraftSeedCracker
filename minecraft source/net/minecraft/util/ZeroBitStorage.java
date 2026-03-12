/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.function.IntConsumer;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class ZeroBitStorage
/*    */   implements BitStorage {
/*  9 */   public static final long[] RAW = new long[0];
/*    */   
/*    */   private final int size;
/*    */ 
/*    */   
/* 14 */   public ZeroBitStorage(int size) { this.size = size; }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getAndSet(int index, int value) {
/* 19 */     Validate.inclusiveBetween(0L, (this.size - 1), index);
/* 20 */     Validate.inclusiveBetween(0L, 0L, value);
/* 21 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(int index, int value) {
/* 26 */     Validate.inclusiveBetween(0L, (this.size - 1), index);
/* 27 */     Validate.inclusiveBetween(0L, 0L, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public int get(int index) {
/* 32 */     Validate.inclusiveBetween(0L, (this.size - 1), index);
/* 33 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public long[] getRaw() { return RAW; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public int getSize() { return this.size; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public int getBits() { return 0; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void getAll(IntConsumer output) {
/* 53 */     for (int i = 0; i < this.size; i++) {
/* 54 */       output.accept(0);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public void unpack(int[] output) { Arrays.fill(output, 0, this.size, 0); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   public BitStorage copy() { return this; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\ZeroBitStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */