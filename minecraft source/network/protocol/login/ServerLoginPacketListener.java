/*   */ package net.minecraft.network.protocol.login;
/*   */ 
/*   */ import net.minecraft.network.ConnectionProtocol;
/*   */ import net.minecraft.network.protocol.cookie.ServerCookiePacketListener;
/*   */ 
/*   */ public interface ServerLoginPacketListener
/*   */   extends ServerCookiePacketListener
/*   */ {
/* 9 */   default ConnectionProtocol protocol() { return ConnectionProtocol.LOGIN; }
/*   */   
/*   */   void handleHello(ServerboundHelloPacket paramServerboundHelloPacket);
/*   */   
/*   */   void handleKey(ServerboundKeyPacket paramServerboundKeyPacket);
/*   */   
/*   */   void handleCustomQueryPacket(ServerboundCustomQueryAnswerPacket paramServerboundCustomQueryAnswerPacket);
/*   */   
/*   */   void handleLoginAcknowledgement(ServerboundLoginAcknowledgedPacket paramServerboundLoginAcknowledgedPacket);
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\login\ServerLoginPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */