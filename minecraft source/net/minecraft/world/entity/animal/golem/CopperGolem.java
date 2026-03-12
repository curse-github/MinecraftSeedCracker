/*     */ package net.minecraft.world.entity.animal.golem;
/*     */ 
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.ContainerUser;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Shearable;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*     */ import net.minecraft.world.level.block.WeatheringCopper;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
/*     */ import net.minecraft.world.level.block.entity.CopperGolemStatueBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CopperGolem
/*     */   extends AbstractGolem
/*     */   implements ContainerUser, Shearable
/*     */ {
/*     */   private static final long IGNORE_WEATHERING_TICK = -2L;
/*     */   private static final long UNSET_WEATHERING_TICK = -1L;
/*     */   private static final int WEATHERING_TICK_FROM = 504000;
/*     */   private static final int WEATHERING_TICK_TO = 552000;
/*     */   private static final int SPIN_ANIMATION_MIN_COOLDOWN = 200;
/*     */   private static final int SPIN_ANIMATION_MAX_COOLDOWN = 240;
/*     */   private static final float SPIN_SOUND_TIME_INTERVAL_OFFSET = 10.0F;
/*     */   private static final float TURN_TO_STATUE_CHANCE = 0.0058F;
/*     */   private static final int SPAWN_COOLDOWN_MIN = 60;
/*     */   private static final int SPAWN_COOLDOWN_MAX = 100;
/*  73 */   private static final EntityDataAccessor<WeatheringCopper.WeatherState> DATA_WEATHER_STATE = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.WEATHERING_COPPER_STATE);
/*  74 */   private static final EntityDataAccessor<CopperGolemState> COPPER_GOLEM_STATE = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.COPPER_GOLEM_STATE);
/*     */   
/*     */   private BlockPos openedChestPos;
/*     */   private UUID lastLightningBoltUUID;
/*  78 */   private long nextWeatheringTick = -1L;
/*  79 */   private int idleAnimationStartTick = 0;
/*     */   
/*  81 */   private final AnimationState idleAnimationState = new AnimationState();
/*  82 */   private final AnimationState interactionGetItemAnimationState = new AnimationState();
/*  83 */   private final AnimationState interactionGetNoItemAnimationState = new AnimationState();
/*  84 */   private final AnimationState interactionDropItemAnimationState = new AnimationState();
/*  85 */   private final AnimationState interactionDropNoItemAnimationState = new AnimationState();
/*     */   
/*  87 */   public static final EquipmentSlot EQUIPMENT_SLOT_ANTENNA = EquipmentSlot.SADDLE;
/*     */   
/*     */   public CopperGolem(EntityType<? extends AbstractGolem> type, Level level) {
/*  90 */     super(type, level);
/*  91 */     getNavigation().setRequiredPathLength(48.0F);
/*  92 */     getNavigation().setCanOpenDoors(true);
/*  93 */     setPersistenceRequired();
/*  94 */     setState(CopperGolemState.IDLE);
/*  95 */     setPathfindingMalus(PathType.DANGER_FIRE, 16.0F);
/*  96 */     setPathfindingMalus(PathType.DANGER_OTHER, 16.0F);
/*  97 */     setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
/*  98 */     getBrain().setMemory(MemoryModuleType.TRANSPORT_ITEMS_COOLDOWN_TICKS, Integer.valueOf(getRandom().nextInt(60, 100)));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 102 */     return Mob.createMobAttributes()
/* 103 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D)
/* 104 */       .add(Attributes.STEP_HEIGHT, 1.0D)
/* 105 */       .add(Attributes.MAX_HEALTH, 12.0D);
/*     */   }
/*     */ 
/*     */   
/* 109 */   public CopperGolemState getState() { return (CopperGolemState)this.entityData.get(COPPER_GOLEM_STATE); }
/*     */ 
/*     */ 
/*     */   
/* 113 */   public void setState(CopperGolemState state) { this.entityData.set(COPPER_GOLEM_STATE, state); }
/*     */ 
/*     */ 
/*     */   
/* 117 */   public WeatheringCopper.WeatherState getWeatherState() { return (WeatheringCopper.WeatherState)this.entityData.get(DATA_WEATHER_STATE); }
/*     */ 
/*     */ 
/*     */   
/* 121 */   public void setWeatherState(WeatheringCopper.WeatherState state) { this.entityData.set(DATA_WEATHER_STATE, state); }
/*     */ 
/*     */ 
/*     */   
/* 125 */   public void setOpenedChestPos(BlockPos openedChestPos) { this.openedChestPos = openedChestPos; }
/*     */ 
/*     */ 
/*     */   
/* 129 */   public void clearOpenedChestPos() { this.openedChestPos = null; }
/*     */ 
/*     */ 
/*     */   
/* 133 */   public AnimationState getIdleAnimationState() { return this.idleAnimationState; }
/*     */ 
/*     */ 
/*     */   
/* 137 */   public AnimationState getInteractionGetItemAnimationState() { return this.interactionGetItemAnimationState; }
/*     */ 
/*     */ 
/*     */   
/* 141 */   public AnimationState getInteractionGetNoItemAnimationState() { return this.interactionGetNoItemAnimationState; }
/*     */ 
/*     */ 
/*     */   
/* 145 */   public AnimationState getInteractionDropItemAnimationState() { return this.interactionDropItemAnimationState; }
/*     */ 
/*     */ 
/*     */   
/* 149 */   public AnimationState getInteractionDropNoItemAnimationState() { return this.interactionDropNoItemAnimationState; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   protected Brain.Provider<CopperGolem> brainProvider() { return CopperGolemAi.brainProvider(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 159 */   protected Brain<?> makeBrain(Dynamic<?> input) { return CopperGolemAi.makeBrain(brainProvider().makeBrain(input)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 165 */   public Brain<CopperGolem> getBrain() { return super.getBrain(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 170 */     super.defineSynchedData(entityData);
/* 171 */     entityData.define(DATA_WEATHER_STATE, WeatheringCopper.WeatherState.UNAFFECTED);
/* 172 */     entityData.define(COPPER_GOLEM_STATE, CopperGolemState.IDLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(ValueOutput output) {
/* 177 */     super.addAdditionalSaveData(output);
/* 178 */     output.putLong("next_weather_age", this.nextWeatheringTick);
/* 179 */     output.store("weather_state", WeatheringCopper.WeatherState.CODEC, getWeatherState());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(ValueInput input) {
/* 184 */     super.readAdditionalSaveData(input);
/* 185 */     this.nextWeatheringTick = input.getLongOr("next_weather_age", -1L);
/* 186 */     setWeatherState((WeatheringCopper.WeatherState)input.read("weather_state", WeatheringCopper.WeatherState.CODEC).orElse(WeatheringCopper.WeatherState.UNAFFECTED));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 191 */     ProfilerFiller profiler = Profiler.get();
/* 192 */     profiler.push("copperGolemBrain");
/* 193 */     getBrain().tick(level, this);
/* 194 */     profiler.pop();
/*     */     
/* 196 */     profiler.push("copperGolemActivityUpdate");
/* 197 */     CopperGolemAi.updateActivity(this);
/* 198 */     profiler.pop();
/*     */     
/* 200 */     super.customServerAiStep(level);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 205 */     super.tick();
/* 206 */     if (level().isClientSide()) {
/* 207 */       if (!isNoAi()) {
/* 208 */         setupAnimationStates();
/*     */       }
/*     */     } else {
/* 211 */       updateWeathering((ServerLevel)level(), level().getRandom(), level().getGameTime());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 217 */     ItemStack itemStack = player.getItemInHand(hand);
/* 218 */     if (itemStack.isEmpty()) {
/* 219 */       ItemStack equippedItem = getMainHandItem();
/* 220 */       if (!equippedItem.isEmpty()) {
/* 221 */         BehaviorUtils.throwItem(this, equippedItem, player.position());
/* 222 */         setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
/* 223 */         return InteractionResult.SUCCESS;
/*     */       } 
/*     */     } 
/*     */     
/* 227 */     Level level = level();
/* 228 */     if (itemStack.is(Items.SHEARS) && readyForShearing()) {
/* 229 */       if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 230 */         shear(serverLevel, SoundSource.PLAYERS, itemStack);
/* 231 */         gameEvent(GameEvent.SHEAR, player);
/* 232 */         itemStack.hurtAndBreak(1, player, hand); }
/*     */       
/* 234 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 237 */     if (level.isClientSide()) {
/* 238 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 241 */     if (itemStack.is(Items.HONEYCOMB) && 
/* 242 */       this.nextWeatheringTick != -2L) {
/* 243 */       level.levelEvent(this, 3003, blockPosition(), 0);
/*     */       
/* 245 */       this.nextWeatheringTick = -2L;
/* 246 */       usePlayerItem(player, hand, itemStack);
/* 247 */       return InteractionResult.SUCCESS_SERVER;
/*     */     } 
/*     */ 
/*     */     
/* 251 */     if (itemStack.is(ItemTags.AXES) && 
/* 252 */       this.nextWeatheringTick == -2L) {
/* 253 */       level.playSound(null, this, SoundEvents.AXE_SCRAPE, getSoundSource(), 1.0F, 1.0F);
/* 254 */       level.levelEvent(this, 3004, blockPosition(), 0);
/*     */       
/* 256 */       this.nextWeatheringTick = -1L;
/* 257 */       itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
/* 258 */       return InteractionResult.SUCCESS_SERVER;
/*     */     } 
/*     */ 
/*     */     
/* 262 */     if (itemStack.is(ItemTags.AXES)) {
/* 263 */       WeatheringCopper.WeatherState weatherState = getWeatherState();
/* 264 */       if (weatherState != WeatheringCopper.WeatherState.UNAFFECTED) {
/* 265 */         level.playSound(null, this, SoundEvents.AXE_SCRAPE, getSoundSource(), 1.0F, 1.0F);
/* 266 */         level.levelEvent(this, 3005, blockPosition(), 0);
/*     */         
/* 268 */         this.nextWeatheringTick = -1L;
/* 269 */         this.entityData.set(DATA_WEATHER_STATE, weatherState.previous(), true);
/* 270 */         itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
/* 271 */         return InteractionResult.SUCCESS_SERVER;
/*     */       } 
/*     */     } 
/*     */     
/* 275 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   private void updateWeathering(ServerLevel level, RandomSource random, long gameTime) {
/* 279 */     if (this.nextWeatheringTick == -2L) {
/*     */       return;
/*     */     }
/* 282 */     if (this.nextWeatheringTick == -1L) {
/* 283 */       this.nextWeatheringTick = gameTime + random.nextIntBetweenInclusive(504000, 552000);
/*     */       return;
/*     */     } 
/* 286 */     WeatheringCopper.WeatherState weatherState = (WeatheringCopper.WeatherState)this.entityData.get(DATA_WEATHER_STATE);
/* 287 */     boolean isFullyOxidized = weatherState.equals(WeatheringCopper.WeatherState.OXIDIZED);
/* 288 */     if (gameTime >= this.nextWeatheringTick && !isFullyOxidized) {
/* 289 */       WeatheringCopper.WeatherState newState = weatherState.next();
/* 290 */       boolean isNewStateFullyOxidized = newState.equals(WeatheringCopper.WeatherState.OXIDIZED);
/* 291 */       setWeatherState(newState);
/* 292 */       this.nextWeatheringTick = isNewStateFullyOxidized ? 0L : (this.nextWeatheringTick + random.nextIntBetweenInclusive(504000, 552000));
/*     */     } 
/*     */     
/* 295 */     if (isFullyOxidized && 
/* 296 */       canTurnToStatue(level)) {
/* 297 */       turnToStatue(level);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 303 */   private boolean canTurnToStatue(Level level) { return (level.getBlockState(blockPosition()).isAir() && level.random.nextFloat() <= 0.0058F); }
/*     */ 
/*     */   
/*     */   private void turnToStatue(ServerLevel level) {
/* 307 */     BlockPos pos = blockPosition();
/* 308 */     level.setBlock(pos, (BlockState)((BlockState)Blocks.OXIDIZED_COPPER_GOLEM_STATUE.defaultBlockState()
/* 309 */         .setValue(CopperGolemStatueBlock.POSE, CopperGolemStatueBlock.Pose.values()[this.random.nextInt(0, CopperGolemStatueBlock.Pose.values().length)]))
/* 310 */         .setValue(CopperGolemStatueBlock.FACING, Direction.fromYRot(getYRot())), 3);
/* 311 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof CopperGolemStatueBlockEntity) { CopperGolemStatueBlockEntity copperGolemStatueBlockEntity = (CopperGolemStatueBlockEntity)blockEntity;
/* 312 */       copperGolemStatueBlockEntity.createStatue(this);
/* 313 */       dropPreservedEquipment(level);
/* 314 */       discard();
/* 315 */       playSound(SoundEvents.COPPER_GOLEM_BECOME_STATUE);
/*     */       
/* 317 */       if (isLeashed()) {
/* 318 */         if (((Boolean)level.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/* 319 */           dropLeash();
/*     */         } else {
/* 321 */           removeLeash();
/*     */         } 
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   private void setupAnimationStates() {
/* 328 */     switch (getState()) {
/*     */       case IDLE:
/* 330 */         this.interactionGetNoItemAnimationState.stop();
/* 331 */         this.interactionGetItemAnimationState.stop();
/* 332 */         this.interactionDropItemAnimationState.stop();
/* 333 */         this.interactionDropNoItemAnimationState.stop();
/* 334 */         if (this.idleAnimationStartTick == this.tickCount) {
/* 335 */           this.idleAnimationState.start(this.tickCount);
/* 336 */         } else if (this.idleAnimationStartTick == 0) {
/* 337 */           this.idleAnimationStartTick = this.tickCount + this.random.nextInt(200, 240);
/*     */         } 
/*     */         
/* 340 */         if (this.tickCount == this.idleAnimationStartTick + 10.0F) {
/* 341 */           playHeadSpinSound();
/* 342 */           this.idleAnimationStartTick = 0;
/*     */         } 
/*     */         break;
/*     */       case GETTING_ITEM:
/* 346 */         this.idleAnimationState.stop();
/* 347 */         this.idleAnimationStartTick = 0;
/* 348 */         this.interactionGetNoItemAnimationState.stop();
/* 349 */         this.interactionDropItemAnimationState.stop();
/* 350 */         this.interactionDropNoItemAnimationState.stop();
/* 351 */         this.interactionGetItemAnimationState.startIfStopped(this.tickCount);
/*     */         break;
/*     */       case GETTING_NO_ITEM:
/* 354 */         this.idleAnimationState.stop();
/* 355 */         this.idleAnimationStartTick = 0;
/* 356 */         this.interactionGetItemAnimationState.stop();
/* 357 */         this.interactionDropNoItemAnimationState.stop();
/* 358 */         this.interactionDropItemAnimationState.stop();
/* 359 */         this.interactionGetNoItemAnimationState.startIfStopped(this.tickCount);
/*     */         break;
/*     */       case DROPPING_ITEM:
/* 362 */         this.idleAnimationState.stop();
/* 363 */         this.idleAnimationStartTick = 0;
/* 364 */         this.interactionGetItemAnimationState.stop();
/* 365 */         this.interactionGetNoItemAnimationState.stop();
/* 366 */         this.interactionDropNoItemAnimationState.stop();
/* 367 */         this.interactionDropItemAnimationState.startIfStopped(this.tickCount);
/*     */         break;
/*     */       case DROPPING_NO_ITEM:
/* 370 */         this.idleAnimationState.stop();
/* 371 */         this.idleAnimationStartTick = 0;
/* 372 */         this.interactionGetItemAnimationState.stop();
/* 373 */         this.interactionGetNoItemAnimationState.stop();
/* 374 */         this.interactionDropItemAnimationState.stop();
/* 375 */         this.interactionDropNoItemAnimationState.startIfStopped(this.tickCount);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void spawn(WeatheringCopper.WeatherState weatherState) {
/* 381 */     setWeatherState(weatherState);
/* 382 */     playSpawnSound();
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 387 */     playSpawnSound();
/* 388 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ 
/*     */   
/* 392 */   public void playSpawnSound() { playSound(SoundEvents.COPPER_GOLEM_SPAWN); }
/*     */ 
/*     */   
/*     */   private void playHeadSpinSound() {
/* 396 */     if (!isSilent()) {
/* 397 */       level().playLocalSound(getX(), getY(), getZ(), getSpinHeadSound(), getSoundSource(), 1.0F, 1.0F, false);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 403 */   protected SoundEvent getHurtSound(DamageSource source) { return CopperGolemOxidationLevels.getOxidationLevel(getWeatherState()).hurtSound(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 408 */   protected SoundEvent getDeathSound() { return CopperGolemOxidationLevels.getOxidationLevel(getWeatherState()).deathSound(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 413 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(CopperGolemOxidationLevels.getOxidationLevel(getWeatherState()).stepSound(), 1.0F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/* 417 */   private SoundEvent getSpinHeadSound() { return CopperGolemOxidationLevels.getOxidationLevel(getWeatherState()).spinHeadSound(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 422 */   public Vec3 getLeashOffset() { return new Vec3(0.0D, (0.75F * getEyeHeight()), 0.0D); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasContainerOpen(ContainerOpenersCounter container, BlockPos blockPos) {
/* 427 */     if (this.openedChestPos == null) {
/* 428 */       return false;
/*     */     }
/* 430 */     BlockState blockState = level().getBlockState(this.openedChestPos);
/* 431 */     return (this.openedChestPos.equals(blockPos) || (blockState.getBlock() instanceof ChestBlock && blockState
/* 432 */       .getValue(ChestBlock.TYPE) != ChestType.SINGLE && 
/* 433 */       ChestBlock.getConnectedBlockPos(this.openedChestPos, blockState).equals(blockPos)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 438 */   public double getContainerInteractionRange() { return 3.0D; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shear(ServerLevel level, SoundSource soundSource, ItemStack tool) {
/* 443 */     level.playSound(null, this, SoundEvents.COPPER_GOLEM_SHEAR, soundSource, 1.0F, 1.0F);
/* 444 */     ItemStack itemStack = getItemBySlot(EQUIPMENT_SLOT_ANTENNA);
/* 445 */     setItemSlot(EQUIPMENT_SLOT_ANTENNA, ItemStack.EMPTY);
/* 446 */     spawnAtLocation(level, itemStack, 1.5F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 451 */   public boolean readyForShearing() { return (isAlive() && getItemBySlot(EQUIPMENT_SLOT_ANTENNA).is(ItemTags.SHEARABLE_FROM_COPPER_GOLEM)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dropEquipment(ServerLevel level) {
/* 456 */     super.dropEquipment(level);
/* 457 */     dropPreservedEquipment(level);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actuallyHurt(ServerLevel level, DamageSource source, float dmg) {
/* 462 */     super.actuallyHurt(level, source, dmg);
/* 463 */     setState(CopperGolemState.IDLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
/* 468 */     super.thunderHit(level, lightningBolt);
/*     */     
/* 470 */     UUID lightningBoltUUID = lightningBolt.getUUID();
/* 471 */     if (!lightningBoltUUID.equals(this.lastLightningBoltUUID)) {
/* 472 */       this.lastLightningBoltUUID = lightningBoltUUID;
/* 473 */       WeatheringCopper.WeatherState weatherState = getWeatherState();
/* 474 */       if (weatherState != WeatheringCopper.WeatherState.UNAFFECTED) {
/* 475 */         this.nextWeatheringTick = -1L;
/* 476 */         this.entityData.set(DATA_WEATHER_STATE, weatherState.previous(), true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\golem\CopperGolem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */