/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
/*    */ 
/*    */ public final class DiskConfiguration extends Record implements FeatureConfiguration {
/*    */   private final RuleBasedBlockStateProvider stateProvider;
/*    */   private final BlockPredicate target;
/*    */   
/*  9 */   public DiskConfiguration(RuleBasedBlockStateProvider stateProvider, BlockPredicate target, IntProvider radius, int halfHeight) { this.stateProvider = stateProvider; this.target = target; this.radius = radius; this.halfHeight = halfHeight; } private final IntProvider radius; private final int halfHeight; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public RuleBasedBlockStateProvider stateProvider() { return this.stateProvider; } public BlockPredicate target() { return this.target; } public IntProvider radius() { return this.radius; } public int halfHeight() { return this.halfHeight; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   public static final Codec<DiskConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(RuleBasedBlockStateProvider.CODEC
/* 16 */         .fieldOf("state_provider").forGetter(DiskConfiguration::stateProvider), BlockPredicate.CODEC
/* 17 */         .fieldOf("target").forGetter(DiskConfiguration::target), 
/* 18 */         IntProvider.codec(0, 8).fieldOf("radius").forGetter(DiskConfiguration::radius), 
/* 19 */         Codec.intRange(0, 4).fieldOf("half_height").forGetter(DiskConfiguration::halfHeight))
/* 20 */       .apply(i, DiskConfiguration::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\DiskConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */