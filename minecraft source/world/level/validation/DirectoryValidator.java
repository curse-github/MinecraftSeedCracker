/*    */ package net.minecraft.world.level.validation;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.FileVisitResult;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.LinkOption;
/*    */ import java.nio.file.NoSuchFileException;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.PathMatcher;
/*    */ import java.nio.file.SimpleFileVisitor;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class DirectoryValidator
/*    */ {
/*    */   private final PathMatcher symlinkTargetAllowList;
/*    */   
/* 19 */   public DirectoryValidator(PathMatcher symlinkTargetAllowList) { this.symlinkTargetAllowList = symlinkTargetAllowList; }
/*    */ 
/*    */   
/*    */   public void validateSymlink(Path path, List<ForbiddenSymlinkInfo> issues) throws IOException {
/* 23 */     Path target = Files.readSymbolicLink(path);
/* 24 */     if (!this.symlinkTargetAllowList.matches(target)) {
/* 25 */       issues.add(new ForbiddenSymlinkInfo(path, target));
/*    */     }
/*    */   }
/*    */   
/*    */   public List<ForbiddenSymlinkInfo> validateSymlink(Path path) throws IOException {
/* 30 */     List<ForbiddenSymlinkInfo> result = new ArrayList<ForbiddenSymlinkInfo>();
/* 31 */     validateSymlink(path, result);
/* 32 */     return result;
/*    */   }
/*    */   public List<ForbiddenSymlinkInfo> validateDirectory(Path directory, boolean allowTopSymlink) throws IOException {
/*    */     BasicFileAttributes targetAttributes;
/* 36 */     List<ForbiddenSymlinkInfo> issues = new ArrayList<ForbiddenSymlinkInfo>();
/*    */ 
/*    */     
/*    */     try {
/* 40 */       targetAttributes = Files.readAttributes(directory, BasicFileAttributes.class, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/* 41 */     } catch (NoSuchFileException e) {
/* 42 */       return issues;
/*    */     } 
/*    */     
/* 45 */     if (targetAttributes.isRegularFile()) {
/* 46 */       throw new IOException("Path " + String.valueOf(directory) + " is not a directory");
/*    */     }
/*    */     
/* 49 */     if (targetAttributes.isSymbolicLink()) {
/* 50 */       if (allowTopSymlink) {
/*    */ 
/*    */         
/* 53 */         directory = Files.readSymbolicLink(directory);
/*    */       } else {
/* 55 */         validateSymlink(directory, issues);
/* 56 */         return issues;
/*    */       } 
/*    */     }
/*    */     
/* 60 */     validateKnownDirectory(directory, issues);
/* 61 */     return issues;
/*    */   }
/*    */   
/*    */   public void validateKnownDirectory(Path directory, final List<ForbiddenSymlinkInfo> issues) throws IOException {
/* 65 */     Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
/*    */           private void validateSymlink(Path path, BasicFileAttributes attrs) throws IOException {
/* 67 */             if (attrs.isSymbolicLink()) {
/* 68 */               DirectoryValidator.this.validateSymlink(path, issues);
/*    */             }
/*    */           }
/*    */ 
/*    */           
/*    */           public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
/* 74 */             validateSymlink(dir, attrs);
/* 75 */             return super.preVisitDirectory(dir, attrs);
/*    */           }
/*    */ 
/*    */           
/*    */           public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/* 80 */             validateSymlink(file, attrs);
/* 81 */             return super.visitFile(file, attrs);
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\validation\DirectoryValidator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */