/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ public static enum PackType {
/*  4 */   CLIENT_RESOURCES("assets"),
/*  5 */   SERVER_DATA("data");
/*    */ 
/*    */   
/*    */   private final String directory;
/*    */ 
/*    */   
/* 11 */   PackType(String directory) { this.directory = directory; }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public String getDirectory() { return this.directory; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\PackType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */