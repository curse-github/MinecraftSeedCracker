/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Keyable;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.tags.TagLoader;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Registry<T>
/*     */   extends IdMap<T>, Keyable, HolderLookup.RegistryLookup<T>
/*     */ {
/*     */   default Codec<T> byNameCodec() {
/*  30 */     return referenceHolderWithLifecycle().flatComapMap(Holder.Reference::value, value -> 
/*     */         
/*  32 */         safeCastToReference(wrapAsHolder(value)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  37 */   default Codec<Holder<T>> holderByNameCodec() { return referenceHolderWithLifecycle().flatComapMap(holder -> 
/*  38 */         holder, this::safeCastToReference); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Codec<Holder.Reference<T>> referenceHolderWithLifecycle() {
/*  44 */     Codec<Holder.Reference<T>> referenceCodec = Identifier.CODEC.comapFlatMap(name -> 
/*  45 */         (DataResult)get(name).map(DataResult::success).orElseGet(()), holder -> 
/*  46 */         holder.key().identifier());
/*     */ 
/*     */     
/*  49 */     return ExtraCodecs.overrideLifecycle(referenceCodec, e -> (Lifecycle)registrationInfo(e.key()).map(RegistrationInfo::lifecycle).orElse(Lifecycle.experimental()));
/*     */   }
/*     */   
/*     */   private DataResult<Holder.Reference<T>> safeCastToReference(Holder<T> holder) {
/*  53 */     Holder.Reference<T> reference = (Holder.Reference)holder; return (holder instanceof Holder.Reference) ? DataResult.success(reference) : DataResult.error(() -> "Unregistered holder in " + String.valueOf(key()) + ": " + String.valueOf(holder));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  58 */   default <U> Stream<U> keys(DynamicOps<U> ops) { return keySet().stream().map(k -> ops.createString(k.toString())); }
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
/*  75 */   default Optional<T> getOptional(Identifier key) { return Optional.ofNullable(getValue(key)); }
/*     */ 
/*     */ 
/*     */   
/*  79 */   default Optional<T> getOptional(ResourceKey<T> key) { return Optional.ofNullable(getValue(key)); }
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
/*     */   default T getValueOrThrow(ResourceKey<T> key) {
/*  91 */     T value = (T)getValue(key);
/*  92 */     if (value == null) {
/*  93 */       throw new IllegalStateException("Missing key in " + String.valueOf(key()) + ": " + String.valueOf(key));
/*     */     }
/*  95 */     return value;
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
/* 107 */   default Stream<T> stream() { return StreamSupport.stream(spliterator(), false); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   static <T> T register(Registry<? super T> registry, String name, T value) { return (T)register(registry, Identifier.parse(name), value); }
/*     */ 
/*     */ 
/*     */   
/* 119 */   static <V, T extends V> T register(Registry<V> registry, Identifier location, T value) { return (T)register(registry, ResourceKey.create(registry.key(), location), value); }
/*     */ 
/*     */   
/*     */   static <V, T extends V> T register(Registry<V> registry, ResourceKey<V> key, T value) {
/* 123 */     ((WritableRegistry)registry).register(key, value, RegistrationInfo.BUILT_IN);
/* 124 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 129 */   static <R, T extends R> Holder.Reference<T> registerForHolder(Registry<R> registry, ResourceKey<R> key, T value) { return ((WritableRegistry)registry).register(key, value, RegistrationInfo.BUILT_IN); }
/*     */ 
/*     */ 
/*     */   
/* 133 */   static <R, T extends R> Holder.Reference<T> registerForHolder(Registry<R> registry, Identifier location, T value) { return registerForHolder(registry, ResourceKey.create(registry.key(), location), value); }
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
/* 152 */   default Iterable<Holder<T>> getTagOrEmpty(TagKey<T> id) { return (Iterable)DataFixUtils.orElse(get(id), List.of()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default IdMap<Holder<T>> asHolderIdMap() {
/* 158 */     return new IdMap<Holder<T>>()
/*     */       {
/*     */         public int getId(Holder<T> thing) {
/* 161 */           return Registry.this.getId(thing.value());
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 166 */         public Holder<T> byId(int id) { return (Holder)Registry.this.get(id).orElse(null); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 171 */         public int size() { return Registry.this.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 176 */         public Iterator<Holder<T>> iterator() { return Registry.this.listElements().map(e -> e).iterator(); }
/*     */       };
/*     */   }
/*     */   
/*     */   ResourceKey<? extends Registry<T>> key();
/*     */   
/*     */   Identifier getKey(T paramT);
/*     */   
/*     */   Optional<ResourceKey<T>> getResourceKey(T paramT);
/*     */   
/*     */   int getId(T paramT);
/*     */   
/*     */   T getValue(ResourceKey<T> paramResourceKey);
/*     */   
/*     */   T getValue(Identifier paramIdentifier);
/*     */   
/*     */   Optional<RegistrationInfo> registrationInfo(ResourceKey<T> paramResourceKey);
/*     */   
/*     */   Optional<Holder.Reference<T>> getAny();
/*     */   
/*     */   Set<Identifier> keySet();
/*     */   
/*     */   Set<Map.Entry<ResourceKey<T>, T>> entrySet();
/*     */   
/*     */   Set<ResourceKey<T>> registryKeySet();
/*     */   
/*     */   Optional<Holder.Reference<T>> getRandom(RandomSource paramRandomSource);
/*     */   
/*     */   boolean containsKey(Identifier paramIdentifier);
/*     */   
/*     */   boolean containsKey(ResourceKey<T> paramResourceKey);
/*     */   
/*     */   Registry<T> freeze();
/*     */   
/*     */   Holder.Reference<T> createIntrusiveHolder(T paramT);
/*     */   
/*     */   Optional<Holder.Reference<T>> get(int paramInt);
/*     */   
/*     */   Optional<Holder.Reference<T>> get(Identifier paramIdentifier);
/*     */   
/*     */   Holder<T> wrapAsHolder(T paramT);
/*     */   
/*     */   Stream<HolderSet.Named<T>> getTags();
/*     */   
/*     */   PendingTags<T> prepareTagReload(TagLoader.LoadResult<T> paramLoadResult);
/*     */   
/*     */   public static interface PendingTags<T> {
/*     */     ResourceKey<? extends Registry<? extends T>> key();
/*     */     
/*     */     HolderLookup.RegistryLookup<T> lookup();
/*     */     
/*     */     void apply();
/*     */     
/*     */     int size();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\Registry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */