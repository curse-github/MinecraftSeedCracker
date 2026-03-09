/*    */ package net.minecraft.commands;
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
/*    */ public class TextCoordinates
/*    */ {
/* 35 */   public static final TextCoordinates DEFAULT_LOCAL = new TextCoordinates("^", "^", "^");
/*    */   
/* 37 */   public static final TextCoordinates DEFAULT_GLOBAL = new TextCoordinates("~", "~", "~");
/*    */   
/*    */   public final String x;
/*    */   
/*    */   public final String y;
/*    */   
/*    */   public final String z;
/*    */   
/*    */   public TextCoordinates(String x, String y, String z) {
/* 46 */     this.x = x;
/* 47 */     this.y = y;
/* 48 */     this.z = z;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\SharedSuggestionProvider$TextCoordinates.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */