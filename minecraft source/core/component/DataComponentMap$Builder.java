/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.google.common.collect.Iterators;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class Builder
/*     */ {
/* 148 */   private final Reference2ObjectMap<DataComponentType<?>, Object> map = new Reference2ObjectArrayMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Builder set(DataComponentType<T> type, T value) {
/* 154 */     setUnchecked(type, value);
/* 155 */     return this;
/*     */   }
/*     */   
/*     */   <T> void setUnchecked(DataComponentType<T> type, Object value) {
/* 159 */     if (value != null) {
/* 160 */       this.map.put(type, value);
/*     */     } else {
/* 162 */       this.map.remove(type);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Builder addAll(DataComponentMap map) {
/* 167 */     for (TypedDataComponent<?> entry : map) {
/* 168 */       this.map.put(entry.type(), entry.value());
/*     */     }
/* 170 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 174 */   public DataComponentMap build() { return buildFromMapTrusted(this.map); }
/*     */ 
/*     */   
/*     */   private static DataComponentMap buildFromMapTrusted(Map<DataComponentType<?>, Object> map) {
/* 178 */     if (map.isEmpty()) {
/* 179 */       return DataComponentMap.EMPTY;
/*     */     }
/* 181 */     if (map.size() < 8) {
/* 182 */       return new SimpleMap(new Reference2ObjectArrayMap(map));
/*     */     }
/* 184 */     return new SimpleMap(new Reference2ObjectOpenHashMap(map));
/*     */   }
/*     */   private static final class SimpleMap extends Record implements DataComponentMap { private final Reference2ObjectMap<DataComponentType<?>, Object> map;
/* 187 */     private SimpleMap(Reference2ObjectMap<DataComponentType<?>, Object> map) { this.map = map; } public Reference2ObjectMap<DataComponentType<?>, Object> map() { return this.map; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap;
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/*     */     
/* 191 */     public <T> T get(DataComponentType<? extends T> type) { return (T)this.map.get(type); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 196 */     public boolean has(DataComponentType<?> type) { return this.map.containsKey(type); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     public Set<DataComponentType<?>> keySet() { return this.map.keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 206 */     public Iterator<TypedDataComponent<?>> iterator() { return Iterators.transform(Reference2ObjectMaps.fastIterator(this.map), TypedDataComponent::fromEntryUnchecked); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     public int size() { return this.map.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     public String toString() { return this.map.toString(); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\DataComponentMap$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */