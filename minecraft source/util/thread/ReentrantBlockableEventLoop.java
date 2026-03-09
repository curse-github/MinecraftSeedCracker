/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ public abstract class ReentrantBlockableEventLoop<R extends Runnable>
/*    */   extends BlockableEventLoop<R> {
/*    */   private int reentrantCount;
/*    */   
/*  7 */   public ReentrantBlockableEventLoop(String name) { super(name); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 12 */   protected boolean scheduleExecutables() { return (runningTask() || super.scheduleExecutables()); }
/*    */ 
/*    */ 
/*    */   
/* 16 */   protected boolean runningTask() { return (this.reentrantCount != 0); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doRunTask(R task) {
/* 21 */     this.reentrantCount++;
/*    */     try {
/* 23 */       super.doRunTask(task);
/*    */     } finally {
/* 25 */       this.reentrantCount--;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\thread\ReentrantBlockableEventLoop.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */