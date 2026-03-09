/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum ChestType implements StringRepresentable {
/*  6 */   SINGLE("single"),
/*  7 */   LEFT("left"),
/*  8 */   RIGHT("right");
/*    */ 
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/* 14 */   ChestType(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public String getSerializedName() { return this.name; }
/*    */ 
/*    */   
/*    */   public ChestType getOpposite() {
/* 23 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: break; }  return 
/*    */ 
/*    */       
/* 26 */       LEFT;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\ChestType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */