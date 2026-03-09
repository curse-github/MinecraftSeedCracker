/*     */ package net.minecraft.world.entity.monster.warden;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Collections;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.GameEventTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffectUtil;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.DynamicGameEventListener;
/*     */ import net.minecraft.world.level.gameevent.EntityPositionSource;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Warden
/*     */   extends Monster
/*     */   implements VibrationSystem
/*     */ {
/*     */   private static final int VIBRATION_COOLDOWN_TICKS = 40;
/*     */   private static final int TIME_TO_USE_MELEE_UNTIL_SONIC_BOOM = 200;
/*     */   private static final int MAX_HEALTH = 500;
/*     */   private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.3F;
/*     */   private static final float KNOCKBACK_RESISTANCE = 1.0F;
/*     */   private static final float ATTACK_KNOCKBACK = 1.5F;
/*     */   private static final int ATTACK_DAMAGE = 30;
/*     */   private static final int FOLLOW_RANGE = 24;
/*  90 */   private static final EntityDataAccessor<Integer> CLIENT_ANGER_LEVEL = SynchedEntityData.defineId(Warden.class, EntityDataSerializers.INT);
/*     */   
/*     */   private static final int DARKNESS_DISPLAY_LIMIT = 200;
/*     */   
/*     */   private static final int DARKNESS_DURATION = 260;
/*     */   
/*     */   private static final int DARKNESS_RADIUS = 20;
/*     */   
/*     */   private static final int DARKNESS_INTERVAL = 120;
/*     */   
/*     */   private static final int ANGERMANAGEMENT_TICK_DELAY = 20;
/*     */   
/*     */   private static final int DEFAULT_ANGER = 35;
/*     */   
/*     */   private static final int PROJECTILE_ANGER = 10;
/*     */   
/*     */   private static final int ON_HURT_ANGER_BOOST = 20;
/*     */   
/*     */   private static final int RECENT_PROJECTILE_TICK_THRESHOLD = 100;
/*     */   private static final int TOUCH_COOLDOWN_TICKS = 20;
/*     */   private static final int DIGGING_PARTICLES_AMOUNT = 30;
/*     */   private static final float DIGGING_PARTICLES_DURATION = 4.5F;
/*     */   private static final float DIGGING_PARTICLES_OFFSET = 0.7F;
/*     */   private static final int PROJECTILE_ANGER_DISTANCE = 30;
/*     */   private int tendrilAnimation;
/*     */   private int tendrilAnimationO;
/*     */   private int heartAnimation;
/*     */   private int heartAnimationO;
/* 118 */   public AnimationState roarAnimationState = new AnimationState();
/* 119 */   public AnimationState sniffAnimationState = new AnimationState();
/* 120 */   public AnimationState emergeAnimationState = new AnimationState();
/* 121 */   public AnimationState diggingAnimationState = new AnimationState();
/* 122 */   public AnimationState attackAnimationState = new AnimationState();
/* 123 */   public AnimationState sonicBoomAnimationState = new AnimationState();
/*     */   
/*     */   private final DynamicGameEventListener<VibrationSystem.Listener> dynamicGameEventListener;
/*     */   
/*     */   private final VibrationSystem.User vibrationUser;
/*     */   private VibrationSystem.Data vibrationData;
/* 129 */   private AngerManagement angerManagement = new AngerManagement(this::canTargetEntity, Collections.emptyList());
/*     */   
/*     */   public Warden(EntityType<? extends Monster> type, Level level) {
/* 132 */     super(type, level);
/* 133 */     this.vibrationUser = new VibrationUser();
/* 134 */     this.vibrationData = new VibrationSystem.Data();
/* 135 */     this.dynamicGameEventListener = new DynamicGameEventListener(new VibrationSystem.Listener(this));
/*     */     
/* 137 */     this.xpReward = 5;
/* 138 */     getNavigation().setCanFloat(true);
/*     */     
/* 140 */     setPathfindingMalus(PathType.UNPASSABLE_RAIL, 0.0F);
/* 141 */     setPathfindingMalus(PathType.DAMAGE_OTHER, 8.0F);
/* 142 */     setPathfindingMalus(PathType.POWDER_SNOW, 8.0F);
/* 143 */     setPathfindingMalus(PathType.LAVA, 8.0F);
/* 144 */     setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
/* 145 */     setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 150 */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) { return new ClientboundAddEntityPacket(this, serverEntity, hasPose(Pose.EMERGING) ? 1 : 0); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 155 */     super.recreateFromPacket(packet);
/* 156 */     if (packet.getData() == 1) {
/* 157 */       setPose(Pose.EMERGING);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 163 */   public boolean checkSpawnObstruction(LevelReader level) { return (super.checkSpawnObstruction(level) && level.noCollision(this, getType().getDimensions().makeBoundingBox(position()))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 168 */   public float getWalkTargetValue(BlockPos pos, LevelReader level) { return 0.0F; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInvulnerableTo(ServerLevel level, DamageSource source) {
/* 173 */     if (isDiggingOrEmerging() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
/* 174 */       return true;
/*     */     }
/* 176 */     return super.isInvulnerableTo(level, source);
/*     */   }
/*     */ 
/*     */   
/* 180 */   private boolean isDiggingOrEmerging() { return (hasPose(Pose.DIGGING) || hasPose(Pose.EMERGING)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 185 */   protected boolean canRide(Entity vehicle) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 190 */   public float getSecondsToDisableBlocking() { return 5.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 196 */   protected float nextStep() { return this.moveDist + 0.55F; }
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 200 */     return Monster.createMonsterAttributes()
/* 201 */       .add(Attributes.MAX_HEALTH, 500.0D)
/* 202 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 203 */       .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
/* 204 */       .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
/* 205 */       .add(Attributes.ATTACK_DAMAGE, 30.0D)
/* 206 */       .add(Attributes.FOLLOW_RANGE, 24.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 211 */   public boolean dampensVibrations() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   protected float getSoundVolume() { return 4.0F; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 222 */     if (hasPose(Pose.ROARING) || isDiggingOrEmerging()) {
/* 223 */       return null;
/*     */     }
/*     */     
/* 226 */     return getAngerLevel().getAmbientSound();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 231 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.WARDEN_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 236 */   protected SoundEvent getDeathSound() { return SoundEvents.WARDEN_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 241 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.WARDEN_STEP, 10.0F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/* 246 */     level.broadcastEntityEvent(this, (byte)4);
/* 247 */     playSound(SoundEvents.WARDEN_ATTACK_IMPACT, 10.0F, getVoicePitch());
/*     */     
/* 249 */     SonicBoom.setCooldown(this, 40);
/*     */     
/* 251 */     return super.doHurtTarget(level, target);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 256 */     super.defineSynchedData(entityData);
/*     */     
/* 258 */     entityData.define(CLIENT_ANGER_LEVEL, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/* 262 */   public int getClientAngerLevel() { return ((Integer)this.entityData.get(CLIENT_ANGER_LEVEL)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 266 */   private void syncClientAngerLevel() { this.entityData.set(CLIENT_ANGER_LEVEL, Integer.valueOf(getActiveAnger())); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 271 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 272 */       VibrationSystem.Ticker.tick(serverLevel, this.vibrationData, this.vibrationUser);
/*     */       
/* 274 */       if (isPersistenceRequired() || requiresCustomPersistence()) {
/* 275 */         WardenAi.setDigCooldown(this);
/*     */       } }
/*     */ 
/*     */     
/* 279 */     super.tick();
/*     */ 
/*     */     
/* 282 */     if (level().isClientSide()) {
/* 283 */       if (this.tickCount % getHeartBeatDelay() == 0) {
/* 284 */         this.heartAnimation = 10;
/* 285 */         if (!isSilent()) {
/* 286 */           level().playLocalSound(getX(), getY(), getZ(), SoundEvents.WARDEN_HEARTBEAT, getSoundSource(), 5.0F, getVoicePitch(), false);
/*     */         }
/*     */       } 
/*     */       
/* 290 */       this.tendrilAnimationO = this.tendrilAnimation;
/* 291 */       if (this.tendrilAnimation > 0) {
/* 292 */         this.tendrilAnimation--;
/*     */       }
/*     */       
/* 295 */       this.heartAnimationO = this.heartAnimation;
/* 296 */       if (this.heartAnimation > 0) {
/* 297 */         this.heartAnimation--;
/*     */       }
/*     */       
/* 300 */       switch (getPose()) { case EMERGING:
/* 301 */           clientDiggingParticles(this.emergeAnimationState); break;
/* 302 */         case DIGGING: clientDiggingParticles(this.diggingAnimationState);
/*     */           break; }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 310 */     ProfilerFiller profiler = Profiler.get();
/* 311 */     profiler.push("wardenBrain");
/* 312 */     getBrain().tick(level, this);
/* 313 */     profiler.pop();
/*     */     
/* 315 */     super.customServerAiStep(level);
/*     */     
/* 317 */     if ((this.tickCount + getId()) % 120 == 0) {
/* 318 */       applyDarknessAround(level, position(), this, 20);
/*     */     }
/*     */     
/* 321 */     if (this.tickCount % 20 == 0) {
/* 322 */       this.angerManagement.tick(level, this::canTargetEntity);
/* 323 */       syncClientAngerLevel();
/*     */     } 
/*     */     
/* 326 */     WardenAi.updateActivity(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 331 */     if (id == 4) {
/* 332 */       this.roarAnimationState.stop();
/* 333 */       this.attackAnimationState.start(this.tickCount);
/* 334 */     } else if (id == 61) {
/* 335 */       this.tendrilAnimation = 10;
/* 336 */     } else if (id == 62) {
/* 337 */       this.sonicBoomAnimationState.start(this.tickCount);
/*     */     } else {
/* 339 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getHeartBeatDelay() {
/* 344 */     float anger = getClientAngerLevel() / AngerLevel.ANGRY.getMinimumAnger();
/* 345 */     return 40 - Mth.floor(Mth.clamp(anger, 0.0F, 1.0F) * 30.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 350 */   public float getTendrilAnimation(float a) { return Mth.lerp(a, this.tendrilAnimationO, this.tendrilAnimation) / 10.0F; }
/*     */ 
/*     */ 
/*     */   
/* 354 */   public float getHeartAnimation(float a) { return Mth.lerp(a, this.heartAnimationO, this.heartAnimation) / 10.0F; }
/*     */ 
/*     */   
/*     */   private void clientDiggingParticles(AnimationState state) {
/* 358 */     if ((float)state.getTimeInMillis(this.tickCount) < 4500.0F) {
/* 359 */       RandomSource random = getRandom();
/* 360 */       BlockState stateBelow = getBlockStateOn();
/*     */       
/* 362 */       if (stateBelow.getRenderShape() != RenderShape.INVISIBLE) {
/* 363 */         for (int i = 0; i < 30; i++) {
/* 364 */           double xx = getX() + Mth.randomBetween(random, -0.7F, 0.7F);
/* 365 */           double yy = getY();
/* 366 */           double zz = getZ() + Mth.randomBetween(random, -0.7F, 0.7F);
/*     */           
/* 368 */           level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, stateBelow), xx, yy, zz, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 376 */     if (DATA_POSE.equals(accessor)) {
/* 377 */       switch (getPose()) { case ROARING:
/* 378 */           this.roarAnimationState.start(this.tickCount); break;
/* 379 */         case SNIFFING: this.sniffAnimationState.start(this.tickCount); break;
/* 380 */         case EMERGING: this.emergeAnimationState.start(this.tickCount); break;
/* 381 */         case DIGGING: this.diggingAnimationState.start(this.tickCount);
/*     */           break; }
/*     */     
/*     */     }
/* 385 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 390 */   public boolean ignoreExplosion(Explosion explosion) { return isDiggingOrEmerging(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 395 */   protected Brain<?> makeBrain(Dynamic<?> input) { return WardenAi.makeBrain(this, input); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 401 */   public Brain<Warden> getBrain() { return super.getBrain(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> action) {
/* 406 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 407 */       action.accept(this.dynamicGameEventListener, serverLevel); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract("null->false")
/*     */   public boolean canTargetEntity(Entity entity) { // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   4: ifeq -> 98
/*     */     //   7: aload_1
/*     */     //   8: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   11: astore_2
/*     */     //   12: aload_0
/*     */     //   13: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   16: aload_1
/*     */     //   17: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   20: if_acmpne -> 98
/*     */     //   23: getstatic net/minecraft/world/entity/EntitySelector.NO_CREATIVE_OR_SPECTATOR : Ljava/util/function/Predicate;
/*     */     //   26: aload_1
/*     */     //   27: invokeinterface test : (Ljava/lang/Object;)Z
/*     */     //   32: ifeq -> 98
/*     */     //   35: aload_0
/*     */     //   36: aload_1
/*     */     //   37: invokevirtual isAlliedTo : (Lnet/minecraft/world/entity/Entity;)Z
/*     */     //   40: ifne -> 98
/*     */     //   43: aload_2
/*     */     //   44: invokevirtual getType : ()Lnet/minecraft/world/entity/EntityType;
/*     */     //   47: getstatic net/minecraft/world/entity/EntityType.ARMOR_STAND : Lnet/minecraft/world/entity/EntityType;
/*     */     //   50: if_acmpeq -> 98
/*     */     //   53: aload_2
/*     */     //   54: invokevirtual getType : ()Lnet/minecraft/world/entity/EntityType;
/*     */     //   57: getstatic net/minecraft/world/entity/EntityType.WARDEN : Lnet/minecraft/world/entity/EntityType;
/*     */     //   60: if_acmpeq -> 98
/*     */     //   63: aload_2
/*     */     //   64: invokevirtual isInvulnerable : ()Z
/*     */     //   67: ifne -> 98
/*     */     //   70: aload_2
/*     */     //   71: invokevirtual isDeadOrDying : ()Z
/*     */     //   74: ifne -> 98
/*     */     //   77: aload_0
/*     */     //   78: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   81: invokevirtual getWorldBorder : ()Lnet/minecraft/world/level/border/WorldBorder;
/*     */     //   84: aload_2
/*     */     //   85: invokevirtual getBoundingBox : ()Lnet/minecraft/world/phys/AABB;
/*     */     //   88: invokevirtual isWithinBounds : (Lnet/minecraft/world/phys/AABB;)Z
/*     */     //   91: ifeq -> 98
/*     */     //   94: iconst_1
/*     */     //   95: goto -> 99
/*     */     //   98: iconst_0
/*     */     //   99: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #421	-> 0
/*     */     //   #413	-> 7
/*     */     //   #414	-> 13
/*     */     //   #415	-> 27
/*     */     //   #416	-> 37
/*     */     //   #417	-> 44
/*     */     //   #418	-> 54
/*     */     //   #419	-> 64
/*     */     //   #420	-> 71
/*     */     //   #421	-> 78
/*     */     //   #413	-> 99
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   12	86	2	livingEntity	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   0	100	0	this	Lnet/minecraft/world/entity/monster/warden/Warden;
/*     */     //   0	100	1	entity	Lnet/minecraft/world/entity/Entity; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void applyDarknessAround(ServerLevel level, Vec3 position, Entity source, int darknessRadius) {
/* 426 */     MobEffectInstance darkness = new MobEffectInstance(MobEffects.DARKNESS, 260, 0, false, false);
/* 427 */     MobEffectUtil.addEffectToPlayersAround(level, source, position, darknessRadius, darkness, 200);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 432 */     super.addAdditionalSaveData(output);
/*     */     
/* 434 */     output.store("anger", AngerManagement.codec(this::canTargetEntity), this.angerManagement);
/* 435 */     output.store("listener", VibrationSystem.Data.CODEC, this.vibrationData);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 440 */     super.readAdditionalSaveData(input);
/*     */     
/* 442 */     this
/* 443 */       .angerManagement = (AngerManagement)input.read("anger", AngerManagement.codec(this::canTargetEntity)).orElseGet(() -> new AngerManagement(this::canTargetEntity, Collections.emptyList()));
/* 444 */     syncClientAngerLevel();
/*     */     
/* 446 */     this.vibrationData = (VibrationSystem.Data)input.read("listener", VibrationSystem.Data.CODEC).orElseGet(net.minecraft.world.level.gameevent.vibrations.VibrationSystem.Data::new);
/*     */   }
/*     */   
/*     */   private void playListeningSound() {
/* 450 */     if (!hasPose(Pose.ROARING)) {
/* 451 */       playSound(getAngerLevel().getListeningSound(), 10.0F, getVoicePitch());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 456 */   public AngerLevel getAngerLevel() { return AngerLevel.byAnger(getActiveAnger()); }
/*     */ 
/*     */ 
/*     */   
/* 460 */   private int getActiveAnger() { return this.angerManagement.getActiveAnger(getTarget()); }
/*     */ 
/*     */ 
/*     */   
/* 464 */   public void clearAnger(Entity entity) { this.angerManagement.clearAnger(entity); }
/*     */ 
/*     */ 
/*     */   
/* 468 */   public void increaseAngerAt(Entity entity) { increaseAngerAt(entity, 35, true); }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public void increaseAngerAt(Entity entity, int amount, boolean playSound) {
/* 473 */     if (!isNoAi() && canTargetEntity(entity)) {
/* 474 */       WardenAi.setDigCooldown(this);
/*     */       
/* 476 */       boolean maybeSwitchTarget = !(getTarget() instanceof net.minecraft.world.entity.player.Player);
/*     */       
/* 478 */       int newAnger = this.angerManagement.increaseAnger(entity, amount);
/*     */       
/* 480 */       if (entity instanceof net.minecraft.world.entity.player.Player && maybeSwitchTarget && AngerLevel.byAnger(newAnger).isAngry())
/*     */       {
/* 482 */         getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/*     */       }
/*     */       
/* 485 */       if (playSound) {
/* 486 */         playListeningSound();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public Optional<LivingEntity> getEntityAngryAt() {
/* 492 */     if (getAngerLevel().isAngry()) {
/* 493 */       return this.angerManagement.getActiveEntity();
/*     */     }
/* 495 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 500 */   public LivingEntity getTarget() { return getTargetFromBrain(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 505 */   public boolean removeWhenFarAway(double distSqr) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 510 */     getBrain().setMemoryWithExpiry(MemoryModuleType.DIG_COOLDOWN, Unit.INSTANCE, 1200L);
/*     */     
/* 512 */     if (spawnReason == EntitySpawnReason.TRIGGERED) {
/* 513 */       setPose(Pose.EMERGING);
/* 514 */       getBrain().setMemoryWithExpiry(MemoryModuleType.IS_EMERGING, Unit.INSTANCE, WardenAi.EMERGE_DURATION);
/* 515 */       playSound(SoundEvents.WARDEN_AGITATED, 5.0F, 1.0F);
/*     */     } 
/*     */     
/* 518 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 523 */     boolean wasHurt = super.hurtServer(level, source, damage);
/* 524 */     if (!isNoAi() && !isDiggingOrEmerging()) {
/* 525 */       Entity attacker = source.getEntity();
/*     */       
/* 527 */       increaseAngerAt(attacker, AngerLevel.ANGRY.getMinimumAnger() + 20, false);
/*     */ 
/*     */       
/* 530 */       if (this.brain.getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty() && attacker instanceof LivingEntity) {
/* 531 */         LivingEntity livingAttacker = (LivingEntity)attacker; if (source
/* 532 */           .isDirect() || closerThan(livingAttacker, 5.0D))
/*     */         {
/* 534 */           setAttackTarget(livingAttacker); } 
/*     */       } 
/*     */     } 
/* 537 */     return wasHurt;
/*     */   }
/*     */   
/*     */   public void setAttackTarget(LivingEntity target) {
/* 541 */     getBrain().eraseMemory(MemoryModuleType.ROAR_TARGET);
/* 542 */     getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
/* 543 */     getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 544 */     SonicBoom.setCooldown(this, 200);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 549 */     EntityDimensions dimensions = super.getDefaultDimensions(pose);
/*     */     
/* 551 */     if (isDiggingOrEmerging()) {
/* 552 */       return EntityDimensions.fixed(dimensions.width(), 1.0F);
/*     */     }
/*     */     
/* 555 */     return dimensions;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 560 */   public boolean isPushable() { return (!isDiggingOrEmerging() && super.isPushable()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doPush(Entity entity) {
/* 565 */     if (!isNoAi() && !getBrain().hasMemoryValue(MemoryModuleType.TOUCH_COOLDOWN)) {
/* 566 */       getBrain().setMemoryWithExpiry(MemoryModuleType.TOUCH_COOLDOWN, Unit.INSTANCE, 20L);
/* 567 */       increaseAngerAt(entity);
/* 568 */       WardenAi.setDisturbanceLocation(this, entity.blockPosition());
/*     */     } 
/*     */     
/* 571 */     super.doPush(entity);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 576 */   public AngerManagement getAngerManagement() { return this.angerManagement; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/* 584 */     return new GroundPathNavigation(this, this, level)
/*     */       {
/*     */         protected PathFinder createPathFinder(int maxVisitedNodes) {
/* 587 */           this.nodeEvaluator = new WalkNodeEvaluator();
/* 588 */           return new PathFinder(this, this.nodeEvaluator, maxVisitedNodes)
/*     */             {
/*     */ 
/*     */               
/*     */               protected float distance(Node from, Node to)
/*     */               {
/* 594 */                 return from.distanceToXZ(to);
/*     */               }
/*     */             };
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 603 */   public VibrationSystem.Data getVibrationData() { return this.vibrationData; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 608 */   public VibrationSystem.User getVibrationUser() { return this.vibrationUser; }
/*     */   
/*     */   private class VibrationUser
/*     */     implements VibrationSystem.User {
/*     */     private static final int GAME_EVENT_LISTENER_RANGE = 16;
/* 613 */     private final PositionSource positionSource = new EntityPositionSource(Warden.this, Warden.this.getEyeHeight());
/*     */ 
/*     */ 
/*     */     
/* 617 */     public int getListenerRadius() { return 16; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 622 */     public PositionSource getPositionSource() { return this.positionSource; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 627 */     public TagKey<GameEvent> getListenableEvents() { return GameEventTags.WARDEN_CAN_LISTEN; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 632 */     public boolean canTriggerAvoidVibration() { return true; }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, GameEvent.Context context) {
/* 637 */       if (Warden.this.isNoAi() || Warden.this
/* 638 */         .isDeadOrDying() || Warden.this
/* 639 */         .getBrain().hasMemoryValue(MemoryModuleType.VIBRATION_COOLDOWN) || Warden.this
/* 640 */         .isDiggingOrEmerging() || 
/* 641 */         !level.getWorldBorder().isWithinBounds(pos))
/*     */       {
/* 643 */         return false;
/*     */       }
/*     */       
/* 646 */       Entity entity = context.sourceEntity(); if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity; if (Warden.this.canTargetEntity(livingEntity)); return false; }
/*     */     
/*     */     }
/*     */     
/*     */     public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, Entity sourceEntity, Entity projectileOwner, float receivingDistance) {
/* 651 */       if (Warden.this.isDeadOrDying()) {
/*     */         return;
/*     */       }
/*     */       
/* 655 */       Warden.this.brain.setMemoryWithExpiry(MemoryModuleType.VIBRATION_COOLDOWN, Unit.INSTANCE, 40L);
/*     */       
/* 657 */       level.broadcastEntityEvent(Warden.this, (byte)61);
/* 658 */       Warden.this.playSound(SoundEvents.WARDEN_TENDRIL_CLICKS, 5.0F, Warden.this.getVoicePitch());
/*     */       
/* 660 */       BlockPos suspiciousPos = pos;
/*     */ 
/*     */       
/* 663 */       if (projectileOwner != null) {
/* 664 */         if (Warden.this.closerThan(projectileOwner, 30.0D)) {
/* 665 */           if (Warden.this.getBrain().hasMemoryValue(MemoryModuleType.RECENT_PROJECTILE)) {
/* 666 */             if (Warden.this.canTargetEntity(projectileOwner)) {
/* 667 */               suspiciousPos = projectileOwner.blockPosition();
/*     */             }
/* 669 */             Warden.this.increaseAngerAt(projectileOwner);
/*     */           } else {
/* 671 */             Warden.this.increaseAngerAt(projectileOwner, 10, true);
/*     */           } 
/*     */         }
/* 674 */         Warden.this.getBrain().setMemoryWithExpiry(MemoryModuleType.RECENT_PROJECTILE, Unit.INSTANCE, 100L);
/*     */       } else {
/* 676 */         Warden.this.increaseAngerAt(sourceEntity);
/*     */       } 
/*     */       
/* 679 */       if (!Warden.this.getAngerLevel().isAngry()) {
/*     */         
/* 681 */         Optional<LivingEntity> activeEntity = Warden.this.angerManagement.getActiveEntity();
/* 682 */         if (projectileOwner != null || activeEntity.isEmpty() || activeEntity.get() == sourceEntity)
/* 683 */           WardenAi.setDisturbanceLocation(Warden.this, suspiciousPos); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\warden\Warden.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */