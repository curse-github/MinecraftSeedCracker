/*   */ package net.minecraft.world.level;public final class BlockEventData extends Record { private final BlockPos pos;
/*   */   private final Block block;
/*   */   private final int paramA;
/*   */   private final int paramB;
/*   */   
/* 6 */   public BlockEventData(BlockPos pos, Block block, int paramA, int paramB) { this.pos = pos; this.block = block; this.paramA = paramA; this.paramB = paramB; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/BlockEventData;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 6 */     //   0	7	0	this	Lnet/minecraft/world/level/BlockEventData; } public BlockPos pos() { return this.pos; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/BlockEventData;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/BlockEventData; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/BlockEventData;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/level/BlockEventData;
/* 6 */     //   0	8	1	o	Ljava/lang/Object; } public Block block() { return this.block; } public int paramA() { return this.paramA; } public int paramB() { return this.paramB; } }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\BlockEventData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */