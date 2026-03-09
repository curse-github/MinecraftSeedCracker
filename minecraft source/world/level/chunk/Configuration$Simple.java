/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Simple
/*    */   extends Record
/*    */   implements Configuration
/*    */ {
/*    */   private final Palette.Factory factory;
/*    */   private final int bits;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/Configuration$Simple;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/Configuration$Simple; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/Configuration$Simple;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/Configuration$Simple; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/Configuration$Simple;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/Configuration$Simple;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 22 */   public Simple(Palette.Factory factory, int bits) { this.factory = factory; this.bits = bits; } public Palette.Factory factory() { return this.factory; } public int bits() { return this.bits; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public boolean alwaysRepack() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public <T> Palette<T> createPalette(Strategy<T> strategy, List<T> paletteEntries) { return this.factory.create(this.bits, paletteEntries); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public int bitsInMemory() { return this.bits; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public int bitsInStorage() { return this.bits; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\Configuration$Simple.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */