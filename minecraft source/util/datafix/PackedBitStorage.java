/*    */ package net.minecraft.util.datafix;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import org.apache.commons.lang3.Validate;
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
/*    */ public class PackedBitStorage
/*    */ {
/*    */   private static final int BIT_TO_LONG_SHIFT = 6;
/*    */   private final long[] data;
/*    */   private final int bits;
/*    */   private final long mask;
/*    */   private final int size;
/*    */   
/* 26 */   public PackedBitStorage(int bits, int size) { this(bits, size, new long[Mth.roundToward(size * bits, 64) / 64]); }
/*    */ 
/*    */   
/*    */   public PackedBitStorage(int bits, int size, long[] data) {
/* 30 */     Validate.inclusiveBetween(1L, 32L, bits);
/*    */     
/* 32 */     this.size = size;
/* 33 */     this.bits = bits;
/* 34 */     this.data = data;
/* 35 */     this.mask = (1L << bits) - 1L;
/*    */     
/* 37 */     int requiredLength = Mth.roundToward(size * bits, 64) / 64;
/* 38 */     if (data.length != requiredLength) {
/* 39 */       throw new IllegalArgumentException("Invalid length given for storage, got: " + data.length + " but expected: " + requiredLength);
/*    */     }
/*    */   }
/*    */   
/*    */   public void set(int index, int value) {
/* 44 */     Validate.inclusiveBetween(0L, (this.size - 1), index);
/* 45 */     Validate.inclusiveBetween(0L, this.mask, value);
/*    */     
/* 47 */     int position = index * this.bits;
/* 48 */     int startData = position >> 6;
/* 49 */     int endData = (index + 1) * this.bits - 1 >> 6;
/* 50 */     int startBit = position ^ startData << 6;
/*    */     
/* 52 */     this.data[startData] = this.data[startData] & (this.mask << startBit ^ 0xFFFFFFFFFFFFFFFFL) | (value & this.mask) << startBit;
/* 53 */     if (startData != endData) {
/* 54 */       int shiftBits = 64 - startBit;
/* 55 */       int wantedBits = this.bits - shiftBits;
/* 56 */       this.data[endData] = this.data[endData] >>> wantedBits << wantedBits | (value & this.mask) >> shiftBits;
/*    */     } 
/*    */   }
/*    */   
/*    */   public int get(int index) {
/* 61 */     Validate.inclusiveBetween(0L, (this.size - 1), index);
/*    */     
/* 63 */     int position = index * this.bits;
/* 64 */     int startData = position >> 6;
/* 65 */     int endData = (index + 1) * this.bits - 1 >> 6;
/* 66 */     int startBit = position ^ startData << 6;
/*    */     
/* 68 */     if (startData == endData) {
/* 69 */       return (int)(this.data[startData] >>> startBit & this.mask);
/*    */     }
/* 71 */     int shiftBits = 64 - startBit;
/* 72 */     return (int)((this.data[startData] >>> startBit | this.data[endData] << shiftBits) & this.mask);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 77 */   public long[] getRaw() { return this.data; }
/*    */ 
/*    */ 
/*    */   
/* 81 */   public int getBits() { return this.bits; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\PackedBitStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */