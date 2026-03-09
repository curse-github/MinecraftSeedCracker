/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public class SliceShape extends VoxelShape {
/*    */   private final VoxelShape delegate;
/*    */   private final Direction.Axis axis;
/*  9 */   private static final DoubleList SLICE_COORDS = new CubePointRange(1);
/*    */   
/*    */   public SliceShape(VoxelShape delegate, Direction.Axis axis, int point) {
/* 12 */     super(makeSlice(delegate.shape, axis, point));
/* 13 */     this.delegate = delegate;
/* 14 */     this.axis = axis;
/*    */   }
/*    */   
/*    */   private static DiscreteVoxelShape makeSlice(DiscreteVoxelShape delegate, Direction.Axis axis, int point) {
/* 18 */     return new SubShape(delegate, axis
/* 19 */         .choose(point, 0, 0), axis
/* 20 */         .choose(0, point, 0), axis
/* 21 */         .choose(0, 0, point), axis
/* 22 */         .choose(point + 1, delegate.xSize, delegate.xSize), axis
/* 23 */         .choose(delegate.ySize, point + 1, delegate.ySize), axis
/* 24 */         .choose(delegate.zSize, delegate.zSize, point + 1));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public DoubleList getCoords(Direction.Axis axis) {
/* 30 */     if (axis == this.axis) {
/* 31 */       return SLICE_COORDS;
/*    */     }
/* 33 */     return this.delegate.getCoords(axis);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\SliceShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */