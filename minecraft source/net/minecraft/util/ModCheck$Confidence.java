/*    */ package net.minecraft.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public static enum Confidence
/*    */ {
/* 13 */   PROBABLY_NOT("Probably not.", false),
/* 14 */   VERY_LIKELY("Very likely;", true),
/* 15 */   DEFINITELY("Definitely;", true);
/*    */   
/*    */   private final String description;
/*    */   
/*    */   private final boolean shouldReportAsModified;
/*    */   
/*    */   Confidence(String description, boolean shouldReportAsModified) {
/* 22 */     this.description = description;
/* 23 */     this.shouldReportAsModified = shouldReportAsModified;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\ModCheck$Confidence.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */