#include "RandomSolver.h"

void printRanges(const std::vector<RandomRange>& ranges, const long long int& mod) {
    for (unsigned int i = 0; i < ranges.size(); i++) {
        if (ranges[i].mult == 1)
            std::cout << ranges[i].min << " < seed < " << ranges[i].max << '\n';
        else
            std::cout << ranges[i].min << " < " << ranges[i].mult << "*seed + " << mod << "*k_" << i << " < " << ranges[i].max << '\n';
    }
}
void JavaSolver::addFloatConstraint(const float& min, const float& max) {
    const unsigned long long int constant = (b*geom(a, ranges.size())) & (m-1);
    const unsigned long long int mult = fastExp(a,ranges.size()) & (m-1);
    ranges.push_back(RandomRange((((unsigned long long int)std::floor(m*min)) - constant) & (m-1), mult, (((unsigned long long int)std::floor(m*min)) - constant) & (m-1)));
}
void JavaSolver::print() const {
    printRanges(ranges, m);
}
void solve12Eye() {
    JavaSolver test;
    test.addFloatConstraint(0.9f,1.0f);
    test.addFloatConstraint(0.9f,1.0f);
    test.addFloatConstraint(0.9f,1.0f);
    test.addFloatConstraint(0.9f,1.0f);
    test.addFloatConstraint(0.9f,1.0f);
    test.addFloatConstraint(0.9f,1.0f);
    test.addFloatConstraint(0.9f,1.0f);
    test.addFloatConstraint(0.9f,1.0f);
    test.addFloatConstraint(0.9f,1.0f);
    test.addFloatConstraint(0.9f,1.0f);
    test.addFloatConstraint(0.9f,1.0f);
    test.addFloatConstraint(0.9f,1.0f);
    test.print();
}