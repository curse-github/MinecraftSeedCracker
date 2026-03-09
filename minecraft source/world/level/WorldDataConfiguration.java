/*    */ package net.minecraft.world.level;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class WorldDataConfiguration extends Record {
/*    */   private final DataPackConfig dataPacks;
/*    */   private final FeatureFlagSet enabledFeatures;
/*    */   public static final String ENABLED_FEATURES_ID = "enabled_features";
/*    */   
/*  9 */   public WorldDataConfiguration(DataPackConfig dataPacks, FeatureFlagSet enabledFeatures) { this.dataPacks = dataPacks; this.enabledFeatures = enabledFeatures; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/WorldDataConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/world/level/WorldDataConfiguration; } public DataPackConfig dataPacks() { return this.dataPacks; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/WorldDataConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/WorldDataConfiguration; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/WorldDataConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/WorldDataConfiguration;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public FeatureFlagSet enabledFeatures() { return this.enabledFeatures; }
/*    */ 
/*    */   
/* 12 */   public static final MapCodec<WorldDataConfiguration> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(DataPackConfig.CODEC
/* 13 */         .lenientOptionalFieldOf("DataPacks", DataPackConfig.DEFAULT).forGetter(WorldDataConfiguration::dataPacks), FeatureFlags.CODEC
/* 14 */         .lenientOptionalFieldOf("enabled_features", FeatureFlags.DEFAULT_FLAGS).forGetter(WorldDataConfiguration::enabledFeatures))
/* 15 */       .apply(i, WorldDataConfiguration::new));
/* 16 */   public static final Codec<WorldDataConfiguration> CODEC = MAP_CODEC.codec();
/*    */   
/* 18 */   public static final WorldDataConfiguration DEFAULT = new WorldDataConfiguration(DataPackConfig.DEFAULT, FeatureFlags.DEFAULT_FLAGS);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public WorldDataConfiguration expandFeatures(FeatureFlagSet newEnabledFeatures) { return new WorldDataConfiguration(this.dataPacks, this.enabledFeatures.join(newEnabledFeatures)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\WorldDataConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */