/*    */ package net.minecraft.network.chat;
/*    */ 
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
/*    */ class null
/*    */   implements FormattedText
/*    */ {
/* 32 */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) { return output.accept(text); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style parentStyle) { return output.accept(parentStyle, text); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\FormattedText$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */