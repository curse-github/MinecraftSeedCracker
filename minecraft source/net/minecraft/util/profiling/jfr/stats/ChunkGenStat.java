/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ import jdk.jfr.consumer.RecordedEvent;
/*    */ 
/*    */ public final class ChunkGenStat extends Record implements TimedStat {
/*    */   private final Duration duration;
/*    */   private final ChunkPos chunkPos;
/*    */   private final ColumnPos worldPos;
/*    */   private final ChunkStatus status;
/*    */   private final String level;
/*    */   
/* 11 */   public ChunkGenStat(Duration duration, ChunkPos chunkPos, ColumnPos worldPos, ChunkStatus status, String level) { this.duration = duration; this.chunkPos = chunkPos; this.worldPos = worldPos; this.status = status; this.level = level; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat; } public Duration duration() { return this.duration; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public ChunkPos chunkPos() { return this.chunkPos; } public ColumnPos worldPos() { return this.worldPos; } public ChunkStatus status() { return this.status; } public String level() { return this.level; }
/*    */   
/*    */   public static ChunkGenStat from(RecordedEvent event) {
/* 14 */     return new ChunkGenStat(event.getDuration(), new ChunkPos(event
/* 15 */           .getInt("chunkPosX"), event.getInt("chunkPosX")), new ColumnPos(event
/* 16 */           .getInt("worldPosX"), event.getInt("worldPosZ")), 
/* 17 */         ChunkStatus.byName(event.getString("status")), event
/* 18 */         .getString("level"));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\stats\ChunkGenStat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */