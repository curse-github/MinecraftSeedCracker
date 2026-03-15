#include "StrongholdStructure.h"

std::vector<std::string> pieceNames = {
    "PieceTypeStraight",
    "PieceTypePrisonHall",
    "PieceTypeLeftTurn",
    "PieceTypeRightTurn",
    "PieceTypeRoomCrossing",
    "PieceTypeStraightStairsDown",
    "PieceTypeStairsDown",
    "PieceTypeFiveCrossing",
    "PieceTypeChestCorridor",
    "PieceTypeLibrary",
    "PieceTypePortalRoom"
};

// StrongholdPieces.STRONGHOLD_PIECE_WEIGHTS variable in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
std::vector<PieceWeight> STRONGHOLD_PIECE_WEIGHTS = {
    PieceWeight(PieceTypeStraight, 40, 0, 0),
    PieceWeight(PieceTypePrisonHall, 5, 5, 0),
    PieceWeight(PieceTypeLeftTurn, 20, 0, 0),
    PieceWeight(PieceTypeRightTurn, 20, 0, 0),
    PieceWeight(PieceTypeRoomCrossing, 10, 6, 0),
    PieceWeight(PieceTypeStraightStairsDown, 5, 5, 0),
    PieceWeight(PieceTypeStairsDown, 5, 5, 0),
    PieceWeight(PieceTypeFiveCrossing, 5, 4, 0),
    PieceWeight(PieceTypeChestCorridor, 5, 4, 0),
    PieceWeight(PieceTypeLibrary, 10, 2, 4),
    PieceWeight(PieceTypePortalRoom, 20, 1, 5)
};

// StrongholdPieces.currentPieces variable in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
std::vector<PieceWeight*> currentPieces;

// StrongholdPieces.imposedPiece variable in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
PieceWeight* imposedPiece = nullptr;

// StrongholdPieces.totalWeight variable in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
int totalWeight = 0;

void resetPieces() {
    currentPieces.clear();
    for (int i = 0; i < STRONGHOLD_PIECE_WEIGHTS.size(); i++) {
        STRONGHOLD_PIECE_WEIGHTS[i].placeCount = 0;
        currentPieces.push_back(&STRONGHOLD_PIECE_WEIGHTS[i]);
    }
    imposedPiece = nullptr;
}

// StrongholdPieces.updatePieceWeight function in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
bool updatePieceWeight() {
    bool hasAnyPieces = false;
    totalWeight = 0;
    for (size_t i = 0; i < currentPieces.size(); i++) {
        PieceWeight* piece = currentPieces[i];
        if (piece->maxPlaceCount > 0 && piece->placeCount < piece->maxPlaceCount) {
            hasAnyPieces = true;
        }
        totalWeight += piece->weight;
    }
    return hasAnyPieces;
}

// StrongholdPieces.findAndCreatePieceFactory function in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
OwningNullable<StrongholdPiece> findAndCreatePieceFactory(const unsigned int& pieceType, LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, Direction direction, int genDepth) {
    switch (pieceType) {
        case PieceTypeStraight: {
            return Straight::createPiece(rand, _pieces, position, direction, genDepth);
        } case PieceTypePrisonHall: {
            return PrisonHall::createPiece(rand, _pieces, position, direction, genDepth);
        } case PieceTypeLeftTurn: {
            return LeftTurn::createPiece(rand, _pieces, position, direction, genDepth);
        } case PieceTypeRightTurn: {
            return RightTurn::createPiece(rand, _pieces, position, direction, genDepth);
        } case PieceTypeRoomCrossing: {
            return RoomCrossing::createPiece(rand, _pieces, position, direction, genDepth);
        } case PieceTypeStraightStairsDown: {
            return StraightStairsDown::createPiece(rand, _pieces, position, direction, genDepth);
        } case PieceTypeStairsDown: {
            return StairsDown::createPiece(rand, _pieces, position, direction, genDepth);
        } case PieceTypeFiveCrossing: {
            return FiveCrossing::createPiece(rand, _pieces, position, direction, genDepth);
        } case PieceTypeChestCorridor: {
            return ChestCorridor::createPiece(rand, _pieces, position, direction, genDepth);
        } case PieceTypeLibrary: {
            return Library::createPiece(rand, _pieces, position, direction, genDepth);
        } case PieceTypePortalRoom: {
            return PortalRoom::createPiece(rand, _pieces, position, direction, genDepth);
        }
    }
    return OwningNullable<StrongholdPiece>();
}

// StrongholdPieces.generatePieceFromSmallDoor function in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
OwningNullable<StrongholdPiece> generatePieceFromSmallDoor(StartPiece& start, LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, Direction direction, int genDepth) {
    if (!updatePieceWeight()) return OwningNullable<StrongholdPiece>();
    if (imposedPiece != nullptr) {
        OwningNullable piece = findAndCreatePieceFactory(imposedPiece->pieceClass, rand, _pieces, position, direction, genDepth);
        if (piece.hasValue) {
            BoundingBox& b = piece.getValue().boundingBox;
            /*std::cout << "accepted " << pieceNames[imposedPiece->pieceClass]
                    << " box=(" << b.minX << "," << b.minY << "," << b.minZ
                    << ")->(" << b.maxX << "," << b.maxY << "," << b.maxZ << ")\n";*/
            imposedPiece = nullptr;
            return piece;
        }
        imposedPiece = nullptr;
    }
    for (unsigned int numAttempts = 0; numAttempts < 5; numAttempts++) {
        int weightSelection = rand.nextInt(totalWeight);
        for (size_t i = 0; i < currentPieces.size(); i++) {
            PieceWeight* piece = currentPieces[i];
            weightSelection -= piece->weight;
            if (weightSelection >= 0) continue;
            if (!piece->doPlace(genDepth) || (start.previousPiece == piece)) {
                /*if (!piece->doPlace(genDepth))
                    std::cout << "reject doPlace depth=" << genDepth
                        << " piece=" << pieceNames[piece->pieceClass]
                        << " prev=" << (start.previousPiece ? pieceNames[start.previousPiece->pieceClass] : "null")
                        << " pos=(" << position.x << "," << position.y << "," << position.z << ")"
                        << " dir=" << direction.index
                        << "\n";
                else
                    std::cout << "reject previousPiece depth=" << genDepth
                        << " piece=" << pieceNames[piece->pieceClass]
                        << " prev=" << (start.previousPiece ? pieceNames[start.previousPiece->pieceClass] : "null")
                        << " pos=(" << position.x << "," << position.y << "," << position.z << ")"
                        << " dir=" << direction.index
                        << "\n";*/
                break;
            }
            OwningNullable<StrongholdPiece> strongholdPiece = findAndCreatePieceFactory(piece->pieceClass, rand, _pieces, position, direction, genDepth);
            if (!strongholdPiece.hasValue)  {
                /*std::cout << "reject createPiece depth=" << genDepth
                    << " piece=" << pieceNames[piece->pieceClass]
                    << " prev=" << (start.previousPiece ? pieceNames[start.previousPiece->pieceClass] : "null")
                    << " pos=(" << position.x << "," << position.y << "," << position.z << ")"
                    << " dir=" << direction.index
                    << "\n";*/
                continue;
            }
            piece->placeCount++;
            start.previousPiece = piece;
            if (!piece->isValid())
                currentPieces.erase(currentPieces.cbegin() + i);
            BoundingBox& b = strongholdPiece.getValue().boundingBox;
            /*std::cout << "accepted " << pieceNames[piece->pieceClass]
                    << " box=(" << b.minX << "," << b.minY << "," << b.minZ
                    << ")->(" << b.maxX << "," << b.maxY << "," << b.maxZ << ")\n";*/
            return strongholdPiece;
        }
    }
    OwningNullable boundingBox = FillerCorridor::createPiece(rand, _pieces, position, direction);
    if (boundingBox.hasValue && boundingBox.getValue().minY > 1)
        return OwningNullable<StrongholdPiece>(new FillerCorridor(genDepth, boundingBox.getValue(), direction));
    return OwningNullable<StrongholdPiece>();
}

// StrongholdPieces.generateAndAddPiece function in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
NonOwningNullable<StructurePiece> generateAndAddPiece(StructurePiece& start, LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& genDepth) {
    StartPiece& _start = (StartPiece&)start;
    if (genDepth > 50) return NonOwningNullable<StructurePiece>();
    if ((std::abs(position.x - start.boundingBox.minX) > 112) || (std::abs(position.z - start.boundingBox.minZ) > 112)) return NonOwningNullable<StructurePiece>();
    OwningNullable newPiece = generatePieceFromSmallDoor(_start, rand, _pieces, position, direction, genDepth + 1);
    if (newPiece.hasValue) {
        StrongholdPiece* piece = newPiece.takeValue();
        _pieces.push_back(piece);
        _start.pendingChildren.push_back(piece);
        return NonOwningNullable(piece);
    } else
        return NonOwningNullable<StructurePiece>();
}

// StructurePiece.StructurePiece.addChildren function in source/net/minecraft/world/level/levelgen/structure/StructurePiece.java
void StructurePiece::addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand) {
}

// StrongholdPieces.StrongholdPiece class in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
#pragma region StrongholdPiece

// TODO: generateSmallDoor

// StrongholdPieces.StrongholdPiece.randomSmallDoor function in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
SmallDoorType randomSmallDoor(LCG& rand) {
    int rnd = rand.nextInt(5);
    switch (rnd) {
        case 0:
        case 1:
        default:
            return SmallDoorType::OPENING;
        case 2:
            return SmallDoorType::WOOD_DOOR;
        case 3:
            return SmallDoorType::GRATES;
        case 4:
            return SmallDoorType::IRON_DOOR;
    }
}

// StrongholdPieces.StrongholdPiece.generateSmallDoorChildForward function in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
NonOwningNullable<StructurePiece> StrongholdPiece::generateSmallDoorChildForward(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand, const int& xOff, const int& yOff) {
    switch (direction.index) {
        case NORTH:
            return generateAndAddPiece(start, rand, _pieces, boundingBox.getMin() + Pos(xOff, yOff, -1), direction, genDepth);
        case SOUTH:
            return generateAndAddPiece(start, rand, _pieces, Pos(boundingBox.minX + xOff, boundingBox.minY + yOff,  boundingBox.maxZ + 1), direction, genDepth);
        case WEST:
            return generateAndAddPiece(start, rand, _pieces, boundingBox.getMin() + Pos(-1, yOff, xOff), direction, genDepth);
        case EAST:
            return generateAndAddPiece(start, rand, _pieces, Pos(boundingBox.maxX + 1, boundingBox.minY + yOff, boundingBox.minZ + xOff), direction, genDepth);
        default:
            return NonOwningNullable<StructurePiece>();
    }
}

// StrongholdPieces.StrongholdPiece.generateSmallDoorChildLeft function in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
NonOwningNullable<StructurePiece> StrongholdPiece::generateSmallDoorChildLeft(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand, const int& yOff, const int& zOff) {
    switch (direction.index) {
        case NORTH:
        case SOUTH:
            return generateAndAddPiece(start, rand, _pieces, boundingBox.getMin() + Pos(-1, yOff, zOff), Directions[WEST], genDepth);
        case WEST:
        case EAST:
            return generateAndAddPiece(start, rand, _pieces, boundingBox.getMin() + Pos(zOff, yOff, -1), Directions[NORTH], genDepth);
        default:
            return NonOwningNullable<StructurePiece>();
    }
}

// StrongholdPieces.StrongholdPiece.generateSmallDoorChildRight function in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
NonOwningNullable<StructurePiece> StrongholdPiece::generateSmallDoorChildRight(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand, const int& yOff, const int& zOff) {
    switch (direction.index) {
        case NORTH:
        case SOUTH:
            return generateAndAddPiece(start, rand, _pieces, Pos(boundingBox.maxX + 1, boundingBox.minY + yOff, boundingBox.minZ + zOff), Directions[EAST], genDepth);
        case WEST:
        case EAST:
            return generateAndAddPiece(start, rand, _pieces, Pos(boundingBox.minX + zOff, boundingBox.minY + yOff, boundingBox.maxZ + 1), Directions[SOUTH], genDepth);
        default:
            return NonOwningNullable<StructurePiece>();
    }
}

// StrongholdPieces.StrongholdPiece.addChildren function in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
void StrongholdPiece::addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand) {
}

// StrongholdPieces.StrongholdPiece.isOkBox function in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
bool StrongholdPiece::isOkBox(BoundingBox boundingBox) {
    return boundingBox.minY > 10;
}

#pragma endregion// StrongholdPiece

// StructurePieceAccessor.findCollisionPiece function in net.minecraft.world.level.levelgen.structure.StructurePieceAccessor.java
BoundingBox* findCollisionPiece(const BoundingBox& box, std::vector<StructurePiece*>& _pieces) {
    for (size_t i = 0; i < _pieces.size(); i++) {
        if (_pieces[i]->boundingBox.intersects(box)) return &(_pieces[i]->boundingBox);
    }
    return nullptr;
}


// StrongholdPieces.FillerCorridor class in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
#pragma region FillerCorridor

FillerCorridor::FillerCorridor(const int& _genDepth, const BoundingBox& _boundingBox, const Direction& _direction) : StrongholdPiece(_genDepth, _boundingBox, _direction) {
    steps = ((direction.index == NORTH) || (direction.index == SOUTH)) ? (boundingBox.maxZ - boundingBox.minZ + 1) : (boundingBox.maxX - boundingBox.minX + 1);
}
OwningNullable<BoundingBox> FillerCorridor::createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction) {
    BoundingBox box = BoundingBox::rotated(position, Vec3(-1, -1, 0), Vec3(5, 5, 4), direction);
    BoundingBox* collisionPiece = findCollisionPiece(box, _pieces);
    if (collisionPiece == nullptr) return OwningNullable<BoundingBox>();
    if (collisionPiece->minY == box.minY) {
        for (int depth = 2; depth >= 1; depth--) {
            BoundingBox attempt = BoundingBox::rotated(position, Vec3(-1, -1, 0), Vec3(5, 5, depth), direction);
            if (collisionPiece->intersects(attempt)) continue;
            OwningNullable returnVal = OwningNullable(BoundingBox::newRotated(position, Vec3(-1, -1, 0), Vec3(5, 5, depth + 1), direction));
            returnVal.getValue().color = "#0000ff";
            return returnVal;
        }
    }
    return OwningNullable<BoundingBox>();
}

#pragma endregion// FillerCorridor

// StrongholdPieces.StairsDown class in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
#pragma region StairsDown

// StrongholdPieces.StairsDown.StairsDown function in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
StairsDown::StairsDown(const int& _genDepth, const Pos& position, const Direction& _direction) : StrongholdPiece(_genDepth, BoundingBox({ position.x, 64, position.z }, {5, 11, 5}, _direction), _direction), isStart(true) {
    entryDoor = SmallDoorType::OPENING;
}
// StrongholdPieces.StairsDown.StairsDown function in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
StairsDown::StairsDown(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction) : StrongholdPiece(_genDepth, _boundingBox, _direction), isStart(false) {
    entryDoor = randomSmallDoor(rand);
}
// StrongholdPieces.StairsDown.StairsDown function in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
void StairsDown::addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand) {
    if (isStart)
        imposedPiece = &STRONGHOLD_PIECE_WEIGHTS[PieceTypeFiveCrossing];
    generateSmallDoorChildForward(start, _pieces, rand, 1, 1);
}
// StrongholdPieces.StairsDown.StairsDown function in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
OwningNullable<StairsDown> StairsDown::createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& genDepth) {
    BoundingBox box = BoundingBox::rotated(position, Vec3(-1, -7, 0), Vec3(5, 11, 5), direction);
    // if (!StairsDown::isOkBox(box)) std::cout << "    Failed to create \"StairsDown\" because not !isOkBox\n";
    // if (findCollisionPiece(box, _pieces) != nullptr) std::cout << "    Failed to create \"StairsDown\" because collision\n";
    if (!StairsDown::isOkBox(box) || (findCollisionPiece(box, _pieces) != nullptr))
        return OwningNullable<StairsDown>();
    box.color = "#00ff00";
    return OwningNullable(new StairsDown(genDepth, rand, box, direction));
}

#pragma endregion// StairsDown

// StrongholdPieces.StartPiece class in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
#pragma region StartPiece

StartPiece::StartPiece(LCG& rand, const Pos& position) : StairsDown(0, position, getRandomHorizontalDirection(rand)) {
    boundingBox.color = "#00ff00";
}

#pragma endregion// StartPiece

// StrongholdPieces.Straight class in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
#pragma region Straight

Straight::Straight(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction) : StrongholdPiece(_genDepth, _boundingBox, _direction) {
    entryDoor = randomSmallDoor(rand);
    leftExitExists = (rand.nextInt(2) == 0);
    rightExitExists = (rand.nextInt(2) == 0);
}
void Straight::addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand) {
    generateSmallDoorChildForward(start, _pieces, rand, 1, 1);
    if (leftExitExists)
        generateSmallDoorChildLeft(start, _pieces, rand, 1, 2);
    if (rightExitExists)
        generateSmallDoorChildRight(start, _pieces, rand, 1, 2);
}
OwningNullable<Straight> Straight::createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& genDepth) {
    BoundingBox box = BoundingBox::rotated(position, Vec3(-1, -1, 0), Vec3(5, 5, 7), direction);
    // if (!Straight::isOkBox(box)) std::cout << "    Failed to create \"Straight\" because not !isOkBox\n";
    // if (findCollisionPiece(box, _pieces) != nullptr) std::cout << "    Failed to create \"Straight\" because collision\n";
    if (!Straight::isOkBox(box) || (findCollisionPiece(box, _pieces) != nullptr))
        return OwningNullable<Straight>();
    box.color = "#0000ff";
    return OwningNullable(new Straight(genDepth, rand, box, direction));
}

#pragma endregion// Straight

// StrongholdPieces.ChestCorridor class in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
#pragma region ChestCorridor

ChestCorridor::ChestCorridor(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction) : StrongholdPiece(_genDepth, _boundingBox, _direction) {
    entryDoor = randomSmallDoor(rand);
}
void ChestCorridor::addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand) {
    generateSmallDoorChildForward(start, _pieces, rand, 1, 1);
}
OwningNullable<ChestCorridor> ChestCorridor::createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& genDepth) {
    BoundingBox box = BoundingBox::rotated(position, Vec3(-1, -1, 0), Vec3(5, 5, 7), direction);
    // if (!ChestCorridor::isOkBox(box)) std::cout << "    Failed to create \"ChestCorridor\" because not !isOkBox\n";
    // if (findCollisionPiece(box, _pieces) != nullptr) std::cout << "    Failed to create \"ChestCorridor\" because collision\n";
    if (!ChestCorridor::isOkBox(box) || (findCollisionPiece(box, _pieces) != nullptr))
        return OwningNullable<ChestCorridor>();
    box.color = "#804000";
    return OwningNullable(new ChestCorridor(genDepth, rand, box, direction));
}

#pragma endregion// ChestCorridor

// StrongholdPieces.StraightStairsDown class in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
#pragma region StraightStairsDown

StraightStairsDown::StraightStairsDown(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction) : StrongholdPiece(_genDepth, _boundingBox, _direction) {
    entryDoor = randomSmallDoor(rand);
}
void StraightStairsDown::addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand) {
    generateSmallDoorChildForward(start, _pieces, rand, 1, 1);
}
OwningNullable<StraightStairsDown> StraightStairsDown::createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& genDepth) {
    BoundingBox box = BoundingBox::rotated(position, Vec3(-1, -7, 0), Vec3(5, 11, 8), direction);
    // if (!StraightStairsDown::isOkBox(box)) std::cout << "    Failed to create \"StraightStairsDown\" because not !isOkBox\n";
    // if (findCollisionPiece(box, _pieces) != nullptr) std::cout << "    Failed to create \"StraightStairsDown\" because collision\n";
    if (!StraightStairsDown::isOkBox(box) || (findCollisionPiece(box, _pieces) != nullptr))
        return OwningNullable<StraightStairsDown>();
    box.color = "#00ff00";
    return OwningNullable(new StraightStairsDown(genDepth, rand, box, direction));
}

#pragma endregion// StraightStairsDown

// StrongholdPieces.Turn class in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
#pragma region Turn

void Turn::addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand) {
}

#pragma endregion// Turn

// StrongholdPieces.LeftTurn class in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
#pragma region LeftTurn

LeftTurn::LeftTurn(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction) : Turn(_genDepth, _boundingBox, _direction) {
    entryDoor = randomSmallDoor(rand);
}
void LeftTurn::addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand) {
    if ((direction.index == NORTH) || (direction.index == EAST))
        generateSmallDoorChildLeft(start, _pieces, rand, 1, 1);
    else
        generateSmallDoorChildRight(start, _pieces, rand, 1, 1);
}
OwningNullable<LeftTurn> LeftTurn::createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& genDepth) {
    BoundingBox box = BoundingBox::rotated(position, Vec3(-1, -1, 0), Vec3(5, 5, 5), direction);
    // if (!LeftTurn::isOkBox(box)) std::cout << "    Failed to create \"LeftTurn\" because not !isOkBox\n";
    // if (findCollisionPiece(box, _pieces) != nullptr) std::cout << "    Failed to create \"LeftTurn\" because collision\n";
    if (!LeftTurn::isOkBox(box) || (findCollisionPiece(box, _pieces) != nullptr)) {
        return OwningNullable<LeftTurn>();
    }
    return OwningNullable(new LeftTurn(genDepth, rand, box, direction));
}

#pragma endregion// LeftTurn

// StrongholdPieces.RightTurn class in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
#pragma region RightTurn

RightTurn::RightTurn(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction) : Turn(_genDepth, _boundingBox, _direction) {
    entryDoor = randomSmallDoor(rand);
}
void RightTurn::addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand) {
    if ((direction.index == NORTH) || (direction.index == EAST))
        generateSmallDoorChildRight(start, _pieces, rand, 1, 1);
    else
        generateSmallDoorChildLeft(start, _pieces, rand, 1, 1);
}
OwningNullable<RightTurn> RightTurn::createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& genDepth) {
    BoundingBox box = BoundingBox::rotated(position, Vec3(-1, -1, 0), Vec3(5, 5, 5), direction);
    // if (!RightTurn::isOkBox(box)) std::cout << "    Failed to create \"RightTurn\" because not !isOkBox\n";
    // if (findCollisionPiece(box, _pieces) != nullptr) std::cout << "    Failed to create \"RightTurn\" because collision\n";
    if (!RightTurn::isOkBox(box) || (findCollisionPiece(box, _pieces) != nullptr))
        return OwningNullable<RightTurn>();
    return OwningNullable(new RightTurn(genDepth, rand, box, direction));
}

#pragma endregion// RightTurn

// StrongholdPieces.RoomCrossing class in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
#pragma region RoomCrossing

RoomCrossing::RoomCrossing(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction) : StrongholdPiece(_genDepth, _boundingBox, _direction) {
    entryDoor = randomSmallDoor(rand);
    roomType = rand.nextInt(5);
}
void RoomCrossing::addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand) {
    generateSmallDoorChildForward(start, _pieces, rand, 4, 1);
    generateSmallDoorChildLeft(start, _pieces, rand, 1, 4);
    generateSmallDoorChildRight(start, _pieces, rand, 1, 4);
}
OwningNullable<RoomCrossing> RoomCrossing::createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& genDepth) {
    BoundingBox box = BoundingBox::rotated(position, Vec3(-4, -1, 0), Vec3(11, 7, 11), direction);
    // if (!RoomCrossing::isOkBox(box)) std::cout << "    Failed to create \"RoomCrossing\" because not !isOkBox\n";
    // if (findCollisionPiece(box, _pieces) != nullptr) std::cout << "    Failed to create \"RoomCrossing\" because collision\n";
    if (!RoomCrossing::isOkBox(box) || (findCollisionPiece(box, _pieces) != nullptr))
        return OwningNullable<RoomCrossing>();
    box.color = "#ff0000";
    return OwningNullable(new RoomCrossing(genDepth, rand, box, direction));
}

#pragma endregion// RoomCrossing

// StrongholdPieces.PrisonHall class in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
#pragma region PrisonHall

PrisonHall::PrisonHall(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction) : StrongholdPiece(_genDepth, _boundingBox, _direction) {
    entryDoor = randomSmallDoor(rand);
}
void PrisonHall::addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand) {
    generateSmallDoorChildForward(start, _pieces, rand, 1, 1);
}
OwningNullable<PrisonHall> PrisonHall::createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& genDepth) {
    BoundingBox box = BoundingBox::rotated(position, Vec3(-1, -1, 0), Vec3(9, 5, 11), direction);
    // if (!PrisonHall::isOkBox(box)) std::cout << "    Failed to create \"PrisonHall\" because not !isOkBox\n";
    // if (findCollisionPiece(box, _pieces) != nullptr) std::cout << "    Failed to create \"PrisonHall\" because collision\n";
    if (!PrisonHall::isOkBox(box) || (findCollisionPiece(box, _pieces) != nullptr))
        return OwningNullable<PrisonHall>();
    return OwningNullable(new PrisonHall(genDepth, rand, box, direction));
}

#pragma endregion// PrisonHall

// StrongholdPieces.Library class in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
#pragma region Library

Library::Library(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction) : StrongholdPiece(_genDepth, _boundingBox, _direction) {
    entryDoor = randomSmallDoor(rand);
    isTall = (boundingBox.maxY - boundingBox.minY + 1) > 6;
}
OwningNullable<Library> Library::createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& genDepth) {
    BoundingBox box = BoundingBox::rotated(position, Vec3(-4, -1, 0), Vec3(14, 11, 15), direction);
    // if (!Library::isOkBox(box)) std::cout << "    Failed to create \"Library\" because not !isOkBox\n";
    // if (findCollisionPiece(box, _pieces) != nullptr) std::cout << "    Failed to create \"Library\" because collision\n";
    if (!Library::isOkBox(box) || (findCollisionPiece(box, _pieces) != nullptr)) {
        box = BoundingBox::rotated(position, Vec3(-4, -1, 0), Vec3(14, 6, 15), direction);
        if (!Library::isOkBox(box) || (findCollisionPiece(box, _pieces) != nullptr))
            return OwningNullable<Library>();
    }
    box.color = "#804000";
    return OwningNullable(new Library(genDepth, rand, box, direction));
}

#pragma endregion// Library

// StrongholdPieces.FiveCrossing class in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
#pragma region FiveCrossing

FiveCrossing::FiveCrossing(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction) : StrongholdPiece(_genDepth, _boundingBox, _direction) {
    entryDoor = randomSmallDoor(rand);
    leftLow = rand.nextBoolean();
    leftHigh = rand.nextBoolean();
    rightLow = rand.nextBoolean();
    rightHigh = rand.nextInt(3) > 0;
}
void FiveCrossing::addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand) {
    int zOffA = 3;
    int zOffB = 5;
    if ((direction.index == WEST) || (direction.index == NORTH)) {
        zOffA = 8-zOffA;
        zOffB = 8-zOffB;
    }
    generateSmallDoorChildForward(start, _pieces, rand, 5, 1);
    if (leftLow)
        generateSmallDoorChildLeft(start, _pieces, rand, zOffA, 1);
    if (leftHigh)
        generateSmallDoorChildLeft(start, _pieces, rand, zOffB, 7);
    if (rightLow)
        generateSmallDoorChildRight(start, _pieces, rand, zOffA, 1);
    if (rightHigh)
        generateSmallDoorChildRight(start, _pieces, rand, zOffB, 7);
}
OwningNullable<FiveCrossing> FiveCrossing::createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& genDepth) {
    BoundingBox box = BoundingBox::rotated(position, Vec3(-4, -3, 0), Vec3(10, 9, 11), direction);
    // if (!FiveCrossing::isOkBox(box)) std::cout << "    Failed to create \"FiveCrossing\" because not !isOkBox\n";
    // if (findCollisionPiece(box, _pieces) != nullptr) std::cout << "    Failed to create \"FiveCrossing\" because collision\n";
    if (!FiveCrossing::isOkBox(box) || (findCollisionPiece(box, _pieces) != nullptr))
        return OwningNullable<FiveCrossing>();
    box.color = "#ff0000";
    return OwningNullable(new FiveCrossing(genDepth, rand, box, direction));
}

#pragma endregion// FiveCrossing

// StrongholdPieces.PortalRoom class in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
#pragma region PortalRoom

PortalRoom::PortalRoom(const int& _genDepth, const BoundingBox& _boundingBox, const Direction& _direction) : StrongholdPiece(_genDepth, _boundingBox, _direction) {}
void PortalRoom::addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand) {
    ((StartPiece&)start).portalRoom = this;
}
OwningNullable<PortalRoom> PortalRoom::createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& genDepth) {
    BoundingBox box = BoundingBox::rotated(position, Vec3(-4, -1, 0), Vec3(11, 8, 16), direction);
    // if (!PortalRoom::isOkBox(box)) std::cout << "    Failed to create \"PortalRoom\" because not !isOkBox\n";
    // if (findCollisionPiece(box, _pieces) != nullptr) std::cout << "    Failed to create \"PortalRoom\" because collision\n";
    if (!PortalRoom::isOkBox(box) || (findCollisionPiece(box, _pieces) != nullptr))
        return OwningNullable<PortalRoom>();
        box.color = "#ff00ff";
    return OwningNullable(new PortalRoom(genDepth, box, direction));
}

#pragma endregion// PortalRoom

void StoneBrickRandomizer::setBlock(LCG& rand, const Pos& position, const bool& placeBlock) {
    float $$5;
    if (placeBlock) {
        rand.nextSeed();
    } else {
    }
}
int getSeaLevel() {
    return 63;
}
int getMinimumY() {
    return -64;
}
BoundingBox getBoundingBox(std::vector<StructurePiece*>& builder) {
    BoundingBox outBox = builder[0]->boundingBox;
    for (size_t i = 1; i < builder.size(); i++) outBox.encompass(builder[i]->boundingBox);
    return outBox;
}
// StructurePiecesBuilder.offsetPiecesVertically function in net/minecraft/world/level/levelgen/structure/pieces/StructurePiecesBuilder
void offsetPiecesVertically(std::vector<StructurePiece*>& builder, const int& yAmount) {
    for (size_t i = 0; i < builder.size(); i++) builder[i]->boundingBox.move({0, yAmount, 0});
}
// StructurePiecesBuilder.moveBelowSeaLevel function in net/minecraft/world/level/levelgen/structure/pieces/StructurePiecesBuilder
int moveBelowSeaLevel(std::vector<StructurePiece*>& builder, const int& seaLevel, const int& minY, LCG& rand, const int& offset) {
    int maxY = seaLevel - offset;
    BoundingBox boundingBox = getBoundingBox(builder);
    int y1Pos = boundingBox.getBlockCountY() + minY + 1;
    if (y1Pos < maxY)
        y1Pos += rand.nextInt(maxY - y1Pos);
    int dy = y1Pos - boundingBox.maxY;
    offsetPiecesVertically(builder, dy);
    return dy;
}
// StrongholdStructure.generatePieces function in net/minecraft/world/level/levelgen/structure/structures/StrongholdStructure
OwningNullable<StartEndBoxes> getPortalRoomPosition(const long long int& worldSeed, const ChunkPos& chunk, const bool& debug) {
    std::vector<StructurePiece*> builder;
    LCG rand(0);
    StartPiece* startRoom = nullptr;
    int tries = 0;
    Pos startPos = chunk.getOffsetPos({ 2, 0, 2 });
    do {
        for (size_t j = 0; j < builder.size(); j++)
            delete builder[j];
        builder.clear();
        rand.setCarverSeed(worldSeed + (long long int)tries++, chunk);
        resetPieces();
        startRoom = new StartPiece(rand, startPos);
        builder.push_back(startRoom);
        startRoom->addChildren(*startRoom, builder, rand);
        std::vector<StrongholdPiece*>& pendingChildren = startRoom->pendingChildren;
        while (pendingChildren.size() > 0) {
            int pos = rand.nextInt(pendingChildren.size());
            StructurePiece* structurePiece = pendingChildren[pos];
            pendingChildren.erase(pendingChildren.begin() + pos);
            structurePiece->addChildren(*startRoom, builder, rand);
        }
        moveBelowSeaLevel(builder, getSeaLevel(), getMinimumY(), rand, 10);
    } while (startRoom->portalRoom == nullptr);

    if (debug) std::cout << "portal room generated after " << tries << " attempt(s).\n";
    OwningNullable<StartEndBoxes> output = OwningNullable<StartEndBoxes>(new StartEndBoxes(startRoom->boundingBox.getBottomCenter() + Pos(0, 1, 0), startRoom->portalRoom->boundingBox.getCenter()));
    std::vector<BoundingBox>& boxes = output.getValue().boxes;
    for (unsigned int i = 0; i < builder.size(); i++) boxes.push_back(builder[i]->boundingBox);
    for (size_t i = 0; i < builder.size(); i++) delete builder[i];
    return output;
}