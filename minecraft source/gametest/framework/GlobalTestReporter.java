/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ public class GlobalTestReporter {
/*  4 */   private static TestReporter DELEGATE = new LogTestReporter();
/*    */ 
/*    */   
/*  7 */   public static void replaceWith(TestReporter testReporter) { DELEGATE = testReporter; }
/*    */ 
/*    */ 
/*    */   
/* 11 */   public static void onTestFailed(GameTestInfo testInfo) { DELEGATE.onTestFailed(testInfo); }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public static void onTestSuccess(GameTestInfo testInfo) { DELEGATE.onTestSuccess(testInfo); }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static void finish() { DELEGATE.finish(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GlobalTestReporter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */