/*     */ package net.minecraft.data.worldgen.placement;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.features.CaveFeatures;
/*     */ import net.minecraft.data.worldgen.features.VegetationFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.valueproviders.ClampedNormalInt;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.RarityFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.SurfaceRelativeThresholdFilter;
/*     */ 
/*     */ public class CavePlacements
/*     */ {
/*  31 */   public static final ResourceKey<PlacedFeature> MONSTER_ROOM = PlacementUtils.createKey("monster_room");
/*  32 */   public static final ResourceKey<PlacedFeature> MONSTER_ROOM_DEEP = PlacementUtils.createKey("monster_room_deep");
/*     */ 
/*     */   
/*  35 */   public static final ResourceKey<PlacedFeature> FOSSIL_UPPER = PlacementUtils.createKey("fossil_upper");
/*  36 */   public static final ResourceKey<PlacedFeature> FOSSIL_LOWER = PlacementUtils.createKey("fossil_lower");
/*     */ 
/*     */   
/*  39 */   public static final ResourceKey<PlacedFeature> DRIPSTONE_CLUSTER = PlacementUtils.createKey("dripstone_cluster");
/*  40 */   public static final ResourceKey<PlacedFeature> LARGE_DRIPSTONE = PlacementUtils.createKey("large_dripstone");
/*  41 */   public static final ResourceKey<PlacedFeature> POINTED_DRIPSTONE = PlacementUtils.createKey("pointed_dripstone");
/*  42 */   public static final ResourceKey<PlacedFeature> UNDERWATER_MAGMA = PlacementUtils.createKey("underwater_magma");
/*  43 */   public static final ResourceKey<PlacedFeature> GLOW_LICHEN = PlacementUtils.createKey("glow_lichen");
/*  44 */   public static final ResourceKey<PlacedFeature> ROOTED_AZALEA_TREE = PlacementUtils.createKey("rooted_azalea_tree");
/*  45 */   public static final ResourceKey<PlacedFeature> CAVE_VINES = PlacementUtils.createKey("cave_vines");
/*  46 */   public static final ResourceKey<PlacedFeature> LUSH_CAVES_VEGETATION = PlacementUtils.createKey("lush_caves_vegetation");
/*  47 */   public static final ResourceKey<PlacedFeature> LUSH_CAVES_CLAY = PlacementUtils.createKey("lush_caves_clay");
/*  48 */   public static final ResourceKey<PlacedFeature> LUSH_CAVES_CEILING_VEGETATION = PlacementUtils.createKey("lush_caves_ceiling_vegetation");
/*  49 */   public static final ResourceKey<PlacedFeature> SPORE_BLOSSOM = PlacementUtils.createKey("spore_blossom");
/*  50 */   public static final ResourceKey<PlacedFeature> CLASSIC_VINES = PlacementUtils.createKey("classic_vines_cave_feature");
/*  51 */   public static final ResourceKey<PlacedFeature> AMETHYST_GEODE = PlacementUtils.createKey("amethyst_geode");
/*     */ 
/*     */   
/*  54 */   public static final ResourceKey<PlacedFeature> SCULK_PATCH_DEEP_DARK = PlacementUtils.createKey("sculk_patch_deep_dark");
/*  55 */   public static final ResourceKey<PlacedFeature> SCULK_PATCH_ANCIENT_CITY = PlacementUtils.createKey("sculk_patch_ancient_city");
/*  56 */   public static final ResourceKey<PlacedFeature> SCULK_VEIN = PlacementUtils.createKey("sculk_vein");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> context) {
/*  59 */     HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
/*  60 */     Holder.Reference reference1 = configuredFeatures.getOrThrow(CaveFeatures.MONSTER_ROOM);
/*  61 */     Holder.Reference reference2 = configuredFeatures.getOrThrow(CaveFeatures.FOSSIL_COAL);
/*  62 */     Holder.Reference reference3 = configuredFeatures.getOrThrow(CaveFeatures.FOSSIL_DIAMONDS);
/*  63 */     Holder.Reference reference4 = configuredFeatures.getOrThrow(CaveFeatures.DRIPSTONE_CLUSTER);
/*  64 */     Holder.Reference reference5 = configuredFeatures.getOrThrow(CaveFeatures.LARGE_DRIPSTONE);
/*  65 */     Holder.Reference reference6 = configuredFeatures.getOrThrow(CaveFeatures.POINTED_DRIPSTONE);
/*  66 */     Holder.Reference reference7 = configuredFeatures.getOrThrow(CaveFeatures.UNDERWATER_MAGMA);
/*  67 */     Holder.Reference reference8 = configuredFeatures.getOrThrow(CaveFeatures.GLOW_LICHEN);
/*  68 */     Holder.Reference reference9 = configuredFeatures.getOrThrow(CaveFeatures.ROOTED_AZALEA_TREE);
/*  69 */     Holder.Reference reference10 = configuredFeatures.getOrThrow(CaveFeatures.CAVE_VINE);
/*  70 */     Holder.Reference reference11 = configuredFeatures.getOrThrow(CaveFeatures.MOSS_PATCH);
/*  71 */     Holder.Reference reference12 = configuredFeatures.getOrThrow(CaveFeatures.LUSH_CAVES_CLAY);
/*  72 */     Holder.Reference reference13 = configuredFeatures.getOrThrow(CaveFeatures.MOSS_PATCH_CEILING);
/*  73 */     Holder.Reference reference14 = configuredFeatures.getOrThrow(CaveFeatures.SPORE_BLOSSOM);
/*  74 */     Holder.Reference reference15 = configuredFeatures.getOrThrow(VegetationFeatures.VINES);
/*  75 */     Holder.Reference reference16 = configuredFeatures.getOrThrow(CaveFeatures.AMETHYST_GEODE);
/*  76 */     Holder.Reference reference17 = configuredFeatures.getOrThrow(CaveFeatures.SCULK_PATCH_DEEP_DARK);
/*  77 */     Holder.Reference reference18 = configuredFeatures.getOrThrow(CaveFeatures.SCULK_PATCH_ANCIENT_CITY);
/*  78 */     Holder.Reference reference19 = configuredFeatures.getOrThrow(CaveFeatures.SCULK_VEIN);
/*     */ 
/*     */     
/*  81 */     PlacementUtils.register(context, MONSTER_ROOM, reference1, new PlacementModifier[] {
/*  82 */           CountPlacement.of(10), 
/*  83 */           InSquarePlacement.spread(), 
/*  84 */           HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.top()), 
/*  85 */           BiomeFilter.biome()
/*     */         });
/*  87 */     PlacementUtils.register(context, MONSTER_ROOM_DEEP, reference1, new PlacementModifier[] {
/*  88 */           CountPlacement.of(4), 
/*  89 */           InSquarePlacement.spread(), 
/*  90 */           HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(-1)), 
/*  91 */           BiomeFilter.biome()
/*     */         });
/*     */ 
/*     */ 
/*     */     
/*  96 */     PlacementUtils.register(context, FOSSIL_UPPER, reference2, new PlacementModifier[] {
/*  97 */           RarityFilter.onAverageOnceEvery(64), 
/*  98 */           InSquarePlacement.spread(), 
/*  99 */           HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.top()), 
/* 100 */           BiomeFilter.biome()
/*     */         });
/* 102 */     PlacementUtils.register(context, FOSSIL_LOWER, reference3, new PlacementModifier[] {
/* 103 */           RarityFilter.onAverageOnceEvery(64), 
/* 104 */           InSquarePlacement.spread(), 
/* 105 */           HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(-8)), 
/* 106 */           BiomeFilter.biome()
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 111 */     PlacementUtils.register(context, DRIPSTONE_CLUSTER, reference4, new PlacementModifier[] {
/* 112 */           CountPlacement.of(UniformInt.of(48, 96)), 
/* 113 */           InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 115 */           BiomeFilter.biome()
/*     */         });
/* 117 */     PlacementUtils.register(context, LARGE_DRIPSTONE, reference5, new PlacementModifier[] {
/* 118 */           CountPlacement.of(UniformInt.of(10, 48)), 
/* 119 */           InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 121 */           BiomeFilter.biome()
/*     */         });
/* 123 */     PlacementUtils.register(context, POINTED_DRIPSTONE, reference6, new PlacementModifier[] {
/* 124 */           CountPlacement.of(UniformInt.of(192, 256)), 
/* 125 */           InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 127 */           CountPlacement.of(UniformInt.of(1, 5)), 
/* 128 */           RandomOffsetPlacement.of(
/* 129 */             ClampedNormalInt.of(0.0F, 3.0F, -10, 10), 
/* 130 */             ClampedNormalInt.of(0.0F, 0.6F, -2, 2)), 
/*     */           
/* 132 */           BiomeFilter.biome()
/*     */         });
/* 134 */     PlacementUtils.register(context, UNDERWATER_MAGMA, reference7, new PlacementModifier[] {
/* 135 */           CountPlacement.of(UniformInt.of(44, 52)), 
/* 136 */           InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 138 */           SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, -2147483648, -2), 
/* 139 */           BiomeFilter.biome()
/*     */         });
/* 141 */     PlacementUtils.register(context, GLOW_LICHEN, reference8, new PlacementModifier[] {
/* 142 */           CountPlacement.of(UniformInt.of(104, 157)), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 144 */           InSquarePlacement.spread(), 
/* 145 */           SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, -2147483648, -13), 
/* 146 */           BiomeFilter.biome()
/*     */         });
/* 148 */     PlacementUtils.register(context, ROOTED_AZALEA_TREE, reference9, new PlacementModifier[] {
/* 149 */           CountPlacement.of(UniformInt.of(1, 2)), 
/* 150 */           InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 152 */           EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 153 */           RandomOffsetPlacement.vertical(ConstantInt.of(-1)), 
/* 154 */           BiomeFilter.biome()
/*     */         });
/* 156 */     PlacementUtils.register(context, CAVE_VINES, reference10, new PlacementModifier[] {
/* 157 */           CountPlacement.of(188), 
/* 158 */           InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 160 */           EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.hasSturdyFace(Direction.DOWN), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 161 */           RandomOffsetPlacement.vertical(ConstantInt.of(-1)), 
/* 162 */           BiomeFilter.biome()
/*     */         });
/* 164 */     PlacementUtils.register(context, LUSH_CAVES_VEGETATION, reference11, new PlacementModifier[] {
/* 165 */           CountPlacement.of(125), 
/* 166 */           InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 168 */           EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 169 */           RandomOffsetPlacement.vertical(ConstantInt.of(1)), 
/* 170 */           BiomeFilter.biome()
/*     */         });
/* 172 */     PlacementUtils.register(context, LUSH_CAVES_CLAY, reference12, new PlacementModifier[] {
/* 173 */           CountPlacement.of(62), 
/* 174 */           InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 176 */           EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 177 */           RandomOffsetPlacement.vertical(ConstantInt.of(1)), 
/* 178 */           BiomeFilter.biome()
/*     */         });
/* 180 */     PlacementUtils.register(context, LUSH_CAVES_CEILING_VEGETATION, reference13, new PlacementModifier[] {
/* 181 */           CountPlacement.of(125), 
/* 182 */           InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 184 */           EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 185 */           RandomOffsetPlacement.vertical(ConstantInt.of(-1)), 
/* 186 */           BiomeFilter.biome()
/*     */         });
/* 188 */     PlacementUtils.register(context, SPORE_BLOSSOM, reference14, new PlacementModifier[] {
/* 189 */           CountPlacement.of(25), 
/* 190 */           InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 192 */           EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 193 */           RandomOffsetPlacement.vertical(ConstantInt.of(-1)), 
/* 194 */           BiomeFilter.biome()
/*     */         });
/* 196 */     PlacementUtils.register(context, CLASSIC_VINES, reference15, new PlacementModifier[] {
/* 197 */           CountPlacement.of(256), 
/* 198 */           InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 200 */           BiomeFilter.biome()
/*     */         });
/* 202 */     PlacementUtils.register(context, AMETHYST_GEODE, reference16, new PlacementModifier[] {
/* 203 */           RarityFilter.onAverageOnceEvery(24), 
/* 204 */           InSquarePlacement.spread(), 
/* 205 */           HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(30)), 
/* 206 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 209 */     PlacementUtils.register(context, SCULK_PATCH_DEEP_DARK, reference17, new PlacementModifier[] {
/* 210 */           CountPlacement.of(ConstantInt.of(256)), 
/* 211 */           InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 213 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 216 */     PlacementUtils.register(context, SCULK_PATCH_ANCIENT_CITY, reference18, new PlacementModifier[0]);
/*     */     
/* 218 */     PlacementUtils.register(context, SCULK_VEIN, reference19, new PlacementModifier[] {
/* 219 */           CountPlacement.of(UniformInt.of(204, 250)), 
/* 220 */           InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 222 */           BiomeFilter.biome()
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\placement\CavePlacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */