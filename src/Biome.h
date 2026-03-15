#ifndef __BIOME
#define __BIOME

#include <vector>
#include <string>
#include <algorithm>
#include <cfloat>// for FLT_MAX
#include "MinecraftLib.h"
#include "Random.h"

#define Biome std::string

class Climate {
    public:
    Climate() {};
    struct Parameter {
        long long int min;
        long long int max;
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
            virtual ~Node();
            long long int distance(const std::vector<long long int>& target);
            virtual Leaf* search(std::vector<long long int> target, Leaf* candidate) = 0;
            friend RTree_Biome;
        };
        class Leaf : Node {
            std::string value;
            protected:
            Leaf(const std::vector<Parameter>& _parameterSpace, const std::string& _value);
            Leaf(const Leaf& copy) = delete;
            Leaf(Leaf&& move) = delete;
            Leaf& operator=(const Leaf& copy) = delete;
            Leaf& operator=(Leaf&& move) = delete;
            public:
            virtual ~Leaf();
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
            virtual ~SubTree();
            virtual Leaf* search(std::vector<long long int> target, Leaf* candidate);
            friend RTree_Biome;
        };
        RTree_Biome(Node* _root);
        static void sort(std::vector<Node*>& children, const int& dimensions);
        static void sort(std::vector<Node*>& children, const int& dimensions, const int& dimension, const bool& absolute);
        static std::vector<Node*> bucketize(const std::vector<Node*>& nodes);
        static Node* build(const int& dimensions, std::vector<Node*>& values);
        static long long int cost(const std::vector<Parameter>& parameterSpace);
        public:
        static RTree_Biome create(const std::vector<std::pair<ParameterPoint, Biome>> values);
        Biome search(const TargetPoint& target);
    };
    class ParameterList_Biome {
        std::vector<std::pair<ParameterPoint, Biome>> values;
        RTree_Biome index;
        public:
        ParameterList_Biome(const std::vector<std::pair<ParameterPoint, Biome>> _values);
        Biome findValue(const TargetPoint& target);
        Biome findValueIndex(const TargetPoint& target);
    };
};
class DensityFunction {
    public:
    DensityFunction();
    
    class SinglePointContext;
    double compute(const SinglePointContext& context) const;

    class SinglePointContext {
        Pos block;
        public:
        SinglePointContext(const Pos& _block);
    };
};
class Sampler {
    DensityFunction temperature;
    DensityFunction humidity;
    DensityFunction continentalness;
    DensityFunction erosion;
    DensityFunction depth;
    DensityFunction weirdness;
    public:
    Sampler();
    Climate::TargetPoint sample(const QuartPos& quart) const;
};
struct BiomeSet_ContainsPredicate {
    std::vector<Biome> set;
    BiomeSet_ContainsPredicate(const std::vector<Biome>& _set);
    bool test(const Biome& biome) const;
};
class MultiNoiseBiomeSource {
    Climate::ParameterList_Biome parameters;
    public:
    MultiNoiseBiomeSource();
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