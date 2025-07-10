#ifndef __FINDER
#define __FINDER

#include <vector>
#include <cmath>
#include "MinecraftLib.h"
#include "Random.h"
#include "Lib.h"

class Finder {
    public:
    Finder() {}
    std::vector<ChunkPos> concentricRings(const long long int& worldSeed, const int& distance=32, const int& count=128, int spread=3);
};

#endif// __FINDER