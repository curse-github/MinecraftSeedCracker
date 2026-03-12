/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ import java.time.Duration;
/*    */ import java.time.Instant;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class GcHeapStat extends Record {
/*    */   private final Instant timestamp;
/*    */   private final long heapUsed;
/*    */   private final Timing timing;
/*    */   
/* 11 */   public GcHeapStat(Instant timestamp, long heapUsed, Timing timing) { this.timestamp = timestamp; this.heapUsed = heapUsed; this.timing = timing; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat; } public Instant timestamp() { return this.timestamp; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public long heapUsed() { return this.heapUsed; } public Timing timing() { return this.timing; }
/*    */   
/*    */   public static GcHeapStat from(RecordedEvent event) {
/* 14 */     return new GcHeapStat(event.getStartTime(), event
/* 15 */         .getLong("heapUsed"), 
/* 16 */         event.getString("when").equalsIgnoreCase("before gc") ? 
/* 17 */         Timing.BEFORE_GC : 
/* 18 */         Timing.AFTER_GC);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static Summary summary(Duration recordingDuration, List<GcHeapStat> heapStats, Duration gcTotalDuration, int totalGCs) { return new Summary(recordingDuration, gcTotalDuration, totalGCs, 
/*    */ 
/*    */ 
/*    */         
/* 27 */         calculateAllocationRatePerSecond(heapStats)); }
/*    */ 
/*    */ 
/*    */   
/*    */   private static double calculateAllocationRatePerSecond(List<GcHeapStat> heapStats) {
/* 32 */     long totalAllocations = 0L;
/* 33 */     Map<Timing, List<GcHeapStat>> byTiming = (Map)heapStats.stream().collect(Collectors.groupingBy(it -> it.timing));
/* 34 */     List<GcHeapStat> beforeGcs = (List)byTiming.get(Timing.BEFORE_GC);
/* 35 */     List<GcHeapStat> afterGcs = (List)byTiming.get(Timing.AFTER_GC);
/*    */     
/* 37 */     for (int i = 1; i < beforeGcs.size(); i++) {
/* 38 */       GcHeapStat beforeGC = (GcHeapStat)beforeGcs.get(i);
/* 39 */       GcHeapStat previousGC = (GcHeapStat)afterGcs.get(i - 1);
/* 40 */       totalAllocations += beforeGC.heapUsed - previousGC.heapUsed;
/*    */     } 
/*    */     
/* 43 */     Duration totalDuration = Duration.between(((GcHeapStat)heapStats.get(1)).timestamp, ((GcHeapStat)heapStats.get(heapStats.size() - 1)).timestamp);
/*    */     
/* 45 */     return totalAllocations / totalDuration.getSeconds();
/*    */   }
/*    */   public static final class Summary extends Record { private final Duration duration; private final Duration gcTotalDuration; private final int totalGCs; private final double allocationRateBytesPerSecond;
/* 48 */     public Summary(Duration duration, Duration gcTotalDuration, int totalGCs, double allocationRateBytesPerSecond) { this.duration = duration; this.gcTotalDuration = gcTotalDuration; this.totalGCs = totalGCs; this.allocationRateBytesPerSecond = allocationRateBytesPerSecond; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary;
/* 48 */       //   0	8	1	o	Ljava/lang/Object; } public Duration duration() { return this.duration; } public Duration gcTotalDuration() { return this.gcTotalDuration; } public int totalGCs() { return this.totalGCs; } public double allocationRateBytesPerSecond() { return this.allocationRateBytesPerSecond; }
/*    */     
/* 50 */     public float gcOverHead() { return (float)this.gcTotalDuration.toMillis() / (float)this.duration.toMillis(); } }
/*    */ 
/*    */   
/*    */   enum Timing
/*    */   {
/* 55 */     BEFORE_GC, AFTER_GC;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\stats\GcHeapStat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */