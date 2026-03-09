/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.StructureManager;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.FenceBlock;
/*      */ import net.minecraft.world.level.block.StairBlock;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ import net.minecraft.world.level.material.Fluids;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ 
/*      */ 
/*      */ 
/*      */ public class NetherFortressPieces
/*      */ {
/*      */   private static final int MAX_DEPTH = 30;
/*      */   private static final int LOWEST_Y_POSITION = 10;
/*      */   public static final int MAGIC_START_Y = 64;
/*      */   
/*      */   private static class PieceWeight
/*      */   {
/*      */     public final Class<? extends NetherFortressPieces.NetherBridgePiece> pieceClass;
/*      */     public final int weight;
/*      */     public int placeCount;
/*      */     public final int maxPlaceCount;
/*      */     public final boolean allowInRow;
/*      */     
/*      */     public PieceWeight(Class<? extends NetherFortressPieces.NetherBridgePiece> pieceClass, int weight, int maxPlaceCount, boolean allowInRow) {
/*   46 */       this.pieceClass = pieceClass;
/*   47 */       this.weight = weight;
/*   48 */       this.maxPlaceCount = maxPlaceCount;
/*   49 */       this.allowInRow = allowInRow;
/*      */     }
/*      */ 
/*      */     
/*   53 */     public PieceWeight(Class<? extends NetherFortressPieces.NetherBridgePiece> pieceClass, int weight, int maxPlaceCount) { this(pieceClass, weight, maxPlaceCount, false); }
/*      */ 
/*      */ 
/*      */     
/*   57 */     public boolean doPlace(int depth) { return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount); }
/*      */ 
/*      */ 
/*      */     
/*   61 */     public boolean isValid() { return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount); }
/*      */   }
/*      */ 
/*      */   
/*   65 */   private static final PieceWeight[] BRIDGE_PIECE_WEIGHTS = { new PieceWeight(BridgeStraight.class, 30, 0, true), new PieceWeight(BridgeCrossing.class, 10, 4), new PieceWeight(RoomCrossing.class, 10, 4), new PieceWeight(StairsRoom.class, 10, 3), new PieceWeight(MonsterThrone.class, 5, 2), new PieceWeight(CastleEntrance.class, 5, 1) };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   73 */   private static final PieceWeight[] CASTLE_PIECE_WEIGHTS = { new PieceWeight(CastleSmallCorridorPiece.class, 25, 0, true), new PieceWeight(CastleSmallCorridorCrossingPiece.class, 15, 5), new PieceWeight(CastleSmallCorridorRightTurnPiece.class, 5, 10), new PieceWeight(CastleSmallCorridorLeftTurnPiece.class, 5, 10), new PieceWeight(CastleCorridorStairsPiece.class, 10, 3, true), new PieceWeight(CastleCorridorTBalconyPiece.class, 7, 2), new PieceWeight(CastleStalkRoom.class, 5, 2) };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static NetherBridgePiece findAndCreateBridgePieceFactory(PieceWeight piece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth) {
/*   84 */     Class<? extends NetherBridgePiece> pieceClass = piece.pieceClass;
/*   85 */     NetherBridgePiece structurePiece = null;
/*      */     
/*   87 */     if (pieceClass == BridgeStraight.class) {
/*   88 */       structurePiece = BridgeStraight.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*   89 */     } else if (pieceClass == BridgeCrossing.class) {
/*   90 */       structurePiece = BridgeCrossing.createPiece(structurePieceAccessor, footX, footY, footZ, direction, depth);
/*   91 */     } else if (pieceClass == RoomCrossing.class) {
/*   92 */       structurePiece = RoomCrossing.createPiece(structurePieceAccessor, footX, footY, footZ, direction, depth);
/*   93 */     } else if (pieceClass == StairsRoom.class) {
/*   94 */       structurePiece = StairsRoom.createPiece(structurePieceAccessor, footX, footY, footZ, depth, direction);
/*   95 */     } else if (pieceClass == MonsterThrone.class) {
/*   96 */       structurePiece = MonsterThrone.createPiece(structurePieceAccessor, footX, footY, footZ, depth, direction);
/*   97 */     } else if (pieceClass == CastleEntrance.class) {
/*   98 */       structurePiece = CastleEntrance.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*   99 */     } else if (pieceClass == CastleSmallCorridorPiece.class) {
/*  100 */       structurePiece = CastleSmallCorridorPiece.createPiece(structurePieceAccessor, footX, footY, footZ, direction, depth);
/*  101 */     } else if (pieceClass == CastleSmallCorridorRightTurnPiece.class) {
/*  102 */       structurePiece = CastleSmallCorridorRightTurnPiece.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  103 */     } else if (pieceClass == CastleSmallCorridorLeftTurnPiece.class) {
/*  104 */       structurePiece = CastleSmallCorridorLeftTurnPiece.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  105 */     } else if (pieceClass == CastleCorridorStairsPiece.class) {
/*  106 */       structurePiece = CastleCorridorStairsPiece.createPiece(structurePieceAccessor, footX, footY, footZ, direction, depth);
/*  107 */     } else if (pieceClass == CastleCorridorTBalconyPiece.class) {
/*  108 */       structurePiece = CastleCorridorTBalconyPiece.createPiece(structurePieceAccessor, footX, footY, footZ, direction, depth);
/*  109 */     } else if (pieceClass == CastleSmallCorridorCrossingPiece.class) {
/*  110 */       structurePiece = CastleSmallCorridorCrossingPiece.createPiece(structurePieceAccessor, footX, footY, footZ, direction, depth);
/*  111 */     } else if (pieceClass == CastleStalkRoom.class) {
/*  112 */       structurePiece = CastleStalkRoom.createPiece(structurePieceAccessor, footX, footY, footZ, direction, depth);
/*      */     } 
/*  114 */     return structurePiece;
/*      */   }
/*      */   
/*      */   private static abstract class NetherBridgePiece
/*      */     extends StructurePiece {
/*  119 */     protected NetherBridgePiece(StructurePieceType type, int genDepth, BoundingBox boundingBox) { super(type, genDepth, boundingBox); }
/*      */ 
/*      */ 
/*      */     
/*  123 */     public NetherBridgePiece(StructurePieceType type, CompoundTag tag) { super(type, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {}
/*      */ 
/*      */     
/*      */     private int updatePieceWeight(List<NetherFortressPieces.PieceWeight> currentPieces) {
/*  131 */       boolean hasAnyPieces = false;
/*  132 */       int totalWeight = 0;
/*  133 */       for (NetherFortressPieces.PieceWeight piece : currentPieces) {
/*  134 */         if (piece.maxPlaceCount > 0 && piece.placeCount < piece.maxPlaceCount) {
/*  135 */           hasAnyPieces = true;
/*      */         }
/*  137 */         totalWeight += piece.weight;
/*      */       } 
/*  139 */       return hasAnyPieces ? totalWeight : -1;
/*      */     }
/*      */     
/*      */     private NetherBridgePiece generatePiece(NetherFortressPieces.StartPiece startPiece, List<NetherFortressPieces.PieceWeight> currentPieces, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth) {
/*  143 */       int totalWeight = updatePieceWeight(currentPieces);
/*  144 */       boolean doStuff = (totalWeight > 0 && depth <= 30);
/*      */       
/*  146 */       int numAttempts = 0;
/*  147 */       while (numAttempts < 5 && doStuff) {
/*  148 */         numAttempts++;
/*      */         
/*  150 */         int weightSelection = random.nextInt(totalWeight);
/*  151 */         for (NetherFortressPieces.PieceWeight piece : currentPieces) {
/*  152 */           weightSelection -= piece.weight;
/*  153 */           if (weightSelection < 0) {
/*  154 */             if (!piece.doPlace(depth) || (piece == startPiece.previousPiece && !piece.allowInRow)) {
/*      */               break;
/*      */             }
/*      */             
/*  158 */             NetherBridgePiece structurePiece = NetherFortressPieces.findAndCreateBridgePieceFactory(piece, structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  159 */             if (structurePiece != null) {
/*  160 */               piece.placeCount++;
/*  161 */               startPiece.previousPiece = piece;
/*      */               
/*  163 */               if (!piece.isValid()) {
/*  164 */                 currentPieces.remove(piece);
/*      */               }
/*  166 */               return structurePiece;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*  171 */       return NetherFortressPieces.BridgeEndFiller.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*      */     }
/*      */     
/*      */     private StructurePiece generateAndAddPiece(NetherFortressPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth, boolean isCastle) {
/*  175 */       if (Math.abs(footX - startPiece.getBoundingBox().minX()) > 112 || Math.abs(footZ - startPiece.getBoundingBox().minZ()) > 112) {
/*  176 */         return NetherFortressPieces.BridgeEndFiller.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*      */       }
/*  178 */       List<NetherFortressPieces.PieceWeight> availablePieces = startPiece.availableBridgePieces;
/*  179 */       if (isCastle) {
/*  180 */         availablePieces = startPiece.availableCastlePieces;
/*      */       }
/*  182 */       StructurePiece newPiece = generatePiece(startPiece, availablePieces, structurePieceAccessor, random, footX, footY, footZ, direction, depth + 1);
/*  183 */       if (newPiece != null) {
/*  184 */         structurePieceAccessor.addPiece(newPiece);
/*  185 */         startPiece.pendingChildren.add(newPiece);
/*      */       } 
/*  187 */       return newPiece;
/*      */     }
/*      */     
/*      */     protected StructurePiece generateChildForward(NetherFortressPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int xOff, int yOff, boolean isCastle) {
/*  191 */       Direction orientation = getOrientation();
/*  192 */       if (orientation != null) {
/*  193 */         switch (NetherFortressPieces.null.$SwitchMap$net$minecraft$core$Direction[orientation.ordinal()]) {
/*      */           case 1:
/*  195 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + xOff, this.boundingBox.minY() + yOff, this.boundingBox.minZ() - 1, orientation, getGenDepth(), isCastle);
/*      */           case 2:
/*  197 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + xOff, this.boundingBox.minY() + yOff, this.boundingBox.maxZ() + 1, orientation, getGenDepth(), isCastle);
/*      */           case 3:
/*  199 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + xOff, orientation, getGenDepth(), isCastle);
/*      */           case 4:
/*  201 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + xOff, orientation, getGenDepth(), isCastle);
/*      */         } 
/*      */       }
/*  204 */       return null;
/*      */     }
/*      */     
/*      */     protected StructurePiece generateChildLeft(NetherFortressPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int yOff, int zOff, boolean isCastle) {
/*  208 */       Direction orientation = getOrientation();
/*  209 */       if (orientation != null) {
/*  210 */         switch (NetherFortressPieces.null.$SwitchMap$net$minecraft$core$Direction[orientation.ordinal()]) {
/*      */           case 1:
/*  212 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.WEST, getGenDepth(), isCastle);
/*      */           case 2:
/*  214 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.WEST, getGenDepth(), isCastle);
/*      */           case 3:
/*  216 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.minZ() - 1, Direction.NORTH, getGenDepth(), isCastle);
/*      */           case 4:
/*  218 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.minZ() - 1, Direction.NORTH, getGenDepth(), isCastle);
/*      */         } 
/*      */       }
/*  221 */       return null;
/*      */     }
/*      */     
/*      */     protected StructurePiece generateChildRight(NetherFortressPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int yOff, int zOff, boolean isCastle) {
/*  225 */       Direction orientation = getOrientation();
/*  226 */       if (orientation != null) {
/*  227 */         switch (NetherFortressPieces.null.$SwitchMap$net$minecraft$core$Direction[orientation.ordinal()]) {
/*      */           case 1:
/*  229 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.EAST, getGenDepth(), isCastle);
/*      */           case 2:
/*  231 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.EAST, getGenDepth(), isCastle);
/*      */           case 3:
/*  233 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.maxZ() + 1, Direction.SOUTH, getGenDepth(), isCastle);
/*      */           case 4:
/*  235 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.maxZ() + 1, Direction.SOUTH, getGenDepth(), isCastle);
/*      */         } 
/*      */       }
/*  238 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  242 */     protected static boolean isOkBox(BoundingBox box) { return (box.minY() > 10); }
/*      */   }
/*      */   
/*      */   public static class StartPiece
/*      */     extends BridgeCrossing
/*      */   {
/*      */     private NetherFortressPieces.PieceWeight previousPiece;
/*  249 */     private final List<NetherFortressPieces.PieceWeight> availableBridgePieces = new ArrayList();
/*  250 */     private final List<NetherFortressPieces.PieceWeight> availableCastlePieces = new ArrayList();
/*      */ 
/*      */     
/*  253 */     public final List<StructurePiece> pendingChildren = Lists.newArrayList();
/*      */     
/*      */     public StartPiece(RandomSource random, int west, int north) {
/*  256 */       super(west, north, getRandomHorizontalDirection(random));
/*      */       
/*  258 */       for (NetherFortressPieces.PieceWeight piece : NetherFortressPieces.BRIDGE_PIECE_WEIGHTS) {
/*  259 */         piece.placeCount = 0;
/*  260 */         this.availableBridgePieces.add(piece);
/*      */       } 
/*      */       
/*  263 */       for (NetherFortressPieces.PieceWeight piece : NetherFortressPieces.CASTLE_PIECE_WEIGHTS) {
/*  264 */         piece.placeCount = 0;
/*  265 */         this.availableCastlePieces.add(piece);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  270 */     public StartPiece(CompoundTag tag) { super(StructurePieceType.NETHER_FORTRESS_START, tag); }
/*      */   }
/*      */   
/*      */   public static class BridgeStraight
/*      */     extends NetherBridgePiece {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 10;
/*      */     private static final int DEPTH = 19;
/*      */     
/*      */     public BridgeStraight(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  280 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_STRAIGHT, genDepth, boundingBox);
/*      */       
/*  282 */       setOrientation(direction);
/*      */     }
/*      */ 
/*      */     
/*  286 */     public BridgeStraight(CompoundTag tag) { super(StructurePieceType.NETHER_FORTRESS_BRIDGE_STRAIGHT, tag); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  291 */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) { generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 3, false); }
/*      */ 
/*      */     
/*      */     public static BridgeStraight createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  295 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -3, 0, 5, 10, 19, direction);
/*      */       
/*  297 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/*  298 */         return null;
/*      */       }
/*      */       
/*  301 */       return new BridgeStraight(genDepth, random, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  307 */       generateBox(level, chunkBB, 0, 3, 0, 4, 4, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  309 */       generateBox(level, chunkBB, 1, 5, 0, 3, 7, 18, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  312 */       generateBox(level, chunkBB, 0, 5, 0, 0, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  313 */       generateBox(level, chunkBB, 4, 5, 0, 4, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  316 */       generateBox(level, chunkBB, 0, 2, 0, 4, 2, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  317 */       generateBox(level, chunkBB, 0, 2, 13, 4, 2, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  318 */       generateBox(level, chunkBB, 0, 0, 0, 4, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  319 */       generateBox(level, chunkBB, 0, 0, 15, 4, 1, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  321 */       for (int x = 0; x <= 4; x++) {
/*  322 */         for (int z = 0; z <= 2; z++) {
/*  323 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
/*  324 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, 18 - z, chunkBB);
/*      */         } 
/*      */       } 
/*      */       
/*  328 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.SOUTH, Boolean.valueOf(true));
/*  329 */       BlockState nseFence = (BlockState)nsFence.setValue(FenceBlock.EAST, Boolean.valueOf(true));
/*  330 */       BlockState nswFence = (BlockState)nsFence.setValue(FenceBlock.WEST, Boolean.valueOf(true));
/*  331 */       generateBox(level, chunkBB, 0, 1, 1, 0, 4, 1, nseFence, nseFence, false);
/*  332 */       generateBox(level, chunkBB, 0, 3, 4, 0, 4, 4, nseFence, nseFence, false);
/*  333 */       generateBox(level, chunkBB, 0, 3, 14, 0, 4, 14, nseFence, nseFence, false);
/*  334 */       generateBox(level, chunkBB, 0, 1, 17, 0, 4, 17, nseFence, nseFence, false);
/*  335 */       generateBox(level, chunkBB, 4, 1, 1, 4, 4, 1, nswFence, nswFence, false);
/*  336 */       generateBox(level, chunkBB, 4, 3, 4, 4, 4, 4, nswFence, nswFence, false);
/*  337 */       generateBox(level, chunkBB, 4, 3, 14, 4, 4, 14, nswFence, nswFence, false);
/*  338 */       generateBox(level, chunkBB, 4, 1, 17, 4, 4, 17, nswFence, nswFence, false);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class BridgeEndFiller
/*      */     extends NetherBridgePiece {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 10;
/*      */     private static final int DEPTH = 8;
/*      */     private final int selfSeed;
/*      */     
/*      */     public BridgeEndFiller(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  350 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_END_FILLER, genDepth, boundingBox);
/*      */       
/*  352 */       setOrientation(direction);
/*  353 */       this.selfSeed = random.nextInt();
/*      */     }
/*      */     
/*      */     public BridgeEndFiller(CompoundTag tag) {
/*  357 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_END_FILLER, tag);
/*  358 */       this.selfSeed = tag.getIntOr("Seed", 0);
/*      */     }
/*      */     
/*      */     public static BridgeEndFiller createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  362 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -3, 0, 5, 10, 8, direction);
/*      */       
/*  364 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/*  365 */         return null;
/*      */       }
/*      */       
/*  368 */       return new BridgeEndFiller(genDepth, random, box, direction);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  373 */       super.addAdditionalSaveData(context, tag);
/*      */       
/*  375 */       tag.putInt("Seed", this.selfSeed);
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  380 */       RandomSource selfRandom = RandomSource.create(this.selfSeed);
/*      */ 
/*      */       
/*  383 */       for (int x = 0; x <= 4; x++) {
/*  384 */         for (int y = 3; y <= 4; y++) {
/*  385 */           int z = selfRandom.nextInt(8);
/*  386 */           generateBox(level, chunkBB, x, y, 0, x, y, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  392 */       int z = selfRandom.nextInt(8);
/*  393 */       generateBox(level, chunkBB, 0, 5, 0, 0, 5, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  396 */       int z = selfRandom.nextInt(8);
/*  397 */       generateBox(level, chunkBB, 4, 5, 0, 4, 5, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */ 
/*      */       
/*  401 */       for (int x = 0; x <= 4; x++) {
/*  402 */         int z = selfRandom.nextInt(5);
/*  403 */         generateBox(level, chunkBB, x, 2, 0, x, 2, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       } 
/*  405 */       for (int x = 0; x <= 4; x++) {
/*  406 */         for (int y = 0; y <= 1; y++) {
/*  407 */           int z = selfRandom.nextInt(3);
/*  408 */           generateBox(level, chunkBB, x, y, 0, x, y, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class BridgeCrossing extends NetherBridgePiece {
/*      */     private static final int WIDTH = 19;
/*      */     private static final int HEIGHT = 10;
/*      */     private static final int DEPTH = 19;
/*      */     
/*      */     public BridgeCrossing(int genDepth, BoundingBox boundingBox, Direction direction) {
/*  420 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, genDepth, boundingBox);
/*      */       
/*  422 */       setOrientation(direction);
/*      */     }
/*      */     
/*      */     protected BridgeCrossing(int west, int north, Direction direction) {
/*  426 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, 0, StructurePiece.makeBoundingBox(west, 64, north, direction, 19, 10, 19));
/*      */       
/*  428 */       setOrientation(direction);
/*      */     }
/*      */ 
/*      */     
/*  432 */     protected BridgeCrossing(StructurePieceType type, CompoundTag tag) { super(type, tag); }
/*      */ 
/*      */ 
/*      */     
/*  436 */     public BridgeCrossing(CompoundTag tag) { this(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  441 */       generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 8, 3, false);
/*  442 */       generateChildLeft((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 3, 8, false);
/*  443 */       generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 3, 8, false);
/*      */     }
/*      */     
/*      */     public static BridgeCrossing createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  447 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -8, -3, 0, 19, 10, 19, direction);
/*      */       
/*  449 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/*  450 */         return null;
/*      */       }
/*      */       
/*  453 */       return new BridgeCrossing(genDepth, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  459 */       generateBox(level, chunkBB, 7, 3, 0, 11, 4, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  460 */       generateBox(level, chunkBB, 0, 3, 7, 18, 4, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  462 */       generateBox(level, chunkBB, 8, 5, 0, 10, 7, 18, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  463 */       generateBox(level, chunkBB, 0, 5, 8, 18, 7, 10, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */       
/*  465 */       generateBox(level, chunkBB, 7, 5, 0, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  466 */       generateBox(level, chunkBB, 7, 5, 11, 7, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  467 */       generateBox(level, chunkBB, 11, 5, 0, 11, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  468 */       generateBox(level, chunkBB, 11, 5, 11, 11, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  469 */       generateBox(level, chunkBB, 0, 5, 7, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  470 */       generateBox(level, chunkBB, 11, 5, 7, 18, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  471 */       generateBox(level, chunkBB, 0, 5, 11, 7, 5, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  472 */       generateBox(level, chunkBB, 11, 5, 11, 18, 5, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  475 */       generateBox(level, chunkBB, 7, 2, 0, 11, 2, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  476 */       generateBox(level, chunkBB, 7, 2, 13, 11, 2, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  477 */       generateBox(level, chunkBB, 7, 0, 0, 11, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  478 */       generateBox(level, chunkBB, 7, 0, 15, 11, 1, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  479 */       for (int x = 7; x <= 11; x++) {
/*  480 */         for (int z = 0; z <= 2; z++) {
/*  481 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
/*  482 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, 18 - z, chunkBB);
/*      */         } 
/*      */       } 
/*      */       
/*  486 */       generateBox(level, chunkBB, 0, 2, 7, 5, 2, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  487 */       generateBox(level, chunkBB, 13, 2, 7, 18, 2, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  488 */       generateBox(level, chunkBB, 0, 0, 7, 3, 1, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  489 */       generateBox(level, chunkBB, 15, 0, 7, 18, 1, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  490 */       for (int x = 0; x <= 2; x++) {
/*  491 */         for (int z = 7; z <= 11; z++) {
/*  492 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
/*  493 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), 18 - x, -1, z, chunkBB);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class RoomCrossing extends NetherBridgePiece {
/*      */     private static final int WIDTH = 7;
/*      */     private static final int HEIGHT = 9;
/*      */     private static final int DEPTH = 7;
/*      */     
/*      */     public RoomCrossing(int genDepth, BoundingBox boundingBox, Direction direction) {
/*  505 */       super(StructurePieceType.NETHER_FORTRESS_ROOM_CROSSING, genDepth, boundingBox);
/*      */       
/*  507 */       setOrientation(direction);
/*      */     }
/*      */ 
/*      */     
/*  511 */     public RoomCrossing(CompoundTag tag) { super(StructurePieceType.NETHER_FORTRESS_ROOM_CROSSING, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  516 */       generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 2, 0, false);
/*  517 */       generateChildLeft((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 2, false);
/*  518 */       generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 2, false);
/*      */     }
/*      */     
/*      */     public static RoomCrossing createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  522 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -2, 0, 0, 7, 9, 7, direction);
/*      */       
/*  524 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/*  525 */         return null;
/*      */       }
/*      */       
/*  528 */       return new RoomCrossing(genDepth, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  534 */       generateBox(level, chunkBB, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  536 */       generateBox(level, chunkBB, 0, 2, 0, 6, 7, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  539 */       generateBox(level, chunkBB, 0, 2, 0, 1, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  540 */       generateBox(level, chunkBB, 0, 2, 6, 1, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  541 */       generateBox(level, chunkBB, 5, 2, 0, 6, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  542 */       generateBox(level, chunkBB, 5, 2, 6, 6, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  543 */       generateBox(level, chunkBB, 0, 2, 0, 0, 6, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  544 */       generateBox(level, chunkBB, 0, 2, 5, 0, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  545 */       generateBox(level, chunkBB, 6, 2, 0, 6, 6, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  546 */       generateBox(level, chunkBB, 6, 2, 5, 6, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  549 */       BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true));
/*  550 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */       
/*  552 */       generateBox(level, chunkBB, 2, 6, 0, 4, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  553 */       generateBox(level, chunkBB, 2, 5, 0, 4, 5, 0, weFence, weFence, false);
/*  554 */       generateBox(level, chunkBB, 2, 6, 6, 4, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  555 */       generateBox(level, chunkBB, 2, 5, 6, 4, 5, 6, weFence, weFence, false);
/*  556 */       generateBox(level, chunkBB, 0, 6, 2, 0, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  557 */       generateBox(level, chunkBB, 0, 5, 2, 0, 5, 4, nsFence, nsFence, false);
/*  558 */       generateBox(level, chunkBB, 6, 6, 2, 6, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  559 */       generateBox(level, chunkBB, 6, 5, 2, 6, 5, 4, nsFence, nsFence, false);
/*      */ 
/*      */       
/*  562 */       for (int x = 0; x <= 6; x++) {
/*  563 */         for (int z = 0; z <= 6; z++)
/*  564 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class StairsRoom
/*      */     extends NetherBridgePiece {
/*      */     private static final int WIDTH = 7;
/*      */     private static final int HEIGHT = 11;
/*      */     private static final int DEPTH = 7;
/*      */     
/*      */     public StairsRoom(int genDepth, BoundingBox boundingBox, Direction direction) {
/*  576 */       super(StructurePieceType.NETHER_FORTRESS_STAIRS_ROOM, genDepth, boundingBox);
/*      */       
/*  578 */       setOrientation(direction);
/*      */     }
/*      */ 
/*      */     
/*  582 */     public StairsRoom(CompoundTag tag) { super(StructurePieceType.NETHER_FORTRESS_STAIRS_ROOM, tag); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  587 */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) { generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 6, 2, false); }
/*      */ 
/*      */     
/*      */     public static StairsRoom createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, int genDepth, Direction direction) {
/*  591 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -2, 0, 0, 7, 11, 7, direction);
/*      */       
/*  593 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/*  594 */         return null;
/*      */       }
/*      */       
/*  597 */       return new StairsRoom(genDepth, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  603 */       generateBox(level, chunkBB, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  605 */       generateBox(level, chunkBB, 0, 2, 0, 6, 10, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  608 */       generateBox(level, chunkBB, 0, 2, 0, 1, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  609 */       generateBox(level, chunkBB, 5, 2, 0, 6, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  610 */       generateBox(level, chunkBB, 0, 2, 1, 0, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  611 */       generateBox(level, chunkBB, 6, 2, 1, 6, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  612 */       generateBox(level, chunkBB, 1, 2, 6, 5, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  615 */       BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true));
/*  616 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */       
/*  618 */       generateBox(level, chunkBB, 0, 3, 2, 0, 5, 4, nsFence, nsFence, false);
/*  619 */       generateBox(level, chunkBB, 6, 3, 2, 6, 5, 2, nsFence, nsFence, false);
/*  620 */       generateBox(level, chunkBB, 6, 3, 4, 6, 5, 4, nsFence, nsFence, false);
/*      */ 
/*      */       
/*  623 */       placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 5, 2, 5, chunkBB);
/*  624 */       generateBox(level, chunkBB, 4, 2, 5, 4, 3, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  625 */       generateBox(level, chunkBB, 3, 2, 5, 3, 4, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  626 */       generateBox(level, chunkBB, 2, 2, 5, 2, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  627 */       generateBox(level, chunkBB, 1, 2, 5, 1, 6, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  630 */       generateBox(level, chunkBB, 1, 7, 1, 5, 7, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  631 */       generateBox(level, chunkBB, 6, 8, 2, 6, 8, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  634 */       generateBox(level, chunkBB, 2, 6, 0, 4, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  635 */       generateBox(level, chunkBB, 2, 5, 0, 4, 5, 0, weFence, weFence, false);
/*      */       
/*  637 */       for (int x = 0; x <= 6; x++) {
/*  638 */         for (int z = 0; z <= 6; z++)
/*  639 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class MonsterThrone
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     private static final int WIDTH = 7;
/*      */     private static final int HEIGHT = 8;
/*      */     private static final int DEPTH = 9;
/*      */     private boolean hasPlacedSpawner;
/*      */     
/*      */     public MonsterThrone(int genDepth, BoundingBox boundingBox, Direction direction) {
/*  653 */       super(StructurePieceType.NETHER_FORTRESS_MONSTER_THRONE, genDepth, boundingBox);
/*      */       
/*  655 */       setOrientation(direction);
/*      */     }
/*      */     
/*      */     public MonsterThrone(CompoundTag tag) {
/*  659 */       super(StructurePieceType.NETHER_FORTRESS_MONSTER_THRONE, tag);
/*  660 */       this.hasPlacedSpawner = tag.getBooleanOr("Mob", false);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  665 */       super.addAdditionalSaveData(context, tag);
/*      */       
/*  667 */       tag.putBoolean("Mob", this.hasPlacedSpawner);
/*      */     }
/*      */     
/*      */     public static MonsterThrone createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, int genDepth, Direction direction) {
/*  671 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -2, 0, 0, 7, 8, 9, direction);
/*      */       
/*  673 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/*  674 */         return null;
/*      */       }
/*      */       
/*  677 */       return new MonsterThrone(genDepth, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  683 */       generateBox(level, chunkBB, 0, 2, 0, 6, 7, 7, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  686 */       generateBox(level, chunkBB, 1, 0, 0, 5, 1, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  687 */       generateBox(level, chunkBB, 1, 2, 1, 5, 2, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  688 */       generateBox(level, chunkBB, 1, 3, 2, 5, 3, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  689 */       generateBox(level, chunkBB, 1, 4, 3, 5, 4, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  692 */       generateBox(level, chunkBB, 1, 2, 0, 1, 4, 2, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  693 */       generateBox(level, chunkBB, 5, 2, 0, 5, 4, 2, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  694 */       generateBox(level, chunkBB, 1, 5, 2, 1, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  695 */       generateBox(level, chunkBB, 5, 5, 2, 5, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  696 */       generateBox(level, chunkBB, 0, 5, 3, 0, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  697 */       generateBox(level, chunkBB, 6, 5, 3, 6, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  698 */       generateBox(level, chunkBB, 1, 5, 8, 5, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  700 */       BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true));
/*  701 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */       
/*  703 */       placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true)), 1, 6, 3, chunkBB);
/*  704 */       placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.EAST, Boolean.valueOf(true)), 5, 6, 3, chunkBB);
/*      */       
/*  706 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.EAST, Boolean.valueOf(true))).setValue(FenceBlock.NORTH, Boolean.valueOf(true)), 0, 6, 3, chunkBB);
/*  707 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true))).setValue(FenceBlock.NORTH, Boolean.valueOf(true)), 6, 6, 3, chunkBB);
/*      */       
/*  709 */       generateBox(level, chunkBB, 0, 6, 4, 0, 6, 7, nsFence, nsFence, false);
/*  710 */       generateBox(level, chunkBB, 6, 6, 4, 6, 6, 7, nsFence, nsFence, false);
/*      */       
/*  712 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.EAST, Boolean.valueOf(true))).setValue(FenceBlock.SOUTH, Boolean.valueOf(true)), 0, 6, 8, chunkBB);
/*  713 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true))).setValue(FenceBlock.SOUTH, Boolean.valueOf(true)), 6, 6, 8, chunkBB);
/*      */       
/*  715 */       generateBox(level, chunkBB, 1, 6, 8, 5, 6, 8, weFence, weFence, false);
/*      */       
/*  717 */       placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.EAST, Boolean.valueOf(true)), 1, 7, 8, chunkBB);
/*  718 */       generateBox(level, chunkBB, 2, 7, 8, 4, 7, 8, weFence, weFence, false);
/*  719 */       placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true)), 5, 7, 8, chunkBB);
/*      */       
/*  721 */       placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.EAST, Boolean.valueOf(true)), 2, 8, 8, chunkBB);
/*  722 */       placeBlock(level, weFence, 3, 8, 8, chunkBB);
/*  723 */       placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true)), 4, 8, 8, chunkBB);
/*      */       
/*  725 */       if (!this.hasPlacedSpawner) {
/*  726 */         BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(3, 5, 5);
/*  727 */         if (chunkBB.isInside(mutableBlockPos)) {
/*  728 */           this.hasPlacedSpawner = true;
/*  729 */           level.setBlock(mutableBlockPos, Blocks.SPAWNER.defaultBlockState(), 2);
/*      */           
/*  731 */           BlockEntity blockEntity = level.getBlockEntity(mutableBlockPos);
/*  732 */           if (blockEntity instanceof SpawnerBlockEntity) { SpawnerBlockEntity spawner = (SpawnerBlockEntity)blockEntity;
/*  733 */             spawner.setEntityId(EntityType.BLAZE, random); }
/*      */         
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  739 */       for (int x = 0; x <= 6; x++) {
/*  740 */         for (int z = 0; z <= 6; z++)
/*  741 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleEntrance
/*      */     extends NetherBridgePiece {
/*      */     private static final int WIDTH = 13;
/*      */     private static final int HEIGHT = 14;
/*      */     private static final int DEPTH = 13;
/*      */     
/*      */     public CastleEntrance(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  753 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_ENTRANCE, genDepth, boundingBox);
/*      */       
/*  755 */       setOrientation(direction);
/*      */     }
/*      */ 
/*      */     
/*  759 */     public CastleEntrance(CompoundTag tag) { super(StructurePieceType.NETHER_FORTRESS_CASTLE_ENTRANCE, tag); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  764 */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) { generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 5, 3, true); }
/*      */ 
/*      */     
/*      */     public static CastleEntrance createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  768 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -5, -3, 0, 13, 14, 13, direction);
/*      */       
/*  770 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/*  771 */         return null;
/*      */       }
/*      */       
/*  774 */       return new CastleEntrance(genDepth, random, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  780 */       generateBox(level, chunkBB, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  782 */       generateBox(level, chunkBB, 0, 5, 0, 12, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  785 */       generateBox(level, chunkBB, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  786 */       generateBox(level, chunkBB, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  787 */       generateBox(level, chunkBB, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  788 */       generateBox(level, chunkBB, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  789 */       generateBox(level, chunkBB, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  790 */       generateBox(level, chunkBB, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  791 */       generateBox(level, chunkBB, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  792 */       generateBox(level, chunkBB, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  795 */       generateBox(level, chunkBB, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  798 */       generateBox(level, chunkBB, 5, 8, 0, 7, 8, 0, Blocks.NETHER_BRICK_FENCE.defaultBlockState(), Blocks.NETHER_BRICK_FENCE.defaultBlockState(), false);
/*      */       
/*  800 */       BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true));
/*  801 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */ 
/*      */       
/*  804 */       for (int i = 1; i <= 11; i += 2) {
/*  805 */         generateBox(level, chunkBB, i, 10, 0, i, 11, 0, weFence, weFence, false);
/*  806 */         generateBox(level, chunkBB, i, 10, 12, i, 11, 12, weFence, weFence, false);
/*  807 */         generateBox(level, chunkBB, 0, 10, i, 0, 11, i, nsFence, nsFence, false);
/*  808 */         generateBox(level, chunkBB, 12, 10, i, 12, 11, i, nsFence, nsFence, false);
/*  809 */         placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 0, chunkBB);
/*  810 */         placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 12, chunkBB);
/*  811 */         placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 0, 13, i, chunkBB);
/*  812 */         placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 12, 13, i, chunkBB);
/*  813 */         if (i != 11) {
/*  814 */           placeBlock(level, weFence, i + 1, 13, 0, chunkBB);
/*  815 */           placeBlock(level, weFence, i + 1, 13, 12, chunkBB);
/*  816 */           placeBlock(level, nsFence, 0, 13, i + 1, chunkBB);
/*  817 */           placeBlock(level, nsFence, 12, 13, i + 1, chunkBB);
/*      */         } 
/*      */       } 
/*  820 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 0, chunkBB);
/*  821 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.SOUTH, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 12, chunkBB);
/*  822 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.SOUTH, Boolean.valueOf(true))).setValue(FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 12, chunkBB);
/*  823 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 0, chunkBB);
/*      */ 
/*      */       
/*  826 */       for (int z = 3; z <= 9; z += 2) {
/*  827 */         generateBox(level, chunkBB, 1, 7, z, 1, 8, z, (BlockState)nsFence.setValue(FenceBlock.WEST, Boolean.valueOf(true)), (BlockState)nsFence.setValue(FenceBlock.WEST, Boolean.valueOf(true)), false);
/*  828 */         generateBox(level, chunkBB, 11, 7, z, 11, 8, z, (BlockState)nsFence.setValue(FenceBlock.EAST, Boolean.valueOf(true)), (BlockState)nsFence.setValue(FenceBlock.EAST, Boolean.valueOf(true)), false);
/*      */       } 
/*      */ 
/*      */       
/*  832 */       generateBox(level, chunkBB, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  833 */       generateBox(level, chunkBB, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  835 */       generateBox(level, chunkBB, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  836 */       generateBox(level, chunkBB, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  837 */       generateBox(level, chunkBB, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  838 */       generateBox(level, chunkBB, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  840 */       for (int x = 4; x <= 8; x++) {
/*  841 */         for (int z = 0; z <= 2; z++) {
/*  842 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
/*  843 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, 12 - z, chunkBB);
/*      */         } 
/*      */       } 
/*  846 */       for (int x = 0; x <= 2; x++) {
/*  847 */         for (int z = 4; z <= 8; z++) {
/*  848 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
/*  849 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), 12 - x, -1, z, chunkBB);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  854 */       generateBox(level, chunkBB, 5, 5, 5, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  855 */       generateBox(level, chunkBB, 6, 1, 6, 6, 4, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  856 */       placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 6, 0, 6, chunkBB);
/*  857 */       placeBlock(level, Blocks.LAVA.defaultBlockState(), 6, 5, 6, chunkBB);
/*      */       
/*  859 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(6, 5, 6);
/*  860 */       if (chunkBB.isInside(mutableBlockPos))
/*  861 */         level.scheduleTick(mutableBlockPos, Fluids.LAVA, 0); 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleStalkRoom
/*      */     extends NetherBridgePiece {
/*      */     private static final int WIDTH = 13;
/*      */     private static final int HEIGHT = 14;
/*      */     private static final int DEPTH = 13;
/*      */     
/*      */     public CastleStalkRoom(int genDepth, BoundingBox boundingBox, Direction direction) {
/*  872 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, genDepth, boundingBox);
/*      */       
/*  874 */       setOrientation(direction);
/*      */     }
/*      */ 
/*      */     
/*  878 */     public CastleStalkRoom(CompoundTag tag) { super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  883 */       generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 5, 3, true);
/*  884 */       generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 5, 11, true);
/*      */     }
/*      */     
/*      */     public static CastleStalkRoom createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  888 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -5, -3, 0, 13, 14, 13, direction);
/*      */       
/*  890 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/*  891 */         return null;
/*      */       }
/*      */       
/*  894 */       return new CastleStalkRoom(genDepth, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  900 */       generateBox(level, chunkBB, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  902 */       generateBox(level, chunkBB, 0, 5, 0, 12, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  905 */       generateBox(level, chunkBB, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  906 */       generateBox(level, chunkBB, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  907 */       generateBox(level, chunkBB, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  908 */       generateBox(level, chunkBB, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  909 */       generateBox(level, chunkBB, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  910 */       generateBox(level, chunkBB, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  911 */       generateBox(level, chunkBB, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  912 */       generateBox(level, chunkBB, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/*  915 */       generateBox(level, chunkBB, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/*  917 */       BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true));
/*  918 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.SOUTH, Boolean.valueOf(true));
/*  919 */       BlockState nswFence = (BlockState)nsFence.setValue(FenceBlock.WEST, Boolean.valueOf(true));
/*  920 */       BlockState nseFence = (BlockState)nsFence.setValue(FenceBlock.EAST, Boolean.valueOf(true));
/*      */ 
/*      */       
/*  923 */       for (int i = 1; i <= 11; i += 2) {
/*  924 */         generateBox(level, chunkBB, i, 10, 0, i, 11, 0, weFence, weFence, false);
/*  925 */         generateBox(level, chunkBB, i, 10, 12, i, 11, 12, weFence, weFence, false);
/*  926 */         generateBox(level, chunkBB, 0, 10, i, 0, 11, i, nsFence, nsFence, false);
/*  927 */         generateBox(level, chunkBB, 12, 10, i, 12, 11, i, nsFence, nsFence, false);
/*  928 */         placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 0, chunkBB);
/*  929 */         placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 12, chunkBB);
/*  930 */         placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 0, 13, i, chunkBB);
/*  931 */         placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 12, 13, i, chunkBB);
/*  932 */         if (i != 11) {
/*  933 */           placeBlock(level, weFence, i + 1, 13, 0, chunkBB);
/*  934 */           placeBlock(level, weFence, i + 1, 13, 12, chunkBB);
/*  935 */           placeBlock(level, nsFence, 0, 13, i + 1, chunkBB);
/*  936 */           placeBlock(level, nsFence, 12, 13, i + 1, chunkBB);
/*      */         } 
/*      */       } 
/*  939 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 0, chunkBB);
/*  940 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.SOUTH, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 12, chunkBB);
/*  941 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.SOUTH, Boolean.valueOf(true))).setValue(FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 12, chunkBB);
/*  942 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 0, chunkBB);
/*      */ 
/*      */       
/*  945 */       for (int z = 3; z <= 9; z += 2) {
/*  946 */         generateBox(level, chunkBB, 1, 7, z, 1, 8, z, nswFence, nswFence, false);
/*  947 */         generateBox(level, chunkBB, 11, 7, z, 11, 8, z, nseFence, nseFence, false);
/*      */       } 
/*      */ 
/*      */       
/*  951 */       BlockState stairs = (BlockState)Blocks.NETHER_BRICK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH);
/*  952 */       for (int i = 0; i <= 6; i++) {
/*  953 */         int z = i + 4;
/*  954 */         for (int x = 5; x <= 7; x++) {
/*  955 */           placeBlock(level, stairs, x, 5 + i, z, chunkBB);
/*      */         }
/*  957 */         if (z >= 5 && z <= 8) {
/*  958 */           generateBox(level, chunkBB, 5, 5, z, 7, i + 4, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  959 */         } else if (z >= 9 && z <= 10) {
/*  960 */           generateBox(level, chunkBB, 5, 8, z, 7, i + 4, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         } 
/*  962 */         if (i >= 1) {
/*  963 */           generateBox(level, chunkBB, 5, 6 + i, z, 7, 9 + i, z, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */         }
/*      */       } 
/*  966 */       for (int x = 5; x <= 7; x++) {
/*  967 */         placeBlock(level, stairs, x, 12, 11, chunkBB);
/*      */       }
/*  969 */       generateBox(level, chunkBB, 5, 6, 7, 5, 7, 7, nseFence, nseFence, false);
/*  970 */       generateBox(level, chunkBB, 7, 6, 7, 7, 7, 7, nswFence, nswFence, false);
/*  971 */       generateBox(level, chunkBB, 5, 13, 12, 7, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/*  974 */       generateBox(level, chunkBB, 2, 5, 2, 3, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  975 */       generateBox(level, chunkBB, 2, 5, 9, 3, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  976 */       generateBox(level, chunkBB, 2, 5, 4, 2, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  977 */       generateBox(level, chunkBB, 9, 5, 2, 10, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  978 */       generateBox(level, chunkBB, 9, 5, 9, 10, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  979 */       generateBox(level, chunkBB, 10, 5, 4, 10, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  980 */       BlockState eastStairs = (BlockState)stairs.setValue(StairBlock.FACING, Direction.EAST);
/*  981 */       BlockState westStairs = (BlockState)stairs.setValue(StairBlock.FACING, Direction.WEST);
/*  982 */       placeBlock(level, westStairs, 4, 5, 2, chunkBB);
/*  983 */       placeBlock(level, westStairs, 4, 5, 3, chunkBB);
/*  984 */       placeBlock(level, westStairs, 4, 5, 9, chunkBB);
/*  985 */       placeBlock(level, westStairs, 4, 5, 10, chunkBB);
/*  986 */       placeBlock(level, eastStairs, 8, 5, 2, chunkBB);
/*  987 */       placeBlock(level, eastStairs, 8, 5, 3, chunkBB);
/*  988 */       placeBlock(level, eastStairs, 8, 5, 9, chunkBB);
/*  989 */       placeBlock(level, eastStairs, 8, 5, 10, chunkBB);
/*      */ 
/*      */       
/*  992 */       generateBox(level, chunkBB, 3, 4, 4, 4, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
/*  993 */       generateBox(level, chunkBB, 8, 4, 4, 9, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
/*  994 */       generateBox(level, chunkBB, 3, 5, 4, 4, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
/*  995 */       generateBox(level, chunkBB, 8, 5, 4, 9, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
/*      */ 
/*      */       
/*  998 */       generateBox(level, chunkBB, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  999 */       generateBox(level, chunkBB, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1001 */       generateBox(level, chunkBB, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1002 */       generateBox(level, chunkBB, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1003 */       generateBox(level, chunkBB, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1004 */       generateBox(level, chunkBB, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1006 */       for (int x = 4; x <= 8; x++) {
/* 1007 */         for (int z = 0; z <= 2; z++) {
/* 1008 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
/* 1009 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, 12 - z, chunkBB);
/*      */         } 
/*      */       } 
/* 1012 */       for (int x = 0; x <= 2; x++) {
/* 1013 */         for (int z = 4; z <= 8; z++) {
/* 1014 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
/* 1015 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), 12 - x, -1, z, chunkBB);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleSmallCorridorPiece extends NetherBridgePiece {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 7;
/*      */     private static final int DEPTH = 5;
/*      */     
/*      */     public CastleSmallCorridorPiece(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 1027 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR, genDepth, boundingBox);
/*      */       
/* 1029 */       setOrientation(direction);
/*      */     }
/*      */ 
/*      */     
/* 1033 */     public CastleSmallCorridorPiece(CompoundTag tag) { super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR, tag); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1038 */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) { generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 0, true); }
/*      */ 
/*      */     
/*      */     public static CastleSmallCorridorPiece createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1042 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, 0, 0, 5, 7, 5, direction);
/*      */       
/* 1044 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/* 1045 */         return null;
/*      */       }
/*      */       
/* 1048 */       return new CastleSmallCorridorPiece(genDepth, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1054 */       generateBox(level, chunkBB, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1056 */       generateBox(level, chunkBB, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */       
/* 1058 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */ 
/*      */       
/* 1061 */       generateBox(level, chunkBB, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1062 */       generateBox(level, chunkBB, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1063 */       generateBox(level, chunkBB, 0, 3, 1, 0, 4, 1, nsFence, nsFence, false);
/* 1064 */       generateBox(level, chunkBB, 0, 3, 3, 0, 4, 3, nsFence, nsFence, false);
/* 1065 */       generateBox(level, chunkBB, 4, 3, 1, 4, 4, 1, nsFence, nsFence, false);
/* 1066 */       generateBox(level, chunkBB, 4, 3, 3, 4, 4, 3, nsFence, nsFence, false);
/*      */ 
/*      */       
/* 1069 */       generateBox(level, chunkBB, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1072 */       for (int x = 0; x <= 4; x++) {
/* 1073 */         for (int z = 0; z <= 4; z++)
/* 1074 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleSmallCorridorCrossingPiece
/*      */     extends NetherBridgePiece {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 7;
/*      */     private static final int DEPTH = 5;
/*      */     
/*      */     public CastleSmallCorridorCrossingPiece(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 1086 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_CROSSING, genDepth, boundingBox);
/*      */       
/* 1088 */       setOrientation(direction);
/*      */     }
/*      */ 
/*      */     
/* 1092 */     public CastleSmallCorridorCrossingPiece(CompoundTag tag) { super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_CROSSING, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1097 */       generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 0, true);
/* 1098 */       generateChildLeft((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 1, true);
/* 1099 */       generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 1, true);
/*      */     }
/*      */     
/*      */     public static CastleSmallCorridorCrossingPiece createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1103 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, 0, 0, 5, 7, 5, direction);
/*      */       
/* 1105 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/* 1106 */         return null;
/*      */       }
/*      */       
/* 1109 */       return new CastleSmallCorridorCrossingPiece(genDepth, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1115 */       generateBox(level, chunkBB, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1117 */       generateBox(level, chunkBB, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1120 */       generateBox(level, chunkBB, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1121 */       generateBox(level, chunkBB, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1122 */       generateBox(level, chunkBB, 0, 2, 4, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1123 */       generateBox(level, chunkBB, 4, 2, 4, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1126 */       generateBox(level, chunkBB, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1129 */       for (int x = 0; x <= 4; x++) {
/* 1130 */         for (int z = 0; z <= 4; z++)
/* 1131 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleSmallCorridorRightTurnPiece
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 7;
/*      */     private static final int DEPTH = 5;
/*      */     private boolean isNeedingChest;
/*      */     
/*      */     public CastleSmallCorridorRightTurnPiece(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 1145 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_RIGHT_TURN, genDepth, boundingBox);
/*      */       
/* 1147 */       setOrientation(direction);
/*      */       
/* 1149 */       this.isNeedingChest = (random.nextInt(3) == 0);
/*      */     }
/*      */     
/*      */     public CastleSmallCorridorRightTurnPiece(CompoundTag tag) {
/* 1153 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_RIGHT_TURN, tag);
/* 1154 */       this.isNeedingChest = tag.getBooleanOr("Chest", false);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 1159 */       super.addAdditionalSaveData(context, tag);
/*      */       
/* 1161 */       tag.putBoolean("Chest", this.isNeedingChest);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1166 */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) { generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 1, true); }
/*      */ 
/*      */     
/*      */     public static CastleSmallCorridorRightTurnPiece createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1170 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, 0, 0, 5, 7, 5, direction);
/*      */       
/* 1172 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/* 1173 */         return null;
/*      */       }
/*      */       
/* 1176 */       return new CastleSmallCorridorRightTurnPiece(genDepth, random, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1182 */       generateBox(level, chunkBB, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1184 */       generateBox(level, chunkBB, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */       
/* 1186 */       BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true));
/* 1187 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */ 
/*      */       
/* 1190 */       generateBox(level, chunkBB, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1191 */       generateBox(level, chunkBB, 0, 3, 1, 0, 4, 1, nsFence, nsFence, false);
/* 1192 */       generateBox(level, chunkBB, 0, 3, 3, 0, 4, 3, nsFence, nsFence, false);
/*      */       
/* 1194 */       generateBox(level, chunkBB, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1196 */       generateBox(level, chunkBB, 1, 2, 4, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1197 */       generateBox(level, chunkBB, 1, 3, 4, 1, 4, 4, weFence, weFence, false);
/* 1198 */       generateBox(level, chunkBB, 3, 3, 4, 3, 4, 4, weFence, weFence, false);
/*      */       
/* 1200 */       if (this.isNeedingChest && 
/* 1201 */         chunkBB.isInside(getWorldPos(1, 2, 3))) {
/* 1202 */         this.isNeedingChest = false;
/* 1203 */         createChest(level, chunkBB, random, 1, 2, 3, BuiltInLootTables.NETHER_BRIDGE);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1208 */       generateBox(level, chunkBB, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1211 */       for (int x = 0; x <= 4; x++) {
/* 1212 */         for (int z = 0; z <= 4; z++)
/* 1213 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleSmallCorridorLeftTurnPiece
/*      */     extends NetherBridgePiece
/*      */   {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 7;
/*      */     private static final int DEPTH = 5;
/*      */     private boolean isNeedingChest;
/*      */     
/*      */     public CastleSmallCorridorLeftTurnPiece(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 1227 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_LEFT_TURN, genDepth, boundingBox);
/*      */       
/* 1229 */       setOrientation(direction);
/*      */       
/* 1231 */       this.isNeedingChest = (random.nextInt(3) == 0);
/*      */     }
/*      */     
/*      */     public CastleSmallCorridorLeftTurnPiece(CompoundTag tag) {
/* 1235 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_LEFT_TURN, tag);
/* 1236 */       this.isNeedingChest = tag.getBooleanOr("Chest", false);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 1241 */       super.addAdditionalSaveData(context, tag);
/*      */       
/* 1243 */       tag.putBoolean("Chest", this.isNeedingChest);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1248 */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) { generateChildLeft((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 1, true); }
/*      */ 
/*      */     
/*      */     public static CastleSmallCorridorLeftTurnPiece createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1252 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, 0, 0, 5, 7, 5, direction);
/*      */       
/* 1254 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/* 1255 */         return null;
/*      */       }
/*      */       
/* 1258 */       return new CastleSmallCorridorLeftTurnPiece(genDepth, random, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1264 */       generateBox(level, chunkBB, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1266 */       generateBox(level, chunkBB, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */       
/* 1268 */       BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true));
/* 1269 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */ 
/*      */       
/* 1272 */       generateBox(level, chunkBB, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1273 */       generateBox(level, chunkBB, 4, 3, 1, 4, 4, 1, nsFence, nsFence, false);
/* 1274 */       generateBox(level, chunkBB, 4, 3, 3, 4, 4, 3, nsFence, nsFence, false);
/*      */       
/* 1276 */       generateBox(level, chunkBB, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1278 */       generateBox(level, chunkBB, 0, 2, 4, 3, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1279 */       generateBox(level, chunkBB, 1, 3, 4, 1, 4, 4, weFence, weFence, false);
/* 1280 */       generateBox(level, chunkBB, 3, 3, 4, 3, 4, 4, weFence, weFence, false);
/*      */       
/* 1282 */       if (this.isNeedingChest && 
/* 1283 */         chunkBB.isInside(getWorldPos(3, 2, 3))) {
/* 1284 */         this.isNeedingChest = false;
/* 1285 */         createChest(level, chunkBB, random, 3, 2, 3, BuiltInLootTables.NETHER_BRIDGE);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1290 */       generateBox(level, chunkBB, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1293 */       for (int x = 0; x <= 4; x++) {
/* 1294 */         for (int z = 0; z <= 4; z++)
/* 1295 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleCorridorStairsPiece
/*      */     extends NetherBridgePiece {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 14;
/*      */     private static final int DEPTH = 10;
/*      */     
/*      */     public CastleCorridorStairsPiece(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 1307 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS, genDepth, boundingBox);
/*      */       
/* 1309 */       setOrientation(direction);
/*      */     }
/*      */ 
/*      */     
/* 1313 */     public CastleCorridorStairsPiece(CompoundTag tag) { super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS, tag); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1318 */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) { generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 0, true); }
/*      */ 
/*      */     
/*      */     public static CastleCorridorStairsPiece createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1322 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -7, 0, 5, 14, 10, direction);
/*      */       
/* 1324 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/* 1325 */         return null;
/*      */       }
/*      */       
/* 1328 */       return new CastleCorridorStairsPiece(genDepth, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1334 */       BlockState stairs = (BlockState)Blocks.NETHER_BRICK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.SOUTH);
/* 1335 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */       
/* 1337 */       for (int step = 0; step <= 9; step++) {
/* 1338 */         int floor = Math.max(1, 7 - step);
/* 1339 */         int roof = Math.min(Math.max(floor + 5, 14 - step), 13);
/* 1340 */         int z = step;
/*      */ 
/*      */         
/* 1343 */         generateBox(level, chunkBB, 0, 0, z, 4, floor, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         
/* 1345 */         generateBox(level, chunkBB, 1, floor + 1, z, 3, roof - 1, z, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1346 */         if (step <= 6) {
/* 1347 */           placeBlock(level, stairs, 1, floor + 1, z, chunkBB);
/* 1348 */           placeBlock(level, stairs, 2, floor + 1, z, chunkBB);
/* 1349 */           placeBlock(level, stairs, 3, floor + 1, z, chunkBB);
/*      */         } 
/*      */         
/* 1352 */         generateBox(level, chunkBB, 0, roof, z, 4, roof, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         
/* 1354 */         generateBox(level, chunkBB, 0, floor + 1, z, 0, roof - 1, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1355 */         generateBox(level, chunkBB, 4, floor + 1, z, 4, roof - 1, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1356 */         if ((step & true) == 0) {
/* 1357 */           generateBox(level, chunkBB, 0, floor + 2, z, 0, floor + 3, z, nsFence, nsFence, false);
/* 1358 */           generateBox(level, chunkBB, 4, floor + 2, z, 4, floor + 3, z, nsFence, nsFence, false);
/*      */         } 
/*      */ 
/*      */         
/* 1362 */         for (int x = 0; x <= 4; x++)
/* 1363 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleCorridorTBalconyPiece
/*      */     extends NetherBridgePiece {
/*      */     private static final int WIDTH = 9;
/*      */     private static final int HEIGHT = 7;
/*      */     private static final int DEPTH = 9;
/*      */     
/*      */     public CastleCorridorTBalconyPiece(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 1375 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY, genDepth, boundingBox);
/*      */       
/* 1377 */       setOrientation(direction);
/*      */     }
/*      */ 
/*      */     
/* 1381 */     public CastleCorridorTBalconyPiece(CompoundTag tag) { super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1386 */       int zOff = 1;
/*      */       
/* 1388 */       Direction orientation = getOrientation();
/* 1389 */       if (orientation == Direction.WEST || orientation == Direction.NORTH) {
/* 1390 */         zOff = 5;
/*      */       }
/*      */       
/* 1393 */       generateChildLeft((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, zOff, (random.nextInt(8) > 0));
/* 1394 */       generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, zOff, (random.nextInt(8) > 0));
/*      */     }
/*      */     
/*      */     public static CastleCorridorTBalconyPiece createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1398 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -3, 0, 0, 9, 7, 9, direction);
/*      */       
/* 1400 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/* 1401 */         return null;
/*      */       }
/*      */       
/* 1404 */       return new CastleCorridorTBalconyPiece(genDepth, box, direction);
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1409 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.SOUTH, Boolean.valueOf(true));
/* 1410 */       BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true));
/*      */ 
/*      */       
/* 1413 */       generateBox(level, chunkBB, 0, 0, 0, 8, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       
/* 1415 */       generateBox(level, chunkBB, 0, 2, 0, 8, 5, 8, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */       
/* 1417 */       generateBox(level, chunkBB, 0, 6, 0, 8, 6, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1420 */       generateBox(level, chunkBB, 0, 2, 0, 2, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1421 */       generateBox(level, chunkBB, 6, 2, 0, 8, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1422 */       generateBox(level, chunkBB, 1, 3, 0, 1, 4, 0, weFence, weFence, false);
/* 1423 */       generateBox(level, chunkBB, 7, 3, 0, 7, 4, 0, weFence, weFence, false);
/*      */ 
/*      */       
/* 1426 */       generateBox(level, chunkBB, 0, 2, 4, 8, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1427 */       generateBox(level, chunkBB, 1, 1, 4, 2, 2, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1428 */       generateBox(level, chunkBB, 6, 1, 4, 7, 2, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1431 */       generateBox(level, chunkBB, 1, 3, 8, 7, 3, 8, weFence, weFence, false);
/* 1432 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.EAST, Boolean.valueOf(true))).setValue(FenceBlock.SOUTH, Boolean.valueOf(true)), 0, 3, 8, chunkBB);
/* 1433 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true))).setValue(FenceBlock.SOUTH, Boolean.valueOf(true)), 8, 3, 8, chunkBB);
/* 1434 */       generateBox(level, chunkBB, 0, 3, 6, 0, 3, 7, nsFence, nsFence, false);
/* 1435 */       generateBox(level, chunkBB, 8, 3, 6, 8, 3, 7, nsFence, nsFence, false);
/*      */ 
/*      */       
/* 1438 */       generateBox(level, chunkBB, 0, 3, 4, 0, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1439 */       generateBox(level, chunkBB, 8, 3, 4, 8, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1440 */       generateBox(level, chunkBB, 1, 3, 5, 2, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1441 */       generateBox(level, chunkBB, 6, 3, 5, 7, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1442 */       generateBox(level, chunkBB, 1, 4, 5, 1, 5, 5, weFence, weFence, false);
/* 1443 */       generateBox(level, chunkBB, 7, 4, 5, 7, 5, 5, weFence, weFence, false);
/*      */ 
/*      */       
/* 1446 */       for (int z = 0; z <= 5; z++) {
/* 1447 */         for (int x = 0; x <= 8; x++)
/* 1448 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */