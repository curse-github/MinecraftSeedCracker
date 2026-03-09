/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Ravager
/*     */   extends Raider
/*     */ {
/*  51 */   private static final Predicate<Entity> ROAR_TARGET_WITH_GRIEFING = entity -> (!(entity instanceof Ravager) && entity.isAlive());
/*     */   
/*  53 */   private static final Predicate<Entity> ROAR_TARGET_WITHOUT_GRIEFING = entity -> (ROAR_TARGET_WITH_GRIEFING.test(entity) && !entity.getType().equals(EntityType.ARMOR_STAND));
/*     */   
/*  55 */   private static final Predicate<LivingEntity> ROAR_TARGET_ON_CLIENT = e -> (!(e instanceof Ravager) && e.isAlive() && e.isLocalInstanceAuthoritative());
/*     */   
/*     */   private static final double BASE_MOVEMENT_SPEED = 0.3D;
/*     */   
/*     */   private static final double ATTACK_MOVEMENT_SPEED = 0.35D;
/*     */   
/*     */   private static final int STUNNED_COLOR = 8356754;
/*     */   private static final float STUNNED_COLOR_BLUE = 0.57254905F;
/*     */   private static final float STUNNED_COLOR_GREEN = 0.5137255F;
/*     */   private static final float STUNNED_COLOR_RED = 0.49803922F;
/*     */   public static final int ATTACK_DURATION = 10;
/*     */   public static final int STUN_DURATION = 40;
/*     */   private static final int DEFAULT_ATTACK_TICK = 0;
/*     */   private static final int DEFAULT_STUN_TICK = 0;
/*     */   private static final int DEFAULT_ROAR_TICK = 0;
/*  70 */   private int attackTick = 0;
/*  71 */   private int stunnedTick = 0;
/*  72 */   private int roarTick = 0;
/*     */   
/*     */   public Ravager(EntityType<? extends Ravager> type, Level level) {
/*  75 */     super(type, level);
/*     */     
/*  77 */     this.xpReward = 20;
/*     */     
/*  79 */     setPathfindingMalus(PathType.LEAVES, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  84 */     super.registerGoals();
/*     */     
/*  86 */     this.goalSelector.addGoal(0, new FloatGoal(this));
/*  87 */     this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
/*  88 */     this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.4D));
/*  89 */     this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 6.0F));
/*  90 */     this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, net.minecraft.world.entity.Mob.class, 8.0F));
/*     */     
/*  92 */     this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, new Class[] { Raider.class })).setAlertOthers(new Class[0]));
/*  93 */     this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.player.Player.class, true));
/*  94 */     this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.npc.villager.AbstractVillager.class, true, (target, level) -> !target.isBaby()));
/*  95 */     this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.animal.golem.IronGolem.class, true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateControlFlags() {
/* 100 */     boolean noController = (!(getControllingPassenger() instanceof net.minecraft.world.entity.Mob) || getControllingPassenger().getType().is(EntityTypeTags.RAIDERS));
/* 101 */     boolean notInBoat = !(getVehicle() instanceof net.minecraft.world.entity.vehicle.boat.AbstractBoat);
/* 102 */     this.goalSelector.setControlFlag(Goal.Flag.MOVE, noController);
/* 103 */     this.goalSelector.setControlFlag(Goal.Flag.JUMP, (noController && notInBoat));
/* 104 */     this.goalSelector.setControlFlag(Goal.Flag.LOOK, noController);
/* 105 */     this.goalSelector.setControlFlag(Goal.Flag.TARGET, noController);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 109 */     return Monster.createMonsterAttributes()
/* 110 */       .add(Attributes.MAX_HEALTH, 100.0D)
/* 111 */       .add(Attributes.MOVEMENT_SPEED, 0.3D)
/* 112 */       .add(Attributes.KNOCKBACK_RESISTANCE, 0.75D)
/* 113 */       .add(Attributes.ATTACK_DAMAGE, 12.0D)
/* 114 */       .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
/* 115 */       .add(Attributes.FOLLOW_RANGE, 32.0D)
/* 116 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 121 */     super.addAdditionalSaveData(output);
/*     */     
/* 123 */     output.putInt("AttackTick", this.attackTick);
/* 124 */     output.putInt("StunTick", this.stunnedTick);
/* 125 */     output.putInt("RoarTick", this.roarTick);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 130 */     super.readAdditionalSaveData(input);
/*     */     
/* 132 */     this.attackTick = input.getIntOr("AttackTick", 0);
/* 133 */     this.stunnedTick = input.getIntOr("StunTick", 0);
/* 134 */     this.roarTick = input.getIntOr("RoarTick", 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 139 */   public SoundEvent getCelebrateSound() { return SoundEvents.RAVAGER_CELEBRATE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 144 */   public int getMaxHeadYRot() { return 45; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 149 */     super.aiStep();
/*     */     
/* 151 */     if (!isAlive()) {
/*     */       return;
/*     */     }
/*     */     
/* 155 */     if (isImmobile()) {
/* 156 */       getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);
/*     */     } else {
/* 158 */       double maxSpeed = (getTarget() != null) ? 0.35D : 0.3D;
/* 159 */       double baseValue = getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue();
/* 160 */       getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Mth.lerp(0.1D, baseValue, maxSpeed));
/*     */     } 
/*     */     
/* 163 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 164 */       if (this.horizontalCollision && ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 165 */         boolean destroyedBlock = false;
/* 166 */         AABB bb = getBoundingBox().inflate(0.2D);
/* 167 */         for (BlockPos pos : BlockPos.betweenClosed(Mth.floor(bb.minX), Mth.floor(bb.minY), Mth.floor(bb.minZ), Mth.floor(bb.maxX), Mth.floor(bb.maxY), Mth.floor(bb.maxZ))) {
/* 168 */           BlockState state = serverLevel.getBlockState(pos);
/* 169 */           Block block = state.getBlock();
/* 170 */           if (block instanceof net.minecraft.world.level.block.LeavesBlock) {
/* 171 */             destroyedBlock = (serverLevel.destroyBlock(pos, true, this) || destroyedBlock);
/*     */           }
/*     */         } 
/*     */         
/* 175 */         if (!destroyedBlock && onGround()) {
/* 176 */           jumpFromGround();
/*     */         }
/*     */       }  }
/*     */ 
/*     */     
/* 181 */     if (this.roarTick > 0) {
/* 182 */       this.roarTick--;
/*     */       
/* 184 */       if (this.roarTick == 10) {
/* 185 */         roar();
/*     */       }
/*     */     } 
/* 188 */     if (this.attackTick > 0) {
/* 189 */       this.attackTick--;
/*     */     }
/* 191 */     if (this.stunnedTick > 0) {
/* 192 */       this.stunnedTick--;
/* 193 */       stunEffect();
/*     */       
/* 195 */       if (this.stunnedTick == 0) {
/* 196 */         playSound(SoundEvents.RAVAGER_ROAR, 1.0F, 1.0F);
/* 197 */         this.roarTick = 20;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void stunEffect() {
/* 203 */     if (this.random.nextInt(6) == 0) {
/* 204 */       double headX = getX() - getBbWidth() * Math.sin((this.yBodyRot * 0.017453292F)) + this.random.nextDouble() * 0.6D - 0.3D;
/* 205 */       double headY = getY() + getBbHeight() - 0.3D;
/* 206 */       double headZ = getZ() + getBbWidth() * Math.cos((this.yBodyRot * 0.017453292F)) + this.random.nextDouble() * 0.6D - 0.3D;
/*     */       
/* 208 */       level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.49803922F, 0.5137255F, 0.57254905F), headX, headY, headZ, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 214 */   protected boolean isImmobile() { return (super.isImmobile() || this.attackTick > 0 || this.stunnedTick > 0 || this.roarTick > 0); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLineOfSight(Entity target) {
/* 219 */     if (this.stunnedTick > 0 || this.roarTick > 0) {
/* 220 */       return false;
/*     */     }
/* 222 */     return super.hasLineOfSight(target);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void blockedByItem(LivingEntity defender) {
/* 227 */     if (this.roarTick == 0) {
/* 228 */       if (this.random.nextDouble() < 0.5D) {
/* 229 */         this.stunnedTick = 40;
/* 230 */         playSound(SoundEvents.RAVAGER_STUNNED, 1.0F, 1.0F);
/* 231 */         level().broadcastEntityEvent(this, (byte)39);
/*     */         
/* 233 */         defender.push(this);
/*     */       } else {
/* 235 */         strongKnockback(defender);
/*     */       } 
/* 237 */       defender.hurtMarked = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void roar() {
/* 242 */     if (isAlive()) { Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/* 243 */         Predicate<Entity> targetSelector = ((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() ? ROAR_TARGET_WITH_GRIEFING : ROAR_TARGET_WITHOUT_GRIEFING;
/* 244 */         List<? extends LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(4.0D), targetSelector);
/* 245 */         for (LivingEntity entity : entities) {
/* 246 */           if (!(entity instanceof net.minecraft.world.entity.monster.illager.AbstractIllager)) {
/* 247 */             entity.hurtServer(level, damageSources().mobAttack(this), 6.0F);
/*     */           }
/*     */           
/* 250 */           if (!(entity instanceof net.minecraft.world.entity.player.Player)) {
/* 251 */             strongKnockback(entity);
/*     */           }
/*     */         } 
/*     */         
/* 255 */         gameEvent(GameEvent.ENTITY_ACTION);
/* 256 */         level.broadcastEntityEvent(this, (byte)69); }
/*     */        }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void applyRoarKnockbackClient() {
/* 264 */     List<? extends LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(4.0D), ROAR_TARGET_ON_CLIENT);
/* 265 */     for (LivingEntity entity : entities) {
/* 266 */       strongKnockback(entity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void strongKnockback(Entity entity) {
/* 272 */     double xd = entity.getX() - getX();
/* 273 */     double zd = entity.getZ() - getZ();
/* 274 */     double dd = Math.max(xd * xd + zd * zd, 0.001D);
/* 275 */     entity.push(xd / dd * 4.0D, 0.2D, zd / dd * 4.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 280 */     if (id == 4) {
/* 281 */       this.attackTick = 10;
/* 282 */       playSound(SoundEvents.RAVAGER_ATTACK, 1.0F, 1.0F);
/* 283 */     } else if (id == 39) {
/* 284 */       this.stunnedTick = 40;
/* 285 */     } else if (id == 69) {
/* 286 */       addRoarParticleEffects();
/* 287 */       applyRoarKnockbackClient();
/*     */     } 
/* 289 */     super.handleEntityEvent(id);
/*     */   }
/*     */   
/*     */   private void addRoarParticleEffects() {
/* 293 */     Vec3 center = getBoundingBox().getCenter();
/* 294 */     for (int i = 0; i < 40; i++) {
/* 295 */       double velocityX = this.random.nextGaussian() * 0.2D;
/* 296 */       double velocityY = this.random.nextGaussian() * 0.2D;
/* 297 */       double velocityZ = this.random.nextGaussian() * 0.2D;
/* 298 */       level().addParticle(ParticleTypes.POOF, center.x, center.y, center.z, velocityX, velocityY, velocityZ);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 303 */   public int getAttackTick() { return this.attackTick; }
/*     */ 
/*     */ 
/*     */   
/* 307 */   public int getStunnedTick() { return this.stunnedTick; }
/*     */ 
/*     */ 
/*     */   
/* 311 */   public int getRoarTick() { return this.roarTick; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/* 316 */     this.attackTick = 10;
/* 317 */     level.broadcastEntityEvent(this, (byte)4);
/* 318 */     playSound(SoundEvents.RAVAGER_ATTACK, 1.0F, 1.0F);
/*     */     
/* 320 */     return super.doHurtTarget(level, target);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 325 */   protected SoundEvent getAmbientSound() { return SoundEvents.RAVAGER_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 330 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.RAVAGER_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 335 */   protected SoundEvent getDeathSound() { return SoundEvents.RAVAGER_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 340 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.RAVAGER_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 345 */   public boolean checkSpawnObstruction(LevelReader level) { return !level.containsAnyLiquid(getBoundingBox()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyRaidBuffs(ServerLevel level, int wave, boolean isCaptain) {}
/*     */ 
/*     */ 
/*     */   
/* 354 */   public boolean canBeLeader() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AABB getAttackBoundingBox(double horizontalExpansion) {
/* 360 */     AABB defaultBB = super.getAttackBoundingBox(horizontalExpansion);
/* 361 */     return defaultBB.deflate(0.05D, 0.0D, 0.05D);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Ravager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */