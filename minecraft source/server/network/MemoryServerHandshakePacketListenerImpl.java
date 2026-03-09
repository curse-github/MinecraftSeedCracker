/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import net.minecraft.network.Connection;
/*    */ import net.minecraft.network.DisconnectionDetails;
/*    */ import net.minecraft.network.protocol.handshake.ClientIntent;
/*    */ import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
/*    */ import net.minecraft.network.protocol.handshake.ServerHandshakePacketListener;
/*    */ import net.minecraft.network.protocol.login.LoginProtocols;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public class MemoryServerHandshakePacketListenerImpl implements ServerHandshakePacketListener {
/*    */   private final MinecraftServer server;
/*    */   private final Connection connection;
/*    */   
/*    */   public MemoryServerHandshakePacketListenerImpl(MinecraftServer server, Connection connection) {
/* 16 */     this.server = server;
/* 17 */     this.connection = connection;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleIntention(ClientIntentionPacket packet) {
/* 22 */     if (packet.intention() != ClientIntent.LOGIN) {
/* 23 */       throw new UnsupportedOperationException("Invalid intention " + String.valueOf(packet.intention()));
/*    */     }
/* 25 */     this.connection.setupInboundProtocol(LoginProtocols.SERVERBOUND, new ServerLoginPacketListenerImpl(this.server, this.connection, false));
/*    */     
/* 27 */     this.connection.setupOutboundProtocol(LoginProtocols.CLIENTBOUND);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisconnect(DisconnectionDetails details) {}
/*    */ 
/*    */ 
/*    */   
/* 36 */   public boolean isAcceptingMessages() { return this.connection.isConnected(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\MemoryServerHandshakePacketListenerImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */