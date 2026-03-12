/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FoundRectangle
/*    */ {
/*    */   public final BlockPos minCorner;
/*    */   public final int axis1Size;
/*    */   public final int axis2Size;
/*    */   
/*    */   public FoundRectangle(BlockPos minCorner, int axis1Size, int axis2Size) {
/* 41 */     this.minCorner = minCorner;
/* 42 */     this.axis1Size = axis1Size;
/* 43 */     this.axis2Size = axis2Size;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\BlockUtil$FoundRectangle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */