/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ import net.minecraft.ChatFormatting;
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
/*    */ public static enum Sentiment
/*    */ {
/* 58 */   POSITIVE,
/* 59 */   NEUTRAL,
/* 60 */   NEGATIVE;
/*    */ 
/*    */   
/*    */   public ChatFormatting getStyle(boolean valueIncrease) {
/* 64 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: return 
/* 65 */           valueIncrease ? ChatFormatting.BLUE : ChatFormatting.RED;
/*    */       case 1: 
/* 67 */       case 2: break; }  return valueIncrease ? ChatFormatting.RED : ChatFormatting.BLUE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\attributes\Attribute$Sentiment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */