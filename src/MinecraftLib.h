#ifndef __MINECRAFT_LIB
#define __MINECRAFT_LIB

#include <string>
#include <vector>
#include "Lib.h"

struct ChunkPos;
struct BiomePos;
struct Pos : public Vec3 {
    Pos() : Vec3() {
    }
    Pos(const int& _x, const int& _y, const int& _z) : Vec3(_x, _y, _z) {
    }
    Pos(const Vec3& copy) : Vec3(copy.x, copy.y, copy.z) {
    }
    Pos(const Pos& copy) : Vec3(copy.x, copy.y, copy.z) {
    }
    Pos operator+(const Pos& rhs) const {
        return Pos(x + rhs.x, y + rhs.y, z + rhs.z);
    }
    Pos operator+(const Vec3& rhs) const {
        return Pos(x + rhs.x, y + rhs.y, z + rhs.z);
    }
    ChunkPos getChunkPos() const;
    Pos getOffsetPos(Pos offset) const;
    BiomePos getBiomePos() const;
    Vec3 toVec3() const;
    Vec3D toVec3D() const;
};
// see ChunkPos class in ChunkPos.java
struct ChunkPos : public Vec2 {
    ChunkPos() : Vec2(0, 0) {
    }
    ChunkPos(const int& _x, const int& _z) : Vec2(_x, _z) {
    }
    ChunkPos(const Vec2& copy) : Vec2(copy.x, copy.z) {
    }
    ChunkPos(const ChunkPos& copy) : Vec2(copy.x, copy.z) {
    }
    ~ChunkPos() {};
    ChunkPos& operator=(const ChunkPos& copy);
    ChunkPos& operator=(ChunkPos&& move);
    Pos getBlockPos() const;
    Pos getOffsetPos(Pos offset) const;
};
// see BiomePos class in BiomePos.java
struct BiomePos : public Vec2 {
    BiomePos() : Vec2(0, 0) {
    }
    BiomePos(const int& _x, const int& _z) : Vec2(_x, _z) {
    }
    BiomePos(const Vec2& copy) : Vec2(copy.x, copy.z) {
    }
    BiomePos(const BiomePos& copy) : Vec2(copy.x, copy.z) {
    }
    ~BiomePos() {};
    BiomePos& operator=(const BiomePos& copy);
    BiomePos& operator=(BiomePos&& move);
    Pos getBlockPos() const;
};

// see Direction class in net.minecraft.core.Direction
struct Direction {
    unsigned int index;
    unsigned int opposite_index;
    int quarterTurns;
    std::string name;
    bool is_positive;
    Vec3 direction;
    Direction(const unsigned int& _index, const unsigned int& _opposite_index, const int& _quarterTurns, const std::string& _name, const bool& _is_positive, const Vec3& _direction)
        : index(_index), opposite_index(_opposite_index), quarterTurns(_quarterTurns), name(_name), is_positive(_is_positive), direction(_direction) {}
};
enum DirectionIndices {
    DOWN = 0,
    UP = 1,
    NORTH = 2,
    SOUTH = 3,
    WEST = 4,
    EAST = 5
};
extern Direction Directions[6];
extern Direction HorizontalDirections[4];

// see BoundingBox class in net.minecraft.world.level.levelgen.structure.BoundingBox
struct BoundingBox {
    int minX;
    int minY;
    int minZ;
    int maxX;
    int maxY;
    int maxZ;
    std::string color = "#000000";
    BoundingBox() : minX(0), minY(0), minZ(0), maxX(0), maxY(0), maxZ(0){}
    BoundingBox(const int& _minX, const int& _minY, const int& _minZ, const int& _maxX, const int& _maxY, const int& _maxZ)
        : minX(_minX), minY(_minY), minZ(_minZ), maxX(_maxX), maxY(_maxY), maxZ(_maxZ) {}
    BoundingBox(const Vec3& min, const Vec3& max)
        : minX(min.x), minY(min.y), minZ(min.z), maxX(max.x), maxY(max.y), maxZ(max.z) {}
    BoundingBox(const Pos& position, const Vec3& size)
        : minX(position.x), minY(position.y), minZ(position.z), maxX(position.x + size.x), maxY(position.y + size.y), maxZ(position.z + size.z) {}
    BoundingBox(const Pos& origin, const Vec3& size, Direction orientation);
    BoundingBox(const BoundingBox& copy)
        : minX(copy.minX), minY(copy.minY), minZ(copy.minZ), maxX(copy.maxX), maxY(copy.maxY), maxZ(copy.maxZ), color(copy.color) {}
    BoundingBox(BoundingBox&& move)
        : minX(move.minX), minY(move.minY), minZ(move.minZ), maxX(move.maxX), maxY(move.maxY), maxZ(move.maxZ), color(move.color) {}
    void operator=(const BoundingBox& copy);
    void operator=(BoundingBox&& move);
    static BoundingBox rotated(const Pos& position, const Vec3& offset, const Vec3& size, const Direction& facing);
    static BoundingBox* newRotated(const Pos& position, const Vec3& offset, const Vec3& size, const Direction& facing);
    bool intersects(const BoundingBox& rhs) const;
    void encompass(const BoundingBox& other);
    void move(const Vec3& offset);
    void move(const int& dx, const int& dy, const int& dz);
    int getBlockCountX() const;
    int getBlockCountY() const;
    int getBlockCountZ() const;
    Pos getCenter() const;
    Pos getBottomCenter() const;
    Pos getMin() const;
    Pos getMax() const;
};
int checkIntersections(const std::vector<BoundingBox>& boxes);

#endif// __MINECRAFT_LIB