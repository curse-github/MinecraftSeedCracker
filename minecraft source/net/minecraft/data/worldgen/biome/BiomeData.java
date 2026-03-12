/*     */ package net.minecraft.data.worldgen.biome;
/*     */ 
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ 
/*     */ public abstract class BiomeData {
/*     */   public static void bootstrap(BootstrapContext<Biome> context) {
/*  13 */     HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
/*  14 */     HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);
/*     */     
/*  16 */     context.register(Biomes.THE_VOID, OverworldBiomes.theVoid(placedFeatures, carvers));
/*     */     
/*  18 */     context.register(Biomes.PLAINS, OverworldBiomes.plains(placedFeatures, carvers, false, false, false));
/*  19 */     context.register(Biomes.SUNFLOWER_PLAINS, OverworldBiomes.plains(placedFeatures, carvers, true, false, false));
/*     */     
/*  21 */     context.register(Biomes.SNOWY_PLAINS, OverworldBiomes.plains(placedFeatures, carvers, false, true, false));
/*  22 */     context.register(Biomes.ICE_SPIKES, OverworldBiomes.plains(placedFeatures, carvers, false, true, true));
/*     */     
/*  24 */     context.register(Biomes.DESERT, OverworldBiomes.desert(placedFeatures, carvers));
/*     */     
/*  26 */     context.register(Biomes.SWAMP, OverworldBiomes.swamp(placedFeatures, carvers));
/*  27 */     context.register(Biomes.MANGROVE_SWAMP, OverworldBiomes.mangroveSwamp(placedFeatures, carvers));
/*     */     
/*  29 */     context.register(Biomes.FOREST, OverworldBiomes.forest(placedFeatures, carvers, false, false, false));
/*  30 */     context.register(Biomes.FLOWER_FOREST, OverworldBiomes.forest(placedFeatures, carvers, false, false, true));
/*  31 */     context.register(Biomes.BIRCH_FOREST, OverworldBiomes.forest(placedFeatures, carvers, true, false, false));
/*     */     
/*  33 */     context.register(Biomes.DARK_FOREST, OverworldBiomes.darkForest(placedFeatures, carvers, false));
/*  34 */     context.register(Biomes.PALE_GARDEN, OverworldBiomes.darkForest(placedFeatures, carvers, true));
/*  35 */     context.register(Biomes.OLD_GROWTH_BIRCH_FOREST, OverworldBiomes.forest(placedFeatures, carvers, true, true, false));
/*  36 */     context.register(Biomes.OLD_GROWTH_PINE_TAIGA, OverworldBiomes.oldGrowthTaiga(placedFeatures, carvers, false));
/*  37 */     context.register(Biomes.OLD_GROWTH_SPRUCE_TAIGA, OverworldBiomes.oldGrowthTaiga(placedFeatures, carvers, true));
/*     */     
/*  39 */     context.register(Biomes.TAIGA, OverworldBiomes.taiga(placedFeatures, carvers, false));
/*  40 */     context.register(Biomes.SNOWY_TAIGA, OverworldBiomes.taiga(placedFeatures, carvers, true));
/*     */     
/*  42 */     context.register(Biomes.SAVANNA, OverworldBiomes.savanna(placedFeatures, carvers, false, false));
/*  43 */     context.register(Biomes.SAVANNA_PLATEAU, OverworldBiomes.savanna(placedFeatures, carvers, false, true));
/*     */     
/*  45 */     context.register(Biomes.WINDSWEPT_HILLS, OverworldBiomes.windsweptHills(placedFeatures, carvers, false));
/*  46 */     context.register(Biomes.WINDSWEPT_GRAVELLY_HILLS, OverworldBiomes.windsweptHills(placedFeatures, carvers, false));
/*  47 */     context.register(Biomes.WINDSWEPT_FOREST, OverworldBiomes.windsweptHills(placedFeatures, carvers, true));
/*  48 */     context.register(Biomes.WINDSWEPT_SAVANNA, OverworldBiomes.savanna(placedFeatures, carvers, true, false));
/*     */     
/*  50 */     context.register(Biomes.JUNGLE, OverworldBiomes.jungle(placedFeatures, carvers));
/*  51 */     context.register(Biomes.SPARSE_JUNGLE, OverworldBiomes.sparseJungle(placedFeatures, carvers));
/*  52 */     context.register(Biomes.BAMBOO_JUNGLE, OverworldBiomes.bambooJungle(placedFeatures, carvers));
/*     */     
/*  54 */     context.register(Biomes.BADLANDS, OverworldBiomes.badlands(placedFeatures, carvers, false));
/*  55 */     context.register(Biomes.ERODED_BADLANDS, OverworldBiomes.badlands(placedFeatures, carvers, false));
/*  56 */     context.register(Biomes.WOODED_BADLANDS, OverworldBiomes.badlands(placedFeatures, carvers, true));
/*     */     
/*  58 */     context.register(Biomes.MEADOW, OverworldBiomes.meadowOrCherryGrove(placedFeatures, carvers, false));
/*  59 */     context.register(Biomes.CHERRY_GROVE, OverworldBiomes.meadowOrCherryGrove(placedFeatures, carvers, true));
/*  60 */     context.register(Biomes.GROVE, OverworldBiomes.grove(placedFeatures, carvers));
/*     */     
/*  62 */     context.register(Biomes.SNOWY_SLOPES, OverworldBiomes.snowySlopes(placedFeatures, carvers));
/*  63 */     context.register(Biomes.FROZEN_PEAKS, OverworldBiomes.frozenPeaks(placedFeatures, carvers));
/*  64 */     context.register(Biomes.JAGGED_PEAKS, OverworldBiomes.jaggedPeaks(placedFeatures, carvers));
/*  65 */     context.register(Biomes.STONY_PEAKS, OverworldBiomes.stonyPeaks(placedFeatures, carvers));
/*     */     
/*  67 */     context.register(Biomes.RIVER, OverworldBiomes.river(placedFeatures, carvers, false));
/*  68 */     context.register(Biomes.FROZEN_RIVER, OverworldBiomes.river(placedFeatures, carvers, true));
/*     */     
/*  70 */     context.register(Biomes.BEACH, OverworldBiomes.beach(placedFeatures, carvers, false, false));
/*  71 */     context.register(Biomes.SNOWY_BEACH, OverworldBiomes.beach(placedFeatures, carvers, true, false));
/*  72 */     context.register(Biomes.STONY_SHORE, OverworldBiomes.beach(placedFeatures, carvers, false, true));
/*     */     
/*  74 */     context.register(Biomes.WARM_OCEAN, OverworldBiomes.warmOcean(placedFeatures, carvers));
/*  75 */     context.register(Biomes.LUKEWARM_OCEAN, OverworldBiomes.lukeWarmOcean(placedFeatures, carvers, false));
/*  76 */     context.register(Biomes.DEEP_LUKEWARM_OCEAN, OverworldBiomes.lukeWarmOcean(placedFeatures, carvers, true));
/*  77 */     context.register(Biomes.OCEAN, OverworldBiomes.ocean(placedFeatures, carvers, false));
/*  78 */     context.register(Biomes.DEEP_OCEAN, OverworldBiomes.ocean(placedFeatures, carvers, true));
/*  79 */     context.register(Biomes.COLD_OCEAN, OverworldBiomes.coldOcean(placedFeatures, carvers, false));
/*  80 */     context.register(Biomes.DEEP_COLD_OCEAN, OverworldBiomes.coldOcean(placedFeatures, carvers, true));
/*  81 */     context.register(Biomes.FROZEN_OCEAN, OverworldBiomes.frozenOcean(placedFeatures, carvers, false));
/*  82 */     context.register(Biomes.DEEP_FROZEN_OCEAN, OverworldBiomes.frozenOcean(placedFeatures, carvers, true));
/*     */     
/*  84 */     context.register(Biomes.MUSHROOM_FIELDS, OverworldBiomes.mushroomFields(placedFeatures, carvers));
/*     */     
/*  86 */     context.register(Biomes.DRIPSTONE_CAVES, OverworldBiomes.dripstoneCaves(placedFeatures, carvers));
/*  87 */     context.register(Biomes.LUSH_CAVES, OverworldBiomes.lushCaves(placedFeatures, carvers));
/*  88 */     context.register(Biomes.DEEP_DARK, OverworldBiomes.deepDark(placedFeatures, carvers));
/*     */     
/*  90 */     context.register(Biomes.NETHER_WASTES, NetherBiomes.netherWastes(placedFeatures, carvers));
/*  91 */     context.register(Biomes.WARPED_FOREST, NetherBiomes.warpedForest(placedFeatures, carvers));
/*  92 */     context.register(Biomes.CRIMSON_FOREST, NetherBiomes.crimsonForest(placedFeatures, carvers));
/*  93 */     context.register(Biomes.SOUL_SAND_VALLEY, NetherBiomes.soulSandValley(placedFeatures, carvers));
/*  94 */     context.register(Biomes.BASALT_DELTAS, NetherBiomes.basaltDeltas(placedFeatures, carvers));
/*     */     
/*  96 */     context.register(Biomes.THE_END, EndBiomes.theEnd(placedFeatures, carvers));
/*  97 */     context.register(Biomes.END_HIGHLANDS, EndBiomes.endHighlands(placedFeatures, carvers));
/*  98 */     context.register(Biomes.END_MIDLANDS, EndBiomes.endMidlands(placedFeatures, carvers));
/*  99 */     context.register(Biomes.SMALL_END_ISLANDS, EndBiomes.smallEndIslands(placedFeatures, carvers));
/* 100 */     context.register(Biomes.END_BARRENS, EndBiomes.endBarrens(placedFeatures, carvers));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\biome\BiomeData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */