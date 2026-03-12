/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.animal.feline.Cat;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Phantom
/*     */   extends Mob
/*     */   implements Enemy
/*     */ {
/*     */   public static final float FLAP_DEGREES_PER_TICK = 7.448451F;
/*  49 */   public static final int TICKS_PER_FLAP = Mth.ceil(24.166098F);
/*     */   
/*  51 */   private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(Phantom.class, EntityDataSerializers.INT);
/*     */   
/*  53 */   private Vec3 moveTargetPoint = Vec3.ZERO;
/*     */   private BlockPos anchorPoint;
/*     */   
/*     */   private enum AttackPhase {
/*  57 */     CIRCLE,
/*  58 */     SWOOP;
/*     */   }
/*     */   
/*  61 */   private AttackPhase attackPhase = AttackPhase.CIRCLE;
/*     */   
/*     */   public Phantom(EntityType<? extends Phantom> type, Level level) {
/*  64 */     super(type, level);
/*  65 */     this.xpReward = 5;
/*     */     
/*  67 */     this.moveControl = new PhantomMoveControl(this);
/*  68 */     this.lookControl = new PhantomLookControl(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public boolean isFlapping() { return ((getUniqueFlapTickOffset() + this.tickCount) % TICKS_PER_FLAP == 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   protected BodyRotationControl createBodyControl() { return new PhantomBodyRotationControl(this); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  85 */     this.goalSelector.addGoal(1, new PhantomAttackStrategyGoal());
/*  86 */     this.goalSelector.addGoal(2, new PhantomSweepAttackGoal());
/*  87 */     this.goalSelector.addGoal(3, new PhantomCircleAroundAnchorGoal());
/*     */     
/*  89 */     this.targetSelector.addGoal(1, new PhantomAttackPlayerTargetGoal());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  94 */     super.defineSynchedData(entityData);
/*     */     
/*  96 */     entityData.define(ID_SIZE, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/* 100 */   public void setPhantomSize(int size) { this.entityData.set(ID_SIZE, Integer.valueOf(Mth.clamp(size, 0, 64))); }
/*     */ 
/*     */   
/*     */   private void updatePhantomSizeInfo() {
/* 104 */     refreshDimensions();
/* 105 */     getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((6 + getPhantomSize()));
/*     */   }
/*     */ 
/*     */   
/* 109 */   public int getPhantomSize() { return ((Integer)this.entityData.get(ID_SIZE)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 114 */     if (ID_SIZE.equals(accessor)) {
/* 115 */       updatePhantomSizeInfo();
/*     */     }
/*     */     
/* 118 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */   
/* 122 */   public int getUniqueFlapTickOffset() { return getId() * 3; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 127 */     super.tick();
/*     */     
/* 129 */     if (level().isClientSide()) {
/* 130 */       float anim = Mth.cos(((getUniqueFlapTickOffset() + this.tickCount) * 7.448451F * 0.017453292F + 3.1415927F));
/* 131 */       float nextAnim = Mth.cos(((getUniqueFlapTickOffset() + this.tickCount + 1) * 7.448451F * 0.017453292F + 3.1415927F));
/* 132 */       if (anim > 0.0F && nextAnim <= 0.0F) {
/* 133 */         level().playLocalSound(getX(), getY(), getZ(), SoundEvents.PHANTOM_FLAP, getSoundSource(), 0.95F + this.random.nextFloat() * 0.05F, 0.95F + this.random.nextFloat() * 0.05F, false);
/*     */       }
/*     */       
/* 136 */       float width = getBbWidth() * 1.48F;
/* 137 */       float c = Mth.cos((getYRot() * 0.017453292F)) * width;
/* 138 */       float s = Mth.sin((getYRot() * 0.017453292F)) * width;
/*     */       
/* 140 */       float h = (0.3F + anim * 0.45F) * getBbHeight() * 2.5F;
/* 141 */       level().addParticle(ParticleTypes.MYCELIUM, getX() + c, getY() + h, getZ() + s, 0.0D, 0.0D, 0.0D);
/* 142 */       level().addParticle(ParticleTypes.MYCELIUM, getX() - c, getY() + h, getZ() - s, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   public boolean onClimbable() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 159 */   public void travel(Vec3 input) { travelFlying(input, 0.2F); }
/*     */ 
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 164 */     this.anchorPoint = blockPosition().above(5);
/* 165 */     setPhantomSize(0);
/* 166 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 171 */     super.readAdditionalSaveData(input);
/*     */     
/* 173 */     this.anchorPoint = (BlockPos)input.read("anchor_pos", BlockPos.CODEC).orElse(null);
/* 174 */     setPhantomSize(input.getIntOr("size", 0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 179 */     super.addAdditionalSaveData(output);
/*     */     
/* 181 */     output.storeNullable("anchor_pos", BlockPos.CODEC, this.anchorPoint);
/* 182 */     output.putInt("size", getPhantomSize());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 187 */   public boolean shouldRenderAtSqrDistance(double distance) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 192 */   public SoundSource getSoundSource() { return SoundSource.HOSTILE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 197 */   protected SoundEvent getAmbientSound() { return SoundEvents.PHANTOM_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.PHANTOM_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 207 */   protected SoundEvent getDeathSound() { return SoundEvents.PHANTOM_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 212 */   protected float getSoundVolume() { return 1.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   public boolean canAttackType(EntityType<?> targetType) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 222 */     int size = getPhantomSize();
/*     */     
/* 224 */     EntityDimensions originalDimensions = super.getDefaultDimensions(pose);
/* 225 */     return originalDimensions.scale(1.0F + 0.15F * size);
/*     */   }
/*     */ 
/*     */   
/* 229 */   private boolean canAttack(ServerLevel level, LivingEntity target, TargetingConditions targetConditions) { return targetConditions.test(level, this, target); }
/*     */   
/*     */   private class PhantomMoveControl
/*     */     extends MoveControl {
/* 233 */     private float speed = 0.1F;
/*     */ 
/*     */     
/* 236 */     public PhantomMoveControl(Mob mob) { super(mob); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 241 */       if (Phantom.this.horizontalCollision) {
/*     */         
/* 243 */         Phantom.this.setYRot(Phantom.this.getYRot() + 180.0F);
/* 244 */         this.speed = 0.1F;
/*     */       } 
/*     */ 
/*     */       
/* 248 */       double tdx = this.this$0.moveTargetPoint.x - Phantom.this.getX();
/* 249 */       double tdy = this.this$0.moveTargetPoint.y - Phantom.this.getY();
/* 250 */       double tdz = this.this$0.moveTargetPoint.z - Phantom.this.getZ();
/* 251 */       double sd = Math.sqrt(tdx * tdx + tdz * tdz);
/*     */ 
/*     */       
/* 254 */       if (Math.abs(sd) > 9.999999747378752E-6D) {
/* 255 */         double yRelativeScale = 1.0D - Math.abs(tdy * 0.699999988079071D) / sd;
/* 256 */         tdx *= yRelativeScale;
/* 257 */         tdz *= yRelativeScale;
/* 258 */         sd = Math.sqrt(tdx * tdx + tdz * tdz);
/* 259 */         double sd2 = Math.sqrt(tdx * tdx + tdz * tdz + tdy * tdy);
/*     */ 
/*     */         
/* 262 */         float prev = Phantom.this.getYRot();
/* 263 */         float angle = (float)Mth.atan2(tdz, tdx);
/* 264 */         float a = Mth.wrapDegrees(Phantom.this.getYRot() + 90.0F);
/* 265 */         float b = Mth.wrapDegrees(angle * 57.295776F);
/* 266 */         Phantom.this.setYRot(Mth.approachDegrees(a, b, 4.0F) - 90.0F);
/* 267 */         Phantom.this.yBodyRot = Phantom.this.getYRot();
/*     */         
/* 269 */         if (Mth.degreesDifferenceAbs(prev, Phantom.this.getYRot()) < 3.0F) {
/* 270 */           this.speed = Mth.approach(this.speed, 1.8F, 0.005F * 1.8F / this.speed);
/*     */         } else {
/* 272 */           this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
/*     */         } 
/*     */         
/* 275 */         float xRotD = (float)-(Mth.atan2(-tdy, sd) * 57.2957763671875D);
/* 276 */         Phantom.this.setXRot(xRotD);
/*     */         
/* 278 */         float moveAngle = Phantom.this.getYRot() + 90.0F;
/* 279 */         double txd = (this.speed * Mth.cos((moveAngle * 0.017453292F))) * Math.abs(tdx / sd2);
/* 280 */         double tzd = (this.speed * Mth.sin((moveAngle * 0.017453292F))) * Math.abs(tdz / sd2);
/* 281 */         double tyd = (this.speed * Mth.sin((xRotD * 0.017453292F))) * Math.abs(tdy / sd2);
/*     */         
/* 283 */         Vec3 movement = Phantom.this.getDeltaMovement();
/* 284 */         Phantom.this.setDeltaMovement(movement.add((new Vec3(txd, tyd, tzd)).subtract(movement).scale(0.2D)));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class PhantomBodyRotationControl
/*     */     extends BodyRotationControl {
/* 291 */     public PhantomBodyRotationControl(Mob mob) { super(mob); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void clientTick() {
/* 296 */       Phantom.this.yHeadRot = Phantom.this.yBodyRot;
/* 297 */       Phantom.this.yBodyRot = Phantom.this.getYRot();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PhantomLookControl
/*     */     extends LookControl {
/* 303 */     public PhantomLookControl(Mob mob) { super(mob); }
/*     */ 
/*     */     
/*     */     public void tick() {}
/*     */   }
/*     */ 
/*     */   
/*     */   private abstract class PhantomMoveTargetGoal
/*     */     extends Goal
/*     */   {
/* 313 */     public PhantomMoveTargetGoal() { setFlags(EnumSet.of(Goal.Flag.MOVE)); }
/*     */ 
/*     */ 
/*     */     
/* 317 */     protected boolean touchingTarget() { return (Phantom.this.moveTargetPoint.distanceToSqr(Phantom.this.getX(), Phantom.this.getY(), Phantom.this.getZ()) < 4.0D); } }
/*     */   
/*     */   private class PhantomCircleAroundAnchorGoal extends PhantomMoveTargetGoal {
/*     */     private PhantomCircleAroundAnchorGoal() {
/* 321 */       super(Phantom.this);
/*     */     }
/*     */     
/*     */     private float angle;
/*     */     private float distance;
/*     */     private float height;
/*     */     private float clockwise;
/*     */     
/* 329 */     public boolean canUse() { return (Phantom.this.getTarget() == null || Phantom.this.attackPhase == Phantom.AttackPhase.CIRCLE); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() {
/* 334 */       this.distance = 5.0F + Phantom.this.random.nextFloat() * 10.0F;
/* 335 */       this.height = -4.0F + Phantom.this.random.nextFloat() * 9.0F;
/* 336 */       this.clockwise = Phantom.this.random.nextBoolean() ? 1.0F : -1.0F;
/* 337 */       selectNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 342 */       if (Phantom.this.random.nextInt(adjustedTickDelay(350)) == 0) {
/* 343 */         this.height = -4.0F + Phantom.this.random.nextFloat() * 9.0F;
/*     */       }
/* 345 */       if (Phantom.this.random.nextInt(adjustedTickDelay(250)) == 0) {
/* 346 */         this.distance++;
/* 347 */         if (this.distance > 15.0F) {
/* 348 */           this.distance = 5.0F;
/* 349 */           this.clockwise = -this.clockwise;
/*     */         } 
/*     */       } 
/* 352 */       if (Phantom.this.random.nextInt(adjustedTickDelay(450)) == 0) {
/* 353 */         this.angle = Phantom.this.random.nextFloat() * 2.0F * 3.1415927F;
/* 354 */         selectNext();
/*     */       } 
/* 356 */       if (touchingTarget()) {
/* 357 */         selectNext();
/*     */       }
/*     */       
/* 360 */       if (this.this$0.moveTargetPoint.y < Phantom.this.getY() && !Phantom.this.level().isEmptyBlock(Phantom.this.blockPosition().below(1))) {
/* 361 */         this.height = Math.max(1.0F, this.height);
/* 362 */         selectNext();
/*     */       } 
/*     */       
/* 365 */       if (this.this$0.moveTargetPoint.y > Phantom.this.getY() && !Phantom.this.level().isEmptyBlock(Phantom.this.blockPosition().above(1))) {
/* 366 */         this.height = Math.min(-1.0F, this.height);
/* 367 */         selectNext();
/*     */       } 
/*     */     }
/*     */     
/*     */     private void selectNext() {
/* 372 */       if (Phantom.this.anchorPoint == null) {
/* 373 */         Phantom.this.anchorPoint = Phantom.this.blockPosition();
/*     */       }
/* 375 */       this.angle += this.clockwise * 15.0F * 0.017453292F;
/* 376 */       Phantom.this.moveTargetPoint = Vec3.atLowerCornerOf(Phantom.this.anchorPoint).add((this.distance * Mth.cos(this.angle)), (-4.0F + this.height), (this.distance * Mth.sin(this.angle)));
/*     */     } }
/*     */   
/*     */   private class PhantomSweepAttackGoal extends PhantomMoveTargetGoal { private PhantomSweepAttackGoal() {
/* 380 */       super(Phantom.this);
/*     */     }
/*     */ 
/*     */     
/*     */     private static final int CAT_SEARCH_TICK_DELAY = 20;
/*     */     private boolean isScaredOfCat;
/*     */     private int catSearchTick;
/*     */     
/* 388 */     public boolean canUse() { return (Phantom.this.getTarget() != null && Phantom.this.attackPhase == Phantom.AttackPhase.SWOOP); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 393 */       LivingEntity target = Phantom.this.getTarget();
/* 394 */       if (target == null) {
/* 395 */         return false;
/*     */       }
/* 397 */       if (!target.isAlive()) {
/* 398 */         return false;
/*     */       }
/* 400 */       if (target instanceof Player) { Player player = (Player)target; if (target.isSpectator() || player.isCreative()) {
/* 401 */           return false;
/*     */         } }
/*     */       
/* 404 */       if (!canUse()) {
/* 405 */         return false;
/*     */       }
/*     */       
/* 408 */       if (Phantom.this.tickCount > this.catSearchTick) {
/* 409 */         this.catSearchTick = Phantom.this.tickCount + 20;
/* 410 */         List<Cat> cats = Phantom.this.level().getEntitiesOfClass(Cat.class, Phantom.this.getBoundingBox().inflate(16.0D), EntitySelector.ENTITY_STILL_ALIVE);
/* 411 */         for (Cat cat : cats) {
/* 412 */           cat.hiss();
/*     */         }
/* 414 */         this.isScaredOfCat = !cats.isEmpty();
/*     */       } 
/*     */       
/* 417 */       return !this.isScaredOfCat;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() {}
/*     */ 
/*     */     
/*     */     public void stop() {
/* 426 */       Phantom.this.setTarget(null);
/* 427 */       Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 432 */       LivingEntity target = Phantom.this.getTarget();
/* 433 */       if (target == null) {
/*     */         return;
/*     */       }
/* 436 */       Phantom.this.moveTargetPoint = new Vec3(target.getX(), target.getY(0.5D), target.getZ());
/*     */       
/* 438 */       if (Phantom.this.getBoundingBox().inflate(0.20000000298023224D).intersects(target.getBoundingBox())) {
/* 439 */         Phantom.this.doHurtTarget(getServerLevel(Phantom.this.level()), target);
/* 440 */         Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
/* 441 */         if (!Phantom.this.isSilent()) {
/* 442 */           Phantom.this.level().levelEvent(1039, Phantom.this.blockPosition(), 0);
/*     */         }
/* 444 */       } else if (Phantom.this.horizontalCollision || Phantom.this.hurtTime > 0) {
/* 445 */         Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
/*     */       } 
/*     */     } }
/*     */ 
/*     */   
/*     */   private class PhantomAttackStrategyGoal
/*     */     extends Goal {
/*     */     private int nextSweepTick;
/*     */     
/*     */     public boolean canUse() {
/* 455 */       LivingEntity target = Phantom.this.getTarget();
/* 456 */       if (target != null) {
/* 457 */         return Phantom.this.canAttack(getServerLevel(Phantom.this.level()), target, TargetingConditions.DEFAULT);
/*     */       }
/* 459 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 464 */       this.nextSweepTick = adjustedTickDelay(10);
/* 465 */       Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
/* 466 */       setAnchorAboveTarget();
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 471 */       if (Phantom.this.anchorPoint != null) {
/* 472 */         Phantom.this.anchorPoint = Phantom.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, Phantom.this.anchorPoint).above(10 + Phantom.this.random.nextInt(20));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 478 */       if (Phantom.this.attackPhase == Phantom.AttackPhase.CIRCLE) {
/* 479 */         this.nextSweepTick--;
/* 480 */         if (this.nextSweepTick <= 0) {
/* 481 */           Phantom.this.attackPhase = Phantom.AttackPhase.SWOOP;
/* 482 */           setAnchorAboveTarget();
/* 483 */           this.nextSweepTick = adjustedTickDelay((8 + Phantom.this.random.nextInt(4)) * 20);
/*     */           
/* 485 */           Phantom.this.playSound(SoundEvents.PHANTOM_SWOOP, 10.0F, 0.95F + Phantom.this.random.nextFloat() * 0.1F);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void setAnchorAboveTarget() {
/* 491 */       if (Phantom.this.anchorPoint == null) {
/*     */         return;
/*     */       }
/* 494 */       Phantom.this.anchorPoint = Phantom.this.getTarget().blockPosition().above(20 + Phantom.this.random.nextInt(20));
/* 495 */       if (Phantom.this.anchorPoint.getY() < Phantom.this.level().getSeaLevel())
/* 496 */         Phantom.this.anchorPoint = new BlockPos(Phantom.this.anchorPoint.getX(), Phantom.this.level().getSeaLevel() + 1, Phantom.this.anchorPoint.getZ()); 
/*     */     }
/*     */   }
/*     */   
/*     */   private class PhantomAttackPlayerTargetGoal
/*     */     extends Goal
/*     */   {
/* 503 */     private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0D);
/*     */     
/* 505 */     private int nextScanTick = reducedTickDelay(20);
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 509 */       if (this.nextScanTick > 0) {
/* 510 */         this.nextScanTick--;
/* 511 */         return false;
/*     */       } 
/* 513 */       this.nextScanTick = reducedTickDelay(60);
/*     */       
/* 515 */       ServerLevel level = getServerLevel(Phantom.this.level());
/* 516 */       List<Player> players = level.getNearbyPlayers(this.attackTargeting, Phantom.this, Phantom.this.getBoundingBox().inflate(16.0D, 64.0D, 16.0D));
/* 517 */       if (!players.isEmpty()) {
/*     */         
/* 519 */         players.sort(Comparator.comparing(Entity::getY).reversed());
/* 520 */         for (Player player : players) {
/* 521 */           if (Phantom.this.canAttack(level, player, TargetingConditions.DEFAULT)) {
/* 522 */             Phantom.this.setTarget(player);
/* 523 */             return true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 527 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 532 */       LivingEntity target = Phantom.this.getTarget();
/* 533 */       if (target != null) {
/* 534 */         return Phantom.this.canAttack(getServerLevel(Phantom.this.level()), target, TargetingConditions.DEFAULT);
/*     */       }
/*     */       
/* 537 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Phantom.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */