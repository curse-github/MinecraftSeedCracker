#include "Lib.h"

Vec2::Vec2() : x(0), z(0) {}
Vec2::Vec2(const int& _x, const int& _z) : x(_x), z(_z) {}
Vec2::Vec2(const Vec2& copy) : x(copy.x), z(copy.z) {}
Vec2 Vec2::operator+(const Vec2& rhs) const {
    return Vec2(x + rhs.x, z + rhs.z);
}
Vec2 Vec2::operator-(const Vec2& rhs) const {
    return Vec2(x - rhs.x, z - rhs.z);
}
int Vec2::magnitude() const {
    return x*x + z*z;
}

Vec2D::Vec2D() : x(0), z(0) {}
Vec2D::Vec2D(const double& _x, const double& _z) : x(_x), z(_z) {}
Vec2D::Vec2D(const Vec2D& copy) : x(copy.x), z(copy.z) {}
Vec2D Vec2D::operator+(const Vec2D& rhs) const {
    return Vec2D(x + rhs.x, z + rhs.z);
}
Vec2D Vec2D::operator+(const Vec2& rhs) const {
    return Vec2D(x + rhs.x, z + rhs.z);
}
Vec2D Vec2D::operator-(const Vec2D& rhs) const {
    return Vec2D(x - rhs.x, z - rhs.z);
}
Vec2D Vec2D::operator-(const Vec2& rhs) const {
    return Vec2D(x - rhs.x, z - rhs.z);
}
double Vec2D::magnitude() const {
    return x*x + z*z;
}

Vec3::Vec3() : x(0), y(0), z(0) {}
Vec3::Vec3(const int& _x, const int& _y, const int& _z) : x(_x), y(_y), z(_z) {}
Vec3::Vec3(const Vec3& copy) : x(copy.x), y(copy.y), z(copy.z) {}
Vec3 Vec3::operator+(const Vec3& rhs) const {
    return Vec3(x + rhs.x, y + rhs.y, z + rhs.z);
}
Vec3 Vec3::operator-(const Vec3& rhs) const {
    return Vec3(x - rhs.x, y - rhs.y, z - rhs.z);
}
int Vec3::magnitude() const {
    return x*x + y*y + z*z;
}

Vec3D::Vec3D() : x(0), y(0), z(0) {}
Vec3D::Vec3D(const double& _x, const double& _y, const double& _z) : x(_x), y(_y), z(_z) {}
Vec3D::Vec3D(const Vec3D& copy) : x(copy.x), y(copy.y), z(copy.z) {}
Vec3D Vec3D::operator+(const Vec3D& rhs) const {
    return Vec3D(x + rhs.x, y + rhs.y, z + rhs.z);
}
Vec3D Vec3D::operator+(const Vec3& rhs) const {
    return Vec3D(x + rhs.x, y + rhs.y, z + rhs.z);
}
Vec3D Vec3D::operator-(const Vec3D& rhs) const {
    return Vec3D(x - rhs.x, y - rhs.y, z - rhs.z);
}
Vec3D Vec3D::operator-(const Vec3& rhs) const {
    return Vec3D(x - rhs.x, y - rhs.y, z - rhs.z);
}
double Vec3D::magnitude() const {
    return x*x + y*y + z*z;
}

// adapted mostly from pseudo-code algorithm at https://en.wikipedia.org/wiki/MD5
// with some slight help from chat-gpt, because I forgot about the little endian parts
// note: this only works on ascii text (which is the default in c++), and wouldnt working on anything like a wstring, or buffer of some kind
// from my tests this took roughly 0.025 milliseconds on average even on strings with a length of 500 characters
std::vector<unsigned int> md5Raw(const unsigned char* rawBytes, const unsigned int& rawSize) {
    // create S list used for leftRotate during the runningHash
    unsigned int S[64] = {
        7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22,
        5,  9, 14, 20, 5,  9, 14, 20, 5,  9, 14, 20, 5,  9, 14, 20,
        4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23,
        6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21
    };
    // create the K list used in runningHashing
    unsigned int K[64];
    for (size_t i = 0; i < 64; i++)
        K[i] = (((unsigned long long int) (std::abs(std::sin(i + 1)) * maxWord)) << 32) >> 32;

    unsigned int newStrLen = (rawSize + 1u) + (120u - ((rawSize + 1u) % 64u)) % 64u;
    // translate the ascii into unsigned integers using bit magic
    std::vector<unsigned long long int> chunks((newStrLen >> 2u) + 2u, 0u);// length of newStrLen/8
    // combine the bits of each character end to end into unsigned ints
    // extra login equivilent to pad string with a 1 bit and then 0 bits until (bit length === 448 (mod 512))
    for(unsigned int i = 0u; i < rawSize; i++)
        chunks[i >> 2] |= ((unsigned int)rawBytes[i]) << ((i % 4u) << 3u);
    chunks[rawSize >> 2] |= 0x80u << ((rawSize % 4u) << 3u);
    // push the original length of the string to the end of the chunks
    chunks[(newStrLen >> 2u)] = rawSize << 3u;
    // loop through the "chunks" array, to do actual iterative runningHashing algorithm
    std::vector<unsigned int> runningHash = { 0x67452301u, 0xefcdab89u, 0x98badcfeu, 0x10325476u };// initialize values in the running runningHash
    for (size_t j = 0; j < chunks.size(); j += 16) {
        // slice of chunks array from j to j+16
        unsigned int chunksSlice[16];
        for (size_t i = 0; i < 16; i++) chunksSlice[i] = chunks[j+i];
        // save hash to add back to itself after hashing
        unsigned int hash[4];
        for (size_t i = 0; i < 4; i++) hash[i] = runningHash[i];
        // inner hashing loop
        for (size_t i = 0; i < 64; i++) {
            unsigned int B = runningHash[1];
            unsigned int C = runningHash[2];
            unsigned int D = runningHash[3];
            
            unsigned int F = runningHash[0] + K[i];
            unsigned int G = 0;
            if (i < 16) {
                F += ((B & C) | ((~B) & D)) + chunksSlice[i];
            } else if (i < 32) {
                F += ((D & B) | ((~D) & C)) + chunksSlice[(i * 5 + 1) % 16];
            } else if (i < 48) {
                F += (B ^ C ^ D) + chunksSlice[(i * 3 + 5) % 16];
            } else {
                F += (C ^ (B | (~D))) + chunksSlice[(i * 7) % 16];
            }
            runningHash[0] = runningHash[3];
            runningHash[3] = runningHash[2];
            runningHash[2] = runningHash[1];
            runningHash[1] += ((F << S[i]) | (F >> (32 - S[i])));// leftRotate(temp, S[i])
        }
        // add old hash to the running hash
        for (size_t i = 0u; i < 4u; i++) runningHash[i] += hash[i];
    }
    for (unsigned int i = 0u; i < 4u; i++)
        runningHash[i] = (runningHash[i] << 24) | (((runningHash[i] >> 8) & 0xff) << 16) | (((runningHash[i] >> 16) & 0xff) << 8) | (runningHash[i] >> 24);
    return runningHash;
}
std::vector<unsigned int> md5Vector(const std::vector<unsigned char>& vec) {
    return md5Raw(vec.data(), vec.size());
}