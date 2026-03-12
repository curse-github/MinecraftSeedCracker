/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ 
/*    */ 
/*    */ public class BuriedTreasurePiece
/*    */   extends StructurePiece
/*    */ {
/* 24 */   public BuriedTreasurePiece(BlockPos offset) { super(StructurePieceType.BURIED_TREASURE_PIECE, 0, new BoundingBox(offset)); }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public BuriedTreasurePiece(CompoundTag tag) { super(StructurePieceType.BURIED_TREASURE_PIECE, tag); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 37 */     int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, this.boundingBox.minX(), this.boundingBox.minZ());
/* 38 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(this.boundingBox.minX(), y, this.boundingBox.minZ());
/*    */     
/* 40 */     while (pos.getY() > level.getMinY()) {
/* 41 */       BlockState currentState = level.getBlockState(pos);
/* 42 */       BlockState belowState = level.getBlockState(pos.below());
/*    */       
/* 44 */       if (belowState == Blocks.SANDSTONE.defaultBlockState() || belowState == Blocks.STONE
/* 45 */         .defaultBlockState() || belowState == Blocks.ANDESITE
/* 46 */         .defaultBlockState() || belowState == Blocks.GRANITE
/* 47 */         .defaultBlockState() || belowState == Blocks.DIORITE
/* 48 */         .defaultBlockState()) {
/*    */         
/* 50 */         BlockState softState = (currentState.isAir() || isLiquid(currentState)) ? Blocks.SAND.defaultBlockState() : currentState;
/*    */         
/* 52 */         for (Direction direction : Direction.values()) {
/* 53 */           BlockPos relativePos = pos.relative(direction);
/* 54 */           BlockState relativeState = level.getBlockState(relativePos);
/*    */           
/* 56 */           if (relativeState.isAir() || isLiquid(relativeState)) {
/* 57 */             BlockPos belowRelativePos = relativePos.below();
/* 58 */             BlockState belowRelativeState = level.getBlockState(belowRelativePos);
/*    */             
/* 60 */             if ((belowRelativeState.isAir() || isLiquid(belowRelativeState)) && direction != Direction.UP) {
/* 61 */               level.setBlock(relativePos, belowState, 3);
/*    */             } else {
/* 63 */               level.setBlock(relativePos, softState, 3);
/*    */             } 
/*    */           } 
/*    */         } 
/* 67 */         this.boundingBox = new BoundingBox(pos);
/* 68 */         createChest(level, chunkBB, random, pos, BuiltInLootTables.BURIED_TREASURE, null);
/*    */         
/*    */         return;
/*    */       } 
/* 72 */       pos.move(0, -1, 0);
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean isLiquid(BlockState blockState) {
/* 77 */     return (blockState == Blocks.WATER.defaultBlockState() || blockState == Blocks.LAVA
/* 78 */       .defaultBlockState());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\BuriedTreasurePieces$BuriedTreasurePiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */