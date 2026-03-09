/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ public class InstantenousMobEffect
/*    */   extends MobEffect {
/*  5 */   public InstantenousMobEffect(MobEffectCategory category, int color) { super(category, color); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 10 */   public boolean isInstantenous() { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   public boolean shouldApplyEffectTickThisTick(int remainingDuration, int amplification) { return (remainingDuration >= 1); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\effect\InstantenousMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */