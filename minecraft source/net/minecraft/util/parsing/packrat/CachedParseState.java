/*     */ package net.minecraft.util.parsing.packrat;
/*     */ public abstract class CachedParseState<S> extends Object implements ParseState<S> {
/*     */   private PositionCache[] positionCache;
/*     */   private final ErrorCollector<S> errorCollector;
/*     */   private final Scope scope;
/*     */   private SimpleControl[] controlCache;
/*     */   private int nextControlToReturn;
/*     */   private final Silent silent;
/*     */   
/*     */   protected CachedParseState(ErrorCollector<S> errorCollector) {
/*  11 */     this.positionCache = new PositionCache[256];
/*     */ 
/*     */ 
/*     */     
/*  15 */     this.scope = new Scope();
/*     */     
/*  17 */     this.controlCache = new SimpleControl[16];
/*     */ 
/*     */     
/*  20 */     this.silent = new Silent();
/*     */ 
/*     */     
/*  23 */     this.errorCollector = errorCollector;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  28 */   public Scope scope() { return this.scope; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  33 */   public ErrorCollector<S> errorCollector() { return this.errorCollector; }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T parse(NamedRule<S, T> rule) {
/*     */     CacheEntry<T> entry;
/*  39 */     int markBeforeParse = mark();
/*  40 */     PositionCache positionCache = getCacheForPosition(markBeforeParse);
/*     */     
/*  42 */     int entryIndex = positionCache.findKeyIndex(rule.name());
/*     */     
/*  44 */     if (entryIndex != -1) {
/*  45 */       CacheEntry<T> value = positionCache.getValue(entryIndex);
/*  46 */       if (value != null) {
/*  47 */         if (value == CacheEntry.NEGATIVE) {
/*  48 */           return null;
/*     */         }
/*  50 */         restore(value.markAfterParse);
/*  51 */         return (T)value.value;
/*     */       } 
/*     */     } else {
/*     */       
/*  55 */       entryIndex = positionCache.allocateNewEntry(rule.name());
/*     */     } 
/*     */     
/*  58 */     T result = (T)rule.value().parse(this);
/*     */ 
/*     */ 
/*     */     
/*  62 */     if (result == null) {
/*     */       
/*  64 */       entry = CacheEntry.negativeEntry();
/*     */     } else {
/*  66 */       int markAfterParse = mark();
/*  67 */       entry = new CacheEntry<T>(result, markAfterParse);
/*     */     } 
/*  69 */     positionCache.setValue(entryIndex, entry);
/*     */     
/*  71 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private PositionCache getCacheForPosition(int index) {
/*  76 */     int currentSize = this.positionCache.length;
/*  77 */     if (index >= currentSize) {
/*  78 */       int newSize = Util.growByHalf(currentSize, index + 1);
/*  79 */       PositionCache[] arrayOfPositionCache = new PositionCache[newSize];
/*  80 */       System.arraycopy(this.positionCache, 0, arrayOfPositionCache, 0, currentSize);
/*  81 */       this.positionCache = arrayOfPositionCache;
/*     */     } 
/*     */     
/*  84 */     PositionCache result = this.positionCache[index];
/*  85 */     if (result == null) {
/*  86 */       result = new PositionCache();
/*  87 */       this.positionCache[index] = result;
/*     */     } 
/*     */     
/*  90 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Control acquireControl() {
/*  95 */     int currentSize = this.controlCache.length;
/*  96 */     if (this.nextControlToReturn >= currentSize) {
/*  97 */       int newSize = Util.growByHalf(currentSize, this.nextControlToReturn + 1);
/*  98 */       SimpleControl[] arrayOfSimpleControl = new SimpleControl[newSize];
/*  99 */       System.arraycopy(this.controlCache, 0, arrayOfSimpleControl, 0, currentSize);
/* 100 */       this.controlCache = arrayOfSimpleControl;
/*     */     } 
/*     */     
/* 103 */     int controlIndex = this.nextControlToReturn++;
/* 104 */     SimpleControl entry = this.controlCache[controlIndex];
/* 105 */     if (entry == null) {
/* 106 */       entry = new SimpleControl();
/* 107 */       this.controlCache[controlIndex] = entry;
/*     */     } else {
/* 109 */       entry.reset();
/*     */     } 
/* 111 */     return entry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public void releaseControl() { this.nextControlToReturn--; }
/*     */ 
/*     */   
/*     */   private static class PositionCache
/*     */   {
/*     */     public static final int ENTRY_STRIDE = 2;
/*     */     
/*     */     private static final int NOT_FOUND = -1;
/* 124 */     private Object[] atomCache = new Object[16];
/*     */     private int nextKey;
/*     */     
/*     */     public int findKeyIndex(Atom<?> key) {
/* 128 */       for (int i = 0; i < this.nextKey; i += 2) {
/* 129 */         if (this.atomCache[i] == key) {
/* 130 */           return i;
/*     */         }
/*     */       } 
/*     */       
/* 134 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int allocateNewEntry(Atom<?> key) {
/* 141 */       int newKeyIndex = this.nextKey;
/* 142 */       this.nextKey += 2;
/*     */       
/* 144 */       int newValueIndex = newKeyIndex + 1;
/* 145 */       int currentSize = this.atomCache.length;
/* 146 */       if (newValueIndex >= currentSize) {
/* 147 */         int newSize = Util.growByHalf(currentSize, newValueIndex + 1);
/* 148 */         Object[] newCache = new Object[newSize];
/* 149 */         System.arraycopy(this.atomCache, 0, newCache, 0, currentSize);
/* 150 */         this.atomCache = newCache;
/*     */       } 
/*     */       
/* 153 */       this.atomCache[newKeyIndex] = key;
/*     */       
/* 155 */       return newKeyIndex;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 160 */     public <T> CachedParseState.CacheEntry<T> getValue(int keyIndex) { return (CachedParseState.CacheEntry)this.atomCache[keyIndex + 1]; }
/*     */ 
/*     */ 
/*     */     
/* 164 */     public void setValue(int keyIndex, CachedParseState.CacheEntry<?> entry) { this.atomCache[keyIndex + 1] = entry; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 170 */   public ParseState<S> silent() { return this.silent; }
/*     */   private static final class CacheEntry<T> extends Record { private final T value; private final int markAfterParse;
/*     */     
/* 173 */     private CacheEntry(T value, int markAfterParse) { this.value = value; this.markAfterParse = markAfterParse; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 173 */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry<TT;>; } public T value() { return (T)this.value; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 173 */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry<TT;>; } public int markAfterParse() { return this.markAfterParse; }
/*     */ 
/*     */ 
/*     */     
/* 177 */     public static final CacheEntry<?> NEGATIVE = new CacheEntry(null, -1);
/*     */ 
/*     */ 
/*     */     
/* 181 */     public static <T> CacheEntry<T> negativeEntry() { return NEGATIVE; } }
/*     */ 
/*     */   
/*     */   private class Silent
/*     */     extends Object implements ParseState<S> {
/* 186 */     private final ErrorCollector<S> silentCollector = new ErrorCollector.Nop();
/*     */ 
/*     */ 
/*     */     
/* 190 */     public ErrorCollector<S> errorCollector() { return this.silentCollector; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     public Scope scope() { return CachedParseState.this.scope(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     public <T> T parse(NamedRule<S, T> rule) { return (T)CachedParseState.this.parse(rule); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     public S input() { return (S)CachedParseState.this.input(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     public int mark() { return CachedParseState.this.mark(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     public void restore(int mark) { CachedParseState.this.restore(mark); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 220 */     public Control acquireControl() { return CachedParseState.this.acquireControl(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     public void releaseControl() { CachedParseState.this.releaseControl(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 230 */     public ParseState<S> silent() { return this; }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class SimpleControl
/*     */     implements Control
/*     */   {
/*     */     private boolean hasCut;
/*     */     
/* 239 */     public void cut() { this.hasCut = true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 244 */     public boolean hasCut() { return this.hasCut; }
/*     */ 
/*     */ 
/*     */     
/* 248 */     public void reset() { this.hasCut = false; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\CachedParseState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */