#include "Random.h"

#pragma region Random

#pragma region rand helpers
unsigned long long int modInverse(unsigned long long int x, unsigned long long int y) {
    const unsigned long long int y_0 = y;
    unsigned long long int q = 0;
    unsigned long long int r = 0;
    unsigned long long int s_0 = 0;
    unsigned long long int s_1 = 0;
    unsigned long long int s_2 = 1;
    while (y != 0) {
        q = x/y;
        r = x%y;
        s_0 = s_2 - q * s_1;
        // cycle variables
        x = y;
        y = r;
        s_2 = s_1;
        s_1 = s_0;
    }
    return s_2;
}
unsigned long long int fastExp(unsigned long long int a, unsigned long long int n) {
    unsigned long long int ret = 1;
    while (n) {
        if (n & 1) ret *= a;
        n >>= 1;
        a *= a;
    }
    return ret;
}
// should be able to be replaced by (a^n-1)/(a-1) but wasnt working for some reason
unsigned long long int geom(unsigned long long int a, unsigned long long int n) {
    unsigned long long int geom = 1;
    unsigned long long int exp = a;
    for (unsigned int i = 1; i < n; i++) { geom += exp; exp *= a; }
    return geom;
}

const unsigned long long int rand_seed_0 = 25214903917ull;
const unsigned long long int rand_seed_neg_1 = 107038380838084ull;
const unsigned long long int rand_seed_neg_2 = 120323824315451ull;
const unsigned long long int rand_seed_neg_3 = 164311266871034ull;
const unsigned long long int rand_seed_neg_4 = 41012168809809ull;
const unsigned long long int rand_seed_neg_5 = 240144965573432ull;
const unsigned long long int rand_seed_neg_6 = 205933732203103ull;
const unsigned long long int rand_seed_neg_7 = 252022095509006ull;
const unsigned long long int rand_seed_neg_8 = 61220665793749ull;
const unsigned long long int rand_seed_neg_9 = 158745686432556ull;
#pragma endregion rand helpers

long long int Random::next() {
    return nextSeed();
}
// see implementation of BaseRandom.nextInt() in BaseRandom
int Random::nextInt() {
    return static_cast<int>(next(32));
}
// see implementation of BaseRandom.nextInt(int bound) in BaseRandom
int Random::nextInt(const unsigned int& bound) {
    if ((bound & (bound - 1)) == 0) return (int)((long long int)bound * (long long int)next(31) >> 31);
    int _1 = 0;
    int _2 = 0;
    while (((_1 = next(31)) - (_2 = (_1 % bound)) + (bound - 1)) < 0) {}
    return _2;
}
// see implementation of BaseRandom.nextLong() in BaseRandom
long long int Random::nextLong() {
    return (((long long int)(int)next(32)) << 32ull) + ((long long int)(int)next(32));
}
// see implementation of BaseRandom.nextBoolean() in BaseRandom
bool Random::nextBoolean() {
    return next(1) != 0;
}
// see implementation of BaseRandom.nextFloat() in BaseRandom
float Random::nextFloat() {
    return (float)next(24) * 5.9604645E-8f;
}
// see implementation of BaseRandom.nextDouble() in BaseRandom
double Random::nextDouble() {
    return (double)(((long long int)next(26) << 27) + ((long long int)next(27))) * 1.110223E-16;
}
// see ChunkRandom.setCarverSeed function in ChunkRandom.java
long long int Random::getCarverSeed(const long long int& worldSeed, const int& chunkX, const int& chunkZ) {
    LCG rand(worldSeed);
    return ((long long int)chunkX * rand.nextLong()) ^ ((long long int)chunkZ * rand.nextLong()) ^ worldSeed;
}
long long int Random::getCarverSeed(const long long int& worldSeed, const ChunkPos& chunk) {
    return getCarverSeed(worldSeed, chunk.x, chunk.z);
}
// see StructurePiece.getRandomHorizontalDirection function in StructurePiece.java
Direction getRandomHorizontalDirection(Random& rand) {
    return HorizontalDirections[rand.nextInt(4)];
}

#pragma region LCG
LCG& LCG::operator=(const LCG& copy) {
    a = copy.a;
    inv_a = copy.inv_a;
    b = copy.b;
    m = copy.m;
    seed = copy.seed;
    return *this;
}
void LCG::setSeed(const long long int& _seed) {
    seed = (_seed ^ a) & (m - 1);
}
// see ChunkRandom.setCarverSeed function in ChunkRandom.java
void LCG::setCarverSeed(const long long int& worldSeed, const int& chunkX, const int& chunkZ) {
    setSeed(worldSeed);
    setSeed(((long long int)chunkX * nextLong()) ^ ((long long int)chunkZ * nextLong()) ^ worldSeed);
}
void LCG::setCarverSeed(const long long int& worldSeed, const ChunkPos& chunk) {
    setCarverSeed(worldSeed, chunk.x, chunk.z);
}
// see CheckedRandom.next(int bits) in CheckedRandom.java
long long int LCG::next(const unsigned int& bits) {
    return nextSeed() >> (48 - bits);
}
long long int LCG::currentSeed() {
    return seed;
}
// new_seed = ((a * seed) + b) % m
long long int LCG::nextSeed() {
    seed *= a;
    seed += b;
    seed &= m-1;
    return seed;
}
// new_seed = ((a * seed) + b) % m
// new_new_seed = ((a * new_seed) + b) % m
// new_new_seed = ((a * ((a * seed) + b)) + b) % m
// new_new_seed = ((((a^2) * seed) + ab) + b) % m
// new_new_seed = (((a^2) * seed) + b(a + 1)) % m
// new_new_new_seed = (((a^2) * new_seed) + b(a + 1)) % m
// new_new_new_seed = (((a^2) * ((a * seed) + b)) + b(a + 1)) % m
// new_new_new_seed = ((a^3 * seed) + b(a^2 + a + 1)) % m
// ...
// new_seed(n) = ((a^n) * seed + b(a^n + a^(n-1) + a^(n-2) + ... + a^3 + a^2 + a + 1)) % m
// new_seed(n) = ((a^n) * seed + b*geom(n-1)) % m
// where
// geom(n-1) = x^(n-1) + x^(n-2) + x^(n-3) + ... + x^3 + x^2 + x + 1
// x*geom(n-1) = x^n + x^(n-1) + x^(n-2) + ... + x^3 + x^2 + x
// x*geom(n-1) + 1 = x^n + x^(n-1) + x^(n-2) + ... + x^3 + x^2 + x + 1
// x*geom(n-1) + 1 - geom(n-1) = x^n
// geom(n-1)(x - 1) = x^n - 1
// geom(n-1) = ((x^n)-1)/(x-1)
// ...
// new_seed(n) = (a^n) * seed + b((a^n)-1)/(a-1)
long long int LCG::nextSeed(const unsigned int& steps) {
    if (steps == 1) return nextSeed();
    seed *= fastExp(a, steps);
    seed += b * geom(a, steps);
    seed &= m - 1;
    return seed;
}
// seed = ((a * old_seed) + b) % m
// seed - b = a * old_seed % m
// old_seed = (seed - b) * inv_a % m
// old_seed = (seed - b) * inv_a % m
long long int LCG::previousSeed() {
    seed -= b;
    seed *= inv_a;
    seed &= m-1;
    return seed;
}
// old_seed = (seed - b) * inv_a % m
// old_old_seed = (old_seed - b) * inv_a % m
// old_old_seed = ((seed - b) * inv_a - b) * inv_a % m
// old_old_old_seed = (seed*inv_a^3 - b*inv_a^3 - b*inv_a^2 - b*inv_a) % m
// old_old_old_seed = (seed - b*(1 + inv_a^-1 + inv_a^-2)) * inv_a^3 % m
// old_old_old_seed = (seed - b*(1 + a + a^2)) * inv_a^3 % m
// ...
// old_old_old_seed = (seed - b*geom(n-1)) * inv_a^n % m
// old_old_old_seed = (seed - b*((a^n)-1)/(a-1)) * inv_a^n % m
long long int LCG::previousSeed(const unsigned int& steps) {
    if (steps == 1) return previousSeed();
    seed -= b * geom(a, steps);
    seed *= fastExp(inv_a, steps);
    seed &= m-1;
    return seed;
}
// see implementation of CheckedRandom.split() in CheckedRandom.java
LCG LCG::split() {
    return LCG(nextLong(), a, b, m);
}
// see implementation of CheckedRandom.nextSplitter() in CheckedRandom.java
LCGSplitter LCG::nextSplitter() {
    return LCGSplitter(nextLong());
}

int hashString(const std::string& str) {
    int value = 0;
    int multiplier = 1;
    for (size_t i = str.size() - 1; i < str.size(); i--) {
        value += str[i]*multiplier;
        multiplier *= 31;
    }
    return value;
}
LCGSplitter::LCGSplitter(const long long int& _seed) : seed(_seed) {}
LCGSplitter::LCGSplitter(const LCGSplitter& copy) : seed(copy.seed) {}
LCGSplitter& LCGSplitter::operator=(const LCGSplitter& copy) {
    seed = copy.seed;
    return *this;
}
// see CheckedRandom.Splitter.split(string) function in CheckedRandom.java
LCG LCGSplitter::split(const std::string& _seed) {
    return LCG(seed ^ hashString(_seed));
}
// see CheckedRandom.Splitter.split(long) function in CheckedRandom.java
LCG LCGSplitter::split(const long long int& _seed) {
    return LCG(_seed);
}
#pragma endregion LCG

#pragma region Xoroshiro
long long int mix(unsigned long long int seed) {
    seed = (seed ^ seed >> 30ll) * -4658895280553007687ll;
    seed = (seed ^ seed >> 27ll) * -7723592293110705685ll;
    return seed ^ seed >> 31ll;
}
Xoroshiro::Xoroshiro(const long long int& seed) {
    seedLo = seed ^ 0x6A09E667F3BCC909ll;
    seedHi = mix(seedLo + -7046029254386353131ll);
    seedLo = mix(seedLo);
}
Xoroshiro::Xoroshiro(const long long int& _seedHi, const long long int& _seedLo) : seedHi(_seedHi), seedLo(_seedLo) {
    if ((_seedHi | _seedLo) == 0L) {
        seedLo = -7046029254386353131L;
        seedHi = 7640891576956012809L;
    }
}
Xoroshiro& Xoroshiro::operator=(const Xoroshiro& copy) {
    seedHi = copy.seedHi;
    seedLo = copy.seedLo;
    return *this;
}
// see Xoroshiro128PlusPlusRandom.setSeed function in Xoroshiro128PlusPlusRandom.java
// see RandomSeed.createXoroshiroSeed statuc function in RandomSeed.java
//      see RandomSeed.createUnmixedXoroshiroSeed statuc function in RandomSeed.java
//          see RandomSeed.XoroshiroSeed.mix function in RandomSeed.java
//              see RandomSeed.mixStafford13 static function in RandomSeed.java
// see Xoroshiro128PlusPlusRandomImpl constructor in Xoroshiro128PlusPlusRandomImpl.java
void Xoroshiro::setSeed(const long long int& seed) {
    seedLo = seed ^ 0x6A09E667F3BCC909ll;
    seedHi = mix(seedLo + -7046029254386353131ll);
    seedLo = mix(seedLo);
}
void Xoroshiro::setSeed(const long long int& _seedHi, const long long int& _seedLo) {
    seedHi = _seedHi;
    seedLo = _seedLo;
    if ((_seedHi | _seedLo) == 0L) {
        seedLo = -7046029254386353131L;
        seedHi = 7640891576956012809L;
    }
}
// see CheckedRandom.next(int bits) in CheckedRandom.java
long long int Xoroshiro::next(const unsigned int& bits) {
    return nextSeed() >> (64ll - bits);
}
unsigned long long int intToLong(const unsigned int& num) {
    return ((long)num) & 0xFFFFFFFFll;
}
unsigned int intToLongMod(const unsigned int& a, const unsigned int& b) {
    return intToLong(a) % intToLong(b);
}
int Xoroshiro::nextInt(const unsigned int& bound) {
    long long int _2 = intToLong(nextSeed()) * ((long long int)bound);
    long long int _3 = intToLong(_2);
    if (_3 < bound) {
        int _4 = intToLongMod(~bound + 1, bound);
        while (_3 < _4) {
            _2 = intToLong(nextSeed()) * ((long long int)bound);
            _3 = _3 = _2 & 0xFFFFFFFFll;
        }
    }
    return (int)(_2 >> 32ll);
}
long long int Xoroshiro::nextLong() {
    return nextSeed();
}

long long int rotateLeft(const long long int& num, const long long int& amount) {
    return (num << amount) | (num >> (64ll - amount));
}
long long int Xoroshiro::nextSeed() {
    long long int _0 = seedLo;
    long long int _1 = seedHi;
    long long int _2 = rotateLeft(_0 + _1, 17ll) + _0;
    seedLo = rotateLeft(_0, 49ll) ^ (_1 ^= _0) ^ _1 << 21ll;
    seedHi = rotateLeft(_1, 28ll);
    return _2;
}
long long int Xoroshiro::nextSeed(const unsigned int& steps) {
    for (size_t i = 0; i < steps-1; i++) nextSeed();
    return nextSeed();
}
Xoroshiro Xoroshiro::split() {
    return Xoroshiro(nextLong(), nextLong());
}
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
//  see Xoroshiro128PlusPlusRandom.RandomSplitter.split function in Xoroshiro128PlusPlusRandom.java
//      see RandomSeed.createXoroshiroSeed function in RandomSeed.java
//          see RandomSeed.XoroshiroSeed.split function in RandomSeed.java
Xoroshiro XoroshiroSplitter::split(const std::string& seed) {
    std::vector<unsigned int> md5 = md5Raw((unsigned char*)seed.data(), seed.size());
    unsigned long long int tmpLo = ((((unsigned long long int)md5[0]) << 32ull) | (unsigned long long int)md5[1]);
    unsigned long long int tmpHi = ((((unsigned long long int)md5[2]) << 32ull) | (unsigned long long int)md5[3]);
    return Xoroshiro(tmpLo ^ seedLo, tmpHi ^ seedHi);
}
Xoroshiro XoroshiroSplitter::split(const long long int& seed) {
    return Xoroshiro(seed ^ seedLo, seed ^ seedHi);
}
#pragma endregion Xoroshiro


// used https://onecompiler.com/java/43q7xtnkm to get values and the following code
/*
import java.util.Scanner;
import java.util.LCG;

public class RandomNumbers {
    public static void main(String[] args) {
        int num = 30;
        long world_seed = 56871783007L;
        LCG random = new LCG();
        random.setSeed(world_seed);
        long carver_seed = ((long)15 * random.nextLong()) ^ ((long)15 * random.nextLong()) ^ world_seed;
        random.setSeed(carver_seed);
        long carver_carver_seed = ((long)15 * random.nextLong()) ^ ((long)15 * random.nextLong()) ^ carver_seed;
        random.setSeed(world_seed);
        for (int i = 0; i < num; i++)
            System.out.print(random.nextInt(5) + ",");
        random.setSeed(world_seed);
        System.out.println("");
        for (int i = 0; i < num; i++)
            System.out.print(random.nextInt(4) + ",");
        random.setSeed(world_seed);
        System.out.println("");
        for (int i = 0; i < num; i++)
            System.out.print((random.nextInt(2) == 0) + ",");
        random.setSeed(world_seed);
        System.out.println("");
        for (int i = 0; i < num; i++)
            System.out.print(random.nextBoolean() + ",");
        random.setSeed(world_seed);
        System.out.println("");
        for (int i = 0; i < num; i++)
            System.out.print((random.nextInt(3) > 0) + ",");
        random.setSeed(carver_seed);
        System.out.println("");
        for (int i = 0; i < num; i++)
            System.out.print(random.nextInt(5) + ",");
        random.setSeed(carver_seed);
        System.out.println("");
        for (int i = 0; i < num; i++)
            System.out.print(random.nextInt(4) + ",");
        random.setSeed(carver_seed);
        System.out.println("");
        for (int i = 0; i < num; i++)
            System.out.print((random.nextInt(2) == 0) + ",");
        random.setSeed(carver_seed);
        System.out.println("");
        for (int i = 0; i < num; i++)
            System.out.print(random.nextBoolean() + ",");
        random.setSeed(carver_seed);
        System.out.println("");
        for (int i = 0; i < num; i++)
            System.out.print((random.nextInt(3) > 0) + ",");
        random.setSeed(carver_carver_seed);
        System.out.println("");
        for (int i = 0; i < num; i++)
            System.out.print(random.nextInt(5) + ",");
        random.setSeed(carver_carver_seed);
        System.out.println("");
        for (int i = 0; i < num; i++)
            System.out.print(random.nextInt(4) + ",");
        random.setSeed(carver_carver_seed);
        System.out.println("");
        for (int i = 0; i < num; i++)
            System.out.print((random.nextInt(2) == 0) + ",");
        random.setSeed(carver_carver_seed);
        System.out.println("");
        for (int i = 0; i < num; i++)
            System.out.print(random.nextBoolean() + ",");
        random.setSeed(carver_carver_seed);
        System.out.println("");
        for (int i = 0; i < num; i++)
            System.out.print((random.nextInt(3) > 0) + ",");
    }
}
*/
const int testValues[450] = {
    1,4,0,0,0,3,1,1,2,0,0,0,4,4,3,3,2,3,0,4,1,2,4,1,4,0,2,3,4,3,
    2,2,1,2,0,1,1,3,2,0,1,0,0,2,1,3,0,1,0,2,1,1,1,3,2,3,0,2,1,0,
    false,false,true,false,true,true,true,false,false,true,true,true,true,false,true,false,true,true,true,false,true,true,true,false,false,false,true,false,true,true,
    true,true,false,true,false,false,false,true,true,false,false,false,false,true,false,true,false,false,false,true,false,false,false,true,true,true,false,true,false,false,
    true,true,true,true,true,true,false,true,true,true,true,true,false,true,false,true,false,false,true,true,true,false,true,true,true,false,true,true,false,true,
    4,0,3,2,0,2,3,2,3,0,2,0,4,3,4,0,4,1,0,1,1,2,3,1,3,0,3,2,0,2,
    3,0,0,0,1,1,0,0,1,0,3,1,0,0,0,0,1,1,2,0,0,0,3,0,2,0,3,0,0,3,
    false,true,true,true,true,true,true,true,true,true,false,true,true,true,true,true,true,true,false,true,true,true,false,true,false,true,false,true,true,false,
    true,false,false,false,false,false,false,false,false,false,true,false,false,false,false,false,false,false,true,false,false,false,true,false,true,false,true,false,false,true,
    true,true,false,true,false,true,true,true,true,true,false,true,true,true,false,true,true,true,true,false,true,false,true,true,true,true,true,true,true,true,
    2,1,2,3,3,3,4,0,0,1,0,1,3,3,1,2,3,3,3,0,4,4,3,3,0,0,0,0,0,0,
    0,2,0,0,1,0,0,0,0,0,0,2,0,3,1,1,3,2,3,1,3,0,3,2,2,1,3,0,1,1,
    true,false,true,true,true,true,true,true,true,true,true,false,true,false,true,true,false,false,false,true,false,true,false,false,false,true,false,true,true,true,
    false,true,false,false,false,false,false,false,false,false,false,true,false,true,false,false,true,true,true,false,true,false,true,true,true,false,true,false,false,false,
    false,true,false,true,true,true,true,true,true,false,false,true,false,false,false,true,true,true,true,true,true,true,true,true,false,true,false,true,true,true
};
void testRand(const long long int& world_seed) {
    bool allRight = true;
    int num = 30;
    LCG rand(world_seed);
    long long int carver_seed = LCG::getCarverSeed(world_seed, 15, 15);
    long long int carver_carver_seed = LCG::getCarverSeed(carver_seed, 15, 15);
    // world_seed
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(5) != testValues[i]) { allRight = false; std::cout << "ERROR1\n"; } }
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(4) != testValues[num + i]) { allRight = false; std::cout << "ERROR2\n"; } }
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(2) == 0) != testValues[(2*num) + i]) { allRight = false; std::cout << "ERROR3\n"; } }
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextBoolean() != testValues[(3*num) + i]) { allRight = false; std::cout << "ERROR4\n"; } }
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(3) > 0) != testValues[(4*num) + i]) { allRight = false; std::cout << "ERROR5\n"; } }
    // carver_seed
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(5) != testValues[(5*num) + i]) { allRight = false; std::cout << "ERROR6\n"; } }
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(4) != testValues[(6*num) + i]) { allRight = false; std::cout << "ERROR7\n"; } }
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(2) == 0) != testValues[(7*num) + i]) { allRight = false; std::cout << "ERROR8\n"; } }
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextBoolean() != testValues[(8*num) + i]) { allRight = false; std::cout << "ERROR9\n"; } }
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(3) > 0) != testValues[(9*num) + i]) { allRight = false; std::cout << "ERROR10\n"; } }
    // carver_carver_seed
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(5) != testValues[(10*num) + i]) { allRight = false; std::cout << "ERROR11\n"; } }
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(4) != testValues[(11*num) + i]) { allRight = false; std::cout << "ERROR12\n"; } }
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(2) == 0) != testValues[(12*num) + i]) { allRight = false; std::cout << "ERROR13\n"; } }
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextBoolean() != testValues[(13*num) + i]) { allRight = false; std::cout << "ERROR14\n"; } }
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(3) > 0) != testValues[(14*num) + i]) { allRight = false; std::cout << "ERROR15\n"; } }
    
    if (allRight) std::cout << "All random values were correct.\n";
    else std::cout << "Some random values were not correct.\n";
}

#pragma endregion Random

#pragma region Noise
// see PERLIN_GRADIENTS constant in SimplexNoiseSampler.java
const int PERLIN_GRADIENTS[16][3] = {{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}, {1, 1, 0}, {0, -1, 1}, {-1, 1, 0}, {0, -1, -1}};
// see PerlinNoiseSample.dot function in PerlinNoiseSample.java
double dot(const int* gradient, const Vec3D& position) {
    return (double)gradient[0] * position.x + (double)gradient[1] * position.y + (double)gradient[2] * position.z;
}
// see MathHelper.perlinFade function in MathHelper.java
double perlinFade(double value) {
    return value * value * value * (value * (value * 6.0 - 15.0) + 10.0);
}
// see MathHelper.perlinFadeDerivative function in MathHelper.java
double perlinFadeDerivative(double value) {
    return 30.0 * value * value * (value - 1.0) * (value - 1.0);
}
// see MathHelper.lerp function in MathHelper.java
double lerp(const double& x, const double& X, const double& t) {
    return x + (t * (X - x));
}
// see MathHelper.lerp2 function in MathHelper.java
double lerp2(const double& xy, const double& Xy, const double& xY, const double& XY, const double& tX, const double& tY) {
    return lerp(lerp(xy, Xy, tX), lerp(xY, XY, tX), tY);
}
double lerp2(const double& xy, const double& Xy, const double& xY, const double& XY, const Vec2D& t) {
    return lerp(lerp(xy, Xy, t.x), lerp(xY, XY, t.x), t.z);
}
// see MathHelper.lerp3 function in MathHelper.java
double lerp3(const double& xyz, const double& Xyz, const double& xYz, const double& XYz, const double& xyZ, const double& XyZ, const double& xYZ, const double& XYZ, const double& tX, const double& tY, const double& tZ) {
    return lerp(lerp2(xyz, Xyz, xYz, XYz, tX, tY), lerp2(xyZ, XyZ, xYZ, XYZ, tX, tY), tZ);
}
double lerp3(const double& xyz, const double& Xyz, const double& xYz, const double& XYz, const double& xyZ, const double& XyZ, const double& xYZ, const double& XYZ, const Vec3D& t) {
    return lerp(lerp2(xyz, Xyz, xYz, XYz, t.x, t.y), lerp2(xyZ, XyZ, xYZ, XYZ, t.x, t.y), t.z);
}

#pragma region Perlin
double PerlinNoise::sample(const double& x, const double& y, const double& z) const {
    return sample(Vec3D(x, y, z), 0.0, 0.0);
}
double PerlinNoise::sample(const Vec3D& position) const {
    return sample(position, 0.0, 0.0);
}
double PerlinNoise::sample(const double& x, const double& y, const double& z, const double& yScale, const double& yMax) const {
    return sample(Vec3D(x, y, z), yScale, yMax);
}
double PerlinNoise::sample(Vec3D V, const double& yScale, const double& yMax) const {
    V = V + origin;
    const Vec3 sectionV(floor(V.x), floor(V.y), floor(V.z));
    Vec3D localV = V - sectionV;
    double offsetY = 0;
    if (yScale != 0) {
        bool needsClamp = (yMax >= 0) && (yMax < localV.y);
        double clampedY = needsClamp ? yMax : localV.y;
        offsetY = floor((clampedY / yScale) + 1.0E-7) * yScale;// round to the nearest yScale
    }
    return sample(sectionV, localV - Vec3D(0, offsetY, 0), localV.y);
}
// see PerlinNoiseSample.sampleDerivative(double*3, double[]) function in PerlinNoiseSample.java
double PerlinNoise::sampleDerivative(const double& x, const double& y, const double& z, std::vector<double>& idk) const {
    return sampleDerivative(Vec3D(x, y, z), idk);
}
double PerlinNoise::sampleDerivative(const Vec3D& V, std::vector<double>& idk) const {
    const Vec3 fV(floor(V.x), floor(V.y), floor(V.z));
    const Vec3 sectionV(fV.x & 0xFF, fV.y & 0xFF, fV.z & 0xFF);
    Vec3D localV = V - fV;
    return sampleDerivative(sectionV, localV, idk);
}
// see PerlinNoiseSample.sample(double*7) function in PerlinNoiseSample.java, but thats broken due to decompilation artifacts :(
// https://rtouti.github.io/graphics/perlin-noise-algorithm helped a lot
double PerlinNoise::sample(const Vec3& sectionV, const Vec3D& localV, const double& fadeLocalY) const {
    const int Mx = map(sectionV.x);
    const int MX = map(sectionV.x + 1);
    const int Mxy = map(Mx + sectionV.y    );
    const int MXy = map(MX + sectionV.y    );
    const int MxY = map(Mx + sectionV.y + 1);
    const int MXY = map(MX + sectionV.y + 1);
    const double xyz = PerlinNoise::grad(map(Mxy + sectionV.z    ), localV + Vec3D( 0,  0,  0));
    const double Xyz = PerlinNoise::grad(map(MXy + sectionV.z    ), localV + Vec3D(-1,  0,  0));
    const double xYz = PerlinNoise::grad(map(MxY + sectionV.z    ), localV + Vec3D( 0, -1,  0));
    const double XYz = PerlinNoise::grad(map(MXY + sectionV.z    ), localV + Vec3D(-1, -1,  0));
    const double xyZ = PerlinNoise::grad(map(Mxy + sectionV.z + 1), localV + Vec3D( 0,  0, -1));
    const double XyZ = PerlinNoise::grad(map(MXy + sectionV.z + 1), localV + Vec3D(-1,  0, -1));
    const double xYZ = PerlinNoise::grad(map(MxY + sectionV.z + 1), localV + Vec3D( 0, -1, -1));
    const double XYZ = PerlinNoise::grad(map(MXY + sectionV.z + 1), localV + Vec3D(-1, -1, -1));
    return lerp3(xyz, Xyz, xYz, XYz, xyZ, XyZ, xYZ, XYZ, Vec3D(perlinFade(localV.x), perlinFade(fadeLocalY), perlinFade(localV.z)));
}
// see PerlinNoiseSample.sample(double*7) function in PerlinNoiseSample.java
double PerlinNoise::sampleDerivative(const Vec3& sectionV, const Vec3D& localV, std::vector<double>& _6) const {
    const int Mx = map(sectionV.x);
    const int MX = map(sectionV.x + 1);
    const int Mxy = map(Mx + sectionV.y    );
    const int MXy = map(MX + sectionV.y    );
    const int MxY = map(Mx + sectionV.y + 1);
    const int MXY = map(MX + sectionV.y + 1);
    const int* Gxyz = PERLIN_GRADIENTS[map(Mxy + sectionV.z    )];
    const int* GXyz = PERLIN_GRADIENTS[map(MXy + sectionV.z    )];
    const int* GxYz = PERLIN_GRADIENTS[map(MxY + sectionV.z    )];
    const int* GXYz = PERLIN_GRADIENTS[map(MXY + sectionV.z    )];
    const int* GxyZ = PERLIN_GRADIENTS[map(Mxy + sectionV.z + 1)];
    const int* GXyZ = PERLIN_GRADIENTS[map(MXy + sectionV.z + 1)];
    const int* GxYZ = PERLIN_GRADIENTS[map(MxY + sectionV.z + 1)];
    const int* GXYZ = PERLIN_GRADIENTS[map(MXY + sectionV.z + 1)];
    const double xyz = dot(Gxyz, localV + Vec3D(-1, -1, -1));
    const double Xyz = dot(GXyz, localV + Vec3D( 0, -1, -1));
    const double xYz = dot(GxYz, localV + Vec3D(-1,  0, -1));
    const double XYz = dot(GXYz, localV + Vec3D( 0,  0, -1));
    const double xyZ = dot(GxyZ, localV + Vec3D(-1, -1,  0));
    const double XyZ = dot(GXyZ, localV + Vec3D( 0, -1,  0));
    const double xYZ = dot(GxYZ, localV + Vec3D(-1,  0,  0));
    const double XYZ = dot(GXYZ, localV);
    const Vec3D fadeV(perlinFade(localV.x), perlinFade(localV.y), perlinFade(localV.z));
    _6[0] += lerp3(Gxyz[0], GXyz[0], GxYz[0], GXYz[0], GxyZ[0], GXyZ[0], GxYZ[0], GXYZ[0], fadeV);
    _6[1] += lerp3(Gxyz[1], GXyz[1], GxYz[1], GXYz[1], GxyZ[1], GXyZ[1], GxYZ[1], GXYZ[1], fadeV);
    _6[2] += lerp3(Gxyz[2], GXyz[2], GxYz[2], GXYz[2], GxyZ[2], GXyZ[2], GxYZ[2], GXYZ[2], fadeV);
    _6[0] += lerp2(Xyz - xyz, XYz - xYz, XyZ - xyZ, XYZ - xYZ, fadeV.y, fadeV.z) * perlinFadeDerivative(localV.x);
    _6[1] += lerp2(xYz - xyz, xYZ - xyZ, XYz - Xyz, XYZ - XyZ, fadeV.z, fadeV.x) * perlinFadeDerivative(localV.y);
    _6[2] += lerp2(xyZ - xyz, XyZ - Xyz, xYZ - xYz, XYZ - XYz, fadeV.x, fadeV.y) * perlinFadeDerivative(localV.z);
    return lerp3(xyz, Xyz, xYz, XYz, xyZ, XyZ, xYZ, XYZ, fadeV);
}
// see PerlinNoiseSample constructor in PerlinNoiseSample.java
void PerlinNoise::init(Xoroshiro* rand) {
    for (int i = 0; i < 256; i++)
        permutation[i] = i;
    for (int i = 0; i < 256; ++i) {
        // swap values at index i and i+r in the array
        std::swap(permutation[i], permutation[i + rand->nextInt(256 - i)]);
    }
    /*std::cout << "permutation table:";
    for (size_t i = 0; i < 256; i++) {
        if ((i&15) == 0) std::cout << '\n';
        std::cout << (unsigned int)permutation[i] << " ";
    }
    std::cout << '\n';*/
}
// see PerlinNoiseSample.map function in PerlinNoiseSample.java
int PerlinNoise::map(const int& input) const {
    return permutation[input & 0xFF];
}
// see PerlinNoiseSample.grad function in PerlinNoiseSample.java
double PerlinNoise::grad(const int& perm, const Vec3D& position) const {
    return dot(PERLIN_GRADIENTS[perm & 0xF], position);
}
#pragma endregion Perlin

long lfloor(const double& value) {
    long long int tmp = (long long int)value;
    return (value < tmp) ? tmp - 1 : tmp;
}
double maintainPrecision(double value) {
    return value - (double)lfloor(value / 3.3554432E7 + 0.5) * 3.3554432E7;
}
#pragma region OctavePerlinNoise
OctavePerlinNoise::OctavePerlinNoise(Xoroshiro* rand, const int& _firstOctave, const std::vector<double>& _amplitudes)
    : firstOctave(_firstOctave), lacunarity(pow(2.0, firstOctave)), amplitudes(_amplitudes) {
    persistence = (pow(2.0, amplitudes.size() - 1.0) / (pow(2.0, amplitudes.size()) - 1.0));
    
    XoroshiroSplitter splitter = rand->nextSplitter();
    for (int i = 0; i < amplitudes.size(); ++i) {
        if (amplitudes[i] == 0.0) continue;
        Xoroshiro samplerRand = splitter.split("octave_" + std::to_string(firstOctave + i));
        samplers.push_back(new PerlinNoise(&samplerRand));
    }
    // see OctavePerlinNoiseSampler.getTotalAmplitude in OctavePerlinNoiseSampler.java
    double p = persistence;
    maxValue = 0;
    for (unsigned int i = 0; i < amplitudes.size(); ++i) {
        maxValue += amplitudes[i] * 2.0 * p;
        p /= 2.0;
    }
}
OctavePerlinNoise::~OctavePerlinNoise() {
    for (unsigned int i = 0; i < samplers.size(); i++)
        delete samplers[i];
}
double OctavePerlinNoise::sample(const double& x, const double& y, const double& z) const {
    double value = 0.0;
    double l = lacunarity;
    double p = persistence;
    int idx = 0;
    for (int i = 0; i < amplitudes.size(); ++i) {
        if (amplitudes[i] != 0) {
            double _11 = samplers[idx]->sample(maintainPrecision(x * l), maintainPrecision(y * l), maintainPrecision(z * l));
            value += amplitudes[i] * p * _11;
            idx++;
        }
        l *= 2.0;
        p /= 2.0;
    }
    return value;
}
double OctavePerlinNoise::sample(const Vec3D& v) const {
    return sample(v.x, v.y, v.z);
}
double OctavePerlinNoise::getMaxValue() const {
    return maxValue;
}
#pragma endregion OctavePerlinNoise

#pragma region DoublePerlinNoise
DoublePerlinNoise::DoublePerlinNoise(Xoroshiro* rand, const int& firstOctave, const std::vector<double>& amplitudes)
    : firstSampler(rand, firstOctave, amplitudes), secondSampler(rand, firstOctave, amplitudes) {
    int bottomAmplitudeIdx = 100000000;
    int TopAmplitudeIdx = -1;
    for (size_t i = 0; i < amplitudes.size(); i++) {
        if (amplitudes[i] == 0) continue;
        bottomAmplitudeIdx = myMin(bottomAmplitudeIdx, i);
        TopAmplitudeIdx = myMax(TopAmplitudeIdx, i);
    }
    amplitude = 1.6666666666666666 / (1.0 + 1.0 / (double)(TopAmplitudeIdx - bottomAmplitudeIdx + 1));
    maxValue = (firstSampler.getMaxValue() + secondSampler.getMaxValue()) * amplitude;
}
double DoublePerlinNoise::sample(const double& x, const double& y, const double& z) const {
    const double sample1 = firstSampler.sample(x, y, z);
    const double sample2 = secondSampler.sample(x * 1.0181268882175227, y * 1.0181268882175227, z * 1.0181268882175227);
    const double value = (sample1 + sample2) * amplitude;
    return value;
}
double DoublePerlinNoise::sample(const Vec3D& v) const {
    return sample(v.x, v.y, v.z);
}
double DoublePerlinNoise::getMaxValue() const {
    return maxValue;
}
#pragma endregion DoublePerlinNoise

#pragma region Simplex
// see SimplexNoiseSampler.skew_2d constant in SimplexNoiseSampler.java
const double skew_2d = 0.5 * (sqrt(3.0) - 1.0);// formula is (sqrt(n+1)-1)/n so n=2 -> (sqrt(3)-1)/2 and n=3 = (sqrt(4)-1)/3 or 1/3 or 0.33333333
// see SimplexNoiseSampler.un_skew_2d constant in SimplexNoiseSampler.java
const double un_skew_2d = (3.0 - sqrt(3.0)) / 6.0;// formula is (1-1/sqrt(n+1))/n so n=2 -> (1-1/sqrt(3))/2 or (3-sqrt(3))/6 and n=3 = (1-1/sqrt4))/3 = (1-0.5)/3 = 0.5/3 or 1/6 or 0.1666666

// see SimplexNoiseSampler.sample(double*2) function in SimplexNoiseSampler.java, but thats broken due to decompilation artifacts :(
double SimplexNoise::sample(const double& x, const double& z) const {
    return sample(Vec2D(x, z));
}
// https://en.wikipedia.org/wiki/Simplex_noise helped a lot
double SimplexNoise::sample(const Vec2D& V) const {
    double skew = (V.x + V.z)*skew_2d;
    const Vec2D Vp(V.x+skew, V.z+skew);
    const Vec2 sectionV(floor(Vp.x), floor(Vp.z));
    double unskew = (sectionV.x + sectionV.z)*un_skew_2d;
    const Vec2D unsquewed(sectionV.x - unskew, sectionV.z - unskew);

    const Vec2D internalV1 = V - unsquewed;
    Vec2 vrt2(0, 0);// (1,0) or (0,1)
    if (internalV1.x > internalV1.z) vrt2.x = 1;
    else vrt2.z = 1;
    const Vec2D internalV2 = internalV1 - vrt2 + Vec2D(un_skew_2d, un_skew_2d);
    const Vec2D internalV3 = internalV1 + Vec2D(2*un_skew_2d - 1, 2*un_skew_2d - 1);

    const Vec2 modSectionV(sectionV.x & 0xFF, sectionV.z & 0xFF);
    return 70.0 * (
        grad(map(modSectionV.x          + map(modSectionV.z         )) % 12, Vec3D(internalV1.x, internalV1.z, 0), 0.5) + 
        grad(map(modSectionV.x + vrt2.x + map(modSectionV.z + vrt2.z)) % 12, Vec3D(internalV2.x, internalV2.z, 0), 0.5) + 
        grad(map(modSectionV.x +      1 + map(modSectionV.z +      1)) % 12, Vec3D(internalV3.x, internalV3.z, 0), 0.5)
    );
}
// see SimplexNoiseSampler.sample(double*3) function in SimplexNoiseSampler.java, but thats broken due to decompilation artifacts :(
double SimplexNoise::sample(const double& x, const double& y, const double& z) const {
    return sample(Vec3D(x, y, z));
}
// https://en.wikipedia.org/wiki/Simplex_noise helped a lot
double SimplexNoise::sample(const Vec3D& V) const {
    double skew = (V.x + V.z)*0.3333333333;
    const Vec3D Vp(V.x+skew, V.y+skew, V.z+skew);
    const Vec3 sectionV(floor(Vp.x), floor(Vp.y), floor(Vp.z));
    double unskew = (sectionV.x + sectionV.z)*0.1666666666;
    const Vec3D unsquewed(sectionV.x - unskew, sectionV.y - unskew, sectionV.z - unskew);

    const Vec3D internalV1 = V - unsquewed;
    Vec3 vrt2(0, 0, 0);// (1, 0, 0) or (0, 1, 0) or (0, 0, 1)
    Vec3 vrt3(0, 0, 0);
    if (internalV1.x > internalV1.y) {
        if (internalV1.y > internalV1.z) {// x > y > z
            vrt2.x = 1;
            vrt3.x = 1;
            vrt3.y = 1;
        } else if (internalV1.x > internalV1.z) {// x > z > y
            vrt2.x = 1;
            vrt3.x = 1;
            vrt3.z = 1;
        } else {// z > x > y
            vrt2.z = 1;
            vrt3.x = 1;
            vrt3.z = 1;
        }
    } else if (internalV1.y < internalV1.z) {// z > y > x
        vrt2.z = 1;
        vrt3.y = 1;
        vrt3.z = 1;
    } else if (internalV1.x < internalV1.z) {// y > z > x
        vrt2.y = 1;
        vrt3.y = 1;
        vrt3.z = 1;
    } else {// y > x > z
        vrt2.y = 1;
        vrt3.x = 1;
        vrt3.y = 1;
    }
    const Vec3D internalV2 = internalV1 - vrt2 + Vec3D(0.1666666666, 0.1666666666, 0.1666666666);
    const Vec3D internalV3 = internalV1 - vrt3 + Vec3D(0.3333333333, 0.3333333333, 0.3333333333);
    const Vec3D internalV4 = internalV1 - Vec3D(0.5, 0.5, 0.5);

    const Vec3 modSectionV(sectionV.x & 0xFF, sectionV.y & 0xFF, sectionV.z & 0xFF);
    return 32.0 * (
        grad(map(modSectionV.x          + map(modSectionV.y          + map(modSectionV.z         ))) % 12, internalV1, 0.6) + 
        grad(map(modSectionV.x + vrt2.x + map(modSectionV.y + vrt2.y + map(modSectionV.z + vrt2.z))) % 12, internalV2, 0.6) + 
        grad(map(modSectionV.x + vrt3.x + map(modSectionV.y + vrt3.y + map(modSectionV.z + vrt3.z))) % 12, internalV3, 0.6) + 
        grad(map(modSectionV.x +      1 + map(modSectionV.y +      1 + map(modSectionV.z +      1))) % 12, internalV4, 0.6)
    );
}
// see SimplexNoiseSampler constructor in SimplexNoiseSampler.java
void SimplexNoise::init(Xoroshiro* rand) {
    for (int i = 0; i < 256; i++)
        permutation[i] = i;
    for (int i = 0; i < 256; ++i) {
        int r = rand->nextInt(256 - i);
        // swap values at index i and i+r in the array
        unsigned char perm = permutation[i];
        permutation[i] = permutation[i + r];
        permutation[i + r] = perm;
    }
}
// see SimplexNoiseSampler.map function in SimplexNoiseSampler.java
int SimplexNoise::map(const int& input) const {
    return permutation[input & 0xFF];
}
// see SimplexNoiseSampler.grad function in SimplexNoiseSampler.java
// r_2 of 0.5 is default, 0.6 may give better results ( see en.wikipedia.org/wiki/Simplex_noise )
double SimplexNoise::grad(const int& hash, const Vec3D& V, double r_2) const {
    double thing = myMax(0.0, r_2 - V.magnitude());
    thing *= thing;
    return thing * thing * dot(PERLIN_GRADIENTS[hash], V);// thing^4 * gradient[hash].dot(V)
}
#pragma endregion Simplex

#pragma endregion Noise