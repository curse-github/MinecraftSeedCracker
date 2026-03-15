#ifndef __RANDOM
#define __RANDOM

#include <string>
#include <iostream>
#include <cmath>
#include "MinecraftLib.h"

unsigned long long int modInverse(unsigned long long int x, unsigned long long int y);
unsigned long long int fastExp(unsigned long long int a, unsigned long long int n);
unsigned long long int geom(unsigned long long int a, unsigned long long int n);

extern const unsigned long long int rand_seed_neg_1;
extern const unsigned long long int rand_seed_neg_2;
class BitRandomSource {
    public:
    virtual ~BitRandomSource() {}
    static long long int getCarverSeed(const long long int& worldSeed, const int& chunkX, const int& chunkZ);
    static long long int getCarverSeed(const long long int& worldSeed, const ChunkPos& chunk);

    virtual long long int nextSeed() = 0;
    virtual long long int nextSeed(const unsigned int& steps) = 0;

    virtual int nextInt() = 0;
    virtual int nextInt(const int& bound) = 0;
    virtual long long int nextLong() = 0;
    virtual bool nextBoolean() = 0;
    virtual float nextFloat() = 0;
    virtual double nextDouble() = 0;
};

struct LCGSplitter;
class LCG : public BitRandomSource {
    long long int a;
    long long int inv_a;
    long long int b;
    unsigned long long int m;
    public:
    long long int seed;
    int count = 0;
    LCG(const long long int& _seed, const long long int& _a = 25214903917ull, const long long int& _b = 11ull, const unsigned long long int& _m = (1ull << 48ull))
        : seed((_seed ^ _a) & (_m - 1)), a(_a), inv_a(modInverse(_a, _m)), b(_b), m(_m) {}// default values are taken from the java LCG "LCG" RNG class
    LCG(const LCG& copy) = delete;
    virtual ~LCG() {}
    LCG& operator=(const LCG& copy) = delete;
    
    virtual long long int nextSeed();
    virtual long long int nextSeed(const unsigned int& steps);
    long long int previousSeed();
    long long int previousSeed(const unsigned int& steps);

    void setSeed(const long long int& _seed);
    void setCarverSeed(const long long int& worldSeed, const int& chunkX, const int& chunkZ);
    void setCarverSeed(const long long int& worldSeed, const ChunkPos& chunk);
    
    int next(const int& bits);
    virtual int nextInt();
    virtual int nextInt(const int& bound);
    virtual long long int nextLong();
    virtual bool nextBoolean();
    virtual float nextFloat();
    virtual double nextDouble();

    LCG split();
    LCGSplitter nextSplitter();
};

struct XoroshiroSplitter;
class Xoroshiro : public BitRandomSource {
    long long int seedLo;
    long long int seedHi;
    public:
    Xoroshiro(const long long int& _seedHi, const long long int& _seedLo);
    Xoroshiro(const Xoroshiro& copy)
        : seedHi(copy.seedHi), seedLo(copy.seedLo) {}
    virtual ~Xoroshiro() {}
    Xoroshiro& operator=(const Xoroshiro& copy);

    virtual long long int nextSeed();
    virtual long long int nextSeed(const unsigned int& steps);
    
    void setSeed(const long long int& _seedHi, const long long int& _seedLo);
    
    virtual int nextInt();
    virtual int nextInt(const int& bound);
    virtual long long int nextLong();
    virtual bool nextBoolean();
    virtual float nextFloat();
    virtual double nextDouble();
    
    long long int nextBits(const int& bits);
    Xoroshiro split();
    XoroshiroSplitter nextSplitter();
};

Direction getRandomHorizontalDirection(BitRandomSource& rand);

int hashString(const std::string& str);
struct LCGSplitter {
    long long int seed;
    LCGSplitter(const long long int& _seed);
    LCGSplitter(const LCGSplitter& copy);
    ~LCGSplitter() {}
    LCGSplitter& operator=(const LCGSplitter& copy);
    LCG split(const std::string& hash);
    LCG split(const long long int& seed);
};
struct XoroshiroSplitter {
    long long int seedLo;
    long long int seedHi;
    XoroshiroSplitter(const long long int& _seedHi, const long long int& _seedLo);
    XoroshiroSplitter(const XoroshiroSplitter& copy);
    ~XoroshiroSplitter() {}
    XoroshiroSplitter& operator=(const XoroshiroSplitter& copy);
    //Xoroshiro at(const int& x, const int& y, const int& z);
    Xoroshiro fromHashOf(const std::string& hash);
    Xoroshiro fromSeed(const long long int& seed);
};

void testRand();

#endif// __RANDOM