/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MansionPiecePlacer
/*     */ {
/*     */   private final StructureTemplateManager structureTemplateManager;
/*     */   private final RandomSource random;
/*     */   private int startX;
/*     */   private int startY;
/*     */   
/*     */   public MansionPiecePlacer(StructureTemplateManager structureTemplateManager, RandomSource random) {
/* 140 */     this.structureTemplateManager = structureTemplateManager;
/* 141 */     this.random = random;
/*     */   }
/*     */   
/*     */   public void createMansion(BlockPos origin, Rotation rotation, List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.MansionGrid mansion) {
/* 145 */     WoodlandMansionPieces.PlacementData data = new WoodlandMansionPieces.PlacementData();
/* 146 */     data.position = origin;
/* 147 */     data.rotation = rotation;
/* 148 */     data.wallType = "wall_flat";
/*     */     
/* 150 */     WoodlandMansionPieces.PlacementData secondData = new WoodlandMansionPieces.PlacementData();
/*     */ 
/*     */     
/* 153 */     entrance(pieces, data);
/* 154 */     secondData.position = data.position.above(8);
/* 155 */     secondData.rotation = data.rotation;
/* 156 */     secondData.wallType = "wall_window";
/*     */     
/* 158 */     if (!pieces.isEmpty());
/*     */ 
/*     */ 
/*     */     
/* 162 */     WoodlandMansionPieces.SimpleGrid baseGrid = mansion.baseGrid;
/* 163 */     WoodlandMansionPieces.SimpleGrid thirdGrid = mansion.thirdFloorGrid;
/*     */     
/* 165 */     this.startX = mansion.entranceX + 1;
/* 166 */     this.startY = mansion.entranceY + 1;
/* 167 */     int endX = mansion.entranceX + 1;
/* 168 */     int endY = mansion.entranceY;
/*     */     
/* 170 */     traverseOuterWalls(pieces, data, baseGrid, Direction.SOUTH, this.startX, this.startY, endX, endY);
/* 171 */     traverseOuterWalls(pieces, secondData, baseGrid, Direction.SOUTH, this.startX, this.startY, endX, endY);
/*     */ 
/*     */     
/* 174 */     WoodlandMansionPieces.PlacementData thirdData = new WoodlandMansionPieces.PlacementData();
/* 175 */     thirdData.position = data.position.above(19);
/* 176 */     thirdData.rotation = data.rotation;
/* 177 */     thirdData.wallType = "wall_window";
/*     */     
/* 179 */     boolean done = false;
/* 180 */     for (int y = 0; y < thirdGrid.height && !done; y++) {
/* 181 */       for (int x = thirdGrid.width - 1; x >= 0 && !done; x--) {
/* 182 */         if (WoodlandMansionPieces.MansionGrid.isHouse(thirdGrid, x, y)) {
/* 183 */           thirdData.position = thirdData.position.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
/* 184 */           thirdData.position = thirdData.position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/* 185 */           traverseWallPiece(pieces, thirdData);
/* 186 */           traverseOuterWalls(pieces, thirdData, thirdGrid, Direction.SOUTH, x, y, x, y);
/* 187 */           done = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 193 */     createRoof(pieces, origin.above(16), rotation, baseGrid, thirdGrid);
/* 194 */     createRoof(pieces, origin.above(27), rotation, thirdGrid, null);
/*     */     
/* 196 */     if (!pieces.isEmpty());
/*     */ 
/*     */ 
/*     */     
/* 200 */     WoodlandMansionPieces.FloorRoomCollection[] arrayOfFloorRoomCollection = new WoodlandMansionPieces.FloorRoomCollection[3];
/* 201 */     arrayOfFloorRoomCollection[0] = new WoodlandMansionPieces.FirstFloorRoomCollection();
/* 202 */     arrayOfFloorRoomCollection[1] = new WoodlandMansionPieces.SecondFloorRoomCollection();
/* 203 */     arrayOfFloorRoomCollection[2] = new WoodlandMansionPieces.ThirdFloorRoomCollection();
/*     */     
/* 205 */     for (int floorNum = 0; floorNum < 3; floorNum++) {
/* 206 */       BlockPos floorOrigin = origin.above(8 * floorNum + ((floorNum == 2) ? 3 : 0));
/* 207 */       WoodlandMansionPieces.SimpleGrid rooms = mansion.floorRooms[floorNum];
/* 208 */       WoodlandMansionPieces.SimpleGrid grid = (floorNum == 2) ? thirdGrid : baseGrid;
/*     */ 
/*     */       
/* 211 */       String southPiece = (floorNum == 0) ? "carpet_south_1" : "carpet_south_2";
/* 212 */       String westPiece = (floorNum == 0) ? "carpet_west_1" : "carpet_west_2";
/* 213 */       for (int y = 0; y < grid.height; y++) {
/* 214 */         for (int x = 0; x < grid.width; x++) {
/* 215 */           if (grid.get(x, y) == 1) {
/* 216 */             BlockPos pos = floorOrigin.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
/* 217 */             pos = pos.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/* 218 */             pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "corridor_floor", pos, rotation));
/*     */             
/* 220 */             if (grid.get(x, y - 1) == 1 || (rooms.get(x, y - 1) & 0x800000) == 8388608) {
/* 221 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "carpet_north", pos.relative(rotation.rotate(Direction.EAST), 1).above(), rotation));
/*     */             }
/* 223 */             if (grid.get(x + 1, y) == 1 || (rooms.get(x + 1, y) & 0x800000) == 8388608) {
/* 224 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "carpet_east", pos.relative(rotation.rotate(Direction.SOUTH), 1).relative(rotation.rotate(Direction.EAST), 5).above(), rotation));
/*     */             }
/* 226 */             if (grid.get(x, y + 1) == 1 || (rooms.get(x, y + 1) & 0x800000) == 8388608) {
/* 227 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, southPiece, pos.relative(rotation.rotate(Direction.SOUTH), 5).relative(rotation.rotate(Direction.WEST), 1), rotation));
/*     */             }
/* 229 */             if (grid.get(x - 1, y) == 1 || (rooms.get(x - 1, y) & 0x800000) == 8388608) {
/* 230 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, westPiece, pos.relative(rotation.rotate(Direction.WEST), 1).relative(rotation.rotate(Direction.NORTH), 1), rotation));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 236 */       String wallPiece = (floorNum == 0) ? "indoors_wall_1" : "indoors_wall_2";
/* 237 */       String doorPiece = (floorNum == 0) ? "indoors_door_1" : "indoors_door_2";
/* 238 */       List<Direction> doorDirs = Lists.newArrayList();
/* 239 */       for (int y = 0; y < grid.height; y++) {
/* 240 */         for (int x = 0; x < grid.width; x++) {
/* 241 */           boolean thirdFloorStartRoom = (floorNum == 2 && grid.get(x, y) == 3);
/* 242 */           if (grid.get(x, y) == 2 || thirdFloorStartRoom) {
/* 243 */             int roomData = rooms.get(x, y);
/* 244 */             int roomType = roomData & 0xF0000;
/* 245 */             int roomId = roomData & 0xFFFF;
/*     */ 
/*     */             
/* 248 */             thirdFloorStartRoom = (thirdFloorStartRoom && (roomData & 0x800000) == 8388608);
/*     */             
/* 250 */             doorDirs.clear();
/* 251 */             if ((roomData & 0x200000) == 2097152) {
/* 252 */               for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 253 */                 if (grid.get(x + direction.getStepX(), y + direction.getStepZ()) == 1) {
/* 254 */                   doorDirs.add(direction);
/*     */                 }
/*     */               } 
/*     */             }
/* 258 */             Direction doorDir = null;
/* 259 */             if (!doorDirs.isEmpty()) {
/* 260 */               doorDir = (Direction)doorDirs.get(this.random.nextInt(doorDirs.size()));
/* 261 */             } else if ((roomData & 0x100000) == 1048576) {
/*     */               
/* 263 */               doorDir = Direction.UP;
/*     */             } 
/*     */             
/* 266 */             BlockPos roomPos = floorOrigin.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
/* 267 */             roomPos = roomPos.relative(rotation.rotate(Direction.EAST), -1 + (x - this.startX) * 8);
/*     */             
/* 269 */             if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y) && !mansion.isRoomId(grid, x - 1, y, floorNum, roomId)) {
/* 270 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (doorDir == Direction.WEST) ? doorPiece : wallPiece, roomPos, rotation));
/*     */             }
/* 272 */             if (grid.get(x + 1, y) == 1 && !thirdFloorStartRoom) {
/* 273 */               BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 8);
/* 274 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (doorDir == Direction.EAST) ? doorPiece : wallPiece, pos, rotation));
/*     */             } 
/* 276 */             if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1) && !mansion.isRoomId(grid, x, y + 1, floorNum, roomId)) {
/* 277 */               BlockPos pos = roomPos.relative(rotation.rotate(Direction.SOUTH), 7);
/* 278 */               pos = pos.relative(rotation.rotate(Direction.EAST), 7);
/* 279 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (doorDir == Direction.SOUTH) ? doorPiece : wallPiece, pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */             } 
/* 281 */             if (grid.get(x, y - 1) == 1 && !thirdFloorStartRoom) {
/* 282 */               BlockPos pos = roomPos.relative(rotation.rotate(Direction.NORTH), 1);
/* 283 */               pos = pos.relative(rotation.rotate(Direction.EAST), 7);
/* 284 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (doorDir == Direction.NORTH) ? doorPiece : wallPiece, pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */             } 
/*     */             
/* 287 */             if (roomType == 65536) {
/* 288 */               addRoom1x1(pieces, roomPos, rotation, doorDir, arrayOfFloorRoomCollection[floorNum]);
/* 289 */             } else if (roomType == 131072 && doorDir != null) {
/*     */               
/* 291 */               Direction roomDir = mansion.get1x2RoomDirection(grid, x, y, floorNum, roomId);
/* 292 */               boolean isStairsRoom = ((roomData & 0x400000) == 4194304);
/* 293 */               addRoom1x2(pieces, roomPos, rotation, roomDir, doorDir, arrayOfFloorRoomCollection[floorNum], isStairsRoom);
/* 294 */             } else if (roomType == 262144 && doorDir != null && doorDir != Direction.UP) {
/*     */               
/* 296 */               Direction roomDir = doorDir.getClockWise();
/* 297 */               if (!mansion.isRoomId(grid, x + roomDir.getStepX(), y + roomDir.getStepZ(), floorNum, roomId)) {
/* 298 */                 roomDir = roomDir.getOpposite();
/*     */               }
/* 300 */               addRoom2x2(pieces, roomPos, rotation, roomDir, doorDir, arrayOfFloorRoomCollection[floorNum]);
/* 301 */             } else if (roomType == 262144 && doorDir == Direction.UP) {
/* 302 */               addRoom2x2Secret(pieces, roomPos, rotation, arrayOfFloorRoomCollection[floorNum]);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void traverseOuterWalls(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data, WoodlandMansionPieces.SimpleGrid grid, Direction gridDirection, int startX, int startY, int endX, int endY) {
/* 311 */     int gridX = startX;
/* 312 */     int gridY = startY;
/* 313 */     Direction startDirection = gridDirection;
/*     */     
/*     */     do {
/* 316 */       if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, gridX + gridDirection.getStepX(), gridY + gridDirection.getStepZ())) {
/*     */         
/* 318 */         traverseTurn(pieces, data);
/* 319 */         gridDirection = gridDirection.getClockWise();
/* 320 */         if (gridX != endX || gridY != endY || startDirection != gridDirection) {
/* 321 */           traverseWallPiece(pieces, data);
/*     */         }
/* 323 */       } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, gridX + gridDirection.getStepX(), gridY + gridDirection.getStepZ()) && WoodlandMansionPieces.MansionGrid.isHouse(grid, gridX + gridDirection.getStepX() + gridDirection.getCounterClockWise().getStepX(), gridY + gridDirection.getStepZ() + gridDirection.getCounterClockWise().getStepZ())) {
/*     */         
/* 325 */         traverseInnerTurn(pieces, data);
/* 326 */         gridX += gridDirection.getStepX();
/* 327 */         gridY += gridDirection.getStepZ();
/* 328 */         gridDirection = gridDirection.getCounterClockWise();
/*     */       } else {
/* 330 */         gridX += gridDirection.getStepX();
/* 331 */         gridY += gridDirection.getStepZ();
/* 332 */         if (gridX != endX || gridY != endY || startDirection != gridDirection) {
/* 333 */           traverseWallPiece(pieces, data);
/*     */         }
/*     */       } 
/* 336 */     } while (gridX != endX || gridY != endY || startDirection != gridDirection);
/*     */   }
/*     */ 
/*     */   
/*     */   private void createRoof(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roofOrigin, Rotation rotation, WoodlandMansionPieces.SimpleGrid grid, WoodlandMansionPieces.SimpleGrid aboveGrid) {
/* 341 */     for (int y = 0; y < grid.height; y++) {
/* 342 */       for (int x = 0; x < grid.width; x++) {
/* 343 */         BlockPos position = roofOrigin;
/* 344 */         position = position.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
/* 345 */         position = position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/*     */ 
/*     */         
/* 348 */         boolean isAbove = (aboveGrid != null && WoodlandMansionPieces.MansionGrid.isHouse(aboveGrid, x, y));
/*     */         
/* 350 */         if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y) && !isAbove) {
/* 351 */           pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof", position.above(3), rotation));
/*     */           
/* 353 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y)) {
/* 354 */             BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
/* 355 */             pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation));
/*     */           } 
/* 357 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y)) {
/* 358 */             BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 0);
/* 359 */             p2 = p2.relative(rotation.rotate(Direction.SOUTH), 7);
/* 360 */             pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*     */           } 
/* 362 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/* 363 */             BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 1);
/* 364 */             pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*     */           } 
/* 366 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/* 367 */             BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
/* 368 */             p2 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
/* 369 */             pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 375 */     if (aboveGrid != null) {
/* 376 */       for (int y = 0; y < grid.height; y++) {
/* 377 */         for (int x = 0; x < grid.width; x++) {
/* 378 */           BlockPos position = roofOrigin;
/* 379 */           position = position.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
/* 380 */           position = position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/*     */ 
/*     */           
/* 383 */           boolean isAbove = WoodlandMansionPieces.MansionGrid.isHouse(aboveGrid, x, y);
/*     */           
/* 385 */           if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y) && isAbove) {
/*     */             
/* 387 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y)) {
/* 388 */               BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 7);
/* 389 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", p2, rotation));
/*     */             } 
/* 391 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y)) {
/* 392 */               BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 1);
/* 393 */               p2 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
/* 394 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", p2, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*     */             } 
/* 396 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/* 397 */               BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 0);
/* 398 */               p2 = p2.relative(rotation.rotate(Direction.NORTH), 1);
/* 399 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*     */             } 
/* 401 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/* 402 */               BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
/* 403 */               p2 = p2.relative(rotation.rotate(Direction.SOUTH), 7);
/* 404 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", p2, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */             } 
/*     */             
/* 407 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y)) {
/* 408 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/* 409 */                 BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 7);
/* 410 */                 p2 = p2.relative(rotation.rotate(Direction.NORTH), 2);
/* 411 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", p2, rotation));
/*     */               } 
/* 413 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/* 414 */                 BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 8);
/* 415 */                 p2 = p2.relative(rotation.rotate(Direction.SOUTH), 7);
/* 416 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", p2, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */               } 
/*     */             } 
/* 419 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y)) {
/* 420 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/* 421 */                 BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 2);
/* 422 */                 p2 = p2.relative(rotation.rotate(Direction.NORTH), 1);
/* 423 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*     */               } 
/* 425 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/* 426 */                 BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 1);
/* 427 */                 p2 = p2.relative(rotation.rotate(Direction.SOUTH), 8);
/* 428 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", p2, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 436 */     for (int y = 0; y < grid.height; y++) {
/* 437 */       for (int x = 0; x < grid.width; x++) {
/* 438 */         BlockPos position = roofOrigin;
/* 439 */         position = position.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
/* 440 */         position = position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/*     */ 
/*     */         
/* 443 */         boolean isAbove = (aboveGrid != null && WoodlandMansionPieces.MansionGrid.isHouse(aboveGrid, x, y));
/*     */         
/* 445 */         if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y) && !isAbove) {
/* 446 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y)) {
/* 447 */             BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
/* 448 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/* 449 */               BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
/* 450 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", p3, rotation));
/* 451 */             } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y + 1)) {
/* 452 */               BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 5);
/* 453 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", p3, rotation));
/*     */             } 
/* 455 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/* 456 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/* 457 */             } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y - 1)) {
/* 458 */               BlockPos p3 = position.relative(rotation.rotate(Direction.EAST), 9);
/* 459 */               p3 = p3.relative(rotation.rotate(Direction.NORTH), 2);
/* 460 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", p3, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */             } 
/*     */           } 
/* 463 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y)) {
/* 464 */             BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 0);
/* 465 */             p2 = p2.relative(rotation.rotate(Direction.SOUTH), 0);
/* 466 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/* 467 */               BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
/* 468 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", p3, rotation.getRotated(Rotation.CLOCKWISE_90)));
/* 469 */             } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y + 1)) {
/* 470 */               BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 8);
/* 471 */               p3 = p3.relative(rotation.rotate(Direction.WEST), 3);
/* 472 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", p3, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*     */             } 
/* 474 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/* 475 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", p2, rotation.getRotated(Rotation.CLOCKWISE_180)));
/* 476 */             } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y - 1)) {
/* 477 */               BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 1);
/* 478 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", p3, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void entrance(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data) {
/* 487 */     Direction west = data.rotation.rotate(Direction.WEST);
/* 488 */     pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "entrance", data.position.relative(west, 9), data.rotation));
/* 489 */     data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), 16);
/*     */   }
/*     */   
/*     */   private void traverseWallPiece(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data) {
/* 493 */     pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, data.wallType, data.position.relative(data.rotation.rotate(Direction.EAST), 7), data.rotation));
/* 494 */     data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), 8);
/*     */   }
/*     */   
/*     */   private void traverseTurn(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data) {
/* 498 */     data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), -1);
/* 499 */     pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "wall_corner", data.position, data.rotation));
/* 500 */     data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), -7);
/* 501 */     data.position = data.position.relative(data.rotation.rotate(Direction.WEST), -6);
/* 502 */     data.rotation = data.rotation.getRotated(Rotation.CLOCKWISE_90);
/*     */   }
/*     */   
/*     */   private void traverseInnerTurn(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data) {
/* 506 */     data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), 6);
/* 507 */     data.position = data.position.relative(data.rotation.rotate(Direction.EAST), 8);
/* 508 */     data.rotation = data.rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*     */   }
/*     */   
/*     */   private void addRoom1x1(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roomPos, Rotation rotation, Direction doorDir, WoodlandMansionPieces.FloorRoomCollection rooms) {
/* 512 */     Rotation pieceRot = Rotation.NONE;
/* 513 */     String roomType = rooms.get1x1(this.random);
/* 514 */     if (doorDir != Direction.EAST) {
/* 515 */       if (doorDir == Direction.NORTH) {
/* 516 */         pieceRot = pieceRot.getRotated(Rotation.COUNTERCLOCKWISE_90);
/* 517 */       } else if (doorDir == Direction.WEST) {
/* 518 */         pieceRot = pieceRot.getRotated(Rotation.CLOCKWISE_180);
/* 519 */       } else if (doorDir == Direction.SOUTH) {
/* 520 */         pieceRot = pieceRot.getRotated(Rotation.CLOCKWISE_90);
/*     */       } else {
/*     */         
/* 523 */         roomType = rooms.get1x1Secret(this.random);
/*     */       } 
/*     */     }
/* 526 */     BlockPos orientation = StructureTemplate.getZeroPositionWithTransform(new BlockPos(1, 0, 0), Mirror.NONE, pieceRot, 7, 7);
/* 527 */     pieceRot = pieceRot.getRotated(rotation);
/* 528 */     orientation = orientation.rotate(rotation);
/* 529 */     BlockPos pos = roomPos.offset(orientation.getX(), 0, orientation.getZ());
/* 530 */     pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, roomType, pos, pieceRot));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addRoom1x2(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roomPos, Rotation rotation, Direction roomDir, Direction doorDir, WoodlandMansionPieces.FloorRoomCollection rooms, boolean isStairsRoom) {
/* 537 */     if (doorDir == Direction.EAST && roomDir == Direction.SOUTH) {
/*     */ 
/*     */       
/* 540 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/* 541 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation));
/* 542 */     } else if (doorDir == Direction.EAST && roomDir == Direction.NORTH) {
/*     */ 
/*     */       
/* 545 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/* 546 */       pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/* 547 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation, Mirror.LEFT_RIGHT));
/* 548 */     } else if (doorDir == Direction.WEST && roomDir == Direction.NORTH) {
/*     */ 
/*     */       
/* 551 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/* 552 */       pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/* 553 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_180)));
/* 554 */     } else if (doorDir == Direction.WEST && roomDir == Direction.SOUTH) {
/*     */ 
/*     */       
/* 557 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/* 558 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation, Mirror.FRONT_BACK));
/* 559 */     } else if (doorDir == Direction.SOUTH && roomDir == Direction.EAST) {
/*     */ 
/*     */       
/* 562 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/* 563 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90), Mirror.LEFT_RIGHT));
/* 564 */     } else if (doorDir == Direction.SOUTH && roomDir == Direction.WEST) {
/*     */ 
/*     */       
/* 567 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/* 568 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/* 569 */     } else if (doorDir == Direction.NORTH && roomDir == Direction.WEST) {
/*     */ 
/*     */       
/* 572 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/* 573 */       pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/* 574 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90), Mirror.FRONT_BACK));
/* 575 */     } else if (doorDir == Direction.NORTH && roomDir == Direction.EAST) {
/*     */ 
/*     */       
/* 578 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/* 579 */       pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/* 580 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/* 581 */     } else if (doorDir == Direction.SOUTH && roomDir == Direction.NORTH) {
/*     */ 
/*     */ 
/*     */       
/* 585 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/* 586 */       pos = pos.relative(rotation.rotate(Direction.NORTH), 8);
/* 587 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation));
/* 588 */     } else if (doorDir == Direction.NORTH && roomDir == Direction.SOUTH) {
/*     */ 
/*     */ 
/*     */       
/* 592 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/* 593 */       pos = pos.relative(rotation.rotate(Direction.SOUTH), 14);
/* 594 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_180)));
/* 595 */     } else if (doorDir == Direction.WEST && roomDir == Direction.EAST) {
/*     */       
/* 597 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 15);
/* 598 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/* 599 */     } else if (doorDir == Direction.EAST && roomDir == Direction.WEST) {
/*     */       
/* 601 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.WEST), 7);
/* 602 */       pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/* 603 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/* 604 */     } else if (doorDir == Direction.UP && roomDir == Direction.EAST) {
/*     */       
/* 606 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 15);
/* 607 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2Secret(this.random), pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/* 608 */     } else if (doorDir == Direction.UP && roomDir == Direction.SOUTH) {
/*     */ 
/*     */ 
/*     */       
/* 612 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/* 613 */       pos = pos.relative(rotation.rotate(Direction.NORTH), 0);
/* 614 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2Secret(this.random), pos, rotation));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addRoom2x2(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roomPos, Rotation rotation, Direction roomDir, Direction doorDir, WoodlandMansionPieces.FloorRoomCollection rooms) {
/* 619 */     int east = 0;
/* 620 */     int south = 0;
/* 621 */     Rotation rot = rotation;
/* 622 */     Mirror mirror = Mirror.NONE;
/*     */ 
/*     */ 
/*     */     
/* 626 */     if (doorDir == Direction.EAST && roomDir == Direction.SOUTH) {
/*     */ 
/*     */       
/* 629 */       east = -7;
/* 630 */     } else if (doorDir == Direction.EAST && roomDir == Direction.NORTH) {
/*     */ 
/*     */       
/* 633 */       east = -7;
/* 634 */       south = 6;
/* 635 */       mirror = Mirror.LEFT_RIGHT;
/* 636 */     } else if (doorDir == Direction.NORTH && roomDir == Direction.EAST) {
/*     */ 
/*     */ 
/*     */       
/* 640 */       east = 1;
/* 641 */       south = 14;
/* 642 */       rot = rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/* 643 */     } else if (doorDir == Direction.NORTH && roomDir == Direction.WEST) {
/*     */ 
/*     */ 
/*     */       
/* 647 */       east = 7;
/* 648 */       south = 14;
/* 649 */       rot = rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/* 650 */       mirror = Mirror.LEFT_RIGHT;
/* 651 */     } else if (doorDir == Direction.SOUTH && roomDir == Direction.WEST) {
/*     */ 
/*     */ 
/*     */       
/* 655 */       east = 7;
/* 656 */       south = -8;
/* 657 */       rot = rotation.getRotated(Rotation.CLOCKWISE_90);
/* 658 */     } else if (doorDir == Direction.SOUTH && roomDir == Direction.EAST) {
/*     */ 
/*     */ 
/*     */       
/* 662 */       east = 1;
/* 663 */       south = -8;
/* 664 */       rot = rotation.getRotated(Rotation.CLOCKWISE_90);
/* 665 */       mirror = Mirror.LEFT_RIGHT;
/* 666 */     } else if (doorDir == Direction.WEST && roomDir == Direction.NORTH) {
/*     */ 
/*     */       
/* 669 */       east = 15;
/* 670 */       south = 6;
/* 671 */       rot = rotation.getRotated(Rotation.CLOCKWISE_180);
/* 672 */     } else if (doorDir == Direction.WEST && roomDir == Direction.SOUTH) {
/*     */ 
/*     */       
/* 675 */       east = 15;
/* 676 */       mirror = Mirror.FRONT_BACK;
/*     */     } 
/*     */     
/* 679 */     BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), east);
/* 680 */     pos = pos.relative(rotation.rotate(Direction.SOUTH), south);
/* 681 */     pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get2x2(this.random), pos, rot, mirror));
/*     */   }
/*     */   
/*     */   private void addRoom2x2Secret(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roomPos, Rotation rotation, WoodlandMansionPieces.FloorRoomCollection rooms) {
/* 685 */     BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/* 686 */     pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get2x2Secret(this.random), pos, rotation, Mirror.NONE));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionPieces$MansionPiecePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */