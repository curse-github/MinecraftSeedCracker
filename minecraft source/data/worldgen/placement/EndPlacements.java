/*    */ package net.minecraft.data.worldgen.placement;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.data.worldgen.features.EndFeatures;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*    */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*    */ import net.minecraft.world.level.levelgen.placement.FixedPlacement;
/*    */ import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
/*    */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*    */ import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
/*    */ import net.minecraft.world.level.levelgen.placement.RarityFilter;
/*    */ 
/*    */ public class EndPlacements {
/* 23 */   public static final ResourceKey<PlacedFeature> END_PLATFORM = PlacementUtils.createKey("end_platform");
/* 24 */   public static final ResourceKey<PlacedFeature> END_SPIKE = PlacementUtils.createKey("end_spike");
/* 25 */   public static final ResourceKey<PlacedFeature> END_GATEWAY_RETURN = PlacementUtils.createKey("end_gateway_return");
/* 26 */   public static final ResourceKey<PlacedFeature> CHORUS_PLANT = PlacementUtils.createKey("chorus_plant");
/* 27 */   public static final ResourceKey<PlacedFeature> END_ISLAND_DECORATED = PlacementUtils.createKey("end_island_decorated");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<PlacedFeature> context) {
/* 30 */     HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
/* 31 */     Holder.Reference reference1 = configuredFeatures.getOrThrow(EndFeatures.END_PLATFORM);
/* 32 */     Holder.Reference reference2 = configuredFeatures.getOrThrow(EndFeatures.END_SPIKE);
/* 33 */     Holder.Reference reference3 = configuredFeatures.getOrThrow(EndFeatures.END_GATEWAY_RETURN);
/* 34 */     Holder.Reference reference4 = configuredFeatures.getOrThrow(EndFeatures.CHORUS_PLANT);
/* 35 */     Holder.Reference reference5 = configuredFeatures.getOrThrow(EndFeatures.END_ISLAND);
/*    */     
/* 37 */     PlacementUtils.register(context, END_PLATFORM, reference1, new PlacementModifier[] {
/* 38 */           FixedPlacement.of(new BlockPos[] { ServerLevel.END_SPAWN_POINT.below()
/* 39 */             }), BiomeFilter.biome()
/*    */         });
/* 41 */     PlacementUtils.register(context, END_SPIKE, reference2, new PlacementModifier[] {
/* 42 */           BiomeFilter.biome()
/*    */         });
/* 44 */     PlacementUtils.register(context, END_GATEWAY_RETURN, reference3, new PlacementModifier[] {
/* 45 */           RarityFilter.onAverageOnceEvery(700), 
/* 46 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*    */           
/* 48 */           RandomOffsetPlacement.vertical(UniformInt.of(3, 9)), 
/* 49 */           BiomeFilter.biome()
/*    */         });
/* 51 */     PlacementUtils.register(context, CHORUS_PLANT, reference4, new PlacementModifier[] {
/* 52 */           CountPlacement.of(UniformInt.of(0, 4)), 
/* 53 */           InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*    */           
/* 55 */           BiomeFilter.biome()
/*    */         });
/* 57 */     PlacementUtils.register(context, END_ISLAND_DECORATED, reference5, new PlacementModifier[] {
/* 58 */           RarityFilter.onAverageOnceEvery(14), 
/* 59 */           PlacementUtils.countExtra(1, 0.25F, 1), 
/* 60 */           InSquarePlacement.spread(), 
/* 61 */           HeightRangePlacement.uniform(VerticalAnchor.absolute(55), VerticalAnchor.absolute(70)), 
/* 62 */           BiomeFilter.biome()
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\placement\EndPlacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */