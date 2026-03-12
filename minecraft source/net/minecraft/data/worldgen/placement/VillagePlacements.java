/*    */ package net.minecraft.data.worldgen.placement;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.data.worldgen.features.PileFeatures;
/*    */ import net.minecraft.data.worldgen.features.TreeFeatures;
/*    */ import net.minecraft.data.worldgen.features.VegetationFeatures;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*    */ 
/*    */ 
/*    */ public class VillagePlacements
/*    */ {
/* 19 */   public static final ResourceKey<PlacedFeature> PILE_HAY_VILLAGE = PlacementUtils.createKey("pile_hay");
/* 20 */   public static final ResourceKey<PlacedFeature> PILE_MELON_VILLAGE = PlacementUtils.createKey("pile_melon");
/* 21 */   public static final ResourceKey<PlacedFeature> PILE_SNOW_VILLAGE = PlacementUtils.createKey("pile_snow");
/* 22 */   public static final ResourceKey<PlacedFeature> PILE_ICE_VILLAGE = PlacementUtils.createKey("pile_ice");
/* 23 */   public static final ResourceKey<PlacedFeature> PILE_PUMPKIN_VILLAGE = PlacementUtils.createKey("pile_pumpkin");
/*    */   
/* 25 */   public static final ResourceKey<PlacedFeature> OAK_VILLAGE = PlacementUtils.createKey("oak");
/* 26 */   public static final ResourceKey<PlacedFeature> ACACIA_VILLAGE = PlacementUtils.createKey("acacia");
/* 27 */   public static final ResourceKey<PlacedFeature> SPRUCE_VILLAGE = PlacementUtils.createKey("spruce");
/* 28 */   public static final ResourceKey<PlacedFeature> PINE_VILLAGE = PlacementUtils.createKey("pine");
/*    */   
/* 30 */   public static final ResourceKey<PlacedFeature> PATCH_CACTUS_VILLAGE = PlacementUtils.createKey("patch_cactus");
/* 31 */   public static final ResourceKey<PlacedFeature> FLOWER_PLAIN_VILLAGE = PlacementUtils.createKey("flower_plain");
/* 32 */   public static final ResourceKey<PlacedFeature> PATCH_TAIGA_GRASS_VILLAGE = PlacementUtils.createKey("patch_taiga_grass");
/* 33 */   public static final ResourceKey<PlacedFeature> PATCH_BERRY_BUSH_VILLAGE = PlacementUtils.createKey("patch_berry_bush");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<PlacedFeature> context) {
/* 36 */     HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
/* 37 */     Holder.Reference reference1 = configuredFeatures.getOrThrow(PileFeatures.PILE_HAY);
/* 38 */     Holder.Reference reference2 = configuredFeatures.getOrThrow(PileFeatures.PILE_MELON);
/* 39 */     Holder.Reference reference3 = configuredFeatures.getOrThrow(PileFeatures.PILE_SNOW);
/* 40 */     Holder.Reference reference4 = configuredFeatures.getOrThrow(PileFeatures.PILE_ICE);
/* 41 */     Holder.Reference reference5 = configuredFeatures.getOrThrow(PileFeatures.PILE_PUMPKIN);
/* 42 */     Holder.Reference reference6 = configuredFeatures.getOrThrow(TreeFeatures.OAK);
/* 43 */     Holder.Reference reference7 = configuredFeatures.getOrThrow(TreeFeatures.ACACIA);
/* 44 */     Holder.Reference reference8 = configuredFeatures.getOrThrow(TreeFeatures.SPRUCE);
/* 45 */     Holder.Reference reference9 = configuredFeatures.getOrThrow(TreeFeatures.PINE);
/* 46 */     Holder.Reference reference10 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_CACTUS);
/* 47 */     Holder.Reference reference11 = configuredFeatures.getOrThrow(VegetationFeatures.FLOWER_PLAIN);
/* 48 */     Holder.Reference reference12 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_TAIGA_GRASS);
/* 49 */     Holder.Reference reference13 = configuredFeatures.getOrThrow(VegetationFeatures.PATCH_BERRY_BUSH);
/*    */     
/* 51 */     PlacementUtils.register(context, PILE_HAY_VILLAGE, reference1, new PlacementModifier[0]);
/* 52 */     PlacementUtils.register(context, PILE_MELON_VILLAGE, reference2, new PlacementModifier[0]);
/* 53 */     PlacementUtils.register(context, PILE_SNOW_VILLAGE, reference3, new PlacementModifier[0]);
/* 54 */     PlacementUtils.register(context, PILE_ICE_VILLAGE, reference4, new PlacementModifier[0]);
/* 55 */     PlacementUtils.register(context, PILE_PUMPKIN_VILLAGE, reference5, new PlacementModifier[0]);
/*    */     
/* 57 */     PlacementUtils.register(context, OAK_VILLAGE, reference6, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING) });
/* 58 */     PlacementUtils.register(context, ACACIA_VILLAGE, reference7, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.ACACIA_SAPLING) });
/* 59 */     PlacementUtils.register(context, SPRUCE_VILLAGE, reference8, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING) });
/* 60 */     PlacementUtils.register(context, PINE_VILLAGE, reference9, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING) });
/*    */     
/* 62 */     PlacementUtils.register(context, PATCH_CACTUS_VILLAGE, reference10, new PlacementModifier[0]);
/* 63 */     PlacementUtils.register(context, FLOWER_PLAIN_VILLAGE, reference11, new PlacementModifier[0]);
/* 64 */     PlacementUtils.register(context, PATCH_TAIGA_GRASS_VILLAGE, reference12, new PlacementModifier[0]);
/* 65 */     PlacementUtils.register(context, PATCH_BERRY_BUSH_VILLAGE, reference13, new PlacementModifier[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\placement\VillagePlacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */