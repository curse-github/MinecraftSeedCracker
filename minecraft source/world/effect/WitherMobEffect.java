/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public class WitherMobEffect
/*    */   extends MobEffect
/*    */ {
/*    */   public static final int DAMAGE_INTERVAL = 40;
/*    */   
/* 11 */   protected WitherMobEffect(MobEffectCategory category, int color) { super(category, color); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
/* 16 */     mob.hurtServer(level, mob.damageSources().wither(), 1.0F);
/* 17 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
/* 22 */     int interval = 40 >> amplification;
/* 23 */     if (interval > 0) {
/* 24 */       return (tickCount % interval == 0);
/*    */     }
/* 26 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\effect\WitherMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */