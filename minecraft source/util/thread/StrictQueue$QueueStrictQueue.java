/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import java.util.Queue;
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
/*    */ public final class QueueStrictQueue
/*    */   extends Object
/*    */   implements StrictQueue<Runnable>
/*    */ {
/*    */   private final Queue<Runnable> queue;
/*    */   
/* 23 */   public QueueStrictQueue(Queue<Runnable> queue) { this.queue = queue; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public Runnable pop() { return (Runnable)this.queue.poll(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public boolean push(Runnable t) { return this.queue.add(t); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public boolean isEmpty() { return this.queue.isEmpty(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public int size() { return this.queue.size(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\thread\StrictQueue$QueueStrictQueue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */