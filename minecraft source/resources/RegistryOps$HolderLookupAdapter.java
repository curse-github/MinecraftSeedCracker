/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
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
/*     */ final class HolderLookupAdapter
/*     */   implements RegistryOps.RegistryInfoLookup
/*     */ {
/*     */   private final HolderLookup.Provider lookupProvider;
/*     */   private final Map<ResourceKey<? extends Registry<?>>, Optional<? extends RegistryOps.RegistryInfo<?>>> lookups;
/*     */   
/*     */   public HolderLookupAdapter(HolderLookup.Provider lookupProvider) {
/* 113 */     this.lookups = new ConcurrentHashMap();
/*     */ 
/*     */     
/* 116 */     this.lookupProvider = lookupProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public <E> Optional<RegistryOps.RegistryInfo<E>> lookup(ResourceKey<? extends Registry<? extends E>> registryKey) { return (Optional)this.lookups.computeIfAbsent(registryKey, this::createLookup); }
/*     */ 
/*     */ 
/*     */   
/* 126 */   private Optional<RegistryOps.RegistryInfo<Object>> createLookup(ResourceKey<? extends Registry<?>> key) { return this.lookupProvider.lookup(key).map(RegistryOps.RegistryInfo::fromRegistryLookup); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 131 */     if (this == obj) {
/* 132 */       return true;
/*     */     }
/* 134 */     if (obj instanceof HolderLookupAdapter) { HolderLookupAdapter adapter = (HolderLookupAdapter)obj; if (this.lookupProvider.equals(adapter.lookupProvider)); }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 139 */   public int hashCode() { return this.lookupProvider.hashCode(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\resources\RegistryOps$HolderLookupAdapter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */