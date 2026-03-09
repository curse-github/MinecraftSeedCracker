/*    */ package net.minecraft.world.level.validation;
/*    */ 
/*    */ import java.nio.file.FileSystem;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.PathMatcher;
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
/*    */ @FunctionalInterface
/*    */ public interface EntryType
/*    */ {
/* 21 */   public static final EntryType FILESYSTEM = FileSystem::getPathMatcher;
/*    */   public static final EntryType PREFIX = (fileSystem, pattern) -> ();
/*    */   
/*    */   PathMatcher compile(FileSystem paramFileSystem, String paramString);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\validation\PathAllowList$EntryType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */