/*    */ package net.minecraft.util.debugchart;
/*    */ 
/*    */ import net.minecraft.util.debug.DebugSubscription;
/*    */ import net.minecraft.util.debug.DebugSubscriptions;
/*    */ 
/*    */ public static enum RemoteDebugSampleType {
/*  7 */   TICK_TIME(DebugSubscriptions.DEDICATED_SERVER_TICK_TIME);
/*    */ 
/*    */   
/*    */   private final DebugSubscription<?> subscription;
/*    */ 
/*    */   
/* 13 */   RemoteDebugSampleType(DebugSubscription<?> subscription) { this.subscription = subscription; }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public DebugSubscription<?> subscription() { return this.subscription; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debugchart\RemoteDebugSampleType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */