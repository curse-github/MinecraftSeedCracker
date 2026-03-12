/*     */ package net.minecraft.world.entity.boss.enderdragon;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhaseManager;
/*     */ import net.minecraft.world.entity.monster.Enemy;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.dimension.end.EndDragonFight;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*     */ import net.minecraft.world.level.pathfinder.BinaryHeap;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnderDragon
/*     */   extends Mob
/*     */   implements Enemy
/*     */ {
/*  60 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  62 */   public static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(EnderDragon.class, EntityDataSerializers.INT);
/*     */   
/*  64 */   private static final TargetingConditions CRYSTAL_DESTROY_TARGETING = TargetingConditions.forCombat().range(64.0D);
/*     */   
/*     */   private static final int GROWL_INTERVAL_MIN = 200;
/*     */   
/*     */   private static final int GROWL_INTERVAL_MAX = 400;
/*     */   private static final float SITTING_ALLOWED_DAMAGE_PERCENTAGE = 0.25F;
/*     */   private static final String DRAGON_DEATH_TIME_KEY = "DragonDeathTime";
/*     */   private static final String DRAGON_PHASE_KEY = "DragonPhase";
/*     */   private static final int DEFAULT_DEATH_TIME = 0;
/*  73 */   public final DragonFlightHistory flightHistory = new DragonFlightHistory();
/*     */   
/*     */   private final EnderDragonPart[] subEntities;
/*     */   
/*     */   public final EnderDragonPart head;
/*     */   private final EnderDragonPart neck;
/*     */   private final EnderDragonPart body;
/*     */   private final EnderDragonPart tail1;
/*     */   private final EnderDragonPart tail2;
/*     */   private final EnderDragonPart tail3;
/*     */   private final EnderDragonPart wing1;
/*     */   private final EnderDragonPart wing2;
/*     */   public float oFlapTime;
/*     */   public float flapTime;
/*     */   public boolean inWall;
/*  88 */   public int dragonDeathTime = 0;
/*     */   
/*     */   public float yRotA;
/*     */   
/*     */   public EndCrystal nearestCrystal;
/*     */   private EndDragonFight dragonFight;
/*  94 */   private BlockPos fightOrigin = BlockPos.ZERO;
/*     */   private final EnderDragonPhaseManager phaseManager;
/*  96 */   private int growlTime = 100;
/*     */   private float sittingDamageReceived;
/*  98 */   private final Node[] nodes = new Node[24];
/*  99 */   private final int[] nodeAdjacency = new int[24];
/* 100 */   private final BinaryHeap openSet = new BinaryHeap();
/*     */   
/*     */   public EnderDragon(EntityType<? extends EnderDragon> type, Level level) {
/* 103 */     super(EntityType.ENDER_DRAGON, level);
/*     */     
/* 105 */     this.head = new EnderDragonPart(this, "head", 1.0F, 1.0F);
/* 106 */     this.neck = new EnderDragonPart(this, "neck", 3.0F, 3.0F);
/* 107 */     this.body = new EnderDragonPart(this, "body", 5.0F, 3.0F);
/* 108 */     this.tail1 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
/* 109 */     this.tail2 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
/* 110 */     this.tail3 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
/* 111 */     this.wing1 = new EnderDragonPart(this, "wing", 4.0F, 2.0F);
/* 112 */     this.wing2 = new EnderDragonPart(this, "wing", 4.0F, 2.0F);
/*     */     
/* 114 */     this.subEntities = new EnderDragonPart[] { this.head, this.neck, this.body, this.tail1, this.tail2, this.tail3, this.wing1, this.wing2 };
/*     */     
/* 116 */     setHealth(getMaxHealth());
/*     */     
/* 118 */     this.noPhysics = true;
/*     */     
/* 120 */     this.phaseManager = new EnderDragonPhaseManager(this);
/*     */   }
/*     */ 
/*     */   
/* 124 */   public void setDragonFight(EndDragonFight fight) { this.dragonFight = fight; }
/*     */ 
/*     */ 
/*     */   
/* 128 */   public void setFightOrigin(BlockPos fightOrigin) { this.fightOrigin = fightOrigin; }
/*     */ 
/*     */ 
/*     */   
/* 132 */   public BlockPos getFightOrigin() { return this.fightOrigin; }
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 136 */     return Mob.createMobAttributes()
/* 137 */       .add(Attributes.MAX_HEALTH, 200.0D)
/* 138 */       .add(Attributes.CAMERA_DISTANCE, 16.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFlapping() {
/* 143 */     float flap = Mth.cos((this.flapTime * 6.2831855F));
/* 144 */     float oldFlap = Mth.cos((this.oFlapTime * 6.2831855F));
/*     */     
/* 146 */     return (oldFlap <= -0.3F && flap >= -0.3F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onFlap() {
/* 151 */     if (level().isClientSide() && !isSilent()) {
/* 152 */       level().playLocalSound(getX(), getY(), getZ(), SoundEvents.ENDER_DRAGON_FLAP, getSoundSource(), 5.0F, 0.8F + this.random.nextFloat() * 0.3F, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 158 */     super.defineSynchedData(entityData);
/* 159 */     entityData.define(DATA_PHASE, Integer.valueOf(EnderDragonPhase.HOVERING.getId()));
/*     */   }
/*     */   
/*     */   public void aiStep() {
/*     */     ServerLevel level;
/* 164 */     processFlappingMovement();
/*     */     
/* 166 */     if (level().isClientSide()) {
/* 167 */       setHealth(getHealth());
/*     */       
/* 169 */       if (!isSilent() && 
/* 170 */         !this.phaseManager.getCurrentPhase().isSitting() && --this.growlTime < 0) {
/* 171 */         level().playLocalSound(getX(), getY(), getZ(), SoundEvents.ENDER_DRAGON_GROWL, getSoundSource(), 2.5F, 0.8F + this.random.nextFloat() * 0.3F, false);
/* 172 */         this.growlTime = 200 + this.random.nextInt(200);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 177 */     if (this.dragonFight == null) { Level level2 = level(); if (level2 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level2;
/* 178 */         EndDragonFight maybeOurFight = serverLevel.getDragonFight();
/* 179 */         if (maybeOurFight != null && getUUID().equals(maybeOurFight.getDragonUUID())) {
/* 180 */           this.dragonFight = maybeOurFight;
/*     */         } }
/*     */        }
/*     */     
/* 184 */     this.oFlapTime = this.flapTime;
/*     */     
/* 186 */     if (isDeadOrDying()) {
/* 187 */       float xo = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 188 */       float yo = (this.random.nextFloat() - 0.5F) * 4.0F;
/* 189 */       float zo = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 190 */       level().addParticle(ParticleTypes.EXPLOSION, getX() + xo, getY() + 2.0D + yo, getZ() + zo, 0.0D, 0.0D, 0.0D);
/*     */       
/*     */       return;
/*     */     } 
/* 194 */     checkCrystals();
/*     */     
/* 196 */     Vec3 movement = getDeltaMovement();
/* 197 */     float flapSpeed = 0.2F / ((float)movement.horizontalDistance() * 10.0F + 1.0F);
/* 198 */     flapSpeed *= (float)Math.pow(2.0D, movement.y);
/* 199 */     if (this.phaseManager.getCurrentPhase().isSitting()) {
/* 200 */       this.flapTime += 0.1F;
/* 201 */     } else if (this.inWall) {
/* 202 */       this.flapTime += flapSpeed * 0.5F;
/*     */     } else {
/* 204 */       this.flapTime += flapSpeed;
/*     */     } 
/*     */     
/* 207 */     setYRot(Mth.wrapDegrees(getYRot()));
/*     */     
/* 209 */     if (isNoAi()) {
/* 210 */       this.flapTime = 0.5F;
/*     */       
/*     */       return;
/*     */     } 
/* 214 */     this.flightHistory.record(getY(), getYRot());
/*     */     
/* 216 */     Level level1 = level(); if (level1 instanceof ServerLevel) { level = (ServerLevel)level1; }
/* 217 */     else { this.interpolation.interpolate();
/*     */       
/* 219 */       this.phaseManager.getCurrentPhase().doClientTick();
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
/*     */ 
/*     */       
/* 272 */       if (!level().isClientSide())
/* 273 */         applyEffectsFromBlocks();  }  DragonPhaseInstance currentPhase = this.phaseManager.getCurrentPhase(); currentPhase.doServerTick(level); if (this.phaseManager.getCurrentPhase() != currentPhase) { currentPhase = this.phaseManager.getCurrentPhase(); currentPhase.doServerTick(level); }  Vec3 targetLocation = currentPhase.getFlyTargetLocation(); if (targetLocation != null) { double xdd = targetLocation.x - getX(); double ydd = targetLocation.y - getY(); double zdd = targetLocation.z - getZ(); double distToTarget = xdd * xdd + ydd * ydd + zdd * zdd; float max = currentPhase.getFlySpeed(); double horizontalDist = Math.sqrt(xdd * xdd + zdd * zdd); if (horizontalDist > 0.0D) ydd = Mth.clamp(ydd / horizontalDist, -max, max);  setDeltaMovement(getDeltaMovement().add(0.0D, ydd * 0.01D, 0.0D)); setYRot(Mth.wrapDegrees(getYRot())); Vec3 aim = targetLocation.subtract(getX(), getY(), getZ()).normalize(); Vec3 dir = (new Vec3(Mth.sin((getYRot() * 0.017453292F)), (getDeltaMovement()).y, -Mth.cos((getYRot() * 0.017453292F)))).normalize(); float dot = Math.max(((float)dir.dot(aim) + 0.5F) / 1.5F, 0.0F); if (Math.abs(xdd) > 9.999999747378752E-6D || Math.abs(zdd) > 9.999999747378752E-6D) { float yRotD = Mth.clamp(Mth.wrapDegrees(180.0F - (float)Mth.atan2(xdd, zdd) * 57.295776F - getYRot()), -50.0F, 50.0F); this.yRotA *= 0.8F; this.yRotA += yRotD * currentPhase.getTurnSpeed(); setYRot(getYRot() + this.yRotA * 0.1F); }  float span = (float)(2.0D / (distToTarget + 1.0D)); float speed = 0.06F; moveRelative(0.06F * (dot * span + 1.0F - span), new Vec3(0.0D, 0.0D, -1.0D)); if (this.inWall) { move(MoverType.SELF, getDeltaMovement().scale(0.800000011920929D)); } else { move(MoverType.SELF, getDeltaMovement()); }  Vec3 actual = getDeltaMovement().normalize(); double slide = 0.8D + 0.15D * (actual.dot(dir) + 1.0D) / 2.0D; setDeltaMovement(getDeltaMovement().multiply(slide, 0.9100000262260437D, slide)); }  if (!level().isClientSide()) applyEffectsFromBlocks();
/*     */   
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 353 */   private void tickPart(EnderDragonPart part, double x, double y, double z) { part.setPos(getX() + x, getY() + y, getZ() + z); }
/*     */ 
/*     */   
/*     */   private float getHeadYOffset() {
/* 357 */     if (this.phaseManager.getCurrentPhase().isSitting()) {
/* 358 */       return -1.0F;
/*     */     }
/* 360 */     DragonFlightHistory.Sample p0 = this.flightHistory.get(5);
/* 361 */     DragonFlightHistory.Sample p1 = this.flightHistory.get(0);
/* 362 */     return (float)(p0.y() - p1.y());
/*     */   }
/*     */   
/*     */   private void checkCrystals() {
/* 366 */     if (this.nearestCrystal != null) {
/* 367 */       if (this.nearestCrystal.isRemoved()) {
/* 368 */         this.nearestCrystal = null;
/* 369 */       } else if (this.tickCount % 10 == 0 && 
/* 370 */         getHealth() < getMaxHealth()) {
/* 371 */         setHealth(getHealth() + 1.0F);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 376 */     if (this.random.nextInt(10) == 0) {
/* 377 */       List<EndCrystal> crystals = level().getEntitiesOfClass(EndCrystal.class, getBoundingBox().inflate(32.0D));
/*     */       
/* 379 */       EndCrystal nearest = null;
/* 380 */       double distance = Double.MAX_VALUE;
/* 381 */       for (EndCrystal crystal : crystals) {
/* 382 */         double dist = crystal.distanceToSqr(this);
/* 383 */         if (dist < distance) {
/* 384 */           distance = dist;
/* 385 */           nearest = crystal;
/*     */         } 
/*     */       } 
/*     */       
/* 389 */       this.nearestCrystal = nearest;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void knockBack(ServerLevel serverLevel, List<Entity> entities) {
/* 394 */     double xm = ((this.body.getBoundingBox()).minX + (this.body.getBoundingBox()).maxX) / 2.0D;
/* 395 */     double zm = ((this.body.getBoundingBox()).minZ + (this.body.getBoundingBox()).maxZ) / 2.0D;
/*     */     
/* 397 */     for (Entity entity : entities) {
/* 398 */       if (entity instanceof LivingEntity) { LivingEntity livingTarget = (LivingEntity)entity;
/* 399 */         double xd = entity.getX() - xm;
/* 400 */         double zd = entity.getZ() - zm;
/* 401 */         double dd = Math.max(xd * xd + zd * zd, 0.1D);
/* 402 */         entity.push(xd / dd * 4.0D, 0.20000000298023224D, zd / dd * 4.0D);
/* 403 */         if (!this.phaseManager.getCurrentPhase().isSitting() && livingTarget.getLastHurtByMobTimestamp() < entity.tickCount - 2) {
/* 404 */           DamageSource damageSource = damageSources().mobAttack(this);
/* 405 */           entity.hurtServer(serverLevel, damageSource, 5.0F);
/* 406 */           EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
/*     */         }  }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   private void hurt(ServerLevel level, List<Entity> entities) {
/* 413 */     for (Entity target : entities) {
/* 414 */       if (target instanceof LivingEntity) {
/* 415 */         DamageSource damageSource = damageSources().mobAttack(this);
/* 416 */         target.hurtServer(level, damageSource, 10.0F);
/* 417 */         EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 423 */   private float rotWrap(double d) { return (float)Mth.wrapDegrees(d); }
/*     */ 
/*     */   
/*     */   private boolean checkWalls(ServerLevel level, AABB bb) {
/* 427 */     int x0 = Mth.floor(bb.minX);
/* 428 */     int y0 = Mth.floor(bb.minY);
/* 429 */     int z0 = Mth.floor(bb.minZ);
/* 430 */     int x1 = Mth.floor(bb.maxX);
/* 431 */     int y1 = Mth.floor(bb.maxY);
/* 432 */     int z1 = Mth.floor(bb.maxZ);
/* 433 */     boolean hitWall = false;
/* 434 */     boolean destroyedBlock = false;
/* 435 */     for (int x = x0; x <= x1; x++) {
/* 436 */       for (int y = y0; y <= y1; y++) {
/* 437 */         for (int z = z0; z <= z1; z++) {
/* 438 */           BlockPos blockPos = new BlockPos(x, y, z);
/* 439 */           BlockState state = level.getBlockState(blockPos);
/* 440 */           if (!state.isAir() && !state.is(BlockTags.DRAGON_TRANSPARENT))
/*     */           {
/* 442 */             if (!((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() || state.is(BlockTags.DRAGON_IMMUNE)) {
/* 443 */               hitWall = true;
/*     */             } else {
/* 445 */               destroyedBlock = (level.removeBlock(blockPos, false) || destroyedBlock);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 451 */     if (destroyedBlock) {
/*     */ 
/*     */ 
/*     */       
/* 455 */       BlockPos randomPos = new BlockPos(x0 + this.random.nextInt(x1 - x0 + 1), y0 + this.random.nextInt(y1 - y0 + 1), z0 + this.random.nextInt(z1 - z0 + 1));
/*     */       
/* 457 */       level.levelEvent(2008, randomPos, 0);
/*     */     } 
/*     */     
/* 460 */     return hitWall;
/*     */   }
/*     */   
/*     */   public boolean hurt(ServerLevel level, EnderDragonPart part, DamageSource source, float damage) {
/* 464 */     if (this.phaseManager.getCurrentPhase().getPhase() == EnderDragonPhase.DYING) {
/* 465 */       return false;
/*     */     }
/*     */     
/* 468 */     damage = this.phaseManager.getCurrentPhase().onHurt(source, damage);
/*     */     
/* 470 */     if (part != this.head) {
/* 471 */       damage = damage / 4.0F + Math.min(damage, 1.0F);
/*     */     }
/*     */     
/* 474 */     if (damage < 0.01F) {
/* 475 */       return false;
/*     */     }
/*     */     
/* 478 */     if (source.getEntity() instanceof Player || source.is(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS)) {
/* 479 */       float healthBefore = getHealth();
/* 480 */       reallyHurt(level, source, damage);
/*     */       
/* 482 */       if (isDeadOrDying() && !this.phaseManager.getCurrentPhase().isSitting()) {
/* 483 */         setHealth(1.0F);
/* 484 */         this.phaseManager.setPhase(EnderDragonPhase.DYING);
/*     */       } 
/*     */       
/* 487 */       if (this.phaseManager.getCurrentPhase().isSitting()) {
/* 488 */         this.sittingDamageReceived = this.sittingDamageReceived + healthBefore - getHealth();
/*     */         
/* 490 */         if (this.sittingDamageReceived > 0.25F * getMaxHealth()) {
/* 491 */           this.sittingDamageReceived = 0.0F;
/* 492 */           this.phaseManager.setPhase(EnderDragonPhase.TAKEOFF);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 497 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 502 */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) { return hurt(level, this.body, source, damage); }
/*     */ 
/*     */ 
/*     */   
/* 506 */   protected void reallyHurt(ServerLevel level, DamageSource source, float damage) { super.hurtServer(level, source, damage); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void kill(ServerLevel level) {
/* 511 */     remove(Entity.RemovalReason.KILLED);
/* 512 */     gameEvent(GameEvent.ENTITY_DIE);
/*     */     
/* 514 */     if (this.dragonFight != null) {
/* 515 */       this.dragonFight.updateDragon(this);
/* 516 */       this.dragonFight.setDragonKilled(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tickDeath() {
/* 522 */     if (this.dragonFight != null) {
/* 523 */       this.dragonFight.updateDragon(this);
/*     */     }
/*     */     
/* 526 */     this.dragonDeathTime++;
/* 527 */     if (this.dragonDeathTime >= 180 && this.dragonDeathTime <= 200) {
/* 528 */       float xo = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 529 */       float yo = (this.random.nextFloat() - 0.5F) * 4.0F;
/* 530 */       float zo = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 531 */       level().addParticle(ParticleTypes.EXPLOSION_EMITTER, getX() + xo, getY() + 2.0D + yo, getZ() + zo, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */     
/* 534 */     int xpCount = 500;
/* 535 */     if (this.dragonFight != null && !this.dragonFight.hasPreviouslyKilledDragon()) {
/* 536 */       xpCount = 12000;
/*     */     }
/*     */     
/* 539 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/* 540 */       if (this.dragonDeathTime > 150 && this.dragonDeathTime % 5 == 0 && ((Boolean)level.getGameRules().get(GameRules.MOB_DROPS)).booleanValue()) {
/* 541 */         ExperienceOrb.award(level, position(), Mth.floor(xpCount * 0.08F));
/*     */       }
/* 543 */       if (this.dragonDeathTime == 1 && !isSilent()) {
/* 544 */         level.globalLevelEvent(1028, blockPosition(), 0);
/*     */       } }
/*     */ 
/*     */     
/* 548 */     Vec3 deathMove = new Vec3(0.0D, 0.10000000149011612D, 0.0D);
/* 549 */     move(MoverType.SELF, deathMove);
/* 550 */     for (EnderDragonPart dragonPart : this.subEntities) {
/* 551 */       dragonPart.setOldPosAndRot();
/* 552 */       dragonPart.setPos(dragonPart.position().add(deathMove));
/*     */     } 
/*     */     
/* 555 */     if (this.dragonDeathTime == 200) { Level level2 = level(); if (level2 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level2;
/* 556 */         if (((Boolean)level.getGameRules().get(GameRules.MOB_DROPS)).booleanValue()) {
/* 557 */           ExperienceOrb.award(level, position(), Mth.floor(xpCount * 0.2F));
/*     */         }
/* 559 */         if (this.dragonFight != null) {
/* 560 */           this.dragonFight.setDragonKilled(this);
/*     */         }
/* 562 */         remove(Entity.RemovalReason.KILLED);
/* 563 */         gameEvent(GameEvent.ENTITY_DIE); }
/*     */        }
/*     */   
/*     */   }
/*     */   
/*     */   public int findClosestNode() {
/* 569 */     if (this.nodes[false] == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 575 */       for (int i = 0; i < 24; i++) {
/* 576 */         int nodeZ, nodeX, yAdjustment = 5;
/* 577 */         int multiplier = i;
/*     */ 
/*     */ 
/*     */         
/* 581 */         if (i < 12) {
/* 582 */           nodeX = Mth.floor(60.0F * Mth.cos((2.0F * (-3.1415927F + 0.2617994F * multiplier))));
/* 583 */           nodeZ = Mth.floor(60.0F * Mth.sin((2.0F * (-3.1415927F + 0.2617994F * multiplier))));
/* 584 */         } else if (i < 20) {
/* 585 */           multiplier -= 12;
/* 586 */           nodeX = Mth.floor(40.0F * Mth.cos((2.0F * (-3.1415927F + 0.3926991F * multiplier))));
/* 587 */           nodeZ = Mth.floor(40.0F * Mth.sin((2.0F * (-3.1415927F + 0.3926991F * multiplier))));
/* 588 */           yAdjustment += 10;
/*     */         } else {
/* 590 */           multiplier -= 20;
/* 591 */           nodeX = Mth.floor(20.0F * Mth.cos((2.0F * (-3.1415927F + 0.7853982F * multiplier))));
/* 592 */           nodeZ = Mth.floor(20.0F * Mth.sin((2.0F * (-3.1415927F + 0.7853982F * multiplier))));
/*     */         } 
/*     */ 
/*     */         
/* 596 */         int nodeY = Math.max(73, level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(nodeX, 0, nodeZ)).getY() + yAdjustment);
/*     */         
/* 598 */         this.nodes[i] = new Node(nodeX, nodeY, nodeZ);
/*     */       } 
/*     */       
/* 601 */       this.nodeAdjacency[0] = 6146;
/* 602 */       this.nodeAdjacency[1] = 8197;
/* 603 */       this.nodeAdjacency[2] = 8202;
/* 604 */       this.nodeAdjacency[3] = 16404;
/* 605 */       this.nodeAdjacency[4] = 32808;
/* 606 */       this.nodeAdjacency[5] = 32848;
/* 607 */       this.nodeAdjacency[6] = 65696;
/* 608 */       this.nodeAdjacency[7] = 131392;
/* 609 */       this.nodeAdjacency[8] = 131712;
/* 610 */       this.nodeAdjacency[9] = 263424;
/* 611 */       this.nodeAdjacency[10] = 526848;
/* 612 */       this.nodeAdjacency[11] = 525313;
/*     */       
/* 614 */       this.nodeAdjacency[12] = 1581057;
/* 615 */       this.nodeAdjacency[13] = 3166214;
/* 616 */       this.nodeAdjacency[14] = 2138120;
/* 617 */       this.nodeAdjacency[15] = 6373424;
/* 618 */       this.nodeAdjacency[16] = 4358208;
/* 619 */       this.nodeAdjacency[17] = 12910976;
/* 620 */       this.nodeAdjacency[18] = 9044480;
/* 621 */       this.nodeAdjacency[19] = 9706496;
/*     */       
/* 623 */       this.nodeAdjacency[20] = 15216640;
/* 624 */       this.nodeAdjacency[21] = 13688832;
/* 625 */       this.nodeAdjacency[22] = 11763712;
/* 626 */       this.nodeAdjacency[23] = 8257536;
/*     */     } 
/*     */     
/* 629 */     return findClosestNode(getX(), getY(), getZ());
/*     */   }
/*     */   
/*     */   public int findClosestNode(double tX, double tY, double tZ) {
/* 633 */     float closestDist = 10000.0F;
/* 634 */     int closestIndex = 0;
/* 635 */     Node currentPos = new Node(Mth.floor(tX), Mth.floor(tY), Mth.floor(tZ));
/* 636 */     int startIndex = 0;
/*     */     
/* 638 */     if (this.dragonFight == null || this.dragonFight.getCrystalsAlive() == 0)
/*     */     {
/* 640 */       startIndex = 12;
/*     */     }
/*     */     
/* 643 */     for (int i = startIndex; i < 24; i++) {
/* 644 */       if (this.nodes[i] != null) {
/* 645 */         float dist = this.nodes[i].distanceToSqr(currentPos);
/* 646 */         if (dist < closestDist) {
/* 647 */           closestDist = dist;
/* 648 */           closestIndex = i;
/*     */         } 
/*     */       } 
/*     */     } 
/* 652 */     return closestIndex;
/*     */   }
/*     */   
/*     */   public Path findPath(int startIndex, int endIndex, Node finalNode) {
/* 656 */     for (int i = 0; i < 24; i++) {
/* 657 */       Node node = this.nodes[i];
/* 658 */       node.closed = false;
/* 659 */       node.f = 0.0F;
/* 660 */       node.g = 0.0F;
/* 661 */       node.h = 0.0F;
/* 662 */       node.cameFrom = null;
/* 663 */       node.heapIdx = -1;
/*     */     } 
/*     */     
/* 666 */     Node from = this.nodes[startIndex];
/* 667 */     Node to = this.nodes[endIndex];
/*     */     
/* 669 */     from.g = 0.0F;
/* 670 */     from.h = from.distanceTo(to);
/* 671 */     from.f = from.h;
/*     */     
/* 673 */     this.openSet.clear();
/* 674 */     this.openSet.insert(from);
/*     */     
/* 676 */     Node closest = from;
/*     */     
/* 678 */     int minimumNodeIndex = 0;
/* 679 */     if (this.dragonFight == null || this.dragonFight.getCrystalsAlive() == 0)
/*     */     {
/* 681 */       minimumNodeIndex = 12;
/*     */     }
/*     */     
/* 684 */     while (!this.openSet.isEmpty()) {
/* 685 */       Node openNode = this.openSet.pop();
/*     */       
/* 687 */       if (openNode.equals(to)) {
/* 688 */         if (finalNode != null) {
/* 689 */           finalNode.cameFrom = to;
/* 690 */           to = finalNode;
/*     */         } 
/* 692 */         return reconstructPath(from, to);
/*     */       } 
/*     */       
/* 695 */       if (openNode.distanceTo(to) < closest.distanceTo(to)) {
/* 696 */         closest = openNode;
/*     */       }
/* 698 */       openNode.closed = true;
/*     */       
/* 700 */       int xIndex = 0;
/* 701 */       for (int i = 0; i < 24; i++) {
/* 702 */         if (this.nodes[i] == openNode) {
/* 703 */           xIndex = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 708 */       for (int i = minimumNodeIndex; i < 24; i++) {
/* 709 */         if ((this.nodeAdjacency[xIndex] & 1 << i) > 0) {
/* 710 */           Node adjacentNode = this.nodes[i];
/*     */           
/* 712 */           if (!adjacentNode.closed) {
/*     */ 
/*     */ 
/*     */             
/* 716 */             float tentativeGScore = openNode.g + openNode.distanceTo(adjacentNode);
/* 717 */             if (!adjacentNode.inOpenSet() || tentativeGScore < adjacentNode.g) {
/* 718 */               adjacentNode.cameFrom = openNode;
/* 719 */               adjacentNode.g = tentativeGScore;
/* 720 */               adjacentNode.h = adjacentNode.distanceTo(to);
/* 721 */               if (adjacentNode.inOpenSet()) {
/* 722 */                 this.openSet.changeCost(adjacentNode, adjacentNode.g + adjacentNode.h);
/*     */               } else {
/* 724 */                 adjacentNode.f = adjacentNode.g + adjacentNode.h;
/* 725 */                 this.openSet.insert(adjacentNode);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 732 */     if (closest == from) {
/* 733 */       return null;
/*     */     }
/* 735 */     LOGGER.debug("Failed to find path from {} to {}", Integer.valueOf(startIndex), Integer.valueOf(endIndex));
/* 736 */     if (finalNode != null) {
/* 737 */       finalNode.cameFrom = closest;
/* 738 */       closest = finalNode;
/*     */     } 
/* 740 */     return reconstructPath(from, closest);
/*     */   }
/*     */   
/*     */   private Path reconstructPath(Node from, Node to) {
/* 744 */     List<Node> nodes = Lists.newArrayList();
/* 745 */     Node node = to;
/* 746 */     nodes.add(0, node);
/* 747 */     while (node.cameFrom != null) {
/* 748 */       node = node.cameFrom;
/* 749 */       nodes.add(0, node);
/*     */     } 
/* 751 */     return new Path(nodes, new BlockPos(to.x, to.y, to.z), true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 756 */     super.addAdditionalSaveData(output);
/* 757 */     output.putInt("DragonPhase", this.phaseManager.getCurrentPhase().getPhase().getId());
/* 758 */     output.putInt("DragonDeathTime", this.dragonDeathTime);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 763 */     super.readAdditionalSaveData(input);
/* 764 */     input.getInt("DragonPhase").ifPresent(phaseId -> 
/* 765 */         this.phaseManager.setPhase(EnderDragonPhase.getById(phaseId.intValue())));
/*     */     
/* 767 */     this.dragonDeathTime = input.getIntOr("DragonDeathTime", 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkDespawn() {}
/*     */ 
/*     */   
/* 775 */   public EnderDragonPart[] getSubEntities() { return this.subEntities; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 780 */   public boolean isPickable() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 785 */   public SoundSource getSoundSource() { return SoundSource.HOSTILE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 790 */   protected SoundEvent getAmbientSound() { return SoundEvents.ENDER_DRAGON_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 795 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ENDER_DRAGON_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 800 */   protected float getSoundVolume() { return 5.0F; }
/*     */   
/*     */   public Vec3 getHeadLookVector(float a) {
/*     */     Vec3 result;
/* 804 */     DragonPhaseInstance phaseInstance = this.phaseManager.getCurrentPhase();
/* 805 */     EnderDragonPhase<? extends DragonPhaseInstance> phase = phaseInstance.getPhase();
/*     */ 
/*     */     
/* 808 */     if (phase == EnderDragonPhase.LANDING || phase == EnderDragonPhase.TAKEOFF) {
/* 809 */       BlockPos egg = level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.fightOrigin));
/* 810 */       float dist = Math.max((float)Math.sqrt(egg.distToCenterSqr(position())) / 4.0F, 1.0F);
/* 811 */       float yOffset = 6.0F / dist;
/*     */       
/* 813 */       float xRotOld = getXRot();
/* 814 */       float rotScale = 1.5F;
/* 815 */       setXRot(-yOffset * 1.5F * 5.0F);
/*     */       
/* 817 */       result = getViewVector(a);
/* 818 */       setXRot(xRotOld);
/* 819 */     } else if (phaseInstance.isSitting()) {
/* 820 */       float xRotOld = getXRot();
/* 821 */       float rotScale = 1.5F;
/* 822 */       setXRot(-45.0F);
/*     */       
/* 824 */       result = getViewVector(a);
/* 825 */       setXRot(xRotOld);
/*     */     } else {
/* 827 */       result = getViewVector(a);
/*     */     } 
/*     */     
/* 830 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCrystalDestroyed(ServerLevel level, EndCrystal crystal, BlockPos pos, DamageSource source) {
/*     */     Player player;
/* 836 */     Entity entity = source.getEntity(); if (entity instanceof Player) { Player playerSource = (Player)entity;
/* 837 */       player = playerSource; }
/*     */     else
/* 839 */     { player = level.getNearestPlayer(CRYSTAL_DESTROY_TARGETING, pos.getX(), pos.getY(), pos.getZ()); }
/*     */ 
/*     */     
/* 842 */     if (crystal == this.nearestCrystal) {
/* 843 */       hurt(level, this.head, damageSources().explosion(crystal, player), 10.0F);
/*     */     }
/*     */     
/* 846 */     this.phaseManager.getCurrentPhase().onCrystalDestroyed(crystal, pos, source, player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 851 */     if (DATA_PHASE.equals(accessor) && level().isClientSide()) {
/* 852 */       this.phaseManager.setPhase(EnderDragonPhase.getById(((Integer)getEntityData().get(DATA_PHASE)).intValue()));
/*     */     }
/*     */     
/* 855 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */   
/* 859 */   public EnderDragonPhaseManager getPhaseManager() { return this.phaseManager; }
/*     */ 
/*     */ 
/*     */   
/* 863 */   public EndDragonFight getDragonFight() { return this.dragonFight; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 868 */   public boolean addEffect(MobEffectInstance newEffect, Entity source) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 873 */   protected boolean canRide(Entity vehicle) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 878 */   public boolean canUsePortal(boolean ignorePassenger) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 883 */     super.recreateFromPacket(packet);
/* 884 */     EnderDragonPart[] subEntities = getSubEntities();
/* 885 */     for (int i = 0; i < subEntities.length; i++) {
/* 886 */       subEntities[i].setId(i + packet.getId() + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 893 */   public boolean canAttack(LivingEntity target) { return target.canBeSeenAsEnemy(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 899 */   protected float sanitizeScale(float scale) { return 1.0F; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\EnderDragon.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */