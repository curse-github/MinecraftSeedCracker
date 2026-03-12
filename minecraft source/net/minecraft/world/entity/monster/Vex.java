/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.TraceableEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.TargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Vex
/*     */   extends Monster
/*     */   implements TraceableEntity
/*     */ {
/*     */   public static final float FLAP_DEGREES_PER_TICK = 45.836624F;
/*  49 */   public static final int TICKS_PER_FLAP = Mth.ceil(3.9269907F);
/*     */   
/*  51 */   protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Vex.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   private static final int FLAG_IS_CHARGING = 1;
/*     */   
/*     */   private EntityReference<Mob> owner;
/*     */   private BlockPos boundOrigin;
/*     */   private boolean hasLimitedLife;
/*     */   private int limitedLifeTicks;
/*     */   
/*     */   public Vex(EntityType<? extends Vex> type, Level level) {
/*  61 */     super(type, level);
/*     */     
/*  63 */     this.moveControl = new VexMoveControl(this);
/*     */     
/*  65 */     this.xpReward = 3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public boolean isFlapping() { return (this.tickCount % TICKS_PER_FLAP == 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   protected boolean isAffectedByBlocks() { return !isRemoved(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  83 */     this.noPhysics = true;
/*  84 */     super.tick();
/*  85 */     this.noPhysics = false;
/*     */     
/*  87 */     setNoGravity(true);
/*     */     
/*  89 */     if (this.hasLimitedLife && 
/*  90 */       --this.limitedLifeTicks <= 0) {
/*  91 */       this.limitedLifeTicks = 20;
/*  92 */       hurt(damageSources().starve(), 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  99 */     super.registerGoals();
/*     */     
/* 101 */     this.goalSelector.addGoal(0, new FloatGoal(this));
/* 102 */     this.goalSelector.addGoal(4, new VexChargeAttackGoal());
/* 103 */     this.goalSelector.addGoal(8, new VexRandomMoveGoal());
/* 104 */     this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 3.0F, 1.0F));
/* 105 */     this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
/*     */     
/* 107 */     this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[] { net.minecraft.world.entity.raid.Raider.class })).setAlertOthers(new Class[0]));
/* 108 */     this.targetSelector.addGoal(2, new VexCopyOwnerTargetGoal(this));
/* 109 */     this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.player.Player.class, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 113 */     return Monster.createMonsterAttributes()
/* 114 */       .add(Attributes.MAX_HEALTH, 14.0D)
/* 115 */       .add(Attributes.ATTACK_DAMAGE, 4.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 120 */     super.defineSynchedData(entityData);
/*     */     
/* 122 */     entityData.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 127 */     super.readAdditionalSaveData(input);
/*     */     
/* 129 */     this.boundOrigin = (BlockPos)input.read("bound_pos", BlockPos.CODEC).orElse(null);
/*     */     
/* 131 */     input.getInt("life_ticks").ifPresentOrElse(this::setLimitedLife, () -> 
/*     */         
/* 133 */         this.hasLimitedLife = false);
/*     */ 
/*     */     
/* 136 */     this.owner = EntityReference.read(input, "owner");
/*     */   }
/*     */ 
/*     */   
/*     */   public void restoreFrom(Entity oldEntity) {
/* 141 */     super.restoreFrom(oldEntity);
/* 142 */     if (oldEntity instanceof Vex) { Vex vex = (Vex)oldEntity;
/* 143 */       this.owner = vex.owner; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 149 */     super.addAdditionalSaveData(output);
/*     */     
/* 151 */     output.storeNullable("bound_pos", BlockPos.CODEC, this.boundOrigin);
/* 152 */     if (this.hasLimitedLife) {
/* 153 */       output.putInt("life_ticks", this.limitedLifeTicks);
/*     */     }
/* 155 */     EntityReference.store(this.owner, output, "owner");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 160 */   public Mob getOwner() { return (Mob)EntityReference.get(this.owner, level(), Mob.class); }
/*     */ 
/*     */ 
/*     */   
/* 164 */   public BlockPos getBoundOrigin() { return this.boundOrigin; }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public void setBoundOrigin(BlockPos boundOrigin) { this.boundOrigin = boundOrigin; }
/*     */ 
/*     */   
/*     */   private boolean getVexFlag(int flag) {
/* 172 */     int flags = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 173 */     return ((flags & flag) != 0);
/*     */   }
/*     */   
/*     */   private void setVexFlag(int flag, boolean value) {
/* 177 */     int flags = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 178 */     if (value) {
/* 179 */       flags |= flag;
/*     */     } else {
/* 181 */       flags &= (flag ^ 0xFFFFFFFF);
/*     */     } 
/* 183 */     this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(flags & 0xFF)));
/*     */   }
/*     */ 
/*     */   
/* 187 */   public boolean isCharging() { return getVexFlag(1); }
/*     */ 
/*     */ 
/*     */   
/* 191 */   public void setIsCharging(boolean value) { setVexFlag(1, value); }
/*     */ 
/*     */ 
/*     */   
/* 195 */   public void setOwner(Mob owner) { this.owner = EntityReference.of(owner); }
/*     */ 
/*     */   
/*     */   public void setLimitedLife(int lifeTicks) {
/* 199 */     this.hasLimitedLife = true;
/* 200 */     this.limitedLifeTicks = lifeTicks;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 205 */   protected SoundEvent getAmbientSound() { return SoundEvents.VEX_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 210 */   protected SoundEvent getDeathSound() { return SoundEvents.VEX_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 215 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.VEX_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 220 */   public float getLightLevelDependentMagicValue() { return 1.0F; }
/*     */   
/*     */   private class VexMoveControl
/*     */     extends MoveControl
/*     */   {
/* 225 */     public VexMoveControl(Vex vex) { super(vex); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 230 */       if (this.operation != MoveControl.Operation.MOVE_TO) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 238 */       Vec3 delta = new Vec3(this.wantedX - Vex.this.getX(), this.wantedY - Vex.this.getY(), this.wantedZ - Vex.this.getZ());
/*     */ 
/*     */       
/* 241 */       double deltaLength = delta.length();
/* 242 */       if (deltaLength < Vex.this.getBoundingBox().getSize()) {
/* 243 */         this.operation = MoveControl.Operation.WAIT;
/* 244 */         Vex.this.setDeltaMovement(Vex.this.getDeltaMovement().scale(0.5D));
/*     */       } else {
/* 246 */         Vex.this.setDeltaMovement(Vex.this.getDeltaMovement().add(delta.scale(this.speedModifier * 0.05D / deltaLength)));
/*     */         
/* 248 */         if (Vex.this.getTarget() == null) {
/* 249 */           Vec3 movement = Vex.this.getDeltaMovement();
/* 250 */           Vex.this.setYRot(-((float)Mth.atan2(movement.x, movement.z)) * 57.295776F);
/* 251 */           Vex.this.yBodyRot = Vex.this.getYRot();
/*     */         } else {
/*     */           
/* 254 */           double tx = Vex.this.getTarget().getX() - Vex.this.getX();
/* 255 */           double tz = Vex.this.getTarget().getZ() - Vex.this.getZ();
/* 256 */           Vex.this.setYRot(-((float)Mth.atan2(tx, tz)) * 57.295776F);
/* 257 */           Vex.this.yBodyRot = Vex.this.getYRot();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class VexChargeAttackGoal
/*     */     extends Goal {
/* 265 */     public VexChargeAttackGoal() { setFlags(EnumSet.of(Goal.Flag.MOVE)); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 270 */       LivingEntity target = Vex.this.getTarget();
/* 271 */       if (target != null && target.isAlive() && !Vex.this.getMoveControl().hasWanted() && Vex.this.random.nextInt(reducedTickDelay(7)) == 0) {
/* 272 */         return (Vex.this.distanceToSqr(target) > 4.0D);
/*     */       }
/* 274 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 279 */     public boolean canContinueToUse() { return (Vex.this.getMoveControl().hasWanted() && Vex.this.isCharging() && Vex.this.getTarget() != null && Vex.this.getTarget().isAlive()); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() {
/* 284 */       LivingEntity attackTarget = Vex.this.getTarget();
/* 285 */       if (attackTarget != null) {
/* 286 */         Vec3 eyePosition = attackTarget.getEyePosition();
/* 287 */         Vex.this.moveControl.setWantedPosition(eyePosition.x, eyePosition.y, eyePosition.z, 1.0D);
/*     */       } 
/* 289 */       Vex.this.setIsCharging(true);
/* 290 */       Vex.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 295 */     public void stop() { Vex.this.setIsCharging(false); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 300 */     public boolean requiresUpdateEveryTick() { return true; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 305 */       LivingEntity attackTarget = Vex.this.getTarget();
/* 306 */       if (attackTarget == null) {
/*     */         return;
/*     */       }
/* 309 */       if (Vex.this.getBoundingBox().intersects(attackTarget.getBoundingBox())) {
/* 310 */         Vex.this.doHurtTarget(getServerLevel(Vex.this.level()), attackTarget);
/* 311 */         Vex.this.setIsCharging(false);
/*     */       } else {
/* 313 */         double distance = Vex.this.distanceToSqr(attackTarget);
/* 314 */         if (distance < 9.0D) {
/* 315 */           Vec3 eyePosition = attackTarget.getEyePosition();
/* 316 */           Vex.this.moveControl.setWantedPosition(eyePosition.x, eyePosition.y, eyePosition.z, 1.0D);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class VexRandomMoveGoal
/*     */     extends Goal {
/* 324 */     public VexRandomMoveGoal() { setFlags(EnumSet.of(Goal.Flag.MOVE)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 329 */     public boolean canUse() { return (!Vex.this.getMoveControl().hasWanted() && Vex.this.random.nextInt(reducedTickDelay(7)) == 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 334 */     public boolean canContinueToUse() { return false; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 339 */       BlockPos boundOrigin = Vex.this.getBoundOrigin();
/* 340 */       if (boundOrigin == null)
/*     */       {
/* 342 */         boundOrigin = Vex.this.blockPosition();
/*     */       }
/*     */       
/* 345 */       for (int attempts = 0; attempts < 3; attempts++) {
/* 346 */         BlockPos testPos = boundOrigin.offset(Vex.this.random.nextInt(15) - 7, Vex.this.random.nextInt(11) - 5, Vex.this.random.nextInt(15) - 7);
/* 347 */         if (Vex.this.level().isEmptyBlock(testPos)) {
/* 348 */           Vex.this.moveControl.setWantedPosition(testPos.getX() + 0.5D, testPos.getY() + 0.5D, testPos.getZ() + 0.5D, 0.25D);
/* 349 */           if (Vex.this.getTarget() == null) {
/* 350 */             Vex.this.getLookControl().setLookAt(testPos.getX() + 0.5D, testPos.getY() + 0.5D, testPos.getZ() + 0.5D, 180.0F, 20.0F);
/*     */           }
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 360 */     RandomSource random = level.getRandom();
/* 361 */     populateDefaultEquipmentSlots(random, difficulty);
/* 362 */     populateDefaultEquipmentEnchantments(level, random, difficulty);
/*     */     
/* 364 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/* 369 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
/* 370 */     setDropChance(EquipmentSlot.MAINHAND, 0.0F);
/*     */   }
/*     */   
/*     */   private class VexCopyOwnerTargetGoal extends TargetGoal {
/* 374 */     private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
/*     */ 
/*     */     
/* 377 */     public VexCopyOwnerTargetGoal(PathfinderMob mob) { super(mob, false); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 382 */       Mob owner = Vex.this.getOwner();
/* 383 */       return (owner != null && owner.getTarget() != null && canAttack(owner.getTarget(), this.copyOwnerTargeting));
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 388 */       Mob owner = Vex.this.getOwner();
/* 389 */       Vex.this.setTarget((owner != null) ? owner.getTarget() : null);
/* 390 */       super.start();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Vex.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */