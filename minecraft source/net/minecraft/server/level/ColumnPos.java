/*    */ package net.minecraft.server.level;
/*    */ public final class ColumnPos extends Record {
/*    */   private final int x;
/*    */   private final int z;
/*    */   
/*  6 */   public ColumnPos(int x, int z) { this.x = x; this.z = z; } private static final long COORD_BITS = 32L; private static final long COORD_MASK = 4294967295L; public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ColumnPos;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/level/ColumnPos;
/*  6 */     //   0	8	1	o	Ljava/lang/Object; } public int x() { return this.x; } public int z() { return this.z; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 11 */   public ChunkPos toChunkPos() { return new ChunkPos(SectionPos.blockToSectionCoord(this.x), SectionPos.blockToSectionCoord(this.z)); }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public long toLong() { return asLong(this.x, this.z); }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static long asLong(int x, int z) { return x & 0xFFFFFFFFL | (z & 0xFFFFFFFFL) << 32; }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static int getX(long pos) { return (int)(pos & 0xFFFFFFFFL); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static int getZ(long pos) { return (int)(pos >>> 32 & 0xFFFFFFFFL); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public String toString() { return "[" + this.x + ", " + this.z + "]"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public int hashCode() { return ChunkPos.hash(this.x, this.z); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ColumnPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */