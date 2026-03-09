/*    */ package net.minecraft.server.packs.linkfs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.FileStore;
/*    */ import java.nio.file.attribute.FileAttributeView;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class LinkFSFileStore
/*    */   extends FileStore
/*    */ {
/*    */   private final String name;
/*    */   
/* 15 */   public LinkFSFileStore(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public String name() { return this.name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public String type() { return "index"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public boolean isReadOnly() { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public long getTotalSpace() { return 0L; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public long getUsableSpace() { return 0L; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public long getUnallocatedSpace() { return 0L; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public boolean supportsFileAttributeView(Class<? extends FileAttributeView> type) { return (type == java.nio.file.attribute.BasicFileAttributeView.class); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public boolean supportsFileAttributeView(String name) { return "basic".equals(name); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public <V extends java.nio.file.attribute.FileStoreAttributeView> V getFileStoreAttributeView(Class<V> type) { return null; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   public Object getAttribute(String attribute) throws IOException { throw new UnsupportedOperationException(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\linkfs\LinkFSFileStore.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */