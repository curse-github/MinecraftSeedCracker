/*   */ package net.minecraft.util.profiling.jfr.stats;
/*   */ public final class FpsStat extends Record {
/*   */   private final int fps;
/*   */   
/* 5 */   public FpsStat(int fps) { this.fps = fps; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/FpsStat;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 5 */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/FpsStat; } public int fps() { return this.fps; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/FpsStat;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/FpsStat; }
/*   */   public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/FpsStat;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/FpsStat;
/*   */     //   0	8	1	o	Ljava/lang/Object; }
/* 7 */   public static FpsStat from(RecordedEvent event, String field) { return new FpsStat(event.getInt(field)); }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\stats\FpsStat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */