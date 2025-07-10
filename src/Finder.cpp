#include "Finder.h"
//  https://minecraft.wiki/w/Structure_set helped some
//  most helpful was the source code
//  ConcentricRingsStructurePlacement.isStartChunk() function in ConcentricRingsStructurePlacement.java
//      uses StructurePlacementCalculator.getPlacementPositions() function in StructurePlacementCalculator.java
//          uses StructurePlacementCalculator.tryCalculate() function in StructurePlacementCalculator.java
//              uses StructurePlacementCalculator.calculate() function in StructurePlacementCalculator.java
//                  uses StructurePlacementCalculator.calculateConcentricsRingPlacementPos() function in StructurePlacementCalculator.java
// https://www.chunkbase.com/apps/stronghold-finder#seed=56871783007&platform=java_1_20&x=-179&z=-2059&zoom=1.25
std::vector<ChunkPos> Finder::concentricRings(const long long int& worldSeed, const int& distance, const int& count, int spread) {
    Random JavaStructureRand(worldSeed);
    std::vector<ChunkPos> positions;
    positions.reserve(count);
    int numPlaced = 0;
    int amount_attempted = 0;
    double angle = JavaStructureRand.nextDouble() * TAU;
    unsigned int num_spawned = 0;
    unsigned int ring = 0;
    for (int i = 0; i < count; i++) {
        double structure_distance = distance * (4 + 6*ring) + ((JavaStructureRand.nextDouble() - 0.5) * (2.5*((double)distance)));
        int x = (int)(std::round(std::cos(angle) * structure_distance));
        int z = (int)(std::round(std::sin(angle) * structure_distance));
        Random biomeRand = JavaStructureRand.split();
        // TODO: biome checking?
        positions.emplace_back(x, z);
        angle += TAU / spread;
        if ((++num_spawned) != spread) continue;
        num_spawned = 0;
        ring++;
        spread += 2 * spread / (ring + 1);
        spread = myMin(spread, count - i);
        angle += JavaStructureRand.nextDouble() * TAU;
    }
    return positions;
}