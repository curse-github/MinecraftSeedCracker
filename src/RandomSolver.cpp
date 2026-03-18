#include "RandomSolver.h"

std::vector<std::string> STRONGHOLD_BIASED_TO {
    "PLAINS",
    "SUNFLOWER_PLAINS",
    "SNOWY_PLAINS",
    "ICE_SPIKES",
    "DESERT",
    "FOREST",
    "FLOWER_FOREST",
    "BIRCH_FOREST",
    "DARK_FOREST",
    "PALE_GARDEN",
    "OLD_GROWTH_BIRCH_FOREST",
    "OLD_GROWTH_PINE_TAIGA",
    "OLD_GROWTH_SPRUCE_TAIGA",
    "TAIGA",
    "SNOWY_TAIGA",
    "SAVANNA",
    "SAVANNA_PLATEAU",
    "WINDSWEPT_HILLS",
    "WINDSWEPT_GRAVELLY_HILLS",
    "WINDSWEPT_FOREST",
    "WINDSWEPT_SAVANNA",
    "JUNGLE",
    "SPARSE_JUNGLE",
    "BAMBOO_JUNGLE",
    "BADLANDS",
    "ERODED_BADLANDS",
    "WOODED_BADLANDS",
    "MEADOW",
    "CHERRY_GROVE",
    "GROVE",
    "SNOWY_SLOPES",
    "FROZEN_PEAKS",
    "JAGGED_PEAKS",
    "STONY_PEAKS",
    "MUSHROOM_FIELDS",
    "DRIPSTONE_CAVES",
    "LUSH_CAVES"
};
//  see ChunkGeneratorStructureState.getRingPositionsFor function in net.minecraft.world.level.chunk.ChunkGeneratorStructureState
//  see ChunkGeneratorStructureState.ensureStructuresGenerated function in net.minecraft.world.level.chunk.ChunkGeneratorStructureState
//  see ChunkGeneratorStructureState.generatePositions function in net.minecraft.world.level.chunk.ChunkGeneratorStructureState
//  see ChunkGeneratorStructureState.generateRingPositions function in net.minecraft.world.level.chunk.ChunkGeneratorStructureState
//      preferredBiomes comes from in here
//          see ConcentricRingsStructurePlacement class in net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement
//          see "context.register(BuiltinStructureSets.STRONGHOLDS"... class in net.minecraft.data.worldgen.StructureSets
//          see BiomeTags.STRONGHOLD_BIASED_TO value in net.minecraft.tags.BiomeTags
//          see tag(BiomeTags.STRONGHOLD_BIASED_TO) list in net.minecraft.data.tags.BiomeTagsProvider
std::vector<SectionPos> StructureFinder::generateRingPositions(const long long int& world_seed, const int& distance, const int& count, int spread) {
    DensityFunction::world_seed = world_seed;
    MultiNoiseBiomeSource source;
    LCG JavaStructureRand(world_seed);
    std::vector<SectionPos> positions;
    positions.reserve(count);
    int amount_attempted = 0;
    double angle = JavaStructureRand.nextDouble() * TAU;
    int positionInCircle = 0;
    int circle = 0;
    for (int i = 0; i < count; i++) {
        double structure_distance = distance * (4 + 6*circle) + ((JavaStructureRand.nextDouble() - 0.5) * 2.5*distance);
        SectionPos pos((int)std::round(std::cos(angle) * structure_distance), (int)std::round(std::sin(angle) * structure_distance));
        LCG biomeRand = JavaStructureRand.split();
        {
            BiomeSet_ContainsPredicate preferredBiomes_contains(STRONGHOLD_BIASED_TO);
            MultiNoiseBiomeSource::findBiomeHorizontal_Output closestBiome = source.findBiomeHorizontal(pos.toBlockPos({8, 0, 8}), 112, preferredBiomes_contains, biomeRand);
            if (closestBiome.biome.size() != 0) {
                SectionPos tmp = SectionPos::fromBlock(closestBiome.pos);
                //std::cout << "offset = (" << (pos.x - tmp.x) << ", " << (pos.z - tmp.z) << ")\n";
                pos = tmp;
            }
        }
        positions.emplace_back(pos);
        angle += TAU / spread;
        if ((++positionInCircle) != spread) continue;
        circle++;
        positionInCircle = 0;
        spread += 2 * spread / (circle + 1);
        spread = std::min(spread, count - i);
        angle += JavaStructureRand.nextDouble() * TAU;
    }
    return positions;
}

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