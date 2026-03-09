/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Deque;
/*    */ import java.util.List;
/*    */ import java.util.RandomAccess;
/*    */ import java.util.SequencedCollection;
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
/*    */ public interface ListAndDeque<T>
/*    */   extends List<T>, RandomAccess, Cloneable, Serializable, Deque<T>
/*    */ {
/* 34 */   default boolean offer(T value) { return offerLast(value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   default T remove() { return (T)removeFirst(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   default T poll() { return (T)pollFirst(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   default T element() { return (T)getFirst(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   default T peek() { return (T)peekFirst(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   default void push(T value) { addFirst(value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   default T pop() { return (T)removeFirst(); }
/*    */   
/*    */   ListAndDeque<T> reversed();
/*    */   
/*    */   T getFirst();
/*    */   
/*    */   T getLast();
/*    */   
/*    */   void addFirst(T paramT);
/*    */   
/*    */   void addLast(T paramT);
/*    */   
/*    */   T removeFirst();
/*    */   
/*    */   T removeLast();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\ListAndDeque.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */