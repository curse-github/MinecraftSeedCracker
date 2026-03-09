/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public interface ProtocolSwapHandler {
/*    */   static void handleInboundTerminalPacket(ChannelHandlerContext ctx, Packet<?> packet) {
/*  8 */     if (packet.isTerminal()) {
/*    */       
/* 10 */       ctx.channel().config().setAutoRead(false);
/*    */ 
/*    */       
/* 13 */       ctx.pipeline().addBefore(ctx.name(), "inbound_config", new UnconfiguredPipelineHandler.Inbound());
/* 14 */       ctx.pipeline().remove(ctx.name());
/*    */     } 
/*    */   }
/*    */   
/*    */   static void handleOutboundTerminalPacket(ChannelHandlerContext ctx, Packet<?> packet) {
/* 19 */     if (packet.isTerminal()) {
/*    */       
/* 21 */       ctx.pipeline().addAfter(ctx.name(), "outbound_config", new UnconfiguredPipelineHandler.Outbound());
/* 22 */       ctx.pipeline().remove(ctx.name());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\ProtocolSwapHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */