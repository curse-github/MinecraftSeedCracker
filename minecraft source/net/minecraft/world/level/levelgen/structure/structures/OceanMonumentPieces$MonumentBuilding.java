/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
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
/*     */ public class MonumentBuilding
/*     */   extends OceanMonumentPieces.OceanMonumentPiece
/*     */ {
/*     */   private static final int WIDTH = 58;
/*     */   private static final int HEIGHT = 22;
/*     */   private static final int DEPTH = 58;
/*     */   public static final int BIOME_RANGE_CHECK = 29;
/*     */   private static final int TOP_POSITION = 61;
/*     */   private OceanMonumentPieces.RoomDefinition sourceRoom;
/*     */   private OceanMonumentPieces.RoomDefinition coreRoom;
/* 209 */   private final List<OceanMonumentPieces.OceanMonumentPiece> childPieces = Lists.newArrayList();
/*     */   
/*     */   public MonumentBuilding(RandomSource random, int west, int north, Direction direction) {
/* 212 */     super(StructurePieceType.OCEAN_MONUMENT_BUILDING, direction, 0, makeBoundingBox(west, 39, north, direction, 58, 23, 58));
/*     */     
/* 214 */     setOrientation(direction);
/*     */     
/* 216 */     List<OceanMonumentPieces.RoomDefinition> roomDefinitions = generateRoomGraph(random);
/*     */     
/* 218 */     this.sourceRoom.claimed = true;
/* 219 */     this.childPieces.add(new OceanMonumentPieces.OceanMonumentEntryRoom(direction, this.sourceRoom));
/* 220 */     this.childPieces.add(new OceanMonumentPieces.OceanMonumentCoreRoom(direction, this.coreRoom));
/*     */     
/* 222 */     List<OceanMonumentPieces.MonumentRoomFitter> fitters = Lists.newArrayList();
/* 223 */     fitters.add(new OceanMonumentPieces.FitDoubleXYRoom());
/* 224 */     fitters.add(new OceanMonumentPieces.FitDoubleYZRoom());
/* 225 */     fitters.add(new OceanMonumentPieces.FitDoubleZRoom());
/* 226 */     fitters.add(new OceanMonumentPieces.FitDoubleXRoom());
/* 227 */     fitters.add(new OceanMonumentPieces.FitDoubleYRoom());
/* 228 */     fitters.add(new OceanMonumentPieces.FitSimpleTopRoom());
/* 229 */     fitters.add(new OceanMonumentPieces.FitSimpleRoom());
/*     */     
/* 231 */     for (OceanMonumentPieces.RoomDefinition definition : roomDefinitions) {
/* 232 */       if (!definition.claimed && !definition.isSpecial())
/*     */       {
/* 234 */         for (OceanMonumentPieces.MonumentRoomFitter fitter : fitters) {
/* 235 */           if (fitter.fits(definition)) {
/* 236 */             this.childPieces.add(fitter.create(direction, definition, random));
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 244 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(9, 0, 22);
/* 245 */     for (OceanMonumentPieces.OceanMonumentPiece child : this.childPieces) {
/* 246 */       child.getBoundingBox().move(mutableBlockPos);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 251 */     BoundingBox leftWing = BoundingBox.fromCorners(getWorldPos(1, 1, 1), getWorldPos(23, 8, 21));
/* 252 */     BoundingBox rightWing = BoundingBox.fromCorners(getWorldPos(34, 1, 1), getWorldPos(56, 8, 21));
/* 253 */     BoundingBox penthouse = BoundingBox.fromCorners(getWorldPos(22, 13, 22), getWorldPos(35, 17, 35));
/*     */ 
/*     */     
/* 256 */     int wingRandom = random.nextInt();
/* 257 */     this.childPieces.add(new OceanMonumentPieces.OceanMonumentWingRoom(direction, leftWing, wingRandom++));
/* 258 */     this.childPieces.add(new OceanMonumentPieces.OceanMonumentWingRoom(direction, rightWing, wingRandom++));
/*     */     
/* 260 */     this.childPieces.add(new OceanMonumentPieces.OceanMonumentPenthouse(direction, penthouse));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 265 */   public MonumentBuilding(CompoundTag tag) { super(StructurePieceType.OCEAN_MONUMENT_BUILDING, tag); }
/*     */ 
/*     */   
/*     */   private List<OceanMonumentPieces.RoomDefinition> generateRoomGraph(RandomSource random) {
/* 269 */     OceanMonumentPieces.RoomDefinition[] arrayOfRoomDefinition = new OceanMonumentPieces.RoomDefinition[75];
/*     */     
/* 271 */     for (int x = 0; x < 5; x++) {
/* 272 */       for (int z = 0; z < 4; z++) {
/* 273 */         int y = 0;
/* 274 */         int pos = getRoomIndex(x, 0, z);
/* 275 */         arrayOfRoomDefinition[pos] = new OceanMonumentPieces.RoomDefinition(pos);
/*     */       } 
/*     */     } 
/* 278 */     for (int x = 0; x < 5; x++) {
/* 279 */       for (int z = 0; z < 4; z++) {
/* 280 */         int y = 1;
/* 281 */         int pos = getRoomIndex(x, 1, z);
/* 282 */         arrayOfRoomDefinition[pos] = new OceanMonumentPieces.RoomDefinition(pos);
/*     */       } 
/*     */     } 
/* 285 */     for (int x = 1; x < 4; x++) {
/* 286 */       for (int z = 0; z < 2; z++) {
/* 287 */         int y = 2;
/* 288 */         int pos = getRoomIndex(x, 2, z);
/* 289 */         arrayOfRoomDefinition[pos] = new OceanMonumentPieces.RoomDefinition(pos);
/*     */       } 
/*     */     } 
/*     */     
/* 293 */     this.sourceRoom = arrayOfRoomDefinition[GRIDROOM_SOURCE_INDEX];
/*     */     
/* 295 */     for (int x = 0; x < 5; x++) {
/* 296 */       for (int z = 0; z < 5; z++) {
/* 297 */         for (int y = 0; y < 3; y++) {
/* 298 */           int pos = getRoomIndex(x, y, z);
/* 299 */           if (arrayOfRoomDefinition[pos] != null)
/*     */           {
/*     */             
/* 302 */             for (Direction direction : Direction.values()) {
/* 303 */               int neighX = x + direction.getStepX();
/* 304 */               int neighY = y + direction.getStepY();
/* 305 */               int neighZ = z + direction.getStepZ();
/* 306 */               if (neighX >= 0 && neighX < 5 && neighZ >= 0 && neighZ < 5 && neighY >= 0 && neighY < 3) {
/* 307 */                 int neighPos = getRoomIndex(neighX, neighY, neighZ);
/* 308 */                 if (arrayOfRoomDefinition[neighPos] != null)
/*     */                 {
/*     */                   
/* 311 */                   if (neighZ == z) {
/* 312 */                     arrayOfRoomDefinition[pos].setConnection(direction, arrayOfRoomDefinition[neighPos]);
/*     */                   } else {
/* 314 */                     arrayOfRoomDefinition[pos].setConnection(direction.getOpposite(), arrayOfRoomDefinition[neighPos]);
/*     */                   }  } 
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 322 */     OceanMonumentPieces.RoomDefinition roofRoom = new OceanMonumentPieces.RoomDefinition(1003);
/* 323 */     OceanMonumentPieces.RoomDefinition leftWing = new OceanMonumentPieces.RoomDefinition(1001);
/* 324 */     OceanMonumentPieces.RoomDefinition rightWing = new OceanMonumentPieces.RoomDefinition(1002);
/* 325 */     arrayOfRoomDefinition[GRIDROOM_TOP_CONNECT_INDEX].setConnection(Direction.UP, roofRoom);
/* 326 */     arrayOfRoomDefinition[GRIDROOM_LEFTWING_CONNECT_INDEX].setConnection(Direction.SOUTH, leftWing);
/* 327 */     arrayOfRoomDefinition[GRIDROOM_RIGHTWING_CONNECT_INDEX].setConnection(Direction.SOUTH, rightWing);
/* 328 */     roofRoom.claimed = true;
/* 329 */     leftWing.claimed = true;
/* 330 */     rightWing.claimed = true;
/* 331 */     this.sourceRoom.isSource = true;
/*     */ 
/*     */     
/* 334 */     this.coreRoom = arrayOfRoomDefinition[getRoomIndex(random.nextInt(4), 0, 2)];
/* 335 */     this.coreRoom.claimed = true;
/* 336 */     (this.coreRoom.connections[Direction.EAST.get3DDataValue()]).claimed = true;
/* 337 */     (this.coreRoom.connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/* 338 */     ((this.coreRoom.connections[Direction.EAST.get3DDataValue()]).connections[Direction.NORTH.get3DDataValue()]).claimed = true;
/* 339 */     (this.coreRoom.connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 340 */     ((this.coreRoom.connections[Direction.EAST.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 341 */     ((this.coreRoom.connections[Direction.NORTH.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/* 342 */     (((this.coreRoom.connections[Direction.EAST.get3DDataValue()]).connections[Direction.NORTH.get3DDataValue()]).connections[Direction.UP.get3DDataValue()]).claimed = true;
/*     */     
/* 344 */     ObjectArrayList<OceanMonumentPieces.RoomDefinition> roomDefs = new ObjectArrayList<OceanMonumentPieces.RoomDefinition>();
/* 345 */     for (OceanMonumentPieces.RoomDefinition definition : arrayOfRoomDefinition) {
/* 346 */       if (definition != null) {
/* 347 */         definition.updateOpenings();
/* 348 */         roomDefs.add(definition);
/*     */       } 
/*     */     } 
/* 351 */     roofRoom.updateOpenings();
/*     */     
/* 353 */     Util.shuffle(roomDefs, random);
/* 354 */     int scanIndex = 1;
/* 355 */     for (ObjectListIterator objectListIterator = roomDefs.iterator(); objectListIterator.hasNext(); ) { OceanMonumentPieces.RoomDefinition definition = (OceanMonumentPieces.RoomDefinition)objectListIterator.next();
/*     */       
/* 357 */       int closeCount = 0;
/* 358 */       int attemptCount = 0;
/* 359 */       while (closeCount < 2 && attemptCount < 5) {
/* 360 */         attemptCount++;
/*     */         
/* 362 */         int f = random.nextInt(6);
/* 363 */         if (definition.hasOpening[f]) {
/* 364 */           int of = Direction.from3DDataValue(f).getOpposite().get3DDataValue();
/*     */ 
/*     */           
/* 367 */           definition.hasOpening[f] = false;
/* 368 */           (definition.connections[f]).hasOpening[of] = false;
/*     */           
/* 370 */           if (definition.findSource(scanIndex++) && definition.connections[f].findSource(scanIndex++)) {
/* 371 */             closeCount++;
/*     */             continue;
/*     */           } 
/* 374 */           definition.hasOpening[f] = true;
/* 375 */           (definition.connections[f]).hasOpening[of] = true;
/*     */         } 
/*     */       }  }
/*     */ 
/*     */     
/* 380 */     roomDefs.add(roofRoom);
/* 381 */     roomDefs.add(leftWing);
/* 382 */     roomDefs.add(rightWing);
/*     */     
/* 384 */     return roomDefs;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 389 */     int waterHeight = Math.max(level.getSeaLevel(), 64) - this.boundingBox.minY();
/*     */     
/* 391 */     generateWaterBox(level, chunkBB, 0, 0, 0, 58, waterHeight, 58);
/*     */ 
/*     */     
/* 394 */     generateWing(false, 0, level, random, chunkBB);
/*     */ 
/*     */     
/* 397 */     generateWing(true, 33, level, random, chunkBB);
/*     */ 
/*     */     
/* 400 */     generateEntranceArchs(level, random, chunkBB);
/*     */     
/* 402 */     generateEntranceWall(level, random, chunkBB);
/* 403 */     generateRoofPiece(level, random, chunkBB);
/*     */     
/* 405 */     generateLowerWall(level, random, chunkBB);
/* 406 */     generateMiddleWall(level, random, chunkBB);
/* 407 */     generateUpperWall(level, random, chunkBB);
/*     */ 
/*     */     
/* 410 */     for (int pillarX = 0; pillarX < 7; pillarX++) {
/* 411 */       for (int pillarZ = 0; pillarZ < 7; ) {
/* 412 */         if (pillarZ == 0 && pillarX == 3)
/*     */         {
/* 414 */           pillarZ = 6;
/*     */         }
/*     */         
/* 417 */         int bx = pillarX * 9;
/* 418 */         int bz = pillarZ * 9;
/* 419 */         for (int w = 0; w < 4; w++) {
/* 420 */           for (int d = 0; d < 4; d++) {
/* 421 */             placeBlock(level, BASE_LIGHT, bx + w, 0, bz + d, chunkBB);
/* 422 */             fillColumnDown(level, BASE_LIGHT, bx + w, -1, bz + d, chunkBB);
/*     */           } 
/*     */         } 
/*     */         
/* 426 */         if (pillarX == 0 || pillarX == 6) {
/* 427 */           pillarZ++; continue;
/*     */         } 
/* 429 */         pillarZ += 6;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 435 */     for (int i = 0; i < 5; i++) {
/* 436 */       generateWaterBox(level, chunkBB, -1 - i, 0 + i * 2, -1 - i, -1 - i, 23, 58 + i);
/* 437 */       generateWaterBox(level, chunkBB, 58 + i, 0 + i * 2, -1 - i, 58 + i, 23, 58 + i);
/* 438 */       generateWaterBox(level, chunkBB, 0 - i, 0 + i * 2, -1 - i, 57 + i, 23, -1 - i);
/* 439 */       generateWaterBox(level, chunkBB, 0 - i, 0 + i * 2, 58 + i, 57 + i, 23, 58 + i);
/*     */     } 
/*     */     
/* 442 */     for (OceanMonumentPieces.OceanMonumentPiece child : this.childPieces) {
/* 443 */       if (child.getBoundingBox().intersects(chunkBB)) {
/* 444 */         child.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void generateWing(boolean isFlipped, int xoff, WorldGenLevel level, RandomSource random, BoundingBox chunkBB) {
/* 451 */     int sectionWidth = 24;
/* 452 */     if (chunkIntersects(chunkBB, xoff, 0, xoff + 23, 20)) {
/* 453 */       generateBox(level, chunkBB, xoff + 0, 0, 0, xoff + 24, 0, 20, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 455 */       generateWaterBox(level, chunkBB, xoff + 0, 1, 0, xoff + 24, 10, 20);
/*     */       
/* 457 */       for (int i = 0; i < 4; i++) {
/* 458 */         generateBox(level, chunkBB, xoff + i, i + 1, i, xoff + i, i + 1, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 459 */         generateBox(level, chunkBB, xoff + i + 7, i + 5, i + 7, xoff + i + 7, i + 5, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 460 */         generateBox(level, chunkBB, xoff + 17 - i, i + 5, i + 7, xoff + 17 - i, i + 5, 20, BASE_LIGHT, BASE_LIGHT, false);
/* 461 */         generateBox(level, chunkBB, xoff + 24 - i, i + 1, i, xoff + 24 - i, i + 1, 20, BASE_LIGHT, BASE_LIGHT, false);
/*     */         
/* 463 */         generateBox(level, chunkBB, xoff + i + 1, i + 1, i, xoff + 23 - i, i + 1, i, BASE_LIGHT, BASE_LIGHT, false);
/* 464 */         generateBox(level, chunkBB, xoff + i + 8, i + 5, i + 7, xoff + 16 - i, i + 5, i + 7, BASE_LIGHT, BASE_LIGHT, false);
/*     */       } 
/* 466 */       generateBox(level, chunkBB, xoff + 4, 4, 4, xoff + 6, 4, 20, BASE_GRAY, BASE_GRAY, false);
/* 467 */       generateBox(level, chunkBB, xoff + 7, 4, 4, xoff + 17, 4, 6, BASE_GRAY, BASE_GRAY, false);
/* 468 */       generateBox(level, chunkBB, xoff + 18, 4, 4, xoff + 20, 4, 20, BASE_GRAY, BASE_GRAY, false);
/* 469 */       generateBox(level, chunkBB, xoff + 11, 8, 11, xoff + 13, 8, 20, BASE_GRAY, BASE_GRAY, false);
/* 470 */       placeBlock(level, DOT_DECO_DATA, xoff + 12, 9, 12, chunkBB);
/* 471 */       placeBlock(level, DOT_DECO_DATA, xoff + 12, 9, 15, chunkBB);
/* 472 */       placeBlock(level, DOT_DECO_DATA, xoff + 12, 9, 18, chunkBB);
/*     */       
/* 474 */       int leftPos = xoff + (isFlipped ? 19 : 5);
/* 475 */       int rightPos = xoff + (isFlipped ? 5 : 19);
/* 476 */       for (int z = 20; z >= 5; z -= 3) {
/* 477 */         placeBlock(level, DOT_DECO_DATA, leftPos, 5, z, chunkBB);
/*     */       }
/* 479 */       for (int z = 19; z >= 7; z -= 3) {
/* 480 */         placeBlock(level, DOT_DECO_DATA, rightPos, 5, z, chunkBB);
/*     */       }
/* 482 */       for (int i = 0; i < 4; i++) {
/* 483 */         int pos = isFlipped ? (xoff + 24 - 17 - i * 3) : (xoff + 17 - i * 3);
/* 484 */         placeBlock(level, DOT_DECO_DATA, pos, 5, 5, chunkBB);
/*     */       } 
/* 486 */       placeBlock(level, DOT_DECO_DATA, rightPos, 5, 5, chunkBB);
/*     */ 
/*     */       
/* 489 */       generateBox(level, chunkBB, xoff + 11, 1, 12, xoff + 13, 7, 12, BASE_GRAY, BASE_GRAY, false);
/* 490 */       generateBox(level, chunkBB, xoff + 12, 1, 11, xoff + 12, 7, 13, BASE_GRAY, BASE_GRAY, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void generateEntranceArchs(WorldGenLevel level, RandomSource random, BoundingBox chunkBB) {
/* 496 */     if (chunkIntersects(chunkBB, 22, 5, 35, 17)) {
/*     */       
/* 498 */       generateWaterBox(level, chunkBB, 25, 0, 0, 32, 8, 20);
/*     */ 
/*     */       
/* 501 */       for (int i = 0; i < 4; i++) {
/* 502 */         generateBox(level, chunkBB, 24, 2, 5 + i * 4, 24, 4, 5 + i * 4, BASE_LIGHT, BASE_LIGHT, false);
/* 503 */         generateBox(level, chunkBB, 22, 4, 5 + i * 4, 23, 4, 5 + i * 4, BASE_LIGHT, BASE_LIGHT, false);
/* 504 */         placeBlock(level, BASE_LIGHT, 25, 5, 5 + i * 4, chunkBB);
/* 505 */         placeBlock(level, BASE_LIGHT, 26, 6, 5 + i * 4, chunkBB);
/* 506 */         placeBlock(level, LAMP_BLOCK, 26, 5, 5 + i * 4, chunkBB);
/*     */         
/* 508 */         generateBox(level, chunkBB, 33, 2, 5 + i * 4, 33, 4, 5 + i * 4, BASE_LIGHT, BASE_LIGHT, false);
/* 509 */         generateBox(level, chunkBB, 34, 4, 5 + i * 4, 35, 4, 5 + i * 4, BASE_LIGHT, BASE_LIGHT, false);
/* 510 */         placeBlock(level, BASE_LIGHT, 32, 5, 5 + i * 4, chunkBB);
/* 511 */         placeBlock(level, BASE_LIGHT, 31, 6, 5 + i * 4, chunkBB);
/* 512 */         placeBlock(level, LAMP_BLOCK, 31, 5, 5 + i * 4, chunkBB);
/*     */         
/* 514 */         generateBox(level, chunkBB, 27, 6, 5 + i * 4, 30, 6, 5 + i * 4, BASE_GRAY, BASE_GRAY, false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateEntranceWall(WorldGenLevel level, RandomSource random, BoundingBox chunkBB) {
/* 522 */     if (chunkIntersects(chunkBB, 15, 20, 42, 21)) {
/* 523 */       generateBox(level, chunkBB, 15, 0, 21, 42, 0, 21, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 525 */       generateWaterBox(level, chunkBB, 26, 1, 21, 31, 3, 21);
/*     */ 
/*     */ 
/*     */       
/* 529 */       generateBox(level, chunkBB, 21, 12, 21, 36, 12, 21, BASE_GRAY, BASE_GRAY, false);
/* 530 */       generateBox(level, chunkBB, 17, 11, 21, 40, 11, 21, BASE_GRAY, BASE_GRAY, false);
/* 531 */       generateBox(level, chunkBB, 16, 10, 21, 41, 10, 21, BASE_GRAY, BASE_GRAY, false);
/* 532 */       generateBox(level, chunkBB, 15, 7, 21, 42, 9, 21, BASE_GRAY, BASE_GRAY, false);
/* 533 */       generateBox(level, chunkBB, 16, 6, 21, 41, 6, 21, BASE_GRAY, BASE_GRAY, false);
/* 534 */       generateBox(level, chunkBB, 17, 5, 21, 40, 5, 21, BASE_GRAY, BASE_GRAY, false);
/* 535 */       generateBox(level, chunkBB, 21, 4, 21, 36, 4, 21, BASE_GRAY, BASE_GRAY, false);
/* 536 */       generateBox(level, chunkBB, 22, 3, 21, 26, 3, 21, BASE_GRAY, BASE_GRAY, false);
/* 537 */       generateBox(level, chunkBB, 31, 3, 21, 35, 3, 21, BASE_GRAY, BASE_GRAY, false);
/* 538 */       generateBox(level, chunkBB, 23, 2, 21, 25, 2, 21, BASE_GRAY, BASE_GRAY, false);
/* 539 */       generateBox(level, chunkBB, 32, 2, 21, 34, 2, 21, BASE_GRAY, BASE_GRAY, false);
/*     */ 
/*     */       
/* 542 */       generateBox(level, chunkBB, 28, 4, 20, 29, 4, 21, BASE_LIGHT, BASE_LIGHT, false);
/* 543 */       placeBlock(level, BASE_LIGHT, 27, 3, 21, chunkBB);
/* 544 */       placeBlock(level, BASE_LIGHT, 30, 3, 21, chunkBB);
/* 545 */       placeBlock(level, BASE_LIGHT, 26, 2, 21, chunkBB);
/* 546 */       placeBlock(level, BASE_LIGHT, 31, 2, 21, chunkBB);
/* 547 */       placeBlock(level, BASE_LIGHT, 25, 1, 21, chunkBB);
/* 548 */       placeBlock(level, BASE_LIGHT, 32, 1, 21, chunkBB);
/* 549 */       for (int i = 0; i < 7; i++) {
/* 550 */         placeBlock(level, BASE_BLACK, 28 - i, 6 + i, 21, chunkBB);
/* 551 */         placeBlock(level, BASE_BLACK, 29 + i, 6 + i, 21, chunkBB);
/*     */       } 
/* 553 */       for (int i = 0; i < 4; i++) {
/* 554 */         placeBlock(level, BASE_BLACK, 28 - i, 9 + i, 21, chunkBB);
/* 555 */         placeBlock(level, BASE_BLACK, 29 + i, 9 + i, 21, chunkBB);
/*     */       } 
/* 557 */       placeBlock(level, BASE_BLACK, 28, 12, 21, chunkBB);
/* 558 */       placeBlock(level, BASE_BLACK, 29, 12, 21, chunkBB);
/* 559 */       for (int i = 0; i < 3; i++) {
/* 560 */         placeBlock(level, BASE_BLACK, 22 - i * 2, 8, 21, chunkBB);
/* 561 */         placeBlock(level, BASE_BLACK, 22 - i * 2, 9, 21, chunkBB);
/*     */         
/* 563 */         placeBlock(level, BASE_BLACK, 35 + i * 2, 8, 21, chunkBB);
/* 564 */         placeBlock(level, BASE_BLACK, 35 + i * 2, 9, 21, chunkBB);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 569 */       generateWaterBox(level, chunkBB, 15, 13, 21, 42, 15, 21);
/* 570 */       generateWaterBox(level, chunkBB, 15, 1, 21, 15, 6, 21);
/* 571 */       generateWaterBox(level, chunkBB, 16, 1, 21, 16, 5, 21);
/* 572 */       generateWaterBox(level, chunkBB, 17, 1, 21, 20, 4, 21);
/* 573 */       generateWaterBox(level, chunkBB, 21, 1, 21, 21, 3, 21);
/* 574 */       generateWaterBox(level, chunkBB, 22, 1, 21, 22, 2, 21);
/* 575 */       generateWaterBox(level, chunkBB, 23, 1, 21, 24, 1, 21);
/* 576 */       generateWaterBox(level, chunkBB, 42, 1, 21, 42, 6, 21);
/* 577 */       generateWaterBox(level, chunkBB, 41, 1, 21, 41, 5, 21);
/* 578 */       generateWaterBox(level, chunkBB, 37, 1, 21, 40, 4, 21);
/* 579 */       generateWaterBox(level, chunkBB, 36, 1, 21, 36, 3, 21);
/* 580 */       generateWaterBox(level, chunkBB, 33, 1, 21, 34, 1, 21);
/* 581 */       generateWaterBox(level, chunkBB, 35, 1, 21, 35, 2, 21);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateRoofPiece(WorldGenLevel level, RandomSource random, BoundingBox chunkBB) {
/* 589 */     if (chunkIntersects(chunkBB, 21, 21, 36, 36)) {
/* 590 */       generateBox(level, chunkBB, 21, 0, 22, 36, 0, 36, BASE_GRAY, BASE_GRAY, false);
/*     */ 
/*     */ 
/*     */       
/* 594 */       generateWaterBox(level, chunkBB, 21, 1, 22, 36, 23, 36);
/*     */ 
/*     */       
/* 597 */       for (int i = 0; i < 4; i++) {
/* 598 */         generateBox(level, chunkBB, 21 + i, 13 + i, 21 + i, 36 - i, 13 + i, 21 + i, BASE_LIGHT, BASE_LIGHT, false);
/* 599 */         generateBox(level, chunkBB, 21 + i, 13 + i, 36 - i, 36 - i, 13 + i, 36 - i, BASE_LIGHT, BASE_LIGHT, false);
/* 600 */         generateBox(level, chunkBB, 21 + i, 13 + i, 22 + i, 21 + i, 13 + i, 35 - i, BASE_LIGHT, BASE_LIGHT, false);
/* 601 */         generateBox(level, chunkBB, 36 - i, 13 + i, 22 + i, 36 - i, 13 + i, 35 - i, BASE_LIGHT, BASE_LIGHT, false);
/*     */       } 
/* 603 */       generateBox(level, chunkBB, 25, 16, 25, 32, 16, 32, BASE_GRAY, BASE_GRAY, false);
/* 604 */       generateBox(level, chunkBB, 25, 17, 25, 25, 19, 25, BASE_LIGHT, BASE_LIGHT, false);
/* 605 */       generateBox(level, chunkBB, 32, 17, 25, 32, 19, 25, BASE_LIGHT, BASE_LIGHT, false);
/* 606 */       generateBox(level, chunkBB, 25, 17, 32, 25, 19, 32, BASE_LIGHT, BASE_LIGHT, false);
/* 607 */       generateBox(level, chunkBB, 32, 17, 32, 32, 19, 32, BASE_LIGHT, BASE_LIGHT, false);
/*     */       
/* 609 */       placeBlock(level, BASE_LIGHT, 26, 20, 26, chunkBB);
/* 610 */       placeBlock(level, BASE_LIGHT, 27, 21, 27, chunkBB);
/* 611 */       placeBlock(level, LAMP_BLOCK, 27, 20, 27, chunkBB);
/* 612 */       placeBlock(level, BASE_LIGHT, 26, 20, 31, chunkBB);
/* 613 */       placeBlock(level, BASE_LIGHT, 27, 21, 30, chunkBB);
/* 614 */       placeBlock(level, LAMP_BLOCK, 27, 20, 30, chunkBB);
/* 615 */       placeBlock(level, BASE_LIGHT, 31, 20, 31, chunkBB);
/* 616 */       placeBlock(level, BASE_LIGHT, 30, 21, 30, chunkBB);
/* 617 */       placeBlock(level, LAMP_BLOCK, 30, 20, 30, chunkBB);
/* 618 */       placeBlock(level, BASE_LIGHT, 31, 20, 26, chunkBB);
/* 619 */       placeBlock(level, BASE_LIGHT, 30, 21, 27, chunkBB);
/* 620 */       placeBlock(level, LAMP_BLOCK, 30, 20, 27, chunkBB);
/*     */       
/* 622 */       generateBox(level, chunkBB, 28, 21, 27, 29, 21, 27, BASE_GRAY, BASE_GRAY, false);
/* 623 */       generateBox(level, chunkBB, 27, 21, 28, 27, 21, 29, BASE_GRAY, BASE_GRAY, false);
/* 624 */       generateBox(level, chunkBB, 28, 21, 30, 29, 21, 30, BASE_GRAY, BASE_GRAY, false);
/* 625 */       generateBox(level, chunkBB, 30, 21, 28, 30, 21, 29, BASE_GRAY, BASE_GRAY, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateLowerWall(WorldGenLevel level, RandomSource random, BoundingBox chunkBB) {
/* 632 */     if (chunkIntersects(chunkBB, 0, 21, 6, 58)) {
/* 633 */       generateBox(level, chunkBB, 0, 0, 21, 6, 0, 57, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 635 */       generateWaterBox(level, chunkBB, 0, 1, 21, 6, 7, 57);
/*     */ 
/*     */       
/* 638 */       generateBox(level, chunkBB, 4, 4, 21, 6, 4, 53, BASE_GRAY, BASE_GRAY, false);
/* 639 */       for (int i = 0; i < 4; i++) {
/* 640 */         generateBox(level, chunkBB, i, i + 1, 21, i, i + 1, 57 - i, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/* 642 */       for (int z = 23; z < 53; z += 3) {
/* 643 */         placeBlock(level, DOT_DECO_DATA, 5, 5, z, chunkBB);
/*     */       }
/* 645 */       placeBlock(level, DOT_DECO_DATA, 5, 5, 52, chunkBB);
/*     */       
/* 647 */       for (int i = 0; i < 4; i++) {
/* 648 */         generateBox(level, chunkBB, i, i + 1, 21, i, i + 1, 57 - i, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/*     */       
/* 651 */       generateBox(level, chunkBB, 4, 1, 52, 6, 3, 52, BASE_GRAY, BASE_GRAY, false);
/* 652 */       generateBox(level, chunkBB, 5, 1, 51, 5, 3, 53, BASE_GRAY, BASE_GRAY, false);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 657 */     if (chunkIntersects(chunkBB, 51, 21, 58, 58)) {
/* 658 */       generateBox(level, chunkBB, 51, 0, 21, 57, 0, 57, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 660 */       generateWaterBox(level, chunkBB, 51, 1, 21, 57, 7, 57);
/*     */ 
/*     */       
/* 663 */       generateBox(level, chunkBB, 51, 4, 21, 53, 4, 53, BASE_GRAY, BASE_GRAY, false);
/* 664 */       for (int i = 0; i < 4; i++) {
/* 665 */         generateBox(level, chunkBB, 57 - i, i + 1, 21, 57 - i, i + 1, 57 - i, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/* 667 */       for (int z = 23; z < 53; z += 3) {
/* 668 */         placeBlock(level, DOT_DECO_DATA, 52, 5, z, chunkBB);
/*     */       }
/* 670 */       placeBlock(level, DOT_DECO_DATA, 52, 5, 52, chunkBB);
/*     */ 
/*     */       
/* 673 */       generateBox(level, chunkBB, 51, 1, 52, 53, 3, 52, BASE_GRAY, BASE_GRAY, false);
/* 674 */       generateBox(level, chunkBB, 52, 1, 51, 52, 3, 53, BASE_GRAY, BASE_GRAY, false);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 679 */     if (chunkIntersects(chunkBB, 0, 51, 57, 57)) {
/* 680 */       generateBox(level, chunkBB, 7, 0, 51, 50, 0, 57, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 682 */       generateWaterBox(level, chunkBB, 7, 1, 51, 50, 10, 57);
/*     */ 
/*     */       
/* 685 */       for (int i = 0; i < 4; i++) {
/* 686 */         generateBox(level, chunkBB, i + 1, i + 1, 57 - i, 56 - i, i + 1, 57 - i, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateMiddleWall(WorldGenLevel level, RandomSource random, BoundingBox chunkBB) {
/* 694 */     if (chunkIntersects(chunkBB, 7, 21, 13, 50)) {
/* 695 */       generateBox(level, chunkBB, 7, 0, 21, 13, 0, 50, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 697 */       generateWaterBox(level, chunkBB, 7, 1, 21, 13, 10, 50);
/*     */ 
/*     */       
/* 700 */       generateBox(level, chunkBB, 11, 8, 21, 13, 8, 53, BASE_GRAY, BASE_GRAY, false);
/* 701 */       for (int i = 0; i < 4; i++) {
/* 702 */         generateBox(level, chunkBB, i + 7, i + 5, 21, i + 7, i + 5, 54, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/* 704 */       for (int z = 21; z <= 45; z += 3) {
/* 705 */         placeBlock(level, DOT_DECO_DATA, 12, 9, z, chunkBB);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 711 */     if (chunkIntersects(chunkBB, 44, 21, 50, 54)) {
/* 712 */       generateBox(level, chunkBB, 44, 0, 21, 50, 0, 50, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 714 */       generateWaterBox(level, chunkBB, 44, 1, 21, 50, 10, 50);
/*     */ 
/*     */       
/* 717 */       generateBox(level, chunkBB, 44, 8, 21, 46, 8, 53, BASE_GRAY, BASE_GRAY, false);
/* 718 */       for (int i = 0; i < 4; i++) {
/* 719 */         generateBox(level, chunkBB, 50 - i, i + 5, 21, 50 - i, i + 5, 54, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/* 721 */       for (int z = 21; z <= 45; z += 3) {
/* 722 */         placeBlock(level, DOT_DECO_DATA, 45, 9, z, chunkBB);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 728 */     if (chunkIntersects(chunkBB, 8, 44, 49, 54)) {
/* 729 */       generateBox(level, chunkBB, 14, 0, 44, 43, 0, 50, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 731 */       generateWaterBox(level, chunkBB, 14, 1, 44, 43, 10, 50);
/*     */ 
/*     */       
/* 734 */       for (int x = 12; x <= 45; x += 3) {
/* 735 */         placeBlock(level, DOT_DECO_DATA, x, 9, 45, chunkBB);
/* 736 */         placeBlock(level, DOT_DECO_DATA, x, 9, 52, chunkBB);
/* 737 */         if (x == 12 || x == 18 || x == 24 || x == 33 || x == 39 || x == 45) {
/* 738 */           placeBlock(level, DOT_DECO_DATA, x, 9, 47, chunkBB);
/* 739 */           placeBlock(level, DOT_DECO_DATA, x, 9, 50, chunkBB);
/* 740 */           placeBlock(level, DOT_DECO_DATA, x, 10, 45, chunkBB);
/* 741 */           placeBlock(level, DOT_DECO_DATA, x, 10, 46, chunkBB);
/* 742 */           placeBlock(level, DOT_DECO_DATA, x, 10, 51, chunkBB);
/* 743 */           placeBlock(level, DOT_DECO_DATA, x, 10, 52, chunkBB);
/* 744 */           placeBlock(level, DOT_DECO_DATA, x, 11, 47, chunkBB);
/* 745 */           placeBlock(level, DOT_DECO_DATA, x, 11, 50, chunkBB);
/* 746 */           placeBlock(level, DOT_DECO_DATA, x, 12, 48, chunkBB);
/* 747 */           placeBlock(level, DOT_DECO_DATA, x, 12, 49, chunkBB);
/*     */         } 
/*     */       } 
/*     */       
/* 751 */       for (int i = 0; i < 3; i++) {
/* 752 */         generateBox(level, chunkBB, 8 + i, 5 + i, 54, 49 - i, 5 + i, 54, BASE_GRAY, BASE_GRAY, false);
/*     */       }
/* 754 */       generateBox(level, chunkBB, 11, 8, 54, 46, 8, 54, BASE_LIGHT, BASE_LIGHT, false);
/* 755 */       generateBox(level, chunkBB, 14, 8, 44, 43, 8, 53, BASE_GRAY, BASE_GRAY, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateUpperWall(WorldGenLevel level, RandomSource random, BoundingBox chunkBB) {
/* 762 */     if (chunkIntersects(chunkBB, 14, 21, 20, 43)) {
/* 763 */       generateBox(level, chunkBB, 14, 0, 21, 20, 0, 43, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 765 */       generateWaterBox(level, chunkBB, 14, 1, 22, 20, 14, 43);
/*     */ 
/*     */       
/* 768 */       generateBox(level, chunkBB, 18, 12, 22, 20, 12, 39, BASE_GRAY, BASE_GRAY, false);
/* 769 */       generateBox(level, chunkBB, 18, 12, 21, 20, 12, 21, BASE_LIGHT, BASE_LIGHT, false);
/* 770 */       for (int i = 0; i < 4; i++) {
/* 771 */         generateBox(level, chunkBB, i + 14, i + 9, 21, i + 14, i + 9, 43 - i, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/* 773 */       for (int z = 23; z <= 39; z += 3) {
/* 774 */         placeBlock(level, DOT_DECO_DATA, 19, 13, z, chunkBB);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 780 */     if (chunkIntersects(chunkBB, 37, 21, 43, 43)) {
/* 781 */       generateBox(level, chunkBB, 37, 0, 21, 43, 0, 43, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 783 */       generateWaterBox(level, chunkBB, 37, 1, 22, 43, 14, 43);
/*     */ 
/*     */       
/* 786 */       generateBox(level, chunkBB, 37, 12, 22, 39, 12, 39, BASE_GRAY, BASE_GRAY, false);
/* 787 */       generateBox(level, chunkBB, 37, 12, 21, 39, 12, 21, BASE_LIGHT, BASE_LIGHT, false);
/* 788 */       for (int i = 0; i < 4; i++) {
/* 789 */         generateBox(level, chunkBB, 43 - i, i + 9, 21, 43 - i, i + 9, 43 - i, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/* 791 */       for (int z = 23; z <= 39; z += 3) {
/* 792 */         placeBlock(level, DOT_DECO_DATA, 38, 13, z, chunkBB);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 798 */     if (chunkIntersects(chunkBB, 15, 37, 42, 43)) {
/* 799 */       generateBox(level, chunkBB, 21, 0, 37, 36, 0, 43, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 801 */       generateWaterBox(level, chunkBB, 21, 1, 37, 36, 14, 43);
/*     */ 
/*     */       
/* 804 */       generateBox(level, chunkBB, 21, 12, 37, 36, 12, 39, BASE_GRAY, BASE_GRAY, false);
/* 805 */       for (int i = 0; i < 4; i++) {
/* 806 */         generateBox(level, chunkBB, 15 + i, i + 9, 43 - i, 42 - i, i + 9, 43 - i, BASE_LIGHT, BASE_LIGHT, false);
/*     */       }
/* 808 */       for (int x = 21; x <= 36; x += 3)
/* 809 */         placeBlock(level, DOT_DECO_DATA, x, 13, 38, chunkBB); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentPieces$MonumentBuilding.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */