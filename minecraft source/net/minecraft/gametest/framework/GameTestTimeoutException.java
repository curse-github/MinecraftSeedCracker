/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class GameTestTimeoutException extends GameTestException {
/*    */   protected final Component message;
/*    */   
/*    */   public GameTestTimeoutException(Component message) {
/*  9 */     super(message.getString());
/* 10 */     this.message = message;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public Component getDescription() { return this.message; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestTimeoutException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */