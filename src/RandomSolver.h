#ifndef __RANDOM_SOLVER
#define __RANDOM_SOLVER

#include <vector>
#include <cmath>
#include "Random.h"

struct Range {
    unsigned long long int min;
    unsigned long long int mult;
    unsigned long long int max;
    Range(const unsigned long long int& _min, const unsigned long long int& _mult, const unsigned long long int& _max) : min(_min), mult(_mult), max(_max) {}
    Range(const Range& copy) : min(copy.min), mult(copy.mult), max(copy.max) {}
};
void printRanges(const std::vector<Range>& ranges, const long long int& mod);
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
    std::vector<Range> ranges;
    public:
    JavaSolver(const unsigned long long int& _a = 25214903917ull, const unsigned long long int& _b = 11ull, const unsigned long long int& _m = (1ull << 48ull))
        : a(_a), inv_a(modInverse(_a, _m)), b(_b), m(_m) {}
    void addFloatConstraint(const float& min, const float& max);
    void print() const;
};
void solve12Eye();

#endif// __RANDOM_SOLVER