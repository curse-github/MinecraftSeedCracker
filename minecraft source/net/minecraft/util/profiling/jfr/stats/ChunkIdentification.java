/*    */ package net.minecraft.util.profiling.jfr.stats;public final class ChunkIdentification extends Record { private final String level;
/*    */   private final String dimension;
/*    */   private final int x;
/*    */   private final int z;
/*    */   
/*  6 */   public ChunkIdentification(String level, String dimension, int x, int z) { this.level = level; this.dimension = dimension; this.x = x; this.z = z; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  6 */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification; } public String level() { return this.level; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification;
/*  6 */     //   0	8	1	o	Ljava/lang/Object; } public String dimension() { return this.dimension; } public int x() { return this.x; } public int z() { return this.z; }
/*    */   public static ChunkIdentification from(RecordedEvent event) {
/*  8 */     return new ChunkIdentification(event
/*  9 */         .getString("level"), event
/* 10 */         .getString("dimension"), event
/* 11 */         .getInt("chunkPosX"), event
/* 12 */         .getInt("chunkPosZ"));
/*    */   } }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\stats\ChunkIdentification.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */