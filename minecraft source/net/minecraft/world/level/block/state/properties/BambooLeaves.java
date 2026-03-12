/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum BambooLeaves implements StringRepresentable {
/*  6 */   NONE("none"),
/*  7 */   SMALL("small"),
/*  8 */   LARGE("large");
/*    */ 
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/* 14 */   BambooLeaves(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public String toString() { return this.name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\BambooLeaves.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */