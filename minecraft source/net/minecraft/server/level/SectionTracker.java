/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.lighting.DynamicGraphMinFixedPoint;
/*    */ 
/*    */ public abstract class SectionTracker
/*    */   extends DynamicGraphMinFixedPoint {
/*  8 */   protected SectionTracker(int levelCount, int minQueueSize, int minMapSize) { super(levelCount, minQueueSize, minMapSize); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void checkNeighborsAfterUpdate(long node, int level, boolean onlyDecrease) {
/* 13 */     if (onlyDecrease && level >= this.levelCount - 2) {
/*    */       return;
/*    */     }
/*    */     
/* 17 */     for (int offsetX = -1; offsetX <= 1; offsetX++) {
/* 18 */       for (int offsetY = -1; offsetY <= 1; offsetY++) {
/* 19 */         for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
/* 20 */           long neighbor = SectionPos.offset(node, offsetX, offsetY, offsetZ);
/* 21 */           if (neighbor != node)
/*    */           {
/*    */             
/* 24 */             checkNeighbor(node, neighbor, level, onlyDecrease);
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected int getComputedLevel(long node, long knownParent, int knownLevelFromParent) {
/* 32 */     int computedLevel = knownLevelFromParent;
/* 33 */     for (int offsetX = -1; offsetX <= 1; offsetX++) {
/* 34 */       for (int offsetY = -1; offsetY <= 1; offsetY++) {
/* 35 */         for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
/* 36 */           long neighbor = SectionPos.offset(node, offsetX, offsetY, offsetZ);
/* 37 */           if (neighbor == node) {
/* 38 */             neighbor = Float.MAX_VALUE;
/*    */           }
/* 40 */           if (neighbor != knownParent) {
/* 41 */             int costFromNeighbor = computeLevelFromNeighbor(neighbor, node, getLevel(neighbor));
/* 42 */             if (computedLevel > costFromNeighbor) {
/* 43 */               computedLevel = costFromNeighbor;
/*    */             }
/* 45 */             if (computedLevel == 0) {
/* 46 */               return computedLevel;
/*    */             }
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/* 52 */     return computedLevel;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int computeLevelFromNeighbor(long from, long to, int fromLevel) {
/* 57 */     if (isSource(from)) {
/* 58 */       return getLevelFromSource(to);
/*    */     }
/* 60 */     return fromLevel + 1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract int getLevelFromSource(long paramLong);
/*    */   
/* 66 */   public void update(long node, int newLevelFrom, boolean onlyDecreased) { checkEdge(Float.MAX_VALUE, node, newLevelFrom, onlyDecreased); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\SectionTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */