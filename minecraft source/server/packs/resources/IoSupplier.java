/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.zip.ZipEntry;
/*    */ import java.util.zip.ZipFile;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface IoSupplier<T>
/*    */ {
/* 13 */   static IoSupplier<InputStream> create(Path path) { return () -> Files.newInputStream(path, new java.nio.file.OpenOption[0]); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   static IoSupplier<InputStream> create(ZipFile zipFile, ZipEntry entry) { return () -> zipFile.getInputStream(entry); }
/*    */   
/*    */   T get() throws IOException;
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\IoSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */