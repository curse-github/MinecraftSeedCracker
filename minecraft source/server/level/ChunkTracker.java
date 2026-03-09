/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.lighting.DynamicGraphMinFixedPoint;
/*    */ 
/*    */ public abstract class ChunkTracker
/*    */   extends DynamicGraphMinFixedPoint {
/*  8 */   protected ChunkTracker(int levelCount, int minQueueSize, int minMapSize) { super(levelCount, minQueueSize, minMapSize); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 13 */   protected boolean isSource(long node) { return (node == ChunkPos.INVALID_CHUNK_POS); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void checkNeighborsAfterUpdate(long node, int level, boolean onlyDecrease) {
/* 18 */     if (onlyDecrease && level >= this.levelCount - 2) {
/*    */       return;
/*    */     }
/*    */     
/* 22 */     ChunkPos pos = new ChunkPos(node);
/* 23 */     int x = pos.x;
/* 24 */     int z = pos.z;
/* 25 */     for (int offsetX = -1; offsetX <= 1; offsetX++) {
/* 26 */       for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
/* 27 */         long neighbor = ChunkPos.asLong(x + offsetX, z + offsetZ);
/* 28 */         if (neighbor != node)
/*    */         {
/*    */           
/* 31 */           checkNeighbor(node, neighbor, level, onlyDecrease);
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected int getComputedLevel(long node, long knownParent, int knownLevelFromParent) {
/* 38 */     int computedLevel = knownLevelFromParent;
/* 39 */     ChunkPos pos = new ChunkPos(node);
/* 40 */     int x = pos.x;
/* 41 */     int z = pos.z;
/* 42 */     for (int offsetX = -1; offsetX <= 1; offsetX++) {
/* 43 */       for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
/* 44 */         long neighbor = ChunkPos.asLong(x + offsetX, z + offsetZ);
/* 45 */         if (neighbor == node) {
/* 46 */           neighbor = ChunkPos.INVALID_CHUNK_POS;
/*    */         }
/* 48 */         if (neighbor != knownParent) {
/* 49 */           int costFromNeighbor = computeLevelFromNeighbor(neighbor, node, getLevel(neighbor));
/* 50 */           if (computedLevel > costFromNeighbor) {
/* 51 */             computedLevel = costFromNeighbor;
/*    */           }
/* 53 */           if (computedLevel == 0) {
/* 54 */             return computedLevel;
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/* 59 */     return computedLevel;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int computeLevelFromNeighbor(long from, long to, int fromLevel) {
/* 64 */     if (from == ChunkPos.INVALID_CHUNK_POS) {
/* 65 */       return getLevelFromSource(to);
/*    */     }
/* 67 */     return fromLevel + 1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract int getLevelFromSource(long paramLong);
/*    */   
/* 73 */   public void update(long node, int newLevelFrom, boolean onlyDecreased) { checkEdge(ChunkPos.INVALID_CHUNK_POS, node, newLevelFrom, onlyDecreased); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ChunkTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */