/*     */ package net.minecraft.world.entity.monster.breeze;
/*     */ 
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.debug.DebugBreezeInfo;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debug.DebugValueSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.ProjectileDeflection;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class Breeze
/*     */   extends Monster
/*     */ {
/*     */   private static final int SLIDE_PARTICLES_AMOUNT = 20;
/*     */   private static final int IDLE_PARTICLES_AMOUNT = 1;
/*     */   private static final int JUMP_DUST_PARTICLES_AMOUNT = 20;
/*     */   private static final int JUMP_TRAIL_PARTICLES_AMOUNT = 3;
/*     */   private static final int JUMP_TRAIL_DURATION_TICKS = 5;
/*     */   private static final int JUMP_CIRCLE_DISTANCE_Y = 10;
/*     */   private static final float FALL_DISTANCE_SOUND_TRIGGER_THRESHOLD = 3.0F;
/*     */   private static final int WHIRL_SOUND_FREQUENCY_MIN = 1;
/*     */   private static final int WHIRL_SOUND_FREQUENCY_MAX = 80;
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  54 */     return Mob.createMobAttributes()
/*  55 */       .add(Attributes.MOVEMENT_SPEED, 0.6299999952316284D)
/*  56 */       .add(Attributes.MAX_HEALTH, 30.0D)
/*  57 */       .add(Attributes.FOLLOW_RANGE, 24.0D)
/*  58 */       .add(Attributes.ATTACK_DAMAGE, 3.0D);
/*     */   }
/*     */ 
/*     */   
/*  62 */   public AnimationState idle = new AnimationState();
/*  63 */   public AnimationState slide = new AnimationState();
/*  64 */   public AnimationState slideBack = new AnimationState();
/*  65 */   public AnimationState longJump = new AnimationState();
/*  66 */   public AnimationState shoot = new AnimationState();
/*  67 */   public AnimationState inhale = new AnimationState();
/*     */   
/*  69 */   private int jumpTrailStartedTick = 0;
/*  70 */   private int soundTick = 0;
/*     */   
/*     */   private static final ProjectileDeflection PROJECTILE_DEFLECTION = (projectile, entity, random) -> {
/*  73 */       entity.level().playSound(null, entity, SoundEvents.BREEZE_DEFLECT, entity.getSoundSource(), 1.0F, 1.0F);
/*  74 */       ProjectileDeflection.REVERSE.deflect(projectile, entity, random);
/*     */     };
/*     */   
/*     */   public Breeze(EntityType<? extends Monster> type, Level level) {
/*  78 */     super(type, level);
/*  79 */     setPathfindingMalus(PathType.DANGER_TRAPDOOR, -1.0F);
/*  80 */     setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
/*  81 */     this.xpReward = 10;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  86 */   protected Brain<?> makeBrain(Dynamic<?> input) { return BreezeAi.makeBrain(this, brainProvider().makeBrain(input)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public Brain<Breeze> getBrain() { return super.getBrain(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   protected Brain.Provider<Breeze> brainProvider() { return Brain.provider(BreezeAi.MEMORY_TYPES, BreezeAi.SENSOR_TYPES); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 101 */     if (level().isClientSide() && DATA_POSE.equals(accessor)) {
/*     */       
/* 103 */       resetAnimations();
/*     */       
/* 105 */       Pose pose = getPose();
/* 106 */       switch (pose) { case SHOOTING:
/* 107 */           this.shoot.startIfStopped(this.tickCount); break;
/* 108 */         case INHALING: this.inhale.startIfStopped(this.tickCount); break;
/* 109 */         case SLIDING: this.slide.startIfStopped(this.tickCount);
/*     */           break; }
/*     */     
/*     */     } 
/* 113 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   private void resetAnimations() {
/* 117 */     this.shoot.stop();
/* 118 */     this.idle.stop();
/* 119 */     this.inhale.stop();
/* 120 */     this.longJump.stop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 125 */     Pose pose = getPose();
/* 126 */     switch (pose) { case SLIDING:
/* 127 */         emitGroundParticles(20); break;
/*     */       case SHOOTING: case INHALING: case STANDING:
/* 129 */         resetJumpTrail().emitGroundParticles(1 + getRandom().nextInt(1)); break;
/*     */       case LONG_JUMPING:
/* 131 */         this.longJump.startIfStopped(this.tickCount);
/* 132 */         emitJumpTrailParticles();
/*     */         break; }
/*     */ 
/*     */     
/* 136 */     this.idle.startIfStopped(this.tickCount);
/*     */     
/* 138 */     if (pose != Pose.SLIDING && this.slide.isStarted()) {
/* 139 */       this.slideBack.start(this.tickCount);
/* 140 */       this.slide.stop();
/*     */     } 
/*     */     
/* 143 */     this.soundTick = (this.soundTick == 0) ? this.random.nextIntBetweenInclusive(1, 80) : (this.soundTick - 1);
/* 144 */     if (this.soundTick == 0) {
/* 145 */       playWhirlSound();
/*     */     }
/*     */     
/* 148 */     super.tick();
/*     */   }
/*     */   
/*     */   public Breeze resetJumpTrail() {
/* 152 */     this.jumpTrailStartedTick = 0;
/* 153 */     return this;
/*     */   }
/*     */   
/*     */   public void emitJumpTrailParticles() {
/* 157 */     if (++this.jumpTrailStartedTick > 5) {
/*     */       return;
/*     */     }
/*     */     
/* 161 */     BlockState ground = !getInBlockState().isAir() ? getInBlockState() : getBlockStateOn();
/* 162 */     Vec3 movement = getDeltaMovement();
/* 163 */     Vec3 centered = position().add(movement).add(0.0D, 0.10000000149011612D, 0.0D);
/*     */     
/* 165 */     for (int i = 0; i < 3; i++) {
/* 166 */       level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, ground), centered.x, centered.y, centered.z, 0.0D, 0.0D, 0.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   public void emitGroundParticles(int amount) {
/* 171 */     if (isPassenger()) {
/*     */       return;
/*     */     }
/*     */     
/* 175 */     Vec3 boundingBoxCenter = getBoundingBox().getCenter();
/* 176 */     Vec3 position = new Vec3(boundingBoxCenter.x, (position()).y, boundingBoxCenter.z);
/*     */     
/* 178 */     BlockState ground = !getInBlockState().isAir() ? getInBlockState() : getBlockStateOn();
/*     */     
/* 180 */     if (ground.getRenderShape() == RenderShape.INVISIBLE) {
/*     */       return;
/*     */     }
/*     */     
/* 184 */     for (int i = 0; i < amount; i++) {
/* 185 */       level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, ground), position.x, position.y, position.z, 0.0D, 0.0D, 0.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void playAmbientSound() {
/* 192 */     if (getTarget() != null && onGround()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 197 */     level().playLocalSound(this, getAmbientSound(), getSoundSource(), 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void playWhirlSound() {
/* 201 */     float pitch = 0.7F + 0.4F * this.random.nextFloat();
/* 202 */     float volume = 0.8F + 0.2F * this.random.nextFloat();
/*     */     
/* 204 */     level().playLocalSound(this, SoundEvents.BREEZE_WHIRL, getSoundSource(), volume, pitch);
/*     */   }
/*     */ 
/*     */   
/*     */   public ProjectileDeflection deflection(Projectile projectile) {
/* 209 */     if (projectile.getType() == EntityType.BREEZE_WIND_CHARGE || projectile.getType() == EntityType.WIND_CHARGE) {
/* 210 */       return ProjectileDeflection.NONE;
/*     */     }
/*     */     
/* 213 */     return getType().is(EntityTypeTags.DEFLECTS_PROJECTILES) ? PROJECTILE_DEFLECTION : ProjectileDeflection.NONE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 218 */   public SoundSource getSoundSource() { return SoundSource.HOSTILE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 223 */   protected SoundEvent getDeathSound() { return SoundEvents.BREEZE_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 228 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.BREEZE_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 233 */   protected SoundEvent getAmbientSound() { return onGround() ? SoundEvents.BREEZE_IDLE_GROUND : SoundEvents.BREEZE_IDLE_AIR; }
/*     */ 
/*     */ 
/*     */   
/* 237 */   public Optional<LivingEntity> getHurtBy() { return getBrain().getMemory(MemoryModuleType.HURT_BY)
/* 238 */       .map(DamageSource::getEntity)
/* 239 */       .filter(entity -> entity instanceof LivingEntity)
/* 240 */       .map(entity -> (LivingEntity)entity); }
/*     */ 
/*     */   
/*     */   public boolean withinInnerCircleRange(Vec3 target) {
/* 244 */     Vec3 ourPosition = blockPosition().getCenter();
/* 245 */     return target.closerThan(ourPosition, 4.0D, 10.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 250 */     ProfilerFiller profiler = Profiler.get();
/* 251 */     profiler.push("breezeBrain");
/* 252 */     getBrain().tick(level, this);
/*     */     
/* 254 */     profiler.popPush("breezeActivityUpdate");
/* 255 */     BreezeAi.updateActivity(this);
/* 256 */     profiler.pop();
/*     */     
/* 258 */     super.customServerAiStep(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 263 */   public boolean canAttackType(EntityType<?> targetType) { return (targetType == EntityType.PLAYER || targetType == EntityType.IRON_GOLEM); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 268 */   public int getMaxHeadYRot() { return 30; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 273 */   public int getHeadRotSpeed() { return 25; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 278 */   public double getFiringYPosition() { return getY() + (getBbHeight() / 2.0F) + 0.30000001192092896D; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 283 */   public boolean isInvulnerableTo(ServerLevel level, DamageSource source) { return (source.getEntity() instanceof Breeze || super.isInvulnerableTo(level, source)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 289 */   public double getFluidJumpThreshold() { return getEyeHeight(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
/* 295 */     if (fallDistance > 3.0D) {
/* 296 */       playSound(SoundEvents.BREEZE_LAND, 1.0F, 1.0F);
/*     */     }
/* 298 */     return super.causeFallDamage(fallDistance, damageModifier, damageSource);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 303 */   protected Entity.MovementEmission getMovementEmission() { return Entity.MovementEmission.EVENTS; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 308 */   public LivingEntity getTarget() { return getTargetFromBrain(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerDebugValues(ServerLevel level, DebugValueSource.Registration registration) {
/* 313 */     super.registerDebugValues(level, registration);
/* 314 */     registration.register(DebugSubscriptions.BREEZES, () -> new DebugBreezeInfo(
/* 315 */           getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).map(Entity::getId), 
/* 316 */           getBrain().getMemory(MemoryModuleType.BREEZE_JUMP_TARGET)));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\breeze\Breeze.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */