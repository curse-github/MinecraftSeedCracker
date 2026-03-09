/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*    */ import it.unimi.dsi.fastutil.longs.LongSet;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.util.thread.TaskScheduler;
/*    */ 
/*    */ 
/*    */ public class ThrottlingChunkTaskDispatcher
/*    */   extends ChunkTaskDispatcher
/*    */ {
/* 14 */   private final LongSet chunkPositionsInExecution = new LongOpenHashSet();
/*    */   private final int maxChunksInExecution;
/*    */   private final String executorSchedulerName;
/*    */   
/*    */   public ThrottlingChunkTaskDispatcher(TaskScheduler<Runnable> executor, Executor dispatcherExecutor, int maxChunksInExecution) {
/* 19 */     super(executor, dispatcherExecutor);
/* 20 */     this.maxChunksInExecution = maxChunksInExecution;
/* 21 */     this.executorSchedulerName = executor.name();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected void onRelease(long key) { this.chunkPositionsInExecution.remove(key); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   protected ChunkTaskPriorityQueue.TasksForChunk popTasks() { return (this.chunkPositionsInExecution.size() < this.maxChunksInExecution) ? super.popTasks() : null; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void scheduleForExecution(ChunkTaskPriorityQueue.TasksForChunk tasksForChunk) {
/* 36 */     this.chunkPositionsInExecution.add(tasksForChunk.chunkPos());
/* 37 */     super.scheduleForExecution(tasksForChunk);
/*    */   }
/*    */ 
/*    */   
/*    */   @VisibleForTesting
/* 42 */   public String getDebugStatus() { return this.executorSchedulerName + "=[" + this.executorSchedulerName + "], s=" + (String)this.chunkPositionsInExecution.longStream().mapToObj(key -> "" + key + ":" + key).collect(Collectors.joining(",")); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ThrottlingChunkTaskDispatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */