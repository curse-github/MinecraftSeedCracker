/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface PostPlacementProcessor {
/*    */   public static final PostPlacementProcessor NONE = (level, structureManager, generator, random, chunkBB, chunkPos, pieces) -> {
/*    */     
/*    */     };
/*    */   
/*    */   void afterPlace(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, PiecesContainer paramPiecesContainer);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\PostPlacementProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */