/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.IoHandlerFactory;
/*     */ import io.netty.channel.MultiThreadIoEventLoopGroup;
/*     */ import io.netty.channel.ServerChannel;
/*     */ import io.netty.channel.epoll.Epoll;
/*     */ import io.netty.channel.epoll.EpollIoHandler;
/*     */ import io.netty.channel.kqueue.KQueue;
/*     */ import io.netty.channel.kqueue.KQueueIoHandler;
/*     */ import io.netty.channel.local.LocalIoHandler;
/*     */ import io.netty.channel.nio.NioIoHandler;
/*     */ import java.util.concurrent.ThreadFactory;
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
/*     */ public abstract class EventLoopGroupHolder
/*     */ {
/*  31 */   private static final EventLoopGroupHolder NIO = new EventLoopGroupHolder("NIO", io.netty.channel.socket.nio.NioSocketChannel.class, io.netty.channel.socket.nio.NioServerSocketChannel.class)
/*     */     {
/*     */       protected IoHandlerFactory ioHandlerFactory() {
/*  34 */         return NioIoHandler.newFactory();
/*     */       }
/*     */     };
/*     */   
/*  38 */   private static final EventLoopGroupHolder EPOLL = new EventLoopGroupHolder("Epoll", io.netty.channel.epoll.EpollSocketChannel.class, io.netty.channel.epoll.EpollServerSocketChannel.class)
/*     */     {
/*     */       protected IoHandlerFactory ioHandlerFactory() {
/*  41 */         return EpollIoHandler.newFactory();
/*     */       }
/*     */     };
/*     */   
/*  45 */   private static final EventLoopGroupHolder KQUEUE = new EventLoopGroupHolder("Kqueue", io.netty.channel.kqueue.KQueueSocketChannel.class, io.netty.channel.kqueue.KQueueServerSocketChannel.class)
/*     */     {
/*     */       protected IoHandlerFactory ioHandlerFactory() {
/*  48 */         return KQueueIoHandler.newFactory();
/*     */       }
/*     */     };
/*     */   
/*  52 */   private static final EventLoopGroupHolder LOCAL = new EventLoopGroupHolder("Local", io.netty.channel.local.LocalChannel.class, io.netty.channel.local.LocalServerChannel.class)
/*     */     {
/*     */       protected IoHandlerFactory ioHandlerFactory() {
/*  55 */         return LocalIoHandler.newFactory(); }
/*     */     };
/*     */   private final String type;
/*     */   
/*     */   public static EventLoopGroupHolder remote(boolean allowNativeTransport) {
/*  60 */     if (allowNativeTransport) {
/*  61 */       if (KQueue.isAvailable()) {
/*  62 */         return KQUEUE;
/*     */       }
/*     */       
/*  65 */       if (Epoll.isAvailable()) {
/*  66 */         return EPOLL;
/*     */       }
/*     */     } 
/*  69 */     return NIO;
/*     */   }
/*     */   private final Class<? extends Channel> channelCls; private final Class<? extends ServerChannel> serverChannelCls;
/*     */   
/*  73 */   public static EventLoopGroupHolder local() { return LOCAL; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private EventLoopGroupHolder(String type, Class<? extends Channel> channelCls, Class<? extends ServerChannel> serverChannelCls) {
/*  83 */     this.type = type;
/*  84 */     this.channelCls = channelCls;
/*  85 */     this.serverChannelCls = serverChannelCls;
/*     */   }
/*     */ 
/*     */   
/*  89 */   private ThreadFactory createThreadFactory() { return (new ThreadFactoryBuilder()).setNameFormat("Netty " + this.type + " IO #%d").setDaemon(true).build(); }
/*     */ 
/*     */   
/*     */   protected abstract IoHandlerFactory ioHandlerFactory();
/*     */ 
/*     */   
/*  95 */   private EventLoopGroup createEventLoopGroup() { return new MultiThreadIoEventLoopGroup(createThreadFactory(), ioHandlerFactory()); }
/*     */ 
/*     */   
/*     */   public EventLoopGroup eventLoopGroup() {
/*  99 */     EventLoopGroup result = this.group;
/* 100 */     if (result == null) {
/* 101 */       synchronized (this) {
/* 102 */         result = this.group;
/* 103 */         if (result == null) {
/* 104 */           result = createEventLoopGroup();
/* 105 */           this.group = result;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 110 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 114 */   public Class<? extends Channel> channelCls() { return this.channelCls; }
/*     */ 
/*     */ 
/*     */   
/* 118 */   public Class<? extends ServerChannel> serverChannelCls() { return this.serverChannelCls; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\EventLoopGroupHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */