/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*     */ import net.minecraft.server.packs.resources.IoSupplier;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class VanillaPackResources implements PackResources {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final PackLocationInfo location;
/*     */   
/*     */   private final BuiltInMetadata metadata;
/*     */   
/*     */   private final Set<String> namespaces;
/*     */   private final List<Path> rootPaths;
/*     */   private final Map<PackType, List<Path>> pathsForType;
/*     */   
/*     */   VanillaPackResources(PackLocationInfo location, BuiltInMetadata metadata, Set<String> namespaces, List<Path> rootPaths, Map<PackType, List<Path>> pathsForType) {
/*  36 */     this.location = location;
/*  37 */     this.metadata = metadata;
/*  38 */     this.namespaces = namespaces;
/*  39 */     this.rootPaths = rootPaths;
/*  40 */     this.pathsForType = pathsForType;
/*     */   }
/*     */ 
/*     */   
/*     */   public IoSupplier<InputStream> getRootResource(String... path) {
/*  45 */     FileUtil.validatePath(path);
/*     */     
/*  47 */     List<String> pathList = List.of(path);
/*  48 */     for (Path rootPath : this.rootPaths) {
/*  49 */       Path pathInRoot = FileUtil.resolvePath(rootPath, pathList);
/*  50 */       if (Files.exists(pathInRoot, new java.nio.file.LinkOption[0]) && PathPackResources.validatePath(pathInRoot)) {
/*  51 */         return IoSupplier.create(pathInRoot);
/*     */       }
/*     */     } 
/*  54 */     return null;
/*     */   }
/*     */   
/*     */   public void listRawPaths(PackType type, Identifier resource, Consumer<Path> output) {
/*  58 */     FileUtil.decomposePath(resource.getPath())
/*  59 */       .ifSuccess(decomposedPath -> {
/*     */           
/*  61 */           String namespace = resource.getNamespace();
/*  62 */           for (Path typePath : (List)this.pathsForType.get(type)) {
/*  63 */             Path namespacedPath = typePath.resolve(namespace);
/*  64 */             output.accept(FileUtil.resolvePath(namespacedPath, decomposedPath));
/*     */           }
/*     */ 
/*     */         
/*  68 */         }).ifError(error -> 
/*  69 */         LOGGER.error("Invalid path {}: {}", resource, error.message()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void listResources(PackType type, String namespace, String directory, PackResources.ResourceOutput output) {
/*  75 */     FileUtil.decomposePath(directory)
/*  76 */       .ifSuccess(decomposedPath -> {
/*     */           
/*  78 */           List<Path> paths = (List)this.pathsForType.get(type);
/*  79 */           int pathsSize = paths.size();
/*  80 */           if (pathsSize == 1) {
/*  81 */             getResources(output, namespace, (Path)paths.get(0), decomposedPath);
/*  82 */           } else if (pathsSize > 1) {
/*  83 */             Map<Identifier, IoSupplier<InputStream>> resources = new HashMap<Identifier, IoSupplier<InputStream>>();
/*  84 */             for (int i = 0; i < pathsSize - 1; i++) {
/*  85 */               Objects.requireNonNull(resources); getResources(resources::putIfAbsent, namespace, (Path)paths.get(i), decomposedPath);
/*     */             } 
/*     */ 
/*     */             
/*  89 */             Path lastPath = (Path)paths.get(pathsSize - 1);
/*  90 */             if (resources.isEmpty()) {
/*  91 */               getResources(output, namespace, lastPath, decomposedPath);
/*     */             } else {
/*  93 */               Objects.requireNonNull(resources); getResources(resources::putIfAbsent, namespace, lastPath, decomposedPath);
/*  94 */               resources.forEach(output);
/*     */             }
/*     */           
/*     */           }
/*     */         
/*  99 */         }).ifError(error -> 
/* 100 */         LOGGER.error("Invalid path {}: {}", directory, error.message()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void getResources(PackResources.ResourceOutput result, String namespace, Path root, List<String> directory) {
/* 105 */     Path namespaceDir = root.resolve(namespace);
/* 106 */     PathPackResources.listPath(namespace, namespaceDir, directory, result);
/*     */   }
/*     */ 
/*     */   
/*     */   public IoSupplier<InputStream> getResource(PackType type, Identifier location) {
/* 111 */     return (IoSupplier)FileUtil.decomposePath(location.getPath()).mapOrElse(decomposedPath -> {
/*     */           
/* 113 */           String namespace = location.getNamespace();
/* 114 */           for (Path typePath : (List)this.pathsForType.get(type)) {
/* 115 */             Path resource = FileUtil.resolvePath(typePath.resolve(namespace), decomposedPath);
/* 116 */             if (Files.exists(resource, new java.nio.file.LinkOption[0]) && PathPackResources.validatePath(resource)) {
/* 117 */               return IoSupplier.create(resource);
/*     */             }
/*     */           } 
/* 120 */           return null;
/*     */         }error -> {
/*     */           
/* 123 */           LOGGER.error("Invalid path {}: {}", location, error.message());
/* 124 */           return null;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   public Set<String> getNamespaces(PackType type) { return this.namespaces; }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getMetadataSection(MetadataSectionType<T> metadataSerializer) {
/* 136 */     IoSupplier<InputStream> resource = getRootResource(new String[] { "pack.mcmeta" });
/* 137 */     if (resource != null) {
/* 138 */       try { InputStream stream = (InputStream)resource.get(); 
/* 139 */         try { T result = (T)AbstractPackResources.getMetadataFromStream(metadataSerializer, stream, this.location);
/* 140 */           if (result != null)
/* 141 */           { T t = result;
/*     */             
/* 143 */             if (stream != null) stream.close();  return t; }  if (stream != null) stream.close();  } catch (Throwable throwable) { if (stream != null) try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 148 */     return (T)this.metadata.get(metadataSerializer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 153 */   public PackLocationInfo location() { return this.location; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceProvider asProvider() {
/* 165 */     return location -> Optional.ofNullable(getResource(PackType.CLIENT_RESOURCES, location)).map(());
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\VanillaPackResources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */