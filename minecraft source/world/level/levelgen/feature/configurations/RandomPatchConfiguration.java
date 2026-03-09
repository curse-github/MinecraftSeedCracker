/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public final class RandomPatchConfiguration extends Record implements FeatureConfiguration {
/*    */   private final int tries;
/*    */   private final int xzSpread;
/*    */   
/*  9 */   public RandomPatchConfiguration(int tries, int xzSpread, int ySpread, Holder<PlacedFeature> feature) { this.tries = tries; this.xzSpread = xzSpread; this.ySpread = ySpread; this.feature = feature; } private final int ySpread; private final Holder<PlacedFeature> feature; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public int tries() { return this.tries; } public int xzSpread() { return this.xzSpread; } public int ySpread() { return this.ySpread; } public Holder<PlacedFeature> feature() { return this.feature; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   public static final Codec<RandomPatchConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.POSITIVE_INT
/* 16 */         .fieldOf("tries").orElse(Integer.valueOf(128)).forGetter(RandomPatchConfiguration::tries), ExtraCodecs.NON_NEGATIVE_INT
/* 17 */         .fieldOf("xz_spread").orElse(Integer.valueOf(7)).forGetter(RandomPatchConfiguration::xzSpread), ExtraCodecs.NON_NEGATIVE_INT
/* 18 */         .fieldOf("y_spread").orElse(Integer.valueOf(3)).forGetter(RandomPatchConfiguration::ySpread), PlacedFeature.CODEC
/* 19 */         .fieldOf("feature").forGetter(RandomPatchConfiguration::feature))
/* 20 */       .apply(i, RandomPatchConfiguration::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\RandomPatchConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */