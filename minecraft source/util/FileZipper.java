/*    */ package net.minecraft.util;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.Closeable;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.UncheckedIOException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.FileSystem;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class FileZipper implements Closeable {
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Path outputFile;
/*    */   private final Path tempFile;
/*    */   private final FileSystem fs;
/*    */   
/*    */   public FileZipper(Path outputFile) {
/* 26 */     this.outputFile = outputFile;
/* 27 */     this.tempFile = outputFile.resolveSibling(outputFile.getFileName().toString() + "_tmp");
/*    */     try {
/* 29 */       this.fs = Util.ZIP_FILE_SYSTEM_PROVIDER.newFileSystem(this.tempFile, ImmutableMap.of("create", "true"));
/* 30 */     } catch (IOException e) {
/* 31 */       throw new UncheckedIOException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void add(Path destinationRelativePath, String content) {
/*    */     try {
/* 37 */       Path root = this.fs.getPath(File.separator, new String[0]);
/* 38 */       Path path = root.resolve(destinationRelativePath.toString());
/*    */       
/* 40 */       Files.createDirectories(path.getParent(), new java.nio.file.attribute.FileAttribute[0]);
/* 41 */       Files.write(path, content.getBytes(StandardCharsets.UTF_8), new java.nio.file.OpenOption[0]);
/* 42 */     } catch (IOException e) {
/* 43 */       throw new UncheckedIOException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void add(Path destinationRelativePath, File file) {
/*    */     try {
/* 49 */       Path root = this.fs.getPath(File.separator, new String[0]);
/* 50 */       Path path = root.resolve(destinationRelativePath.toString());
/*    */       
/* 52 */       Files.createDirectories(path.getParent(), new java.nio.file.attribute.FileAttribute[0]);
/* 53 */       Files.copy(file.toPath(), path, new java.nio.file.CopyOption[0]);
/* 54 */     } catch (IOException e) {
/* 55 */       throw new UncheckedIOException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void add(Path path) {
/*    */     
/* 61 */     try { Path root = this.fs.getPath(File.separator, new String[0]);
/*    */       
/* 63 */       if (Files.isRegularFile(path, new java.nio.file.LinkOption[0])) {
/* 64 */         Path targetFile = root.resolve(path.getParent().relativize(path).toString());
/* 65 */         Files.copy(targetFile, path, new java.nio.file.CopyOption[0]);
/*    */         
/*    */         return;
/*    */       } 
/* 69 */       Stream<Path> sourceFiles = Files.find(path, 2147483647, (p, a) -> a.isRegularFile(), new java.nio.file.FileVisitOption[0]); 
/* 70 */       try { for (Path sourceFile : (List)sourceFiles.collect(Collectors.toList())) {
/* 71 */           Path targetFile = root.resolve(path.relativize(sourceFile).toString());
/* 72 */           Files.createDirectories(targetFile.getParent(), new java.nio.file.attribute.FileAttribute[0]);
/* 73 */           Files.copy(sourceFile, targetFile, new java.nio.file.CopyOption[0]);
/*    */         } 
/* 75 */         if (sourceFiles != null) sourceFiles.close();  } catch (Throwable throwable) { if (sourceFiles != null)
/* 76 */           try { sourceFiles.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 77 */     { throw new UncheckedIOException(e); }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/*    */     try {
/* 84 */       this.fs.close();
/* 85 */       Files.move(this.tempFile, this.outputFile, new java.nio.file.CopyOption[0]);
/* 86 */       LOGGER.info("Compressed to {}", this.outputFile);
/* 87 */     } catch (IOException e) {
/* 88 */       throw new UncheckedIOException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\FileZipper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */