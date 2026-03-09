/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class LogTestReporter implements TestReporter {
/*  8 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */   
/*    */   public void onTestFailed(GameTestInfo testInfo) {
/* 12 */     String testPosition = testInfo.getTestBlockPos().toShortString();
/* 13 */     if (testInfo.isRequired()) {
/* 14 */       LOGGER.error("{} failed at {}! {}", new Object[] { testInfo.id(), testPosition, Util.describeError(testInfo.getError()) });
/*    */     } else {
/* 16 */       LOGGER.warn("(optional) {} failed at {}. {}", new Object[] { testInfo.id(), testPosition, Util.describeError(testInfo.getError()) });
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onTestSuccess(GameTestInfo testInfo) {}
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\LogTestReporter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */