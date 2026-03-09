/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.BlockColumn;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   implements BlockColumn
/*     */ {
/*     */   null(SurfaceSystem this$0) {}
/*     */   
/*  91 */   public BlockState getBlock(int blockY) { return protoChunk.getBlockState(columnPos.setY(blockY)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlock(int blockY, BlockState state) {
/*  96 */     LevelHeightAccessor heightAccessor = protoChunk.getHeightAccessorForGeneration();
/*  97 */     if (heightAccessor.isInsideBuildHeight(blockY)) {
/*  98 */       protoChunk.setBlockState(columnPos.setY(blockY), state);
/*  99 */       if (!state.getFluidState().isEmpty()) {
/* 100 */         protoChunk.markPosForPostprocessing(columnPos);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public String toString() { return "ChunkBlockColumn " + String.valueOf(chunkPos); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\SurfaceSystem$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */