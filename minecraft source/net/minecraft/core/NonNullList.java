/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.AbstractList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class NonNullList<E> extends AbstractList<E> {
/*    */   private final List<E> list;
/*    */   private final E defaultValue;
/*    */   
/* 13 */   public static <E> NonNullList<E> create() { return new NonNullList(Lists.newArrayList(), null); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static <E> NonNullList<E> createWithCapacity(int capacity) { return new NonNullList(Lists.newArrayListWithCapacity(capacity), null); }
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> NonNullList<E> withSize(int size, E defaultValue) {
/* 22 */     Objects.requireNonNull(defaultValue);
/*    */     
/* 24 */     Object[] objects = new Object[size];
/* 25 */     Arrays.fill(objects, defaultValue);
/* 26 */     return new NonNullList(Arrays.asList(objects), defaultValue);
/*    */   }
/*    */ 
/*    */   
/*    */   @SafeVarargs
/* 31 */   public static <E> NonNullList<E> of(E defaultValue, E... values) { return new NonNullList(Arrays.asList(values), defaultValue); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected NonNullList(List<E> list, E defaultValue) {
/* 38 */     this.list = list;
/* 39 */     this.defaultValue = defaultValue;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public E get(int index) { return (E)this.list.get(index); }
/*    */ 
/*    */ 
/*    */   
/*    */   public E set(int index, E element) {
/* 49 */     Objects.requireNonNull(element);
/*    */     
/* 51 */     return (E)this.list.set(index, element);
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(int index, E element) {
/* 56 */     Objects.requireNonNull(element);
/*    */     
/* 58 */     this.list.add(index, element);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public E remove(int index) { return (E)this.list.remove(index); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 68 */   public int size() { return this.list.size(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void clear() {
/* 73 */     if (this.defaultValue == null) {
/* 74 */       super.clear();
/*    */     } else {
/* 76 */       for (int i = 0; i < size(); i++)
/* 77 */         set(i, this.defaultValue); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\NonNullList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */