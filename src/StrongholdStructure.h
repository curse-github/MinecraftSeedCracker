#ifndef __STRONGHOLD_STRUCTURE
#define __STRONGHOLD_STRUCTURE

#include <vector>
#include "MinecraftLib.h"
#include "Random.h"
#include "Lib.h"

// see StrongholdGenerator.PieceData class in StrongholdGenerator.java
struct PieceData {
    unsigned int type;
    unsigned int weight;
    unsigned int pieceLimit;
    unsigned int piecesGenerated;
    unsigned int chainLengthMinimum;
    PieceData(const unsigned int& _type, const unsigned int& _weight, const unsigned int& _pieceLimit, const unsigned int& _chainLengthMinimum)
        : type(_type), weight(_weight), pieceLimit(_pieceLimit), piecesGenerated(0), chainLengthMinimum(_chainLengthMinimum) {}
    bool canGenerate(const unsigned int& chainLength) {
        return ((pieceLimit == 0) || (piecesGenerated < pieceLimit)) && (chainLength > chainLengthMinimum);
    }
    bool canGenerate() {
        return (pieceLimit == 0) || (piecesGenerated < pieceLimit);
    }
};
extern const unsigned int PieceTypeNone;
extern const unsigned int PieceTypeCorridor;
extern const unsigned int PieceTypePrisonHall;
extern const unsigned int PieceTypeLeftTurn;
extern const unsigned int PieceTypeRightTurn;
extern const unsigned int PieceTypeSquareRoom;
extern const unsigned int PieceTypeStairs;
extern const unsigned int PieceTypeSpiralStaircase;
extern const unsigned int PieceTypeFiveWayCrossing;
extern const unsigned int PieceTypeChestCorridor;
extern const unsigned int PieceTypeLibrary;
extern const unsigned int PieceTypePortalRoom;
extern std::vector<std::string> allPieceNames;
extern const std::vector<PieceData> allPieces;
extern unsigned int activePieceType;
extern int totalWeight;
bool checkRemainingPieces(const std::vector<PieceData>& possiblePieces);
// see StrongholdGenerator.Piece.EntranceType enum in StrongholdGenerator.java
enum EntranceType {
    OPENING,
    WOOD_DOOR,
    GRATES,
    IRON_DOOR
};
EntranceType getRandomEntrance(LCG& rand);

#pragma region structs
struct Piece;
// see StructurePiece class in StructurePiece.java
struct StructurePiece {
    int chainLength;
    BlockBox boundingBox;
    Direction orientation;
    StructurePiece(const int& _chainLength, BlockBox _boundingBox, Direction _orientation) : chainLength(_chainLength), boundingBox(_boundingBox), orientation(_orientation) {}
    StructurePiece(const StructurePiece& copy) = delete;
    StructurePiece(StructurePiece&& move) = delete;
    virtual ~StructurePiece() {};
    StructurePiece& operator=(const StructurePiece& copy) = delete;
    StructurePiece& operator=(StructurePiece&& move) = delete;
    virtual void fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand);
};
NonOwningNullable<StructurePiece> generatePiece(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand, const Pos& position, const Direction& orientation, const int& chainLength);
// see StrongholdGenerator.Piece class in StrongholdGenerator.java
struct Piece : public StructurePiece{
    EntranceType entryDoor = EntranceType::OPENING;
    Piece(const int& _chainLength, BlockBox _boundingBox, Direction _orientation) : StructurePiece(_chainLength, _boundingBox, _orientation) {}
    Piece(const Piece& copy) = delete;
    Piece(Piece&& move) = delete;
    virtual ~Piece() {};
    Piece& operator=(const Piece& copy) = delete;
    Piece& operator=(Piece&& move) = delete;
    virtual void fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand);
    NonOwningNullable<StructurePiece> fillForwardOpening(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand, const int& leftRightOffset, const int& heightOffset);
    NonOwningNullable<StructurePiece> fillNWOpening(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand, const int& leftRightOffset, const int& heightOffset);
    NonOwningNullable<StructurePiece> fillSEOpening(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand, const int& leftRightOffset, const int& heightOffset);
    static bool isInBounds(BlockBox boundingBox);
};
// see StrongholdGenerator.Corridor class in StrongholdGenerator.java
struct Corridor : public Piece {
    bool leftExitExists;
    bool rightExitExists;
    Corridor(const int& _chainLength, LCG& rand, const BlockBox& _boundingBox, const Direction& _orientation);
    Corridor(const Corridor& copy) = delete;
    Corridor(Corridor&& move) = delete;
    ~Corridor() {};
    Corridor& operator=(const Corridor& copy) = delete;
    Corridor& operator=(Corridor&& move) = delete;
    void fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand);
    static OwningNullable<Corridor> create(std::vector<Piece*>& _pieces, LCG& rand, const Pos& position, const Direction& orientation, const int& chainLength);
};
// see StrongholdGenerator.PrisonHall class in StrongholdGenerator.java
struct PrisonHall : public Piece {
    PrisonHall(const int& _chainLength, LCG& rand, const BlockBox& _boundingBox, const Direction& _orientation);
    PrisonHall(const PrisonHall& copy) = delete;
    PrisonHall(PrisonHall&& move) = delete;
    ~PrisonHall() {};
    PrisonHall& operator=(const PrisonHall& copy) = delete;
    PrisonHall& operator=(PrisonHall&& move) = delete;
    void fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand);
    static OwningNullable<PrisonHall> create(std::vector<Piece*>& _pieces, LCG& rand, const Pos& position, const Direction& orientation, const int& chainLength);
};
// see StrongholdGenerator.Turn class in StrongholdGenerator.java
struct Turn : public Piece {
    // see StrongholdGenerator.Turn constructor in StrongholdGenerator.java
    Turn(const int& _chainLength, const BlockBox& _boundingBox, const Direction& _orientation) : Piece(_chainLength, _boundingBox, _orientation) {}
    Turn(const Turn& copy) = delete;
    Turn(Turn&& move) = delete;
    virtual ~Turn() {};
    Turn& operator=(const Turn& copy) = delete;
    Turn& operator=(Turn&& move) = delete;
    virtual void fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand);
};
// see StrongholdGenerator.LeftTurn class in StrongholdGenerator.java
struct LeftTurn : public Turn {
    LeftTurn(const int& _chainLength, LCG& rand, const BlockBox& _boundingBox, const Direction& _orientation);
    LeftTurn(const LeftTurn& copy) = delete;
    LeftTurn(LeftTurn&& move) = delete;
    ~LeftTurn() {};
    LeftTurn& operator=(const LeftTurn& copy) = delete;
    LeftTurn& operator=(LeftTurn&& move) = delete;
    void fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand);
    static OwningNullable<LeftTurn> create(std::vector<Piece*>& _pieces, LCG& rand, const Pos& position, const Direction& orientation, const int& chainLength);
};
// see StrongholdGenerator.RightTurn class in StrongholdGenerator.java
struct RightTurn : public Turn {
    RightTurn(const int& _chainLength, LCG& rand, const BlockBox& _boundingBox, const Direction& _orientation);
    RightTurn(const RightTurn& copy) = delete;
    RightTurn(RightTurn&& move) = delete;
    ~RightTurn() {};
    RightTurn& operator=(const RightTurn& copy) = delete;
    RightTurn& operator=(RightTurn&& move) = delete;
    void fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand);
    static OwningNullable<RightTurn> create(std::vector<Piece*>& _pieces, LCG& rand, const Pos& position, const Direction& orientation, const int& chainLength);
};
// see StrongholdGenerator.SquareRoom class in StrongholdGenerator.java
struct SquareRoom : public Piece {
    int roomType;
    SquareRoom(const int& _chainLength, LCG& rand, const BlockBox& _boundingBox, const Direction& _orientation);
    SquareRoom(const SquareRoom& copy) = delete;
    SquareRoom(SquareRoom&& move) = delete;
    ~SquareRoom() {};
    SquareRoom& operator=(const SquareRoom& copy) = delete;
    SquareRoom& operator=(SquareRoom&& move) = delete;
    void fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand);
    static OwningNullable<SquareRoom> create(std::vector<Piece*>& _pieces, LCG& rand, const Pos& position, const Direction& orientation, const int& chainLength);
};
// see StrongholdGenerator.Stairs class in StrongholdGenerator.java
struct Stairs : public Piece {
    Stairs(const int& _chainLength, LCG& rand, const BlockBox& _boundingBox, const Direction& _orientation);
    Stairs(const Stairs& copy) = delete;
    Stairs(Stairs&& move) = delete;
    ~Stairs() {};
    Stairs& operator=(const Stairs& copy) = delete;
    Stairs& operator=(Stairs&& move) = delete;
    void fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand);
    static OwningNullable<Stairs> create(std::vector<Piece*>& _pieces, LCG& rand, const Pos& position, const Direction& orientation, const int& chainLength);
};
// see StrongholdGenerator.SpiralStaircase class in StrongholdGenerator.java
struct SpiralStaircase : public Piece {
    const bool isStart;
    SpiralStaircase(const int& _chainLength, const Pos& position, const Direction& _orientation);
    SpiralStaircase(const int& _chainLength, LCG& rand, const BlockBox& _boundingBox, const Direction& _orientation);
    SpiralStaircase(const SpiralStaircase& copy) = delete;
    SpiralStaircase(SpiralStaircase&& move) = delete;
    ~SpiralStaircase() {};
    SpiralStaircase& operator=(const SpiralStaircase& copy) = delete;
    SpiralStaircase& operator=(SpiralStaircase&& move) = delete;
    void fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand);
    static OwningNullable<SpiralStaircase> create(std::vector<Piece*>& _pieces, LCG& rand, const Pos& position, const Direction& orientation, const int& chainLength);
};
// see StrongholdGenerator.FiveWayCrossing class in StrongholdGenerator.java
struct FiveWayCrossing : public Piece {
    bool lowerLeftExists;
    bool upperLeftExists;
    bool lowerRightExists;
    bool upperRightExists;
    FiveWayCrossing(const int& _chainLength, LCG& rand, const BlockBox& _boundingBox, const Direction& _orientation);
    FiveWayCrossing(const FiveWayCrossing& copy) = delete;
    FiveWayCrossing(FiveWayCrossing&& move) = delete;
    ~FiveWayCrossing() {};
    FiveWayCrossing& operator=(const FiveWayCrossing& copy) = delete;
    FiveWayCrossing& operator=(FiveWayCrossing&& move) = delete;
    void fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand);
    static OwningNullable<FiveWayCrossing> create(std::vector<Piece*>& _pieces, LCG& rand, const Pos& position, const Direction& orientation, const int& chainLength);
};
// see StrongholdGenerator.ChestCorridor class in StrongholdGenerator.java
struct ChestCorridor : public Piece {
    ChestCorridor(const int& _chainLength, LCG& rand, const BlockBox& _boundingBox, const Direction& _orientation);
    ChestCorridor(const ChestCorridor& copy) = delete;
    ChestCorridor(ChestCorridor&& move) = delete;
    ~ChestCorridor() {};
    ChestCorridor& operator=(const ChestCorridor& copy) = delete;
    ChestCorridor& operator=(ChestCorridor&& move) = delete;
    void fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand);
    static OwningNullable<ChestCorridor> create(std::vector<Piece*>& _pieces, LCG& rand, const Pos& position, const Direction& orientation, const int& chainLength);
};
// see StrongholdGenerator.Library class in StrongholdGenerator.java
struct Library : public Piece {
    bool isTall;
    Library(const int& _chainLength, LCG& rand, const BlockBox& _boundingBox, const Direction& _orientation);
    Library(const Library& copy) = delete;
    Library(Library&& move) = delete;
    ~Library() {};
    Library& operator=(const Library& copy) = delete;
    Library& operator=(Library&& move) = delete;
    static OwningNullable<Library> create(std::vector<Piece*>& _pieces, LCG& rand, const Pos& position, const Direction& orientation, const int& chainLength);
};
// see StrongholdGenerator.PortalRoom class in StrongholdGenerator.java
struct PortalRoom : public Piece {
    // see StrongholdGenerator.PortalRoom constructor in StrongholdGenerator.java
    PortalRoom(const int& _chainLength, const BlockBox& _boundingBox, const Direction& _orientation);
    PortalRoom(const PortalRoom& copy) = delete;
    PortalRoom(PortalRoom&& move) = delete;
    ~PortalRoom() {};
    PortalRoom& operator=(const PortalRoom& copy) = delete;
    PortalRoom& operator=(PortalRoom&& move) = delete;
    void fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand);
    static OwningNullable<PortalRoom> create(std::vector<Piece*>& _pieces, const Pos& position, const Direction& orientation, const int& chainLength);
};
// see StrongholdGenerator.SmallCorridor class in StrongholdGenerator.java
struct SmallCorridor : public Piece {
    int length;
    SmallCorridor(const int& _chainLength, const BlockBox& _boundingBox, const Direction& _orientation);
    SmallCorridor(const SmallCorridor& copy) = delete;
    SmallCorridor(SmallCorridor&& move) = delete;
    ~SmallCorridor() {};
    SmallCorridor& operator=(const SmallCorridor& copy) = delete;
    SmallCorridor& operator=(SmallCorridor&& move) = delete;
    static OwningNullable<BlockBox> create(std::vector<Piece*>& _pieces, LCG& rand, const Pos& position, const Direction& orientation);
};
// see StrongholdGenerator.Start class in StrongholdGenerator.java
struct Start : public SpiralStaircase {
    PortalRoom* portalRoom = nullptr;
    std::vector<Piece*> pieces;
    unsigned int lastPiece = PieceTypeNone;
    std::vector<PieceData> possiblePieces;
    Start(LCG& rand, const Pos& position);
};
// see StructurePiece.BlockRandomizer class in StructurePiece.java
struct BlockRandomizer {
    // Block block = Blocks.AIR.getDefaultState();
    virtual void setBlock(LCG& rand, const Pos& position, const bool& placeBlock);
};
// see StrongholdGenerator.StoneBrickRandomizer class in StrongholdGenerator.java
struct StoneBrickRandomizer : public BlockRandomizer {
    static StoneBrickRandomizer* singleton;
    StoneBrickRandomizer() {
        StoneBrickRandomizer::singleton = this;
    }
    void setBlock(LCG& rand, const Pos& position, const bool& placeBlock);
};
#pragma endregion structs

BlockBox* getIntersecting(const BlockBox& box, const std::vector<Piece*>& _pieces);
OwningNullable<Piece> createPiece(const unsigned int& pieceType, std::vector<Piece*>& _pieces, LCG& rand, const Pos& position, Direction orientation, int chainLength);
OwningNullable<Piece> pickPiece(Start& start, std::vector<Piece*>& _pieces, LCG& rand, const Pos& position, Direction orientation, int chainLength);
NonOwningNullable<StructurePiece> generatePiece(StructurePiece& start, std::vector<Piece*>& _pieces, LCG& rand, const Pos& position, const Direction& orientation, const int& chainLength);

struct StartEndBoxes {
    Pos start;
    std::vector<BlockBox> boxes;
    Pos end;
    StartEndBoxes(const Pos& _start, const Pos& _end) : start(_start), end(_end) {}
};
OwningNullable<StartEndBoxes> getPortalRoomPosition(const long long int& worldSeed, const ChunkPos& chunk, const bool& debug = false);

#endif// __STRONGHOLD_STRUCTURE