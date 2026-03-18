#include "Random.h"


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

#pragma endregion rand helpers

#pragma region BitRandomSource

long long int BitRandomSource::getCarverSeed(const long long int& worldSeed, const int& chunkX, const int& chunkZ) {
    LCG rand(worldSeed);
    long long int xScale = rand.nextLong();
    long long int zScale = rand.nextLong();
    return (chunkX * xScale) ^ (chunkZ * zScale) ^ worldSeed;
}
long long int BitRandomSource::getCarverSeed(const long long int& worldSeed, const SectionPos& chunk) {
    return getCarverSeed(worldSeed, chunk.x, chunk.z);
}
Direction getRandomHorizontalDirection(BitRandomSource& rand) {
    return HorizontalDirections[rand.nextInt(4)];
}

#pragma endregion// BitRandomSource

#pragma region LCG


// new_seed = ((a * seed) + b) % m
long long int LCG::nextSeed() {
    seed *= a;
    seed += b;
    seed &= m-1;
    count++;
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
    count += steps;
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
    count--;
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
    count -= steps;
    return seed;
}

void LCG::setSeed(const long long int& _seed) {
    count = 0;
    seed = (_seed ^ a) & (m - 1);
}
void LCG::setCarverSeed(const long long int& worldSeed, const int& chunkX, const int& chunkZ) {
    setSeed(worldSeed);
    long long int xScale = nextLong();
    long long int zScale = nextLong();
    setSeed((chunkX * xScale) ^ (chunkZ * zScale) ^ worldSeed);
}
void LCG::setCarverSeed(const long long int& worldSeed, const SectionPos& chunk) {
    setCarverSeed(worldSeed, chunk.x, chunk.z);
}

// see LegacyRandomSource.next function in net.minecraft.world.level.levelgen.LegacyRandomSource
int LCG::next(const int& bits) {
    return nextSeed() >> (48 - bits);
}
// see BitRandomSource.nextInt() function in net.minecraft.world.level.levelgen.BitRandomSource
int LCG::nextInt() {
    return next(32);
}
// see BitRandomSource.nextInt(int) function in net.minecraft.world.level.levelgen.BitRandomSource
int LCG::nextInt(const int& bound) {
    int modulo;
    int sample;
    if ((bound & (bound - 1)) == 0)
        return (int)((bound * (long long int)next(31)) >> 31);
    do {
        sample = next(31);
        modulo = sample % bound;
    } while ((sample - modulo + (bound - 1)) < 0);
    return modulo;
}
// see BitRandomSource.nextLong function in net.minecraft.world.level.levelgen.BitRandomSource
long long int LCG::nextLong() {
    int upper = next(32);
    int lower = next(32);
    return ((long long int)upper << 32ull) + lower;
}
// see BitRandomSource.nextBoolean function in net.minecraft.world.level.levelgen.BitRandomSource
bool LCG::nextBoolean() {
    return next(1) != 0;
}
// see BitRandomSource.nextFloat function in net.minecraft.world.level.levelgen.BitRandomSource
float LCG::nextFloat() {
    return next(24) * 5.9604645E-8f;
}
// see BitRandomSource.nextDouble function in net.minecraft.world.level.levelgen.BitRandomSource
double LCG::nextDouble() {
    unsigned long long int upper = (unsigned long long int)next(26);
    unsigned long long int lower = (unsigned long long int)next(27);
    return ((upper << 27ll) | lower) * 1.1102230246251565E-16;
}

LCG LCG::split() {
    return LCG(nextLong(), a, b, m);
}
LCGSplitter LCG::nextSplitter() {
    return LCGSplitter(nextLong());
}

#pragma region splitter

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
LCG LCGSplitter::split(const std::string& _seed) {
    return LCG(seed ^ hashString(_seed));
}
LCG LCGSplitter::split(const long long int& _seed) {
    return LCG(_seed);
}

#pragma endregion// splitter

#pragma endregion// LCG

#pragma region tests

// used https://onecompiler.com/java to get values and the following code
/*
import java.util.Scanner;
import java.util.Random;
public class RandomNumbers {
  public static void main(String[] args) {
    int num = 50;
    long world_seed = 8606738414634885904L;
    Random random = new Random();
    random.setSeed(world_seed);
    long carver_seed = ((long)15 * random.nextLong()) ^ ((long)15 * random.nextLong()) ^ world_seed;
    random.setSeed(carver_seed);
    long carver_carver_seed = ((long)15 * random.nextLong()) ^ ((long)15 * random.nextLong()) ^ carver_seed;
    System.out.println("world_seed: " + world_seed);
    System.out.println("carver_seed: " + carver_seed);
    System.out.println("carver_carver_seed: " + carver_carver_seed);
    random.setSeed(world_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextInt(5) + ",");
    System.out.println("");
    random.setSeed(world_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextInt(4) + ",");
    System.out.println("");
    random.setSeed(world_seed);
    for (int i = 0; i < num; i++)
        System.out.print((random.nextInt(2) == 0) + ",");
    System.out.println("");
    random.setSeed(world_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextBoolean() + ",");
    System.out.println("");
    random.setSeed(world_seed);
    for (int i = 0; i < num; i++)
        System.out.print((random.nextInt(3) > 0) + ",");
    System.out.println("");
    random.setSeed(world_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextInt(145) + ",");
    System.out.println("");

    random.setSeed(carver_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextInt(5) + ",");
    System.out.println("");
    random.setSeed(carver_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextInt(4) + ",");
    System.out.println("");
    random.setSeed(carver_seed);
    for (int i = 0; i < num; i++)
        System.out.print((random.nextInt(2) == 0) + ",");
    System.out.println("");
    random.setSeed(carver_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextBoolean() + ",");
    System.out.println("");
    random.setSeed(carver_seed);
    for (int i = 0; i < num; i++)
        System.out.print((random.nextInt(3) > 0) + ",");
    System.out.println("");
    random.setSeed(carver_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextInt(145) + ",");
    System.out.println("");

    random.setSeed(carver_carver_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextInt(5) + ",");
    System.out.println("");
    random.setSeed(carver_carver_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextInt(4) + ",");
    System.out.println("");
    random.setSeed(carver_carver_seed);
    for (int i = 0; i < num; i++)
        System.out.print((random.nextInt(2) == 0) + ",");
    System.out.println("");
    random.setSeed(carver_carver_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextBoolean() + ",");
    System.out.println("");
    random.setSeed(carver_carver_seed);
    for (int i = 0; i < num; i++)
        System.out.print((random.nextInt(3) > 0) + ",");
    System.out.println("");
    random.setSeed(carver_carver_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextInt(145) + ",");
    System.out.println("");
    
    random.setSeed(world_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextDouble() + ",");
    System.out.println("");
    random.setSeed(carver_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextDouble() + ",");
    System.out.println("");
    random.setSeed(carver_carver_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextDouble() + ",");
    System.out.println("");
    
    random.setSeed(world_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextLong() + ",");
    System.out.println("");
    random.setSeed(carver_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextLong() + ",");
    System.out.println("");
    random.setSeed(carver_carver_seed);
    for (int i = 0; i < num; i++)
        System.out.print(random.nextLong() + ",");
    System.out.println("");
  }
}
*/
const int testValues[900] = {
    1,2,4,0,1,0,4,3,2,4,1,3,4,0,1,4,4,2,2,4,1,2,0,2,3,1,3,1,0,4,2,2,2,2,1,1,3,0,3,1,2,3,0,4,0,2,0,2,4,2,
    0,2,1,2,1,1,3,1,0,3,3,2,3,3,1,1,1,0,3,0,2,1,1,3,3,1,1,3,3,1,2,0,1,1,2,3,0,0,0,2,0,3,1,0,1,1,0,0,2,2,
    true,false,true,false,true,true,false,true,true,false,false,false,false,false,true,true,true,true,false,true,false,true,true,false,false,true,true,false,false,true,false,true,true,true,false,false,true,true,true,false,true,false,true,true,true,true,true,true,false,false,
    false,true,false,true,false,false,true,false,false,true,true,true,true,true,false,false,false,false,true,false,true,false,false,true,true,false,false,true,true,false,true,false,false,false,true,true,false,false,false,true,false,true,false,false,false,false,false,false,true,true,
    true,true,true,false,true,true,false,true,false,true,false,true,false,true,true,true,true,true,true,true,false,true,true,false,false,true,true,true,false,true,true,false,true,true,true,true,true,true,true,false,true,true,true,true,false,false,true,true,true,true,
    61,107,114,130,26,55,104,118,47,109,126,68,19,30,66,129,34,37,77,4,111,82,115,7,143,126,98,6,95,9,142,87,42,137,41,1,93,5,23,81,12,83,80,114,140,127,15,62,134,57,
    0,4,0,1,2,3,1,2,4,2,1,4,2,4,0,3,1,1,4,3,1,1,0,1,3,0,3,1,3,2,2,2,0,2,4,3,1,0,4,4,4,1,2,0,4,3,4,2,2,1,
    3,2,1,1,1,0,3,3,0,1,1,1,2,0,3,2,2,1,3,3,1,2,0,1,0,0,3,1,3,2,3,3,1,2,0,1,1,1,3,3,1,2,1,2,1,2,2,0,3,1,
    false,false,true,true,true,true,false,false,true,true,true,true,false,true,false,false,false,true,false,false,true,false,true,true,true,true,false,true,false,false,false,false,true,false,true,true,true,true,false,false,true,false,true,false,true,false,false,true,false,true,
    true,true,false,false,false,false,true,true,false,false,false,false,true,false,true,true,true,false,true,true,false,true,false,false,false,false,true,false,true,true,true,true,false,true,false,false,false,false,true,true,false,true,false,true,false,true,true,false,true,false,
    false,true,false,true,true,false,true,false,true,true,true,true,true,true,true,false,false,false,false,false,true,false,true,false,true,true,true,true,true,true,true,true,true,true,false,false,true,true,true,true,true,false,true,true,true,true,true,true,true,false,
    70,134,5,1,137,103,36,67,14,72,26,59,62,19,15,43,66,1,64,48,96,11,140,51,143,15,123,121,118,87,87,2,135,22,49,98,116,65,24,79,59,136,12,30,39,38,94,142,122,36,
    0,3,3,2,2,2,3,0,2,4,0,0,3,2,1,2,3,1,0,0,2,3,0,2,3,4,1,0,4,2,3,1,4,4,3,0,1,3,3,3,4,2,1,1,0,1,4,4,2,2,
    0,3,1,1,1,0,2,0,3,2,2,2,2,0,1,0,2,1,1,3,3,3,2,0,1,2,2,1,2,2,1,3,3,0,3,1,0,1,2,2,1,2,1,0,0,3,2,1,1,0,
    true,false,true,true,true,true,false,true,false,false,false,false,false,true,true,true,false,true,true,false,false,false,false,true,true,false,false,true,false,false,true,false,false,true,false,true,true,true,false,false,true,false,true,true,true,false,false,true,true,true,
    false,true,false,false,false,false,true,false,true,true,true,true,true,false,false,false,true,false,false,true,true,true,true,false,false,true,true,false,true,true,false,true,true,false,true,false,false,false,true,true,false,true,false,false,false,true,true,false,false,false,
    true,true,true,true,true,false,true,true,true,true,true,false,false,true,true,true,true,true,true,true,false,false,true,true,false,true,false,true,true,false,false,false,true,true,false,true,false,false,true,true,false,true,true,true,false,true,true,true,false,true,
    65,43,23,107,122,72,123,70,37,119,105,45,63,17,96,52,143,96,55,120,37,58,75,37,108,9,11,85,29,47,58,1,74,89,63,90,141,8,38,38,24,57,76,41,135,121,34,39,122,72
};
const double testValues2[150] = {
    0.21648381271159334,0.4578800426895253,0.27989137739208236,0.8274819471874967,0.02337906897965436,0.8130527520544811,0.8259982166192706,0.2860039839242797,0.4544498059116442,0.8607397539052419,0.6327249763219895,0.3984689682454855,0.9733862337136455,0.3778862944107677,0.9574980064169152,0.5094214542631903,0.3678694724061542,0.5208161756158796,0.15443468413907369,0.18226447278113111,0.07973927046909612,0.40928389140334853,0.3607318173222258,0.11153620730276659,0.6100983399200082,0.4172767573286176,0.6715801599752946,0.9578943669855986,0.9400580291666438,0.14764800605596862,0.7252668474976796,0.3333776444240978,0.44005607377205946,0.4514516922498514,0.417978383259504,0.43892802769431094,0.05415400595919695,0.26788853452934935,0.5469187600220254,0.6618213860900939,0.6404480059493185,0.5070611698840551,0.540708933375955,0.7775686492783243,0.2247042259055787,0.675276560386554,0.0776350608220111,0.2690127123383166,0.5815553324386502,0.4514218770738275,
    0.7511790796230312,0.4098862181030156,0.3404879736602128,0.7846782770692885,0.16357361187009323,0.4353798831778861,0.6147201691162139,0.7957866001088524,0.7275589177798784,0.7849444596751881,0.32940366229077267,0.023405975328188555,0.13469991136736947,0.881064912604999,0.8399010091807934,0.9215629829653318,0.4264974377344004,0.1688918372851289,0.457970116689255,0.774984968153102,0.3585905668585654,0.40284622511450296,0.4413523163860624,0.638758766349437,0.7786164780752133,0.3306481729145143,0.11763865660510431,0.12965202697864708,0.684994786975144,0.04091311944228471,0.4156384418713258,0.21072921140950762,0.4161294571836053,0.8742483532742267,0.12532539830076173,0.6353083988465895,0.9829989693538562,0.46282204563306684,0.536538456364238,0.771969387787956,0.9028303143990107,0.1596654148692792,0.7169655712170044,0.11923686049848659,0.017530732074018007,0.8465084041095748,0.5815410652357746,0.93672736998078,0.6877764176980472,0.8677377380393656,
    0.06936593092736709,0.3161417136685938,0.3151577130304273,0.6683531867448551,0.8453307227942559,0.6548602821570302,0.6842927491964281,0.31492738700431344,0.6069760372792659,0.4370164093931297,0.8383995139571817,0.5462581828004222,0.36623779939778023,0.5240750515950545,0.654774289145794,0.38478353489825334,0.929920837344097,0.9136826016892498,0.04167591615708499,0.6473051416073519,0.4104729851851917,0.3900000021992245,0.013475147423727574,0.6557640189211197,0.38025742862322875,0.09732695683288928,0.3368690706118216,0.8361052046598272,0.783489979072712,0.29009972582352317,0.21242440925849027,0.46856778223544737,0.3967648538988452,0.07191505660842246,0.7105963351807526,0.11543186826056884,0.7121013299027736,0.1520527715663731,0.8425024500470536,0.6711991357527287,0.6057005381219611,0.5200409878033774,0.213114329183279,0.5553791922441149,0.051110197414060865,0.46712666334694164,0.17816152958846632,0.299499194677443,0.4372525151977634,0.5680859014309048
};
const long long int testValues3[150] = {
    3993421386266097420,8446396091387007308,5163084521500006212,-3182396289919908946,431267565062771753,-3448568117310212254,-3209766529390575008,5275842407096387067,8383119457941297358,-2568898069125957857,-6775028311821563503,7350454965637694624,-490937351838304717,6970771722608266188,-784023411361909988,-9049576841953427452,6785994058163129929,-8839381542754192757,2848817259642309947,3362186108455961503,1470929897726449201,7549955381383762125,6654327566831859864,2057479864545293441,-7192416222364596030,7697397653644896988,-6058276652260916522,-776711846695063606,-1105734286210520825,2723624970521537106,-5067932022391926520,6149732309845375280,8117601921063670975,8327813915681453216,7710340156710148664,8096792908062701948,998965025268508380,4941671328888099209,-8357873808086650398,-6238294297583317747,-6632563633528444412,-9093116244002545244,-8472424732283313546,-4103134322323725741,4145061445482470163,-5990090252982262563,1432114121687668697,4962408673508730965,-7718941875875497065,8327263980069097716,
    -4589935837202980118,7561066201763648723,6280894568551754616,-3971984871524159835,3017400494401384848,8031341381972090527,-7107158343953755303,-3767072458830508680,-5025650851501546115,-3967074709380734918,6076425033111228448,431764091862982748,2484774843009156877,-2193965066575245041,-2953305156101582542,-1446907618438593064,7867488968799327054,3115504548522610270,8448057662895493608,-4150794770841613435,6614828286402829954,7431201225841590714,8141513285486863374,-6663724467157189488,-4083805021293590879,6099382375024269196,2170050061616351846,2391657938607341628,-5810820778434464300,754713792011404686,7667175860762370851,3887267576920885342,7676233781346694395,-2319708495763201712,2311845763354550894,-6727372804038176801,-313613503392805336,8537559676516206262,-8549356405423481627,-4206422423496562370,-1792464306529796318,2945307038842474103,-5221063644851166697,2199531943983734027,323384836003412963,-2831420071207611232,-7719204871565482231,-1167173972713301907,-5759508305598447333,-2439808292566249712,
    1279575472323729037,5831785309522532696,5813633651739524245,-6117803810242137254,-2853144466140541760,-6366704064905785646,-5823770826205225585,5809384890054044572,-7250012421785923961,8061529794050959061,-2981002930490528716,-8370059088092764036,6755894889892008048,-8779265785458571434,-6368290290359188764,7098003342591624979,-1292732229196440113,-1592274851480480594,768784944199875334,-6506071909234252519,7571890113224650043,7194230221678762520,248572438436743553,-6350033111775735444,7014511571296437519,1795365334861431348,6214137464638876483,-3023325454433121136,-3993905028804792342,5351395591634444167,3918538700376285630,8643550024130910506,7319019772816556335,1326598436126229753,-5338555379468402842,2129342211008076932,-5310792966477223546,2804878475269451349,-2905316869139200972,-6065305422516629747,-7273541155862799527,-8853681168100902708,3931265370267765286,-8201806254326874922,942816859681111896,8616965955322948466,3286500139546673596,5524784936110102037,8065885336673777909,-7967408835833131072
};
void testRand() {
    long long int world_seed = 8606738414634885904ull;
    bool allCorrect = true;
    int num = 50;
    LCG rand(world_seed);
    long long int carver_seed = LCG::getCarverSeed(world_seed, 15, 15);
    long long int carver_carver_seed = LCG::getCarverSeed(carver_seed, 15, 15);
    // world_seed
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(5) != testValues[i]) { allCorrect = false; std::cout << "ERROR1\n"; } }
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(4) != testValues[num + i]) { allCorrect = false; std::cout << "ERROR2\n"; } }
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(2) == 0) != testValues[(2*num) + i]) { allCorrect = false; std::cout << "ERROR3\n"; } }
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextBoolean() != testValues[(3*num) + i]) { allCorrect = false; std::cout << "ERROR4\n"; } }
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(3) > 0) != testValues[(4*num) + i]) { allCorrect = false; std::cout << "ERROR5\n"; } }
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextDouble() != testValues2[i]) { allCorrect = false; std::cout << "ERROR6\n"; } }
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextLong() != testValues3[i]) { allCorrect = false; std::cout << "ERROR7\n"; } }
    rand.setSeed(world_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(145) != testValues[(5*num) + i]) { allCorrect = false; std::cout << "ERROR8\n"; } }
    // carver_seed
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(5) != testValues[(6*num) + i]) { allCorrect = false; std::cout << "ERROR9\n"; } }
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(4) != testValues[(7*num) + i]) { allCorrect = false; std::cout << "ERROR10\n"; } }
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(2) == 0) != testValues[(8*num) + i]) { allCorrect = false; std::cout << "ERROR11\n"; } }
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextBoolean() != testValues[(9*num) + i]) { allCorrect = false; std::cout << "ERROR12\n"; } }
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(3) > 0) != testValues[(10*num) + i]) { allCorrect = false; std::cout << "ERROR13\n"; } }
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextDouble() != testValues2[num + i]) { allCorrect = false; std::cout << "ERROR14\n"; } }
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextLong() != testValues3[num + i]) { allCorrect = false; std::cout << "ERROR15\n"; } }
    rand.setSeed(carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(145) != testValues[(11*num) + i]) { allCorrect = false; std::cout << "ERROR16\n"; } }
    // carver_carver_seed
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(5) != testValues[(12*num) + i]) { allCorrect = false; std::cout << "ERROR17\n"; } }
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(4) != testValues[(13*num) + i]) { allCorrect = false; std::cout << "ERROR18\n"; } }
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(2) == 0) != testValues[(14*num) + i]) { allCorrect = false; std::cout << "ERROR19\n"; } }
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextBoolean() != testValues[(15*num) + i]) { allCorrect = false; std::cout << "ERROR20\n"; } }
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if ((rand.nextInt(3) > 0) != testValues[(16*num) + i]) { allCorrect = false; std::cout << "ERROR21\n"; } }
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextDouble() != testValues2[(2*num) + i]) { allCorrect = false; std::cout << "ERROR22\n"; } }
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextLong() != testValues3[(2*num) + i]) { allCorrect = false; std::cout << "ERROR23\n"; } }
    rand.setSeed(carver_carver_seed);
    for (size_t i = 0; i < num; i++) { if (rand.nextInt(145) != testValues[(17*num) + i]) { allCorrect = false; std::cout << "ERROR4\n"; } }
    
    if (allCorrect) std::cout << "All random values were correct.\n";
    else std::cout << "Some random values were not correct.\n";
}

#pragma endregion// tests