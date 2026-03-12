#include "Finder.h"
//  ChunkGenerator.generateFeatures() function in ChunkGenerator.java
//      StructureStart.place() function in StructureStart.java
//          extends(StructurePiece).generate() function in StructurePiece.java
//              extends(StrongholdGenerator.Piece).generate function in StrongholdGenerator.java
//                  StrongholdGenerator.Corridor.generate function in StrongholdGenerator.java
//                  or StrongholdGenerator.SpiralStaircase.generate function in StrongholdGenerator.java
//                  or StrongholdGenerator.ChestCorridor.generate function in StrongholdGenerator.java
//                  or StrongholdGenerator.Library.generate function in StrongholdGenerator.java
//                  etc...
//  https://minecraft.wiki/w/Structure_set helped some
//  most helpful was the source code
bool testPredicate(const std::vector<std::string>& predicate, const std::string& value) {
    for (size_t i = 0; i < predicate.size(); i++) if (predicate[i] == value) return true;
    return false;
}
// see BiomeSource.locateBiome(int*5, Predicate, LCG, boolean, MultiNoiseUtil.MultiNoiseSampler) function in BiomeSource.java
OwningNullable<Pos> locateBiome(const Pos& pos, const int& radius, const int& blockCheckInterval, const std::vector<std::string>& predicate, LCG& rand, const bool& _7) {
    OwningNullable<Pos> returnVal;
    const ChunkPos biomePos = pos.getBiomePos();
    const int BiomeScaleRadius = radius >> 2;
    int _14 = 1;
    int _15 = _7 ? 0 : BiomeScaleRadius;
    for (int i = _15; i <= BiomeScaleRadius; i += blockCheckInterval) {
        int _17 = -i;
        while (_17 <= i) {
            bool _18 = std::abs(_17) == i;
            for (int j = -i; j < i; j++) {
                if (_7 && !_18 && !(std::abs(j) == i)) continue;
                BiomePos testPos = biomePos + BiomePos(j, _17);
                if (testPredicate(predicate, getBiome(testPos.getBlockPos()))) continue;
                if (!returnVal.hasValue || (rand.nextInt(_14) == 0)) {
                    returnVal = new Pos(testPos.getBlockPos());
                    if (_7) return returnVal;
                }
                _14++;
            }
            _17 += blockCheckInterval;
        }
    }
    return returnVal;
}
// BiomeSource.locateBiome(int*4, Predicate, LCG, MultiNoiseUtil.MultiNoiseSampler) function in BiomeSource.java
OwningNullable<Pos> locateBiome(const Pos& pos, const int& radius, const std::vector<std::string>& predicate, LCG& rand) {
    return locateBiome(pos, radius, 1, predicate, rand, false);
}

//  StructurePlacement. function in net.minecraft.world.level.levelgen.structure.placement.StructurePlacement
//  ConcentricRingsStructurePlacement.calculateConcentricsRingPlacementPos function in net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement
//  ChunkGeneratorStructureState.getRingPositionsFor function in net.minecraft.world.level.levelgen.structure.placement

// generateRingPositions function in net.minecraft.world.level.chunk.ChunkGeneratorStructureState
std::vector<ChunkPos> Finder::generateRingPositions(const long long int& worldSeed, const int& distance, const int& count, int spread) {
    //  net.minecraft.world.level.chunk.ChunkGeneratorStructureState => concentricRingsSeed = levelSeed
    //  net.minecraft.world.level.chunk.ChunkGenerator => levelSeed = legacyLevelSeed
    //  net.minecraft.server.level.ChunkMap => legacyLevelSeed = levelSeed
    //  net.minecraft.server.level.ChunkMap => levelSeed = level.getSeed()
    //  net.minecraft.server.level.ServerLevel => ChunkMap.level.getSeed() = this.server.getWorldData().worldGenOptions().seed()
    //      net.minecraft.server.level.MinecraftServer => ServerLevel.server = this
    //  net.minecraft.server.level.MinecraftServer => getWorldData().worldGenOptions().seed() = this.worldStem.worldData().worldGenOptions().seed()
    //      net.minecraft.server.dedicated.DedicatedServer => MinecraftServer.worldStem = this.worldstem
    //  net.minecraft.server.level.DedicatedServer => this.worldStem.worldData().worldGenOptions().seed()
    //      net.minecraft.server.WorldLoader => WorldStem.worldData() = worldDataAndRegistries.cookie
    //          net.minecraft.server.WorldLoader => worldDataAndRegistries = worldDataSupplier.get(new DataLoadContext(resources, worldDataConfiguration, dimensionContextProvider, initialWorldgenDimensions))
    //      net.minecraft.server.Main => worldDataSupplier = WorldDataSupplier<WorldData>()
    //      net.minecraft.server.WorldLoader => worldDataSupplier.get(new DataLoadContext(resources, worldDataConfiguration, dimensionContextProvider, initialWorldgenDimensions)) = ???
    LCG JavaStructureRand(worldSeed);
    std::vector<ChunkPos> positions;
    positions.reserve(count);
    int amount_attempted = 0;
    double angle = JavaStructureRand.nextDouble() * TAU;
    int positionInCircle = 0;
    int circle = 0;
    for (int i = 0; i < count; i++) {
        double structure_distance = distance * (4 + 6*circle) + ((JavaStructureRand.nextDouble() - 0.5) * 2.5*distance);
        ChunkPos pos((int)(std::round(std::cos(angle) * structure_distance)), (int)(std::round(std::sin(angle) * structure_distance)));
        LCG biomeRand = JavaStructureRand.split();
        //OwningNullable<Pos> possiblePos = locateBiome(pos.getOffsetPos({8, 0, 8}), 112, {}, biomeRand);
        //if (possiblePos.hasValue) positions.push_back(possiblePos.getValue().getChunkPos());
        //else positions.emplace_back(pos);
        positions.emplace_back(pos);
        angle += TAU / spread;
        if ((++positionInCircle) != spread) continue;
        circle++;
        positionInCircle = 0;
        spread += 2 * spread / (circle + 1);
        spread = std::min(spread, count - i);
        angle += JavaStructureRand.nextDouble() * TAU;
    }
    return positions;
}