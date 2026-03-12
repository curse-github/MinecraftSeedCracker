/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.util.profiling.metrics.MetricsRegistry;
/*    */ 
/*    */ public class PriorityConsecutiveExecutor
/*    */   extends AbstractConsecutiveExecutor<StrictQueue.RunnableWithPriority> {
/*    */   public PriorityConsecutiveExecutor(int priorityCount, Executor executor, String name) {
/* 11 */     super(new StrictQueue.FixedPriorityQueue(priorityCount), executor, name);
/* 12 */     MetricsRegistry.INSTANCE.add(this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public StrictQueue.RunnableWithPriority wrapRunnable(Runnable runnable) { return new StrictQueue.RunnableWithPriority(0, runnable); }
/*    */ 
/*    */   
/*    */   public <Source> CompletableFuture<Source> scheduleWithResult(int priority, Consumer<CompletableFuture<Source>> futureConsumer) {
/* 21 */     CompletableFuture<Source> future = new CompletableFuture<Source>();
/* 22 */     schedule(new StrictQueue.RunnableWithPriority(priority, () -> futureConsumer.accept(future)));
/* 23 */     return future;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\thread\PriorityConsecutiveExecutor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */