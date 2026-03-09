/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import org.apache.commons.lang3.mutable.MutableInt;
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
/*    */ final class LevelData
/*    */   extends Record
/*    */ {
/*    */   private final Object2IntMap<FeatureCountTracker.FeatureData> featureData;
/*    */   private final MutableInt chunksWithFeatures;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$LevelData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$LevelData; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$LevelData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$LevelData; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$LevelData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$LevelData;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 27 */   private LevelData(Object2IntMap<FeatureCountTracker.FeatureData> featureData, MutableInt chunksWithFeatures) { this.featureData = featureData; this.chunksWithFeatures = chunksWithFeatures; } public Object2IntMap<FeatureCountTracker.FeatureData> featureData() { return this.featureData; } public MutableInt chunksWithFeatures() { return this.chunksWithFeatures; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\FeatureCountTracker$LevelData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */