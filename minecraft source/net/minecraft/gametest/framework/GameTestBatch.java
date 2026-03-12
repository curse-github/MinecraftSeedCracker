/*    */ package net.minecraft.gametest.framework;
/*    */ import java.util.Collection;
/*    */ 
/*    */ public final class GameTestBatch extends Record {
/*    */   private final int index;
/*    */   
/*  7 */   public Holder<TestEnvironmentDefinition> environment() { return this.environment; } private final Collection<GameTestInfo> gameTestInfos; private final Holder<TestEnvironmentDefinition> environment; public Collection<GameTestInfo> gameTestInfos() { return this.gameTestInfos; } public int index() { return this.index; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/GameTestBatch;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gametest/framework/GameTestBatch;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   public GameTestBatch(int index, Collection<GameTestInfo> gameTestInfos, Holder<TestEnvironmentDefinition> environment) {
/*  9 */     if (gameTestInfos.isEmpty())
/* 10 */       throw new IllegalArgumentException("A GameTestBatch must include at least one GameTestInfo!"); 
/*    */     this.index = index;
/*    */     this.gameTestInfos = gameTestInfos;
/*    */     this.environment = environment;
/*    */   }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/GameTestBatch;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/GameTestBatch; }
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/GameTestBatch;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/GameTestBatch; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestBatch.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */