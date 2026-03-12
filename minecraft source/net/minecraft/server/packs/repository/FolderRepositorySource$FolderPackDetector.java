/*     */ package net.minecraft.server.packs.repository;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Path;
/*     */ import net.minecraft.server.packs.FilePackResources;
/*     */ import net.minecraft.server.packs.PathPackResources;
/*     */ import net.minecraft.world.level.validation.DirectoryValidator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FolderPackDetector
/*     */   extends PackDetector<Pack.ResourcesSupplier>
/*     */ {
/* 107 */   protected FolderPackDetector(DirectoryValidator validator) { super(validator); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Pack.ResourcesSupplier createZipPack(Path content) {
/* 112 */     FileSystem fileSystem = content.getFileSystem();
/* 113 */     if (fileSystem == FileSystems.getDefault() || fileSystem instanceof net.minecraft.server.packs.linkfs.LinkFileSystem) {
/* 114 */       return new FilePackResources.FileResourcesSupplier(content);
/*     */     }
/* 116 */     FolderRepositorySource.LOGGER.info("Can't open pack archive at {}", content);
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 122 */   protected Pack.ResourcesSupplier createDirectoryPack(Path content) { return new PathPackResources.PathResourcesSupplier(content); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\repository\FolderRepositorySource$FolderPackDetector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */