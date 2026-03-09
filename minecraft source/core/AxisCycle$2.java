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
/*    */ static enum null
/*    */ {
/* 31 */   public int cycle(int x, int y, int z, Direction.Axis axis) { return axis.choose(z, x, y); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public double cycle(double x, double y, double z, Direction.Axis axis) { return axis.choose(z, x, y); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public Direction.Axis cycle(Direction.Axis axis) { return AXIS_VALUES[Math.floorMod(axis.ordinal() + 1, 3)]; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public AxisCycle inverse() { return BACKWARD; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\AxisCycle$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */