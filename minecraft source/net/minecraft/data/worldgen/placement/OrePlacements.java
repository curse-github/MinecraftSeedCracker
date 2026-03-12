/*     */ package net.minecraft.data.worldgen.placement;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.features.OreFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ import net.minecraft.world.level.levelgen.placement.RarityFilter;
/*     */ 
/*     */ public class OrePlacements
/*     */ {
/*     */   private static List<PlacementModifier> orePlacement(PlacementModifier frequencyModifier, PlacementModifier heightRange) {
/*  24 */     return List.of(frequencyModifier, 
/*     */         
/*  26 */         InSquarePlacement.spread(), heightRange, 
/*     */         
/*  28 */         BiomeFilter.biome());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  33 */   private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange) { return orePlacement(CountPlacement.of(count), heightRange); }
/*     */ 
/*     */ 
/*     */   
/*  37 */   private static List<PlacementModifier> rareOrePlacement(int rarity, PlacementModifier heightRange) { return orePlacement(RarityFilter.onAverageOnceEvery(rarity), heightRange); }
/*     */ 
/*     */   
/*  40 */   public static final ResourceKey<PlacedFeature> ORE_MAGMA = PlacementUtils.createKey("ore_magma");
/*  41 */   public static final ResourceKey<PlacedFeature> ORE_SOUL_SAND = PlacementUtils.createKey("ore_soul_sand");
/*  42 */   public static final ResourceKey<PlacedFeature> ORE_GOLD_DELTAS = PlacementUtils.createKey("ore_gold_deltas");
/*  43 */   public static final ResourceKey<PlacedFeature> ORE_QUARTZ_DELTAS = PlacementUtils.createKey("ore_quartz_deltas");
/*  44 */   public static final ResourceKey<PlacedFeature> ORE_GOLD_NETHER = PlacementUtils.createKey("ore_gold_nether");
/*  45 */   public static final ResourceKey<PlacedFeature> ORE_QUARTZ_NETHER = PlacementUtils.createKey("ore_quartz_nether");
/*  46 */   public static final ResourceKey<PlacedFeature> ORE_GRAVEL_NETHER = PlacementUtils.createKey("ore_gravel_nether");
/*  47 */   public static final ResourceKey<PlacedFeature> ORE_BLACKSTONE = PlacementUtils.createKey("ore_blackstone");
/*  48 */   public static final ResourceKey<PlacedFeature> ORE_DIRT = PlacementUtils.createKey("ore_dirt");
/*  49 */   public static final ResourceKey<PlacedFeature> ORE_GRAVEL = PlacementUtils.createKey("ore_gravel");
/*  50 */   public static final ResourceKey<PlacedFeature> ORE_GRANITE_UPPER = PlacementUtils.createKey("ore_granite_upper");
/*  51 */   public static final ResourceKey<PlacedFeature> ORE_GRANITE_LOWER = PlacementUtils.createKey("ore_granite_lower");
/*  52 */   public static final ResourceKey<PlacedFeature> ORE_DIORITE_UPPER = PlacementUtils.createKey("ore_diorite_upper");
/*  53 */   public static final ResourceKey<PlacedFeature> ORE_DIORITE_LOWER = PlacementUtils.createKey("ore_diorite_lower");
/*  54 */   public static final ResourceKey<PlacedFeature> ORE_ANDESITE_UPPER = PlacementUtils.createKey("ore_andesite_upper");
/*  55 */   public static final ResourceKey<PlacedFeature> ORE_ANDESITE_LOWER = PlacementUtils.createKey("ore_andesite_lower");
/*  56 */   public static final ResourceKey<PlacedFeature> ORE_TUFF = PlacementUtils.createKey("ore_tuff");
/*  57 */   public static final ResourceKey<PlacedFeature> ORE_COAL_UPPER = PlacementUtils.createKey("ore_coal_upper");
/*  58 */   public static final ResourceKey<PlacedFeature> ORE_COAL_LOWER = PlacementUtils.createKey("ore_coal_lower");
/*  59 */   public static final ResourceKey<PlacedFeature> ORE_IRON_UPPER = PlacementUtils.createKey("ore_iron_upper");
/*  60 */   public static final ResourceKey<PlacedFeature> ORE_IRON_MIDDLE = PlacementUtils.createKey("ore_iron_middle");
/*  61 */   public static final ResourceKey<PlacedFeature> ORE_IRON_SMALL = PlacementUtils.createKey("ore_iron_small");
/*  62 */   public static final ResourceKey<PlacedFeature> ORE_GOLD_EXTRA = PlacementUtils.createKey("ore_gold_extra");
/*  63 */   public static final ResourceKey<PlacedFeature> ORE_GOLD = PlacementUtils.createKey("ore_gold");
/*  64 */   public static final ResourceKey<PlacedFeature> ORE_GOLD_LOWER = PlacementUtils.createKey("ore_gold_lower");
/*  65 */   public static final ResourceKey<PlacedFeature> ORE_REDSTONE = PlacementUtils.createKey("ore_redstone");
/*  66 */   public static final ResourceKey<PlacedFeature> ORE_REDSTONE_LOWER = PlacementUtils.createKey("ore_redstone_lower");
/*  67 */   public static final ResourceKey<PlacedFeature> ORE_DIAMOND = PlacementUtils.createKey("ore_diamond");
/*  68 */   public static final ResourceKey<PlacedFeature> ORE_DIAMOND_MEDIUM = PlacementUtils.createKey("ore_diamond_medium");
/*  69 */   public static final ResourceKey<PlacedFeature> ORE_DIAMOND_LARGE = PlacementUtils.createKey("ore_diamond_large");
/*  70 */   public static final ResourceKey<PlacedFeature> ORE_DIAMOND_BURIED = PlacementUtils.createKey("ore_diamond_buried");
/*  71 */   public static final ResourceKey<PlacedFeature> ORE_LAPIS = PlacementUtils.createKey("ore_lapis");
/*  72 */   public static final ResourceKey<PlacedFeature> ORE_LAPIS_BURIED = PlacementUtils.createKey("ore_lapis_buried");
/*  73 */   public static final ResourceKey<PlacedFeature> ORE_INFESTED = PlacementUtils.createKey("ore_infested");
/*  74 */   public static final ResourceKey<PlacedFeature> ORE_EMERALD = PlacementUtils.createKey("ore_emerald");
/*  75 */   public static final ResourceKey<PlacedFeature> ORE_ANCIENT_DEBRIS_LARGE = PlacementUtils.createKey("ore_ancient_debris_large");
/*  76 */   public static final ResourceKey<PlacedFeature> ORE_ANCIENT_DEBRIS_SMALL = PlacementUtils.createKey("ore_debris_small");
/*  77 */   public static final ResourceKey<PlacedFeature> ORE_COPPER = PlacementUtils.createKey("ore_copper");
/*  78 */   public static final ResourceKey<PlacedFeature> ORE_COPPER_LARGE = PlacementUtils.createKey("ore_copper_large");
/*  79 */   public static final ResourceKey<PlacedFeature> ORE_CLAY = PlacementUtils.createKey("ore_clay");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> context) {
/*  82 */     HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
/*  83 */     Holder.Reference reference1 = configuredFeatures.getOrThrow(OreFeatures.ORE_MAGMA);
/*  84 */     Holder.Reference reference2 = configuredFeatures.getOrThrow(OreFeatures.ORE_SOUL_SAND);
/*  85 */     Holder.Reference reference3 = configuredFeatures.getOrThrow(OreFeatures.ORE_NETHER_GOLD);
/*  86 */     Holder.Reference reference4 = configuredFeatures.getOrThrow(OreFeatures.ORE_QUARTZ);
/*  87 */     Holder.Reference reference5 = configuredFeatures.getOrThrow(OreFeatures.ORE_GRAVEL_NETHER);
/*  88 */     Holder.Reference reference6 = configuredFeatures.getOrThrow(OreFeatures.ORE_BLACKSTONE);
/*  89 */     Holder.Reference reference7 = configuredFeatures.getOrThrow(OreFeatures.ORE_DIRT);
/*  90 */     Holder.Reference reference8 = configuredFeatures.getOrThrow(OreFeatures.ORE_GRAVEL);
/*  91 */     Holder.Reference reference9 = configuredFeatures.getOrThrow(OreFeatures.ORE_GRANITE);
/*  92 */     Holder.Reference reference10 = configuredFeatures.getOrThrow(OreFeatures.ORE_DIORITE);
/*  93 */     Holder.Reference reference11 = configuredFeatures.getOrThrow(OreFeatures.ORE_ANDESITE);
/*  94 */     Holder.Reference reference12 = configuredFeatures.getOrThrow(OreFeatures.ORE_TUFF);
/*  95 */     Holder.Reference reference13 = configuredFeatures.getOrThrow(OreFeatures.ORE_COAL);
/*  96 */     Holder.Reference reference14 = configuredFeatures.getOrThrow(OreFeatures.ORE_COAL_BURIED);
/*  97 */     Holder.Reference reference15 = configuredFeatures.getOrThrow(OreFeatures.ORE_IRON);
/*  98 */     Holder.Reference reference16 = configuredFeatures.getOrThrow(OreFeatures.ORE_IRON_SMALL);
/*  99 */     Holder.Reference reference17 = configuredFeatures.getOrThrow(OreFeatures.ORE_GOLD);
/* 100 */     Holder.Reference reference18 = configuredFeatures.getOrThrow(OreFeatures.ORE_GOLD_BURIED);
/* 101 */     Holder.Reference reference19 = configuredFeatures.getOrThrow(OreFeatures.ORE_REDSTONE);
/* 102 */     Holder.Reference reference20 = configuredFeatures.getOrThrow(OreFeatures.ORE_DIAMOND_SMALL);
/* 103 */     Holder.Reference reference21 = configuredFeatures.getOrThrow(OreFeatures.ORE_DIAMOND_MEDIUM);
/* 104 */     Holder.Reference reference22 = configuredFeatures.getOrThrow(OreFeatures.ORE_DIAMOND_LARGE);
/* 105 */     Holder.Reference reference23 = configuredFeatures.getOrThrow(OreFeatures.ORE_DIAMOND_BURIED);
/* 106 */     Holder.Reference reference24 = configuredFeatures.getOrThrow(OreFeatures.ORE_LAPIS);
/* 107 */     Holder.Reference reference25 = configuredFeatures.getOrThrow(OreFeatures.ORE_LAPIS_BURIED);
/* 108 */     Holder.Reference reference26 = configuredFeatures.getOrThrow(OreFeatures.ORE_INFESTED);
/* 109 */     Holder.Reference reference27 = configuredFeatures.getOrThrow(OreFeatures.ORE_EMERALD);
/* 110 */     Holder.Reference reference28 = configuredFeatures.getOrThrow(OreFeatures.ORE_ANCIENT_DEBRIS_LARGE);
/* 111 */     Holder.Reference reference29 = configuredFeatures.getOrThrow(OreFeatures.ORE_ANCIENT_DEBRIS_SMALL);
/* 112 */     Holder.Reference reference30 = configuredFeatures.getOrThrow(OreFeatures.ORE_COPPPER_SMALL);
/* 113 */     Holder.Reference reference31 = configuredFeatures.getOrThrow(OreFeatures.ORE_COPPER_LARGE);
/* 114 */     Holder.Reference reference32 = configuredFeatures.getOrThrow(OreFeatures.ORE_CLAY);
/*     */     
/* 116 */     PlacementUtils.register(context, ORE_MAGMA, reference1, 
/* 117 */         commonOrePlacement(4, HeightRangePlacement.uniform(VerticalAnchor.absolute(27), VerticalAnchor.absolute(36))));
/*     */     
/* 119 */     PlacementUtils.register(context, ORE_SOUL_SAND, reference2, 
/* 120 */         commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(31))));
/*     */     
/* 122 */     PlacementUtils.register(context, ORE_GOLD_DELTAS, reference3, 
/* 123 */         commonOrePlacement(20, PlacementUtils.RANGE_10_10));
/*     */     
/* 125 */     PlacementUtils.register(context, ORE_QUARTZ_DELTAS, reference4, 
/* 126 */         commonOrePlacement(32, PlacementUtils.RANGE_10_10));
/*     */     
/* 128 */     PlacementUtils.register(context, ORE_GOLD_NETHER, reference3, 
/* 129 */         commonOrePlacement(10, PlacementUtils.RANGE_10_10));
/*     */     
/* 131 */     PlacementUtils.register(context, ORE_QUARTZ_NETHER, reference4, 
/* 132 */         commonOrePlacement(16, PlacementUtils.RANGE_10_10));
/*     */     
/* 134 */     PlacementUtils.register(context, ORE_GRAVEL_NETHER, reference5, 
/* 135 */         commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(5), VerticalAnchor.absolute(41))));
/*     */     
/* 137 */     PlacementUtils.register(context, ORE_BLACKSTONE, reference6, 
/* 138 */         commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(5), VerticalAnchor.absolute(31))));
/*     */     
/* 140 */     PlacementUtils.register(context, ORE_DIRT, reference7, 
/* 141 */         commonOrePlacement(7, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(160))));
/*     */     
/* 143 */     PlacementUtils.register(context, ORE_GRAVEL, reference8, 
/* 144 */         commonOrePlacement(14, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top())));
/*     */     
/* 146 */     PlacementUtils.register(context, ORE_GRANITE_UPPER, reference9, 
/* 147 */         rareOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128))));
/*     */     
/* 149 */     PlacementUtils.register(context, ORE_GRANITE_LOWER, reference9, 
/* 150 */         commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(60))));
/*     */     
/* 152 */     PlacementUtils.register(context, ORE_DIORITE_UPPER, reference10, 
/* 153 */         rareOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128))));
/*     */     
/* 155 */     PlacementUtils.register(context, ORE_DIORITE_LOWER, reference10, 
/* 156 */         commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(60))));
/*     */     
/* 158 */     PlacementUtils.register(context, ORE_ANDESITE_UPPER, reference11, 
/* 159 */         rareOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128))));
/*     */     
/* 161 */     PlacementUtils.register(context, ORE_ANDESITE_LOWER, reference11, 
/* 162 */         commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(60))));
/*     */     
/* 164 */     PlacementUtils.register(context, ORE_TUFF, reference12, 
/* 165 */         commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(0))));
/*     */     
/* 167 */     PlacementUtils.register(context, ORE_COAL_UPPER, reference13, 
/* 168 */         commonOrePlacement(30, HeightRangePlacement.uniform(VerticalAnchor.absolute(136), VerticalAnchor.top())));
/*     */     
/* 170 */     PlacementUtils.register(context, ORE_COAL_LOWER, reference14, 
/* 171 */         commonOrePlacement(20, HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(192))));
/*     */     
/* 173 */     PlacementUtils.register(context, ORE_IRON_UPPER, reference15, 
/* 174 */         commonOrePlacement(90, HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384))));
/*     */     
/* 176 */     PlacementUtils.register(context, ORE_IRON_MIDDLE, reference15, 
/* 177 */         commonOrePlacement(10, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56))));
/*     */     
/* 179 */     PlacementUtils.register(context, ORE_IRON_SMALL, reference16, 
/* 180 */         commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72))));
/*     */     
/* 182 */     PlacementUtils.register(context, ORE_GOLD_EXTRA, reference17, 
/* 183 */         commonOrePlacement(50, HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(256))));
/*     */     
/* 185 */     PlacementUtils.register(context, ORE_GOLD, reference18, 
/* 186 */         commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));
/*     */     
/* 188 */     PlacementUtils.register(context, ORE_GOLD_LOWER, reference18, 
/* 189 */         orePlacement(
/* 190 */           CountPlacement.of(UniformInt.of(0, 1)), 
/* 191 */           HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-48))));
/*     */ 
/*     */     
/* 194 */     PlacementUtils.register(context, ORE_REDSTONE, reference19, 
/* 195 */         commonOrePlacement(4, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(15))));
/*     */     
/* 197 */     PlacementUtils.register(context, ORE_REDSTONE_LOWER, reference19, 
/* 198 */         commonOrePlacement(8, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-32), VerticalAnchor.aboveBottom(32))));
/*     */     
/* 200 */     PlacementUtils.register(context, ORE_DIAMOND, reference20, 
/* 201 */         commonOrePlacement(7, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
/*     */     
/* 203 */     PlacementUtils.register(context, ORE_DIAMOND_MEDIUM, reference21, 
/* 204 */         commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-4))));
/*     */     
/* 206 */     PlacementUtils.register(context, ORE_DIAMOND_LARGE, reference22, 
/* 207 */         rareOrePlacement(9, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
/*     */     
/* 209 */     PlacementUtils.register(context, ORE_DIAMOND_BURIED, reference23, 
/* 210 */         commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
/*     */     
/* 212 */     PlacementUtils.register(context, ORE_LAPIS, reference24, 
/* 213 */         commonOrePlacement(2, HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(32))));
/*     */     
/* 215 */     PlacementUtils.register(context, ORE_LAPIS_BURIED, reference25, 
/* 216 */         commonOrePlacement(4, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64))));
/*     */     
/* 218 */     PlacementUtils.register(context, ORE_INFESTED, reference26, 
/* 219 */         commonOrePlacement(14, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(63))));
/*     */     
/* 221 */     PlacementUtils.register(context, ORE_EMERALD, reference27, 
/* 222 */         commonOrePlacement(100, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(480))));
/*     */     
/* 224 */     PlacementUtils.register(context, ORE_ANCIENT_DEBRIS_LARGE, reference28, new PlacementModifier[] {
/* 225 */           InSquarePlacement.spread(), 
/* 226 */           HeightRangePlacement.triangle(VerticalAnchor.absolute(8), VerticalAnchor.absolute(24)), 
/* 227 */           BiomeFilter.biome()
/*     */         });
/* 229 */     PlacementUtils.register(context, ORE_ANCIENT_DEBRIS_SMALL, reference29, new PlacementModifier[] {
/* 230 */           InSquarePlacement.spread(), PlacementUtils.RANGE_8_8, 
/*     */           
/* 232 */           BiomeFilter.biome()
/*     */         });
/* 234 */     PlacementUtils.register(context, ORE_COPPER, reference30, 
/* 235 */         commonOrePlacement(16, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(112))));
/*     */     
/* 237 */     PlacementUtils.register(context, ORE_COPPER_LARGE, reference31, 
/* 238 */         commonOrePlacement(16, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(112))));
/*     */     
/* 240 */     PlacementUtils.register(context, ORE_CLAY, reference32, 
/* 241 */         commonOrePlacement(46, PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\placement\OrePlacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */