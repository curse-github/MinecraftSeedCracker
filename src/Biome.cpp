#include "Biome.h"

//  ChunkGenerator.setStructureStarts function in ChunkGenerator.java
//      StructurePlacement.shouldGenerate function in StructurePlacement.java
//          uses ConcentricRingsStructurePlacement.isStartChunk function in ConcentricRingsStructurePlacement.java
//              uses StructurePlacementCalculator.getPlacementPositions function in StructurePlacementCalculator.java
//                  // note, this is constructued in the ChunkGenerator class
//                      // ChunkGenerator is indirectly constructed in NoiseChunkGenerator class
//IMPORTANT                 // NoiseChunkGenerator is constructed in the WorldPresets.Registrar.bootstrap function in WorldPresets.java, with settings coming from "/worldgen/noise_settings/_.json"
//                          // the NoiseChunkGenerator constructor is also passed a BiomeSource object constructed with the MultiNoiseBiomeSource.create function
//                          // MultiNoiseBiomeSource object is constructed with settings from a MultiNoiseBiomeSourceParameterList object called MultiNoiseBiomeSourceParameterLists.OVERWORLD
//                              // MultiNoiseBiomeSourceParameterLists.OVERWORLD is constructed with the MultiNoiseBiomeSourceParameterList constructor
//                              // the MultiNoiseBiomeSourceParameterList object is constructed with settings from MultiNoiseBiomeSourceParameterList.Preset.OVERWORLD
//                              // the MultiNoiseBiomeSourceParameterList.entries variable comes from the MultiNoiseBiomeSourceParameterList.Preset.OVERWORLD.biomeSourceFunction.apply function
//                                  // this function returns the value of calling the MultiNoiseBiomeSourceParameterList.Preset.getOverworldEntries function
//IMPORTANT                             // this function returns the value from calling the VanillaBiomeParameters.writeOverworldBiomeParameters function, converted to a list
//IMPORTANT                                 // calls VanillaBiomeParameters.writeOverworldBiomeParameters
//IMPORTANT                                 // calls VanillaBiomeParameters.writeLandBiomes
//IMPORTANT                                 // calls VanillaBiomeParameters.writeCaveBiomes
//                  uses StructurePlacementCalculator.tryCalculate function in StructurePlacementCalculator.java
//                      uses StructurePlacementCalculator.calculate function in StructurePlacementCalculator.java
//                          uses StructurePlacementCalculator.calculateConcentricsRingPlacementPos function in StructurePlacementCalculator.java
//                              // NoiseConfig used in this function is passed in in the StructurePlacementCalculator constructor
//                                  // the StructurePlacementCalculator constructor is called in the StructurePlacementCalculator.create function
//                                      // StructurePlacementCalculator.create is called in ChunkGenerator.createStructurePlacementCalculator
//                                          // ChunkGenerator.createStructurePlacementCalculator is called in the ServerChunkLoadingManager constructor
//                                              // NoiseChunkGenerator.settings are set in the NoiseChunkGenerator constructor
//                                                  // the NoiseChunkGenerator constructor is called in the WorldPresets.Registrar class
//                                                      // it gets the settings using presetRegisterable<WorldPreset>.lookup("worldgen/noise_settings").get(RegistryKey("worldgen/noise_settings", "minecraft:overworld"))
//                                                      // in other words, just the "worldgen/noise_settings/overworld.json" file
//IMPORTANT                                                 // with the "worldgen/noise_settings/overworld.json" file, "worldgen/noise/_.json" files, and the world seed
//IMPORTANT                                                 // NoiseConfig.create(ChunkGeneratorSettings, RegistryEntryLookup<NoiseParameters>, long)
//IMPORTANT                                                     // NoiseConfig.getMultiNoiseSampler can be used to create the MultiNoiseUtil.MultiNoiseSampler used in the MultiNoiseBiomeSource class
//                                                                  // NoiseConfig.getMultiNoiseSampler function returns multiNoiseSampler which is created in NoiseConfig constructor
struct ParameterRange {
    float min;
    float max;
    ParameterRange(const float& _min, const float& _max) : min(_min), max(_max) {}
    ParameterRange(const float& value) : min(value), max(value) {}
    ParameterRange(const ParameterRange& copy) : min(copy.min), max(copy.max) {}
    static ParameterRange combine(const ParameterRange& min, const ParameterRange& max) {
        return ParameterRange(min.min, max.max);
    }
};
struct BiomeParameter {
    ParameterRange temperature;
    ParameterRange humidity;
    ParameterRange continentalness;
    ParameterRange erosion;
    ParameterRange depth;
    ParameterRange weirdness;
    float offset;
    std::string biome;
    BiomeParameter(const ParameterRange& _temperature, const ParameterRange& _humidity, const ParameterRange& _continentalness, const ParameterRange& _erosion, const ParameterRange& _depth, const ParameterRange& _weirdness, const float& _offset, const std::string& _biome)
        : temperature(_temperature), humidity(_humidity), continentalness(_continentalness), erosion(_erosion), depth(_depth), weirdness(_weirdness), offset(_offset), biome(_biome) {}
    void print() {
        std::cout << "BiomeParameter(ParameterRange(" << temperature.min << ", " << temperature.max << "), ParameterRange(" << humidity.min << ", " << humidity.max << "), ParameterRange(" << continentalness.min << ", " << continentalness.max << "), ParameterRange(" << erosion.min << ", " << erosion.max << "), ParameterRange(" << depth.min << ", " << depth.max << "), ParameterRange(" << weirdness.min << ", " << weirdness.max << "), " << offset << ", \"" << biome << "\")";
    }
};
std::vector<BiomeParameter> biomeParameters;
void printParameters() {
    for (size_t i = 0; i < biomeParameters.size(); i++) {
        biomeParameters[i].print();
        std::cout << ((i == (biomeParameters.size()-1)) ? "," : "") << '\n';
    }
    std::cout << "biomeParameters.length = " << biomeParameters.size() << '\n';
}
#pragma region initialize parameters
ParameterRange defaultParameterRange(-1.0f, 1.0f);
ParameterRange temperatureParameters[5] = {ParameterRange(-1.0f, -0.45f), ParameterRange(-0.45f, -0.15f), ParameterRange(-0.15f, 0.2f), ParameterRange(0.2f, 0.55f), ParameterRange(0.55f, 1.0f)};
ParameterRange humidityParameters[5] = {ParameterRange(-1.0f, -0.35f), ParameterRange(-0.35f, -0.1f), ParameterRange(-0.1f, 0.1f), ParameterRange(0.1f, 0.3f), ParameterRange(0.3f, 1.0f)};
ParameterRange erosionParameters[7] = {ParameterRange(-1.0f, -0.78f), ParameterRange(-0.78f, -0.375f), ParameterRange(-0.375f, -0.2225f), ParameterRange(-0.2225f, 0.05f), ParameterRange(0.05f, 0.45f), ParameterRange(0.45f, 0.55f), ParameterRange(0.55f, 1.0f)};
ParameterRange frozenTemperature = temperatureParameters[0];
ParameterRange nonFrozenTemperatureParameters = ParameterRange::combine(temperatureParameters[1], temperatureParameters[4]);
ParameterRange mushroomFieldsContinentalness(-1.2f, -1.05f);
ParameterRange deepOceanContinentalness(-1.05f, -0.455f);
ParameterRange oceanContinentalness(-0.455f, -0.19f);
ParameterRange coastContinentalness(-0.19f, -0.11f);
ParameterRange riverContinentalness(-0.11f, 0.55f);
ParameterRange nearInlandContinentalness(-0.11f, 0.03f);
ParameterRange midInlandContinentalness(0.03f, 0.3f);
ParameterRange farInlandContinentalness(0.3f, 1.0f);

std::string oceanBiomes[2][6] = {{"DEEP_FROZEN_OCEAN", "DEEP_COLD_OCEAN", "DEEP_OCEAN", "DEEP_LUKEWARM_OCEAN", "WARM_OCEAN"}, {"FROZEN_OCEAN", "COLD_OCEAN", "OCEAN", "LUKEWARM_OCEAN", "WARM_OCEAN"}};
std::string commonBiomes[5][5] = {{"SNOWY_PLAINS", "SNOWY_PLAINS", "SNOWY_PLAINS", "SNOWY_TAIGA", "TAIGA"}, {"PLAINS", "PLAINS", "FOREST", "TAIGA", "OLD_GROWTH_SPRUCE_TAIGA"}, {"FLOWER_FOREST", "PLAINS", "FOREST", "BIRCH_FOREST", "DARK_FOREST"}, {"SAVANNA", "SAVANNA", "FOREST", "JUNGLE", "JUNGLE"}, {"DESERT", "DESERT", "DESERT", "DESERT", "DESERT"}};
std::string uncommonBiomes[5][5] = {{"ICE_SPIKES", "", "SNOWY_TAIGA", "", ""}, {"", "", "", "", "OLD_GROWTH_PINE_TAIGA"}, {"SUNFLOWER_PLAINS", "", "", "OLD_GROWTH_BIRCH_FOREST", ""}, {"", "", "PLAINS", "SPARSE_JUNGLE", "BAMBOO_JUNGLE"}, {"", "", "", "", ""}};
std::string nearMountainBiomes[5][5] = {{"SNOWY_PLAINS", "SNOWY_PLAINS", "SNOWY_PLAINS", "SNOWY_TAIGA", "SNOWY_TAIGA"}, {"MEADOW", "MEADOW", "FOREST", "TAIGA", "OLD_GROWTH_SPRUCE_TAIGA"}, {"MEADOW", "MEADOW", "MEADOW", "MEADOW", "PALE_GARDEN"}, {"SAVANNA_PLATEAU", "SAVANNA_PLATEAU", "FOREST", "FOREST", "JUNGLE"}, {"BADLANDS", "BADLANDS", "BADLANDS", "WOODED_BADLANDS", "WOODED_BADLANDS"}};
std::string specialNearMountainBiomes[5][5] = {{"ICE_SPIKES", "", "", "", ""}, {"CHERRY_GROVE", "", "MEADOW", "MEADOW", "OLD_GROWTH_PINE_TAIGA"}, {"CHERRY_GROVE", "CHERRY_GROVE", "FOREST", "BIRCH_FOREST", ""}, {"", "", "", "", ""}, {"ERODED_BADLANDS", "ERODED_BADLANDS", "", "", ""}};
std::string windsweptBiomes[5][5] = {{"WINDSWEPT_GRAVELLY_HILLS", "WINDSWEPT_GRAVELLY_HILLS", "WINDSWEPT_HILLS", "WINDSWEPT_FOREST", "WINDSWEPT_FOREST"}, {"WINDSWEPT_GRAVELLY_HILLS", "WINDSWEPT_GRAVELLY_HILLS", "WINDSWEPT_HILLS", "WINDSWEPT_FOREST", "WINDSWEPT_FOREST"}, {"WINDSWEPT_HILLS", "WINDSWEPT_HILLS", "WINDSWEPT_HILLS", "WINDSWEPT_FOREST", "WINDSWEPT_FOREST"}, {"", "", "", "", ""}, {"", "", "", "", ""}};

void writeBiomeParameters(const ParameterRange& temperature, const ParameterRange& humidity, const ParameterRange& continentalness, const ParameterRange& erosion, const ParameterRange& weirdness, const float& offset, const std::string& biome) {
    biomeParameters.emplace_back(temperature, humidity, continentalness, erosion, ParameterRange(0.0f), weirdness, offset, biome);
    biomeParameters.emplace_back(temperature, humidity, continentalness, erosion, ParameterRange(1.0f), weirdness, offset, biome);
}
/*void writeCaveBiomeParameters(ParameterRange temperature, ParameterRange humidity, ParameterRange continentalness, ParameterRange erosion, ParameterRange weirdness, float offset, std::string biome) {
    biomeParameters.emplace_back(temperature, humidity, continentalness, erosion, ParameterRange(0.2f, 0.9f), weirdness, offset, biome);
}
void writeDeepDarkParameters(ParameterRange temperature, ParameterRange humidity, ParameterRange continentalness, ParameterRange erosion, ParameterRange weirdness, float offset, std::string biome) {
    biomeParameters.emplace_back(temperature, humidity, continentalness, erosion, ParameterRange(1.1f), weirdness, offset, biome);
}*/

std::string getRegularBiome(int temperature, int humidity, ParameterRange weirdness) {
    if (weirdness.max < 0L) {
        return commonBiomes[temperature][humidity];
    }
    std::string _3 = uncommonBiomes[temperature][humidity];
    return _3 == "" ? commonBiomes[temperature][humidity] : _3;
}
std::string getBadlandsBiome(int humidity, ParameterRange weirdness) {
    if (humidity < 2) {
        return weirdness.max < 0L ? "BADLANDS" : "ERODED_BADLANDS";
    }
    if (humidity < 3) {
        return "BADLANDS";
    }
    return "WOODED_BADLANDS";
}
std::string getBadlandsOrRegularBiome(int temperature, int humidity, ParameterRange weirdness) {
    return temperature == 4 ? getBadlandsBiome(humidity, weirdness) : getRegularBiome(temperature, humidity, weirdness);
}
std::string getNearMountainBiome(int temperature, int humidity, ParameterRange weirdness) {
    std::string _3;
    if (weirdness.max >= 0L && (_3 = specialNearMountainBiomes[temperature][humidity]) != "") {
        return _3;
    }
    return nearMountainBiomes[temperature][humidity];
}
std::string getMountainSlopeBiome(int temperature, int humidity, ParameterRange weirdness) {
    if (temperature >= 3) {
        return getNearMountainBiome(temperature, humidity, weirdness);
    }
    if (humidity <= 1) {
        return "SNOWY_SLOPES";
    }
    return "GROVE";
}
std::string getMountainStartBiome(int temperature, int humidity, ParameterRange weirdness) {
    return temperature == 0 ? getMountainSlopeBiome(temperature, humidity, weirdness) : getBadlandsOrRegularBiome(temperature, humidity, weirdness);
}
std::string getBiomeOrWindsweptSavanna(int temperature, int humidity, ParameterRange weirdness, std::string biomeKey) {
    if (temperature > 1 && humidity < 4 && weirdness.max >= 0L) {
        return "WINDSWEPT_SAVANNA";
    }
    return biomeKey;
}
std::string getShoreBiome(int temperature, int humidity) {
    if (temperature == 0) {
        return "SNOWY_BEACH";
    }
    if (temperature == 4) {
        return "DESERT";
    }
    return "BEACH";
}
std::string getErodedShoreBiome(int temperature, int humidity, ParameterRange weirdness) {
    std::string _3 = weirdness.max >= 0L ? getRegularBiome(temperature, humidity, weirdness) : getShoreBiome(temperature, humidity);
    return getBiomeOrWindsweptSavanna(temperature, humidity, weirdness, _3);
}
std::string getPeakBiome(int temperature, int humidity, ParameterRange weirdness) {
    if (temperature <= 2) {
        return weirdness.max < 0L ? "JAGGED_PEAKS" : "FROZEN_PEAKS";
    }
    if (temperature == 3) {
        return "STONY_PEAKS";
    }
    return getBadlandsBiome(humidity, weirdness);
}
std::string getWindsweptOrRegularBiome(int temperature, int humidity, ParameterRange weirdness) {
    std::string _3 = windsweptBiomes[temperature][humidity];
    return _3 == "" ? getRegularBiome(temperature, humidity, weirdness) : _3;
}

void writePeakBiomes(ParameterRange weirdness) {
    for (int _2 = 0; _2 < 5; ++_2) {
        ParameterRange _3 = temperatureParameters[_2];
        for (int _4 = 0; _4 < 5; ++_4) {
            ParameterRange _5 = humidityParameters[_4];
            std::string _6 = getRegularBiome(_2, _4, weirdness);
            std::string _7 = getBadlandsOrRegularBiome(_2, _4, weirdness);
            std::string _8 = getMountainStartBiome(_2, _4, weirdness);
            std::string _9 = getNearMountainBiome(_2, _4, weirdness);
            std::string _10 = getWindsweptOrRegularBiome(_2, _4, weirdness);
            std::string _11 = getBiomeOrWindsweptSavanna(_2, _4, weirdness, _10);
            std::string _12 = getPeakBiome(_2, _4, weirdness);
            writeBiomeParameters(_3, _5, ParameterRange::combine(coastContinentalness, farInlandContinentalness), erosionParameters[0], weirdness, 0.0f, _12);
            writeBiomeParameters(_3, _5, ParameterRange::combine(coastContinentalness, nearInlandContinentalness), erosionParameters[1], weirdness, 0.0f, _8);
            writeBiomeParameters(_3, _5, ParameterRange::combine(midInlandContinentalness, farInlandContinentalness), erosionParameters[1], weirdness, 0.0f, _12);
            writeBiomeParameters(_3, _5, ParameterRange::combine(coastContinentalness, nearInlandContinentalness), ParameterRange::combine(erosionParameters[2], erosionParameters[3]), weirdness, 0.0f, _6);
            writeBiomeParameters(_3, _5, ParameterRange::combine(midInlandContinentalness, farInlandContinentalness), erosionParameters[2], weirdness, 0.0f, _9);
            writeBiomeParameters(_3, _5, midInlandContinentalness, erosionParameters[3], weirdness, 0.0f, _7);
            writeBiomeParameters(_3, _5, farInlandContinentalness, erosionParameters[3], weirdness, 0.0f, _9);
            writeBiomeParameters(_3, _5, ParameterRange::combine(coastContinentalness, farInlandContinentalness), erosionParameters[4], weirdness, 0.0f, _6);
            writeBiomeParameters(_3, _5, ParameterRange::combine(coastContinentalness, nearInlandContinentalness), erosionParameters[5], weirdness, 0.0f, _11);
            writeBiomeParameters(_3, _5, ParameterRange::combine(midInlandContinentalness, farInlandContinentalness), erosionParameters[5], weirdness, 0.0f, _10);
            writeBiomeParameters(_3, _5, ParameterRange::combine(coastContinentalness, farInlandContinentalness), erosionParameters[6], weirdness, 0.0f, _6);
        }
    }
}
void writeHighBiomes(ParameterRange weirdness) {
    for (int _2 = 0; _2 < 5; ++_2) {
        ParameterRange _3 = temperatureParameters[_2];
        for (int _4 = 0; _4 < 5; ++_4) {
            ParameterRange _5 = humidityParameters[_4];
            std::string _6 = getRegularBiome(_2, _4, weirdness);
            std::string _7 = getBadlandsOrRegularBiome(_2, _4, weirdness);
            std::string _8 = getMountainStartBiome(_2, _4, weirdness);
            std::string _9 = getNearMountainBiome(_2, _4, weirdness);
            std::string _10 = getWindsweptOrRegularBiome(_2, _4, weirdness);
            std::string _11 = getBiomeOrWindsweptSavanna(_2, _4, weirdness, _6);
            std::string _12 = getMountainSlopeBiome(_2, _4, weirdness);
            std::string _13 = getPeakBiome(_2, _4, weirdness);
            writeBiomeParameters(_3, _5, coastContinentalness, ParameterRange::combine(erosionParameters[0], erosionParameters[1]), weirdness, 0.0f, _6);
            writeBiomeParameters(_3, _5, nearInlandContinentalness, erosionParameters[0], weirdness, 0.0f, _12);
            writeBiomeParameters(_3, _5, ParameterRange::combine(midInlandContinentalness, farInlandContinentalness), erosionParameters[0], weirdness, 0.0f, _13);
            writeBiomeParameters(_3, _5, nearInlandContinentalness, erosionParameters[1], weirdness, 0.0f, _8);
            writeBiomeParameters(_3, _5, ParameterRange::combine(midInlandContinentalness, farInlandContinentalness), erosionParameters[1], weirdness, 0.0f, _12);
            writeBiomeParameters(_3, _5, ParameterRange::combine(coastContinentalness, nearInlandContinentalness), ParameterRange::combine(erosionParameters[2], erosionParameters[3]), weirdness, 0.0f, _6);
            writeBiomeParameters(_3, _5, ParameterRange::combine(midInlandContinentalness, farInlandContinentalness), erosionParameters[2], weirdness, 0.0f, _9);
            writeBiomeParameters(_3, _5, midInlandContinentalness, erosionParameters[3], weirdness, 0.0f, _7);
            writeBiomeParameters(_3, _5, farInlandContinentalness, erosionParameters[3], weirdness, 0.0f, _9);
            writeBiomeParameters(_3, _5, ParameterRange::combine(coastContinentalness, farInlandContinentalness), erosionParameters[4], weirdness, 0.0f, _6);
            writeBiomeParameters(_3, _5, ParameterRange::combine(coastContinentalness, nearInlandContinentalness), erosionParameters[5], weirdness, 0.0f, _11);
            writeBiomeParameters(_3, _5, ParameterRange::combine(midInlandContinentalness, farInlandContinentalness), erosionParameters[5], weirdness, 0.0f, _10);
            writeBiomeParameters(_3, _5, ParameterRange::combine(coastContinentalness, farInlandContinentalness), erosionParameters[6], weirdness, 0.0f, _6);
        }
    }
}
void writeMidBiomes(ParameterRange weirdness) {
    writeBiomeParameters(defaultParameterRange, defaultParameterRange, coastContinentalness, ParameterRange::combine(erosionParameters[0], erosionParameters[2]), weirdness, 0.0f, "STONY_SHORE");
    writeBiomeParameters(ParameterRange::combine(temperatureParameters[1], temperatureParameters[2]), defaultParameterRange, ParameterRange::combine(nearInlandContinentalness, farInlandContinentalness), erosionParameters[6], weirdness, 0.0f, "SWAMP");
    writeBiomeParameters(ParameterRange::combine(temperatureParameters[3], temperatureParameters[4]), defaultParameterRange, ParameterRange::combine(nearInlandContinentalness, farInlandContinentalness), erosionParameters[6], weirdness, 0.0f, "MANGROVE_SWAMP");
    for (int _2 = 0; _2 < 5; ++_2) {
        ParameterRange _3 = temperatureParameters[_2];
        for (int _4 = 0; _4 < 5; ++_4) {
            ParameterRange _5 = humidityParameters[_4];
            std::string _6 = getRegularBiome(_2, _4, weirdness);
            std::string _7 = getBadlandsOrRegularBiome(_2, _4, weirdness);
            std::string _8 = getMountainStartBiome(_2, _4, weirdness);
            std::string _9 = getWindsweptOrRegularBiome(_2, _4, weirdness);
            std::string _10 = getNearMountainBiome(_2, _4, weirdness);
            std::string _11 = getShoreBiome(_2, _4);
            std::string _12 = getBiomeOrWindsweptSavanna(_2, _4, weirdness, _6);
            std::string _13 = getErodedShoreBiome(_2, _4, weirdness);
            std::string _14 = getMountainSlopeBiome(_2, _4, weirdness);
            writeBiomeParameters(_3, _5, ParameterRange::combine(nearInlandContinentalness, farInlandContinentalness), erosionParameters[0], weirdness, 0.0f, _14);
            writeBiomeParameters(_3, _5, ParameterRange::combine(nearInlandContinentalness, midInlandContinentalness), erosionParameters[1], weirdness, 0.0f, _8);
            writeBiomeParameters(_3, _5, farInlandContinentalness, erosionParameters[1], weirdness, 0.0f, _2 == 0 ? _14 : _10);
            writeBiomeParameters(_3, _5, nearInlandContinentalness, erosionParameters[2], weirdness, 0.0f, _6);
            writeBiomeParameters(_3, _5, midInlandContinentalness, erosionParameters[2], weirdness, 0.0f, _7);
            writeBiomeParameters(_3, _5, farInlandContinentalness, erosionParameters[2], weirdness, 0.0f, _10);
            writeBiomeParameters(_3, _5, ParameterRange::combine(coastContinentalness, nearInlandContinentalness), erosionParameters[3], weirdness, 0.0f, _6);
            writeBiomeParameters(_3, _5, ParameterRange::combine(midInlandContinentalness, farInlandContinentalness), erosionParameters[3], weirdness, 0.0f, _7);
            if (weirdness.max < 0L) {
                writeBiomeParameters(_3, _5, coastContinentalness, erosionParameters[4], weirdness, 0.0f, _11);
                writeBiomeParameters(_3, _5, ParameterRange::combine(nearInlandContinentalness, farInlandContinentalness), erosionParameters[4], weirdness, 0.0f, _6);
            } else {
                writeBiomeParameters(_3, _5, ParameterRange::combine(coastContinentalness, farInlandContinentalness), erosionParameters[4], weirdness, 0.0f, _6);
            }
            writeBiomeParameters(_3, _5, coastContinentalness, erosionParameters[5], weirdness, 0.0f, _13);
            writeBiomeParameters(_3, _5, nearInlandContinentalness, erosionParameters[5], weirdness, 0.0f, _12);
            writeBiomeParameters(_3, _5, ParameterRange::combine(midInlandContinentalness, farInlandContinentalness), erosionParameters[5], weirdness, 0.0f, _9);
            if (weirdness.max < 0L) {
                writeBiomeParameters(_3, _5, coastContinentalness, erosionParameters[6], weirdness, 0.0f, _11);
            } else {
                writeBiomeParameters(_3, _5, coastContinentalness, erosionParameters[6], weirdness, 0.0f, _6);
            }
            if (_2 != 0) continue;
            writeBiomeParameters(_3, _5, ParameterRange::combine(nearInlandContinentalness, farInlandContinentalness), erosionParameters[6], weirdness, 0.0f, _6);
        }
    }
}
void writeLowBiomes(ParameterRange weirdness) {
    writeBiomeParameters(defaultParameterRange, defaultParameterRange, coastContinentalness, ParameterRange::combine(erosionParameters[0], erosionParameters[2]), weirdness, 0.0f, "STONY_SHORE");
    writeBiomeParameters(ParameterRange::combine(temperatureParameters[1], temperatureParameters[2]), defaultParameterRange, ParameterRange::combine(nearInlandContinentalness, farInlandContinentalness), erosionParameters[6], weirdness, 0.0f, "SWAMP");
    writeBiomeParameters(ParameterRange::combine(temperatureParameters[3], temperatureParameters[4]), defaultParameterRange, ParameterRange::combine(nearInlandContinentalness, farInlandContinentalness), erosionParameters[6], weirdness, 0.0f, "MANGROVE_SWAMP");
    for (int _2 = 0; _2 < 5; ++_2) {
        ParameterRange _3 = temperatureParameters[_2];
        for (int _4 = 0; _4 < 5; ++_4) {
            ParameterRange _5 = humidityParameters[_4];
            std::string _6 = getRegularBiome(_2, _4, weirdness);
            std::string _7 = getBadlandsOrRegularBiome(_2, _4, weirdness);
            std::string _8 = getMountainStartBiome(_2, _4, weirdness);
            std::string _9 = getShoreBiome(_2, _4);
            std::string _10 = getBiomeOrWindsweptSavanna(_2, _4, weirdness, _6);
            std::string _11 = getErodedShoreBiome(_2, _4, weirdness);
            writeBiomeParameters(_3, _5, nearInlandContinentalness, ParameterRange::combine(erosionParameters[0], erosionParameters[1]), weirdness, 0.0f, _7);
            writeBiomeParameters(_3, _5, ParameterRange::combine(midInlandContinentalness, farInlandContinentalness), ParameterRange::combine(erosionParameters[0], erosionParameters[1]), weirdness, 0.0f, _8);
            writeBiomeParameters(_3, _5, nearInlandContinentalness, ParameterRange::combine(erosionParameters[2], erosionParameters[3]), weirdness, 0.0f, _6);
            writeBiomeParameters(_3, _5, ParameterRange::combine(midInlandContinentalness, farInlandContinentalness), ParameterRange::combine(erosionParameters[2], erosionParameters[3]), weirdness, 0.0f, _7);
            writeBiomeParameters(_3, _5, coastContinentalness, ParameterRange::combine(erosionParameters[3], erosionParameters[4]), weirdness, 0.0f, _9);
            writeBiomeParameters(_3, _5, ParameterRange::combine(nearInlandContinentalness, farInlandContinentalness), erosionParameters[4], weirdness, 0.0f, _6);
            writeBiomeParameters(_3, _5, coastContinentalness, erosionParameters[5], weirdness, 0.0f, _11);
            writeBiomeParameters(_3, _5, nearInlandContinentalness, erosionParameters[5], weirdness, 0.0f, _10);
            writeBiomeParameters(_3, _5, ParameterRange::combine(midInlandContinentalness, farInlandContinentalness), erosionParameters[5], weirdness, 0.0f, _6);
            writeBiomeParameters(_3, _5, coastContinentalness, erosionParameters[6], weirdness, 0.0f, _9);
            if (_2 != 0) continue;
            writeBiomeParameters(_3, _5, ParameterRange::combine(nearInlandContinentalness, farInlandContinentalness), erosionParameters[6], weirdness, 0.0f, _6);
        }
    }
}
void writeValleyBiomes(ParameterRange weirdness) {
    writeBiomeParameters(frozenTemperature, defaultParameterRange, coastContinentalness, ParameterRange::combine(erosionParameters[0], erosionParameters[1]), weirdness, 0.0f, weirdness.max < 0L ? "STONY_SHORE" : "FROZEN_RIVER");
    writeBiomeParameters(nonFrozenTemperatureParameters, defaultParameterRange, coastContinentalness, ParameterRange::combine(erosionParameters[0], erosionParameters[1]), weirdness, 0.0f, weirdness.max < 0L ? "STONY_SHORE" : "RIVER");
    writeBiomeParameters(frozenTemperature, defaultParameterRange, nearInlandContinentalness, ParameterRange::combine(erosionParameters[0], erosionParameters[1]), weirdness, 0.0f, "FROZEN_RIVER");
    writeBiomeParameters(nonFrozenTemperatureParameters, defaultParameterRange, nearInlandContinentalness, ParameterRange::combine(erosionParameters[0], erosionParameters[1]), weirdness, 0.0f, "RIVER");
    writeBiomeParameters(frozenTemperature, defaultParameterRange, ParameterRange::combine(coastContinentalness, farInlandContinentalness), ParameterRange::combine(erosionParameters[2], erosionParameters[5]), weirdness, 0.0f, "FROZEN_RIVER");
    writeBiomeParameters(nonFrozenTemperatureParameters, defaultParameterRange, ParameterRange::combine(coastContinentalness, farInlandContinentalness), ParameterRange::combine(erosionParameters[2], erosionParameters[5]), weirdness, 0.0f, "RIVER");
    writeBiomeParameters(frozenTemperature, defaultParameterRange, coastContinentalness, erosionParameters[6], weirdness, 0.0f, "FROZEN_RIVER");
    writeBiomeParameters(nonFrozenTemperatureParameters, defaultParameterRange, coastContinentalness, erosionParameters[6], weirdness, 0.0f, "RIVER");
    writeBiomeParameters(ParameterRange::combine(temperatureParameters[1], temperatureParameters[2]), defaultParameterRange, ParameterRange::combine(riverContinentalness, farInlandContinentalness), erosionParameters[6], weirdness, 0.0f, "SWAMP");
    writeBiomeParameters(ParameterRange::combine(temperatureParameters[3], temperatureParameters[4]), defaultParameterRange, ParameterRange::combine(riverContinentalness, farInlandContinentalness), erosionParameters[6], weirdness, 0.0f, "MANGROVE_SWAMP");
    writeBiomeParameters(frozenTemperature, defaultParameterRange, ParameterRange::combine(riverContinentalness, farInlandContinentalness), erosionParameters[6], weirdness, 0.0f, "FROZEN_RIVER");
    for (int _2 = 0; _2 < 5; ++_2) {
        ParameterRange _3 = temperatureParameters[_2];
        for (int _4 = 0; _4 < 5; ++_4) {
            ParameterRange _5 = humidityParameters[_4];
            std::string _6 = getBadlandsOrRegularBiome(_2, _4, weirdness);
            writeBiomeParameters(_3, _5, ParameterRange::combine(midInlandContinentalness, farInlandContinentalness), ParameterRange::combine(erosionParameters[0], erosionParameters[1]), weirdness, 0.0f, _6);
        }
    }
}

void writeOceanBiomes() {
    writeBiomeParameters(defaultParameterRange, defaultParameterRange, mushroomFieldsContinentalness, defaultParameterRange, defaultParameterRange, 0.0f, "MUSHROOM_FIELDS");
    for (int _1 = 0; _1 < 5; ++_1) {
        ParameterRange _2 = temperatureParameters[_1];
        writeBiomeParameters(_2, defaultParameterRange, deepOceanContinentalness, defaultParameterRange, defaultParameterRange, 0.0f, oceanBiomes[0][_1]);
        writeBiomeParameters(_2, defaultParameterRange, oceanContinentalness, defaultParameterRange, defaultParameterRange, 0.0f, oceanBiomes[1][_1]);
    }
}
void writeLandBiomes() {
    writeMidBiomes(ParameterRange(-1.0f, -0.93333334f));
    writeHighBiomes(ParameterRange(-0.93333334f, -0.7666667f));
    writePeakBiomes(ParameterRange(-0.7666667f, -0.56666666f));
    writeHighBiomes(ParameterRange(-0.56666666f, -0.4f));
    writeMidBiomes(ParameterRange(-0.4f, -0.26666668f));
    writeLowBiomes(ParameterRange(-0.26666668f, -0.05f));
    writeValleyBiomes(ParameterRange(-0.05f, 0.05f));
    writeLowBiomes(ParameterRange(0.05f, 0.26666668f));
    writeMidBiomes(ParameterRange(0.26666668f, 0.4f));
    writeHighBiomes(ParameterRange(0.4f, 0.56666666f));
    writePeakBiomes(ParameterRange(0.56666666f, 0.7666667f));
    writeHighBiomes(ParameterRange(0.7666667f, 0.93333334f));
    writeMidBiomes(ParameterRange(0.93333334f, 1.0f));
}
/*void writeCaveBiomes() {
    writeCaveBiomeParameters(defaultParameterRange, defaultParameterRange, ParameterRange(0.8f, 1.0f), defaultParameterRange, defaultParameterRange, 0.0f, "DRIPSTONE_CAVES");
    writeCaveBiomeParameters(defaultParameterRange, ParameterRange(0.7f, 1.0f), defaultParameterRange, defaultParameterRange, defaultParameterRange, 0.0f, "LUSH_CAVES");
    writeDeepDarkParameters(defaultParameterRange, defaultParameterRange, defaultParameterRange, ParameterRange::combine(erosionParameters[0], erosionParameters[1]), defaultParameterRange, 0.0f, "DEEP_DARK");
}*/

void writeOverworldBiomeParameters() {
    writeOceanBiomes();
    writeLandBiomes();
    // writeCaveBiomes();
}
#pragma endregion initialize parameters

//  see NoiseConfig constructor in NoiseConfig.java
//      uses ChunkGeneratorSettings.noiseRouter which gets data from noise_router field in the "/noise_settings/overworld.json" file
//      see MultiNoiseUtil.MultiNoiseSampler constructor
//      // MultiNoiseSampler constructor gets passed the "temperature", "vegetation", "continents", "erosion", "depth", and "ridges" fields form the NoiseRouter
//      // these are density functions, sometimes defined inline and sometimes in a "/desity_function/_.json" file
OwningNullable<Temperature> temperature = nullptr;
OwningNullable<Vegetation> vegetation = nullptr;
OwningNullable<Continentalness> continentalness = nullptr;
OwningNullable<Erosion> erosion = nullptr;
OwningNullable<Ridges> ridges = nullptr;
void initBiomes(const long long int& worldSeed) {
    writeOverworldBiomeParameters();
    temperature = new Temperature(worldSeed);
    vegetation = new Vegetation(worldSeed);
    continentalness = new Continentalness(worldSeed);
    erosion = new Erosion(worldSeed);
    ridges = new Ridges(worldSeed);
}

//  see extends(BiomeSource).getBiome function
//      uses MultiNoiseBiomeSource.getBiome(MultiNoiseSampler) function in MultiNoiseBiomeSource.java
//          uses MultiNoiseBiomeSource.getBiomeAtPoint function in MultiNoiseBiomeSource.java
//              uses MultiNoiseUtil.Entries.get function in MultiNoiseBiomeSource.java
//                  uses MultiNoiseUtil.Entries.getValue(NoiseValuePoint) function in MultiNoiseBiomeSource.java
//                      uses MultiNoiseUtil.Entries.getValue(NoiseValuePoint, NodeDistanceFunction) function in MultiNoiseBiomeSource.java
std::string getBiome(const Pos& pos) {
    if (!temperature.hasValue) return "";
    Vec3D rounded = pos.getBiomePos().getBlockPos().toVec3D();
    double temp = temperature.getValue().sample(rounded);
    double veg = vegetation.getValue().sample(rounded);
    double cont = continentalness.getValue().sample(rounded);
    double ero = erosion.getValue().sample(rounded);
    double ridge = ridges.getValue().sample(rounded);
    std::cout << "temperature: " << temp << '\n';
    std::cout << "vegetation: " << veg << '\n';
    std::cout << "continentalness: " << cont << '\n';
    std::cout << "erosion: " << ero << '\n';
    std::cout << "ridges: " << ridge << '\n';
    return "";
}