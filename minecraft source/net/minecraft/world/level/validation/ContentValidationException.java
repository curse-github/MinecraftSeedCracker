/*    */ package net.minecraft.world.level.validation;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ public class ContentValidationException extends Exception {
/*    */   private final Path directory;
/*    */   private final List<ForbiddenSymlinkInfo> entries;
/*    */   
/*    */   public ContentValidationException(Path directory, List<ForbiddenSymlinkInfo> entries) {
/* 12 */     this.directory = directory;
/* 13 */     this.entries = entries;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public String getMessage() { return getMessage(this.directory, this.entries); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static String getMessage(Path directory, List<ForbiddenSymlinkInfo> entries) { return "Failed to validate '" + String.valueOf(directory) + "'. Found forbidden symlinks: " + (String)entries.stream().map(e -> String.valueOf(e.link()) + "->" + String.valueOf(e.link())).collect(Collectors.joining(", ")); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\validation\ContentValidationException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */