/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.CarvingMask;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.chunk.ProtoChunk;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public class PlacementContext
/*    */   extends WorldGenerationContext {
/*    */   private final WorldGenLevel level;
/*    */   private final ChunkGenerator generator;
/*    */   private final Optional<PlacedFeature> topFeature;
/*    */   
/*    */   public PlacementContext(WorldGenLevel level, ChunkGenerator generator, Optional<PlacedFeature> topFeature) {
/* 21 */     super(generator, level);
/* 22 */     this.level = level;
/* 23 */     this.generator = generator;
/* 24 */     this.topFeature = topFeature;
/*    */   }
/*    */ 
/*    */   
/* 28 */   public int getHeight(Heightmap.Types type, int x, int z) { return this.level.getHeight(type, x, z); }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public CarvingMask getCarvingMask(ChunkPos pos) { return ((ProtoChunk)this.level.getChunk(pos.x, pos.z)).getOrCreateCarvingMask(); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public BlockState getBlockState(BlockPos pos) { return this.level.getBlockState(pos); }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public int getMinY() { return this.level.getMinY(); }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public WorldGenLevel getLevel() { return this.level; }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public Optional<PlacedFeature> topFeature() { return this.topFeature; }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public ChunkGenerator generator() { return this.generator; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\PlacementContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */