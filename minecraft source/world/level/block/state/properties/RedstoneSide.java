/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum RedstoneSide implements StringRepresentable {
/*  6 */   UP("up"),
/*  7 */   SIDE("side"),
/*  8 */   NONE("none");
/*    */ 
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/* 14 */   RedstoneSide(String name) { this.name = name; }
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
/*    */ 
/*    */ 
/*    */   
/* 28 */   public boolean isConnected() { return (this != NONE); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\RedstoneSide.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */