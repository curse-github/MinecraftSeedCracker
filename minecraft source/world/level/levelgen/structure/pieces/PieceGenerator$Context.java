/*    */ package net.minecraft.world.level.levelgen.structure.pieces;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ 
/*    */ public final class Context<C extends FeatureConfiguration> extends Record {
/*    */   private final C config;
/*    */   private final ChunkGenerator chunkGenerator;
/*    */   private final StructureTemplateManager structureTemplateManager;
/*    */   private final ChunkPos chunkPos;
/*    */   private final LevelHeightAccessor heightAccessor;
/*    */   private final WorldgenRandom random;
/*    */   private final long seed;
/*    */   
/* 14 */   public Context(C config, ChunkGenerator chunkGenerator, StructureTemplateManager structureTemplateManager, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, WorldgenRandom random, long seed) { this.config = config; this.chunkGenerator = chunkGenerator; this.structureTemplateManager = structureTemplateManager; this.chunkPos = chunkPos; this.heightAccessor = heightAccessor; this.random = random; this.seed = seed; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context<TC;>; } public C config() { return (C)this.config; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context<TC;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 14 */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context<TC;>; } public ChunkGenerator chunkGenerator() { return this.chunkGenerator; } public StructureTemplateManager structureTemplateManager() { return this.structureTemplateManager; } public ChunkPos chunkPos() { return this.chunkPos; } public LevelHeightAccessor heightAccessor() { return this.heightAccessor; } public WorldgenRandom random() { return this.random; } public long seed() { return this.seed; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pieces\PieceGenerator$Context.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */