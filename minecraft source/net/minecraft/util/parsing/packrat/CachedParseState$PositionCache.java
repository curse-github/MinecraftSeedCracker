/*     */ package net.minecraft.util.parsing.packrat;
/*     */ 
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PositionCache
/*     */ {
/*     */   public static final int ENTRY_STRIDE = 2;
/*     */   private static final int NOT_FOUND = -1;
/* 124 */   private Object[] atomCache = new Object[16];
/*     */   private int nextKey;
/*     */   
/*     */   public int findKeyIndex(Atom<?> key) {
/* 128 */     for (int i = 0; i < this.nextKey; i += 2) {
/* 129 */       if (this.atomCache[i] == key) {
/* 130 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 134 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int allocateNewEntry(Atom<?> key) {
/* 141 */     int newKeyIndex = this.nextKey;
/* 142 */     this.nextKey += 2;
/*     */     
/* 144 */     int newValueIndex = newKeyIndex + 1;
/* 145 */     int currentSize = this.atomCache.length;
/* 146 */     if (newValueIndex >= currentSize) {
/* 147 */       int newSize = Util.growByHalf(currentSize, newValueIndex + 1);
/* 148 */       Object[] newCache = new Object[newSize];
/* 149 */       System.arraycopy(this.atomCache, 0, newCache, 0, currentSize);
/* 150 */       this.atomCache = newCache;
/*     */     } 
/*     */     
/* 153 */     this.atomCache[newKeyIndex] = key;
/*     */     
/* 155 */     return newKeyIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 160 */   public <T> CachedParseState.CacheEntry<T> getValue(int keyIndex) { return (CachedParseState.CacheEntry)this.atomCache[keyIndex + 1]; }
/*     */ 
/*     */ 
/*     */   
/* 164 */   public void setValue(int keyIndex, CachedParseState.CacheEntry<?> entry) { this.atomCache[keyIndex + 1] = entry; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\CachedParseState$PositionCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */