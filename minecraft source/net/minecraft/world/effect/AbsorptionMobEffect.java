/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ class AbsorptionMobEffect
/*    */   extends MobEffect {
/*  8 */   protected AbsorptionMobEffect(MobEffectCategory category, int color) { super(category, color); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 13 */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) { return (mob.getAbsorptionAmount() > 0.0F); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEffectStarted(LivingEntity mob, int amplifier) {
/* 23 */     super.onEffectStarted(mob, amplifier);
/* 24 */     mob.setAbsorptionAmount(Math.max(mob.getAbsorptionAmount(), (4 * (1 + amplifier))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\effect\AbsorptionMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */