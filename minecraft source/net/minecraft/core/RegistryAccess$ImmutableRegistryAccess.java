/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ImmutableRegistryAccess
/*    */   implements RegistryAccess
/*    */ {
/*    */   private final Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>> registries;
/*    */   
/* 57 */   public ImmutableRegistryAccess(List<? extends Registry<?>> registries) { this.registries = (Map)registries.stream().collect(Collectors.toUnmodifiableMap(Registry::key, v -> v)); }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public ImmutableRegistryAccess(Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>> registries) { this.registries = Map.copyOf(registries); }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public ImmutableRegistryAccess(Stream<RegistryAccess.RegistryEntry<?>> entries) { this.registries = (Map)entries.collect(ImmutableMap.toImmutableMap(RegistryAccess.RegistryEntry::key, RegistryAccess.RegistryEntry::value)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public <E> Optional<Registry<E>> lookup(ResourceKey<? extends Registry<? extends E>> registryKey) { return Optional.ofNullable((Registry)this.registries.get(registryKey)).map(r -> r); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 76 */   public Stream<RegistryAccess.RegistryEntry<?>> registries() { return this.registries.entrySet().stream().map(RegistryAccess.RegistryEntry::fromMapEntry); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\RegistryAccess$ImmutableRegistryAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */