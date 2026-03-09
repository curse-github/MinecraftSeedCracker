/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ 
/*    */ public class OffsetDoubleList extends AbstractDoubleList {
/*    */   private final DoubleList delegate;
/*    */   private final double offset;
/*    */   
/*    */   public OffsetDoubleList(DoubleList delegate, double offset) {
/* 11 */     this.delegate = delegate;
/* 12 */     this.offset = offset;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public double getDouble(int index) { return this.delegate.getDouble(index) + this.offset; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public int size() { return this.delegate.size(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\OffsetDoubleList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */