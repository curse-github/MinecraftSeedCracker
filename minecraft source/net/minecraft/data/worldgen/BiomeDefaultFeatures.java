/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import net.minecraft.data.worldgen.placement.AquaticPlacements;
/*     */ import net.minecraft.data.worldgen.placement.CavePlacements;
/*     */ import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
/*     */ import net.minecraft.data.worldgen.placement.OrePlacements;
/*     */ import net.minecraft.data.worldgen.placement.VegetationPlacements;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*     */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ 
/*     */ public class BiomeDefaultFeatures {
/*     */   public static void addDefaultCarversAndLakes(BiomeGenerationSettings.Builder builder) {
/*  16 */     builder.addCarver(Carvers.CAVE);
/*  17 */     builder.addCarver(Carvers.CAVE_EXTRA_UNDERGROUND);
/*  18 */     builder.addCarver(Carvers.CANYON);
/*  19 */     builder.addFeature(GenerationStep.Decoration.LAKES, MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND);
/*  20 */     builder.addFeature(GenerationStep.Decoration.LAKES, MiscOverworldPlacements.LAKE_LAVA_SURFACE);
/*     */   }
/*     */   
/*     */   public static void addDefaultMonsterRoom(BiomeGenerationSettings.Builder builder) {
/*  24 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.MONSTER_ROOM);
/*  25 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.MONSTER_ROOM_DEEP);
/*     */   }
/*     */   
/*     */   public static void addDefaultUndergroundVariety(BiomeGenerationSettings.Builder builder) {
/*  29 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIRT);
/*  30 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GRAVEL);
/*  31 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GRANITE_UPPER);
/*  32 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GRANITE_LOWER);
/*  33 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIORITE_UPPER);
/*  34 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIORITE_LOWER);
/*  35 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_ANDESITE_UPPER);
/*  36 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_ANDESITE_LOWER);
/*  37 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_TUFF);
/*  38 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.GLOW_LICHEN);
/*     */   }
/*     */   
/*     */   public static void addDripstone(BiomeGenerationSettings.Builder builder) {
/*  42 */     builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, CavePlacements.LARGE_DRIPSTONE);
/*  43 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, CavePlacements.DRIPSTONE_CLUSTER);
/*  44 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, CavePlacements.POINTED_DRIPSTONE);
/*     */   }
/*     */   
/*     */   public static void addSculk(BiomeGenerationSettings.Builder builder) {
/*  48 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, CavePlacements.SCULK_VEIN);
/*  49 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, CavePlacements.SCULK_PATCH_DEEP_DARK);
/*     */   }
/*     */ 
/*     */   
/*  53 */   public static void addDefaultOres(BiomeGenerationSettings.Builder builder) { addDefaultOres(builder, false); }
/*     */ 
/*     */   
/*     */   public static void addDefaultOres(BiomeGenerationSettings.Builder builder, boolean largeCopperBlobs) {
/*  57 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_COAL_UPPER);
/*  58 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_COAL_LOWER);
/*     */     
/*  60 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_IRON_UPPER);
/*  61 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_IRON_MIDDLE);
/*  62 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_IRON_SMALL);
/*     */     
/*  64 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GOLD);
/*  65 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GOLD_LOWER);
/*     */     
/*  67 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_REDSTONE);
/*  68 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_REDSTONE_LOWER);
/*     */     
/*  70 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIAMOND);
/*  71 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIAMOND_MEDIUM);
/*  72 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIAMOND_LARGE);
/*  73 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIAMOND_BURIED);
/*     */     
/*  75 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_LAPIS);
/*  76 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_LAPIS_BURIED);
/*     */     
/*  78 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, largeCopperBlobs ? OrePlacements.ORE_COPPER_LARGE : OrePlacements.ORE_COPPER);
/*     */     
/*  80 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, CavePlacements.UNDERWATER_MAGMA);
/*     */   }
/*     */ 
/*     */   
/*  84 */   public static void addExtraGold(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GOLD_EXTRA); }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public static void addExtraEmeralds(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_EMERALD); }
/*     */ 
/*     */ 
/*     */   
/*  92 */   public static void addInfestedStone(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_INFESTED); }
/*     */ 
/*     */   
/*     */   public static void addDefaultSoftDisks(BiomeGenerationSettings.Builder builder) {
/*  96 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_SAND);
/*  97 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_CLAY);
/*  98 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_GRAVEL);
/*     */   }
/*     */ 
/*     */   
/* 102 */   public static void addSwampClayDisk(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_CLAY); }
/*     */ 
/*     */   
/*     */   public static void addMangroveSwampDisks(BiomeGenerationSettings.Builder builder) {
/* 106 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_GRASS);
/* 107 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_CLAY);
/*     */   }
/*     */ 
/*     */   
/* 111 */   public static void addMossyStoneBlock(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacements.FOREST_ROCK); }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public static void addFerns(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_LARGE_FERN); }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public static void addBushes(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_BUSH); }
/*     */ 
/*     */ 
/*     */   
/* 123 */   public static void addRareBerryBushes(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_BERRY_RARE); }
/*     */ 
/*     */ 
/*     */   
/* 127 */   public static void addCommonBerryBushes(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_BERRY_COMMON); }
/*     */ 
/*     */ 
/*     */   
/* 131 */   public static void addLightBambooVegetation(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BAMBOO_LIGHT); }
/*     */ 
/*     */   
/*     */   public static void addBambooVegetation(BiomeGenerationSettings.Builder builder) {
/* 135 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BAMBOO);
/* 136 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BAMBOO_VEGETATION);
/*     */   }
/*     */ 
/*     */   
/* 140 */   public static void addTaigaTrees(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_TAIGA); }
/*     */ 
/*     */ 
/*     */   
/* 144 */   public static void addGroveTrees(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_GROVE); }
/*     */ 
/*     */ 
/*     */   
/* 148 */   public static void addWaterTrees(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_WATER); }
/*     */ 
/*     */ 
/*     */   
/* 152 */   public static void addBirchTrees(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_BIRCH); }
/*     */ 
/*     */ 
/*     */   
/* 156 */   public static void addOtherBirchTrees(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_BIRCH_AND_OAK_LEAF_LITTER); }
/*     */ 
/*     */ 
/*     */   
/* 160 */   public static void addTallBirchTrees(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BIRCH_TALL); }
/*     */ 
/*     */ 
/*     */   
/* 164 */   public static void addBirchForestFlowers(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.WILDFLOWERS_BIRCH_FOREST); }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public static void addSavannaTrees(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_SAVANNA); }
/*     */ 
/*     */ 
/*     */   
/* 172 */   public static void addShatteredSavannaTrees(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_WINDSWEPT_SAVANNA); }
/*     */ 
/*     */   
/*     */   public static void addLushCavesVegetationFeatures(BiomeGenerationSettings.Builder builder) {
/* 176 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.LUSH_CAVES_CEILING_VEGETATION);
/* 177 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.CAVE_VINES);
/* 178 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.LUSH_CAVES_CLAY);
/* 179 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.LUSH_CAVES_VEGETATION);
/* 180 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.ROOTED_AZALEA_TREE);
/* 181 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.SPORE_BLOSSOM);
/* 182 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.CLASSIC_VINES);
/*     */   }
/*     */ 
/*     */   
/* 186 */   public static void addLushCavesSpecialOres(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_CLAY); }
/*     */ 
/*     */ 
/*     */   
/* 190 */   public static void addMountainTrees(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_WINDSWEPT_HILLS); }
/*     */ 
/*     */ 
/*     */   
/* 194 */   public static void addMountainForestTrees(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_WINDSWEPT_FOREST); }
/*     */ 
/*     */ 
/*     */   
/* 198 */   public static void addJungleTrees(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_JUNGLE); }
/*     */ 
/*     */ 
/*     */   
/* 202 */   public static void addSparseJungleTrees(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_SPARSE_JUNGLE); }
/*     */ 
/*     */ 
/*     */   
/* 206 */   public static void addBadlandsTrees(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_BADLANDS); }
/*     */ 
/*     */ 
/*     */   
/* 210 */   public static void addSnowyTrees(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_SNOWY); }
/*     */ 
/*     */ 
/*     */   
/* 214 */   public static void addJungleGrass(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_JUNGLE); }
/*     */ 
/*     */ 
/*     */   
/* 218 */   public static void addSavannaGrass(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS); }
/*     */ 
/*     */ 
/*     */   
/* 222 */   public static void addShatteredSavannaGrass(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_NORMAL); }
/*     */ 
/*     */ 
/*     */   
/* 226 */   public static void addSavannaExtraGrass(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_SAVANNA); }
/*     */ 
/*     */   
/*     */   public static void addBadlandGrass(BiomeGenerationSettings.Builder builder) {
/* 230 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_BADLANDS);
/* 231 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DRY_GRASS_BADLANDS);
/* 232 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH_BADLANDS);
/*     */   }
/*     */ 
/*     */   
/* 236 */   public static void addForestFlowers(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FOREST_FLOWERS); }
/*     */ 
/*     */ 
/*     */   
/* 240 */   public static void addForestGrass(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST); }
/*     */ 
/*     */   
/*     */   public static void addSwampVegetation(BiomeGenerationSettings.Builder builder) {
/* 244 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_SWAMP);
/* 245 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_SWAMP);
/* 246 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_NORMAL);
/* 247 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);
/* 248 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_WATERLILY);
/* 249 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_SWAMP);
/* 250 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_SWAMP);
/*     */   }
/*     */   
/*     */   public static void addMangroveSwampVegetation(BiomeGenerationSettings.Builder builder) {
/* 254 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_MANGROVE);
/* 255 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_NORMAL);
/* 256 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);
/* 257 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_WATERLILY);
/*     */   }
/*     */   
/*     */   public static void addMushroomFieldVegetation(BiomeGenerationSettings.Builder builder) {
/* 261 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.MUSHROOM_ISLAND_VEGETATION);
/* 262 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_TAIGA);
/* 263 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_TAIGA);
/*     */   }
/*     */   
/*     */   public static void addPlainVegetation(BiomeGenerationSettings.Builder builder) {
/* 267 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_PLAINS);
/* 268 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_PLAINS);
/* 269 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_PLAIN);
/*     */   }
/*     */   
/*     */   public static void addDesertVegetation(BiomeGenerationSettings.Builder builder) {
/* 273 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DRY_GRASS_DESERT);
/* 274 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH_2);
/*     */   }
/*     */   
/*     */   public static void addGiantTaigaVegetation(BiomeGenerationSettings.Builder builder) {
/* 278 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_TAIGA);
/* 279 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);
/* 280 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_OLD_GROWTH);
/* 281 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_OLD_GROWTH);
/*     */   }
/*     */ 
/*     */   
/* 285 */   public static void addDefaultFlowers(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_DEFAULT); }
/*     */ 
/*     */   
/*     */   public static void addCherryGroveVegetation(BiomeGenerationSettings.Builder builder) {
/* 289 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_PLAIN);
/* 290 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_CHERRY);
/* 291 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_CHERRY);
/*     */   }
/*     */   
/*     */   public static void addMeadowVegetation(BiomeGenerationSettings.Builder builder) {
/* 295 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_MEADOW);
/* 296 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_MEADOW);
/* 297 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_MEADOW);
/* 298 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.WILDFLOWERS_MEADOW);
/*     */   }
/*     */ 
/*     */   
/* 302 */   public static void addWarmFlowers(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_WARM); }
/*     */ 
/*     */ 
/*     */   
/* 306 */   public static void addDefaultGrass(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_BADLANDS); }
/*     */ 
/*     */   
/*     */   public static void addTaigaGrass(BiomeGenerationSettings.Builder builder) {
/* 310 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_TAIGA_2);
/* 311 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_TAIGA);
/* 312 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_TAIGA);
/*     */   }
/*     */ 
/*     */   
/* 316 */   public static void addPlainGrass(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS_2); }
/*     */ 
/*     */   
/*     */   public static void addDefaultMushrooms(BiomeGenerationSettings.Builder builder) {
/* 320 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_NORMAL);
/* 321 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_NORMAL);
/*     */   }
/*     */   
/*     */   public static void addDefaultExtraVegetation(BiomeGenerationSettings.Builder builder, boolean shouldGenerateNearWaterVegetation) {
/* 325 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);
/* 326 */     if (shouldGenerateNearWaterVegetation) {
/* 327 */       addNearWaterVegetation(builder);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void addNearWaterVegetation(BiomeGenerationSettings.Builder builder) {
/* 332 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE);
/* 333 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_FIREFLY_BUSH_NEAR_WATER);
/*     */   }
/*     */ 
/*     */   
/* 337 */   public static void addLeafLitterPatch(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_LEAF_LITTER); }
/*     */ 
/*     */   
/*     */   public static void addBadlandExtraVegetation(BiomeGenerationSettings.Builder builder) {
/* 341 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE_BADLANDS);
/* 342 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);
/* 343 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_CACTUS_DECORATED);
/* 344 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_FIREFLY_BUSH_NEAR_WATER);
/*     */   }
/*     */ 
/*     */   
/* 348 */   public static void addJungleMelons(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_MELON); }
/*     */ 
/*     */ 
/*     */   
/* 352 */   public static void addSparseJungleMelons(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_MELON_SPARSE); }
/*     */ 
/*     */ 
/*     */   
/* 356 */   public static void addJungleVines(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.VINES); }
/*     */ 
/*     */   
/*     */   public static void addDesertExtraVegetation(BiomeGenerationSettings.Builder builder) {
/* 360 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE_DESERT);
/* 361 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);
/* 362 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_CACTUS_DESERT);
/*     */   }
/*     */   
/*     */   public static void addSwampExtraVegetation(BiomeGenerationSettings.Builder builder) {
/* 366 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE_SWAMP);
/* 367 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);
/* 368 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_FIREFLY_BUSH_SWAMP);
/* 369 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP);
/*     */   }
/*     */   
/*     */   public static void addMangroveSwampExtraVegetation(BiomeGenerationSettings.Builder builder) {
/* 373 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_SWAMP);
/* 374 */     builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_FIREFLY_BUSH_NEAR_WATER);
/*     */   }
/*     */ 
/*     */   
/* 378 */   public static void addDesertExtraDecoration(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MiscOverworldPlacements.DESERT_WELL); }
/*     */ 
/*     */   
/*     */   public static void addFossilDecoration(BiomeGenerationSettings.Builder builder) {
/* 382 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.FOSSIL_UPPER);
/* 383 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.FOSSIL_LOWER);
/*     */   }
/*     */ 
/*     */   
/* 387 */   public static void addColdOceanExtraVegetation(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.KELP_COLD); }
/*     */ 
/*     */ 
/*     */   
/* 391 */   public static void addLukeWarmKelp(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.KELP_WARM); }
/*     */ 
/*     */   
/*     */   public static void addDefaultSprings(BiomeGenerationSettings.Builder builder) {
/* 395 */     builder.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, MiscOverworldPlacements.SPRING_WATER);
/* 396 */     builder.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, MiscOverworldPlacements.SPRING_LAVA);
/*     */   }
/*     */ 
/*     */   
/* 400 */   public static void addFrozenSprings(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, MiscOverworldPlacements.SPRING_LAVA_FROZEN); }
/*     */ 
/*     */   
/*     */   public static void addIcebergs(BiomeGenerationSettings.Builder builder) {
/* 404 */     builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacements.ICEBERG_PACKED);
/* 405 */     builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacements.ICEBERG_BLUE);
/*     */   }
/*     */ 
/*     */   
/* 409 */   public static void addBlueIce(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MiscOverworldPlacements.BLUE_ICE); }
/*     */ 
/*     */ 
/*     */   
/* 413 */   public static void addSurfaceFreezing(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MiscOverworldPlacements.FREEZE_TOP_LAYER); }
/*     */ 
/*     */   
/*     */   public static void addNetherDefaultOres(BiomeGenerationSettings.Builder builder) {
/* 417 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_GRAVEL_NETHER);
/* 418 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_BLACKSTONE);
/*     */     
/* 420 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_GOLD_NETHER);
/* 421 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_QUARTZ_NETHER);
/* 422 */     addAncientDebris(builder);
/*     */   }
/*     */   
/*     */   public static void addAncientDebris(BiomeGenerationSettings.Builder builder) {
/* 426 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_ANCIENT_DEBRIS_LARGE);
/* 427 */     builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_ANCIENT_DEBRIS_SMALL);
/*     */   }
/*     */ 
/*     */   
/* 431 */   public static void addDefaultCrystalFormations(BiomeGenerationSettings.Builder builder) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, CavePlacements.AMETHYST_GEODE); }
/*     */ 
/*     */   
/*     */   public static void farmAnimals(MobSpawnSettings.Builder builder) {
/* 435 */     builder.addSpawn(MobCategory.CREATURE, 12, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 4, 4));
/* 436 */     builder.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.PIG, 4, 4));
/* 437 */     builder.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 4, 4));
/* 438 */     builder.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.COW, 4, 4));
/*     */   }
/*     */   
/*     */   public static void caveSpawns(MobSpawnSettings.Builder builder) {
/* 442 */     builder.addSpawn(MobCategory.AMBIENT, 10, new MobSpawnSettings.SpawnerData(EntityType.BAT, 8, 8));
/* 443 */     builder.addSpawn(MobCategory.UNDERGROUND_WATER_CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.GLOW_SQUID, 4, 6));
/*     */   }
/*     */ 
/*     */   
/* 447 */   public static void commonSpawns(MobSpawnSettings.Builder builder) { commonSpawns(builder, 100); }
/*     */ 
/*     */   
/*     */   public static void commonSpawns(MobSpawnSettings.Builder builder, int skeletonWeight) {
/* 451 */     caveSpawns(builder);
/* 452 */     monsters(builder, 95, 5, 0, skeletonWeight, false);
/*     */   }
/*     */   
/*     */   public static void commonSpawnWithZombieHorse(MobSpawnSettings.Builder builder) {
/* 456 */     caveSpawns(builder);
/* 457 */     monsters(builder, 90, 5, 5, 100, false);
/*     */   }
/*     */   
/*     */   public static void swampSpawns(MobSpawnSettings.Builder builder, int swampSkeletonWeight) {
/* 461 */     commonSpawns(builder, swampSkeletonWeight);
/* 462 */     builder.addSpawn(MobCategory.MONSTER, 1, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 1, 1));
/* 463 */     builder.addSpawn(MobCategory.MONSTER, 30, new MobSpawnSettings.SpawnerData(EntityType.BOGGED, 4, 4));
/* 464 */     builder.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.FROG, 2, 5));
/*     */   }
/*     */   
/*     */   public static void oceanSpawns(MobSpawnSettings.Builder builder, int squidProbabilityWeight, int squidMaxCount, int codProbabilityWeight) {
/* 468 */     builder.addSpawn(MobCategory.WATER_CREATURE, squidProbabilityWeight, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 1, squidMaxCount));
/* 469 */     builder.addSpawn(MobCategory.WATER_AMBIENT, codProbabilityWeight, new MobSpawnSettings.SpawnerData(EntityType.COD, 3, 6));
/* 470 */     commonSpawns(builder);
/* 471 */     builder.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 1, 1));
/*     */   }
/*     */   
/*     */   public static void warmOceanSpawns(MobSpawnSettings.Builder builder, int squidProbabilityWeight, int squidMinCount) {
/* 475 */     builder.addSpawn(MobCategory.WATER_CREATURE, squidProbabilityWeight, new MobSpawnSettings.SpawnerData(EntityType.SQUID, squidMinCount, 4));
/* 476 */     builder.addSpawn(MobCategory.WATER_AMBIENT, 25, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 8, 8));
/* 477 */     builder.addSpawn(MobCategory.WATER_CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 1, 2));
/* 478 */     builder.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 1, 1));
/* 479 */     commonSpawns(builder);
/*     */   }
/*     */   
/*     */   public static void plainsSpawns(MobSpawnSettings.Builder builder) {
/* 483 */     farmAnimals(builder);
/* 484 */     builder.addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.HORSE, 2, 6));
/* 485 */     builder.addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.DONKEY, 1, 3));
/* 486 */     commonSpawnWithZombieHorse(builder);
/*     */   }
/*     */   
/*     */   public static void snowySpawns(MobSpawnSettings.Builder builder, boolean spawnZombieHorse) {
/* 490 */     builder.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 3));
/* 491 */     builder.addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.POLAR_BEAR, 1, 2));
/* 492 */     caveSpawns(builder);
/* 493 */     monsters(builder, spawnZombieHorse ? 90 : 95, 5, spawnZombieHorse ? 5 : 0, 20, false);
/* 494 */     builder.addSpawn(MobCategory.MONSTER, 80, new MobSpawnSettings.SpawnerData(EntityType.STRAY, 4, 4));
/*     */   }
/*     */   
/*     */   public static void desertSpawns(MobSpawnSettings.Builder builder) {
/* 498 */     builder.addSpawn(MobCategory.CREATURE, 12, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 3));
/* 499 */     builder.addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.CAMEL, 1, 1));
/* 500 */     caveSpawns(builder);
/* 501 */     monsters(builder, 19, 1, 0, 50, false);
/* 502 */     builder.addSpawn(MobCategory.MONSTER, 80, new MobSpawnSettings.SpawnerData(EntityType.HUSK, 4, 4));
/* 503 */     builder.addSpawn(MobCategory.MONSTER, 50, new MobSpawnSettings.SpawnerData(EntityType.PARCHED, 4, 4));
/*     */   }
/*     */   
/*     */   public static void dripstoneCavesSpawns(MobSpawnSettings.Builder builder) {
/* 507 */     caveSpawns(builder);
/* 508 */     int zombieWeight = 95;
/* 509 */     monsters(builder, 95, 5, 0, 100, false);
/* 510 */     builder.addSpawn(MobCategory.MONSTER, 95, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 4, 4));
/*     */   }
/*     */   
/*     */   public static void monsters(MobSpawnSettings.Builder builder, int zombieWeight, int zombieVillagerWeight, int zombieHorseWeight, int skeletonWeight, boolean drownedZombies) {
/* 514 */     builder.addSpawn(MobCategory.MONSTER, 100, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 4, 4));
/* 515 */     builder.addSpawn(MobCategory.MONSTER, zombieWeight, new MobSpawnSettings.SpawnerData(drownedZombies ? EntityType.DROWNED : EntityType.ZOMBIE, 4, 4));
/* 516 */     builder.addSpawn(MobCategory.MONSTER, zombieVillagerWeight, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_VILLAGER, 1, 1));
/* 517 */     if (zombieHorseWeight > 0) {
/* 518 */       builder.addSpawn(MobCategory.MONSTER, zombieHorseWeight, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_HORSE, 1, 1));
/*     */     }
/* 520 */     builder.addSpawn(MobCategory.MONSTER, skeletonWeight, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 4, 4));
/* 521 */     builder.addSpawn(MobCategory.MONSTER, 100, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 4, 4));
/* 522 */     builder.addSpawn(MobCategory.MONSTER, 100, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 4, 4));
/* 523 */     builder.addSpawn(MobCategory.MONSTER, 10, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 4));
/* 524 */     builder.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1));
/*     */   }
/*     */   
/*     */   public static void mooshroomSpawns(MobSpawnSettings.Builder builder) {
/* 528 */     builder.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.MOOSHROOM, 4, 8));
/* 529 */     caveSpawns(builder);
/*     */   }
/*     */   
/*     */   public static void baseJungleSpawns(MobSpawnSettings.Builder builder) {
/* 533 */     farmAnimals(builder);
/* 534 */     builder.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 4, 4));
/* 535 */     commonSpawns(builder);
/*     */   }
/*     */ 
/*     */   
/* 539 */   public static void endSpawns(MobSpawnSettings.Builder builder) { builder.addSpawn(MobCategory.MONSTER, 10, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 4, 4)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\BiomeDefaultFeatures.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */