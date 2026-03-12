/*     */ package net.minecraft.data.worldgen.placement;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.WeightedListInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlacementUtils
/*     */ {
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> context) {
/*  33 */     AquaticPlacements.bootstrap(context);
/*  34 */     CavePlacements.bootstrap(context);
/*  35 */     EndPlacements.bootstrap(context);
/*  36 */     MiscOverworldPlacements.bootstrap(context);
/*  37 */     NetherPlacements.bootstrap(context);
/*  38 */     OrePlacements.bootstrap(context);
/*  39 */     TreePlacements.bootstrap(context);
/*  40 */     VegetationPlacements.bootstrap(context);
/*  41 */     VillagePlacements.bootstrap(context);
/*     */   }
/*     */   
/*  44 */   public static final PlacementModifier HEIGHTMAP = HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING);
/*  45 */   public static final PlacementModifier HEIGHTMAP_NO_LEAVES = HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES);
/*  46 */   public static final PlacementModifier HEIGHTMAP_TOP_SOLID = HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG);
/*  47 */   public static final PlacementModifier HEIGHTMAP_WORLD_SURFACE = HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG);
/*  48 */   public static final PlacementModifier HEIGHTMAP_OCEAN_FLOOR = HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR);
/*     */   
/*  50 */   public static final PlacementModifier FULL_RANGE = HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top());
/*  51 */   public static final PlacementModifier RANGE_10_10 = HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(10));
/*  52 */   public static final PlacementModifier RANGE_8_8 = HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(8), VerticalAnchor.belowTop(8));
/*  53 */   public static final PlacementModifier RANGE_4_4 = HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(4), VerticalAnchor.belowTop(4));
/*  54 */   public static final PlacementModifier RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT = HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256));
/*     */ 
/*     */   
/*  57 */   public static ResourceKey<PlacedFeature> createKey(String name) { return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.withDefaultNamespace(name)); }
/*     */ 
/*     */ 
/*     */   
/*  61 */   public static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> id, Holder<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> placementModifiers) { context.register(id, new PlacedFeature(feature, List.copyOf(placementModifiers))); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> id, Holder<ConfiguredFeature<?, ?>> feature, PlacementModifier... placementModifiers) { register(context, id, feature, List.of(placementModifiers)); }
/*     */ 
/*     */   
/*     */   public static PlacementModifier countExtra(int count, float chance, int extra) {
/*  69 */     float weight = 1.0F / chance;
/*  70 */     if (Math.abs(weight - (int)weight) > 1.0E-5F) {
/*  71 */       throw new IllegalStateException("Chance data cannot be represented as list weight");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  76 */     WeightedList<IntProvider> distribution = WeightedList.builder().add(ConstantInt.of(count), (int)weight - 1).add(ConstantInt.of(count + extra), 1).build();
/*  77 */     return CountPlacement.of(new WeightedListInt(distribution));
/*     */   }
/*     */ 
/*     */   
/*  81 */   public static PlacementFilter isEmpty() { return BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE); }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public static BlockPredicateFilter filteredByBlockSurvival(Block block) { return BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(block.defaultBlockState(), BlockPos.ZERO)); }
/*     */ 
/*     */ 
/*     */   
/*  89 */   public static Holder<PlacedFeature> inlinePlaced(Holder<ConfiguredFeature<?, ?>> configuredFeature, PlacementModifier... placedFeatures) { return Holder.direct(new PlacedFeature(configuredFeature, List.of(placedFeatures))); }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public static <FC extends net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration, F extends net.minecraft.world.level.levelgen.feature.Feature<FC>> Holder<PlacedFeature> inlinePlaced(F feature, FC config, PlacementModifier... placedFeatures) { return inlinePlaced(Holder.direct(new ConfiguredFeature(feature, config)), placedFeatures); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public static <FC extends net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration, F extends net.minecraft.world.level.levelgen.feature.Feature<FC>> Holder<PlacedFeature> onlyWhenEmpty(F feature, FC config) { return filtered(feature, config, BlockPredicate.ONLY_IN_AIR_PREDICATE); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public static <FC extends net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration, F extends net.minecraft.world.level.levelgen.feature.Feature<FC>> Holder<PlacedFeature> filtered(F feature, FC config, BlockPredicate predicate) { return inlinePlaced(feature, config, new PlacementModifier[] { BlockPredicateFilter.forPredicate(predicate) }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\placement\PlacementUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */