/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Unit;
/*     */ 
/*     */ public final class DataComponentPatch
/*     */ {
/*  23 */   public static final DataComponentPatch EMPTY = new DataComponentPatch(Reference2ObjectMaps.emptyMap());
/*     */   
/*  25 */   public static final Codec<DataComponentPatch> CODEC = Codec.dispatchedMap(PatchKey.CODEC, PatchKey::valueCodec).xmap(data -> {
/*     */         
/*  27 */         if (data.isEmpty()) {
/*  28 */           return EMPTY;
/*     */         }
/*  30 */         Reference2ObjectArrayMap reference2ObjectArrayMap = new Reference2ObjectArrayMap(data.size());
/*  31 */         for (Map.Entry<PatchKey, ?> entry : data.entrySet()) {
/*  32 */           PatchKey key = (PatchKey)entry.getKey();
/*  33 */           if (key.removed()) {
/*  34 */             reference2ObjectArrayMap.put(key.type(), Optional.empty()); continue;
/*     */           } 
/*  36 */           reference2ObjectArrayMap.put(key.type(), Optional.of(entry.getValue()));
/*     */         } 
/*     */         
/*  39 */         return new DataComponentPatch(reference2ObjectArrayMap);
/*     */       }patch -> {
/*     */         
/*  42 */         Reference2ObjectArrayMap reference2ObjectArrayMap = new Reference2ObjectArrayMap(patch.map.size());
/*  43 */         for (ObjectIterator objectIterator = Reference2ObjectMaps.fastIterable(patch.map).iterator(); objectIterator.hasNext(); ) { Map.Entry<DataComponentType<?>, Optional<?>> entry = (Map.Entry)objectIterator.next();
/*  44 */           DataComponentType<?> type = (DataComponentType)entry.getKey();
/*  45 */           if (type.isTransient()) {
/*     */             continue;
/*     */           }
/*  48 */           Optional<?> value = (Optional)entry.getValue();
/*  49 */           if (value.isPresent()) {
/*  50 */             reference2ObjectArrayMap.put(new PatchKey(type, false), value.get()); continue;
/*     */           } 
/*  52 */           reference2ObjectArrayMap.put(new PatchKey(type, true), Unit.INSTANCE); }
/*     */ 
/*     */         
/*  55 */         return reference2ObjectArrayMap;
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   public static final StreamCodec<RegistryFriendlyByteBuf, DataComponentPatch> STREAM_CODEC = createStreamCodec(new CodecGetter()
/*     */       {
/*     */         public <T> StreamCodec<RegistryFriendlyByteBuf, T> apply(DataComponentType<T> type) {
/*  67 */           return type.streamCodec().cast();
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static final StreamCodec<RegistryFriendlyByteBuf, DataComponentPatch> DELIMITED_STREAM_CODEC = createStreamCodec(new CodecGetter()
/*     */       {
/*     */         public <T> StreamCodec<RegistryFriendlyByteBuf, T> apply(DataComponentType<T> type) {
/*  78 */           StreamCodec<RegistryFriendlyByteBuf, T> original = type.streamCodec().cast();
/*  79 */           return original.apply(ByteBufCodecs.registryFriendlyLengthPrefixed(2147483647));
/*     */         }
/*     */       });
/*     */   private static final String REMOVED_PREFIX = "!"; final Reference2ObjectMap<DataComponentType<?>, Optional<?>> map;
/*     */   private static StreamCodec<RegistryFriendlyByteBuf, DataComponentPatch> createStreamCodec(final CodecGetter codecGetter) {
/*  84 */     return new StreamCodec<RegistryFriendlyByteBuf, DataComponentPatch>()
/*     */       {
/*     */         public DataComponentPatch decode(RegistryFriendlyByteBuf input) {
/*  87 */           int positiveCount = input.readVarInt();
/*  88 */           int negativeCount = input.readVarInt();
/*  89 */           if (positiveCount == 0 && negativeCount == 0) {
/*  90 */             return DataComponentPatch.EMPTY;
/*     */           }
/*     */           
/*  93 */           int expectedSize = positiveCount + negativeCount;
/*  94 */           Reference2ObjectArrayMap reference2ObjectArrayMap = new Reference2ObjectArrayMap(Math.min(expectedSize, 65536));
/*  95 */           for (int i = 0; i < positiveCount; i++) {
/*  96 */             DataComponentType<?> type = (DataComponentType)DataComponentType.STREAM_CODEC.decode(input);
/*  97 */             Object value = codecGetter.apply(type).decode(input);
/*  98 */             reference2ObjectArrayMap.put(type, Optional.of(value));
/*     */           } 
/*     */           
/* 101 */           for (int i = 0; i < negativeCount; i++) {
/* 102 */             DataComponentType<?> type = (DataComponentType)DataComponentType.STREAM_CODEC.decode(input);
/* 103 */             reference2ObjectArrayMap.put(type, Optional.empty());
/*     */           } 
/*     */           
/* 106 */           return new DataComponentPatch(reference2ObjectArrayMap);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(RegistryFriendlyByteBuf output, DataComponentPatch patch) {
/* 111 */           if (patch.isEmpty()) {
/* 112 */             output.writeVarInt(0);
/* 113 */             output.writeVarInt(0);
/*     */             
/*     */             return;
/*     */           } 
/* 117 */           int positiveCount = 0;
/* 118 */           int negativeCount = 0; ObjectIterator objectIterator;
/* 119 */           for (objectIterator = Reference2ObjectMaps.fastIterable(patch.map).iterator(); objectIterator.hasNext(); ) { Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = (Reference2ObjectMap.Entry)objectIterator.next();
/* 120 */             if (((Optional)entry.getValue()).isPresent()) {
/* 121 */               positiveCount++; continue;
/*     */             } 
/* 123 */             negativeCount++; }
/*     */ 
/*     */ 
/*     */           
/* 127 */           output.writeVarInt(positiveCount);
/* 128 */           output.writeVarInt(negativeCount);
/* 129 */           for (objectIterator = Reference2ObjectMaps.fastIterable(patch.map).iterator(); objectIterator.hasNext(); ) { Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = (Reference2ObjectMap.Entry)objectIterator.next();
/* 130 */             Optional<?> value = (Optional)entry.getValue();
/* 131 */             if (value.isPresent()) {
/* 132 */               DataComponentType<?> type = (DataComponentType)entry.getKey();
/* 133 */               DataComponentType.STREAM_CODEC.encode(output, type);
/* 134 */               encodeComponent(output, type, value.get());
/*     */             }  }
/*     */ 
/*     */           
/* 138 */           for (objectIterator = Reference2ObjectMaps.fastIterable(patch.map).iterator(); objectIterator.hasNext(); ) { Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = (Reference2ObjectMap.Entry)objectIterator.next();
/* 139 */             if (((Optional)entry.getValue()).isEmpty()) {
/* 140 */               DataComponentType<?> type = (DataComponentType)entry.getKey();
/* 141 */               DataComponentType.STREAM_CODEC.encode(output, type);
/*     */             }  }
/*     */         
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 148 */         private <T> void encodeComponent(RegistryFriendlyByteBuf output, DataComponentType<T> type, Object value) { codecGetter.apply(type).encode(output, value); }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   DataComponentPatch(Reference2ObjectMap<DataComponentType<?>, Optional<?>> map) { this.map = map; }
/*     */ 
/*     */ 
/*     */   
/* 162 */   public static Builder builder() { return new Builder(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   public <T> Optional<? extends T> get(DataComponentType<? extends T> type) { return (Optional)this.map.get(type); }
/*     */ 
/*     */ 
/*     */   
/* 171 */   public Set<Map.Entry<DataComponentType<?>, Optional<?>>> entrySet() { return this.map.entrySet(); }
/*     */ 
/*     */ 
/*     */   
/* 175 */   public int size() { return this.map.size(); }
/*     */ 
/*     */   
/*     */   public DataComponentPatch forget(Predicate<DataComponentType<?>> test) {
/* 179 */     if (isEmpty()) {
/* 180 */       return EMPTY;
/*     */     }
/*     */     
/* 183 */     Reference2ObjectArrayMap reference2ObjectArrayMap = new Reference2ObjectArrayMap(this.map);
/* 184 */     reference2ObjectArrayMap.keySet().removeIf(test);
/*     */     
/* 186 */     if (reference2ObjectArrayMap.isEmpty()) {
/* 187 */       return EMPTY;
/*     */     }
/* 189 */     return new DataComponentPatch(reference2ObjectArrayMap);
/*     */   }
/*     */ 
/*     */   
/* 193 */   public boolean isEmpty() { return this.map.isEmpty(); }
/*     */   public static final class SplitResult extends Record { private final DataComponentMap added; private final Set<DataComponentType<?>> removed;
/*     */     
/* 196 */     public SplitResult(DataComponentMap added, Set<DataComponentType<?>> removed) { this.added = added; this.removed = removed; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/DataComponentPatch$SplitResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #196	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 196 */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentPatch$SplitResult; } public DataComponentMap added() { return this.added; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentPatch$SplitResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #196	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentPatch$SplitResult; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentPatch$SplitResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #196	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/component/DataComponentPatch$SplitResult;
/* 196 */       //   0	8	1	o	Ljava/lang/Object; } public Set<DataComponentType<?>> removed() { return this.removed; }
/* 197 */     public static final SplitResult EMPTY = new SplitResult(DataComponentMap.EMPTY, Set.of()); }
/*     */ 
/*     */   
/*     */   public SplitResult split() {
/* 201 */     if (isEmpty()) {
/* 202 */       return SplitResult.EMPTY;
/*     */     }
/*     */     
/* 205 */     DataComponentMap.Builder added = DataComponentMap.builder();
/* 206 */     Set<DataComponentType<?>> removed = Sets.newIdentityHashSet();
/*     */     
/* 208 */     this.map.forEach((type, optionalValue) -> {
/* 209 */           if (optionalValue.isPresent()) {
/* 210 */             added.setUnchecked(type, optionalValue.get());
/*     */           } else {
/* 212 */             removed.add(type);
/*     */           } 
/*     */         });
/*     */     
/* 216 */     return new SplitResult(added.build(), removed);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 221 */     if (this == obj) {
/* 222 */       return true;
/*     */     }
/* 224 */     if (obj instanceof DataComponentPatch) { DataComponentPatch patch = (DataComponentPatch)obj; if (this.map.equals(patch.map)); }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 229 */   public int hashCode() { return this.map.hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 234 */   public String toString() { return toString(this.map); }
/*     */ 
/*     */   
/*     */   static String toString(Reference2ObjectMap<DataComponentType<?>, Optional<?>> map) {
/* 238 */     StringBuilder builder = new StringBuilder();
/* 239 */     builder.append('{');
/* 240 */     boolean first = true;
/* 241 */     for (ObjectIterator objectIterator = Reference2ObjectMaps.fastIterable(map).iterator(); objectIterator.hasNext(); ) { Map.Entry<DataComponentType<?>, Optional<?>> entry = (Map.Entry)objectIterator.next();
/* 242 */       if (first) {
/* 243 */         first = false;
/*     */       } else {
/* 245 */         builder.append(", ");
/*     */       } 
/* 247 */       Optional<?> value = (Optional)entry.getValue();
/* 248 */       if (value.isPresent()) {
/* 249 */         builder.append(entry.getKey());
/* 250 */         builder.append("=>");
/* 251 */         builder.append(value.get()); continue;
/*     */       } 
/* 253 */       builder.append("!");
/* 254 */       builder.append(entry.getKey()); }
/*     */ 
/*     */     
/* 257 */     builder.append('}');
/* 258 */     return builder.toString();
/*     */   }
/*     */   private static final class PatchKey extends Record { private final DataComponentType<?> type; private final boolean removed;
/* 261 */     private PatchKey(DataComponentType<?> type, boolean removed) { this.type = type; this.removed = removed; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/DataComponentPatch$PatchKey;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #261	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentPatch$PatchKey; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentPatch$PatchKey;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #261	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentPatch$PatchKey; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentPatch$PatchKey;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #261	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/component/DataComponentPatch$PatchKey;
/* 261 */       //   0	8	1	o	Ljava/lang/Object; } public DataComponentType<?> type() { return this.type; } public boolean removed() { return this.removed; }
/* 262 */     public static final Codec<PatchKey> CODEC = Codec.STRING.flatXmap(string -> {
/*     */           
/* 264 */           boolean removed = string.startsWith("!");
/* 265 */           if (removed) {
/* 266 */             string = string.substring("!".length());
/*     */           }
/* 268 */           Identifier id = Identifier.tryParse(string);
/* 269 */           DataComponentType<?> type = (DataComponentType)BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(id);
/* 270 */           if (type == null) {
/* 271 */             return DataResult.error(());
/*     */           }
/* 273 */           if (type.isTransient()) {
/* 274 */             return DataResult.error(());
/*     */           }
/* 276 */           return DataResult.success(new PatchKey(type, removed));
/*     */         }key -> {
/*     */           
/* 279 */           DataComponentType<?> type = key.type();
/* 280 */           Identifier id = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type);
/* 281 */           if (id == null) {
/* 282 */             return DataResult.error(());
/*     */           }
/* 284 */           return DataResult.success(key.removed() ? ("!" + String.valueOf(id)) : id.toString());
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 289 */     public Codec<?> valueCodec() { return this.removed ? Codec.EMPTY.codec() : this.type.codecOrThrow(); } }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/* 294 */     private final Reference2ObjectMap<DataComponentType<?>, Optional<?>> map = new Reference2ObjectArrayMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> Builder set(DataComponentType<T> type, T value) {
/* 300 */       this.map.put(type, Optional.of(value));
/* 301 */       return this;
/*     */     }
/*     */     
/*     */     public <T> Builder remove(DataComponentType<T> type) {
/* 305 */       this.map.put(type, Optional.empty());
/* 306 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 310 */     public <T> Builder set(TypedDataComponent<T> component) { return set(component.type(), component.value()); }
/*     */ 
/*     */     
/*     */     public DataComponentPatch build() {
/* 314 */       if (this.map.isEmpty()) {
/* 315 */         return DataComponentPatch.EMPTY;
/*     */       }
/* 317 */       return new DataComponentPatch(this.map);
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface CodecGetter {
/*     */     <T> StreamCodec<? super RegistryFriendlyByteBuf, T> apply(DataComponentType<T> param1DataComponentType);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\DataComponentPatch.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */