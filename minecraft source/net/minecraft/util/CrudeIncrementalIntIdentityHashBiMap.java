/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.collect.Iterators;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.core.IdMap;
/*     */ 
/*     */ public class CrudeIncrementalIntIdentityHashBiMap<K>
/*     */   extends Object
/*     */   implements IdMap<K> {
/*     */   private static final int NOT_FOUND = -1;
/*  13 */   private static final Object EMPTY_SLOT = null;
/*     */   
/*     */   private static final float LOADFACTOR = 0.8F;
/*     */   
/*     */   private K[] keys;
/*     */   
/*     */   private int[] values;
/*     */   private K[] byId;
/*     */   private int nextId;
/*     */   private int size;
/*     */   
/*     */   private CrudeIncrementalIntIdentityHashBiMap(int capacity) {
/*  25 */     this.keys = new Object[capacity];
/*  26 */     this.values = new int[capacity];
/*  27 */     this.byId = new Object[capacity];
/*     */   }
/*     */   
/*     */   private CrudeIncrementalIntIdentityHashBiMap(K[] keys, int[] values, K[] byId, int nextId, int size) {
/*  31 */     this.keys = keys;
/*  32 */     this.values = values;
/*  33 */     this.byId = byId;
/*  34 */     this.nextId = nextId;
/*  35 */     this.size = size;
/*     */   }
/*     */ 
/*     */   
/*  39 */   public static <A> CrudeIncrementalIntIdentityHashBiMap<A> create(int initialCapacity) { return new CrudeIncrementalIntIdentityHashBiMap((int)(initialCapacity / 0.8F)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   public int getId(K thing) { return getValue(indexOf(thing, hash(thing))); }
/*     */ 
/*     */ 
/*     */   
/*     */   public K byId(int id) {
/*  49 */     if (id < 0 || id >= this.byId.length) {
/*  50 */       return null;
/*     */     }
/*     */     
/*  53 */     return (K)this.byId[id];
/*     */   }
/*     */   
/*     */   private int getValue(int index) {
/*  57 */     if (index == -1) {
/*  58 */       return -1;
/*     */     }
/*  60 */     return this.values[index];
/*     */   }
/*     */ 
/*     */   
/*  64 */   public boolean contains(K key) { return (getId(key) != -1); }
/*     */ 
/*     */ 
/*     */   
/*  68 */   public boolean contains(int id) { return (byId(id) != null); }
/*     */ 
/*     */   
/*     */   public int add(K key) {
/*  72 */     int value = nextId();
/*     */     
/*  74 */     addMapping(key, value);
/*     */     
/*  76 */     return value;
/*     */   }
/*     */   
/*     */   private int nextId() {
/*  80 */     while (this.nextId < this.byId.length && this.byId[this.nextId] != null) {
/*  81 */       this.nextId++;
/*     */     }
/*  83 */     return this.nextId;
/*     */   }
/*     */ 
/*     */   
/*     */   private void grow(int newSize) {
/*  88 */     K[] oldKeys = (K[])this.keys;
/*  89 */     int[] oldValues = this.values;
/*     */     
/*  91 */     CrudeIncrementalIntIdentityHashBiMap<K> resized = new CrudeIncrementalIntIdentityHashBiMap<K>(newSize);
/*  92 */     for (int i = 0; i < oldKeys.length; i++) {
/*  93 */       if (oldKeys[i] != null) {
/*  94 */         resized.addMapping(oldKeys[i], oldValues[i]);
/*     */       }
/*     */     } 
/*     */     
/*  98 */     this.keys = resized.keys;
/*  99 */     this.values = resized.values;
/* 100 */     this.byId = resized.byId;
/* 101 */     this.nextId = resized.nextId;
/* 102 */     this.size = resized.size;
/*     */   }
/*     */   
/*     */   public void addMapping(K key, int id) {
/* 106 */     int minSize = Math.max(id, this.size + 1);
/* 107 */     if (minSize >= this.keys.length * 0.8F) {
/* 108 */       int newSize = this.keys.length << 1;
/* 109 */       while (newSize < id) {
/* 110 */         newSize <<= 1;
/*     */       }
/* 112 */       grow(newSize);
/*     */     } 
/*     */     
/* 115 */     int index = findEmpty(hash(key));
/* 116 */     this.keys[index] = key;
/* 117 */     this.values[index] = id;
/* 118 */     this.byId[id] = key;
/* 119 */     this.size++;
/*     */     
/* 121 */     if (id == this.nextId) {
/* 122 */       this.nextId++;
/*     */     }
/*     */   }
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
/* 138 */   private int hash(K key) { return (Mth.murmurHash3Mixer(System.identityHashCode(key)) & 0x7FFFFFFF) % this.keys.length; }
/*     */ 
/*     */   
/*     */   private int indexOf(K key, int startFrom) {
/* 142 */     for (int i = startFrom; i < this.keys.length; i++) {
/* 143 */       if (this.keys[i] == key) {
/* 144 */         return i;
/*     */       }
/* 146 */       if (this.keys[i] == EMPTY_SLOT) {
/* 147 */         return -1;
/*     */       }
/*     */     } 
/*     */     
/* 151 */     for (int i = 0; i < startFrom; i++) {
/* 152 */       if (this.keys[i] == key) {
/* 153 */         return i;
/*     */       }
/* 155 */       if (this.keys[i] == EMPTY_SLOT) {
/* 156 */         return -1;
/*     */       }
/*     */     } 
/*     */     
/* 160 */     return -1;
/*     */   }
/*     */   
/*     */   private int findEmpty(int startFrom) {
/* 164 */     for (int i = startFrom; i < this.keys.length; i++) {
/* 165 */       if (this.keys[i] == EMPTY_SLOT) {
/* 166 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 170 */     for (int i = 0; i < startFrom; i++) {
/* 171 */       if (this.keys[i] == EMPTY_SLOT) {
/* 172 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 176 */     throw new RuntimeException("Overflowed :(");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 181 */   public Iterator<K> iterator() { return Iterators.filter(Iterators.forArray(this.byId), Predicates.notNull()); }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 185 */     Arrays.fill(this.keys, null);
/* 186 */     Arrays.fill(this.byId, null);
/* 187 */     this.nextId = 0;
/* 188 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 193 */   public int size() { return this.size; }
/*     */ 
/*     */   
/*     */   public CrudeIncrementalIntIdentityHashBiMap<K> copy() {
/* 197 */     return new CrudeIncrementalIntIdentityHashBiMap((Object[])this.keys
/* 198 */         .clone(), (int[])this.values
/* 199 */         .clone(), (Object[])this.byId
/* 200 */         .clone(), this.nextId, this.size);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\CrudeIncrementalIntIdentityHashBiMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */