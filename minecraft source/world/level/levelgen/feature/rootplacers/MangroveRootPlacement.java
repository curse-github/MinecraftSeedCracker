/*    */ package net.minecraft.world.level.levelgen.feature.rootplacers;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public final class MangroveRootPlacement extends Record {
/*    */   private final HolderSet<Block> canGrowThrough;
/*    */   private final HolderSet<Block> muddyRootsIn;
/*    */   private final BlockStateProvider muddyRootsProvider;
/*    */   
/* 11 */   public MangroveRootPlacement(HolderSet<Block> canGrowThrough, HolderSet<Block> muddyRootsIn, BlockStateProvider muddyRootsProvider, int maxRootWidth, int maxRootLength, float randomSkewChance) { this.canGrowThrough = canGrowThrough; this.muddyRootsIn = muddyRootsIn; this.muddyRootsProvider = muddyRootsProvider; this.maxRootWidth = maxRootWidth; this.maxRootLength = maxRootLength; this.randomSkewChance = randomSkewChance; } private final int maxRootWidth; private final int maxRootLength; private final float randomSkewChance; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public HolderSet<Block> canGrowThrough() { return this.canGrowThrough; } public HolderSet<Block> muddyRootsIn() { return this.muddyRootsIn; } public BlockStateProvider muddyRootsProvider() { return this.muddyRootsProvider; } public int maxRootWidth() { return this.maxRootWidth; } public int maxRootLength() { return this.maxRootLength; } public float randomSkewChance() { return this.randomSkewChance; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final Codec<MangroveRootPlacement> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 20 */         RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_grow_through").forGetter(()), 
/* 21 */         RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("muddy_roots_in").forGetter(()), BlockStateProvider.CODEC
/* 22 */         .fieldOf("muddy_roots_provider").forGetter(()), 
/* 23 */         Codec.intRange(1, 12).fieldOf("max_root_width").forGetter(()), 
/* 24 */         Codec.intRange(1, 64).fieldOf("max_root_length").forGetter(()), 
/* 25 */         Codec.floatRange(0.0F, 1.0F).fieldOf("random_skew_chance").forGetter(()))
/* 26 */       .apply(i, MangroveRootPlacement::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\rootplacers\MangroveRootPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */