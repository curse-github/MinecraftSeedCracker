#include "MinecraftLib.h"

#pragma region Pos
ChunkPos Pos::getChunkPos() const {
    return ChunkPos(x >> 4, z >> 4);
}
Pos Pos::getOffsetPos(Pos offset) const {
    return operator+(offset);
}
// see BiomeCoords.fromBlock function in BiomeCoords.java
BiomePos Pos::getBiomePos() const {
    return BiomePos(x >> 2, z >> 2);
}
Vec3 Pos::toVec3() const {
    return Vec3(x, y, z);
}
Vec3D Pos::toVec3D() const {
    return Vec3D(x, y, z);
}
#pragma endregion Pos

#pragma region ChunkPos
ChunkPos& ChunkPos::operator=(const ChunkPos& copy) {
    this->x = copy.x;
    this->z = copy.z;
    return *this;
};
ChunkPos& ChunkPos::operator=(ChunkPos&& move) {
    this->x = move.x;
    this->z = move.z;
    return *this;
};
Pos ChunkPos::getBlockPos() const {
    return Pos(x << 4, 0, z << 4);
}
Pos ChunkPos::getOffsetPos(Pos offset) const {
    return getBlockPos() + offset;
}
#pragma endregion ChunkPos

#pragma region BiomePos
BiomePos& BiomePos::operator=(const BiomePos& copy) {
    this->x = copy.x;
    this->z = copy.z;
    return *this;
};
BiomePos& BiomePos::operator=(BiomePos&& move) {
    this->x = move.x;
    this->z = move.z;
    return *this;
};
// see BiomeCoords.toBlock function in BiomeCoords.java
Pos BiomePos::getBlockPos() const {
    return Pos(x << 2, 0, z << 2);
}
#pragma endregion BiomePos

// see Direction enum in Direction.java
Direction Directions[6] = {
    Direction(0, 1, -1, "down" , false, { 0, 1, 0 }), // 0
    Direction(1, 0, -1, "up"   , true, { 0, -1, 0 }), // 1
    Direction(2, 3,  2, "north", false, { 0, 0, -1 }), // 2
    Direction(3, 2,  0, "south", true, { 0, 0, 1 }), // 3
    Direction(4, 5,  1, "west" , false, { -1, 0, 0 }), // 4
    Direction(5, 4,  3, "east" , true, { 1, 0, 0 }) // 5
};
// see Direction.Type.HORIZTONAL constant in Direction.java
Direction HorizontalDirections[4] = {
    Direction(2, 3, 2, "north", false, { 0, 0, -1 }), // 2
    Direction(5, 4, 3, "east" , true, { 1, 0, 0 }), // 5
    Direction(3, 2, 0, "south", true, { 0, 0, 1 }), // 3
    Direction(4, 5, 1, "west" , false, { -1, 0, 0 }) // 4
};

#pragma region BoundingBox
BoundingBox::BoundingBox(const Pos& origin, const Vec3& size, Direction orientation) {
    *this = BoundingBox::rotated(origin, Vec3(0, 0, 0), size, orientation);
}
void BoundingBox::operator=(const BoundingBox& copy) {
    minX = copy.minX; minY = copy.minY; minZ = copy.minZ;
    maxX = copy.maxX; maxY = copy.maxY; maxZ = copy.maxZ;
    color = copy.color;
}
void BoundingBox::operator=(BoundingBox&& move) {
    minX = move.minX; minY = move.minY; minZ = move.minZ;
    maxX = move.maxX; maxY = move.maxY; maxZ = move.maxZ;
    color = move.color;
}
// see BoundingBox.rotated function in BoundingBox.java
BoundingBox BoundingBox::rotated(const Pos& position, const Vec3& offset, const Vec3& size, const Direction& facing) {
    Vec3 positionVec = Vec3(position.x, position.y, position.z);
    switch (facing.index) {
        case NORTH:
            return BoundingBox(position.x + offset.x, position.y + offset.y, position.z - size.z + 1 + offset.z, position.x + size.x - 1 + offset.x, position.y + size.y - 1 + offset.y, position.z + offset.z);
        case SOUTH:
            return BoundingBox(positionVec + offset, positionVec + size + Vec3(-1, -1, -1) + offset);
        case WEST:
            return BoundingBox(position.x - size.z + 1 + offset.z, position.y + offset.y, position.z + offset.x, position.x + offset.z, position.y + size.y - 1 + offset.y, position.z + size.x - 1 + offset.x);
        case EAST:
            return BoundingBox(positionVec + Vec3(offset.z, offset.y, offset.x), positionVec + Vec3(size.z - 1 + offset.z, size.y - 1 + offset.y, size.x - 1 + offset.x));
        default:
            return BoundingBox();
    }
}
// variation of BoundingBox.rotated function in BoundingBox.java that just returns a pointer
BoundingBox* BoundingBox::newRotated(const Pos& position, const Vec3& offset, const Vec3& size, const Direction& facing) {
    Vec3 positionVec = Vec3(position.x, position.y, position.z);
    switch (facing.index) {
        case NORTH:
            return new BoundingBox(position.x + offset.x, position.y + offset.y, position.z - size.z + 1 + offset.z, position.x + size.x - 1 + offset.x, position.y + size.y - 1 + offset.y, position.z + offset.z);
        case SOUTH:
            return new BoundingBox(positionVec + offset, positionVec + size + Vec3(-1, -1, -1) + offset);
        case WEST:
            return new BoundingBox(position.x - size.z + 1 + offset.z, position.y + offset.y, position.z + offset.x, position.x + offset.z, position.y + size.y - 1 + offset.y, position.z + size.x - 1 + offset.x);
        case EAST:
            return new BoundingBox(positionVec + Vec3(offset.z, offset.y, offset.x), positionVec + Vec3(size.z - 1 + offset.z, size.y - 1 + offset.y, size.x - 1 + offset.x));
        default:
            return new BoundingBox();
    }
}
// see BoundingBox.intersects function in BoundingBox.java
bool BoundingBox::intersects(const BoundingBox& rhs) const {
    return (maxX >= rhs.minX) && (minX <= rhs.maxX) && (maxZ >= rhs.minZ) && (minZ <= rhs.maxZ) && (maxY >= rhs.minY) && (minY <= rhs.maxY);
}
//  see BoundingBox.encompass function in BoundingBox.java
void BoundingBox::encompass(const BoundingBox& other) {
    minX = std::min(minX, other.minX);
    minY = std::min(minY, other.minY);
    minZ = std::min(minZ, other.minZ);
    maxX = std::max(maxX, other.maxX);
    maxY = std::max(maxY, other.maxY);
    maxZ = std::max(maxZ, other.maxZ);
}
//  see BoundingBox.move function in BoundingBox.java
void BoundingBox::move(const Vec3& offset) {
    move(offset.x, offset.y, offset.z);
}
//  see BoundingBox.move function in BoundingBox.java
void BoundingBox::move(const int& dx, const int& dy, const int& dz) {
    minX += dx;
    minY += dy;
    minZ += dz;
    maxX += dx;
    maxY += dy;
    maxZ += dz;
}
// see BoundingBox.getBlockCountX function in BoundingBox.java
int BoundingBox::getBlockCountX() const {
    return maxX - minX + 1;
}
// see BoundingBox.getBlockCountY function in BoundingBox.java
int BoundingBox::getBlockCountY() const {
    return maxY - minY + 1;
}
// see BoundingBox.getBlockCountZ function in BoundingBox.java
int BoundingBox::getBlockCountZ() const {
    return maxZ - minZ + 1;
}
// see BoundingBox.getCenter function in BoundingBox.java
Pos BoundingBox::getCenter() const {
    return Pos(minX + (getBlockCountX() / 2), minY + (getBlockCountY() / 2), minZ + (getBlockCountZ() / 2));
}
Pos BoundingBox::getBottomCenter() const {
    return Pos(minX + (getBlockCountX() / 2), minY, minZ + (getBlockCountZ() / 2));
}
Pos BoundingBox::getMin() const {
    return { minX, minY, minZ };
}
Pos BoundingBox::getMax() const {
    return { maxX, maxY, maxZ };
}
int checkIntersections(const std::vector<BoundingBox>& boxes) {
    size_t size = boxes.size();
    int num = 0;
    for (size_t i = 0; i < size; i++) {
        for (size_t j = i; j < size; j++) {
            if (boxes[i].intersects(boxes[j])) num++;
        }
    }
    return num;
}
#pragma endregion BoundingBox