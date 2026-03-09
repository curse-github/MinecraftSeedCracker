/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BiFunction;
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
/*     */ abstract class SplitterBase<K, U, V>
/*     */   extends Object
/*     */ {
/*     */   private int lastScheduledIndex;
/*     */   private int currentIndex;
/*     */   private final CompletableFuture<?>[] tasks;
/*     */   private int batchIndex;
/*     */   private final ParallelMapTransform.Container<K, U, V> container;
/*     */   
/*     */   private SplitterBase(BiFunction<K, U, V> operation, int size, int taskCount) {
/* 117 */     this.container = new ParallelMapTransform.Container(operation, size);
/* 118 */     this.tasks = new CompletableFuture[taskCount];
/*     */   }
/*     */ 
/*     */   
/* 122 */   private int pendingBatchSize() { return this.currentIndex - this.lastScheduledIndex; }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Map<K, V>> scheduleTasks(Map<K, U> input, Executor executor) {
/* 126 */     input.forEach((key, inputValue) -> {
/* 127 */           this.container.put(this.currentIndex++, key, inputValue);
/*     */           
/* 129 */           if (pendingBatchSize() == batchSize(this.batchIndex)) {
/* 130 */             this.tasks[this.batchIndex++] = scheduleBatch(this.container, this.lastScheduledIndex, this.currentIndex, executor);
/* 131 */             this.lastScheduledIndex = this.currentIndex;
/*     */           } 
/*     */         });
/* 134 */     assert this.currentIndex == this.container.size();
/* 135 */     assert this.lastScheduledIndex == this.currentIndex;
/* 136 */     assert this.batchIndex == this.tasks.length;
/*     */     
/* 138 */     return scheduleFinalOperation(CompletableFuture.allOf(this.tasks), this.container);
/*     */   }
/*     */   
/*     */   protected abstract int batchSize(int paramInt);
/*     */   
/*     */   protected abstract CompletableFuture<?> scheduleBatch(ParallelMapTransform.Container<K, U, V> paramContainer, int paramInt1, int paramInt2, Executor paramExecutor);
/*     */   
/*     */   protected abstract CompletableFuture<Map<K, V>> scheduleFinalOperation(CompletableFuture<?> paramCompletableFuture, ParallelMapTransform.Container<K, U, V> paramContainer);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\thread\ParallelMapTransform$SplitterBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */