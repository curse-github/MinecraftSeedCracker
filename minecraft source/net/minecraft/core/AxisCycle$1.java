/*    */ package net.minecraft.core;
/*    */ 
/*    */ 
/*    */ 
/*    */ static enum null
/*    */ {
/*  7 */   public int cycle(int x, int y, int z, Direction.Axis axis) { return axis.choose(x, y, z); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 12 */   public double cycle(double x, double y, double z, Direction.Axis axis) { return axis.choose(x, y, z); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public Direction.Axis cycle(Direction.Axis axis) { return axis; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public AxisCycle inverse() { return this; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\AxisCycle$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */