/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum Tilt implements StringRepresentable {
/*  6 */   NONE("none", true),
/*  7 */   UNSTABLE("unstable", false),
/*  8 */   PARTIAL("partial", true),
/*  9 */   FULL("full", true);
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final boolean causesVibration;
/*    */   
/*    */   Tilt(String name, boolean causesVibration) {
/* 16 */     this.name = name;
/* 17 */     this.causesVibration = causesVibration;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public String getSerializedName() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public boolean causesVibration() { return this.causesVibration; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\Tilt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */