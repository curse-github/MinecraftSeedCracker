/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortList;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.blending.BlendingData;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.lighting.LightEngine;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.ticks.LevelChunkTicks;
/*     */ import net.minecraft.world.ticks.ProtoChunkTicks;
/*     */ import net.minecraft.world.ticks.TickContainerAccess;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ProtoChunk
/*     */   extends ChunkAccess {
/*  45 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   private final List<CompoundTag> entities = Lists.newArrayList();
/*     */   
/*     */   private CarvingMask carvingMask;
/*     */   
/*     */   private BelowZeroRetrogen belowZeroRetrogen;
/*     */   
/*     */   private final ProtoChunkTicks<Block> blockTicks;
/*     */   private final ProtoChunkTicks<Fluid> fluidTicks;
/*     */   
/*  59 */   public ProtoChunk(ChunkPos chunkPos, UpgradeData upgradeData, LevelHeightAccessor levelHeightAccessor, PalettedContainerFactory containerFactory, BlendingData blendingData) { this(chunkPos, upgradeData, null, new ProtoChunkTicks(), new ProtoChunkTicks(), levelHeightAccessor, containerFactory, blendingData); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtoChunk(ChunkPos chunkPos, UpgradeData upgradeData, LevelChunkSection[] sections, ProtoChunkTicks<Block> blockTicks, ProtoChunkTicks<Fluid> fluidTicks, LevelHeightAccessor levelHeightAccessor, PalettedContainerFactory containerFactory, BlendingData blendingData) {
/*  72 */     super(chunkPos, upgradeData, levelHeightAccessor, containerFactory, 0L, sections, blendingData);
/*  73 */     this.blockTicks = blockTicks;
/*  74 */     this.fluidTicks = fluidTicks;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  79 */   public TickContainerAccess<Block> getBlockTicks() { return this.blockTicks; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   public TickContainerAccess<Fluid> getFluidTicks() { return this.fluidTicks; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   public ChunkAccess.PackedTicks getTicksForSerialization(long currentTick) { return new ChunkAccess.PackedTicks(this.blockTicks.pack(currentTick), this.fluidTicks.pack(currentTick)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getBlockState(BlockPos pos) {
/*  94 */     int y = pos.getY();
/*  95 */     if (isOutsideBuildHeight(y)) {
/*  96 */       return Blocks.VOID_AIR.defaultBlockState();
/*     */     }
/*     */     
/*  99 */     LevelChunkSection section = getSection(getSectionIndex(y));
/* 100 */     if (section.hasOnlyAir()) {
/* 101 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 104 */     return section.getBlockState(pos.getX() & 0xF, y & 0xF, pos.getZ() & 0xF);
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockPos pos) {
/* 109 */     int y = pos.getY();
/* 110 */     if (isOutsideBuildHeight(y)) {
/* 111 */       return Fluids.EMPTY.defaultFluidState();
/*     */     }
/*     */     
/* 114 */     LevelChunkSection section = getSection(getSectionIndex(y));
/* 115 */     if (section.hasOnlyAir()) {
/* 116 */       return Fluids.EMPTY.defaultFluidState();
/*     */     }
/*     */     
/* 119 */     return section.getFluidState(pos.getX() & 0xF, y & 0xF, pos.getZ() & 0xF);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState setBlockState(BlockPos pos, BlockState state, @UpdateFlags int flags) {
/* 124 */     int x = pos.getX();
/* 125 */     int y = pos.getY();
/* 126 */     int z = pos.getZ();
/*     */     
/* 128 */     if (isOutsideBuildHeight(y)) {
/* 129 */       return Blocks.VOID_AIR.defaultBlockState();
/*     */     }
/*     */     
/* 132 */     int sectionIndex = getSectionIndex(y);
/* 133 */     LevelChunkSection section = getSection(sectionIndex);
/* 134 */     boolean wasEmpty = section.hasOnlyAir();
/* 135 */     if (wasEmpty && state.is(Blocks.AIR)) {
/* 136 */       return state;
/*     */     }
/*     */     
/* 139 */     int localX = SectionPos.sectionRelative(x);
/* 140 */     int localY = SectionPos.sectionRelative(y);
/* 141 */     int localZ = SectionPos.sectionRelative(z);
/* 142 */     BlockState oldState = section.setBlockState(localX, localY, localZ, state);
/*     */     
/* 144 */     if (this.status.isOrAfter(ChunkStatus.INITIALIZE_LIGHT)) {
/* 145 */       boolean isEmpty = section.hasOnlyAir();
/* 146 */       if (isEmpty != wasEmpty) {
/* 147 */         this.lightEngine.updateSectionStatus(pos, isEmpty);
/*     */       }
/*     */       
/* 150 */       if (LightEngine.hasDifferentLightProperties(oldState, state)) {
/* 151 */         this.skyLightSources.update(this, localX, y, localZ);
/* 152 */         this.lightEngine.checkBlock(pos);
/*     */       } 
/*     */     } 
/*     */     
/* 156 */     EnumSet<Heightmap.Types> heightmapsAfter = getPersistedStatus().heightmapsAfter();
/* 157 */     EnumSet<Heightmap.Types> toPrime = null;
/*     */     
/* 159 */     for (Heightmap.Types type : heightmapsAfter) {
/* 160 */       Heightmap heightmap = (Heightmap)this.heightmaps.get(type);
/* 161 */       if (heightmap == null) {
/* 162 */         if (toPrime == null) {
/* 163 */           toPrime = EnumSet.noneOf(Heightmap.Types.class);
/*     */         }
/* 165 */         toPrime.add(type);
/*     */       } 
/*     */     } 
/*     */     
/* 169 */     if (toPrime != null) {
/* 170 */       Heightmap.primeHeightmaps(this, toPrime);
/*     */     }
/*     */     
/* 173 */     for (Heightmap.Types type : heightmapsAfter) {
/* 174 */       ((Heightmap)this.heightmaps.get(type)).update(localX, y, localZ, state);
/*     */     }
/*     */     
/* 177 */     return oldState;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockEntity(BlockEntity blockEntity) {
/* 182 */     this.pendingBlockEntities.remove(blockEntity.getBlockPos());
/* 183 */     this.blockEntities.put(blockEntity.getBlockPos(), blockEntity);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 188 */   public BlockEntity getBlockEntity(BlockPos pos) { return (BlockEntity)this.blockEntities.get(pos); }
/*     */ 
/*     */ 
/*     */   
/* 192 */   public Map<BlockPos, BlockEntity> getBlockEntities() { return this.blockEntities; }
/*     */ 
/*     */ 
/*     */   
/* 196 */   public void addEntity(CompoundTag tag) { this.entities.add(tag); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addEntity(Entity entity) {
/* 201 */     if (entity.isPassenger()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 207 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), LOGGER); 
/* 208 */     try { TagValueOutput output = TagValueOutput.createWithContext(reporter, entity.registryAccess());
/* 209 */       entity.save(output);
/* 210 */       addEntity(output.buildResult());
/* 211 */       reporter.close(); }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1)
/*     */       { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 216 */      } public void setStartForStructure(Structure structure, StructureStart structureStart) { BelowZeroRetrogen belowZeroRetrogen = getBelowZeroRetrogen();
/* 217 */     if (belowZeroRetrogen != null && structureStart.isValid()) {
/* 218 */       BoundingBox boundingBox = structureStart.getBoundingBox();
/* 219 */       LevelHeightAccessor heightAccessor = getHeightAccessorForGeneration();
/* 220 */       if (boundingBox.minY() < heightAccessor.getMinY() || boundingBox.maxY() > heightAccessor.getMaxY()) {
/*     */         return;
/*     */       }
/*     */     } 
/* 224 */     super.setStartForStructure(structure, structureStart); }
/*     */ 
/*     */ 
/*     */   
/* 228 */   public List<CompoundTag> getEntities() { return this.entities; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 233 */   public ChunkStatus getPersistedStatus() { return this.status; }
/*     */ 
/*     */   
/*     */   public void setPersistedStatus(ChunkStatus status) {
/* 237 */     this.status = status;
/* 238 */     if (this.belowZeroRetrogen != null && status.isOrAfter(this.belowZeroRetrogen.targetStatus())) {
/* 239 */       setBelowZeroRetrogen(null);
/*     */     }
/* 241 */     markUnsaved();
/*     */   }
/*     */ 
/*     */   
/*     */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ) {
/* 246 */     if (getHighestGeneratedStatus().isOrAfter(ChunkStatus.BIOMES)) {
/* 247 */       return super.getNoiseBiome(quartX, quartY, quartZ);
/*     */     }
/* 249 */     throw new IllegalStateException("Asking for biomes before we have biomes");
/*     */   }
/*     */   
/*     */   public static short packOffsetCoordinates(BlockPos blockPos) {
/* 253 */     int x = blockPos.getX();
/* 254 */     int y = blockPos.getY();
/* 255 */     int z = blockPos.getZ();
/* 256 */     int dx = x & 0xF;
/* 257 */     int dy = y & 0xF;
/* 258 */     int dz = z & 0xF;
/* 259 */     return (short)(dx | dy << 4 | dz << 8);
/*     */   }
/*     */   
/*     */   public static BlockPos unpackOffsetCoordinates(short packedCoord, int sectionY, ChunkPos chunkPos) {
/* 263 */     int posX = SectionPos.sectionToBlockCoord(chunkPos.x, packedCoord & 0xF);
/* 264 */     int posY = SectionPos.sectionToBlockCoord(sectionY, packedCoord >>> 4 & 0xF);
/* 265 */     int posZ = SectionPos.sectionToBlockCoord(chunkPos.z, packedCoord >>> 8 & 0xF);
/* 266 */     return new BlockPos(posX, posY, posZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public void markPosForPostprocessing(BlockPos blockPos) {
/* 271 */     if (!isOutsideBuildHeight(blockPos)) {
/* 272 */       ChunkAccess.getOrCreateOffsetList(this.postProcessing, getSectionIndex(blockPos.getY())).add(packOffsetCoordinates(blockPos));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 278 */   public void addPackedPostProcess(ShortList packedOffsets, int sectionIndex) { ChunkAccess.getOrCreateOffsetList(this.postProcessing, sectionIndex).addAll(packedOffsets); }
/*     */ 
/*     */ 
/*     */   
/* 282 */   public Map<BlockPos, CompoundTag> getBlockEntityNbts() { return Collections.unmodifiableMap(this.pendingBlockEntities); }
/*     */ 
/*     */ 
/*     */   
/*     */   public CompoundTag getBlockEntityNbtForSaving(BlockPos blockPos, HolderLookup.Provider registryAccess) {
/* 287 */     BlockEntity blockEntity = getBlockEntity(blockPos);
/* 288 */     if (blockEntity != null) {
/* 289 */       return blockEntity.saveWithFullMetadata(registryAccess);
/*     */     }
/* 291 */     return (CompoundTag)this.pendingBlockEntities.get(blockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeBlockEntity(BlockPos pos) {
/* 296 */     this.blockEntities.remove(pos);
/* 297 */     this.pendingBlockEntities.remove(pos);
/*     */   }
/*     */ 
/*     */   
/* 301 */   public CarvingMask getCarvingMask() { return this.carvingMask; }
/*     */ 
/*     */   
/*     */   public CarvingMask getOrCreateCarvingMask() {
/* 305 */     if (this.carvingMask == null) {
/* 306 */       this.carvingMask = new CarvingMask(getHeight(), getMinY());
/*     */     }
/* 308 */     return this.carvingMask;
/*     */   }
/*     */ 
/*     */   
/* 312 */   public void setCarvingMask(CarvingMask data) { this.carvingMask = data; }
/*     */ 
/*     */ 
/*     */   
/* 316 */   public void setLightEngine(LevelLightEngine lightEngine) { this.lightEngine = lightEngine; }
/*     */ 
/*     */ 
/*     */   
/* 320 */   public void setBelowZeroRetrogen(BelowZeroRetrogen belowZeroRetrogen) { this.belowZeroRetrogen = belowZeroRetrogen; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 325 */   public BelowZeroRetrogen getBelowZeroRetrogen() { return this.belowZeroRetrogen; }
/*     */ 
/*     */ 
/*     */   
/* 329 */   private static <T> LevelChunkTicks<T> unpackTicks(ProtoChunkTicks<T> ticks) { return new LevelChunkTicks(ticks.scheduledTicks()); }
/*     */ 
/*     */ 
/*     */   
/* 333 */   public LevelChunkTicks<Block> unpackBlockTicks() { return unpackTicks(this.blockTicks); }
/*     */ 
/*     */ 
/*     */   
/* 337 */   public LevelChunkTicks<Fluid> unpackFluidTicks() { return unpackTicks(this.fluidTicks); }
/*     */ 
/*     */ 
/*     */   
/*     */   public LevelHeightAccessor getHeightAccessorForGeneration() {
/* 342 */     if (isUpgrading()) {
/* 343 */       return BelowZeroRetrogen.UPGRADE_HEIGHT_ACCESSOR;
/*     */     }
/* 345 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\ProtoChunk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */