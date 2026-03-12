/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Illusioner
/*     */   extends SpellcasterIllager
/*     */   implements RangedAttackMob
/*     */ {
/*     */   private static final int NUM_ILLUSIONS = 4;
/*     */   private static final int ILLUSION_TRANSITION_TICKS = 3;
/*     */   public static final int ILLUSION_SPREAD = 3;
/*     */   private int clientSideIllusionTicks;
/*     */   private final Vec3[][] clientSideIllusionOffsets;
/*     */   
/*     */   public Illusioner(EntityType<? extends Illusioner> type, Level level) {
/*  55 */     super(type, level);
/*     */     
/*  57 */     this.xpReward = 5;
/*     */     
/*  59 */     this.clientSideIllusionOffsets = new Vec3[2][4];
/*  60 */     for (int i = 0; i < 4; i++) {
/*  61 */       this.clientSideIllusionOffsets[0][i] = Vec3.ZERO;
/*  62 */       this.clientSideIllusionOffsets[1][i] = Vec3.ZERO;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  68 */     super.registerGoals();
/*     */     
/*  70 */     this.goalSelector.addGoal(0, new FloatGoal(this));
/*  71 */     this.goalSelector.addGoal(1, new SpellcasterIllager.SpellcasterCastingSpellGoal(this));
/*  72 */     this.goalSelector.addGoal(3, new AvoidEntityGoal(this, net.minecraft.world.entity.monster.creaking.Creaking.class, 8.0F, 1.0D, 1.2D));
/*  73 */     this.goalSelector.addGoal(4, new IllusionerMirrorSpellGoal());
/*  74 */     this.goalSelector.addGoal(5, new IllusionerBlindnessSpellGoal());
/*  75 */     this.goalSelector.addGoal(6, new RangedBowAttackGoal(this, 0.5D, 20, 15.0F));
/*  76 */     this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6D));
/*  77 */     this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 3.0F, 1.0F));
/*  78 */     this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, net.minecraft.world.entity.Mob.class, 8.0F));
/*     */     
/*  80 */     this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[] { net.minecraft.world.entity.raid.Raider.class })).setAlertOthers(new Class[0]));
/*  81 */     this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal(this, net.minecraft.world.entity.player.Player.class, true)).setUnseenMemoryTicks(300));
/*  82 */     this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal(this, net.minecraft.world.entity.npc.villager.AbstractVillager.class, false)).setUnseenMemoryTicks(300));
/*  83 */     this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal(this, net.minecraft.world.entity.animal.golem.IronGolem.class, false)).setUnseenMemoryTicks(300));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  87 */     return Monster.createMonsterAttributes()
/*  88 */       .add(Attributes.MOVEMENT_SPEED, 0.5D)
/*  89 */       .add(Attributes.FOLLOW_RANGE, 18.0D)
/*  90 */       .add(Attributes.MAX_HEALTH, 32.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*  95 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
/*     */     
/*  97 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 102 */     super.aiStep();
/*     */     
/* 104 */     if (level().isClientSide() && isInvisible()) {
/* 105 */       this.clientSideIllusionTicks--;
/* 106 */       if (this.clientSideIllusionTicks < 0) {
/* 107 */         this.clientSideIllusionTicks = 0;
/*     */       }
/*     */       
/* 110 */       if (this.hurtTime == 1 || this.tickCount % 1200 == 0) {
/* 111 */         this.clientSideIllusionTicks = 3;
/*     */         
/* 113 */         float minSpread = -6.0F;
/* 114 */         int spreadSpan = 13;
/*     */         
/* 116 */         for (int i = 0; i < 4; i++) {
/* 117 */           this.clientSideIllusionOffsets[0][i] = this.clientSideIllusionOffsets[1][i];
/* 118 */           this.clientSideIllusionOffsets[1][i] = new Vec3((-6.0F + this.random.nextInt(13)) * 0.5D, Math.max(0, this.random.nextInt(6) - 4), (-6.0F + this.random.nextInt(13)) * 0.5D);
/*     */         } 
/* 120 */         for (int i = 0; i < 16; i++) {
/* 121 */           level().addParticle(ParticleTypes.CLOUD, getRandomX(0.5D), getRandomY(), getZ(0.5D), 0.0D, 0.0D, 0.0D);
/*     */         }
/*     */         
/* 124 */         level().playLocalSound(getX(), getY(), getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, getSoundSource(), 1.0F, 1.0F, false);
/* 125 */       } else if (this.hurtTime == this.hurtDuration - 1) {
/* 126 */         this.clientSideIllusionTicks = 3;
/* 127 */         for (int i = 0; i < 4; i++) {
/* 128 */           this.clientSideIllusionOffsets[0][i] = this.clientSideIllusionOffsets[1][i];
/* 129 */           this.clientSideIllusionOffsets[1][i] = new Vec3(0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 137 */   public SoundEvent getCelebrateSound() { return SoundEvents.ILLUSIONER_AMBIENT; }
/*     */ 
/*     */   
/*     */   public Vec3[] getIllusionOffsets(float a) {
/* 141 */     if (this.clientSideIllusionTicks <= 0) {
/* 142 */       return this.clientSideIllusionOffsets[1];
/*     */     }
/* 144 */     double scale = ((this.clientSideIllusionTicks - a) / 3.0F);
/* 145 */     scale = Math.pow(scale, 0.25D);
/* 146 */     Vec3[] offsets = new Vec3[4];
/* 147 */     for (int i = 0; i < 4; i++) {
/* 148 */       offsets[i] = this.clientSideIllusionOffsets[1][i].scale(1.0D - scale).add(this.clientSideIllusionOffsets[0][i].scale(scale));
/*     */     }
/* 150 */     return offsets;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 155 */   protected SoundEvent getAmbientSound() { return SoundEvents.ILLUSIONER_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 160 */   protected SoundEvent getDeathSound() { return SoundEvents.ILLUSIONER_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 165 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ILLUSIONER_HURT; }
/*     */ 
/*     */   
/*     */   public void applyRaidBuffs(ServerLevel level, int wave, boolean isCaptain) {}
/*     */   
/* 170 */   protected SoundEvent getCastingSoundEvent() { return SoundEvents.ILLUSIONER_CAST_SPELL; }
/*     */ 
/*     */ 
/*     */   
/*     */   private class IllusionerMirrorSpellGoal
/*     */     extends SpellcasterIllager.SpellcasterUseSpellGoal
/*     */   {
/*     */     private IllusionerMirrorSpellGoal() {
/* 178 */       super(Illusioner.this);
/*     */     }
/*     */     public boolean canUse() {
/* 181 */       if (!super.canUse()) {
/* 182 */         return false;
/*     */       }
/* 184 */       if (Illusioner.this.hasEffect(MobEffects.INVISIBILITY)) {
/* 185 */         return false;
/*     */       }
/* 187 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 192 */     protected int getCastingTime() { return 20; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 197 */     protected int getCastingInterval() { return 340; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 202 */     protected void performSpellCasting() { Illusioner.this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 1200)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 207 */     protected SoundEvent getSpellPrepareSound() { return SoundEvents.ILLUSIONER_PREPARE_MIRROR; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 212 */     protected SpellcasterIllager.IllagerSpell getSpell() { return SpellcasterIllager.IllagerSpell.DISAPPEAR; } }
/*     */   
/*     */   private class IllusionerBlindnessSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
/*     */     private IllusionerBlindnessSpellGoal() {
/* 216 */       super(Illusioner.this);
/*     */     }
/*     */     private int lastTargetId;
/*     */     
/*     */     public boolean canUse() {
/* 221 */       if (!super.canUse()) {
/* 222 */         return false;
/*     */       }
/* 224 */       if (Illusioner.this.getTarget() == null) {
/* 225 */         return false;
/*     */       }
/* 227 */       if (Illusioner.this.getTarget().getId() == this.lastTargetId) {
/* 228 */         return false;
/*     */       }
/* 230 */       if (!getServerLevel(Illusioner.this).getCurrentDifficultyAt(Illusioner.this.blockPosition()).isHarderThan(Difficulty.NORMAL.ordinal())) {
/* 231 */         return false;
/*     */       }
/* 233 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 238 */       super.start();
/*     */       
/* 240 */       LivingEntity target = Illusioner.this.getTarget();
/* 241 */       if (target != null) {
/* 242 */         this.lastTargetId = target.getId();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 248 */     protected int getCastingTime() { return 20; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 253 */     protected int getCastingInterval() { return 180; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 258 */     protected void performSpellCasting() { Illusioner.this.getTarget().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 400), Illusioner.this); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 263 */     protected SoundEvent getSpellPrepareSound() { return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 268 */     protected SpellcasterIllager.IllagerSpell getSpell() { return SpellcasterIllager.IllagerSpell.BLINDNESS; }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity target, float power) {
/* 274 */     ItemStack bowItem = getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW));
/* 275 */     ItemStack projectile = getProjectile(bowItem);
/* 276 */     AbstractArrow arrow = ProjectileUtil.getMobArrow(this, projectile, power, bowItem);
/*     */     
/* 278 */     double xd = target.getX() - getX();
/* 279 */     double yd = target.getY(0.3333333333333333D) - arrow.getY();
/* 280 */     double zd = target.getZ() - getZ();
/* 281 */     double distanceToTarget = Math.sqrt(xd * xd + zd * zd);
/* 282 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 283 */       Projectile.spawnProjectileUsingShoot(arrow, serverLevel, projectile, xd, yd + distanceToTarget * 0.20000000298023224D, zd, 1.6F, (14 - serverLevel
/*     */ 
/*     */ 
/*     */           
/* 287 */           .getDifficulty().getId() * 4)); }
/*     */     
/* 289 */     playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractIllager.IllagerArmPose getArmPose() {
/* 294 */     if (isCastingSpell())
/* 295 */       return AbstractIllager.IllagerArmPose.SPELLCASTING; 
/* 296 */     if (isAggressive()) {
/* 297 */       return AbstractIllager.IllagerArmPose.BOW_AND_ARROW;
/*     */     }
/* 299 */     return AbstractIllager.IllagerArmPose.CROSSED;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\illager\Illusioner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */