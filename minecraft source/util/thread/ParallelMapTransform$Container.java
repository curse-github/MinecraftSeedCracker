/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Container<K, U, V>
/*     */   extends Record
/*     */ {
/*     */   private final BiFunction<K, U, V> operation;
/*     */   private final Object[] keys;
/*     */   private final Object[] values;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/thread/ParallelMapTransform$Container;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #54	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container<TK;TU;TV;>; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/thread/ParallelMapTransform$Container;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #54	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container<TK;TU;TV;>; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/thread/ParallelMapTransform$Container;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #54	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container<TK;TU;TV;>; }
/*     */   
/*  54 */   private Container(BiFunction<K, U, V> operation, Object[] keys, Object[] values) { this.operation = operation; this.keys = keys; this.values = values; } public BiFunction<K, U, V> operation() { return this.operation; } public Object[] keys() { return this.keys; } public Object[] values() { return this.values; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public Container(BiFunction<K, U, V> operation, int size) { this(operation, new Object[size], new Object[size]); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(int index, K key, U input) {
/*  70 */     this.keys[index] = key;
/*  71 */     this.values[index] = input;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  76 */   private K key(int index) { return (K)this.keys[index]; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   private V output(int index) { return (V)this.values[index]; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   private U input(int index) { return (U)this.values[index]; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public void applyOperation(int index) { this.values[index] = this.operation.apply(key(index), input(index)); }
/*     */ 
/*     */   
/*     */   public void copyOut(int index, Map<K, V> output) {
/*  95 */     V value = (V)output(index);
/*  96 */     if (value != null) {
/*  97 */       K key = (K)key(index);
/*  98 */       output.put(key, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 103 */   public int size() { return this.keys.length; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\thread\ParallelMapTransform$Container.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */