/*     */ package net.minecraft.util.parsing.packrat;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Scope
/*     */ {
/*     */   private static final int NOT_FOUND = -1;
/*     */   
/*  18 */   private static final Object FRAME_START_MARKER = new Object()
/*     */     {
/*     */       public String toString() {
/*  21 */         return "frame"; }
/*     */     };
/*     */   private static final int ENTRY_STRIDE = 2;
/*     */   private Object[] stack;
/*     */   
/*     */   public Scope() {
/*  27 */     this.stack = new Object[128];
/*  28 */     this.topEntryKeyIndex = 0;
/*  29 */     this.topMarkerKeyIndex = 0;
/*     */ 
/*     */ 
/*     */     
/*  33 */     this.stack[0] = FRAME_START_MARKER;
/*     */     
/*  35 */     this.stack[1] = null;
/*     */   }
/*     */   private int topEntryKeyIndex; private int topMarkerKeyIndex;
/*     */   private int valueIndex(Atom<?> atom) {
/*  39 */     for (int i = this.topEntryKeyIndex; i > this.topMarkerKeyIndex; i -= 2) {
/*  40 */       Object key = this.stack[i];
/*  41 */       assert key instanceof Atom;
/*  42 */       if (key == atom) {
/*  43 */         return i + 1;
/*     */       }
/*     */     } 
/*  46 */     return -1;
/*     */   }
/*     */   
/*     */   public int valueIndexForAny(Atom... atoms) {
/*  50 */     for (int i = this.topEntryKeyIndex; i > this.topMarkerKeyIndex; i -= 2) {
/*  51 */       Object key = this.stack[i];
/*  52 */       assert key instanceof Atom;
/*  53 */       for (Atom<?> atom : atoms) {
/*  54 */         if (atom == key) {
/*  55 */           return i + 1;
/*     */         }
/*     */       } 
/*     */     } 
/*  59 */     return -1;
/*     */   }
/*     */   
/*     */   private void ensureCapacity(int additionalEntryCount) {
/*  63 */     int currentSize = this.stack.length;
/*  64 */     int currentLastValueIndex = this.topEntryKeyIndex + 1;
/*  65 */     int newLastValueIndex = currentLastValueIndex + additionalEntryCount * 2;
/*     */     
/*  67 */     if (newLastValueIndex >= currentSize) {
/*  68 */       int newSize = Util.growByHalf(currentSize, newLastValueIndex + 1);
/*  69 */       Object[] newStack = new Object[newSize];
/*  70 */       System.arraycopy(this.stack, 0, newStack, 0, currentSize);
/*  71 */       this.stack = newStack;
/*     */     } 
/*  73 */     assert validateStructure();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setupNewFrame() {
/*  82 */     this.topEntryKeyIndex += 2;
/*  83 */     this.stack[this.topEntryKeyIndex] = FRAME_START_MARKER;
/*  84 */     this.stack[this.topEntryKeyIndex + 1] = Integer.valueOf(this.topMarkerKeyIndex);
/*  85 */     this.topMarkerKeyIndex = this.topEntryKeyIndex;
/*     */   }
/*     */   
/*     */   public void pushFrame() {
/*  89 */     ensureCapacity(1);
/*  90 */     setupNewFrame();
/*  91 */     assert validateStructure();
/*     */   }
/*     */ 
/*     */   
/*  95 */   private int getPreviousMarkerIndex(int markerKeyIndex) { return ((Integer)this.stack[markerKeyIndex + 1]).intValue(); }
/*     */ 
/*     */   
/*     */   public void popFrame() {
/*  99 */     assert this.topMarkerKeyIndex != 0;
/*     */ 
/*     */ 
/*     */     
/* 103 */     this.topEntryKeyIndex = this.topMarkerKeyIndex - 2;
/* 104 */     this.topMarkerKeyIndex = getPreviousMarkerIndex(this.topMarkerKeyIndex);
/* 105 */     assert validateStructure();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void splitFrame() {
/* 113 */     int currentFrameMarkerIndex = this.topMarkerKeyIndex;
/* 114 */     int nonMarkerEntriesInFrame = (this.topEntryKeyIndex - this.topMarkerKeyIndex) / 2;
/* 115 */     ensureCapacity(nonMarkerEntriesInFrame + 1);
/*     */     
/* 117 */     setupNewFrame();
/*     */     
/* 119 */     int sourceCursor = currentFrameMarkerIndex + 2;
/* 120 */     int targetCursor = this.topEntryKeyIndex;
/* 121 */     for (int i = 0; i < nonMarkerEntriesInFrame; i++) {
/* 122 */       targetCursor += 2;
/* 123 */       Object key = this.stack[sourceCursor];
/* 124 */       assert key != null;
/* 125 */       this.stack[targetCursor] = key;
/* 126 */       this.stack[targetCursor + 1] = null;
/* 127 */       sourceCursor += 2;
/*     */     } 
/* 129 */     this.topEntryKeyIndex = targetCursor;
/* 130 */     assert validateStructure();
/*     */   }
/*     */   
/*     */   public void clearFrameValues() {
/* 134 */     for (int i = this.topEntryKeyIndex; i > this.topMarkerKeyIndex; i -= 2) {
/* 135 */       assert this.stack[i] instanceof Atom;
/* 136 */       this.stack[i + 1] = null;
/*     */     } 
/* 138 */     assert validateStructure();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mergeFrame() {
/* 146 */     int previousMarkerIndex = getPreviousMarkerIndex(this.topMarkerKeyIndex);
/* 147 */     int previousFrameCursor = previousMarkerIndex;
/* 148 */     int currentFrameCursor = this.topMarkerKeyIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 154 */     while (currentFrameCursor < this.topEntryKeyIndex) {
/* 155 */       previousFrameCursor += 2;
/* 156 */       currentFrameCursor += 2;
/* 157 */       Object newKey = this.stack[currentFrameCursor];
/* 158 */       assert newKey instanceof Atom;
/* 159 */       Object newValue = this.stack[currentFrameCursor + 1];
/*     */       
/* 161 */       Object oldKey = this.stack[previousFrameCursor];
/* 162 */       if (oldKey != newKey) {
/*     */         
/* 164 */         this.stack[previousFrameCursor] = newKey;
/* 165 */         this.stack[previousFrameCursor + 1] = newValue; continue;
/* 166 */       }  if (newValue != null) {
/* 167 */         this.stack[previousFrameCursor + 1] = newValue;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 172 */     this.topEntryKeyIndex = previousFrameCursor;
/* 173 */     this.topMarkerKeyIndex = previousMarkerIndex;
/* 174 */     assert validateStructure();
/*     */   }
/*     */   
/*     */   public <T> void put(Atom<T> name, T value) {
/* 178 */     int valueIndex = valueIndex(name);
/* 179 */     if (valueIndex != -1) {
/* 180 */       this.stack[valueIndex] = value;
/*     */     } else {
/* 182 */       ensureCapacity(1);
/* 183 */       this.topEntryKeyIndex += 2;
/* 184 */       this.stack[this.topEntryKeyIndex] = name;
/* 185 */       this.stack[this.topEntryKeyIndex + 1] = value;
/*     */     } 
/* 187 */     assert validateStructure();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(Atom<T> name) {
/* 192 */     int valueIndex = valueIndex(name);
/* 193 */     return (T)((valueIndex != -1) ? this.stack[valueIndex] : null);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getOrThrow(Atom<T> name) {
/* 198 */     int valueIndex = valueIndex(name);
/* 199 */     if (valueIndex == -1) {
/* 200 */       throw new IllegalArgumentException("No value for atom " + String.valueOf(name));
/*     */     }
/* 202 */     return (T)this.stack[valueIndex];
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getOrDefault(Atom<T> name, T fallback) {
/* 207 */     int valueIndex = valueIndex(name);
/* 208 */     return (T)((valueIndex != -1) ? this.stack[valueIndex] : fallback);
/*     */   }
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public final <T> T getAny(Atom... names) {
/* 214 */     int valueIndex = valueIndexForAny(names);
/* 215 */     return (T)((valueIndex != -1) ? this.stack[valueIndex] : null);
/*     */   }
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public final <T> T getAnyOrThrow(Atom... names) {
/* 221 */     int valueIndex = valueIndexForAny(names);
/* 222 */     if (valueIndex == -1) {
/* 223 */       throw new IllegalArgumentException("No value for atoms " + Arrays.toString(names));
/*     */     }
/* 225 */     return (T)this.stack[valueIndex];
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 230 */     StringBuilder result = new StringBuilder();
/* 231 */     boolean afterFrame = true;
/* 232 */     for (int i = 0; i <= this.topEntryKeyIndex; i += 2) {
/* 233 */       Object key = this.stack[i];
/* 234 */       Object value = this.stack[i + 1];
/* 235 */       if (key == FRAME_START_MARKER) {
/* 236 */         result.append('|');
/* 237 */         afterFrame = true;
/*     */       } else {
/* 239 */         if (!afterFrame) {
/* 240 */           result.append(',');
/*     */         }
/* 242 */         afterFrame = false;
/* 243 */         result.append(key).append(':').append(value);
/*     */       } 
/*     */     } 
/* 246 */     return result.toString();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public Map<Atom<?>, ?> lastFrame() {
/* 251 */     HashMap<Atom<?>, Object> result = new HashMap<Atom<?>, Object>();
/*     */ 
/*     */     
/* 254 */     for (int i = this.topEntryKeyIndex; i > this.topMarkerKeyIndex; i -= 2) {
/* 255 */       Object key = this.stack[i];
/* 256 */       Object value = this.stack[i + 1];
/* 257 */       result.put((Atom)key, value);
/*     */     } 
/* 259 */     return result;
/*     */   }
/*     */   
/*     */   public boolean hasOnlySingleFrame() {
/* 263 */     for (int i = this.topEntryKeyIndex; i > 0; i--) {
/* 264 */       if (this.stack[i] == FRAME_START_MARKER) {
/* 265 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 269 */     if (this.stack[false] != FRAME_START_MARKER) {
/* 270 */       throw new IllegalStateException("Corrupted stack");
/*     */     }
/* 272 */     return true;
/*     */   }
/*     */   
/*     */   private boolean validateStructure() {
/* 276 */     assert this.topMarkerKeyIndex >= 0;
/* 277 */     assert this.topEntryKeyIndex >= this.topMarkerKeyIndex;
/*     */     
/* 279 */     for (int i = 0; i <= this.topEntryKeyIndex; i += 2) {
/* 280 */       Object key = this.stack[i];
/* 281 */       if (key != FRAME_START_MARKER && !(key instanceof Atom)) {
/* 282 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 286 */     int marker = this.topMarkerKeyIndex;
/* 287 */     while (marker != 0) {
/* 288 */       Object key = this.stack[marker];
/* 289 */       if (key != FRAME_START_MARKER) {
/* 290 */         return false;
/*     */       }
/* 292 */       marker = getPreviousMarkerIndex(marker);
/*     */     } 
/*     */     
/* 295 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\Scope.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */