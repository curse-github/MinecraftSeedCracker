/*    */ package net.minecraft.server.packs.linkfs;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.Map;
/*    */ 
/*    */ interface PathContents {
/*  7 */   public static final PathContents MISSING = new PathContents()
/*    */     {
/*    */       public String toString() {
/* 10 */         return "empty";
/*    */       }
/*    */     };
/*    */   
/* 14 */   public static final PathContents RELATIVE = new PathContents()
/*    */     {
/*    */       public String toString() {
/* 17 */         return "relative"; }
/*    */     }; public static final class FileContents extends Record implements PathContents { private final Path contents;
/*    */     public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/linkfs/PathContents$FileContents;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/packs/linkfs/PathContents$FileContents; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/linkfs/PathContents$FileContents;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/packs/linkfs/PathContents$FileContents; }
/* 21 */     public FileContents(Path contents) { this.contents = contents; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/linkfs/PathContents$FileContents;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/packs/linkfs/PathContents$FileContents;
/* 21 */       //   0	8	1	o	Ljava/lang/Object; } public Path contents() { return this.contents; } }
/*    */   public static final class DirectoryContents extends Record implements PathContents { private final Map<String, LinkFSPath> children;
/*    */     
/* 24 */     public DirectoryContents(Map<String, LinkFSPath> children) { this.children = children; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/linkfs/PathContents$DirectoryContents;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/packs/linkfs/PathContents$DirectoryContents; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/linkfs/PathContents$DirectoryContents;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/packs/linkfs/PathContents$DirectoryContents; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/linkfs/PathContents$DirectoryContents;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/packs/linkfs/PathContents$DirectoryContents;
/* 24 */       //   0	8	1	o	Ljava/lang/Object; } public Map<String, LinkFSPath> children() { return this.children; } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\linkfs\PathContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */