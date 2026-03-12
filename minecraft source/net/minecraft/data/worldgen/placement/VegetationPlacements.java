/*     */ package net.minecraft.data.worldgen.placement;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.features.TreeFeatures;
/*     */ import net.minecraft.data.worldgen.features.VegetationFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.valueproviders.ClampedInt;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.NoiseBasedCountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.NoiseThresholdCountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ import net.minecraft.world.level.levelgen.placement.RarityFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
/*     */ 
/*     */ 
/*     */ public class VegetationPlacements
/*     */ {
/*  35 */   public static final ResourceKey<PlacedFeature> BAMBOO_LIGHT = PlacementUtils.createKey("bamboo_light");
/*  36 */   public static final ResourceKey<PlacedFeature> BAMBOO = PlacementUtils.createKey("bamboo");
/*  37 */   public static final ResourceKey<PlacedFeature> VINES = PlacementUtils.createKey("vines");
/*     */   
/*  39 */   public static final ResourceKey<PlacedFeature> PATCH_SUNFLOWER = PlacementUtils.createKey("patch_sunflower");
/*  40 */   public static final ResourceKey<PlacedFeature> PATCH_PUMPKIN = PlacementUtils.createKey("patch_pumpkin");
/*     */   
/*  42 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_PLAIN = PlacementUtils.createKey("patch_grass_plain");
/*  43 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_MEADOW = PlacementUtils.createKey("patch_grass_meadow");
/*  44 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_FOREST = PlacementUtils.createKey("patch_grass_forest");
/*  45 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_BADLANDS = PlacementUtils.createKey("patch_grass_badlands");
/*  46 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_SAVANNA = PlacementUtils.createKey("patch_grass_savanna");
/*  47 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_NORMAL = PlacementUtils.createKey("patch_grass_normal");
/*  48 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_TAIGA_2 = PlacementUtils.createKey("patch_grass_taiga_2");
/*  49 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_TAIGA = PlacementUtils.createKey("patch_grass_taiga");
/*  50 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_JUNGLE = PlacementUtils.createKey("patch_grass_jungle");
/*     */   
/*  52 */   public static final ResourceKey<PlacedFeature> GRASS_BONEMEAL = PlacementUtils.createKey("grass_bonemeal");
/*     */   
/*  54 */   public static final ResourceKey<PlacedFeature> PATCH_DEAD_BUSH_2 = PlacementUtils.createKey("patch_dead_bush_2");
/*  55 */   public static final ResourceKey<PlacedFeature> PATCH_DEAD_BUSH = PlacementUtils.createKey("patch_dead_bush");
/*  56 */   public static final ResourceKey<PlacedFeature> PATCH_DEAD_BUSH_BADLANDS = PlacementUtils.createKey("patch_dead_bush_badlands");
/*  57 */   public static final ResourceKey<PlacedFeature> PATCH_DRY_GRASS_BADLANDS = PlacementUtils.createKey("patch_dry_grass_badlands");
/*  58 */   public static final ResourceKey<PlacedFeature> PATCH_DRY_GRASS_DESERT = PlacementUtils.createKey("patch_dry_grass_desert");
/*     */   
/*  60 */   public static final ResourceKey<PlacedFeature> PATCH_MELON = PlacementUtils.createKey("patch_melon");
/*     */   
/*  62 */   public static final ResourceKey<PlacedFeature> PATCH_MELON_SPARSE = PlacementUtils.createKey("patch_melon_sparse");
/*     */   
/*  64 */   public static final ResourceKey<PlacedFeature> PATCH_BERRY_COMMON = PlacementUtils.createKey("patch_berry_common");
/*  65 */   public static final ResourceKey<PlacedFeature> PATCH_BERRY_RARE = PlacementUtils.createKey("patch_berry_rare");
/*     */   
/*  67 */   public static final ResourceKey<PlacedFeature> PATCH_WATERLILY = PlacementUtils.createKey("patch_waterlily");
/*     */   
/*  69 */   public static final ResourceKey<PlacedFeature> PATCH_TALL_GRASS_2 = PlacementUtils.createKey("patch_tall_grass_2");
/*  70 */   public static final ResourceKey<PlacedFeature> PATCH_TALL_GRASS = PlacementUtils.createKey("patch_tall_grass");
/*  71 */   public static final ResourceKey<PlacedFeature> PATCH_LARGE_FERN = PlacementUtils.createKey("patch_large_fern");
/*  72 */   public static final ResourceKey<PlacedFeature> PATCH_BUSH = PlacementUtils.createKey("patch_bush");
/*  73 */   public static final ResourceKey<PlacedFeature> PATCH_LEAF_LITTER = PlacementUtils.createKey("patch_leaf_litter");
/*     */   
/*  75 */   public static final ResourceKey<PlacedFeature> PATCH_CACTUS_DESERT = PlacementUtils.createKey("patch_cactus_desert");
/*  76 */   public static final ResourceKey<PlacedFeature> PATCH_CACTUS_DECORATED = PlacementUtils.createKey("patch_cactus_decorated");
/*     */   
/*  78 */   public static final ResourceKey<PlacedFeature> PATCH_SUGAR_CANE_SWAMP = PlacementUtils.createKey("patch_sugar_cane_swamp");
/*  79 */   public static final ResourceKey<PlacedFeature> PATCH_SUGAR_CANE_DESERT = PlacementUtils.createKey("patch_sugar_cane_desert");
/*  80 */   public static final ResourceKey<PlacedFeature> PATCH_SUGAR_CANE_BADLANDS = PlacementUtils.createKey("patch_sugar_cane_badlands");
/*  81 */   public static final ResourceKey<PlacedFeature> PATCH_SUGAR_CANE = PlacementUtils.createKey("patch_sugar_cane");
/*     */   
/*  83 */   public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_SWAMP = PlacementUtils.createKey("patch_firefly_bush_swamp");
/*  84 */   public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP = PlacementUtils.createKey("patch_firefly_bush_near_water_swamp");
/*  85 */   public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_NEAR_WATER = PlacementUtils.createKey("patch_firefly_bush_near_water");
/*     */   
/*  87 */   public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_NETHER = PlacementUtils.createKey("brown_mushroom_nether");
/*  88 */   public static final ResourceKey<PlacedFeature> RED_MUSHROOM_NETHER = PlacementUtils.createKey("red_mushroom_nether");
/*  89 */   public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_NORMAL = PlacementUtils.createKey("brown_mushroom_normal");
/*  90 */   public static final ResourceKey<PlacedFeature> RED_MUSHROOM_NORMAL = PlacementUtils.createKey("red_mushroom_normal");
/*  91 */   public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_TAIGA = PlacementUtils.createKey("brown_mushroom_taiga");
/*  92 */   public static final ResourceKey<PlacedFeature> RED_MUSHROOM_TAIGA = PlacementUtils.createKey("red_mushroom_taiga");
/*  93 */   public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_OLD_GROWTH = PlacementUtils.createKey("brown_mushroom_old_growth");
/*  94 */   public static final ResourceKey<PlacedFeature> RED_MUSHROOM_OLD_GROWTH = PlacementUtils.createKey("red_mushroom_old_growth");
/*  95 */   public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_SWAMP = PlacementUtils.createKey("brown_mushroom_swamp");
/*  96 */   public static final ResourceKey<PlacedFeature> RED_MUSHROOM_SWAMP = PlacementUtils.createKey("red_mushroom_swamp");
/*     */   
/*  98 */   public static final ResourceKey<PlacedFeature> FLOWER_WARM = PlacementUtils.createKey("flower_warm");
/*  99 */   public static final ResourceKey<PlacedFeature> FLOWER_DEFAULT = PlacementUtils.createKey("flower_default");
/* 100 */   public static final ResourceKey<PlacedFeature> FLOWER_FLOWER_FOREST = PlacementUtils.createKey("flower_flower_forest");
/* 101 */   public static final ResourceKey<PlacedFeature> FLOWER_SWAMP = PlacementUtils.createKey("flower_swamp");
/* 102 */   public static final ResourceKey<PlacedFeature> FLOWER_PLAINS = PlacementUtils.createKey("flower_plains");
/* 103 */   public static final ResourceKey<PlacedFeature> FLOWER_MEADOW = PlacementUtils.createKey("flower_meadow");
/* 104 */   public static final ResourceKey<PlacedFeature> FLOWER_CHERRY = PlacementUtils.createKey("flower_cherry");
/* 105 */   public static final ResourceKey<PlacedFeature> FLOWER_PALE_GARDEN = PlacementUtils.createKey("flower_pale_garden");
/* 106 */   public static final ResourceKey<PlacedFeature> WILDFLOWERS_BIRCH_FOREST = PlacementUtils.createKey("wildflowers_birch_forest");
/* 107 */   public static final ResourceKey<PlacedFeature> WILDFLOWERS_MEADOW = PlacementUtils.createKey("wildflowers_meadow");
/*     */   
/* 109 */   public static final ResourceKey<PlacedFeature> TREES_PLAINS = PlacementUtils.createKey("trees_plains");
/* 110 */   public static final ResourceKey<PlacedFeature> DARK_FOREST_VEGETATION = PlacementUtils.createKey("dark_forest_vegetation");
/* 111 */   public static final ResourceKey<PlacedFeature> PALE_GARDEN_VEGETATION = PlacementUtils.createKey("pale_garden_vegetation");
/* 112 */   public static final ResourceKey<PlacedFeature> FLOWER_FOREST_FLOWERS = PlacementUtils.createKey("flower_forest_flowers");
/* 113 */   public static final ResourceKey<PlacedFeature> FOREST_FLOWERS = PlacementUtils.createKey("forest_flowers");
/* 114 */   public static final ResourceKey<PlacedFeature> PALE_GARDEN_FLOWERS = PlacementUtils.createKey("pale_garden_flowers");
/* 115 */   public static final ResourceKey<PlacedFeature> PALE_MOSS_PATCH = PlacementUtils.createKey("pale_moss_patch");
/*     */   
/* 117 */   public static final ResourceKey<PlacedFeature> TREES_FLOWER_FOREST = PlacementUtils.createKey("trees_flower_forest");
/* 118 */   public static final ResourceKey<PlacedFeature> TREES_MEADOW = PlacementUtils.createKey("trees_meadow");
/* 119 */   public static final ResourceKey<PlacedFeature> TREES_CHERRY = PlacementUtils.createKey("trees_cherry");
/* 120 */   public static final ResourceKey<PlacedFeature> TREES_TAIGA = PlacementUtils.createKey("trees_taiga");
/* 121 */   public static final ResourceKey<PlacedFeature> TREES_GROVE = PlacementUtils.createKey("trees_grove");
/* 122 */   public static final ResourceKey<PlacedFeature> TREES_BADLANDS = PlacementUtils.createKey("trees_badlands");
/* 123 */   public static final ResourceKey<PlacedFeature> TREES_SNOWY = PlacementUtils.createKey("trees_snowy");
/* 124 */   public static final ResourceKey<PlacedFeature> TREES_SWAMP = PlacementUtils.createKey("trees_swamp");
/* 125 */   public static final ResourceKey<PlacedFeature> TREES_WINDSWEPT_SAVANNA = PlacementUtils.createKey("trees_windswept_savanna");
/* 126 */   public static final ResourceKey<PlacedFeature> TREES_SAVANNA = PlacementUtils.createKey("trees_savanna");
/* 127 */   public static final ResourceKey<PlacedFeature> BIRCH_TALL = PlacementUtils.createKey("birch_tall");
/* 128 */   public static final ResourceKey<PlacedFeature> TREES_BIRCH = PlacementUtils.createKey("trees_birch");
/* 129 */   public static final ResourceKey<PlacedFeature> TREES_WINDSWEPT_FOREST = PlacementUtils.createKey("trees_windswept_forest");
/* 130 */   public static final ResourceKey<PlacedFeature> TREES_WINDSWEPT_HILLS = PlacementUtils.createKey("trees_windswept_hills");
/* 131 */   public static final ResourceKey<PlacedFeature> TREES_WATER = PlacementUtils.createKey("trees_water");
/* 132 */   public static final ResourceKey<PlacedFeature> TREES_BIRCH_AND_OAK_LEAF_LITTER = PlacementUtils.createKey("trees_birch_and_oak_leaf_litter");
/* 133 */   public static final ResourceKey<PlacedFeature> TREES_SPARSE_JUNGLE = PlacementUtils.createKey("trees_sparse_jungle");
/* 134 */   public static final ResourceKey<PlacedFeature> TREES_OLD_GROWTH_SPRUCE_TAIGA = PlacementUtils.createKey("trees_old_growth_spruce_taiga");
/* 135 */   public static final ResourceKey<PlacedFeature> TREES_OLD_GROWTH_PINE_TAIGA = PlacementUtils.createKey("trees_old_growth_pine_taiga");
/* 136 */   public static final ResourceKey<PlacedFeature> TREES_JUNGLE = PlacementUtils.createKey("trees_jungle");
/* 137 */   public static final ResourceKey<PlacedFeature> BAMBOO_VEGETATION = PlacementUtils.createKey("bamboo_vegetation");
/*     */   
/* 139 */   public static final ResourceKey<PlacedFeature> MUSHROOM_ISLAND_VEGETATION = PlacementUtils.createKey("mushroom_island_vegetation");
/*     */   
/* 141 */   public static final ResourceKey<PlacedFeature> TREES_MANGROVE = PlacementUtils.createKey("trees_mangrove");
/*     */   
/* 143 */   private static final PlacementModifier TREE_THRESHOLD = SurfaceWaterDepthFilter.forMaxDepth(0);
/*     */   
/*     */   public static List<PlacementModifier> worldSurfaceSquaredWithCount(int count) {
/* 146 */     return List.of(
/* 147 */         CountPlacement.of(count), 
/* 148 */         InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */         
/* 150 */         BiomeFilter.biome());
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<PlacementModifier> getMushroomPlacement(int rarity, PlacementModifier prefix) {
/* 155 */     ImmutableList.Builder<PlacementModifier> builder = ImmutableList.builder();
/* 156 */     if (prefix != null) {
/* 157 */       builder.add(prefix);
/*     */     }
/* 159 */     if (rarity != 0) {
/* 160 */       builder.add(RarityFilter.onAverageOnceEvery(rarity));
/*     */     }
/* 162 */     builder.add(InSquarePlacement.spread());
/* 163 */     builder.add(PlacementUtils.HEIGHTMAP);
/* 164 */     builder.add(BiomeFilter.biome());
/* 165 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/* 169 */   private static ImmutableList.Builder<PlacementModifier> treePlacementBase(PlacementModifier frequency) { return ImmutableList.builder()
/* 170 */       .add(frequency)
/* 171 */       .add(InSquarePlacement.spread())
/* 172 */       .add(TREE_THRESHOLD)
/* 173 */       .add(PlacementUtils.HEIGHTMAP_OCEAN_FLOOR)
/* 174 */       .add(BiomeFilter.biome()); }
/*     */ 
/*     */ 
/*     */   
/* 178 */   public static List<PlacementModifier> treePlacement(PlacementModifier frequency) { return treePlacementBase(frequency).build(); }
/*     */ 
/*     */ 
/*     */   
/* 182 */   public static List<PlacementModifier> treePlacement(PlacementModifier frequency, Block sapling) { return treePlacementBase(frequency)
/* 183 */       .add(BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(sapling.defaultBlockState(), BlockPos.ZERO)))
/* 184 */       .build(); }
/*     */ 
/*     */   
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> context) {
/* 188 */     HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
/* 189 */     Holder.Reference reference1 = configuredFeatures.getOrThrow(VegetationFeatures.BAMBOO_NO_PODZOL);
/* 190 */     Holder.Reference reference2 = configuredFeatures.getOrThrow(VegetationFeatures.BAMBOO_SOME_PODZOL);
/* 191 */     Holder.Reference reference3 = configuredFeatures.getOrThrow(VegetationFeatures.VINES);
/* 192 */     Holder.Reference reference4 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_SUNFLOWER);
/* 193 */     Holder.Reference reference5 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_PUMPKIN);
/* 194 */     Holder.Reference reference6 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_GRASS);
/* 195 */     Holder.Reference reference7 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_GRASS_MEADOW);
/* 196 */     Holder.Reference reference8 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_LEAF_LITTER);
/* 197 */     Holder.Reference reference9 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_TAIGA_GRASS);
/* 198 */     Holder.Reference reference10 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_GRASS_JUNGLE);
/* 199 */     Holder.Reference reference11 = configuredFeatures.getOrThrow(VegetationFeatures.SINGLE_PIECE_OF_GRASS);
/* 200 */     Holder.Reference reference12 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_DEAD_BUSH);
/* 201 */     Holder.Reference reference13 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_DRY_GRASS);
/* 202 */     Holder.Reference reference14 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_FIREFLY_BUSH);
/* 203 */     Holder.Reference reference15 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_MELON);
/* 204 */     Holder.Reference reference16 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_BERRY_BUSH);
/* 205 */     Holder.Reference reference17 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_WATERLILY);
/* 206 */     Holder.Reference reference18 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_TALL_GRASS);
/* 207 */     Holder.Reference reference19 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_LARGE_FERN);
/* 208 */     Holder.Reference reference20 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_BUSH);
/* 209 */     Holder.Reference reference21 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_CACTUS);
/* 210 */     Holder.Reference reference22 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_SUGAR_CANE);
/* 211 */     Holder.Reference reference23 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_BROWN_MUSHROOM);
/* 212 */     Holder.Reference reference24 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_RED_MUSHROOM);
/* 213 */     Holder.Reference reference25 = configuredFeatures.getOrThrow(VegetationFeatures.FLOWER_DEFAULT);
/* 214 */     Holder.Reference reference26 = configuredFeatures.getOrThrow(VegetationFeatures.FLOWER_FLOWER_FOREST);
/* 215 */     Holder.Reference reference27 = configuredFeatures.getOrThrow(VegetationFeatures.FLOWER_SWAMP);
/* 216 */     Holder.Reference reference28 = configuredFeatures.getOrThrow(VegetationFeatures.FLOWER_PLAIN);
/* 217 */     Holder.Reference reference29 = configuredFeatures.getOrThrow(VegetationFeatures.FLOWER_MEADOW);
/* 218 */     Holder.Reference reference30 = configuredFeatures.getOrThrow(VegetationFeatures.FLOWER_CHERRY);
/* 219 */     Holder.Reference reference31 = configuredFeatures.getOrThrow(VegetationFeatures.FLOWER_PALE_GARDEN);
/* 220 */     Holder.Reference reference32 = configuredFeatures.getOrThrow(VegetationFeatures.WILDFLOWERS_BIRCH_FOREST);
/* 221 */     Holder.Reference reference33 = configuredFeatures.getOrThrow(VegetationFeatures.WILDFLOWERS_MEADOW);
/* 222 */     Holder.Reference reference34 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_PLAINS);
/* 223 */     Holder.Reference reference35 = configuredFeatures.getOrThrow(VegetationFeatures.DARK_FOREST_VEGETATION);
/* 224 */     Holder.Reference reference36 = configuredFeatures.getOrThrow(VegetationFeatures.PALE_GARDEN_VEGETATION);
/* 225 */     Holder.Reference reference37 = configuredFeatures.getOrThrow(VegetationFeatures.FOREST_FLOWERS);
/* 226 */     Holder.Reference reference38 = configuredFeatures.getOrThrow(VegetationFeatures.PALE_FOREST_FLOWERS);
/* 227 */     Holder.Reference reference39 = configuredFeatures.getOrThrow(VegetationFeatures.PALE_MOSS_PATCH);
/* 228 */     Holder.Reference reference40 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_FLOWER_FOREST);
/* 229 */     Holder.Reference reference41 = configuredFeatures.getOrThrow(VegetationFeatures.MEADOW_TREES);
/* 230 */     Holder.Reference reference42 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_TAIGA);
/* 231 */     Holder.Reference reference43 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_BADLANDS);
/* 232 */     Holder.Reference reference44 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_GROVE);
/* 233 */     Holder.Reference reference45 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_SNOWY);
/* 234 */     Holder.Reference reference46 = configuredFeatures.getOrThrow(TreeFeatures.CHERRY_BEES_005);
/* 235 */     Holder.Reference reference47 = configuredFeatures.getOrThrow(TreeFeatures.SWAMP_OAK);
/* 236 */     Holder.Reference reference48 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_SAVANNA);
/* 237 */     Holder.Reference reference49 = configuredFeatures.getOrThrow(VegetationFeatures.BIRCH_TALL);
/* 238 */     Holder.Reference reference50 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_BIRCH);
/* 239 */     Holder.Reference reference51 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_WINDSWEPT_HILLS);
/* 240 */     Holder.Reference reference52 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_WATER);
/* 241 */     Holder.Reference reference53 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_BIRCH_AND_OAK_LEAF_LITTER);
/* 242 */     Holder.Reference reference54 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_SPARSE_JUNGLE);
/* 243 */     Holder.Reference reference55 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_OLD_GROWTH_SPRUCE_TAIGA);
/* 244 */     Holder.Reference reference56 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_OLD_GROWTH_PINE_TAIGA);
/* 245 */     Holder.Reference reference57 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_JUNGLE);
/* 246 */     Holder.Reference reference58 = configuredFeatures.getOrThrow(VegetationFeatures.BAMBOO_VEGETATION);
/* 247 */     Holder.Reference reference59 = configuredFeatures.getOrThrow(VegetationFeatures.MUSHROOM_ISLAND_VEGETATION);
/* 248 */     Holder.Reference reference60 = configuredFeatures.getOrThrow(VegetationFeatures.MANGROVE_VEGETATION);
/*     */     
/* 250 */     PlacementUtils.register(context, BAMBOO_LIGHT, reference1, new PlacementModifier[] {
/* 251 */           RarityFilter.onAverageOnceEvery(4), 
/* 252 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 254 */           BiomeFilter.biome()
/*     */         });
/* 256 */     PlacementUtils.register(context, BAMBOO, reference2, new PlacementModifier[] {
/* 257 */           NoiseBasedCountPlacement.of(160, 80.0D, 0.3D), 
/* 258 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 260 */           BiomeFilter.biome()
/*     */         });
/* 262 */     PlacementUtils.register(context, VINES, reference3, new PlacementModifier[] {
/* 263 */           CountPlacement.of(127), 
/* 264 */           InSquarePlacement.spread(), 
/* 265 */           HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(100)), 
/* 266 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 269 */     PlacementUtils.register(context, PATCH_SUNFLOWER, reference4, new PlacementModifier[] {
/* 270 */           RarityFilter.onAverageOnceEvery(3), 
/* 271 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 273 */           BiomeFilter.biome()
/*     */         });
/* 275 */     PlacementUtils.register(context, PATCH_PUMPKIN, reference5, new PlacementModifier[] {
/* 276 */           RarityFilter.onAverageOnceEvery(300), 
/* 277 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 279 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 282 */     PlacementUtils.register(context, PATCH_GRASS_PLAIN, reference6, new PlacementModifier[] {
/* 283 */           NoiseThresholdCountPlacement.of(-0.8D, 5, 10), 
/* 284 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 286 */           BiomeFilter.biome()
/*     */         });
/* 288 */     PlacementUtils.register(context, PATCH_GRASS_MEADOW, reference7, new PlacementModifier[] {
/* 289 */           NoiseThresholdCountPlacement.of(-0.8D, 5, 10), 
/* 290 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 292 */           BiomeFilter.biome()
/*     */         });
/* 294 */     PlacementUtils.register(context, PATCH_GRASS_FOREST, reference6, 
/* 295 */         worldSurfaceSquaredWithCount(2));
/*     */     
/* 297 */     PlacementUtils.register(context, PATCH_LEAF_LITTER, reference8, 
/* 298 */         worldSurfaceSquaredWithCount(2));
/*     */     
/* 300 */     PlacementUtils.register(context, PATCH_GRASS_BADLANDS, reference6, new PlacementModifier[] {
/* 301 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 303 */           BiomeFilter.biome()
/*     */         });
/* 305 */     PlacementUtils.register(context, PATCH_GRASS_SAVANNA, reference6, 
/* 306 */         worldSurfaceSquaredWithCount(20));
/*     */     
/* 308 */     PlacementUtils.register(context, PATCH_GRASS_NORMAL, reference6, 
/* 309 */         worldSurfaceSquaredWithCount(5));
/*     */     
/* 311 */     PlacementUtils.register(context, PATCH_GRASS_TAIGA_2, reference9, new PlacementModifier[] {
/* 312 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 314 */           BiomeFilter.biome()
/*     */         });
/* 316 */     PlacementUtils.register(context, PATCH_GRASS_TAIGA, reference9, 
/* 317 */         worldSurfaceSquaredWithCount(7));
/*     */     
/* 319 */     PlacementUtils.register(context, PATCH_GRASS_JUNGLE, reference10, 
/* 320 */         worldSurfaceSquaredWithCount(25));
/*     */ 
/*     */     
/* 323 */     PlacementUtils.register(context, GRASS_BONEMEAL, reference11, new PlacementModifier[] { PlacementUtils.isEmpty() });
/*     */     
/* 325 */     PlacementUtils.register(context, PATCH_DEAD_BUSH_2, reference12, 
/* 326 */         worldSurfaceSquaredWithCount(2));
/*     */     
/* 328 */     PlacementUtils.register(context, PATCH_DEAD_BUSH, reference12, new PlacementModifier[] {
/* 329 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 331 */           BiomeFilter.biome()
/*     */         });
/* 333 */     PlacementUtils.register(context, PATCH_DEAD_BUSH_BADLANDS, reference12, 
/* 334 */         worldSurfaceSquaredWithCount(20));
/*     */     
/* 336 */     PlacementUtils.register(context, PATCH_DRY_GRASS_BADLANDS, reference13, new PlacementModifier[] {
/* 337 */           RarityFilter.onAverageOnceEvery(6), 
/* 338 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 340 */           BiomeFilter.biome()
/*     */         });
/* 342 */     PlacementUtils.register(context, PATCH_DRY_GRASS_DESERT, reference13, new PlacementModifier[] {
/* 343 */           RarityFilter.onAverageOnceEvery(3), 
/* 344 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 346 */           BiomeFilter.biome()
/*     */         });
/* 348 */     PlacementUtils.register(context, PATCH_MELON, reference15, new PlacementModifier[] {
/* 349 */           RarityFilter.onAverageOnceEvery(6), 
/* 350 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 352 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 355 */     PlacementUtils.register(context, PATCH_MELON_SPARSE, reference15, new PlacementModifier[] {
/* 356 */           RarityFilter.onAverageOnceEvery(64), 
/* 357 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 359 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 362 */     PlacementUtils.register(context, PATCH_BERRY_COMMON, reference16, new PlacementModifier[] {
/* 363 */           RarityFilter.onAverageOnceEvery(32), 
/* 364 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 366 */           BiomeFilter.biome()
/*     */         });
/* 368 */     PlacementUtils.register(context, PATCH_BERRY_RARE, reference16, new PlacementModifier[] {
/* 369 */           RarityFilter.onAverageOnceEvery(384), 
/* 370 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 372 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 375 */     PlacementUtils.register(context, PATCH_WATERLILY, reference17, 
/* 376 */         worldSurfaceSquaredWithCount(4));
/*     */ 
/*     */     
/* 379 */     PlacementUtils.register(context, PATCH_TALL_GRASS_2, reference18, new PlacementModifier[] {
/* 380 */           NoiseThresholdCountPlacement.of(-0.8D, 0, 7), 
/* 381 */           RarityFilter.onAverageOnceEvery(32), 
/* 382 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 384 */           BiomeFilter.biome()
/*     */         });
/* 386 */     PlacementUtils.register(context, PATCH_TALL_GRASS, reference18, new PlacementModifier[] {
/* 387 */           RarityFilter.onAverageOnceEvery(5), 
/* 388 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 390 */           BiomeFilter.biome()
/*     */         });
/* 392 */     PlacementUtils.register(context, PATCH_LARGE_FERN, reference19, new PlacementModifier[] {
/* 393 */           RarityFilter.onAverageOnceEvery(5), 
/* 394 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 396 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 399 */     PlacementUtils.register(context, PATCH_BUSH, reference20, new PlacementModifier[] {
/* 400 */           RarityFilter.onAverageOnceEvery(4), 
/* 401 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 403 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 406 */     PlacementUtils.register(context, PATCH_CACTUS_DESERT, reference21, new PlacementModifier[] {
/* 407 */           RarityFilter.onAverageOnceEvery(6), 
/* 408 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 410 */           BiomeFilter.biome()
/*     */         });
/* 412 */     PlacementUtils.register(context, PATCH_CACTUS_DECORATED, reference21, new PlacementModifier[] {
/* 413 */           RarityFilter.onAverageOnceEvery(13), 
/* 414 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 416 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 419 */     PlacementUtils.register(context, PATCH_SUGAR_CANE_SWAMP, reference22, new PlacementModifier[] {
/* 420 */           RarityFilter.onAverageOnceEvery(3), 
/* 421 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 423 */           BiomeFilter.biome()
/*     */         });
/* 425 */     PlacementUtils.register(context, PATCH_SUGAR_CANE_DESERT, reference22, new PlacementModifier[] {
/* 426 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 428 */           BiomeFilter.biome()
/*     */         });
/* 430 */     PlacementUtils.register(context, PATCH_SUGAR_CANE_BADLANDS, reference22, new PlacementModifier[] {
/* 431 */           RarityFilter.onAverageOnceEvery(5), 
/* 432 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 434 */           BiomeFilter.biome()
/*     */         });
/* 436 */     PlacementUtils.register(context, PATCH_SUGAR_CANE, reference22, new PlacementModifier[] {
/* 437 */           RarityFilter.onAverageOnceEvery(6), 
/* 438 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 440 */           BiomeFilter.biome()
/*     */         });
/* 442 */     PlacementUtils.register(context, PATCH_FIREFLY_BUSH_NEAR_WATER, reference14, new PlacementModifier[] {
/* 443 */           CountPlacement.of(2), 
/* 444 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_NO_LEAVES, 
/*     */           
/* 446 */           BiomeFilter.biome(), 
/* 447 */           VegetationFeatures.nearWaterPredicate(Blocks.FIREFLY_BUSH)
/*     */         });
/* 449 */     PlacementUtils.register(context, PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP, reference14, new PlacementModifier[] {
/* 450 */           CountPlacement.of(3), 
/* 451 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 453 */           BiomeFilter.biome(), 
/* 454 */           VegetationFeatures.nearWaterPredicate(Blocks.FIREFLY_BUSH)
/*     */         });
/* 456 */     PlacementUtils.register(context, PATCH_FIREFLY_BUSH_SWAMP, reference14, new PlacementModifier[] {
/* 457 */           RarityFilter.onAverageOnceEvery(8), 
/* 458 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 460 */           BiomeFilter.biome()
/*     */         });
/* 462 */     PlacementUtils.register(context, BROWN_MUSHROOM_NETHER, reference23, new PlacementModifier[] {
/* 463 */           RarityFilter.onAverageOnceEvery(2), 
/* 464 */           InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 466 */           BiomeFilter.biome()
/*     */         });
/* 468 */     PlacementUtils.register(context, RED_MUSHROOM_NETHER, reference24, new PlacementModifier[] {
/* 469 */           RarityFilter.onAverageOnceEvery(2), 
/* 470 */           InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 472 */           BiomeFilter.biome()
/*     */         });
/* 474 */     PlacementUtils.register(context, BROWN_MUSHROOM_NORMAL, reference23, 
/* 475 */         getMushroomPlacement(256, null));
/*     */     
/* 477 */     PlacementUtils.register(context, RED_MUSHROOM_NORMAL, reference24, 
/* 478 */         getMushroomPlacement(512, null));
/*     */     
/* 480 */     PlacementUtils.register(context, BROWN_MUSHROOM_TAIGA, reference23, 
/* 481 */         getMushroomPlacement(4, null));
/*     */     
/* 483 */     PlacementUtils.register(context, RED_MUSHROOM_TAIGA, reference24, 
/* 484 */         getMushroomPlacement(256, null));
/*     */     
/* 486 */     PlacementUtils.register(context, BROWN_MUSHROOM_OLD_GROWTH, reference23, 
/* 487 */         getMushroomPlacement(4, CountPlacement.of(3)));
/*     */     
/* 489 */     PlacementUtils.register(context, RED_MUSHROOM_OLD_GROWTH, reference24, 
/* 490 */         getMushroomPlacement(171, null));
/*     */     
/* 492 */     PlacementUtils.register(context, BROWN_MUSHROOM_SWAMP, reference23, 
/* 493 */         getMushroomPlacement(0, CountPlacement.of(2)));
/*     */     
/* 495 */     PlacementUtils.register(context, RED_MUSHROOM_SWAMP, reference24, 
/* 496 */         getMushroomPlacement(64, null));
/*     */ 
/*     */     
/* 499 */     PlacementUtils.register(context, FLOWER_WARM, reference25, new PlacementModifier[] {
/* 500 */           RarityFilter.onAverageOnceEvery(16), 
/* 501 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 503 */           BiomeFilter.biome()
/*     */         });
/* 505 */     PlacementUtils.register(context, FLOWER_DEFAULT, reference25, new PlacementModifier[] {
/* 506 */           RarityFilter.onAverageOnceEvery(32), 
/* 507 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 509 */           BiomeFilter.biome()
/*     */         });
/* 511 */     PlacementUtils.register(context, FLOWER_FLOWER_FOREST, reference26, new PlacementModifier[] {
/* 512 */           CountPlacement.of(3), 
/* 513 */           RarityFilter.onAverageOnceEvery(2), 
/* 514 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 516 */           BiomeFilter.biome()
/*     */         });
/* 518 */     PlacementUtils.register(context, FLOWER_SWAMP, reference27, new PlacementModifier[] {
/* 519 */           RarityFilter.onAverageOnceEvery(32), 
/* 520 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 522 */           BiomeFilter.biome()
/*     */         });
/* 524 */     PlacementUtils.register(context, FLOWER_PLAINS, reference28, new PlacementModifier[] {
/* 525 */           NoiseThresholdCountPlacement.of(-0.8D, 15, 4), 
/* 526 */           RarityFilter.onAverageOnceEvery(32), 
/* 527 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 529 */           BiomeFilter.biome()
/*     */         });
/* 531 */     PlacementUtils.register(context, FLOWER_CHERRY, reference30, new PlacementModifier[] {
/* 532 */           NoiseThresholdCountPlacement.of(-0.8D, 5, 10), 
/* 533 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 535 */           BiomeFilter.biome()
/*     */         });
/* 537 */     PlacementUtils.register(context, FLOWER_MEADOW, reference29, new PlacementModifier[] {
/* 538 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 540 */           BiomeFilter.biome()
/*     */         });
/* 542 */     PlacementUtils.register(context, FLOWER_PALE_GARDEN, reference31, new PlacementModifier[] {
/* 543 */           RarityFilter.onAverageOnceEvery(32), 
/* 544 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 546 */           BiomeFilter.biome()
/*     */         });
/* 548 */     PlacementUtils.register(context, WILDFLOWERS_BIRCH_FOREST, reference32, new PlacementModifier[] {
/* 549 */           CountPlacement.of(3), 
/* 550 */           RarityFilter.onAverageOnceEvery(2), 
/* 551 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 553 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 556 */     PlacementUtils.register(context, WILDFLOWERS_MEADOW, reference33, new PlacementModifier[] {
/* 557 */           NoiseThresholdCountPlacement.of(-0.8D, 5, 10), 
/* 558 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 560 */           BiomeFilter.biome()
/*     */         });
/* 562 */     SurfaceWaterDepthFilter surfaceWaterDepthFilter = SurfaceWaterDepthFilter.forMaxDepth(0);
/* 563 */     PlacementUtils.register(context, TREES_PLAINS, reference34, new PlacementModifier[] {
/* 564 */           PlacementUtils.countExtra(0, 0.05F, 1), 
/* 565 */           InSquarePlacement.spread(), surfaceWaterDepthFilter, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, 
/*     */ 
/*     */           
/* 568 */           BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.defaultBlockState(), BlockPos.ZERO)), 
/* 569 */           BiomeFilter.biome()
/*     */         });
/* 571 */     PlacementUtils.register(context, DARK_FOREST_VEGETATION, reference35, new PlacementModifier[] {
/* 572 */           CountPlacement.of(16), 
/* 573 */           InSquarePlacement.spread(), surfaceWaterDepthFilter, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, 
/*     */ 
/*     */           
/* 576 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 579 */     PlacementUtils.register(context, PALE_GARDEN_VEGETATION, reference36, new PlacementModifier[] {
/* 580 */           CountPlacement.of(16), 
/* 581 */           InSquarePlacement.spread(), surfaceWaterDepthFilter, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, 
/*     */ 
/*     */           
/* 584 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 587 */     PlacementUtils.register(context, FLOWER_FOREST_FLOWERS, reference37, new PlacementModifier[] {
/* 588 */           RarityFilter.onAverageOnceEvery(7), 
/* 589 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 591 */           CountPlacement.of(ClampedInt.of(UniformInt.of(-1, 3), 0, 3)), 
/* 592 */           BiomeFilter.biome()
/*     */         });
/* 594 */     PlacementUtils.register(context, FOREST_FLOWERS, reference37, new PlacementModifier[] {
/* 595 */           RarityFilter.onAverageOnceEvery(7), 
/* 596 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 598 */           CountPlacement.of(ClampedInt.of(UniformInt.of(-3, 1), 0, 1)), 
/* 599 */           BiomeFilter.biome()
/*     */         });
/* 601 */     PlacementUtils.register(context, PALE_GARDEN_FLOWERS, reference38, new PlacementModifier[] {
/* 602 */           RarityFilter.onAverageOnceEvery(8), 
/* 603 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_NO_LEAVES, 
/*     */           
/* 605 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 608 */     PlacementUtils.register(context, PALE_MOSS_PATCH, reference39, new PlacementModifier[] {
/* 609 */           CountPlacement.of(1), 
/* 610 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_NO_LEAVES, 
/*     */           
/* 612 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 615 */     PlacementUtils.register(context, TREES_FLOWER_FOREST, reference40, 
/* 616 */         treePlacement(PlacementUtils.countExtra(6, 0.1F, 1)));
/*     */     
/* 618 */     PlacementUtils.register(context, TREES_MEADOW, reference41, 
/* 619 */         treePlacement(RarityFilter.onAverageOnceEvery(100)));
/*     */     
/* 621 */     PlacementUtils.register(context, TREES_CHERRY, reference46, 
/* 622 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1), Blocks.CHERRY_SAPLING));
/*     */     
/* 624 */     PlacementUtils.register(context, TREES_TAIGA, reference42, 
/* 625 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/*     */     
/* 627 */     PlacementUtils.register(context, TREES_GROVE, reference44, 
/* 628 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/*     */     
/* 630 */     PlacementUtils.register(context, TREES_BADLANDS, reference43, 
/* 631 */         treePlacement(PlacementUtils.countExtra(5, 0.1F, 1), Blocks.OAK_SAPLING));
/*     */     
/* 633 */     PlacementUtils.register(context, TREES_SNOWY, reference45, 
/* 634 */         treePlacement(PlacementUtils.countExtra(0, 0.1F, 1), Blocks.SPRUCE_SAPLING));
/*     */     
/* 636 */     PlacementUtils.register(context, TREES_SWAMP, reference47, new PlacementModifier[] {
/* 637 */           PlacementUtils.countExtra(2, 0.1F, 1), 
/* 638 */           InSquarePlacement.spread(), 
/* 639 */           SurfaceWaterDepthFilter.forMaxDepth(2), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, 
/*     */           
/* 641 */           BiomeFilter.biome(), 
/* 642 */           BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.defaultBlockState(), BlockPos.ZERO))
/*     */         });
/* 644 */     PlacementUtils.register(context, TREES_WINDSWEPT_SAVANNA, reference48, 
/* 645 */         treePlacement(PlacementUtils.countExtra(2, 0.1F, 1)));
/*     */     
/* 647 */     PlacementUtils.register(context, TREES_SAVANNA, reference48, 
/* 648 */         treePlacement(PlacementUtils.countExtra(1, 0.1F, 1)));
/*     */     
/* 650 */     PlacementUtils.register(context, BIRCH_TALL, reference49, 
/* 651 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/*     */     
/* 653 */     PlacementUtils.register(context, TREES_BIRCH, reference50, 
/* 654 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1), Blocks.BIRCH_SAPLING));
/*     */     
/* 656 */     PlacementUtils.register(context, TREES_WINDSWEPT_FOREST, reference51, 
/* 657 */         treePlacement(PlacementUtils.countExtra(3, 0.1F, 1)));
/*     */     
/* 659 */     PlacementUtils.register(context, TREES_WINDSWEPT_HILLS, reference51, 
/* 660 */         treePlacement(PlacementUtils.countExtra(0, 0.1F, 1)));
/*     */     
/* 662 */     PlacementUtils.register(context, TREES_WATER, reference52, 
/* 663 */         treePlacement(PlacementUtils.countExtra(0, 0.1F, 1)));
/*     */     
/* 665 */     PlacementUtils.register(context, TREES_BIRCH_AND_OAK_LEAF_LITTER, reference53, 
/* 666 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/*     */     
/* 668 */     PlacementUtils.register(context, TREES_SPARSE_JUNGLE, reference54, 
/* 669 */         treePlacement(PlacementUtils.countExtra(2, 0.1F, 1)));
/*     */     
/* 671 */     PlacementUtils.register(context, TREES_OLD_GROWTH_SPRUCE_TAIGA, reference55, 
/* 672 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/*     */     
/* 674 */     PlacementUtils.register(context, TREES_OLD_GROWTH_PINE_TAIGA, reference56, 
/* 675 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/*     */     
/* 677 */     PlacementUtils.register(context, TREES_JUNGLE, reference57, 
/* 678 */         treePlacement(PlacementUtils.countExtra(50, 0.1F, 1)));
/*     */     
/* 680 */     PlacementUtils.register(context, BAMBOO_VEGETATION, reference58, 
/* 681 */         treePlacement(PlacementUtils.countExtra(30, 0.1F, 1)));
/*     */ 
/*     */     
/* 684 */     PlacementUtils.register(context, MUSHROOM_ISLAND_VEGETATION, reference59, new PlacementModifier[] {
/* 685 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 687 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 690 */     PlacementUtils.register(context, TREES_MANGROVE, reference60, new PlacementModifier[] {
/* 691 */           CountPlacement.of(25), 
/* 692 */           InSquarePlacement.spread(), 
/* 693 */           SurfaceWaterDepthFilter.forMaxDepth(5), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, 
/*     */           
/* 695 */           BiomeFilter.biome()
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\placement\VegetationPlacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */