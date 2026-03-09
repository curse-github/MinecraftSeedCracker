/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ 
/*    */ import java.util.Comparator;
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
/*    */ public final class CountAndSize
/*    */   extends Record
/*    */ {
/*    */   private final long totalCount;
/*    */   private final long totalSize;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #46	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #46	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #46	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 46 */   public CountAndSize(long totalCount, long totalSize) { this.totalCount = totalCount; this.totalSize = totalSize; } public long totalCount() { return this.totalCount; } public long totalSize() { return this.totalSize; }
/* 47 */   private static final Comparator<CountAndSize> SIZE_THEN_COUNT = Comparator.comparing(CountAndSize::totalSize).thenComparing(CountAndSize::totalCount).reversed();
/*    */ 
/*    */   
/* 50 */   CountAndSize add(CountAndSize that) { return new CountAndSize(this.totalCount + that.totalCount, this.totalSize + that.totalSize); }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public float averageSize() { return (float)this.totalSize / (float)this.totalCount; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\stats\IoSummary$CountAndSize.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */