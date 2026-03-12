/*    */ package net.minecraft.data.worldgen.features;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.data.worldgen.placement.PlacementUtils;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ 
/*    */ public class FeatureUtils
/*    */ {
/*    */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
/* 24 */     AquaticFeatures.bootstrap(context);
/* 25 */     CaveFeatures.bootstrap(context);
/* 26 */     EndFeatures.bootstrap(context);
/* 27 */     MiscOverworldFeatures.bootstrap(context);
/* 28 */     NetherFeatures.bootstrap(context);
/* 29 */     OreFeatures.bootstrap(context);
/* 30 */     PileFeatures.bootstrap(context);
/* 31 */     TreeFeatures.bootstrap(context);
/* 32 */     VegetationFeatures.bootstrap(context);
/*    */   }
/*    */   
/*    */   private static BlockPredicate simplePatchPredicate(List<Block> allowedOn) {
/*    */     BlockPredicate predicate;
/* 37 */     if (!allowedOn.isEmpty()) {
/* 38 */       predicate = BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), allowedOn));
/*    */     } else {
/* 40 */       predicate = BlockPredicate.ONLY_IN_AIR_PREDICATE;
/*    */     } 
/* 42 */     return predicate;
/*    */   }
/*    */ 
/*    */   
/* 46 */   public static RandomPatchConfiguration simpleRandomPatchConfiguration(int tries, Holder<PlacedFeature> feature) { return new RandomPatchConfiguration(tries, 7, 3, feature); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F feature, FC config, List<Block> allowedOn, int tries) { return simpleRandomPatchConfiguration(tries, PlacementUtils.filtered(feature, config, simplePatchPredicate(allowedOn))); }
/*    */ 
/*    */ 
/*    */   
/* 59 */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F feature, FC config, List<Block> allowedOn) { return simplePatchConfiguration(feature, config, allowedOn, 96); }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F feature, FC config) { return simplePatchConfiguration(feature, config, List.of(), 96); }
/*    */ 
/*    */ 
/*    */   
/* 67 */   public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) { return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.withDefaultNamespace(name)); }
/*    */ 
/*    */ 
/*    */   
/* 71 */   public static void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> id, Feature<NoneFeatureConfiguration> feature) { register(context, id, feature, FeatureConfiguration.NONE); }
/*    */ 
/*    */ 
/*    */   
/* 75 */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> id, F feature, FC config) { context.register(id, new ConfiguredFeature(feature, config)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\features\FeatureUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */