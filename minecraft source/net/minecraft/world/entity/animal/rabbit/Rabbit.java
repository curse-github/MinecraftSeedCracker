/*     */ package net.minecraft.world.entity.animal.rabbit;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.JumpControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CarrotBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Rabbit
/*     */   extends Animal
/*     */ {
/*     */   public static final double STROLL_SPEED_MOD = 0.6D;
/*     */   public static final double BREED_SPEED_MOD = 0.8D;
/*     */   public static final double FOLLOW_SPEED_MOD = 1.0D;
/*     */   public static final double FLEE_SPEED_MOD = 2.2D;
/*     */   public static final double ATTACK_SPEED_MOD = 1.4D;
/*  87 */   private static final EntityDataAccessor<Integer> DATA_TYPE_ID = SynchedEntityData.defineId(Rabbit.class, EntityDataSerializers.INT); private static final int DEFAULT_MORE_CARROT_TICKS = 0;
/*     */   
/*     */   public enum Variant implements StringRepresentable { public static final Variant DEFAULT;
/*     */     private static final IntFunction<Variant> BY_ID;
/*  91 */     BROWN(0, "brown"),
/*  92 */     WHITE(1, "white"),
/*  93 */     BLACK(2, "black"),
/*  94 */     WHITE_SPLOTCHED(3, "white_splotched"),
/*  95 */     GOLD(4, "gold"),
/*  96 */     SALT(5, "salt"),
/*  97 */     EVIL(99, "evil"); public static final Codec<Variant> CODEC;
/*     */     
/*     */     static  {
/* 100 */       DEFAULT = BROWN;
/*     */       
/* 102 */       BY_ID = ByIdMap.sparse(Variant::id, values(), DEFAULT);
/*     */       
/* 104 */       CODEC = StringRepresentable.fromEnum(Variant::values);
/*     */       
/* 106 */       Objects.requireNonNull(BY_ID); LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Variant::id);
/*     */       
/* 108 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::id);
/*     */     }
/*     */     @Deprecated
/*     */     public static final Codec<Variant> LEGACY_CODEC; public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC;
/*     */     
/*     */     Variant(int id, String name) {
/* 114 */       this.id = id;
/* 115 */       this.name = name;
/*     */     }
/*     */     private final int id;
/*     */     private final String name;
/*     */     
/* 120 */     public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */     
/* 124 */     public int id() { return this.id; }
/*     */ 
/*     */ 
/*     */     
/* 128 */     public static Variant byId(int id) { return (Variant)BY_ID.apply(id); } }
/*     */ 
/*     */ 
/*     */   
/* 132 */   private static final Identifier KILLER_BUNNY = Identifier.withDefaultNamespace("killer_bunny");
/*     */   
/*     */   private static final int DEFAULT_ATTACK_POWER = 3;
/*     */   private static final int EVIL_ATTACK_POWER_INCREMENT = 5;
/* 136 */   private static final Identifier EVIL_ATTACK_POWER_MODIFIER = Identifier.withDefaultNamespace("evil");
/*     */   
/*     */   private static final int EVIL_ARMOR_VALUE = 8;
/*     */   
/*     */   private static final int MORE_CARROTS_DELAY = 40;
/*     */   
/*     */   private int jumpTicks;
/*     */   
/*     */   private int jumpDuration;
/*     */   private boolean wasOnGround;
/*     */   private int jumpDelayTicks;
/* 147 */   private int moreCarrotTicks = 0;
/*     */   
/*     */   public Rabbit(EntityType<? extends Rabbit> type, Level level) {
/* 150 */     super(type, level);
/*     */     
/* 152 */     this.jumpControl = new RabbitJumpControl(this);
/*     */     
/* 154 */     this.moveControl = new RabbitMoveControl(this);
/*     */     
/* 156 */     setSpeedModifier(0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 161 */     this.goalSelector.addGoal(1, new FloatGoal(this));
/* 162 */     this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, level()));
/* 163 */     this.goalSelector.addGoal(1, new RabbitPanicGoal(this, 2.2D));
/* 164 */     this.goalSelector.addGoal(2, new BreedGoal(this, 0.8D));
/* 165 */     this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, i -> i.is(ItemTags.RABBIT_FOOD), false));
/* 166 */     this.goalSelector.addGoal(4, new RabbitAvoidEntityGoal(this, net.minecraft.world.entity.player.Player.class, 8.0F, 2.2D, 2.2D));
/* 167 */     this.goalSelector.addGoal(4, new RabbitAvoidEntityGoal(this, net.minecraft.world.entity.animal.wolf.Wolf.class, 10.0F, 2.2D, 2.2D));
/* 168 */     this.goalSelector.addGoal(4, new RabbitAvoidEntityGoal(this, net.minecraft.world.entity.monster.Monster.class, 4.0F, 2.2D, 2.2D));
/* 169 */     this.goalSelector.addGoal(5, new RaidGardenGoal(this));
/* 170 */     this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6D));
/* 171 */     this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 10.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getJumpPower() {
/* 176 */     float baseJumpPower = 0.3F;
/*     */     
/* 178 */     if (this.moveControl.getSpeedModifier() <= 0.6D) {
/* 179 */       baseJumpPower = 0.2F;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     Path path = this.navigation.getPath();
/* 186 */     if (path != null && !path.isDone()) {
/* 187 */       Vec3 currentPos = path.getNextEntityPos(this);
/* 188 */       if (currentPos.y > getY() + 0.5D) {
/* 189 */         baseJumpPower = 0.5F;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     if (this.horizontalCollision || (this.jumping && this.moveControl.getWantedY() > getY() + 0.5D)) {
/* 199 */       baseJumpPower = 0.5F;
/*     */     }
/*     */     
/* 202 */     return getJumpPower(baseJumpPower / 0.42F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void jumpFromGround() {
/* 207 */     super.jumpFromGround();
/* 208 */     double speedModifier = this.moveControl.getSpeedModifier();
/* 209 */     if (speedModifier > 0.0D) {
/* 210 */       double current = getDeltaMovement().horizontalDistanceSqr();
/* 211 */       if (current < 0.01D)
/*     */       {
/* 213 */         moveRelative(0.1F, new Vec3(0.0D, 0.0D, 1.0D));
/*     */       }
/*     */     } 
/* 216 */     if (!level().isClientSide()) {
/* 217 */       level().broadcastEntityEvent(this, (byte)1);
/*     */     }
/*     */   }
/*     */   
/*     */   public float getJumpCompletion(float a) {
/* 222 */     if (this.jumpDuration == 0) {
/* 223 */       return 0.0F;
/*     */     }
/* 225 */     return (this.jumpTicks + a) / this.jumpDuration;
/*     */   }
/*     */   
/*     */   public void setSpeedModifier(double speed) {
/* 229 */     getNavigation().setSpeedModifier(speed);
/* 230 */     this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), speed);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setJumping(boolean jump) {
/* 235 */     super.setJumping(jump);
/* 236 */     if (jump) {
/* 237 */       playSound(getJumpSound(), getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
/*     */     }
/*     */   }
/*     */   
/*     */   public void startJumping() {
/* 242 */     setJumping(true);
/* 243 */     this.jumpDuration = 10;
/* 244 */     this.jumpTicks = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 249 */     super.defineSynchedData(entityData);
/*     */     
/* 251 */     entityData.define(DATA_TYPE_ID, Integer.valueOf(Variant.DEFAULT.id));
/*     */   }
/*     */ 
/*     */   
/*     */   public void customServerAiStep(ServerLevel level) {
/* 256 */     if (this.jumpDelayTicks > 0) {
/* 257 */       this.jumpDelayTicks--;
/*     */     }
/*     */     
/* 260 */     if (this.moreCarrotTicks > 0) {
/* 261 */       this.moreCarrotTicks -= this.random.nextInt(3);
/* 262 */       if (this.moreCarrotTicks < 0) {
/* 263 */         this.moreCarrotTicks = 0;
/*     */       }
/*     */     } 
/*     */     
/* 267 */     if (onGround()) {
/* 268 */       if (!this.wasOnGround) {
/* 269 */         setJumping(false);
/* 270 */         checkLandingDelay();
/*     */       } 
/*     */       
/* 273 */       if (getVariant() == Variant.EVIL && this.jumpDelayTicks == 0) {
/* 274 */         LivingEntity target = getTarget();
/* 275 */         if (target != null && distanceToSqr(target) < 16.0D) {
/* 276 */           facePoint(target.getX(), target.getZ());
/* 277 */           this.moveControl.setWantedPosition(target.getX(), target.getY(), target.getZ(), this.moveControl.getSpeedModifier());
/* 278 */           startJumping();
/* 279 */           this.wasOnGround = true;
/*     */         } 
/*     */       } 
/*     */       
/* 283 */       RabbitJumpControl jumpControl = (RabbitJumpControl)this.jumpControl;
/* 284 */       if (!jumpControl.wantJump()) {
/* 285 */         if (this.moveControl.hasWanted() && this.jumpDelayTicks == 0) {
/* 286 */           Path path = this.navigation.getPath();
/* 287 */           Vec3 pos = new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ());
/* 288 */           if (path != null && !path.isDone()) {
/* 289 */             pos = path.getNextEntityPos(this);
/*     */           }
/* 291 */           facePoint(pos.x, pos.z);
/* 292 */           startJumping();
/*     */         } 
/* 294 */       } else if (!jumpControl.canJump()) {
/* 295 */         enableJumpControl();
/*     */       } 
/*     */     } 
/*     */     
/* 299 */     this.wasOnGround = onGround();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 304 */   public boolean canSpawnSprintParticle() { return false; }
/*     */ 
/*     */ 
/*     */   
/* 308 */   private void facePoint(double faceX, double faceZ) { setYRot((float)(Mth.atan2(faceZ - getZ(), faceX - getX()) * 57.2957763671875D) - 90.0F); }
/*     */ 
/*     */ 
/*     */   
/* 312 */   private void enableJumpControl() { ((RabbitJumpControl)this.jumpControl).setCanJump(true); }
/*     */ 
/*     */ 
/*     */   
/* 316 */   private void disableJumpControl() { ((RabbitJumpControl)this.jumpControl).setCanJump(false); }
/*     */ 
/*     */   
/*     */   private void setLandingDelay() {
/* 320 */     if (this.moveControl.getSpeedModifier() < 2.2D) {
/* 321 */       this.jumpDelayTicks = 10;
/*     */     } else {
/* 323 */       this.jumpDelayTicks = 1;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkLandingDelay() {
/* 328 */     setLandingDelay();
/* 329 */     disableJumpControl();
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 334 */     super.aiStep();
/* 335 */     if (this.jumpTicks != this.jumpDuration) {
/* 336 */       this.jumpTicks++;
/* 337 */     } else if (this.jumpDuration != 0) {
/* 338 */       this.jumpTicks = 0;
/* 339 */       this.jumpDuration = 0;
/* 340 */       setJumping(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 345 */     return Animal.createAnimalAttributes()
/* 346 */       .add(Attributes.MAX_HEALTH, 3.0D)
/* 347 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 348 */       .add(Attributes.ATTACK_DAMAGE, 3.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 353 */     super.addAdditionalSaveData(output);
/* 354 */     output.store("RabbitType", Variant.LEGACY_CODEC, getVariant());
/* 355 */     output.putInt("MoreCarrotTicks", this.moreCarrotTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 360 */     super.readAdditionalSaveData(input);
/* 361 */     setVariant((Variant)input.read("RabbitType", Variant.LEGACY_CODEC).orElse(Variant.DEFAULT));
/* 362 */     this.moreCarrotTicks = input.getIntOr("MoreCarrotTicks", 0);
/*     */   }
/*     */ 
/*     */   
/* 366 */   protected SoundEvent getJumpSound() { return SoundEvents.RABBIT_JUMP; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 371 */   protected SoundEvent getAmbientSound() { return SoundEvents.RABBIT_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 376 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.RABBIT_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 381 */   protected SoundEvent getDeathSound() { return SoundEvents.RABBIT_DEATH; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void playAttackSound() {
/* 386 */     if (getVariant() == Variant.EVIL) {
/* 387 */       playSound(SoundEvents.RABBIT_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 393 */   public SoundSource getSoundSource() { return (getVariant() == Variant.EVIL) ? SoundSource.HOSTILE : SoundSource.NEUTRAL; }
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
/*     */   public Rabbit getBreedOffspring(ServerLevel level, AgeableMob partner) { // Byte code:
/*     */     //   0: getstatic net/minecraft/world/entity/EntityType.RABBIT : Lnet/minecraft/world/entity/EntityType;
/*     */     //   3: aload_1
/*     */     //   4: getstatic net/minecraft/world/entity/EntitySpawnReason.BREEDING : Lnet/minecraft/world/entity/EntitySpawnReason;
/*     */     //   7: invokevirtual create : (Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnReason;)Lnet/minecraft/world/entity/Entity;
/*     */     //   10: checkcast net/minecraft/world/entity/animal/rabbit/Rabbit
/*     */     //   13: astore_3
/*     */     //   14: aload_3
/*     */     //   15: ifnull -> 89
/*     */     //   18: aload_1
/*     */     //   19: aload_0
/*     */     //   20: invokevirtual blockPosition : ()Lnet/minecraft/core/BlockPos;
/*     */     //   23: invokestatic getRandomRabbitVariant : (Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/entity/animal/rabbit/Rabbit$Variant;
/*     */     //   26: astore #4
/*     */     //   28: aload_0
/*     */     //   29: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   32: bipush #20
/*     */     //   34: invokeinterface nextInt : (I)I
/*     */     //   39: ifeq -> 83
/*     */     //   42: aload_2
/*     */     //   43: instanceof net/minecraft/world/entity/animal/rabbit/Rabbit
/*     */     //   46: ifeq -> 77
/*     */     //   49: aload_2
/*     */     //   50: checkcast net/minecraft/world/entity/animal/rabbit/Rabbit
/*     */     //   53: astore #5
/*     */     //   55: aload_0
/*     */     //   56: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   59: invokeinterface nextBoolean : ()Z
/*     */     //   64: ifeq -> 77
/*     */     //   67: aload #5
/*     */     //   69: invokevirtual getVariant : ()Lnet/minecraft/world/entity/animal/rabbit/Rabbit$Variant;
/*     */     //   72: astore #4
/*     */     //   74: goto -> 83
/*     */     //   77: aload_0
/*     */     //   78: invokevirtual getVariant : ()Lnet/minecraft/world/entity/animal/rabbit/Rabbit$Variant;
/*     */     //   81: astore #4
/*     */     //   83: aload_3
/*     */     //   84: aload #4
/*     */     //   86: invokevirtual setVariant : (Lnet/minecraft/world/entity/animal/rabbit/Rabbit$Variant;)V
/*     */     //   89: aload_3
/*     */     //   90: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #398	-> 0
/*     */     //   #399	-> 14
/*     */     //   #400	-> 18
/*     */     //   #401	-> 28
/*     */     //   #402	-> 42
/*     */     //   #403	-> 67
/*     */     //   #405	-> 77
/*     */     //   #408	-> 83
/*     */     //   #410	-> 89
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   55	22	5	rabbitPartner	Lnet/minecraft/world/entity/animal/rabbit/Rabbit;
/*     */     //   28	61	4	variant	Lnet/minecraft/world/entity/animal/rabbit/Rabbit$Variant;
/*     */     //   0	91	0	this	Lnet/minecraft/world/entity/animal/rabbit/Rabbit;
/*     */     //   0	91	1	level	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   0	91	2	partner	Lnet/minecraft/world/entity/AgeableMob;
/*     */     //   14	77	3	offspring	Lnet/minecraft/world/entity/animal/rabbit/Rabbit; }
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
/* 415 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.RABBIT_FOOD); }
/*     */ 
/*     */ 
/*     */   
/* 419 */   public Variant getVariant() { return Variant.byId(((Integer)this.entityData.get(DATA_TYPE_ID)).intValue()); }
/*     */ 
/*     */   
/*     */   private void setVariant(Variant variant) {
/* 423 */     if (variant == Variant.EVIL) {
/* 424 */       getAttribute(Attributes.ARMOR).setBaseValue(8.0D);
/* 425 */       this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.4D, true));
/* 426 */       this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
/* 427 */       this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.player.Player.class, true));
/* 428 */       this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.animal.wolf.Wolf.class, true));
/* 429 */       getAttribute(Attributes.ATTACK_DAMAGE).addOrUpdateTransientModifier(new AttributeModifier(EVIL_ATTACK_POWER_MODIFIER, 5.0D, AttributeModifier.Operation.ADD_VALUE));
/*     */       
/* 431 */       if (!hasCustomName()) {
/* 432 */         setCustomName(Component.translatable(Util.makeDescriptionId("entity", KILLER_BUNNY)));
/*     */       }
/*     */     } else {
/* 435 */       getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(EVIL_ATTACK_POWER_MODIFIER);
/*     */     } 
/*     */     
/* 438 */     this.entityData.set(DATA_TYPE_ID, Integer.valueOf(variant.id));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 443 */     if (type == DataComponents.RABBIT_VARIANT) {
/* 444 */       return (T)castComponentValue(type, getVariant());
/*     */     }
/*     */     
/* 447 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 452 */     applyImplicitComponentIfPresent(components, DataComponents.RABBIT_VARIANT);
/* 453 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 458 */     if (type == DataComponents.RABBIT_VARIANT) {
/* 459 */       setVariant((Variant)castComponentValue(DataComponents.RABBIT_VARIANT, value));
/* 460 */       return true;
/*     */     } 
/*     */     
/* 463 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     RabbitGroupData rabbitGroupData;
/* 468 */     Variant variant = getRandomRabbitVariant(level, blockPosition());
/* 469 */     if (groupData instanceof RabbitGroupData) {
/*     */       
/* 471 */       variant = ((RabbitGroupData)groupData).variant;
/*     */     } else {
/* 473 */       rabbitGroupData = new RabbitGroupData(variant);
/*     */     } 
/*     */     
/* 476 */     setVariant(variant);
/*     */     
/* 478 */     return super.finalizeSpawn(level, difficulty, spawnReason, rabbitGroupData);
/*     */   }
/*     */   
/*     */   private static Variant getRandomRabbitVariant(LevelAccessor level, BlockPos pos) {
/* 482 */     Holder<Biome> biome = level.getBiome(pos);
/*     */     
/* 484 */     int randomVal = level.getRandom().nextInt(100);
/* 485 */     if (biome.is(BiomeTags.SPAWNS_WHITE_RABBITS)) {
/* 486 */       return (randomVal < 80) ? Variant.WHITE : Variant.WHITE_SPLOTCHED;
/*     */     }
/*     */     
/* 489 */     if (biome.is(BiomeTags.SPAWNS_GOLD_RABBITS)) {
/* 490 */       return Variant.GOLD;
/*     */     }
/*     */     
/* 493 */     return (randomVal < 50) ? Variant.BROWN : ((randomVal < 90) ? Variant.SALT : Variant.BLACK);
/*     */   }
/*     */   
/*     */   public static boolean checkRabbitSpawnRules(EntityType<Rabbit> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 497 */     return (level.getBlockState(pos.below()).is(BlockTags.RABBITS_SPAWNABLE_ON) && 
/* 498 */       isBrightEnoughToSpawn(level, pos));
/*     */   }
/*     */   
/*     */   public static class RabbitGroupData extends AgeableMob.AgeableMobGroupData {
/*     */     public final Rabbit.Variant variant;
/*     */     
/*     */     public RabbitGroupData(Rabbit.Variant variant) {
/* 505 */       super(1.0F);
/* 506 */       this.variant = variant;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 511 */   private boolean wantsMoreFood() { return (this.moreCarrotTicks <= 0); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 516 */     if (id == 1) {
/* 517 */       spawnSprintParticle();
/* 518 */       this.jumpDuration = 10;
/* 519 */       this.jumpTicks = 0;
/*     */     } else {
/* 521 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 527 */   public Vec3 getLeashOffset() { return new Vec3(0.0D, (0.6F * getEyeHeight()), (getBbWidth() * 0.4F)); }
/*     */   
/*     */   public static class RabbitJumpControl
/*     */     extends JumpControl {
/*     */     private final Rabbit rabbit;
/*     */     private boolean canJump;
/*     */     
/*     */     public RabbitJumpControl(Rabbit rabbit) {
/* 535 */       super(rabbit);
/* 536 */       this.rabbit = rabbit;
/*     */     }
/*     */ 
/*     */     
/* 540 */     public boolean wantJump() { return this.jump; }
/*     */ 
/*     */ 
/*     */     
/* 544 */     public boolean canJump() { return this.canJump; }
/*     */ 
/*     */ 
/*     */     
/* 548 */     public void setCanJump(boolean canJump) { this.canJump = canJump; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 553 */       if (this.jump) {
/* 554 */         this.rabbit.startJumping();
/* 555 */         this.jump = false;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class RabbitMoveControl extends MoveControl {
/*     */     private final Rabbit rabbit;
/*     */     private double nextJumpSpeed;
/*     */     
/*     */     public RabbitMoveControl(Rabbit rabbit) {
/* 565 */       super(rabbit);
/* 566 */       this.rabbit = rabbit;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 571 */       if (this.rabbit.onGround() && !this.rabbit.jumping && !((Rabbit.RabbitJumpControl)this.rabbit.jumpControl).wantJump()) {
/* 572 */         this.rabbit.setSpeedModifier(0.0D);
/* 573 */       } else if (hasWanted() || this.operation == MoveControl.Operation.JUMPING) {
/* 574 */         this.rabbit.setSpeedModifier(this.nextJumpSpeed);
/*     */       } 
/* 576 */       super.tick();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setWantedPosition(double x, double y, double z, double speedModifier) {
/* 581 */       if (this.rabbit.isInWater()) {
/* 582 */         speedModifier = 1.5D;
/*     */       }
/*     */       
/* 585 */       super.setWantedPosition(x, y, z, speedModifier);
/* 586 */       if (speedModifier > 0.0D)
/* 587 */         this.nextJumpSpeed = speedModifier; 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class RabbitAvoidEntityGoal<T extends LivingEntity>
/*     */     extends AvoidEntityGoal<T> {
/*     */     private final Rabbit rabbit;
/*     */     
/*     */     public RabbitAvoidEntityGoal(Rabbit rabbit, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
/* 596 */       super(rabbit, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier);
/* 597 */       this.rabbit = rabbit;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 602 */     public boolean canUse() { return (this.rabbit.getVariant() != Rabbit.Variant.EVIL && super.canUse()); }
/*     */   }
/*     */   
/*     */   private static class RaidGardenGoal
/*     */     extends MoveToBlockGoal
/*     */   {
/*     */     private final Rabbit rabbit;
/*     */     private boolean wantsToRaid;
/*     */     private boolean canRaid;
/*     */     
/*     */     public RaidGardenGoal(Rabbit rabbit) {
/* 613 */       super(rabbit, 0.699999988079071D, 16);
/* 614 */       this.rabbit = rabbit;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 619 */       if (this.nextStartTick <= 0) {
/* 620 */         if (!((Boolean)getServerLevel(this.rabbit).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 621 */           return false;
/*     */         }
/*     */ 
/*     */         
/* 625 */         this.canRaid = false;
/* 626 */         this.wantsToRaid = this.rabbit.wantsMoreFood();
/*     */       } 
/*     */       
/* 629 */       return super.canUse();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 634 */     public boolean canContinueToUse() { return (this.canRaid && super.canContinueToUse()); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 639 */       super.tick();
/*     */       
/* 641 */       this.rabbit.getLookControl().setLookAt(this.blockPos.getX() + 0.5D, (this.blockPos.getY() + 1), this.blockPos.getZ() + 0.5D, 10.0F, this.rabbit.getMaxHeadXRot());
/*     */       
/* 643 */       if (isReachedTarget()) {
/* 644 */         Level level = this.rabbit.level();
/* 645 */         BlockPos cropsPos = this.blockPos.above();
/*     */         
/* 647 */         BlockState blockState = level.getBlockState(cropsPos);
/* 648 */         Block block = blockState.getBlock();
/*     */         
/* 650 */         if (this.canRaid && block instanceof CarrotBlock) {
/* 651 */           int carrotAge = ((Integer)blockState.getValue(CarrotBlock.AGE)).intValue();
/* 652 */           if (carrotAge == 0) {
/* 653 */             level.setBlock(cropsPos, Blocks.AIR.defaultBlockState(), 2);
/* 654 */             level.destroyBlock(cropsPos, true, this.rabbit);
/*     */           } else {
/* 656 */             level.setBlock(cropsPos, (BlockState)blockState.setValue(CarrotBlock.AGE, Integer.valueOf(carrotAge - 1)), 2);
/* 657 */             level.gameEvent(GameEvent.BLOCK_CHANGE, cropsPos, GameEvent.Context.of(this.rabbit));
/* 658 */             level.levelEvent(2001, cropsPos, Block.getId(blockState));
/*     */           } 
/* 660 */           this.rabbit.moreCarrotTicks = 40;
/*     */         } 
/*     */         
/* 663 */         this.canRaid = false;
/*     */ 
/*     */         
/* 666 */         this.nextStartTick = 10;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 672 */       BlockState state = level.getBlockState(pos);
/*     */       
/* 674 */       if (state.is(Blocks.FARMLAND) && this.wantsToRaid && !this.canRaid) {
/* 675 */         state = level.getBlockState(pos.above());
/*     */         
/* 677 */         if (state.getBlock() instanceof CarrotBlock && ((CarrotBlock)state.getBlock()).isMaxAge(state)) {
/* 678 */           this.canRaid = true;
/* 679 */           return true;
/*     */         } 
/*     */       } 
/* 682 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class RabbitPanicGoal extends PanicGoal {
/*     */     private final Rabbit rabbit;
/*     */     
/*     */     public RabbitPanicGoal(Rabbit rabbit, double speedModifier) {
/* 690 */       super(rabbit, speedModifier);
/* 691 */       this.rabbit = rabbit;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 696 */       super.tick();
/*     */       
/* 698 */       this.rabbit.setSpeedModifier(this.speedModifier);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\rabbit\Rabbit.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */