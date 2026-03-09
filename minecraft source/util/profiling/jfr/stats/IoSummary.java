/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.time.Duration;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class IoSummary<T>
/*    */   extends Object {
/*    */   private final CountAndSize totalCountAndSize;
/*    */   private final List<Pair<T, CountAndSize>> largestSizeContributors;
/*    */   private final Duration recordingDuration;
/*    */   
/*    */   public IoSummary(Duration recordingDuration, List<Pair<T, CountAndSize>> packetStats) {
/* 15 */     this.recordingDuration = recordingDuration;
/* 16 */     this
/*    */       
/* 18 */       .totalCountAndSize = (CountAndSize)packetStats.stream().map(Pair::getSecond).reduce(new CountAndSize(0L, 0L), CountAndSize::add);
/*    */     
/* 20 */     this
/*    */ 
/*    */       
/* 23 */       .largestSizeContributors = packetStats.stream().sorted(Comparator.comparing(Pair::getSecond, CountAndSize.SIZE_THEN_COUNT)).limit(10L).toList();
/*    */   }
/*    */ 
/*    */   
/* 27 */   public double getCountsPerSecond() { return this.totalCountAndSize.totalCount / this.recordingDuration.getSeconds(); }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public double getSizePerSecond() { return this.totalCountAndSize.totalSize / this.recordingDuration.getSeconds(); }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public long getTotalCount() { return this.totalCountAndSize.totalCount; }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public long getTotalSize() { return this.totalCountAndSize.totalSize; }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public List<Pair<T, CountAndSize>> largestSizeContributors() { return this.largestSizeContributors; }
/*    */   public static final class CountAndSize extends Record { private final long totalCount; private final long totalSize;
/*    */     
/* 46 */     public CountAndSize(long totalCount, long totalSize) { this.totalCount = totalCount; this.totalSize = totalSize; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #46	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 46 */       //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize; } public long totalCount() { return this.totalCount; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #46	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #46	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize;
/* 46 */       //   0	8	1	o	Ljava/lang/Object; } public long totalSize() { return this.totalSize; }
/* 47 */     private static final Comparator<CountAndSize> SIZE_THEN_COUNT = Comparator.comparing(CountAndSize::totalSize).thenComparing(CountAndSize::totalCount).reversed();
/*    */ 
/*    */     
/* 50 */     CountAndSize add(CountAndSize that) { return new CountAndSize(this.totalCount + that.totalCount, this.totalSize + that.totalSize); }
/*    */ 
/*    */ 
/*    */     
/* 54 */     public float averageSize() { return (float)this.totalSize / (float)this.totalCount; } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\stats\IoSummary.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */