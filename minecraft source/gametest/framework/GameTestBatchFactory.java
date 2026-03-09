/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Streams;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GameTestBatchFactory
/*    */ {
/*    */   private static final int MAX_TESTS_PER_BATCH = 50;
/* 24 */   public static final TestDecorator DIRECT = (test, level) -> Stream.of(new GameTestInfo(test, Rotation.NONE, level, RetryOptions.noRetries()));
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<GameTestBatch> divideIntoBatches(Collection<Holder.Reference<GameTestInstance>> allTests, TestDecorator decorator, ServerLevel level) {
/* 29 */     Map<Holder<TestEnvironmentDefinition>, List<GameTestInfo>> testsPerBatch = (Map)allTests.stream().flatMap(test -> decorator.decorate(test, level)).collect(Collectors.groupingBy(info -> info.getTest().batch()));
/*    */     
/* 31 */     return testsPerBatch.entrySet().stream().flatMap(e -> {
/* 32 */           Holder<TestEnvironmentDefinition> batchKey = (Holder)e.getKey();
/* 33 */           List<GameTestInfo> testsInBatch = (List)e.getValue();
/* 34 */           return Streams.mapWithIndex(
/* 35 */               Lists.partition(testsInBatch, 50).stream(), ());
/*    */ 
/*    */         
/* 38 */         }).toList();
/*    */   }
/*    */ 
/*    */   
/* 42 */   public static GameTestRunner.GameTestBatcher fromGameTestInfo() { return fromGameTestInfo(50); }
/*    */ 
/*    */   
/*    */   public static GameTestRunner.GameTestBatcher fromGameTestInfo(int maxTestsPerBatch) {
/* 46 */     return gameTestInfos -> {
/* 47 */         Map<Holder<TestEnvironmentDefinition>, List<GameTestInfo>> testFunctionsPerBatch = (Map)gameTestInfos.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(()));
/*    */         
/* 49 */         return testFunctionsPerBatch.entrySet().stream().flatMap(())
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 56 */           .toList();
/*    */       };
/*    */   }
/*    */ 
/*    */   
/* 61 */   public static GameTestBatch toGameTestBatch(Collection<GameTestInfo> tests, Holder<TestEnvironmentDefinition> batch, int counter) { return new GameTestBatch(counter, tests, batch); }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface TestDecorator {
/*    */     Stream<GameTestInfo> decorate(Holder.Reference<GameTestInstance> param1Reference, ServerLevel param1ServerLevel);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestBatchFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */