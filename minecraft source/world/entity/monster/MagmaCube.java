/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class MagmaCube
/*     */   extends Slime {
/*  24 */   public MagmaCube(EntityType<? extends MagmaCube> type, Level level) { super(type, level); }
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  28 */     return Monster.createMonsterAttributes()
/*  29 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
/*     */   }
/*     */ 
/*     */   
/*  33 */   public static boolean checkMagmaCubeSpawnRules(EntityType<MagmaCube> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) { return (level.getDifficulty() != Difficulty.PEACEFUL); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(int size, boolean updateHealth) {
/*  38 */     super.setSize(size, updateHealth);
/*  39 */     getAttribute(Attributes.ARMOR).setBaseValue((size * 3));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  44 */   public float getLightLevelDependentMagicValue() { return 1.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   protected ParticleOptions getParticleType() { return ParticleTypes.FLAME; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   public boolean isOnFire() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   protected int getJumpDelay() { return super.getJumpDelay() * 4; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   protected void decreaseSquish() { this.targetSquish *= 0.9F; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void jumpFromGround() {
/*  69 */     Vec3 movement = getDeltaMovement();
/*  70 */     float sizeJumpBoostPower = getSize() * 0.1F;
/*  71 */     setDeltaMovement(movement.x, (getJumpPower() + sizeJumpBoostPower), movement.z);
/*  72 */     this.needsSync = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void jumpInLiquid(TagKey<Fluid> type) {
/*  77 */     if (type == FluidTags.LAVA) {
/*  78 */       Vec3 movement = getDeltaMovement();
/*  79 */       setDeltaMovement(movement.x, (0.22F + getSize() * 0.05F), movement.z);
/*  80 */       this.needsSync = true;
/*     */     } else {
/*  82 */       super.jumpInLiquid(type);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  88 */   protected boolean isDealsDamage() { return isEffectiveAi(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   protected float getAttackDamage() { return super.getAttackDamage() + 2.0F; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/*  98 */     if (isTiny()) {
/*  99 */       return SoundEvents.MAGMA_CUBE_HURT_SMALL;
/*     */     }
/* 101 */     return SoundEvents.MAGMA_CUBE_HURT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 107 */     if (isTiny()) {
/* 108 */       return SoundEvents.MAGMA_CUBE_DEATH_SMALL;
/*     */     }
/* 110 */     return SoundEvents.MAGMA_CUBE_DEATH;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getSquishSound() {
/* 116 */     if (isTiny()) {
/* 117 */       return SoundEvents.MAGMA_CUBE_SQUISH_SMALL;
/*     */     }
/* 119 */     return SoundEvents.MAGMA_CUBE_SQUISH;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   protected SoundEvent getJumpSound() { return SoundEvents.MAGMA_CUBE_JUMP; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\MagmaCube.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */