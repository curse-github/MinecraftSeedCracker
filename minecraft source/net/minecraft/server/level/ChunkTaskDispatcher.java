/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.IntConsumer;
/*     */ import java.util.function.IntSupplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.thread.PriorityConsecutiveExecutor;
/*     */ import net.minecraft.util.thread.StrictQueue;
/*     */ import net.minecraft.util.thread.TaskScheduler;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ChunkTaskDispatcher
/*     */   implements ChunkHolder.LevelChangeListener, AutoCloseable
/*     */ {
/*     */   public static final int DISPATCHER_PRIORITY_COUNT = 4;
/*  20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private final ChunkTaskPriorityQueue queue;
/*     */   private final TaskScheduler<Runnable> executor;
/*     */   private final PriorityConsecutiveExecutor dispatcher;
/*     */   protected boolean sleeping;
/*     */   
/*     */   public ChunkTaskDispatcher(TaskScheduler<Runnable> executor, Executor dispatcherExecutor) {
/*  27 */     this.queue = new ChunkTaskPriorityQueue(executor.name() + "_queue");
/*  28 */     this.executor = executor;
/*  29 */     this.dispatcher = new PriorityConsecutiveExecutor(4, dispatcherExecutor, "dispatcher");
/*  30 */     this.sleeping = true;
/*     */   }
/*     */ 
/*     */   
/*  34 */   public boolean hasWork() { return (this.dispatcher.hasWork() || this.queue.hasWork()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onLevelChange(ChunkPos pos, IntSupplier oldLevel, int newLevel, IntConsumer setQueueLevel) {
/*  39 */     this.dispatcher.schedule(new StrictQueue.RunnableWithPriority(0, () -> {
/*  40 */             int oldTicketLevel = oldLevel.getAsInt();
/*  41 */             if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS) {
/*  42 */               LOGGER.debug("RES {} {} -> {}", new Object[] { pos, Integer.valueOf(oldTicketLevel), Integer.valueOf(newLevel) });
/*     */             }
/*  44 */             this.queue.resortChunkTasks(oldTicketLevel, pos, newLevel);
/*  45 */             setQueueLevel.accept(newLevel);
/*     */           }));
/*     */   }
/*     */   
/*     */   public void release(long pos, Runnable whenReleased, boolean clearQueue) {
/*  50 */     this.dispatcher.schedule(new StrictQueue.RunnableWithPriority(1, () -> {
/*  51 */             this.queue.release(pos, clearQueue);
/*  52 */             onRelease(pos);
/*  53 */             if (this.sleeping) {
/*  54 */               this.sleeping = false;
/*  55 */               pollTask();
/*     */             } 
/*  57 */             whenReleased.run();
/*     */           }));
/*     */   }
/*     */   
/*     */   public void submit(Runnable task, long pos, IntSupplier level) {
/*  62 */     this.dispatcher.schedule(new StrictQueue.RunnableWithPriority(2, () -> {
/*  63 */             int ticketLevel = level.getAsInt();
/*  64 */             if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS) {
/*  65 */               LOGGER.debug("SUB {} {} {} {}", new Object[] { new ChunkPos(pos), Integer.valueOf(ticketLevel), this.executor, this.queue });
/*     */             }
/*  67 */             this.queue.submit(task, pos, ticketLevel);
/*  68 */             if (this.sleeping) {
/*  69 */               this.sleeping = false;
/*  70 */               pollTask();
/*     */             } 
/*     */           }));
/*     */   }
/*     */   
/*     */   protected void pollTask() {
/*  76 */     this.dispatcher.schedule(new StrictQueue.RunnableWithPriority(3, () -> {
/*  77 */             ChunkTaskPriorityQueue.TasksForChunk tasksForChunk = popTasks();
/*  78 */             if (tasksForChunk == null) {
/*  79 */               this.sleeping = true;
/*     */             } else {
/*  81 */               scheduleForExecution(tasksForChunk);
/*     */             } 
/*     */           }));
/*     */   }
/*     */ 
/*     */   
/*  87 */   protected void scheduleForExecution(ChunkTaskPriorityQueue.TasksForChunk tasksForChunk) { CompletableFuture.allOf((CompletableFuture[])tasksForChunk.tasks().stream().map(message -> this.executor.scheduleWithResult(()))
/*     */ 
/*     */         
/*  90 */         .toArray(x$0 -> new CompletableFuture[x$0])).thenAccept(r -> pollTask()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onRelease(long key) {}
/*     */ 
/*     */   
/*  97 */   protected ChunkTaskPriorityQueue.TasksForChunk popTasks() { return this.queue.pop(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   public void close() { this.executor.close(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ChunkTaskDispatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */