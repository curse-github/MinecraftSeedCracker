/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleLists;
/*    */ 
/*    */ public class IndirectMerger implements IndexMerger {
/*  8 */   private static final DoubleList EMPTY = DoubleLists.unmodifiable(DoubleArrayList.wrap(new double[] { 0.0D }));
/*    */   
/*    */   private final double[] result;
/*    */   
/*    */   private final int[] firstIndices;
/*    */   
/*    */   private final int[] secondIndices;
/*    */   
/*    */   private final int resultLength;
/*    */ 
/*    */   
/*    */   public IndirectMerger(DoubleList first, DoubleList second, boolean firstOnlyMatters, boolean secondOnlyMatters) {
/* 20 */     double lastValue = NaND;
/*    */     
/* 22 */     int firstSize = first.size();
/* 23 */     int secondSize = second.size();
/* 24 */     int capacity = firstSize + secondSize;
/* 25 */     this.result = new double[capacity];
/* 26 */     this.firstIndices = new int[capacity];
/* 27 */     this.secondIndices = new int[capacity];
/*    */     
/* 29 */     boolean canSkipFirst = !firstOnlyMatters;
/* 30 */     boolean canSkipSecond = !secondOnlyMatters;
/*    */     
/* 32 */     int resultIndex = 0;
/* 33 */     int firstIndex = 0;
/* 34 */     int secondIndex = 0;
/*    */     
/*    */     while (true) {
/* 37 */       boolean ranOutOfFirst = (firstIndex >= firstSize);
/* 38 */       boolean ranOutOfSecond = (secondIndex >= secondSize);
/*    */       
/* 40 */       if (ranOutOfFirst && ranOutOfSecond) {
/*    */         break;
/*    */       }
/* 43 */       boolean choseFirst = (!ranOutOfFirst && (ranOutOfSecond || first.getDouble(firstIndex) < second.getDouble(secondIndex) + 1.0E-7D));
/*    */       
/* 45 */       if (choseFirst) {
/* 46 */         firstIndex++;
/* 47 */         if (canSkipFirst && (secondIndex == 0 || ranOutOfSecond)) {
/*    */           continue;
/*    */         }
/*    */       } else {
/* 51 */         secondIndex++;
/* 52 */         if (canSkipSecond && (firstIndex == 0 || ranOutOfFirst)) {
/*    */           continue;
/*    */         }
/*    */       } 
/*    */       
/* 57 */       int currentFirstIndex = firstIndex - 1;
/* 58 */       int currentSecondIndex = secondIndex - 1;
/*    */       
/* 60 */       double nextValue = choseFirst ? first.getDouble(currentFirstIndex) : second.getDouble(currentSecondIndex);
/* 61 */       if (lastValue < nextValue - 1.0E-7D) {
/* 62 */         this.firstIndices[resultIndex] = currentFirstIndex;
/* 63 */         this.secondIndices[resultIndex] = currentSecondIndex;
/* 64 */         this.result[resultIndex] = nextValue;
/* 65 */         resultIndex++;
/* 66 */         lastValue = nextValue; continue;
/*    */       } 
/* 68 */       this.firstIndices[resultIndex - 1] = currentFirstIndex;
/* 69 */       this.secondIndices[resultIndex - 1] = currentSecondIndex;
/*    */     } 
/*    */ 
/*    */     
/* 73 */     this.resultLength = Math.max(1, resultIndex);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean forMergedIndexes(IndexMerger.IndexConsumer consumer) {
/* 78 */     int length = this.resultLength - 1;
/* 79 */     for (int i = 0; i < length; i++) {
/* 80 */       if (!consumer.merge(this.firstIndices[i], this.secondIndices[i], i)) {
/* 81 */         return false;
/*    */       }
/*    */     } 
/* 84 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 89 */   public int size() { return this.resultLength; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 94 */   public DoubleList getList() { return (this.resultLength <= 1) ? EMPTY : DoubleArrayList.wrap(this.result, this.resultLength); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\IndirectMerger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */