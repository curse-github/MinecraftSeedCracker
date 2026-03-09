/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class StepFeatureData
/*    */   extends Record
/*    */ {
/*    */   private final List<PlacedFeature> features;
/*    */   private final ToIntFunction<PlacedFeature> indexMapping;
/*    */   
/* 28 */   public ToIntFunction<PlacedFeature> indexMapping() { return this.indexMapping; } public List<PlacedFeature> features() { return this.features; } public StepFeatureData(List<PlacedFeature> features, ToIntFunction<PlacedFeature> indexMapping) { this.features = features; this.indexMapping = indexMapping; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData; }
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData; }
/*    */   
/* 33 */   private StepFeatureData(List<PlacedFeature> features) { this(features, Util.createIndexIdentityLookup(features)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\FeatureSorter$StepFeatureData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */