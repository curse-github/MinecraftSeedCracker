/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class TimedStatSummary<T extends TimedStat> extends Record {
/*    */   private final T fastest;
/*    */   private final T slowest;
/*    */   private final T secondSlowest;
/*    */   private final int count;
/*    */   private final Map<Integer, Double> percentilesNanos;
/*    */   private final Duration totalDuration;
/*    */   
/* 12 */   public TimedStatSummary(T fastest, T slowest, T secondSlowest, int count, Map<Integer, Double> percentilesNanos, Duration totalDuration) { this.fastest = fastest; this.slowest = slowest; this.secondSlowest = secondSlowest; this.count = count; this.percentilesNanos = percentilesNanos; this.totalDuration = totalDuration; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary<TT;>; } public T fastest() { return (T)this.fastest; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary<TT;>; } public T slowest() { return (T)this.slowest; } public T secondSlowest() { return (T)this.secondSlowest; } public int count() { return this.count; } public Map<Integer, Double> percentilesNanos() { return this.percentilesNanos; } public Duration totalDuration() { return this.totalDuration; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T extends TimedStat> Optional<TimedStatSummary<T>> summary(List<T> values) {
/* 21 */     if (values.isEmpty()) {
/* 22 */       return Optional.empty();
/*    */     }
/* 24 */     List<T> sorted = values.stream().sorted(Comparator.comparing(TimedStat::duration)).toList();
/* 25 */     Duration totalDuration = (Duration)sorted.stream().map(TimedStat::duration).reduce(Duration::plus).orElse(Duration.ZERO);
/* 26 */     T fastest = (T)(TimedStat)sorted.getFirst();
/* 27 */     T slowest = (T)(TimedStat)sorted.getLast();
/* 28 */     T secondSlowest = (T)((sorted.size() > 1) ? (TimedStat)sorted.get(sorted.size() - 2) : null);
/* 29 */     int count = sorted.size();
/* 30 */     Map<Integer, Double> percentilesNanos = Percentiles.evaluate(sorted.stream().mapToLong(it -> it.duration().toNanos()).toArray());
/* 31 */     return Optional.of(new TimedStatSummary(fastest, slowest, secondSlowest, count, percentilesNanos, totalDuration));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\stats\TimedStatSummary.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */