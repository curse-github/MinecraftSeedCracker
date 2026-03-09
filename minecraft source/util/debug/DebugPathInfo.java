/*    */ package net.minecraft.util.debug;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ 
/*    */ public final class DebugPathInfo extends Record {
/*    */   private final Path path;
/*    */   private final float maxNodeDistance;
/*    */   
/*  8 */   public DebugPathInfo(Path path, float maxNodeDistance) { this.path = path; this.maxNodeDistance = maxNodeDistance; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugPathInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugPathInfo; } public Path path() { return this.path; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugPathInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugPathInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugPathInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugPathInfo;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public float maxNodeDistance() { return this.maxNodeDistance; }
/*    */ 
/*    */ 
/*    */   
/* 12 */   public static final StreamCodec<FriendlyByteBuf, DebugPathInfo> STREAM_CODEC = StreamCodec.composite(Path.STREAM_CODEC, DebugPathInfo::path, ByteBufCodecs.FLOAT, DebugPathInfo::maxNodeDistance, DebugPathInfo::new);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debug\DebugPathInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */