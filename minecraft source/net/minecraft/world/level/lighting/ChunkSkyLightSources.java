/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.BitStorage;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.SimpleBitStorage;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ public class ChunkSkyLightSources
/*     */ {
/*     */   private static final int SIZE = 16;
/*     */   public static final int NEGATIVE_INFINITY = -2147483648;
/*     */   private final int minY;
/*     */   
/*     */   public ChunkSkyLightSources(LevelHeightAccessor level) {
/*  26 */     this.mutablePos1 = new BlockPos.MutableBlockPos();
/*  27 */     this.mutablePos2 = new BlockPos.MutableBlockPos();
/*     */ 
/*     */ 
/*     */     
/*  31 */     this.minY = level.getMinY() - 1;
/*  32 */     int maxY = level.getMaxY() + 1;
/*  33 */     int bits = Mth.ceillog2(maxY - this.minY + 1);
/*  34 */     this.heightmap = new SimpleBitStorage(bits, 256);
/*     */   }
/*     */   private final BitStorage heightmap; private final BlockPos.MutableBlockPos mutablePos1; private final BlockPos.MutableBlockPos mutablePos2;
/*     */   public void fillFrom(ChunkAccess chunk) {
/*  38 */     int maxSectionIndex = chunk.getHighestFilledSectionIndex();
/*  39 */     if (maxSectionIndex == -1) {
/*  40 */       fill(this.minY);
/*     */       
/*     */       return;
/*     */     } 
/*  44 */     for (int z = 0; z < 16; z++) {
/*  45 */       for (int x = 0; x < 16; x++) {
/*  46 */         int initialEdgeY = Math.max(findLowestSourceY(chunk, maxSectionIndex, x, z), this.minY);
/*  47 */         set(index(x, z), initialEdgeY);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int findLowestSourceY(ChunkAccess chunk, int topSectionIndex, int x, int z) {
/*  53 */     int topY = SectionPos.sectionToBlockCoord(chunk.getSectionYFromSectionIndex(topSectionIndex) + 1);
/*     */     
/*  55 */     BlockPos.MutableBlockPos topPos = this.mutablePos1.set(x, topY, z);
/*  56 */     BlockPos.MutableBlockPos bottomPos = this.mutablePos2.setWithOffset(topPos, Direction.DOWN);
/*     */     
/*  58 */     BlockState topState = Blocks.AIR.defaultBlockState();
/*     */     
/*  60 */     for (int sectionIndex = topSectionIndex; sectionIndex >= 0; sectionIndex--) {
/*  61 */       LevelChunkSection section = chunk.getSection(sectionIndex);
/*  62 */       if (section.hasOnlyAir()) {
/*     */         
/*  64 */         topState = Blocks.AIR.defaultBlockState();
/*  65 */         int sectionY = chunk.getSectionYFromSectionIndex(sectionIndex);
/*  66 */         topPos.setY(SectionPos.sectionToBlockCoord(sectionY));
/*  67 */         bottomPos.setY(topPos.getY() - 1);
/*     */       } else {
/*     */         
/*  70 */         for (int y = 15; y >= 0; y--) {
/*  71 */           BlockState bottomState = section.getBlockState(x, y, z);
/*  72 */           if (isEdgeOccluded(topState, bottomState)) {
/*  73 */             return topPos.getY();
/*     */           }
/*  75 */           topState = bottomState;
/*  76 */           topPos.set(bottomPos);
/*  77 */           bottomPos.move(Direction.DOWN);
/*     */         } 
/*     */       } 
/*     */     } 
/*  81 */     return this.minY;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean update(BlockGetter level, int x, int y, int z) {
/*  86 */     int upperEdgeY = y + 1;
/*     */     
/*  88 */     int index = index(x, z);
/*  89 */     int currentLowestSourceY = get(index);
/*  90 */     if (upperEdgeY < currentLowestSourceY) {
/*  91 */       return false;
/*     */     }
/*     */     
/*  94 */     BlockPos.MutableBlockPos mutableBlockPos1 = this.mutablePos1.set(x, y + 1, z);
/*  95 */     BlockState topState = level.getBlockState(mutableBlockPos1);
/*  96 */     BlockPos.MutableBlockPos mutableBlockPos2 = this.mutablePos2.set(x, y, z);
/*  97 */     BlockState middleState = level.getBlockState(mutableBlockPos2);
/*  98 */     if (updateEdge(level, index, currentLowestSourceY, mutableBlockPos1, topState, mutableBlockPos2, middleState)) {
/*  99 */       return true;
/*     */     }
/*     */     
/* 102 */     BlockPos.MutableBlockPos mutableBlockPos3 = this.mutablePos1.set(x, y - 1, z);
/* 103 */     BlockState bottomState = level.getBlockState(mutableBlockPos3);
/* 104 */     return updateEdge(level, index, currentLowestSourceY, mutableBlockPos2, middleState, mutableBlockPos3, bottomState);
/*     */   }
/*     */   
/*     */   private boolean updateEdge(BlockGetter level, int index, int oldTopEdgeY, BlockPos topPos, BlockState topState, BlockPos bottomPos, BlockState bottomState) {
/* 108 */     int checkedEdgeY = topPos.getY();
/* 109 */     if (isEdgeOccluded(topState, bottomState)) {
/* 110 */       if (checkedEdgeY > oldTopEdgeY) {
/* 111 */         set(index, checkedEdgeY);
/* 112 */         return true;
/*     */       }
/*     */     
/* 115 */     } else if (checkedEdgeY == oldTopEdgeY) {
/* 116 */       set(index, findLowestSourceBelow(level, bottomPos, bottomState));
/* 117 */       return true;
/*     */     } 
/*     */     
/* 120 */     return false;
/*     */   }
/*     */   
/*     */   private int findLowestSourceBelow(BlockGetter level, BlockPos startPos, BlockState startState) {
/* 124 */     BlockPos.MutableBlockPos topPos = this.mutablePos1.set(startPos);
/* 125 */     BlockPos.MutableBlockPos bottomPos = this.mutablePos2.setWithOffset(startPos, Direction.DOWN);
/* 126 */     BlockState topState = startState;
/* 127 */     while (bottomPos.getY() >= this.minY) {
/* 128 */       BlockState bottomState = level.getBlockState(bottomPos);
/* 129 */       if (isEdgeOccluded(topState, bottomState)) {
/* 130 */         return topPos.getY();
/*     */       }
/* 132 */       topState = bottomState;
/* 133 */       topPos.set(bottomPos);
/* 134 */       bottomPos.move(Direction.DOWN);
/*     */     } 
/* 136 */     return this.minY;
/*     */   }
/*     */   
/*     */   private static boolean isEdgeOccluded(BlockState topState, BlockState bottomState) {
/* 140 */     if (bottomState.getLightBlock() != 0) {
/* 141 */       return true;
/*     */     }
/* 143 */     VoxelShape topShape = LightEngine.getOcclusionShape(topState, Direction.DOWN);
/* 144 */     VoxelShape bottomShape = LightEngine.getOcclusionShape(bottomState, Direction.UP);
/* 145 */     return Shapes.faceShapeOccludes(topShape, bottomShape);
/*     */   }
/*     */   
/*     */   public int getLowestSourceY(int x, int z) {
/* 149 */     int value = get(index(x, z));
/* 150 */     return extendSourcesBelowWorld(value);
/*     */   }
/*     */   
/*     */   public int getHighestLowestSourceY() {
/* 154 */     int maxValue = Integer.MIN_VALUE;
/* 155 */     for (int i = 0; i < this.heightmap.getSize(); i++) {
/* 156 */       int value = this.heightmap.get(i);
/* 157 */       if (value > maxValue) {
/* 158 */         maxValue = value;
/*     */       }
/*     */     } 
/* 161 */     return extendSourcesBelowWorld(maxValue + this.minY);
/*     */   }
/*     */   
/*     */   private void fill(int lowestSourceY) {
/* 165 */     int value = lowestSourceY - this.minY;
/* 166 */     for (int i = 0; i < this.heightmap.getSize(); i++) {
/* 167 */       this.heightmap.set(i, value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 172 */   private void set(int index, int value) { this.heightmap.set(index, value - this.minY); }
/*     */ 
/*     */ 
/*     */   
/* 176 */   private int get(int index) { return this.heightmap.get(index) + this.minY; }
/*     */ 
/*     */   
/*     */   private int extendSourcesBelowWorld(int value) {
/* 180 */     if (value == this.minY) {
/* 181 */       return Integer.MIN_VALUE;
/*     */     }
/* 183 */     return value;
/*     */   }
/*     */ 
/*     */   
/* 187 */   private static int index(int x, int z) { return x + z * 16; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\lighting\ChunkSkyLightSources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */