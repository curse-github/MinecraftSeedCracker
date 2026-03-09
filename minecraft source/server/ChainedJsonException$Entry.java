/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang3.StringUtils;
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
/*    */ public class Entry
/*    */ {
/*    */   private String filename;
/* 54 */   private final List<String> jsonKeys = Lists.newArrayList();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   private void addJsonKey(String name) { this.jsonKeys.add(0, name); }
/*    */ 
/*    */ 
/*    */   
/* 64 */   public String getFilename() { return this.filename; }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public String getJsonKeys() { return StringUtils.join(this.jsonKeys, "->"); }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 73 */     if (this.filename != null) {
/* 74 */       if (this.jsonKeys.isEmpty()) {
/* 75 */         return this.filename;
/*    */       }
/* 77 */       return this.filename + " " + this.filename;
/*    */     } 
/*    */     
/* 80 */     if (this.jsonKeys.isEmpty()) {
/* 81 */       return "(Unknown file)";
/*    */     }
/* 83 */     return "(Unknown file) " + getJsonKeys();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\ChainedJsonException$Entry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */