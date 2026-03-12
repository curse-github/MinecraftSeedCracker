/*    */ package net.minecraft.world.entity.variant;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class SpawnPrioritySelectors extends Record {
/*    */   private final List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors;
/*    */   
/*  7 */   public SpawnPrioritySelectors(List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors) { this.selectors = selectors; } public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() { return this.selectors; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/SpawnPrioritySelectors;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/SpawnPrioritySelectors; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/SpawnPrioritySelectors;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/SpawnPrioritySelectors; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/SpawnPrioritySelectors;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/SpawnPrioritySelectors;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 10 */   public static final SpawnPrioritySelectors EMPTY = new SpawnPrioritySelectors(List.of());
/*    */ 
/*    */   
/* 13 */   public static SpawnPrioritySelectors single(SpawnCondition condition, int priority) { return new SpawnPrioritySelectors(PriorityProvider.single(condition, priority)); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static SpawnPrioritySelectors fallback(int priority) { return new SpawnPrioritySelectors(PriorityProvider.alwaysTrue(priority)); }
/*    */ 
/*    */   
/* 20 */   public static final Codec<SpawnPrioritySelectors> CODEC = PriorityProvider.Selector.codec(SpawnCondition.CODEC)
/* 21 */     .listOf()
/* 22 */     .xmap(SpawnPrioritySelectors::new, SpawnPrioritySelectors::selectors);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\variant\SpawnPrioritySelectors.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */