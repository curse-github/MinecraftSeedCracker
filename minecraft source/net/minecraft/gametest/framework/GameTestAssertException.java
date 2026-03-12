/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class GameTestAssertException extends GameTestException {
/*    */   protected final Component message;
/*    */   protected final int tick;
/*    */   
/*    */   public GameTestAssertException(Component message, int tick) {
/* 10 */     super(message.getString());
/* 11 */     this.message = message;
/* 12 */     this.tick = tick;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public Component getDescription() { return Component.translatable("test.error.tick", new Object[] { this.message, Integer.valueOf(this.tick) }); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public String getMessage() { return getDescription().getString(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestAssertException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */