#ifndef __NOISE
#define __NOISE

#include "Random.h"
#include "Lib.h"

struct Sampleable {
    virtual double sample(const Vec3D& pos) const = 0;
    virtual double getMaxValue() const = 0;
    virtual double getMinValue() const = 0;
};
struct Constant : public Sampleable {
    double value;
    Constant(const double& _value) : value(_value) {}
    double sample(const Vec3D& pos) {
        return value;
    }
    double getMaxValue() {
        return value;
    }
    double getMinValue() {
        return value;
    }
};
struct Noise : public Sampleable {
    DoublePerlinNoise noise;
    Xoroshiro* randPointer;
    Noise(Xoroshiro* rand, const int& firstOctave, const std::vector<double>& amplitudes);
    Noise(const Noise& _noise) = delete;
    Noise(Noise&& _noise) = delete;
    ~Noise() { delete randPointer; }
    double sample(const double& x, const double& y, const double& z) const;
    double sample(const Vec3D& pos) const;
    double getMaxValue() const;
    double getMinValue() const;
};
struct FlatCache : Sampleable {
    const Sampleable& noise;
    FlatCache(const Sampleable& _noise);
    double sample(const Vec3D& pos) const;
    double getMaxValue() const;
    double getMinValue() const;
};
struct Shift : public Sampleable {
    Noise noise;
    Shift(Xoroshiro* rand);
    double sample(const double& x, const double& y, const double& z) const;
    double sample(const Vec3D& pos) const;
    double getMaxValue() const;
    double getMinValue() const;
};
struct ShiftA : public Shift {
    ShiftA(Xoroshiro* rand);
    double sample(const Vec3D& pos) const;// technically a "NoisePos"
};
struct ShiftB : public Shift {
    ShiftB(Xoroshiro* rand);
    double sample(const Vec3D& pos) const;// technically a "NoisePos"
};
struct FlatCache;
struct ShiftX : public FlatCache {
    ShiftA mainNoise;
    ShiftX(Xoroshiro* rand);
};
struct ShiftZ : public FlatCache {
    ShiftB mainNoise;
    ShiftZ(Xoroshiro* rand);
};
struct ShiftedXZ : public Sampleable {
    Sampleable& noise;
    ShiftX shiftX;
    ShiftZ shiftZ;
    double xzScale;
    ShiftedXZ(Sampleable& _noise, Xoroshiro* randX, Xoroshiro* randZ, const double& _xzScale);
    double sample(const Vec3D& pos) const;
    double getMaxValue() const;
    double getMinValue() const;
};
struct YClampedGradient : public Sampleable {
    double fromY;
    double toY;
    double fromValue;
    double toValue;
    YClampedGradient(const double& _fromY, const double& _toY, const double& _fromValue, const double& _toValue);
    double sample(const Vec3D& pos) const;
    double getMaxValue() const;
    double getMinValue() const;
};
struct Added : public Sampleable {
    const Sampleable& noise1;
    const Sampleable& noise2;
    Added(const Sampleable& _noise1, const Sampleable& _noise2);
    double sample(const Vec3D& pos) const;
    double getMaxValue() const;
    double getMinValue() const;
};
struct Multiplied : public Sampleable {
    const Sampleable& noise1;
    const Sampleable& noise2;
    Multiplied(const Sampleable& _noise1, const Sampleable& _noise2);
    double sample(const Vec3D& pos) const;
    double getMaxValue() const;
    double getMinValue() const;
};

struct Temperature : public ShiftedXZ {
    Noise mainNoise;
    Temperature(const long long int& worldSeed);
};
struct Vegetation : public ShiftedXZ {
    Noise mainNoise;
    Vegetation(const long long int& worldSeed);
};
struct Continentalness : public FlatCache {
    Noise mainNoise;
    ShiftedXZ shiftedNoise;
    Continentalness(const long long int& worldSeed);
};
struct Erosion : public FlatCache {
    Noise mainNoise;
    ShiftedXZ shiftedNoise;
    Erosion(const long long int& worldSeed);
};
struct Ridges : public FlatCache {
    Noise mainNoise;
    ShiftedXZ shiftedNoise;
    Ridges(const long long int& worldSeed);
};

#endif// __NOISE