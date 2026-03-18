#include "Xoroshiro.h"

#pragma region Xoroshiro

long long int mix(unsigned long long int seed) {
    seed = (seed ^ seed >> 30ll) * -4658895280553007687ll;
    seed = (seed ^ seed >> 27ll) * -7723592293110705685ll;
    return seed ^ seed >> 31ll;
}
Xoroshiro::Xoroshiro(const long long int& _seedHi, const long long int& _seedLo) : seedHi(_seedHi), seedLo(_seedLo) {
    if ((_seedHi | _seedLo) == 0L) {
        seedLo = -7046029254386353131ll;
        seedHi = 7640891576956012809ll;
    }
}
Xoroshiro& Xoroshiro::operator=(const Xoroshiro& copy) {
    seedHi = copy.seedHi;
    seedLo = copy.seedLo;
    return *this;
}
long long int rotateLeft(const long long int& num, const long long int& amount) {
    return (num << amount) | (num >> (64ll - amount));
}
// see Xoroshiro128PlusPlus.nextLong function in net.minecraft.world.level.levelgen.Xoroshiro128PlusPlus
long long int Xoroshiro::nextSeed() {
    long s0 = seedLo;
    long s1 = seedHi;
    long result = rotateLeft(s0 + s1, 17) + s0;
    s1 ^= s0;
    seedLo = rotateLeft(s0, 49) ^ s1 ^ s1 << 21;
    seedHi = rotateLeft(s1, 28);
    return result;
}
long long int Xoroshiro::nextSeed(const unsigned int& steps) {
    for (size_t i = 0; i < steps-1; i++) nextSeed();
    return nextSeed();
}

// see Xoroshiro128PlusPlus.Xoroshiro128PlusPlus() function in net.minecraft.world.level.levelgen.Xoroshiro128PlusPlus
void Xoroshiro::setSeed(const long long int& _seedHi, const long long int& _seedLo) {
    seedHi = _seedHi;
    seedLo = _seedLo;
    if ((_seedHi | _seedLo) == 0L) {
        seedLo = -7046029254386353131ll;
        seedHi = 7640891576956012809ll;
    }
}

// see XoroshiroRandomSource.nextInt() function in net.minecraft.world.level.levelgen.XoroshiroRandomSource
int Xoroshiro::nextInt() {
    return (int)nextLong();
}
unsigned long long int toUnsignedLong(const unsigned int& num) {
    return ((long long int)num) & 0xFFFFFFFFll;
}
unsigned long long int toUnsignedLong(const long long int& num) {
    return num & 0xFFFFFFFFll;
}
unsigned int remainderUnsigned(const unsigned int& a, const unsigned int& b) {
    return toUnsignedLong(a) % toUnsignedLong(b);
}
// see XoroshiroRandomSource.nextInt(int) function in net.minecraft.world.level.levelgen.XoroshiroRandomSource
int Xoroshiro::nextInt(const int& bound) {
    long long int randomBits = ((long long int)nextSeed()) & 0xFFFFFFFFll;// Integer.toUnsignedLong
    long long int multipliedRandomBits = randomBits * (long long int)bound;
    long long int fractionalPart = multipliedRandomBits & 0xFFFFFFFFll;
    if (fractionalPart < bound) {
        int unbiasedBucketsStartIndex = remainderUnsigned((bound ^ 0xFFFFFFFFll) + 1, bound);
        while (fractionalPart < unbiasedBucketsStartIndex) {
            randomBits = ((long long int)nextSeed()) & 0xFFFFFFFFll;
            multipliedRandomBits = randomBits * (long long int)bound;
            fractionalPart = multipliedRandomBits & 0xFFFFFFFFll;
        }
    }
    return (int)(multipliedRandomBits >> 32ll);
}
// see XoroshiroRandomSource.nextLong function in net.minecraft.world.level.levelgen.XoroshiroRandomSource
long long int Xoroshiro::nextLong() {
    return nextSeed();
}
// see XoroshiroRandomSource.nextBoolean function in net.minecraft.world.level.levelgen.XoroshiroRandomSource
bool Xoroshiro::nextBoolean() {
    return (nextLong() & 0x1ll) != 0ll;
}
// see XoroshiroRandomSource.nextBoolean function in net.minecraft.world.level.levelgen.XoroshiroRandomSource
float Xoroshiro::nextFloat() {
    return nextBits(24) * 5.9604645E-8f;
}
// see XoroshiroRandomSource.nextBoolean function in net.minecraft.world.level.levelgen.XoroshiroRandomSource
double Xoroshiro::nextDouble() {
    return nextBits(53) * 1.1102230246251565E-16;
}

long long int Xoroshiro::nextBits(const int& bits) {
    return nextSeed() >> (64ll - bits);
}

Xoroshiro Xoroshiro::split() {
    return Xoroshiro(nextLong(), nextLong());
}

#pragma region splitter

XoroshiroSplitter Xoroshiro::nextSplitter() {
    return XoroshiroSplitter(nextLong(), nextLong());
}
XoroshiroSplitter::XoroshiroSplitter(const long long int& _seedHi, const long long int& _seedLo) : seedLo(_seedLo), seedHi(_seedHi) {}
XoroshiroSplitter::XoroshiroSplitter(const XoroshiroSplitter& copy) : seedLo(copy.seedLo), seedHi(copy.seedHi) {}
XoroshiroSplitter& XoroshiroSplitter::operator=(const XoroshiroSplitter& copy) {
    seedLo = copy.seedLo;
    seedHi = copy.seedHi;
    return *this;
}
// see XoroshiroRandomSource.XoroshiroPositionalRandomFactory.at function in net.minecraft.world.level.levelgen.XoroshiroRandomSource
/*Xoroshiro XoroshiroSplitter::at(const int& x, const int& y, const int& z) {
    long positionalSeed = 0;//Mth.getSeed(x, y, z);
    long randomSeed = positionalSeed ^ seedLo;
    return Xoroshiro(randomSeed, seedHi);
}*/
// see XoroshiroRandomSource.XoroshiroPositionalRandomFactory.fromHashOf function in net.minecraft.world.level.levelgen.XoroshiroRandomSource
Xoroshiro XoroshiroSplitter::fromHashOf(const std::string& seed) {
    std::vector<unsigned int> md5 = md5Raw((unsigned char*)seed.data(), seed.size());
    unsigned long long int tmpLo = ((((unsigned long long int)md5[0]) << 32ull) | (unsigned long long int)md5[1]);
    unsigned long long int tmpHi = ((((unsigned long long int)md5[2]) << 32ull) | (unsigned long long int)md5[3]);
    return Xoroshiro(tmpLo ^ seedLo, tmpHi ^ seedHi);
}
// see XoroshiroRandomSource.XoroshiroPositionalRandomFactory.fromSeed function in net.minecraft.world.level.levelgen.XoroshiroRandomSource
Xoroshiro XoroshiroSplitter::fromSeed(const long long int& seed) {
    return Xoroshiro(seed ^ seedLo, seed ^ seedHi);
}

#pragma endregion// splitter