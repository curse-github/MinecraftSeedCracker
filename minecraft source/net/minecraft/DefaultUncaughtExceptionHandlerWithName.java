/*    */ package net.minecraft;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DefaultUncaughtExceptionHandlerWithName
/*    */   implements Thread.UncaughtExceptionHandler {
/*    */   private final Logger logger;
/*    */   
/*  9 */   public DefaultUncaughtExceptionHandlerWithName(Logger logger) { this.logger = logger; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void uncaughtException(Thread t, Throwable e) {
/* 14 */     this.logger.error("Caught previously unhandled exception :");
/* 15 */     this.logger.error(t.getName(), e);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\DefaultUncaughtExceptionHandlerWithName.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */