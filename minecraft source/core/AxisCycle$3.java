/*    */ package net.minecraft.core;
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
/*    */ static enum null
/*    */ {
/* 52 */   public int cycle(int x, int y, int z, Direction.Axis axis) { return axis.choose(y, z, x); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public double cycle(double x, double y, double z, Direction.Axis axis) { return axis.choose(y, z, x); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   public Direction.Axis cycle(Direction.Axis axis) { return AXIS_VALUES[Math.floorMod(axis.ordinal() - 1, 3)]; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   public AxisCycle inverse() { return FORWARD; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\AxisCycle$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */