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
            bool _18 = myAbs(_17) == i;
            for (int j = -i; j < i; j++) {
                if (_7 && !_18 && !(myAbs(j) == i)) continue;
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
// see BiomeSource.locateBiome(int*4, Predicate, LCG, MultiNoiseUtil.MultiNoiseSampler) function in BiomeSource.java
OwningNullable<Pos> locateBiome(const Pos& pos, const int& radius, const std::vector<std::string>& predicate, LCG& rand) {
    return locateBiome(pos, radius, 1, predicate, rand, false);
}
// see StructurePlacementCalculator.calculateConcentricsRingPlacementPos function in StructurePlacementCalculator
// https://www.chunkbase.com/apps/stronghold-finder#seed=56871783007&platform=java_1_20&x=-179&z=-2059&zoom=1.25
std::vector<ChunkPos> Finder::concentricRings(const long long int& worldSeed, const int& distance, const int& count, int spread) {
    LCG JavaStructureRand(worldSeed);
    std::vector<ChunkPos> positions;
    positions.reserve(count);
    int numPlaced = 0;
    int amount_attempted = 0;
    double angle = JavaStructureRand.nextDouble() * TAU;
    unsigned int num_spawned = 0;
    unsigned int ring = 0;
    for (int i = 0; i < count; i++) {
        double structure_distance = distance * (4 + 6*ring) + ((JavaStructureRand.nextDouble() - 0.5) * (2.5*((double)distance)));
        ChunkPos pos((int)(std::round(std::cos(angle) * structure_distance)), (int)(std::round(std::sin(angle) * structure_distance)));
        LCG biomeRand = JavaStructureRand.split();
        OwningNullable<Pos> possiblePos = locateBiome(pos.getOffsetPos({8, 0, 8}), 112, {}, biomeRand);
        if (possiblePos.hasValue) positions.push_back(possiblePos.getValue().getChunkPos());
        else positions.emplace_back(pos);
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