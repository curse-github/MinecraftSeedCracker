/*    */ package net.minecraft.util.debugchart;
/*    */ 
/*    */ import net.minecraft.network.protocol.game.ClientboundDebugSamplePacket;
/*    */ import net.minecraft.util.debug.ServerDebugSubscribers;
/*    */ 
/*    */ public class RemoteSampleLogger
/*    */   extends AbstractSampleLogger {
/*    */   private final ServerDebugSubscribers subscribers;
/*    */   private final RemoteDebugSampleType sampleType;
/*    */   
/* 11 */   public RemoteSampleLogger(int dimensions, ServerDebugSubscribers subscribers, RemoteDebugSampleType sampleType) { this(dimensions, subscribers, sampleType, new long[dimensions]); }
/*    */ 
/*    */   
/*    */   public RemoteSampleLogger(int dimensions, ServerDebugSubscribers subscribers, RemoteDebugSampleType sampleType, long[] defaults) {
/* 15 */     super(dimensions, defaults);
/* 16 */     this.subscribers = subscribers;
/* 17 */     this.sampleType = sampleType;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void useSample() {
/* 22 */     if (this.subscribers.hasAnySubscriberFor(this.sampleType.subscription()))
/* 23 */       this.subscribers.broadcastToAll(this.sampleType.subscription(), new ClientboundDebugSamplePacket((long[])this.sample.clone(), this.sampleType)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debugchart\RemoteSampleLogger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */