/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ public final class TwistingVinesConfig extends Record implements FeatureConfiguration {
/*    */   private final int spreadWidth;
/*    */   private final int spreadHeight;
/*    */   private final int maxHeight;
/*    */   
/*  7 */   public TwistingVinesConfig(int spreadWidth, int spreadHeight, int maxHeight) { this.spreadWidth = spreadWidth; this.spreadHeight = spreadHeight; this.maxHeight = maxHeight; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/TwistingVinesConfig;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/TwistingVinesConfig; } public int spreadWidth() { return this.spreadWidth; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/TwistingVinesConfig;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/TwistingVinesConfig; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/TwistingVinesConfig;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/TwistingVinesConfig;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public int spreadHeight() { return this.spreadHeight; } public int maxHeight() { return this.maxHeight; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 12 */   public static final Codec<TwistingVinesConfig> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.POSITIVE_INT
/* 13 */         .fieldOf("spread_width").forGetter(TwistingVinesConfig::spreadWidth), ExtraCodecs.POSITIVE_INT
/* 14 */         .fieldOf("spread_height").forGetter(TwistingVinesConfig::spreadHeight), ExtraCodecs.POSITIVE_INT
/* 15 */         .fieldOf("max_height").forGetter(TwistingVinesConfig::maxHeight))
/* 16 */       .apply(i, TwistingVinesConfig::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\TwistingVinesConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */