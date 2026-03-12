/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.levelgen.DensityFunction;
/*     */ import net.minecraft.world.level.levelgen.DensityFunctions;
/*     */ 
/*     */ public class Climate
/*     */ {
/*     */   private static final boolean DEBUG_SLOW_BIOME_SEARCH = false;
/*     */   private static final float QUANTIZATION_FACTOR = 10000.0F;
/*     */   @VisibleForTesting
/*     */   protected static final int PARAMETER_COUNT = 7;
/*     */   
/*  35 */   public static TargetPoint target(float temperature, float humidity, float continentalness, float erosion, float depth, float weirdness) { return new TargetPoint(quantizeCoord(temperature), quantizeCoord(humidity), quantizeCoord(continentalness), quantizeCoord(erosion), quantizeCoord(depth), quantizeCoord(weirdness)); }
/*     */ 
/*     */ 
/*     */   
/*  39 */   public static ParameterPoint parameters(float temperature, float humidity, float continentalness, float erosion, float depth, float weirdness, float offset) { return new ParameterPoint(Parameter.point(temperature), Parameter.point(humidity), Parameter.point(continentalness), Parameter.point(erosion), Parameter.point(depth), Parameter.point(weirdness), quantizeCoord(offset)); }
/*     */ 
/*     */ 
/*     */   
/*  43 */   public static ParameterPoint parameters(Parameter temperature, Parameter humidity, Parameter continentalness, Parameter erosion, Parameter depth, Parameter weirdness, float offset) { return new ParameterPoint(temperature, humidity, continentalness, erosion, depth, weirdness, quantizeCoord(offset)); }
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static long quantizeCoord(float coord) { return (long)(coord * 10000.0F); }
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static float unquantizeCoord(long coord) { return (float)coord / 10000.0F; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final class RTree<T>
/*     */     extends Object
/*     */   {
/*     */     private static final int CHILDREN_PER_NODE = 6;
/*     */ 
/*     */     
/*     */     private final Node<T> root;
/*     */ 
/*     */     
/*     */     private final ThreadLocal<Leaf<T>> lastResult;
/*     */ 
/*     */ 
/*     */     
/*     */     private RTree(Node<T> root) {
/*  69 */       this.lastResult = new ThreadLocal();
/*     */ 
/*     */       
/*  72 */       this.root = root;
/*     */     }
/*     */     
/*     */     static abstract class Node<T>
/*     */       extends Object {
/*     */       protected final Climate.Parameter[] parameterSpace;
/*     */       
/*  79 */       protected Node(List<Climate.Parameter> parameterSpace) { this.parameterSpace = (Parameter[])parameterSpace.toArray(new Climate.Parameter[0]); }
/*     */ 
/*     */       
/*     */       protected abstract Climate.RTree.Leaf<T> search(long[] param2ArrayOfLong, Climate.RTree.Leaf<T> param2Leaf, Climate.DistanceMetric<T> param2DistanceMetric);
/*     */       
/*     */       protected long distance(long[] target) {
/*  85 */         long distance = 0L;
/*  86 */         for (int i = 0; i < 7; i++) {
/*  87 */           distance += Mth.square(this.parameterSpace[i].distance(target[i]));
/*     */         }
/*  89 */         return distance;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  94 */       public String toString() { return Arrays.toString(this.parameterSpace); }
/*     */     }
/*     */     
/*     */     private static final class Leaf<T>
/*     */       extends Node<T> {
/*     */       private final T value;
/*     */       
/*     */       private Leaf(Climate.ParameterPoint parameterPoint, T value) {
/* 102 */         super(parameterPoint.parameterSpace());
/* 103 */         this.value = value;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 108 */       protected Leaf<T> search(long[] target, Leaf<T> candidate, Climate.DistanceMetric<T> distanceMetric) { return this; }
/*     */     }
/*     */     
/*     */     private static final class SubTree<T>
/*     */       extends Node<T>
/*     */     {
/*     */       private final Climate.RTree.Node<T>[] children;
/*     */       
/* 116 */       protected SubTree(List<? extends Climate.RTree.Node<T>> children) { this(Climate.RTree.buildParameterSpace(children), children); }
/*     */ 
/*     */       
/*     */       protected SubTree(List<Climate.Parameter> parameterSpace, List<? extends Climate.RTree.Node<T>> children) {
/* 120 */         super(parameterSpace);
/* 121 */         this.children = (Node[])children.toArray(new Climate.RTree.Node[0]);
/*     */       }
/*     */ 
/*     */       
/*     */       protected Climate.RTree.Leaf<T> search(long[] target, Climate.RTree.Leaf<T> candidate, Climate.DistanceMetric<T> distanceMetric) {
/* 126 */         long minDistance = (candidate == null) ? Float.MAX_VALUE : distanceMetric.distance(candidate, target);
/* 127 */         Climate.RTree.Leaf<T> closestLeaf = candidate;
/*     */         
/* 129 */         for (Climate.RTree.Node<T> child : this.children) {
/* 130 */           long childDistance = distanceMetric.distance(child, target);
/* 131 */           if (minDistance > childDistance) {
/*     */             
/* 133 */             Climate.RTree.Leaf<T> leaf = child.search(target, closestLeaf, distanceMetric);
/* 134 */             long leafDistance = (child == leaf) ? childDistance : distanceMetric.distance(leaf, target);
/* 135 */             if (minDistance > leafDistance) {
/* 136 */               minDistance = leafDistance;
/* 137 */               closestLeaf = leaf;
/*     */             } 
/*     */           } 
/*     */         } 
/* 141 */         return closestLeaf;
/*     */       }
/*     */     }
/*     */     
/*     */     public static <T> RTree<T> create(List<Pair<Climate.ParameterPoint, T>> values) {
/* 146 */       if (values.isEmpty()) {
/* 147 */         throw new IllegalArgumentException("Need at least one value to build the search tree.");
/*     */       }
/* 149 */       int dimensions = ((Climate.ParameterPoint)((Pair)values.get(0)).getFirst()).parameterSpace().size();
/* 150 */       if (dimensions != 7) {
/* 151 */         throw new IllegalStateException("Expecting parameter space to be 7, got " + dimensions);
/*     */       }
/*     */       
/* 154 */       List<Leaf<T>> leaves = (List)values.stream().map(p -> new Leaf((Climate.ParameterPoint)p.getFirst(), p.getSecond())).collect(Collectors.toCollection(java.util.ArrayList::new));
/*     */       
/* 156 */       return new RTree(build(dimensions, leaves));
/*     */     }
/*     */     
/*     */     private static <T> Node<T> build(int dimensions, List<? extends Node<T>> children) {
/* 160 */       if (children.isEmpty()) {
/* 161 */         throw new IllegalStateException("Need at least one child to build a node");
/*     */       }
/* 163 */       if (children.size() == 1) {
/* 164 */         return (Node)children.get(0);
/*     */       }
/* 166 */       if (children.size() <= 6) {
/* 167 */         children.sort(Comparator.comparingLong(leaf -> {
/* 168 */                 long totalMagnitude = 0L;
/* 169 */                 for (int d = 0; d < dimensions; d++) {
/* 170 */                   Climate.Parameter parameter = leaf.parameterSpace[d];
/* 171 */                   totalMagnitude += Math.abs((parameter.min() + parameter.max()) / 2L);
/*     */                 } 
/* 173 */                 return totalMagnitude;
/*     */               }));
/* 175 */         return new SubTree(children);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 182 */       long minCost = Float.MAX_VALUE;
/* 183 */       int minDimension = -1;
/* 184 */       List<SubTree<T>> minBuckets = null;
/*     */       
/* 186 */       for (int d = 0; d < dimensions; d++) {
/* 187 */         sort(children, dimensions, d, false);
/* 188 */         List<SubTree<T>> buckets = bucketize(children);
/*     */         
/* 190 */         long totalCost = 0L;
/* 191 */         for (SubTree<T> bucket : buckets) {
/* 192 */           totalCost += cost(bucket.parameterSpace);
/*     */         }
/*     */         
/* 195 */         if (minCost > totalCost) {
/* 196 */           minCost = totalCost;
/* 197 */           minDimension = d;
/* 198 */           minBuckets = buckets;
/*     */         } 
/*     */       } 
/*     */       
/* 202 */       sort(minBuckets, dimensions, minDimension, true);
/*     */       
/* 204 */       return new SubTree((List)minBuckets.stream().map(b -> build(dimensions, Arrays.asList(b.children))).collect(Collectors.toList()));
/*     */     }
/*     */     
/*     */     private static <T> void sort(List<? extends Node<T>> children, int dimensions, int dimension, boolean absolute) {
/* 208 */       Comparator<Node<T>> comparator = comparator(dimension, absolute);
/* 209 */       for (int d = 1; d < dimensions; d++) {
/* 210 */         comparator = comparator.thenComparing(comparator((dimension + d) % dimensions, absolute));
/*     */       }
/* 212 */       children.sort(comparator);
/*     */     }
/*     */     
/*     */     private static <T> Comparator<Node<T>> comparator(int dimension, boolean absolute) {
/* 216 */       return Comparator.comparingLong(leaf -> {
/* 217 */             Climate.Parameter parameter = leaf.parameterSpace[dimension];
/* 218 */             long center = (parameter.min() + parameter.max()) / 2L;
/* 219 */             return absolute ? Math.abs(center) : center;
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static <T> List<SubTree<T>> bucketize(List<? extends Node<T>> nodes) {
/* 227 */       List<SubTree<T>> buckets = Lists.newArrayList();
/*     */       
/* 229 */       List<Node<T>> children = Lists.newArrayList();
/* 230 */       int expectedChildrenCount = (int)Math.pow(6.0D, Math.floor(Math.log(nodes.size() - 0.01D) / Math.log(6.0D)));
/* 231 */       for (Node<T> child : nodes) {
/* 232 */         children.add(child);
/* 233 */         if (children.size() >= expectedChildrenCount) {
/* 234 */           buckets.add(new SubTree(children));
/* 235 */           children = Lists.newArrayList();
/*     */         } 
/*     */       } 
/* 238 */       if (!children.isEmpty()) {
/* 239 */         buckets.add(new SubTree(children));
/*     */       }
/* 241 */       return buckets;
/*     */     }
/*     */     
/*     */     private static long cost(Parameter[] parameterSpace) {
/* 245 */       long result = 0L;
/* 246 */       for (Climate.Parameter parameter : parameterSpace) {
/* 247 */         result += Math.abs(parameter.max() - parameter.min());
/*     */       }
/* 249 */       return result;
/*     */     }
/*     */     
/*     */     private static <T> List<Climate.Parameter> buildParameterSpace(List<? extends Node<T>> children) {
/* 253 */       if (children.isEmpty()) {
/* 254 */         throw new IllegalArgumentException("SubTree needs at least one child");
/*     */       }
/* 256 */       int dimensions = 7;
/* 257 */       List<Climate.Parameter> bounds = Lists.newArrayList();
/* 258 */       for (int d = 0; d < 7; d++) {
/* 259 */         bounds.add(null);
/*     */       }
/* 261 */       for (Node<T> child : children) {
/* 262 */         for (int d = 0; d < 7; d++) {
/* 263 */           bounds.set(d, child.parameterSpace[d].span((Climate.Parameter)bounds.get(d)));
/*     */         }
/*     */       } 
/* 266 */       return bounds;
/*     */     }
/*     */     
/*     */     public T search(Climate.TargetPoint target, Climate.DistanceMetric<T> distanceMetric) {
/* 270 */       long[] targetArray = target.toParameterArray();
/* 271 */       Leaf<T> leaf = this.root.search(targetArray, (Leaf)this.lastResult.get(), distanceMetric);
/* 272 */       this.lastResult.set(leaf);
/* 273 */       return (T)leaf.value;
/*     */     } } static abstract class Node<T> extends Object {
/*     */     protected final Climate.Parameter[] parameterSpace; protected Node(List<Climate.Parameter> parameterSpace) { this.parameterSpace = (Parameter[])parameterSpace.toArray(new Climate.Parameter[0]); } protected abstract Climate.RTree.Leaf<T> search(long[] param1ArrayOfLong, Climate.RTree.Leaf<T> param1Leaf, Climate.DistanceMetric<T> param1DistanceMetric); protected long distance(long[] target) { long distance = 0L; for (int i = 0; i < 7; i++)
/*     */         distance += Mth.square(this.parameterSpace[i].distance(target[i]));  return distance; } public String toString() { return Arrays.toString(this.parameterSpace); }
/*     */   } private static final class Leaf<T> extends RTree.Node<T> {
/*     */     private final T value; private Leaf(Climate.ParameterPoint parameterPoint, T value) { super(parameterPoint.parameterSpace()); this.value = value; } protected Leaf<T> search(long[] target, Leaf<T> candidate, Climate.DistanceMetric<T> distanceMetric) { return this; }
/*     */   } private static final class SubTree<T> extends RTree.Node<T> {
/*     */     private final Climate.RTree.Node<T>[] children; protected SubTree(List<? extends Climate.RTree.Node<T>> children) { this(Climate.RTree.buildParameterSpace(children), children); } protected SubTree(List<Climate.Parameter> parameterSpace, List<? extends Climate.RTree.Node<T>> children) { super(parameterSpace); this.children = (Node[])children.toArray(new Climate.RTree.Node[0]); } protected Climate.RTree.Leaf<T> search(long[] target, Climate.RTree.Leaf<T> candidate, Climate.DistanceMetric<T> distanceMetric) { long minDistance = (candidate == null) ? Float.MAX_VALUE : distanceMetric.distance(candidate, target); Climate.RTree.Leaf<T> closestLeaf = candidate; for (Climate.RTree.Node<T> child : this.children) { long childDistance = distanceMetric.distance(child, target); if (minDistance > childDistance) { Climate.RTree.Leaf<T> leaf = child.search(target, closestLeaf, distanceMetric); long leafDistance = (child == leaf) ? childDistance : distanceMetric.distance(leaf, target); if (minDistance > leafDistance) { minDistance = leafDistance; closestLeaf = leaf; }  }  }
/*     */        return closestLeaf; }
/* 282 */   } public static class ParameterList<T> extends Object { private final List<Pair<Climate.ParameterPoint, T>> values; public static <T> Codec<ParameterList<T>> codec(MapCodec<T> valueCodec) { return ExtraCodecs.nonEmptyList(RecordCodecBuilder.create(i -> i.group(Climate.ParameterPoint.CODEC
/* 283 */               .fieldOf("parameters").forGetter(Pair::getFirst), valueCodec
/* 284 */               .forGetter(Pair::getSecond)).apply(i, Pair::of))
/* 285 */           .listOf()).xmap(ParameterList::new, ParameterList::values); }
/*     */     
/*     */     private final Climate.RTree<T> index;
/*     */     public ParameterList(List<Pair<Climate.ParameterPoint, T>> values) {
/* 289 */       this.values = values;
/* 290 */       this.index = Climate.RTree.create(values);
/*     */     }
/*     */ 
/*     */     
/* 294 */     public List<Pair<Climate.ParameterPoint, T>> values() { return this.values; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 301 */     public T findValue(Climate.TargetPoint target) { return (T)findValueIndex(target); }
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     public T findValueBruteForce(Climate.TargetPoint target) {
/* 306 */       Iterator<Pair<Climate.ParameterPoint, T>> iterator = values().iterator();
/*     */ 
/*     */       
/* 309 */       Pair<Climate.ParameterPoint, T> first = (Pair)iterator.next();
/* 310 */       long bestFitness = ((Climate.ParameterPoint)first.getFirst()).fitness(target);
/* 311 */       T best = (T)first.getSecond();
/*     */       
/* 313 */       while (iterator.hasNext()) {
/* 314 */         Pair<Climate.ParameterPoint, T> parameter = (Pair)iterator.next();
/* 315 */         long fitness = ((Climate.ParameterPoint)parameter.getFirst()).fitness(target);
/* 316 */         if (fitness < bestFitness) {
/* 317 */           bestFitness = fitness;
/* 318 */           best = (T)parameter.getSecond();
/*     */         } 
/*     */       } 
/* 321 */       return best;
/*     */     }
/*     */ 
/*     */     
/* 325 */     public T findValueIndex(Climate.TargetPoint target) { return (T)findValueIndex(target, Climate.RTree.Node::distance); }
/*     */ 
/*     */ 
/*     */     
/* 329 */     protected T findValueIndex(Climate.TargetPoint target, Climate.DistanceMetric<T> distanceMetric) { return (T)this.index.search(target, distanceMetric); } }
/*     */   public static final class TargetPoint extends Record { private final long temperature;
/*     */     private final long humidity;
/*     */     private final long continentalness;
/*     */     private final long erosion;
/*     */     private final long depth;
/*     */     private final long weirdness;
/*     */     
/* 337 */     public TargetPoint(long temperature, long humidity, long continentalness, long erosion, long depth, long weirdness) { this.temperature = temperature; this.humidity = humidity; this.continentalness = continentalness; this.erosion = erosion; this.depth = depth; this.weirdness = weirdness; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/Climate$TargetPoint;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #337	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$TargetPoint; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Climate$TargetPoint;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #337	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$TargetPoint; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Climate$TargetPoint;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #337	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/Climate$TargetPoint;
/* 337 */       //   0	8	1	o	Ljava/lang/Object; } public long temperature() { return this.temperature; } public long humidity() { return this.humidity; } public long continentalness() { return this.continentalness; } public long erosion() { return this.erosion; } public long depth() { return this.depth; } public long weirdness() { return this.weirdness; }
/*     */     
/*     */     @VisibleForTesting
/* 340 */     protected long[] toParameterArray() { return new long[] { this.temperature, this.humidity, this.continentalness, this.erosion, this.depth, this.weirdness, 0L }; } }
/*     */   public static final class ParameterPoint extends Record { private final Climate.Parameter temperature; private final Climate.Parameter humidity; private final Climate.Parameter continentalness; private final Climate.Parameter erosion; private final Climate.Parameter depth; private final Climate.Parameter weirdness;
/*     */     private final long offset;
/*     */     
/* 344 */     public ParameterPoint(Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter depth, Climate.Parameter weirdness, long offset) { this.temperature = temperature; this.humidity = humidity; this.continentalness = continentalness; this.erosion = erosion; this.depth = depth; this.weirdness = weirdness; this.offset = offset; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/Climate$ParameterPoint;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #344	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$ParameterPoint; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Climate$ParameterPoint;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #344	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$ParameterPoint; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Climate$ParameterPoint;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #344	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/Climate$ParameterPoint;
/* 344 */       //   0	8	1	o	Ljava/lang/Object; } public Climate.Parameter temperature() { return this.temperature; } public Climate.Parameter humidity() { return this.humidity; } public Climate.Parameter continentalness() { return this.continentalness; } public Climate.Parameter erosion() { return this.erosion; } public Climate.Parameter depth() { return this.depth; } public Climate.Parameter weirdness() { return this.weirdness; } public long offset() { return this.offset; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 353 */     public static final Codec<ParameterPoint> CODEC = RecordCodecBuilder.create(i -> i.group(Climate.Parameter.CODEC
/* 354 */           .fieldOf("temperature").forGetter(()), Climate.Parameter.CODEC
/* 355 */           .fieldOf("humidity").forGetter(()), Climate.Parameter.CODEC
/* 356 */           .fieldOf("continentalness").forGetter(()), Climate.Parameter.CODEC
/* 357 */           .fieldOf("erosion").forGetter(()), Climate.Parameter.CODEC
/* 358 */           .fieldOf("depth").forGetter(()), Climate.Parameter.CODEC
/* 359 */           .fieldOf("weirdness").forGetter(()), 
/* 360 */           Codec.floatRange(0.0F, 1.0F).fieldOf("offset").xmap(Climate::quantizeCoord, Climate::unquantizeCoord).forGetter(()))
/* 361 */         .apply(i, ParameterPoint::new));
/*     */     
/*     */     private long fitness(Climate.TargetPoint target) {
/* 364 */       return Mth.square(this.temperature.distance(target.temperature)) + 
/* 365 */         Mth.square(this.humidity.distance(target.humidity)) + 
/* 366 */         Mth.square(this.continentalness.distance(target.continentalness)) + 
/* 367 */         Mth.square(this.erosion.distance(target.erosion)) + 
/* 368 */         Mth.square(this.depth.distance(target.depth)) + 
/* 369 */         Mth.square(this.weirdness.distance(target.weirdness)) + 
/* 370 */         Mth.square(this.offset);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 375 */     protected List<Climate.Parameter> parameterSpace() { return ImmutableList.of(this.temperature, this.humidity, this.continentalness, this.erosion, this.depth, this.weirdness, new Climate.Parameter(this.offset, this.offset)); } }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Parameter
/*     */     extends Record
/*     */   {
/*     */     private final long min;
/*     */     
/*     */     private final long max;
/*     */ 
/*     */     
/* 387 */     public Parameter(long min, long max) { this.min = min; this.max = max; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Climate$Parameter;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #387	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$Parameter; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Climate$Parameter;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #387	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/Climate$Parameter;
/* 387 */       //   0	8	1	o	Ljava/lang/Object; } public long min() { return this.min; } public long max() { return this.max; }
/* 388 */     public static final Codec<Parameter> CODEC = ExtraCodecs.intervalCodec(Codec.floatRange(-2.0F, 2.0F), "min", "max", (min, max) -> {
/* 389 */           if (min.compareTo(max) > 0) {
/* 390 */             return DataResult.error(());
/*     */           }
/* 392 */           return DataResult.success(new Parameter(Climate.quantizeCoord(min.floatValue()), Climate.quantizeCoord(max.floatValue())));
/* 393 */         }p -> Float.valueOf(Climate.unquantizeCoord(p.min())), p -> Float.valueOf(Climate.unquantizeCoord(p.max())));
/*     */ 
/*     */     
/* 396 */     public static Parameter point(float min) { return span(min, min); }
/*     */ 
/*     */     
/*     */     public static Parameter span(float min, float max) {
/* 400 */       if (min > max) {
/* 401 */         throw new IllegalArgumentException("min > max: " + min + " " + max);
/*     */       }
/* 403 */       return new Parameter(Climate.quantizeCoord(min), Climate.quantizeCoord(max));
/*     */     }
/*     */     
/*     */     public static Parameter span(Parameter min, Parameter max) {
/* 407 */       if (min.min() > max.max()) {
/* 408 */         throw new IllegalArgumentException("min > max: " + String.valueOf(min) + " " + String.valueOf(max));
/*     */       }
/* 410 */       return new Parameter(min.min(), max.max());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 415 */     public String toString() { return (this.min == this.max) ? String.format(Locale.ROOT, "%d", new Object[] { Long.valueOf(this.min) }) : String.format(Locale.ROOT, "[%d-%d]", new Object[] { Long.valueOf(this.min), Long.valueOf(this.max) }); }
/*     */ 
/*     */     
/*     */     public long distance(long target) {
/* 419 */       long above = target - this.max;
/* 420 */       long below = this.min - target;
/* 421 */       if (above > 0L) {
/* 422 */         return above;
/*     */       }
/* 424 */       return Math.max(below, 0L);
/*     */     }
/*     */     
/*     */     public long distance(Parameter target) {
/* 428 */       long above = target.min() - this.max;
/* 429 */       long below = this.min - target.max();
/*     */       
/* 431 */       if (above > 0L) {
/* 432 */         return above;
/*     */       }
/* 434 */       return Math.max(below, 0L);
/*     */     }
/*     */ 
/*     */     
/* 438 */     public Parameter span(Parameter other) { return (other == null) ? this : new Parameter(Math.min(this.min, other.min()), Math.max(this.max, other.max())); }
/*     */   }
/*     */ 
/*     */   
/*     */   public static Sampler empty() {
/* 443 */     zero = DensityFunctions.zero();
/* 444 */     return new Sampler(zero, zero, zero, zero, zero, zero, List.of());
/*     */   }
/*     */   public static final class Sampler extends Record { private final DensityFunction temperature; private final DensityFunction humidity; private final DensityFunction continentalness; private final DensityFunction erosion; private final DensityFunction depth; private final DensityFunction weirdness; private final List<Climate.ParameterPoint> spawnTarget;
/* 447 */     public Sampler(DensityFunction temperature, DensityFunction humidity, DensityFunction continentalness, DensityFunction erosion, DensityFunction depth, DensityFunction weirdness, List<Climate.ParameterPoint> spawnTarget) { this.temperature = temperature; this.humidity = humidity; this.continentalness = continentalness; this.erosion = erosion; this.depth = depth; this.weirdness = weirdness; this.spawnTarget = spawnTarget; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/Climate$Sampler;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #447	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$Sampler; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Climate$Sampler;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #447	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$Sampler; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Climate$Sampler;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #447	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/Climate$Sampler;
/* 447 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction temperature() { return this.temperature; } public DensityFunction humidity() { return this.humidity; } public DensityFunction continentalness() { return this.continentalness; } public DensityFunction erosion() { return this.erosion; } public DensityFunction depth() { return this.depth; } public DensityFunction weirdness() { return this.weirdness; } public List<Climate.ParameterPoint> spawnTarget() { return this.spawnTarget; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Climate.TargetPoint sample(int quartX, int quartY, int quartZ) {
/* 457 */       int blockX = QuartPos.toBlock(quartX);
/* 458 */       int blockY = QuartPos.toBlock(quartY);
/* 459 */       int blockZ = QuartPos.toBlock(quartZ);
/*     */       
/* 461 */       DensityFunction.SinglePointContext context = new DensityFunction.SinglePointContext(blockX, blockY, blockZ);
/*     */       
/* 463 */       return Climate.target(
/* 464 */           (float)this.temperature.compute(context), 
/* 465 */           (float)this.humidity.compute(context), 
/* 466 */           (float)this.continentalness.compute(context), 
/* 467 */           (float)this.erosion.compute(context), 
/* 468 */           (float)this.depth.compute(context), 
/* 469 */           (float)this.weirdness.compute(context));
/*     */     }
/*     */ 
/*     */     
/*     */     public BlockPos findSpawnPosition() {
/* 474 */       if (this.spawnTarget.isEmpty()) {
/* 475 */         return BlockPos.ZERO;
/*     */       }
/* 477 */       return Climate.findSpawnPosition(this.spawnTarget, this);
/*     */     } }
/*     */   private static class SpawnFinder { private static final long MAX_RADIUS = 2048L; private Result result;
/*     */     
/*     */     private static final class Result extends Record { private final BlockPos location;
/*     */       private final long fitness;
/*     */       
/* 484 */       private Result(BlockPos location, long fitness) { this.location = location; this.fitness = fitness; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #484	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #484	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #484	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;
/* 484 */         //   0	8	1	o	Ljava/lang/Object; } public BlockPos location() { return this.location; } public long fitness() { return this.fitness; } }
/*     */ 
/*     */ 
/*     */     
/*     */     private SpawnFinder(List<Climate.ParameterPoint> targetClimates, Climate.Sampler sampler) {
/* 489 */       this.result = getSpawnPositionAndFitness(targetClimates, sampler, 0, 0);
/*     */ 
/*     */       
/* 492 */       radialSearch(targetClimates, sampler, 2048.0F, 512.0F);
/*     */       
/* 494 */       radialSearch(targetClimates, sampler, 512.0F, 32.0F);
/*     */     }
/*     */     
/*     */     private void radialSearch(List<Climate.ParameterPoint> targetClimates, Climate.Sampler sampler, float maxRadius, float radiusIncrement) {
/* 498 */       float angle = 0.0F;
/* 499 */       float radius = radiusIncrement;
/* 500 */       BlockPos searchOrigin = this.result.location();
/* 501 */       while (radius <= maxRadius) {
/* 502 */         int x = searchOrigin.getX() + (int)(Math.sin(angle) * radius);
/* 503 */         int z = searchOrigin.getZ() + (int)(Math.cos(angle) * radius);
/* 504 */         Result candidate = getSpawnPositionAndFitness(targetClimates, sampler, x, z);
/* 505 */         if (candidate.fitness() < this.result.fitness()) {
/* 506 */           this.result = candidate;
/*     */         }
/*     */         
/* 509 */         angle += radiusIncrement / radius;
/* 510 */         if (angle > 6.283185307179586D) {
/* 511 */           angle = 0.0F;
/* 512 */           radius += radiusIncrement;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private static Result getSpawnPositionAndFitness(List<Climate.ParameterPoint> targetClimates, Climate.Sampler sampler, int blockX, int blockZ) {
/* 518 */       Climate.TargetPoint targetPoint = sampler.sample(QuartPos.fromBlock(blockX), 0, QuartPos.fromBlock(blockZ));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 525 */       Climate.TargetPoint zeroDepthTargetPoint = new Climate.TargetPoint(targetPoint.temperature(), targetPoint.humidity(), targetPoint.continentalness(), targetPoint.erosion(), 0L, targetPoint.weirdness());
/*     */ 
/*     */       
/* 528 */       long minFitness = Float.MAX_VALUE;
/* 529 */       for (Climate.ParameterPoint point : targetClimates) {
/* 530 */         minFitness = Math.min(minFitness, point.fitness(zeroDepthTargetPoint));
/*     */       }
/*     */       
/* 533 */       long distanceBiasToWorldOrigin = Mth.square(blockX) + Mth.square(blockZ);
/*     */       
/* 535 */       long fitnessWithDistance = minFitness * Mth.square(2048L) + distanceBiasToWorldOrigin;
/* 536 */       return new Result(new BlockPos(blockX, 0, blockZ), fitnessWithDistance);
/*     */     } } private static final class Result extends Record { private final BlockPos location; private final long fitness; private Result(BlockPos location, long fitness) { this.location = location;
/*     */       this.fitness = fitness; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #484	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #484	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #484	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/*     */     public BlockPos location() { return this.location; }
/*     */     public long fitness() { return this.fitness; } }
/* 541 */   public static BlockPos findSpawnPosition(List<ParameterPoint> targetClimates, Sampler sampler) { return (new SpawnFinder(targetClimates, sampler)).result.location(); }
/*     */   
/*     */   static interface DistanceMetric<T> {
/*     */     long distance(Climate.RTree.Node<T> param1Node, long[] param1ArrayOfLong);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\Climate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */