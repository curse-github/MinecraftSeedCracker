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
/* 46 */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) { return output.accept(text); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style parentStyle) { return output.accept(style.applyTo(parentStyle), text); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\FormattedText$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */