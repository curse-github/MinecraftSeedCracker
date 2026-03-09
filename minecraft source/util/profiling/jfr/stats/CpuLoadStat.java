/*    */ package net.minecraft.util.profiling.jfr.stats;public final class CpuLoadStat extends Record { private final double jvm;
/*    */   private final double userJvm;
/*    */   private final double system;
/*    */   
/*  5 */   public CpuLoadStat(double jvm, double userJvm, double system) { this.jvm = jvm; this.userJvm = userJvm; this.system = system; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/CpuLoadStat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  5 */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/CpuLoadStat; } public double jvm() { return this.jvm; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/CpuLoadStat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/CpuLoadStat; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/CpuLoadStat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/CpuLoadStat;
/*  5 */     //   0	8	1	o	Ljava/lang/Object; } public double userJvm() { return this.userJvm; } public double system() { return this.system; }
/*    */   
/*    */   public static CpuLoadStat from(RecordedEvent event) {
/*  8 */     return new CpuLoadStat(event.getFloat("jvmSystem"), event
/*  9 */         .getFloat("jvmUser"), event
/* 10 */         .getFloat("machineTotal"));
/*    */   } }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\stats\CpuLoadStat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */