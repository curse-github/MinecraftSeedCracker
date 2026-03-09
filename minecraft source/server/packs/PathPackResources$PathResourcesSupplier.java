/*     */ package net.minecraft.server.packs;
/*     */ 
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
/*     */ public class PathResourcesSupplier
/*     */   implements Pack.ResourcesSupplier
/*     */ {
/*     */   private final Path content;
/*     */   
/* 167 */   public PathResourcesSupplier(Path content) { this.content = content; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 172 */   public PackResources openPrimary(PackLocationInfo location) { return new PathPackResources(location, this.content); }
/*     */ 
/*     */ 
/*     */   
/*     */   public PackResources openFull(PackLocationInfo location, Pack.Metadata metadata) {
/* 177 */     PackResources primary = openPrimary(location);
/*     */     
/* 179 */     List<String> overlays = metadata.overlays();
/* 180 */     if (overlays.isEmpty()) {
/* 181 */       return primary;
/*     */     }
/*     */     
/* 184 */     List<PackResources> overlayResources = new ArrayList<PackResources>(overlays.size());
/* 185 */     for (String overlay : overlays) {
/* 186 */       Path overlayRoot = this.content.resolve(overlay);
/* 187 */       overlayResources.add(new PathPackResources(location, overlayRoot));
/*     */     } 
/*     */     
/* 190 */     return new CompositePackResources(primary, overlayResources);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\PathPackResources$PathResourcesSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */