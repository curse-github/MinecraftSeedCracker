/*    */ package net.minecraft.world.level.gamerules;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum GameRuleType implements StringRepresentable {
/*  6 */   INT("integer"),
/*  7 */   BOOL("boolean");
/*    */   
/*    */   private final String name;
/*    */   
/* 11 */   GameRuleType(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gamerules\GameRuleType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */