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
    Direction(0, 1, -1, "down", false, { 0, 1, 0 }), // 0
    Direction(1, 0, -1, "up", true, { 0, -1, 0 }), // 1
    Direction(2, 3, 2, "north", false, { 0, 0, -1 }), // 2
    Direction(3, 2, 0, "south", true, { 0, 0, 1 }), // 3
    Direction(4, 5, 1, "west", false, { -1, 0, 0 }), // 4
    Direction(5, 4, 3, "east", true, { 1, 0, 0 }) // 5
};
// see Direction.Type.HORIZONAL constant in Direction.java
Direction HorizontalDirections[4] = {
    Direction(2, 3, 2, "north", false, { 0, 0, -1 }), // 2
    Direction(5, 4, 3, "east", true, { 1, 0, 0 }), // 5
    Direction(3, 2, 0, "south", true, { 0, 0, 1 }), // 3
    Direction(4, 5, 1, "west", false, { -1, 0, 0 }) // 4
};

#pragma region BlockBox
BlockBox::BlockBox(const Pos& origin, const int& width, const int& height, const int& depth, Direction orientation)
    : minX(origin.x), minY(origin.y), minZ(origin.z), maxX(origin.x - 1), maxY(origin.y + height - 1), maxZ(origin.z - 1) {
    if (orientation.direction.z != 0) {
        maxX += width;
        maxZ += depth;
    } else {
        maxX += depth;
        maxZ += width;
    }
}
void BlockBox::operator=(const BlockBox& copy) {
    minX = copy.minX; minY = copy.minY; minZ = copy.minZ;
    maxX = copy.maxX; maxY = copy.maxY; maxZ = copy.maxZ;
    color = copy.color;
}
void BlockBox::operator=(BlockBox&& move) {
    minX = move.minX; minY = move.minY; minZ = move.minZ;
    maxX = move.maxX; maxY = move.maxY; maxZ = move.maxZ;
    color = move.color;
}
// see BlockBox.rotated function in BlockBox.java
BlockBox BlockBox::rotated(const Pos& position, const Vec3& offset, const Vec3& size, const Direction& facing) {
    Vec3 positionVec = Vec3(position.x, position.y, position.z);
    switch (facing.index) {
        case NORTH:
            return BlockBox(position.x + offset.x, position.y + offset.y, position.z - size.z + 1 + offset.z, position.x + size.x - 1 + offset.x, position.y + size.y - 1 + offset.y, position.z + offset.z);
        case SOUTH:
            return BlockBox(positionVec + offset, positionVec + size + Vec3(-1, -1, -1) + offset);
        case WEST:
            return BlockBox(position.x - size.z + 1 + offset.z, position.y + offset.y, position.z + offset.x, position.x + offset.z, position.y + size.y - 1 + offset.y, position.z + size.x - 1 + offset.x);
        case EAST:
            return BlockBox(positionVec + Vec3(offset.z, offset.y, offset.x), positionVec + Vec3(size.z - 1 + offset.z, size.y - 1 + offset.y, size.x - 1 + offset.x));
        default:
            return BlockBox();
    }
}
// variation of BlockBox.rotated function in BlockBox.java that just returns a pointer
BlockBox* BlockBox::newRotated(const Pos& position, const Vec3& offset, const Vec3& size, const Direction& facing) {
    Vec3 positionVec = Vec3(position.x, position.y, position.z);
    switch (facing.index) {
        case NORTH:
            return new BlockBox(position.x + offset.x, position.y + offset.y, position.z - size.z + 1 + offset.z, position.x + size.x - 1 + offset.x, position.y + size.y - 1 + offset.y, position.z + offset.z);
        case SOUTH:
            return new BlockBox(positionVec + offset, positionVec + size + Vec3(-1, -1, -1) + offset);
        case WEST:
            return new BlockBox(position.x - size.z + 1 + offset.z, position.y + offset.y, position.z + offset.x, position.x + offset.z, position.y + size.y - 1 + offset.y, position.z + size.x - 1 + offset.x);
        case EAST:
            return new BlockBox(positionVec + Vec3(offset.z, offset.y, offset.x), positionVec + Vec3(size.z - 1 + offset.z, size.y - 1 + offset.y, size.x - 1 + offset.x));
        default:
            return new BlockBox();
    }
}
// see BlockBox.intersects function in BlockBox.java
bool BlockBox::intersects(const BlockBox& rhs) const {
    return (maxX >= rhs.minX) && (minX <= rhs.maxX) && (maxZ >= rhs.minZ) && (minZ <= rhs.maxZ) && (maxY >= rhs.minY) && (minY <= rhs.maxY);
}
//  see BlockBox.encompass function in BlockBox.java
void BlockBox::encompass(const BlockBox& other) {
    minX = myMin(minX, other.minX);
    minY = myMin(minY, other.minY);
    minZ = myMin(minZ, other.minZ);
    maxX = myMax(maxX, other.maxX);
    maxY = myMax(maxY, other.maxY);
    maxZ = myMax(maxZ, other.maxZ);
}
//  see BlockBox.move function in BlockBox.java
void BlockBox::move(const Vec3& offset) {
    move(offset.x, offset.y, offset.z);
}
//  see BlockBox.move function in BlockBox.java
void BlockBox::move(const int& dx, const int& dy, const int& dz) {
    minX += dx;
    minY += dy;
    minZ += dz;
    maxX += dx;
    maxY += dy;
    maxZ += dz;
}
void BlockBox::moveX(const int& dx) {
    minX += dx;
    maxX += dx;
}
void BlockBox::moveY(const int& dy) {
    minY += dy;
    maxY += dy;
}
void BlockBox::moveZ(const int& dz) {
    minZ += dz;
    maxZ += dz;
}
// see BlockBox.getBlockCountX function in BlockBox.java
int BlockBox::getBlockCountX() const {
    return maxX - minX + 1;
}
// see BlockBox.getBlockCountY function in BlockBox.java
int BlockBox::getBlockCountY() const {
    return maxY - minY + 1;
}
// see BlockBox.getBlockCountZ function in BlockBox.java
int BlockBox::getBlockCountZ() const {
    return maxZ - minZ + 1;
}
// see BlockBox.getCenter function in BlockBox.java
Pos BlockBox::getCenter() const {
    return Pos(minX + (getBlockCountX() / 2), minY + (getBlockCountY() / 2), minZ + (getBlockCountZ() / 2));
}
Pos BlockBox::getBottomCenter() const {
    return Pos(minX + (getBlockCountX() / 2), minY, minZ + (getBlockCountZ() / 2));
}
Pos BlockBox::getMin() const {
    return { minX, minY, minZ };
}
Pos BlockBox::getMax() const {
    return { maxX, maxY, maxZ };
}
int checkIntersections(const std::vector<BlockBox>& boxes) {
    size_t size = boxes.size();
    int num = 0;
    for (size_t i = 0; i < size; i++) {
        for (size_t j = i; j < size; j++) {
            if (boxes[i].intersects(boxes[j])) num++;
        }
    }
    return num;
}
#pragma endregion BlockBox