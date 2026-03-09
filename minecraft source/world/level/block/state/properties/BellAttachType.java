/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum BellAttachType implements StringRepresentable {
/*  6 */   FLOOR("floor"),
/*  7 */   CEILING("ceiling"),
/*  8 */   SINGLE_WALL("single_wall"),
/*  9 */   DOUBLE_WALL("double_wall");
/*    */ 
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/* 15 */   BellAttachType(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\BellAttachType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */