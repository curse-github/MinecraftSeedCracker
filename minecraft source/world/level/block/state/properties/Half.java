/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum Half implements StringRepresentable {
/*  6 */   TOP("top"),
/*  7 */   BOTTOM("bottom");
/*    */ 
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/* 13 */   Half(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public String toString() { return this.name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\Half.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */