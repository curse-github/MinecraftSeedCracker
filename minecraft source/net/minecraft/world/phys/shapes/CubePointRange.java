/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
/*    */ 
/*    */ public class CubePointRange extends AbstractDoubleList {
/*    */   private final int parts;
/*    */   
/*    */   public CubePointRange(int parts) {
/*  9 */     if (parts <= 0) {
/* 10 */       throw new IllegalArgumentException("Need at least 1 part");
/*    */     }
/* 12 */     this.parts = parts;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public double getDouble(int index) { return index / this.parts; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public int size() { return this.parts + 1; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\CubePointRange.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */