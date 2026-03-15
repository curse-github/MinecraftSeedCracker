#include "Biome.h"


// see Climate.Parameter class in net.minecraft.world.level.biome.Climate
Climate::Parameter Climate::Parameter::span(const Climate::Parameter& other) const {
    return { std::min(min, other.min), std::max(max, other.max) };
}
// see Climate.Parameter.parameterSpace function in net.minecraft.world.level.biome.Climate
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


// see Climate.RTree<T>.Node class in net.minecraft.world.level.biome.Climate
Climate::RTree_Biome::Node::Node(const std::vector<Climate::Parameter>& _parameterSpace) : parameterSpace(_parameterSpace) {

}
Climate::RTree_Biome::Node::~Node() {

}


// see Climate.RTree<T>.Leaf class in net.minecraft.world.level.biome.Climate
Climate::RTree_Biome::Leaf::Leaf(const std::vector<Climate::Parameter>& _parameterSpace, const Biome& _value) : value(_value), Node(_parameterSpace) {

}
Climate::RTree_Biome::Leaf::~Leaf() {
    
}
Climate::RTree_Biome::Leaf* Climate::RTree_Biome::Leaf::search_distance(std::vector<long long int> target, Climate::RTree_Biome::Leaf* candidate) {
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


// see Climate.RTree<T>.SubTree class in net.minecraft.world.level.biome.Climate
Climate::RTree_Biome::SubTree::SubTree(const std::vector<Climate::RTree_Biome::Node*>& _children) : children(_children), Node(buildParameterSpace(_children)) {
    
}
Climate::RTree_Biome::SubTree::SubTree(const std::vector<Climate::Parameter>& _parameterSpace, const std::vector<Climate::RTree_Biome::Node*>& _children) : children(_children), Node(_parameterSpace) {

}
Climate::RTree_Biome::SubTree::~SubTree() {

}
//  see Climate.RTree<T>.SubTree.search function in net.minecraft.world.level.biome.Climate
//      line 79
Climate::RTree_Biome::Leaf* Climate::RTree_Biome::SubTree::search_distance(std::vector<long long int> target, Climate::RTree_Biome::Leaf* candidate) {
    // TODO
    return nullptr;
}


//  see Climate.RTree<T>.Leaf class in net.minecraft.world.level.biome.Climate
Climate::RTree_Biome::RTree_Biome(Climate::RTree_Biome::Node* _root) : root(_root) {
}
//  see Climate.RTree<T>.sort function in net.minecraft.world.level.biome.Climate
//      line 141
void Climate::RTree_Biome::sort(std::vector<Node*> children, const int& dimensions, const int& dimension, const bool& absolute) {
    Comparator comp = comparator(dimension, absolute);
    for (int d = 1; d < dimensions; d++) {
        comp = comp.thenComparing(comparator((dimension + d) % dimensions, absolute));
    }
    //children.sort(comparator);// TODO
}
//  see Climate.RTree<T>.comparator function in net.minecraft.world.level.biome.Climate
//      line 148
Comparator Climate::RTree_Biome::comparator(const int& dimension, const bool& absolute) {
    // TODO
    return Comparator();
}
//  see Climate.RTree<T>.bucketize function in net.minecraft.world.level.biome.Climate
//      line 155
std::vector<Climate::RTree_Biome::Node*> Climate::RTree_Biome::bucketize(const std::vector<Climate::RTree_Biome::Node*>& nodes) {
    // TODO
    return {};
}
//  see Climate.RTree<T>.cost function in net.minecraft.world.level.biome.Climate
long long int Climate::RTree_Biome::cost(const std::vector<Climate::Parameter>& parameterSpace) {
    long result = 0L;
    for (const Climate::Parameter& parameter : parameterSpace)
        result += std::abs(parameter.max - parameter.min);
    return result;
}
//  see Climate.RTree<T>.build function in net.minecraft.world.level.biome.Climate
//      line 107
Climate::RTree_Biome::Node* Climate::RTree_Biome::build(const int& dimensions, const std::vector<Node*>& children) {
    if (children.size() == 1)
        return children[0];
    /*if (children.size() <= 6) {
        children.sort(Comparator.comparingLong(leaf -> {
                long totalMagnitude = 0L;
                for (int d = 0; d < dimensions; d++) {
                    Climate.Parameter parameter = leaf.parameterSpace[d];
                    totalMagnitude += Math.abs((parameter.min() + parameter.max()) / 2L);
                }
                return totalMagnitude;
            }));
        return new SubTree(children);
    }*/
    long minCost = 1000000000000000000000000000000.0;
    int minDimension = -1;
    std::vector<Node*> minBuckets;
    for (int d = 0; d < dimensions; d++) {
        sort(children, dimensions, d, false);
        std::vector<Node*> buckets = bucketize(children);
        long totalCost = 0L;
        for (Node* bucket : buckets)
            totalCost += cost(bucket->parameterSpace);
        if (minCost > totalCost) {
            minCost = totalCost;
            minDimension = d;
            minBuckets = buckets;
        }
    } 
    sort(minBuckets, dimensions, minDimension, true);
    return new SubTree(minBuckets);
}
Climate::RTree_Biome Climate::RTree_Biome::create(const std::vector<std::pair<ParameterPoint, Biome>> values) {
    std::vector<Node*> children;
    for (size_t i = 0; i < values.size(); i++)
        children.push_back(new Leaf(values[i].first.parameterSpace(), values[i].second));
    return Climate::RTree_Biome(build(7, children));
}
//  see Climate.RTree<T>.search function in net.minecraft.world.level.biome.Climate
//      line 197
Biome Climate::RTree_Biome::search_distance(const TargetPoint& target) {
    // TODO
    return "";
}


Climate::ParameterList_Biome::ParameterList_Biome(const std::vector<std::pair<ParameterPoint, Biome>> _values) : values(_values), index(RTree_Biome::create(values)) {
    
}
Biome Climate::ParameterList_Biome::findValue(const TargetPoint& target) {
    return findValueIndex(target);
}
Biome Climate::ParameterList_Biome::findValueIndex(const TargetPoint& target) {
    return index.search_distance(target);
}


DensityFunction::DensityFunction() {
    
}
//  see DensityFunction class in net.minecraft.world.level.levelgen.DensityFunction
//      line 191
//  see <? extends DensityFunction> classes in net.minecraft.world.level.levelgen.DensityFunctions
double DensityFunction::compute(const DensityFunction::SinglePointContext& context) const {
    // TODO
    return 0.0;
};
// see DensityFunction.SinglePointContext class in net.minecraft.world.level.levelgen.DensityFunction
DensityFunction::SinglePointContext::SinglePointContext(const Pos& _block) : block(_block) {}


// see Climate.Sampler class in net.minecraft.world.level.biome.Climate
Sampler::Sampler() {};
Climate::TargetPoint Sampler::sample(const QuartPos& quart) const {
    Pos block = quart.toBlock();
    DensityFunction::SinglePointContext context = DensityFunction::SinglePointContext(block);    
    return Climate::TargetPoint(
        (float)temperature.compute(context),
        (float)humidity.compute(context),
        (float)continentalness.compute(context),
        (float)erosion.compute(context),
        (float)depth.compute(context),
        (float)weirdness.compute(context)
    );
}


BiomeSet_ContainsPredicate::BiomeSet_ContainsPredicate(const std::vector<Biome>& _set) : set(_set) {}
bool BiomeSet_ContainsPredicate::test(const Biome& biome) const {
    return std::find(set.cbegin(), set.cend(), biome) != set.cend();
}


//  MultiNoiseBiomeSource class in net.minecraft.world.level.biome.findBiomeHorizontal_Output
MultiNoiseBiomeSource::MultiNoiseBiomeSource() : parameters({}) {

}
// see MultiNoiseBiomeSource.getNoiseBiome function in net.minecraft.world.level.biome.MultiNoiseBiomeSource
Biome MultiNoiseBiomeSource::getNoiseBiome(const QuartPos& quart, const Sampler& sampler) {
    return getNoiseBiome(sampler.sample(quart));
}
//  see MultiNoiseBiomeSource.getNoiseBiome function in net.minecraft.world.level.biome.MultiNoiseBiomeSource
Biome MultiNoiseBiomeSource::getNoiseBiome(const Climate::TargetPoint& target) {
    return parameters.findValue(target);
}
// see BiomeSource.findBiomeHorizontal in net.minecraft.world.level.biome.BiomeSource
MultiNoiseBiomeSource::findBiomeHorizontal_Output MultiNoiseBiomeSource::findBiomeHorizontal(const Pos& origin, const int& searchRadius, const int& skipSteps, const BiomeSet_ContainsPredicate& allowed, LCG& random, const bool& findClosest) {
    QuartPos noiseCenter = origin.toQuartPos();
    int noiseRadius = QuartPos::fromBlock(searchRadius);
    int noiseY = QuartPos::fromBlock(origin.y);
    findBiomeHorizontal_Output result;
    int found = 0;
    int startRadius = findClosest ? 0 : noiseRadius; int currentRadius;
    for (currentRadius = startRadius; currentRadius <= noiseRadius; currentRadius += skipSteps) {
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