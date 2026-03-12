/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import java.util.Locale;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*     */ import org.apache.commons.lang3.exception.ExceptionUtils;
/*     */ 
/*     */ 
/*     */ class ReportGameListener
/*     */   implements GameTestListener
/*     */ {
/*  21 */   private int attempts = 0;
/*  22 */   private int successes = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  27 */   public void testStructureLoaded(GameTestInfo testInfo) { this.attempts++; }
/*     */ 
/*     */   
/*     */   private void handleRetry(GameTestInfo testInfo, GameTestRunner runner, boolean passed) {
/*  31 */     RetryOptions retryOptions = testInfo.retryOptions();
/*  32 */     String reportAs = String.format(Locale.ROOT, "[Run: %4d, Ok: %4d, Fail: %4d", new Object[] { Integer.valueOf(this.attempts), Integer.valueOf(this.successes), Integer.valueOf(this.attempts - this.successes) });
/*  33 */     if (!retryOptions.unlimitedTries()) {
/*  34 */       reportAs = reportAs + reportAs;
/*     */     }
/*  36 */     reportAs = reportAs + "]";
/*  37 */     String namePart = String.valueOf(testInfo.id()) + " " + String.valueOf(testInfo.id()) + "! " + (passed ? "passed" : "failed") + "ms";
/*  38 */     String text = String.format(Locale.ROOT, "%-53s%s", new Object[] { reportAs, namePart });
/*  39 */     if (passed) {
/*  40 */       reportPassed(testInfo, text);
/*     */     } else {
/*  42 */       say(testInfo.getLevel(), ChatFormatting.RED, text);
/*     */     } 
/*     */     
/*  45 */     if (retryOptions.hasTriesLeft(this.attempts, this.successes)) {
/*  46 */       runner.rerunTest(testInfo);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void testPassed(GameTestInfo testInfo, GameTestRunner runner) {
/*  52 */     this.successes++;
/*  53 */     if (testInfo.retryOptions().hasRetries()) {
/*  54 */       handleRetry(testInfo, runner, true);
/*     */       return;
/*     */     } 
/*  57 */     if (!testInfo.isFlaky()) {
/*  58 */       reportPassed(testInfo, String.valueOf(testInfo.id()) + " passed! (" + String.valueOf(testInfo.id()) + "ms / " + testInfo.getRunTime() + "gameticks)");
/*     */       
/*     */       return;
/*     */     } 
/*  62 */     if (this.successes >= testInfo.requiredSuccesses()) {
/*  63 */       reportPassed(testInfo, String.valueOf(testInfo) + " passed " + String.valueOf(testInfo) + " times of " + this.successes + " attempts.");
/*     */     } else {
/*  65 */       say(testInfo.getLevel(), ChatFormatting.GREEN, "Flaky test " + String.valueOf(testInfo) + " succeeded, attempt: " + this.attempts + " successes: " + this.successes);
/*  66 */       runner.rerunTest(testInfo);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void testFailed(GameTestInfo testInfo, GameTestRunner runner) {
/*  72 */     if (!testInfo.isFlaky()) {
/*  73 */       reportFailure(testInfo, testInfo.getError());
/*  74 */       if (testInfo.retryOptions().hasRetries()) {
/*  75 */         handleRetry(testInfo, runner, false);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*  80 */     GameTestInstance testFunction = testInfo.getTest();
/*  81 */     String text = "Flaky test " + String.valueOf(testInfo) + " failed, attempt: " + this.attempts + "/" + testFunction.maxAttempts();
/*  82 */     if (testFunction.requiredSuccesses() > 1) {
/*  83 */       text = text + ", successes: " + text + " (" + this.successes + " required)";
/*     */     }
/*  85 */     say(testInfo.getLevel(), ChatFormatting.YELLOW, text);
/*  86 */     if (testInfo.maxAttempts() - this.attempts + this.successes >= testInfo.requiredSuccesses()) {
/*  87 */       runner.rerunTest(testInfo);
/*     */     } else {
/*  89 */       reportFailure(testInfo, new ExhaustedAttemptsException(this.attempts, this.successes, testInfo));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  95 */   public void testAddedForRerun(GameTestInfo original, GameTestInfo copy, GameTestRunner runner) { copy.addListener(this); }
/*     */ 
/*     */   
/*     */   public static void reportPassed(GameTestInfo testInfo, String text) {
/*  99 */     getTestInstanceBlockEntity(testInfo).ifPresent(blockEntity -> blockEntity.setSuccess());
/* 100 */     visualizePassedTest(testInfo, text);
/*     */   }
/*     */   
/*     */   private static void visualizePassedTest(GameTestInfo testInfo, String text) {
/* 104 */     say(testInfo.getLevel(), ChatFormatting.GREEN, text);
/*     */     
/* 106 */     GlobalTestReporter.onTestSuccess(testInfo);
/*     */   }
/*     */   
/*     */   protected static void reportFailure(GameTestInfo testInfo, Throwable error) {
/*     */     MutableComponent mutableComponent;
/* 111 */     if (error instanceof GameTestAssertException) { GameTestAssertException testException = (GameTestAssertException)error;
/* 112 */       mutableComponent = testException.getDescription(); }
/*     */     else
/* 114 */     { mutableComponent = Component.literal(Util.describeError(error)); }
/*     */     
/* 116 */     getTestInstanceBlockEntity(testInfo).ifPresent(blockEntity -> blockEntity.setErrorMessage(description));
/* 117 */     visualizeFailedTest(testInfo, error);
/*     */   }
/*     */   
/*     */   protected static void visualizeFailedTest(GameTestInfo testInfo, Throwable error) {
/* 121 */     String errorMessage = error.getMessage() + error.getMessage();
/* 122 */     String failureMessage = (testInfo.isRequired() ? "" : "(optional) ") + (testInfo.isRequired() ? "" : "(optional) ") + " failed! " + String.valueOf(testInfo.id());
/*     */     
/* 124 */     say(testInfo.getLevel(), testInfo.isRequired() ? ChatFormatting.RED : ChatFormatting.YELLOW, failureMessage);
/*     */     
/* 126 */     Throwable rootCause = (Throwable)MoreObjects.firstNonNull(ExceptionUtils.getRootCause(error), error);
/* 127 */     if (rootCause instanceof GameTestAssertPosException) { GameTestAssertPosException assertError = (GameTestAssertPosException)rootCause;
/* 128 */       testInfo.getTestInstanceBlockEntity().markError(assertError.getAbsolutePos(), assertError.getMessageToShowAtBlock()); }
/*     */ 
/*     */     
/* 131 */     GlobalTestReporter.onTestFailed(testInfo);
/*     */   }
/*     */   
/*     */   private static Optional<TestInstanceBlockEntity> getTestInstanceBlockEntity(GameTestInfo testInfo) {
/* 135 */     ServerLevel level = testInfo.getLevel();
/* 136 */     Optional<BlockPos> testPos = Optional.ofNullable(testInfo.getTestBlockPos());
/* 137 */     return testPos.flatMap(pos -> level.getBlockEntity(pos, BlockEntityType.TEST_INSTANCE_BLOCK));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 142 */   protected static void say(ServerLevel level, ChatFormatting format, String text) { level.getPlayers(player -> true).forEach(player -> player.sendSystemMessage(Component.literal(text).withStyle(format))); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\ReportGameListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */