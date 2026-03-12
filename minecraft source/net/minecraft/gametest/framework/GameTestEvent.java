/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ 
/*    */ class GameTestEvent
/*    */ {
/*    */   public final Long expectedDelay;
/*    */   public final Runnable assertion;
/*    */   
/*    */   private GameTestEvent(Long expectedDelay, Runnable assertion) {
/* 10 */     this.expectedDelay = expectedDelay;
/* 11 */     this.assertion = assertion;
/*    */   }
/*    */ 
/*    */   
/* 15 */   static GameTestEvent create(Runnable runnable) { return new GameTestEvent(null, runnable); }
/*    */ 
/*    */ 
/*    */   
/* 19 */   static GameTestEvent create(long expectedTick, Runnable runnable) { return new GameTestEvent(Long.valueOf(expectedTick), runnable); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */