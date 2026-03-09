/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.StringUtil;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public final class MobEffectUtil
/*    */ {
/*    */   public static Component formatDuration(MobEffectInstance instance, float scale, float tickrate) {
/* 18 */     if (instance.isInfiniteDuration()) {
/* 19 */       return Component.translatable("effect.duration.infinite");
/*    */     }
/* 21 */     int duration = Mth.floor(instance.getDuration() * scale);
/* 22 */     return Component.literal(StringUtil.formatTickDuration(duration, tickrate));
/*    */   }
/*    */ 
/*    */   
/* 26 */   public static boolean hasDigSpeed(LivingEntity mob) { return (mob.hasEffect(MobEffects.HASTE) || mob.hasEffect(MobEffects.CONDUIT_POWER)); }
/*    */ 
/*    */   
/*    */   public static int getDigSpeedAmplification(LivingEntity mob) {
/* 30 */     int a = 0, b = 0;
/* 31 */     if (mob.hasEffect(MobEffects.HASTE)) {
/* 32 */       a = mob.getEffect(MobEffects.HASTE).getAmplifier();
/*    */     }
/* 34 */     if (mob.hasEffect(MobEffects.CONDUIT_POWER)) {
/* 35 */       b = mob.getEffect(MobEffects.CONDUIT_POWER).getAmplifier();
/*    */     }
/*    */     
/* 38 */     return Math.max(a, b);
/*    */   }
/*    */ 
/*    */   
/* 42 */   public static boolean hasWaterBreathing(LivingEntity mob) { return (mob.hasEffect(MobEffects.WATER_BREATHING) || mob.hasEffect(MobEffects.CONDUIT_POWER) || mob.hasEffect(MobEffects.BREATH_OF_THE_NAUTILUS)); }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public static boolean shouldEffectsRefillAirsupply(LivingEntity mob) { return (!mob.hasEffect(MobEffects.BREATH_OF_THE_NAUTILUS) || mob.hasEffect(MobEffects.WATER_BREATHING) || mob.hasEffect(MobEffects.CONDUIT_POWER)); }
/*    */ 
/*    */   
/*    */   public static List<ServerPlayer> addEffectToPlayersAround(ServerLevel level, Entity source, Vec3 position, double radius, MobEffectInstance effectInstance, int displayEffectLimit) {
/* 50 */     Holder<MobEffect> effect = effectInstance.getEffect();
/* 51 */     List<ServerPlayer> players = level.getPlayers(input -> 
/* 52 */         (input.gameMode.isSurvival() && (source == null || 
/* 53 */         !source.isAlliedTo(input)) && position
/* 54 */         .closerThan(input.position(), radius) && (
/*    */         
/* 56 */         !input.hasEffect(effect) || input
/* 57 */         .getEffect(effect).getAmplifier() < effectInstance.getAmplifier() || input
/* 58 */         .getEffect(effect).endsWithin(displayEffectLimit - 1))));
/*    */ 
/*    */ 
/*    */     
/* 62 */     players.forEach(player -> player.addEffect(new MobEffectInstance(effectInstance), source));
/*    */     
/* 64 */     return players;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\effect\MobEffectUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */