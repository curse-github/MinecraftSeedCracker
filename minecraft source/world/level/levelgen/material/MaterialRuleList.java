/*    */ package net.minecraft.world.level.levelgen.material;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.NoiseChunk;
/*    */ 
/*    */ public final class MaterialRuleList extends Record implements NoiseChunk.BlockStateFiller {
/*    */   private final NoiseChunk.BlockStateFiller[] materialRuleList;
/*    */   
/*  8 */   public MaterialRuleList(BlockStateFiller[] materialRuleList) { this.materialRuleList = materialRuleList; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/material/MaterialRuleList;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/material/MaterialRuleList; } public NoiseChunk.BlockStateFiller[] materialRuleList() { return this.materialRuleList; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/material/MaterialRuleList;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/material/MaterialRuleList; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/material/MaterialRuleList;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/material/MaterialRuleList;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   public BlockState calculate(DensityFunction.FunctionContext context) {
/* 11 */     for (NoiseChunk.BlockStateFiller rule : this.materialRuleList) {
/* 12 */       BlockState state = rule.calculate(context);
/* 13 */       if (state != null) {
/* 14 */         return state;
/*    */       }
/*    */     } 
/* 17 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\material\MaterialRuleList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */