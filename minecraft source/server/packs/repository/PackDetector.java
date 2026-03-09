/*    */ package net.minecraft.server.packs.repository;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.LinkOption;
/*    */ import java.nio.file.NoSuchFileException;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.level.validation.DirectoryValidator;
/*    */ import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class PackDetector<T>
/*    */   extends Object
/*    */ {
/*    */   private final DirectoryValidator validator;
/*    */   
/* 20 */   protected PackDetector(DirectoryValidator validator) { this.validator = validator; }
/*    */   
/*    */   public T detectPackResources(Path content, List<ForbiddenSymlinkInfo> issues) throws IOException {
/*    */     BasicFileAttributes attributes;
/* 24 */     Path targetContext = content;
/*    */     
/*    */     try {
/* 27 */       attributes = Files.readAttributes(content, BasicFileAttributes.class, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/* 28 */     } catch (NoSuchFileException e) {
/* 29 */       return null;
/*    */     } 
/*    */     
/* 32 */     if (attributes.isSymbolicLink()) {
/* 33 */       this.validator.validateSymlink(content, issues);
/* 34 */       if (!issues.isEmpty()) {
/* 35 */         return null;
/*    */       }
/* 37 */       targetContext = Files.readSymbolicLink(content);
/* 38 */       attributes = Files.readAttributes(targetContext, BasicFileAttributes.class, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/*    */     } 
/*    */     
/* 41 */     if (attributes.isDirectory()) {
/* 42 */       this.validator.validateKnownDirectory(targetContext, issues);
/* 43 */       if (!issues.isEmpty()) {
/* 44 */         return null;
/*    */       }
/* 46 */       if (!Files.isRegularFile(targetContext.resolve("pack.mcmeta"), new LinkOption[0])) {
/* 47 */         return null;
/*    */       }
/* 49 */       return (T)createDirectoryPack(targetContext);
/* 50 */     }  if (attributes.isRegularFile() && targetContext.getFileName().toString().endsWith(".zip")) {
/* 51 */       return (T)createZipPack(targetContext);
/*    */     }
/* 53 */     return null;
/*    */   }
/*    */   
/*    */   protected abstract T createZipPack(Path paramPath) throws IOException;
/*    */   
/*    */   protected abstract T createDirectoryPack(Path paramPath) throws IOException;
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\repository\PackDetector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */