/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParallelMapTransform
/*     */ {
/*     */   private static final int DEFAULT_TASKS_PER_THREAD = 16;
/*     */   
/*     */   public static <K, U, V> CompletableFuture<Map<K, V>> schedule(Map<K, U> input, BiFunction<K, U, V> operation, int maxTaskCount, Executor executor) {
/*  23 */     int inputSize = input.size();
/*     */     
/*  25 */     if (inputSize == 0) {
/*  26 */       return CompletableFuture.completedFuture(Map.of());
/*     */     }
/*     */     
/*  29 */     if (inputSize == 1) {
/*  30 */       Map.Entry<K, U> element = (Map.Entry)input.entrySet().iterator().next();
/*  31 */       K key = (K)element.getKey();
/*  32 */       U value = (U)element.getValue();
/*  33 */       return CompletableFuture.supplyAsync(() -> {
/*  34 */             V result = (V)operation.apply(key, value);
/*  35 */             return (result != null) ? Map.of(key, result) : Map.of();
/*     */           }executor);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  41 */     SplitterBase<K, U, V> splitter = (inputSize <= maxTaskCount) ? new SingleTaskSplitter(operation, inputSize) : new BatchedTaskSplitter(operation, inputSize, maxTaskCount);
/*     */     
/*  43 */     return splitter.scheduleTasks(input, executor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, U, V> CompletableFuture<Map<K, V>> schedule(Map<K, U> input, BiFunction<K, U, V> operation, Executor executor) {
/*  50 */     int maxTaskCount = Util.maxAllowedExecutorThreads() * 16;
/*  51 */     return schedule(input, operation, maxTaskCount, executor);
/*     */   }
/*     */   private static final class Container<K, U, V> extends Record { private final BiFunction<K, U, V> operation; private final Object[] keys; private final Object[] values;
/*  54 */     private Container(BiFunction<K, U, V> operation, Object[] keys, Object[] values) { this.operation = operation; this.keys = keys; this.values = values; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/thread/ParallelMapTransform$Container;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #54	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  54 */       //   0	7	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container<TK;TU;TV;>; } public BiFunction<K, U, V> operation() { return this.operation; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/thread/ParallelMapTransform$Container;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #54	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container<TK;TU;TV;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/thread/ParallelMapTransform$Container;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #54	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  54 */       //   0	8	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container<TK;TU;TV;>; } public Object[] keys() { return this.keys; } public Object[] values() { return this.values; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  62 */     public Container(BiFunction<K, U, V> operation, int size) { this(operation, new Object[size], new Object[size]); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void put(int index, K key, U input) {
/*  70 */       this.keys[index] = key;
/*  71 */       this.values[index] = input;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  76 */     private K key(int index) { return (K)this.keys[index]; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     private V output(int index) { return (V)this.values[index]; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     private U input(int index) { return (U)this.values[index]; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     public void applyOperation(int index) { this.values[index] = this.operation.apply(key(index), input(index)); }
/*     */ 
/*     */     
/*     */     public void copyOut(int index, Map<K, V> output) {
/*  95 */       V value = (V)output(index);
/*  96 */       if (value != null) {
/*  97 */         K key = (K)key(index);
/*  98 */         output.put(key, value);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 103 */     public int size() { return this.keys.length; } }
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class SplitterBase<K, U, V>
/*     */     extends Object
/*     */   {
/*     */     private int lastScheduledIndex;
/*     */     private int currentIndex;
/*     */     private final CompletableFuture<?>[] tasks;
/*     */     private int batchIndex;
/*     */     private final ParallelMapTransform.Container<K, U, V> container;
/*     */     
/*     */     private SplitterBase(BiFunction<K, U, V> operation, int size, int taskCount) {
/* 117 */       this.container = new ParallelMapTransform.Container(operation, size);
/* 118 */       this.tasks = new CompletableFuture[taskCount];
/*     */     }
/*     */ 
/*     */     
/* 122 */     private int pendingBatchSize() { return this.currentIndex - this.lastScheduledIndex; }
/*     */ 
/*     */     
/*     */     public CompletableFuture<Map<K, V>> scheduleTasks(Map<K, U> input, Executor executor) {
/* 126 */       input.forEach((key, inputValue) -> {
/* 127 */             this.container.put(this.currentIndex++, key, inputValue);
/*     */             
/* 129 */             if (pendingBatchSize() == batchSize(this.batchIndex)) {
/* 130 */               this.tasks[this.batchIndex++] = scheduleBatch(this.container, this.lastScheduledIndex, this.currentIndex, executor);
/* 131 */               this.lastScheduledIndex = this.currentIndex;
/*     */             } 
/*     */           });
/* 134 */       assert this.currentIndex == this.container.size();
/* 135 */       assert this.lastScheduledIndex == this.currentIndex;
/* 136 */       assert this.batchIndex == this.tasks.length;
/*     */       
/* 138 */       return scheduleFinalOperation(CompletableFuture.allOf(this.tasks), this.container);
/*     */     }
/*     */ 
/*     */     
/*     */     protected abstract int batchSize(int param1Int);
/*     */ 
/*     */     
/*     */     protected abstract CompletableFuture<?> scheduleBatch(ParallelMapTransform.Container<K, U, V> param1Container, int param1Int1, int param1Int2, Executor param1Executor);
/*     */     
/*     */     protected abstract CompletableFuture<Map<K, V>> scheduleFinalOperation(CompletableFuture<?> param1CompletableFuture, ParallelMapTransform.Container<K, U, V> param1Container);
/*     */   }
/*     */   
/*     */   private static class SingleTaskSplitter<K, U, V>
/*     */     extends SplitterBase<K, U, V>
/*     */   {
/* 153 */     private SingleTaskSplitter(BiFunction<K, U, V> operation, int size) { super(operation, size, size); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     protected int batchSize(int index) { return 1; }
/*     */ 
/*     */ 
/*     */     
/*     */     protected CompletableFuture<?> scheduleBatch(ParallelMapTransform.Container<K, U, V> container, int startIndex, int endIndex, Executor executor) {
/* 163 */       assert startIndex + 1 == endIndex;
/* 164 */       return CompletableFuture.runAsync(() -> container.applyOperation(startIndex), executor);
/*     */     }
/*     */ 
/*     */     
/*     */     protected CompletableFuture<Map<K, V>> scheduleFinalOperation(CompletableFuture<?> allTasksDone, ParallelMapTransform.Container<K, U, V> container) {
/* 169 */       return allTasksDone.thenApply(ignored -> {
/*     */             
/* 171 */             Map<K, V> result = new HashMap<K, V>(container.size());
/* 172 */             for (int i = 0; i < container.size(); i++) {
/* 173 */               container.copyOut(i, result);
/*     */             }
/* 175 */             return result;
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class BatchedTaskSplitter<K, U, V>
/*     */     extends SplitterBase<K, U, V>
/*     */   {
/*     */     private final Map<K, V> result;
/*     */     
/*     */     private final int batchSize;
/*     */     private final int firstUndersizedBatchIndex;
/*     */     
/*     */     private BatchedTaskSplitter(BiFunction<K, U, V> operation, int size, int maxTasks) {
/* 190 */       super(operation, size, maxTasks);
/* 191 */       this.result = new HashMap(size);
/* 192 */       this.batchSize = Mth.positiveCeilDiv(size, maxTasks);
/*     */       
/* 194 */       int fullCapacity = this.batchSize * maxTasks;
/* 195 */       int leftoverCapacity = fullCapacity - size;
/*     */ 
/*     */       
/* 198 */       this.firstUndersizedBatchIndex = maxTasks - leftoverCapacity;
/* 199 */       assert this.firstUndersizedBatchIndex > 0 && this.firstUndersizedBatchIndex <= maxTasks;
/*     */     }
/*     */ 
/*     */     
/*     */     protected CompletableFuture<?> scheduleBatch(ParallelMapTransform.Container<K, U, V> container, int startIndex, int endIndex, Executor executor) {
/* 204 */       int batchSize = endIndex - startIndex;
/*     */       
/* 206 */       assert batchSize == this.batchSize || batchSize == this.batchSize - 1;
/* 207 */       return CompletableFuture.runAsync(createTask(this.result, startIndex, endIndex, container), executor);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 212 */     protected int batchSize(int index) { return (index < this.firstUndersizedBatchIndex) ? this.batchSize : (this.batchSize - 1); }
/*     */ 
/*     */ 
/*     */     
/*     */     private static <K, U, V> Runnable createTask(Map<K, V> result, int startIndex, int endIndex, ParallelMapTransform.Container<K, U, V> container) {
/* 217 */       return () -> {
/* 218 */           for (int i = startIndex; i < endIndex; i++) {
/* 219 */             container.applyOperation(i);
/*     */           }
/*     */ 
/*     */           
/* 223 */           synchronized (result) {
/* 224 */             for (int i = startIndex; i < endIndex; i++) {
/* 225 */               container.copyOut(i, result);
/*     */             }
/*     */           } 
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected CompletableFuture<Map<K, V>> scheduleFinalOperation(CompletableFuture<?> allTasksDone, ParallelMapTransform.Container<K, U, V> container) {
/* 234 */       Map<K, V> result = this.result;
/* 235 */       return allTasksDone.thenApply(ignored -> result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\thread\ParallelMapTransform.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */