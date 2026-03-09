/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import com.google.common.collect.Queues;
/*    */ import java.util.Locale;
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class FixedPriorityQueue
/*    */   extends Object
/*    */   implements StrictQueue<StrictQueue.RunnableWithPriority>
/*    */ {
/*    */   private final Queue<Runnable>[] queues;
/*    */   private final AtomicInteger size;
/*    */   
/*    */   public FixedPriorityQueue(int size) {
/* 56 */     this.size = new AtomicInteger();
/*    */ 
/*    */ 
/*    */     
/* 60 */     this.queues = new Queue[size];
/* 61 */     for (int i = 0; i < size; i++) {
/* 62 */       this.queues[i] = Queues.newConcurrentLinkedQueue();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Runnable pop() {
/* 69 */     for (Queue<Runnable> queue : this.queues) {
/* 70 */       Runnable task = (Runnable)queue.poll();
/* 71 */       if (task != null) {
/* 72 */         this.size.decrementAndGet();
/* 73 */         return task;
/*    */       } 
/*    */     } 
/* 76 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean push(StrictQueue.RunnableWithPriority task) {
/* 81 */     int priority = task.priority;
/*    */     
/* 83 */     if (priority >= this.queues.length || priority < 0) {
/* 84 */       throw new IndexOutOfBoundsException(String.format(Locale.ROOT, "Priority %d not supported. Expected range [0-%d]", new Object[] { Integer.valueOf(priority), Integer.valueOf(this.queues.length - 1) }));
/*    */     }
/*    */     
/* 87 */     this.queues[priority].add(task);
/* 88 */     this.size.incrementAndGet();
/* 89 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 94 */   public boolean isEmpty() { return (this.size.get() == 0); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 99 */   public int size() { return this.size.get(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\thread\StrictQueue$FixedPriorityQueue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */