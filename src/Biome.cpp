#include "Biome.h"


// see Climate::Parameter::span function in net.minecraft.world.level.biome.Climate
Climate::Parameter Climate::Parameter::point(const float& min) {
    return span(min, min);
}
// see Climate::Parameter::span function in net.minecraft.world.level.biome.Climate
Climate::Parameter Climate::Parameter::span(const float& min, const float& max) {
    return { (long long int)(min * 10000.0f), (long long int)(max * 10000.0f) };
}
// see Climate::Parameter::span function in net.minecraft.world.level.biome.Climate
Climate::Parameter Climate::Parameter::span(const Parameter& min, const Parameter& max) {
    return { std::min(min.min, max.min), std::max(min.max, max.max) };
}
// see Climate::Parameter::span function in net.minecraft.world.level.biome.Climate
Climate::Parameter Climate::Parameter::span(const Climate::Parameter& other) const {
    return { std::min(min, other.min), std::max(max, other.max) };
}
// see Climate::Parameter.distance function in net.minecraft.world.level.biome.Climate
long long int Climate::Parameter::distance(const long long int& target) const {
    long long int above = target - max;
    long long int below = min - target;
    if (above > 0ll)
        return above;
    return std::max(below, 0ll);
}


Climate::ParameterPoint::ParameterPoint(const Climate::Parameter& _temperature, const Climate::Parameter& _humidity, const Climate::Parameter& _continentalness, const Climate::Parameter& _erosion, const Climate::Parameter& _depth, const Climate::Parameter& _weirdness, const float& _offset)
    : temperature(_temperature), humidity(_humidity), continentalness(_continentalness), erosion(_erosion), depth(_depth), weirdness(_weirdness), offset(_offset)
{

}
// see Climate::Parameter.parameterSpace function in net.minecraft.world.level.biome.Climate
std::vector<Climate::Parameter> Climate::ParameterPoint::parameterSpace() const {
    return { temperature, humidity, continentalness, erosion, depth, weirdness, { offset, offset } };
}
// see Climate.TargetPoint class in net.minecraft.world.level.biome.Climate
Climate::TargetPoint::TargetPoint(const long long int& _temperature, const long long int& _humidity, const long long int& _continentalness, const long long int& _erosion, const long long int& _depth, const long long int& _weirdness)
    : temperature(_temperature), humidity(_humidity), continentalness(_continentalness), erosion(_erosion), depth(_depth), weirdness(_weirdness)
{}
Climate::TargetPoint::TargetPoint(const float& _temperature, const float& _humidity, const float& _continentalness, const float& _erosion, const float& _depth, const float& _weirdness)
    : temperature((long long int)(_temperature * 10000.0f)), humidity((long long int)(_humidity * 10000.0f)), continentalness((long long int)(_continentalness * 10000.0f)), erosion((long long int)(_erosion * 10000.0f)), depth((long long int)(_depth * 10000.0f)), weirdness((long long int)(_weirdness * 10000.0f))
{}
// see Climate.TargetPoint.toParameterArray function in net.minecraft.world.level.biome.Climate
std::vector<long long int> Climate::TargetPoint::toParameterArray() const {
    return { temperature, humidity, continentalness, erosion, depth, weirdness };
}


// see Climate.RTree<T>.Node class in net.minecraft.world.level.biome.Climate
Climate::RTree_Biome::Node::Node(const std::vector<Climate::Parameter>& _parameterSpace) : parameterSpace(_parameterSpace) {
}
Climate::RTree_Biome::Node::~Node() {
}
// see Climate.RTree.Node.distance function in net.minecraft.world.level.biome.Climate
long long int Climate::RTree_Biome::Node::distance(const std::vector<long long int>& target) {
    long long int distance = 0ll;
    for (int i = 0; i < 7; i++)
        distance += std::pow(parameterSpace[i].distance(target[i]), 2);
    return distance;
}


Climate::RTree_Biome::CenterIterativeComparator::CenterIterativeComparator(const long long int& _dimensions, const long long int& _minDimension, const bool& _absolute) : dimensions(_dimensions), minDimension(_minDimension), absolute(_absolute) {

}
//  see Climate.RTree<T>.comparator function in net.minecraft.world.level.biome.Climate
bool Climate::RTree_Biome::CenterIterativeComparator::operator()(const Climate::RTree_Biome::Node* a, const Climate::RTree_Biome::Node* b) {
    std::vector<Parameter> parameterSpaceA = a->parameterSpace;
    std::vector<Parameter> parameterSpaceB = b->parameterSpace;
    for (size_t d = 0; d < 7; d++) {
        const long long int dimension = (minDimension + d) % dimensions;
        Parameter parameterA = parameterSpaceA[dimension];
        long long int centerA = (parameterA.min + parameterA.max) / 2L;
        centerA = absolute ? std::abs(centerA) : centerA;
        Parameter parameterB = parameterSpaceB[dimension];
        long long int centerB = (parameterB.min + parameterB.max) / 2L;
        centerB = absolute ? std::abs(centerB) : centerB;
        if (centerA != centerB) return centerA < centerB;
    }
    return false;
}
Climate::RTree_Biome::TotalMagnitudeComparator::TotalMagnitudeComparator(const long long int& _dimensions) : dimensions(_dimensions) {

}
//  see first Comparator.comparingLong function call in Climate.RTree<T>.build function in net.minecraft.world.level.biome.Climate
bool Climate::RTree_Biome::TotalMagnitudeComparator::operator()(const Climate::RTree_Biome::Node* a, const Climate::RTree_Biome::Node* b) {
    long totalMagnitudeA = 0L;
    for (int d = 0; d < dimensions; d++) {
        Parameter parameter = a->parameterSpace[d];
        totalMagnitudeA += std::abs((parameter.min + parameter.max) / 2L);
    }
    long totalMagnitudeB = 0L;
    for (int d = 0; d < dimensions; d++) {
        Parameter parameter = b->parameterSpace[d];
        totalMagnitudeB += std::abs((parameter.min + parameter.max) / 2L);
    }
    return totalMagnitudeA < totalMagnitudeB;
}

// see Climate.RTree<T>.Leaf class in net.minecraft.world.level.biome.Climate
Climate::RTree_Biome::Leaf::Leaf(const std::vector<Climate::Parameter>& _parameterSpace, const Biome& _value) : value(_value), Node(_parameterSpace) {
}
Climate::RTree_Biome::Leaf::~Leaf() {
}
// see Climate.RTree<T>.Leaf.search function in net.minecraft.world.level.biome.Climate
Climate::RTree_Biome::Leaf* Climate::RTree_Biome::Leaf::search(std::vector<long long int> target, Climate::RTree_Biome::Leaf* candidate) {
    return this;
}


// see Climate.RTree<T>.buildParameterSpace class in net.minecraft.world.level.biome.Climate
std::vector<Climate::Parameter> Climate::RTree_Biome::buildParameterSpace(const std::vector<Climate::RTree_Biome::Node*>& children) {
    int dimensions = 7;
    std::vector<Parameter> bounds;
    for (int d = 0; d < 7; d++) {
        bounds.push_back({ 0,0 });
    }
    for (const Node* child : children) {
        for (int d = 0; d < 7; d++) {
            bounds[d] = child->parameterSpace[d].span(bounds[d]);
        }
    }
    return bounds;
}


long long int Climate::RTree_Biome::SubTree::numSubTreesCreated = 0;
long long int Climate::RTree_Biome::SubTree::numSubTreesDeleted = 0;
// see Climate.RTree<T>.SubTree class in net.minecraft.world.level.biome.Climate
Climate::RTree_Biome::SubTree::SubTree(const std::vector<Climate::RTree_Biome::Node*>& _children) : children(_children), Node(buildParameterSpace(_children)) {
    
}
Climate::RTree_Biome::SubTree::SubTree(const std::vector<Climate::Parameter>& _parameterSpace, const std::vector<Climate::RTree_Biome::Node*>& _children) : children(_children), Node(_parameterSpace) {
    
}
void Climate::RTree_Biome::SubTree::deleteChildren() {
    for (size_t i = 0; i < children.size(); i++) {
        children[i]->deleteChildren();
        delete children[i];
    }
}
Climate::RTree_Biome::SubTree::~SubTree() {

}
//  see Climate.RTree<T>.SubTree.search function in net.minecraft.world.level.biome.Climate
Climate::RTree_Biome::Leaf* Climate::RTree_Biome::SubTree::search(std::vector<long long int> target, Climate::RTree_Biome::Leaf* candidate) {
    long long int minDistance = (candidate == nullptr) ? LLONG_MAX : candidate->distance(target);
    Climate::RTree_Biome::Leaf* closestLeaf = candidate;
    for (Climate::RTree_Biome::Node* child : children) {
        long childDistance = child->distance(target);
        if (minDistance > childDistance) {
            Climate::RTree_Biome::Leaf* leaf = child->search(target, closestLeaf);
            long leafDistance = (child == leaf) ? childDistance : leaf->distance(target);
            if (minDistance > leafDistance) {
                minDistance = leafDistance;
                closestLeaf = leaf;
            }
        }
    }
    return closestLeaf;
}


//  see Climate.RTree<T>.Leaf class in net.minecraft.world.level.biome.Climate
Climate::RTree_Biome::RTree_Biome(Climate::RTree_Biome::Node* _root) : root(_root) {
}
Climate::RTree_Biome::~RTree_Biome() {
    root->deleteChildren();
    delete root;
}
//  see first Comparator.comparingLong function in Climate.RTree<T>.build function in net.minecraft.world.level.biome.Climate
void Climate::RTree_Biome::sort(std::vector<Node*>& children, const int& dimensions) {
    std::sort(children.begin(), children.end(), TotalMagnitudeComparator(dimensions));
}
//  and Climate.RTree<T>.sort function in net.minecraft.world.level.biome.Climate
void Climate::RTree_Biome::sort(std::vector<Node*>& children, const int& dimensions, const int& minDimension, const bool& absolute) {
    std::sort(children.begin(), children.end(), CenterIterativeComparator(dimensions, minDimension, absolute));
}
//  see Climate.RTree<T>.bucketize function in net.minecraft.world.level.biome.Climate
std::vector<Climate::RTree_Biome::Node*> Climate::RTree_Biome::bucketize(const std::vector<Climate::RTree_Biome::Node*>& nodes) {
    std::vector<Node*> buckets;
    std::vector<Node*> children;
    int expectedChildrenCount = (int)std::pow(6.0, std::floor(std::log(nodes.size() - 0.01) / std::log(6.0)));// round to power of 6
    for (Node* child : nodes) {
        children.push_back(child);
        if (children.size() >= expectedChildrenCount) {
            buckets.push_back(new SubTree(children));
            children.clear();
        }
    }
    if (children.size() != 0)
        buckets.push_back(new SubTree(children));
    return buckets;
}
//  see Climate.RTree<T>.cost function in net.minecraft.world.level.biome.Climate
long long int Climate::RTree_Biome::cost(const std::vector<Climate::Parameter>& parameterSpace) {
    long result = 0L;
    for (const Climate::Parameter& parameter : parameterSpace)
        result += std::abs(parameter.max - parameter.min);
    return result;
}
//  see Climate.RTree<T>.build function in net.minecraft.world.level.biome.Climate
Climate::RTree_Biome::Node* Climate::RTree_Biome::build(const int& dimensions, std::vector<Node*>& children) {
    if (children.size() == 1)
        return children[0];
    if (children.size() <= 6) {
        sort(children, dimensions);
        return new SubTree(children);
    }
    long long int minCost = LLONG_MAX;
    int minDimension = -1;
    std::vector<Node*> minBuckets;
    for (int d = 0; d < dimensions; d++) {
        sort(children, dimensions, d, false);
        std::vector<Node*> buckets = bucketize(children);
        long totalCost = 0ll;
        for (Node* bucket : buckets)
            totalCost += cost(bucket->parameterSpace);
        if (minCost > totalCost) {
            minCost = totalCost;
            minDimension = d;
            for (size_t i = 0; i < minBuckets.size(); i++) {
                delete minBuckets[i];
            }
            minBuckets = buckets;
        } else {
            for (size_t i = 0; i < buckets.size(); i++)
                delete buckets[i];
        }
    }
    sort(minBuckets, dimensions, minDimension, true);
    return new SubTree(minBuckets);
}
Climate::RTree_Biome* Climate::RTree_Biome::create(const std::vector<std::pair<ParameterPoint, Biome>>& values) {
    std::vector<Node*> children;
    // values.size() = 7593
    for (size_t i = 0; i < values.size(); i++)
        children.push_back(new Leaf(values[i].first.parameterSpace(), values[i].second));
    return new Climate::RTree_Biome(build(7, children));
}
//  see Climate.RTree<T>.search function in net.minecraft.world.level.biome.Climate
Biome Climate::RTree_Biome::search(const TargetPoint& target) {
    lastResult = root->search(target.toParameterArray(), lastResult);
    return (lastResult != nullptr) ? lastResult->value : "";
}


Climate::ParameterList_Biome::ParameterList_Biome(const std::vector<std::pair<ParameterPoint, Biome>> _values) : values(_values), index(RTree_Biome::create(values)) {
    // index.size() = 7593
}
Climate::ParameterList_Biome::~ParameterList_Biome() {
    delete index;
}
Biome Climate::ParameterList_Biome::findValue(const TargetPoint& target) {
    return findValueIndex(target);
}
Biome Climate::ParameterList_Biome::findValueIndex(const TargetPoint& target) {
    return index->search(target);
}


#pragma region addBiomes
const Climate::Parameter FULL_RANGE = Climate::Parameter::span(-1.0f, 1.0f);
const Climate::Parameter temperatures[5] = {
    Climate::Parameter::span(-1.0f, -0.45f),
    Climate::Parameter::span(-0.45f, -0.15f),
    Climate::Parameter::span(-0.15f, 0.2f),
    Climate::Parameter::span(0.2f, 0.55f),
    Climate::Parameter::span(0.55f, 1.0f)
};
const Climate::Parameter humidities[5] = {
    Climate::Parameter::span(-1.0f, -0.35f),
    Climate::Parameter::span(-0.35f, -0.1f),
    Climate::Parameter::span(-0.1f, 0.1f),
    Climate::Parameter::span(0.1f, 0.3f),
    Climate::Parameter::span(0.3f, 1.0f)
};
const Climate::Parameter erosions[7] = {
    Climate::Parameter::span(-1.0f, -0.78f),
    Climate::Parameter::span(-0.78f, -0.375f),
    Climate::Parameter::span(-0.375f, -0.2225f),
    Climate::Parameter::span(-0.2225f, 0.05f),
    Climate::Parameter::span(0.05f, 0.45f),
    Climate::Parameter::span(0.45f, 0.55f),
    Climate::Parameter::span(0.55f, 1.0f)
};
const Climate::Parameter FROZEN_RANGE = temperatures[0];
const Climate::Parameter UNFROZEN_RANGE = Climate::Parameter::span(temperatures[1], temperatures[4]);
const Climate::Parameter mushroomFieldsContinentalness = Climate::Parameter::span(-1.2f, -1.05f);
const Climate::Parameter deepOceanContinentalness = Climate::Parameter::span(-1.05f, -0.455f);
const Climate::Parameter oceanContinentalness = Climate::Parameter::span(-0.455f, -0.19f);
const Climate::Parameter coastContinentalness = Climate::Parameter::span(-0.19f, -0.11f);
const Climate::Parameter inlandContinentalness = Climate::Parameter::span(-0.11f, 0.55f);
const Climate::Parameter nearInlandContinentalness = Climate::Parameter::span(-0.11f, 0.03f);
const Climate::Parameter midInlandContinentalness = Climate::Parameter::span(0.03f, 0.3f);
const Climate::Parameter farInlandContinentalness = Climate::Parameter::span(0.3f, 1.0f);
const Biome OCEANS[2][5] = {
    { "DEEP_FROZEN_OCEAN", "DEEP_COLD_OCEAN", "DEEP_OCEAN", "DEEP_LUKEWARM_OCEAN", "WARM_OCEAN" },
    { "FROZEN_OCEAN", "COLD_OCEAN", "OCEAN", "LUKEWARM_OCEAN", "WARM_OCEAN" }
};
const Biome MIDDLE_BIOMES[5][5] = {
    { "SNOWY_PLAINS", "SNOWY_PLAINS", "SNOWY_PLAINS", "SNOWY_TAIGA", "TAIGA" },
    { "PLAINS", "PLAINS", "FOREST", "TAIGA", "OLD_GROWTH_SPRUCE_TAIGA" },
    { "FLOWER_FOREST", "PLAINS", "FOREST", "BIRCH_FOREST", "DARK_FOREST" },
    { "SAVANNA", "SAVANNA", "FOREST", "JUNGLE", "JUNGLE" },
    { "DESERT", "DESERT", "DESERT", "DESERT", "DESERT" }
};
const Biome MIDDLE_BIOMES_VARIANT[5][5] = {
    { "ICE_SPIKES", "", "SNOWY_TAIGA", "", "" },
    { "", "", "", "", "OLD_GROWTH_PINE_TAIGA" },
    { "SUNFLOWER_PLAINS", "", "", "OLD_GROWTH_BIRCH_FOREST", "" },
    { "", "", "PLAINS", "SPARSE_JUNGLE", "BAMBOO_JUNGLE" },
    { "", "", "", "", "" }
};
const Biome PLATEAU_BIOMES[5][5] = {
    { "SNOWY_PLAINS", "SNOWY_PLAINS", "SNOWY_PLAINS", "SNOWY_TAIGA", "SNOWY_TAIGA" },
    { "MEADOW", "MEADOW", "FOREST", "TAIGA", "OLD_GROWTH_SPRUCE_TAIGA" },
    { "MEADOW", "MEADOW", "MEADOW", "MEADOW", "PALE_GARDEN" },
    { "SAVANNA_PLATEAU", "SAVANNA_PLATEAU", "FOREST", "FOREST", "JUNGLE" },
    { "BADLANDS", "BADLANDS", "BADLANDS", "WOODED_BADLANDS", "WOODED_BADLANDS" }
};
const Biome PLATEAU_BIOMES_VARIANT[5][5] = {
    { "ICE_SPIKES", "", "", "", "" },
    { "CHERRY_GROVE", "", "MEADOW", "MEADOW", "OLD_GROWTH_PINE_TAIGA" },
    { "CHERRY_GROVE", "CHERRY_GROVE", "FOREST", "BIRCH_FOREST", "" },
    { "", "", "", "", "" },
    { "ERODED_BADLANDS", "ERODED_BADLANDS", "", "", "" }
};
const Biome SHATTERED_BIOMES[5][5] = {
    { "WINDSWEPT_GRAVELLY_HILLS", "WINDSWEPT_GRAVELLY_HILLS", "WINDSWEPT_HILLS", "WINDSWEPT_FOREST", "WINDSWEPT_FOREST" },
    { "WINDSWEPT_GRAVELLY_HILLS", "WINDSWEPT_GRAVELLY_HILLS", "WINDSWEPT_HILLS", "WINDSWEPT_FOREST", "WINDSWEPT_FOREST" },
    { "WINDSWEPT_HILLS", "WINDSWEPT_HILLS", "WINDSWEPT_HILLS", "WINDSWEPT_FOREST", "WINDSWEPT_FOREST" },
    { "", "", "", "", "" },
    { "", "", "", "", "" }
};

void addBiomes(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes) {
    addOffCoastBiomes(biomes);
    addInlandBiomes(biomes);
    addUndergroundBiomes(biomes);
}
void addOffCoastBiomes(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes) {
    addSurfaceBiome(biomes, FULL_RANGE, FULL_RANGE, mushroomFieldsContinentalness, FULL_RANGE, FULL_RANGE, 0.0f, "MUSHROOM_FIELDS");
    for (int temperatureIndex = 0; temperatureIndex < 5; temperatureIndex++) {
        Climate::Parameter temperature = temperatures[temperatureIndex];
        addSurfaceBiome(biomes, temperature, FULL_RANGE, deepOceanContinentalness, FULL_RANGE, FULL_RANGE, 0.0f, OCEANS[0][temperatureIndex]);
        addSurfaceBiome(biomes, temperature, FULL_RANGE, oceanContinentalness, FULL_RANGE, FULL_RANGE, 0.0f, OCEANS[1][temperatureIndex]);
    }
}

void addInlandBiomes(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes) {
    addMidSlice(biomes, Climate::Parameter::span(-1.0f, -0.93333334f));
    addHighSlice(biomes, Climate::Parameter::span(-0.93333334f, -0.7666667f));
    addPeaks(biomes, Climate::Parameter::span(-0.7666667f, -0.56666666f));
    addHighSlice(biomes, Climate::Parameter::span(-0.56666666f, -0.4f));
    addMidSlice(biomes, Climate::Parameter::span(-0.4f, -0.26666668f));
    addLowSlice(biomes, Climate::Parameter::span(-0.26666668f, -0.05f));
    addValleys(biomes, Climate::Parameter::span(-0.05f, 0.05f));
    addLowSlice(biomes, Climate::Parameter::span(0.05f, 0.26666668f));
    addMidSlice(biomes, Climate::Parameter::span(0.26666668f, 0.4f));
    addHighSlice(biomes, Climate::Parameter::span(0.4f, 0.56666666f));
    addPeaks(biomes, Climate::Parameter::span(0.56666666f, 0.7666667f));
    addHighSlice(biomes, Climate::Parameter::span(0.7666667f, 0.93333334f));
    addMidSlice(biomes, Climate::Parameter::span(0.93333334f, 1.0f));
}
void addUndergroundBiomes(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes) {
    addUndergroundBiome(biomes, FULL_RANGE, FULL_RANGE, Climate::Parameter::span(0.8F, 1.0F), FULL_RANGE, FULL_RANGE, 0.0f, "DRIPSTONE_CAVES");
    addUndergroundBiome(biomes, FULL_RANGE, Climate::Parameter::span(0.7F, 1.0F), FULL_RANGE, FULL_RANGE, FULL_RANGE, 0.0f, "LUSH_CAVES");
    addBottomBiome(biomes, FULL_RANGE, FULL_RANGE, FULL_RANGE, Climate::Parameter::span(erosions[0], erosions[1]), FULL_RANGE, 0.0f, "DEEP_DARK");
}

void addValleys(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes, const Climate::Parameter& weirdness) {
    addSurfaceBiome(biomes, FROZEN_RANGE, FULL_RANGE, coastContinentalness, Climate::Parameter::span(erosions[0], erosions[1]), weirdness, 0.0f, (weirdness.max < 0L) ? "STONY_SHORE" : "FROZEN_RIVER");
    addSurfaceBiome(biomes, UNFROZEN_RANGE, FULL_RANGE, coastContinentalness, Climate::Parameter::span(erosions[0], erosions[1]), weirdness, 0.0f, (weirdness.max < 0L) ? "STONY_SHORE" : "RIVER");
    addSurfaceBiome(biomes, FROZEN_RANGE, FULL_RANGE, nearInlandContinentalness, Climate::Parameter::span(erosions[0], erosions[1]), weirdness, 0.0f, "FROZEN_RIVER");
    addSurfaceBiome(biomes, UNFROZEN_RANGE, FULL_RANGE, nearInlandContinentalness, Climate::Parameter::span(erosions[0], erosions[1]), weirdness, 0.0f, "RIVER");
    addSurfaceBiome(biomes, FROZEN_RANGE, FULL_RANGE, Climate::Parameter::span(coastContinentalness, farInlandContinentalness), Climate::Parameter::span(erosions[2], erosions[5]), weirdness, 0.0f, "FROZEN_RIVER");
    addSurfaceBiome(biomes, UNFROZEN_RANGE, FULL_RANGE, Climate::Parameter::span(coastContinentalness, farInlandContinentalness), Climate::Parameter::span(erosions[2], erosions[5]), weirdness, 0.0f, "RIVER");
    addSurfaceBiome(biomes, FROZEN_RANGE, FULL_RANGE, coastContinentalness, erosions[6], weirdness, 0.0f, "FROZEN_RIVER");
    addSurfaceBiome(biomes, UNFROZEN_RANGE, FULL_RANGE, coastContinentalness, erosions[6], weirdness, 0.0f, "RIVER");
    addSurfaceBiome(biomes, Climate::Parameter::span(temperatures[1], temperatures[2]), FULL_RANGE, Climate::Parameter::span(inlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, "SWAMP");
    addSurfaceBiome(biomes, Climate::Parameter::span(temperatures[3], temperatures[4]), FULL_RANGE, Climate::Parameter::span(inlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, "MANGROVE_SWAMP");
    addSurfaceBiome(biomes, FROZEN_RANGE, FULL_RANGE, Climate::Parameter::span(inlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, "FROZEN_RIVER");
    for (int temperatureIndex = 0; temperatureIndex < 5; temperatureIndex++) {
        Climate::Parameter temperature = temperatures[temperatureIndex];
        for (int humidityIndex = 0; humidityIndex < 5; humidityIndex++) {
            Climate::Parameter humidity = humidities[humidityIndex];
            Biome middleBiomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(midInlandContinentalness, farInlandContinentalness), Climate::Parameter::span(erosions[0], erosions[1]), weirdness, 0.0f, middleBiomeOrBadlandsIfHot);
        }
    }
}
void addLowSlice(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes, const Climate::Parameter& weirdness) {
    addSurfaceBiome(biomes, FULL_RANGE, FULL_RANGE, coastContinentalness, Climate::Parameter::span(erosions[0], erosions[2]), weirdness, 0.0f, "STONY_SHORE");
    addSurfaceBiome(biomes, Climate::Parameter::span(temperatures[1], temperatures[2]), FULL_RANGE, Climate::Parameter::span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, "SWAMP");
    addSurfaceBiome(biomes, Climate::Parameter::span(temperatures[3], temperatures[4]), FULL_RANGE, Climate::Parameter::span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, "MANGROVE_SWAMP");
    for (int temperatureIndex = 0; temperatureIndex < 5; temperatureIndex++) {
        Climate::Parameter temperature = temperatures[temperatureIndex];
        for (int humidityIndex = 0; humidityIndex < 5; humidityIndex++) {
            Climate::Parameter humidity = humidities[humidityIndex];
            Biome middleBiome = pickMiddleBiome(temperatureIndex, humidityIndex, weirdness);
            Biome middleBiomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
            Biome middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(temperatureIndex, humidityIndex, weirdness);
            Biome beachBiome = pickBeachBiome(temperatureIndex, humidityIndex);
            Biome middleBiomeOrWindsweptSavanna = maybePickWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, middleBiome);
            Biome shatteredCoastBiome = pickShatteredCoastBiome(temperatureIndex, humidityIndex, weirdness);
            addSurfaceBiome(biomes, temperature, humidity, nearInlandContinentalness, Climate::Parameter::span(erosions[0], erosions[1]), weirdness, 0.0f, middleBiomeOrBadlandsIfHot);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(midInlandContinentalness, farInlandContinentalness), Climate::Parameter::span(erosions[0], erosions[1]), weirdness, 0.0f, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
            addSurfaceBiome(biomes, temperature, humidity, nearInlandContinentalness, Climate::Parameter::span(erosions[2], erosions[3]), weirdness, 0.0f, middleBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(midInlandContinentalness, farInlandContinentalness), Climate::Parameter::span(erosions[2], erosions[3]), weirdness, 0.0f, middleBiomeOrBadlandsIfHot);
            addSurfaceBiome(biomes, temperature, humidity, coastContinentalness, Climate::Parameter::span(erosions[3], erosions[4]), weirdness, 0.0f, beachBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(nearInlandContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0f, middleBiome);
            addSurfaceBiome(biomes, temperature, humidity, coastContinentalness, erosions[5], weirdness, 0.0f, shatteredCoastBiome);
            addSurfaceBiome(biomes, temperature, humidity, nearInlandContinentalness, erosions[5], weirdness, 0.0f, middleBiomeOrWindsweptSavanna);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(midInlandContinentalness, farInlandContinentalness), erosions[5], weirdness, 0.0f, middleBiome);
            addSurfaceBiome(biomes, temperature, humidity, coastContinentalness, erosions[6], weirdness, 0.0f, beachBiome);
            if (temperatureIndex == 0) {
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, middleBiome);
            }
        }
    }
}
void addMidSlice(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes, const Climate::Parameter& weirdness) {
    addSurfaceBiome(biomes, FULL_RANGE, FULL_RANGE, coastContinentalness, Climate::Parameter::span(erosions[0], erosions[2]), weirdness, 0.0f, "STONY_SHORE");
    addSurfaceBiome(biomes, Climate::Parameter::span(temperatures[1], temperatures[2]), FULL_RANGE, Climate::Parameter::span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, "SWAMP");
    addSurfaceBiome(biomes, Climate::Parameter::span(temperatures[3], temperatures[4]), FULL_RANGE, Climate::Parameter::span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, "MANGROVE_SWAMP");
    for (int temperatureIndex = 0; temperatureIndex < 5; temperatureIndex++) {
        Climate::Parameter temperature = temperatures[temperatureIndex];
        for (int humidityIndex = 0; humidityIndex < 5; humidityIndex++) {
            Climate::Parameter humidity = humidities[humidityIndex];
            Biome middleBiome = pickMiddleBiome(temperatureIndex, humidityIndex, weirdness);
            Biome middleBiomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
            Biome middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(temperatureIndex, humidityIndex, weirdness);
            Biome shatteredBiome = pickShatteredBiome(temperatureIndex, humidityIndex, weirdness);
            Biome plateauBiome = pickPlateauBiome(temperatureIndex, humidityIndex, weirdness);
            Biome beachBiome = pickBeachBiome(temperatureIndex, humidityIndex);
            Biome middleBiomeOrWindsweptSavanna = maybePickWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, middleBiome);
            Biome shatteredCoastBiome = pickShatteredCoastBiome(temperatureIndex, humidityIndex, weirdness);
            Biome slopeBiome = pickSlopeBiome(temperatureIndex, humidityIndex, weirdness);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(nearInlandContinentalness, farInlandContinentalness), erosions[0], weirdness, 0.0f, slopeBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(nearInlandContinentalness, midInlandContinentalness), erosions[1], weirdness, 0.0f, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
            addSurfaceBiome(biomes, temperature, humidity, farInlandContinentalness, erosions[1], weirdness, 0.0f, (temperatureIndex == 0) ? slopeBiome : plateauBiome);
            addSurfaceBiome(biomes, temperature, humidity, nearInlandContinentalness, erosions[2], weirdness, 0.0f, middleBiome);
            addSurfaceBiome(biomes, temperature, humidity, midInlandContinentalness, erosions[2], weirdness, 0.0f, middleBiomeOrBadlandsIfHot);
            addSurfaceBiome(biomes, temperature, humidity, farInlandContinentalness, erosions[2], weirdness, 0.0f, plateauBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(coastContinentalness, nearInlandContinentalness), erosions[3], weirdness, 0.0f, middleBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(midInlandContinentalness, farInlandContinentalness), erosions[3], weirdness, 0.0f, middleBiomeOrBadlandsIfHot);
            if (weirdness.max < 0L) {
            addSurfaceBiome(biomes, temperature, humidity, coastContinentalness, erosions[4], weirdness, 0.0f, beachBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(nearInlandContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0f, middleBiome);
            } else {
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(coastContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0f, middleBiome);
            }
            addSurfaceBiome(biomes, temperature, humidity, coastContinentalness, erosions[5], weirdness, 0.0f, shatteredCoastBiome);
            addSurfaceBiome(biomes, temperature, humidity, nearInlandContinentalness, erosions[5], weirdness, 0.0f, middleBiomeOrWindsweptSavanna);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(midInlandContinentalness, farInlandContinentalness), erosions[5], weirdness, 0.0f, shatteredBiome);
            if (weirdness.max < 0L) {
            addSurfaceBiome(biomes, temperature, humidity, coastContinentalness, erosions[6], weirdness, 0.0f, beachBiome);
            } else {
            addSurfaceBiome(biomes, temperature, humidity, coastContinentalness, erosions[6], weirdness, 0.0f, middleBiome);
            }
            if (temperatureIndex == 0) {
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, middleBiome);
            }
        }
    }
}
void addHighSlice(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes, const Climate::Parameter& weirdness) {
    for (int temperatureIndex = 0; temperatureIndex < 5; temperatureIndex++) {
        Climate::Parameter temperature = temperatures[temperatureIndex];
        for (int humidityIndex = 0; humidityIndex < 5; humidityIndex++) {
            Climate::Parameter humidity = humidities[humidityIndex];
            Biome middleBiome = pickMiddleBiome(temperatureIndex, humidityIndex, weirdness);
            Biome middleBiomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
            Biome middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(temperatureIndex, humidityIndex, weirdness);
            Biome plateauBiome = pickPlateauBiome(temperatureIndex, humidityIndex, weirdness);
            Biome shatteredBiome = pickShatteredBiome(temperatureIndex, humidityIndex, weirdness);
            Biome middleBiomeOrWindsweptSavanna = maybePickWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, middleBiome);
            Biome slopeBiome = pickSlopeBiome(temperatureIndex, humidityIndex, weirdness);
            Biome peakBiome = pickPeakBiome(temperatureIndex, humidityIndex, weirdness);
            addSurfaceBiome(biomes, temperature, humidity, coastContinentalness, Climate::Parameter::span(erosions[0], erosions[1]), weirdness, 0.0f, middleBiome);
            addSurfaceBiome(biomes, temperature, humidity, nearInlandContinentalness, erosions[0], weirdness, 0.0f, slopeBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(midInlandContinentalness, farInlandContinentalness), erosions[0], weirdness, 0.0f, peakBiome);
            addSurfaceBiome(biomes, temperature, humidity, nearInlandContinentalness, erosions[1], weirdness, 0.0f, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(midInlandContinentalness, farInlandContinentalness), erosions[1], weirdness, 0.0f, slopeBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(coastContinentalness, nearInlandContinentalness), Climate::Parameter::span(erosions[2], erosions[3]), weirdness, 0.0f, middleBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(midInlandContinentalness, farInlandContinentalness), erosions[2], weirdness, 0.0f, plateauBiome);
            addSurfaceBiome(biomes, temperature, humidity, midInlandContinentalness, erosions[3], weirdness, 0.0f, middleBiomeOrBadlandsIfHot);
            addSurfaceBiome(biomes, temperature, humidity, farInlandContinentalness, erosions[3], weirdness, 0.0f, plateauBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(coastContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0f, middleBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(coastContinentalness, nearInlandContinentalness), erosions[5], weirdness, 0.0f, middleBiomeOrWindsweptSavanna);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(midInlandContinentalness, farInlandContinentalness), erosions[5], weirdness, 0.0f, shatteredBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(coastContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, middleBiome);
        }
    }
}
void addPeaks(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes, const Climate::Parameter& weirdness) {
    for (int temperatureIndex = 0; temperatureIndex < 5; temperatureIndex++) {
        Climate::Parameter temperature = temperatures[temperatureIndex];
        for (int humidityIndex = 0; humidityIndex < 5; humidityIndex++) {
            Climate::Parameter humidity = humidities[humidityIndex];
            Biome middleBiome = pickMiddleBiome(temperatureIndex, humidityIndex, weirdness);
            Biome middleBiomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
            Biome middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(temperatureIndex, humidityIndex, weirdness);
            Biome plateauBiome = pickPlateauBiome(temperatureIndex, humidityIndex, weirdness);
            Biome shatteredBiome = pickShatteredBiome(temperatureIndex, humidityIndex, weirdness);
            Biome shatteredBiomeOrWindsweptSavanna = maybePickWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, shatteredBiome);
            Biome peakBiome = pickPeakBiome(temperatureIndex, humidityIndex, weirdness);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(coastContinentalness, farInlandContinentalness), erosions[0], weirdness, 0.0f, peakBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(coastContinentalness, nearInlandContinentalness), erosions[1], weirdness, 0.0f, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(midInlandContinentalness, farInlandContinentalness), erosions[1], weirdness, 0.0f, peakBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(coastContinentalness, nearInlandContinentalness), Climate::Parameter::span(erosions[2], erosions[3]), weirdness, 0.0f, middleBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(midInlandContinentalness, farInlandContinentalness), erosions[2], weirdness, 0.0f, plateauBiome);
            addSurfaceBiome(biomes, temperature, humidity, midInlandContinentalness, erosions[3], weirdness, 0.0f, middleBiomeOrBadlandsIfHot);
            addSurfaceBiome(biomes, temperature, humidity, farInlandContinentalness, erosions[3], weirdness, 0.0f, plateauBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(coastContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0f, middleBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(coastContinentalness, nearInlandContinentalness), erosions[5], weirdness, 0.0f, shatteredBiomeOrWindsweptSavanna);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(midInlandContinentalness, farInlandContinentalness), erosions[5], weirdness, 0.0f, shatteredBiome);
            addSurfaceBiome(biomes, temperature, humidity, Climate::Parameter::span(coastContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0f, middleBiome);
        }
    }
}
void addUndergroundBiome(
    std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes,
    const Climate::Parameter& temperature, const Climate::Parameter& humidity, const Climate::Parameter& continentalness, const Climate::Parameter& erosion, const Climate::Parameter& weirdness, const float& offset,
    const Biome& biome
) {
    biomes.push_back({ Climate::ParameterPoint(temperature, humidity, continentalness, erosion, Climate::Parameter::span(0.2F, 0.9F), weirdness, offset), biome });
}
void addBottomBiome(
    std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes,
    const Climate::Parameter& temperature, const Climate::Parameter& humidity, const Climate::Parameter& continentalness, const Climate::Parameter& erosion, const Climate::Parameter& weirdness, const float& offset,
    const Biome& biome
) {
    biomes.push_back({ Climate::ParameterPoint(temperature, humidity, continentalness, erosion, Climate::Parameter::point(1.1F), weirdness, offset), biome });
}

Biome pickMiddleBiomeOrBadlandsIfHot(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness) {
    return (temperatureIndex == 4) ? pickBadlandsBiome(humidityIndex, weirdness) : pickMiddleBiome(temperatureIndex, humidityIndex, weirdness);
}
Biome pickBadlandsBiome(const int& humidityIndex, const Climate::Parameter& weirdness) {
    if (humidityIndex < 2)
        return (weirdness.max < 0L) ? "BADLANDS" : "ERODED_BADLANDS";
    if (humidityIndex < 3)
        return "BADLANDS";
    return "WOODED_BADLANDS";
}
Biome pickMiddleBiome(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness) {
    if (weirdness.max < 0L)
        return MIDDLE_BIOMES[temperatureIndex][humidityIndex];
    Biome variant = MIDDLE_BIOMES_VARIANT[temperatureIndex][humidityIndex];
    return (variant.size() == 0) ? MIDDLE_BIOMES[temperatureIndex][humidityIndex] : variant;
}
Biome pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness) {
    return (temperatureIndex == 0) ? pickSlopeBiome(temperatureIndex, humidityIndex, weirdness) : pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
}
Biome pickBeachBiome(const int& temperatureIndex, const int& humidityIndex) {
    if (temperatureIndex == 0)
        return "SNOWY_BEACH";
    if (temperatureIndex == 4)
        return "DESERT";
    return "BEACH";
}
Biome maybePickWindsweptSavannaBiome(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness, const Biome& underlyingBiome) {
    if (temperatureIndex > 1 && humidityIndex < 4 && weirdness.max >= 0L)
        return "WINDSWEPT_SAVANNA";
    return underlyingBiome;
}
Biome pickShatteredCoastBiome(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness) {
    Biome beachOrMiddleBiome = (weirdness.max >= 0L) ? pickMiddleBiome(temperatureIndex, humidityIndex, weirdness) : pickBeachBiome(temperatureIndex, humidityIndex);
    return maybePickWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, beachOrMiddleBiome);
}
Biome pickShatteredBiome(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness) {
    Biome biome = SHATTERED_BIOMES[temperatureIndex][humidityIndex];
    return (biome.size() == 0) ? pickMiddleBiome(temperatureIndex, humidityIndex, weirdness) : biome;
}
Biome pickPlateauBiome(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness) {
    if (weirdness.max >= 0L) {
        Biome variant = PLATEAU_BIOMES_VARIANT[temperatureIndex][humidityIndex];
        if (variant.size() != 0)
            return variant;
    }
    return PLATEAU_BIOMES[temperatureIndex][humidityIndex];
}
Biome pickSlopeBiome(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness) {
    if (temperatureIndex >= 3)
        return pickPlateauBiome(temperatureIndex, humidityIndex, weirdness);
    if (humidityIndex <= 1)
        return "SNOWY_SLOPES";
    return "GROVE";
}
Biome pickPeakBiome(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness) {
    if (temperatureIndex <= 2)
        return (weirdness.max < 0L) ? "JAGGED_PEAKS" : "FROZEN_PEAKS";
    if (temperatureIndex == 3)
        return "STONY_PEAKS";
    return pickBadlandsBiome(humidityIndex, weirdness);
}
void addSurfaceBiome(
    std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes,
    const Climate::Parameter& temperature, const Climate::Parameter& humidity, const Climate::Parameter& continentalness, const Climate::Parameter& erosion, const Climate::Parameter& weirdness, const float& offset,
    const Biome& second
) {
    biomes.push_back({ Climate::ParameterPoint(temperature, humidity, continentalness, erosion, Climate::Parameter::point(0.0f), weirdness, offset), second });
    biomes.push_back({ Climate::ParameterPoint(temperature, humidity, continentalness, erosion, Climate::Parameter::point(1.0f), weirdness, offset), second });
}
#pragma endregion// addBiomes


//  see Climate.Sampler class in net.minecraft.world.level.biome.Climate
//  see RandomState.sampler assignment net.minecraft.world.level.levelgen.RandomState
//  see RandomState.router assignment net.minecraft.world.level.levelgen.RandomState
//  see NoiseGeneratorSettings.overworld function net.minecraft.world.level.levelgen.NoiseGeneratorSettings
//      NoiseRouterData.overworld(context.lookup(Registries.DENSITY_FUNCTION), context.lookup(Registries.NOISE), false, false)
//  see NoiseRouterData.overworld function in net.minecraft.world.level.levelgen.NoiseRouterData
Sampler::Sampler() {
    temperature = (DensityFunction*) new ShiftedNoise(DensityFunction::shift_x(), DensityFunction::zero(), DensityFunction::shift_z(), 0.25, 0.0, DensityFunction::noise_temperature());
    humidity = (DensityFunction*) new ShiftedNoise(DensityFunction::shift_x(), DensityFunction::zero(), DensityFunction::shift_z(), 0.25, 0.0, DensityFunction::noise_vegetation());
    continentalness = DensityFunction::continents();
    erosion = DensityFunction::erosion();
    depth = DensityFunction::depth();
    weirdness = DensityFunction::ridges();
}
Climate::TargetPoint Sampler::sample(const QuartPos& quart) const {
    Pos block = quart.toBlock();
    DensityFunction::SinglePointContext context = DensityFunction::SinglePointContext(block);
    return Climate::TargetPoint(
        (float)temperature->compute(context),
        (float)humidity->compute(context),
        (float)continentalness->compute(context),
        (float)erosion->compute(context),
        (float)depth->compute(context),
        (float)weirdness->compute(context)
    );
}


BiomeSet_ContainsPredicate::BiomeSet_ContainsPredicate(const std::vector<Biome>& _set) : set(_set) {}
bool BiomeSet_ContainsPredicate::test(const Biome& biome) const {
    return std::find(set.cbegin(), set.cend(), biome) != set.cend();
}


//  MultiNoiseBiomeSource class in net.minecraft.world.level.biome.MultiNoiseBiomeSource
MultiNoiseBiomeSource::MultiNoiseBiomeSource() {
    std::vector<std::pair<Climate::ParameterPoint, Biome>> values;
    addBiomes(values);
    parameters = new Climate::ParameterList_Biome(values);
}
MultiNoiseBiomeSource::~MultiNoiseBiomeSource() {
    delete parameters;
}
// see MultiNoiseBiomeSource.getNoiseBiome function in net.minecraft.world.level.biome.MultiNoiseBiomeSource
Biome MultiNoiseBiomeSource::getNoiseBiome(const QuartPos& quart, const Sampler& sampler) {
    return getNoiseBiome(sampler.sample(quart));
}
//  see MultiNoiseBiomeSource.getNoiseBiome function in net.minecraft.world.level.biome.MultiNoiseBiomeSource
Biome MultiNoiseBiomeSource::getNoiseBiome(const Climate::TargetPoint& target) {
    return parameters->findValue(target);
}
// see BiomeSource.findBiomeHorizontal in net.minecraft.world.level.biome.BiomeSource
MultiNoiseBiomeSource::findBiomeHorizontal_Output MultiNoiseBiomeSource::findBiomeHorizontal(const Pos& origin, const int& searchRadius, const int& skipSteps, const BiomeSet_ContainsPredicate& allowed, LCG& random, const bool& findClosest) {
    QuartPos noiseCenter = origin.toQuartPos();
    int noiseRadius = QuartPos::fromBlock(searchRadius);
    int noiseY = QuartPos::fromBlock(origin.y);
    findBiomeHorizontal_Output result;
    int found = 0;
    int startRadius = findClosest ? 0 : noiseRadius;
    for (int currentRadius = startRadius; currentRadius <= noiseRadius; currentRadius += skipSteps) {
        for (int z = -currentRadius; z <= currentRadius; z += skipSteps) {
            bool zEdge = (std::abs(z) == currentRadius);
            for (int x = -currentRadius; x <= currentRadius; x += skipSteps) {
                if (findClosest) {
                    bool xEdge = (std::abs(x) == currentRadius);
                    if (!xEdge && !zEdge)
                        continue;
                }
                QuartPos noise = noiseCenter + QuartPos(x, 0, z);
                Biome biome = getNoiseBiome(noise, Sampler());
                if (allowed.test(biome)) {
                    if ((result.biome.size() == 0) || random.nextInt(found + 1) == 0) {
                        Vec3 resultPos(QuartPos::toBlock(noise.x), origin.y, QuartPos::toBlock(noise.z));
                        result.pos = resultPos;
                        result.biome = biome;
                        if (findClosest) return result;
                    }
                    found++;
                }
            }
        }
    }
    return result;
}