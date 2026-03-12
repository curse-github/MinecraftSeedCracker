/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.HashMultimap;
/*    */ import com.google.common.collect.Multimap;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DependencySorter<K, V extends DependencySorter.Entry<K>>
/*    */   extends Object
/*    */ {
/* 19 */   private final Map<K, V> contents = new HashMap();
/*    */   
/*    */   public DependencySorter<K, V> addEntry(K id, V value) {
/* 22 */     this.contents.put(id, value);
/* 23 */     return this;
/*    */   }
/*    */   
/*    */   private void visitDependenciesAndElement(Multimap<K, K> dependencies, Set<K> alreadyVisited, K id, BiConsumer<K, V> output) {
/* 27 */     if (!alreadyVisited.add(id)) {
/*    */       return;
/*    */     }
/*    */     
/* 31 */     dependencies.get(id).forEach(dependency -> visitDependenciesAndElement(dependencies, alreadyVisited, dependency, output));
/*    */     
/* 33 */     V current = (V)(Entry)this.contents.get(id);
/* 34 */     if (current != null) {
/* 35 */       output.accept(id, current);
/*    */     }
/*    */   }
/*    */   
/*    */   private static <K> boolean isCyclic(Multimap<K, K> directDependencies, K from, K to) {
/* 40 */     Collection<K> dependencies = directDependencies.get(to);
/* 41 */     if (dependencies.contains(from)) {
/* 42 */       return true;
/*    */     }
/* 44 */     return dependencies.stream().anyMatch(dep -> isCyclic(directDependencies, from, dep));
/*    */   }
/*    */   
/*    */   private static <K> void addDependencyIfNotCyclic(Multimap<K, K> directDependencies, K from, K to) {
/* 48 */     if (!isCyclic(directDependencies, from, to)) {
/* 49 */       directDependencies.put(from, to);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void orderByDependencies(BiConsumer<K, V> output) {
/* 59 */     HashMultimap hashMultimap = HashMultimap.create();
/*    */ 
/*    */ 
/*    */     
/* 63 */     this.contents.forEach((id, value) -> 
/* 64 */         value.visitRequiredDependencies(()));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 70 */     this.contents.forEach((id, value) -> 
/* 71 */         value.visitOptionalDependencies(()));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 77 */     Set<K> alreadyVisited = new HashSet<K>();
/* 78 */     this.contents.keySet().forEach(topId -> visitDependenciesAndElement(directDependencies, alreadyVisited, topId, output));
/*    */   }
/*    */   
/*    */   public static interface Entry<K> {
/*    */     void visitRequiredDependencies(Consumer<K> param1Consumer);
/*    */     
/*    */     void visitOptionalDependencies(Consumer<K> param1Consumer);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\DependencySorter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */