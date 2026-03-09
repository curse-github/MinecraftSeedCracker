/*    */ package net.minecraft.world.entity.monster.hoglin;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public interface HoglinBase
/*    */ {
/*    */   public static final int ATTACK_ANIMATION_DURATION = 10;
/*    */   public static final float PROBABILITY_OF_SPAWNING_AS_BABY = 0.2F;
/*    */   
/*    */   int getAttackAnimationRemainingTicks();
/*    */   
/*    */   static boolean hurtAndThrowTarget(ServerLevel level, LivingEntity body, LivingEntity target) {
/* 19 */     float actualDamage, attackDamage = (float)body.getAttributeValue(Attributes.ATTACK_DAMAGE);
/* 20 */     if (!body.isBaby() && (int)attackDamage > 0) {
/* 21 */       actualDamage = attackDamage / 2.0F + level.random.nextInt((int)attackDamage);
/*    */     } else {
/* 23 */       actualDamage = attackDamage;
/*    */     } 
/*    */     
/* 26 */     DamageSource damageSource = body.damageSources().mobAttack(body);
/* 27 */     boolean wasHurt = target.hurtServer(level, damageSource, actualDamage);
/* 28 */     if (wasHurt) {
/* 29 */       EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
/* 30 */       if (!body.isBaby()) {
/* 31 */         throwTarget(body, target);
/*    */       }
/*    */     } 
/* 34 */     return wasHurt;
/*    */   }
/*    */   
/*    */   static void throwTarget(LivingEntity body, LivingEntity target) {
/* 38 */     double knockbackPower = body.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
/* 39 */     double knockbackResistance = target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
/* 40 */     double effectiveKnockbackPower = knockbackPower - knockbackResistance;
/* 41 */     if (effectiveKnockbackPower <= 0.0D) {
/*    */       return;
/*    */     }
/*    */     
/* 45 */     double xd = target.getX() - body.getX();
/* 46 */     double zd = target.getZ() - body.getZ();
/* 47 */     float horizontalPushAngle = ((body.level()).random.nextInt(21) - 10);
/* 48 */     double horizontalScale = effectiveKnockbackPower * ((body.level()).random.nextFloat() * 0.5F + 0.2F);
/* 49 */     Vec3 horizontalPushVector = (new Vec3(xd, 0.0D, zd)).normalize().scale(horizontalScale).yRot(horizontalPushAngle);
/*    */     
/* 51 */     double verticalScale = effectiveKnockbackPower * (body.level()).random.nextFloat() * 0.5D;
/* 52 */     target.push(horizontalPushVector.x, verticalScale, horizontalPushVector.z);
/* 53 */     target.hurtMarked = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\hoglin\HoglinBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */