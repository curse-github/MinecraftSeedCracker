/*     */ package net.minecraft.world.entity.projectile.hurtingprojectile;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.boss.wither.WitherBoss;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class WitherSkull
/*     */   extends AbstractHurtingProjectile {
/*  30 */   private static final EntityDataAccessor<Boolean> DATA_DANGEROUS = SynchedEntityData.defineId(WitherSkull.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final boolean DEFAULT_DANGEROUS = false;
/*     */   
/*  34 */   public WitherSkull(EntityType<? extends WitherSkull> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*  38 */   public WitherSkull(Level level, LivingEntity mob, Vec3 direction) { super(EntityType.WITHER_SKULL, mob, direction, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   protected float getInertia() { return isDangerous() ? 0.73F : super.getInertia(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   public boolean isOnFire() { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState block, FluidState fluid, float resistance) {
/*  53 */     if (isDangerous() && WitherBoss.canDestroy(block)) {
/*  54 */       return Math.min(0.8F, resistance);
/*     */     }
/*     */     
/*  57 */     return resistance;
/*     */   }
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/*     */     boolean wasHurt;
/*     */     ServerLevel serverLevel;
/*  62 */     super.onHitEntity(hitResult);
/*     */     
/*  64 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*     */     else
/*     */     { return; }
/*  67 */      Entity entity = hitResult.getEntity();
/*  68 */     Entity owner = getOwner();
/*     */     
/*  70 */     if (owner instanceof LivingEntity) { LivingEntity livingOwner = (LivingEntity)owner;
/*  71 */       DamageSource damageSource = damageSources().witherSkull(this, livingOwner);
/*  72 */       wasHurt = entity.hurtServer(serverLevel, damageSource, 8.0F);
/*  73 */       if (wasHurt) {
/*  74 */         if (entity.isAlive()) {
/*  75 */           EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
/*     */         } else {
/*  77 */           livingOwner.heal(5.0F);
/*     */         } 
/*     */       } }
/*     */     else
/*  81 */     { wasHurt = entity.hurtServer(serverLevel, damageSources().magic(), 5.0F); }
/*     */     
/*  83 */     if (wasHurt && entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/*  84 */       int witherSeconds = 0;
/*  85 */       if (level().getDifficulty() == Difficulty.NORMAL) {
/*  86 */         witherSeconds = 10;
/*  87 */       } else if (level().getDifficulty() == Difficulty.HARD) {
/*  88 */         witherSeconds = 40;
/*     */       } 
/*  90 */       if (witherSeconds > 0) {
/*  91 */         livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * witherSeconds, 1), getEffectSource());
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHit(HitResult hitResult) {
/*  98 */     super.onHit(hitResult);
/*  99 */     if (!level().isClientSide()) {
/* 100 */       level().explode(this, getX(), getY(), getZ(), 1.0F, false, Level.ExplosionInteraction.MOB);
/* 101 */       discard();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 107 */   protected void defineSynchedData(SynchedEntityData.Builder entityData) { entityData.define(DATA_DANGEROUS, Boolean.valueOf(false)); }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public boolean isDangerous() { return ((Boolean)this.entityData.get(DATA_DANGEROUS)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public void setDangerous(boolean value) { this.entityData.set(DATA_DANGEROUS, Boolean.valueOf(value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   protected boolean shouldBurn() { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 125 */     super.addAdditionalSaveData(output);
/* 126 */     output.putBoolean("dangerous", isDangerous());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 131 */     super.readAdditionalSaveData(input);
/* 132 */     setDangerous(input.getBooleanOr("dangerous", false));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\WitherSkull.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */