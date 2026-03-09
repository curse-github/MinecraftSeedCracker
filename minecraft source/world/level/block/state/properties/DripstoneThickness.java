/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum DripstoneThickness implements StringRepresentable {
/*  6 */   TIP_MERGE("tip_merge"),
/*  7 */   TIP("tip"),
/*  8 */   FRUSTUM("frustum"),
/*  9 */   MIDDLE("middle"),
/* 10 */   BASE("base");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/* 15 */   DripstoneThickness(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public String toString() { return this.name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\DripstoneThickness.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */