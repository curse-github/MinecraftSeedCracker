/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.FileSystemUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class VanillaPackResourcesBuilder
/*     */ {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   public static Consumer<VanillaPackResourcesBuilder> developmentConfig = builder -> {
/*     */     
/*     */     };
/*  33 */   private static final Map<PackType, Path> ROOT_DIR_BY_TYPE = (Map)Util.make(() -> {
/*  34 */         synchronized (VanillaPackResources.class) {
/*  35 */           ImmutableMap.Builder<PackType, Path> result = ImmutableMap.builder();
/*  36 */           for (PackType type : PackType.values()) {
/*  37 */             String probeName = "/" + type.getDirectory() + "/.mcassetsroot";
/*  38 */             URL probeUrl = VanillaPackResources.class.getResource(probeName);
/*  39 */             if (probeUrl == null) {
/*  40 */               LOGGER.error("File {} does not exist in classpath", probeName);
/*     */             } else {
/*     */               
/*     */               try {
/*  44 */                 URI probeUri = probeUrl.toURI();
/*  45 */                 String scheme = probeUri.getScheme();
/*  46 */                 if (!"jar".equals(scheme) && !"file".equals(scheme)) {
/*  47 */                   LOGGER.warn("Assets URL '{}' uses unexpected schema", probeUri);
/*     */                 }
/*     */                 
/*  50 */                 Path probePath = FileSystemUtil.safeGetPath(probeUri);
/*  51 */                 result.put(type, probePath.getParent());
/*  52 */               } catch (Exception e) {
/*  53 */                 LOGGER.error("Couldn't resolve path to vanilla assets", e);
/*     */               } 
/*     */             } 
/*  56 */           }  return result.build();
/*     */         } 
/*     */       });
/*     */   
/*  60 */   private final Set<Path> rootPaths = new LinkedHashSet();
/*  61 */   private final Map<PackType, Set<Path>> pathsForType = new EnumMap(PackType.class);
/*     */   
/*  63 */   private BuiltInMetadata metadata = BuiltInMetadata.of();
/*  64 */   private final Set<String> namespaces = new HashSet();
/*     */   
/*     */   private boolean validateDirPath(Path path) {
/*  67 */     if (!Files.exists(path, new java.nio.file.LinkOption[0])) {
/*  68 */       return false;
/*     */     }
/*  70 */     if (!Files.isDirectory(path, new java.nio.file.LinkOption[0])) {
/*  71 */       throw new IllegalArgumentException("Path " + String.valueOf(path.toAbsolutePath()) + " is not directory");
/*     */     }
/*  73 */     return true;
/*     */   }
/*     */   
/*     */   private void pushRootPath(Path path) {
/*  77 */     if (validateDirPath(path)) {
/*  78 */       this.rootPaths.add(path);
/*     */     }
/*     */   }
/*     */   
/*     */   private void pushPathForType(PackType packType, Path path) {
/*  83 */     if (validateDirPath(path)) {
/*  84 */       ((Set)this.pathsForType.computeIfAbsent(packType, k -> new LinkedHashSet())).add(path);
/*     */     }
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder pushJarResources() {
/*  89 */     ROOT_DIR_BY_TYPE.forEach((packType, path) -> {
/*  90 */           pushRootPath(path.getParent());
/*  91 */           pushPathForType(packType, path);
/*     */         });
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VanillaPackResourcesBuilder pushClasspathResources(PackType packType, Class<?> source) {
/*  99 */     Enumeration<URL> resources = null;
/*     */     try {
/* 101 */       resources = source.getClassLoader().getResources(packType.getDirectory() + "/");
/* 102 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/* 105 */     while (resources != null && resources.hasMoreElements()) {
/* 106 */       URL url = (URL)resources.nextElement();
/*     */       try {
/* 108 */         URI uri = url.toURI();
/* 109 */         if ("file".equals(uri.getScheme())) {
/* 110 */           Path assetsPath = Paths.get(uri);
/* 111 */           pushRootPath(assetsPath.getParent());
/* 112 */           pushPathForType(packType, assetsPath);
/*     */         } 
/* 114 */       } catch (Exception e) {
/* 115 */         LOGGER.error("Failed to extract path from {}", url, e);
/*     */       } 
/*     */     } 
/* 118 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder applyDevelopmentConfig() {
/* 122 */     developmentConfig.accept(this);
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VanillaPackResourcesBuilder pushUniversalPath(Path path) {
/* 130 */     pushRootPath(path);
/* 131 */     for (PackType packType : PackType.values()) {
/* 132 */       pushPathForType(packType, path.resolve(packType.getDirectory()));
/*     */     }
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VanillaPackResourcesBuilder pushAssetPath(PackType packType, Path path) {
/* 141 */     pushRootPath(path);
/* 142 */     pushPathForType(packType, path);
/* 143 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder setMetadata(BuiltInMetadata metadata) {
/* 147 */     this.metadata = metadata;
/* 148 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder exposeNamespace(String... namespaces) {
/* 152 */     this.namespaces.addAll(Arrays.asList(namespaces));
/* 153 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResources build(PackLocationInfo location) {
/* 157 */     return new VanillaPackResources(location, this.metadata, 
/*     */ 
/*     */         
/* 160 */         Set.copyOf(this.namespaces), 
/* 161 */         copyAndReverse(this.rootPaths), 
/* 162 */         Util.makeEnumMap(PackType.class, packType -> copyAndReverse((Collection)this.pathsForType.getOrDefault(packType, Set.of()))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Path> copyAndReverse(Collection<Path> input) {
/* 167 */     List<Path> paths = new ArrayList<Path>(input);
/* 168 */     Collections.reverse(paths);
/* 169 */     return List.copyOf(paths);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\VanillaPackResourcesBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */