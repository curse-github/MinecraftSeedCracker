/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collection;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultipleTestTracker
/*     */ {
/*     */   private static final char NOT_STARTED_TEST_CHAR = ' ';
/*     */   private static final char ONGOING_TEST_CHAR = '_';
/*     */   private static final char SUCCESSFUL_TEST_CHAR = '+';
/*     */   private static final char FAILED_OPTIONAL_TEST_CHAR = 'x';
/*     */   private static final char FAILED_REQUIRED_TEST_CHAR = 'X';
/*  18 */   private final Collection<GameTestInfo> tests = Lists.newArrayList();
/*  19 */   private final Collection<GameTestListener> listeners = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  25 */   public MultipleTestTracker(Collection<GameTestInfo> tests) { this.tests.addAll(tests); }
/*     */ 
/*     */   
/*     */   public void addTestToTrack(GameTestInfo testInfo) {
/*  29 */     this.tests.add(testInfo);
/*  30 */     Objects.requireNonNull(testInfo); this.listeners.forEach(testInfo::addListener);
/*     */   }
/*     */   
/*     */   public void addListener(GameTestListener listener) {
/*  34 */     this.listeners.add(listener);
/*  35 */     this.tests.forEach(testInfo -> testInfo.addListener(listener));
/*     */   }
/*     */   
/*     */   public void addFailureListener(final Consumer<GameTestInfo> listener) {
/*  39 */     addListener(new GameTestListener(this)
/*     */         {
/*     */           public void testStructureLoaded(GameTestInfo testInfo) {}
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void testPassed(GameTestInfo testInfo, GameTestRunner runner) {}
/*     */ 
/*     */ 
/*     */           
/*  50 */           public void testFailed(GameTestInfo testInfo, GameTestRunner runner) { listener.accept(testInfo); }
/*     */ 
/*     */ 
/*     */           
/*     */           public void testAddedForRerun(GameTestInfo original, GameTestInfo copy, GameTestRunner runner) {}
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  60 */   public int getFailedRequiredCount() { return (int)this.tests.stream().filter(GameTestInfo::hasFailed).filter(GameTestInfo::isRequired).count(); }
/*     */ 
/*     */ 
/*     */   
/*  64 */   public int getFailedOptionalCount() { return (int)this.tests.stream().filter(GameTestInfo::hasFailed).filter(GameTestInfo::isOptional).count(); }
/*     */ 
/*     */ 
/*     */   
/*  68 */   public int getDoneCount() { return (int)this.tests.stream().filter(GameTestInfo::isDone).count(); }
/*     */ 
/*     */ 
/*     */   
/*  72 */   public boolean hasFailedRequired() { return (getFailedRequiredCount() > 0); }
/*     */ 
/*     */ 
/*     */   
/*  76 */   public boolean hasFailedOptional() { return (getFailedOptionalCount() > 0); }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public Collection<GameTestInfo> getFailedRequired() { return (Collection)this.tests.stream().filter(GameTestInfo::hasFailed).filter(GameTestInfo::isRequired).collect(Collectors.toList()); }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public Collection<GameTestInfo> getFailedOptional() { return (Collection)this.tests.stream().filter(GameTestInfo::hasFailed).filter(GameTestInfo::isOptional).collect(Collectors.toList()); }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public int getTotalCount() { return this.tests.size(); }
/*     */ 
/*     */ 
/*     */   
/*  92 */   public boolean isDone() { return (getDoneCount() == getTotalCount()); }
/*     */ 
/*     */   
/*     */   public String getProgressBar() {
/*  96 */     StringBuffer buf = new StringBuffer();
/*  97 */     buf.append('[');
/*  98 */     this.tests.forEach(test -> {
/*  99 */           if (!test.hasStarted()) {
/* 100 */             buf.append(' ');
/* 101 */           } else if (test.hasSucceeded()) {
/* 102 */             buf.append('+');
/* 103 */           } else if (test.hasFailed()) {
/* 104 */             buf.append(test.isRequired() ? 88 : 120);
/*     */           } else {
/* 106 */             buf.append('_');
/*     */           } 
/*     */         });
/* 109 */     buf.append(']');
/* 110 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public String toString() { return getProgressBar(); }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public void remove(GameTestInfo testInfo) { this.tests.remove(testInfo); }
/*     */   
/*     */   public MultipleTestTracker() {}
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\MultipleTestTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */