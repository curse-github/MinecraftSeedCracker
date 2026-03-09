/*   */ package net.minecraft.world.level.storage;public final class LevelDataAndDimensions extends Record {
/*   */   private final WorldData worldData;
/*   */   private final WorldDimensions.Complete dimensions;
/*   */   
/* 5 */   public LevelDataAndDimensions(WorldData worldData, WorldDimensions.Complete dimensions) { this.worldData = worldData; this.dimensions = dimensions; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/LevelDataAndDimensions;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 5 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelDataAndDimensions; } public WorldData worldData() { return this.worldData; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/LevelDataAndDimensions;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelDataAndDimensions; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/LevelDataAndDimensions;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/level/storage/LevelDataAndDimensions;
/* 5 */     //   0	8	1	o	Ljava/lang/Object; } public WorldDimensions.Complete dimensions() { return this.dimensions; }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\LevelDataAndDimensions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */