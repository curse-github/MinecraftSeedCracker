/*     */ package net.minecraft.world.level.biome;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.util.Graph;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public class FeatureSorter {
/*     */   public static final class StepFeatureData extends Record {
/*     */     private final List<PlacedFeature> features;
/*     */     private final ToIntFunction<PlacedFeature> indexMapping;
/*     */     
/*  28 */     public ToIntFunction<PlacedFeature> indexMapping() { return this.indexMapping; } public List<PlacedFeature> features() { return this.features; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData;
/*  28 */       //   0	8	1	o	Ljava/lang/Object; } public StepFeatureData(List<PlacedFeature> features, ToIntFunction<PlacedFeature> indexMapping) { this.features = features; this.indexMapping = indexMapping; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData; }
/*     */     
/*     */     public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData; }
/*     */     
/*  33 */     private StepFeatureData(List<PlacedFeature> features) { this(features, Util.createIndexIdentityLookup(features)); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> List<StepFeatureData> buildFeaturesPerStep(List<T> featureSources, Function<T, List<HolderSet<PlacedFeature>>> featureGetter, boolean tryReducingError) {
/*  39 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/*  40 */     MutableInt nextFeatureIndex = new MutableInt(0);
/*     */     static final class FeatureData extends Record { private final int featureIndex; private final int step; private final PlacedFeature feature;
/*  42 */       FeatureData(int featureIndex, int step, PlacedFeature feature) { this.featureIndex = featureIndex; this.step = step; this.feature = feature; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/FeatureSorter$1FeatureData;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #42	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*  42 */         //   0	7	0	this	Lnet/minecraft/world/level/biome/FeatureSorter$1FeatureData; } public int featureIndex() { return this.featureIndex; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/FeatureSorter$1FeatureData;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #42	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/level/biome/FeatureSorter$1FeatureData; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/FeatureSorter$1FeatureData;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #42	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/world/level/biome/FeatureSorter$1FeatureData;
/*  42 */         //   0	8	1	o	Ljava/lang/Object; } public int step() { return this.step; } public PlacedFeature feature() { return this.feature; } };
/*  43 */     Comparator<FeatureData> featureDataComparator = Comparator.comparingInt(FeatureData::step).thenComparingInt(FeatureData::featureIndex);
/*     */     
/*  45 */     Map<FeatureData, Set<FeatureData>> edges = new TreeMap<FeatureData, Set<FeatureData>>(featureDataComparator);
/*     */     
/*  47 */     int maxStep = 0;
/*  48 */     for (T featureSource : featureSources) {
/*  49 */       List<FeatureData> featureList = Lists.newArrayList();
/*  50 */       List<HolderSet<PlacedFeature>> featuresForStep = (List)featureGetter.apply(featureSource);
/*  51 */       maxStep = Math.max(maxStep, featuresForStep.size());
/*  52 */       for (int i = 0; i < featuresForStep.size(); i++) {
/*  53 */         for (Holder<PlacedFeature> featureSupplier : (HolderSet)featuresForStep.get(i)) {
/*  54 */           PlacedFeature feature = (PlacedFeature)featureSupplier.value();
/*  55 */           featureList.add(new FeatureData(object2IntOpenHashMap.computeIfAbsent(feature, f -> nextFeatureIndex.getAndIncrement()), i, feature));
/*     */         } 
/*     */       } 
/*  58 */       for (int i = 0; i < featureList.size(); i++) {
/*  59 */         Set<FeatureData> data = (Set)edges.computeIfAbsent((FeatureData)featureList.get(i), k -> new TreeSet(featureDataComparator));
/*  60 */         if (i < featureList.size() - 1) {
/*  61 */           data.add((FeatureData)featureList.get(i + 1));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  66 */     Set<FeatureData> discovered = new TreeSet<FeatureData>(featureDataComparator);
/*  67 */     Set<FeatureData> currentlyVisiting = new TreeSet<FeatureData>(featureDataComparator);
/*  68 */     List<FeatureData> sortedFeatures = Lists.newArrayList();
/*     */     
/*  70 */     for (FeatureData feature : edges.keySet()) {
/*  71 */       if (!currentlyVisiting.isEmpty()) {
/*  72 */         throw new IllegalStateException("You somehow broke the universe; DFS bork (iteration finished with non-empty in-progress vertex set");
/*     */       }
/*  74 */       if (discovered.contains(feature)) {
/*     */         continue;
/*     */       }
/*     */       
/*  78 */       Objects.requireNonNull(sortedFeatures); if (Graph.depthFirstSearch(edges, discovered, currentlyVisiting, sortedFeatures::add, feature)) {
/*  79 */         if (tryReducingError) {
/*  80 */           int lastSize; List<T> reducedSources = new ArrayList<T>(featureSources);
/*     */ 
/*     */           
/*     */           do {
/*  84 */             lastSize = reducedSources.size();
/*  85 */             ListIterator<T> iterator = reducedSources.listIterator();
/*  86 */             while (iterator.hasNext()) {
/*  87 */               T source = (T)iterator.next();
/*  88 */               iterator.remove();
/*     */               try {
/*  90 */                 buildFeaturesPerStep(reducedSources, featureGetter, false);
/*  91 */               } catch (IllegalStateException e) {
/*     */                 continue;
/*     */               } 
/*     */               
/*  95 */               iterator.add(source);
/*     */             } 
/*  97 */           } while (lastSize != reducedSources.size());
/*     */           
/*  99 */           throw new IllegalStateException("Feature order cycle found, involved sources: " + String.valueOf(reducedSources));
/*     */         } 
/*     */         
/* 102 */         throw new IllegalStateException("Feature order cycle found");
/*     */       } 
/*     */     } 
/*     */     
/* 106 */     Collections.reverse(sortedFeatures);
/*     */     
/* 108 */     ImmutableList.Builder<StepFeatureData> features = ImmutableList.builder();
/* 109 */     for (int step = 0; step < maxStep; step++) {
/* 110 */       int finalStep = step;
/* 111 */       List<PlacedFeature> featuresInStep = (List)sortedFeatures.stream().filter(p -> (p.step() == finalStep)).map(FeatureData::feature).collect(Collectors.toList());
/* 112 */       features.add(new StepFeatureData(featuresInStep));
/*     */     } 
/* 114 */     return features.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\FeatureSorter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */