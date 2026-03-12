/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RTree<T>
/*     */   extends Object
/*     */ {
/*     */   private static final int CHILDREN_PER_NODE = 6;
/*     */   private final Node<T> root;
/*     */   private final ThreadLocal<Leaf<T>> lastResult;
/*     */   
/*     */   private RTree(Node<T> root) {
/*  69 */     this.lastResult = new ThreadLocal();
/*     */ 
/*     */     
/*  72 */     this.root = root;
/*     */   }
/*     */   
/*     */   static abstract class Node<T>
/*     */     extends Object {
/*     */     protected final Climate.Parameter[] parameterSpace;
/*     */     
/*  79 */     protected Node(List<Climate.Parameter> parameterSpace) { this.parameterSpace = (Parameter[])parameterSpace.toArray(new Climate.Parameter[0]); }
/*     */ 
/*     */     
/*     */     protected abstract Climate.RTree.Leaf<T> search(long[] param2ArrayOfLong, Climate.RTree.Leaf<T> param2Leaf, Climate.DistanceMetric<T> param2DistanceMetric);
/*     */     
/*     */     protected long distance(long[] target) {
/*  85 */       long distance = 0L;
/*  86 */       for (int i = 0; i < 7; i++) {
/*  87 */         distance += Mth.square(this.parameterSpace[i].distance(target[i]));
/*     */       }
/*  89 */       return distance;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  94 */     public String toString() { return Arrays.toString(this.parameterSpace); }
/*     */   }
/*     */   
/*     */   private static final class Leaf<T>
/*     */     extends Node<T> {
/*     */     private final T value;
/*     */     
/*     */     private Leaf(Climate.ParameterPoint parameterPoint, T value) {
/* 102 */       super(parameterPoint.parameterSpace());
/* 103 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 108 */     protected Leaf<T> search(long[] target, Leaf<T> candidate, Climate.DistanceMetric<T> distanceMetric) { return this; }
/*     */   }
/*     */   
/*     */   private static final class SubTree<T>
/*     */     extends Node<T>
/*     */   {
/*     */     private final Climate.RTree.Node<T>[] children;
/*     */     
/* 116 */     protected SubTree(List<? extends Climate.RTree.Node<T>> children) { this(Climate.RTree.buildParameterSpace(children), children); }
/*     */ 
/*     */     
/*     */     protected SubTree(List<Climate.Parameter> parameterSpace, List<? extends Climate.RTree.Node<T>> children) {
/* 120 */       super(parameterSpace);
/* 121 */       this.children = (Node[])children.toArray(new Climate.RTree.Node[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Climate.RTree.Leaf<T> search(long[] target, Climate.RTree.Leaf<T> candidate, Climate.DistanceMetric<T> distanceMetric) {
/* 126 */       long minDistance = (candidate == null) ? Float.MAX_VALUE : distanceMetric.distance(candidate, target);
/* 127 */       Climate.RTree.Leaf<T> closestLeaf = candidate;
/*     */       
/* 129 */       for (Climate.RTree.Node<T> child : this.children) {
/* 130 */         long childDistance = distanceMetric.distance(child, target);
/* 131 */         if (minDistance > childDistance) {
/*     */           
/* 133 */           Climate.RTree.Leaf<T> leaf = child.search(target, closestLeaf, distanceMetric);
/* 134 */           long leafDistance = (child == leaf) ? childDistance : distanceMetric.distance(leaf, target);
/* 135 */           if (minDistance > leafDistance) {
/* 136 */             minDistance = leafDistance;
/* 137 */             closestLeaf = leaf;
/*     */           } 
/*     */         } 
/*     */       } 
/* 141 */       return closestLeaf;
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> RTree<T> create(List<Pair<Climate.ParameterPoint, T>> values) {
/* 146 */     if (values.isEmpty()) {
/* 147 */       throw new IllegalArgumentException("Need at least one value to build the search tree.");
/*     */     }
/* 149 */     int dimensions = ((Climate.ParameterPoint)((Pair)values.get(0)).getFirst()).parameterSpace().size();
/* 150 */     if (dimensions != 7) {
/* 151 */       throw new IllegalStateException("Expecting parameter space to be 7, got " + dimensions);
/*     */     }
/*     */     
/* 154 */     List<Leaf<T>> leaves = (List)values.stream().map(p -> new Leaf((Climate.ParameterPoint)p.getFirst(), p.getSecond())).collect(Collectors.toCollection(java.util.ArrayList::new));
/*     */     
/* 156 */     return new RTree(build(dimensions, leaves));
/*     */   }
/*     */   
/*     */   private static <T> Node<T> build(int dimensions, List<? extends Node<T>> children) {
/* 160 */     if (children.isEmpty()) {
/* 161 */       throw new IllegalStateException("Need at least one child to build a node");
/*     */     }
/* 163 */     if (children.size() == 1) {
/* 164 */       return (Node)children.get(0);
/*     */     }
/* 166 */     if (children.size() <= 6) {
/* 167 */       children.sort(Comparator.comparingLong(leaf -> {
/* 168 */               long totalMagnitude = 0L;
/* 169 */               for (int d = 0; d < dimensions; d++) {
/* 170 */                 Climate.Parameter parameter = leaf.parameterSpace[d];
/* 171 */                 totalMagnitude += Math.abs((parameter.min() + parameter.max()) / 2L);
/*     */               } 
/* 173 */               return totalMagnitude;
/*     */             }));
/* 175 */       return new SubTree(children);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     long minCost = Float.MAX_VALUE;
/* 183 */     int minDimension = -1;
/* 184 */     List<SubTree<T>> minBuckets = null;
/*     */     
/* 186 */     for (int d = 0; d < dimensions; d++) {
/* 187 */       sort(children, dimensions, d, false);
/* 188 */       List<SubTree<T>> buckets = bucketize(children);
/*     */       
/* 190 */       long totalCost = 0L;
/* 191 */       for (SubTree<T> bucket : buckets) {
/* 192 */         totalCost += cost(bucket.parameterSpace);
/*     */       }
/*     */       
/* 195 */       if (minCost > totalCost) {
/* 196 */         minCost = totalCost;
/* 197 */         minDimension = d;
/* 198 */         minBuckets = buckets;
/*     */       } 
/*     */     } 
/*     */     
/* 202 */     sort(minBuckets, dimensions, minDimension, true);
/*     */     
/* 204 */     return new SubTree((List)minBuckets.stream().map(b -> build(dimensions, Arrays.asList(b.children))).collect(Collectors.toList()));
/*     */   }
/*     */   
/*     */   private static <T> void sort(List<? extends Node<T>> children, int dimensions, int dimension, boolean absolute) {
/* 208 */     Comparator<Node<T>> comparator = comparator(dimension, absolute);
/* 209 */     for (int d = 1; d < dimensions; d++) {
/* 210 */       comparator = comparator.thenComparing(comparator((dimension + d) % dimensions, absolute));
/*     */     }
/* 212 */     children.sort(comparator);
/*     */   }
/*     */   
/*     */   private static <T> Comparator<Node<T>> comparator(int dimension, boolean absolute) {
/* 216 */     return Comparator.comparingLong(leaf -> {
/* 217 */           Climate.Parameter parameter = leaf.parameterSpace[dimension];
/* 218 */           long center = (parameter.min() + parameter.max()) / 2L;
/* 219 */           return absolute ? Math.abs(center) : center;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> List<SubTree<T>> bucketize(List<? extends Node<T>> nodes) {
/* 227 */     List<SubTree<T>> buckets = Lists.newArrayList();
/*     */     
/* 229 */     List<Node<T>> children = Lists.newArrayList();
/* 230 */     int expectedChildrenCount = (int)Math.pow(6.0D, Math.floor(Math.log(nodes.size() - 0.01D) / Math.log(6.0D)));
/* 231 */     for (Node<T> child : nodes) {
/* 232 */       children.add(child);
/* 233 */       if (children.size() >= expectedChildrenCount) {
/* 234 */         buckets.add(new SubTree(children));
/* 235 */         children = Lists.newArrayList();
/*     */       } 
/*     */     } 
/* 238 */     if (!children.isEmpty()) {
/* 239 */       buckets.add(new SubTree(children));
/*     */     }
/* 241 */     return buckets;
/*     */   }
/*     */   
/*     */   private static long cost(Parameter[] parameterSpace) {
/* 245 */     long result = 0L;
/* 246 */     for (Climate.Parameter parameter : parameterSpace) {
/* 247 */       result += Math.abs(parameter.max() - parameter.min());
/*     */     }
/* 249 */     return result;
/*     */   }
/*     */   
/*     */   private static <T> List<Climate.Parameter> buildParameterSpace(List<? extends Node<T>> children) {
/* 253 */     if (children.isEmpty()) {
/* 254 */       throw new IllegalArgumentException("SubTree needs at least one child");
/*     */     }
/* 256 */     int dimensions = 7;
/* 257 */     List<Climate.Parameter> bounds = Lists.newArrayList();
/* 258 */     for (int d = 0; d < 7; d++) {
/* 259 */       bounds.add(null);
/*     */     }
/* 261 */     for (Node<T> child : children) {
/* 262 */       for (int d = 0; d < 7; d++) {
/* 263 */         bounds.set(d, child.parameterSpace[d].span((Climate.Parameter)bounds.get(d)));
/*     */       }
/*     */     } 
/* 266 */     return bounds;
/*     */   }
/*     */   
/*     */   public T search(Climate.TargetPoint target, Climate.DistanceMetric<T> distanceMetric) {
/* 270 */     long[] targetArray = target.toParameterArray();
/* 271 */     Leaf<T> leaf = this.root.search(targetArray, (Leaf)this.lastResult.get(), distanceMetric);
/* 272 */     this.lastResult.set(leaf);
/* 273 */     return (T)leaf.value;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\Climate$RTree.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */