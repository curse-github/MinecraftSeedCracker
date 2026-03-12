/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class BinaryHeap {
/*   6 */   private Node[] heap = new Node[128];
/*     */   
/*     */   private int size;
/*     */   
/*     */   public Node insert(Node node) {
/*  11 */     if (node.heapIdx >= 0) {
/*  12 */       throw new IllegalStateException("OW KNOWS!");
/*     */     }
/*     */     
/*  15 */     if (this.size == this.heap.length) {
/*  16 */       Node[] newHeap = new Node[this.size << 1];
/*  17 */       System.arraycopy(this.heap, 0, newHeap, 0, this.size);
/*  18 */       this.heap = newHeap;
/*     */     } 
/*     */ 
/*     */     
/*  22 */     this.heap[this.size] = node;
/*  23 */     node.heapIdx = this.size;
/*  24 */     upHeap(this.size++);
/*     */     
/*  26 */     return node;
/*     */   }
/*     */ 
/*     */   
/*  30 */   public void clear() { this.size = 0; }
/*     */ 
/*     */ 
/*     */   
/*  34 */   public Node peek() { return this.heap[0]; }
/*     */ 
/*     */   
/*     */   public Node pop() {
/*  38 */     Node popped = this.heap[0];
/*  39 */     this.heap[0] = this.heap[--this.size];
/*  40 */     this.heap[this.size] = null;
/*  41 */     if (this.size > 0) {
/*  42 */       downHeap(0);
/*     */     }
/*  44 */     popped.heapIdx = -1;
/*  45 */     return popped;
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Node node) {
/*  50 */     this.heap[node.heapIdx] = this.heap[--this.size];
/*  51 */     this.heap[this.size] = null;
/*  52 */     if (this.size > node.heapIdx) {
/*  53 */       if ((this.heap[node.heapIdx]).f < node.f) {
/*  54 */         upHeap(node.heapIdx);
/*     */       } else {
/*  56 */         downHeap(node.heapIdx);
/*     */       } 
/*     */     }
/*     */     
/*  60 */     node.heapIdx = -1;
/*     */   }
/*     */   
/*     */   public void changeCost(Node node, float newCost) {
/*  64 */     float oldCost = node.f;
/*  65 */     node.f = newCost;
/*  66 */     if (newCost < oldCost) {
/*  67 */       upHeap(node.heapIdx);
/*     */     } else {
/*  69 */       downHeap(node.heapIdx);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  74 */   public int size() { return this.size; }
/*     */ 
/*     */   
/*     */   private void upHeap(int idx) {
/*  78 */     Node node = this.heap[idx];
/*  79 */     float cost = node.f;
/*  80 */     while (idx > 0) {
/*  81 */       int parentIdx = idx - 1 >> 1;
/*  82 */       Node parent = this.heap[parentIdx];
/*  83 */       if (cost < parent.f) {
/*  84 */         this.heap[idx] = parent;
/*  85 */         parent.heapIdx = idx;
/*  86 */         idx = parentIdx;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  91 */     this.heap[idx] = node;
/*  92 */     node.heapIdx = idx;
/*     */   }
/*     */   
/*     */   private void downHeap(int idx) {
/*  96 */     Node node = this.heap[idx];
/*  97 */     float cost = node.f; while (true) {
/*     */       float rightCost;
/*     */       Node rightNode;
/* 100 */       int leftIdx = 1 + (idx << 1);
/* 101 */       int rightIdx = leftIdx + 1;
/*     */       
/* 103 */       if (leftIdx >= this.size) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 108 */       Node leftNode = this.heap[leftIdx];
/* 109 */       float leftCost = leftNode.f;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 114 */       if (rightIdx >= this.size) {
/*     */         
/* 116 */         rightNode = null;
/* 117 */         rightCost = Float.POSITIVE_INFINITY;
/*     */       } else {
/* 119 */         rightNode = this.heap[rightIdx];
/* 120 */         rightCost = rightNode.f;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 125 */       if (leftCost < rightCost) {
/* 126 */         if (leftCost < cost) {
/* 127 */           this.heap[idx] = leftNode;
/* 128 */           leftNode.heapIdx = idx;
/* 129 */           idx = leftIdx;
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/* 134 */       if (rightCost < cost) {
/* 135 */         this.heap[idx] = rightNode;
/* 136 */         rightNode.heapIdx = idx;
/* 137 */         idx = rightIdx;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*     */       break;
/*     */     } 
/* 144 */     this.heap[idx] = node;
/* 145 */     node.heapIdx = idx;
/*     */   }
/*     */ 
/*     */   
/* 149 */   public boolean isEmpty() { return (this.size == 0); }
/*     */ 
/*     */ 
/*     */   
/* 153 */   public Node[] getHeap() { return (Node[])Arrays.copyOf(this.heap, this.size); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\pathfinder\BinaryHeap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */