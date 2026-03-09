/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum DoorHingeSide implements StringRepresentable {
/*  6 */   LEFT,
/*  7 */   RIGHT;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 12 */   public String toString() { return getSerializedName(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public String getSerializedName() { return (this == LEFT) ? "left" : "right"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\DoorHingeSide.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */