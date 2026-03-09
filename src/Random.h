#ifndef __RANDOM
#define __RANDOM

#include <string>
#include <iostream>
#include <cmath>
#include "MinecraftLib.h"
#include "md5.h"

unsigned long long int modInverse(unsigned long long int x, unsigned long long int y);
unsigned long long int fastExp(unsigned long long int a, unsigned long long int n);
unsigned long long int geom(unsigned long long int a, unsigned long long int n);

extern const unsigned long long int rand_seed_neg_1;
extern const unsigned long long int rand_seed_neg_2;
class Random {
    public:
    virtual ~Random() {}
    virtual void setSeed(const long long int& _seed) = 0;
    static long long int getCarverSeed(const long long int& worldSeed, const int& chunkX, const int& chunkZ);
    static long long int getCarverSeed(const long long int& worldSeed, const ChunkPos& chunk);

    virtual long long int nextSeed() = 0;
    virtual long long int nextSeed(const unsigned int& steps) = 0;
    long long int next();
    virtual long long int next(const unsigned int& bits) = 0;
    int nextInt();
    virtual int nextInt(const unsigned int& bound);
    virtual long long int nextLong();
    bool nextBoolean();
    float nextFloat();
    double nextDouble();
};
struct LCGSplitter;
class LCG : public Random {
    long long int a;
    long long int inv_a;
    long long int b;
    unsigned long long int m;
    long long int seed;
    public:
    LCG(const long long int& _seed, const long long int& _a = 25214903917ull, const long long int& _b = 11ull, const unsigned long long int& _m = (1ull << 48ull))
        : seed((_seed ^ _a) & (_m - 1)), a(_a), inv_a(modInverse(_a, _m)), b(_b), m(_m) {}// default values are taken from the java LCG "LCG" RNG class
    LCG(const LCG& copy)
        : seed(copy.seed), a(copy.a), inv_a(copy.inv_a), b(copy.b), m(copy.m) {}// default values are taken from the java LCG "LCG" RNG class
    virtual ~LCG() {}
    LCG& operator=(const LCG& copy);
    void setSeed(const long long int& _seed);
    void setCarverSeed(const long long int& worldSeed, const int& chunkX, const int& chunkZ);
    void setCarverSeed(const long long int& worldSeed, const ChunkPos& chunk);
    virtual long long int next(const unsigned int& bits);

    long long int currentSeed();
    long long int nextSeed();
    long long int nextSeed(const unsigned int& steps);
    long long int previousSeed();
    long long int previousSeed(const unsigned int& steps);
    LCG split();
    LCGSplitter nextSplitter();
};
struct XoroshiroSplitter;
class Xoroshiro : public Random {
    public:// TEMP
    long long int seedLo;
    long long int seedHi;
    public:
    Xoroshiro(const long long int& seed);
    Xoroshiro(const long long int& _seedHi, const long long int& _seedLo);
    Xoroshiro(const Xoroshiro& copy)
        : seedHi(copy.seedHi), seedLo(copy.seedLo) {}
    virtual ~Xoroshiro() {}
    Xoroshiro& operator=(const Xoroshiro& copy);
    void setSeed(const long long int& seed);
    void setSeed(const long long int& _seedHi, const long long int& _seedLo);
    virtual long long int next(const unsigned int& bits);
    
    virtual int nextInt(const unsigned int& bound);
    virtual long long int nextLong();

    long long int nextSeed();
    long long int nextSeed(const unsigned int& steps);
    long long int previousSeed();
    long long int previousSeed(const unsigned int& steps);
    Xoroshiro split();
    XoroshiroSplitter nextSplitter();
};

Direction getRandomHorizontalDirection(Random& rand);

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
    Xoroshiro split(const std::string& hash);
    Xoroshiro split(const long long int& seed);
};

struct PerlinNoise {
    private:
    Vec3D origin;
    unsigned char permutation[256];
    public:
    PerlinNoise(Xoroshiro* rand) : origin(rand->nextDouble()*256, rand->nextDouble()*256, rand->nextDouble()*256) {
        for (int i = 0; i < 256; i++)
            permutation[i] = i;
        init(rand);
    }
    PerlinNoise(const PerlinNoise& copy) = delete;
    virtual ~PerlinNoise() {}
    PerlinNoise& operator=(const PerlinNoise& copy) = delete;
    double sample(const double& x, const double& y, const double& z) const;
    double sample(const Vec3D& position) const;
    double sample(const double& x, const double& y, const double& z, const double& yScale, const double& yMax) const;
    double sample(Vec3D V, const double& yScale, const double& yMax) const;
    double sampleDerivative(const double& x, const double& y, const double& z, std::vector<double>& idk) const;
    double sampleDerivative(const Vec3D& position, std::vector<double>& idk) const;
    private:
    void init(Xoroshiro* rand);
    int map(const int& input) const;
    double grad(const int& hash, const Vec3D& position) const;
    double sample(const Vec3& iXYZ, const Vec3D& dV, const double& fadeLocalY) const;
    double sampleDerivative(const Vec3& section, const Vec3D& local, std::vector<double>& idk) const;
};
struct OctavePerlinNoise {
    int firstOctave;
    double lacunarity;
    double persistence;
    double maxValue;
    std::vector<PerlinNoise*> samplers;
    std::vector<double> amplitudes;
    OctavePerlinNoise(Xoroshiro* rand, const int& firstOctave, const std::vector<double>& amplitudes);
    OctavePerlinNoise(const OctavePerlinNoise& copy);
    ~OctavePerlinNoise();
    OctavePerlinNoise& operator=(const OctavePerlinNoise& copy) = delete;
    double sample(const double& x, const double& y, const double& z) const;
    double sample(const Vec3D& v) const;
    double getMaxValue() const;
};
struct DoublePerlinNoise {
    OctavePerlinNoise firstSampler;
    OctavePerlinNoise secondSampler;
    double amplitude;
    double maxValue;
    DoublePerlinNoise(Xoroshiro* rand, const int& firstOctave, const std::vector<double>& amplitudes);
    DoublePerlinNoise(const DoublePerlinNoise& copy) = delete;
    DoublePerlinNoise(DoublePerlinNoise&& move) = delete;
    ~DoublePerlinNoise() {}
    DoublePerlinNoise& operator=(const DoublePerlinNoise& copy) = delete;
    DoublePerlinNoise& operator=(DoublePerlinNoise&& move) = delete;
    double sample(const double& x, const double& y, const double& z) const;
    double sample(const Vec3D& v) const;
    double getMaxValue() const;
};
struct SimplexNoise {
    private:
    Vec3D origin;
    unsigned char permutation[256];
    public:
    SimplexNoise() {}
    SimplexNoise(Xoroshiro* rand) : origin(rand->nextDouble()*256, rand->nextDouble()*256, rand->nextDouble()*256)
    {
        for (int i = 0; i < 256; i++)
            permutation[i] = i;
        init(rand);
    }
    virtual ~SimplexNoise() {}
    SimplexNoise(const SimplexNoise& copy) = delete;
    SimplexNoise(SimplexNoise&& move) = delete;
    double sample(const double& x, const double& z) const;
    double sample(const Vec2D& V) const;
    double sample(const double& x, const double& y, const double& z) const;
    double sample(const Vec3D& V) const;
    private:
    void init(Xoroshiro* rand);
    int map(const int& input) const;
    double grad(const int& hash, const Vec3D& position, double r_2) const;
};

void testRand(const long long int& world_seed);

#endif// __RANDOM