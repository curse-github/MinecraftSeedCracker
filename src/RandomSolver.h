#ifndef __RANDOM_SOLVER
#define __RANDOM_SOLVER

#include <vector>
#include <string>
#include <cmath>
#include "MinecraftLib.h"
#include "Random.h"
#include "Lib.h"
#include "Biome.h"

class StructureFinder {
    public:
    StructureFinder() {}
    static std::vector<SectionPos> generateRingPositions(const long long int& world_seed, const int& distance=32, const int& count=128, int spread=3);
};

struct RandomRange {
    unsigned long long int min;
    unsigned long long int mult;
    unsigned long long int max;
    RandomRange(const unsigned long long int& _min, const unsigned long long int& _mult, const unsigned long long int& _max) : min(_min), mult(_mult), max(_max) {}
    RandomRange(const RandomRange& copy) : min(copy.min), mult(copy.mult), max(copy.max) {}
};
void printRanges(const std::vector<RandomRange>& ranges, const long long int& mod);
class JavaSolver {
    unsigned long long int a;
    unsigned long long int inv_a;
    unsigned long long int b;
    unsigned long long int m;
    // add constraints where
    // range.min < ((a^n)*seed + b*geom(n-1)) % m < range.max
    // range.min < (a^n)*seed + b*geom(n-1) + k_n*m < range.max
    // ranges[n].min - b*geom(n-1) < (a^n)*seed + k_n*m < ranges[n].max - b*geom(n-1)
    // for each n value in ranges
    std::vector<RandomRange> ranges;
    public:
    JavaSolver(const unsigned long long int& _a = 25214903917ull, const unsigned long long int& _b = 11ull, const unsigned long long int& _m = (1ull << 48ull))
        : a(_a), inv_a(modInverse(_a, _m)), b(_b), m(_m) {}
    void addFloatConstraint(const float& min, const float& max);
    void print() const;
};
void solve12Eye();

#endif// __RANDOM_SOLVER