/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.function.Consumer;
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
/*    */ public final class Graph
/*    */ {
/*    */   public static <T> boolean depthFirstSearch(Map<T, Set<T>> edges, Set<T> discovered, Set<T> currentlyVisiting, Consumer<T> reverseTopologicalOrder, T current) {
/* 25 */     if (discovered.contains(current)) {
/* 26 */       return false;
/*    */     }
/* 28 */     if (currentlyVisiting.contains(current)) {
/* 29 */       return true;
/*    */     }
/* 31 */     currentlyVisiting.add(current);
/* 32 */     for (T next : (Set)edges.getOrDefault(current, ImmutableSet.of())) {
/* 33 */       if (depthFirstSearch(edges, discovered, currentlyVisiting, reverseTopologicalOrder, next)) {
/* 34 */         return true;
/*    */       }
/*    */     } 
/* 37 */     currentlyVisiting.remove(current);
/* 38 */     discovered.add(current);
/* 39 */     reverseTopologicalOrder.accept(current);
/* 40 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\Graph.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */