/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.protocol.game.ServerPacketListener;
/*    */ import net.minecraft.network.protocol.ping.ServerPingPacketListener;
/*    */ 
/*    */ public interface ServerStatusPacketListener
/*    */   extends ServerPacketListener, ServerPingPacketListener
/*    */ {
/* 10 */   default ConnectionProtocol protocol() { return ConnectionProtocol.STATUS; }
/*    */   
/*    */   void handleStatusRequest(ServerboundStatusRequestPacket paramServerboundStatusRequestPacket);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\status\ServerStatusPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */