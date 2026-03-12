/*    */ package net.minecraft.server.rcon.thread;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import net.minecraft.DefaultUncaughtExceptionHandlerWithName;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public abstract class GenericThread
/*    */   implements Runnable
/*    */ {
/* 11 */   private static final Logger LOGGER = LogUtils.getLogger();
/* 12 */   private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);
/*    */   
/*    */   private static final int MAX_STOP_WAIT = 5;
/*    */   
/*    */   protected final String name;
/*    */   protected Thread thread;
/*    */   
/* 19 */   protected GenericThread(String name) { this.name = name; }
/*    */ 
/*    */   
/*    */   public boolean start() {
/* 23 */     if (this.running) {
/* 24 */       return true;
/*    */     }
/* 26 */     this.running = true;
/* 27 */     this.thread = new Thread(this, this.name + " #" + this.name);
/* 28 */     this.thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandlerWithName(LOGGER));
/* 29 */     this.thread.start();
/* 30 */     LOGGER.info("Thread {} started", this.name);
/* 31 */     return true;
/*    */   }
/*    */   
/*    */   public void stop() {
/* 35 */     this.running = false;
/* 36 */     if (null == this.thread) {
/*    */       return;
/*    */     }
/* 39 */     int waited = 0;
/* 40 */     while (this.thread.isAlive()) {
/*    */       
/*    */       try {
/* 43 */         this.thread.join(1000L);
/* 44 */         waited++;
/* 45 */         if (waited >= 5) {
/*    */ 
/*    */ 
/*    */           
/* 49 */           LOGGER.warn("Waited {} seconds attempting force stop!", Integer.valueOf(waited)); continue;
/* 50 */         }  if (this.thread.isAlive()) {
/* 51 */           LOGGER.warn("Thread {} ({}) failed to exit after {} second(s)", new Object[] { this, this.thread.getState(), Integer.valueOf(waited), new Exception("Stack:") });
/*    */           
/* 53 */           this.thread.interrupt();
/*    */         } 
/* 55 */       } catch (InterruptedException interruptedException) {}
/*    */     } 
/*    */ 
/*    */     
/* 59 */     LOGGER.info("Thread {} stopped", this.name);
/* 60 */     this.thread = null;
/*    */   }
/*    */ 
/*    */   
/* 64 */   public boolean isRunning() { return this.running; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\rcon\thread\GenericThread.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */