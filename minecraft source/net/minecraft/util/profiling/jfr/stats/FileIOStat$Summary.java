/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.time.Duration;
/*    */ import java.util.List;
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
/*    */   private final long totalBytes;
/*    */   private final double bytesPerSecond;
/*    */   private final long counts;
/*    */   private final double countsPerSecond;
/*    */   private final Duration timeSpentInIO;
/*    */   private final List<Pair<String, Long>> topTenContributorsByTotalBytes;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 30 */   public Summary(long totalBytes, double bytesPerSecond, long counts, double countsPerSecond, Duration timeSpentInIO, List<Pair<String, Long>> topTenContributorsByTotalBytes) { this.totalBytes = totalBytes; this.bytesPerSecond = bytesPerSecond; this.counts = counts; this.countsPerSecond = countsPerSecond; this.timeSpentInIO = timeSpentInIO; this.topTenContributorsByTotalBytes = topTenContributorsByTotalBytes; } public long totalBytes() { return this.totalBytes; } public double bytesPerSecond() { return this.bytesPerSecond; } public long counts() { return this.counts; } public double countsPerSecond() { return this.countsPerSecond; } public Duration timeSpentInIO() { return this.timeSpentInIO; } public List<Pair<String, Long>> topTenContributorsByTotalBytes() { return this.topTenContributorsByTotalBytes; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\stats\FileIOStat$Summary.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */