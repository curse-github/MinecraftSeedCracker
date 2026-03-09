/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortArrayList;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.BiomeResolver;
/*     */ import net.minecraft.world.level.biome.Climate;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.gameevent.GameEventListenerRegistry;
/*     */ import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.NoiseChunk;
/*     */ import net.minecraft.world.level.levelgen.blending.BlendingData;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.lighting.ChunkSkyLightSources;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.ticks.SavedTick;
/*     */ import net.minecraft.world.ticks.TickContainerAccess;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class ChunkAccess
/*     */   implements LightChunk, StructureAccess, BiomeManager.NoiseBiomeSource
/*     */ {
/*     */   public static final int NO_FILLED_SECTION = -1;
/*  63 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  64 */   private static final LongSet EMPTY_REFERENCE_SET = new LongOpenHashSet(); protected final ShortList[] postProcessing; protected final ChunkPos chunkPos; private long inhabitedTime;
/*     */   @Deprecated
/*     */   private BiomeGenerationSettings carverBiomeSettings;
/*     */   protected NoiseChunk noiseChunk;
/*     */   protected final UpgradeData upgradeData;
/*     */   protected BlendingData blendingData;
/*     */   protected final Map<Heightmap.Types, Heightmap> heightmaps;
/*     */   protected ChunkSkyLightSources skyLightSources;
/*     */   private final Map<Structure, StructureStart> structureStarts;
/*     */   private final Map<Structure, LongSet> structuresRefences;
/*     */   protected final Map<BlockPos, CompoundTag> pendingBlockEntities;
/*     */   protected final Map<BlockPos, BlockEntity> blockEntities;
/*     */   protected final LevelHeightAccessor levelHeightAccessor;
/*     */   protected final LevelChunkSection[] sections;
/*     */   
/*     */   public ChunkAccess(ChunkPos chunkPos, UpgradeData upgradeData, LevelHeightAccessor levelHeightAccessor, PalettedContainerFactory containerFactory, long inhabitedTime, LevelChunkSection[] sections, BlendingData blendingData) {
/*  80 */     this.heightmaps = Maps.newEnumMap(Heightmap.Types.class);
/*     */ 
/*     */     
/*  83 */     this.structureStarts = Maps.newHashMap();
/*  84 */     this.structuresRefences = Maps.newHashMap();
/*     */     
/*  86 */     this.pendingBlockEntities = Maps.newHashMap();
/*  87 */     this.blockEntities = new Object2ObjectOpenHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     this.chunkPos = chunkPos;
/*  94 */     this.upgradeData = upgradeData;
/*  95 */     this.levelHeightAccessor = levelHeightAccessor;
/*  96 */     this.sections = new LevelChunkSection[levelHeightAccessor.getSectionsCount()];
/*  97 */     this.inhabitedTime = inhabitedTime;
/*  98 */     this.postProcessing = new ShortList[levelHeightAccessor.getSectionsCount()];
/*  99 */     this.blendingData = blendingData;
/* 100 */     this.skyLightSources = new ChunkSkyLightSources(levelHeightAccessor);
/*     */     
/* 102 */     if (sections != null) {
/* 103 */       if (this.sections.length == sections.length) {
/* 104 */         System.arraycopy(sections, 0, this.sections, 0, this.sections.length);
/*     */       } else {
/* 106 */         LOGGER.warn("Could not set level chunk sections, array length is {} instead of {}", Integer.valueOf(sections.length), Integer.valueOf(this.sections.length));
/*     */       } 
/*     */     }
/*     */     
/* 110 */     replaceMissingSections(containerFactory, this.sections);
/*     */   }
/*     */   
/*     */   private static void replaceMissingSections(PalettedContainerFactory containerFactory, LevelChunkSection[] sections) {
/* 114 */     for (int i = 0; i < sections.length; i++) {
/* 115 */       if (sections[i] == null) {
/* 116 */         sections[i] = new LevelChunkSection(containerFactory);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 122 */   public GameEventListenerRegistry getListenerRegistry(int section) { return GameEventListenerRegistry.NOOP; }
/*     */ 
/*     */ 
/*     */   
/* 126 */   public BlockState setBlockState(BlockPos pos, BlockState state) { return setBlockState(pos, state, 3); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHighestFilledSectionIndex() {
/* 136 */     LevelChunkSection[] sections = getSections();
/* 137 */     for (int sectionIndex = sections.length - 1; sectionIndex >= 0; sectionIndex--) {
/* 138 */       LevelChunkSection section = sections[sectionIndex];
/* 139 */       if (!section.hasOnlyAir()) {
/* 140 */         return sectionIndex;
/*     */       }
/*     */     } 
/* 143 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   public int getHighestSectionPosition() {
/* 149 */     int sectionIndex = getHighestFilledSectionIndex();
/* 150 */     return (sectionIndex == -1) ? getMinY() : SectionPos.sectionToBlockCoord(getSectionYFromSectionIndex(sectionIndex));
/*     */   }
/*     */   
/*     */   public Set<BlockPos> getBlockEntitiesPos() {
/* 154 */     Set<BlockPos> result = Sets.newHashSet(this.pendingBlockEntities.keySet());
/* 155 */     result.addAll(this.blockEntities.keySet());
/* 156 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 160 */   public LevelChunkSection[] getSections() { return this.sections; }
/*     */ 
/*     */ 
/*     */   
/* 164 */   public LevelChunkSection getSection(int sectionIndex) { return getSections()[sectionIndex]; }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public Collection<Map.Entry<Heightmap.Types, Heightmap>> getHeightmaps() { return Collections.unmodifiableSet(this.heightmaps.entrySet()); }
/*     */ 
/*     */ 
/*     */   
/* 172 */   public void setHeightmap(Heightmap.Types key, long[] data) { getOrCreateHeightmapUnprimed(key).setRawData(this, key, data); }
/*     */ 
/*     */ 
/*     */   
/* 176 */   public Heightmap getOrCreateHeightmapUnprimed(Heightmap.Types type) { return (Heightmap)this.heightmaps.computeIfAbsent(type, k -> new Heightmap(this, k)); }
/*     */ 
/*     */ 
/*     */   
/* 180 */   public boolean hasPrimedHeightmap(Heightmap.Types type) { return (this.heightmaps.get(type) != null); }
/*     */ 
/*     */   
/*     */   public int getHeight(Heightmap.Types type, int x, int z) {
/* 184 */     Heightmap heightmap = (Heightmap)this.heightmaps.get(type);
/* 185 */     if (heightmap == null) {
/* 186 */       if (SharedConstants.IS_RUNNING_IN_IDE && this instanceof LevelChunk) {
/* 187 */         LOGGER.error("Unprimed heightmap: {} {} {}", new Object[] { type, Integer.valueOf(x), Integer.valueOf(z) });
/*     */       }
/* 189 */       Heightmap.primeHeightmaps(this, EnumSet.of(type));
/* 190 */       heightmap = (Heightmap)this.heightmaps.get(type);
/*     */     } 
/* 192 */     return heightmap.getFirstAvailable(x & 0xF, z & 0xF) - 1;
/*     */   }
/*     */ 
/*     */   
/* 196 */   public ChunkPos getPos() { return this.chunkPos; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 201 */   public StructureStart getStartForStructure(Structure structure) { return (StructureStart)this.structureStarts.get(structure); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartForStructure(Structure structure, StructureStart structureStart) {
/* 206 */     this.structureStarts.put(structure, structureStart);
/* 207 */     markUnsaved();
/*     */   }
/*     */ 
/*     */   
/* 211 */   public Map<Structure, StructureStart> getAllStarts() { return Collections.unmodifiableMap(this.structureStarts); }
/*     */ 
/*     */   
/*     */   public void setAllStarts(Map<Structure, StructureStart> starts) {
/* 215 */     this.structureStarts.clear();
/* 216 */     this.structureStarts.putAll(starts);
/* 217 */     markUnsaved();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 222 */   public LongSet getReferencesForStructure(Structure structure) { return (LongSet)this.structuresRefences.getOrDefault(structure, EMPTY_REFERENCE_SET); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addReferenceForStructure(Structure structure, long reference) {
/* 227 */     ((LongSet)this.structuresRefences.computeIfAbsent(structure, k -> new LongOpenHashSet())).add(reference);
/* 228 */     markUnsaved();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 233 */   public Map<Structure, LongSet> getAllReferences() { return Collections.unmodifiableMap(this.structuresRefences); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllReferences(Map<Structure, LongSet> data) {
/* 238 */     this.structuresRefences.clear();
/* 239 */     this.structuresRefences.putAll(data);
/* 240 */     markUnsaved();
/*     */   }
/*     */   
/*     */   public boolean isYSpaceEmpty(int yStartInclusive, int yEndInclusive) {
/* 244 */     if (yStartInclusive < getMinY()) {
/* 245 */       yStartInclusive = getMinY();
/*     */     }
/* 247 */     if (yEndInclusive > getMaxY()) {
/* 248 */       yEndInclusive = getMaxY();
/*     */     }
/* 250 */     for (int y = yStartInclusive; y <= yEndInclusive; y += 16) {
/* 251 */       if (!getSection(getSectionIndex(y)).hasOnlyAir()) {
/* 252 */         return false;
/*     */       }
/*     */     } 
/* 255 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 259 */   public void markUnsaved() { this.unsaved = true; }
/*     */ 
/*     */   
/*     */   public boolean tryMarkSaved() {
/* 263 */     if (this.unsaved) {
/* 264 */       this.unsaved = false;
/* 265 */       return true;
/*     */     } 
/* 267 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 271 */   public boolean isUnsaved() { return this.unsaved; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkStatus getHighestGeneratedStatus() {
/* 278 */     ChunkStatus status = getPersistedStatus();
/* 279 */     BelowZeroRetrogen belowZeroRetrogen = getBelowZeroRetrogen();
/* 280 */     if (belowZeroRetrogen != null) {
/* 281 */       ChunkStatus targetStatus = belowZeroRetrogen.targetStatus();
/* 282 */       return ChunkStatus.max(targetStatus, status);
/*     */     } 
/* 284 */     return status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 290 */   public void markPosForPostprocessing(BlockPos blockPos) { LOGGER.warn("Trying to mark a block for PostProcessing @ {}, but this operation is not supported.", blockPos); }
/*     */ 
/*     */ 
/*     */   
/* 294 */   public ShortList[] getPostProcessing() { return this.postProcessing; }
/*     */ 
/*     */ 
/*     */   
/* 298 */   public void addPackedPostProcess(ShortList packedOffsets, int sectionIndex) { getOrCreateOffsetList(getPostProcessing(), sectionIndex).addAll(packedOffsets); }
/*     */ 
/*     */   
/*     */   public void setBlockEntityNbt(CompoundTag entityTag) {
/* 302 */     BlockPos posFromTag = BlockEntity.getPosFromTag(this.chunkPos, entityTag);
/* 303 */     if (!this.blockEntities.containsKey(posFromTag)) {
/* 304 */       this.pendingBlockEntities.put(posFromTag, entityTag);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 309 */   public CompoundTag getBlockEntityNbt(BlockPos blockPos) { return (CompoundTag)this.pendingBlockEntities.get(blockPos); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 316 */   public final void findBlockLightSources(BiConsumer<BlockPos, BlockState> consumer) { findBlocks(state -> (state.getLightEmission() != 0), consumer); }
/*     */ 
/*     */   
/*     */   public void findBlocks(Predicate<BlockState> predicate, BiConsumer<BlockPos, BlockState> consumer) {
/* 320 */     BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
/* 321 */     for (int sectionY = getMinSectionY(); sectionY <= getMaxSectionY(); sectionY++) {
/* 322 */       LevelChunkSection section = getSection(getSectionIndexFromSectionY(sectionY));
/* 323 */       if (section.maybeHas(predicate)) {
/*     */ 
/*     */         
/* 326 */         BlockPos origin = SectionPos.of(this.chunkPos, sectionY).origin();
/* 327 */         for (int y = 0; y < 16; y++) {
/* 328 */           for (int z = 0; z < 16; z++) {
/* 329 */             for (int x = 0; x < 16; x++) {
/* 330 */               BlockState state = section.getBlockState(x, y, z);
/* 331 */               if (predicate.test(state)) {
/* 332 */                 consumer.accept(mutablePos.setWithOffset(origin, x, y, z), state);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 345 */   public boolean canBeSerialized() { return true; }
/*     */   public static final class PackedTicks extends Record { private final List<SavedTick<Block>> blocks; private final List<SavedTick<Fluid>> fluids;
/*     */     
/* 348 */     public PackedTicks(List<SavedTick<Block>> blocks, List<SavedTick<Fluid>> fluids) { this.blocks = blocks; this.fluids = fluids; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #348	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 348 */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks; } public List<SavedTick<Block>> blocks() { return this.blocks; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #348	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #348	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks;
/* 348 */       //   0	8	1	o	Ljava/lang/Object; } public List<SavedTick<Fluid>> fluids() { return this.fluids; } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 353 */   public UpgradeData getUpgradeData() { return this.upgradeData; }
/*     */ 
/*     */ 
/*     */   
/* 357 */   public boolean isOldNoiseGeneration() { return (this.blendingData != null); }
/*     */ 
/*     */ 
/*     */   
/* 361 */   public BlendingData getBlendingData() { return this.blendingData; }
/*     */ 
/*     */ 
/*     */   
/* 365 */   public long getInhabitedTime() { return this.inhabitedTime; }
/*     */ 
/*     */ 
/*     */   
/* 369 */   public void incrementInhabitedTime(long inhabitedTimeDelta) { this.inhabitedTime += inhabitedTimeDelta; }
/*     */ 
/*     */ 
/*     */   
/* 373 */   public void setInhabitedTime(long inhabitedTime) { this.inhabitedTime = inhabitedTime; }
/*     */ 
/*     */   
/*     */   public static ShortList getOrCreateOffsetList(ShortList[] list, int sectionIndex) {
/* 377 */     ShortArrayList shortArrayList = list[sectionIndex];
/* 378 */     if (shortArrayList == null) {
/* 379 */       shortArrayList = new ShortArrayList();
/* 380 */       list[sectionIndex] = shortArrayList;
/*     */     } 
/* 382 */     return shortArrayList;
/*     */   }
/*     */ 
/*     */   
/* 386 */   public boolean isLightCorrect() { return this.isLightCorrect; }
/*     */ 
/*     */   
/*     */   public void setLightCorrect(boolean isLightCorrect) {
/* 390 */     this.isLightCorrect = isLightCorrect;
/* 391 */     markUnsaved();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 396 */   public int getMinY() { return this.levelHeightAccessor.getMinY(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 401 */   public int getHeight() { return this.levelHeightAccessor.getHeight(); }
/*     */ 
/*     */   
/*     */   public NoiseChunk getOrCreateNoiseChunk(Function<ChunkAccess, NoiseChunk> factory) {
/* 405 */     if (this.noiseChunk == null) {
/* 406 */       this.noiseChunk = (NoiseChunk)factory.apply(this);
/*     */     }
/* 408 */     return this.noiseChunk;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public BiomeGenerationSettings carverBiome(Supplier<BiomeGenerationSettings> source) {
/* 413 */     if (this.carverBiomeSettings == null) {
/* 414 */       this.carverBiomeSettings = (BiomeGenerationSettings)source.get();
/*     */     }
/* 416 */     return this.carverBiomeSettings;
/*     */   }
/*     */ 
/*     */   
/*     */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ) {
/*     */     try {
/* 422 */       int quartMinY = QuartPos.fromBlock(getMinY());
/* 423 */       int quartMaxY = quartMinY + QuartPos.fromBlock(getHeight()) - 1;
/* 424 */       int clampedQuartY = Mth.clamp(quartY, quartMinY, quartMaxY);
/* 425 */       int sectionIndex = getSectionIndex(QuartPos.toBlock(clampedQuartY));
/* 426 */       return this.sections[sectionIndex].getNoiseBiome(quartX & 0x3, clampedQuartY & 0x3, quartZ & 0x3);
/* 427 */     } catch (Throwable t) {
/* 428 */       CrashReport report = CrashReport.forThrowable(t, "Getting biome");
/* 429 */       CrashReportCategory category = report.addCategory("Biome being got");
/* 430 */       category.setDetail("Location", () -> CrashReportCategory.formatLocation(this, quartX, quartY, quartZ));
/* 431 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void fillBiomesFromNoise(BiomeResolver biomeResolver, Climate.Sampler sampler) {
/* 436 */     ChunkPos pos = getPos();
/* 437 */     int quartMinX = QuartPos.fromBlock(pos.getMinBlockX());
/* 438 */     int quartMinZ = QuartPos.fromBlock(pos.getMinBlockZ());
/* 439 */     LevelHeightAccessor heightAccessor = getHeightAccessorForGeneration();
/* 440 */     for (int sectionY = heightAccessor.getMinSectionY(); sectionY <= heightAccessor.getMaxSectionY(); sectionY++) {
/* 441 */       LevelChunkSection section = getSection(getSectionIndexFromSectionY(sectionY));
/* 442 */       int quartMinY = QuartPos.fromSection(sectionY);
/* 443 */       section.fillBiomesFromNoise(biomeResolver, sampler, quartMinX, quartMinY, quartMinZ);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 448 */   public boolean hasAnyStructureReferences() { return !getAllReferences().isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/* 452 */   public BelowZeroRetrogen getBelowZeroRetrogen() { return null; }
/*     */ 
/*     */ 
/*     */   
/* 456 */   public boolean isUpgrading() { return (getBelowZeroRetrogen() != null); }
/*     */ 
/*     */ 
/*     */   
/* 460 */   public LevelHeightAccessor getHeightAccessorForGeneration() { return this; }
/*     */ 
/*     */ 
/*     */   
/* 464 */   public void initializeLightSources() { this.skyLightSources.fillFrom(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 469 */   public ChunkSkyLightSources getSkyLightSources() { return this.skyLightSources; }
/*     */   private static final class ChunkPathElement extends Record implements ProblemReporter.PathElement { private final ChunkPos pos;
/*     */     
/* 472 */     private ChunkPathElement(ChunkPos pos) { this.pos = pos; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #472	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #472	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #472	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement;
/* 472 */       //   0	8	1	o	Ljava/lang/Object; } public ChunkPos pos() { return this.pos; }
/*     */ 
/*     */     
/* 475 */     public String get() { return "chunk@" + String.valueOf(this.pos); } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 480 */   public static ProblemReporter.PathElement problemPath(ChunkPos pos) { return new ChunkPathElement(pos); }
/*     */ 
/*     */ 
/*     */   
/* 484 */   public ProblemReporter.PathElement problemPath() { return problemPath(getPos()); }
/*     */   
/*     */   public abstract BlockState setBlockState(BlockPos paramBlockPos, BlockState paramBlockState, @UpdateFlags int paramInt);
/*     */   
/*     */   public abstract void setBlockEntity(BlockEntity paramBlockEntity);
/*     */   
/*     */   public abstract void addEntity(Entity paramEntity);
/*     */   
/*     */   public abstract ChunkStatus getPersistedStatus();
/*     */   
/*     */   public abstract void removeBlockEntity(BlockPos paramBlockPos);
/*     */   
/*     */   public abstract CompoundTag getBlockEntityNbtForSaving(BlockPos paramBlockPos, HolderLookup.Provider paramProvider);
/*     */   
/*     */   public abstract TickContainerAccess<Block> getBlockTicks();
/*     */   
/*     */   public abstract TickContainerAccess<Fluid> getFluidTicks();
/*     */   
/*     */   public abstract PackedTicks getTicksForSerialization(long paramLong);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\ChunkAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */