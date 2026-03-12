/*    */ package net.minecraft.server;
/*    */ 
/*    */ public final class RunningOnDifferentThreadException extends RuntimeException {
/*  4 */   public static final RunningOnDifferentThreadException RUNNING_ON_DIFFERENT_THREAD = new RunningOnDifferentThreadException();
/*    */ 
/*    */   
/*  7 */   private RunningOnDifferentThreadException() { setStackTrace(new StackTraceElement[0]); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Throwable fillInStackTrace() {
/* 12 */     setStackTrace(new StackTraceElement[0]);
/* 13 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\RunningOnDifferentThreadException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */