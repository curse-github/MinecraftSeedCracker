/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.Comparator;
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
/*    */ final class PathAndPriority
/*    */   extends Record
/*    */ {
/*    */   private final Path path;
/*    */   private final int removalPriority;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/DownloadCacheCleaner$PathAndPriority;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 31 */   private PathAndPriority(Path path, int removalPriority) { this.path = path; this.removalPriority = removalPriority; } public Path path() { return this.path; } public int removalPriority() { return this.removalPriority; }
/* 32 */   public static final Comparator<PathAndPriority> HIGHEST_PRIORITY_FIRST = Comparator.comparing(PathAndPriority::removalPriority).reversed();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\DownloadCacheCleaner$PathAndPriority.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */