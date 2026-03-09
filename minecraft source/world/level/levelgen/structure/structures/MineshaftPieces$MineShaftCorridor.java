/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.vehicle.minecart.MinecartChest;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.FenceBlock;
/*     */ import net.minecraft.world.level.block.RailBlock;
/*     */ import net.minecraft.world.level.block.WallTorchBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MineShaftCorridor
/*     */   extends MineshaftPieces.MineShaftPiece
/*     */ {
/*     */   private final boolean hasRails;
/*     */   private final boolean spiderCorridor;
/*     */   private boolean hasPlacedSpider;
/*     */   private final int numSections;
/*     */   
/*     */   public MineShaftCorridor(CompoundTag tag) {
/* 310 */     super(StructurePieceType.MINE_SHAFT_CORRIDOR, tag);
/*     */     
/* 312 */     this.hasRails = tag.getBooleanOr("hr", false);
/* 313 */     this.spiderCorridor = tag.getBooleanOr("sc", false);
/* 314 */     this.hasPlacedSpider = tag.getBooleanOr("hps", false);
/* 315 */     this.numSections = tag.getIntOr("Num", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 320 */     super.addAdditionalSaveData(context, tag);
/* 321 */     tag.putBoolean("hr", this.hasRails);
/* 322 */     tag.putBoolean("sc", this.spiderCorridor);
/* 323 */     tag.putBoolean("hps", this.hasPlacedSpider);
/* 324 */     tag.putInt("Num", this.numSections);
/*     */   }
/*     */   
/*     */   public MineShaftCorridor(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction, MineshaftStructure.Type type) {
/* 328 */     super(StructurePieceType.MINE_SHAFT_CORRIDOR, genDepth, type, boundingBox);
/* 329 */     setOrientation(direction);
/* 330 */     this.hasRails = (random.nextInt(3) == 0);
/* 331 */     this.spiderCorridor = (!this.hasRails && random.nextInt(23) == 0);
/*     */     
/* 333 */     if (getOrientation().getAxis() == Direction.Axis.Z) {
/* 334 */       this.numSections = boundingBox.getZSpan() / 5;
/*     */     } else {
/* 336 */       this.numSections = boundingBox.getXSpan() / 5;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static BoundingBox findCorridorSize(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction) {
/* 341 */     int corridorLength = random.nextInt(3) + 2;
/* 342 */     while (corridorLength > 0) {
/*     */       BoundingBox box, box, box, box;
/* 344 */       int blockLength = corridorLength * 5;
/*     */       
/* 346 */       switch (MineshaftPieces.null.$SwitchMap$net$minecraft$core$Direction[direction.ordinal()]) {
/*     */         
/*     */         default:
/* 349 */           box = new BoundingBox(0, 0, -(blockLength - 1), 2, 2, 0);
/*     */           break;
/*     */         case 2:
/* 352 */           box = new BoundingBox(0, 0, 0, 2, 2, blockLength - 1);
/*     */           break;
/*     */         case 3:
/* 355 */           box = new BoundingBox(-(blockLength - 1), 0, 0, 0, 2, 2);
/*     */           break;
/*     */         case 4:
/* 358 */           box = new BoundingBox(0, 0, 0, blockLength - 1, 2, 2);
/*     */           break;
/*     */       } 
/*     */       
/* 362 */       box.move(footX, footY, footZ);
/*     */       
/* 364 */       if (structurePieceAccessor.findCollisionPiece(box) != null) {
/* 365 */         corridorLength--; continue;
/*     */       } 
/* 367 */       return box;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 372 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 377 */     int depth = getGenDepth();
/* 378 */     int endSelection = random.nextInt(4);
/* 379 */     Direction orientation = getOrientation();
/* 380 */     if (orientation != null) {
/* 381 */       switch (MineshaftPieces.null.$SwitchMap$net$minecraft$core$Direction[orientation.ordinal()]) {
/*     */         
/*     */         default:
/* 384 */           if (endSelection <= 1) {
/* 385 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, orientation, depth); break;
/* 386 */           }  if (endSelection == 2) {
/* 387 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), Direction.WEST, depth); break;
/*     */           } 
/* 389 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), Direction.EAST, depth);
/*     */           break;
/*     */         
/*     */         case 2:
/* 393 */           if (endSelection <= 1) {
/* 394 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, orientation, depth); break;
/* 395 */           }  if (endSelection == 2) {
/* 396 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() - 3, Direction.WEST, depth); break;
/*     */           } 
/* 398 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() - 3, Direction.EAST, depth);
/*     */           break;
/*     */         
/*     */         case 3:
/* 402 */           if (endSelection <= 1) {
/* 403 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), orientation, depth); break;
/* 404 */           }  if (endSelection == 2) {
/* 405 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, Direction.NORTH, depth); break;
/*     */           } 
/* 407 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, Direction.SOUTH, depth);
/*     */           break;
/*     */         
/*     */         case 4:
/* 411 */           if (endSelection <= 1) {
/* 412 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), orientation, depth); break;
/* 413 */           }  if (endSelection == 2) {
/* 414 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() - 3, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, Direction.NORTH, depth); break;
/*     */           } 
/* 416 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() - 3, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, Direction.SOUTH, depth);
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 423 */     if (depth < 8) {
/* 424 */       if (orientation == Direction.NORTH || orientation == Direction.SOUTH) {
/* 425 */         for (int z = this.boundingBox.minZ() + 3; z + 3 <= this.boundingBox.maxZ(); z += 5) {
/* 426 */           int selection = random.nextInt(5);
/* 427 */           if (selection == 0) {
/* 428 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), z, Direction.WEST, depth + 1);
/* 429 */           } else if (selection == 1) {
/* 430 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), z, Direction.EAST, depth + 1);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 434 */         for (int x = this.boundingBox.minX() + 3; x + 3 <= this.boundingBox.maxX(); x += 5) {
/* 435 */           int selection = random.nextInt(5);
/* 436 */           if (selection == 0) {
/* 437 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, x, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, depth + 1);
/* 438 */           } else if (selection == 1) {
/* 439 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, x, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, depth + 1);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean createChest(WorldGenLevel level, BoundingBox chunkBB, RandomSource random, int x, int y, int z, ResourceKey<LootTable> lootTable) {
/* 448 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(x, y, z);
/* 449 */     if (chunkBB.isInside(mutableBlockPos) && 
/* 450 */       level.getBlockState(mutableBlockPos).isAir() && !level.getBlockState(mutableBlockPos.below()).isAir()) {
/* 451 */       BlockState state = (BlockState)Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, random.nextBoolean() ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST);
/* 452 */       placeBlock(level, state, x, y, z, chunkBB);
/* 453 */       MinecartChest chest = (MinecartChest)EntityType.CHEST_MINECART.create(level.getLevel(), EntitySpawnReason.CHUNK_GENERATION);
/* 454 */       if (chest != null) {
/* 455 */         chest.setInitialPos(mutableBlockPos.getX() + 0.5D, mutableBlockPos.getY() + 0.5D, mutableBlockPos.getZ() + 0.5D);
/* 456 */         chest.setLootTable(lootTable, random.nextLong());
/* 457 */         level.addFreshEntity(chest);
/*     */       } 
/* 459 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 463 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 468 */     if (isInInvalidLocation(level, chunkBB)) {
/*     */       return;
/*     */     }
/*     */     
/* 472 */     int x0 = 0;
/* 473 */     int x1 = 2;
/* 474 */     int y0 = 0;
/* 475 */     int y1 = 2;
/* 476 */     int length = this.numSections * 5 - 1;
/*     */     
/* 478 */     BlockState planks = this.type.getPlanksState();
/*     */ 
/*     */     
/* 481 */     generateBox(level, chunkBB, 0, 0, 0, 2, 1, length, CAVE_AIR, CAVE_AIR, false);
/* 482 */     generateMaybeBox(level, chunkBB, random, 0.8F, 0, 2, 0, 2, 2, length, CAVE_AIR, CAVE_AIR, false, false);
/*     */     
/* 484 */     if (this.spiderCorridor) {
/* 485 */       generateMaybeBox(level, chunkBB, random, 0.6F, 0, 0, 0, 2, 1, length, Blocks.COBWEB.defaultBlockState(), CAVE_AIR, false, true);
/*     */     }
/*     */ 
/*     */     
/* 489 */     for (int section = 0; section < this.numSections; section++) {
/* 490 */       int z = 2 + section * 5;
/*     */       
/* 492 */       placeSupport(level, chunkBB, 0, 0, z, 2, 2, random);
/*     */       
/* 494 */       maybePlaceCobWeb(level, chunkBB, random, 0.1F, 0, 2, z - 1);
/* 495 */       maybePlaceCobWeb(level, chunkBB, random, 0.1F, 2, 2, z - 1);
/* 496 */       maybePlaceCobWeb(level, chunkBB, random, 0.1F, 0, 2, z + 1);
/* 497 */       maybePlaceCobWeb(level, chunkBB, random, 0.1F, 2, 2, z + 1);
/* 498 */       maybePlaceCobWeb(level, chunkBB, random, 0.05F, 0, 2, z - 2);
/* 499 */       maybePlaceCobWeb(level, chunkBB, random, 0.05F, 2, 2, z - 2);
/* 500 */       maybePlaceCobWeb(level, chunkBB, random, 0.05F, 0, 2, z + 2);
/* 501 */       maybePlaceCobWeb(level, chunkBB, random, 0.05F, 2, 2, z + 2);
/*     */       
/* 503 */       if (random.nextInt(100) == 0) {
/* 504 */         createChest(level, chunkBB, random, 2, 0, z - 1, BuiltInLootTables.ABANDONED_MINESHAFT);
/*     */       }
/* 506 */       if (random.nextInt(100) == 0) {
/* 507 */         createChest(level, chunkBB, random, 0, 0, z + 1, BuiltInLootTables.ABANDONED_MINESHAFT);
/*     */       }
/* 509 */       if (this.spiderCorridor && !this.hasPlacedSpider) {
/* 510 */         int newX = 1;
/* 511 */         int newZ = z - 1 + random.nextInt(3);
/* 512 */         BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(1, 0, newZ);
/*     */         
/* 514 */         if (chunkBB.isInside(mutableBlockPos) && isInterior(level, 1, 0, newZ, chunkBB)) {
/* 515 */           this.hasPlacedSpider = true;
/* 516 */           level.setBlock(mutableBlockPos, Blocks.SPAWNER.defaultBlockState(), 2);
/*     */           
/* 518 */           BlockEntity blockEntity = level.getBlockEntity(mutableBlockPos);
/* 519 */           if (blockEntity instanceof SpawnerBlockEntity) { SpawnerBlockEntity spawner = (SpawnerBlockEntity)blockEntity;
/* 520 */             spawner.setEntityId(EntityType.CAVE_SPIDER, random); }
/*     */         
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 527 */     for (int x = 0; x <= 2; x++) {
/* 528 */       for (int z = 0; z <= length; z++) {
/* 529 */         setPlanksBlock(level, chunkBB, planks, x, -1, z);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 534 */     int supportPillarIndent = 2;
/* 535 */     placeDoubleLowerOrUpperSupport(level, chunkBB, 0, -1, 2);
/* 536 */     if (this.numSections > 1) {
/* 537 */       int lastSupportPillar = length - 2;
/* 538 */       placeDoubleLowerOrUpperSupport(level, chunkBB, 0, -1, lastSupportPillar);
/*     */     } 
/*     */     
/* 541 */     if (this.hasRails) {
/* 542 */       BlockState state = (BlockState)Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.NORTH_SOUTH);
/* 543 */       for (int z = 0; z <= length; z++) {
/* 544 */         BlockState floor = getBlock(level, 1, -1, z, chunkBB);
/* 545 */         if (!floor.isAir() && floor.isSolidRender()) {
/* 546 */           float probability = isInterior(level, 1, 0, z, chunkBB) ? 0.7F : 0.9F;
/* 547 */           maybeGenerateBlock(level, chunkBB, random, probability, 1, 0, z, state);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeDoubleLowerOrUpperSupport(WorldGenLevel level, BoundingBox chunkBB, int x, int y, int z) {
/* 554 */     BlockState woodBlock = this.type.getWoodState();
/* 555 */     BlockState plankBlock = this.type.getPlanksState();
/* 556 */     if (getBlock(level, x, y, z, chunkBB).is(plankBlock.getBlock())) {
/* 557 */       fillPillarDownOrChainUp(level, woodBlock, x, y, z, chunkBB);
/*     */     }
/* 559 */     if (getBlock(level, x + 2, y, z, chunkBB).is(plankBlock.getBlock())) {
/* 560 */       fillPillarDownOrChainUp(level, woodBlock, x + 2, y, z, chunkBB);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void fillColumnDown(WorldGenLevel level, BlockState columnState, int x, int startY, int z, BoundingBox chunkBB) {
/* 566 */     BlockPos.MutableBlockPos pos = getWorldPos(x, startY, z);
/* 567 */     if (!chunkBB.isInside(pos)) {
/*     */       return;
/*     */     }
/*     */     
/* 571 */     int worldY = pos.getY();
/*     */ 
/*     */     
/* 574 */     while (isReplaceableByStructures(level.getBlockState(pos)) && pos.getY() > level.getMinY() + 1) {
/* 575 */       pos.move(Direction.DOWN);
/*     */     }
/* 577 */     if (!canPlaceColumnOnTopOf(level, pos, level.getBlockState(pos))) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 582 */     while (pos.getY() < worldY) {
/* 583 */       pos.move(Direction.UP);
/* 584 */       level.setBlock(pos, columnState, 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void fillPillarDownOrChainUp(WorldGenLevel level, BlockState pillarState, int x, int y, int z, BoundingBox chunkBB) {
/* 590 */     BlockPos.MutableBlockPos pos = getWorldPos(x, y, z);
/* 591 */     if (!chunkBB.isInside(pos)) {
/*     */       return;
/*     */     }
/*     */     
/* 595 */     int worldY = pos.getY();
/*     */ 
/*     */     
/* 598 */     int distanceFromWorldY = 1;
/*     */     
/* 600 */     boolean checkBelow = true;
/* 601 */     boolean checkAbove = true;
/* 602 */     while (checkBelow || checkAbove) {
/* 603 */       if (checkBelow) {
/* 604 */         pos.setY(worldY - distanceFromWorldY);
/* 605 */         BlockState belowState = level.getBlockState(pos);
/* 606 */         boolean emptyBelow = (isReplaceableByStructures(belowState) && !belowState.is(Blocks.LAVA));
/* 607 */         if (!emptyBelow && canPlaceColumnOnTopOf(level, pos, belowState)) {
/* 608 */           fillColumnBetween(level, pillarState, pos, worldY - distanceFromWorldY + 1, worldY);
/*     */           return;
/*     */         } 
/* 611 */         checkBelow = (distanceFromWorldY <= 20 && emptyBelow && pos.getY() > level.getMinY() + 1);
/*     */       } 
/*     */       
/* 614 */       if (checkAbove) {
/* 615 */         pos.setY(worldY + distanceFromWorldY);
/* 616 */         BlockState aboveState = level.getBlockState(pos);
/* 617 */         boolean emptyAbove = isReplaceableByStructures(aboveState);
/* 618 */         if (!emptyAbove && canHangChainBelow(level, pos, aboveState)) {
/*     */           
/* 620 */           level.setBlock(pos.setY(worldY + 1), this.type.getFenceState(), 2);
/* 621 */           fillColumnBetween(level, Blocks.IRON_CHAIN.defaultBlockState(), pos, worldY + 2, worldY + distanceFromWorldY);
/*     */           return;
/*     */         } 
/* 624 */         checkAbove = (distanceFromWorldY <= 50 && emptyAbove && pos.getY() < level.getMaxY());
/*     */       } 
/*     */       
/* 627 */       distanceFromWorldY++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void fillColumnBetween(WorldGenLevel level, BlockState pillarState, BlockPos.MutableBlockPos pos, int bottomInclusive, int topExclusive) {
/* 632 */     for (int pillarY = bottomInclusive; pillarY < topExclusive; pillarY++) {
/* 633 */       level.setBlock(pos.setY(pillarY), pillarState, 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 638 */   private boolean canPlaceColumnOnTopOf(LevelReader level, BlockPos posBelow, BlockState stateBelow) { return stateBelow.isFaceSturdy(level, posBelow, Direction.UP); }
/*     */ 
/*     */ 
/*     */   
/* 642 */   private boolean canHangChainBelow(LevelReader level, BlockPos posAbove, BlockState stateAbove) { return (Block.canSupportCenter(level, posAbove, Direction.DOWN) && !(stateAbove.getBlock() instanceof net.minecraft.world.level.block.FallingBlock)); }
/*     */ 
/*     */ 
/*     */   
/*     */   private void placeSupport(WorldGenLevel level, BoundingBox chunkBB, int x0, int y0, int z, int y1, int x1, RandomSource random) {
/* 647 */     if (!isSupportingBox(level, chunkBB, x0, x1, y1, z)) {
/*     */       return;
/*     */     }
/*     */     
/* 651 */     BlockState planksBlock = this.type.getPlanksState();
/* 652 */     BlockState fenceBlock = this.type.getFenceState();
/*     */     
/* 654 */     generateBox(level, chunkBB, x0, y0, z, x0, y1 - 1, z, (BlockState)fenceBlock.setValue(FenceBlock.WEST, Boolean.valueOf(true)), CAVE_AIR, false);
/* 655 */     generateBox(level, chunkBB, x1, y0, z, x1, y1 - 1, z, (BlockState)fenceBlock.setValue(FenceBlock.EAST, Boolean.valueOf(true)), CAVE_AIR, false);
/* 656 */     if (random.nextInt(4) == 0) {
/* 657 */       generateBox(level, chunkBB, x0, y1, z, x0, y1, z, planksBlock, CAVE_AIR, false);
/* 658 */       generateBox(level, chunkBB, x1, y1, z, x1, y1, z, planksBlock, CAVE_AIR, false);
/*     */     } else {
/* 660 */       generateBox(level, chunkBB, x0, y1, z, x1, y1, z, planksBlock, CAVE_AIR, false);
/* 661 */       maybeGenerateBlock(level, chunkBB, random, 0.05F, x0 + 1, y1, z - 1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.SOUTH));
/* 662 */       maybeGenerateBlock(level, chunkBB, random, 0.05F, x0 + 1, y1, z + 1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.NORTH));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void maybePlaceCobWeb(WorldGenLevel level, BoundingBox chunkBB, RandomSource random, float probability, int x, int y, int z) {
/* 667 */     if (isInterior(level, x, y, z, chunkBB) && random.nextFloat() < probability && hasSturdyNeighbours(level, chunkBB, x, y, z, 2)) {
/* 668 */       placeBlock(level, Blocks.COBWEB.defaultBlockState(), x, y, z, chunkBB);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean hasSturdyNeighbours(WorldGenLevel level, BoundingBox chunkBB, int x, int y, int z, int count) {
/* 673 */     BlockPos.MutableBlockPos worldPos = getWorldPos(x, y, z);
/* 674 */     int sturdyNeighbours = 0;
/* 675 */     for (Direction direction : Direction.values()) {
/* 676 */       worldPos.move(direction);
/*     */       
/* 678 */       sturdyNeighbours++;
/* 679 */       if (chunkBB.isInside(worldPos) && level.getBlockState(worldPos).isFaceSturdy(level, worldPos, direction.getOpposite()) && sturdyNeighbours >= count) {
/* 680 */         return true;
/*     */       }
/*     */       
/* 683 */       worldPos.move(direction.getOpposite());
/*     */     } 
/* 685 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\MineshaftPieces$MineShaftCorridor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */