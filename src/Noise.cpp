#include "Noise.h"

Noise::Noise(Xoroshiro* rand, const int& firstOctave, const std::vector<double>& amplitudes) : randPointer(rand), noise(rand, firstOctave, amplitudes) {}
double Noise::sample(const double& x, const double& y, const double& z) const {
    return noise.sample(x, y, z);
}
double Noise::sample(const Vec3D& pos) const {
    return noise.sample(pos);
}
double Noise::getMaxValue() const {
    return noise.getMaxValue();
}
double Noise::getMinValue() const {
    return -noise.getMaxValue();
}
Shift::Shift(Xoroshiro* rand) : noise(rand, -3, { 1.0, 1.0, 1.0, 0.0 }) {}
double Shift::sample(const double& x, const double& y, const double& z) const {
    return noise.sample(x * 0.25, 0.0, z * 0.25) * 4.0;
}
double Shift::sample(const Vec3D& pos) const {
    return noise.sample(pos.x * 0.25, 0.0, pos.z * 0.25) * 4.0;
}
double Shift::getMaxValue() const {
    return noise.getMaxValue();
}
double Shift::getMinValue() const {
    return noise.getMinValue();
}
ShiftA::ShiftA(Xoroshiro* rand) : Shift(rand) {}
double ShiftA::sample(const Vec3D& pos) const {
    return Shift::sample(pos.x, 0.0, pos.z);
}
ShiftB::ShiftB(Xoroshiro* rand) : Shift(rand) {}
double ShiftB::sample(const Vec3D& pos) const {
    return Shift::sample(pos.z, pos.x, 0.0);
}
ShiftX::ShiftX(Xoroshiro* rand) : mainNoise(rand), FlatCache(mainNoise) {}
ShiftZ::ShiftZ(Xoroshiro* rand) : mainNoise(rand), FlatCache(mainNoise) {}
ShiftedXZ::ShiftedXZ(Sampleable& _noise, Xoroshiro* randX, Xoroshiro* randZ, const double& _xzScale) : noise(_noise), shiftX(randX), shiftZ(randZ), xzScale(_xzScale) {}
double ShiftedXZ::sample(const Vec3D& pos) const {
    const Vec3D actualPos(pos.x * xzScale + shiftX.sample(pos), 0.0, pos.z * xzScale + shiftZ.sample(pos));
    const double value = noise.sample(actualPos);
    std::cout << "    ShiftedXZ: actually sampled at point (x,y,z) = (" << actualPos.x << ", " << actualPos.y << ", " << actualPos.z << ")\n";
    std::cout << "    ShiftedXZ: value = " << value << '\n';
    return value;
}
double ShiftedXZ::getMaxValue() const {
    return noise.getMaxValue();
}
double ShiftedXZ::getMinValue() const {
    return noise.getMinValue();
}
YClampedGradient::YClampedGradient(const double& _fromY, const double& _toY, const double& _fromValue, const double& _toValue) : fromY(_fromY), toY(_toY), fromValue(_fromValue), toValue(_toValue) {}
double YClampedGradient::sample(const Vec3D& pos) const {
    return fromValue + ((pos.y - fromY)/(toY - fromY)*(toValue - fromValue));
}
double YClampedGradient::getMaxValue() const {
    return std::max(fromValue, toValue);
}
double YClampedGradient::getMinValue() const {
    return std::min(fromValue, toValue);
}
Added::Added(const Sampleable& _noise1, const Sampleable& _noise2) : noise1(_noise1), noise2(_noise2) {}
double Added::sample(const Vec3D& pos) const {
    return noise1.sample(pos) + noise2.sample(pos);
}
double Added::getMaxValue() const {
    return noise1.getMaxValue() + noise2.getMaxValue();
}
double Added::getMinValue() const {
    return noise1.getMinValue() + noise2.getMinValue();
}
Multiplied::Multiplied(const Sampleable& _noise1, const Sampleable& _noise2) : noise1(_noise1), noise2(_noise2) {}
double Multiplied::sample(const Vec3D& pos) const {
    return noise1.sample(pos) * noise2.sample(pos);
}
double Multiplied::getMaxValue() const {
    return noise1.getMaxValue() * noise2.getMaxValue();
}
double Multiplied::getMinValue() const {
    return noise1.getMinValue() * noise2.getMinValue();
}

FlatCache::FlatCache(const Sampleable& _noise) : noise(_noise) {}
// see ChunkNoiseSampler.FlatCache.sample function in ChunkNoiseSampler.java
double FlatCache::sample(const Vec3D& pos) const {
    const Vec3D actualPos(pos.x, 0.0, pos.z);
    const double value = noise.sample(actualPos);
    std::cout << "FlatCache: actually sampled at point (x,y,z) = (" << actualPos.x << ", " << actualPos.y << ", " << actualPos.z << ")\n";
    std::cout << "FlatCache: value = " << value << '\n';
    return value;
}
double FlatCache::getMaxValue() const {
    return noise.getMaxValue();
}
double FlatCache::getMinValue() const {
    return noise.getMinValue();
}
/*
struct SplinePoint {
    double derivative;
    double location;
    const Sampleable& value;
    SplinePoint(const double& _derivative, const double& _location, const Sampleable& _value) : derivative(_derivative), location(_location), value(_value) {}
};
struct Spline : public Sampleable {
    std::vector<SplinePoint> points;
    Spline() {}
    double sample(const Vec3D& pos) {
        return 0.0;
    }
    double getMaxValue() {
        return 0.0;
    }
    double getMinValue() {
        return 0.0;
    }
};
*/
#pragma endregion base noise types

// see BuiltinNoiseParameters.java!!!!
// or a combination of the data/density_function/overworld/_.json and "data/noise_settings/overworld.json" and "data/noise/_.json" files
Temperature::Temperature(const long long int& worldSeed) : mainNoise(new Xoroshiro(worldSeed), -10, { 1.5, 0.0, 1.0, 0.0, 0.0, 0.0 }), ShiftedXZ(mainNoise, new Xoroshiro(worldSeed), new Xoroshiro(worldSeed), 0.25) {}
Vegetation::Vegetation(const long long int& worldSeed) : mainNoise(new Xoroshiro(worldSeed), -8, { 1.0, 1.0, 0.0, 0.0, 0.0, 0.0 }), ShiftedXZ(mainNoise, new Xoroshiro(worldSeed), new Xoroshiro(worldSeed), 0.25) {}
Continentalness::Continentalness(const long long int& worldSeed) : mainNoise(new Xoroshiro(worldSeed), -9.0, { 1.0, 1.0, 2.0, 2.0, 2.0, 1.0, 1.0, 1.0, 1.0 }), shiftedNoise(mainNoise, new Xoroshiro(worldSeed), new Xoroshiro(worldSeed), 0.25), FlatCache(shiftedNoise) {}
Erosion::Erosion(const long long int& worldSeed) : mainNoise(new Xoroshiro(worldSeed), -9, { 1.0, 1.0, 0.0, 1.0, 1.0}), shiftedNoise(mainNoise, new Xoroshiro(worldSeed), new Xoroshiro(worldSeed), 0.25), FlatCache(shiftedNoise) {}
/*
struct Offset : public Added {
    Constant argument1;
    Spline argument2;
    Offset() : argument1(-0.5037500262260437), argument2(), Added(argument1, argument2) {}
};
struct Depth : public Added {
    YClampedGradient argument1;
    Offset argument2;
    Constant p1;
    Constant p2;
    Constant p3;
    Constant p4;
    Constant p5;
    Depth()
        : argument1(-64, 320, 1.5, -1.5), argument2(), Added(argument1, argument2),
        p1(0.044), p2(-0.2222), p3(-0.2222), p4(-0.12), p5(-0.12) {}
};
*/
Ridges::Ridges(const long long int& worldSeed) : mainNoise(new Xoroshiro(worldSeed), -7.0, { 1.0, 2.0, 1.0, 0.0, 0.0, 0.0 }), shiftedNoise(mainNoise, new Xoroshiro(worldSeed), new Xoroshiro(worldSeed), 0.25), FlatCache(shiftedNoise) {}