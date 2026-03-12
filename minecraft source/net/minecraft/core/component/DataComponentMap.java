/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface DataComponentMap
/*     */   extends Iterable<TypedDataComponent<?>>, DataComponentGetter
/*     */ {
/*  32 */   public static final DataComponentMap EMPTY = new DataComponentMap()
/*     */     {
/*     */       public <T> T get(DataComponentType<? extends T> type) {
/*  35 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  40 */       public Set<DataComponentType<?>> keySet() { return Set.of(); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  45 */       public Iterator<TypedDataComponent<?>> iterator() { return Collections.emptyIterator(); }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*  50 */   static Codec<DataComponentMap> makeCodec(Codec<DataComponentType<?>> componentTypeCodec) { return makeCodecFromMap(Codec.dispatchedMap(componentTypeCodec, DataComponentType::codecOrThrow)); }
/*     */ 
/*     */   
/*     */   static Codec<DataComponentMap> makeCodecFromMap(Codec<Map<DataComponentType<?>, Object>> mapCodec) {
/*  54 */     return mapCodec.flatComapMap(Builder::buildFromMapTrusted, components -> {
/*     */ 
/*     */           
/*  57 */           int size = components.size();
/*  58 */           if (size == 0) {
/*  59 */             return DataResult.success(Reference2ObjectMaps.emptyMap());
/*     */           }
/*     */           
/*  62 */           Reference2ObjectArrayMap reference2ObjectArrayMap = new Reference2ObjectArrayMap(size);
/*  63 */           for (TypedDataComponent<?> entry : components) {
/*  64 */             if (!entry.type().isTransient()) {
/*  65 */               reference2ObjectArrayMap.put(entry.type(), entry.value());
/*     */             }
/*     */           } 
/*  68 */           return DataResult.success(reference2ObjectArrayMap);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*  73 */   public static final Codec<DataComponentMap> CODEC = makeCodecFromMap(DataComponentType.VALUE_MAP_CODEC);
/*     */   
/*     */   static DataComponentMap composite(final DataComponentMap prototype, final DataComponentMap overrides) {
/*  76 */     return new DataComponentMap()
/*     */       {
/*     */         public <T> T get(DataComponentType<? extends T> type) {
/*  79 */           T value = (T)overrides.get(type);
/*  80 */           if (value != null) {
/*  81 */             return value;
/*     */           }
/*  83 */           return (T)prototype.get(type);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  88 */         public Set<DataComponentType<?>> keySet() { return Sets.union(prototype.keySet(), overrides.keySet()); }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  94 */   static Builder builder() { return new Builder(); }
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
/* 113 */   default boolean has(DataComponentType<?> type) { return (get(type) != null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   default Iterator<TypedDataComponent<?>> iterator() { return Iterators.transform(keySet().iterator(), type -> (TypedDataComponent)Objects.requireNonNull(getTyped(type))); }
/*     */ 
/*     */ 
/*     */   
/* 122 */   default Stream<TypedDataComponent<?>> stream() { return StreamSupport.stream(Spliterators.spliterator(iterator(), size(), 1345), false); }
/*     */ 
/*     */ 
/*     */   
/* 126 */   default int size() { return keySet().size(); }
/*     */ 
/*     */ 
/*     */   
/* 130 */   default boolean isEmpty() { return (size() == 0); }
/*     */ 
/*     */   
/*     */   default DataComponentMap filter(final Predicate<DataComponentType<?>> predicate) {
/* 134 */     return new DataComponentMap()
/*     */       {
/*     */         public <T> T get(DataComponentType<? extends T> type) {
/* 137 */           return (T)(predicate.test(type) ? DataComponentMap.this.get(type) : null);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 142 */         public Set<DataComponentType<?>> keySet() { Objects.requireNonNull(predicate); return Sets.filter(DataComponentMap.this.keySet(), predicate::test); }
/*     */       };
/*     */   }
/*     */   
/*     */   Set<DataComponentType<?>> keySet();
/*     */   
/* 148 */   public static class Builder { private final Reference2ObjectMap<DataComponentType<?>, Object> map = new Reference2ObjectArrayMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> Builder set(DataComponentType<T> type, T value) {
/* 154 */       setUnchecked(type, value);
/* 155 */       return this;
/*     */     }
/*     */     
/*     */     <T> void setUnchecked(DataComponentType<T> type, Object value) {
/* 159 */       if (value != null) {
/* 160 */         this.map.put(type, value);
/*     */       } else {
/* 162 */         this.map.remove(type);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Builder addAll(DataComponentMap map) {
/* 167 */       for (TypedDataComponent<?> entry : map) {
/* 168 */         this.map.put(entry.type(), entry.value());
/*     */       }
/* 170 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 174 */     public DataComponentMap build() { return buildFromMapTrusted(this.map); }
/*     */ 
/*     */     
/*     */     private static DataComponentMap buildFromMapTrusted(Map<DataComponentType<?>, Object> map) {
/* 178 */       if (map.isEmpty()) {
/* 179 */         return DataComponentMap.EMPTY;
/*     */       }
/* 181 */       if (map.size() < 8) {
/* 182 */         return new SimpleMap(new Reference2ObjectArrayMap(map));
/*     */       }
/* 184 */       return new SimpleMap(new Reference2ObjectOpenHashMap(map));
/*     */     }
/*     */     private static final class SimpleMap extends Record implements DataComponentMap { private final Reference2ObjectMap<DataComponentType<?>, Object> map;
/* 187 */       private SimpleMap(Reference2ObjectMap<DataComponentType<?>, Object> map) { this.map = map; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #187	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/* 187 */         //   0	7	0	this	Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap; } public Reference2ObjectMap<DataComponentType<?>, Object> map() { return this.map; }
/*     */       
/*     */       public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #187	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap;
/*     */         //   0	8	1	o	Ljava/lang/Object; }
/*     */       
/* 191 */       public <T> T get(DataComponentType<? extends T> type) { return (T)this.map.get(type); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 196 */       public boolean has(DataComponentType<?> type) { return this.map.containsKey(type); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 201 */       public Set<DataComponentType<?>> keySet() { return this.map.keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 206 */       public Iterator<TypedDataComponent<?>> iterator() { return Iterators.transform(Reference2ObjectMaps.fastIterator(this.map), TypedDataComponent::fromEntryUnchecked); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 211 */       public int size() { return this.map.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 216 */       public String toString() { return this.map.toString(); } } } private static final class SimpleMap extends Record implements DataComponentMap { public String toString() { return this.map.toString(); }
/*     */     
/*     */     private final Reference2ObjectMap<DataComponentType<?>, Object> map;
/*     */     
/*     */     private SimpleMap(Reference2ObjectMap<DataComponentType<?>, Object> map) { this.map = map; }
/*     */     
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
/*     */     
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
/*     */     public Reference2ObjectMap<DataComponentType<?>, Object> map() { return this.map; }
/*     */     
/*     */     public <T> T get(DataComponentType<? extends T> type) { return (T)this.map.get(type); }
/*     */     
/*     */     public boolean has(DataComponentType<?> type) { return this.map.containsKey(type); }
/*     */     
/*     */     public Set<DataComponentType<?>> keySet() { return this.map.keySet(); }
/*     */     
/*     */     public Iterator<TypedDataComponent<?>> iterator() { return Iterators.transform(Reference2ObjectMaps.fastIterator(this.map), TypedDataComponent::fromEntryUnchecked); }
/*     */     
/*     */     public int size() { return this.map.size(); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\DataComponentMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */