/*   */ package net.minecraft.network.protocol.login;
/*   */ 
/*   */ import net.minecraft.network.ConnectionProtocol;
/*   */ import net.minecraft.network.protocol.cookie.ClientCookiePacketListener;
/*   */ 
/*   */ public interface ClientLoginPacketListener
/*   */   extends ClientCookiePacketListener
/*   */ {
/* 9 */   default ConnectionProtocol protocol() { return ConnectionProtocol.LOGIN; }
/*   */   
/*   */   void handleHello(ClientboundHelloPacket paramClientboundHelloPacket);
/*   */   
/*   */   void handleLoginFinished(ClientboundLoginFinishedPacket paramClientboundLoginFinishedPacket);
/*   */   
/*   */   void handleDisconnect(ClientboundLoginDisconnectPacket paramClientboundLoginDisconnectPacket);
/*   */   
/*   */   void handleCompression(ClientboundLoginCompressionPacket paramClientboundLoginCompressionPacket);
/*   */   
/*   */   void handleCustomQuery(ClientboundCustomQueryPacket paramClientboundCustomQueryPacket);
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\login\ClientLoginPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */