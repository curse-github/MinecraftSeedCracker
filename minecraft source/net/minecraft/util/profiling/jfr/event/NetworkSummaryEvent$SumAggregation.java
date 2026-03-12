/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SumAggregation
/*    */ {
/*    */   private final AtomicLong sentBytes;
/*    */   private final AtomicInteger sentPackets;
/*    */   private final AtomicLong receivedBytes;
/*    */   private final AtomicInteger receivedPackets;
/*    */   private final NetworkSummaryEvent event;
/*    */   
/*    */   public SumAggregation(String remoteAddress) {
/* 63 */     this.sentBytes = new AtomicLong();
/* 64 */     this.sentPackets = new AtomicInteger();
/* 65 */     this.receivedBytes = new AtomicLong();
/* 66 */     this.receivedPackets = new AtomicInteger();
/*    */ 
/*    */ 
/*    */     
/* 70 */     this.event = new NetworkSummaryEvent(remoteAddress);
/* 71 */     this.event.begin();
/*    */   }
/*    */   
/*    */   public void trackSentPacket(int size) {
/* 75 */     this.sentPackets.incrementAndGet();
/* 76 */     this.sentBytes.addAndGet(size);
/*    */   }
/*    */   
/*    */   public void trackReceivedPacket(int size) {
/* 80 */     this.receivedPackets.incrementAndGet();
/* 81 */     this.receivedBytes.addAndGet(size);
/*    */   }
/*    */   
/*    */   public void commitEvent() {
/* 85 */     this.event.sentBytes = this.sentBytes.get();
/* 86 */     this.event.sentPackets = this.sentPackets.get();
/* 87 */     this.event.receivedBytes = this.receivedBytes.get();
/* 88 */     this.event.receivedPackets = this.receivedPackets.get();
/* 89 */     this.event.commit();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\event\NetworkSummaryEvent$SumAggregation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */