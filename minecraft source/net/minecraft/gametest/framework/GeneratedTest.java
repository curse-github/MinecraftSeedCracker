/*    */ package net.minecraft.gametest.framework;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public final class GeneratedTest extends Record {
/*    */   private final Map<Identifier, TestData<ResourceKey<TestEnvironmentDefinition>>> tests;
/*    */   private final ResourceKey<Consumer<GameTestHelper>> functionKey;
/*    */   private final Consumer<GameTestHelper> function;
/*    */   
/* 10 */   public GeneratedTest(Map<Identifier, TestData<ResourceKey<TestEnvironmentDefinition>>> tests, ResourceKey<Consumer<GameTestHelper>> functionKey, Consumer<GameTestHelper> function) { this.tests = tests; this.functionKey = functionKey; this.function = function; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/GeneratedTest;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/gametest/framework/GeneratedTest; } public Map<Identifier, TestData<ResourceKey<TestEnvironmentDefinition>>> tests() { return this.tests; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/GeneratedTest;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/GeneratedTest; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/GeneratedTest;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gametest/framework/GeneratedTest;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<Consumer<GameTestHelper>> functionKey() { return this.functionKey; } public Consumer<GameTestHelper> function() { return this.function; }
/*    */   
/* 12 */   public GeneratedTest(Map<Identifier, TestData<ResourceKey<TestEnvironmentDefinition>>> tests, Identifier functionId, Consumer<GameTestHelper> function) { this(tests, ResourceKey.create(Registries.TEST_FUNCTION, functionId), function); }
/*    */ 
/*    */ 
/*    */   
/* 16 */   public GeneratedTest(Identifier id, TestData<ResourceKey<TestEnvironmentDefinition>> testData, Consumer<GameTestHelper> function) { this(Map.of(id, testData), id, function); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GeneratedTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */