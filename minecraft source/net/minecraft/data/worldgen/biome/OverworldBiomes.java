/*      */ package net.minecraft.data.worldgen.biome;
/*      */ 
/*      */ import net.minecraft.core.HolderGetter;
/*      */ import net.minecraft.data.worldgen.BiomeDefaultFeatures;
/*      */ import net.minecraft.data.worldgen.Carvers;
/*      */ import net.minecraft.data.worldgen.placement.AquaticPlacements;
/*      */ import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
/*      */ import net.minecraft.data.worldgen.placement.VegetationPlacements;
/*      */ import net.minecraft.sounds.Musics;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.util.ARGB;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.attribute.BackgroundMusic;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributeMap;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.attribute.modifier.FloatModifier;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.MobCategory;
/*      */ import net.minecraft.world.level.biome.Biome;
/*      */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*      */ import net.minecraft.world.level.biome.BiomeSpecialEffects;
/*      */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*      */ import net.minecraft.world.level.levelgen.GenerationStep;
/*      */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*      */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*      */ 
/*      */ public class OverworldBiomes {
/*      */   protected static final int NORMAL_WATER_COLOR = 4159204;
/*      */   private static final int DARK_DRY_FOLIAGE_COLOR = 8082228;
/*      */   public static final int SWAMP_SKELETON_WEIGHT = 70;
/*      */   
/*      */   public static int calculateSkyColor(float temperature) {
/*   33 */     float temp = temperature;
/*   34 */     temp /= 3.0F;
/*   35 */     temp = Mth.clamp(temp, -1.0F, 1.0F);
/*   36 */     return ARGB.opaque(Mth.hsvToRgb(0.62222224F - temp * 0.05F, 0.5F + temp * 0.1F, 1.0F));
/*      */   }
/*      */   
/*      */   private static Biome.BiomeBuilder baseBiome(float temperature, float downfall) {
/*   40 */     return (new Biome.BiomeBuilder())
/*   41 */       .hasPrecipitation(true)
/*   42 */       .temperature(temperature)
/*   43 */       .downfall(downfall)
/*   44 */       .setAttribute(EnvironmentAttributes.SKY_COLOR, Integer.valueOf(calculateSkyColor(temperature)))
/*   45 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*   46 */         .waterColor(4159204)
/*   47 */         .build());
/*      */   }
/*      */ 
/*      */   
/*      */   private static void globalOverworldGeneration(BiomeGenerationSettings.Builder generation) {
/*   52 */     BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
/*   53 */     BiomeDefaultFeatures.addDefaultCrystalFormations(generation);
/*   54 */     BiomeDefaultFeatures.addDefaultMonsterRoom(generation);
/*   55 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(generation);
/*   56 */     BiomeDefaultFeatures.addDefaultSprings(generation);
/*   57 */     BiomeDefaultFeatures.addSurfaceFreezing(generation);
/*      */   }
/*      */   
/*      */   public static Biome oldGrowthTaiga(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean spruce) {
/*   61 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*   62 */     BiomeDefaultFeatures.farmAnimals(mobs);
/*   63 */     mobs.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 4, 4));
/*   64 */     mobs.addSpawn(MobCategory.CREATURE, 4, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 3));
/*   65 */     mobs.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.FOX, 2, 4));
/*   66 */     if (spruce) {
/*   67 */       BiomeDefaultFeatures.commonSpawns(mobs);
/*      */     } else {
/*   69 */       BiomeDefaultFeatures.caveSpawns(mobs);
/*   70 */       BiomeDefaultFeatures.monsters(mobs, 100, 25, 0, 100, false);
/*      */     } 
/*      */     
/*   73 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*   75 */     globalOverworldGeneration(generation);
/*   76 */     BiomeDefaultFeatures.addMossyStoneBlock(generation);
/*   77 */     BiomeDefaultFeatures.addFerns(generation);
/*   78 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*   79 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*   80 */     generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, spruce ? VegetationPlacements.TREES_OLD_GROWTH_SPRUCE_TAIGA : VegetationPlacements.TREES_OLD_GROWTH_PINE_TAIGA);
/*   81 */     BiomeDefaultFeatures.addDefaultFlowers(generation);
/*   82 */     BiomeDefaultFeatures.addGiantTaigaVegetation(generation);
/*   83 */     BiomeDefaultFeatures.addDefaultMushrooms(generation);
/*   84 */     BiomeDefaultFeatures.addDefaultExtraVegetation(generation, true);
/*   85 */     BiomeDefaultFeatures.addCommonBerryBushes(generation);
/*      */     
/*   87 */     return baseBiome(spruce ? 0.25F : 0.3F, 0.8F)
/*   88 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_OLD_GROWTH_TAIGA))
/*   89 */       .mobSpawnSettings(mobs.build())
/*   90 */       .generationSettings(generation.build())
/*   91 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome sparseJungle(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/*   95 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*   96 */     BiomeDefaultFeatures.baseJungleSpawns(mobs);
/*   97 */     mobs.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 2, 4));
/*      */     
/*   99 */     return baseJungle(placedFeatures, carvers, 0.8F, false, true, false)
/*  100 */       .mobSpawnSettings(mobs.build())
/*  101 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_SPARSE_JUNGLE))
/*  102 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome jungle(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/*  106 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  107 */     BiomeDefaultFeatures.baseJungleSpawns(mobs);
/*  108 */     mobs.addSpawn(MobCategory.CREATURE, 40, new MobSpawnSettings.SpawnerData(EntityType.PARROT, 1, 2))
/*  109 */       .addSpawn(MobCategory.MONSTER, 2, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 1, 3))
/*  110 */       .addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.PANDA, 1, 2));
/*      */     
/*  112 */     return baseJungle(placedFeatures, carvers, 0.9F, false, false, true)
/*  113 */       .mobSpawnSettings(mobs.build())
/*  114 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_JUNGLE))
/*  115 */       .setAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT, Boolean.valueOf(true))
/*  116 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome bambooJungle(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/*  120 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  121 */     BiomeDefaultFeatures.baseJungleSpawns(mobs);
/*  122 */     mobs.addSpawn(MobCategory.CREATURE, 40, new MobSpawnSettings.SpawnerData(EntityType.PARROT, 1, 2))
/*  123 */       .addSpawn(MobCategory.CREATURE, 80, new MobSpawnSettings.SpawnerData(EntityType.PANDA, 1, 2))
/*  124 */       .addSpawn(MobCategory.MONSTER, 2, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 1, 1));
/*      */     
/*  126 */     return baseJungle(placedFeatures, carvers, 0.9F, true, false, true)
/*  127 */       .mobSpawnSettings(mobs.build())
/*  128 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_BAMBOO_JUNGLE))
/*  129 */       .setAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT, Boolean.valueOf(true))
/*  130 */       .build();
/*      */   }
/*      */   
/*      */   private static Biome.BiomeBuilder baseJungle(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers, float downfall, boolean bamboo, boolean sparse, boolean core) {
/*  134 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  136 */     globalOverworldGeneration(generation);
/*  137 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  138 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*  139 */     if (bamboo) {
/*  140 */       BiomeDefaultFeatures.addBambooVegetation(generation);
/*      */     } else {
/*  142 */       if (core) {
/*  143 */         BiomeDefaultFeatures.addLightBambooVegetation(generation);
/*      */       }
/*  145 */       if (sparse) {
/*  146 */         BiomeDefaultFeatures.addSparseJungleTrees(generation);
/*      */       } else {
/*  148 */         BiomeDefaultFeatures.addJungleTrees(generation);
/*      */       } 
/*      */     } 
/*  151 */     BiomeDefaultFeatures.addWarmFlowers(generation);
/*  152 */     BiomeDefaultFeatures.addJungleGrass(generation);
/*  153 */     BiomeDefaultFeatures.addDefaultMushrooms(generation);
/*  154 */     BiomeDefaultFeatures.addDefaultExtraVegetation(generation, true);
/*  155 */     BiomeDefaultFeatures.addJungleVines(generation);
/*  156 */     if (sparse) {
/*  157 */       BiomeDefaultFeatures.addSparseJungleMelons(generation);
/*      */     } else {
/*  159 */       BiomeDefaultFeatures.addJungleMelons(generation);
/*      */     } 
/*      */     
/*  162 */     return baseBiome(0.95F, downfall)
/*  163 */       .generationSettings(generation.build());
/*      */   }
/*      */   
/*      */   public static Biome windsweptHills(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean moreTrees) {
/*  167 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  168 */     BiomeDefaultFeatures.farmAnimals(mobs);
/*  169 */     mobs.addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.LLAMA, 4, 6));
/*  170 */     BiomeDefaultFeatures.commonSpawns(mobs);
/*      */     
/*  172 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  174 */     globalOverworldGeneration(generation);
/*  175 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  176 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*  177 */     if (moreTrees) {
/*  178 */       BiomeDefaultFeatures.addMountainForestTrees(generation);
/*      */     } else {
/*  180 */       BiomeDefaultFeatures.addMountainTrees(generation);
/*      */     } 
/*  182 */     BiomeDefaultFeatures.addBushes(generation);
/*  183 */     BiomeDefaultFeatures.addDefaultFlowers(generation);
/*  184 */     BiomeDefaultFeatures.addDefaultGrass(generation);
/*  185 */     BiomeDefaultFeatures.addDefaultMushrooms(generation);
/*  186 */     BiomeDefaultFeatures.addDefaultExtraVegetation(generation, true);
/*  187 */     BiomeDefaultFeatures.addExtraEmeralds(generation);
/*  188 */     BiomeDefaultFeatures.addInfestedStone(generation);
/*      */     
/*  190 */     return baseBiome(0.2F, 0.3F)
/*  191 */       .mobSpawnSettings(mobs.build())
/*  192 */       .generationSettings(generation.build())
/*  193 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome desert(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/*  197 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  198 */     BiomeDefaultFeatures.desertSpawns(mobs);
/*      */     
/*  200 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*  201 */     BiomeDefaultFeatures.addFossilDecoration(generation);
/*      */     
/*  203 */     globalOverworldGeneration(generation);
/*  204 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  205 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*  206 */     BiomeDefaultFeatures.addDefaultFlowers(generation);
/*  207 */     BiomeDefaultFeatures.addDefaultGrass(generation);
/*  208 */     BiomeDefaultFeatures.addDesertVegetation(generation);
/*  209 */     BiomeDefaultFeatures.addDefaultMushrooms(generation);
/*  210 */     BiomeDefaultFeatures.addDesertExtraVegetation(generation);
/*  211 */     BiomeDefaultFeatures.addDesertExtraDecoration(generation);
/*      */     
/*  213 */     return baseBiome(2.0F, 0.0F)
/*  214 */       .hasPrecipitation(false)
/*  215 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_DESERT))
/*  216 */       .setAttribute(EnvironmentAttributes.SNOW_GOLEM_MELTS, Boolean.valueOf(true))
/*  217 */       .mobSpawnSettings(mobs.build())
/*  218 */       .generationSettings(generation.build())
/*  219 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome plains(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean sunflower, boolean snowy, boolean spikes) {
/*  223 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  224 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  226 */     globalOverworldGeneration(generation);
/*      */     
/*  228 */     if (snowy) {
/*  229 */       mobs.creatureGenerationProbability(0.07F);
/*  230 */       BiomeDefaultFeatures.snowySpawns(mobs, !spikes);
/*      */       
/*  232 */       if (spikes) {
/*  233 */         generation.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MiscOverworldPlacements.ICE_SPIKE);
/*  234 */         generation.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MiscOverworldPlacements.ICE_PATCH);
/*      */       } 
/*      */     } else {
/*  237 */       BiomeDefaultFeatures.plainsSpawns(mobs);
/*  238 */       BiomeDefaultFeatures.addPlainGrass(generation);
/*  239 */       if (sunflower) {
/*  240 */         generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUNFLOWER);
/*      */       } else {
/*  242 */         BiomeDefaultFeatures.addBushes(generation);
/*      */       } 
/*      */     } 
/*      */     
/*  246 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  247 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*      */     
/*  249 */     if (snowy) {
/*  250 */       BiomeDefaultFeatures.addSnowyTrees(generation);
/*  251 */       BiomeDefaultFeatures.addDefaultFlowers(generation);
/*  252 */       BiomeDefaultFeatures.addDefaultGrass(generation);
/*      */     } else {
/*  254 */       BiomeDefaultFeatures.addPlainVegetation(generation);
/*      */     } 
/*      */     
/*  257 */     BiomeDefaultFeatures.addDefaultMushrooms(generation);
/*  258 */     BiomeDefaultFeatures.addDefaultExtraVegetation(generation, true);
/*      */     
/*  260 */     return baseBiome(snowy ? 0.0F : 0.8F, snowy ? 0.5F : 0.4F)
/*  261 */       .mobSpawnSettings(mobs.build())
/*  262 */       .generationSettings(generation.build())
/*  263 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome mushroomFields(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/*  267 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  268 */     BiomeDefaultFeatures.mooshroomSpawns(mobs);
/*      */     
/*  270 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  272 */     globalOverworldGeneration(generation);
/*  273 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  274 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*  275 */     BiomeDefaultFeatures.addMushroomFieldVegetation(generation);
/*  276 */     BiomeDefaultFeatures.addNearWaterVegetation(generation);
/*      */     
/*  278 */     return baseBiome(0.9F, 1.0F)
/*  279 */       .setAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT, Boolean.valueOf(true))
/*  280 */       .setAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, Boolean.valueOf(false))
/*  281 */       .mobSpawnSettings(mobs.build())
/*  282 */       .generationSettings(generation.build())
/*  283 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome savanna(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean shattered, boolean plateau) {
/*  287 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  289 */     globalOverworldGeneration(generation);
/*  290 */     if (!shattered) {
/*  291 */       BiomeDefaultFeatures.addSavannaGrass(generation);
/*      */     }
/*  293 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  294 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*  295 */     if (shattered) {
/*  296 */       BiomeDefaultFeatures.addShatteredSavannaTrees(generation);
/*  297 */       BiomeDefaultFeatures.addDefaultFlowers(generation);
/*  298 */       BiomeDefaultFeatures.addShatteredSavannaGrass(generation);
/*      */     } else {
/*  300 */       BiomeDefaultFeatures.addSavannaTrees(generation);
/*  301 */       BiomeDefaultFeatures.addWarmFlowers(generation);
/*  302 */       BiomeDefaultFeatures.addSavannaExtraGrass(generation);
/*      */     } 
/*  304 */     BiomeDefaultFeatures.addDefaultMushrooms(generation);
/*  305 */     BiomeDefaultFeatures.addDefaultExtraVegetation(generation, true);
/*      */     
/*  307 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  308 */     BiomeDefaultFeatures.farmAnimals(mobs);
/*  309 */     mobs.addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.HORSE, 2, 6))
/*  310 */       .addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.DONKEY, 1, 1))
/*  311 */       .addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.ARMADILLO, 2, 3));
/*      */     
/*  313 */     BiomeDefaultFeatures.commonSpawnWithZombieHorse(mobs);
/*      */     
/*  315 */     if (plateau) {
/*  316 */       mobs.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.LLAMA, 4, 4));
/*  317 */       mobs.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 4, 8));
/*      */     } 
/*      */     
/*  320 */     return baseBiome(2.0F, 0.0F)
/*  321 */       .hasPrecipitation(false)
/*  322 */       .setAttribute(EnvironmentAttributes.SNOW_GOLEM_MELTS, Boolean.valueOf(true))
/*  323 */       .mobSpawnSettings(mobs.build())
/*  324 */       .generationSettings(generation.build())
/*  325 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome badlands(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean wooded) {
/*  329 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  330 */     BiomeDefaultFeatures.farmAnimals(mobs);
/*  331 */     BiomeDefaultFeatures.commonSpawns(mobs);
/*  332 */     mobs.addSpawn(MobCategory.CREATURE, 6, new MobSpawnSettings.SpawnerData(EntityType.ARMADILLO, 1, 2));
/*  333 */     mobs.creatureGenerationProbability(0.03F);
/*  334 */     if (wooded) {
/*  335 */       mobs.addSpawn(MobCategory.CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 4, 8));
/*  336 */       mobs.creatureGenerationProbability(0.04F);
/*      */     } 
/*  338 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  340 */     globalOverworldGeneration(generation);
/*  341 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  342 */     BiomeDefaultFeatures.addExtraGold(generation);
/*  343 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*  344 */     if (wooded) {
/*  345 */       BiomeDefaultFeatures.addBadlandsTrees(generation);
/*      */     }
/*  347 */     BiomeDefaultFeatures.addBadlandGrass(generation);
/*  348 */     BiomeDefaultFeatures.addDefaultMushrooms(generation);
/*  349 */     BiomeDefaultFeatures.addBadlandExtraVegetation(generation);
/*  350 */     return baseBiome(2.0F, 0.0F)
/*  351 */       .hasPrecipitation(false)
/*  352 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_BADLANDS))
/*  353 */       .setAttribute(EnvironmentAttributes.SNOW_GOLEM_MELTS, Boolean.valueOf(true))
/*  354 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  355 */         .waterColor(4159204)
/*  356 */         .foliageColorOverride(10387789)
/*  357 */         .grassColorOverride(9470285)
/*  358 */         .build())
/*      */       
/*  360 */       .mobSpawnSettings(mobs.build())
/*  361 */       .generationSettings(generation.build())
/*  362 */       .build();
/*      */   }
/*      */   
/*      */   private static Biome.BiomeBuilder baseOcean() {
/*  366 */     return baseBiome(0.5F, 0.5F)
/*  367 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD.withUnderwater(Musics.UNDER_WATER));
/*      */   }
/*      */   
/*      */   private static BiomeGenerationSettings.Builder baseOceanGeneration(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/*  371 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  373 */     globalOverworldGeneration(generation);
/*  374 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  375 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*  376 */     BiomeDefaultFeatures.addWaterTrees(generation);
/*  377 */     BiomeDefaultFeatures.addDefaultFlowers(generation);
/*  378 */     BiomeDefaultFeatures.addDefaultGrass(generation);
/*  379 */     BiomeDefaultFeatures.addDefaultMushrooms(generation);
/*  380 */     BiomeDefaultFeatures.addDefaultExtraVegetation(generation, true);
/*  381 */     return generation;
/*      */   }
/*      */   
/*      */   public static Biome coldOcean(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean deep) {
/*  385 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  386 */     BiomeDefaultFeatures.oceanSpawns(mobs, 3, 4, 15);
/*  387 */     mobs.addSpawn(MobCategory.WATER_AMBIENT, 15, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 1, 5));
/*  388 */     mobs.addSpawn(MobCategory.WATER_CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.NAUTILUS, 1, 1));
/*      */     
/*  390 */     BiomeGenerationSettings.Builder generation = baseOceanGeneration(placedFeatures, carvers);
/*  391 */     generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, deep ? AquaticPlacements.SEAGRASS_DEEP_COLD : AquaticPlacements.SEAGRASS_COLD);
/*  392 */     BiomeDefaultFeatures.addColdOceanExtraVegetation(generation);
/*      */     
/*  394 */     return baseOcean()
/*  395 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  396 */         .waterColor(4020182)
/*  397 */         .build())
/*      */       
/*  399 */       .mobSpawnSettings(mobs.build())
/*  400 */       .generationSettings(generation.build())
/*  401 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome ocean(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean deep) {
/*  405 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  406 */     BiomeDefaultFeatures.oceanSpawns(mobs, 1, 4, 10);
/*  407 */     mobs.addSpawn(MobCategory.WATER_CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 1, 2))
/*  408 */       .addSpawn(MobCategory.WATER_CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.NAUTILUS, 1, 1));
/*      */     
/*  410 */     BiomeGenerationSettings.Builder generation = baseOceanGeneration(placedFeatures, carvers);
/*  411 */     generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, deep ? AquaticPlacements.SEAGRASS_DEEP : AquaticPlacements.SEAGRASS_NORMAL);
/*  412 */     BiomeDefaultFeatures.addColdOceanExtraVegetation(generation);
/*      */     
/*  414 */     return baseOcean()
/*  415 */       .mobSpawnSettings(mobs.build())
/*  416 */       .generationSettings(generation.build())
/*  417 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome lukeWarmOcean(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean deep) {
/*  421 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  422 */     if (deep) {
/*  423 */       BiomeDefaultFeatures.oceanSpawns(mobs, 8, 4, 8);
/*      */     } else {
/*  425 */       BiomeDefaultFeatures.oceanSpawns(mobs, 10, 2, 15);
/*      */     } 
/*  427 */     mobs.addSpawn(MobCategory.WATER_AMBIENT, 5, new MobSpawnSettings.SpawnerData(EntityType.PUFFERFISH, 1, 3))
/*  428 */       .addSpawn(MobCategory.WATER_AMBIENT, 25, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 8, 8))
/*  429 */       .addSpawn(MobCategory.WATER_CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 1, 2))
/*  430 */       .addSpawn(MobCategory.WATER_CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.NAUTILUS, 1, 1));
/*      */     
/*  432 */     BiomeGenerationSettings.Builder generation = baseOceanGeneration(placedFeatures, carvers);
/*  433 */     generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, deep ? AquaticPlacements.SEAGRASS_DEEP_WARM : AquaticPlacements.SEAGRASS_WARM);
/*  434 */     BiomeDefaultFeatures.addLukeWarmKelp(generation);
/*      */     
/*  436 */     return baseOcean()
/*  437 */       .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, Integer.valueOf(-16509389))
/*  438 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  439 */         .waterColor(4566514)
/*  440 */         .build())
/*      */       
/*  442 */       .mobSpawnSettings(mobs.build())
/*  443 */       .generationSettings(generation.build())
/*  444 */       .build();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Biome warmOcean(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/*  450 */     MobSpawnSettings.Builder mobs = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.WATER_AMBIENT, 15, new MobSpawnSettings.SpawnerData(EntityType.PUFFERFISH, 1, 3)).addSpawn(MobCategory.WATER_CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.NAUTILUS, 1, 1));
/*  451 */     BiomeDefaultFeatures.warmOceanSpawns(mobs, 10, 4);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  456 */     BiomeGenerationSettings.Builder generation = baseOceanGeneration(placedFeatures, carvers).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.WARM_OCEAN_VEGETATION).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_WARM).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEA_PICKLE);
/*      */     
/*  458 */     return baseOcean()
/*  459 */       .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, Integer.valueOf(-16507085))
/*  460 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  461 */         .waterColor(4445678)
/*  462 */         .build())
/*      */       
/*  464 */       .mobSpawnSettings(mobs.build())
/*  465 */       .generationSettings(generation.build())
/*  466 */       .build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Biome frozenOcean(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean deep) {
/*  474 */     MobSpawnSettings.Builder mobs = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.WATER_CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 1, 4)).addSpawn(MobCategory.WATER_AMBIENT, 15, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 1, 5)).addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.POLAR_BEAR, 1, 2)).addSpawn(MobCategory.WATER_CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.NAUTILUS, 1, 1));
/*  475 */     BiomeDefaultFeatures.commonSpawns(mobs);
/*  476 */     mobs.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 1, 1));
/*      */     
/*  478 */     float temperature = deep ? 0.5F : 0.0F;
/*  479 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  481 */     BiomeDefaultFeatures.addIcebergs(generation);
/*      */     
/*  483 */     globalOverworldGeneration(generation);
/*  484 */     BiomeDefaultFeatures.addBlueIce(generation);
/*  485 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  486 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*  487 */     BiomeDefaultFeatures.addWaterTrees(generation);
/*  488 */     BiomeDefaultFeatures.addDefaultFlowers(generation);
/*  489 */     BiomeDefaultFeatures.addDefaultGrass(generation);
/*  490 */     BiomeDefaultFeatures.addDefaultMushrooms(generation);
/*  491 */     BiomeDefaultFeatures.addDefaultExtraVegetation(generation, true);
/*      */     
/*  493 */     return baseBiome(temperature, 0.5F)
/*  494 */       .temperatureAdjustment(Biome.TemperatureModifier.FROZEN)
/*  495 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  496 */         .waterColor(3750089)
/*  497 */         .build())
/*      */       
/*  499 */       .mobSpawnSettings(mobs.build())
/*  500 */       .generationSettings(generation.build())
/*  501 */       .build();
/*      */   }
/*      */   public static Biome forest(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean birch, boolean tall, boolean flower) {
/*      */     BackgroundMusic music;
/*  505 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  507 */     globalOverworldGeneration(generation);
/*      */ 
/*      */     
/*  510 */     if (flower) {
/*  511 */       music = new BackgroundMusic(SoundEvents.MUSIC_BIOME_FLOWER_FOREST);
/*  512 */       generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_FOREST_FLOWERS);
/*      */     } else {
/*  514 */       music = new BackgroundMusic(SoundEvents.MUSIC_BIOME_FOREST);
/*  515 */       BiomeDefaultFeatures.addForestFlowers(generation);
/*      */     } 
/*      */     
/*  518 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  519 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*      */     
/*  521 */     if (flower) {
/*  522 */       generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_FLOWER_FOREST);
/*  523 */       generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_FLOWER_FOREST);
/*  524 */       BiomeDefaultFeatures.addDefaultGrass(generation);
/*      */     } else {
/*  526 */       if (birch) {
/*  527 */         BiomeDefaultFeatures.addBirchForestFlowers(generation);
/*  528 */         if (tall) {
/*  529 */           BiomeDefaultFeatures.addTallBirchTrees(generation);
/*      */         } else {
/*  531 */           BiomeDefaultFeatures.addBirchTrees(generation);
/*      */         } 
/*      */       } else {
/*  534 */         BiomeDefaultFeatures.addOtherBirchTrees(generation);
/*      */       } 
/*  536 */       BiomeDefaultFeatures.addBushes(generation);
/*  537 */       BiomeDefaultFeatures.addDefaultFlowers(generation);
/*  538 */       BiomeDefaultFeatures.addForestGrass(generation);
/*      */     } 
/*      */     
/*  541 */     BiomeDefaultFeatures.addDefaultMushrooms(generation);
/*  542 */     BiomeDefaultFeatures.addDefaultExtraVegetation(generation, true);
/*      */     
/*  544 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  545 */     BiomeDefaultFeatures.farmAnimals(mobs);
/*  546 */     BiomeDefaultFeatures.commonSpawns(mobs);
/*      */     
/*  548 */     if (flower) {
/*  549 */       mobs.addSpawn(MobCategory.CREATURE, 4, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 3));
/*  550 */     } else if (!birch) {
/*  551 */       mobs.addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 4, 4));
/*      */     } 
/*      */     
/*  554 */     return baseBiome(birch ? 0.6F : 0.7F, birch ? 0.6F : 0.8F)
/*  555 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, music)
/*  556 */       .mobSpawnSettings(mobs.build())
/*  557 */       .generationSettings(generation.build())
/*  558 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome taiga(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean snowy) {
/*  562 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  563 */     BiomeDefaultFeatures.farmAnimals(mobs);
/*  564 */     mobs.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 4, 4))
/*  565 */       .addSpawn(MobCategory.CREATURE, 4, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 3))
/*  566 */       .addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.FOX, 2, 4));
/*      */     
/*  568 */     BiomeDefaultFeatures.commonSpawns(mobs);
/*      */     
/*  570 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  572 */     globalOverworldGeneration(generation);
/*  573 */     BiomeDefaultFeatures.addFerns(generation);
/*  574 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  575 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*  576 */     BiomeDefaultFeatures.addTaigaTrees(generation);
/*  577 */     BiomeDefaultFeatures.addDefaultFlowers(generation);
/*  578 */     BiomeDefaultFeatures.addTaigaGrass(generation);
/*  579 */     BiomeDefaultFeatures.addDefaultExtraVegetation(generation, true);
/*  580 */     if (snowy) {
/*  581 */       BiomeDefaultFeatures.addRareBerryBushes(generation);
/*      */     } else {
/*  583 */       BiomeDefaultFeatures.addCommonBerryBushes(generation);
/*      */     } 
/*      */     
/*  586 */     int waterColor = snowy ? 4020182 : 4159204;
/*      */     
/*  588 */     return baseBiome(snowy ? -0.5F : 0.25F, snowy ? 0.4F : 0.8F)
/*  589 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  590 */         .waterColor(waterColor)
/*  591 */         .build())
/*  592 */       .mobSpawnSettings(mobs.build())
/*  593 */       .generationSettings(generation.build())
/*  594 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome darkForest(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean isPaleGarden) {
/*  598 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  599 */     if (!isPaleGarden) {
/*  600 */       BiomeDefaultFeatures.farmAnimals(mobs);
/*      */     }
/*  602 */     BiomeDefaultFeatures.commonSpawns(mobs);
/*      */     
/*  604 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  606 */     globalOverworldGeneration(generation);
/*  607 */     generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, isPaleGarden ? VegetationPlacements.PALE_GARDEN_VEGETATION : VegetationPlacements.DARK_FOREST_VEGETATION);
/*  608 */     if (!isPaleGarden) {
/*  609 */       BiomeDefaultFeatures.addForestFlowers(generation);
/*      */     } else {
/*  611 */       generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PALE_MOSS_PATCH);
/*  612 */       generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PALE_GARDEN_FLOWERS);
/*      */     } 
/*  614 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  615 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*  616 */     if (!isPaleGarden) {
/*  617 */       BiomeDefaultFeatures.addDefaultFlowers(generation);
/*      */     } else {
/*  619 */       generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_PALE_GARDEN);
/*      */     } 
/*  621 */     BiomeDefaultFeatures.addForestGrass(generation);
/*  622 */     if (!isPaleGarden) {
/*  623 */       BiomeDefaultFeatures.addDefaultMushrooms(generation);
/*  624 */       BiomeDefaultFeatures.addLeafLitterPatch(generation);
/*      */     } 
/*  626 */     BiomeDefaultFeatures.addDefaultExtraVegetation(generation, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  634 */     EnvironmentAttributeMap paleGardenAttributes = EnvironmentAttributeMap.builder().set(EnvironmentAttributes.SKY_COLOR, Integer.valueOf(-4605511)).set(EnvironmentAttributes.FOG_COLOR, Integer.valueOf(-8292496)).set(EnvironmentAttributes.WATER_FOG_COLOR, Integer.valueOf(-11179648)).set(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.EMPTY).set(EnvironmentAttributes.MUSIC_VOLUME, Float.valueOf(0.0F)).build();
/*      */ 
/*      */     
/*  637 */     EnvironmentAttributeMap darkForestAttributes = EnvironmentAttributeMap.builder().set(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_FOREST)).build();
/*      */     
/*  639 */     return baseBiome(0.7F, 0.8F)
/*  640 */       .putAttributes(isPaleGarden ? paleGardenAttributes : darkForestAttributes)
/*  641 */       .specialEffects(isPaleGarden ? (
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  647 */         new BiomeSpecialEffects.Builder()).waterColor(7768221).grassColorOverride(7832178).foliageColorOverride(8883574).dryFoliageColorOverride(10528412).build() : (
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  652 */         new BiomeSpecialEffects.Builder()).waterColor(4159204).dryFoliageColorOverride(8082228).grassColorModifier(BiomeSpecialEffects.GrassColorModifier.DARK_FOREST).build())
/*  653 */       .mobSpawnSettings(mobs.build())
/*  654 */       .generationSettings(generation.build())
/*  655 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome swamp(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/*  659 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*      */     
/*  661 */     BiomeDefaultFeatures.farmAnimals(mobs);
/*  662 */     BiomeDefaultFeatures.swampSpawns(mobs, 70);
/*      */     
/*  664 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  666 */     BiomeDefaultFeatures.addFossilDecoration(generation);
/*      */     
/*  668 */     globalOverworldGeneration(generation);
/*  669 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*      */     
/*  671 */     BiomeDefaultFeatures.addSwampClayDisk(generation);
/*  672 */     BiomeDefaultFeatures.addSwampVegetation(generation);
/*  673 */     BiomeDefaultFeatures.addDefaultMushrooms(generation);
/*  674 */     BiomeDefaultFeatures.addSwampExtraVegetation(generation);
/*  675 */     generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_SWAMP);
/*      */     
/*  677 */     return baseBiome(0.8F, 0.9F)
/*  678 */       .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, Integer.valueOf(-14474473))
/*  679 */       .modifyAttribute(EnvironmentAttributes.WATER_FOG_END_DISTANCE, FloatModifier.MULTIPLY, Float.valueOf(0.85F))
/*  680 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_SWAMP))
/*  681 */       .setAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT, Boolean.valueOf(true))
/*  682 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  683 */         .waterColor(6388580)
/*  684 */         .foliageColorOverride(6975545)
/*  685 */         .dryFoliageColorOverride(8082228)
/*  686 */         .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.SWAMP)
/*  687 */         .build())
/*      */       
/*  689 */       .mobSpawnSettings(mobs.build())
/*  690 */       .generationSettings(generation.build())
/*  691 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome mangroveSwamp(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/*  695 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  696 */     BiomeDefaultFeatures.swampSpawns(mobs, 70);
/*  697 */     mobs.addSpawn(MobCategory.WATER_AMBIENT, 25, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 8, 8));
/*      */     
/*  699 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  701 */     BiomeDefaultFeatures.addFossilDecoration(generation);
/*      */     
/*  703 */     globalOverworldGeneration(generation);
/*  704 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  705 */     BiomeDefaultFeatures.addMangroveSwampDisks(generation);
/*  706 */     BiomeDefaultFeatures.addMangroveSwampVegetation(generation);
/*  707 */     BiomeDefaultFeatures.addMangroveSwampExtraVegetation(generation);
/*      */     
/*  709 */     return baseBiome(0.8F, 0.9F)
/*  710 */       .setAttribute(EnvironmentAttributes.FOG_COLOR, Integer.valueOf(-4138753))
/*  711 */       .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, Integer.valueOf(-11699616))
/*  712 */       .modifyAttribute(EnvironmentAttributes.WATER_FOG_END_DISTANCE, FloatModifier.MULTIPLY, Float.valueOf(0.85F))
/*  713 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_SWAMP))
/*  714 */       .setAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT, Boolean.valueOf(true))
/*  715 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  716 */         .waterColor(3832426)
/*  717 */         .foliageColorOverride(9285927)
/*  718 */         .dryFoliageColorOverride(8082228)
/*  719 */         .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.SWAMP)
/*  720 */         .build())
/*      */       
/*  722 */       .mobSpawnSettings(mobs.build())
/*  723 */       .generationSettings(generation.build())
/*  724 */       .build();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Biome river(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean frozen) {
/*  730 */     MobSpawnSettings.Builder mobs = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.WATER_CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 1, 4)).addSpawn(MobCategory.WATER_AMBIENT, 5, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 1, 5));
/*  731 */     BiomeDefaultFeatures.commonSpawns(mobs);
/*  732 */     mobs.addSpawn(MobCategory.MONSTER, frozen ? 1 : 100, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 1, 1));
/*      */     
/*  734 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  736 */     globalOverworldGeneration(generation);
/*  737 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  738 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*  739 */     BiomeDefaultFeatures.addWaterTrees(generation);
/*  740 */     BiomeDefaultFeatures.addBushes(generation);
/*  741 */     BiomeDefaultFeatures.addDefaultFlowers(generation);
/*  742 */     BiomeDefaultFeatures.addDefaultGrass(generation);
/*  743 */     BiomeDefaultFeatures.addDefaultMushrooms(generation);
/*  744 */     BiomeDefaultFeatures.addDefaultExtraVegetation(generation, true);
/*      */     
/*  746 */     if (!frozen) {
/*  747 */       generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_RIVER);
/*      */     }
/*      */     
/*  750 */     return baseBiome(frozen ? 0.0F : 0.5F, 0.5F)
/*  751 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD.withUnderwater(Musics.UNDER_WATER))
/*  752 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  753 */         .waterColor(frozen ? 3750089 : 4159204)
/*  754 */         .build())
/*  755 */       .mobSpawnSettings(mobs.build())
/*  756 */       .generationSettings(generation.build())
/*  757 */       .build();
/*      */   }
/*      */   public static Biome beach(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean snowy, boolean stony) {
/*      */     float temperature;
/*  761 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  762 */     boolean sandy = (!stony && !snowy);
/*  763 */     if (sandy) {
/*  764 */       mobs.addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.TURTLE, 2, 5));
/*      */     }
/*  766 */     BiomeDefaultFeatures.commonSpawns(mobs);
/*      */     
/*  768 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  770 */     globalOverworldGeneration(generation);
/*  771 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  772 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*  773 */     BiomeDefaultFeatures.addDefaultFlowers(generation);
/*  774 */     BiomeDefaultFeatures.addDefaultGrass(generation);
/*  775 */     BiomeDefaultFeatures.addDefaultMushrooms(generation);
/*  776 */     BiomeDefaultFeatures.addDefaultExtraVegetation(generation, true);
/*      */ 
/*      */     
/*  779 */     if (snowy) {
/*  780 */       temperature = 0.05F;
/*  781 */     } else if (stony) {
/*  782 */       temperature = 0.2F;
/*      */     } else {
/*  784 */       temperature = 0.8F;
/*      */     } 
/*      */     
/*  787 */     int waterColor = snowy ? 4020182 : 4159204;
/*      */     
/*  789 */     return baseBiome(temperature, sandy ? 0.4F : 0.3F)
/*  790 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  791 */         .waterColor(waterColor)
/*  792 */         .build())
/*  793 */       .mobSpawnSettings(mobs.build())
/*  794 */       .generationSettings(generation.build())
/*  795 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome theVoid(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/*  799 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*  800 */     generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MiscOverworldPlacements.VOID_START_PLATFORM);
/*      */     
/*  802 */     return baseBiome(0.5F, 0.5F)
/*  803 */       .hasPrecipitation(false)
/*  804 */       .mobSpawnSettings((new MobSpawnSettings.Builder()).build())
/*  805 */       .generationSettings(generation.build())
/*  806 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome meadowOrCherryGrove(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers, boolean cherryGrove) {
/*  810 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  812 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  813 */     mobs.addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(cherryGrove ? EntityType.PIG : EntityType.DONKEY, 1, 2))
/*  814 */       .addSpawn(MobCategory.CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 6))
/*  815 */       .addSpawn(MobCategory.CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 2, 4));
/*  816 */     BiomeDefaultFeatures.commonSpawns(mobs);
/*      */     
/*  818 */     globalOverworldGeneration(generation);
/*  819 */     BiomeDefaultFeatures.addPlainGrass(generation);
/*      */     
/*  821 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  822 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*      */     
/*  824 */     if (cherryGrove) {
/*  825 */       BiomeDefaultFeatures.addCherryGroveVegetation(generation);
/*      */     } else {
/*  827 */       BiomeDefaultFeatures.addMeadowVegetation(generation);
/*      */     } 
/*      */     
/*  830 */     BiomeDefaultFeatures.addExtraEmeralds(generation);
/*  831 */     BiomeDefaultFeatures.addInfestedStone(generation);
/*      */     
/*  833 */     if (cherryGrove) {
/*      */ 
/*      */ 
/*      */       
/*  837 */       BiomeSpecialEffects.Builder effects = (new BiomeSpecialEffects.Builder()).waterColor(6141935).grassColorOverride(11983713).foliageColorOverride(11983713);
/*      */       
/*  839 */       return baseBiome(0.5F, 0.8F)
/*  840 */         .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, Integer.valueOf(-10635281))
/*  841 */         .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_CHERRY_GROVE))
/*  842 */         .specialEffects(effects.build())
/*  843 */         .mobSpawnSettings(mobs.build())
/*  844 */         .generationSettings(generation.build())
/*  845 */         .build();
/*      */     } 
/*      */     
/*  848 */     return baseBiome(0.5F, 0.8F)
/*  849 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_MEADOW))
/*  850 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  851 */         .waterColor(937679)
/*  852 */         .build())
/*  853 */       .mobSpawnSettings(mobs.build())
/*  854 */       .generationSettings(generation.build())
/*  855 */       .build();
/*      */   }
/*      */   
/*      */   private static Biome.BiomeBuilder basePeaks(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/*  859 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  861 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  862 */     mobs.addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.GOAT, 1, 3));
/*  863 */     BiomeDefaultFeatures.commonSpawns(mobs);
/*      */     
/*  865 */     globalOverworldGeneration(generation);
/*  866 */     BiomeDefaultFeatures.addFrozenSprings(generation);
/*  867 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  868 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*      */     
/*  870 */     BiomeDefaultFeatures.addExtraEmeralds(generation);
/*  871 */     BiomeDefaultFeatures.addInfestedStone(generation);
/*      */     
/*  873 */     return baseBiome(-0.7F, 0.9F)
/*  874 */       .setAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT, Boolean.valueOf(true))
/*  875 */       .mobSpawnSettings(mobs.build())
/*  876 */       .generationSettings(generation.build());
/*      */   }
/*      */ 
/*      */   
/*  880 */   public static Biome frozenPeaks(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) { return basePeaks(placedFeatures, carvers)
/*  881 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_FROZEN_PEAKS))
/*  882 */       .build(); }
/*      */ 
/*      */ 
/*      */   
/*  886 */   public static Biome jaggedPeaks(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) { return basePeaks(placedFeatures, carvers)
/*  887 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_JAGGED_PEAKS))
/*  888 */       .build(); }
/*      */ 
/*      */   
/*      */   public static Biome stonyPeaks(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/*  892 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  894 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  895 */     BiomeDefaultFeatures.commonSpawns(mobs);
/*      */     
/*  897 */     globalOverworldGeneration(generation);
/*  898 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  899 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*      */     
/*  901 */     BiomeDefaultFeatures.addExtraEmeralds(generation);
/*  902 */     BiomeDefaultFeatures.addInfestedStone(generation);
/*      */     
/*  904 */     return baseBiome(1.0F, 0.3F)
/*  905 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_STONY_PEAKS))
/*  906 */       .mobSpawnSettings(mobs.build())
/*  907 */       .generationSettings(generation.build())
/*  908 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome snowySlopes(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/*  912 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  914 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  915 */     mobs.addSpawn(MobCategory.CREATURE, 4, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 3))
/*  916 */       .addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.GOAT, 1, 3));
/*  917 */     BiomeDefaultFeatures.commonSpawns(mobs);
/*      */     
/*  919 */     globalOverworldGeneration(generation);
/*  920 */     BiomeDefaultFeatures.addFrozenSprings(generation);
/*  921 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  922 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*      */     
/*  924 */     BiomeDefaultFeatures.addDefaultExtraVegetation(generation, false);
/*  925 */     BiomeDefaultFeatures.addExtraEmeralds(generation);
/*  926 */     BiomeDefaultFeatures.addInfestedStone(generation);
/*      */     
/*  928 */     return baseBiome(-0.3F, 0.9F)
/*  929 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_SNOWY_SLOPES))
/*  930 */       .setAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT, Boolean.valueOf(true))
/*  931 */       .mobSpawnSettings(mobs.build())
/*  932 */       .generationSettings(generation.build())
/*  933 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome grove(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/*  937 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  939 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  940 */     mobs.addSpawn(MobCategory.CREATURE, 1, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 1, 1))
/*  941 */       .addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 3))
/*  942 */       .addSpawn(MobCategory.CREATURE, 4, new MobSpawnSettings.SpawnerData(EntityType.FOX, 2, 4));
/*  943 */     BiomeDefaultFeatures.commonSpawns(mobs);
/*      */     
/*  945 */     globalOverworldGeneration(generation);
/*  946 */     BiomeDefaultFeatures.addFrozenSprings(generation);
/*  947 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  948 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*      */     
/*  950 */     BiomeDefaultFeatures.addGroveTrees(generation);
/*      */     
/*  952 */     BiomeDefaultFeatures.addDefaultExtraVegetation(generation, false);
/*  953 */     BiomeDefaultFeatures.addExtraEmeralds(generation);
/*  954 */     BiomeDefaultFeatures.addInfestedStone(generation);
/*      */     
/*  956 */     return baseBiome(-0.2F, 0.8F)
/*  957 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_GROVE))
/*  958 */       .mobSpawnSettings(mobs.build())
/*  959 */       .generationSettings(generation.build())
/*  960 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome lushCaves(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/*  964 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  965 */     mobs.addSpawn(MobCategory.AXOLOTLS, 10, new MobSpawnSettings.SpawnerData(EntityType.AXOLOTL, 4, 6));
/*  966 */     mobs.addSpawn(MobCategory.WATER_AMBIENT, 25, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 8, 8));
/*  967 */     BiomeDefaultFeatures.commonSpawns(mobs);
/*      */     
/*  969 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  971 */     globalOverworldGeneration(generation);
/*  972 */     BiomeDefaultFeatures.addPlainGrass(generation);
/*      */     
/*  974 */     BiomeDefaultFeatures.addDefaultOres(generation);
/*  975 */     BiomeDefaultFeatures.addLushCavesSpecialOres(generation);
/*  976 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*      */     
/*  978 */     BiomeDefaultFeatures.addLushCavesVegetationFeatures(generation);
/*      */     
/*  980 */     return baseBiome(0.5F, 0.5F)
/*  981 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_LUSH_CAVES))
/*  982 */       .mobSpawnSettings(mobs.build())
/*  983 */       .generationSettings(generation.build())
/*  984 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome dripstoneCaves(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/*  988 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/*  989 */     BiomeDefaultFeatures.dripstoneCavesSpawns(mobs);
/*      */     
/*  991 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */     
/*  993 */     globalOverworldGeneration(generation);
/*  994 */     BiomeDefaultFeatures.addPlainGrass(generation);
/*      */ 
/*      */     
/*  997 */     BiomeDefaultFeatures.addDefaultOres(generation, true);
/*  998 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/*  999 */     BiomeDefaultFeatures.addPlainVegetation(generation);
/* 1000 */     BiomeDefaultFeatures.addDefaultMushrooms(generation);
/* 1001 */     BiomeDefaultFeatures.addDefaultExtraVegetation(generation, false);
/*      */     
/* 1003 */     BiomeDefaultFeatures.addDripstone(generation);
/*      */     
/* 1005 */     return baseBiome(0.8F, 0.4F)
/* 1006 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_DRIPSTONE_CAVES))
/* 1007 */       .mobSpawnSettings(mobs.build())
/* 1008 */       .generationSettings(generation.build())
/* 1009 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome deepDark(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/* 1013 */     MobSpawnSettings.Builder noMobs = new MobSpawnSettings.Builder();
/*      */     
/* 1015 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/*      */ 
/*      */     
/* 1018 */     generation.addCarver(Carvers.CAVE);
/* 1019 */     generation.addCarver(Carvers.CAVE_EXTRA_UNDERGROUND);
/* 1020 */     generation.addCarver(Carvers.CANYON);
/*      */     
/* 1022 */     BiomeDefaultFeatures.addDefaultCrystalFormations(generation);
/* 1023 */     BiomeDefaultFeatures.addDefaultMonsterRoom(generation);
/* 1024 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(generation);
/* 1025 */     BiomeDefaultFeatures.addSurfaceFreezing(generation);
/*      */     
/* 1027 */     BiomeDefaultFeatures.addPlainGrass(generation);
/*      */     
/* 1029 */     BiomeDefaultFeatures.addDefaultOres(generation);
/* 1030 */     BiomeDefaultFeatures.addDefaultSoftDisks(generation);
/* 1031 */     BiomeDefaultFeatures.addPlainVegetation(generation);
/* 1032 */     BiomeDefaultFeatures.addDefaultMushrooms(generation);
/* 1033 */     BiomeDefaultFeatures.addDefaultExtraVegetation(generation, false);
/*      */     
/* 1035 */     BiomeDefaultFeatures.addSculk(generation);
/*      */     
/* 1037 */     return baseBiome(0.8F, 0.4F)
/* 1038 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_DEEP_DARK))
/* 1039 */       .mobSpawnSettings(noMobs.build())
/* 1040 */       .generationSettings(generation.build())
/* 1041 */       .build();
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\biome\OverworldBiomes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */