/*     */ package net.minecraft.world.entity.animal.armadillo;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Armadillo
/*     */   extends Animal
/*     */ {
/*     */   public static final float BABY_SCALE = 0.6F;
/*     */   public static final float MAX_HEAD_ROTATION_EXTENT = 32.5F;
/*     */   public static final int SCARE_CHECK_INTERVAL = 80;
/*     */   private static final double SCARE_DISTANCE_HORIZONTAL = 7.0D;
/*     */   private static final double SCARE_DISTANCE_VERTICAL = 2.0D;
/*  63 */   private static final EntityDataAccessor<ArmadilloState> ARMADILLO_STATE = SynchedEntityData.defineId(Armadillo.class, EntityDataSerializers.ARMADILLO_STATE);
/*  64 */   private long inStateTicks = 0L;
/*  65 */   public final AnimationState rollOutAnimationState = new AnimationState();
/*  66 */   public final AnimationState rollUpAnimationState = new AnimationState();
/*  67 */   public final AnimationState peekAnimationState = new AnimationState();
/*     */   
/*     */   private int scuteTime;
/*     */   
/*     */   private boolean peekReceivedClient = false;
/*     */   
/*     */   public Armadillo(EntityType<? extends Animal> type, Level level) {
/*  74 */     super(type, level);
/*  75 */     getNavigation().setCanFloat(true);
/*  76 */     this.scuteTime = pickNextScuteDropTime();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) { return (AgeableMob)EntityType.ARMADILLO.create(level, EntitySpawnReason.BREEDING); }
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  85 */     return Animal.createAnimalAttributes()
/*  86 */       .add(Attributes.MAX_HEALTH, 12.0D)
/*  87 */       .add(Attributes.MOVEMENT_SPEED, 0.14D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  92 */     super.defineSynchedData(entityData);
/*  93 */     entityData.define(ARMADILLO_STATE, ArmadilloState.IDLE);
/*     */   }
/*     */ 
/*     */   
/*  97 */   public boolean isScared() { return (this.entityData.get(ARMADILLO_STATE) != ArmadilloState.IDLE); }
/*     */ 
/*     */ 
/*     */   
/* 101 */   public boolean shouldHideInShell() { return getState().shouldHideInShell(this.inStateTicks); }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public boolean shouldSwitchToScaredState() { return (getState() == ArmadilloState.ROLLING && this.inStateTicks > ArmadilloState.ROLLING.animationDuration()); }
/*     */ 
/*     */ 
/*     */   
/* 109 */   public ArmadilloState getState() { return (ArmadilloState)this.entityData.get(ARMADILLO_STATE); }
/*     */ 
/*     */ 
/*     */   
/* 113 */   public void switchToState(ArmadilloState state) { this.entityData.set(ARMADILLO_STATE, state); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 118 */     if (ARMADILLO_STATE.equals(accessor)) {
/* 119 */       this.inStateTicks = 0L;
/*     */     }
/* 121 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 126 */   protected Brain.Provider<Armadillo> brainProvider() { return ArmadilloAi.brainProvider(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   protected Brain<?> makeBrain(Dynamic<?> input) { return ArmadilloAi.makeBrain(brainProvider().makeBrain(input)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 136 */     ProfilerFiller profiler = Profiler.get();
/* 137 */     profiler.push("armadilloBrain");
/* 138 */     this.brain.tick(level, this);
/* 139 */     profiler.pop();
/*     */     
/* 141 */     profiler.push("armadilloActivityUpdate");
/* 142 */     ArmadilloAi.updateActivity(this);
/* 143 */     profiler.pop();
/*     */     
/* 145 */     if (isAlive() && --this.scuteTime <= 0 && shouldDropLoot(level)) {
/* 146 */       if (dropFromGiftLootTable(level, BuiltInLootTables.ARMADILLO_SHED, this::spawnAtLocation)) {
/* 147 */         playSound(SoundEvents.ARMADILLO_SCUTE_DROP, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/* 148 */         gameEvent(GameEvent.ENTITY_PLACE);
/*     */       } 
/* 150 */       this.scuteTime = pickNextScuteDropTime();
/*     */     } 
/*     */     
/* 153 */     super.customServerAiStep(level);
/*     */   }
/*     */ 
/*     */   
/* 157 */   private int pickNextScuteDropTime() { return this.random.nextInt(20 * TimeUtil.SECONDS_PER_MINUTE * 5) + 20 * TimeUtil.SECONDS_PER_MINUTE * 5; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 162 */     super.tick();
/* 163 */     if (level().isClientSide()) {
/* 164 */       setupAnimationStates();
/*     */     }
/* 166 */     if (isScared()) {
/* 167 */       clampHeadRotationToBody();
/*     */     }
/*     */     
/* 170 */     this.inStateTicks++;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 175 */   public float getAgeScale() { return isBaby() ? 0.6F : 1.0F; }
/*     */ 
/*     */   
/*     */   private void setupAnimationStates() {
/* 179 */     switch (getState().ordinal()) {
/*     */       case 0:
/* 181 */         this.rollOutAnimationState.stop();
/* 182 */         this.rollUpAnimationState.stop();
/* 183 */         this.peekAnimationState.stop();
/*     */         break;
/*     */       case 3:
/* 186 */         this.rollOutAnimationState.startIfStopped(this.tickCount);
/* 187 */         this.rollUpAnimationState.stop();
/* 188 */         this.peekAnimationState.stop();
/*     */         break;
/*     */       case 1:
/* 191 */         this.rollOutAnimationState.stop();
/* 192 */         this.rollUpAnimationState.startIfStopped(this.tickCount);
/* 193 */         this.peekAnimationState.stop();
/*     */         break;
/*     */       case 2:
/* 196 */         this.rollOutAnimationState.stop();
/* 197 */         this.rollUpAnimationState.stop();
/* 198 */         if (this.peekReceivedClient) {
/* 199 */           this.peekAnimationState.stop();
/* 200 */           this.peekReceivedClient = false;
/*     */         } 
/*     */         
/* 203 */         if (this.inStateTicks == 0L) {
/* 204 */           this.peekAnimationState.start(this.tickCount);
/* 205 */           this.peekAnimationState.fastForward(ArmadilloState.SCARED.animationDuration(), 1.0F); break;
/*     */         } 
/* 207 */         this.peekAnimationState.startIfStopped(this.tickCount);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 215 */     if (id == 64 && level().isClientSide()) {
/*     */       
/* 217 */       this.peekReceivedClient = true;
/* 218 */       level().playLocalSound(getX(), getY(), getZ(), SoundEvents.ARMADILLO_PEEK, getSoundSource(), 1.0F, 1.0F, false);
/*     */     } else {
/* 220 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 226 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.ARMADILLO_FOOD); }
/*     */ 
/*     */   
/*     */   public static boolean checkArmadilloSpawnRules(EntityType<Armadillo> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 230 */     return (level.getBlockState(pos.below()).is(BlockTags.ARMADILLO_SPAWNABLE_ON) && 
/* 231 */       isBrightEnoughToSpawn(level, pos));
/*     */   }
/*     */   
/*     */   public boolean isScaredBy(LivingEntity livingEntity) {
/* 235 */     if (!getBoundingBox().inflate(7.0D, 2.0D, 7.0D).intersects(livingEntity.getBoundingBox())) {
/* 236 */       return false;
/*     */     }
/* 238 */     if (livingEntity.getType().is(EntityTypeTags.UNDEAD)) {
/* 239 */       return true;
/*     */     }
/* 241 */     if (getLastHurtByMob() == livingEntity) {
/* 242 */       return true;
/*     */     }
/* 244 */     if (livingEntity instanceof Player) { Player player = (Player)livingEntity;
/* 245 */       if (player.isSpectator()) {
/* 246 */         return false;
/*     */       }
/* 248 */       return (player.isSprinting() || player.isPassenger()); }
/*     */     
/* 250 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 255 */     super.addAdditionalSaveData(output);
/* 256 */     output.store("state", ArmadilloState.CODEC, getState());
/* 257 */     output.putInt("scute_time", this.scuteTime);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 262 */     super.readAdditionalSaveData(input);
/* 263 */     switchToState((ArmadilloState)input.read("state", ArmadilloState.CODEC).orElse(ArmadilloState.IDLE));
/*     */     
/* 265 */     input.getInt("scute_time").ifPresent(time -> this.scuteTime = time.intValue());
/*     */   }
/*     */   
/*     */   public void rollUp() {
/* 269 */     if (isScared()) {
/*     */       return;
/*     */     }
/* 272 */     stopInPlace();
/* 273 */     resetLove();
/* 274 */     gameEvent(GameEvent.ENTITY_ACTION);
/* 275 */     makeSound(SoundEvents.ARMADILLO_ROLL);
/* 276 */     switchToState(ArmadilloState.ROLLING);
/*     */   }
/*     */   
/*     */   public void rollOut() {
/* 280 */     if (!isScared()) {
/*     */       return;
/*     */     }
/* 283 */     gameEvent(GameEvent.ENTITY_ACTION);
/* 284 */     makeSound(SoundEvents.ARMADILLO_UNROLL_FINISH);
/* 285 */     switchToState(ArmadilloState.IDLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 290 */     if (isScared()) {
/* 291 */       damage = (damage - 1.0F) / 2.0F;
/*     */     }
/* 293 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actuallyHurt(ServerLevel level, DamageSource source, float dmg) {
/* 298 */     super.actuallyHurt(level, source, dmg);
/* 299 */     if (isNoAi() || isDeadOrDying()) {
/*     */       return;
/*     */     }
/*     */     
/* 303 */     if (source.getEntity() instanceof LivingEntity) {
/* 304 */       getBrain().setMemoryWithExpiry(MemoryModuleType.DANGER_DETECTED_RECENTLY, Boolean.valueOf(true), 80L);
/* 305 */       if (canStayRolledUp()) {
/* 306 */         rollUp();
/*     */       }
/* 308 */     } else if (source.is(DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES)) {
/* 309 */       rollOut();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 315 */     ItemStack itemStack = player.getItemInHand(hand);
/*     */     
/* 317 */     if (itemStack.is(Items.BRUSH) && brushOffScute(player, itemStack)) {
/* 318 */       itemStack.hurtAndBreak(16, player, hand.asEquipmentSlot());
/* 319 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 322 */     if (isScared()) {
/* 323 */       return InteractionResult.FAIL;
/*     */     }
/* 325 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   public boolean brushOffScute(Entity interactingEntity, ItemStack tool) {
/* 329 */     if (isBaby()) {
/* 330 */       return false;
/*     */     }
/* 332 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/* 333 */       dropFromEntityInteractLootTable(level, BuiltInLootTables.ARMADILLO_BRUSH, interactingEntity, tool, this::spawnAtLocation);
/* 334 */       playSound(SoundEvents.ARMADILLO_BRUSH);
/* 335 */       gameEvent(GameEvent.ENTITY_INTERACT); }
/*     */     
/* 337 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 341 */   public boolean canStayRolledUp() { return (!isPanicking() && !isInLiquid() && !isLeashed() && !isPassenger() && !isVehicle()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 346 */   public boolean canFallInLove() { return (super.canFallInLove() && !isScared()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 351 */     if (isScared()) {
/* 352 */       return null;
/*     */     }
/* 354 */     return SoundEvents.ARMADILLO_AMBIENT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 359 */   protected void playEatingSound() { makeSound(SoundEvents.ARMADILLO_EAT); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 364 */   protected SoundEvent getDeathSound() { return SoundEvents.ARMADILLO_DEATH; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 369 */     if (isScared()) {
/* 370 */       return SoundEvents.ARMADILLO_HURT_REDUCED;
/*     */     }
/* 372 */     return SoundEvents.ARMADILLO_HURT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 377 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.ARMADILLO_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 382 */     if (isScared()) {
/* 383 */       return 0;
/*     */     }
/* 385 */     return 32;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BodyRotationControl createBodyControl() {
/* 390 */     return new BodyRotationControl(this)
/*     */       {
/*     */         public void clientTick() {
/* 393 */           if (!Armadillo.this.isScared()) {
/* 394 */             super.clientTick();
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final abstract enum ArmadilloState
/*     */     implements StringRepresentable
/*     */   {
/*     */     IDLE, ROLLING, SCARED, UNROLLING;
/*     */ 
/*     */ 
/*     */     
/*     */     private static final Codec<ArmadilloState> CODEC;
/*     */ 
/*     */     
/*     */     private static final IntFunction<ArmadilloState> BY_ID;
/*     */ 
/*     */     
/*     */     public static final StreamCodec<ByteBuf, ArmadilloState> STREAM_CODEC;
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     private final boolean isThreatened;
/*     */ 
/*     */     
/*     */     private final int animationDuration;
/*     */ 
/*     */     
/*     */     private final int id;
/*     */ 
/*     */ 
/*     */     
/*     */     static  {
/*     */       // Byte code:
/*     */       //   0: new net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState$1
/*     */       //   3: dup
/*     */       //   4: ldc 'IDLE'
/*     */       //   6: iconst_0
/*     */       //   7: ldc 'idle'
/*     */       //   9: iconst_0
/*     */       //   10: iconst_0
/*     */       //   11: iconst_0
/*     */       //   12: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;ZII)V
/*     */       //   15: putstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.IDLE : Lnet/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState;
/*     */       //   18: new net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState$2
/*     */       //   21: dup
/*     */       //   22: ldc 'ROLLING'
/*     */       //   24: iconst_1
/*     */       //   25: ldc 'rolling'
/*     */       //   27: iconst_1
/*     */       //   28: bipush #10
/*     */       //   30: iconst_1
/*     */       //   31: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;ZII)V
/*     */       //   34: putstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.ROLLING : Lnet/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState;
/*     */       //   37: new net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState$3
/*     */       //   40: dup
/*     */       //   41: ldc 'SCARED'
/*     */       //   43: iconst_2
/*     */       //   44: ldc 'scared'
/*     */       //   46: iconst_1
/*     */       //   47: bipush #50
/*     */       //   49: iconst_2
/*     */       //   50: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;ZII)V
/*     */       //   53: putstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.SCARED : Lnet/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState;
/*     */       //   56: new net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState$4
/*     */       //   59: dup
/*     */       //   60: ldc 'UNROLLING'
/*     */       //   62: iconst_3
/*     */       //   63: ldc 'unrolling'
/*     */       //   65: iconst_1
/*     */       //   66: bipush #30
/*     */       //   68: iconst_3
/*     */       //   69: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;ZII)V
/*     */       //   72: putstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.UNROLLING : Lnet/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState;
/*     */       //   75: invokestatic $values : ()[Lnet/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState;
/*     */       //   78: putstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.$VALUES : [Lnet/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState;
/*     */       //   81: <illegal opcode> get : ()Ljava/util/function/Supplier;
/*     */       //   86: invokestatic fromEnum : (Ljava/util/function/Supplier;)Lnet/minecraft/util/StringRepresentable$EnumCodec;
/*     */       //   89: putstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.CODEC : Lcom/mojang/serialization/Codec;
/*     */       //   92: <illegal opcode> applyAsInt : ()Ljava/util/function/ToIntFunction;
/*     */       //   97: invokestatic values : ()[Lnet/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState;
/*     */       //   100: getstatic net/minecraft/util/ByIdMap$OutOfBoundsStrategy.ZERO : Lnet/minecraft/util/ByIdMap$OutOfBoundsStrategy;
/*     */       //   103: invokestatic continuous : (Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/ByIdMap$OutOfBoundsStrategy;)Ljava/util/function/IntFunction;
/*     */       //   106: putstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.BY_ID : Ljava/util/function/IntFunction;
/*     */       //   109: getstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.BY_ID : Ljava/util/function/IntFunction;
/*     */       //   112: <illegal opcode> applyAsInt : ()Ljava/util/function/ToIntFunction;
/*     */       //   117: invokestatic idMapper : (Ljava/util/function/IntFunction;Ljava/util/function/ToIntFunction;)Lnet/minecraft/network/codec/StreamCodec;
/*     */       //   120: putstatic net/minecraft/world/entity/animal/armadillo/Armadillo$ArmadilloState.STREAM_CODEC : Lnet/minecraft/network/codec/StreamCodec;
/*     */       //   123: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #402	-> 0
/*     */       //   #408	-> 18
/*     */       //   #414	-> 37
/*     */       //   #420	-> 56
/*     */       //   #400	-> 75
/*     */       //   #428	-> 81
/*     */       //   #429	-> 92
/*     */       //   #431	-> 109
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     ArmadilloState(String name, boolean isThreatened, int animationDuration, int id) {
/* 439 */       this.name = name;
/* 440 */       this.isThreatened = isThreatened;
/* 441 */       this.animationDuration = animationDuration;
/* 442 */       this.id = id;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 447 */     public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */     
/* 451 */     private int id() { return this.id; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 457 */     public boolean isThreatened() { return this.isThreatened; }
/*     */ 
/*     */ 
/*     */     
/* 461 */     public int animationDuration() { return this.animationDuration; }
/*     */     
/*     */     public abstract boolean shouldHideInShell(long param1Long);
/*     */   }
/*     */   
/*     */   static enum null {
/*     */     public boolean shouldHideInShell(long ticksInState) { return false; }
/*     */   }
/*     */   
/*     */   static enum null {
/*     */     public boolean shouldHideInShell(long ticksInState) { return (ticksInState > 5L); }
/*     */   }
/*     */   
/*     */   static enum null {
/*     */     public boolean shouldHideInShell(long ticksInState) { return true; }
/*     */   }
/*     */   
/*     */   static enum null {
/*     */     public boolean shouldHideInShell(long ticksInState) { return (ticksInState < 26L); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\armadillo\Armadillo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */