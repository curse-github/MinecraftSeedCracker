/*     */ package net.minecraft.world.level.validation;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.stream.Stream;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PathAllowList implements PathMatcher {
/*  16 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final String COMMENT_PREFIX = "#";
/*     */   private final List<ConfigEntry> entries;
/*     */   private final Map<String, PathMatcher> compiledPaths;
/*     */   
/*     */   @FunctionalInterface
/*  21 */   public static interface EntryType { public static final EntryType FILESYSTEM = FileSystem::getPathMatcher; public static final EntryType PREFIX = (fileSystem, pattern) -> ();
/*     */     
/*     */     PathMatcher compile(FileSystem param1FileSystem, String param1String); }
/*     */   
/*     */   public static final class ConfigEntry extends Record { private final PathAllowList.EntryType type;
/*     */     private final String pattern;
/*     */     
/*  28 */     public ConfigEntry(PathAllowList.EntryType type, String pattern) { this.type = type; this.pattern = pattern; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  28 */       //   0	7	0	this	Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry; } public PathAllowList.EntryType type() { return this.type; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;
/*  28 */       //   0	8	1	o	Ljava/lang/Object; } public String pattern() { return this.pattern; }
/*     */     
/*  30 */     public PathMatcher compile(FileSystem fileSystem) { return type().compile(fileSystem, this.pattern); }
/*     */ 
/*     */     
/*     */     static Optional<ConfigEntry> parse(String definition) {
/*  34 */       if (definition.isBlank() || definition.startsWith("#")) {
/*  35 */         return Optional.empty();
/*     */       }
/*  37 */       if (!definition.startsWith("[")) {
/*  38 */         return Optional.of(new ConfigEntry(PathAllowList.EntryType.PREFIX, definition));
/*     */       }
/*     */       
/*  41 */       int split = definition.indexOf(']', 1);
/*  42 */       if (split == -1) {
/*  43 */         throw new IllegalArgumentException("Unterminated type in line '" + definition + "'");
/*     */       }
/*     */       
/*  46 */       String type = definition.substring(1, split);
/*  47 */       String contents = definition.substring(split + 1);
/*  48 */       switch (type) { case "glob": case "regex":
/*     */         
/*     */         case "prefix":
/*  51 */          }  throw new IllegalArgumentException("Unsupported definition type in line '" + definition + "'");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  56 */     static ConfigEntry glob(String pattern) { return new ConfigEntry(PathAllowList.EntryType.FILESYSTEM, "glob:" + pattern); }
/*     */ 
/*     */ 
/*     */     
/*  60 */     static ConfigEntry regex(String pattern) { return new ConfigEntry(PathAllowList.EntryType.FILESYSTEM, "regex:" + pattern); }
/*     */ 
/*     */ 
/*     */     
/*  64 */     static ConfigEntry prefix(String pattern) { return new ConfigEntry(PathAllowList.EntryType.PREFIX, pattern); } }
/*     */ 
/*     */ 
/*     */   
/*     */   public PathAllowList(List<ConfigEntry> entries) {
/*  69 */     this.compiledPaths = new ConcurrentHashMap();
/*     */ 
/*     */     
/*  72 */     this.entries = entries;
/*     */   }
/*     */   
/*     */   public PathMatcher getForFileSystem(FileSystem fileSystem) {
/*  76 */     return (PathMatcher)this.compiledPaths.computeIfAbsent(fileSystem.provider().getScheme(), scheme -> {
/*     */           List<PathMatcher> compiledMatchers;
/*     */ 
/*     */           
/*     */           try {
/*  81 */             compiledMatchers = this.entries.stream().map(()).toList();
/*  82 */           } catch (Exception e) {
/*  83 */             LOGGER.error("Failed to compile file pattern list", e);
/*  84 */             return ();
/*     */           } 
/*     */           
/*  87 */           switch (compiledMatchers.size()) { case 0: case 1:  }  return ();
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public boolean matches(Path path) { return getForFileSystem(path.getFileSystem()).matches(path); }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public static PathAllowList readPlain(BufferedReader reader) { return new PathAllowList(reader.lines().flatMap(line -> ConfigEntry.parse(line).stream()).toList()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\validation\PathAllowList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */