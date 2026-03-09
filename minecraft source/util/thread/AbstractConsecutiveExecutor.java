/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*     */ import net.minecraft.util.profiling.metrics.MetricsRegistry;
/*     */ import net.minecraft.util.profiling.metrics.ProfilerMeasured;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class AbstractConsecutiveExecutor<T extends Runnable>
/*     */   extends Object implements Runnable, TaskScheduler<T>, ProfilerMeasured {
/*  18 */   private static final Logger LOGGER = LogUtils.getLogger(); private final AtomicReference<Status> status; private final StrictQueue<T> queue; public AbstractConsecutiveExecutor(StrictQueue<T> queue, Executor executor, String name) {
/*  19 */     this.status = new AtomicReference(Status.SLEEPING);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  25 */     this.executor = executor;
/*  26 */     this.queue = queue;
/*  27 */     this.name = name;
/*  28 */     MetricsRegistry.INSTANCE.add(this);
/*     */   }
/*     */   private final Executor executor; private final String name;
/*     */   
/*  32 */   private boolean canBeScheduled() { return (!isClosed() && !this.queue.isEmpty()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   public void close() { this.status.set(Status.CLOSED); }
/*     */ 
/*     */   
/*     */   private boolean pollTask() {
/*  41 */     if (!isRunning()) {
/*  42 */       return false;
/*     */     }
/*     */     
/*  45 */     Runnable runnable = this.queue.pop();
/*  46 */     if (runnable == null) {
/*  47 */       return false;
/*     */     }
/*     */     
/*  50 */     Util.runNamed(runnable, this.name);
/*     */     
/*  52 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/*  62 */       pollTask();
/*     */     } finally {
/*  64 */       setSleeping();
/*  65 */       registerForExecution();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void runAll() {
/*     */     try {
/*  76 */       while (pollTask());
/*     */     } finally {
/*     */       
/*  79 */       setSleeping();
/*  80 */       registerForExecution();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void schedule(T task) {
/*  86 */     this.queue.push(task);
/*  87 */     registerForExecution();
/*     */   }
/*     */   
/*     */   private void registerForExecution() {
/*  91 */     if (canBeScheduled() && 
/*  92 */       setRunning()) {
/*     */       try {
/*  94 */         this.executor.execute(this);
/*  95 */       } catch (RejectedExecutionException e) {
/*     */         
/*     */         try {
/*  98 */           this.executor.execute(this);
/*  99 */         } catch (RejectedExecutionException e2) {
/* 100 */           LOGGER.error("Could not schedule ConsecutiveExecutor", e2);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public int size() { return this.queue.size(); }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public boolean hasWork() { return (isRunning() && !this.queue.isEmpty()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   public String toString() { return this.name + " " + this.name + " " + String.valueOf(this.status.get()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public String name() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public List<MetricSampler> profiledMetrics() { return ImmutableList.of(
/* 128 */         MetricSampler.create(this.name + "-queue-size", MetricCategory.CONSECUTIVE_EXECUTORS, this::size)); }
/*     */ 
/*     */   
/*     */   private enum Status
/*     */   {
/* 133 */     SLEEPING,
/* 134 */     RUNNING,
/* 135 */     CLOSED;
/*     */   }
/*     */ 
/*     */   
/* 139 */   private boolean setRunning() { return this.status.compareAndSet(Status.SLEEPING, Status.RUNNING); }
/*     */ 
/*     */ 
/*     */   
/* 143 */   private void setSleeping() { this.status.compareAndSet(Status.RUNNING, Status.SLEEPING); }
/*     */ 
/*     */ 
/*     */   
/* 147 */   private boolean isRunning() { return (this.status.get() == Status.RUNNING); }
/*     */ 
/*     */ 
/*     */   
/* 151 */   private boolean isClosed() { return (this.status.get() == Status.CLOSED); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\thread\AbstractConsecutiveExecutor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */