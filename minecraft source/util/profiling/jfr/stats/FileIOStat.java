/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ import java.time.Duration;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ public final class FileIOStat extends Record {
/*    */   private final Duration duration;
/*    */   private final String path;
/*    */   private final long bytes;
/*    */   
/* 11 */   public FileIOStat(Duration duration, String path, long bytes) { this.duration = duration; this.path = path; this.bytes = bytes; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat; } public Duration duration() { return this.duration; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public String path() { return this.path; } public long bytes() { return this.bytes; }
/*    */   
/*    */   public static Summary summary(Duration recordingDuration, List<FileIOStat> ioStats) {
/* 14 */     long totalBytes = ioStats.stream().mapToLong(it -> it.bytes).sum();
/* 15 */     return new Summary(totalBytes, totalBytes / recordingDuration
/*    */         
/* 17 */         .getSeconds(), ioStats
/* 18 */         .size(), ioStats
/* 19 */         .size() / recordingDuration.getSeconds(), (Duration)ioStats
/* 20 */         .stream().map(FileIOStat::duration).reduce(Duration.ZERO, Duration::plus), ((Map)ioStats
/* 21 */         .stream().filter(it -> (it.path != null)).collect(Collectors.groupingBy(stat -> stat.path, Collectors.summingLong(it -> it.bytes))))
/* 22 */         .entrySet().stream()
/* 23 */         .sorted(Map.Entry.comparingByValue().reversed())
/* 24 */         .map(e -> Pair.of((String)e.getKey(), (Long)e.getValue()))
/* 25 */         .limit(10L)
/* 26 */         .toList());
/*    */   }
/*    */   public static final class Summary extends Record { private final long totalBytes; private final double bytesPerSecond; private final long counts; private final double countsPerSecond; private final Duration timeSpentInIO; private final List<Pair<String, Long>> topTenContributorsByTotalBytes;
/*    */     
/* 30 */     public Summary(long totalBytes, double bytesPerSecond, long counts, double countsPerSecond, Duration timeSpentInIO, List<Pair<String, Long>> topTenContributorsByTotalBytes) { this.totalBytes = totalBytes; this.bytesPerSecond = bytesPerSecond; this.counts = counts; this.countsPerSecond = countsPerSecond; this.timeSpentInIO = timeSpentInIO; this.topTenContributorsByTotalBytes = topTenContributorsByTotalBytes; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;
/* 30 */       //   0	8	1	o	Ljava/lang/Object; } public long totalBytes() { return this.totalBytes; } public double bytesPerSecond() { return this.bytesPerSecond; } public long counts() { return this.counts; } public double countsPerSecond() { return this.countsPerSecond; } public Duration timeSpentInIO() { return this.timeSpentInIO; } public List<Pair<String, Long>> topTenContributorsByTotalBytes() { return this.topTenContributorsByTotalBytes; } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\stats\FileIOStat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */