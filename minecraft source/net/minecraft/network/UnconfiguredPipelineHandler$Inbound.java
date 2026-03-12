/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.channel.ChannelDuplexHandler;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelPromise;
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import io.netty.util.ReferenceCountUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Inbound
/*    */   extends ChannelDuplexHandler
/*    */ {
/*    */   public void channelRead(ChannelHandlerContext ctx, Object msg) {
/* 19 */     if (msg instanceof io.netty.buffer.ByteBuf || msg instanceof net.minecraft.network.protocol.Packet) {
/* 20 */       ReferenceCountUtil.release(msg);
/* 21 */       throw new DecoderException("Pipeline has no inbound protocol configured, can't process packet " + String.valueOf(msg));
/*    */     } 
/*    */     
/* 24 */     ctx.fireChannelRead(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/* 30 */     if (msg instanceof UnconfiguredPipelineHandler.InboundConfigurationTask) { UnconfiguredPipelineHandler.InboundConfigurationTask configurationTask = (UnconfiguredPipelineHandler.InboundConfigurationTask)msg;
/*    */       try {
/* 32 */         configurationTask.run(ctx);
/*    */       } finally {
/* 34 */         ReferenceCountUtil.release(msg);
/*    */       } 
/* 36 */       promise.setSuccess(); }
/*    */     else
/* 38 */     { ctx.write(msg, promise); }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\UnconfiguredPipelineHandler$Inbound.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */