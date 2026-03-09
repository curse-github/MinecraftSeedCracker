/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import java.util.concurrent.Executor;
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
/*    */ class null
/*    */   extends Object
/*    */   implements TaskScheduler<Runnable>
/*    */ {
/* 29 */   public String name() { return name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public void schedule(Runnable runnable) { executor.execute(runnable); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public Runnable wrapRunnable(Runnable runnable) { return runnable; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public String toString() { return name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\thread\TaskScheduler$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */