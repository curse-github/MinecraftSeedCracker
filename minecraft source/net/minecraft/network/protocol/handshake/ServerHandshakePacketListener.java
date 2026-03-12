/*   */ package net.minecraft.network.protocol.handshake;
/*   */ 
/*   */ import net.minecraft.network.ConnectionProtocol;
/*   */ import net.minecraft.network.protocol.game.ServerPacketListener;
/*   */ 
/*   */ public interface ServerHandshakePacketListener
/*   */   extends ServerPacketListener
/*   */ {
/* 9 */   default ConnectionProtocol protocol() { return ConnectionProtocol.HANDSHAKING; }
/*   */   
/*   */   void handleIntention(ClientIntentionPacket paramClientIntentionPacket);
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\handshake\ServerHandshakePacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */