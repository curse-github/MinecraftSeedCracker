/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ public final class SculkPatchConfiguration extends Record implements FeatureConfiguration {
/*    */   private final int chargeCount;
/*    */   private final int amountPerCharge;
/*    */   private final int spreadAttempts;
/*    */   
/*  7 */   public SculkPatchConfiguration(int chargeCount, int amountPerCharge, int spreadAttempts, int growthRounds, int spreadRounds, IntProvider extraRareGrowths, float catalystChance) { this.chargeCount = chargeCount; this.amountPerCharge = amountPerCharge; this.spreadAttempts = spreadAttempts; this.growthRounds = growthRounds; this.spreadRounds = spreadRounds; this.extraRareGrowths = extraRareGrowths; this.catalystChance = catalystChance; } private final int growthRounds; private final int spreadRounds; private final IntProvider extraRareGrowths; private final float catalystChance; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public int chargeCount() { return this.chargeCount; } public int amountPerCharge() { return this.amountPerCharge; } public int spreadAttempts() { return this.spreadAttempts; } public int growthRounds() { return this.growthRounds; } public int spreadRounds() { return this.spreadRounds; } public IntProvider extraRareGrowths() { return this.extraRareGrowths; } public float catalystChance() { return this.catalystChance; }
/*  8 */   public static final Codec<SculkPatchConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(
/*  9 */         Codec.intRange(1, 32).fieldOf("charge_count").forGetter(SculkPatchConfiguration::chargeCount), 
/* 10 */         Codec.intRange(1, 500).fieldOf("amount_per_charge").forGetter(SculkPatchConfiguration::amountPerCharge), 
/* 11 */         Codec.intRange(1, 64).fieldOf("spread_attempts").forGetter(SculkPatchConfiguration::spreadAttempts), 
/* 12 */         Codec.intRange(0, 8).fieldOf("growth_rounds").forGetter(SculkPatchConfiguration::growthRounds), 
/* 13 */         Codec.intRange(0, 8).fieldOf("spread_rounds").forGetter(SculkPatchConfiguration::spreadRounds), IntProvider.CODEC
/* 14 */         .fieldOf("extra_rare_growths").forGetter(SculkPatchConfiguration::extraRareGrowths), 
/* 15 */         Codec.floatRange(0.0F, 1.0F).fieldOf("catalyst_chance").forGetter(SculkPatchConfiguration::catalystChance))
/* 16 */       .apply(i, SculkPatchConfiguration::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\SculkPatchConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */