/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.ChannelException;
/*    */ import io.netty.channel.ChannelInitializer;
/*    */ import io.netty.channel.ChannelOption;
/*    */ import io.netty.channel.ChannelPipeline;
/*    */ import io.netty.handler.timeout.ReadTimeoutHandler;
/*    */ import net.minecraft.network.Connection;
/*    */ import net.minecraft.network.RateKickingConnection;
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends ChannelInitializer<Channel>
/*    */ {
/*    */   protected void initChannel(Channel channel) {
/*    */     try {
/* 64 */       channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/* 65 */     } catch (ChannelException channelException) {}
/*    */ 
/*    */ 
/*    */     
/* 69 */     ChannelPipeline pipeline = channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30));
/*    */     
/* 71 */     if (ServerConnectionListener.this.server.repliesToStatus()) {
/* 72 */       pipeline.addLast("legacy_query", new LegacyQueryHandler(ServerConnectionListener.this.getServer()));
/*    */     }
/*    */     
/* 75 */     Connection.configureSerialization(pipeline, PacketFlow.SERVERBOUND, false, null);
/*    */     
/* 77 */     int rateLimitPacketsPerSecond = ServerConnectionListener.this.server.getRateLimitPacketsPerSecond();
/* 78 */     RateKickingConnection rateKickingConnection = (rateLimitPacketsPerSecond > 0) ? new RateKickingConnection(rateLimitPacketsPerSecond) : new Connection(PacketFlow.SERVERBOUND);
/* 79 */     ServerConnectionListener.this.connections.add(rateKickingConnection);
/* 80 */     rateKickingConnection.configurePacketHandler(pipeline);
/* 81 */     rateKickingConnection.setListenerForServerboundHandshake(new ServerHandshakePacketListenerImpl(ServerConnectionListener.this.server, rateKickingConnection));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\ServerConnectionListener$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */