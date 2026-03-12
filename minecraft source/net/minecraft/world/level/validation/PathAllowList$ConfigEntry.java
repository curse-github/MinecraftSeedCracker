/*    */ package net.minecraft.world.level.validation;
/*    */ 
/*    */ import java.nio.file.FileSystem;
/*    */ import java.nio.file.PathMatcher;
/*    */ import java.util.Optional;
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
/*    */ public final class ConfigEntry
/*    */   extends Record
/*    */ {
/*    */   private final PathAllowList.EntryType type;
/*    */   private final String pattern;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/validation/PathAllowList$ConfigEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 28 */   public ConfigEntry(PathAllowList.EntryType type, String pattern) { this.type = type; this.pattern = pattern; } public PathAllowList.EntryType type() { return this.type; } public String pattern() { return this.pattern; }
/*    */   
/* 30 */   public PathMatcher compile(FileSystem fileSystem) { return type().compile(fileSystem, this.pattern); }
/*    */ 
/*    */   
/*    */   static Optional<ConfigEntry> parse(String definition) {
/* 34 */     if (definition.isBlank() || definition.startsWith("#")) {
/* 35 */       return Optional.empty();
/*    */     }
/* 37 */     if (!definition.startsWith("[")) {
/* 38 */       return Optional.of(new ConfigEntry(PathAllowList.EntryType.PREFIX, definition));
/*    */     }
/*    */     
/* 41 */     int split = definition.indexOf(']', 1);
/* 42 */     if (split == -1) {
/* 43 */       throw new IllegalArgumentException("Unterminated type in line '" + definition + "'");
/*    */     }
/*    */     
/* 46 */     String type = definition.substring(1, split);
/* 47 */     String contents = definition.substring(split + 1);
/* 48 */     switch (type) { case "glob": case "regex":
/*    */       
/*    */       case "prefix":
/* 51 */        }  throw new IllegalArgumentException("Unsupported definition type in line '" + definition + "'");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 56 */   static ConfigEntry glob(String pattern) { return new ConfigEntry(PathAllowList.EntryType.FILESYSTEM, "glob:" + pattern); }
/*    */ 
/*    */ 
/*    */   
/* 60 */   static ConfigEntry regex(String pattern) { return new ConfigEntry(PathAllowList.EntryType.FILESYSTEM, "regex:" + pattern); }
/*    */ 
/*    */ 
/*    */   
/* 64 */   static ConfigEntry prefix(String pattern) { return new ConfigEntry(PathAllowList.EntryType.PREFIX, pattern); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\validation\PathAllowList$ConfigEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */