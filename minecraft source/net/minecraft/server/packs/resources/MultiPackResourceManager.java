/*     */ package net.minecraft.server.packs.resources;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MultiPackResourceManager
/*     */   implements CloseableResourceManager
/*     */ {
/*  21 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final Map<String, FallbackResourceManager> namespacedManagers;
/*     */   private final List<PackResources> packs;
/*     */   
/*     */   public MultiPackResourceManager(PackType type, List<PackResources> packs) {
/*  27 */     this.packs = List.copyOf(packs);
/*     */     
/*  29 */     Map<String, FallbackResourceManager> namespacedManagers = new HashMap<String, FallbackResourceManager>();
/*     */     
/*  31 */     List<String> namespaces = packs.stream().flatMap(p -> p.getNamespaces(type).stream()).distinct().toList();
/*     */     
/*  33 */     for (PackResources pack : packs) {
/*  34 */       ResourceFilterSection filterSection = getPackFilterSection(pack);
/*  35 */       Set<String> providedNamespaces = pack.getNamespaces(type);
/*     */       
/*  37 */       Predicate<Identifier> pathFilter = (filterSection != null) ? (location -> filterSection.isPathFiltered(location.getPath())) : null;
/*     */       
/*  39 */       for (String namespace : namespaces) {
/*  40 */         boolean packContainsNamespace = providedNamespaces.contains(namespace);
/*  41 */         boolean filterMatchesNamespace = (filterSection != null && filterSection.isNamespaceFiltered(namespace));
/*  42 */         if (packContainsNamespace || filterMatchesNamespace) {
/*  43 */           FallbackResourceManager fallbackResourceManager = (FallbackResourceManager)namespacedManagers.get(namespace);
/*  44 */           if (fallbackResourceManager == null) {
/*  45 */             fallbackResourceManager = new FallbackResourceManager(type, namespace);
/*  46 */             namespacedManagers.put(namespace, fallbackResourceManager);
/*     */           } 
/*     */           
/*  49 */           if (packContainsNamespace && filterMatchesNamespace) {
/*  50 */             fallbackResourceManager.push(pack, pathFilter); continue;
/*  51 */           }  if (packContainsNamespace) {
/*  52 */             fallbackResourceManager.push(pack); continue;
/*     */           } 
/*  54 */           fallbackResourceManager.pushFilterOnly(pack.packId(), pathFilter);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  59 */     this.namespacedManagers = namespacedManagers;
/*     */   }
/*     */   
/*     */   private ResourceFilterSection getPackFilterSection(PackResources pack) {
/*     */     try {
/*  64 */       return (ResourceFilterSection)pack.getMetadataSection(ResourceFilterSection.TYPE);
/*  65 */     } catch (IOException e) {
/*  66 */       LOGGER.error("Failed to get filter section from pack {}", pack.packId());
/*     */       
/*  68 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  73 */   public Set<String> getNamespaces() { return this.namespacedManagers.keySet(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<Resource> getResource(Identifier location) {
/*  78 */     ResourceManager pack = (ResourceManager)this.namespacedManagers.get(location.getNamespace());
/*     */     
/*  80 */     if (pack != null) {
/*  81 */       return pack.getResource(location);
/*     */     }
/*     */     
/*  84 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Resource> getResourceStack(Identifier location) {
/*  89 */     ResourceManager pack = (ResourceManager)this.namespacedManagers.get(location.getNamespace());
/*     */     
/*  91 */     if (pack != null) {
/*  92 */       return pack.getResourceStack(location);
/*     */     }
/*  94 */     return List.of();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Identifier, Resource> listResources(String directory, Predicate<Identifier> filter) {
/* 100 */     checkTrailingDirectoryPath(directory);
/*     */     
/* 102 */     Map<Identifier, Resource> result = new TreeMap<Identifier, Resource>();
/*     */ 
/*     */     
/* 105 */     for (FallbackResourceManager manager : this.namespacedManagers.values()) {
/* 106 */       result.putAll(manager.listResources(directory, filter));
/*     */     }
/*     */     
/* 109 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Identifier, List<Resource>> listResourceStacks(String directory, Predicate<Identifier> filter) {
/* 114 */     checkTrailingDirectoryPath(directory);
/*     */     
/* 116 */     Map<Identifier, List<Resource>> result = new TreeMap<Identifier, List<Resource>>();
/*     */ 
/*     */     
/* 119 */     for (FallbackResourceManager manager : this.namespacedManagers.values()) {
/* 120 */       result.putAll(manager.listResourceStacks(directory, filter));
/*     */     }
/*     */     
/* 123 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void checkTrailingDirectoryPath(String directory) {
/* 128 */     if (directory.endsWith("/")) {
/* 129 */       throw new IllegalArgumentException("Trailing slash in path " + directory);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public Stream<PackResources> listPacks() { return this.packs.stream(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   public void close() { this.packs.forEach(PackResources::close); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\MultiPackResourceManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */