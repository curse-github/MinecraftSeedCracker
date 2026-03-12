/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkSource;
/*     */ import net.minecraft.world.level.chunk.EmptyLevelChunk;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathNavigationRegion
/*     */   implements CollisionGetter
/*     */ {
/*     */   protected final int centerX;
/*     */   protected final int centerZ;
/*     */   protected final ChunkAccess[][] chunks;
/*     */   protected boolean allEmpty;
/*     */   protected final Level level;
/*     */   private final Supplier<Holder<Biome>> plains;
/*     */   
/*     */   public PathNavigationRegion(Level level, BlockPos start, BlockPos end) {
/*  39 */     this.level = level;
/*     */     
/*  41 */     this.plains = Suppliers.memoize(() -> level.registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS));
/*     */     
/*  43 */     this.centerX = SectionPos.blockToSectionCoord(start.getX());
/*  44 */     this.centerZ = SectionPos.blockToSectionCoord(start.getZ());
/*  45 */     int xc2 = SectionPos.blockToSectionCoord(end.getX());
/*  46 */     int zc2 = SectionPos.blockToSectionCoord(end.getZ());
/*     */     
/*  48 */     this.chunks = new ChunkAccess[xc2 - this.centerX + 1][zc2 - this.centerZ + 1];
/*     */     
/*  50 */     ChunkSource chunkSource = level.getChunkSource();
/*  51 */     this.allEmpty = true;
/*  52 */     for (int xc = this.centerX; xc <= xc2; xc++) {
/*  53 */       for (int zc = this.centerZ; zc <= zc2; zc++) {
/*  54 */         this.chunks[xc - this.centerX][zc - this.centerZ] = chunkSource.getChunkNow(xc, zc);
/*     */       }
/*     */     } 
/*     */     
/*  58 */     for (int xc = SectionPos.blockToSectionCoord(start.getX()); xc <= SectionPos.blockToSectionCoord(end.getX()); xc++) {
/*  59 */       for (int zc = SectionPos.blockToSectionCoord(start.getZ()); zc <= SectionPos.blockToSectionCoord(end.getZ()); zc++) {
/*  60 */         ChunkAccess chunk = this.chunks[xc - this.centerX][zc - this.centerZ];
/*  61 */         if (chunk != null && 
/*  62 */           !chunk.isYSpaceEmpty(start.getY(), end.getY())) {
/*  63 */           this.allEmpty = false;
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  72 */   private ChunkAccess getChunk(BlockPos pos) { return getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ())); }
/*     */ 
/*     */   
/*     */   private ChunkAccess getChunk(int chunkX, int chunkZ) {
/*  76 */     int xc = chunkX - this.centerX;
/*  77 */     int zc = chunkZ - this.centerZ;
/*     */     
/*  79 */     if (xc < 0 || xc >= this.chunks.length || zc < 0 || zc >= this.chunks[xc].length) {
/*  80 */       return new EmptyLevelChunk(this.level, new ChunkPos(chunkX, chunkZ), (Holder)this.plains.get());
/*     */     }
/*  82 */     ChunkAccess chunk = this.chunks[xc][zc];
/*  83 */     return (chunk != null) ? chunk : new EmptyLevelChunk(this.level, new ChunkPos(chunkX, chunkZ), (Holder)this.plains.get());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public WorldBorder getWorldBorder() { return this.level.getWorldBorder(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public BlockGetter getChunkForCollisions(int chunkX, int chunkZ) { return getChunk(chunkX, chunkZ); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public List<VoxelShape> getEntityCollisions(Entity source, AABB testArea) { return List.of(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 103 */     ChunkAccess chunk = getChunk(pos);
/* 104 */     return chunk.getBlockEntity(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getBlockState(BlockPos pos) {
/* 109 */     if (isOutsideBuildHeight(pos)) {
/* 110 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 113 */     ChunkAccess chunk = getChunk(pos);
/* 114 */     return chunk.getBlockState(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockPos pos) {
/* 119 */     if (isOutsideBuildHeight(pos)) {
/* 120 */       return Fluids.EMPTY.defaultFluidState();
/*     */     }
/*     */     
/* 123 */     ChunkAccess chunk = getChunk(pos);
/* 124 */     return chunk.getFluidState(pos);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 129 */   public int getMinY() { return this.level.getMinY(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   public int getHeight() { return this.level.getHeight(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\PathNavigationRegion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */