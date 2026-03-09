/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*    */ 
/*    */ public class MonitoredLocalFrameDecoder
/*    */   extends ChannelInboundHandlerAdapter {
/*    */   private final BandwidthDebugMonitor monitor;
/*    */   
/* 11 */   public MonitoredLocalFrameDecoder(BandwidthDebugMonitor monitor) { this.monitor = monitor; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void channelRead(ChannelHandlerContext ctx, Object msg) {
/* 16 */     msg = HiddenByteBuf.unpack(msg);
/* 17 */     if (msg instanceof ByteBuf) { ByteBuf in = (ByteBuf)msg;
/* 18 */       this.monitor.onReceive(in.readableBytes()); }
/*    */     
/* 20 */     ctx.fireChannelRead(msg);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\MonitoredLocalFrameDecoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */