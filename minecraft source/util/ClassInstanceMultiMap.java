/*    */ package net.minecraft.util;
/*    */ import com.google.common.collect.Iterators;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class ClassInstanceMultiMap<T> extends AbstractCollection<T> {
/*    */   private final Map<Class<?>, List<T>> byClass;
/*    */   
/*    */   public ClassInstanceMultiMap(Class<T> baseClass) {
/* 16 */     this.byClass = Maps.newHashMap();
/*    */ 
/*    */     
/* 19 */     this.allInstances = Lists.newArrayList();
/*    */ 
/*    */     
/* 22 */     this.baseClass = baseClass;
/* 23 */     this.byClass.put(baseClass, this.allInstances);
/*    */   }
/*    */   private final Class<T> baseClass; private final List<T> allInstances;
/*    */   
/*    */   public boolean add(T instance) {
/* 28 */     boolean success = false;
/* 29 */     for (Map.Entry<Class<?>, List<T>> entry : this.byClass.entrySet()) {
/* 30 */       if (((Class)entry.getKey()).isInstance(instance)) {
/* 31 */         success |= ((List)entry.getValue()).add(instance);
/*    */       }
/*    */     } 
/* 34 */     return success;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(Object object) {
/* 39 */     boolean success = false;
/* 40 */     for (Map.Entry<Class<?>, List<T>> entry : this.byClass.entrySet()) {
/* 41 */       if (((Class)entry.getKey()).isInstance(object)) {
/* 42 */         List<T> list = (List)entry.getValue();
/* 43 */         success |= list.remove(object);
/*    */       } 
/*    */     } 
/* 46 */     return success;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public boolean contains(Object o) { return find(o.getClass()).contains(o); }
/*    */ 
/*    */ 
/*    */   
/*    */   public <S> Collection<S> find(Class<S> index) {
/* 56 */     if (!this.baseClass.isAssignableFrom(index)) {
/* 57 */       throw new IllegalArgumentException("Don't know how to search for " + String.valueOf(index));
/*    */     }
/* 59 */     List<? extends T> instances = (List)this.byClass.computeIfAbsent(index, k -> { Objects.requireNonNull(k); return (List)this.allInstances.stream().filter(k::isInstance).collect(Util.toMutableList());
/* 60 */         }); return Collections.unmodifiableCollection(instances);
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<T> iterator() {
/* 65 */     if (this.allInstances.isEmpty()) {
/* 66 */       return Collections.emptyIterator();
/*    */     }
/* 68 */     return Iterators.unmodifiableIterator(this.allInstances.iterator());
/*    */   }
/*    */ 
/*    */   
/* 72 */   public List<T> getAllInstances() { return ImmutableList.copyOf(this.allInstances); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 77 */   public int size() { return this.allInstances.size(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\ClassInstanceMultiMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */