/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.monster.ElderGuardian;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
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
/*     */ public abstract class OceanMonumentPiece
/*     */   extends StructurePiece
/*     */ {
/*  34 */   protected static final BlockState BASE_GRAY = Blocks.PRISMARINE.defaultBlockState();
/*  35 */   protected static final BlockState BASE_LIGHT = Blocks.PRISMARINE_BRICKS.defaultBlockState();
/*  36 */   protected static final BlockState BASE_BLACK = Blocks.DARK_PRISMARINE.defaultBlockState();
/*     */   
/*  38 */   protected static final BlockState DOT_DECO_DATA = BASE_LIGHT;
/*     */   
/*  40 */   protected static final BlockState LAMP_BLOCK = Blocks.SEA_LANTERN.defaultBlockState();
/*     */   
/*     */   protected static final boolean DO_FILL = true;
/*  43 */   protected static final BlockState FILL_BLOCK = Blocks.WATER.defaultBlockState();
/*  44 */   protected static final Set<Block> FILL_KEEP = ImmutableSet.builder()
/*  45 */     .add(Blocks.ICE)
/*  46 */     .add(Blocks.PACKED_ICE)
/*  47 */     .add(Blocks.BLUE_ICE)
/*  48 */     .add(FILL_BLOCK.getBlock())
/*  49 */     .build();
/*     */   
/*     */   protected static final int GRIDROOM_WIDTH = 8;
/*     */   
/*     */   protected static final int GRIDROOM_DEPTH = 8;
/*     */   protected static final int GRIDROOM_HEIGHT = 4;
/*     */   protected static final int GRID_WIDTH = 5;
/*     */   protected static final int GRID_DEPTH = 5;
/*     */   protected static final int GRID_HEIGHT = 3;
/*     */   protected static final int GRID_FLOOR_COUNT = 25;
/*     */   protected static final int GRID_SIZE = 75;
/*  60 */   protected static final int GRIDROOM_SOURCE_INDEX = getRoomIndex(2, 0, 0);
/*  61 */   protected static final int GRIDROOM_TOP_CONNECT_INDEX = getRoomIndex(2, 2, 0);
/*  62 */   protected static final int GRIDROOM_LEFTWING_CONNECT_INDEX = getRoomIndex(0, 1, 0);
/*  63 */   protected static final int GRIDROOM_RIGHTWING_CONNECT_INDEX = getRoomIndex(4, 1, 0);
/*     */   
/*     */   protected static final int LEFTWING_INDEX = 1001;
/*     */   
/*     */   protected static final int RIGHTWING_INDEX = 1002;
/*     */   
/*     */   protected static final int PENTHOUSE_INDEX = 1003;
/*     */   protected OceanMonumentPieces.RoomDefinition roomDefinition;
/*     */   
/*  72 */   protected static int getRoomIndex(int roomX, int roomY, int roomZ) { return roomY * 25 + roomZ * 5 + roomX; }
/*     */ 
/*     */   
/*     */   public OceanMonumentPiece(StructurePieceType type, Direction orientation, int genDepth, BoundingBox boundingBox) {
/*  76 */     super(type, genDepth, boundingBox);
/*  77 */     setOrientation(orientation);
/*     */   }
/*     */   
/*     */   protected OceanMonumentPiece(StructurePieceType type, int genDepth, Direction orientation, OceanMonumentPieces.RoomDefinition roomDefinition, int roomWidth, int roomHeight, int roomDepth) {
/*  81 */     super(type, genDepth, makeBoundingBox(orientation, roomDefinition, roomWidth, roomHeight, roomDepth));
/*     */     
/*  83 */     setOrientation(orientation);
/*  84 */     this.roomDefinition = roomDefinition;
/*     */   }
/*     */   
/*     */   private static BoundingBox makeBoundingBox(Direction orientation, OceanMonumentPieces.RoomDefinition roomDefinition, int roomWidth, int roomHeight, int roomDepth) {
/*  88 */     int roomIndex = roomDefinition.index;
/*  89 */     int roomX = roomIndex % 5;
/*  90 */     int roomZ = roomIndex / 5 % 5;
/*  91 */     int roomY = roomIndex / 25;
/*     */ 
/*     */ 
/*     */     
/*  95 */     BoundingBox boundingBox = makeBoundingBox(0, 0, 0, orientation, roomWidth * 8, roomHeight * 4, roomDepth * 8);
/*     */     
/*  97 */     switch (OceanMonumentPieces.null.$SwitchMap$net$minecraft$core$Direction[orientation.ordinal()])
/*     */     { case 1:
/*  99 */         boundingBox.move(roomX * 8, roomY * 4, -(roomZ + roomDepth) * 8 + 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 114 */         return boundingBox;case 2: boundingBox.move(roomX * 8, roomY * 4, roomZ * 8); return boundingBox;case 3: boundingBox.move(-(roomZ + roomDepth) * 8 + 1, roomY * 4, roomX * 8); return boundingBox; }  boundingBox.move(roomZ * 8, roomY * 4, roomX * 8); return boundingBox;
/*     */   }
/*     */ 
/*     */   
/* 118 */   public OceanMonumentPiece(StructurePieceType type, CompoundTag tag) { super(type, tag); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {}
/*     */ 
/*     */   
/*     */   protected void generateWaterBox(WorldGenLevel level, BoundingBox chunkBB, int x0, int y0, int z0, int x1, int y1, int z1) {
/* 126 */     for (int y = y0; y <= y1; y++) {
/* 127 */       for (int x = x0; x <= x1; x++) {
/* 128 */         for (int z = z0; z <= z1; z++) {
/* 129 */           BlockState block = getBlock(level, x, y, z, chunkBB);
/* 130 */           if (!FILL_KEEP.contains(block.getBlock())) {
/* 131 */             if (getWorldY(y) >= level.getSeaLevel() && block != FILL_BLOCK) {
/* 132 */               placeBlock(level, Blocks.AIR.defaultBlockState(), x, y, z, chunkBB);
/*     */             } else {
/* 134 */               placeBlock(level, FILL_BLOCK, x, y, z, chunkBB);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void generateDefaultFloor(WorldGenLevel level, BoundingBox chunkBB, int xOff, int zOff, boolean downOpening) {
/* 143 */     if (downOpening) {
/* 144 */       generateBox(level, chunkBB, xOff + 0, 0, zOff + 0, xOff + 2, 0, zOff + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/* 145 */       generateBox(level, chunkBB, xOff + 5, 0, zOff + 0, xOff + 8 - 1, 0, zOff + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/* 146 */       generateBox(level, chunkBB, xOff + 3, 0, zOff + 0, xOff + 4, 0, zOff + 2, BASE_GRAY, BASE_GRAY, false);
/* 147 */       generateBox(level, chunkBB, xOff + 3, 0, zOff + 5, xOff + 4, 0, zOff + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 149 */       generateBox(level, chunkBB, xOff + 3, 0, zOff + 2, xOff + 4, 0, zOff + 2, BASE_LIGHT, BASE_LIGHT, false);
/* 150 */       generateBox(level, chunkBB, xOff + 3, 0, zOff + 5, xOff + 4, 0, zOff + 5, BASE_LIGHT, BASE_LIGHT, false);
/* 151 */       generateBox(level, chunkBB, xOff + 2, 0, zOff + 3, xOff + 2, 0, zOff + 4, BASE_LIGHT, BASE_LIGHT, false);
/* 152 */       generateBox(level, chunkBB, xOff + 5, 0, zOff + 3, xOff + 5, 0, zOff + 4, BASE_LIGHT, BASE_LIGHT, false);
/*     */     } else {
/* 154 */       generateBox(level, chunkBB, xOff + 0, 0, zOff + 0, xOff + 8 - 1, 0, zOff + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void generateBoxOnFillOnly(WorldGenLevel level, BoundingBox chunkBB, int x0, int y0, int z0, int x1, int y1, int z1, BlockState targetBlock) {
/* 159 */     for (int y = y0; y <= y1; y++) {
/* 160 */       for (int x = x0; x <= x1; x++) {
/* 161 */         for (int z = z0; z <= z1; z++) {
/* 162 */           if (getBlock(level, x, y, z, chunkBB) == FILL_BLOCK)
/*     */           {
/*     */             
/* 165 */             placeBlock(level, targetBlock, x, y, z, chunkBB); } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean chunkIntersects(BoundingBox chunkBB, int x0, int z0, int x1, int z1) {
/* 172 */     int wx0 = getWorldX(x0, z0);
/* 173 */     int wz0 = getWorldZ(x0, z0);
/* 174 */     int wx1 = getWorldX(x1, z1);
/* 175 */     int wz1 = getWorldZ(x1, z1);
/* 176 */     return chunkBB.intersects(Math.min(wx0, wx1), Math.min(wz0, wz1), Math.max(wx0, wx1), Math.max(wz0, wz1));
/*     */   }
/*     */   
/*     */   protected void spawnElder(WorldGenLevel level, BoundingBox chunkBB, int x, int y, int z) {
/* 180 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(x, y, z);
/* 181 */     if (chunkBB.isInside(mutableBlockPos)) {
/* 182 */       ElderGuardian elder = (ElderGuardian)EntityType.ELDER_GUARDIAN.create(level.getLevel(), EntitySpawnReason.STRUCTURE);
/* 183 */       if (elder != null) {
/* 184 */         elder.heal(elder.getMaxHealth());
/* 185 */         elder.snapTo(mutableBlockPos.getX() + 0.5D, mutableBlockPos.getY(), mutableBlockPos.getZ() + 0.5D, 0.0F, 0.0F);
/* 186 */         elder.finalizeSpawn(level, level.getCurrentDifficultyAt(elder.blockPosition()), EntitySpawnReason.STRUCTURE, null);
/* 187 */         level.addFreshEntityWithPassengers(elder);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentPieces$OceanMonumentPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */