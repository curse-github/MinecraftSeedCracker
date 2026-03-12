/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.net.HostAndPort;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.bootstrap.ServerBootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.nio.NioEventLoopGroup;
/*     */ import io.netty.handler.codec.http.HttpObjectAggregator;
/*     */ import io.netty.handler.codec.http.HttpServerCodec;
/*     */ import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
/*     */ import io.netty.handler.logging.LogLevel;
/*     */ import io.netty.handler.logging.LoggingHandler;
/*     */ import io.netty.handler.ssl.SslContext;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.security.AuthenticationHandler;
/*     */ import net.minecraft.server.jsonrpc.websocket.JsonToWebSocketEncoder;
/*     */ import net.minecraft.server.jsonrpc.websocket.WebSocketToJsonCodec;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class ManagementServer
/*     */ {
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private final HostAndPort hostAndPort;
/*     */   private final AuthenticationHandler authenticationHandler;
/*     */   private Channel serverChannel;
/*     */   private final NioEventLoopGroup nioEventLoopGroup;
/*     */   private final Set<Connection> connections;
/*     */   
/*     */   public ManagementServer(HostAndPort hostAndPort, AuthenticationHandler authenticationHandler) {
/*  42 */     this.connections = Sets.newIdentityHashSet();
/*     */ 
/*     */     
/*  45 */     this.hostAndPort = hostAndPort;
/*  46 */     this.authenticationHandler = authenticationHandler;
/*  47 */     this
/*     */ 
/*     */       
/*  50 */       .nioEventLoopGroup = new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Management server IO #%d").setDaemon(true).build());
/*     */   }
/*     */   public ManagementServer(HostAndPort hostAndPort, AuthenticationHandler authenticationHandler, NioEventLoopGroup nioEventLoopGroup) {
/*     */     this.connections = Sets.newIdentityHashSet();
/*  54 */     this.hostAndPort = hostAndPort;
/*  55 */     this.authenticationHandler = authenticationHandler;
/*  56 */     this.nioEventLoopGroup = nioEventLoopGroup;
/*     */   }
/*     */   
/*     */   public void onConnected(Connection connection) {
/*  60 */     synchronized (this.connections) {
/*  61 */       this.connections.add(connection);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onDisconnected(Connection connection) {
/*  66 */     synchronized (this.connections) {
/*  67 */       this.connections.remove(connection);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  72 */   public void startWithoutTls(MinecraftApi minecraftApi) { start(minecraftApi, null); }
/*     */ 
/*     */ 
/*     */   
/*  76 */   public void startWithTls(MinecraftApi minecraftApi, SslContext sslContext) { start(minecraftApi, sslContext); }
/*     */ 
/*     */   
/*     */   private void start(final MinecraftApi minecraftApi, final SslContext sslContext) {
/*  80 */     final JsonRpcLogger jsonrpcLogger = new JsonRpcLogger();
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
/* 110 */     ChannelFuture channel = ((ServerBootstrap)((ServerBootstrap)((ServerBootstrap)(new ServerBootstrap()).handler(new LoggingHandler(LogLevel.DEBUG))).channel(io.netty.channel.socket.nio.NioServerSocketChannel.class)).childHandler(new ChannelInitializer<Channel>() { protected void initChannel(Channel channel) { try { channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true)); } catch (ChannelException channelException) {} ChannelPipeline pipeline = channel.pipeline(); if (sslContext != null) pipeline.addLast(new ChannelHandler[] { sslContext.newHandler(channel.alloc()) });  pipeline.addLast(new ChannelHandler[] { new HttpServerCodec() }).addLast(new ChannelHandler[] { new HttpObjectAggregator(65536) }).addLast(new ChannelHandler[] { ManagementServer.this.authenticationHandler }).addLast(new ChannelHandler[] { new WebSocketServerProtocolHandler("/") }).addLast(new ChannelHandler[] { new WebSocketToJsonCodec() }).addLast(new ChannelHandler[] { new JsonToWebSocketEncoder() }).addLast(new ChannelHandler[] { new Connection(channel, ManagementServer.this, minecraftApi, jsonrpcLogger) }); } }).group(this.nioEventLoopGroup).localAddress(this.hostAndPort.getHost(), this.hostAndPort.getPort())).bind();
/* 111 */     this.serverChannel = channel.channel();
/* 112 */     channel.syncUninterruptibly();
/*     */     
/* 114 */     LOGGER.info("Json-RPC Management connection listening on {}:{}", this.hostAndPort.getHost(), Integer.valueOf(getPort()));
/*     */   }
/*     */   
/*     */   public void stop(boolean closeNioEventLoopGroup) throws InterruptedException {
/* 118 */     if (this.serverChannel != null) {
/* 119 */       this.serverChannel.close().sync();
/* 120 */       this.serverChannel = null;
/*     */     } 
/* 122 */     this.connections.clear();
/*     */     
/* 124 */     if (closeNioEventLoopGroup) {
/* 125 */       this.nioEventLoopGroup.shutdownGracefully().sync();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 130 */   public void tick() { forEachConnection(Connection::tick); }
/*     */ 
/*     */ 
/*     */   
/* 134 */   public int getPort() { return (this.serverChannel != null) ? ((InetSocketAddress)this.serverChannel.localAddress()).getPort() : this.hostAndPort.getPort(); }
/*     */ 
/*     */   
/*     */   void forEachConnection(Consumer<Connection> action) {
/* 138 */     synchronized (this.connections) {
/* 139 */       this.connections.forEach(action);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\ManagementServer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */