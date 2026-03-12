/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import com.google.common.collect.Queues;
/*    */ import java.util.Locale;
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ 
/*    */ public interface StrictQueue<T extends Runnable>
/*    */ {
/*    */   Runnable pop();
/*    */   
/*    */   boolean push(T paramT);
/*    */   
/*    */   boolean isEmpty();
/*    */   
/*    */   int size();
/*    */   
/*    */   public static final class QueueStrictQueue
/*    */     extends Object
/*    */     implements StrictQueue<Runnable> {
/*    */     private final Queue<Runnable> queue;
/*    */     
/* 23 */     public QueueStrictQueue(Queue<Runnable> queue) { this.queue = queue; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 28 */     public Runnable pop() { return (Runnable)this.queue.poll(); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 33 */     public boolean push(Runnable t) { return this.queue.add(t); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 38 */     public boolean isEmpty() { return this.queue.isEmpty(); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 43 */     public int size() { return this.queue.size(); } }
/*    */   public static final class RunnableWithPriority extends Record implements Runnable { private final int priority;
/*    */     private final Runnable task;
/*    */     
/* 47 */     public RunnableWithPriority(int priority, Runnable task) { this.priority = priority; this.task = task; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #47	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 47 */       //   0	7	0	this	Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority; } public int priority() { return this.priority; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #47	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #47	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority;
/* 47 */       //   0	8	1	o	Ljava/lang/Object; } public Runnable task() { return this.task; }
/*    */ 
/*    */     
/* 50 */     public void run() { this.task.run(); } }
/*    */   
/*    */   public static final class FixedPriorityQueue extends Object implements StrictQueue<RunnableWithPriority> { private final Queue<Runnable>[] queues;
/*    */     private final AtomicInteger size;
/*    */     
/*    */     public FixedPriorityQueue(int size) {
/* 56 */       this.size = new AtomicInteger();
/*    */ 
/*    */ 
/*    */       
/* 60 */       this.queues = new Queue[size];
/* 61 */       for (int i = 0; i < size; i++) {
/* 62 */         this.queues[i] = Queues.newConcurrentLinkedQueue();
/*    */       }
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public Runnable pop() {
/* 69 */       for (Queue<Runnable> queue : this.queues) {
/* 70 */         Runnable task = (Runnable)queue.poll();
/* 71 */         if (task != null) {
/* 72 */           this.size.decrementAndGet();
/* 73 */           return task;
/*    */         } 
/*    */       } 
/* 76 */       return null;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean push(StrictQueue.RunnableWithPriority task) {
/* 81 */       int priority = task.priority;
/*    */       
/* 83 */       if (priority >= this.queues.length || priority < 0) {
/* 84 */         throw new IndexOutOfBoundsException(String.format(Locale.ROOT, "Priority %d not supported. Expected range [0-%d]", new Object[] { Integer.valueOf(priority), Integer.valueOf(this.queues.length - 1) }));
/*    */       }
/*    */       
/* 87 */       this.queues[priority].add(task);
/* 88 */       this.size.incrementAndGet();
/* 89 */       return true;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 94 */     public boolean isEmpty() { return (this.size.get() == 0); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 99 */     public int size() { return this.size.get(); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\thread\StrictQueue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */