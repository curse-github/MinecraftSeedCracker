/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Endermite
/*     */   extends Monster {
/*     */   private static final int MAX_LIFE = 2400;
/*     */   private static final int DEFAULT_LIFE = 0;
/*  33 */   private int life = 0;
/*     */   
/*     */   public Endermite(EntityType<? extends Endermite> type, Level level) {
/*  36 */     super(type, level);
/*  37 */     this.xpReward = 3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  42 */     this.goalSelector.addGoal(1, new FloatGoal(this));
/*  43 */     this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, level()));
/*  44 */     this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
/*  45 */     this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
/*  46 */     this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
/*  47 */     this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
/*     */     
/*  49 */     this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
/*  50 */     this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  54 */     return Monster.createMonsterAttributes()
/*  55 */       .add(Attributes.MAX_HEALTH, 8.0D)
/*  56 */       .add(Attributes.MOVEMENT_SPEED, 0.25D)
/*  57 */       .add(Attributes.ATTACK_DAMAGE, 2.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  62 */   protected Entity.MovementEmission getMovementEmission() { return Entity.MovementEmission.EVENTS; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   protected SoundEvent getAmbientSound() { return SoundEvents.ENDERMITE_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ENDERMITE_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   protected SoundEvent getDeathSound() { return SoundEvents.ENDERMITE_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.ENDERMITE_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  87 */     super.readAdditionalSaveData(input);
/*  88 */     this.life = input.getIntOr("Lifetime", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  93 */     super.addAdditionalSaveData(output);
/*  94 */     output.putInt("Lifetime", this.life);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 100 */     this.yBodyRot = getYRot();
/*     */     
/* 102 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setYBodyRot(float yBodyRot) {
/* 107 */     setYRot(yBodyRot);
/* 108 */     super.setYBodyRot(yBodyRot);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 113 */     super.aiStep();
/*     */     
/* 115 */     if (level().isClientSide()) {
/* 116 */       for (int i = 0; i < 2; i++) {
/* 117 */         level().addParticle(ParticleTypes.PORTAL, getRandomX(0.5D), getRandomY(), getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
/*     */       }
/*     */     } else {
/* 120 */       if (!isPersistenceRequired()) {
/* 121 */         this.life++;
/*     */       }
/*     */       
/* 124 */       if (this.life >= 2400) {
/* 125 */         discard();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean checkEndermiteSpawnRules(EntityType<Endermite> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 131 */     if (!checkAnyLightMonsterSpawnRules(type, level, spawnReason, pos, random)) {
/* 132 */       return false;
/*     */     }
/*     */     
/* 135 */     if (EntitySpawnReason.isSpawner(spawnReason)) {
/* 136 */       return true;
/*     */     }
/*     */     
/* 139 */     Player nearestPlayer = level.getNearestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 5.0D, true);
/* 140 */     return (nearestPlayer == null);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Endermite.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */