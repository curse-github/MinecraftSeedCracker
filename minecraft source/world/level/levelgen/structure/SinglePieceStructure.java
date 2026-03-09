/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class SinglePieceStructure
/*    */   extends Structure
/*    */ {
/*    */   private final PieceConstructor constructor;
/*    */   private final int width;
/*    */   private final int depth;
/*    */   
/*    */   protected SinglePieceStructure(PieceConstructor constructor, int width, int depth, Structure.StructureSettings settings) {
/* 21 */     super(settings);
/* 22 */     this.constructor = constructor;
/* 23 */     this.width = width;
/* 24 */     this.depth = depth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
/* 29 */     if (getLowestY(context, this.width, this.depth) < context.chunkGenerator().getSeaLevel()) {
/* 30 */       return Optional.empty();
/*    */     }
/*    */     
/* 33 */     return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, builder -> generatePieces(builder, context));
/*    */   }
/*    */   
/*    */   private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
/* 37 */     ChunkPos chunkPos = context.chunkPos();
/* 38 */     builder.addPiece(this.constructor.construct(context.random(), chunkPos.getMinBlockX(), chunkPos.getMinBlockZ()));
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   protected static interface PieceConstructor {
/*    */     StructurePiece construct(WorldgenRandom param1WorldgenRandom, int param1Int1, int param1Int2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\SinglePieceStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */