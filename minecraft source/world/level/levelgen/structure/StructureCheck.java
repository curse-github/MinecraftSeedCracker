/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.Long2BooleanMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.IntTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.visitors.CollectFields;
/*     */ import net.minecraft.nbt.visitors.FieldSelector;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.storage.ChunkScanAccess;
/*     */ import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class StructureCheck
/*     */ {
/*  44 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int NO_STRUCTURE = -1;
/*     */   
/*     */   private final ChunkScanAccess storageAccess;
/*     */   
/*     */   private final RegistryAccess registryAccess;
/*     */   
/*     */   private final StructureTemplateManager structureTemplateManager;
/*     */   
/*     */   private final ResourceKey<Level> dimension;
/*     */   
/*     */   private final ChunkGenerator chunkGenerator;
/*     */   
/*     */   public StructureCheck(ChunkScanAccess storageAccess, RegistryAccess registryAccess, StructureTemplateManager structureTemplateManager, ResourceKey<Level> dimension, ChunkGenerator chunkGenerator, RandomState randomState, LevelHeightAccessor heightAccessor, BiomeSource biomeSource, long seed, DataFixer fixerUpper) {
/*  59 */     this.loadedChunks = new Long2ObjectOpenHashMap();
/*  60 */     this.featureChecks = new HashMap();
/*     */ 
/*     */     
/*  63 */     this.storageAccess = storageAccess;
/*  64 */     this.registryAccess = registryAccess;
/*  65 */     this.structureTemplateManager = structureTemplateManager;
/*  66 */     this.dimension = dimension;
/*  67 */     this.chunkGenerator = chunkGenerator;
/*  68 */     this.randomState = randomState;
/*  69 */     this.heightAccessor = heightAccessor;
/*  70 */     this.biomeSource = biomeSource;
/*  71 */     this.seed = seed;
/*  72 */     this.fixerUpper = fixerUpper;
/*     */   }
/*     */   private final RandomState randomState; private final LevelHeightAccessor heightAccessor; private final BiomeSource biomeSource; private final long seed; private final DataFixer fixerUpper; private final Long2ObjectMap<Object2IntMap<Structure>> loadedChunks; private final Map<Structure, Long2BooleanMap> featureChecks;
/*     */   public StructureCheckResult checkStart(ChunkPos pos, Structure structure, StructurePlacement placement, boolean requireUnreferenced) {
/*  76 */     long posKey = pos.toLong();
/*  77 */     Object2IntMap<Structure> cachedResult = (Object2IntMap)this.loadedChunks.get(posKey);
/*  78 */     if (cachedResult != null) {
/*  79 */       return checkStructureInfo(cachedResult, structure, requireUnreferenced);
/*     */     }
/*     */ 
/*     */     
/*  83 */     StructureCheckResult storageCheckResult = tryLoadFromStorage(pos, structure, requireUnreferenced, posKey);
/*  84 */     if (storageCheckResult != null)
/*     */     {
/*  86 */       return storageCheckResult;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     if (!placement.applyAdditionalChunkRestrictions(pos.x, pos.z, this.seed)) {
/*  95 */       return StructureCheckResult.START_NOT_PRESENT;
/*     */     }
/*     */ 
/*     */     
/*  99 */     boolean isFeatureChunk = ((Long2BooleanMap)this.featureChecks.computeIfAbsent(structure, k -> new Long2BooleanOpenHashMap())).computeIfAbsent(posKey, k -> 
/* 100 */         canCreateStructure(pos, structure));
/*     */ 
/*     */     
/* 103 */     if (!isFeatureChunk)
/*     */     {
/* 105 */       return StructureCheckResult.START_NOT_PRESENT;
/*     */     }
/*     */ 
/*     */     
/* 109 */     return StructureCheckResult.CHUNK_LOAD_NEEDED;
/*     */   }
/*     */ 
/*     */   
/* 113 */   private boolean canCreateStructure(ChunkPos pos, Structure structure) { Objects.requireNonNull(structure.biomes()); return structure.findValidGenerationPoint(new Structure.GenerationContext(this.registryAccess, this.chunkGenerator, this.biomeSource, this.randomState, this.structureTemplateManager, this.seed, pos, this.heightAccessor, structure.biomes()::contains)).isPresent(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StructureCheckResult tryLoadFromStorage(ChunkPos pos, Structure structure, boolean requireUnreferenced, long posKey) {
/*     */     CompoundTag fixedChunkTag;
/* 120 */     CollectFields collectFields = new CollectFields(new FieldSelector[] { new FieldSelector(IntTag.TYPE, "DataVersion"), new FieldSelector("Level", "Structures", CompoundTag.TYPE, "Starts"), new FieldSelector("structures", CompoundTag.TYPE, "starts") });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 127 */       this.storageAccess.scanChunk(pos, collectFields).join();
/* 128 */     } catch (Exception e) {
/* 129 */       LOGGER.warn("Failed to read chunk {}", pos, e);
/* 130 */       return StructureCheckResult.CHUNK_LOAD_NEEDED;
/*     */     } 
/* 132 */     Tag result = collectFields.getResult();
/* 133 */     if (!(result instanceof CompoundTag))
/*     */     {
/* 135 */       return null;
/*     */     }
/*     */     
/* 138 */     CompoundTag chunkTag = (CompoundTag)result;
/* 139 */     int version = NbtUtils.getDataVersion(chunkTag);
/*     */     
/* 141 */     if (version <= 1493)
/*     */     {
/* 143 */       return StructureCheckResult.CHUNK_LOAD_NEEDED;
/*     */     }
/*     */     
/* 146 */     SimpleRegionStorage.injectDatafixingContext(chunkTag, ChunkMap.getChunkDataFixContextTag(this.dimension, this.chunkGenerator.getTypeNameForDataFixer()));
/*     */ 
/*     */     
/*     */     try {
/* 150 */       fixedChunkTag = DataFixTypes.CHUNK.updateToCurrentVersion(this.fixerUpper, chunkTag, version);
/* 151 */     } catch (Exception e) {
/* 152 */       LOGGER.warn("Failed to partially datafix chunk {}", pos, e);
/*     */       
/* 154 */       return StructureCheckResult.CHUNK_LOAD_NEEDED;
/*     */     } 
/*     */     
/* 157 */     Object2IntMap<Structure> knownStarts = loadStructures(fixedChunkTag);
/* 158 */     if (knownStarts == null)
/*     */     {
/* 160 */       return null;
/*     */     }
/*     */     
/* 163 */     storeFullResults(posKey, knownStarts);
/* 164 */     return checkStructureInfo(knownStarts, structure, requireUnreferenced);
/*     */   }
/*     */ 
/*     */   
/*     */   private Object2IntMap<Structure> loadStructures(CompoundTag chunkTag) {
/* 169 */     Optional<CompoundTag> maybeStartsTag = chunkTag.getCompound("structures").flatMap(tag -> tag.getCompound("starts"));
/* 170 */     if (maybeStartsTag.isEmpty()) {
/* 171 */       return null;
/*     */     }
/*     */     
/* 174 */     CompoundTag startsTag = (CompoundTag)maybeStartsTag.get();
/* 175 */     if (startsTag.isEmpty()) {
/* 176 */       return Object2IntMaps.emptyMap();
/*     */     }
/*     */     
/* 179 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 180 */     Registry<Structure> structuresRegistry = this.registryAccess.lookupOrThrow(Registries.STRUCTURE);
/* 181 */     startsTag.forEach((key, tag) -> {
/* 182 */           Identifier id = Identifier.tryParse(key);
/* 183 */           if (id == null) {
/*     */             return;
/*     */           }
/* 186 */           Structure foundFeature = (Structure)structuresRegistry.getValue(id);
/* 187 */           if (foundFeature == null) {
/*     */             return;
/*     */           }
/*     */           
/* 191 */           tag.asCompound().ifPresent(());
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 199 */     return object2IntOpenHashMap;
/*     */   }
/*     */ 
/*     */   
/* 203 */   private static Object2IntMap<Structure> deduplicateEmptyMap(Object2IntMap<Structure> map) { return map.isEmpty() ? Object2IntMaps.emptyMap() : map; }
/*     */ 
/*     */   
/*     */   private StructureCheckResult checkStructureInfo(Object2IntMap<Structure> cachedResult, Structure structure, boolean requireUnreferenced) {
/* 207 */     int referenceCount = cachedResult.getOrDefault(structure, -1);
/*     */     
/* 209 */     return (referenceCount != -1 && (!requireUnreferenced || referenceCount == 0)) ? StructureCheckResult.START_PRESENT : StructureCheckResult.START_NOT_PRESENT;
/*     */   }
/*     */   
/*     */   public void onStructureLoad(ChunkPos pos, Map<Structure, StructureStart> starts) {
/* 213 */     long posKey = pos.toLong();
/*     */     
/* 215 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 216 */     starts.forEach((structure, structureStart) -> {
/* 217 */           if (structureStart.isValid()) {
/* 218 */             startsToReferences.put(structure, structureStart.getReferences());
/*     */           }
/*     */         });
/* 221 */     storeFullResults(posKey, object2IntOpenHashMap);
/*     */   }
/*     */   
/*     */   private void storeFullResults(long posKey, Object2IntMap<Structure> starts) {
/* 225 */     this.loadedChunks.put(posKey, deduplicateEmptyMap(starts));
/*     */ 
/*     */     
/* 228 */     this.featureChecks.values().forEach(m -> m.remove(posKey));
/*     */   }
/*     */   
/*     */   public void incrementReference(ChunkPos chunkPos, Structure structure) {
/* 232 */     this.loadedChunks.compute(chunkPos.toLong(), (key, counts) -> {
/* 233 */           Object2IntOpenHashMap object2IntOpenHashMap; if (counts == null || counts.isEmpty()) {
/* 234 */             object2IntOpenHashMap = new Object2IntOpenHashMap();
/*     */           }
/* 236 */           object2IntOpenHashMap.computeInt(structure, ());
/* 237 */           return object2IntOpenHashMap;
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\StructureCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */