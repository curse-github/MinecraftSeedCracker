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
    return noise->getValue(block.x * 0.25, block.y * 0.25, block.z * 0.25)*4.0;
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
    double x = context.block.x * xzScale + shiftX->compute(context);
    double y = context.block.y * yScale + shiftY->compute(context);
    double z = context.block.z * xzScale + shiftZ->compute(context);
    return noise->getValue(x, y, z);
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


#pragma region PerlinNoise

PerlinNoise::PerlinNoise(WorldgenRandom* rand, const int& firstOctave, const std::vector<double>& amplitudes) {
    // TODO
}
double PerlinNoise::maxValue() {
    return 0.0;// TODO
}
double PerlinNoise::getValue(const Vec3D& block) {
    return getValue(block.x, block.y, block.z);
}
double PerlinNoise::getValue(const double& x, const double& y, const double& z) {
    return 0.0;// TODO
}

#pragma endregion// PerlinNoise


#pragma region NormalNoise

double expectedDeviation(int octaveSpan) {
    return 0.1 * (1.0 + 1.0 / (octaveSpan + 1));
}
NormalNoise::NormalNoise(const int& firstOctave, const std::vector<double>& amplitudes) {
    WorldgenRandom* thing = new WorldgenRandom(DensityFunction::world_seed);
    first = new PerlinNoise(thing, firstOctave, amplitudes);
    second = new PerlinNoise(thing, firstOctave, amplitudes);
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
    maxValue = (first->maxValue() + second->maxValue()) * valueFactor;
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
    return (first->getValue(x, y, z) + second->getValue(x2, y2, z2)) * valueFactor;
}

#pragma endregion// NormalNoise