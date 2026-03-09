/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import io.netty.handler.codec.MessageToMessageDecoder;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.protocol.BundlerInfo;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PacketBundlePacker
/*    */   extends MessageToMessageDecoder<Packet<?>>
/*    */ {
/*    */   private final BundlerInfo bundlerInfo;
/*    */   private BundlerInfo.Bundler currentBundler;
/*    */   
/* 18 */   public PacketBundlePacker(BundlerInfo bundlerInfo) { this.bundlerInfo = bundlerInfo; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, Packet<?> msg, List<Object> out) throws Exception {
/* 23 */     if (this.currentBundler != null) {
/* 24 */       verifyNonTerminalPacket(msg);
/*    */       
/* 26 */       Packet<?> bundlePacket = this.currentBundler.addPacket(msg);
/* 27 */       if (bundlePacket != null) {
/* 28 */         this.currentBundler = null;
/* 29 */         out.add(bundlePacket);
/*    */       } 
/*    */     } else {
/* 32 */       BundlerInfo.Bundler bundler = this.bundlerInfo.startPacketBundling(msg);
/* 33 */       if (bundler != null) {
/* 34 */         verifyNonTerminalPacket(msg);
/* 35 */         this.currentBundler = bundler;
/*    */       } else {
/* 37 */         out.add(msg);
/* 38 */         if (msg.isTerminal()) {
/* 39 */           ctx.pipeline().remove(ctx.name());
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void verifyNonTerminalPacket(Packet<?> msg) {
/* 46 */     if (msg.isTerminal())
/* 47 */       throw new DecoderException("Terminal message received in bundle"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\PacketBundlePacker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */