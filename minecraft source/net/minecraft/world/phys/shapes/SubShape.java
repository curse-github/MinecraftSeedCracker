/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public final class SubShape extends DiscreteVoxelShape {
/*    */   private final DiscreteVoxelShape parent;
/*    */   private final int startX;
/*    */   private final int startY;
/*    */   private final int startZ;
/*    */   private final int endX;
/*    */   private final int endY;
/*    */   private final int endZ;
/*    */   
/*    */   protected SubShape(DiscreteVoxelShape parent, int startX, int startY, int startZ, int endX, int endY, int endZ) {
/* 16 */     super(endX - startX, endY - startY, endZ - startZ);
/* 17 */     this.parent = parent;
/* 18 */     this.startX = startX;
/* 19 */     this.startY = startY;
/* 20 */     this.startZ = startZ;
/* 21 */     this.endX = endX;
/* 22 */     this.endY = endY;
/* 23 */     this.endZ = endZ;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public boolean isFull(int x, int y, int z) { return this.parent.isFull(this.startX + x, this.startY + y, this.startZ + z); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public void fill(int x, int y, int z) { this.parent.fill(this.startX + x, this.startY + y, this.startZ + z); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public int firstFull(Direction.Axis axis) { return clampToShape(axis, this.parent.firstFull(axis)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public int lastFull(Direction.Axis axis) { return clampToShape(axis, this.parent.lastFull(axis)); }
/*    */ 
/*    */   
/*    */   private int clampToShape(Direction.Axis axis, int parentResult) {
/* 47 */     int start = axis.choose(this.startX, this.startY, this.startZ);
/* 48 */     int end = axis.choose(this.endX, this.endY, this.endZ);
/* 49 */     return Mth.clamp(parentResult, start, end) - start;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\SubShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */