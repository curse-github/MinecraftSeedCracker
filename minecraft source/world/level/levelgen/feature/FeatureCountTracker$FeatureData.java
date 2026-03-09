/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class FeatureData
/*    */   extends Record
/*    */ {
/*    */   private final ConfiguredFeature<?, ?> feature;
/*    */   private final Optional<PlacedFeature> topFeature;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$FeatureData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$FeatureData; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$FeatureData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$FeatureData; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$FeatureData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$FeatureData;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 25 */   private FeatureData(ConfiguredFeature<?, ?> feature, Optional<PlacedFeature> topFeature) { this.feature = feature; this.topFeature = topFeature; } public ConfiguredFeature<?, ?> feature() { return this.feature; } public Optional<PlacedFeature> topFeature() { return this.topFeature; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\FeatureCountTracker$FeatureData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */