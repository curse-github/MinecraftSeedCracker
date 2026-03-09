/*   */ package net.minecraft.world.level.chunk.storage;public final class RegionStorageInfo extends Record {
/*   */   private final String level;
/*   */   private final ResourceKey<Level> dimension;
/*   */   private final String type;
/*   */   
/* 6 */   public RegionStorageInfo(String level, ResourceKey<Level> dimension, String type) { this.level = level; this.dimension = dimension; this.type = type; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/storage/RegionStorageInfo;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 6 */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/storage/RegionStorageInfo; } public String level() { return this.level; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/storage/RegionStorageInfo;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/storage/RegionStorageInfo; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/storage/RegionStorageInfo;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/storage/RegionStorageInfo;
/* 6 */     //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<Level> dimension() { return this.dimension; } public String type() { return this.type; }
/*   */   
/* 8 */   public RegionStorageInfo withTypeSuffix(String suffix) { return new RegionStorageInfo(this.level, this.dimension, this.type + this.type); }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\storage\RegionStorageInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */