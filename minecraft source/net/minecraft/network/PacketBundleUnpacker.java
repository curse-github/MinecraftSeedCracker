/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToMessageEncoder;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.protocol.BundlerInfo;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class PacketBundleUnpacker
/*    */   extends MessageToMessageEncoder<Packet<?>> {
/*    */   private final BundlerInfo bundlerInfo;
/*    */   
/* 14 */   public PacketBundleUnpacker(BundlerInfo bundlerInfo) { this.bundlerInfo = bundlerInfo; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, Packet<?> msg, List<Object> out) throws Exception {
/* 19 */     Objects.requireNonNull(out); this.bundlerInfo.unbundlePacket(msg, out::add);
/* 20 */     if (msg.isTerminal())
/* 21 */       ctx.pipeline().remove(ctx.name()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\PacketBundleUnpacker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */