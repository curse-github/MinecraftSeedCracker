#ifndef __MINECRAFT_LIB
#define __MINECRAFT_LIB

#include <string>
#include <vector>
#include "Lib.h"

struct ChunkPos;
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

// see Direction class in Direction.java
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

// see BlockBox class in BlockBox.java
struct BlockBox {
    int minX;
    int minY;
    int minZ;
    int maxX;
    int maxY;
    int maxZ;
    std::string color = "#000000";
    BlockBox() : minX(0), minY(0), minZ(0), maxX(0), maxY(0), maxZ(0){}
    BlockBox(const int& _minX, const int& _minY, const int& _minZ, const int& _maxX, const int& _maxY, const int& _maxZ)
        : minX(_minX), minY(_minY), minZ(_minZ), maxX(_maxX), maxY(_maxY), maxZ(_maxZ) {}
    BlockBox(const Vec3& min, const Vec3& max)
        : minX(min.x), minY(min.y), minZ(min.z), maxX(max.x), maxY(max.y), maxZ(max.z) {}
    BlockBox(const Pos& position, const Vec3& size)
        : minX(position.x), minY(position.y), minZ(position.z), maxX(position.x + size.x), maxY(position.y + size.y), maxZ(position.z + size.z) {}
    // see StructurePiece.createBox function in StructurePiece.java
    BlockBox(const Pos& origin, const int& width, const int& height, const int& depth, Direction orientation);
    BlockBox(const BlockBox& copy)
        : minX(copy.minX), minY(copy.minY), minZ(copy.minZ), maxX(copy.maxX), maxY(copy.maxY), maxZ(copy.maxZ), color(copy.color) {}
    BlockBox(BlockBox&& move)
        : minX(move.minX), minY(move.minY), minZ(move.minZ), maxX(move.maxX), maxY(move.maxY), maxZ(move.maxZ), color(move.color) {}
    void operator=(const BlockBox& copy);
    void operator=(BlockBox&& move);
    static BlockBox rotated(const Pos& position, const Vec3& offset, const Vec3& size, const Direction& facing);
    static BlockBox* newRotated(const Pos& position, const Vec3& offset, const Vec3& size, const Direction& facing);
    bool intersects(const BlockBox& rhs) const;
    void encompass(const BlockBox& other);
    void move(const Vec3& offset);
    void move(const int& dx, const int& dy, const int& dz);
    void moveX(const int& dx);
    void moveY(const int& dy);
    void moveZ(const int& dz);
    int getBlockCountX() const;
    int getBlockCountY() const;
    int getBlockCountZ() const;
    Pos getCenter() const;
    Pos getBottomCenter() const;
    Pos getMin() const;
    Pos getMax() const;
};
int checkIntersections(const std::vector<BlockBox>& boxes);

#endif// __MINECRAFT_LIB