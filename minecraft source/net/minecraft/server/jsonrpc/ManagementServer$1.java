/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.http.HttpObjectAggregator;
/*     */ import io.netty.handler.codec.http.HttpServerCodec;
/*     */ import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
/*     */ import io.netty.handler.ssl.SslContext;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.websocket.JsonToWebSocketEncoder;
/*     */ import net.minecraft.server.jsonrpc.websocket.WebSocketToJsonCodec;
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
/*     */     try {
/*  89 */       channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/*  90 */     } catch (ChannelException channelException) {}
/*     */ 
/*     */     
/*  93 */     ChannelPipeline pipeline = channel.pipeline();
/*  94 */     if (sslContext != null) {
/*  95 */       pipeline.addLast(new ChannelHandler[] { sslContext.newHandler(channel.alloc()) });
/*     */     }
/*  97 */     pipeline.addLast(new ChannelHandler[] { new HttpServerCodec()
/*  98 */         }).addLast(new ChannelHandler[] { new HttpObjectAggregator(65536)
/*  99 */         }).addLast(new ChannelHandler[] { ManagementServer.this.authenticationHandler
/* 100 */         }).addLast(new ChannelHandler[] { new WebSocketServerProtocolHandler("/")
/*     */         
/* 102 */         }).addLast(new ChannelHandler[] { new WebSocketToJsonCodec()
/* 103 */         }).addLast(new ChannelHandler[] { new JsonToWebSocketEncoder()
/*     */         
/* 105 */         }).addLast(new ChannelHandler[] { new Connection(channel, ManagementServer.this, minecraftApi, jsonrpcLogger) });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\ManagementServer$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */