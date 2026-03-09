/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ 
/*    */ import java.time.Duration;
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
/*    */ public final class Summary
/*    */   extends Record
/*    */ {
/*    */   private final Duration duration;
/*    */   private final Duration gcTotalDuration;
/*    */   private final int totalGCs;
/*    */   private final double allocationRateBytesPerSecond;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 48 */   public Summary(Duration duration, Duration gcTotalDuration, int totalGCs, double allocationRateBytesPerSecond) { this.duration = duration; this.gcTotalDuration = gcTotalDuration; this.totalGCs = totalGCs; this.allocationRateBytesPerSecond = allocationRateBytesPerSecond; } public Duration duration() { return this.duration; } public Duration gcTotalDuration() { return this.gcTotalDuration; } public int totalGCs() { return this.totalGCs; } public double allocationRateBytesPerSecond() { return this.allocationRateBytesPerSecond; }
/*    */   
/* 50 */   public float gcOverHead() { return (float)this.gcTotalDuration.toMillis() / (float)this.duration.toMillis(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\stats\GcHeapStat$Summary.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */