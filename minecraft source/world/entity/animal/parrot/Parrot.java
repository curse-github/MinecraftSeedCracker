/*     */ package net.minecraft.world.entity.animal.parrot;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
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
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.TamableAnimal;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.FlyingMoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowMobGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LandOnOwnersShoulderGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.FlyingAnimal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Parrot
/*     */   extends ShoulderRidingEntity implements FlyingAnimal {
/*  78 */   private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(Parrot.class, EntityDataSerializers.INT);
/*  79 */   private static final Predicate<Mob> NOT_PARROT_PREDICATE = new Predicate<Mob>()
/*     */     {
/*     */       public boolean test(Mob input) {
/*  82 */         return (input != null && Parrot.MOB_SOUND_MAP.containsKey(input.getType()));
/*     */       }
/*     */     };
/*     */   
/*  86 */   private static final Map<EntityType<?>, SoundEvent> MOB_SOUND_MAP = (Map)Util.make(Maps.newHashMap(), map -> {
/*  87 */         map.put(EntityType.BLAZE, SoundEvents.PARROT_IMITATE_BLAZE);
/*  88 */         map.put(EntityType.BOGGED, SoundEvents.PARROT_IMITATE_BOGGED);
/*  89 */         map.put(EntityType.BREEZE, SoundEvents.PARROT_IMITATE_BREEZE);
/*  90 */         map.put(EntityType.CAMEL_HUSK, SoundEvents.PARROT_IMITATE_CAMEL_HUSK);
/*  91 */         map.put(EntityType.CAVE_SPIDER, SoundEvents.PARROT_IMITATE_SPIDER);
/*  92 */         map.put(EntityType.CREAKING, SoundEvents.PARROT_IMITATE_CREAKING);
/*  93 */         map.put(EntityType.CREEPER, SoundEvents.PARROT_IMITATE_CREEPER);
/*  94 */         map.put(EntityType.DROWNED, SoundEvents.PARROT_IMITATE_DROWNED);
/*  95 */         map.put(EntityType.ELDER_GUARDIAN, SoundEvents.PARROT_IMITATE_ELDER_GUARDIAN);
/*  96 */         map.put(EntityType.ENDER_DRAGON, SoundEvents.PARROT_IMITATE_ENDER_DRAGON);
/*  97 */         map.put(EntityType.ENDERMITE, SoundEvents.PARROT_IMITATE_ENDERMITE);
/*  98 */         map.put(EntityType.EVOKER, SoundEvents.PARROT_IMITATE_EVOKER);
/*  99 */         map.put(EntityType.GHAST, SoundEvents.PARROT_IMITATE_GHAST);
/* 100 */         map.put(EntityType.HAPPY_GHAST, SoundEvents.EMPTY);
/* 101 */         map.put(EntityType.GUARDIAN, SoundEvents.PARROT_IMITATE_GUARDIAN);
/* 102 */         map.put(EntityType.HOGLIN, SoundEvents.PARROT_IMITATE_HOGLIN);
/* 103 */         map.put(EntityType.HUSK, SoundEvents.PARROT_IMITATE_HUSK);
/* 104 */         map.put(EntityType.ILLUSIONER, SoundEvents.PARROT_IMITATE_ILLUSIONER);
/* 105 */         map.put(EntityType.MAGMA_CUBE, SoundEvents.PARROT_IMITATE_MAGMA_CUBE);
/* 106 */         map.put(EntityType.PARCHED, SoundEvents.PARROT_IMITATE_PARCHED);
/* 107 */         map.put(EntityType.PHANTOM, SoundEvents.PARROT_IMITATE_PHANTOM);
/* 108 */         map.put(EntityType.PIGLIN, SoundEvents.PARROT_IMITATE_PIGLIN);
/* 109 */         map.put(EntityType.PIGLIN_BRUTE, SoundEvents.PARROT_IMITATE_PIGLIN_BRUTE);
/* 110 */         map.put(EntityType.PILLAGER, SoundEvents.PARROT_IMITATE_PILLAGER);
/* 111 */         map.put(EntityType.RAVAGER, SoundEvents.PARROT_IMITATE_RAVAGER);
/* 112 */         map.put(EntityType.SHULKER, SoundEvents.PARROT_IMITATE_SHULKER);
/* 113 */         map.put(EntityType.SILVERFISH, SoundEvents.PARROT_IMITATE_SILVERFISH);
/* 114 */         map.put(EntityType.SKELETON, SoundEvents.PARROT_IMITATE_SKELETON);
/* 115 */         map.put(EntityType.SLIME, SoundEvents.PARROT_IMITATE_SLIME);
/* 116 */         map.put(EntityType.SPIDER, SoundEvents.PARROT_IMITATE_SPIDER);
/* 117 */         map.put(EntityType.STRAY, SoundEvents.PARROT_IMITATE_STRAY);
/* 118 */         map.put(EntityType.VEX, SoundEvents.PARROT_IMITATE_VEX);
/* 119 */         map.put(EntityType.VINDICATOR, SoundEvents.PARROT_IMITATE_VINDICATOR);
/* 120 */         map.put(EntityType.WARDEN, SoundEvents.PARROT_IMITATE_WARDEN);
/* 121 */         map.put(EntityType.WITCH, SoundEvents.PARROT_IMITATE_WITCH);
/* 122 */         map.put(EntityType.WITHER, SoundEvents.PARROT_IMITATE_WITHER);
/* 123 */         map.put(EntityType.WITHER_SKELETON, SoundEvents.PARROT_IMITATE_WITHER_SKELETON);
/* 124 */         map.put(EntityType.ZOGLIN, SoundEvents.PARROT_IMITATE_ZOGLIN);
/* 125 */         map.put(EntityType.ZOMBIE, SoundEvents.PARROT_IMITATE_ZOMBIE);
/* 126 */         map.put(EntityType.ZOMBIE_HORSE, SoundEvents.PARROT_IMITATE_ZOMBIE_HORSE);
/* 127 */         map.put(EntityType.ZOMBIE_NAUTILUS, SoundEvents.PARROT_IMITATE_ZOMBIE_NAUTILUS);
/* 128 */         map.put(EntityType.ZOMBIE_VILLAGER, SoundEvents.PARROT_IMITATE_ZOMBIE_VILLAGER);
/*     */       });
/*     */   
/*     */   public float flap;
/*     */   public float flapSpeed;
/*     */   public float oFlapSpeed;
/*     */   public float oFlap;
/* 135 */   private float flapping = 1.0F;
/* 136 */   private float nextFlap = 1.0F;
/*     */   private boolean partyParrot;
/*     */   private BlockPos jukebox;
/*     */   
/*     */   public enum Variant
/*     */     implements StringRepresentable {
/* 142 */     RED_BLUE(0, "red_blue"),
/* 143 */     BLUE(1, "blue"),
/* 144 */     GREEN(2, "green"),
/* 145 */     YELLOW_BLUE(3, "yellow_blue"),
/* 146 */     GRAY(4, "gray"); public static final Variant DEFAULT; private static final IntFunction<Variant> BY_ID; public static final Codec<Variant> CODEC;
/*     */     
/*     */     static  {
/* 149 */       DEFAULT = RED_BLUE;
/*     */       
/* 151 */       BY_ID = ByIdMap.continuous(Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/* 152 */       CODEC = StringRepresentable.fromEnum(Variant::values);
/*     */       
/* 154 */       Objects.requireNonNull(BY_ID); LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Variant::getId);
/*     */       
/* 156 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::getId);
/*     */     }
/*     */     @Deprecated
/*     */     public static final Codec<Variant> LEGACY_CODEC; public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC;
/*     */     
/*     */     Variant(int id, String name) {
/* 162 */       this.id = id;
/* 163 */       this.name = name;
/*     */     }
/*     */     private final int id; private final String name;
/*     */     
/* 167 */     public int getId() { return this.id; }
/*     */ 
/*     */ 
/*     */     
/* 171 */     public static Variant byId(int id) { return (Variant)BY_ID.apply(id); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 176 */     public String getSerializedName() { return this.name; }
/*     */   }
/*     */ 
/*     */   
/*     */   public Parrot(EntityType<? extends Parrot> type, Level level) {
/* 181 */     super(type, level);
/* 182 */     this.moveControl = new FlyingMoveControl(this, 10, false);
/*     */ 
/*     */ 
/*     */     
/* 186 */     setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
/* 187 */     setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
/* 188 */     setPathfindingMalus(PathType.COCOA, -1.0F);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/* 193 */     setVariant((Variant)Util.getRandom(Variant.values(), level.getRandom()));
/*     */     
/* 195 */     if (groupData == null) {
/* 196 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(false);
/*     */     }
/*     */     
/* 199 */     return super.finalizeSpawn(level, difficulty, spawnReason, ageableMobGroupData);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 204 */   public boolean isBaby() { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 209 */     this.goalSelector.addGoal(0, new TamableAnimal.TamableAnimalPanicGoal(this, 1.25D));
/* 210 */     this.goalSelector.addGoal(0, new FloatGoal(this));
/* 211 */     this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
/* 212 */     this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
/* 213 */     this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F));
/* 214 */     this.goalSelector.addGoal(2, new ParrotWanderGoal(this, 1.0D));
/* 215 */     this.goalSelector.addGoal(3, new LandOnOwnersShoulderGoal(this));
/* 216 */     this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0D, 3.0F, 7.0F));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 220 */     return Animal.createAnimalAttributes()
/* 221 */       .add(Attributes.MAX_HEALTH, 6.0D)
/* 222 */       .add(Attributes.FLYING_SPEED, 0.4000000059604645D)
/* 223 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D)
/* 224 */       .add(Attributes.ATTACK_DAMAGE, 3.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/* 229 */     FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level);
/* 230 */     flyingPathNavigation.setCanOpenDoors(false);
/* 231 */     flyingPathNavigation.setCanFloat(true);
/* 232 */     return flyingPathNavigation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 237 */     if (this.jukebox == null || !this.jukebox.closerToCenterThan(position(), 3.46D) || !level().getBlockState(this.jukebox).is(Blocks.JUKEBOX)) {
/* 238 */       this.partyParrot = false;
/* 239 */       this.jukebox = null;
/*     */     } 
/*     */     
/* 242 */     if ((level()).random.nextInt(400) == 0) {
/* 243 */       imitateNearbyMobs(level(), this);
/*     */     }
/*     */     
/* 246 */     super.aiStep();
/*     */     
/* 248 */     calculateFlapping();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRecordPlayingNearby(BlockPos jukebox, boolean isPlaying) {
/* 253 */     this.jukebox = jukebox;
/* 254 */     this.partyParrot = isPlaying;
/*     */   }
/*     */ 
/*     */   
/* 258 */   public boolean isPartyParrot() { return this.partyParrot; }
/*     */ 
/*     */   
/*     */   private void calculateFlapping() {
/* 262 */     this.oFlap = this.flap;
/* 263 */     this.oFlapSpeed = this.flapSpeed;
/*     */     
/* 265 */     this.flapSpeed += ((onGround() || isPassenger()) ? -1 : 4) * 0.3F;
/* 266 */     this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
/*     */     
/* 268 */     if (!onGround() && this.flapping < 1.0F) {
/* 269 */       this.flapping = 1.0F;
/*     */     }
/* 271 */     this.flapping *= 0.9F;
/*     */     
/* 273 */     Vec3 movement = getDeltaMovement();
/* 274 */     if (!onGround() && movement.y < 0.0D) {
/* 275 */       setDeltaMovement(movement.multiply(1.0D, 0.6D, 1.0D));
/*     */     }
/*     */     
/* 278 */     this.flap += this.flapping * 2.0F;
/*     */   }
/*     */   
/*     */   public static boolean imitateNearbyMobs(Level level, Entity entity) {
/* 282 */     if (!entity.isAlive() || entity.isSilent() || level.random.nextInt(2) != 0) {
/* 283 */       return false;
/*     */     }
/*     */     
/* 286 */     List<Mob> mobs = level.getEntitiesOfClass(Mob.class, entity.getBoundingBox().inflate(20.0D), NOT_PARROT_PREDICATE);
/* 287 */     if (!mobs.isEmpty()) {
/* 288 */       Mob mob = (Mob)mobs.get(level.random.nextInt(mobs.size()));
/* 289 */       if (!mob.isSilent()) {
/* 290 */         SoundEvent soundEvent = getImitatedSound(mob.getType());
/* 291 */         level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), soundEvent, entity.getSoundSource(), 0.7F, getPitch(level.random));
/*     */         
/* 293 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 297 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 302 */     ItemStack itemStack = player.getItemInHand(hand);
/*     */     
/* 304 */     if (!isTame() && itemStack.is(ItemTags.PARROT_FOOD)) {
/* 305 */       usePlayerItem(player, hand, itemStack);
/*     */       
/* 307 */       if (!isSilent()) {
/* 308 */         level().playSound(null, getX(), getY(), getZ(), SoundEvents.PARROT_EAT, getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
/*     */       }
/*     */       
/* 311 */       if (!level().isClientSide()) {
/* 312 */         if (this.random.nextInt(10) == 0) {
/* 313 */           tame(player);
/* 314 */           level().broadcastEntityEvent(this, (byte)7);
/*     */         } else {
/* 316 */           level().broadcastEntityEvent(this, (byte)6);
/*     */         } 
/*     */       }
/*     */       
/* 320 */       return InteractionResult.SUCCESS;
/* 321 */     }  if (itemStack.is(ItemTags.PARROT_POISONOUS_FOOD)) {
/* 322 */       usePlayerItem(player, hand, itemStack);
/*     */       
/* 324 */       addEffect(new MobEffectInstance(MobEffects.POISON, 900));
/* 325 */       if (player.isCreative() || !isInvulnerable()) {
/* 326 */         hurt(damageSources().playerAttack(player), Float.MAX_VALUE);
/*     */       }
/*     */       
/* 329 */       return InteractionResult.SUCCESS;
/* 330 */     }  if (!isFlying() && isTame() && isOwnedBy(player)) {
/* 331 */       if (!level().isClientSide()) {
/* 332 */         setOrderedToSit(!isOrderedToSit());
/*     */       }
/* 334 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 337 */     return super.mobInteract(player, hand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 342 */   public boolean isFood(ItemStack itemStack) { return false; }
/*     */ 
/*     */   
/*     */   public static boolean checkParrotSpawnRules(EntityType<Parrot> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 346 */     return (level.getBlockState(pos.below()).is(BlockTags.PARROTS_SPAWNABLE_ON) && 
/* 347 */       isBrightEnoughToSpawn(level, pos));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {}
/*     */ 
/*     */ 
/*     */   
/* 357 */   public boolean canMate(Animal partner) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 362 */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 367 */   public SoundEvent getAmbientSound() { return getAmbient(level(), (level()).random); }
/*     */ 
/*     */   
/*     */   public static SoundEvent getAmbient(Level level, RandomSource random) {
/* 371 */     if (level.getDifficulty() != Difficulty.PEACEFUL && random.nextInt(1000) == 0) {
/*     */       
/* 373 */       List<EntityType<?>> keys = Lists.newArrayList(MOB_SOUND_MAP.keySet());
/* 374 */       return getImitatedSound((EntityType)keys.get(random.nextInt(keys.size())));
/*     */     } 
/* 376 */     return SoundEvents.PARROT_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/* 380 */   private static SoundEvent getImitatedSound(EntityType<?> id) { return (SoundEvent)MOB_SOUND_MAP.getOrDefault(id, SoundEvents.PARROT_AMBIENT); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 385 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.PARROT_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 390 */   protected SoundEvent getDeathSound() { return SoundEvents.PARROT_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 395 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.PARROT_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 400 */   protected boolean isFlapping() { return (this.flyDist > this.nextFlap); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onFlap() {
/* 405 */     playSound(SoundEvents.PARROT_FLY, 0.15F, 1.0F);
/* 406 */     this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 411 */   public float getVoicePitch() { return getPitch(this.random); }
/*     */ 
/*     */ 
/*     */   
/* 415 */   public static float getPitch(RandomSource random) { return (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 420 */   public SoundSource getSoundSource() { return SoundSource.NEUTRAL; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 425 */   public boolean isPushable() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doPush(Entity entity) {
/* 430 */     if (entity instanceof Player) {
/*     */       return;
/*     */     }
/* 433 */     super.doPush(entity);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 438 */     if (isInvulnerableTo(level, source)) {
/* 439 */       return false;
/*     */     }
/*     */     
/* 442 */     setOrderedToSit(false);
/*     */     
/* 444 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */ 
/*     */   
/* 448 */   public Variant getVariant() { return Variant.byId(((Integer)this.entityData.get(DATA_VARIANT_ID)).intValue()); }
/*     */ 
/*     */ 
/*     */   
/* 452 */   private void setVariant(Variant variant) { this.entityData.set(DATA_VARIANT_ID, Integer.valueOf(variant.id)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 457 */     if (type == DataComponents.PARROT_VARIANT) {
/* 458 */       return (T)castComponentValue(type, getVariant());
/*     */     }
/*     */     
/* 461 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 466 */     applyImplicitComponentIfPresent(components, DataComponents.PARROT_VARIANT);
/* 467 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 472 */     if (type == DataComponents.PARROT_VARIANT) {
/* 473 */       setVariant((Variant)castComponentValue(DataComponents.PARROT_VARIANT, value));
/* 474 */       return true;
/*     */     } 
/*     */     
/* 477 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 482 */     super.defineSynchedData(entityData);
/* 483 */     entityData.define(DATA_VARIANT_ID, Integer.valueOf(Variant.DEFAULT.id));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 488 */     super.addAdditionalSaveData(output);
/* 489 */     output.store("Variant", Variant.LEGACY_CODEC, getVariant());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 494 */     super.readAdditionalSaveData(input);
/* 495 */     setVariant((Variant)input.read("Variant", Variant.LEGACY_CODEC).orElse(Variant.DEFAULT));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 500 */   public boolean isFlying() { return !onGround(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 505 */   protected boolean canFlyToOwner() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 510 */   public Vec3 getLeashOffset() { return new Vec3(0.0D, (0.5F * getEyeHeight()), (getBbWidth() * 0.4F)); }
/*     */   
/*     */   private static class ParrotWanderGoal
/*     */     extends WaterAvoidingRandomFlyingGoal
/*     */   {
/* 515 */     public ParrotWanderGoal(PathfinderMob mob, double speedModifier) { super(mob, speedModifier); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Vec3 getPosition() {
/* 520 */       Vec3 pos = null;
/* 521 */       if (this.mob.isInWater()) {
/* 522 */         pos = LandRandomPos.getPos(this.mob, 15, 15);
/*     */       }
/* 524 */       if (this.mob.getRandom().nextFloat() >= this.probability) {
/* 525 */         pos = getTreePos();
/*     */       }
/* 527 */       return (pos == null) ? super.getPosition() : pos;
/*     */     }
/*     */     
/*     */     private Vec3 getTreePos() {
/* 531 */       BlockPos mobPos = this.mob.blockPosition();
/*     */       
/* 533 */       BlockPos.MutableBlockPos abovePos = new BlockPos.MutableBlockPos();
/* 534 */       BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();
/* 535 */       Iterable<BlockPos> between = BlockPos.betweenClosed(
/* 536 */           Mth.floor(this.mob.getX() - 3.0D), 
/* 537 */           Mth.floor(this.mob.getY() - 6.0D), 
/* 538 */           Mth.floor(this.mob.getZ() - 3.0D), 
/* 539 */           Mth.floor(this.mob.getX() + 3.0D), 
/* 540 */           Mth.floor(this.mob.getY() + 6.0D), 
/* 541 */           Mth.floor(this.mob.getZ() + 3.0D));
/*     */ 
/*     */       
/* 544 */       for (BlockPos pos : between) {
/* 545 */         if (mobPos.equals(pos)) {
/*     */           continue;
/*     */         }
/*     */         
/* 549 */         BlockState state = this.mob.level().getBlockState(belowPos.setWithOffset(pos, Direction.DOWN));
/* 550 */         boolean canSitOn = (state.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock || state.is(BlockTags.LOGS));
/* 551 */         if (canSitOn && this.mob.level().isEmptyBlock(pos) && this.mob.level().isEmptyBlock(abovePos.setWithOffset(pos, Direction.UP))) {
/* 552 */           return Vec3.atBottomCenterOf(pos);
/*     */         }
/*     */       } 
/*     */       
/* 556 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\parrot\Parrot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */