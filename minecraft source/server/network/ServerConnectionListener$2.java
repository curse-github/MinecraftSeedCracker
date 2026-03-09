/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.protocol.PacketFlow;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   extends ChannelInitializer<Channel>
/*     */ {
/*     */   protected void initChannel(Channel channel) {
/* 100 */     Connection connection = new Connection(PacketFlow.SERVERBOUND);
/* 101 */     connection.setListenerForServerboundHandshake(new MemoryServerHandshakePacketListenerImpl(ServerConnectionListener.this.server, connection));
/* 102 */     ServerConnectionListener.this.connections.add(connection);
/*     */     
/* 104 */     ChannelPipeline pipeline = channel.pipeline();
/*     */     
/* 106 */     Connection.configureInMemoryPipeline(pipeline, PacketFlow.SERVERBOUND);
/*     */     
/* 108 */     if (SharedConstants.DEBUG_FAKE_LATENCY_MS > 0) {
/* 109 */       pipeline.addLast("latency", new ServerConnectionListener.LatencySimulator(SharedConstants.DEBUG_FAKE_LATENCY_MS, SharedConstants.DEBUG_FAKE_JITTER_MS));
/*     */     }
/* 111 */     connection.configurePacketHandler(pipeline);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\ServerConnectionListener$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */