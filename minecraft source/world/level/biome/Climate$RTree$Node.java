/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.Mth;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class Node<T>
/*    */   extends Object
/*    */ {
/*    */   protected final Climate.Parameter[] parameterSpace;
/*    */   
/* 79 */   protected Node(List<Climate.Parameter> parameterSpace) { this.parameterSpace = (Parameter[])parameterSpace.toArray(new Climate.Parameter[0]); }
/*    */ 
/*    */   
/*    */   protected abstract Climate.RTree.Leaf<T> search(long[] paramArrayOfLong, Climate.RTree.Leaf<T> paramLeaf, Climate.DistanceMetric<T> paramDistanceMetric);
/*    */   
/*    */   protected long distance(long[] target) {
/* 85 */     long distance = 0L;
/* 86 */     for (int i = 0; i < 7; i++) {
/* 87 */       distance += Mth.square(this.parameterSpace[i].distance(target[i]));
/*    */     }
/* 89 */     return distance;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 94 */   public String toString() { return Arrays.toString(this.parameterSpace); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\Climate$RTree$Node.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */