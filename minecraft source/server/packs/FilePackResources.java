/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.server.packs.resources.IoSupplier;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FilePackResources
/*     */   extends AbstractPackResources
/*     */ {
/*  26 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private final SharedZipFileAccess zipFileAccess;
/*     */   private final String prefix;
/*     */   
/*     */   private FilePackResources(PackLocationInfo location, SharedZipFileAccess zipFileAccess, String prefix) {
/*  31 */     super(location);
/*  32 */     this.zipFileAccess = zipFileAccess;
/*  33 */     this.prefix = prefix;
/*     */   }
/*     */ 
/*     */   
/*  37 */   private static String getPathFromLocation(PackType type, Identifier location) { return String.format(Locale.ROOT, "%s/%s/%s", new Object[] { type.getDirectory(), location.getNamespace(), location.getPath() }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public IoSupplier<InputStream> getRootResource(String... path) { return getResource(String.join("/", path)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public IoSupplier<InputStream> getResource(PackType type, Identifier location) { return getResource(getPathFromLocation(type, location)); }
/*     */ 
/*     */   
/*     */   private String addPrefix(String path) {
/*  51 */     if (this.prefix.isEmpty()) {
/*  52 */       return path;
/*     */     }
/*     */     
/*  55 */     return this.prefix + "/" + this.prefix;
/*     */   }
/*     */   
/*     */   private IoSupplier<InputStream> getResource(String path) {
/*  59 */     ZipFile zipFile = this.zipFileAccess.getOrCreateZipFile();
/*  60 */     if (zipFile == null) {
/*  61 */       return null;
/*     */     }
/*     */     
/*  64 */     ZipEntry entry = zipFile.getEntry(addPrefix(path));
/*  65 */     if (entry == null) {
/*  66 */       return null;
/*     */     }
/*     */     
/*  69 */     return IoSupplier.create(zipFile, entry);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getNamespaces(PackType type) {
/*  74 */     ZipFile zipFile = this.zipFileAccess.getOrCreateZipFile();
/*  75 */     if (zipFile == null) {
/*  76 */       return Set.of();
/*     */     }
/*     */     
/*  79 */     Enumeration<? extends ZipEntry> entries = zipFile.entries();
/*     */     
/*  81 */     Set<String> namespaces = Sets.newHashSet();
/*     */     
/*  83 */     String typePrefix = addPrefix(type.getDirectory() + "/");
/*     */     
/*  85 */     while (entries.hasMoreElements()) {
/*  86 */       ZipEntry zipEntry = (ZipEntry)entries.nextElement();
/*     */       
/*  88 */       String name = zipEntry.getName();
/*  89 */       String namespace = extractNamespace(typePrefix, name);
/*  90 */       if (!namespace.isEmpty()) {
/*  91 */         if (Identifier.isValidNamespace(namespace)) {
/*  92 */           namespaces.add(namespace); continue;
/*     */         } 
/*  94 */         LOGGER.warn("Non [a-z0-9_.-] character in namespace {} in pack {}, ignoring", namespace, this.zipFileAccess.file);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  99 */     return namespaces;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static String extractNamespace(String prefix, String name) {
/* 104 */     if (!name.startsWith(prefix)) {
/* 105 */       return "";
/*     */     }
/*     */     
/* 108 */     int prefixLength = prefix.length();
/* 109 */     int firstPart = name.indexOf('/', prefixLength);
/* 110 */     if (firstPart == -1) {
/* 111 */       return name.substring(prefixLength);
/*     */     }
/* 113 */     return name.substring(prefixLength, firstPart);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 118 */   public void close() { this.zipFileAccess.close(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void listResources(PackType type, String namespace, String directory, PackResources.ResourceOutput output) {
/* 123 */     ZipFile zipFile = this.zipFileAccess.getOrCreateZipFile();
/* 124 */     if (zipFile == null) {
/*     */       return;
/*     */     }
/* 127 */     Enumeration<? extends ZipEntry> entries = zipFile.entries();
/* 128 */     String root = addPrefix(type.getDirectory() + "/" + type.getDirectory() + "/");
/* 129 */     String prefix = root + root + "/";
/*     */     
/* 131 */     while (entries.hasMoreElements()) {
/* 132 */       ZipEntry zipEntry = (ZipEntry)entries.nextElement();
/* 133 */       if (zipEntry.isDirectory()) {
/*     */         continue;
/*     */       }
/*     */       
/* 137 */       String name = zipEntry.getName();
/* 138 */       if (!name.startsWith(prefix)) {
/*     */         continue;
/*     */       }
/*     */       
/* 142 */       String path = name.substring(root.length());
/* 143 */       Identifier id = Identifier.tryBuild(namespace, path);
/* 144 */       if (id != null) {
/* 145 */         output.accept(id, IoSupplier.create(zipFile, zipEntry)); continue;
/*     */       } 
/* 147 */       LOGGER.warn("Invalid path in datapack: {}:{}, ignoring", namespace, path);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class SharedZipFileAccess
/*     */     implements AutoCloseable
/*     */   {
/*     */     private final File file;
/*     */     private ZipFile zipFile;
/*     */     private boolean failedToLoad;
/*     */     
/* 158 */     private SharedZipFileAccess(File file) { this.file = file; }
/*     */ 
/*     */     
/*     */     private ZipFile getOrCreateZipFile() {
/* 162 */       if (this.failedToLoad) {
/* 163 */         return null;
/*     */       }
/*     */       
/* 166 */       if (this.zipFile == null) {
/*     */         try {
/* 168 */           this.zipFile = new ZipFile(this.file);
/* 169 */         } catch (IOException e) {
/* 170 */           FilePackResources.LOGGER.error("Failed to open pack {}", this.file, e);
/* 171 */           this.failedToLoad = true;
/* 172 */           return null;
/*     */         } 
/*     */       }
/*     */       
/* 176 */       return this.zipFile;
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() {
/* 181 */       if (this.zipFile != null) {
/* 182 */         IOUtils.closeQuietly(this.zipFile);
/* 183 */         this.zipFile = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void finalize() {
/* 190 */       close();
/* 191 */       super.finalize();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FileResourcesSupplier
/*     */     implements Pack.ResourcesSupplier {
/*     */     private final File content;
/*     */     
/* 199 */     public FileResourcesSupplier(Path content) { this(content.toFile()); }
/*     */ 
/*     */ 
/*     */     
/* 203 */     public FileResourcesSupplier(File content) { this.content = content; }
/*     */ 
/*     */ 
/*     */     
/*     */     public PackResources openPrimary(PackLocationInfo location) {
/* 208 */       FilePackResources.SharedZipFileAccess fileAccess = new FilePackResources.SharedZipFileAccess(this.content);
/* 209 */       return new FilePackResources(location, fileAccess, "");
/*     */     }
/*     */ 
/*     */     
/*     */     public PackResources openFull(PackLocationInfo location, Pack.Metadata metadata) {
/* 214 */       FilePackResources.SharedZipFileAccess fileAccess = new FilePackResources.SharedZipFileAccess(this.content);
/*     */       
/* 216 */       PackResources primary = new FilePackResources(location, fileAccess, "");
/* 217 */       List<String> overlays = metadata.overlays();
/* 218 */       if (overlays.isEmpty()) {
/* 219 */         return primary;
/*     */       }
/*     */       
/* 222 */       List<PackResources> overlayResources = new ArrayList<PackResources>(overlays.size());
/* 223 */       for (String overlay : overlays) {
/* 224 */         overlayResources.add(new FilePackResources(location, fileAccess, overlay));
/*     */       }
/*     */       
/* 227 */       return new CompositePackResources(primary, overlayResources);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\FilePackResources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */