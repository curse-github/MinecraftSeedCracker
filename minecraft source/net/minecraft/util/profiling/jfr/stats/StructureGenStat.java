/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ 
/*    */ public final class StructureGenStat extends Record implements TimedStat {
/*    */   private final Duration duration;
/*    */   private final ChunkPos chunkPos;
/*    */   private final String structureName;
/*    */   private final String level;
/*    */   private final boolean success;
/*    */   
/* 10 */   public StructureGenStat(Duration duration, ChunkPos chunkPos, String structureName, String level, boolean success) { this.duration = duration; this.chunkPos = chunkPos; this.structureName = structureName; this.level = level; this.success = success; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/StructureGenStat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/StructureGenStat; } public Duration duration() { return this.duration; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/StructureGenStat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/StructureGenStat; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/StructureGenStat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/StructureGenStat;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public ChunkPos chunkPos() { return this.chunkPos; } public String structureName() { return this.structureName; } public String level() { return this.level; } public boolean success() { return this.success; }
/*    */   
/*    */   public static StructureGenStat from(RecordedEvent event) {
/* 13 */     return new StructureGenStat(event.getDuration(), new ChunkPos(event
/* 14 */           .getInt("chunkPosX"), event.getInt("chunkPosX")), event
/* 15 */         .getString("structure"), event
/* 16 */         .getString("level"), event
/* 17 */         .getBoolean("success"));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\stats\StructureGenStat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */