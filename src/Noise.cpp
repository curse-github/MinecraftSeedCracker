#include "Noise.h"


#pragma region DensityFunction

long long int DensityFunction::world_seed = 0;

// see DensityFunction.SinglePointContext class in net.minecraft.world.level.levelgen.DensityFunction
DensityFunction::SinglePointContext::SinglePointContext(const Pos& _block) : block(_block) {}

// see "root/worldgen/density_function/zero.json"
DensityFunction* DensityFunction::zero() {
    return (DensityFunction*) new Zero();
}
//  see "root/worldgen/density_function/shift_x.json"
DensityFunction* DensityFunction::shift_x() {
    return (DensityFunction*) new ShiftA(DensityFunction::noise_offset());
}
//  see "root/worldgen/density_function/shift_z.json"
DensityFunction* DensityFunction::shift_z() {
    return (DensityFunction*) new ShiftB(DensityFunction::noise_offset());
}
//  see "root/worldgen/density_function/overworld/continents.json"
DensityFunction* DensityFunction::continents() {
    return (DensityFunction*) new ShiftedNoise(DensityFunction::shift_x(), DensityFunction::zero(), DensityFunction::shift_z(), 0.25, 0.0, DensityFunction::noise_continentalness());
}
//  see "root/worldgen/density_function/overworld/erosion.json"
DensityFunction* DensityFunction::erosion() {
    return (DensityFunction*) new ShiftedNoise(DensityFunction::shift_x(), DensityFunction::zero(), DensityFunction::shift_z(), 0.25, 0.0, DensityFunction::noise_erosion());
}
//  see "root/worldgen/density_function/overworld/depth.json"
DensityFunction* DensityFunction::depth() {
    return (DensityFunction*) new Add((DensityFunction*) new YClampedGradient(1.5, -64, -1.5, 320), (DensityFunction*) new Offset());
}
//  see "root/worldgen/density_function/overworld/ridges.json"
DensityFunction* DensityFunction::ridges() {
    return (DensityFunction*) new ShiftedNoise(DensityFunction::shift_x(), DensityFunction::zero(), DensityFunction::shift_z(), 0.25, 0.0, DensityFunction::noise_ridges());
}

//  see "root/worldgen/noise/offset.json"
NoiseHolder* DensityFunction::noise_offset() {
    return new NoiseHolder(-3, { 1.0, 1.0, 1.0, 0.0 });
}
//  see "root/worldgen/noise/temperature.json"
NoiseHolder* DensityFunction::noise_temperature() {
    return new NoiseHolder(-10, { 1.5, 0.0, 1.0, 0.0, 0.0, 0.0 });
}
//  see "root/worldgen/noise/vegetation.json"
NoiseHolder* DensityFunction::noise_vegetation() {
    return new NoiseHolder(-8, { 1.0, 1.0, 0.0, 0.0, 0.0, 0.0 });
}
//  see "root/worldgen/noise/continentalness.json"
NoiseHolder* DensityFunction::noise_continentalness() {
    return new NoiseHolder(-9, { 1.0, 1.0, 2.0, 2.0, 2.0, 1.0, 1.0, 1.0, 1.0 });
}
//  see "root/worldgen/noise/erosion.json"
NoiseHolder* DensityFunction::noise_erosion() {
    return new NoiseHolder(-9, { 1.0, 1.0, 0.0, 1.0, 1.0 });
}
//  see "root/worldgen/noise/ridges.json"
NoiseHolder* DensityFunction::noise_ridges() {
    return new NoiseHolder(-7, { 1.0, 2.0, 1.0, 0.0, 0.0, 0.0 });
}


Zero::Zero() {

}
double Zero::compute(const DensityFunction::SinglePointContext& context) const { return 0.0; }

ShiftNoise::ShiftNoise(NoiseHolder* _noise) : noise(_noise) {
    
}
ShiftNoise::~ShiftNoise() {
    delete noise;
}
double ShiftNoise::compute(const Pos& block) const {
    //std::cout << "            x-before-before\n";
    const double out = noise->getValue(block.x * 0.25, block.y * 0.25, block.z * 0.25)*4.0;
    //std::cout << "            x-before-after\n";
    return out;
}

ShiftA::ShiftA(NoiseHolder* _noise) : ShiftNoise(_noise) {
    
}
double ShiftA::compute(const DensityFunction::SinglePointContext& context) const {
    return ShiftNoise::compute({ context.block.x, 0, context.block.z });
}

ShiftB::ShiftB(NoiseHolder* _noise) : ShiftNoise(_noise) {
    
}
double ShiftB::compute(const DensityFunction::SinglePointContext& context) const {
    return ShiftNoise::compute(Pos{ context.block.z, context.block.x, 0 });
}

ShiftedNoise::ShiftedNoise(DensityFunction* _shiftX, DensityFunction* _shiftY, DensityFunction* _shiftZ, const double& _xzScale, const double& _yScale, NoiseHolder* _noise)
    : shiftX(_shiftX), shiftY(_shiftY), shiftZ(_shiftZ), xzScale(_xzScale), yScale(_yScale), noise(_noise)
{
    
}
ShiftedNoise::~ShiftedNoise() {
    delete shiftX;
    delete shiftY;
    delete shiftZ;
    delete noise;
}
double ShiftedNoise::compute(const DensityFunction::SinglePointContext& context) const {
    //std::cout << "        x-before\n";
    const double x = context.block.x * xzScale + shiftX->compute(context);
    //std::cout << "        x-after\n";
    const double y = context.block.y * yScale + shiftY->compute(context);
    const double z = context.block.z * xzScale + shiftZ->compute(context);
    const double out = noise->getValue(x, y, z);
    return out;
}


YClampedGradient::YClampedGradient(const double& _fromValue, const double& _fromY, const double& _toValue, const double& _toY)
    : fromValue(_fromValue), fromY(_fromY), toValue(_toValue), toY(_toY)
{
    
}
double clampedMap(const double& x, const double& fromA, const double& toA, const double& fromB, const double& toB) {
    return fromB + std::clamp((x - fromA) / (toA - fromA), 0.0, 1.0) * (toB - fromB);
}
double YClampedGradient::compute(const DensityFunction::SinglePointContext& context) const {
    return clampedMap(context.block.y, fromY, toY, fromValue, toValue);
}

Add::Add(DensityFunction* _argument1, DensityFunction* _argument2) : argument1(_argument1), argument2(_argument2){
    
}
Add::~Add() {
    delete argument1;
    delete argument2;
}
double Add::compute(const DensityFunction::SinglePointContext& context) const {
    return argument1->compute(context) + argument2->compute(context);
}


NoiseHolder::NoiseHolder(const int& firstOctave, const std::vector<double>& amplitudes) : noise(new NormalNoise(firstOctave, amplitudes)) {

}
NoiseHolder::~NoiseHolder() {
    delete noise;
}
double NoiseHolder::compute(const DensityFunction::SinglePointContext& context) const {
    return noise->getValue(context.block.toVec3D());
}
double NoiseHolder::getValue(const Vec3D& block) const {
    return noise->getValue(block);
}
double NoiseHolder::getValue(const double& x, const double& y, const double& z) const {
    return noise->getValue(x, y, z);
}
Offset::Offset() {
    // TODO
}
double Offset::compute(const DensityFunction::SinglePointContext& context) const {
    return 0.0;// TODO
}

#pragma endregion// DensityFunction


#pragma region ImprovedNoise

ImprovedNoise::ImprovedNoise(BitRandomSource* random) : p(256, 0) {
    xo = random->nextDouble() * 256.0;
    yo = random->nextDouble() * 256.0;
    zo = random->nextDouble() * 256.0;
    for (int i = 0; i < 256; i++)
        p[i] = (char) i;
    for (int i = 0; i < 256; i++) {
        int offset = random->nextInt(256 - i);
        char tmp = p[i];
        p[i] = p[i + offset];
        p[i + offset] = tmp;
    }
}
ImprovedNoise::~ImprovedNoise() {

}
double ImprovedNoise::noise(const double& _x, const double& _y, const double& _z, const double& yScale, const double& yFudge) {
    double yrFudge;
    double x = _x + xo;
    double y = _y + yo;
    double z = _z + zo;
    int xf = std::floor(x);
    int yf = std::floor(y);
    int zf = std::floor(z);
    double xr = x - xf;
    double yr = y - yf;
    double zr = z - zf;
    if (yScale != 0.0) {
        double fudgeLimit;
        if (yFudge >= 0.0 && yFudge < yr) {
            fudgeLimit = yFudge;
        } else {
            fudgeLimit = yr;
        }
        yrFudge = std::floor(fudgeLimit / yScale + 1.0000000116860974E-7) * yScale;
    } else {
        yrFudge = 0.0;
    }
    //std::cout << "                    x-before-before-before-x-before\n";
    double out = sampleAndLerp(xf, yf, zf, xr, yr - yrFudge, zr, yr);
    //std::cout << "                    x-before-before-before-x-after\n";
    return out;
}
double SimplexNoise_dot(const int g[3], const double& x, const double& y, const double& z) {
    return g[0] * x + g[1] * y + g[2] * z;
}
int SimplexNoise_GRADIENT[16][3] = {
    { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 },
    { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 },
    { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 },
    { 1, 1, 0 }, { 0, -1, 1 }, { -1, 1, 0 }, { 0, -1, -1 }
};
double ImprovedNoise::gradDot(int hash, double x, double y, double z) {
    return SimplexNoise_dot(SimplexNoise_GRADIENT[hash & 0xF], x, y, z);
}
double smoothstep(const double& x) {
    return x * x * x * (x * (x * 6.0 - 15.0) + 10.0);
}
double lerp(const double& alpha1, const double& p0, const double& p1) {
    return p0 + alpha1 * (p1 - p0);
}
double lerp2(const double& alpha1, const double& alpha2, const double& x00, const double& x10, const double& x01, const double& x11) {
    return lerp(
        alpha2,
        lerp(alpha1, x00, x10), 
        lerp(alpha1, x01, x11)
    );
}
double lerp3(const double& alpha1, const double& alpha2, const double& alpha3, const double& x000, const double& x100, const double& x010, const double& x110, const double& x001, const double& x101, const double& x011, const double& x111) {
    return lerp(
        alpha3,
        lerp2(alpha1, alpha2, x000, x100, x010, x110), 
        lerp2(alpha1, alpha2, x001, x101, x011, x111)
    );
}
int ImprovedNoise::getP(const int& x) {
    return p[x & 0xff] & 0xff;
}
double ImprovedNoise::sampleAndLerp(int x, int y, int z, double xr, double yr, double zr, double yrOriginal) {
    int x0 = getP(x);
    int x1 = getP(x + 1);
    int xy00 = getP(x0 + y);
    int xy01 = getP(x0 + y + 1);
    int xy10 = getP(x1 + y);
    int xy11 = getP(x1 + y + 1);
    double d000 = gradDot(getP(xy00 + z), xr, yr, zr);
    double d100 = gradDot(getP(xy10 + z), xr - 1.0, yr, zr);
    double d010 = gradDot(getP(xy01 + z), xr, yr - 1.0, zr);
    double d110 = gradDot(getP(xy11 + z), xr - 1.0, yr - 1.0, zr);
    double d001 = gradDot(getP(xy00 + z + 1), xr, yr, zr - 1.0);
    double d101 = gradDot(getP(xy10 + z + 1), xr - 1.0, yr, zr - 1.0);
    double d011 = gradDot(getP(xy01 + z + 1), xr, yr - 1.0, zr - 1.0);
    double d111 = gradDot(getP(xy11 + z + 1), xr - 1.0, yr - 1.0, zr - 1.0);
    double xAlpha = smoothstep(xr);
    double yAlpha = smoothstep(yrOriginal);
    double zAlpha = smoothstep(zr);
    return lerp3(xAlpha, yAlpha, zAlpha, d000, d100, d010, d110, d001, d101, d011, d111);
}

#pragma endregion// ImprovedNoise


#pragma region PerlinNoise

PerlinNoise::PerlinNoise(LCG* random, const int& _firstOctave, const std::vector<double>& _amplitudes) : firstOctave(_firstOctave), amplitudes(_amplitudes), noiseLevels(amplitudes.size(), nullptr) {
    int octaves = amplitudes.size();
    int zeroOctaveIndex = -firstOctave;
    ImprovedNoise* zeroOctave = new ImprovedNoise(random);
    if (zeroOctaveIndex >= 0 && zeroOctaveIndex < octaves) {
        double zeroOctaveAmplitude = amplitudes[zeroOctaveIndex];
        if (zeroOctaveAmplitude != 0.0)
            noiseLevels[zeroOctaveIndex] = zeroOctave;
    }
    for (int i = zeroOctaveIndex - 1; i >= 0; i--) {
        if (i < octaves) {
            double amplitude = amplitudes[i];
            if (amplitude != 0.0)
                noiseLevels[i] = new ImprovedNoise(random);
            else
                skipOctave(random);
        } else
            skipOctave(random);
    }
    lowestFreqInputFactor = std::pow(2.0, -zeroOctaveIndex);
    lowestFreqValueFactor = std::pow(2.0, (octaves - 1)) / (std::pow(2.0, octaves) - 1.0);
    maxValue = edgeValue(2.0);
}
PerlinNoise::~PerlinNoise() {
    for(ImprovedNoise* noiseLevel : noiseLevels)
        delete noiseLevel;
};
void PerlinNoise::skipOctave(LCG* random) {
    random->nextSeed(262);
}
double PerlinNoise::edgeValue(double noiseValue) {
    double value = 0.0;
    double valueFactor = lowestFreqValueFactor;
    for (int i = 0; i < noiseLevels.size(); i++) {
        ImprovedNoise* noise = noiseLevels[i];
        if (noise != nullptr)
            value += amplitudes[i] * noiseValue * valueFactor;
        valueFactor /= 2.0;
    } 
    return value;
}
double PerlinNoise::getMaxValue() {
    return maxValue;
}
double PerlinNoise::getValue(const Vec3D& block) {
    return getValue(block.x, block.y, block.z, 0.0, 0.0, false);
}
double PerlinNoise::getValue(const double& x, const double& y, const double& z) {
    return getValue(x, y, z, 0.0, 0.0, false);
}
double PerlinNoise::getValue(const double& x, const double& y, const double& z, const double& yScale, const double& yFudge, const bool& yFlatHack) {
    double value = 0.0;
    double factor = lowestFreqInputFactor;
    double valueFactor = lowestFreqValueFactor;
    for (int i = 0; i < noiseLevels.size(); i++) {
        ImprovedNoise* noise = noiseLevels[i];
        if (noise != nullptr) {
            //std::cout << "                x-before-before-before-" << i << "\n";
            double noiseVal = noise->noise(wrap(x * factor), yFlatHack ? -noise->yo : wrap(y * factor), wrap(z * factor), yScale * factor, yFudge * factor);
            value += amplitudes[i] * noiseVal * valueFactor;
        } 
        factor *= 2.0;
        valueFactor /= 2.0;
    } 
    return value;
}
long long int lfloor(const double& v) {
    long long int i = (long long int)v;
    return (v < i) ? (i - 1ll) : i;
}
double PerlinNoise::wrap(double x) {
    return x - lfloor(x / 3.3554432E7 + 0.5) * 3.3554432E7;
}

#pragma endregion// PerlinNoise


#pragma region NormalNoise

double expectedDeviation(int octaveSpan) {
    return 0.1 * (1.0 + 1.0 / (octaveSpan + 1));
}
NormalNoise::NormalNoise(const int& firstOctave, const std::vector<double>& amplitudes) {
    rand = new LCG(DensityFunction::world_seed);
    first = new PerlinNoise(rand, firstOctave, amplitudes);
    second = new PerlinNoise(rand, firstOctave, amplitudes);
    int minOctave = 2147483647;
    int maxOctave = -2147483648;
    for (int i = 0; i < amplitudes.size(); i++) {
        double amplitude = amplitudes[i];
        if (amplitude != 0.0) {
            minOctave = std::min(minOctave, i);
            maxOctave = std::max(maxOctave, i);
        }
    }
    valueFactor = 0.16666666666666666 / expectedDeviation(maxOctave - minOctave);
    maxValue = (first->getMaxValue() + second->getMaxValue()) * valueFactor;
}
NormalNoise::~NormalNoise() {
    delete first;
    delete second;
    delete rand;
}
double NormalNoise::getValue(const Vec3D& block) {
    return getValue(block.x, block.y, block.z);
}
double NormalNoise::getValue(const double& x, const double& y, const double& z) {
    double x2 = x * 1.0181268882175227;
    double y2 = y * 1.0181268882175227;
    double z2 = z * 1.0181268882175227;
    //std::cout << "            x-before-before-before\n";
    const double firstValue = first->getValue(x, y, z);
    //std::cout << "            x-before-before-after\n";
    const double secondValue = second->getValue(x2, y2, z2);
    return (firstValue + secondValue) * valueFactor;
}

#pragma endregion// NormalNoise