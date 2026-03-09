/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.vehicle.minecart.MinecartChest;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelAccessor;
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
/*     */ public class MineshaftPieces
/*     */ {
/*     */   private static final int DEFAULT_SHAFT_WIDTH = 3;
/*     */   private static final int DEFAULT_SHAFT_HEIGHT = 3;
/*     */   private static final int DEFAULT_SHAFT_LENGTH = 5;
/*     */   private static final int MAX_PILLAR_HEIGHT = 20;
/*     */   private static final int MAX_CHAIN_HEIGHT = 50;
/*     */   private static final int MAX_DEPTH = 8;
/*     */   public static final int MAGIC_START_Y = 50;
/*     */   
/*     */   private static abstract class MineShaftPiece
/*     */     extends StructurePiece
/*     */   {
/*     */     protected MineshaftStructure.Type type;
/*     */     
/*     */     public MineShaftPiece(StructurePieceType pieceType, int genDepth, MineshaftStructure.Type type, BoundingBox boundingBox) {
/*  57 */       super(pieceType, genDepth, boundingBox);
/*  58 */       this.type = type;
/*     */     }
/*     */     
/*     */     public MineShaftPiece(StructurePieceType type, CompoundTag tag) {
/*  62 */       super(type, tag);
/*  63 */       this.type = MineshaftStructure.Type.byId(tag.getIntOr("MST", 0));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean canBeReplaced(LevelReader level, int x, int y, int z, BoundingBox chunkBB) {
/*  69 */       BlockState state = getBlock(level, x, y, z, chunkBB);
/*  70 */       return (!state.is(this.type.getPlanksState().getBlock()) && 
/*  71 */         !state.is(this.type.getWoodState().getBlock()) && 
/*  72 */         !state.is(this.type.getFenceState().getBlock()) && 
/*  73 */         !state.is(Blocks.IRON_CHAIN));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  78 */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) { tag.putInt("MST", this.type.ordinal()); }
/*     */ 
/*     */     
/*     */     protected boolean isSupportingBox(BlockGetter level, BoundingBox chunkBB, int x0, int x1, int y1, int z0) {
/*  82 */       for (int x = x0; x <= x1; x++) {
/*  83 */         if (getBlock(level, x, y1 + 1, z0, chunkBB).isAir()) {
/*  84 */           return false;
/*     */         }
/*     */       } 
/*  87 */       return true;
/*     */     }
/*     */     
/*     */     protected boolean isInInvalidLocation(LevelAccessor level, BoundingBox chunkBB) {
/*  91 */       int x0 = Math.max(this.boundingBox.minX() - 1, chunkBB.minX());
/*  92 */       int y0 = Math.max(this.boundingBox.minY() - 1, chunkBB.minY());
/*  93 */       int z0 = Math.max(this.boundingBox.minZ() - 1, chunkBB.minZ());
/*  94 */       int x1 = Math.min(this.boundingBox.maxX() + 1, chunkBB.maxX());
/*  95 */       int y1 = Math.min(this.boundingBox.maxY() + 1, chunkBB.maxY());
/*  96 */       int z1 = Math.min(this.boundingBox.maxZ() + 1, chunkBB.maxZ());
/*     */       
/*  98 */       BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos((x0 + x1) / 2, (y0 + y1) / 2, (z0 + z1) / 2);
/*     */       
/* 100 */       if (level.getBiome(blockPos).is(BiomeTags.MINESHAFT_BLOCKING)) {
/* 101 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 105 */       for (int x = x0; x <= x1; x++) {
/* 106 */         for (int z = z0; z <= z1; z++) {
/* 107 */           if (level.getBlockState(blockPos.set(x, y0, z)).liquid()) {
/* 108 */             return true;
/*     */           }
/* 110 */           if (level.getBlockState(blockPos.set(x, y1, z)).liquid()) {
/* 111 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 116 */       for (int x = x0; x <= x1; x++) {
/* 117 */         for (int y = y0; y <= y1; y++) {
/* 118 */           if (level.getBlockState(blockPos.set(x, y, z0)).liquid()) {
/* 119 */             return true;
/*     */           }
/* 121 */           if (level.getBlockState(blockPos.set(x, y, z1)).liquid()) {
/* 122 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 127 */       for (int z = z0; z <= z1; z++) {
/* 128 */         for (int y = y0; y <= y1; y++) {
/* 129 */           if (level.getBlockState(blockPos.set(x0, y, z)).liquid()) {
/* 130 */             return true;
/*     */           }
/* 132 */           if (level.getBlockState(blockPos.set(x1, y, z)).liquid()) {
/* 133 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 137 */       return false;
/*     */     }
/*     */     
/*     */     protected void setPlanksBlock(WorldGenLevel level, BoundingBox chunkBB, BlockState planksBlock, int x, int y, int z) {
/* 141 */       if (!isInterior(level, x, y, z, chunkBB)) {
/*     */         return;
/*     */       }
/* 144 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(x, y, z);
/* 145 */       BlockState existingState = level.getBlockState(mutableBlockPos);
/* 146 */       if (!existingState.isFaceSturdy(level, mutableBlockPos, Direction.UP))
/*     */       {
/* 148 */         level.setBlock(mutableBlockPos, planksBlock, 2);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static MineShaftPiece createRandomShaftPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth, MineshaftStructure.Type type) {
/* 154 */     int randomSelection = random.nextInt(100);
/* 155 */     if (randomSelection >= 80) {
/* 156 */       BoundingBox crossingBox = MineShaftCrossing.findCrossing(structurePieceAccessor, random, footX, footY, footZ, direction);
/* 157 */       if (crossingBox != null) {
/* 158 */         return new MineShaftCrossing(genDepth, crossingBox, direction, type);
/*     */       }
/* 160 */     } else if (randomSelection >= 70) {
/* 161 */       BoundingBox stairsBox = MineShaftStairs.findStairs(structurePieceAccessor, random, footX, footY, footZ, direction);
/* 162 */       if (stairsBox != null) {
/* 163 */         return new MineShaftStairs(genDepth, stairsBox, direction, type);
/*     */       }
/*     */     } else {
/* 166 */       BoundingBox corridorBox = MineShaftCorridor.findCorridorSize(structurePieceAccessor, random, footX, footY, footZ, direction);
/* 167 */       if (corridorBox != null) {
/* 168 */         return new MineShaftCorridor(genDepth, random, corridorBox, direction, type);
/*     */       }
/*     */     } 
/*     */     
/* 172 */     return null;
/*     */   }
/*     */   
/*     */   private static MineShaftPiece generateAndAddPiece(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth) {
/* 176 */     if (depth > 8) {
/* 177 */       return null;
/*     */     }
/* 179 */     if (Math.abs(footX - startPiece.getBoundingBox().minX()) > 80 || Math.abs(footZ - startPiece.getBoundingBox().minZ()) > 80) {
/* 180 */       return null;
/*     */     }
/*     */     
/* 183 */     MineshaftStructure.Type type = ((MineShaftPiece)startPiece).type;
/* 184 */     MineShaftPiece newPiece = createRandomShaftPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth + 1, type);
/* 185 */     if (newPiece != null) {
/* 186 */       structurePieceAccessor.addPiece(newPiece);
/* 187 */       newPiece.addChildren(startPiece, structurePieceAccessor, random);
/*     */     } 
/* 189 */     return newPiece;
/*     */   }
/*     */   
/*     */   public static class MineShaftRoom extends MineShaftPiece {
/* 193 */     private final List<BoundingBox> childEntranceBoxes = Lists.newLinkedList();
/*     */     
/*     */     public MineShaftRoom(int genDepth, RandomSource random, int west, int north, MineshaftStructure.Type type) {
/* 196 */       super(StructurePieceType.MINE_SHAFT_ROOM, genDepth, type, new BoundingBox(west, 50, north, west + 7 + random.nextInt(6), 54 + random.nextInt(6), north + 7 + random.nextInt(6)));
/* 197 */       this.type = type;
/*     */     }
/*     */     
/*     */     public MineShaftRoom(CompoundTag tag) {
/* 201 */       super(StructurePieceType.MINE_SHAFT_ROOM, tag);
/* 202 */       this.childEntranceBoxes.addAll((Collection)tag.read("Entrances", BoundingBox.CODEC.listOf()).orElse(List.of()));
/*     */     }
/*     */ 
/*     */     
/*     */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 207 */       int depth = getGenDepth();
/*     */ 
/*     */ 
/*     */       
/* 211 */       int heightSpace = this.boundingBox.getYSpan() - 3 - 1;
/* 212 */       if (heightSpace <= 0) {
/* 213 */         heightSpace = 1;
/*     */       }
/*     */ 
/*     */       
/* 217 */       int pos = 0;
/* 218 */       while (pos < this.boundingBox.getXSpan()) {
/* 219 */         pos += random.nextInt(this.boundingBox.getXSpan());
/* 220 */         if (pos + 3 > this.boundingBox.getXSpan()) {
/*     */           break;
/*     */         }
/* 223 */         MineshaftPieces.MineShaftPiece child = MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + pos, this.boundingBox.minY() + random.nextInt(heightSpace) + 1, this.boundingBox.minZ() - 1, Direction.NORTH, depth);
/* 224 */         if (child != null) {
/* 225 */           BoundingBox childBox = child.getBoundingBox();
/* 226 */           this.childEntranceBoxes.add(new BoundingBox(childBox.minX(), childBox.minY(), this.boundingBox.minZ(), childBox.maxX(), childBox.maxY(), this.boundingBox.minZ() + 1));
/*     */         } 
/* 228 */         pos += 4;
/*     */       } 
/*     */       
/* 231 */       pos = 0;
/* 232 */       while (pos < this.boundingBox.getXSpan()) {
/* 233 */         pos += random.nextInt(this.boundingBox.getXSpan());
/* 234 */         if (pos + 3 > this.boundingBox.getXSpan()) {
/*     */           break;
/*     */         }
/* 237 */         MineshaftPieces.MineShaftPiece child = MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + pos, this.boundingBox.minY() + random.nextInt(heightSpace) + 1, this.boundingBox.maxZ() + 1, Direction.SOUTH, depth);
/* 238 */         if (child != null) {
/* 239 */           BoundingBox childBox = child.getBoundingBox();
/* 240 */           this.childEntranceBoxes.add(new BoundingBox(childBox.minX(), childBox.minY(), this.boundingBox.maxZ() - 1, childBox.maxX(), childBox.maxY(), this.boundingBox.maxZ()));
/*     */         } 
/* 242 */         pos += 4;
/*     */       } 
/*     */       
/* 245 */       pos = 0;
/* 246 */       while (pos < this.boundingBox.getZSpan()) {
/* 247 */         pos += random.nextInt(this.boundingBox.getZSpan());
/* 248 */         if (pos + 3 > this.boundingBox.getZSpan()) {
/*     */           break;
/*     */         }
/* 251 */         MineshaftPieces.MineShaftPiece child = MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + random.nextInt(heightSpace) + 1, this.boundingBox.minZ() + pos, Direction.WEST, depth);
/* 252 */         if (child != null) {
/* 253 */           BoundingBox childBox = child.getBoundingBox();
/* 254 */           this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.minX(), childBox.minY(), childBox.minZ(), this.boundingBox.minX() + 1, childBox.maxY(), childBox.maxZ()));
/*     */         } 
/* 256 */         pos += 4;
/*     */       } 
/*     */       
/* 259 */       pos = 0;
/* 260 */       while (pos < this.boundingBox.getZSpan()) {
/* 261 */         pos += random.nextInt(this.boundingBox.getZSpan());
/* 262 */         if (pos + 3 > this.boundingBox.getZSpan()) {
/*     */           break;
/*     */         }
/* 265 */         StructurePiece child = MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + random.nextInt(heightSpace) + 1, this.boundingBox.minZ() + pos, Direction.EAST, depth);
/* 266 */         if (child != null) {
/* 267 */           BoundingBox childBox = child.getBoundingBox();
/* 268 */           this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.maxX() - 1, childBox.minY(), childBox.minZ(), this.boundingBox.maxX(), childBox.maxY(), childBox.maxZ()));
/*     */         } 
/* 270 */         pos += 4;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 276 */       if (isInInvalidLocation(level, chunkBB)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 281 */       generateBox(level, chunkBB, this.boundingBox.minX(), this.boundingBox.minY() + 1, this.boundingBox.minZ(), this.boundingBox.maxX(), Math.min(this.boundingBox.minY() + 3, this.boundingBox.maxY()), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/* 282 */       for (BoundingBox entranceBox : this.childEntranceBoxes) {
/* 283 */         generateBox(level, chunkBB, entranceBox.minX(), entranceBox.maxY() - 2, entranceBox.minZ(), entranceBox.maxX(), entranceBox.maxY(), entranceBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/*     */       }
/* 285 */       generateUpperHalfSphere(level, chunkBB, this.boundingBox.minX(), this.boundingBox.minY() + 4, this.boundingBox.minZ(), this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, false);
/*     */     }
/*     */ 
/*     */     
/*     */     public void move(int dx, int dy, int dz) {
/* 290 */       super.move(dx, dy, dz);
/* 291 */       for (BoundingBox bb : this.childEntranceBoxes) {
/* 292 */         bb.move(dx, dy, dz);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 298 */       super.addAdditionalSaveData(context, tag);
/* 299 */       tag.store("Entrances", BoundingBox.CODEC.listOf(), this.childEntranceBoxes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MineShaftCorridor extends MineShaftPiece {
/*     */     private final boolean hasRails;
/*     */     private final boolean spiderCorridor;
/*     */     private boolean hasPlacedSpider;
/*     */     private final int numSections;
/*     */     
/*     */     public MineShaftCorridor(CompoundTag tag) {
/* 310 */       super(StructurePieceType.MINE_SHAFT_CORRIDOR, tag);
/*     */       
/* 312 */       this.hasRails = tag.getBooleanOr("hr", false);
/* 313 */       this.spiderCorridor = tag.getBooleanOr("sc", false);
/* 314 */       this.hasPlacedSpider = tag.getBooleanOr("hps", false);
/* 315 */       this.numSections = tag.getIntOr("Num", 0);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 320 */       super.addAdditionalSaveData(context, tag);
/* 321 */       tag.putBoolean("hr", this.hasRails);
/* 322 */       tag.putBoolean("sc", this.spiderCorridor);
/* 323 */       tag.putBoolean("hps", this.hasPlacedSpider);
/* 324 */       tag.putInt("Num", this.numSections);
/*     */     }
/*     */     
/*     */     public MineShaftCorridor(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction, MineshaftStructure.Type type) {
/* 328 */       super(StructurePieceType.MINE_SHAFT_CORRIDOR, genDepth, type, boundingBox);
/* 329 */       setOrientation(direction);
/* 330 */       this.hasRails = (random.nextInt(3) == 0);
/* 331 */       this.spiderCorridor = (!this.hasRails && random.nextInt(23) == 0);
/*     */       
/* 333 */       if (getOrientation().getAxis() == Direction.Axis.Z) {
/* 334 */         this.numSections = boundingBox.getZSpan() / 5;
/*     */       } else {
/* 336 */         this.numSections = boundingBox.getXSpan() / 5;
/*     */       } 
/*     */     }
/*     */     
/*     */     public static BoundingBox findCorridorSize(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction) {
/* 341 */       int corridorLength = random.nextInt(3) + 2;
/* 342 */       while (corridorLength > 0) {
/*     */         BoundingBox box, box, box, box;
/* 344 */         int blockLength = corridorLength * 5;
/*     */         
/* 346 */         switch (MineshaftPieces.null.$SwitchMap$net$minecraft$core$Direction[direction.ordinal()]) {
/*     */           
/*     */           default:
/* 349 */             box = new BoundingBox(0, 0, -(blockLength - 1), 2, 2, 0);
/*     */             break;
/*     */           case 2:
/* 352 */             box = new BoundingBox(0, 0, 0, 2, 2, blockLength - 1);
/*     */             break;
/*     */           case 3:
/* 355 */             box = new BoundingBox(-(blockLength - 1), 0, 0, 0, 2, 2);
/*     */             break;
/*     */           case 4:
/* 358 */             box = new BoundingBox(0, 0, 0, blockLength - 1, 2, 2);
/*     */             break;
/*     */         } 
/*     */         
/* 362 */         box.move(footX, footY, footZ);
/*     */         
/* 364 */         if (structurePieceAccessor.findCollisionPiece(box) != null) {
/* 365 */           corridorLength--; continue;
/*     */         } 
/* 367 */         return box;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 372 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 377 */       int depth = getGenDepth();
/* 378 */       int endSelection = random.nextInt(4);
/* 379 */       Direction orientation = getOrientation();
/* 380 */       if (orientation != null) {
/* 381 */         switch (MineshaftPieces.null.$SwitchMap$net$minecraft$core$Direction[orientation.ordinal()]) {
/*     */           
/*     */           default:
/* 384 */             if (endSelection <= 1) {
/* 385 */               MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, orientation, depth); break;
/* 386 */             }  if (endSelection == 2) {
/* 387 */               MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), Direction.WEST, depth); break;
/*     */             } 
/* 389 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), Direction.EAST, depth);
/*     */             break;
/*     */           
/*     */           case 2:
/* 393 */             if (endSelection <= 1) {
/* 394 */               MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, orientation, depth); break;
/* 395 */             }  if (endSelection == 2) {
/* 396 */               MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() - 3, Direction.WEST, depth); break;
/*     */             } 
/* 398 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() - 3, Direction.EAST, depth);
/*     */             break;
/*     */           
/*     */           case 3:
/* 402 */             if (endSelection <= 1) {
/* 403 */               MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), orientation, depth); break;
/* 404 */             }  if (endSelection == 2) {
/* 405 */               MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, Direction.NORTH, depth); break;
/*     */             } 
/* 407 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, Direction.SOUTH, depth);
/*     */             break;
/*     */           
/*     */           case 4:
/* 411 */             if (endSelection <= 1) {
/* 412 */               MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), orientation, depth); break;
/* 413 */             }  if (endSelection == 2) {
/* 414 */               MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() - 3, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, Direction.NORTH, depth); break;
/*     */             } 
/* 416 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() - 3, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, Direction.SOUTH, depth);
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 423 */       if (depth < 8) {
/* 424 */         if (orientation == Direction.NORTH || orientation == Direction.SOUTH) {
/* 425 */           for (int z = this.boundingBox.minZ() + 3; z + 3 <= this.boundingBox.maxZ(); z += 5) {
/* 426 */             int selection = random.nextInt(5);
/* 427 */             if (selection == 0) {
/* 428 */               MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), z, Direction.WEST, depth + 1);
/* 429 */             } else if (selection == 1) {
/* 430 */               MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), z, Direction.EAST, depth + 1);
/*     */             } 
/*     */           } 
/*     */         } else {
/* 434 */           for (int x = this.boundingBox.minX() + 3; x + 3 <= this.boundingBox.maxX(); x += 5) {
/* 435 */             int selection = random.nextInt(5);
/* 436 */             if (selection == 0) {
/* 437 */               MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, x, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, depth + 1);
/* 438 */             } else if (selection == 1) {
/* 439 */               MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, x, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, depth + 1);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean createChest(WorldGenLevel level, BoundingBox chunkBB, RandomSource random, int x, int y, int z, ResourceKey<LootTable> lootTable) {
/* 448 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(x, y, z);
/* 449 */       if (chunkBB.isInside(mutableBlockPos) && 
/* 450 */         level.getBlockState(mutableBlockPos).isAir() && !level.getBlockState(mutableBlockPos.below()).isAir()) {
/* 451 */         BlockState state = (BlockState)Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, random.nextBoolean() ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST);
/* 452 */         placeBlock(level, state, x, y, z, chunkBB);
/* 453 */         MinecartChest chest = (MinecartChest)EntityType.CHEST_MINECART.create(level.getLevel(), EntitySpawnReason.CHUNK_GENERATION);
/* 454 */         if (chest != null) {
/* 455 */           chest.setInitialPos(mutableBlockPos.getX() + 0.5D, mutableBlockPos.getY() + 0.5D, mutableBlockPos.getZ() + 0.5D);
/* 456 */           chest.setLootTable(lootTable, random.nextLong());
/* 457 */           level.addFreshEntity(chest);
/*     */         } 
/* 459 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 463 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 468 */       if (isInInvalidLocation(level, chunkBB)) {
/*     */         return;
/*     */       }
/*     */       
/* 472 */       int x0 = 0;
/* 473 */       int x1 = 2;
/* 474 */       int y0 = 0;
/* 475 */       int y1 = 2;
/* 476 */       int length = this.numSections * 5 - 1;
/*     */       
/* 478 */       BlockState planks = this.type.getPlanksState();
/*     */ 
/*     */       
/* 481 */       generateBox(level, chunkBB, 0, 0, 0, 2, 1, length, CAVE_AIR, CAVE_AIR, false);
/* 482 */       generateMaybeBox(level, chunkBB, random, 0.8F, 0, 2, 0, 2, 2, length, CAVE_AIR, CAVE_AIR, false, false);
/*     */       
/* 484 */       if (this.spiderCorridor) {
/* 485 */         generateMaybeBox(level, chunkBB, random, 0.6F, 0, 0, 0, 2, 1, length, Blocks.COBWEB.defaultBlockState(), CAVE_AIR, false, true);
/*     */       }
/*     */ 
/*     */       
/* 489 */       for (int section = 0; section < this.numSections; section++) {
/* 490 */         int z = 2 + section * 5;
/*     */         
/* 492 */         placeSupport(level, chunkBB, 0, 0, z, 2, 2, random);
/*     */         
/* 494 */         maybePlaceCobWeb(level, chunkBB, random, 0.1F, 0, 2, z - 1);
/* 495 */         maybePlaceCobWeb(level, chunkBB, random, 0.1F, 2, 2, z - 1);
/* 496 */         maybePlaceCobWeb(level, chunkBB, random, 0.1F, 0, 2, z + 1);
/* 497 */         maybePlaceCobWeb(level, chunkBB, random, 0.1F, 2, 2, z + 1);
/* 498 */         maybePlaceCobWeb(level, chunkBB, random, 0.05F, 0, 2, z - 2);
/* 499 */         maybePlaceCobWeb(level, chunkBB, random, 0.05F, 2, 2, z - 2);
/* 500 */         maybePlaceCobWeb(level, chunkBB, random, 0.05F, 0, 2, z + 2);
/* 501 */         maybePlaceCobWeb(level, chunkBB, random, 0.05F, 2, 2, z + 2);
/*     */         
/* 503 */         if (random.nextInt(100) == 0) {
/* 504 */           createChest(level, chunkBB, random, 2, 0, z - 1, BuiltInLootTables.ABANDONED_MINESHAFT);
/*     */         }
/* 506 */         if (random.nextInt(100) == 0) {
/* 507 */           createChest(level, chunkBB, random, 0, 0, z + 1, BuiltInLootTables.ABANDONED_MINESHAFT);
/*     */         }
/* 509 */         if (this.spiderCorridor && !this.hasPlacedSpider) {
/* 510 */           int newX = 1;
/* 511 */           int newZ = z - 1 + random.nextInt(3);
/* 512 */           BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(1, 0, newZ);
/*     */           
/* 514 */           if (chunkBB.isInside(mutableBlockPos) && isInterior(level, 1, 0, newZ, chunkBB)) {
/* 515 */             this.hasPlacedSpider = true;
/* 516 */             level.setBlock(mutableBlockPos, Blocks.SPAWNER.defaultBlockState(), 2);
/*     */             
/* 518 */             BlockEntity blockEntity = level.getBlockEntity(mutableBlockPos);
/* 519 */             if (blockEntity instanceof SpawnerBlockEntity) { SpawnerBlockEntity spawner = (SpawnerBlockEntity)blockEntity;
/* 520 */               spawner.setEntityId(EntityType.CAVE_SPIDER, random); }
/*     */           
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 527 */       for (int x = 0; x <= 2; x++) {
/* 528 */         for (int z = 0; z <= length; z++) {
/* 529 */           setPlanksBlock(level, chunkBB, planks, x, -1, z);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 534 */       int supportPillarIndent = 2;
/* 535 */       placeDoubleLowerOrUpperSupport(level, chunkBB, 0, -1, 2);
/* 536 */       if (this.numSections > 1) {
/* 537 */         int lastSupportPillar = length - 2;
/* 538 */         placeDoubleLowerOrUpperSupport(level, chunkBB, 0, -1, lastSupportPillar);
/*     */       } 
/*     */       
/* 541 */       if (this.hasRails) {
/* 542 */         BlockState state = (BlockState)Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.NORTH_SOUTH);
/* 543 */         for (int z = 0; z <= length; z++) {
/* 544 */           BlockState floor = getBlock(level, 1, -1, z, chunkBB);
/* 545 */           if (!floor.isAir() && floor.isSolidRender()) {
/* 546 */             float probability = isInterior(level, 1, 0, z, chunkBB) ? 0.7F : 0.9F;
/* 547 */             maybeGenerateBlock(level, chunkBB, random, probability, 1, 0, z, state);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void placeDoubleLowerOrUpperSupport(WorldGenLevel level, BoundingBox chunkBB, int x, int y, int z) {
/* 554 */       BlockState woodBlock = this.type.getWoodState();
/* 555 */       BlockState plankBlock = this.type.getPlanksState();
/* 556 */       if (getBlock(level, x, y, z, chunkBB).is(plankBlock.getBlock())) {
/* 557 */         fillPillarDownOrChainUp(level, woodBlock, x, y, z, chunkBB);
/*     */       }
/* 559 */       if (getBlock(level, x + 2, y, z, chunkBB).is(plankBlock.getBlock())) {
/* 560 */         fillPillarDownOrChainUp(level, woodBlock, x + 2, y, z, chunkBB);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected void fillColumnDown(WorldGenLevel level, BlockState columnState, int x, int startY, int z, BoundingBox chunkBB) {
/* 566 */       BlockPos.MutableBlockPos pos = getWorldPos(x, startY, z);
/* 567 */       if (!chunkBB.isInside(pos)) {
/*     */         return;
/*     */       }
/*     */       
/* 571 */       int worldY = pos.getY();
/*     */ 
/*     */       
/* 574 */       while (isReplaceableByStructures(level.getBlockState(pos)) && pos.getY() > level.getMinY() + 1) {
/* 575 */         pos.move(Direction.DOWN);
/*     */       }
/* 577 */       if (!canPlaceColumnOnTopOf(level, pos, level.getBlockState(pos))) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 582 */       while (pos.getY() < worldY) {
/* 583 */         pos.move(Direction.UP);
/* 584 */         level.setBlock(pos, columnState, 2);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void fillPillarDownOrChainUp(WorldGenLevel level, BlockState pillarState, int x, int y, int z, BoundingBox chunkBB) {
/* 590 */       BlockPos.MutableBlockPos pos = getWorldPos(x, y, z);
/* 591 */       if (!chunkBB.isInside(pos)) {
/*     */         return;
/*     */       }
/*     */       
/* 595 */       int worldY = pos.getY();
/*     */ 
/*     */       
/* 598 */       int distanceFromWorldY = 1;
/*     */       
/* 600 */       boolean checkBelow = true;
/* 601 */       boolean checkAbove = true;
/* 602 */       while (checkBelow || checkAbove) {
/* 603 */         if (checkBelow) {
/* 604 */           pos.setY(worldY - distanceFromWorldY);
/* 605 */           BlockState belowState = level.getBlockState(pos);
/* 606 */           boolean emptyBelow = (isReplaceableByStructures(belowState) && !belowState.is(Blocks.LAVA));
/* 607 */           if (!emptyBelow && canPlaceColumnOnTopOf(level, pos, belowState)) {
/* 608 */             fillColumnBetween(level, pillarState, pos, worldY - distanceFromWorldY + 1, worldY);
/*     */             return;
/*     */           } 
/* 611 */           checkBelow = (distanceFromWorldY <= 20 && emptyBelow && pos.getY() > level.getMinY() + 1);
/*     */         } 
/*     */         
/* 614 */         if (checkAbove) {
/* 615 */           pos.setY(worldY + distanceFromWorldY);
/* 616 */           BlockState aboveState = level.getBlockState(pos);
/* 617 */           boolean emptyAbove = isReplaceableByStructures(aboveState);
/* 618 */           if (!emptyAbove && canHangChainBelow(level, pos, aboveState)) {
/*     */             
/* 620 */             level.setBlock(pos.setY(worldY + 1), this.type.getFenceState(), 2);
/* 621 */             fillColumnBetween(level, Blocks.IRON_CHAIN.defaultBlockState(), pos, worldY + 2, worldY + distanceFromWorldY);
/*     */             return;
/*     */           } 
/* 624 */           checkAbove = (distanceFromWorldY <= 50 && emptyAbove && pos.getY() < level.getMaxY());
/*     */         } 
/*     */         
/* 627 */         distanceFromWorldY++;
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void fillColumnBetween(WorldGenLevel level, BlockState pillarState, BlockPos.MutableBlockPos pos, int bottomInclusive, int topExclusive) {
/* 632 */       for (int pillarY = bottomInclusive; pillarY < topExclusive; pillarY++) {
/* 633 */         level.setBlock(pos.setY(pillarY), pillarState, 2);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 638 */     private boolean canPlaceColumnOnTopOf(LevelReader level, BlockPos posBelow, BlockState stateBelow) { return stateBelow.isFaceSturdy(level, posBelow, Direction.UP); }
/*     */ 
/*     */ 
/*     */     
/* 642 */     private boolean canHangChainBelow(LevelReader level, BlockPos posAbove, BlockState stateAbove) { return (Block.canSupportCenter(level, posAbove, Direction.DOWN) && !(stateAbove.getBlock() instanceof net.minecraft.world.level.block.FallingBlock)); }
/*     */ 
/*     */ 
/*     */     
/*     */     private void placeSupport(WorldGenLevel level, BoundingBox chunkBB, int x0, int y0, int z, int y1, int x1, RandomSource random) {
/* 647 */       if (!isSupportingBox(level, chunkBB, x0, x1, y1, z)) {
/*     */         return;
/*     */       }
/*     */       
/* 651 */       BlockState planksBlock = this.type.getPlanksState();
/* 652 */       BlockState fenceBlock = this.type.getFenceState();
/*     */       
/* 654 */       generateBox(level, chunkBB, x0, y0, z, x0, y1 - 1, z, (BlockState)fenceBlock.setValue(FenceBlock.WEST, Boolean.valueOf(true)), CAVE_AIR, false);
/* 655 */       generateBox(level, chunkBB, x1, y0, z, x1, y1 - 1, z, (BlockState)fenceBlock.setValue(FenceBlock.EAST, Boolean.valueOf(true)), CAVE_AIR, false);
/* 656 */       if (random.nextInt(4) == 0) {
/* 657 */         generateBox(level, chunkBB, x0, y1, z, x0, y1, z, planksBlock, CAVE_AIR, false);
/* 658 */         generateBox(level, chunkBB, x1, y1, z, x1, y1, z, planksBlock, CAVE_AIR, false);
/*     */       } else {
/* 660 */         generateBox(level, chunkBB, x0, y1, z, x1, y1, z, planksBlock, CAVE_AIR, false);
/* 661 */         maybeGenerateBlock(level, chunkBB, random, 0.05F, x0 + 1, y1, z - 1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.SOUTH));
/* 662 */         maybeGenerateBlock(level, chunkBB, random, 0.05F, x0 + 1, y1, z + 1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.NORTH));
/*     */       } 
/*     */     }
/*     */     
/*     */     private void maybePlaceCobWeb(WorldGenLevel level, BoundingBox chunkBB, RandomSource random, float probability, int x, int y, int z) {
/* 667 */       if (isInterior(level, x, y, z, chunkBB) && random.nextFloat() < probability && hasSturdyNeighbours(level, chunkBB, x, y, z, 2)) {
/* 668 */         placeBlock(level, Blocks.COBWEB.defaultBlockState(), x, y, z, chunkBB);
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean hasSturdyNeighbours(WorldGenLevel level, BoundingBox chunkBB, int x, int y, int z, int count) {
/* 673 */       BlockPos.MutableBlockPos worldPos = getWorldPos(x, y, z);
/* 674 */       int sturdyNeighbours = 0;
/* 675 */       for (Direction direction : Direction.values()) {
/* 676 */         worldPos.move(direction);
/*     */         
/* 678 */         sturdyNeighbours++;
/* 679 */         if (chunkBB.isInside(worldPos) && level.getBlockState(worldPos).isFaceSturdy(level, worldPos, direction.getOpposite()) && sturdyNeighbours >= count) {
/* 680 */           return true;
/*     */         }
/*     */         
/* 683 */         worldPos.move(direction.getOpposite());
/*     */       } 
/* 685 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MineShaftCrossing extends MineShaftPiece {
/*     */     private final Direction direction;
/*     */     private final boolean isTwoFloored;
/*     */     
/*     */     public MineShaftCrossing(CompoundTag tag) {
/* 694 */       super(StructurePieceType.MINE_SHAFT_CROSSING, tag);
/* 695 */       this.isTwoFloored = tag.getBooleanOr("tf", false);
/* 696 */       this.direction = (Direction)tag.read("D", Direction.LEGACY_ID_CODEC_2D).orElse(Direction.SOUTH);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 701 */       super.addAdditionalSaveData(context, tag);
/* 702 */       tag.putBoolean("tf", this.isTwoFloored);
/* 703 */       tag.store("D", Direction.LEGACY_ID_CODEC_2D, this.direction);
/*     */     }
/*     */     
/*     */     public MineShaftCrossing(int genDepth, BoundingBox boundingBox, Direction direction, MineshaftStructure.Type type) {
/* 707 */       super(StructurePieceType.MINE_SHAFT_CROSSING, genDepth, type, boundingBox);
/*     */       
/* 709 */       this.direction = direction;
/* 710 */       this.isTwoFloored = (boundingBox.getYSpan() > 3);
/*     */     }
/*     */     public static BoundingBox findCrossing(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction) {
/*     */       BoundingBox box, box, box, box;
/*     */       int y1;
/* 715 */       if (random.nextInt(4) == 0) {
/* 716 */         y1 = 6;
/*     */       } else {
/* 718 */         y1 = 2;
/*     */       } 
/*     */ 
/*     */       
/* 722 */       switch (MineshaftPieces.null.$SwitchMap$net$minecraft$core$Direction[direction.ordinal()]) {
/*     */         
/*     */         default:
/* 725 */           box = new BoundingBox(-1, 0, -4, 3, y1, 0);
/*     */           break;
/*     */         case 2:
/* 728 */           box = new BoundingBox(-1, 0, 0, 3, y1, 4);
/*     */           break;
/*     */         case 3:
/* 731 */           box = new BoundingBox(-4, 0, -1, 0, y1, 3);
/*     */           break;
/*     */         case 4:
/* 734 */           box = new BoundingBox(0, 0, -1, 4, y1, 3);
/*     */           break;
/*     */       } 
/*     */       
/* 738 */       box.move(footX, footY, footZ);
/*     */       
/* 740 */       if (structurePieceAccessor.findCollisionPiece(box) != null) {
/* 741 */         return null;
/*     */       }
/*     */       
/* 744 */       return box;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 749 */       int depth = getGenDepth();
/*     */ 
/*     */       
/* 752 */       switch (MineshaftPieces.null.$SwitchMap$net$minecraft$core$Direction[this.direction.ordinal()]) {
/*     */         
/*     */         default:
/* 755 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, depth);
/* 756 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, depth);
/* 757 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, depth);
/*     */           break;
/*     */         case 2:
/* 760 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, depth);
/* 761 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, depth);
/* 762 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, depth);
/*     */           break;
/*     */         case 3:
/* 765 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, depth);
/* 766 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, depth);
/* 767 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, depth);
/*     */           break;
/*     */         case 4:
/* 770 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, depth);
/* 771 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, depth);
/* 772 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, depth);
/*     */           break;
/*     */       } 
/*     */       
/* 776 */       if (this.isTwoFloored) {
/* 777 */         if (random.nextBoolean()) {
/* 778 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3 + 1, this.boundingBox.minZ() - 1, Direction.NORTH, depth);
/*     */         }
/* 780 */         if (random.nextBoolean()) {
/* 781 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + 3 + 1, this.boundingBox.minZ() + 1, Direction.WEST, depth);
/*     */         }
/* 783 */         if (random.nextBoolean()) {
/* 784 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + 3 + 1, this.boundingBox.minZ() + 1, Direction.EAST, depth);
/*     */         }
/* 786 */         if (random.nextBoolean()) {
/* 787 */           MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3 + 1, this.boundingBox.maxZ() + 1, Direction.SOUTH, depth);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 794 */       if (isInInvalidLocation(level, chunkBB)) {
/*     */         return;
/*     */       }
/*     */       
/* 798 */       BlockState planks = this.type.getPlanksState();
/*     */ 
/*     */       
/* 801 */       if (this.isTwoFloored) {
/* 802 */         generateBox(level, chunkBB, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.minY() + 3 - 1, this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/* 803 */         generateBox(level, chunkBB, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.minY() + 3 - 1, this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
/* 804 */         generateBox(level, chunkBB, this.boundingBox.minX() + 1, this.boundingBox.maxY() - 2, this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/* 805 */         generateBox(level, chunkBB, this.boundingBox.minX(), this.boundingBox.maxY() - 2, this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
/* 806 */         generateBox(level, chunkBB, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3, this.boundingBox.minZ() + 1, this.boundingBox.maxX() - 1, this.boundingBox.minY() + 3, this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
/*     */       } else {
/* 808 */         generateBox(level, chunkBB, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/* 809 */         generateBox(level, chunkBB, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
/*     */       } 
/*     */ 
/*     */       
/* 813 */       placeSupportPillar(level, chunkBB, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxY());
/* 814 */       placeSupportPillar(level, chunkBB, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 1, this.boundingBox.maxY());
/* 815 */       placeSupportPillar(level, chunkBB, this.boundingBox.maxX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxY());
/* 816 */       placeSupportPillar(level, chunkBB, this.boundingBox.maxX() - 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 1, this.boundingBox.maxY());
/*     */ 
/*     */ 
/*     */       
/* 820 */       int y = this.boundingBox.minY() - 1;
/* 821 */       for (int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); x++) {
/* 822 */         for (int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); z++) {
/* 823 */           setPlanksBlock(level, chunkBB, planks, x, y, z);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     private void placeSupportPillar(WorldGenLevel level, BoundingBox chunkBB, int x, int y0, int z, int y1) {
/* 829 */       if (!getBlock(level, x, y1 + 1, z, chunkBB).isAir())
/* 830 */         generateBox(level, chunkBB, x, y0, z, x, y1, z, this.type.getPlanksState(), CAVE_AIR, false); 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MineShaftStairs
/*     */     extends MineShaftPiece {
/*     */     public MineShaftStairs(int genDepth, BoundingBox boundingBox, Direction direction, MineshaftStructure.Type type) {
/* 837 */       super(StructurePieceType.MINE_SHAFT_STAIRS, genDepth, type, boundingBox);
/* 838 */       setOrientation(direction);
/*     */     }
/*     */ 
/*     */     
/* 842 */     public MineShaftStairs(CompoundTag tag) { super(StructurePieceType.MINE_SHAFT_STAIRS, tag); }
/*     */ 
/*     */ 
/*     */     
/*     */     public static BoundingBox findStairs(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction) {
/*     */       BoundingBox box, box, box, box;
/* 848 */       switch (MineshaftPieces.null.$SwitchMap$net$minecraft$core$Direction[direction.ordinal()]) {
/*     */         
/*     */         default:
/* 851 */           box = new BoundingBox(0, -5, -8, 2, 2, 0);
/*     */           break;
/*     */         case 2:
/* 854 */           box = new BoundingBox(0, -5, 0, 2, 2, 8);
/*     */           break;
/*     */         case 3:
/* 857 */           box = new BoundingBox(-8, -5, 0, 0, 2, 2);
/*     */           break;
/*     */         case 4:
/* 860 */           box = new BoundingBox(0, -5, 0, 8, 2, 2);
/*     */           break;
/*     */       } 
/*     */       
/* 864 */       box.move(footX, footY, footZ);
/*     */       
/* 866 */       if (structurePieceAccessor.findCollisionPiece(box) != null) {
/* 867 */         return null;
/*     */       }
/*     */       
/* 870 */       return box;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 875 */       int depth = getGenDepth();
/*     */ 
/*     */       
/* 878 */       Direction orientation = getOrientation();
/* 879 */       if (orientation != null) {
/* 880 */         switch (MineshaftPieces.null.$SwitchMap$net$minecraft$core$Direction[orientation.ordinal()]) {
/*     */           
/*     */           default:
/* 883 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, depth);
/*     */             return;
/*     */           case 2:
/* 886 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, depth);
/*     */             return;
/*     */           case 3:
/* 889 */             MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ(), Direction.WEST, depth); return;
/*     */           case 4:
/*     */             break;
/* 892 */         }  MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), Direction.EAST, depth);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 900 */       if (isInInvalidLocation(level, chunkBB)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 905 */       generateBox(level, chunkBB, 0, 5, 0, 2, 7, 1, CAVE_AIR, CAVE_AIR, false);
/*     */       
/* 907 */       generateBox(level, chunkBB, 0, 0, 7, 2, 2, 8, CAVE_AIR, CAVE_AIR, false);
/*     */       
/* 909 */       for (int i = 0; i < 5; i++)
/* 910 */         generateBox(level, chunkBB, 0, 5 - i - ((i < 4) ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, CAVE_AIR, CAVE_AIR, false); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\MineshaftPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */