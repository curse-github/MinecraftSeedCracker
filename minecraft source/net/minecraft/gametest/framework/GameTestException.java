/*   */ package net.minecraft.gametest.framework;
/*   */ 
/*   */ import net.minecraft.network.chat.Component;
/*   */ 
/*   */ public abstract class GameTestException
/*   */   extends RuntimeException {
/* 7 */   public GameTestException(String message) { super(message); }
/*   */   
/*   */   public abstract Component getDescription();
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */