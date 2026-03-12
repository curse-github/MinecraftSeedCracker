/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ import java.time.Duration;
/*    */ import java.time.Instant;
/*    */ 
/*    */ public final class TickTimeStat extends Record {
/*    */   private final Instant timestamp;
/*    */   private final Duration currentAverage;
/*    */   
/*  9 */   public TickTimeStat(Instant timestamp, Duration currentAverage) { this.timestamp = timestamp; this.currentAverage = currentAverage; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/TickTimeStat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/TickTimeStat; } public Instant timestamp() { return this.timestamp; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/TickTimeStat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/TickTimeStat; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/TickTimeStat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/TickTimeStat;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public Duration currentAverage() { return this.currentAverage; }
/*    */   
/* 11 */   public static TickTimeStat from(RecordedEvent event) { return new TickTimeStat(event.getStartTime(), event.getDuration("averageTickDuration")); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\stats\TickTimeStat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */