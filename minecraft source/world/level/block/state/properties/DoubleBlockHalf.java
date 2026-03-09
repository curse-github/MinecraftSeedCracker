/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum DoubleBlockHalf implements StringRepresentable {
/*  7 */   UPPER(Direction.DOWN),
/*  8 */   LOWER(Direction.UP);
/*    */ 
/*    */   
/*    */   private final Direction directionToOther;
/*    */ 
/*    */   
/* 14 */   DoubleBlockHalf(Direction directionToOther) { this.directionToOther = directionToOther; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public Direction getDirectionToOther() { return this.directionToOther; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public String toString() { return getSerializedName(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public String getSerializedName() { return (this == UPPER) ? "upper" : "lower"; }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public DoubleBlockHalf getOtherHalf() { return (this == UPPER) ? LOWER : UPPER; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\DoubleBlockHalf.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */