/*    */ package net.minecraft.locale;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ import net.minecraft.util.FormattedCharSink;
/*    */ import net.minecraft.util.StringDecomposer;
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
/*    */   extends Language
/*    */ {
/* 56 */   public String getOrDefault(String elementId, String defaultValue) { return (String)storage.getOrDefault(elementId, defaultValue); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   public boolean has(String elementId) { return storage.containsKey(elementId); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 66 */   public boolean isDefaultRightToLeft() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FormattedCharSequence getVisualOrder(FormattedText logicalOrderText) {
/* 72 */     return output -> logicalOrderText.visit((), Style.EMPTY)
/*    */       
/* 74 */       .isPresent();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\locale\Language$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */