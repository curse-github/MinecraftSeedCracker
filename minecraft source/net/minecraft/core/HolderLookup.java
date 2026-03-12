/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.flag.FeatureElement;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface HolderLookup<T>
/*     */   extends HolderGetter<T>
/*     */ {
/*     */   Stream<Holder.Reference<T>> listElements();
/*     */   
/*  25 */   default Stream<ResourceKey<T>> listElementIds() { return listElements().map(Holder.Reference::key); }
/*     */ 
/*     */   
/*     */   Stream<HolderSet.Named<T>> listTags();
/*     */ 
/*     */   
/*  31 */   default Stream<TagKey<T>> listTagIds() { return listTags().map(HolderSet.Named::key); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface RegistryLookup<T>
/*     */     extends HolderLookup<T>, HolderOwner<T>
/*     */   {
/*     */     default RegistryLookup<T> filterFeatures(FeatureFlagSet enabledFeatures) {
/*  43 */       if (FeatureElement.FILTERED_REGISTRIES.contains(key())) {
/*  44 */         return filterElements(t -> ((FeatureElement)t).isEnabled(enabledFeatures));
/*     */       }
/*     */       
/*  47 */       return this;
/*     */     }
/*     */     
/*     */     default RegistryLookup<T> filterElements(final Predicate<T> filter) {
/*  51 */       return new Delegate<T>()
/*     */         {
/*     */           public HolderLookup.RegistryLookup<T> parent() {
/*  54 */             return HolderLookup.RegistryLookup.this;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*  59 */           public Optional<Holder.Reference<T>> get(ResourceKey<T> id) { return parent().get(id).filter(holder -> filter.test(holder.value())); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  64 */           public Stream<Holder.Reference<T>> listElements() { return parent().listElements().filter(e -> filter.test(e.value())); }
/*     */         };
/*     */     }
/*     */     
/*     */     ResourceKey<? extends Registry<? extends T>> key();
/*     */     
/*     */     Lifecycle registryLifecycle();
/*     */     
/*     */     public static interface Delegate<T> extends RegistryLookup<T> { HolderLookup.RegistryLookup<T> parent();
/*     */       
/*  74 */       default ResourceKey<? extends Registry<? extends T>> key() { return parent().key(); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  79 */       default Lifecycle registryLifecycle() { return parent().registryLifecycle(); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  84 */       default Optional<Holder.Reference<T>> get(ResourceKey<T> id) { return parent().get(id); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  89 */       default Stream<Holder.Reference<T>> listElements() { return parent().listElements(); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  94 */       default Optional<HolderSet.Named<T>> get(TagKey<T> id) { return parent().get(id); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  99 */       default Stream<HolderSet.Named<T>> listTags() { return parent().listTags(); } } } class null extends Object implements RegistryLookup.Delegate<T> { public HolderLookup.RegistryLookup<T> parent() { return HolderLookup.RegistryLookup.this; } public Optional<Holder.Reference<T>> get(ResourceKey<T> id) { return parent().get(id).filter(holder -> filter.test(holder.value())); } public Stream<Holder.Reference<T>> listElements() { return parent().listElements().filter(e -> filter.test(e.value())); } } public static interface Delegate<T> extends RegistryLookup<T> { HolderLookup.RegistryLookup<T> parent(); default Stream<HolderSet.Named<T>> listTags() { return parent().listTags(); }
/*     */     default ResourceKey<? extends Registry<? extends T>> key() { return parent().key(); }
/*     */     default Lifecycle registryLifecycle() { return parent().registryLifecycle(); }
/*     */     default Optional<Holder.Reference<T>> get(ResourceKey<T> id) { return parent().get(id); }
/*     */     
/*     */     default Stream<Holder.Reference<T>> listElements() { return parent().listElements(); }
/*     */     
/*     */     default Optional<HolderSet.Named<T>> get(TagKey<T> id) { return parent().get(id); } }
/*     */   
/* 108 */   public static interface Provider extends HolderGetter.Provider { default Stream<HolderLookup.RegistryLookup<?>> listRegistries() { return listRegistryKeys().map(this::lookupOrThrow); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     default <T> HolderLookup.RegistryLookup<T> lookupOrThrow(ResourceKey<? extends Registry<? extends T>> key) {
/* 116 */       return (HolderLookup.RegistryLookup)lookup(key).orElseThrow(() -> new IllegalStateException("Registry " + String.valueOf(key.identifier()) + " not found"));
/*     */     }
/*     */ 
/*     */     
/* 120 */     default <V> RegistryOps<V> createSerializationContext(DynamicOps<V> parent) { return RegistryOps.create(parent, this); }
/*     */ 
/*     */     
/*     */     static Provider create(Stream<HolderLookup.RegistryLookup<?>> lookups) {
/* 124 */       final Map<ResourceKey<? extends Registry<?>>, HolderLookup.RegistryLookup<?>> map = (Map)lookups.collect(Collectors.toUnmodifiableMap(HolderLookup.RegistryLookup::key, e -> e));
/* 125 */       return new Provider()
/*     */         {
/*     */           public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
/* 128 */             return map.keySet().stream();
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 134 */           public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) { return Optional.ofNullable((HolderLookup.RegistryLookup)map.get(key)); }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     default Lifecycle allRegistriesLifecycle() { return (Lifecycle)listRegistries().map(HolderLookup.RegistryLookup::registryLifecycle).reduce(Lifecycle.stable(), Lifecycle::add); }
/*     */     
/*     */     Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys();
/*     */     
/*     */     <T> Optional<? extends HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> param1ResourceKey); }
/*     */ 
/*     */   
/*     */   class null implements Provider {
/*     */     public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() { return map.keySet().stream(); }
/*     */     
/*     */     public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) { return Optional.ofNullable((HolderLookup.RegistryLookup)map.get(key)); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\HolderLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */