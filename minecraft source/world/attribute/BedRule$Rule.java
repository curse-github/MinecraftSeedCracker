/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public static enum Rule
/*    */   implements StringRepresentable
/*    */ {
/* 47 */   ALWAYS("always"),
/*    */ 
/*    */   
/* 50 */   WHEN_DARK("when_dark"),
/* 51 */   NEVER("never"); public static final Codec<Rule> CODEC; private final String name;
/*    */   
/*    */   static  {
/* 54 */     CODEC = StringRepresentable.fromEnum(Rule::values);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 59 */   Rule(String name) { this.name = name; }
/*    */ 
/*    */   
/*    */   public boolean test(Level level) {
/* 63 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: break; }  return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 72 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\BedRule$Rule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */