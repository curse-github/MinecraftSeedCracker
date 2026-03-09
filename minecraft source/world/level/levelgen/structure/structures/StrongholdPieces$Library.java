/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.StructureManager;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.FenceBlock;
/*      */ import net.minecraft.world.level.block.LadderBlock;
/*      */ import net.minecraft.world.level.block.WallTorchBlock;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Library
/*      */   extends StrongholdPieces.StrongholdPiece
/*      */ {
/*      */   protected static final int WIDTH = 14;
/*      */   protected static final int HEIGHT = 6;
/*      */   protected static final int TALL_HEIGHT = 11;
/*      */   protected static final int DEPTH = 15;
/*      */   private final boolean isTall;
/*      */   
/*      */   public Library(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 1069 */     super(StructurePieceType.STRONGHOLD_LIBRARY, genDepth, boundingBox);
/*      */     
/* 1071 */     setOrientation(direction);
/* 1072 */     this.entryDoor = randomSmallDoor(random);
/* 1073 */     this.isTall = (boundingBox.getYSpan() > 6);
/*      */   }
/*      */   
/*      */   public Library(CompoundTag tag) {
/* 1077 */     super(StructurePieceType.STRONGHOLD_LIBRARY, tag);
/* 1078 */     this.isTall = tag.getBooleanOr("Tall", false);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 1083 */     super.addAdditionalSaveData(context, tag);
/* 1084 */     tag.putBoolean("Tall", this.isTall);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Library createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1089 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 14, 11, 15, direction);
/*      */     
/* 1091 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/*      */       
/* 1093 */       box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 14, 6, 15, direction);
/*      */       
/* 1095 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/* 1096 */         return null;
/*      */       }
/*      */     } 
/*      */     
/* 1100 */     return new Library(genDepth, random, box, direction);
/*      */   }
/*      */ 
/*      */   
/*      */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1105 */     int currentHeight = 11;
/* 1106 */     if (!this.isTall) {
/* 1107 */       currentHeight = 6;
/*      */     }
/*      */ 
/*      */     
/* 1111 */     generateBox(level, chunkBB, 0, 0, 0, 13, currentHeight - 1, 14, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */     
/* 1113 */     generateSmallDoor(level, random, chunkBB, this.entryDoor, 4, 1, 0);
/*      */ 
/*      */     
/* 1116 */     generateMaybeBox(level, chunkBB, random, 0.07F, 2, 1, 1, 11, 4, 13, Blocks.COBWEB.defaultBlockState(), Blocks.COBWEB.defaultBlockState(), false, false);
/*      */     
/* 1118 */     int bookLeft = 1;
/* 1119 */     int bookRight = 12;
/*      */ 
/*      */     
/* 1122 */     for (int d = 1; d <= 13; d++) {
/* 1123 */       if ((d - 1) % 4 == 0) {
/* 1124 */         generateBox(level, chunkBB, 1, 1, d, 1, 4, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1125 */         generateBox(level, chunkBB, 12, 1, d, 12, 4, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/*      */         
/* 1127 */         placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.EAST), 2, 3, d, chunkBB);
/* 1128 */         placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.WEST), 11, 3, d, chunkBB);
/*      */         
/* 1130 */         if (this.isTall) {
/* 1131 */           generateBox(level, chunkBB, 1, 6, d, 1, 9, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1132 */           generateBox(level, chunkBB, 12, 6, d, 12, 9, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/*      */         } 
/*      */       } else {
/* 1135 */         generateBox(level, chunkBB, 1, 1, d, 1, 4, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1136 */         generateBox(level, chunkBB, 12, 1, d, 12, 4, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/*      */         
/* 1138 */         if (this.isTall) {
/* 1139 */           generateBox(level, chunkBB, 1, 6, d, 1, 9, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1140 */           generateBox(level, chunkBB, 12, 6, d, 12, 9, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1146 */     for (int d = 3; d < 12; d += 2) {
/* 1147 */       generateBox(level, chunkBB, 3, 1, d, 4, 3, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1148 */       generateBox(level, chunkBB, 6, 1, d, 7, 3, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1149 */       generateBox(level, chunkBB, 9, 1, d, 10, 3, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/*      */     } 
/*      */     
/* 1152 */     if (this.isTall) {
/*      */       
/* 1154 */       generateBox(level, chunkBB, 1, 5, 1, 3, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1155 */       generateBox(level, chunkBB, 10, 5, 1, 12, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1156 */       generateBox(level, chunkBB, 4, 5, 1, 9, 5, 2, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1157 */       generateBox(level, chunkBB, 4, 5, 12, 9, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/*      */       
/* 1159 */       placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 11, chunkBB);
/* 1160 */       placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 8, 5, 11, chunkBB);
/* 1161 */       placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 10, chunkBB);
/*      */       
/* 1163 */       BlockState weFence = (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true));
/* 1164 */       BlockState nsFence = (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */ 
/*      */       
/* 1167 */       generateBox(level, chunkBB, 3, 6, 3, 3, 6, 11, nsFence, nsFence, false);
/* 1168 */       generateBox(level, chunkBB, 10, 6, 3, 10, 6, 9, nsFence, nsFence, false);
/* 1169 */       generateBox(level, chunkBB, 4, 6, 2, 9, 6, 2, weFence, weFence, false);
/* 1170 */       generateBox(level, chunkBB, 4, 6, 12, 7, 6, 12, weFence, weFence, false);
/*      */       
/* 1172 */       placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true)), 3, 6, 2, chunkBB);
/* 1173 */       placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.SOUTH, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true)), 3, 6, 12, chunkBB);
/* 1174 */       placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.WEST, Boolean.valueOf(true)), 10, 6, 2, chunkBB);
/*      */       
/* 1176 */       for (int i = 0; i <= 2; i++) {
/* 1177 */         placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.SOUTH, Boolean.valueOf(true))).setValue(FenceBlock.WEST, Boolean.valueOf(true)), 8 + i, 6, 12 - i, chunkBB);
/* 1178 */         if (i != 2) {
/* 1179 */           placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true)), 8 + i, 6, 11 - i, chunkBB);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1184 */       BlockState ladder = (BlockState)Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.SOUTH);
/* 1185 */       placeBlock(level, ladder, 10, 1, 13, chunkBB);
/* 1186 */       placeBlock(level, ladder, 10, 2, 13, chunkBB);
/* 1187 */       placeBlock(level, ladder, 10, 3, 13, chunkBB);
/* 1188 */       placeBlock(level, ladder, 10, 4, 13, chunkBB);
/* 1189 */       placeBlock(level, ladder, 10, 5, 13, chunkBB);
/* 1190 */       placeBlock(level, ladder, 10, 6, 13, chunkBB);
/* 1191 */       placeBlock(level, ladder, 10, 7, 13, chunkBB);
/*      */ 
/*      */       
/* 1194 */       int x = 7;
/* 1195 */       int z = 7;
/* 1196 */       BlockState eFence = (BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.EAST, Boolean.valueOf(true));
/* 1197 */       placeBlock(level, eFence, 6, 9, 7, chunkBB);
/* 1198 */       BlockState wFence = (BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, Boolean.valueOf(true));
/* 1199 */       placeBlock(level, wFence, 7, 9, 7, chunkBB);
/*      */       
/* 1201 */       placeBlock(level, eFence, 6, 8, 7, chunkBB);
/* 1202 */       placeBlock(level, wFence, 7, 8, 7, chunkBB);
/*      */       
/* 1204 */       BlockState nsweFence = (BlockState)((BlockState)nsFence.setValue(FenceBlock.WEST, Boolean.valueOf(true))).setValue(FenceBlock.EAST, Boolean.valueOf(true));
/*      */       
/* 1206 */       placeBlock(level, nsweFence, 6, 7, 7, chunkBB);
/* 1207 */       placeBlock(level, nsweFence, 7, 7, 7, chunkBB);
/*      */       
/* 1209 */       placeBlock(level, eFence, 5, 7, 7, chunkBB);
/*      */       
/* 1211 */       placeBlock(level, wFence, 8, 7, 7, chunkBB);
/*      */       
/* 1213 */       placeBlock(level, (BlockState)eFence.setValue(FenceBlock.NORTH, Boolean.valueOf(true)), 6, 7, 6, chunkBB);
/* 1214 */       placeBlock(level, (BlockState)eFence.setValue(FenceBlock.SOUTH, Boolean.valueOf(true)), 6, 7, 8, chunkBB);
/*      */       
/* 1216 */       placeBlock(level, (BlockState)wFence.setValue(FenceBlock.NORTH, Boolean.valueOf(true)), 7, 7, 6, chunkBB);
/* 1217 */       placeBlock(level, (BlockState)wFence.setValue(FenceBlock.SOUTH, Boolean.valueOf(true)), 7, 7, 8, chunkBB);
/*      */       
/* 1219 */       BlockState torch = Blocks.TORCH.defaultBlockState();
/* 1220 */       placeBlock(level, torch, 5, 8, 7, chunkBB);
/* 1221 */       placeBlock(level, torch, 8, 8, 7, chunkBB);
/* 1222 */       placeBlock(level, torch, 6, 8, 6, chunkBB);
/* 1223 */       placeBlock(level, torch, 6, 8, 8, chunkBB);
/* 1224 */       placeBlock(level, torch, 7, 8, 6, chunkBB);
/* 1225 */       placeBlock(level, torch, 7, 8, 8, chunkBB);
/*      */     } 
/*      */ 
/*      */     
/* 1229 */     createChest(level, chunkBB, random, 3, 3, 5, BuiltInLootTables.STRONGHOLD_LIBRARY);
/* 1230 */     if (this.isTall) {
/* 1231 */       placeBlock(level, CAVE_AIR, 12, 9, 1, chunkBB);
/* 1232 */       createChest(level, chunkBB, random, 12, 8, 1, BuiltInLootTables.STRONGHOLD_LIBRARY);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$Library.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */