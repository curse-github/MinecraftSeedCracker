/*     */ package net.minecraft.world.entity.animal.feline;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.TamableAnimal;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.CatLieOnBedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.CatSitOnBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.variant.SpawnContext;
/*     */ import net.minecraft.world.entity.variant.VariantUtils;
/*     */ import net.minecraft.world.food.FoodProperties;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.DyeItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.BedBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Cat
/*     */   extends TamableAnimal
/*     */ {
/*     */   public static final double TEMPT_SPEED_MOD = 0.6D;
/*     */   public static final double WALK_SPEED_MOD = 0.8D;
/*     */   public static final double SPRINT_SPEED_MOD = 1.33D;
/*  81 */   private static final EntityDataAccessor<Holder<CatVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.CAT_VARIANT);
/*  82 */   private static final EntityDataAccessor<Boolean> IS_LYING = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.BOOLEAN);
/*  83 */   private static final EntityDataAccessor<Boolean> RELAX_STATE_ONE = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.BOOLEAN);
/*  84 */   private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.INT);
/*     */   
/*  86 */   private static final ResourceKey<CatVariant> DEFAULT_VARIANT = CatVariants.BLACK;
/*  87 */   private static final DyeColor DEFAULT_COLLAR_COLOR = DyeColor.RED;
/*     */   
/*     */   private CatAvoidEntityGoal<Player> avoidPlayersGoal;
/*     */   
/*     */   private TemptGoal temptGoal;
/*     */   private float lieDownAmount;
/*     */   private float lieDownAmountO;
/*     */   private float lieDownAmountTail;
/*     */   private float lieDownAmountOTail;
/*     */   private boolean isLyingOnTopOfSleepingPlayer;
/*     */   private float relaxStateOneAmount;
/*     */   private float relaxStateOneAmountO;
/*     */   
/*     */   public Cat(EntityType<? extends Cat> type, Level level) {
/* 101 */     super(type, level);
/* 102 */     reassessTameGoals();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 107 */     this.temptGoal = new CatTemptGoal(this, 0.6D, i -> i.is(ItemTags.CAT_FOOD), true);
/*     */     
/* 109 */     this.goalSelector.addGoal(1, new FloatGoal(this));
/* 110 */     this.goalSelector.addGoal(1, new TamableAnimal.TamableAnimalPanicGoal(this, 1.5D));
/* 111 */     this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
/* 112 */     this.goalSelector.addGoal(3, new CatRelaxOnOwnerGoal(this));
/* 113 */     this.goalSelector.addGoal(4, this.temptGoal);
/* 114 */     this.goalSelector.addGoal(5, new CatLieOnBedGoal(this, 1.1D, 8));
/* 115 */     this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 5.0F));
/* 116 */     this.goalSelector.addGoal(7, new CatSitOnBlockGoal(this, 0.8D));
/* 117 */     this.goalSelector.addGoal(8, new LeapAtTargetGoal(this, 0.3F));
/* 118 */     this.goalSelector.addGoal(9, new OcelotAttackGoal(this));
/* 119 */     this.goalSelector.addGoal(10, new BreedGoal(this, 0.8D));
/* 120 */     this.goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 0.8D, 1.0000001E-5F));
/* 121 */     this.goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 10.0F));
/*     */     
/* 123 */     this.targetSelector.addGoal(1, new NonTameRandomTargetGoal(this, net.minecraft.world.entity.animal.rabbit.Rabbit.class, false, null));
/* 124 */     this.targetSelector.addGoal(1, new NonTameRandomTargetGoal(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
/*     */   }
/*     */ 
/*     */   
/* 128 */   public Holder<CatVariant> getVariant() { return (Holder)this.entityData.get(DATA_VARIANT_ID); }
/*     */ 
/*     */ 
/*     */   
/* 132 */   private void setVariant(Holder<CatVariant> variant) { this.entityData.set(DATA_VARIANT_ID, variant); }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 137 */     if (type == DataComponents.CAT_VARIANT) {
/* 138 */       return (T)castComponentValue(type, getVariant());
/*     */     }
/*     */     
/* 141 */     if (type == DataComponents.CAT_COLLAR) {
/* 142 */       return (T)castComponentValue(type, getCollarColor());
/*     */     }
/*     */     
/* 145 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 150 */     applyImplicitComponentIfPresent(components, DataComponents.CAT_VARIANT);
/* 151 */     applyImplicitComponentIfPresent(components, DataComponents.CAT_COLLAR);
/* 152 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 157 */     if (type == DataComponents.CAT_VARIANT) {
/* 158 */       setVariant((Holder)castComponentValue(DataComponents.CAT_VARIANT, value));
/* 159 */       return true;
/*     */     } 
/*     */     
/* 162 */     if (type == DataComponents.CAT_COLLAR) {
/* 163 */       setCollarColor((DyeColor)castComponentValue(DataComponents.CAT_COLLAR, value));
/* 164 */       return true;
/*     */     } 
/*     */     
/* 167 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ 
/*     */   
/* 171 */   public void setLying(boolean value) { this.entityData.set(IS_LYING, Boolean.valueOf(value)); }
/*     */ 
/*     */ 
/*     */   
/* 175 */   public boolean isLying() { return ((Boolean)this.entityData.get(IS_LYING)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 179 */   private void setRelaxStateOne(boolean value) { this.entityData.set(RELAX_STATE_ONE, Boolean.valueOf(value)); }
/*     */ 
/*     */ 
/*     */   
/* 183 */   private boolean isRelaxStateOne() { return ((Boolean)this.entityData.get(RELAX_STATE_ONE)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 187 */   public DyeColor getCollarColor() { return DyeColor.byId(((Integer)this.entityData.get(DATA_COLLAR_COLOR)).intValue()); }
/*     */ 
/*     */ 
/*     */   
/* 191 */   private void setCollarColor(DyeColor color) { this.entityData.set(DATA_COLLAR_COLOR, Integer.valueOf(color.getId())); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 196 */     super.defineSynchedData(entityData);
/*     */     
/* 198 */     entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(registryAccess(), DEFAULT_VARIANT));
/* 199 */     entityData.define(IS_LYING, Boolean.valueOf(false));
/* 200 */     entityData.define(RELAX_STATE_ONE, Boolean.valueOf(false));
/* 201 */     entityData.define(DATA_COLLAR_COLOR, Integer.valueOf(DEFAULT_COLLAR_COLOR.getId()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 206 */     super.addAdditionalSaveData(output);
/* 207 */     VariantUtils.writeVariant(output, getVariant());
/* 208 */     output.store("CollarColor", DyeColor.LEGACY_ID_CODEC, getCollarColor());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 213 */     super.readAdditionalSaveData(input);
/*     */     
/* 215 */     VariantUtils.readVariant(input, Registries.CAT_VARIANT).ifPresent(this::setVariant);
/*     */     
/* 217 */     setCollarColor((DyeColor)input.read("CollarColor", DyeColor.LEGACY_ID_CODEC).orElse(DEFAULT_COLLAR_COLOR));
/*     */   }
/*     */ 
/*     */   
/*     */   public void customServerAiStep(ServerLevel level) {
/* 222 */     if (getMoveControl().hasWanted()) {
/* 223 */       double speed = getMoveControl().getSpeedModifier();
/* 224 */       if (speed == 0.6D) {
/* 225 */         setPose(Pose.CROUCHING);
/* 226 */         setSprinting(false);
/* 227 */       } else if (speed == 1.33D) {
/* 228 */         setPose(Pose.STANDING);
/* 229 */         setSprinting(true);
/*     */       } else {
/* 231 */         setPose(Pose.STANDING);
/* 232 */         setSprinting(false);
/*     */       } 
/*     */     } else {
/* 235 */       setPose(Pose.STANDING);
/* 236 */       setSprinting(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 242 */     if (isTame()) {
/* 243 */       if (isInLove()) {
/* 244 */         return SoundEvents.CAT_PURR;
/*     */       }
/* 246 */       if (this.random.nextInt(4) == 0) {
/* 247 */         return SoundEvents.CAT_PURREOW;
/*     */       }
/* 249 */       return SoundEvents.CAT_AMBIENT;
/*     */     } 
/*     */     
/* 252 */     return SoundEvents.CAT_STRAY_AMBIENT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 257 */   public int getAmbientSoundInterval() { return 120; }
/*     */ 
/*     */ 
/*     */   
/* 261 */   public void hiss() { makeSound(SoundEvents.CAT_HISS); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 266 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.CAT_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 271 */   protected SoundEvent getDeathSound() { return SoundEvents.CAT_DEATH; }
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 275 */     return Animal.createAnimalAttributes()
/* 276 */       .add(Attributes.MAX_HEALTH, 10.0D)
/* 277 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 278 */       .add(Attributes.ATTACK_DAMAGE, 3.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 283 */   protected void playEatingSound() { playSound(SoundEvents.CAT_EAT, 1.0F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 288 */     super.tick();
/*     */     
/* 290 */     if (this.temptGoal != null && this.temptGoal.isRunning() && !isTame() && this.tickCount % 100 == 0) {
/* 291 */       playSound(SoundEvents.CAT_BEG_FOR_FOOD, 1.0F, 1.0F);
/*     */     }
/* 293 */     handleLieDown();
/*     */   }
/*     */   
/*     */   private void handleLieDown() {
/* 297 */     if ((isLying() || isRelaxStateOne()) && this.tickCount % 5 == 0) {
/* 298 */       playSound(SoundEvents.CAT_PURR, 0.6F + 0.4F * (this.random.nextFloat() - this.random.nextFloat()), 1.0F);
/*     */     }
/* 300 */     updateLieDownAmount();
/* 301 */     updateRelaxStateOneAmount();
/* 302 */     this.isLyingOnTopOfSleepingPlayer = false;
/* 303 */     if (isLying()) {
/* 304 */       BlockPos catPos = blockPosition();
/* 305 */       List<Player> players = level().getEntitiesOfClass(Player.class, (new AABB(catPos)).inflate(2.0D, 2.0D, 2.0D));
/* 306 */       for (Player player : players) {
/* 307 */         if (player.isSleeping()) {
/* 308 */           this.isLyingOnTopOfSleepingPlayer = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 316 */   public boolean isLyingOnTopOfSleepingPlayer() { return this.isLyingOnTopOfSleepingPlayer; }
/*     */ 
/*     */   
/*     */   private void updateLieDownAmount() {
/* 320 */     this.lieDownAmountO = this.lieDownAmount;
/* 321 */     this.lieDownAmountOTail = this.lieDownAmountTail;
/* 322 */     if (isLying()) {
/* 323 */       this.lieDownAmount = Math.min(1.0F, this.lieDownAmount + 0.15F);
/* 324 */       this.lieDownAmountTail = Math.min(1.0F, this.lieDownAmountTail + 0.08F);
/*     */     } else {
/* 326 */       this.lieDownAmount = Math.max(0.0F, this.lieDownAmount - 0.22F);
/* 327 */       this.lieDownAmountTail = Math.max(0.0F, this.lieDownAmountTail - 0.13F);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateRelaxStateOneAmount() {
/* 332 */     this.relaxStateOneAmountO = this.relaxStateOneAmount;
/* 333 */     if (isRelaxStateOne()) {
/* 334 */       this.relaxStateOneAmount = Math.min(1.0F, this.relaxStateOneAmount + 0.1F);
/*     */     } else {
/* 336 */       this.relaxStateOneAmount = Math.max(0.0F, this.relaxStateOneAmount - 0.13F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 341 */   public float getLieDownAmount(float a) { return Mth.lerp(a, this.lieDownAmountO, this.lieDownAmount); }
/*     */ 
/*     */ 
/*     */   
/* 345 */   public float getLieDownAmountTail(float a) { return Mth.lerp(a, this.lieDownAmountOTail, this.lieDownAmountTail); }
/*     */ 
/*     */ 
/*     */   
/* 349 */   public float getRelaxStateOneAmount(float a) { return Mth.lerp(a, this.relaxStateOneAmountO, this.relaxStateOneAmount); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Cat getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 354 */     Cat baby = (Cat)EntityType.CAT.create(level, EntitySpawnReason.BREEDING);
/* 355 */     if (baby != null && partner instanceof Cat) { Cat partnerCat = (Cat)partner;
/* 356 */       if (this.random.nextBoolean()) {
/* 357 */         baby.setVariant(getVariant());
/*     */       } else {
/* 359 */         baby.setVariant(partnerCat.getVariant());
/*     */       } 
/*     */       
/* 362 */       if (isTame()) {
/* 363 */         baby.setOwnerReference(getOwnerReference());
/* 364 */         baby.setTame(true, true);
/* 365 */         DyeColor parent1CollarColor = getCollarColor();
/* 366 */         DyeColor parent2CollarColor = partnerCat.getCollarColor();
/* 367 */         baby.setCollarColor(DyeColor.getMixedColor(level, parent1CollarColor, parent2CollarColor));
/*     */       }  }
/*     */ 
/*     */     
/* 371 */     return baby;
/*     */   }
/*     */   
/*     */   public boolean canMate(Animal partner) {
/*     */     Cat cat;
/* 376 */     if (!isTame()) {
/* 377 */       return false;
/*     */     }
/*     */     
/* 380 */     if (partner instanceof Cat) { cat = (Cat)partner; }
/* 381 */     else { return false; }
/*     */ 
/*     */     
/* 384 */     return (cat.isTame() && super.canMate(partner));
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 389 */     groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */     
/* 391 */     VariantUtils.selectVariantToSpawn(SpawnContext.create(level, blockPosition()), Registries.CAT_VARIANT).ifPresent(this::setVariant);
/*     */     
/* 393 */     return groupData;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 398 */     ItemStack itemStack = player.getItemInHand(hand);
/* 399 */     Item item = itemStack.getItem();
/*     */     
/* 401 */     if (isTame()) {
/* 402 */       if (isOwnedBy(player)) {
/* 403 */         if (item instanceof DyeItem) { DyeItem dyeItem = (DyeItem)item;
/* 404 */           DyeColor color = dyeItem.getDyeColor();
/* 405 */           if (color != getCollarColor()) {
/* 406 */             if (!level().isClientSide()) {
/* 407 */               setCollarColor(color);
/* 408 */               itemStack.consume(1, player);
/* 409 */               setPersistenceRequired();
/*     */             } 
/* 411 */             return InteractionResult.SUCCESS;
/*     */           }  }
/* 413 */         else if (isFood(itemStack) && getHealth() < getMaxHealth())
/* 414 */         { if (!level().isClientSide()) {
/* 415 */             usePlayerItem(player, hand, itemStack);
/* 416 */             FoodProperties foodProperties = (FoodProperties)itemStack.get(DataComponents.FOOD);
/* 417 */             heal((foodProperties != null) ? foodProperties.nutrition() : 1.0F);
/* 418 */             playEatingSound();
/*     */           } 
/* 420 */           return InteractionResult.SUCCESS; }
/*     */ 
/*     */         
/* 423 */         InteractionResult parentInteraction = super.mobInteract(player, hand);
/* 424 */         if (!parentInteraction.consumesAction()) {
/* 425 */           setOrderedToSit(!isOrderedToSit());
/* 426 */           return InteractionResult.SUCCESS;
/*     */         } 
/* 428 */         return parentInteraction;
/*     */       }
/*     */     
/* 431 */     } else if (isFood(itemStack)) {
/* 432 */       if (!level().isClientSide()) {
/* 433 */         usePlayerItem(player, hand, itemStack);
/* 434 */         tryToTame(player);
/* 435 */         setPersistenceRequired();
/* 436 */         playEatingSound();
/*     */       } 
/* 438 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 441 */     InteractionResult interact = super.mobInteract(player, hand);
/* 442 */     if (interact.consumesAction()) {
/* 443 */       setPersistenceRequired();
/*     */     }
/* 445 */     return interact;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 450 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.CAT_FOOD); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 455 */   public boolean removeWhenFarAway(double distSqr) { return (!isTame() && this.tickCount > 2400); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTame(boolean isTame, boolean includeSideEffects) {
/* 460 */     super.setTame(isTame, includeSideEffects);
/* 461 */     reassessTameGoals();
/*     */   }
/*     */   
/*     */   protected void reassessTameGoals() {
/* 465 */     if (this.avoidPlayersGoal == null) {
/* 466 */       this.avoidPlayersGoal = new CatAvoidEntityGoal(this, Player.class, 16.0F, 0.8D, 1.33D);
/*     */     }
/*     */     
/* 469 */     this.goalSelector.removeGoal(this.avoidPlayersGoal);
/*     */     
/* 471 */     if (!isTame()) {
/* 472 */       this.goalSelector.addGoal(4, this.avoidPlayersGoal);
/*     */     }
/*     */   }
/*     */   
/*     */   private void tryToTame(Player player) {
/* 477 */     if (this.random.nextInt(3) == 0) {
/* 478 */       tame(player);
/* 479 */       setOrderedToSit(true);
/* 480 */       level().broadcastEntityEvent(this, (byte)7);
/*     */     } else {
/* 482 */       level().broadcastEntityEvent(this, (byte)6);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 488 */   public boolean isSteppingCarefully() { return (isCrouching() || super.isSteppingCarefully()); }
/*     */   
/*     */   private static class CatAvoidEntityGoal<T extends LivingEntity>
/*     */     extends AvoidEntityGoal<T> {
/*     */     private final Cat cat;
/*     */     
/*     */     public CatAvoidEntityGoal(Cat cat, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
/* 495 */       super(cat, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
/* 496 */       this.cat = cat;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 501 */     public boolean canUse() { return (!this.cat.isTame() && super.canUse()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 506 */     public boolean canContinueToUse() { return (!this.cat.isTame() && super.canContinueToUse()); }
/*     */   }
/*     */   
/*     */   private static class CatTemptGoal
/*     */     extends TemptGoal {
/*     */     private Player selectedPlayer;
/*     */     private final Cat cat;
/*     */     
/*     */     public CatTemptGoal(Cat mob, double speedModifier, Predicate<ItemStack> items, boolean canScare) {
/* 515 */       super(mob, speedModifier, items, canScare);
/* 516 */       this.cat = mob;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 521 */       super.tick();
/*     */       
/* 523 */       if (this.selectedPlayer == null && this.mob.getRandom().nextInt(adjustedTickDelay(600)) == 0) {
/* 524 */         this.selectedPlayer = this.player;
/* 525 */       } else if (this.mob.getRandom().nextInt(adjustedTickDelay(500)) == 0) {
/* 526 */         this.selectedPlayer = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean canScare() {
/* 532 */       if (this.selectedPlayer != null && this.selectedPlayer.equals(this.player)) {
/* 533 */         return false;
/*     */       }
/*     */       
/* 536 */       return super.canScare();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 541 */     public boolean canUse() { return (super.canUse() && !this.cat.isTame()); }
/*     */   }
/*     */   
/*     */   private static class CatRelaxOnOwnerGoal
/*     */     extends Goal
/*     */   {
/*     */     private final Cat cat;
/*     */     private Player ownerPlayer;
/*     */     private BlockPos goalPos;
/*     */     private int onBedTicks;
/*     */     
/* 552 */     public CatRelaxOnOwnerGoal(Cat cat) { this.cat = cat; }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 557 */       if (!this.cat.isTame()) {
/* 558 */         return false;
/*     */       }
/*     */       
/* 561 */       if (this.cat.isOrderedToSit()) {
/* 562 */         return false;
/*     */       }
/*     */       
/* 565 */       LivingEntity owner = this.cat.getOwner();
/* 566 */       if (owner instanceof Player) { Player playerOwner = (Player)owner;
/* 567 */         this.ownerPlayer = playerOwner;
/*     */         
/* 569 */         if (!owner.isSleeping()) {
/* 570 */           return false;
/*     */         }
/*     */         
/* 573 */         if (this.cat.distanceToSqr(this.ownerPlayer) > 100.0D) {
/* 574 */           return false;
/*     */         }
/*     */         
/* 577 */         BlockPos ownerPos = this.ownerPlayer.blockPosition();
/* 578 */         BlockState ownerPosState = this.cat.level().getBlockState(ownerPos);
/* 579 */         if (ownerPosState.is(BlockTags.BEDS)) {
/* 580 */           this.goalPos = (BlockPos)ownerPosState.getOptionalValue(BedBlock.FACING).map(bedDir -> ownerPos.relative(bedDir.getOpposite())).orElseGet(() -> new BlockPos(ownerPos));
/* 581 */           return !spaceIsOccupied();
/*     */         }  }
/*     */       
/* 584 */       return false;
/*     */     }
/*     */     
/*     */     private boolean spaceIsOccupied() {
/* 588 */       List<Cat> cats = this.cat.level().getEntitiesOfClass(Cat.class, (new AABB(this.goalPos)).inflate(2.0D));
/* 589 */       for (Cat otherCat : cats) {
/* 590 */         if (otherCat != this.cat && (otherCat.isLying() || otherCat.isRelaxStateOne())) {
/* 591 */           return true;
/*     */         }
/*     */       } 
/* 594 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 599 */     public boolean canContinueToUse() { return (this.cat.isTame() && !this.cat.isOrderedToSit() && this.ownerPlayer != null && this.ownerPlayer.isSleeping() && this.goalPos != null && !spaceIsOccupied()); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() {
/* 604 */       if (this.goalPos != null) {
/* 605 */         this.cat.setInSittingPose(false);
/* 606 */         this.cat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.100000023841858D);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 612 */       this.cat.setLying(false);
/*     */       
/* 614 */       if (this.ownerPlayer.getSleepTimer() >= 100 && this.cat
/* 615 */         .level().getRandom().nextFloat() < ((Float)this.cat.level().environmentAttributes().getValue(EnvironmentAttributes.CAT_WAKING_UP_GIFT_CHANCE, this.cat.position())).floatValue()) {
/* 616 */         giveMorningGift();
/*     */       }
/*     */       
/* 619 */       this.onBedTicks = 0;
/* 620 */       this.cat.setRelaxStateOne(false);
/* 621 */       this.cat.getNavigation().stop();
/*     */     }
/*     */     
/*     */     private void giveMorningGift() {
/* 625 */       RandomSource random = this.cat.getRandom();
/* 626 */       BlockPos.MutableBlockPos catPos = new BlockPos.MutableBlockPos();
/* 627 */       catPos.set(this.cat.isLeashed() ? this.cat.getLeashHolder().blockPosition() : this.cat.blockPosition());
/* 628 */       this.cat.randomTeleport((catPos.getX() + random.nextInt(11) - 5), (catPos.getY() + random.nextInt(5) - 2), (catPos.getZ() + random.nextInt(11) - 5), false);
/*     */       
/* 630 */       catPos.set(this.cat.blockPosition());
/* 631 */       this.cat.dropFromGiftLootTable(getServerLevel(this.cat), BuiltInLootTables.CAT_MORNING_GIFT, (level, itemStack) -> 
/* 632 */           level.addFreshEntity(new ItemEntity(level, catPos.getX() - Mth.sin((this.cat.yBodyRot * 0.017453292F)), catPos.getY(), catPos.getZ() + Mth.cos((this.cat.yBodyRot * 0.017453292F)), itemStack)));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 638 */       if (this.ownerPlayer != null && this.goalPos != null) {
/* 639 */         this.cat.setInSittingPose(false);
/* 640 */         this.cat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.100000023841858D);
/* 641 */         if (this.cat.distanceToSqr(this.ownerPlayer) < 2.5D) {
/* 642 */           this.onBedTicks++;
/* 643 */           if (this.onBedTicks > adjustedTickDelay(16)) {
/* 644 */             this.cat.setLying(true);
/* 645 */             this.cat.setRelaxStateOne(false);
/*     */           } else {
/* 647 */             this.cat.lookAt(this.ownerPlayer, 45.0F, 45.0F);
/* 648 */             this.cat.setRelaxStateOne(true);
/*     */           } 
/*     */         } else {
/* 651 */           this.cat.setLying(false);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\feline\Cat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */