/*     */ package net.minecraft.data.worldgen.placement;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.features.NetherFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.valueproviders.BiasedToBottomInt;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ 
/*     */ public class NetherPlacements
/*     */ {
/*  22 */   public static final ResourceKey<PlacedFeature> DELTA = PlacementUtils.createKey("delta");
/*  23 */   public static final ResourceKey<PlacedFeature> SMALL_BASALT_COLUMNS = PlacementUtils.createKey("small_basalt_columns");
/*  24 */   public static final ResourceKey<PlacedFeature> LARGE_BASALT_COLUMNS = PlacementUtils.createKey("large_basalt_columns");
/*  25 */   public static final ResourceKey<PlacedFeature> BASALT_BLOBS = PlacementUtils.createKey("basalt_blobs");
/*  26 */   public static final ResourceKey<PlacedFeature> BLACKSTONE_BLOBS = PlacementUtils.createKey("blackstone_blobs");
/*     */   
/*  28 */   public static final ResourceKey<PlacedFeature> GLOWSTONE_EXTRA = PlacementUtils.createKey("glowstone_extra");
/*  29 */   public static final ResourceKey<PlacedFeature> GLOWSTONE = PlacementUtils.createKey("glowstone");
/*     */   
/*  31 */   public static final ResourceKey<PlacedFeature> CRIMSON_FOREST_VEGETATION = PlacementUtils.createKey("crimson_forest_vegetation");
/*  32 */   public static final ResourceKey<PlacedFeature> WARPED_FOREST_VEGETATION = PlacementUtils.createKey("warped_forest_vegetation");
/*  33 */   public static final ResourceKey<PlacedFeature> NETHER_SPROUTS = PlacementUtils.createKey("nether_sprouts");
/*  34 */   public static final ResourceKey<PlacedFeature> TWISTING_VINES = PlacementUtils.createKey("twisting_vines");
/*  35 */   public static final ResourceKey<PlacedFeature> WEEPING_VINES = PlacementUtils.createKey("weeping_vines");
/*  36 */   public static final ResourceKey<PlacedFeature> PATCH_CRIMSON_ROOTS = PlacementUtils.createKey("patch_crimson_roots");
/*     */   
/*  38 */   public static final ResourceKey<PlacedFeature> BASALT_PILLAR = PlacementUtils.createKey("basalt_pillar");
/*     */   
/*  40 */   public static final ResourceKey<PlacedFeature> SPRING_DELTA = PlacementUtils.createKey("spring_delta");
/*  41 */   public static final ResourceKey<PlacedFeature> SPRING_CLOSED = PlacementUtils.createKey("spring_closed");
/*  42 */   public static final ResourceKey<PlacedFeature> SPRING_CLOSED_DOUBLE = PlacementUtils.createKey("spring_closed_double");
/*  43 */   public static final ResourceKey<PlacedFeature> SPRING_OPEN = PlacementUtils.createKey("spring_open");
/*     */   
/*  45 */   public static final ResourceKey<PlacedFeature> PATCH_SOUL_FIRE = PlacementUtils.createKey("patch_soul_fire");
/*  46 */   public static final ResourceKey<PlacedFeature> PATCH_FIRE = PlacementUtils.createKey("patch_fire");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> context) {
/*  49 */     HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
/*  50 */     Holder.Reference reference1 = configuredFeatures.getOrThrow(NetherFeatures.DELTA);
/*  51 */     Holder.Reference reference2 = configuredFeatures.getOrThrow(NetherFeatures.SMALL_BASALT_COLUMNS);
/*  52 */     Holder.Reference reference3 = configuredFeatures.getOrThrow(NetherFeatures.LARGE_BASALT_COLUMNS);
/*  53 */     Holder.Reference reference4 = configuredFeatures.getOrThrow(NetherFeatures.BASALT_BLOBS);
/*  54 */     Holder.Reference reference5 = configuredFeatures.getOrThrow(NetherFeatures.BLACKSTONE_BLOBS);
/*  55 */     Holder.Reference reference6 = configuredFeatures.getOrThrow(NetherFeatures.GLOWSTONE_EXTRA);
/*  56 */     Holder.Reference reference7 = configuredFeatures.getOrThrow(NetherFeatures.CRIMSON_FOREST_VEGETATION);
/*  57 */     Holder.Reference reference8 = configuredFeatures.getOrThrow(NetherFeatures.WARPED_FOREST_VEGETION);
/*  58 */     Holder.Reference reference9 = configuredFeatures.getOrThrow(NetherFeatures.NETHER_SPROUTS);
/*  59 */     Holder.Reference reference10 = configuredFeatures.getOrThrow(NetherFeatures.TWISTING_VINES);
/*  60 */     Holder.Reference reference11 = configuredFeatures.getOrThrow(NetherFeatures.WEEPING_VINES);
/*  61 */     Holder.Reference reference12 = configuredFeatures.getOrThrow(NetherFeatures.PATCH_CRIMSON_ROOTS);
/*  62 */     Holder.Reference reference13 = configuredFeatures.getOrThrow(NetherFeatures.BASALT_PILLAR);
/*  63 */     Holder.Reference reference14 = configuredFeatures.getOrThrow(NetherFeatures.SPRING_LAVA_NETHER);
/*  64 */     Holder.Reference reference15 = configuredFeatures.getOrThrow(NetherFeatures.SPRING_NETHER_CLOSED);
/*  65 */     Holder.Reference reference16 = configuredFeatures.getOrThrow(NetherFeatures.SPRING_NETHER_OPEN);
/*  66 */     Holder.Reference reference17 = configuredFeatures.getOrThrow(NetherFeatures.PATCH_SOUL_FIRE);
/*  67 */     Holder.Reference reference18 = configuredFeatures.getOrThrow(NetherFeatures.PATCH_FIRE);
/*     */     
/*  69 */     PlacementUtils.register(context, DELTA, reference1, new PlacementModifier[] {
/*  70 */           CountOnEveryLayerPlacement.of(40), 
/*  71 */           BiomeFilter.biome()
/*     */         });
/*  73 */     PlacementUtils.register(context, SMALL_BASALT_COLUMNS, reference2, new PlacementModifier[] {
/*  74 */           CountOnEveryLayerPlacement.of(4), 
/*  75 */           BiomeFilter.biome()
/*     */         });
/*  77 */     PlacementUtils.register(context, LARGE_BASALT_COLUMNS, reference3, new PlacementModifier[] {
/*  78 */           CountOnEveryLayerPlacement.of(2), 
/*  79 */           BiomeFilter.biome()
/*     */         });
/*  81 */     PlacementUtils.register(context, BASALT_BLOBS, reference4, new PlacementModifier[] {
/*  82 */           CountPlacement.of(75), 
/*  83 */           InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/*  85 */           BiomeFilter.biome()
/*     */         });
/*  87 */     PlacementUtils.register(context, BLACKSTONE_BLOBS, reference5, new PlacementModifier[] {
/*  88 */           CountPlacement.of(25), 
/*  89 */           InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/*  91 */           BiomeFilter.biome()
/*     */         });
/*     */     
/*  94 */     PlacementUtils.register(context, GLOWSTONE_EXTRA, reference6, new PlacementModifier[] {
/*  95 */           CountPlacement.of(BiasedToBottomInt.of(0, 9)), 
/*  96 */           InSquarePlacement.spread(), PlacementUtils.RANGE_4_4, 
/*     */           
/*  98 */           BiomeFilter.biome()
/*     */         });
/* 100 */     PlacementUtils.register(context, GLOWSTONE, reference6, new PlacementModifier[] {
/* 101 */           CountPlacement.of(10), 
/* 102 */           InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 104 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 107 */     PlacementUtils.register(context, CRIMSON_FOREST_VEGETATION, reference7, new PlacementModifier[] {
/* 108 */           CountOnEveryLayerPlacement.of(6), 
/* 109 */           BiomeFilter.biome()
/*     */         });
/* 111 */     PlacementUtils.register(context, WARPED_FOREST_VEGETATION, reference8, new PlacementModifier[] {
/* 112 */           CountOnEveryLayerPlacement.of(5), 
/* 113 */           BiomeFilter.biome()
/*     */         });
/* 115 */     PlacementUtils.register(context, NETHER_SPROUTS, reference9, new PlacementModifier[] {
/* 116 */           CountOnEveryLayerPlacement.of(4), 
/* 117 */           BiomeFilter.biome()
/*     */         });
/* 119 */     PlacementUtils.register(context, TWISTING_VINES, reference10, new PlacementModifier[] {
/* 120 */           CountPlacement.of(10), 
/* 121 */           InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 123 */           BiomeFilter.biome()
/*     */         });
/* 125 */     PlacementUtils.register(context, WEEPING_VINES, reference11, new PlacementModifier[] {
/* 126 */           CountPlacement.of(10), 
/* 127 */           InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 129 */           BiomeFilter.biome()
/*     */         });
/* 131 */     PlacementUtils.register(context, PATCH_CRIMSON_ROOTS, reference12, new PlacementModifier[] { PlacementUtils.FULL_RANGE, 
/*     */           
/* 133 */           BiomeFilter.biome() });
/*     */ 
/*     */     
/* 136 */     PlacementUtils.register(context, BASALT_PILLAR, reference13, new PlacementModifier[] {
/* 137 */           CountPlacement.of(10), 
/* 138 */           InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 140 */           BiomeFilter.biome()
/*     */         });
/*     */     
/* 143 */     PlacementUtils.register(context, SPRING_DELTA, reference14, new PlacementModifier[] {
/* 144 */           CountPlacement.of(16), 
/* 145 */           InSquarePlacement.spread(), PlacementUtils.RANGE_4_4, 
/*     */           
/* 147 */           BiomeFilter.biome()
/*     */         });
/* 149 */     PlacementUtils.register(context, SPRING_CLOSED, reference15, new PlacementModifier[] {
/* 150 */           CountPlacement.of(16), 
/* 151 */           InSquarePlacement.spread(), PlacementUtils.RANGE_10_10, 
/*     */           
/* 153 */           BiomeFilter.biome()
/*     */         });
/* 155 */     PlacementUtils.register(context, SPRING_CLOSED_DOUBLE, reference15, new PlacementModifier[] {
/* 156 */           CountPlacement.of(32), 
/* 157 */           InSquarePlacement.spread(), PlacementUtils.RANGE_10_10, 
/*     */           
/* 159 */           BiomeFilter.biome()
/*     */         });
/* 161 */     PlacementUtils.register(context, SPRING_OPEN, reference16, new PlacementModifier[] {
/* 162 */           CountPlacement.of(8), 
/* 163 */           InSquarePlacement.spread(), PlacementUtils.RANGE_4_4, 
/*     */           
/* 165 */           BiomeFilter.biome()
/*     */         });
/*     */ 
/*     */     
/* 169 */     List<PlacementModifier> firePlacement = List.of(
/* 170 */         CountPlacement.of(UniformInt.of(0, 5)), 
/* 171 */         InSquarePlacement.spread(), PlacementUtils.RANGE_4_4, 
/*     */         
/* 173 */         BiomeFilter.biome());
/*     */     
/* 175 */     PlacementUtils.register(context, PATCH_SOUL_FIRE, reference17, firePlacement);
/*     */ 
/*     */     
/* 178 */     PlacementUtils.register(context, PATCH_FIRE, reference18, firePlacement);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\placement\NetherPlacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */