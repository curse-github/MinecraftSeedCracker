/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BiFunction;
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
/*     */ class BatchedTaskSplitter<K, U, V>
/*     */   extends ParallelMapTransform.SplitterBase<K, U, V>
/*     */ {
/*     */   private final Map<K, V> result;
/*     */   private final int batchSize;
/*     */   private final int firstUndersizedBatchIndex;
/*     */   
/*     */   private BatchedTaskSplitter(BiFunction<K, U, V> operation, int size, int maxTasks) {
/* 190 */     super(operation, size, maxTasks);
/* 191 */     this.result = new HashMap(size);
/* 192 */     this.batchSize = Mth.positiveCeilDiv(size, maxTasks);
/*     */     
/* 194 */     int fullCapacity = this.batchSize * maxTasks;
/* 195 */     int leftoverCapacity = fullCapacity - size;
/*     */ 
/*     */     
/* 198 */     this.firstUndersizedBatchIndex = maxTasks - leftoverCapacity;
/* 199 */     assert this.firstUndersizedBatchIndex > 0 && this.firstUndersizedBatchIndex <= maxTasks;
/*     */   }
/*     */ 
/*     */   
/*     */   protected CompletableFuture<?> scheduleBatch(ParallelMapTransform.Container<K, U, V> container, int startIndex, int endIndex, Executor executor) {
/* 204 */     int batchSize = endIndex - startIndex;
/*     */     
/* 206 */     assert batchSize == this.batchSize || batchSize == this.batchSize - 1;
/* 207 */     return CompletableFuture.runAsync(createTask(this.result, startIndex, endIndex, container), executor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 212 */   protected int batchSize(int index) { return (index < this.firstUndersizedBatchIndex) ? this.batchSize : (this.batchSize - 1); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, U, V> Runnable createTask(Map<K, V> result, int startIndex, int endIndex, ParallelMapTransform.Container<K, U, V> container) {
/* 217 */     return () -> {
/* 218 */         for (int i = startIndex; i < endIndex; i++) {
/* 219 */           container.applyOperation(i);
/*     */         }
/*     */ 
/*     */         
/* 223 */         synchronized (result) {
/* 224 */           for (int i = startIndex; i < endIndex; i++) {
/* 225 */             container.copyOut(i, result);
/*     */           }
/*     */         } 
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected CompletableFuture<Map<K, V>> scheduleFinalOperation(CompletableFuture<?> allTasksDone, ParallelMapTransform.Container<K, U, V> container) {
/* 234 */     Map<K, V> result = this.result;
/* 235 */     return allTasksDone.thenApply(ignored -> result);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\thread\ParallelMapTransform$BatchedTaskSplitter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */