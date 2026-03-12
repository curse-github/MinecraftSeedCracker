/*    */ package net.minecraft.server.packs.linkfs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.DirectoryIteratorException;
/*    */ import java.nio.file.DirectoryStream;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Iterator;
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
/*    */ class null
/*    */   extends Object
/*    */   implements DirectoryStream<Path>
/*    */ {
/*    */   null(LinkFSProvider this$0) {}
/*    */   
/* 82 */   public Iterator<Path> iterator() { return directoryContents.children().values()
/* 83 */       .stream()
/* 84 */       .filter(path -> {
/*    */           try {
/* 86 */             return filter.accept(path);
/* 87 */           } catch (IOException e) {
/* 88 */             throw new DirectoryIteratorException(e);
/*    */           }
/*    */         
/* 91 */         }).map(path -> path)
/* 92 */       .iterator(); }
/*    */   
/*    */   public void close() {}
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\linkfs\LinkFSProvider$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */