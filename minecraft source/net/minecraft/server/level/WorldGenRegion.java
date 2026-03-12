/*     */ package net.minecraft.server.level;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StaticCache2D;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeReader;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.EntityBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkSource;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStep;
/*     */ import net.minecraft.world.level.chunk.status.ChunkType;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.ticks.LevelTickAccess;
/*     */ import net.minecraft.world.ticks.TickContainerAccess;
/*     */ import net.minecraft.world.ticks.WorldGenTickAccess;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class WorldGenRegion implements WorldGenLevel {
/*  66 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final StaticCache2D<GenerationChunkHolder> cache;
/*     */   private final ChunkAccess center;
/*     */   private final ServerLevel level;
/*     */   private final long seed;
/*     */   private final LevelData levelData;
/*     */   private final RandomSource random;
/*     */   private final DimensionType dimensionType;
/*     */   private final WorldGenTickAccess<Block> blockTicks;
/*     */   private final WorldGenTickAccess<Fluid> fluidTicks;
/*     */   private final BiomeManager biomeManager;
/*     */   private final ChunkStep generatingStep;
/*     */   private Supplier<String> currentlyGenerating;
/*     */   private final AtomicLong subTickCount;
/*  81 */   private static final Identifier WORLDGEN_REGION_RANDOM = Identifier.withDefaultNamespace("worldgen_region_random"); public WorldGenRegion(ServerLevel level, StaticCache2D<GenerationChunkHolder> cache, ChunkStep generatingStep, ChunkAccess center) { this.blockTicks = new WorldGenTickAccess(pos -> getChunk(pos).getBlockTicks());
/*     */     this.fluidTicks = new WorldGenTickAccess(pos -> getChunk(pos).getFluidTicks());
/*     */     this.subTickCount = new AtomicLong();
/*  84 */     this.generatingStep = generatingStep;
/*  85 */     this.cache = cache;
/*  86 */     this.center = center;
/*  87 */     this.level = level;
/*  88 */     this.seed = level.getSeed();
/*  89 */     this.levelData = level.getLevelData();
/*  90 */     this.random = level.getChunkSource().randomState().getOrCreateRandomFactory(WORLDGEN_REGION_RANDOM).at(this.center.getPos().getWorldPosition());
/*     */     
/*  92 */     this.dimensionType = level.dimensionType();
/*  93 */     this.biomeManager = new BiomeManager(this, BiomeManager.obfuscateSeed(this.seed)); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public boolean isOldChunkAround(ChunkPos pos, int range) { return (this.level.getChunkSource()).chunkMap.isOldChunkAround(pos, range); }
/*     */ 
/*     */ 
/*     */   
/* 101 */   public ChunkPos getCenter() { return this.center.getPos(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public void setCurrentlyGenerating(Supplier<String> currentlyGenerating) { this.currentlyGenerating = currentlyGenerating; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   public ChunkAccess getChunk(int chunkX, int chunkZ) { return getChunk(chunkX, chunkZ, ChunkStatus.EMPTY); }
/*     */ 
/*     */   
/*     */   public ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus targetStatus, boolean loadOrGenerate) {
/*     */     GenerationChunkHolder chunkHolder;
/* 116 */     int distance = this.center.getPos().getChessboardDistance(chunkX, chunkZ);
/* 117 */     ChunkStatus maxAllowedStatus = (distance >= this.generatingStep.directDependencies().size()) ? null : this.generatingStep.directDependencies().get(distance);
/*     */     
/* 119 */     if (maxAllowedStatus != null) {
/* 120 */       chunkHolder = (GenerationChunkHolder)this.cache.get(chunkX, chunkZ);
/* 121 */       if (targetStatus.isOrBefore(maxAllowedStatus)) {
/* 122 */         ChunkAccess chunk = chunkHolder.getChunkIfPresentUnchecked(maxAllowedStatus);
/* 123 */         if (chunk != null) {
/* 124 */           return chunk;
/*     */         }
/*     */       } 
/*     */     } else {
/* 128 */       chunkHolder = null;
/*     */     } 
/* 130 */     CrashReport report = CrashReport.forThrowable(new IllegalStateException("Requested chunk unavailable during world generation"), "Exception generating new chunk");
/* 131 */     CrashReportCategory category = report.addCategory("Chunk request details");
/* 132 */     category.setDetail("Requested chunk", String.format(Locale.ROOT, "%d, %d", new Object[] { Integer.valueOf(chunkX), Integer.valueOf(chunkZ) }));
/* 133 */     category.setDetail("Generating status", () -> this.generatingStep.targetStatus().getName());
/* 134 */     Objects.requireNonNull(targetStatus); category.setDetail("Requested status", targetStatus::getName);
/* 135 */     category.setDetail("Actual status", () -> (chunkHolder == null) ? "[out of cache bounds]" : chunkHolder.getPersistedStatus().getName());
/* 136 */     category.setDetail("Maximum allowed status", () -> (maxAllowedStatus == null) ? "null" : maxAllowedStatus.getName());
/* 137 */     Objects.requireNonNull(this.generatingStep.directDependencies()); category.setDetail("Dependencies", this.generatingStep.directDependencies()::toString);
/* 138 */     category.setDetail("Requested distance", Integer.valueOf(distance));
/* 139 */     Objects.requireNonNull(this.center.getPos()); category.setDetail("Generating chunk", this.center.getPos()::toString);
/* 140 */     throw new ReportedException(report);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasChunk(int chunkX, int chunkZ) {
/* 145 */     int distance = this.center.getPos().getChessboardDistance(chunkX, chunkZ);
/* 146 */     return (distance < this.generatingStep.directDependencies().size());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 151 */   public BlockState getBlockState(BlockPos pos) { return getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ())).getBlockState(pos); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   public FluidState getFluidState(BlockPos pos) { return getChunk(pos).getFluidState(pos); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   public Player getNearestPlayer(double x, double y, double z, double maxDist, Predicate<Entity> predicate) { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 166 */   public int getSkyDarken() { return 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   public BiomeManager getBiomeManager() { return this.biomeManager; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   public Holder<Biome> getUncachedNoiseBiome(int quartX, int quartY, int quartZ) { return this.level.getUncachedNoiseBiome(quartX, quartY, quartZ); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 181 */   public float getShade(Direction direction, boolean shade) { return 1.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 186 */   public LevelLightEngine getLightEngine() { return this.level.getLightEngine(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean destroyBlock(BlockPos pos, boolean dropResources, Entity breaker, int updateLimit) {
/* 191 */     BlockState blockState = getBlockState(pos);
/* 192 */     if (blockState.isAir()) {
/* 193 */       return false;
/*     */     }
/*     */     
/* 196 */     if (dropResources) {
/* 197 */       BlockEntity blockEntity = blockState.hasBlockEntity() ? getBlockEntity(pos) : null;
/* 198 */       Block.dropResources(blockState, this.level, pos, blockEntity, breaker, ItemStack.EMPTY);
/*     */     } 
/* 200 */     return setBlock(pos, Blocks.AIR.defaultBlockState(), 3, updateLimit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 206 */     ChunkAccess chunk = getChunk(pos);
/* 207 */     BlockEntity blockEntity = chunk.getBlockEntity(pos);
/*     */     
/* 209 */     if (blockEntity != null) {
/* 210 */       return blockEntity;
/*     */     }
/*     */     
/* 213 */     CompoundTag tag = chunk.getBlockEntityNbt(pos);
/* 214 */     BlockState state = chunk.getBlockState(pos);
/* 215 */     if (tag != null) {
/* 216 */       if ("DUMMY".equals(tag.getStringOr("id", ""))) {
/* 217 */         if (!state.hasBlockEntity()) {
/* 218 */           return null;
/*     */         }
/* 220 */         blockEntity = ((EntityBlock)state.getBlock()).newBlockEntity(pos, state);
/*     */       } else {
/* 222 */         blockEntity = BlockEntity.loadStatic(pos, state, tag, this.level.registryAccess());
/*     */       } 
/*     */       
/* 225 */       if (blockEntity != null) {
/* 226 */         chunk.setBlockEntity(blockEntity);
/* 227 */         return blockEntity;
/*     */       } 
/*     */     } 
/*     */     
/* 231 */     if (state.hasBlockEntity()) {
/* 232 */       LOGGER.warn("Tried to access a block entity before it was created. {}", pos);
/*     */     }
/*     */     
/* 235 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ensureCanWrite(BlockPos pos) {
/* 240 */     int chunkX = SectionPos.blockToSectionCoord(pos.getX());
/* 241 */     int chunkZ = SectionPos.blockToSectionCoord(pos.getZ());
/*     */     
/* 243 */     ChunkPos centerPos = getCenter();
/* 244 */     int distanceX = Math.abs(centerPos.x - chunkX);
/* 245 */     int distanceZ = Math.abs(centerPos.z - chunkZ);
/*     */     
/* 247 */     if (distanceX > this.generatingStep.blockStateWriteRadius() || distanceZ > this.generatingStep.blockStateWriteRadius()) {
/* 248 */       Util.logAndPauseIfInIde("Detected setBlock in a far chunk [" + chunkX + ", " + chunkZ + "], pos: " + String.valueOf(pos) + ", status: " + String.valueOf(this.generatingStep.targetStatus()) + ((this.currentlyGenerating == null) ? "" : (", currently generating: " + (String)this.currentlyGenerating.get())));
/* 249 */       return false;
/*     */     } 
/*     */     
/* 252 */     if (this.center.isUpgrading()) {
/* 253 */       LevelHeightAccessor levelHeightAccessor = this.center.getHeightAccessorForGeneration();
/* 254 */       if (levelHeightAccessor.isOutsideBuildHeight(pos.getY())) {
/* 255 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 259 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setBlock(BlockPos pos, BlockState blockState, @UpdateFlags int updateFlags, int updateLimit) {
/* 264 */     if (!ensureCanWrite(pos)) {
/* 265 */       return false;
/*     */     }
/*     */     
/* 268 */     ChunkAccess chunk = getChunk(pos);
/* 269 */     BlockState oldState = chunk.setBlockState(pos, blockState, updateFlags);
/*     */     
/* 271 */     if (oldState != null) {
/* 272 */       this.level.updatePOIOnBlockStateChange(pos, oldState, blockState);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 277 */     if (blockState.hasBlockEntity()) {
/* 278 */       if (chunk.getPersistedStatus().getChunkType() == ChunkType.LEVELCHUNK) {
/* 279 */         BlockEntity blockEntity = ((EntityBlock)blockState.getBlock()).newBlockEntity(pos, blockState);
/* 280 */         if (blockEntity != null) {
/* 281 */           chunk.setBlockEntity(blockEntity);
/*     */         } else {
/* 283 */           chunk.removeBlockEntity(pos);
/*     */         } 
/*     */       } else {
/* 286 */         CompoundTag tag = new CompoundTag();
/* 287 */         tag.putInt("x", pos.getX());
/* 288 */         tag.putInt("y", pos.getY());
/* 289 */         tag.putInt("z", pos.getZ());
/* 290 */         tag.putString("id", "DUMMY");
/* 291 */         chunk.setBlockEntityNbt(tag);
/*     */       } 
/* 293 */     } else if (oldState != null && oldState.hasBlockEntity()) {
/* 294 */       chunk.removeBlockEntity(pos);
/*     */     } 
/*     */     
/* 297 */     if (blockState.hasPostProcess(this, pos) && (updateFlags & 0x10) == 0) {
/* 298 */       markPosForPostprocessing(pos);
/*     */     }
/*     */     
/* 301 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 305 */   private void markPosForPostprocessing(BlockPos blockPos) { getChunk(blockPos).markPosForPostprocessing(blockPos); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addFreshEntity(Entity entity) {
/* 313 */     int xc = SectionPos.blockToSectionCoord(entity.getBlockX());
/* 314 */     int zc = SectionPos.blockToSectionCoord(entity.getBlockZ());
/*     */     
/* 316 */     getChunk(xc, zc).addEntity(entity);
/* 317 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 322 */   public boolean removeBlock(BlockPos pos, boolean movedByPiston) { return setBlock(pos, Blocks.AIR.defaultBlockState(), 3); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 327 */   public WorldBorder getWorldBorder() { return this.level.getWorldBorder(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 332 */   public boolean isClientSide() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 338 */   public ServerLevel getLevel() { return this.level; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 343 */   public RegistryAccess registryAccess() { return this.level.registryAccess(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 348 */   public FeatureFlagSet enabledFeatures() { return this.level.enabledFeatures(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 353 */   public LevelData getLevelData() { return this.levelData; }
/*     */ 
/*     */ 
/*     */   
/*     */   public DifficultyInstance getCurrentDifficultyAt(BlockPos pos) {
/* 358 */     if (!hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()))) {
/* 359 */       throw new RuntimeException("We are asking a region for a chunk out of bound");
/*     */     }
/*     */     
/* 362 */     return new DifficultyInstance(this.level.getDifficulty(), this.level.getDayTime(), 0L, this.level.getMoonBrightness(pos));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 367 */   public MinecraftServer getServer() { return this.level.getServer(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 372 */   public ChunkSource getChunkSource() { return this.level.getChunkSource(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 377 */   public long getSeed() { return this.seed; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 382 */   public LevelTickAccess<Block> getBlockTicks() { return this.blockTicks; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 387 */   public LevelTickAccess<Fluid> getFluidTicks() { return this.fluidTicks; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 392 */   public int getSeaLevel() { return this.level.getSeaLevel(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 397 */   public RandomSource getRandom() { return this.random; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 402 */   public int getHeight(Heightmap.Types type, int x, int z) { return getChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z)).getHeight(type, x & 0xF, z & 0xF) + 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void playSound(Entity except, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void levelEvent(Entity source, int type, BlockPos pos, int data) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void gameEvent(Holder<GameEvent> gameEvent, Vec3 position, GameEvent.Context context) {}
/*     */ 
/*     */ 
/*     */   
/* 423 */   public DimensionType dimensionType() { return this.dimensionType; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 428 */   public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> predicate) { return predicate.test(getBlockState(pos)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 433 */   public boolean isFluidAtPosition(BlockPos pos, Predicate<FluidState> predicate) { return predicate.test(getFluidState(pos)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 438 */   public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector) { return Collections.emptyList(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 443 */   public List<Entity> getEntities(Entity except, AABB bb, Predicate<? super Entity> selector) { return Collections.emptyList(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 448 */   public List<Player> players() { return Collections.emptyList(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 453 */   public int getMinY() { return this.level.getMinY(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 458 */   public int getHeight() { return this.level.getHeight(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 463 */   public long nextSubTickCount() { return this.subTickCount.getAndIncrement(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 468 */   public EnvironmentAttributeReader environmentAttributes() { return EnvironmentAttributeReader.EMPTY; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\WorldGenRegion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */