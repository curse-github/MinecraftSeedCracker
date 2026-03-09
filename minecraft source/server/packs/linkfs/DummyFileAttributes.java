/*    */ package net.minecraft.server.packs.linkfs;
/*    */ 
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.nio.file.attribute.FileTime;
/*    */ 
/*    */ abstract class DummyFileAttributes
/*    */   implements BasicFileAttributes
/*    */ {
/*  9 */   private static final FileTime EPOCH = FileTime.fromMillis(0L);
/*    */ 
/*    */ 
/*    */   
/* 13 */   public FileTime lastModifiedTime() { return EPOCH; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public FileTime lastAccessTime() { return EPOCH; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public FileTime creationTime() { return EPOCH; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public boolean isSymbolicLink() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public boolean isOther() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public long size() { return 0L; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public Object fileKey() { return null; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\linkfs\DummyFileAttributes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */