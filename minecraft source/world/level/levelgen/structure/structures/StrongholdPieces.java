/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.serialization.Codec;
/*      */ import java.util.List;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.util.ExtraCodecs;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.StructureManager;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.ButtonBlock;
/*      */ import net.minecraft.world.level.block.DoorBlock;
/*      */ import net.minecraft.world.level.block.EndPortalFrameBlock;
/*      */ import net.minecraft.world.level.block.FenceBlock;
/*      */ import net.minecraft.world.level.block.IronBarsBlock;
/*      */ import net.minecraft.world.level.block.LadderBlock;
/*      */ import net.minecraft.world.level.block.SlabBlock;
/*      */ import net.minecraft.world.level.block.StairBlock;
/*      */ import net.minecraft.world.level.block.WallTorchBlock;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*      */ import net.minecraft.world.level.block.state.properties.SlabType;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class StrongholdPieces
/*      */ {
/*      */   private static final int SMALL_DOOR_WIDTH = 3;
/*      */   private static final int SMALL_DOOR_HEIGHT = 3;
/*      */   private static final int MAX_DEPTH = 50;
/*      */   private static final int LOWEST_Y_POSITION = 10;
/*      */   private static final boolean CHECK_AIR = true;
/*      */   public static final int MAGIC_START_Y = 64;
/*      */   
/*      */   private static class PieceWeight
/*      */   {
/*      */     public final Class<? extends StrongholdPieces.StrongholdPiece> pieceClass;
/*      */     public final int weight;
/*      */     public int placeCount;
/*      */     public final int maxPlaceCount;
/*      */     
/*      */     public PieceWeight(Class<? extends StrongholdPieces.StrongholdPiece> pieceClass, int weight, int maxPlaceCount) {
/*   62 */       this.pieceClass = pieceClass;
/*   63 */       this.weight = weight;
/*   64 */       this.maxPlaceCount = maxPlaceCount;
/*      */     }
/*      */ 
/*      */     
/*   68 */     public boolean doPlace(int depth) { return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount); }
/*      */ 
/*      */ 
/*      */     
/*   72 */     public boolean isValid() { return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount); }
/*      */   }
/*      */   
/*      */   private static final PieceWeight[] STRONGHOLD_PIECE_WEIGHTS = { 
/*   76 */       new PieceWeight(Straight.class, 40, 0), new PieceWeight(PrisonHall.class, 5, 5), new PieceWeight(LeftTurn.class, 20, 0), new PieceWeight(RightTurn.class, 20, 0), new PieceWeight(RoomCrossing.class, 10, 6), new PieceWeight(StraightStairsDown.class, 5, 5), new PieceWeight(StairsDown.class, 5, 5), new PieceWeight(FiveCrossing.class, 5, 4), new PieceWeight(ChestCorridor.class, 5, 4), new PieceWeight(Library.class, 10, 2)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public boolean doPlace(int depth)
/*      */         {
/*   89 */           return (super.doPlace(depth) && depth > 4);
/*      */         }
/*      */       }, 
/*      */       new PieceWeight(PortalRoom.class, 20, 1)
/*      */       {
/*      */         public boolean doPlace(int depth) {
/*   95 */           return (super.doPlace(depth) && depth > 5);
/*      */         }
/*      */       } };
/*      */   
/*      */   private static List<PieceWeight> currentPieces;
/*      */   private static Class<? extends StrongholdPiece> imposedPiece;
/*      */   private static int totalWeight;
/*      */   
/*      */   public static void resetPieces() {
/*  104 */     currentPieces = Lists.newArrayList();
/*  105 */     for (PieceWeight piece : STRONGHOLD_PIECE_WEIGHTS) {
/*  106 */       piece.placeCount = 0;
/*  107 */       currentPieces.add(piece);
/*      */     } 
/*  109 */     imposedPiece = null;
/*      */   }
/*      */   
/*      */   private static boolean updatePieceWeight() {
/*  113 */     hasAnyPieces = false;
/*  114 */     totalWeight = 0;
/*  115 */     for (PieceWeight piece : currentPieces) {
/*  116 */       if (piece.maxPlaceCount > 0 && piece.placeCount < piece.maxPlaceCount) {
/*  117 */         hasAnyPieces = true;
/*      */       }
/*  119 */       totalWeight += piece.weight;
/*      */     } 
/*  121 */     return hasAnyPieces;
/*      */   }
/*      */   
/*      */   private static StrongholdPiece findAndCreatePieceFactory(Class<? extends StrongholdPiece> pieceClass, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth) {
/*  125 */     StrongholdPiece strongholdPiece = null;
/*      */     
/*  127 */     if (pieceClass == Straight.class) {
/*  128 */       strongholdPiece = Straight.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  129 */     } else if (pieceClass == PrisonHall.class) {
/*  130 */       strongholdPiece = PrisonHall.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  131 */     } else if (pieceClass == LeftTurn.class) {
/*  132 */       strongholdPiece = LeftTurn.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  133 */     } else if (pieceClass == RightTurn.class) {
/*  134 */       strongholdPiece = RightTurn.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  135 */     } else if (pieceClass == RoomCrossing.class) {
/*  136 */       strongholdPiece = RoomCrossing.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  137 */     } else if (pieceClass == StraightStairsDown.class) {
/*  138 */       strongholdPiece = StraightStairsDown.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  139 */     } else if (pieceClass == StairsDown.class) {
/*  140 */       strongholdPiece = StairsDown.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  141 */     } else if (pieceClass == FiveCrossing.class) {
/*  142 */       strongholdPiece = FiveCrossing.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  143 */     } else if (pieceClass == ChestCorridor.class) {
/*  144 */       strongholdPiece = ChestCorridor.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  145 */     } else if (pieceClass == Library.class) {
/*  146 */       strongholdPiece = Library.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  147 */     } else if (pieceClass == PortalRoom.class) {
/*  148 */       strongholdPiece = PortalRoom.createPiece(structurePieceAccessor, footX, footY, footZ, direction, depth);
/*      */     } 
/*      */     
/*  151 */     return strongholdPiece;
/*      */   }
/*      */   
/*      */   private static StrongholdPiece generatePieceFromSmallDoor(StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth) {
/*  155 */     if (!updatePieceWeight()) {
/*  156 */       return null;
/*      */     }
/*      */     
/*  159 */     if (imposedPiece != null) {
/*  160 */       StrongholdPiece strongholdPiece = findAndCreatePieceFactory(imposedPiece, structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  161 */       imposedPiece = null;
/*      */       
/*  163 */       if (strongholdPiece != null) {
/*  164 */         return strongholdPiece;
/*      */       }
/*      */     } 
/*      */     
/*  168 */     int numAttempts = 0;
/*  169 */     while (numAttempts < 5) {
/*  170 */       numAttempts++;
/*      */       
/*  172 */       int weightSelection = random.nextInt(totalWeight);
/*  173 */       for (PieceWeight piece : currentPieces) {
/*  174 */         weightSelection -= piece.weight;
/*  175 */         if (weightSelection < 0) {
/*  176 */           if (!piece.doPlace(depth) || piece == startPiece.previousPiece) {
/*      */             break;
/*      */           }
/*      */           
/*  180 */           StrongholdPiece strongholdPiece = findAndCreatePieceFactory(piece.pieceClass, structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  181 */           if (strongholdPiece != null) {
/*  182 */             piece.placeCount++;
/*  183 */             startPiece.previousPiece = piece;
/*      */             
/*  185 */             if (!piece.isValid()) {
/*  186 */               currentPieces.remove(piece);
/*      */             }
/*  188 */             return strongholdPiece;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  193 */     BoundingBox box = FillerCorridor.findPieceBox(structurePieceAccessor, random, footX, footY, footZ, direction);
/*  194 */     if (box != null && box.minY() > 1) {
/*  195 */       return new FillerCorridor(depth, box, direction);
/*      */     }
/*      */     
/*  198 */     return null;
/*      */   }
/*      */   
/*      */   private static StructurePiece generateAndAddPiece(StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth) {
/*  202 */     if (depth > 50) {
/*  203 */       return null;
/*      */     }
/*  205 */     if (Math.abs(footX - startPiece.getBoundingBox().minX()) > 112 || Math.abs(footZ - startPiece.getBoundingBox().minZ()) > 112) {
/*  206 */       return null;
/*      */     }
/*      */     
/*  209 */     StructurePiece newPiece = generatePieceFromSmallDoor(startPiece, structurePieceAccessor, random, footX, footY, footZ, direction, depth + 1);
/*  210 */     if (newPiece != null) {
/*  211 */       structurePieceAccessor.addPiece(newPiece);
/*  212 */       startPiece.pendingChildren.add(newPiece);
/*      */     } 
/*  214 */     return newPiece;
/*      */   }
/*      */   
/*      */   private static abstract class StrongholdPiece extends StructurePiece {
/*  218 */     protected SmallDoorType entryDoor = SmallDoorType.OPENING;
/*      */ 
/*      */     
/*  221 */     protected StrongholdPiece(StructurePieceType type, int genDepth, BoundingBox boundingBox) { super(type, genDepth, boundingBox); }
/*      */ 
/*      */     
/*      */     public StrongholdPiece(StructurePieceType type, CompoundTag tag) {
/*  225 */       super(type, tag);
/*  226 */       this.entryDoor = (SmallDoorType)tag.read("EntryDoor", SmallDoorType.LEGACY_CODEC).orElseThrow();
/*      */     }
/*      */     
/*      */     protected enum SmallDoorType {
/*  230 */       OPENING, WOOD_DOOR, GRATES, IRON_DOOR; @Deprecated
/*      */       public static final Codec<SmallDoorType> LEGACY_CODEC;
/*      */       
/*      */       static  {
/*  234 */         LEGACY_CODEC = ExtraCodecs.legacyEnum(SmallDoorType::valueOf);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*  239 */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) { tag.store("EntryDoor", SmallDoorType.LEGACY_CODEC, this.entryDoor); }
/*      */ 
/*      */     
/*      */     protected void generateSmallDoor(WorldGenLevel level, RandomSource random, BoundingBox chunkBB, SmallDoorType doorType, int footX, int footY, int footZ) {
/*  243 */       switch (doorType.ordinal()) {
/*      */         case 0:
/*  245 */           generateBox(level, chunkBB, footX, footY, footZ, footX + 3 - 1, footY + 3 - 1, footZ, CAVE_AIR, CAVE_AIR, false);
/*      */           break;
/*      */         case 1:
/*  248 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY, footZ, chunkBB);
/*  249 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 1, footZ, chunkBB);
/*  250 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 2, footZ, chunkBB);
/*  251 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 1, footY + 2, footZ, chunkBB);
/*  252 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 2, footZ, chunkBB);
/*  253 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 1, footZ, chunkBB);
/*  254 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY, footZ, chunkBB);
/*  255 */           placeBlock(level, Blocks.OAK_DOOR.defaultBlockState(), footX + 1, footY, footZ, chunkBB);
/*  256 */           placeBlock(level, (BlockState)Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), footX + 1, footY + 1, footZ, chunkBB);
/*      */           break;
/*      */         case 2:
/*  259 */           placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), footX + 1, footY, footZ, chunkBB);
/*  260 */           placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), footX + 1, footY + 1, footZ, chunkBB);
/*  261 */           placeBlock(level, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.WEST, Boolean.valueOf(true)), footX, footY, footZ, chunkBB);
/*  262 */           placeBlock(level, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.WEST, Boolean.valueOf(true)), footX, footY + 1, footZ, chunkBB);
/*  263 */           placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.EAST, Boolean.valueOf(true))).setValue(IronBarsBlock.WEST, Boolean.valueOf(true)), footX, footY + 2, footZ, chunkBB);
/*  264 */           placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.EAST, Boolean.valueOf(true))).setValue(IronBarsBlock.WEST, Boolean.valueOf(true)), footX + 1, footY + 2, footZ, chunkBB);
/*  265 */           placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.EAST, Boolean.valueOf(true))).setValue(IronBarsBlock.WEST, Boolean.valueOf(true)), footX + 2, footY + 2, footZ, chunkBB);
/*  266 */           placeBlock(level, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.EAST, Boolean.valueOf(true)), footX + 2, footY + 1, footZ, chunkBB);
/*  267 */           placeBlock(level, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.EAST, Boolean.valueOf(true)), footX + 2, footY, footZ, chunkBB);
/*      */           break;
/*      */         case 3:
/*  270 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY, footZ, chunkBB);
/*  271 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 1, footZ, chunkBB);
/*  272 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 2, footZ, chunkBB);
/*  273 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 1, footY + 2, footZ, chunkBB);
/*  274 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 2, footZ, chunkBB);
/*  275 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 1, footZ, chunkBB);
/*  276 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY, footZ, chunkBB);
/*  277 */           placeBlock(level, Blocks.IRON_DOOR.defaultBlockState(), footX + 1, footY, footZ, chunkBB);
/*  278 */           placeBlock(level, (BlockState)Blocks.IRON_DOOR.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), footX + 1, footY + 1, footZ, chunkBB);
/*  279 */           placeBlock(level, (BlockState)Blocks.STONE_BUTTON.defaultBlockState().setValue(ButtonBlock.FACING, Direction.NORTH), footX + 2, footY + 1, footZ + 1, chunkBB);
/*  280 */           placeBlock(level, (BlockState)Blocks.STONE_BUTTON.defaultBlockState().setValue(ButtonBlock.FACING, Direction.SOUTH), footX + 2, footY + 1, footZ - 1, chunkBB);
/*      */           break;
/*      */       } 
/*      */     }
/*      */     
/*      */     protected SmallDoorType randomSmallDoor(RandomSource random) {
/*  286 */       int selection = random.nextInt(5);
/*  287 */       switch (selection)
/*      */       
/*      */       { 
/*      */         default:
/*  291 */           return SmallDoorType.OPENING;
/*      */         case 2:
/*  293 */           return SmallDoorType.WOOD_DOOR;
/*      */         case 3:
/*  295 */           return SmallDoorType.GRATES;
/*      */         case 4:
/*  297 */           break; }  return SmallDoorType.IRON_DOOR;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected StructurePiece generateSmallDoorChildForward(StrongholdPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int xOff, int yOff) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: invokevirtual getOrientation : ()Lnet/minecraft/core/Direction;
/*      */       //   4: astore #6
/*      */       //   6: aload #6
/*      */       //   8: ifnull -> 220
/*      */       //   11: getstatic net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$3.$SwitchMap$net$minecraft$core$Direction : [I
/*      */       //   14: aload #6
/*      */       //   16: invokevirtual ordinal : ()I
/*      */       //   19: iaload
/*      */       //   20: tableswitch default -> 220, 1 -> 52, 2 -> 94, 3 -> 136, 4 -> 178
/*      */       //   52: aload_1
/*      */       //   53: aload_2
/*      */       //   54: aload_3
/*      */       //   55: aload_0
/*      */       //   56: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   59: invokevirtual minX : ()I
/*      */       //   62: iload #4
/*      */       //   64: iadd
/*      */       //   65: aload_0
/*      */       //   66: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   69: invokevirtual minY : ()I
/*      */       //   72: iload #5
/*      */       //   74: iadd
/*      */       //   75: aload_0
/*      */       //   76: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   79: invokevirtual minZ : ()I
/*      */       //   82: iconst_1
/*      */       //   83: isub
/*      */       //   84: aload #6
/*      */       //   86: aload_0
/*      */       //   87: invokevirtual getGenDepth : ()I
/*      */       //   90: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   93: areturn
/*      */       //   94: aload_1
/*      */       //   95: aload_2
/*      */       //   96: aload_3
/*      */       //   97: aload_0
/*      */       //   98: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   101: invokevirtual minX : ()I
/*      */       //   104: iload #4
/*      */       //   106: iadd
/*      */       //   107: aload_0
/*      */       //   108: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   111: invokevirtual minY : ()I
/*      */       //   114: iload #5
/*      */       //   116: iadd
/*      */       //   117: aload_0
/*      */       //   118: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   121: invokevirtual maxZ : ()I
/*      */       //   124: iconst_1
/*      */       //   125: iadd
/*      */       //   126: aload #6
/*      */       //   128: aload_0
/*      */       //   129: invokevirtual getGenDepth : ()I
/*      */       //   132: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   135: areturn
/*      */       //   136: aload_1
/*      */       //   137: aload_2
/*      */       //   138: aload_3
/*      */       //   139: aload_0
/*      */       //   140: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   143: invokevirtual minX : ()I
/*      */       //   146: iconst_1
/*      */       //   147: isub
/*      */       //   148: aload_0
/*      */       //   149: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   152: invokevirtual minY : ()I
/*      */       //   155: iload #5
/*      */       //   157: iadd
/*      */       //   158: aload_0
/*      */       //   159: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   162: invokevirtual minZ : ()I
/*      */       //   165: iload #4
/*      */       //   167: iadd
/*      */       //   168: aload #6
/*      */       //   170: aload_0
/*      */       //   171: invokevirtual getGenDepth : ()I
/*      */       //   174: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   177: areturn
/*      */       //   178: aload_1
/*      */       //   179: aload_2
/*      */       //   180: aload_3
/*      */       //   181: aload_0
/*      */       //   182: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   185: invokevirtual maxX : ()I
/*      */       //   188: iconst_1
/*      */       //   189: iadd
/*      */       //   190: aload_0
/*      */       //   191: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   194: invokevirtual minY : ()I
/*      */       //   197: iload #5
/*      */       //   199: iadd
/*      */       //   200: aload_0
/*      */       //   201: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   204: invokevirtual minZ : ()I
/*      */       //   207: iload #4
/*      */       //   209: iadd
/*      */       //   210: aload #6
/*      */       //   212: aload_0
/*      */       //   213: invokevirtual getGenDepth : ()I
/*      */       //   216: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   219: areturn
/*      */       //   220: aconst_null
/*      */       //   221: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #302	-> 0
/*      */       //   #303	-> 6
/*      */       //   #304	-> 11
/*      */       //   #306	-> 52
/*      */       //   #308	-> 94
/*      */       //   #310	-> 136
/*      */       //   #312	-> 178
/*      */       //   #315	-> 220
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	222	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StrongholdPiece;
/*      */       //   0	222	1	startPiece	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;
/*      */       //   0	222	2	structurePieceAccessor	Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;
/*      */       //   0	222	3	random	Lnet/minecraft/util/RandomSource;
/*      */       //   0	222	4	xOff	I
/*      */       //   0	222	5	yOff	I
/*      */       //   6	216	6	orientation	Lnet/minecraft/core/Direction; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected StructurePiece generateSmallDoorChildLeft(StrongholdPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int yOff, int zOff) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: invokevirtual getOrientation : ()Lnet/minecraft/core/Direction;
/*      */       //   4: astore #6
/*      */       //   6: aload #6
/*      */       //   8: ifnull -> 224
/*      */       //   11: getstatic net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$3.$SwitchMap$net$minecraft$core$Direction : [I
/*      */       //   14: aload #6
/*      */       //   16: invokevirtual ordinal : ()I
/*      */       //   19: iaload
/*      */       //   20: tableswitch default -> 224, 1 -> 52, 2 -> 95, 3 -> 138, 4 -> 181
/*      */       //   52: aload_1
/*      */       //   53: aload_2
/*      */       //   54: aload_3
/*      */       //   55: aload_0
/*      */       //   56: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   59: invokevirtual minX : ()I
/*      */       //   62: iconst_1
/*      */       //   63: isub
/*      */       //   64: aload_0
/*      */       //   65: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   68: invokevirtual minY : ()I
/*      */       //   71: iload #4
/*      */       //   73: iadd
/*      */       //   74: aload_0
/*      */       //   75: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   78: invokevirtual minZ : ()I
/*      */       //   81: iload #5
/*      */       //   83: iadd
/*      */       //   84: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*      */       //   87: aload_0
/*      */       //   88: invokevirtual getGenDepth : ()I
/*      */       //   91: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   94: areturn
/*      */       //   95: aload_1
/*      */       //   96: aload_2
/*      */       //   97: aload_3
/*      */       //   98: aload_0
/*      */       //   99: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   102: invokevirtual minX : ()I
/*      */       //   105: iconst_1
/*      */       //   106: isub
/*      */       //   107: aload_0
/*      */       //   108: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   111: invokevirtual minY : ()I
/*      */       //   114: iload #4
/*      */       //   116: iadd
/*      */       //   117: aload_0
/*      */       //   118: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   121: invokevirtual minZ : ()I
/*      */       //   124: iload #5
/*      */       //   126: iadd
/*      */       //   127: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*      */       //   130: aload_0
/*      */       //   131: invokevirtual getGenDepth : ()I
/*      */       //   134: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   137: areturn
/*      */       //   138: aload_1
/*      */       //   139: aload_2
/*      */       //   140: aload_3
/*      */       //   141: aload_0
/*      */       //   142: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   145: invokevirtual minX : ()I
/*      */       //   148: iload #5
/*      */       //   150: iadd
/*      */       //   151: aload_0
/*      */       //   152: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   155: invokevirtual minY : ()I
/*      */       //   158: iload #4
/*      */       //   160: iadd
/*      */       //   161: aload_0
/*      */       //   162: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   165: invokevirtual minZ : ()I
/*      */       //   168: iconst_1
/*      */       //   169: isub
/*      */       //   170: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*      */       //   173: aload_0
/*      */       //   174: invokevirtual getGenDepth : ()I
/*      */       //   177: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   180: areturn
/*      */       //   181: aload_1
/*      */       //   182: aload_2
/*      */       //   183: aload_3
/*      */       //   184: aload_0
/*      */       //   185: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   188: invokevirtual minX : ()I
/*      */       //   191: iload #5
/*      */       //   193: iadd
/*      */       //   194: aload_0
/*      */       //   195: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   198: invokevirtual minY : ()I
/*      */       //   201: iload #4
/*      */       //   203: iadd
/*      */       //   204: aload_0
/*      */       //   205: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   208: invokevirtual minZ : ()I
/*      */       //   211: iconst_1
/*      */       //   212: isub
/*      */       //   213: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*      */       //   216: aload_0
/*      */       //   217: invokevirtual getGenDepth : ()I
/*      */       //   220: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   223: areturn
/*      */       //   224: aconst_null
/*      */       //   225: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #319	-> 0
/*      */       //   #320	-> 6
/*      */       //   #321	-> 11
/*      */       //   #323	-> 52
/*      */       //   #325	-> 95
/*      */       //   #327	-> 138
/*      */       //   #329	-> 181
/*      */       //   #332	-> 224
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	226	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StrongholdPiece;
/*      */       //   0	226	1	startPiece	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;
/*      */       //   0	226	2	structurePieceAccessor	Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;
/*      */       //   0	226	3	random	Lnet/minecraft/util/RandomSource;
/*      */       //   0	226	4	yOff	I
/*      */       //   0	226	5	zOff	I
/*      */       //   6	220	6	orientation	Lnet/minecraft/core/Direction; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected StructurePiece generateSmallDoorChildRight(StrongholdPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int yOff, int zOff) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: invokevirtual getOrientation : ()Lnet/minecraft/core/Direction;
/*      */       //   4: astore #6
/*      */       //   6: aload #6
/*      */       //   8: ifnull -> 224
/*      */       //   11: getstatic net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$3.$SwitchMap$net$minecraft$core$Direction : [I
/*      */       //   14: aload #6
/*      */       //   16: invokevirtual ordinal : ()I
/*      */       //   19: iaload
/*      */       //   20: tableswitch default -> 224, 1 -> 52, 2 -> 95, 3 -> 138, 4 -> 181
/*      */       //   52: aload_1
/*      */       //   53: aload_2
/*      */       //   54: aload_3
/*      */       //   55: aload_0
/*      */       //   56: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   59: invokevirtual maxX : ()I
/*      */       //   62: iconst_1
/*      */       //   63: iadd
/*      */       //   64: aload_0
/*      */       //   65: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   68: invokevirtual minY : ()I
/*      */       //   71: iload #4
/*      */       //   73: iadd
/*      */       //   74: aload_0
/*      */       //   75: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   78: invokevirtual minZ : ()I
/*      */       //   81: iload #5
/*      */       //   83: iadd
/*      */       //   84: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*      */       //   87: aload_0
/*      */       //   88: invokevirtual getGenDepth : ()I
/*      */       //   91: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   94: areturn
/*      */       //   95: aload_1
/*      */       //   96: aload_2
/*      */       //   97: aload_3
/*      */       //   98: aload_0
/*      */       //   99: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   102: invokevirtual maxX : ()I
/*      */       //   105: iconst_1
/*      */       //   106: iadd
/*      */       //   107: aload_0
/*      */       //   108: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   111: invokevirtual minY : ()I
/*      */       //   114: iload #4
/*      */       //   116: iadd
/*      */       //   117: aload_0
/*      */       //   118: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   121: invokevirtual minZ : ()I
/*      */       //   124: iload #5
/*      */       //   126: iadd
/*      */       //   127: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*      */       //   130: aload_0
/*      */       //   131: invokevirtual getGenDepth : ()I
/*      */       //   134: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   137: areturn
/*      */       //   138: aload_1
/*      */       //   139: aload_2
/*      */       //   140: aload_3
/*      */       //   141: aload_0
/*      */       //   142: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   145: invokevirtual minX : ()I
/*      */       //   148: iload #5
/*      */       //   150: iadd
/*      */       //   151: aload_0
/*      */       //   152: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   155: invokevirtual minY : ()I
/*      */       //   158: iload #4
/*      */       //   160: iadd
/*      */       //   161: aload_0
/*      */       //   162: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   165: invokevirtual maxZ : ()I
/*      */       //   168: iconst_1
/*      */       //   169: iadd
/*      */       //   170: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*      */       //   173: aload_0
/*      */       //   174: invokevirtual getGenDepth : ()I
/*      */       //   177: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   180: areturn
/*      */       //   181: aload_1
/*      */       //   182: aload_2
/*      */       //   183: aload_3
/*      */       //   184: aload_0
/*      */       //   185: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   188: invokevirtual minX : ()I
/*      */       //   191: iload #5
/*      */       //   193: iadd
/*      */       //   194: aload_0
/*      */       //   195: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   198: invokevirtual minY : ()I
/*      */       //   201: iload #4
/*      */       //   203: iadd
/*      */       //   204: aload_0
/*      */       //   205: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   208: invokevirtual maxZ : ()I
/*      */       //   211: iconst_1
/*      */       //   212: iadd
/*      */       //   213: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*      */       //   216: aload_0
/*      */       //   217: invokevirtual getGenDepth : ()I
/*      */       //   220: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   223: areturn
/*      */       //   224: aconst_null
/*      */       //   225: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #336	-> 0
/*      */       //   #337	-> 6
/*      */       //   #338	-> 11
/*      */       //   #340	-> 52
/*      */       //   #342	-> 95
/*      */       //   #344	-> 138
/*      */       //   #346	-> 181
/*      */       //   #349	-> 224
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	226	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StrongholdPiece;
/*      */       //   0	226	1	startPiece	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;
/*      */       //   0	226	2	structurePieceAccessor	Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;
/*      */       //   0	226	3	random	Lnet/minecraft/util/RandomSource;
/*      */       //   0	226	4	yOff	I
/*      */       //   0	226	5	zOff	I
/*      */       //   6	220	6	orientation	Lnet/minecraft/core/Direction; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  353 */     protected static boolean isOkBox(BoundingBox box) { return (box.minY() > 10); }
/*      */   }
/*      */   protected enum SmallDoorType { OPENING, WOOD_DOOR, GRATES, IRON_DOOR;
/*      */     @Deprecated
/*      */     public static final Codec<SmallDoorType> LEGACY_CODEC;
/*      */     
/*      */     static  {
/*      */       LEGACY_CODEC = ExtraCodecs.legacyEnum(SmallDoorType::valueOf);
/*      */     } }
/*      */   
/*      */   public static class FillerCorridor extends StrongholdPiece { public FillerCorridor(int genDepth, BoundingBox boundingBox, Direction direction) {
/*  364 */       super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, genDepth, boundingBox);
/*      */       
/*  366 */       setOrientation(direction);
/*  367 */       this.steps = (direction == Direction.NORTH || direction == Direction.SOUTH) ? boundingBox.getZSpan() : boundingBox.getXSpan();
/*      */     }
/*      */     private final int steps;
/*      */     public FillerCorridor(CompoundTag tag) {
/*  371 */       super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, tag);
/*  372 */       this.steps = tag.getIntOr("Steps", 0);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  377 */       super.addAdditionalSaveData(context, tag);
/*  378 */       tag.putInt("Steps", this.steps);
/*      */     }
/*      */     
/*      */     public static BoundingBox findPieceBox(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction) {
/*  382 */       int maxLength = 3;
/*      */       
/*  384 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 4, direction);
/*      */       
/*  386 */       StructurePiece collisionPiece = structurePieceAccessor.findCollisionPiece(box);
/*  387 */       if (collisionPiece == null)
/*      */       {
/*  389 */         return null;
/*      */       }
/*      */       
/*  392 */       if (collisionPiece.getBoundingBox().minY() == box.minY())
/*      */       {
/*  394 */         for (int depth = 2; depth >= 1; depth--) {
/*  395 */           box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, depth, direction);
/*  396 */           if (!collisionPiece.getBoundingBox().intersects(box))
/*      */           {
/*      */             
/*  399 */             return BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, depth + 1, direction);
/*      */           }
/*      */         } 
/*      */       }
/*      */       
/*  404 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  410 */       for (int i = 0; i < this.steps; i++) {
/*      */         
/*  412 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 0, 0, i, chunkBB);
/*  413 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 0, i, chunkBB);
/*  414 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 0, i, chunkBB);
/*  415 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 0, i, chunkBB);
/*  416 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 4, 0, i, chunkBB);
/*      */         
/*  418 */         for (int y = 1; y <= 3; y++) {
/*  419 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 0, y, i, chunkBB);
/*  420 */           placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), 1, y, i, chunkBB);
/*  421 */           placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), 2, y, i, chunkBB);
/*  422 */           placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), 3, y, i, chunkBB);
/*  423 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 4, y, i, chunkBB);
/*      */         } 
/*      */         
/*  426 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 0, 4, i, chunkBB);
/*  427 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 4, i, chunkBB);
/*  428 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 4, i, chunkBB);
/*  429 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 4, i, chunkBB);
/*  430 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 4, 4, i, chunkBB);
/*      */       } 
/*      */     } }
/*      */ 
/*      */   
/*      */   public static class StairsDown
/*      */     extends StrongholdPiece {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 11;
/*      */     private static final int DEPTH = 5;
/*      */     private final boolean isSource;
/*      */     
/*      */     public StairsDown(StructurePieceType type, int genDepth, int west, int north, Direction direction) {
/*  443 */       super(type, genDepth, makeBoundingBox(west, 64, north, direction, 5, 11, 5));
/*      */       
/*  445 */       this.isSource = true;
/*  446 */       setOrientation(direction);
/*  447 */       this.entryDoor = StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING;
/*      */     }
/*      */     
/*      */     public StairsDown(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  451 */       super(StructurePieceType.STRONGHOLD_STAIRS_DOWN, genDepth, boundingBox);
/*      */       
/*  453 */       this.isSource = false;
/*  454 */       setOrientation(direction);
/*  455 */       this.entryDoor = randomSmallDoor(random);
/*      */     }
/*      */     
/*      */     public StairsDown(StructurePieceType type, CompoundTag tag) {
/*  459 */       super(type, tag);
/*  460 */       this.isSource = tag.getBooleanOr("Source", false);
/*      */     }
/*      */ 
/*      */     
/*  464 */     public StairsDown(CompoundTag tag) { this(StructurePieceType.STRONGHOLD_STAIRS_DOWN, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  469 */       super.addAdditionalSaveData(context, tag);
/*  470 */       tag.putBoolean("Source", this.isSource);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  475 */       if (this.isSource)
/*      */       {
/*  477 */         StrongholdPieces.imposedPiece = StrongholdPieces.FiveCrossing.class;
/*      */       }
/*  479 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*      */     }
/*      */     
/*      */     public static StairsDown createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  483 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -7, 0, 5, 11, 5, direction);
/*      */       
/*  485 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/*  486 */         return null;
/*      */       }
/*      */       
/*  489 */       return new StairsDown(genDepth, random, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  495 */       generateBox(level, chunkBB, 0, 0, 0, 4, 10, 4, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/*  497 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 7, 0);
/*      */       
/*  499 */       generateSmallDoor(level, random, chunkBB, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 4);
/*      */ 
/*      */       
/*  502 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 6, 1, chunkBB);
/*  503 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5, 1, chunkBB);
/*  504 */       placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 6, 1, chunkBB);
/*  505 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5, 2, chunkBB);
/*  506 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 4, 3, chunkBB);
/*  507 */       placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 5, 3, chunkBB);
/*  508 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 4, 3, chunkBB);
/*  509 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 3, 3, chunkBB);
/*  510 */       placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3, 4, 3, chunkBB);
/*  511 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 3, 2, chunkBB);
/*  512 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 2, 1, chunkBB);
/*  513 */       placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3, 3, 1, chunkBB);
/*  514 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 2, 1, chunkBB);
/*  515 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 1, 1, chunkBB);
/*  516 */       placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 2, 1, chunkBB);
/*  517 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 1, 2, chunkBB);
/*  518 */       placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 1, 3, chunkBB);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class StartPiece
/*      */     extends StairsDown
/*      */   {
/*      */     public StrongholdPieces.PieceWeight previousPiece;
/*      */     public StrongholdPieces.PortalRoom portalRoomPiece;
/*  527 */     public final List<StructurePiece> pendingChildren = Lists.newArrayList();
/*      */ 
/*      */     
/*  530 */     public StartPiece(RandomSource random, int west, int north) { super(StructurePieceType.STRONGHOLD_START, 0, west, north, getRandomHorizontalDirection(random)); }
/*      */ 
/*      */ 
/*      */     
/*  534 */     public StartPiece(CompoundTag tag) { super(StructurePieceType.STRONGHOLD_START, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public BlockPos getLocatorPosition() {
/*  539 */       if (this.portalRoomPiece != null) {
/*  540 */         return this.portalRoomPiece.getLocatorPosition();
/*      */       }
/*  542 */       return super.getLocatorPosition();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Straight
/*      */     extends StrongholdPiece {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 5;
/*      */     private static final int DEPTH = 7;
/*      */     private final boolean leftChild;
/*      */     private final boolean rightChild;
/*      */     
/*      */     public Straight(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  555 */       super(StructurePieceType.STRONGHOLD_STRAIGHT, genDepth, boundingBox);
/*      */       
/*  557 */       setOrientation(direction);
/*  558 */       this.entryDoor = randomSmallDoor(random);
/*      */       
/*  560 */       this.leftChild = (random.nextInt(2) == 0);
/*  561 */       this.rightChild = (random.nextInt(2) == 0);
/*      */     }
/*      */     
/*      */     public Straight(CompoundTag tag) {
/*  565 */       super(StructurePieceType.STRONGHOLD_STRAIGHT, tag);
/*  566 */       this.leftChild = tag.getBooleanOr("Left", false);
/*  567 */       this.rightChild = tag.getBooleanOr("Right", false);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  572 */       super.addAdditionalSaveData(context, tag);
/*  573 */       tag.putBoolean("Left", this.leftChild);
/*  574 */       tag.putBoolean("Right", this.rightChild);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  579 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*  580 */       if (this.leftChild) {
/*  581 */         generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 2);
/*      */       }
/*  583 */       if (this.rightChild) {
/*  584 */         generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 2);
/*      */       }
/*      */     }
/*      */     
/*      */     public static Straight createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  589 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 7, direction);
/*      */       
/*  591 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/*  592 */         return null;
/*      */       }
/*      */       
/*  595 */       return new Straight(genDepth, random, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  601 */       generateBox(level, chunkBB, 0, 0, 0, 4, 4, 6, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/*  603 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 1, 0);
/*      */       
/*  605 */       generateSmallDoor(level, random, chunkBB, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 6);
/*      */       
/*  607 */       BlockState eastTorch = (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.EAST);
/*  608 */       BlockState westTorch = (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.WEST);
/*      */       
/*  610 */       maybeGenerateBlock(level, chunkBB, random, 0.1F, 1, 2, 1, eastTorch);
/*  611 */       maybeGenerateBlock(level, chunkBB, random, 0.1F, 3, 2, 1, westTorch);
/*  612 */       maybeGenerateBlock(level, chunkBB, random, 0.1F, 1, 2, 5, eastTorch);
/*  613 */       maybeGenerateBlock(level, chunkBB, random, 0.1F, 3, 2, 5, westTorch);
/*      */       
/*  615 */       if (this.leftChild) {
/*  616 */         generateBox(level, chunkBB, 0, 1, 2, 0, 3, 4, CAVE_AIR, CAVE_AIR, false);
/*      */       }
/*  618 */       if (this.rightChild)
/*  619 */         generateBox(level, chunkBB, 4, 1, 2, 4, 3, 4, CAVE_AIR, CAVE_AIR, false); 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ChestCorridor
/*      */     extends StrongholdPiece
/*      */   {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 5;
/*      */     private static final int DEPTH = 7;
/*      */     private boolean hasPlacedChest;
/*      */     
/*      */     public ChestCorridor(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  632 */       super(StructurePieceType.STRONGHOLD_CHEST_CORRIDOR, genDepth, boundingBox);
/*      */       
/*  634 */       setOrientation(direction);
/*  635 */       this.entryDoor = randomSmallDoor(random);
/*      */     }
/*      */     
/*      */     public ChestCorridor(CompoundTag tag) {
/*  639 */       super(StructurePieceType.STRONGHOLD_CHEST_CORRIDOR, tag);
/*  640 */       this.hasPlacedChest = tag.getBooleanOr("Chest", false);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  645 */       super.addAdditionalSaveData(context, tag);
/*  646 */       tag.putBoolean("Chest", this.hasPlacedChest);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  651 */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) { generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1); }
/*      */ 
/*      */     
/*      */     public static ChestCorridor createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  655 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 7, direction);
/*      */       
/*  657 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/*  658 */         return null;
/*      */       }
/*      */       
/*  661 */       return new ChestCorridor(genDepth, random, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  667 */       generateBox(level, chunkBB, 0, 0, 0, 4, 4, 6, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/*  669 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 1, 0);
/*      */       
/*  671 */       generateSmallDoor(level, random, chunkBB, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 6);
/*      */ 
/*      */       
/*  674 */       generateBox(level, chunkBB, 3, 1, 2, 3, 1, 4, Blocks.STONE_BRICKS.defaultBlockState(), Blocks.STONE_BRICKS.defaultBlockState(), false);
/*  675 */       placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 1, 1, chunkBB);
/*  676 */       placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 1, 5, chunkBB);
/*  677 */       placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 2, 2, chunkBB);
/*  678 */       placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 2, 4, chunkBB);
/*  679 */       for (int z = 2; z <= 4; z++) {
/*  680 */         placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 2, 1, z, chunkBB);
/*      */       }
/*      */       
/*  683 */       if (!this.hasPlacedChest && 
/*  684 */         chunkBB.isInside(getWorldPos(3, 2, 3))) {
/*  685 */         this.hasPlacedChest = true;
/*  686 */         createChest(level, chunkBB, random, 3, 2, 3, BuiltInLootTables.STRONGHOLD_CORRIDOR);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class StraightStairsDown
/*      */     extends StrongholdPiece {
/*      */     private static final int WIDTH = 5;
/*      */     private static final int HEIGHT = 11;
/*      */     private static final int DEPTH = 8;
/*      */     
/*      */     public StraightStairsDown(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  698 */       super(StructurePieceType.STRONGHOLD_STRAIGHT_STAIRS_DOWN, genDepth, boundingBox);
/*      */       
/*  700 */       setOrientation(direction);
/*  701 */       this.entryDoor = randomSmallDoor(random);
/*      */     }
/*      */ 
/*      */     
/*  705 */     public StraightStairsDown(CompoundTag tag) { super(StructurePieceType.STRONGHOLD_STRAIGHT_STAIRS_DOWN, tag); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  710 */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) { generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1); }
/*      */ 
/*      */     
/*      */     public static StraightStairsDown createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  714 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -7, 0, 5, 11, 8, direction);
/*      */       
/*  716 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/*  717 */         return null;
/*      */       }
/*      */       
/*  720 */       return new StraightStairsDown(genDepth, random, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  726 */       generateBox(level, chunkBB, 0, 0, 0, 4, 10, 7, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/*  728 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 7, 0);
/*      */       
/*  730 */       generateSmallDoor(level, random, chunkBB, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 7);
/*      */ 
/*      */       
/*  733 */       BlockState stairs = (BlockState)Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.SOUTH);
/*  734 */       for (int i = 0; i < 6; i++) {
/*  735 */         placeBlock(level, stairs, 1, 6 - i, 1 + i, chunkBB);
/*  736 */         placeBlock(level, stairs, 2, 6 - i, 1 + i, chunkBB);
/*  737 */         placeBlock(level, stairs, 3, 6 - i, 1 + i, chunkBB);
/*  738 */         if (i < 5) {
/*  739 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5 - i, 1 + i, chunkBB);
/*  740 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 5 - i, 1 + i, chunkBB);
/*  741 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 5 - i, 1 + i, chunkBB);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static abstract class Turn
/*      */     extends StrongholdPiece {
/*      */     protected static final int WIDTH = 5;
/*      */     protected static final int HEIGHT = 5;
/*      */     protected static final int DEPTH = 5;
/*      */     
/*  753 */     protected Turn(StructurePieceType type, int genDepth, BoundingBox boundingBox) { super(type, genDepth, boundingBox); }
/*      */ 
/*      */ 
/*      */     
/*  757 */     public Turn(StructurePieceType type, CompoundTag tag) { super(type, tag); }
/*      */   }
/*      */   
/*      */   public static class LeftTurn
/*      */     extends Turn {
/*      */     public LeftTurn(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  763 */       super(StructurePieceType.STRONGHOLD_LEFT_TURN, genDepth, boundingBox);
/*      */       
/*  765 */       setOrientation(direction);
/*  766 */       this.entryDoor = randomSmallDoor(random);
/*      */     }
/*      */ 
/*      */     
/*  770 */     public LeftTurn(CompoundTag tag) { super(StructurePieceType.STRONGHOLD_LEFT_TURN, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  775 */       Direction orientation = getOrientation();
/*  776 */       if (orientation == Direction.NORTH || orientation == Direction.EAST) {
/*  777 */         generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*      */       } else {
/*  779 */         generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*      */       } 
/*      */     }
/*      */     
/*      */     public static LeftTurn createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  784 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 5, direction);
/*      */       
/*  786 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/*  787 */         return null;
/*      */       }
/*      */       
/*  790 */       return new LeftTurn(genDepth, random, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  796 */       generateBox(level, chunkBB, 0, 0, 0, 4, 4, 4, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/*  798 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 1, 0);
/*      */       
/*  800 */       Direction orientation = getOrientation();
/*  801 */       if (orientation == Direction.NORTH || orientation == Direction.EAST) {
/*  802 */         generateBox(level, chunkBB, 0, 1, 1, 0, 3, 3, CAVE_AIR, CAVE_AIR, false);
/*      */       } else {
/*  804 */         generateBox(level, chunkBB, 4, 1, 1, 4, 3, 3, CAVE_AIR, CAVE_AIR, false);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class RightTurn extends Turn {
/*      */     public RightTurn(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  811 */       super(StructurePieceType.STRONGHOLD_RIGHT_TURN, genDepth, boundingBox);
/*      */       
/*  813 */       setOrientation(direction);
/*  814 */       this.entryDoor = randomSmallDoor(random);
/*      */     }
/*      */ 
/*      */     
/*  818 */     public RightTurn(CompoundTag tag) { super(StructurePieceType.STRONGHOLD_RIGHT_TURN, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  823 */       Direction orientation = getOrientation();
/*  824 */       if (orientation == Direction.NORTH || orientation == Direction.EAST) {
/*  825 */         generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*      */       } else {
/*  827 */         generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*      */       } 
/*      */     }
/*      */     
/*      */     public static RightTurn createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  832 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 5, direction);
/*      */       
/*  834 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/*  835 */         return null;
/*      */       }
/*      */       
/*  838 */       return new RightTurn(genDepth, random, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  844 */       generateBox(level, chunkBB, 0, 0, 0, 4, 4, 4, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/*  846 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 1, 0);
/*      */       
/*  848 */       Direction orientation = getOrientation();
/*  849 */       if (orientation == Direction.NORTH || orientation == Direction.EAST) {
/*  850 */         generateBox(level, chunkBB, 4, 1, 1, 4, 3, 3, CAVE_AIR, CAVE_AIR, false);
/*      */       } else {
/*  852 */         generateBox(level, chunkBB, 0, 1, 1, 0, 3, 3, CAVE_AIR, CAVE_AIR, false);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class RoomCrossing
/*      */     extends StrongholdPiece {
/*      */     protected static final int WIDTH = 11;
/*      */     protected static final int HEIGHT = 7;
/*      */     protected static final int DEPTH = 11;
/*      */     protected final int type;
/*      */     
/*      */     public RoomCrossing(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  865 */       super(StructurePieceType.STRONGHOLD_ROOM_CROSSING, genDepth, boundingBox);
/*      */       
/*  867 */       setOrientation(direction);
/*  868 */       this.entryDoor = randomSmallDoor(random);
/*  869 */       this.type = random.nextInt(5);
/*      */     }
/*      */     
/*      */     public RoomCrossing(CompoundTag tag) {
/*  873 */       super(StructurePieceType.STRONGHOLD_ROOM_CROSSING, tag);
/*  874 */       this.type = tag.getIntOr("Type", 0);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  879 */       super.addAdditionalSaveData(context, tag);
/*  880 */       tag.putInt("Type", this.type);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  885 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 4, 1);
/*  886 */       generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 4);
/*  887 */       generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 4);
/*      */     }
/*      */     
/*      */     public static RoomCrossing createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  891 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 11, 7, 11, direction);
/*      */       
/*  893 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/*  894 */         return null;
/*      */       }
/*      */       
/*  897 */       return new RoomCrossing(genDepth, random, box, direction);
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*      */       int i;
/*  903 */       generateBox(level, chunkBB, 0, 0, 0, 10, 6, 10, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/*  905 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 4, 1, 0);
/*      */       
/*  907 */       generateBox(level, chunkBB, 4, 1, 10, 6, 3, 10, CAVE_AIR, CAVE_AIR, false);
/*  908 */       generateBox(level, chunkBB, 0, 1, 4, 0, 3, 6, CAVE_AIR, CAVE_AIR, false);
/*  909 */       generateBox(level, chunkBB, 10, 1, 4, 10, 3, 6, CAVE_AIR, CAVE_AIR, false);
/*      */       
/*  911 */       switch (this.type) {
/*      */         default:
/*      */           return;
/*      */         
/*      */         case 0:
/*  916 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, chunkBB);
/*  917 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 2, 5, chunkBB);
/*  918 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 3, 5, chunkBB);
/*  919 */           placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.WEST), 4, 3, 5, chunkBB);
/*  920 */           placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.EAST), 6, 3, 5, chunkBB);
/*  921 */           placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.SOUTH), 5, 3, 4, chunkBB);
/*  922 */           placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.NORTH), 5, 3, 6, chunkBB);
/*  923 */           placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 4, chunkBB);
/*  924 */           placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 5, chunkBB);
/*  925 */           placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 6, chunkBB);
/*  926 */           placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 4, chunkBB);
/*  927 */           placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 5, chunkBB);
/*  928 */           placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 6, chunkBB);
/*  929 */           placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 5, 1, 4, chunkBB);
/*  930 */           placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 5, 1, 6, chunkBB);
/*      */         
/*      */         case 1:
/*  933 */           for (i = 0; i < 5; i++) {
/*  934 */             placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 1, 3 + i, chunkBB);
/*  935 */             placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 7, 1, 3 + i, chunkBB);
/*  936 */             placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3 + i, 1, 3, chunkBB);
/*  937 */             placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3 + i, 1, 7, chunkBB);
/*      */           } 
/*  939 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, chunkBB);
/*  940 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 2, 5, chunkBB);
/*  941 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 3, 5, chunkBB);
/*  942 */           placeBlock(level, Blocks.WATER.defaultBlockState(), 5, 4, 5, chunkBB);
/*      */         case 2:
/*      */           break;
/*  945 */       }  for (int z = 1; z <= 9; z++) {
/*  946 */         placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 1, 3, z, chunkBB);
/*  947 */         placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 9, 3, z, chunkBB);
/*      */       } 
/*  949 */       for (int x = 1; x <= 9; x++) {
/*  950 */         placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), x, 3, 1, chunkBB);
/*  951 */         placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), x, 3, 9, chunkBB);
/*      */       } 
/*  953 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 5, 1, 4, chunkBB);
/*  954 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 5, 1, 6, chunkBB);
/*  955 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 5, 3, 4, chunkBB);
/*  956 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 5, 3, 6, chunkBB);
/*  957 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 4, 1, 5, chunkBB);
/*  958 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 6, 1, 5, chunkBB);
/*  959 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 4, 3, 5, chunkBB);
/*  960 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 6, 3, 5, chunkBB);
/*  961 */       for (int y = 1; y <= 3; y++) {
/*  962 */         placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 4, y, 4, chunkBB);
/*  963 */         placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 6, y, 4, chunkBB);
/*  964 */         placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 4, y, 6, chunkBB);
/*  965 */         placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 6, y, 6, chunkBB);
/*      */       } 
/*  967 */       placeBlock(level, Blocks.WALL_TORCH.defaultBlockState(), 5, 3, 5, chunkBB);
/*  968 */       for (int z = 2; z <= 8; z++) {
/*  969 */         placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 2, 3, z, chunkBB);
/*  970 */         placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 3, 3, z, chunkBB);
/*  971 */         if (z <= 3 || z >= 7) {
/*  972 */           placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 4, 3, z, chunkBB);
/*  973 */           placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 5, 3, z, chunkBB);
/*  974 */           placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 6, 3, z, chunkBB);
/*      */         } 
/*  976 */         placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 7, 3, z, chunkBB);
/*  977 */         placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 8, 3, z, chunkBB);
/*      */       } 
/*  979 */       BlockState ladder = (BlockState)Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.WEST);
/*  980 */       placeBlock(level, ladder, 9, 1, 3, chunkBB);
/*  981 */       placeBlock(level, ladder, 9, 2, 3, chunkBB);
/*  982 */       placeBlock(level, ladder, 9, 3, 3, chunkBB);
/*      */       
/*  984 */       createChest(level, chunkBB, random, 3, 4, 8, BuiltInLootTables.STRONGHOLD_CROSSING);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class PrisonHall
/*      */     extends StrongholdPiece
/*      */   {
/*      */     protected static final int WIDTH = 9;
/*      */     protected static final int HEIGHT = 5;
/*      */     protected static final int DEPTH = 11;
/*      */     
/*      */     public PrisonHall(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  997 */       super(StructurePieceType.STRONGHOLD_PRISON_HALL, genDepth, boundingBox);
/*      */       
/*  999 */       setOrientation(direction);
/* 1000 */       this.entryDoor = randomSmallDoor(random);
/*      */     }
/*      */ 
/*      */     
/* 1004 */     public PrisonHall(CompoundTag tag) { super(StructurePieceType.STRONGHOLD_PRISON_HALL, tag); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1009 */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) { generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1); }
/*      */ 
/*      */     
/*      */     public static PrisonHall createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1013 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 9, 5, 11, direction);
/*      */       
/* 1015 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/* 1016 */         return null;
/*      */       }
/*      */       
/* 1019 */       return new PrisonHall(genDepth, random, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1025 */       generateBox(level, chunkBB, 0, 0, 0, 8, 4, 10, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/* 1027 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 1, 0);
/*      */       
/* 1029 */       generateBox(level, chunkBB, 1, 1, 10, 3, 3, 10, CAVE_AIR, CAVE_AIR, false);
/*      */ 
/*      */       
/* 1032 */       generateBox(level, chunkBB, 4, 1, 1, 4, 3, 1, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1033 */       generateBox(level, chunkBB, 4, 1, 3, 4, 3, 3, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1034 */       generateBox(level, chunkBB, 4, 1, 7, 4, 3, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1035 */       generateBox(level, chunkBB, 4, 1, 9, 4, 3, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */ 
/*      */       
/* 1038 */       for (int y = 1; y <= 3; y++) {
/* 1039 */         placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue(IronBarsBlock.SOUTH, Boolean.valueOf(true)), 4, y, 4, chunkBB);
/* 1040 */         placeBlock(level, (BlockState)((BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue(IronBarsBlock.SOUTH, Boolean.valueOf(true))).setValue(IronBarsBlock.EAST, Boolean.valueOf(true)), 4, y, 5, chunkBB);
/* 1041 */         placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue(IronBarsBlock.SOUTH, Boolean.valueOf(true)), 4, y, 6, chunkBB);
/*      */         
/* 1043 */         placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.WEST, Boolean.valueOf(true))).setValue(IronBarsBlock.EAST, Boolean.valueOf(true)), 5, y, 5, chunkBB);
/* 1044 */         placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.WEST, Boolean.valueOf(true))).setValue(IronBarsBlock.EAST, Boolean.valueOf(true)), 6, y, 5, chunkBB);
/* 1045 */         placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.WEST, Boolean.valueOf(true))).setValue(IronBarsBlock.EAST, Boolean.valueOf(true)), 7, y, 5, chunkBB);
/*      */       } 
/*      */ 
/*      */       
/* 1049 */       placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue(IronBarsBlock.SOUTH, Boolean.valueOf(true)), 4, 3, 2, chunkBB);
/* 1050 */       placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue(IronBarsBlock.SOUTH, Boolean.valueOf(true)), 4, 3, 8, chunkBB);
/* 1051 */       BlockState doorBottom = (BlockState)Blocks.IRON_DOOR.defaultBlockState().setValue(DoorBlock.FACING, Direction.WEST);
/* 1052 */       BlockState doorTop = (BlockState)((BlockState)Blocks.IRON_DOOR.defaultBlockState().setValue(DoorBlock.FACING, Direction.WEST)).setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER);
/* 1053 */       placeBlock(level, doorBottom, 4, 1, 2, chunkBB);
/* 1054 */       placeBlock(level, doorTop, 4, 2, 2, chunkBB);
/* 1055 */       placeBlock(level, doorBottom, 4, 1, 8, chunkBB);
/* 1056 */       placeBlock(level, doorTop, 4, 2, 8, chunkBB);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Library
/*      */     extends StrongholdPiece {
/*      */     protected static final int WIDTH = 14;
/*      */     protected static final int HEIGHT = 6;
/*      */     protected static final int TALL_HEIGHT = 11;
/*      */     protected static final int DEPTH = 15;
/*      */     private final boolean isTall;
/*      */     
/*      */     public Library(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 1069 */       super(StructurePieceType.STRONGHOLD_LIBRARY, genDepth, boundingBox);
/*      */       
/* 1071 */       setOrientation(direction);
/* 1072 */       this.entryDoor = randomSmallDoor(random);
/* 1073 */       this.isTall = (boundingBox.getYSpan() > 6);
/*      */     }
/*      */     
/*      */     public Library(CompoundTag tag) {
/* 1077 */       super(StructurePieceType.STRONGHOLD_LIBRARY, tag);
/* 1078 */       this.isTall = tag.getBooleanOr("Tall", false);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 1083 */       super.addAdditionalSaveData(context, tag);
/* 1084 */       tag.putBoolean("Tall", this.isTall);
/*      */     }
/*      */ 
/*      */     
/*      */     public static Library createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1089 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 14, 11, 15, direction);
/*      */       
/* 1091 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/*      */         
/* 1093 */         box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 14, 6, 15, direction);
/*      */         
/* 1095 */         if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/* 1096 */           return null;
/*      */         }
/*      */       } 
/*      */       
/* 1100 */       return new Library(genDepth, random, box, direction);
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1105 */       int currentHeight = 11;
/* 1106 */       if (!this.isTall) {
/* 1107 */         currentHeight = 6;
/*      */       }
/*      */ 
/*      */       
/* 1111 */       generateBox(level, chunkBB, 0, 0, 0, 13, currentHeight - 1, 14, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/* 1113 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 4, 1, 0);
/*      */ 
/*      */       
/* 1116 */       generateMaybeBox(level, chunkBB, random, 0.07F, 2, 1, 1, 11, 4, 13, Blocks.COBWEB.defaultBlockState(), Blocks.COBWEB.defaultBlockState(), false, false);
/*      */       
/* 1118 */       int bookLeft = 1;
/* 1119 */       int bookRight = 12;
/*      */ 
/*      */       
/* 1122 */       for (int d = 1; d <= 13; d++) {
/* 1123 */         if ((d - 1) % 4 == 0) {
/* 1124 */           generateBox(level, chunkBB, 1, 1, d, 1, 4, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1125 */           generateBox(level, chunkBB, 12, 1, d, 12, 4, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/*      */           
/* 1127 */           placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.EAST), 2, 3, d, chunkBB);
/* 1128 */           placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.WEST), 11, 3, d, chunkBB);
/*      */           
/* 1130 */           if (this.isTall) {
/* 1131 */             generateBox(level, chunkBB, 1, 6, d, 1, 9, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1132 */             generateBox(level, chunkBB, 12, 6, d, 12, 9, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/*      */           } 
/*      */         } else {
/* 1135 */           generateBox(level, chunkBB, 1, 1, d, 1, 4, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1136 */           generateBox(level, chunkBB, 12, 1, d, 12, 4, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/*      */           
/* 1138 */           if (this.isTall) {
/* 1139 */             generateBox(level, chunkBB, 1, 6, d, 1, 9, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1140 */             generateBox(level, chunkBB, 12, 6, d, 12, 9, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1146 */       for (int d = 3; d < 12; d += 2) {
/* 1147 */         generateBox(level, chunkBB, 3, 1, d, 4, 3, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1148 */         generateBox(level, chunkBB, 6, 1, d, 7, 3, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1149 */         generateBox(level, chunkBB, 9, 1, d, 10, 3, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/*      */       } 
/*      */       
/* 1152 */       if (this.isTall) {
/*      */         
/* 1154 */         generateBox(level, chunkBB, 1, 5, 1, 3, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1155 */         generateBox(level, chunkBB, 10, 5, 1, 12, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1156 */         generateBox(level, chunkBB, 4, 5, 1, 9, 5, 2, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1157 */         generateBox(level, chunkBB, 4, 5, 12, 9, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/*      */         
/* 1159 */         placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 11, chunkBB);
/* 1160 */         placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 8, 5, 11, chunkBB);
/* 1161 */         placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 10, chunkBB);
/*      */         
/* 1163 */         BlockState weFence = (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true));
/* 1164 */         BlockState nsFence = (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */ 
/*      */         
/* 1167 */         generateBox(level, chunkBB, 3, 6, 3, 3, 6, 11, nsFence, nsFence, false);
/* 1168 */         generateBox(level, chunkBB, 10, 6, 3, 10, 6, 9, nsFence, nsFence, false);
/* 1169 */         generateBox(level, chunkBB, 4, 6, 2, 9, 6, 2, weFence, weFence, false);
/* 1170 */         generateBox(level, chunkBB, 4, 6, 12, 7, 6, 12, weFence, weFence, false);
/*      */         
/* 1172 */         placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true)), 3, 6, 2, chunkBB);
/* 1173 */         placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.SOUTH, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true)), 3, 6, 12, chunkBB);
/* 1174 */         placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.WEST, Boolean.valueOf(true)), 10, 6, 2, chunkBB);
/*      */         
/* 1176 */         for (int i = 0; i <= 2; i++) {
/* 1177 */           placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.SOUTH, Boolean.valueOf(true))).setValue(FenceBlock.WEST, Boolean.valueOf(true)), 8 + i, 6, 12 - i, chunkBB);
/* 1178 */           if (i != 2) {
/* 1179 */             placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true)), 8 + i, 6, 11 - i, chunkBB);
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/* 1184 */         BlockState ladder = (BlockState)Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.SOUTH);
/* 1185 */         placeBlock(level, ladder, 10, 1, 13, chunkBB);
/* 1186 */         placeBlock(level, ladder, 10, 2, 13, chunkBB);
/* 1187 */         placeBlock(level, ladder, 10, 3, 13, chunkBB);
/* 1188 */         placeBlock(level, ladder, 10, 4, 13, chunkBB);
/* 1189 */         placeBlock(level, ladder, 10, 5, 13, chunkBB);
/* 1190 */         placeBlock(level, ladder, 10, 6, 13, chunkBB);
/* 1191 */         placeBlock(level, ladder, 10, 7, 13, chunkBB);
/*      */ 
/*      */         
/* 1194 */         int x = 7;
/* 1195 */         int z = 7;
/* 1196 */         BlockState eFence = (BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.EAST, Boolean.valueOf(true));
/* 1197 */         placeBlock(level, eFence, 6, 9, 7, chunkBB);
/* 1198 */         BlockState wFence = (BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true));
/* 1199 */         placeBlock(level, wFence, 7, 9, 7, chunkBB);
/*      */         
/* 1201 */         placeBlock(level, eFence, 6, 8, 7, chunkBB);
/* 1202 */         placeBlock(level, wFence, 7, 8, 7, chunkBB);
/*      */         
/* 1204 */         BlockState nsweFence = (BlockState)((BlockState)nsFence.setValue(FenceBlock.WEST, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true));
/*      */         
/* 1206 */         placeBlock(level, nsweFence, 6, 7, 7, chunkBB);
/* 1207 */         placeBlock(level, nsweFence, 7, 7, 7, chunkBB);
/*      */         
/* 1209 */         placeBlock(level, eFence, 5, 7, 7, chunkBB);
/*      */         
/* 1211 */         placeBlock(level, wFence, 8, 7, 7, chunkBB);
/*      */         
/* 1213 */         placeBlock(level, (BlockState)eFence.setValue(FenceBlock.NORTH, Boolean.valueOf(true)), 6, 7, 6, chunkBB);
/* 1214 */         placeBlock(level, (BlockState)eFence.setValue(FenceBlock.SOUTH, Boolean.valueOf(true)), 6, 7, 8, chunkBB);
/*      */         
/* 1216 */         placeBlock(level, (BlockState)wFence.setValue(FenceBlock.NORTH, Boolean.valueOf(true)), 7, 7, 6, chunkBB);
/* 1217 */         placeBlock(level, (BlockState)wFence.setValue(FenceBlock.SOUTH, Boolean.valueOf(true)), 7, 7, 8, chunkBB);
/*      */         
/* 1219 */         BlockState torch = Blocks.TORCH.defaultBlockState();
/* 1220 */         placeBlock(level, torch, 5, 8, 7, chunkBB);
/* 1221 */         placeBlock(level, torch, 8, 8, 7, chunkBB);
/* 1222 */         placeBlock(level, torch, 6, 8, 6, chunkBB);
/* 1223 */         placeBlock(level, torch, 6, 8, 8, chunkBB);
/* 1224 */         placeBlock(level, torch, 7, 8, 6, chunkBB);
/* 1225 */         placeBlock(level, torch, 7, 8, 8, chunkBB);
/*      */       } 
/*      */ 
/*      */       
/* 1229 */       createChest(level, chunkBB, random, 3, 3, 5, BuiltInLootTables.STRONGHOLD_LIBRARY);
/* 1230 */       if (this.isTall) {
/* 1231 */         placeBlock(level, CAVE_AIR, 12, 9, 1, chunkBB);
/* 1232 */         createChest(level, chunkBB, random, 12, 8, 1, BuiltInLootTables.STRONGHOLD_LIBRARY);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class FiveCrossing
/*      */     extends StrongholdPiece {
/*      */     protected static final int WIDTH = 10;
/*      */     protected static final int HEIGHT = 9;
/*      */     protected static final int DEPTH = 11;
/*      */     private final boolean leftLow;
/*      */     private final boolean leftHigh;
/*      */     private final boolean rightLow;
/*      */     private final boolean rightHigh;
/*      */     
/*      */     public FiveCrossing(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 1248 */       super(StructurePieceType.STRONGHOLD_FIVE_CROSSING, genDepth, boundingBox);
/*      */       
/* 1250 */       setOrientation(direction);
/* 1251 */       this.entryDoor = randomSmallDoor(random);
/*      */       
/* 1253 */       this.leftLow = random.nextBoolean();
/* 1254 */       this.leftHigh = random.nextBoolean();
/* 1255 */       this.rightLow = random.nextBoolean();
/* 1256 */       this.rightHigh = (random.nextInt(3) > 0);
/*      */     }
/*      */     
/*      */     public FiveCrossing(CompoundTag tag) {
/* 1260 */       super(StructurePieceType.STRONGHOLD_FIVE_CROSSING, tag);
/* 1261 */       this.leftLow = tag.getBooleanOr("leftLow", false);
/* 1262 */       this.leftHigh = tag.getBooleanOr("leftHigh", false);
/* 1263 */       this.rightLow = tag.getBooleanOr("rightLow", false);
/* 1264 */       this.rightHigh = tag.getBooleanOr("rightHigh", false);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 1269 */       super.addAdditionalSaveData(context, tag);
/* 1270 */       tag.putBoolean("leftLow", this.leftLow);
/* 1271 */       tag.putBoolean("leftHigh", this.leftHigh);
/* 1272 */       tag.putBoolean("rightLow", this.rightLow);
/* 1273 */       tag.putBoolean("rightHigh", this.rightHigh);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1278 */       int zOffA = 3;
/* 1279 */       int zOffB = 5;
/*      */       
/* 1281 */       Direction orientation = getOrientation();
/* 1282 */       if (orientation == Direction.WEST || orientation == Direction.NORTH) {
/* 1283 */         zOffA = 8 - zOffA;
/* 1284 */         zOffB = 8 - zOffB;
/*      */       } 
/*      */       
/* 1287 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 5, 1);
/* 1288 */       if (this.leftLow) {
/* 1289 */         generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, zOffA, 1);
/*      */       }
/* 1291 */       if (this.leftHigh) {
/* 1292 */         generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, zOffB, 7);
/*      */       }
/* 1294 */       if (this.rightLow) {
/* 1295 */         generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, zOffA, 1);
/*      */       }
/* 1297 */       if (this.rightHigh) {
/* 1298 */         generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, zOffB, 7);
/*      */       }
/*      */     }
/*      */     
/*      */     public static FiveCrossing createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1303 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -4, -3, 0, 10, 9, 11, direction);
/*      */       
/* 1305 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/* 1306 */         return null;
/*      */       }
/*      */       
/* 1309 */       return new FiveCrossing(genDepth, random, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1315 */       generateBox(level, chunkBB, 0, 0, 0, 9, 8, 10, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/* 1317 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 4, 3, 0);
/*      */ 
/*      */       
/* 1320 */       if (this.leftLow) {
/* 1321 */         generateBox(level, chunkBB, 0, 3, 1, 0, 5, 3, CAVE_AIR, CAVE_AIR, false);
/*      */       }
/* 1323 */       if (this.rightLow) {
/* 1324 */         generateBox(level, chunkBB, 9, 3, 1, 9, 5, 3, CAVE_AIR, CAVE_AIR, false);
/*      */       }
/* 1326 */       if (this.leftHigh) {
/* 1327 */         generateBox(level, chunkBB, 0, 5, 7, 0, 7, 9, CAVE_AIR, CAVE_AIR, false);
/*      */       }
/* 1329 */       if (this.rightHigh) {
/* 1330 */         generateBox(level, chunkBB, 9, 5, 7, 9, 7, 9, CAVE_AIR, CAVE_AIR, false);
/*      */       }
/* 1332 */       generateBox(level, chunkBB, 5, 1, 10, 7, 3, 10, CAVE_AIR, CAVE_AIR, false);
/*      */ 
/*      */       
/* 1335 */       generateBox(level, chunkBB, 1, 2, 1, 8, 2, 6, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/* 1337 */       generateBox(level, chunkBB, 4, 1, 5, 4, 4, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1338 */       generateBox(level, chunkBB, 8, 1, 5, 8, 4, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/* 1340 */       generateBox(level, chunkBB, 1, 4, 7, 3, 4, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */ 
/*      */       
/* 1343 */       generateBox(level, chunkBB, 1, 3, 5, 3, 3, 6, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1344 */       generateBox(level, chunkBB, 1, 3, 4, 3, 3, 4, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1345 */       generateBox(level, chunkBB, 1, 4, 6, 3, 4, 6, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1348 */       generateBox(level, chunkBB, 5, 1, 7, 7, 1, 8, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1349 */       generateBox(level, chunkBB, 5, 1, 9, 7, 1, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1350 */       generateBox(level, chunkBB, 5, 2, 7, 7, 2, 7, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1353 */       generateBox(level, chunkBB, 4, 5, 7, 4, 5, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1354 */       generateBox(level, chunkBB, 8, 5, 7, 8, 5, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1355 */       generateBox(level, chunkBB, 5, 5, 7, 7, 5, 9, (BlockState)Blocks.SMOOTH_STONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.DOUBLE), (BlockState)Blocks.SMOOTH_STONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.DOUBLE), false);
/* 1356 */       placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.SOUTH), 6, 5, 6, chunkBB);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class PortalRoom
/*      */     extends StrongholdPiece {
/*      */     protected static final int WIDTH = 11;
/*      */     protected static final int HEIGHT = 8;
/*      */     protected static final int DEPTH = 16;
/*      */     private boolean hasPlacedSpawner;
/*      */     
/*      */     public PortalRoom(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 1368 */       super(StructurePieceType.STRONGHOLD_PORTAL_ROOM, genDepth, boundingBox);
/*      */       
/* 1370 */       setOrientation(direction);
/*      */     }
/*      */     
/*      */     public PortalRoom(CompoundTag tag) {
/* 1374 */       super(StructurePieceType.STRONGHOLD_PORTAL_ROOM, tag);
/* 1375 */       this.hasPlacedSpawner = tag.getBooleanOr("Mob", false);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 1380 */       super.addAdditionalSaveData(context, tag);
/* 1381 */       tag.putBoolean("Mob", this.hasPlacedSpawner);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1386 */       if (startPiece != null) {
/* 1387 */         ((StrongholdPieces.StartPiece)startPiece).portalRoomPiece = this;
/*      */       }
/*      */     }
/*      */     
/*      */     public static PortalRoom createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1392 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 11, 8, 16, direction);
/*      */       
/* 1394 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/* 1395 */         return null;
/*      */       }
/*      */       
/* 1398 */       return new PortalRoom(genDepth, box, direction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1404 */       generateBox(level, chunkBB, 0, 0, 0, 10, 7, 15, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */       
/* 1406 */       generateSmallDoor(level, random, chunkBB, StrongholdPieces.StrongholdPiece.SmallDoorType.GRATES, 4, 1, 0);
/*      */ 
/*      */       
/* 1409 */       int y = 6;
/* 1410 */       generateBox(level, chunkBB, 1, 6, 1, 1, 6, 14, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1411 */       generateBox(level, chunkBB, 9, 6, 1, 9, 6, 14, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1412 */       generateBox(level, chunkBB, 2, 6, 1, 8, 6, 2, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1413 */       generateBox(level, chunkBB, 2, 6, 14, 8, 6, 14, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */ 
/*      */       
/* 1416 */       generateBox(level, chunkBB, 1, 1, 1, 2, 1, 4, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1417 */       generateBox(level, chunkBB, 8, 1, 1, 9, 1, 4, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1418 */       generateBox(level, chunkBB, 1, 1, 1, 1, 1, 3, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
/* 1419 */       generateBox(level, chunkBB, 9, 1, 1, 9, 1, 3, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1422 */       generateBox(level, chunkBB, 3, 1, 8, 7, 1, 12, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1423 */       generateBox(level, chunkBB, 4, 1, 9, 6, 1, 11, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
/*      */ 
/*      */       
/* 1426 */       BlockState nsBars = (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue(IronBarsBlock.SOUTH, Boolean.valueOf(true));
/* 1427 */       BlockState weBars = (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.WEST, Boolean.valueOf(true))).setValue(IronBarsBlock.EAST, Boolean.valueOf(true));
/* 1428 */       for (int z = 3; z < 14; z += 2) {
/* 1429 */         generateBox(level, chunkBB, 0, 3, z, 0, 4, z, nsBars, nsBars, false);
/* 1430 */         generateBox(level, chunkBB, 10, 3, z, 10, 4, z, nsBars, nsBars, false);
/*      */       } 
/* 1432 */       for (int x = 2; x < 9; x += 2) {
/* 1433 */         generateBox(level, chunkBB, x, 3, 15, x, 4, 15, weBars, weBars, false);
/*      */       }
/*      */ 
/*      */       
/* 1437 */       BlockState blockState = (BlockState)Blocks.STONE_BRICK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH);
/* 1438 */       generateBox(level, chunkBB, 4, 1, 5, 6, 1, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1439 */       generateBox(level, chunkBB, 4, 2, 6, 6, 2, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1440 */       generateBox(level, chunkBB, 4, 3, 7, 6, 3, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1441 */       for (int x = 4; x <= 6; x++) {
/* 1442 */         placeBlock(level, blockState, x, 1, 4, chunkBB);
/* 1443 */         placeBlock(level, blockState, x, 2, 5, chunkBB);
/* 1444 */         placeBlock(level, blockState, x, 3, 6, chunkBB);
/*      */       } 
/*      */       
/* 1447 */       BlockState northFrame = (BlockState)Blocks.END_PORTAL_FRAME.defaultBlockState().setValue(EndPortalFrameBlock.FACING, Direction.NORTH);
/* 1448 */       BlockState southFrame = (BlockState)Blocks.END_PORTAL_FRAME.defaultBlockState().setValue(EndPortalFrameBlock.FACING, Direction.SOUTH);
/* 1449 */       BlockState eastFrame = (BlockState)Blocks.END_PORTAL_FRAME.defaultBlockState().setValue(EndPortalFrameBlock.FACING, Direction.EAST);
/* 1450 */       BlockState westFrame = (BlockState)Blocks.END_PORTAL_FRAME.defaultBlockState().setValue(EndPortalFrameBlock.FACING, Direction.WEST);
/*      */       
/* 1452 */       boolean allEyes = true;
/* 1453 */       boolean[] eyes = new boolean[12];
/* 1454 */       for (int i = 0; i < eyes.length; i++) {
/* 1455 */         eyes[i] = (random.nextFloat() > 0.9F);
/* 1456 */         allEyes &= eyes[i];
/*      */       } 
/*      */       
/* 1459 */       placeBlock(level, (BlockState)northFrame.setValue(EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[0])), 4, 3, 8, chunkBB);
/* 1460 */       placeBlock(level, (BlockState)northFrame.setValue(EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[1])), 5, 3, 8, chunkBB);
/* 1461 */       placeBlock(level, (BlockState)northFrame.setValue(EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[2])), 6, 3, 8, chunkBB);
/* 1462 */       placeBlock(level, (BlockState)southFrame.setValue(EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[3])), 4, 3, 12, chunkBB);
/* 1463 */       placeBlock(level, (BlockState)southFrame.setValue(EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[4])), 5, 3, 12, chunkBB);
/* 1464 */       placeBlock(level, (BlockState)southFrame.setValue(EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[5])), 6, 3, 12, chunkBB);
/* 1465 */       placeBlock(level, (BlockState)eastFrame.setValue(EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[6])), 3, 3, 9, chunkBB);
/* 1466 */       placeBlock(level, (BlockState)eastFrame.setValue(EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[7])), 3, 3, 10, chunkBB);
/* 1467 */       placeBlock(level, (BlockState)eastFrame.setValue(EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[8])), 3, 3, 11, chunkBB);
/* 1468 */       placeBlock(level, (BlockState)westFrame.setValue(EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[9])), 7, 3, 9, chunkBB);
/* 1469 */       placeBlock(level, (BlockState)westFrame.setValue(EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[10])), 7, 3, 10, chunkBB);
/* 1470 */       placeBlock(level, (BlockState)westFrame.setValue(EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[11])), 7, 3, 11, chunkBB);
/*      */       
/* 1472 */       if (allEyes) {
/* 1473 */         BlockState portal = Blocks.END_PORTAL.defaultBlockState();
/*      */         
/* 1475 */         placeBlock(level, portal, 4, 3, 9, chunkBB);
/* 1476 */         placeBlock(level, portal, 5, 3, 9, chunkBB);
/* 1477 */         placeBlock(level, portal, 6, 3, 9, chunkBB);
/* 1478 */         placeBlock(level, portal, 4, 3, 10, chunkBB);
/* 1479 */         placeBlock(level, portal, 5, 3, 10, chunkBB);
/* 1480 */         placeBlock(level, portal, 6, 3, 10, chunkBB);
/* 1481 */         placeBlock(level, portal, 4, 3, 11, chunkBB);
/* 1482 */         placeBlock(level, portal, 5, 3, 11, chunkBB);
/* 1483 */         placeBlock(level, portal, 6, 3, 11, chunkBB);
/*      */       } 
/*      */       
/* 1486 */       if (!this.hasPlacedSpawner) {
/* 1487 */         BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(5, 3, 6);
/* 1488 */         if (chunkBB.isInside(mutableBlockPos)) {
/* 1489 */           this.hasPlacedSpawner = true;
/* 1490 */           level.setBlock(mutableBlockPos, Blocks.SPAWNER.defaultBlockState(), 2);
/*      */           
/* 1492 */           BlockEntity blockEntity = level.getBlockEntity(mutableBlockPos);
/* 1493 */           if (blockEntity instanceof SpawnerBlockEntity) { SpawnerBlockEntity spawner = (SpawnerBlockEntity)blockEntity;
/* 1494 */             spawner.setEntityId(EntityType.SILVERFISH, random); }
/*      */         
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SmoothStoneSelector
/*      */     extends StructurePiece.BlockSelector {
/*      */     public void next(RandomSource random, int worldX, int worldY, int worldZ, boolean isEdge) {
/* 1504 */       if (isEdge) {
/* 1505 */         float selection = random.nextFloat();
/* 1506 */         if (selection < 0.2F) {
/* 1507 */           this.next = Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
/* 1508 */         } else if (selection < 0.5F) {
/* 1509 */           this.next = Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
/* 1510 */         } else if (selection < 0.55F) {
/* 1511 */           this.next = Blocks.INFESTED_STONE_BRICKS.defaultBlockState();
/*      */         } else {
/* 1513 */           this.next = Blocks.STONE_BRICKS.defaultBlockState();
/*      */         } 
/*      */       } else {
/* 1516 */         this.next = Blocks.CAVE_AIR.defaultBlockState();
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/* 1521 */   private static final SmoothStoneSelector SMOOTH_STONE_SELECTOR = new SmoothStoneSelector();
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */