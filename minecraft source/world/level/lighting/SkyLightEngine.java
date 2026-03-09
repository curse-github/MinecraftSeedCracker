/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LightChunk;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ 
/*     */ public final class SkyLightEngine
/*     */   extends LightEngine<SkyLightSectionStorage.SkyDataLayerStorageMap, SkyLightSectionStorage>
/*     */ {
/*  18 */   private static final long REMOVE_TOP_SKY_SOURCE_ENTRY = LightEngine.QueueEntry.decreaseAllDirections(15);
/*  19 */   private static final long REMOVE_SKY_SOURCE_ENTRY = LightEngine.QueueEntry.decreaseSkipOneDirection(15, Direction.UP);
/*  20 */   private static final long ADD_SKY_SOURCE_ENTRY = LightEngine.QueueEntry.increaseSkipOneDirection(15, false, Direction.UP);
/*     */   
/*  22 */   private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
/*     */   
/*     */   private final ChunkSkyLightSources emptyChunkSources;
/*     */ 
/*     */   
/*  27 */   public SkyLightEngine(LightChunkGetter chunkSource) { this(chunkSource, new SkyLightSectionStorage(chunkSource)); }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   protected SkyLightEngine(LightChunkGetter chunkSource, SkyLightSectionStorage storage) {
/*  32 */     super(chunkSource, storage);
/*  33 */     this.emptyChunkSources = new ChunkSkyLightSources(chunkSource.getLevel());
/*     */   }
/*     */ 
/*     */   
/*  37 */   private static boolean isSourceLevel(int value) { return (value == 15); }
/*     */ 
/*     */   
/*     */   private int getLowestSourceY(int x, int z, int defaultValue) {
/*  41 */     ChunkSkyLightSources sources = getChunkSources(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z));
/*  42 */     if (sources == null) {
/*  43 */       return defaultValue;
/*     */     }
/*  45 */     return sources.getLowestSourceY(SectionPos.sectionRelative(x), SectionPos.sectionRelative(z));
/*     */   }
/*     */   
/*     */   private ChunkSkyLightSources getChunkSources(int chunkX, int chunkZ) {
/*  49 */     LightChunk chunk = this.chunkSource.getChunkForLighting(chunkX, chunkZ);
/*  50 */     return (chunk != null) ? chunk.getSkyLightSources() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkNode(long blockNode) {
/*  55 */     int x = BlockPos.getX(blockNode);
/*  56 */     int y = BlockPos.getY(blockNode);
/*  57 */     int z = BlockPos.getZ(blockNode);
/*  58 */     long sectionNode = SectionPos.blockToSection(blockNode);
/*     */     
/*  60 */     int lowestSourceY = ((SkyLightSectionStorage)this.storage).lightOnInSection(sectionNode) ? getLowestSourceY(x, z, 2147483647) : Integer.MAX_VALUE;
/*  61 */     if (lowestSourceY != Integer.MAX_VALUE) {
/*  62 */       updateSourcesInColumn(x, z, lowestSourceY);
/*     */     }
/*     */     
/*  65 */     if (!((SkyLightSectionStorage)this.storage).storingLightForSection(sectionNode)) {
/*     */       return;
/*     */     }
/*     */     
/*  69 */     boolean isSource = (y >= lowestSourceY);
/*  70 */     if (isSource) {
/*  71 */       enqueueDecrease(blockNode, REMOVE_SKY_SOURCE_ENTRY);
/*  72 */       enqueueIncrease(blockNode, ADD_SKY_SOURCE_ENTRY);
/*     */     } else {
/*  74 */       int oldLevel = ((SkyLightSectionStorage)this.storage).getStoredLevel(blockNode);
/*  75 */       if (oldLevel > 0) {
/*  76 */         ((SkyLightSectionStorage)this.storage).setStoredLevel(blockNode, 0);
/*  77 */         enqueueDecrease(blockNode, LightEngine.QueueEntry.decreaseAllDirections(oldLevel));
/*     */       } else {
/*  79 */         enqueueDecrease(blockNode, PULL_LIGHT_IN_ENTRY);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateSourcesInColumn(int x, int z, int lowestSourceY) {
/*  85 */     int worldBottomY = SectionPos.sectionToBlockCoord(((SkyLightSectionStorage)this.storage).getBottomSectionY());
/*  86 */     removeSourcesBelow(x, z, lowestSourceY, worldBottomY);
/*  87 */     addSourcesAbove(x, z, lowestSourceY, worldBottomY);
/*     */   }
/*     */   
/*     */   private void removeSourcesBelow(int x, int z, int lowestSourceY, int worldBottomY) {
/*  91 */     if (lowestSourceY <= worldBottomY) {
/*     */       return;
/*     */     }
/*     */     
/*  95 */     int sectionX = SectionPos.blockToSectionCoord(x);
/*  96 */     int sectionZ = SectionPos.blockToSectionCoord(z);
/*     */     
/*  98 */     int startY = lowestSourceY - 1;
/*     */     
/* 100 */     int sectionY = SectionPos.blockToSectionCoord(startY);
/* 101 */     while (((SkyLightSectionStorage)this.storage).hasLightDataAtOrBelow(sectionY)) {
/* 102 */       if (((SkyLightSectionStorage)this.storage).storingLightForSection(SectionPos.asLong(sectionX, sectionY, sectionZ))) {
/* 103 */         int sectionBottomY = SectionPos.sectionToBlockCoord(sectionY);
/* 104 */         int sectionTopY = sectionBottomY + 15;
/* 105 */         for (int y = Math.min(sectionTopY, startY); y >= sectionBottomY; y--) {
/* 106 */           long blockNode = BlockPos.asLong(x, y, z);
/* 107 */           if (!isSourceLevel(((SkyLightSectionStorage)this.storage).getStoredLevel(blockNode))) {
/*     */             return;
/*     */           }
/* 110 */           ((SkyLightSectionStorage)this.storage).setStoredLevel(blockNode, 0);
/*     */           
/* 112 */           enqueueDecrease(blockNode, (y == lowestSourceY - 1) ? REMOVE_TOP_SKY_SOURCE_ENTRY : REMOVE_SKY_SOURCE_ENTRY);
/*     */         } 
/*     */       } 
/* 115 */       sectionY--;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addSourcesAbove(int x, int z, int lowestSourceY, int worldBottomY) {
/* 120 */     int sectionX = SectionPos.blockToSectionCoord(x);
/* 121 */     int sectionZ = SectionPos.blockToSectionCoord(z);
/*     */     
/* 123 */     int neighborLowestSourceY = Math.max(
/* 124 */         Math.max(getLowestSourceY(x - 1, z, -2147483648), getLowestSourceY(x + 1, z, -2147483648)), 
/* 125 */         Math.max(getLowestSourceY(x, z - 1, -2147483648), getLowestSourceY(x, z + 1, -2147483648)));
/*     */ 
/*     */     
/* 128 */     int startY = Math.max(lowestSourceY, worldBottomY);
/* 129 */     long sectionNode = SectionPos.asLong(sectionX, SectionPos.blockToSectionCoord(startY), sectionZ);
/* 130 */     while (!((SkyLightSectionStorage)this.storage).isAboveData(sectionNode)) {
/* 131 */       if (((SkyLightSectionStorage)this.storage).storingLightForSection(sectionNode)) {
/* 132 */         int sectionBottomY = SectionPos.sectionToBlockCoord(SectionPos.y(sectionNode));
/* 133 */         int sectionTopY = sectionBottomY + 15;
/* 134 */         for (int y = Math.max(sectionBottomY, startY); y <= sectionTopY; y++) {
/* 135 */           long blockNode = BlockPos.asLong(x, y, z);
/* 136 */           if (isSourceLevel(((SkyLightSectionStorage)this.storage).getStoredLevel(blockNode))) {
/*     */             return;
/*     */           }
/* 139 */           ((SkyLightSectionStorage)this.storage).setStoredLevel(blockNode, 15);
/* 140 */           if (y < neighborLowestSourceY || y == lowestSourceY)
/*     */           {
/* 142 */             enqueueIncrease(blockNode, ADD_SKY_SOURCE_ENTRY);
/*     */           }
/*     */         } 
/*     */       } 
/* 146 */       sectionNode = SectionPos.offset(sectionNode, Direction.UP);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void propagateIncrease(long fromNode, long increaseData, int fromLevel) {
/* 152 */     BlockState fromState = null;
/* 153 */     int emptySectionsBelow = countEmptySectionsBelowIfAtBorder(fromNode);
/* 154 */     for (Direction propagationDirection : PROPAGATION_DIRECTIONS) {
/* 155 */       if (LightEngine.QueueEntry.shouldPropagateInDirection(increaseData, propagationDirection)) {
/*     */ 
/*     */         
/* 158 */         long toNode = BlockPos.offset(fromNode, propagationDirection);
/* 159 */         if (((SkyLightSectionStorage)this.storage).storingLightForSection(SectionPos.blockToSection(toNode))) {
/*     */ 
/*     */ 
/*     */           
/* 163 */           int toLevel = ((SkyLightSectionStorage)this.storage).getStoredLevel(toNode);
/* 164 */           int maxPossibleNewToLevel = fromLevel - 1;
/* 165 */           if (maxPossibleNewToLevel > toLevel) {
/*     */ 
/*     */ 
/*     */             
/* 169 */             this.mutablePos.set(toNode);
/* 170 */             BlockState toState = getState(this.mutablePos);
/* 171 */             int newToLevel = fromLevel - getOpacity(toState);
/* 172 */             if (newToLevel > toLevel) {
/*     */ 
/*     */ 
/*     */               
/* 176 */               if (fromState == null) {
/* 177 */                 fromState = LightEngine.QueueEntry.isFromEmptyShape(increaseData) ? Blocks.AIR.defaultBlockState() : getState(this.mutablePos.set(fromNode));
/*     */               }
/* 179 */               if (!shapeOccludes(fromState, toState, propagationDirection)) {
/* 180 */                 ((SkyLightSectionStorage)this.storage).setStoredLevel(toNode, newToLevel);
/* 181 */                 if (newToLevel > 1) {
/* 182 */                   enqueueIncrease(toNode, LightEngine.QueueEntry.increaseSkipOneDirection(newToLevel, isEmptyShape(toState), propagationDirection.getOpposite()));
/*     */                 }
/* 184 */                 propagateFromEmptySections(toNode, propagationDirection, newToLevel, true, emptySectionsBelow);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }  } protected void propagateDecrease(long fromNode, long decreaseData) {
/* 191 */     int emptySectionsBelow = countEmptySectionsBelowIfAtBorder(fromNode);
/* 192 */     int oldFromLevel = LightEngine.QueueEntry.getFromLevel(decreaseData);
/* 193 */     for (Direction propagationDirection : PROPAGATION_DIRECTIONS) {
/* 194 */       if (LightEngine.QueueEntry.shouldPropagateInDirection(decreaseData, propagationDirection)) {
/*     */ 
/*     */         
/* 197 */         long toNode = BlockPos.offset(fromNode, propagationDirection);
/* 198 */         if (((SkyLightSectionStorage)this.storage).storingLightForSection(SectionPos.blockToSection(toNode))) {
/*     */ 
/*     */ 
/*     */           
/* 202 */           int toLevel = ((SkyLightSectionStorage)this.storage).getStoredLevel(toNode);
/* 203 */           if (toLevel != 0)
/*     */           {
/*     */ 
/*     */             
/* 207 */             if (toLevel <= oldFromLevel - 1) {
/* 208 */               ((SkyLightSectionStorage)this.storage).setStoredLevel(toNode, 0);
/* 209 */               enqueueDecrease(toNode, LightEngine.QueueEntry.decreaseSkipOneDirection(toLevel, propagationDirection.getOpposite()));
/* 210 */               propagateFromEmptySections(toNode, propagationDirection, toLevel, false, emptySectionsBelow);
/*     */             } else {
/* 212 */               enqueueIncrease(toNode, LightEngine.QueueEntry.increaseOnlyOneDirection(toLevel, false, propagationDirection.getOpposite()));
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int countEmptySectionsBelowIfAtBorder(long blockNode) {
/* 222 */     int y = BlockPos.getY(blockNode);
/* 223 */     int localY = SectionPos.sectionRelative(y);
/* 224 */     if (localY != 0) {
/* 225 */       return 0;
/*     */     }
/* 227 */     int x = BlockPos.getX(blockNode);
/* 228 */     int z = BlockPos.getZ(blockNode);
/* 229 */     int localX = SectionPos.sectionRelative(x);
/* 230 */     int localZ = SectionPos.sectionRelative(z);
/* 231 */     if (localX == 0 || localX == 15 || localZ == 0 || localZ == 15) {
/* 232 */       int sectionX = SectionPos.blockToSectionCoord(x);
/* 233 */       int sectionY = SectionPos.blockToSectionCoord(y);
/* 234 */       int sectionZ = SectionPos.blockToSectionCoord(z);
/* 235 */       int emptySectionsBelow = 0;
/* 236 */       while (!((SkyLightSectionStorage)this.storage).storingLightForSection(SectionPos.asLong(sectionX, sectionY - emptySectionsBelow - 1, sectionZ)) && ((SkyLightSectionStorage)this.storage).hasLightDataAtOrBelow(sectionY - emptySectionsBelow - 1)) {
/* 237 */         emptySectionsBelow++;
/*     */       }
/* 239 */       return emptySectionsBelow;
/*     */     } 
/* 241 */     return 0;
/*     */   }
/*     */   
/*     */   private void propagateFromEmptySections(long toNode, Direction propagationDirection, int toLevel, boolean increase, int emptySectionsBelow) {
/* 245 */     if (emptySectionsBelow == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 249 */     int x = BlockPos.getX(toNode);
/* 250 */     int z = BlockPos.getZ(toNode);
/* 251 */     if (!crossedSectionEdge(propagationDirection, SectionPos.sectionRelative(x), SectionPos.sectionRelative(z))) {
/*     */       return;
/*     */     }
/*     */     
/* 255 */     int y = BlockPos.getY(toNode);
/* 256 */     int sectionX = SectionPos.blockToSectionCoord(x);
/* 257 */     int sectionZ = SectionPos.blockToSectionCoord(z);
/* 258 */     int sectionY = SectionPos.blockToSectionCoord(y) - 1;
/*     */     
/* 260 */     int bottomSectionY = sectionY - emptySectionsBelow + 1;
/* 261 */     while (sectionY >= bottomSectionY) {
/* 262 */       if (!((SkyLightSectionStorage)this.storage).storingLightForSection(SectionPos.asLong(sectionX, sectionY, sectionZ))) {
/* 263 */         sectionY--;
/*     */         continue;
/*     */       } 
/* 266 */       int sectionMinY = SectionPos.sectionToBlockCoord(sectionY);
/* 267 */       for (int localY = 15; localY >= 0; localY--) {
/* 268 */         long blockNode = BlockPos.asLong(x, sectionMinY + localY, z);
/* 269 */         if (increase) {
/* 270 */           ((SkyLightSectionStorage)this.storage).setStoredLevel(blockNode, toLevel);
/* 271 */           if (toLevel > 1)
/*     */           {
/* 273 */             enqueueIncrease(blockNode, LightEngine.QueueEntry.increaseSkipOneDirection(toLevel, true, propagationDirection.getOpposite()));
/*     */           }
/*     */         } else {
/* 276 */           ((SkyLightSectionStorage)this.storage).setStoredLevel(blockNode, 0);
/* 277 */           enqueueDecrease(blockNode, LightEngine.QueueEntry.decreaseSkipOneDirection(toLevel, propagationDirection.getOpposite()));
/*     */         } 
/*     */       } 
/* 280 */       sectionY--;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean crossedSectionEdge(Direction propagationDirection, int x, int z) {
/* 285 */     switch (propagationDirection) { case NORTH: return 
/* 286 */           (z == 15);
/* 287 */       case SOUTH: return (z == 0);
/* 288 */       case WEST: return (x == 15);
/* 289 */       case EAST: return (x == 0); }
/*     */     
/*     */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLightEnabled(ChunkPos pos, boolean enable) {
/* 296 */     super.setLightEnabled(pos, enable);
/*     */ 
/*     */ 
/*     */     
/* 300 */     if (enable) {
/* 301 */       ChunkSkyLightSources sources = (ChunkSkyLightSources)Objects.requireNonNullElse(getChunkSources(pos.x, pos.z), this.emptyChunkSources);
/* 302 */       int highestNonSourceY = sources.getHighestLowestSourceY() - 1;
/* 303 */       int lowestFullySourceSectionY = SectionPos.blockToSectionCoord(highestNonSourceY) + 1;
/*     */       
/* 305 */       long zeroNode = SectionPos.getZeroNode(pos.x, pos.z);
/* 306 */       int topSectionY = ((SkyLightSectionStorage)this.storage).getTopSectionY(zeroNode);
/* 307 */       int bottomSectionY = Math.max(((SkyLightSectionStorage)this.storage).getBottomSectionY(), lowestFullySourceSectionY);
/* 308 */       for (int sectionY = topSectionY - 1; sectionY >= bottomSectionY; sectionY--) {
/* 309 */         DataLayer dataLayer = ((SkyLightSectionStorage)this.storage).getDataLayerToWrite(SectionPos.asLong(pos.x, sectionY, pos.z));
/* 310 */         if (dataLayer != null && dataLayer.isEmpty()) {
/* 311 */           dataLayer.fill(15);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void propagateLightSources(ChunkPos pos) {
/* 319 */     long zeroNode = SectionPos.getZeroNode(pos.x, pos.z);
/* 320 */     ((SkyLightSectionStorage)this.storage).setLightEnabled(zeroNode, true);
/*     */     
/* 322 */     ChunkSkyLightSources sources = (ChunkSkyLightSources)Objects.requireNonNullElse(getChunkSources(pos.x, pos.z), this.emptyChunkSources);
/* 323 */     ChunkSkyLightSources northSources = (ChunkSkyLightSources)Objects.requireNonNullElse(getChunkSources(pos.x, pos.z - 1), this.emptyChunkSources);
/* 324 */     ChunkSkyLightSources southSources = (ChunkSkyLightSources)Objects.requireNonNullElse(getChunkSources(pos.x, pos.z + 1), this.emptyChunkSources);
/* 325 */     ChunkSkyLightSources westSources = (ChunkSkyLightSources)Objects.requireNonNullElse(getChunkSources(pos.x - 1, pos.z), this.emptyChunkSources);
/* 326 */     ChunkSkyLightSources eastSources = (ChunkSkyLightSources)Objects.requireNonNullElse(getChunkSources(pos.x + 1, pos.z), this.emptyChunkSources);
/*     */     
/* 328 */     int topSectionY = ((SkyLightSectionStorage)this.storage).getTopSectionY(zeroNode);
/* 329 */     int bottomSectionY = ((SkyLightSectionStorage)this.storage).getBottomSectionY();
/*     */     
/* 331 */     int sectionMinX = SectionPos.sectionToBlockCoord(pos.x);
/* 332 */     int sectionMinZ = SectionPos.sectionToBlockCoord(pos.z);
/*     */     
/* 334 */     for (int sectionY = topSectionY - 1; sectionY >= bottomSectionY; sectionY--) {
/* 335 */       long sectionNode = SectionPos.asLong(pos.x, sectionY, pos.z);
/* 336 */       DataLayer dataLayer = ((SkyLightSectionStorage)this.storage).getDataLayerToWrite(sectionNode);
/* 337 */       if (dataLayer != null) {
/*     */ 
/*     */ 
/*     */         
/* 341 */         int sectionMinY = SectionPos.sectionToBlockCoord(sectionY);
/* 342 */         int sectionMaxY = sectionMinY + 15;
/*     */         
/* 344 */         boolean sourcesBelow = false;
/*     */         
/* 346 */         for (int z = 0; z < 16; z++) {
/* 347 */           for (int x = 0; x < 16; x++) {
/* 348 */             int lowestSourceY = sources.getLowestSourceY(x, z);
/* 349 */             if (lowestSourceY <= sectionMaxY) {
/*     */ 
/*     */ 
/*     */               
/* 353 */               int northLowestSourceY = (z == 0) ? northSources.getLowestSourceY(x, 15) : sources.getLowestSourceY(x, z - 1);
/* 354 */               int southLowestSourceY = (z == 15) ? southSources.getLowestSourceY(x, 0) : sources.getLowestSourceY(x, z + 1);
/* 355 */               int westLowestSourceY = (x == 0) ? westSources.getLowestSourceY(15, z) : sources.getLowestSourceY(x - 1, z);
/* 356 */               int eastLowestSourceY = (x == 15) ? eastSources.getLowestSourceY(0, z) : sources.getLowestSourceY(x + 1, z);
/* 357 */               int neighborLowestSourceY = Math.max(
/* 358 */                   Math.max(northLowestSourceY, southLowestSourceY), 
/* 359 */                   Math.max(westLowestSourceY, eastLowestSourceY));
/*     */ 
/*     */               
/* 362 */               for (int y = sectionMaxY; y >= Math.max(sectionMinY, lowestSourceY); y--) {
/* 363 */                 dataLayer.set(x, SectionPos.sectionRelative(y), z, 15);
/* 364 */                 if (y == lowestSourceY || y < neighborLowestSourceY) {
/* 365 */                   long blockNode = BlockPos.asLong(sectionMinX + x, y, sectionMinZ + z);
/* 366 */                   enqueueIncrease(blockNode, LightEngine.QueueEntry.increaseSkySourceInDirections((y == lowestSourceY), (y < northLowestSourceY), (y < southLowestSourceY), (y < westLowestSourceY), (y < eastLowestSourceY)));
/*     */                 } 
/*     */               } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 376 */               if (lowestSourceY < sectionMinY) {
/* 377 */                 sourcesBelow = true;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/* 382 */         if (!sourcesBelow)
/*     */           break; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\lighting\SkyLightEngine.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */