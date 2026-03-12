/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.server.packs.resources.IoSupplier;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PathPackResources
/*     */   extends AbstractPackResources
/*     */ {
/*  32 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  33 */   private static final Joiner PATH_JOINER = Joiner.on("/");
/*     */   
/*     */   private final Path root;
/*     */   
/*     */   public PathPackResources(PackLocationInfo location, Path root) {
/*  38 */     super(location);
/*  39 */     this.root = root;
/*     */   }
/*     */ 
/*     */   
/*     */   public IoSupplier<InputStream> getRootResource(String... path) {
/*  44 */     FileUtil.validatePath(path);
/*     */     
/*  46 */     Path pathInRoot = FileUtil.resolvePath(this.root, List.of(path));
/*  47 */     if (Files.exists(pathInRoot, new java.nio.file.LinkOption[0])) {
/*  48 */       return IoSupplier.create(pathInRoot);
/*     */     }
/*  50 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean validatePath(Path path) {
/*  57 */     if (!SharedConstants.DEBUG_VALIDATE_RESOURCE_PATH_CASE) {
/*  58 */       return true;
/*     */     }
/*  60 */     if (path.getFileSystem() != FileSystems.getDefault()) {
/*  61 */       return true;
/*     */     }
/*     */     
/*     */     try {
/*  65 */       return path.toRealPath(new java.nio.file.LinkOption[0]).endsWith(path);
/*  66 */     } catch (IOException e) {
/*  67 */       LOGGER.warn("Failed to resolve real path for {}", path, e);
/*  68 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public IoSupplier<InputStream> getResource(PackType type, Identifier location) {
/*  74 */     Path namespacePath = this.root.resolve(type.getDirectory()).resolve(location.getNamespace());
/*  75 */     return getResource(location, namespacePath);
/*     */   }
/*     */   
/*     */   public static IoSupplier<InputStream> getResource(Identifier location, Path path) {
/*  79 */     return (IoSupplier)FileUtil.decomposePath(location.getPath()).mapOrElse(decomposedPath -> {
/*     */           
/*  81 */           Path resolvedPath = FileUtil.resolvePath(path, decomposedPath);
/*  82 */           return returnFileIfExists(resolvedPath);
/*     */         }error -> {
/*     */           
/*  85 */           LOGGER.error("Invalid path {}: {}", location, error.message());
/*  86 */           return null;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static IoSupplier<InputStream> returnFileIfExists(Path resolvedPath) {
/*  92 */     if (Files.exists(resolvedPath, new java.nio.file.LinkOption[0]) && validatePath(resolvedPath)) {
/*  93 */       return IoSupplier.create(resolvedPath);
/*     */     }
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void listResources(PackType type, String namespace, String directory, PackResources.ResourceOutput output) {
/* 100 */     FileUtil.decomposePath(directory)
/* 101 */       .ifSuccess(decomposedPath -> {
/* 102 */           Path namespaceDir = this.root.resolve(type.getDirectory()).resolve(namespace);
/* 103 */           listPath(namespace, namespaceDir, decomposedPath, output);
/*     */         
/* 105 */         }).ifError(error -> 
/* 106 */         LOGGER.error("Invalid path {}: {}", directory, error.message()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void listPath(String namespace, Path rootDir, List<String> decomposedPrefixPath, PackResources.ResourceOutput output) {
/* 111 */     Path targetPath = FileUtil.resolvePath(rootDir, decomposedPrefixPath); 
/* 112 */     try { Stream<Path> files = Files.find(targetPath, 2147483647, PathPackResources::isRegularFile, new java.nio.file.FileVisitOption[0]); 
/* 113 */       try { files.forEach(file -> {
/* 114 */               String resourcePath = PATH_JOINER.join(rootDir.relativize(file));
/* 115 */               Identifier identifier = Identifier.tryBuild(namespace, resourcePath);
/* 116 */               if (identifier == null) {
/* 117 */                 Util.logAndPauseIfInIde(String.format(Locale.ROOT, "Invalid path in pack: %s:%s, ignoring", new Object[] { namespace, resourcePath }));
/*     */               } else {
/* 119 */                 output.accept(identifier, IoSupplier.create(file));
/*     */               } 
/*     */             });
/* 122 */         if (files != null) files.close();  } catch (Throwable throwable) { if (files != null) try { files.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (NoSuchFileException|java.nio.file.NotDirectoryException noSuchFileException)
/*     */     {  }
/* 124 */     catch (IOException e)
/* 125 */     { LOGGER.error("Failed to list path {}", targetPath, e); }
/*     */   
/*     */   }
/*     */   
/*     */   private static boolean isRegularFile(Path file, BasicFileAttributes attributes) {
/* 130 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 131 */       return (attributes.isRegularFile() && !StringUtils.equalsIgnoreCase(file.getFileName().toString(), ".ds_store"));
/*     */     }
/* 133 */     return attributes.isRegularFile();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getNamespaces(PackType type) {
/* 138 */     Set<String> namespaces = Sets.newHashSet();
/* 139 */     Path assetRoot = this.root.resolve(type.getDirectory());
/*     */     
/* 141 */     try { DirectoryStream<Path> directDirs = Files.newDirectoryStream(assetRoot); 
/* 142 */       try { for (Path directDir : directDirs) {
/* 143 */           String namespace = directDir.getFileName().toString();
/*     */           
/* 145 */           if (Identifier.isValidNamespace(namespace)) {
/* 146 */             namespaces.add(namespace); continue;
/*     */           } 
/* 148 */           LOGGER.warn("Non [a-z0-9_.-] character in namespace {} in pack {}, ignoring", namespace, this.root);
/*     */         } 
/*     */         
/* 151 */         if (directDirs != null) directDirs.close();  } catch (Throwable throwable) { if (directDirs != null) try { directDirs.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (NoSuchFileException|java.nio.file.NotDirectoryException noSuchFileException)
/*     */     {  }
/* 153 */     catch (IOException e)
/* 154 */     { LOGGER.error("Failed to list path {}", assetRoot, e); }
/*     */     
/* 156 */     return namespaces;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {}
/*     */   
/*     */   public static class PathResourcesSupplier
/*     */     implements Pack.ResourcesSupplier
/*     */   {
/*     */     private final Path content;
/*     */     
/* 167 */     public PathResourcesSupplier(Path content) { this.content = content; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     public PackResources openPrimary(PackLocationInfo location) { return new PathPackResources(location, this.content); }
/*     */ 
/*     */ 
/*     */     
/*     */     public PackResources openFull(PackLocationInfo location, Pack.Metadata metadata) {
/* 177 */       PackResources primary = openPrimary(location);
/*     */       
/* 179 */       List<String> overlays = metadata.overlays();
/* 180 */       if (overlays.isEmpty()) {
/* 181 */         return primary;
/*     */       }
/*     */       
/* 184 */       List<PackResources> overlayResources = new ArrayList<PackResources>(overlays.size());
/* 185 */       for (String overlay : overlays) {
/* 186 */         Path overlayRoot = this.content.resolve(overlay);
/* 187 */         overlayResources.add(new PathPackResources(location, overlayRoot));
/*     */       } 
/*     */       
/* 190 */       return new CompositePackResources(primary, overlayResources);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\PathPackResources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */