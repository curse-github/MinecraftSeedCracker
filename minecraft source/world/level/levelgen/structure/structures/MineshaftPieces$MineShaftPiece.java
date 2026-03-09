/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
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
/*     */ abstract class MineShaftPiece
/*     */   extends StructurePiece
/*     */ {
/*     */   protected MineshaftStructure.Type type;
/*     */   
/*     */   public MineShaftPiece(StructurePieceType pieceType, int genDepth, MineshaftStructure.Type type, BoundingBox boundingBox) {
/*  57 */     super(pieceType, genDepth, boundingBox);
/*  58 */     this.type = type;
/*     */   }
/*     */   
/*     */   public MineShaftPiece(StructurePieceType type, CompoundTag tag) {
/*  62 */     super(type, tag);
/*  63 */     this.type = MineshaftStructure.Type.byId(tag.getIntOr("MST", 0));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(LevelReader level, int x, int y, int z, BoundingBox chunkBB) {
/*  69 */     BlockState state = getBlock(level, x, y, z, chunkBB);
/*  70 */     return (!state.is(this.type.getPlanksState().getBlock()) && 
/*  71 */       !state.is(this.type.getWoodState().getBlock()) && 
/*  72 */       !state.is(this.type.getFenceState().getBlock()) && 
/*  73 */       !state.is(Blocks.IRON_CHAIN));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  78 */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) { tag.putInt("MST", this.type.ordinal()); }
/*     */ 
/*     */   
/*     */   protected boolean isSupportingBox(BlockGetter level, BoundingBox chunkBB, int x0, int x1, int y1, int z0) {
/*  82 */     for (int x = x0; x <= x1; x++) {
/*  83 */       if (getBlock(level, x, y1 + 1, z0, chunkBB).isAir()) {
/*  84 */         return false;
/*     */       }
/*     */     } 
/*  87 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean isInInvalidLocation(LevelAccessor level, BoundingBox chunkBB) {
/*  91 */     int x0 = Math.max(this.boundingBox.minX() - 1, chunkBB.minX());
/*  92 */     int y0 = Math.max(this.boundingBox.minY() - 1, chunkBB.minY());
/*  93 */     int z0 = Math.max(this.boundingBox.minZ() - 1, chunkBB.minZ());
/*  94 */     int x1 = Math.min(this.boundingBox.maxX() + 1, chunkBB.maxX());
/*  95 */     int y1 = Math.min(this.boundingBox.maxY() + 1, chunkBB.maxY());
/*  96 */     int z1 = Math.min(this.boundingBox.maxZ() + 1, chunkBB.maxZ());
/*     */     
/*  98 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos((x0 + x1) / 2, (y0 + y1) / 2, (z0 + z1) / 2);
/*     */     
/* 100 */     if (level.getBiome(blockPos).is(BiomeTags.MINESHAFT_BLOCKING)) {
/* 101 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 105 */     for (int x = x0; x <= x1; x++) {
/* 106 */       for (int z = z0; z <= z1; z++) {
/* 107 */         if (level.getBlockState(blockPos.set(x, y0, z)).liquid()) {
/* 108 */           return true;
/*     */         }
/* 110 */         if (level.getBlockState(blockPos.set(x, y1, z)).liquid()) {
/* 111 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 116 */     for (int x = x0; x <= x1; x++) {
/* 117 */       for (int y = y0; y <= y1; y++) {
/* 118 */         if (level.getBlockState(blockPos.set(x, y, z0)).liquid()) {
/* 119 */           return true;
/*     */         }
/* 121 */         if (level.getBlockState(blockPos.set(x, y, z1)).liquid()) {
/* 122 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 127 */     for (int z = z0; z <= z1; z++) {
/* 128 */       for (int y = y0; y <= y1; y++) {
/* 129 */         if (level.getBlockState(blockPos.set(x0, y, z)).liquid()) {
/* 130 */           return true;
/*     */         }
/* 132 */         if (level.getBlockState(blockPos.set(x1, y, z)).liquid()) {
/* 133 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 137 */     return false;
/*     */   }
/*     */   
/*     */   protected void setPlanksBlock(WorldGenLevel level, BoundingBox chunkBB, BlockState planksBlock, int x, int y, int z) {
/* 141 */     if (!isInterior(level, x, y, z, chunkBB)) {
/*     */       return;
/*     */     }
/* 144 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(x, y, z);
/* 145 */     BlockState existingState = level.getBlockState(mutableBlockPos);
/* 146 */     if (!existingState.isFaceSturdy(level, mutableBlockPos, Direction.UP))
/*     */     {
/* 148 */       level.setBlock(mutableBlockPos, planksBlock, 2);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\MineshaftPieces$MineShaftPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */