/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.ChatFormatting;
/*    */ 
/*    */ public static enum MobEffectCategory {
/*  6 */   BENEFICIAL(ChatFormatting.BLUE),
/*  7 */   HARMFUL(ChatFormatting.RED),
/*  8 */   NEUTRAL(ChatFormatting.BLUE);
/*    */   
/*    */   private final ChatFormatting tooltipFormatting;
/*    */ 
/*    */   
/* 13 */   MobEffectCategory(ChatFormatting tooltipFormatting) { this.tooltipFormatting = tooltipFormatting; }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public ChatFormatting getTooltipFormatting() { return this.tooltipFormatting; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\effect\MobEffectCategory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */