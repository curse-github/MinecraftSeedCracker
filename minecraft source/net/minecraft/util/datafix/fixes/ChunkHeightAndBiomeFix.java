/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntFunction;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.BitSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.util.Util;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public class ChunkHeightAndBiomeFix
/*     */   extends DataFix
/*     */ {
/*     */   public static final String DATAFIXER_CONTEXT_TAG = "__context";
/*     */   private static final String NAME = "ChunkHeightAndBiomeFix";
/*     */   private static final int OLD_SECTION_COUNT = 16;
/*     */   private static final int NEW_SECTION_COUNT = 24;
/*     */   private static final int NEW_MIN_SECTION_Y = -4;
/*     */   public static final int BLOCKS_PER_SECTION = 4096;
/*     */   private static final int LONGS_PER_SECTION = 64;
/*     */   private static final int HEIGHTMAP_BITS = 9;
/*     */   private static final long HEIGHTMAP_MASK = 511L;
/*     */   private static final int HEIGHTMAP_OFFSET = 64;
/*  54 */   private static final String[] HEIGHTMAP_TYPES = { "WORLD_SURFACE_WG", "WORLD_SURFACE", "WORLD_SURFACE_IGNORE_SNOW", "OCEAN_FLOOR_WG", "OCEAN_FLOOR", "MOTION_BLOCKING", "MOTION_BLOCKING_NO_LEAVES" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   private static final Set<String> STATUS_IS_OR_AFTER_SURFACE = Set.of("surface", "carvers", "liquid_carvers", "features", "light", "spawn", "heightmaps", "full");
/*  65 */   private static final Set<String> STATUS_IS_OR_AFTER_NOISE = Set.of("noise", "surface", "carvers", "liquid_carvers", "features", "light", "spawn", "heightmaps", "full");
/*     */   
/*  67 */   private static final Set<String> BLOCKS_BEFORE_FEATURE_STATUS = Set.of(new String[] { "minecraft:air", "minecraft:basalt", "minecraft:bedrock", "minecraft:blackstone", "minecraft:calcite", "minecraft:cave_air", "minecraft:coarse_dirt", "minecraft:crimson_nylium", "minecraft:dirt", "minecraft:end_stone", "minecraft:grass_block", "minecraft:gravel", "minecraft:ice", "minecraft:lava", "minecraft:mycelium", "minecraft:nether_wart_block", "minecraft:netherrack", "minecraft:orange_terracotta", "minecraft:packed_ice", "minecraft:podzol", "minecraft:powder_snow", "minecraft:red_sand", "minecraft:red_sandstone", "minecraft:sand", "minecraft:sandstone", "minecraft:snow_block", "minecraft:soul_sand", "minecraft:soul_soil", "minecraft:stone", "minecraft:terracotta", "minecraft:warped_nylium", "minecraft:warped_wart_block", "minecraft:water", "minecraft:white_terracotta" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int BIOME_CONTAINER_LAYER_SIZE = 16;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int BIOME_CONTAINER_SIZE = 64;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int BIOME_CONTAINER_TOP_LAYER_OFFSET = 1008;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String DEFAULT_BIOME = "minecraft:plains";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   private static final Int2ObjectMap<String> BIOMES_BY_ID = new Int2ObjectOpenHashMap();
/*     */   
/*     */   static  {
/* 112 */     BIOMES_BY_ID.put(0, "minecraft:ocean");
/* 113 */     BIOMES_BY_ID.put(1, "minecraft:plains");
/* 114 */     BIOMES_BY_ID.put(2, "minecraft:desert");
/* 115 */     BIOMES_BY_ID.put(3, "minecraft:mountains");
/* 116 */     BIOMES_BY_ID.put(4, "minecraft:forest");
/* 117 */     BIOMES_BY_ID.put(5, "minecraft:taiga");
/* 118 */     BIOMES_BY_ID.put(6, "minecraft:swamp");
/* 119 */     BIOMES_BY_ID.put(7, "minecraft:river");
/* 120 */     BIOMES_BY_ID.put(8, "minecraft:nether_wastes");
/* 121 */     BIOMES_BY_ID.put(9, "minecraft:the_end");
/* 122 */     BIOMES_BY_ID.put(10, "minecraft:frozen_ocean");
/* 123 */     BIOMES_BY_ID.put(11, "minecraft:frozen_river");
/* 124 */     BIOMES_BY_ID.put(12, "minecraft:snowy_tundra");
/* 125 */     BIOMES_BY_ID.put(13, "minecraft:snowy_mountains");
/* 126 */     BIOMES_BY_ID.put(14, "minecraft:mushroom_fields");
/* 127 */     BIOMES_BY_ID.put(15, "minecraft:mushroom_field_shore");
/* 128 */     BIOMES_BY_ID.put(16, "minecraft:beach");
/* 129 */     BIOMES_BY_ID.put(17, "minecraft:desert_hills");
/* 130 */     BIOMES_BY_ID.put(18, "minecraft:wooded_hills");
/* 131 */     BIOMES_BY_ID.put(19, "minecraft:taiga_hills");
/* 132 */     BIOMES_BY_ID.put(20, "minecraft:mountain_edge");
/* 133 */     BIOMES_BY_ID.put(21, "minecraft:jungle");
/* 134 */     BIOMES_BY_ID.put(22, "minecraft:jungle_hills");
/* 135 */     BIOMES_BY_ID.put(23, "minecraft:jungle_edge");
/* 136 */     BIOMES_BY_ID.put(24, "minecraft:deep_ocean");
/* 137 */     BIOMES_BY_ID.put(25, "minecraft:stone_shore");
/* 138 */     BIOMES_BY_ID.put(26, "minecraft:snowy_beach");
/* 139 */     BIOMES_BY_ID.put(27, "minecraft:birch_forest");
/* 140 */     BIOMES_BY_ID.put(28, "minecraft:birch_forest_hills");
/* 141 */     BIOMES_BY_ID.put(29, "minecraft:dark_forest");
/* 142 */     BIOMES_BY_ID.put(30, "minecraft:snowy_taiga");
/* 143 */     BIOMES_BY_ID.put(31, "minecraft:snowy_taiga_hills");
/* 144 */     BIOMES_BY_ID.put(32, "minecraft:giant_tree_taiga");
/* 145 */     BIOMES_BY_ID.put(33, "minecraft:giant_tree_taiga_hills");
/* 146 */     BIOMES_BY_ID.put(34, "minecraft:wooded_mountains");
/* 147 */     BIOMES_BY_ID.put(35, "minecraft:savanna");
/* 148 */     BIOMES_BY_ID.put(36, "minecraft:savanna_plateau");
/* 149 */     BIOMES_BY_ID.put(37, "minecraft:badlands");
/* 150 */     BIOMES_BY_ID.put(38, "minecraft:wooded_badlands_plateau");
/* 151 */     BIOMES_BY_ID.put(39, "minecraft:badlands_plateau");
/* 152 */     BIOMES_BY_ID.put(40, "minecraft:small_end_islands");
/* 153 */     BIOMES_BY_ID.put(41, "minecraft:end_midlands");
/* 154 */     BIOMES_BY_ID.put(42, "minecraft:end_highlands");
/* 155 */     BIOMES_BY_ID.put(43, "minecraft:end_barrens");
/* 156 */     BIOMES_BY_ID.put(44, "minecraft:warm_ocean");
/* 157 */     BIOMES_BY_ID.put(45, "minecraft:lukewarm_ocean");
/* 158 */     BIOMES_BY_ID.put(46, "minecraft:cold_ocean");
/* 159 */     BIOMES_BY_ID.put(47, "minecraft:deep_warm_ocean");
/* 160 */     BIOMES_BY_ID.put(48, "minecraft:deep_lukewarm_ocean");
/* 161 */     BIOMES_BY_ID.put(49, "minecraft:deep_cold_ocean");
/* 162 */     BIOMES_BY_ID.put(50, "minecraft:deep_frozen_ocean");
/* 163 */     BIOMES_BY_ID.put(127, "minecraft:the_void");
/* 164 */     BIOMES_BY_ID.put(129, "minecraft:sunflower_plains");
/* 165 */     BIOMES_BY_ID.put(130, "minecraft:desert_lakes");
/* 166 */     BIOMES_BY_ID.put(131, "minecraft:gravelly_mountains");
/* 167 */     BIOMES_BY_ID.put(132, "minecraft:flower_forest");
/* 168 */     BIOMES_BY_ID.put(133, "minecraft:taiga_mountains");
/* 169 */     BIOMES_BY_ID.put(134, "minecraft:swamp_hills");
/* 170 */     BIOMES_BY_ID.put(140, "minecraft:ice_spikes");
/* 171 */     BIOMES_BY_ID.put(149, "minecraft:modified_jungle");
/* 172 */     BIOMES_BY_ID.put(151, "minecraft:modified_jungle_edge");
/* 173 */     BIOMES_BY_ID.put(155, "minecraft:tall_birch_forest");
/* 174 */     BIOMES_BY_ID.put(156, "minecraft:tall_birch_hills");
/* 175 */     BIOMES_BY_ID.put(157, "minecraft:dark_forest_hills");
/* 176 */     BIOMES_BY_ID.put(158, "minecraft:snowy_taiga_mountains");
/* 177 */     BIOMES_BY_ID.put(160, "minecraft:giant_spruce_taiga");
/* 178 */     BIOMES_BY_ID.put(161, "minecraft:giant_spruce_taiga_hills");
/* 179 */     BIOMES_BY_ID.put(162, "minecraft:modified_gravelly_mountains");
/* 180 */     BIOMES_BY_ID.put(163, "minecraft:shattered_savanna");
/* 181 */     BIOMES_BY_ID.put(164, "minecraft:shattered_savanna_plateau");
/* 182 */     BIOMES_BY_ID.put(165, "minecraft:eroded_badlands");
/* 183 */     BIOMES_BY_ID.put(166, "minecraft:modified_wooded_badlands_plateau");
/* 184 */     BIOMES_BY_ID.put(167, "minecraft:modified_badlands_plateau");
/* 185 */     BIOMES_BY_ID.put(168, "minecraft:bamboo_jungle");
/* 186 */     BIOMES_BY_ID.put(169, "minecraft:bamboo_jungle_hills");
/* 187 */     BIOMES_BY_ID.put(170, "minecraft:soul_sand_valley");
/* 188 */     BIOMES_BY_ID.put(171, "minecraft:crimson_forest");
/* 189 */     BIOMES_BY_ID.put(172, "minecraft:warped_forest");
/* 190 */     BIOMES_BY_ID.put(173, "minecraft:basalt_deltas");
/* 191 */     BIOMES_BY_ID.put(174, "minecraft:dripstone_caves");
/* 192 */     BIOMES_BY_ID.put(175, "minecraft:lush_caves");
/* 193 */     BIOMES_BY_ID.put(177, "minecraft:meadow");
/* 194 */     BIOMES_BY_ID.put(178, "minecraft:grove");
/* 195 */     BIOMES_BY_ID.put(179, "minecraft:snowy_slopes");
/* 196 */     BIOMES_BY_ID.put(180, "minecraft:snowcapped_peaks");
/* 197 */     BIOMES_BY_ID.put(181, "minecraft:lofty_peaks");
/* 198 */     BIOMES_BY_ID.put(182, "minecraft:stony_peaks");
/*     */   }
/*     */ 
/*     */   
/* 202 */   public ChunkHeightAndBiomeFix(Schema outputSchema) { super(outputSchema, true); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/* 207 */     Type<?> oldChunkType = getInputSchema().getType(References.CHUNK);
/* 208 */     OpticFinder<?> levelFinder = oldChunkType.findField("Level");
/* 209 */     OpticFinder<?> sectionsFinder = levelFinder.type().findField("Sections");
/*     */     
/* 211 */     Schema outputSchema = getOutputSchema();
/* 212 */     Type<?> chunkType = outputSchema.getType(References.CHUNK);
/* 213 */     Type<?> levelType = chunkType.findField("Level").type();
/* 214 */     Type<?> sectionsType = levelType.findField("Sections").type();
/*     */     
/* 216 */     return fixTypeEverywhereTyped("ChunkHeightAndBiomeFix", oldChunkType, chunkType, chunk -> 
/* 217 */         chunk.updateTyped(levelFinder, levelType, ()));
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
/*     */   private Dynamic<?> predictChunkStatusBeforeSurface(Dynamic<?> chunkTag, Set<String> blocksInChunk) {
/* 291 */     return chunkTag.update("Status", statusDynamic -> {
/* 292 */           String status = statusDynamic.asString("empty");
/*     */           
/* 294 */           if (STATUS_IS_OR_AFTER_SURFACE.contains(status)) {
/* 295 */             return statusDynamic;
/*     */           }
/*     */           
/* 298 */           blocksInChunk.remove("minecraft:air");
/* 299 */           boolean hasNonAirBlocks = !blocksInChunk.isEmpty();
/* 300 */           blocksInChunk.removeAll(BLOCKS_BEFORE_FEATURE_STATUS);
/* 301 */           boolean hasFeatureBlocks = !blocksInChunk.isEmpty();
/*     */ 
/*     */ 
/*     */           
/* 305 */           if (hasFeatureBlocks) {
/* 306 */             return statusDynamic.createString("liquid_carvers");
/*     */           }
/*     */ 
/*     */           
/* 310 */           if ("noise".equals(status) || hasNonAirBlocks) {
/* 311 */             return statusDynamic.createString("noise");
/*     */           }
/*     */           
/* 314 */           if ("biomes".equals(status))
/*     */           {
/* 316 */             return statusDynamic.createString("structure_references");
/*     */           }
/*     */           
/* 319 */           return statusDynamic;
/*     */         });
/*     */   }
/*     */   
/*     */   private static Dynamic<?>[] getBiomeContainers(Dynamic<?> tag, boolean increaseHeight, int minSection, MutableBoolean wasIncreasedHeight) {
/* 324 */     Dynamic[] biomeContainers = new Dynamic[increaseHeight ? 24 : 16];
/*     */     
/* 326 */     int[] oldBiomes = (int[])tag.get("Biomes").asIntStreamOpt().result().map(IntStream::toArray).orElse(null);
/* 327 */     if (oldBiomes != null && oldBiomes.length == 1536) {
/* 328 */       wasIncreasedHeight.setValue(true);
/*     */       
/* 330 */       for (int sectionYIndex = 0; sectionYIndex < 24; sectionYIndex++) {
/* 331 */         int finalSectionYIndex = sectionYIndex;
/* 332 */         biomeContainers[sectionYIndex] = makeBiomeContainer(tag, i -> getOldBiome(oldBiomes, finalSectionYIndex * 64 + i));
/*     */       } 
/* 334 */     } else if (oldBiomes != null && oldBiomes.length == 1024) {
/* 335 */       for (int sectionY = 0; sectionY < 16; sectionY++) {
/* 336 */         int sectionYIndex = sectionY - minSection;
/* 337 */         int finalSectionY = sectionY;
/* 338 */         biomeContainers[sectionYIndex] = makeBiomeContainer(tag, i -> getOldBiome(oldBiomes, finalSectionY * 64 + i));
/*     */       } 
/* 340 */       if (increaseHeight) {
/* 341 */         Dynamic<?> belowWorldBiomes = makeBiomeContainer(tag, i -> getOldBiome(oldBiomes, i % 16));
/* 342 */         Dynamic<?> aboveWorldBiomes = makeBiomeContainer(tag, i -> getOldBiome(oldBiomes, i % 16 + 1008));
/* 343 */         for (int i = 0; i < 4; i++) {
/* 344 */           biomeContainers[i] = belowWorldBiomes;
/*     */         }
/* 346 */         for (int i = 20; i < 24; i++) {
/* 347 */           biomeContainers[i] = aboveWorldBiomes;
/*     */         }
/*     */       } 
/*     */     } else {
/* 351 */       Arrays.fill(biomeContainers, makePalettedContainer(tag.createList(Stream.of(tag.createString("minecraft:plains")))));
/*     */     } 
/* 353 */     return biomeContainers;
/*     */   }
/*     */ 
/*     */   
/* 357 */   private static int getOldBiome(int[] oldBiomes, int index) { return oldBiomes[index] & 0xFF; }
/*     */ 
/*     */   
/*     */   private static Dynamic<?> updateChunkTag(Dynamic<?> chunkTag, boolean isOverworld, boolean wasIncreasedHeight, boolean needsBlendingAndUpgrade, Supplier<ChunkProtoTickListFix.PoorMansPalettedContainer> bedrockSectionBlocks) {
/* 361 */     chunkTag = chunkTag.remove("Biomes");
/*     */     
/* 363 */     if (!isOverworld) {
/* 364 */       return updateCarvingMasks(chunkTag, 16, 0);
/*     */     }
/*     */     
/* 367 */     if (wasIncreasedHeight) {
/* 368 */       return updateCarvingMasks(chunkTag, 24, 0);
/*     */     }
/*     */     
/* 371 */     chunkTag = updateHeightmaps(chunkTag);
/* 372 */     chunkTag = addPaddingEntries(chunkTag, "LiquidsToBeTicked");
/* 373 */     chunkTag = addPaddingEntries(chunkTag, "PostProcessing");
/* 374 */     chunkTag = addPaddingEntries(chunkTag, "ToBeTicked");
/* 375 */     chunkTag = updateCarvingMasks(chunkTag, 24, 4);
/* 376 */     chunkTag = chunkTag.update("UpgradeData", ChunkHeightAndBiomeFix::shiftUpgradeData);
/*     */     
/* 378 */     if (!needsBlendingAndUpgrade) {
/* 379 */       return chunkTag;
/*     */     }
/*     */     
/* 382 */     Optional<? extends Dynamic<?>> statusOpt = chunkTag.get("Status").result();
/* 383 */     if (statusOpt.isPresent()) {
/* 384 */       Dynamic<?> status = (Dynamic)statusOpt.get();
/* 385 */       String lastStatus = status.asString("");
/* 386 */       if (!"empty".equals(lastStatus)) {
/* 387 */         chunkTag = chunkTag.set("blending_data", chunkTag.createMap(ImmutableMap.of(chunkTag
/* 388 */                 .createString("old_noise"), chunkTag.createBoolean(STATUS_IS_OR_AFTER_NOISE.contains(lastStatus)))));
/*     */ 
/*     */         
/* 391 */         if (!SharedConstants.DEBUG_DISABLE_BELOW_ZERO_RETROGENERATION) {
/* 392 */           ChunkProtoTickListFix.PoorMansPalettedContainer poorMansPalettedContainer = (ChunkProtoTickListFix.PoorMansPalettedContainer)bedrockSectionBlocks.get();
/* 393 */           if (poorMansPalettedContainer != null) {
/* 394 */             BitSet missingBedrock = new BitSet(256);
/* 395 */             boolean hasAnyBedrock = lastStatus.equals("noise");
/* 396 */             for (int z = 0; z < 16; z++) {
/* 397 */               for (int x = 0; x < 16; x++) {
/* 398 */                 Dynamic<?> blockState = poorMansPalettedContainer.get(x, 0, z);
/* 399 */                 boolean isBedrock = (blockState != null && "minecraft:bedrock".equals(blockState.get("Name").asString("")));
/* 400 */                 boolean isAir = (blockState != null && "minecraft:air".equals(blockState.get("Name").asString("")));
/* 401 */                 if (isAir) {
/* 402 */                   missingBedrock.set(z * 16 + x);
/*     */                 }
/* 404 */                 hasAnyBedrock |= isBedrock;
/*     */               } 
/*     */             } 
/*     */             
/* 408 */             if (hasAnyBedrock && missingBedrock.cardinality() != missingBedrock.size()) {
/*     */               
/* 410 */               Dynamic<?> targetStatus = "full".equals(lastStatus) ? chunkTag.createString("heightmaps") : status;
/*     */               
/* 412 */               chunkTag = chunkTag.set("below_zero_retrogen", chunkTag.createMap(ImmutableMap.of(chunkTag
/* 413 */                       .createString("target_status"), targetStatus, chunkTag
/* 414 */                       .createString("missing_bedrock"), chunkTag.createLongList(LongStream.of(missingBedrock.toLongArray())))));
/*     */ 
/*     */               
/* 417 */               chunkTag = chunkTag.set("Status", chunkTag.createString("empty"));
/*     */             } 
/* 419 */             chunkTag = chunkTag.set("isLightOn", chunkTag.createBoolean(false));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 424 */     return chunkTag;
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> shiftUpgradeData(Dynamic<T> upgradeData) {
/* 428 */     return upgradeData.update("Indices", indices -> {
/* 429 */           Map<Dynamic<?>, Dynamic<?>> shiftedIndices = new HashMap<Dynamic<?>, Dynamic<?>>();
/*     */           
/* 431 */           indices.getMapValues().ifSuccess(());
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
/* 444 */           return indices.createMap(shiftedIndices);
/*     */         });
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateCarvingMasks(Dynamic<?> chunkTag, int sectionCount, int addedSectionsBelow) {
/* 449 */     Dynamic<?> carvingMasks = chunkTag.get("CarvingMasks").orElseEmptyMap();
/* 450 */     carvingMasks = carvingMasks.updateMapValues(pair -> {
/* 451 */           long[] oldValues = BitSet.valueOf(((Dynamic)pair.getSecond()).asByteBuffer().array()).toLongArray();
/* 452 */           long[] newValues = new long[64 * sectionCount];
/*     */           
/* 454 */           System.arraycopy(oldValues, 0, newValues, 64 * addedSectionsBelow, oldValues.length);
/*     */           
/* 456 */           return Pair.of((Dynamic)pair.getFirst(), chunkTag.createLongList(LongStream.of(newValues)));
/*     */         });
/*     */     
/* 459 */     return chunkTag.set("CarvingMasks", carvingMasks);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> addPaddingEntries(Dynamic<?> chunkTag, String key) {
/* 463 */     List<Dynamic<?>> list = (List)chunkTag.get(key).orElseEmptyList().asStream().collect(Collectors.toCollection(java.util.ArrayList::new));
/* 464 */     if (list.size() == 24)
/*     */     {
/* 466 */       return chunkTag;
/*     */     }
/* 468 */     Dynamic<?> emptyList = chunkTag.emptyList();
/* 469 */     for (int i = 0; i < 4; i++) {
/* 470 */       list.add(0, emptyList);
/* 471 */       list.add(emptyList);
/*     */     } 
/* 473 */     return chunkTag.set(key, chunkTag.createList(list.stream()));
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateHeightmaps(Dynamic<?> chunkTag) {
/* 477 */     return chunkTag.update("Heightmaps", heightmapTag -> {
/* 478 */           for (String heightmapType : HEIGHTMAP_TYPES) {
/* 479 */             heightmapTag = heightmapTag.update(heightmapType, ChunkHeightAndBiomeFix::getFixedHeightmap);
/*     */           }
/* 481 */           return heightmapTag;
/*     */         });
/*     */   }
/*     */   
/*     */   private static Dynamic<?> getFixedHeightmap(Dynamic<?> tag) {
/* 486 */     return tag.createLongList(tag.asLongStream().map(value -> {
/* 487 */             long newValue = 0L;
/* 488 */             int bitIndex = 0;
/* 489 */             while (bitIndex + 9 <= 64) {
/* 490 */               long newHeight, oldHeight = value >> bitIndex & 0x1FFL;
/*     */               
/* 492 */               if (oldHeight == 0L) {
/*     */                 
/* 494 */                 newHeight = 0L;
/*     */               } else {
/* 496 */                 newHeight = Math.min(oldHeight + 64L, 511L);
/*     */               } 
/* 498 */               newValue |= newHeight << bitIndex;
/* 499 */               bitIndex += 9;
/*     */             } 
/* 501 */             return newValue;
/*     */           }));
/*     */   }
/*     */   
/*     */   private static Dynamic<?> makeBiomeContainer(Dynamic<?> tag, Int2IntFunction sourceStorage) {
/* 506 */     Int2IntLinkedOpenHashMap int2IntLinkedOpenHashMap = new Int2IntLinkedOpenHashMap();
/* 507 */     for (int i = 0; i < 64; i++) {
/* 508 */       int biomeId = sourceStorage.applyAsInt(i);
/* 509 */       if (!int2IntLinkedOpenHashMap.containsKey(biomeId)) {
/* 510 */         int2IntLinkedOpenHashMap.put(biomeId, int2IntLinkedOpenHashMap.size());
/*     */       }
/*     */     } 
/* 513 */     Dynamic<?> palette = tag.createList(int2IntLinkedOpenHashMap.keySet().stream().map(biomeId1 -> tag.createString((String)BIOMES_BY_ID.getOrDefault(biomeId1.intValue(), "minecraft:plains"))));
/*     */     
/* 515 */     int bits = ceillog2(int2IntLinkedOpenHashMap.size());
/* 516 */     if (bits == 0) {
/* 517 */       return makePalettedContainer(palette);
/*     */     }
/*     */     
/* 520 */     int valuesPerLong = 64 / bits;
/* 521 */     int requiredLength = (64 + valuesPerLong - 1) / valuesPerLong;
/* 522 */     long[] bitStorage = new long[requiredLength];
/* 523 */     int cellIndex = 0;
/* 524 */     int bitIndex = 0;
/* 525 */     for (int i = 0; i < 64; i++) {
/* 526 */       int biomeId = sourceStorage.applyAsInt(i);
/* 527 */       bitStorage[cellIndex] = bitStorage[cellIndex] | int2IntLinkedOpenHashMap.get(biomeId) << bitIndex;
/* 528 */       bitIndex += bits;
/* 529 */       if (bitIndex + bits > 64) {
/* 530 */         cellIndex++;
/* 531 */         bitIndex = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 535 */     Dynamic<?> storage = tag.createLongList(Arrays.stream(bitStorage));
/* 536 */     return makePalettedContainer(palette, storage);
/*     */   }
/*     */ 
/*     */   
/* 540 */   private static Dynamic<?> makePalettedContainer(Dynamic<?> palette) { return palette.createMap(ImmutableMap.of(palette.createString("palette"), palette)); }
/*     */ 
/*     */ 
/*     */   
/* 544 */   private static Dynamic<?> makePalettedContainer(Dynamic<?> palette, Dynamic<?> storage) { return palette.createMap(ImmutableMap.of(palette.createString("palette"), palette, palette.createString("data"), storage)); }
/*     */ 
/*     */   
/*     */   private static Dynamic<?> makeOptimizedPalettedContainer(Dynamic<?> palette, Dynamic<?> data) {
/* 548 */     List<Dynamic<?>> paletteList = (List)palette.asStream().collect(Collectors.toCollection(java.util.ArrayList::new));
/* 549 */     if (paletteList.size() == 1) {
/* 550 */       return makePalettedContainer(palette);
/*     */     }
/* 552 */     palette = padPaletteEntries(palette, data, paletteList);
/* 553 */     return makePalettedContainer(palette, data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dynamic<?> padPaletteEntries(Dynamic<?> palette, Dynamic<?> data, List<Dynamic<?>> paletteList) {
/* 559 */     long dataSizeInBits = data.asLongStream().count() * 64L;
/* 560 */     long estimatedBitsPerBlock = dataSizeInBits / 4096L;
/*     */     
/* 562 */     int paletteSize = paletteList.size();
/* 563 */     int expectedBitsPerBlock = ceillog2(paletteSize);
/*     */     
/* 565 */     if (estimatedBitsPerBlock > expectedBitsPerBlock) {
/* 566 */       Dynamic<?> airPalleteEntry = palette.createMap(ImmutableMap.of(palette.createString("Name"), palette.createString("minecraft:air")));
/*     */       
/* 568 */       int minimumPaletteSizeToMatchData = (1 << (int)(estimatedBitsPerBlock - 1L)) + 1;
/* 569 */       int additionalPaletteEntries = minimumPaletteSizeToMatchData - paletteSize;
/* 570 */       for (int i = 0; i < additionalPaletteEntries; i++) {
/* 571 */         paletteList.add(airPalleteEntry);
/*     */       }
/* 573 */       return palette.createList(paletteList.stream());
/*     */     } 
/* 575 */     return palette;
/*     */   }
/*     */   
/*     */   public static int ceillog2(int input) {
/* 579 */     if (input == 0) {
/* 580 */       return 0;
/*     */     }
/*     */     
/* 583 */     return (int)Math.ceil(Math.log(input) / Math.log(2.0D));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkHeightAndBiomeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */