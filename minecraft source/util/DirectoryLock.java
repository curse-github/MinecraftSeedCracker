/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.channels.FileChannel;
/*    */ import java.nio.channels.FileLock;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.AccessDeniedException;
/*    */ import java.nio.file.NoSuchFileException;
/*    */ import java.nio.file.OpenOption;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.StandardOpenOption;
/*    */ 
/*    */ public class DirectoryLock
/*    */   implements AutoCloseable {
/*    */   public static final String LOCK_FILE = "session.lock";
/*    */   private final FileChannel lockFile;
/*    */   private final FileLock lock;
/*    */   private static final ByteBuffer DUMMY;
/*    */   
/*    */   static  {
/* 22 */     chars = "☃".getBytes(StandardCharsets.UTF_8);
/* 23 */     DUMMY = ByteBuffer.allocateDirect(chars.length);
/* 24 */     DUMMY.put(chars);
/* 25 */     DUMMY.flip();
/*    */   }
/*    */   
/*    */   public static DirectoryLock create(Path dir) throws IOException {
/* 29 */     Path lockPath = dir.resolve("session.lock");
/*    */     
/* 31 */     FileUtil.createDirectoriesSafe(dir);
/* 32 */     FileChannel lockFile = FileChannel.open(lockPath, new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.WRITE });
/*    */     
/*    */     try {
/* 35 */       lockFile.write(DUMMY.duplicate());
/* 36 */       lockFile.force(true);
/* 37 */       FileLock lock = lockFile.tryLock();
/* 38 */       if (lock == null) {
/* 39 */         throw LockException.alreadyLocked(lockPath);
/*    */       }
/* 41 */       return new DirectoryLock(lockFile, lock);
/* 42 */     } catch (IOException e) {
/*    */       try {
/* 44 */         lockFile.close();
/* 45 */       } catch (IOException nested) {
/* 46 */         e.addSuppressed(nested);
/*    */       } 
/* 48 */       throw e;
/*    */     } 
/*    */   }
/*    */   
/*    */   private DirectoryLock(FileChannel lockFile, FileLock lock) {
/* 53 */     this.lockFile = lockFile;
/* 54 */     this.lock = lock;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/*    */     try {
/* 60 */       if (this.lock.isValid()) {
/* 61 */         this.lock.release();
/*    */       }
/*    */     } finally {
/* 64 */       if (this.lockFile.isOpen()) {
/* 65 */         this.lockFile.close();
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 71 */   public boolean isValid() { return this.lock.isValid(); }
/*    */ 
/*    */   
/*    */   public static boolean isLocked(Path dir) throws IOException {
/* 75 */     Path lockPath = dir.resolve("session.lock");
/*    */     
/* 77 */     try { FileChannel lockFile = FileChannel.open(lockPath, new OpenOption[] { StandardOpenOption.WRITE }); 
/* 78 */       try { FileLock maybeLock = lockFile.tryLock(); 
/* 79 */         try { boolean bool = (maybeLock == null);
/* 80 */           if (maybeLock != null) maybeLock.close();  if (lockFile != null) lockFile.close();  return bool; } catch (Throwable throwable) { if (maybeLock != null) try { maybeLock.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (lockFile != null) try { lockFile.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (AccessDeniedException e)
/* 81 */     { return true; }
/* 82 */     catch (NoSuchFileException e)
/* 83 */     { return false; }
/*    */   
/*    */   }
/*    */   
/*    */   public static class LockException
/*    */     extends IOException {
/* 89 */     private LockException(Path path, String message) { super(String.valueOf(path.toAbsolutePath()) + ": " + String.valueOf(path.toAbsolutePath())); }
/*    */ 
/*    */ 
/*    */     
/* 93 */     public static LockException alreadyLocked(Path path) { return new LockException(path, "already locked (possibly by other Minecraft instance?)"); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\DirectoryLock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */