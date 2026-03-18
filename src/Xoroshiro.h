#ifndef __XOROSHIRO
#define __XOROSHIRO

#include "Random.h"

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

#endif// __XOROSHIRO