/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ class HealOrHarmMobEffect
/*    */   extends InstantenousMobEffect {
/*    */   private final boolean isHarm;
/*    */   
/*    */   public HealOrHarmMobEffect(MobEffectCategory category, int color, boolean isHarm) {
/* 12 */     super(category, color);
/* 13 */     this.isHarm = isHarm;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
/* 18 */     if (this.isHarm == mob.isInvertedHealAndHarm()) {
/* 19 */       mob.heal(Math.max(4 << amplification, 0));
/*    */     } else {
/* 21 */       mob.hurtServer(level, mob.damageSources().magic(), (6 << amplification));
/*    */     } 
/* 23 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyInstantenousEffect(ServerLevel serverLevel, Entity source, Entity owner, LivingEntity mob, int amplification, double scale) {
/* 28 */     if (this.isHarm == mob.isInvertedHealAndHarm()) {
/* 29 */       int amount = (int)(scale * (4 << amplification) + 0.5D);
/* 30 */       mob.heal(amount);
/*    */     } else {
/* 32 */       int amount = (int)(scale * (6 << amplification) + 0.5D);
/* 33 */       if (source == null) {
/* 34 */         mob.hurtServer(serverLevel, mob.damageSources().magic(), amount);
/*    */       } else {
/* 36 */         mob.hurtServer(serverLevel, mob.damageSources().indirectMagic(source, owner), amount);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\effect\HealOrHarmMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */