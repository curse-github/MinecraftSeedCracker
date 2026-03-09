/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortArrayList;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.LongArrayTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ThreadedLevelLightEngine;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.CarvingMask;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.PalettedContainer;
/*     */ import net.minecraft.world.level.chunk.PalettedContainerFactory;
/*     */ import net.minecraft.world.level.chunk.PalettedContainerRO;
/*     */ import net.minecraft.world.level.chunk.ProtoChunk;
/*     */ import net.minecraft.world.level.chunk.UpgradeData;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.status.ChunkType;
/*     */ import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.blending.BlendingData;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.ticks.LevelChunkTicks;
/*     */ import net.minecraft.world.ticks.ProtoChunkTicks;
/*     */ import net.minecraft.world.ticks.SavedTick;
/*     */ 
/*     */ public final class SerializableChunkData extends Record {
/*     */   private final PalettedContainerFactory containerFactory;
/*     */   private final ChunkPos chunkPos;
/*     */   private final int minSectionY;
/*     */   private final long lastUpdateTime;
/*     */   private final long inhabitedTime;
/*     */   private final ChunkStatus chunkStatus;
/*     */   private final BlendingData.Packed blendingData;
/*     */   private final BelowZeroRetrogen belowZeroRetrogen;
/*     */   private final UpgradeData upgradeData;
/*     */   
/*  76 */   public SerializableChunkData(PalettedContainerFactory containerFactory, ChunkPos chunkPos, int minSectionY, long lastUpdateTime, long inhabitedTime, ChunkStatus chunkStatus, BlendingData.Packed blendingData, BelowZeroRetrogen belowZeroRetrogen, UpgradeData upgradeData, long[] carvingMask, Map<Heightmap.Types, long[]> heightmaps, ChunkAccess.PackedTicks packedTicks, ShortList[] postProcessingSections, boolean lightCorrect, List<SectionData> sectionData, List<CompoundTag> entities, List<CompoundTag> blockEntities, CompoundTag structureData) { this.containerFactory = containerFactory; this.chunkPos = chunkPos; this.minSectionY = minSectionY; this.lastUpdateTime = lastUpdateTime; this.inhabitedTime = inhabitedTime; this.chunkStatus = chunkStatus; this.blendingData = blendingData; this.belowZeroRetrogen = belowZeroRetrogen; this.upgradeData = upgradeData; this.carvingMask = carvingMask; this.heightmaps = heightmaps; this.packedTicks = packedTicks; this.postProcessingSections = postProcessingSections; this.lightCorrect = lightCorrect; this.sectionData = sectionData; this.entities = entities; this.blockEntities = blockEntities; this.structureData = structureData; } private final long[] carvingMask; private final Map<Heightmap.Types, long[]> heightmaps; private final ChunkAccess.PackedTicks packedTicks; private final ShortList[] postProcessingSections; private final boolean lightCorrect; private final List<SectionData> sectionData; private final List<CompoundTag> entities; private final List<CompoundTag> blockEntities; private final CompoundTag structureData; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/storage/SerializableChunkData;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #76	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/storage/SerializableChunkData; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/storage/SerializableChunkData;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #76	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/storage/SerializableChunkData; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/storage/SerializableChunkData;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #76	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/storage/SerializableChunkData;
/*  76 */     //   0	8	1	o	Ljava/lang/Object; } public PalettedContainerFactory containerFactory() { return this.containerFactory; } public ChunkPos chunkPos() { return this.chunkPos; } public int minSectionY() { return this.minSectionY; } public long lastUpdateTime() { return this.lastUpdateTime; } public long inhabitedTime() { return this.inhabitedTime; } public ChunkStatus chunkStatus() { return this.chunkStatus; } public BlendingData.Packed blendingData() { return this.blendingData; } public BelowZeroRetrogen belowZeroRetrogen() { return this.belowZeroRetrogen; } public UpgradeData upgradeData() { return this.upgradeData; } public long[] carvingMask() { return this.carvingMask; } public Map<Heightmap.Types, long[]> heightmaps() { return this.heightmaps; } public ChunkAccess.PackedTicks packedTicks() { return this.packedTicks; } public ShortList[] postProcessingSections() { return this.postProcessingSections; } public boolean lightCorrect() { return this.lightCorrect; } public List<SectionData> sectionData() { return this.sectionData; } public List<CompoundTag> entities() { return this.entities; } public List<CompoundTag> blockEntities() { return this.blockEntities; } public CompoundTag structureData() { return this.structureData; }
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
/*  97 */   private static final Codec<List<SavedTick<Block>>> BLOCK_TICKS_CODEC = SavedTick.codec(BuiltInRegistries.BLOCK.byNameCodec()).listOf();
/*  98 */   private static final Codec<List<SavedTick<Fluid>>> FLUID_TICKS_CODEC = SavedTick.codec(BuiltInRegistries.FLUID.byNameCodec()).listOf();
/*     */   
/* 100 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final String TAG_UPGRADE_DATA = "UpgradeData"; private static final String BLOCK_TICKS_TAG = "block_ticks"; private static final String FLUID_TICKS_TAG = "fluid_ticks"; public static final String X_POS_TAG = "xPos"; public static final String Z_POS_TAG = "zPos";
/*     */   public static final String HEIGHTMAPS_TAG = "Heightmaps";
/*     */   public static final String IS_LIGHT_ON_TAG = "isLightOn";
/*     */   public static final String SECTIONS_TAG = "sections";
/*     */   public static final String BLOCK_LIGHT_TAG = "BlockLight";
/*     */   public static final String SKY_LIGHT_TAG = "SkyLight";
/*     */   
/*     */   public static final class SectionData extends Record { private final int y;
/*     */     private final LevelChunkSection chunkSection;
/*     */     private final DataLayer blockLight;
/*     */     private final DataLayer skyLight;
/*     */     
/* 112 */     public SectionData(int y, LevelChunkSection chunkSection, DataLayer blockLight, DataLayer skyLight) { this.y = y; this.chunkSection = chunkSection; this.blockLight = blockLight; this.skyLight = skyLight; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/storage/SerializableChunkData$SectionData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #112	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/storage/SerializableChunkData$SectionData; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/storage/SerializableChunkData$SectionData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #112	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/storage/SerializableChunkData$SectionData; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/storage/SerializableChunkData$SectionData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #112	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/storage/SerializableChunkData$SectionData;
/* 112 */       //   0	8	1	o	Ljava/lang/Object; } public int y() { return this.y; } public LevelChunkSection chunkSection() { return this.chunkSection; } public DataLayer blockLight() { return this.blockLight; } public DataLayer skyLight() { return this.skyLight; } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SerializableChunkData parse(LevelHeightAccessor levelHeight, PalettedContainerFactory containerFactory, CompoundTag chunkData) {
/* 121 */     if (chunkData.getString("Status").isEmpty()) {
/* 122 */       return null;
/*     */     }
/*     */     
/* 125 */     ChunkPos chunkPos = new ChunkPos(chunkData.getIntOr("xPos", 0), chunkData.getIntOr("zPos", 0));
/* 126 */     long lastUpdateTime = chunkData.getLongOr("LastUpdate", 0L);
/* 127 */     long inhabitedTime = chunkData.getLongOr("InhabitedTime", 0L);
/* 128 */     ChunkStatus status = (ChunkStatus)chunkData.read("Status", ChunkStatus.CODEC).orElse(ChunkStatus.EMPTY);
/*     */     
/* 130 */     UpgradeData upgradeData = (UpgradeData)chunkData.getCompound("UpgradeData").map(tag -> new UpgradeData(tag, levelHeight)).orElse(UpgradeData.EMPTY);
/*     */     
/* 132 */     boolean lightCorrect = chunkData.getBooleanOr("isLightOn", false);
/*     */     
/* 134 */     BlendingData.Packed blendingData = (BlendingData.Packed)chunkData.read("blending_data", BlendingData.Packed.CODEC).orElse(null);
/* 135 */     BelowZeroRetrogen belowZeroRetrogen = (BelowZeroRetrogen)chunkData.read("below_zero_retrogen", BelowZeroRetrogen.CODEC).orElse(null);
/*     */     
/* 137 */     long[] carvingMask = (long[])chunkData.getLongArray("carving_mask").orElse(null);
/*     */     
/* 139 */     Map<Heightmap.Types, long[]> heightmaps = new EnumMap<Heightmap.Types, long[]>(Heightmap.Types.class);
/*     */     
/* 141 */     chunkData.getCompound("Heightmaps").ifPresent(heightmapsTag -> {
/* 142 */           for (Heightmap.Types type : status.heightmapsAfter()) {
/* 143 */             heightmapsTag.getLongArray(type.getSerializationKey()).ifPresent(());
/*     */           }
/*     */         });
/*     */     
/* 147 */     List<SavedTick<Block>> blockTicks = SavedTick.filterTickListForChunk((List)chunkData.read("block_ticks", BLOCK_TICKS_CODEC).orElse(List.of()), chunkPos);
/* 148 */     List<SavedTick<Fluid>> fluidTicks = SavedTick.filterTickListForChunk((List)chunkData.read("fluid_ticks", FLUID_TICKS_CODEC).orElse(List.of()), chunkPos);
/* 149 */     ChunkAccess.PackedTicks packedTicks = new ChunkAccess.PackedTicks(blockTicks, fluidTicks);
/*     */     
/* 151 */     ListTag postProcessTags = chunkData.getListOrEmpty("PostProcessing");
/* 152 */     ShortList[] postProcessingSections = new ShortList[postProcessTags.size()];
/* 153 */     for (int sectionIndex = 0; sectionIndex < postProcessTags.size(); sectionIndex++) {
/* 154 */       ListTag offsetsTag = (ListTag)postProcessTags.getList(sectionIndex).orElse(null);
/* 155 */       if (offsetsTag != null && !offsetsTag.isEmpty()) {
/* 156 */         ShortArrayList shortArrayList = new ShortArrayList(offsetsTag.size());
/* 157 */         for (int i = 0; i < offsetsTag.size(); i++) {
/* 158 */           shortArrayList.add(offsetsTag.getShortOr(i, (short)0));
/*     */         }
/* 160 */         postProcessingSections[sectionIndex] = shortArrayList;
/*     */       } 
/*     */     } 
/*     */     
/* 164 */     List<CompoundTag> entities = chunkData.getList("entities").stream().flatMap(ListTag::compoundStream).toList();
/* 165 */     List<CompoundTag> blockEntities = chunkData.getList("block_entities").stream().flatMap(ListTag::compoundStream).toList();
/*     */     
/* 167 */     CompoundTag structureData = chunkData.getCompoundOrEmpty("structures");
/*     */     
/* 169 */     ListTag sectionTags = chunkData.getListOrEmpty("sections");
/* 170 */     List<SectionData> sectionData = new ArrayList<SectionData>(sectionTags.size());
/*     */     
/* 172 */     Codec<PalettedContainerRO<Holder<Biome>>> biomesCodec = containerFactory.biomeContainerCodec();
/* 173 */     Codec<PalettedContainer<BlockState>> blockStatesCodec = containerFactory.blockStatesContainerCodec();
/*     */     
/* 175 */     for (int i = 0; i < sectionTags.size(); i++) {
/* 176 */       Optional<CompoundTag> maybeSectionTag = sectionTags.getCompound(i);
/* 177 */       if (!maybeSectionTag.isEmpty()) {
/*     */         LevelChunkSection section;
/*     */         
/* 180 */         CompoundTag sectionTag = (CompoundTag)maybeSectionTag.get();
/*     */         
/* 182 */         int y = sectionTag.getByteOr("Y", (byte)0);
/*     */ 
/*     */         
/* 185 */         if (y >= levelHeight.getMinSectionY() && y <= levelHeight.getMaxSectionY()) {
/*     */ 
/*     */           
/* 188 */           Objects.requireNonNull(containerFactory); PalettedContainer<BlockState> blocks = (PalettedContainer)sectionTag.getCompound("block_states").map(container -> (PalettedContainer)blockStatesCodec.parse(NbtOps.INSTANCE, container).promotePartial(()).getOrThrow(ChunkReadException::new)).orElseGet(containerFactory::createForBlockStates);
/*     */ 
/*     */ 
/*     */           
/* 192 */           Objects.requireNonNull(containerFactory); PalettedContainerRO<Holder<Biome>> biomes = (PalettedContainerRO)sectionTag.getCompound("biomes").map(container -> (PalettedContainerRO)biomesCodec.parse(NbtOps.INSTANCE, container).promotePartial(()).getOrThrow(ChunkReadException::new)).orElseGet(containerFactory::createForBiomes);
/*     */           
/* 194 */           section = new LevelChunkSection(blocks, biomes);
/*     */         } else {
/* 196 */           section = null;
/*     */         } 
/*     */         
/* 199 */         DataLayer blockLight = (DataLayer)sectionTag.getByteArray("BlockLight").map(DataLayer::new).orElse(null);
/* 200 */         DataLayer skyLight = (DataLayer)sectionTag.getByteArray("SkyLight").map(DataLayer::new).orElse(null);
/*     */         
/* 202 */         sectionData.add(new SectionData(y, section, blockLight, skyLight));
/*     */       } 
/*     */     } 
/* 205 */     return new SerializableChunkData(containerFactory, chunkPos, levelHeight
/*     */ 
/*     */         
/* 208 */         .getMinSectionY(), lastUpdateTime, inhabitedTime, status, blendingData, belowZeroRetrogen, upgradeData, carvingMask, heightmaps, packedTicks, postProcessingSections, lightCorrect, sectionData, entities, blockEntities, structureData);
/*     */   }
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
/*     */   public ProtoChunk read(ServerLevel level, PoiManager poiManager, RegionStorageInfo regionInfo, ChunkPos pos) {
/*     */     ProtoChunk protoChunk1;
/* 228 */     if (!Objects.equals(pos, this.chunkPos)) {
/* 229 */       LOGGER.error("Chunk file at {} is in the wrong location; relocating. (Expected {}, got {})", new Object[] { pos, pos, this.chunkPos });
/* 230 */       level.getServer().reportMisplacedChunk(this.chunkPos, pos, regionInfo);
/*     */     } 
/*     */     
/* 233 */     int sectionCount = level.getSectionsCount();
/* 234 */     LevelChunkSection[] sections = new LevelChunkSection[sectionCount];
/*     */     
/* 236 */     boolean skyLight = level.dimensionType().hasSkyLight();
/* 237 */     ServerChunkCache serverChunkCache = level.getChunkSource();
/*     */     
/* 239 */     LevelLightEngine lightEngine = serverChunkCache.getLightEngine();
/*     */     
/* 241 */     PalettedContainerFactory containerFactory = level.palettedContainerFactory();
/* 242 */     boolean loadedAnyLight = false;
/* 243 */     for (SectionData section : this.sectionData) {
/* 244 */       SectionPos sectionPos = SectionPos.of(pos, protoChunk1.y);
/*     */       
/* 246 */       if (protoChunk1.chunkSection != null) {
/* 247 */         sections[level.getSectionIndexFromSectionY(protoChunk1.y)] = protoChunk1.chunkSection;
/* 248 */         poiManager.checkConsistencyWithBlocks(sectionPos, protoChunk1.chunkSection);
/*     */       } 
/*     */       
/* 251 */       boolean hasBlockLight = (protoChunk1.blockLight != null);
/* 252 */       boolean hasSkyLight = (skyLight && protoChunk1.skyLight != null);
/* 253 */       if (hasBlockLight || hasSkyLight) {
/* 254 */         if (!loadedAnyLight) {
/* 255 */           lightEngine.retainData(pos, true);
/* 256 */           loadedAnyLight = true;
/*     */         } 
/* 258 */         if (hasBlockLight) {
/* 259 */           lightEngine.queueSectionData(LightLayer.BLOCK, sectionPos, protoChunk1.blockLight);
/*     */         }
/* 261 */         if (hasSkyLight) {
/* 262 */           lightEngine.queueSectionData(LightLayer.SKY, sectionPos, protoChunk1.skyLight);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 267 */     ChunkType chunkType = this.chunkStatus.getChunkType();
/*     */ 
/*     */     
/* 270 */     if (chunkType == ChunkType.LEVELCHUNK) {
/* 271 */       LevelChunkTicks<Block> blockTicks = new LevelChunkTicks<Block>(this.packedTicks.blocks());
/* 272 */       LevelChunkTicks<Fluid> fluidTicks = new LevelChunkTicks<Fluid>(this.packedTicks.fluids());
/* 273 */       protoChunk1 = new LevelChunk(level.getLevel(), pos, this.upgradeData, blockTicks, fluidTicks, this.inhabitedTime, sections, postLoadChunk(level, this.entities, this.blockEntities), BlendingData.unpack(this.blendingData));
/*     */     } else {
/* 275 */       ProtoChunkTicks<Block> blockTicks = ProtoChunkTicks.load(this.packedTicks.blocks());
/* 276 */       ProtoChunkTicks<Fluid> fluidTicks = ProtoChunkTicks.load(this.packedTicks.fluids());
/*     */       
/* 278 */       ProtoChunk protoChunk = new ProtoChunk(pos, this.upgradeData, sections, blockTicks, fluidTicks, level, containerFactory, BlendingData.unpack(this.blendingData));
/* 279 */       protoChunk1 = protoChunk;
/* 280 */       protoChunk1.setInhabitedTime(this.inhabitedTime);
/*     */       
/* 282 */       if (this.belowZeroRetrogen != null) {
/* 283 */         protoChunk.setBelowZeroRetrogen(this.belowZeroRetrogen);
/*     */       }
/*     */       
/* 286 */       protoChunk.setPersistedStatus(this.chunkStatus);
/* 287 */       if (this.chunkStatus.isOrAfter(ChunkStatus.INITIALIZE_LIGHT)) {
/* 288 */         protoChunk.setLightEngine(lightEngine);
/*     */       }
/*     */     } 
/*     */     
/* 292 */     protoChunk1.setLightCorrect(this.lightCorrect);
/*     */     
/* 294 */     EnumSet<Heightmap.Types> toPrime = EnumSet.noneOf(Heightmap.Types.class);
/* 295 */     for (Heightmap.Types type : protoChunk1.getPersistedStatus().heightmapsAfter()) {
/* 296 */       long[] heightmap = (long[])this.heightmaps.get(type);
/* 297 */       if (heightmap != null) {
/* 298 */         protoChunk1.setHeightmap(type, heightmap); continue;
/*     */       } 
/* 300 */       toPrime.add(type);
/*     */     } 
/*     */     
/* 303 */     Heightmap.primeHeightmaps(protoChunk1, toPrime);
/*     */     
/* 305 */     protoChunk1.setAllStarts(unpackStructureStart(StructurePieceSerializationContext.fromLevel(level), this.structureData, level.getSeed()));
/* 306 */     protoChunk1.setAllReferences(unpackStructureReferences(level.registryAccess(), pos, this.structureData));
/*     */     
/* 308 */     for (int sectionIndex = 0; sectionIndex < this.postProcessingSections.length; sectionIndex++) {
/* 309 */       ShortList postProcessingSection = this.postProcessingSections[sectionIndex];
/* 310 */       if (postProcessingSection != null) {
/* 311 */         protoChunk1.addPackedPostProcess(postProcessingSection, sectionIndex);
/*     */       }
/*     */     } 
/*     */     
/* 315 */     if (chunkType == ChunkType.LEVELCHUNK) {
/* 316 */       return new ImposterProtoChunk((LevelChunk)protoChunk1, false);
/*     */     }
/*     */     
/* 319 */     ProtoChunk protoChunk = (ProtoChunk)protoChunk1;
/* 320 */     for (CompoundTag entity : this.entities) {
/* 321 */       protoChunk.addEntity(entity);
/*     */     }
/*     */     
/* 324 */     for (CompoundTag blockEntity : this.blockEntities) {
/* 325 */       protoChunk.setBlockEntityNbt(blockEntity);
/*     */     }
/*     */     
/* 328 */     if (this.carvingMask != null) {
/* 329 */       protoChunk.setCarvingMask(new CarvingMask(this.carvingMask, protoChunk1.getMinY()));
/*     */     }
/*     */     
/* 332 */     return protoChunk;
/*     */   }
/*     */ 
/*     */   
/* 336 */   private static void logErrors(ChunkPos pos, int sectionY, String message) { LOGGER.error("Recoverable errors when loading section [{}, {}, {}]: {}", new Object[] { Integer.valueOf(pos.x), Integer.valueOf(sectionY), Integer.valueOf(pos.z), message }); }
/*     */ 
/*     */   
/*     */   public static SerializableChunkData copyOf(ServerLevel level, ChunkAccess chunk) {
/* 340 */     if (!chunk.canBeSerialized()) {
/* 341 */       throw new IllegalArgumentException("Chunk can't be serialized: " + String.valueOf(chunk));
/*     */     }
/* 343 */     ChunkPos pos = chunk.getPos();
/*     */     
/* 345 */     List<SectionData> sectionData = new ArrayList<SectionData>();
/* 346 */     LevelChunkSection[] chunkSections = chunk.getSections();
/* 347 */     ThreadedLevelLightEngine threadedLevelLightEngine = level.getChunkSource().getLightEngine();
/*     */     
/* 349 */     for (int sectionY = threadedLevelLightEngine.getMinLightSection(); sectionY < threadedLevelLightEngine.getMaxLightSection(); sectionY++) {
/*     */       
/* 351 */       int sectionIndex = chunk.getSectionIndexFromSectionY(sectionY);
/* 352 */       boolean hasSection = (sectionIndex >= 0 && sectionIndex < chunkSections.length);
/*     */       
/* 354 */       DataLayer sourceBlockLight = threadedLevelLightEngine.getLayerListener(LightLayer.BLOCK).getDataLayerData(SectionPos.of(pos, sectionY));
/* 355 */       DataLayer sourceSkyLight = threadedLevelLightEngine.getLayerListener(LightLayer.SKY).getDataLayerData(SectionPos.of(pos, sectionY));
/*     */       
/* 357 */       DataLayer blockLight = (sourceBlockLight != null && !sourceBlockLight.isEmpty()) ? sourceBlockLight.copy() : null;
/* 358 */       DataLayer skyLight = (sourceSkyLight != null && !sourceSkyLight.isEmpty()) ? sourceSkyLight.copy() : null;
/*     */       
/* 360 */       if (hasSection || blockLight != null || skyLight != null) {
/*     */ 
/*     */ 
/*     */         
/* 364 */         LevelChunkSection section = hasSection ? chunkSections[sectionIndex].copy() : null;
/* 365 */         sectionData.add(new SectionData(sectionY, section, blockLight, skyLight));
/*     */       } 
/*     */     } 
/* 368 */     List<CompoundTag> blockEntities = new ArrayList<CompoundTag>(chunk.getBlockEntitiesPos().size());
/* 369 */     for (BlockPos blockPos : chunk.getBlockEntitiesPos()) {
/* 370 */       CompoundTag blockEntityTag = chunk.getBlockEntityNbtForSaving(blockPos, level.registryAccess());
/* 371 */       if (blockEntityTag != null) {
/* 372 */         blockEntities.add(blockEntityTag);
/*     */       }
/*     */     } 
/*     */     
/* 376 */     List<CompoundTag> entities = new ArrayList<CompoundTag>();
/* 377 */     long[] carvingMask = null;
/* 378 */     if (chunk.getPersistedStatus().getChunkType() == ChunkType.PROTOCHUNK) {
/* 379 */       ProtoChunk protoChunk = (ProtoChunk)chunk;
/*     */       
/* 381 */       entities.addAll(protoChunk.getEntities());
/*     */       
/* 383 */       CarvingMask existingMask = protoChunk.getCarvingMask();
/* 384 */       if (existingMask != null) {
/* 385 */         carvingMask = existingMask.toArray();
/*     */       }
/*     */     } 
/*     */     
/* 389 */     Map<Heightmap.Types, long[]> heightmaps = new EnumMap<Heightmap.Types, long[]>(Heightmap.Types.class);
/* 390 */     for (Map.Entry<Heightmap.Types, Heightmap> entry : chunk.getHeightmaps()) {
/* 391 */       if (chunk.getPersistedStatus().heightmapsAfter().contains(entry.getKey())) {
/* 392 */         long[] data = ((Heightmap)entry.getValue()).getRawData();
/* 393 */         heightmaps.put((Heightmap.Types)entry.getKey(), (long[])data.clone());
/*     */       } 
/*     */     } 
/*     */     
/* 397 */     ChunkAccess.PackedTicks ticksForSerialization = chunk.getTicksForSerialization(level.getGameTime());
/*     */ 
/*     */ 
/*     */     
/* 401 */     ShortList[] postProcessingSections = (ShortList[])Arrays.stream(chunk.getPostProcessing()).map(shorts -> (shorts != null && !shorts.isEmpty()) ? new ShortArrayList(shorts) : null).toArray(x$0 -> new ShortList[x$0]);
/*     */     
/* 403 */     CompoundTag structureData = packStructureData(StructurePieceSerializationContext.fromLevel(level), pos, chunk.getAllStarts(), chunk.getAllReferences());
/*     */     
/* 405 */     return new SerializableChunkData(level
/* 406 */         .palettedContainerFactory(), pos, chunk
/*     */         
/* 408 */         .getMinSectionY(), level
/* 409 */         .getGameTime(), chunk
/* 410 */         .getInhabitedTime(), chunk
/* 411 */         .getPersistedStatus(), 
/* 412 */         (BlendingData.Packed)Optionull.map(chunk.getBlendingData(), BlendingData::pack), chunk
/* 413 */         .getBelowZeroRetrogen(), chunk
/* 414 */         .getUpgradeData().copy(), carvingMask, heightmaps, ticksForSerialization, postProcessingSections, chunk
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 419 */         .isLightCorrect(), sectionData, entities, blockEntities, structureData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompoundTag write() {
/* 428 */     CompoundTag tag = NbtUtils.addCurrentDataVersion(new CompoundTag());
/* 429 */     tag.putInt("xPos", this.chunkPos.x);
/* 430 */     tag.putInt("yPos", this.minSectionY);
/* 431 */     tag.putInt("zPos", this.chunkPos.z);
/* 432 */     tag.putLong("LastUpdate", this.lastUpdateTime);
/* 433 */     tag.putLong("InhabitedTime", this.inhabitedTime);
/* 434 */     tag.putString("Status", BuiltInRegistries.CHUNK_STATUS.getKey(this.chunkStatus).toString());
/* 435 */     tag.storeNullable("blending_data", BlendingData.Packed.CODEC, this.blendingData);
/*     */     
/* 437 */     tag.storeNullable("below_zero_retrogen", BelowZeroRetrogen.CODEC, this.belowZeroRetrogen);
/*     */     
/* 439 */     if (!this.upgradeData.isEmpty()) {
/* 440 */       tag.put("UpgradeData", this.upgradeData.write());
/*     */     }
/*     */     
/* 443 */     ListTag sectionTags = new ListTag();
/*     */     
/* 445 */     Codec<PalettedContainer<BlockState>> blockStatesCodec = this.containerFactory.blockStatesContainerCodec();
/* 446 */     Codec<PalettedContainerRO<Holder<Biome>>> biomeCodec = this.containerFactory.biomeContainerCodec();
/*     */     
/* 448 */     for (SectionData section : this.sectionData) {
/* 449 */       CompoundTag sectionTag = new CompoundTag();
/*     */       
/* 451 */       LevelChunkSection chunkSection = section.chunkSection;
/* 452 */       if (chunkSection != null) {
/* 453 */         sectionTag.store("block_states", blockStatesCodec, chunkSection.getStates());
/* 454 */         sectionTag.store("biomes", biomeCodec, chunkSection.getBiomes());
/*     */       } 
/* 456 */       if (section.blockLight != null) {
/* 457 */         sectionTag.putByteArray("BlockLight", section.blockLight.getData());
/*     */       }
/* 459 */       if (section.skyLight != null) {
/* 460 */         sectionTag.putByteArray("SkyLight", section.skyLight.getData());
/*     */       }
/*     */       
/* 463 */       if (!sectionTag.isEmpty()) {
/* 464 */         sectionTag.putByte("Y", (byte)section.y);
/* 465 */         sectionTags.add(sectionTag);
/*     */       } 
/*     */     } 
/*     */     
/* 469 */     tag.put("sections", sectionTags);
/*     */     
/* 471 */     if (this.lightCorrect) {
/* 472 */       tag.putBoolean("isLightOn", true);
/*     */     }
/*     */     
/* 475 */     ListTag blockEntityTags = new ListTag();
/* 476 */     blockEntityTags.addAll(this.blockEntities);
/* 477 */     tag.put("block_entities", blockEntityTags);
/*     */     
/* 479 */     if (this.chunkStatus.getChunkType() == ChunkType.PROTOCHUNK) {
/* 480 */       ListTag entityTags = new ListTag();
/* 481 */       entityTags.addAll(this.entities);
/* 482 */       tag.put("entities", entityTags);
/* 483 */       if (this.carvingMask != null) {
/* 484 */         tag.putLongArray("carving_mask", this.carvingMask);
/*     */       }
/*     */     } 
/*     */     
/* 488 */     saveTicks(tag, this.packedTicks);
/*     */     
/* 490 */     tag.put("PostProcessing", packOffsets(this.postProcessingSections));
/*     */     
/* 492 */     CompoundTag heightmapsTag = new CompoundTag();
/* 493 */     this.heightmaps.forEach((type, data) -> 
/* 494 */         heightmapsTag.put(type.getSerializationKey(), new LongArrayTag(data)));
/*     */     
/* 496 */     tag.put("Heightmaps", heightmapsTag);
/* 497 */     tag.put("structures", this.structureData);
/*     */     
/* 499 */     return tag;
/*     */   }
/*     */   
/*     */   private static void saveTicks(CompoundTag levelData, ChunkAccess.PackedTicks ticksForSerialization) {
/* 503 */     levelData.store("block_ticks", BLOCK_TICKS_CODEC, ticksForSerialization.blocks());
/* 504 */     levelData.store("fluid_ticks", FLUID_TICKS_CODEC, ticksForSerialization.fluids());
/*     */   }
/*     */ 
/*     */   
/* 508 */   public static ChunkStatus getChunkStatusFromTag(CompoundTag tag) { return (tag != null) ? (ChunkStatus)tag.read("Status", ChunkStatus.CODEC).orElse(ChunkStatus.EMPTY) : ChunkStatus.EMPTY; }
/*     */ 
/*     */   
/*     */   private static LevelChunk.PostLoadProcessor postLoadChunk(ServerLevel level, List<CompoundTag> entities, List<CompoundTag> blockEntities) {
/* 512 */     if (entities.isEmpty() && blockEntities.isEmpty()) {
/* 513 */       return null;
/*     */     }
/*     */     
/* 516 */     return levelChunk -> {
/*     */         
/* 518 */         if (!entities.isEmpty()) {
/* 519 */           ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(levelChunk.problemPath(), LOGGER); 
/* 520 */           try { level.addLegacyChunkEntities(EntityType.loadEntitiesRecursive(TagValueInput.create(reporter, level.registryAccess(), entities), level, EntitySpawnReason.LOAD));
/* 521 */             reporter.close(); } catch (Throwable throwable) { try { reporter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */              throw throwable; }
/*     */         
/* 524 */         }  for (CompoundTag entityTag : blockEntities) {
/* 525 */           boolean keepPacked = entityTag.getBooleanOr("keepPacked", false);
/* 526 */           if (keepPacked) {
/* 527 */             levelChunk.setBlockEntityNbt(entityTag);
/*     */             continue;
/*     */           } 
/* 530 */           BlockPos pos = BlockEntity.getPosFromTag(levelChunk.getPos(), entityTag);
/* 531 */           BlockEntity blockEntity = BlockEntity.loadStatic(pos, levelChunk.getBlockState(pos), entityTag, level.registryAccess());
/* 532 */           if (blockEntity != null) {
/* 533 */             levelChunk.setBlockEntity(blockEntity);
/*     */           }
/*     */         } 
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   private static CompoundTag packStructureData(StructurePieceSerializationContext context, ChunkPos pos, Map<Structure, StructureStart> starts, Map<Structure, LongSet> references) {
/* 541 */     CompoundTag outTag = new CompoundTag();
/*     */     
/* 543 */     CompoundTag startsTag = new CompoundTag();
/* 544 */     Registry<Structure> structuresRegistry = context.registryAccess().lookupOrThrow(Registries.STRUCTURE);
/* 545 */     for (Map.Entry<Structure, StructureStart> entry : starts.entrySet()) {
/* 546 */       Identifier key = structuresRegistry.getKey((Structure)entry.getKey());
/* 547 */       startsTag.put(key.toString(), ((StructureStart)entry.getValue()).createTag(context, pos));
/*     */     } 
/* 549 */     outTag.put("starts", startsTag);
/*     */     
/* 551 */     CompoundTag referencesTag = new CompoundTag();
/* 552 */     for (Map.Entry<Structure, LongSet> entry : references.entrySet()) {
/* 553 */       if (((LongSet)entry.getValue()).isEmpty()) {
/*     */         continue;
/*     */       }
/* 556 */       Identifier key = structuresRegistry.getKey((Structure)entry.getKey());
/* 557 */       referencesTag.putLongArray(key.toString(), ((LongSet)entry.getValue()).toLongArray());
/*     */     } 
/* 559 */     outTag.put("References", referencesTag);
/*     */     
/* 561 */     return outTag;
/*     */   }
/*     */   
/*     */   private static Map<Structure, StructureStart> unpackStructureStart(StructurePieceSerializationContext context, CompoundTag tag, long seed) {
/* 565 */     Map<Structure, StructureStart> outmap = Maps.newHashMap();
/*     */     
/* 567 */     Registry<Structure> structuresRegistry = context.registryAccess().lookupOrThrow(Registries.STRUCTURE);
/* 568 */     CompoundTag startsTag = tag.getCompoundOrEmpty("starts");
/* 569 */     for (String key : startsTag.keySet()) {
/* 570 */       Identifier id = Identifier.tryParse(key);
/* 571 */       Structure startFeature = (Structure)structuresRegistry.getValue(id);
/* 572 */       if (startFeature == null) {
/* 573 */         LOGGER.error("Unknown structure start: {}", id);
/*     */         continue;
/*     */       } 
/* 576 */       StructureStart start = StructureStart.loadStaticStart(context, startsTag.getCompoundOrEmpty(key), seed);
/* 577 */       if (start != null) {
/* 578 */         outmap.put(startFeature, start);
/*     */       }
/*     */     } 
/*     */     
/* 582 */     return outmap;
/*     */   }
/*     */   
/*     */   private static Map<Structure, LongSet> unpackStructureReferences(RegistryAccess registryAccess, ChunkPos pos, CompoundTag tag) {
/* 586 */     Map<Structure, LongSet> outmap = Maps.newHashMap();
/*     */     
/* 588 */     Registry<Structure> structuresRegistry = registryAccess.lookupOrThrow(Registries.STRUCTURE);
/* 589 */     CompoundTag referencesTag = tag.getCompoundOrEmpty("References");
/* 590 */     referencesTag.forEach((key, entry) -> {
/*     */           
/* 592 */           Identifier structureId = Identifier.tryParse(key);
/* 593 */           Structure structureType = (Structure)structuresRegistry.getValue(structureId);
/* 594 */           if (structureType == null) {
/* 595 */             LOGGER.warn("Found reference to unknown structure '{}' in chunk {}, discarding", structureId, pos);
/*     */             return;
/*     */           } 
/* 598 */           Optional<long[]> longArray = entry.asLongArray();
/* 599 */           if (longArray.isEmpty()) {
/*     */             return;
/*     */           }
/* 602 */           outmap.put(structureType, new LongOpenHashSet(Arrays.stream((long[])longArray.get()).filter(())
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 610 */                 .toArray()));
/*     */         });
/*     */     
/* 613 */     return outmap;
/*     */   }
/*     */   
/*     */   private static ListTag packOffsets(ShortList[] sections) {
/* 617 */     ListTag listTag = new ListTag();
/* 618 */     for (ShortList offsetList : sections) {
/* 619 */       ListTag offsetsTag = new ListTag();
/* 620 */       if (offsetList != null) {
/* 621 */         for (int i = 0; i < offsetList.size(); i++) {
/* 622 */           offsetsTag.add(ShortTag.valueOf(offsetList.getShort(i)));
/*     */         }
/*     */       }
/* 625 */       listTag.add(offsetsTag);
/*     */     } 
/* 627 */     return listTag;
/*     */   }
/*     */   
/*     */   public static class ChunkReadException
/*     */     extends NbtException {
/* 632 */     public ChunkReadException(String message) { super(message); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\storage\SerializableChunkData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */