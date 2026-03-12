/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.function.Consumer;
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
/*    */   implements GameTestListener
/*    */ {
/*    */   null(MultipleTestTracker this$0) {}
/*    */   
/*    */   public void testStructureLoaded(GameTestInfo testInfo) {}
/*    */   
/*    */   public void testPassed(GameTestInfo testInfo, GameTestRunner runner) {}
/*    */   
/* 50 */   public void testFailed(GameTestInfo testInfo, GameTestRunner runner) { listener.accept(testInfo); }
/*    */   
/*    */   public void testAddedForRerun(GameTestInfo original, GameTestInfo copy, GameTestRunner runner) {}
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\MultipleTestTracker$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */