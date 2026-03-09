/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import java.util.HashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SingleTaskSplitter<K, U, V>
/*     */   extends ParallelMapTransform.SplitterBase<K, U, V>
/*     */ {
/* 153 */   private SingleTaskSplitter(BiFunction<K, U, V> operation, int size) { super(operation, size, size); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   protected int batchSize(int index) { return 1; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected CompletableFuture<?> scheduleBatch(ParallelMapTransform.Container<K, U, V> container, int startIndex, int endIndex, Executor executor) {
/* 163 */     assert startIndex + 1 == endIndex;
/* 164 */     return CompletableFuture.runAsync(() -> container.applyOperation(startIndex), executor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected CompletableFuture<Map<K, V>> scheduleFinalOperation(CompletableFuture<?> allTasksDone, ParallelMapTransform.Container<K, U, V> container) {
/* 169 */     return allTasksDone.thenApply(ignored -> {
/*     */           
/* 171 */           Map<K, V> result = new HashMap<K, V>(container.size());
/* 172 */           for (int i = 0; i < container.size(); i++) {
/* 173 */             container.copyOut(i, result);
/*     */           }
/* 175 */           return result;
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\thread\ParallelMapTransform$SingleTaskSplitter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */