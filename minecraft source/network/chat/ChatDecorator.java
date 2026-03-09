/*   */ package net.minecraft.network.chat;
/*   */ 
/*   */ import net.minecraft.server.level.ServerPlayer;
/*   */ 
/*   */ @FunctionalInterface
/*   */ public interface ChatDecorator
/*   */ {
/* 8 */   public static final ChatDecorator PLAIN = (player, plain) -> plain;
/*   */   
/*   */   Component decorate(ServerPlayer paramServerPlayer, Component paramComponent);
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\ChatDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */