/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*    */ 
/*    */ public abstract class ScatteredFeaturePiece
/*    */   extends StructurePiece {
/*    */   protected final int width;
/*    */   protected final int height;
/*    */   protected final int depth;
/* 16 */   protected int heightPosition = -1;
/*    */   
/*    */   protected ScatteredFeaturePiece(StructurePieceType type, int west, int floor, int north, int width, int height, int depth, Direction direction) {
/* 19 */     super(type, 0, StructurePiece.makeBoundingBox(west, floor, north, direction, width, height, depth));
/*    */     
/* 21 */     this.width = width;
/* 22 */     this.height = height;
/* 23 */     this.depth = depth;
/*    */     
/* 25 */     setOrientation(direction);
/*    */   }
/*    */   
/*    */   protected ScatteredFeaturePiece(StructurePieceType type, CompoundTag tag) {
/* 29 */     super(type, tag);
/* 30 */     this.width = tag.getIntOr("Width", 0);
/* 31 */     this.height = tag.getIntOr("Height", 0);
/* 32 */     this.depth = tag.getIntOr("Depth", 0);
/* 33 */     this.heightPosition = tag.getIntOr("HPos", 0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 38 */     tag.putInt("Width", this.width);
/* 39 */     tag.putInt("Height", this.height);
/* 40 */     tag.putInt("Depth", this.depth);
/* 41 */     tag.putInt("HPos", this.heightPosition);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean updateAverageGroundHeight(LevelAccessor level, BoundingBox chunkBB, int offset) {
/* 46 */     if (this.heightPosition >= 0) {
/* 47 */       return true;
/*    */     }
/*    */     
/* 50 */     int total = 0;
/* 51 */     int count = 0;
/* 52 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 53 */     for (int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); z++) {
/* 54 */       for (int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); x++) {
/* 55 */         pos.set(x, 64, z);
/* 56 */         if (chunkBB.isInside(pos)) {
/* 57 */           total += level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos).getY();
/* 58 */           count++;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 63 */     if (count == 0) {
/* 64 */       return false;
/*    */     }
/* 66 */     this.heightPosition = total / count;
/* 67 */     this.boundingBox.move(0, this.heightPosition - this.boundingBox.minY() + offset, 0);
/* 68 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean updateHeightPositionToLowestGroundHeight(LevelAccessor level, int offset) {
/* 73 */     if (this.heightPosition >= 0) {
/* 74 */       return true;
/*    */     }
/*    */     
/* 77 */     int lowestGroundHeight = level.getMaxY() + 1;
/* 78 */     boolean foundPositionWithinBoundingBox = false;
/* 79 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 80 */     for (int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); z++) {
/* 81 */       for (int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); x++) {
/*    */         
/* 83 */         pos.set(x, 0, z);
/* 84 */         lowestGroundHeight = Math.min(lowestGroundHeight, level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos).getY());
/* 85 */         foundPositionWithinBoundingBox = true;
/*    */       } 
/*    */     } 
/*    */     
/* 89 */     if (!foundPositionWithinBoundingBox) {
/* 90 */       return false;
/*    */     }
/* 92 */     this.heightPosition = lowestGroundHeight;
/* 93 */     this.boundingBox.move(0, this.heightPosition - this.boundingBox.minY() + offset, 0);
/* 94 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\ScatteredFeaturePiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */