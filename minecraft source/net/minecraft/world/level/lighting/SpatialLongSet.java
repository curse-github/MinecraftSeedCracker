/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.HashCommon;
/*     */ import it.unimi.dsi.fastutil.longs.Long2LongLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
/*     */ import java.util.NoSuchElementException;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class SpatialLongSet
/*     */   extends LongLinkedOpenHashSet
/*     */ {
/*     */   private final InternalMap map;
/*     */   
/*     */   public SpatialLongSet(int expected, float f) {
/*  15 */     super(expected, f);
/*  16 */     this.map = new InternalMap(expected / 64, f);
/*     */   }
/*     */   
/*     */   protected static class InternalMap extends Long2LongLinkedOpenHashMap {
/*  20 */     private static final int X_BITS = Mth.log2(60000000);
/*  21 */     private static final int Z_BITS = Mth.log2(60000000);
/*  22 */     private static final int Y_BITS = 64 - X_BITS - Z_BITS;
/*     */     
/*     */     private static final int Y_OFFSET = 0;
/*  25 */     private static final int Z_OFFSET = Y_BITS;
/*  26 */     private static final int X_OFFSET = Y_BITS + Z_BITS;
/*  27 */     private static final long OUTER_MASK = 3L << X_OFFSET | 0x3L | 3L << Z_OFFSET;
/*     */     
/*  29 */     private int lastPos = -1;
/*     */     private long lastOuterKey;
/*     */     private final int minSize;
/*     */     
/*     */     public InternalMap(int expected, float f) {
/*  34 */       super(expected, f);
/*  35 */       this.minSize = expected;
/*     */     }
/*     */ 
/*     */     
/*  39 */     static long getOuterKey(long key) { return key & (OUTER_MASK ^ 0xFFFFFFFFFFFFFFFFL); }
/*     */ 
/*     */     
/*     */     static int getInnerKey(long key) {
/*  43 */       int innerX = (int)(key >>> X_OFFSET & 0x3L);
/*  44 */       int innerY = (int)(key >>> false & 0x3L);
/*  45 */       int innerZ = (int)(key >>> Z_OFFSET & 0x3L);
/*  46 */       return innerX << 4 | innerZ << 2 | innerY;
/*     */     }
/*     */     
/*     */     static long getFullKey(long outerKey, int innerKey) {
/*  50 */       outerKey |= (innerKey >>> 4 & 0x3) << X_OFFSET;
/*  51 */       outerKey |= (innerKey >>> 2 & 0x3) << Z_OFFSET;
/*  52 */       return (innerKey >>> 0 & 0x3) << false;
/*     */     }
/*     */     
/*     */     public boolean addBit(long key) {
/*     */       int pos;
/*  57 */       long outerKey = getOuterKey(key);
/*  58 */       int innerKey = getInnerKey(key);
/*  59 */       long bitMask = 1L << innerKey;
/*     */       
/*  61 */       if (outerKey == 0L) {
/*  62 */         if (this.containsNullKey) {
/*  63 */           return replaceBit(this.n, bitMask);
/*     */         }
/*  65 */         this.containsNullKey = true;
/*  66 */         pos = this.n;
/*     */       } else {
/*  68 */         if (this.lastPos != -1 && outerKey == this.lastOuterKey) {
/*  69 */           return replaceBit(this.lastPos, bitMask);
/*     */         }
/*  71 */         long[] keys = this.key;
/*  72 */         pos = (int)HashCommon.mix(outerKey) & this.mask;
/*  73 */         long curr = keys[pos];
/*  74 */         while (curr != 0L) {
/*  75 */           if (curr == outerKey) {
/*  76 */             this.lastPos = pos;
/*  77 */             this.lastOuterKey = outerKey;
/*  78 */             return replaceBit(pos, bitMask);
/*     */           } 
/*  80 */           pos = pos + 1 & this.mask;
/*  81 */           curr = keys[pos];
/*     */         } 
/*     */       } 
/*  84 */       this.key[pos] = outerKey;
/*  85 */       this.value[pos] = bitMask;
/*  86 */       if (this.size == 0) {
/*  87 */         this.first = this.last = pos;
/*     */         
/*  89 */         this.link[pos] = -1L;
/*     */       } else {
/*  91 */         this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  92 */         this.link[pos] = (this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL;
/*  93 */         this.last = pos;
/*     */       } 
/*  95 */       if (this.size++ >= this.maxFill) {
/*  96 */         rehash(HashCommon.arraySize(this.size + 1, this.f));
/*     */       }
/*  98 */       return false;
/*     */     }
/*     */     
/*     */     private boolean replaceBit(int pos, long bitMask) {
/* 102 */       boolean oldValue = ((this.value[pos] & bitMask) != 0L);
/* 103 */       this.value[pos] = this.value[pos] | bitMask;
/* 104 */       return oldValue;
/*     */     }
/*     */     
/*     */     public boolean removeBit(long key) {
/* 108 */       long outerKey = getOuterKey(key);
/* 109 */       int innerKey = getInnerKey(key);
/* 110 */       long bitMask = 1L << innerKey;
/* 111 */       if (outerKey == 0L) {
/* 112 */         if (this.containsNullKey) {
/* 113 */           return removeFromNullEntry(bitMask);
/*     */         }
/* 115 */         return false;
/*     */       } 
/* 117 */       if (this.lastPos != -1 && outerKey == this.lastOuterKey) {
/* 118 */         return removeFromEntry(this.lastPos, bitMask);
/*     */       }
/* 120 */       long[] keys = this.key;
/* 121 */       int pos = (int)HashCommon.mix(outerKey) & this.mask;
/* 122 */       long curr = keys[pos];
/*     */       while (true) {
/* 124 */         if (curr == 0L) {
/* 125 */           return false;
/*     */         }
/* 127 */         if (outerKey == curr) {
/* 128 */           this.lastPos = pos;
/* 129 */           this.lastOuterKey = outerKey;
/* 130 */           return removeFromEntry(pos, bitMask);
/*     */         } 
/* 132 */         pos = pos + 1 & this.mask;
/* 133 */         curr = keys[pos];
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean removeFromNullEntry(long bitMask) {
/* 138 */       if ((this.value[this.n] & bitMask) == 0L) {
/* 139 */         return false;
/*     */       }
/* 141 */       this.value[this.n] = this.value[this.n] & (bitMask ^ 0xFFFFFFFFFFFFFFFFL);
/* 142 */       if (this.value[this.n] != 0L) {
/* 143 */         return true;
/*     */       }
/* 145 */       this.containsNullKey = false;
/* 146 */       this.size--;
/* 147 */       fixPointers(this.n);
/* 148 */       if (this.size < this.maxFill / 4 && this.n > 16) {
/* 149 */         rehash(this.n / 2);
/*     */       }
/* 151 */       return true;
/*     */     }
/*     */     
/*     */     private boolean removeFromEntry(int pos, long bitMask) {
/* 155 */       if ((this.value[pos] & bitMask) == 0L) {
/* 156 */         return false;
/*     */       }
/* 158 */       this.value[pos] = this.value[pos] & (bitMask ^ 0xFFFFFFFFFFFFFFFFL);
/* 159 */       if (this.value[pos] != 0L) {
/* 160 */         return true;
/*     */       }
/* 162 */       this.lastPos = -1;
/* 163 */       this.size--;
/* 164 */       fixPointers(pos);
/* 165 */       shiftKeys(pos);
/* 166 */       if (this.size < this.maxFill / 4 && this.n > 16) {
/* 167 */         rehash(this.n / 2);
/*     */       }
/* 169 */       return true;
/*     */     }
/*     */     
/*     */     public long removeFirstBit() {
/* 173 */       if (this.size == 0) {
/* 174 */         throw new NoSuchElementException();
/*     */       }
/* 176 */       int pos = this.first;
/* 177 */       long outerKey = this.key[pos];
/* 178 */       int innerKey = Long.numberOfTrailingZeros(this.value[pos]);
/* 179 */       this.value[pos] = this.value[pos] & (1L << innerKey ^ 0xFFFFFFFFFFFFFFFFL);
/* 180 */       if (this.value[pos] == 0L) {
/* 181 */         removeFirstLong();
/* 182 */         this.lastPos = -1;
/*     */       } 
/* 184 */       return getFullKey(outerKey, innerKey);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void rehash(int newN) {
/* 189 */       if (newN > this.minSize) {
/* 190 */         super.rehash(newN);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 197 */   public boolean add(long k) { return this.map.addBit(k); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   public boolean rem(long k) { return this.map.removeBit(k); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 207 */   public long removeFirstLong() { return this.map.removeFirstBit(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 212 */   public int size() { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   public boolean isEmpty() { return this.map.isEmpty(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\lighting\SpatialLongSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */