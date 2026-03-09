#include "Lib.h"
unsigned int myMin(const unsigned int& a, const unsigned int& b) {
    return a ^ ((b < a)*(a ^ b));
}
unsigned int myMax(const unsigned int& a, const unsigned int& b) {
    return a ^ ((b > a)*(a ^ b));
}
int myMin(const int& a, const int& b) {
    return a ^ ((b < a)*(a ^ b));
}
int myMax(const int& a, const int& b) {
    return a ^ ((b > a)*(a ^ b));
}
int myMin(const double& a, const double& b) {
    return (a < b) ? a : b;
}
int myMax(const double& a, const double& b) {
    return (a > b) ? a : b;
}
int myAbs(const int& x) {
    int mask = x >> 31;
    return (x ^ mask) - mask;
}