/*    */ package net.minecraft.world;public final class Stopwatch extends Record { private final long creationTime; private final long accumulatedElapsedTime;
/*    */   
/*  3 */   public Stopwatch(long creationTime, long accumulatedElapsedTime) { this.creationTime = creationTime; this.accumulatedElapsedTime = accumulatedElapsedTime; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/Stopwatch;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  3 */     //   0	7	0	this	Lnet/minecraft/world/Stopwatch; } public long creationTime() { return this.creationTime; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/Stopwatch;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/Stopwatch; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/Stopwatch;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/Stopwatch;
/*  3 */     //   0	8	1	o	Ljava/lang/Object; } public long accumulatedElapsedTime() { return this.accumulatedElapsedTime; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*  8 */   public Stopwatch(long creationTime) { this(creationTime, 0L); }
/*    */ 
/*    */   
/*    */   public long elapsedMilliseconds(long currentTime) {
/* 12 */     long timeSinceInstanceCreation = currentTime - this.creationTime;
/* 13 */     return this.accumulatedElapsedTime + timeSinceInstanceCreation;
/*    */   }
/*    */ 
/*    */   
/* 17 */   public double elapsedSeconds(long currentTime) { return elapsedMilliseconds(currentTime) / 1000.0D; } }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\Stopwatch.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */