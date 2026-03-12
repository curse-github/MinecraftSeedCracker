/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
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
/*     */ public final class PatchedDataComponentMap
/*     */   implements DataComponentMap
/*     */ {
/*     */   private final DataComponentMap prototype;
/*     */   private Reference2ObjectMap<DataComponentType<?>, Optional<?>> patch;
/*     */   private boolean copyOnWrite;
/*     */   
/*  33 */   public PatchedDataComponentMap(DataComponentMap prototype) { this(prototype, Reference2ObjectMaps.emptyMap(), true); }
/*     */ 
/*     */   
/*     */   private PatchedDataComponentMap(DataComponentMap prototype, Reference2ObjectMap<DataComponentType<?>, Optional<?>> patch, boolean copyOnWrite) {
/*  37 */     this.prototype = prototype;
/*  38 */     this.patch = patch;
/*  39 */     this.copyOnWrite = copyOnWrite;
/*     */   }
/*     */   
/*     */   public static PatchedDataComponentMap fromPatch(DataComponentMap prototype, DataComponentPatch patch) {
/*  43 */     if (isPatchSanitized(prototype, patch.map))
/*     */     {
/*  45 */       return new PatchedDataComponentMap(prototype, patch.map, true);
/*     */     }
/*     */     
/*  48 */     PatchedDataComponentMap map = new PatchedDataComponentMap(prototype);
/*  49 */     map.applyPatch(patch);
/*  50 */     return map;
/*     */   }
/*     */   
/*     */   private static boolean isPatchSanitized(DataComponentMap prototype, Reference2ObjectMap<DataComponentType<?>, Optional<?>> patch) {
/*  54 */     for (ObjectIterator objectIterator = Reference2ObjectMaps.fastIterable(patch).iterator(); objectIterator.hasNext(); ) { Map.Entry<DataComponentType<?>, Optional<?>> entry = (Map.Entry)objectIterator.next();
/*  55 */       Object defaultValue = prototype.get((DataComponentType)entry.getKey());
/*  56 */       Optional<?> value = (Optional)entry.getValue();
/*  57 */       if (value.isPresent() && value.get().equals(defaultValue))
/*  58 */         return false; 
/*  59 */       if (value.isEmpty() && defaultValue == null) {
/*  60 */         return false;
/*     */       } }
/*     */     
/*  63 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/*  72 */     Optional<? extends T> value = (Optional)this.patch.get(type);
/*  73 */     if (value != null) {
/*  74 */       return (T)value.orElse(null);
/*     */     }
/*  76 */     return (T)this.prototype.get(type);
/*     */   }
/*     */ 
/*     */   
/*  80 */   public boolean hasNonDefault(DataComponentType<?> type) { return this.patch.containsKey(type); }
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
/*     */   public <T> T set(DataComponentType<T> type, T value) {
/*     */     Optional<T> lastValue;
/*  97 */     ensureMapOwnership();
/*  98 */     T defaultValue = (T)this.prototype.get(type);
/*     */     
/* 100 */     if (Objects.equals(value, defaultValue)) {
/* 101 */       lastValue = (Optional)this.patch.remove(type);
/*     */     } else {
/* 103 */       lastValue = (Optional)this.patch.put(type, Optional.ofNullable(value));
/*     */     } 
/* 105 */     if (lastValue != null) {
/* 106 */       return (T)lastValue.orElse(defaultValue);
/*     */     }
/* 108 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   
/* 112 */   public <T> T set(TypedDataComponent<T> value) { return (T)set(value.type(), value.value()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T remove(DataComponentType<? extends T> type) {
/*     */     Optional<? extends T> lastValue;
/* 124 */     ensureMapOwnership();
/* 125 */     T defaultValue = (T)this.prototype.get(type);
/*     */     
/* 127 */     if (defaultValue != null) {
/* 128 */       lastValue = (Optional)this.patch.put(type, Optional.empty());
/*     */     } else {
/* 130 */       lastValue = (Optional)this.patch.remove(type);
/*     */     } 
/* 132 */     if (lastValue != null) {
/* 133 */       return (T)lastValue.orElse(null);
/*     */     }
/* 135 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public void applyPatch(DataComponentPatch patch) {
/* 139 */     ensureMapOwnership();
/* 140 */     for (ObjectIterator objectIterator = Reference2ObjectMaps.fastIterable(patch.map).iterator(); objectIterator.hasNext(); ) { Map.Entry<DataComponentType<?>, Optional<?>> entry = (Map.Entry)objectIterator.next();
/* 141 */       applyPatch((DataComponentType)entry.getKey(), (Optional)entry.getValue()); }
/*     */   
/*     */   }
/*     */   
/*     */   private void applyPatch(DataComponentType<?> type, Optional<?> value) {
/* 146 */     Object defaultValue = this.prototype.get(type);
/* 147 */     if (value.isPresent()) {
/* 148 */       if (value.get().equals(defaultValue)) {
/* 149 */         this.patch.remove(type);
/*     */       } else {
/* 151 */         this.patch.put(type, value);
/*     */       }
/*     */     
/* 154 */     } else if (defaultValue != null) {
/* 155 */       this.patch.put(type, Optional.empty());
/*     */     } else {
/* 157 */       this.patch.remove(type);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void restorePatch(DataComponentPatch patch) {
/* 163 */     ensureMapOwnership();
/* 164 */     this.patch.clear();
/* 165 */     this.patch.putAll(patch.map);
/*     */   }
/*     */   
/*     */   public void clearPatch() {
/* 169 */     ensureMapOwnership();
/* 170 */     this.patch.clear();
/*     */   }
/*     */   
/*     */   public void setAll(DataComponentMap components) {
/* 174 */     for (TypedDataComponent<?> entry : components) {
/* 175 */       entry.applyTo(this);
/*     */     }
/*     */   }
/*     */   
/*     */   private void ensureMapOwnership() {
/* 180 */     if (this.copyOnWrite) {
/* 181 */       this.patch = new Reference2ObjectArrayMap(this.patch);
/* 182 */       this.copyOnWrite = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<DataComponentType<?>> keySet() {
/* 191 */     if (this.patch.isEmpty()) {
/* 192 */       return this.prototype.keySet();
/*     */     }
/* 194 */     ReferenceArraySet referenceArraySet = new ReferenceArraySet(this.prototype.keySet());
/* 195 */     for (ObjectIterator objectIterator = Reference2ObjectMaps.fastIterable(this.patch).iterator(); objectIterator.hasNext(); ) { Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = (Reference2ObjectMap.Entry)objectIterator.next();
/* 196 */       Optional<?> value = (Optional)entry.getValue();
/* 197 */       if (value.isPresent()) {
/* 198 */         referenceArraySet.add((DataComponentType)entry.getKey()); continue;
/*     */       } 
/* 200 */       referenceArraySet.remove(entry.getKey()); }
/*     */ 
/*     */     
/* 203 */     return referenceArraySet;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<TypedDataComponent<?>> iterator() {
/* 208 */     if (this.patch.isEmpty()) {
/* 209 */       return this.prototype.iterator();
/*     */     }
/* 211 */     List<TypedDataComponent<?>> components = new ArrayList<TypedDataComponent<?>>(this.patch.size() + this.prototype.size());
/* 212 */     for (ObjectIterator objectIterator = Reference2ObjectMaps.fastIterable(this.patch).iterator(); objectIterator.hasNext(); ) { Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = (Reference2ObjectMap.Entry)objectIterator.next();
/* 213 */       if (((Optional)entry.getValue()).isPresent()) {
/* 214 */         components.add(TypedDataComponent.createUnchecked((DataComponentType)entry.getKey(), ((Optional)entry.getValue()).get()));
/*     */       } }
/*     */     
/* 217 */     for (TypedDataComponent<?> component : this.prototype) {
/* 218 */       if (!this.patch.containsKey(component.type())) {
/* 219 */         components.add(component);
/*     */       }
/*     */     } 
/* 222 */     return components.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 227 */     int size = this.prototype.size();
/* 228 */     for (ObjectIterator objectIterator = Reference2ObjectMaps.fastIterable(this.patch).iterator(); objectIterator.hasNext(); ) { Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = (Reference2ObjectMap.Entry)objectIterator.next();
/* 229 */       boolean inPatch = ((Optional)entry.getValue()).isPresent();
/* 230 */       boolean inPrototype = this.prototype.has((DataComponentType)entry.getKey());
/* 231 */       if (inPatch != inPrototype) {
/* 232 */         size += (inPatch ? 1 : -1);
/*     */       } }
/*     */     
/* 235 */     return size;
/*     */   }
/*     */   
/*     */   public DataComponentPatch asPatch() {
/* 239 */     if (this.patch.isEmpty()) {
/* 240 */       return DataComponentPatch.EMPTY;
/*     */     }
/* 242 */     this.copyOnWrite = true;
/* 243 */     return new DataComponentPatch(this.patch);
/*     */   }
/*     */   
/*     */   public PatchedDataComponentMap copy() {
/* 247 */     this.copyOnWrite = true;
/* 248 */     return new PatchedDataComponentMap(this.prototype, this.patch, true);
/*     */   }
/*     */   
/*     */   public DataComponentMap toImmutableMap() {
/* 252 */     if (this.patch.isEmpty()) {
/* 253 */       return this.prototype;
/*     */     }
/* 255 */     return copy();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 260 */     if (this == obj) {
/* 261 */       return true;
/*     */     }
/* 263 */     if (obj instanceof PatchedDataComponentMap) { PatchedDataComponentMap otherMap = (PatchedDataComponentMap)obj; if (this.prototype.equals(otherMap.prototype) && this.patch.equals(otherMap.patch)); }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 268 */   public int hashCode() { return this.prototype.hashCode() + this.patch.hashCode() * 31; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 273 */   public String toString() { return "{" + (String)stream().map(TypedDataComponent::toString).collect(Collectors.joining(", ")) + "}"; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\PatchedDataComponentMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */