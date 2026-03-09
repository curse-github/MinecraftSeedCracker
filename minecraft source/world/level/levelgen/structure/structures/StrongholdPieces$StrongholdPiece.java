/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ButtonBlock;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.block.IronBarsBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class StrongholdPiece
/*     */   extends StructurePiece
/*     */ {
/* 218 */   protected SmallDoorType entryDoor = SmallDoorType.OPENING;
/*     */ 
/*     */   
/* 221 */   protected StrongholdPiece(StructurePieceType type, int genDepth, BoundingBox boundingBox) { super(type, genDepth, boundingBox); }
/*     */ 
/*     */   
/*     */   public StrongholdPiece(StructurePieceType type, CompoundTag tag) {
/* 225 */     super(type, tag);
/* 226 */     this.entryDoor = (SmallDoorType)tag.read("EntryDoor", SmallDoorType.LEGACY_CODEC).orElseThrow();
/*     */   }
/*     */   
/*     */   protected enum SmallDoorType {
/* 230 */     OPENING, WOOD_DOOR, GRATES, IRON_DOOR; @Deprecated
/*     */     public static final Codec<SmallDoorType> LEGACY_CODEC;
/*     */     
/*     */     static  {
/* 234 */       LEGACY_CODEC = ExtraCodecs.legacyEnum(SmallDoorType::valueOf);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 239 */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) { tag.store("EntryDoor", SmallDoorType.LEGACY_CODEC, this.entryDoor); }
/*     */ 
/*     */   
/*     */   protected void generateSmallDoor(WorldGenLevel level, RandomSource random, BoundingBox chunkBB, SmallDoorType doorType, int footX, int footY, int footZ) {
/* 243 */     switch (doorType.ordinal()) {
/*     */       case 0:
/* 245 */         generateBox(level, chunkBB, footX, footY, footZ, footX + 3 - 1, footY + 3 - 1, footZ, CAVE_AIR, CAVE_AIR, false);
/*     */         break;
/*     */       case 1:
/* 248 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY, footZ, chunkBB);
/* 249 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 1, footZ, chunkBB);
/* 250 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 2, footZ, chunkBB);
/* 251 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 1, footY + 2, footZ, chunkBB);
/* 252 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 2, footZ, chunkBB);
/* 253 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 1, footZ, chunkBB);
/* 254 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY, footZ, chunkBB);
/* 255 */         placeBlock(level, Blocks.OAK_DOOR.defaultBlockState(), footX + 1, footY, footZ, chunkBB);
/* 256 */         placeBlock(level, (BlockState)Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), footX + 1, footY + 1, footZ, chunkBB);
/*     */         break;
/*     */       case 2:
/* 259 */         placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), footX + 1, footY, footZ, chunkBB);
/* 260 */         placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), footX + 1, footY + 1, footZ, chunkBB);
/* 261 */         placeBlock(level, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.WEST, Boolean.valueOf(true)), footX, footY, footZ, chunkBB);
/* 262 */         placeBlock(level, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.WEST, Boolean.valueOf(true)), footX, footY + 1, footZ, chunkBB);
/* 263 */         placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.EAST, Boolean.valueOf(true))).setValue(IronBarsBlock.WEST, Boolean.valueOf(true)), footX, footY + 2, footZ, chunkBB);
/* 264 */         placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.EAST, Boolean.valueOf(true))).setValue(IronBarsBlock.WEST, Boolean.valueOf(true)), footX + 1, footY + 2, footZ, chunkBB);
/* 265 */         placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.EAST, Boolean.valueOf(true))).setValue(IronBarsBlock.WEST, Boolean.valueOf(true)), footX + 2, footY + 2, footZ, chunkBB);
/* 266 */         placeBlock(level, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.EAST, Boolean.valueOf(true)), footX + 2, footY + 1, footZ, chunkBB);
/* 267 */         placeBlock(level, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.EAST, Boolean.valueOf(true)), footX + 2, footY, footZ, chunkBB);
/*     */         break;
/*     */       case 3:
/* 270 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY, footZ, chunkBB);
/* 271 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 1, footZ, chunkBB);
/* 272 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 2, footZ, chunkBB);
/* 273 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 1, footY + 2, footZ, chunkBB);
/* 274 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 2, footZ, chunkBB);
/* 275 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 1, footZ, chunkBB);
/* 276 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY, footZ, chunkBB);
/* 277 */         placeBlock(level, Blocks.IRON_DOOR.defaultBlockState(), footX + 1, footY, footZ, chunkBB);
/* 278 */         placeBlock(level, (BlockState)Blocks.IRON_DOOR.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), footX + 1, footY + 1, footZ, chunkBB);
/* 279 */         placeBlock(level, (BlockState)Blocks.STONE_BUTTON.defaultBlockState().setValue(ButtonBlock.FACING, Direction.NORTH), footX + 2, footY + 1, footZ + 1, chunkBB);
/* 280 */         placeBlock(level, (BlockState)Blocks.STONE_BUTTON.defaultBlockState().setValue(ButtonBlock.FACING, Direction.SOUTH), footX + 2, footY + 1, footZ - 1, chunkBB);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected SmallDoorType randomSmallDoor(RandomSource random) {
/* 286 */     int selection = random.nextInt(5);
/* 287 */     switch (selection)
/*     */     
/*     */     { 
/*     */       default:
/* 291 */         return SmallDoorType.OPENING;
/*     */       case 2:
/* 293 */         return SmallDoorType.WOOD_DOOR;
/*     */       case 3:
/* 295 */         return SmallDoorType.GRATES;
/*     */       case 4:
/* 297 */         break; }  return SmallDoorType.IRON_DOOR;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StructurePiece generateSmallDoorChildForward(StrongholdPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int xOff, int yOff) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual getOrientation : ()Lnet/minecraft/core/Direction;
/*     */     //   4: astore #6
/*     */     //   6: aload #6
/*     */     //   8: ifnull -> 220
/*     */     //   11: getstatic net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$3.$SwitchMap$net$minecraft$core$Direction : [I
/*     */     //   14: aload #6
/*     */     //   16: invokevirtual ordinal : ()I
/*     */     //   19: iaload
/*     */     //   20: tableswitch default -> 220, 1 -> 52, 2 -> 94, 3 -> 136, 4 -> 178
/*     */     //   52: aload_1
/*     */     //   53: aload_2
/*     */     //   54: aload_3
/*     */     //   55: aload_0
/*     */     //   56: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   59: invokevirtual minX : ()I
/*     */     //   62: iload #4
/*     */     //   64: iadd
/*     */     //   65: aload_0
/*     */     //   66: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   69: invokevirtual minY : ()I
/*     */     //   72: iload #5
/*     */     //   74: iadd
/*     */     //   75: aload_0
/*     */     //   76: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   79: invokevirtual minZ : ()I
/*     */     //   82: iconst_1
/*     */     //   83: isub
/*     */     //   84: aload #6
/*     */     //   86: aload_0
/*     */     //   87: invokevirtual getGenDepth : ()I
/*     */     //   90: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */     //   93: areturn
/*     */     //   94: aload_1
/*     */     //   95: aload_2
/*     */     //   96: aload_3
/*     */     //   97: aload_0
/*     */     //   98: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   101: invokevirtual minX : ()I
/*     */     //   104: iload #4
/*     */     //   106: iadd
/*     */     //   107: aload_0
/*     */     //   108: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   111: invokevirtual minY : ()I
/*     */     //   114: iload #5
/*     */     //   116: iadd
/*     */     //   117: aload_0
/*     */     //   118: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   121: invokevirtual maxZ : ()I
/*     */     //   124: iconst_1
/*     */     //   125: iadd
/*     */     //   126: aload #6
/*     */     //   128: aload_0
/*     */     //   129: invokevirtual getGenDepth : ()I
/*     */     //   132: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */     //   135: areturn
/*     */     //   136: aload_1
/*     */     //   137: aload_2
/*     */     //   138: aload_3
/*     */     //   139: aload_0
/*     */     //   140: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   143: invokevirtual minX : ()I
/*     */     //   146: iconst_1
/*     */     //   147: isub
/*     */     //   148: aload_0
/*     */     //   149: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   152: invokevirtual minY : ()I
/*     */     //   155: iload #5
/*     */     //   157: iadd
/*     */     //   158: aload_0
/*     */     //   159: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   162: invokevirtual minZ : ()I
/*     */     //   165: iload #4
/*     */     //   167: iadd
/*     */     //   168: aload #6
/*     */     //   170: aload_0
/*     */     //   171: invokevirtual getGenDepth : ()I
/*     */     //   174: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */     //   177: areturn
/*     */     //   178: aload_1
/*     */     //   179: aload_2
/*     */     //   180: aload_3
/*     */     //   181: aload_0
/*     */     //   182: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   185: invokevirtual maxX : ()I
/*     */     //   188: iconst_1
/*     */     //   189: iadd
/*     */     //   190: aload_0
/*     */     //   191: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   194: invokevirtual minY : ()I
/*     */     //   197: iload #5
/*     */     //   199: iadd
/*     */     //   200: aload_0
/*     */     //   201: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   204: invokevirtual minZ : ()I
/*     */     //   207: iload #4
/*     */     //   209: iadd
/*     */     //   210: aload #6
/*     */     //   212: aload_0
/*     */     //   213: invokevirtual getGenDepth : ()I
/*     */     //   216: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */     //   219: areturn
/*     */     //   220: aconst_null
/*     */     //   221: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #302	-> 0
/*     */     //   #303	-> 6
/*     */     //   #304	-> 11
/*     */     //   #306	-> 52
/*     */     //   #308	-> 94
/*     */     //   #310	-> 136
/*     */     //   #312	-> 178
/*     */     //   #315	-> 220
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	222	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StrongholdPiece;
/*     */     //   0	222	1	startPiece	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;
/*     */     //   0	222	2	structurePieceAccessor	Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;
/*     */     //   0	222	3	random	Lnet/minecraft/util/RandomSource;
/*     */     //   0	222	4	xOff	I
/*     */     //   0	222	5	yOff	I
/*     */     //   6	216	6	orientation	Lnet/minecraft/core/Direction; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StructurePiece generateSmallDoorChildLeft(StrongholdPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int yOff, int zOff) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual getOrientation : ()Lnet/minecraft/core/Direction;
/*     */     //   4: astore #6
/*     */     //   6: aload #6
/*     */     //   8: ifnull -> 224
/*     */     //   11: getstatic net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$3.$SwitchMap$net$minecraft$core$Direction : [I
/*     */     //   14: aload #6
/*     */     //   16: invokevirtual ordinal : ()I
/*     */     //   19: iaload
/*     */     //   20: tableswitch default -> 224, 1 -> 52, 2 -> 95, 3 -> 138, 4 -> 181
/*     */     //   52: aload_1
/*     */     //   53: aload_2
/*     */     //   54: aload_3
/*     */     //   55: aload_0
/*     */     //   56: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   59: invokevirtual minX : ()I
/*     */     //   62: iconst_1
/*     */     //   63: isub
/*     */     //   64: aload_0
/*     */     //   65: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   68: invokevirtual minY : ()I
/*     */     //   71: iload #4
/*     */     //   73: iadd
/*     */     //   74: aload_0
/*     */     //   75: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   78: invokevirtual minZ : ()I
/*     */     //   81: iload #5
/*     */     //   83: iadd
/*     */     //   84: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */     //   87: aload_0
/*     */     //   88: invokevirtual getGenDepth : ()I
/*     */     //   91: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */     //   94: areturn
/*     */     //   95: aload_1
/*     */     //   96: aload_2
/*     */     //   97: aload_3
/*     */     //   98: aload_0
/*     */     //   99: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   102: invokevirtual minX : ()I
/*     */     //   105: iconst_1
/*     */     //   106: isub
/*     */     //   107: aload_0
/*     */     //   108: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   111: invokevirtual minY : ()I
/*     */     //   114: iload #4
/*     */     //   116: iadd
/*     */     //   117: aload_0
/*     */     //   118: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   121: invokevirtual minZ : ()I
/*     */     //   124: iload #5
/*     */     //   126: iadd
/*     */     //   127: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */     //   130: aload_0
/*     */     //   131: invokevirtual getGenDepth : ()I
/*     */     //   134: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */     //   137: areturn
/*     */     //   138: aload_1
/*     */     //   139: aload_2
/*     */     //   140: aload_3
/*     */     //   141: aload_0
/*     */     //   142: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   145: invokevirtual minX : ()I
/*     */     //   148: iload #5
/*     */     //   150: iadd
/*     */     //   151: aload_0
/*     */     //   152: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   155: invokevirtual minY : ()I
/*     */     //   158: iload #4
/*     */     //   160: iadd
/*     */     //   161: aload_0
/*     */     //   162: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   165: invokevirtual minZ : ()I
/*     */     //   168: iconst_1
/*     */     //   169: isub
/*     */     //   170: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */     //   173: aload_0
/*     */     //   174: invokevirtual getGenDepth : ()I
/*     */     //   177: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */     //   180: areturn
/*     */     //   181: aload_1
/*     */     //   182: aload_2
/*     */     //   183: aload_3
/*     */     //   184: aload_0
/*     */     //   185: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   188: invokevirtual minX : ()I
/*     */     //   191: iload #5
/*     */     //   193: iadd
/*     */     //   194: aload_0
/*     */     //   195: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   198: invokevirtual minY : ()I
/*     */     //   201: iload #4
/*     */     //   203: iadd
/*     */     //   204: aload_0
/*     */     //   205: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   208: invokevirtual minZ : ()I
/*     */     //   211: iconst_1
/*     */     //   212: isub
/*     */     //   213: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */     //   216: aload_0
/*     */     //   217: invokevirtual getGenDepth : ()I
/*     */     //   220: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */     //   223: areturn
/*     */     //   224: aconst_null
/*     */     //   225: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #319	-> 0
/*     */     //   #320	-> 6
/*     */     //   #321	-> 11
/*     */     //   #323	-> 52
/*     */     //   #325	-> 95
/*     */     //   #327	-> 138
/*     */     //   #329	-> 181
/*     */     //   #332	-> 224
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	226	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StrongholdPiece;
/*     */     //   0	226	1	startPiece	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;
/*     */     //   0	226	2	structurePieceAccessor	Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;
/*     */     //   0	226	3	random	Lnet/minecraft/util/RandomSource;
/*     */     //   0	226	4	yOff	I
/*     */     //   0	226	5	zOff	I
/*     */     //   6	220	6	orientation	Lnet/minecraft/core/Direction; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StructurePiece generateSmallDoorChildRight(StrongholdPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int yOff, int zOff) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual getOrientation : ()Lnet/minecraft/core/Direction;
/*     */     //   4: astore #6
/*     */     //   6: aload #6
/*     */     //   8: ifnull -> 224
/*     */     //   11: getstatic net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$3.$SwitchMap$net$minecraft$core$Direction : [I
/*     */     //   14: aload #6
/*     */     //   16: invokevirtual ordinal : ()I
/*     */     //   19: iaload
/*     */     //   20: tableswitch default -> 224, 1 -> 52, 2 -> 95, 3 -> 138, 4 -> 181
/*     */     //   52: aload_1
/*     */     //   53: aload_2
/*     */     //   54: aload_3
/*     */     //   55: aload_0
/*     */     //   56: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   59: invokevirtual maxX : ()I
/*     */     //   62: iconst_1
/*     */     //   63: iadd
/*     */     //   64: aload_0
/*     */     //   65: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   68: invokevirtual minY : ()I
/*     */     //   71: iload #4
/*     */     //   73: iadd
/*     */     //   74: aload_0
/*     */     //   75: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   78: invokevirtual minZ : ()I
/*     */     //   81: iload #5
/*     */     //   83: iadd
/*     */     //   84: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */     //   87: aload_0
/*     */     //   88: invokevirtual getGenDepth : ()I
/*     */     //   91: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */     //   94: areturn
/*     */     //   95: aload_1
/*     */     //   96: aload_2
/*     */     //   97: aload_3
/*     */     //   98: aload_0
/*     */     //   99: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   102: invokevirtual maxX : ()I
/*     */     //   105: iconst_1
/*     */     //   106: iadd
/*     */     //   107: aload_0
/*     */     //   108: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   111: invokevirtual minY : ()I
/*     */     //   114: iload #4
/*     */     //   116: iadd
/*     */     //   117: aload_0
/*     */     //   118: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   121: invokevirtual minZ : ()I
/*     */     //   124: iload #5
/*     */     //   126: iadd
/*     */     //   127: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */     //   130: aload_0
/*     */     //   131: invokevirtual getGenDepth : ()I
/*     */     //   134: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */     //   137: areturn
/*     */     //   138: aload_1
/*     */     //   139: aload_2
/*     */     //   140: aload_3
/*     */     //   141: aload_0
/*     */     //   142: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   145: invokevirtual minX : ()I
/*     */     //   148: iload #5
/*     */     //   150: iadd
/*     */     //   151: aload_0
/*     */     //   152: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   155: invokevirtual minY : ()I
/*     */     //   158: iload #4
/*     */     //   160: iadd
/*     */     //   161: aload_0
/*     */     //   162: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   165: invokevirtual maxZ : ()I
/*     */     //   168: iconst_1
/*     */     //   169: iadd
/*     */     //   170: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */     //   173: aload_0
/*     */     //   174: invokevirtual getGenDepth : ()I
/*     */     //   177: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */     //   180: areturn
/*     */     //   181: aload_1
/*     */     //   182: aload_2
/*     */     //   183: aload_3
/*     */     //   184: aload_0
/*     */     //   185: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   188: invokevirtual minX : ()I
/*     */     //   191: iload #5
/*     */     //   193: iadd
/*     */     //   194: aload_0
/*     */     //   195: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   198: invokevirtual minY : ()I
/*     */     //   201: iload #4
/*     */     //   203: iadd
/*     */     //   204: aload_0
/*     */     //   205: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */     //   208: invokevirtual maxZ : ()I
/*     */     //   211: iconst_1
/*     */     //   212: iadd
/*     */     //   213: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */     //   216: aload_0
/*     */     //   217: invokevirtual getGenDepth : ()I
/*     */     //   220: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */     //   223: areturn
/*     */     //   224: aconst_null
/*     */     //   225: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #336	-> 0
/*     */     //   #337	-> 6
/*     */     //   #338	-> 11
/*     */     //   #340	-> 52
/*     */     //   #342	-> 95
/*     */     //   #344	-> 138
/*     */     //   #346	-> 181
/*     */     //   #349	-> 224
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	226	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StrongholdPiece;
/*     */     //   0	226	1	startPiece	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;
/*     */     //   0	226	2	structurePieceAccessor	Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;
/*     */     //   0	226	3	random	Lnet/minecraft/util/RandomSource;
/*     */     //   0	226	4	yOff	I
/*     */     //   0	226	5	zOff	I
/*     */     //   6	220	6	orientation	Lnet/minecraft/core/Direction; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 353 */   protected static boolean isOkBox(BoundingBox box) { return (box.minY() > 10); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$StrongholdPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */