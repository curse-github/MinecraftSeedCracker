 package net.minecraft.data.tags;
 
 import java.util.List;
 import java.util.concurrent.CompletableFuture;
 import net.minecraft.core.HolderLookup;
 import net.minecraft.core.registries.Registries;
 import net.minecraft.data.PackOutput;
 import net.minecraft.resources.ResourceKey;
 import net.minecraft.tags.BiomeTags;
 import net.minecraft.world.level.biome.Biome;
 import net.minecraft.world.level.biome.Biomes;
 import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
 
 public class BiomeTagsProvider
   extends KeyTagProvider<Biome>
 {
   public BiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) { super(output, Registries.BIOME, lookupProvider); }
 
 
   
   protected void addTags(HolderLookup.Provider registries) {
     tag(BiomeTags.IS_DEEP_OCEAN)
       .add(Biomes.DEEP_FROZEN_OCEAN)
       .add(Biomes.DEEP_COLD_OCEAN)
       .add(Biomes.DEEP_OCEAN)
       .add(Biomes.DEEP_LUKEWARM_OCEAN);
 
     
     tag(BiomeTags.IS_OCEAN)
       .addTag(BiomeTags.IS_DEEP_OCEAN)
       .add(Biomes.FROZEN_OCEAN)
       .add(Biomes.OCEAN)
       .add(Biomes.COLD_OCEAN)
       .add(Biomes.LUKEWARM_OCEAN)
       .add(Biomes.WARM_OCEAN);
 
     
     tag(BiomeTags.IS_BEACH)
       .add(Biomes.BEACH)
       .add(Biomes.SNOWY_BEACH);
 
     
     tag(BiomeTags.IS_RIVER)
       .add(Biomes.RIVER)
       .add(Biomes.FROZEN_RIVER);
 
     
     tag(BiomeTags.IS_MOUNTAIN)
       .add(Biomes.MEADOW)
       .add(Biomes.FROZEN_PEAKS)
       .add(Biomes.JAGGED_PEAKS)
       .add(Biomes.STONY_PEAKS)
       .add(Biomes.SNOWY_SLOPES)
       .add(Biomes.CHERRY_GROVE);
 
     
     tag(BiomeTags.IS_BADLANDS)
       .add(Biomes.BADLANDS)
       .add(Biomes.ERODED_BADLANDS)
       .add(Biomes.WOODED_BADLANDS);
 
     
     tag(BiomeTags.IS_HILL)
       .add(Biomes.WINDSWEPT_HILLS)
       .add(Biomes.WINDSWEPT_FOREST)
       .add(Biomes.WINDSWEPT_GRAVELLY_HILLS);
 
     
     tag(BiomeTags.IS_TAIGA)
       .add(Biomes.TAIGA)
       .add(Biomes.SNOWY_TAIGA)
       .add(Biomes.OLD_GROWTH_PINE_TAIGA)
       .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA);
 
     
     tag(BiomeTags.IS_JUNGLE)
       .add(Biomes.BAMBOO_JUNGLE)
       .add(Biomes.JUNGLE)
       .add(Biomes.SPARSE_JUNGLE);
 
     
     tag(BiomeTags.IS_FOREST)
       .add(Biomes.FOREST)
       .add(Biomes.FLOWER_FOREST)
       .add(Biomes.BIRCH_FOREST)
       .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
       .add(Biomes.DARK_FOREST)
       .add(Biomes.PALE_GARDEN)
       .add(Biomes.GROVE);
 
     
     tag(BiomeTags.IS_SAVANNA)
       .add(Biomes.SAVANNA)
       .add(Biomes.SAVANNA_PLATEAU)
       .add(Biomes.WINDSWEPT_SAVANNA);
 
     
     tag(BiomeTags.IS_NETHER).addAll(MultiNoiseBiomeSourceParameterList.Preset.NETHER.usedBiomes());
     
     List<ResourceKey<Biome>> overworldBiomes = MultiNoiseBiomeSourceParameterList.Preset.OVERWORLD.usedBiomes().toList();
     tag(BiomeTags.IS_OVERWORLD).addAll(overworldBiomes);
     
     tag(BiomeTags.IS_END)
       .add(Biomes.THE_END)
       .add(Biomes.END_HIGHLANDS)
       .add(Biomes.END_MIDLANDS)
       .add(Biomes.SMALL_END_ISLANDS)
       .add(Biomes.END_BARRENS);
 
 
     
     tag(BiomeTags.HAS_BURIED_TREASURE)
       .addTag(BiomeTags.IS_BEACH);
 
     
     tag(BiomeTags.HAS_DESERT_PYRAMID)
       .add(Biomes.DESERT);
 
     
     tag(BiomeTags.HAS_IGLOO)
       .add(Biomes.SNOWY_TAIGA)
       .add(Biomes.SNOWY_PLAINS)
       .add(Biomes.SNOWY_SLOPES);
 
     
     tag(BiomeTags.HAS_JUNGLE_TEMPLE)
       .add(Biomes.BAMBOO_JUNGLE)
       .add(Biomes.JUNGLE);
 
     
     tag(BiomeTags.HAS_MINESHAFT)
       .addTag(BiomeTags.IS_OCEAN)
       .addTag(BiomeTags.IS_RIVER)
       .addTag(BiomeTags.IS_BEACH)
       .addTag(BiomeTags.IS_MOUNTAIN)
       .addTag(BiomeTags.IS_HILL)
       .addTag(BiomeTags.IS_TAIGA)
       .addTag(BiomeTags.IS_JUNGLE)
       .addTag(BiomeTags.IS_FOREST)
       .add(Biomes.STONY_SHORE)
       .add(Biomes.MUSHROOM_FIELDS)
       .add(Biomes.ICE_SPIKES)
       .add(Biomes.WINDSWEPT_SAVANNA)
       .add(Biomes.DESERT)
       .add(Biomes.SAVANNA)
       .add(Biomes.SNOWY_PLAINS)
       .add(Biomes.PLAINS)
       .add(Biomes.SUNFLOWER_PLAINS)
       .add(Biomes.SWAMP)
       .add(Biomes.MANGROVE_SWAMP)
       .add(Biomes.SAVANNA_PLATEAU)
       .add(Biomes.DRIPSTONE_CAVES)
       .add(Biomes.LUSH_CAVES);
 
     
     tag(BiomeTags.HAS_MINESHAFT_MESA)
       .addTag(BiomeTags.IS_BADLANDS);
 
     
     tag(BiomeTags.MINESHAFT_BLOCKING)
       .add(Biomes.DEEP_DARK);
 
     
     tag(BiomeTags.HAS_OCEAN_MONUMENT)
       .addTag(BiomeTags.IS_DEEP_OCEAN);
 
     
     tag(BiomeTags.REQUIRED_OCEAN_MONUMENT_SURROUNDING)
       .addTag(BiomeTags.IS_OCEAN)
       .addTag(BiomeTags.IS_RIVER);
 
     
     tag(BiomeTags.HAS_OCEAN_RUIN_COLD)
       .add(Biomes.FROZEN_OCEAN)
       .add(Biomes.COLD_OCEAN)
       .add(Biomes.OCEAN)
       .add(Biomes.DEEP_FROZEN_OCEAN)
       .add(Biomes.DEEP_COLD_OCEAN)
       .add(Biomes.DEEP_OCEAN);
 
     
     tag(BiomeTags.HAS_OCEAN_RUIN_WARM)
       .add(Biomes.LUKEWARM_OCEAN)
       .add(Biomes.WARM_OCEAN)
       .add(Biomes.DEEP_LUKEWARM_OCEAN);
 
     
     tag(BiomeTags.HAS_PILLAGER_OUTPOST)
       .add(Biomes.DESERT)
       .add(Biomes.PLAINS)
       .add(Biomes.SAVANNA)
       .add(Biomes.SNOWY_PLAINS)
       .add(Biomes.TAIGA)
       .addTag(BiomeTags.IS_MOUNTAIN)
       .add(Biomes.GROVE);
 
     
     tag(BiomeTags.HAS_RUINED_PORTAL_DESERT)
       .add(Biomes.DESERT);
 
     
     tag(BiomeTags.HAS_RUINED_PORTAL_JUNGLE)
       .addTag(BiomeTags.IS_JUNGLE);
 
     
     tag(BiomeTags.HAS_RUINED_PORTAL_OCEAN)
       .addTag(BiomeTags.IS_OCEAN);
 
     
     tag(BiomeTags.HAS_RUINED_PORTAL_SWAMP)
       .add(Biomes.SWAMP)
       .add(Biomes.MANGROVE_SWAMP);
 
     
     tag(BiomeTags.HAS_RUINED_PORTAL_MOUNTAIN)
       .addTag(BiomeTags.IS_BADLANDS)
       .addTag(BiomeTags.IS_HILL)
       .add(Biomes.SAVANNA_PLATEAU)
       .add(Biomes.WINDSWEPT_SAVANNA)
       .add(Biomes.STONY_SHORE)
       .addTag(BiomeTags.IS_MOUNTAIN);
 
     
     tag(BiomeTags.HAS_RUINED_PORTAL_STANDARD)
       .addTag(BiomeTags.IS_BEACH)
       .addTag(BiomeTags.IS_RIVER)
       .addTag(BiomeTags.IS_TAIGA)
       .addTag(BiomeTags.IS_FOREST)
       .add(Biomes.MUSHROOM_FIELDS)
       .add(Biomes.ICE_SPIKES)
       .add(Biomes.DRIPSTONE_CAVES)
       .add(Biomes.LUSH_CAVES)
       .add(Biomes.SAVANNA)
       .add(Biomes.SNOWY_PLAINS)
       .add(Biomes.PLAINS)
       .add(Biomes.SUNFLOWER_PLAINS);
 
     
     tag(BiomeTags.HAS_SHIPWRECK_BEACHED)
       .addTag(BiomeTags.IS_BEACH);
 
     
     tag(BiomeTags.HAS_SHIPWRECK)
       .addTag(BiomeTags.IS_OCEAN);
 
     
     tag(BiomeTags.HAS_SWAMP_HUT)
       .add(Biomes.SWAMP);
 
     
     tag(BiomeTags.HAS_VILLAGE_DESERT)
       .add(Biomes.DESERT);
 
     
     tag(BiomeTags.HAS_VILLAGE_PLAINS)
       .add(Biomes.PLAINS)
       .add(Biomes.MEADOW);
 
     
     tag(BiomeTags.HAS_VILLAGE_SAVANNA)
       .add(Biomes.SAVANNA);
 
     
     tag(BiomeTags.HAS_VILLAGE_SNOWY)
       .add(Biomes.SNOWY_PLAINS);
 
     
     tag(BiomeTags.HAS_VILLAGE_TAIGA)
       .add(Biomes.TAIGA);
 
     
     tag(BiomeTags.HAS_TRAIL_RUINS)
       .add(Biomes.TAIGA)
       .add(Biomes.SNOWY_TAIGA)
       .add(Biomes.OLD_GROWTH_PINE_TAIGA)
       .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
       .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
       .add(Biomes.JUNGLE);
 
     
     tag(BiomeTags.HAS_WOODLAND_MANSION)
       .add(Biomes.DARK_FOREST)
       .add(Biomes.PALE_GARDEN);
 
     
     tag(BiomeTags.STRONGHOLD_BIASED_TO)
       .add(Biomes.PLAINS)
       .add(Biomes.SUNFLOWER_PLAINS)
       .add(Biomes.SNOWY_PLAINS)
       .add(Biomes.ICE_SPIKES)
       .add(Biomes.DESERT)
       .add(Biomes.FOREST)
       .add(Biomes.FLOWER_FOREST)
       .add(Biomes.BIRCH_FOREST)
       .add(Biomes.DARK_FOREST)
       .add(Biomes.PALE_GARDEN)
       .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
       .add(Biomes.OLD_GROWTH_PINE_TAIGA)
       .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
       .add(Biomes.TAIGA)
       .add(Biomes.SNOWY_TAIGA)
       .add(Biomes.SAVANNA)
       .add(Biomes.SAVANNA_PLATEAU)
       .add(Biomes.WINDSWEPT_HILLS)
       .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
       .add(Biomes.WINDSWEPT_FOREST)
       .add(Biomes.WINDSWEPT_SAVANNA)
       .add(Biomes.JUNGLE)
       .add(Biomes.SPARSE_JUNGLE)
       .add(Biomes.BAMBOO_JUNGLE)
       .add(Biomes.BADLANDS)
       .add(Biomes.ERODED_BADLANDS)
       .add(Biomes.WOODED_BADLANDS)
       .add(Biomes.MEADOW)
       .add(Biomes.CHERRY_GROVE)
       .add(Biomes.GROVE)
       .add(Biomes.SNOWY_SLOPES)
       .add(Biomes.FROZEN_PEAKS)
       .add(Biomes.JAGGED_PEAKS)
       .add(Biomes.STONY_PEAKS)
       .add(Biomes.MUSHROOM_FIELDS)
       .add(Biomes.DRIPSTONE_CAVES)
       .add(Biomes.LUSH_CAVES);
 
     
     tag(BiomeTags.HAS_STRONGHOLD)
       .addTag(BiomeTags.IS_OVERWORLD);
     
     tag(BiomeTags.HAS_TRIAL_CHAMBERS).addAll(overworldBiomes.stream().filter(biomeKey -> (biomeKey != Biomes.DEEP_DARK)));
 
     
     tag(BiomeTags.HAS_NETHER_FORTRESS)
       .addTag(BiomeTags.IS_NETHER);
 
     
     tag(BiomeTags.HAS_NETHER_FOSSIL)
       .add(Biomes.SOUL_SAND_VALLEY);
 
     
     tag(BiomeTags.HAS_BASTION_REMNANT)
       .add(Biomes.CRIMSON_FOREST)
       .add(Biomes.NETHER_WASTES)
       .add(Biomes.SOUL_SAND_VALLEY)
       .add(Biomes.WARPED_FOREST);
 
     
     tag(BiomeTags.HAS_ANCIENT_CITY)
       .add(Biomes.DEEP_DARK);
 
     
     tag(BiomeTags.HAS_RUINED_PORTAL_NETHER)
       .addTag(BiomeTags.IS_NETHER);
 
 
     
     tag(BiomeTags.HAS_END_CITY)
       .add(Biomes.END_HIGHLANDS)
       .add(Biomes.END_MIDLANDS);
 
 
     
     tag(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL)
       .add(Biomes.WARM_OCEAN);
 
 
     
     tag(BiomeTags.WATER_ON_MAP_OUTLINES)
       .addTag(BiomeTags.IS_OCEAN)
       .addTag(BiomeTags.IS_RIVER)
       .add(Biomes.SWAMP)
       .add(Biomes.MANGROVE_SWAMP);
 
 
     
     tag(BiomeTags.WITHOUT_ZOMBIE_SIEGES)
       .add(Biomes.MUSHROOM_FIELDS);
 
     
     tag(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS)
       .add(Biomes.THE_VOID);
 
     
     tag(BiomeTags.SPAWNS_COLD_VARIANT_FROGS)
       .add(Biomes.SNOWY_PLAINS)
       .add(Biomes.ICE_SPIKES)
       .add(Biomes.FROZEN_PEAKS)
       .add(Biomes.JAGGED_PEAKS)
       .add(Biomes.SNOWY_SLOPES)
       .add(Biomes.FROZEN_OCEAN)
       .add(Biomes.DEEP_FROZEN_OCEAN)
       .add(Biomes.GROVE)
       .add(Biomes.DEEP_DARK)
       .add(Biomes.FROZEN_RIVER)
       .add(Biomes.SNOWY_TAIGA)
       .add(Biomes.SNOWY_BEACH)
       .addTag(BiomeTags.IS_END);
 
     
     tag(BiomeTags.SPAWNS_WARM_VARIANT_FROGS)
       .add(Biomes.DESERT)
       .add(Biomes.WARM_OCEAN)
       .addTag(BiomeTags.IS_JUNGLE)
       .addTag(BiomeTags.IS_SAVANNA)
       .addTag(BiomeTags.IS_NETHER)
       .addTag(BiomeTags.IS_BADLANDS)
       .add(Biomes.MANGROVE_SWAMP);
 
     
     tag(BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS)
       .add(Biomes.SNOWY_PLAINS)
       .add(Biomes.ICE_SPIKES)
       .add(Biomes.FROZEN_PEAKS)
       .add(Biomes.JAGGED_PEAKS)
       .add(Biomes.SNOWY_SLOPES)
       .add(Biomes.FROZEN_OCEAN)
       .add(Biomes.DEEP_FROZEN_OCEAN)
       .add(Biomes.GROVE)
       .add(Biomes.DEEP_DARK)
       .add(Biomes.FROZEN_RIVER)
       .add(Biomes.SNOWY_TAIGA)
       .add(Biomes.SNOWY_BEACH)
       .addTag(BiomeTags.IS_END)
       .add(Biomes.COLD_OCEAN)
       .add(Biomes.DEEP_COLD_OCEAN)
       .add(Biomes.OLD_GROWTH_PINE_TAIGA)
       .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
       .add(Biomes.TAIGA)
       .add(Biomes.WINDSWEPT_FOREST)
       .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
       .add(Biomes.WINDSWEPT_HILLS)
       .add(Biomes.STONY_PEAKS);
 
     
     tag(BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS)
       .add(Biomes.DESERT)
       .add(Biomes.WARM_OCEAN)
       .addTag(BiomeTags.IS_JUNGLE)
       .addTag(BiomeTags.IS_SAVANNA)
       .addTag(BiomeTags.IS_NETHER)
       .addTag(BiomeTags.IS_BADLANDS)
       .add(Biomes.MANGROVE_SWAMP)
       .add(Biomes.DEEP_LUKEWARM_OCEAN)
       .add(Biomes.LUKEWARM_OCEAN);
 
     
     tag(BiomeTags.SPAWNS_GOLD_RABBITS)
       .add(Biomes.DESERT);
 
     
     tag(BiomeTags.SPAWNS_WHITE_RABBITS)
       .add(Biomes.SNOWY_PLAINS)
       .add(Biomes.ICE_SPIKES)
       .add(Biomes.FROZEN_OCEAN)
       .add(Biomes.SNOWY_TAIGA)
       .add(Biomes.FROZEN_RIVER)
       .add(Biomes.SNOWY_BEACH)
       .add(Biomes.FROZEN_PEAKS)
       .add(Biomes.JAGGED_PEAKS)
       .add(Biomes.SNOWY_SLOPES)
       .add(Biomes.GROVE);
 
     
     tag(BiomeTags.REDUCED_WATER_AMBIENT_SPAWNS)
       .addTag(BiomeTags.IS_RIVER);
 
     
     tag(BiomeTags.ALLOWS_TROPICAL_FISH_SPAWNS_AT_ANY_HEIGHT)
       .add(Biomes.LUSH_CAVES);
 
     
     tag(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS)
       .add(Biomes.FROZEN_OCEAN)
       .add(Biomes.DEEP_FROZEN_OCEAN);
 
     
     tag(BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS)
       .addTag(BiomeTags.IS_RIVER);
 
     
     tag(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS)
       .add(Biomes.SWAMP)
       .add(Biomes.MANGROVE_SWAMP);
 
     
     tag(BiomeTags.SPAWNS_SNOW_FOXES)
       .add(Biomes.SNOWY_PLAINS)
       .add(Biomes.ICE_SPIKES)
       .add(Biomes.FROZEN_OCEAN)
       .add(Biomes.SNOWY_TAIGA)
       .add(Biomes.FROZEN_RIVER)
       .add(Biomes.SNOWY_BEACH)
       .add(Biomes.FROZEN_PEAKS)
       .add(Biomes.JAGGED_PEAKS)
       .add(Biomes.SNOWY_SLOPES)
       .add(Biomes.GROVE);
 
     
     tag(BiomeTags.SPAWNS_CORAL_VARIANT_ZOMBIE_NAUTILUS)
       .add(Biomes.WARM_OCEAN);
   }
 }


