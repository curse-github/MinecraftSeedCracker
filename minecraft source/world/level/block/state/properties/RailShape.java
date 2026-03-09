/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum RailShape implements StringRepresentable {
/*  6 */   NORTH_SOUTH("north_south"),
/*  7 */   EAST_WEST("east_west"),
/*  8 */   ASCENDING_EAST("ascending_east"),
/*  9 */   ASCENDING_WEST("ascending_west"),
/* 10 */   ASCENDING_NORTH("ascending_north"),
/* 11 */   ASCENDING_SOUTH("ascending_south"),
/* 12 */   SOUTH_EAST("south_east"),
/* 13 */   SOUTH_WEST("south_west"),
/* 14 */   NORTH_WEST("north_west"),
/* 15 */   NORTH_EAST("north_east");
/*    */ 
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/* 21 */   RailShape(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public String getName() { return this.name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public String toString() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public boolean isSlope() { return (this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\RailShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */