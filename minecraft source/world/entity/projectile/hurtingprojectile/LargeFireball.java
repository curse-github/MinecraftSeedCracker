/*    */ package net.minecraft.world.entity.projectile.hurtingprojectile;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class LargeFireball
/*    */   extends Fireball {
/*    */   private static final byte DEFAULT_EXPLOSION_POWER = 1;
/* 20 */   private int explosionPower = 1;
/*    */ 
/*    */   
/* 23 */   public LargeFireball(EntityType<? extends LargeFireball> type, Level level) { super(type, level); }
/*    */ 
/*    */   
/*    */   public LargeFireball(Level level, LivingEntity mob, Vec3 direction, int explosionPower) {
/* 27 */     super(EntityType.FIREBALL, mob, direction, level);
/* 28 */     this.explosionPower = explosionPower;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult hitResult) {
/* 33 */     super.onHit(hitResult);
/* 34 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 35 */       boolean grief = ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue();
/* 36 */       level().explode(this, getX(), getY(), getZ(), this.explosionPower, grief, Level.ExplosionInteraction.MOB);
/* 37 */       discard(); }
/*    */   
/*    */   }
/*    */   
/*    */   protected void onHitEntity(EntityHitResult hitResult) {
/*    */     ServerLevel serverLevel;
/* 43 */     super.onHitEntity(hitResult);
/*    */     
/* 45 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*    */     else
/*    */     { return; }
/* 48 */      Entity entity = hitResult.getEntity();
/* 49 */     Entity owner = getOwner();
/* 50 */     DamageSource damageSource = damageSources().fireball(this, owner);
/* 51 */     entity.hurtServer(serverLevel, damageSource, 6.0F);
/* 52 */     EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(ValueOutput output) {
/* 57 */     super.addAdditionalSaveData(output);
/* 58 */     output.putByte("ExplosionPower", (byte)this.explosionPower);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readAdditionalSaveData(ValueInput input) {
/* 63 */     super.readAdditionalSaveData(input);
/* 64 */     this.explosionPower = input.getByteOr("ExplosionPower", (byte)1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\LargeFireball.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */