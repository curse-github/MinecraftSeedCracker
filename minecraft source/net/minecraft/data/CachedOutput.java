/*    */ package net.minecraft.data;
/*    */ 
/*    */ import com.google.common.hash.HashCode;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.util.FileUtil;
/*    */ 
/*    */ public interface CachedOutput
/*    */ {
/*    */   public static final CachedOutput NO_CACHE = (path, input, hash) -> {
/* 12 */       FileUtil.createDirectoriesSafe(path.getParent());
/* 13 */       Files.write(path, input, new java.nio.file.OpenOption[0]);
/*    */     };
/*    */   
/*    */   void writeIfNeeded(Path paramPath, byte[] paramArrayOfByte, HashCode paramHashCode) throws IOException;
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\CachedOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */