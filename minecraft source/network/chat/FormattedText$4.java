/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
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
/*    */   implements FormattedText
/*    */ {
/*    */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 64 */     for (FormattedText part : parts) {
/* 65 */       Optional<T> result = part.visit(output);
/* 66 */       if (result.isPresent()) {
/* 67 */         return result;
/*    */       }
/*    */     } 
/*    */     
/* 71 */     return Optional.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style parentStyle) {
/* 76 */     for (FormattedText part : parts) {
/* 77 */       Optional<T> result = part.visit(output, parentStyle);
/* 78 */       if (result.isPresent()) {
/* 79 */         return result;
/*    */       }
/*    */     } 
/*    */     
/* 83 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\FormattedText$4.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */