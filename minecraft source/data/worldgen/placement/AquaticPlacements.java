/*    */ package net.minecraft.data.worldgen.placement;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.data.worldgen.features.AquaticFeatures;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*    */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*    */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*    */ import net.minecraft.world.level.levelgen.placement.NoiseBasedCountPlacement;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*    */ import net.minecraft.world.level.levelgen.placement.RarityFilter;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AquaticPlacements
/*    */ {
/*    */   private static List<PlacementModifier> seagrassPlacement(int count) {
/* 24 */     return List.of(
/* 25 */         InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*    */         
/* 27 */         CountPlacement.of(count), 
/* 28 */         BiomeFilter.biome());
/*    */   }
/*    */ 
/*    */   
/* 32 */   public static final ResourceKey<PlacedFeature> SEAGRASS_WARM = PlacementUtils.createKey("seagrass_warm");
/* 33 */   public static final ResourceKey<PlacedFeature> SEAGRASS_NORMAL = PlacementUtils.createKey("seagrass_normal");
/* 34 */   public static final ResourceKey<PlacedFeature> SEAGRASS_COLD = PlacementUtils.createKey("seagrass_cold");
/* 35 */   public static final ResourceKey<PlacedFeature> SEAGRASS_RIVER = PlacementUtils.createKey("seagrass_river");
/* 36 */   public static final ResourceKey<PlacedFeature> SEAGRASS_SWAMP = PlacementUtils.createKey("seagrass_swamp");
/* 37 */   public static final ResourceKey<PlacedFeature> SEAGRASS_DEEP_WARM = PlacementUtils.createKey("seagrass_deep_warm");
/* 38 */   public static final ResourceKey<PlacedFeature> SEAGRASS_DEEP = PlacementUtils.createKey("seagrass_deep");
/* 39 */   public static final ResourceKey<PlacedFeature> SEAGRASS_DEEP_COLD = PlacementUtils.createKey("seagrass_deep_cold");
/* 40 */   public static final ResourceKey<PlacedFeature> SEA_PICKLE = PlacementUtils.createKey("sea_pickle");
/* 41 */   public static final ResourceKey<PlacedFeature> KELP_COLD = PlacementUtils.createKey("kelp_cold");
/* 42 */   public static final ResourceKey<PlacedFeature> KELP_WARM = PlacementUtils.createKey("kelp_warm");
/* 43 */   public static final ResourceKey<PlacedFeature> WARM_OCEAN_VEGETATION = PlacementUtils.createKey("warm_ocean_vegetation");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<PlacedFeature> context) {
/* 46 */     HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
/* 47 */     Holder.Reference<ConfiguredFeature<?, ?>> seagrassShort = configuredFeatures.getOrThrow(AquaticFeatures.SEAGRASS_SHORT);
/* 48 */     Holder.Reference<ConfiguredFeature<?, ?>> seagrassSlightlyLessShort = configuredFeatures.getOrThrow(AquaticFeatures.SEAGRASS_SLIGHTLY_LESS_SHORT);
/* 49 */     Holder.Reference<ConfiguredFeature<?, ?>> seagrassMid = configuredFeatures.getOrThrow(AquaticFeatures.SEAGRASS_MID);
/* 50 */     Holder.Reference<ConfiguredFeature<?, ?>> seagrassTall = configuredFeatures.getOrThrow(AquaticFeatures.SEAGRASS_TALL);
/* 51 */     Holder.Reference<ConfiguredFeature<?, ?>> seaPickle = configuredFeatures.getOrThrow(AquaticFeatures.SEA_PICKLE);
/* 52 */     Holder.Reference<ConfiguredFeature<?, ?>> kelp = configuredFeatures.getOrThrow(AquaticFeatures.KELP);
/* 53 */     Holder.Reference<ConfiguredFeature<?, ?>> warmOceanVegetation = configuredFeatures.getOrThrow(AquaticFeatures.WARM_OCEAN_VEGETATION);
/*    */     
/* 55 */     PlacementUtils.register(context, SEAGRASS_WARM, seagrassShort, seagrassPlacement(80));
/* 56 */     PlacementUtils.register(context, SEAGRASS_NORMAL, seagrassShort, seagrassPlacement(48));
/* 57 */     PlacementUtils.register(context, SEAGRASS_COLD, seagrassShort, seagrassPlacement(32));
/* 58 */     PlacementUtils.register(context, SEAGRASS_RIVER, seagrassSlightlyLessShort, seagrassPlacement(48));
/* 59 */     PlacementUtils.register(context, SEAGRASS_SWAMP, seagrassMid, seagrassPlacement(64));
/* 60 */     PlacementUtils.register(context, SEAGRASS_DEEP_WARM, seagrassTall, seagrassPlacement(80));
/* 61 */     PlacementUtils.register(context, SEAGRASS_DEEP, seagrassTall, seagrassPlacement(48));
/* 62 */     PlacementUtils.register(context, SEAGRASS_DEEP_COLD, seagrassTall, seagrassPlacement(40));
/*    */     
/* 64 */     PlacementUtils.register(context, SEA_PICKLE, seaPickle, new PlacementModifier[] {
/* 65 */           RarityFilter.onAverageOnceEvery(16), 
/* 66 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*    */           
/* 68 */           BiomeFilter.biome()
/*    */         });
/*    */     
/* 71 */     PlacementUtils.register(context, KELP_COLD, kelp, new PlacementModifier[] {
/* 72 */           NoiseBasedCountPlacement.of(120, 80.0D, 0.0D), 
/* 73 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*    */           
/* 75 */           BiomeFilter.biome()
/*    */         });
/*    */     
/* 78 */     PlacementUtils.register(context, KELP_WARM, kelp, new PlacementModifier[] {
/* 79 */           NoiseBasedCountPlacement.of(80, 80.0D, 0.0D), 
/* 80 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*    */           
/* 82 */           BiomeFilter.biome()
/*    */         });
/*    */     
/* 85 */     PlacementUtils.register(context, WARM_OCEAN_VEGETATION, warmOceanVegetation, new PlacementModifier[] {
/* 86 */           NoiseBasedCountPlacement.of(20, 400.0D, 0.0D), 
/* 87 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*    */           
/* 89 */           BiomeFilter.biome()
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\placement\AquaticPlacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */