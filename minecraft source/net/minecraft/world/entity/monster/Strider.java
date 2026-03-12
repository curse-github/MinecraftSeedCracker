/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.ItemBasedSteering;
/*     */ import net.minecraft.world.entity.ItemSteerable;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.DismountHelper;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LiquidBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ 
/*     */ public class Strider
/*     */   extends Animal
/*     */   implements ItemSteerable
/*     */ {
/*  77 */   private static final Identifier SUFFOCATING_MODIFIER_ID = Identifier.withDefaultNamespace("suffocating");
/*  78 */   private static final AttributeModifier SUFFOCATING_MODIFIER = new AttributeModifier(SUFFOCATING_MODIFIER_ID, -0.3400000035762787D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
/*     */   
/*     */   private static final float SUFFOCATE_STEERING_MODIFIER = 0.35F;
/*     */   private static final float STEERING_MODIFIER = 0.55F;
/*  82 */   private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(Strider.class, EntityDataSerializers.INT);
/*  83 */   private static final EntityDataAccessor<Boolean> DATA_SUFFOCATING = SynchedEntityData.defineId(Strider.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private final ItemBasedSteering steering;
/*     */   
/*     */   private TemptGoal temptGoal;
/*     */   
/*     */   public Strider(EntityType<? extends Strider> strider, Level level) {
/*  90 */     super(strider, level);
/*  91 */     this.steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME);
/*     */     
/*  93 */     this.blocksBuilding = true;
/*     */     
/*  95 */     setPathfindingMalus(PathType.WATER, -1.0F);
/*  96 */     setPathfindingMalus(PathType.LAVA, 0.0F);
/*  97 */     setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
/*  98 */     setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
/*     */   }
/*     */   
/*     */   public static boolean checkStriderSpawnRules(EntityType<Strider> ignoredType, LevelAccessor level, EntitySpawnReason ignoredSpawnType, BlockPos pos, RandomSource ignoredRandom) {
/* 102 */     BlockPos.MutableBlockPos checkPos = pos.mutable();
/*     */     do {
/* 104 */       checkPos.move(Direction.UP);
/* 105 */     } while (level.getFluidState(checkPos).is(FluidTags.LAVA));
/*     */     
/* 107 */     return level.getBlockState(checkPos).isAir();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 112 */     if (DATA_BOOST_TIME.equals(accessor) && level().isClientSide()) {
/* 113 */       this.steering.onSynced();
/*     */     }
/* 115 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 120 */     super.defineSynchedData(entityData);
/* 121 */     entityData.define(DATA_BOOST_TIME, Integer.valueOf(0));
/* 122 */     entityData.define(DATA_SUFFOCATING, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUseSlot(EquipmentSlot slot) {
/* 127 */     if (slot == EquipmentSlot.SADDLE) {
/* 128 */       return (isAlive() && !isBaby());
/*     */     }
/* 130 */     return super.canUseSlot(slot);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 135 */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) { return (slot == EquipmentSlot.SADDLE || super.canDispenserEquipIntoSlot(slot)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Holder<SoundEvent> getEquipSound(EquipmentSlot slot, ItemStack stack, Equippable equippable) {
/* 140 */     if (slot == EquipmentSlot.SADDLE) {
/* 141 */       return SoundEvents.STRIDER_SADDLE;
/*     */     }
/* 143 */     return super.getEquipSound(slot, stack, equippable);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 148 */     this.goalSelector.addGoal(1, new PanicGoal(this, 1.65D));
/* 149 */     this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
/* 150 */     this.temptGoal = new TemptGoal(this, 1.4D, i -> i.is(ItemTags.STRIDER_TEMPT_ITEMS), false);
/* 151 */     this.goalSelector.addGoal(3, this.temptGoal);
/* 152 */     this.goalSelector.addGoal(4, new StriderGoToLavaGoal(this, 1.0D));
/* 153 */     this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.0D));
/* 154 */     this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D, 60));
/* 155 */     this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
/* 156 */     this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
/* 157 */     this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Strider.class, 8.0F));
/*     */   }
/*     */   
/*     */   public void setSuffocating(boolean flag) {
/* 161 */     this.entityData.set(DATA_SUFFOCATING, Boolean.valueOf(flag));
/*     */     
/* 163 */     AttributeInstance attribute = getAttribute(Attributes.MOVEMENT_SPEED);
/* 164 */     if (attribute != null) {
/* 165 */       if (flag) {
/* 166 */         attribute.addOrUpdateTransientModifier(SUFFOCATING_MODIFIER);
/*     */       } else {
/* 168 */         attribute.removeModifier(SUFFOCATING_MODIFIER_ID);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 174 */   public boolean isSuffocating() { return ((Boolean)this.entityData.get(DATA_SUFFOCATING)).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   public boolean canStandOnFluid(FluidState fluid) { return fluid.is(FluidTags.LAVA); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
/* 184 */     if (!level().isClientSide()) {
/* 185 */       return super.getPassengerAttachmentPoint(passenger, dimensions, scale);
/*     */     }
/*     */     
/* 188 */     float animSpeed = Math.min(0.25F, this.walkAnimation.speed());
/* 189 */     float animPos = this.walkAnimation.position();
/*     */     
/* 191 */     float offset = 0.12F * Mth.cos((animPos * 1.5F)) * 2.0F * animSpeed;
/* 192 */     return super.getPassengerAttachmentPoint(passenger, dimensions, scale).add(0.0D, (offset * scale), 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 197 */   public boolean checkSpawnObstruction(LevelReader level) { return level.isUnobstructed(this); }
/*     */ 
/*     */ 
/*     */   
/*     */   public LivingEntity getControllingPassenger() {
/* 202 */     if (isSaddled()) { Entity entity = getFirstPassenger(); if (entity instanceof Player) { Player player = (Player)entity; if (player.isHolding(Items.WARPED_FUNGUS_ON_A_STICK))
/* 203 */           return player;  }
/*     */        }
/* 205 */      return super.getControllingPassenger();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
/* 215 */     Vec3[] directions = { getCollisionHorizontalEscapeVector(getBbWidth(), passenger.getBbWidth(), passenger.getYRot()), getCollisionHorizontalEscapeVector(getBbWidth(), passenger.getBbWidth(), passenger.getYRot() - 22.5F), getCollisionHorizontalEscapeVector(getBbWidth(), passenger.getBbWidth(), passenger.getYRot() + 22.5F), getCollisionHorizontalEscapeVector(getBbWidth(), passenger.getBbWidth(), passenger.getYRot() - 45.0F), getCollisionHorizontalEscapeVector(getBbWidth(), passenger.getBbWidth(), passenger.getYRot() + 45.0F) };
/*     */ 
/*     */     
/* 218 */     Set<BlockPos> targetBlockPositions = Sets.newLinkedHashSet();
/* 219 */     double colliderTop = (getBoundingBox()).maxY;
/* 220 */     double colliderBottom = (getBoundingBox()).minY - 0.5D;
/*     */     
/* 222 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 223 */     for (Vec3 direction : directions) {
/* 224 */       blockPos.set(getX() + direction.x, colliderTop, getZ() + direction.z);
/*     */       
/*     */       double y;
/* 227 */       for (y = colliderTop; y > colliderBottom; y--) {
/* 228 */         targetBlockPositions.add(blockPos.immutable());
/* 229 */         blockPos.move(Direction.DOWN);
/*     */       } 
/*     */     } 
/*     */     
/* 233 */     for (BlockPos targetBlockPos : targetBlockPositions) {
/* 234 */       if (level().getFluidState(targetBlockPos).is(FluidTags.LAVA)) {
/*     */         continue;
/*     */       }
/*     */       
/* 238 */       double blockFloorHeight = level().getBlockFloorHeight(targetBlockPos);
/* 239 */       if (DismountHelper.isBlockFloorValid(blockFloorHeight)) {
/* 240 */         Vec3 location = Vec3.upFromBottomCenterOf(targetBlockPos, blockFloorHeight);
/*     */         
/* 242 */         for (UnmodifiableIterator unmodifiableIterator = passenger.getDismountPoses().iterator(); unmodifiableIterator.hasNext(); ) { Pose dismountPose = (Pose)unmodifiableIterator.next();
/* 243 */           AABB poseCollisionBox = passenger.getLocalBoundsForPose(dismountPose);
/*     */           
/* 245 */           if (DismountHelper.canDismountTo(level(), passenger, poseCollisionBox.move(location))) {
/* 246 */             passenger.setPose(dismountPose);
/* 247 */             return location;
/*     */           }  }
/*     */       
/*     */       } 
/*     */     } 
/*     */     
/* 253 */     return new Vec3(getX(), (getBoundingBox()).maxY, getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tickRidden(Player controller, Vec3 riddenInput) {
/* 258 */     setRot(controller.getYRot(), controller.getXRot() * 0.5F);
/* 259 */     this.yRotO = this.yBodyRot = this.yHeadRot = getYRot();
/* 260 */     this.steering.tickBoost();
/* 261 */     super.tickRidden(controller, riddenInput);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 266 */   protected Vec3 getRiddenInput(Player controller, Vec3 selfInput) { return new Vec3(0.0D, 0.0D, 1.0D); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 271 */   protected float getRiddenSpeed(Player controller) { return (float)(getAttributeValue(Attributes.MOVEMENT_SPEED) * (isSuffocating() ? 0.35F : 0.55F) * this.steering.boostFactor()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 276 */   protected float nextStep() { return this.moveDist + 0.6F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 281 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(isInLava() ? SoundEvents.STRIDER_STEP_LAVA : SoundEvents.STRIDER_STEP, 1.0F, 1.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 286 */   public boolean boost() { return this.steering.boost(getRandom()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
/* 291 */     if (isInLava()) {
/* 292 */       resetFallDistance();
/*     */       
/*     */       return;
/*     */     } 
/* 296 */     super.checkFallDamage(ya, onGround, onState, pos);
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
/*     */   public void tick() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual isBeingTempted : ()Z
/*     */     //   4: ifeq -> 32
/*     */     //   7: aload_0
/*     */     //   8: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   11: sipush #140
/*     */     //   14: invokeinterface nextInt : (I)I
/*     */     //   19: ifne -> 32
/*     */     //   22: aload_0
/*     */     //   23: getstatic net/minecraft/sounds/SoundEvents.STRIDER_HAPPY : Lnet/minecraft/sounds/SoundEvent;
/*     */     //   26: invokevirtual makeSound : (Lnet/minecraft/sounds/SoundEvent;)V
/*     */     //   29: goto -> 60
/*     */     //   32: aload_0
/*     */     //   33: invokevirtual isPanicking : ()Z
/*     */     //   36: ifeq -> 60
/*     */     //   39: aload_0
/*     */     //   40: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   43: bipush #60
/*     */     //   45: invokeinterface nextInt : (I)I
/*     */     //   50: ifne -> 60
/*     */     //   53: aload_0
/*     */     //   54: getstatic net/minecraft/sounds/SoundEvents.STRIDER_RETREAT : Lnet/minecraft/sounds/SoundEvent;
/*     */     //   57: invokevirtual makeSound : (Lnet/minecraft/sounds/SoundEvent;)V
/*     */     //   60: aload_0
/*     */     //   61: invokevirtual isNoAi : ()Z
/*     */     //   64: ifne -> 176
/*     */     //   67: aload_0
/*     */     //   68: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   71: aload_0
/*     */     //   72: invokevirtual blockPosition : ()Lnet/minecraft/core/BlockPos;
/*     */     //   75: invokevirtual getBlockState : (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   78: astore_1
/*     */     //   79: aload_0
/*     */     //   80: invokevirtual getBlockStateOnLegacy : ()Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   83: astore_2
/*     */     //   84: aload_1
/*     */     //   85: getstatic net/minecraft/tags/BlockTags.STRIDER_WARM_BLOCKS : Lnet/minecraft/tags/TagKey;
/*     */     //   88: invokevirtual is : (Lnet/minecraft/tags/TagKey;)Z
/*     */     //   91: ifne -> 116
/*     */     //   94: aload_2
/*     */     //   95: getstatic net/minecraft/tags/BlockTags.STRIDER_WARM_BLOCKS : Lnet/minecraft/tags/TagKey;
/*     */     //   98: invokevirtual is : (Lnet/minecraft/tags/TagKey;)Z
/*     */     //   101: ifne -> 116
/*     */     //   104: aload_0
/*     */     //   105: getstatic net/minecraft/tags/FluidTags.LAVA : Lnet/minecraft/tags/TagKey;
/*     */     //   108: invokevirtual getFluidHeight : (Lnet/minecraft/tags/TagKey;)D
/*     */     //   111: dconst_0
/*     */     //   112: dcmpl
/*     */     //   113: ifle -> 120
/*     */     //   116: iconst_1
/*     */     //   117: goto -> 121
/*     */     //   120: iconst_0
/*     */     //   121: istore_3
/*     */     //   122: aload_0
/*     */     //   123: invokevirtual getVehicle : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   126: astore #6
/*     */     //   128: aload #6
/*     */     //   130: instanceof net/minecraft/world/entity/monster/Strider
/*     */     //   133: ifeq -> 155
/*     */     //   136: aload #6
/*     */     //   138: checkcast net/minecraft/world/entity/monster/Strider
/*     */     //   141: astore #5
/*     */     //   143: aload #5
/*     */     //   145: invokevirtual isSuffocating : ()Z
/*     */     //   148: ifeq -> 155
/*     */     //   151: iconst_1
/*     */     //   152: goto -> 156
/*     */     //   155: iconst_0
/*     */     //   156: istore #4
/*     */     //   158: aload_0
/*     */     //   159: iload_3
/*     */     //   160: ifeq -> 168
/*     */     //   163: iload #4
/*     */     //   165: ifeq -> 172
/*     */     //   168: iconst_1
/*     */     //   169: goto -> 173
/*     */     //   172: iconst_0
/*     */     //   173: invokevirtual setSuffocating : (Z)V
/*     */     //   176: aload_0
/*     */     //   177: invokespecial tick : ()V
/*     */     //   180: aload_0
/*     */     //   181: invokevirtual floatStrider : ()V
/*     */     //   184: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #301	-> 0
/*     */     //   #302	-> 22
/*     */     //   #303	-> 32
/*     */     //   #304	-> 53
/*     */     //   #307	-> 60
/*     */     //   #308	-> 67
/*     */     //   #309	-> 79
/*     */     //   #311	-> 84
/*     */     //   #312	-> 122
/*     */     //   #315	-> 158
/*     */     //   #318	-> 176
/*     */     //   #319	-> 180
/*     */     //   #320	-> 184
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   143	12	5	strider	Lnet/minecraft/world/entity/monster/Strider;
/*     */     //   79	97	1	stateInside	Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   84	92	2	stateOn	Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   122	54	3	inWarmBlocks	Z
/*     */     //   158	18	4	vehicleSuffocating	Z
/*     */     //   0	185	0	this	Lnet/minecraft/world/entity/monster/Strider; }
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
/* 323 */   private boolean isBeingTempted() { return (this.temptGoal != null && this.temptGoal.isRunning()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 328 */   protected boolean shouldPassengersInheritMalus() { return true; }
/*     */ 
/*     */   
/*     */   private void floatStrider() {
/* 332 */     if (isInLava()) {
/* 333 */       CollisionContext context = CollisionContext.of(this);
/* 334 */       if (!context.isAbove(LiquidBlock.SHAPE_STABLE, blockPosition(), true) || level().getFluidState(blockPosition().above()).is(FluidTags.LAVA)) {
/* 335 */         setDeltaMovement(getDeltaMovement().scale(0.5D).add(0.0D, 0.05D, 0.0D));
/*     */       } else {
/* 337 */         setOnGround(true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 343 */     return Animal.createAnimalAttributes()
/* 344 */       .add(Attributes.MOVEMENT_SPEED, 0.17499999701976776D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 349 */     if (isPanicking() || isBeingTempted()) {
/* 350 */       return null;
/*     */     }
/* 352 */     return SoundEvents.STRIDER_AMBIENT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 357 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.STRIDER_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 362 */   protected SoundEvent getDeathSound() { return SoundEvents.STRIDER_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 367 */   protected boolean canAddPassenger(Entity passenger) { return (!isVehicle() && !isEyeInFluid(FluidTags.LAVA)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 372 */   public boolean isSensitiveToWater() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 377 */   public boolean isOnFire() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 382 */   protected PathNavigation createNavigation(Level level) { return new StriderPathNavigation(this, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/* 387 */     if (level.getBlockState(pos).getFluidState().is(FluidTags.LAVA)) {
/* 388 */       return 10.0F;
/*     */     }
/*     */ 
/*     */     
/* 392 */     return isInLava() ? Float.NEGATIVE_INFINITY : 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 397 */   public Strider getBreedOffspring(ServerLevel level, AgeableMob partner) { return (Strider)EntityType.STRIDER.create(level, EntitySpawnReason.BREEDING); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 402 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.STRIDER_FOOD); }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 407 */     boolean hasFood = isFood(player.getItemInHand(hand));
/*     */     
/* 409 */     if (!hasFood && isSaddled() && !isVehicle() && !player.isSecondaryUseActive()) {
/* 410 */       if (!level().isClientSide()) {
/* 411 */         player.startRiding(this);
/*     */       }
/* 413 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 416 */     InteractionResult interactionResult = super.mobInteract(player, hand);
/* 417 */     if (!interactionResult.consumesAction()) {
/* 418 */       ItemStack itemStack = player.getItemInHand(hand);
/* 419 */       if (isEquippableInSlot(itemStack, EquipmentSlot.SADDLE)) {
/* 420 */         return itemStack.interactLivingEntity(player, this, hand);
/*     */       }
/* 422 */       return InteractionResult.PASS;
/* 423 */     }  if (hasFood && !isSilent()) {
/* 424 */       level().playSound(null, getX(), getY(), getZ(), SoundEvents.STRIDER_EAT, getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
/*     */     }
/*     */     
/* 427 */     return interactionResult;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 432 */   public Vec3 getLeashOffset() { return new Vec3(0.0D, (0.6F * getEyeHeight()), (getBbWidth() * 0.4F)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/* 438 */     if (isBaby()) {
/* 439 */       return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */     }
/*     */     
/* 442 */     RandomSource random = level.getRandom();
/* 443 */     if (random.nextInt(30) == 0) {
/* 444 */       Mob jockey = (Mob)EntityType.ZOMBIFIED_PIGLIN.create(level.getLevel(), EntitySpawnReason.JOCKEY);
/* 445 */       if (jockey != null) {
/* 446 */         groupData = spawnJockey(level, difficulty, jockey, new Zombie.ZombieGroupData(Zombie.getSpawnAsBabyOdds(random), false));
/*     */         
/* 448 */         jockey.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.WARPED_FUNGUS_ON_A_STICK));
/* 449 */         setItemSlot(EquipmentSlot.SADDLE, new ItemStack(Items.SADDLE));
/* 450 */         setGuaranteedDrop(EquipmentSlot.SADDLE);
/*     */       } 
/* 452 */     } else if (random.nextInt(10) == 0) {
/* 453 */       AgeableMob jockey = (AgeableMob)EntityType.STRIDER.create(level.getLevel(), EntitySpawnReason.JOCKEY);
/* 454 */       if (jockey != null) {
/* 455 */         jockey.setAge(-24000);
/*     */         
/* 457 */         groupData = spawnJockey(level, difficulty, jockey, null);
/*     */       } 
/*     */     } else {
/* 460 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(0.5F);
/*     */     } 
/*     */     
/* 463 */     return super.finalizeSpawn(level, difficulty, spawnReason, ageableMobGroupData);
/*     */   }
/*     */   
/*     */   private SpawnGroupData spawnJockey(ServerLevelAccessor level, DifficultyInstance difficulty, Mob jockey, SpawnGroupData jockeyGroupData) {
/* 467 */     jockey.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/* 468 */     jockey.finalizeSpawn(level, difficulty, EntitySpawnReason.JOCKEY, jockeyGroupData);
/* 469 */     jockey.startRiding(this, true, false);
/*     */     
/* 471 */     return new AgeableMob.AgeableMobGroupData(0.0F);
/*     */   }
/*     */   
/*     */   private static class StriderPathNavigation
/*     */     extends GroundPathNavigation {
/* 476 */     StriderPathNavigation(Strider mob, Level level) { super(mob, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected PathFinder createPathFinder(int maxVisitedNodes) {
/* 482 */       this.nodeEvaluator = new WalkNodeEvaluator();
/* 483 */       return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean hasValidPathType(PathType pathType) {
/* 488 */       if (pathType == PathType.LAVA || pathType == PathType.DAMAGE_FIRE || pathType == PathType.DANGER_FIRE) {
/* 489 */         return true;
/*     */       }
/*     */       
/* 492 */       return super.hasValidPathType(pathType);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 497 */     public boolean isStableDestination(BlockPos pos) { return (this.level.getBlockState(pos).is(Blocks.LAVA) || super.isStableDestination(pos)); }
/*     */   }
/*     */   
/*     */   private static class StriderGoToLavaGoal
/*     */     extends MoveToBlockGoal {
/*     */     private final Strider strider;
/*     */     
/*     */     private StriderGoToLavaGoal(Strider strider, double speedModifier) {
/* 505 */       super(strider, speedModifier, 8, 2);
/* 506 */       this.strider = strider;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 511 */     public BlockPos getMoveToTarget() { return this.blockPos; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 516 */     public boolean canContinueToUse() { return (!this.strider.isInLava() && isValidTarget(this.strider.level(), this.blockPos)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 521 */     public boolean canUse() { return (!this.strider.isInLava() && super.canUse()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 526 */     public boolean shouldRecalculatePath() { return (this.tryTicks % 20 == 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 531 */     protected boolean isValidTarget(LevelReader level, BlockPos pos) { return (level.getBlockState(pos).is(Blocks.LAVA) && level.getBlockState(pos.above()).isPathfindable(PathComputationType.LAND)); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Strider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */