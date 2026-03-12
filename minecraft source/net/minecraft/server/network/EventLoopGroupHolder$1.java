/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.IoHandlerFactory;
/*    */ import io.netty.channel.ServerChannel;
/*    */ import io.netty.channel.nio.NioIoHandler;
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
/* 31 */   null(String type, Class<? extends Channel> channelCls, Class<? extends ServerChannel> serverChannelCls) { super(type, channelCls, serverChannelCls); }
/*    */ 
/*    */   
/* 34 */   protected IoHandlerFactory ioHandlerFactory() { return NioIoHandler.newFactory(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\EventLoopGroupHolder$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */