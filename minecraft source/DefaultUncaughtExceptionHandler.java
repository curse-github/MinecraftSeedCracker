/*    */ package net.minecraft;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DefaultUncaughtExceptionHandler
/*    */   implements Thread.UncaughtExceptionHandler {
/*    */   private final Logger logger;
/*    */   
/*  9 */   public DefaultUncaughtExceptionHandler(Logger logger) { this.logger = logger; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   public void uncaughtException(Thread t, Throwable e) { this.logger.error("Caught previously unhandled exception :", e); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\DefaultUncaughtExceptionHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */