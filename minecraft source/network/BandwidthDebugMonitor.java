/*    */ package net.minecraft.network;
/*    */ import net.minecraft.util.debugchart.LocalSampleLogger;
/*    */ 
/*    */ public class BandwidthDebugMonitor {
/*    */   private final AtomicInteger bytesReceived;
/*    */   
/*    */   public BandwidthDebugMonitor(LocalSampleLogger bandwidthLogger) {
/*  8 */     this.bytesReceived = new AtomicInteger();
/*    */ 
/*    */ 
/*    */     
/* 12 */     this.bandwidthLogger = bandwidthLogger;
/*    */   }
/*    */   private final LocalSampleLogger bandwidthLogger;
/*    */   
/* 16 */   public void onReceive(int bytes) { this.bytesReceived.getAndAdd(bytes); }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public void tick() { this.bandwidthLogger.logSample(this.bytesReceived.getAndSet(0)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\BandwidthDebugMonitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */