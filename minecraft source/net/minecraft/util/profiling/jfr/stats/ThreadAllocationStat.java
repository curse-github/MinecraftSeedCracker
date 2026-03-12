/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ import java.time.Instant;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import jdk.jfr.consumer.RecordedEvent;
/*    */ import jdk.jfr.consumer.RecordedThread;
/*    */ 
/*    */ public final class ThreadAllocationStat extends Record {
/*    */   private final Instant timestamp;
/*    */   private final String threadName;
/*    */   private final long totalBytes;
/*    */   private static final String UNKNOWN_THREAD = "unknown";
/*    */   
/* 14 */   public ThreadAllocationStat(Instant timestamp, String threadName, long totalBytes) { this.timestamp = timestamp; this.threadName = threadName; this.totalBytes = totalBytes; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat; } public Instant timestamp() { return this.timestamp; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public String threadName() { return this.threadName; } public long totalBytes() { return this.totalBytes; }
/*    */ 
/*    */   
/*    */   public static ThreadAllocationStat from(RecordedEvent event) {
/* 18 */     RecordedThread recoredThread = event.getThread("thread");
/*    */ 
/*    */ 
/*    */     
/* 22 */     String threadName = (recoredThread == null) ? "unknown" : (String)MoreObjects.firstNonNull(recoredThread.getJavaName(), "unknown");
/*    */     
/* 24 */     return new ThreadAllocationStat(event.getStartTime(), threadName, event.getLong("allocated"));
/*    */   }
/*    */   
/*    */   public static Summary summary(List<ThreadAllocationStat> stats) {
/* 28 */     Map<String, Double> allocationsPerSecondByThread = new TreeMap<String, Double>();
/* 29 */     Map<String, List<ThreadAllocationStat>> byThread = (Map)stats.stream().collect(Collectors.groupingBy(it -> it.threadName));
/*    */     
/* 31 */     byThread.forEach((thread, threadStats) -> {
/* 32 */           if (threadStats.size() < 2) {
/*    */             return;
/*    */           }
/*    */           
/* 36 */           ThreadAllocationStat first = (ThreadAllocationStat)threadStats.get(0);
/* 37 */           ThreadAllocationStat last = (ThreadAllocationStat)threadStats.get(threadStats.size() - 1);
/* 38 */           long duration = Duration.between(first.timestamp, last.timestamp).getSeconds();
/*    */           
/* 40 */           long diff = last.totalBytes - first.totalBytes;
/* 41 */           allocationsPerSecondByThread.put(thread, Double.valueOf(diff / duration));
/*    */         });
/*    */     
/* 44 */     return new Summary(allocationsPerSecondByThread);
/*    */   }
/*    */   public static final class Summary extends Record { private final Map<String, Double> allocationsPerSecondByThread;
/* 47 */     public Summary(Map<String, Double> allocationsPerSecondByThread) { this.allocationsPerSecondByThread = allocationsPerSecondByThread; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat$Summary;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #47	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat$Summary; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat$Summary;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #47	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat$Summary; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat$Summary;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #47	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat$Summary;
/* 47 */       //   0	8	1	o	Ljava/lang/Object; } public Map<String, Double> allocationsPerSecondByThread() { return this.allocationsPerSecondByThread; } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\stats\ThreadAllocationStat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */