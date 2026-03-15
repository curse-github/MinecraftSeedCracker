#include "Finder.h"

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