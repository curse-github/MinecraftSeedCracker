/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileAlreadyExistsException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.InvalidPathException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.SharedConstants;
/*     */ import org.apache.commons.io.FilenameUtils;
/*     */ 
/*     */ 
/*     */ public class FileUtil
/*     */ {
/*  22 */   private static final Pattern COPY_COUNTER_PATTERN = Pattern.compile("(<name>.*) \\((<count>\\d*)\\)", 66);
/*     */   
/*     */   private static final int MAX_FILE_NAME = 255;
/*  25 */   private static final Pattern RESERVED_WINDOWS_FILENAMES = Pattern.compile(".*\\.|(?:COM|CLOCK\\$|CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(?:\\..*)?", 2);
/*     */   
/*  27 */   private static final Pattern STRICT_PATH_SEGMENT_CHECK = Pattern.compile("[-._a-z0-9]+");
/*     */   
/*     */   public static String sanitizeName(String baseName) {
/*  30 */     for (char replacer : SharedConstants.ILLEGAL_FILE_CHARACTERS) {
/*  31 */       baseName = baseName.replace(replacer, '_');
/*     */     }
/*     */     
/*  34 */     return baseName.replaceAll("[./\"]", "_");
/*     */   }
/*     */   
/*     */   public static String findAvailableName(Path baseDir, String baseName, String suffix) throws IOException {
/*  38 */     baseName = sanitizeName(baseName);
/*     */     
/*  40 */     if (!isPathPartPortable(baseName)) {
/*  41 */       baseName = "_" + baseName + "_";
/*     */     }
/*     */     
/*  44 */     Matcher matcher = COPY_COUNTER_PATTERN.matcher(baseName);
/*  45 */     int count = 0;
/*  46 */     if (matcher.matches()) {
/*  47 */       baseName = matcher.group("name");
/*  48 */       count = Integer.parseInt(matcher.group("count"));
/*     */     } 
/*  50 */     if (baseName.length() > 255 - suffix.length()) {
/*  51 */       baseName = baseName.substring(0, 255 - suffix.length());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/*  57 */       String nameToTest = baseName;
/*  58 */       if (count != 0) {
/*  59 */         String countSuffix = " (" + count + ")";
/*  60 */         int length = 255 - countSuffix.length();
/*  61 */         if (nameToTest.length() > length) {
/*  62 */           nameToTest = nameToTest.substring(0, length);
/*     */         }
/*  64 */         nameToTest = nameToTest + nameToTest;
/*     */       } 
/*     */       
/*  67 */       nameToTest = nameToTest + nameToTest;
/*     */       
/*  69 */       Path fullPath = baseDir.resolve(nameToTest);
/*     */       try {
/*  71 */         Path created = Files.createDirectory(fullPath, new java.nio.file.attribute.FileAttribute[0]);
/*  72 */         Files.deleteIfExists(created);
/*  73 */         return baseDir.relativize(created).toString();
/*  74 */       } catch (FileAlreadyExistsException e) {
/*  75 */         count++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isPathNormalized(Path path) {
/*  81 */     Path normalized = path.normalize();
/*  82 */     return normalized.equals(path);
/*     */   }
/*     */   
/*     */   public static boolean isPathPortable(Path path) {
/*  86 */     for (Path part : path) {
/*  87 */       if (!isPathPartPortable(part.toString())) {
/*  88 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   
/*  96 */   public static boolean isPathPartPortable(String name) { return !RESERVED_WINDOWS_FILENAMES.matcher(name).matches(); }
/*     */ 
/*     */   
/*     */   public static Path createPathToResource(Path resourceDirectory, String resource, String extension) {
/* 100 */     String path = resource + resource;
/* 101 */     Path relativeResourcePath = Paths.get(path, new String[0]);
/*     */     
/* 103 */     if (relativeResourcePath.endsWith(extension)) {
/* 104 */       throw new InvalidPathException(path, "empty resource name");
/*     */     }
/*     */     
/* 107 */     return resourceDirectory.resolve(relativeResourcePath);
/*     */   }
/*     */ 
/*     */   
/* 111 */   public static String getFullResourcePath(String filename) { return FilenameUtils.getFullPath(filename).replace(File.separator, "/"); }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public static String normalizeResourcePath(String filename) { return FilenameUtils.normalize(filename).replace(File.separator, "/"); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataResult<List<String>> decomposePath(String path) {
/* 127 */     int segmentEnd = path.indexOf('/');
/* 128 */     if (segmentEnd == -1) {
/* 129 */       switch (path) { case "": case ".": case "..":  }  return 
/*     */ 
/*     */         
/* 132 */         !containsAllowedCharactersOnly(path) ? 
/* 133 */         DataResult.error(() -> "Invalid path '" + path + "'") : 
/*     */         
/* 135 */         DataResult.success(List.of(path));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 140 */     List<String> result = new ArrayList<String>();
/*     */     
/* 142 */     int segmentStart = 0;
/* 143 */     boolean lastSegment = false;
/*     */     while (true) {
/* 145 */       String segment = path.substring(segmentStart, segmentEnd);
/* 146 */       switch (segment) {
/*     */         case "":
/*     */         case ".":
/*     */         case "..":
/* 150 */           return DataResult.error(() -> "Invalid segment '" + segment + "' in path '" + path + "'");
/*     */       } 
/* 152 */       if (!containsAllowedCharactersOnly(segment)) {
/* 153 */         return DataResult.error(() -> "Invalid segment '" + segment + "' in path '" + path + "'");
/*     */       }
/* 155 */       result.add(segment);
/*     */ 
/*     */ 
/*     */       
/* 159 */       if (lastSegment) {
/* 160 */         return DataResult.success(result);
/*     */       }
/* 162 */       segmentStart = segmentEnd + 1;
/* 163 */       segmentEnd = path.indexOf('/', segmentStart);
/* 164 */       if (segmentEnd == -1) {
/* 165 */         segmentEnd = path.length();
/* 166 */         lastSegment = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Path resolvePath(Path root, List<String> segments) {
/* 173 */     int size = segments.size();
/* 174 */     switch (size) { case 0:
/*     */       
/*     */       case 1:
/*     */        }
/* 178 */      String[] rest = new String[size - 1];
/* 179 */     for (int i = 1; i < size; i++) {
/* 180 */       rest[i - 1] = (String)segments.get(i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 188 */   private static boolean containsAllowedCharactersOnly(String segment) { return STRICT_PATH_SEGMENT_CHECK.matcher(segment).matches(); }
/*     */ 
/*     */ 
/*     */   
/* 192 */   public static boolean isValidPathSegment(String segment) { return (!segment.equals("..") && !segment.equals(".") && containsAllowedCharactersOnly(segment)); }
/*     */ 
/*     */   
/*     */   public static void validatePath(String... path) {
/* 196 */     if (path.length == 0) {
/* 197 */       throw new IllegalArgumentException("Path must have at least one element");
/*     */     }
/* 199 */     for (String segment : path) {
/* 200 */       if (!isValidPathSegment(segment)) {
/* 201 */         throw new IllegalArgumentException("Illegal segment " + segment + " in path " + Arrays.toString(path));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 208 */   public static void createDirectoriesSafe(Path dir) throws IOException { Files.createDirectories(Files.exists(dir, new java.nio.file.LinkOption[0]) ? dir.toRealPath(new java.nio.file.LinkOption[0]) : dir, new java.nio.file.attribute.FileAttribute[0]); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\FileUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */