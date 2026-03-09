/*     */ package net.minecraft.data.worldgen.features;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.ProcessorLists;
/*     */ import net.minecraft.data.worldgen.placement.PlacementUtils;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.util.valueproviders.ClampedNormalFloat;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.UniformFloat;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.util.valueproviders.WeightedListInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CaveVines;
/*     */ import net.minecraft.world.level.block.CaveVinesBlock;
/*     */ import net.minecraft.world.level.block.MultifaceSpreadeableBlock;
/*     */ import net.minecraft.world.level.block.SmallDripleafBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.levelgen.GeodeBlockSettings;
/*     */ import net.minecraft.world.level.levelgen.GeodeCrackSettings;
/*     */ import net.minecraft.world.level.levelgen.GeodeLayerSettings;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.FossilFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DripstoneClusterConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.MultifaceGrowthConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.PointedDripstoneConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RootSystemConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SculkPatchConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.UnderwaterMagmaConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
/*     */ import net.minecraft.world.level.levelgen.placement.CaveSurface;
/*     */ import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CaveFeatures
/*     */ {
/*  63 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MONSTER_ROOM = FeatureUtils.createKey("monster_room");
/*     */   
/*  65 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FOSSIL_COAL = FeatureUtils.createKey("fossil_coal");
/*  66 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FOSSIL_DIAMONDS = FeatureUtils.createKey("fossil_diamonds");
/*     */ 
/*     */   
/*  69 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DRIPSTONE_CLUSTER = FeatureUtils.createKey("dripstone_cluster");
/*     */   
/*  71 */   public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_DRIPSTONE = FeatureUtils.createKey("large_dripstone");
/*     */   
/*  73 */   public static final ResourceKey<ConfiguredFeature<?, ?>> POINTED_DRIPSTONE = FeatureUtils.createKey("pointed_dripstone");
/*     */   
/*  75 */   public static final ResourceKey<ConfiguredFeature<?, ?>> UNDERWATER_MAGMA = FeatureUtils.createKey("underwater_magma");
/*     */   
/*  77 */   public static final ResourceKey<ConfiguredFeature<?, ?>> GLOW_LICHEN = FeatureUtils.createKey("glow_lichen");
/*     */   
/*  79 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ROOTED_AZALEA_TREE = FeatureUtils.createKey("rooted_azalea_tree");
/*     */   
/*  81 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CAVE_VINE = FeatureUtils.createKey("cave_vine");
/*  82 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CAVE_VINE_IN_MOSS = FeatureUtils.createKey("cave_vine_in_moss");
/*  83 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MOSS_VEGETATION = FeatureUtils.createKey("moss_vegetation");
/*  84 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MOSS_PATCH = FeatureUtils.createKey("moss_patch");
/*  85 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MOSS_PATCH_BONEMEAL = FeatureUtils.createKey("moss_patch_bonemeal");
/*     */   
/*     */   private static Holder<PlacedFeature> makeDripleaf(Direction direction) {
/*  88 */     return PlacementUtils.inlinePlaced(Feature.BLOCK_COLUMN, new BlockColumnConfiguration(
/*     */ 
/*     */           
/*  91 */           List.of(
/*  92 */             BlockColumnConfiguration.layer(new WeightedListInt(
/*  93 */                 WeightedList.builder()
/*  94 */                 .add(UniformInt.of(0, 4), 2)
/*  95 */                 .add(ConstantInt.of(0), 1)
/*  96 */                 .build()), 
/*  97 */               BlockStateProvider.simple((BlockState)Blocks.BIG_DRIPLEAF_STEM.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction))), 
/*  98 */             BlockColumnConfiguration.layer(
/*  99 */               ConstantInt.of(1), 
/* 100 */               BlockStateProvider.simple((BlockState)Blocks.BIG_DRIPLEAF.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction)))), Direction.UP, BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, true), new PlacementModifier[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Holder<PlacedFeature> makeSmallDripleaf() {
/* 110 */     return PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(
/*     */ 
/*     */             
/* 113 */             WeightedList.builder()
/* 114 */             .add((BlockState)Blocks.SMALL_DRIPLEAF.defaultBlockState().setValue(SmallDripleafBlock.FACING, Direction.EAST), 1)
/* 115 */             .add((BlockState)Blocks.SMALL_DRIPLEAF.defaultBlockState().setValue(SmallDripleafBlock.FACING, Direction.WEST), 1)
/* 116 */             .add((BlockState)Blocks.SMALL_DRIPLEAF.defaultBlockState().setValue(SmallDripleafBlock.FACING, Direction.NORTH), 1)
/* 117 */             .add((BlockState)Blocks.SMALL_DRIPLEAF.defaultBlockState().setValue(SmallDripleafBlock.FACING, Direction.SOUTH), 1))), new PlacementModifier[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DRIPLEAF = FeatureUtils.createKey("dripleaf");
/*     */   
/* 125 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CLAY_WITH_DRIPLEAVES = FeatureUtils.createKey("clay_with_dripleaves");
/* 126 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CLAY_POOL_WITH_DRIPLEAVES = FeatureUtils.createKey("clay_pool_with_dripleaves");
/* 127 */   public static final ResourceKey<ConfiguredFeature<?, ?>> LUSH_CAVES_CLAY = FeatureUtils.createKey("lush_caves_clay");
/*     */   
/* 129 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MOSS_PATCH_CEILING = FeatureUtils.createKey("moss_patch_ceiling");
/* 130 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SPORE_BLOSSOM = FeatureUtils.createKey("spore_blossom");
/*     */   
/* 132 */   public static final ResourceKey<ConfiguredFeature<?, ?>> AMETHYST_GEODE = FeatureUtils.createKey("amethyst_geode");
/*     */   
/* 134 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SCULK_PATCH_DEEP_DARK = FeatureUtils.createKey("sculk_patch_deep_dark");
/*     */   
/* 136 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SCULK_PATCH_ANCIENT_CITY = FeatureUtils.createKey("sculk_patch_ancient_city");
/*     */   
/* 138 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SCULK_VEIN = FeatureUtils.createKey("sculk_vein");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
/* 141 */     HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
/* 142 */     HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);
/*     */ 
/*     */ 
/*     */     
/* 146 */     FeatureUtils.register(context, MONSTER_ROOM, Feature.MONSTER_ROOM);
/*     */ 
/*     */ 
/*     */     
/* 150 */     List<Identifier> fossilStructures = List.of(
/* 151 */         Identifier.withDefaultNamespace("fossil/spine_1"), 
/* 152 */         Identifier.withDefaultNamespace("fossil/spine_2"), 
/* 153 */         Identifier.withDefaultNamespace("fossil/spine_3"), 
/* 154 */         Identifier.withDefaultNamespace("fossil/spine_4"), 
/* 155 */         Identifier.withDefaultNamespace("fossil/skull_1"), 
/* 156 */         Identifier.withDefaultNamespace("fossil/skull_2"), 
/* 157 */         Identifier.withDefaultNamespace("fossil/skull_3"), 
/* 158 */         Identifier.withDefaultNamespace("fossil/skull_4"));
/*     */     
/* 160 */     List<Identifier> fossilCoalStructures = List.of(
/* 161 */         Identifier.withDefaultNamespace("fossil/spine_1_coal"), 
/* 162 */         Identifier.withDefaultNamespace("fossil/spine_2_coal"), 
/* 163 */         Identifier.withDefaultNamespace("fossil/spine_3_coal"), 
/* 164 */         Identifier.withDefaultNamespace("fossil/spine_4_coal"), 
/* 165 */         Identifier.withDefaultNamespace("fossil/skull_1_coal"), 
/* 166 */         Identifier.withDefaultNamespace("fossil/skull_2_coal"), 
/* 167 */         Identifier.withDefaultNamespace("fossil/skull_3_coal"), 
/* 168 */         Identifier.withDefaultNamespace("fossil/skull_4_coal"));
/*     */ 
/*     */     
/* 171 */     Holder.Reference reference = processorLists.getOrThrow(ProcessorLists.FOSSIL_ROT);
/*     */     
/* 173 */     FeatureUtils.register(context, FOSSIL_COAL, Feature.FOSSIL, new FossilFeatureConfiguration(fossilStructures, fossilCoalStructures, reference, processorLists
/*     */ 
/*     */ 
/*     */           
/* 177 */           .getOrThrow(ProcessorLists.FOSSIL_COAL), 4));
/*     */ 
/*     */     
/* 180 */     FeatureUtils.register(context, FOSSIL_DIAMONDS, Feature.FOSSIL, new FossilFeatureConfiguration(fossilStructures, fossilCoalStructures, reference, processorLists
/*     */ 
/*     */ 
/*     */           
/* 184 */           .getOrThrow(ProcessorLists.FOSSIL_DIAMONDS), 4));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     FeatureUtils.register(context, DRIPSTONE_CLUSTER, Feature.DRIPSTONE_CLUSTER, new DripstoneClusterConfiguration(12, 
/*     */           
/* 192 */           UniformInt.of(3, 6), 
/* 193 */           UniformInt.of(2, 8), 1, 3, 
/*     */ 
/*     */           
/* 196 */           UniformInt.of(2, 4), 
/* 197 */           UniformFloat.of(0.3F, 0.7F), 
/* 198 */           ClampedNormalFloat.of(0.1F, 0.3F, 0.1F, 0.9F), 0.1F, 3, 8));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 204 */     FeatureUtils.register(context, LARGE_DRIPSTONE, Feature.LARGE_DRIPSTONE, new LargeDripstoneConfiguration(30, 
/*     */           
/* 206 */           UniformInt.of(3, 19), 
/* 207 */           UniformFloat.of(0.4F, 2.0F), 0.33F, 
/*     */           
/* 209 */           UniformFloat.of(0.3F, 0.9F), 
/* 210 */           UniformFloat.of(0.4F, 1.0F), 
/* 211 */           UniformFloat.of(0.0F, 0.3F), 4, 0.6F));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     FeatureUtils.register(context, POINTED_DRIPSTONE, Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(HolderSet.direct(new Holder[] {
/* 217 */               PlacementUtils.inlinePlaced(Feature.POINTED_DRIPSTONE, new PointedDripstoneConfiguration(0.2F, 0.7F, 0.5F, 0.5F), new PlacementModifier[] {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 225 */                   EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12), 
/* 226 */                   RandomOffsetPlacement.vertical(ConstantInt.of(1))
/*     */                 
/* 228 */                 }), PlacementUtils.inlinePlaced(Feature.POINTED_DRIPSTONE, new PointedDripstoneConfiguration(0.2F, 0.7F, 0.5F, 0.5F), new PlacementModifier[] {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 236 */                   EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12), 
/* 237 */                   RandomOffsetPlacement.vertical(ConstantInt.of(-1))
/*     */                 })
/*     */             })));
/*     */     
/* 241 */     FeatureUtils.register(context, UNDERWATER_MAGMA, Feature.UNDERWATER_MAGMA, new UnderwaterMagmaConfiguration(5, 1, 0.5F));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 247 */     MultifaceSpreadeableBlock glowLichenBlock = (MultifaceSpreadeableBlock)Blocks.GLOW_LICHEN;
/* 248 */     FeatureUtils.register(context, GLOW_LICHEN, Feature.MULTIFACE_GROWTH, new MultifaceGrowthConfiguration(glowLichenBlock, 20, false, true, true, 0.5F, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 255 */           HolderSet.direct(Block::builtInRegistryHolder, new Block[] { Blocks.STONE, Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.DRIPSTONE_BLOCK, Blocks.CALCITE, Blocks.TUFF, Blocks.DEEPSLATE })));
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
/* 267 */     FeatureUtils.register(context, ROOTED_AZALEA_TREE, Feature.ROOT_SYSTEM, new RootSystemConfiguration(
/* 268 */           PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(TreeFeatures.AZALEA_TREE), new PlacementModifier[0]), 3, 3, BlockTags.AZALEA_ROOT_REPLACEABLE, 
/*     */ 
/*     */ 
/*     */           
/* 272 */           BlockStateProvider.simple(Blocks.ROOTED_DIRT), 20, 100, 3, 2, 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 277 */           BlockStateProvider.simple(Blocks.HANGING_ROOTS), 20, 2, 
/*     */ 
/*     */           
/* 280 */           BlockPredicate.allOf(
/*     */             
/* 282 */             BlockPredicate.anyOf(
/* 283 */               BlockPredicate.matchesBlocks(List.of(Blocks.AIR, Blocks.CAVE_AIR, Blocks.VOID_AIR)), 
/* 284 */               BlockPredicate.matchesTag(BlockTags.REPLACEABLE_BY_TREES)), 
/*     */             
/* 286 */             BlockPredicate.matchesTag(Direction.DOWN.getUnitVec3i(), BlockTags.AZALEA_GROWS_ON))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 292 */     WeightedStateProvider caveVinesBodyProvider = new WeightedStateProvider(WeightedList.builder().add(Blocks.CAVE_VINES_PLANT.defaultBlockState(), 4).add((BlockState)Blocks.CAVE_VINES_PLANT.defaultBlockState().setValue(CaveVines.BERRIES, Boolean.valueOf(true)), 1));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 300 */     RandomizedIntStateProvider caveVinesHeadProvider = new RandomizedIntStateProvider(new WeightedStateProvider(WeightedList.builder().add(Blocks.CAVE_VINES.defaultBlockState(), 4).add((BlockState)Blocks.CAVE_VINES.defaultBlockState().setValue(CaveVines.BERRIES, Boolean.valueOf(true)), 1)), CaveVinesBlock.AGE, UniformInt.of(23, 25));
/*     */     
/* 302 */     FeatureUtils.register(context, CAVE_VINE, Feature.BLOCK_COLUMN, new BlockColumnConfiguration(List.of(
/* 303 */             BlockColumnConfiguration.layer(new WeightedListInt(
/* 304 */                 WeightedList.builder()
/* 305 */                 .add(UniformInt.of(0, 19), 2)
/* 306 */                 .add(UniformInt.of(0, 2), 3)
/* 307 */                 .add(UniformInt.of(0, 6), 10)
/* 308 */                 .build()), caveVinesBodyProvider), 
/*     */             
/* 310 */             BlockColumnConfiguration.layer(
/* 311 */               ConstantInt.of(1), caveVinesHeadProvider)), Direction.DOWN, BlockPredicate.ONLY_IN_AIR_PREDICATE, true));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 318 */     FeatureUtils.register(context, CAVE_VINE_IN_MOSS, Feature.BLOCK_COLUMN, new BlockColumnConfiguration(List.of(
/* 319 */             BlockColumnConfiguration.layer(new WeightedListInt(
/* 320 */                 WeightedList.builder()
/* 321 */                 .add(UniformInt.of(0, 3), 5)
/* 322 */                 .add(UniformInt.of(1, 7), 1)
/* 323 */                 .build()), caveVinesBodyProvider), 
/*     */             
/* 325 */             BlockColumnConfiguration.layer(
/* 326 */               ConstantInt.of(1), caveVinesHeadProvider)), Direction.DOWN, BlockPredicate.ONLY_IN_AIR_PREDICATE, true));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 333 */     FeatureUtils.register(context, MOSS_VEGETATION, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(
/* 334 */             WeightedList.builder()
/* 335 */             .add(Blocks.FLOWERING_AZALEA.defaultBlockState(), 4)
/* 336 */             .add(Blocks.AZALEA.defaultBlockState(), 7)
/* 337 */             .add(Blocks.MOSS_CARPET.defaultBlockState(), 25)
/* 338 */             .add(Blocks.SHORT_GRASS.defaultBlockState(), 50)
/* 339 */             .add(Blocks.TALL_GRASS.defaultBlockState(), 10))));
/*     */ 
/*     */     
/* 342 */     FeatureUtils.register(context, MOSS_PATCH, Feature.VEGETATION_PATCH, new VegetationPatchConfiguration(BlockTags.MOSS_REPLACEABLE, 
/*     */           
/* 344 */           BlockStateProvider.simple(Blocks.MOSS_BLOCK), 
/* 345 */           PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(MOSS_VEGETATION), new PlacementModifier[0]), CaveSurface.FLOOR, 
/*     */           
/* 347 */           ConstantInt.of(1), 0.0F, 5, 0.8F, 
/*     */ 
/*     */ 
/*     */           
/* 351 */           UniformInt.of(4, 7), 0.3F));
/*     */ 
/*     */     
/* 354 */     FeatureUtils.register(context, MOSS_PATCH_BONEMEAL, Feature.VEGETATION_PATCH, new VegetationPatchConfiguration(BlockTags.MOSS_REPLACEABLE, 
/*     */           
/* 356 */           BlockStateProvider.simple(Blocks.MOSS_BLOCK), 
/* 357 */           PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(MOSS_VEGETATION), new PlacementModifier[0]), CaveSurface.FLOOR, 
/*     */           
/* 359 */           ConstantInt.of(1), 0.0F, 5, 0.6F, 
/*     */ 
/*     */ 
/*     */           
/* 363 */           UniformInt.of(1, 2), 0.75F));
/*     */ 
/*     */ 
/*     */     
/* 367 */     FeatureUtils.register(context, DRIPLEAF, Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(
/* 368 */           HolderSet.direct(new Holder[] {
/* 369 */               makeSmallDripleaf(), 
/* 370 */               makeDripleaf(Direction.EAST), 
/* 371 */               makeDripleaf(Direction.WEST), 
/* 372 */               makeDripleaf(Direction.SOUTH), 
/* 373 */               makeDripleaf(Direction.NORTH)
/*     */             })));
/*     */ 
/*     */     
/* 377 */     FeatureUtils.register(context, CLAY_WITH_DRIPLEAVES, Feature.VEGETATION_PATCH, new VegetationPatchConfiguration(BlockTags.LUSH_GROUND_REPLACEABLE, 
/*     */           
/* 379 */           BlockStateProvider.simple(Blocks.CLAY), 
/* 380 */           PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(DRIPLEAF), new PlacementModifier[0]), CaveSurface.FLOOR, 
/*     */           
/* 382 */           ConstantInt.of(3), 0.8F, 2, 0.05F, 
/*     */ 
/*     */ 
/*     */           
/* 386 */           UniformInt.of(4, 7), 0.7F));
/*     */ 
/*     */     
/* 389 */     FeatureUtils.register(context, CLAY_POOL_WITH_DRIPLEAVES, Feature.WATERLOGGED_VEGETATION_PATCH, new VegetationPatchConfiguration(BlockTags.LUSH_GROUND_REPLACEABLE, 
/*     */           
/* 391 */           BlockStateProvider.simple(Blocks.CLAY), 
/* 392 */           PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(DRIPLEAF), new PlacementModifier[0]), CaveSurface.FLOOR, 
/*     */           
/* 394 */           ConstantInt.of(3), 0.8F, 5, 0.1F, 
/*     */ 
/*     */ 
/*     */           
/* 398 */           UniformInt.of(4, 7), 0.7F));
/*     */ 
/*     */     
/* 401 */     FeatureUtils.register(context, LUSH_CAVES_CLAY, Feature.RANDOM_BOOLEAN_SELECTOR, new RandomBooleanFeatureConfiguration(
/* 402 */           PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(CLAY_WITH_DRIPLEAVES), new PlacementModifier[0]), 
/* 403 */           PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(CLAY_POOL_WITH_DRIPLEAVES), new PlacementModifier[0])));
/*     */ 
/*     */     
/* 406 */     FeatureUtils.register(context, MOSS_PATCH_CEILING, Feature.VEGETATION_PATCH, new VegetationPatchConfiguration(BlockTags.MOSS_REPLACEABLE, 
/*     */           
/* 408 */           BlockStateProvider.simple(Blocks.MOSS_BLOCK), 
/* 409 */           PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(CAVE_VINE_IN_MOSS), new PlacementModifier[0]), CaveSurface.CEILING, 
/*     */           
/* 411 */           UniformInt.of(1, 2), 0.0F, 5, 0.08F, 
/*     */ 
/*     */ 
/*     */           
/* 415 */           UniformInt.of(4, 7), 0.3F));
/*     */ 
/*     */     
/* 418 */     FeatureUtils.register(context, SPORE_BLOSSOM, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
/* 419 */           BlockStateProvider.simple(Blocks.SPORE_BLOSSOM)));
/*     */ 
/*     */     
/* 422 */     FeatureUtils.register(context, AMETHYST_GEODE, Feature.GEODE, new GeodeConfiguration(new GeodeBlockSettings(
/*     */             
/* 424 */             BlockStateProvider.simple(Blocks.AIR), 
/* 425 */             BlockStateProvider.simple(Blocks.AMETHYST_BLOCK), 
/* 426 */             BlockStateProvider.simple(Blocks.BUDDING_AMETHYST), 
/* 427 */             BlockStateProvider.simple(Blocks.CALCITE), 
/* 428 */             BlockStateProvider.simple(Blocks.SMOOTH_BASALT), 
/* 429 */             List.of(Blocks.SMALL_AMETHYST_BUD
/* 430 */               .defaultBlockState(), Blocks.MEDIUM_AMETHYST_BUD
/* 431 */               .defaultBlockState(), Blocks.LARGE_AMETHYST_BUD
/* 432 */               .defaultBlockState(), Blocks.AMETHYST_CLUSTER
/* 433 */               .defaultBlockState()), BlockTags.FEATURES_CANNOT_REPLACE, BlockTags.GEODE_INVALID_BLOCKS), new GeodeLayerSettings(1.7D, 2.2D, 3.2D, 4.2D), new GeodeCrackSettings(0.95D, 2.0D, 2), 0.35D, 0.083D, true, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 442 */           UniformInt.of(4, 6), 
/* 443 */           UniformInt.of(3, 4), 
/* 444 */           UniformInt.of(1, 2), -16, 16, 0.05D, 1));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 451 */     FeatureUtils.register(context, SCULK_PATCH_DEEP_DARK, Feature.SCULK_PATCH, new SculkPatchConfiguration(10, 32, 64, 0, 1, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 457 */           ConstantInt.of(0), 0.5F));
/*     */ 
/*     */ 
/*     */     
/* 461 */     FeatureUtils.register(context, SCULK_PATCH_ANCIENT_CITY, Feature.SCULK_PATCH, new SculkPatchConfiguration(10, 32, 64, 0, 1, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 467 */           UniformInt.of(1, 3), 0.5F));
/*     */ 
/*     */ 
/*     */     
/* 471 */     MultifaceSpreadeableBlock sculkVeinBlock = (MultifaceSpreadeableBlock)Blocks.SCULK_VEIN;
/* 472 */     FeatureUtils.register(context, SCULK_VEIN, Feature.MULTIFACE_GROWTH, new MultifaceGrowthConfiguration(sculkVeinBlock, 20, true, true, true, 1.0F, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 479 */           HolderSet.direct(Block::builtInRegistryHolder, new Block[] { Blocks.STONE, Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.DRIPSTONE_BLOCK, Blocks.CALCITE, Blocks.TUFF, Blocks.DEEPSLATE })));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\features\CaveFeatures.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */