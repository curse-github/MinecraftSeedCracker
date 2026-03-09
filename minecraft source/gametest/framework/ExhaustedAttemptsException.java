/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ 
/*    */ class ExhaustedAttemptsException
/*    */   extends Throwable
/*    */ {
/*    */   public ExhaustedAttemptsException(int attempts, int successes, GameTestInfo testInfo) {
/*  8 */     super("Not enough successes: " + successes + " out of " + attempts + " attempts. Required successes: " + testInfo
/*    */         
/* 10 */         .requiredSuccesses() + ". max attempts: " + testInfo.maxAttempts() + ".", testInfo.getError());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\ExhaustedAttemptsException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */