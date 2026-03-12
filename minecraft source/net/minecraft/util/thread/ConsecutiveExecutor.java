/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentLinkedQueue;
/*    */ import java.util.concurrent.Executor;
/*    */ 
/*    */ public class ConsecutiveExecutor
/*    */   extends AbstractConsecutiveExecutor<Runnable>
/*    */ {
/*  9 */   public ConsecutiveExecutor(Executor dispatcher, String name) { super(new StrictQueue.QueueStrictQueue(new ConcurrentLinkedQueue()), dispatcher, name); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   public Runnable wrapRunnable(Runnable runnable) { return runnable; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\thread\ConsecutiveExecutor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */