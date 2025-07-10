//  ChunkGenerator.setStructureStarts() function in ChunkGenerator.java
//      StructurePlacement.shouldGenerate() function in StructurePlacement.java
//          StructurePlacement.shouldGenerate() function in StructurePlacement.java
//              ConcentricRingsStructurePlacement.isStartChunk() function in ConcentricRingsStructurePlacement.java
//                  stuff, see above
//              StructurePlacement.applyFrequencyReduction() function in StructurePlacement.java
//                  this->frequency >= 1 // this also happens to default to one, making applyFrequencyReduction always return true
//                  StructurePlacement.FrequencyReductionMethod.shouldGenerate(seed, this->salt, chunkX, chunkZ, this->frequency)
//                      // the FrequencyReductionMethod is either the "frequency_reduction_method" field from the structure_set, or the "default" type
//                          // see https://minecraft.wiki/w/Structure_set for default frequency_reduction_method is "default"
//                      // possible types are "default", "legacy_type_1", "legacy_type_2", "legacy_type_3"
//                      // where the FrequencyReductionMethod.should generate simple calls "defaultShouldGenerate", "legacyType1ShouldGenerate", "legacyType2ShouldGenerate", or "legacyType3ShouldGenerate" for each type respectively
//                      StructurePlacement.defaultShouldGenerate(long seed, int salt, int chunkX, int chunkZ, float frequency)
//IMPORTANT                 ChunkRandom.setRegionSeed(seed, salt, chunkX, chunkZ) function in ChunkRandom.java
//IMPORTANT                     seed = worldSeed + (long)chunkX * 341873128712ull + (long)chunkZ * 132897987541ull + (long)salt;
//IMPORTANT                 (ChunkRandom.nextFloat() in ChunkRandom.java) < frequency
//                              // small frequency, spawns less often.
//                              // larger frequency, spawns more often.
//                              // frequency on 1 or higher, spawns every time.// frequency defaults to 1, see https://minecraft.wiki/w/Structure_set
//      ChunkGenerator.trySetStructureStart(StructureSet.WeightedEntry weightedEntry, StructureAccessor structureAccessor, DynamicRegistryManager dynamicRegistryManager, NoiseConfig noiseConfig, StructureTemplateManager structureManager, long seed, Chunk chunk, ChunkPos pos, ChunkSectionPos sectionPos, RegistryKey<World> dimension) function in ChunkGenerator.java
//          Structure.getValidBiomes() function in Structure.java
//              gets the "biomes" value from the structure json file referenced by the structure_set
//          Structure.createStructureStart(structure, World dimension, DynamicRegistryManager dynamicRegistryManager, ChunkGenerator chunkGenerator, BiomeSource biomeSource, NoiseConfig coiseConfig, StructureTemplateManager structureTemplateManager, long seed, ChunkPos chunkPos, int references, HeightLimitView world, Biome[] validBiomes) function in Structure.java
//              Structure.Context.constructor
//                  Structure.Context.createChunkRandom(seed, chunkPos)
//                      ChunkRandom.setCarverSeed(long seed, int chunkX = chunkPos.x, int chunkZ = chunkPos.z)
//IMPORTANT                 Random worldSeed_rand = new Random(worldSeed)
//IMPORTANT                 return new Random((long)chunkX * this->nextLong() ^ (long)chunkZ * this->nextLong() ^ worldSeed);
//              Structure.getValidStructurePosition(Structure.Context context) function in Structure.java
//                  extends(Structure).getStructurePosition(Structure.Context context) function of class which extends Structure
//                      Structure.StructurePosition.constructor(BlockPos pos, Consumer<StructurePiecesCollector> generator) in Structure.java
//                      // see StrongholdStructure.getStructurePosition
//                      // or JigsawStructure.getStructurePosition
//                  Struction.isBiomeValid(StructurePosition position, Structure.Context context) function in Struction.java
//                      // returns true if structure.getValidBiomes() contains the biome at BiomeCoords.fromBlock(position)
//                      BiomeCoords.fromBlock(int blockCoord) function in BiomeCoords.java
//                          // corresponds to every 4 blocks ie. blockCoord / 4 or blockCoord >> 2
//                  // functions returns the position from structure.getStructurePosition() if the biome is valid, otherwise an empty optional value
//              Structure.StructurePosition.generate() function in Structure.java
//                  // creates a new StructurePiecesCollector, and calls the Consumer's predicate if there is one, such as StrongholdStructure.addPieces or StrongholdStructure.addPieces or StructurePoolBasedGenerator.method_39824
//                      StructurePiecesCollector.constructor() in StructurePiecesCollector.java
//                          // only creates a StructurePiece list and contains nothing else
//                      // though, its rather dissapointing, the StrongholdStructure class has not been switched to using the JigsawStructure, as proven by the code in the Structures.java file
//                      StrongholdStructure.addPieces(Collector collector, Structure.Context context) in StrongholdStructure.java
//                          // repeat until it has a portal room, clearing and starting over if it doesnt.
//                              Random rand = new Random()
//IMPORTANT                     rand.setCarverSeed(seed+#attempts, chunkX, chunkZ)// see definition above
//                              static StrongholdGenerator.init() function in StrongholdGenerator.java
//                                  // populates the possiblePieces list with the following
//                                      // Corridor         , weight: 40, limit: 0
//                                      // PrisonHall       , weight:  5, limit: 5
//                                      // LeftTurn         , weight: 20, limit: 0
//                                      // RightTurn        , weight: 20, limit: 0
//                                      // SquareRoom       , weight: 10, limit: 6
//                                      // Stairs           , weight:  5, limit: 5
//                                      // SpiralStaircase  , weight:  5, limit: 5
//                                      // FiveWayCrossing  , weight:  5, limit: 4
//                                      // ChestCorridor    , weight:  5, limit: 4
//                                      // Library          , weight: 10, limit: 2
//                                      // PortalRoom       , weight: 20, limit: 1
//                                  // another thing to note is a "chain limit" of 4 for all peices except the portal room, which has a limit of 5
//                              generator = StrongholdGenerator.Start.constructor
//                                  calls StructurePiece.getRandomHorizontalDirection(rand) from StructurePiece.java
//                                      Direction.Type.HORIZONTAL.random(rand) from Direction.java
//                                          Util.getRandom(Direction.Type.HORIZONTAL, rand)
//                                              Direction.Type.HORIZONTAL.facingArray[rand.nextInt(Direction.Type.HORIZONTAL.facingArray.length)]
//IMPORTANT                                         // note, calls rand.nextInt once
//                              StrongholdGenerator.Start.fillOpenings(generator, rand)
//                              StructurePiece peice = // take peice at index rand.nextInt(generator.peices.size()) generator.peices and remove it from main list
//                              call StructurePiece.fillOpenings(generator, rand) on "peice" from file StructurePiece.java
//                                  // see definition above
//                              StructurePiecesCollector.shiftInto(sea_level, world_bottom, rand, 10)// shifts structure underground
//                                  StructurePiecesCollector.shiftInto(int topY, int bottomY, Random rand, int topPenalty)
//                                      yLevel = bounds.height + bittomY + 1
//                                      if (yLevel < topY - topPenalty) yLevel += rand.nextInt(topY - topPenalty - yLevel) // IMPORTANT
//                                      StructurePiecesCollector.shift(yLevel - bounds.maxY)
//                  // returns a new Structure.StructurePiecesCollector
//              StructurePiecesCollector.toList() function in StructurePiecesCollector.java function in StructurePiecesCollector.java
//                  // returns StructurePiecesCollector.pieces but as a StructurePiecesList class
//              StructureStart.constructor(Structure structure, ChunkPos chunkPos, int references, StructurePiecesList children) function in StructureStart.java
//                  // simply set its internal variables with the arguments
//              StructureStart.hasChildren() function in StructureStart.java
//                  // returns true if the StructureStart.children object is not empty
//                  // the children array will not be empty as long as the consumer used to construct the Structure.StructurePosition in the extends(Struction).getStructurePosition function populates it
//              // returns the StructureStart
//              // will return DEFAULT if structureStart has no children, which is equal to (new StructureStart(null, new ChunkPos(0, 0), 0, new StructurePiecesList(List.of())))
//          StructureAccessor.setStructureStart() function in StructureAccessor.java

#include "StrongholdStructure.h"

const unsigned int PieceTypeNone = -1;
const unsigned int PieceTypeCorridor = 0;
const unsigned int PieceTypePrisonHall = 1;
const unsigned int PieceTypeLeftTurn = 2;
const unsigned int PieceTypeRightTurn = 3;
const unsigned int PieceTypeSquareRoom = 4;
const unsigned int PieceTypeStairs = 5;
const unsigned int PieceTypeSpiralStaircase = 6;
const unsigned int PieceTypeFiveWayCrossing = 7;
const unsigned int PieceTypeChestCorridor = 8;
const unsigned int PieceTypeLibrary = 9;
const unsigned int PieceTypePortalRoom = 10;
std::vector<std::string> allPieceNames = {
    "Corridor       ",
    "PrisonHall     ",
    "LeftTurn       ",
    "RightTurn      ",
    "SquareRoom     ",
    "Stairs         ",
    "SpiralStaircase",
    "FiveWayCrossing",
    "ChestCorridor  ",
    "Library        ",
    "PortalRoom     "
};
// see StrongholdGenerator.ALL_PIECES constant in StrongholdGenerator.java
const std::vector<PieceData> allPieces = {
    PieceData(PieceTypeCorridor, 40, 0, 0),
    PieceData(PieceTypePrisonHall, 5, 5, 0),
    PieceData(PieceTypeLeftTurn, 20, 0, 0),
    PieceData(PieceTypeRightTurn, 20, 0, 0),
    PieceData(PieceTypeSquareRoom, 10, 6, 0),
    PieceData(PieceTypeStairs, 5, 5, 0),
    PieceData(PieceTypeSpiralStaircase, 5, 5, 0),
    PieceData(PieceTypeFiveWayCrossing, 5, 4, 0),
    PieceData(PieceTypeChestCorridor, 5, 4, 0),
    PieceData(PieceTypeLibrary, 10, 2, 4),
    PieceData(PieceTypePortalRoom, 20, 1, 5)
};
// see StrongholdGenerator.activePieceType variable in StrongholdGenerator.java
unsigned int activePieceType = PieceTypeNone;
// see StrongholdGenerator.totalWeight variable in StrongholdGenerator.java
int totalWeight = 0;
// see StrongholdGenerator.checkRemainingPieces function in StrongholdGenerator.java
bool checkRemainingPieces(const std::vector<PieceData>& possiblePieces) {
    bool tmp = false;
    totalWeight = 0;
    for (size_t i = 0; i < possiblePieces.size(); i++) {
        PieceData piece = possiblePieces[i];
        if (piece.pieceLimit > 0 && piece.piecesGenerated < piece.pieceLimit) {
            tmp = true;
        }
        totalWeight += piece.weight;
    }
    return tmp;
}
// see StrongholdGenerator.Piece.getRandomEntrance function in StrongholdGenerator.java
EntranceType getRandomEntrance(Random& rand) {
    int rnd = rand.nextInt(5);
    switch (rnd) {
        case 0:
        case 1:
        default:// for 0, 1, or somthing larger than 4 somehow
            return EntranceType::OPENING;
        case 2:
            return EntranceType::WOOD_DOOR;
        case 3:
            return EntranceType::GRATES;
        case 4:
            return EntranceType::IRON_DOOR;
    }
}

//  see abstract StructurePiecesHolder.getIntersecting function in StructurePiecesHolder.java
//      StructurePiecesCollector.getIntersecting function in StructurePiecesCollector.java
//          StructurePiece.firstIntersecting function in StructurePiece.java
//              BlockBox.intersects function in BlockBox.java
BlockBox* getIntersecting(const BlockBox& box, const std::vector<Piece*>& _pieces) {
    for (size_t i = 0; i < _pieces.size(); i++) {
        if (_pieces[i]->boundingBox.intersects(box)) return &(_pieces[i]->boundingBox);
    }
    return nullptr;
}
// see StrongholdGenerator.createPiece function in StrongholdGenerator.java
OwningNullable<Piece> createPiece(const unsigned int& pieceType, std::vector<Piece*>& _pieces, Random& rand, const Pos& position, Direction orientation, int chainLength) {
    // std::cout << "                createPiece(\"" << allPieceNames[pieceType] << "\")\n";
    switch (pieceType) {
        case PieceTypeCorridor: {
            return Corridor::create(_pieces, rand, position, orientation, chainLength);
        } case PieceTypePrisonHall: {
            return PrisonHall::create(_pieces, rand, position, orientation, chainLength);
        } case PieceTypeLeftTurn: {
            return LeftTurn::create(_pieces, rand, position, orientation, chainLength);
        } case PieceTypeRightTurn: {
            return RightTurn::create(_pieces, rand, position, orientation, chainLength);
        } case PieceTypeSquareRoom: {
            return SquareRoom::create(_pieces, rand, position, orientation, chainLength);
        } case PieceTypeStairs: {
            return Stairs::create(_pieces, rand, position, orientation, chainLength);
        } case PieceTypeSpiralStaircase: {
            return SpiralStaircase::create(_pieces, rand, position, orientation, chainLength);
        } case PieceTypeFiveWayCrossing: {
            return FiveWayCrossing::create(_pieces, rand, position, orientation, chainLength);
        } case PieceTypeChestCorridor: {
            return ChestCorridor::create(_pieces, rand, position, orientation, chainLength);
        } case PieceTypeLibrary: {
            return Library::create(_pieces, rand, position, orientation, chainLength);
        } case PieceTypePortalRoom: {
            return PortalRoom::create(_pieces, position, orientation, chainLength);
        }
    }
    return OwningNullable<Piece>();
}
// see StrongholdGenerator.pickPiece function in StrongholdGenerator.java
OwningNullable<Piece> pickPiece(Start& start, std::vector<Piece*>& _pieces, Random& rand, const Pos& position, Direction orientation, int chainLength) {
    // std::cout << "            pickPiece()\n";
    if (!checkRemainingPieces(start.possiblePieces)) return OwningNullable<Piece>();
    if (activePieceType != PieceTypeNone) {
        OwningNullable piece = createPiece(activePieceType, _pieces, rand, position, orientation, chainLength);
        // if (!piece.hasValue) std::cout << "                failed to create \"" << allPieceNames[activePieceType] << "\".\n";
        if (piece.hasValue) {
            // std::cout << "created piece of type \"" << allPieceNames[activePieceType] << "\".\n";
            activePieceType = PieceTypeNone;
            return piece;
        }
        activePieceType = PieceTypeNone;
    }
    for (unsigned int attempts = 0; attempts < 5; attempts++) {
        int rnd = rand.nextInt(totalWeight);
        for (size_t i = 0; i < start.possiblePieces.size(); i++) {
            PieceData& possiblePiece = start.possiblePieces[i];
            // std::cout << "\"" << allPieceNames[possiblePiece.type] << "\"\n";
            rnd -= possiblePiece.weight;
            // if (rnd >= 0) std::cout << "                weight for piece \"" << allPieceNames[possiblePiece.type] << "\" did not match.\n";
            if (rnd >= 0) continue;
            // if (!possiblePiece.canGenerate(chainLength)) std::cout << "                piece \"" << allPieceNames[possiblePiece.type] << "\" cannot generate\n";
            // if ((start.lastPiece == possiblePiece.type)) std::cout << "                piece \"" << allPieceNames[possiblePiece.type] << "\" was generated last\n";
            if (!possiblePiece.canGenerate(chainLength) || (start.lastPiece == possiblePiece.type)) break;
            OwningNullable piece = createPiece(possiblePiece.type, _pieces, rand, position, orientation, chainLength);
            // if (!piece.hasValue) std::cout << "                failed to create \"" << allPieceNames[possiblePiece.type] << "\".\n";
            if (!piece.hasValue) continue;
            // std::cout << "                created piece of type \"" << allPieceNames[possiblePiece.type] << "\".\n";
            // std::cout << "created piece of type \"" << allPieceNames[possiblePiece.type] << "\".\n";
            // std::cout << "\"" << allPieceNames[possiblePiece.type] << "\" #" << possiblePiece.piecesGenerated << "\n";
            possiblePiece.piecesGenerated++;
            start.lastPiece = possiblePiece.type;
            if (!possiblePiece.canGenerate()) {
                start.possiblePieces.erase(start.possiblePieces.cbegin() + i);
            }
            return piece;
        }
    }
    OwningNullable boundingBox = SmallCorridor::create(_pieces, rand, position, orientation);
    if (boundingBox.hasValue && boundingBox.getValue().minY > 1)
        return OwningNullable<Piece>(new SmallCorridor(chainLength, boundingBox.getValue(), orientation));
    return OwningNullable<Piece>();
}
// see StrongholdGenerator.generatePiece function in StrongholdGenerator.java
NonOwningNullable<StructurePiece> generatePiece(StructurePiece& start, std::vector<Piece*>& _pieces, Random& rand, const Pos& position, const Direction& orientation, const int& chainLength) {
    // std::cout << "        generatePiece(chainLength=" << chainLength << ")\n";
    Start& _start = (Start&)start;
    if (chainLength > 50) { std::cout << "chainLength reached maximum.\n"; return NonOwningNullable<StructurePiece>(); }
    if ((myAbs(position.x - start.boundingBox.minX) > 112) || (myAbs(position.z - start.boundingBox.minZ) > 112)) return NonOwningNullable<StructurePiece>();
    OwningNullable pieceMaybe = pickPiece(_start, _pieces, rand, position, orientation, chainLength + 1);
    if (pieceMaybe.hasValue) {
        // std::cout << "            generated piece\n";
        Piece* piece = pieceMaybe.takeValue();
        // std::cout << "            piece: " << piece << "\n";
        _pieces.push_back(piece);
        _start.pieces.push_back(piece);
        /* std::cout << "_pieces: {\n";
        for (size_t i = 0; i < _pieces.size(); i++) {
            std::cout << _pieces[i] << ",\n";
        }
        std::cout << "}\n";*/
        return NonOwningNullable(piece);
    } else
        return NonOwningNullable<StructurePiece>();
}
// see StrongholdGenerator.Piece.fillForwardOpening function in StrongholdGenerator.java
NonOwningNullable<StructurePiece> Piece::fillForwardOpening(StructurePiece& start, std::vector<Piece*>& _pieces, Random& rand, const int& leftRightOffset, const int& heightOffset) {
    // std::cout << "    fillForwardOpening()\n";
    switch (orientation.index) {
        case NORTH:
            return generatePiece(start, _pieces, rand, boundingBox.getMin() + Pos(leftRightOffset, heightOffset, -1), orientation, chainLength);
        case SOUTH:
            return generatePiece(start, _pieces, rand, Pos(boundingBox.minX + leftRightOffset, boundingBox.minY + heightOffset,  boundingBox.maxZ + 1), orientation, chainLength);
        case WEST:
            return generatePiece(start, _pieces, rand, boundingBox.getMin() + Pos(-1, heightOffset, leftRightOffset), orientation, chainLength);
        case EAST:
            return generatePiece(start, _pieces, rand, Pos(boundingBox.maxX + 1, boundingBox.minY + heightOffset, boundingBox.minZ + leftRightOffset), orientation, chainLength);
        default:
            return NonOwningNullable<StructurePiece>();
    }
}
// see StrongholdGenerator.Piece.fillNWOpening function in StrongholdGenerator.java
NonOwningNullable<StructurePiece> Piece::fillNWOpening(StructurePiece& start, std::vector<Piece*>& _pieces, Random& rand, const int& leftRightOffset, const int& heightOffset) {
    // std::cout << "    fillNWOpening()\n";
    switch (orientation.index) {
        case NORTH:
            return generatePiece(start, _pieces, rand, boundingBox.getMin() + Pos(-1, heightOffset, leftRightOffset), Directions[WEST], chainLength);
        case SOUTH:
            return generatePiece(start, _pieces, rand, boundingBox.getMin() + Pos(-1, heightOffset, leftRightOffset), Directions[WEST], chainLength);
        case WEST:
            return generatePiece(start, _pieces, rand, boundingBox.getMin() + Pos(leftRightOffset, heightOffset, -1), Directions[NORTH], chainLength);
        case EAST:
            return generatePiece(start, _pieces, rand, boundingBox.getMin() + Pos(leftRightOffset, heightOffset, -1), Directions[NORTH], chainLength);
        default:
            return NonOwningNullable<StructurePiece>();
    }
}
// see StrongholdGenerator.Piece.fillSEOpening function in StrongholdGenerator.java
NonOwningNullable<StructurePiece> Piece::fillSEOpening(StructurePiece& start, std::vector<Piece*>& _pieces, Random& rand, const int& leftRightOffset, const int& heightOffset) {
    // std::cout << "    fillSEOpening()\n";
    switch (orientation.index) {
        case NORTH:
            return generatePiece(start, _pieces, rand, Pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ) + Pos(1, heightOffset, leftRightOffset), Directions[EAST], chainLength);
        case SOUTH:
            return generatePiece(start, _pieces, rand, Pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ) + Pos(1, heightOffset, leftRightOffset), Directions[EAST], chainLength);
        case WEST:
            return generatePiece(start, _pieces, rand, Pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ) + Pos(leftRightOffset, heightOffset, 1), Directions[SOUTH], chainLength);
        case EAST:
            return generatePiece(start, _pieces, rand, Pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ) + Pos(leftRightOffset, heightOffset, 1), Directions[SOUTH], chainLength);
        default:
            return NonOwningNullable<StructurePiece>();
    }
}

struct Piece;
// see StructurePiece.fillOpenings function in StructurePiece.java
void StructurePiece::fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, Random& rand) {
    // std::cout << "fillOpenings()\n";
}
// see StrongholdGenerator.Piece.fillOpenings function in StrongholdGenerator.java
void Piece::fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, Random& rand) {
    // StructurePiece::fillOpenings(start, _pieces, rand);
}
// see StrongholdGenerator.Piece.isInBounds function in StrongholdGenerator.java
bool Piece::isInBounds(BlockBox boundingBox) {
    return boundingBox.minY > 10;
}

// see StrongholdGenerator.Corridor constructor in StrongholdGenerator.java
Corridor::Corridor(const int& _chainLength, Random& rand, const BlockBox& _boundingBox, const Direction& _orientation) : Piece(_chainLength, _boundingBox, _orientation) {
    entryDoor = getRandomEntrance(rand);
    leftExitExists = rand.nextInt(2) == 0;
    rightExitExists = rand.nextInt(2) == 0;
}
// see StrongholdGenerator.Corridor.fillOpenings function in StrongholdGenerator.java
void Corridor::fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, Random& rand) {
    // std::cout << "Corridor.";
    // Piece::fillOpenings(start, _pieces, rand);
    fillForwardOpening(start, _pieces, rand, 1, 1);
    if (leftExitExists)
        fillNWOpening(start, _pieces, rand, 2, 1);
    if (rightExitExists)
        fillSEOpening(start, _pieces, rand, 2, 1);
}
// see StrongholdGenerator.Corridor.create function in StrongholdGenerator.java
OwningNullable<Corridor> Corridor::create(std::vector<Piece*>& _pieces, Random& rand, const Pos& position, const Direction& orientation, const int& chainLength) {
    BlockBox box = BlockBox::rotated(position, Vec3(-1, -1, 0), Vec3(5, 5, 7), orientation);
    if (!Corridor::isInBounds(box) || (getIntersecting(box, _pieces) != nullptr))
        return OwningNullable<Corridor>();
    box.color = "#0000ff";
    return OwningNullable(new Corridor(chainLength, rand, box, orientation));
}

// see StrongholdGenerator.PrisonHall constructor in StrongholdGenerator.java
PrisonHall::PrisonHall(const int& _chainLength, Random& rand, const BlockBox& _boundingBox, const Direction& _orientation) : Piece(_chainLength, _boundingBox, _orientation) {
    entryDoor = getRandomEntrance(rand);
}
// see StrongholdGenerator.PrisonHall.fillOpenings function in StrongholdGenerator.java
void PrisonHall::fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, Random& rand) {
    // std::cout << "PrisonHall.";
    // Piece::fillOpenings(start, _pieces, rand);
    fillForwardOpening(start, _pieces, rand, 1, 1);
}
// see StrongholdGenerator.PrisonHall.create function in StrongholdGenerator.java
OwningNullable<PrisonHall> PrisonHall::create(std::vector<Piece*>& _pieces, Random& rand, const Pos& position, const Direction& orientation, const int& chainLength) {
    BlockBox box = BlockBox::rotated(position, Vec3(-1, -1, 0), Vec3(9, 5, 11), orientation);
    if (!PrisonHall::isInBounds(box) || (getIntersecting(box, _pieces) != nullptr))
        return OwningNullable<PrisonHall>();
    return OwningNullable(new PrisonHall(chainLength, rand, box, orientation));
}

// see StrongholdGenerator.Turn.fillOpenings function in StrongholdGenerator.java
void Turn::fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, Random& rand) {
    // Piece::fillOpenings(start, _pieces, rand);
}

// see StrongholdGenerator.LeftTurn constructor in StrongholdGenerator.java
LeftTurn::LeftTurn(const int& _chainLength, Random& rand, const BlockBox& _boundingBox, const Direction& _orientation) : Turn(_chainLength, _boundingBox, _orientation) {
    entryDoor = getRandomEntrance(rand);
}
// see StrongholdGenerator.LeftTurn.fillOpenings function in StrongholdGenerator.java
void LeftTurn::fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, Random& rand) {
    // std::cout << "LeftTurn.";
    // Piece::fillOpenings(start, _pieces, rand);
    if ((orientation.index == NORTH) || (orientation.index == EAST))
        fillNWOpening(start, _pieces, rand, 1, 1);
    else
        fillSEOpening(start, _pieces, rand, 1, 1);
}
// see StrongholdGenerator.LeftTurn.create function in StrongholdGenerator.java
OwningNullable<LeftTurn> LeftTurn::create(std::vector<Piece*>& _pieces, Random& rand, const Pos& position, const Direction& orientation, const int& chainLength) {
    // std::cout << "                    LeftTurn::create()\n";
    BlockBox box = BlockBox::rotated(position, Vec3(-1, -1, 0), Vec3(5, 5, 5), orientation);
    if (!LeftTurn::isInBounds(box) || (getIntersecting(box, _pieces) != nullptr)) {
        return OwningNullable<LeftTurn>();
    }
    return OwningNullable(new LeftTurn(chainLength, rand, box, orientation));
}

// see StrongholdGenerator.RightTurn constructor in StrongholdGenerator.java
RightTurn::RightTurn(const int& _chainLength, Random& rand, const BlockBox& _boundingBox, const Direction& _orientation) : Turn(_chainLength, _boundingBox, _orientation) {
    entryDoor = getRandomEntrance(rand);
}
// see StrongholdGenerator.RightTurn.fillOpenings function in StrongholdGenerator.java
void RightTurn::fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, Random& rand) {
    // std::cout << "RightTurn.";
    // Piece::fillOpenings(start, _pieces, rand);
    if ((orientation.index == NORTH) || (orientation.index == EAST))
        fillSEOpening(start, _pieces, rand, 1, 1);
    else
        fillNWOpening(start, _pieces, rand, 1, 1);
}
// see StrongholdGenerator.RightTurn.create function in StrongholdGenerator.java
OwningNullable<RightTurn> RightTurn::create(std::vector<Piece*>& _pieces, Random& rand, const Pos& position, const Direction& orientation, const int& chainLength) {
    BlockBox box = BlockBox::rotated(position, Vec3(-1, -1, 0), Vec3(5, 5, 5), orientation);
    if (!RightTurn::isInBounds(box) || (getIntersecting(box, _pieces) != nullptr))
        return OwningNullable<RightTurn>();
    return OwningNullable(new RightTurn(chainLength, rand, box, orientation));
}

// see StrongholdGenerator.SquareRoom constructor in StrongholdGenerator.java
SquareRoom::SquareRoom(const int& _chainLength, Random& rand, const BlockBox& _boundingBox, const Direction& _orientation) : Piece(_chainLength, _boundingBox, _orientation) {
    entryDoor = getRandomEntrance(rand);
    roomType = rand.nextInt(5);
}
// see StrongholdGenerator.SquareRoom.fillOpenings function in StrongholdGenerator.java
void SquareRoom::fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, Random& rand) {
    // std::cout << "SquareRoom.";
    // Piece::fillOpenings(start, _pieces, rand);
    fillForwardOpening(start, _pieces, rand, 4, 1);
    fillNWOpening(start, _pieces, rand, 4, 1);
    fillSEOpening(start, _pieces, rand, 4, 1);
}
// see StrongholdGenerator.SquareRoom.create function in StrongholdGenerator.java
OwningNullable<SquareRoom> SquareRoom::create(std::vector<Piece*>& _pieces, Random& rand, const Pos& position, const Direction& orientation, const int& chainLength) {
    BlockBox box = BlockBox::rotated(position, Vec3(-4, -1, 0), Vec3(11, 7, 11), orientation);
    if (!SquareRoom::isInBounds(box) || (getIntersecting(box, _pieces) != nullptr))
        return OwningNullable<SquareRoom>();
    box.color = "#ff0000";
    return OwningNullable(new SquareRoom(chainLength, rand, box, orientation));
}

// see StrongholdGenerator.Stairs constructor in StrongholdGenerator.java
Stairs::Stairs(const int& _chainLength, Random& rand, const BlockBox& _boundingBox, const Direction& _orientation) : Piece(_chainLength, _boundingBox, _orientation) {
    entryDoor = getRandomEntrance(rand);
}
// see StrongholdGenerator.Stairs.fillOpenings function in StrongholdGenerator.java
void Stairs::fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, Random& rand) {
    // std::cout << "Stairs.";
    // Piece::fillOpenings(start, _pieces, rand);
    fillForwardOpening(start, _pieces, rand, 1, 1);
}
// see StrongholdGenerator.Stairs.create function in StrongholdGenerator.java
OwningNullable<Stairs> Stairs::create(std::vector<Piece*>& _pieces, Random& rand, const Pos& position, const Direction& orientation, const int& chainLength) {
    BlockBox box = BlockBox::rotated(position, Vec3(-1, -7, 0), Vec3(5, 11, 8), orientation);
    if (!Stairs::isInBounds(box) || (getIntersecting(box, _pieces) != nullptr))
        return OwningNullable<Stairs>();
    box.color = "#00ff00";
    return OwningNullable(new Stairs(chainLength, rand, box, orientation));
}

// see StrongholdGenerator.SpiralStaircase constructor in StrongholdGenerator.java
SpiralStaircase::SpiralStaircase(const int& _chainLength, const Pos& position, const Direction& _orientation) : Piece(_chainLength, BlockBox({ position.x, 64, position.z }, 5, 11, 5, _orientation), _orientation), isStart(true) {
    entryDoor = EntranceType::OPENING;
}
SpiralStaircase::SpiralStaircase(const int& _chainLength, Random& rand, const BlockBox& _boundingBox, const Direction& _orientation) : Piece(_chainLength, _boundingBox, _orientation), isStart(false) {
    entryDoor = getRandomEntrance(rand);
}
// see StrongholdGenerator.SpiralStaircase.fillOpenings function in StrongholdGenerator.java
void SpiralStaircase::fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, Random& rand) {
    // std::cout << "SpiralStaircase.";
    // Piece::fillOpenings(start, _pieces, rand);
    if (isStart)
        activePieceType = PieceTypeFiveWayCrossing;
    fillForwardOpening(start, _pieces, rand, 1, 1);
}
// see StrongholdGenerator.SpiralStaircase.create function in StrongholdGenerator.java
OwningNullable<SpiralStaircase> SpiralStaircase::create(std::vector<Piece*>& _pieces, Random& rand, const Pos& position, const Direction& orientation, const int& chainLength) {
    BlockBox box = BlockBox::rotated(position, Vec3(-1, -7, 0), Vec3(5, 11, 5), orientation);
    if (!SpiralStaircase::isInBounds(box) || (getIntersecting(box, _pieces) != nullptr))
        return OwningNullable<SpiralStaircase>();
    box.color = "#00ff00";
    return OwningNullable(new SpiralStaircase(chainLength, rand, box, orientation));
}

// see StrongholdGenerator.FiveWayCrossing constructor in StrongholdGenerator.java
FiveWayCrossing::FiveWayCrossing(const int& _chainLength, Random& rand, const BlockBox& _boundingBox, const Direction& _orientation) : Piece(_chainLength, _boundingBox, _orientation) {
    entryDoor = getRandomEntrance(rand);
    lowerLeftExists = rand.nextBoolean();
    upperLeftExists = rand.nextBoolean();
    lowerRightExists = rand.nextBoolean();
    upperRightExists = rand.nextInt(3) > 0;// 66.666% chance
}
// see StrongholdGenerator.FiveWayCrossing.fillOpenings function in StrongholdGenerator.java
void FiveWayCrossing::fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, Random& rand) {
    // std::cout << "FiveWayCrossing.";
    // Piece::fillOpenings(start, _pieces, rand);
    int corner13heightOffset = 3;
    int corner24heightOffset = 5;
    if ((orientation.index == WEST) || (orientation.index == NORTH)) {
        corner13heightOffset = 8-corner13heightOffset;
        corner24heightOffset = 8-corner24heightOffset;
    }
    fillForwardOpening(start, _pieces, rand, 5, 1);
    if (lowerLeftExists)
        fillNWOpening(start, _pieces, rand, 1, corner13heightOffset);
    if (upperLeftExists)
        fillNWOpening(start, _pieces, rand, 7, corner24heightOffset);
    if (lowerRightExists)
        fillSEOpening(start, _pieces, rand, 1, corner13heightOffset);
    if (upperRightExists)
        fillSEOpening(start, _pieces, rand, 7, corner24heightOffset);
}
// see StrongholdGenerator.FiveWayCrossing.create function in StrongholdGenerator.java
OwningNullable<FiveWayCrossing> FiveWayCrossing::create(std::vector<Piece*>& _pieces, Random& rand, const Pos& position, const Direction& orientation, const int& chainLength) {
    BlockBox box = BlockBox::rotated(position, Vec3(-4, -3, 0), Vec3(10, 9, 11), orientation);
    if (!FiveWayCrossing::isInBounds(box) || (getIntersecting(box, _pieces) != nullptr))
        return OwningNullable<FiveWayCrossing>();
    box.color = "#ff0000";
    return OwningNullable(new FiveWayCrossing(chainLength, rand, box, orientation));
}

// see StrongholdGenerator.ChestCorridor constructor in StrongholdGenerator.java
ChestCorridor::ChestCorridor(const int& _chainLength, Random& rand, const BlockBox& _boundingBox, const Direction& _orientation) : Piece(_chainLength, _boundingBox, _orientation) {
    entryDoor = getRandomEntrance(rand);
}
// see StrongholdGenerator.ChestCorridor.fillOpenings function in StrongholdGenerator.java
void ChestCorridor::fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, Random& rand) {
    // std::cout << "ChestCorridor.";
    // Piece::fillOpenings(start, _pieces, rand);
    fillForwardOpening(start, _pieces, rand, 1, 1);
}
// see StrongholdGenerator.ChestCorridor.create function in StrongholdGenerator.java
OwningNullable<ChestCorridor> ChestCorridor::create(std::vector<Piece*>& _pieces, Random& rand, const Pos& position, const Direction& orientation, const int& chainLength) {
    BlockBox box = BlockBox::rotated(position, Vec3(-1, -1, 0), Vec3(5, 5, 7), orientation);
    if (!ChestCorridor::isInBounds(box) || (getIntersecting(box, _pieces) != nullptr))
        return OwningNullable<ChestCorridor>();
    box.color = "#804000";
    return OwningNullable(new ChestCorridor(chainLength, rand, box, orientation));
}

// see StrongholdGenerator.Library constructor in StrongholdGenerator.java
Library::Library(const int& _chainLength, Random& rand, const BlockBox& _boundingBox, const Direction& _orientation) : Piece(_chainLength, _boundingBox, _orientation) {
    entryDoor = getRandomEntrance(rand);
    isTall = (boundingBox.maxY - boundingBox.minY + 1) > 6;
}
// see StrongholdGenerator.Library.create function in StrongholdGenerator.java
OwningNullable<Library> Library::create(std::vector<Piece*>& _pieces, Random& rand, const Pos& position, const Direction& orientation, const int& chainLength) {
    BlockBox box = BlockBox::rotated(position, Vec3(-4, -1, 0), Vec3(14, 11, 15), orientation);
    if (!Library::isInBounds(box) || (getIntersecting(box, _pieces) != nullptr)) {
        box = BlockBox::rotated(position, Vec3(-4, -1, 0), Vec3(14, 6, 15), orientation);
        if (!Library::isInBounds(box) || (getIntersecting(box, _pieces) != nullptr))
            return OwningNullable<Library>();
    }
    box.color = "#804000";
    return OwningNullable(new Library(chainLength, rand, box, orientation));
}

// see StrongholdGenerator.PortalRoom constructor in StrongholdGenerator.java
PortalRoom::PortalRoom(const int& _chainLength, const BlockBox& _boundingBox, const Direction& _orientation) : Piece(_chainLength, _boundingBox, _orientation) {}
// see StrongholdGenerator.PortalRoom.fillOpenings function in StrongholdGenerator.java
void PortalRoom::fillOpenings(StructurePiece& start, std::vector<Piece*>& _pieces, Random& rand) {
    // std::cout << "PortalRoom.";
    // Piece::fillOpenings(start, _pieces, rand);
    ((Start&)start).portalRoom = this;
}
// see StrongholdGenerator.PortalRoom.create function in StrongholdGenerator.java
OwningNullable<PortalRoom> PortalRoom::create(std::vector<Piece*>& _pieces, const Pos& position, const Direction& orientation, const int& chainLength) {
    BlockBox box = BlockBox::rotated(position, Vec3(-4, -1, 0), Vec3(11, 8, 16), orientation);
    if (!PortalRoom::isInBounds(box) || (getIntersecting(box, _pieces) != nullptr))
        return OwningNullable<PortalRoom>();
        box.color = "#ff00ff";
    return OwningNullable(new PortalRoom(chainLength, box, orientation));
}

SmallCorridor::SmallCorridor(const int& _chainLength, const BlockBox& _boundingBox, const Direction& _orientation) : Piece(_chainLength, _boundingBox, _orientation) {
    length = (orientation.direction.z != 0) ? (boundingBox.maxZ - boundingBox.minZ + 1) : (boundingBox.maxX - boundingBox.minX + 1);// blockCountZ if facing positive or negative z otherwise, blockCountX
}
// see StrongholdGenerator.SmallCorridor.create function in StrongholdGenerator.java
OwningNullable<BlockBox> SmallCorridor::create(std::vector<Piece*>& _pieces, Random& rand, const Pos& position, const Direction& orientation) {
    BlockBox box = BlockBox::rotated(position, Vec3(-1, -1, 0), Vec3(5, 5, 4), orientation);
    BlockBox* intersectingBox = getIntersecting(box, _pieces);
    if (intersectingBox == nullptr) return OwningNullable<BlockBox>();
    if (intersectingBox->minY == box.minY) {
        for (int i = 2; i >= 1; i--) {
            BlockBox attempt = BlockBox::rotated(position, Vec3(-1, -1, 0), Vec3(5, 5, i), orientation);
            if (intersectingBox->intersects(attempt)) continue;
            OwningNullable returnVal = OwningNullable(BlockBox::newRotated(position, Vec3(-1, -1, 0), Vec3(5, 5, i + 1), orientation));
            returnVal.getValue().color = "#0000ff";
            return returnVal;
        }
    }
    return OwningNullable<BlockBox>();
}

// see StrongholdGenerator.Start constructor in StrongholdGenerator.java
Start::Start(Random& rand, const Pos& position) : SpiralStaircase(0, position, getRandomHorizontalDirection(rand)) {
    boundingBox.color = "#00ff00";
    for (size_t i = 0; i < allPieces.size(); i++) {
        // std::cout << "populating possiblePieces with \"" << allPieceNames[allPieces[i].type] << "\"\n";
        possiblePieces.push_back(allPieces[i]);
    }
}
// see StrongholdGenerator.StoneBrickRandomizer.setBlock function in StrongholdGenerator.java
void StoneBrickRandomizer::setBlock(Random& rand, const Pos& position, const bool& placeBlock) {
    float $$5;
    if (placeBlock) {
        rand.nextSeed();
        /*float rnd = rand.nextFloat();
        if (rnd < 0.2)
            block = Blocks[BlockIndices::CRACKED_STONE_BRICKS]
        else if (rnd < 0.5)
            block = Blocks[BlockIndices::MOSSY_STONE_BRICKS]
        else if (rnd < 0.55)
            block = Blocks[BlockIndices::INFESTED_STONE_BRICKS]
        else
            block = Blocks[BlockIndices::STONE_BRICKS] */
        // 20% for cracked stone bricks
        // 30% for mossy stone bricks
        // 5% for infested stone bricks
        // 45% for stone bricks
    } else {
        // block = Blocks.CAVE_AIR;
    }
}
//  see ChunkGenerator.getSeaLevel abstract function in ChunkGenerator.java
//      NoiseChunkGenerator.getSeaLevel function in getSeaLevel.java
//          ChunkGeneratorSettings.seaLevel variable
//              // gets its value from the "sea_level" field in /data/minecraft/world_gen/noise_settings/overworld.json
//  only applies to overworld
int getSeaLevel() {
    return 63;
}
//  see ChunkGenerator.getMinimumY abstract function in ChunkGenerator.java
//      NoiseChunkGenerator.getMinimumY function in getSeaLevel.java
//          GenerationShapeConfig.minimumY
//              // gets its value from "min_y" field in /data/minecraft/dimension_type/overworld.json
//  only applies to overworld
int getMinimumY() {
    return -64;
}
//  see StructurePiecesCollector.getBoundingBox function in StructurePiecesCollector.java
//      StructurePiece.boundingBox static function in StructurePiece.java
//          see BlockBox.encompass(BlockBox[]) static function in BlockBox.java
BlockBox getBoundingBox(std::vector<Piece*>& pieces) {
    BlockBox outBox = pieces[0]->boundingBox;
    for (size_t i = 1; i < pieces.size(); i++) outBox.encompass(pieces[i]->boundingBox);
    return outBox;
}
//  see StructurePiecesCollector.shift function in StructurePiecesCollector.java
//      StructurePiece.translate function in StructurePiece.java
void shift(std::vector<Piece*>& pieces, const int& yAmount) {
    for (size_t i = 0; i < pieces.size(); i++) pieces[i]->boundingBox.moveY(yAmount);
}
// see StructurePiecesCollector.shiftInto function in StructurePiecesCollector.java
void shiftInto(std::vector<Piece*>& pieces, const int& topY, const int& bottomY, Random& rand, const int& topPenalty) {
    int preferedTop = topY - topPenalty;
    BlockBox boundingBox = getBoundingBox(pieces);
    int yLevel = boundingBox.getBlockCountY() + bottomY + 1;
    if (yLevel < preferedTop) {
        yLevel += rand.nextInt(preferedTop - yLevel);
    }
    yLevel -= boundingBox.maxY;
    shift(pieces, yLevel);
}
// see StructurePlacementCalculator.calculateConcentricsRingPlacementPos function in StructurePlacementCalculator.java
OwningNullable<StartEndBoxes> getPortalRoomPosition(const long long int& worldSeed, const ChunkPos& chunk, const bool& debug) {
    std::vector<Piece*> pieces;
    Random rand(worldSeed);
    Start* start = nullptr;
    int i = 0;
    bool firstRun = true;
    Pos startPos = chunk.getOffsetPos({ 2, 0, 2 });
    while ((pieces.size() == 0) || (start == nullptr) || (start->portalRoom == nullptr)) {
        if (debug) std::cout << "attempt#" << (i + 1) << '\n';
        if (!firstRun) {
            for (size_t j = 0; j < pieces.size(); j++)
                delete pieces[j];
            pieces.clear();
        }
        firstRun = false;
        rand.setCarverSeed(worldSeed + (long long int)i++, chunk);
        start = new Start(rand, startPos);
        pieces.push_back(start);
        start->fillOpenings(*start, pieces, rand);
        std::vector<Piece*>& start_pieces = start->pieces;
        while (start_pieces.size() > 0) {
            int randIndex = rand.nextInt(start_pieces.size());
            StructurePiece* randPiece = start_pieces[randIndex];
            start_pieces.erase(start_pieces.cbegin() + randIndex);
            randPiece->fillOpenings(*start, pieces, rand);
        }
        if (debug) std::cout << "    portal room " << ((start->portalRoom == nullptr) ? "was NOT" : "WAS") << " generated.\n";
    }
    if (debug) std::cout << "portal room generated after " << i << " attempt(s).\n";
    if (start->portalRoom != nullptr) {
        shiftInto(pieces, getSeaLevel(), getMinimumY(), rand, 10);
        OwningNullable<StartEndBoxes> output = OwningNullable<StartEndBoxes>(new StartEndBoxes(start->boundingBox.getBottomCenter() + Pos(0, 1, 0), start->portalRoom->boundingBox.getCenter()));
        std::vector<BlockBox>& boxes = output.getValue().boxes;
        for (unsigned int i = 0; i < pieces.size(); i++) boxes.push_back(pieces[i]->boundingBox);
        for (size_t i = 0; i < pieces.size(); i++) delete pieces[i];
        return output;
    }
    for (size_t i = 0; i < pieces.size(); i++) delete pieces[i];
    return OwningNullable<StartEndBoxes>();
}