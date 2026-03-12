/*   */ package net.minecraft.network;
/*   */ 
/*   */ import io.netty.channel.ChannelHandlerContext;
/*   */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*   */ 
/*   */ public class LocalFrameDecoder
/*   */   extends ChannelInboundHandlerAdapter
/*   */ {
/* 9 */   public void channelRead(ChannelHandlerContext ctx, Object msg) { ctx.fireChannelRead(HiddenByteBuf.unpack(msg)); }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\LocalFrameDecoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */