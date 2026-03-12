#ifndef __FINDER
#define __FINDER

#include <vector>
#include <string>
#include <cmath>
#include "MinecraftLib.h"
#include "Random.h"
#include "Biome.h"
#include "Lib.h"

OwningNullable<Pos> locateBiome(const Pos& pos, const int& radius, const std::vector<std::string>& predicate, LCG& rand);
class Finder {
    public:
    Finder() {}
    std::vector<ChunkPos> generateRingPositions(const long long int& worldSeed, const int& distance=32, const int& count=128, int spread=3);
};

#endif// __FINDER