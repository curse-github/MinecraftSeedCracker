/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ 
/*    */ public class IdenticalMerger
/*    */   implements IndexMerger {
/*    */   private final DoubleList coords;
/*    */   
/*  9 */   public IdenticalMerger(DoubleList coords) { this.coords = coords; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean forMergedIndexes(IndexMerger.IndexConsumer consumer) {
/* 14 */     int size = this.coords.size() - 1;
/* 15 */     for (int i = 0; i < size; i++) {
/* 16 */       if (!consumer.merge(i, i, i)) {
/* 17 */         return false;
/*    */       }
/*    */     } 
/* 20 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public int size() { return this.coords.size(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public DoubleList getList() { return this.coords; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\IdenticalMerger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */