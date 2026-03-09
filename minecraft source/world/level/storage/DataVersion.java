/*    */ package net.minecraft.world.level.storage;public final class DataVersion extends Record { private final int version;
/*    */   private final String series;
/*    */   public static final String MAIN_SERIES = "main";
/*    */   
/*  5 */   public DataVersion(int version, String series) { this.version = version; this.series = series; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/DataVersion;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  5 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/DataVersion; } public int version() { return this.version; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/DataVersion;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/DataVersion; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/DataVersion;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/DataVersion;
/*  5 */     //   0	8	1	o	Ljava/lang/Object; } public String series() { return this.series; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 12 */   public boolean isSideSeries() { return !this.series.equals("main"); }
/*    */ 
/*    */   
/*    */   public boolean isCompatible(DataVersion other) {
/* 16 */     if (SharedConstants.DEBUG_OPEN_INCOMPATIBLE_WORLDS) {
/* 17 */       return true;
/*    */     }
/* 19 */     return series().equals(other.series());
/*    */   } }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\DataVersion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */