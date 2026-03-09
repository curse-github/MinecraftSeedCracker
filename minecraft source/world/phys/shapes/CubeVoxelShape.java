/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public final class CubeVoxelShape
/*    */   extends VoxelShape {
/*  9 */   protected CubeVoxelShape(DiscreteVoxelShape shape) { super(shape); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   public DoubleList getCoords(Direction.Axis axis) { return new CubePointRange(this.shape.getSize(axis)); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int findIndex(Direction.Axis axis, double coord) {
/* 19 */     int size = this.shape.getSize(axis);
/* 20 */     return Mth.floor(Mth.clamp(coord * size, -1.0D, size));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\CubeVoxelShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */