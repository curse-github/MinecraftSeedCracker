/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ 
/*    */ public class NonOverlappingMerger extends AbstractDoubleList implements IndexMerger {
/*    */   private final DoubleList lower;
/*    */   private final DoubleList upper;
/*    */   private final boolean swap;
/*    */   
/*    */   protected NonOverlappingMerger(DoubleList lower, DoubleList upper, boolean swap) {
/* 12 */     this.lower = lower;
/* 13 */     this.upper = upper;
/* 14 */     this.swap = swap;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public int size() { return this.lower.size() + this.upper.size(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean forMergedIndexes(IndexMerger.IndexConsumer consumer) {
/* 24 */     if (this.swap) {
/* 25 */       return forNonSwappedIndexes((firstIndex, secondIndex, resultIndex) -> consumer.merge(secondIndex, firstIndex, resultIndex));
/*    */     }
/* 27 */     return forNonSwappedIndexes(consumer);
/*    */   }
/*    */   
/*    */   private boolean forNonSwappedIndexes(IndexMerger.IndexConsumer consumer) {
/* 31 */     int lowerSize = this.lower.size();
/* 32 */     for (int i = 0; i < lowerSize; i++) {
/* 33 */       if (!consumer.merge(i, -1, i)) {
/* 34 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 38 */     int upperSize = this.upper.size() - 1;
/* 39 */     for (int i = 0; i < upperSize; i++) {
/* 40 */       if (!consumer.merge(lowerSize - 1, i, lowerSize + i)) {
/* 41 */         return false;
/*    */       }
/*    */     } 
/* 44 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public double getDouble(int index) {
/* 49 */     if (index < this.lower.size()) {
/* 50 */       return this.lower.getDouble(index);
/*    */     }
/* 52 */     return this.upper.getDouble(index - this.lower.size());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 58 */   public DoubleList getList() { return this; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\NonOverlappingMerger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */