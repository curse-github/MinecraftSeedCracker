/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import io.netty.util.HashedWheelTimer;
/*     */ import io.netty.util.Timeout;
/*     */ import io.netty.util.Timer;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LatencySimulator
/*     */   extends ChannelInboundHandlerAdapter
/*     */ {
/* 173 */   private static final Timer TIMER = new HashedWheelTimer();
/*     */   private final int delay;
/*     */   
/*     */   public LatencySimulator(int delay, int jitter) {
/* 177 */     this.queuedMessages = Lists.newArrayList();
/*     */ 
/*     */     
/* 180 */     this.delay = delay;
/* 181 */     this.jitter = jitter;
/*     */   }
/*     */   
/*     */   private final int jitter;
/*     */   
/* 186 */   public void channelRead(ChannelHandlerContext ctx, Object msg) { delayDownstream(ctx, msg); }
/*     */   private final List<DelayedMessage> queuedMessages;
/*     */   
/*     */   private void delayDownstream(ChannelHandlerContext ctx, Object msg) {
/* 190 */     int sendDelay = this.delay + (int)(Math.random() * this.jitter);
/* 191 */     this.queuedMessages.add(new DelayedMessage(ctx, msg));
/* 192 */     TIMER.newTimeout(this::onTimeout, sendDelay, TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */   private void onTimeout(Timeout timeout) {
/* 196 */     DelayedMessage next = (DelayedMessage)this.queuedMessages.remove(0);
/* 197 */     next.ctx.fireChannelRead(next.msg);
/*     */   }
/*     */   
/*     */   private static class DelayedMessage {
/*     */     public final ChannelHandlerContext ctx;
/*     */     public final Object msg;
/*     */     
/*     */     public DelayedMessage(ChannelHandlerContext ctx, Object msg) {
/* 205 */       this.ctx = ctx;
/* 206 */       this.msg = msg;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\ServerConnectionListener$LatencySimulator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */