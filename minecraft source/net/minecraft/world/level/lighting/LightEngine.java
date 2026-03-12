/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
/*     */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LightChunk;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LightEngine<M extends DataLayerStorageMap<M>, S extends LayerLightSectionStorage<M>>
/*     */   extends Object
/*     */   implements LayerLightEventListener
/*     */ {
/*     */   public static final int MAX_LEVEL = 15;
/*     */   protected static final int MIN_OPACITY = 1;
/*  29 */   protected static final long PULL_LIGHT_IN_ENTRY = QueueEntry.decreaseAllDirections(1);
/*     */   
/*     */   private static final int MIN_QUEUE_SIZE = 512;
/*     */   
/*  33 */   protected static final Direction[] PROPAGATION_DIRECTIONS = Direction.values(); protected final LightChunkGetter chunkSource; protected final S storage;
/*     */   private final LongOpenHashSet blockNodesToCheck;
/*     */   private final LongArrayFIFOQueue decreaseQueue;
/*     */   
/*     */   protected LightEngine(LightChunkGetter chunkSource, S storage) {
/*  38 */     this.blockNodesToCheck = new LongOpenHashSet(512, 0.5F);
/*  39 */     this.decreaseQueue = new LongArrayFIFOQueue();
/*  40 */     this.increaseQueue = new LongArrayFIFOQueue();
/*     */ 
/*     */     
/*  43 */     this.lastChunkPos = new long[2];
/*  44 */     this.lastChunk = new LightChunk[2];
/*     */ 
/*     */     
/*  47 */     this.chunkSource = chunkSource;
/*  48 */     this.storage = storage;
/*  49 */     clearChunkCache();
/*     */   }
/*     */   private final LongArrayFIFOQueue increaseQueue; private static final int CACHE_SIZE = 2; private final long[] lastChunkPos; private final LightChunk[] lastChunk;
/*     */   public static boolean hasDifferentLightProperties(BlockState oldState, BlockState newState) {
/*  53 */     if (newState == oldState) {
/*  54 */       return false;
/*     */     }
/*  56 */     return (newState.getLightBlock() != oldState.getLightBlock() || newState
/*  57 */       .getLightEmission() != oldState.getLightEmission() || newState
/*  58 */       .useShapeForLightOcclusion() || oldState
/*  59 */       .useShapeForLightOcclusion());
/*     */   }
/*     */   
/*     */   public static int getLightBlockInto(BlockState fromState, BlockState toState, Direction direction, int simpleOpacity) {
/*  63 */     boolean fromEmpty = isEmptyShape(fromState);
/*  64 */     boolean toEmpty = isEmptyShape(toState);
/*     */     
/*  66 */     if (fromEmpty && toEmpty) {
/*  67 */       return simpleOpacity;
/*     */     }
/*     */     
/*  70 */     VoxelShape fromShape = fromEmpty ? Shapes.empty() : fromState.getOcclusionShape();
/*  71 */     VoxelShape toShape = toEmpty ? Shapes.empty() : toState.getOcclusionShape();
/*     */     
/*  73 */     if (Shapes.mergedFaceOccludes(fromShape, toShape, direction)) {
/*  74 */       return 16;
/*     */     }
/*     */     
/*  77 */     return simpleOpacity;
/*     */   }
/*     */ 
/*     */   
/*  81 */   public static VoxelShape getOcclusionShape(BlockState state, Direction direction) { return isEmptyShape(state) ? Shapes.empty() : state.getFaceOcclusionShape(direction); }
/*     */ 
/*     */ 
/*     */   
/*  85 */   protected static boolean isEmptyShape(BlockState state) { return (!state.canOcclude() || !state.useShapeForLightOcclusion()); }
/*     */ 
/*     */   
/*     */   protected BlockState getState(BlockPos pos) {
/*  89 */     int chunkX = SectionPos.blockToSectionCoord(pos.getX());
/*  90 */     int chunkZ = SectionPos.blockToSectionCoord(pos.getZ());
/*  91 */     LightChunk chunk = getChunk(chunkX, chunkZ);
/*  92 */     if (chunk == null)
/*     */     {
/*     */ 
/*     */       
/*  96 */       return Blocks.BEDROCK.defaultBlockState();
/*     */     }
/*  98 */     return chunk.getBlockState(pos);
/*     */   }
/*     */ 
/*     */   
/* 102 */   protected int getOpacity(BlockState state) { return Math.max(1, state.getLightBlock()); }
/*     */ 
/*     */   
/*     */   protected boolean shapeOccludes(BlockState fromState, BlockState toState, Direction direction) {
/* 106 */     VoxelShape fromShape = getOcclusionShape(fromState, direction);
/* 107 */     VoxelShape toShape = getOcclusionShape(toState, direction.getOpposite());
/* 108 */     return Shapes.faceShapeOccludes(fromShape, toShape);
/*     */   }
/*     */   
/*     */   protected LightChunk getChunk(int chunkX, int chunkZ) {
/* 112 */     long pos = ChunkPos.asLong(chunkX, chunkZ);
/* 113 */     for (int i = 0; i < 2; i++) {
/* 114 */       if (pos == this.lastChunkPos[i]) {
/* 115 */         return this.lastChunk[i];
/*     */       }
/*     */     } 
/* 118 */     LightChunk chunk = this.chunkSource.getChunkForLighting(chunkX, chunkZ);
/* 119 */     for (int i = 1; i > 0; i--) {
/* 120 */       this.lastChunkPos[i] = this.lastChunkPos[i - 1];
/* 121 */       this.lastChunk[i] = this.lastChunk[i - 1];
/*     */     } 
/* 123 */     this.lastChunkPos[0] = pos;
/* 124 */     this.lastChunk[0] = chunk;
/* 125 */     return chunk;
/*     */   }
/*     */   
/*     */   private void clearChunkCache() {
/* 129 */     Arrays.fill(this.lastChunkPos, ChunkPos.INVALID_CHUNK_POS);
/* 130 */     Arrays.fill(this.lastChunk, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public void checkBlock(BlockPos pos) { this.blockNodesToCheck.add(pos.asLong()); }
/*     */ 
/*     */ 
/*     */   
/* 139 */   public void queueSectionData(long pos, DataLayer data) { this.storage.queueSectionData(pos, data); }
/*     */ 
/*     */ 
/*     */   
/* 143 */   public void retainData(ChunkPos pos, boolean retain) { this.storage.retainData(SectionPos.getZeroNode(pos.x, pos.z), retain); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 148 */   public void updateSectionStatus(SectionPos pos, boolean sectionEmpty) { this.storage.updateSectionStatus(pos.asLong(), sectionEmpty); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 153 */   public void setLightEnabled(ChunkPos pos, boolean enable) { this.storage.setLightEnabled(SectionPos.getZeroNode(pos.x, pos.z), enable); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int runLightUpdates() {
/* 158 */     LongIterator iterator = this.blockNodesToCheck.iterator();
/* 159 */     while (iterator.hasNext()) {
/* 160 */       checkNode(iterator.nextLong());
/*     */     }
/* 162 */     this.blockNodesToCheck.clear();
/* 163 */     this.blockNodesToCheck.trim(512);
/*     */     
/* 165 */     int count = 0;
/* 166 */     count += propagateDecreases();
/* 167 */     count += propagateIncreases();
/*     */     
/* 169 */     clearChunkCache();
/*     */     
/* 171 */     this.storage.markNewInconsistencies(this);
/* 172 */     this.storage.swapSectionMap();
/*     */     
/* 174 */     return count;
/*     */   }
/*     */   
/*     */   private int propagateIncreases() {
/* 178 */     int count = 0;
/* 179 */     while (!this.increaseQueue.isEmpty()) {
/* 180 */       long fromNode = this.increaseQueue.dequeueLong();
/* 181 */       long increaseData = this.increaseQueue.dequeueLong();
/*     */       
/* 183 */       int fromLevel = this.storage.getStoredLevel(fromNode);
/*     */       
/* 185 */       int fromTargetLevel = QueueEntry.getFromLevel(increaseData);
/* 186 */       if (QueueEntry.isIncreaseFromEmission(increaseData) && fromLevel < fromTargetLevel) {
/* 187 */         this.storage.setStoredLevel(fromNode, fromTargetLevel);
/* 188 */         fromLevel = fromTargetLevel;
/*     */       } 
/* 190 */       if (fromLevel == fromTargetLevel) {
/* 191 */         propagateIncrease(fromNode, increaseData, fromLevel);
/*     */       }
/*     */       
/* 194 */       count++;
/*     */     } 
/* 196 */     return count;
/*     */   }
/*     */   
/*     */   private int propagateDecreases() {
/* 200 */     int count = 0;
/* 201 */     while (!this.decreaseQueue.isEmpty()) {
/* 202 */       long fromNode = this.decreaseQueue.dequeueLong();
/* 203 */       long decreaseData = this.decreaseQueue.dequeueLong();
/* 204 */       propagateDecrease(fromNode, decreaseData);
/* 205 */       count++;
/*     */     } 
/* 207 */     return count;
/*     */   }
/*     */   
/*     */   protected void enqueueDecrease(long fromNode, long decreaseData) {
/* 211 */     this.decreaseQueue.enqueue(fromNode);
/* 212 */     this.decreaseQueue.enqueue(decreaseData);
/*     */   }
/*     */   
/*     */   protected void enqueueIncrease(long fromNode, long increaseData) {
/* 216 */     this.increaseQueue.enqueue(fromNode);
/* 217 */     this.increaseQueue.enqueue(increaseData);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 222 */   public boolean hasLightWork() { return (this.storage.hasInconsistencies() || !this.blockNodesToCheck.isEmpty() || !this.decreaseQueue.isEmpty() || !this.increaseQueue.isEmpty()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 227 */   public DataLayer getDataLayerData(SectionPos pos) { return this.storage.getDataLayerData(pos.asLong()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 232 */   public int getLightValue(BlockPos pos) { return this.storage.getLightValue(pos.asLong()); }
/*     */ 
/*     */ 
/*     */   
/* 236 */   public String getDebugData(long sectionNode) { return getDebugSectionType(sectionNode).display(); }
/*     */ 
/*     */ 
/*     */   
/* 240 */   public LayerLightSectionStorage.SectionType getDebugSectionType(long sectionNode) { return this.storage.getDebugSectionType(sectionNode); }
/*     */   
/*     */   protected abstract void checkNode(long paramLong);
/*     */   
/*     */   protected abstract void propagateIncrease(long paramLong1, long paramLong2, int paramInt);
/*     */   
/*     */   protected abstract void propagateDecrease(long paramLong1, long paramLong2);
/*     */   
/*     */   public static class QueueEntry
/*     */   {
/*     */     private static final int FROM_LEVEL_BITS = 4;
/*     */     private static final int DIRECTION_BITS = 6;
/*     */     private static final long LEVEL_MASK = 15L;
/*     */     private static final long DIRECTIONS_MASK = 1008L;
/*     */     private static final long FLAG_FROM_EMPTY_SHAPE = 1024L;
/*     */     private static final long FLAG_INCREASE_FROM_EMISSION = 2048L;
/*     */     
/*     */     public static long decreaseSkipOneDirection(int oldFromLevel, Direction skipDirection) {
/* 258 */       long decreaseData = withoutDirection(1008L, skipDirection);
/* 259 */       return withLevel(decreaseData, oldFromLevel);
/*     */     }
/*     */ 
/*     */     
/* 263 */     public static long decreaseAllDirections(int oldFromLevel) { return withLevel(1008L, oldFromLevel); }
/*     */ 
/*     */     
/*     */     public static long increaseLightFromEmission(int newFromLevel, boolean fromEmptyShape) {
/* 267 */       long increaseData = 1008L;
/* 268 */       increaseData |= 0x800L;
/* 269 */       if (fromEmptyShape) {
/* 270 */         increaseData |= 0x400L;
/*     */       }
/* 272 */       return withLevel(increaseData, newFromLevel);
/*     */     }
/*     */     
/*     */     public static long increaseSkipOneDirection(int newFromLevel, boolean fromEmptyShape, Direction skipDirection) {
/* 276 */       long increaseData = withoutDirection(1008L, skipDirection);
/* 277 */       if (fromEmptyShape) {
/* 278 */         increaseData |= 0x400L;
/*     */       }
/* 280 */       return withLevel(increaseData, newFromLevel);
/*     */     }
/*     */     
/*     */     public static long increaseOnlyOneDirection(int newFromLevel, boolean fromEmptyShape, Direction direction) {
/* 284 */       long increaseData = 0L;
/* 285 */       if (fromEmptyShape) {
/* 286 */         increaseData |= 0x400L;
/*     */       }
/* 288 */       increaseData = withDirection(increaseData, direction);
/* 289 */       return withLevel(increaseData, newFromLevel);
/*     */     }
/*     */     
/*     */     public static long increaseSkySourceInDirections(boolean down, boolean north, boolean south, boolean west, boolean east) {
/* 293 */       long increaseData = withLevel(0L, 15);
/* 294 */       if (down) {
/* 295 */         increaseData = withDirection(increaseData, Direction.DOWN);
/*     */       }
/* 297 */       if (north) {
/* 298 */         increaseData = withDirection(increaseData, Direction.NORTH);
/*     */       }
/* 300 */       if (south) {
/* 301 */         increaseData = withDirection(increaseData, Direction.SOUTH);
/*     */       }
/* 303 */       if (west) {
/* 304 */         increaseData = withDirection(increaseData, Direction.WEST);
/*     */       }
/* 306 */       if (east) {
/* 307 */         increaseData = withDirection(increaseData, Direction.EAST);
/*     */       }
/* 309 */       return increaseData;
/*     */     }
/*     */ 
/*     */     
/* 313 */     public static int getFromLevel(long entry) { return (int)(entry & 0xFL); }
/*     */ 
/*     */ 
/*     */     
/* 317 */     public static boolean isFromEmptyShape(long entry) { return ((entry & 0x400L) != 0L); }
/*     */ 
/*     */ 
/*     */     
/* 321 */     public static boolean isIncreaseFromEmission(long entry) { return ((entry & 0x800L) != 0L); }
/*     */ 
/*     */ 
/*     */     
/* 325 */     public static boolean shouldPropagateInDirection(long entry, Direction direction) { return ((entry & 1L << direction.ordinal() + 4) != 0L); }
/*     */ 
/*     */ 
/*     */     
/* 329 */     private static long withLevel(long entry, int level) { return entry & 0xFFFFFFFFFFFFFFF0L | level & 0xFL; }
/*     */ 
/*     */ 
/*     */     
/* 333 */     private static long withDirection(long entry, Direction direction) { return entry | 1L << direction.ordinal() + 4; }
/*     */ 
/*     */ 
/*     */     
/* 337 */     private static long withoutDirection(long entry, Direction direction) { return entry & (1L << direction.ordinal() + 4 ^ 0xFFFFFFFFFFFFFFFFL); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\lighting\LightEngine.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */