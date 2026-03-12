/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.IoHandlerFactory;
/*    */ import io.netty.channel.ServerChannel;
/*    */ import io.netty.channel.kqueue.KQueueIoHandler;
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
/*    */   extends EventLoopGroupHolder
/*    */ {
/* 45 */   null(String type, Class<? extends Channel> channelCls, Class<? extends ServerChannel> serverChannelCls) { super(type, channelCls, serverChannelCls); }
/*    */ 
/*    */   
/* 48 */   protected IoHandlerFactory ioHandlerFactory() { return KQueueIoHandler.newFactory(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\EventLoopGroupHolder$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */