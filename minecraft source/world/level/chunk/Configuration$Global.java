/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Global
/*    */   extends Record
/*    */   implements Configuration
/*    */ {
/*    */   private final int bitsInMemory;
/*    */   private final int bitsInStorage;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/Configuration$Global;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/Configuration$Global; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/Configuration$Global;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/Configuration$Global; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/Configuration$Global;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/Configuration$Global;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 47 */   public Global(int bitsInMemory, int bitsInStorage) { this.bitsInMemory = bitsInMemory; this.bitsInStorage = bitsInStorage; } public int bitsInMemory() { return this.bitsInMemory; } public int bitsInStorage() { return this.bitsInStorage; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public boolean alwaysRepack() { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public <T> Palette<T> createPalette(Strategy<T> strategy, List<T> paletteEntries) { return strategy.globalPalette(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\Configuration$Global.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */