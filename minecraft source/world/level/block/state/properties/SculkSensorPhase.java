/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum SculkSensorPhase implements StringRepresentable {
/*  6 */   INACTIVE("inactive"),
/*  7 */   ACTIVE("active"),
/*  8 */   COOLDOWN("cooldown");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/* 13 */   SculkSensorPhase(String name) { this.name = name; }
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


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\SculkSensorPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */