/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
/*    */ 
/*    */ public class LeveledPriorityQueue
/*    */ {
/*    */   private final int levelCount;
/*    */   private final LongLinkedOpenHashSet[] queues;
/*    */   private int firstQueuedLevel;
/*    */   
/*    */   public LeveledPriorityQueue(int levelCount, final int minSize) {
/* 12 */     this.levelCount = levelCount;
/* 13 */     this.queues = new LongLinkedOpenHashSet[levelCount];
/* 14 */     for (int i = 0; i < levelCount; i++) {
/* 15 */       this.queues[i] = new LongLinkedOpenHashSet(minSize, 0.5F)
/*    */         {
/*    */           protected void rehash(int newN) {
/* 18 */             if (newN > minSize) {
/* 19 */               super.rehash(newN);
/*    */             }
/*    */           }
/*    */         };
/*    */     } 
/* 24 */     this.firstQueuedLevel = levelCount;
/*    */   }
/*    */   
/*    */   public long removeFirstLong() {
/* 28 */     LongLinkedOpenHashSet queue = this.queues[this.firstQueuedLevel];
/* 29 */     long result = queue.removeFirstLong();
/* 30 */     if (queue.isEmpty()) {
/* 31 */       checkFirstQueuedLevel(this.levelCount);
/*    */     }
/* 33 */     return result;
/*    */   }
/*    */ 
/*    */   
/* 37 */   public boolean isEmpty() { return (this.firstQueuedLevel >= this.levelCount); }
/*    */ 
/*    */   
/*    */   public void dequeue(long node, int key, int upperBound) {
/* 41 */     LongLinkedOpenHashSet queue = this.queues[key];
/* 42 */     queue.remove(node);
/* 43 */     if (queue.isEmpty() && this.firstQueuedLevel == key) {
/* 44 */       checkFirstQueuedLevel(upperBound);
/*    */     }
/*    */   }
/*    */   
/*    */   public void enqueue(long node, int key) {
/* 49 */     this.queues[key].add(node);
/* 50 */     if (this.firstQueuedLevel > key) {
/* 51 */       this.firstQueuedLevel = key;
/*    */     }
/*    */   }
/*    */   
/*    */   private void checkFirstQueuedLevel(int upperBound) {
/* 56 */     int oldLevel = this.firstQueuedLevel;
/* 57 */     this.firstQueuedLevel = upperBound;
/* 58 */     for (int i = oldLevel + 1; i < upperBound; i++) {
/* 59 */       if (!this.queues[i].isEmpty()) {
/* 60 */         this.firstQueuedLevel = i;
/*    */         break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\lighting\LeveledPriorityQueue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */