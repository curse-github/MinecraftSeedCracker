/*    */ package net.minecraft.network;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import io.netty.channel.ChannelFuture;
/*    */ import io.netty.channel.ChannelFutureListener;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PacketSendListener
/*    */ {
/* 17 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static ChannelFutureListener thenRun(Runnable runnable) {
/* 20 */     return future -> {
/* 21 */         runnable.run();
/* 22 */         if (!future.isSuccess()) {
/* 23 */           future.channel().pipeline().fireExceptionCaught(future.cause());
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static ChannelFutureListener exceptionallySend(Supplier<Packet<?>> handler) {
/* 29 */     return future -> {
/* 30 */         if (!future.isSuccess()) {
/* 31 */           Packet<?> newPacket = (Packet)handler.get();
/* 32 */           if (newPacket != null) {
/* 33 */             LOGGER.warn("Failed to deliver packet, sending fallback {}", newPacket.type(), future.cause());
/* 34 */             future.channel().writeAndFlush(newPacket, future.channel().voidPromise());
/*    */           } else {
/* 36 */             future.channel().pipeline().fireExceptionCaught(future.cause());
/*    */           } 
/*    */         } 
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\PacketSendListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */