/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class ChainedJsonException extends IOException {
/*    */   private final List<Entry> entries;
/*    */   
/*    */   public ChainedJsonException(String message) {
/* 12 */     this.entries = Lists.newArrayList();
/*    */ 
/*    */ 
/*    */     
/* 16 */     this.entries.add(new Entry());
/* 17 */     this.message = message;
/*    */   }
/*    */   private final String message;
/*    */   public ChainedJsonException(String message, Throwable cause) {
/* 21 */     super(cause); this.entries = Lists.newArrayList();
/* 22 */     this.entries.add(new Entry());
/* 23 */     this.message = message;
/*    */   }
/*    */ 
/*    */   
/* 27 */   public void prependJsonKey(String key) { ((Entry)this.entries.get(0)).addJsonKey(key); }
/*    */ 
/*    */   
/*    */   public void setFilenameAndFlush(String filename) {
/* 31 */     ((Entry)this.entries.get(0)).filename = filename;
/* 32 */     this.entries.add(0, new Entry());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public String getMessage() { return "Invalid " + String.valueOf(this.entries.get(this.entries.size() - 1)) + ": " + this.message; }
/*    */ 
/*    */   
/*    */   public static ChainedJsonException forException(Exception e) {
/* 41 */     if (e instanceof ChainedJsonException) {
/* 42 */       return (ChainedJsonException)e;
/*    */     }
/* 44 */     String message = e.getMessage();
/* 45 */     if (e instanceof java.io.FileNotFoundException) {
/* 46 */       message = "File not found";
/*    */     }
/* 48 */     return new ChainedJsonException(message, e);
/*    */   }
/*    */   
/*    */   public static class Entry
/*    */   {
/*    */     private String filename;
/* 54 */     private final List<String> jsonKeys = Lists.newArrayList();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 60 */     private void addJsonKey(String name) { this.jsonKeys.add(0, name); }
/*    */ 
/*    */ 
/*    */     
/* 64 */     public String getFilename() { return this.filename; }
/*    */ 
/*    */ 
/*    */     
/* 68 */     public String getJsonKeys() { return StringUtils.join(this.jsonKeys, "->"); }
/*    */ 
/*    */ 
/*    */     
/*    */     public String toString() {
/* 73 */       if (this.filename != null) {
/* 74 */         if (this.jsonKeys.isEmpty()) {
/* 75 */           return this.filename;
/*    */         }
/* 77 */         return this.filename + " " + this.filename;
/*    */       } 
/*    */       
/* 80 */       if (this.jsonKeys.isEmpty()) {
/* 81 */         return "(Unknown file)";
/*    */       }
/* 83 */       return "(Unknown file) " + getJsonKeys();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\ChainedJsonException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */