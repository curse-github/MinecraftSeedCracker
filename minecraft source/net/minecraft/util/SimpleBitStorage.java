/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.function.IntConsumer;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ public class SimpleBitStorage
/*     */   implements BitStorage
/*     */ {
/*     */   public static class InitializationException
/*     */     extends RuntimeException {
/*  11 */     private InitializationException(String message) { super(message); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int[] MAGIC = { 
/*  18 */       -1, -1, 0, Integer.MIN_VALUE, 0, 0, 1431655765, 1431655765, 0, Integer.MIN_VALUE, 0, 1, 858993459, 858993459, 0, 715827882, 715827882, 0, 613566756, 613566756, 0, Integer.MIN_VALUE, 0, 2, 477218588, 477218588, 0, 429496729, 429496729, 0, 390451572, 390451572, 0, 357913941, 357913941, 0, 330382099, 330382099, 0, 306783378, 306783378, 0, 286331153, 286331153, 0, Integer.MIN_VALUE, 0, 3, 252645135, 252645135, 0, 238609294, 238609294, 0, 226050910, 226050910, 0, 214748364, 214748364, 0, 204522252, 204522252, 0, 195225786, 195225786, 0, 186737708, 186737708, 0, 178956970, 178956970, 0, 171798691, 171798691, 0, 165191049, 165191049, 0, 159072862, 159072862, 0, 153391689, 153391689, 0, 148102320, 148102320, 0, 143165576, 143165576, 0, 138547332, 138547332, 0, Integer.MIN_VALUE, 0, 4, 130150524, 130150524, 0, 126322567, 126322567, 0, 122713351, 122713351, 0, 119304647, 119304647, 0, 116080197, 116080197, 0, 113025455, 113025455, 0, 110127366, 110127366, 0, 107374182, 107374182, 0, 104755299, 104755299, 0, 102261126, 102261126, 0, 99882960, 99882960, 0, 97612893, 97612893, 0, 95443717, 95443717, 0, 93368854, 93368854, 0, 91382282, 91382282, 0, 89478485, 89478485, 0, 87652393, 87652393, 0, 85899345, 85899345, 0, 84215045, 84215045, 0, 82595524, 82595524, 0, 81037118, 81037118, 0, 79536431, 79536431, 0, 78090314, 78090314, 0, 76695844, 76695844, 0, 75350303, 75350303, 0, 74051160, 74051160, 0, 72796055, 72796055, 0, 71582788, 71582788, 0, 70409299, 70409299, 0, 69273666, 69273666, 0, 68174084, 68174084, 0, Integer.MIN_VALUE, 0, 5 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final long[] data;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int bits;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final long mask;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int valuesPerLong;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int divideMul;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int divideAdd;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int divideShift;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleBitStorage(int bits, int size, int[] values) {
/*  97 */     this(bits, size);
/*     */     
/*  99 */     int outputIndex = 0;
/*     */     int inputOffset;
/* 101 */     for (inputOffset = 0; inputOffset <= size - this.valuesPerLong; inputOffset += this.valuesPerLong) {
/* 102 */       long packedValue = 0L;
/* 103 */       for (int indexInLong = this.valuesPerLong - 1; indexInLong >= 0; indexInLong--) {
/* 104 */         packedValue <<= bits;
/* 105 */         packedValue |= values[inputOffset + indexInLong] & this.mask;
/*     */       } 
/* 107 */       this.data[outputIndex++] = packedValue;
/*     */     } 
/*     */     
/* 110 */     int remainderCount = size - inputOffset;
/* 111 */     if (remainderCount > 0) {
/* 112 */       long lastPackedValue = 0L;
/* 113 */       for (int indexInLong = remainderCount - 1; indexInLong >= 0; indexInLong--) {
/* 114 */         lastPackedValue <<= bits;
/* 115 */         lastPackedValue |= values[inputOffset + indexInLong] & this.mask;
/*     */       } 
/* 117 */       this.data[outputIndex] = lastPackedValue;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 122 */   public SimpleBitStorage(int bits, int size) { this(bits, size, (long[])null); }
/*     */ 
/*     */   
/*     */   public SimpleBitStorage(int bits, int size, long[] data) {
/* 126 */     Validate.inclusiveBetween(1L, 32L, bits);
/*     */     
/* 128 */     this.size = size;
/* 129 */     this.bits = bits;
/* 130 */     this.mask = (1L << bits) - 1L;
/* 131 */     this.valuesPerLong = (char)(64 / bits);
/*     */     
/* 133 */     int row = 3 * (this.valuesPerLong - 1);
/* 134 */     this.divideMul = MAGIC[row + 0];
/* 135 */     this.divideAdd = MAGIC[row + 1];
/* 136 */     this.divideShift = MAGIC[row + 2];
/*     */     
/* 138 */     int requiredLength = (size + this.valuesPerLong - 1) / this.valuesPerLong;
/* 139 */     if (data != null) {
/* 140 */       if (data.length != requiredLength) {
/* 141 */         throw new InitializationException("Invalid length given for storage, got: " + data.length + " but expected: " + requiredLength);
/*     */       }
/* 143 */       this.data = data;
/*     */     } else {
/* 145 */       this.data = new long[requiredLength];
/*     */     } 
/*     */   }
/*     */   
/*     */   private int cellIndex(int bitIndex) {
/* 150 */     long mul = Integer.toUnsignedLong(this.divideMul);
/* 151 */     long add = Integer.toUnsignedLong(this.divideAdd);
/* 152 */     return (int)(bitIndex * mul + add >> 32 >> this.divideShift);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAndSet(int index, int value) {
/* 157 */     Validate.inclusiveBetween(0L, (this.size - 1), index);
/* 158 */     Validate.inclusiveBetween(0L, this.mask, value);
/*     */     
/* 160 */     int cellIndex = cellIndex(index);
/* 161 */     long cellValue = this.data[cellIndex];
/* 162 */     int bitIndex = (index - cellIndex * this.valuesPerLong) * this.bits;
/*     */     
/* 164 */     int oldValue = (int)(cellValue >> bitIndex & this.mask);
/* 165 */     this.data[cellIndex] = cellValue & (this.mask << bitIndex ^ 0xFFFFFFFFFFFFFFFFL) | (value & this.mask) << bitIndex;
/*     */     
/* 167 */     return oldValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(int index, int value) {
/* 172 */     Validate.inclusiveBetween(0L, (this.size - 1), index);
/* 173 */     Validate.inclusiveBetween(0L, this.mask, value);
/*     */     
/* 175 */     int cellIndex = cellIndex(index);
/* 176 */     long cellValue = this.data[cellIndex];
/* 177 */     int bitIndex = (index - cellIndex * this.valuesPerLong) * this.bits;
/*     */     
/* 179 */     this.data[cellIndex] = cellValue & (this.mask << bitIndex ^ 0xFFFFFFFFFFFFFFFFL) | (value & this.mask) << bitIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public int get(int index) {
/* 184 */     Validate.inclusiveBetween(0L, (this.size - 1), index);
/*     */     
/* 186 */     int cellIndex = cellIndex(index);
/* 187 */     long cellValue = this.data[cellIndex];
/* 188 */     int bitIndex = (index - cellIndex * this.valuesPerLong) * this.bits;
/*     */     
/* 190 */     return (int)(cellValue >> bitIndex & this.mask);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 195 */   public long[] getRaw() { return this.data; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 200 */   public int getSize() { return this.size; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 205 */   public int getBits() { return this.bits; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getAll(IntConsumer output) {
/* 210 */     int count = 0;
/* 211 */     for (long cellValue : this.data) {
/* 212 */       for (int value = 0; value < this.valuesPerLong; value++) {
/* 213 */         output.accept((int)(cellValue & this.mask));
/* 214 */         cellValue >>= this.bits;
/* 215 */         if (++count >= this.size) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void unpack(int[] output) {
/* 224 */     int dataLength = this.data.length;
/*     */     
/* 226 */     int outputOffset = 0;
/* 227 */     for (int i = 0; i < dataLength - 1; i++) {
/* 228 */       long cellValue = this.data[i];
/* 229 */       for (int indexInLong = 0; indexInLong < this.valuesPerLong; indexInLong++) {
/* 230 */         output[outputOffset + indexInLong] = (int)(cellValue & this.mask);
/* 231 */         cellValue >>= this.bits;
/*     */       } 
/* 233 */       outputOffset += this.valuesPerLong;
/*     */     } 
/*     */     
/* 236 */     int remainder = this.size - outputOffset;
/* 237 */     if (remainder > 0) {
/* 238 */       long cellValue = this.data[dataLength - 1];
/* 239 */       for (int indexInLong = 0; indexInLong < remainder; indexInLong++) {
/* 240 */         output[outputOffset + indexInLong] = (int)(cellValue & this.mask);
/* 241 */         cellValue >>= this.bits;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 248 */   public BitStorage copy() { return new SimpleBitStorage(this.bits, this.size, (long[])this.data.clone()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\SimpleBitStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */