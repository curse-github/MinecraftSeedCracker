/*     */ package net.minecraft.world.entity.animal.squid;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class GlowSquid
/*     */   extends Squid {
/*  26 */   private static final EntityDataAccessor<Integer> DATA_DARK_TICKS_REMAINING = SynchedEntityData.defineId(GlowSquid.class, EntityDataSerializers.INT);
/*     */   
/*     */   private static final int DEFAULT_DARK_TICKS_REMAINING = 0;
/*     */   
/*  30 */   public GlowSquid(EntityType<? extends GlowSquid> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  35 */   protected ParticleOptions getInkParticle() { return ParticleTypes.GLOW_SQUID_INK; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  40 */     super.defineSynchedData(entityData);
/*  41 */     entityData.define(DATA_DARK_TICKS_REMAINING, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  46 */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) { return (AgeableMob)EntityType.GLOW_SQUID.create(level, EntitySpawnReason.BREEDING); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   protected SoundEvent getSquirtSound() { return SoundEvents.GLOW_SQUID_SQUIRT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   protected SoundEvent getAmbientSound() { return SoundEvents.GLOW_SQUID_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.GLOW_SQUID_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   protected SoundEvent getDeathSound() { return SoundEvents.GLOW_SQUID_DEATH; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  71 */     super.addAdditionalSaveData(output);
/*  72 */     output.putInt("DarkTicksRemaining", getDarkTicksRemaining());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  77 */     super.readAdditionalSaveData(input);
/*  78 */     setDarkTicks(input.getIntOr("DarkTicksRemaining", 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  83 */     super.aiStep();
/*     */     
/*  85 */     int darkTicks = getDarkTicksRemaining();
/*  86 */     if (darkTicks > 0) {
/*  87 */       setDarkTicks(darkTicks - 1);
/*     */     }
/*     */     
/*  90 */     level().addParticle(ParticleTypes.GLOW, getRandomX(0.6D), getRandomY(), getRandomZ(0.6D), 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*  95 */     boolean hurt = super.hurtServer(level, source, damage);
/*  96 */     if (hurt) {
/*  97 */       setDarkTicks(100);
/*     */     }
/*     */     
/* 100 */     return hurt;
/*     */   }
/*     */ 
/*     */   
/* 104 */   private void setDarkTicks(int ticks) { this.entityData.set(DATA_DARK_TICKS_REMAINING, Integer.valueOf(ticks)); }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public int getDarkTicksRemaining() { return ((Integer)this.entityData.get(DATA_DARK_TICKS_REMAINING)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public static boolean checkGlowSquidSpawnRules(EntityType<? extends LivingEntity> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) { return (pos.getY() <= level.getSeaLevel() - 33 && level.getRawBrightness(pos, 0) == 0 && level.getBlockState(pos).is(Blocks.WATER)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\squid\GlowSquid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */