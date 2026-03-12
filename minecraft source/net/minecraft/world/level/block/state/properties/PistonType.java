/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum PistonType implements StringRepresentable {
/*  6 */   DEFAULT("normal"),
/*  7 */   STICKY("sticky");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/* 12 */   PistonType(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public String toString() { return this.name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\PistonType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */