/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ class WindChargedMobEffect
/*    */   extends MobEffect {
/* 14 */   protected WindChargedMobEffect(MobEffectCategory category, int color) { super(category, color, ParticleTypes.SMALL_GUST); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onMobRemoved(ServerLevel level, LivingEntity mob, int amplifier, Entity.RemovalReason reason) {
/* 19 */     if (reason == Entity.RemovalReason.KILLED) {
/* 20 */       double x = mob.getX();
/* 21 */       double y = mob.getY() + (mob.getBbHeight() / 2.0F);
/* 22 */       double z = mob.getZ();
/* 23 */       float gustStrength = 3.0F + mob.getRandom().nextFloat() * 2.0F;
/* 24 */       level.explode(mob, null, AbstractWindCharge.EXPLOSION_DAMAGE_CALCULATOR, x, y, z, gustStrength, false, Level.ExplosionInteraction.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE, WeightedList.of(), SoundEvents.BREEZE_WIND_CHARGE_BURST);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\effect\WindChargedMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */