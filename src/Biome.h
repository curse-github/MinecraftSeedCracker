#ifndef __BIOME
#define __BIOME

#include <vector>
#include <string>
#include <algorithm>
#include <climits>
#include "MinecraftLib.h"
#include "Random.h"
#include "Noise.h"

#define Biome std::string

class Climate {
    public:
    Climate() {};

    struct Parameter {
        long long int min;
        long long int max;
        static Parameter point(const float& min);
        static Parameter span(const float& min, const float& max);
        static Parameter span(const Parameter& min, const Parameter& max);
        Parameter span(const Parameter& other) const;
        long long int distance(const long long int& target) const;
    };
    struct ParameterPoint {
        Parameter temperature;
        Parameter humidity;
        Parameter continentalness;
        Parameter erosion;
        Parameter depth;
        Parameter weirdness;
        long long int offset;
        ParameterPoint(const Parameter& _temperature, const Parameter& _humidity, const Parameter& _continentalness, const Parameter& _erosion, const Parameter& _depth, const Parameter& _weirdness, const float& _offset);
        std::vector<Parameter> parameterSpace() const;
    };
    struct TargetPoint {
        long long int temperature;
        long long int humidity;
        long long int continentalness;
        long long int erosion;
        long long int depth;
        long long int weirdness;

        public:
        TargetPoint(const long long int& _temperature, const long long int& _humidity, const long long int& _continentalness, const long long int& _erosion, const long long int& _depth, const long long int& _weirdness);
        TargetPoint(const float& _temperature, const float& _humidity, const float& _continentalness, const float& _erosion, const float& _depth, const float& _weirdness);
        std::vector<long long int> toParameterArray() const;
    };
    class RTree_Biome {
        public:
        class Node;
        class Leaf;

        private:
        Node* root;
        Leaf* lastResult = nullptr;

        public:
        class Node {
            protected:
            std::vector<Parameter> parameterSpace;
            Node(const std::vector<Parameter>& _parameterSpace);
            Node(const Node& copy) = delete;
            Node(Node&& move) = delete;
            Node& operator=(const Node& copy) = delete;
            Node& operator=(Node&& move) = delete;
            public:
            virtual void deleteChildren() {};
            virtual ~Node();
            long long int distance(const std::vector<long long int>& target);
            virtual long long int size() = 0;
            virtual Leaf* search(std::vector<long long int> target, Leaf* candidate) = 0;
            friend RTree_Biome;
        };

        struct Comparator {
            virtual bool operator()(const Node* a, const Node* b) = 0;
        };
        struct CenterIterativeComparator : Comparator{
            long long int dimensions;
            long long int minDimension;
            bool absolute;
            CenterIterativeComparator(const long long int& _dimensions, const long long int& _minDimension, const bool& _absolute);
            virtual bool operator()(const Node* a, const Node* b);
        };
        struct TotalMagnitudeComparator : Comparator{
            long long int dimensions;
            TotalMagnitudeComparator(const long long int& _dimensions);
            virtual bool operator()(const Node* a, const Node* b);
        };

        class Leaf : Node {
            Biome value;
            protected:
            Leaf(const std::vector<Parameter>& _parameterSpace, const Biome& _value);
            Leaf(const Leaf& copy) = delete;
            Leaf(Leaf&& move) = delete;
            Leaf& operator=(const Leaf& copy) = delete;
            Leaf& operator=(Leaf&& move) = delete;
            public:
            virtual ~Leaf();
            virtual long long int size() { return 1; };
            virtual Leaf* search(std::vector<long long int> target, Leaf* candidate);
            friend RTree_Biome;
        };

        static std::vector<Parameter> buildParameterSpace(const std::vector<Climate::RTree_Biome::Node*>& children);
        class SubTree : Node {
            std::vector<Climate::RTree_Biome::Node*> children;
            protected:
            SubTree(const std::vector<Climate::RTree_Biome::Node*>& _children);
            SubTree(const std::vector<Parameter>& _parameterSpace, const std::vector<Climate::RTree_Biome::Node*>& _children);
            SubTree(const SubTree& copy) = delete;
            SubTree(SubTree&& move) = delete;
            SubTree& operator=(const SubTree& copy) = delete;
            SubTree& operator=(SubTree&& move) = delete;
            public:
            static long long int numSubTreesCreated;
            static long long int numSubTreesDeleted;
            virtual void deleteChildren();
            virtual ~SubTree();
            virtual long long int size() {
                long long int _size = 0ll;
                for (size_t i = 0; i < children.size(); i++) {
                    _size += children[i]->size();
                }
                return _size;
            };
            virtual Leaf* search(std::vector<long long int> target, Leaf* candidate);
            friend RTree_Biome;
        };

        RTree_Biome(Node* _root);
        ~RTree_Biome();
        static void sort(std::vector<Node*>& children, const int& dimensions);
        static void sort(std::vector<Node*>& children, const int& dimensions, const int& minDimension, const bool& absolute);
        static std::vector<Node*> bucketize(const std::vector<Node*>& nodes);
        static Node* build(const int& dimensions, std::vector<Node*>& values);
        static long long int cost(const std::vector<Parameter>& parameterSpace);
        public:
        static RTree_Biome* create(const std::vector<std::pair<ParameterPoint, Biome>>& values);
        long long int size() {
            return root->size();
        };
        Biome search(const TargetPoint& target);
    };

    class ParameterList_Biome {
        std::vector<std::pair<ParameterPoint, Biome>> values;
        RTree_Biome* index;
        public:
        ParameterList_Biome(const std::vector<std::pair<ParameterPoint, Biome>> _values);
        ~ParameterList_Biome();
        long long int size() {
            return index->size();
        };
        Biome findValue(const TargetPoint& target);
        Biome findValueIndex(const TargetPoint& target);
    };
};

void addBiomes(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes);

void addOffCoastBiomes(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes);
void addInlandBiomes(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes);
void addUndergroundBiomes(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes);

void addValleys(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes, const Climate::Parameter& weirdness);
void addLowSlice(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes, const Climate::Parameter& weirdness);
void addMidSlice(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes, const Climate::Parameter& weirdness);
void addHighSlice(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes, const Climate::Parameter& weirdness);
void addPeaks(std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes, const Climate::Parameter& weirdness);
void addUndergroundBiome(
    std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes,
    const Climate::Parameter& temperature, const Climate::Parameter& humidity, const Climate::Parameter& continentalness, const Climate::Parameter& erosion, const Climate::Parameter& weirdness, const float& offset,
    const Biome& biome
);
void addBottomBiome(
    std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes,
    const Climate::Parameter& temperature, const Climate::Parameter& humidity, const Climate::Parameter& continentalness, const Climate::Parameter& erosion, const Climate::Parameter& weirdness, const float& offset,
    const Biome& biome
);

Biome pickMiddleBiomeOrBadlandsIfHot(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness);
Biome pickBadlandsBiome(const int& humidityIndex, const Climate::Parameter& weirdness);
Biome pickMiddleBiome(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness);
Biome pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness);
Biome pickBeachBiome(const int& temperatureIndex, const int& humidityIndex);
Biome maybePickWindsweptSavannaBiome(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness, const Biome& underlyingBiome);
Biome pickShatteredCoastBiome(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness);
Biome pickShatteredBiome(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness);
Biome pickPlateauBiome(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness);
Biome pickSlopeBiome(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness);
Biome pickPeakBiome(const int& temperatureIndex, const int& humidityIndex, const Climate::Parameter& weirdness);
void addSurfaceBiome(
    std::vector<std::pair<Climate::ParameterPoint, Biome>>& biomes,
    const Climate::Parameter& temperature, const Climate::Parameter& humidity, const Climate::Parameter& continentalness, const Climate::Parameter& erosion, const Climate::Parameter& weirdness, const float& offset,
    const Biome& second
);


class Sampler {
    OwnedPointer<DensityFunction> shift_x;
    OwnedPointer<DensityFunction> shift_z;
    OwnedPointer<DensityFunction> temperature;
    OwnedPointer<DensityFunction> humidity;
    OwnedPointer<DensityFunction> continentalness;
    OwnedPointer<DensityFunction> erosion;
    OwnedPointer<DensityFunction> depth;
    OwnedPointer<DensityFunction> weirdness;
    public:
    Sampler();
    ~Sampler() {};
    Climate::TargetPoint sample(const QuartPos& quart) const;
};

struct BiomeSet_ContainsPredicate {
    std::vector<Biome> set;
    BiomeSet_ContainsPredicate(const std::vector<Biome>& _set);
    bool test(const Biome& biome) const;
};

class MultiNoiseBiomeSource {
    Climate::ParameterList_Biome* parameters;
    public:
    MultiNoiseBiomeSource();
    ~MultiNoiseBiomeSource();
    Biome getNoiseBiome(const QuartPos& quart, const Sampler& sampler);
    Biome getNoiseBiome(const Climate::TargetPoint& target);
    struct findBiomeHorizontal_Output {
        Vec3 pos;
        Biome biome;
    };
    findBiomeHorizontal_Output findBiomeHorizontal(const Pos& origin, const int& searchRadius, const int& skipSteps, const BiomeSet_ContainsPredicate& allowed, LCG& random, const bool& findClosest);
    // see BiomeSource.findBiomeHorizontal in net.minecraft.world.level.biome.BiomeSource
    findBiomeHorizontal_Output findBiomeHorizontal(const Pos& origin, const int& searchRadius, const BiomeSet_ContainsPredicate& allowed, LCG& random) {
        return findBiomeHorizontal(origin, searchRadius, 1, allowed, random, false);
    }
};

#endif// __BIOME