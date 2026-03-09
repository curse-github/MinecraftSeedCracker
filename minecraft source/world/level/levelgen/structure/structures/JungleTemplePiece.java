/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LeverBlock;
/*     */ import net.minecraft.world.level.block.RedStoneWireBlock;
/*     */ import net.minecraft.world.level.block.RepeaterBlock;
/*     */ import net.minecraft.world.level.block.StairBlock;
/*     */ import net.minecraft.world.level.block.TripWireBlock;
/*     */ import net.minecraft.world.level.block.TripWireHookBlock;
/*     */ import net.minecraft.world.level.block.VineBlock;
/*     */ import net.minecraft.world.level.block.piston.PistonBaseBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*     */ import net.minecraft.world.level.block.state.properties.RedstoneSide;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class JungleTemplePiece extends ScatteredFeaturePiece {
/*     */   public static final int WIDTH = 12;
/*     */   public static final int DEPTH = 15;
/*     */   private boolean placedMainChest;
/*     */   private boolean placedHiddenChest;
/*     */   private boolean placedTrap1;
/*     */   private boolean placedTrap2;
/*     */   
/*  38 */   public JungleTemplePiece(RandomSource random, int west, int north) { super(StructurePieceType.JUNGLE_PYRAMID_PIECE, west, 64, north, 12, 10, 15, getRandomHorizontalDirection(random)); }
/*     */ 
/*     */   
/*     */   public JungleTemplePiece(CompoundTag tag) {
/*  42 */     super(StructurePieceType.JUNGLE_PYRAMID_PIECE, tag);
/*  43 */     this.placedMainChest = tag.getBooleanOr("placedMainChest", false);
/*  44 */     this.placedHiddenChest = tag.getBooleanOr("placedHiddenChest", false);
/*  45 */     this.placedTrap1 = tag.getBooleanOr("placedTrap1", false);
/*  46 */     this.placedTrap2 = tag.getBooleanOr("placedTrap2", false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  51 */     super.addAdditionalSaveData(context, tag);
/*  52 */     tag.putBoolean("placedMainChest", this.placedMainChest);
/*  53 */     tag.putBoolean("placedHiddenChest", this.placedHiddenChest);
/*  54 */     tag.putBoolean("placedTrap1", this.placedTrap1);
/*  55 */     tag.putBoolean("placedTrap2", this.placedTrap2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  60 */     if (!updateAverageGroundHeight(level, chunkBB, 0)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  65 */     generateBox(level, chunkBB, 0, -4, 0, this.width - 1, 0, this.depth - 1, false, random, STONE_SELECTOR);
/*     */ 
/*     */     
/*  68 */     generateBox(level, chunkBB, 2, 1, 2, 9, 2, 2, false, random, STONE_SELECTOR);
/*  69 */     generateBox(level, chunkBB, 2, 1, 12, 9, 2, 12, false, random, STONE_SELECTOR);
/*  70 */     generateBox(level, chunkBB, 2, 1, 3, 2, 2, 11, false, random, STONE_SELECTOR);
/*  71 */     generateBox(level, chunkBB, 9, 1, 3, 9, 2, 11, false, random, STONE_SELECTOR);
/*     */ 
/*     */     
/*  74 */     generateBox(level, chunkBB, 1, 3, 1, 10, 6, 1, false, random, STONE_SELECTOR);
/*  75 */     generateBox(level, chunkBB, 1, 3, 13, 10, 6, 13, false, random, STONE_SELECTOR);
/*  76 */     generateBox(level, chunkBB, 1, 3, 2, 1, 6, 12, false, random, STONE_SELECTOR);
/*  77 */     generateBox(level, chunkBB, 10, 3, 2, 10, 6, 12, false, random, STONE_SELECTOR);
/*     */ 
/*     */     
/*  80 */     generateBox(level, chunkBB, 2, 3, 2, 9, 3, 12, false, random, STONE_SELECTOR);
/*  81 */     generateBox(level, chunkBB, 2, 6, 2, 9, 6, 12, false, random, STONE_SELECTOR);
/*  82 */     generateBox(level, chunkBB, 3, 7, 3, 8, 7, 11, false, random, STONE_SELECTOR);
/*  83 */     generateBox(level, chunkBB, 4, 8, 4, 7, 8, 10, false, random, STONE_SELECTOR);
/*     */ 
/*     */     
/*  86 */     generateAirBox(level, chunkBB, 3, 1, 3, 8, 2, 11);
/*  87 */     generateAirBox(level, chunkBB, 4, 3, 6, 7, 3, 9);
/*  88 */     generateAirBox(level, chunkBB, 2, 4, 2, 9, 5, 12);
/*  89 */     generateAirBox(level, chunkBB, 4, 6, 5, 7, 6, 9);
/*  90 */     generateAirBox(level, chunkBB, 5, 7, 6, 6, 7, 8);
/*     */ 
/*     */     
/*  93 */     generateAirBox(level, chunkBB, 5, 1, 2, 6, 2, 2);
/*  94 */     generateAirBox(level, chunkBB, 5, 2, 12, 6, 2, 12);
/*  95 */     generateAirBox(level, chunkBB, 5, 5, 1, 6, 5, 1);
/*  96 */     generateAirBox(level, chunkBB, 5, 5, 13, 6, 5, 13);
/*  97 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 1, 5, 5, chunkBB);
/*  98 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 10, 5, 5, chunkBB);
/*  99 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 1, 5, 9, chunkBB);
/* 100 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 10, 5, 9, chunkBB);
/*     */ 
/*     */     
/* 103 */     for (int z = 0; z <= 14; z += 14) {
/* 104 */       generateBox(level, chunkBB, 2, 4, z, 2, 5, z, false, random, STONE_SELECTOR);
/* 105 */       generateBox(level, chunkBB, 4, 4, z, 4, 5, z, false, random, STONE_SELECTOR);
/* 106 */       generateBox(level, chunkBB, 7, 4, z, 7, 5, z, false, random, STONE_SELECTOR);
/* 107 */       generateBox(level, chunkBB, 9, 4, z, 9, 5, z, false, random, STONE_SELECTOR);
/*     */     } 
/* 109 */     generateBox(level, chunkBB, 5, 6, 0, 6, 6, 0, false, random, STONE_SELECTOR);
/* 110 */     for (int x = 0; x <= 11; x += 11) {
/* 111 */       for (int z = 2; z <= 12; z += 2) {
/* 112 */         generateBox(level, chunkBB, x, 4, z, x, 5, z, false, random, STONE_SELECTOR);
/*     */       }
/* 114 */       generateBox(level, chunkBB, x, 6, 5, x, 6, 5, false, random, STONE_SELECTOR);
/* 115 */       generateBox(level, chunkBB, x, 6, 9, x, 6, 9, false, random, STONE_SELECTOR);
/*     */     } 
/* 117 */     generateBox(level, chunkBB, 2, 7, 2, 2, 9, 2, false, random, STONE_SELECTOR);
/* 118 */     generateBox(level, chunkBB, 9, 7, 2, 9, 9, 2, false, random, STONE_SELECTOR);
/* 119 */     generateBox(level, chunkBB, 2, 7, 12, 2, 9, 12, false, random, STONE_SELECTOR);
/* 120 */     generateBox(level, chunkBB, 9, 7, 12, 9, 9, 12, false, random, STONE_SELECTOR);
/* 121 */     generateBox(level, chunkBB, 4, 9, 4, 4, 9, 4, false, random, STONE_SELECTOR);
/* 122 */     generateBox(level, chunkBB, 7, 9, 4, 7, 9, 4, false, random, STONE_SELECTOR);
/* 123 */     generateBox(level, chunkBB, 4, 9, 10, 4, 9, 10, false, random, STONE_SELECTOR);
/* 124 */     generateBox(level, chunkBB, 7, 9, 10, 7, 9, 10, false, random, STONE_SELECTOR);
/* 125 */     generateBox(level, chunkBB, 5, 9, 7, 6, 9, 7, false, random, STONE_SELECTOR);
/*     */     
/* 127 */     BlockState eastStairs = (BlockState)Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.EAST);
/* 128 */     BlockState westStairs = (BlockState)Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.WEST);
/* 129 */     BlockState southStairs = (BlockState)Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.SOUTH);
/* 130 */     BlockState northStairs = (BlockState)Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH);
/*     */     
/* 132 */     placeBlock(level, northStairs, 5, 9, 6, chunkBB);
/* 133 */     placeBlock(level, northStairs, 6, 9, 6, chunkBB);
/* 134 */     placeBlock(level, southStairs, 5, 9, 8, chunkBB);
/* 135 */     placeBlock(level, southStairs, 6, 9, 8, chunkBB);
/*     */ 
/*     */     
/* 138 */     placeBlock(level, northStairs, 4, 0, 0, chunkBB);
/* 139 */     placeBlock(level, northStairs, 5, 0, 0, chunkBB);
/* 140 */     placeBlock(level, northStairs, 6, 0, 0, chunkBB);
/* 141 */     placeBlock(level, northStairs, 7, 0, 0, chunkBB);
/*     */ 
/*     */     
/* 144 */     placeBlock(level, northStairs, 4, 1, 8, chunkBB);
/* 145 */     placeBlock(level, northStairs, 4, 2, 9, chunkBB);
/* 146 */     placeBlock(level, northStairs, 4, 3, 10, chunkBB);
/* 147 */     placeBlock(level, northStairs, 7, 1, 8, chunkBB);
/* 148 */     placeBlock(level, northStairs, 7, 2, 9, chunkBB);
/* 149 */     placeBlock(level, northStairs, 7, 3, 10, chunkBB);
/* 150 */     generateBox(level, chunkBB, 4, 1, 9, 4, 1, 9, false, random, STONE_SELECTOR);
/* 151 */     generateBox(level, chunkBB, 7, 1, 9, 7, 1, 9, false, random, STONE_SELECTOR);
/* 152 */     generateBox(level, chunkBB, 4, 1, 10, 7, 2, 10, false, random, STONE_SELECTOR);
/*     */ 
/*     */     
/* 155 */     generateBox(level, chunkBB, 5, 4, 5, 6, 4, 5, false, random, STONE_SELECTOR);
/* 156 */     placeBlock(level, eastStairs, 4, 4, 5, chunkBB);
/* 157 */     placeBlock(level, westStairs, 7, 4, 5, chunkBB);
/*     */ 
/*     */     
/* 160 */     for (int i = 0; i < 4; i++) {
/* 161 */       placeBlock(level, southStairs, 5, 0 - i, 6 + i, chunkBB);
/* 162 */       placeBlock(level, southStairs, 6, 0 - i, 6 + i, chunkBB);
/* 163 */       generateAirBox(level, chunkBB, 5, 0 - i, 7 + i, 6, 0 - i, 9 + i);
/*     */     } 
/*     */ 
/*     */     
/* 167 */     generateAirBox(level, chunkBB, 1, -3, 12, 10, -1, 13);
/* 168 */     generateAirBox(level, chunkBB, 1, -3, 1, 3, -1, 13);
/* 169 */     generateAirBox(level, chunkBB, 1, -3, 1, 9, -1, 5);
/* 170 */     for (int z = 1; z <= 13; z += 2) {
/* 171 */       generateBox(level, chunkBB, 1, -3, z, 1, -2, z, false, random, STONE_SELECTOR);
/*     */     }
/* 173 */     for (int z = 2; z <= 12; z += 2) {
/* 174 */       generateBox(level, chunkBB, 1, -1, z, 3, -1, z, false, random, STONE_SELECTOR);
/*     */     }
/* 176 */     generateBox(level, chunkBB, 2, -2, 1, 5, -2, 1, false, random, STONE_SELECTOR);
/* 177 */     generateBox(level, chunkBB, 7, -2, 1, 9, -2, 1, false, random, STONE_SELECTOR);
/* 178 */     generateBox(level, chunkBB, 6, -3, 1, 6, -3, 1, false, random, STONE_SELECTOR);
/* 179 */     generateBox(level, chunkBB, 6, -1, 1, 6, -1, 1, false, random, STONE_SELECTOR);
/*     */ 
/*     */     
/* 182 */     placeBlock(level, (BlockState)((BlockState)Blocks.TRIPWIRE_HOOK.defaultBlockState().setValue(TripWireHookBlock.FACING, Direction.EAST)).setValue(TripWireHookBlock.ATTACHED, Boolean.valueOf(true)), 1, -3, 8, chunkBB);
/* 183 */     placeBlock(level, (BlockState)((BlockState)Blocks.TRIPWIRE_HOOK.defaultBlockState().setValue(TripWireHookBlock.FACING, Direction.WEST)).setValue(TripWireHookBlock.ATTACHED, Boolean.valueOf(true)), 4, -3, 8, chunkBB);
/* 184 */     placeBlock(level, (BlockState)((BlockState)((BlockState)Blocks.TRIPWIRE.defaultBlockState().setValue(TripWireBlock.EAST, Boolean.valueOf(true))).setValue(TripWireBlock.WEST, Boolean.valueOf(true))).setValue(TripWireBlock.ATTACHED, Boolean.valueOf(true)), 2, -3, 8, chunkBB);
/* 185 */     placeBlock(level, (BlockState)((BlockState)((BlockState)Blocks.TRIPWIRE.defaultBlockState().setValue(TripWireBlock.EAST, Boolean.valueOf(true))).setValue(TripWireBlock.WEST, Boolean.valueOf(true))).setValue(TripWireBlock.ATTACHED, Boolean.valueOf(true)), 3, -3, 8, chunkBB);
/* 186 */     BlockState redstoneWireNS = (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue(RedStoneWireBlock.NORTH, RedstoneSide.SIDE)).setValue(RedStoneWireBlock.SOUTH, RedstoneSide.SIDE);
/* 187 */     placeBlock(level, redstoneWireNS, 5, -3, 7, chunkBB);
/* 188 */     placeBlock(level, redstoneWireNS, 5, -3, 6, chunkBB);
/* 189 */     placeBlock(level, redstoneWireNS, 5, -3, 5, chunkBB);
/* 190 */     placeBlock(level, redstoneWireNS, 5, -3, 4, chunkBB);
/* 191 */     placeBlock(level, redstoneWireNS, 5, -3, 3, chunkBB);
/* 192 */     placeBlock(level, redstoneWireNS, 5, -3, 2, chunkBB);
/* 193 */     placeBlock(level, (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue(RedStoneWireBlock.NORTH, RedstoneSide.SIDE)).setValue(RedStoneWireBlock.WEST, RedstoneSide.SIDE), 5, -3, 1, chunkBB);
/* 194 */     placeBlock(level, (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue(RedStoneWireBlock.EAST, RedstoneSide.SIDE)).setValue(RedStoneWireBlock.WEST, RedstoneSide.SIDE), 4, -3, 1, chunkBB);
/* 195 */     placeBlock(level, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 3, -3, 1, chunkBB);
/* 196 */     if (!this.placedTrap1) {
/* 197 */       this.placedTrap1 = createDispenser(level, chunkBB, random, 3, -2, 1, Direction.NORTH, BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER);
/*     */     }
/* 199 */     placeBlock(level, (BlockState)Blocks.VINE.defaultBlockState().setValue(VineBlock.SOUTH, Boolean.valueOf(true)), 3, -2, 2, chunkBB);
/*     */ 
/*     */     
/* 202 */     placeBlock(level, (BlockState)((BlockState)Blocks.TRIPWIRE_HOOK.defaultBlockState().setValue(TripWireHookBlock.FACING, Direction.NORTH)).setValue(TripWireHookBlock.ATTACHED, Boolean.valueOf(true)), 7, -3, 1, chunkBB);
/* 203 */     placeBlock(level, (BlockState)((BlockState)Blocks.TRIPWIRE_HOOK.defaultBlockState().setValue(TripWireHookBlock.FACING, Direction.SOUTH)).setValue(TripWireHookBlock.ATTACHED, Boolean.valueOf(true)), 7, -3, 5, chunkBB);
/* 204 */     placeBlock(level, (BlockState)((BlockState)((BlockState)Blocks.TRIPWIRE.defaultBlockState().setValue(TripWireBlock.NORTH, Boolean.valueOf(true))).setValue(TripWireBlock.SOUTH, Boolean.valueOf(true))).setValue(TripWireBlock.ATTACHED, Boolean.valueOf(true)), 7, -3, 2, chunkBB);
/* 205 */     placeBlock(level, (BlockState)((BlockState)((BlockState)Blocks.TRIPWIRE.defaultBlockState().setValue(TripWireBlock.NORTH, Boolean.valueOf(true))).setValue(TripWireBlock.SOUTH, Boolean.valueOf(true))).setValue(TripWireBlock.ATTACHED, Boolean.valueOf(true)), 7, -3, 3, chunkBB);
/* 206 */     placeBlock(level, (BlockState)((BlockState)((BlockState)Blocks.TRIPWIRE.defaultBlockState().setValue(TripWireBlock.NORTH, Boolean.valueOf(true))).setValue(TripWireBlock.SOUTH, Boolean.valueOf(true))).setValue(TripWireBlock.ATTACHED, Boolean.valueOf(true)), 7, -3, 4, chunkBB);
/* 207 */     placeBlock(level, (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue(RedStoneWireBlock.EAST, RedstoneSide.SIDE)).setValue(RedStoneWireBlock.WEST, RedstoneSide.SIDE), 8, -3, 6, chunkBB);
/* 208 */     placeBlock(level, (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue(RedStoneWireBlock.WEST, RedstoneSide.SIDE)).setValue(RedStoneWireBlock.SOUTH, RedstoneSide.SIDE), 9, -3, 6, chunkBB);
/* 209 */     placeBlock(level, (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue(RedStoneWireBlock.NORTH, RedstoneSide.SIDE)).setValue(RedStoneWireBlock.SOUTH, RedstoneSide.UP), 9, -3, 5, chunkBB);
/* 210 */     placeBlock(level, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 9, -3, 4, chunkBB);
/* 211 */     placeBlock(level, redstoneWireNS, 9, -2, 4, chunkBB);
/* 212 */     if (!this.placedTrap2) {
/* 213 */       this.placedTrap2 = createDispenser(level, chunkBB, random, 9, -2, 3, Direction.WEST, BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER);
/*     */     }
/* 215 */     placeBlock(level, (BlockState)Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, Boolean.valueOf(true)), 8, -1, 3, chunkBB);
/* 216 */     placeBlock(level, (BlockState)Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, Boolean.valueOf(true)), 8, -2, 3, chunkBB);
/* 217 */     if (!this.placedMainChest) {
/* 218 */       this.placedMainChest = createChest(level, chunkBB, random, 8, -3, 3, BuiltInLootTables.JUNGLE_TEMPLE);
/*     */     }
/* 220 */     placeBlock(level, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 9, -3, 2, chunkBB);
/* 221 */     placeBlock(level, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 8, -3, 1, chunkBB);
/* 222 */     placeBlock(level, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 4, -3, 5, chunkBB);
/* 223 */     placeBlock(level, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 5, -2, 5, chunkBB);
/* 224 */     placeBlock(level, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 5, -1, 5, chunkBB);
/* 225 */     placeBlock(level, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 6, -3, 5, chunkBB);
/* 226 */     placeBlock(level, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 7, -2, 5, chunkBB);
/* 227 */     placeBlock(level, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 7, -1, 5, chunkBB);
/* 228 */     placeBlock(level, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 8, -3, 5, chunkBB);
/* 229 */     generateBox(level, chunkBB, 9, -1, 1, 9, -1, 5, false, random, STONE_SELECTOR);
/*     */ 
/*     */     
/* 232 */     generateAirBox(level, chunkBB, 8, -3, 8, 10, -1, 10);
/* 233 */     placeBlock(level, Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 8, -2, 11, chunkBB);
/* 234 */     placeBlock(level, Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 9, -2, 11, chunkBB);
/* 235 */     placeBlock(level, Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 10, -2, 11, chunkBB);
/* 236 */     BlockState lever = (BlockState)((BlockState)Blocks.LEVER.defaultBlockState().setValue(LeverBlock.FACING, Direction.NORTH)).setValue(LeverBlock.FACE, AttachFace.WALL);
/* 237 */     placeBlock(level, lever, 8, -2, 12, chunkBB);
/* 238 */     placeBlock(level, lever, 9, -2, 12, chunkBB);
/* 239 */     placeBlock(level, lever, 10, -2, 12, chunkBB);
/* 240 */     generateBox(level, chunkBB, 8, -3, 8, 8, -3, 10, false, random, STONE_SELECTOR);
/* 241 */     generateBox(level, chunkBB, 10, -3, 8, 10, -3, 10, false, random, STONE_SELECTOR);
/* 242 */     placeBlock(level, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 10, -2, 9, chunkBB);
/* 243 */     placeBlock(level, redstoneWireNS, 8, -2, 9, chunkBB);
/* 244 */     placeBlock(level, redstoneWireNS, 8, -2, 10, chunkBB);
/* 245 */     placeBlock(level, (BlockState)((BlockState)((BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue(RedStoneWireBlock.NORTH, RedstoneSide.SIDE)).setValue(RedStoneWireBlock.SOUTH, RedstoneSide.SIDE)).setValue(RedStoneWireBlock.EAST, RedstoneSide.SIDE)).setValue(RedStoneWireBlock.WEST, RedstoneSide.SIDE), 10, -1, 9, chunkBB);
/* 246 */     placeBlock(level, (BlockState)Blocks.STICKY_PISTON.defaultBlockState().setValue(PistonBaseBlock.FACING, Direction.UP), 9, -2, 8, chunkBB);
/* 247 */     placeBlock(level, (BlockState)Blocks.STICKY_PISTON.defaultBlockState().setValue(PistonBaseBlock.FACING, Direction.WEST), 10, -2, 8, chunkBB);
/* 248 */     placeBlock(level, (BlockState)Blocks.STICKY_PISTON.defaultBlockState().setValue(PistonBaseBlock.FACING, Direction.WEST), 10, -1, 8, chunkBB);
/* 249 */     placeBlock(level, (BlockState)Blocks.REPEATER.defaultBlockState().setValue(RepeaterBlock.FACING, Direction.NORTH), 10, -2, 10, chunkBB);
/* 250 */     if (!this.placedHiddenChest)
/* 251 */       this.placedHiddenChest = createChest(level, chunkBB, random, 9, -3, 10, BuiltInLootTables.JUNGLE_TEMPLE); 
/*     */   }
/*     */   
/*     */   private static class MossStoneSelector
/*     */     extends StructurePiece.BlockSelector
/*     */   {
/*     */     public void next(RandomSource random, int worldX, int worldY, int worldZ, boolean isEdge) {
/* 258 */       if (random.nextFloat() < 0.4F) {
/* 259 */         this.next = Blocks.COBBLESTONE.defaultBlockState();
/*     */       } else {
/* 261 */         this.next = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/* 266 */   private static final MossStoneSelector STONE_SELECTOR = new MossStoneSelector();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\JungleTemplePiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */