/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Provider
/*     */   extends HolderGetter.Provider
/*     */ {
/* 108 */   default Stream<HolderLookup.RegistryLookup<?>> listRegistries() { return listRegistryKeys().map(this::lookupOrThrow); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default <T> HolderLookup.RegistryLookup<T> lookupOrThrow(ResourceKey<? extends Registry<? extends T>> key) {
/* 116 */     return (HolderLookup.RegistryLookup)lookup(key).orElseThrow(() -> new IllegalStateException("Registry " + String.valueOf(key.identifier()) + " not found"));
/*     */   }
/*     */ 
/*     */   
/* 120 */   default <V> RegistryOps<V> createSerializationContext(DynamicOps<V> parent) { return RegistryOps.create(parent, this); }
/*     */ 
/*     */   
/*     */   static Provider create(Stream<HolderLookup.RegistryLookup<?>> lookups) {
/* 124 */     final Map<ResourceKey<? extends Registry<?>>, HolderLookup.RegistryLookup<?>> map = (Map)lookups.collect(Collectors.toUnmodifiableMap(HolderLookup.RegistryLookup::key, e -> e));
/* 125 */     return new Provider()
/*     */       {
/*     */         public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
/* 128 */           return map.keySet().stream();
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 134 */         public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) { return Optional.ofNullable((HolderLookup.RegistryLookup)map.get(key)); }
/*     */       };
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
/* 146 */   default Lifecycle allRegistriesLifecycle() { return (Lifecycle)listRegistries().map(HolderLookup.RegistryLookup::registryLifecycle).reduce(Lifecycle.stable(), Lifecycle::add); }
/*     */   
/*     */   Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys();
/*     */   
/*     */   <T> Optional<? extends HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> paramResourceKey);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\HolderLookup$Provider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */