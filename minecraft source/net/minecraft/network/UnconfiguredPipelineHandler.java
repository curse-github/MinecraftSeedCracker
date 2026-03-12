/*     */ package net.minecraft.network;
/*     */ 
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandler;
/*     */ import io.netty.channel.ChannelOutboundHandler;
/*     */ import io.netty.channel.ChannelOutboundHandlerAdapter;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.EncoderException;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ 
/*     */ public class UnconfiguredPipelineHandler
/*     */ {
/*     */   public static class Inbound
/*     */     extends ChannelDuplexHandler
/*     */   {
/*     */     public void channelRead(ChannelHandlerContext ctx, Object msg) {
/*  19 */       if (msg instanceof io.netty.buffer.ByteBuf || msg instanceof net.minecraft.network.protocol.Packet) {
/*  20 */         ReferenceCountUtil.release(msg);
/*  21 */         throw new DecoderException("Pipeline has no inbound protocol configured, can't process packet " + String.valueOf(msg));
/*     */       } 
/*     */       
/*  24 */       ctx.fireChannelRead(msg);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/*  30 */       if (msg instanceof UnconfiguredPipelineHandler.InboundConfigurationTask) { UnconfiguredPipelineHandler.InboundConfigurationTask configurationTask = (UnconfiguredPipelineHandler.InboundConfigurationTask)msg;
/*     */         try {
/*  32 */           configurationTask.run(ctx);
/*     */         } finally {
/*  34 */           ReferenceCountUtil.release(msg);
/*     */         } 
/*  36 */         promise.setSuccess(); }
/*     */       else
/*  38 */       { ctx.write(msg, promise); }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Outbound
/*     */     extends ChannelOutboundHandlerAdapter {
/*     */     public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/*  46 */       if (msg instanceof net.minecraft.network.protocol.Packet) {
/*  47 */         ReferenceCountUtil.release(msg);
/*  48 */         throw new EncoderException("Pipeline has no outbound protocol configured, can't process packet " + String.valueOf(msg));
/*  49 */       }  if (msg instanceof UnconfiguredPipelineHandler.OutboundConfigurationTask) { UnconfiguredPipelineHandler.OutboundConfigurationTask configurationTask = (UnconfiguredPipelineHandler.OutboundConfigurationTask)msg;
/*     */         try {
/*  51 */           configurationTask.run(ctx);
/*     */         } finally {
/*  53 */           ReferenceCountUtil.release(msg);
/*     */         } 
/*  55 */         promise.setSuccess(); }
/*     */       else
/*  57 */       { ctx.write(msg, promise); }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface InboundConfigurationTask {
/*     */     void run(ChannelHandlerContext param1ChannelHandlerContext);
/*     */     
/*     */     default InboundConfigurationTask andThen(InboundConfigurationTask otherTask) {
/*  67 */       return ctx -> {
/*  68 */           run(ctx);
/*  69 */           otherTask.run(ctx);
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface OutboundConfigurationTask {
/*     */     void run(ChannelHandlerContext param1ChannelHandlerContext);
/*     */     
/*     */     default OutboundConfigurationTask andThen(OutboundConfigurationTask otherTask) {
/*  79 */       return ctx -> {
/*  80 */           run(ctx);
/*  81 */           otherTask.run(ctx);
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  87 */   public static <T extends PacketListener> InboundConfigurationTask setupInboundProtocol(ProtocolInfo<T> protocolInfo) { return setupInboundHandler(new PacketDecoder(protocolInfo)); }
/*     */ 
/*     */   
/*     */   private static InboundConfigurationTask setupInboundHandler(ChannelInboundHandler newHandler) {
/*  91 */     return ctx -> {
/*  92 */         ctx.pipeline().replace(ctx.name(), "decoder", newHandler);
/*  93 */         ctx.channel().config().setAutoRead(true);
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*  98 */   public static <T extends PacketListener> OutboundConfigurationTask setupOutboundProtocol(ProtocolInfo<T> codecData) { return setupOutboundHandler(new PacketEncoder(codecData)); }
/*     */ 
/*     */ 
/*     */   
/* 102 */   private static OutboundConfigurationTask setupOutboundHandler(ChannelOutboundHandler newHandler) { return ctx -> ctx.pipeline().replace(ctx.name(), "encoder", newHandler); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\UnconfiguredPipelineHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */