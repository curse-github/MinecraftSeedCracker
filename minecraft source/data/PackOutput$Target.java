/*    */ package net.minecraft.data;
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
/*    */ public static enum Target
/*    */ {
/* 26 */   DATA_PACK("data"),
/* 27 */   RESOURCE_PACK("assets"),
/* 28 */   REPORTS("reports");
/*    */ 
/*    */   
/*    */   private final String directory;
/*    */ 
/*    */   
/* 34 */   Target(String directory) { this.directory = directory; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\PackOutput$Target.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */