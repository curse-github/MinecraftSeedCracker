/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.StairBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class DesertPyramidPiece
/*     */   extends ScatteredFeaturePiece {
/*     */   public static final int WIDTH = 21;
/*     */   public static final int DEPTH = 21;
/*  27 */   private final boolean[] hasPlacedChest = new boolean[4];
/*  28 */   private final List<BlockPos> potentialSuspiciousSandWorldPositions = new ArrayList();
/*  29 */   private BlockPos randomCollapsedRoofPos = BlockPos.ZERO;
/*     */ 
/*     */   
/*  32 */   public DesertPyramidPiece(RandomSource random, int west, int north) { super(StructurePieceType.DESERT_PYRAMID_PIECE, west, 64, north, 21, 15, 21, getRandomHorizontalDirection(random)); }
/*     */ 
/*     */   
/*     */   public DesertPyramidPiece(CompoundTag tag) {
/*  36 */     super(StructurePieceType.DESERT_PYRAMID_PIECE, tag);
/*  37 */     this.hasPlacedChest[0] = tag.getBooleanOr("hasPlacedChest0", false);
/*  38 */     this.hasPlacedChest[1] = tag.getBooleanOr("hasPlacedChest1", false);
/*  39 */     this.hasPlacedChest[2] = tag.getBooleanOr("hasPlacedChest2", false);
/*  40 */     this.hasPlacedChest[3] = tag.getBooleanOr("hasPlacedChest3", false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  45 */     super.addAdditionalSaveData(context, tag);
/*  46 */     tag.putBoolean("hasPlacedChest0", this.hasPlacedChest[0]);
/*  47 */     tag.putBoolean("hasPlacedChest1", this.hasPlacedChest[1]);
/*  48 */     tag.putBoolean("hasPlacedChest2", this.hasPlacedChest[2]);
/*  49 */     tag.putBoolean("hasPlacedChest3", this.hasPlacedChest[3]);
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  54 */     if (!updateHeightPositionToLowestGroundHeight(level, -random.nextInt(3))) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  59 */     generateBox(level, chunkBB, 0, -4, 0, this.width - 1, 0, this.depth - 1, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/*  60 */     for (int pos = 1; pos <= 9; pos++) {
/*  61 */       generateBox(level, chunkBB, pos, pos, pos, this.width - 1 - pos, pos, this.depth - 1 - pos, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/*  62 */       generateBox(level, chunkBB, pos + 1, pos, pos + 1, this.width - 2 - pos, pos, this.depth - 2 - pos, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*     */     } 
/*  64 */     for (int x = 0; x < this.width; x++) {
/*  65 */       for (int z = 0; z < this.depth; z++) {
/*  66 */         int startY = -5;
/*  67 */         fillColumnDown(level, Blocks.SANDSTONE.defaultBlockState(), x, -5, z, chunkBB);
/*     */       } 
/*     */     } 
/*     */     
/*  71 */     BlockState northStairs = (BlockState)Blocks.SANDSTONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH);
/*  72 */     BlockState southStairs = (BlockState)Blocks.SANDSTONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.SOUTH);
/*  73 */     BlockState eastStairs = (BlockState)Blocks.SANDSTONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.EAST);
/*  74 */     BlockState westStairs = (BlockState)Blocks.SANDSTONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.WEST);
/*     */ 
/*     */     
/*  77 */     generateBox(level, chunkBB, 0, 0, 0, 4, 9, 4, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  78 */     generateBox(level, chunkBB, 1, 10, 1, 3, 10, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/*  79 */     placeBlock(level, northStairs, 2, 10, 0, chunkBB);
/*  80 */     placeBlock(level, southStairs, 2, 10, 4, chunkBB);
/*  81 */     placeBlock(level, eastStairs, 0, 10, 2, chunkBB);
/*  82 */     placeBlock(level, westStairs, 4, 10, 2, chunkBB);
/*  83 */     generateBox(level, chunkBB, this.width - 5, 0, 0, this.width - 1, 9, 4, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  84 */     generateBox(level, chunkBB, this.width - 4, 10, 1, this.width - 2, 10, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/*  85 */     placeBlock(level, northStairs, this.width - 3, 10, 0, chunkBB);
/*  86 */     placeBlock(level, southStairs, this.width - 3, 10, 4, chunkBB);
/*  87 */     placeBlock(level, eastStairs, this.width - 5, 10, 2, chunkBB);
/*  88 */     placeBlock(level, westStairs, this.width - 1, 10, 2, chunkBB);
/*     */ 
/*     */     
/*  91 */     generateBox(level, chunkBB, 8, 0, 0, 12, 4, 4, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  92 */     generateBox(level, chunkBB, 9, 1, 0, 11, 3, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  93 */     placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), 9, 1, 1, chunkBB);
/*  94 */     placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), 9, 2, 1, chunkBB);
/*  95 */     placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), 9, 3, 1, chunkBB);
/*  96 */     placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), 10, 3, 1, chunkBB);
/*  97 */     placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), 11, 3, 1, chunkBB);
/*  98 */     placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), 11, 2, 1, chunkBB);
/*  99 */     placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), 11, 1, 1, chunkBB);
/*     */ 
/*     */     
/* 102 */     generateBox(level, chunkBB, 4, 1, 1, 8, 3, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 103 */     generateBox(level, chunkBB, 4, 1, 2, 8, 2, 2, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 104 */     generateBox(level, chunkBB, 12, 1, 1, 16, 3, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 105 */     generateBox(level, chunkBB, 12, 1, 2, 16, 2, 2, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*     */ 
/*     */     
/* 108 */     generateBox(level, chunkBB, 5, 4, 5, this.width - 6, 4, this.depth - 6, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 109 */     generateBox(level, chunkBB, 9, 4, 9, 11, 4, 11, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 110 */     generateBox(level, chunkBB, 8, 1, 8, 8, 3, 8, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 111 */     generateBox(level, chunkBB, 12, 1, 8, 12, 3, 8, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 112 */     generateBox(level, chunkBB, 8, 1, 12, 8, 3, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 113 */     generateBox(level, chunkBB, 12, 1, 12, 12, 3, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/*     */ 
/*     */     
/* 116 */     generateBox(level, chunkBB, 1, 1, 5, 4, 4, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 117 */     generateBox(level, chunkBB, this.width - 5, 1, 5, this.width - 2, 4, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 118 */     generateBox(level, chunkBB, 6, 7, 9, 6, 7, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 119 */     generateBox(level, chunkBB, this.width - 7, 7, 9, this.width - 7, 7, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 120 */     generateBox(level, chunkBB, 5, 5, 9, 5, 7, 11, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 121 */     generateBox(level, chunkBB, this.width - 6, 5, 9, this.width - 6, 7, 11, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 122 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 5, 5, 10, chunkBB);
/* 123 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 5, 6, 10, chunkBB);
/* 124 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 6, 6, 10, chunkBB);
/* 125 */     placeBlock(level, Blocks.AIR.defaultBlockState(), this.width - 6, 5, 10, chunkBB);
/* 126 */     placeBlock(level, Blocks.AIR.defaultBlockState(), this.width - 6, 6, 10, chunkBB);
/* 127 */     placeBlock(level, Blocks.AIR.defaultBlockState(), this.width - 7, 6, 10, chunkBB);
/*     */ 
/*     */     
/* 130 */     generateBox(level, chunkBB, 2, 4, 4, 2, 6, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 131 */     generateBox(level, chunkBB, this.width - 3, 4, 4, this.width - 3, 6, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 132 */     placeBlock(level, northStairs, 2, 4, 5, chunkBB);
/* 133 */     placeBlock(level, northStairs, 2, 3, 4, chunkBB);
/* 134 */     placeBlock(level, northStairs, this.width - 3, 4, 5, chunkBB);
/* 135 */     placeBlock(level, northStairs, this.width - 3, 3, 4, chunkBB);
/* 136 */     generateBox(level, chunkBB, 1, 1, 3, 2, 2, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 137 */     generateBox(level, chunkBB, this.width - 3, 1, 3, this.width - 2, 2, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 138 */     placeBlock(level, Blocks.SANDSTONE.defaultBlockState(), 1, 1, 2, chunkBB);
/* 139 */     placeBlock(level, Blocks.SANDSTONE.defaultBlockState(), this.width - 2, 1, 2, chunkBB);
/* 140 */     placeBlock(level, Blocks.SANDSTONE_SLAB.defaultBlockState(), 1, 2, 2, chunkBB);
/* 141 */     placeBlock(level, Blocks.SANDSTONE_SLAB.defaultBlockState(), this.width - 2, 2, 2, chunkBB);
/* 142 */     placeBlock(level, westStairs, 2, 1, 2, chunkBB);
/* 143 */     placeBlock(level, eastStairs, this.width - 3, 1, 2, chunkBB);
/*     */ 
/*     */     
/* 146 */     generateBox(level, chunkBB, 4, 3, 5, 4, 3, 17, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 147 */     generateBox(level, chunkBB, this.width - 5, 3, 5, this.width - 5, 3, 17, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 148 */     generateBox(level, chunkBB, 3, 1, 5, 4, 2, 16, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 149 */     generateBox(level, chunkBB, this.width - 6, 1, 5, this.width - 5, 2, 16, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 150 */     for (int z = 5; z <= 17; z += 2) {
/* 151 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), 4, 1, z, chunkBB);
/* 152 */       placeBlock(level, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 4, 2, z, chunkBB);
/* 153 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), this.width - 5, 1, z, chunkBB);
/* 154 */       placeBlock(level, Blocks.CHISELED_SANDSTONE.defaultBlockState(), this.width - 5, 2, z, chunkBB);
/*     */     } 
/* 156 */     placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 7, chunkBB);
/* 157 */     placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 8, chunkBB);
/* 158 */     placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 9, 0, 9, chunkBB);
/* 159 */     placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 11, 0, 9, chunkBB);
/* 160 */     placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 8, 0, 10, chunkBB);
/* 161 */     placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 12, 0, 10, chunkBB);
/* 162 */     placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 7, 0, 10, chunkBB);
/* 163 */     placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 13, 0, 10, chunkBB);
/* 164 */     placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 9, 0, 11, chunkBB);
/* 165 */     placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 11, 0, 11, chunkBB);
/* 166 */     placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 12, chunkBB);
/* 167 */     placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 13, chunkBB);
/* 168 */     placeBlock(level, Blocks.BLUE_TERRACOTTA.defaultBlockState(), 10, 0, 10, chunkBB);
/*     */     
/*     */     int x;
/* 171 */     for (x = 0; x <= this.width - 1; x += this.width - 1) {
/* 172 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x, 2, 1, chunkBB);
/* 173 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x, 2, 2, chunkBB);
/* 174 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x, 2, 3, chunkBB);
/* 175 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x, 3, 1, chunkBB);
/* 176 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x, 3, 2, chunkBB);
/* 177 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x, 3, 3, chunkBB);
/* 178 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x, 4, 1, chunkBB);
/* 179 */       placeBlock(level, Blocks.CHISELED_SANDSTONE.defaultBlockState(), x, 4, 2, chunkBB);
/* 180 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x, 4, 3, chunkBB);
/* 181 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x, 5, 1, chunkBB);
/* 182 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x, 5, 2, chunkBB);
/* 183 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x, 5, 3, chunkBB);
/* 184 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x, 6, 1, chunkBB);
/* 185 */       placeBlock(level, Blocks.CHISELED_SANDSTONE.defaultBlockState(), x, 6, 2, chunkBB);
/* 186 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x, 6, 3, chunkBB);
/* 187 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x, 7, 1, chunkBB);
/* 188 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x, 7, 2, chunkBB);
/* 189 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x, 7, 3, chunkBB);
/* 190 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x, 8, 1, chunkBB);
/* 191 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x, 8, 2, chunkBB);
/* 192 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x, 8, 3, chunkBB);
/*     */     }  int x;
/* 194 */     for (x = 2; x <= this.width - 3; x += this.width - 3 - 2) {
/* 195 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x - 1, 2, 0, chunkBB);
/* 196 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x, 2, 0, chunkBB);
/* 197 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x + 1, 2, 0, chunkBB);
/* 198 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x - 1, 3, 0, chunkBB);
/* 199 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x, 3, 0, chunkBB);
/* 200 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x + 1, 3, 0, chunkBB);
/* 201 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x - 1, 4, 0, chunkBB);
/* 202 */       placeBlock(level, Blocks.CHISELED_SANDSTONE.defaultBlockState(), x, 4, 0, chunkBB);
/* 203 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x + 1, 4, 0, chunkBB);
/* 204 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x - 1, 5, 0, chunkBB);
/* 205 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x, 5, 0, chunkBB);
/* 206 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x + 1, 5, 0, chunkBB);
/* 207 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x - 1, 6, 0, chunkBB);
/* 208 */       placeBlock(level, Blocks.CHISELED_SANDSTONE.defaultBlockState(), x, 6, 0, chunkBB);
/* 209 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x + 1, 6, 0, chunkBB);
/* 210 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x - 1, 7, 0, chunkBB);
/* 211 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x, 7, 0, chunkBB);
/* 212 */       placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), x + 1, 7, 0, chunkBB);
/* 213 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x - 1, 8, 0, chunkBB);
/* 214 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x, 8, 0, chunkBB);
/* 215 */       placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), x + 1, 8, 0, chunkBB);
/*     */     } 
/* 217 */     generateBox(level, chunkBB, 8, 4, 0, 12, 6, 0, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 218 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 8, 6, 0, chunkBB);
/* 219 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 12, 6, 0, chunkBB);
/* 220 */     placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 9, 5, 0, chunkBB);
/* 221 */     placeBlock(level, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 10, 5, 0, chunkBB);
/* 222 */     placeBlock(level, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 11, 5, 0, chunkBB);
/*     */ 
/*     */     
/* 225 */     generateBox(level, chunkBB, 8, -14, 8, 12, -11, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 226 */     generateBox(level, chunkBB, 8, -10, 8, 12, -10, 12, Blocks.CHISELED_SANDSTONE.defaultBlockState(), Blocks.CHISELED_SANDSTONE.defaultBlockState(), false);
/* 227 */     generateBox(level, chunkBB, 8, -9, 8, 12, -9, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 228 */     generateBox(level, chunkBB, 8, -8, 8, 12, -1, 12, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 229 */     generateBox(level, chunkBB, 9, -11, 9, 11, -1, 11, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 230 */     placeBlock(level, Blocks.STONE_PRESSURE_PLATE.defaultBlockState(), 10, -11, 10, chunkBB);
/* 231 */     generateBox(level, chunkBB, 9, -13, 9, 11, -13, 11, Blocks.TNT.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 232 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 8, -11, 10, chunkBB);
/* 233 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 8, -10, 10, chunkBB);
/* 234 */     placeBlock(level, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 7, -10, 10, chunkBB);
/* 235 */     placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), 7, -11, 10, chunkBB);
/* 236 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 12, -11, 10, chunkBB);
/* 237 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 12, -10, 10, chunkBB);
/* 238 */     placeBlock(level, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 13, -10, 10, chunkBB);
/* 239 */     placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), 13, -11, 10, chunkBB);
/* 240 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 10, -11, 8, chunkBB);
/* 241 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 10, -10, 8, chunkBB);
/* 242 */     placeBlock(level, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 10, -10, 7, chunkBB);
/* 243 */     placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), 10, -11, 7, chunkBB);
/* 244 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 10, -11, 12, chunkBB);
/* 245 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 10, -10, 12, chunkBB);
/* 246 */     placeBlock(level, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 10, -10, 13, chunkBB);
/* 247 */     placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), 10, -11, 13, chunkBB);
/*     */ 
/*     */     
/* 250 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 251 */       if (!this.hasPlacedChest[direction.get2DDataValue()]) {
/* 252 */         int xo = direction.getStepX() * 2;
/* 253 */         int zo = direction.getStepZ() * 2;
/* 254 */         this.hasPlacedChest[direction.get2DDataValue()] = createChest(level, chunkBB, random, 10 + xo, -11, 10 + zo, BuiltInLootTables.DESERT_PYRAMID);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 259 */     addCellar(level, chunkBB);
/*     */   }
/*     */   
/*     */   private void addCellar(WorldGenLevel level, BoundingBox chunkBB) {
/* 263 */     BlockPos roomCenter = new BlockPos(16, -4, 13);
/*     */     
/* 265 */     addCellarStairs(roomCenter, level, chunkBB);
/* 266 */     addCellarRoom(roomCenter, level, chunkBB);
/*     */   }
/*     */   
/*     */   private void addCellarStairs(BlockPos roomCenter, WorldGenLevel level, BoundingBox chunkBB) {
/* 270 */     int x = roomCenter.getX();
/* 271 */     int y = roomCenter.getY();
/* 272 */     int z = roomCenter.getZ();
/*     */ 
/*     */     
/* 275 */     BlockState sandStoneStairs = Blocks.SANDSTONE_STAIRS.defaultBlockState();
/* 276 */     placeBlock(level, sandStoneStairs.rotate(Rotation.COUNTERCLOCKWISE_90), 13, -1, 17, chunkBB);
/* 277 */     placeBlock(level, sandStoneStairs.rotate(Rotation.COUNTERCLOCKWISE_90), 14, -2, 17, chunkBB);
/* 278 */     placeBlock(level, sandStoneStairs.rotate(Rotation.COUNTERCLOCKWISE_90), 15, -3, 17, chunkBB);
/*     */     
/* 280 */     BlockState sand = Blocks.SAND.defaultBlockState();
/* 281 */     BlockState sandStone = Blocks.SANDSTONE.defaultBlockState();
/* 282 */     boolean variant = level.getRandom().nextBoolean();
/* 283 */     placeBlock(level, sand, x - 4, y + 4, z + 4, chunkBB);
/* 284 */     placeBlock(level, sand, x - 3, y + 4, z + 4, chunkBB);
/* 285 */     placeBlock(level, sand, x - 2, y + 4, z + 4, chunkBB);
/* 286 */     placeBlock(level, sand, x - 1, y + 4, z + 4, chunkBB);
/* 287 */     placeBlock(level, sand, x, y + 4, z + 4, chunkBB);
/*     */ 
/*     */     
/* 290 */     placeBlock(level, sand, x - 2, y + 3, z + 4, chunkBB);
/* 291 */     placeBlock(level, variant ? sand : sandStone, x - 1, y + 3, z + 4, chunkBB);
/* 292 */     placeBlock(level, !variant ? sand : sandStone, x, y + 3, z + 4, chunkBB);
/*     */     
/* 294 */     placeBlock(level, sand, x - 1, y + 2, z + 4, chunkBB);
/* 295 */     placeBlock(level, sandStone, x, y + 2, z + 4, chunkBB);
/* 296 */     placeBlock(level, sand, x, y + 1, z + 4, chunkBB);
/*     */   }
/*     */   
/*     */   private void addCellarRoom(BlockPos roomCenter, WorldGenLevel level, BoundingBox chunkBB) {
/* 300 */     int x = roomCenter.getX();
/* 301 */     int y = roomCenter.getY();
/* 302 */     int z = roomCenter.getZ();
/*     */ 
/*     */     
/* 305 */     BlockState cutSandStone = Blocks.CUT_SANDSTONE.defaultBlockState();
/* 306 */     BlockState hieroglyphsSandStone = Blocks.CHISELED_SANDSTONE.defaultBlockState();
/* 307 */     generateBox(level, chunkBB, x - 3, y + 1, z - 3, x - 3, y + 1, z + 2, cutSandStone, cutSandStone, true);
/* 308 */     generateBox(level, chunkBB, x + 3, y + 1, z - 3, x + 3, y + 1, z + 2, cutSandStone, cutSandStone, true);
/* 309 */     generateBox(level, chunkBB, x - 3, y + 1, z - 3, x + 3, y + 1, z - 2, cutSandStone, cutSandStone, true);
/* 310 */     generateBox(level, chunkBB, x - 3, y + 1, z + 3, x + 3, y + 1, z + 3, cutSandStone, cutSandStone, true);
/*     */     
/* 312 */     generateBox(level, chunkBB, x - 3, y + 2, z - 3, x - 3, y + 2, z + 2, hieroglyphsSandStone, hieroglyphsSandStone, true);
/* 313 */     generateBox(level, chunkBB, x + 3, y + 2, z - 3, x + 3, y + 2, z + 2, hieroglyphsSandStone, hieroglyphsSandStone, true);
/* 314 */     generateBox(level, chunkBB, x - 3, y + 2, z - 3, x + 3, y + 2, z - 2, hieroglyphsSandStone, hieroglyphsSandStone, true);
/* 315 */     generateBox(level, chunkBB, x - 3, y + 2, z + 3, x + 3, y + 2, z + 3, hieroglyphsSandStone, hieroglyphsSandStone, true);
/*     */     
/* 317 */     generateBox(level, chunkBB, x - 3, -1, z - 3, x - 3, -1, z + 2, cutSandStone, cutSandStone, true);
/* 318 */     generateBox(level, chunkBB, x + 3, -1, z - 3, x + 3, -1, z + 2, cutSandStone, cutSandStone, true);
/* 319 */     generateBox(level, chunkBB, x - 3, -1, z - 3, x + 3, -1, z - 2, cutSandStone, cutSandStone, true);
/* 320 */     generateBox(level, chunkBB, x - 3, -1, z + 3, x + 3, -1, z + 3, cutSandStone, cutSandStone, true);
/*     */     
/* 322 */     placeSandBox(x - 2, y + 1, z - 2, x + 2, y + 3, z + 2);
/* 323 */     placeCollapsedRoof(level, chunkBB, x - 2, y + 4, z - 2, x + 2, z + 2);
/*     */     
/* 325 */     BlockState orangeTeracotta = Blocks.ORANGE_TERRACOTTA.defaultBlockState();
/* 326 */     BlockState blueTeracotta = Blocks.BLUE_TERRACOTTA.defaultBlockState();
/*     */ 
/*     */     
/* 329 */     placeBlock(level, blueTeracotta, x, y, z, chunkBB);
/*     */     
/* 331 */     placeBlock(level, orangeTeracotta, x + 1, y, z - 1, chunkBB);
/* 332 */     placeBlock(level, orangeTeracotta, x + 1, y, z + 1, chunkBB);
/* 333 */     placeBlock(level, orangeTeracotta, x - 1, y, z - 1, chunkBB);
/* 334 */     placeBlock(level, orangeTeracotta, x - 1, y, z + 1, chunkBB);
/*     */     
/* 336 */     placeBlock(level, orangeTeracotta, x + 2, y, z, chunkBB);
/* 337 */     placeBlock(level, orangeTeracotta, x - 2, y, z, chunkBB);
/* 338 */     placeBlock(level, orangeTeracotta, x, y, z + 2, chunkBB);
/* 339 */     placeBlock(level, orangeTeracotta, x, y, z - 2, chunkBB);
/*     */ 
/*     */     
/* 342 */     placeBlock(level, orangeTeracotta, x + 3, y, z, chunkBB);
/* 343 */     placeSand(x + 3, y + 1, z);
/* 344 */     placeSand(x + 3, y + 2, z);
/* 345 */     placeBlock(level, cutSandStone, x + 4, y + 1, z, chunkBB);
/* 346 */     placeBlock(level, hieroglyphsSandStone, x + 4, y + 2, z, chunkBB);
/*     */     
/* 348 */     placeBlock(level, orangeTeracotta, x - 3, y, z, chunkBB);
/* 349 */     placeSand(x - 3, y + 1, z);
/* 350 */     placeSand(x - 3, y + 2, z);
/* 351 */     placeBlock(level, cutSandStone, x - 4, y + 1, z, chunkBB);
/* 352 */     placeBlock(level, hieroglyphsSandStone, x - 4, y + 2, z, chunkBB);
/*     */     
/* 354 */     placeBlock(level, orangeTeracotta, x, y, z + 3, chunkBB);
/* 355 */     placeSand(x, y + 1, z + 3);
/* 356 */     placeSand(x, y + 2, z + 3);
/*     */     
/* 358 */     placeBlock(level, orangeTeracotta, x, y, z - 3, chunkBB);
/* 359 */     placeSand(x, y + 1, z - 3);
/* 360 */     placeSand(x, y + 2, z - 3);
/* 361 */     placeBlock(level, cutSandStone, x, y + 1, z - 4, chunkBB);
/* 362 */     placeBlock(level, hieroglyphsSandStone, x, -2, z - 4, chunkBB);
/*     */   }
/*     */   
/*     */   private void placeSand(int x, int y, int z) {
/* 366 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(x, y, z);
/* 367 */     this.potentialSuspiciousSandWorldPositions.add(mutableBlockPos);
/*     */   }
/*     */   
/*     */   private void placeSandBox(int x0, int y0, int z0, int x1, int y1, int z1) {
/* 371 */     for (int y = y0; y <= y1; y++) {
/* 372 */       for (int x = x0; x <= x1; x++) {
/* 373 */         for (int z = z0; z <= z1; z++) {
/* 374 */           placeSand(x, y, z);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void placeCollapsedRoofPiece(WorldGenLevel level, int x, int y, int z, BoundingBox chunkBB) {
/* 382 */     if (level.getRandom().nextFloat() < 0.33F) {
/* 383 */       BlockState blockState = Blocks.SANDSTONE.defaultBlockState();
/* 384 */       placeBlock(level, blockState, x, y, z, chunkBB);
/*     */     } else {
/* 386 */       BlockState blockState = Blocks.SAND.defaultBlockState();
/* 387 */       placeBlock(level, blockState, x, y, z, chunkBB);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeCollapsedRoof(WorldGenLevel level, BoundingBox chunkBB, int x0, int y0, int z0, int x1, int z1) {
/* 392 */     for (int x = x0; x <= x1; x++) {
/* 393 */       for (int z = z0; z <= z1; z++) {
/* 394 */         placeCollapsedRoofPiece(level, x, y0, z, chunkBB);
/*     */       }
/*     */     } 
/*     */     
/* 398 */     RandomSource random = RandomSource.create(level.getSeed()).forkPositional().at(getWorldPos(x0, y0, z0));
/* 399 */     int roofPosX = random.nextIntBetweenInclusive(x0, x1);
/* 400 */     int roofPosZ = random.nextIntBetweenInclusive(z0, z1);
/* 401 */     this.randomCollapsedRoofPos = new BlockPos(getWorldX(roofPosX, roofPosZ), getWorldY(y0), getWorldZ(roofPosX, roofPosZ));
/*     */   }
/*     */ 
/*     */   
/* 405 */   public List<BlockPos> getPotentialSuspiciousSandWorldPositions() { return this.potentialSuspiciousSandWorldPositions; }
/*     */ 
/*     */ 
/*     */   
/* 409 */   public BlockPos getRandomCollapsedRoofPos() { return this.randomCollapsedRoofPos; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\DesertPyramidPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */