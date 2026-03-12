/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
/*    */ import java.util.List;
/*    */ import java.util.stream.IntStream;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChunkTaskPriorityQueue
/*    */ {
/* 15 */   public static final int PRIORITY_LEVEL_COUNT = ChunkLevel.MAX_LEVEL + 2; private final List<Long2ObjectLinkedOpenHashMap<List<Runnable>>> queuesPerPriority; private final String name; public ChunkTaskPriorityQueue(String name) {
/* 16 */     this.queuesPerPriority = IntStream.range(0, PRIORITY_LEVEL_COUNT).mapToObj(priority -> new Long2ObjectLinkedOpenHashMap()).toList();
/* 17 */     this.topPriorityQueueIndex = PRIORITY_LEVEL_COUNT;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 22 */     this.name = name;
/*    */   }
/*    */   
/*    */   protected void resortChunkTasks(int oldPriority, ChunkPos pos, int newPriority) {
/* 26 */     if (oldPriority >= PRIORITY_LEVEL_COUNT) {
/*    */       return;
/*    */     }
/* 29 */     Long2ObjectLinkedOpenHashMap<List<Runnable>> oldQueue = (Long2ObjectLinkedOpenHashMap)this.queuesPerPriority.get(oldPriority);
/* 30 */     List<Runnable> oldTasks = (List)oldQueue.remove(pos.toLong());
/* 31 */     if (oldPriority == this.topPriorityQueueIndex) {
/* 32 */       while (hasWork() && ((Long2ObjectLinkedOpenHashMap)this.queuesPerPriority.get(this.topPriorityQueueIndex)).isEmpty()) {
/* 33 */         this.topPriorityQueueIndex++;
/*    */       }
/*    */     }
/* 36 */     if (oldTasks != null && !oldTasks.isEmpty()) {
/* 37 */       ((List)((Long2ObjectLinkedOpenHashMap)this.queuesPerPriority.get(newPriority)).computeIfAbsent(pos.toLong(), k -> Lists.newArrayList())).addAll(oldTasks);
/* 38 */       this.topPriorityQueueIndex = Math.min(this.topPriorityQueueIndex, newPriority);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void submit(Runnable task, long chunkPos, int level) {
/* 43 */     ((List)((Long2ObjectLinkedOpenHashMap)this.queuesPerPriority.get(level)).computeIfAbsent(chunkPos, p -> Lists.newArrayList())).add(task);
/* 44 */     this.topPriorityQueueIndex = Math.min(this.topPriorityQueueIndex, level);
/*    */   }
/*    */   
/*    */   protected void release(long pos, boolean unschedule) {
/* 48 */     for (Long2ObjectLinkedOpenHashMap<List<Runnable>> queue : this.queuesPerPriority) {
/* 49 */       List<Runnable> tasks = (List)queue.get(pos);
/* 50 */       if (tasks == null) {
/*    */         continue;
/*    */       }
/* 53 */       if (unschedule) {
/* 54 */         tasks.clear();
/*    */       }
/* 56 */       if (tasks.isEmpty()) {
/* 57 */         queue.remove(pos);
/*    */       }
/*    */     } 
/* 60 */     while (hasWork() && ((Long2ObjectLinkedOpenHashMap)this.queuesPerPriority.get(this.topPriorityQueueIndex)).isEmpty()) {
/* 61 */       this.topPriorityQueueIndex++;
/*    */     }
/*    */   }
/*    */   
/*    */   public TasksForChunk pop() {
/* 66 */     if (!hasWork()) {
/* 67 */       return null;
/*    */     }
/* 69 */     int index = this.topPriorityQueueIndex;
/* 70 */     Long2ObjectLinkedOpenHashMap<List<Runnable>> queue = (Long2ObjectLinkedOpenHashMap)this.queuesPerPriority.get(index);
/* 71 */     long chunkPos = queue.firstLongKey();
/* 72 */     List<Runnable> tasks = (List)queue.removeFirst();
/* 73 */     while (hasWork() && ((Long2ObjectLinkedOpenHashMap)this.queuesPerPriority.get(this.topPriorityQueueIndex)).isEmpty()) {
/* 74 */       this.topPriorityQueueIndex++;
/*    */     }
/* 76 */     return new TasksForChunk(chunkPos, tasks);
/*    */   }
/*    */ 
/*    */   
/* 80 */   public boolean hasWork() { return (this.topPriorityQueueIndex < PRIORITY_LEVEL_COUNT); }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 85 */     return this.name + " " + this.name + "...";
/*    */   }
/*    */   public static final class TasksForChunk extends Record { private final long chunkPos; private final List<Runnable> tasks;
/* 88 */     public TasksForChunk(long chunkPos, List<Runnable> tasks) { this.chunkPos = chunkPos; this.tasks = tasks; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ChunkTaskPriorityQueue$TasksForChunk;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #88	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 88 */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkTaskPriorityQueue$TasksForChunk; } public long chunkPos() { return this.chunkPos; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ChunkTaskPriorityQueue$TasksForChunk;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #88	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkTaskPriorityQueue$TasksForChunk; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ChunkTaskPriorityQueue$TasksForChunk;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #88	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/level/ChunkTaskPriorityQueue$TasksForChunk;
/* 88 */       //   0	8	1	o	Ljava/lang/Object; } public List<Runnable> tasks() { return this.tasks; } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ChunkTaskPriorityQueue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */