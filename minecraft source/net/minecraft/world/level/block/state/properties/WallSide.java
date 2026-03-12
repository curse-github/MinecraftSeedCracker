/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum WallSide implements StringRepresentable {
/*  6 */   NONE("none"),
/*  7 */   LOW("low"),
/*  8 */   TALL("tall");
/*    */ 
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/* 14 */   WallSide(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public String toString() { return getSerializedName(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\WallSide.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */