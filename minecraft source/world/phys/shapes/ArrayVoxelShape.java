/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArrayVoxelShape
/*    */   extends VoxelShape
/*    */ {
/*    */   private final DoubleList xs;
/*    */   private final DoubleList ys;
/*    */   private final DoubleList zs;
/*    */   
/*    */   protected ArrayVoxelShape(DiscreteVoxelShape shape, double[] xs, double[] ys, double[] zs) {
/* 19 */     this(shape, 
/*    */         
/* 21 */         DoubleArrayList.wrap(Arrays.copyOf(xs, shape.getXSize() + 1)), 
/* 22 */         DoubleArrayList.wrap(Arrays.copyOf(ys, shape.getYSize() + 1)), 
/* 23 */         DoubleArrayList.wrap(Arrays.copyOf(zs, shape.getZSize() + 1)));
/*    */   }
/*    */ 
/*    */   
/*    */   ArrayVoxelShape(DiscreteVoxelShape shape, DoubleList xs, DoubleList ys, DoubleList zs) {
/* 28 */     super(shape);
/* 29 */     int xSize = shape.getXSize() + 1;
/* 30 */     int ySize = shape.getYSize() + 1;
/* 31 */     int zSize = shape.getZSize() + 1;
/* 32 */     if (xSize != xs.size() || ySize != ys.size() || zSize != zs.size()) {
/* 33 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException("Lengths of point arrays must be consistent with the size of the VoxelShape."));
/*    */     }
/* 35 */     this.xs = xs;
/* 36 */     this.ys = ys;
/* 37 */     this.zs = zs;
/*    */   }
/*    */ 
/*    */   
/*    */   public DoubleList getCoords(Direction.Axis axis) {
/* 42 */     switch (axis) { default: throw new MatchException(null, null);case X: case Y: case Z: break; }  return 
/*    */ 
/*    */       
/* 45 */       this.zs;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\ArrayVoxelShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */