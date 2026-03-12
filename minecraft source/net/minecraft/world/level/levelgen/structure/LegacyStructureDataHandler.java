/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.storage.LegacyTagFixer;
/*     */ import net.minecraft.world.level.storage.DimensionDataStorage;
/*     */ 
/*     */ public class LegacyStructureDataHandler implements LegacyTagFixer {
/*     */   public static final int LAST_MONOLYTH_STRUCTURE_DATA_VERSION = 1493;
/*  33 */   private static final Map<String, String> CURRENT_TO_LEGACY_MAP = (Map)Util.make(Maps.newHashMap(), map -> {
/*  34 */         map.put("Village", "Village");
/*  35 */         map.put("Mineshaft", "Mineshaft");
/*  36 */         map.put("Mansion", "Mansion");
/*  37 */         map.put("Igloo", "Temple");
/*  38 */         map.put("Desert_Pyramid", "Temple");
/*  39 */         map.put("Jungle_Pyramid", "Temple");
/*  40 */         map.put("Swamp_Hut", "Temple");
/*  41 */         map.put("Stronghold", "Stronghold");
/*  42 */         map.put("Monument", "Monument");
/*  43 */         map.put("Fortress", "Fortress");
/*  44 */         map.put("EndCity", "EndCity");
/*     */       });
/*     */ 
/*     */   
/*  48 */   private static final Map<String, String> LEGACY_TO_CURRENT_MAP = (Map)Util.make(Maps.newHashMap(), map -> {
/*  49 */         map.put("Iglu", "Igloo");
/*  50 */         map.put("TeDP", "Desert_Pyramid");
/*  51 */         map.put("TeJP", "Jungle_Pyramid");
/*  52 */         map.put("TeSH", "Swamp_Hut");
/*     */       });
/*     */ 
/*     */   
/*  56 */   private static final Set<String> OLD_STRUCTURE_REGISTRY_KEYS = Set.of(new String[] { "pillager_outpost", "mineshaft", "mansion", "jungle_pyramid", "desert_pyramid", "igloo", "ruined_portal", "shipwreck", "swamp_hut", "stronghold", "monument", "ocean_ruin", "fortress", "endcity", "buried_treasure", "village", "nether_fossil", "bastion_remnant" });
/*     */ 
/*     */   
/*     */   private final boolean hasLegacyData;
/*     */ 
/*     */   
/*     */   private final Map<String, Long2ObjectMap<CompoundTag>> dataMap;
/*     */   
/*     */   private final Map<String, StructureFeatureIndexSavedData> indexMap;
/*     */   
/*     */   private final DimensionDataStorage dimensionDataStorage;
/*     */   
/*     */   private final List<String> legacyKeys;
/*     */   
/*     */   private final List<String> currentKeys;
/*     */   
/*     */   private final DataFixer dataFixer;
/*     */   
/*     */   private boolean cachesInitialized;
/*     */ 
/*     */   
/*     */   public LegacyStructureDataHandler(DimensionDataStorage dimensionDataStorage, List<String> legacyKeys, List<String> currentKeys, DataFixer dataFixer) {
/*  78 */     this.dataMap = Maps.newHashMap();
/*  79 */     this.indexMap = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     this.dimensionDataStorage = dimensionDataStorage;
/*  88 */     this.legacyKeys = legacyKeys;
/*  89 */     this.currentKeys = currentKeys;
/*  90 */     this.dataFixer = dataFixer;
/*     */ 
/*     */     
/*  93 */     boolean b = false;
/*  94 */     for (String legacyKey : this.currentKeys) {
/*  95 */       b |= ((this.dataMap.get(legacyKey) != null));
/*     */     }
/*  97 */     this.hasLegacyData = b;
/*     */   }
/*     */ 
/*     */   
/*     */   public void markChunkDone(ChunkPos pos) {
/* 102 */     long index = pos.toLong();
/* 103 */     for (String legacyKey : this.legacyKeys) {
/* 104 */       StructureFeatureIndexSavedData savedData = (StructureFeatureIndexSavedData)this.indexMap.get(legacyKey);
/* 105 */       if (savedData != null && savedData.hasUnhandledIndex(index)) {
/* 106 */         savedData.removeIndex(index);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 113 */   public int targetDataVersion() { return 1493; }
/*     */ 
/*     */ 
/*     */   
/*     */   public CompoundTag applyFix(CompoundTag chunkTag) {
/* 118 */     if (!this.cachesInitialized && this.dimensionDataStorage != null) {
/* 119 */       populateCaches(this.dimensionDataStorage);
/*     */     }
/* 121 */     int version = NbtUtils.getDataVersion(chunkTag);
/*     */     
/* 123 */     if (version < 1493) {
/* 124 */       chunkTag = DataFixTypes.CHUNK.update(this.dataFixer, chunkTag, version, 1493);
/*     */       
/* 126 */       if (((Boolean)chunkTag.getCompound("Level").flatMap(level -> level.getBoolean("hasLegacyStructureData")).orElse(Boolean.valueOf(false))).booleanValue()) {
/* 127 */         chunkTag = updateFromLegacy(chunkTag);
/*     */       }
/*     */     } 
/* 130 */     return chunkTag;
/*     */   }
/*     */   
/*     */   private CompoundTag updateFromLegacy(CompoundTag tag) {
/* 134 */     CompoundTag levelTag = tag.getCompoundOrEmpty("Level");
/*     */     
/* 136 */     ChunkPos pos = new ChunkPos(levelTag.getIntOr("xPos", 0), levelTag.getIntOr("zPos", 0));
/*     */     
/* 138 */     if (isUnhandledStructureStart(pos.x, pos.z)) {
/* 139 */       tag = updateStructureStart(tag, pos);
/*     */     }
/*     */     
/* 142 */     CompoundTag structureTag = levelTag.getCompoundOrEmpty("Structures");
/* 143 */     CompoundTag referencesTag = structureTag.getCompoundOrEmpty("References");
/*     */     
/* 145 */     for (String key : this.currentKeys) {
/* 146 */       boolean featureExists = OLD_STRUCTURE_REGISTRY_KEYS.contains(key.toLowerCase(Locale.ROOT));
/*     */       
/* 148 */       if (referencesTag.getLongArray(key).isPresent() || !featureExists) {
/*     */         continue;
/*     */       }
/*     */       
/* 152 */       int lookupRange = 8;
/* 153 */       LongArrayList longArrayList = new LongArrayList();
/*     */       
/* 155 */       for (int sourceX = pos.x - 8; sourceX <= pos.x + 8; sourceX++) {
/* 156 */         for (int sourceZ = pos.z - 8; sourceZ <= pos.z + 8; sourceZ++) {
/* 157 */           if (hasLegacyStart(sourceX, sourceZ, key)) {
/* 158 */             longArrayList.add(ChunkPos.asLong(sourceX, sourceZ));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 163 */       referencesTag.putLongArray(key, longArrayList.toLongArray());
/*     */     } 
/*     */     
/* 166 */     structureTag.put("References", referencesTag);
/* 167 */     levelTag.put("Structures", structureTag);
/* 168 */     tag.put("Level", levelTag);
/*     */     
/* 170 */     return tag;
/*     */   }
/*     */   
/*     */   private boolean hasLegacyStart(int x, int z, String feature) {
/* 174 */     if (!this.hasLegacyData) {
/* 175 */       return false;
/*     */     }
/*     */     
/* 178 */     if (this.dataMap.get(feature) != null && ((StructureFeatureIndexSavedData)this.indexMap.get(CURRENT_TO_LEGACY_MAP.get(feature))).hasStartIndex(ChunkPos.asLong(x, z))) {
/* 179 */       return true;
/*     */     }
/*     */     
/* 182 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isUnhandledStructureStart(int x, int z) {
/* 186 */     if (!this.hasLegacyData) {
/* 187 */       return false;
/*     */     }
/*     */     
/* 190 */     for (String key : this.currentKeys) {
/* 191 */       if (this.dataMap.get(key) != null && ((StructureFeatureIndexSavedData)this.indexMap.get(CURRENT_TO_LEGACY_MAP.get(key))).hasUnhandledIndex(ChunkPos.asLong(x, z))) {
/* 192 */         return true;
/*     */       }
/*     */     } 
/* 195 */     return false;
/*     */   }
/*     */   
/*     */   private CompoundTag updateStructureStart(CompoundTag tag, ChunkPos pos) {
/* 199 */     CompoundTag levelTag = tag.getCompoundOrEmpty("Level");
/* 200 */     CompoundTag structureTag = levelTag.getCompoundOrEmpty("Structures");
/* 201 */     CompoundTag startTag = structureTag.getCompoundOrEmpty("Starts");
/*     */     
/* 203 */     for (String key : this.currentKeys) {
/* 204 */       Long2ObjectMap<CompoundTag> tagMap = (Long2ObjectMap)this.dataMap.get(key);
/* 205 */       if (tagMap == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 209 */       long longPos = pos.toLong();
/*     */       
/* 211 */       if (!((StructureFeatureIndexSavedData)this.indexMap.get(CURRENT_TO_LEGACY_MAP.get(key))).hasUnhandledIndex(longPos)) {
/*     */         continue;
/*     */       }
/*     */       
/* 215 */       CompoundTag featureTag = (CompoundTag)tagMap.get(longPos);
/* 216 */       if (featureTag == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 220 */       startTag.put(key, featureTag);
/*     */     } 
/*     */     
/* 223 */     structureTag.put("Starts", startTag);
/* 224 */     levelTag.put("Structures", structureTag);
/* 225 */     tag.put("Level", levelTag);
/*     */     
/* 227 */     return tag;
/*     */   }
/*     */   
/*     */   private void populateCaches(DimensionDataStorage dimensionDataStorage) {
/* 231 */     if (this.cachesInitialized) {
/*     */       return;
/*     */     }
/*     */     
/* 235 */     for (Iterator iterator = this.legacyKeys.iterator(); iterator.hasNext(); ) { String legacyKey = (String)iterator.next();
/* 236 */       CompoundTag legacyData = new CompoundTag();
/*     */       try {
/* 238 */         legacyData = dimensionDataStorage.readTagFromDisk(legacyKey, DataFixTypes.SAVED_DATA_STRUCTURE_FEATURE_INDICES, 1493).getCompoundOrEmpty("data").getCompoundOrEmpty("Features");
/* 239 */         if (legacyData.isEmpty()) {
/*     */           continue;
/*     */         }
/* 242 */       } catch (IOException iOException) {}
/*     */ 
/*     */       
/* 245 */       legacyData.forEach((tagKey, tag) -> {
/* 246 */             CompoundTag compoundTag; if (tag instanceof CompoundTag) { compoundTag = (CompoundTag)tag; }
/*     */             else
/*     */             { return; }
/* 249 */              long longPos = ChunkPos.asLong(compoundTag.getIntOr("ChunkX", 0), compoundTag.getIntOr("ChunkZ", 0));
/*     */ 
/*     */             
/* 252 */             ListTag childList = compoundTag.getListOrEmpty("Children");
/* 253 */             if (!childList.isEmpty()) {
/* 254 */               Optional<String> startId = childList.getCompound(0).flatMap(());
/* 255 */               Objects.requireNonNull(LEGACY_TO_CURRENT_MAP); startId.map(LEGACY_TO_CURRENT_MAP::get).ifPresent(());
/*     */             } 
/*     */             
/* 258 */             compoundTag.getString("id").ifPresent(());
/*     */           });
/*     */ 
/*     */ 
/*     */       
/* 263 */       String indexesID = legacyKey + "_index";
/* 264 */       StructureFeatureIndexSavedData legacyIndexes = (StructureFeatureIndexSavedData)dimensionDataStorage.computeIfAbsent(StructureFeatureIndexSavedData.type(indexesID));
/*     */       
/* 266 */       if (legacyIndexes.getAll().isEmpty()) {
/*     */         
/* 268 */         StructureFeatureIndexSavedData indexSaveData = new StructureFeatureIndexSavedData();
/* 269 */         this.indexMap.put(legacyKey, indexSaveData);
/* 270 */         legacyData.forEach((key, tag) -> {
/* 271 */               if (tag instanceof CompoundTag) { CompoundTag entryTag = (CompoundTag)tag;
/* 272 */                 indexSaveData.addIndex(ChunkPos.asLong(entryTag.getIntOr("ChunkX", 0), entryTag.getIntOr("ChunkZ", 0))); }
/*     */             
/*     */             }); continue;
/*     */       } 
/* 276 */       this.indexMap.put(legacyKey, legacyIndexes); }
/*     */ 
/*     */ 
/*     */     
/* 280 */     this.cachesInitialized = true;
/*     */   }
/*     */   
/*     */   public static Supplier<LegacyTagFixer> getLegacyTagFixer(ResourceKey<Level> dimension, Supplier<DimensionDataStorage> dimensionDataStorage, DataFixer dataFixer) {
/* 284 */     if (dimension == Level.OVERWORLD) {
/* 285 */       return () -> new LegacyStructureDataHandler((DimensionDataStorage)dimensionDataStorage
/* 286 */           .get(), 
/* 287 */           ImmutableList.of("Monument", "Stronghold", "Village", "Mineshaft", "Temple", "Mansion"), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 295 */           ImmutableList.of("Village", "Mineshaft", "Mansion", "Igloo", "Desert_Pyramid", "Jungle_Pyramid", "Swamp_Hut", "Stronghold", "Monument"), dataFixer);
/*     */     }
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
/* 308 */     if (dimension == Level.NETHER) {
/* 309 */       ImmutableList immutableList = ImmutableList.of("Fortress");
/* 310 */       return () -> new LegacyStructureDataHandler((DimensionDataStorage)dimensionDataStorage.get(), netherKeys, netherKeys, dataFixer);
/* 311 */     }  if (dimension == Level.END) {
/* 312 */       ImmutableList immutableList = ImmutableList.of("EndCity");
/* 313 */       return () -> new LegacyStructureDataHandler((DimensionDataStorage)dimensionDataStorage.get(), endKeys, endKeys, dataFixer);
/*     */     } 
/* 315 */     return LegacyTagFixer.EMPTY;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\LegacyStructureDataHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */