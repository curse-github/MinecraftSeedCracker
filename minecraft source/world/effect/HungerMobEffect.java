/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ class HungerMobEffect
/*    */   extends MobEffect
/*    */ {
/* 10 */   protected HungerMobEffect(MobEffectCategory category, int color) { super(category, color); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplification) {
/* 15 */     if (mob instanceof Player) { Player player = (Player)mob;
/*    */       
/* 17 */       player.causeFoodExhaustion(0.005F * (amplification + 1)); }
/*    */     
/* 19 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) { return true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\effect\HungerMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */