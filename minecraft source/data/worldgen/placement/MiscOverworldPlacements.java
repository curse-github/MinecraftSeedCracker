/*     */ package net.minecraft.data.worldgen.placement;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
/*     */ import net.minecraft.world.level.levelgen.heightproviders.VeryBiasedToBottomHeight;
/*     */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.RarityFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.SurfaceRelativeThresholdFilter;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class MiscOverworldPlacements {
/*  33 */   public static final ResourceKey<PlacedFeature> ICE_SPIKE = PlacementUtils.createKey("ice_spike");
/*  34 */   public static final ResourceKey<PlacedFeature> ICE_PATCH = PlacementUtils.createKey("ice_patch");
/*  35 */   public static final ResourceKey<PlacedFeature> FOREST_ROCK = PlacementUtils.createKey("forest_rock");
/*  36 */   public static final ResourceKey<PlacedFeature> ICEBERG_PACKED = PlacementUtils.createKey("iceberg_packed");
/*  37 */   public static final ResourceKey<PlacedFeature> ICEBERG_BLUE = PlacementUtils.createKey("iceberg_blue");
/*  38 */   public static final ResourceKey<PlacedFeature> BLUE_ICE = PlacementUtils.createKey("blue_ice");
/*     */   
/*  40 */   public static final ResourceKey<PlacedFeature> LAKE_LAVA_UNDERGROUND = PlacementUtils.createKey("lake_lava_underground");
/*  41 */   public static final ResourceKey<PlacedFeature> LAKE_LAVA_SURFACE = PlacementUtils.createKey("lake_lava_surface");
/*     */   
/*  43 */   public static final ResourceKey<PlacedFeature> DISK_CLAY = PlacementUtils.createKey("disk_clay");
/*  44 */   public static final ResourceKey<PlacedFeature> DISK_GRAVEL = PlacementUtils.createKey("disk_gravel");
/*  45 */   public static final ResourceKey<PlacedFeature> DISK_SAND = PlacementUtils.createKey("disk_sand");
/*     */   
/*  47 */   public static final ResourceKey<PlacedFeature> DISK_GRASS = PlacementUtils.createKey("disk_grass");
/*     */   
/*  49 */   public static final ResourceKey<PlacedFeature> FREEZE_TOP_LAYER = PlacementUtils.createKey("freeze_top_layer");
/*  50 */   public static final ResourceKey<PlacedFeature> VOID_START_PLATFORM = PlacementUtils.createKey("void_start_platform");
/*     */   
/*  52 */   public static final ResourceKey<PlacedFeature> DESERT_WELL = PlacementUtils.createKey("desert_well");
/*     */   
/*  54 */   public static final ResourceKey<PlacedFeature> SPRING_LAVA = PlacementUtils.createKey("spring_lava");
/*  55 */   public static final ResourceKey<PlacedFeature> SPRING_LAVA_FROZEN = PlacementUtils.createKey("spring_lava_frozen");
/*  56 */   public static final ResourceKey<PlacedFeature> SPRING_WATER = PlacementUtils.createKey("spring_water");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> context) {
/*  59 */     HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
/*  60 */     Holder.Reference reference1 = configuredFeatures.getOrThrow(MiscOverworldFeatures.ICE_SPIKE);
/*  61 */     Holder.Reference reference2 = configuredFeatures.getOrThrow(MiscOverworldFeatures.ICE_PATCH);
/*  62 */     Holder.Reference reference3 = configuredFeatures.getOrThrow(MiscOverworldFeatures.FOREST_ROCK);
/*  63 */     Holder.Reference reference4 = configuredFeatures.getOrThrow(MiscOverworldFeatures.ICEBERG_PACKED);
/*  64 */     Holder.Reference reference5 = configuredFeatures.getOrThrow(MiscOverworldFeatures.ICEBERG_BLUE);
/*  65 */     Holder.Reference reference6 = configuredFeatures.getOrThrow(MiscOverworldFeatures.BLUE_ICE);
/*  66 */     Holder.Reference reference7 = configuredFeatures.getOrThrow(MiscOverworldFeatures.LAKE_LAVA);
/*  67 */     Holder.Reference reference8 = configuredFeatures.getOrThrow(MiscOverworldFeatures.DISK_CLAY);
/*  68 */     Holder.Reference reference9 = configuredFeatures.getOrThrow(MiscOverworldFeatures.DISK_GRAVEL);
/*  69 */     Holder.Reference reference10 = configuredFeatures.getOrThrow(MiscOverworldFeatures.DISK_SAND);
/*  70 */     Holder.Reference reference11 = configuredFeatures.getOrThrow(MiscOverworldFeatures.DISK_GRASS);
/*  71 */     Holder.Reference reference12 = configuredFeatures.getOrThrow(MiscOverworldFeatures.FREEZE_TOP_LAYER);
/*  72 */     Holder.Reference reference13 = configuredFeatures.getOrThrow(MiscOverworldFeatures.VOID_START_PLATFORM);
/*  73 */     Holder.Reference reference14 = configuredFeatures.getOrThrow(MiscOverworldFeatures.DESERT_WELL);
/*  74 */     Holder.Reference reference15 = configuredFeatures.getOrThrow(MiscOverworldFeatures.SPRING_LAVA_OVERWORLD);
/*  75 */     Holder.Reference reference16 = configuredFeatures.getOrThrow(MiscOverworldFeatures.SPRING_LAVA_FROZEN);
/*  76 */     Holder.Reference reference17 = configuredFeatures.getOrThrow(MiscOverworldFeatures.SPRING_WATER);
/*     */     
/*  78 */     PlacementUtils.register(context, ICE_SPIKE, reference1, new PlacementModifier[] {
/*  79 */           CountPlacement.of(3), 
/*  80 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/*  82 */           BiomeFilter.biome()
/*     */         });
/*  84 */     PlacementUtils.register(context, ICE_PATCH, reference2, new PlacementModifier[] {
/*  85 */           CountPlacement.of(2), 
/*  86 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/*  88 */           RandomOffsetPlacement.vertical(ConstantInt.of(-1)), 
/*  89 */           BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(new Block[] { Blocks.SNOW_BLOCK
/*  90 */               })), BiomeFilter.biome()
/*     */         });
/*  92 */     PlacementUtils.register(context, FOREST_ROCK, reference3, new PlacementModifier[] {
/*  93 */           CountPlacement.of(2), 
/*  94 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/*  96 */           BiomeFilter.biome()
/*     */         });
/*  98 */     PlacementUtils.register(context, ICEBERG_BLUE, reference5, new PlacementModifier[] {
/*  99 */           RarityFilter.onAverageOnceEvery(200), 
/* 100 */           InSquarePlacement.spread(), 
/* 101 */           BiomeFilter.biome()
/*     */         });
/* 103 */     PlacementUtils.register(context, ICEBERG_PACKED, reference4, new PlacementModifier[] {
/* 104 */           RarityFilter.onAverageOnceEvery(16), 
/* 105 */           InSquarePlacement.spread(), 
/* 106 */           BiomeFilter.biome()
/*     */         });
/* 108 */     PlacementUtils.register(context, BLUE_ICE, reference6, new PlacementModifier[] {
/* 109 */           CountPlacement.of(UniformInt.of(0, 19)), 
/* 110 */           InSquarePlacement.spread(), 
/* 111 */           HeightRangePlacement.uniform(VerticalAnchor.absolute(30), VerticalAnchor.absolute(61)), 
/* 112 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 115 */     PlacementUtils.register(context, LAKE_LAVA_UNDERGROUND, reference7, new PlacementModifier[] {
/* 116 */           RarityFilter.onAverageOnceEvery(9), 
/* 117 */           InSquarePlacement.spread(), 
/* 118 */           HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.top())), 
/* 119 */           EnvironmentScanPlacement.scanningFor(Direction.DOWN, 
/*     */             
/* 121 */             BlockPredicate.allOf(
/* 122 */               BlockPredicate.not(BlockPredicate.ONLY_IN_AIR_PREDICATE), 
/* 123 */               BlockPredicate.insideWorld(new BlockPos(0, -5, 0))), 32), 
/*     */ 
/*     */ 
/*     */           
/* 127 */           SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, -2147483648, -5), 
/* 128 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 131 */     PlacementUtils.register(context, LAKE_LAVA_SURFACE, reference7, new PlacementModifier[] {
/* 132 */           RarityFilter.onAverageOnceEvery(200), 
/* 133 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 135 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 138 */     PlacementUtils.register(context, DISK_CLAY, reference8, new PlacementModifier[] {
/* 139 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*     */           
/* 141 */           BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(new Fluid[] { Fluids.WATER
/* 142 */               })), BiomeFilter.biome()
/*     */         });
/* 144 */     PlacementUtils.register(context, DISK_GRAVEL, reference9, new PlacementModifier[] {
/* 145 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*     */           
/* 147 */           BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(new Fluid[] { Fluids.WATER
/* 148 */               })), BiomeFilter.biome()
/*     */         });
/* 150 */     PlacementUtils.register(context, DISK_SAND, reference10, new PlacementModifier[] {
/* 151 */           CountPlacement.of(3), 
/* 152 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*     */           
/* 154 */           BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(new Fluid[] { Fluids.WATER
/* 155 */               })), BiomeFilter.biome()
/*     */         });
/*     */     
/* 158 */     PlacementUtils.register(context, DISK_GRASS, reference11, new PlacementModifier[] {
/* 159 */           CountPlacement.of(1), 
/* 160 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*     */           
/* 162 */           RandomOffsetPlacement.vertical(ConstantInt.of(-1)), 
/* 163 */           BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(new Block[] { Blocks.MUD
/* 164 */               })), BiomeFilter.biome()
/*     */         });
/*     */     
/* 167 */     PlacementUtils.register(context, FREEZE_TOP_LAYER, reference12, new PlacementModifier[] {
/* 168 */           BiomeFilter.biome()
/*     */         });
/* 170 */     PlacementUtils.register(context, VOID_START_PLATFORM, reference13, new PlacementModifier[] {
/* 171 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 174 */     PlacementUtils.register(context, DESERT_WELL, reference14, new PlacementModifier[] {
/* 175 */           RarityFilter.onAverageOnceEvery(1000), 
/* 176 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 178 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 181 */     PlacementUtils.register(context, SPRING_LAVA, reference15, new PlacementModifier[] {
/* 182 */           CountPlacement.of(20), 
/* 183 */           InSquarePlacement.spread(), 
/* 184 */           HeightRangePlacement.of(VeryBiasedToBottomHeight.of(VerticalAnchor.bottom(), VerticalAnchor.belowTop(8), 8)), 
/* 185 */           BiomeFilter.biome()
/*     */         });
/* 187 */     PlacementUtils.register(context, SPRING_LAVA_FROZEN, reference16, new PlacementModifier[] {
/* 188 */           CountPlacement.of(20), 
/* 189 */           InSquarePlacement.spread(), 
/* 190 */           HeightRangePlacement.of(VeryBiasedToBottomHeight.of(VerticalAnchor.bottom(), VerticalAnchor.belowTop(8), 8)), 
/* 191 */           BiomeFilter.biome()
/*     */         });
/* 193 */     PlacementUtils.register(context, SPRING_WATER, reference17, new PlacementModifier[] {
/* 194 */           CountPlacement.of(25), 
/* 195 */           InSquarePlacement.spread(), 
/* 196 */           HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(192)), 
/* 197 */           BiomeFilter.biome()
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\placement\MiscOverworldPlacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */