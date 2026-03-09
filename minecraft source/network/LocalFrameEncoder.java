/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelOutboundHandlerAdapter;
/*    */ import io.netty.channel.ChannelPromise;
/*    */ 
/*    */ public class LocalFrameEncoder
/*    */   extends ChannelOutboundHandlerAdapter
/*    */ {
/* 10 */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) { ctx.write(HiddenByteBuf.pack(msg), promise); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\LocalFrameEncoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */