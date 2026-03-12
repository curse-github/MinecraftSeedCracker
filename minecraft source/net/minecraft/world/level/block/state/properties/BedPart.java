/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum BedPart implements StringRepresentable {
/*  6 */   HEAD("head"),
/*  7 */   FOOT("foot");
/*    */ 
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/* 13 */   BedPart(String name) { this.name = name; }
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


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\BedPart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */