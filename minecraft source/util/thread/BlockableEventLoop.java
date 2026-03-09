/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Queues;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import com.mojang.jtracy.Zone;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.locks.LockSupport;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*     */ import net.minecraft.util.profiling.metrics.MetricsRegistry;
/*     */ import net.minecraft.util.profiling.metrics.ProfilerMeasured;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class BlockableEventLoop<R extends Runnable>
/*     */   extends Object
/*     */   implements Executor, TaskScheduler<R>, ProfilerMeasured {
/*     */   public static final long BLOCK_TIME_NANOS = 100000L;
/*     */   private final String name;
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger(); private final Queue<R> pendingRunnables; private int blockingCount;
/*     */   protected BlockableEventLoop(String name) {
/*  31 */     this.pendingRunnables = Queues.newConcurrentLinkedQueue();
/*     */ 
/*     */ 
/*     */     
/*  35 */     this.name = name;
/*  36 */     MetricsRegistry.INSTANCE.add(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public boolean isSameThread() { return (Thread.currentThread() == getRunningThread()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   protected boolean scheduleExecutables() { return !isSameThread(); }
/*     */ 
/*     */ 
/*     */   
/*  52 */   public int getPendingTasksCount() { return this.pendingRunnables.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public String name() { return this.name; }
/*     */ 
/*     */   
/*     */   public <V> CompletableFuture<V> submit(Supplier<V> supplier) {
/*  61 */     if (scheduleExecutables()) {
/*  62 */       return CompletableFuture.supplyAsync(supplier, this);
/*     */     }
/*  64 */     return CompletableFuture.completedFuture(supplier.get());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  69 */   private CompletableFuture<Void> submitAsync(Runnable runnable) { return CompletableFuture.supplyAsync(() -> {
/*  70 */           runnable.run();
/*  71 */           return null;
/*     */         }this); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public CompletableFuture<Void> submit(Runnable runnable) {
/*  83 */     if (scheduleExecutables()) {
/*  84 */       return submitAsync(runnable);
/*     */     }
/*  86 */     runnable.run();
/*  87 */     return CompletableFuture.completedFuture(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void executeBlocking(Runnable runnable) {
/*  92 */     if (!isSameThread()) {
/*  93 */       submitAsync(runnable).join();
/*     */     } else {
/*  95 */       runnable.run();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void schedule(R r) {
/* 101 */     this.pendingRunnables.add(r);
/* 102 */     LockSupport.unpark(getRunningThread());
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(Runnable command) {
/* 107 */     R task = (R)wrapRunnable(command);
/* 108 */     if (scheduleExecutables()) {
/* 109 */       schedule(task);
/*     */     } else {
/* 111 */       doRunTask(task);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 116 */   public void executeIfPossible(Runnable command) { execute(command); }
/*     */ 
/*     */ 
/*     */   
/* 120 */   protected void dropAllTasks() { this.pendingRunnables.clear(); }
/*     */ 
/*     */   
/*     */   protected void runAllTasks() {
/* 124 */     while (pollTask());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   protected boolean shouldRunAllTasks() { return (this.blockingCount > 0); }
/*     */ 
/*     */   
/*     */   protected boolean pollTask() {
/* 134 */     R task = (R)(Runnable)this.pendingRunnables.peek();
/* 135 */     if (task == null) {
/* 136 */       return false;
/*     */     }
/*     */     
/* 139 */     if (!shouldRunAllTasks() && !shouldRun(task)) {
/* 140 */       return false;
/*     */     }
/*     */     
/* 143 */     doRunTask((Runnable)this.pendingRunnables.remove());
/*     */     
/* 145 */     return true;
/*     */   }
/*     */   
/*     */   public void managedBlock(BooleanSupplier condition) {
/* 149 */     this.blockingCount++;
/*     */     try {
/* 151 */       while (!condition.getAsBoolean()) {
/* 152 */         if (!pollTask())
/*     */         {
/* 154 */           waitForTasks();
/*     */         }
/*     */       } 
/*     */     } finally {
/* 158 */       this.blockingCount--;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void waitForTasks() {
/* 163 */     Thread.yield();
/* 164 */     LockSupport.parkNanos("waiting for tasks", 100000L);
/*     */   }
/*     */   protected void doRunTask(R task) {
/*     */     
/* 168 */     try { Zone ignored = TracyClient.beginZone("Task", SharedConstants.IS_RUNNING_IN_IDE); 
/* 169 */       try { task.run();
/* 170 */         if (ignored != null) ignored.close();  } catch (Throwable throwable) { if (ignored != null) try { ignored.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/* 171 */     { LOGGER.error(LogUtils.FATAL_MARKER, "Error executing task on {}", name(), e);
/* 172 */       if (isNonRecoverable(e)) {
/* 173 */         throw e;
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 180 */   public List<MetricSampler> profiledMetrics() { return ImmutableList.of(
/* 181 */         MetricSampler.create(this.name + "-pending-tasks", MetricCategory.EVENT_LOOPS, this::getPendingTasksCount)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNonRecoverable(Throwable t) {
/* 186 */     if (t instanceof ReportedException) { ReportedException r = (ReportedException)t;
/* 187 */       return isNonRecoverable(r.getCause()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 192 */     return (t instanceof OutOfMemoryError || t instanceof StackOverflowError);
/*     */   }
/*     */   
/*     */   protected abstract boolean shouldRun(R paramR);
/*     */   
/*     */   protected abstract Thread getRunningThread();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\thread\BlockableEventLoop.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */