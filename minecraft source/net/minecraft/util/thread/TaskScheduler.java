/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface TaskScheduler<R extends Runnable>
/*    */   extends AutoCloseable
/*    */ {
/*    */   default void close() {}
/*    */   
/*    */   default <Source> CompletableFuture<Source> scheduleWithResult(Consumer<CompletableFuture<Source>> futureConsumer) {
/* 20 */     CompletableFuture<Source> future = new CompletableFuture<Source>();
/* 21 */     schedule(wrapRunnable(() -> futureConsumer.accept(future)));
/* 22 */     return future;
/*    */   }
/*    */   
/*    */   static TaskScheduler<Runnable> wrapExecutor(final String name, final Executor executor) {
/* 26 */     return new TaskScheduler<Runnable>()
/*    */       {
/*    */         public String name() {
/* 29 */           return name;
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 34 */         public void schedule(Runnable runnable) { executor.execute(runnable); }
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 39 */         public Runnable wrapRunnable(Runnable runnable) { return runnable; }
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 44 */         public String toString() { return name; }
/*    */       };
/*    */   }
/*    */   
/*    */   String name();
/*    */   
/*    */   void schedule(R paramR);
/*    */   
/*    */   R wrapRunnable(Runnable paramRunnable);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\thread\TaskScheduler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */