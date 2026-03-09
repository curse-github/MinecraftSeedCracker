/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeReader;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface LevelReader
/*     */   extends BlockAndTintGetter, CollisionGetter, SignalGetter, BiomeManager.NoiseBiomeSource
/*     */ {
/*     */   ChunkAccess getChunk(int paramInt1, int paramInt2, ChunkStatus paramChunkStatus, boolean paramBoolean);
/*     */   
/*     */   @Deprecated
/*     */   boolean hasChunk(int paramInt1, int paramInt2);
/*     */   
/*     */   int getHeight(Heightmap.Types paramTypes, int paramInt1, int paramInt2);
/*     */   
/*  39 */   default int getHeight(Heightmap.Types type, BlockPos pos) { return getHeight(type, pos.getX(), pos.getZ()); }
/*     */ 
/*     */   
/*     */   int getSkyDarken();
/*     */ 
/*     */   
/*     */   BiomeManager getBiomeManager();
/*     */   
/*  47 */   default Holder<Biome> getBiome(BlockPos pos) { return getBiomeManager().getBiome(pos); }
/*     */ 
/*     */   
/*     */   default Stream<BlockState> getBlockStatesIfLoaded(AABB box) {
/*  51 */     int x0 = Mth.floor(box.minX);
/*  52 */     int x1 = Mth.floor(box.maxX);
/*  53 */     int y0 = Mth.floor(box.minY);
/*  54 */     int y1 = Mth.floor(box.maxY);
/*  55 */     int z0 = Mth.floor(box.minZ);
/*  56 */     int z1 = Mth.floor(box.maxZ);
/*     */     
/*  58 */     if (hasChunksAt(x0, y0, z0, x1, y1, z1)) {
/*  59 */       return getBlockStates(box);
/*     */     }
/*  61 */     return Stream.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  66 */   default int getBlockTint(BlockPos pos, ColorResolver resolver) { return resolver.getColor((Biome)getBiome(pos).value(), pos.getX(), pos.getZ()); }
/*     */ 
/*     */ 
/*     */   
/*     */   default Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ) {
/*  71 */     ChunkAccess chunk = getChunk(QuartPos.toSection(quartX), QuartPos.toSection(quartZ), ChunkStatus.BIOMES, false);
/*  72 */     if (chunk != null) {
/*  73 */       return chunk.getNoiseBiome(quartX, quartY, quartZ);
/*     */     }
/*  75 */     return getUncachedNoiseBiome(quartX, quartY, quartZ);
/*     */   }
/*     */ 
/*     */   
/*     */   Holder<Biome> getUncachedNoiseBiome(int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   
/*     */   boolean isClientSide();
/*     */   
/*     */   int getSeaLevel();
/*     */   
/*     */   DimensionType dimensionType();
/*     */   
/*  88 */   default int getMinY() { return dimensionType().minY(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   default int getHeight() { return dimensionType().height(); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   default BlockPos getHeightmapPos(Heightmap.Types type, BlockPos pos) { return new BlockPos(pos.getX(), getHeight(type, pos.getX(), pos.getZ()), pos.getZ()); }
/*     */ 
/*     */ 
/*     */   
/* 101 */   default boolean isEmptyBlock(BlockPos pos) { return getBlockState(pos).isAir(); }
/*     */ 
/*     */   
/*     */   default boolean canSeeSkyFromBelowWater(BlockPos pos) {
/* 105 */     if (pos.getY() >= getSeaLevel()) {
/* 106 */       return canSeeSky(pos);
/*     */     }
/* 108 */     BlockPos scanPoint = new BlockPos(pos.getX(), getSeaLevel(), pos.getZ());
/* 109 */     if (!canSeeSky(scanPoint)) {
/* 110 */       return false;
/*     */     }
/* 112 */     scanPoint = scanPoint.below();
/* 113 */     while (scanPoint.getY() > pos.getY()) {
/* 114 */       BlockState state = getBlockState(scanPoint);
/* 115 */       if (state.getLightBlock() > 0 && !state.liquid()) {
/* 116 */         return false;
/*     */       }
/* 118 */       scanPoint = scanPoint.below();
/*     */     } 
/* 120 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 125 */   default float getPathfindingCostFromLightLevels(BlockPos pos) { return getLightLevelDependentMagicValue(pos) - 0.5F; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default float getLightLevelDependentMagicValue(BlockPos pos) {
/* 141 */     float v = getMaxLocalRawBrightness(pos) / 15.0F;
/*     */     
/* 143 */     float curvedV = v / (4.0F - 3.0F * v);
/* 144 */     return Mth.lerp(dimensionType().ambientLight(), curvedV, 1.0F);
/*     */   }
/*     */ 
/*     */   
/* 148 */   default ChunkAccess getChunk(BlockPos pos) { return getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ())); }
/*     */ 
/*     */ 
/*     */   
/* 152 */   default ChunkAccess getChunk(int chunkX, int chunkZ) { return getChunk(chunkX, chunkZ, ChunkStatus.FULL, true); }
/*     */ 
/*     */ 
/*     */   
/* 156 */   default ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus status) { return getChunk(chunkX, chunkZ, status, true); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   default BlockGetter getChunkForCollisions(int chunkX, int chunkZ) { return getChunk(chunkX, chunkZ, ChunkStatus.EMPTY, false); }
/*     */ 
/*     */ 
/*     */   
/* 165 */   default boolean isWaterAt(BlockPos pos) { return getFluidState(pos).is(FluidTags.WATER); }
/*     */ 
/*     */   
/*     */   default boolean containsAnyLiquid(AABB box) {
/* 169 */     int x0 = Mth.floor(box.minX);
/* 170 */     int x1 = Mth.ceil(box.maxX);
/* 171 */     int y0 = Mth.floor(box.minY);
/* 172 */     int y1 = Mth.ceil(box.maxY);
/* 173 */     int z0 = Mth.floor(box.minZ);
/* 174 */     int z1 = Mth.ceil(box.maxZ);
/*     */     
/* 176 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 177 */     for (int x = x0; x < x1; x++) {
/* 178 */       for (int y = y0; y < y1; y++) {
/* 179 */         for (int z = z0; z < z1; z++) {
/* 180 */           BlockState blockState = getBlockState(pos.set(x, y, z));
/* 181 */           if (!blockState.getFluidState().isEmpty()) {
/* 182 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 187 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 191 */   default int getMaxLocalRawBrightness(BlockPos pos) { return getMaxLocalRawBrightness(pos, getSkyDarken()); }
/*     */ 
/*     */   
/*     */   default int getMaxLocalRawBrightness(BlockPos pos, int skyDarkening) {
/* 195 */     if (pos.getX() < -30000000 || pos.getZ() < -30000000 || pos.getX() >= 30000000 || pos.getZ() >= 30000000) {
/* 196 */       return 15;
/*     */     }
/*     */     
/* 199 */     return getRawBrightness(pos, skyDarkening);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 207 */   default boolean hasChunkAt(int blockX, int blockZ) { return hasChunk(SectionPos.blockToSectionCoord(blockX), SectionPos.blockToSectionCoord(blockZ)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 215 */   default boolean hasChunkAt(BlockPos pos) { return hasChunkAt(pos.getX(), pos.getZ()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 223 */   default boolean hasChunksAt(BlockPos pos0, BlockPos pos1) { return hasChunksAt(pos0.getX(), pos0.getY(), pos0.getZ(), pos1.getX(), pos1.getY(), pos1.getZ()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunksAt(int x0, int y0, int z0, int x1, int y1, int z1) {
/* 231 */     if (y1 < getMinY() || y0 > getMaxY()) {
/* 232 */       return false;
/*     */     }
/*     */     
/* 235 */     return hasChunksAt(x0, z0, x1, z1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunksAt(int x0, int z0, int x1, int z1) {
/* 243 */     int chunkX0 = SectionPos.blockToSectionCoord(x0);
/* 244 */     int chunkX1 = SectionPos.blockToSectionCoord(x1);
/* 245 */     int chunkZ0 = SectionPos.blockToSectionCoord(z0);
/* 246 */     int chunkZ1 = SectionPos.blockToSectionCoord(z1);
/*     */     
/* 248 */     for (int chunkX = chunkX0; chunkX <= chunkX1; chunkX++) {
/* 249 */       for (int chunkZ = chunkZ0; chunkZ <= chunkZ1; chunkZ++) {
/* 250 */         if (!hasChunk(chunkX, chunkZ)) {
/* 251 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 256 */     return true;
/*     */   }
/*     */   
/*     */   RegistryAccess registryAccess();
/*     */   
/*     */   FeatureFlagSet enabledFeatures();
/*     */   
/*     */   default <T> HolderLookup<T> holderLookup(ResourceKey<? extends Registry<? extends T>> key) {
/* 264 */     Registry<T> registry = registryAccess().lookupOrThrow(key);
/* 265 */     return registry.filterFeatures(enabledFeatures());
/*     */   }
/*     */   
/*     */   EnvironmentAttributeReader environmentAttributes();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\LevelReader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */