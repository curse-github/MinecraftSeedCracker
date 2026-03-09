/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FillerCorridor
/*     */   extends StrongholdPieces.StrongholdPiece
/*     */ {
/*     */   private final int steps;
/*     */   
/*     */   public FillerCorridor(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 364 */     super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, genDepth, boundingBox);
/*     */     
/* 366 */     setOrientation(direction);
/* 367 */     this.steps = (direction == Direction.NORTH || direction == Direction.SOUTH) ? boundingBox.getZSpan() : boundingBox.getXSpan();
/*     */   }
/*     */   
/*     */   public FillerCorridor(CompoundTag tag) {
/* 371 */     super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, tag);
/* 372 */     this.steps = tag.getIntOr("Steps", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 377 */     super.addAdditionalSaveData(context, tag);
/* 378 */     tag.putInt("Steps", this.steps);
/*     */   }
/*     */   
/*     */   public static BoundingBox findPieceBox(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction) {
/* 382 */     int maxLength = 3;
/*     */     
/* 384 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 4, direction);
/*     */     
/* 386 */     StructurePiece collisionPiece = structurePieceAccessor.findCollisionPiece(box);
/* 387 */     if (collisionPiece == null)
/*     */     {
/* 389 */       return null;
/*     */     }
/*     */     
/* 392 */     if (collisionPiece.getBoundingBox().minY() == box.minY())
/*     */     {
/* 394 */       for (int depth = 2; depth >= 1; depth--) {
/* 395 */         box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, depth, direction);
/* 396 */         if (!collisionPiece.getBoundingBox().intersects(box))
/*     */         {
/*     */           
/* 399 */           return BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, depth + 1, direction);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 404 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 410 */     for (int i = 0; i < this.steps; i++) {
/*     */       
/* 412 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 0, 0, i, chunkBB);
/* 413 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 0, i, chunkBB);
/* 414 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 0, i, chunkBB);
/* 415 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 0, i, chunkBB);
/* 416 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 4, 0, i, chunkBB);
/*     */       
/* 418 */       for (int y = 1; y <= 3; y++) {
/* 419 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 0, y, i, chunkBB);
/* 420 */         placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), 1, y, i, chunkBB);
/* 421 */         placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), 2, y, i, chunkBB);
/* 422 */         placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), 3, y, i, chunkBB);
/* 423 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 4, y, i, chunkBB);
/*     */       } 
/*     */       
/* 426 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 0, 4, i, chunkBB);
/* 427 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 4, i, chunkBB);
/* 428 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 4, i, chunkBB);
/* 429 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 4, i, chunkBB);
/* 430 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 4, 4, i, chunkBB);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$FillerCorridor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */