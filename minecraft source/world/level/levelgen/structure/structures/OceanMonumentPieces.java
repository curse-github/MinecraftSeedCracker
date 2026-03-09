/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.Lists;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.monster.ElderGuardian;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.StructureManager;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ 
/*      */ public class OceanMonumentPieces
/*      */ {
/*      */   protected static abstract class OceanMonumentPiece
/*      */     extends StructurePiece
/*      */   {
/*   34 */     protected static final BlockState BASE_GRAY = Blocks.PRISMARINE.defaultBlockState();
/*   35 */     protected static final BlockState BASE_LIGHT = Blocks.PRISMARINE_BRICKS.defaultBlockState();
/*   36 */     protected static final BlockState BASE_BLACK = Blocks.DARK_PRISMARINE.defaultBlockState();
/*      */     
/*   38 */     protected static final BlockState DOT_DECO_DATA = BASE_LIGHT;
/*      */     
/*   40 */     protected static final BlockState LAMP_BLOCK = Blocks.SEA_LANTERN.defaultBlockState();
/*      */     
/*      */     protected static final boolean DO_FILL = true;
/*   43 */     protected static final BlockState FILL_BLOCK = Blocks.WATER.defaultBlockState();
/*   44 */     protected static final Set<Block> FILL_KEEP = ImmutableSet.builder()
/*   45 */       .add(Blocks.ICE)
/*   46 */       .add(Blocks.PACKED_ICE)
/*   47 */       .add(Blocks.BLUE_ICE)
/*   48 */       .add(FILL_BLOCK.getBlock())
/*   49 */       .build();
/*      */     
/*      */     protected static final int GRIDROOM_WIDTH = 8;
/*      */     
/*      */     protected static final int GRIDROOM_DEPTH = 8;
/*      */     protected static final int GRIDROOM_HEIGHT = 4;
/*      */     protected static final int GRID_WIDTH = 5;
/*      */     protected static final int GRID_DEPTH = 5;
/*      */     protected static final int GRID_HEIGHT = 3;
/*      */     protected static final int GRID_FLOOR_COUNT = 25;
/*      */     protected static final int GRID_SIZE = 75;
/*   60 */     protected static final int GRIDROOM_SOURCE_INDEX = getRoomIndex(2, 0, 0);
/*   61 */     protected static final int GRIDROOM_TOP_CONNECT_INDEX = getRoomIndex(2, 2, 0);
/*   62 */     protected static final int GRIDROOM_LEFTWING_CONNECT_INDEX = getRoomIndex(0, 1, 0);
/*   63 */     protected static final int GRIDROOM_RIGHTWING_CONNECT_INDEX = getRoomIndex(4, 1, 0);
/*      */     
/*      */     protected static final int LEFTWING_INDEX = 1001;
/*      */     
/*      */     protected static final int RIGHTWING_INDEX = 1002;
/*      */     
/*      */     protected static final int PENTHOUSE_INDEX = 1003;
/*      */     protected OceanMonumentPieces.RoomDefinition roomDefinition;
/*      */     
/*   72 */     protected static int getRoomIndex(int roomX, int roomY, int roomZ) { return roomY * 25 + roomZ * 5 + roomX; }
/*      */ 
/*      */     
/*      */     public OceanMonumentPiece(StructurePieceType type, Direction orientation, int genDepth, BoundingBox boundingBox) {
/*   76 */       super(type, genDepth, boundingBox);
/*   77 */       setOrientation(orientation);
/*      */     }
/*      */     
/*      */     protected OceanMonumentPiece(StructurePieceType type, int genDepth, Direction orientation, OceanMonumentPieces.RoomDefinition roomDefinition, int roomWidth, int roomHeight, int roomDepth) {
/*   81 */       super(type, genDepth, makeBoundingBox(orientation, roomDefinition, roomWidth, roomHeight, roomDepth));
/*      */       
/*   83 */       setOrientation(orientation);
/*   84 */       this.roomDefinition = roomDefinition;
/*      */     }
/*      */     
/*      */     private static BoundingBox makeBoundingBox(Direction orientation, OceanMonumentPieces.RoomDefinition roomDefinition, int roomWidth, int roomHeight, int roomDepth) {
/*   88 */       int roomIndex = roomDefinition.index;
/*   89 */       int roomX = roomIndex % 5;
/*   90 */       int roomZ = roomIndex / 5 % 5;
/*   91 */       int roomY = roomIndex / 25;
/*      */ 
/*      */ 
/*      */       
/*   95 */       BoundingBox boundingBox = makeBoundingBox(0, 0, 0, orientation, roomWidth * 8, roomHeight * 4, roomDepth * 8);
/*      */       
/*   97 */       switch (OceanMonumentPieces.null.$SwitchMap$net$minecraft$core$Direction[orientation.ordinal()])
/*      */       { case 1:
/*   99 */           boundingBox.move(roomX * 8, roomY * 4, -(roomZ + roomDepth) * 8 + 1);
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
/*      */           
/*  114 */           return boundingBox;case 2: boundingBox.move(roomX * 8, roomY * 4, roomZ * 8); return boundingBox;case 3: boundingBox.move(-(roomZ + roomDepth) * 8 + 1, roomY * 4, roomX * 8); return boundingBox; }  boundingBox.move(roomZ * 8, roomY * 4, roomX * 8); return boundingBox;
/*      */     }
/*      */ 
/*      */     
/*  118 */     public OceanMonumentPiece(StructurePieceType type, CompoundTag tag) { super(type, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {}
/*      */ 
/*      */     
/*      */     protected void generateWaterBox(WorldGenLevel level, BoundingBox chunkBB, int x0, int y0, int z0, int x1, int y1, int z1) {
/*  126 */       for (int y = y0; y <= y1; y++) {
/*  127 */         for (int x = x0; x <= x1; x++) {
/*  128 */           for (int z = z0; z <= z1; z++) {
/*  129 */             BlockState block = getBlock(level, x, y, z, chunkBB);
/*  130 */             if (!FILL_KEEP.contains(block.getBlock())) {
/*  131 */               if (getWorldY(y) >= level.getSeaLevel() && block != FILL_BLOCK) {
/*  132 */                 placeBlock(level, Blocks.AIR.defaultBlockState(), x, y, z, chunkBB);
/*      */               } else {
/*  134 */                 placeBlock(level, FILL_BLOCK, x, y, z, chunkBB);
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     protected void generateDefaultFloor(WorldGenLevel level, BoundingBox chunkBB, int xOff, int zOff, boolean downOpening) {
/*  143 */       if (downOpening) {
/*  144 */         generateBox(level, chunkBB, xOff + 0, 0, zOff + 0, xOff + 2, 0, zOff + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/*  145 */         generateBox(level, chunkBB, xOff + 5, 0, zOff + 0, xOff + 8 - 1, 0, zOff + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/*  146 */         generateBox(level, chunkBB, xOff + 3, 0, zOff + 0, xOff + 4, 0, zOff + 2, BASE_GRAY, BASE_GRAY, false);
/*  147 */         generateBox(level, chunkBB, xOff + 3, 0, zOff + 5, xOff + 4, 0, zOff + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  149 */         generateBox(level, chunkBB, xOff + 3, 0, zOff + 2, xOff + 4, 0, zOff + 2, BASE_LIGHT, BASE_LIGHT, false);
/*  150 */         generateBox(level, chunkBB, xOff + 3, 0, zOff + 5, xOff + 4, 0, zOff + 5, BASE_LIGHT, BASE_LIGHT, false);
/*  151 */         generateBox(level, chunkBB, xOff + 2, 0, zOff + 3, xOff + 2, 0, zOff + 4, BASE_LIGHT, BASE_LIGHT, false);
/*  152 */         generateBox(level, chunkBB, xOff + 5, 0, zOff + 3, xOff + 5, 0, zOff + 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } else {
/*  154 */         generateBox(level, chunkBB, xOff + 0, 0, zOff + 0, xOff + 8 - 1, 0, zOff + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */     }
/*      */     
/*      */     protected void generateBoxOnFillOnly(WorldGenLevel level, BoundingBox chunkBB, int x0, int y0, int z0, int x1, int y1, int z1, BlockState targetBlock) {
/*  159 */       for (int y = y0; y <= y1; y++) {
/*  160 */         for (int x = x0; x <= x1; x++) {
/*  161 */           for (int z = z0; z <= z1; z++) {
/*  162 */             if (getBlock(level, x, y, z, chunkBB) == FILL_BLOCK)
/*      */             {
/*      */               
/*  165 */               placeBlock(level, targetBlock, x, y, z, chunkBB); } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     protected boolean chunkIntersects(BoundingBox chunkBB, int x0, int z0, int x1, int z1) {
/*  172 */       int wx0 = getWorldX(x0, z0);
/*  173 */       int wz0 = getWorldZ(x0, z0);
/*  174 */       int wx1 = getWorldX(x1, z1);
/*  175 */       int wz1 = getWorldZ(x1, z1);
/*  176 */       return chunkBB.intersects(Math.min(wx0, wx1), Math.min(wz0, wz1), Math.max(wx0, wx1), Math.max(wz0, wz1));
/*      */     }
/*      */     
/*      */     protected void spawnElder(WorldGenLevel level, BoundingBox chunkBB, int x, int y, int z) {
/*  180 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(x, y, z);
/*  181 */       if (chunkBB.isInside(mutableBlockPos)) {
/*  182 */         ElderGuardian elder = (ElderGuardian)EntityType.ELDER_GUARDIAN.create(level.getLevel(), EntitySpawnReason.STRUCTURE);
/*  183 */         if (elder != null) {
/*  184 */           elder.heal(elder.getMaxHealth());
/*  185 */           elder.snapTo(mutableBlockPos.getX() + 0.5D, mutableBlockPos.getY(), mutableBlockPos.getZ() + 0.5D, 0.0F, 0.0F);
/*  186 */           elder.finalizeSpawn(level, level.getCurrentDifficultyAt(elder.blockPosition()), EntitySpawnReason.STRUCTURE, null);
/*  187 */           level.addFreshEntityWithPassengers(elder);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class MonumentBuilding
/*      */     extends OceanMonumentPiece
/*      */   {
/*      */     private static final int WIDTH = 58;
/*      */     
/*      */     private static final int HEIGHT = 22;
/*      */     
/*      */     private static final int DEPTH = 58;
/*      */     
/*      */     public static final int BIOME_RANGE_CHECK = 29;
/*      */     
/*      */     private static final int TOP_POSITION = 61;
/*      */     
/*      */     private OceanMonumentPieces.RoomDefinition sourceRoom;
/*      */     private OceanMonumentPieces.RoomDefinition coreRoom;
/*  209 */     private final List<OceanMonumentPieces.OceanMonumentPiece> childPieces = Lists.newArrayList();
/*      */     
/*      */     public MonumentBuilding(RandomSource random, int west, int north, Direction direction) {
/*  212 */       super(StructurePieceType.OCEAN_MONUMENT_BUILDING, direction, 0, makeBoundingBox(west, 39, north, direction, 58, 23, 58));
/*      */       
/*  214 */       setOrientation(direction);
/*      */       
/*  216 */       List<OceanMonumentPieces.RoomDefinition> roomDefinitions = generateRoomGraph(random);
/*      */       
/*  218 */       this.sourceRoom.claimed = true;
/*  219 */       this.childPieces.add(new OceanMonumentPieces.OceanMonumentEntryRoom(direction, this.sourceRoom));
/*  220 */       this.childPieces.add(new OceanMonumentPieces.OceanMonumentCoreRoom(direction, this.coreRoom));
/*      */       
/*  222 */       List<OceanMonumentPieces.MonumentRoomFitter> fitters = Lists.newArrayList();
/*  223 */       fitters.add(new OceanMonumentPieces.FitDoubleXYRoom());
/*  224 */       fitters.add(new OceanMonumentPieces.FitDoubleYZRoom());
/*  225 */       fitters.add(new OceanMonumentPieces.FitDoubleZRoom());
/*  226 */       fitters.add(new OceanMonumentPieces.FitDoubleXRoom());
/*  227 */       fitters.add(new OceanMonumentPieces.FitDoubleYRoom());
/*  228 */       fitters.add(new OceanMonumentPieces.FitSimpleTopRoom());
/*  229 */       fitters.add(new OceanMonumentPieces.FitSimpleRoom());
/*      */       
/*  231 */       for (OceanMonumentPieces.RoomDefinition definition : roomDefinitions) {
/*  232 */         if (!definition.claimed && !definition.isSpecial())
/*      */         {
/*  234 */           for (OceanMonumentPieces.MonumentRoomFitter fitter : fitters) {
/*  235 */             if (fitter.fits(definition)) {
/*  236 */               this.childPieces.add(fitter.create(direction, definition, random));
/*      */             }
/*      */           } 
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  244 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(9, 0, 22);
/*  245 */       for (OceanMonumentPieces.OceanMonumentPiece child : this.childPieces) {
/*  246 */         child.getBoundingBox().move(mutableBlockPos);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  251 */       BoundingBox leftWing = BoundingBox.fromCorners(getWorldPos(1, 1, 1), getWorldPos(23, 8, 21));
/*  252 */       BoundingBox rightWing = BoundingBox.fromCorners(getWorldPos(34, 1, 1), getWorldPos(56, 8, 21));
/*  253 */       BoundingBox penthouse = BoundingBox.fromCorners(getWorldPos(22, 13, 22), getWorldPos(35, 17, 35));
/*      */ 
/*      */       
/*  256 */       int wingRandom = random.nextInt();
/*  257 */       this.childPieces.add(new OceanMonumentPieces.OceanMonumentWingRoom(direction, leftWing, wingRandom++));
/*  258 */       this.childPieces.add(new OceanMonumentPieces.OceanMonumentWingRoom(direction, rightWing, wingRandom++));
/*      */       
/*  260 */       this.childPieces.add(new OceanMonumentPieces.OceanMonumentPenthouse(direction, penthouse));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  265 */     public MonumentBuilding(CompoundTag tag) { super(StructurePieceType.OCEAN_MONUMENT_BUILDING, tag); }
/*      */ 
/*      */     
/*      */     private List<OceanMonumentPieces.RoomDefinition> generateRoomGraph(RandomSource random) {
/*  269 */       OceanMonumentPieces.RoomDefinition[] arrayOfRoomDefinition = new OceanMonumentPieces.RoomDefinition[75];
/*      */       
/*  271 */       for (int x = 0; x < 5; x++) {
/*  272 */         for (int z = 0; z < 4; z++) {
/*  273 */           int y = 0;
/*  274 */           int pos = getRoomIndex(x, 0, z);
/*  275 */           arrayOfRoomDefinition[pos] = new OceanMonumentPieces.RoomDefinition(pos);
/*      */         } 
/*      */       } 
/*  278 */       for (int x = 0; x < 5; x++) {
/*  279 */         for (int z = 0; z < 4; z++) {
/*  280 */           int y = 1;
/*  281 */           int pos = getRoomIndex(x, 1, z);
/*  282 */           arrayOfRoomDefinition[pos] = new OceanMonumentPieces.RoomDefinition(pos);
/*      */         } 
/*      */       } 
/*  285 */       for (int x = 1; x < 4; x++) {
/*  286 */         for (int z = 0; z < 2; z++) {
/*  287 */           int y = 2;
/*  288 */           int pos = getRoomIndex(x, 2, z);
/*  289 */           arrayOfRoomDefinition[pos] = new OceanMonumentPieces.RoomDefinition(pos);
/*      */         } 
/*      */       } 
/*      */       
/*  293 */       this.sourceRoom = arrayOfRoomDefinition[GRIDROOM_SOURCE_INDEX];
/*      */       
/*  295 */       for (int x = 0; x < 5; x++) {
/*  296 */         for (int z = 0; z < 5; z++) {
/*  297 */           for (int y = 0; y < 3; y++) {
/*  298 */             int pos = getRoomIndex(x, y, z);
/*  299 */             if (arrayOfRoomDefinition[pos] != null)
/*      */             {
/*      */               
/*  302 */               for (Direction direction : Direction.values()) {
/*  303 */                 int neighX = x + direction.getStepX();
/*  304 */                 int neighY = y + direction.getStepY();
/*  305 */                 int neighZ = z + direction.getStepZ();
/*  306 */                 if (neighX >= 0 && neighX < 5 && neighZ >= 0 && neighZ < 5 && neighY >= 0 && neighY < 3) {
/*  307 */                   int neighPos = getRoomIndex(neighX, neighY, neighZ);
/*  308 */                   if (arrayOfRoomDefinition[neighPos] != null)
/*      */                   {
/*      */                     
/*  311 */                     if (neighZ == z) {
/*  312 */                       arrayOfRoomDefinition[pos].setConnection(direction, arrayOfRoomDefinition[neighPos]);
/*      */                     } else {
/*  314 */                       arrayOfRoomDefinition[pos].setConnection(direction.getOpposite(), arrayOfRoomDefinition[neighPos]);
/*      */                     }  } 
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*  322 */       OceanMonumentPieces.RoomDefinition roofRoom = new OceanMonumentPieces.RoomDefinition(1003);
/*  323 */       OceanMonumentPieces.RoomDefinition leftWing = new OceanMonumentPieces.RoomDefinition(1001);
/*  324 */       OceanMonumentPieces.RoomDefinition rightWing = new OceanMonumentPieces.RoomDefinition(1002);
/*  325 */       arrayOfRoomDefinition[GRIDROOM_TOP_CONNECT_INDEX].setConnection(Direction.UP, roofRoom);
/*  326 */       arrayOfRoomDefinition[GRIDROOM_LEFTWING_CONNECT_INDEX].setConnection(Direction.SOUTH, leftWing);
/*  327 */       arrayOfRoomDefinition[GRIDROOM_RIGHTWING_CONNECT_INDEX].setConnection(Direction.SOUTH, rightWing);
/*  328 */       roofRoom.claimed = true;
/*  329 */       leftWing.claimed = true;
/*  330 */       rightWing.claimed = true;
/*  331 */       this.sourceRoom.isSource = true;
/*      */ 
/*      */       
/*  334 */       this.coreRoom = arrayOfRoomDefinition[getRoomIndex(random.nextInt(4), 0, 2)];
/*  335 */       this.coreRoom.claimed = true;
/*  336 */       (this.coreRoom.connections[Direction.EAST.get3DDataValue()]).claimed = true;
/*  337 */       (this.coreRoom.connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/*  338 */       ((this.coreRoom.connections[Direction.EAST.get3DDataValue()]).connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/*  339 */       (this.coreRoom.connections[Direction.UP.get3DDataValue()]).claimed = true;
/*  340 */       ((this.coreRoom.connections[Direction.EAST.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/*  341 */       ((this.coreRoom.connections[Direction.NORTH.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/*  342 */       (((this.coreRoom.connections[Direction.EAST.get3DDataValue()]).connections[Direction.NORTH.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/*      */       
/*  344 */       ObjectArrayList<OceanMonumentPieces.RoomDefinition> roomDefs = new ObjectArrayList<OceanMonumentPieces.RoomDefinition>();
/*  345 */       for (OceanMonumentPieces.RoomDefinition definition : arrayOfRoomDefinition) {
/*  346 */         if (definition != null) {
/*  347 */           definition.updateOpenings();
/*  348 */           roomDefs.add(definition);
/*      */         } 
/*      */       } 
/*  351 */       roofRoom.updateOpenings();
/*      */       
/*  353 */       Util.shuffle(roomDefs, random);
/*  354 */       int scanIndex = 1;
/*  355 */       for (ObjectListIterator objectListIterator = roomDefs.iterator(); objectListIterator.hasNext(); ) { OceanMonumentPieces.RoomDefinition definition = (OceanMonumentPieces.RoomDefinition)objectListIterator.next();
/*      */         
/*  357 */         int closeCount = 0;
/*  358 */         int attemptCount = 0;
/*  359 */         while (closeCount < 2 && attemptCount < 5) {
/*  360 */           attemptCount++;
/*      */           
/*  362 */           int f = random.nextInt(6);
/*  363 */           if (definition.hasOpening[f]) {
/*  364 */             int of = Direction.from3DDataValue(f).getOpposite().get3DDataValue();
/*      */ 
/*      */             
/*  367 */             definition.hasOpening[f] = false;
/*  368 */             (definition.connections[f]).hasOpening[of] = false;
/*      */             
/*  370 */             if (definition.findSource(scanIndex++) && definition.connections[f].findSource(scanIndex++)) {
/*  371 */               closeCount++;
/*      */               continue;
/*      */             } 
/*  374 */             definition.hasOpening[f] = true;
/*  375 */             (definition.connections[f]).hasOpening[of] = true;
/*      */           } 
/*      */         }  }
/*      */ 
/*      */       
/*  380 */       roomDefs.add(roofRoom);
/*  381 */       roomDefs.add(leftWing);
/*  382 */       roomDefs.add(rightWing);
/*      */       
/*  384 */       return roomDefs;
/*      */     }
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  389 */       int waterHeight = Math.max(level.getSeaLevel(), 64) - this.boundingBox.minY();
/*      */       
/*  391 */       generateWaterBox(level, chunkBB, 0, 0, 0, 58, waterHeight, 58);
/*      */ 
/*      */       
/*  394 */       generateWing(false, 0, level, random, chunkBB);
/*      */ 
/*      */       
/*  397 */       generateWing(true, 33, level, random, chunkBB);
/*      */ 
/*      */       
/*  400 */       generateEntranceArchs(level, random, chunkBB);
/*      */       
/*  402 */       generateEntranceWall(level, random, chunkBB);
/*  403 */       generateRoofPiece(level, random, chunkBB);
/*      */       
/*  405 */       generateLowerWall(level, random, chunkBB);
/*  406 */       generateMiddleWall(level, random, chunkBB);
/*  407 */       generateUpperWall(level, random, chunkBB);
/*      */ 
/*      */       
/*  410 */       for (int pillarX = 0; pillarX < 7; pillarX++) {
/*  411 */         for (int pillarZ = 0; pillarZ < 7; ) {
/*  412 */           if (pillarZ == 0 && pillarX == 3)
/*      */           {
/*  414 */             pillarZ = 6;
/*      */           }
/*      */           
/*  417 */           int bx = pillarX * 9;
/*  418 */           int bz = pillarZ * 9;
/*  419 */           for (int w = 0; w < 4; w++) {
/*  420 */             for (int d = 0; d < 4; d++) {
/*  421 */               placeBlock(level, BASE_LIGHT, bx + w, 0, bz + d, chunkBB);
/*  422 */               fillColumnDown(level, BASE_LIGHT, bx + w, -1, bz + d, chunkBB);
/*      */             } 
/*      */           } 
/*      */           
/*  426 */           if (pillarX == 0 || pillarX == 6) {
/*  427 */             pillarZ++; continue;
/*      */           } 
/*  429 */           pillarZ += 6;
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  435 */       for (int i = 0; i < 5; i++) {
/*  436 */         generateWaterBox(level, chunkBB, -1 - i, 0 + i * 2, -1 - i, -1 - i, 23, 58 + i);
/*  437 */         generateWaterBox(level, chunkBB, 58 + i, 0 + i * 2, -1 - i, 58 + i, 23, 58 + i);
/*  438 */         generateWaterBox(level, chunkBB, 0 - i, 0 + i * 2, -1 - i, 57 + i, 23, -1 - i);
/*  439 */         generateWaterBox(level, chunkBB, 0 - i, 0 + i * 2, 58 + i, 57 + i, 23, 58 + i);
/*      */       } 
/*      */       
/*  442 */       for (OceanMonumentPieces.OceanMonumentPiece child : this.childPieces) {
/*  443 */         if (child.getBoundingBox().intersects(chunkBB)) {
/*  444 */           child.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private void generateWing(boolean isFlipped, int xoff, WorldGenLevel level, RandomSource random, BoundingBox chunkBB) {
/*  451 */       int sectionWidth = 24;
/*  452 */       if (chunkIntersects(chunkBB, xoff, 0, xoff + 23, 20)) {
/*  453 */         generateBox(level, chunkBB, xoff + 0, 0, 0, xoff + 24, 0, 20, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  455 */         generateWaterBox(level, chunkBB, xoff + 0, 1, 0, xoff + 24, 10, 20);
/*      */         
/*  457 */         for (int i = 0; i < 4; i++) {
/*  458 */           generateBox(level, chunkBB, xoff + i, i + 1, i, xoff + i, i + 1, 20, BASE_LIGHT, BASE_LIGHT, false);
/*  459 */           generateBox(level, chunkBB, xoff + i + 7, i + 5, i + 7, xoff + i + 7, i + 5, 20, BASE_LIGHT, BASE_LIGHT, false);
/*  460 */           generateBox(level, chunkBB, xoff + 17 - i, i + 5, i + 7, xoff + 17 - i, i + 5, 20, BASE_LIGHT, BASE_LIGHT, false);
/*  461 */           generateBox(level, chunkBB, xoff + 24 - i, i + 1, i, xoff + 24 - i, i + 1, 20, BASE_LIGHT, BASE_LIGHT, false);
/*      */           
/*  463 */           generateBox(level, chunkBB, xoff + i + 1, i + 1, i, xoff + 23 - i, i + 1, i, BASE_LIGHT, BASE_LIGHT, false);
/*  464 */           generateBox(level, chunkBB, xoff + i + 8, i + 5, i + 7, xoff + 16 - i, i + 5, i + 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  466 */         generateBox(level, chunkBB, xoff + 4, 4, 4, xoff + 6, 4, 20, BASE_GRAY, BASE_GRAY, false);
/*  467 */         generateBox(level, chunkBB, xoff + 7, 4, 4, xoff + 17, 4, 6, BASE_GRAY, BASE_GRAY, false);
/*  468 */         generateBox(level, chunkBB, xoff + 18, 4, 4, xoff + 20, 4, 20, BASE_GRAY, BASE_GRAY, false);
/*  469 */         generateBox(level, chunkBB, xoff + 11, 8, 11, xoff + 13, 8, 20, BASE_GRAY, BASE_GRAY, false);
/*  470 */         placeBlock(level, DOT_DECO_DATA, xoff + 12, 9, 12, chunkBB);
/*  471 */         placeBlock(level, DOT_DECO_DATA, xoff + 12, 9, 15, chunkBB);
/*  472 */         placeBlock(level, DOT_DECO_DATA, xoff + 12, 9, 18, chunkBB);
/*      */         
/*  474 */         int leftPos = xoff + (isFlipped ? 19 : 5);
/*  475 */         int rightPos = xoff + (isFlipped ? 5 : 19);
/*  476 */         for (int z = 20; z >= 5; z -= 3) {
/*  477 */           placeBlock(level, DOT_DECO_DATA, leftPos, 5, z, chunkBB);
/*      */         }
/*  479 */         for (int z = 19; z >= 7; z -= 3) {
/*  480 */           placeBlock(level, DOT_DECO_DATA, rightPos, 5, z, chunkBB);
/*      */         }
/*  482 */         for (int i = 0; i < 4; i++) {
/*  483 */           int pos = isFlipped ? (xoff + 24 - 17 - i * 3) : (xoff + 17 - i * 3);
/*  484 */           placeBlock(level, DOT_DECO_DATA, pos, 5, 5, chunkBB);
/*      */         } 
/*  486 */         placeBlock(level, DOT_DECO_DATA, rightPos, 5, 5, chunkBB);
/*      */ 
/*      */         
/*  489 */         generateBox(level, chunkBB, xoff + 11, 1, 12, xoff + 13, 7, 12, BASE_GRAY, BASE_GRAY, false);
/*  490 */         generateBox(level, chunkBB, xoff + 12, 1, 11, xoff + 12, 7, 13, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private void generateEntranceArchs(WorldGenLevel level, RandomSource random, BoundingBox chunkBB) {
/*  496 */       if (chunkIntersects(chunkBB, 22, 5, 35, 17)) {
/*      */         
/*  498 */         generateWaterBox(level, chunkBB, 25, 0, 0, 32, 8, 20);
/*      */ 
/*      */         
/*  501 */         for (int i = 0; i < 4; i++) {
/*  502 */           generateBox(level, chunkBB, 24, 2, 5 + i * 4, 24, 4, 5 + i * 4, BASE_LIGHT, BASE_LIGHT, false);
/*  503 */           generateBox(level, chunkBB, 22, 4, 5 + i * 4, 23, 4, 5 + i * 4, BASE_LIGHT, BASE_LIGHT, false);
/*  504 */           placeBlock(level, BASE_LIGHT, 25, 5, 5 + i * 4, chunkBB);
/*  505 */           placeBlock(level, BASE_LIGHT, 26, 6, 5 + i * 4, chunkBB);
/*  506 */           placeBlock(level, LAMP_BLOCK, 26, 5, 5 + i * 4, chunkBB);
/*      */           
/*  508 */           generateBox(level, chunkBB, 33, 2, 5 + i * 4, 33, 4, 5 + i * 4, BASE_LIGHT, BASE_LIGHT, false);
/*  509 */           generateBox(level, chunkBB, 34, 4, 5 + i * 4, 35, 4, 5 + i * 4, BASE_LIGHT, BASE_LIGHT, false);
/*  510 */           placeBlock(level, BASE_LIGHT, 32, 5, 5 + i * 4, chunkBB);
/*  511 */           placeBlock(level, BASE_LIGHT, 31, 6, 5 + i * 4, chunkBB);
/*  512 */           placeBlock(level, LAMP_BLOCK, 31, 5, 5 + i * 4, chunkBB);
/*      */           
/*  514 */           generateBox(level, chunkBB, 27, 6, 5 + i * 4, 30, 6, 5 + i * 4, BASE_GRAY, BASE_GRAY, false);
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void generateEntranceWall(WorldGenLevel level, RandomSource random, BoundingBox chunkBB) {
/*  522 */       if (chunkIntersects(chunkBB, 15, 20, 42, 21)) {
/*  523 */         generateBox(level, chunkBB, 15, 0, 21, 42, 0, 21, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  525 */         generateWaterBox(level, chunkBB, 26, 1, 21, 31, 3, 21);
/*      */ 
/*      */ 
/*      */         
/*  529 */         generateBox(level, chunkBB, 21, 12, 21, 36, 12, 21, BASE_GRAY, BASE_GRAY, false);
/*  530 */         generateBox(level, chunkBB, 17, 11, 21, 40, 11, 21, BASE_GRAY, BASE_GRAY, false);
/*  531 */         generateBox(level, chunkBB, 16, 10, 21, 41, 10, 21, BASE_GRAY, BASE_GRAY, false);
/*  532 */         generateBox(level, chunkBB, 15, 7, 21, 42, 9, 21, BASE_GRAY, BASE_GRAY, false);
/*  533 */         generateBox(level, chunkBB, 16, 6, 21, 41, 6, 21, BASE_GRAY, BASE_GRAY, false);
/*  534 */         generateBox(level, chunkBB, 17, 5, 21, 40, 5, 21, BASE_GRAY, BASE_GRAY, false);
/*  535 */         generateBox(level, chunkBB, 21, 4, 21, 36, 4, 21, BASE_GRAY, BASE_GRAY, false);
/*  536 */         generateBox(level, chunkBB, 22, 3, 21, 26, 3, 21, BASE_GRAY, BASE_GRAY, false);
/*  537 */         generateBox(level, chunkBB, 31, 3, 21, 35, 3, 21, BASE_GRAY, BASE_GRAY, false);
/*  538 */         generateBox(level, chunkBB, 23, 2, 21, 25, 2, 21, BASE_GRAY, BASE_GRAY, false);
/*  539 */         generateBox(level, chunkBB, 32, 2, 21, 34, 2, 21, BASE_GRAY, BASE_GRAY, false);
/*      */ 
/*      */         
/*  542 */         generateBox(level, chunkBB, 28, 4, 20, 29, 4, 21, BASE_LIGHT, BASE_LIGHT, false);
/*  543 */         placeBlock(level, BASE_LIGHT, 27, 3, 21, chunkBB);
/*  544 */         placeBlock(level, BASE_LIGHT, 30, 3, 21, chunkBB);
/*  545 */         placeBlock(level, BASE_LIGHT, 26, 2, 21, chunkBB);
/*  546 */         placeBlock(level, BASE_LIGHT, 31, 2, 21, chunkBB);
/*  547 */         placeBlock(level, BASE_LIGHT, 25, 1, 21, chunkBB);
/*  548 */         placeBlock(level, BASE_LIGHT, 32, 1, 21, chunkBB);
/*  549 */         for (int i = 0; i < 7; i++) {
/*  550 */           placeBlock(level, BASE_BLACK, 28 - i, 6 + i, 21, chunkBB);
/*  551 */           placeBlock(level, BASE_BLACK, 29 + i, 6 + i, 21, chunkBB);
/*      */         } 
/*  553 */         for (int i = 0; i < 4; i++) {
/*  554 */           placeBlock(level, BASE_BLACK, 28 - i, 9 + i, 21, chunkBB);
/*  555 */           placeBlock(level, BASE_BLACK, 29 + i, 9 + i, 21, chunkBB);
/*      */         } 
/*  557 */         placeBlock(level, BASE_BLACK, 28, 12, 21, chunkBB);
/*  558 */         placeBlock(level, BASE_BLACK, 29, 12, 21, chunkBB);
/*  559 */         for (int i = 0; i < 3; i++) {
/*  560 */           placeBlock(level, BASE_BLACK, 22 - i * 2, 8, 21, chunkBB);
/*  561 */           placeBlock(level, BASE_BLACK, 22 - i * 2, 9, 21, chunkBB);
/*      */           
/*  563 */           placeBlock(level, BASE_BLACK, 35 + i * 2, 8, 21, chunkBB);
/*  564 */           placeBlock(level, BASE_BLACK, 35 + i * 2, 9, 21, chunkBB);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  569 */         generateWaterBox(level, chunkBB, 15, 13, 21, 42, 15, 21);
/*  570 */         generateWaterBox(level, chunkBB, 15, 1, 21, 15, 6, 21);
/*  571 */         generateWaterBox(level, chunkBB, 16, 1, 21, 16, 5, 21);
/*  572 */         generateWaterBox(level, chunkBB, 17, 1, 21, 20, 4, 21);
/*  573 */         generateWaterBox(level, chunkBB, 21, 1, 21, 21, 3, 21);
/*  574 */         generateWaterBox(level, chunkBB, 22, 1, 21, 22, 2, 21);
/*  575 */         generateWaterBox(level, chunkBB, 23, 1, 21, 24, 1, 21);
/*  576 */         generateWaterBox(level, chunkBB, 42, 1, 21, 42, 6, 21);
/*  577 */         generateWaterBox(level, chunkBB, 41, 1, 21, 41, 5, 21);
/*  578 */         generateWaterBox(level, chunkBB, 37, 1, 21, 40, 4, 21);
/*  579 */         generateWaterBox(level, chunkBB, 36, 1, 21, 36, 3, 21);
/*  580 */         generateWaterBox(level, chunkBB, 33, 1, 21, 34, 1, 21);
/*  581 */         generateWaterBox(level, chunkBB, 35, 1, 21, 35, 2, 21);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void generateRoofPiece(WorldGenLevel level, RandomSource random, BoundingBox chunkBB) {
/*  589 */       if (chunkIntersects(chunkBB, 21, 21, 36, 36)) {
/*  590 */         generateBox(level, chunkBB, 21, 0, 22, 36, 0, 36, BASE_GRAY, BASE_GRAY, false);
/*      */ 
/*      */ 
/*      */         
/*  594 */         generateWaterBox(level, chunkBB, 21, 1, 22, 36, 23, 36);
/*      */ 
/*      */         
/*  597 */         for (int i = 0; i < 4; i++) {
/*  598 */           generateBox(level, chunkBB, 21 + i, 13 + i, 21 + i, 36 - i, 13 + i, 21 + i, BASE_LIGHT, BASE_LIGHT, false);
/*  599 */           generateBox(level, chunkBB, 21 + i, 13 + i, 36 - i, 36 - i, 13 + i, 36 - i, BASE_LIGHT, BASE_LIGHT, false);
/*  600 */           generateBox(level, chunkBB, 21 + i, 13 + i, 22 + i, 21 + i, 13 + i, 35 - i, BASE_LIGHT, BASE_LIGHT, false);
/*  601 */           generateBox(level, chunkBB, 36 - i, 13 + i, 22 + i, 36 - i, 13 + i, 35 - i, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  603 */         generateBox(level, chunkBB, 25, 16, 25, 32, 16, 32, BASE_GRAY, BASE_GRAY, false);
/*  604 */         generateBox(level, chunkBB, 25, 17, 25, 25, 19, 25, BASE_LIGHT, BASE_LIGHT, false);
/*  605 */         generateBox(level, chunkBB, 32, 17, 25, 32, 19, 25, BASE_LIGHT, BASE_LIGHT, false);
/*  606 */         generateBox(level, chunkBB, 25, 17, 32, 25, 19, 32, BASE_LIGHT, BASE_LIGHT, false);
/*  607 */         generateBox(level, chunkBB, 32, 17, 32, 32, 19, 32, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/*  609 */         placeBlock(level, BASE_LIGHT, 26, 20, 26, chunkBB);
/*  610 */         placeBlock(level, BASE_LIGHT, 27, 21, 27, chunkBB);
/*  611 */         placeBlock(level, LAMP_BLOCK, 27, 20, 27, chunkBB);
/*  612 */         placeBlock(level, BASE_LIGHT, 26, 20, 31, chunkBB);
/*  613 */         placeBlock(level, BASE_LIGHT, 27, 21, 30, chunkBB);
/*  614 */         placeBlock(level, LAMP_BLOCK, 27, 20, 30, chunkBB);
/*  615 */         placeBlock(level, BASE_LIGHT, 31, 20, 31, chunkBB);
/*  616 */         placeBlock(level, BASE_LIGHT, 30, 21, 30, chunkBB);
/*  617 */         placeBlock(level, LAMP_BLOCK, 30, 20, 30, chunkBB);
/*  618 */         placeBlock(level, BASE_LIGHT, 31, 20, 26, chunkBB);
/*  619 */         placeBlock(level, BASE_LIGHT, 30, 21, 27, chunkBB);
/*  620 */         placeBlock(level, LAMP_BLOCK, 30, 20, 27, chunkBB);
/*      */         
/*  622 */         generateBox(level, chunkBB, 28, 21, 27, 29, 21, 27, BASE_GRAY, BASE_GRAY, false);
/*  623 */         generateBox(level, chunkBB, 27, 21, 28, 27, 21, 29, BASE_GRAY, BASE_GRAY, false);
/*  624 */         generateBox(level, chunkBB, 28, 21, 30, 29, 21, 30, BASE_GRAY, BASE_GRAY, false);
/*  625 */         generateBox(level, chunkBB, 30, 21, 28, 30, 21, 29, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void generateLowerWall(WorldGenLevel level, RandomSource random, BoundingBox chunkBB) {
/*  632 */       if (chunkIntersects(chunkBB, 0, 21, 6, 58)) {
/*  633 */         generateBox(level, chunkBB, 0, 0, 21, 6, 0, 57, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  635 */         generateWaterBox(level, chunkBB, 0, 1, 21, 6, 7, 57);
/*      */ 
/*      */         
/*  638 */         generateBox(level, chunkBB, 4, 4, 21, 6, 4, 53, BASE_GRAY, BASE_GRAY, false);
/*  639 */         for (int i = 0; i < 4; i++) {
/*  640 */           generateBox(level, chunkBB, i, i + 1, 21, i, i + 1, 57 - i, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  642 */         for (int z = 23; z < 53; z += 3) {
/*  643 */           placeBlock(level, DOT_DECO_DATA, 5, 5, z, chunkBB);
/*      */         }
/*  645 */         placeBlock(level, DOT_DECO_DATA, 5, 5, 52, chunkBB);
/*      */         
/*  647 */         for (int i = 0; i < 4; i++) {
/*  648 */           generateBox(level, chunkBB, i, i + 1, 21, i, i + 1, 57 - i, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*      */         
/*  651 */         generateBox(level, chunkBB, 4, 1, 52, 6, 3, 52, BASE_GRAY, BASE_GRAY, false);
/*  652 */         generateBox(level, chunkBB, 5, 1, 51, 5, 3, 53, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  657 */       if (chunkIntersects(chunkBB, 51, 21, 58, 58)) {
/*  658 */         generateBox(level, chunkBB, 51, 0, 21, 57, 0, 57, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  660 */         generateWaterBox(level, chunkBB, 51, 1, 21, 57, 7, 57);
/*      */ 
/*      */         
/*  663 */         generateBox(level, chunkBB, 51, 4, 21, 53, 4, 53, BASE_GRAY, BASE_GRAY, false);
/*  664 */         for (int i = 0; i < 4; i++) {
/*  665 */           generateBox(level, chunkBB, 57 - i, i + 1, 21, 57 - i, i + 1, 57 - i, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  667 */         for (int z = 23; z < 53; z += 3) {
/*  668 */           placeBlock(level, DOT_DECO_DATA, 52, 5, z, chunkBB);
/*      */         }
/*  670 */         placeBlock(level, DOT_DECO_DATA, 52, 5, 52, chunkBB);
/*      */ 
/*      */         
/*  673 */         generateBox(level, chunkBB, 51, 1, 52, 53, 3, 52, BASE_GRAY, BASE_GRAY, false);
/*  674 */         generateBox(level, chunkBB, 52, 1, 51, 52, 3, 53, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  679 */       if (chunkIntersects(chunkBB, 0, 51, 57, 57)) {
/*  680 */         generateBox(level, chunkBB, 7, 0, 51, 50, 0, 57, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  682 */         generateWaterBox(level, chunkBB, 7, 1, 51, 50, 10, 57);
/*      */ 
/*      */         
/*  685 */         for (int i = 0; i < 4; i++) {
/*  686 */           generateBox(level, chunkBB, i + 1, i + 1, 57 - i, 56 - i, i + 1, 57 - i, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void generateMiddleWall(WorldGenLevel level, RandomSource random, BoundingBox chunkBB) {
/*  694 */       if (chunkIntersects(chunkBB, 7, 21, 13, 50)) {
/*  695 */         generateBox(level, chunkBB, 7, 0, 21, 13, 0, 50, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  697 */         generateWaterBox(level, chunkBB, 7, 1, 21, 13, 10, 50);
/*      */ 
/*      */         
/*  700 */         generateBox(level, chunkBB, 11, 8, 21, 13, 8, 53, BASE_GRAY, BASE_GRAY, false);
/*  701 */         for (int i = 0; i < 4; i++) {
/*  702 */           generateBox(level, chunkBB, i + 7, i + 5, 21, i + 7, i + 5, 54, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  704 */         for (int z = 21; z <= 45; z += 3) {
/*  705 */           placeBlock(level, DOT_DECO_DATA, 12, 9, z, chunkBB);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  711 */       if (chunkIntersects(chunkBB, 44, 21, 50, 54)) {
/*  712 */         generateBox(level, chunkBB, 44, 0, 21, 50, 0, 50, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  714 */         generateWaterBox(level, chunkBB, 44, 1, 21, 50, 10, 50);
/*      */ 
/*      */         
/*  717 */         generateBox(level, chunkBB, 44, 8, 21, 46, 8, 53, BASE_GRAY, BASE_GRAY, false);
/*  718 */         for (int i = 0; i < 4; i++) {
/*  719 */           generateBox(level, chunkBB, 50 - i, i + 5, 21, 50 - i, i + 5, 54, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  721 */         for (int z = 21; z <= 45; z += 3) {
/*  722 */           placeBlock(level, DOT_DECO_DATA, 45, 9, z, chunkBB);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  728 */       if (chunkIntersects(chunkBB, 8, 44, 49, 54)) {
/*  729 */         generateBox(level, chunkBB, 14, 0, 44, 43, 0, 50, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  731 */         generateWaterBox(level, chunkBB, 14, 1, 44, 43, 10, 50);
/*      */ 
/*      */         
/*  734 */         for (int x = 12; x <= 45; x += 3) {
/*  735 */           placeBlock(level, DOT_DECO_DATA, x, 9, 45, chunkBB);
/*  736 */           placeBlock(level, DOT_DECO_DATA, x, 9, 52, chunkBB);
/*  737 */           if (x == 12 || x == 18 || x == 24 || x == 33 || x == 39 || x == 45) {
/*  738 */             placeBlock(level, DOT_DECO_DATA, x, 9, 47, chunkBB);
/*  739 */             placeBlock(level, DOT_DECO_DATA, x, 9, 50, chunkBB);
/*  740 */             placeBlock(level, DOT_DECO_DATA, x, 10, 45, chunkBB);
/*  741 */             placeBlock(level, DOT_DECO_DATA, x, 10, 46, chunkBB);
/*  742 */             placeBlock(level, DOT_DECO_DATA, x, 10, 51, chunkBB);
/*  743 */             placeBlock(level, DOT_DECO_DATA, x, 10, 52, chunkBB);
/*  744 */             placeBlock(level, DOT_DECO_DATA, x, 11, 47, chunkBB);
/*  745 */             placeBlock(level, DOT_DECO_DATA, x, 11, 50, chunkBB);
/*  746 */             placeBlock(level, DOT_DECO_DATA, x, 12, 48, chunkBB);
/*  747 */             placeBlock(level, DOT_DECO_DATA, x, 12, 49, chunkBB);
/*      */           } 
/*      */         } 
/*      */         
/*  751 */         for (int i = 0; i < 3; i++) {
/*  752 */           generateBox(level, chunkBB, 8 + i, 5 + i, 54, 49 - i, 5 + i, 54, BASE_GRAY, BASE_GRAY, false);
/*      */         }
/*  754 */         generateBox(level, chunkBB, 11, 8, 54, 46, 8, 54, BASE_LIGHT, BASE_LIGHT, false);
/*  755 */         generateBox(level, chunkBB, 14, 8, 44, 43, 8, 53, BASE_GRAY, BASE_GRAY, false);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void generateUpperWall(WorldGenLevel level, RandomSource random, BoundingBox chunkBB) {
/*  762 */       if (chunkIntersects(chunkBB, 14, 21, 20, 43)) {
/*  763 */         generateBox(level, chunkBB, 14, 0, 21, 20, 0, 43, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  765 */         generateWaterBox(level, chunkBB, 14, 1, 22, 20, 14, 43);
/*      */ 
/*      */         
/*  768 */         generateBox(level, chunkBB, 18, 12, 22, 20, 12, 39, BASE_GRAY, BASE_GRAY, false);
/*  769 */         generateBox(level, chunkBB, 18, 12, 21, 20, 12, 21, BASE_LIGHT, BASE_LIGHT, false);
/*  770 */         for (int i = 0; i < 4; i++) {
/*  771 */           generateBox(level, chunkBB, i + 14, i + 9, 21, i + 14, i + 9, 43 - i, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  773 */         for (int z = 23; z <= 39; z += 3) {
/*  774 */           placeBlock(level, DOT_DECO_DATA, 19, 13, z, chunkBB);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  780 */       if (chunkIntersects(chunkBB, 37, 21, 43, 43)) {
/*  781 */         generateBox(level, chunkBB, 37, 0, 21, 43, 0, 43, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  783 */         generateWaterBox(level, chunkBB, 37, 1, 22, 43, 14, 43);
/*      */ 
/*      */         
/*  786 */         generateBox(level, chunkBB, 37, 12, 22, 39, 12, 39, BASE_GRAY, BASE_GRAY, false);
/*  787 */         generateBox(level, chunkBB, 37, 12, 21, 39, 12, 21, BASE_LIGHT, BASE_LIGHT, false);
/*  788 */         for (int i = 0; i < 4; i++) {
/*  789 */           generateBox(level, chunkBB, 43 - i, i + 9, 21, 43 - i, i + 9, 43 - i, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  791 */         for (int z = 23; z <= 39; z += 3) {
/*  792 */           placeBlock(level, DOT_DECO_DATA, 38, 13, z, chunkBB);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  798 */       if (chunkIntersects(chunkBB, 15, 37, 42, 43)) {
/*  799 */         generateBox(level, chunkBB, 21, 0, 37, 36, 0, 43, BASE_GRAY, BASE_GRAY, false);
/*      */         
/*  801 */         generateWaterBox(level, chunkBB, 21, 1, 37, 36, 14, 43);
/*      */ 
/*      */         
/*  804 */         generateBox(level, chunkBB, 21, 12, 37, 36, 12, 39, BASE_GRAY, BASE_GRAY, false);
/*  805 */         for (int i = 0; i < 4; i++) {
/*  806 */           generateBox(level, chunkBB, 15 + i, i + 9, 43 - i, 42 - i, i + 9, 43 - i, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/*  808 */         for (int x = 21; x <= 36; x += 3)
/*  809 */           placeBlock(level, DOT_DECO_DATA, x, 13, 38, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentEntryRoom
/*      */     extends OceanMonumentPiece
/*      */   {
/*  817 */     public OceanMonumentEntryRoom(Direction orientation, OceanMonumentPieces.RoomDefinition definition) { super(StructurePieceType.OCEAN_MONUMENT_ENTRY_ROOM, 1, orientation, definition, 1, 1, 1); }
/*      */ 
/*      */ 
/*      */     
/*  821 */     public OceanMonumentEntryRoom(CompoundTag tag) { super(StructurePieceType.OCEAN_MONUMENT_ENTRY_ROOM, tag); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  827 */       generateBox(level, chunkBB, 0, 3, 0, 2, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  828 */       generateBox(level, chunkBB, 5, 3, 0, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  829 */       generateBox(level, chunkBB, 0, 2, 0, 1, 2, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  830 */       generateBox(level, chunkBB, 6, 2, 0, 7, 2, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  831 */       generateBox(level, chunkBB, 0, 1, 0, 0, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  832 */       generateBox(level, chunkBB, 7, 1, 0, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/*  835 */       generateBox(level, chunkBB, 0, 1, 7, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/*  838 */       generateBox(level, chunkBB, 1, 1, 0, 2, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  839 */       generateBox(level, chunkBB, 5, 1, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/*  841 */       if (this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/*  842 */         generateWaterBox(level, chunkBB, 3, 1, 7, 4, 2, 7);
/*      */       }
/*  844 */       if (this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/*  845 */         generateWaterBox(level, chunkBB, 0, 1, 3, 1, 2, 4);
/*      */       }
/*  847 */       if (this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()])
/*  848 */         generateWaterBox(level, chunkBB, 6, 1, 3, 7, 2, 4); 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentSimpleRoom
/*      */     extends OceanMonumentPiece {
/*      */     private int mainDesign;
/*      */     
/*      */     public OceanMonumentSimpleRoom(Direction orientation, OceanMonumentPieces.RoomDefinition definition, RandomSource random) {
/*  857 */       super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_ROOM, 1, orientation, definition, 1, 1, 1);
/*  858 */       this.mainDesign = random.nextInt(3);
/*      */     }
/*      */ 
/*      */     
/*  862 */     public OceanMonumentSimpleRoom(CompoundTag tag) { super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_ROOM, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  867 */       if (this.roomDefinition.index / 25 > 0) {
/*  868 */         generateDefaultFloor(level, chunkBB, 0, 0, this.roomDefinition.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       }
/*  870 */       if (this.roomDefinition.connections[Direction.UP.get3DDataValue()] == null) {
/*  871 */         generateBoxOnFillOnly(level, chunkBB, 1, 4, 1, 6, 4, 6, BASE_GRAY);
/*      */       }
/*      */       
/*  874 */       boolean centerPillar = (this.mainDesign != 0 && random.nextBoolean() && !this.roomDefinition.hasOpening[Direction.DOWN.get3DDataValue()] && !this.roomDefinition.hasOpening[Direction.UP.get3DDataValue()] && this.roomDefinition.countOpenings() > 1);
/*      */       
/*  876 */       if (this.mainDesign == 0) {
/*      */         
/*  878 */         generateBox(level, chunkBB, 0, 1, 0, 2, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  879 */         generateBox(level, chunkBB, 0, 3, 0, 2, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  880 */         generateBox(level, chunkBB, 0, 2, 0, 0, 2, 2, BASE_GRAY, BASE_GRAY, false);
/*  881 */         generateBox(level, chunkBB, 1, 2, 0, 2, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  882 */         placeBlock(level, LAMP_BLOCK, 1, 2, 1, chunkBB);
/*      */ 
/*      */         
/*  885 */         generateBox(level, chunkBB, 5, 1, 0, 7, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  886 */         generateBox(level, chunkBB, 5, 3, 0, 7, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  887 */         generateBox(level, chunkBB, 7, 2, 0, 7, 2, 2, BASE_GRAY, BASE_GRAY, false);
/*  888 */         generateBox(level, chunkBB, 5, 2, 0, 6, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  889 */         placeBlock(level, LAMP_BLOCK, 6, 2, 1, chunkBB);
/*      */ 
/*      */         
/*  892 */         generateBox(level, chunkBB, 0, 1, 5, 2, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  893 */         generateBox(level, chunkBB, 0, 3, 5, 2, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  894 */         generateBox(level, chunkBB, 0, 2, 5, 0, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  895 */         generateBox(level, chunkBB, 1, 2, 7, 2, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  896 */         placeBlock(level, LAMP_BLOCK, 1, 2, 6, chunkBB);
/*      */ 
/*      */         
/*  899 */         generateBox(level, chunkBB, 5, 1, 5, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  900 */         generateBox(level, chunkBB, 5, 3, 5, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  901 */         generateBox(level, chunkBB, 7, 2, 5, 7, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  902 */         generateBox(level, chunkBB, 5, 2, 7, 6, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  903 */         placeBlock(level, LAMP_BLOCK, 6, 2, 6, chunkBB);
/*      */         
/*  905 */         if (this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/*  906 */           generateBox(level, chunkBB, 3, 3, 0, 4, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/*  908 */           generateBox(level, chunkBB, 3, 3, 0, 4, 3, 1, BASE_LIGHT, BASE_LIGHT, false);
/*  909 */           generateBox(level, chunkBB, 3, 2, 0, 4, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  910 */           generateBox(level, chunkBB, 3, 1, 0, 4, 1, 1, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  912 */         if (this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/*  913 */           generateBox(level, chunkBB, 3, 3, 7, 4, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/*  915 */           generateBox(level, chunkBB, 3, 3, 6, 4, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  916 */           generateBox(level, chunkBB, 3, 2, 7, 4, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  917 */           generateBox(level, chunkBB, 3, 1, 6, 4, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  919 */         if (this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/*  920 */           generateBox(level, chunkBB, 0, 3, 3, 0, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/*  922 */           generateBox(level, chunkBB, 0, 3, 3, 1, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*  923 */           generateBox(level, chunkBB, 0, 2, 3, 0, 2, 4, BASE_GRAY, BASE_GRAY, false);
/*  924 */           generateBox(level, chunkBB, 0, 1, 3, 1, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  926 */         if (this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()]) {
/*  927 */           generateBox(level, chunkBB, 7, 3, 3, 7, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/*  929 */           generateBox(level, chunkBB, 6, 3, 3, 7, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*  930 */           generateBox(level, chunkBB, 7, 2, 3, 7, 2, 4, BASE_GRAY, BASE_GRAY, false);
/*  931 */           generateBox(level, chunkBB, 6, 1, 3, 7, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  933 */       } else if (this.mainDesign == 1) {
/*      */         
/*  935 */         generateBox(level, chunkBB, 2, 1, 2, 2, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  936 */         generateBox(level, chunkBB, 2, 1, 5, 2, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
/*  937 */         generateBox(level, chunkBB, 5, 1, 5, 5, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
/*  938 */         generateBox(level, chunkBB, 5, 1, 2, 5, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/*  939 */         placeBlock(level, LAMP_BLOCK, 2, 2, 2, chunkBB);
/*  940 */         placeBlock(level, LAMP_BLOCK, 2, 2, 5, chunkBB);
/*  941 */         placeBlock(level, LAMP_BLOCK, 5, 2, 5, chunkBB);
/*  942 */         placeBlock(level, LAMP_BLOCK, 5, 2, 2, chunkBB);
/*      */ 
/*      */         
/*  945 */         generateBox(level, chunkBB, 0, 1, 0, 1, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  946 */         generateBox(level, chunkBB, 0, 1, 1, 0, 3, 1, BASE_LIGHT, BASE_LIGHT, false);
/*  947 */         generateBox(level, chunkBB, 0, 1, 7, 1, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  948 */         generateBox(level, chunkBB, 0, 1, 6, 0, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  949 */         generateBox(level, chunkBB, 6, 1, 7, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  950 */         generateBox(level, chunkBB, 7, 1, 6, 7, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  951 */         generateBox(level, chunkBB, 6, 1, 0, 7, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  952 */         generateBox(level, chunkBB, 7, 1, 1, 7, 3, 1, BASE_LIGHT, BASE_LIGHT, false);
/*  953 */         placeBlock(level, BASE_GRAY, 1, 2, 0, chunkBB);
/*  954 */         placeBlock(level, BASE_GRAY, 0, 2, 1, chunkBB);
/*  955 */         placeBlock(level, BASE_GRAY, 1, 2, 7, chunkBB);
/*  956 */         placeBlock(level, BASE_GRAY, 0, 2, 6, chunkBB);
/*  957 */         placeBlock(level, BASE_GRAY, 6, 2, 7, chunkBB);
/*  958 */         placeBlock(level, BASE_GRAY, 7, 2, 6, chunkBB);
/*  959 */         placeBlock(level, BASE_GRAY, 6, 2, 0, chunkBB);
/*  960 */         placeBlock(level, BASE_GRAY, 7, 2, 1, chunkBB);
/*  961 */         if (!this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/*  962 */           generateBox(level, chunkBB, 1, 3, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  963 */           generateBox(level, chunkBB, 1, 2, 0, 6, 2, 0, BASE_GRAY, BASE_GRAY, false);
/*  964 */           generateBox(level, chunkBB, 1, 1, 0, 6, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  966 */         if (!this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/*  967 */           generateBox(level, chunkBB, 1, 3, 7, 6, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  968 */           generateBox(level, chunkBB, 1, 2, 7, 6, 2, 7, BASE_GRAY, BASE_GRAY, false);
/*  969 */           generateBox(level, chunkBB, 1, 1, 7, 6, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  971 */         if (!this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/*  972 */           generateBox(level, chunkBB, 0, 3, 1, 0, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  973 */           generateBox(level, chunkBB, 0, 2, 1, 0, 2, 6, BASE_GRAY, BASE_GRAY, false);
/*  974 */           generateBox(level, chunkBB, 0, 1, 1, 0, 1, 6, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  976 */         if (!this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()]) {
/*  977 */           generateBox(level, chunkBB, 7, 3, 1, 7, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/*  978 */           generateBox(level, chunkBB, 7, 2, 1, 7, 2, 6, BASE_GRAY, BASE_GRAY, false);
/*  979 */           generateBox(level, chunkBB, 7, 1, 1, 7, 1, 6, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } 
/*  981 */       } else if (this.mainDesign == 2) {
/*  982 */         generateBox(level, chunkBB, 0, 1, 0, 0, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  983 */         generateBox(level, chunkBB, 7, 1, 0, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  984 */         generateBox(level, chunkBB, 1, 1, 0, 6, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  985 */         generateBox(level, chunkBB, 1, 1, 7, 6, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/*  987 */         generateBox(level, chunkBB, 0, 2, 0, 0, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*  988 */         generateBox(level, chunkBB, 7, 2, 0, 7, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*  989 */         generateBox(level, chunkBB, 1, 2, 0, 6, 2, 0, BASE_BLACK, BASE_BLACK, false);
/*  990 */         generateBox(level, chunkBB, 1, 2, 7, 6, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*      */         
/*  992 */         generateBox(level, chunkBB, 0, 3, 0, 0, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  993 */         generateBox(level, chunkBB, 7, 3, 0, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*  994 */         generateBox(level, chunkBB, 1, 3, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/*  995 */         generateBox(level, chunkBB, 1, 3, 7, 6, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/*  997 */         generateBox(level, chunkBB, 0, 1, 3, 0, 2, 4, BASE_BLACK, BASE_BLACK, false);
/*  998 */         generateBox(level, chunkBB, 7, 1, 3, 7, 2, 4, BASE_BLACK, BASE_BLACK, false);
/*  999 */         generateBox(level, chunkBB, 3, 1, 0, 4, 2, 0, BASE_BLACK, BASE_BLACK, false);
/* 1000 */         generateBox(level, chunkBB, 3, 1, 7, 4, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*      */         
/* 1002 */         if (this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1003 */           generateWaterBox(level, chunkBB, 3, 1, 0, 4, 2, 0);
/*      */         }
/* 1005 */         if (this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1006 */           generateWaterBox(level, chunkBB, 3, 1, 7, 4, 2, 7);
/*      */         }
/* 1008 */         if (this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1009 */           generateWaterBox(level, chunkBB, 0, 1, 3, 0, 2, 4);
/*      */         }
/* 1011 */         if (this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1012 */           generateWaterBox(level, chunkBB, 7, 1, 3, 7, 2, 4);
/*      */         }
/*      */       } 
/* 1015 */       if (centerPillar) {
/* 1016 */         generateBox(level, chunkBB, 3, 1, 3, 4, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1017 */         generateBox(level, chunkBB, 3, 2, 3, 4, 2, 4, BASE_GRAY, BASE_GRAY, false);
/* 1018 */         generateBox(level, chunkBB, 3, 3, 3, 4, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentSimpleTopRoom
/*      */     extends OceanMonumentPiece {
/* 1025 */     public OceanMonumentSimpleTopRoom(Direction orientation, OceanMonumentPieces.RoomDefinition definition) { super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_TOP_ROOM, 1, orientation, definition, 1, 1, 1); }
/*      */ 
/*      */ 
/*      */     
/* 1029 */     public OceanMonumentSimpleTopRoom(CompoundTag tag) { super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_TOP_ROOM, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1034 */       if (this.roomDefinition.index / 25 > 0) {
/* 1035 */         generateDefaultFloor(level, chunkBB, 0, 0, this.roomDefinition.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       }
/* 1037 */       if (this.roomDefinition.connections[Direction.UP.get3DDataValue()] == null) {
/* 1038 */         generateBoxOnFillOnly(level, chunkBB, 1, 4, 1, 6, 4, 6, BASE_GRAY);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1043 */       for (int x = 1; x <= 6; x++) {
/* 1044 */         for (int z = 1; z <= 6; z++) {
/* 1045 */           if (random.nextInt(3) != 0) {
/* 1046 */             int y0 = 2 + ((random.nextInt(4) == 0) ? 0 : 1);
/* 1047 */             BlockState wetSponge = Blocks.WET_SPONGE.defaultBlockState();
/* 1048 */             generateBox(level, chunkBB, x, y0, z, x, 3, z, wetSponge, wetSponge, false);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1053 */       generateBox(level, chunkBB, 0, 1, 0, 0, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1054 */       generateBox(level, chunkBB, 7, 1, 0, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1055 */       generateBox(level, chunkBB, 1, 1, 0, 6, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1056 */       generateBox(level, chunkBB, 1, 1, 7, 6, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1058 */       generateBox(level, chunkBB, 0, 2, 0, 0, 2, 7, BASE_BLACK, BASE_BLACK, false);
/* 1059 */       generateBox(level, chunkBB, 7, 2, 0, 7, 2, 7, BASE_BLACK, BASE_BLACK, false);
/* 1060 */       generateBox(level, chunkBB, 1, 2, 0, 6, 2, 0, BASE_BLACK, BASE_BLACK, false);
/* 1061 */       generateBox(level, chunkBB, 1, 2, 7, 6, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*      */       
/* 1063 */       generateBox(level, chunkBB, 0, 3, 0, 0, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1064 */       generateBox(level, chunkBB, 7, 3, 0, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1065 */       generateBox(level, chunkBB, 1, 3, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1066 */       generateBox(level, chunkBB, 1, 3, 7, 6, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1068 */       generateBox(level, chunkBB, 0, 1, 3, 0, 2, 4, BASE_BLACK, BASE_BLACK, false);
/* 1069 */       generateBox(level, chunkBB, 7, 1, 3, 7, 2, 4, BASE_BLACK, BASE_BLACK, false);
/* 1070 */       generateBox(level, chunkBB, 3, 1, 0, 4, 2, 0, BASE_BLACK, BASE_BLACK, false);
/* 1071 */       generateBox(level, chunkBB, 3, 1, 7, 4, 2, 7, BASE_BLACK, BASE_BLACK, false);
/*      */       
/* 1073 */       if (this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1074 */         generateWaterBox(level, chunkBB, 3, 1, 0, 4, 2, 0);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentDoubleYRoom
/*      */     extends OceanMonumentPiece
/*      */   {
/* 1082 */     public OceanMonumentDoubleYRoom(Direction orientation, OceanMonumentPieces.RoomDefinition definition) { super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Y_ROOM, 1, orientation, definition, 1, 2, 1); }
/*      */ 
/*      */ 
/*      */     
/* 1086 */     public OceanMonumentDoubleYRoom(CompoundTag tag) { super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Y_ROOM, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1091 */       if (this.roomDefinition.index / 25 > 0) {
/* 1092 */         generateDefaultFloor(level, chunkBB, 0, 0, this.roomDefinition.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       }
/* 1094 */       OceanMonumentPieces.RoomDefinition above = this.roomDefinition.connections[Direction.UP.get3DDataValue()];
/* 1095 */       if (above.connections[Direction.UP.get3DDataValue()] == null) {
/* 1096 */         generateBoxOnFillOnly(level, chunkBB, 1, 8, 1, 6, 8, 6, BASE_GRAY);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1101 */       generateBox(level, chunkBB, 0, 4, 0, 0, 4, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1102 */       generateBox(level, chunkBB, 7, 4, 0, 7, 4, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1103 */       generateBox(level, chunkBB, 1, 4, 0, 6, 4, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1104 */       generateBox(level, chunkBB, 1, 4, 7, 6, 4, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1106 */       generateBox(level, chunkBB, 2, 4, 1, 2, 4, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1107 */       generateBox(level, chunkBB, 1, 4, 2, 1, 4, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1108 */       generateBox(level, chunkBB, 5, 4, 1, 5, 4, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1109 */       generateBox(level, chunkBB, 6, 4, 2, 6, 4, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1110 */       generateBox(level, chunkBB, 2, 4, 5, 2, 4, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1111 */       generateBox(level, chunkBB, 1, 4, 5, 1, 4, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1112 */       generateBox(level, chunkBB, 5, 4, 5, 5, 4, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1113 */       generateBox(level, chunkBB, 6, 4, 5, 6, 4, 5, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1115 */       OceanMonumentPieces.RoomDefinition definition = this.roomDefinition;
/* 1116 */       for (int y = 1; y <= 5; y += 4) {
/* 1117 */         int z = 0;
/* 1118 */         if (definition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1119 */           generateBox(level, chunkBB, 2, y, z, 2, y + 2, z, BASE_LIGHT, BASE_LIGHT, false);
/* 1120 */           generateBox(level, chunkBB, 5, y, z, 5, y + 2, z, BASE_LIGHT, BASE_LIGHT, false);
/* 1121 */           generateBox(level, chunkBB, 3, y + 2, z, 4, y + 2, z, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/* 1123 */           generateBox(level, chunkBB, 0, y, z, 7, y + 2, z, BASE_LIGHT, BASE_LIGHT, false);
/* 1124 */           generateBox(level, chunkBB, 0, y + 1, z, 7, y + 1, z, BASE_GRAY, BASE_GRAY, false);
/*      */         } 
/* 1126 */         z = 7;
/* 1127 */         if (definition.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1128 */           generateBox(level, chunkBB, 2, y, z, 2, y + 2, z, BASE_LIGHT, BASE_LIGHT, false);
/* 1129 */           generateBox(level, chunkBB, 5, y, z, 5, y + 2, z, BASE_LIGHT, BASE_LIGHT, false);
/* 1130 */           generateBox(level, chunkBB, 3, y + 2, z, 4, y + 2, z, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/* 1132 */           generateBox(level, chunkBB, 0, y, z, 7, y + 2, z, BASE_LIGHT, BASE_LIGHT, false);
/* 1133 */           generateBox(level, chunkBB, 0, y + 1, z, 7, y + 1, z, BASE_GRAY, BASE_GRAY, false);
/*      */         } 
/* 1135 */         int x = 0;
/* 1136 */         if (definition.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1137 */           generateBox(level, chunkBB, x, y, 2, x, y + 2, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1138 */           generateBox(level, chunkBB, x, y, 5, x, y + 2, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1139 */           generateBox(level, chunkBB, x, y + 2, 3, x, y + 2, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/* 1141 */           generateBox(level, chunkBB, x, y, 0, x, y + 2, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1142 */           generateBox(level, chunkBB, x, y + 1, 0, x, y + 1, 7, BASE_GRAY, BASE_GRAY, false);
/*      */         } 
/* 1144 */         x = 7;
/* 1145 */         if (definition.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1146 */           generateBox(level, chunkBB, x, y, 2, x, y + 2, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1147 */           generateBox(level, chunkBB, x, y, 5, x, y + 2, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1148 */           generateBox(level, chunkBB, x, y + 2, 3, x, y + 2, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */         } else {
/* 1150 */           generateBox(level, chunkBB, x, y, 0, x, y + 2, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1151 */           generateBox(level, chunkBB, x, y + 1, 0, x, y + 1, 7, BASE_GRAY, BASE_GRAY, false);
/*      */         } 
/* 1153 */         definition = above;
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentDoubleXRoom
/*      */     extends OceanMonumentPiece
/*      */   {
/* 1161 */     public OceanMonumentDoubleXRoom(Direction orientation, OceanMonumentPieces.RoomDefinition definition) { super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_X_ROOM, 1, orientation, definition, 2, 1, 1); }
/*      */ 
/*      */ 
/*      */     
/* 1165 */     public OceanMonumentDoubleXRoom(CompoundTag tag) { super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_X_ROOM, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1170 */       OceanMonumentPieces.RoomDefinition east = this.roomDefinition.connections[Direction.EAST.get3DDataValue()];
/* 1171 */       OceanMonumentPieces.RoomDefinition west = this.roomDefinition;
/* 1172 */       if (this.roomDefinition.index / 25 > 0) {
/* 1173 */         generateDefaultFloor(level, chunkBB, 8, 0, east.hasOpening[Direction.DOWN.get3DDataValue()]);
/* 1174 */         generateDefaultFloor(level, chunkBB, 0, 0, west.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       } 
/* 1176 */       if (west.connections[Direction.UP.get3DDataValue()] == null) {
/* 1177 */         generateBoxOnFillOnly(level, chunkBB, 1, 4, 1, 7, 4, 6, BASE_GRAY);
/*      */       }
/* 1179 */       if (east.connections[Direction.UP.get3DDataValue()] == null) {
/* 1180 */         generateBoxOnFillOnly(level, chunkBB, 8, 4, 1, 14, 4, 6, BASE_GRAY);
/*      */       }
/*      */ 
/*      */       
/* 1184 */       generateBox(level, chunkBB, 0, 3, 0, 0, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1185 */       generateBox(level, chunkBB, 15, 3, 0, 15, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1186 */       generateBox(level, chunkBB, 1, 3, 0, 15, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1187 */       generateBox(level, chunkBB, 1, 3, 7, 14, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1188 */       generateBox(level, chunkBB, 0, 2, 0, 0, 2, 7, BASE_GRAY, BASE_GRAY, false);
/* 1189 */       generateBox(level, chunkBB, 15, 2, 0, 15, 2, 7, BASE_GRAY, BASE_GRAY, false);
/* 1190 */       generateBox(level, chunkBB, 1, 2, 0, 15, 2, 0, BASE_GRAY, BASE_GRAY, false);
/* 1191 */       generateBox(level, chunkBB, 1, 2, 7, 14, 2, 7, BASE_GRAY, BASE_GRAY, false);
/* 1192 */       generateBox(level, chunkBB, 0, 1, 0, 0, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1193 */       generateBox(level, chunkBB, 15, 1, 0, 15, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1194 */       generateBox(level, chunkBB, 1, 1, 0, 15, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1195 */       generateBox(level, chunkBB, 1, 1, 7, 14, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/* 1198 */       generateBox(level, chunkBB, 5, 1, 0, 10, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1199 */       generateBox(level, chunkBB, 6, 2, 0, 9, 2, 3, BASE_GRAY, BASE_GRAY, false);
/* 1200 */       generateBox(level, chunkBB, 5, 3, 0, 10, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1202 */       placeBlock(level, LAMP_BLOCK, 6, 2, 3, chunkBB);
/* 1203 */       placeBlock(level, LAMP_BLOCK, 9, 2, 3, chunkBB);
/*      */ 
/*      */       
/* 1206 */       if (west.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1207 */         generateWaterBox(level, chunkBB, 3, 1, 0, 4, 2, 0);
/*      */       }
/* 1209 */       if (west.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1210 */         generateWaterBox(level, chunkBB, 3, 1, 7, 4, 2, 7);
/*      */       }
/* 1212 */       if (west.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1213 */         generateWaterBox(level, chunkBB, 0, 1, 3, 0, 2, 4);
/*      */       }
/* 1215 */       if (east.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1216 */         generateWaterBox(level, chunkBB, 11, 1, 0, 12, 2, 0);
/*      */       }
/* 1218 */       if (east.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1219 */         generateWaterBox(level, chunkBB, 11, 1, 7, 12, 2, 7);
/*      */       }
/* 1221 */       if (east.hasOpening[Direction.EAST.get3DDataValue()])
/* 1222 */         generateWaterBox(level, chunkBB, 15, 1, 3, 15, 2, 4); 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentDoubleZRoom
/*      */     extends OceanMonumentPiece
/*      */   {
/* 1229 */     public OceanMonumentDoubleZRoom(Direction orientation, OceanMonumentPieces.RoomDefinition definition) { super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Z_ROOM, 1, orientation, definition, 1, 1, 2); }
/*      */ 
/*      */ 
/*      */     
/* 1233 */     public OceanMonumentDoubleZRoom(CompoundTag tag) { super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Z_ROOM, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1238 */       OceanMonumentPieces.RoomDefinition north = this.roomDefinition.connections[Direction.NORTH.get3DDataValue()];
/* 1239 */       OceanMonumentPieces.RoomDefinition south = this.roomDefinition;
/* 1240 */       if (this.roomDefinition.index / 25 > 0) {
/* 1241 */         generateDefaultFloor(level, chunkBB, 0, 8, north.hasOpening[Direction.DOWN.get3DDataValue()]);
/* 1242 */         generateDefaultFloor(level, chunkBB, 0, 0, south.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       } 
/* 1244 */       if (south.connections[Direction.UP.get3DDataValue()] == null) {
/* 1245 */         generateBoxOnFillOnly(level, chunkBB, 1, 4, 1, 6, 4, 7, BASE_GRAY);
/*      */       }
/* 1247 */       if (north.connections[Direction.UP.get3DDataValue()] == null) {
/* 1248 */         generateBoxOnFillOnly(level, chunkBB, 1, 4, 8, 6, 4, 14, BASE_GRAY);
/*      */       }
/*      */ 
/*      */       
/* 1252 */       generateBox(level, chunkBB, 0, 3, 0, 0, 3, 15, BASE_LIGHT, BASE_LIGHT, false);
/* 1253 */       generateBox(level, chunkBB, 7, 3, 0, 7, 3, 15, BASE_LIGHT, BASE_LIGHT, false);
/* 1254 */       generateBox(level, chunkBB, 1, 3, 0, 7, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1255 */       generateBox(level, chunkBB, 1, 3, 15, 6, 3, 15, BASE_LIGHT, BASE_LIGHT, false);
/* 1256 */       generateBox(level, chunkBB, 0, 2, 0, 0, 2, 15, BASE_GRAY, BASE_GRAY, false);
/* 1257 */       generateBox(level, chunkBB, 7, 2, 0, 7, 2, 15, BASE_GRAY, BASE_GRAY, false);
/* 1258 */       generateBox(level, chunkBB, 1, 2, 0, 7, 2, 0, BASE_GRAY, BASE_GRAY, false);
/* 1259 */       generateBox(level, chunkBB, 1, 2, 15, 6, 2, 15, BASE_GRAY, BASE_GRAY, false);
/* 1260 */       generateBox(level, chunkBB, 0, 1, 0, 0, 1, 15, BASE_LIGHT, BASE_LIGHT, false);
/* 1261 */       generateBox(level, chunkBB, 7, 1, 0, 7, 1, 15, BASE_LIGHT, BASE_LIGHT, false);
/* 1262 */       generateBox(level, chunkBB, 1, 1, 0, 7, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1263 */       generateBox(level, chunkBB, 1, 1, 15, 6, 1, 15, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/* 1266 */       generateBox(level, chunkBB, 1, 1, 1, 1, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1267 */       generateBox(level, chunkBB, 6, 1, 1, 6, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1268 */       generateBox(level, chunkBB, 1, 3, 1, 1, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1269 */       generateBox(level, chunkBB, 6, 3, 1, 6, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1270 */       generateBox(level, chunkBB, 1, 1, 13, 1, 1, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1271 */       generateBox(level, chunkBB, 6, 1, 13, 6, 1, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1272 */       generateBox(level, chunkBB, 1, 3, 13, 1, 3, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1273 */       generateBox(level, chunkBB, 6, 3, 13, 6, 3, 14, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/* 1276 */       generateBox(level, chunkBB, 2, 1, 6, 2, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1277 */       generateBox(level, chunkBB, 5, 1, 6, 5, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1278 */       generateBox(level, chunkBB, 2, 1, 9, 2, 3, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1279 */       generateBox(level, chunkBB, 5, 1, 9, 5, 3, 9, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1281 */       generateBox(level, chunkBB, 3, 2, 6, 4, 2, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1282 */       generateBox(level, chunkBB, 3, 2, 9, 4, 2, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1283 */       generateBox(level, chunkBB, 2, 2, 7, 2, 2, 8, BASE_LIGHT, BASE_LIGHT, false);
/* 1284 */       generateBox(level, chunkBB, 5, 2, 7, 5, 2, 8, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1286 */       placeBlock(level, LAMP_BLOCK, 2, 2, 5, chunkBB);
/* 1287 */       placeBlock(level, LAMP_BLOCK, 5, 2, 5, chunkBB);
/* 1288 */       placeBlock(level, LAMP_BLOCK, 2, 2, 10, chunkBB);
/* 1289 */       placeBlock(level, LAMP_BLOCK, 5, 2, 10, chunkBB);
/* 1290 */       placeBlock(level, BASE_LIGHT, 2, 3, 5, chunkBB);
/* 1291 */       placeBlock(level, BASE_LIGHT, 5, 3, 5, chunkBB);
/* 1292 */       placeBlock(level, BASE_LIGHT, 2, 3, 10, chunkBB);
/* 1293 */       placeBlock(level, BASE_LIGHT, 5, 3, 10, chunkBB);
/*      */ 
/*      */       
/* 1296 */       if (south.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1297 */         generateWaterBox(level, chunkBB, 3, 1, 0, 4, 2, 0);
/*      */       }
/* 1299 */       if (south.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1300 */         generateWaterBox(level, chunkBB, 7, 1, 3, 7, 2, 4);
/*      */       }
/* 1302 */       if (south.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1303 */         generateWaterBox(level, chunkBB, 0, 1, 3, 0, 2, 4);
/*      */       }
/* 1305 */       if (north.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1306 */         generateWaterBox(level, chunkBB, 3, 1, 15, 4, 2, 15);
/*      */       }
/* 1308 */       if (north.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1309 */         generateWaterBox(level, chunkBB, 0, 1, 11, 0, 2, 12);
/*      */       }
/* 1311 */       if (north.hasOpening[Direction.EAST.get3DDataValue()])
/* 1312 */         generateWaterBox(level, chunkBB, 7, 1, 11, 7, 2, 12); 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentDoubleXYRoom
/*      */     extends OceanMonumentPiece
/*      */   {
/* 1319 */     public OceanMonumentDoubleXYRoom(Direction orientation, OceanMonumentPieces.RoomDefinition definition) { super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_XY_ROOM, 1, orientation, definition, 2, 2, 1); }
/*      */ 
/*      */ 
/*      */     
/* 1323 */     public OceanMonumentDoubleXYRoom(CompoundTag tag) { super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_XY_ROOM, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1328 */       OceanMonumentPieces.RoomDefinition east = this.roomDefinition.connections[Direction.EAST.get3DDataValue()];
/* 1329 */       OceanMonumentPieces.RoomDefinition west = this.roomDefinition;
/* 1330 */       OceanMonumentPieces.RoomDefinition westUp = west.connections[Direction.UP.get3DDataValue()];
/* 1331 */       OceanMonumentPieces.RoomDefinition eastUp = east.connections[Direction.UP.get3DDataValue()];
/*      */       
/* 1333 */       if (this.roomDefinition.index / 25 > 0) {
/* 1334 */         generateDefaultFloor(level, chunkBB, 8, 0, east.hasOpening[Direction.DOWN.get3DDataValue()]);
/* 1335 */         generateDefaultFloor(level, chunkBB, 0, 0, west.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       } 
/* 1337 */       if (westUp.connections[Direction.UP.get3DDataValue()] == null) {
/* 1338 */         generateBoxOnFillOnly(level, chunkBB, 1, 8, 1, 7, 8, 6, BASE_GRAY);
/*      */       }
/* 1340 */       if (eastUp.connections[Direction.UP.get3DDataValue()] == null) {
/* 1341 */         generateBoxOnFillOnly(level, chunkBB, 8, 8, 1, 14, 8, 6, BASE_GRAY);
/*      */       }
/*      */ 
/*      */       
/* 1345 */       for (int y = 1; y <= 7; y++) {
/* 1346 */         BlockState block = BASE_LIGHT;
/* 1347 */         if (y == 2 || y == 6) {
/* 1348 */           block = BASE_GRAY;
/*      */         }
/* 1350 */         generateBox(level, chunkBB, 0, y, 0, 0, y, 7, block, block, false);
/* 1351 */         generateBox(level, chunkBB, 15, y, 0, 15, y, 7, block, block, false);
/* 1352 */         generateBox(level, chunkBB, 1, y, 0, 15, y, 0, block, block, false);
/* 1353 */         generateBox(level, chunkBB, 1, y, 7, 14, y, 7, block, block, false);
/*      */       } 
/*      */ 
/*      */       
/* 1357 */       generateBox(level, chunkBB, 2, 1, 3, 2, 7, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1358 */       generateBox(level, chunkBB, 3, 1, 2, 4, 7, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1359 */       generateBox(level, chunkBB, 3, 1, 5, 4, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1360 */       generateBox(level, chunkBB, 13, 1, 3, 13, 7, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1361 */       generateBox(level, chunkBB, 11, 1, 2, 12, 7, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1362 */       generateBox(level, chunkBB, 11, 1, 5, 12, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1364 */       generateBox(level, chunkBB, 5, 1, 3, 5, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1365 */       generateBox(level, chunkBB, 10, 1, 3, 10, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1367 */       generateBox(level, chunkBB, 5, 7, 2, 10, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1368 */       generateBox(level, chunkBB, 5, 5, 2, 5, 7, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1369 */       generateBox(level, chunkBB, 10, 5, 2, 10, 7, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1370 */       generateBox(level, chunkBB, 5, 5, 5, 5, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1371 */       generateBox(level, chunkBB, 10, 5, 5, 10, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1372 */       placeBlock(level, BASE_LIGHT, 6, 6, 2, chunkBB);
/* 1373 */       placeBlock(level, BASE_LIGHT, 9, 6, 2, chunkBB);
/* 1374 */       placeBlock(level, BASE_LIGHT, 6, 6, 5, chunkBB);
/* 1375 */       placeBlock(level, BASE_LIGHT, 9, 6, 5, chunkBB);
/*      */       
/* 1377 */       generateBox(level, chunkBB, 5, 4, 3, 6, 4, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1378 */       generateBox(level, chunkBB, 9, 4, 3, 10, 4, 4, BASE_LIGHT, BASE_LIGHT, false);
/* 1379 */       placeBlock(level, LAMP_BLOCK, 5, 4, 2, chunkBB);
/* 1380 */       placeBlock(level, LAMP_BLOCK, 5, 4, 5, chunkBB);
/* 1381 */       placeBlock(level, LAMP_BLOCK, 10, 4, 2, chunkBB);
/* 1382 */       placeBlock(level, LAMP_BLOCK, 10, 4, 5, chunkBB);
/*      */ 
/*      */       
/* 1385 */       if (west.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1386 */         generateWaterBox(level, chunkBB, 3, 1, 0, 4, 2, 0);
/*      */       }
/* 1388 */       if (west.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1389 */         generateWaterBox(level, chunkBB, 3, 1, 7, 4, 2, 7);
/*      */       }
/* 1391 */       if (west.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1392 */         generateWaterBox(level, chunkBB, 0, 1, 3, 0, 2, 4);
/*      */       }
/* 1394 */       if (east.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1395 */         generateWaterBox(level, chunkBB, 11, 1, 0, 12, 2, 0);
/*      */       }
/* 1397 */       if (east.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1398 */         generateWaterBox(level, chunkBB, 11, 1, 7, 12, 2, 7);
/*      */       }
/* 1400 */       if (east.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1401 */         generateWaterBox(level, chunkBB, 15, 1, 3, 15, 2, 4);
/*      */       }
/* 1403 */       if (westUp.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1404 */         generateWaterBox(level, chunkBB, 3, 5, 0, 4, 6, 0);
/*      */       }
/* 1406 */       if (westUp.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1407 */         generateWaterBox(level, chunkBB, 3, 5, 7, 4, 6, 7);
/*      */       }
/* 1409 */       if (westUp.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1410 */         generateWaterBox(level, chunkBB, 0, 5, 3, 0, 6, 4);
/*      */       }
/* 1412 */       if (eastUp.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1413 */         generateWaterBox(level, chunkBB, 11, 5, 0, 12, 6, 0);
/*      */       }
/* 1415 */       if (eastUp.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1416 */         generateWaterBox(level, chunkBB, 11, 5, 7, 12, 6, 7);
/*      */       }
/* 1418 */       if (eastUp.hasOpening[Direction.EAST.get3DDataValue()])
/* 1419 */         generateWaterBox(level, chunkBB, 15, 5, 3, 15, 6, 4); 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentDoubleYZRoom
/*      */     extends OceanMonumentPiece
/*      */   {
/* 1426 */     public OceanMonumentDoubleYZRoom(Direction orientation, OceanMonumentPieces.RoomDefinition definition) { super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_YZ_ROOM, 1, orientation, definition, 1, 2, 2); }
/*      */ 
/*      */ 
/*      */     
/* 1430 */     public OceanMonumentDoubleYZRoom(CompoundTag tag) { super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_YZ_ROOM, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1435 */       OceanMonumentPieces.RoomDefinition north = this.roomDefinition.connections[Direction.NORTH.get3DDataValue()];
/* 1436 */       OceanMonumentPieces.RoomDefinition south = this.roomDefinition;
/* 1437 */       OceanMonumentPieces.RoomDefinition northUp = north.connections[Direction.UP.get3DDataValue()];
/* 1438 */       OceanMonumentPieces.RoomDefinition southUp = south.connections[Direction.UP.get3DDataValue()];
/* 1439 */       if (this.roomDefinition.index / 25 > 0) {
/* 1440 */         generateDefaultFloor(level, chunkBB, 0, 8, north.hasOpening[Direction.DOWN.get3DDataValue()]);
/* 1441 */         generateDefaultFloor(level, chunkBB, 0, 0, south.hasOpening[Direction.DOWN.get3DDataValue()]);
/*      */       } 
/* 1443 */       if (southUp.connections[Direction.UP.get3DDataValue()] == null) {
/* 1444 */         generateBoxOnFillOnly(level, chunkBB, 1, 8, 1, 6, 8, 7, BASE_GRAY);
/*      */       }
/* 1446 */       if (northUp.connections[Direction.UP.get3DDataValue()] == null) {
/* 1447 */         generateBoxOnFillOnly(level, chunkBB, 1, 8, 8, 6, 8, 14, BASE_GRAY);
/*      */       }
/*      */ 
/*      */       
/* 1451 */       for (int y = 1; y <= 7; y++) {
/* 1452 */         BlockState block = BASE_LIGHT;
/* 1453 */         if (y == 2 || y == 6) {
/* 1454 */           block = BASE_GRAY;
/*      */         }
/* 1456 */         generateBox(level, chunkBB, 0, y, 0, 0, y, 15, block, block, false);
/* 1457 */         generateBox(level, chunkBB, 7, y, 0, 7, y, 15, block, block, false);
/* 1458 */         generateBox(level, chunkBB, 1, y, 0, 6, y, 0, block, block, false);
/* 1459 */         generateBox(level, chunkBB, 1, y, 15, 6, y, 15, block, block, false);
/*      */       } 
/*      */ 
/*      */       
/* 1463 */       for (int y = 1; y <= 7; y++) {
/* 1464 */         BlockState block = BASE_BLACK;
/* 1465 */         if (y == 2 || y == 6) {
/* 1466 */           block = LAMP_BLOCK;
/*      */         }
/* 1468 */         generateBox(level, chunkBB, 3, y, 7, 4, y, 8, block, block, false);
/*      */       } 
/*      */ 
/*      */       
/* 1472 */       if (south.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1473 */         generateWaterBox(level, chunkBB, 3, 1, 0, 4, 2, 0);
/*      */       }
/* 1475 */       if (south.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1476 */         generateWaterBox(level, chunkBB, 7, 1, 3, 7, 2, 4);
/*      */       }
/* 1478 */       if (south.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1479 */         generateWaterBox(level, chunkBB, 0, 1, 3, 0, 2, 4);
/*      */       }
/* 1481 */       if (north.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1482 */         generateWaterBox(level, chunkBB, 3, 1, 15, 4, 2, 15);
/*      */       }
/* 1484 */       if (north.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1485 */         generateWaterBox(level, chunkBB, 0, 1, 11, 0, 2, 12);
/*      */       }
/* 1487 */       if (north.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1488 */         generateWaterBox(level, chunkBB, 7, 1, 11, 7, 2, 12);
/*      */       }
/*      */       
/* 1491 */       if (southUp.hasOpening[Direction.SOUTH.get3DDataValue()]) {
/* 1492 */         generateWaterBox(level, chunkBB, 3, 5, 0, 4, 6, 0);
/*      */       }
/* 1494 */       if (southUp.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1495 */         generateWaterBox(level, chunkBB, 7, 5, 3, 7, 6, 4);
/* 1496 */         generateBox(level, chunkBB, 5, 4, 2, 6, 4, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1497 */         generateBox(level, chunkBB, 6, 1, 2, 6, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1498 */         generateBox(level, chunkBB, 6, 1, 5, 6, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/* 1500 */       if (southUp.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1501 */         generateWaterBox(level, chunkBB, 0, 5, 3, 0, 6, 4);
/* 1502 */         generateBox(level, chunkBB, 1, 4, 2, 2, 4, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1503 */         generateBox(level, chunkBB, 1, 1, 2, 1, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1504 */         generateBox(level, chunkBB, 1, 1, 5, 1, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/* 1506 */       if (northUp.hasOpening[Direction.NORTH.get3DDataValue()]) {
/* 1507 */         generateWaterBox(level, chunkBB, 3, 5, 15, 4, 6, 15);
/*      */       }
/* 1509 */       if (northUp.hasOpening[Direction.WEST.get3DDataValue()]) {
/* 1510 */         generateWaterBox(level, chunkBB, 0, 5, 11, 0, 6, 12);
/* 1511 */         generateBox(level, chunkBB, 1, 4, 10, 2, 4, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1512 */         generateBox(level, chunkBB, 1, 1, 10, 1, 3, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1513 */         generateBox(level, chunkBB, 1, 1, 13, 1, 3, 13, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/* 1515 */       if (northUp.hasOpening[Direction.EAST.get3DDataValue()]) {
/* 1516 */         generateWaterBox(level, chunkBB, 7, 5, 11, 7, 6, 12);
/* 1517 */         generateBox(level, chunkBB, 5, 4, 10, 6, 4, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1518 */         generateBox(level, chunkBB, 6, 1, 10, 6, 3, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1519 */         generateBox(level, chunkBB, 6, 1, 13, 6, 3, 13, BASE_LIGHT, BASE_LIGHT, false);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentCoreRoom
/*      */     extends OceanMonumentPiece {
/* 1526 */     public OceanMonumentCoreRoom(Direction orientation, OceanMonumentPieces.RoomDefinition definition) { super(StructurePieceType.OCEAN_MONUMENT_CORE_ROOM, 1, orientation, definition, 2, 2, 2); }
/*      */ 
/*      */ 
/*      */     
/* 1530 */     public OceanMonumentCoreRoom(CompoundTag tag) { super(StructurePieceType.OCEAN_MONUMENT_CORE_ROOM, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1535 */       generateBoxOnFillOnly(level, chunkBB, 1, 8, 0, 14, 8, 14, BASE_GRAY);
/*      */ 
/*      */ 
/*      */       
/* 1539 */       int y = 7;
/* 1540 */       BlockState block = BASE_LIGHT;
/* 1541 */       generateBox(level, chunkBB, 0, 7, 0, 0, 7, 15, block, block, false);
/* 1542 */       generateBox(level, chunkBB, 15, 7, 0, 15, 7, 15, block, block, false);
/* 1543 */       generateBox(level, chunkBB, 1, 7, 0, 15, 7, 0, block, block, false);
/* 1544 */       generateBox(level, chunkBB, 1, 7, 15, 14, 7, 15, block, block, false);
/*      */       
/* 1546 */       for (int y = 1; y <= 6; y++) {
/* 1547 */         BlockState block = BASE_LIGHT;
/* 1548 */         if (y == 2 || y == 6) {
/* 1549 */           block = BASE_GRAY;
/*      */         }
/*      */         
/* 1552 */         for (int x = 0; x <= 15; x += 15) {
/* 1553 */           generateBox(level, chunkBB, x, y, 0, x, y, 1, block, block, false);
/* 1554 */           generateBox(level, chunkBB, x, y, 6, x, y, 9, block, block, false);
/* 1555 */           generateBox(level, chunkBB, x, y, 14, x, y, 15, block, block, false);
/*      */         } 
/* 1557 */         generateBox(level, chunkBB, 1, y, 0, 1, y, 0, block, block, false);
/* 1558 */         generateBox(level, chunkBB, 6, y, 0, 9, y, 0, block, block, false);
/* 1559 */         generateBox(level, chunkBB, 14, y, 0, 14, y, 0, block, block, false);
/*      */         
/* 1561 */         generateBox(level, chunkBB, 1, y, 15, 14, y, 15, block, block, false);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1566 */       generateBox(level, chunkBB, 6, 3, 6, 9, 6, 9, BASE_BLACK, BASE_BLACK, false);
/* 1567 */       generateBox(level, chunkBB, 7, 4, 7, 8, 5, 8, Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
/* 1568 */       for (int y = 3; y <= 6; y += 3) {
/* 1569 */         for (int x = 6; x <= 9; x += 3) {
/* 1570 */           placeBlock(level, LAMP_BLOCK, x, y, 6, chunkBB);
/* 1571 */           placeBlock(level, LAMP_BLOCK, x, y, 9, chunkBB);
/*      */         } 
/*      */       } 
/*      */       
/* 1575 */       generateBox(level, chunkBB, 5, 1, 6, 5, 2, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1576 */       generateBox(level, chunkBB, 5, 1, 9, 5, 2, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1577 */       generateBox(level, chunkBB, 10, 1, 6, 10, 2, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1578 */       generateBox(level, chunkBB, 10, 1, 9, 10, 2, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1579 */       generateBox(level, chunkBB, 6, 1, 5, 6, 2, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1580 */       generateBox(level, chunkBB, 9, 1, 5, 9, 2, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1581 */       generateBox(level, chunkBB, 6, 1, 10, 6, 2, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1582 */       generateBox(level, chunkBB, 9, 1, 10, 9, 2, 10, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1584 */       generateBox(level, chunkBB, 5, 2, 5, 5, 6, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1585 */       generateBox(level, chunkBB, 5, 2, 10, 5, 6, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1586 */       generateBox(level, chunkBB, 10, 2, 5, 10, 6, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1587 */       generateBox(level, chunkBB, 10, 2, 10, 10, 6, 10, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1589 */       generateBox(level, chunkBB, 5, 7, 1, 5, 7, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1590 */       generateBox(level, chunkBB, 10, 7, 1, 10, 7, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1591 */       generateBox(level, chunkBB, 5, 7, 9, 5, 7, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1592 */       generateBox(level, chunkBB, 10, 7, 9, 10, 7, 14, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1594 */       generateBox(level, chunkBB, 1, 7, 5, 6, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1595 */       generateBox(level, chunkBB, 1, 7, 10, 6, 7, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1596 */       generateBox(level, chunkBB, 9, 7, 5, 14, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
/* 1597 */       generateBox(level, chunkBB, 9, 7, 10, 14, 7, 10, BASE_LIGHT, BASE_LIGHT, false);
/*      */ 
/*      */       
/* 1600 */       generateBox(level, chunkBB, 2, 1, 2, 2, 1, 3, BASE_LIGHT, BASE_LIGHT, false);
/* 1601 */       generateBox(level, chunkBB, 3, 1, 2, 3, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1602 */       generateBox(level, chunkBB, 13, 1, 2, 13, 1, 3, BASE_LIGHT, BASE_LIGHT, false);
/* 1603 */       generateBox(level, chunkBB, 12, 1, 2, 12, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
/* 1604 */       generateBox(level, chunkBB, 2, 1, 12, 2, 1, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1605 */       generateBox(level, chunkBB, 3, 1, 13, 3, 1, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1606 */       generateBox(level, chunkBB, 13, 1, 12, 13, 1, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1607 */       generateBox(level, chunkBB, 12, 1, 13, 12, 1, 13, BASE_LIGHT, BASE_LIGHT, false);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentWingRoom extends OceanMonumentPiece {
/*      */     private int mainDesign;
/*      */     
/*      */     public OceanMonumentWingRoom(Direction orientation, BoundingBox boundingBox, int randomValue) {
/* 1615 */       super(StructurePieceType.OCEAN_MONUMENT_WING_ROOM, orientation, 1, boundingBox);
/* 1616 */       this.mainDesign = randomValue & true;
/*      */     }
/*      */ 
/*      */     
/* 1620 */     public OceanMonumentWingRoom(CompoundTag tag) { super(StructurePieceType.OCEAN_MONUMENT_WING_ROOM, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1625 */       if (this.mainDesign == 0) {
/* 1626 */         for (int i = 0; i < 4; i++) {
/* 1627 */           generateBox(level, chunkBB, 10 - i, 3 - i, 20 - i, 12 + i, 3 - i, 20, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/* 1629 */         generateBox(level, chunkBB, 7, 0, 6, 15, 0, 16, BASE_LIGHT, BASE_LIGHT, false);
/* 1630 */         generateBox(level, chunkBB, 6, 0, 6, 6, 3, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 1631 */         generateBox(level, chunkBB, 16, 0, 6, 16, 3, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 1632 */         generateBox(level, chunkBB, 7, 1, 7, 7, 1, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 1633 */         generateBox(level, chunkBB, 15, 1, 7, 15, 1, 20, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/* 1635 */         generateBox(level, chunkBB, 7, 1, 6, 9, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1636 */         generateBox(level, chunkBB, 13, 1, 6, 15, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
/* 1637 */         generateBox(level, chunkBB, 8, 1, 7, 9, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1638 */         generateBox(level, chunkBB, 13, 1, 7, 14, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1639 */         generateBox(level, chunkBB, 9, 0, 5, 13, 0, 5, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/* 1641 */         generateBox(level, chunkBB, 10, 0, 7, 12, 0, 7, BASE_BLACK, BASE_BLACK, false);
/* 1642 */         generateBox(level, chunkBB, 8, 0, 10, 8, 0, 12, BASE_BLACK, BASE_BLACK, false);
/* 1643 */         generateBox(level, chunkBB, 14, 0, 10, 14, 0, 12, BASE_BLACK, BASE_BLACK, false);
/*      */         
/* 1645 */         for (int z = 18; z >= 7; z -= 3) {
/* 1646 */           placeBlock(level, LAMP_BLOCK, 6, 3, z, chunkBB);
/* 1647 */           placeBlock(level, LAMP_BLOCK, 16, 3, z, chunkBB);
/*      */         } 
/* 1649 */         placeBlock(level, LAMP_BLOCK, 10, 0, 10, chunkBB);
/* 1650 */         placeBlock(level, LAMP_BLOCK, 12, 0, 10, chunkBB);
/* 1651 */         placeBlock(level, LAMP_BLOCK, 10, 0, 12, chunkBB);
/* 1652 */         placeBlock(level, LAMP_BLOCK, 12, 0, 12, chunkBB);
/*      */         
/* 1654 */         placeBlock(level, LAMP_BLOCK, 8, 3, 6, chunkBB);
/* 1655 */         placeBlock(level, LAMP_BLOCK, 14, 3, 6, chunkBB);
/*      */ 
/*      */         
/* 1658 */         placeBlock(level, BASE_LIGHT, 4, 2, 4, chunkBB);
/* 1659 */         placeBlock(level, LAMP_BLOCK, 4, 1, 4, chunkBB);
/* 1660 */         placeBlock(level, BASE_LIGHT, 4, 0, 4, chunkBB);
/*      */         
/* 1662 */         placeBlock(level, BASE_LIGHT, 18, 2, 4, chunkBB);
/* 1663 */         placeBlock(level, LAMP_BLOCK, 18, 1, 4, chunkBB);
/* 1664 */         placeBlock(level, BASE_LIGHT, 18, 0, 4, chunkBB);
/*      */         
/* 1666 */         placeBlock(level, BASE_LIGHT, 4, 2, 18, chunkBB);
/* 1667 */         placeBlock(level, LAMP_BLOCK, 4, 1, 18, chunkBB);
/* 1668 */         placeBlock(level, BASE_LIGHT, 4, 0, 18, chunkBB);
/*      */         
/* 1670 */         placeBlock(level, BASE_LIGHT, 18, 2, 18, chunkBB);
/* 1671 */         placeBlock(level, LAMP_BLOCK, 18, 1, 18, chunkBB);
/* 1672 */         placeBlock(level, BASE_LIGHT, 18, 0, 18, chunkBB);
/*      */ 
/*      */         
/* 1675 */         placeBlock(level, BASE_LIGHT, 9, 7, 20, chunkBB);
/* 1676 */         placeBlock(level, BASE_LIGHT, 13, 7, 20, chunkBB);
/* 1677 */         generateBox(level, chunkBB, 6, 0, 21, 7, 4, 21, BASE_LIGHT, BASE_LIGHT, false);
/* 1678 */         generateBox(level, chunkBB, 15, 0, 21, 16, 4, 21, BASE_LIGHT, BASE_LIGHT, false);
/*      */         
/* 1680 */         spawnElder(level, chunkBB, 11, 2, 16);
/* 1681 */       } else if (this.mainDesign == 1) {
/* 1682 */         generateBox(level, chunkBB, 9, 3, 18, 13, 3, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 1683 */         generateBox(level, chunkBB, 9, 0, 18, 9, 2, 18, BASE_LIGHT, BASE_LIGHT, false);
/* 1684 */         generateBox(level, chunkBB, 13, 0, 18, 13, 2, 18, BASE_LIGHT, BASE_LIGHT, false);
/* 1685 */         int x = 9;
/* 1686 */         int z = 20;
/* 1687 */         int y = 5;
/* 1688 */         for (int i = 0; i < 2; i++) {
/* 1689 */           placeBlock(level, BASE_LIGHT, x, 6, 20, chunkBB);
/* 1690 */           placeBlock(level, LAMP_BLOCK, x, 5, 20, chunkBB);
/* 1691 */           placeBlock(level, BASE_LIGHT, x, 4, 20, chunkBB);
/* 1692 */           x = 13;
/*      */         } 
/*      */         
/* 1695 */         generateBox(level, chunkBB, 7, 3, 7, 15, 3, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1696 */         x = 10;
/* 1697 */         for (int i = 0; i < 2; i++) {
/* 1698 */           generateBox(level, chunkBB, x, 0, 10, x, 6, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1699 */           generateBox(level, chunkBB, x, 0, 12, x, 6, 12, BASE_LIGHT, BASE_LIGHT, false);
/* 1700 */           placeBlock(level, LAMP_BLOCK, x, 0, 10, chunkBB);
/* 1701 */           placeBlock(level, LAMP_BLOCK, x, 0, 12, chunkBB);
/* 1702 */           placeBlock(level, LAMP_BLOCK, x, 4, 10, chunkBB);
/* 1703 */           placeBlock(level, LAMP_BLOCK, x, 4, 12, chunkBB);
/* 1704 */           x = 12;
/*      */         } 
/* 1706 */         x = 8;
/* 1707 */         for (int i = 0; i < 2; i++) {
/* 1708 */           generateBox(level, chunkBB, x, 0, 7, x, 2, 7, BASE_LIGHT, BASE_LIGHT, false);
/* 1709 */           generateBox(level, chunkBB, x, 0, 14, x, 2, 14, BASE_LIGHT, BASE_LIGHT, false);
/* 1710 */           x = 14;
/*      */         } 
/* 1712 */         generateBox(level, chunkBB, 8, 3, 8, 8, 3, 13, BASE_BLACK, BASE_BLACK, false);
/* 1713 */         generateBox(level, chunkBB, 14, 3, 8, 14, 3, 13, BASE_BLACK, BASE_BLACK, false);
/*      */         
/* 1715 */         spawnElder(level, chunkBB, 11, 5, 13);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class OceanMonumentPenthouse
/*      */     extends OceanMonumentPiece {
/* 1722 */     public OceanMonumentPenthouse(Direction orientation, BoundingBox boundingBox) { super(StructurePieceType.OCEAN_MONUMENT_PENTHOUSE, orientation, 1, boundingBox); }
/*      */ 
/*      */ 
/*      */     
/* 1726 */     public OceanMonumentPenthouse(CompoundTag tag) { super(StructurePieceType.OCEAN_MONUMENT_PENTHOUSE, tag); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1731 */       generateBox(level, chunkBB, 2, -1, 2, 11, -1, 11, BASE_LIGHT, BASE_LIGHT, false);
/* 1732 */       generateBox(level, chunkBB, 0, -1, 0, 1, -1, 11, BASE_GRAY, BASE_GRAY, false);
/* 1733 */       generateBox(level, chunkBB, 12, -1, 0, 13, -1, 11, BASE_GRAY, BASE_GRAY, false);
/* 1734 */       generateBox(level, chunkBB, 2, -1, 0, 11, -1, 1, BASE_GRAY, BASE_GRAY, false);
/* 1735 */       generateBox(level, chunkBB, 2, -1, 12, 11, -1, 13, BASE_GRAY, BASE_GRAY, false);
/*      */       
/* 1737 */       generateBox(level, chunkBB, 0, 0, 0, 0, 0, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1738 */       generateBox(level, chunkBB, 13, 0, 0, 13, 0, 13, BASE_LIGHT, BASE_LIGHT, false);
/* 1739 */       generateBox(level, chunkBB, 1, 0, 0, 12, 0, 0, BASE_LIGHT, BASE_LIGHT, false);
/* 1740 */       generateBox(level, chunkBB, 1, 0, 13, 12, 0, 13, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1742 */       for (int i = 2; i <= 11; i += 3) {
/* 1743 */         placeBlock(level, LAMP_BLOCK, 0, 0, i, chunkBB);
/* 1744 */         placeBlock(level, LAMP_BLOCK, 13, 0, i, chunkBB);
/* 1745 */         placeBlock(level, LAMP_BLOCK, i, 0, 0, chunkBB);
/*      */       } 
/*      */       
/* 1748 */       generateBox(level, chunkBB, 2, 0, 3, 4, 0, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1749 */       generateBox(level, chunkBB, 9, 0, 3, 11, 0, 9, BASE_LIGHT, BASE_LIGHT, false);
/* 1750 */       generateBox(level, chunkBB, 4, 0, 9, 9, 0, 11, BASE_LIGHT, BASE_LIGHT, false);
/* 1751 */       placeBlock(level, BASE_LIGHT, 5, 0, 8, chunkBB);
/* 1752 */       placeBlock(level, BASE_LIGHT, 8, 0, 8, chunkBB);
/* 1753 */       placeBlock(level, BASE_LIGHT, 10, 0, 10, chunkBB);
/* 1754 */       placeBlock(level, BASE_LIGHT, 3, 0, 10, chunkBB);
/* 1755 */       generateBox(level, chunkBB, 3, 0, 3, 3, 0, 7, BASE_BLACK, BASE_BLACK, false);
/* 1756 */       generateBox(level, chunkBB, 10, 0, 3, 10, 0, 7, BASE_BLACK, BASE_BLACK, false);
/* 1757 */       generateBox(level, chunkBB, 6, 0, 10, 7, 0, 10, BASE_BLACK, BASE_BLACK, false);
/*      */       
/* 1759 */       int x = 3;
/* 1760 */       for (int i = 0; i < 2; i++) {
/* 1761 */         for (int z = 2; z <= 8; z += 3) {
/* 1762 */           generateBox(level, chunkBB, x, 0, z, x, 2, z, BASE_LIGHT, BASE_LIGHT, false);
/*      */         }
/* 1764 */         x = 10;
/*      */       } 
/* 1766 */       generateBox(level, chunkBB, 5, 0, 10, 5, 2, 10, BASE_LIGHT, BASE_LIGHT, false);
/* 1767 */       generateBox(level, chunkBB, 8, 0, 10, 8, 2, 10, BASE_LIGHT, BASE_LIGHT, false);
/*      */       
/* 1769 */       generateBox(level, chunkBB, 6, -1, 7, 7, -1, 8, BASE_BLACK, BASE_BLACK, false);
/*      */ 
/*      */       
/* 1772 */       generateWaterBox(level, chunkBB, 6, -1, 3, 7, -1, 4);
/*      */       
/* 1774 */       spawnElder(level, chunkBB, 6, 1, 6);
/*      */     } }
/*      */   private static class RoomDefinition { private final int index; private final RoomDefinition[] connections;
/*      */     private final boolean[] hasOpening;
/*      */     
/*      */     public RoomDefinition(int roomIndex) {
/* 1780 */       this.connections = new RoomDefinition[6];
/* 1781 */       this.hasOpening = new boolean[6];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1787 */       this.index = roomIndex;
/*      */     }
/*      */     private boolean claimed; private boolean isSource; private int scanIndex;
/*      */     public void setConnection(Direction direction, RoomDefinition definition) {
/* 1791 */       this.connections[direction.get3DDataValue()] = definition;
/* 1792 */       definition.connections[direction.getOpposite().get3DDataValue()] = this;
/*      */     }
/*      */     
/*      */     public void updateOpenings() {
/* 1796 */       for (int i = 0; i < 6; i++) {
/* 1797 */         this.hasOpening[i] = (this.connections[i] != null);
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean findSource(int scanIndex) {
/* 1802 */       if (this.isSource) {
/* 1803 */         return true;
/*      */       }
/* 1805 */       this.scanIndex = scanIndex;
/* 1806 */       for (int i = 0; i < 6; i++) {
/* 1807 */         if (this.connections[i] != null && this.hasOpening[i] && 
/* 1808 */           (this.connections[i]).scanIndex != scanIndex && this.connections[i].findSource(scanIndex)) {
/* 1809 */           return true;
/*      */         }
/*      */       } 
/*      */       
/* 1813 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1817 */     public boolean isSpecial() { return (this.index >= 75); }
/*      */ 
/*      */     
/*      */     public int countOpenings() {
/* 1821 */       int c = 0;
/* 1822 */       for (int i = 0; i < 6; i++) {
/* 1823 */         if (this.hasOpening[i]) {
/* 1824 */           c++;
/*      */         }
/*      */       } 
/* 1827 */       return c;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class FitSimpleRoom
/*      */     implements MonumentRoomFitter
/*      */   {
/* 1840 */     public boolean fits(OceanMonumentPieces.RoomDefinition definition) { return true; }
/*      */ 
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction orientation, OceanMonumentPieces.RoomDefinition definition, RandomSource random) {
/* 1845 */       definition.claimed = true;
/* 1846 */       return new OceanMonumentPieces.OceanMonumentSimpleRoom(orientation, definition, random);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FitSimpleTopRoom
/*      */     implements MonumentRoomFitter
/*      */   {
/* 1853 */     public boolean fits(OceanMonumentPieces.RoomDefinition definition) { return (!definition.hasOpening[Direction.WEST.get3DDataValue()] && !definition.hasOpening[Direction.EAST.get3DDataValue()] && !definition.hasOpening[Direction.NORTH.get3DDataValue()] && !definition.hasOpening[Direction.SOUTH.get3DDataValue()] && !definition.hasOpening[Direction.UP.get3DDataValue()]); }
/*      */ 
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction orientation, OceanMonumentPieces.RoomDefinition definition, RandomSource random) {
/* 1858 */       definition.claimed = true;
/* 1859 */       return new OceanMonumentPieces.OceanMonumentSimpleTopRoom(orientation, definition);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FitDoubleYRoom
/*      */     implements MonumentRoomFitter
/*      */   {
/* 1866 */     public boolean fits(OceanMonumentPieces.RoomDefinition definition) { return (definition.hasOpening[Direction.UP.get3DDataValue()] && !(definition.connections[Direction.UP.get3DDataValue()]).claimed); }
/*      */ 
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction orientation, OceanMonumentPieces.RoomDefinition definition, RandomSource random) {
/* 1871 */       definition.claimed = true;
/* 1872 */       (definition.connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1873 */       return new OceanMonumentPieces.OceanMonumentDoubleYRoom(orientation, definition);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FitDoubleXRoom
/*      */     implements MonumentRoomFitter
/*      */   {
/* 1880 */     public boolean fits(OceanMonumentPieces.RoomDefinition definition) { return (definition.hasOpening[Direction.EAST.get3DDataValue()] && !(definition.connections[Direction.EAST.get3DDataValue()]).claimed); }
/*      */ 
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction orientation, OceanMonumentPieces.RoomDefinition definition, RandomSource random) {
/* 1885 */       definition.claimed = true;
/* 1886 */       (definition.connections[Direction.EAST.get3DDataValue()]).claimed = true;
/* 1887 */       return new OceanMonumentPieces.OceanMonumentDoubleXRoom(orientation, definition);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FitDoubleZRoom
/*      */     implements MonumentRoomFitter
/*      */   {
/* 1894 */     public boolean fits(OceanMonumentPieces.RoomDefinition definition) { return (definition.hasOpening[Direction.NORTH.get3DDataValue()] && !(definition.connections[Direction.NORTH.get3DDataValue()]).claimed); }
/*      */ 
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction orientation, OceanMonumentPieces.RoomDefinition definition, RandomSource random) {
/* 1899 */       OceanMonumentPieces.RoomDefinition source = definition;
/* 1900 */       if (!definition.hasOpening[Direction.NORTH.get3DDataValue()] || (definition.connections[Direction.NORTH.get3DDataValue()]).claimed) {
/* 1901 */         source = definition.connections[Direction.SOUTH.get3DDataValue()];
/*      */       }
/* 1903 */       source.claimed = true;
/* 1904 */       (source.connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/* 1905 */       return new OceanMonumentPieces.OceanMonumentDoubleZRoom(orientation, source);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FitDoubleXYRoom
/*      */     implements MonumentRoomFitter {
/*      */     public boolean fits(OceanMonumentPieces.RoomDefinition definition) {
/* 1912 */       if (definition.hasOpening[Direction.EAST.get3DDataValue()] && !(definition.connections[Direction.EAST.get3DDataValue()]).claimed && 
/* 1913 */         definition.hasOpening[Direction.UP.get3DDataValue()] && !(definition.connections[Direction.UP.get3DDataValue()]).claimed) {
/* 1914 */         OceanMonumentPieces.RoomDefinition east = definition.connections[Direction.EAST.get3DDataValue()];
/*      */         
/* 1916 */         return (east.hasOpening[Direction.UP.get3DDataValue()] && !(east.connections[Direction.UP.get3DDataValue()]).claimed);
/*      */       } 
/*      */       
/* 1919 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction orientation, OceanMonumentPieces.RoomDefinition definition, RandomSource random) {
/* 1924 */       definition.claimed = true;
/* 1925 */       (definition.connections[Direction.EAST.get3DDataValue()]).claimed = true;
/* 1926 */       (definition.connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1927 */       ((definition.connections[Direction.EAST.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1928 */       return new OceanMonumentPieces.OceanMonumentDoubleXYRoom(orientation, definition);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FitDoubleYZRoom
/*      */     implements MonumentRoomFitter {
/*      */     public boolean fits(OceanMonumentPieces.RoomDefinition definition) {
/* 1935 */       if (definition.hasOpening[Direction.NORTH.get3DDataValue()] && !(definition.connections[Direction.NORTH.get3DDataValue()]).claimed && 
/* 1936 */         definition.hasOpening[Direction.UP.get3DDataValue()] && !(definition.connections[Direction.UP.get3DDataValue()]).claimed) {
/* 1937 */         OceanMonumentPieces.RoomDefinition north = definition.connections[Direction.NORTH.get3DDataValue()];
/*      */         
/* 1939 */         return (north.hasOpening[Direction.UP.get3DDataValue()] && !(north.connections[Direction.UP.get3DDataValue()]).claimed);
/*      */       } 
/*      */       
/* 1942 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public OceanMonumentPieces.OceanMonumentPiece create(Direction orientation, OceanMonumentPieces.RoomDefinition definition, RandomSource random) {
/* 1947 */       definition.claimed = true;
/* 1948 */       (definition.connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/* 1949 */       (definition.connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1950 */       ((definition.connections[Direction.NORTH.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 1951 */       return new OceanMonumentPieces.OceanMonumentDoubleYZRoom(orientation, definition);
/*      */     }
/*      */   }
/*      */   
/*      */   private static interface MonumentRoomFitter {
/*      */     boolean fits(OceanMonumentPieces.RoomDefinition param1RoomDefinition);
/*      */     
/*      */     OceanMonumentPieces.OceanMonumentPiece create(Direction param1Direction, OceanMonumentPieces.RoomDefinition param1RoomDefinition, RandomSource param1RandomSource);
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */