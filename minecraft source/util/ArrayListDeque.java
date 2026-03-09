/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.SequencedCollection;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.UnaryOperator;
/*     */ 
/*     */ public class ArrayListDeque<T>
/*     */   extends AbstractList<T> implements ListAndDeque<T> {
/*     */   private static final int MIN_GROWTH = 1;
/*     */   private Object[] contents;
/*     */   private int head;
/*     */   private int size;
/*     */   
/*  22 */   public ArrayListDeque() { this(1); }
/*     */ 
/*     */   
/*     */   public ArrayListDeque(int capacity) {
/*  26 */     this.contents = new Object[capacity];
/*  27 */     this.head = 0;
/*  28 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  33 */   public int size() { return this.size; }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*  38 */   public int capacity() { return this.contents.length; }
/*     */ 
/*     */ 
/*     */   
/*  42 */   private int getIndex(int index) { return (index + this.head) % this.contents.length; }
/*     */ 
/*     */ 
/*     */   
/*     */   public T get(int index) {
/*  47 */     verifyIndexInRange(index);
/*  48 */     return (T)getInner(getIndex(index));
/*     */   }
/*     */   
/*     */   private static void verifyIndexInRange(int index, int size) {
/*  52 */     if (index < 0 || index >= size) {
/*  53 */       throw new IndexOutOfBoundsException(index);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  58 */   private void verifyIndexInRange(int index) { verifyIndexInRange(index, this.size); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   private T getInner(int innerIndex) { return (T)this.contents[innerIndex]; }
/*     */ 
/*     */ 
/*     */   
/*     */   public T set(int index, T element) {
/*  68 */     verifyIndexInRange(index);
/*  69 */     Objects.requireNonNull(element);
/*  70 */     int innerIndex = getIndex(index);
/*  71 */     T current = (T)getInner(innerIndex);
/*  72 */     this.contents[innerIndex] = element;
/*  73 */     return current;
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int index, T element) {
/*  78 */     verifyIndexInRange(index, this.size + 1);
/*  79 */     Objects.requireNonNull(element);
/*  80 */     if (this.size == this.contents.length) {
/*  81 */       grow();
/*     */     }
/*  83 */     int internalIndex = getIndex(index);
/*  84 */     if (index == this.size) {
/*  85 */       this.contents[internalIndex] = element;
/*  86 */     } else if (index == 0) {
/*  87 */       this.head--;
/*  88 */       if (this.head < 0) {
/*  89 */         this.head += this.contents.length;
/*     */       }
/*  91 */       this.contents[getIndex(0)] = element;
/*     */     } else {
/*  93 */       for (int i = this.size - 1; i >= index; i--) {
/*  94 */         this.contents[getIndex(i + 1)] = this.contents[getIndex(i)];
/*     */       }
/*  96 */       this.contents[internalIndex] = element;
/*     */     } 
/*  98 */     this.modCount++;
/*  99 */     this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   private void grow() {
/* 104 */     int newLength = this.contents.length + Math.max(this.contents.length >> 1, 1);
/* 105 */     Object[] newContents = new Object[newLength];
/* 106 */     copyCount(newContents, this.size);
/* 107 */     this.head = 0;
/* 108 */     this.contents = newContents;
/*     */   }
/*     */ 
/*     */   
/*     */   public T remove(int index) {
/* 113 */     verifyIndexInRange(index);
/* 114 */     int innerIndex = getIndex(index);
/* 115 */     T value = (T)getInner(innerIndex);
/* 116 */     if (index == 0) {
/* 117 */       this.contents[innerIndex] = null;
/* 118 */       this.head++;
/* 119 */     } else if (index == this.size - 1) {
/* 120 */       this.contents[innerIndex] = null;
/*     */     } else {
/* 122 */       for (int i = index + 1; i < this.size; i++) {
/* 123 */         this.contents[getIndex(i - 1)] = get(i);
/*     */       }
/* 125 */       this.contents[getIndex(this.size - 1)] = null;
/*     */     } 
/* 127 */     this.modCount++;
/* 128 */     this.size--;
/* 129 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeIf(Predicate<? super T> filter) {
/* 134 */     int removed = 0;
/* 135 */     for (int i = 0; i < this.size; i++) {
/* 136 */       T value = (T)get(i);
/* 137 */       if (filter.test(value)) {
/* 138 */         removed++;
/* 139 */       } else if (removed != 0) {
/* 140 */         this.contents[getIndex(i - removed)] = value;
/* 141 */         this.contents[getIndex(i)] = null;
/*     */       } 
/*     */     } 
/* 144 */     this.modCount += removed;
/* 145 */     this.size -= removed;
/* 146 */     return (removed != 0);
/*     */   }
/*     */   
/*     */   private void copyCount(Object[] newContents, int count) {
/* 150 */     for (int i = 0; i < count; i++) {
/* 151 */       newContents[i] = get(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void replaceAll(UnaryOperator<T> operator) {
/* 157 */     for (int i = 0; i < this.size; i++) {
/* 158 */       int index = getIndex(i);
/* 159 */       this.contents[index] = Objects.requireNonNull(operator.apply(getInner(i)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super T> action) {
/* 165 */     for (int i = 0; i < this.size; i++) {
/* 166 */       action.accept(get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 172 */   public void addFirst(T value) { add(0, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   public void addLast(T value) { add(this.size, value); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean offerFirst(T value) {
/* 182 */     addFirst(value);
/* 183 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offerLast(T value) {
/* 188 */     addLast(value);
/* 189 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public T removeFirst() {
/* 194 */     if (this.size == 0) {
/* 195 */       throw new NoSuchElementException();
/*     */     }
/* 197 */     return (T)remove(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public T removeLast() {
/* 202 */     if (this.size == 0) {
/* 203 */       throw new NoSuchElementException();
/*     */     }
/* 205 */     return (T)remove(this.size - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 210 */   public ListAndDeque<T> reversed() { return new ReversedView(this); }
/*     */ 
/*     */ 
/*     */   
/*     */   public T pollFirst() {
/* 215 */     if (this.size == 0) {
/* 216 */       return null;
/*     */     }
/* 218 */     return (T)removeFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public T pollLast() {
/* 223 */     if (this.size == 0) {
/* 224 */       return null;
/*     */     }
/* 226 */     return (T)removeLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public T getFirst() {
/* 231 */     if (this.size == 0) {
/* 232 */       throw new NoSuchElementException();
/*     */     }
/* 234 */     return (T)get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public T getLast() {
/* 239 */     if (this.size == 0) {
/* 240 */       throw new NoSuchElementException();
/*     */     }
/* 242 */     return (T)get(this.size - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public T peekFirst() {
/* 247 */     if (this.size == 0) {
/* 248 */       return null;
/*     */     }
/* 250 */     return (T)getFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public T peekLast() {
/* 255 */     if (this.size == 0) {
/* 256 */       return null;
/*     */     }
/* 258 */     return (T)getLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeFirstOccurrence(Object o) {
/* 263 */     for (int i = 0; i < this.size; i++) {
/* 264 */       T value = (T)get(i);
/* 265 */       if (Objects.equals(o, value)) {
/* 266 */         remove(i);
/* 267 */         return true;
/*     */       } 
/*     */     } 
/* 270 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeLastOccurrence(Object o) {
/* 275 */     for (int i = this.size - 1; i >= 0; i--) {
/* 276 */       T value = (T)get(i);
/* 277 */       if (Objects.equals(o, value)) {
/* 278 */         remove(i);
/* 279 */         return true;
/*     */       } 
/*     */     } 
/* 282 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 287 */   public Iterator<T> descendingIterator() { return new DescendingIterator(); }
/*     */   
/*     */   private class DescendingIterator
/*     */     extends Object
/*     */     implements Iterator<T> {
/*     */     private int index;
/*     */     
/* 294 */     public DescendingIterator() { this.index = this$0.size() - 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 299 */     public boolean hasNext() { return (this.index >= 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 304 */     public T next() { return (T)ArrayListDeque.this.get(this.index--); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 309 */     public void remove() { ArrayListDeque.this.remove(this.index + 1); }
/*     */   }
/*     */   
/*     */   private class ReversedView
/*     */     extends AbstractList<T>
/*     */     implements ListAndDeque<T> {
/*     */     private final ArrayListDeque<T> source;
/*     */     
/* 317 */     public ReversedView(ArrayListDeque<T> source) { this.source = source; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 322 */     public ListAndDeque<T> reversed() { return this.source; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 327 */     public T getFirst() { return (T)this.source.getLast(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 332 */     public T getLast() { return (T)this.source.getFirst(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 337 */     public void addFirst(T t) { this.source.addLast(t); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 342 */     public void addLast(T t) { this.source.addFirst(t); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 347 */     public boolean offerFirst(T t) { return this.source.offerLast(t); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 352 */     public boolean offerLast(T t) { return this.source.offerFirst(t); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 357 */     public T pollFirst() { return (T)this.source.pollLast(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 362 */     public T pollLast() { return (T)this.source.pollFirst(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 367 */     public T peekFirst() { return (T)this.source.peekLast(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 372 */     public T peekLast() { return (T)this.source.peekFirst(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 377 */     public T removeFirst() { return (T)this.source.removeLast(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 382 */     public T removeLast() { return (T)this.source.removeFirst(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 387 */     public boolean removeFirstOccurrence(Object o) { return this.source.removeLastOccurrence(o); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 392 */     public boolean removeLastOccurrence(Object o) { return this.source.removeFirstOccurrence(o); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 397 */     public Iterator<T> descendingIterator() { return this.source.iterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 402 */     public int size() { return this.source.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 407 */     public boolean isEmpty() { return this.source.isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 412 */     public boolean contains(Object o) { return this.source.contains(o); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 417 */     public T get(int index) { return (T)this.source.get(reverseIndex(index)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 422 */     public T set(int index, T element) { return (T)this.source.set(reverseIndex(index), element); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 428 */     public void add(int index, T element) { this.source.add(reverseIndex(index) + 1, element); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 433 */     public T remove(int index) { return (T)this.source.remove(reverseIndex(index)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 438 */     public int indexOf(Object o) { return reverseIndex(this.source.lastIndexOf(o)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 443 */     public int lastIndexOf(Object o) { return reverseIndex(this.source.indexOf(o)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 448 */     public List<T> subList(int fromIndex, int toIndex) { return this.source.subList(reverseIndex(toIndex) + 1, reverseIndex(fromIndex) + 1).reversed(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 453 */     public Iterator<T> iterator() { return this.source.descendingIterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 458 */     public void clear() { this.source.clear(); }
/*     */ 
/*     */ 
/*     */     
/* 462 */     private int reverseIndex(int index) { return (index == -1) ? -1 : (this.source.size() - 1 - index); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\ArrayListDeque.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */