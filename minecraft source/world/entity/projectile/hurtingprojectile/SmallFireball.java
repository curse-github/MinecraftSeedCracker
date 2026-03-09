/*    */ package net.minecraft.world.entity.projectile.hurtingprojectile;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BaseFireBlock;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class SmallFireball
/*    */   extends Fireball
/*    */ {
/* 21 */   public SmallFireball(EntityType<? extends SmallFireball> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public SmallFireball(Level level, LivingEntity mob, Vec3 direction) { super(EntityType.SMALL_FIREBALL, mob, direction, level); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public SmallFireball(Level level, double x, double y, double z, Vec3 direction) { super(EntityType.SMALL_FIREBALL, x, y, z, direction, level); }
/*    */ 
/*    */   
/*    */   protected void onHitEntity(EntityHitResult hitResult) {
/*    */     ServerLevel serverLevel;
/* 34 */     super.onHitEntity(hitResult);
/*    */     
/* 36 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*    */     else
/*    */     { return; }
/* 39 */      Entity entity = hitResult.getEntity();
/* 40 */     Entity owner = getOwner();
/* 41 */     int remainingFireTicks = entity.getRemainingFireTicks();
/* 42 */     entity.igniteForSeconds(5.0F);
/* 43 */     DamageSource damageSource = damageSources().fireball(this, owner);
/* 44 */     if (!entity.hurtServer(serverLevel, damageSource, 5.0F)) {
/*    */ 
/*    */       
/* 47 */       entity.setRemainingFireTicks(remainingFireTicks);
/*    */     } else {
/* 49 */       EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void onHitBlock(BlockHitResult hitResult) {
/*    */     ServerLevel serverLevel;
/* 55 */     super.onHitBlock(hitResult);
/* 56 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*    */     else
/*    */     { return; }
/* 59 */      Entity owner = getOwner();
/* 60 */     if (!(owner instanceof net.minecraft.world.entity.Mob) || ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 61 */       BlockPos pos = hitResult.getBlockPos().relative(hitResult.getDirection());
/* 62 */       if (level().isEmptyBlock(pos)) {
/* 63 */         level().setBlockAndUpdate(pos, BaseFireBlock.getState(level(), pos));
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult hitResult) {
/* 70 */     super.onHit(hitResult);
/* 71 */     if (!level().isClientSide())
/* 72 */       discard(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\SmallFireball.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */