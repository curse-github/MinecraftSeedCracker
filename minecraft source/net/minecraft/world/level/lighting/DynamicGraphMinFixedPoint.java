/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*     */ import it.unimi.dsi.fastutil.longs.LongList;
/*     */ import java.util.function.LongPredicate;
/*     */ import net.minecraft.util.Mth;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DynamicGraphMinFixedPoint
/*     */ {
/*     */   public static final long SOURCE = 9223372036854775807L;
/*     */   private static final int NO_COMPUTED_LEVEL = 255;
/*     */   protected final int levelCount;
/*     */   private final LeveledPriorityQueue priorityQueue;
/*     */   private final Long2ByteMap computedLevels;
/*     */   
/*     */   protected DynamicGraphMinFixedPoint(int levelCount, int minQueueSize, final int minMapSize) {
/*  40 */     if (levelCount >= 254) {
/*  41 */       throw new IllegalArgumentException("Level count must be < 254.");
/*     */     }
/*  43 */     this.levelCount = levelCount;
/*     */     
/*  45 */     this.priorityQueue = new LeveledPriorityQueue(levelCount, minQueueSize);
/*     */     
/*  47 */     this.computedLevels = new Long2ByteOpenHashMap(minMapSize, 0.5F)
/*     */       {
/*     */         protected void rehash(int newN) {
/*  50 */           if (newN > minMapSize) {
/*  51 */             super.rehash(newN);
/*     */           }
/*     */         }
/*     */       };
/*  55 */     this.computedLevels.defaultReturnValue((byte)-1);
/*     */   }
/*     */   
/*     */   protected void removeFromQueue(long node) {
/*  59 */     int computedLevel = this.computedLevels.remove(node) & 0xFF;
/*  60 */     if (computedLevel == 255) {
/*     */       return;
/*     */     }
/*  63 */     int level = getLevel(node);
/*  64 */     int priority = calculatePriority(level, computedLevel);
/*  65 */     this.priorityQueue.dequeue(node, priority, this.levelCount);
/*  66 */     this.hasWork = !this.priorityQueue.isEmpty();
/*     */   }
/*     */   
/*     */   public void removeIf(LongPredicate pred) {
/*  70 */     LongArrayList longArrayList = new LongArrayList();
/*     */     
/*  72 */     this.computedLevels.keySet().forEach(node -> {
/*  73 */           if (pred.test(node)) {
/*  74 */             nodesToRemove.add(node);
/*     */           }
/*     */         });
/*     */     
/*  78 */     longArrayList.forEach(this::removeFromQueue);
/*     */   }
/*     */ 
/*     */   
/*  82 */   private int calculatePriority(int level, int computedLevel) { return Math.min(Math.min(level, computedLevel), this.levelCount - 1); }
/*     */ 
/*     */ 
/*     */   
/*  86 */   protected void checkNode(long node) { checkEdge(node, node, this.levelCount - 1, false); }
/*     */ 
/*     */   
/*     */   protected void checkEdge(long from, long to, int newLevelFrom, boolean onlyDecreased) {
/*  90 */     checkEdge(from, to, newLevelFrom, getLevel(to), this.computedLevels.get(to) & 0xFF, onlyDecreased);
/*  91 */     this.hasWork = !this.priorityQueue.isEmpty();
/*     */   }
/*     */   private void checkEdge(long from, long to, int newLevelFrom, int levelTo, int oldComputedLevel, boolean onlyDecreased) {
/*     */     int newComputedLevel;
/*  95 */     if (isSource(to)) {
/*     */       return;
/*     */     }
/*  98 */     newLevelFrom = Mth.clamp(newLevelFrom, 0, this.levelCount - 1);
/*  99 */     levelTo = Mth.clamp(levelTo, 0, this.levelCount - 1);
/* 100 */     boolean wasConsistent = (oldComputedLevel == 255);
/* 101 */     if (wasConsistent) {
/* 102 */       oldComputedLevel = levelTo;
/*     */     }
/*     */     
/* 105 */     if (onlyDecreased) {
/*     */       
/* 107 */       newComputedLevel = Math.min(oldComputedLevel, newLevelFrom);
/*     */     } else {
/* 109 */       newComputedLevel = Mth.clamp(getComputedLevel(to, from, newLevelFrom), 0, this.levelCount - 1);
/*     */     } 
/* 111 */     int oldPriority = calculatePriority(levelTo, oldComputedLevel);
/* 112 */     if (levelTo != newComputedLevel) {
/* 113 */       int newPriority = calculatePriority(levelTo, newComputedLevel);
/* 114 */       if (oldPriority != newPriority && !wasConsistent) {
/* 115 */         this.priorityQueue.dequeue(to, oldPriority, newPriority);
/*     */       }
/* 117 */       this.priorityQueue.enqueue(to, newPriority);
/* 118 */       this.computedLevels.put(to, (byte)newComputedLevel);
/* 119 */     } else if (!wasConsistent) {
/* 120 */       this.priorityQueue.dequeue(to, oldPriority, this.levelCount);
/* 121 */       this.computedLevels.remove(to);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void checkNeighbor(long from, long to, int level, boolean onlyDecreased) {
/* 126 */     int storedOldComputedLevel = this.computedLevels.get(to) & 0xFF;
/* 127 */     int levelFrom = Mth.clamp(computeLevelFromNeighbor(from, to, level), 0, this.levelCount - 1);
/* 128 */     if (onlyDecreased) {
/* 129 */       checkEdge(from, to, levelFrom, getLevel(to), storedOldComputedLevel, onlyDecreased);
/*     */     } else {
/*     */       int oldComputedLevel;
/* 132 */       boolean wasConsistent = (storedOldComputedLevel == 255);
/* 133 */       if (wasConsistent) {
/* 134 */         oldComputedLevel = Mth.clamp(getLevel(to), 0, this.levelCount - 1);
/*     */       } else {
/* 136 */         oldComputedLevel = storedOldComputedLevel;
/*     */       } 
/*     */       
/* 139 */       if (levelFrom == oldComputedLevel)
/*     */       {
/* 141 */         checkEdge(from, to, this.levelCount - 1, wasConsistent ? oldComputedLevel : getLevel(to), storedOldComputedLevel, onlyDecreased);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 147 */   protected final boolean hasWork() { return this.hasWork; }
/*     */ 
/*     */   
/*     */   protected final int runUpdates(int count) {
/* 151 */     if (this.priorityQueue.isEmpty()) {
/* 152 */       return count;
/*     */     }
/* 154 */     while (!this.priorityQueue.isEmpty() && count > 0) {
/* 155 */       count--;
/* 156 */       long node = this.priorityQueue.removeFirstLong();
/* 157 */       int level = Mth.clamp(getLevel(node), 0, this.levelCount - 1);
/* 158 */       int computedLevel = this.computedLevels.remove(node) & 0xFF;
/* 159 */       if (computedLevel < level) {
/*     */         
/* 161 */         setLevel(node, computedLevel);
/* 162 */         checkNeighborsAfterUpdate(node, computedLevel, true); continue;
/* 163 */       }  if (computedLevel > level) {
/*     */         
/* 165 */         setLevel(node, this.levelCount - 1);
/* 166 */         if (computedLevel != this.levelCount - 1) {
/*     */           
/* 168 */           this.priorityQueue.enqueue(node, calculatePriority(this.levelCount - 1, computedLevel));
/* 169 */           this.computedLevels.put(node, (byte)computedLevel);
/*     */         } 
/* 171 */         checkNeighborsAfterUpdate(node, level, false);
/*     */       } 
/*     */     } 
/* 174 */     this.hasWork = !this.priorityQueue.isEmpty();
/* 175 */     return count;
/*     */   }
/*     */ 
/*     */   
/* 179 */   public int getQueueSize() { return this.computedLevels.size(); }
/*     */ 
/*     */ 
/*     */   
/* 183 */   protected boolean isSource(long node) { return (node == Float.MAX_VALUE); }
/*     */   
/*     */   protected abstract int getComputedLevel(long paramLong1, long paramLong2, int paramInt);
/*     */   
/*     */   protected abstract void checkNeighborsAfterUpdate(long paramLong, int paramInt, boolean paramBoolean);
/*     */   
/*     */   protected abstract int getLevel(long paramLong);
/*     */   
/*     */   protected abstract void setLevel(long paramLong, int paramInt);
/*     */   
/*     */   protected abstract int computeLevelFromNeighbor(long paramLong1, long paramLong2, int paramInt);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\lighting\DynamicGraphMinFixedPoint.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */