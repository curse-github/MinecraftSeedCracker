/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import net.minecraft.network.Connection;
/*    */ import net.minecraft.network.DisconnectionDetails;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
/*    */ import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
/*    */ import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
/*    */ import net.minecraft.network.protocol.status.ServerStatus;
/*    */ import net.minecraft.network.protocol.status.ServerStatusPacketListener;
/*    */ import net.minecraft.network.protocol.status.ServerboundStatusRequestPacket;
/*    */ 
/*    */ public class ServerStatusPacketListenerImpl implements ServerStatusPacketListener {
/* 14 */   private static final Component DISCONNECT_REASON = Component.translatable("multiplayer.status.request_handled");
/*    */   
/*    */   private final ServerStatus status;
/*    */   private final Connection connection;
/*    */   private boolean hasRequestedStatus;
/*    */   
/*    */   public ServerStatusPacketListenerImpl(ServerStatus status, Connection connection) {
/* 21 */     this.status = status;
/* 22 */     this.connection = connection;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisconnect(DisconnectionDetails details) {}
/*    */ 
/*    */ 
/*    */   
/* 32 */   public boolean isAcceptingMessages() { return this.connection.isConnected(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handleStatusRequest(ServerboundStatusRequestPacket packet) {
/* 37 */     if (this.hasRequestedStatus) {
/* 38 */       this.connection.disconnect(DISCONNECT_REASON);
/*    */       return;
/*    */     } 
/* 41 */     this.hasRequestedStatus = true;
/* 42 */     this.connection.send(new ClientboundStatusResponsePacket(this.status));
/*    */   }
/*    */ 
/*    */   
/*    */   public void handlePingRequest(ServerboundPingRequestPacket packet) {
/* 47 */     this.connection.send(new ClientboundPongResponsePacket(packet.getTime()));
/* 48 */     this.connection.disconnect(DISCONNECT_REASON);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\ServerStatusPacketListenerImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */