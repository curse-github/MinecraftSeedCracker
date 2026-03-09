/*      */ package net.minecraft.world.entity.animal.fox;
/*      */ 
/*      */ import com.mojang.serialization.Codec;
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Optional;
/*      */ import java.util.function.IntFunction;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.advancements.CriteriaTriggers;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.component.DataComponentGetter;
/*      */ import net.minecraft.core.component.DataComponentType;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.particles.ItemParticleOption;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.network.codec.ByteBufCodecs;
/*      */ import net.minecraft.network.codec.StreamCodec;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.tags.BiomeTags;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.util.ByIdMap;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.StringRepresentable;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.AgeableMob;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityDimensions;
/*      */ import net.minecraft.world.entity.EntityReference;
/*      */ import net.minecraft.world.entity.EntitySelector;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.ExperienceOrb;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.Pose;
/*      */ import net.minecraft.world.entity.SpawnGroupData;
/*      */ import net.minecraft.world.entity.TamableAnimal;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.control.LookControl;
/*      */ import net.minecraft.world.entity.ai.control.MoveControl;
/*      */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*      */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*      */ import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FleeSunGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.ai.goal.JumpGoal;
/*      */ import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
/*      */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*      */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*      */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*      */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*      */ import net.minecraft.world.entity.ai.goal.StrollThroughVillageGoal;
/*      */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*      */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*      */ import net.minecraft.world.entity.animal.Animal;
/*      */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*      */ import net.minecraft.world.entity.animal.wolf.Wolf;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelAccessor;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.biome.Biome;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.CaveVines;
/*      */ import net.minecraft.world.level.block.SweetBerryBushBlock;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.pathfinder.PathType;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Fox
/*      */   extends Animal
/*      */ {
/*  110 */   private static final EntityDataAccessor<Integer> DATA_TYPE_ID = SynchedEntityData.defineId(Fox.class, EntityDataSerializers.INT);
/*  111 */   private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Fox.class, EntityDataSerializers.BYTE);
/*      */   
/*      */   private static final int FLAG_SITTING = 1;
/*      */   
/*      */   public static final int FLAG_CROUCHING = 4;
/*      */   public static final int FLAG_INTERESTED = 8;
/*      */   public static final int FLAG_POUNCING = 16;
/*      */   private static final int FLAG_SLEEPING = 32;
/*      */   private static final int FLAG_FACEPLANTED = 64;
/*      */   private static final int FLAG_DEFENDING = 128;
/*  121 */   private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_TRUSTED_ID_0 = SynchedEntityData.defineId(Fox.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
/*  122 */   private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_TRUSTED_ID_1 = SynchedEntityData.defineId(Fox.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
/*      */   
/*  124 */   private static final Predicate<ItemEntity> ALLOWED_ITEMS = e -> (!e.hasPickUpDelay() && e.isAlive());
/*      */   
/*      */   private static final Predicate<Entity> TRUSTED_TARGET_SELECTOR = entity -> {
/*  127 */       if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/*  128 */         return (livingEntity.getLastHurtMob() != null && livingEntity.getLastHurtMobTimestamp() < livingEntity.tickCount + 600); }
/*      */       
/*  130 */       return false;
/*      */     };
/*      */   
/*  133 */   private static final Predicate<Entity> STALKABLE_PREY = entity -> (entity instanceof net.minecraft.world.entity.animal.chicken.Chicken || entity instanceof net.minecraft.world.entity.animal.rabbit.Rabbit);
/*      */   
/*  135 */   private static final Predicate<Entity> AVOID_PLAYERS = entity -> (!entity.isDiscrete() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity));
/*      */   
/*      */   private static final int MIN_TICKS_BEFORE_EAT = 600;
/*      */   
/*  139 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.FOX.getDimensions().scale(0.5F).withEyeHeight(0.2975F);
/*      */   
/*  141 */   private static final Codec<List<EntityReference<LivingEntity>>> TRUSTED_LIST_CODEC = EntityReference.codec().listOf();
/*      */   
/*      */   private static final boolean DEFAULT_SLEEPING = false;
/*      */   private static final boolean DEFAULT_SITTING = false;
/*      */   private static final boolean DEFAULT_CROUCHING = false;
/*      */   private Goal landTargetGoal;
/*      */   private Goal turtleEggTargetGoal;
/*      */   private Goal fishTargetGoal;
/*      */   private float interestedAngle;
/*      */   private float interestedAngleO;
/*      */   private float crouchAmount;
/*      */   private float crouchAmountO;
/*      */   private int ticksSinceEaten;
/*      */   
/*      */   public enum Variant
/*      */     implements StringRepresentable
/*      */   {
/*      */     public static final Variant DEFAULT;
/*      */     public static final StringRepresentable.EnumCodec<Variant> CODEC;
/*      */     private static final IntFunction<Variant> BY_ID;
/*  161 */     RED(0, "red"),
/*  162 */     SNOW(1, "snow");
/*      */     static  {
/*  164 */       DEFAULT = RED;
/*      */       
/*  166 */       CODEC = StringRepresentable.fromEnum(Variant::values);
/*      */       
/*  168 */       BY_ID = ByIdMap.continuous(Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*      */       
/*  170 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::getId);
/*      */     }
/*      */     
/*      */     public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC;
/*      */     
/*      */     Variant(int id, String name) {
/*  176 */       this.id = id;
/*  177 */       this.name = name;
/*      */     }
/*      */     private final int id;
/*      */     private final String name;
/*      */     
/*  182 */     public String getSerializedName() { return this.name; }
/*      */ 
/*      */ 
/*      */     
/*  186 */     public int getId() { return this.id; }
/*      */ 
/*      */ 
/*      */     
/*  190 */     public static Variant byId(int id) { return (Variant)BY_ID.apply(id); }
/*      */ 
/*      */ 
/*      */     
/*  194 */     public static Variant byBiome(Holder<Biome> biome) { return biome.is(BiomeTags.SPAWNS_SNOW_FOXES) ? SNOW : RED; }
/*      */   }
/*      */ 
/*      */   
/*      */   public Fox(EntityType<? extends Fox> type, Level level) {
/*  199 */     super(type, level);
/*      */     
/*  201 */     this.lookControl = new FoxLookControl();
/*  202 */     this.moveControl = new FoxMoveControl();
/*      */     
/*  204 */     setPathfindingMalus(PathType.DANGER_OTHER, 0.0F);
/*  205 */     setPathfindingMalus(PathType.DAMAGE_OTHER, 0.0F);
/*      */     
/*  207 */     setCanPickUpLoot(true);
/*      */     
/*  209 */     getNavigation().setRequiredPathLength(32.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  214 */     super.defineSynchedData(entityData);
/*  215 */     entityData.define(DATA_TRUSTED_ID_0, Optional.empty());
/*  216 */     entityData.define(DATA_TRUSTED_ID_1, Optional.empty());
/*  217 */     entityData.define(DATA_TYPE_ID, Integer.valueOf(Variant.DEFAULT.getId()));
/*  218 */     entityData.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void registerGoals() {
/*  223 */     this.landTargetGoal = new NearestAttackableTargetGoal(this, Animal.class, 10, false, false, (target, level) -> (target instanceof net.minecraft.world.entity.animal.chicken.Chicken || target instanceof net.minecraft.world.entity.animal.rabbit.Rabbit));
/*  224 */     this.turtleEggTargetGoal = new NearestAttackableTargetGoal(this, Turtle.class, 10, false, false, Turtle.BABY_ON_LAND_SELECTOR);
/*  225 */     this.fishTargetGoal = new NearestAttackableTargetGoal(this, net.minecraft.world.entity.animal.fish.AbstractFish.class, 20, false, false, (target, level) -> target instanceof net.minecraft.world.entity.animal.fish.AbstractSchoolingFish);
/*      */     
/*  227 */     this.goalSelector.addGoal(0, new FoxFloatGoal());
/*  228 */     this.goalSelector.addGoal(0, new ClimbOnTopOfPowderSnowGoal(this, level()));
/*  229 */     this.goalSelector.addGoal(1, new FaceplantGoal());
/*  230 */     this.goalSelector.addGoal(2, new FoxPanicGoal(2.2D));
/*  231 */     this.goalSelector.addGoal(3, new FoxBreedGoal(this, 1.0D));
/*  232 */     this.goalSelector.addGoal(4, new AvoidEntityGoal(this, Player.class, 16.0F, 1.6D, 1.4D, entity -> (AVOID_PLAYERS.test(entity) && !trusts(entity) && !isDefending())));
/*  233 */     this.goalSelector.addGoal(4, new AvoidEntityGoal(this, Wolf.class, 8.0F, 1.6D, 1.4D, entity -> (!((Wolf)entity).isTame() && !isDefending())));
/*  234 */     this.goalSelector.addGoal(4, new AvoidEntityGoal(this, net.minecraft.world.entity.animal.polarbear.PolarBear.class, 8.0F, 1.6D, 1.4D, entity -> !isDefending()));
/*  235 */     this.goalSelector.addGoal(5, new StalkPreyGoal());
/*  236 */     this.goalSelector.addGoal(6, new FoxPounceGoal());
/*  237 */     this.goalSelector.addGoal(6, new SeekShelterGoal(1.25D));
/*  238 */     this.goalSelector.addGoal(7, new FoxMeleeAttackGoal(1.2000000476837158D, true));
/*  239 */     this.goalSelector.addGoal(7, new SleepGoal());
/*  240 */     this.goalSelector.addGoal(8, new FoxFollowParentGoal(this, 1.25D));
/*  241 */     this.goalSelector.addGoal(9, new FoxStrollThroughVillageGoal(32, 200));
/*  242 */     this.goalSelector.addGoal(10, new FoxEatBerriesGoal(1.2000000476837158D, 12, 1));
/*  243 */     this.goalSelector.addGoal(10, new LeapAtTargetGoal(this, 0.4F));
/*  244 */     this.goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 1.0D));
/*  245 */     this.goalSelector.addGoal(11, new FoxSearchForItemsGoal());
/*  246 */     this.goalSelector.addGoal(12, new FoxLookAtPlayerGoal(this, Player.class, 24.0F));
/*  247 */     this.goalSelector.addGoal(13, new PerchAndSearchGoal());
/*      */     
/*  249 */     this.targetSelector.addGoal(3, new DefendTrustedTargetGoal(LivingEntity.class, false, false, (target, level) -> (TRUSTED_TARGET_SELECTOR.test(target) && !trusts(target))));
/*      */   }
/*      */ 
/*      */   
/*      */   public void aiStep() {
/*  254 */     if (!level().isClientSide() && isAlive() && isEffectiveAi()) {
/*      */       
/*  256 */       this.ticksSinceEaten++;
/*  257 */       ItemStack itemInMouth = getItemBySlot(EquipmentSlot.MAINHAND);
/*  258 */       if (canEat(itemInMouth)) {
/*  259 */         if (this.ticksSinceEaten > 600) {
/*  260 */           ItemStack remainingFood = itemInMouth.finishUsingItem(level(), this);
/*  261 */           if (!remainingFood.isEmpty()) {
/*  262 */             setItemSlot(EquipmentSlot.MAINHAND, remainingFood);
/*      */           }
/*  264 */           this.ticksSinceEaten = 0;
/*  265 */         } else if (this.ticksSinceEaten > 560 && 
/*  266 */           this.random.nextFloat() < 0.1F) {
/*  267 */           playEatingSound();
/*  268 */           level().broadcastEntityEvent(this, (byte)45);
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  274 */       LivingEntity target = getTarget();
/*  275 */       if (target == null || !target.isAlive()) {
/*  276 */         setIsCrouching(false);
/*  277 */         setIsInterested(false);
/*      */       } 
/*      */     } 
/*      */     
/*  281 */     if (isSleeping() || isImmobile()) {
/*  282 */       this.jumping = false;
/*  283 */       this.xxa = 0.0F;
/*  284 */       this.zza = 0.0F;
/*      */     } 
/*      */     
/*  287 */     super.aiStep();
/*      */     
/*  289 */     if (isDefending() && this.random.nextFloat() < 0.05F) {
/*  290 */       playSound(SoundEvents.FOX_AGGRO, 1.0F, 1.0F);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  296 */   protected boolean isImmobile() { return isDeadOrDying(); }
/*      */ 
/*      */ 
/*      */   
/*  300 */   private boolean canEat(ItemStack itemInMouth) { return (isConsumableFood(itemInMouth) && getTarget() == null && onGround() && !isSleeping()); }
/*      */ 
/*      */ 
/*      */   
/*  304 */   private boolean isConsumableFood(ItemStack itemStack) { return (itemStack.has(DataComponents.FOOD) && itemStack.has(DataComponents.CONSUMABLE)); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/*  309 */     if (random.nextFloat() < 0.2F) {
/*  310 */       ItemStack heldInMouth; float odds = random.nextFloat();
/*      */       
/*  312 */       if (odds < 0.05F) {
/*  313 */         heldInMouth = new ItemStack(Items.EMERALD);
/*  314 */       } else if (odds < 0.2F) {
/*  315 */         heldInMouth = new ItemStack(Items.EGG);
/*  316 */       } else if (odds < 0.4F) {
/*  317 */         heldInMouth = random.nextBoolean() ? new ItemStack(Items.RABBIT_FOOT) : new ItemStack(Items.RABBIT_HIDE);
/*  318 */       } else if (odds < 0.6F) {
/*  319 */         heldInMouth = new ItemStack(Items.WHEAT);
/*  320 */       } else if (odds < 0.8F) {
/*  321 */         heldInMouth = new ItemStack(Items.LEATHER);
/*      */       } else {
/*  323 */         heldInMouth = new ItemStack(Items.FEATHER);
/*      */       } 
/*  325 */       setItemSlot(EquipmentSlot.MAINHAND, heldInMouth);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEntityEvent(byte id) {
/*  331 */     if (id == 45) {
/*  332 */       ItemStack mouthItem = getItemBySlot(EquipmentSlot.MAINHAND);
/*  333 */       if (!mouthItem.isEmpty()) {
/*  334 */         for (int i = 0; i < 8; i++) {
/*      */ 
/*      */           
/*  337 */           Vec3 direction = (new Vec3((this.random.nextFloat() - 0.5D) * 0.1D, this.random.nextFloat() * 0.1D + 0.1D, 0.0D)).xRot(-getXRot() * 0.017453292F).yRot(-getYRot() * 0.017453292F);
/*      */           
/*  339 */           level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, mouthItem), getX() + (getLookAngle()).x / 2.0D, getY(), getZ() + (getLookAngle()).z / 2.0D, direction.x, direction.y + 0.05D, direction.z);
/*      */         } 
/*      */       }
/*      */     } else {
/*  343 */       super.handleEntityEvent(id);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createAttributes() {
/*  348 */     return Animal.createAnimalAttributes()
/*  349 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/*  350 */       .add(Attributes.MAX_HEALTH, 10.0D)
/*  351 */       .add(Attributes.ATTACK_DAMAGE, 2.0D)
/*  352 */       .add(Attributes.SAFE_FALL_DISTANCE, 5.0D)
/*  353 */       .add(Attributes.FOLLOW_RANGE, 32.0D);
/*      */   }
/*      */ 
/*      */   
/*      */   public Fox getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  358 */     Fox baby = (Fox)EntityType.FOX.create(level, EntitySpawnReason.BREEDING);
/*  359 */     if (baby != null) {
/*  360 */       baby.setVariant(this.random.nextBoolean() ? getVariant() : ((Fox)partner).getVariant());
/*      */     }
/*  362 */     return baby;
/*      */   }
/*      */   
/*      */   public static boolean checkFoxSpawnRules(EntityType<Fox> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/*  366 */     return (level.getBlockState(pos.below()).is(BlockTags.FOXES_SPAWNABLE_ON) && 
/*  367 */       isBrightEnoughToSpawn(level, pos));
/*      */   }
/*      */   
/*      */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*      */     FoxGroupData foxGroupData1;
/*  372 */     Holder<Biome> biome = level.getBiome(blockPosition());
/*  373 */     Variant variant = Variant.byBiome(biome);
/*  374 */     boolean isBaby = false;
/*  375 */     if (groupData instanceof FoxGroupData) { FoxGroupData foxGroupData = (FoxGroupData)groupData;
/*      */       
/*  377 */       variant = foxGroupData.variant;
/*  378 */       if (foxGroupData.getGroupSize() >= 2) {
/*  379 */         isBaby = true;
/*      */       } }
/*      */     else
/*  382 */     { foxGroupData1 = new FoxGroupData(variant); }
/*      */ 
/*      */     
/*  385 */     setVariant(variant);
/*  386 */     if (isBaby) {
/*  387 */       setAge(-24000);
/*      */     }
/*      */     
/*  390 */     if (level instanceof ServerLevel) {
/*  391 */       setTargetGoals();
/*      */     }
/*      */     
/*  394 */     populateDefaultEquipmentSlots(level.getRandom(), difficulty);
/*      */     
/*  396 */     return super.finalizeSpawn(level, difficulty, spawnReason, foxGroupData1);
/*      */   }
/*      */ 
/*      */   
/*      */   private void setTargetGoals() {
/*  401 */     if (getVariant() == Variant.RED) {
/*  402 */       this.targetSelector.addGoal(4, this.landTargetGoal);
/*  403 */       this.targetSelector.addGoal(4, this.turtleEggTargetGoal);
/*  404 */       this.targetSelector.addGoal(6, this.fishTargetGoal);
/*      */     } else {
/*  406 */       this.targetSelector.addGoal(4, this.fishTargetGoal);
/*  407 */       this.targetSelector.addGoal(6, this.landTargetGoal);
/*  408 */       this.targetSelector.addGoal(6, this.turtleEggTargetGoal);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  414 */   protected void playEatingSound() { playSound(SoundEvents.FOX_EAT, 1.0F, 1.0F); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  419 */   public EntityDimensions getDefaultDimensions(Pose pose) { return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose); }
/*      */ 
/*      */ 
/*      */   
/*  423 */   public Variant getVariant() { return Variant.byId(((Integer)this.entityData.get(DATA_TYPE_ID)).intValue()); }
/*      */ 
/*      */ 
/*      */   
/*  427 */   private void setVariant(Variant variant) { this.entityData.set(DATA_TYPE_ID, Integer.valueOf(variant.getId())); }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T get(DataComponentType<? extends T> type) {
/*  432 */     if (type == DataComponents.FOX_VARIANT) {
/*  433 */       return (T)castComponentValue(type, getVariant());
/*      */     }
/*      */     
/*  436 */     return (T)super.get(type);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void applyImplicitComponents(DataComponentGetter components) {
/*  441 */     applyImplicitComponentIfPresent(components, DataComponents.FOX_VARIANT);
/*  442 */     super.applyImplicitComponents(components);
/*      */   }
/*      */ 
/*      */   
/*      */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/*  447 */     if (type == DataComponents.FOX_VARIANT) {
/*  448 */       setVariant((Variant)castComponentValue(DataComponents.FOX_VARIANT, value));
/*  449 */       return true;
/*      */     } 
/*      */     
/*  452 */     return super.applyImplicitComponent(type, value);
/*      */   }
/*      */ 
/*      */   
/*  456 */   private Stream<EntityReference<LivingEntity>> getTrustedEntities() { return Stream.concat(((Optional)this.entityData.get(DATA_TRUSTED_ID_0)).stream(), ((Optional)this.entityData.get(DATA_TRUSTED_ID_1)).stream()); }
/*      */ 
/*      */ 
/*      */   
/*  460 */   private void addTrustedEntity(LivingEntity entity) { addTrustedEntity(EntityReference.of(entity)); }
/*      */ 
/*      */   
/*      */   private void addTrustedEntity(EntityReference<LivingEntity> reference) {
/*  464 */     if (((Optional)this.entityData.get(DATA_TRUSTED_ID_0)).isPresent()) {
/*      */       
/*  466 */       this.entityData.set(DATA_TRUSTED_ID_1, Optional.of(reference));
/*      */     } else {
/*  468 */       this.entityData.set(DATA_TRUSTED_ID_0, Optional.of(reference));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  474 */     super.addAdditionalSaveData(output);
/*  475 */     output.store("Trusted", TRUSTED_LIST_CODEC, getTrustedEntities().toList());
/*  476 */     output.putBoolean("Sleeping", isSleeping());
/*  477 */     output.store("Type", Variant.CODEC, getVariant());
/*  478 */     output.putBoolean("Sitting", isSitting());
/*  479 */     output.putBoolean("Crouching", isCrouching());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  484 */     super.readAdditionalSaveData(input);
/*      */     
/*  486 */     clearTrusted();
/*  487 */     ((List)input.read("Trusted", TRUSTED_LIST_CODEC).orElse(List.of()))
/*  488 */       .forEach(this::addTrustedEntity);
/*      */     
/*  490 */     setSleeping(input.getBooleanOr("Sleeping", false));
/*  491 */     setVariant((Variant)input.read("Type", Variant.CODEC).orElse(Variant.DEFAULT));
/*  492 */     setSitting(input.getBooleanOr("Sitting", false));
/*  493 */     setIsCrouching(input.getBooleanOr("Crouching", false));
/*      */ 
/*      */     
/*  496 */     if (level() instanceof ServerLevel) {
/*  497 */       setTargetGoals();
/*      */     }
/*      */   }
/*      */   
/*      */   private void clearTrusted() {
/*  502 */     this.entityData.set(DATA_TRUSTED_ID_0, Optional.empty());
/*  503 */     this.entityData.set(DATA_TRUSTED_ID_1, Optional.empty());
/*      */   }
/*      */ 
/*      */   
/*  507 */   public boolean isSitting() { return getFlag(1); }
/*      */ 
/*      */ 
/*      */   
/*  511 */   public void setSitting(boolean value) { setFlag(1, value); }
/*      */ 
/*      */ 
/*      */   
/*  515 */   public boolean isFaceplanted() { return getFlag(64); }
/*      */ 
/*      */ 
/*      */   
/*  519 */   private void setFaceplanted(boolean faceplanted) { setFlag(64, faceplanted); }
/*      */ 
/*      */ 
/*      */   
/*  523 */   private boolean isDefending() { return getFlag(128); }
/*      */ 
/*      */ 
/*      */   
/*  527 */   private void setDefending(boolean defending) { setFlag(128, defending); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  532 */   public boolean isSleeping() { return getFlag(32); }
/*      */ 
/*      */ 
/*      */   
/*  536 */   private void setSleeping(boolean sleeping) { setFlag(32, sleeping); }
/*      */ 
/*      */   
/*      */   private void setFlag(int flag, boolean value) {
/*  540 */     if (value) {
/*  541 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() | flag)));
/*      */     } else {
/*  543 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & (flag ^ 0xFFFFFFFF))));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  548 */   private boolean getFlag(int flag) { return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & flag) != 0); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  553 */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) { return (slot == EquipmentSlot.MAINHAND && canPickUpLoot()); }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canHoldItem(ItemStack itemStack) {
/*  558 */     ItemStack heldItemStack = getItemBySlot(EquipmentSlot.MAINHAND);
/*      */     
/*  560 */     return (heldItemStack.isEmpty() || (this.ticksSinceEaten > 0 && isConsumableFood(itemStack) && !isConsumableFood(heldItemStack)));
/*      */   }
/*      */   
/*      */   private void spitOutItem(ItemStack itemStack) {
/*  564 */     if (itemStack.isEmpty() || level().isClientSide()) {
/*      */       return;
/*      */     }
/*      */     
/*  568 */     ItemEntity thrownItem = new ItemEntity(level(), getX() + (getLookAngle()).x, getY() + 1.0D, getZ() + (getLookAngle()).z, itemStack);
/*  569 */     thrownItem.setPickUpDelay(40);
/*  570 */     thrownItem.setThrower(this);
/*      */     
/*  572 */     playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F);
/*  573 */     level().addFreshEntity(thrownItem);
/*      */   }
/*      */   
/*      */   private void dropItemStack(ItemStack itemStack) {
/*  577 */     ItemEntity itemEntity = new ItemEntity(level(), getX(), getY(), getZ(), itemStack);
/*  578 */     level().addFreshEntity(itemEntity);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/*  583 */     ItemStack itemStack = entity.getItem();
/*  584 */     if (canHoldItem(itemStack)) {
/*  585 */       int count = itemStack.getCount();
/*  586 */       if (count > 1) {
/*  587 */         dropItemStack(itemStack.split(count - 1));
/*      */       }
/*      */       
/*  590 */       spitOutItem(getItemBySlot(EquipmentSlot.MAINHAND));
/*      */       
/*  592 */       onItemPickup(entity);
/*      */       
/*  594 */       setItemSlot(EquipmentSlot.MAINHAND, itemStack.split(1));
/*  595 */       setGuaranteedDrop(EquipmentSlot.MAINHAND);
/*  596 */       take(entity, itemStack.getCount());
/*  597 */       entity.discard();
/*  598 */       this.ticksSinceEaten = 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  604 */     super.tick();
/*      */     
/*  606 */     if (isEffectiveAi()) {
/*  607 */       boolean inWater = isInWater();
/*  608 */       if (inWater || getTarget() != null || level().isThundering()) {
/*  609 */         wakeUp();
/*      */       }
/*      */       
/*  612 */       if (inWater || isSleeping()) {
/*  613 */         setSitting(false);
/*      */       }
/*      */       
/*  616 */       if (isFaceplanted() && (level()).random.nextFloat() < 0.2F) {
/*  617 */         BlockPos pos = blockPosition();
/*  618 */         BlockState state = level().getBlockState(pos);
/*  619 */         level().levelEvent(2001, pos, Block.getId(state));
/*      */       } 
/*      */     } 
/*      */     
/*  623 */     this.interestedAngleO = this.interestedAngle;
/*  624 */     if (isInterested()) {
/*  625 */       this.interestedAngle += (1.0F - this.interestedAngle) * 0.4F;
/*      */     } else {
/*  627 */       this.interestedAngle += (0.0F - this.interestedAngle) * 0.4F;
/*      */     } 
/*      */     
/*  630 */     this.crouchAmountO = this.crouchAmount;
/*  631 */     if (isCrouching()) {
/*  632 */       this.crouchAmount += 0.2F;
/*  633 */       if (this.crouchAmount > 3.0F) {
/*  634 */         this.crouchAmount = 3.0F;
/*      */       }
/*      */     } else {
/*  637 */       this.crouchAmount = 0.0F;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  643 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.FOX_FOOD); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  648 */   protected void onOffspringSpawnedFromEgg(Player spawner, Mob offspring) { ((Fox)offspring).addTrustedEntity(spawner); }
/*      */ 
/*      */ 
/*      */   
/*  652 */   public boolean isPouncing() { return getFlag(16); }
/*      */ 
/*      */ 
/*      */   
/*  656 */   public void setIsPouncing(boolean pouncing) { setFlag(16, pouncing); }
/*      */ 
/*      */ 
/*      */   
/*  660 */   public boolean isFullyCrouched() { return (this.crouchAmount == 3.0F); }
/*      */ 
/*      */ 
/*      */   
/*  664 */   public void setIsCrouching(boolean isCrouching) { setFlag(4, isCrouching); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  669 */   public boolean isCrouching() { return getFlag(4); }
/*      */ 
/*      */ 
/*      */   
/*  673 */   public void setIsInterested(boolean value) { setFlag(8, value); }
/*      */ 
/*      */ 
/*      */   
/*  677 */   public boolean isInterested() { return getFlag(8); }
/*      */ 
/*      */ 
/*      */   
/*  681 */   public float getHeadRollAngle(float a) { return Mth.lerp(a, this.interestedAngleO, this.interestedAngle) * 0.11F * 3.1415927F; }
/*      */ 
/*      */ 
/*      */   
/*  685 */   public float getCrouchAmount(float a) { return Mth.lerp(a, this.crouchAmountO, this.crouchAmount); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTarget(LivingEntity target) {
/*  690 */     if (isDefending() && target == null) {
/*  691 */       setDefending(false);
/*      */     }
/*  693 */     super.setTarget(target);
/*      */   }
/*      */ 
/*      */   
/*  697 */   private void wakeUp() { setSleeping(false); }
/*      */ 
/*      */   
/*      */   private void clearStates() {
/*  701 */     setIsInterested(false);
/*  702 */     setIsCrouching(false);
/*  703 */     setSitting(false);
/*  704 */     setSleeping(false);
/*  705 */     setDefending(false);
/*  706 */     setFaceplanted(false);
/*      */   }
/*      */ 
/*      */   
/*  710 */   private boolean canMove() { return (!isSleeping() && !isSitting() && !isFaceplanted()); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void playAmbientSound() {
/*  715 */     SoundEvent ambient = getAmbientSound();
/*      */     
/*  717 */     if (ambient == SoundEvents.FOX_SCREECH) {
/*  718 */       playSound(ambient, 2.0F, getVoicePitch());
/*      */     } else {
/*  720 */       super.playAmbientSound();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getAmbientSound() {
/*  726 */     if (isSleeping()) {
/*  727 */       return SoundEvents.FOX_SLEEP;
/*      */     }
/*  729 */     if (!level().isBrightOutside() && this.random.nextFloat() < 0.1F) {
/*  730 */       List<Player> nearbyEntities = level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(16.0D, 16.0D, 16.0D), EntitySelector.NO_SPECTATORS);
/*  731 */       if (nearbyEntities.isEmpty()) {
/*  732 */         return SoundEvents.FOX_SCREECH;
/*      */       }
/*      */     } 
/*  735 */     return SoundEvents.FOX_AMBIENT;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  740 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.FOX_HURT; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  745 */   protected SoundEvent getDeathSound() { return SoundEvents.FOX_DEATH; }
/*      */ 
/*      */ 
/*      */   
/*  749 */   private boolean trusts(LivingEntity entity) { return getTrustedEntities().anyMatch(trusted -> trusted.matches(entity)); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void dropAllDeathLoot(ServerLevel level, DamageSource source) {
/*  754 */     ItemStack itemStack = getItemBySlot(EquipmentSlot.MAINHAND);
/*      */     
/*  756 */     if (!itemStack.isEmpty()) {
/*  757 */       spawnAtLocation(level, itemStack);
/*  758 */       setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*      */     } 
/*      */     
/*  761 */     super.dropAllDeathLoot(level, source);
/*      */   }
/*      */   
/*      */   public static boolean isPathClear(Fox fox, LivingEntity target) {
/*  765 */     double zdiff = target.getZ() - fox.getZ();
/*  766 */     double xdiff = target.getX() - fox.getX();
/*  767 */     double slope = zdiff / xdiff;
/*      */     
/*  769 */     int increments = 6;
/*  770 */     for (int i = 0; i < 6; i++) {
/*  771 */       double z = (slope == 0.0D) ? 0.0D : (zdiff * (i / 6.0F));
/*  772 */       double x = (slope == 0.0D) ? (xdiff * (i / 6.0F)) : (z / slope);
/*  773 */       for (int j = 1; j < 4; j++) {
/*  774 */         if (!fox.level().getBlockState(BlockPos.containing(fox.getX() + x, fox.getY() + j, fox.getZ() + z)).canBeReplaced()) {
/*  775 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  780 */     return true;
/*      */   }
/*      */   
/*      */   private class FoxSearchForItemsGoal
/*      */     extends Goal {
/*  785 */     public FoxSearchForItemsGoal() { setFlags(EnumSet.of(Goal.Flag.MOVE)); }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  790 */       if (!Fox.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
/*  791 */         return false;
/*      */       }
/*      */       
/*  794 */       if (Fox.this.getTarget() != null || Fox.this.getLastHurtByMob() != null) {
/*  795 */         return false;
/*      */       }
/*      */       
/*  798 */       if (!Fox.this.canMove()) {
/*  799 */         return false;
/*      */       }
/*      */       
/*  802 */       if (Fox.this.getRandom().nextInt(reducedTickDelay(10)) != 0) {
/*  803 */         return false;
/*      */       }
/*  805 */       List<ItemEntity> items = Fox.this.level().getEntitiesOfClass(ItemEntity.class, Fox.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Fox.ALLOWED_ITEMS);
/*  806 */       return (!items.isEmpty() && Fox.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty());
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/*  811 */       List<ItemEntity> items = Fox.this.level().getEntitiesOfClass(ItemEntity.class, Fox.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Fox.ALLOWED_ITEMS);
/*  812 */       ItemStack itemStack = Fox.this.getItemBySlot(EquipmentSlot.MAINHAND);
/*      */       
/*  814 */       if (itemStack.isEmpty() && !items.isEmpty()) {
/*  815 */         Fox.this.getNavigation().moveTo((Entity)items.get(0), 1.2000000476837158D);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/*  821 */       List<ItemEntity> items = Fox.this.level().getEntitiesOfClass(ItemEntity.class, Fox.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Fox.ALLOWED_ITEMS);
/*  822 */       if (!items.isEmpty())
/*  823 */         Fox.this.getNavigation().moveTo((Entity)items.get(0), 1.2000000476837158D); 
/*      */     }
/*      */   }
/*      */   
/*      */   private class FoxMoveControl
/*      */     extends MoveControl {
/*      */     public FoxMoveControl() {
/*  830 */       super(Fox.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/*  835 */       if (Fox.this.canMove())
/*  836 */         super.tick(); 
/*      */     }
/*      */   }
/*      */   
/*      */   private class StalkPreyGoal
/*      */     extends Goal
/*      */   {
/*  843 */     public StalkPreyGoal() { setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK)); }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  848 */       if (Fox.this.isSleeping()) {
/*  849 */         return false;
/*      */       }
/*      */       
/*  852 */       LivingEntity target = Fox.this.getTarget();
/*  853 */       return (target != null && target.isAlive() && Fox.STALKABLE_PREY.test(target) && Fox.this.distanceToSqr(target) > 36.0D && !Fox.this.isCrouching() && !Fox.this.isInterested() && !Fox.this.jumping);
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/*  858 */       Fox.this.setSitting(false);
/*  859 */       Fox.this.setFaceplanted(false);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void stop() {
/*  865 */       LivingEntity target = Fox.this.getTarget();
/*  866 */       if (target != null && Fox.isPathClear(Fox.this, target)) {
/*  867 */         Fox.this.setIsInterested(true);
/*  868 */         Fox.this.setIsCrouching(true);
/*  869 */         Fox.this.getNavigation().stop();
/*  870 */         Fox.this.getLookControl().setLookAt(target, Fox.this.getMaxHeadYRot(), Fox.this.getMaxHeadXRot());
/*      */       } else {
/*  872 */         Fox.this.setIsInterested(false);
/*  873 */         Fox.this.setIsCrouching(false);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/*  879 */       LivingEntity target = Fox.this.getTarget();
/*  880 */       if (target == null) {
/*      */         return;
/*      */       }
/*  883 */       Fox.this.getLookControl().setLookAt(target, Fox.this.getMaxHeadYRot(), Fox.this.getMaxHeadXRot());
/*  884 */       if (Fox.this.distanceToSqr(target) <= 36.0D) {
/*  885 */         Fox.this.setIsInterested(true);
/*  886 */         Fox.this.setIsCrouching(true);
/*  887 */         Fox.this.getNavigation().stop();
/*      */       } else {
/*  889 */         Fox.this.getNavigation().moveTo(target, 1.5D);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private class FoxMeleeAttackGoal
/*      */     extends MeleeAttackGoal {
/*  896 */     public FoxMeleeAttackGoal(double speedModifier, boolean trackTarget) { super(Fox.this, speedModifier, trackTarget); }
/*      */ 
/*      */ 
/*      */     
/*      */     protected void checkAndPerformAttack(LivingEntity target) {
/*  901 */       if (canPerformAttack(target)) {
/*  902 */         resetAttackCooldown();
/*  903 */         this.mob.doHurtTarget(getServerLevel(this.mob), target);
/*  904 */         Fox.this.playSound(SoundEvents.FOX_BITE, 1.0F, 1.0F);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/*  910 */       Fox.this.setIsInterested(false);
/*  911 */       super.start();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  916 */     public boolean canUse() { return (!Fox.this.isSitting() && !Fox.this.isSleeping() && !Fox.this.isCrouching() && !Fox.this.isFaceplanted() && super.canUse()); }
/*      */   }
/*      */   
/*      */   private class FoxBreedGoal
/*      */     extends BreedGoal
/*      */   {
/*  922 */     public FoxBreedGoal(Fox this$0, double speedModifier) { super(this$0, speedModifier); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void start() {
/*  927 */       ((Fox)this.animal).clearStates();
/*  928 */       ((Fox)this.partner).clearStates();
/*  929 */       super.start();
/*      */     }
/*      */ 
/*      */     
/*      */     protected void breed() {
/*  934 */       Fox offspring = (Fox)this.animal.getBreedOffspring(this.level, this.partner);
/*  935 */       if (offspring == null) {
/*      */         return;
/*      */       }
/*      */       
/*  939 */       ServerPlayer animalLoveCause = this.animal.getLoveCause();
/*  940 */       ServerPlayer partnerLoveCause = this.partner.getLoveCause();
/*  941 */       ServerPlayer loveCause = animalLoveCause;
/*      */       
/*  943 */       if (animalLoveCause != null) {
/*  944 */         offspring.addTrustedEntity(animalLoveCause);
/*      */       } else {
/*  946 */         loveCause = partnerLoveCause;
/*      */       } 
/*      */       
/*  949 */       if (partnerLoveCause != null && animalLoveCause != partnerLoveCause) {
/*  950 */         offspring.addTrustedEntity(partnerLoveCause);
/*      */       }
/*      */       
/*  953 */       if (loveCause != null) {
/*  954 */         loveCause.awardStat(Stats.ANIMALS_BRED);
/*  955 */         CriteriaTriggers.BRED_ANIMALS.trigger(loveCause, this.animal, this.partner, offspring);
/*      */       } 
/*      */       
/*  958 */       this.animal.setAge(6000);
/*  959 */       this.partner.setAge(6000);
/*  960 */       this.animal.resetLove();
/*  961 */       this.partner.resetLove();
/*  962 */       offspring.setAge(-24000);
/*  963 */       offspring.snapTo(this.animal.getX(), this.animal.getY(), this.animal.getZ(), 0.0F, 0.0F);
/*  964 */       this.level.addFreshEntityWithPassengers(offspring);
/*      */       
/*  966 */       this.level.broadcastEntityEvent(this.animal, (byte)18);
/*      */       
/*  968 */       if (((Boolean)this.level.getGameRules().get(GameRules.MOB_DROPS)).booleanValue())
/*  969 */         this.level.addFreshEntity(new ExperienceOrb(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), this.animal.getRandom().nextInt(7) + 1)); 
/*      */     }
/*      */   }
/*      */   
/*      */   private class DefendTrustedTargetGoal
/*      */     extends NearestAttackableTargetGoal<LivingEntity>
/*      */   {
/*      */     private LivingEntity trustedLastHurtBy;
/*      */     private LivingEntity trustedLastHurt;
/*      */     private int timestamp;
/*      */     
/*  980 */     public DefendTrustedTargetGoal(Class<LivingEntity> targetType, boolean mustSee, boolean mustReach, TargetingConditions.Selector subselector) { super(Fox.this, targetType, 10, mustSee, mustReach, subselector); }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  985 */       if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
/*  986 */         return false;
/*      */       }
/*      */       
/*  989 */       ServerLevel level = getServerLevel(Fox.this.level());
/*  990 */       for (EntityReference<LivingEntity> trustedReference : Fox.this.getTrustedEntities().toList()) {
/*  991 */         LivingEntity trustedEntity = (LivingEntity)trustedReference.getEntity(level, LivingEntity.class);
/*  992 */         if (trustedEntity != null) {
/*  993 */           this.trustedLastHurt = trustedEntity;
/*  994 */           this.trustedLastHurtBy = trustedEntity.getLastHurtByMob();
/*  995 */           int timestamp = trustedEntity.getLastHurtByMobTimestamp();
/*  996 */           return (timestamp != this.timestamp && canAttack(this.trustedLastHurtBy, this.targetConditions));
/*      */         } 
/*      */       } 
/*  999 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1004 */       setTarget(this.trustedLastHurtBy);
/* 1005 */       this.target = this.trustedLastHurtBy;
/*      */       
/* 1007 */       if (this.trustedLastHurt != null) {
/* 1008 */         this.timestamp = this.trustedLastHurt.getLastHurtByMobTimestamp();
/*      */       }
/*      */       
/* 1011 */       Fox.this.playSound(SoundEvents.FOX_AGGRO, 1.0F, 1.0F);
/*      */       
/* 1013 */       Fox.this.setDefending(true);
/*      */ 
/*      */       
/* 1016 */       Fox.this.wakeUp();
/*      */       
/* 1018 */       super.start();
/*      */     }
/*      */   }
/*      */   
/*      */   private class SeekShelterGoal extends FleeSunGoal {
/*      */     private int interval;
/*      */     
/*      */     public SeekShelterGoal(double speedModifier) {
/* 1026 */       super(Fox.this, speedModifier);
/* 1027 */       this.interval = reducedTickDelay(100);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1032 */       if (Fox.this.isSleeping() || this.mob.getTarget() != null) {
/* 1033 */         return false;
/*      */       }
/* 1035 */       if (Fox.this.level().isThundering() && Fox.this.level().canSeeSky(this.mob.blockPosition())) {
/* 1036 */         return setWantedPos();
/*      */       }
/* 1038 */       if (this.interval > 0) {
/* 1039 */         this.interval--;
/* 1040 */         return false;
/*      */       } 
/* 1042 */       this.interval = 100;
/*      */       
/* 1044 */       BlockPos pos = this.mob.blockPosition();
/*      */       
/* 1046 */       return (Fox.this.level().isBrightOutside() && Fox.this
/* 1047 */         .level().canSeeSky(pos) && 
/* 1048 */         !((ServerLevel)Fox.this.level()).isVillage(pos) && 
/* 1049 */         setWantedPos());
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1054 */       Fox.this.clearStates();
/* 1055 */       super.start();
/*      */     }
/*      */   }
/*      */   
/*      */   public class FoxAlertableEntitiesSelector
/*      */     implements TargetingConditions.Selector {
/*      */     public boolean test(LivingEntity target, ServerLevel level) {
/* 1062 */       if (target instanceof Fox) {
/* 1063 */         return false;
/*      */       }
/*      */ 
/*      */       
/* 1067 */       if (target instanceof net.minecraft.world.entity.animal.chicken.Chicken || target instanceof net.minecraft.world.entity.animal.rabbit.Rabbit || target instanceof net.minecraft.world.entity.monster.Monster) {
/* 1068 */         return true;
/*      */       }
/*      */ 
/*      */       
/* 1072 */       if (target instanceof TamableAnimal) {
/* 1073 */         return !((TamableAnimal)target).isTame();
/*      */       }
/*      */ 
/*      */       
/* 1077 */       if (target instanceof Player) { Player player = (Player)target; if (player.isSpectator() || player.isCreative()) {
/* 1078 */           return false;
/*      */         } }
/*      */ 
/*      */       
/* 1082 */       if (Fox.this.trusts(target)) {
/* 1083 */         return false;
/*      */       }
/*      */ 
/*      */       
/* 1087 */       return (!target.isSleeping() && !target.isDiscrete());
/*      */     }
/*      */   }
/*      */   
/*      */   private abstract class FoxBehaviorGoal extends Goal {
/* 1092 */     private final TargetingConditions alertableTargeting = TargetingConditions.forCombat().range(12.0D).ignoreLineOfSight().selector(new Fox.FoxAlertableEntitiesSelector(Fox.this));
/*      */     
/*      */     protected boolean hasShelter() {
/* 1095 */       BlockPos foxPos = BlockPos.containing(Fox.this.getX(), (this.this$0.getBoundingBox()).maxY, Fox.this.getZ());
/* 1096 */       return (!Fox.this.level().canSeeSky(foxPos) && Fox.this.getWalkTargetValue(foxPos) >= 0.0F);
/*      */     }
/*      */ 
/*      */     
/* 1100 */     protected boolean alertable() { return !getServerLevel(Fox.this.level()).getNearbyEntities(LivingEntity.class, this.alertableTargeting, Fox.this, Fox.this.getBoundingBox().inflate(12.0D, 6.0D, 12.0D)).isEmpty(); }
/*      */   }
/*      */   
/*      */   private class SleepGoal
/*      */     extends FoxBehaviorGoal {
/* 1105 */     private static final int WAIT_TIME_BEFORE_SLEEP = reducedTickDelay(140); private int countdown;
/*      */     
/*      */     public SleepGoal() {
/* 1108 */       super(Fox.this);
/* 1109 */       this.countdown = this$0.random.nextInt(WAIT_TIME_BEFORE_SLEEP);
/* 1110 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1115 */       if (Fox.this.xxa != 0.0F || Fox.this.yya != 0.0F || Fox.this.zza != 0.0F) {
/* 1116 */         return false;
/*      */       }
/* 1118 */       return (canSleep() || Fox.this.isSleeping());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1123 */     public boolean canContinueToUse() { return canSleep(); }
/*      */ 
/*      */     
/*      */     private boolean canSleep() {
/* 1127 */       if (this.countdown > 0) {
/* 1128 */         this.countdown--;
/* 1129 */         return false;
/*      */       } 
/* 1131 */       return (Fox.this.level().isBrightOutside() && hasShelter() && !alertable() && !Fox.this.isInPowderSnow);
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/* 1136 */       this.countdown = Fox.this.random.nextInt(WAIT_TIME_BEFORE_SLEEP);
/* 1137 */       Fox.this.clearStates();
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1142 */       Fox.this.setSitting(false);
/* 1143 */       Fox.this.setIsCrouching(false);
/* 1144 */       Fox.this.setIsInterested(false);
/* 1145 */       Fox.this.setJumping(false);
/* 1146 */       Fox.this.setSleeping(true);
/* 1147 */       Fox.this.getNavigation().stop();
/* 1148 */       Fox.this.getMoveControl().setWantedPosition(Fox.this.getX(), Fox.this.getY(), Fox.this.getZ(), 0.0D);
/*      */     } }
/*      */   
/*      */   private class PerchAndSearchGoal extends FoxBehaviorGoal {
/*      */     private double relX;
/*      */     private double relZ;
/*      */     private int lookTime;
/*      */     private int looksRemaining;
/*      */     
/*      */     public PerchAndSearchGoal() {
/* 1158 */       super(Fox.this);
/* 1159 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1164 */     public boolean canUse() { return (Fox.this.getLastHurtByMob() == null && Fox.this.getRandom().nextFloat() < 0.02F && !Fox.this.isSleeping() && Fox.this.getTarget() == null && Fox.this.getNavigation().isDone() && !alertable() && !Fox.this.isPouncing() && !Fox.this.isCrouching()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1169 */     public boolean canContinueToUse() { return (this.looksRemaining > 0); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void start() {
/* 1174 */       resetLook();
/* 1175 */       this.looksRemaining = 2 + Fox.this.getRandom().nextInt(3);
/* 1176 */       Fox.this.setSitting(true);
/* 1177 */       Fox.this.getNavigation().stop();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1182 */     public void stop() { Fox.this.setSitting(false); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1187 */       this.lookTime--;
/* 1188 */       if (this.lookTime <= 0) {
/* 1189 */         this.looksRemaining--;
/* 1190 */         resetLook();
/*      */       } 
/* 1192 */       Fox.this.getLookControl().setLookAt(Fox.this.getX() + this.relX, Fox.this.getEyeY(), Fox.this.getZ() + this.relZ, Fox.this.getMaxHeadYRot(), Fox.this.getMaxHeadXRot());
/*      */     }
/*      */     
/*      */     private void resetLook() {
/* 1196 */       double rnd = 6.283185307179586D * Fox.this.getRandom().nextDouble();
/* 1197 */       this.relX = Math.cos(rnd);
/* 1198 */       this.relZ = Math.sin(rnd);
/* 1199 */       this.lookTime = adjustedTickDelay(80 + Fox.this.getRandom().nextInt(20));
/*      */     }
/*      */   }
/*      */   
/*      */   public class FoxEatBerriesGoal
/*      */     extends MoveToBlockGoal
/*      */   {
/*      */     private static final int WAIT_TICKS = 40;
/*      */     protected int ticksWaited;
/*      */     
/* 1209 */     public FoxEatBerriesGoal(double speedModifier, int searchRange, int verticalSearchRange) { super(Fox.this, speedModifier, searchRange, verticalSearchRange); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1214 */     public double acceptedDistance() { return 2.0D; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1219 */     public boolean shouldRecalculatePath() { return (this.tryTicks % 100 == 0); }
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 1224 */       BlockState blockState = level.getBlockState(pos);
/* 1225 */       return ((blockState.is(Blocks.SWEET_BERRY_BUSH) && ((Integer)blockState.getValue(SweetBerryBushBlock.AGE)).intValue() >= 2) || 
/* 1226 */         CaveVines.hasGlowBerries(blockState));
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1231 */       if (isReachedTarget()) {
/* 1232 */         if (this.ticksWaited >= 40) {
/* 1233 */           onReachedTarget();
/*      */         } else {
/* 1235 */           this.ticksWaited++;
/*      */         } 
/* 1237 */       } else if (!isReachedTarget() && Fox.this.random.nextFloat() < 0.05F) {
/* 1238 */         Fox.this.playSound(SoundEvents.FOX_SNIFF, 1.0F, 1.0F);
/*      */       } 
/*      */       
/* 1241 */       super.tick();
/*      */     }
/*      */     
/*      */     protected void onReachedTarget() {
/* 1245 */       if (!((Boolean)getServerLevel(Fox.this.level()).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/*      */         return;
/*      */       }
/*      */       
/* 1249 */       BlockState state = Fox.this.level().getBlockState(this.blockPos);
/*      */ 
/*      */       
/* 1252 */       if (state.is(Blocks.SWEET_BERRY_BUSH)) {
/* 1253 */         pickSweetBerries(state);
/* 1254 */       } else if (CaveVines.hasGlowBerries(state)) {
/* 1255 */         pickGlowBerry(state);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1260 */     private void pickGlowBerry(BlockState state) { CaveVines.use(Fox.this, state, Fox.this.level(), this.blockPos); }
/*      */ 
/*      */     
/*      */     private void pickSweetBerries(BlockState state) {
/* 1264 */       int age = ((Integer)state.getValue(SweetBerryBushBlock.AGE)).intValue();
/* 1265 */       state.setValue(SweetBerryBushBlock.AGE, Integer.valueOf(1));
/* 1266 */       int count = 1 + (this.this$0.level()).random.nextInt(2) + ((age == 3) ? 1 : 0);
/* 1267 */       ItemStack heldItem = Fox.this.getItemBySlot(EquipmentSlot.MAINHAND);
/* 1268 */       if (heldItem.isEmpty()) {
/* 1269 */         Fox.this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.SWEET_BERRIES));
/* 1270 */         count--;
/*      */       } 
/* 1272 */       if (count > 0) {
/* 1273 */         Block.popResource(Fox.this.level(), this.blockPos, new ItemStack(Items.SWEET_BERRIES, count));
/*      */       }
/* 1275 */       Fox.this.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
/* 1276 */       Fox.this.level().setBlock(this.blockPos, (BlockState)state.setValue(SweetBerryBushBlock.AGE, Integer.valueOf(1)), 2);
/* 1277 */       Fox.this.level().gameEvent(GameEvent.BLOCK_CHANGE, this.blockPos, GameEvent.Context.of(Fox.this));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1282 */     public boolean canUse() { return (!Fox.this.isSleeping() && super.canUse()); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void start() {
/* 1287 */       this.ticksWaited = 0;
/* 1288 */       Fox.this.setSitting(false);
/* 1289 */       super.start();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class FoxGroupData extends AgeableMob.AgeableMobGroupData {
/*      */     public final Fox.Variant variant;
/*      */     
/*      */     public FoxGroupData(Fox.Variant variant) {
/* 1297 */       super(false);
/* 1298 */       this.variant = variant;
/*      */     }
/*      */   }
/*      */   
/*      */   private class FaceplantGoal
/*      */     extends Goal {
/*      */     int countdown;
/*      */     
/* 1306 */     public FaceplantGoal() { setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.JUMP, Goal.Flag.MOVE)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1311 */     public boolean canUse() { return Fox.this.isFaceplanted(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1316 */     public boolean canContinueToUse() { return (canUse() && this.countdown > 0); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1321 */     public void start() { this.countdown = adjustedTickDelay(40); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1326 */     public void stop() { Fox.this.setFaceplanted(false); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1331 */     public void tick() { this.countdown--; }
/*      */   }
/*      */   
/*      */   private class FoxPanicGoal
/*      */     extends PanicGoal
/*      */   {
/* 1337 */     public FoxPanicGoal(double speedModifier) { super(Fox.this, speedModifier); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1342 */     public boolean shouldPanic() { return (!Fox.this.isDefending() && super.shouldPanic()); }
/*      */   }
/*      */   
/*      */   private class FoxStrollThroughVillageGoal
/*      */     extends StrollThroughVillageGoal
/*      */   {
/* 1348 */     public FoxStrollThroughVillageGoal(int searchRadius, int interval) { super(Fox.this, interval); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void start() {
/* 1353 */       Fox.this.clearStates();
/* 1354 */       super.start();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1359 */     public boolean canUse() { return (super.canUse() && canFoxMove()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1364 */     public boolean canContinueToUse() { return (super.canContinueToUse() && canFoxMove()); }
/*      */ 
/*      */ 
/*      */     
/* 1368 */     private boolean canFoxMove() { return (!Fox.this.isSleeping() && !Fox.this.isSitting() && !Fox.this.isDefending() && Fox.this.getTarget() == null); }
/*      */   }
/*      */   
/*      */   private class FoxFloatGoal
/*      */     extends FloatGoal {
/*      */     public FoxFloatGoal() {
/* 1374 */       super(Fox.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1379 */       super.start();
/* 1380 */       Fox.this.clearStates();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1385 */     public boolean canUse() { return ((Fox.this.isInWater() && Fox.this.getFluidHeight(FluidTags.WATER) > 0.25D) || Fox.this.isInLava()); }
/*      */   }
/*      */   
/*      */   public class FoxPounceGoal
/*      */     extends JumpGoal
/*      */   {
/*      */     public boolean canUse() {
/* 1392 */       if (!Fox.this.isFullyCrouched()) {
/* 1393 */         return false;
/*      */       }
/*      */       
/* 1396 */       LivingEntity target = Fox.this.getTarget();
/*      */       
/* 1398 */       if (target == null || !target.isAlive()) {
/* 1399 */         return false;
/*      */       }
/*      */       
/* 1402 */       if (target.getMotionDirection() != target.getDirection()) {
/* 1403 */         return false;
/*      */       }
/*      */       
/* 1406 */       boolean hasClearPath = Fox.isPathClear(Fox.this, target);
/* 1407 */       if (!hasClearPath) {
/* 1408 */         Fox.this.getNavigation().createPath(target, 0);
/* 1409 */         Fox.this.setIsCrouching(false);
/* 1410 */         Fox.this.setIsInterested(false);
/*      */       } 
/*      */       
/* 1413 */       return hasClearPath;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1418 */       LivingEntity target = Fox.this.getTarget();
/*      */       
/* 1420 */       if (target == null || !target.isAlive()) {
/* 1421 */         return false;
/*      */       }
/*      */       
/* 1424 */       double yd = (this.this$0.getDeltaMovement()).y;
/* 1425 */       return ((yd * yd >= 0.05000000074505806D || Math.abs(Fox.this.getXRot()) >= 15.0F || !Fox.this.onGround()) && !Fox.this.isFaceplanted());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1430 */     public boolean isInterruptable() { return false; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void start() {
/* 1435 */       Fox.this.setJumping(true);
/* 1436 */       Fox.this.setIsPouncing(true);
/* 1437 */       Fox.this.setIsInterested(false);
/*      */       
/* 1439 */       LivingEntity target = Fox.this.getTarget();
/* 1440 */       if (target != null) {
/* 1441 */         Fox.this.getLookControl().setLookAt(target, 60.0F, 30.0F);
/*      */         
/* 1443 */         Vec3 uv = (new Vec3(target.getX() - Fox.this.getX(), target.getY() - Fox.this.getY(), target.getZ() - Fox.this.getZ())).normalize();
/* 1444 */         Fox.this.setDeltaMovement(Fox.this.getDeltaMovement().add(uv.x * 0.8D, 0.9D, uv.z * 0.8D));
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1451 */       Fox.this.getNavigation().stop();
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/* 1456 */       Fox.this.setIsCrouching(false);
/* 1457 */       Fox.this.crouchAmount = 0.0F;
/* 1458 */       Fox.this.crouchAmountO = 0.0F;
/* 1459 */       Fox.this.setIsInterested(false);
/* 1460 */       Fox.this.setIsPouncing(false);
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1465 */       LivingEntity target = Fox.this.getTarget();
/*      */       
/* 1467 */       if (target != null) {
/* 1468 */         Fox.this.getLookControl().setLookAt(target, 60.0F, 30.0F);
/*      */       }
/*      */       
/* 1471 */       if (!Fox.this.isFaceplanted()) {
/* 1472 */         Vec3 movement = Fox.this.getDeltaMovement();
/* 1473 */         if (movement.y * movement.y < 0.029999999329447746D && Fox.this.getXRot() != 0.0F) {
/* 1474 */           Fox.this.setXRot(Mth.rotLerp(0.2F, Fox.this.getXRot(), 0.0F));
/*      */         } else {
/* 1476 */           double direction = movement.horizontalDistance();
/* 1477 */           double rotation = Math.signum(-movement.y) * Math.acos(direction / movement.length()) * 57.2957763671875D;
/* 1478 */           Fox.this.setXRot((float)rotation);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1483 */       if (target != null && Fox.this.distanceTo(target) <= 2.0F) {
/* 1484 */         Fox.this.doHurtTarget(getServerLevel(Fox.this.level()), target);
/*      */       }
/* 1486 */       else if (Fox.this.getXRot() > 0.0F && Fox.this.onGround() && (float)(this.this$0.getDeltaMovement()).y != 0.0F && 
/* 1487 */         Fox.this.level().getBlockState(Fox.this.blockPosition()).is(Blocks.SNOW)) {
/* 1488 */         Fox.this.setXRot(60.0F);
/* 1489 */         Fox.this.setTarget(null);
/* 1490 */         Fox.this.setFaceplanted(true);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1499 */   public Vec3 getLeashOffset() { return new Vec3(0.0D, (0.55F * getEyeHeight()), (getBbWidth() * 0.4F)); }
/*      */   
/*      */   public class FoxLookControl
/*      */     extends LookControl {
/*      */     public FoxLookControl() {
/* 1504 */       super(Fox.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1509 */       if (!Fox.this.isSleeping()) {
/* 1510 */         super.tick();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1516 */     protected boolean resetXRotOnTick() { return (!Fox.this.isPouncing() && !Fox.this.isCrouching() && !Fox.this.isInterested() && !Fox.this.isFaceplanted()); }
/*      */   }
/*      */   
/*      */   private static class FoxFollowParentGoal
/*      */     extends FollowParentGoal {
/*      */     private final Fox fox;
/*      */     
/*      */     public FoxFollowParentGoal(Fox fox, double speedModifier) {
/* 1524 */       super(fox, speedModifier);
/* 1525 */       this.fox = fox;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1530 */     public boolean canUse() { return (!this.fox.isDefending() && super.canUse()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1535 */     public boolean canContinueToUse() { return (!this.fox.isDefending() && super.canContinueToUse()); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void start() {
/* 1540 */       this.fox.clearStates();
/* 1541 */       super.start();
/*      */     }
/*      */   }
/*      */   
/*      */   private class FoxLookAtPlayerGoal
/*      */     extends LookAtPlayerGoal {
/* 1547 */     public FoxLookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance) { super(mob, lookAtType, lookDistance); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1552 */     public boolean canUse() { return (super.canUse() && !Fox.this.isFaceplanted() && !Fox.this.isInterested()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1557 */     public boolean canContinueToUse() { return (super.canContinueToUse() && !Fox.this.isFaceplanted() && !Fox.this.isInterested()); }
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\fox\Fox.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */