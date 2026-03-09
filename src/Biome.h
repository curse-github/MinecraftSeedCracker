#ifndef __BIOME
#define __BIOME
#include <string>
#include <vector>
#include <iostream>
#include "MinecraftLib.h"
#include "Noise.h"

void initBiomes(const long long int& worldSeed);
void printParameters();
std::string getBiome(const Pos& pos);

#endif// __BIOME