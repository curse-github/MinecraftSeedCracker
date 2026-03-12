/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.Noises;
/*     */ import net.minecraft.world.level.levelgen.SurfaceRules;
/*     */ import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SurfaceRuleData
/*     */ {
/*  21 */   private static final SurfaceRules.RuleSource AIR = makeStateRule(Blocks.AIR);
/*     */   
/*  23 */   private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
/*  24 */   private static final SurfaceRules.RuleSource WHITE_TERRACOTTA = makeStateRule(Blocks.WHITE_TERRACOTTA);
/*  25 */   private static final SurfaceRules.RuleSource ORANGE_TERRACOTTA = makeStateRule(Blocks.ORANGE_TERRACOTTA);
/*  26 */   private static final SurfaceRules.RuleSource TERRACOTTA = makeStateRule(Blocks.TERRACOTTA);
/*  27 */   private static final SurfaceRules.RuleSource RED_SAND = makeStateRule(Blocks.RED_SAND);
/*  28 */   private static final SurfaceRules.RuleSource RED_SANDSTONE = makeStateRule(Blocks.RED_SANDSTONE);
/*  29 */   private static final SurfaceRules.RuleSource STONE = makeStateRule(Blocks.STONE);
/*  30 */   private static final SurfaceRules.RuleSource DEEPSLATE = makeStateRule(Blocks.DEEPSLATE);
/*  31 */   private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
/*  32 */   private static final SurfaceRules.RuleSource PODZOL = makeStateRule(Blocks.PODZOL);
/*  33 */   private static final SurfaceRules.RuleSource COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);
/*  34 */   private static final SurfaceRules.RuleSource MYCELIUM = makeStateRule(Blocks.MYCELIUM);
/*  35 */   private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
/*  36 */   private static final SurfaceRules.RuleSource CALCITE = makeStateRule(Blocks.CALCITE);
/*  37 */   private static final SurfaceRules.RuleSource GRAVEL = makeStateRule(Blocks.GRAVEL);
/*  38 */   private static final SurfaceRules.RuleSource SAND = makeStateRule(Blocks.SAND);
/*  39 */   private static final SurfaceRules.RuleSource SANDSTONE = makeStateRule(Blocks.SANDSTONE);
/*  40 */   private static final SurfaceRules.RuleSource PACKED_ICE = makeStateRule(Blocks.PACKED_ICE);
/*  41 */   private static final SurfaceRules.RuleSource SNOW_BLOCK = makeStateRule(Blocks.SNOW_BLOCK);
/*  42 */   private static final SurfaceRules.RuleSource MUD = makeStateRule(Blocks.MUD);
/*  43 */   private static final SurfaceRules.RuleSource POWDER_SNOW = makeStateRule(Blocks.POWDER_SNOW);
/*  44 */   private static final SurfaceRules.RuleSource ICE = makeStateRule(Blocks.ICE);
/*  45 */   private static final SurfaceRules.RuleSource WATER = makeStateRule(Blocks.WATER);
/*     */   
/*  47 */   private static final SurfaceRules.RuleSource LAVA = makeStateRule(Blocks.LAVA);
/*  48 */   private static final SurfaceRules.RuleSource NETHERRACK = makeStateRule(Blocks.NETHERRACK);
/*  49 */   private static final SurfaceRules.RuleSource SOUL_SAND = makeStateRule(Blocks.SOUL_SAND);
/*  50 */   private static final SurfaceRules.RuleSource SOUL_SOIL = makeStateRule(Blocks.SOUL_SOIL);
/*  51 */   private static final SurfaceRules.RuleSource BASALT = makeStateRule(Blocks.BASALT);
/*  52 */   private static final SurfaceRules.RuleSource BLACKSTONE = makeStateRule(Blocks.BLACKSTONE);
/*  53 */   private static final SurfaceRules.RuleSource WARPED_WART_BLOCK = makeStateRule(Blocks.WARPED_WART_BLOCK);
/*  54 */   private static final SurfaceRules.RuleSource WARPED_NYLIUM = makeStateRule(Blocks.WARPED_NYLIUM);
/*  55 */   private static final SurfaceRules.RuleSource NETHER_WART_BLOCK = makeStateRule(Blocks.NETHER_WART_BLOCK);
/*  56 */   private static final SurfaceRules.RuleSource CRIMSON_NYLIUM = makeStateRule(Blocks.CRIMSON_NYLIUM);
/*     */   
/*  58 */   private static final SurfaceRules.RuleSource ENDSTONE = makeStateRule(Blocks.END_STONE);
/*     */ 
/*     */   
/*  61 */   private static SurfaceRules.RuleSource makeStateRule(Block block) { return SurfaceRules.state(block.defaultBlockState()); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public static SurfaceRules.RuleSource overworld() { return overworldLike(true, false, true); }
/*     */ 
/*     */   
/*     */   public static SurfaceRules.RuleSource overworldLike(boolean doPreliminarySurfaceCheck, boolean bedrockRoof, boolean bedrockFloor) {
/*  69 */     SurfaceRules.ConditionSource woodedBadlandsTop = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(97), 2);
/*  70 */     SurfaceRules.ConditionSource badlandsTop = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(256), 0);
/*  71 */     SurfaceRules.ConditionSource badlandsHeightCondition = SurfaceRules.yStartCheck(VerticalAnchor.absolute(63), -1);
/*  72 */     SurfaceRules.ConditionSource badlandsMid = SurfaceRules.yStartCheck(VerticalAnchor.absolute(74), 1);
/*     */     
/*  74 */     SurfaceRules.ConditionSource mangroveSwampPuddleLevel = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(60), 0);
/*  75 */     SurfaceRules.ConditionSource swampPuddleLevel = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
/*  76 */     SurfaceRules.ConditionSource aboveOverworldSeaLevel = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(63), 0);
/*     */ 
/*     */ 
/*     */     
/*  80 */     SurfaceRules.ConditionSource notUnderwater = SurfaceRules.waterBlockCheck(-1, 0);
/*  81 */     SurfaceRules.ConditionSource aboveWater = SurfaceRules.waterBlockCheck(0, 0);
/*     */     
/*  83 */     SurfaceRules.ConditionSource notUnderDeepWater = SurfaceRules.waterStartCheck(-6, -1);
/*     */     
/*  85 */     SurfaceRules.ConditionSource hole = SurfaceRules.hole();
/*  86 */     SurfaceRules.ConditionSource frozenOcean = SurfaceRules.isBiome(new ResourceKey[] { Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN });
/*  87 */     SurfaceRules.ConditionSource steep = SurfaceRules.steep();
/*     */     
/*  89 */     SurfaceRules.RuleSource grassOrDirtIfUnderwater = SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/*  90 */           SurfaceRules.ifTrue(aboveWater, GRASS_BLOCK), DIRT
/*     */         });
/*     */ 
/*     */     
/*  94 */     SurfaceRules.RuleSource sandOrSandstoneIfCeiling = SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/*  95 */           SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, SANDSTONE), SAND
/*     */         });
/*     */ 
/*     */     
/*  99 */     SurfaceRules.RuleSource gravelOrStoneIfCeiling = SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 100 */           SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, STONE), GRAVEL
/*     */         });
/*     */ 
/*     */     
/* 104 */     SurfaceRules.ConditionSource biomesWithSandAndSandstone = SurfaceRules.isBiome(new ResourceKey[] { Biomes.WARM_OCEAN, Biomes.BEACH, Biomes.SNOWY_BEACH });
/* 105 */     SurfaceRules.ConditionSource biomesWithSandAndVeryDeepSandstone = SurfaceRules.isBiome(new ResourceKey[] { Biomes.DESERT });
/*     */     
/* 107 */     SurfaceRules.RuleSource commonSurfaceAndUnderRules = SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 108 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.STONY_PEAKS }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 109 */                 SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.CALCITE, -0.0125D, 0.0125D), CALCITE), STONE
/*     */ 
/*     */               
/* 112 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.STONY_SHORE }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 113 */                 SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.GRAVEL, -0.05D, 0.05D), gravelOrStoneIfCeiling), STONE
/*     */ 
/*     */               
/* 116 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.WINDSWEPT_HILLS }, ), SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE)), 
/* 117 */           SurfaceRules.ifTrue(biomesWithSandAndSandstone, sandOrSandstoneIfCeiling), 
/* 118 */           SurfaceRules.ifTrue(biomesWithSandAndVeryDeepSandstone, sandOrSandstoneIfCeiling), 
/* 119 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.DRIPSTONE_CAVES }, ), STONE)
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 124 */     SurfaceRules.RuleSource powderSnowUnderRule = SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.45D, 0.58D), SurfaceRules.ifTrue(aboveWater, POWDER_SNOW));
/* 125 */     SurfaceRules.RuleSource powderSnowSurfaceRule = SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.35D, 0.6D), SurfaceRules.ifTrue(aboveWater, POWDER_SNOW));
/*     */     
/* 127 */     SurfaceRules.RuleSource biomeUnderSurfaceRule = SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 128 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.FROZEN_PEAKS }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 129 */                 SurfaceRules.ifTrue(steep, PACKED_ICE), 
/* 130 */                 SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PACKED_ICE, -0.5D, 0.2D), PACKED_ICE), 
/* 131 */                 SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, -0.0625D, 0.025D), ICE), 
/* 132 */                 SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK)
/*     */               
/* 134 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.SNOWY_SLOPES }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 135 */                 SurfaceRules.ifTrue(steep, STONE), powderSnowUnderRule, 
/*     */                 
/* 137 */                 SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK)
/*     */               
/* 139 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.JAGGED_PEAKS }, ), STONE), 
/* 140 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.GROVE }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { powderSnowUnderRule, DIRT })), commonSurfaceAndUnderRules, 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 145 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.WINDSWEPT_SAVANNA }, ), SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE)), 
/* 146 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.WINDSWEPT_GRAVELLY_HILLS }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 147 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), gravelOrStoneIfCeiling), 
/* 148 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE), 
/* 149 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), DIRT), gravelOrStoneIfCeiling
/*     */ 
/*     */               
/* 152 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.MANGROVE_SWAMP }, ), MUD), DIRT
/*     */         });
/*     */ 
/*     */     
/* 156 */     SurfaceRules.RuleSource biomeSurfaceRule = SurfaceRules.sequence(new SurfaceRules.RuleSource[] { 
/* 157 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.FROZEN_PEAKS }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 158 */                 SurfaceRules.ifTrue(steep, PACKED_ICE), 
/* 159 */                 SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PACKED_ICE, 0.0D, 0.2D), PACKED_ICE), 
/* 160 */                 SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, 0.0D, 0.025D), ICE), 
/* 161 */                 SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK)
/*     */               
/* 163 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.SNOWY_SLOPES }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 164 */                 SurfaceRules.ifTrue(steep, STONE), powderSnowSurfaceRule, 
/*     */                 
/* 166 */                 SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK)
/*     */               
/* 168 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.JAGGED_PEAKS }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 169 */                 SurfaceRules.ifTrue(steep, STONE), 
/* 170 */                 SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK)
/*     */               
/* 172 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.GROVE }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { powderSnowSurfaceRule, 
/*     */                 
/* 174 */                 SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK)
/*     */ 
/*     */               
/* 177 */               })), commonSurfaceAndUnderRules, SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.WINDSWEPT_SAVANNA }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 178 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE), 
/* 179 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(-0.5D), COARSE_DIRT)
/*     */               
/* 181 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.WINDSWEPT_GRAVELLY_HILLS }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 182 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), gravelOrStoneIfCeiling), 
/* 183 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE), 
/* 184 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), grassOrDirtIfUnderwater), gravelOrStoneIfCeiling
/*     */ 
/*     */               
/* 187 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 188 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), COARSE_DIRT), 
/* 189 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(-0.95D), PODZOL)
/*     */               
/* 191 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.ICE_SPIKES }, ), SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK)), 
/* 192 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.MANGROVE_SWAMP }, ), MUD), 
/* 193 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.MUSHROOM_FIELDS }, ), MYCELIUM), grassOrDirtIfUnderwater });
/*     */ 
/*     */ 
/*     */     
/* 197 */     SurfaceRules.ConditionSource clayBand1 = SurfaceRules.noiseCondition(Noises.SURFACE, -0.909D, -0.5454D);
/* 198 */     SurfaceRules.ConditionSource clayBand2 = SurfaceRules.noiseCondition(Noises.SURFACE, -0.1818D, 0.1818D);
/* 199 */     SurfaceRules.ConditionSource clayBand3 = SurfaceRules.noiseCondition(Noises.SURFACE, 0.5454D, 0.909D);
/*     */     
/* 201 */     SurfaceRules.RuleSource mainRuleCloseToSurface = SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 202 */           SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 203 */                 SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.WOODED_BADLANDS }, ), SurfaceRules.ifTrue(woodedBadlandsTop, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 204 */                         SurfaceRules.ifTrue(clayBand1, COARSE_DIRT), 
/* 205 */                         SurfaceRules.ifTrue(clayBand2, COARSE_DIRT), 
/* 206 */                         SurfaceRules.ifTrue(clayBand3, COARSE_DIRT), grassOrDirtIfUnderwater
/*     */ 
/*     */                       
/* 209 */                       }))), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.SWAMP }, ), SurfaceRules.ifTrue(swampPuddleLevel, SurfaceRules.ifTrue(SurfaceRules.not(aboveOverworldSeaLevel), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP, 0.0D), WATER)))), 
/* 210 */                 SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.MANGROVE_SWAMP }, ), SurfaceRules.ifTrue(mangroveSwampPuddleLevel, SurfaceRules.ifTrue(SurfaceRules.not(aboveOverworldSeaLevel), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP, 0.0D), WATER))))
/*     */               
/* 212 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 213 */                 SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 214 */                       SurfaceRules.ifTrue(badlandsTop, ORANGE_TERRACOTTA), 
/* 215 */                       SurfaceRules.ifTrue(badlandsMid, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 216 */                             SurfaceRules.ifTrue(clayBand1, TERRACOTTA), 
/* 217 */                             SurfaceRules.ifTrue(clayBand2, TERRACOTTA), 
/* 218 */                             SurfaceRules.ifTrue(clayBand3, TERRACOTTA), 
/* 219 */                             SurfaceRules.bandlands()
/*     */                           
/* 221 */                           })), SurfaceRules.ifTrue(notUnderwater, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 222 */                             SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, RED_SANDSTONE), RED_SAND
/*     */ 
/*     */                           
/* 225 */                           })), SurfaceRules.ifTrue(SurfaceRules.not(hole), ORANGE_TERRACOTTA), 
/* 226 */                       SurfaceRules.ifTrue(notUnderDeepWater, WHITE_TERRACOTTA), gravelOrStoneIfCeiling
/*     */ 
/*     */                     
/* 229 */                     })), SurfaceRules.ifTrue(badlandsHeightCondition, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 230 */                       SurfaceRules.ifTrue(aboveOverworldSeaLevel, SurfaceRules.ifTrue(SurfaceRules.not(badlandsMid), ORANGE_TERRACOTTA)), 
/* 231 */                       SurfaceRules.bandlands()
/*     */                     
/* 233 */                     })), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(notUnderDeepWater, WHITE_TERRACOTTA))
/*     */               
/* 235 */               })), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(notUnderwater, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 236 */                   SurfaceRules.ifTrue(frozenOcean, SurfaceRules.ifTrue(hole, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 237 */                           SurfaceRules.ifTrue(aboveWater, AIR), 
/* 238 */                           SurfaceRules.ifTrue(SurfaceRules.temperature(), ICE), WATER
/*     */ 
/*     */                         
/*     */                         }))), biomeSurfaceRule
/*     */                 
/* 243 */                 }))), SurfaceRules.ifTrue(notUnderDeepWater, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 244 */                 SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(frozenOcean, SurfaceRules.ifTrue(hole, WATER))), 
/* 245 */                 SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, biomeUnderSurfaceRule), 
/* 246 */                 SurfaceRules.ifTrue(biomesWithSandAndSandstone, SurfaceRules.ifTrue(SurfaceRules.DEEP_UNDER_FLOOR, SANDSTONE)), 
/* 247 */                 SurfaceRules.ifTrue(biomesWithSandAndVeryDeepSandstone, SurfaceRules.ifTrue(SurfaceRules.VERY_DEEP_UNDER_FLOOR, SANDSTONE))
/*     */               
/* 249 */               })), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 250 */                 SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS }, ), STONE), 
/* 251 */                 SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN }, ), sandOrSandstoneIfCeiling), gravelOrStoneIfCeiling
/*     */               }))
/*     */         });
/*     */ 
/*     */     
/* 256 */     ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
/*     */     
/* 258 */     if (bedrockRoof) {
/* 259 */       builder.add(SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK));
/*     */     }
/* 261 */     if (bedrockFloor) {
/* 262 */       builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK));
/*     */     }
/* 264 */     SurfaceRules.RuleSource ruleAbovePreliminarySurface = SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), mainRuleCloseToSurface);
/* 265 */     builder.add(doPreliminarySurfaceCheck ? ruleAbovePreliminarySurface : mainRuleCloseToSurface);
/* 266 */     builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)), DEEPSLATE));
/*     */     
/* 268 */     return SurfaceRules.sequence((RuleSource[])builder.build().toArray(x$0 -> new SurfaceRules.RuleSource[x$0]));
/*     */   }
/*     */   
/*     */   public static SurfaceRules.RuleSource nether() {
/* 272 */     aboveNetherLavaLevel = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(31), 0);
/* 273 */     SurfaceRules.ConditionSource aboveNetherLavaSurface = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(32), 0);
/*     */     
/* 275 */     SurfaceRules.ConditionSource netherBandAroundLavaLevelBottom = SurfaceRules.yStartCheck(VerticalAnchor.absolute(30), 0);
/* 276 */     SurfaceRules.ConditionSource netherBandAroundLavaLevelTop = SurfaceRules.not(SurfaceRules.yStartCheck(VerticalAnchor.absolute(35), 0));
/*     */     
/* 278 */     SurfaceRules.ConditionSource closeToCeiling = SurfaceRules.yBlockCheck(VerticalAnchor.belowTop(5), 0);
/*     */     
/* 280 */     SurfaceRules.ConditionSource hole = SurfaceRules.hole();
/*     */     
/* 282 */     SurfaceRules.ConditionSource soulSandLayer = SurfaceRules.noiseCondition(Noises.SOUL_SAND_LAYER, -0.012D);
/* 283 */     SurfaceRules.ConditionSource gravelLayer = SurfaceRules.noiseCondition(Noises.GRAVEL_LAYER, -0.012D);
/* 284 */     SurfaceRules.ConditionSource patch = SurfaceRules.noiseCondition(Noises.PATCH, -0.012D);
/* 285 */     SurfaceRules.ConditionSource netherrack = SurfaceRules.noiseCondition(Noises.NETHERRACK, 0.54D);
/* 286 */     SurfaceRules.ConditionSource netherWart = SurfaceRules.noiseCondition(Noises.NETHER_WART, 1.17D);
/* 287 */     SurfaceRules.ConditionSource netherStateSelector = SurfaceRules.noiseCondition(Noises.NETHER_STATE_SELECTOR, 0.0D);
/*     */     
/* 289 */     SurfaceRules.RuleSource gravelPatch = SurfaceRules.ifTrue(patch, SurfaceRules.ifTrue(netherBandAroundLavaLevelBottom, SurfaceRules.ifTrue(netherBandAroundLavaLevelTop, GRAVEL)));
/*     */     
/* 291 */     return SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 292 */           SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK), 
/* 293 */           SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK), 
/* 294 */           SurfaceRules.ifTrue(closeToCeiling, NETHERRACK), 
/* 295 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.BASALT_DELTAS }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 296 */                 SurfaceRules.ifTrue(SurfaceRules.UNDER_CEILING, BASALT), 
/* 297 */                 SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/*     */                       
/* 299 */                       gravelPatch, SurfaceRules.ifTrue(netherStateSelector, BASALT), BLACKSTONE
/*     */                     
/*     */                     }))
/*     */               
/* 303 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.SOUL_SAND_VALLEY }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 304 */                 SurfaceRules.ifTrue(SurfaceRules.UNDER_CEILING, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 305 */                       SurfaceRules.ifTrue(netherStateSelector, SOUL_SAND), SOUL_SOIL
/*     */ 
/*     */                     
/* 308 */                     })), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/*     */                       
/* 310 */                       gravelPatch, SurfaceRules.ifTrue(netherStateSelector, SOUL_SAND), SOUL_SOIL
/*     */                     
/*     */                     }))
/*     */               
/* 314 */               })), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 315 */                 SurfaceRules.ifTrue(SurfaceRules.not(aboveNetherLavaSurface), SurfaceRules.ifTrue(hole, LAVA)), 
/* 316 */                 SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.WARPED_FOREST }, ), SurfaceRules.ifTrue(SurfaceRules.not(netherrack), SurfaceRules.ifTrue(aboveNetherLavaLevel, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 317 */                           SurfaceRules.ifTrue(netherWart, WARPED_WART_BLOCK), WARPED_NYLIUM
/*     */ 
/*     */                         
/* 320 */                         })))), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.CRIMSON_FOREST }, ), SurfaceRules.ifTrue(SurfaceRules.not(netherrack), SurfaceRules.ifTrue(aboveNetherLavaLevel, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 321 */                           SurfaceRules.ifTrue(netherWart, NETHER_WART_BLOCK), CRIMSON_NYLIUM
/*     */                         
/*     */                         }))))
/*     */               
/* 325 */               })), SurfaceRules.ifTrue(SurfaceRules.isBiome(new ResourceKey[] { Biomes.NETHER_WASTES }, ), SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 326 */                 SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(soulSandLayer, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 327 */                         SurfaceRules.ifTrue(SurfaceRules.not(hole), SurfaceRules.ifTrue(netherBandAroundLavaLevelBottom, SurfaceRules.ifTrue(netherBandAroundLavaLevelTop, SOUL_SAND))), NETHERRACK
/*     */ 
/*     */                       
/* 330 */                       }))), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(aboveNetherLavaLevel, SurfaceRules.ifTrue(netherBandAroundLavaLevelTop, SurfaceRules.ifTrue(gravelLayer, SurfaceRules.sequence(new SurfaceRules.RuleSource[] {
/* 331 */                             SurfaceRules.ifTrue(aboveNetherLavaSurface, GRAVEL), 
/* 332 */                             SurfaceRules.ifTrue(SurfaceRules.not(hole), GRAVEL)
/*     */                           })))))
/*     */               })), NETHERRACK
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 340 */   public static SurfaceRules.RuleSource end() { return ENDSTONE; }
/*     */ 
/*     */ 
/*     */   
/* 344 */   public static SurfaceRules.RuleSource air() { return AIR; }
/*     */ 
/*     */ 
/*     */   
/* 348 */   private static SurfaceRules.ConditionSource surfaceNoiseAbove(double threshold) { return SurfaceRules.noiseCondition(Noises.SURFACE, threshold / 8.25D, Double.MAX_VALUE); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\SurfaceRuleData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */