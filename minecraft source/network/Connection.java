/*     */ package net.minecraft.network;
/*     */ 
/*     */ import com.google.common.collect.Queues;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.bootstrap.Bootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandler;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelOutboundHandler;
/*     */ import io.netty.channel.ChannelOutboundHandlerAdapter;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.SimpleChannelInboundHandler;
/*     */ import io.netty.handler.flow.FlowControlHandler;
/*     */ import io.netty.handler.timeout.ReadTimeoutHandler;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.function.Consumer;
/*     */ import javax.crypto.Cipher;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.protocol.BundlerInfo;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketFlow;
/*     */ import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
/*     */ import net.minecraft.network.protocol.handshake.ClientIntent;
/*     */ import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
/*     */ import net.minecraft.network.protocol.handshake.HandshakeProtocols;
/*     */ import net.minecraft.network.protocol.handshake.ServerHandshakePacketListener;
/*     */ import net.minecraft.network.protocol.login.ClientLoginPacketListener;
/*     */ import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
/*     */ import net.minecraft.network.protocol.login.LoginProtocols;
/*     */ import net.minecraft.network.protocol.status.ClientStatusPacketListener;
/*     */ import net.minecraft.network.protocol.status.StatusProtocols;
/*     */ import net.minecraft.server.RunningOnDifferentThreadException;
/*     */ import net.minecraft.server.network.EventLoopGroupHolder;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.debugchart.LocalSampleLogger;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.MarkerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Connection
/*     */   extends SimpleChannelInboundHandler<Packet<?>>
/*     */ {
/*     */   private static final float AVERAGE_PACKETS_SMOOTHING = 0.75F;
/*  61 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  63 */   public static final Marker ROOT_MARKER = MarkerFactory.getMarker("NETWORK");
/*     */   
/*  65 */   public static final Marker PACKET_MARKER = (Marker)Util.make(MarkerFactory.getMarker("NETWORK_PACKETS"), m -> m.add(ROOT_MARKER));
/*     */   
/*  67 */   public static final Marker PACKET_RECEIVED_MARKER = (Marker)Util.make(MarkerFactory.getMarker("PACKET_RECEIVED"), m -> m.add(PACKET_MARKER));
/*  68 */   public static final Marker PACKET_SENT_MARKER = (Marker)Util.make(MarkerFactory.getMarker("PACKET_SENT"), m -> m.add(PACKET_MARKER));
/*     */   
/*  70 */   private static final ProtocolInfo<ServerHandshakePacketListener> INITIAL_PROTOCOL = HandshakeProtocols.SERVERBOUND; private final PacketFlow receiving; private final Queue<Consumer<Connection>> pendingActions; private Channel channel; private SocketAddress address; private DisconnectionDetails disconnectionDetails; private boolean encrypted;
/*     */   private boolean disconnectionHandled;
/*     */   
/*     */   public Connection(PacketFlow receiving) {
/*  74 */     this.sendLoginDisconnect = true;
/*  75 */     this.pendingActions = Queues.newConcurrentLinkedQueue();
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
/*  93 */     this.receiving = receiving;
/*     */   }
/*     */   private int receivedPackets; private int sentPackets; private float averageReceivedPackets; private float averageSentPackets; private int tickCount; private boolean handlingFault; private BandwidthDebugMonitor bandwidthDebugMonitor;
/*     */   
/*     */   public void channelActive(ChannelHandlerContext ctx) throws Exception {
/*  98 */     super.channelActive(ctx);
/*  99 */     this.channel = ctx.channel();
/* 100 */     this.address = this.channel.remoteAddress();
/* 101 */     if (this.delayedDisconnect != null) {
/* 102 */       disconnect(this.delayedDisconnect);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public void channelInactive(ChannelHandlerContext ctx) throws Exception { disconnect(Component.translatable("disconnect.endOfStream")); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
/* 113 */     if (cause instanceof SkipPacketException) {
/* 114 */       LOGGER.debug("Skipping packet due to errors", cause.getCause());
/*     */       
/*     */       return;
/*     */     } 
/* 118 */     boolean isFirstFault = !this.handlingFault;
/* 119 */     this.handlingFault = true;
/*     */     
/* 121 */     if (!this.channel.isOpen()) {
/*     */       return;
/*     */     }
/*     */     
/* 125 */     if (cause instanceof io.netty.handler.timeout.TimeoutException) {
/* 126 */       LOGGER.debug("Timeout", cause);
/* 127 */       disconnect(Component.translatable("disconnect.timeout"));
/*     */     } else {
/* 129 */       DisconnectionDetails details; MutableComponent mutableComponent = Component.translatable("disconnect.genericReason", new Object[] { "Internal Exception: " + String.valueOf(cause) });
/*     */ 
/*     */       
/* 132 */       PacketListener listener = this.packetListener;
/* 133 */       if (listener != null) {
/* 134 */         details = listener.createDisconnectionInfo(mutableComponent, cause);
/*     */       } else {
/* 136 */         details = new DisconnectionDetails(mutableComponent);
/*     */       } 
/*     */       
/* 139 */       if (isFirstFault) {
/* 140 */         LOGGER.debug("Failed to sent packet", cause);
/* 141 */         if (getSending() == PacketFlow.CLIENTBOUND) {
/* 142 */           ClientboundLoginDisconnectPacket clientboundLoginDisconnectPacket = this.sendLoginDisconnect ? new ClientboundLoginDisconnectPacket(mutableComponent) : new ClientboundDisconnectPacket(mutableComponent);
/* 143 */           send(clientboundLoginDisconnectPacket, PacketSendListener.thenRun(() -> disconnect(details)));
/*     */         } else {
/* 145 */           disconnect(details);
/*     */         } 
/* 147 */         setReadOnly();
/*     */       } else {
/* 149 */         LOGGER.debug("Double fault", cause);
/* 150 */         disconnect(details);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void channelRead0(ChannelHandlerContext ctx, Packet<?> packet) {
/* 157 */     if (!this.channel.isOpen()) {
/*     */       return;
/*     */     }
/* 160 */     PacketListener packetListener = this.packetListener;
/* 161 */     if (packetListener == null) {
/* 162 */       throw new IllegalStateException("Received a packet before the packet listener was initialized");
/*     */     }
/* 164 */     if (packetListener.shouldHandleMessage(packet)) {
/*     */       
/* 166 */       try { genericsFtw(packet, packetListener); }
/* 167 */       catch (RunningOnDifferentThreadException runningOnDifferentThreadException) {  }
/* 168 */       catch (RejectedExecutionException ignored)
/* 169 */       { disconnect(Component.translatable("multiplayer.disconnect.server_shutdown")); }
/* 170 */       catch (ClassCastException exception)
/*     */       
/* 172 */       { LOGGER.error("Received {} that couldn't be processed", packet.getClass(), exception);
/* 173 */         disconnect(Component.translatable("multiplayer.disconnect.invalid_packet")); }
/*     */       
/* 175 */       this.receivedPackets++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 181 */   private static <T extends PacketListener> void genericsFtw(Packet<T> packet, PacketListener listener) { packet.handle(listener); }
/*     */ 
/*     */   
/*     */   private void validateListener(ProtocolInfo<?> protocol, PacketListener packetListener) {
/* 185 */     Objects.requireNonNull(packetListener, "packetListener");
/* 186 */     PacketFlow listenerFlow = packetListener.flow();
/* 187 */     if (listenerFlow != this.receiving) {
/* 188 */       throw new IllegalStateException("Trying to set listener for wrong side: connection is " + String.valueOf(this.receiving) + ", but listener is " + String.valueOf(listenerFlow));
/*     */     }
/* 190 */     ConnectionProtocol listenerProtocol = packetListener.protocol();
/* 191 */     if (protocol.id() != listenerProtocol) {
/* 192 */       throw new IllegalStateException("Listener protocol (" + String.valueOf(listenerProtocol) + ") does not match requested one " + String.valueOf(protocol));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void syncAfterConfigurationChange(ChannelFuture future) {
/*     */     try {
/* 200 */       future.syncUninterruptibly();
/* 201 */     } catch (Exception e) {
/*     */       
/* 203 */       if (e instanceof java.nio.channels.ClosedChannelException) {
/* 204 */         LOGGER.info("Connection closed during protocol change");
/*     */         return;
/*     */       } 
/* 207 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public <T extends PacketListener> void setupInboundProtocol(ProtocolInfo<T> protocol, T packetListener) {
/* 212 */     validateListener(protocol, packetListener);
/* 213 */     if (protocol.flow() != getReceiving()) {
/* 214 */       throw new IllegalStateException("Invalid inbound protocol: " + String.valueOf(protocol.id()));
/*     */     }
/*     */     
/* 217 */     this.packetListener = packetListener;
/* 218 */     this.disconnectListener = null;
/*     */     
/* 220 */     UnconfiguredPipelineHandler.InboundConfigurationTask configMessage = UnconfiguredPipelineHandler.setupInboundProtocol(protocol);
/*     */     
/* 222 */     BundlerInfo bundlerInfo = protocol.bundlerInfo();
/* 223 */     if (bundlerInfo != null) {
/* 224 */       PacketBundlePacker newBundler = new PacketBundlePacker(bundlerInfo);
/* 225 */       configMessage = configMessage.andThen(ctx -> ctx.pipeline().addAfter("decoder", "bundler", newBundler));
/*     */     } 
/*     */     
/* 228 */     syncAfterConfigurationChange(this.channel.writeAndFlush(configMessage));
/*     */   }
/*     */   
/*     */   public void setupOutboundProtocol(ProtocolInfo<?> protocol) {
/* 232 */     if (protocol.flow() != getSending()) {
/* 233 */       throw new IllegalStateException("Invalid outbound protocol: " + String.valueOf(protocol.id()));
/*     */     }
/*     */     
/* 236 */     UnconfiguredPipelineHandler.OutboundConfigurationTask configMessage = UnconfiguredPipelineHandler.setupOutboundProtocol(protocol);
/*     */     
/* 238 */     BundlerInfo bundlerInfo = protocol.bundlerInfo();
/* 239 */     if (bundlerInfo != null) {
/* 240 */       PacketBundleUnpacker newUnbundler = new PacketBundleUnpacker(bundlerInfo);
/* 241 */       configMessage = configMessage.andThen(ctx -> ctx.pipeline().addAfter("encoder", "unbundler", newUnbundler));
/*     */     } 
/*     */     
/* 244 */     boolean isLoginProtocol = (protocol.id() == ConnectionProtocol.LOGIN);
/* 245 */     syncAfterConfigurationChange(this.channel.writeAndFlush(configMessage.andThen(ctx -> this.sendLoginDisconnect = isLoginProtocol)));
/*     */   }
/*     */   
/*     */   public void setListenerForServerboundHandshake(PacketListener packetListener) {
/* 249 */     if (this.packetListener != null) {
/* 250 */       throw new IllegalStateException("Listener already set");
/*     */     }
/* 252 */     if (this.receiving != PacketFlow.SERVERBOUND || packetListener
/* 253 */       .flow() != PacketFlow.SERVERBOUND || packetListener
/* 254 */       .protocol() != INITIAL_PROTOCOL.id())
/*     */     {
/* 256 */       throw new IllegalStateException("Invalid initial listener");
/*     */     }
/* 258 */     this.packetListener = packetListener;
/*     */   }
/*     */ 
/*     */   
/* 262 */   public void initiateServerboundStatusConnection(String hostName, int port, ClientStatusPacketListener listener) { initiateServerboundConnection(hostName, port, StatusProtocols.SERVERBOUND, StatusProtocols.CLIENTBOUND, listener, ClientIntent.STATUS); }
/*     */ 
/*     */ 
/*     */   
/* 266 */   public void initiateServerboundPlayConnection(String hostName, int port, ClientLoginPacketListener listener) { initiateServerboundConnection(hostName, port, LoginProtocols.SERVERBOUND, LoginProtocols.CLIENTBOUND, listener, ClientIntent.LOGIN); }
/*     */ 
/*     */ 
/*     */   
/* 270 */   public <S extends ServerboundPacketListener, C extends ClientboundPacketListener> void initiateServerboundPlayConnection(String hostName, int port, ProtocolInfo<S> outbound, ProtocolInfo<C> inbound, C listener, boolean transfer) { initiateServerboundConnection(hostName, port, outbound, inbound, listener, transfer ? ClientIntent.TRANSFER : ClientIntent.LOGIN); }
/*     */ 
/*     */   
/*     */   private <S extends ServerboundPacketListener, C extends ClientboundPacketListener> void initiateServerboundConnection(String hostName, int port, ProtocolInfo<S> outbound, ProtocolInfo<C> inbound, C listener, ClientIntent intent) {
/* 274 */     if (outbound.id() != inbound.id()) {
/* 275 */       throw new IllegalStateException("Mismatched initial protocols");
/*     */     }
/*     */     
/* 278 */     this.disconnectListener = listener;
/* 279 */     runOnceConnected(connection -> {
/*     */           
/* 281 */           setupInboundProtocol(inbound, listener);
/* 282 */           connection.sendPacket(new ClientIntentionPacket(SharedConstants.getCurrentVersion().protocolVersion(), hostName, port, intent), null, true);
/* 283 */           setupOutboundProtocol(outbound);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/* 288 */   public void send(Packet<?> packet) { send(packet, null); }
/*     */ 
/*     */ 
/*     */   
/* 292 */   public void send(Packet<?> packet, ChannelFutureListener listener) { send(packet, listener, true); }
/*     */ 
/*     */   
/*     */   public void send(Packet<?> packet, ChannelFutureListener listener, boolean flush) {
/* 296 */     if (isConnected()) {
/* 297 */       flushQueue();
/* 298 */       sendPacket(packet, listener, flush);
/*     */     } else {
/* 300 */       this.pendingActions.add(connection -> connection.sendPacket(packet, listener, flush));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void runOnceConnected(Consumer<Connection> action) {
/* 305 */     if (isConnected()) {
/* 306 */       flushQueue();
/* 307 */       action.accept(this);
/*     */     } else {
/* 309 */       this.pendingActions.add(action);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void sendPacket(Packet<?> packet, ChannelFutureListener listener, boolean flush) {
/* 314 */     this.sentPackets++;
/* 315 */     if (this.channel.eventLoop().inEventLoop()) {
/* 316 */       doSendPacket(packet, listener, flush);
/*     */     } else {
/* 318 */       this.channel.eventLoop().execute(() -> doSendPacket(packet, listener, flush));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doSendPacket(Packet<?> packet, ChannelFutureListener listener, boolean flush) {
/* 323 */     if (listener != null) {
/* 324 */       ChannelFuture future = flush ? this.channel.writeAndFlush(packet) : this.channel.write(packet);
/* 325 */       future.addListener(listener);
/*     */     }
/* 327 */     else if (flush) {
/* 328 */       this.channel.writeAndFlush(packet, this.channel.voidPromise());
/*     */     } else {
/* 330 */       this.channel.write(packet, this.channel.voidPromise());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void flushChannel() {
/* 336 */     if (isConnected()) {
/* 337 */       flush();
/*     */     } else {
/* 339 */       this.pendingActions.add(Connection::flush);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void flush() {
/* 344 */     if (this.channel.eventLoop().inEventLoop()) {
/* 345 */       this.channel.flush();
/*     */     } else {
/* 347 */       this.channel.eventLoop().execute(() -> this.channel.flush());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void flushQueue() {
/* 352 */     if (this.channel == null || !this.channel.isOpen()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 357 */     synchronized (this.pendingActions) {
/*     */       Consumer<Connection> pendingAction;
/* 359 */       while ((pendingAction = (Consumer)this.pendingActions.poll()) != null) {
/* 360 */         pendingAction.accept(this);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 366 */     flushQueue();
/*     */     
/* 368 */     PacketListener packetListener1 = this.packetListener; if (packetListener1 instanceof TickablePacketListener) { TickablePacketListener tickable = (TickablePacketListener)packetListener1;
/* 369 */       tickable.tick(); }
/*     */ 
/*     */     
/* 372 */     if (!isConnected() && !this.disconnectionHandled) {
/* 373 */       handleDisconnection();
/*     */     }
/*     */     
/* 376 */     if (this.channel != null) {
/* 377 */       this.channel.flush();
/*     */     }
/*     */     
/* 380 */     if (this.tickCount++ % 20 == 0) {
/* 381 */       tickSecond();
/*     */     }
/* 383 */     if (this.bandwidthDebugMonitor != null) {
/* 384 */       this.bandwidthDebugMonitor.tick();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void tickSecond() {
/* 389 */     this.averageSentPackets = Mth.lerp(0.75F, this.sentPackets, this.averageSentPackets);
/* 390 */     this.averageReceivedPackets = Mth.lerp(0.75F, this.receivedPackets, this.averageReceivedPackets);
/* 391 */     this.sentPackets = 0;
/* 392 */     this.receivedPackets = 0;
/*     */   }
/*     */ 
/*     */   
/* 396 */   public SocketAddress getRemoteAddress() { return this.address; }
/*     */ 
/*     */   
/*     */   public String getLoggableAddress(boolean logIPs) {
/* 400 */     if (this.address == null) {
/* 401 */       return "local";
/*     */     }
/* 403 */     if (logIPs) {
/* 404 */       return this.address.toString();
/*     */     }
/* 406 */     return "IP hidden";
/*     */   }
/*     */ 
/*     */   
/* 410 */   public void disconnect(Component reason) { disconnect(new DisconnectionDetails(reason)); }
/*     */ 
/*     */   
/*     */   public void disconnect(DisconnectionDetails details) {
/* 414 */     if (this.channel == null) {
/* 415 */       this.delayedDisconnect = details;
/*     */     }
/* 417 */     if (isConnected()) {
/* 418 */       this.channel.close().awaitUninterruptibly();
/*     */       
/* 420 */       this.disconnectionDetails = details;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 425 */   public boolean isMemoryConnection() { return (this.channel instanceof io.netty.channel.local.LocalChannel || this.channel instanceof io.netty.channel.local.LocalServerChannel); }
/*     */ 
/*     */ 
/*     */   
/* 429 */   public PacketFlow getReceiving() { return this.receiving; }
/*     */ 
/*     */ 
/*     */   
/* 433 */   public PacketFlow getSending() { return this.receiving.getOpposite(); }
/*     */ 
/*     */   
/*     */   public static Connection connectToServer(InetSocketAddress address, EventLoopGroupHolder eventLoopGroupHolder, LocalSampleLogger bandwidthLogger) {
/* 437 */     Connection connection = new Connection(PacketFlow.CLIENTBOUND);
/*     */     
/* 439 */     if (bandwidthLogger != null) {
/* 440 */       connection.setBandwidthLogger(bandwidthLogger);
/*     */     }
/* 442 */     ChannelFuture connect = connect(address, eventLoopGroupHolder, connection);
/* 443 */     connect.syncUninterruptibly();
/*     */     
/* 445 */     return connection;
/*     */   }
/*     */ 
/*     */   
/* 449 */   public static ChannelFuture connect(InetSocketAddress address, EventLoopGroupHolder eventLoopGroupHolder, final Connection connection) { return ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group(eventLoopGroupHolder.eventLoopGroup())).handler(new ChannelInitializer<Channel>()
/*     */         {
/*     */           protected void initChannel(Channel channel) {
/*     */             try {
/* 453 */               channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/* 454 */             } catch (ChannelException channelException) {}
/*     */ 
/*     */ 
/*     */             
/* 458 */             ChannelPipeline pipeline = channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30));
/*     */             
/* 460 */             Connection.configureSerialization(pipeline, PacketFlow.CLIENTBOUND, false, this.val$connection.bandwidthDebugMonitor);
/*     */             
/* 462 */             connection.configurePacketHandler(pipeline);
/*     */           }
/* 464 */         })).channel(eventLoopGroupHolder.channelCls())).connect(address.getAddress(), address.getPort()); }
/*     */ 
/*     */ 
/*     */   
/* 468 */   private static String outboundHandlerName(boolean configureOutbound) { return configureOutbound ? "encoder" : "outbound_config"; }
/*     */ 
/*     */ 
/*     */   
/* 472 */   private static String inboundHandlerName(boolean configureInbound) { return configureInbound ? "decoder" : "inbound_config"; }
/*     */ 
/*     */   
/*     */   public void configurePacketHandler(ChannelPipeline pipeline) {
/* 476 */     pipeline
/* 477 */       .addLast("hackfix", new ChannelOutboundHandlerAdapter(this)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */           {
/* 488 */             super.write(ctx, msg, promise);
/*     */           }
/* 491 */         }).addLast("packet_handler", this);
/*     */   }
/*     */   
/*     */   public static void configureSerialization(ChannelPipeline pipeline, PacketFlow inboundDirection, boolean local, BandwidthDebugMonitor monitor) {
/* 495 */     PacketFlow outboundDirection = inboundDirection.getOpposite();
/*     */ 
/*     */     
/* 498 */     boolean configureInbound = (inboundDirection == PacketFlow.SERVERBOUND);
/* 499 */     boolean configureOutbound = (outboundDirection == PacketFlow.SERVERBOUND);
/*     */ 
/*     */ 
/*     */     
/* 503 */     pipeline
/*     */       
/* 505 */       .addLast("splitter", createFrameDecoder(monitor, local))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 511 */       .addLast(new ChannelHandler[] { new FlowControlHandler()
/* 512 */         }).addLast(inboundHandlerName(configureInbound), configureInbound ? new PacketDecoder(INITIAL_PROTOCOL) : new UnconfiguredPipelineHandler.Inbound())
/*     */ 
/*     */ 
/*     */       
/* 516 */       .addLast("prepender", createFrameEncoder(local))
/*     */ 
/*     */       
/* 519 */       .addLast(outboundHandlerName(configureOutbound), configureOutbound ? new PacketEncoder(INITIAL_PROTOCOL) : new UnconfiguredPipelineHandler.Outbound());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 524 */   private static ChannelOutboundHandler createFrameEncoder(boolean local) { return local ? new LocalFrameEncoder() : new Varint21LengthFieldPrepender(); }
/*     */ 
/*     */   
/*     */   private static ChannelInboundHandler createFrameDecoder(BandwidthDebugMonitor monitor, boolean local) {
/* 528 */     if (!local) {
/* 529 */       return new Varint21FrameDecoder(monitor);
/*     */     }
/*     */     
/* 532 */     if (monitor != null) {
/* 533 */       return new MonitoredLocalFrameDecoder(monitor);
/*     */     }
/*     */     
/* 536 */     return new LocalFrameDecoder();
/*     */   }
/*     */ 
/*     */   
/* 540 */   public static void configureInMemoryPipeline(ChannelPipeline pipeline, PacketFlow packetFlow) { configureSerialization(pipeline, packetFlow, true, null); }
/*     */ 
/*     */   
/*     */   public static Connection connectToLocalServer(SocketAddress address) {
/* 544 */     final Connection connection = new Connection(PacketFlow.CLIENTBOUND);
/*     */     
/* 546 */     ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group(EventLoopGroupHolder.local().eventLoopGroup())).handler(new ChannelInitializer<Channel>()
/*     */         {
/*     */           protected void initChannel(Channel channel) {
/* 549 */             ChannelPipeline pipeline = channel.pipeline();
/* 550 */             Connection.configureInMemoryPipeline(pipeline, PacketFlow.CLIENTBOUND);
/* 551 */             connection.configurePacketHandler(pipeline);
/*     */           }
/* 553 */         })).channel(EventLoopGroupHolder.local().channelCls())).connect(address).syncUninterruptibly();
/*     */     
/* 555 */     return connection;
/*     */   }
/*     */   
/*     */   public void setEncryptionKey(Cipher decryptCipher, Cipher encryptCipher) {
/* 559 */     this.encrypted = true;
/* 560 */     this.channel.pipeline().addBefore("splitter", "decrypt", new CipherDecoder(decryptCipher));
/* 561 */     this.channel.pipeline().addBefore("prepender", "encrypt", new CipherEncoder(encryptCipher));
/*     */   }
/*     */ 
/*     */   
/* 565 */   public boolean isEncrypted() { return this.encrypted; }
/*     */ 
/*     */ 
/*     */   
/* 569 */   public boolean isConnected() { return (this.channel != null && this.channel.isOpen()); }
/*     */ 
/*     */ 
/*     */   
/* 573 */   public boolean isConnecting() { return (this.channel == null); }
/*     */ 
/*     */ 
/*     */   
/* 577 */   public PacketListener getPacketListener() { return this.packetListener; }
/*     */ 
/*     */ 
/*     */   
/* 581 */   public DisconnectionDetails getDisconnectionDetails() { return this.disconnectionDetails; }
/*     */ 
/*     */   
/*     */   public void setReadOnly() {
/* 585 */     if (this.channel != null) {
/* 586 */       this.channel.config().setAutoRead(false);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setupCompression(int threshold, boolean validateDecompressed) {
/* 591 */     if (threshold >= 0) {
/* 592 */       ChannelHandler channelHandler = this.channel.pipeline().get("decompress"); if (channelHandler instanceof CompressionDecoder) { CompressionDecoder compressionDecoder = (CompressionDecoder)channelHandler;
/* 593 */         compressionDecoder.setThreshold(threshold, validateDecompressed); }
/*     */       else
/* 595 */       { this.channel.pipeline().addAfter("splitter", "decompress", new CompressionDecoder(threshold, validateDecompressed)); }
/*     */ 
/*     */       
/* 598 */       channelHandler = this.channel.pipeline().get("compress"); if (channelHandler instanceof CompressionEncoder) { CompressionEncoder compressionEncoder = (CompressionEncoder)channelHandler;
/* 599 */         compressionEncoder.setThreshold(threshold); }
/*     */       else
/* 601 */       { this.channel.pipeline().addAfter("prepender", "compress", new CompressionEncoder(threshold)); }
/*     */     
/*     */     } else {
/* 604 */       if (this.channel.pipeline().get("decompress") instanceof CompressionDecoder) {
/* 605 */         this.channel.pipeline().remove("decompress");
/*     */       }
/*     */       
/* 608 */       if (this.channel.pipeline().get("compress") instanceof CompressionEncoder) {
/* 609 */         this.channel.pipeline().remove("compress");
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleDisconnection() {
/* 615 */     if (this.channel == null || this.channel.isOpen()) {
/*     */       return;
/*     */     }
/*     */     
/* 619 */     if (this.disconnectionHandled) {
/* 620 */       LOGGER.warn("handleDisconnection() called twice");
/*     */       
/*     */       return;
/*     */     } 
/* 624 */     this.disconnectionHandled = true;
/* 625 */     PacketListener packetListener = getPacketListener();
/* 626 */     PacketListener disconnectListener = (packetListener != null) ? packetListener : this.disconnectListener;
/* 627 */     if (disconnectListener != null) {
/* 628 */       DisconnectionDetails details = (DisconnectionDetails)Objects.requireNonNullElseGet(getDisconnectionDetails(), () -> new DisconnectionDetails(Component.translatable("multiplayer.disconnect.generic")));
/* 629 */       disconnectListener.onDisconnect(details);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 634 */   public float getAverageReceivedPackets() { return this.averageReceivedPackets; }
/*     */ 
/*     */ 
/*     */   
/* 638 */   public float getAverageSentPackets() { return this.averageSentPackets; }
/*     */ 
/*     */ 
/*     */   
/* 642 */   public void setBandwidthLogger(LocalSampleLogger bandwidthLogger) { this.bandwidthDebugMonitor = new BandwidthDebugMonitor(bandwidthLogger); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\Connection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */