/*    */ package net.minecraft.world.level.validation;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.FileVisitResult;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.SimpleFileVisitor;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends SimpleFileVisitor<Path>
/*    */ {
/*    */   private void validateSymlink(Path path, BasicFileAttributes attrs) throws IOException {
/* 67 */     if (attrs.isSymbolicLink()) {
/* 68 */       DirectoryValidator.this.validateSymlink(path, issues);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
/* 74 */     validateSymlink(dir, attrs);
/* 75 */     return super.preVisitDirectory(dir, attrs);
/*    */   }
/*    */ 
/*    */   
/*    */   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/* 80 */     validateSymlink(file, attrs);
/* 81 */     return super.visitFile(file, attrs);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\validation\DirectoryValidator$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */