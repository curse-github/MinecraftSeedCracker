/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum AttachFace implements StringRepresentable {
/*  6 */   FLOOR("floor"),
/*  7 */   WALL("wall"),
/*  8 */   CEILING("ceiling");
/*    */ 
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/* 14 */   AttachFace(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\AttachFace.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */