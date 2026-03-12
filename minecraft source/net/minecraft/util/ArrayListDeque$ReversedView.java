/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.AbstractList;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.SequencedCollection;
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
/*     */ class ReversedView
/*     */   extends AbstractList<T>
/*     */   implements ListAndDeque<T>
/*     */ {
/*     */   private final ArrayListDeque<T> source;
/*     */   
/* 317 */   public ReversedView(ArrayListDeque<T> source) { this.source = source; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 322 */   public ListAndDeque<T> reversed() { return this.source; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 327 */   public T getFirst() { return (T)this.source.getLast(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 332 */   public T getLast() { return (T)this.source.getFirst(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 337 */   public void addFirst(T t) { this.source.addLast(t); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 342 */   public void addLast(T t) { this.source.addFirst(t); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 347 */   public boolean offerFirst(T t) { return this.source.offerLast(t); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 352 */   public boolean offerLast(T t) { return this.source.offerFirst(t); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 357 */   public T pollFirst() { return (T)this.source.pollLast(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 362 */   public T pollLast() { return (T)this.source.pollFirst(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 367 */   public T peekFirst() { return (T)this.source.peekLast(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 372 */   public T peekLast() { return (T)this.source.peekFirst(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 377 */   public T removeFirst() { return (T)this.source.removeLast(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 382 */   public T removeLast() { return (T)this.source.removeFirst(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 387 */   public boolean removeFirstOccurrence(Object o) { return this.source.removeLastOccurrence(o); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 392 */   public boolean removeLastOccurrence(Object o) { return this.source.removeFirstOccurrence(o); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 397 */   public Iterator<T> descendingIterator() { return this.source.iterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 402 */   public int size() { return this.source.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 407 */   public boolean isEmpty() { return this.source.isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 412 */   public boolean contains(Object o) { return this.source.contains(o); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 417 */   public T get(int index) { return (T)this.source.get(reverseIndex(index)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 422 */   public T set(int index, T element) { return (T)this.source.set(reverseIndex(index), element); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 428 */   public void add(int index, T element) { this.source.add(reverseIndex(index) + 1, element); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 433 */   public T remove(int index) { return (T)this.source.remove(reverseIndex(index)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 438 */   public int indexOf(Object o) { return reverseIndex(this.source.lastIndexOf(o)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 443 */   public int lastIndexOf(Object o) { return reverseIndex(this.source.indexOf(o)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 448 */   public List<T> subList(int fromIndex, int toIndex) { return this.source.subList(reverseIndex(toIndex) + 1, reverseIndex(fromIndex) + 1).reversed(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 453 */   public Iterator<T> iterator() { return this.source.descendingIterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 458 */   public void clear() { this.source.clear(); }
/*     */ 
/*     */ 
/*     */   
/* 462 */   private int reverseIndex(int index) { return (index == -1) ? -1 : (this.source.size() - 1 - index); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\ArrayListDeque$ReversedView.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */