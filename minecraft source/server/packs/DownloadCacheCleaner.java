/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.DirectoryNotEmptyException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class DownloadCacheCleaner
/*     */ {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static final class PathAndTime extends Record { private final Path path; private final FileTime modifiedTime;
/*  27 */     private PathAndTime(Path path, FileTime modifiedTime) { this.path = path; this.modifiedTime = modifiedTime; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  27 */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime; } public Path path() { return this.path; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndTime;
/*  27 */       //   0	8	1	o	Ljava/lang/Object; } public FileTime modifiedTime() { return this.modifiedTime; }
/*  28 */     public static final Comparator<PathAndTime> NEWEST_FIRST = Comparator.comparing(PathAndTime::modifiedTime).reversed(); }
/*     */   private static final class PathAndPriority extends Record { private final Path path; private final int removalPriority;
/*     */     
/*  31 */     private PathAndPriority(Path path, int removalPriority) { this.path = path; this.removalPriority = removalPriority; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority;
/*  31 */       //   0	8	1	o	Ljava/lang/Object; } public Path path() { return this.path; } public int removalPriority() { return this.removalPriority; }
/*  32 */     public static final Comparator<PathAndPriority> HIGHEST_PRIORITY_FIRST = Comparator.comparing(PathAndPriority::removalPriority).reversed(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void vacuumCacheDir(Path cacheDir, int maxFiles) {
/*     */     try {
/*  44 */       List<PathAndTime> filesAndDates = listFilesWithModificationTimes(cacheDir);
/*  45 */       int toRemove = filesAndDates.size() - maxFiles;
/*  46 */       if (toRemove <= 0) {
/*     */         return;
/*     */       }
/*     */       
/*  50 */       filesAndDates.sort(PathAndTime.NEWEST_FIRST);
/*  51 */       List<PathAndPriority> filesWithDirOrder = prioritizeFilesInDirs(filesAndDates);
/*     */       
/*  53 */       Collections.reverse(filesWithDirOrder);
/*  54 */       filesWithDirOrder.sort(PathAndPriority.HIGHEST_PRIORITY_FIRST);
/*     */       
/*  56 */       Set<Path> emptyDirectoryCandidates = new HashSet<Path>();
/*  57 */       for (int i = 0; i < toRemove; i++) {
/*  58 */         PathAndPriority entry = (PathAndPriority)filesWithDirOrder.get(i);
/*  59 */         Path pathToRemove = entry.path;
/*     */         try {
/*  61 */           Files.delete(pathToRemove);
/*     */ 
/*     */           
/*  64 */           if (entry.removalPriority == 0) {
/*  65 */             emptyDirectoryCandidates.add(pathToRemove.getParent());
/*     */           }
/*  67 */         } catch (IOException e) {
/*     */           
/*  69 */           LOGGER.warn("Failed to delete cache file {}", pathToRemove, e);
/*     */         } 
/*     */       } 
/*  72 */       emptyDirectoryCandidates.remove(cacheDir);
/*  73 */       for (Path dir : emptyDirectoryCandidates) {
/*     */         try {
/*  75 */           Files.delete(dir);
/*  76 */         } catch (DirectoryNotEmptyException directoryNotEmptyException) {
/*     */         
/*  78 */         } catch (IOException e) {
/*  79 */           LOGGER.warn("Failed to delete empty(?) cache directory {}", dir, e);
/*     */         }
/*     */       
/*     */       } 
/*  83 */     } catch (IOException|java.io.UncheckedIOException e) {
/*  84 */       LOGGER.error("Failed to vacuum cache dir {}", cacheDir, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<PathAndTime> listFilesWithModificationTimes(final Path cacheDir) throws IOException {
/*     */     try {
/*  90 */       final List<PathAndTime> unsortedFiles = new ArrayList<PathAndTime>();
/*  91 */       Files.walkFileTree(cacheDir, new SimpleFileVisitor<Path>()
/*     */           {
/*     */             public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
/*     */             {
/*  95 */               if (attrs.isRegularFile() && !file.getParent().equals(cacheDir)) {
/*     */ 
/*     */                 
/*  98 */                 FileTime fileTime = attrs.lastModifiedTime();
/*  99 */                 unsortedFiles.add(new DownloadCacheCleaner.PathAndTime(file, fileTime));
/*     */               } 
/* 101 */               return FileVisitResult.CONTINUE;
/*     */             }
/*     */           });
/* 104 */       return unsortedFiles;
/* 105 */     } catch (NoSuchFileException e) {
/*     */       
/* 107 */       return List.of();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<PathAndPriority> prioritizeFilesInDirs(List<PathAndTime> filesAndDates) {
/* 112 */     List<PathAndPriority> result = new ArrayList<PathAndPriority>();
/*     */ 
/*     */     
/* 115 */     Object2IntOpenHashMap<Path> parentCounts = new Object2IntOpenHashMap<Path>();
/* 116 */     for (PathAndTime entry : filesAndDates) {
/* 117 */       int removalPriority = parentCounts.addTo(entry.path.getParent(), 1);
/* 118 */       result.add(new PathAndPriority(entry.path, removalPriority));
/*     */     } 
/*     */     
/* 121 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\DownloadCacheCleaner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */