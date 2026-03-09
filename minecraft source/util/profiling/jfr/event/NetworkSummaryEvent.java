/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import jdk.jfr.Category;
/*    */ import jdk.jfr.DataAmount;
/*    */ import jdk.jfr.Event;
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ import jdk.jfr.Period;
/*    */ import jdk.jfr.StackTrace;
/*    */ 
/*    */ @Name("minecraft.NetworkSummary")
/*    */ @Label("Network Summary")
/*    */ @Category({"Minecraft", "Network"})
/*    */ @StackTrace(false)
/*    */ @Period("10 s")
/*    */ public class NetworkSummaryEvent
/*    */   extends Event
/*    */ {
/*    */   public static final String EVENT_NAME = "minecraft.NetworkSummary";
/* 23 */   public static final EventType TYPE = EventType.getEventType(NetworkSummaryEvent.class);
/*    */   
/*    */   @Name("remoteAddress")
/*    */   @Label("Remote Address")
/*    */   public final String remoteAddress;
/*    */   
/*    */   @Name("sentBytes")
/*    */   @Label("Sent Bytes")
/*    */   @DataAmount
/*    */   public long sentBytes;
/*    */   
/*    */   @Name("sentPackets")
/*    */   @Label("Sent Packets")
/*    */   public int sentPackets;
/*    */   
/*    */   @Name("receivedBytes")
/*    */   @Label("Received Bytes")
/*    */   @DataAmount
/*    */   public long receivedBytes;
/*    */   
/*    */   @Name("receivedPackets")
/*    */   @Label("Received Packets")
/*    */   public int receivedPackets;
/*    */ 
/*    */   
/* 48 */   public NetworkSummaryEvent(String remoteAddress) { this.remoteAddress = remoteAddress; }
/*    */   
/*    */   public static final class Fields {
/*    */     public static final String REMOTE_ADDRESS = "remoteAddress";
/*    */     public static final String SENT_BYTES = "sentBytes";
/*    */     private static final String SENT_PACKETS = "sentPackets";
/*    */     public static final String RECEIVED_BYTES = "receivedBytes";
/*    */     private static final String RECEIVED_PACKETS = "receivedPackets";
/*    */   }
/*    */   
/*    */   public static final class SumAggregation {
/*    */     private final AtomicLong sentBytes;
/*    */     private final AtomicInteger sentPackets;
/*    */     
/*    */     public SumAggregation(String remoteAddress) {
/* 63 */       this.sentBytes = new AtomicLong();
/* 64 */       this.sentPackets = new AtomicInteger();
/* 65 */       this.receivedBytes = new AtomicLong();
/* 66 */       this.receivedPackets = new AtomicInteger();
/*    */ 
/*    */ 
/*    */       
/* 70 */       this.event = new NetworkSummaryEvent(remoteAddress);
/* 71 */       this.event.begin();
/*    */     }
/*    */     private final AtomicLong receivedBytes; private final AtomicInteger receivedPackets; private final NetworkSummaryEvent event;
/*    */     public void trackSentPacket(int size) {
/* 75 */       this.sentPackets.incrementAndGet();
/* 76 */       this.sentBytes.addAndGet(size);
/*    */     }
/*    */     
/*    */     public void trackReceivedPacket(int size) {
/* 80 */       this.receivedPackets.incrementAndGet();
/* 81 */       this.receivedBytes.addAndGet(size);
/*    */     }
/*    */     
/*    */     public void commitEvent() {
/* 85 */       this.event.sentBytes = this.sentBytes.get();
/* 86 */       this.event.sentPackets = this.sentPackets.get();
/* 87 */       this.event.receivedBytes = this.receivedBytes.get();
/* 88 */       this.event.receivedPackets = this.receivedPackets.get();
/* 89 */       this.event.commit();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\event\NetworkSummaryEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */