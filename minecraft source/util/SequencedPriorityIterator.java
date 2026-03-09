/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.AbstractIterator;
/*    */ import com.google.common.collect.Queues;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import java.util.Deque;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SequencedPriorityIterator<T>
/*    */   extends AbstractIterator<T>
/*    */ {
/*    */   private static final int MIN_PRIO = -2147483648;
/* 21 */   private Deque<T> highestPrioQueue = null;
/* 22 */   private int highestPrio = Integer.MIN_VALUE;
/*    */   
/* 24 */   private final Int2ObjectMap<Deque<T>> queuesByPriority = new Int2ObjectOpenHashMap();
/*    */   
/*    */   public void add(T data, int priority) {
/* 27 */     if (priority == this.highestPrio && this.highestPrioQueue != null) {
/* 28 */       this.highestPrioQueue.addLast(data);
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     Deque<T> queue = (Deque)this.queuesByPriority.computeIfAbsent(priority, order -> Queues.newArrayDeque());
/* 33 */     queue.addLast(data);
/*    */     
/* 35 */     if (priority >= this.highestPrio) {
/* 36 */       this.highestPrioQueue = queue;
/* 37 */       this.highestPrio = priority;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected T computeNext() {
/* 43 */     if (this.highestPrioQueue == null) {
/* 44 */       return (T)endOfData();
/*    */     }
/*    */     
/* 47 */     T result = (T)this.highestPrioQueue.removeFirst();
/*    */     
/* 49 */     if (result == null) {
/* 50 */       return (T)endOfData();
/*    */     }
/*    */     
/* 53 */     if (this.highestPrioQueue.isEmpty()) {
/* 54 */       switchCacheToNextHighestPrioQueue();
/*    */     }
/*    */     
/* 57 */     return result;
/*    */   }
/*    */   
/*    */   private void switchCacheToNextHighestPrioQueue() {
/* 61 */     int foundHighestPrio = Integer.MIN_VALUE;
/* 62 */     Deque<T> foundHighestPrioQueue = null;
/*    */     
/* 64 */     for (ObjectIterator objectIterator = Int2ObjectMaps.fastIterable(this.queuesByPriority).iterator(); objectIterator.hasNext(); ) { Int2ObjectMap.Entry<Deque<T>> entry = (Int2ObjectMap.Entry)objectIterator.next();
/* 65 */       Deque<T> queue = (Deque)entry.getValue();
/* 66 */       int prio = entry.getIntKey();
/*    */       
/* 68 */       if (prio > foundHighestPrio && !queue.isEmpty()) {
/* 69 */         foundHighestPrio = prio;
/* 70 */         foundHighestPrioQueue = queue;
/*    */         
/* 72 */         if (prio == this.highestPrio - 1) {
/*    */           break;
/*    */         }
/*    */       }  }
/*    */ 
/*    */     
/* 78 */     this.highestPrio = foundHighestPrio;
/* 79 */     this.highestPrioQueue = foundHighestPrioQueue;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\SequencedPriorityIterator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */