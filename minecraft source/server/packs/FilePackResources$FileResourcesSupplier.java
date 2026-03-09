/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.server.packs.repository.Pack;
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
/*     */ public class FileResourcesSupplier
/*     */   implements Pack.ResourcesSupplier
/*     */ {
/*     */   private final File content;
/*     */   
/* 199 */   public FileResourcesSupplier(Path content) { this(content.toFile()); }
/*     */ 
/*     */ 
/*     */   
/* 203 */   public FileResourcesSupplier(File content) { this.content = content; }
/*     */ 
/*     */ 
/*     */   
/*     */   public PackResources openPrimary(PackLocationInfo location) {
/* 208 */     FilePackResources.SharedZipFileAccess fileAccess = new FilePackResources.SharedZipFileAccess(this.content);
/* 209 */     return new FilePackResources(location, fileAccess, "");
/*     */   }
/*     */ 
/*     */   
/*     */   public PackResources openFull(PackLocationInfo location, Pack.Metadata metadata) {
/* 214 */     FilePackResources.SharedZipFileAccess fileAccess = new FilePackResources.SharedZipFileAccess(this.content);
/*     */     
/* 216 */     PackResources primary = new FilePackResources(location, fileAccess, "");
/* 217 */     List<String> overlays = metadata.overlays();
/* 218 */     if (overlays.isEmpty()) {
/* 219 */       return primary;
/*     */     }
/*     */     
/* 222 */     List<PackResources> overlayResources = new ArrayList<PackResources>(overlays.size());
/* 223 */     for (String overlay : overlays) {
/* 224 */       overlayResources.add(new FilePackResources(location, fileAccess, overlay));
/*     */     }
/*     */     
/* 227 */     return new CompositePackResources(primary, overlayResources);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\FilePackResources$FileResourcesSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */