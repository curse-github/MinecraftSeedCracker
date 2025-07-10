#include "Random.h"
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

const unsigned long long int rand_seed_neg_1 = 107038380838084ull;
const unsigned long long int rand_seed_neg_2 = 120323824315451ull;
const unsigned long long int rand_seed_neg_3 = 0ull;
const unsigned long long int rand_seed_neg_4 = 0ull;
const unsigned long long int rand_seed_neg_5 = 0ull;
const unsigned long long int rand_seed_neg_6 = 0ull;
const unsigned long long int rand_seed_neg_7 = 0ull;
const unsigned long long int rand_seed_neg_8 = 0ull;
const unsigned long long int rand_seed_neg_9 = 0ull;

void Random::setSeed(const long long int& _seed) {
    seed = (_seed ^ a) & (m - 1);
}
// see ChunkRandom.setCarverSeed function in ChunkRandom.java
void Random::setCarverSeed(const long long int& worldSeed, const int& chunkX, const int& chunkZ) {
    setSeed(worldSeed);
    setSeed(((long long int)chunkX * nextLong()) ^ ((long long int)chunkZ * nextLong()) ^ worldSeed);
}
void Random::setCarverSeed(const long long int& worldSeed, const ChunkPos& chunk) {
    setCarverSeed(worldSeed, chunk.x, chunk.z);
}
// see ChunkRandom.setCarverSeed function in ChunkRandom.java
long long int Random::getCarverSeed(const long long int& worldSeed, const int& chunkX, const int& chunkZ) {
    Random rand(worldSeed);
    return ((long long int)chunkX * rand.nextLong()) ^ ((long long int)chunkZ * rand.nextLong()) ^ worldSeed;
}
long long int Random::getCarverSeed(const long long int& worldSeed, const ChunkPos& chunk) {
    return getCarverSeed(worldSeed, chunk.x, chunk.z);
}

long long int Random::currentSeed() {
    return seed;
}
// new_seed = ((a * seed) + b) % m
long long int Random::nextSeed() {
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
long long int Random::nextSeed(const unsigned int& steps) {
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
long long int Random::previousSeed() {
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
long long int Random::previousSeed(const unsigned int& steps) {
    if (steps == 1) return previousSeed();
    seed -= b * geom(a, steps);
    seed *= fastExp(inv_a, steps);
    seed &= m-1;
    return seed;
}
// see CheckedRandom.next(int bits) in CheckedRandom.java
int Random::next(const unsigned int& bits) {
    return nextSeed() >> (48 - bits);
}
// see implementation of CheckedRandom.split() in CheckedRandom.java
Random Random::split() {
    return Random(nextLong(), a, b, m);
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
    return ((long long int)next(32) << 32ull) + ((long long int)next(32));
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
// see StructurePiece.getRandomHorizontalDirection function in StructurePiece.java
Direction getRandomHorizontalDirection(Random& rand) {
    return HorizontalDirections[rand.nextInt(4)];
}


// used https://onecompiler.com/java/43q7xtnkm to get values and the following code
/*
import java.util.Scanner;
import java.util.Random;

public class RandomNumbers {
    public static void main(String[] args) {
        int num = 30;
        long world_seed = 56871783007L;
        Random random = new Random();
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
    Random rand(world_seed);
    long long int carver_seed = Random::getCarverSeed(world_seed, 15, 15);
    long long int carver_carver_seed = Random::getCarverSeed(carver_seed, 15, 15);
    // world_seed
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(5) != testValues[i]) { allRight = false; std::cout << "ERROR\n"; } }
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(4) != testValues[num + i]) { allRight = false; std::cout << "ERROR\n"; } }
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(2) == 0) != testValues[(2*num) + i]) { allRight = false; std::cout << "ERROR\n"; } }
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextBoolean() != testValues[(3*num) + i]) { allRight = false; std::cout << "ERROR\n"; } }
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(3) > 0) != testValues[(4*num) + i]) { allRight = false; std::cout << "ERROR\n"; } }
    // carver_seed
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(5) != testValues[(5*num) + i]) { allRight = false; std::cout << "ERROR\n"; } }
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(4) != testValues[(6*num) + i]) { allRight = false; std::cout << "ERROR\n"; } }
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(2) == 0) != testValues[(7*num) + i]) { allRight = false; std::cout << "ERROR\n"; } }
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextBoolean() != testValues[(8*num) + i]) { allRight = false; std::cout << "ERROR\n"; } }
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(3) > 0) != testValues[(9*num) + i]) { allRight = false; std::cout << "ERROR\n"; } }
    // carver_carver_seed
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(5) != testValues[(10*num) + i]) { allRight = false; std::cout << "ERROR\n"; } }
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(4) != testValues[(11*num) + i]) { allRight = false; std::cout << "ERROR\n"; } }
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(2) == 0) != testValues[(12*num) + i]) { allRight = false; std::cout << "ERROR\n"; } }
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextBoolean() != testValues[(13*num) + i]) { allRight = false; std::cout << "ERROR\n"; } }
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(3) > 0) != testValues[(14*num) + i]) { allRight = false; std::cout << "ERROR\n"; } }
    
    if (allRight) std::cout << "All random values were correct.\n";
    else std::cout << "Some random values were not correct.\n";
}