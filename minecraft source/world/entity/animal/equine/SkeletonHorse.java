/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class SkeletonHorse
/*     */   extends AbstractHorse {
/*  32 */   private final SkeletonTrapGoal skeletonTrapGoal = new SkeletonTrapGoal(this);
/*     */   
/*     */   private static final int TRAP_MAX_LIFE = 18000;
/*     */   private static final boolean DEFAULT_IS_TRAP = false;
/*     */   private static final int DEFAULT_TRAP_TIME = 0;
/*  37 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.SKELETON_HORSE.getDimensions()
/*  38 */     .withAttachments(EntityAttachments.builder()
/*  39 */       .attach(EntityAttachment.PASSENGER, 0.0F, EntityType.SKELETON_HORSE.getHeight() - 0.03125F, 0.0F))
/*     */     
/*  41 */     .scale(0.5F);
/*     */   
/*     */   private boolean isTrap = false;
/*  44 */   private int trapTime = 0;
/*     */ 
/*     */   
/*  47 */   public SkeletonHorse(EntityType<? extends SkeletonHorse> type, Level level) { super(type, level); }
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  51 */     return createBaseHorseAttributes()
/*  52 */       .add(Attributes.MAX_HEALTH, 15.0D)
/*  53 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
/*     */   }
/*     */   
/*     */   public static boolean checkSkeletonHorseSpawnRules(EntityType<? extends Animal> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/*  57 */     if (EntitySpawnReason.isSpawner(spawnReason)) {
/*  58 */       return (EntitySpawnReason.ignoresLightRequirements(spawnReason) || isBrightEnoughToSpawn(level, pos));
/*     */     }
/*  60 */     return Animal.checkAnimalSpawnRules(type, level, spawnReason, pos, random);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  65 */   protected void randomizeAttributes(RandomSource random) { Objects.requireNonNull(random); getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(generateJumpStrength(random::nextDouble)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addBehaviourGoals() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  75 */     if (isEyeInFluid(FluidTags.WATER)) {
/*  76 */       return SoundEvents.SKELETON_HORSE_AMBIENT_WATER;
/*     */     }
/*  78 */     return SoundEvents.SKELETON_HORSE_AMBIENT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   protected SoundEvent getDeathSound() { return SoundEvents.SKELETON_HORSE_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.SKELETON_HORSE_HURT; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/*  94 */     if (onGround()) {
/*  95 */       if (isVehicle()) {
/*  96 */         this.gallopSoundCounter++;
/*  97 */         if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0)
/*  98 */           return SoundEvents.SKELETON_HORSE_GALLOP_WATER; 
/*  99 */         if (this.gallopSoundCounter <= 5) {
/* 100 */           return SoundEvents.SKELETON_HORSE_STEP_WATER;
/*     */         }
/*     */       } else {
/* 103 */         return SoundEvents.SKELETON_HORSE_STEP_WATER;
/*     */       } 
/*     */     }
/* 106 */     return SoundEvents.SKELETON_HORSE_SWIM;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playSwimSound(float volume) {
/* 111 */     if (onGround()) {
/* 112 */       super.playSwimSound(0.3F);
/*     */     } else {
/* 114 */       super.playSwimSound(Math.min(0.1F, volume * 25.0F));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playJumpSound() {
/* 120 */     if (isInWater()) {
/* 121 */       playSound(SoundEvents.SKELETON_HORSE_JUMP_WATER, 0.4F, 1.0F);
/*     */     } else {
/* 123 */       super.playJumpSound();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 129 */   public EntityDimensions getDefaultDimensions(Pose pose) { return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 134 */     super.aiStep();
/*     */     
/* 136 */     if (isTrap() && this.trapTime++ >= 18000) {
/* 137 */       discard();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 143 */     super.addAdditionalSaveData(output);
/*     */     
/* 145 */     output.putBoolean("SkeletonTrap", isTrap());
/* 146 */     output.putInt("SkeletonTrapTime", this.trapTime);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 151 */     super.readAdditionalSaveData(input);
/*     */     
/* 153 */     setTrap(input.getBooleanOr("SkeletonTrap", false));
/* 154 */     this.trapTime = input.getIntOr("SkeletonTrapTime", 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 159 */   protected float getWaterSlowDown() { return 0.96F; }
/*     */ 
/*     */ 
/*     */   
/* 163 */   public boolean isTrap() { return this.isTrap; }
/*     */ 
/*     */   
/*     */   public void setTrap(boolean trap) {
/* 167 */     if (trap == this.isTrap) {
/*     */       return;
/*     */     }
/*     */     
/* 171 */     this.isTrap = trap;
/* 172 */     if (trap) {
/* 173 */       this.goalSelector.addGoal(1, this.skeletonTrapGoal);
/*     */     } else {
/* 175 */       this.goalSelector.removeGoal(this.skeletonTrapGoal);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 181 */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) { return (AgeableMob)EntityType.SKELETON_HORSE.create(level, EntitySpawnReason.BREEDING); }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 186 */     if (!isTamed()) {
/* 187 */       return InteractionResult.PASS;
/*     */     }
/* 189 */     return super.mobInteract(player, hand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 194 */   public boolean canUseSlot(EquipmentSlot slot) { return true; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\equine\SkeletonHorse.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */