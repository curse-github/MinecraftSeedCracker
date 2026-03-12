#ifndef __STRONGHOLD_STRUCTURE
#define __STRONGHOLD_STRUCTURE

#include <vector>
#include "MinecraftLib.h"
#include "Random.h"
#include "Lib.h"

//  source/net/minecraft/world/level/levelgen/structure/structures/StrongholdStructure.java
//      source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
//          source/net/minecraft/world/level/levelgen/structure/StructurePiece.java

// StrongholdPieces.SmallDoorType in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
struct PieceWeight {
    unsigned int pieceClass;
    unsigned int weight;
    unsigned int placeCount;
    unsigned int maxPlaceCount;
    unsigned int depthMinimum;
    PieceWeight(const unsigned int& _pieceClass, const unsigned int& _weight, const unsigned int& _maxPlaceCount, const unsigned int& _depthMinimum)
        : pieceClass(_pieceClass), weight(_weight), maxPlaceCount(_maxPlaceCount), placeCount(0), depthMinimum(_depthMinimum) {}
    bool doPlace(const unsigned int& depth) {
        return ((maxPlaceCount == 0) || (placeCount < maxPlaceCount)) && ((depthMinimum == 0) || (depth > depthMinimum));
    }
    bool isValid() {
        return (maxPlaceCount == 0) || (placeCount < maxPlaceCount);
    }
};
#define PieceTypeStraight 0
#define PieceTypePrisonHall 1
#define PieceTypeLeftTurn 2
#define PieceTypeRightTurn 3
#define PieceTypeRoomCrossing 4
#define PieceTypeStraightStairsDown 5
#define PieceTypeStairsDown 6
#define PieceTypeFiveCrossing 7
#define PieceTypeChestCorridor 8
#define PieceTypeLibrary 9
#define PieceTypePortalRoom 10

#pragma region structs
struct StructurePiece {
    BoundingBox boundingBox;
    Direction direction;
    int genDepth;
    StructurePiece(const int& _genDepth, BoundingBox _boundingBox, Direction _direction) : genDepth(_genDepth), boundingBox(_boundingBox), direction(_direction) {}
    StructurePiece(const StructurePiece& copy) = delete;
    StructurePiece(StructurePiece&& move) = delete;
    virtual ~StructurePiece() {};
    StructurePiece& operator=(const StructurePiece& copy) = delete;
    StructurePiece& operator=(StructurePiece&& move) = delete;
    virtual void addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand);
};

// StrongholdPieces.SmallDoorType in source/net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces.java
enum SmallDoorType {
    OPENING,
    WOOD_DOOR,
    GRATES,
    IRON_DOOR
};

struct StrongholdPiece : public StructurePiece{
    SmallDoorType entryDoor = SmallDoorType::OPENING;
    StrongholdPiece(const int& _genDepth, BoundingBox _boundingBox, Direction _direction) : StructurePiece(_genDepth, _boundingBox, _direction) {}
    StrongholdPiece(const StrongholdPiece& copy) = delete;
    StrongholdPiece(StrongholdPiece&& move) = delete;
    virtual ~StrongholdPiece() {};
    StrongholdPiece& operator=(const StrongholdPiece& copy) = delete;
    StrongholdPiece& operator=(StrongholdPiece&& move) = delete;
    virtual void addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand);
    NonOwningNullable<StructurePiece> generateSmallDoorChildForward(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand, const int& xOff, const int& yOff);
    NonOwningNullable<StructurePiece> generateSmallDoorChildLeft(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand, const int& yOff, const int& zOff);
    NonOwningNullable<StructurePiece> generateSmallDoorChildRight(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand, const int& yOff, const int& zOff);
    static bool isOkBox(BoundingBox boundingBox);
};

struct FillerCorridor : public StrongholdPiece {
    int steps;
    FillerCorridor(const int& _genDepth, const BoundingBox& _boundingBox, const Direction& _direction);
    FillerCorridor(const FillerCorridor& copy) = delete;
    FillerCorridor(FillerCorridor&& move) = delete;
    virtual ~FillerCorridor() {};
    FillerCorridor& operator=(const FillerCorridor& copy) = delete;
    FillerCorridor& operator=(FillerCorridor&& move) = delete;
    static OwningNullable<BoundingBox> createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction);
};
struct StairsDown : public StrongholdPiece {
    const bool isStart;
    StairsDown(const int& _genDepth, const Pos& position, const Direction& _direction);
    StairsDown(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction);
    StairsDown(const StairsDown& copy) = delete;
    StairsDown(StairsDown&& move) = delete;
    virtual ~StairsDown() {};
    StairsDown& operator=(const StairsDown& copy) = delete;
    StairsDown& operator=(StairsDown&& move) = delete;
    virtual void addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand);
    static OwningNullable<StairsDown> createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& depth);
};
struct PortalRoom;
struct StartPiece : public StairsDown {
    PieceWeight* previousPiece = nullptr;
    PortalRoom* portalRoom = nullptr;
    std::vector<StrongholdPiece*> pendingChildren;
    StartPiece(LCG& rand, const Pos& position);
};
struct Straight : public StrongholdPiece {
    bool leftExitExists;
    bool rightExitExists;
    Straight(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction);
    Straight(const Straight& copy) = delete;
    Straight(Straight&& move) = delete;
    virtual ~Straight() {};
    Straight& operator=(const Straight& copy) = delete;
    Straight& operator=(Straight&& move) = delete;
    virtual void addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand);
    static OwningNullable<Straight> createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& depth);
};
struct ChestCorridor : public StrongholdPiece {
    ChestCorridor(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction);
    ChestCorridor(const ChestCorridor& copy) = delete;
    ChestCorridor(ChestCorridor&& move) = delete;
    virtual ~ChestCorridor() {};
    ChestCorridor& operator=(const ChestCorridor& copy) = delete;
    ChestCorridor& operator=(ChestCorridor&& move) = delete;
    virtual void addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand);
    static OwningNullable<ChestCorridor> createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& depth);
};
struct StraightStairsDown : public StrongholdPiece {
    StraightStairsDown(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction);
    StraightStairsDown(const StraightStairsDown& copy) = delete;
    StraightStairsDown(StraightStairsDown&& move) = delete;
    virtual ~StraightStairsDown() {};
    StraightStairsDown& operator=(const StraightStairsDown& copy) = delete;
    StraightStairsDown& operator=(StraightStairsDown&& move) = delete;
    virtual void addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand);
    static OwningNullable<StraightStairsDown> createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& depth);
};
struct Turn : public StrongholdPiece {
    Turn(const int& _genDepth, const BoundingBox& _boundingBox, const Direction& _direction) : StrongholdPiece(_genDepth, _boundingBox, _direction) {}
    Turn(const Turn& copy) = delete;
    Turn(Turn&& move) = delete;
    virtual ~Turn() {};
    Turn& operator=(const Turn& copy) = delete;
    Turn& operator=(Turn&& move) = delete;
    virtual void addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand);
};
struct LeftTurn : public Turn {
    LeftTurn(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction);
    LeftTurn(const LeftTurn& copy) = delete;
    LeftTurn(LeftTurn&& move) = delete;
    virtual ~LeftTurn() {};
    LeftTurn& operator=(const LeftTurn& copy) = delete;
    LeftTurn& operator=(LeftTurn&& move) = delete;
    virtual void addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand);
    static OwningNullable<LeftTurn> createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& depth);
};
struct RightTurn : public Turn {
    RightTurn(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction);
    RightTurn(const RightTurn& copy) = delete;
    RightTurn(RightTurn&& move) = delete;
    virtual ~RightTurn() {};
    RightTurn& operator=(const RightTurn& copy) = delete;
    RightTurn& operator=(RightTurn&& move) = delete;
    virtual void addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand);
    static OwningNullable<RightTurn> createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& depth);
};
struct RoomCrossing : public StrongholdPiece {
    int roomType;
    RoomCrossing(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction);
    RoomCrossing(const RoomCrossing& copy) = delete;
    RoomCrossing(RoomCrossing&& move) = delete;
    virtual ~RoomCrossing() {};
    RoomCrossing& operator=(const RoomCrossing& copy) = delete;
    RoomCrossing& operator=(RoomCrossing&& move) = delete;
    virtual void addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand);
    static OwningNullable<RoomCrossing> createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& depth);
};
struct PrisonHall : public StrongholdPiece {
    PrisonHall(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction);
    PrisonHall(const PrisonHall& copy) = delete;
    PrisonHall(PrisonHall&& move) = delete;
    virtual ~PrisonHall() {};
    PrisonHall& operator=(const PrisonHall& copy) = delete;
    PrisonHall& operator=(PrisonHall&& move) = delete;
    virtual void addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand);
    static OwningNullable<PrisonHall> createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& depth);
};
struct Library : public StrongholdPiece {
    bool isTall;
    Library(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction);
    Library(const Library& copy) = delete;
    Library(Library&& move) = delete;
    virtual ~Library() {};
    Library& operator=(const Library& copy) = delete;
    Library& operator=(Library&& move) = delete;
    static OwningNullable<Library> createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& depth);
};
struct FiveCrossing : public StrongholdPiece {
    bool leftLow;
    bool leftHigh;
    bool rightLow;
    bool rightHigh;
    FiveCrossing(const int& _genDepth, LCG& rand, const BoundingBox& _boundingBox, const Direction& _direction);
    FiveCrossing(const FiveCrossing& copy) = delete;
    FiveCrossing(FiveCrossing&& move) = delete;
    virtual ~FiveCrossing() {};
    FiveCrossing& operator=(const FiveCrossing& copy) = delete;
    FiveCrossing& operator=(FiveCrossing&& move) = delete;
    virtual void addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand);
    static OwningNullable<FiveCrossing> createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& depth);
};
struct PortalRoom : public StrongholdPiece {
    PortalRoom(const int& _genDepth, const BoundingBox& _boundingBox, const Direction& _direction);
    PortalRoom(const PortalRoom& copy) = delete;
    PortalRoom(PortalRoom&& move) = delete;
    virtual ~PortalRoom() {};
    PortalRoom& operator=(const PortalRoom& copy) = delete;
    PortalRoom& operator=(PortalRoom&& move) = delete;
    virtual void addChildren(StructurePiece& start, std::vector<StructurePiece*>& _pieces, LCG& rand);
    static OwningNullable<PortalRoom> createPiece(LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& depth);
};

struct BlockRandomizer {
    virtual void setBlock(LCG& rand, const Pos& position, const bool& placeBlock);
};
struct StoneBrickRandomizer : public BlockRandomizer {
    static StoneBrickRandomizer* singleton;
    StoneBrickRandomizer() {
        StoneBrickRandomizer::singleton = this;
    }
    void setBlock(LCG& rand, const Pos& position, const bool& placeBlock);
};
#pragma endregion structs
BoundingBox* getIntersecting(const BoundingBox& box, const std::vector<StrongholdPiece*>& _pieces);
OwningNullable<StrongholdPiece> findAndcreatePiecePieceFactory(const unsigned int& pieceType, LCG& rand, std::vector<StrongholdPiece*>& _pieces, const Pos& position, Direction direction, int depth);
OwningNullable<StrongholdPiece> generatePieceFromSmallDoor(StartPiece& start, LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, Direction direction, int depth);
NonOwningNullable<StructurePiece> generateAndAddPiece(StructurePiece& start, LCG& rand, std::vector<StructurePiece*>& _pieces, const Pos& position, const Direction& direction, const int& depth);
struct StartEndBoxes {
    Pos start;
    std::vector<BoundingBox> boxes;
    Pos end;
    StartEndBoxes(const Pos& _start, const Pos& _end) : start(_start), end(_end) {}
};
OwningNullable<StartEndBoxes> getPortalRoomPosition(const long long int& worldSeed, const ChunkPos& chunk, const bool& debug = false);
#endif