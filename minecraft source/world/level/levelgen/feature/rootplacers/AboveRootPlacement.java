/*    */ package net.minecraft.world.level.levelgen.feature.rootplacers;
/*    */ 
/*    */ public final class AboveRootPlacement extends Record {
/*    */   private final BlockStateProvider aboveRootProvider;
/*    */   private final float aboveRootPlacementChance;
/*    */   
/*  7 */   public AboveRootPlacement(BlockStateProvider aboveRootProvider, float aboveRootPlacementChance) { this.aboveRootProvider = aboveRootProvider; this.aboveRootPlacementChance = aboveRootPlacementChance; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/rootplacers/AboveRootPlacement;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/rootplacers/AboveRootPlacement; } public BlockStateProvider aboveRootProvider() { return this.aboveRootProvider; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/rootplacers/AboveRootPlacement;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/rootplacers/AboveRootPlacement; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/rootplacers/AboveRootPlacement;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/rootplacers/AboveRootPlacement;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public float aboveRootPlacementChance() { return this.aboveRootPlacementChance; }
/*    */ 
/*    */ 
/*    */   
/* 11 */   public static final Codec<AboveRootPlacement> CODEC = RecordCodecBuilder.create(i -> i.group(BlockStateProvider.CODEC
/* 12 */         .fieldOf("above_root_provider").forGetter(()), 
/* 13 */         Codec.floatRange(0.0F, 1.0F).fieldOf("above_root_placement_chance").forGetter(()))
/* 14 */       .apply(i, AboveRootPlacement::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\rootplacers\AboveRootPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */