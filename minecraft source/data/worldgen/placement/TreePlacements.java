/*     */ package net.minecraft.data.worldgen.placement;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.features.TreeFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ 
/*     */ public class TreePlacements {
/*  23 */   public static final ResourceKey<PlacedFeature> CRIMSON_FUNGI = PlacementUtils.createKey("crimson_fungi");
/*  24 */   public static final ResourceKey<PlacedFeature> WARPED_FUNGI = PlacementUtils.createKey("warped_fungi");
/*     */   
/*  26 */   public static final ResourceKey<PlacedFeature> OAK_CHECKED = PlacementUtils.createKey("oak_checked");
/*  27 */   public static final ResourceKey<PlacedFeature> DARK_OAK_CHECKED = PlacementUtils.createKey("dark_oak_checked");
/*  28 */   public static final ResourceKey<PlacedFeature> PALE_OAK_CHECKED = PlacementUtils.createKey("pale_oak_checked");
/*  29 */   public static final ResourceKey<PlacedFeature> PALE_OAK_CREAKING_CHECKED = PlacementUtils.createKey("pale_oak_creaking_checked");
/*  30 */   public static final ResourceKey<PlacedFeature> BIRCH_CHECKED = PlacementUtils.createKey("birch_checked");
/*  31 */   public static final ResourceKey<PlacedFeature> ACACIA_CHECKED = PlacementUtils.createKey("acacia_checked");
/*  32 */   public static final ResourceKey<PlacedFeature> SPRUCE_CHECKED = PlacementUtils.createKey("spruce_checked");
/*  33 */   public static final ResourceKey<PlacedFeature> MANGROVE_CHECKED = PlacementUtils.createKey("mangrove_checked");
/*  34 */   public static final ResourceKey<PlacedFeature> CHERRY_CHECKED = PlacementUtils.createKey("cherry_checked");
/*     */   
/*  36 */   public static final ResourceKey<PlacedFeature> PINE_ON_SNOW = PlacementUtils.createKey("pine_on_snow");
/*  37 */   public static final ResourceKey<PlacedFeature> SPRUCE_ON_SNOW = PlacementUtils.createKey("spruce_on_snow");
/*     */   
/*  39 */   public static final ResourceKey<PlacedFeature> PINE_CHECKED = PlacementUtils.createKey("pine_checked");
/*  40 */   public static final ResourceKey<PlacedFeature> JUNGLE_TREE_CHECKED = PlacementUtils.createKey("jungle_tree");
/*  41 */   public static final ResourceKey<PlacedFeature> FANCY_OAK_CHECKED = PlacementUtils.createKey("fancy_oak_checked");
/*  42 */   public static final ResourceKey<PlacedFeature> MEGA_JUNGLE_TREE_CHECKED = PlacementUtils.createKey("mega_jungle_tree_checked");
/*  43 */   public static final ResourceKey<PlacedFeature> MEGA_SPRUCE_CHECKED = PlacementUtils.createKey("mega_spruce_checked");
/*  44 */   public static final ResourceKey<PlacedFeature> MEGA_PINE_CHECKED = PlacementUtils.createKey("mega_pine_checked");
/*  45 */   public static final ResourceKey<PlacedFeature> TALL_MANGROVE_CHECKED = PlacementUtils.createKey("tall_mangrove_checked");
/*     */   
/*  47 */   public static final ResourceKey<PlacedFeature> JUNGLE_BUSH = PlacementUtils.createKey("jungle_bush");
/*     */   
/*  49 */   public static final ResourceKey<PlacedFeature> SUPER_BIRCH_BEES_0002 = PlacementUtils.createKey("super_birch_bees_0002");
/*  50 */   public static final ResourceKey<PlacedFeature> SUPER_BIRCH_BEES = PlacementUtils.createKey("super_birch_bees");
/*     */   
/*  52 */   public static final ResourceKey<PlacedFeature> OAK_BEES_0002_LEAF_LITTER = PlacementUtils.createKey("oak_bees_0002_leaf_litter");
/*  53 */   public static final ResourceKey<PlacedFeature> OAK_BEES_002 = PlacementUtils.createKey("oak_bees_002");
/*  54 */   public static final ResourceKey<PlacedFeature> BIRCH_BEES_0002_PLACED = PlacementUtils.createKey("birch_bees_0002");
/*  55 */   public static final ResourceKey<PlacedFeature> BIRCH_BEES_0002_LEAF_LITTER = PlacementUtils.createKey("birch_bees_0002_leaf_litter");
/*  56 */   public static final ResourceKey<PlacedFeature> BIRCH_BEES_002 = PlacementUtils.createKey("birch_bees_002");
/*  57 */   public static final ResourceKey<PlacedFeature> FANCY_OAK_BEES_0002_LEAF_LITTER = PlacementUtils.createKey("fancy_oak_bees_0002_leaf_litter");
/*  58 */   public static final ResourceKey<PlacedFeature> FANCY_OAK_BEES_002 = PlacementUtils.createKey("fancy_oak_bees_002");
/*  59 */   public static final ResourceKey<PlacedFeature> FANCY_OAK_BEES = PlacementUtils.createKey("fancy_oak_bees");
/*  60 */   public static final ResourceKey<PlacedFeature> CHERRY_BEES_005 = PlacementUtils.createKey("cherry_bees_005");
/*     */   
/*  62 */   public static final ResourceKey<PlacedFeature> OAK_LEAF_LITTER = PlacementUtils.createKey("oak_leaf_litter");
/*  63 */   public static final ResourceKey<PlacedFeature> DARK_OAK_LEAF_LITTER = PlacementUtils.createKey("dark_oak_leaf_litter");
/*  64 */   public static final ResourceKey<PlacedFeature> BIRCH_LEAF_LITTER = PlacementUtils.createKey("birch_leaf_litter");
/*  65 */   public static final ResourceKey<PlacedFeature> FANCY_OAK_LEAF_LITTER = PlacementUtils.createKey("fancy_oak_leaf_litter");
/*     */   
/*  67 */   public static final ResourceKey<PlacedFeature> FALLEN_OAK_TREE = PlacementUtils.createKey("fallen_oak_tree");
/*  68 */   public static final ResourceKey<PlacedFeature> FALLEN_BIRCH_TREE = PlacementUtils.createKey("fallen_birch_tree");
/*  69 */   public static final ResourceKey<PlacedFeature> FALLEN_SUPER_BIRCH_TREE = PlacementUtils.createKey("fallen_super_birch_tree");
/*  70 */   public static final ResourceKey<PlacedFeature> FALLEN_SPRUCE_TREE = PlacementUtils.createKey("fallen_spruce_tree");
/*  71 */   public static final ResourceKey<PlacedFeature> FALLEN_JUNGLE_TREE = PlacementUtils.createKey("fallen_jungle_tree");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> context) {
/*  74 */     HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
/*  75 */     Holder.Reference reference1 = configuredFeatures.getOrThrow(TreeFeatures.CRIMSON_FUNGUS);
/*  76 */     Holder.Reference reference2 = configuredFeatures.getOrThrow(TreeFeatures.WARPED_FUNGUS);
/*  77 */     Holder.Reference reference3 = configuredFeatures.getOrThrow(TreeFeatures.OAK);
/*  78 */     Holder.Reference reference4 = configuredFeatures.getOrThrow(TreeFeatures.DARK_OAK);
/*  79 */     Holder.Reference reference5 = configuredFeatures.getOrThrow(TreeFeatures.PALE_OAK);
/*  80 */     Holder.Reference reference6 = configuredFeatures.getOrThrow(TreeFeatures.PALE_OAK_CREAKING);
/*  81 */     Holder.Reference reference7 = configuredFeatures.getOrThrow(TreeFeatures.BIRCH);
/*  82 */     Holder.Reference reference8 = configuredFeatures.getOrThrow(TreeFeatures.ACACIA);
/*  83 */     Holder.Reference reference9 = configuredFeatures.getOrThrow(TreeFeatures.SPRUCE);
/*  84 */     Holder.Reference reference10 = configuredFeatures.getOrThrow(TreeFeatures.MANGROVE);
/*  85 */     Holder.Reference reference11 = configuredFeatures.getOrThrow(TreeFeatures.CHERRY);
/*  86 */     Holder.Reference reference12 = configuredFeatures.getOrThrow(TreeFeatures.PINE);
/*  87 */     Holder.Reference reference13 = configuredFeatures.getOrThrow(TreeFeatures.JUNGLE_TREE);
/*  88 */     Holder.Reference reference14 = configuredFeatures.getOrThrow(TreeFeatures.FANCY_OAK);
/*  89 */     Holder.Reference reference15 = configuredFeatures.getOrThrow(TreeFeatures.MEGA_JUNGLE_TREE);
/*  90 */     Holder.Reference reference16 = configuredFeatures.getOrThrow(TreeFeatures.MEGA_SPRUCE);
/*  91 */     Holder.Reference reference17 = configuredFeatures.getOrThrow(TreeFeatures.MEGA_PINE);
/*  92 */     Holder.Reference reference18 = configuredFeatures.getOrThrow(TreeFeatures.TALL_MANGROVE);
/*  93 */     Holder.Reference reference19 = configuredFeatures.getOrThrow(TreeFeatures.JUNGLE_BUSH);
/*  94 */     Holder.Reference reference20 = configuredFeatures.getOrThrow(TreeFeatures.SUPER_BIRCH_BEES_0002);
/*  95 */     Holder.Reference reference21 = configuredFeatures.getOrThrow(TreeFeatures.SUPER_BIRCH_BEES);
/*  96 */     Holder.Reference reference22 = configuredFeatures.getOrThrow(TreeFeatures.OAK_BEES_0002_LEAF_LITTER);
/*  97 */     Holder.Reference reference23 = configuredFeatures.getOrThrow(TreeFeatures.OAK_BEES_002);
/*  98 */     Holder.Reference reference24 = configuredFeatures.getOrThrow(TreeFeatures.BIRCH_BEES_0002);
/*  99 */     Holder.Reference reference25 = configuredFeatures.getOrThrow(TreeFeatures.BIRCH_BEES_0002_LEAF_LITTER);
/* 100 */     Holder.Reference reference26 = configuredFeatures.getOrThrow(TreeFeatures.BIRCH_BEES_002);
/* 101 */     Holder.Reference reference27 = configuredFeatures.getOrThrow(TreeFeatures.FANCY_OAK_BEES_0002_LEAF_LITTER);
/* 102 */     Holder.Reference reference28 = configuredFeatures.getOrThrow(TreeFeatures.FANCY_OAK_BEES_002);
/* 103 */     Holder.Reference reference29 = configuredFeatures.getOrThrow(TreeFeatures.FANCY_OAK_BEES);
/* 104 */     Holder.Reference reference30 = configuredFeatures.getOrThrow(TreeFeatures.CHERRY_BEES_005);
/* 105 */     Holder.Reference reference31 = configuredFeatures.getOrThrow(TreeFeatures.OAK_LEAF_LITTER);
/* 106 */     Holder.Reference reference32 = configuredFeatures.getOrThrow(TreeFeatures.DARK_OAK_LEAF_LITTER);
/* 107 */     Holder.Reference reference33 = configuredFeatures.getOrThrow(TreeFeatures.BIRCH_LEAF_LITTER);
/* 108 */     Holder.Reference reference34 = configuredFeatures.getOrThrow(TreeFeatures.FANCY_OAK_LEAF_LITTER);
/* 109 */     Holder.Reference reference35 = configuredFeatures.getOrThrow(TreeFeatures.FALLEN_OAK_TREE);
/* 110 */     Holder.Reference reference36 = configuredFeatures.getOrThrow(TreeFeatures.FALLEN_BIRCH_TREE);
/* 111 */     Holder.Reference reference37 = configuredFeatures.getOrThrow(TreeFeatures.FALLEN_SUPER_BIRCH_TREE);
/* 112 */     Holder.Reference reference38 = configuredFeatures.getOrThrow(TreeFeatures.FALLEN_SPRUCE_TREE);
/* 113 */     Holder.Reference reference39 = configuredFeatures.getOrThrow(TreeFeatures.FALLEN_JUNGLE_TREE);
/*     */     
/* 115 */     PlacementUtils.register(context, CRIMSON_FUNGI, reference1, new PlacementModifier[] {
/* 116 */           CountOnEveryLayerPlacement.of(8), 
/* 117 */           BiomeFilter.biome()
/*     */         });
/* 119 */     PlacementUtils.register(context, WARPED_FUNGI, reference2, new PlacementModifier[] {
/* 120 */           CountOnEveryLayerPlacement.of(8), 
/* 121 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 124 */     PlacementUtils.register(context, OAK_CHECKED, reference3, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING) });
/* 125 */     PlacementUtils.register(context, DARK_OAK_CHECKED, reference4, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.DARK_OAK_SAPLING) });
/* 126 */     PlacementUtils.register(context, PALE_OAK_CHECKED, reference5, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.PALE_OAK_SAPLING) });
/* 127 */     PlacementUtils.register(context, PALE_OAK_CREAKING_CHECKED, reference6, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.PALE_OAK_SAPLING) });
/* 128 */     PlacementUtils.register(context, BIRCH_CHECKED, reference7, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING) });
/* 129 */     PlacementUtils.register(context, ACACIA_CHECKED, reference8, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.ACACIA_SAPLING) });
/* 130 */     PlacementUtils.register(context, SPRUCE_CHECKED, reference9, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING) });
/* 131 */     PlacementUtils.register(context, MANGROVE_CHECKED, reference10, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.MANGROVE_PROPAGULE) });
/* 132 */     PlacementUtils.register(context, CHERRY_CHECKED, reference11, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.CHERRY_SAPLING) });
/*     */     
/* 134 */     BlockPredicate snowTreePredicate = BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), new Block[] { Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW });
/* 135 */     List<PlacementModifier> snowTreeFilterDecorator = List.of(
/* 136 */         EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.not(BlockPredicate.matchesBlocks(new Block[] { Blocks.POWDER_SNOW }, )), 8), 
/* 137 */         BlockPredicateFilter.forPredicate(snowTreePredicate));
/*     */     
/* 139 */     PlacementUtils.register(context, PINE_ON_SNOW, reference12, snowTreeFilterDecorator);
/* 140 */     PlacementUtils.register(context, SPRUCE_ON_SNOW, reference9, snowTreeFilterDecorator);
/*     */     
/* 142 */     PlacementUtils.register(context, PINE_CHECKED, reference12, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING) });
/* 143 */     PlacementUtils.register(context, JUNGLE_TREE_CHECKED, reference13, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.JUNGLE_SAPLING) });
/* 144 */     PlacementUtils.register(context, FANCY_OAK_CHECKED, reference14, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING) });
/* 145 */     PlacementUtils.register(context, MEGA_JUNGLE_TREE_CHECKED, reference15, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.JUNGLE_SAPLING) });
/* 146 */     PlacementUtils.register(context, MEGA_SPRUCE_CHECKED, reference16, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING) });
/* 147 */     PlacementUtils.register(context, MEGA_PINE_CHECKED, reference17, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING) });
/* 148 */     PlacementUtils.register(context, TALL_MANGROVE_CHECKED, reference18, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.MANGROVE_PROPAGULE) });
/*     */     
/* 150 */     PlacementUtils.register(context, JUNGLE_BUSH, reference19, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING) });
/*     */     
/* 152 */     PlacementUtils.register(context, SUPER_BIRCH_BEES_0002, reference20, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING) });
/* 153 */     PlacementUtils.register(context, SUPER_BIRCH_BEES, reference21, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING) });
/*     */     
/* 155 */     PlacementUtils.register(context, OAK_BEES_0002_LEAF_LITTER, reference22, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING) });
/* 156 */     PlacementUtils.register(context, OAK_BEES_002, reference23, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING) });
/* 157 */     PlacementUtils.register(context, BIRCH_BEES_0002_PLACED, reference24, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING) });
/* 158 */     PlacementUtils.register(context, BIRCH_BEES_0002_LEAF_LITTER, reference25, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING) });
/* 159 */     PlacementUtils.register(context, BIRCH_BEES_002, reference26, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING) });
/* 160 */     PlacementUtils.register(context, FANCY_OAK_BEES_0002_LEAF_LITTER, reference27, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING) });
/* 161 */     PlacementUtils.register(context, FANCY_OAK_BEES_002, reference28, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING) });
/* 162 */     PlacementUtils.register(context, FANCY_OAK_BEES, reference29, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING) });
/* 163 */     PlacementUtils.register(context, CHERRY_BEES_005, reference30, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.CHERRY_SAPLING) });
/*     */     
/* 165 */     PlacementUtils.register(context, OAK_LEAF_LITTER, reference31, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING) });
/* 166 */     PlacementUtils.register(context, DARK_OAK_LEAF_LITTER, reference32, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.DARK_OAK_SAPLING) });
/* 167 */     PlacementUtils.register(context, BIRCH_LEAF_LITTER, reference33, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING) });
/* 168 */     PlacementUtils.register(context, FANCY_OAK_LEAF_LITTER, reference34, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING) });
/*     */     
/* 170 */     PlacementUtils.register(context, FALLEN_OAK_TREE, reference35, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING) });
/* 171 */     PlacementUtils.register(context, FALLEN_BIRCH_TREE, reference36, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING) });
/* 172 */     PlacementUtils.register(context, FALLEN_SUPER_BIRCH_TREE, reference37, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING) });
/* 173 */     PlacementUtils.register(context, FALLEN_SPRUCE_TREE, reference38, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING) });
/* 174 */     PlacementUtils.register(context, FALLEN_JUNGLE_TREE, reference39, new PlacementModifier[] { PlacementUtils.filteredByBlockSurvival(Blocks.JUNGLE_SAPLING) });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\placement\TreePlacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */