#ifndef __RANDOM
#define __RANDOM

#include <iostream>
#include "MinecraftLib.h"

unsigned long long int modInverse(unsigned long long int x, unsigned long long int y);
unsigned long long int fastExp(unsigned long long int a, unsigned long long int n);
unsigned long long int geom(unsigned long long int a, unsigned long long int n);

extern const unsigned long long int rand_seed_neg_1;
extern const unsigned long long int rand_seed_neg_2;
class Random {
    long long int a;
    long long int inv_a;
    long long int b;
    unsigned long long int m;
    long long int seed;
    public:
    Random(const long long int& _seed, const long long int& _a = 25214903917ull, const long long int& _b = 11ull, const unsigned long long int& _m = (1ull << 48ull))
        : seed((_seed ^ _a) & (_m - 1)), a(_a), inv_a(modInverse(_a, _m)), b(_b), m(_m) {}// default values are taken from the java LCG "Random" RNG class
    Random(const Random& copy) = delete;
    Random(Random&& move) = delete;
    Random& operator=(const Random& copy) = delete;
    Random& operator=(Random&& move) = delete;
    void setSeed(const long long int& _seed);
    void setCarverSeed(const long long int& worldSeed, const int& chunkX, const int& chunkZ);
    void setCarverSeed(const long long int& worldSeed, const ChunkPos& chunk);
    static long long int getCarverSeed(const long long int& worldSeed, const int& chunkX, const int& chunkZ);
    static long long int getCarverSeed(const long long int& worldSeed, const ChunkPos& chunk);

    long long int currentSeed();
    long long int nextSeed();
    long long int nextSeed(const unsigned int& steps);
    long long int previousSeed();
    long long int previousSeed(const unsigned int& steps);
    int next(const unsigned int& bits);
    Random split();
    int nextInt();
    int nextInt(const unsigned int& bound);
    long long int nextLong();
    bool nextBoolean();
    float nextFloat();
    double nextDouble();
};
Direction getRandomHorizontalDirection(Random& rand);

extern const int testValues[450];
void testRand(const long long int& world_seed);

#endif// __RANDOM