/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import com.google.common.math.IntMath;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ 
/*    */ public final class DiscreteCubeMerger implements IndexMerger {
/*    */   private final CubePointRange result;
/*    */   private final int firstDiv;
/*    */   private final int secondDiv;
/*    */   
/*    */   DiscreteCubeMerger(int firstSize, int secondSize) {
/* 12 */     this.result = new CubePointRange((int)Shapes.lcm(firstSize, secondSize));
/*    */     
/* 14 */     int gcd = IntMath.gcd(firstSize, secondSize);
/* 15 */     this.firstDiv = firstSize / gcd;
/* 16 */     this.secondDiv = secondSize / gcd;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean forMergedIndexes(IndexMerger.IndexConsumer consumer) {
/* 21 */     int size = this.result.size() - 1;
/* 22 */     for (int i = 0; i < size; i++) {
/* 23 */       if (!consumer.merge(i / this.secondDiv, i / this.firstDiv, i)) {
/* 24 */         return false;
/*    */       }
/*    */     } 
/* 27 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public int size() { return this.result.size(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public DoubleList getList() { return this.result; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\DiscreteCubeMerger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */