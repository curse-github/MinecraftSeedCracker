/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ class RaidOmenMobEffect
/*    */   extends MobEffect {
/* 11 */   protected RaidOmenMobEffect(MobEffectCategory category, int color, ParticleOptions particleOptions) { super(category, color, particleOptions); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   public boolean shouldApplyEffectTickThisTick(int remainingDuration, int amplification) { return (remainingDuration == 1); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
/* 21 */     if (mob instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)mob; if (!mob.isSpectator()) {
/* 22 */         BlockPos raidOmenPosition = player.getRaidOmenPosition();
/*    */         
/* 24 */         if (raidOmenPosition != null) {
/* 25 */           level.getRaids().createOrExtendRaid(player, raidOmenPosition);
/* 26 */           player.clearRaidOmenPosition();
/* 27 */           return false;
/*    */         } 
/*    */       }  }
/* 30 */      return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\effect\RaidOmenMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */