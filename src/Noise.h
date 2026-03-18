#ifndef __NOISE
#define __NOISE

#include <algorithm>
#include "MinecraftLib.h"
#include "Random.h"

class NoiseHolder;
class DensityFunction {
    public:
    static long long int world_seed;
    DensityFunction() {};
    DensityFunction(const DensityFunction& copy) = delete;
    DensityFunction(DensityFunction&& move) = delete;
    DensityFunction& operator=(const DensityFunction& copy) = delete;
    DensityFunction& operator=(DensityFunction&& move) = delete;
    virtual ~DensityFunction() {};

    class SinglePointContext {
        public:
        Pos block;
        SinglePointContext(const Pos& _block);
    };
    virtual double compute(const SinglePointContext& context) const = 0;
    

    static DensityFunction* zero();
    static DensityFunction* shift_x();
    static DensityFunction* shift_z();
    static DensityFunction* continents();
    static DensityFunction* erosion();
    static DensityFunction* depth();
    static DensityFunction* ridges();

    static NoiseHolder* noise_offset();
    static NoiseHolder* noise_temperature();
    static NoiseHolder* noise_vegetation();
    static NoiseHolder* noise_continentalness();
    static NoiseHolder* noise_erosion();
    static NoiseHolder* noise_ridges();
};
class Zero : public DensityFunction {
    public:
    Zero();
    Zero(const Zero& copy) = delete;
    Zero(Zero&& move) = delete;
    Zero& operator=(const Zero& copy) = delete;
    Zero& operator=(Zero&& move) = delete;
    virtual ~Zero() {};
    virtual double compute(const SinglePointContext& context) const;
};
class ShiftNoise : public DensityFunction {
    public:
    NoiseHolder* noise;
    ShiftNoise(NoiseHolder* _noise);
    ShiftNoise(const ShiftNoise& copy) = delete;
    ShiftNoise(ShiftNoise&& move) = delete;
    ShiftNoise& operator=(const ShiftNoise& copy) = delete;
    ShiftNoise& operator=(ShiftNoise&& move) = delete;
    virtual ~ShiftNoise();
    double compute(const Pos& block) const;
};
class ShiftA : public ShiftNoise {
    public:
    ShiftA(NoiseHolder* _noise);
    ShiftA(const ShiftA& copy) = delete;
    ShiftA(ShiftA&& move) = delete;
    ShiftA& operator=(const ShiftA& copy) = delete;
    ShiftA& operator=(ShiftA&& move) = delete;
    virtual ~ShiftA() {};
    virtual double compute(const DensityFunction::SinglePointContext& context) const;
};
class ShiftB : public ShiftNoise {
    public:
    ShiftB(NoiseHolder* _noise);
    ShiftB(const ShiftB& copy) = delete;
    ShiftB(ShiftB&& move) = delete;
    ShiftB& operator=(const ShiftB& copy) = delete;
    ShiftB& operator=(ShiftB&& move) = delete;
    virtual ~ShiftB() {};
    virtual double compute(const DensityFunction::SinglePointContext& context) const;
};
class ShiftedNoise : public DensityFunction {
    public:
    DensityFunction* shiftX;
    DensityFunction* shiftY;
    DensityFunction* shiftZ;
    double xzScale;
    double yScale;
    NoiseHolder* noise;
    ShiftedNoise(DensityFunction* _shiftX, DensityFunction* _shiftY, DensityFunction* _shiftZ, const double& _xzScale, const double& _yScale, NoiseHolder* _noise);
    ShiftedNoise(const ShiftedNoise& copy) = delete;
    ShiftedNoise(ShiftedNoise&& move) = delete;
    ShiftedNoise& operator=(const ShiftedNoise& copy) = delete;
    ShiftedNoise& operator=(ShiftedNoise&& move) = delete;
    virtual ~ShiftedNoise();
    virtual double compute(const DensityFunction::SinglePointContext& context) const;
};
class YClampedGradient : public DensityFunction {
    public:
    double fromValue;
    double fromY;
    double toValue;
    double toY;
    YClampedGradient(const double& _fromValue, const double& _fromY, const double& _toValue, const double& _toY);
    YClampedGradient(const YClampedGradient& copy) = delete;
    YClampedGradient(YClampedGradient&& move) = delete;
    YClampedGradient& operator=(const YClampedGradient& copy) = delete;
    YClampedGradient& operator=(YClampedGradient&& move) = delete;
    virtual ~YClampedGradient() {};
    virtual double compute(const DensityFunction::SinglePointContext& context) const;
};
class Add : public DensityFunction {
    public:
    DensityFunction* argument1;
    DensityFunction* argument2;
    Add(DensityFunction* _argument1, DensityFunction* _argument2);
    Add(const Add& copy) = delete;
    Add(Add&& move) = delete;
    Add& operator=(const Add& copy) = delete;
    Add& operator=(Add&& move) = delete;
    virtual ~Add();
    virtual double compute(const DensityFunction::SinglePointContext& context) const;
};

class NormalNoise;
class NoiseHolder : public DensityFunction {
    public:
    NormalNoise* noise;
    NoiseHolder(const int& firstOctave, const std::vector<double>& amplitudes);
    NoiseHolder(const NoiseHolder& copy) = delete;
    NoiseHolder(NoiseHolder&& move) = delete;
    NoiseHolder& operator=(const NoiseHolder& copy) = delete;
    NoiseHolder& operator=(NoiseHolder&& move) = delete;
    virtual ~NoiseHolder();
    virtual double compute(const DensityFunction::SinglePointContext& context) const;
    double getValue(const Vec3D& block) const;
    double getValue(const double& x, const double& y, const double& z) const;
};
class Offset : public DensityFunction {
    public:
    // TODO
    Offset();
    Offset(const Offset& copy) = delete;
    Offset(Offset&& move) = delete;
    Offset& operator=(const Offset& copy) = delete;
    Offset& operator=(Offset&& move) = delete;
    virtual ~Offset() {};
    virtual double compute(const DensityFunction::SinglePointContext& context) const;
};


class ImprovedNoise {
    public:
    std::vector<char> p;
    double xo;
    double yo;
    double zo;
    ImprovedNoise(BitRandomSource* random);
    ImprovedNoise(const ImprovedNoise& copy) = delete;
    ImprovedNoise(ImprovedNoise&& move) = delete;
    ImprovedNoise& operator=(const ImprovedNoise& copy) = delete;
    ImprovedNoise& operator=(ImprovedNoise&& move) = delete;
    ~ImprovedNoise();

    double noise(const double& _x, const double& _y, const double& _z, const double& yScale, const double& yFudge);
    static double gradDot(int hash, double x, double y, double z);
    int getP(const int& x);
    double sampleAndLerp(int x, int y, int z, double xr, double yr, double zr, double yrOriginal);
};
class PerlinNoise {
    public:
    int firstOctave;
    std::vector<double> amplitudes;
    std::vector<ImprovedNoise*> noiseLevels;
    double lowestFreqInputFactor;
    double lowestFreqValueFactor;
    double maxValue;
    PerlinNoise(LCG* rand, const int& _firstOctave, const std::vector<double>& _amplitudes);
    PerlinNoise(const PerlinNoise& copy) = delete;
    PerlinNoise(PerlinNoise&& move) = delete;
    PerlinNoise& operator=(const PerlinNoise& copy) = delete;
    PerlinNoise& operator=(PerlinNoise&& move) = delete;
    virtual ~PerlinNoise();
    double getMaxValue();
    double getValue(const Vec3D& block);
    double getValue(const double& x, const double& y, const double& z);
    double getValue(const double& x, const double& y, const double& z, const double& yScale, const double& yFudge, const bool& yFlatHack);
    static void skipOctave(LCG* random);
    double edgeValue(double noiseValue);
    double wrap(double x);
};

class NormalNoise {
    public:
    double valueFactor = 0.0;
    LCG* rand;
    PerlinNoise* first;
    PerlinNoise* second;
    double maxValue = 0.0;
    NormalNoise(const int& firstOctave, const std::vector<double>& amplitudes);
    NormalNoise(const NormalNoise& copy) = delete;
    NormalNoise(NormalNoise&& move) = delete;
    NormalNoise& operator=(const NormalNoise& copy) = delete;
    NormalNoise& operator=(NormalNoise&& move) = delete;
    virtual ~NormalNoise();
    double getValue(const Vec3D& block);
    double getValue(const double& x, const double& y, const double& z);
};

#endif// __NOISE