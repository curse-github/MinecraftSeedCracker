/*     */ package net.minecraft.world.entity.animal.squid;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.animal.AgeableWaterCreature;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Squid
/*     */   extends AgeableWaterCreature
/*     */ {
/*     */   public float xBodyRot;
/*     */   public float xBodyRotO;
/*     */   public float zBodyRot;
/*     */   public float zBodyRotO;
/*     */   public float tentacleMovement;
/*     */   public float oldTentacleMovement;
/*     */   public float tentacleAngle;
/*     */   public float oldTentacleAngle;
/*     */   private float speed;
/*     */   private float tentacleSpeed;
/*     */   private float rotateSpeed;
/*  51 */   private Vec3 movementVector = Vec3.ZERO;
/*     */   
/*     */   public Squid(EntityType<? extends Squid> type, Level level) {
/*  54 */     super(type, level);
/*     */     
/*  56 */     this.random.setSeed(getId());
/*  57 */     this.tentacleSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  62 */     this.goalSelector.addGoal(0, new SquidRandomMovementGoal(this));
/*  63 */     this.goalSelector.addGoal(1, new SquidFleeGoal());
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  67 */     return Mob.createMobAttributes()
/*  68 */       .add(Attributes.MAX_HEALTH, 10.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  73 */   protected SoundEvent getAmbientSound() { return SoundEvents.SQUID_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.SQUID_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   protected SoundEvent getDeathSound() { return SoundEvents.SQUID_DEATH; }
/*     */ 
/*     */ 
/*     */   
/*  87 */   protected SoundEvent getSquirtSound() { return SoundEvents.SQUID_SQUIRT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public boolean canBeLeashed() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   protected float getSoundVolume() { return 0.4F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   protected Entity.MovementEmission getMovementEmission() { return Entity.MovementEmission.EVENTS; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) { return (AgeableMob)EntityType.SQUID.create(level, EntitySpawnReason.BREEDING); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   protected double getDefaultGravity() { return 0.08D; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 117 */     super.aiStep();
/*     */     
/* 119 */     this.xBodyRotO = this.xBodyRot;
/* 120 */     this.zBodyRotO = this.zBodyRot;
/*     */     
/* 122 */     this.oldTentacleMovement = this.tentacleMovement;
/* 123 */     this.oldTentacleAngle = this.tentacleAngle;
/*     */     
/* 125 */     this.tentacleMovement += this.tentacleSpeed;
/* 126 */     if (this.tentacleMovement > 6.283185307179586D) {
/* 127 */       if (level().isClientSide()) {
/* 128 */         this.tentacleMovement = 6.2831855F;
/*     */       } else {
/* 130 */         this.tentacleMovement -= 6.2831855F;
/* 131 */         if (this.random.nextInt(10) == 0) {
/* 132 */           this.tentacleSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
/*     */         }
/* 134 */         level().broadcastEntityEvent(this, (byte)19);
/*     */       } 
/*     */     }
/*     */     
/* 138 */     if (isInWater()) {
/* 139 */       if (this.tentacleMovement < 3.1415927F) {
/* 140 */         float tentacleScale = this.tentacleMovement / 3.1415927F;
/* 141 */         this.tentacleAngle = Mth.sin((tentacleScale * tentacleScale * 3.1415927F)) * 3.1415927F * 0.25F;
/*     */         
/* 143 */         if (tentacleScale > 0.75D) {
/* 144 */           if (isLocalInstanceAuthoritative()) {
/* 145 */             setDeltaMovement(this.movementVector);
/*     */           }
/* 147 */           this.rotateSpeed = 1.0F;
/*     */         } else {
/* 149 */           this.rotateSpeed *= 0.8F;
/*     */         } 
/*     */       } else {
/* 152 */         this.tentacleAngle = 0.0F;
/* 153 */         if (isLocalInstanceAuthoritative()) {
/* 154 */           setDeltaMovement(getDeltaMovement().scale(0.9D));
/*     */         }
/* 156 */         this.rotateSpeed *= 0.99F;
/*     */       } 
/*     */       
/* 159 */       Vec3 movement = getDeltaMovement();
/* 160 */       double horizontalMovement = movement.horizontalDistance();
/*     */       
/* 162 */       this.yBodyRot += (-((float)Mth.atan2(movement.x, movement.z)) * 57.295776F - this.yBodyRot) * 0.1F;
/* 163 */       setYRot(this.yBodyRot);
/* 164 */       this.zBodyRot += 3.1415927F * this.rotateSpeed * 1.5F;
/* 165 */       this.xBodyRot += (-((float)Mth.atan2(horizontalMovement, movement.y)) * 57.295776F - this.xBodyRot) * 0.1F;
/*     */     } else {
/* 167 */       this.tentacleAngle = Mth.abs(Mth.sin(this.tentacleMovement)) * 3.1415927F * 0.25F;
/*     */       
/* 169 */       if (!level().isClientSide()) {
/* 170 */         double yd = (getDeltaMovement()).y;
/*     */         
/* 172 */         if (hasEffect(MobEffects.LEVITATION)) {
/* 173 */           yd = 0.05D * (getEffect(MobEffects.LEVITATION).getAmplifier() + 1);
/*     */         } else {
/* 175 */           yd -= getGravity();
/*     */         } 
/*     */         
/* 178 */         setDeltaMovement(0.0D, yd * 0.9800000190734863D, 0.0D);
/*     */       } 
/*     */ 
/*     */       
/* 182 */       this.xBodyRot += (-90.0F - this.xBodyRot) * 0.02F;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 188 */     if (super.hurtServer(level, source, damage) && getLastHurtByMob() != null) {
/* 189 */       spawnInk();
/* 190 */       return true;
/*     */     } 
/*     */     
/* 193 */     return false;
/*     */   }
/*     */   
/*     */   private Vec3 rotateVector(Vec3 vec) {
/* 197 */     v = vec.xRot(this.xBodyRotO * 0.017453292F);
/* 198 */     return v.yRot(-this.yBodyRotO * 0.017453292F);
/*     */   }
/*     */ 
/*     */   
/*     */   private void spawnInk() {
/* 203 */     makeSound(getSquirtSound());
/* 204 */     Vec3 pos = rotateVector(new Vec3(0.0D, -1.0D, 0.0D)).add(getX(), getY(), getZ());
/* 205 */     for (int i = 0; i < 30; i++) {
/* 206 */       Vec3 dir = rotateVector(new Vec3(this.random.nextFloat() * 0.6D - 0.3D, -1.0D, this.random.nextFloat() * 0.6D - 0.3D));
/* 207 */       float inkPosOffsetScale = isBaby() ? 0.1F : 0.3F;
/* 208 */       Vec3 dirOffset = dir.scale((inkPosOffsetScale + this.random.nextFloat() * 2.0F));
/* 209 */       ((ServerLevel)level()).sendParticles(getInkParticle(), pos.x, pos.y + 0.5D, pos.z, 0, dirOffset.x, dirOffset.y, dirOffset.z, 0.10000000149011612D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 214 */   protected ParticleOptions getInkParticle() { return ParticleTypes.SQUID_INK; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 219 */   public void travel(Vec3 input) { move(MoverType.SELF, getDeltaMovement()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 224 */     if (id == 19) {
/* 225 */       this.tentacleMovement = 0.0F;
/*     */     } else {
/* 227 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 232 */   public boolean hasMovementVector() { return (this.movementVector.lengthSqr() > 9.999999747378752E-6D); }
/*     */ 
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 237 */     SpawnGroupData spawnGroupData = (SpawnGroupData)Objects.requireNonNullElseGet(groupData, () -> new AgeableMob.AgeableMobGroupData(0.05F));
/* 238 */     return super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData);
/*     */   }
/*     */   
/*     */   private static class SquidRandomMovementGoal
/*     */     extends Goal {
/*     */     private final Squid squid;
/*     */     
/* 245 */     public SquidRandomMovementGoal(Squid squid) { this.squid = squid; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 250 */     public boolean canUse() { return true; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 255 */       int noActionTime = this.squid.getNoActionTime();
/*     */       
/* 257 */       if (noActionTime > 100) {
/* 258 */         this.squid.movementVector = Vec3.ZERO;
/* 259 */       } else if (this.squid.getRandom().nextInt(reducedTickDelay(50)) == 0 || !this.squid.wasTouchingWater || !this.squid.hasMovementVector()) {
/* 260 */         float angle = this.squid.getRandom().nextFloat() * 6.2831855F;
/* 261 */         this.squid
/*     */ 
/*     */           
/* 264 */           .movementVector = new Vec3((Mth.cos(angle) * 0.2F), (-0.1F + this.squid.getRandom().nextFloat() * 0.2F), (Mth.sin(angle) * 0.2F));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class SquidFleeGoal
/*     */     extends Goal
/*     */   {
/*     */     private static final float SQUID_FLEE_SPEED = 3.0F;
/*     */     private static final float SQUID_FLEE_MIN_DISTANCE = 5.0F;
/*     */     private static final float SQUID_FLEE_MAX_DISTANCE = 10.0F;
/*     */     private int fleeTicks;
/*     */     
/*     */     public boolean canUse() {
/* 279 */       LivingEntity entity = Squid.this.getLastHurtByMob();
/* 280 */       if (Squid.this.isInWater() && entity != null) {
/* 281 */         return (Squid.this.distanceToSqr(entity) < 100.0D);
/*     */       }
/*     */       
/* 284 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 289 */     public void start() { this.fleeTicks = 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 294 */     public boolean requiresUpdateEveryTick() { return true; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 299 */       this.fleeTicks++;
/*     */       
/* 301 */       LivingEntity lastHurtByMob = Squid.this.getLastHurtByMob();
/* 302 */       if (lastHurtByMob == null) {
/*     */         return;
/*     */       }
/*     */       
/* 306 */       Vec3 fleeTo = new Vec3(Squid.this.getX() - lastHurtByMob.getX(), Squid.this.getY() - lastHurtByMob.getY(), Squid.this.getZ() - lastHurtByMob.getZ());
/*     */       
/* 308 */       BlockState blockState = Squid.this.level().getBlockState(BlockPos.containing(Squid.this.getX() + fleeTo.x, Squid.this.getY() + fleeTo.y, Squid.this.getZ() + fleeTo.z));
/* 309 */       FluidState fluidState = Squid.this.level().getFluidState(BlockPos.containing(Squid.this.getX() + fleeTo.x, Squid.this.getY() + fleeTo.y, Squid.this.getZ() + fleeTo.z));
/* 310 */       if (fluidState.is(FluidTags.WATER) || blockState.isAir()) {
/* 311 */         double length = fleeTo.length();
/* 312 */         if (length > 0.0D) {
/* 313 */           fleeTo.normalize();
/*     */           
/* 315 */           double avoidSpeed = 3.0D;
/* 316 */           if (length > 5.0D) {
/* 317 */             avoidSpeed -= (length - 5.0D) / 5.0D;
/*     */           }
/*     */           
/* 320 */           if (avoidSpeed > 0.0D) {
/* 321 */             fleeTo = fleeTo.scale(avoidSpeed);
/*     */           }
/*     */         } 
/*     */         
/* 325 */         if (blockState.isAir()) {
/* 326 */           fleeTo = fleeTo.subtract(0.0D, fleeTo.y, 0.0D);
/*     */         }
/*     */         
/* 329 */         Squid.this.movementVector = new Vec3(fleeTo.x / 20.0D, fleeTo.y / 20.0D, fleeTo.z / 20.0D);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 336 */       if (this.fleeTicks % 10 == 5)
/* 337 */         Squid.this.level().addParticle(ParticleTypes.BUBBLE, Squid.this.getX(), Squid.this.getY(), Squid.this.getZ(), 0.0D, 0.0D, 0.0D); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\squid\Squid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */