/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.raid.Raid;
/*    */ 
/*    */ class BadOmenMobEffect
/*    */   extends MobEffect
/*    */ {
/* 12 */   protected BadOmenMobEffect(MobEffectCategory category, int color) { super(category, color); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public boolean shouldApplyEffectTickThisTick(int remainingDuration, int amplification) { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
/* 22 */     if (mob instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)mob; if (!player.isSpectator() && 
/* 23 */         level.getDifficulty() != Difficulty.PEACEFUL && level.isVillage(player.blockPosition())) {
/* 24 */         Raid raid = level.getRaidAt(player.blockPosition());
/*    */         
/* 26 */         if (raid == null || raid.getRaidOmenLevel() < raid.getMaxRaidOmenLevel()) {
/* 27 */           player.addEffect(new MobEffectInstance(MobEffects.RAID_OMEN, 600, amplification));
/* 28 */           player.setRaidOmenPosition(player.blockPosition());
/* 29 */           return false;
/*    */         } 
/*    */       }  }
/*    */     
/* 33 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\effect\BadOmenMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */