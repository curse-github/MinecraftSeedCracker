/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.FullChunkStatus;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class EmptyLevelChunk
/*    */   extends LevelChunk {
/*    */   private final Holder<Biome> biome;
/*    */   
/*    */   public EmptyLevelChunk(Level level, ChunkPos pos, Holder<Biome> biome) {
/* 21 */     super(level, pos);
/* 22 */     this.biome = biome;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public BlockState getBlockState(BlockPos pos) { return Blocks.VOID_AIR.defaultBlockState(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public BlockState setBlockState(BlockPos pos, BlockState state, @UpdateFlags int flags) { return null; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public FluidState getFluidState(BlockPos pos) { return Fluids.EMPTY.defaultFluidState(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public int getLightEmission(BlockPos pos) { return 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public BlockEntity getBlockEntity(BlockPos pos, LevelChunk.EntityCreationType creationType) { return null; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addAndRegisterBlockEntity(BlockEntity blockEntity) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBlockEntity(BlockEntity blockEntity) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void removeBlockEntity(BlockPos pos) {}
/*    */ 
/*    */ 
/*    */   
/* 64 */   public boolean isEmpty() { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 69 */   public boolean isYSpaceEmpty(int yStartInclusive, int yEndInclusive) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   public FullChunkStatus getFullStatus() { return FullChunkStatus.FULL; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 79 */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ) { return this.biome; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\EmptyLevelChunk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */