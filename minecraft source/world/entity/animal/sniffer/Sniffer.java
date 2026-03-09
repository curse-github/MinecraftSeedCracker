/*     */ package net.minecraft.world.entity.animal.sniffer;
/*     */ 
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Leashable;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Sniffer
/*     */   extends Animal
/*     */ {
/*     */   private static final int DIGGING_PARTICLES_DELAY_TICKS = 1700;
/*     */   private static final int DIGGING_PARTICLES_DURATION_TICKS = 6000;
/*     */   private static final int DIGGING_PARTICLES_AMOUNT = 30;
/*     */   private static final int DIGGING_DROP_SEED_OFFSET_TICKS = 120;
/*     */   private static final int SNIFFER_BABY_AGE_TICKS = 48000;
/*     */   private static final float DIGGING_BB_HEIGHT_OFFSET = 0.4F;
/*     */   
/*     */   public enum State
/*     */   {
/*  81 */     IDLING(0),
/*  82 */     FEELING_HAPPY(1),
/*  83 */     SCENTING(2),
/*  84 */     SNIFFING(3),
/*  85 */     SEARCHING(4),
/*  86 */     DIGGING(5),
/*  87 */     RISING(6); public static final IntFunction<State> BY_ID;
/*     */     
/*     */     static  {
/*  90 */       BY_ID = ByIdMap.continuous(State::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */       
/*  92 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, State::id);
/*     */     }
/*     */     public static final StreamCodec<ByteBuf, State> STREAM_CODEC;
/*     */     private final int id;
/*     */     
/*  97 */     State(int id) { this.id = id; }
/*     */ 
/*     */ 
/*     */     
/* 101 */     public int id() { return this.id; }
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
/* 114 */   private static final EntityDimensions DIGGING_DIMENSIONS = EntityDimensions.scalable(EntityType.SNIFFER.getWidth(), EntityType.SNIFFER.getHeight() - 0.4F).withEyeHeight(0.81F);
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 117 */     return Animal.createAnimalAttributes()
/* 118 */       .add(Attributes.MOVEMENT_SPEED, 0.10000000149011612D)
/* 119 */       .add(Attributes.MAX_HEALTH, 14.0D);
/*     */   }
/*     */ 
/*     */   
/* 123 */   private static final EntityDataAccessor<State> DATA_STATE = SynchedEntityData.defineId(Sniffer.class, EntityDataSerializers.SNIFFER_STATE);
/* 124 */   private static final EntityDataAccessor<Integer> DATA_DROP_SEED_AT_TICK = SynchedEntityData.defineId(Sniffer.class, EntityDataSerializers.INT);
/*     */   
/* 126 */   public final AnimationState feelingHappyAnimationState = new AnimationState();
/* 127 */   public final AnimationState scentingAnimationState = new AnimationState();
/* 128 */   public final AnimationState sniffingAnimationState = new AnimationState();
/* 129 */   public final AnimationState diggingAnimationState = new AnimationState();
/* 130 */   public final AnimationState risingAnimationState = new AnimationState();
/*     */   
/*     */   public Sniffer(EntityType<? extends Animal> type, Level level) {
/* 133 */     super(type, level);
/*     */     
/* 135 */     getNavigation().setCanFloat(true);
/* 136 */     setPathfindingMalus(PathType.WATER, -1.0F);
/* 137 */     setPathfindingMalus(PathType.DANGER_POWDER_SNOW, -1.0F);
/* 138 */     setPathfindingMalus(PathType.DAMAGE_CAUTIOUS, -1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 143 */     super.defineSynchedData(entityData);
/* 144 */     entityData.define(DATA_STATE, State.IDLING);
/* 145 */     entityData.define(DATA_DROP_SEED_AT_TICK, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPathfindingStart() {
/* 150 */     super.onPathfindingStart();
/*     */ 
/*     */ 
/*     */     
/* 154 */     if (isOnFire() || isInWater()) {
/* 155 */       setPathfindingMalus(PathType.WATER, 0.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 161 */   public void onPathfindingDone() { setPathfindingMalus(PathType.WATER, -1.0F); }
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 166 */     if (getState() == State.DIGGING) {
/* 167 */       return DIGGING_DIMENSIONS.scale(getAgeScale());
/*     */     }
/* 169 */     return super.getDefaultDimensions(pose);
/*     */   }
/*     */ 
/*     */   
/* 173 */   public boolean isSearching() { return (getState() == State.SEARCHING); }
/*     */ 
/*     */ 
/*     */   
/* 177 */   public boolean isTempted() { return ((Boolean)this.brain.getMemory(MemoryModuleType.IS_TEMPTED).orElse(Boolean.valueOf(false))).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 181 */   public boolean canSniff() { return (!isTempted() && !isPanicking() && !isInWater() && !isInLove() && onGround() && !isPassenger() && !isLeashed()); }
/*     */ 
/*     */ 
/*     */   
/* 185 */   public boolean canPlayDiggingSound() { return (getState() == State.DIGGING || getState() == State.SEARCHING); }
/*     */ 
/*     */   
/*     */   private BlockPos getHeadBlock() {
/* 189 */     Vec3 position = getHeadPosition();
/*     */ 
/*     */     
/* 192 */     return BlockPos.containing(position.x(), getY() + 0.20000000298023224D, position.z());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 197 */   private Vec3 getHeadPosition() { return position().add(getForward().scale(2.25D)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   public boolean supportQuadLeash() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 207 */   public Vec3[] getQuadLeashOffsets() { return Leashable.createQuadLeashOffsets(this, -0.01D, 0.63D, 0.38D, 1.15D); }
/*     */ 
/*     */ 
/*     */   
/* 211 */   private State getState() { return (State)this.entityData.get(DATA_STATE); }
/*     */ 
/*     */   
/*     */   private Sniffer setState(State state) {
/* 215 */     this.entityData.set(DATA_STATE, state);
/* 216 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 221 */     if (DATA_STATE.equals(accessor)) {
/* 222 */       State state = getState();
/*     */       
/* 224 */       resetAnimations();
/*     */       
/* 226 */       switch (state.ordinal()) { case 2:
/* 227 */           this.scentingAnimationState.startIfStopped(this.tickCount); break;
/* 228 */         case 3: this.sniffingAnimationState.startIfStopped(this.tickCount); break;
/* 229 */         case 5: this.diggingAnimationState.startIfStopped(this.tickCount); break;
/* 230 */         case 6: this.risingAnimationState.startIfStopped(this.tickCount); break;
/* 231 */         case 1: this.feelingHappyAnimationState.startIfStopped(this.tickCount);
/*     */           break; }
/*     */       
/* 234 */       refreshDimensions();
/*     */     } 
/*     */     
/* 237 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   private void resetAnimations() {
/* 241 */     this.diggingAnimationState.stop();
/* 242 */     this.sniffingAnimationState.stop();
/* 243 */     this.risingAnimationState.stop();
/* 244 */     this.feelingHappyAnimationState.stop();
/* 245 */     this.scentingAnimationState.stop();
/*     */   }
/*     */   
/*     */   public Sniffer transitionTo(State state) {
/* 249 */     switch (state.ordinal()) {
/*     */       case 0:
/* 251 */         setState(State.IDLING);
/*     */         break;
/*     */       case 2:
/* 254 */         setState(State.SCENTING).onScentingStart();
/*     */         break;
/*     */       case 3:
/* 257 */         playSound(SoundEvents.SNIFFER_SNIFFING, 1.0F, 1.0F);
/* 258 */         setState(State.SNIFFING);
/*     */         break;
/*     */       case 4:
/* 261 */         setState(State.SEARCHING);
/*     */         break;
/*     */       case 5:
/* 264 */         setState(State.DIGGING).onDiggingStart();
/*     */         break;
/*     */       case 6:
/* 267 */         playSound(SoundEvents.SNIFFER_DIGGING_STOP, 1.0F, 1.0F);
/* 268 */         setState(State.RISING);
/*     */         break;
/*     */       case 1:
/* 271 */         playSound(SoundEvents.SNIFFER_HAPPY, 1.0F, 1.0F);
/* 272 */         setState(State.FEELING_HAPPY);
/*     */         break;
/*     */     } 
/* 275 */     return this;
/*     */   }
/*     */   
/*     */   private Sniffer onScentingStart() {
/* 279 */     playSound(SoundEvents.SNIFFER_SCENTING, 1.0F, isBaby() ? 1.3F : 1.0F);
/* 280 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   private Sniffer onDiggingStart() {
/* 285 */     this.entityData.set(DATA_DROP_SEED_AT_TICK, Integer.valueOf(this.tickCount + 120));
/*     */ 
/*     */     
/* 288 */     level().broadcastEntityEvent(this, (byte)63);
/*     */     
/* 290 */     return this;
/*     */   }
/*     */   
/*     */   public Sniffer onDiggingComplete(boolean success) {
/* 294 */     if (success) {
/* 295 */       storeExploredPosition(getOnPos());
/*     */     }
/* 297 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 301 */   Optional<BlockPos> calculateDigPosition() { return 
/* 302 */       IntStream.range(0, 5)
/* 303 */       .mapToObj(idx -> LandRandomPos.getPos(this, 10 + 2 * idx, 3))
/* 304 */       .filter(Objects::nonNull)
/* 305 */       .map(BlockPos::containing)
/* 306 */       .filter(position -> level().getWorldBorder().isWithinBounds(position))
/* 307 */       .map(BlockPos::below)
/* 308 */       .filter(this::canDig)
/* 309 */       .findFirst(); }
/*     */ 
/*     */ 
/*     */   
/* 313 */   boolean canDig() { return (!isPanicking() && !isTempted() && !isBaby() && !isInWater() && onGround() && !isPassenger() && canDig(getHeadBlock().below())); }
/*     */ 
/*     */   
/*     */   private boolean canDig(BlockPos position) {
/* 317 */     return (level().getBlockState(position).is(BlockTags.SNIFFER_DIGGABLE_BLOCK) && 
/* 318 */       getExploredPositions().noneMatch(explored -> GlobalPos.of(level().dimension(), position).equals(explored)) && (
/* 319 */       (Boolean)Optional.ofNullable(getNavigation().createPath(position, 1)).map(Path::canReach).orElse(Boolean.valueOf(false))).booleanValue());
/*     */   }
/*     */   
/*     */   private void dropSeed() {
/* 323 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1; if (((Integer)this.entityData.get(DATA_DROP_SEED_AT_TICK)).intValue() == this.tickCount) {
/*     */ 
/*     */ 
/*     */         
/* 327 */         BlockPos head = getHeadBlock();
/* 328 */         dropFromGiftLootTable(level, BuiltInLootTables.SNIFFER_DIGGING, (l, itemStack) -> {
/* 329 */               ItemEntity entity = new ItemEntity(level(), head.getX(), head.getY(), head.getZ(), itemStack);
/* 330 */               entity.setDefaultPickUpDelay();
/* 331 */               l.addFreshEntity(entity);
/*     */             });
/*     */         
/* 334 */         playSound(SoundEvents.SNIFFER_DROP_SEED, 1.0F, 1.0F);
/*     */         return;
/*     */       }  }
/*     */   
/*     */   }
/*     */   private Sniffer emitDiggingParticles(AnimationState state) {
/* 340 */     boolean emit = (state.getTimeInMillis(this.tickCount) > 1700L && state.getTimeInMillis(this.tickCount) < 6000L);
/*     */     
/* 342 */     if (emit) {
/* 343 */       BlockPos head = getHeadBlock();
/* 344 */       BlockState stateBelow = level().getBlockState(head.below());
/*     */       
/* 346 */       if (stateBelow.getRenderShape() != RenderShape.INVISIBLE) {
/* 347 */         for (int i = 0; i < 30; i++) {
/* 348 */           Vec3 centered = Vec3.atCenterOf(head).add(0.0D, -0.6499999761581421D, 0.0D);
/*     */           
/* 350 */           level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, stateBelow), centered.x, centered.y, centered.z, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */ 
/*     */         
/* 354 */         if (this.tickCount % 10 == 0) {
/* 355 */           level().playLocalSound(getX(), getY(), getZ(), stateBelow.getSoundType().getHitSound(), getSoundSource(), 0.5F, 0.5F, false);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 361 */     if (this.tickCount % 10 == 0) {
/* 362 */       level().gameEvent(GameEvent.ENTITY_ACTION, getHeadBlock(), GameEvent.Context.of(this));
/*     */     }
/*     */     
/* 365 */     return this;
/*     */   }
/*     */   
/*     */   private Sniffer storeExploredPosition(BlockPos position) {
/* 369 */     List<GlobalPos> updated = (List)getExploredPositions().limit(20L).collect(Collectors.toList());
/*     */     
/* 371 */     updated.add(0, GlobalPos.of(level().dimension(), position));
/* 372 */     getBrain().setMemory(MemoryModuleType.SNIFFER_EXPLORED_POSITIONS, updated);
/* 373 */     return this;
/*     */   }
/*     */   
/*     */   private Stream<GlobalPos> getExploredPositions() {
/* 377 */     return getBrain().getMemory(MemoryModuleType.SNIFFER_EXPLORED_POSITIONS)
/* 378 */       .stream()
/* 379 */       .flatMap(Collection::stream);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void jumpFromGround() {
/* 385 */     super.jumpFromGround();
/* 386 */     double speedModifier = this.moveControl.getSpeedModifier();
/* 387 */     if (speedModifier > 0.0D) {
/* 388 */       double current = getDeltaMovement().horizontalDistanceSqr();
/* 389 */       if (current < 0.01D) {
/* 390 */         moveRelative(0.1F, new Vec3(0.0D, 0.0D, 1.0D));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void spawnChildFromBreeding(ServerLevel level, Animal partner) {
/* 397 */     ItemStack itemStack = new ItemStack(Items.SNIFFER_EGG);
/* 398 */     ItemEntity entity = new ItemEntity(level, position().x(), position().y(), position().z(), itemStack);
/* 399 */     entity.setDefaultPickUpDelay();
/*     */     
/* 401 */     finalizeSpawnChildFromBreeding(level, partner, null);
/*     */     
/* 403 */     playSound(SoundEvents.SNIFFER_EGG_PLOP, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.5F);
/* 404 */     level.addFreshEntity(entity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void die(DamageSource source) {
/* 409 */     transitionTo(State.IDLING);
/* 410 */     super.die(source);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 415 */     switch (getState().ordinal()) { case 5:
/* 416 */         emitDiggingParticles(this.diggingAnimationState).dropSeed(); break;
/* 417 */       case 4: playSearchingSound(); break; }
/*     */     
/* 419 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 424 */     ItemStack heldItem = player.getItemInHand(hand);
/* 425 */     boolean isFood = isFood(heldItem);
/*     */     
/* 427 */     InteractionResult interactionResult = super.mobInteract(player, hand);
/* 428 */     if (interactionResult.consumesAction() && isFood) {
/* 429 */       playEatingSound();
/*     */     }
/* 431 */     return interactionResult;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 436 */   protected void playEatingSound() { level().playSound(null, this, SoundEvents.SNIFFER_EAT, SoundSource.NEUTRAL, 1.0F, Mth.randomBetween((level()).random, 0.8F, 1.2F)); }
/*     */ 
/*     */   
/*     */   private void playSearchingSound() {
/* 440 */     if (level().isClientSide() && this.tickCount % 20 == 0) {
/* 441 */       level().playLocalSound(getX(), getY(), getZ(), SoundEvents.SNIFFER_SEARCHING, getSoundSource(), 1.0F, 1.0F, false);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 447 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.SNIFFER_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 452 */   protected SoundEvent getAmbientSound() { return Set.of(State.DIGGING, State.SEARCHING).contains(getState()) ? null : SoundEvents.SNIFFER_IDLE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 457 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.SNIFFER_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 462 */   protected SoundEvent getDeathSound() { return SoundEvents.SNIFFER_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 467 */   public int getMaxHeadYRot() { return 50; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 472 */   public void setBaby(boolean baby) { setAge(baby ? -48000 : 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 477 */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) { return (AgeableMob)EntityType.SNIFFER.create(level, EntitySpawnReason.BREEDING); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canMate(Animal partner) {
/* 482 */     if (partner instanceof Sniffer) { Sniffer snifferPartner = (Sniffer)partner;
/* 483 */       Set<State> states = Set.of(State.IDLING, State.SCENTING, State.FEELING_HAPPY);
/* 484 */       return (states.contains(getState()) && states.contains(snifferPartner.getState()) && super.canMate(partner)); }
/*     */ 
/*     */     
/* 487 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 492 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.SNIFFER_FOOD); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 497 */   protected Brain<?> makeBrain(Dynamic<?> input) { return SnifferAi.makeBrain(brainProvider().makeBrain(input)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 502 */   public Brain<Sniffer> getBrain() { return super.getBrain(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 507 */   protected Brain.Provider<Sniffer> brainProvider() { return Brain.provider(SnifferAi.MEMORY_TYPES, SnifferAi.SENSOR_TYPES); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 512 */     ProfilerFiller profiler = Profiler.get();
/* 513 */     profiler.push("snifferBrain");
/* 514 */     getBrain().tick(level, this);
/*     */     
/* 516 */     profiler.popPush("snifferActivityUpdate");
/*     */     
/* 518 */     SnifferAi.updateActivity(this);
/* 519 */     profiler.pop();
/*     */     
/* 521 */     super.customServerAiStep(level);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\sniffer\Sniffer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */