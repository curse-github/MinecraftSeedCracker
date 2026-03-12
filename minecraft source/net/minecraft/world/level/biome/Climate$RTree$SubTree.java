/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class SubTree<T>
/*     */   extends Climate.RTree.Node<T>
/*     */ {
/*     */   private final Climate.RTree.Node<T>[] children;
/*     */   
/* 116 */   protected SubTree(List<? extends Climate.RTree.Node<T>> children) { this(Climate.RTree.buildParameterSpace(children), children); }
/*     */ 
/*     */   
/*     */   protected SubTree(List<Climate.Parameter> parameterSpace, List<? extends Climate.RTree.Node<T>> children) {
/* 120 */     super(parameterSpace);
/* 121 */     this.children = (Node[])children.toArray(new Climate.RTree.Node[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Climate.RTree.Leaf<T> search(long[] target, Climate.RTree.Leaf<T> candidate, Climate.DistanceMetric<T> distanceMetric) {
/* 126 */     long minDistance = (candidate == null) ? Float.MAX_VALUE : distanceMetric.distance(candidate, target);
/* 127 */     Climate.RTree.Leaf<T> closestLeaf = candidate;
/*     */     
/* 129 */     for (Climate.RTree.Node<T> child : this.children) {
/* 130 */       long childDistance = distanceMetric.distance(child, target);
/* 131 */       if (minDistance > childDistance) {
/*     */         
/* 133 */         Climate.RTree.Leaf<T> leaf = child.search(target, closestLeaf, distanceMetric);
/* 134 */         long leafDistance = (child == leaf) ? childDistance : distanceMetric.distance(leaf, target);
/* 135 */         if (minDistance > leafDistance) {
/* 136 */           minDistance = leafDistance;
/* 137 */           closestLeaf = leaf;
/*     */         } 
/*     */       } 
/*     */     } 
/* 141 */     return closestLeaf;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\Climate$RTree$SubTree.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */