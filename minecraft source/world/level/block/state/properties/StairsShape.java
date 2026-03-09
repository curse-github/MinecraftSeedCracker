/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum StairsShape implements StringRepresentable {
/*  6 */   STRAIGHT("straight"),
/*    */   
/*  8 */   INNER_LEFT("inner_left"),
/*  9 */   INNER_RIGHT("inner_right"),
/* 10 */   OUTER_LEFT("outer_left"),
/* 11 */   OUTER_RIGHT("outer_right");
/*    */ 
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/* 17 */   StairsShape(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public String toString() { return this.name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\StairsShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */