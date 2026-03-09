/*    */ package net.minecraft.server;
/*    */ 
/*    */ public class TickTask implements Runnable {
/*    */   private final int tick;
/*    */   private final Runnable runnable;
/*    */   
/*    */   public TickTask(int tick, Runnable runnable) {
/*  8 */     this.tick = tick;
/*  9 */     this.runnable = runnable;
/*    */   }
/*    */ 
/*    */   
/* 13 */   public int getTick() { return this.tick; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public void run() { this.runnable.run(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\TickTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */